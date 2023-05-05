package com.ejb.txnapi.inv;

import java.util.ArrayList;

import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.GlobalBranchAccountNumberInvalidException;
import com.ejb.exception.global.GlobalInvItemLocationNotFoundException;
import com.ejb.exception.global.GlobalInventoryDateException;
import com.ejb.exception.global.GlobalJournalNotBalanceException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordInvalidException;
import com.ejb.exception.global.GlobalTransactionAlreadyPostedException;
import com.ejb.restfulapi.OfsApiResponse;
import com.ejb.restfulapi.inv.models.AdjustmentRequest;
import com.util.inv.InvStockTransferDetails;

import jakarta.ejb.Local;

@Local
public interface InvStockTransferEntryApiController {

	Integer saveStockTransfer(InvStockTransferDetails details, ArrayList lineItems)
			throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException,
			GlobalInvItemLocationNotFoundException, GlJREffectiveDateNoPeriodExistException,
			GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalInventoryDateException,
			AdPRFCoaGlVarianceAccountNotFoundException, GlobalRecordInvalidException,
			GlobalBranchAccountNumberInvalidException;

	OfsApiResponse createStockTransfer(AdjustmentRequest adjustmentRequest);

}