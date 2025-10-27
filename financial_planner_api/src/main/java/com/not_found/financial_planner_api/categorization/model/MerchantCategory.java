package com.not_found.financial_planner_api.categorization.model;

import com.not_found.financial_planner_api.model.Transaction;
import java.util.List;
import lombok.Getter;

@Getter
public class MerchantCategory {
    private final String merchantName;
    private final String category;
    private final List<TransactionPattern> patterns;

    public MerchantCategory(String merchantName, String category, List<TransactionPattern> patterns) {
        this.merchantName = merchantName;
        this.category = category;
        this.patterns = patterns;
    }

    public boolean matches(Transaction tx, List<Transaction> history) {
        if (tx.getDescription() == null || !tx.getDescription().toLowerCase().contains(merchantName.toLowerCase())) {
            return false;
        }

        // If no patterns defined, match based on merchant name only
        if (patterns == null || patterns.isEmpty()) {
            return true;
        }

        // Check if any pattern matches
        return patterns.stream().anyMatch(p -> p.matches(tx, history));
    }
}