package model;

import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class Nodes {
    private List<User> users;
    private List<Tweet> tweets;

    // Getters and Setters
    public List<User> getUsers() { return users; }
    public void setUsers(List<User> users) { this.users = users; }
    public List<Tweet> getTweets() { return tweets; }
    public void setTweets(List<Tweet> tweets) { this.tweets = tweets; }

    // Constructor không tham số
    public Nodes() {}

    // Constructor với dữ liệu được nạp từ file JSON
    public Nodes(String filePath) {
        loadDataFromFile(filePath);
    }

    // Phương thức để load dữ liệu từ file JSON
    private void loadDataFromFile(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Load dữ liệu JSON vào đối tượng Nodes
            Nodes nodes = objectMapper.readValue(new File(filePath), Nodes.class);
            this.users = nodes.getUsers();
            this.tweets = nodes.getTweets();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading data from file: " + filePath);
        }
    }

    // Phương thức tĩnh để tải dữ liệu từ file nếu cần
    public static Nodes loadFromFile(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Đọc JSON từ file và trả về đối tượng Nodes
            return objectMapper.readValue(new File(filePath), Nodes.class);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading data from file: " + filePath);
            return null;
        }
    }
}

