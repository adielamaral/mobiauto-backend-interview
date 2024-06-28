package src.com.mobiautorevenda.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import src.com.mobiautorevenda.enums.Profile;
import src.com.mobiautorevenda.model.Resale;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAccountDto {
    private String name;
    private String email;
    @JsonIgnore
    private String password;
    private Resale resale;
    private int qtdServicesInProgress;
    private Profile profile;
}
