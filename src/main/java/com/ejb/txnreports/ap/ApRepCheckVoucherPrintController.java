package com.ejb.txnreports.ap;

import com.ejb.exception.gen.GenVSVNoValueSetValueFoundException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface ApRepCheckVoucherPrintController {

    java.util.ArrayList executeApRepCheckVoucherPrint(java.util.ArrayList chkCodeList, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException, GenVSVNoValueSetValueFoundException;

    java.util.ArrayList executeApRepCheckVoucherPrintSub(java.util.ArrayList chkCodeList, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    java.lang.String getAdPrfApCheckVoucherDataSource(java.lang.Integer AD_CMPNY);

}