
import java.util.*;

public class ScrapeResult {
    private int charCounter = 0;

    private long downloadDataTime;
    private long scrapeDataTime;

    private Map<String, KeywordResult> keywords = new HashMap<String, KeywordResult>();
    private final String url;

    public ScrapeResult(String url) {
        this.url = url;
    }

    public void keywordFound(String keyword, String contextLine) {
        if (keywords.containsKey(keyword)) {
            keywords.get(keyword).addLine(contextLine);
        } else {
            keywords.put(keyword, new KeywordResult(contextLine));
        }
    }

    public void incrementCharCounter(int delta) {
        charCounter += delta;
    }

    public int getCharCounter() {
        return charCounter;
    }

    public int getHitsForWord(String searchWord) {
        return keywords.get(searchWord).getWordCounter();
    }

    public Map<String, Integer> getAllHits() {
        Map<String, Integer> result = new HashMap<String, Integer>();
        for (Map.Entry<String, KeywordResult> entry : keywords.entrySet()) {
            result.put(entry.getKey(), entry.getValue().getWordCounter());
        }
        return result;
    }

    public Map<String, Set<String>> getKeywordLines() {
        Map<String, Set<String>> result = new HashMap<String, Set<String>>();
        for (Map.Entry<String, KeywordResult> entry : keywords.entrySet()) {
            result.put(entry.getKey(), entry.getValue().getContextLines());
        }
        return result;
    }

    public long getDownloadDataTime() {
        return downloadDataTime;
    }

    public void setDownloadDataTime(long downloadDataTime) {
        this.downloadDataTime = downloadDataTime;
    }

    public long getScrapeDataTime() {
        return scrapeDataTime;
    }

    public void setScrapeDataTime(long scrapeDataTime) {
        this.scrapeDataTime = scrapeDataTime;
    }

    public String getUrl() {
        return url;
    }

    public boolean hasHits() {
        return keywords.size() > 0;
    }
}
