package com.ejb.txnreports.inv;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface InvRepInventoryProfitabilityController {

    byte getAdPrfEnableInvShift(java.lang.Integer AD_CMPNY);

    java.util.ArrayList executeInvRepInventoryProfitability(java.util.HashMap criteria, java.lang.String costingMethod, java.util.ArrayList branchList, java.lang.String GROUP_BY, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

}