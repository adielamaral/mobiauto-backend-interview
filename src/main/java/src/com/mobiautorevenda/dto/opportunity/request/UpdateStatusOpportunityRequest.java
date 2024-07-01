package src.com.mobiautorevenda.dto.opportunity.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import src.com.mobiautorevenda.enums.OpportunityStatus;

@Data
public class UpdateStatusOpportunityRequest {
    @NotNull(message = "Status is required")
    private OpportunityStatus status;
    private String reasonForConclusion;
}
