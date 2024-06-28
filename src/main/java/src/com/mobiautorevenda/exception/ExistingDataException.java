package src.com.mobiautorevenda.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExistingDataException extends RuntimeException {
    public ExistingDataException(String message) {
        super(message);
    }

}
