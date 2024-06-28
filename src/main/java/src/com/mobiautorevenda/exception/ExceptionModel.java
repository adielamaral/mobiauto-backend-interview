package src.com.mobiautorevenda.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionModel {
    private int code;
    private String message;
}
