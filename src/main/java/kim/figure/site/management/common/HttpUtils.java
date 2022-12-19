package kim.figure.site.management.common;

import org.reactivestreams.Publisher;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * author         : walker
 * date           : 2022. 12. 08.
 * description    :
 */
public class HttpUtils {

    public static <T> Mono<ServerResponse>  okResponse(Publisher<T> bodyPublisher, Class<T> clazz){
        return ServerResponse.ok().body(bodyPublisher,clazz);
    }
}
