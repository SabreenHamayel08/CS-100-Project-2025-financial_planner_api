package com.not_found.financial_planner_api.data;

import com.not_found.financial_planner_api.model.Account;
import com.not_found.financial_planner_api.model.MerchantCategory;
import com.not_found.financial_planner_api.model.Transaction;
import com.not_found.financial_planner_api.model.TransactionPattern;
import com.not_found.financial_planner_api.service.TransactionCategorizationService;

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


    // initializes accounts for a specific customer that is a basic user and a premium user
  
    public static List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public static List<Transaction> getTransactionsByAccountId(String id) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction t : transactions) {
            if (id != null && id.equals(t.getAccountId())) {
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