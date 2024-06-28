package src.com.mobiautorevenda.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "car_details")
public class CarDetails {
    @JsonIgnore
    @Id
    private String id;
    private String brand;
    private String model;
    private String version;
    private String year;
}
