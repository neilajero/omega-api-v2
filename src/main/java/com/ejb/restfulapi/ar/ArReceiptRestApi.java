package com.ejb.restfulapi.ar;

import com.ejb.ConfigurationClass;
import com.ejb.restfulapi.ar.models.ReceiptApiResponse;
import com.ejb.restfulapi.ar.models.ReceiptRequest;
import com.ejb.txnapi.ar.ArMiscReceiptEntryApiController;
import com.ejb.txnapi.ar.ArReceiptEntryApiController;
import com.util.EJBCommon;
import com.util.EJBCommonAPIErrCodes;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("/receipt")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ArReceiptRestApi {

    @Inject
    private ArReceiptEntryApiController arReceiptEntryApiController;
    @Inject
    private ArMiscReceiptEntryApiController arMiscReceiptEntryApiController;

    @POST
    @Path("/add")
    @RolesAllowed({"Admin"})
    public Response create(ReceiptRequest request) {

        ReceiptApiResponse response = new ReceiptApiResponse();
        String defaultDateFormat = ConfigurationClass.DEFAULT_DATE_FORMAT;

        try {

            if (EJBCommon.validateApiDate(request.getReceiptDate(), defaultDateFormat)) {
                response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_021);
                response.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_021_MSG, defaultDateFormat));
                return Response.status(Response.Status.BAD_REQUEST).entity(response.toString()).build();
            }

            // Create the Receipt Details
            if (request.getReceiptDetails() != null) {
                if (!request.getReceiptDetails().isEmpty()) {
                    response = arReceiptEntryApiController.createReceipt(request);
                }
            } else {
                if (request.getReceiptItems() != null) {
                    // Create receipts items
                    if (!request.getReceiptItems().isEmpty()) {
                        response = arMiscReceiptEntryApiController.createMiscReceipt(request);
                    }
                } else {
                    // Create misc receipt - memo lines
                    response = arMiscReceiptEntryApiController.createMiscReceiptMemoLines(request);
                }
            }
            return Response.status(response.getCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ? Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response.toString()).build();
        }
        catch (Exception e) {
            response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return Response.status(Response.Status.BAD_REQUEST).entity(response.toString()).build();
        }
    }

}