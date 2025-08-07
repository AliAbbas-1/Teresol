package com.name.library.resources;

import java.util.List;
import java.util.Optional;

import com.name.library.dto.LendingDto;
import com.name.library.services.LendingService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/lending")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LendingResource {

    @Inject
    LendingService lendingService;

    @POST
    @Path("/lend/{bookId}/{memberId}")
    public Response lendBook(@PathParam("bookId") String bookId, @PathParam("memberId") String memberId) {
        try {
            LendingDto newLending = lendingService.lendBook(bookId, memberId);
            return Response.status(Response.Status.CREATED).entity(newLending).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/return/{lendingId}")
    public Response returnBook(@PathParam("lendingId") String lendingId) {
        Optional<LendingDto> returnedLending = lendingService.returnBook(lendingId);
        if (returnedLending.isPresent()) {
            return Response.ok(returnedLending.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    public List<LendingDto> getAllLendings() {
        return lendingService.getAllLendings();
    }

    @GET
    @Path("/{id}")
    public Response getLendingById(@PathParam("id") String id) {
        return lendingService.getLendingById(id)
                .map(lending -> Response.ok(lending).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}