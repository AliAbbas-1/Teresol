package com.name.library.exceptions.mappers;

import com.name.library.api.ApiResponseFactory;
import com.name.library.api.ApiError;
import com.name.library.exceptions.AlreadyLentException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class AlreadyLentExceptionMapper implements ExceptionMapper<AlreadyLentException> {

  @Override
  public Response toResponse(AlreadyLentException e) {
    return ApiResponseFactory.failure(
        Response.Status.CONFLICT.getStatusCode(),
        new ApiError(new String[] {"BOOK_ALREADY_LENT"}, e.getMessage()));
  }
}
