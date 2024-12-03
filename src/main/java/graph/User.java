package graph;

import crawl.ICrawl;
import crawl.WebDriverUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User extends Node implements ICrawl{
    public static String userClass = "a:not([tabindex='-1']).css-175oi2r.r-1wbh5a2.r-dnmrzs.r-1ny4l3l.r-1loqt21";
    public static String userNameClass = "span.css-1jxf684.r-bcqeeo.r-1ttztb7.r-qvutc0.r-poiln3";
    public static String descriptionClass = "div[data-testid='UserDescription']";
    public static String imageClass = "a.css-175oi2r.r-1pi2tsx.r-13qz1uu.r-o7ynqc.r-6416eg.r-1ny4l3l.r-1loqt21";
    public static String followClass = "span.css-1jxf684.r-bcqeeo.r-1ttztb7.r-qvutc0.r-poiln3.r-1b43r93.r-1cwl3u0.r-b88u0q";
    public static final int MAX_TWEET_LATEST = 1000;
    public static final int MAX_FOLLOWERS = 1000;

    private String name;
    private String link;
    private String userId;
    private String description; // driver
    private String profileImageUrl; //driver
    private String followersCount; //driver
    private int followingCount; //driver
    private int tweetCount; // size of tweets
    private Set<Tweet> tweets; // getLatestTweets
    private Set<User> followingUsers;

    public User(String link){
        this.link = link;
        setUserId();
    }
    public User(String link, String name){
        this.link = link;
        this.name = name;
        setUserId();
    }
    public void setUserId(){
        String[] partsOfLink = link.split("/");
        if(partsOfLink.length >= 4){
            this.userId = "@" + partsOfLink[3];
        }
    }
    @Override
    public void setBasicInfo() throws IOException, InterruptedException {
        WebDriver driver = WebDriverUtil.setUpDriver();
        driver.get(link);
        Thread.sleep(10000);
        List<WebElement> userNameCard = new ArrayList<>(driver.findElements(By.cssSelector("div[data-testid='UserName'] " + userNameClass)));
        List<WebElement> descriptionCard = new ArrayList<>(driver.findElements(By.cssSelector(descriptionClass)));
        List<WebElement> imageCard = new ArrayList<>(driver.findElements(By.cssSelector(imageClass)));
        List<WebElement> followCards = new ArrayList<>(driver.findElements(By.cssSelector(followClass)));
        this.name = userNameCard.getFirst().getText();
        this.profileImageUrl = imageCard.getFirst().getAttribute("href");
        this.followingCount = Integer.parseInt(followCards.getFirst().getText());
        this.followersCount = followCards.getLast().getText();
        StringBuffer descriptionTmp = new StringBuffer();
        for (WebElement descriptionSubCard : descriptionCard) {
            descriptionTmp.append(descriptionSubCard.getText());
        }
        this.description = String.valueOf(descriptionTmp);
    }

    public Set<Tweet> setLatestTweets() throws IOException, InterruptedException{
        WebDriver driver = WebDriverUtil.setUpDriver();
        driver.get(link);
        this.tweets = (Set<Tweet>) ICrawl.getElements(driver, MAX_TWEET_LATEST, Tweet.class);
        return tweets;
    }

    public Set<Tweet> setLatestTweets(int limit) throws IOException, InterruptedException{
        WebDriver driver = WebDriverUtil.setUpDriver();
        driver.get(link);
        this.tweets = (Set<Tweet>) ICrawl.getElements(driver, limit, Tweet.class);
        return tweets;
    }

    public Set<User> getFollowers(boolean verified) throws IOException, InterruptedException{
        String linkQuery;
        if(verified){
            linkQuery = link + "/" + "verified_followers";
        }
        else {
            linkQuery = link + "/" + "followers";
        }
        WebDriver driver = WebDriverUtil.setUpDriver();
        driver.get(linkQuery);
        return (Set<User>) ICrawl.getElements(driver, MAX_FOLLOWERS, User.class );
    }

    public Set<User> setFollowingUsers() throws IOException, InterruptedException{
        String linkQuery = link + "/" + "following";
        WebDriver driver = WebDriverUtil.setUpDriver();
        driver.get(linkQuery);
        this.followingUsers = (Set<User>) ICrawl.getElements(driver, MAX_FOLLOWERS, User.class );
        return followingUsers;
    }

    public int setTweetCount(){
        this.tweetCount = tweets.size();
        return tweetCount;
    }

    public void print(){
        System.out.println("User information: ");
        System.out.println(this.name);
        System.out.println(this.link);
        System.out.println(this.userId);
        System.out.println(this.description);
        System.out.println(this.profileImageUrl);
        System.out.println(this.followersCount);
        System.out.println(this.followersCount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return link.equals(user.link);
    }

    @Override
    public int hashCode() {
        return link.hashCode();
    }

    public String getName() {
        return name;
    }


    public String getLink() {
        return link;
    }


    public String getUserId() {
        return userId;
    }

    public String getDescription() {
        return description;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getFollowersCount() {
        return followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public int getTweetCount() {
        return tweetCount;
    }

    public Set<Tweet> getTweets() {
        return tweets;
    }

    public Set<User> getFollowingUsers() {
        return followingUsers;
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        User user = new User("https://x.com/RealCandaceO");
        user.setBasicInfo();
        System.out.println(user.getName());
        System.out.println(user.getLink());
        System.out.println(user.getUserId());
        System.out.println(user.getDescription());
        System.out.println(user.getProfileImageUrl());
        System.out.println(user.getFollowingCount());
        System.out.println(user.getFollowersCount());

    }

}
