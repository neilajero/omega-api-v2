package com.ejb.restfulapi.ar.models;

public class InvoiceMemoLineResponse {
    private String statusCode;
    private String message;
    private String status;
    private String invoiceNumber;

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

    public String getInvoiceNumber() {

        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {

        this.invoiceNumber = invoiceNumber;
    }

}