package com.ejb.txnreports.inv;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface InvRepBranchStockTransferRegisterController {

    java.util.ArrayList executeInvRepBranchStockTransferRegister(java.util.HashMap criteria, java.lang.String ORDER_BY, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

}