package com.name.library.controllers;

import com.name.library.api.ApiResponseFactory;
import com.name.library.dtos.BookCreateRequestDTO;
import com.name.library.dtos.BookUpdateRequestDTO;
import com.name.library.dtos.BookResponseDTO;
import com.name.library.services.BookService;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookController {

  @Inject BookService bookService;

  @GET
  public Response getAllBooks() {
    List<BookResponseDTO> books = bookService.getAllBooks();
    return ApiResponseFactory.success(Response.Status.OK.getStatusCode(), "Success", books);
  }

  @GET
  @Path("/{id}")
  public Response getBook(@PathParam("id") String id) {
    return ApiResponseFactory.success(
        Response.Status.OK.getStatusCode(), "Success", bookService.getBook(id));
  }

  @POST
  public Response addBook(@Valid BookCreateRequestDTO newBook) {
    return ApiResponseFactory.success(
        Response.Status.CREATED.getStatusCode(),
        "Book added successfully",
        bookService.addBook(newBook));
  }

  @PUT
  @Path("/{id}")
  public Response updateBook(@PathParam("id") String id, BookUpdateRequestDTO updatedBook) {
    bookService.updateBook(id, updatedBook);
    return ApiResponseFactory.success(
        Response.Status.OK.getStatusCode(), "Book updated successfully", null);
  }

  @DELETE
  @Path("/{id}")
  public Response deleteBook(@PathParam("id") String id) {
    bookService.deleteBook(id);
    return ApiResponseFactory.success(
        Response.Status.OK.getStatusCode(), "Book deleted successfully", null);
  }
}
