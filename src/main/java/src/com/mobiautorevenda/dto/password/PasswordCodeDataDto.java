package src.com.mobiautorevenda.dto.password;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PasswordCodeDataDto {
    private String email;
    private String code;
    private boolean valid;
    private LocalDateTime createdAt;
}
