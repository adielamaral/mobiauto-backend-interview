package src.com.mobiautorevenda.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.com.mobiautorevenda.model.Opportunity;

public interface OpportunityRepository extends MongoRepository<Opportunity, String> {

}
