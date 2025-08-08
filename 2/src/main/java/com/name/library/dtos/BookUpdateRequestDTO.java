package com.name.library.dtos;

import jakarta.annotation.Nullable;

import java.util.Date;

public record BookUpdateRequestDTO(
        @Nullable String isbn,
        @Nullable String title,
        @Nullable String author,
        @Nullable Date publicationDate
) {}