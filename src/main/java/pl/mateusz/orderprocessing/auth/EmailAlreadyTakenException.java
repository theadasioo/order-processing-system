package pl.mateusz.orderprocessing.auth;

public class EmailAlreadyTakenException extends RuntimeException{
    public EmailAlreadyTakenException(String email) {
        super("Email already : " + email);
    }
}
