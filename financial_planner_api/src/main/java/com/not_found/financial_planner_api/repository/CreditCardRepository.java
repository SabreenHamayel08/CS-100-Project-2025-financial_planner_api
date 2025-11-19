package com.not_found.financial_planner_api.repository;

import com.not_found.financial_planner_api.entity.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, String> {
    List<CreditCard> findByIssuer(String issuer);
}