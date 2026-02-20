package com.mostafa.clinic.controller;

import com.mostafa.clinic.dto.request.UpdateDoctorProfileRequest;
import com.mostafa.clinic.dto.response.DoctorResponse;
import com.mostafa.clinic.entity.User;
import com.mostafa.clinic.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Tag(name = "Doctors", description = "Doctor browsing and profile management")
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    @Operation(summary = "Get all doctors")
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/available")
    @Operation(summary = "Get available doctors")
    public ResponseEntity<List<DoctorResponse>> getAvailableDoctors() {
        return ResponseEntity.ok(doctorService.getAvailableDoctors());
    }

    @GetMapping("/search")
    @Operation(summary = "Search doctors by specialty")
    public ResponseEntity<List<DoctorResponse>> searchDoctors(@RequestParam String specialty) {
        return ResponseEntity.ok(doctorService.searchDoctors(specialty));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get doctor by ID")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @PutMapping("/profile")
    @Operation(summary = "Update doctor's own profile (Doctor only)")
    public ResponseEntity<DoctorResponse> updateMyProfile(
            @AuthenticationPrincipal User currentUser,
            @RequestBody UpdateDoctorProfileRequest request) {
        return ResponseEntity.ok(doctorService.updateProfile(currentUser.getId(), request));
    }
}
