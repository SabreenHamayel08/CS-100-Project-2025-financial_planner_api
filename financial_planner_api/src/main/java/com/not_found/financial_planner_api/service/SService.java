package com.not_found.financial_planner_api.service;

import com.not_found.financial_planner_api.data.SampleData;
import com.not_found.financial_planner_api.model.Account;
import com.not_found.financial_planner_api.model.Transaction;
import com.not_found.financial_planner_api.categorization.service.TransactionCategorizationService;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SService {
    private List<Account> accounts;
    private List<Transaction> transactions;
    private final TransactionCategorizationService categorizationService;

    public SService() {
        this.categorizationService = new TransactionCategorizationService(SampleData.getMerchantCategories());
    }

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
        return categorizationService.categorizeTransactions(txs);
    }
}
