package com.ejb.restfulapi.ad;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.InputStream;

@Stateless
@Path("/health")
@LocalBean
public class AdOfsHealthCheckRestApi {

    @SuppressWarnings("unchecked")
    @GET
    @Hidden
    @Path("/check")
    public Response getHealthCheck() {

        JSONObject response = new JSONObject();
        response.put("status", "healthy");
        String command = "curl -v --digest -u omega:0M3gabc! http://localhost:9990/health";

        try {
            Process process = Runtime.getRuntime().exec(command);
            InputStream inputStream = process.getInputStream();
            if (inputStream.read() == -1) {
                process.destroy();
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return Response.ok(response, MediaType.APPLICATION_JSON).build();
    }

}