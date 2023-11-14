package visitor;

import user.Tweet;
import user.User;
import user.UserGroup;

public class AnalysisVisitor implements NodeVisitor {
    @Override
    public void visit(User user) {
        user.incrementUserTotal();
    }
    @Override
    public void visit(UserGroup userGroup) {
        userGroup.incrementGroupTotal();
    }
    @Override
    public void visit(Tweet tweet) {
        tweet.incrementTweetTotal();
    }
    @Override
    public void visit(String message) {
        message = message.toLowerCase();

        if(message.contains("good") || message.contains("great"))
        {
            Tweet.updatePositivePercent(true);
        }
        else {
            Tweet.updatePositivePercent(false);
        }
    }

}