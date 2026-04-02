package com.not_found.financial_planner_api.repository;

import com.not_found.financial_planner_api.entity.TransactionEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {

    List<TransactionEntity> findByAccountNumber(String accountNumber);
    List<TransactionEntity> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate);
    List<TransactionEntity> findByTransactionCategory(String category);

    @Query("SELECT t FROM TransactionEntity t WHERE t.accountNumber = :accountNumber ORDER BY t.transactionDate DESC")
    List<TransactionEntity> findByAccountNumberOrderByDateDesc(String accountNumber);

    // ---- EXPENSE CATEGORY PIE CHART ----
    @Query("""
        SELECT t.transactionCategory, SUM(t.transactionAmount)
        FROM TransactionEntity t
        WHERE t.transactionAmount < 0
        GROUP BY t.transactionCategory
    """)
    List<Object[]> getExpenseTotalsByCategory();

    // ---- PREDICT INCOME TREND ----
    @Query("""
        SELECT FUNCTION('MONTH', t.transactionDate) AS month, SUM(t.transactionAmount)
        FROM TransactionEntity t
        WHERE t.transactionAmount > 0
        GROUP BY FUNCTION('MONTH', t.transactionDate)
        ORDER BY month
    """)
List<Object[]> getMonthlyTotals();
    @Query("""
        SELECT FUNCTION('MONTH',t.transactionDate) AS month, SUM(t.transactionAmount)
        FROM TransactionEntity t
        WHERE t.transactionAmount > 0 AND t.accountNumber = :id
        GROUP BY FUNCTION('MONTH', t.transactionDate)
        """)
    List<Object[]> getMonthlyTotalsForAccount(String id);
    @Query("""
        SELECT t.transactionCategory, SUM(t.transactionAmount)
        FROM TransactionEntity t
        WHERE t.transactionAmount < 0 AND t.accountNumber = :id
        GROUP BY t.transactionCategory
    """)
    List<Object[]> getExpenseTotalsByCategoryForAccount(String id); 
    @Query("SELECT new com.not_found.financial_planner_api.model.Account(t.accountNumber, t.transactionDate, t.transactionAmount, t.transactionCategory) FROM TransactionEntity t WHERE t.accountNumber = :id")
    List<Object[]> getTransactionsForAccount(String id);
}
