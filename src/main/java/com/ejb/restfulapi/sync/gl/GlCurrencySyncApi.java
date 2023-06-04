package com.ejb.restfulapi.sync.gl;

import com.ejb.restfulapi.sync.gl.models.GlCurrencySyncRequest;
import com.ejb.restfulapi.sync.gl.models.GlCurrencySyncResponse;
import com.ejb.txnsync.gl.GlCurrencySyncController;
import com.util.EJBCommonAPIErrCodes;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("/currencysync")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class GlCurrencySyncApi {

    @Inject
    private GlCurrencySyncController glCurrencySyncController;

    @GET
    @Path("/all")
    @RolesAllowed({"Admin"})
    public Response getGlFcAll(GlCurrencySyncRequest request) {

        GlCurrencySyncResponse response = new GlCurrencySyncResponse();
        try {
            response = glCurrencySyncController.getGlFcAll(request);
            return Response.status(response.getStatusCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                            Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    @GET
    @Path("/rates")
    @RolesAllowed({"Admin"})
    public Response getGlCurrentFcRates(GlCurrencySyncRequest request) {

        GlCurrencySyncResponse response = new GlCurrencySyncResponse();
        try {
            response = glCurrencySyncController.getGlCurrentFcRates(request);
            return Response.status(response.getStatusCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                            Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

}