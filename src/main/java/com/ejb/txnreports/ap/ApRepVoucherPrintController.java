package com.ejb.txnreports.ap;

import com.ejb.exception.gen.GenVSVNoValueSetValueFoundException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface ApRepVoucherPrintController {

    java.util.ArrayList executeApRepVoucherPrint(java.util.ArrayList vouCodeList, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException, GenVSVNoValueSetValueFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

}