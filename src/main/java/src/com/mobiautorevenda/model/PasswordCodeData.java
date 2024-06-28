package src.com.mobiautorevenda.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "password_code_data")
public class PasswordCodeData {
    @Id
    private String id;
    @Indexed(unique = true)
    private String email;
    private String code;
    private boolean valid;
    private LocalDateTime createdAt;
}
