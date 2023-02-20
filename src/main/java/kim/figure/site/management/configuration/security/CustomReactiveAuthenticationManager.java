package kim.figure.site.management.configuration.security;

import kim.figure.site.management.account.AdminAccountRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Log4j2
@Component
public class CustomReactiveAuthenticationManager extends UserDetailsRepositoryReactiveAuthenticationManager {
    @Value("${login.maxAttemptCount:5}")
    int loginMaxAttemptCount;

    @Autowired
    AdminAccountRepository adminAccountRepository;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    public CustomReactiveAuthenticationManager(CustomUserDetailsService userDetailsService) {
        super(userDetailsService);
        this.userDetailsService = (CustomUserDetailsService)userDetailsService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        if (authentication.isAuthenticated()) {
            log.debug("authentication.isAuthenticated()");
            return Mono.just(authentication);
        }
        String username = authentication.getName();
        return userDetailsService.findByUsername(username).flatMap(userDetails -> {
            CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
            if(customUserDetails.getAttemptCount()>loginMaxAttemptCount){
                return Mono.error(new RuntimeException("Login attempt limit exceeded"));
            }else{
                if(authentication.getCredentials()==null){
                    adminAccountRepository.findByUsernameAndIsActive(userDetails.getUsername(), true).flatMap(accountDocument -> {
                        accountDocument.setAttemptCount(accountDocument.getAttemptCount() + 1);
                        return adminAccountRepository.save(accountDocument);
                    }).subscribe();
                    return Mono.error(new RuntimeException("Wrong password"));
                }else{
                    return super.authenticate(authentication).doOnError(throwable -> {
                        log.debug(throwable);
                        if(throwable instanceof BadCredentialsException){
//                        ((CustomUserDetails) userDetails).setAttemptCount();
                            adminAccountRepository.findByUsernameAndIsActive(userDetails.getUsername(), true).flatMap(accountDocument -> {
                                accountDocument.setAttemptCount(accountDocument.getAttemptCount() + 1);
                                return adminAccountRepository.save(accountDocument);
                            }).subscribe();
                        }
                    }).doOnSuccess(successAuthentication -> {
                        adminAccountRepository.findByUsernameAndIsActive(userDetails.getUsername(), true).flatMap(accountDocument -> {
                            accountDocument.setAttemptCount(0);
                            return adminAccountRepository.save(accountDocument);
                        }).subscribe();
                    });
                }

            }
        });
    }
}