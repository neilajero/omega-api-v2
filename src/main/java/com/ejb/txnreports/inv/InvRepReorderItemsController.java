package com.ejb.txnreports.inv;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface InvRepReorderItemsController {

    java.util.ArrayList executeInvRepReorderItems(java.util.HashMap criteria, java.lang.String ORDER_BY, java.lang.String GROUP_BY, java.lang.String type, java.util.ArrayList branchList, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

}