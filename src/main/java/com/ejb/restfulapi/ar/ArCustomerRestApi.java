package com.ejb.restfulapi.ar;

import com.ejb.restfulapi.OfsApiResponse;
import com.ejb.restfulapi.ar.models.CustomerListResponse;
import com.ejb.restfulapi.ar.models.CustomerRequest;
import com.ejb.txnapi.ar.ArCustomerEntryApiController;
import com.util.EJBCommonAPIErrCodes;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("/customer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ArCustomerRestApi {

    @Inject
    private ArCustomerEntryApiController arCustomerEntryApiController;

    @POST
    @Path("/add")
    @RolesAllowed({"Admin"})
    public Response create(CustomerRequest request) {

        OfsApiResponse response = new OfsApiResponse();
        try {
            response = arCustomerEntryApiController.createCustomer(request);
            return Response.status(response.getCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ? Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response.toString()).build();
        }
        catch (Exception e) {
            response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return Response.status(Response.Status.BAD_REQUEST).entity(response.toString()).build();
        }
    }

    @GET
    @Path("/all/{companyshortname}")
    @RolesAllowed({"Admin"})
    public Response getAllCustomersPerCompany(@PathParam("companyshortname") String companyshortname) {

        CustomerListResponse response = new CustomerListResponse();
        try {
            response = arCustomerEntryApiController.getAllCustomersPerCompany(companyshortname);
            return Response.status(response.getStatusCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ?
                            Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response).build();
        }
        catch (Exception e) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return Response.status(Response.Status.BAD_REQUEST).entity(response.toString()).build();
        }
    }

}