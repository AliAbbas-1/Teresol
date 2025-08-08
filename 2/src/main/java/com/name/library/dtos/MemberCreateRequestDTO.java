package com.name.library.dtos;

public record MemberCreateRequestDTO (
        String name,
        String email,
        String password
) {}

