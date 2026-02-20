package com.mostafa.clinic.repository;

import com.mostafa.clinic.entity.DoctorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, Long> {
    Optional<DoctorProfile> findByUserId(Long userId);
    List<DoctorProfile> findByAvailableTrue();
    List<DoctorProfile> findBySpecialtyContainingIgnoreCase(String specialty);
}
