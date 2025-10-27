package com.not_found.financial_planner_api.model;

import java.util.List;

public class AllDataResponse {
    private List<Account> accounts;
    private List<Transaction> transactions;
    private DashboardResponse dashboard;

    public AllDataResponse() {}

    public AllDataResponse(List<Account> accounts, List<Transaction> transactions, DashboardResponse dashboard) {
        this.accounts = accounts;
        this.transactions = transactions;
        this.dashboard = dashboard;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public DashboardResponse getDashboard() {
        return dashboard;
    }

    public void setDashboard(DashboardResponse dashboard) {
        this.dashboard = dashboard;
    }
}
