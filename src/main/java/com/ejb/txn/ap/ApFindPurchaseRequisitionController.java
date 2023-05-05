package com.ejb.txn.ap;

import com.ejb.exception.global.*;
import com.util.mod.ap.ApModPurchaseOrderDetails;
import com.util.mod.ap.ApModPurchaseRequisitionDetails;
import com.util.ap.ApPurchaseOrderDetails;

import jakarta.ejb.Local;


@Local
public interface ApFindPurchaseRequisitionController {

    java.util.ArrayList getAdUsrAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvDEPARTMENT(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlFcAll(java.lang.Integer AD_CMPNY);

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApPrByCriteria(java.util.HashMap criteria, java.lang.Integer OFFSET, java.lang.Integer LIMIT, java.lang.String ORDER_BY, java.lang.Integer AD_BRNCH, boolean omitBranch, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.lang.Integer getApPrSizeByCriteria(java.util.HashMap criteria, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList generateApPo(java.lang.Integer PR_CODE, java.lang.String CRTD_BY, java.lang.String BR_BRNCH_CODE, java.lang.Integer AD_CMPNY);

    java.lang.Integer saveApPoEntry(ApPurchaseOrderDetails details, java.lang.String PYT_NM, java.lang.String TC_NM, java.lang.String FC_NM, java.lang.String SPL_SPPLR_CODE, java.util.ArrayList plList, boolean isDraft, java.lang.String BR_BRNCH_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalInvItemLocationNotFoundException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalSupplierItemInvalidException;

    ApModPurchaseOrderDetails getApPoByPoCode(java.lang.Integer PO_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    ApModPurchaseRequisitionDetails getApPrByPrCode(java.lang.Integer PR_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.lang.Integer generateApPr(java.util.ArrayList prSelectedList, java.lang.Integer AD_BRNCH, java.lang.String AD_USR, java.lang.Integer AD_CMPNY);

    java.lang.Integer consolidateApPr(java.util.ArrayList prSelectedList, java.lang.Integer AD_BRNCH, java.lang.String AD_USR, java.lang.Integer AD_CMPNY);

}