package com.name.library.controllers;

import com.name.library.api.ApiResponseFactory;
import com.name.library.dtos.MemberRequestDTO;
import com.name.library.dtos.MemberResponseDTO;
import com.name.library.services.MemberService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;

import java.util.List;

@Path("/members")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MemberController {

  @Inject MemberService memberService;

  @GET
  @Operation(summary = "Get all members", description = "Returns a list of all members")
  public Response getAllMembers() {
    List<MemberResponseDTO> members = memberService.getAllMembers();
    return ApiResponseFactory.success(
        Response.Status.OK.getStatusCode(), "Success", memberService.getAllMembers());
  }

  @POST
  public Response addMember(@Valid MemberRequestDTO newMember) {

    return ApiResponseFactory.success(
        Response.Status.CREATED.getStatusCode(),
        "Book added successfully",
        memberService.addMember(newMember));
  }
}
