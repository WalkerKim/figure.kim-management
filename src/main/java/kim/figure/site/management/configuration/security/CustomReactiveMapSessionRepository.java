package kim.figure.site.management.configuration.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.session.ReactiveMapSessionRepository;
import org.springframework.session.Session;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;


@Log4j2
public class CustomReactiveMapSessionRepository extends ReactiveMapSessionRepository {
    private final Map<String, Session> sessions;

    /**
     * Creates a new instance backed by the provided {@link Map}. This allows injecting a
     * distributed {@link Map}.
     *
     * @param sessions the {@link Map} to use. Cannot be null.
     */
    public CustomReactiveMapSessionRepository(Map<String, Session> sessions) {
        super(sessions);
        if (sessions == null) {
            throw new IllegalArgumentException("sessions cannot be null");
        }
        this.sessions = sessions;
    }

    public Map<String, Session> getSessions() {
        return sessions;
    }

    public void deleteSessionByPrincipal(String username) {
        getSessionIdByPrincipal(username).map(i -> sessions.remove(i)).subscribe();
    }

    public Flux<String> getSessionIdByPrincipal(String username) {
        return Flux.fromIterable(sessions.values())
                .filter((session) -> !session.isExpired())
                .flatMap(i -> Mono.zip(Mono.just(i.getId()),
                        Mono.justOrEmpty(i.getAttributeOrDefault("SPRING_SECURITY_CONTEXT", new SecurityContextImpl()).getAuthentication())
                ))
                .filter(i -> i.getT2().isAuthenticated())
                .filter(i -> ((UserDetails) i.getT2().getPrincipal()).getUsername().equals(username))
                .flatMap(i -> Mono.just(i.getT1()));
    }
    public Mono<Boolean> getIsExpiredByPrincipal(String username) {
        return getSessionIdByPrincipal(username)
                .flatMap(id -> findById(id)).filter(mapSession -> !mapSession.isExpired()).count().flatMap(aLong -> {
                    if (aLong.longValue() > 0l) {
                        //expired되지 않은 세션이 있으면
                        log.debug(username + " unexpired");
                        return Mono.just(Boolean.FALSE);
                    } else {
                        //expired되지 않은 세션이 없으면
                        log.debug(username + " expired");
                        return Mono.just(Boolean.TRUE);
                    }
                });
    }

}
