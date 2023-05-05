package com.ejb.txn.ar;

import com.ejb.entities.gl.LocalGlInvestorAccountBalance;
import com.ejb.exception.ar.ArINVAmountExceedsCreditLimitException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.util.ad.AdResponsibilityDetails;
import com.util.ar.ArInvoiceDetails;
import com.util.ar.ArReceiptDetails;
import com.util.mod.ar.ArModStandardMemoLineDetails;

import jakarta.ejb.Local;

@Local
public interface ArStandardMemoLineController {

    java.lang.String generateArInvestorBonusAndInterest(java.lang.String SPL_SUPPLIER_CODE, boolean isRecalculate, java.lang.String PERIOD_MONTH, int PERIOD_YEAR, java.lang.String USER_NM, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException, GlJREffectiveDatePeriodClosedException;

    java.util.ArrayList getSupplierInvestors(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException, GlJREffectiveDatePeriodClosedException;

    int generateArAccruedInterestIS(java.lang.String USER_NM, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException, GlJREffectiveDatePeriodClosedException;

    int generateArAccruedInterestTB(java.lang.String USER_NM, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException, GlJREffectiveDatePeriodClosedException;

    int generateArOverDueInvoices(java.lang.String USER_NM, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException, GlJREffectiveDatePeriodClosedException;

    int generateArPastDueInvoices(java.lang.String USER_NM, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getArSmlAll(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getAdBrAll(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getAdBrSMLAll(java.lang.Integer BSML_CODE, java.lang.String RS_NM, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdResponsibilityDetails getAdRsByRsCode(java.lang.Integer RS_CODE) throws GlobalNoRecordFoundException;

    void addArSmlEntry(ArModStandardMemoLineDetails mdetails, java.lang.String SML_GL_COA_ACCNT_NMBR, java.lang.String CST_GL_COA_RCVBL_ACCNT, java.lang.String CST_GL_COA_RVNUE_ACCNT, java.lang.String SML_INTRM_ACCNT_NMBR, java.util.ArrayList branchList, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException;

    void updateArSmlEntry(ArModStandardMemoLineDetails mdetails, java.lang.String SML_GL_COA_ACCNT_NMBR, java.lang.String SML_GL_COA_RCVBL_ACCNT_NMBR, java.lang.String SML_GL_COA_RVN_ACCNT_NMBR, java.lang.String SML_INTRM_ACCNT_NMBR, java.lang.String RS_NM, java.util.ArrayList branchList, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException;

    void deleteArSmlEntry(java.lang.Integer SML_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    boolean getArSmlGlCoaAccountNumberEnable(java.lang.Integer AD_CMPNY);

    short getInvGpQuantityPrecisionUnit(java.lang.Integer AD_CMPNY);

    double getFrRateByFrNameAndFrDate(java.lang.String FC_NM, java.util.Date CONVERSION_DATE, java.lang.Integer AD_CMPNY) throws GlobalConversionDateNotExistException;

    java.lang.String saveArInvEntry(ArInvoiceDetails details, java.lang.String PYT_NM, java.lang.String TC_NM, java.lang.String WTC_NM, java.lang.String FC_NM, java.lang.String CST_CSTMR_CODE, java.lang.String IB_NM, java.util.ArrayList ilList, boolean isDraft, java.lang.String SLP_SLSPRSN_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, ArINVAmountExceedsCreditLimitException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException;

    java.lang.Integer saveArRctEntry(ArReceiptDetails details, LocalGlInvestorAccountBalance glInvestorAccountBalance, java.lang.String BA_NM, java.lang.String TC_NM, java.lang.String WTC_NM, java.lang.String FC_NM, java.lang.String CST_CSTMR_CODE, java.lang.String RB_NM, java.util.ArrayList ilList, boolean isDraft, java.lang.String SLP_SLSPRSN_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException, GlobalRecordAlreadyAssignedException;

}