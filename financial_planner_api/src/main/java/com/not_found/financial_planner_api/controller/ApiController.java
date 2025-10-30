package com.not_found.financial_planner_api.controller;

import com.not_found.financial_planner_api.model.Account;
import com.not_found.financial_planner_api.model.Transaction;
import com.not_found.financial_planner_api.service.SService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    /**
     * Constructor for dependency injection of the service layer
     * @param dataService The service layer instance
     */
    public ApiController(SService dataService) {
        this.dataService = dataService;
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

    @GetMapping("/dashboard")
    public com.not_found.financial_planner_api.model.DashboardResponse getDashboard(@org.springframework.web.bind.annotation.RequestParam(value = "accountId", required = false) Long accountId) {
        List<Transaction> recent;
        java.util.Map<String, Double> breakdown;
        if (accountId == null) {
            recent = dataService.getRecentTransactions(6);
            breakdown = dataService.getExpenseBreakdown();
        } else {
            recent = dataService.getRecentTransactionsForAccount(accountId, 3);
            breakdown = dataService.getExpenseBreakdownForAccount(accountId);
        }
        return new com.not_found.financial_planner_api.model.DashboardResponse(recent, breakdown);
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
}
