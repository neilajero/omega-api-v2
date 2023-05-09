package com.ejb.txnapi.ar;

import com.ejb.exception.ar.ArInvoiceStandardMemolineDoesNotExist;
import com.util.mod.ar.ArModInvoiceLineDetails;
import com.util.mod.ar.ArModInvoiceLineItemDetails;
import jakarta.ejb.Local;

import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.ar.ArINVAmountExceedsCreditLimitException;
import com.ejb.exception.ar.ArInvDuplicateUploadNumberException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.ejb.restfulapi.OfsApiResponse;
import com.ejb.restfulapi.ar.models.InvoiceMemoLineRequest;
import com.ejb.restfulapi.ar.models.InvoiceItemRequest;
import com.util.ar.ArInvoiceDetails;

import java.util.ArrayList;

@Local
public interface ArInvoiceEntryApiController {

    Integer saveInvoiceItems(ArInvoiceDetails invoiceDetails, ArrayList<ArModInvoiceLineItemDetails> invoiceLines)
            throws GlobalInvItemLocationNotFoundException, GlobalInventoryDateException,
            GlobalMiscInfoIsRequiredException, GlobalBranchAccountNumberInvalidException,
            GlobalRecordInvalidException, ArINVAmountExceedsCreditLimitException, GlobalRecordAlreadyDeletedException,
            GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException,
            GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException,
            GlobalJournalNotBalanceException, AdPRFCoaGlVarianceAccountNotFoundException,
            GlobalExpiryDateNotFoundException, GlobalPaymentTermInvalidException, GlobalNoRecordFoundException,
            GlobalInvItemCostingNotFoundException;

    Integer saveInvoiceMemoLines(ArInvoiceDetails invoiceDetails, ArrayList<ArModInvoiceLineDetails> invoiceLines)
            throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException,
            GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException,
            ArINVAmountExceedsCreditLimitException, GlobalTransactionAlreadyApprovedException,
            GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException,
            GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException,
            GlobalNoApprovalApproverFoundException, GlJREffectiveDateNoPeriodExistException,
            GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException,
            GlobalBranchAccountNumberInvalidException, ArInvDuplicateUploadNumberException,
            ArInvoiceStandardMemolineDoesNotExist;

    OfsApiResponse createInvoiceItems(InvoiceItemRequest invoiceItemRequest);

    OfsApiResponse createInvoiceMemoLines(InvoiceMemoLineRequest invoiceMemoLineRequest);

}