package src.com.mobiautorevenda.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "client_data")
public class ClientData {
    @JsonIgnore
    @Id
    private String id;
    private String name;
    private String email;
    private String phone;
}
