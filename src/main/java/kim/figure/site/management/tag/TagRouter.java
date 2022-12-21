package kim.figure.site.management.tag;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

/**
 * author         : walker
 * date           : 2022. 12. 15.
 * description    :
 */
@Configuration
public class TagRouter {
    @Bean
    public RouterFunction<ServerResponse> tagRoutes(TagHandler tagHandler){
        return RouterFunctions
                .   route(GET("/tag").and(contentType(MediaType.APPLICATION_JSON)), tagHandler::getTagList)
                .andRoute(DELETE("/tag/{id}").and(accept(MediaType.APPLICATION_JSON)), tagHandler::deleteTag)
                .andRoute(GET("/tag/count").and(accept(MediaType.APPLICATION_JSON)), tagHandler::getTagCount)
                .andRoute(GET("/tag/{id}/content").and(accept(MediaType.APPLICATION_JSON)), tagHandler::getTagContent)
                .andRoute(GET("/tag/{id}/content/count").and(accept(MediaType.APPLICATION_JSON)), tagHandler::getTagContentCount)

                ;
    }
}
