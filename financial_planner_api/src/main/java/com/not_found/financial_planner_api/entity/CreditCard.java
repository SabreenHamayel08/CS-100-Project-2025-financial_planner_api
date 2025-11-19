package com.not_found.financial_planner_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "credit_card")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard {
    
    @Id
    @Column(name = "card_id", length = 64)
    private String cardId;
    
    @Column(name = "card_name", length = 120, nullable = false)
    private String cardName;
    
    @Column(name = "issuer", length = 80, nullable = false)
    private String issuer;
    
    @Column(name = "card_network", length = 40)
    private String cardNetwork;
    
    @Column(name = "reward_rate_dining", precision = 5, scale = 2)
    private BigDecimal rewardRateDining;
    
    @Column(name = "reward_rate_gas", precision = 5, scale = 2)
    private BigDecimal rewardRateGas;
    
    @Column(name = "reward_rate_groceries", precision = 5, scale = 2)
    private BigDecimal rewardRateGroceries;
    
    @Column(name = "reward_rate_entertainment", precision = 5, scale = 2)
    private BigDecimal rewardRateEntertainment;
    
    @Column(name = "reward_rate_travel", precision = 5, scale = 2)
    private BigDecimal rewardRateTravel;
}