package com.not_found.financial_planner_api.model;

import lombok.Getter;
import lombok.Setter;
import java.util.Map;
import java.util.List;

/**
 * Represents a credit card with its reward program details
 */
@Getter
@Setter
public class RewardCard {
    /** Unique identifier for the card */
    private String id;
    
    /** Name of the credit card */
    private String cardName;
    
    /** Card issuer (e.g., "Chase", "American Express") */
    private String issuer;
    
    /** Annual fee for the card */
    private double annualFee;
    
    /** Base reward rate for non-category spending (as a decimal) */
    private double baseRewardRate;
    
    /** Category-specific reward rates (e.g., "dining": 0.03 for 3%) */
    private Map<String, Double> categoryRewardRates;
    
    /** Sign-up bonus details */
    private String signupBonus;
    
    /** List of additional card benefits */
    private List<String> benefits;

    /** Current APR range */
    private String apr;
}