package com.name.library.resources;

// import java.lang.reflect.MemberDto;

// import com.name.library.dto.BookDto;
import java.util.List;

import com.name.library.dto.MemberDto;
import com.name.library.services.MemberService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/members")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MemberResource {

    @Inject
    MemberService memberService;

    @GET
    public List<MemberDto> getAllMembers() {
        return memberService.getAllMembers();
    }
     @GET
    @Path("/{id}")
    public Response getMemberById(@PathParam("id") String id) {
        return memberService.getMemberById(id)
                .map(member -> Response.ok(member).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
    @POST
    public Response addMember(MemberDto memberDto) {
        MemberDto newMember = memberService.addMember(memberDto);
        return Response.status(Response.Status.CREATED).entity(newMember).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateMember(@PathParam("id") String id, MemberDto memberDto) {
        return memberService.updateMember(id, memberDto)
                .map(updatedMember -> Response.ok(updatedMember).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    public Response deleteMember(@PathParam("id") String id) {
        memberService.deleteMember(id);
        return Response.noContent().build();
    }
    
}
