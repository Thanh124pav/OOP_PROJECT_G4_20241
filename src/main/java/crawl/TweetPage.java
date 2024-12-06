package crawl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TweetPage extends Page{
    protected static String hashtagCard = "a.css-1jxf684.r-bcqeeo.r-1ttztb7.r-qvutc0.r-poiln3.r-1loqt21";
    protected static String contentCard = "div[data-testid='tweetText']"; // "span.css-1jxf684.r-bcqeeo.r-1ttztb7.r-qvutc0.r-poiln3"
    protected static String likesCard = "button[data-testid='like'] span.css-1jxf684.r-1ttztb7.r-qvutc0.r-poiln3.r-n6v787.r-1cwl3u0.r-1k6nrdp.r-n7gxbd";
    protected static String retweetCard ="button[data-testid='retweet'] span.css-1jxf684.r-1ttztb7.r-qvutc0.r-poiln3.r-n6v787.r-1cwl3u0.r-1k6nrdp.r-n7gxbd";
    protected static String replyCard = "button[data-testid='reply'] span.css-1jxf684.r-1ttztb7.r-qvutc0.r-poiln3.r-n6v787.r-1cwl3u0.r-1k6nrdp.r-n7gxbd";
    protected static String timeCard = "a.css-1jxf684.r-bcqeeo.r-1ttztb7.r-qvutc0.r-poiln3.r-xoduu5.r-1q142lx.r-1w6e6rj.r-9aw3ui.r-3s2u2q.r-1loqt21 time";

    //Basic attributes
    private String tweetUrl;
    private String tweetId;
    private String author;

    //Detail attributes
    private String content;
    private Set<String> hashtags = new HashSet<>();
    private String repliesCount;
    private String retweetsCount;
    private String likesCount;
    private String createdAt;

    //Advance attributes
    private List<String> retweeters = new ArrayList<>();

    public TweetPage() {}

    public String getTweetUrl() {
        return tweetUrl;
    }

    public TweetPage(String link) {
        this.tweetUrl = link;
        extractBasicInfo(link);
    }
    private void extractBasicInfo(String link){
        String[] partsOfLink = link.split("/");
        if(partsOfLink.length >=4){
            this.author = "@" + partsOfLink[3];
        }
        if(partsOfLink.length >=6){
            this.tweetId = partsOfLink[5];
        }

    }
    public void extractDetails(WebDriver driver) throws InterruptedException, IOException {
//        WebDriver driver = WebDriverUtil.setUpDriver();
//        System.out.printf("\t\tChange to %s", tweetUrl);
        driver.get(tweetUrl);
        Thread.sleep(5000);

        content = driver.findElement(By.cssSelector(contentCard)).getText();
        Set<WebElement> hashtagCards = new HashSet<>(driver.findElements(By.cssSelector(TweetPage.hashtagCard)));
        if(!hashtagCards.isEmpty()){
            for (WebElement hashtagCard : hashtagCards) {
                String hashtag = hashtagCard.getText();
                hashtags.add(hashtag);
            }
        }

        repliesCount = driver.findElement(By.cssSelector(replyCard)).getText() ;
        retweetsCount =  driver.findElement(By.cssSelector(retweetCard)).getText() ;
        likesCount = driver.findElement(By.cssSelector(likesCard)).getText() ;
        createdAt = driver.findElement(By.cssSelector(timeCard)).getAttribute("datetime");

    }

    public void extractRetweeters(WebDriver driver, int limit) throws InterruptedException, IOException {
//        WebDriver driver = WebDriverUtil.setUpDriver();
        driver.get(tweetUrl + "/retweets");
        Thread.sleep(5000);

        Set<String> elements = getElementsByScroll(driver, limit, Page.userCard);
        retweeters.addAll(elements);
    }

    public void extractAllInfo(WebDriver driver, int limitRetweets) throws InterruptedException, IOException {
        extractDetails(driver);
        extractRetweeters(driver, limitRetweets);
    }

    public String getTweetId() {
        return tweetId;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public Set<String> getHashtags() {
        return hashtags;
    }

    public String getRepliesCount() {
        return repliesCount;
    }

    public String getRetweetsCount() {
        return retweetsCount;
    }

    public String getLikesCount() {
        return likesCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public List<String> getRetweeters() {
        return retweeters;
    }

}
