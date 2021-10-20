package banking;

import org.sqlite.SQLiteConnection;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class CreateSqlDataBase {
    static String url = "jdbc:sqlite:" + Main.argument;

    //static String url = "jdbc:sqlite:C:/Users/torzh/IdeaProjects/Simple Banking System/Simple Banking System/task/banking.db";
    private static int accountNumber = 1;
    static SQLiteConnection connection;
    private static Map<String, String> userInput = new HashMap<>();

    private static String tempUserCardNumber;

    public static String getTempUserCardNumber() {
        return tempUserCardNumber;
    }

    public static void setTempUserCardNumber(String tempUserCardNumber) {
        CreateSqlDataBase.tempUserCardNumber = tempUserCardNumber;
    }


    private static Connection getNewConnection() throws SQLException {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        return dataSource.getConnection();
    }

    private static int executeUpdate(String query) throws SQLException {
        Statement statement = getNewConnection().createStatement();
        return statement.executeUpdate(query);
    }

    private static ResultSet executeQuery(String query) throws SQLException {
        Statement statement = getNewConnection().createStatement();
        return statement.executeQuery(query);
    }

    public static void insertDataToSQL(StringBuilder number, StringBuilder pin) {
        String insertNumberQuery = "INSERT INTO card (id, number) VALUES (" + accountNumber + ", '" + number + "');";
        String insertPinQuery = "UPDATE card SET pin = '" + pin + "' WHERE id = " + accountNumber + ";";
        userInput.put(number.toString(), pin.toString());
        try (Connection connection = getNewConnection()) {
            int j = executeUpdate(insertNumberQuery);
            int i = executeUpdate(insertPinQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        accountNumber++;

    }

    public static void createNewTable() {
        String statement = "CREATE TABLE IF NOT EXISTS card (" +
                "id INTEGER," +
                "number TEXT," +
                "pin TEXT," +
                "balance INTEGER DEFAULT 0)";
        try (Connection connection = getNewConnection()) {
            int i = executeUpdate(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidCardNumberAndPinCode(Map hashMap) {
        Map<String, String> hashMap1 = new HashMap<>(hashMap);
        boolean isValid = false;

        for (Map.Entry<String, String> entry : hashMap1.entrySet()) {
            String query1 = "SELECT " +
                    "CASE " +
                    "WHEN EXISTS (SELECT * FROM card WHERE number = '" + entry.getKey() + "') " +
                    "THEN (SELECT pin FROM card WHERE number = '" + entry.getKey() + "') " +
                    "ELSE '-1' END;";

            try (Connection connection = getNewConnection()) {
                try (ResultSet resultSet = executeQuery(query1)) {

                    String pinDB = resultSet.getString(1);
                    if (pinDB.equals(entry.getValue())) {
                        isValid = true;
                        setTempUserCardNumber(entry.getKey());
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isValid;

    }

    static void addIncome() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter income:");
        //System.out.println(getTempUserCardNumber());
        int sumOfIncome = scanner.nextInt();

        String query1 = "UPDATE card SET balance = balance + " + sumOfIncome + " WHERE number = '" + getTempUserCardNumber() + "';";
        try (Connection connection = CreateSqlDataBase.getNewConnection()) {
            int j = CreateSqlDataBase.executeUpdate(query1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Income was added!");
    }

    static void doTransfer() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Transfer");
        System.out.println("Enter card number: ");
        String transferCardNumber = scanner.next();


        if (Methods.checkLuhnAlgorithm(transferCardNumber)) {
            if (!transferCardNumber.equals(getTempUserCardNumber())) {
                if (isExistAccount(transferCardNumber)) {
                    System.out.println("Enter how much money you want to transfer: ");
                    int sumOfTransfer = scanner.nextInt();
                    if (isEnoughMoney(sumOfTransfer, getTempUserCardNumber())) {
                        transferMoney(sumOfTransfer, getTempUserCardNumber(), transferCardNumber);
                        System.out.println("Success!");
                    } else {
                        System.out.println("Not enough money!");
                    }

                } else {
                    System.out.println("Such a card does not exist.");
                }
            } else {
                System.out.println("You can't transfer money to the same account!");

            }


        } else {
            System.out.println("Probably you made mistake in the card number. Please try again!");

        }

    }

    static boolean isExistAccount(String transferCardNumber) {
        boolean isExist = false;
        String query = "SELECT " +
                "CASE " +
                "WHEN EXISTS (SELECT * FROM card WHERE number = '" + transferCardNumber + "') " +
                "THEN (SELECT 'TRUE') " +
                "ELSE 'FALSE' END;";
        try (Connection connection = getNewConnection()) {
            try (ResultSet resultSet = executeQuery(query)) {
                if (resultSet.getString(1).equals("TRUE")) {
                    isExist = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isExist;
    }

    static boolean isEnoughMoney(int sumOfTransfer, String tempUserCardNumber) {
        boolean isEnough = false;

        String query =
                "SELECT balance " +
                        "FROM card " +
                        "WHERE number = '" + tempUserCardNumber + "';";
        try (Connection connection = getNewConnection()) {
            try (ResultSet resultSet = executeQuery(query)) {
                if (resultSet.getInt(1) >= sumOfTransfer) {
                    isEnough = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isEnough;
    }

    static void transferMoney(int sumOfTransfer, String tempUserCardNumber, String transferCardNumber) {
        String reduceBalanceOfCurrentUser = "UPDATE card SET balance = balance - " + sumOfTransfer +
                " WHERE number = '" + tempUserCardNumber + "';";
        String increaseBalanceOfTransferCard = "UPDATE card SET balance = balance + " + sumOfTransfer +
                " WHERE number = '" + transferCardNumber + "';";

        try (Connection connection = getNewConnection()) {
            connection.setAutoCommit(false);
            int i = executeUpdate(reduceBalanceOfCurrentUser);
            int j = executeUpdate(increaseBalanceOfTransferCard);

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void closeAccount() {
        String deleteAccountCommand = "DELETE FROM card WHERE number = ?;";
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteAccountCommand)) {

            preparedStatement.setString(1, getTempUserCardNumber());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public static int getBalance() {
        String informationAboutBalance = "SELECT balance FROM card WHERE number = ?";
        int balance = 0;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(informationAboutBalance)) {
                preparedStatement.setString(1, getTempUserCardNumber());
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                balance = resultSet.getInt(1);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException a) {
            a.printStackTrace();
        }
        return balance;
    }

}

