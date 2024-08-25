# Market App with Java Spring

## Project Overview
This project is a marketplace application built using Java Spring, designed to handle multiple user roles (admin, user, seller). It includes features such as product management, photo uploads, email notifications, secure authentication with JWT tokens, and price adjustments based on inventory changes. The project also leverages modern tools and technologies like MinIO, Docker, PostgreSQL, and GitLab CI/CD for a robust and scalable architecture.

## Key Features

- **User Roles & Authentication:**
  - JWT-based authentication with access and refresh tokens.
  - Three roles: **Admin**, **User**, and **Seller**.
  - Role-based access control using Spring Security.

- **MinIO for File Storage:**
  - Handles product image uploads when sellers create or update products.

- **Swagger & Postman for API Documentation:**
  - Auto-generated API documentation using Swagger for easy interaction.
  - Postman collection provided for testing API endpoints.

- **Email Notifications:**
  - Automatically sends email notifications to users when they create an order.
  - The notification provides information about order status and confirmation.

- **Trigger for Automatic Sale Price Adjustment:**
  - Trigger-based system automatically applies discounts or adjustments when product quantities or weights decrease below a certain threshold.

- **PostgreSQL Database:**
  - Relational database to store user, product, and order information.
  - Triggers and procedures used for inventory management and price adjustment.

- **Docker & Docker Compose:**
  - Containerized application using Docker for ease of deployment.
  - Docker Compose for orchestrating multiple services like PostgreSQL, MinIO, and the Java Spring app.

- **CI/CD with GitLab Actions:**
  - Continuous integration and deployment pipeline using GitLab Actions.
  - Automated testing, building, and deployment for a streamlined development workflow.

- **Clean Code Practices:**
  - Code refactoring and improvements using Stream API for clarity and performance.

## Technologies Used

- **Java Spring Boot**: Backend framework.
- **MinIO**: Object storage for handling product photo uploads.
- **PostgreSQL**: Relational database for data storage.
- **Swagger**: API documentation.
- **Postman**: API testing.
- **Spring Security**: Secure the application with role-based authentication.
- **JWT**: JSON Web Tokens for secure user authentication.
- **Docker & Docker Compose**: Containerization and orchestration.
- **GitLab CI/CD**: Automated pipelines for continuous integration and deployment.
- **Email Notifications**: Sent via email when users create an order.
- **Triggers in PostgreSQL**: For automatic price adjustments when inventory decreases.
