package kim.figure.site.management.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

//@Component
public class AddControlHeaderWebFilter implements WebFilter {
    @Value("${frontend.host:http://localhost:5174}")
    String[] frontendHost;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        exchange.getResponse()
                .getHeaders()
                .addAll("Access-Control-Allow-Origin", List.of(frontendHost));

        return chain.filter(exchange);
    }
}