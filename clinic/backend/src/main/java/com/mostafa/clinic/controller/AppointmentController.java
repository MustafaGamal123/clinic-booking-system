package com.mostafa.clinic.controller;

import com.mostafa.clinic.dto.request.AppointmentRequest;
import com.mostafa.clinic.dto.response.AppointmentResponse;
import com.mostafa.clinic.entity.User;
import com.mostafa.clinic.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointments", description = "Appointment booking and management")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/book")
    @Operation(summary = "Book a new appointment (Patient only)")
    public ResponseEntity<AppointmentResponse> book(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody AppointmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(appointmentService.bookAppointment(currentUser.getId(), request));
    }

    @PutMapping("/confirm/{id}")
    @Operation(summary = "Confirm an appointment (Doctor only)")
    public ResponseEntity<AppointmentResponse> confirm(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.confirmAppointment(currentUser.getId(), id));
    }

    @PutMapping("/reject/{id}")
    @Operation(summary = "Reject/Cancel an appointment (Doctor only)")
    public ResponseEntity<AppointmentResponse> reject(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {
        String notes = body != null ? body.get("doctorNotes") : null;
        return ResponseEntity.ok(appointmentService.rejectAppointment(currentUser.getId(), id, notes));
    }

    @PutMapping("/complete/{id}")
    @Operation(summary = "Mark appointment as completed (Doctor only)")
    public ResponseEntity<AppointmentResponse> complete(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {
        String notes = body != null ? body.get("doctorNotes") : null;
        return ResponseEntity.ok(appointmentService.completeAppointment(currentUser.getId(), id, notes));
    }

    @PutMapping("/cancel/{id}")
    @Operation(summary = "Cancel an appointment (Patient or Doctor)")
    public ResponseEntity<AppointmentResponse> cancel(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(currentUser.getId(), id));
    }

    @GetMapping("/my")
    @Operation(summary = "Get current user's appointments (Patient: own, Doctor: assigned)")
    public ResponseEntity<List<AppointmentResponse>> getMyAppointments(
            @AuthenticationPrincipal User currentUser) {
        if (currentUser.getRole().name().equals("DOCTOR")) {
            return ResponseEntity.ok(appointmentService.getDoctorAppointments(currentUser.getId()));
        }
        return ResponseEntity.ok(appointmentService.getPatientAppointments(currentUser.getId()));
    }
}
