package com.name.library.dtos;

import java.util.Date;

public class BookCreateRequest {
    public String isbn;
    public String title;
    public String author;
    public Date publicationDate;

    public BookCreateRequest(String isbn,
                             String title,
                             String author,
                             Date publicationDate
    ) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publicationDate = publicationDate;
    }
}
