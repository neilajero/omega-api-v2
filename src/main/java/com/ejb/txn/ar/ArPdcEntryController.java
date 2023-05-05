package com.ejb.txn.ar;

import com.ejb.exception.ar.ArINVOverapplicationNotAllowedException;
import com.ejb.exception.ar.ArRCTInvoiceHasNoWTaxCodeException;
import com.ejb.exception.global.*;
import com.util.ar.ArPdcDetails;
import com.util.ar.ArStandardMemoLineDetails;
import com.util.mod.ar.ArModCustomerDetails;
import com.util.mod.ar.ArModPdcDetails;

import jakarta.ejb.Local;

@Local
public interface ArPdcEntryController {

    java.util.ArrayList getInvUomByIiName(java.lang.String II_NM, java.lang.Integer AD_CMPNY);

    double getInvIiSalesPriceByIiNameAndUomName(java.lang.String II_NM, java.lang.String UOM_NM, java.lang.Integer AD_CMPNY);

    short getAdPrfArInvoiceLineNumber(java.lang.Integer AD_CMPNY);

    ArModPdcDetails getArPdcByPdcCode(java.lang.Integer PDC_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    ArModCustomerDetails getArCstByCstCustomerCode(java.lang.String CST_CSTMR_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    ArStandardMemoLineDetails getArSmlBySmlName(java.lang.String SML_NM, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.lang.Integer saveArPdcEntry(ArPdcDetails details, java.lang.String PYT_NM, java.lang.String TC_NM, java.lang.String WTC_NM, java.lang.String FC_NM, java.lang.String CST_CSTMR_CODE, java.util.ArrayList ilList, boolean isDraft, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException;

    java.lang.Integer saveArPdcIliEntry(ArPdcDetails details, java.lang.String PYT_NM, java.lang.String TC_NM, java.lang.String WTC_NM, java.lang.String FC_NM, java.lang.String CST_CSTMR_CODE, java.util.ArrayList iliList, boolean isDraft, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalInvItemLocationNotFoundException;

    java.lang.Integer saveArRctEntry(ArPdcDetails details, java.lang.String BA_NM, java.lang.String FC_NM, java.lang.String CST_CSTMR_CODE, java.util.ArrayList ilList, boolean isDraft, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, ArINVOverapplicationNotAllowedException, GlobalTransactionAlreadyLockedException, ArRCTInvoiceHasNoWTaxCodeException;

    void deleteArPdcEntry(java.lang.Integer PDC_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    byte getAdPrfEnableInvShift(java.lang.Integer AD_CMPNY);

    short getInvGpQuantityPrecisionUnit(java.lang.Integer AD_CMPNY);

    double getFrRateByFrNameAndFrDate(java.lang.String FC_NM, java.util.Date CONVERSION_DATE, java.lang.Integer AD_CMPNY) throws GlobalConversionDateNotExistException;

    double getArRctDepositAmountByCstCustomerCode(java.lang.String CST_CSTMR_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getArIpsByCstCustomerCode(java.lang.String CST_CSTMR_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

}