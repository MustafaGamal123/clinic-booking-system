package com.mostafa.clinic.dto.response;

import com.mostafa.clinic.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String type;
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
}
