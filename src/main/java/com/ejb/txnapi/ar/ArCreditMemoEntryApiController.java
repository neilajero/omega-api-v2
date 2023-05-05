package com.ejb.txnapi.ar;

import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.ar.ArINVOverapplicationNotAllowedException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.ejb.restfulapi.OfsApiResponse;
import com.ejb.restfulapi.ar.models.InvoiceItemRequest;
import com.util.ar.ArInvoiceDetails;

import jakarta.ejb.Local;
import java.util.ArrayList;

@Local
public interface ArCreditMemoEntryApiController {

    Integer saveCreditMemo(ArInvoiceDetails invoiceDetails, String customerCode, ArrayList invoiceLines)
            throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException,
            GlobalTransactionAlreadyVoidPostedException, ArINVOverapplicationNotAllowedException, GlJREffectiveDateNoPeriodExistException,
            GlJREffectiveDatePeriodClosedException, GlobalTransactionAlreadyLockedException, GlobalJournalNotBalanceException,
            GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException;

    OfsApiResponse createCreditMemo(InvoiceItemRequest invoiceItemRequest);
}