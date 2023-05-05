package com.ejb.txnreports.inv;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface InvRepMarkupListController {

    java.util.ArrayList executeInvRepMarkupList(java.util.HashMap criteria, java.lang.String UOM_NM, boolean INCLD_ZRS, boolean recalcMarkup, java.lang.String ORDER_BY, java.util.ArrayList priceLevelList, java.util.ArrayList branchList, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdBrResAll(java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getInvUomAll(java.lang.Integer AD_CMPNY);

}