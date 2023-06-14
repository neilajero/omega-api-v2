package com.ejb.restfulapi.sync.ad.models;

public class BranchSyncResponse {
    private String message;
    private String status;
    private String statusCode;
    private String[] results;

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {

        this.message = message;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }

    public String getStatusCode() {

        return statusCode;
    }

    public void setStatusCode(String statusCode) {

        this.statusCode = statusCode;
    }

    public String[] getResults() {

        return results;
    }

    public void setResults(String[] results) {

        this.results = results;
    }
}