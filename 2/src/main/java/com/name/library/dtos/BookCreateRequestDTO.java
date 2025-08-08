package com.name.library.dtos;

import java.util.Date;

public record BookCreateRequestDTO(
        String isbn,
        String title,
        String author,
        Date publicationDate
) {}