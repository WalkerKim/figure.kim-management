package kim.figure.site.management.category;

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
 * date           : 2022. 11. 29.
 * description    :
 */
@Configuration
public class CategoryRouter {

    @Bean
    public RouterFunction<ServerResponse> CategoryRoutes(CategoryHandler categoryHandler){
        return RouterFunctions
                .   route(POST("/category").and(contentType(MediaType.APPLICATION_JSON)), categoryHandler::postCategory)
                .andRoute(GET("/category").and(accept(MediaType.APPLICATION_JSON)), categoryHandler::getCategoryList)
                .andRoute(GET("/category/{id}").and(accept(MediaType.APPLICATION_JSON)), categoryHandler::getContent)
                .andRoute(PUT("/category/{id}").and(accept(MediaType.APPLICATION_JSON)), categoryHandler::putContent)
                .andRoute(DELETE("/category/{id}").and(accept(MediaType.APPLICATION_JSON)), categoryHandler::deleteContent)
                ;
    }
}
