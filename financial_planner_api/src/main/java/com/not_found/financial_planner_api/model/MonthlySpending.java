package com.not_found.financial_planner_api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MonthlySpending {
    private String month; // YYYY-MM format
    private double totalSpent;
    private int transactionCount;

    public MonthlySpending() {}

    public MonthlySpending(String month, double totalSpent,
                          int transactionCount) {
        this.month = month;
        this.totalSpent = totalSpent;
        this.transactionCount = transactionCount;
    }
}