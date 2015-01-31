import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        if (args.length < 2) {
            printErrAndExit("You missed necessary parameters!");
        }

        if (!args[1].matches("\\w+(,\\w+)*")) {
            printErrAndExit("The second parameter should be a word or words separated by comma!");
        }

        try {
            String[] printingArgs = Arrays.copyOfRange(args, 2, args.length);
            ScrapeCompleteListener listener = new ScrapeResultsPrinter(printingArgs);

            List<String> urlsList = buildUrlsList(args[0]);
            String searchArgs = args[1];

            for (String url : urlsList) {
                WebScraper webScraper = new WebScraper(url, searchArgs, listener);
                webScraper.scrapeWebPage();
            }

        } catch (ScrapeResultsPrinterArgumentException e) {
            printErrAndExit(e.getMessage());
        }
    }

    private static void printErrAndExit(String errorMsg) {
        System.err.println(errorMsg);
        System.exit(1);
    }

    private static List<String> buildUrlsList(String urlParam) {
        List<String> urls = new ArrayList<String>();

        try {
            File file = new File(urlParam);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String url;
            while ((url = reader.readLine()) != null) {
                urls.add(url);
            }

        } catch (FileNotFoundException e) {
            urls.add(urlParam);

        } catch (IOException e) {
            System.err.println("Could not read urls info from file " + urlParam + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return urls;
    }
}
