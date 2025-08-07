package com.name.library.services;

import java.util.List;
import java.util.Optional;

import com.name.library.dto.LendingDto;

public interface LendingService {

    LendingDto lendBook(String bookId, String memberId);

    Optional<LendingDto> returnBook(String lendingId);

    List<LendingDto> getAllLendings();

    Optional<LendingDto> getLendingById(String id);
}
