package com.name.library.dtos;

import java.util.UUID;

public record LendingRequestDTO (
        UUID bookId,
        UUID memberId
) {}