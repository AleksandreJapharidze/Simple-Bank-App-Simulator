package guis;

import database.MySQLInteractor;
import database.Transaction;
import database.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

public class BillsDialog extends JDialog implements ActionListener {
    private User user;
    private BankAppGUI bankAppGUI;
    private BillsGUI billsGUI;
    private JLabel balanceLabel;
    private JLabel typeLabel, amountLabel, dollarSignLabel;
    private JButton payButton;

    public BillsDialog(BillsGUI billsGUI, BankAppGUI bankAppGUI, User user) {
        setSize(300, 250);
        setModal(true);
        setLocationRelativeTo(billsGUI);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        this.billsGUI = billsGUI;
        this.bankAppGUI = bankAppGUI;
        this.user = user;
    }

    public void addCurrentBalance() {
        balanceLabel = new JLabel();
        balanceLabel = new JLabel("Current balance: $" + user.getCurrentBalance());
        balanceLabel.setBounds(0, 10, super.getWidth() - 20, 20);
        balanceLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(balanceLabel);
    }

    public void addTypeLabel(String type) {
        typeLabel = new JLabel(type);
        typeLabel.setBounds(45, 70, super.getWidth() - 20, 30);
        typeLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        add(typeLabel);
    }

    public void addDollarSignLabel() {
        dollarSignLabel = new JLabel("$");
        dollarSignLabel.setBounds(200, 70, 12, 30);
        dollarSignLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        add(dollarSignLabel);
    }

    public void addAmountLabel(String amount) {
        amountLabel = new JLabel(amount);
        amountLabel.setBounds(212, 70, super.getWidth() - 20, 30);
        amountLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        add(amountLabel);
    }

    public void addPayButton() {
        payButton = new JButton("Pay");
        payButton.setBounds(15, 150, super.getWidth() - 50, 40);
        payButton.setFont(new Font("Dialog", Font.BOLD, 20));
        payButton.addActionListener(this);
        add(payButton);
    }

    private void handlePayment(int amount) {
        Transaction transaction;
        user.setCurrentBalance(user.getCurrentBalance().subtract(new BigDecimal(amount)));
        transaction = new Transaction(user.getId(), "Service charge", new BigDecimal(-amount), null);

        if (MySQLInteractor.addTransactionToDatabase(transaction) && MySQLInteractor.updateCurrentBalance(user)) {
            JOptionPane.showMessageDialog(this, "Payment successful!");
            resetFieldsAndUpdateCurrentBalance();
        } else {
            JOptionPane.showMessageDialog(this, "Payment failed!");
        }
    }

    private void resetFieldsAndUpdateCurrentBalance() {
        balanceLabel.setText("Current balance: $" + user.getCurrentBalance());
        bankAppGUI.getCurrentBalanceTextField().setText("$" + user.getCurrentBalance());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String pressedButtonCommand = e.getActionCommand();

        int amount = Integer.parseInt(amountLabel.getText());
        if (pressedButtonCommand.equalsIgnoreCase("Pay")) {
            int result = user.getCurrentBalance().compareTo(BigDecimal.valueOf(amount));
            if (result < 0) {
                JOptionPane.showMessageDialog(this, "Payment failed! Insufficient funds!");
                return;
            }

            int option = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to proceed with the payment?",
                    "Payment",
                    JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                handlePayment(amount);
            }
        }
    }
}
