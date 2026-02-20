package com.mostafa.clinic.service;

import com.mostafa.clinic.dto.request.AppointmentRequest;
import com.mostafa.clinic.dto.response.AppointmentResponse;
import com.mostafa.clinic.entity.*;
import com.mostafa.clinic.exception.BusinessException;
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
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final EmailService emailService;

    public AppointmentResponse bookAppointment(Long patientId, AppointmentRequest request) {
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        User doctor = userRepository.findById(request.getDoctorId())
                .filter(u -> u.getRole() == Role.DOCTOR)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        DoctorProfile profile = doctorProfileRepository.findByUserId(doctor.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found"));

        if (!profile.isAvailable()) {
            throw new BusinessException("This doctor is currently not accepting appointments");
        }

        boolean slotTaken = appointmentRepository.existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndStatusNot(
                doctor.getId(), request.getAppointmentDate(), request.getAppointmentTime(), AppointmentStatus.CANCELLED);

        if (slotTaken) {
            throw new BusinessException("This time slot is already booked. Please choose another time.");
        }

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDate(request.getAppointmentDate())
                .appointmentTime(request.getAppointmentTime())
                .patientNotes(request.getPatientNotes())
                .status(AppointmentStatus.PENDING)
                .build();

        appointment = appointmentRepository.save(appointment);
        emailService.sendAppointmentBooked(appointment);
        return mapToResponse(appointment);
    }

    public AppointmentResponse confirmAppointment(Long doctorId, Long appointmentId) {
        Appointment appointment = getAppointmentForDoctor(doctorId, appointmentId);

        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new BusinessException("Only PENDING appointments can be confirmed");
        }

        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointment = appointmentRepository.save(appointment);
        emailService.sendAppointmentConfirmation(appointment);
        return mapToResponse(appointment);
    }

    public AppointmentResponse rejectAppointment(Long doctorId, Long appointmentId, String doctorNotes) {
        Appointment appointment = getAppointmentForDoctor(doctorId, appointmentId);

        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new BusinessException("Only PENDING appointments can be rejected");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setDoctorNotes(doctorNotes);
        appointment = appointmentRepository.save(appointment);
        emailService.sendAppointmentCancellation(appointment, "the doctor");
        return mapToResponse(appointment);
    }

    public AppointmentResponse completeAppointment(Long doctorId, Long appointmentId, String doctorNotes) {
        Appointment appointment = getAppointmentForDoctor(doctorId, appointmentId);

        if (appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new BusinessException("Only CONFIRMED appointments can be completed");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointment.setDoctorNotes(doctorNotes);
        appointment = appointmentRepository.save(appointment);
        return mapToResponse(appointment);
    }

    public AppointmentResponse cancelAppointment(Long userId, Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        boolean isPatient = appointment.getPatient().getId().equals(userId);
        boolean isDoctor = appointment.getDoctor().getId().equals(userId);

        if (!isPatient && !isDoctor) {
            throw new BusinessException("You are not authorized to cancel this appointment");
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED || appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new BusinessException("This appointment cannot be cancelled");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment = appointmentRepository.save(appointment);
        emailService.sendAppointmentCancellation(appointment, isPatient ? "the patient" : "the doctor");
        return mapToResponse(appointment);
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getPatientAppointments(Long patientId) {
        return appointmentRepository.findByPatientIdOrderByAppointmentDateDescAppointmentTimeDesc(patientId)
                .stream().map(this::mapToResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getDoctorAppointments(Long doctorId) {
        return appointmentRepository.findByDoctorIdOrderByAppointmentDateAscAppointmentTimeAsc(doctorId)
                .stream().map(this::mapToResponse).toList();
    }

    private Appointment getAppointmentForDoctor(Long doctorId, Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        if (!appointment.getDoctor().getId().equals(doctorId)) {
            throw new BusinessException("You are not authorized to manage this appointment");
        }
        return appointment;
    }

    public AppointmentResponse mapToResponse(Appointment a) {
        DoctorProfile profile = doctorProfileRepository.findByUserId(a.getDoctor().getId()).orElse(null);
        return AppointmentResponse.builder()
                .id(a.getId())
                .patientId(a.getPatient().getId())
                .patientName(a.getPatient().getFullName())
                .patientEmail(a.getPatient().getEmail())
                .doctorId(a.getDoctor().getId())
                .doctorName(a.getDoctor().getFullName())
                .doctorEmail(a.getDoctor().getEmail())
                .specialty(profile != null ? profile.getSpecialty() : null)
                .appointmentDate(a.getAppointmentDate())
                .appointmentTime(a.getAppointmentTime())
                .status(a.getStatus())
                .patientNotes(a.getPatientNotes())
                .doctorNotes(a.getDoctorNotes())
                .createdAt(a.getCreatedAt())
                .build();
    }
}
