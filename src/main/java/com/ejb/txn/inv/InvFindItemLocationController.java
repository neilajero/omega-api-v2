
package com.ejb.txn.inv;

import com.ejb.exception.global.GlobalNoRecordFoundException;

import jakarta.ejb.Local;


@Local
public interface InvFindItemLocationController
   
{

   java.util.ArrayList getAdLvInvItemCategoryAll(java.lang.Integer AD_CMPNY)
           ;

   java.util.ArrayList getInvIiAll(java.lang.Integer AD_CMPNY)
           ;

   java.util.ArrayList getInvLocAll(java.lang.Integer AD_CMPNY)
           ;

   short getAdPrfInvQuantityPrecisionUnit(java.lang.Integer AD_CMPNY)
           ;

   java.util.ArrayList getAdBrResAll(java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY)
      throws GlobalNoRecordFoundException;

   java.util.ArrayList getInvIlByCriteria(java.util.HashMap criteria, java.util.ArrayList branchList, java.lang.Integer OFFSET, java.lang.Integer LIMIT, java.lang.String ORDER_BY, java.lang.Integer AD_CMPNY)
      throws GlobalNoRecordFoundException;

   java.lang.Integer getInvIlSizeByCriteria(java.util.HashMap criteria, java.util.ArrayList branchList, java.lang.Integer AD_CMPNY)
      throws GlobalNoRecordFoundException;

}