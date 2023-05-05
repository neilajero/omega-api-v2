package com.ejb.restfulapi.ar;

import com.ejb.restfulapi.OfsApiResponse;
import com.ejb.restfulapi.ar.models.CustomerRequest;
import com.ejb.txnapi.ar.ArCustomerEntryApiController;
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

}