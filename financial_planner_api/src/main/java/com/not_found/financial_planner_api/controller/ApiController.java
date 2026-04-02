package com.not_found.financial_planner_api.controller;

import com.not_found.financial_planner_api.model.Account;
import com.not_found.financial_planner_api.model.Transaction;
import com.not_found.financial_planner_api.repository.TransactionRepository;
import com.not_found.financial_planner_api.model.RewardsAnalysis;
import com.not_found.financial_planner_api.service.SService;
import com.not_found.financial_planner_api.service.TransactionCategorizationService;


import com.not_found.financial_planner_api.service.AnalyticsService;
import com.not_found.financial_planner_api.service.RewardsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.not_found.financial_planner_api.model.MonthlySpending;
import com.not_found.financial_planner_api.entity.AccountEntity;
import com.not_found.financial_planner_api.repository.AccountRepository;



import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

/**
 * REST API Controller for the Financial Planner application.
 * Provides endpoints for accessing account information, transactions, and dashboard data.
 * Includes various sorting options for transaction data.
 */
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
public class ApiController {

    /** Service layer for handling business logic and data operations */
    private final SService dataService;
    private final RewardsService rewardsService;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    

    /**
     * Constructor for dependency injection of the service layers
     * @param dataService The service layer instance for data operations
     * @param rewardsService The service layer instance for rewards analysis
     */
    public ApiController(SService dataService, RewardsService rewardsService, AnalyticsService analyticsService, TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.dataService = dataService;
        this.rewardsService = rewardsService;
        this.analyticsService = analyticsService;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Get all accounts in the system
     * @return List of all accounts
     */
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/accounts")
    public List<Account> getAccounts() {
        return dataService.getAccounts();
    }
    /*
     * Subscription API - Subscription plan 
       - if they have the basic plan, dashboard only shows expense categorization
       and last 6 transactions and also monthly spending summary for free users
        - Premium plan users get to see future trends graph analysis, create budget goals, 
        and see best card recommendation based on their spending patterns
     */
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/accounts/subscription/{plan}")
    public List<Account> getAccountsBySubscriptionPlan(@PathVariable("plan") String plan){
        List<Account> allAccounts = dataService.getAccounts();
        List<Account> filteredAccounts = new ArrayList<>();
        for (Account account : allAccounts) {
            if (account.getSubscriptionPlan().equalsIgnoreCase(plan)) {
                filteredAccounts.add(account);
            }
        }
        return filteredAccounts;
    }
           

     /*
      * Customer info -customer is either a free member account
        or a premium user account 
        Also inscludes their Name, Age, date joined, account cards,
        balance, etc.
      */
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/accounts/customerinfo/{name}")
    public List<Account> getAccountsByCustomerName(@PathVariable("name") String name){
        List<Account> allAccounts = dataService.getAccounts();
        List<Account> filteredAccounts = new ArrayList<>();
        for (Account account : allAccounts) {
            if (account.getAccountName().equalsIgnoreCase(name)) {
                filteredAccounts.add(account);
            }
        }

        return filteredAccounts;
    }

    /**
     * Get all transactions for a specific account
     * @param id Account ID
     * @return List of transactions for the specified account
     */
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/accounts/{id}/transactions")
    public List<Transaction> getTransactionsForAccount(@PathVariable("id") String id) {
        return transactionRepository.findByAccountNumberOrderByDateDesc(id).stream()
                .map(e -> {
                    Transaction t = new Transaction();
                    t.setId(e.getTransactionId());
                    t.setAccountId(e.getAccountNumber());
                    t.setDate(e.getTransactionDate());
                    t.setDescription(e.getDescription());
                    double amt = e.getTransactionAmount() != null ? e.getTransactionAmount().doubleValue() : 0.0;
                    t.setAmount(java.math.BigDecimal.valueOf(amt).setScale(2, RoundingMode.HALF_UP).doubleValue());
                    t.setCategory(e.getTransactionCategory());
                    return t;
                })
                .toList();
    }

    /**
     * Get account transactions sorted by date in ascending order (oldest first)
     * @param id Account ID
     * @return List of sorted transactions
     */
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/accounts/{id}/transactions/by-date/asc")
    public List<Transaction> getAccountTransactionsByDateAsc(@PathVariable("id") String id) {
        return dataService.getTransactionsForAccount(id, "date", "asc");
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/accounts/{id}/transactions/by-date/desc")
    public List<Transaction> getAccountTransactionsByDateDesc(@PathVariable("id") String id) {
        return dataService.getTransactionsForAccount(id, "date", "desc");
    }
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/accounts/{id}/transactions/by-amount/asc")
    public List<Transaction> getAccountTransactionsByAmountAsc(@PathVariable("id") String id) {
        return dataService.getTransactionsForAccount(id, "amount", "asc");
    }
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/accounts/{id}/transactions/by-amount/desc")
    public List<Transaction> getAccountTransactionsByAmountDesc(@PathVariable("id") String id) {
        return dataService.getTransactionsForAccount(id, "amount", "desc");
    }
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/accounts/{id}/transactions/by-description/asc")
    public List<Transaction> getAccountTransactionsByDescriptionAsc(@PathVariable("id") String id) {
        return dataService.getTransactionsForAccount(id, "description", "asc");
    }
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/accounts/{id}/transactions/by-description/desc")
    public List<Transaction> getAccountTransactionsByDescriptionDesc(@PathVariable("id") String id) {
        return dataService.getTransactionsForAccount(id, "description", "desc");
    }
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/transactions")
    public List<Transaction> getTransactions() {
        return dataService.getTransactions();
    }
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/transactions/by-date/asc")
    public List<Transaction> getTransactionsByDateAsc() {
        return dataService.getTransactions("date", "asc");
    }
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/transactions/by-date/desc")
    public List<Transaction> getTransactionsByDateDesc() {
        return dataService.getTransactions("date", "desc");
    }
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/transactions/by-amount/asc")
    public List<Transaction> getTransactionsByAmountAsc() {
        return dataService.getTransactions("amount", "asc");
    }
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/transactions/by-amount/desc")
    public List<Transaction> getTransactionsByAmountDesc() {
        return dataService.getTransactions("amount", "desc");
    }
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/transactions/by-description/asc")
    public List<Transaction> getTransactionsByDescriptionAsc() {
        return dataService.getTransactions("description", "asc");
    }
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/transactions/by-description/desc")
    public List<Transaction> getTransactionsByDescriptionDesc() {
        return dataService.getTransactions("description", "desc");
    }
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/transactions/by-account/asc")
    public List<Transaction> getTransactionsByAccountAsc() {
        return dataService.getTransactions("account", "asc");
    }
    @CrossOrigin(origins = "http://localhost:5173")
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
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/transactions/search")
    public List<Transaction> searchTransactions(
            @org.springframework.web.bind.annotation.RequestParam(required = false) String query,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String startDate,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String endDate,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String accountId
    ) {
        return dataService.searchTransactions(query, startDate, endDate, accountId);
    }
    
    @Autowired
    private TransactionCategorizationService TransactionCategorizationService;
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/dashboard")
    public com.not_found.financial_planner_api.model.DashboardResponse getDashboard(@org.springframework.web.bind.annotation.RequestParam(value = "accountId", required = false) String accountId) {
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
    private List<MonthlySpending> getLastTwelveMonthsSpending(String accountId) {
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
    

    private final AnalyticsService analyticsService;
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/expenses/pie")
    public Map<String, BigDecimal> getExpensePieChart() {
        return analyticsService.getExpenseCategoryPieChart();
    }
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/expenses/pie/{id}")
    public Map<String, BigDecimal> getExpensePieChartForAccount(@PathVariable("id") String id) {
        Map<String, BigDecimal> results = new HashMap<>();

        // If 'id' is an account number, return pie for that account.
        if (accountRepository.existsById(id)) {
            List<Object[]> rows = transactionRepository.getExpenseTotalsByCategoryForAccount(id);
            for (Object[] r : rows) {
                String cat = String.valueOf(r[0]);
                java.math.BigDecimal val = (java.math.BigDecimal) r[1];
                java.math.BigDecimal outVal = val != null ? val.abs() : java.math.BigDecimal.ZERO;
                results.put(cat, outVal.setScale(2, RoundingMode.HALF_UP));
            }
            return new TreeMap<>(results);
        }

        // Otherwise assume 'id' is a user id and aggregate across the user's accounts
        List<AccountEntity> accounts = accountRepository.findByUserId(id);
        if (accounts == null || accounts.isEmpty()) {
            // no accounts or unknown id -> return empty map
            return new TreeMap<>();
        }

        for (AccountEntity acc : accounts) {
            String accNum = acc.getAccountNumber();
            List<Object[]> rows = transactionRepository.getExpenseTotalsByCategoryForAccount(accNum);
            for (Object[] r : rows) {
                String cat = String.valueOf(r[0]);
                java.math.BigDecimal val = (java.math.BigDecimal) r[1];
                java.math.BigDecimal outVal = val != null ? val.abs() : java.math.BigDecimal.ZERO;
                results.merge(cat, outVal, BigDecimal::add);
            }
        }

        // Round aggregated values to 2 decimals
        results.replaceAll((k, v) -> v.setScale(2, RoundingMode.HALF_UP));
        return new TreeMap<>(results);
    }
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/income/predict")
    public Map<String, BigDecimal> predictIncome(
            @RequestParam(defaultValue = "3") int months) {
        return analyticsService.predictFutureIncome(months);
    }
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/income/predict/{id}")
    public Map<String, BigDecimal> predictIncomeForAccount(@PathVariable("id") String id) {
        // Use AnalyticsService for per-account income prediction (moved from SService)
        return analyticsService.predictFutureIncomeForAccount(id, 3);
    }
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/income/analysis")
    public Map<String, BigDecimal> incomeAnalysis() {
        Map<String, BigDecimal> results = transactionRepository.getMonthlyTotals().stream().collect(
            HashMap::new,
            (map, row) -> {
                String k = String.valueOf(row[0]);
                java.math.BigDecimal v = (java.math.BigDecimal) row[1];
                map.put(k, v != null ? v.setScale(2, RoundingMode.HALF_UP) : java.math.BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            },
            HashMap::putAll
        );
        return new TreeMap<> (results);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/income/analysis/{id}")
    public Map<String, BigDecimal> incomeAnalysisForAccount(@PathVariable("id") String id) {
        Map<String, BigDecimal> results = transactionRepository.getMonthlyTotalsForAccount(id).stream().collect(
            HashMap::new,
            (map, row) -> {
                String k = String.valueOf(row[0]);
                java.math.BigDecimal v = (java.math.BigDecimal) row[1];
                map.put(k, v != null ? v.setScale(2, RoundingMode.HALF_UP) : java.math.BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            },
            HashMap::putAll
        );
        return new TreeMap<> (results);
    }
    


    /**
     * Get rewards analysis for transactions. Can be filtered by account ID.
     * Analyzes spending patterns and provides recommendations for reward cards.
     * 
     * @param accountId Optional account ID to analyze rewards for specific account
     * @return RewardsAnalysis containing spending analysis and card recommendations
     */
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/rewards")
    public RewardsAnalysis getRewardsAnalysis(
            @org.springframework.web.bind.annotation.RequestParam(value = "accountId", required = false) String accountId
    ) {
        List<Transaction> transactions;
        if (accountId == null) {
            // load all transactions from repository (DB-backed)
            transactions = transactionRepository.findAll().stream().map(e -> {
                Transaction t = new Transaction();
                t.setId(e.getTransactionId());
                t.setAccountId(e.getAccountNumber());
                t.setDate(e.getTransactionDate());
                t.setDescription(e.getDescription());
                double amt = e.getTransactionAmount() != null ? e.getTransactionAmount().doubleValue() : 0.0;
                t.setAmount(java.math.BigDecimal.valueOf(amt).setScale(2, RoundingMode.HALF_UP).doubleValue());
                t.setCategory(e.getTransactionCategory());
                return t;
            }).toList();
        } else {
            // load transactions for a specific account from repository
            transactions = transactionRepository.findByAccountNumberOrderByDateDesc(accountId).stream().map(e -> {
                Transaction t = new Transaction();
                t.setId(e.getTransactionId());
                t.setAccountId(e.getAccountNumber());
                t.setDate(e.getTransactionDate());
                t.setDescription(e.getDescription());
                double amt = e.getTransactionAmount() != null ? e.getTransactionAmount().doubleValue() : 0.0;
                t.setAmount(java.math.BigDecimal.valueOf(amt).setScale(2, RoundingMode.HALF_UP).doubleValue());
                t.setCategory(e.getTransactionCategory());
                return t;
            }).toList();
        }

        // Use RewardsService analyzeRewards which will load relevant cards internally
        return rewardsService.analyzeRewards(transactions);
    }

    /**
     * Return a static projected annual return per account (demo/static values).
     */
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/rewards/accounts")
    public java.util.List<java.util.Map<String, Object>> getRewardsPerAccount() {
        java.util.List<Account> accounts = dataService.getAccounts();
        java.util.List<java.util.Map<String, Object>> out = new ArrayList<>();
        for (Account acc : accounts) {
            java.util.Map<String, Object> m = new HashMap<>();
            String accNum = acc.getAccountNumber();
            m.put("accountId", accNum);
            double val = 0.0;
            try {
                int h = accNum != null ? Math.abs(accNum.hashCode()) : 0;
                // deterministic static value between 0.00 and 49.99
                val = (h % 5000) / 100.0;
            } catch (Exception ex) {
                val = 0.0;
            }
            // round to 2 decimals
            m.put("projectedAnnualReturn", Math.round(val * 100.0) / 100.0);
            m.put("recommendationReason", "Static projection");
            out.add(m);
        }
        return out;
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/rewards/accounts/{id}")
    public java.util.Map<String, Object> getRewardsForAccount(@PathVariable("id") String id) {
        java.util.Map<String, Object> m = new HashMap<>();
        m.put("accountId", id);
        if (!accountRepository.existsById(id)) {
            m.put("projectedAnnualReturn", 0.0);
            m.put("recommendationReason", "Account not found");
            return m;
        }
        double val = 0.0;
        try {
            int h = id != null ? Math.abs(id.hashCode()) : 0;
            val = (h % 5000) / 100.0;
        } catch (Exception ex) {
            val = 0.0;
        }
        m.put("projectedAnnualReturn", Math.round(val * 100.0) / 100.0);
        m.put("recommendationReason", "Static projection");
        return m;
    }

}