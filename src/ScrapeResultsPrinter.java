
import java.util.Map;
import java.util.Set;

public class ScrapeResultsPrinter implements ScrapeCompleteListener {

    boolean verboseOn = false;
    boolean wordsNumberOn = false;
    boolean characterNumberOn = false;
    boolean extractSentencesOn = false;

    public ScrapeResultsPrinter(String[] args) throws ScrapeResultsPrinterArgumentException {
        if (args != null && args.length < 1) {
            throw new ScrapeResultsPrinterArgumentException("No printing options given! You should give at least one!");
        }

        for (String option : args) {
            option = option.trim();
            if (option.equals("-v")) {
                verboseOn = true;
            } else if (option.equals("-w")) {
                wordsNumberOn = true;
            } else if (option.equals("-c")) {
                characterNumberOn = true;
            } else if (option.equals("-e")) {
                extractSentencesOn = true;
            } else {
                throw new ScrapeResultsPrinterArgumentException("Illegal printing option given: '" + option + "'");
            }
        }
    }

    @Override
    public void onScrapeComplete(ScrapeResult result) {
        printResults(result);
    }

    private synchronized void printResults(ScrapeResult result) {
        System.out.println();
        System.out.println("*** Scraping results for: " + result.getUrl() + " ***");

        if (verboseOn) {
            printElapsedTime(result);
        }

        if (!result.hasHits()) {
            System.out.println("No hits for this url");
            return;
        }

        if (characterNumberOn) {
            printCharactersTotal(result);
        }

        if (wordsNumberOn) {
            printHitsNumbers(result);
        }

        if (extractSentencesOn) {
            printHitSentences(result);
        }
    }

    private void printElapsedTime(ScrapeResult result) {
        System.out.println("Page download time: " + result.getDownloadDataTime() + " ms");
        System.out.println("Scraping time: " + result.getScrapeDataTime() + " ms");
    }

    private void printCharactersTotal(ScrapeResult result) {
        System.out.println();
        System.out.println("Total characters on webpage: " + result.getCharCounter());
    }

    private void printHitsNumbers(ScrapeResult result) {
        System.out.println();
        System.out.println("Hits list:");
        for (Map.Entry<String, Integer> entry : result.getAllHits().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private void printHitSentences(ScrapeResult result) {
        System.out.println();
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
