package com.ejb.txnreports.inv;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.FinderException;
import jakarta.ejb.Local;


@Local
public interface InvRepAdjustmentRegisterController {

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdBrResAll(java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    java.lang.Double getforwardedBal();

    java.util.ArrayList executeInvRepAdjustmentRegister(java.util.HashMap criteria, java.lang.String ORDER_BY, java.lang.String GROUP_BY, boolean SHOW_LN_ITEMS, java.lang.String DateFrom, java.lang.String DateTo, java.util.ArrayList adBrnchList, java.lang.String REPORT_TYPE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException, FinderException;

}