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
        Set<TweetPage> dataTweet35to75 = new HashSet<>();
        Set<UserPage> dataUsers35to75 = new HashSet<>();
        for (int i = 40; i <= 75; i += 5){
            String fileNameTweet = "D:\\Project\\OOP20241\\OOP_PROJECT_G4_20241\\src\\main\\resources\\TweetBlc_Web3_" + i + ".json";
            Set<TweetPage> subDataTweets = Save.loadTweetJSON(
                    fileNameTweet,
                    new TypeReference<Set<TweetPage>>() {}
            );
            dataTweet35to75.addAll(subDataTweets);

            String fileNameUser = "D:\\Project\\OOP20241\\OOP_PROJECT_G4_20241\\src\\main\\resources\\UserBlc_Web3_" + i + ".json";
            Set<UserPage> subDataUsers = Save.loadTweetJSON(
                    fileNameUser,
                    new TypeReference<Set<UserPage>>() {}
            );
            dataUsers35to75.addAll(subDataUsers);
        }

        String fileName = "D:\\Project\\OOP20241\\OOP_PROJECT_G4_20241\\src\\main\\resources\\Data35to75Blc_Web3.json";
        Save save = new Save(fileName, dataTweet35to75, dataUsers35to75);
        save.saveToJSON();
        System.out.println("Merge and save sucessfully!");
    }


}
