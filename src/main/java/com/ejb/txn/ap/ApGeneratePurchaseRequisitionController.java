package com.ejb.txn.ap;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.mod.ap.ApModPurchaseRequisitionDetails;

import jakarta.ejb.Local;

@Local
public interface ApGeneratePurchaseRequisitionController {

    java.util.ArrayList getAdBrAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApGenPRLinesByCriteria(java.util.HashMap criteria, java.lang.String ORDER_BY, java.util.ArrayList branchList, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getAdLvInvItemCategoryAll(java.lang.Integer AD_CMPNY);

    java.lang.Integer generateApPurchaseRequisition(ApModPurchaseRequisitionDetails mdetails, java.util.ArrayList prlList, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    short getInvGpQuantityPrecisionUnit(java.lang.Integer AD_CMPNY);

}