package exceptions;

public class InvalidMaterialException extends RuntimeException {
    public InvalidMaterialException(String message) {
        super(message);
    }
}
