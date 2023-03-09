package kim.figure.site.management.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RequiredArgsConstructor
@Log4j2
public class LoginJsonAuthConverter implements ServerAuthenticationConverter {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return DataBufferUtils.join(exchange.getRequest().getBody())
                .map(buffer -> {
                    try {
                        LoginRequestDto request = objectMapper.readValue(buffer.asInputStream(), LoginRequestDto.class);
                        return new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
                    } catch (IOException e) {
                        log.debug("Can't read login request from JSON");
                        throw new RuntimeException(e);
                    }
                });
    }

    @Getter
    @Setter
    @Data
    public static class LoginRequestDto {
        String username;
        String password;

    }
}