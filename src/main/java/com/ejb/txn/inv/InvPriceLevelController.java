
package com.ejb.txn.inv;

import com.util.mod.inv.InvModItemDetails;

import jakarta.ejb.Local;


@Local
public interface InvPriceLevelController
   
{

   java.util.ArrayList getInvPriceLevelsByIiCode(java.lang.Integer II_CODE, java.lang.Integer AD_CMPNY)
           ;

   void saveInvPl(java.util.ArrayList plList, java.lang.Integer II_CODE, java.lang.Integer AD_CMPNY)
           ;

   short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY)
           ;

   InvModItemDetails getInvIiMarkupValueByIiCode(java.lang.Integer II_CODE, java.lang.Integer AD_CMPNY)
           ;

}