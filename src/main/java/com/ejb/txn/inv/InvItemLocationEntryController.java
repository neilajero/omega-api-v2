package com.ejb.txn.inv;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.inv.*;
import com.util.inv.InvItemDetails;
import com.util.mod.inv.InvModItemLocationDetails;

import jakarta.ejb.Local;

@Local
public interface InvItemLocationEntryController {

	java.util.ArrayList getInvLocAll(java.lang.Integer AD_CMPNY);

	java.util.ArrayList getInvIiAll(java.lang.Integer AD_CMPNY);

	InvItemDetails getInvItemByIiName(java.lang.String II_NM, java.lang.Integer AD_CMPNY)
            ;

	byte getAdPrfInvItemLocationShowAll(java.lang.Integer AD_CMPNY);

	byte getAdPrfInvItemLocationAddByItemList(java.lang.Integer AD_CMPNY);

	java.util.ArrayList getAdLvInvItemCategoryAll(java.lang.Integer AD_CMPNY);

	java.util.ArrayList getAdBrResAll(java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY)
			throws GlobalNoRecordFoundException;

	void saveInvIlEntry(InvModItemLocationDetails mdetails, java.util.ArrayList itemList,
						java.util.ArrayList locationList, java.util.ArrayList branchItemLocationList,
						java.util.ArrayList categoryList, java.lang.String II_CLSS, java.lang.Integer AD_CMPNY)
			throws InvILCoaGlSalesAccountNotFoundException,
			InvILCoaGlSalesReturnAccountNotFoundException,
			InvILCoaGlInventoryAccountNotFoundException,
			InvILCoaGlCostOfSalesAccountNotFoundException,
			InvILCoaGlWipAccountNotFoundException,
			InvILCoaGlAccruedInventoryAccountNotFoundException;

	void saveInvNonIlEntry(InvModItemLocationDetails mdetails, java.util.ArrayList itemList,
						   java.util.ArrayList locationList, java.util.ArrayList branchItemLocationList,
						   java.util.ArrayList categoryList, java.lang.String II_CLSS, java.lang.Integer AD_CMPNY)
			throws InvILCoaGlAccruedInventoryAccountNotFoundException;

	void updateInvIliAveSales(java.util.ArrayList itemList, java.util.ArrayList locationList,
                              java.util.ArrayList branchItemLocationList, java.util.ArrayList categoryList, java.lang.String II_CLSS,
                              java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY);

	void deleteInvIlEntry(java.util.ArrayList itemList, java.util.ArrayList locationList,
                          java.util.ArrayList categoryList, java.lang.String II_CLSS, java.lang.Integer AD_CMPNY)
			throws GlobalRecordAlreadyAssignedException;

	InvModItemLocationDetails getInvIlByIlCode(java.lang.Integer IL_CODE, java.lang.Integer AD_CMPNY)
			throws GlobalNoRecordFoundException;

	short getAdPrfInvQuantityPrecisionUnit(java.lang.Integer AD_CMPNY);

	void saveInvIlReorderPointAndReorderQuantity(java.util.ArrayList locationList, java.util.ArrayList itemList,
                                                 java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY);
}