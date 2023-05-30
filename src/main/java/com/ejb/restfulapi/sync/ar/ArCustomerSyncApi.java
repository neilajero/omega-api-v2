package com.ejb.restfulapi.sync.ar;

import com.ejb.restfulapi.sync.ar.models.ArCustomerSyncRequest;
import com.ejb.restfulapi.sync.ar.models.ArCustomerSyncResponse;
import com.ejb.txnsync.ar.ArCustomerSyncController;
import com.util.EJBCommonAPIErrCodes;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("/customersync")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class ArCustomerSyncApi {

    @Inject
    private ArCustomerSyncController arCustomerSyncController;

    @GET
    @Path("/all")
    @RolesAllowed({"Admin"})
    public Response getArCSTAreaAll(ArCustomerSyncRequest request) {

        ArCustomerSyncResponse response = new ArCustomerSyncResponse();
        try {
            response = arCustomerSyncController.getArCSTAreaAll(request);
            return Response.status(response.getStatusCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                            Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    @GET
    @Path("/postedso")
    @RolesAllowed({"Admin"})
    public Response getArSoPostedAllByCstArea(ArCustomerSyncRequest request) {

        ArCustomerSyncResponse response = new ArCustomerSyncResponse();
        try {
            response = arCustomerSyncController.getArSoPostedAllByCstArea(request);
            return Response.status(response.getStatusCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                            Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    @GET
    @Path("/new")
    @RolesAllowed({"Admin"})
    public Response getArCustomersAllNewLength(ArCustomerSyncRequest request) {

        ArCustomerSyncResponse response = new ArCustomerSyncResponse();
        try {
            response = arCustomerSyncController.getArCustomersAllNewLength(request);
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
    public Response getArCustomersAllUpdatedLength(ArCustomerSyncRequest request) {

        ArCustomerSyncResponse response = new ArCustomerSyncResponse();
        try {
            response = arCustomerSyncController.getArCustomersAllUpdatedLength(request);
            return Response.status(response.getStatusCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                            Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    @GET
    @Path("/salesperson")
    @RolesAllowed({"Admin"})
    public Response getArSalespersonAll(ArCustomerSyncRequest request) {

        ArCustomerSyncResponse response = new ArCustomerSyncResponse();
        try {
            response = arCustomerSyncController.getArSalespersonAll(request);
            return Response.status(response.getStatusCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                            Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    @GET
    @Path("/draftbalances")
    @RolesAllowed({"Admin"})
    public Response getArCustomerDraftBalances(ArCustomerSyncRequest request) {

        ArCustomerSyncResponse response = new ArCustomerSyncResponse();
        try {
            response = arCustomerSyncController.getArCustomerDraftBalances(request);
            return Response.status(response.getStatusCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                            Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    @GET
    @Path("/details")
    @RolesAllowed({"Admin"})
    public Response getArCustomersNameCodeAddressSlp(ArCustomerSyncRequest request) {

        ArCustomerSyncResponse response = new ArCustomerSyncResponse();
        try {
            response = arCustomerSyncController.getArCustomersNameCodeAddressSlp(request);
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
    public Response getArCustomersAllNewAndUpdated(ArCustomerSyncRequest request) {

        ArCustomerSyncResponse response = new ArCustomerSyncResponse();
        try {
            response = arCustomerSyncController.getArCustomersAllNewAndUpdated(request);
            return Response.status(response.getStatusCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                            Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    @GET
    @Path("/withsalesperson")
    @RolesAllowed({"Admin"})
    public Response getArCustomersAllNewAndUpdatedWithSalesperson(ArCustomerSyncRequest request) {

        ArCustomerSyncResponse response = new ArCustomerSyncResponse();
        try {
            response = arCustomerSyncController.getArCustomersAllNewAndUpdatedWithSalesperson(request);
            return Response.status(response.getStatusCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                            Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    @GET
    @Path("/withclass")
    @RolesAllowed({"Admin"})
    public Response getArCustomersAllNewAndUpdatedWithCustomerClass(ArCustomerSyncRequest request) {

        ArCustomerSyncResponse response = new ArCustomerSyncResponse();
        try {
            response = arCustomerSyncController.getArCustomersAllNewAndUpdatedWithCustomerClass(request);
            return Response.status(response.getStatusCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                            Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    @GET
    @Path("/alldownloaded")
    @RolesAllowed({"Admin"})
    public Response getArCustomersBalanceAllDownloaded(ArCustomerSyncRequest request) {

        ArCustomerSyncResponse response = new ArCustomerSyncResponse();
        try {
            response = arCustomerSyncController.getArCustomersBalanceAllDownloaded(request);
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
    public Response setArCustomersAllNewAndUpdatedSuccessConfirmation(ArCustomerSyncRequest request) {

        ArCustomerSyncResponse response = new ArCustomerSyncResponse();
        try {
            response = arCustomerSyncController.setArCustomersAllNewAndUpdatedSuccessConfirmation(request);
            return Response.status(response.getStatusCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                            Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

}