package com.ejb.restfulapi.reports.ar.models;

public class StatementResponse {
    private String code;
    private String message;
    private byte[] pdfReport;
    private String status;

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }

    public byte[] getPdfReport() {

        return pdfReport;
    }

    public void setPdfReport(byte[] pdfReport) {

        this.pdfReport = pdfReport;
    }

    public String getCode() {

        return code;
    }

    public void setCode(String code) {

        this.code = code;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {

        this.message = message;
    }

}