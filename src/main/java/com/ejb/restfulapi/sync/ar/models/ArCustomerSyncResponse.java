package com.ejb.restfulapi.sync.ar.models;

public class ArCustomerSyncResponse {
    private String message;
    private String status;
    private String statusCode;
    private String[] result;
    private int count;

    public int getCount() {

        return count;
    }

    public void setCount(int count) {

        this.count = count;
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

    public String[] getResult() {

        return result;
    }

    public void setResult(String[] result) {

        this.result = result;
    }

}