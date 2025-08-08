package com.name.library.services;

import com.name.library.dtos.BookCreateRequestDTO;
import com.name.library.dtos.BookResponseDTO;
import com.name.library.dtos.BookUpdateRequestDTO;
import com.name.library.exceptions.BookNotFoundException;
import com.name.library.models.BookModel;
import com.name.library.repositories.BookRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.awt.print.Book;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class BookService {
    @Inject
    BookRepository bookRepository;

    public BookResponseDTO getBook(UUID bookId) {
        return bookRepository.getBook(bookId)
                .map(bookModel -> new BookResponseDTO(
                        bookModel.id,
                        bookModel.title,
                        bookModel.author,
                        bookModel.publicationDate,
                        bookModel.available
                ))
                .orElseThrow(() -> new BookNotFoundException(
                        "Book not found with ID: " + bookId
                ));
    }

    public List<BookResponseDTO> getAllBooks() {
        return bookRepository.getAllBooks().stream()
                .map(bookModel -> new BookResponseDTO(
                        bookModel.id,
                        bookModel.title,
                        bookModel.author,
                        bookModel.publicationDate,
                        bookModel.available
                ))
                .collect(Collectors.toList());
    }

    public void addBook(BookCreateRequestDTO newBook) {
        bookRepository.addBook(new BookModel(
                newBook.isbn(),
                newBook.title(),
                newBook.author(),
                newBook.publicationDate()
        ));
    }

    public void updateBook(UUID bookId, BookUpdateRequestDTO updatedBook) {
        bookRepository.getBook(bookId).ifPresentOrElse(bookModel -> {
            if (updatedBook.isbn() != null) {
                bookModel.isbn = updatedBook.isbn();
            }

            if (updatedBook.title() != null) {
                bookModel.title = updatedBook.title();
            }

            if (updatedBook.author() != null) {
                bookModel.author = updatedBook.author();
            }

            if (updatedBook.publicationDate() != null) {
                bookModel.publicationDate = updatedBook.publicationDate();
            }

            bookModel.updatedAt = new Date();
        }, () -> {
            throw new BookNotFoundException(
                    "Book not found with ID: " + bookId
            );
        });
    }

    public void deleteBook(UUID bookId) {
        if (!bookRepository.deleteBook(bookId)) {
            throw new BookNotFoundException(
                    "Book not found with ID: " + bookId
            );
        }
    }
}