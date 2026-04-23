package com.smartcampus.exception;

import com.smartcampus.model.ErrorMessage;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {

    @Override
    public Response toResponse(SensorUnavailableException exception) {
        ErrorMessage errorMessage = new ErrorMessage(
            exception.getMessage(),
            403,
            "https://smartcampus.com/api/docs/errors"
        );
        return Response.status(403)
                .type(MediaType.APPLICATION_JSON)
                .entity(errorMessage)
                .build();
    }
}