package com.ejb.restfulapi.sync.ar;

import com.ejb.restfulapi.sync.ar.models.ArMiscReceiptSyncRequest;
import com.ejb.restfulapi.sync.ar.models.ArMiscReceiptSyncResponse;
import com.ejb.txnsync.ar.ArMiscReceiptSyncController;
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

@RequestScoped
@Path("/miscreceiptsync")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class ArMiscReceiptSyncApi {

    @Inject
    private ArMiscReceiptSyncController arMiscReceiptSyncController;

    @POST
    @Path("/newandvoid")
    @RolesAllowed({"Admin"})
    public Response setArMiscReceiptAllNewAndVoid(ArMiscReceiptSyncRequest request) {

        ArMiscReceiptSyncResponse response = new ArMiscReceiptSyncResponse();
        try {
            response = arMiscReceiptSyncController.setArMiscReceiptAllNewAndVoid(request);
            return Response.status(response.getStatusCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                            Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    @POST
    @Path("/us")
    @RolesAllowed({"Admin"})
    public Response setArMiscReceiptAllNewAndVoidUS(ArMiscReceiptSyncRequest request) {

        ArMiscReceiptSyncResponse response = new ArMiscReceiptSyncResponse();
        try {
            response = arMiscReceiptSyncController.setArMiscReceiptAllNewAndVoidUS(request);
            return Response.status(response.getStatusCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                            Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    @POST
    @Path("/expirydate")
    @RolesAllowed({"Admin"})
    public Response setArMiscReceiptAllNewAndVoidWithExpiryDate(ArMiscReceiptSyncRequest request) {

        ArMiscReceiptSyncResponse response = new ArMiscReceiptSyncResponse();
        try {
            response = arMiscReceiptSyncController.setArMiscReceiptAllNewAndVoidWithExpiryDate(request);
            return Response.status(response.getStatusCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                            Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    @POST
    @Path("/itemautobuild")
    @RolesAllowed({"Admin"})
    public Response setArMiscReceiptAllNewAndVoidWithExpiryDateEnableItemAutoBuild(ArMiscReceiptSyncRequest request) {

        ArMiscReceiptSyncResponse response = new ArMiscReceiptSyncResponse();
        try {
            response = arMiscReceiptSyncController.setArMiscReceiptAllNewAndVoidWithExpiryDateEnableItemAutoBuild(request);
            return Response.status(response.getStatusCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                            Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    @POST
    @Path("/entries")
    @RolesAllowed({"Admin"})
    public Response setArMiscReceiptAllNewAndVoidWithExpiryDateAndEntries(ArMiscReceiptSyncRequest request) {

        ArMiscReceiptSyncResponse response = new ArMiscReceiptSyncResponse();
        try {
            response = arMiscReceiptSyncController.setArMiscReceiptAllNewAndVoidWithExpiryDateAndEntries(request);
            return Response.status(response.getStatusCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                            Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    @POST
    @Path("/salesperson")
    @RolesAllowed({"Admin"})
    public Response setArMiscReceiptAllNewAndVoidWithSalesperson(ArMiscReceiptSyncRequest request) {

        ArMiscReceiptSyncResponse response = new ArMiscReceiptSyncResponse();
        try {
            response = arMiscReceiptSyncController.setArMiscReceiptAllNewAndVoidWithSalesperson(request);
            return Response.status(response.getStatusCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                            Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

}