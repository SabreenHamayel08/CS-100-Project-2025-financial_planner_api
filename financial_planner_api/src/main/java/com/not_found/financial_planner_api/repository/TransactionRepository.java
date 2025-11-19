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
}