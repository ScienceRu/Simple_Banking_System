package banking;

import static banking.Methods.createNewTable;

public class Main {
    public static void main(String[] args) {
        createNewTable();
        Methods user1 = new Methods();
        user1.choosingMenuItem();

    }
}