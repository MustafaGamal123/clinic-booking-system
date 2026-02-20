package com.mostafa.clinic.service;

import com.mostafa.clinic.dto.request.LoginRequest;
import com.mostafa.clinic.dto.request.RegisterRequest;
import com.mostafa.clinic.dto.response.AuthResponse;
import com.mostafa.clinic.entity.DoctorProfile;
import com.mostafa.clinic.entity.Role;
import com.mostafa.clinic.entity.User;
import com.mostafa.clinic.exception.BusinessException;
import com.mostafa.clinic.repository.DoctorProfileRepository;
import com.mostafa.clinic.repository.UserRepository;
import com.mostafa.clinic.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        log.info("📝 Register attempt for email: {} as role: {}", request.getEmail(), request.getRole());
        
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("❌ Email already registered: {}", request.getEmail());
            throw new BusinessException("Email is already registered: " + request.getEmail());
        }

        if (request.getRole() == Role.ADMIN) {
            log.error("❌ Attempt to register as ADMIN which is not allowed");
            throw new BusinessException("Cannot register as ADMIN");
        }

        if (request.getRole() == Role.DOCTOR && (request.getSpecialty() == null || request.getSpecialty().isBlank())) {
            log.warn("❌ Doctor registration without specialty: {}", request.getEmail());
            throw new BusinessException("Specialty is required for doctors");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(request.getRole())
                .build();

        user = userRepository.save(user);
        log.info("✅ User saved to database: {} with id: {}", user.getEmail(), user.getId());

        if (request.getRole() == Role.DOCTOR) {
            DoctorProfile profile = DoctorProfile.builder()
                    .user(user)
                    .specialty(request.getSpecialty())
                    .bio(request.getBio())
                    .experienceYears(request.getExperienceYears() != null ? request.getExperienceYears() : 0)
                    .consultationFee(request.getConsultationFee() != null
                            ? BigDecimal.valueOf(request.getConsultationFee()) : BigDecimal.ZERO)
                    .workingDays(request.getWorkingDays() != null ? request.getWorkingDays() : "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY")
                    .workingHours(request.getWorkingHours() != null ? request.getWorkingHours() : "09:00-17:00")
                    .build();
            doctorProfileRepository.save(profile);
            log.info("✅ Doctor profile created for user: {}", user.getEmail());
        }

        String token = jwtService.generateToken(user);
        log.info("✅ Registration successful for user: {} with role: {}", user.getEmail(), user.getRole());
        return buildAuthResponse(user, token);
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.getEmail();
        
        log.info("🔐 Login attempt for email: {}", email);
        
        // First check if user exists in database
        boolean userExists = userRepository.existsByEmail(email);
        if (!userExists) {
            log.warn("❌ User not found in database: {}", email);
            throw new BusinessException("Invalid email or password");
        }
        
        log.info("✅ User found in database: {}", email);
        
        try {
            log.debug("🔑 Attempting authentication with UsernamePasswordAuthenticationToken...");
            var auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, request.getPassword()));
            log.info("✅ Authentication successful for user: {}", email);
        } catch (BadCredentialsException e) {
            log.warn("❌ Authentication failed for {}: {}", email, e.getMessage());
            throw new BusinessException("Invalid email or password");
        } catch (Exception e) {
            log.error("❌ Unexpected authentication error: {}", e.getMessage(), e);
            throw new BusinessException("Authentication failed: " + e.getMessage());
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("User not found"));

        log.info("✅ Generating JWT token for user: {}", email);
        String token = jwtService.generateToken(user);
        log.info("✅ Login successful for user: {} with role: {}", email, user.getRole());
        return buildAuthResponse(user, token);
    }

    private AuthResponse buildAuthResponse(User user, String token) {
        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
