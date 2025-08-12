package com.name.library.exceptions.mappers;

import com.name.library.api.ApiFactory;

import com.name.library.exceptions.AlreadyReturnedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public class AlreadyReturnedExceptionMapper implements ExceptionMapper<AlreadyReturnedException> {
  @Override
  public Response toResponse(AlreadyReturnedException e) {
    return ApiFactory.failure(Response.Status.CONFLICT.getStatusCode(), e.getMessage());
  }
}
