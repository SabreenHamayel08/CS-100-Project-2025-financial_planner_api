package com.not_found.financial_planner_api.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.not_found.financial_planner_api.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    @Autowired
    private final TransactionRepository transactionRepository;

    // ------------------ CATEGORY PIE CHART ------------------
    public Map<String, BigDecimal> getExpenseCategoryPieChart() {

        List<Object[]> results = transactionRepository.getExpenseTotalsByCategory();

        Map<String, BigDecimal> map = new HashMap<>();

        for (Object[] row : results) {
            String category = (String) row[0];
            BigDecimal total = (BigDecimal) row[1];
            map.put(category, total);
        }

        return map;
    }


    // ------------------ INCOME TREND PREDICTION ------------------
    public Map<String, BigDecimal> predictFutureIncome(int monthsAhead) {

        List<Object[]> results = transactionRepository.getMonthlyTotals();

        List<BigDecimal> y = new ArrayList<>();
        List<Integer> x = new ArrayList<>();

        int index = 0;
        for (Object[] row : results) {
            BigDecimal total = (BigDecimal) row[1];
            y.add(total);
            x.add(index++);
        }

        if (x.size() < 2) {
            throw new IllegalStateException("Not enough months for prediction.");
        }

        // Linear regression calculations
        BigDecimal sumX = BigDecimal.ZERO;
        BigDecimal sumY = BigDecimal.ZERO;
        BigDecimal sumXY = BigDecimal.ZERO;
        BigDecimal sumX2 = BigDecimal.ZERO;

        for (int i = 0; i < x.size(); i++) {
            BigDecimal bx = BigDecimal.valueOf(x.get(i));
            BigDecimal by = y.get(i);

            sumX = sumX.add(bx);
            sumY = sumY.add(by);
            sumXY = sumXY.add(bx.multiply(by));
            sumX2 = sumX2.add(bx.multiply(bx));
        }

        BigDecimal n = BigDecimal.valueOf(x.size());

        BigDecimal b = sumXY.subtract(sumX.multiply(sumY).divide(n, 8, RoundingMode.HALF_UP))
                .divide(sumX2.subtract(sumX.pow(2).divide(n, 8, RoundingMode.HALF_UP)),
                        8, RoundingMode.HALF_UP);

        BigDecimal a = sumY.subtract(b.multiply(sumX))
                .divide(n, 8, RoundingMode.HALF_UP);

        Map<String, BigDecimal> predictions = new LinkedHashMap<>();

        int lastIndex = x.get(x.size() - 1);

        for (int i = 1; i <= monthsAhead; i++) {
            BigDecimal xFuture = BigDecimal.valueOf(lastIndex + i);
            BigDecimal prediction = a.add(b.multiply(xFuture));
            predictions.put("Month +" + i, prediction);
        }

        return predictions;
    }

    /**
     * Predict future net income for a specific account using its monthly totals.
     */
    public Map<String, BigDecimal> predictFutureIncomeForAccount(String accountId, int monthsAhead) {
        List<Object[]> results = transactionRepository.getMonthlyTotalsForAccount(accountId);

        List<BigDecimal> y = new ArrayList<>();
        List<Integer> x = new ArrayList<>();

        int index = 0;
        for (Object[] row : results) {
            BigDecimal total = (BigDecimal) row[1];
            y.add(total);
            x.add(index++);
        }

        Map<String, BigDecimal> empty = new LinkedHashMap<>();
        if (x.size() < 2) {
            // return zeros for requested months when not enough history
            for (int i = 1; i <= monthsAhead; i++) {
                empty.put("Month +" + i, BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            }
            return empty;
        }

        // Linear regression calculations (BigDecimal)
        BigDecimal sumX = BigDecimal.ZERO;
        BigDecimal sumY = BigDecimal.ZERO;
        BigDecimal sumXY = BigDecimal.ZERO;
        BigDecimal sumX2 = BigDecimal.ZERO;

        for (int i = 0; i < x.size(); i++) {
            BigDecimal bx = BigDecimal.valueOf(x.get(i));
            BigDecimal by = y.get(i);

            sumX = sumX.add(bx);
            sumY = sumY.add(by);
            sumXY = sumXY.add(bx.multiply(by));
            sumX2 = sumX2.add(bx.multiply(bx));
        }

        BigDecimal n = BigDecimal.valueOf(x.size());

        BigDecimal b = sumXY.subtract(sumX.multiply(sumY).divide(n, 8, RoundingMode.HALF_UP))
                .divide(sumX2.subtract(sumX.pow(2).divide(n, 8, RoundingMode.HALF_UP)),
                        8, RoundingMode.HALF_UP);

        BigDecimal a = sumY.subtract(b.multiply(sumX)).divide(n, 8, RoundingMode.HALF_UP);

        Map<String, BigDecimal> predictions = new LinkedHashMap<>();
        int lastIndex = x.get(x.size() - 1);
        for (int i = 1; i <= monthsAhead; i++) {
            BigDecimal xFuture = BigDecimal.valueOf(lastIndex + i);
            BigDecimal prediction = a.add(b.multiply(xFuture)).setScale(2, RoundingMode.HALF_UP);
            predictions.put("Month +" + i, prediction);
        }

        return predictions;
    }
}