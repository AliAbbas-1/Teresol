package com.name.library.dtos;

import jakarta.annotation.Nullable;

import java.util.Date;

@Nullable
public record BookUpdateRequestDTO(
    String isbn, String title, String author, Date publicationDate) {}
