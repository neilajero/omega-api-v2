package com.ejb.txn.ap;

import com.ejb.exception.global.*;
import com.util.mod.ap.ApModRecurringVoucherDetails;
import com.util.mod.ap.ApModSupplierDetails;
import com.util.ap.ApRecurringVoucherDetails;

import jakarta.ejb.Local;


@Local
public interface ApRecurringVoucherEntryController {

    java.util.ArrayList getGlFcAllWithDefault(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdPytAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApTcAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdUsrAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApWtcAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApOpenVbAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    short getAdPrfApJournalLineNumber(java.lang.Integer AD_CMPNY);

    byte getAdPrfEnableApVoucherBatch(java.lang.Integer AD_CMPNY);

    ApModRecurringVoucherDetails getApRvByRvCode(java.lang.Integer RV_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    ApModSupplierDetails getApSplBySplSupplierCode(java.lang.String SPL_SPPLR_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getApDrBySplSupplierCodeAndTcNameAndWtcNameAndRvAmount(java.lang.String SPL_SPPLR_CODE, java.lang.String TC_NM, java.lang.String WTC_NM, double RV_AMNT, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    void saveApRvEntry(ApRecurringVoucherDetails details, java.lang.String RV_AD_NTFD_USR1, java.lang.String RV_AD_NTFD_USR2, java.lang.String RV_AD_NTFD_USR3, java.lang.String RV_AD_NTFD_USR4, java.lang.String RV_AD_NTFD_USR5, java.lang.String PYT_NM, java.lang.String TC_NM, java.lang.String WTC_NM, java.lang.String FC_NM, java.lang.String SPL_SPPLR_CODE, java.util.ArrayList drList, java.lang.String VB_NM, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalRecordAlreadyDeletedException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, GlobalBranchAccountNumberInvalidException, GlobalRecordInvalidException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    void deleteApRvEntry(java.lang.Integer RV_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException;

    byte getAdPrfApUseSupplierPulldown(java.lang.Integer AD_CMPNY);

    double getFrRateByFrNameAndFrDate(java.lang.String FC_NM, java.util.Date CONVERSION_DATE, java.lang.Integer AD_CMPNY) throws GlobalConversionDateNotExistException;

}