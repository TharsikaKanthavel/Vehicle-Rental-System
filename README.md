# Web-Based Vehicle Rental System

A comprehensive vehicle rental management system built with Spring Boot, designed to handle vehicle bookings, fleet management, user roles, and administrative operations.

## Features

### User Management
- **Multiple User Roles**: Admin, Customer, Driver, Staff, Finance Manager
- **Authentication & Authorization**: Secure login system with role-based access control
- **User Profiles**: Profile management and password change functionality

### Vehicle Management
- **Fleet Management**: Add, update, and manage vehicle inventory
- **Vehicle Categories**: Support for different types of vehicles
- **Availability Tracking**: Real-time vehicle availability checking

### Booking System
- **Online Bookings**: Customers can book vehicles online
- **Booking Management**: Admin approval and management of bookings
- **Booking Status Tracking**: Monitor booking lifecycle from pending to completed

### Delivery & Inspection
- **Vehicle Delivery**: Driver assignments for vehicle pickup/delivery
- **Pre/Post Inspection**: Vehicle condition checks before and after rental
- **Damage Reporting**: Track and manage vehicle damages

### Financial Management
- **Payment Processing**: Handle rental payments and transactions
- **Financial Reports**: Generate reports on revenue and transactions
- **Payment Status Tracking**: Monitor payment statuses

### Administrative Features
- **Dashboard**: Comprehensive admin dashboard with system overview
- **User Management**: Admin can manage all user accounts
- **System Metrics**: Monitor system performance and usage

## Technology Stack

- **Backend**: Spring Boot 3.2.5
- **Language**: Java 17
- **Database**: Microsoft SQL Server
- **ORM**: Spring Data JPA / Hibernate
- **Frontend**: Thymeleaf Templates
- **Security**: Spring Security
- **Build Tool**: Maven
- **Validation**: Bean Validation
- **Utilities**: Lombok

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Microsoft SQL Server
- Git (for cloning the repository)

## Installation & Setup

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd vehicle-rental-system
   ```

2. **Database Setup:**
   - Install Microsoft SQL Server
   - Create a database named `vehicle_rental_data`
   - Update database credentials in `src/main/resources/application.properties`:
     ```properties
     spring.datasource.username=your_username
     spring.datasource.password=your_password
     ```

3. **Build the application:**
   ```bash
   mvn clean install
   ```

4. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

5. **Access the application:**
   - Open your browser and navigate to `http://localhost:8080`

## Configuration

The application can be configured via `src/main/resources/application.properties`:

- **Server Port**: `server.port=8080`
- **Database URL**: Configure your SQL Server connection
- **JPA Settings**: Hibernate DDL auto-update strategy
- **Application Name**: `spring.application.name=vehicle-rental-system`

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/vehiclerental/
│   │       ├── WebBasedVehicleRentalSystemApplication.java
│   │       ├── config/          # Configuration classes
│   │       ├── controllers/     # REST/Web controllers
│   │       ├── dto/            # Data Transfer Objects
│   │       ├── models/         # JPA Entity models
│   │       ├── repositories/   # Data access layer
│   │       └── services/       # Business logic layer
│   └── resources/
│       ├── application.properties
│       ├── static/             # CSS, JS, images
│       └── templates/          # Thymeleaf templates
└── test/                       # Unit and integration tests
```

## API Endpoints

The application provides RESTful endpoints for various operations:

- **Authentication**: `/login`, `/register`, `/logout`
- **Admin**: `/admin/dashboard`, `/admin/users`, `/admin/vehicles`
- **Customer**: `/customer/dashboard`, `/customer/bookings`
- **Driver**: `/driver/dashboard`, `/driver/assignments`
- **Staff**: `/staff/dashboard`
- **Finance**: `/finance/dashboard`, `/finance/reports`

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions, please open an issue in the GitHub repository.