package kim.figure.site.management.content;

import kim.figure.site.common.content.Content;
import kim.figure.site.common.tag.Tag;
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
import reactor.util.function.Tuples;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

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

    @Value("${project.static.path}")
    String projectStaticPath;

    public Mono<Content> putContent(Mono<ContentDto.Put> monoDto, Long id) {
        return monoDto
                .map(dto -> {
                    System.out.println(dto);
                    validationUtil.validate(dto);
                    System.out.println(dto);
                    dto.setId(id);
                    Content postEntity = ContentMapper.INSTANCE.contentPutToEntity(dto);
                    System.out.println(postEntity);
                    saveTag(postEntity.getTagList());
                    return postEntity;
                })
                .flatMap(contentEntity -> contentRepository.save(contentEntity));
    }


    public Mono<Content> postContent(Mono<ContentDto.Post> postMono) {
        return postMono.map(dto -> {
                    validationUtil.validate(dto);
                    Content content = ContentMapper.INSTANCE.contentPostToEntity(dto);
                    long seq = contentSequenceService.getSeq(content.SEQUENCE_NAME);
                    content.setId(seq);
                    saveTag(dto.getTagList());
                    return Tuples.of(content, dto.getId());
                }).flatMap(tuple2->{
                    //copy temp images to new content id directory
                    final String tempImageDir = projectStaticPath + "/assets/image/" + tuple2.getT2();
                    Path tempImagePath = Path.of(tempImageDir);
                    boolean tempImageDirectoryExists = Files.exists(tempImagePath);
                    if(!tempImageDirectoryExists){
                        //temp content has no images
                    }else{
                        final String destImageDir = projectStaticPath + "/assets/image/" + tuple2.getT1().getId();
                        try {
                            Files.move(tempImagePath, Path.of(destImageDir));
                        } catch (IOException e) {
                            return Mono.error(new RuntimeException("Cannot move image directory [" + tempImagePath +"] to ["+destImageDir+"]" ));
                        }
                    }
                    tuple2.getT1().setRawContent(tuple2.getT1().getRawContent().replaceAll("/assets/image/"+tuple2.getT2(),"/assets/image/"+tuple2.getT1().getId()));
                    tuple2.getT1().setRenderedContent(tuple2.getT1().getRenderedContent().replaceAll("/assets/image/"+tuple2.getT2(),"/assets/image/"+tuple2.getT1().getId()));
                    return Mono.just(tuple2);
                })
                .flatMap(tuple2 -> contentRepository.findById(tuple2.getT2()).flatMap(tempContent -> {
                    Content newContent = tuple2.getT1();
                    newContent.setCreatedAt(tuple2.getT1().getCreatedAt());
                    newContent.setIsDraft(false);
                    newContent.setLastModifiedAt(Instant.now());
                    if (newContent.getIsPublished()) {
                        newContent.setPublishAt(Instant.now());
                    }
                    if (tuple2.getT1().getTagList() != null) {
                        tagRepository.saveAll(tuple2.getT1().getTagList().stream().map(tag->{
                            tag.setId(tag.getId().replaceAll(" ", ""));
                            return tag;
                        }).distinct().toList()).subscribe();
                    }
                    return contentRepository.save(newContent).doOnSuccess(i-> contentRepository.deleteById(tuple2.getT2()));
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
        System.out.println("getContentList");
        var pageRequest = PageRequest.of(Integer.parseInt(params.get("offset").get(0)), Integer.parseInt(params.get("limit").get(0)), Sort.by("id").descending());
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
}
