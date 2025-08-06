package com.name.library.resources;

import java.util.List;

import com.name.library.dto.BookDto;
import com.name.library.services.BookService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    @Inject
    BookService bookService;

    @GET
    public List<BookDto> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GET
    @Path("/{id}")
    public Response getBookById(@PathParam("id") String id) {
        return bookService.getBookById(id)
                .map(book -> Response.ok(book).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response addBook(BookDto bookDto) {
        BookDto newBook = bookService.addBook(bookDto);
        return Response.status(Response.Status.CREATED).entity(newBook).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") String id, BookDto bookDto) {
        return bookService.updateBook(id, bookDto)
                .map(updatedBook -> Response.ok(updatedBook).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") String id) {
        bookService.deleteBook(id);
        return Response.noContent().build();
    }
}