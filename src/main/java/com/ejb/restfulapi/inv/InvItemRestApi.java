package com.ejb.restfulapi.inv;

import com.ejb.ConfigurationClass;
import com.ejb.dao.inv.LocalInvItemHome;
import com.ejb.entities.inv.LocalInvItem;
import com.ejb.restfulapi.OfsApiResponse;
import com.ejb.restfulapi.inv.models.AdjustmentRequest;
import com.ejb.restfulapi.inv.models.ItemRequest;
import com.ejb.txnapi.inv.InvAdjustmentEntryApiController;
import com.ejb.txnapi.inv.InvItemEntryApiController;
import com.ejb.txnapi.inv.InvStockTransferEntryApiController;
import com.util.EJBCommon;
import com.util.EJBCommonAPIErrCodes;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequestScoped
@Path("/invitem")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InvItemRestApi {

    @Inject
    private LocalInvItemHome invItemHome;

    @Inject
    private InvItemEntryApiController invItemEntryApiController;

    @Inject
    private InvAdjustmentEntryApiController invAdjustmentEntryApiController;

    @Inject
    private InvStockTransferEntryApiController invStockTransferEntryApiController;

    @GET
    @Path("/getall")
    @RolesAllowed({"Admin"})
    public Response getItemDetails() {

        try {

            int companyCode = 1;
            Collection<LocalInvItem> result = invItemHome.findAll(companyCode);

            if (!result.isEmpty()) {
                return Response.ok(result).build();
            }

            return Response.status(Response.Status.NOT_FOUND).entity(EJBCommon.NO_RECORD_FOUND).build();

        }
        catch (FinderException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).entity(EJBCommon.NO_RECORD_FOUND).build();
        }
    }

    @POST
    @Path("/add")
    @RolesAllowed({"Admin"})
    public Response create(ItemRequest request) {

        OfsApiResponse response = new OfsApiResponse();
        try {
            response = invItemEntryApiController.createItem(request);
            return Response.status(response.getCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ? Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response.toString()).build();
        }
        catch (Exception e) {
            response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return Response.status(Response.Status.BAD_REQUEST).entity(response.toString()).build();
        }
    }

    @PUT
    @Path("/update/{itemName}")
    @RolesAllowed({"Admin"})
    public Response update(@PathParam("itemName") String itemName, AdjustmentRequest request) {

        OfsApiResponse response = new OfsApiResponse();
        String defaultDateFormat = ConfigurationClass.DEFAULT_DATE_FORMAT;

        try {

            int companyCode = 1;
            LocalInvItem updateItem = null;
            try {
                updateItem = invItemHome.findByIiName(itemName, companyCode);
                // Item not found
                if (updateItem == null || updateItem.getIiEnable() == EJBCommon.FALSE) {
                    return Response.status(Response.Status.NOT_FOUND).entity(response.toString()).build();
                }
            }
            catch (FinderException ex) {
                throw ex;
            }

            // Invalid date format
            // This is a temporary parameter
            LocalDateTime myDateObj = LocalDateTime.now();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(ConfigurationClass.DEFAULT_DATE_FORMAT);
            String currentDate = myDateObj.format(myFormatObj);
            request.setAdjustmentDate(currentDate);

            if (EJBCommon.validateApiDate(request.getAdjustmentDate(), defaultDateFormat)) {
                response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_021);
                response.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_021_MSG, defaultDateFormat));
                return Response.status(Response.Status.BAD_REQUEST).entity(response.toString()).build();
            }

            // Invalid identifier - valid for item stock update only
            if (request.getIdentifier() != null && request.getIdentifier().length() > 1) {
                response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_027);
                response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_027_MSG);
                return Response.status(Response.Status.BAD_REQUEST).entity(response.toString()).build();
            }

            // This is temporary only
            List<ItemRequest> itemList = new ArrayList<>();
            ItemRequest itemRequest = new ItemRequest();
            itemRequest.setItemName(updateItem.getIiName());
            itemRequest.setUnitCost(updateItem.getIiUnitCost() == 0d ? request.getUnitCost() : updateItem.getIiUnitCost());
            itemRequest.setDescription(updateItem.getIiDescription());
            itemRequest.setItemQuantity(request.getQuantity());
            itemRequest.setItemLocation(request.getBranchCode());
            itemList.add(itemRequest);

            request.setItems(itemList);
            request.setAdjustmentType("GENERAL");

            if (request.getAdjustmentAction() == null || request.getAdjustmentAction().equals("")) {
                response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_053);
                response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_053_MSG);
                return Response.status(Response.Status.BAD_REQUEST).entity(response.toString()).build();
            }

            if (request.getAdjustmentAction().equals(EJBCommon.INVENTORY_UPLOAD)) {
                response = invAdjustmentEntryApiController.createAdjustment(request);
            } else if (request.getAdjustmentAction().equals(EJBCommon.INVENTORY_TRANSFER)) {
                // This is stock transfer from main LOCATION to any branch
                response = invStockTransferEntryApiController.createStockTransfer(request);
            }
            return Response.status(response.getCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ? Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response.toString()).build();

        }
        catch (FinderException e) {
            response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_010);
            response.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_010_MSG, itemName));
            return Response.status(Response.Status.NOT_FOUND).entity(response.toString()).build();
        }
        catch (Exception e) {
            response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return Response.status(Response.Status.NOT_FOUND).entity(response.toString()).build();
        }
    }

    @DELETE
    @Path("/delete/{itemName}")
    @RolesAllowed({"Admin"})
    public Response delete(@PathParam("itemName") String itemName) {

        OfsApiResponse response = new OfsApiResponse();
        response.setStatus(EJBCommon.NO_RECORD_FOUND);

        try {

            int companyCode = 1;
            LocalInvItem deleteItem = invItemHome.findByIiName(itemName, companyCode);

            if (deleteItem == null || deleteItem.getIiEnable() == EJBCommon.FALSE) {
                return Response.status(Response.Status.NOT_FOUND).entity(response.toString()).build();
            }

            deleteItem.setIiEnable(EJBCommon.FALSE);
            invItemHome.updateItem(deleteItem);

            response.setStatus(EJBCommon.DELETED_RECORD);
            return Response.status(Response.Status.OK).entity(response.toString()).build();

        }
        catch (FinderException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).entity(response.toString()).build();
        }
    }

}