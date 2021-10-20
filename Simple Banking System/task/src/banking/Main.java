package banking;
//This programme provides opportunity for user to create bank account and act with it. Functions that available
// for users: create account (issue of a bank card with the assignment of a unique pin code), log into account, receive
//information about balance, add cash into balance, transfer money to other account (by card number), log out, close
//account (deleting all information about account from database).
//
// It uses Luhn algorithm for creating card number and SQL database for saving all information about actual
//accounts. All interaction with accounts realized through SQL database queries and executes.
//For run programme you should specify the path to the database in parameter of Main. If database is not exists, it
//will be created

public class Main {

    public static String argument;

    public static void main(String[] args) {
        argument = args[1];
        CreateSqlDataBase.createNewTable();
        Methods user1 = new Methods();
        user1.choosingMenuItem();

    }
}