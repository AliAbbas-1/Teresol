package com.name.library.services;

import com.name.library.dtos.BookCreateRequest;
import com.name.library.dtos.BookResponse;
import com.name.library.dtos.BookUpdateRequest;
import com.name.library.models.Book;
import com.name.library.repositories.BookRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class BookService {
    @Inject
    BookRepository bookRepository;

    public BookResponse getBook(UUID targetId) {
        return bookRepository.getBook(targetId)
                .map(book -> new BookResponse(
                        book.id,
                        book.title,
                        book.author,
                        book.publicationDate,
                        book.available
                ))
                .orElseThrow(() -> new RuntimeException(
                        "Book not found with ID: " + targetId
                ));
    }

    public List<BookResponse> getAllBooks() {
        return bookRepository.getAllBooks().stream()
                .map(book -> new BookResponse(
                        book.id,
                        book.title,
                        book.author,
                        book.publicationDate,
                        book.available
                ))
                .collect(Collectors.toList());
    }

    public void addBook(BookCreateRequest newBook) {
        bookRepository.addBook(new Book(
                newBook.isbn,
                newBook.title,
                newBook.author,
                newBook.publicationDate
        ));
    }

    public void updateBook(UUID targetId, BookUpdateRequest updatedBook) {
        bookRepository.getBook(targetId).ifPresentOrElse(book -> {
            if (updatedBook.isbn != null) {
                book.isbn = updatedBook.isbn;
            }

            if (updatedBook.title != null) {
                book.title = updatedBook.title;
            }

            if (updatedBook.author != null) {
                book.author = updatedBook.author;
            }

            if (updatedBook.publicationDate != null) {
                book.publicationDate = updatedBook.publicationDate;
            }

            book.updatedAt = new Date();
        }, () -> {
            throw new RuntimeException(
                    "Book not found with ID: " + targetId
            );
        });
    }

    public void deleteBook(UUID targetId) {
        if (!bookRepository.deleteBook(targetId)) {
            throw new RuntimeException(
                    "Book not found with ID: " + targetId
            );
        }
    }
}