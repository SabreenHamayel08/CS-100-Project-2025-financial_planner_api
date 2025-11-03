package com.not_found.financial_planner_api.data;

import com.not_found.financial_planner_api.model.Account;
import com.not_found.financial_planner_api.model.MerchantCategory;
import com.not_found.financial_planner_api.model.Transaction;
import com.not_found.financial_planner_api.model.TransactionPattern;
import com.not_found.financial_planner_api.service.TransactionCategorizationService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SampleData {
    private static final List<Transaction> transactions = new ArrayList<>();
    private static final List<Account> accounts = new ArrayList<>();
    private static final List<MerchantCategory> merchantCategories = new ArrayList<>();

    // Category Constants (from TransactionCategorizationService)
    private static final String CATEGORY_GROCERIES = TransactionCategorizationService.CATEGORY_GROCERIES;
    private static final String CATEGORY_TRANSPORTATION = TransactionCategorizationService.CATEGORY_TRANSPORTATION;
    private static final String CATEGORY_ENTERTAINMENT = TransactionCategorizationService.CATEGORY_ENTERTAINMENT;
    @SuppressWarnings("unused")
    private static final String CATEGORY_OTHER = TransactionCategorizationService.CATEGORY_OTHER;

    static {
        initializeCategories();
        initializeTransactions();
        initializeAccounts();
    }

    private static void initializeCategories() {
        // Grocery stores
        merchantCategories.add(new MerchantCategory("Whole Foods", CATEGORY_GROCERIES,
                List.of(new TransactionPattern(0, 200, CATEGORY_GROCERIES, 1))));
        merchantCategories.add(new MerchantCategory("Trader", CATEGORY_GROCERIES,
                List.of(new TransactionPattern(0, 200, CATEGORY_GROCERIES, 1))));
        merchantCategories.add(new MerchantCategory("Jewel Osco", CATEGORY_GROCERIES,
                List.of(new TransactionPattern(0, 200, CATEGORY_GROCERIES, 1))));
        merchantCategories.add(new MerchantCategory("Coffee", CATEGORY_GROCERIES,
                List.of(new TransactionPattern(2, 10, CATEGORY_GROCERIES, 4))));

        // Transportation
        merchantCategories.add(new MerchantCategory("Gas", CATEGORY_TRANSPORTATION,
                List.of(new TransactionPattern(0, 100, CATEGORY_TRANSPORTATION, 1))));

        // Entertainment
        merchantCategories.add(new MerchantCategory("Netflix", CATEGORY_ENTERTAINMENT,
                List.of(new TransactionPattern(5, 20, CATEGORY_ENTERTAINMENT, 1))));
    }

    //intializes transactions for specific for each account (ac1, ac2, ac3, etc.)
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
    // initializes accounts for a specific customer that is a basic user and a premium user
    private static void initializeAccounts() {
        // Basic User Accounts  
        accounts.add(new Account(1, "Checking", transactions.stream()
                .filter(t -> t.getAccountId() == 1)
                .mapToDouble(Transaction::getAmount)
                .sum(), "John Doe", false, "10-03-2024", 22, "M", "842-935-9807", "john.doe@gmail.com", "07-08-2003"));
        accounts.add(new Account(2, "Savings", transactions.stream()
                .filter(t -> t.getAccountId() == 2)
                .mapToDouble(Transaction::getAmount)
                .sum(), "John Doe", false, "10-03-2024", 22, "M", "842-935-9807", "john.doe@gmail.com", "07-08-2003"));
        accounts.add(new Account(3, "Credit Card", transactions.stream()
                .filter(t -> t.getAccountId() == 3)
                .mapToDouble(Transaction::getAmount)
                .sum() + 5000, "John Doe", false, "10-03-2024", 22, "M", "842-935-9807", "john.doe@gmail.com", "07-08-2003")); // Assuming a credit limit of 5000
        // Premium User Accounts
        accounts.add(new Account(1, "Checking", transactions.stream()
                .filter(t -> t.getAccountId() == 4)
                .mapToDouble(Transaction::getAmount)
                .sum(), "Alice Smith", true, "09-17-2023", 31, "F", "777-432-2981", "asmith94@yahoo.com", "02-14-1994"));
        accounts.add(new Account(2, "Savings", transactions.stream()
                .filter(t -> t.getAccountId() == 5)
                .mapToDouble(Transaction::getAmount)
                .sum(), "Alice Smith", true, "09-17-2023", 31, "F", "777-432-2981", "asmith94@yahoo.com", "02-14-1994"));
        accounts.add(new Account(3, "Credit Card", transactions.stream()
                .filter(t -> t.getAccountId() == 6)
                .mapToDouble(Transaction::getAmount)
                .sum() + 10000, "Alice Smith", true, "09-17-2023", 31, "F", "777-432-2981", "asmith94@yahoo.com", "02-14-1994")); // Assuming a credit limit of 10000          
    }

    public static List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public static List<Account> getAccounts() {
        return new ArrayList<>(accounts);
    }

    public static List<MerchantCategory> getMerchantCategories() {
        return new ArrayList<>(merchantCategories);
    }


}