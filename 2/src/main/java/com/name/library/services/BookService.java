package com.name.library.services;

import java.util.List;
import java.util.Optional;

import com.name.library.dto.BookDto;

public interface BookService {

    List<BookDto> getAllBooks();
    
    Optional<BookDto> getBookById(String id);

    BookDto addBook(BookDto bookDto);

    Optional<BookDto> updateBook(String id, BookDto bookDto);

    void deleteBook(String id);
    
    void markBookAsAvailable(String id);
    
    void markBookAsUnavailable(String id);
}