package wsm.exception;

public class EmptyResultException extends RuntimeException {
    private final String description;

    public EmptyResultException(String msg) {
        this.description = msg;
    }

    public String getDescription() {
        return description;
    }
}
