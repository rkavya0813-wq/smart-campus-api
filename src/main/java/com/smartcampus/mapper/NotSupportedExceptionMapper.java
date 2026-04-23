package com.smartcampus.mapper;

import com.smartcampus.exception.ErrorResponse;
import jakarta.ws.rs.NotSupportedException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NotSupportedExceptionMapper implements ExceptionMapper<NotSupportedException> {

    @Override
    public Response toResponse(NotSupportedException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Unsupported media type. Please send application/json",
                Response.Status.UNSUPPORTED_MEDIA_TYPE.getStatusCode()
        );
        return Response.status(Response.Status.UNSUPPORTED_MEDIA_TYPE)
                .type(MediaType.APPLICATION_JSON)
                .entity(errorResponse)
                .build();
    }
}
