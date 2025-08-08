package com.name.library.exceptions.mappers;

import com.name.library.exceptions.BookNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public class AlreadyLentExceptionMapper implements ExceptionMapper<BookNotFoundException> {
    @Override
    public Response toResponse(BookNotFoundException e) {
        return Response.status(Response.Status.CONFLICT)
                .entity(Map.of("error", e.getMessage()))
                .build();
    }
}
