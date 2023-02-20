package kim.figure.site.management.account;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface AdminAccountRepository {

    Mono<AdminAccount>  findByUsername(String username);

    Mono<Boolean> existsByUsernameAndIsActive(String username, boolean isActive);


    Mono<AdminAccount> findByUsernameAndIsActive(String username, boolean b);

    Mono<AdminAccount> save(AdminAccount adminAccount);
}
