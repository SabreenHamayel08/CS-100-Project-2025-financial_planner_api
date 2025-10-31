package com.not_found.financial_planner_api.service;

import com.not_found.financial_planner_api.data.SampleData;
import com.not_found.financial_planner_api.model.Account;
import com.not_found.financial_planner_api.model.Transaction;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service layer for the Financial Planner application.
 * Handles business logic, data operations, and transaction categorization.
 */
@Service
public class SService {
    /** List of all accounts in the system */
    private List<Account> accounts;
    
    /** List of all transactions in the system */
    private List<Transaction> transactions;
    
    /** Service for categorizing transactions */
    private final TransactionCategorizationService categorizationService;

    /**
     * Constructor initializes the categorization service with merchant categories
     */
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

    /**
     * Get all transactions with optional sorting
     * @param sortBy Sorting criterion: "date", "amount", "description", or "account"
     * @param order Sort order: "asc" for ascending, "desc" for descending
     * @return Sorted list of all transactions
     */
    public List<Transaction> getTransactions(String sortBy, String order) {
        List<Transaction> result = new ArrayList<>(transactions);
        sortTransactions(result, sortBy, order);
        return result;
    }

    /**
     * Get all transactions in default order
     * @return List of all transactions
     */
    public List<Transaction> getTransactions() {
        return getTransactions(null, null);
    }

    /**
     * Get transactions for a specific account with optional sorting
     * @param accountId Account ID to filter transactions
     * @param sortBy Sorting criterion: "date", "amount", "description", or "account"
     * @param order Sort order: "asc" for ascending, "desc" for descending
     * @return Sorted list of transactions for the specified account
     */
    public List<Transaction> getTransactionsForAccount(long accountId, String sortBy, String order) {
        List<Transaction> filtered = transactions.stream()
                .filter(t -> t.getAccountId() == accountId)
                .collect(Collectors.toList());
        sortTransactions(filtered, sortBy, order);
        return filtered;
    }

    public List<Transaction> getTransactionsForAccount(long accountId) {
        return getTransactionsForAccount(accountId, null, null);
    }

    /**
     * Sort a list of transactions based on specified criteria and order
     * @param txs List of transactions to sort
     * @param sortBy Sorting criterion: "date", "amount", "description", or "account"
     * @param order Sort order: "asc" for ascending, "desc" for descending
     */
    private void sortTransactions(List<Transaction> txs, String sortBy, String order) {
        if (sortBy == null) return;

        boolean ascending = order == null || order.equalsIgnoreCase("asc");
        
        switch (sortBy.toLowerCase()) {
            case "date":
                // Sort by transaction date
                txs.sort((a, b) -> ascending ? 
                    a.getDate().compareTo(b.getDate()) :
                    b.getDate().compareTo(a.getDate()));
                break;
            case "amount":
                // Sort by transaction amount
                txs.sort((a, b) -> ascending ? 
                    Double.compare(a.getAmount(), b.getAmount()) :
                    Double.compare(b.getAmount(), a.getAmount()));
                break;
            case "description":
                // Sort by transaction description alphabetically
                txs.sort((a, b) -> ascending ?
                    compareDescriptions(a, b) :
                    compareDescriptions(b, a));
                break;
            case "account":
                // Sort by account ID
                txs.sort((a, b) -> ascending ?
                    Long.compare(a.getAccountId(), b.getAccountId()) :
                    Long.compare(b.getAccountId(), a.getAccountId()));
                break;
            default:
                // No sorting if invalid sort parameter
                break;
        }
    }

    private int compareDescriptions(Transaction a, Transaction b) {
        String descA = a.getDescription() == null ? "" : a.getDescription();
        String descB = b.getDescription() == null ? "" : b.getDescription();
        return descA.compareToIgnoreCase(descB);
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

    /**
     * Search transactions by description or date range
     * @param query Search query for description (case-insensitive)
     * @param startDate Optional start date in YYYY-MM-DD format
     * @param endDate Optional end date in YYYY-MM-DD format
     * @param accountId Optional account ID to filter transactions
     * @return List of matching transactions
     */
    public List<Transaction> searchTransactions(String query, String startDate, String endDate, Long accountId) {
        List<Transaction> result = new ArrayList<>(transactions);

        // Filter by account if specified
        if (accountId != null) {
            result = result.stream()
                    .filter(t -> t.getAccountId() == accountId)
                    .collect(Collectors.toList());
        }

        // Filter by description if query is provided
        if (query != null && !query.trim().isEmpty()) {
            String searchQuery = query.trim().toLowerCase();
            result = result.stream()
                    .filter(t -> t.getDescription() != null && 
                               t.getDescription().toLowerCase().contains(searchQuery))
                    .collect(Collectors.toList());
        }

        // Filter by date range if provided
        if (startDate != null || endDate != null) {
            LocalDate start = startDate != null ? LocalDate.parse(startDate) : LocalDate.MIN;
            LocalDate end = endDate != null ? LocalDate.parse(endDate) : LocalDate.MAX;

            result = result.stream()
                    .filter(t -> !t.getDate().isBefore(start) && !t.getDate().isAfter(end))
                    .collect(Collectors.toList());
        }

        // Sort results by date (newest first) by default
        result.sort((a, b) -> b.getDate().compareTo(a.getDate()));
        
        return result;
    }

    private Map<String, Double> categorize(List<Transaction> txs) {
        return categorizationService.categorizeTransactions(txs);
    }
}
