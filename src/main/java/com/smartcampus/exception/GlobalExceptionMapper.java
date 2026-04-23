package com.smartcampus.exception;

import com.smartcampus.model.ErrorMessage;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable exception) {
        LOGGER.severe("Unexpected error: " + exception.getMessage());
        ErrorMessage errorMessage = new ErrorMessage(
            "An unexpected internal server error occurred",
            500,
            "https://smartcampus.com/api/docs/errors"
        );
        return Response.status(500)
                .type(MediaType.APPLICATION_JSON)
                .entity(errorMessage)
                .build();
    }
}