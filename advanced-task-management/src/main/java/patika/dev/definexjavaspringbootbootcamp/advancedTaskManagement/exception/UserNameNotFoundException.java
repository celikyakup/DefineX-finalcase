package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception;

public class UserNameNotFoundException extends RuntimeException {
    public UserNameNotFoundException(String message) {
        super(message);
    }
}
