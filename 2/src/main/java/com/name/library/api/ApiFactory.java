package com.name.library.api;

import com.name.library.api.responses.FailureResponse;
import com.name.library.api.responses.SuccessResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class ApiFactory {

  public static <T> Response success(int code, T data) {
    return Response.status(code)
        .entity(new SuccessResponse<>(data))
        .type(MediaType.APPLICATION_JSON)
        .build();
  }

  public static Response failure(int code, String error) {
    return Response.status(code)
        .entity(new FailureResponse(error))
        .type(MediaType.APPLICATION_JSON)
        .build();
  }
}
