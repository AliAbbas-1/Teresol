package com.name.library.controllers;

import com.name.library.dtos.BookCreateRequestDTO;
import com.name.library.dtos.BookResponseDTO;
import com.name.library.dtos.BookUpdateRequestDTO;
import com.name.library.services.BookService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.UUID;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookController {
    @Inject
    BookService bookService;

    @GET
    public List<BookResponseDTO> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GET
    @Path("/{id}")
    public BookResponseDTO getBook(UUID id) {
        return bookService.getBook(id);
    }

    @POST
    public void addBook(BookCreateRequestDTO newBook) {
        bookService.addBook(newBook);
    }

    @PUT
    @Path("/{id}")
    public void updateBook(UUID id, BookUpdateRequestDTO updatedBook) {
        bookService.updateBook(id, updatedBook);
    }

    @DELETE
    @Path("/{id}")
    public void deleteBook(UUID id) {
        bookService.deleteBook(id);
    }
}
