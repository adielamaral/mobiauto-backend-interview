package src.com.mobiautorevenda.dto.resale.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResaleRequest {
    @NotNull(message = "Cnpj is required")
    private String cnpj;
    private String corporateReason;
}
