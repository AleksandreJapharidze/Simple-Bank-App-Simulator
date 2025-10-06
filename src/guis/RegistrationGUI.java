package guis;

import database.MySQLInteractor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegistrationGUI extends Base {
    public RegistrationGUI() {
        super("Registration");
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
        passwordLabel.setBounds(20, 220, super.getWidth() - 30, 25);
        passwordLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(passwordLabel);

        JPasswordField passwordTextField = new JPasswordField();
        passwordTextField.setBounds(20, 260, super.getWidth() - 50, 42);
        passwordTextField.setFont(new Font("Dialog", Font.PLAIN, 25));
        add(passwordTextField);

        JLabel rePasswordLabel = new JLabel("Re-enter password:");
        rePasswordLabel.setBounds(20, 312, super.getWidth() - 50, 42);
        rePasswordLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(rePasswordLabel);

        JPasswordField rePasswordTextField = new JPasswordField();
        rePasswordTextField.setBounds(20, 360, super.getWidth() - 50, 42);
        rePasswordTextField.setFont(new Font("Dialog", Font.PLAIN, 25));
        add(rePasswordTextField);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(20, 450, super.getWidth() - 50, 42);
        registerButton.setFont(new Font("Dialog", Font.PLAIN, 25));

        registerButton.addActionListener(e -> {
            String username = usernameTextField.getText();
            String password = String.valueOf(passwordTextField.getPassword());
            String rePassword = String.valueOf(rePasswordTextField.getPassword());

            if (validateRegistration(username, password, rePassword)) {
                if (MySQLInteractor.registerUser(username, password)) {
                    RegistrationGUI.this.dispose();
                    LoginGUI loginGUI = new LoginGUI();
                    JOptionPane.showMessageDialog(loginGUI, "Registration successful!");
                } else {
                    JOptionPane.showMessageDialog(RegistrationGUI.this, "Username already exists!");
                }
            } else {
                if (username.length() < 6) {
                    JOptionPane.showMessageDialog(RegistrationGUI.this, "Username must be at least 6 characters long!");
                } else if (!password.equals(rePassword)) {
                    JOptionPane.showMessageDialog(RegistrationGUI.this, "Passwords do not match!");
                } else {
                    JOptionPane.showMessageDialog(RegistrationGUI.this, "Invalid username and password!");
                }
            }
        });
        add(registerButton);

        JLabel loginOptionLabel = new JLabel("<html><a href=\"#\">Already have an account? Sign in here!</a></html>");
        loginOptionLabel.setBounds(0, 512, super.getWidth() - 20, 25);
        loginOptionLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        loginOptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginOptionLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginOptionLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                RegistrationGUI.this.dispose();
                new LoginGUI().setVisible(true);
            }
        });
        add(loginOptionLabel);
    }

    private boolean validateRegistration(String username, String password, String rePassword) {
        if (username.isEmpty() || password.isEmpty() || rePassword.isEmpty()) return false;
        if (username.length() < 6) return false;
        if (!password.equals(rePassword)) return false;

        return true;
    }
}
