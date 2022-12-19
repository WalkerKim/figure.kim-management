package kim.figure.site.management.category;

import kim.figure.site.common.category.Category;
import kim.figure.site.common.content.Content;
import kim.figure.site.management.common.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static kim.figure.site.management.common.HttpUtils.okResponse;

/**
 * author         : walker
 * date           : 2022. 11. 29.
 * description    :
 */
@Component
public class CategoryHandler {

    @Autowired
    ValidationUtil validationUtil;

    @Autowired
    CategoryService categoryService;

    public Mono<ServerResponse> postCategory(ServerRequest serverRequest) {
        return okResponse(categoryService.postCategory(serverRequest.bodyToMono(CategoryDto.Post.class).map(validationUtil::validateWithIdentity)), Category.class);
    }

    public Mono<ServerResponse> getCategoryList(ServerRequest serverRequest) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(categoryService.getCategoryFlux(), Content.class);
    }

    public Mono<ServerResponse> deleteCategory(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return okResponse(categoryService.deleteCategory(id), Void.class);
    }
}
