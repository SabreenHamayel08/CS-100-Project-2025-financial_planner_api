package com.not_found.financial_planner_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "merchant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Merchant {
    
    @Id
    @Column(name = "merchant_id", length = 64)
    private String merchantId;
    
    @Column(name = "merchant_name", length = 160, nullable = false)
    private String merchantName;
    
    @Column(name = "merchant_category", length = 80)
    private String merchantCategory;
}