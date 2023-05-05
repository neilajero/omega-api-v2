package com.ejb.txnreports.inv;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface InvRepStockOnHandController {

    java.util.ArrayList executeInvRepStockOnHand(java.util.HashMap criteria, boolean INCLD_ZRS, java.lang.String ORDER_BY, boolean SHW_CMMTTD_QNTTY, boolean INCLD_UNPSTD, boolean INCLD_FRCST, boolean LAYERED, java.util.Date AS_OF_DT, java.lang.String UOM_NM, java.lang.Integer AD_BRNCH, java.util.ArrayList branchList, java.lang.Integer AD_CMPNY, java.lang.String rpt) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getInvUomAll(java.lang.Integer AD_CMPNY);

}