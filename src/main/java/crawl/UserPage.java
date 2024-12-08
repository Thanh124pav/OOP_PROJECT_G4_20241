package crawl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserPage extends Page{
    protected static String userNameCard = primaryColumn + "div[data-testid='UserName'] span.css-1jxf684.r-bcqeeo.r-1ttztb7.r-qvutc0.r-poiln3";//"span.css-1jxf684.r-bcqeeo.r-1ttztb7.r-qvutc0.r-poiln3";
    protected static String bioCard = primaryColumn + "div[data-testid='UserDescription']";
    protected static String imageCard = primaryColumn + "a.css-175oi2r.r-1pi2tsx.r-13qz1uu.r-o7ynqc.r-6416eg.r-1ny4l3l.r-1loqt21";
    protected static String followCard = primaryColumn + "span.css-1jxf684.r-bcqeeo.r-1ttztb7.r-qvutc0.r-poiln3.r-1b43r93.r-1cwl3u0.r-b88u0q";

    private String profileUrl;
    private String userId;

    private String userName;
    private String bio;

    private String profileImageUrl;

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getBio() {
        return bio;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getFollowersCount() {
        return followersCount;
    }

    public String getFollowingCount() {
        return followingCount;
    }

    public int getTweetCount() {
        return tweetCount;
    }

    public Set<String> getTweets() {
        return tweets;
    }

    public Set<String> getFollowing() {
        return following;
    }

    private String followersCount;
    private String followingCount;

    private int tweetCount;
    private Set<String >tweets; // (tweetId)
    private Set<String> following; // (<1000) (userId)

    public UserPage(){};
    public UserPage(String userId){
        this.userId = userId.substring(1);
        this.profileUrl = "https://x.com/" + userId;
    }

    public void extractDetails(WebDriver driver) throws InterruptedException, IOException {
        driver.get(profileUrl);
        Thread.sleep(4000);

        userName = driver.findElement(By.cssSelector(userNameCard)).getText();
        WebElement bioElement = driver.findElement(By.cssSelector(bioCard));
        if(bioElement != null){
            bio = bioElement.getText();
        }
        profileImageUrl = driver.findElement(By.cssSelector(imageCard)).getText();
        List<WebElement> follow = driver.findElements(By.cssSelector(followCard));
        followingCount = follow.getFirst().getText();
        followersCount = follow.getLast().getText();
    }

    public void extractTweets(WebDriver driver, int limit) throws InterruptedException, IOException {
        driver.get(profileUrl);
        Thread.sleep(4000);
        tweets = getElementsByScroll(driver, limit, Page.tweetCard);
        tweetCount = tweets.size();
    }

    public void extractFollowing(WebDriver driver, int limit) throws InterruptedException, IOException {
        driver.get(profileUrl + "/following");
        Thread.sleep(4000);

        following = getElementsByScroll(driver, limit, Page.userCard);
    }

    public void extractAllInfo(WebDriver driver, int limitTweets, int limitFollowing) throws InterruptedException, IOException {
        extractDetails(driver);
        extractTweets(driver, limitTweets);
        extractFollowing(driver, limitFollowing);
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
}
