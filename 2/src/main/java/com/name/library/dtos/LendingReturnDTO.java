package com.name.library.dtos;

import jakarta.validation.constraints.NotBlank;

public record LendingReturnDTO(@NotBlank String lendingId) {}
