package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.config;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.NotFoundException;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFoundError(NotFoundException e, WebRequest request) {
        ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFoundError(ChangeSetPersister.NotFoundException e, WebRequest request) {
        ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> globalException(Exception e, WebRequest request) {
        ErrorMessage message = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationErrors(MethodArgumentNotValidException e, WebRequest request) {
        ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationErrors(patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.MethodArgumentNotValidException e, WebRequest request) {
        ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}
