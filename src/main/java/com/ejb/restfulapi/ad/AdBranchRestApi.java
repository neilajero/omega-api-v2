package com.ejb.restfulapi.ad;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.restfulapi.OfsApiResponse;
import com.ejb.txnapi.ad.AdBranchApiController;
import com.util.EJBCommonAPIErrCodes;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("/branch")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdBranchRestApi {

    @Inject
    private AdBranchApiController adBranchApiController;

    @GET
    @Path("/get/{companyShortName}")
    @RolesAllowed({"Admin"})
    public Response getAll(@PathParam("companyShortName") String companyShortName) {

        OfsApiResponse response = new OfsApiResponse();
        try {
            response = adBranchApiController.getAdBrAll(companyShortName);
            return Response.status(response.getCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                            Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response.toString()).build();
        }
        catch (GlobalNoRecordFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response.toString()).build();
        }
    }

}