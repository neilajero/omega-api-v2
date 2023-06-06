package com.ejb.txn.ar;

import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.ar.ArINVOverapplicationNotAllowedException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.util.ar.ArInvoiceDetails;
import com.util.mod.ar.ArModInvoiceDetails;

import jakarta.ejb.Local;

@Local
public interface ArCreditMemoEntryController {

    java.util.Date getArInvoiceDateByInvoiceNumber(java.lang.String INV_NMBR, java.lang.Integer INV_AD_BRNCH, java.lang.Integer INV_AD_CMPNY);

    double getArInvoiceAmountDueByInvoiceNumber(java.lang.String INV_NMBR, java.lang.Integer INV_AD_BRNCH, java.lang.Integer INV_AD_CMPNY);

    java.util.ArrayList getArCstAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getInvLocAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getInvUomByIiName(java.lang.String II_NM, java.lang.Integer AD_CMPNY);

    double getIiSalesPriceByInvCstCustomerCodeAndIiNameAndUomName(java.lang.String CST_CSTMR_CODE, java.lang.String II_NM, java.lang.String UOM_NM, java.lang.Integer AD_CMPNY);

    short getInvGpQuantityPrecisionUnit(java.lang.Integer AD_CMPNY);

    short getAdPrfArInvoiceLineNumber(java.lang.Integer AD_CMPNY);

    ArModInvoiceDetails getArInvByInvCode(java.lang.Integer INV_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    byte getAdPrfEnableInvShift(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvInvShiftAll(java.lang.Integer AD_CMPNY);

    java.lang.Integer saveArCmInvEntry(ArInvoiceDetails details, String CST_CSTMR_CODE, String IB_NM, Integer AD_BRNCH, Integer AD_CMPNY);

    java.lang.Integer saveArInvEntry(ArInvoiceDetails details, java.lang.String CST_CSTMR_CODE, java.lang.String IB_NM,
                                     java.util.ArrayList drList, boolean isDraft, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)
            throws GlobalRecordAlreadyDeletedException, GlobalNoRecordFoundException, GlobalDocumentNumberNotUniqueException,
            GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException,
            GlobalTransactionAlreadyVoidPostedException, ArINVOverapplicationNotAllowedException, GlobalTransactionAlreadyLockedException,
            GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlJREffectiveDateNoPeriodExistException,
            GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalAccountNumberInvalidException,
            GlobalBranchAccountNumberInvalidException;

    java.lang.Integer saveArInvIliEntry(ArInvoiceDetails details, java.lang.String CST_CSTMR_CODE, java.lang.String IB_NM,
                                        java.util.ArrayList iliList, boolean isDraft, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)
            throws GlobalRecordAlreadyDeletedException, GlobalNoRecordFoundException, GlobalDocumentNumberNotUniqueException,
            GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException,
            GlobalTransactionAlreadyVoidPostedException, ArINVOverapplicationNotAllowedException, GlobalTransactionAlreadyLockedException,
            GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException,
            GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException,
            GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException;

    void deleteArInvEntry(java.lang.Integer INV_CODE, java.lang.String AD_USR, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)
            throws GlobalRecordAlreadyDeletedException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdApprovalNotifiedUsersByInvCode(java.lang.Integer INV_CODE, java.lang.Integer AD_CMPNY);

    byte getAdPrfEnableArCreditMemoBatch(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArOpenIbAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArSoInvLnItmByArInvNmbr(java.lang.String AR_INV_NMBR,
                                                   java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getArJoInvLnItmByArInvNmbr(java.lang.String AR_INV_NMBR,
                                                   java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    ArModInvoiceDetails getArInvByArInvNmbr(java.lang.String AR_INV_NMBR, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getArInvLnItmByArInvNmbr(java.lang.String AR_INV_NMBR, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getArDrByInvCmInvoiceNumberAndInvBillAmountAndCstCustomerCode(java.lang.String INV_CM_INVC_NMBR, double INV_BLL_AMNT, java.lang.String CST_CSTMR_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException, ArINVOverapplicationNotAllowedException;

    java.lang.String getInvItemClassByIiName(java.lang.String II_NM, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.lang.String getArCstCustomerCodeByArInvNmbr(java.lang.String AR_INV_NMBR, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.lang.String getArCstNameByCstCustomerCode(java.lang.String CST_CSTMR_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getArCreditMemoReportParameters(java.lang.Integer AD_CMPNY);

}