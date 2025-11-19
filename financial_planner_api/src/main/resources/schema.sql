CREATE TABLE IF NOT EXISTS subscription (
    subscription_id   VARCHAR(64) PRIMARY KEY,
    plan_name         VARCHAR(50) NOT NULL,
    price             DECIMAL(10,2) DEFAULT 0.00,
    billing_cycle     VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    user_id         VARCHAR(64) PRIMARY KEY,
    name            VARCHAR(120) NOT NULL,
    email           VARCHAR(160) NOT NULL,
    subscription_id VARCHAR(64),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (subscription_id) REFERENCES subscription(subscription_id)
);

CREATE TABLE IF NOT EXISTS credit_card (
    card_id                 VARCHAR(64) PRIMARY KEY,
    card_name               VARCHAR(120) NOT NULL,
    issuer                  VARCHAR(80) NOT NULL,
    card_network            VARCHAR(40),
    reward_rate_dining      DECIMAL(5,2),
    reward_rate_gas         DECIMAL(5,2),
    reward_rate_groceries   DECIMAL(5,2),
    reward_rate_entertainment DECIMAL(5,2),
    reward_rate_travel      DECIMAL(5,2)
);

CREATE TABLE IF NOT EXISTS account (
    account_number  VARCHAR(64) PRIMARY KEY,
    user_id         VARCHAR(64) NOT NULL,
    card_id         VARCHAR(64),
    account_name    VARCHAR(120) NOT NULL,
    account_type    VARCHAR(40) NOT NULL,
    institution     VARCHAR(120),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (card_id) REFERENCES credit_card(card_id)
);

CREATE TABLE IF NOT EXISTS transactions (
    transaction_id       VARCHAR(64) PRIMARY KEY,
    account_number       VARCHAR(64) NOT NULL,
    transaction_date     DATE NOT NULL,
    description          VARCHAR(255),
    transaction_amount   DECIMAL(12,2) NOT NULL,
    transaction_category VARCHAR(80),
    FOREIGN KEY (account_number) REFERENCES account(account_number)
);

CREATE TABLE IF NOT EXISTS merchant (
    merchant_id       VARCHAR(64) PRIMARY KEY,
    merchant_name     VARCHAR(160) NOT NULL,
    merchant_category VARCHAR(80)
);