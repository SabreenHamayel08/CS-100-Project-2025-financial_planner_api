package com.not_found.financial_planner_api.service;

import com.not_found.financial_planner_api.model.Account;
import com.not_found.financial_planner_api.model.Transaction;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SampleDataService {
    private final List<Account> accounts = new ArrayList<>();
    private final List<Transaction> transactions = new ArrayList<>();

    @PostConstruct
    public void init() {
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
