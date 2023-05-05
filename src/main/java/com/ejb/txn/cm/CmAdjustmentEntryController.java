package com.ejb.txn.cm;

import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.util.cm.CmAdjustmentDetails;
import com.util.mod.ad.AdModBankAccountDetails;
import com.util.ap.ApCheckDetails;
import com.util.mod.ar.ArModCustomerDetails;
import com.util.mod.ar.ArModSalesOrderDetails;
import com.util.mod.cm.CmModAdjustmentDetails;

import jakarta.ejb.Local;

@Local
public interface CmAdjustmentEntryController {

    java.util.ArrayList getAdBaAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    CmModAdjustmentDetails getCmAdjByAdjCode(java.lang.Integer ADJ_CODE, java.lang.Integer AD_CMPNY);


    void checkCmAdjEntryUpload(CmAdjustmentDetails details, java.lang.Integer branchCode, Integer companyCode) throws GlobalReferenceNumberNotUniqueException;

    void saveCmAdjRefundDetails(ApCheckDetails details, java.lang.Integer CHK_CODE, java.lang.String ADJ_DCMNT_NMBR, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException;

    void saveCmAdjRefundDetailsByAdjCode(ApCheckDetails details, java.lang.Integer CHK_CODE, java.lang.Integer ADJ_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException;

    java.lang.Integer saveCmAdjEntry(CmAdjustmentDetails details, java.lang.String BA_NM, java.lang.String CST_NM, java.lang.String SO_NMBR, java.lang.String FC_NM, boolean isDraft, java.lang.Integer PYRLL_PRD, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyApprovedException, GlobalConversionDateNotExistException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalRecordAlreadyAssignedException, GlobalDocumentNumberNotUniqueException, GlobalBranchAccountNumberInvalidException;

    java.util.ArrayList getCmCstAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getCmSplAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    ArModCustomerDetails getCmCstByCstCustomerCode(java.lang.String CST_CSTMR_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    ArModSalesOrderDetails getCmSoBySoNumber(java.lang.String SO_NMBR, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    void deleteCmAdjEntry(java.lang.Integer ADJ_CODE, java.lang.String AD_USR, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException;

    java.util.ArrayList getAdApprovalNotifiedUsersByAdjCode(java.lang.Integer ADJ_CODE, java.lang.Integer AD_CMPNY);

    AdModBankAccountDetails getAdBaByBaName(java.lang.String BA_NM, java.lang.Integer AD_CMPNY);

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    double getFrRateByFrNameAndFrDate(java.lang.String FC_NM, java.util.Date CONVERSION_DATE, java.lang.Integer AD_CMPNY) throws GlobalConversionDateNotExistException;

}