package guis;

import database.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BankAppGUI extends Base implements ActionListener {
    private JTextField currentBalanceTextField;

    public JTextField getCurrentBalanceTextField() {
        return currentBalanceTextField;
    }

    public BankAppGUI(User user) {
        super("Bank App", user);
    }

    @Override
    protected void addGUIComponents() {
        String welcomeMessage = "<html>" +
                "<body style='text-align: center;'>" +
                "<b>Hello, " + user.getUsername() + "!</b><br>" +
                "Which operation would you like to perform today?</body></html>";

        JLabel welcomeMessageLabel = new JLabel(welcomeMessage);
        welcomeMessageLabel.setBounds(0, 20, super.getWidth() - 10, 48);
        welcomeMessageLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
        welcomeMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcomeMessageLabel);

        JLabel currentBalanceLabel = new JLabel("Current Balance");
        currentBalanceLabel.setBounds(0, 80, super.getWidth() - 10, 32);
        currentBalanceLabel.setFont(new Font("Dialog", Font.BOLD, 22));
        currentBalanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(currentBalanceLabel);

        currentBalanceTextField = new JTextField("$" + user.getCurrentBalance());
        currentBalanceTextField.setBounds(15, 120, super.getWidth() - 50, 40);
        currentBalanceTextField.setFont(new Font("Dialog", Font.BOLD, 28));
        currentBalanceTextField.setHorizontalAlignment(SwingConstants.RIGHT);
        currentBalanceTextField.setEditable(false);
        add(currentBalanceTextField);

        JButton depositButton = new JButton("Deposit");
        depositButton.setBounds(15, 180, super.getWidth() - 50, 42);
        depositButton.setFont(new Font("Dialog", Font.BOLD, 22));
        depositButton.addActionListener(this);
        add(depositButton);

        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.setBounds(15, 240, super.getWidth() - 50, 42);
        withdrawButton.setFont(new Font("Dialog", Font.BOLD, 22));
        withdrawButton.addActionListener(this);
        add(withdrawButton);

        JButton pastTransactionButton = new JButton("Past transaction");
        pastTransactionButton.setBounds(15, 300, super.getWidth() - 50, 42);
        pastTransactionButton.setFont(new Font("Dialog", Font.BOLD, 22));
        pastTransactionButton.addActionListener(this);
        add(pastTransactionButton);

        JButton transferButton = new JButton("Transfer");
        transferButton.setBounds(15, 360, super.getWidth() - 50, 42);
        transferButton.setFont(new Font("Dialog", Font.BOLD, 22));
        transferButton.addActionListener(this);
        add(transferButton);

        JButton billsButton = new JButton("Bills");
        billsButton.setBounds(15, 420, super.getWidth() / 2 - 40, 42);
        billsButton.setFont(new Font("Dialog", Font.BOLD, 18));
        billsButton.addActionListener(this);
        add(billsButton);

        JButton deleteAccountButton = new JButton("Delete account");
        deleteAccountButton.setBounds(230, 420, super.getWidth() / 2 - 40, 42);
        deleteAccountButton.setFont(new Font("Dialog", Font.BOLD, 18));
        deleteAccountButton.addActionListener(this);
        add(deleteAccountButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(15, 500, super.getWidth() - 50, 42);
        logoutButton.setFont(new Font("Dialog", Font.BOLD, 22));
        logoutButton.addActionListener(this);
        add(logoutButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String pressedButtonCommand = e.getActionCommand();
        if (pressedButtonCommand.equalsIgnoreCase("Logout")) {
            new LoginGUI().setVisible(true);
            this.dispose();
            return;
        } else if (pressedButtonCommand.equalsIgnoreCase("Bills")) {
            new BillsGUI(this, user).setVisible(true);
            this.dispose();
            return;
        }

        BankAppDialog bankAppDialog = new BankAppDialog(this, user);
        bankAppDialog.setTitle(pressedButtonCommand);
        if (pressedButtonCommand.equalsIgnoreCase("Deposit") || pressedButtonCommand.equalsIgnoreCase("Withdraw")
                || pressedButtonCommand.equalsIgnoreCase("Transfer")) {
            bankAppDialog.addCurrentBalanceAndAmount();
            bankAppDialog.addActionButton(pressedButtonCommand);
            if (pressedButtonCommand.equalsIgnoreCase("Transfer")) {
                bankAppDialog.addUserField();
            }
        } else if (pressedButtonCommand.equalsIgnoreCase("Logout")) {
            bankAppDialog.setVisible(false);
            return;
        } else if (pressedButtonCommand.equalsIgnoreCase("Past transaction")) {
            bankAppDialog.addPastTransactionComponents();
        } else if (pressedButtonCommand.equalsIgnoreCase("Delete account")) {
            int option = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete your account?",
                    "Delete account",
                    JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                bankAppDialog.addAccountDeletionLabel();
                bankAppDialog.addPasswordTextField();
                bankAppDialog.addActionButton("Delete account");
            } else {
                return;
            }
        }
        bankAppDialog.setVisible(true);
    }
}
