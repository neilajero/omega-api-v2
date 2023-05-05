package com.ejb.restfulapi.filters;

import jakarta.ws.rs.core.SecurityContext;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

public class JwtTokenSecurityContext implements SecurityContext {

    private final String token;
    private final boolean secure;
    private final String scheme;

    public JwtTokenSecurityContext(String token, boolean secure, String scheme) {

        this.token = token;
        this.secure = secure;
        this.scheme = scheme;
    }

    @Override
    public Principal getUserPrincipal() {

        return () -> "user";
    }

    @Override
    public boolean isUserInRole(String role) {

        List<String> userRoles = new ArrayList<>();
        userRoles.add("Admin");
        userRoles.add("User");

        return userRoles.contains(role);
    }

    @Override
    public boolean isSecure() {

        return secure;
    }

    @Override
    public String getAuthenticationScheme() {

        return scheme;
    }

    public String getToken() {

        return token;
    }

}