package com.ejb.txnreports.ar;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;


@Local
public interface ArRepCustomerListController {

    void executeSpArRepCustomerList(String STORED_PROCEDURE, String CUSTOMER_CODE, Integer AD_COMPANY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getArCcAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArCtAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdBrResAll(java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList executeArRepCustomerList(java.util.HashMap criteria, java.util.ArrayList branchList, java.lang.String date, java.lang.String salesPerson, java.lang.String ORDER_BY, java.lang.String GROUP_BY, boolean splitBySalesperson, boolean includedNegativeBalances, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

}