package com.name.library.exceptions.mappers;

import com.name.library.api.ApiResponseFactory;

import com.name.library.api.ApiError;
import com.name.library.exceptions.AlreadyReturnedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class AlreadyReturnedExceptionMapper implements ExceptionMapper<AlreadyReturnedException> {

  @Override
  public Response toResponse(AlreadyReturnedException e) {
    return ApiResponseFactory.failure(
        Response.Status.CONFLICT.getStatusCode(),
        new ApiError(new String[] {"BOOK_ALREADY_RETURNED"}, e.getMessage()));
  }
}
