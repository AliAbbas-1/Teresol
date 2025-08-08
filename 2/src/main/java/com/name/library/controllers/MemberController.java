package com.name.library.controllers;

import com.name.library.dtos.MemberCreateRequestDTO;
import com.name.library.dtos.MemberResponseDTO;
import com.name.library.services.MemberService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/members")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MemberController {
    @Inject
    MemberService memberService;

    @GET
    public List<MemberResponseDTO> getAllMembers() {
        return memberService.getAllMembers();
    }

    @POST
    public void addMember(MemberCreateRequestDTO newMember) {
        memberService.addMember(newMember);
    }

}
