package com.name.library.models;

import java.util.Date;
import java.util.UUID;

public class LendingModel {
    public UUID lendingId;
    public UUID bookId;
    public UUID memberId;
    public Date lendingDate;
    public Date returnDate;

    public LendingModel(UUID bookId, UUID memberId) {
        // creating this object means a book has been lent
        this.lendingId = UUID.randomUUID();

        this.bookId = bookId;
        this.memberId = memberId;

        this.lendingDate = new Date();
        this.returnDate = null;
        // returnDate set when book is returned
    }
}
