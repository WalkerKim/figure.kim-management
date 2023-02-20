package kim.figure.site.management.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import kim.figure.site.management.configuration.security.LoginJsonAuthConverter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.AndServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.MediaTypeServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Log4j2
public class JsonAuthenticationWebFilter extends AuthenticationWebFilter {


    /**
     * Creates an instance
     *
     * @param authenticationManager the authentication manager to use
     */
    public JsonAuthenticationWebFilter(ReactiveAuthenticationManager authenticationManager, ObjectMapper objectMapper, String contextPath) {
        super(authenticationManager);

        // 일단 빌어먹을 formData파싱하는 부분 JSON body에서 가져오는 것으로 변경
        this.setRequiresAuthenticationMatcher(new AndServerWebExchangeMatcher(ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, contextPath+"/login"),new MediaTypeServerWebExchangeMatcher(MediaType.APPLICATION_JSON)));
//        this.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.matchers(new MediaTypeServerWebExchangeMatcher(MediaType.APPLICATION_JSON)));
        this.setServerAuthenticationConverter(new LoginJsonAuthConverter(objectMapper));
        this.setAuthenticationFailureHandler(new ServerAuthenticationEntryPointFailureHandler(
                (exchange, exception) -> {
                    return Mono.error(exception);
                }));
        this.setSecurityContextRepository(new WebSessionServerSecurityContextRepository());
    }
    //요청 검사해서 컨버터 넣을지 말지 하려면 filter부분 overriding 해서 헤더 검사해서 컨버터 선택하도록 해야할듯
    /**
     * Creates an instance
     *
     * @param authenticationManagerResolver the authentication manager resolver to use
     * @since 5.3
     */
    public JsonAuthenticationWebFilter(ReactiveAuthenticationManagerResolver<ServerWebExchange> authenticationManagerResolver) {
        super(authenticationManagerResolver);
    }
}
