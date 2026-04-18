package com.smartcampus.mapper;

import com.smartcampus.exception.ErrorResponse;
import com.smartcampus.exception.SensorUnavailableException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {
    
    @Override
    public Response toResponse(SensorUnavailableException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Sensor unavailable",
                exception.getMessage(),
                503
        );
        return Response.status(503)
                .type(MediaType.APPLICATION_JSON)
                .entity(errorResponse)
                .build();
    }
}
