package src.com.mobiautorevenda.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException.Forbidden;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(Forbidden.class)
    public ResponseEntity<ExceptionModel> handleForbidden(Forbidden e) {
        ExceptionModel exceptionModel = new ExceptionModel(HttpStatus.FORBIDDEN.value(), e.getMessage());
        log.error("", e);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exceptionModel);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionModel> handleNotFoundException(NotFoundException e) {
        ExceptionModel exceptionModel = new ExceptionModel(HttpStatus.NOT_FOUND.value(), e.getMessage());
        log.error("", e);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionModel);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionModel> handleUserAlreadyExistsException(MethodArgumentNotValidException e) {

        ExceptionModel exceptionModel = new ExceptionModel(HttpStatus.BAD_REQUEST.value(), e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        log.error("", e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionModel);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionModel> handleBadCredentialsException(BadCredentialsException e) {

        ExceptionModel exceptionModel = new ExceptionModel(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        log.error("", e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionModel);
    }

    @ExceptionHandler(InvalidCodeException.class)
    public ResponseEntity<ExceptionModel> handleInvalidCodeException(InvalidCodeException e) {

        ExceptionModel exceptionModel = new ExceptionModel(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        log.error("", e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionModel);
    }

    @ExceptionHandler(ExistingDataException.class)
    public ResponseEntity<ExceptionModel> handleExistingDataException(ExistingDataException e) {

        ExceptionModel exceptionModel = new ExceptionModel(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        log.error("", e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionModel);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionModel> handleUnauthorizedException(UnauthorizedException e) {

        ExceptionModel exceptionModel = new ExceptionModel(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        log.error("", e);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionModel);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ExceptionModel> handleInvalidDataException(InvalidDataException e) {

        ExceptionModel exceptionModel = new ExceptionModel(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        log.error("", e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionModel);
    }

}
