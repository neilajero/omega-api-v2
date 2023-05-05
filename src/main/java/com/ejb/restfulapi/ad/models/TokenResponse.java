package com.ejb.restfulapi.ad.models;

import java.util.Map;

public class TokenResponse {
    private Map<String, String> tokens;
    public Map<String, String> getTokens() { return tokens; }
    public void setTokens(Map<String, String> tokens) { this.tokens = tokens; }

}