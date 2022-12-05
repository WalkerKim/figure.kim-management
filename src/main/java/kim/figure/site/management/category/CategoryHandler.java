package kim.figure.site.management.category;

import kim.figure.site.common.category.Category;
import kim.figure.site.common.content.Content;
import kim.figure.site.management.common.ValidationUtil;
import kim.figure.site.management.content.ContentDto;
import kim.figure.site.management.content.ContentMapper;
import kim.figure.site.management.content.ContentRepository;
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

    @Autowired
    ValidationUtil validationUtil;

    @Autowired
    ContentRepository contentRepository;

    public <T extends ServerResponse> Mono<T> postCategory(ServerRequest serverRequest) {
//        return serverRequest.bodyToMono(ContentDto.Post.class)
//                .map(dto->{
//                    validationUtil.validate(dto);
//                    Content content = ContentMapper.INSTANCE.contentPostToEntity(dto);
//
//
//                    return content;
//                })
//                .flatMap(content -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(contentRepository.save(content), Content.class));
        return null;
    }

    public Mono<ServerResponse> getCategoryList(ServerRequest serverRequest) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(getCategoryFlux(), Content.class);
    }

    private static Flux<CategoryDto.Get> getCategoryFlux() {
        return Flux.just(
                CategoryDto.Get.builder().id("dev").depth(0).name("개발").childCategoryList(
                        List.of(CategoryDto.Get.builder().id("java").depth(1).name("Java").build(),
                                CategoryDto.Get.builder().id("javascript").depth(1).name("JavaScript").build(),
                                CategoryDto.Get.builder().id("Spring").depth(1).name("Spring").build())
                ).build(),
                CategoryDto.Get.builder().id("lifestyle").depth(0).name("라이프스타일").childCategoryList(
                        List.of(CategoryDto.Get.builder().id("cook").depth(1).name("요리").build(),
                                CategoryDto.Get.builder().id("photograph").depth(1).name("사진").build(),
                                CategoryDto.Get.builder().id("it").depth(1).name("it").build())
                ).build(),
                CategoryDto.Get.builder().id("startup").depth(0).name("스타트업").childCategoryList(
                        List.of(CategoryDto.Get.builder().id("organizational-culture").depth(1).name("조직문화").build(),
                                CategoryDto.Get.builder().id("personnel").depth(1).name("인사").build(),
                                CategoryDto.Get.builder().id("how-to-start-startup").depth(1).name("How to Start Startup").build())
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
