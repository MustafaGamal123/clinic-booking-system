@echo off
REM 🔐 اختبار API المصادقة - Windows Batch Script
REM Login API Test Script for Windows

echo.
echo ════════════════════════════════════════════════════════════════
echo           🔐 اختبار API تسجيل الدخول - Login API Test
echo ════════════════════════════════════════════════════════════════
echo.

REM Configuration
set API_URL=http://localhost:8080/api/auth/login
set HEADER_CONTENT_TYPE=Content-Type: application/json

REM Test accounts
echo 📋 الحسابات الافتراضية - Default Test Accounts:
echo.
echo   ├─ Admin (20)          -- email: admin@clinic.com           password: admin123
echo   ├─ Doctor Ahmed (20)   -- email: dr.ahmed@clinic.com        password: doctor123
echo   ├─ Doctor Sara (20)    -- email: dr.sara@clinic.com         password: doctor123
echo   ├─ Patient (20)        -- email: patient@clinic.com         password: patient123
echo.
echo ════════════════════════════════════════════════════════════════
echo.

REM Check if backend is running
echo 🔌 التحقق من اتصال Backend - Checking Backend Connection...
echo.

timeout /t 1 > nul

REM Prepare temporary file for JSON
set "TEMP_JSON=%TEMP%\login_test.json"

REM Test Admin
echo 🔍 اختبار تسجيل دخول: Admin
echo    Email: admin@clinic.com
echo    URL: %API_URL%
echo.

(
echo {
echo     "email": "admin@clinic.com",
echo     "password": "admin123"
echo }
) > "%TEMP_JSON%"

curl -s -X POST "%API_URL%" ^
    -H "%HEADER_CONTENT_TYPE%" ^
    -d @"%TEMP_JSON%" > "%TEMP%\response_admin.json"

if exist "%TEMP%\response_admin.json" (
    echo ✅ تم إرسال الطلب - Request Sent
    type "%TEMP%\response_admin.json"
    echo.
)

echo ───────────────────────────────────────────────────────────────
echo.

REM Test Doctor
echo 🔍 اختبار تسجيل دخول: Doctor
echo    Email: dr.ahmed@clinic.com
echo    URL: %API_URL%
echo.

(
echo {
echo     "email": "dr.ahmed@clinic.com",
echo     "password": "doctor123"
echo }
) > "%TEMP_JSON%"

curl -s -X POST "%API_URL%" ^
    -H "%HEADER_CONTENT_TYPE%" ^
    -d @"%TEMP_JSON%" > "%TEMP%\response_doctor.json"

if exist "%TEMP%\response_doctor.json" (
    echo ✅ تم إرسال الطلب - Request Sent
    type "%TEMP%\response_doctor.json"
    echo.
)

echo ───────────────────────────────────────────────────────────────
echo.

REM Test Patient
echo 🔍 اختبار تسجيل دخول: Patient
echo    Email: patient@clinic.com
echo    URL: %API_URL%
echo.

(
echo {
echo     "email": "patient@clinic.com",
echo     "password": "patient123"
echo }
) > "%TEMP_JSON%"

curl -s -X POST "%API_URL%" ^
    -H "%HEADER_CONTENT_TYPE%" ^
    -d @"%TEMP_JSON%" > "%TEMP%\response_patient.json"

if exist "%TEMP%\response_patient.json" (
    echo ✅ تم إرسال الطلب - Request Sent
    type "%TEMP%\response_patient.json"
    echo.
)

echo ═══════════════════════════════════════════════════════════════
echo.
echo 🎉 اكتمل الاختبار - Test Complete!
echo.
echo 💡 نصائح:
echo   • تأكد من تشغيل MySQL (mysql.exe في Services)
echo   • تأكد من تشغيل Backend على port 8080
echo   • شغّل هذا الـ script بعد تشغيل Backend بـ 5-10 ثواني
echo   • إذا فشلت الاختبارات، تفقد Backend Logs
echo.
echo اضغط أي مفتاح للإغلاق...
pause > nul
del "%TEMP_JSON%" 2>nul
del "%TEMP%\response_*.json" 2>nul
