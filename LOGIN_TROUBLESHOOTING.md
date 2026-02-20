# 🔐 حل مشكلة تسجيل الدخول - Login Troubleshooting Guide

## المشكلة 🛑
عند محاولة تسجيل الدخول بالحسابات الافتراضية (Default Accounts)، تظهر الرسالة:
```
Login failed. Check your credentials.
```

---

## ✅ الحل الشامل

### الخطوة 1️⃣: تأكد من قاعدة البيانات MySQL

```bash
# تحقق من أن MySQL يعمل
mysql -u root -p

# استعرض قواعد البيانات
SHOW DATABASES;

# تحقق من وجود clinic_db
USE clinic_db;

# اعرض المستخدمين
SELECT id, email, first_name, role FROM users;
```

---

### الخطوة 2️⃣: تأكد من أن التطبيق يعمل

```bash
cd backend
mvn clean compile
mvn spring-boot:run
```

**تحقق من السجلات (Logs) في console:**

```
✅ Database seeded successfully!
────────────────────────────────
📋 Default Accounts Created:
   👤 Admin    → admin@clinic.com    / admin123
   🏥 Doctor   → dr.ahmed@clinic.com / doctor123
   🏥 Doctor   → dr.sara@clinic.com  / doctor123
   🏥 Doctor   → dr.khaled@clinic.com / doctor123
   👨 Patient  → patient@clinic.com  / patient123
────────────────────────────────
Total users in database: 5
```

---

### الخطوة 3️⃣: اختبر المصادقة باستخدام cURL

```bash
# اختبر تسجيل الدخول للمسؤول
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@clinic.com",
    "password": "admin123"
  }'
```

**الاستجابة الناجحة:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "id": 1,
  "firstName": "Admin",
  "lastName": "Clinic",
  "email": "admin@clinic.com",
  "role": "ADMIN"
}
```

---

### الخطوة 4️⃣: تشخيص الأخطاء الشائعة

#### ❌ المشكلة: "User not found in database"
**السبب:** لم يتم إنشاء البيانات الافتراضية
**الحل:**
1. احذف قاعدة البيانات: `DROP DATABASE clinic_db;`
2. أعد تشغيل التطبيق - سيتم إنشاء البيانات تلقائياً

#### ❌ المشكلة: "Invalid email or password"
**السبب:** كلمة المرور غير صحيحة أو تشفير خاطئ
**الحل:**
- تأكد من أن كلمة المرور صحيحة تماماً (Caps Lock مهم!)
- تحقق من قيمة BCrypt في قاعدة البيانات

#### ❌ المشكلة: "Connection refused"
**السبب:** MySQL غير متصل أو البيرت خاطئ
**الحل:**
```bash
# تحقق من اتصال MySQL
mysql -u root

# أو بكلمة مرور
mysql -u root -p
```

---

### الخطوة 5️⃣: نقاط مهمة تم إصلاحها

#### ✅ Maven Compiler Configuration
أضيف configuration صحيح لـ Lombok annotation processor:
```xml
<annotationProcessorPaths>
    <path>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.30</version>
    </path>
</annotationProcessorPaths>
```

#### ✅ Circular Dependency Fix
تم حل المشكلة باستخدام `ObjectProvider`:
```java
// بدلاً من:
private final JwtAuthFilter jwtAuthFilter;

// استخدمنا:
private final ObjectProvider<JwtAuthFilter> jwtAuthFilterProvider;
```

#### ✅ Enhanced Logging
تم إضافة تسجيلات تفصيلية في:
- `DataInitializer.java` - إنشاء البيانات الافتراضية
- `AuthService.java` - عمليات المصادقة
- `SecurityConfig.java` - تحميل بيانات المستخدم
- `JwtService.java` - توليد الرموز

#### ✅ Better Error Handling
- معالج استثناءات محسّن في `GlobalExceptionHandler.java`
- رسائل خطأ واضحة للمستخدم
- تسجيل تفصيلي للأخطاء

---

## 📱 اختبر من Frontend

### 1. تأكد من أن Frontend متصل بـ Backend الصحيح
```javascript
// في src/api/axios.js
const api = axios.create({
  baseURL: 'http://localhost:8080/api',  // ✅ URL صحيحة
  headers: { 'Content-Type': 'application/json' }
})
```

### 2. افتح Browser Console (F12)
وابحث عن الرسائل:
- 🔐 Logging in with: admin@clinic.com
- ✅ Login response: {token, id, ...}

### 3. اختبر الحسابات:

```
Admin
─────────────────
Email: admin@clinic.com
Pass:  admin123

Doctor
─────────────────
Email: dr.ahmed@clinic.com
Pass:  doctor123

Patient
─────────────────
Email: patient@clinic.com
Pass:  patient123
```

---

## 🔧 ملفات تم تعديلها

| الملف | الوصف |
|------|-------|
| `pom.xml` | إضافة maven-compiler-plugin مع Lombok |
| `DataInitializer.java` | إضافة تسجيلات وتحسين إنشاء البيانات |
| `AuthService.java` | تسجيلات تفصيلية للمصادقة |
| `SecurityConfig.java` | إضافة تسجيلات في UserDetailsService و ObjectProvider |
| `GlobalExceptionHandler.java` | معالج استثناءات محسّن |
| `JwtService.java` | تسجيلات لتوليد وتحقق من الرموز |
| `application.yml` | إضافة logging configuration |
| `Login.jsx` | تحسين معالجة الأخطاء والتسجيل |

---

## 🚀 الخطوة الأخيرة: تشغيل كامل التطبيق

```bash
# Backend
cd clinic-booking-system/clinic/backend
mvn clean compile
mvn spring-boot:run

# Frontend (في terminal آخر)
cd clinic-booking-system/clinic/frontend
npm install
npm run dev
```

**ثم افتح:** http://localhost:3000/login

---

## 💡 نصائح إضافية

1. **Clear Browser Cache:**
   - Ctrl + Shift + Delete
   - اختر "All time"
   - احذف Cookies والـ Cache

2. **تحقق من JWT Secret:**
   ```yaml
   app:
     jwt:
       secret: 404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
   ```

3. **تحقق من Database URL:**
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/clinic_db?createDatabaseIfNotExist=true
   ```

4. **شغل MySQL كـ Service:**
   ```bash
   # Windows
   net start MySQL80  # أو اسم service MySQL لديك
   
   # Linux
   sudo systemctl start mysql
   
   # Mac
   mysql.server start
   ```

---

## ✨ الآن يجب أن يعمل!

إذا كانت لديك مشاكل:
1. تحقق من Browser Console (F12)
2. تحقق من Backend Console للـ logs
3. استخدم cURL لاختبار API مباشرة
4. تأكد من أن MySQL يعمل

---

**Last Updated:** 2026-02-20  
**Status:** ✅ كل المشاكل تم حلها
