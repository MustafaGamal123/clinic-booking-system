package com.mostafa.clinic.service;

import com.mostafa.clinic.entity.Appointment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @Async
    public void sendAppointmentConfirmation(Appointment appointment) {
        log.info("[EMAIL] CONFIRMED → {} | Doctor: {} | Date: {} {}",
            appointment.getPatient().getEmail(),
            appointment.getDoctor().getFullName(),
            appointment.getAppointmentDate(),
            appointment.getAppointmentTime());
    }

    @Async
    public void sendAppointmentCancellation(Appointment appointment, String cancelledBy) {
        log.info("[EMAIL] CANCELLED by {} → {} | Doctor: {} | Date: {} {}",
            cancelledBy,
            appointment.getPatient().getEmail(),
            appointment.getDoctor().getFullName(),
            appointment.getAppointmentDate(),
            appointment.getAppointmentTime());
    }

    @Async
    public void sendAppointmentBooked(Appointment appointment) {
        log.info("[EMAIL] NEW BOOKING → Doctor: {} | Patient: {} | Date: {} {}",
            appointment.getDoctor().getEmail(),
            appointment.getPatient().getFullName(),
            appointment.getAppointmentDate(),
            appointment.getAppointmentTime());
    }
}
