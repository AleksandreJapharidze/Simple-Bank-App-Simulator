package guis;

import database.MySQLInteractor;
import database.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginGUI extends Base {
    public LoginGUI() {
        super("Login");
        setVisible(true);
    }

    @Override
    protected void addGUIComponents() {
        JLabel bankAppLabel = new JLabel("Simple Bank Application");
        bankAppLabel.setBounds(0, 20, super.getWidth(), 40);
        bankAppLabel.setFont(new Font("Dialog", Font.BOLD, 30));
        bankAppLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(bankAppLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(20, 120, super.getWidth() - 30, 25);
        usernameLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(usernameLabel);

        JTextField usernameTextField = new JTextField();
        usernameTextField.setBounds(20, 160, super.getWidth() - 50, 42);
        usernameTextField.setFont(new Font("Dialog", Font.PLAIN, 25));
        add(usernameTextField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20, 280, super.getWidth() - 30, 25);
        passwordLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(passwordLabel);

        JPasswordField passwordTextField = new JPasswordField();
        passwordTextField.setBounds(20, 320, super.getWidth() - 50, 42);
        passwordTextField.setFont(new Font("Dialog", Font.PLAIN, 25));
        add(passwordTextField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(20, 450, super.getWidth() - 50, 42);
        loginButton.setFont(new Font("Dialog", Font.PLAIN, 25));
        loginButton.addActionListener(e -> {
            String username = usernameTextField.getText();
            String password = String.valueOf(passwordTextField.getPassword());

            User user = MySQLInteractor.validateLogin(username, password);
            if (user != null) {
                new BankAppGUI(user);
                dispose();
                BankAppGUI bankAppGUI = new BankAppGUI(user);
                bankAppGUI.setVisible(true);

                JOptionPane.showMessageDialog(bankAppGUI, "Login successful!");
            } else {
                JOptionPane.showMessageDialog(LoginGUI.this, "Invalid username or password! Please try again");
            }
        });
        add(loginButton);

        JLabel registerLabel = new JLabel("<html><a href=\"#\">Don't have an account? Click here to register!</a></html>");
        registerLabel.setBounds(0, 512, super.getWidth() - 20, 25);
        registerLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                LoginGUI.this.dispose();
                new RegistrationGUI().setVisible(true);
            }
        });
        add(registerLabel);
    }
}
