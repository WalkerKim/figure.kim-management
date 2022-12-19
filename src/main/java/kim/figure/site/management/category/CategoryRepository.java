package kim.figure.site.management.category;

import kim.figure.site.common.category.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * author         : walker
 * date           : 2022. 12. 19.
 * description    :
 */
public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {

    Flux<Category> findByParentCategory(Mono<Category> parentCategory);

    Flux<Category> findByDepth(int i);
}
