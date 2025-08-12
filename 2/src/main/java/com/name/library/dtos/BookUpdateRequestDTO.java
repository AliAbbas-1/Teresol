package com.name.library.dtos;

import jakarta.annotation.Nullable;

public record BookUpdateRequestDTO(
    @Nullable String isbn, @Nullable String title, @Nullable String author) {}
