package com.ejb.txnapi.inv;

import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.ejb.restfulapi.OfsApiResponse;
import com.ejb.restfulapi.inv.models.AdjustmentRequest;
import com.util.inv.InvAdjustmentDetails;
import jakarta.ejb.Local;

import java.util.ArrayList;

@Local
public interface InvAdjustmentEntryApiController {

    Integer saveInventoryAdjustment(InvAdjustmentDetails details, ArrayList lineItems)
            throws GlobalInvItemLocationNotFoundException, GlJREffectiveDateNoPeriodExistException,
            GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException,
            AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException,
            GlobalMiscInfoIsRequiredException, GlobalRecordAlreadyDeletedException,
            GlobalBranchAccountNumberInvalidException;

    OfsApiResponse createAdjustment(AdjustmentRequest itemAdjustmentRequest);

}