package kim.figure.site.management.content;

import kim.figure.site.common.content.Content;
import kim.figure.site.common.tag.Tag;
import kim.figure.site.management.category.CategoryRepository;
import kim.figure.site.management.common.ValidationUtil;
import kim.figure.site.management.tag.TagRepository;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static kim.figure.site.management.common.JsoupUtils.extractDescriptionFromHtml;

/**
 * author         : walker
 * date           : 2022. 12. 08.
 * description    :
 */
@Service
public class ContentService {

    @Autowired
    ValidationUtil validationUtil;

    @Autowired
    ContentRepository contentRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    ContentSequenceService contentSequenceService;

    @Autowired
    CategoryRepository categoryRepository;

    @Value("${project.static.path}")
    String projectStaticPath;

    public Mono<Content> putContent(Mono<ContentDto.Put> monoDto, Long id) {
        return monoDto.map(validationUtil::validateWithIdentity)
                .map(content->{
                    if(content.getDescription()==null || content.getDescription().trim().equals("")){

                        content.setDescription(
                                extractDescriptionFromHtml(content.getRenderedContent())
                        );

                    }
                    return content;
                })
                .flatMap(dto-> {
                    dto.setId(id);
                    return Mono.zip(Mono.just(ContentMapper.INSTANCE.contentPutToEntity(dto)), categoryRepository.findAllById(dto.getCategoryIdList()).collectList());
                })
                .map(tuple2 -> {
                    var content = tuple2.getT1();
                    content.setCategoryList(tuple2.getT2());
                    return content;
                })
                .flatMap(contentEntity -> contentRepository.save(contentEntity));
    }


    public Mono<Content> postContent(Mono<ContentDto.Post> postMono) {
        return postMono.map(validationUtil::validateWithIdentity)
                .flatMap(dto->{
                    long newId = contentSequenceService.getSeq(Content.SEQUENCE_NAME);
                    //copy temp images to new content id directory
                    final String tempImageDir = projectStaticPath + "/assets/image/" + dto.getId();
                    Path tempImagePath = Path.of(tempImageDir);
                    boolean tempImageDirectoryExists = Files.exists(tempImagePath);
                    if(!tempImageDirectoryExists){
                        //temp content has no images
                    }else{
                        final String destImageDir = projectStaticPath + "/assets/image/" + newId;
                        try {
                            Files.move(tempImagePath, Path.of(destImageDir));
                        } catch (IOException e) {
                            return Mono.error(new RuntimeException("Cannot move image directory [" + tempImagePath +"] to ["+destImageDir+"]" ));
                        }
                    }
                    Content content = ContentMapper.INSTANCE.contentPostToEntity(dto);
                    content.setRawContent(content.getRawContent().replaceAll("/assets/image/"+dto.getId(),"/assets/image/"+newId));
                    content.setRenderedContent(content.getRenderedContent().replaceAll("/assets/image/"+dto.getId(),"/assets/image/"+newId));
                    if(content.getDescription()==null || content.getDescription().trim().equals("")){
                        content.setDescription(
                                extractDescriptionFromHtml(content.getRenderedContent())
                        );
                    }
                    saveTag(dto.getTagList());
                    content.setId(newId);
                    return Mono.zip(Mono.just(content), Mono.just(dto.getId()), categoryRepository.findAllById(dto.getCategoryIdList()).collectList());
                })
                .flatMap(tuple3 -> contentRepository.findById(tuple3.getT2()).flatMap(tempContent -> {
                    Content newContent = tuple3.getT1();
                    newContent.setCreatedAt(tuple3.getT1().getCreatedAt());
                    newContent.setIsDraft(false);
                    newContent.setLastModifiedAt(Instant.now());
                    newContent.setCategoryList(tuple3.getT3());
                    if (newContent.getIsPublished()) {
                        newContent.setPublishedAt(Instant.now());
                    }
                    if (tuple3.getT1().getTagList() != null) {
                        tagRepository.saveAll(tuple3.getT1().getTagList().stream().map(tag->{
                            tag.setId(tag.getId().replaceAll(" ", ""));
                            return tag;
                        }).distinct().toList()).subscribe();
                    }
                    return contentRepository.save(newContent).doOnSuccess(i-> contentRepository.deleteById(tuple3.getT2()));
                }));
    }

    private Mono<Void> deleteFile(File file) {
        return Mono.fromRunnable(() -> {
            try {
                Files.deleteIfExists(file.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).then();

    }

    public Publisher<Content> getContentList(MultiValueMap<String, String> params ) {
        var pageRequest = PageRequest.of(Integer.parseInt(params.get("offset").get(0)), Integer.parseInt(params.get("limit").get(0)), Sort.by("id").ascending());
        Flux<Content> contentEntityFlux = Mono.just(pageRequest).flatMapMany(contentRepository::findBy);
        return contentEntityFlux;
    }

    private void saveTag(List<Tag> tagList){
        tagRepository.saveAll(tagList.stream().map(tag->{
            tag.setId(tag.getId().replaceAll(" ", ""));
            return tag;
        }).distinct().toList()).subscribe();
    }

    public Mono<Long> getContentCount() {
        return contentRepository.count();

    }

    public Mono<ContentDto.TempGet> postTempContent() {
        return contentRepository.save(Content.builder().id(Instant.now().getEpochSecond()).lastModifiedAt(Instant.now()).createdAt(Instant.now()).isPublished(false).isDraft(true).categoryList(new ArrayList<>()).build()).map(content->{
            ContentDto.TempGet getDto = ContentMapper.INSTANCE.contentEntityToGet(content);
            getDto.setCategoryIdList(content.getCategoryList().stream().map(i -> i.getId()).toList());
            return getDto;
        });

    }
}
