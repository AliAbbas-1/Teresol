package com.name.library.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberRequestDTO(
    @NotBlank String name, @NotBlank @Email(message = "Invalid email format") String email) {}
