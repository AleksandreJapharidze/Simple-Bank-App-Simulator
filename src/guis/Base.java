package guis;

import database.User;

import javax.swing.*;

public abstract class Base extends JFrame {
    protected User user;

    public Base(String title, User user) {
        this.user = user;

        initialize(title);
    }

    public Base(String title) {
        initialize(title);
    }

    private void initialize(String title) {
        setTitle(title);
        setSize(450, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        addGUIComponents();
    }

    protected abstract void addGUIComponents();
}
