package com.ejb.txn.ar;

import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.ar.ArINVAmountExceedsCreditLimitException;
import com.ejb.exception.ar.ArINVInvoiceHasReceipt;
import com.ejb.exception.ar.ArINVNoSalesOrderLinesFoundException;
import com.ejb.exception.ar.ArInvDuplicateUploadNumberException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.util.ar.ArInvoiceDetails;
import com.util.ar.ArStandardMemoLineDetails;
import com.util.mod.ar.ArModCustomerDetails;
import com.util.mod.ar.ArModInvoiceDetails;
import com.util.mod.ar.ArModJobOrderDetails;
import com.util.mod.ar.ArModSalesOrderDetails;

import jakarta.ejb.Local;
import java.util.ArrayList;
import java.util.Date;

@Local
public interface ArInvoiceEntryController {

    Integer saveArInvEntry(ArInvoiceDetails details, String paymentName, String taxCode, String withholdingTaxCode,
                           String currencyName, String customerCode, String invoiceBatchName, ArrayList invoiceLines, boolean isDraft,
                           String salesPersonCode, String projectCode, String projectTypeCode, String projectPhaseName,
                           String contractTermName, Integer branchCode, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException,
            ArINVAmountExceedsCreditLimitException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException,
            GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlJREffectiveDateNoPeriodExistException,
            GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException, ArInvDuplicateUploadNumberException;

    Integer saveArInvIliEntry(ArInvoiceDetails invoiceDetails, String paymentName, String taxCode,
                              String withholdingTaxCode, String currencyName, String customerCode, String invoiceBatchName, ArrayList invoiceLines, boolean isDraft,
                              String salesPersonCode, String deployedBranchName, Integer branchCode, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException,
            ArINVAmountExceedsCreditLimitException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException,
            GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException,
            GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalInventoryDateException,
            GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException, GlobalRecordInvalidException, GlobalExpiryDateNotFoundException,
            GlobalMiscInfoIsRequiredException, ArInvDuplicateUploadNumberException;

    Integer saveArInvSolEntry(ArInvoiceDetails details, String paymentName, String taxCode, String withholdingTaxCode,
                              String currencyName, String customerCode, String invoiceBatchName, ArrayList solList, boolean isDraft,
                              String salesPersonCode, Integer branchCode, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException,
            GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException,
            GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException, GlJREffectiveDateNoPeriodExistException,
            GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalTransactionAlreadyLockedException, GlobalInventoryDateException,
            GlobalBranchAccountNumberInvalidException, ArINVAmountExceedsCreditLimitException, AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException,
            ArInvDuplicateUploadNumberException;

    Integer saveArInvJolEntry(ArInvoiceDetails details, String paymentName, String taxCode, String withholdingTaxCode,
                              String currencyName, String customerCode, String invoiceBatchName, ArrayList jolList, boolean isDraft,
                              String salesPersonCode, Integer branchCode, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException,
            GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException,
            GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException, GlJREffectiveDateNoPeriodExistException,
            GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalTransactionAlreadyLockedException, GlobalInventoryDateException,
            GlobalBranchAccountNumberInvalidException, ArINVAmountExceedsCreditLimitException, AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException,
            ArInvDuplicateUploadNumberException;

    short getInvGpQuantityPrecisionUnit(Integer companyCode);

    short getInvGpCostPrecisionUnit(Integer companyCode);

    short getAdPrfArInvoiceLineNumber(Integer companyCode);

    byte getAdPrfEnableArInvoiceBatch(Integer companyCode);

    byte getAdPrfInvoiceSalespersonRequired(Integer companyCode);

    byte getAdPrfEnableInvShift(Integer companyCode);

    byte getAdPrfArUseCustomerPulldown(Integer companyCode);

    byte getAdPrfArDisableSalesPrice(Integer companyCode);

    byte getAdPrfArEnablePaymentTerm(Integer companyCode);

    double getFrRateByFrNameAndFrDate(String currencyName, Date conversionDate, Integer companyCode) throws GlobalConversionDateNotExistException;

    double getIiSalesPriceByInvCstCustomerCodeAndIiNameAndUomName(String customerCode, String itemName, String unitOfMeasure, Integer companyCode);

    boolean getArTraceMisc(String itemName, Integer companyCode);

    boolean getInvIiNonInventoriableByIiName(String itemName, Integer companyCode);

    void deleteArInvEntry(Integer invoiceCode, String userCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, ArINVInvoiceHasReceipt;

    void recalculateCostOfSalesEntries();

    void saveReceivedDate(ArInvoiceDetails details, Integer branchCode, Integer companyCode);

    void saveDisableInterest(ArInvoiceDetails details, Integer branchCode, Integer companyCode);

    ArrayList getAdApprovalNotifiedUsersByInvCode(Integer invoiceCode, Integer companyCode);

    ArrayList getArNotIssuedSolBySoNumberAndCustomer(String soDocumentNumber, String customerCode,
                                                     ArrayList issuedSolList, Integer companyCode) throws GlobalNoRecordFoundException;

    ArrayList getArNotIssuedJolByJoNumberAndCustomer(String joDocumentNumber, String customerCode,
                                                     ArrayList issuedJolList, Integer companyCode) throws GlobalNoRecordFoundException;

    ArrayList getInvLitByCstLitName(String lineItemTemplateName, Integer companyCode) throws GlobalNoRecordFoundException;

    ArrayList getArCstAllForGeneration(Integer branchCode, java.lang.Integer companyCode);

    ArrayList getAdLvCustomerBatchAll(Integer companyCode);

    ArrayList getArCstAllByCustomerBatch(ArrayList customerBatchList, java.lang.Integer branchCode, java.lang.Integer companyCode);

    ArrayList getInvUomByIiName(String itemName, java.lang.Integer companyCode);

    String getInvIiClassByIiName(String itemName, Integer companyCode);

    ArModInvoiceDetails getArInvByInvCode(Integer invoiceCode, Integer companyCode) throws GlobalNoRecordFoundException;

    ArModCustomerDetails getArCstByCstCustomerCode(String customerCode, Integer companyCode) throws GlobalNoRecordFoundException;

    ArStandardMemoLineDetails getArSmlByCstCstmrCodeSmlNm(String customerCode, String standardMemoLineName, int branchCode, Integer companyCode) throws GlobalNoRecordFoundException;

    ArStandardMemoLineDetails getArSmlBySmlName(String standardMemoLineName, Integer companyCode) throws GlobalNoRecordFoundException;

    ArModSalesOrderDetails getArSoBySoDocumentNumberAndCstCustomerCodeAndAdBranch(String soDocumentNumber, String customerCode, Integer branchCode, Integer companyCode)
            throws GlobalNoRecordFoundException, ArINVNoSalesOrderLinesFoundException;

    ArModJobOrderDetails getArJoByJoDocumentNumberAndCstCustomerCodeAndAdBranch(String joDocumentNumber, String customerCode, Integer branchCode, Integer companyCode)
            throws GlobalNoRecordFoundException, ArINVNoSalesOrderLinesFoundException;

}