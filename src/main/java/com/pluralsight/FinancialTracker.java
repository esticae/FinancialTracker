package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class FinancialTracker {

    private static final ArrayList<Transaction> transactions = new ArrayList<>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public static void main(String[] args) {
        loadTransactions(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true; //used as conditon to keep the boolean running true

        while (running) { //the condition keeps the code true till you hit false the X
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option: ");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();
            //reads a line of text input from the user and Stores it in the input variable
            switch (input.toUpperCase()) { //switch is like a if so if a case is added then it'll go to the method
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }


    }

    public static void loadTransactions(String fileName) {
        try {
            BufferedReader buff = new BufferedReader(new FileReader(fileName));
            String line;
            //Then, you're initializing a String variable line to hold each line of the file as you read it
            while ((line = buff.readLine()) != null) {
                //That line sets up a while loop that reads each line from the file until there are no more lines to read
                String[] trans = line.split("\\|");
                //this will take the string lines from tans.csv and split it at the | symbols
                if (trans.length == 5) {
                    //check if the line has 5 fields and is equal to 5
                    LocalDate date = LocalDate.parse(trans[0].trim());
                    LocalTime time = LocalTime.parse(trans[1].trim());
                    String vendor = trans[2].trim();
                    String type = trans[3].trim();
                    double amount = Double.parseDouble(trans[4]);

                    transactions.add(new Transaction(date, time, vendor, type, amount));
                    //This is adding this to the array transaction


                }


            }
            buff.close();

        } catch (FileNotFoundException e) {

            System.out.println("FileNotFoundException occurred! " + e.getMessage());

        } catch (Exception e) {
            System.out.println("Error loading file: " + e.getMessage());

        }
    }


    private static void addDeposit(Scanner scanner) {


        System.out.println("Please Enter the date (yyyy-MM-dd): ");
        String userDate = scanner.nextLine();
        LocalDate date = LocalDate.parse(userDate, DATE_FORMATTER);

        System.out.println("Please Enter the time (HH:mm:ss): ");
        String userTime = scanner.nextLine();
        LocalTime time = LocalTime.parse(userTime, TIME_FORMATTER);

        System.out.println("Enter the vendor");
        String vendor = scanner.nextLine();

        System.out.println("Please enter a type : ");
        String type = scanner.nextLine().trim();


        System.out.println("Enter your deposit amount: ");
        double amount = 0;

        try {
            amount = Double.parseDouble(scanner.nextLine());
            if (amount <= 0) {
                System.out.println("Deposit must be a positive number");
            }
        } catch (Exception e) {
            System.out.println("Invalid amount format. Please enter a valid number. " + e.getMessage());
        }
        Transaction deposit = new Transaction(date, time, vendor, type, amount);
        transactions.add(deposit);
        BufferedWriter buff;
        try {
            buff = new BufferedWriter(new FileWriter("transactions.csv", true));
            buff.write(
                    deposit.getDate().toString() + "|" +
                            deposit.getTime() + "|" +
                            deposit.getType() + "|" +
                            deposit.getVendor() + "|" +
                            deposit.getAmount()
            );
            // Adding a new line after writing the transaction
            buff.newLine();
            buff.close(); // Don't forget to close the writer after writing
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    private static void addPayment(Scanner scanner) {

        System.out.println("Please Enter the date (yyyy-MM-dd): ");
        String userDate = scanner.nextLine();
        LocalDate date = LocalDate.parse(userDate, DATE_FORMATTER);

        System.out.println("Please Enter the time (HH:mm:ss): ");
        String userTime = scanner.nextLine();
        LocalTime time = LocalTime.parse(userTime, TIME_FORMATTER);


        System.out.println("Enter the vendor");
        String vendor = scanner.nextLine();
        System.out.println("Please enter a type");
        String type = scanner.nextLine();

        System.out.println("Enter your payment amount");
        double amount = 0;

        try {
            amount = Double.parseDouble(scanner.nextLine());
            if (amount > 0) {
                System.out.println("Payment must be a negative number");
            }
        } catch (Exception e) {
            System.out.println("Error getting payment " + e.getMessage());
        }
        if (amount > 0) {
            amount = -amount;
        }
        Transaction payment = new Transaction(date, time, vendor, type, amount);
        transactions.add(payment);


        BufferedWriter buff;
        try {
            buff = new BufferedWriter(new FileWriter("transactions.csv", true));
            buff.write(
                    payment.getDate().toString() + "|" +
                            payment.getTime() + "|" +
                            payment.getType() + "|" +
                            payment.getVendor() + "|" +
                            payment.getAmount()
            );
            // Adding a new line after writing the transaction
            buff.newLine();
            buff.close(); // Don't forget to close the writer after writing
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }


    }


    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("=======================Ledger Menu===================================");
            System.out.println("Choose an option:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void displayLedger() {
        System.out.println("Ledger");
        System.out.println("=========================================================================================================");
        System.out.printf("%-15s %-10s %-20s %-20s %s\n", "Date", "Time", "Vendor", "Type", "Amount"); // align output to the left and specify the width for every item
        System.out.println("=========================================================================================================");
        for (Transaction transaction : transactions) {
            System.out.printf("%-15s %-10s %-20s %-20s $%.2f\n", transaction.getDate(), transaction.getTime(), transaction.getVendor(), transaction.getType(), transaction.getAmount());
        }
    }


private static void displayDeposits() {

    for (Transaction deposits : transactions) {
        System.out.println("Deposits");
        System.out.println("Deposit Details:");
        System.out.println("Date: " + deposits.getDate());
        System.out.println("Time: " + deposits.getTime());
        System.out.println("Vendor: " + deposits.getVendor());
        System.out.println("Type: " + deposits.getType());
        System.out.println("Amount: " + deposits.getAmount());
        System.out.println();
    }
}

private static void displayPayments() {

    for (Transaction Payments : transactions) {
        System.out.println("Transactions");
        System.out.println("Transaction Details:");
        System.out.println("Date: " + Payments.getDate());
        System.out.println("Time: " + Payments.getTime());
        System.out.println("Vendor: " + Payments.getVendor());
        System.out.println("Type: " + Payments.getType());
        System.out.println("Amount: " + Payments.getAmount());
        System.out.println();
    }

}

private static void reportsMenu(Scanner scanner) {
    boolean running = true;
    while (running) {
        System.out.println("Reports");
        System.out.println("Choose an option:");
        System.out.println("1) Month To Date");
        System.out.println("2) Previous Month");
        System.out.println("3) Year To Date");
        System.out.println("4) Previous Year");
        System.out.println("5) Search by Vendor");
        System.out.println("0) Back");


        String input = scanner.nextLine().trim();


        switch (input) {
            case "1":

                LocalDate today = LocalDate.now();
                LocalDate startOfMonth = today.withDayOfMonth(1);
                LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());
                filterTransactionsByDate(startOfMonth, endOfMonth);
                break;


            case "2":

                LocalDate today2 = LocalDate.now();
                LocalDate startOfPreviousMonth = today2.minusMonths(1).withDayOfMonth(1);
                LocalDate endOfPreviousMonth = today2.minusMonths(1).withDayOfMonth(today2.minusMonths(1).lengthOfMonth());

                System.out.println("Previous Month Start Date: " + startOfPreviousMonth);
                System.out.println("Previous Month End Date: " + endOfPreviousMonth);

                filterTransactionsByDate(startOfPreviousMonth, endOfPreviousMonth);
                break;


            case "3":
                // Generate a report for all transactions within the current year,
                // including the date, vendor, and amount for each transaction.
                LocalDate today3 = LocalDate.now();
                LocalDate startOfYear = today3.withDayOfYear(1);  // Start date of the current year
                LocalDate endOfYear = today3.withDayOfYear(1).plusDays(1);  // End date of the current year
                filterTransactionsByDate(startOfYear, endOfYear);
                break;

            case "4":
                // Generate a report for all transactions within the previous year,
                // including the date, vendor, and amount for each transaction.
                LocalDate today4 = LocalDate.now();
                LocalDate startOfPreviousYear = today4.minusYears(1).withDayOfYear(1);
                LocalDate endOfPreviousYear = today4.withDayOfYear(1); // Set to the first day of the current year
                filterTransactionsByDate(startOfPreviousYear, endOfPreviousYear);
                break;
            case "5":
                // Prompt the user to enter a vendor name, then generate a report for all transactions
                // with that vendor, including the date, vendor, and amount for each transaction.
                System.out.println("Enter vendor name: ");
                String vendorName = scanner.nextLine().trim();
                filterTransactionsByVendor(vendorName);
                break;

            case "0":
                running = false;
            default:
                System.out.println("Invalid option");
                break;
        }
    }
}


private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {


    System.out.println("Transactions within the date range " + startDate + " to " + endDate + ":");

    boolean foundTransactions = false;

    for (Transaction transaction : transactions) {
        LocalDate transactionDate = transaction.getDate();
        System.out.println("Transaction Date: " + transactionDate + ", Start Date: " + startDate + ", End Date: " + endDate);
        System.out.println();

        if (!transactionDate.isBefore(startDate) && !transactionDate.isAfter(endDate)) {
            // Print transaction details
            System.out.println("Transaction Details:");
            System.out.println(("Date: " + transaction.getDate()) + " " + "Vendor: " + transaction.getVendor() + " " + "Type: " + transaction.getType() + " " + "Amount: " + transaction.getAmount());
            //  System.out.println("Vendor: " + transaction.getVendor());
            //   System.out.println("Type: " + transaction.getType());
            //    System.out.println("Amount: " + transaction.getAmount());
            System.out.println();

            foundTransactions = true;
        }

        if (!foundTransactions) {
            System.out.println("No transactions found within the date range.");
        }
    }


}


private static void filterTransactionsByVendor(String vendor) {

    System.out.println("Transactions for vendor: " + vendor);

    boolean foundTransactions = false;

    for (Transaction transaction : transactions) {
        if (transaction.getVendor().equalsIgnoreCase(vendor)) {
            System.out.println("Transaction Details:");
            System.out.println("Date: " + transaction.getDate());
            System.out.println("Time: " + transaction.getTime());
            System.out.println("Vendor: " + transaction.getVendor());
            System.out.println("Type: " + transaction.getType());
            System.out.println("Amount: " + transaction.getAmount());
            System.out.println();
            foundTransactions = true;
        }
    }

    if (!foundTransactions) {
        System.out.println("No transactions found for vendor: " + vendor);
    }
}

}