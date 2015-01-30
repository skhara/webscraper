
public class ResultPrinterArgumentException extends Exception {

    private String msg;

    public ResultPrinterArgumentException(String message) {
        msg = message;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
