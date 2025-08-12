package com.name.library.exceptions.mappers;

import com.name.library.api.ApiFactory;
import com.name.library.exceptions.BookNotFoundException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BookNotFoundExceptionMapper implements ExceptionMapper<BookNotFoundException> {
  @Override
  public Response toResponse(BookNotFoundException e) {
    return ApiFactory.failure(Response.Status.NOT_FOUND.getStatusCode(), e.getMessage());
  }
}
