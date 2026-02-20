package com.mostafa.clinic.dto.request;

import com.mostafa.clinic.entity.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank @Email
    private String email;

    @NotBlank @Size(min = 6)
    private String password;

    private String phone;

    @NotNull
    private Role role;

    // Doctor-specific (only when role = DOCTOR)
    private String specialty;
    private String bio;
    private Integer experienceYears;
    private Double consultationFee;
    private String workingDays;
    private String workingHours;
}
