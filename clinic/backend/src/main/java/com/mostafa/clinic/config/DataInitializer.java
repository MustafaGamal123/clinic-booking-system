package com.mostafa.clinic.config;

import com.mostafa.clinic.entity.DoctorProfile;
import com.mostafa.clinic.entity.Role;
import com.mostafa.clinic.entity.User;
import com.mostafa.clinic.repository.DoctorProfileRepository;
import com.mostafa.clinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        try {
            if (userRepository.count() > 0) {
                log.info("✅ Database already seeded with {} users. Skipping initialization.", userRepository.count());
                return;
            }

            log.info("🔄 Starting database seeding with default accounts...");

        // ── Admin ──────────────────────────────────────────────────────
        User admin = userRepository.save(User.builder()
                .firstName("Admin")
                .lastName("Clinic")
                .email("admin@clinic.com")
                .password(passwordEncoder.encode("admin123"))
                .phone("01000000000")
                .role(Role.ADMIN)
                .build());

        // ── Doctors ────────────────────────────────────────────────────
        User drAhmed = userRepository.save(User.builder()
                .firstName("Ahmed")
                .lastName("Hassan")
                .email("dr.ahmed@clinic.com")
                .password(passwordEncoder.encode("doctor123"))
                .phone("01011111111")
                .role(Role.DOCTOR)
                .build());

        doctorProfileRepository.save(DoctorProfile.builder()
                .user(drAhmed)
                .specialty("Cardiology")
                .bio("Experienced cardiologist with 10+ years in interventional cardiology.")
                .experienceYears(10)
                .consultationFee(new BigDecimal("350.00"))
                .workingDays("MONDAY,TUESDAY,WEDNESDAY,THURSDAY")
                .workingHours("09:00-17:00")
                .build());

        User drSara = userRepository.save(User.builder()
                .firstName("Sara")
                .lastName("Mahmoud")
                .email("dr.sara@clinic.com")
                .password(passwordEncoder.encode("doctor123"))
                .phone("01022222222")
                .role(Role.DOCTOR)
                .build());

        doctorProfileRepository.save(DoctorProfile.builder()
                .user(drSara)
                .specialty("Dermatology")
                .bio("Board-certified dermatologist specializing in skin conditions and cosmetic procedures.")
                .experienceYears(7)
                .consultationFee(new BigDecimal("280.00"))
                .workingDays("SUNDAY,MONDAY,WEDNESDAY,FRIDAY")
                .workingHours("10:00-18:00")
                .build());

        User drKhaled = userRepository.save(User.builder()
                .firstName("Khaled")
                .lastName("Ali")
                .email("dr.khaled@clinic.com")
                .password(passwordEncoder.encode("doctor123"))
                .phone("01033333333")
                .role(Role.DOCTOR)
                .build());

        doctorProfileRepository.save(DoctorProfile.builder()
                .user(drKhaled)
                .specialty("Orthopedics")
                .bio("Orthopedic surgeon with expertise in sports injuries and joint replacement.")
                .experienceYears(12)
                .consultationFee(new BigDecimal("400.00"))
                .workingDays("SATURDAY,SUNDAY,MONDAY,TUESDAY")
                .workingHours("08:00-16:00")
                .build());

        // ── Sample Patient ─────────────────────────────────────────────
        userRepository.save(User.builder()
                .firstName("Mohamed")
                .lastName("Gamal")
                .email("patient@clinic.com")
                .password(passwordEncoder.encode("patient123"))
                .phone("01044444444")
                .role(Role.PATIENT)
                .build());

            log.info("✅ Database seeded successfully!");
            log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            log.info("📋 Default Accounts Created:");
            log.info("   👤 Admin    → admin@clinic.com    / admin123");
            log.info("   🏥 Doctor   → dr.ahmed@clinic.com / doctor123");
            log.info("   🏥 Doctor   → dr.sara@clinic.com  / doctor123");
            log.info("   🏥 Doctor   → dr.khaled@clinic.com / doctor123");
            log.info("   👥 Patient  → patient@clinic.com  / patient123");
            log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            log.info("Total users in database: {}", userRepository.count());
        } catch (Exception e) {
            log.error("❌ Error during database initialization: {}", e.getMessage(), e);
            throw e;
        }
    }
}
