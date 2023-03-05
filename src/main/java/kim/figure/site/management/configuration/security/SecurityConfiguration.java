package kim.figure.site.management.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.HttpStatusReturningServerLogoutSuccessHandler;
import org.springframework.security.web.server.authentication.logout.LogoutWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@EnableWebFluxSecurity
@Configuration
@Log4j2
//@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    ReactiveUserDetailsService reactiveUserDetailsService;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${frontend.host:http://localhost:5000}")
    String[] frontendHost;

    @Value("${server.context-path:}")
    String contextPath;

    @Autowired
    CustomReactiveAuthenticationManager customReactiveAuthenticationManager;

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.applyPermitDefaultValues();
        configuration.setAllowedOrigins(Arrays.asList(frontendHost));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .addFilterAt(new JsonAuthenticationWebFilter(customReactiveAuthenticationManager, objectMapper, contextPath), SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAt(getLogoutWebFilter(), SecurityWebFiltersOrder.LOGOUT)
                .csrf()
                .disable()
                .authorizeExchange()
//                .anyExchange().permitAll()
                .pathMatchers(HttpMethod.GET, "/login").permitAll()
                .pathMatchers(HttpMethod.POST, "/login").permitAll()
                .pathMatchers(HttpMethod.GET, contextPath + "/account/self").permitAll()
//                .pathMatchers(HttpMethod.PUT, contextPath + "/account/{id}/tempPassword").hasAnyAuthority("ADMIN")
//                .pathMatchers(HttpMethod.GET, contextPath + "/account/{id}/tempPassword").hasAnyAuthority("ADMIN")
//                .pathMatchers( contextPath + "/**").authenticated()
                .pathMatchers(HttpMethod.GET, contextPath + "/**").authenticated()
                .pathMatchers(HttpMethod.GET, contextPath+"/session-check").authenticated()
                .pathMatchers(HttpMethod.POST, contextPath + "/**").hasAnyAuthority("ADMIN")
                .pathMatchers(HttpMethod.DELETE, contextPath + "/**").hasAnyAuthority("ADMIN")
                .pathMatchers(HttpMethod.PUT, contextPath + "/**").hasAnyAuthority("ADMIN")
//                .pathMatchers(HttpMethod.GET, contextPath+"/**").authenticated()
                .anyExchange().permitAll()
                .and()
                .cors()
                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .exceptionHandling().authenticationEntryPoint((exchange, exception) -> Mono.error(exception));

        return http.build();
    }

//    private UserDetailsRepositoryReactiveAuthenticationManager reactiveAuthenticationManager() {
//
////        return new UserDetailsRepositoryReactiveAuthenticationManager (reactiveUserDetailsService);
//        return new CustomReactiveAuthenticationManager(reactiveUserDetailsService);
//    }

    private LogoutWebFilter getLogoutWebFilter() {
        LogoutWebFilter logoutWebFilter = new LogoutWebFilter();
        logoutWebFilter.setRequiresLogoutMatcher(ServerWebExchangeMatchers.pathMatchers(HttpMethod.GET,
                contextPath + "/logout"));
        logoutWebFilter.setLogoutSuccessHandler(new HttpStatusReturningServerLogoutSuccessHandler());
        return logoutWebFilter;
    }

//    @Bean
//    public WebFilter contextPathWebFilter() {
//        return (exchange, chain) -> {
//            ServerHttpRequest request = exchange.getRequest();
//            if (request.getURI().getPath().startsWith(contextPath)) {
//                return chain.filter(
//                        exchange.mutate()
//                                .request(request.mutate().contextPath(contextPath).build())
//                                .build());
//            }
//            return chain.filter(exchange);
//        };
//    }


}
