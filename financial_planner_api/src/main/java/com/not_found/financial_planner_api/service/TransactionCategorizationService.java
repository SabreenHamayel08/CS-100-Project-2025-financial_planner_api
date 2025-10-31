package com.not_found.financial_planner_api.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.not_found.financial_planner_api.data.SampleData;
import com.not_found.financial_planner_api.model.MerchantCategory;
import com.not_found.financial_planner_api.model.Transaction;


@Service
public class TransactionCategorizationService {
    // Category Constants
    public static final String CATEGORY_GROCERIES = "Food & Groceries";
    public static final String CATEGORY_TRANSPORTATION = "Transportation";
    public static final String CATEGORY_HOUSING = "Housing & Utilities";
    public static final String CATEGORY_ENTERTAINMENT = "Entertainment";
    public static final String CATEGORY_HEALTHCARE = "Healthcare";
    public static final String CATEGORY_EDUCATION = "Education";
    public static final String CATEGORY_SHOPPING = "Shopping";
    public static final String CATEGORY_OTHER = "Other";

    private final List<MerchantCategory> merchantCategories;

    public TransactionCategorizationService(List<MerchantCategory> merchantCategories) {
        this.merchantCategories = merchantCategories;
    }

    public Map<String, Double> categorizeTransactions(List<Transaction> transactions) {
        Map<String, Double> categories = new java.util.LinkedHashMap<>();
        categories.put(CATEGORY_GROCERIES, 0.0);
        categories.put(CATEGORY_TRANSPORTATION, 0.0);
        categories.put(CATEGORY_HOUSING, 0.0);
        categories.put(CATEGORY_ENTERTAINMENT, 0.0);
        categories.put(CATEGORY_HEALTHCARE, 0.0);
        categories.put(CATEGORY_EDUCATION, 0.0);
        categories.put(CATEGORY_SHOPPING, 0.0);
        categories.put(CATEGORY_OTHER, 0.0);

        for (Transaction tx : transactions) {
            
            if (tx.getAmount() >= 0) continue; // Skip income transactions
            
            double amount = Math.abs(tx.getAmount());
            String category = CATEGORY_OTHER;

            // Try to match merchant categories
            for (MerchantCategory mc : merchantCategories) {
                if (mc.matches(tx, transactions)) {
                    category = mc.getCategory();
                    break;
                }
            }

            // Fallback to amount-based classification if no merchant match
            if (category.equals(CATEGORY_OTHER)) {
                category = classifyByAmount(amount);
            }
            tx.setCategory(category);
            
            categories.put(category, categories.getOrDefault(category, 0.0
            ) + amount);
            
        }

        return categories;
    }

    private String classifyByAmount(double amount) {
        List<Transaction> transactions = SampleData.getTransactions();
        for(Transaction tx : transactions){
            if (tx.getCategory() == "Gas"){
                return CATEGORY_TRANSPORTATION; // Gas purchases
            } else if (amount < 20.0) {
                return CATEGORY_GROCERIES; // Small purchases likely groceries
            } else if (amount >= 1000.0) {
                return CATEGORY_HOUSING; // Large amounts likely housing/utilities
            } else if (amount >= 100.0 && amount < 1000.0) {
                return CATEGORY_SHOPPING; // Medium amounts likely shopping
            } else if (amount >= 50.0 && amount < 100.0) {
                return CATEGORY_ENTERTAINMENT; // Moderate amounts likely entertainment
            } else if (amount >= 20.0 && amount < 50.0) {
                return CATEGORY_TRANSPORTATION; // Smaller amounts likely transportation
            }
        }
        return CATEGORY_OTHER;
    }
}