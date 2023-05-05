package com.ejb.txnreports.inv;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface InvRepCostOfSalesController {

    java.util.ArrayList executeInvRepCostOfSales(java.util.HashMap criteria, java.lang.String costingMethod, java.util.ArrayList categoryList, java.util.ArrayList branchList, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

}