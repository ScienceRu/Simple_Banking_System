package banking;

import java.util.*;


public class Methods {
    private long fullCardNumber = 400000000000000L;
    private StringBuilder sbCardNumber = new StringBuilder("");
    private StringBuilder sbPinCode = new StringBuilder("");
    private int balance = 0;
    private ArrayList<StringBuilder> userData = new ArrayList<>();
    private int menuItem;
    private static boolean isCorrect = false;

    public static boolean isIsCorrect() {
        return isCorrect;
    }

    public static void setIsCorrect(boolean isCorrect) {
        Methods.isCorrect = isCorrect;
    }


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

    public static boolean checkLuhnAlgorithm(String transferCardNumber) {
        transferCardNumber = CreateSqlDataBase.getTempUserCardNumber();
        boolean isValid = false;

        ArrayList<Double> arrayList = new ArrayList<>();

        for (int i = 0, j = 1; j <= transferCardNumber.length(); i++, j++) {
            arrayList.add(Double.parseDouble(transferCardNumber.substring(i, j)));
        }
        double numberIndex16 = arrayList.get(16);
        arrayList.remove(16);


        for (int i = 0; i < arrayList.size(); i++) {
            if (i % 2 == 0) {
                arrayList.set(i, arrayList.get(i) * 2);
                if (arrayList.get(i) > 9) {
                    arrayList.set(i, arrayList.get(i) - 9);
                }
            }
        }
        arrayList.add(16, numberIndex16);

        double sum = 0;
        for (double i : arrayList) {
            sum += i;
        }

        if (sum % 10 != 0) {
            isValid = true;
        }
        return isValid;

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
            default:
                System.out.println("Try again");
                break;

        }
    }

    Map inputOfUser() {
        Map<String, String> hashMap = new HashMap<>();
        System.out.println("Enter your card number:");
        String inputOfUser1 = sc.next();
        System.out.println("Enter your PIN:");
        String inputOfUser2 = sc.next();

        hashMap.put(inputOfUser1, inputOfUser2);
        return hashMap;
    }

    void creatingAccount() {
//dropping values to default for iteration
        sbCardNumber = new StringBuilder("");
        fullCardNumber = 400000000000000L;
        sbPinCode = new StringBuilder("");
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
        System.out.println(getPinCode() + "\r\n");
        choosingMenuItem();

    }

    void loggingIntoAccount() {
        if (CreateSqlDataBase.isValidCardNumberAndPinCode(inputOfUser())) {
            System.out.println("You have successfully logged in!");
            actingWithAccount();
        } else {
            System.out.println("Wrong card number or PIN!");
            choosingMenuItem();
        }
    }

    private void actingWithAccount() {
        System.out.println("1. Balance\r\n2. Add income\r\n3. Do transfer\r\n4. Close account\r\n" +
                "5. Log out\r\n0. Exit");
        int actionOfUser = sc.nextInt();
        switch (actionOfUser) {
            case 1://1. Balance
                System.out.printf("Balance %d\r\n", getBalance());
                actingWithAccount();
                break;
            case 2://Add income
                CreateSqlDataBase.addIncome();
                actingWithAccount();
                break;
            case 3://3. Do transfer
                CreateSqlDataBase.doTransfer();
                actingWithAccount();
                break;
            case 4://4. Close account
                CreateSqlDataBase.closeAccount();
                System.out.println("The account has been closed!");
                setIsCorrect(false);
                choosingMenuItem();
                break;
            case 5://5. Log out
                System.out.println("You have successfully logged out!");
                setIsCorrect(false);

                CreateSqlDataBase.setTempUserCardNumber(null);
                choosingMenuItem();
                break;
            case 0://0. Exit
                System.out.println("Buy!");
                break;
        }

    }

}



