package com.not_found.financial_planner_api.model;

import java.util.List;
import java.util.Map;

public class DashboardResponse {
    private List<Transaction> recent;
    private Map<String, Double> breakdown;

    public DashboardResponse() {}

    public DashboardResponse(List<Transaction> recent, Map<String, Double> breakdown) {
        this.recent = recent;
        this.breakdown = breakdown;
    }

    public List<Transaction> getRecent() {
        return recent;
    }

    public void setRecent(List<Transaction> recent) {
        this.recent = recent;
    }

    public Map<String, Double> getBreakdown() {
        return breakdown;
    }

    public void setBreakdown(Map<String, Double> breakdown) {
        this.breakdown = breakdown;
    }
}
