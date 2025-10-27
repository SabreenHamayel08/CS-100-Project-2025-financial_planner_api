package com.not_found.financial_planner_api.service;

import com.not_found.financial_planner_api.data.SampleData;
import com.not_found.financial_planner_api.model.Account;
import com.not_found.financial_planner_api.model.Transaction;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SampleDataService {
    private List<Account> accounts;
    private List<Transaction> transactions;

    @PostConstruct
    public void init() {
        transactions = SampleData.getTransactions();
        accounts = SampleData.getAccounts();
    }

    public List<Account> getAccounts() {
        return new ArrayList<>(accounts);
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public List<Transaction> getTransactionsForAccount(long accountId) {
        return transactions.stream()
                .filter(t -> t.getAccountId() == accountId)
                .collect(Collectors.toList());
    }

    public List<Transaction> getRecentTransactions(int limit) {
        return transactions.stream()
                .sorted((a,b) -> b.getDate().compareTo(a.getDate()))
                .limit(limit)
                .toList();
    }

    public List<Transaction> getRecentTransactionsForAccount(long accountId, int limit) {
        return transactions.stream()
                .filter(t -> t.getAccountId() == accountId)
                .sorted((a,b) -> b.getDate().compareTo(a.getDate()))
                .limit(limit)
                .toList();
    }

    public Map<String, Double> getExpenseBreakdown() {
        return categorize(transactions);
    }

    public Map<String, Double> getExpenseBreakdownForAccount(long accountId) {
        List<Transaction> tx = getTransactionsForAccount(accountId);
        return categorize(tx);
    }

    private Map<String, Double> categorize(List<Transaction> txs) {
        Map<String, Double> categories = new java.util.LinkedHashMap<>();
        categories.put("Food & Groceries", 0.0);
        categories.put("Gas", 0.0);
        categories.put("Bills", 0.0);
        categories.put("Entertainment", 0.0);
        categories.put("Other", 0.0);

        for (Transaction t : txs) {
            if (t.getAmount() >= 0) continue;
            String desc = (t.getDescription() == null) ? "" : t.getDescription().toLowerCase();
            double amt = Math.abs(t.getAmount());
            if (desc.contains("whole") || desc.contains("trader") || desc.contains("jewel") || desc.contains("food") || desc.contains("grocery")) {
                categories.put("Food & Groceries", categories.get("Food & Groceries") + amt);
            } else if (desc.contains("gas")) {
                categories.put("Gas", categories.get("Gas") + amt);
            } else if (desc.contains("netflix") || desc.contains("entertain")) {
                categories.put("Entertainment", categories.get("Entertainment") + amt);
            } else if (desc.contains("bill") || desc.contains("payment") || desc.contains("deposit") || desc.contains("atm")) {
                categories.put("Bills", categories.get("Bills") + amt);
            } else {
                categories.put("Other", categories.get("Other") + amt);
            }
        }
        return categories;
    }
}
