package com.name.library.controllers;

import com.name.library.dtos.LendingRequestDTO;
import com.name.library.dtos.LendingResponseDTO;
import com.name.library.dtos.LendingReturnDTO;
import com.name.library.services.LendingService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.UUID;

@Path("/lending")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LendingController {
    @Inject
    LendingService lendingService;

    @GET
    @Path("/history")
    public List<LendingResponseDTO> getLendingHistory() {
        return lendingService.getLendingHistory();
    }

    @POST
    public void lendBook(LendingRequestDTO lendingRequestDTO) {
        lendingService.lendBook(
                lendingRequestDTO.bookId(),
                lendingRequestDTO.memberId()
        );
    }

    @POST
    @Path("/returns")
    public void returnBook(LendingReturnDTO lendingReturnDTO) {
        lendingService.returnBook(lendingReturnDTO);
    }
}
