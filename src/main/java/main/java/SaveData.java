package main.java;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class SaveData {
    private final String fileName;
    private Set<User> users = new HashSet<>();
    private Set<Tweet> tweets = new HashSet<>();

    SaveData(String fileName, Set<User> users, Set<Tweet> tweets) {
        this.fileName = fileName;
        this.users = users;
        this.tweets = tweets;
    }
    SaveData(String fileName, Set<?> elements) {
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
    public void saveToCSV(String type) throws IOException {
        if(type.equals("User")){
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
                // Write the header
                writer.write("Link, Name\n");
                for (User user : users) {
                    writer.write(
                            String.format(
                                    "%s,%s%n", user.getLink(), user.getName()
                            )
                    );
                }
            }
        }
        else if(type.equals("Tweet")){
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
                writer.write("Link, Author\n");
                for (Tweet tweet : tweets) {
                    writer.write(String.format("%s,%s%n", tweet.getLink(), tweet.getAuthor().getName()));
                }
            }
        }

    }
}
