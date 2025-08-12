package com.name.library.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public class BookModel {

  public String id;
  public String isbn;
  public String title;
  public String author;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
  public Instant createdAt;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
  public Instant updatedAt;

  public boolean available;
  public String lentTo;

  public BookModel(String isbn, String title, String author) {
    this.id = IdModel.generate(IdModel.Type.BOOK);

    this.isbn = isbn;
    this.title = title;
    this.author = author;

    this.createdAt = Instant.now();
    this.updatedAt = this.createdAt; // problem??

    this.available = true; // new books are always available
    this.lentTo = null; // new books have no borrower
  }
}
