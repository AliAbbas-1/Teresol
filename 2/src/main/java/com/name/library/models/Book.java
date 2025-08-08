package com.name.library.models;

import java.util.Date;
import java.util.UUID;

public class Book {
    public UUID id;
    public String isbn;
    public String title;
    public String author;
    public Date publicationDate;
    public boolean available;
    public String lentTo;
    public Date createdAt;
    public Date updatedAt;

    public Book(String isbn,
                String title,
                String author,
                Date publicationDate
    ) {
        this.id = UUID.randomUUID();

        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publicationDate = publicationDate;

        this.available = true; // new books are always available
        this.lentTo = null; // new books have no borrower
        this.createdAt = new Date();
        this.updatedAt = this.createdAt; // problem??
    }
}
