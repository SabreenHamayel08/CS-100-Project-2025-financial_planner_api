package com.not_found.financial_planner_api.data;

import com.not_found.financial_planner_api.model.Account;
import com.not_found.financial_planner_api.model.Transaction;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SampleData {
    private static final List<Transaction> transactions = new ArrayList<>();
    private static final List<Account> accounts = new ArrayList<>();

    static {
        initializeTransactions();
        initializeAccounts();
    }

    private static void initializeTransactions() {
        transactions.add(new Transaction(1, 1, LocalDate.now().minusDays(1), "Whole Foods", -72.50));
        transactions.add(new Transaction(2, 1, LocalDate.now().minusDays(2), "REMOTE ONLINE DEPOSIT #1", 1500.00));
        transactions.add(new Transaction(3, 3, LocalDate.now().minusDays(3), "Gas", -3.23));
        transactions.add(new Transaction(4, 3, LocalDate.now().minusDays(7), "Coffee", -3.75));
        transactions.add(new Transaction(5, 1, LocalDate.now().minusDays(10), "Trader Joes", -65.10));
        transactions.add(new Transaction(6, 1, LocalDate.now().minusDays(12), "Jewel Osco", -83.59));
        transactions.add(new Transaction(7, 1, LocalDate.now().minusDays(20), "REMOTE ONLINE DEPOSIT #1", 2000.00));
        transactions.add(new Transaction(8, 3, LocalDate.now().minusDays(27), "Netflix", -7.99));
        transactions.add(new Transaction(9, 3, LocalDate.now().minusDays(30), "Coffee", -3.75));
        transactions.add(new Transaction(10, 2, LocalDate.now().minusDays(41), "REMOTE ONLINE DEPOSIT #1", 1500.00));
        transactions.add(new Transaction(11, 1, LocalDate.now().minusDays(46), "Whole Foods", -93.75));
        transactions.add(new Transaction(12, 3, LocalDate.now().minusDays(51), "Coffee", -3.75));
        transactions.add(new Transaction(13, 3, LocalDate.now().minusDays(57), "Gas", -3.23));
        transactions.add(new Transaction(14, 3, LocalDate.now().minusDays(62), "Netflix", -7.99));
        transactions.add(new Transaction(15, 1, LocalDate.now().minusDays(68), "REMOTE ONLINE DEPOSIT #1", 2000.00));
        transactions.add(new Transaction(16, 3, LocalDate.now().minusDays(74), "Coffee", -3.75));
        transactions.add(new Transaction(17, 3, LocalDate.now().minusDays(80), "Jewel Osco", -7.99));
        transactions.add(new Transaction(18, 2, LocalDate.now().minusDays(86), "CASH DEPOSIT ATM", 3500.00));
    }

    private static void initializeAccounts() {
        accounts.add(new Account(1, "Checking", transactions.stream()
                .filter(t -> t.getAccountId() == 1)
                .mapToDouble(Transaction::getAmount)
                .sum()));
        accounts.add(new Account(2, "Savings", transactions.stream()
                .filter(t -> t.getAccountId() == 2)
                .mapToDouble(Transaction::getAmount)
                .sum()));
        accounts.add(new Account(3, "Credit Card", transactions.stream()
                .filter(t -> t.getAccountId() == 3)
                .mapToDouble(Transaction::getAmount)
                .sum() + 5000)); // Assuming a credit limit of 5000
    }

    public static List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public static List<Account> getAccounts() {
        return new ArrayList<>(accounts);
    }
}