package dev.artep.latroc_backend_simple.exception;

public class UserAlreadyLoggedInException  extends RuntimeException {
    public UserAlreadyLoggedInException(String message) {
        super(message);
    }
}
