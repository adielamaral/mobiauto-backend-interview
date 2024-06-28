package src.com.mobiautorevenda.dto.login.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import src.com.mobiautorevenda.dto.user.UserAccountDto;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLoginResponse {
    private String token;
    private String registrationPasswordCode;
    private UserAccountDto user;
}
