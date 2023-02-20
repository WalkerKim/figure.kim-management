package kim.figure.site.management.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.savedrequest.ServerRequestCache;
import org.springframework.security.web.server.savedrequest.WebSessionServerRequestCache;
import org.springframework.session.ReactiveSessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class CustomRedirectServerAuthenticationSuccessHandler
        extends RedirectServerAuthenticationSuccessHandler {

    @Autowired
    ReactiveSessionRepository reactiveSessionRepository;
    @Value("${login.duplicate.rejectRemainSession:true}")
    boolean isBlockDuplicateLogin;


    private URI location = URI.create("/");
    private ServerRequestCache requestCache = new WebSessionServerRequestCache();
    private ServerRedirectStrategy redirectStrategy = new DefaultServerRedirectStrategy();

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange,
                                              Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        return this.requestCache.getRedirectUri(exchange)
                .defaultIfEmpty(this.location)
                .doOnNext(i -> {
                    if (isBlockDuplicateLogin) {
                        ((CustomReactiveMapSessionRepository) reactiveSessionRepository)
                                .deleteSessionByPrincipal(((UserDetails) authentication.getPrincipal()).getUsername());
                    }
                })
                .flatMap(location -> this.redirectStrategy.sendRedirect(exchange, location));
    }

}
