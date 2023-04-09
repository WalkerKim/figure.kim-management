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
                    saveTagToTagRepository(dto.getTagList()).subscribe();
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
                .flatMap(dto-> Mono.zip(Mono.just(dto), contentSequenceService.getSeq(Content.SEQUENCE_NAME)))
                .flatMap(tuple2->{
                    //get new content id
                    //copy temp images to new content id directory
                    final String tempImageDir = projectStaticPath + "/assets/image/" + tuple2.getT2();
                    Path tempImagePath = Path.of(tempImageDir);
                    boolean tempImageDirectoryExists = Files.exists(tempImagePath);
                    if(!tempImageDirectoryExists){
                        //temp content has no images
                    }else{
                        final String destImageDir = projectStaticPath + "/assets/image/" + tuple2.getT2();
                        try {
                            Files.move(tempImagePath, Path.of(destImageDir));
                        } catch (IOException e) {
                            return Mono.error(new RuntimeException("Cannot move image directory [" + tempImagePath +"] to ["+destImageDir+"]" ));
                        }
                    }
                    Content content = ContentMapper.INSTANCE.contentPostToEntity(tuple2.getT1());
                    content.setRawContent(content.getRawContent().replaceAll("/assets/image/"+tuple2.getT1().getId(),"/assets/image/"+tuple2.getT2()));
                    content.setRenderedContent(content.getRenderedContent().replaceAll("/assets/image/"+tuple2.getT1().getId(),"/assets/image/"+tuple2.getT2()));
                    if(content.getDescription()==null || content.getDescription().trim().equals("")){
                        content.setDescription(
                                extractDescriptionFromHtml(content.getRenderedContent())
                        );
                    }
                    saveTagToTagRepository(tuple2.getT1().getTagList()).subscribe();
                    content.setId(tuple2.getT2());
                    return Mono.zip(Mono.just(content), Mono.just(tuple2.getT1().getId()), categoryRepository.findAllById(tuple2.getT1().getCategoryIdList()).collectList());
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
                    return contentRepository.save(newContent).doOnSuccess(i-> {
                        contentRepository.deleteById(tuple3.getT2()).subscribe();
                    });
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
        String sortColumnName = params.getOrDefault("order",List.of("id")).get(0);
        String sortDirection = params.getOrDefault("dir",List.of("desc")).get(0);
        var sort = Sort.by(sortColumnName);
        if(sortDirection.equals("desc")){
            sort = sort.descending();
        }else if(sortDirection.equals("asc")){
            sort = sort.ascending();
        }
        var pageRequest = PageRequest.of(Integer.parseInt(params.get("offset").get(0)), Integer.parseInt(params.get("limit").get(0)), sort);
        Flux<Content> contentEntityFlux = Mono.just(pageRequest).flatMapMany(contentRepository::findBy);
        return contentEntityFlux;
    }

    private Flux<Tag> saveTagToTagRepository(List<Tag> tagList){
        return tagRepository.saveAll(tagList.stream().map(tag->{
            tag.setId(tag.getId().replaceAll(" ", ""));
            return tag;
        }).distinct().toList());
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
