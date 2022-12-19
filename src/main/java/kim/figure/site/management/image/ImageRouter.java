package kim.figure.site.management.image;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;

/**
 * author         : walker
 * date           : 2022. 12. 04.
 * description    :
 */

@Configuration
public class ImageRouter {

    @Bean
    public RouterFunction<ServerResponse> imageRoutes(ImageHandler imageHandler){
        return RouterFunctions
                .   route(POST("/image").and(contentType(MediaType.MULTIPART_FORM_DATA)), imageHandler::postImage)
                ;
    }
}
