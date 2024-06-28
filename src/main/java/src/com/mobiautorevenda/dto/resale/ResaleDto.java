package src.com.mobiautorevenda.dto.resale;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResaleDto {
    @NotNull(message = "Cnpj is required")
    private String cnpj;
    private String corporateReason;
}
