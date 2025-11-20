package com.not_found.financial_planner_api.entity;

import com.not_found.financial_planner_api.model.Account;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity {
    
    @Id
    @Column(name = "account_number", length = 64)
    private String accountNumber;
    
    @Column(name = "user_id", length = 64, nullable = false)
    private String userId;
    
    @Column(name = "card_id", length = 64)
    private String cardId;
    
    @Column(name = "account_name", length = 120, nullable = false)
    private String accountName;
    
    @Column(name = "account_type", length = 40, nullable = false)
    private String accountType;

    @Column(name = "account_amount", length = 40, nullable = false)
    private String accountAmount;
    
    @Column(name = "institution", length = 120)
    private String institution;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", referencedColumnName = "card_id", insertable = false, updatable = false)
    private CreditCard creditCard;

    public Double getBalance() {
        return Double.valueOf(accountAmount);
    }

    public String getSubscriptionPlan(){
        Account account = new Account();
        if (account.getSubscriptionPlan().equals("Premium")) {
            return "Premium";
        } else {
            return "Basic";
        }
    
}
}