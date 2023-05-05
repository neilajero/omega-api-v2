package com.ejb.restfulapi.ad;

import com.ejb.restfulapi.OfsApiResponse;
import com.ejb.restfulapi.ad.models.AmountLimitRequest;
import com.ejb.txnapi.ad.AdAmountLimitApiController;
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
@Path("/amountlimit")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class AdAmountLimitRestApi {

    @Inject
    private AdAmountLimitApiController adAmountLimitApiController;

    @POST
    @Path("/add")
    @RolesAllowed({"Admin"})
    public Response addAmountLimit(AmountLimitRequest request) {

        OfsApiResponse response = new OfsApiResponse();
        try {
            response = adAmountLimitApiController.saveAmountLimit(request);
            return Response.status(response.getCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ? Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response.toString())
                    .build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response.toString()).build();
        }
    }

}