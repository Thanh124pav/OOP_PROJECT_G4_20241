package crawl;

import graph.Tweet;
import graph.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;
import java.util.Set;

public class Save {
    private final String fileName;
    private Set<User> users = new HashSet<>();
    private Set<Tweet> tweets = new HashSet<>();

    Save(String fileName, Set<User> users, Set<Tweet> tweets) {
        this.fileName = fileName;
        this.users = users;
        this.tweets = tweets;
    }
    public Save(String fileName, Set<?> elements) {
        this.fileName = fileName;
        if (!elements.isEmpty()) {
            Object firstElement = elements.iterator().next();
            if (firstElement instanceof Tweet) {
                this.tweets = (Set<Tweet>) elements;
            } else if (firstElement instanceof User) {
                this.users = (Set<User>) elements;
            } else {
                throw new IllegalArgumentException("Elements must be of type User or Tweet");
            }
        }
    }
    public void saveToJSON() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(fileName), this);
    }

    public static Save loadFromJSON(String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(fileName), Save.class);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        User user = new User("https://x.com/RealCandaceO");
        user.setBasicInfo();
        user.setFollowingUsers();
        Save saveData = new Save("test.json", user.getFollowingUsers());
        saveData.saveToJSON();
        System.out.println("Save successfully!");
    }
}
