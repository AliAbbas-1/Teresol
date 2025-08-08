package com.name.library.dtos;

import java.util.UUID;
import java.util.Date;


public record BookResponseDTO (
        UUID id,
        String title,
        String author,
        Date publicationDate,
        boolean available
) {}