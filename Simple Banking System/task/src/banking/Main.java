package banking;


public class Main {

    public static String argument;

    public static void main(String[] args) {
        //argument = args[1];
        CreateSqlDataBase.createNewTable();
        Methods user1 = new Methods();
        user1.choosingMenuItem();

    }
}