package guis;

import database.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class BaseDialog extends JDialog implements ActionListener {
    protected User user;
    protected BankAppGUI bankAppGUI;

    public BaseDialog(BankAppGUI bankAppGUI, User user, Component relativeTo, int width, int height) {
        setSize(width, height);
        setModal(true);
        setLocationRelativeTo(relativeTo);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        this.bankAppGUI = bankAppGUI;
        this.user = user;
    }
}
