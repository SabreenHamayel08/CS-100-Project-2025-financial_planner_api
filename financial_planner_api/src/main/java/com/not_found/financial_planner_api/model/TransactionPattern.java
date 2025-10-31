package com.not_found.financial_planner_api.model;

import java.util.List;
import lombok.Getter;

@Getter
public class TransactionPattern {
    private final double minAmount;
    private final double maxAmount;
    private final String category;
    private final int frequencyPerMonth;

    public TransactionPattern(double minAmount, double maxAmount, String category, int frequencyPerMonth) {
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.category = category;
        this.frequencyPerMonth = frequencyPerMonth;
    }

    public boolean matches(Transaction tx, List<Transaction> history) {
        double amount = Math.abs(tx.getAmount());
        if (amount < minAmount || amount > maxAmount) {
            return false;
        }

        // If frequency is specified, check the transaction history
        if (frequencyPerMonth > 0) {
            long monthlyCount = history.stream()
                .filter(t -> t.getDescription() != null 
                    && t.getDescription().equalsIgnoreCase(tx.getDescription())
                    && t.getDate().getYear() == tx.getDate().getYear()
                    && t.getDate().getMonth() == tx.getDate().getMonth())
                .count();
            return monthlyCount <= frequencyPerMonth;
        }

        return true;
    }
}