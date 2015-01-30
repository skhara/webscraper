
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebScraper {
    private static final Pattern HEAD_PATTERN = Pattern.compile("<head>(.*?)</head>");
    private static final Pattern TITLE_PATTERN = Pattern.compile("<title>(.*?)</title>");
    private static final Pattern BODY_PATTERN = Pattern.compile("(<body\\b[^>]*>)(.*?)(</body>)");
    private static final Pattern ANYTAG_PATTERN = Pattern.compile("((<\\w+[^>]*>|</\\w+>)|(.*?))(<\\w+[^>]*>|</\\w+>)");

    private ScrapeResult scrapeResult;
    private Pattern keywordRegex;
    private String url;

    private ScrapeCompleteListener listener;

    public WebScraper(String url, String searchArgs, ScrapeCompleteListener listener) {
        scrapeResult = new ScrapeResult(url);
        keywordRegex = buildKeywordsRegex(searchArgs);
        this.url = url;
        this.listener = listener;
    }

    public void scrapeWebPage() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    String page = getWebPage(url);
                    processWebPage(page);
                    listener.onScrapeComplete(scrapeResult);
                } catch (MalformedURLException e) {
                    System.err.println("Url given is invalid!");
                    e.printStackTrace();
                } catch (IOException e) {
                    System.err.println("Cannot open given url!");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void processWebPage(String page) {
        long startTime = System.currentTimeMillis();
        processHeadTitle(page);
        processBody(page);
        scrapeResult.setScrapeDataTime(System.currentTimeMillis() - startTime);
    }

    private void processHeadTitle(String page) {
        Matcher headMatcher = HEAD_PATTERN.matcher(page);
        if (headMatcher.find()) {
            Matcher titleMatcher = TITLE_PATTERN.matcher(headMatcher.group());
            if (titleMatcher.find()) {
                String title = normalizeTitle(titleMatcher.group());
                scrapeResult.incrementCharCounter(title.length());
                serachForKeywords(splitLine(title));
            }
        }
    }

    private void processBody(String page) {
        Matcher matcher = BODY_PATTERN.matcher(page);
        if (matcher.find()) {
            page = normalizeBody(matcher.group());
            processBodyTagByTag(page);
        }
    }

    private void processBodyTagByTag(String page) {
        Matcher anyTagMatcher = ANYTAG_PATTERN.matcher(page);
        while (anyTagMatcher.find()) {
            String line = anyTagMatcher.group();
            line = normalizeLine(line);
            scrapeResult.incrementCharCounter(line.length());
            serachForKeywords(splitLine(line));
        }
    }

    private String[] splitLine(String line) {
        return line.split("\\.\\s+");
    }

    private void serachForKeywords(String[] lines) {
        if (lines.length > 0) {
            for (String str : lines) {
                Matcher matcher = keywordRegex.matcher(str);
                while (matcher.find()) {
                    String keyword = matcher.group().toLowerCase();
                    scrapeResult.keywordFound(keyword, str);
                }
            }
        }
    }

    private Pattern buildKeywordsRegex(String searchArgs) {
        StringBuilder builder = new StringBuilder();
        builder
                .append("(?i)(")
                .append(searchArgs.replaceAll(",", "|"))
                .append(")")
                .append("\\b");

        return Pattern.compile(builder.toString());
    }

    private String getWebPage(String url) throws IOException {
        long startTime = System.currentTimeMillis();
        String s;
        StringBuilder builder = new StringBuilder();

        BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(new URL(url).openStream()));
        while ((s = bufferedReader.readLine()) != null) {
            builder.append(s);
        }
        String result = builder.toString();
        scrapeResult.setDownloadDataTime(System.currentTimeMillis() - startTime);

        return result;
    }

    private String normalizeTitle(String title) {
        return title
                .replaceFirst("<title>", "")
                .replaceFirst("</title>", "")
                .trim();
    }

    private String normalizeBody(String body) {
        return body
                .replaceAll("<(strong|span|em|br|i|pre|code|blockquote)\\b[^>]*>", "")
                .replaceAll("</(strong|span|em|br|i|pre|code|blockquote)>", "")
                .replaceAll("<script\\b[^>]*>(.*?)</script>", "");
    }

    private String normalizeLine(String line) {
        return line
                .replaceFirst("<\\w+[^>]*>", "")
                .replaceFirst("<\\w+[^>]*>|</\\w+>", "")
                .trim();
    }
}
