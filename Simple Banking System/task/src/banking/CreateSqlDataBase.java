package banking;

import org.sqlite.SQLiteDataSource;

import javax.sql.ConnectionEvent;
import javax.sql.DataSource;
import java.security.DrbgParameters;
import java.sql.*;


public class CreateSqlDataBase {
    static String url = "jdbc:sqlite:C:/sqlite/banking.db";
    private static int accountNumber = 1;
    private static Connection connection;


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
        String insertNumberQuery = "INSERT INTO accounts (id, number) VALUES (" + accountNumber + ", '" + number + "');";
        String insertPinQuery = "UPDATE accounts SET pin = '" + pin + "' WHERE id = " + accountNumber + ";";
        try (Connection connection = getNewConnection()) {
            int j = executeUpdate(insertNumberQuery);
            int i = executeUpdate(insertPinQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void createNewTable() {
        String statement = "CREATE TABLE IF NOT EXISTS accounts (" +
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
        String query = "SELECT * FROM accounts WHERE number = " + number + ";";
        boolean isValid = false;
        try (Connection connection = getNewConnection()) {
            //int j = executeUpdate(query);
            try (ResultSet resultSet = executeQuery(query)) {
                //String pinDB = resultSet.getString("pin");
                //getNewConnection();
                //while(resultSet.next()){
                //int i=resultSet.findColumn("pin");
                String pinDB=resultSet.getString("pin");;
                //String pinDB = resultSet.getString("pin");
                if (pinDB.equals(pin)) {
                    isValid = true;
                }
                //connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isValid;
    }
}

