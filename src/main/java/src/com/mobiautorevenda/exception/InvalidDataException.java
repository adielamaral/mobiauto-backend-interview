package src.com.mobiautorevenda.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String message) {
        super(message);
    }

}
