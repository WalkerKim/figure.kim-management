package kim.figure.site.management.content;

import kim.figure.site.common.content.Content;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

/**
 * author         : walker
 * date           : 2022. 08. 28.
 * description    :
 */
public interface ContentRepository extends R2dbcRepository<Content, String> {
    Flux<Content> findBy(Pageable pageable);
}
