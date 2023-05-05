package com.ejb.txnreports.cm;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;
import com.util.reports.cm.CmRepBankReconciliationDetails;

import jakarta.ejb.Local;

@Local
public interface CmRepBankReconciliationController {

    java.util.ArrayList getAdBaAll(java.lang.Integer AD_CMPNY);

    CmRepBankReconciliationDetails executeCmRepBankReconciliation(java.lang.String BA_NM, java.util.Date DT_FRM, java.util.Date DT_TO, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY, java.lang.String isDraftBCBD) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdBrResAll(java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

}