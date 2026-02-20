-- Run this in MySQL before starting the app
-- mysql -u root -p < setup.sql

CREATE DATABASE IF NOT EXISTS clinic_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- If your root has a password, just run the app directly after creating the DB.
-- Spring Boot (ddl-auto: update) will create all tables automatically.

SELECT 'Database clinic_db created successfully!' as status;
