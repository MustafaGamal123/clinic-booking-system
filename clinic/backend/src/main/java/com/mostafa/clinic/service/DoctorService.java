package com.mostafa.clinic.service;

import com.mostafa.clinic.dto.request.UpdateDoctorProfileRequest;
import com.mostafa.clinic.dto.response.DoctorResponse;
import com.mostafa.clinic.entity.DoctorProfile;
import com.mostafa.clinic.entity.Role;
import com.mostafa.clinic.entity.User;
import com.mostafa.clinic.exception.ResourceNotFoundException;
import com.mostafa.clinic.repository.DoctorProfileRepository;
import com.mostafa.clinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DoctorService {

    private final UserRepository userRepository;
    private final DoctorProfileRepository doctorProfileRepository;

    public List<DoctorResponse> getAllDoctors() {
        return userRepository.findByRole(Role.DOCTOR).stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<DoctorResponse> getAvailableDoctors() {
        return doctorProfileRepository.findByAvailableTrue().stream()
                .map(p -> mapToResponse(p.getUser()))
                .toList();
    }

    public List<DoctorResponse> searchDoctors(String specialty) {
        return doctorProfileRepository.findBySpecialtyContainingIgnoreCase(specialty).stream()
                .map(p -> mapToResponse(p.getUser()))
                .toList();
    }

    public DoctorResponse getDoctorById(Long doctorId) {
        User doctor = userRepository.findById(doctorId)
                .filter(u -> u.getRole() == Role.DOCTOR)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));
        return mapToResponse(doctor);
    }

    @Transactional
    public DoctorResponse updateProfile(Long userId, UpdateDoctorProfileRequest request) {
        DoctorProfile profile = doctorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found"));

        if (request.getSpecialty() != null) profile.setSpecialty(request.getSpecialty());
        if (request.getBio() != null) profile.setBio(request.getBio());
        if (request.getExperienceYears() != null) profile.setExperienceYears(request.getExperienceYears());
        if (request.getConsultationFee() != null) profile.setConsultationFee(BigDecimal.valueOf(request.getConsultationFee()));
        if (request.getWorkingDays() != null) profile.setWorkingDays(request.getWorkingDays());
        if (request.getWorkingHours() != null) profile.setWorkingHours(request.getWorkingHours());

        doctorProfileRepository.save(profile);
        return mapToResponse(profile.getUser());
    }

    public DoctorResponse mapToResponse(User doctor) {
        DoctorProfile profile = doctorProfileRepository.findByUserId(doctor.getId()).orElse(null);
        return DoctorResponse.builder()
                .id(doctor.getId())
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .email(doctor.getEmail())
                .phone(doctor.getPhone())
                .specialty(profile != null ? profile.getSpecialty() : null)
                .bio(profile != null ? profile.getBio() : null)
                .experienceYears(profile != null ? profile.getExperienceYears() : 0)
                .consultationFee(profile != null ? profile.getConsultationFee() : BigDecimal.ZERO)
                .workingDays(profile != null ? profile.getWorkingDays() : null)
                .workingHours(profile != null ? profile.getWorkingHours() : null)
                .available(profile != null && profile.isAvailable())
                .build();
    }
}
