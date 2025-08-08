package com.name.library.dtos;

import jakarta.annotation.Nullable;

import java.util.Date;
import java.util.UUID;

public record LendingResponseDTO (
        UUID lendingId,
        UUID bookId,
        UUID memberId,
        Date lendingDate,
        @Nullable Date returnDate
) {}