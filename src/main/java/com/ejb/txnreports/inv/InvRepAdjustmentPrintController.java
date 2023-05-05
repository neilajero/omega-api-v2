package com.ejb.txnreports.inv;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface InvRepAdjustmentPrintController {

    java.util.ArrayList executeInvRepAdjustmentPrint(java.util.ArrayList adjCodeList, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    boolean getInvTraceMisc(java.lang.String II_NAME, java.lang.Integer AD_CMPNY);

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    String getPrfApDefaultChecker(java.lang.Integer AD_CMPNY);

}