package com.ejb.restfulapi.ad;

import com.ejb.restfulapi.OfsApiResponse;
import com.ejb.restfulapi.ad.models.UserDetailsRequest;
import com.ejb.txnapi.ad.AdAuthenticationApiController;
import com.ejb.txnapi.ad.AdKeyGeneratorController;
import com.util.EJBCommonAPIErrCodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.Map;

@RequestScoped
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class AdAuthenticationRestApi {

    @Context
    private SecurityContext securityContext;

    @Inject
    private AdAuthenticationApiController adAuthenticationApiController;

    @Inject
    private AdKeyGeneratorController adKeyGeneratorController;

    @POST
    @Path("/user")
    @RolesAllowed({"Admin"})
    @Operation(summary = "Validate user details", description = "This can only be done by an authorized user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Not Found")
    })
    public Response validateUser(@Parameter(description = "Created user details object", required = true) UserDetailsRequest request) {

        OfsApiResponse response = new OfsApiResponse();
        try {
            response = adAuthenticationApiController.validateUserDetails(request);

            if (response.getCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000)) {
                // Generate JWT Token
                Map<String, String> tokens = adKeyGeneratorController.generateTokens(request.getUsername());
                response.setTokens(tokens);
                response.setStatus("Success");
            }
            return Response.status(response.getCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ? Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response.toString())
                    .build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response.toString()).build();
        }
    }

    @POST
    @Path("/logout")
    @RolesAllowed({"Admin"})
    @Operation(summary = "Refresh user token details", description = "This can only be done by an authorized user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Not Found")
    })
    public Response refreshToken(@Parameter(description = "Refresh Token", required = true) UserDetailsRequest request) {

        OfsApiResponse response = new OfsApiResponse();
        try {

            String token = adKeyGeneratorController.refreshToken(request.getRefreshToken());
            response.setRefreshToken(token);
            response.setStatus("Success");
            return Response.status(Response.Status.OK)
                    .entity(response.toString())
                    .build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response.toString()).build();
        }
    }

}