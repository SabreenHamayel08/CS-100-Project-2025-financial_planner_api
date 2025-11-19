package com.not_found.financial_planner_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @Column(name = "user_id", length = 64)
    private String userId;
    
    @Column(name = "name", length = 120, nullable = false)
    private String name;
    
    @Column(name = "email", length = 160, nullable = false)
    private String email;
    
    @Column(name = "subscription_id", length = 64)
    private String subscriptionId;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", referencedColumnName = "subscription_id", insertable = false, updatable = false)
    private Subscription subscription;
}