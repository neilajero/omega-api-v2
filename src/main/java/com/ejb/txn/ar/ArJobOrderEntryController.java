package com.ejb.txn.ar;

import com.ejb.exception.global.*;
import com.util.ar.ArTaxCodeDetails;
import com.util.mod.ar.ArModCustomerDetails;
import com.util.mod.ar.ArModJobOrderDetails;
import com.util.mod.ar.ArModJobOrderTypeDetails;

import jakarta.ejb.Local;

@Local
public interface ArJobOrderEntryController {

    java.lang.Integer saveArJoEntry(ArModJobOrderDetails details, java.lang.String PYT_NM, java.lang.String TC_NM, java.lang.String FC_NM, java.lang.String CST_CSTMR_CODE, java.util.ArrayList jolList, boolean isDraft, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyVoidException, GlobalJournalNotBalanceException, GlobalInvItemLocationNotFoundException, GlobalNoApprovalApproverFoundException, GlobalNoApprovalRequesterFoundException, GlobalRecordAlreadyAssignedException;

    void deleteArJoEntry(java.lang.Integer JO_CODE, java.lang.String AD_USR, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException;

    ArTaxCodeDetails getArTcByTcName(java.lang.String TC_NM, java.lang.Integer AD_CMPNY);

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    short getInvGpQuantityPrecisionUnit(java.lang.Integer AD_CMPNY);

    short getAdPrfArJournalLineNumber(java.lang.Integer AD_CMPNY);

    ArModJobOrderDetails getArJoBySoCode(java.lang.Integer JO_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    ArModCustomerDetails getArCstByCstCustomerCode(java.lang.String CST_CSTMR_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getInvUomByIiName(java.lang.String II_NM, java.lang.Integer AD_CMPNY);

    double getIiSalesPriceByInvCstCustomerCodeAndIiNameAndUomName(java.lang.String CST_CSTMR_CODE, java.lang.String II_NM, java.lang.String UOM_NM, java.lang.Integer AD_CMPNY);

    ArModJobOrderTypeDetails getArJobOrderTypeByJotNm(java.lang.String JOT_NM, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdApprovalNotifiedUsersBySoCode(java.lang.Integer SO_CODE, java.lang.Integer AD_CMPNY);

    double getFrRateByFrNameAndFrDate(java.lang.String FC_NM, java.util.Date CONVERSION_DATE, java.lang.Integer AD_CMPNY) throws GlobalConversionDateNotExistException;

    java.util.ArrayList getInvLitByCstLitName(java.lang.String CST_LIT_NAME, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.lang.String getInvIiClassByIiName(java.lang.String II_NM, java.lang.Integer AD_CMPNY);

    byte getAdPrfArEnablePaymentTerm(java.lang.Integer AD_CMPNY);

    boolean getJoTraceMisc(java.lang.String II_NAME, java.lang.Integer AD_CMPNY);

    boolean getJoJobServices(java.lang.String II_NAME, java.lang.Integer AD_CMPNY);

    byte getAdPrfArDisableSalesPrice(java.lang.Integer AD_CMPNY);

    boolean getInvTraceMisc(java.lang.String II_NAME, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAllJobOrderTypeName(java.lang.Integer AD_CMPNY);

    java.lang.Integer getArJolCodeByJolLineAndJoCode(short JOL_LN, java.lang.Integer JO_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

}