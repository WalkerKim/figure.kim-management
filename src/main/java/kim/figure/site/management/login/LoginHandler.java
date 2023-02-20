package kim.figure.site.management.login;

import kim.figure.site.management.account.AccountDto;
import kim.figure.site.management.common.mapper.UserMapper;
import kim.figure.site.management.configuration.security.CustomReactiveMapSessionRepository;
import kim.figure.site.management.configuration.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.session.ReactiveSessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;


@Component
public class LoginHandler {
    @Autowired
    ReactiveSessionRepository reactiveSessionRepository;

    Mono<ServerResponse> login(ServerRequest request) {
        return ReactiveSecurityContextHolder.getContext()
                .switchIfEmpty(Mono.error(new IllegalStateException("ReactiveSecurityContext is empty")))
                .map(securityContext -> (CustomUserDetails)securityContext.getAuthentication().getPrincipal())
                .map(customUserDetails -> UserMapper.INSTANCE.userDetailsToAccountDto(customUserDetails))
//                .flatMap(accountDto ->logService.setLog(LogService.LOGIN, accountDto, accountDto.getAgentId()))
                .flatMap(customUserDetails -> {
                    request.exchange().getResponse().addCookie(ResponseCookie.from("username", customUserDetails.getUsername()).build());
                    var res = ServerResponse.ok().body(Mono.just(customUserDetails), AccountDto.Get.class);
                    return res;
                });
    }


    public Mono<ServerResponse> getSessions(ServerRequest serverRequest) {
        return ServerResponse.ok().body(Mono.just(((CustomReactiveMapSessionRepository) reactiveSessionRepository).getSessions()), Map.class);
    }
}
