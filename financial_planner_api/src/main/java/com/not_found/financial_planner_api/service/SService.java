package com.not_found.financial_planner_api.service;
 
import com.not_found.financial_planner_api.data.SampleData;
import com.not_found.financial_planner_api.model.Account;
import com.not_found.financial_planner_api.model.Transaction;
import com.not_found.financial_planner_api.repository.AccountRepository;
import com.not_found.financial_planner_api.repository.TransactionRepository;
import com.not_found.financial_planner_api.entity.AccountEntity;
import com.not_found.financial_planner_api.entity.TransactionEntity;
 
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
 
/**
* Service layer for the Financial Planner application.
* Handles business logic, data operations, and transaction categorization.
* Now pulls data from H2 database via JPA repositories.
*/
@Service
public class SService {
    
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionCategorizationService categorizationService;
 
    /**
     * Constructor with dependency injection for repositories
     */
    public SService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.categorizationService = new TransactionCategorizationService(SampleData.getMerchantCategories());
    }
 
    /**
     * Get all accounts from database
     * Maps database entities to model objects for backward compatibility
     */
    public List<Account> getAccounts() {
        List<AccountEntity> entities = accountRepository.findAll();
        return entities.stream()
                .map(entity -> {
                    Account account = new Account();
                    // Account.id is now String, matches AccountEntity.accountNumber
                    account.setId(entity.getAccountNumber());
                    account.setAccountName(entity.getAccountName());
                    account.setCustomerName(entity.getAccountName());
                    // Other Account fields (customerName, balance, etc.) are not yet stored in the DB
                    return account;
                })
                .collect(Collectors.toList());
    }
 
    /**
     * Get all transactions from database with optional sorting
     * @param sortBy Sorting criterion: "date", "amount", "description", or "account"
     * @param order Sort order: "asc" for ascending, "desc" for descending
     * @return Sorted list of all transactions
     */
    public List<Transaction> getTransactions(String sortBy, String order) {
        List<TransactionEntity> entities = transactionRepository.findAll();
        List<Transaction> result = mapTransactionEntitiesToModels(entities);
        sortTransactions(result, sortBy, order);
        return result;
    }
 
    /**
     * Get all transactions in default order
     * @return List of all transactions
     */
    public List<Transaction> getTransactions() {
        return getTransactions(null, null);
    }
 
    /**
     * Get transactions for a specific account from database with optional sorting
     * @param accountId Account ID to filter transactions
     * @param sortBy Sorting criterion: "date", "amount", "description", or "account"
     * @param order Sort order: "asc" for ascending, "desc" for descending
     * @return Sorted list of transactions for the specified account
     */
    public List<Transaction> getTransactionsForAccount(String accountId, String sortBy, String order) {
        // accountId is already String format
        String accountNumber = accountId;
        List<TransactionEntity> entities = transactionRepository.findByAccountNumber(accountNumber);
        List<Transaction> filtered = mapTransactionEntitiesToModels(entities);
        sortTransactions(filtered, sortBy, order);
        return filtered;
    }
 
    public List<Transaction> getTransactionsForAccount(String accountId) {
        return getTransactionsForAccount(accountId, null, null);
    }
 
    /**
     * Sort a list of transactions based on specified criteria and order
     * @param txs List of transactions to sort
     * @param sortBy Sorting criterion: "date", "amount", "description", or "account"
     * @param order Sort order: "asc" for ascending, "desc" for descending
     */
    private void sortTransactions(List<Transaction> txs, String sortBy, String order) {
        if (sortBy == null) return;
 
        boolean ascending = order == null || order.equalsIgnoreCase("asc");
        
        switch (sortBy.toLowerCase()) {
            case "date":
                // Sort by transaction date
                txs.sort((a, b) -> ascending ?
                    a.getDate().compareTo(b.getDate()) :
                    b.getDate().compareTo(a.getDate()));
                break;
            case "amount":
                // Sort by transaction amount
                txs.sort((a, b) -> ascending ?
                    Double.compare(a.getAmount(), b.getAmount()) :
                    Double.compare(b.getAmount(), a.getAmount()));
                break;
            case "description":
                // Sort by transaction description alphabetically
                txs.sort((a, b) -> ascending ?
                    compareDescriptions(a, b) :
                    compareDescriptions(b, a));
                break;
            default:
                // No sorting if invalid sort parameter
                break;
        }
    }
 
    private int compareDescriptions(Transaction a, Transaction b) {
        String descA = a.getDescription() == null ? "" : a.getDescription();
        String descB = b.getDescription() == null ? "" : b.getDescription();
        return descA.compareToIgnoreCase(descB);
    }
 
    public List<Transaction> getRecentTransactions(int limit) {
        List<TransactionEntity> entities = transactionRepository.findAll();
        List<Transaction> allTransactions = mapTransactionEntitiesToModels(entities);
        return allTransactions.stream()
                .sorted((a,b) -> b.getDate().compareTo(a.getDate()))
                .limit(limit)
                .toList();
    }
 
    public List<Transaction> getRecentTransactionsForAccount(String accountId, int limit) {
        String accountNumber = accountId;
        List<TransactionEntity> entities = transactionRepository.findByAccountNumberOrderByDateDesc(accountNumber);
        List<Transaction> transactions = mapTransactionEntitiesToModels(entities);
        return transactions.stream()
                .limit(limit)
                .toList();
    }
 
    public Map<String, Double> getExpenseBreakdown() {
        return categorize(getTransactions());
    }
 
    public Map<String, Double> getExpenseBreakdownForAccount(String accountId) {
        List<Transaction> tx = getTransactionsForAccount(accountId);
        return categorize(tx);
    }
 
    /**
     * Search transactions by description or date range from database
     * @param query Search query for description (case-insensitive)
     * @param startDate Optional start date in YYYY-MM-DD format
     * @param endDate Optional end date in YYYY-MM-DD format
     * @param accountId Optional account ID (alphanumeric) to filter transactions
     * @return List of matching transactions
     */
    public List<Transaction> searchTransactions(String query, String startDate, String endDate, String accountId) {
        List<TransactionEntity> entities;
        
        // Get transactions from database
        if (accountId != null) {
            entities = transactionRepository.findByAccountNumber(accountId);
        } else {
            entities = transactionRepository.findAll();
        }
        
        List<Transaction> result = mapTransactionEntitiesToModels(entities);
 
        // Filter by description if query is provided
        if (query != null && !query.trim().isEmpty()) {
            String searchQuery = query.trim().toLowerCase();
            result = result.stream()
                    .filter(t -> t.getDescription() != null &&
                               t.getDescription().toLowerCase().contains(searchQuery))
                    .collect(Collectors.toList());
        }
 
        // Filter by date range if provided
        if (startDate != null || endDate != null) {
            LocalDate start = startDate != null ? LocalDate.parse(startDate) : LocalDate.MIN;
            LocalDate end = endDate != null ? LocalDate.parse(endDate) : LocalDate.MAX;
 
            result = result.stream()
                    .filter(t -> !t.getDate().isBefore(start) && !t.getDate().isAfter(end))
                    .collect(Collectors.toList());
        }
 
        // Sort results by date (newest first) by default
        result.sort((a, b) -> b.getDate().compareTo(a.getDate()));
        
        return result;
    }
 
    private Map<String, Double> categorize(List<Transaction> txs) {
        return categorizationService.categorizeTransactions(txs);
    }
    
    /**
     * Maps database TransactionEntity objects to Transaction model objects
     * for backward compatibility with existing controllers
     */
    private List<Transaction> mapTransactionEntitiesToModels(List<TransactionEntity> entities) {
        return entities.stream()
                .map(entity -> {
                    Transaction transaction = new Transaction();
                    transaction.setId((entity.getTransactionId()));
                    transaction.setDate(entity.getTransactionDate());
                    transaction.setDescription(entity.getDescription());
                    transaction.setAmount(entity.getTransactionAmount().doubleValue());
                    transaction.setCategory(entity.getTransactionCategory());
                    return transaction;
                })
                .collect(Collectors.toList());
    }
    
}
 