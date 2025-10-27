package com.not_found.financial_planner_api.controller;

import com.not_found.financial_planner_api.model.Account;
import com.not_found.financial_planner_api.model.Transaction;
import com.not_found.financial_planner_api.service.SampleDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final SampleDataService dataService;

    public ApiController(SampleDataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/accounts")
    public List<Account> getAccounts() {
        return dataService.getAccounts();
    }

    @GetMapping("/accounts/{id}/transactions")
    public List<Transaction> getTransactionsForAccount(@PathVariable("id") long id) {
        return dataService.getTransactionsForAccount(id);
    }

    @GetMapping("/transactions")
    public List<Transaction> getTransactions() {
        return dataService.getTransactions();
    }

    @GetMapping("/dashboard")
    public com.not_found.financial_planner_api.model.DashboardResponse getDashboard(@org.springframework.web.bind.annotation.RequestParam(value = "accountId", required = false) Long accountId) {
        List<Transaction> recent;
        java.util.Map<String, Double> breakdown;
        if (accountId == null) {
            recent = dataService.getRecentTransactions(3);
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
