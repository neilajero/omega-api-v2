package com.ejb.txnreports.ar;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface ArRepJobStatusReportController {

    java.util.ArrayList executeArRepJobStatusReport(java.util.HashMap criteria, java.util.ArrayList branchList, java.lang.String personelName, java.lang.String invoiceStatus, java.lang.String ORDER_BY, java.lang.String GROUP_BY, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAllJobOrderTypeName(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArPeAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArPtAll(java.lang.Integer AD_CMPNY);

}