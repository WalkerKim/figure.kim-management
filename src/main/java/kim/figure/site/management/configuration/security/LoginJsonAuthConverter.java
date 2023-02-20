package kim.figure.site.management.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RequiredArgsConstructor
@Log4j2
public class LoginJsonAuthConverter implements ServerAuthenticationConverter {

    private final ObjectMapper mapper;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return exchange.getRequest().getBody()
                .next()
                .flatMap(buffer -> {
                    try {
                        LoginRequestDto request = mapper.readValue(buffer.asInputStream(), LoginRequestDto.class);
                        return Mono.just(request);
                    } catch (IOException e) {
                        log.debug("Can't read login request from JSON");
                        return Mono.error(e);
                    }
                })
                .map(request -> new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    }

    /**
     * Converts a {@link ServerWebExchange} to an {@link Authentication}
     *
     * @param exchange The {@link ServerWebExchange}
     * @return A {@link Mono} representing an {@link Authentication}
     */

    @Getter
    @Setter
    @Data
    public static class LoginRequestDto {
        String username;
        String password;

    }
}