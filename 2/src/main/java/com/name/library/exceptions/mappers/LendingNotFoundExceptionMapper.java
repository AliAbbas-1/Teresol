package com.name.library.exceptions.mappers;

import com.name.library.api.ApiFactory;
import com.name.library.exceptions.LendingNotFoundException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class LendingNotFoundExceptionMapper implements ExceptionMapper<LendingNotFoundException> {
  @Override
  public Response toResponse(LendingNotFoundException e) {
    return ApiFactory.failure(Response.Status.NOT_FOUND.getStatusCode(), e.getMessage());
  }
}
