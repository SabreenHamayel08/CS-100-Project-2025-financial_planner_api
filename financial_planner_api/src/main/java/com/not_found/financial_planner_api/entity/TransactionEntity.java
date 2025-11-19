package com.not_found.financial_planner_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {
    
    @Id
    @Column(name = "transaction_id", length = 64)
    private String transactionId;
    
    @Column(name = "account_number", length = 64, nullable = false)
    private String accountNumber;
    
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;
    
    @Column(name = "description", length = 255)
    private String description;
    
    @Column(name = "transaction_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal transactionAmount;
    
    @Column(name = "transaction_category", length = 80)
    private String transactionCategory;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_number", referencedColumnName = "account_number", insertable = false, updatable = false)
    private AccountEntity account;
}