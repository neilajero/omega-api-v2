
package com.ejb.txn.inv;

import com.ejb.exception.global.GlobalNoRecordFoundException;

import jakarta.ejb.Local;


@Local
public interface InvUnitOfMeasureConversionController
   
{

   short getAdPrfInvQuantityPrecisionUnit(java.lang.Integer AD_CMPNY)
           ;

   java.util.ArrayList getInvUmcByIiCode(java.lang.Integer II_CODE, java.lang.Integer AD_CMPNY)
      throws GlobalNoRecordFoundException;

   void saveInvUMCEntry(java.lang.Integer II_CODE, java.util.ArrayList umcList, java.lang.Integer AD_CMPNY)
      throws GlobalNoRecordFoundException;

}