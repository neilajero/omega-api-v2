package com.ejb.txnreports.inv;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface InvRepInventoryListController {

    java.util.ArrayList executeInvRepInventoryList(java.util.HashMap criteria, java.lang.String UOM_NM, java.util.Date AS_OF_DT, boolean INCLD_ZRS, boolean SHW_CMMTTD_QNTTY, boolean INCLD_UNPSTD, boolean SHW_TGS, java.lang.String ORDER_BY, java.util.ArrayList priceLevelList, java.util.ArrayList branchList, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY, java.lang.String EXPRY_DT) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getInvUomAll(java.lang.Integer AD_CMPNY);

}