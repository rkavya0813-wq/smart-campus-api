package com.smartcampus.mapper;

import com.smartcampus.exception.ErrorResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Internal server error",
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()
        );
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(errorResponse)
                .build();
    }
}
