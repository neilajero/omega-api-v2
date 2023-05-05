package com.ejb.restfulapi.ad;

import com.ejb.restfulapi.OfsApiResponse;
import com.ejb.txnapi.ad.AdDocumentSequenceAssignmentApiController;
import com.util.EJBCommonAPIErrCodes;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@RequestScoped
@Path("/document")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdDocumentSequenceAssignmentRestApi {

    @Context
    private SecurityContext securityContext;
    @Inject
    private AdDocumentSequenceAssignmentApiController adDocumentSequenceAssignmentApiController;

    @GET
    @Path("/{documentName}/{companyShortName}")
    @RolesAllowed({"Admin"})
    @Operation(summary = "Retrieve document sequence assignment", description = "This can only be done by an authorized user.")
    public Response getDsaCode(@PathParam("documentName") String documentName, @PathParam("companyShortName") String companyShortName) {

        OfsApiResponse response = new OfsApiResponse();
        try {
            response = adDocumentSequenceAssignmentApiController.findByDcName(documentName, companyShortName);
            return Response.status(response.getCode().equals(EJBCommonAPIErrCodes.OAPI_ERR_000) ? Response.Status.OK : Response.Status.BAD_REQUEST)
                    .entity(response.toString()).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response.toString()).build();
        }
    }

}