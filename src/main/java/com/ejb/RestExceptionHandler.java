package com.ejb;

import com.ejb.exception.global.RestApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class RestExceptionHandler implements ExceptionMapper<RestApplicationException> {

    @Override
    public Response toResponse(RestApplicationException exception) {

        return Response.status(Response.Status.BAD_REQUEST).entity(exception.getMessage()).build();
    }

}