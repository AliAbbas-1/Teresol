package com.name.library.dtos;

import jakarta.annotation.Nullable;

import java.util.Date;

public class BookUpdateRequest {
    @Nullable
    public String isbn;

    @Nullable
    public String title;

    @Nullable
    public String author;

    @Nullable
    public Date publicationDate;

    public BookUpdateRequest(@Nullable String isbn,
                             @Nullable String title,
                             @Nullable String author,
                             @Nullable Date publicationDate
    ) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publicationDate = publicationDate;
    }
}
