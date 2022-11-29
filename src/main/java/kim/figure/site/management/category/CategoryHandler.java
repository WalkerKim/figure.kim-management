package kim.figure.site.management.category;

import kim.figure.site.common.category.Category;
import kim.figure.site.common.content.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * author         : walker
 * date           : 2022. 11. 29.
 * description    :
 */
@Component
public class CategoryHandler {
    public <T extends ServerResponse> Mono<T> postCategory(ServerRequest serverRequest) {
        return null;
    }

    public Mono<ServerResponse> getCategoryList(ServerRequest serverRequest) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(getCategoryFlux(), Content.class);
    }

    private static Flux<Category> getCategoryFlux() {
        return Flux.just(
                Category.builder().id("dev").depth(0).name("개발").childCategoryList(
                        List.of(Category.builder().id("java").depth(1).name("Java").build(),
                                Category.builder().id("javascript").depth(1).name("JavaScript").build(),
                                Category.builder().id("Spring").depth(1).name("Spring").build())
                ).build(),
                Category.builder().id("lifestyle").depth(0).name("라이프스타일").childCategoryList(
                        List.of(Category.builder().id("cook").depth(1).name("요리").build(),
                                Category.builder().id("photograph").depth(1).name("사진").build(),
                                Category.builder().id("it").depth(1).name("it").build())
                ).build(),
                Category.builder().id("startup").depth(0).name("스타트업").childCategoryList(
                        List.of(Category.builder().id("organizational-culture").depth(1).name("조직문화").build(),
                                Category.builder().id("personnel").depth(1).name("인사").build(),
                                Category.builder().id("how-to-start-startup").depth(1).name("How to Start Startup").build())
                ).build()


        );
    }

    public Mono<ServerResponse> getContent(ServerRequest serverRequest) {
        return null;
    }

    public Mono<ServerResponse> putContent(ServerRequest serverRequest) {
        return null;
    }

    public Mono<ServerResponse> deleteContent(ServerRequest serverRequest) {
        return null;
    }


}
