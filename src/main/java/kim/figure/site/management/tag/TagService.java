package kim.figure.site.management.tag;

import kim.figure.site.common.content.Content;
import kim.figure.site.management.content.ContentDto;
import kim.figure.site.management.content.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * author         : walker
 * date           : 2022. 12. 15.
 * description    :
 */
@Service
public class TagService {
    @Autowired
    TagRepository tagRepository;
    @Autowired
    ContentRepository contentRepository;

    public Mono<Void> deleteTag(String id) {
        return tagRepository.deleteById(id);
    }

    public Mono<Long> getTagCount() {
        return tagRepository.count();
    }

    public Flux<TagDto.Get> getTagList(MultiValueMap<String, String> params) {
        var pageRequest = PageRequest.of(Integer.parseInt(params.get("offset").get(0)), Integer.parseInt(params.get("limit").get(0)), Sort.by("id").descending());
        Flux<TagDto.Get> tagFlux =  tagRepository.findBy(pageRequest).flatMap(tag -> contentRepository.findByTagList(tag)
                .map(content -> ContentDto.TagRef.builder().id(content.getId()).title(content.getTitle()).build())
                .collectList().map(contentList -> TagDto.Get.builder().contentList(contentList).id(tag.getId()).build()));
        return tagFlux;
    }

    public Flux<Content> contentByTag(String id) {
        return tagRepository.findById(id).flatMapMany(tag -> contentRepository.findByTagList(tag));
    }

    public Mono<Long> getTagContentCount(String id) {
        return tagRepository.findById(id).flatMap(tag -> contentRepository.countByTagList(tag));
    }
}
