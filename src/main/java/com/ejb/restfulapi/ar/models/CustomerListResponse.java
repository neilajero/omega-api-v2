package com.ejb.restfulapi.ar.models;

import java.util.ArrayList;
import java.util.List;

public class CustomerListResponse {
    private List<String> customerCodes = new ArrayList<>();
    private String message;
    private String status;
    private String statusCode;

    public String getStatusCode() {

        return statusCode;
    }

    public void setStatusCode(String statusCode) {

        this.statusCode = statusCode;
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

    public List<String> getCustomerCodes() {

        return customerCodes;
    }

    public void setCustomerCodes(List<String> customerCodes) {

        this.customerCodes = customerCodes;
    }

}