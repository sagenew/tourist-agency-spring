package com.touristagency.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegisterUserDTO {
    @NotBlank(message = "{validation.user.username.not_blank}")
    @Size(min = 5, max = 39, message = "{validation.user.username.size}")
    private String username;

    @NotBlank(message = "{validation.user.password.not_blank}")
    @Size(min = 5, max = 39, message = "{validation.user.password.size}")
    private String password;

    @NotBlank(message = "{validation.user.full_name.not_blank}")
    @Size(max = 50, message = "{validation.user.full_name.size}")
    private String fullName;

    @NotBlank(message = "{validation.user.email.not_blank}")
    @Email(message = "{validation.user.email.invalid}")
    private String email;

    @Size(max = 500, message = "{validation.user.bio.size}")
    private String bio;
}
