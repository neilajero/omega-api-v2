package com.ejb.restfulapi.sync.inv;

import com.ejb.restfulapi.sync.inv.models.InvUnitOfMeasureSyncRequest;
import com.ejb.restfulapi.sync.inv.models.InvUnitOfMeasureSyncResponse;
import com.ejb.txnsync.inv.InvUnitOfMeasureSyncController;
import com.util.EJBCommonAPIErrCodes;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("/uomsync")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class InvUnitOfMeasureSyncApi {

    @Inject
    private InvUnitOfMeasureSyncController invUnitOfMeasureSyncController;

    @POST
    @Path("/all")
    @RolesAllowed({"Admin"})
    public Response getInvUnitOfMeasuresAll(InvUnitOfMeasureSyncRequest request) {

        InvUnitOfMeasureSyncResponse response = new InvUnitOfMeasureSyncResponse();
        try {
            response = invUnitOfMeasureSyncController.getInvUnitOfMeasuresAll(request);
            return Response.status(response.getStatusCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                            Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    @POST
    @Path("/status")
    @RolesAllowed({"Admin"})
    public Response setInvUnitOfMeasuresAllSuccessConfirmation(InvUnitOfMeasureSyncRequest request) {

        InvUnitOfMeasureSyncResponse response = new InvUnitOfMeasureSyncResponse();
        try {
            response = invUnitOfMeasureSyncController.setInvUnitOfMeasuresAllSuccessConfirmation(request);
            return Response.status(response.getStatusCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                            Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

}