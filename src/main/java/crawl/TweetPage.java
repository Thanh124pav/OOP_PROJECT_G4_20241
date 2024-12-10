package crawl;

import com.fasterxml.jackson.core.type.TypeReference;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.*;

public class TweetPage extends Page{
    protected static String hashtagCard = primaryColumn + "a.css-1jxf684.r-bcqeeo.r-1ttztb7.r-qvutc0.r-poiln3.r-1loqt21";
    protected static String contentCard = primaryColumn + "div[data-testid='tweetText']"; // "span.css-1jxf684.r-bcqeeo.r-1ttztb7.r-qvutc0.r-poiln3"
    protected static String likesCard = primaryColumn + "button[data-testid='like'] span.css-1jxf684.r-1ttztb7.r-qvutc0.r-poiln3.r-n6v787.r-1cwl3u0.r-1k6nrdp.r-n7gxbd";
    protected static String retweetCard = primaryColumn + "button[data-testid='retweet'] span.css-1jxf684.r-1ttztb7.r-qvutc0.r-poiln3.r-n6v787.r-1cwl3u0.r-1k6nrdp.r-n7gxbd";
    protected static String replyCard = primaryColumn +  "button[data-testid='reply'] span.css-1jxf684.r-1ttztb7.r-qvutc0.r-poiln3.r-n6v787.r-1cwl3u0.r-1k6nrdp.r-n7gxbd";
    protected static String timeCard = primaryColumn + "a.css-1jxf684.r-bcqeeo.r-1ttztb7.r-qvutc0.r-poiln3.r-xoduu5.r-1q142lx.r-1w6e6rj.r-9aw3ui.r-3s2u2q.r-1loqt21 time";

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
            this.author = partsOfLink[3];
        }
        if(partsOfLink.length >=6){
            this.tweetId = partsOfLink[5];
        }

    }
    public void extractDetails(WebDriver driver) throws InterruptedException, IOException {
        driver.get(tweetUrl);
        Thread.sleep(5000);

        content = driver.findElement(By.cssSelector(contentCard)).getText();
        Set<WebElement> hashtagCards = new HashSet<>(driver.findElements(By.cssSelector(TweetPage.hashtagCard)));
        if(!hashtagCards.isEmpty()){
            for (WebElement hashtagCard : hashtagCards) {
                String hashtag = hashtagCard.getText();
                if(!hashtag.isEmpty()){
                    if(hashtag.charAt(0) == '#'){
                        hashtags.add(hashtag);
                    }
                }
            }
        }

        repliesCount = driver.findElement(By.cssSelector(replyCard)).getText() ;
        retweetsCount =  driver.findElement(By.cssSelector(retweetCard)).getText() ;
        likesCount = driver.findElement(By.cssSelector(likesCard)).getText() ;
        createdAt = driver.findElement(By.cssSelector(timeCard)).getAttribute("datetime");

    }

    public void extractRetweeters(WebDriver driver, int limit) throws InterruptedException, IOException {
        driver.get(tweetUrl + "/retweets");
        Thread.sleep(5000);

        Set<String> elements = getElementsByScroll(driver, limit, Page.userCard);
        retweeters.addAll(elements);
    }

    public void extractAllInfo(WebDriver driver, int limitRetweets) throws InterruptedException, IOException {
        extractDetails(driver);
        extractRetweeters(driver, limitRetweets);
    }

    @Override
    protected Set<String> extractInfoBySCroll(Set<WebElement> elements){
        //Set<Page> basicInfos = new HashSet<>();
        Set<String> links = super.extractInfoBySCroll(elements);
        Set<String> ids = new HashSet<>();
        for (String link: links){
            String[] parts = link.split("/");
            ids.add(parts[parts.length-1]);
        }
        return ids;
    };

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TweetPage tweetPage = (TweetPage) o;
        return Objects.equals(tweetId, tweetPage.tweetId);  // So sánh dựa trên thuộc tính id
    }

    @Override
    public int hashCode() {
        return Objects.hash(tweetId);
    }



    public static void main(String[] args) throws InterruptedException, IOException {

        Set<String> dataTweets = new HashSet<>(
                Save.loadJSON("src/main/resources/linkBTC_ETC_Cryto.json", new TypeReference<Set<String>>() {})
        );
        List<String> data = new ArrayList<>(dataTweets);
        List<String> subData = data.subList(54, data.size());

        System.out.printf("Start crawling details of %d Tweets!", subData.size());
        List<TweetPage> tweetsCrawl = new ArrayList<>();
        List<UserPage> usersCrawl = new ArrayList<>();
        Set<String> tweetLinksCrawl = new HashSet<>();
        Set<String> userIdCrawl = new HashSet<>();

        int i = 54;
        WebDriver driver = WebDriverUtil.setUpDriver();
        for (String link : subData){
            if(!tweetLinksCrawl.contains(link) ) {
                tweetLinksCrawl.add(link);
                i+=1;
                System.out.println("\t" + link);
                TweetPage tweet = new TweetPage(link);
                UserPage user = new UserPage(tweet.getAuthor());
                tweet.extractAllInfo(driver, 30);
                tweetsCrawl.add(tweet);
                if(!userIdCrawl.contains(tweet.getAuthor())){
                    userIdCrawl.add(tweet.getAuthor());
                    user.extractAllInfo(driver, 5, 30);
                    usersCrawl.add(user);
                }
                for(String userId: tweet.getRetweeters()){
                    if(!userIdCrawl.contains(userId)){
                        userIdCrawl.add(userId);
                        UserPage retweeter = new UserPage(userId);
                        try{
                            retweeter.extractDetails(driver);
                            if(retweeter.checkFollowersCount()){
                                retweeter.extractFollowing(driver, 30);
                                usersCrawl.add(retweeter);
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            continue;
                        }

                    }
                }
            }
            System.out.printf("finish %d-th element\n", i);
            String fileTweet = "src\\main\\resources\\small_data\\TweetBTC_ETC_Crypto_" + i + ".json";
            Save saveTweetData = new Save(fileTweet);
            saveTweetData.saveToJSON(tweetsCrawl);

            String fileUser = "src\\main\\resources\\small_data\\UserBTC_ETC_Crypto_" + i + ".json" ;
            Save saveUserData = new Save(fileUser);
            saveUserData.saveToJSON(usersCrawl);
            tweetsCrawl.clear();
            usersCrawl.clear();
            System.out.printf("Save %d elements successfully!\n", i);
        }
        System.out.println("Finish crawling details of Tweet!");
    }
}
