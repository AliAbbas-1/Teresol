package com.name.library.services.impl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map; // <-- Use 'jakarta' for newer Quarkus versions
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.name.library.dto.BookDto;
import com.name.library.services.BookService;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped // <-- This annotation is crucial for CDI to work
public class BookServiceImpl implements BookService {

    private final Map<String, BookDto> books = new ConcurrentHashMap<>();

    @Override
    public List<BookDto> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    @Override
    public Optional<BookDto> getBookById(String id) {
        return Optional.ofNullable(books.get(id));
    }

    @Override
    public BookDto addBook(BookDto bookDto) {
        String newId = UUID.randomUUID().toString();
        bookDto.id = newId;
        bookDto.available = true;
        books.put(newId, bookDto);
        return bookDto;
    }

    @Override
    public Optional<BookDto> updateBook(String id, BookDto bookDto) {
        if (books.containsKey(id)) {
            bookDto.id = id;
            books.put(id, bookDto);
            return Optional.of(bookDto);
        }
        return Optional.empty();
    }

    @Override
    public void deleteBook(String id) {
        books.remove(id);
    }
    
    @Override
    public void markBookAsAvailable(String id) {
        Optional.ofNullable(books.get(id))
                .ifPresent(book -> book.available = true);
    }
    
    @Override
    public void markBookAsUnavailable(String id) {
        Optional.ofNullable(books.get(id))
                .ifPresent(book -> book.available = false);
    }
}