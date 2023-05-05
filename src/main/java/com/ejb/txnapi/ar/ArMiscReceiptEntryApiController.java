package com.ejb.txnapi.ar;

import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.ejb.restfulapi.ar.models.ReceiptApiResponse;
import com.ejb.restfulapi.ar.models.ReceiptRequest;
import com.util.ar.ArReceiptDetails;

import jakarta.ejb.Local;
import java.util.ArrayList;

@Local
public interface ArMiscReceiptEntryApiController {
    java.lang.Integer saveArRctIliEntry(ArReceiptDetails details, String bankAccountName, String bankAccountCard1Name, String bankAccountCard2Name,
                                        String bankAccountCard3Name, String taxCode, String wTaxCode, String foreignCurrency,
                                        String customerCode, ArrayList receiptLines,
                                        Integer branchCode, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException, GlobalRecordAlreadyAssignedException, AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException, GlobalMiscInfoIsRequiredException, GlobalRecordInvalidException;

    ReceiptApiResponse createMiscReceipt(ReceiptRequest request);

    ReceiptApiResponse createMiscReceiptMemoLines(ReceiptRequest request);
}