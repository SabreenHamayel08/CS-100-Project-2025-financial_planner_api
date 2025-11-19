package com.not_found.financial_planner_api.repository;

import com.not_found.financial_planner_api.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, String> {
    Optional<Merchant> findByMerchantName(String merchantName);
    List<Merchant> findByMerchantCategory(String category);
}