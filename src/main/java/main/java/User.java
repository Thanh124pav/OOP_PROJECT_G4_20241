package main.java;

import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.Set;

public class User {
    public static String userCard = "a";
    public static String userClass = "css-175oi2r.r-1wbh5a2.r-dnmrzs.r-1ny4l3l.r-1loqt21";
    public static String userSubCard = "span";
    public static String userSubClass = "css-1jxf684.r-bcqeeo.r-1ttztb7.r-qvutc0.r-poiln3";
    public static final int MAX_TWEET_LATEST = 1000;
    public static final int MAX_FOLLOWERS = 1000;

    private String name;
    private String link;

    public User(String link, String name){
        this.link = link;
        this.name = name;
    }

    public Set<Tweet> getLatestTweets() throws IOException, InterruptedException{
        WebDriver driver = WebDriverUtil.setUpDriver();
        driver.get(link);
        return (Set<Tweet>) ICrawl.getElements(driver, MAX_TWEET_LATEST, Tweet.class);
    }

    public Set<Tweet> getLatestTweets(int limit) throws IOException, InterruptedException{
        WebDriver driver = WebDriverUtil.setUpDriver();
        driver.get(link);
        return (Set<Tweet>) ICrawl.getElements(driver, limit, Tweet.class);
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

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
