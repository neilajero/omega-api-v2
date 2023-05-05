package com.ejb.restfulapi.ar;

import com.ejb.restfulapi.OfsApiResponse;
import com.ejb.txnapi.ar.ArStandardMemoLineApiController;
import com.util.EJBCommonAPIErrCodes;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;

@RequestScoped
@Path("/sml")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ArStandardMemoLineRestApi {

    @Inject
    private ArStandardMemoLineApiController arStandardMemoLineApiController;

    @GET
    @Path("/{companyshortname}")
    @RolesAllowed({"Admin"})
    public Response getStandardMemoLines(@PathParam("companyshortname") String companyshortname) {

        OfsApiResponse response = new OfsApiResponse();
        try {
            ArrayList<String> smlNames = arStandardMemoLineApiController.getArSmlAll(companyshortname);
            response.setSmlNames(smlNames);
            response.setStatus("Success");
            return Response.status(Response.Status.OK)
                    .entity(response.toString()).build();
        }
        catch (Exception e) {
            response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return Response.status(Response.Status.BAD_REQUEST).entity(response.toString()).build();
        }
    }

}