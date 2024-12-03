package crawl;


import graph.Tweet;
import graph.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;

import java.util.HashSet;
import java.util.Set;

public interface ICrawl {
    static boolean checkInvalidType(Class<?> type){
        return !(type.equals(User.class)|| type.equals(Tweet.class));
    }

    static void scrollUp(JavascriptExecutor js) throws InterruptedException {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        System.out.println("Go to the bottom of page!");
        Thread.sleep(5000);
    }

    static String setQuery(Class<?> type){
        String query;
        if(type.equals(User.class)){
            query = User.userClass;
        }
        else if (type.equals(Tweet.class)){
            query = Tweet.tweetClass;
        }
        else {
            System.out.println("Not valid type!");
            return null;
        }
        return query;
    }

    static Set<?> getElements(WebDriver driver, int limit, Class<?> type) throws InterruptedException {
        System.out.println("Crawl elements!");
        if(checkInvalidType(type)){
            System.out.println("Invalid type for getElement function!");
            return null;
        }
        String query = setQuery(type);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Set<User> users = new HashSet<>();
        Set<Tweet> tweets = new HashSet<>();
        //System.out.println(query);
        Set<WebElement> elements = new HashSet<>();
        while(elements.size() < limit) {
            int oldSize = elements.size();
            Set<WebElement> newElements = new HashSet<>(driver.findElements(By.cssSelector(query)));
            elements.addAll(newElements);
            if(elements.size() == oldSize){
                System.out.println("get all elements, break the loop!");
                break;
            }
            if (type.equals(User.class)) {
                users.addAll(getInfoUsers(newElements));
            }
            else {
                tweets.addAll(getInfoTweets(newElements));
            }
            scrollUp(js);
        }
        System.out.println("finish crawling!");
        if(type.equals(User.class)){
            return users;
        }
        else{
            return tweets;
        }

    }

    /** Get details of users */
    static Set<User> getInfoUsers(Set<WebElement> userCards) {
        Set<User> users = new HashSet<>();
        for (WebElement userCard: userCards) {
            String href = userCard.getAttribute("href");
            String queryName = User.userNameClass;
            String name = userCard.findElement(By.cssSelector(queryName)).getText();
            User user = new User(href, name);
            if(!users.contains(user)){
                users.add(user);
                System.out.println("\t" + href + " - " + name + " added!");
            }
        }
        return users;
    }
    /** Get details of tweets */
    static Set<Tweet> getInfoTweets(Set<WebElement> tweetCards) {
        Set<Tweet> tweets = new HashSet<>();
        for (WebElement tweetCard: tweetCards) {
            String href = tweetCard.getAttribute("href");
            Tweet tweet = new Tweet(href);
            if(!tweets.contains(tweet)){
                tweets.add(tweet);
                System.out.println("\t" + href +  " - " + "added!");
            }
        }
        return tweets;
    }
}

