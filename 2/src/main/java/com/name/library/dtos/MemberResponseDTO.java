package com.name.library.dtos;

import java.util.UUID;

public record MemberResponseDTO (
        UUID id,
        String name,
        String email
) {}