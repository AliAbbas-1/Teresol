package com.name.library.controllers;

import com.name.library.dtos.MemberCreateRequestDTO;
import com.name.library.dtos.MemberResponseDTO;
import com.name.library.services.MemberService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.util.List;

@Path("/members")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MemberController {

    @Inject
    MemberService memberService;

    @GET
    @Operation(summary = "Get all members", description = "Returns a list of all members")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "List of members returned"),
            @APIResponse(responseCode = "204", description = "No members found")}
    )
    public Response getAllMembers() {
        List<MemberResponseDTO> members = memberService.getAllMembers();
        if (members.isEmpty()) {
            return Response.noContent().build();
        }
        return Response.ok(members).build();
    }

    @POST
    @Operation(summary = "Add a new member", description = "Creates a new member")
    @APIResponse(responseCode = "201", description = "Member successfully created")
    public Response addMember(MemberCreateRequestDTO newMember) {
        memberService.addMember(newMember);
        return Response.status(Response.Status.CREATED).build();
    }
}
