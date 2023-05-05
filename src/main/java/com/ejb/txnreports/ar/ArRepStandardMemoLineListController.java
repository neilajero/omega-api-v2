package com.ejb.txnreports.ar;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface ArRepStandardMemoLineListController {

    java.util.ArrayList getAdBrResAll(java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList executeArRepStandardMemoLineList(java.util.HashMap criteria, java.util.ArrayList branchList, java.lang.String ORDER_BY, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

}