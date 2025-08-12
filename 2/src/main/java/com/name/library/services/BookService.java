package com.name.library.services;

import com.name.library.dtos.BookCreateRequestDTO;
import com.name.library.dtos.BookUpdateRequestDTO;
import com.name.library.dtos.BookResponseDTO;
import com.name.library.exceptions.BookNotFoundException;
import com.name.library.models.BookModel;
import com.name.library.repositories.BookRepository;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class BookService {

  @Inject BookRepository bookRepository;

  @PostConstruct
  void initDummyBooks() {
    List<BookCreateRequestDTO> dummyBooks =
        List.of(
            new BookCreateRequestDTO("978-0132350884", "Clean Code", "R. Martin"),
            new BookCreateRequestDTO("978-0201633610", "Design Patterns", "E. Gamma"),
            new BookCreateRequestDTO(
                "978-0131103627", "The C Programming Language", "B. Kernighan"),
            new BookCreateRequestDTO("978-0134685991", "Effective Java", "J. Bloch"),
            new BookCreateRequestDTO("978-0596007126", "Head First Design Patterns", "E. Freeman"),
            new BookCreateRequestDTO("978-1491950357", "Refactoring", "M. Fowler"),
            new BookCreateRequestDTO(
                "978-0596517748", "JavaScript: The Good Parts", "D. Crockford"),
            new BookCreateRequestDTO("978-0131177055", "Introduction to Algorithms", "T. Cormen"),
            new BookCreateRequestDTO("978-0262033848", "Artificial Intelligence", "S. Russell"),
            new BookCreateRequestDTO(
                "978-0135974445",
                "Structure and Interpretation of Computer Programs",
                "H. Abelson"));

    for (BookCreateRequestDTO dto : dummyBooks) {
      addBook(dto);
    }
  }

  public BookResponseDTO getBook(String bookId) {
    return bookRepository
        .getBook(bookId)
        .map(
            bookModel ->
                new BookResponseDTO(
                    bookModel.id,
                    bookModel.isbn,
                    bookModel.title,
                    bookModel.author,
                    bookModel.available))
        .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + bookId));
  }

  public List<BookResponseDTO> getAllBooks() {
    return bookRepository.getAllBooks().stream()
        .map(
            bookModel ->
                new BookResponseDTO(
                    bookModel.id,
                    bookModel.isbn,
                    bookModel.title,
                    bookModel.author,
                    bookModel.available))
        .collect(Collectors.toList());
  }

  public BookResponseDTO addBook(BookCreateRequestDTO newBook) {
    BookModel newBookModel =
        bookRepository.addBook(new BookModel(newBook.isbn(), newBook.title(), newBook.author()));

    return new BookResponseDTO(
        newBookModel.id,
        newBookModel.isbn,
        newBookModel.title,
        newBookModel.author,
        newBookModel.available);
  }

  public void updateBook(String bookId, BookUpdateRequestDTO updatedBook) {

    bookRepository
        .getBook(bookId)
        .ifPresentOrElse(
            bookModel -> {
              if (!(updatedBook.isbn() == null || updatedBook.isbn().isBlank())) {
                bookModel.isbn = updatedBook.isbn();
              }

              if (!(updatedBook.title() == null || updatedBook.title().isBlank())) {
                bookModel.title = updatedBook.title();
              }

              if (!(updatedBook.author() == null || updatedBook.author().isBlank())) {
                bookModel.author = updatedBook.author();
              }

              bookModel.updatedAt = Instant.now();
            },
            () -> {
              throw new BookNotFoundException("Book not found with ID: " + bookId);
            });
  }

  public void deleteBook(String bookId) {
    if (!bookRepository.deleteBook(bookId)) {
      throw new BookNotFoundException("Book not found with ID: " + bookId);
    }
  }
}
