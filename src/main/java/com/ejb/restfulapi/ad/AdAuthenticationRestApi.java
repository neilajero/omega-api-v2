package com.ejb.restfulapi.ad;

import com.ejb.restfulapi.OfsApiResponse;
import com.ejb.restfulapi.ad.models.TokenRefreshResponse;
import com.ejb.restfulapi.ad.models.TokenResponse;
import com.ejb.restfulapi.ad.models.UserDetailsRequest;
import com.ejb.txnapi.ad.AdAuthenticationApiController;
import com.ejb.txnapi.ad.AdKeyGeneratorController;
import com.util.EJBCommonAPIErrCodes;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@RequestScoped
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class AdAuthenticationRestApi {

    @Inject
    private AdAuthenticationApiController adAuthenticationApiController;

    @Inject
    private AdKeyGeneratorController adKeyGeneratorController;

    @POST
    @Path("/user")
    @RolesAllowed({"Admin"})
    public Response validateUser(UserDetailsRequest request) {

        TokenResponse tokenResp = new TokenResponse();
        try {
            OfsApiResponse ofsResp = adAuthenticationApiController.validateUserDetails(request);
            if (ofsResp.getCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000)) {
                // Generate JWT Token
                Map<String, String> tokens = adKeyGeneratorController.generateTokens(request.getUsername());
                tokenResp.setTokens(tokens);
            }
            return Response.status(ofsResp.getCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ? Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(tokenResp)
                    .build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(tokenResp).build();
        }
    }

    @POST
    @Path("/logout")
    @RolesAllowed({"Admin"})
    public Response refreshToken(UserDetailsRequest request) {
        TokenRefreshResponse tokenResp = new TokenRefreshResponse();
        try {
            String token = adKeyGeneratorController.refreshToken(request.getRefreshToken());
            tokenResp.setRefreshToken(token);
            return Response.status(Response.Status.OK)
                    .entity(tokenResp)
                    .build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(tokenResp).build();
        }
    }

}