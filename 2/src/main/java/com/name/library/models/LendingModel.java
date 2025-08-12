package com.name.library.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public class LendingModel {

  public String lendingId;
  public String bookId;
  public String memberId;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
  public Instant lendingDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
  public Instant returnDate;

  public LendingModel(String bookId, String memberId) {
    // creating this object means a book has been lent
    this.lendingId = IdModel.generate(IdModel.Type.LEND);

    this.bookId = bookId;
    this.memberId = memberId;

    this.lendingDate = Instant.now();
    this.returnDate = null;
    // returnDate set when book is returned
  }
}
