package com.ejb.restfulapi.ap;

import com.ejb.dao.ap.ILocalApSupplierHome;
import com.ejb.entities.ap.LocalApSupplier;
import com.util.EJBCommon;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Collection;

@RequestScoped
@Path("/apsupplier")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApSupplierRestApi {

    @Inject
    private ILocalApSupplierHome apSupplierHome;

    @GET
    @Path("/getall/{branchCode}/{companyCode}")
    @RolesAllowed({"Admin"})
    public Response getAll(@PathParam("branchCode") Integer branchCode, @PathParam("companyCode") Integer companyCode) {

        try {

            Collection result = apSupplierHome.findSplAll(branchCode, companyCode);

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
    public Response create(LocalApSupplier apSupplier) {

        try {

            apSupplierHome
                    .SplSupplierCode(apSupplier.getSplSupplierCode())
                    .SplName(apSupplier.getSplName())
                    .SplEnable(EJBCommon.TRUE)
                    .SplAdCompany(apSupplier.getSplAdCompany())
                    .buildSupplier();

        }
        catch (CreateException e) {
            e.printStackTrace();
        }

        return Response.ok().entity(EJBCommon.ADDED_NEW_RECORD).build();
    }

    @PUT
    @Path("/update/{id}")
    @RolesAllowed({"Admin"})
    public Response update(@PathParam("id") Integer id, LocalApSupplier apSupplier) {

        try {

            LocalApSupplier updateSupplier = apSupplierHome.findById(id);

            if (updateSupplier != null) {

                updateSupplier.setSplName(apSupplier.getSplName());
                apSupplierHome.updateSupplier(updateSupplier);

                return Response.ok().entity(EJBCommon.UPDATED_EXISTING_RECORD).build();
            }

            return Response.status(Response.Status.NOT_FOUND).entity(EJBCommon.NO_RECORD_FOUND).build();


        }
        catch (FinderException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).entity(EJBCommon.NO_RECORD_FOUND).build();
        }
    }

    @DELETE
    @Path("/delete/{id}")
    @RolesAllowed({"Admin"})
    public Response delete(@PathParam("id") Integer id) {

        try {

            LocalApSupplier deletedSupplier = apSupplierHome.findById(id);

            if (deletedSupplier != null) {

                deletedSupplier.setSplEnable(EJBCommon.FALSE);
                apSupplierHome.updateSupplier(deletedSupplier);

                return Response.ok().entity(EJBCommon.DELETED_RECORD).build();
            }
            return Response.status(Response.Status.NOT_FOUND).entity(EJBCommon.NO_RECORD_FOUND).build();

        }
        catch (FinderException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).entity(EJBCommon.NO_RECORD_FOUND).build();
        }
    }

}