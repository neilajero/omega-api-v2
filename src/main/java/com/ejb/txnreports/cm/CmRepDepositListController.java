package com.ejb.txnreports.cm;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;


@Local
public interface CmRepDepositListController {

    java.util.ArrayList getArCcAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArCtAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdBaAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdBrResAll(java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList executeCmRepDepositList(java.util.HashMap criteria, java.util.ArrayList branchList, java.lang.String ORDER_BY, java.lang.String GROUP_BY, java.lang.String status, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

}