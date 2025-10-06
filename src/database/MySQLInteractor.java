package database;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class MySQLInteractor {
    private static final String DATABASE_URL = "jdbc:mysql://127.0.0.1:3306/bank_app";
    private static final String DATABASE_USERNAME = "root";
    private static final String DATABASE_PASSWORD = "MoolyFTW12.";

    public static User validateLogin(String username, String password) {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ? AND password = ?"
            );

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int userID = resultSet.getInt("id");
                BigDecimal currentBalance = resultSet.getBigDecimal("current_balance");

                return new User(userID, username, password, currentBalance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean registerUser(String username, String password) {
        try {
            if (!usernameExists(username)) {
                Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO users (username, password, current_balance)" +
                                "VALUES (?, ?, ?)"
                );

                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setBigDecimal(3, BigDecimal.ZERO);

                preparedStatement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteUserAccount(User user) {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

            PreparedStatement deleteTransactionData = connection.prepareStatement(
                    "DELETE FROM transactions WHERE user_id = ?"
            );

            PreparedStatement deleteAccountItself = connection.prepareStatement(
                    "DELETE FROM users WHERE id = ?"
            );

            deleteTransactionData.setInt(1, user.getId());
            deleteTransactionData.executeUpdate();

            deleteAccountItself.setInt(1, user.getId());
            deleteAccountItself.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean usernameExists(String username) {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ?"
            );

            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean addTransactionToDatabase(Transaction transaction) {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT transactions(user_id, transaction_type, transaction_amount, transaction_date)" +
                            "VALUES (?, ?, ?, NOW())"
            );

            preparedStatement.setInt(1, transaction.getUserID());
            preparedStatement.setString(2, transaction.getTransactionType());
            preparedStatement.setBigDecimal(3, transaction.getTransactionAmount());

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateCurrentBalance(User user) {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE users SET current_balance = ? WHERE id = ?"
            );

            preparedStatement.setBigDecimal(1, user.getCurrentBalance());
            preparedStatement.setInt(2, user.getId());

            preparedStatement.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean transfer(User user, String transferUsername, float transferAmount) {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ?"
            );

            preparedStatement.setString(1, transferUsername);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User transferUser = new User(
                        resultSet.getInt("id"),
                        transferUsername,
                        resultSet.getString("password"),
                        resultSet.getBigDecimal("current_balance")
                );

                Transaction transferTransaction = new Transaction(
                        user.getId(),
                        "Transfer",
                        new BigDecimal(-transferAmount),
                        null
                );

                Transaction ReceiveTransaction = new Transaction(
                        transferUser.getId(),
                        "Transfer",
                        new BigDecimal(transferAmount),
                        null
                );

                transferUser.setCurrentBalance(transferUser.getCurrentBalance().add(BigDecimal.valueOf(transferAmount)));
                updateCurrentBalance(transferUser);

                user.setCurrentBalance(user.getCurrentBalance().subtract(BigDecimal.valueOf(transferAmount)));
                updateCurrentBalance(user);

                addTransactionToDatabase(transferTransaction);
                addTransactionToDatabase(ReceiveTransaction);

                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Bills getBills(User user) {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM bills WHERE users_id = ?"
            );

            preparedStatement.setInt(1, user.getId());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int electricity_bill = resultSet.getInt("electricity_bill");
                int internet_bill = resultSet.getInt("internet_bill");
                int gas_bill = resultSet.getInt("gas_bill");
                int water_bill = resultSet.getInt("water_bill");

                return new Bills(electricity_bill, internet_bill, gas_bill, water_bill);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Transaction> getPastTransactions(User user) {
        ArrayList<Transaction> pastTransactions = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM transactions WHERE user_id = ?"
            );

            preparedStatement.setInt(1, user.getId());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Transaction transaction = new Transaction(
                        user.getId(),
                        resultSet.getString("transaction_type"),
                        resultSet.getBigDecimal("transaction_amount"),
                        resultSet.getDate("transaction_date")
                );

                pastTransactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pastTransactions;
    }
}
