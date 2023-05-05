package com.ejb.txnreports.ar;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface ArRepSalesController {

    java.util.ArrayList executeArRepSales(java.util.HashMap criteria, java.util.ArrayList branchList, java.lang.String GROUP_BY, java.lang.String ORDER_BY, boolean INCLUDECM, boolean INCLUDEINVOICE, boolean INCLUDEMISC, boolean INCLUDECOLLECTION, boolean SHOW_ENTRIES, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList executeArRepSalesSub(java.util.HashMap criteria, java.util.ArrayList branchList, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

}