package src.com.mobiautorevenda.dto.opportunity.request;

import lombok.Data;
import src.com.mobiautorevenda.dto.user.UserResponsibleDto;
import src.com.mobiautorevenda.enums.OpportunityStatus;
import src.com.mobiautorevenda.model.CarDetails;
import src.com.mobiautorevenda.model.ClientData;

@Data
public class UpdateOpportunityRequest {
    private UserResponsibleDto userResponsible;
    private OpportunityStatus status;
    private ClientData clientData;
    private CarDetails carDetails;
    private String reasonForConclusion;
}
