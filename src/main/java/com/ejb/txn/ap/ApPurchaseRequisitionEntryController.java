package com.ejb.txn.ap;

import jakarta.ejb.Local;

import com.ejb.exception.global.*;
import com.util.SendEmailDetails;
import com.util.mod.ap.ApModPurchaseRequisitionDetails;
import com.util.mod.ap.ApModPurchaseRequisitionLineDetails;
import com.util.ap.ApPurchaseRequisitionDetails;

@Local
public interface ApPurchaseRequisitionEntryController {

    java.util.ArrayList getApTcAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdPrType(java.lang.Integer AD_CMPNY);

    boolean getInvTraceMisc(java.lang.String II_NAME, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getInvTbAllByItemName(java.lang.String itemName);

    double getInvTbForItemForCurrentMonth(java.lang.String itemName, java.lang.String userName, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getGlFcAllWithDefault(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getInvLocAll(java.lang.Integer AD_CMPNY);

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    short getInvGpQuantityPrecisionUnit(java.lang.Integer AD_CMPNY);

    short getAdPrfApJournalLineNumber(java.lang.Integer AD_CMPNY);

    java.lang.String getAdPrfApDefaultPrTax(java.lang.Integer AD_CMPNY);

    java.lang.String getAdPrfApDefaultPrCurrency(java.lang.Integer AD_CMPNY);

    ApModPurchaseRequisitionDetails getApPrByPrCode(java.lang.Integer PR_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    ApModPurchaseRequisitionLineDetails getApCnvByPrlCode(ApModPurchaseRequisitionLineDetails mdetails, java.lang.Integer PRL_CODE, java.lang.Integer AD_CMPNY);

    java.lang.Integer getApPrlCodeByPrlLineAndPrCode(short PRL_LN, java.lang.Integer PR_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    java.lang.Integer saveApPrCanvass(ApPurchaseRequisitionDetails details, java.lang.String RV_AD_NTFD_USR1, java.lang.String TC_NM, java.lang.String FC_NM, java.util.ArrayList prlList, boolean isDraft, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalInvItemLocationNotFoundException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalSupplierItemInvalidException, GlobalRecordInvalidException;

    java.lang.Integer saveApPrEntry(ApPurchaseRequisitionDetails details, java.lang.String RV_AD_NTFD_USR1, java.lang.String TC_NM, java.lang.String FC_NM, java.util.ArrayList prlList, boolean isDraft, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalInvItemLocationNotFoundException, GlobalSupplierItemInvalidException, GlobalRecordInvalidException, GlobalNoApprovalApproverFoundException, GlobalNoApprovalRequesterFoundException, GlobalInsufficientBudgetQuantityException, GlobalSendEmailMessageException;

    void deleteApPrEntry(java.lang.Integer PR_CODE, java.lang.String AD_USR, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException;

    java.lang.Integer generateApPo(java.lang.Integer PR_CODE, java.lang.String CRTD_BY, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    Integer generateCanvassToPo(Integer PR_CODE, String CRTD_BY, Integer AD_BRNCH, Integer AD_CMPNY);

    java.util.ArrayList getInvUomByIiName(java.lang.String II_NM, java.lang.Integer AD_CMPNY);

    double getInvIiUnitCostByIiNameAndUomName(java.lang.String II_NM, java.lang.String LOC_NM, java.lang.String UOM_NM, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    double getFrRateByFrNameAndFrDate(java.lang.String FC_NM, java.util.Date CONVERSION_DATE, java.lang.Integer AD_CMPNY) throws GlobalConversionDateNotExistException;

    byte getAdPrfApShowPrCost(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdApprovalNotifiedUsersByPrCode(java.lang.Integer PR_CODE, SendEmailDetails sendEmailDetails, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdApprovalNotifiedUsersByPrCodeCanvass(java.lang.Integer PR_CODE, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdUsrAll(java.lang.Integer AD_CMPNY);

    java.lang.String getAdUsrDepartment(java.lang.String USR_NM, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdUsrByDepartmentAll(java.lang.String USR_DEPT, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvPurchaseRequisitionType(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvPurchaseRequisitionMisc1(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvPurchaseRequisitionMisc2(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvPurchaseRequisitionMisc3(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvPurchaseRequisitionMisc4(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvPurchaseRequisitionMisc5(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvPurchaseRequisitionMisc6(java.lang.Integer AD_CMPNY);

    String getSupplierCodeFromApCanvass(Integer PR_CODE, Integer AD_CMPNY);

}