package com.ejb.restfulapi.sync.ad;

import com.ejb.restfulapi.sync.ad.models.BankAccountSyncRequest;
import com.ejb.restfulapi.sync.ad.models.BankAccountSyncResponse;
import com.ejb.txnsync.ad.AdBankAccountSyncController;
import com.util.EJBCommonAPIErrCodes;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@RequestScoped
@Path("/bankaccountsync")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class AdBankAccountSyncApi {

    @Inject
    private AdBankAccountSyncController adBankAccountSyncController;

    @GET
    @Path("/new")
    @RolesAllowed({"Admin"})
    public Response getAllNewLength(BankAccountSyncRequest request) {
        BankAccountSyncResponse response = new BankAccountSyncResponse();
        try {
            response = adBankAccountSyncController.getAllNewLength(request);
            response.setCount(response.getCount());
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
    public Response getAllUpdatedLength(BankAccountSyncRequest request) {
        BankAccountSyncResponse response = new BankAccountSyncResponse();
        try {
            response = adBankAccountSyncController.getAllUpdatedLength(request);
            response.setCount(response.getCount());
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
    public Response getAllNewAndUpdated(BankAccountSyncRequest request) {
        BankAccountSyncResponse response = new BankAccountSyncResponse();
        try {
            response = adBankAccountSyncController.getAllNewAndUpdated(request);
            response.setCount(response.getCount());
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
    public Response setAllNewAndUpdatedSuccessConfirmation(BankAccountSyncRequest request) {
        BankAccountSyncResponse response = new BankAccountSyncResponse();
        try {
            response = adBankAccountSyncController.setAllNewAndUpdatedSuccessConfirmation(request);
            response.setCount(response.getCount());
            return Response.status(response.getStatusCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                            Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

}