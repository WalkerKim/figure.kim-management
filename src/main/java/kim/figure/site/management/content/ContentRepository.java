package kim.figure.site.management.content;

import kim.figure.site.common.category.Category;
import kim.figure.site.common.content.Content;
import kim.figure.site.common.tag.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * author         : walker
 * date           : 2022. 08. 28.
 * description    :
 */
public interface ContentRepository extends ReactiveMongoRepository<Content, Long> {
    Flux<Content> findBy(Pageable pageable);

    Flux<Content> deleteByIsDraft(boolean b);

    Flux<Content> deleteByIsDraftAndRawContent(boolean b, Object o);

    Flux<Content> findByTagList(Tag tag);

    Mono<Long> countByTagList(Tag tag);

    Flux<Content> findByCategoryList(Category c);
}
