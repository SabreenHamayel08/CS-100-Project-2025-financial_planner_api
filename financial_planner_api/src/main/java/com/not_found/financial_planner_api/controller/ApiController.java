package com.not_found.financial_planner_api.controller;

import com.not_found.financial_planner_api.model.Account;
import com.not_found.financial_planner_api.model.Transaction;
import com.not_found.financial_planner_api.model.RewardsAnalysis;
import com.not_found.financial_planner_api.service.SService;
import com.not_found.financial_planner_api.service.TransactionCategorizationService;
import com.not_found.financial_planner_api.service.RewardsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.not_found.financial_planner_api.model.MonthlySpending;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

/**
 * REST API Controller for the Financial Planner application.
 * Provides endpoints for accessing account information, transactions, and dashboard data.
 * Includes various sorting options for transaction data.
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    /** Service layer for handling business logic and data operations */
    private final SService dataService;
    private final RewardsService rewardsService;

    /**
     * Constructor for dependency injection of the service layers
     * @param dataService The service layer instance for data operations
     * @param rewardsService The service layer instance for rewards analysis
     */
    public ApiController(SService dataService, RewardsService rewardsService) {
        this.dataService = dataService;
        this.rewardsService = rewardsService;
    }

    /**
     * Get all accounts in the system
     * @return List of all accounts
     */
    @GetMapping("/accounts")
    public List<Account> getAccounts() {
        return dataService.getAccounts();
    }

    /**
     * Get all transactions for a specific account
     * @param id Account ID
     * @return List of transactions for the specified account
     */
    @GetMapping("/accounts/{id}/transactions")
    public List<Transaction> getTransactionsForAccount(@PathVariable("id") long id) {
        return dataService.getTransactionsForAccount(id);
    }

    /**
     * Get account transactions sorted by date in ascending order (oldest first)
     * @param id Account ID
     * @return List of sorted transactions
     */
    @GetMapping("/accounts/{id}/transactions/by-date/asc")
    public List<Transaction> getAccountTransactionsByDateAsc(@PathVariable("id") long id) {
        return dataService.getTransactionsForAccount(id, "date", "asc");
    }

    @GetMapping("/accounts/{id}/transactions/by-date/desc")
    public List<Transaction> getAccountTransactionsByDateDesc(@PathVariable("id") long id) {
        return dataService.getTransactionsForAccount(id, "date", "desc");
    }

    @GetMapping("/accounts/{id}/transactions/by-amount/asc")
    public List<Transaction> getAccountTransactionsByAmountAsc(@PathVariable("id") long id) {
        return dataService.getTransactionsForAccount(id, "amount", "asc");
    }

    @GetMapping("/accounts/{id}/transactions/by-amount/desc")
    public List<Transaction> getAccountTransactionsByAmountDesc(@PathVariable("id") long id) {
        return dataService.getTransactionsForAccount(id, "amount", "desc");
    }

    @GetMapping("/accounts/{id}/transactions/by-description/asc")
    public List<Transaction> getAccountTransactionsByDescriptionAsc(@PathVariable("id") long id) {
        return dataService.getTransactionsForAccount(id, "description", "asc");
    }

    @GetMapping("/accounts/{id}/transactions/by-description/desc")
    public List<Transaction> getAccountTransactionsByDescriptionDesc(@PathVariable("id") long id) {
        return dataService.getTransactionsForAccount(id, "description", "desc");
    }

    @GetMapping("/transactions")
    public List<Transaction> getTransactions() {
        return dataService.getTransactions();
    }

    @GetMapping("/transactions/by-date/asc")
    public List<Transaction> getTransactionsByDateAsc() {
        return dataService.getTransactions("date", "asc");
    }

    @GetMapping("/transactions/by-date/desc")
    public List<Transaction> getTransactionsByDateDesc() {
        return dataService.getTransactions("date", "desc");
    }

    @GetMapping("/transactions/by-amount/asc")
    public List<Transaction> getTransactionsByAmountAsc() {
        return dataService.getTransactions("amount", "asc");
    }

    @GetMapping("/transactions/by-amount/desc")
    public List<Transaction> getTransactionsByAmountDesc() {
        return dataService.getTransactions("amount", "desc");
    }

    @GetMapping("/transactions/by-description/asc")
    public List<Transaction> getTransactionsByDescriptionAsc() {
        return dataService.getTransactions("description", "asc");
    }

    @GetMapping("/transactions/by-description/desc")
    public List<Transaction> getTransactionsByDescriptionDesc() {
        return dataService.getTransactions("description", "desc");
    }

    @GetMapping("/transactions/by-account/asc")
    public List<Transaction> getTransactionsByAccountAsc() {
        return dataService.getTransactions("account", "asc");
    }

    @GetMapping("/transactions/by-account/desc")
    public List<Transaction> getTransactionsByAccountDesc() {
        return dataService.getTransactions("account", "desc");
    }

    /**
     * Search transactions by description or date
     * @param query Search query for description (case-insensitive)
     * @param startDate Optional start date (format: YYYY-MM-DD)
     * @param endDate Optional end date (format: YYYY-MM-DD)
     * @param accountId Optional account ID to filter transactions
     * @return List of matching transactions
     * Search by description: GET /api/transactions/search?query=coffee
     * Search by date range: GET /api/transactions/search?startDate=2025-01-01&endDate=2025-12-31
     * Search by description within date range: GET /api/transactions/search?query=grocery&startDate=2025-01-01&endDate=2025-12-31
     * Search in specific account: GET /api/transactions/search?query=coffee&accountId=1
     * Search by date range in specific account: GET /api/transactions/search?startDate=2025-01-01&endDate=2025-12-31&accountId=1
     */
    @GetMapping("/transactions/search")
    public List<Transaction> searchTransactions(
            @org.springframework.web.bind.annotation.RequestParam(required = false) String query,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String startDate,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String endDate,
            @org.springframework.web.bind.annotation.RequestParam(required = false) Long accountId
    ) {
        return dataService.searchTransactions(query, startDate, endDate, accountId);
    }
    
    @Autowired
    private TransactionCategorizationService TransactionCategorizationService;
    @GetMapping("/dashboard")
    public com.not_found.financial_planner_api.model.DashboardResponse getDashboard(@org.springframework.web.bind.annotation.RequestParam(value = "accountId", required = false) Long accountId) {
        List<Transaction> recent;
        java.util.Map<String, Double> breakdown;
        RewardsAnalysis.CardRecommendation bestCard;
        List<MonthlySpending> monthlySpending = new ArrayList<>();
        
        if (accountId == null) {
            recent = dataService.getRecentTransactions(6);
            TransactionCategorizationService.categorizeTransactions(recent);
            breakdown = dataService.getExpenseBreakdown();
            // Get all transactions for rewards analysis
            List<Transaction> allTransactions = dataService.getTransactions();
            bestCard = rewardsService.analyzeRewards(allTransactions)
                .getRecommendedCards()
                .stream()
                .findFirst()
                .orElse(null);
                
            // Get monthly spending for last 12 months
            monthlySpending = getLastTwelveMonthsSpending(accountId);
        } else {
            recent = dataService.getRecentTransactionsForAccount(accountId, 6);
            TransactionCategorizationService.categorizeTransactions(recent);
            breakdown = dataService.getExpenseBreakdownForAccount(accountId);
            // Get account transactions for rewards analysis
            List<Transaction> accountTransactions = dataService.getTransactionsForAccount(accountId);
            bestCard = rewardsService.analyzeRewards(accountTransactions)
                .getRecommendedCards()
                .stream()
                .findFirst()
                .orElse(null);
                
            // Get monthly spending for last 12 months for this account
            monthlySpending = getLastTwelveMonthsSpending(accountId);
        }
        return new com.not_found.financial_planner_api.model.DashboardResponse(recent, breakdown, bestCard, monthlySpending);
    }
    
    /**
     * Helper method to get spending aggregation for the last 12 months
     */
    private List<MonthlySpending> getLastTwelveMonthsSpending(Long accountId) {
        List<MonthlySpending> result = new ArrayList<>();
        LocalDate now = LocalDate.now();
        
        // Get last 6 months
        for (int i = 0; i < 12; i++) {
            YearMonth month = YearMonth.from(now.minusMonths(i));
            String monthStr = month.toString(); // YYYY-MM format
            
            // Get transactions for this month
            List<Transaction> transactions = dataService.searchTransactions(
                null, 
                month.atDay(1).toString(),
                month.atEndOfMonth().toString(),
                accountId
            );
            
            // Calculate totals
            double totalSpent = transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .mapToDouble(t -> Math.abs(t.getAmount()))
                .sum();
                
            // Get spending breakdown by category
            Map<String, Double> categoryBreakdown = new HashMap<>();
            transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .forEach(t -> {
                    String category = t.getCategory();
                    if (category == null || category.isEmpty()) {
                        category = rewardsService.determineCategory(t);
                    }
                    categoryBreakdown.merge(category, Math.abs(t.getAmount()), Double::sum);
                });
            
            // Create monthly spending summary
            MonthlySpending monthSpending = new MonthlySpending(
                monthStr,
                totalSpent,
                transactions.size()
            );
            
            result.add(monthSpending);
        }
        
        return result;
    }

    @GetMapping("/data")
    public com.not_found.financial_planner_api.model.AllDataResponse getAllData(
            @org.springframework.web.bind.annotation.RequestParam(value = "accountId", required = false) Long accountId,
            @org.springframework.web.bind.annotation.RequestParam(value = "include", required = false) String include
    ) {
        // parse include param (comma-separated). If absent or empty => include all sections
        java.util.Set<String> inc = new java.util.HashSet<>();
        if (include == null || include.isBlank()) {
            inc.add("accounts");
            inc.add("transactions");
            inc.add("dashboard");
        } else {
            String[] parts = include.split(",");
            for (String p : parts) {
                inc.add(p.trim().toLowerCase());
            }
        }

        List<Account> accounts = null;
        List<Transaction> transactions = null;
        com.not_found.financial_planner_api.model.DashboardResponse dashboard = null;

        if (inc.contains("accounts")) {
            accounts = dataService.getAccounts();
        }

        if (inc.contains("transactions")) {
            transactions = (accountId == null) ? dataService.getTransactions() : dataService.getTransactionsForAccount(accountId);
        }

        if (inc.contains("dashboard")) {
            dashboard = getDashboard(accountId);
        }

        return new com.not_found.financial_planner_api.model.AllDataResponse(accounts, transactions, dashboard);
    }

    /**
     * Get rewards analysis for transactions. Can be filtered by account ID.
     * Analyzes spending patterns and provides recommendations for reward cards.
     * 
     * @param accountId Optional account ID to analyze rewards for specific account
     * @return RewardsAnalysis containing spending analysis and card recommendations
     */
    @GetMapping("/rewards")
    public RewardsAnalysis getRewardsAnalysis(
            @org.springframework.web.bind.annotation.RequestParam(value = "accountId", required = false) Long accountId
    ) {
        List<Transaction> transactions = (accountId == null) ? 
            dataService.getTransactions() : 
            dataService.getTransactionsForAccount(accountId);
        
        return rewardsService.analyzeRewards(transactions);
    }

    /**
     * Get the best card recommendation based on spending patterns up to a specific date
     * 
     * @param date The date up to which to analyze transactions (format: YYYY-MM-DD)
     * @return CardRecommendation for the most beneficial rewards card
     */
    @GetMapping("/bestCardRecommendation/{date}")
    public RewardsAnalysis.CardRecommendation getBestCardRecommendation(
            @PathVariable("date") String date,
            @org.springframework.web.bind.annotation.RequestParam(value = "accountId", required = false) Long accountId
    ) {
        List<Transaction> transactions = (accountId == null) ? 
            dataService.searchTransactions(null, null, date, accountId) : 
            dataService.searchTransactions(null, null, date, accountId);

        // Get full rewards analysis
        RewardsAnalysis analysis = rewardsService.analyzeRewards(transactions);
        
        // Return the top recommendation (first in the sorted list)
        List<RewardsAnalysis.CardRecommendation> recommendations = analysis.getRecommendedCards();
        return recommendations != null && !recommendations.isEmpty() ? 
            recommendations.get(0) : null;
    }

    /**
     * Get monthly aggregated spending across all registered cards
     * 
     * @param month Month in YYYY-MM format (e.g., 2025-10)
     * @return Map of account details to spending summaries
     */
    @GetMapping("/spending/{month}")
    public Map<String, Map<String, Object>> getMonthlySpending(@PathVariable("month") String month) {
        // Validate month format
        if (!month.matches("\\d{4}-\\d{2}")) {
            throw new IllegalArgumentException("Month must be in YYYY-MM format");
        }
        
        // Get all accounts
        List<Account> accounts = dataService.getAccounts();
        Map<String, Map<String, Object>> result = new HashMap<>();
        
        // Process each account
        for (Account account : accounts) {
            String startDate = month + "-01";
            String endDate = month + "-31"; // This works for our purpose since invalid dates will be handled by the search
            
            // Get transactions for this account in the specified month
            List<Transaction> transactions = dataService.searchTransactions(
                null, startDate, endDate, account.getId());
            
            // Calculate spending metrics
            double totalSpent = transactions.stream()
                .filter(t -> t.getAmount() < 0) // Only include expenses (negative amounts)
                .mapToDouble(t -> Math.abs(t.getAmount()))
                .sum();
                
            // Get spending breakdown by category
            Map<String, Double> categoryBreakdown = new HashMap<>();
            transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .forEach(t -> {
                    String category = t.getCategory();
                    if (category == null || category.isEmpty()) {
                        // Try to determine category from description
                        category = rewardsService.determineCategory(t);
                    }
                    categoryBreakdown.merge(category, Math.abs(t.getAmount()), Double::sum);
                });
            
            
            // Create summary for this account
            Map<String, Object> accountSummary = new HashMap<>();
            accountSummary.put("accountName", account.getName());
            accountSummary.put("totalSpent", totalSpent);
            accountSummary.put("transactionCount", transactions.size());
        
            
            // Add to result
            result.put(String.valueOf(account.getId()), accountSummary);
        }
        
        return result;
    }
}
