package com.ejb.txn.ar;

import com.ejb.exception.ad.AdPRFCoaGlCustomerDepositAccountNotFoundException;
import com.ejb.exception.ar.*;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.util.ar.ArReceiptDetails;
import com.util.mod.ar.ArModCustomerDetails;
import com.util.mod.ar.ArModReceiptDetails;

import jakarta.ejb.Local;

@Local
public interface ArReceiptEntryController {

    java.util.ArrayList getGlFcAllWithDefault(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdBaAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    int getArCstAdBrnchByEmplyId(java.lang.String EMP_ID, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException, GlobalDuplicateEmployeeIdException;

    int getArCstAdBrnchByCustomerCode(java.lang.String CUSTOMER_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException, GlobalDuplicateCustomerCodeException;

    java.lang.String getArCstCstmrCodeByEmplyId(java.lang.String EMP_ID, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException, GlobalDuplicateEmployeeIdException;

    ArModReceiptDetails getArRctByRctNum(java.lang.String RCT_NMBR, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    ArModReceiptDetails getArRctByRctCode(java.lang.Integer RCT_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    ArModCustomerDetails getArCstByCstCustomerCode(java.lang.String CST_CSTMR_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getArIpsByInvcNmbr(java.lang.String AR_INVC_NMBR, java.util.Date RCT_DT, boolean ENBL_RBT, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getArIpsByCstCustomerCode(java.lang.String CST_CSTMR_CODE, java.util.Date RCT_DT, boolean ENBL_RBT, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    public java.util.ArrayList getArIpsByReferenceAndCstCustomerCode(java.lang.String CST_CSTMR_CODE, java.util.Date RCT_DT, boolean ENBL_RBT, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException, ArReceiptInvoiceAlreadyPaidException;

    java.util.ArrayList getArIpsByIpsCode(java.lang.Integer IPS_CODE, boolean ENBL_RBT, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    boolean validateDuplicateCollectionTransaction(ArReceiptDetails details, java.lang.Integer AD_CMPNY);

    java.lang.Integer saveArRctEntry(ArReceiptDetails details, boolean recalculateJournal, java.lang.String BA_NM, java.lang.String FC_NM, java.lang.String CST_CSTMR_CODE, java.lang.String RB_NM, java.util.ArrayList aiList, boolean isDraft, boolean isValidating, java.lang.String PP_NM, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws ArReceiptEntryValidationException, GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, ArINVOverCreditBalancePaidapplicationNotAllowedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, ArINVOverapplicationNotAllowedException, GlobalTransactionAlreadyLockedException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, ArRCTInvoiceHasNoWTaxCodeException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException, GlobalRecordAlreadyAssignedException, AdPRFCoaGlCustomerDepositAccountNotFoundException, ArREDuplicatePayfileReferenceNumberException;

    void deleteArRctEntry(java.lang.Integer RCT_CODE, java.lang.String AD_USR, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdApprovalNotifiedUsersByRctCode(java.lang.Integer RCT_CODE, java.lang.Integer AD_CMPNY);

    byte getAdPrfEnableArReceiptBatch(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArOpenRbAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    double getAiCreditBalancePaidByRctCodeByInvNum(java.lang.Integer IPS_CODE, java.lang.Integer RCT_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    double getArRctCreditBalanceByCstCustomerCode(java.lang.String CST_CSTMR_CODE, java.lang.Integer AD_BRANCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    boolean checkIfExistArRctReferenceNumber(java.lang.String RCT_RFRNC_NMBR, java.lang.Integer AD_CMPNY);

    double getArRctDepositAmountByCstCustomerCode(java.lang.String CST_CSTMR_CODE, java.lang.Integer AD_BRANCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    double getFrRateByFrNameAndFrDate(java.lang.String FC_NM, java.util.Date CONVERSION_DATE, java.lang.Integer AD_CMPNY) throws GlobalConversionDateNotExistException;

    java.lang.String createAdvancePayment(ArReceiptDetails rctDetails, java.lang.String customer_code, java.lang.String BA_NM, java.lang.String payfileReference, double advanceAmount, int AD_BRNCH, int AD_CMPNY) throws java.lang.Exception;

    java.util.ArrayList getArReceiptReportParameters(java.lang.Integer AD_CMPNY);

    byte getAdPrfArUseCustomerPulldown(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getDocumentTypeList(java.lang.String DCMNT_TYP, java.lang.Integer AD_CMPNY);
}