package kim.figure.site.management.configuration.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.ReactiveMapSessionRepository;
import org.springframework.session.ReactiveSessionRepository;
import org.springframework.session.config.annotation.web.server.EnableSpringWebSession;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.ConcurrentHashMap;

import static java.time.temporal.ChronoUnit.SECONDS;

@Configuration
@EnableSpringWebSession
public class SessionConfig {

    @Value("${spring.session.timeout:3600}")
    int sessionTimeoutSeconds;

    @Bean
    public ReactiveSessionRepository reactiveSessionRepository() {
        ReactiveMapSessionRepository reactiveSessionRepository = new CustomReactiveMapSessionRepository(new ConcurrentHashMap<>());
        reactiveSessionRepository.setDefaultMaxInactiveInterval(Duration.of(sessionTimeoutSeconds, SECONDS));
        return reactiveSessionRepository;
    }
}