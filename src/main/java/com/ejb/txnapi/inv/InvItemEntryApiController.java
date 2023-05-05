package com.ejb.txnapi.inv;

import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.exception.inv.InvAccumulatedDepreciationAccountNotFoundException;
import com.ejb.exception.inv.InvDepreciationAccountNotFoundException;
import com.ejb.exception.inv.InvFixedAssetAccountNotFoundException;
import com.ejb.exception.inv.InvFixedAssetInformationNotCompleteException;
import com.ejb.restfulapi.OfsApiResponse;
import com.ejb.restfulapi.inv.models.ItemRequest;
import com.util.inv.InvItemDetails;

import jakarta.ejb.Local;

@Local
public interface InvItemEntryApiController {

    java.lang.Integer saveInvIiEntry(InvItemDetails details, java.lang.String UOM_NM,
                                     java.lang.String SPL_SPPLR_CD, java.lang.String CST_CSTMR_CD, java.lang.Integer AD_CMPNY)
			throws GlobalRecordAlreadyExistException,
            GlobalRecordAlreadyAssignedException,
            InvFixedAssetAccountNotFoundException,
            InvDepreciationAccountNotFoundException,
            InvFixedAssetInformationNotCompleteException,
            InvAccumulatedDepreciationAccountNotFoundException;

    OfsApiResponse createItem(ItemRequest itemRequest);
}