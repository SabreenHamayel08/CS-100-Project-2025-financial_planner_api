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
    transactions.add(new Transaction(1, "CheckingB", LocalDate.of(2024, 10, 1), "Whole Foods Market", -150.75));
    transactions.add(new Transaction(2, "CheckingB", LocalDate.of(2024, 10, 3), "Gas Station", -40.00));
    transactions.add(new Transaction(3, "SavingsB", LocalDate.of(2024, 10, 5), "Trader Joe's", -85.20));
    transactions.add(new Transaction(4, "CreditB", LocalDate.of(2024, 10, 7), "Netflix Subscription", -15.99));
    transactions.add(new Transaction(5, "CheckingP", LocalDate.of(2024, 9, 20), "Jewel Osco", -60.50));
    transactions.add(new Transaction(6, "CheckingP", LocalDate.of(2024, 9, 22), "Gas Station", -35.00));
    transactions.add(new Transaction(7, "SavingsP", LocalDate.of(2024, 9, 25), "Whole Foods Market", -120.00));
    transactions.add(new Transaction(8, "CreditP", LocalDate.of(2024, 9, 28), "Netflix Subscription", -15.99));
    transactions.add(new Transaction(9, "SavingsB", LocalDate.of(2024, 10, 10), "Coffee Shop", -8.50));
    transactions.add(new Transaction(10, "SavingsB", LocalDate.of(2024, 9, 30), "Gas Station", -50.00));
    transactions.add(new Transaction(11, "CheckingB", LocalDate.of(2024, 10, 12), "Trader Joe's", -95.00));
    transactions.add(new Transaction(12, "CreditP", LocalDate.of(2024, 10, 15), "Coffee Shop", -12.00));
    transactions.add(new Transaction(13, "CreditP", LocalDate.of(2024, 10, 18), "Whole Foods Market", -130.25));
    transactions.add(new Transaction(14, "CheckingP", LocalDate.of(2024, 10, 20), "Netflix Subscription", -15.99));
    transactions.add(new Transaction(15, "SavingsB", LocalDate.of(2024, 10, 22), "Gas Station", -45.00));
    transactions.add(new Transaction(16, "SavingsP", LocalDate.of(2024, 10, 25), "Jewel Osco", -70.75));
}

    // initializes accounts for a specific customer that is a basic user and a premium user
    public static void initializeAccounts() {
        // Basic User Accounts  
        accounts.add(new Account(1, "CheckingB", transactions.stream()
                .filter(t -> t.getAccountId().equals("CheckingB"))
                .mapToDouble(Transaction::getAmount)
                .sum(), "John Doe", false, "10-03-2024", 22, "M", "842-935-9807", "john.doe@gmail.com", "07-08-2003"));
        accounts.add(new Account(2, "SavingsB", transactions.stream()
                .filter(t -> t.getAccountId().equals("SavingsB"))
                .mapToDouble(Transaction::getAmount)
                .sum(), "John Doe", false, "10-03-2024", 22, "M", "842-935-9807", "john.doe@gmail.com", "07-08-2003"));
        accounts.add(new Account(3, "CreditB", transactions.stream()
                .filter(t -> t.getAccountId().equals("CreditB"))
                .mapToDouble(Transaction::getAmount)
                .sum() + 5000, "John Doe", false, "10-03-2024", 22, "M", "842-935-9807", "john.doe@gmail.com", "07-08-2003")); // Assuming a credit limit of 5000
        // Premium User Accounts
        accounts.add(new Account(1, "CheckingP", transactions.stream()
                .filter(t -> t.getAccountId().equals("CheckingP"))
                .mapToDouble(Transaction::getAmount)
                .sum(), "Alice Smith", true, "09-17-2023", 31, "F", "777-432-2981", "asmith94@yahoo.com", "02-14-1994"));
        accounts.add(new Account(2, "SavingsP", transactions.stream()
                .filter(t -> t.getAccountId().equals("SavingsP"))
                .mapToDouble(Transaction::getAmount)
                .sum(), "Alice Smith", true, "09-17-2023", 31, "F", "777-432-2981", "asmith94@yahoo.com", "02-14-1994"));
        accounts.add(new Account(3, "CreditP", transactions.stream()
                .filter(t -> t.getAccountId().equals("CreditP"))
                .mapToDouble(Transaction::getAmount)
                .sum() + 10000, "Alice Smith", true, "09-17-2023", 31, "F", "777-432-2981", "asmith94@yahoo.com", "02-14-1994")); // Assuming a credit limit of 10000          
    }

    public static List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public static List<Transaction> getTransactionsByAccountId(String id) {
        List<Transaction> result = new ArrayList<>();
        initializeAccounts();
        for (Transaction t : transactions) {
            if (t.getAccountId() == id) {
                result.add(t);
            }
        }
        return result;
    }

    public static List<Account> getAccounts() {
        return new ArrayList<>(accounts);
    }

    public static List<MerchantCategory> getMerchantCategories() {
        return new ArrayList<>(merchantCategories);
    }


}