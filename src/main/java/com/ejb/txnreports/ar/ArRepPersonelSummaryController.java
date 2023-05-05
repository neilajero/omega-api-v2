package com.ejb.txnreports.ar;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface ArRepPersonelSummaryController {
    java.util.ArrayList executeArRepPersonelSummary(java.util.HashMap criteria, java.util.ArrayList branchList, java.lang.String personelName, java.lang.String GROUP_BY, boolean includeUnposted, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAllJobOrderTypeName(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArPeAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

}