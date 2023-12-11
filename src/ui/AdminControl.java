package ui;

import user.Tweet;
import user.User;
import user.UserComponent;
import user.UserGroup;
import visitor.AnalysisVisitor;
import visitor.NodeVisitor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

public class AdminControl extends JFrame {
    private static AdminControl instance;
    public static UserComponent curUserSelected;
    private UserGroup root;
    private JPanel treePanel;
    private JTextField textUser;
    private JTextField textGroup;
    private JButton btnAddUser;
    private JButton btnAddGroup;
    private JButton btnOpenUserView;
    private JButton btnShowUserTotal;
    private JButton btnShowGroupTotal;
    private JButton btnShowMessagesTotal;
    private JButton btnShowPosPercent;

    // Action listeners for each button on the admin control
    private ActionListener actLastUpdatedUser = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            User lastUpdatedUser = User.getLastUpdatedUser();

            if (lastUpdatedUser != null) {
                JOptionPane.showMessageDialog(AdminControl.this, "Last Updated User: " + lastUpdatedUser.getId());
            } else {
                JOptionPane.showMessageDialog(AdminControl.this, "No users have been updated yet.");
            }
        }
    };
    private ActionListener actAddUser = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            addToGroup(new User(textUser.getText()));
            textUser.setText("");
            drawTree();
        }
    };
    private ActionListener actAddGroup = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            addToGroup(new UserGroup(textGroup.getText()));
            textGroup.setText("");
            drawTree();
        }
    };
    private ActionListener actOpenUserView = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(curUserSelected instanceof User) {
                new UserView((User) curUserSelected);
            }
        }
    };

    private ActionListener actVerifyIDs = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            verifyIDs();
        }
    };


    private ActionListener actShowUserTotal = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            new StatWindow("User Total: " + User.getUserTotal());
        }
    };
    private ActionListener actShowGroupTotal = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            new StatWindow("Group Total: " + UserGroup.getGroupTotal());
        }
    };
    private ActionListener actShowMessagesTotal = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            new StatWindow("Messages Total: " + Tweet.getTweetTotal());
        }
    };
    private ActionListener actShowPosPercent = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            new StatWindow("Positive Percentage: " + Tweet.getPositivePercentage() + "%");
        }
    };

    private AdminControl () {
        try {
            // Use the system look and feel
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }


        root = new UserGroup("Root");
        // Set up frame
        this.setTitle("MiniTwitter");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(750, 500);
        this.setLayout(new GridLayout(1, 2));


        // Set up tree view panel
        treePanel = new JPanel();
        Color treePanelColor = new Color(210, 250, 250);
        treePanel.setLayout(new BoxLayout(treePanel, BoxLayout.PAGE_AXIS));
        treePanel.setBackground(treePanelColor);
        this.add(treePanel);
        drawTree();


        // Set up control view panel (panel for all controls on the right side of UI)
        JPanel controlPanel = new JPanel();
        Color controlPanelColor = new Color(210, 250, 250);
        controlPanel.setBackground(controlPanelColor);
        controlPanel.setLayout(new GridLayout(0, 1, 0, 4));


        // Set up add view panel
        JPanel addPanel = new JPanel();
        addPanel.setBackground(controlPanelColor);
        addPanel.setLayout(new GridLayout(2, 2, 4, 4));
        textUser = new JTextField(16);
        addPanel.add(textUser);
        btnAddUser = new JButton("Add User");
        btnAddUser.addActionListener(actAddUser);
        addPanel.add(btnAddUser);
        textGroup = new JTextField(16);
        addPanel.add(textGroup);
        btnAddGroup = new JButton("Add Group");
        btnAddGroup.addActionListener(actAddGroup);
        addPanel.add(btnAddGroup);
        controlPanel.add(addPanel);


        // Set up UV (User View) open view panel
        JPanel uvOpenPanel = new JPanel();
        uvOpenPanel.setBackground(controlPanelColor);
        uvOpenPanel.setLayout(new GridLayout(3, 1));
        btnOpenUserView = new JButton("Open User View");
        btnOpenUserView.addActionListener(actOpenUserView);
        uvOpenPanel.add(btnOpenUserView);
        controlPanel.add(uvOpenPanel);
        disableUserView();


        // Set up stats view panel
        JPanel statsPanel = new JPanel();
        statsPanel.setBackground(controlPanelColor);
        statsPanel.setLayout(new GridLayout(2, 2, 4, 4));
        btnShowUserTotal = new JButton("Show User Total");
        btnShowUserTotal.addActionListener(actShowUserTotal);
        statsPanel.add(btnShowUserTotal);
        btnShowGroupTotal = new JButton("Show Group Total");
        btnShowGroupTotal.addActionListener(actShowGroupTotal);
        statsPanel.add(btnShowGroupTotal);
        btnShowMessagesTotal = new JButton("Show Messages Total");
        btnShowMessagesTotal.addActionListener(actShowMessagesTotal);
        statsPanel.add(btnShowMessagesTotal);
        btnShowPosPercent = new JButton("Show Positive Percentage");
        btnShowPosPercent.addActionListener(actShowPosPercent);
        statsPanel.add(btnShowPosPercent);
        controlPanel.add(statsPanel);

        JPanel verifyPanel = new JPanel();
        verifyPanel.setBackground(controlPanelColor);
        verifyPanel.setLayout(new GridLayout(1, 1, 4, 4));
        JButton btnVerifyIDs = new JButton("Verify IDs");
        btnVerifyIDs.addActionListener(actVerifyIDs);
        verifyPanel.add(btnVerifyIDs);
        controlPanel.add(verifyPanel);

        // Add a button to display the last updated user
        JButton btnLastUpdatedUser = new JButton("Last Updated User");
        btnLastUpdatedUser.addActionListener(actLastUpdatedUser);
        statsPanel.add(btnLastUpdatedUser);



        this.add(controlPanel);
        this.setVisible(true);
    }

    private void verifyIDs() {
        Set<String> allIDs = new HashSet<>();
        boolean isValid = true;

        // Define a local visitor function to traverse the tree and collect IDs
        NodeVisitor idVerificationVisitor = new AnalysisVisitor() {
            public void visitUser(User user) {
                allIDs.add(user.getUserID());
            }

            public void visitUserGroup(UserGroup group) {
                allIDs.add(group.getGroupID());

                // If you have nested groups, you might need to recursively visit them
                for (UserComponent component : group.getComponents()) {
                    component.accept(this);
                }
            }
        };

        // Traverse the tree and collect all user and group IDs
        root.accept(idVerificationVisitor);

        // Check for duplicates and spaces in IDs
        for (String id : allIDs) {
            if (!isValidID(id)) {
                isValid = false;
                break;
            }
        }

        // Display the result in a dialog or console
        if (isValid) {
            JOptionPane.showMessageDialog(this, "All IDs are valid.");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid IDs found. Please ensure IDs are unique and do not contain spaces.");
        }
    }

    private boolean isValidID(String id) {
        return !id.contains(" ") && !id.isBlank();
    }



    // Singleton Instance
    public static AdminControl getInstance() {
        if(instance == null) {
            instance = new AdminControl();
        }

        return instance;
    }

    //Bind user view
    public void enableUserView() {
        btnOpenUserView.setEnabled(true);
    }

    //Unbind user view
    public void disableUserView() {
        btnOpenUserView.setEnabled(false);
    }

    // Add a UserComponent to a UserGroup
    private void addToGroup(UserComponent componentToAdd) {
        if(curUserSelected instanceof UserGroup) {
            ((UserGroup) curUserSelected).add(componentToAdd);
        }
        else {
            root.add(componentToAdd);
        }

        NodeVisitor analysisVisitor = new AnalysisVisitor();
        componentToAdd.accept(analysisVisitor);
    }

    // Output the List of users and groups
    public void drawTree() {
        treePanel.removeAll();
        JLabel treeLabel = new JLabel("Tree View");
        treePanel.add(treeLabel);
        root.display(treePanel);
        treePanel.revalidate();
        treePanel.repaint();
    }

}