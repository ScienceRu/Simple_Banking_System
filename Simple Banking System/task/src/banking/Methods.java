package banking;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;


public class Methods {
    private long fullCardNumber = 400000000000000L;
    private StringBuilder sbCardNumber = new StringBuilder("");
    private StringBuilder sbPinCode = new StringBuilder("");
    private int balance = 0;
    private ArrayList<StringBuilder> userData = new ArrayList<>();
    private int menuItem;
    private static boolean isCorrect = false;

    Scanner sc = new Scanner(System.in);

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balanceIncrease) {
        balance += balanceIncrease;
    }

    public StringBuilder getCardNumber() {
        StringBuilder copySbCardNumber = new StringBuilder("");
        copySbCardNumber.append(sbCardNumber);
        return copySbCardNumber;
    }

    ArrayList<Integer> useLuhnAlgorithm() {
        Random random = new Random();
        long accountIdentifier = random.nextInt(9999999) + 1;
        fullCardNumber = fullCardNumber + accountIdentifier;
        String cardNumberInString = String.valueOf(fullCardNumber);
        ArrayList<Double> arrayList = new ArrayList<>();
        for (int i = 0, j = 1; j <= cardNumberInString.length(); i++, j++) {
            arrayList.add(Double.parseDouble(cardNumberInString.substring(i, j)));

        }

        ArrayList<Double> copyArrayList = new ArrayList<>(arrayList);

        for (int i = 0; i < copyArrayList.size(); i++) {
            if (i % 2 == 0) {
                copyArrayList.set(i, copyArrayList.get(i) * 2);
                if (copyArrayList.get(i) > 9) {
                    copyArrayList.set(i, copyArrayList.get(i) - 9);
                }
            }
        }
        double sum = 0;
        for (double i : copyArrayList) {
            sum += i;
        }

        if (sum % 10 != 0) {
            double difference = 10 - (sum % 10);

            arrayList.add(difference);
        } else {
            arrayList.add(0.0);
        }
        ArrayList<Integer> integerArrayList = new ArrayList<>();

        for (double d : arrayList) {
            integerArrayList.add((int) d);
        }

        return integerArrayList;
    }


    public StringBuilder getPinCode() {
        StringBuilder copySbPinCode = new StringBuilder("");
        copySbPinCode.append(sbPinCode);
        return copySbPinCode;
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
        //creating card number
        ArrayList<Integer> copyArrayListCardNumber = new ArrayList<>(useLuhnAlgorithm());
        for (int i : copyArrayListCardNumber) {
            sbCardNumber.append(i);
        }

        //creating pin code
        Random random = new Random();
        long accountIdentifier = random.nextInt(9999) + 1;
        sbPinCode.append(accountIdentifier);
        while (sbPinCode.length() < 4) {
            sbPinCode.append("0", 0, 1);
        }
        //adding card number and pin to sql base
        CreateSqlDataBase.insertDataToSQL(getCardNumber(), getPinCode());
        //showing card number and pin to user
        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(getCardNumber());
        System.out.println("Your card PIN:");
        System.out.println(getPinCode());
//dropping values to default for next iteration
        sbCardNumber = new StringBuilder("");
        fullCardNumber = 400000000000000L;
        sbPinCode = new StringBuilder("");
        choosingMenuItem();
    }

    void loggingIntoAccount() {
        System.out.println("Enter your card number:");
        String inputOfUser1 = sc.next();
        System.out.println("Enter your PIN:");
        String inputOfUser2 = sc.next();

        if (CreateSqlDataBase.isValidCardNumberAndPinCode(inputOfUser1, inputOfUser2)) {
            System.out.println("You have successfully logged in!");
            actingWithAccount();
        } else {
            System.out.println("Wrong card number or PIN!");
            choosingMenuItem();
        }
    }

    //checking if card number and pin code are in array userData
//    private boolean isValidCardNumberAndPinCode(String s1, String s2) {
//
//        for (int i = 0, j = 1; j < userData.size(); ) {
//            if (s1.equals(userData.get(i).toString())) {
//                if (s2.equals(userData.get(j).toString())) {
//                    isCorrect = true;
//                    break;
//                } else {
//                    break;
//                }
//            } else {
//                i += 2;
//                j += 2;
//
//            }
//        }
//        return isCorrect;
//    }

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

