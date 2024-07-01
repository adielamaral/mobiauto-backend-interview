package src.com.mobiautorevenda.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import src.com.mobiautorevenda.enums.Profile;

@Data
@Builder
@Document(collection = "user_account")
public class UserAccount {
    @Id
    private String id;
    private String name;
    @Indexed(unique = true)
    private String email;
    @JsonIgnore
    private String password;
    private Resale resale;
    private int qtdServicesInProgress;
    private Profile profile;
}
