package crawl;

import com.fasterxml.jackson.databind.ObjectMapper;
//import graph.UserOld;


import java.io.File;
import java.io.IOException;

public class Save {
    private final String fileName;

    public Save(String fileName) {
        this.fileName = fileName;
    }
    public void saveToJSON(Object o) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), o);
    }

    public static Save loadFromJSON(String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(fileName), Save.class);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
//        User user = new User("https://x.com/RealCandaceO");
//        user.setBasicInfo();
//        user.setFollowingUsers();
//        user.setTweets(10);
//        user.setTweetCount();

    }


}
