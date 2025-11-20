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
    List<Object[]> getMonthlyIncomeTotals();

@Query("SELECT FUNCTION('DATE_FORMAT', t.date, '%Y-%m'), SUM(t.amount) " +
       "FROM Transaction t " +
       "WHERE t.type = 'INCOME' " +
       "GROUP BY FUNCTION('DATE_FORMAT', t.date, '%Y-%m') " +
       "ORDER BY FUNCTION('DATE_FORMAT', t.date, '%Y-%m')")
List<Object[]> getMonthlyTotals();

//     // ---- INCOME ANALYSIS FOR PAST YEAR ----
//     @Query(" SELECT FUNCTION('DATE_FORMAT', t.transactionDate, '%b') AS month, SUM(t.transactionAmount) " +
//         "FROM TransactionEntity t WHERE t.transactionAmount > 0 " +
//         "AND t.transactionDate >= FUNCTION('DATE_SUB', CURRENT_DATE, 'INTERVAL 1 YEAR') " + 
//         "GROUP BY month " +
//         "ORDER BY FUNCTION('MONTH', t.transactionDate) ")
//     List<Object[]> getIncomePastYear();
}
