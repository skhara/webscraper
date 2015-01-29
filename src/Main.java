
import java.util.Map;
import java.util.Set;

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

        boolean verbose = false;
        boolean showWordsNumber = false;
        boolean showCharacterNumber = false;
        boolean showExtractedSentences = false;

        for (int i = 2; i < args.length; i++) {
            String option = args[i].trim();
            if (option.equals("-v")) {
                verbose = true;
            } else if (option.equals("-w")) {
                showWordsNumber = true;
            } else if (option.equals("-c")) {
                showCharacterNumber = true;
            } else if (option.equals("-e")) {
                showExtractedSentences = true;
            } else {
                System.err.println("Illegal option given: '" + args[i] + "'");
                System.exit(1);
            }
        }

        WebScrapper webScrapper = new WebScrapper();
        ScrapResult result = webScrapper.scrapWebPage(args[0], args[1]);

        if (verbose) {
            System.out.println("Scrapper time: " + result.getScrapDataTime() + " ms");
            System.out.println("Process time: " + result.getProcessDataTime() + " ms");
            System.out.println();
        }

        if (showCharacterNumber) {
            System.out.println("Count number of characters on webpage: " + result.getCharCounter());
            System.out.println();
        }
        if (showWordsNumber) {
            System.out.println("Hits list:");
            for (Map.Entry<String, Integer> entry : result.getAllHits().entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            System.out.println();
        }
        if (showExtractedSentences) {
            System.out.println("Sentence list:");
            for (Map.Entry<String, Set<String>> entry : result.getKeywordLines().entrySet()) {
                String searchWord = entry.getKey();
                Set<String> wordLines = entry.getValue();
                System.out.println("Scraping for '" + searchWord + "' resulted in " +
                        result.getHitsForWord(searchWord) + " hits:");
                for (String wordLine : wordLines) {
                    System.out.println(wordLine.replaceAll("(?i)" + searchWord + "\\b", "[[" + searchWord + "]]"));
                }
                System.out.println();
            }
        }
    }
}
