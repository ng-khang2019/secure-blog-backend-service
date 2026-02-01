package com.project.prjauth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data @Builder
@AllArgsConstructor @NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRegistrationRequest {

    @Size(min = 2, message = "First name must be at least 2 characters long")
    @NotBlank(message = "First name cannot be blank")
    String firstName;

    @Size(min = 2, message = "Last name must be at least 2 characters long")
    @NotBlank(message = "Last name cannot be blank")
    String lastName;

    @Size(min = 4, message = "Username must be at least 4 characters long")
    @NotBlank(message = "Username cannot be blank")
    String username;

    @Size(min = 6, message = "Password must be at least 6 characters long")
    @NotBlank(message = "Password cannot be blank")
    String password;

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email cannot be blank")
    String email;

    @NotBlank(message = "Phone cannot be blank")
    String phone;
}
