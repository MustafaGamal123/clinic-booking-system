# 🏥 Clinic Booking System — Local Setup Guide (No Docker)

---

## Prerequisites

Make sure these are installed on your machine:

| Tool | Version | Check |
|------|---------|-------|
| Java (JDK) | 17 or 21 | `java -version` |
| Maven | 3.8+ | `mvn -version` |
| MySQL | 8.0 | `mysql --version` |
| Node.js | 18+ | `node -v` |
| npm | 9+ | `npm -v` |

---

## Part 1: MySQL Setup

### Step 1 — Start MySQL
```bash
# macOS (Homebrew)
brew services start mysql

# Windows
net start MySQL80

# Linux
sudo systemctl start mysql
```

### Step 2 — Create the database
```bash
mysql -u root -p
```
Then run:
```sql
CREATE DATABASE IF NOT EXISTS clinic_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
EXIT;
```

Or just run the provided script:
```bash
mysql -u root -p < setup.sql
```

### Step 3 — Configure your MySQL password

Open `backend/src/main/resources/application.yml` and update:

```yaml
spring:
  datasource:
    username: root
    password: "your_mysql_password_here"   # ← put your password here
```

If MySQL has **no password** (common on local installs), leave it as:
```yaml
    password: ""
```

---

## Part 2: Run the Backend

### Step 1 — Navigate to the backend folder
```bash
cd backend
```

### Step 2 — Run with Maven
```bash
mvn spring-boot:run
```

**First run takes 2–5 minutes** — Maven downloads all dependencies.

### ✅ Success signs in the console:
```
INFO  DataInitializer - ✅ Database seeded successfully!
INFO  DataInitializer -   Admin    → admin@clinic.com    / admin123
INFO  DataInitializer -   Doctor   → dr.ahmed@clinic.com / doctor123
INFO  DataInitializer -   Patient  → patient@clinic.com  / patient123
INFO  Started ClinicApplication in 4.2 seconds
```

### URLs after backend starts:
| Resource | URL |
|----------|-----|
| API base | http://localhost:8080/api |
| Swagger UI | http://localhost:8080/swagger-ui.html |

---

## Part 3: Test the Backend with Swagger

Open **http://localhost:8080/swagger-ui.html**

### Test 1 — Login as Admin
- Go to **POST /api/auth/login**
- Click "Try it out"
- Body:
```json
{
  "email": "admin@clinic.com",
  "password": "admin123"
}
```
- Execute → Copy the `token` from the response

### Test 2 — Authorize all requests
- Click the **Authorize 🔓** button (top right of Swagger)
- Paste: `Bearer <your_token_here>`
- Click Authorize

### Test 3 — Get admin stats
- Go to **GET /api/admin/stats**
- Execute → Should return patient/doctor/appointment counts

### Test 4 — Get available doctors (as Patient)
Login with `patient@clinic.com / patient123`, get token, authorize, then:
- **GET /api/doctors/available** → returns 3 doctors

### Test 5 — Book an appointment
Still as patient:
- **POST /api/appointments/book**
```json
{
  "doctorId": 2,
  "appointmentDate": "2026-03-15",
  "appointmentTime": "10:00:00",
  "patientNotes": "I have a chest pain"
}
```
→ Returns appointment with `status: "PENDING"`

### Test 6 — Confirm as Doctor
Login with `dr.ahmed@clinic.com / doctor123`, get token, authorize, then:
- **PUT /api/appointments/confirm/1**
→ Status changes to `"CONFIRMED"` + log shows email would be sent

### Test 7 — Check all endpoints work
Run through this list in Swagger:
```
✅ POST /api/auth/register
✅ POST /api/auth/login
✅ GET  /api/doctors
✅ GET  /api/doctors/available
✅ GET  /api/doctors/search?specialty=Cardiology
✅ GET  /api/doctors/{id}
✅ PUT  /api/doctors/profile      (Doctor only)
✅ POST /api/appointments/book    (Patient only)
✅ GET  /api/appointments/my
✅ PUT  /api/appointments/confirm/{id}   (Doctor)
✅ PUT  /api/appointments/reject/{id}    (Doctor)
✅ PUT  /api/appointments/complete/{id}  (Doctor)
✅ PUT  /api/appointments/cancel/{id}    (Patient or Doctor)
✅ GET  /api/admin/stats          (Admin only)
✅ GET  /api/admin/doctors        (Admin only)
✅ GET  /api/admin/appointments   (Admin only)
✅ GET  /api/admin/users          (Admin only)
✅ PUT  /api/admin/doctors/{id}/toggle-availability
✅ PUT  /api/admin/users/{id}/toggle-active
```

---

## Part 4: Run the Frontend

Open a **new terminal** (keep backend running).

### Step 1 — Navigate to frontend
```bash
cd frontend
```

### Step 2 — Install dependencies
```bash
npm install
```

### Step 3 — Run Vite dev server
```bash
npm run dev
```

### ✅ Success:
```
  VITE v5.x.x  ready in 800ms

  ➜  Local:   http://localhost:5173/
  ➜  Network: use --host to expose
```

### Open: http://localhost:5173

---

## Part 5: Test the Full Flow

### Flow 1: Patient books appointment

1. Go to http://localhost:5173
2. Click **"Don't have an account? Register"**
3. Register as a **Patient** (fill name, email, password, role = Patient)
4. You'll be redirected to the Patient Dashboard
5. Browse available doctors → click **Book Appointment**
6. Pick a date + time → Confirm Booking
7. Go to **My Appointments** tab → see status: `PENDING`

### Flow 2: Doctor confirms

1. Logout (top right)
2. Login as `dr.ahmed@clinic.com / doctor123`
3. See the new appointment in the **Pending** tab
4. Click **✓ Confirm**
5. Status changes to `CONFIRMED`
6. Console logs: `[EMAIL] CONFIRMED → patient@...`

### Flow 3: Admin manages system

1. Login as `admin@clinic.com / admin123`
2. Stats tab shows live counts
3. Doctors tab → toggle a doctor's availability
4. Appointments tab → see all bookings

---

## Common Problems & Fixes

### ❌ "Communications link failure" or "Connection refused"
**MySQL is not running.**
```bash
# Start MySQL
brew services start mysql      # macOS
sudo systemctl start mysql     # Linux
net start MySQL80              # Windows
```

### ❌ "Access denied for user 'root'"
**Wrong MySQL password in application.yml.**
```yaml
spring:
  datasource:
    password: "your_actual_password"
```

### ❌ "Port 8080 already in use"
Something else is using port 8080.
```bash
# Kill it (Linux/macOS)
lsof -ti:8080 | xargs kill

# Or change the port in application.yml
server:
  port: 9090
```

### ❌ Frontend can't reach API (CORS / 404 on /api calls)
Make sure backend is running on **port 8080**.
The Vite proxy in `vite.config.js` forwards `/api/*` → `http://localhost:8080`.

### ❌ "Failed to load ApplicationContext" / Hibernate error
```bash
# Clean Maven cache and retry
mvn clean spring-boot:run
```

### ❌ JWT token expired in Swagger
The token expires after 24 hours. Just log in again and re-authorize.

---

## Default Accounts Summary

| Role | Email | Password | Can do |
|------|-------|----------|--------|
| Admin | admin@clinic.com | admin123 | Manage everything |
| Doctor | dr.ahmed@clinic.com | doctor123 | Confirm/complete appointments |
| Doctor | dr.sara@clinic.com | doctor123 | Confirm/complete appointments |
| Doctor | dr.khaled@clinic.com | doctor123 | Confirm/complete appointments |
| Patient | patient@clinic.com | patient123 | Book appointments |

---

## Project Folder Structure

```
clinic/
├── backend/                          ← Spring Boot app
│   ├── src/main/java/com/mostafa/clinic/
│   │   ├── config/
│   │   │   ├── SecurityConfig.java   ← JWT + RBAC rules
│   │   │   ├── SwaggerConfig.java    ← API docs config
│   │   │   └── DataInitializer.java  ← Seeds default data
│   │   ├── controller/               ← REST endpoints
│   │   ├── dto/                      ← Request & Response objects
│   │   ├── entity/                   ← JPA database entities
│   │   ├── exception/                ← Error handling
│   │   ├── repository/               ← Database queries
│   │   ├── security/                 ← JWT filter & service
│   │   └── service/                  ← Business logic
│   ├── src/main/resources/
│   │   └── application.yml           ← ← ← CONFIGURE YOUR DB HERE
│   └── pom.xml
│
├── frontend/                         ← React 18 app
│   ├── src/
│   │   ├── api/axios.js              ← Axios + interceptors
│   │   ├── context/AuthContext.jsx   ← Global auth state
│   │   ├── components/Navbar.jsx
│   │   └── pages/
│   │       ├── Login.jsx
│   │       ├── Register.jsx
│   │       ├── PatientDashboard.jsx
│   │       ├── DoctorDashboard.jsx
│   │       └── AdminDashboard.jsx
│   ├── vite.config.js                ← Proxy /api → localhost:8080
│   └── package.json
│
├── setup.sql                         ← Create MySQL database
├── docker-compose.yml                ← Docker alternative
└── README.md
```

---

## Quick Reference Commands

```bash
# Backend
cd backend
mvn spring-boot:run                    # Start (downloads deps on first run)
mvn clean spring-boot:run             # Start fresh (clears Maven cache)
mvn clean package -DskipTests         # Build JAR only

# Frontend
cd frontend
npm install                           # Install dependencies (first time only)
npm run dev                           # Start dev server on port 5173
npm run build                         # Build for production

# MySQL
mysql -u root -p clinic_db           # Connect to the DB directly
show tables;                          # List all tables after app starts
select * from users;                  # Check seeded users
```
