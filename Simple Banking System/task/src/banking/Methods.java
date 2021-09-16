package banking;

import java.util.Scanner;
import java.util.Random;

public class Methods {
    private StringBuilder fullCardNumber = new StringBuilder("400000");
    private StringBuilder pinCode = new StringBuilder("");
    private int balance = 0;
    private StringBuilder[][] userData = new StringBuilder[2][3];
    private int menuItem;
    private static int countCard = 0;
    private static int countPin = 0;
    private static boolean isCorrect = false;

    Scanner sc = new Scanner(System.in);

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balanceIncrease) {
        balance += balanceIncrease;
    }

    public StringBuilder getCardNumber() {
        StringBuilder copyCardNumber = new StringBuilder("");
        copyCardNumber.append(fullCardNumber);
        return copyCardNumber;
    }

    public void setCardNumber() {
        Random random = new Random();
        int accountIdentifier = random.nextInt(99999999) + 1;
        fullCardNumber.append(accountIdentifier);

        while (fullCardNumber.length() < 16) {
            fullCardNumber.append("0", 0, 1);
        }
        userData[0][countCard] = fullCardNumber;
        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(getCardNumber());
        fullCardNumber = new StringBuilder("400000");
        countCard++;
    }

    public StringBuilder getPinCode() {
        StringBuilder copyPinCode = new StringBuilder("");
        copyPinCode.append(pinCode);
        return copyPinCode;
    }

    public void setPinCode() {
        Random random = new Random();
        int accountIdentifier = random.nextInt(9999) + 1;
        pinCode.append(accountIdentifier);
        while (pinCode.length() < 4) {
            pinCode.append("0", 0, 1);
        }
        userData[1][countPin] = pinCode;
        System.out.println("Your card PIN:");
        System.out.println(getPinCode());
        pinCode = new StringBuilder("");
        countPin++;
    }

    public int getMenuItem() {
        return menuItem;
    }

    public void setMenuItem() {
        this.menuItem = sc.nextInt();
    }

    void choosingMenuItem() {
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
        setMenuItem();
        switch (getMenuItem()) {
            case 1:
                creatingAccount();
                break;
            case 2:
                loggingIntoAccount();
                break;
            case 0:
                System.out.println("Buy!");
                break;
        }
    }

    void creatingAccount() {
        setCardNumber();
        setPinCode();
        //for cheking consistency of array
//        for (int i = 0; i < userData.length; i++) {
//            for (int j = 0; j < userData[i].length; j++) {
//                System.out.print(userData[i][j] + " ");
//            }
//        }
//      System.out.println(userData.toString());
        choosingMenuItem();
    }

    void loggingIntoAccount() {
        System.out.println("Enter your card number:");
        String inputOfUser1 = sc.next();
        System.out.println("Enter your PIN:");
        String inputOfUser2 = sc.next();

        if (isValidCardNumberAndPinCode(inputOfUser1, inputOfUser2)) {
            System.out.println("You have successfully logged in!");
            actingWithAccount();
        } else {
            System.out.println("Wrong card number or PIN!");
            choosingMenuItem();
        }
    }

//checking if card number and pin code are in array userData
    private boolean isValidCardNumberAndPinCode(String s1, String s2) {

        for (int i = 0; i < userData.length; ) {
            for (int j = 0; j < userData[i].length; j++) {
                if (s1.equals(userData[i][j].toString())) {
                    if (s2.equals(userData[i + 1][j].toString())) {
                        isCorrect = true;
                        break;
                    }
                    break;
                }
                break;
            }
            break;
        }
        return isCorrect;
    }

    private void actingWithAccount() {
        System.out.println("1. Balance\r\n2. Log out\r\n0. Exit");
        int actionOfUser = sc.nextInt();
        switch (actionOfUser) {
            case 1:
                System.out.printf("Balance %d\r\n", getBalance());
                actingWithAccount();
                break;
            case 2:
                System.out.println("You have successfully logged out!");
                isCorrect = false;
                choosingMenuItem();
                break;
            case 0:
                System.out.println("Buy!");
                break;
        }
    }

}

