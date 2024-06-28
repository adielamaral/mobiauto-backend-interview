package src.com.mobiautorevenda.dto.user.response;

import lombok.Data;
import src.com.mobiautorevenda.enums.Profile;

@Data
public class UserAccountDataResponse {
    private String name;
    private String email;
    private Profile profile;
}
