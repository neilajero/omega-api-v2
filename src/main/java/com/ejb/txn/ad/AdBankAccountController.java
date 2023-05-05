package com.ejb.txn.ad;

import jakarta.ejb.Local;
import java.util.ArrayList;

import com.ejb.exception.ad.*;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.ad.AdResponsibilityDetails;
import com.util.mod.ad.AdModBankAccountDetails;

@Local
public interface AdBankAccountController {

    ArrayList getAdBaAll(Integer companyCode) throws GlobalNoRecordFoundException;

    void addAdBaEntry(AdModBankAccountDetails mdetails, String coaCashAccount, String coaOnAccountReceiptAccount, String coaUnappliedReceiptAccount, String coaBankChargeAccount, String coaClearingAccount, String coaInterestAccount, String coaAdjustmentAccount, String coaCashDiscountAccount, String coaSalesDiscountAccount, String coaUnappliedCheckAccount, String coaAdvanceAccount, String bankName, String currencyName, ArrayList branchList, Integer companyCode) throws GlobalRecordAlreadyExistException, AdBACoaGlCashAccountNotFoundException, AdBACoaGlAccountReceiptNotFoundException, AdBACoaGlUnappliedReceiptNotFoundException, AdBACoaGlUnappliedCheckNotFoundException, AdBACoaGlBankChargeAccountNotFoundException, AdBACoaGlClearingAccountNotFoundException, AdBACoaGlCashDiscountNotFoundException, AdBACoaGlSalesDiscountNotFoundException, AdBACoaGlInterestAccountNotFoundException, AdBACoaGlAdjustmentAccountNotFoundException, AdBACoaGlAdvanceAccountNotFoundException;

    void updateAdBaEntry(AdModBankAccountDetails details, String coaCashAccount, String coaOnAccountReceiptAccount, String coaUnappliedReceiptAccount, String coaBankChargeAccount, String coaClearingAccount, String coaInterestAccount, String coaAdjustmentAccount, String coaCashDiscountAccount, String coaSalesDiscountAccount, String coaUnappliedCheckAccount, String coaAdvanceAccount, String bankName, String currencyName, String responsibilityName, ArrayList branchList, Integer companyCode) throws GlobalRecordAlreadyExistException, AdBACoaGlCashAccountNotFoundException, AdBACoaGlAccountReceiptNotFoundException, AdBACoaGlUnappliedReceiptNotFoundException, AdBACoaGlUnappliedCheckNotFoundException, AdBACoaGlBankChargeAccountNotFoundException, AdBACoaGlClearingAccountNotFoundException, AdBACoaGlCashDiscountNotFoundException, AdBACoaGlSalesDiscountNotFoundException, AdBACoaGlInterestAccountNotFoundException, AdBACoaGlAdjustmentAccountNotFoundException, AdBACoaGlAdvanceAccountNotFoundException;

    short getGlFcPrecisionUnit(Integer companyCode);

    void deleteAdBaEntry(Integer bankAccountCode, Integer companyCode) throws GlobalRecordAlreadyAssignedException, GlobalRecordAlreadyDeletedException;

    ArrayList getAdBnkAll(Integer companyCode);

    ArrayList getGlFcAllWithDefault(Integer companyCode);

    ArrayList getAdBrResAll(Integer responsibilityCode, Integer companyCode) throws GlobalNoRecordFoundException;

    ArrayList getAdBrBaAll(Integer bankAccountCode, Integer companyCode) throws GlobalNoRecordFoundException;

    ArrayList getAdBrDsaAll(Integer documentSequenceAssignmentCode, Integer companyCode) throws GlobalNoRecordFoundException;

    AdResponsibilityDetails getAdRsByRsCode(Integer responsibilityCode) throws GlobalNoRecordFoundException;

}