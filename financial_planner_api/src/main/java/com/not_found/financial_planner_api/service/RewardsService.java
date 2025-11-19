package com.not_found.financial_planner_api.service;

import com.not_found.financial_planner_api.model.*;
import org.springframework.stereotype.Service;
import java.util.*;
import java.time.LocalDate;

/**
 * Service for analyzing spending patterns and providing rewards recommendations
 */
@Service
public class RewardsService {
    private final Map<String, RewardCard> availableCards;
    private final SService transactionService;

    public RewardsService(SService transactionService) {
        this.transactionService = transactionService;
        this.availableCards = initializeAvailableCards();
    }

    /**
     * Generate rewards analysis for a list of transactions
     * @param transactions List of transactions to analyze
     * @return Rewards analysis and recommendations
     */
    public RewardsAnalysis analyzeRewards(List<Transaction> transactions) {
        RewardsAnalysis analysis = new RewardsAnalysis();
        
        // Filter to only include last year's transactions
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        List<Transaction> recentTransactions = transactions.stream()
            .filter(tx -> tx.getDate() != null && !tx.getDate().isBefore(oneYearAgo))
            .toList();
        
        // Calculate current rewards
        RewardsAnalysis.RewardsSummary summary = calculateCurrentRewards(recentTransactions);
        analysis.setCurrentRewards(summary);
        
        // Generate card recommendations
        List<RewardsAnalysis.CardRecommendation> recommendations = 
            generateCardRecommendations(recentTransactions);
        analysis.setRecommendedCards(recommendations);
        
        // Generate merchant-specific recommendations
        List<RewardsAnalysis.MerchantRecommendation> merchantRecs = 
            generateMerchantRecommendations(recentTransactions);
        analysis.setMerchantRecommendations(merchantRecs);
        
        return analysis;
    }

    /**
     * Generate rewards analysis for a user ID
     * @param userId User ID to analyze
     * @return Rewards analysis and recommendations
     */
    public RewardsAnalysis analyzeRewards(long userId) {
        List<Transaction> transactions = getLastYearTransactions(userId);
        return analyzeRewards(transactions);
    }

    private Map<String, RewardCard> initializeAvailableCards() {
        Map<String, RewardCard> cards = new HashMap<>();
        
        // Chase Sapphire Preferred
        RewardCard csp = new RewardCard();
        csp.setId("csp");
        csp.setCardName("Chase Sapphire Preferred");
        csp.setIssuer("Chase");
        csp.setAnnualFee(95.0);
        csp.setBaseRewardRate(0.01);
        Map<String, Double> cspRates = new HashMap<>();
        cspRates.put("dining", 0.03);
        cspRates.put("travel", 0.02);
        cspRates.put("streaming", 0.03);
        csp.setCategoryRewardRates(cspRates);
        csp.setSignupBonus("60,000 points after spending $4,000 in first 3 months");
        cards.put(csp.getId(), csp);

        // American Express Blue Cash Preferred
        RewardCard bcp = new RewardCard();
        bcp.setId("bcp");
        bcp.setCardName("Blue Cash Preferred");
        bcp.setIssuer("American Express");
        bcp.setAnnualFee(95.0);
        bcp.setBaseRewardRate(0.01);
        Map<String, Double> bcpRates = new HashMap<>();
        bcpRates.put("groceries", 0.06);
        bcpRates.put("streaming", 0.06);
        bcpRates.put("transit", 0.03);
        bcpRates.put("gas", 0.03);
        bcp.setCategoryRewardRates(bcpRates);
        bcp.setSignupBonus("$350 back after spending $3,000 in first 6 months");
        cards.put(bcp.getId(), bcp);

        // Add more cards...
        return cards;
    }

    private List<Transaction> getLastYearTransactions(long userId) {
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        return transactionService.searchTransactions(
            null, 
            oneYearAgo.toString(), 
            LocalDate.now().toString(),
            userId
        );
    }

    @SuppressWarnings("null")
    private RewardsAnalysis.RewardsSummary calculateCurrentRewards(List<Transaction> transactions) {
        RewardsAnalysis.RewardsSummary summary = new RewardsAnalysis.RewardsSummary();
        Map<String, Double> pointsByCategory = new HashMap<>();
        double totalPoints = 0;
        
        for (Transaction tx : transactions) {
            if (tx.getAmount() >= 0) continue; // Skip deposits
            
            String category = determineCategory(tx);
            double amount = Math.abs(tx.getAmount());
            double points = calculatePoints(amount, category);
            
            pointsByCategory.merge(category, points, Double::sum);
            totalPoints += points;
        }
        
        summary.setTotalPointsEarned(totalPoints);
        summary.setEstimatedCashValue(totalPoints * 0.01); // Assuming 1 cent per point
        summary.setPointsByCategory(pointsByCategory);
        
        return summary;
    }

    private List<RewardsAnalysis.CardRecommendation> generateCardRecommendations(
            List<Transaction> transactions) {
        List<RewardsAnalysis.CardRecommendation> recommendations = new ArrayList<>();
        
        for (RewardCard card : availableCards.values()) {
            RewardsAnalysis.CardRecommendation rec = new RewardsAnalysis.CardRecommendation();
            rec.setCard(card);
            
            // Calculate projected rewards
            double projectedRewards = calculateProjectedRewards(transactions, card);
            rec.setProjectedAnnualRewards(projectedRewards);
            
            // Calculate rewards by category
            Map<String, Double> rewardsByCategory = calculateRewardsByCategory(transactions, card);
            rec.setRewardsByCategory(rewardsByCategory);
            
            // Generate recommendation reason
            String reason = generateRecommendationReason(card, projectedRewards, rewardsByCategory);
            rec.setRecommendationReason(reason);
            
            recommendations.add(rec);
        }
        
        // Sort by projected rewards (highest first)
        recommendations.sort((a, b) -> 
            Double.compare(b.getProjectedAnnualRewards(), a.getProjectedAnnualRewards()));
        
        return recommendations;
    }

    private List<RewardsAnalysis.MerchantRecommendation> generateMerchantRecommendations(
            List<Transaction> transactions) {
        // Group transactions by merchant
        Map<String, List<Transaction>> byMerchant = new HashMap<>();
        for (Transaction tx : transactions) {
            if (tx.getDescription() == null) continue;
            byMerchant.computeIfAbsent(tx.getDescription(), k -> new ArrayList<>())
                     .add(tx);
        }
        
        List<RewardsAnalysis.MerchantRecommendation> recommendations = new ArrayList<>();
        
        for (Map.Entry<String, List<Transaction>> entry : byMerchant.entrySet()) {
            String merchant = entry.getKey();
            List<Transaction> merchantTxs = entry.getValue();
            
            if (merchantTxs.size() < 2) continue; // Skip one-time merchants
            
            RewardsAnalysis.MerchantRecommendation rec = new RewardsAnalysis.MerchantRecommendation();
            rec.setMerchantName(merchant);
            
            // Find best card for this merchant
            String category = determineCategory(merchantTxs.get(0));
            RewardCard bestCard = findBestCard(category);
            
            rec.setBestCard(bestCard.getCardName());
            rec.setRewardRate(getBestRewardRate(bestCard, category));
            rec.setCategory(category);
            rec.setCurrentOffer(getCurrentOffer(merchant));
            
            recommendations.add(rec);
        }
        
        // Sort by reward rate (highest first)
        recommendations.sort((a, b) -> Double.compare(b.getRewardRate(), a.getRewardRate()));
        
        return recommendations;
    }

    public String determineCategory(Transaction transaction) {
        // Simple heuristic based on description keywords
        String desc = transaction.getDescription().toLowerCase();
        if (desc.contains("grocery") || desc.contains("supermarket") || desc.contains("coffee")) {
            return "groceries";
        } else if (desc.contains("restaurant") || desc.contains("dining") || desc.contains("cafe")) {
            return "dining";
        } else if (desc.contains("uber") || desc.contains("lyft") || desc.contains("taxi") || desc.contains("transit")) {
            return "transportation";
        } else if (desc.contains("flight") || desc.contains("hotel") || desc.contains("travel")) {
            return "travel";
        } else if (desc.contains("netflix") || desc.contains("spotify") || desc.contains("streaming")) {
            return "streaming";
        } else if (desc.contains("gas") || desc.contains("fuel")) {
            return "gas";
        }
        return "other";
    }
        

    private double calculatePoints(double amount, String category) {
        // Simple example - can be made more sophisticated
        switch (category) {
            case "dining":
            case "travel":
                return amount * 2; // 2x points
            case "groceries":
                return amount * 3; // 3x points
            default:
                return amount; // 1x points
        }
    }

    private double calculateProjectedRewards(List<Transaction> transactions, RewardCard card) {
        return transactions.stream()
                .filter(tx -> tx.getAmount() < 0)
                .mapToDouble(tx -> {
                    String category = determineCategory(tx);
                    double amount = Math.abs(tx.getAmount());
                    double rate = card.getCategoryRewardRates().getOrDefault(category, 
                                    card.getBaseRewardRate());
                    return amount * rate;
                })
                .sum();
    }

    
    @SuppressWarnings("null")
    private Map<String, Double> calculateRewardsByCategory(List<Transaction> transactions, 
            RewardCard card) {
        Map<String, Double> rewards = new HashMap<>();
        
        for (Transaction tx : transactions) {
            if (tx.getAmount() >= 0) continue;
            
            String category = determineCategory(tx);
            double amount = Math.abs(tx.getAmount());
            double rate = card.getCategoryRewardRates().getOrDefault(category, 
                            card.getBaseRewardRate());
            double reward = amount * rate;
            
            rewards.merge(category, reward, Double::sum);
        }
        
        return rewards;
    }

    private String generateRecommendationReason(RewardCard card, double projectedRewards,
            Map<String, Double> rewardsByCategory) {
        String topCategory = rewardsByCategory.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
                
        if (topCategory == null) return "Good for general spending";
        
        return String.format("Best for %s spending with %.1f%% back. Projected annual rewards: $%.2f",
                topCategory,
                card.getCategoryRewardRates().getOrDefault(topCategory, card.getBaseRewardRate()) * 100,
                projectedRewards);
    }

    private RewardCard findBestCard(String category) {
        return availableCards.values().stream()
                .max((a, b) -> Double.compare(
                    a.getCategoryRewardRates().getOrDefault(category, a.getBaseRewardRate()),
                    b.getCategoryRewardRates().getOrDefault(category, b.getBaseRewardRate())))
                .orElse(availableCards.values().iterator().next());
    }

    private double getBestRewardRate(RewardCard card, String category) {
        return card.getCategoryRewardRates().getOrDefault(category, card.getBaseRewardRate());
    }

    private String getCurrentOffer(String merchant) {
        // This could be expanded to include real-time offers from card issuers
        // For now, returning a sample offer
        if (merchant.toLowerCase().contains("grocery")) {
            return "Extra 2% back on groceries this month";
        }
        return null;
    }
}