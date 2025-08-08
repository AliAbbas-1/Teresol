package com.name.library.dtos;

import java.util.UUID;
import java.util.Date;


public class BookResponse {
    public UUID id;
    public String title;
    public String author;
    public Date publicationDate;
    public boolean available;

    public BookResponse(UUID id,
                        String title,
                        String author,
                        Date publicationDate,
                        boolean available
    ) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publicationDate = publicationDate;
        this.available = available;
    }
}
