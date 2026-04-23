package com.smartcampus.exception;

import com.smartcampus.model.ErrorMessage;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException exception) {
        ErrorMessage errorMessage = new ErrorMessage(
            exception.getMessage(),
            409,
            "https://smartcampus.com/api/docs/errors"
        );
        return Response.status(409)
                .type(MediaType.APPLICATION_JSON)
                .entity(errorMessage)
                .build();
    }
}