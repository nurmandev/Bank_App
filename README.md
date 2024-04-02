# Bank App API Documentation

This document provides an overview of the endpoints available in the Bank App API along with instructions on how to run the code.

## Setup Instructions

1. Clone the repository containing the code.
2. Make sure you have Java and Maven installed on your system.
3. Navigate to the project directory.
4. Run the command `mvn spring-boot:run` to start the Spring Boot application.

## UserController

### `/api/user`

- **GET**:
    - **Description**: Check if the server is running.
    - **Functionality**: Returns a simple "Hello" message.

### `/api/user/users`

- **GET**:
    - **Description**: Retrieve all users.
    - **Functionality**: Returns a list of all users registered in the system.

### `/api/user/register`

- **POST**:
    - **Description**: Create a new user account.
    - **Functionality**: Creates a new user account based on the provided user details.

### `/api/user/login`

- **POST**:
    - **Description**: Login to user account.
    - **Functionality**: Authenticates the user based on login credentials.

### `/api/user/balanceEnquiry`

- **GET**:
    - **Description**: Check the balance of a user account.
    - **Functionality**: Retrieves the current balance of the specified user account.

### `/api/user/nameEnquiry`

- **GET**:
    - **Description**: Retrieve the name associated with a user account.
    - **Functionality**: Retrieves the name of the user associated with the specified account number.

### `/api/user/credit`

- **POST**:
    - **Description**: Deposit money to user account.
    - **Functionality**: Adds funds to the specified user account.

### `/api/user/debit`

- **POST**:
    - **Description**: Debit money from user account.
    - **Functionality**: Deducts funds from the specified user account.

### `/api/user/transfer`

- **POST**:
    - **Description**: Transfer money to another user.
    - **Functionality**: Transfers funds from one user account to another.

## TransactionController

### `/bankStatement`

- **GET**:
    - **Description**: Generate bank statement.
    - **Parameters**:
        - `accountNumber`: Account number for which the statement is to be generated.
        - `startDate`: Start date of the statement period (format: yyyy-MM-dd).
        - `endDate`: End date of the statement period (format: yyyy-MM-dd).
    - **Functionality**: Generates a bank statement for the specified account within the given date range.

