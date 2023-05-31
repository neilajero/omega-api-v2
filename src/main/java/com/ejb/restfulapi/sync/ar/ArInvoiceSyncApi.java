package com.ejb.restfulapi.sync.ar;

import com.ejb.restfulapi.sync.ar.models.ArInvoiceSyncRequest;
import com.ejb.restfulapi.sync.ar.models.ArInvoiceSyncResponse;
import com.ejb.txnsync.ar.ArInvoiceSyncController;
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
@Path("/invoicesync")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class ArInvoiceSyncApi {

    @Inject
    private ArInvoiceSyncController arInvoiceSyncController;

    @POST
    @Path("/invoices")
    @RolesAllowed({"Admin"})
    public Response setArInvoiceAllNewAndVoid(ArInvoiceSyncRequest request) {

        ArInvoiceSyncResponse response = new ArInvoiceSyncResponse();
        try {
            response = arInvoiceSyncController.setArInvoiceAllNewAndVoid(request);
            return Response.status(response.getStatusCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                            Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    @POST
    @Path("/salesorder")
    @RolesAllowed({"Admin"})
    public Response setArSoNew(ArInvoiceSyncRequest request) {

        ArInvoiceSyncResponse response = new ArInvoiceSyncResponse();
        try {
            response = arInvoiceSyncController.setArSoNew(request);
            return Response.status(response.getStatusCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                            Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

}