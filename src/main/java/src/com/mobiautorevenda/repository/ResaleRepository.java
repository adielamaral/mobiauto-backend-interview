package src.com.mobiautorevenda.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.com.mobiautorevenda.model.Resale;

import java.util.Optional;

public interface ResaleRepository extends MongoRepository<Resale, String> {

    Optional<Resale> findByCnpj(String cnpj);

}
