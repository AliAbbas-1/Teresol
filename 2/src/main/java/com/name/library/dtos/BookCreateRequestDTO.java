package com.name.library.dtos;

import jakarta.validation.constraints.NotBlank;

public record BookCreateRequestDTO(
    @NotBlank String isbn, @NotBlank String title, @NotBlank String author) {}
