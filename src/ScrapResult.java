
import java.util.*;

class ScrapResult {
    private int charCounter = 0;

    private long scrapDataTime;
    private long processDataTime;
    private Map<String, KeywordResult> keywords = new HashMap<String, KeywordResult>();

    private String url;

    public ScrapResult(String url) {
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

    public long getScrapDataTime() {
        return scrapDataTime;
    }

    public void setScrapDataTime(long scrapDataTime) {
        this.scrapDataTime = scrapDataTime;
    }

    public long getProcessDataTime() {
        return processDataTime;
    }

    public void setProcessDataTime(long processDataTime) {
        this.processDataTime = processDataTime;
    }

    public String getUrl() {
        return url;
    }

    public boolean hasHits() {
        return keywords.size() > 0;
    }
}
