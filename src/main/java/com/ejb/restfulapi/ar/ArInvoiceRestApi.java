package com.ejb.restfulapi.ar;

import com.ejb.ConfigurationClass;
import com.ejb.restfulapi.OfsApiResponse;
import com.ejb.restfulapi.ar.models.InvoiceItemRequest;
import com.ejb.restfulapi.ar.models.InvoiceMemoLineRequest;
import com.ejb.restfulapi.ar.models.InvoiceMemoLineResponse;
import com.ejb.txnapi.ar.ArCreditMemoEntryApiController;
import com.ejb.txnapi.ar.ArInvoiceEntryApiController;
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
@Path("/invoice")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ArInvoiceRestApi {

    @Inject
    private ArInvoiceEntryApiController arInvoiceEntryApiController;
    @Inject
    private ArCreditMemoEntryApiController arCreditMemoEntryApiController;

    @POST
    @Path("/item")
    @RolesAllowed({"Admin"})
    public Response createInvoiceItem(InvoiceItemRequest request) {

        OfsApiResponse response = new OfsApiResponse();
        String defaultDateFormat = ConfigurationClass.DEFAULT_DATE_FORMAT;
        try {
            if (EJBCommon.validateApiDate(request.getInvoiceDate(), defaultDateFormat)) {
                response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_021);
                response.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_021_MSG, defaultDateFormat));
                return Response.status(Response.Status.BAD_REQUEST).entity(response.toString()).build();
            }
            // Create the invoice
            response = arInvoiceEntryApiController.createInvoiceItems(request);
            return Response.status(response.getCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                    Response.Status.OK : Response.Status.BAD_REQUEST).entity(response.toString()).build();
        }
        catch (Exception e) {
            response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return Response.status(Response.Status.BAD_REQUEST).entity(response.toString()).build();
        }
    }

    @POST
    @Path("/memo")
    @RolesAllowed({"Admin"})
    public Response createInvoiceMemoLines(InvoiceMemoLineRequest request) {

        OfsApiResponse response = new OfsApiResponse();
        InvoiceMemoLineResponse memoLineResponse = new InvoiceMemoLineResponse();
        String defaultDateFormat = ConfigurationClass.DEFAULT_DATE_FORMAT;
        try {
            if (EJBCommon.validateApiDate(request.getInvoiceDate(), defaultDateFormat)) {
                memoLineResponse.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_021);
                memoLineResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_021_MSG, defaultDateFormat));
                return Response.status(Response.Status.BAD_REQUEST).entity(memoLineResponse).build();
            }
            // Create the invoice memo lines
            response = arInvoiceEntryApiController.createInvoiceMemoLines(request);

            // Return the response to client
            memoLineResponse.setStatusCode(response.getCode());
            memoLineResponse.setStatus(response.getStatus());
            memoLineResponse.setMessage(response.getMessage());
            memoLineResponse.setInvoiceNumber(response.getDocumentNumber());

            return Response.status(response.getCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                    Response.Status.OK : Response.Status.BAD_REQUEST).entity(memoLineResponse).build();
        }
        catch (Exception e) {
            response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return Response.status(Response.Status.BAD_REQUEST).entity(memoLineResponse).build();
        }
    }

    @POST
    @Path("/modify")
    @RolesAllowed({"Admin"})
    public Response createInvoiceItemAdjustment(InvoiceItemRequest request) {

        OfsApiResponse response = new OfsApiResponse();
        try {
            response = arCreditMemoEntryApiController.createCreditMemo(request);
            return Response.status(response.getCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                    Response.Status.OK : Response.Status.BAD_REQUEST).entity(response.toString()).build();
        }
        catch (Exception e) {
            response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return Response.status(Response.Status.BAD_REQUEST).entity(response.toString()).build();
        }
    }
}