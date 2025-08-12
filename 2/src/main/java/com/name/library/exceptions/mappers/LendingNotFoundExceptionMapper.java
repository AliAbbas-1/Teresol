package com.name.library.exceptions.mappers;

import com.name.library.api.ApiResponseFactory;
import com.name.library.api.ApiError;
import com.name.library.exceptions.LendingNotFoundException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class LendingNotFoundExceptionMapper implements ExceptionMapper<LendingNotFoundException> {

  @Override
  public Response toResponse(LendingNotFoundException e) {
    return ApiResponseFactory.failure(
        Response.Status.NOT_FOUND.getStatusCode(),
        new ApiError(new String[] {"LENDING_NOT_FOUND"}, e.getMessage()));
  }
}
