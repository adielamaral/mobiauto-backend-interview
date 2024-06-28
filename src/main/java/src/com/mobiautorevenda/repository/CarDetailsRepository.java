package src.com.mobiautorevenda.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.com.mobiautorevenda.model.CarDetails;

public interface CarDetailsRepository extends MongoRepository<CarDetails, String> {

}
