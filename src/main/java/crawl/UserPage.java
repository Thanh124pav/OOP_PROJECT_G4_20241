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

    private User user;

    public UserPage(){}

    public UserPage(String userId){
        this.user = new User(userId);
        user.setProfileUrl("https://x.com/" + userId);
    }

    public void extractDetails(WebDriver driver) throws InterruptedException, IOException {
        driver.get(this.user.getProfileUrl());
        Thread.sleep(3000);
        this.user.setUserName(driver.findElement(By.cssSelector(userNameCard)).getText());
        try {
            WebElement bioElement = driver.findElement(By.cssSelector(bioCard));
            user.setBio(bioElement.getText());
        } catch (NoSuchElementException e) {
            //bio = "";
            user.setBio("");
        }
        List<WebElement> follow = driver.findElements(By.cssSelector(followCard));
        user.setFollowingCount(follow.getFirst().getText());
        user.setFollowersCount(follow.getLast().getText());
    }

    public void extractTweets(WebDriver driver, int limit) throws InterruptedException, IOException {
        driver.get(this.user.getProfileUrl());
        Thread.sleep(4000);
        Set<String> tweets = getElementsByScroll(driver, limit, Page.tweetCard);
        user.setTweets(new ArrayList<>(tweets));
        user.setTweetCount(tweets.size());
    }

    public boolean checkFollowersCount(){
        int count = -1;
        try {
            count = Integer.parseInt(user.getFollowersCount());
            return (count > 300);
        } catch (NumberFormatException e) {
            return true;// số lượng followers lớn hơn 1000
        }
    }

    public void extractFollowing(WebDriver driver, int limit) throws InterruptedException, IOException {
        driver.get(this.user.getProfileUrl());
        Thread.sleep(4000);
        Set<String> following = getElementsByScroll(driver, limit, Page.userCard);
        user.setFollowing(new ArrayList<>(following));
    }

    public void extractAllInfo(WebDriver driver, int limitTweets, int limitFollowing) throws InterruptedException, IOException {
        extractDetails(driver);
        extractFollowing(driver, limitFollowing);
    }

    @Override
    protected Set<String> extractInfoBySCroll(Set<WebElement> elements){
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
        return Objects.equals(user.getUserId(), userPage.user.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getUserId());
    }

    public User getUser() {
        return user;
    }
}