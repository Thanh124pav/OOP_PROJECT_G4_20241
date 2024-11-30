package main.java;

import java.io.IOException;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        String hashTag = "blockchain";
        System.out.printf("Crawl %s hashtag!", hashTag);
        Search search = new Search();
        search.search(15,hashTag,Tweet.class);

        SaveData saveData = new SaveData("src/main/resources/TweetsCrawl20.csv", search.getTweets()) ;
        saveData.saveToCSV("Tweet");
        System.out.println(search.getTweets().size());

        Tweet firstTweet = search.getTweets().iterator().next();
        System.out.println("Get user retweeting first tweet!");
        Set<User> userRetweets =  firstTweet.getUserRelated("retweet");
        SaveData userRetweetsSave = new SaveData(
                "src/main/resources/UserRetweets_" + firstTweet.getAuthor() + ".csv",
                userRetweets
        );
        userRetweetsSave.saveToCSV("User");

        System.out.println("Get users commenting first tweet!");
        Set<User> userComments =  firstTweet.getUserRelated("comment");
        SaveData userCommentsSave = new SaveData(
                "src/main/resources/UserComments_" + firstTweet.getAuthor() + ".csv",
                userComments
        );
        userCommentsSave.saveToCSV("User");

        System.out.println("Get tweets qouting first tweet!");
        Set<Tweet> tweetsQoutes = firstTweet.getTweetQoutes();
        SaveData tweetsQoutesSave = new SaveData(
                "src/main/resources/tweetQoutes_" + firstTweet.getAuthor() + ".csv",
                tweetsQoutes
        );
        tweetsQoutesSave.saveToCSV("Tweet");

    }
}