
import java.util.*;

public class KeywordResult {
    private int wordCounter = 0;
    private Set<String> lines = new LinkedHashSet<String>();

    public KeywordResult(String contextLine) {
        addLine(contextLine);
    }

    public void addLine(String line) {
        lines.add(line);
        wordCounter++;
    }

    public int getWordCounter() {
        return wordCounter;
    }

    public Set<String> getContextLines() {
        return lines;
    }
}
