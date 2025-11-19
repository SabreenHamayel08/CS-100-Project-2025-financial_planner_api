package com.not_found.financial_planner_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "subscription")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {
    
    @Id
    @Column(name = "subscription_id", length = 64)
    private String subscriptionId;
    
    @Column(name = "plan_name", length = 50, nullable = false)
    private String planName;
    
    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price = BigDecimal.ZERO;
    
    @Column(name = "billing_cycle", length = 20, nullable = false)
    private String billingCycle;
}