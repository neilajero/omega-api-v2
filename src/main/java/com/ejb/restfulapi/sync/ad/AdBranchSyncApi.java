package com.ejb.restfulapi.sync.ad;

import com.ejb.restfulapi.sync.ad.models.BranchSyncRequest;
import com.ejb.restfulapi.sync.ad.models.BranchSyncResponse;
import com.ejb.txnsync.ad.AdBranchSyncController;
import com.util.EJBCommonAPIErrCodes;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("/branchsync")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class AdBranchSyncApi {

    @Inject
    private AdBranchSyncController adBranchSyncController;

    @GET
    @Path("/all")
    @RolesAllowed({"Admin"})
    public Response getAdBranchAll(BranchSyncRequest request) {

        BranchSyncResponse response = new BranchSyncResponse();
        try {
            response = adBranchSyncController.getAdBranchAll(request);
            return Response.status(response.getStatusCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                            Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    @GET
    @Path("/name")
    @RolesAllowed({"Admin"})
    public Response getAdBranchAllWithBranchName(BranchSyncRequest request) {

        BranchSyncResponse response = new BranchSyncResponse();
        try {
            response = adBranchSyncController.getAdBranchAllWithBranchName(request);
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
    public Response setAdBranchAllSuccessConfirmation(BranchSyncRequest request) {

        BranchSyncResponse response = new BranchSyncResponse();
        try {
            response = adBranchSyncController.setAdBranchAllSuccessConfirmation(request);
            return Response.status(response.getStatusCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                            Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

}