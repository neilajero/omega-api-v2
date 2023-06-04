package com.ejb.restfulapi.sync.inv;

import com.ejb.restfulapi.sync.inv.models.InvLocationSyncRequest;
import com.ejb.restfulapi.sync.inv.models.InvLocationSyncResponse;
import com.ejb.txnsync.inv.InvLocationSyncController;
import com.util.EJBCommonAPIErrCodes;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("/locationsync")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class InvLocationSyncApi {

    @Inject
    private InvLocationSyncController invLocationSyncController;

    @GET
    @Path("/all")
    @RolesAllowed({"Admin"})
    public Response getInvLocationAllNewLength(InvLocationSyncRequest request) {

        InvLocationSyncResponse response = new InvLocationSyncResponse();
        try {
            response = invLocationSyncController.getInvLocationAllNewLength(request);
            return Response.status(response.getStatusCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                            Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    @GET
    @Path("/updated")
    @RolesAllowed({"Admin"})
    public Response getInvLocationAllUpdatedLength(InvLocationSyncRequest request) {

        InvLocationSyncResponse response = new InvLocationSyncResponse();
        try {
            response = invLocationSyncController.getInvLocationAllUpdatedLength(request);
            return Response.status(response.getStatusCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                            Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    @GET
    @Path("/both")
    @RolesAllowed({"Admin"})
    public Response getInvLocationAllNewAndUpdated(InvLocationSyncRequest request) {

        InvLocationSyncResponse response = new InvLocationSyncResponse();
        try {
            response = invLocationSyncController.getInvLocationAllNewAndUpdated(request);
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
    public Response setInvLocationAllNewAndUpdatedSuccessConfirmation(InvLocationSyncRequest request) {

        InvLocationSyncResponse response = new InvLocationSyncResponse();
        try {
            response = invLocationSyncController.setInvLocationAllNewAndUpdatedSuccessConfirmation(request);
            return Response.status(response.getStatusCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                            Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }
}