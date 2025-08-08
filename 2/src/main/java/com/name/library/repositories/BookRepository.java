package com.name.library.repositories;

import com.name.library.models.Book;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class BookRepository {
    List<Book> books;

    public Optional<Book> getBook(UUID targetId) {
        return books.stream()
                .filter(book -> book.id.equals(targetId))
                .findFirst();
    }

    public List<Book> getAllBooks() {
        return books;
    }

    public void addBook(Book newBook) {
        books.add(newBook);
    }

    public boolean deleteBook(UUID targetId) {
        return books.removeIf(book -> book.id == targetId);
    }
}