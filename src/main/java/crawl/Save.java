package crawl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import model.Tweet;
import model.User;
//import graph.UserOld;


import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Save {
    private final String FILE_NAME;
    private Set<Tweet> tweets;
    private Set<User> users;

    public Save(String fileName) {
        this.FILE_NAME = fileName;
    }

    public Save(String FILE_NAME, Set<Tweet> tweets, Set<User> users) {
        this.FILE_NAME = FILE_NAME;
        this.tweets = tweets;
        this.users = users;
    }
    public void saveToJSON() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_NAME), this);
    }

    public void saveToJSON(Object o) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_NAME), o);
    }

    public static <T> Set<T> loadJSON(String fileName, TypeReference<Set<T>> typeReference) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(fileName), typeReference);
    }

    public static void loadSubData(Set<Tweet> dataTweets, Set<User> dataUsers, String prefixName, int i) throws IOException {
        Set <Tweet> subDataTweets = Save.loadJSON(
                "src/main/resources/small_data/Tweet" + prefixName + i + ".json",
                new TypeReference<Set<Tweet>>() {}
        );
        dataTweets.addAll(subDataTweets);

        Set <User> subDataUsers = Save.loadJSON(
                "src/main/resources/small_data/User" + prefixName + i + ".json",
                new TypeReference<Set<User>>() {}
        );
        dataUsers.addAll(subDataUsers);
    }

    public Set<Tweet> getTweets() {
        return tweets;
    }

    public Set<User> getUsers() {
        return users;
    }


    public static void main(String[] args) throws IOException {
        Set<Tweet> dataTweets = new HashSet<>();
        Set<User> dataUsers = new HashSet<>();
        for (int i = 5; i <= 110; i += 5){
            loadSubData(dataTweets, dataUsers, "Blc_Web3_", i);
        }
        for (int i = 5; i <= 40; i += 5){
            loadSubData(dataTweets, dataUsers, "BTC_ETC_Crypto_", i);
        }
        for (int i = 41; i <= 149; i += 1){
            loadSubData(dataTweets, dataUsers, "BTC_ETC_Crypto_", i);
        }
        for (int i = 176; i <= 257; i += 1){
            loadSubData(dataTweets, dataUsers, "BTC_ETC_Crypto_", i);
        }
        dataUsers.addAll(
                Save.loadJSON(
                "src/main/resources/small_data/userRetweets.json",
                new TypeReference<Set<User>>() {}
                )
        );
        Save allData = new Save("src/main/resources/allData.json", dataTweets, dataUsers);
        allData.saveToJSON();
        System.out.printf("finish merge sub data! \nNumber of Tweets: %d, \nNumber of Users: %d\n", dataTweets.size(), dataUsers.size());
    }

}
