package user;

import ui.AdminControl;
import visitor.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public abstract class UserComponent implements Node {

    private static final Color TRANSPARENT = new Color(0, 0, 0, 0);
    private static final Color SELECTED = new Color(160, 180, 255);

    private static List<String> allIds = new ArrayList<>();

    // Spacing to add to start of label for UserComponent in tree view
    protected static int spacing = 0;

    private String id;
    private JLabel label;

    public UserComponent(String id) {
        setId(id);
    }

    // Returns true if all IDs (Users and UserGroups) are valid, false otherwise
    public static boolean validateIds() {
        boolean isValid = true;

        for(String curId : allIds) {
            // Spaces or duplicates will make ID invalid
            if(curId.contains(" ") || (allIds.indexOf(curId)
                    != allIds.lastIndexOf(curId))) {
                isValid = false;
            }
        }

        return isValid;
    }

    public void setId(String id) {
        this.id = id;
        allIds.add(id);
    }

    public String getId() {

        return id;
    }


    protected JLabel getLabel(String labelText) {
        if(label == null) {
            label = new JLabel();
            labelText = String.format("%" + (spacing + labelText.length()) + "s", labelText);
            label.setText(labelText);
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    UserComponent curUserSelected = AdminControl.curUserSelected;

                    if(curUserSelected != null) {
                        JLabel curSelectedLabel = curUserSelected.getLabel(null);
                        curSelectedLabel.setOpaque(false);
                        curSelectedLabel.setBackground(TRANSPARENT);
                    }

                    label.setOpaque(true);
                    label.setBackground(SELECTED);
                    AdminControl.curUserSelected = UserComponent.this;

                    if(AdminControl.curUserSelected instanceof user.User) {
                        AdminControl.getInstance().enableUserView();
                    }
                    else {
                        AdminControl.getInstance().disableUserView();
                    }
                }
            });
        }

        return label;
    }

    public abstract void display(JPanel displayPanel);

}