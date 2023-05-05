package com.ejb.txnreports.cm;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface CmRepAdjustmentPrintController {

    java.util.ArrayList executeCmRepAdjustmentPrint(java.util.ArrayList adjCodeList, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

}