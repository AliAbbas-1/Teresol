package com.name.library.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.name.library.dto.BookDto;
import com.name.library.dto.LendingDto;
import com.name.library.services.BookService;
import com.name.library.services.LendingService;
import com.name.library.services.MemberService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LendingServiceImpl implements LendingService {

    @Inject
    BookService bookService;
    
    @Inject
    MemberService memberService;

    private final Map<String, LendingDto> lendings = new ConcurrentHashMap<>();

    @Override
    public LendingDto lendBook(String bookId, String memberId) {
        // Find the book and check for availability
        Optional<BookDto> bookOptional = bookService.getBookById(bookId);
        if (bookOptional.isEmpty() || !bookOptional.get().available) {
            // Handle the case where the book is not found or not available
            throw new RuntimeException("Book not found or not available for lending.");
        }

        // Check if the member exists
        if (memberService.getMemberById(memberId).isEmpty()) {
            throw new RuntimeException("Member not found.");
        }

        // Mark the book as unavailable
        bookService.markBookAsUnavailable(bookId);

        String newLendingId = UUID.randomUUID().toString();
        LendingDto newLending = new LendingDto();
        newLending.lendingid = newLendingId;
        newLending.bookid = bookId;
        newLending.memberid = memberId;
        newLending.lendingDate = LocalDateTime.now().toString();

        lendings.put(newLendingId, newLending);
        
        return newLending;
    }

    @Override
    public Optional<LendingDto> returnBook(String lendingId) {
        Optional<LendingDto> lending = Optional.ofNullable(lendings.get(lendingId));
        lending.ifPresent(l -> {
            l.returnDate = LocalDateTime.now().toString();
            // Mark the book as available again
            bookService.markBookAsAvailable(l.bookid);
        });
        return lending;
    }

    @Override
    public List<LendingDto> getAllLendings() {
        return lendings.values().stream().collect(Collectors.toList());
    }
    
    @Override
    public Optional<LendingDto> getLendingById(String id) {
        return Optional.ofNullable(lendings.get(id));
    }
}