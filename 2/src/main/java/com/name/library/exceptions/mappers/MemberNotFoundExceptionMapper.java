package com.name.library.exceptions.mappers;

import com.name.library.api.ApiResponseFactory;
import com.name.library.api.ApiError;
import com.name.library.exceptions.MemberNotFoundException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class MemberNotFoundExceptionMapper implements ExceptionMapper<MemberNotFoundException> {

  @Override
  public Response toResponse(MemberNotFoundException e) {
    return ApiResponseFactory.failure(
        Response.Status.NOT_FOUND.getStatusCode(),
        new ApiError(new String[] {"MEMBER_NOT_FOUND"}, e.getMessage()));
  }
}
