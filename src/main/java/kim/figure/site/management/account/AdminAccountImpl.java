package kim.figure.site.management.account;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author         : walker
 * date           : 2023. 02. 15.
 * description    :
 */
@Component
public class AdminAccountImpl implements AdminAccountRepository {
    @Value("${admin.username:admin}")
    String adminUsername;

    @Value("${admin.password:testAdminPassword}")
    String adminPassword;

    @Value("${guest.username:guest}")
    String guestUsername;

    @Value("${guest.password:guest}")
    String guestPassword;

    @Autowired
    PasswordEncoder passwordEncoder;

    private static Map<String, AdminAccount> ADMIN_ACCOUNT_STORE_MAP = new ConcurrentHashMap<String, AdminAccount>();

    @PostConstruct
    public void postConstructInit(){
        ADMIN_ACCOUNT_STORE_MAP.put(adminUsername, AdminAccount.builder()
                .username(adminUsername)
                .password(passwordEncoder.encode(adminPassword))
                .isActive(true)
                .id(adminUsername)
                        .authorities(List.of("ADMIN"))
                .build());
        ADMIN_ACCOUNT_STORE_MAP.put(guestUsername, AdminAccount.builder()
                .id(guestUsername)
                .username(guestUsername)
                .name(guestUsername)
                .password(passwordEncoder.encode(guestPassword))
                .isActive(true)
                .build());
    }
    @Override
    public Mono<AdminAccount> findByUsername(String username) {
        return Optional.ofNullable(ADMIN_ACCOUNT_STORE_MAP.get(username)).map(adminAccount -> Mono.just(AdminAccountMapper.INSTANCE.copyOfAdminAccount(adminAccount))).orElseGet(() -> Mono.empty());
    }

    @Override
    public Mono<Boolean> existsByUsernameAndIsActive(String username, boolean isActive) {
        //boolean don't have to be handled when whitelist logic
        return Mono.just(ADMIN_ACCOUNT_STORE_MAP.containsKey(username));
    }

    @Override
    public Mono<AdminAccount> findByUsernameAndIsActive(String username, boolean b) {
        //boolean don't have to be handled when whitelist logic
        return Optional.ofNullable(ADMIN_ACCOUNT_STORE_MAP.get(username)).map(adminAccount -> Mono.just(AdminAccountMapper.INSTANCE.copyOfAdminAccount(adminAccount))).orElseGet(() -> Mono.empty());
    }

    @Override
    public Mono<AdminAccount> save(AdminAccount adminAccount) {
        ADMIN_ACCOUNT_STORE_MAP.put(adminAccount.getId(), adminAccount);
        return Mono.just(ADMIN_ACCOUNT_STORE_MAP.get(adminAccount.getId()));
    }
}
