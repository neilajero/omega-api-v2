package com.ejb.txnapi.ar;

import java.util.ArrayList;

import com.ejb.exception.ad.AdPRFCoaGlCustomerDepositAccountNotFoundException;
import com.ejb.exception.ar.ArINVOverCreditBalancePaidapplicationNotAllowedException;
import com.ejb.exception.ar.ArINVOverapplicationNotAllowedException;
import com.ejb.exception.ar.ArRCTInvoiceHasNoWTaxCodeException;
import com.ejb.exception.ar.ArREDuplicatePayfileReferenceNumberException;
import com.ejb.exception.ar.ArReceiptEntryValidationException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.GlobalBranchAccountNumberInvalidException;
import com.ejb.exception.global.GlobalConversionDateNotExistException;
import com.ejb.exception.global.GlobalDocumentNumberNotUniqueException;
import com.ejb.exception.global.GlobalJournalNotBalanceException;
import com.ejb.exception.global.GlobalNoApprovalApproverFoundException;
import com.ejb.exception.global.GlobalNoApprovalRequesterFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalTransactionAlreadyApprovedException;
import com.ejb.exception.global.GlobalTransactionAlreadyLockedException;
import com.ejb.exception.global.GlobalTransactionAlreadyPendingException;
import com.ejb.exception.global.GlobalTransactionAlreadyPostedException;
import com.ejb.exception.global.GlobalTransactionAlreadyVoidException;
import com.ejb.restfulapi.OfsApiResponse;
import com.ejb.restfulapi.ar.models.ReceiptApiResponse;
import com.ejb.restfulapi.ar.models.ReceiptRequest;
import com.util.ar.ArReceiptDetails;

import jakarta.ejb.Local;

@Local
public interface ArReceiptEntryApiController {

    Integer saveReceipt(ArReceiptDetails details, String bankAccount,
                        String foreignCurrency, String customerCode, String receiptBatch, ArrayList receiptLines,
                        Integer branchCode, Integer companyCode)
            throws ArReceiptEntryValidationException, GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException,
            GlobalConversionDateNotExistException, GlobalTransactionAlreadyApprovedException,
            GlobalTransactionAlreadyPendingException, ArINVOverCreditBalancePaidapplicationNotAllowedException,
            GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException,
            ArINVOverapplicationNotAllowedException, GlobalTransactionAlreadyLockedException,
            GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException,
            ArRCTInvoiceHasNoWTaxCodeException, GlJREffectiveDateNoPeriodExistException,
            GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException,
            GlobalBranchAccountNumberInvalidException, GlobalRecordAlreadyAssignedException,
            AdPRFCoaGlCustomerDepositAccountNotFoundException, ArREDuplicatePayfileReferenceNumberException;

    OfsApiResponse createReceipt(ReceiptRequest request);
}