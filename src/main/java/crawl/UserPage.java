package crawl;

import com.fasterxml.jackson.core.type.TypeReference;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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
        this.userId = userId;
        this.profileUrl = "https://x.com/" + userId;
    }

    public void extractDetails(WebDriver driver) throws InterruptedException, IOException {
        driver.get(profileUrl);
        Thread.sleep(3000);
        userName = driver.findElement(By.cssSelector(userNameCard)).getText();
        try {
            WebElement bioElement = driver.findElement(By.cssSelector(bioCard));
            bio =  bioElement.getText();
        } catch (NoSuchElementException e) {
            bio = "";
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

    public boolean checkFollowersCount(){
        int count = -1;
        try {
            count = Integer.parseInt(followersCount);
            return count > 5000;
        } catch (NumberFormatException e) {
            return true;
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPage userPage = (UserPage) o;
        return Objects.equals(userId, userPage.userId);  // So sánh dựa trên thuộc tính id
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        Set<TweetPage> dataTweets = Save.loadJSON(
                "src/main/resources/OldTweet.json",
                new TypeReference<Set<TweetPage>>() {}
        );
        Set<UserPage> dataUsers = Save.loadJSON(
                "src/main/resources/OldUser.json",
                new TypeReference<Set<UserPage>>() {}
        );

        Set<String> userRetweets = new HashSet<>();
        Set<String> userCrawleds = new HashSet<>();
        Set<String> userNeedCrawl = new HashSet<>();
        for (TweetPage tweet: dataTweets){
            userRetweets.addAll(tweet.getRetweeters());
        }
        for (UserPage user: dataUsers){
            userCrawleds.add(user.getUserId());
        }

        WebDriver driver = WebDriverUtil.setUpDriver();
        for(String userId: userRetweets){
            boolean checkToPrint = false;
            if(!userCrawleds.contains(userId)){
                UserPage user = new UserPage(userId);
                try {
                    user.extractDetails(driver);
                }
                catch (Exception e){
                    e.printStackTrace();
                    continue;
                }
                if(user.checkFollowersCount()){
                    userNeedCrawl.add(user.getUserId());
                    checkToPrint = true;
                }
            }
            if( checkToPrint && userNeedCrawl.size()%50 == 1){
                System.out.printf("Find out %d users with more than 5K followers\n", userNeedCrawl.size());
            }

        }
        Save saveUsers = new Save("src/main/resources/small_data/UserRetweetsNeedCrawl.json");
        saveUsers.saveToJSON(userRetweets);

//        System.out.println(dataUsers.size());
//        System.out.println(dataTweets.size());
//        System.out.println(userRetweets.size());
//        Set<UserPage> dataUsersCrawled = new HashSet<>(
//                Save.loadJSON("src/main/resources/small_data/userRetweets.json",
//                        new TypeReference<Set<UserPage>>() {}
//                )
//        );
//        System.out.println(dataUsersCrawled.size());
    }
}
