package com.name.library.controllers;

import com.name.library.api.ApiResponseFactory;
import com.name.library.dtos.LendingRequestDTO;
import com.name.library.dtos.LendingReturnDTO;
import com.name.library.services.LendingService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/lending")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LendingController {

  @Inject LendingService lendingService;

  @GET
  public Response getLendingHistory() {
    return ApiResponseFactory.success(
        Response.Status.OK.getStatusCode(), "Success", lendingService.getLendingHistory());
  }

  @POST
  public Response lendBook(@Valid LendingRequestDTO lendingRequestDTO) {
    lendingService.lendBook(lendingRequestDTO);
    return ApiResponseFactory.success(
        Response.Status.OK.getStatusCode(), "Successfully lent book", null);
  }

  @PUT
  public Response returnBook(@Valid LendingReturnDTO lendingReturnDTO) {
    lendingService.returnBook(lendingReturnDTO);
    return ApiResponseFactory.success(
        Response.Status.OK.getStatusCode(), "Successfully returned book", null);
  }
}
