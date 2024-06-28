package src.com.mobiautorevenda.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.com.mobiautorevenda.model.UserAccount;

import java.util.Optional;

public interface UserAccountRepository extends MongoRepository<UserAccount, String> {

    Optional<UserAccount> findByEmail(String email);

}
