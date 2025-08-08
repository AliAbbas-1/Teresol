package com.name.library.repositories;

import com.name.library.models.BookModel;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class BookRepository {
    List<BookModel> bookModels = new ArrayList<>();

    public Optional<BookModel> getBook(UUID bookId) {
        return bookModels.stream()
                .filter(bookModel -> bookModel.id.equals(bookId))
                .findFirst();
    }

    public List<BookModel> getAllBooks() {
        return bookModels;
    }

    public void addBook(BookModel newBookModel) {
        bookModels.add(newBookModel);
    }

    public boolean deleteBook(UUID bookId) {
        return bookModels.removeIf(bookModel -> bookModel.id.equals(bookId));
    }
}