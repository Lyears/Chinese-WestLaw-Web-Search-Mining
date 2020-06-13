package wsm.exception;

public class QueryFormatException extends RuntimeException{

    private final Integer code;

    private final String description;

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public QueryFormatException(Integer i, String s) {
        code = i;
        description = s;
    }
}
