package com.ejb.restfulapi.sync.ar.models;

public class ArStandardMemoLineSyncResponse {

    private String message;
    private String status;
    private String statusCode;
    private int count;
    private String[] results;

    public String[] getResults() {

        return results;
    }

    public void setResults(String[] results) {

        this.results = results;
    }

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

    public int getCount() {

        return count;
    }

    public void setCount(int count) {

        this.count = count;
    }

}