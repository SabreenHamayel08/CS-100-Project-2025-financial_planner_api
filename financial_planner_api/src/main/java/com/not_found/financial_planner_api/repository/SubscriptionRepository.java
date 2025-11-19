package com.not_found.financial_planner_api.repository;

import com.not_found.financial_planner_api.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, String> {
}