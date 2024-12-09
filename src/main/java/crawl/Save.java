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
    private Set<TweetPage> tweets;
    private Set<UserPage> users;
    public Save(String fileName) {
        this.fileName = fileName;
    }

    public Save(String fileName, Set<TweetPage> tweets, Set<UserPage> users) {
        this.fileName = fileName;
        this.tweets = tweets;
        this.users = users;
    }
    public void saveToJSON() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), this);
    }

    public void saveToJSON(Object o) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), o);
    }

    public static <T> Set<T> loadTweetJSON(String fileName, TypeReference<Set<T>> typeReference) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(fileName), typeReference);
    }

    public Set<TweetPage> getTweets() {
        return tweets;
    }

    public Set<UserPage> getUsers() {
        return users;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Set<TweetPage> dataTweets = new HashSet<>();
        Set<UserPage> dataUsers = new HashSet<>();
        for (int i = 5; i <= 25; i += 5){
            String fileNameTweet = "D:\\Project\\OOP20241\\OOP_PROJECT_G4_20241\\src\\main\\resources\\small_data\\TweetBTC_ETC_Crypto_" + i + ".json";
            Set<TweetPage> subDataTweets = Save.loadTweetJSON(
                    fileNameTweet,
                    new TypeReference<Set<TweetPage>>() {}
            );
            dataTweets.addAll(subDataTweets);

            String fileNameUser = "D:\\Project\\OOP20241\\OOP_PROJECT_G4_20241\\src\\main\\resources\\small_data\\UserBTC_ETC_Crypto_" + i + ".json";
            Set<UserPage> subDataUsers = Save.loadTweetJSON(
                    fileNameUser,
                    new TypeReference<Set<UserPage>>() {}
            );
            dataUsers.addAll(subDataUsers);
        }

        String fileName = "D:\\Project\\OOP20241\\OOP_PROJECT_G4_20241\\src\\main\\resources\\Data25BTC_ETC_Crypto.json";
        Save save = new Save(fileName, dataTweets, dataUsers);
        save.saveToJSON();
        System.out.println("Merge and save sucessfully!");
    }


}
