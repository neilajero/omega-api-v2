package com.ejb.txnreports.ap;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface ApRepWithholdingTaxCodeListController {

    java.util.ArrayList executeApRepWithholdingTaxCodeList(java.util.HashMap criteria, java.lang.String ORDER_BY, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

}