package user;

import visitor.NodeVisitor;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class UserGroup extends user.UserComponent {

    private static int groupTotal = 1; // Initialized to 1 to account for root group
    private long creationTime;

    private List<user.UserComponent> userComponents;

    public long getCreationTime() {
        return creationTime;
    }
    public UserGroup(String id) {
        super(id);
        this.creationTime = System.currentTimeMillis();
        userComponents = new ArrayList<>();
    }

    public String getGroupID(){
        return getID();
    }

    public List<user.UserComponent> getComponents(){
        return userComponents;
    }

    public static int getGroupTotal() {
        return groupTotal;
    }

    public void add(user.UserComponent userComponent) {
        userComponents.add(userComponent);
    }

    public void display(JPanel displayPanel) {
        // Dislay ID for current UserGroup
        displayPanel.add(getLabel("\uD83D\uDCC1 " + getId()));
        spacing += 2;

        // Display all UserComponents in this group
        for(user.UserComponent userComponent : userComponents) {
            userComponent.display(displayPanel);
        }

        spacing -= 2;
    }

    public void incrementGroupTotal() {
        groupTotal++;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

}