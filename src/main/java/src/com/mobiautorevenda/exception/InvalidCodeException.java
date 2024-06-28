package src.com.mobiautorevenda.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InvalidCodeException extends RuntimeException {
    public InvalidCodeException(String message) {
        super(message);
    }

}
