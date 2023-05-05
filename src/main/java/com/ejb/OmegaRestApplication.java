package com.ejb;

import com.ejb.restfulapi.ad.*;
import com.ejb.restfulapi.ap.ApSupplierRestApi;
import com.ejb.restfulapi.ar.ArCustomerRestApi;
import com.ejb.restfulapi.ar.ArInvoiceRestApi;
import com.ejb.restfulapi.ar.ArReceiptRestApi;
import com.ejb.restfulapi.ar.ArStandardMemoLineRestApi;
import com.ejb.restfulapi.filters.CORSFilter;
import com.ejb.restfulapi.filters.OmegaJWTTokenFilter;
import com.ejb.restfulapi.inv.InvItemRestApi;
import com.web.UserService;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.integration.GenericOpenApiContextBuilder;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationPath("/api")
public class OmegaRestApplication extends Application {
    private Set<Class> classes = new HashSet<Class>();

    public OmegaRestApplication() {

        this.OpenAPIDocs();

        classes.add(UserService.class);

        // Filters
        classes.add(CORSFilter.class);
        classes.add(OmegaJWTTokenFilter.class);

        // Admin
        classes.add(AdAmountLimitRestApi.class);
        classes.add(AdBranchRestApi.class);
        classes.add(AdOfsHealthCheckRestApi.class);
        classes.add(AdDocumentSequenceAssignmentRestApi.class);
        classes.add(AdAuthenticationRestApi.class);

        // Accounts Payables
        classes.add(ApSupplierRestApi.class);

        // Accounts Receivables
        classes.add(ArInvoiceRestApi.class);
        classes.add(ArReceiptRestApi.class);
        classes.add(ArCustomerRestApi.class);
        classes.add(ArStandardMemoLineRestApi.class);

        // Inventory
        classes.add(InvItemRestApi.class);

        classes.add(OpenApiResource.class);
        classes.add(AcceptHeaderOpenApiResource.class);
    }

    @Override
    public Set<Class<?>> getClasses() {

        return super.getClasses();
    }

    private void OpenAPIDocs() {

        OpenAPI oas = new OpenAPI();

        Info info = new Info()
                .title("Omega API Specification")
                .description("This is an Omega ERP application interface.")
                .version("1.0.0")
                .termsOfService("https://www.omegabci.com/our-process/")
                .contact(new Contact().email("info@omegabci.com"))
                .license(new License().name("About Us").url("https://www.omegabci.com/about/"));

        oas.info(info);

        SwaggerConfiguration oasConfig = new SwaggerConfiguration()
                .openAPI(oas)
                .prettyPrint(true)
                .resourcePackages(Stream.of("com.ejb.restfulapi").collect(Collectors.toSet()));

        try {
            new GenericOpenApiContextBuilder()
                    .openApiConfiguration(oasConfig)
                    .buildContext(true)
                    .read();
        }
        catch (OpenApiConfigurationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}