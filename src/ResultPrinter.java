
import java.util.Map;
import java.util.Set;

public class ResultPrinter implements ScrapCompleteListener {

    boolean verboseOn = false;
    boolean wordsNumberOn = false;
    boolean characterNumberOn = false;
    boolean extractSentencesOn = false;

    public ResultPrinter(String[] args) throws ResultPrinterArgumentException {
        if (args != null && args.length < 1) {
            throw new ResultPrinterArgumentException("No printing options given! You should give at least one!");
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
                throw new ResultPrinterArgumentException("Illegal printing option given: '" + option + "'");
            }
        }
    }

    @Override
    public void onScrapComplete(ScrapResult result) {
        printResults(result);
    }

    private synchronized void printResults(ScrapResult result) {
        System.out.println();
        System.out.println("*** Scraping results for: " + result.getUrl() + " ***");
        if (verboseOn) {
            System.out.println("Scrapper time: " + result.getScrapDataTime() + " ms");
            System.out.println("Process time: " + result.getProcessDataTime() + " ms");
        }

        if (!result.hasHits()) {
            System.out.println("No hits for this url");
            return;
        }

        if (characterNumberOn) {
            System.out.println();
            System.out.println("Count number of characters on webpage: " + result.getCharCounter());
        }
        if (wordsNumberOn) {
            System.out.println();
            System.out.println("Hits list:");
            for (Map.Entry<String, Integer> entry : result.getAllHits().entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
        if (extractSentencesOn) {
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
}
