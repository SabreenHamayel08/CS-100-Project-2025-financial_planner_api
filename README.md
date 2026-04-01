# Financial Planner API

A Spring Boot REST API for a financial spending tracker dashboard application. This API provides endpoints to manage user accounts, transactions, subscriptions, credit cards, merchants, and analytics for personal financial planning.

## Features

- **User Management**: Handle user accounts and customer information.
- **Transaction Tracking**: Record and retrieve financial transactions with sorting and search capabilities.
- **Account Management**: Manage bank accounts, credit cards, and subscriptions.
- **Analytics**: Generate dashboard data, monthly spending reports, and transaction patterns.
- **Rewards Analysis**: Calculate projected rewards and returns for accounts.
- **Merchant Categorization**: Categorize transactions by merchants.

## Technologies Used

- **Java 21**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 Database** (in-memory for development)
- **Maven** for build management
- **Lombok** for reducing boilerplate code

## Prerequisites

- Java 21 or higher
- Maven 3.6+

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/financial_planner_api.git
   cd financial_planner_api
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The API will be available at `http://localhost:8080`.

## API Endpoints

### Accounts
- `GET /api/accounts` - Get all accounts
- `GET /api/accounts/subscription/{plan}` - Get accounts by subscription plan
- `GET /api/accounts/customerinfo/{name}` - Get customer information by name
- `GET /api/accounts/{id}/transactions` - Get transactions for a specific account
- `GET /api/accounts/{id}/transactions/by-date/asc` - Get transactions sorted by date ascending
- `GET /api/accounts/{id}/transactions/by-date/desc` - Get transactions sorted by date descending
- `GET /api/accounts/{id}/transactions/by-amount/asc` - Get transactions sorted by amount ascending
- `GET /api/accounts/{id}/transactions/by-amount/desc` - Get transactions sorted by amount descending
- `GET /api/accounts/{id}/transactions/by-description/asc` - Get transactions sorted by description ascending
- `GET /api/accounts/{id}/transactions/by-description/desc` - Get transactions sorted by description descending

### Transactions
- `GET /api/transactions` - Get all transactions
- `GET /api/transactions/by-date/asc` - Get transactions sorted by date ascending
- `GET /api/transactions/by-date/desc` - Get transactions sorted by date descending
- `GET /api/transactions/by-amount/asc` - Get transactions sorted by amount ascending
- `GET /api/transactions/by-amount/desc` - Get transactions sorted by amount descending
- `GET /api/transactions/by-description/asc` - Get transactions sorted by description ascending
- `GET /api/transactions/by-description/desc` - Get transactions sorted by description descending
- `GET /api/transactions/by-account/asc` - Get transactions sorted by account ascending
- `GET /api/transactions/by-account/desc` - Get transactions sorted by account descending
- `GET /api/transactions/search?query={query}` - Search transactions by description

### Dashboard
- `GET /api/dashboard` - Get dashboard data including accounts, transactions, and analytics

### Rewards
- `GET /api/rewards/accounts/{id}` - Get rewards analysis for a specific account

## Database

The application uses an H2 in-memory database for development. Schema and sample data are loaded from `src/main/resources/schema.sql` and `src/main/resources/data.sql`.

## Configuration

Configuration is managed in `src/main/resources/application.properties`. Key settings include:
- Server port: 8080
- Database: H2 in-memory
- Logging: DEBUG level for the application package

## Testing

Run tests with:
```bash
mvn test
```

## Contributing

1. Fork the repository.
2. Create a feature branch.
3. Commit your changes.
4. Push to the branch.
5. Open a Pull Request.
