package com.ejb.txnapi.inv;

import java.util.*;
import java.lang.*;

import jakarta.ejb.Local;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.inv.*;
import com.util.mod.inv.InvModItemLocationDetails;

@Local
public interface InvItemLocationEntryApiController {

	ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException;
	
	void saveInvIlEntry(InvModItemLocationDetails mdetails, ArrayList itemList,
                        ArrayList locationList, ArrayList branchItemLocationList,
                        ArrayList categoryList, String II_CLSS, Integer AD_CMPNY)
			throws InvILCoaGlSalesAccountNotFoundException,
            InvILCoaGlSalesReturnAccountNotFoundException,
            InvILCoaGlInventoryAccountNotFoundException,
            InvILCoaGlCostOfSalesAccountNotFoundException,
			InvILCoaGlWipAccountNotFoundException,
            InvILCoaGlAccruedInventoryAccountNotFoundException;
}