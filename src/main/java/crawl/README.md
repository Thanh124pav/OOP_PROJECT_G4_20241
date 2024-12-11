# Thuật toán thu thập dữ liệu 
Với mỗi từ khóa, ta tìm kiếm links các bài tweet về từ khóa đó \
Với mỗi tweet thu được, ta thu thập: 
- Thông tin cơ bản: ID, link, author 
- Thông tin chi tiết: số lượt tương tác, thời điểm đăng bài
- Thông tin nâng cao: danh sách những người retweet 

Với author và retweeters thu được, ta thu thập: 
- Thông tin cơ bản: userID
- Thông tin chi tiết: tên, số lượng following, followers
- Thông tin nâng cao: danh sách những người following

# Pseudocode
```
for (keyword : keywords){
    tweetLinks = search(keyword);
}
save(tweetLinks)

load(tweetLinks)
for (link : tweetLinks) {
    tweet = Tweet(link)
    tweet.extractBasicInfo();
    tweet.extractDetails(link);
    tweet.extractRetweeters(link);
    userIds = [authorId, retweeterIds];
    users = set()
    for (userId : userIds){
        user = User(userId);
        user.extractBasicInfo();
        user.extractDetails();
        user.extractFollowing();
        users.add(user)
    }
    save(tweet)
    save(users)
}
```