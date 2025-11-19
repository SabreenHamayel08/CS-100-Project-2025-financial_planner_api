package com.not_found.financial_planner_api.repository;

import com.not_found.financial_planner_api.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, String> {
    List<AccountEntity> findByUserId(String userId);
    List<AccountEntity> findByAccountType(String accountType);
}