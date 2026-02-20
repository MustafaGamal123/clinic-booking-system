package com.mostafa.clinic.service;

import com.mostafa.clinic.dto.response.AdminStatsResponse;
import com.mostafa.clinic.dto.response.AppointmentResponse;
import com.mostafa.clinic.dto.response.DoctorResponse;
import com.mostafa.clinic.dto.response.UserResponse;
import com.mostafa.clinic.entity.AppointmentStatus;
import com.mostafa.clinic.entity.Role;
import com.mostafa.clinic.entity.User;
import com.mostafa.clinic.exception.ResourceNotFoundException;
import com.mostafa.clinic.repository.AppointmentRepository;
import com.mostafa.clinic.repository.DoctorProfileRepository;
import com.mostafa.clinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;

    public AdminStatsResponse getStats() {
        return AdminStatsResponse.builder()
                .totalPatients(userRepository.countByRole(Role.PATIENT))
                .totalDoctors(userRepository.countByRole(Role.DOCTOR))
                .totalAppointments(appointmentRepository.count())
                .pendingAppointments(appointmentRepository.countByStatus(AppointmentStatus.PENDING))
                .confirmedAppointments(appointmentRepository.countByStatus(AppointmentStatus.CONFIRMED))
                .completedAppointments(appointmentRepository.countByStatus(AppointmentStatus.COMPLETED))
                .cancelledAppointments(appointmentRepository.countByStatus(AppointmentStatus.CANCELLED))
                .build();
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .role(user.getRole())
                        .active(user.isActive())
                        .createdAt(user.getCreatedAt())
                        .build())
                .toList();
    }

    public List<DoctorResponse> getAllDoctors() {
        return userRepository.findByRole(Role.DOCTOR).stream()
                .map(doctorService::mapToResponse).toList();
    }

    public List<AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAllOrderByCreatedAtDesc().stream()
                .map(appointmentService::mapToResponse).toList();
    }

    @Transactional
    public void toggleDoctorAvailability(Long doctorId) {
        var profile = doctorProfileRepository.findByUserId(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found"));
        profile.setAvailable(!profile.isAvailable());
        doctorProfileRepository.save(profile);
    }

    @Transactional
    public void toggleUserActive(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setActive(!user.isActive());
        userRepository.save(user);
    }
}
