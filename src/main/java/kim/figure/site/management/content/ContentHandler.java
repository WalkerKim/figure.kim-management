package kim.figure.site.management.content;

import kim.figure.site.common.content.Content;
import kim.figure.site.management.common.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static kim.figure.site.management.common.HttpUtils.okResponse;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

/**
 * author         : walker
 * date           : 2022. 11. 21.
 * description    :
 */
@Component
public class ContentHandler {

    @Autowired
    ContentRepository contentRepository;


    @Autowired
    ValidationUtil validationUtil;

    @Autowired
    ContentService contentService;

    public Mono<ServerResponse> postTempContent(ServerRequest serverRequest) {
        return okResponse(contentService.postTempContent(), ContentDto.TempGet.class);
    }

    public Mono<ServerResponse> postContent(ServerRequest serverRequest) {
        Mono<ContentDto.Post> bodyToMono = serverRequest.bodyToMono(ContentDto.Post.class).log();
        return okResponse(contentService.postContent(bodyToMono), Content.class);
    }

    public Mono<ServerResponse> getContent(ServerRequest serverRequest) {
        return ServerResponse.ok().body(fromPublisher(contentRepository.findById(Long.parseLong(serverRequest.pathVariable("id"))), Content.class));
    }

    public Mono<ServerResponse> getContentList(ServerRequest serverRequest) {
        MultiValueMap<String, String> params = serverRequest.queryParams();
        return okResponse(contentService.getContentList(params), Content.class);
    }
    public Mono<ServerResponse> getContentCount(ServerRequest serverRequest) {
        return ServerResponse.ok().body(contentService.getContentCount(), Long.class);
    }



    public Mono<ServerResponse> putContent(ServerRequest serverRequest) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(contentService.putContent(serverRequest.bodyToMono(ContentDto.Put.class)
                        , Long.parseLong(serverRequest.pathVariable("id"))), Content.class);
    }

    public Mono<ServerResponse> deleteContent(ServerRequest serverRequest) {
        return contentRepository.deleteById(Long.parseLong(serverRequest.pathVariable("id"))).then(ServerResponse.ok().build());
    }

    public Mono<ServerResponse> deleteUneditedContent(ServerRequest serverRequest) {
        return okResponse(contentRepository.deleteByIsDraftAndRawContent(true, null), Content.class);
    }

}
