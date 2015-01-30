import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("You missed necessary parameters!");
            System.exit(1);
        }

        if (!args[1].matches("\\w+(,\\w+)*")) {
            System.err.println("The second parameter should be a word\n" +
                    "or words separated by comma!");
            System.exit(1);
        }

        try {
            String[] printingArgs = Arrays.copyOfRange(args, 2, args.length);
            ScrapeCompleteListener listener = new ScrapeResultsPrinter(printingArgs);
            for (String url : buildUrlsList(args[0])) {
                WebScraper webScraper = new WebScraper(url, args[1], listener);
                webScraper.scrapeWebPage();
            }

        } catch (ScrapeResultsPrinterArgumentException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
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
