package com.name.library.api;

import jakarta.annotation.Nullable;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class ApiResponseFactory {

  public static <T> Response success(int code, String message, @Nullable T data) {
    return Response.status(code)
        .entity(new ApiResponse<>(message, data, null))
        .type(MediaType.APPLICATION_JSON)
        .build();
  }

  public static Response failure(int code, ApiError error) {
    return Response.status(code)
        .entity(new ApiResponse<>("Failure", null, error))
        .type(MediaType.APPLICATION_JSON)
        .build();
  }
}
