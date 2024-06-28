package src.com.mobiautorevenda.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import src.com.mobiautorevenda.enums.OpportunityStatus;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "opportunity")
public class Opportunity {
    @Id
    private String id;
    @JsonIgnore
    private UserAccount userResponsible;
    @NotNull(message = "Status is required")
    private OpportunityStatus status;
    @NotNull(message = "ClientData is required")
    private ClientData clientData;
    private CarDetails carDetails;
    private LocalDateTime attributionData;
    private LocalDateTime completionDate;
}
