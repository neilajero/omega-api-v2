package com.ejb.restfulapi.filters;

import com.ejb.ConfigurationClass;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.IOException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.Priority;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class OmegaJWTTokenFilter implements ContainerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String OPENAPI_ENDPOINT = "/openapi.yaml";
    private static final String AUTH_ENDPOINT = "/auth/user";
    private static final String LOGOUT_ENDPOINT = "/auth/logout";
    private static final String SECRET_KEY = ConfigurationClass.SECRETKEY;
    byte[] secretKeyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
    Key key = Keys.hmacShaKeyFor(secretKeyBytes);

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        // check if the endpoint is the openapi.yaml endpoint
        if (requestContext.getUriInfo().getRequestUri().getPath().endsWith(OPENAPI_ENDPOINT) ||
                requestContext.getUriInfo().getRequestUri().getPath().endsWith(AUTH_ENDPOINT) ||
                requestContext.getUriInfo().getRequestUri().getPath().endsWith(LOGOUT_ENDPOINT)) {
            return; // skip token validation
        }

        // Get the JWT token from the request headers
        String token = requestContext.getHeaderString(AUTHORIZATION_HEADER);
        if (token == null || !token.startsWith(BEARER_PREFIX)) {
            throw new NotAuthorizedException("Authorization header is missing or invalid");
        }

        // Validate the JWT token
        boolean isValid = validateJwtToken(token.substring(7)); // Remove the "Bearer " prefix
        if (!isValid) {
            throw new NotAuthorizedException("Invalid JWT token");
        }

        // Set the security context for the request
        SecurityContext securityContext = (SecurityContext) new JwtTokenSecurityContext(token, true, "JWT");
        requestContext.setSecurityContext(securityContext);
    }

    private boolean validateJwtToken(String jwtToken) {

        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();
            return true;
        }
        catch (SecurityException | ExpiredJwtException |
               SignatureException | MalformedJwtException e) {
            return false;
        }
    }

}