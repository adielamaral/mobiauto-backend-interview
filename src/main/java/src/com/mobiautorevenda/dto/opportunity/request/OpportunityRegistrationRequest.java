package src.com.mobiautorevenda.dto.opportunity.request;

import lombok.Builder;
import lombok.Data;
import src.com.mobiautorevenda.enums.OpportunityStatus;
import src.com.mobiautorevenda.model.CarDetails;
import src.com.mobiautorevenda.model.ClientData;

@Data
@Builder
public class OpportunityRegistrationRequest {
    private OpportunityStatus status;
    private ClientData clientData;
    private CarDetails carDetails;
}
