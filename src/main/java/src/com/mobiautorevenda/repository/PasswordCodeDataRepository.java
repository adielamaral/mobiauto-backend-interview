package src.com.mobiautorevenda.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.com.mobiautorevenda.model.PasswordCodeData;

public interface PasswordCodeDataRepository extends MongoRepository<PasswordCodeData, String> {
    PasswordCodeData findByEmail(String email);

    PasswordCodeData findByCode(String code);
}
