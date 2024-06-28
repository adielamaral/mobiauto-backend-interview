package src.com.mobiautorevenda.dto.user.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import src.com.mobiautorevenda.enums.Profile;

@Data
public class CreateUserAccountRequest {
    @NotNull(message = "Name is required")
    private String name;
    @NotNull(message = "Email is required")
    private String email;
    private String cnpjResale;
    @NotNull(message = "Profile is required")
    private Profile profile;
}
