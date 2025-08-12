package com.name.library.services;

import com.name.library.dtos.LendingRequestDTO;
import com.name.library.dtos.LendingResponseDTO;
import com.name.library.dtos.LendingReturnDTO;
import com.name.library.exceptions.*;
import com.name.library.models.BookModel;
import com.name.library.models.LendingModel;
import com.name.library.models.MemberModel;
import com.name.library.repositories.BookRepository;
import com.name.library.repositories.LendingRepository;
import com.name.library.repositories.MemberRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class LendingService {

  @Inject LendingRepository lendingRepository;

  @Inject BookRepository bookRepository;

  @Inject MemberRepository memberRepository;

  public List<LendingResponseDTO> getLendingHistory() {
    return lendingRepository.getAllLendingModels().stream()
        .map(
            lendingModel ->
                new LendingResponseDTO(
                    lendingModel.lendingId,
                    lendingModel.bookId,
                    lendingModel.memberId,
                    lendingModel.lendingDate,
                    lendingModel.returnDate))
        .collect(Collectors.toList());
  }

  public void lendBook(LendingRequestDTO lendingRequestDTO) {
    String bookId = lendingRequestDTO.bookId();
    String memberId = lendingRequestDTO.memberId();

    BookModel bookModel =
        bookRepository
            .getBook(bookId)
            .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + bookId));

    MemberModel memberModel =
        memberRepository
            .getMember(memberId)
            .orElseThrow(
                () -> new MemberNotFoundException("Member not found with ID: " + memberId));

    if (!bookModel.available) {
      throw new AlreadyLentException("Book with ID " + bookId + " is already lent");
    }

    memberModel.borrowedBookModels.add(bookId);
    bookModel.available = false;

    lendingRepository.addLendingModel(new LendingModel(bookId, memberId));
  }

  public void returnBook(LendingReturnDTO lendingReturnDTO) {
    String lendingId = lendingReturnDTO.lendingId();

    LendingModel lendingModel =
        lendingRepository
            .getLendingModel(lendingId)
            .orElseThrow(
                () -> new LendingNotFoundException("Lending Log not found with ID: " + lendingId));

    if (lendingModel.returnDate != null) {
      throw new AlreadyReturnedException(
          "Lending Log of ID: " + lendingId + " has already been resolved");
    }

    lendingModel.returnDate = Instant.now();

    BookModel bookModel =
        bookRepository
            .getBook(lendingModel.bookId)
            .orElseThrow(
                () -> new BookNotFoundException("Book not found with ID: " + lendingModel.bookId));

    MemberModel memberModel =
        memberRepository
            .getMember(lendingModel.memberId)
            .orElseThrow(
                () ->
                    new MemberNotFoundException(
                        "Member not found with ID: " + lendingModel.memberId));

    memberModel.borrowedBookModels.removeIf(bookId -> bookId.equals(lendingModel.bookId));

    bookModel.available = true;
  }
}
