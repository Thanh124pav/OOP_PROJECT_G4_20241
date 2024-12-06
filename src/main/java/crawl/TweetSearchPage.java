package crawl;

//import graph.UserOld;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

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
    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println("Start crawling!");
        String keyword = "Blockchain";
        System.out.printf("\t Crawl tweets by searching hashtag: %s\n", keyword);

        WebDriver driver = WebDriverUtil.setUpDriver();
        TweetSearchPage searchPage = new TweetSearchPage(encode(keyword));
        driver.get(encode(keyword));
        Thread.sleep(5000);
        Set<String> tweetLinks = searchPage.getElementsByScroll(driver, 2, Page.tweetCard);
        System.out.println("\t Finish crawling from search page");

        System.out.println("Start crawling details of Tweet!");
        List<TweetPage> tweetsCrawl = new ArrayList<>();
        List<UserPage> usersCrawl = new ArrayList<>();
        Set<String> tweetLinksCrawl = new HashSet<>();
        Set<String> userIdCrawl = new HashSet<>();
        for (String link : tweetLinks) {
            if(!tweetLinksCrawl.contains(link)){
                tweetLinksCrawl.add(link);
                System.out.println("\t\t" + link);
                TweetPage tweet = new TweetPage(link);
                UserPage user = new UserPage(tweet.getAuthor());
                tweet.extractAllInfo(driver, 100);
                tweetsCrawl.add(tweet);
                if(!userIdCrawl.contains(tweet.getAuthor())){
                    userIdCrawl.add(tweet.getAuthor());
                    user.extractAllInfo(driver, 5, 100);
                    usersCrawl.add(user);
                }
            }
        }
        Save saveTweetData = new Save("D:\\Project\\OOP20241\\OOP_PROJECT_G4_20241\\src\\main\\resources\\testTweet.json");
        saveTweetData.saveToJSON(tweetsCrawl);
        System.out.println("Save successfully!");
        Save saveUserData = new Save("D:\\Project\\OOP20241\\OOP_PROJECT_G4_20241\\src\\main\\resources\\testUser.json");
        saveUserData.saveToJSON(usersCrawl);
        System.out.println("Finish crawling details of Tweet!");
    }
}
