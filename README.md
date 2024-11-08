# Library Management System (LMS)

## Introduction
The Library Management System (LMS) is a web application designed to manage the operations of a library. It provides functionalities to handle the borrowing and returning of books, manage customer information, and track transaction history. This system is built using Java and integrates with Spring Boot, Jakarta EE, and other technologies to deliver a robust and scalable solution.

## Features
- Retrieve all users in the database.
- Retrieve all available books in the library.
- Borrow books by customers.
- Return borrowed books.
- Get the list of books currently borrowed by a customer.
- View the borrowing history of a customer.

## Technologies Used
- **Java 17**
- **Spring Boot** (with MVC, JDBC, and Transaction support)
- **Jakarta EE**
- **Lombok** for reducing boilerplate code
- **PostgreSQL Database**

## Project Structure
- **Controller Layer**: Handles incoming HTTP requests and interacts with the service layer.
- **Service Layer**: Contains business logic and transaction management.
- **Repository Layer**: Data access logic using Spring's `JdbcTemplate` and `NamedParameterJdbcTemplate`.
- **Model Layer**: Entity definitions.
- **DTOs (Data Transfer Objects)**: Used for transferring data between different layers of the application.

## How to Run the Project
1. **Clone the repository**:
    ```bash
    git clone https://github.com/your-username/lms.git
    cd library-management-system
    ```

2. **Build the project**:
    ```bash
    ./mvnw clean install
    ```

3. **Run the application**:
    ```bash
    ./mvnw spring-boot:run
    ```

4. **Access the application** at `http://localhost:8080`.

## Endpoints
- **GET** `/api/users`: Retrieves a list of all users.
- **GET** `/api/books`: Retrieves a list of all available books.
- **POST** `/api/books/borrow`: Borrows a book for a customer.
- **POST** `/api/books/return`: Returns a borrowed book.
- **GET** `/api/users/:id/borrowed-books` : Retrieves the current books borrowed by a customer.
- **GET** `/api/users/:id/history`: Gets the borrowing history of a customer.

## Example Requests

### Borrow a Book
```json
POST /api/books/borrow
{
  "customerId": "customer-uuid-here",
  "bookId": "book-uuid-here"
}
```

### Return a Book
```json
POST /api/books/return
{
  "customerId": "customer-uuid-here",
  "bookId": "book-uuid-here"
}
```

## Contributing
Contributions are welcome! Please read the [contribution guidelines](CONTRIBUTING.md) first.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact
For any questions or suggestions, please contact [your-email@example.com](mailto:your-email@example.com).