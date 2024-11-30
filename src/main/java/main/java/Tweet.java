package main.java;

import org.openqa.selenium.WebDriver;

import java.io.*;
import java.util.Set;

public class Tweet implements ICrawl {
    public static String tweetCard = "a";
    public static String tweetClass = "css-146c3p1.r-bcqeeo.r-1ttztb7.r-qvutc0.r-37j5jr.r-a023e6.r-rjixqe.r-16dba41.r-xoduu5.r-1q142lx.r-1w6e6rj.r-9aw3ui.r-3s2u2q.r-1loqt21";
    private String link;
    private User author;


    Tweet(String link) {
        this.link = link;
        if (extractAuthor()!=null){
            this.author = extractAuthor();
        }
    }

    public User extractAuthor(){
        String[] partsOfLink = link.split("/");
        if(partsOfLink.length >= 4){
            String linkAuthor = "https://x.com/" + partsOfLink[3];
            String nameAuthor = partsOfLink[3];
            return new User(linkAuthor, nameAuthor);
        }
        return null;
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

    public void setLink(String link) {
        this.link = link;
    }

    public User getAuthor() {
        return author;
    }

}
