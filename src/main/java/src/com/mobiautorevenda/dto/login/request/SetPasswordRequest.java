package src.com.mobiautorevenda.dto.login.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SetPasswordRequest {
    @NotNull(message = "Email is required")
    private String email;
    private String code;
    @NotNull(message = "Password is required")
    private String password;
}
