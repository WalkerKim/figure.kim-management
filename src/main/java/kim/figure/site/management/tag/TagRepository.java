package kim.figure.site.management.tag;

import kim.figure.site.common.tag.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 * author         : walker
 * date           : 2022. 12. 15.
 * description    :
 */
public interface TagRepository extends ReactiveMongoRepository<Tag, String> {

    Flux<Tag> findBy(Pageable pageable);

}
