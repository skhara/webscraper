
public class ScrapeResultsPrinterArgumentException extends Exception {

    private String msg;

    public ScrapeResultsPrinterArgumentException(String message) {
        msg = message;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
