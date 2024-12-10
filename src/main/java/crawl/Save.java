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

    public static <T> Set<T> loadJSON(String fileName, TypeReference<Set<T>> typeReference) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(fileName), typeReference);
    }

    public Set<TweetPage> getTweets() {
        return tweets;
    }

    public Set<UserPage> getUsers() {
        return users;
    }



}
