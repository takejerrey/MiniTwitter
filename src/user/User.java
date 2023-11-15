package user;

import observable.Observer;
import ui.UserView;
import visitor.NodeVisitor;

import javax.swing.*;
import java.util.*;

public class User extends user.UserComponent implements Observer {
    private static List<User> allUsers = new ArrayList<>();
    private static int userTotal = 0;
    private UserView curUserView;
    private List<User> followings;
    private List<User> followers;
    private PostedTweets tweetsPosted;
    private Map<String, Tweet> feed;

    public User(String id) {
        super(id);
        feed = new HashMap<>();
        allUsers.add(this);
        followings = new ArrayList<>();
        followers = new ArrayList<>();
        tweetsPosted = new PostedTweets();
        follow(this);
    }

    public static User getUserById(String id) {
        User foundUser = null;
        for(User user : allUsers) {
            if(foundUser == null && user.getId().equals(id)) {
                foundUser = user;
            }
        }
        return foundUser;
    }

    public static int getUserTotal() {

        return userTotal;
    }



    public List<User> getFollowings() {

        return followings;
    }

    public void incrementUserTotal() {

        userTotal++;
    }

    public Collection<Tweet> getOrderedFeedMessages() {
        List<Tweet> orderedFeed = new ArrayList<>(feed.values());
        orderedFeed.sort(Comparator.comparing(Tweet::getOrderCreated));
        return orderedFeed;
    }

    public void display(JPanel displayPanel) {
        displayPanel.add(getLabel("\uD83D\uDC64 " + getId()));
    }

    public void post(Tweet tweet) {
        tweetsPosted.post(tweet);
    }

    public void follow(User user) {
        user.tweetsPosted.attachObserver(this);
        followings.add(user);
        user.followers.add(this);

        if(curUserView != null) {
            curUserView.drawFollowing(followings);
        }
    }

    // Bind the instance of the UserView to the User
    public void bindUserView(UserView feedView) {
        curUserView = feedView;
    }

    public void unbindUserView() {
        curUserView = null;
    }

    @Override
    public void update() {
        for(User followedUser : followings) {
            List<Tweet> tweetsByUser = followedUser.tweetsPosted.getTweets();

            for(Tweet tweet : tweetsByUser) {
                String curTweetId = tweet.getId();

                if(!feed.containsKey(curTweetId)) {
                    feed.put(curTweetId, tweet);
                }
            }
        }
        curUserView.drawInfo();
        curUserView.drawFeed(getOrderedFeedMessages());
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

}
