# 🏥 Clinic Appointment Booking System

A full-stack clinic appointment booking system with **Role-Based Access Control** (Admin / Doctor / Patient), built with Spring Boot 3 + React 18.

---

## 🚀 Quick Start (1 command)

```bash
git clone <repo>
cd clinic
docker-compose up --build
```

| Service     | URL                         |
|------------|------------------------------|
| 🌐 Frontend  | http://localhost             |
| ⚙️ Backend API | http://localhost:8080       |
| 📖 Swagger UI | http://localhost:8080/swagger-ui.html |
| 🗄️ MySQL     | localhost:3306               |

---

## 🔑 Default Accounts

| Role    | Email                   | Password   |
|---------|-------------------------|------------|
| Admin   | admin@clinic.com        | admin123   |
| Doctor  | dr.ahmed@clinic.com     | doctor123  |
| Doctor  | dr.sara@clinic.com      | doctor123  |
| Patient | patient@clinic.com      | patient123 |

---

## ✨ Features

### Patient
- Browse and search available doctors by specialty
- Book appointments with date/time selection
- View all personal appointments with status
- Cancel pending or confirmed appointments

### Doctor  
- View all assigned appointments
- Confirm / Reject / Complete appointments
- Dashboard with appointment stats by status

### Admin
- System-wide statistics dashboard
- Manage all users, doctors, and appointments
- Toggle doctor availability on/off
- Activate/deactivate user accounts

---

## 🏗️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 17, Spring Boot 3.3 |
| Security | Spring Security 6 + JWT (JJWT 0.12.5) |
| Database | MySQL 8.0 + Spring Data JPA |
| Frontend | React 18, React Router v6, Axios |
| DevOps | Docker, Docker Compose, Nginx |
| API Docs | SpringDoc OpenAPI (Swagger UI) |
| Email | JavaMailSender (optional — graceful fallback) |

---

## 📡 API Endpoints

### Auth
```
POST /api/auth/register   → Register Patient or Doctor
POST /api/auth/login      → Login, receive JWT token
```

### Doctors
```
GET  /api/doctors              → All doctors
GET  /api/doctors/available    → Available doctors only
GET  /api/doctors/search?specialty=... → Search by specialty
GET  /api/doctors/{id}         → Doctor details
PUT  /api/doctors/profile      → Update own profile (DOCTOR)
```

### Appointments
```
POST /api/appointments/book          → Book appointment (PATIENT)
PUT  /api/appointments/confirm/{id}  → Confirm (DOCTOR)
PUT  /api/appointments/reject/{id}   → Reject (DOCTOR)
PUT  /api/appointments/complete/{id} → Complete (DOCTOR)
PUT  /api/appointments/cancel/{id}   → Cancel (PATIENT or DOCTOR)
GET  /api/appointments/my            → My appointments
```

### Admin
```
GET /api/admin/stats                          → Dashboard stats
GET /api/admin/users                          → All users
GET /api/admin/doctors                        → All doctors
GET /api/admin/appointments                   → All appointments
PUT /api/admin/doctors/{id}/toggle-availability
PUT /api/admin/users/{id}/toggle-active
```

---

## 📊 Appointment Status Flow

```
PATIENT books → PENDING
                  ↓
DOCTOR confirms → CONFIRMED  ← email sent to patient
                  ↓
DOCTOR completes → COMPLETED

At any stage → CANCELLED  ← email sent to patient
```

---

## 📧 Email Configuration (Optional)

Edit `backend/src/main/resources/application.yml`:

```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-app-password
```

**Without email config:** The app works perfectly — emails are just logged to console.

---

## 🗂️ Project Structure

```
clinic/
├── backend/
│   ├── src/main/java/com/mostafa/clinic/
│   │   ├── config/       # Security, Swagger, DataInitializer
│   │   ├── controller/   # Auth, Doctor, Appointment, Admin
│   │   ├── dto/          # Request & Response DTOs
│   │   ├── entity/       # User, DoctorProfile, Appointment
│   │   ├── exception/    # Custom exceptions & handler
│   │   ├── repository/   # JPA repositories
│   │   ├── security/     # JWT service & filter
│   │   └── service/      # Business logic
│   ├── Dockerfile
│   └── pom.xml
├── frontend/
│   ├── src/
│   │   ├── api/          # Axios config
│   │   ├── components/   # Navbar
│   │   ├── context/      # AuthContext
│   │   └── pages/        # Login, Register, Patient/Doctor/Admin Dashboard
│   ├── nginx.conf        # Serves React + proxies /api to backend
│   └── Dockerfile
└── docker-compose.yml
```
