package crawl;

import com.fasterxml.jackson.core.type.TypeReference;
import model.User;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.*;

public class UserPage extends Page{
    protected static String userNameCard = primaryColumn + "div[data-testid='UserName'] span.css-1jxf684.r-bcqeeo.r-1ttztb7.r-qvutc0.r-poiln3";//"span.css-1jxf684.r-bcqeeo.r-1ttztb7.r-qvutc0.r-poiln3";
    protected static String bioCard = primaryColumn + "div[data-testid='UserDescription']";
    protected static String imageCard = primaryColumn + "a.css-175oi2r.r-1pi2tsx.r-13qz1uu.r-o7ynqc.r-6416eg.r-1ny4l3l.r-1loqt21";
    protected static String followCard = primaryColumn + "span.css-1jxf684.r-bcqeeo.r-1ttztb7.r-qvutc0.r-poiln3.r-1b43r93.r-1cwl3u0.r-b88u0q";

//    private String profileUrl;
//    private String userId;
//
//    private String userName;
//    private String bio;
//
//    private String profileImageUrl;
//
//    public String getProfileUrl() {
//        return profileUrl;
//    }
//
//    public String getUserId() {
//        return userId;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public String getBio() {
//        return bio;
//    }
//
//    public String getProfileImageUrl() {
//        return profileImageUrl;
//    }
//
//    public String getFollowersCount() {
//        return followersCount;
//    }
//
//    public String getFollowingCount() {
//        return followingCount;
//    }
//
//    public int getTweetCount() {
//        return tweetCount;
//    }
//
//    public Set<String> getTweets() {
//        return tweets;
//    }
//
//    public Set<String> getFollowing() {
//        return following;
//    }

//    private String followersCount;
//    private String followingCount;
//
//    private int tweetCount;
//    private Set<String >tweets; // (tweetId)
//    private Set<String> following; // (<1000) (userId)

    private User user;

    public UserPage(){}

    public UserPage(String userId){
//        this.userId = userId;
//        this.profileUrl = "https://x.com/" + userId;
        this.user = new User(userId);
        user.setProfileUrl("https://x.com/" + userId);
    }

    public void extractDetails(WebDriver driver) throws InterruptedException, IOException {
        //driver.get(profileUrl);
        driver.get(this.user.getProfileUrl());
        Thread.sleep(3000);
        //userName = driver.findElement(By.cssSelector(userNameCard)).getText();
        this.user.setUsername(driver.findElement(By.cssSelector(userNameCard)).getText());
        try {
            WebElement bioElement = driver.findElement(By.cssSelector(bioCard));
            //bio =  bioElement.getText();
            user.setBio(bioElement.getText());
        } catch (NoSuchElementException e) {
            //bio = "";
            user.setBio("");
        }
        //profileImageUrl = driver.findElement(By.cssSelector(imageCard)).getText();
        List<WebElement> follow = driver.findElements(By.cssSelector(followCard));
        //followingCount = follow.getFirst().getText();
        user.setFollowingCount(follow.getFirst().getText());
        //followersCount = follow.getLast().getText();
        user.setFollowersCount(follow.getLast().getText());
    }

    public void extractTweets(WebDriver driver, int limit) throws InterruptedException, IOException {
        //driver.get(profileUrl);
        driver.get(this.user.getProfileUrl());
        Thread.sleep(4000);
        //tweets = getElementsByScroll(driver, limit, Page.tweetCard);
        Set<String> tweets = getElementsByScroll(driver, limit, Page.tweetCard);
        user.setTweets(new ArrayList<>(tweets));
        //tweetCount = tweets.size();
        user.setTweetCount(tweets.size());
    }

    public boolean checkFollowersCount(){
        int count = -1;
        try {
            //count = Integer.parseInt(followersCount);
            count = Integer.parseInt(user.getFollowersCount());
            return (count > 300);
        } catch (NumberFormatException e) {
            return true;// số lượng followers lớn hơn 1000
//            String[] parts = followersCount.split(" ");
//            if(parts.length < 2){
//                return false;
//            }
//            if(!parts[1].equals("N")){
//                return true;
//            }
//            String[] subParts = parts[0].split(",");
//            int firstDigit = Integer.parseInt(subParts[0]);
//            if(firstDigit >= 5){
//                return true;
//            }
        }
    }

    public void extractFollowing(WebDriver driver, int limit) throws InterruptedException, IOException {
        //driver.get(profileUrl + "/following");
        driver.get(this.user.getProfileUrl());
        Thread.sleep(4000);

        //following = getElementsByScroll(driver, limit, Page.userCard);
        Set<String> following = getElementsByScroll(driver, limit, Page.userCard);
        user.setFollowing(new ArrayList<>(following));
    }

    public void extractAllInfo(WebDriver driver, int limitTweets, int limitFollowing) throws InterruptedException, IOException {
        extractDetails(driver);
        //extractTweets(driver, limitTweets);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPage userPage = (UserPage) o;
        //return Objects.equals(userId, userPage.userId);  // So sánh dựa trên thuộc tính id
        return Objects.equals(user.getUserId(), userPage.user.getUserId());
    }

    @Override
    public int hashCode() {
        //return Objects.hash(userId);
        return Objects.hash(user.getUserId());
    }

    public User getUser() {
        return user;
    }
}
