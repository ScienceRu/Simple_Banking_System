package banking;

import org.sqlite.SQLiteDataSource;
import java.sql.*;


public class CreateSqlDataBase {
    static String url = "jdbc:sqlite:"+Main.argument;
    //static String url = "jdbc:sqlite:C:/sqlite/banking.db";
    private static int accountNumber = 1;
     static Connection connection;


    private static Connection getNewConnection() throws SQLException {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        return dataSource.getConnection();
    }

    private static int executeUpdate(String query) throws SQLException {
        Statement statement = getNewConnection().createStatement();
        int result = statement.executeUpdate(query);
        return result;
    }

    private static ResultSet executeQuery(String query) throws SQLException {
        Statement statement = getNewConnection().createStatement();
        ResultSet result = statement.executeQuery(query);
        return result;
    }


    public static void insertDataToSQL(StringBuilder number, StringBuilder pin) {
        String insertNumberQuery = "INSERT INTO card (id, number) VALUES (" + accountNumber + ", '" + number + "');";
        String insertPinQuery = "UPDATE card SET pin = '" + pin + "' WHERE id = " + accountNumber + ";";
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

    public static boolean isValidCardNumberAndPinCode(String number, String pin) {
        String query1 = "SELECT " +
                "CASE " +
                "WHEN EXISTS (SELECT * FROM card WHERE number = " + number + ") " +
                "THEN (SELECT pin FROM card WHERE number = " + number + ") " +
                "ELSE '-1' END;";

        boolean isValid = false;
        try (Connection connection = getNewConnection()) {
            try (ResultSet resultSet = executeQuery(query1)) {
                    String pinDB = resultSet.getString(1);
                    if (pinDB.equals(pin)) {
                        isValid = true;
                    }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isValid;
    }
}

