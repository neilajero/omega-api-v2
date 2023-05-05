package com.ejb.txnreports.inv;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface InvRepPhysicalInventoryPrintController {

    java.util.ArrayList executeInvRepPhysicalInventoryPrint(java.util.ArrayList piCodeList, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

}