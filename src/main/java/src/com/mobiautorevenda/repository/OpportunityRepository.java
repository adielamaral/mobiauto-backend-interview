package src.com.mobiautorevenda.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.com.mobiautorevenda.enums.OpportunityStatus;
import src.com.mobiautorevenda.model.Opportunity;

import java.util.List;

public interface OpportunityRepository extends MongoRepository<Opportunity, String> {

    List<Opportunity> findAllByResaleId(String resaleId);

    List<Opportunity> findAllByResaleIdAndStatus(String resaleId, OpportunityStatus status);

}
