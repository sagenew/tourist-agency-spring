package com.touristagency.dto;

import com.touristagency.entity.enums.Authority;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.Set;

@Data
public class UpdateUserDTO {
    private long id;

    @NotBlank(message = "{validation.user.username.not_blank}")
    @Size(min = 5, max = 39, message = "{validation.user.username.size}")
    private String username;

    private String password;

    @NotBlank(message = "{validation.user.full_name.not_blank}")
    @Size(max = 50, message = "{validation.user.full_name.size}")
    private String fullName;

    @NotBlank(message = "{validation.user.email.not_blank}")
    @Email(message = "{validation.user.email.invalid}")
    private String email;

    @Size(max = 500, message = "{validation.user.bio.size}")
    private String bio;

    @Size(min = 1, message = "{validation.user.authorities.size}")
    private Set<Authority> authorities;

    @Min(value = 0, message = "LESS THAN 0")
    @Max(value = 100, message = "GREATER THAN 100")
    private double currentDiscount;
}
