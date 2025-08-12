package com.name.library.repositories;

import com.name.library.models.BookModel;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class BookRepository {

  List<BookModel> bookModels = new ArrayList<>();

  public Optional<BookModel> getBook(String bookId) {
    return bookModels.stream().filter(bookModel -> bookModel.id.equals(bookId)).findFirst();
  }

  public List<BookModel> getAllBooks() {
    return bookModels;
  }

  public BookModel addBook(BookModel newBookModel) {
    bookModels.add(newBookModel);
    return newBookModel;
  }

  public boolean deleteBook(String bookId) {
    return bookModels.removeIf(bookModel -> bookModel.id.equals(bookId));
  }
}
