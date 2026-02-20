package com.mostafa.clinic.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDoctorProfileRequest {
    private String specialty;
    private String bio;
    private Integer experienceYears;
    private Double consultationFee;
    private String workingDays;
    private String workingHours;
}
