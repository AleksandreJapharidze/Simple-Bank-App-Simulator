package guis;

import database.MySQLInteractor;
import database.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BillsGUI extends Base implements ActionListener {
    private BankAppGUI bankAppGUI;

    public BillsGUI(BankAppGUI bankAppGUI, User user) {
        super("Bills", user);
        this.bankAppGUI = bankAppGUI;
    }

    @Override
    protected void addGUIComponents() {
        JLabel titleLabel = new JLabel("Bills you might have to pay");
        titleLabel.setBounds(0, 20, super.getWidth(), 40);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel);

        JButton powerBillButton = new JButton("Electricity");
        powerBillButton.setBounds(15, 100, super.getWidth() - 50, 42);
        powerBillButton.setFont(new Font("Dialog", Font.BOLD, 22));
        powerBillButton.addActionListener(this);
        add(powerBillButton);

        JButton internetBillButton = new JButton("Internet");
        internetBillButton.setBounds(15, 170, super.getWidth() - 50, 42);
        internetBillButton.setFont(new Font("Dialog", Font.BOLD, 22));
        internetBillButton.addActionListener(this);
        add(internetBillButton);

        JButton gasBillButton = new JButton("Gas");
        gasBillButton.setBounds(15, 240, super.getWidth() - 50, 42);
        gasBillButton.setFont(new Font("Dialog", Font.BOLD, 22));
        gasBillButton.addActionListener(this);
        add(gasBillButton);

        JButton waterBillButton = new JButton("Water");
        waterBillButton.setBounds(15, 310, super.getWidth() - 50, 42);
        waterBillButton.setFont(new Font("Dialog", Font.BOLD, 22));
        waterBillButton.addActionListener(this);
        add(waterBillButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(15, 500, super.getWidth() - 50, 42);
        exitButton.setFont(new Font("Dialog", Font.BOLD, 22));
        exitButton.addActionListener(this);
        add(exitButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String pressedButtonCommand = e.getActionCommand();
        if (pressedButtonCommand.equalsIgnoreCase("Exit")) {
            new BankAppGUI(user).setVisible(true);
            this.dispose();
            return;
        }

        BillsDialog billsDialog = new BillsDialog(this, bankAppGUI, user);
        billsDialog.setTitle(pressedButtonCommand);
        billsDialog.addCurrentBalance();
        billsDialog.addTypeLabel(pressedButtonCommand + " bill:");

        String billAmount = "";
        switch (pressedButtonCommand) {
            case "Electricity":
                billAmount = String.valueOf(MySQLInteractor.getBills(user).getElectricity_bill());
                break;
            case "Internet":
                billAmount = String.valueOf(MySQLInteractor.getBills(user).getInternet_bill());
                break;
            case "Gas":
                billAmount = String.valueOf(MySQLInteractor.getBills(user).getGas_bill());
                break;
            case "Water":
                billAmount = String.valueOf(MySQLInteractor.getBills(user).getWater_bill());
                break;
        }
        billsDialog.addDollarSignLabel();
        billsDialog.addAmountLabel(billAmount);
        billsDialog.addPayButton();

        billsDialog.setVisible(true);
    }
}
