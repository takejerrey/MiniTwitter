package visitor;

import user.Tweet;
import user.User;
import user.UserGroup;

public interface NodeVisitor {

    public void visit(User user);
    public void visit(UserGroup userGroup);
    public void visit(Tweet tweet);
    public void visit(String message);

}