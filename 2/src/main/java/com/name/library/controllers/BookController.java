package com.name.library.controllers;

import com.name.library.dtos.BookCreateRequestDTO;
import com.name.library.dtos.BookResponseDTO;
import com.name.library.dtos.BookUpdateRequestDTO;
import com.name.library.services.BookService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookController {
    @Inject
    BookService bookService;

    @GET
    public Response getAllBooks() {
        List<BookResponseDTO> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            return Response.noContent().build();  // 204 No Content
        }
        return Response.ok(books).build();
    }

    @GET
    @Path("/{id}")
    public Response getBook(UUID id) {
        return Response.ok(bookService.getBook(id)).build();
    }

    @POST
    public Response addBook(BookCreateRequestDTO newBook) {
        bookService.addBook(newBook);

        return Response.ok().build();
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
