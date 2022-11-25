package kim.figure.site.management.content;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

/**
 * author         : walker
 * date           : 2022. 11. 21.
 * description    :
 */

@Configuration
public class ContentRouter {

    @Bean
    public RouterFunction<ServerResponse> contentRoutes(ContentHandler contentHandler){
        return RouterFunctions
                .   route(POST("/content").and(contentType(MediaType.APPLICATION_JSON)), contentHandler::postContent)
                .andRoute(GET("/content").and(accept(MediaType.APPLICATION_JSON)), contentHandler::getContentList)
                .andRoute(GET("/content/{id}").and(accept(MediaType.APPLICATION_JSON)), contentHandler::getContent)
                .andRoute(PUT("/content/{id}").and(accept(MediaType.APPLICATION_JSON)), contentHandler::putContent)
                .andRoute(DELETE("/content/{id}").and(accept(MediaType.APPLICATION_JSON)), contentHandler::deleteContent)
                ;
    }

}
