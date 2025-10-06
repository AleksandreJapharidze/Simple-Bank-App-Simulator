package guis;

import database.MySQLInteractor;
import database.Transaction;
import database.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;

public class BankAppDialog extends JDialog implements ActionListener {
    private User user;
    private BankAppGUI bankAppGUI;
    private LoginGUI loginGUI;
    private JLabel balanceLabel, amountLabel, userLabel, accountDeletionLabel;
    private JTextField amountTextField, userTextField;
    private JPasswordField passwordTextField;
    private JButton actionButton;
    private JPanel pastTransactionPanel;
    private ArrayList<Transaction> pastTransactions;

    public BankAppDialog(BankAppGUI bankAppGUI, User user) {
        setSize(400, 400);
        setModal(true);
        setLocationRelativeTo(bankAppGUI);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        this.bankAppGUI = bankAppGUI;
        this.user = user;
    }

    public void addCurrentBalanceAndAmount() {
        balanceLabel = new JLabel("Current balance: $" + user.getCurrentBalance());
        balanceLabel.setBounds(0, 10, super.getWidth() - 20, 20);
        balanceLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(balanceLabel);

        amountLabel = new JLabel("Enter amount:");
        amountLabel.setBounds(0, 50, super.getWidth() - 20, 20);
        amountLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        amountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(amountLabel);

        amountTextField = new JTextField();
        amountTextField.setBounds(15, 80, super.getWidth() - 50, 40);
        amountTextField.setFont(new Font("Dialog", Font.BOLD, 20));
        amountTextField.setHorizontalAlignment(SwingConstants.CENTER);
        add(amountTextField);
    }

    public void addActionButton(String actionButtonType) {
        actionButton = new JButton(actionButtonType);
        actionButton.setBounds(15, 300, super.getWidth() - 50, 40);
        actionButton.setFont(new Font("Dialog", Font.BOLD, 20));
        actionButton.addActionListener(this);
        add(actionButton);
    }

    public void addAccountDeletionLabel() {
        accountDeletionLabel = new JLabel("<html><body>Make sure you have $0 balance.<br> " +
                "Otherwise it's not possible to delete your account!</body></html>");
        accountDeletionLabel.setBounds(0, 10, super.getWidth() - 20, 40);
        accountDeletionLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        accountDeletionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(accountDeletionLabel);
    }

    public void addUserField() {
        userLabel = new JLabel("Enter username:");
        userLabel.setBounds(0, 160, super.getWidth() - 20, 20);
        userLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        userLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(userLabel);

        userTextField = new JTextField();
        userTextField.setBounds(15, 190, super.getWidth() - 50, 40);
        userTextField.setFont(new Font("Dialog", Font.BOLD, 20));
        userTextField.setHorizontalAlignment(SwingConstants.CENTER);
        add(userTextField);
    }

    public void addPasswordTextField() {
        passwordTextField = new JPasswordField();
        passwordTextField.setBounds(15, 100, super.getWidth() - 50, 40);
        passwordTextField.setFont(new Font("Dialog", Font.BOLD, 20));
        add(passwordTextField);
    }

    public void addPastTransactionComponents() {
        pastTransactionPanel = new JPanel();
        pastTransactionPanel.setLayout(new BoxLayout(pastTransactionPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(pastTransactionPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(0, 20, getWidth() - 15, getHeight() - 80);

        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(20);

        pastTransactions = MySQLInteractor.getPastTransactions(user);

        for (Transaction pastTransaction : pastTransactions) {
            JPanel pastTransactionContainer = new JPanel();
            pastTransactionContainer.setLayout(new BorderLayout());

            JLabel transactionTypeLabel = new JLabel(pastTransaction.getTransactionType());
            transactionTypeLabel.setFont(new Font("Dialog", Font.BOLD, 20));

            JLabel transactionAmountLabel = new JLabel(String.valueOf(pastTransaction.getTransactionAmount()));
            transactionAmountLabel.setFont(new Font("Dialog", Font.BOLD, 20));

            JLabel transactionDateLabel = new JLabel(String.valueOf(pastTransaction.getTransactionDate()));
            transactionDateLabel.setFont(new Font("Dialog", Font.BOLD, 20));

            pastTransactionContainer.add(transactionTypeLabel, BorderLayout.WEST);
            pastTransactionContainer.add(transactionAmountLabel, BorderLayout.EAST);
            pastTransactionContainer.add(transactionDateLabel, BorderLayout.SOUTH);

            pastTransactionContainer.setBackground(Color.WHITE);
            pastTransactionContainer.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            pastTransactionPanel.add(pastTransactionContainer);
        }

        add(scrollPane);
    }

    private void handleTransaction(String transactionType, float amount) {
        Transaction transaction = null;
        if (transactionType.equalsIgnoreCase("Deposit")) {
            user.setCurrentBalance(user.getCurrentBalance().add(new BigDecimal(amount)));
            transaction = new Transaction(user.getId(), transactionType, new BigDecimal(amount), null);
        } else if (transactionType.equalsIgnoreCase("Withdraw")) {
            user.setCurrentBalance(user.getCurrentBalance().subtract(new BigDecimal(amount)));
            transaction = new Transaction(user.getId(), transactionType, new BigDecimal(-amount), null);
        }

        if (MySQLInteractor.addTransactionToDatabase(transaction) && MySQLInteractor.updateCurrentBalance(user)) {
            JOptionPane.showMessageDialog(this, "Successful!");
            resetFieldsAndUpdateCurrentBalance();
        } else {
            JOptionPane.showMessageDialog(this, "Failed!");
        }
    }

    private void handleTransfer(User user, String transferUsername, float transferAmount) {
        Transaction transaction = null;
        if (MySQLInteractor.transfer(user, transferUsername, transferAmount)) {
            JOptionPane.showMessageDialog(this, "Transfer successful!");
            resetFieldsAndUpdateCurrentBalance();
        } else {
            if (user.getCurrentBalance().compareTo(BigDecimal.valueOf(transferAmount)) < 0) {
                JOptionPane.showMessageDialog(this, "Transfer failed! Insufficient funds!");
            }
            JOptionPane.showMessageDialog(this, "Transfer failed! Invalid username!");
        }
    }

    private void handleAccountDeletion(User user) {
        if (user.getCurrentBalance().compareTo(BigDecimal.ZERO) == 0 &&
                String.valueOf(passwordTextField.getPassword()).equals(user.getPassword())) {
            if (MySQLInteractor.deleteUserAccount(user)) {
                JOptionPane.showMessageDialog(this, "Account deleted successfully!");
                bankAppGUI.dispose();
                this.dispose();
                loginGUI = new LoginGUI();
                loginGUI.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete account!");
            }
        } else if (user.getCurrentBalance().compareTo(BigDecimal.ZERO) == 0 &&
                !String.valueOf(passwordTextField.getPassword()).equals(user.getPassword())){
            JOptionPane.showMessageDialog(this, "Invalid password!");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete account!");
        }
    }

    private void resetFieldsAndUpdateCurrentBalance() {
        amountTextField.setText("");

        if (amountTextField != null) {
            amountTextField.setText("");
        }

        balanceLabel.setText("Current balance: $" + user.getCurrentBalance());
        bankAppGUI.getCurrentBalanceTextField().setText("$" + user.getCurrentBalance());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String pressedButtonCommand = e.getActionCommand();

        if (pressedButtonCommand.equalsIgnoreCase("Delete account")) {
            handleAccountDeletion(user);
            return;
        }


        float amount = Float.parseFloat(amountTextField.getText());
        if (pressedButtonCommand.equalsIgnoreCase("Deposit")) {
            handleTransaction(pressedButtonCommand, amount);
        } else {
            int result = user.getCurrentBalance().compareTo(BigDecimal.valueOf(amount));
            if (result < 0) {
                JOptionPane.showMessageDialog(this, "Error: Insufficient funds!");
                return;
            }

            if (pressedButtonCommand.equalsIgnoreCase("Withdraw")) {
                handleTransaction(pressedButtonCommand, amount);
            } else if (pressedButtonCommand.equalsIgnoreCase("Transfer")) {
                String transferUser = userTextField.getText();
                handleTransfer(user, transferUser, amount);
            }
        }
    }
}
