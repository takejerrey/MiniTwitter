package user;

import observable.Subject;

import java.util.ArrayList;
import java.util.List;

// Subclass of Subject, for user observers
public class PostedTweets extends Subject {
    List<Tweet> tweets;
    public PostedTweets() {
        tweets = new ArrayList<>();
    }

    public void post(Tweet tweet) {
        tweets.add(tweet);
        notifyObservers();
    }

    public List<Tweet> getTweets() {
        return tweets;
    }

}