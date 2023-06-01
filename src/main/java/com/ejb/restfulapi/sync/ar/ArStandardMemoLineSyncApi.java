package com.ejb.restfulapi.sync.ar;

import com.ejb.restfulapi.sync.ar.models.ArStandardMemoLineSyncRequest;
import com.ejb.restfulapi.sync.ar.models.ArStandardMemoLineSyncResponse;
import com.ejb.txnsync.ar.ArStandardMemoLineSyncController;
import com.util.EJBCommonAPIErrCodes;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("/smlsync")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class ArStandardMemoLineSyncApi {

    @Inject
    private ArStandardMemoLineSyncController arStandardMemoLineSyncController;

    @GET
    @Path("/new")
    @RolesAllowed({"Admin"})
    public Response getArStandardMemoLineAllNewLength(ArStandardMemoLineSyncRequest request) {

        ArStandardMemoLineSyncResponse response = new ArStandardMemoLineSyncResponse();
        try {
            response = arStandardMemoLineSyncController.getArStandardMemoLineAllNewLength(request);
            return Response.status(response.getStatusCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                            Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    @GET
    @Path("/updatedlength")
    @RolesAllowed({"Admin"})
    public Response getArStandardMemoLineHomeAllUpdatedLength(ArStandardMemoLineSyncRequest request) {

        ArStandardMemoLineSyncResponse response = new ArStandardMemoLineSyncResponse();
        try {
            response = arStandardMemoLineSyncController.getArStandardMemoLineHomeAllUpdatedLength(request);
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
    public Response getArStandardMemoLineAllNewAndUpdated(ArStandardMemoLineSyncRequest request) {

        ArStandardMemoLineSyncResponse response = new ArStandardMemoLineSyncResponse();
        try {
            response = arStandardMemoLineSyncController.getArStandardMemoLineAllNewAndUpdated(request);
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
    public Response setArStandardMemoLineAllNewAndUpdatedSuccessConfirmation(ArStandardMemoLineSyncRequest request) {

        ArStandardMemoLineSyncResponse response = new ArStandardMemoLineSyncResponse();
        try {
            response = arStandardMemoLineSyncController.setArStandardMemoLineAllNewAndUpdatedSuccessConfirmation(request);
            return Response.status(response.getStatusCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                            Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

}