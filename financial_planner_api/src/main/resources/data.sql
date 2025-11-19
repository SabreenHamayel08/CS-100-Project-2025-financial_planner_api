-- Insert Subscription Plans
INSERT INTO subscription (subscription_id, plan_name, price, billing_cycle) VALUES
('sub-001', 'Free', 0.00, 'monthly'),
('sub-002', 'Basic', 9.99, 'monthly'),
('sub-003', 'Premium', 19.99, 'monthly'),
('sub-004', 'Enterprise', 49.99, 'monthly');

-- Insert Users
INSERT INTO users (user_id, name, email, subscription_id, created_at) VALUES
('user-001', 'John Doe', 'john.doe@email.com', 'sub-003', '2024-01-15 10:00:00'),
('user-002', 'Jane Smith', 'jane.smith@email.com', 'sub-002', '2024-02-20 14:30:00'),
('user-003', 'Mike Johnson', 'mike.johnson@email.com', 'sub-003', '2024-03-10 09:15:00'),
('user-004', 'Sarah Williams', 'sarah.williams@email.com', 'sub-001', '2024-04-05 16:45:00');

-- Insert Credit Cards
INSERT INTO credit_card (card_id, card_name, issuer, card_network, reward_rate_dining, reward_rate_gas, reward_rate_groceries, reward_rate_entertainment, reward_rate_travel) VALUES
('card-001', 'Chase Sapphire Preferred', 'Chase', 'Visa', 2.00, 1.00, 1.00, 2.00, 2.00),
('card-002', 'American Express Gold', 'American Express', 'Amex', 4.00, 1.00, 4.00, 1.00, 3.00),
('card-003', 'Citi Double Cash', 'Citibank', 'Mastercard', 2.00, 2.00, 2.00, 2.00, 2.00),
('card-004', 'Capital One Venture', 'Capital One', 'Visa', 2.00, 2.00, 2.00, 2.00, 5.00),
('card-005', 'Discover It Cash Back', 'Discover', 'Discover', 1.00, 1.00, 5.00, 1.00, 1.00);

-- Insert Accounts
INSERT INTO account (account_number, user_id, card_id, account_name, account_type, account_amount, institution) VALUES
('acc-001', 'user-001', 'card-001', 'Chase Checking', 'checking', '5000.00', 'Chase Bank'),
('acc-002', 'user-001', 'card-001', 'Chase Sapphire Card', 'credit_card', '0.00', 'Chase Bank'),
('acc-003', 'user-002', 'card-002', 'Amex Gold Card', 'credit_card', '0.00', 'American Express'),
('acc-004', 'user-002', NULL, 'Bank of America Savings', 'savings', '10000.00', 'Bank of America'),
('acc-005', 'user-003', 'card-003', 'Citi Double Cash Card', 'credit_card', '0.00', 'Citibank'),
('acc-006', 'user-003', NULL, 'Wells Fargo Checking', 'checking', '7500.00', 'Wells Fargo'),
('acc-007', 'user-004', 'card-005', 'Discover Card', 'credit_card', '0.00', 'Discover');

-- Insert Transactions
INSERT INTO transactions (transaction_id, account_number, transaction_date, description, transaction_amount, transaction_category) VALUES
-- User 1 transactions
('txn-001', 'acc-001', '2024-11-01', 'Grocery Store Purchase', -125.50, 'groceries'),
('txn-002', 'acc-002', '2024-11-02', 'Restaurant - Italian Bistro', -65.00, 'dining'),
('txn-003', 'acc-001', '2024-11-03', 'Salary Deposit', 3500.00, 'income'),
('txn-004', 'acc-002', '2024-11-05', 'Gas Station', -45.00, 'gas'),
('txn-005', 'acc-002', '2024-11-07', 'Movie Theater', -35.00, 'entertainment'),
('txn-006', 'acc-001', '2024-11-10', 'Electric Bill', -120.00, 'utilities'),
('txn-007', 'acc-002', '2024-11-12', 'Flight Booking', -450.00, 'travel'),
('txn-008', 'acc-001', '2024-11-15', 'Grocery Store Purchase', -98.75, 'groceries'),

-- User 2 transactions
('txn-009', 'acc-003', '2024-11-01', 'Fine Dining Restaurant', -180.00, 'dining'),
('txn-010', 'acc-004', '2024-11-01', 'Savings Transfer', 500.00, 'transfer'),
('txn-011', 'acc-003', '2024-11-04', 'Whole Foods', -145.30, 'groceries'),
('txn-012', 'acc-003', '2024-11-06', 'Coffee Shop', -12.50, 'dining'),
('txn-013', 'acc-003', '2024-11-08', 'Hotel Booking', -320.00, 'travel'),
('txn-014', 'acc-004', '2024-11-10', 'Interest Earned', 5.25, 'income'),

-- User 3 transactions
('txn-015', 'acc-005', '2024-11-02', 'Online Shopping - Amazon', -78.99, 'shopping'),
('txn-016', 'acc-006', '2024-11-03', 'Paycheck Direct Deposit', 4200.00, 'income'),
('txn-017', 'acc-005', '2024-11-05', 'Target Purchase', -156.43, 'shopping'),
('txn-018', 'acc-005', '2024-11-08', 'Shell Gas Station', -52.00, 'gas'),
('txn-019', 'acc-006', '2024-11-10', 'Rent Payment', -1500.00, 'housing'),
('txn-020', 'acc-005', '2024-11-12', 'Restaurant - Sushi Place', -95.00, 'dining'),

-- User 4 transactions
('txn-021', 'acc-007', '2024-11-01', 'Walmart Groceries', -89.25, 'groceries'),
('txn-022', 'acc-007', '2024-11-03', 'Gas Station', -38.00, 'gas'),
('txn-023', 'acc-007', '2024-11-05', 'Pharmacy - CVS', -42.50, 'healthcare'),
('txn-024', 'acc-007', '2024-11-08', 'Fast Food', -15.75, 'dining'),
('txn-025', 'acc-007', '2024-11-12', 'Streaming Service', -14.99, 'entertainment');

-- Insert Merchants
INSERT INTO merchant (merchant_id, merchant_name, merchant_category) VALUES
('merch-001', 'Kroger', 'groceries'),
('merch-002', 'Whole Foods Market', 'groceries'),
('merch-003', 'Walmart', 'retail'),
('merch-004', 'Target', 'retail'),
('merch-005', 'Shell Gas Station', 'gas'),
('merch-006', 'Chevron', 'gas'),
('merch-007', 'Italian Bistro', 'dining'),
('merch-008', 'Sushi Restaurant', 'dining'),
('merch-009', 'Starbucks', 'dining'),
('merch-010', 'AMC Theaters', 'entertainment'),
('merch-011', 'Netflix', 'entertainment'),
('merch-012', 'Amazon', 'retail'),
('merch-013', 'Delta Airlines', 'travel'),
('merch-014', 'Marriott Hotels', 'travel'),
('merch-015', 'CVS Pharmacy', 'healthcare');