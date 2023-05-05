
package com.ejb.txn.inv;

import com.ejb.exception.global.GlobalNoRecordFoundException;

import jakarta.ejb.Local;


@Local
public interface InvFindBranchStockTransferController
   
{

   java.util.ArrayList getInvBstByCriteria(java.util.HashMap criteria, java.lang.Integer OFFSET, java.lang.Integer LIMIT, java.lang.String ORDER_BY, boolean isBstLookup, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)
      throws GlobalNoRecordFoundException;

   java.lang.Integer getInvBstSizeByCriteria(java.util.HashMap criteria, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)
      throws GlobalNoRecordFoundException;

}