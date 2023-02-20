package kim.figure.site.management.login;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

/**
 * author         : walker
 * date           : 2022. 11. 29.
 * description    :
 */
@Configuration
public class LoginRouter {

    @Bean
    public RouterFunction<ServerResponse> LoginRoutes(LoginHandler loginHandler){
        return RouterFunctions
                .route(POST("/login").and(contentType(MediaType.APPLICATION_JSON)), loginHandler::login)
                .andRoute(GET("session-check").and(contentType(MediaType.APPLICATION_JSON)), req -> ServerResponse.ok().build());
    }
}
