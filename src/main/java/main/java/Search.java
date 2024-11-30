package main.java;

import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Search implements ICrawl {
    private Set<User> users = new HashSet<>();
    private Set<Tweet> tweets = new HashSet<>();

    Search(){
    }
    /** Encode hashtag for searching by url */
    private String encodeTag(String text, Class<?> type){
        StringBuilder encodedText = new StringBuilder();
        for(int i = 0; i < text.length(); i++){
            if(text.charAt(i) == '#'){
                encodedText.append("%23");
            }
            else if (text.charAt(i) == ' '){
                encodedText.append("%20");
            }
            else {
                encodedText.append(text.charAt(i));
            }
        }
        if(type.equals(User.class)) {
            encodedText.append("&f=user");
        }
        return encodedText.toString();
    }

    /** get elements from search page*/
    public void search(int limit, String keyword, Class<?> type) throws InterruptedException, IOException {
        if(ICrawl.checkInvalidType(type)){
            System.out.println("Invalid type for search!");
            return ;
        }
        WebDriver driver = WebDriverUtil.setUpDriver();
        // Start searching
        String hashTag = encodeTag(keyword, type);
        driver.get("https://x.com/search?q=" + hashTag);
        Thread.sleep(10000);

        Set<?> elements = ICrawl.getElements(driver, limit, type);
        if(type.equals(User.class)){
            users = (Set<User>) elements;
            System.out.println("Finish crawl accounts!");
        }
        else {
            tweets = (Set<Tweet>) elements;
            System.out.println("Finish crawl tweets!");
        }
        System.out.println("Finish search function!");
        driver.quit();
    }

    public Set<User> getUsers() {
        return users;
    }

    public Set<Tweet> getTweets() {
        return tweets;
    }

}
