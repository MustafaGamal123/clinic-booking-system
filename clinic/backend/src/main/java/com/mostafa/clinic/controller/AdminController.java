package com.mostafa.clinic.controller;

import com.mostafa.clinic.dto.response.AdminStatsResponse;
import com.mostafa.clinic.dto.response.AppointmentResponse;
import com.mostafa.clinic.dto.response.DoctorResponse;
import com.mostafa.clinic.dto.response.UserResponse;
import com.mostafa.clinic.entity.User;
import com.mostafa.clinic.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Admin-only endpoints for dashboard and management")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/stats")
    @Operation(summary = "Get dashboard statistics")
    public ResponseEntity<AdminStatsResponse> getStats() {
        return ResponseEntity.ok(adminService.getStats());
    }

    @GetMapping("/users")
    @Operation(summary = "Get all users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/doctors")
    @Operation(summary = "Get all doctors with profiles")
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {
        return ResponseEntity.ok(adminService.getAllDoctors());
    }

    @GetMapping("/appointments")
    @Operation(summary = "Get all appointments")
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments() {
        return ResponseEntity.ok(adminService.getAllAppointments());
    }

    @PutMapping("/doctors/{id}/toggle-availability")
    @Operation(summary = "Toggle doctor availability")
    public ResponseEntity<Void> toggleDoctorAvailability(@PathVariable Long id) {
        adminService.toggleDoctorAvailability(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{id}/toggle-active")
    @Operation(summary = "Toggle user active status")
    public ResponseEntity<Void> toggleUserActive(@PathVariable Long id) {
        adminService.toggleUserActive(id);
        return ResponseEntity.ok().build();
    }
}
