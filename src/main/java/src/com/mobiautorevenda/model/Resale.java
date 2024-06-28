package src.com.mobiautorevenda.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "resale")
public class Resale {
    @Id
    private String id;
    @Indexed(unique = true)
    private String cnpj;
    private String corporateReason;
}
