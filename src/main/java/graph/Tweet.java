package graph;

import crawl.ICrawl;
import crawl.WebDriverUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tweet extends Node implements ICrawl {
    public static String tweetClass = "a.css-146c3p1.r-bcqeeo.r-1ttztb7.r-qvutc0.r-37j5jr.r-a023e6.r-rjixqe.r-16dba41.r-xoduu5.r-1q142lx.r-1w6e6rj.r-9aw3ui.r-3s2u2q.r-1loqt21";
    public static String hashtagClass = "a.css-1jxf684.r-bcqeeo.r-1ttztb7.r-qvutc0.r-poiln3.r-1loqt21";
    public static String contentClass = "div[data-testid='tweetText']"; // "span.css-1jxf684.r-bcqeeo.r-1ttztb7.r-qvutc0.r-poiln3"
    public static String likesClass = "button[data-testid='like'] span.css-1jxf684.r-bcqeeo.r-1ttztb7.r-qvutc0.r-poiln3";

    private String link;
    private User author;
    private String id;
    private String content; //driver
    private int likesCount; //driver
    private int retweetsCount;
    private int repliesCount;
    private Set<String> hashtags = new HashSet<>(); //driver
    private Set<User> retweeters;


    public Tweet(String link) {
        this.link = link;
        setAuthor();
        setId();
    }

    public void setAuthor(){
        String[] partsOfLink = link.split("/");
        if(partsOfLink.length >= 4){
            String linkAuthor = "https://x.com/" + partsOfLink[3];
            String nameAuthor = partsOfLink[3];
            this.author = new User(linkAuthor, nameAuthor);
        }
    }

    public void setId(){
        String[] parts = link.split("/");
        if(parts.length >= 6) {
            this.id = parts[5];
        }
    }

    public void setBasicInfo() throws InterruptedException, IOException {
        WebDriver driver = WebDriverUtil.setUpDriver();
        driver.get(link);
        Thread.sleep(10000);
        List<WebElement> contentCard = new ArrayList<>(driver.findElements(By.cssSelector(contentClass)));
        Set<WebElement> likesCard = new HashSet<>(driver.findElements(By.cssSelector(likesClass)));
        Set<WebElement> hashtagCards = new HashSet<>(driver.findElements(By.cssSelector(hashtagClass)));
        this.content = contentCard.getFirst().getText();
        this.likesCount = Integer.parseInt(likesCard.iterator().next().getText());
        if(hashtagCards != null){
            for (WebElement hashtagCard : hashtagCards){
                String hashtag = hashtagCard.getText();
                hashtags.add(hashtag);
            }
        }
    }

    public Set<User> getUserRelated(String category) throws IOException, InterruptedException {
        String linkQuery;
        if(category.equals("retweet")){
            linkQuery = link + "/retweets";
        }
        else if(category.equals("comment")){
            linkQuery = link;
        }
        else {
            System.out.println("Invalid category!");
            return null;
        }
        WebDriver driver = WebDriverUtil.setUpDriver();
        driver.get(linkQuery);
        Thread.sleep(20000);
        return (Set<User>) ICrawl.getElements(driver, 1000, User.class);
    }

    public Set<Tweet> getTweetQoutes() throws IOException, InterruptedException {
        WebDriver driver = WebDriverUtil.setUpDriver();
        driver.get(link + "/quotes");
        Thread.sleep(20000);
        return (Set<Tweet>) ICrawl.getElements(driver, 1000, Tweet.class);
    }

    public void setRepliesCount() throws IOException, InterruptedException {
        this.repliesCount = this.getUserRelated("comment").size();
    }

    public void setRetweetsCount() throws IOException, InterruptedException {
        this.retweetsCount = retweeters.size();
    }

    public void setRetweeters() throws IOException, InterruptedException {
        this.retweeters = this.getUserRelated("retweet");
        this.retweetsCount = retweeters.size();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tweet tweet = (Tweet) o;
        return link.equals(tweet.link);
    }

    @Override
    public int hashCode() {
        return link.hashCode();
    }
    public String getLink() {
        return link;
    }

    public User getAuthor() {
        return author;
    }

    public static String getTweetClass() {
        return tweetClass;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public int getRetweetsCount() {
        return retweetsCount;
    }

    public int getRepliesCount() {
        return repliesCount;
    }

    public Set<String> getHashtags() {
        return hashtags;
    }

    public Set<User> getRetweeters() {
        return retweeters;
    }


    public static void main(String[] args) throws InterruptedException, IOException {
        Tweet tweet = new Tweet("https://x.com/maximelabonne/status/1863906046659416225");
        tweet.setBasicInfo();
        System.out.println("link: " + tweet.getLink());
        System.out.println("author:" + tweet.getAuthor().getName());
        System.out.println("id: " + tweet.getId());
        System.out.println("content: " + tweet.getContent());
        System.out.println("likes: " + tweet.getLikesCount());
        tweet.setRetweeters();
        System.out.println("retweets: " + tweet.getRetweetsCount());
        tweet.setRepliesCount();
        System.out.println("replies: " + tweet.getRepliesCount());
        Set<String> hashtags = tweet.getHashtags();
        if(hashtags != null){
            for (String hashtag : hashtags){
                System.out.println(hashtag);
            }
        }
        Set<User> retweeters = tweet.getRetweeters();
        for (User user : retweeters){
            user.print();
        }

    }

}
