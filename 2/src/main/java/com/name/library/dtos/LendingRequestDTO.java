package com.name.library.dtos;

import jakarta.validation.constraints.NotBlank;

public record LendingRequestDTO(@NotBlank String bookId, @NotBlank String memberId) {}
