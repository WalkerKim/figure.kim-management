package kim.figure.site.management.tag;

import kim.figure.site.common.content.Content;
import kim.figure.site.common.tag.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static kim.figure.site.management.common.HttpUtils.okResponse;

/**
 * author         : walker
 * date           : 2022. 12. 15.
 * description    :
 */
@Component
public class TagHandler {
    @Autowired
    TagService tagService;

    public Mono<ServerResponse> getTagList(ServerRequest serverRequest) {
        MultiValueMap<String, String> params = serverRequest.queryParams();
        return okResponse(tagService.getTagList(params), TagDto.Get.class);
    }

    public Mono<ServerResponse> deleteTag(ServerRequest serverRequest) {
        return okResponse(tagService.deleteTag(serverRequest.pathVariable("id")), Void.class);
    }

    public Mono<ServerResponse> getTagCount(ServerRequest serverRequest) {
        return okResponse(tagService.getTagCount(), Long.class);
    }

    public Mono<ServerResponse> getTagContent(ServerRequest serverRequest) {
        return okResponse(tagService.contentByTag(serverRequest.pathVariable("id")), Content.class);
    }

    public Mono<ServerResponse> getTagContentCount(ServerRequest serverRequest) {
        return okResponse(tagService.getTagContentCount(serverRequest.pathVariable("id")), Long.class);
    }
}
