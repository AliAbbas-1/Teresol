package com.name.library.controllers;

import com.name.library.api.ApiFactory;
import com.name.library.dtos.BookCreateRequestDTO;
import com.name.library.dtos.BookResponseDTO;
import com.name.library.dtos.BookUpdateRequestDTO;
import com.name.library.services.BookService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.util.List;
import java.util.UUID;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookController {

  @Inject BookService bookService;

  @GET
  @Operation(
      summary = "Get all books",
      description = "Returns all books, or 204 No Content if none")
  @APIResponses({
    @APIResponse(responseCode = "200", description = "List of books returned"),
    @APIResponse(responseCode = "204", description = "No books found")
  })
  public Response getAllBooks() {
    List<BookResponseDTO> books = bookService.getAllBooks();
    return ApiFactory.success(Response.Status.OK.getStatusCode(), books);
  }

  @GET
  @Path("/{id}")
  public Response getBook(@PathParam("id") UUID id) {
    return ApiFactory.success(Response.Status.OK.getStatusCode(), bookService.getBook(id));
  }

  @POST
  @Operation(summary = "Add a new book", description = "Creates a new book entry")
  @APIResponse(responseCode = "201", description = "Book successfully created")
  public Response addBook(BookCreateRequestDTO newBook) {
    bookService.addBook(newBook);
    return ApiFactory.success(Response.Status.CREATED.getStatusCode(), );
  }

  @PUT    
  @Path("/{id}")
  @Operation(
      summary = "Update an existing book",
      description = "Updates book details for the given UUID")
  @APIResponses({
    @APIResponse(responseCode = "204", description = "Book updated successfully"),
    @APIResponse(responseCode = "404", description = "Book not found")
  })
  public Response updateBook(@PathParam("id") UUID id, BookUpdateRequestDTO updatedBook) {
    bookService.updateBook(id, updatedBook);
    return Response.noContent().build();
  }

  @DELETE
  @Path("/{id}")
  @Operation(summary = "Delete a book", description = "Deletes the book with the given UUID")
  @APIResponses({
    @APIResponse(responseCode = "204", description = "Book deleted successfully"),
    @APIResponse(responseCode = "404", description = "Book not found")
  })
  public Response deleteBook(@PathParam("id") UUID id) {
    bookService.deleteBook(id);
    return Response.noContent().build();
  }
}
