package com.name.library.dtos;

public record BookResponseDTO(
    String id, String isbn, String title, String author, boolean available) {}
