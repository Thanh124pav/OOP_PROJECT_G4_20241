package crawl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
//import graph.UserOld;


import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Save {
    private final String fileName;

    public Save(String fileName) {
        this.fileName = fileName;
    }
    public void saveToJSON(Object o) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), o);
    }

    public static <T> Set<T> loadTweetJSON(String fileName, TypeReference<Set<T>> typeReference) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(fileName), typeReference);
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        Set<TweetPage> dataTweets = Save.loadTweetJSON(
                "D:\\Project\\OOP20241\\OOP_PROJECT_G4_20241\\src\\main\\resources\\testTweet.json",
                new TypeReference<Set<TweetPage>>() {}
        );
        Set<String> tweetlinks = new HashSet<>();
        for (TweetPage dataTweet : dataTweets ){
            tweetlinks.add(dataTweet.getTweetId());
        }
        for (String tweetlink : tweetlinks) {
            System.out.println(tweetlink);
        }
        System.out.println(tweetlinks.size());

        Set<UserPage> dataUsers = Save.loadTweetJSON(
                "D:\\Project\\OOP20241\\OOP_PROJECT_G4_20241\\src\\main\\resources\\testUser.json",
                new TypeReference<Set<UserPage>>() {}
        );
        Set<String> userlinks = new HashSet<>();
        for (UserPage dataUser : dataUsers) {
            userlinks.add(dataUser.getUserId());
        }
        for (String userlink : userlinks) {
            System.out.println(userlink);
        }
        System.out.println(userlinks.size());
    }


}
