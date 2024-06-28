package src.com.mobiautorevenda.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NonMatchingPasswordsException extends RuntimeException {
    public NonMatchingPasswordsException(String message) {
        super(message);
    }

}
