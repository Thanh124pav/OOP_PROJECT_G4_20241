package crawl;

//import graph.UserOld;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TweetSearchPage extends Page{
    public static String prefix = "https://x.com/search?q=";

    public TweetSearchPage(String link) {
        super(link);
    }

    public static String encode(String keyword) {
        StringBuffer query = new StringBuffer(prefix);
        for (char c : keyword.toCharArray()) {
            if(c == ' '){
                query.append("+");
            }else if(c == '#'){
                query.append("%23");
            }else {
                query.append(c);
            }
        }
        return query.toString();
    }
    public static String encode(String keyword, String type) {
        String query = encode(keyword);
        String postfix = "&f=" + type;
        query = query + postfix;
        return query;
    }

    public Set<String> crawlLinks(WebDriver driver, String typeQuery, String fileName) throws IOException, InterruptedException {
        System.out.printf("Crawl tweets by: %s\n", this.link);
        driver.get(this.link);
        Thread.sleep(5000);
        Set<String> links = this.getElementsByScroll(driver, 1000, typeQuery);
        Save saveData = new Save(fileName);
        saveData.saveToJSON(links);
        System.out.println("\t Finish crawling from search page");
        return links;
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        String keyword = "#Bitcoin";
        WebDriver driver = WebDriverUtil.setUpDriver();
        TweetSearchPage searchPage = new TweetSearchPage(encode(keyword));
        searchPage.crawlLinks(driver, Page.tweetCard, "src\\main\\resources\\linkHBitcoin.json" );

    }
}
