package com.not_found.financial_planner_api.model;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Map;

/**
 * Response object for rewards analysis and recommendations
 */
@Getter
@Setter
public class RewardsAnalysis {
    /** Current rewards summary across all cards */
    private RewardsSummary currentRewards;
    
    /** List of recommended cards based on spending patterns */
    private List<CardRecommendation> recommendedCards;
    
    /** List of merchant-specific offers and optimal cards to use */
    private List<MerchantRecommendation> merchantRecommendations;

    @Getter
    @Setter
    public static class RewardsSummary {
        private double totalPointsEarned;
        private double estimatedCashValue;
        private Map<String, Double> pointsByCategory;
        private List<String> missedOpportunities;
    }

    @Getter
    @Setter
    public static class CardRecommendation {
        private RewardCard card;
        private double projectedAnnualRewards;
        private Map<String, Double> rewardsByCategory;
        private String recommendationReason;
    }

    @Getter
    @Setter
    public static class MerchantRecommendation {
        private String merchantName;
        private String bestCard;
        private double rewardRate;
        private String currentOffer;
        private String category;
    }
}