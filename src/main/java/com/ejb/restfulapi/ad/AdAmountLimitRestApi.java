package com.ejb.restfulapi.ad;

import com.ejb.restfulapi.OfsApiResponse;
import com.ejb.restfulapi.ad.models.AmountLimitRequest;
import com.ejb.txnapi.ad.AdAmountLimitApiController;
import com.util.EJBCommonAPIErrCodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@RequestScoped
@Path("/amountlimit")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class AdAmountLimitRestApi {

    @Context
    private SecurityContext securityContext;

    @Inject
    private AdAmountLimitApiController adAmountLimitApiController;

    @POST
    @Path("/add")
    @RolesAllowed({"Admin"})
    @Operation(summary = "Add amount limit details", description = "This can only be done by an authorized user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Not Found")
    })
    public Response addAmountLimit(@Parameter(description = "Created amount limit request object", required = true) AmountLimitRequest request) {

        OfsApiResponse response = new OfsApiResponse();
        try {
            response = adAmountLimitApiController.saveAmountLimit(request);
            return Response.status(response.getCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ? Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response.toString())
                    .build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response.toString()).build();
        }
    }

}