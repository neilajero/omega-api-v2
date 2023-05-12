package com.ejb.restfulapi.reports.ar;

import com.ejb.restfulapi.reports.ar.models.StatementRequest;
import com.ejb.restfulapi.reports.ar.models.StatementResponse;
import com.ejb.txnapi.reports.ar.ArRepStatementApiController;
import com.util.EJBCommon;
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
@Path("/soa")
@Consumes(MediaType.APPLICATION_JSON)
public class ArRepStatementRestApi {

    @Inject
    ArRepStatementApiController arRepStatementApiController;

    @POST
    @Path("/pdf")
    @Produces("application/pdf")
    @RolesAllowed({"Admin"})
    public Response generateSoa(StatementRequest request) {

        StatementResponse response = arRepStatementApiController.generateSoa(request);
        String outputFilename = String.format("attachment; filename=%s.pdf", EJBCommon.SOA_REPORT_FILENAME);
        return Response.ok(response.getPdfReport(), "application/pdf")
                .header("Content-Disposition", outputFilename)
                .build();
    }

}