package com.name.library.exceptions.mappers;

import com.name.library.api.ApiResponseFactory;
import com.name.library.api.ApiError;
import com.name.library.exceptions.BookNotFoundException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BookNotFoundExceptionMapper implements ExceptionMapper<BookNotFoundException> {

  @Override
  public Response toResponse(BookNotFoundException e) {
    return ApiResponseFactory.failure(
        Response.Status.NOT_FOUND.getStatusCode(),
        new ApiError(new String[] {"BOOK_NOT_FOUND"}, e.getMessage()));
  }
}
