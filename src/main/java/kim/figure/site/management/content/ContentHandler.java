package kim.figure.site.management.content;

import kim.figure.site.common.content.Content;
import kim.figure.site.management.common.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<ServerResponse> postContent(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(ContentDto.Post.class)
                .map(dto->{
                    validationUtil.validate(dto);
                    return ContentMapper.INSTANCE.contentPostToEntity(dto);
                })
                .flatMap(contentEntity -> contentRepository.save(contentEntity).flatMap(savedContentEntity -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(savedContentEntity, Content.class)));
    }

    public Mono<ServerResponse> getContent(ServerRequest serverRequest) {
        return ServerResponse.ok().body(fromPublisher(contentRepository.findById(serverRequest.pathVariable("id")), Content.class));
    }

    public Mono<ServerResponse> getContentList(ServerRequest serverRequest) {
        Flux<Content> contentEntityFlux = serverRequest.bodyToMono(Pageable.class).flatMapMany(contentRepository::findBy);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(contentEntityFlux, Content.class);
    }

    public Mono<ServerResponse> putContent(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ContentDto.Put.class)
                .map(dto->{
                    validationUtil.validate(dto);
                    dto.setId(Long.parseLong(serverRequest.pathVariable("id")));
                    Content postEntity = ContentMapper.INSTANCE.contentPutToEntity(dto);
                    return postEntity;
                })
                .flatMap(contentEntity->ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(contentRepository.save(contentEntity), Content.class));
    }

    public Mono<ServerResponse> deleteContent(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return contentRepository.deleteById(id).then(ServerResponse.ok().build());
    }
}
