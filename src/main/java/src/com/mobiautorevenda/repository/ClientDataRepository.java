package src.com.mobiautorevenda.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.com.mobiautorevenda.model.ClientData;

public interface ClientDataRepository extends MongoRepository<ClientData, String> {

}
