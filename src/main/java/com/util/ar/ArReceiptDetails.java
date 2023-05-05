package com.util.ar;

import com.ejb.entities.ar.LocalArReceipt;

public class ArReceiptDetails extends LocalArReceipt implements java.io.Serializable{

    private String companyShortName = null;
    public ArReceiptDetails(){}

    public String getCompanyShortName() { return companyShortName; }

    public void setCompanyShortName(String companyShortName) { this.companyShortName = companyShortName; }
}