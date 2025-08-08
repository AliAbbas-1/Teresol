package com.name.library.controllers;

import com.name.library.dtos.LendingRequestDTO;
import com.name.library.dtos.LendingResponseDTO;
import com.name.library.dtos.LendingReturnDTO;
import com.name.library.services.LendingService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.util.List;

@Path("/lending")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LendingController {

    @Inject
    LendingService lendingService;

    @GET
    @Path("/history")
    @Operation(summary = "Get lending history", description = "Returns the full lending history")
    @APIResponse(responseCode = "200", description = "Lending history returned")
    @APIResponse(responseCode = "204", description = "No lending history found")
    public Response getLendingHistory() {
        List<LendingResponseDTO> history = lendingService.getLendingHistory();
        if (history.isEmpty()) {
            return Response.noContent().build();
        }
        return Response.ok(history).build();
    }

    @POST
    @Operation(summary = "Lend a book to a member", description = "Lends a book if available")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Book lent successfully"),
        @APIResponse(responseCode = "404", description = "Book or member not found"),
        @APIResponse(responseCode = "409", description = "Book is already lent")
    })
    public Response lendBook(LendingRequestDTO lendingRequestDTO) {
        lendingService.lendBook(lendingRequestDTO.bookId(), lendingRequestDTO.memberId());
        return Response.ok().build();
    }

    @POST
    @Path("/returns")
    @Operation(summary = "Return a lent book", description = "Returns a book based on lending ID")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Book returned successfully"),
            @APIResponse(responseCode = "404", description = "Lending record, book or member not found"),
            @APIResponse(responseCode = "409", description = "Book was already returned")
    })
    public Response returnBook(LendingReturnDTO lendingReturnDTO) {
        lendingService.returnBook(lendingReturnDTO);
        return Response.ok().build();
    }
}
