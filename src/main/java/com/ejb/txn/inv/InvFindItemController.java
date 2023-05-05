package com.ejb.txn.inv;

import com.ejb.exception.global.GlobalNoRecordFoundException;

import jakarta.ejb.Local;

@Local
public interface InvFindItemController {

    double getIiSalesPriceByInvCstCustomerCodeAndIiNameAndUomName(java.lang.String CST_CSTMR_CODE, java.lang.String II_NM,
                                                                  java.lang.String UOM_NM, java.lang.Integer AD_CMPNY);

    java.lang.String getIiDealPriceByInvCstCustomerCodeAndIiNameAndUomName(java.lang.String CST_CSTMR_CODE, java.lang.String II_NM,
                                                                           java.lang.String UOM_NM, java.lang.Integer AD_CMPNY);

    double getInvCstRemainingQuantityByIiNameAndLocNameAndUomName(java.lang.String II_NM, java.lang.String LOC_NM, java.lang.String UOM_NM,
                                                                  java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvInvItemCategoryAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getInvIiByCriteria(java.util.HashMap criteria, java.lang.Integer OFFSET, java.lang.Integer LIMIT,
                                           java.lang.String ORDER_BY, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)
            throws GlobalNoRecordFoundException;

    java.lang.Integer getInvIiSizeByCriteria(java.util.HashMap criteria, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)
            throws GlobalNoRecordFoundException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

}