package kim.figure.site.management.configuration.security;


import kim.figure.site.management.account.AdminAccount;
import kim.figure.site.management.account.AdminAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Log4j2
@RequiredArgsConstructor
public class CustomUserDetailsService implements ReactiveUserDetailsService {

    @Autowired
    AdminAccountRepository adminAccountRepository;

//    @Autowired
//    PasswordEncoder passwordEncoder;


    @Override
    public Mono<UserDetails> findByUsername(String username) {
        log.info("username : " + username);
        return adminAccountRepository.findByUsernameAndIsActive(username, true)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("ID not found"))))
                .flatMap(i-> Mono.just(new CustomUserDetails(i)));
    }
        
}
