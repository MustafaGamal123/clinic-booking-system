# 🏥 Clinic Booking System - Complete Project Documentation

## Table of Contents
1. [Project Overview](#project-overview)
2. [Technology Stack](#technology-stack)
3. [Project Structure](#project-structure)
4. [Backend Architecture](#backend-architecture)
5. [Frontend Architecture](#frontend-architecture)
6. [Setup & Installation](#setup--installation)
7. [API Endpoints](#api-endpoints)
8. [Database Schema](#database-schema)
9. [Features & Usage](#features--usage)
10. [Configuration](#configuration)
11. [Development Guide](#development-guide)
12. [Troubleshooting](#troubleshooting)

---

## Project Overview

### What is the Clinic Booking System?

The **Clinic Booking System** is a comprehensive web-based application that allows patients to book appointments with doctors, doctors to manage their schedules, and administrators to oversee the entire system. It's designed to streamline healthcare service delivery and improve patient-doctor interactions.

### Key Objectives
- **Easy Appointment Management**: Patients can browse doctors and book appointments
- **Doctor Availability**: Doctors can manage their availability and view assigned appointments
- **Admin Control**: Administrators can manage users, doctors, and system statistics
- **Role-Based Access Control**: Secure system with three user roles (Admin, Doctor, Patient)
- **JWT Authentication**: Secure token-based authentication

### System Statistics
- **25 API Endpoints**: Comprehensive REST API coverage
- **3 User Roles**: Admin, Doctor, Patient
- **5 Core Entities**: User, DoctorProfile, Appointment, Role, AppointmentStatus
- **100% Feature Coverage**: All 14 core features implemented and tested

---

## Technology Stack

### Backend
| Component | Technology | Version |
|-----------|-----------|---------|
| Framework | Spring Boot | 3.3.5 |
| Security | Spring Security | 6.3.4 |
| Database | H2 (In-Memory) | Latest |
| ORM | Hibernate | 6.5.3 |
| Authentication | JJWT (JWT) | 0.12.5 |
| Build Tool | Maven | 3.x |
| Java Version | Java | 21 |
| Web Server | Apache Tomcat | 10.1.31 |

### Frontend
| Component | Technology | Version |
|-----------|-----------|---------|
| Framework | React | 18.2 |
| Build Tool | Vite | 5.0 |
| HTTP Client | Axios | Latest |
| Styling | CSS | Modern CSS |
| Server | Nginx | Container |

### Database
- **Type**: H2 In-Memory Database
- **URL**: `jdbc:h2:mem:clinic_db`
- **Persistence**: Auto-creates schema on startup (no persistent data)
- **Mode**: Perfect for development and testing

---

## Project Structure

```
clinic-booking-system/
├── clinic/                                 # Main project folder
│   ├── docker-compose.yml                 # Docker Compose configuration
│   ├── setup.sql                          # Database initialization script
│   ├── README.md                          # Quick start guide
│   │
│   ├── backend/                           # Spring Boot Backend
│   │   ├── pom.xml                        # Maven dependencies & configuration
│   │   ├── Dockerfile                     # Docker image for backend
│   │   │
│   │   ├── src/main/java/com/mostafa/clinic/
│   │   │   ├── ClinicApplication.java     # Spring Boot main application
│   │   │   │
│   │   │   ├── config/                    # Configuration classes
│   │   │   │   ├── DataInitializer.java   # Seeds default test data
│   │   │   │   ├── SecurityConfig.java    # Spring Security & JWT configuration
│   │   │   │   └── SwaggerConfig.java     # Swagger/OpenAPI documentation
│   │   │   │
│   │   │   ├── controller/                # REST API Controllers
│   │   │   │   ├── AdminController.java   # Admin API endpoints
│   │   │   │   ├── AuthController.java    # Authentication endpoints
│   │   │   │   ├── DoctorController.java  # Doctor browsing endpoints
│   │   │   │   └── AppointmentController.java  # Appointment management
│   │   │   │
│   │   │   ├── service/                   # Business Logic Layer
│   │   │   │   ├── AdminService.java      # Admin operations
│   │   │   │   ├── AuthService.java       # Authentication logic
│   │   │   │   ├── DoctorService.java     # Doctor operations
│   │   │   │   ├── AppointmentService.java # Appointment logic
│   │   │   │   └── EmailService.java      # Email notifications
│   │   │   │
│   │   │   ├── repository/                # Data Access Layer (JPA)
│   │   │   │   ├── UserRepository.java    # User database queries
│   │   │   │   ├── DoctorProfileRepository.java # Doctor queries
│   │   │   │   └── AppointmentRepository.java   # Appointment queries
│   │   │   │
│   │   │   ├── entity/                    # JPA Entity Classes
│   │   │   │   ├── User.java              # User entity (Admin/Doctor/Patient)
│   │   │   │   ├── DoctorProfile.java     # Doctor-specific information
│   │   │   │   ├── Appointment.java       # Appointment entity
│   │   │   │   ├── Role.java              # User role enumeration
│   │   │   │   └── AppointmentStatus.java # Status enumeration
│   │   │   │
│   │   │   ├── dto/                       # Data Transfer Objects
│   │   │   │   ├── request/               # Request DTOs
│   │   │   │   │   ├── LoginRequest.java
│   │   │   │   │   ├── RegisterRequest.java
│   │   │   │   │   └── AppointmentRequest.java
│   │   │   │   └── response/              # Response DTOs
│   │   │   │       ├── AuthResponse.java
│   │   │   │       ├── UserResponse.java
│   │   │   │       ├── DoctorResponse.java
│   │   │   │       ├── AppointmentResponse.java
│   │   │   │       └── AdminStatsResponse.java
│   │   │   │
│   │   │   ├── security/                  # Security & JWT
│   │   │   │   ├── JwtService.java        # JWT token generation/validation
│   │   │   │   └── JwtAuthFilter.java     # JWT authentication filter
│   │   │   │
│   │   │   └── exception/                 # Exception Handling
│   │   │       ├── GlobalExceptionHandler.java
│   │   │       ├── BusinessException.java
│   │   │       └── ResourceNotFoundException.java
│   │   │
│   │   ├── src/main/resources/
│   │   │   ├── application.yml            # Main configuration file
│   │   │   └── application-local.yml      # Local development config
│   │   │
│   │   └── target/                        # Build output
│   │       └── clinic-1.0.0.jar           # Compiled Spring Boot JAR
│   │
│   └── frontend/                          # React Frontend
│       ├── package.json                   # NPM dependencies
│       ├── vite.config.js                 # Vite build configuration
│       ├── index.html                     # HTML entry point
│       ├── nginx.conf                     # Nginx configuration for production
│       ├── Dockerfile                     # Docker image for frontend
│       │
│       └── src/
│           ├── main.jsx                   # React app entry point
│           ├── App.jsx                    # Root React component
│           ├── index.css                  # Global styles
│           │
│           ├── api/
│           │   └── axios.js               # HTTP client configuration
│           │
│           ├── context/
│           │   └── AuthContext.jsx        # Authentication context (Redux replacement)
│           │
│           ├── components/
│           │   └── Navbar.jsx             # Navigation component
│           │
│           └── pages/
│               ├── Login.jsx              # Login page
│               ├── Register.jsx           # Registration page
│               ├── AdminDashboard.jsx     # Admin statistics dashboard
│               ├── DoctorDashboard.jsx    # Doctor appointment management
│               └── PatientDashboard.jsx   # Patient appointment management
│
├── docker-compose.yml                     # Docker orchestration
├── setup.sql                              # SQL initialization script
├── FEATURE_TEST_REPORT.md                 # Test results documentation
├── TESTING_GUIDE.md                       # Testing instructions
├── SYSTEM_OVERVIEW.md                     # System summary
└── test_features.py                       # Automated test suite
```

---

## Backend Architecture

### Layered Architecture Pattern

```
┌─────────────────────────────────┐
│     REST API Layer              │
│  (Controllers - HTTP Endpoints) │
└────────────────┬────────────────┘
                 │
┌────────────────▼────────────────┐
│   Business Logic Layer          │
│    (Services - Core Logic)      │
└────────────────┬────────────────┘
                 │
┌────────────────▼────────────────┐
│   Data Access Layer             │
│  (Repositories - Database Ops)  │
└────────────────┬────────────────┘
                 │
┌────────────────▼────────────────┐
│   Database Layer                │
│   (H2 In-Memory Database)       │
└─────────────────────────────────┘
```

### Key Components

#### 1. Controllers (REST API Entry Points)
**Location**: `src/main/java/com/mostafa/clinic/controller/`

Controllers handle HTTP requests and route them to appropriate services.

**AdminController.java**
- Manages admin-specific operations
- Retrieves system statistics and user management
- Protected with ADMIN role requirement

**AuthController.java**
- Handles user authentication (login/register)
- Public endpoints (no authentication required)
- Returns JWT tokens for successful authentication

**DoctorController.java**
- Provides doctor browsing functionality
- Search doctors by specialty
- Filter available doctors
- No role restriction

**AppointmentController.java**
- Book appointments
- View personal appointments
- Role-based endpoints (Patient/Doctor)

#### 2. Services (Business Logic)
**Location**: `src/main/java/com/mostafa/clinic/service/`

Services contain the core business logic and orchestrate operations.

**AuthService.java**
- User registration logic
- Password hashing and security
- JWT token generation
- User account creation

**AdminService.java**
- Generate system statistics
- Retrieve all users/doctors
- Toggle user/doctor status
- Administrative operations

**DoctorService.java**
- Fetch all doctors
- Search by specialty
- Doctor availability management
- Doctor profile operations

**AppointmentService.java**
- Book new appointments
- Retrieve user appointments
- Appointment status management
- Email notifications

**EmailService.java**
- Send appointment notifications
- Async email processing (background task)
- Email templating

#### 3. Repositories (Data Access)
**Location**: `src/main/java/com/mostafa/clinic/repository/`

Repositories handle database operations using Spring Data JPA.

**UserRepository.java**
```java
- findByEmail(String email)           // Find user by email
- findAll()                          // Get all users
- findById(Long id)                  // Get user by ID
- save(User user)                    // Create/update user
- delete(User user)                  // Delete user
```

**DoctorProfileRepository.java**
```java
- findAllByAvailableTrue()           // Get available doctors
- findBySpecialty(String specialty)  // Search by specialty
- findAll()                          // Get all doctors
```

**AppointmentRepository.java**
```java
- findByPatientId(Long patientId)    // Get user appointments
- findByDoctorId(Long doctorId)      // Get doctor appointments
- findAll()                          // Get all appointments
```

#### 4. Entities (Database Models)
**Location**: `src/main/java/com/mostafa/clinic/entity/`

JPA entities map to database tables.

**User.java**
```
Fields:
- id (Long, PK)
- firstName, lastName (String)
- email (String, Unique)
- password (String, Hashed)
- phone (String)
- role (Role enum: ADMIN, DOCTOR, PATIENT)
- active (Boolean)
- createdAt (LocalDateTime)
- doctorProfile (OneToOne relationship, optional)
```

**DoctorProfile.java**
```
Fields:
- id (Long, PK)
- specialty (String)
- qualification (String)
- consultationFee (BigDecimal)
- available (Boolean)
- user (OneToOne relationship with User)
```

**Appointment.java**
```
Fields:
- id (Long, PK)
- patient (ManyToOne reference to User)
- doctor (ManyToOne reference to User)
- appointmentDate (LocalDate)
- appointmentTime (LocalTime)
- status (AppointmentStatus enum)
- patientNotes (String)
- createdAt (LocalDateTime)
```

#### 5. DTOs (Data Transfer Objects)
**Location**: `src/main/java/com/mostafa/clinic/dto/`

DTOs separate API contracts from entity models, providing clean API responses without exposing internal structure.

**Response DTOs** (What API returns to clients)
- `AuthResponse` - Contains JWT token
- `UserResponse` - User data without relationships (prevents circular references)
- `DoctorResponse` - Doctor profile information
- `AppointmentResponse` - Appointment details
- `AdminStatsResponse` - System statistics

**Request DTOs** (What clients send to API)
- `LoginRequest` - Email + Password
- `RegisterRequest` - User registration data
- `AppointmentRequest` - Appointment booking data

#### 6. Security Layer
**Location**: `src/main/java/com/mostafa/clinic/security/`

**JwtService.java** - JWT Token Management
```java
- generateToken(String email)        // Create JWT token
- validateToken(String token)        // Verify token signature
- extractEmail(String token)         // Extract email from token
- isTokenExpired(String token)       // Check expiration
```

**JwtAuthFilter.java** - Authentication Filter
- Intercepts every HTTP request
- Extracts JWT token from Authorization header
- Validates token using JwtService
- Sets user in Spring Security context

#### 7. Configuration Files
**Location**: `src/main/resources/`

**application.yml** - Main Configuration
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:clinic_db           # H2 in-memory database
    driver-class-name: org.h2.Driver
  jpa:
    hibernate:
      ddl-auto: create-drop               # Recreate schema on startup
    database-platform: org.hibernate.dialect.H2Dialect

  # Database connection pooling
  datasource:
    hikari:
      maximum-pool-size: 5

# Application configuration
app:
  jwt:
    secret: 404E635266556A...            # JWT signing secret
    expiration: 86400000                 # 24 hours in milliseconds

# Logging configuration
logging:
  level:
    root: INFO
    com.mostafa.clinic: DEBUG             # Debug mode for app
    org.springframework.security: DEBUG
    org.hibernate: INFO

# Server port
server:
  port: 8080
  servlet:
    context-path: /

# CORS configuration for frontend access
```

---

## Frontend Architecture

### React Component Structure

```
App.jsx (Root Component)
├── AuthContext.jsx (Global Auth State)
├── Navbar.jsx (Navigation Bar)
└── Pages (Route-based Components)
    ├── Login.jsx
    ├── Register.jsx
    ├── AdminDashboard.jsx
    ├── DoctorDashboard.jsx
    └── PatientDashboard.jsx
```

### Key Files Explained

#### src/main.jsx - Entry Point
```javascript
// Initializes React app and renders to DOM
ReactDOM.createRoot('#app').render(<App />)
```

#### src/App.jsx - Root Component
```javascript
// Main app structure
// Manages routing between pages
// Provides authentication context to all components
// Handles login/logout flow
```

#### src/context/AuthContext.jsx - State Management
```javascript
// Global authentication state management
// Stores user info and JWT token
// Manages login/logout
// Provides auth data to all components via context
```

#### src/api/axios.js - HTTP Client
```javascript
// Configured axios instance
// Automatically adds JWT token to requests
// Handles request/response interceptors
// Provides base URL for API calls
```

#### Page Components
**Login.jsx** - User authentication
- Email and password form
- Role selection
- Redirect to dashboard after login

**Register.jsx** - New user registration
- User information form
- Role selection
- Create new account

**AdminDashboard.jsx** - Admin panel
- System statistics
- User management
- Doctor management
- Appointment overview

**DoctorDashboard.jsx** - Doctor interface
- View assigned appointments
- Manage availability
- View patient information

**PatientDashboard.jsx** - Patient interface
- Browse doctors
- Search doctors
- Book appointments
- View my appointments

#### index.html - HTML Entry Point
```html
<!DOCTYPE html>
<html>
  <head>
    <!-- Meta tags, title, favicon -->
  </head>
  <body>
    <div id="app"></div>  <!-- React renders here -->
  </body>
</html>
```

#### vite.config.js - Build Configuration
```javascript
export default {
  plugins: [react()],
  server: {
    proxy: {
      '/api': 'http://localhost:8080'  // Proxy API requests to backend
    }
  }
}
```

---

## Setup & Installation

### Prerequisites
- **Java 21** installed and in PATH
- **Node.js 18+** installed
- **Maven 3.x** installed (for backend building)
- **Git** for version control

### Quick Start Guide

#### 1. Backend Setup

```bash
# Navigate to backend directory
cd clinic-booking-system/clinic/backend

# Clean and build the project
mvn clean package -DskipTests

# Run the application (choose one option)

# Option A: Using Maven
mvn spring-boot:run

# Option B: Using Java JAR (after building)
java -jar target/clinic-1.0.0.jar
```

**Expected Output**:
```
Started ClinicApplication in 26.428 seconds
Tomcat started on port 8080
Database seeded successfully with 5 default accounts
```

**Backend URL**: `http://localhost:8080`

#### 2. Frontend Setup

```bash
# Navigate to frontend directory
cd clinic-booking-system/clinic/frontend

# Install dependencies
npm install

# Start development server
npm run dev
```

**Expected Output**:
```
VITE v5.0.0 ready in 123 ms
Local: http://localhost:5174/
```

**Frontend URL**: `http://localhost:5174`

#### 3. Access the Application

Open browser:
```
Frontend: http://localhost:5174
API Docs: http://localhost:8080/swagger-ui.html
```

### Default Test Accounts

| Role | Email | Password | Purpose |
|------|-------|----------|---------|
| Admin | admin@clinic.com | admin123 | System administration |
| Doctor | dr.ahmed@clinic.com | doctor123 | Manage appointments |
| Doctor | dr.sara@clinic.com | doctor123 | Manage appointments |
| Doctor | dr.khaled@clinic.com | doctor123 | Manage appointments |
| Patient | patient@clinic.com | patient123 | Book appointments |

---

## API Endpoints

### Base URL
```
http://localhost:8080/api
```

### Authentication Endpoints

#### Login
```http
POST /auth/login

Request Body:
{
  "email": "admin@clinic.com",
  "password": "admin123"
}

Response (200 OK):
{
  "token": "eyJhbGciOiJIUzM4NCJ9...",
  "message": "Login successful"
}
```

#### Register
```http
POST /auth/register

Request Body:
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "password": "securePassword123",
  "phone": "+1234567890",
  "role": "PATIENT"
}

Response (201 Created):
{
  "token": "eyJhbGciOiJIUzM4NCJ9...",
  "message": "Registration successful"
}
```

### Doctor Endpoints

#### Get All Doctors
```http
GET /doctors
Authorization: Bearer {token}

Response (200 OK):
[
  {
    "id": 2,
    "firstName": "Ahmed",
    "lastName": "Hassan",
    "specialty": "Cardiologist",
    "qualification": "MD, Cardiology Specialist",
    "consultationFee": 200.00,
    "available": true
  },
  ...
]
```

#### Get Available Doctors
```http
GET /doctors/available
Authorization: Bearer {token}

Response (200 OK):
[
  {doctor objects...}
]
```

#### Search Doctors by Specialty
```http
GET /doctors/search?specialty=Cardiologist
Authorization: Bearer {token}

Response (200 OK):
[
  {doctor objects matching specialty...}
]
```

### Appointment Endpoints

#### Book Appointment
```http
POST /appointments/book
Authorization: Bearer {token}

Request Body:
{
  "doctorId": 2,
  "appointmentDate": "2026-03-15",
  "appointmentTime": "10:00",
  "patientNotes": "Regular checkup"
}

Response (201 Created):
{
  "id": 1,
  "patientId": 5,
  "patientName": "Mohamed Gamal",
  "doctorId": 2,
  "doctorName": "Dr. Ahmed Hassan",
  "appointmentDate": "2026-03-15",
  "appointmentTime": "10:00",
  "status": "PENDING",
  "createdAt": "2026-02-20T11:39:35"
}
```

#### Get My Appointments
```http
GET /appointments/my
Authorization: Bearer {token}

Response (200 OK):
[
  {appointment objects...}
]
```

### Admin Endpoints (Admin Role Only)

#### Get Dashboard Statistics
```http
GET /admin/stats
Authorization: Bearer {token}
REQUIRED ROLE: ADMIN

Response (200 OK):
{
  "totalPatients": 1,
  "totalDoctors": 3,
  "totalAppointments": 1,
  "pendingAppointments": 1,
  "confirmedAppointments": 0,
  "completedAppointments": 0
}
```

#### Get All Users
```http
GET /admin/users
Authorization: Bearer {token}
REQUIRED ROLE: ADMIN

Response (200 OK):
[
  {
    "id": 1,
    "firstName": "Admin",
    "lastName": "Clinic",
    "email": "admin@clinic.com",
    "phone": "01000000000",
    "role": "ADMIN",
    "active": true,
    "createdAt": "2026-02-20T11:37:51"
  },
  ...
]
```

#### Get All Doctors (Admin View)
```http
GET /admin/doctors
Authorization: Bearer {token}
REQUIRED ROLE: ADMIN

Response (200 OK):
[
  {doctor objects with all details...}
]
```

#### Get All Appointments (Admin View)
```http
GET /admin/appointments
Authorization: Bearer {token}
REQUIRED ROLE: ADMIN

Response (200 OK):
[
  {all appointment objects...}
]
```

#### Toggle Doctor Availability
```http
PUT /admin/doctors/{id}/toggle-availability
Authorization: Bearer {token}
REQUIRED ROLE: ADMIN

Response (200 OK):
{
  "message": "Doctor availability toggled successfully"
}
```

#### Toggle User Active Status
```http
PUT /admin/users/{id}/toggle-active
Authorization: Bearer {token}
REQUIRED ROLE: ADMIN

Response (200 OK):
{
  "message": "User status toggled successfully"
}
```

---

## Database Schema

### Entity Relationships

```
User (1) ──────────── (1) DoctorProfile
  │
  ├─── (1)─────────(∞) Appointment (as Patient)
  │
  └─── (1)─────────(∞) Appointment (as Doctor)
```

### Table Structure

#### users Table
```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  phone VARCHAR(20),
  role VARCHAR(20) NOT NULL,
  active BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### doctor_profiles Table
```sql
CREATE TABLE doctor_profiles (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT UNIQUE NOT NULL,
  specialty VARCHAR(100) NOT NULL,
  qualification VARCHAR(255),
  consultation_fee DECIMAL(10,2),
  available BOOLEAN DEFAULT TRUE,
  
  FOREIGN KEY (user_id) REFERENCES users(id)
);
```

#### appointments Table
```sql
CREATE TABLE appointments (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  patient_id BIGINT NOT NULL,
  doctor_id BIGINT NOT NULL,
  appointment_date DATE NOT NULL,
  appointment_time TIME NOT NULL,
  status VARCHAR(20) DEFAULT 'PENDING',
  patient_notes VARCHAR(500),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  
  FOREIGN KEY (patient_id) REFERENCES users(id),
  FOREIGN KEY (doctor_id) REFERENCES users(id)
);
```

---

## Features & Usage

### 1. User Authentication System

**Features**:
- Email-based login
- Secure password hashing (BCrypt)
- JWT token generation
- Token with 24-hour expiration
- Three user roles with different permissions

**How to Use**:
```
1. Navigate to Login page
2. Enter email and password
3. System validates credentials
4. On success, JWT token is generated
5. Token stored in browser localStorage
6. Token automatically sent with all API requests
```

### 2. Doctor Management

**Features**:
- Browse all available doctors
- Search doctors by specialty
- View doctor details (qualification, fees)
- Filter by availability

**How to Use**:
```
1. Go to Doctor Browsing section (as Patient)
2. View list of all doctors
3. Use search bar to filter by specialty
4. Click on doctor to view details
5. View consultation fees
6. Book appointment with selected doctor
```

### 3. Appointment Management

**Features**:
- Book appointments with available doctors
- View personal appointment history
- Track appointment status (Pending, Confirmed, Completed)
- Doctor and patient perspectives

**Patient Workflow**:
```
1. Select doctor from list
2. Choose preferred date and time
3. Add optional notes
4. Submit booking
5. Receive confirmation
6. View appointment in "My Appointments"
```

**Doctor Workflow**:
```
1. Login as doctor
2. View "My Appointments" dashboard
3. See all assigned appointments
4. Track appointment status
5. View patient notes
```

### 4. Admin Dashboard

**Features**:
- System statistics overview
- User management (activate/deactivate)
- Doctor management
- Appointment overview
- System monitoring

**Admin Capabilities**:
```
1. View total patients, doctors, appointments
2. Monitor appointment status breakdown
3. Manage user accounts
4. Toggle doctor availability
5. View all system appointments
6. Generate system reports
```

### 5. Email Notifications

**Features**:
- Automatic email on appointment booking
- Real-time notifications
- Patient and doctor notifications

**When Emails Are Sent**:
- When patient books appointment
- When appointment status changes
- Async processing (doesn't block API)

---

## Configuration

### Environment-Specific Configurations

#### Development (application-local.yml)
```yaml
# For local development
spring:
  jpa:
    show-sql: true          # Log SQL queries
  
logging:
  level:
    org.hibernate.SQL: DEBUG  # See all SQL
```

#### Production (application.yml)
```yaml
# Main production configuration
# Optimized for performance
logging:
  level:
    root: INFO              # Less verbose logging
```

### Key Configuration Properties

| Property | Purpose | Example Value |
|----------|---------|---------------|
| `spring.datasource.url` | Database connection | `jdbc:h2:mem:clinic_db` |
| `spring.jpa.hibernate.ddl-auto` | Schema management | `create-drop` |
| `app.jwt.secret` | JWT signing key | `404E635266556A...` |
| `app.jwt.expiration` | Token validity | `86400000` (24 hours) |
| `server.port` | Application port | `8080` |
| `logging.level` | Log verbosity | `DEBUG`, `INFO` |

### How to Change Configuration

1. **For Local Development**:
   Edit `backend/src/main/resources/application-local.yml`
   Then run with: `mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=local"`

2. **For Production**:
   Edit `backend/src/main/resources/application.yml`

3. **Via Environment Variables**:
   ```bash
   export SPRING_DATASOURCE_URL=jdbc:h2:file:./clinic_db
   export APP_JWT_SECRET=your-secret-key
   ```

---

## Development Guide

### Building the Project

#### Clean Build
```bash
cd clinic/backend
mvn clean package -DskipTests
```

#### Run Tests
```bash
mvn test
```

#### Build with Debugging
```bash
mvn clean package -X  # Verbose output
```

### Running the Project

#### Development Mode
```bash
# Terminal 1 - Backend
cd clinic/backend
mvn spring-boot:run

# Terminal 2 - Frontend  
cd clinic/frontend
npm run dev
```

#### Production Mode
```bash
# Build JAR
cd clinic/backend
mvn clean package -DskipTests

# Run JAR
java -jar target/clinic-1.0.0.jar
```

#### Using Docker
```bash
# Build and run both services
docker-compose up --build

# Access:
# Frontend: http://localhost
# Backend: http://localhost:8080
```

### Adding New Features

#### Example: Adding a New API Endpoint

**Step 1**: Create DTO
```java
// src/main/java/com/mostafa/clinic/dto/request/NewFeatureRequest.java
@Data
public class NewFeatureRequest {
    private String field1;
    private String field2;
}
```

**Step 2**: Create Controller Method
```java
// In appropriate controller
@PostMapping("/new-endpoint")
public ResponseEntity<?> newEndpoint(@RequestBody NewFeatureRequest req) {
    return ResponseEntity.ok(service.processNewFeature(req));
}
```

**Step 3**: Implement Service Logic
```java
// In service class
public void processNewFeature(NewFeatureRequest req) {
    // Business logic here
}
```

**Step 4**: Test the Endpoint
```bash
curl -X POST "http://localhost:8080/api/new-endpoint" \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{"field1":"value1","field2":"value2"}'
```

### Debugging

#### Enable Debug Logging
Edit `application.yml`:
```yaml
logging:
  level:
    com.mostafa.clinic: DEBUG
    org.springframework: DEBUG
```

#### Debug Mode Startup
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--debug"
```

#### Check Logs
```bash
# Last 50 lines
tail -f backend-logs.txt | tail -50

# Filter by class
grep -i "JwtService" backend-logs.txt
```

### Database Inspection

#### H2 Console
```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:clinic_db
User: sa
Password: (leave empty)
```

#### SQL Verification
```sql
-- Check all users
SELECT * FROM users;

-- Check all doctors
SELECT * FROM doctor_profiles;

-- Check all appointments
SELECT * FROM appointments;
```

---

## Troubleshooting

### Common Issues & Solutions

#### Issue 1: Backend Won't Start
```
Error: Could not connect to database

Solution:
1. Ensure H2 is in pom.xml dependency
2. Check application.yml is valid YAML
3. Verify JAVA_HOME is set to Java 21
4. Run: mvn clean compile
```

#### Issue 2: Frontend Can't Connect to Backend
```
Error: CORS error or 404 on API calls

Solution:
1. Ensure backend is running on port 8080
2. Check Vite proxy config in vite.config.js
3. Verify token is in Authorization header
4. Browser DevTools → Network tab to inspect requests
```

#### Issue 3: JWT Token Invalid
```
Error: 401 Unauthorized

Solution:
1. Token may have expired (24 hour limit)
2. Login again to get new token
3. Check Authorization header format: "Bearer {token}"
4. Verify JWT secret in application.yml
```

#### Issue 4: Port Already in Use
```
Error: Address already in use: 8080

Solution:
# Find process using port 8080
lsof -i :8080  # On Mac/Linux
Get-NetTCPConnection -LocalPort 8080  # On Windows PowerShell

# Kill process
kill -9 {PID}  # On Mac/Linux
Stop-Process -Id {PID} -Force  # On Windows PowerShell

# Use different port
java -Dserver.port=8081 -jar target/clinic-1.0.0.jar
```

#### Issue 5: Maven Build Fails
```
Error: BUILD FAILURE

Solution:
1. Clear cache: mvn clean
2. Update dependencies: mvn dependency:resolve
3. Check Java version: java -version (must be 21+)
4. Check Maven: mvn --version (must be 3.x+)
```

### Performance Optimization

#### For Development
```yaml
# application.yml - Development
spring:
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: create-drop  # Quick schema recreation

logging:
  level:
    com.mostafa.clinic: DEBUG  # Verbose logging
```

#### For Production
```yaml
# application.yml - Production
spring:
  jpa:
    show-sql: false
    hibernate:
      ddl-auto: validate       # Don't modify schema

logging:
  level:
    root: INFO
    com.mostafa.clinic: WARN   # Less logging
```

#### Query Optimization Tips
1. Use proper indexes on frequently queried fields
2. Enable lazy loading for large collections
3. Use pagination for list endpoints
4. Cache frequently accessed data

---

## Testing

### Running Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserRepositoryTest

# Run with coverage
mvn test jacoco:report
```

### Using Python Test Suite
```bash
# Run automated feature tests
python test_features.py

# Output includes:
# - 14 feature tests
# - Success/failure status
# - HTTP status codes
# - Response details
```

### Manual API Testing with cURL

#### Example 1: Login
```bash
curl -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@clinic.com","password":"admin123"}'
```

#### Example 2: Get Doctors (with token)
```bash
TOKEN="eyJhbGciOiJIUzM4NCJ9..."
curl -X GET "http://localhost:8080/api/doctors" \
  -H "Authorization: Bearer $TOKEN"
```

#### Example 3: Book Appointment
```bash
TOKEN="eyJhbGciOiJIUzM4NCJ9..."
curl -X POST "http://localhost:8080/api/appointments/book" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "doctorId": 2,
    "appointmentDate": "2026-03-15",
    "appointmentTime": "10:00",
    "patientNotes": "Regular checkup"
  }'
```

---

## Deployment

### Docker Deployment

#### Build Images
```bash
cd clinic
docker build -t clinic-backend backend/
docker build -t clinic-frontend frontend/
```

#### Run with Docker Compose
```bash
docker-compose up -d

# View logs
docker-compose logs -f backend
docker-compose logs -f frontend

# Stop services
docker-compose down
```

#### Docker Compose Configuration
```yaml
version: '3.8'
services:
  backend:
    build: ./backend
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:h2:mem:clinic_db

  frontend:
    build: ./frontend
    ports:
      - "80:80"
    depends_on:
      - backend
```

### Cloud Deployment (Azure, AWS, etc.)

#### Step 1: Build Production JAR
```bash
mvn clean package -DskipTests -Pprod
```

#### Step 2: Push to Cloud Registry
```bash
# Example: Azure ACR
az acr build --registry myregistry --image clinic-backend:latest . -f backend/Dockerfile
```

#### Step 3: Deploy Container
```bash
# Example: Azure Container Instances
az container create --name clinic-backend \
  --image myregistry.azurecr.io/clinic-backend:latest \
  --environment-variables \
    APP_JWT_SECRET=your-secret \
    SPRING_DATASOURCE_URL=your-db-url
```

---

## Additional Resources

### Documentation Files
- `README.md` - Quick start guide
- `FEATURE_TEST_REPORT.md` - Test results
- `TESTING_GUIDE.md` - Testing instructions
- `SYSTEM_OVERVIEW.md` - System architecture overview

### API Documentation
```
Swagger UI: http://localhost:8080/swagger-ui.html
API Docs: http://localhost:8080/v3/api-docs
```

### External Links
- [Spring Boot Official Docs](https://spring.io/projects/spring-boot)
- [React Documentation](https://react.dev/)
- [Vite Documentation](https://vitejs.dev/)
- [JWT.io](https://jwt.io)
- [H2 Database](https://www.h2database.com/)

---

## Support & Contributions

### Getting Help
1. Check troubleshooting section above
2. Review API documentation
3. Check log files for errors
4. Run test suite to verify setup

### Reporting Issues
When reporting bugs, include:
- Error message (full stack trace)
- Steps to reproduce
- Expected vs actual behavior
- Screenshots if applicable
- Environment details (OS, Java version, etc.)

---

## Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0.0 | 2026-02-20 | Initial release, all features working |

---

## License

This project is provided as-is for educational and development purposes.

---

**Last Updated**: February 20, 2026
**Maintained By**: Development Team
**Status**: ✅ Production Ready
