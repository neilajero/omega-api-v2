package com.ejb.txn.ad;

import java.util.*;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.cm.LocalCmFundTransferHome;
import com.ejb.dao.ad.*;
import com.ejb.dao.gl.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.gl.*;
import com.ejb.exception.ad.*;
import com.ejb.exception.global.*;
import com.util.*;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdResponsibilityDetails;
import com.util.mod.ad.AdModBankAccountDetails;
import com.util.mod.ad.AdModBranchBankAccountDetails;
import com.util.mod.ad.AdModBranchDocumentSequenceAssignmentDetails;
import com.util.mod.gl.GlModFunctionalCurrencyDetails;

@Stateless(name = "AdBankAccountControllerEJB")
public class AdBankAccountControllerBean extends EJBContextClass implements AdBankAccountController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalAdBankAccountBalanceHome adBankAccountBalanceHome;
    @EJB
    private LocalCmFundTransferHome cmFundTransferHome;
    @EJB
    private LocalAdBankHome adBankHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalAdBranchBankAccountHome adBranchBankAccountHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDsaHome;
    @EJB
    private LocalAdResponsibilityHome adResHome;

    public ArrayList getAdBaAll(Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdBankAccountControllerBean getAdBnkAll");

        Collection adBankAccounts = null;
        LocalAdBankAccount adBankAccount;
        ArrayList list = new ArrayList();
        try {

            try {

                adBankAccounts = adBankAccountHome.findBaAll(companyCode);

            } catch (FinderException ex) {

            }

            if (adBankAccounts.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            LocalGlChartOfAccount glCashChartOfAccount = null;
            LocalGlChartOfAccount glOnAccountReceiptChartOfAccount = null;
            LocalGlChartOfAccount glUnappliedReceiptChartOfAccount = null;
            LocalGlChartOfAccount glUnappliedCheckChartOfAccount = null;
            LocalGlChartOfAccount glBankChargeChartOfAccount = null;
            LocalGlChartOfAccount glClearingChartOfAccount = null;
            LocalGlChartOfAccount glInterestChartOfAccount = null;
            LocalGlChartOfAccount glAdjustmentChartOfAccount = null;
            LocalGlChartOfAccount glCashDiscountChartOfAccount = null;
            LocalGlChartOfAccount glSalesDiscountChartOfAccount = null;
            LocalGlChartOfAccount glAdvanceChartOfAccount = null;

            for (Object bankAccount : adBankAccounts) {

                adBankAccount = (LocalAdBankAccount) bankAccount;

                AdModBankAccountDetails mdetails = new AdModBankAccountDetails();
                mdetails.setBaCode(adBankAccount.getBaCode());
                mdetails.setBaName(adBankAccount.getBaName());
                mdetails.setBaDescription(adBankAccount.getBaDescription());
                mdetails.setBaAccountType(adBankAccount.getBaAccountType());
                mdetails.setBaAccountNumber(adBankAccount.getBaAccountNumber());
                mdetails.setBaAccountUse(adBankAccount.getBaAccountUse());

                // Get Cash Account Number with Description

                if (adBankAccount.getBaCoaGlCashAccount() != null) {

                    try {

                        glCashChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBankAccount.getBaCoaGlCashAccount());

                        mdetails.setBaCoaCashAccount(glCashChartOfAccount.getCoaAccountNumber());
                        mdetails.setBaCashAccountDescription(glCashChartOfAccount.getCoaAccountDescription());

                    } catch (FinderException ex) {

                    }
                }

                // Get On-Account Receipt Account Number with Description

                if (adBankAccount.getBaCoaGlOnAccountReceipt() != null) {

                    try {

                        glOnAccountReceiptChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBankAccount.getBaCoaGlOnAccountReceipt());

                        mdetails.setBaCoaOnAccountReceipt(glOnAccountReceiptChartOfAccount.getCoaAccountNumber());
                        mdetails.setBaOnAccountReceiptDescription(glOnAccountReceiptChartOfAccount.getCoaAccountDescription());

                    } catch (FinderException ex) {

                    }
                }

                if (adBankAccount.getBaCoaGlUnappliedReceipt() != null) {

                    // Get Unapplied Receipt Account Number with Description

                    try {

                        glUnappliedReceiptChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBankAccount.getBaCoaGlUnappliedReceipt());

                        mdetails.setBaCoaUnappliedReceipt(glUnappliedReceiptChartOfAccount.getCoaAccountNumber());
                        mdetails.setBaUnappliedReceiptDescription(glUnappliedReceiptChartOfAccount.getCoaAccountDescription());

                    } catch (FinderException ex) {

                    }
                }

                // Get Bank Charge Account Number with Description

                if (adBankAccount.getBaCoaGlBankChargeAccount() != null) {

                    try {

                        glBankChargeChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBankAccount.getBaCoaGlBankChargeAccount());

                        mdetails.setBaCoaBankChargeAccount(glBankChargeChartOfAccount.getCoaAccountNumber());
                        mdetails.setBaBankChargeAccountDescription(glBankChargeChartOfAccount.getCoaAccountDescription());

                    } catch (FinderException ex) {

                    }
                }

                // Get Clearing Account Number with Description

                if (adBankAccount.getBaCoaGlClearingAccount() != null) {

                    try {

                        glClearingChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBankAccount.getBaCoaGlClearingAccount());

                        mdetails.setBaCoaClearingAccount(glClearingChartOfAccount.getCoaAccountNumber());
                        mdetails.setBaClearingAccountDescription(glClearingChartOfAccount.getCoaAccountDescription());

                    } catch (FinderException ex) {

                    }
                }

                // Get Interest Account Number with Description

                if (adBankAccount.getBaCoaGlInterestAccount() != null) {

                    try {

                        glInterestChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBankAccount.getBaCoaGlInterestAccount());

                        mdetails.setBaCoaInterestAccount(glInterestChartOfAccount.getCoaAccountNumber());
                        mdetails.setBaInterestAccountDescription(glInterestChartOfAccount.getCoaAccountDescription());

                    } catch (FinderException ex) {

                    }
                }

                // Get Adjustment Account Number with Description

                if (adBankAccount.getBaCoaGlAdjustmentAccount() != null) {

                    try {

                        glAdjustmentChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBankAccount.getBaCoaGlAdjustmentAccount());

                        mdetails.setBaCoaAdjustmentAccount(glAdjustmentChartOfAccount.getCoaAccountNumber());
                        mdetails.setBaAdjustmentAccountDescription(glAdjustmentChartOfAccount.getCoaAccountDescription());

                    } catch (FinderException ex) {

                    }
                }

                // Get Cash Discount Account Number with Description

                if (adBankAccount.getBaCoaGlCashDiscount() != null) {

                    try {

                        glCashDiscountChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBankAccount.getBaCoaGlCashDiscount());

                        mdetails.setBaCoaCashDiscount(glCashDiscountChartOfAccount.getCoaAccountNumber());
                        mdetails.setBaCashDiscountDescription(glCashDiscountChartOfAccount.getCoaAccountDescription());

                    } catch (FinderException ex) {

                    }
                }

                // Get Sales Discount Account Number with Description

                if (adBankAccount.getBaCoaGlSalesDiscount() != null) {

                    try {

                        glSalesDiscountChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBankAccount.getBaCoaGlSalesDiscount());

                        mdetails.setBaCoaSalesDiscount(glSalesDiscountChartOfAccount.getCoaAccountNumber());
                        mdetails.setBaSalesDiscountDescription(glSalesDiscountChartOfAccount.getCoaAccountDescription());

                    } catch (FinderException ex) {

                    }
                }

                // Get Advance Account Number with Description

                if (adBankAccount.getBaCoaGlAdvanceAccount() != null) {

                    try {

                        glAdvanceChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBankAccount.getBaCoaGlAdvanceAccount());

                        mdetails.setBaCoaAdvanceAccount(glAdvanceChartOfAccount.getCoaAccountNumber());
                        mdetails.setBaAdvanceAccountDescription(glAdvanceChartOfAccount.getCoaAccountDescription());

                    } catch (FinderException ex) {

                    }
                }

                // Get Unapplied Check Account Number with Description

                if (adBankAccount.getBaCoaGlUnappliedCheck() != null) {

                    try {

                        glUnappliedCheckChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBankAccount.getBaCoaGlUnappliedCheck());

                        mdetails.setBaCoaUnappliedCheck(glUnappliedCheckChartOfAccount.getCoaAccountNumber());
                        mdetails.setBaUnappliedCheckDescription(glUnappliedCheckChartOfAccount.getCoaAccountDescription());

                    } catch (FinderException ex) {

                    }
                }

                // get latest bank account balance for current bank account

                double bankAccountBalance = 0;

                Collection adBankAccountBalances = adBankAccountBalanceHome.findByBaCodeAndType(adBankAccount.getBaCode(), "BOOK", companyCode);

                if (!adBankAccountBalances.isEmpty()) {

                    // get last check

                    ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                    LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                    bankAccountBalance = adBankAccountBalance.getBabBalance();
                }

                mdetails.setBaAvailableBalance(bankAccountBalance);
                mdetails.setBaFloatBalance(adBankAccount.getBaFloatBalance());
                mdetails.setBaNextCheckNumber(adBankAccount.getBaNextCheckNumber());
                mdetails.setBaEnable(adBankAccount.getBaEnable());
                mdetails.setBaAccountNumberShow(adBankAccount.getBaAccountNumberShow());
                mdetails.setBaAccountNumberTop(adBankAccount.getBaAccountNumberTop());
                mdetails.setBaAccountNumberLeft(adBankAccount.getBaAccountNumberLeft());
                mdetails.setBaAccountNameShow(adBankAccount.getBaAccountNameShow());
                mdetails.setBaAccountNameTop(adBankAccount.getBaAccountNameTop());
                mdetails.setBaAccountNameLeft(adBankAccount.getBaAccountNameLeft());
                mdetails.setBaNumberShow(adBankAccount.getBaNumberShow());
                mdetails.setBaNumberTop(adBankAccount.getBaNumberTop());
                mdetails.setBaNumberLeft(adBankAccount.getBaNumberLeft());
                mdetails.setBaDateShow(adBankAccount.getBaDateShow());
                mdetails.setBaDateTop(adBankAccount.getBaDateTop());
                mdetails.setBaDateLeft(adBankAccount.getBaDateLeft());
                mdetails.setBaPayeeShow(adBankAccount.getBaPayeeShow());
                mdetails.setBaPayeeTop(adBankAccount.getBaPayeeTop());
                mdetails.setBaPayeeLeft(adBankAccount.getBaPayeeLeft());
                mdetails.setBaAmountShow(adBankAccount.getBaAmountShow());
                mdetails.setBaAmountTop(adBankAccount.getBaAmountTop());
                mdetails.setBaAmountLeft(adBankAccount.getBaAmountLeft());
                mdetails.setBaWordAmountShow(adBankAccount.getBaWordAmountShow());
                mdetails.setBaWordAmountTop(adBankAccount.getBaWordAmountTop());
                mdetails.setBaWordAmountLeft(adBankAccount.getBaWordAmountLeft());
                mdetails.setBaCurrencyShow(adBankAccount.getBaCurrencyShow());
                mdetails.setBaCurrencyTop(adBankAccount.getBaCurrencyTop());
                mdetails.setBaCurrencyLeft(adBankAccount.getBaCurrencyLeft());
                mdetails.setBaAddressShow(adBankAccount.getBaAddressShow());
                mdetails.setBaAddressTop(adBankAccount.getBaAddressTop());
                mdetails.setBaAddressLeft(adBankAccount.getBaAddressLeft());
                mdetails.setBaMemoShow(adBankAccount.getBaMemoShow());
                mdetails.setBaMemoTop(adBankAccount.getBaMemoTop());
                mdetails.setBaMemoLeft(adBankAccount.getBaMemoLeft());
                mdetails.setBadocNumberShow(adBankAccount.getBadocNumberShow());
                mdetails.setBadocNumberTop(adBankAccount.getBadocNumberTop());
                mdetails.setBadocNumberLeft(adBankAccount.getBadocNumberLeft());
                mdetails.setBaFontSize(adBankAccount.getBaFontSize());
                mdetails.setBaFontStyle(adBankAccount.getBaFontStyle());
                mdetails.setBaBankName(adBankAccount.getAdBank().getBnkName());
                mdetails.setBaFcName(adBankAccount.getGlFunctionalCurrency().getFcName());
                mdetails.setBaIsCashAccount(adBankAccount.getBaIsCashAccount());

                list.add(mdetails);
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public void addAdBaEntry(AdModBankAccountDetails mdetails, String coaCashAccount, String coaOnAccountReceiptAccount, String coaUnappliedReceiptAccount, String coaBankChargeAccount, String coaClearingAccount, String coaInterestAccount, String coaAdjustmentAccount, String coaCashDiscountAccount, String coaSalesDiscountAccount, String coaUnappliedCheckAccount, String coaAdvanceAccount, String bankName, String currencyName, ArrayList branchList, Integer companyCode) throws GlobalRecordAlreadyExistException, AdBACoaGlCashAccountNotFoundException, AdBACoaGlAccountReceiptNotFoundException, AdBACoaGlUnappliedReceiptNotFoundException, AdBACoaGlUnappliedCheckNotFoundException, AdBACoaGlBankChargeAccountNotFoundException, AdBACoaGlClearingAccountNotFoundException, AdBACoaGlCashDiscountNotFoundException, AdBACoaGlSalesDiscountNotFoundException, AdBACoaGlInterestAccountNotFoundException, AdBACoaGlAdjustmentAccountNotFoundException, AdBACoaGlAdvanceAccountNotFoundException {

        Debug.print("AdBankAccountControllerBean addAdBaEntry");

        LocalAdBankAccount adBankAccount;
        LocalAdBranchBankAccount adBranchBankAccount;
        LocalAdBranch adBranch;

        // Validate Chart of Accounts
        LocalGlChartOfAccount glCashChartOfAccount = null;
        LocalGlChartOfAccount glBankChargeChartOfAccount = null;
        LocalGlChartOfAccount glInterestChartOfAccount = null;
        LocalGlChartOfAccount glAdjustmentChartOfAccount = null;
        LocalGlChartOfAccount glSalesDiscountChartOfAccount = null;
        LocalGlChartOfAccount glAdvanceAccountChartOfAccount = null;
        LocalGlChartOfAccount glCashDiscountChartOfAccount = null;
        LocalGlChartOfAccount glOnAccountReceiptChartOfAccount = null;
        LocalGlChartOfAccount glUnappliedReceiptChartOfAccount = null;
        LocalGlChartOfAccount glUnappliedCheckChartOfAccount = null;
        LocalGlChartOfAccount glClearingChartOfAccount = null;

        if (coaCashAccount != null && coaCashAccount.length() > 0) {
            try {
                glCashChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(coaCashAccount, companyCode);
            } catch (FinderException ex) {
                throw new AdBACoaGlCashAccountNotFoundException();
            } catch (Exception ex) {
                throw new EJBException(ex.getMessage());
            }
        }

        if (coaBankChargeAccount != null && coaBankChargeAccount.length() > 0) {
            try {
                glBankChargeChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(coaBankChargeAccount, companyCode);
            } catch (FinderException ex) {
                throw new AdBACoaGlBankChargeAccountNotFoundException();
            } catch (Exception ex) {
                throw new EJBException(ex.getMessage());
            }
        }

        if (coaInterestAccount != null && coaInterestAccount.length() > 0) {
            try {
                glInterestChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(coaInterestAccount, companyCode);
            } catch (FinderException ex) {
                throw new AdBACoaGlInterestAccountNotFoundException();
            } catch (Exception ex) {
                throw new EJBException(ex.getMessage());
            }
        }

        if (coaAdjustmentAccount != null && coaAdjustmentAccount.length() > 0) {
            try {
                glAdjustmentChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(coaAdjustmentAccount, companyCode);
            } catch (FinderException ex) {
                throw new AdBACoaGlAdjustmentAccountNotFoundException();
            } catch (Exception ex) {
                throw new EJBException(ex.getMessage());
            }
        }

        if (coaSalesDiscountAccount != null && coaSalesDiscountAccount.length() > 0) {
            try {
                glSalesDiscountChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(coaSalesDiscountAccount, companyCode);
            } catch (FinderException ex) {
                throw new AdBACoaGlSalesDiscountNotFoundException();
            } catch (Exception ex) {
                throw new EJBException(ex.getMessage());
            }
        }

        if (coaAdvanceAccount != null && coaAdvanceAccount.length() > 0) {
            try {
                glAdvanceAccountChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(coaAdvanceAccount, companyCode);
            } catch (FinderException ex) {
                throw new AdBACoaGlAdvanceAccountNotFoundException();
            } catch (Exception ex) {
                throw new EJBException(ex.getMessage());
            }
        }

        try {
            // create new bank account
            adBankAccount = adBankAccountHome.create(mdetails.getBaName(), mdetails.getBaDescription(), mdetails.getBaAccountType(), mdetails.getBaAccountNumber(), mdetails.getBaAccountUse(), glCashChartOfAccount != null ? glCashChartOfAccount.getCoaCode() : null, glOnAccountReceiptChartOfAccount != null ? glOnAccountReceiptChartOfAccount.getCoaCode() : null, glUnappliedReceiptChartOfAccount != null ? glUnappliedReceiptChartOfAccount.getCoaCode() : null, glBankChargeChartOfAccount != null ? glBankChargeChartOfAccount.getCoaCode() : null, glClearingChartOfAccount != null ? glClearingChartOfAccount.getCoaCode() : null, glInterestChartOfAccount != null ? glInterestChartOfAccount.getCoaCode() : null, glAdjustmentChartOfAccount != null ? glAdjustmentChartOfAccount.getCoaCode() : null, glCashDiscountChartOfAccount != null ? glCashDiscountChartOfAccount.getCoaCode() : null, glSalesDiscountChartOfAccount != null ? glSalesDiscountChartOfAccount.getCoaCode() : null, glAdvanceAccountChartOfAccount != null ? glAdvanceAccountChartOfAccount.getCoaCode() : null, glUnappliedCheckChartOfAccount != null ? glUnappliedCheckChartOfAccount.getCoaCode() : null, mdetails.getBaFloatBalance(), null, 0d, mdetails.getBaNextCheckNumber(), mdetails.getBaEnable(), mdetails.getBaAccountNumberShow(), mdetails.getBaAccountNumberTop(), mdetails.getBaAccountNumberLeft(), mdetails.getBaAccountNameShow(), mdetails.getBaAccountNameTop(), mdetails.getBaAccountNameLeft(), mdetails.getBaNumberShow(), mdetails.getBaNumberTop(), mdetails.getBaNumberLeft(), mdetails.getBaDateShow(), mdetails.getBaDateTop(), mdetails.getBaDateLeft(), mdetails.getBaPayeeShow(), mdetails.getBaPayeeTop(), mdetails.getBaPayeeLeft(), mdetails.getBaAmountShow(), mdetails.getBaAmountTop(), mdetails.getBaAmountLeft(), mdetails.getBaWordAmountShow(), mdetails.getBaWordAmountTop(), mdetails.getBaWordAmountLeft(), mdetails.getBaCurrencyShow(), mdetails.getBaCurrencyTop(), mdetails.getBaCurrencyLeft(), mdetails.getBaAddressShow(), mdetails.getBaAddressTop(), mdetails.getBaAddressLeft(), mdetails.getBaMemoShow(), mdetails.getBaMemoTop(), mdetails.getBaMemoLeft(), mdetails.getBadocNumberShow(), mdetails.getBadocNumberTop(), mdetails.getBadocNumberLeft(), mdetails.getBaFontSize(), mdetails.getBaFontStyle(), mdetails.getBaIsCashAccount(), companyCode);

            LocalAdBank adBank = adBankHome.findByBnkName(bankName, companyCode);
            adBank.addAdBankAccount(adBankAccount);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(currencyName, companyCode);
            glFunctionalCurrency.addAdBankAccount(adBankAccount);

        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

        // add branch dsa
        try {
            Iterator i = branchList.iterator();
            while (i.hasNext()) {
                AdModBranchBankAccountDetails brBaDetails = (AdModBranchBankAccountDetails) i.next();
                Integer glCashChrtOfAccntCode;
                Integer glBankChargeChrtOfAccntCode;
                Integer glInterestChrtOfAccntCode;
                Integer glAdjustmnetChrtOfAccntCode;
                Integer glSalesDiscountChrtOfAccntCode;
                Integer glAdvanceChrtOfAccntCode;

                if (brBaDetails.getBbaCashAccountNumber() != null && brBaDetails.getBbaCashAccountNumber().length() > 0) {
                    glCashChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(brBaDetails.getBbaCashAccountNumber(), companyCode);
                }
                if (brBaDetails.getBbaBankChargeAccountNumber() != null && brBaDetails.getBbaBankChargeAccountNumber().length() > 0) {
                    glBankChargeChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(brBaDetails.getBbaBankChargeAccountNumber(), companyCode);
                }
                if (brBaDetails.getBbaInterestAccountNumber() != null && brBaDetails.getBbaInterestAccountNumber().length() > 0) {
                    glInterestChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(brBaDetails.getBbaInterestAccountNumber(), companyCode);
                }
                if (brBaDetails.getBbaAdjustmentAccountNumber() != null && brBaDetails.getBbaAdjustmentAccountNumber().length() > 0) {
                    glAdjustmentChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(brBaDetails.getBbaAdjustmentAccountNumber(), companyCode);
                }
                if (brBaDetails.getBbaSalesDiscountAccountNumber() != null && brBaDetails.getBbaSalesDiscountAccountNumber().length() > 0) {
                    glSalesDiscountChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(brBaDetails.getBbaSalesDiscountAccountNumber(), companyCode);
                }
                if (brBaDetails.getBbaAdvanceAccountNumber() != null && brBaDetails.getBbaAdvanceAccountNumber().length() > 0) {
                    glAdvanceAccountChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(brBaDetails.getBbaAdvanceAccountNumber(), companyCode);
                }

                glCashChrtOfAccntCode = glCashChartOfAccount.getCoaCode();
                glBankChargeChrtOfAccntCode = glBankChargeChartOfAccount.getCoaCode();
                glInterestChrtOfAccntCode = glInterestChartOfAccount.getCoaCode();
                glAdjustmnetChrtOfAccntCode = glAdjustmentChartOfAccount.getCoaCode();
                glSalesDiscountChrtOfAccntCode = glSalesDiscountChartOfAccount.getCoaCode();
                glAdvanceChrtOfAccntCode = glAdvanceAccountChartOfAccount.getCoaCode();

                adBranchBankAccount = adBranchBankAccountHome.create(glCashChrtOfAccntCode, glBankChargeChrtOfAccntCode, glInterestChrtOfAccntCode, glAdjustmnetChrtOfAccntCode, glSalesDiscountChrtOfAccntCode, glAdvanceChrtOfAccntCode, 'N', companyCode);

                adBankAccount.addAdBranchBankAccount(adBranchBankAccount);
                adBranch = adBranchHome.findByPrimaryKey(brBaDetails.getBbaBranchCode());
                adBranch.addAdBranchBankAccount(adBranchBankAccount);
            }
        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateAdBaEntry(AdModBankAccountDetails details, String coaCashAccount, String coaOnAccountReceiptAccount, String coaUnappliedReceiptAccount, String coaBankChargeAccount, String coaClearingAccount, String coaInterestAccount, String coaAdjustmentAccount, String coaCashDiscountAccount, String coaSalesDiscountAccount, String coaUnappliedCheckAccount, String coaAdvanceAccount, String bankName, String currencyName, String responsibilityName, ArrayList branchList, Integer companyCode) throws GlobalRecordAlreadyExistException, AdBACoaGlCashAccountNotFoundException, AdBACoaGlAccountReceiptNotFoundException, AdBACoaGlUnappliedReceiptNotFoundException, AdBACoaGlUnappliedCheckNotFoundException, AdBACoaGlBankChargeAccountNotFoundException, AdBACoaGlClearingAccountNotFoundException, AdBACoaGlCashDiscountNotFoundException, AdBACoaGlSalesDiscountNotFoundException, AdBACoaGlInterestAccountNotFoundException, AdBACoaGlAdjustmentAccountNotFoundException, AdBACoaGlAdvanceAccountNotFoundException {

        Debug.print("AdBankAccountControllerBean updateAdBaEntry");

        LocalAdBankAccount adBankAccount;
        LocalAdBranch adBranch;

        try {
            LocalAdBankAccount adExistingBankAccount = adBankAccountHome.findByBaName(details.getBaName(), companyCode);
            if (!adExistingBankAccount.getBaCode().equals(details.getBaCode())) {
                throw new GlobalRecordAlreadyExistException();
            }
        } catch (GlobalRecordAlreadyExistException ex) {
            throw ex;
        } catch (FinderException ex) {
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        // Validate Chart of Accounts
        LocalGlChartOfAccount glCashChartOfAccount = null;
        LocalGlChartOfAccount glBankChargeChartOfAccount = null;
        LocalGlChartOfAccount glInterestChartOfAccount = null;
        LocalGlChartOfAccount glAdjustmentChartOfAccount = null;
        LocalGlChartOfAccount glSalesDiscountChartOfAccount = null;
        LocalGlChartOfAccount glAdvanceAccountChartOfAccount = null;
        LocalGlChartOfAccount glCashDiscountChartOfAccount = null;
        LocalGlChartOfAccount glOnAccountReceiptChartOfAccount = null;
        LocalGlChartOfAccount glUnappliedReceiptChartOfAccount = null;
        LocalGlChartOfAccount glUnappliedCheckChartOfAccount = null;
        LocalGlChartOfAccount glClearingChartOfAccount = null;

        if (coaCashAccount != null && coaCashAccount.length() > 0) {
            try {
                glCashChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(coaCashAccount, companyCode);
            } catch (FinderException ex) {
                throw new AdBACoaGlCashAccountNotFoundException();
            } catch (Exception ex) {
                throw new EJBException(ex.getMessage());
            }
        }

        if (coaBankChargeAccount != null && coaBankChargeAccount.length() > 0) {
            try {
                glBankChargeChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(coaBankChargeAccount, companyCode);
            } catch (FinderException ex) {
                throw new AdBACoaGlBankChargeAccountNotFoundException();
            } catch (Exception ex) {
                throw new EJBException(ex.getMessage());
            }
        }

        if (coaInterestAccount != null && coaInterestAccount.length() > 0) {
            try {
                glInterestChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(coaInterestAccount, companyCode);
            } catch (FinderException ex) {
                throw new AdBACoaGlInterestAccountNotFoundException();
            } catch (Exception ex) {
                throw new EJBException(ex.getMessage());
            }
        }

        if (coaAdjustmentAccount != null && coaAdjustmentAccount.length() > 0) {
            try {
                glAdjustmentChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(coaAdjustmentAccount, companyCode);
            } catch (FinderException ex) {
                throw new AdBACoaGlAdjustmentAccountNotFoundException();
            } catch (Exception ex) {
                throw new EJBException(ex.getMessage());
            }
        }

        if (coaSalesDiscountAccount != null && coaSalesDiscountAccount.length() > 0) {
            try {
                glSalesDiscountChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(coaSalesDiscountAccount, companyCode);
            } catch (FinderException ex) {
                throw new AdBACoaGlSalesDiscountNotFoundException();
            } catch (Exception ex) {
                throw new EJBException(ex.getMessage());
            }
        }

        if (coaAdvanceAccount != null && coaAdvanceAccount.length() > 0) {
            try {
                glAdvanceAccountChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(coaAdvanceAccount, companyCode);
            } catch (FinderException ex) {
                throw new AdBACoaGlAdvanceAccountNotFoundException();
            } catch (Exception ex) {
                throw new EJBException(ex.getMessage());
            }
        }

        try {

            // find and update bank account
            adBankAccount = adBankAccountHome.findByPrimaryKey(details.getBaCode());
            adBankAccount.setBaName(details.getBaName());
            adBankAccount.setBaDescription(details.getBaDescription());
            adBankAccount.setBaAccountType(details.getBaAccountType());
            adBankAccount.setBaAccountNumber(details.getBaAccountNumber());
            adBankAccount.setBaAccountUse(details.getBaAccountUse());
            adBankAccount.setBaCoaGlCashAccount(glCashChartOfAccount != null ? glCashChartOfAccount.getCoaCode() : null);
            adBankAccount.setBaCoaGlBankChargeAccount(glBankChargeChartOfAccount != null ? glBankChargeChartOfAccount.getCoaCode() : null);
            adBankAccount.setBaCoaGlInterestAccount(glInterestChartOfAccount != null ? glInterestChartOfAccount.getCoaCode() : null);
            adBankAccount.setBaCoaGlAdjustmentAccount(glAdjustmentChartOfAccount != null ? glAdjustmentChartOfAccount.getCoaCode() : null);
            adBankAccount.setBaCoaGlSalesDiscount(glSalesDiscountChartOfAccount != null ? glSalesDiscountChartOfAccount.getCoaCode() : null);
            adBankAccount.setBaCoaGlAdvanceAccount(glAdvanceAccountChartOfAccount != null ? glAdvanceAccountChartOfAccount.getCoaCode() : null);
            adBankAccount.setBaCoaGlCashDiscount(glCashDiscountChartOfAccount != null ? glCashDiscountChartOfAccount.getCoaCode() : null);
            adBankAccount.setBaCoaGlClearingAccount(glClearingChartOfAccount != null ? glClearingChartOfAccount.getCoaCode() : null);
            adBankAccount.setBaCoaGlUnappliedReceipt(glUnappliedReceiptChartOfAccount != null ? glUnappliedReceiptChartOfAccount.getCoaCode() : null);
            adBankAccount.setBaCoaGlOnAccountReceipt(glOnAccountReceiptChartOfAccount != null ? glOnAccountReceiptChartOfAccount.getCoaCode() : null);
            adBankAccount.setBaCoaGlUnappliedCheck(glUnappliedCheckChartOfAccount != null ? glUnappliedCheckChartOfAccount.getCoaCode() : null);
            adBankAccount.setBaFloatBalance(details.getBaFloatBalance());
            adBankAccount.setBaNextCheckNumber(details.getBaNextCheckNumber());
            adBankAccount.setBaEnable(details.getBaEnable());
            adBankAccount.setBaAccountNumberShow(details.getBaAccountNumberShow());
            adBankAccount.setBaAccountNumberTop(details.getBaAccountNumberTop());
            adBankAccount.setBaAccountNumberLeft(details.getBaAccountNumberLeft());
            adBankAccount.setBaAccountNameShow(details.getBaAccountNameShow());
            adBankAccount.setBaAccountNameTop(details.getBaAccountNameTop());
            adBankAccount.setBaAccountNameLeft(details.getBaAccountNameLeft());
            adBankAccount.setBaNumberShow(details.getBaNumberShow());
            adBankAccount.setBaNumberTop(details.getBaNumberTop());
            adBankAccount.setBaNumberLeft(details.getBaNumberLeft());
            adBankAccount.setBaDateShow(details.getBaDateShow());
            adBankAccount.setBaDateTop(details.getBaDateTop());
            adBankAccount.setBaDateLeft(details.getBaDateLeft());
            adBankAccount.setBaPayeeShow(details.getBaPayeeShow());
            adBankAccount.setBaPayeeTop(details.getBaPayeeTop());
            adBankAccount.setBaPayeeLeft(details.getBaPayeeLeft());
            adBankAccount.setBaAmountShow(details.getBaAmountShow());
            adBankAccount.setBaAmountTop(details.getBaAmountTop());
            adBankAccount.setBaAmountLeft(details.getBaAmountLeft());
            adBankAccount.setBaWordAmountShow(details.getBaWordAmountShow());
            adBankAccount.setBaWordAmountTop(details.getBaWordAmountTop());
            adBankAccount.setBaWordAmountLeft(details.getBaWordAmountLeft());
            adBankAccount.setBaCurrencyShow(details.getBaCurrencyShow());
            adBankAccount.setBaCurrencyTop(details.getBaCurrencyTop());
            adBankAccount.setBaCurrencyLeft(details.getBaCurrencyLeft());
            adBankAccount.setBaAddressShow(details.getBaAddressShow());
            adBankAccount.setBaAddressTop(details.getBaAddressTop());
            adBankAccount.setBaAddressLeft(details.getBaAddressLeft());
            adBankAccount.setBaMemoShow(details.getBaMemoShow());
            adBankAccount.setBaMemoTop(details.getBaMemoTop());
            adBankAccount.setBaMemoLeft(details.getBaMemoLeft());
            adBankAccount.setBadocNumberShow(details.getBadocNumberShow());
            adBankAccount.setBadocNumberTop(details.getBadocNumberTop());
            adBankAccount.setBadocNumberLeft(details.getBadocNumberLeft());
            adBankAccount.setBaFontSize(details.getBaFontSize());
            adBankAccount.setBaFontStyle(details.getBaFontStyle());
            adBankAccount.setBaIsCashAccount(details.getBaIsCashAccount());
            try {
                LocalAdBank adBank = adBankHome.findByBnkName(bankName, companyCode);
                adBank.addAdBankAccount(adBankAccount);
            } catch (FinderException ex) {
            }

            try {
                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(currencyName, companyCode);
                glFunctionalCurrency.addAdBankAccount(adBankAccount);
            } catch (FinderException ex) {
            }
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        try {
            Collection adBranchBBAs = adBranchBankAccountHome.findBbaByBaCodeAndRsName(adBankAccount.getBaCode(), responsibilityName, companyCode);
            LocalAdBranchBankAccount adBranchBankAccount;
            for (Object adBranchBBA : adBranchBBAs) {
                adBranchBankAccount = (LocalAdBranchBankAccount) adBranchBBA;
                adBankAccount.dropAdBranchBankAccount(adBranchBankAccount);
                adBranch = adBranchHome.findByPrimaryKey(adBranchBankAccount.getAdBranch().getBrCode());
                adBranch.dropAdBranchBankAccount(adBranchBankAccount);
                em.remove(adBranchBankAccount);
            }
            Iterator x = branchList.iterator();
            while (x.hasNext()) {
                AdModBranchBankAccountDetails brBaDetails = (AdModBranchBankAccountDetails) x.next();
                LocalGlChartOfAccount glCashCOA = null;
                LocalGlChartOfAccount glBankCOA = null;
                LocalGlChartOfAccount glInterestCOA = null;
                LocalGlChartOfAccount glAdjustmentCOA = null;
                LocalGlChartOfAccount glSalesDiscountCOA = null;
                LocalGlChartOfAccount glAdvanceCOA = null;

                if (brBaDetails.getBbaCashAccountNumber() != null && brBaDetails.getBbaCashAccountNumber().length() > 0) {

                    try {

                        glCashCOA = glChartOfAccountHome.findByCoaAccountNumber(brBaDetails.getBbaCashAccountNumber(), companyCode);

                    } catch (FinderException ex) {

                        throw new AdBACoaGlCashAccountNotFoundException();
                    }
                }
                if (brBaDetails.getBbaBankChargeAccountNumber() != null && brBaDetails.getBbaBankChargeAccountNumber().length() > 0) {

                    try {

                        glBankCOA = glChartOfAccountHome.findByCoaAccountNumber(brBaDetails.getBbaBankChargeAccountNumber(), companyCode);

                    } catch (FinderException ex) {

                        throw new AdBACoaGlBankChargeAccountNotFoundException();
                    }
                }
                if (brBaDetails.getBbaInterestAccountNumber() != null && brBaDetails.getBbaInterestAccountNumber().length() > 0) {

                    try {

                        glInterestCOA = glChartOfAccountHome.findByCoaAccountNumber(brBaDetails.getBbaInterestAccountNumber(), companyCode);

                    } catch (FinderException ex) {

                        throw new AdBACoaGlInterestAccountNotFoundException();
                    }
                }
                if (brBaDetails.getBbaAdjustmentAccountNumber() != null && brBaDetails.getBbaAdjustmentAccountNumber().length() > 0) {

                    try {

                        glAdjustmentCOA = glChartOfAccountHome.findByCoaAccountNumber(brBaDetails.getBbaAdjustmentAccountNumber(), companyCode);

                    } catch (FinderException ex) {

                        throw new AdBACoaGlAdjustmentAccountNotFoundException();
                    }
                }
                if (brBaDetails.getBbaSalesDiscountAccountNumber() != null && brBaDetails.getBbaSalesDiscountAccountNumber().length() > 0) {

                    try {

                        glSalesDiscountCOA = glChartOfAccountHome.findByCoaAccountNumber(brBaDetails.getBbaSalesDiscountAccountNumber(), companyCode);

                    } catch (FinderException ex) {

                        throw new AdBACoaGlSalesDiscountNotFoundException();
                    }
                }
                if (brBaDetails.getBbaAdvanceAccountNumber() != null && brBaDetails.getBbaAdvanceAccountNumber().length() > 0) {

                    try {

                        glAdvanceCOA = glChartOfAccountHome.findByCoaAccountNumber(brBaDetails.getBbaAdvanceAccountNumber(), companyCode);

                    } catch (FinderException ex) {

                        throw new AdBACoaGlAdvanceAccountNotFoundException();
                    }
                }

                adBranchBankAccount = adBranchBankAccountHome.create(glCashCOA != null ? glCashCOA.getCoaCode() : null, glBankCOA != null ? glBankCOA.getCoaCode() : null, glInterestCOA != null ? glInterestCOA.getCoaCode() : null, glAdjustmentCOA != null ? glAdjustmentCOA.getCoaCode() : null, glSalesDiscountCOA != null ? glSalesDiscountCOA.getCoaCode() : null, glAdvanceCOA != null ? glAdvanceCOA.getCoaCode() : null, 'N', companyCode);
                adBankAccount.addAdBranchBankAccount(adBranchBankAccount);

                adBranch = adBranchHome.findByPrimaryKey(brBaDetails.getBbaBranchCode());
                adBranch.addAdBranchBankAccount(adBranchBankAccount);
            }
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer companyCode) {

        Debug.print("AdBankAccountControllerBean getGlFcPrecisionUnit");
        try {
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            return adCompany.getGlFunctionalCurrency().getFcPrecision();
        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteAdBaEntry(Integer bankAccountCode, Integer companyCode) throws GlobalRecordAlreadyAssignedException, GlobalRecordAlreadyDeletedException {

        Debug.print("AdBankAccountControllerBean deleteAdBaEntry");
        LocalAdBankAccount adBankAccount = null;
        try {

            adBankAccount = adBankAccountHome.findByPrimaryKey(bankAccountCode);

        } catch (FinderException ex) {

            throw new GlobalRecordAlreadyDeletedException();
        }
        try {

            Collection fundTransfers1 = cmFundTransferHome.findByAdBankAccountFrom(adBankAccount.getBaCode(), companyCode);
            Collection fundTransfers2 = cmFundTransferHome.findByAdBankAccountTo(adBankAccount.getBaCode(), companyCode);

            if (!adBankAccount.getCmAdjustments().isEmpty() || !adBankAccount.getApSuppliers().isEmpty() || !adBankAccount.getApSupplierTypes().isEmpty() || !adBankAccount.getArCustomers().isEmpty() || !adBankAccount.getArCustomerTypes().isEmpty() || !adBankAccount.getApChecks().isEmpty() || !adBankAccount.getArReceipts().isEmpty() || !fundTransfers1.isEmpty() || !fundTransfers2.isEmpty()) {

                throw new GlobalRecordAlreadyAssignedException();
            }

            //    	    adBankAccount.entityRemove();
            em.remove(adBankAccount);

        } catch (GlobalRecordAlreadyAssignedException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdBnkAll(Integer companyCode) {

        Debug.print("AdBankAccountControllerBean getAdBnkAll");

        Collection adBanks = null;
        LocalAdBank adBank;
        ArrayList list = new ArrayList();
        try {

            adBanks = adBankHome.findEnabledBnkAll(companyCode);

        } catch (FinderException ex) {
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        if (adBanks.isEmpty()) {

            return null;
        }
        for (Object bank : adBanks) {

            adBank = (LocalAdBank) bank;

            list.add(adBank.getBnkName());
        }
        return list;
    }

    public ArrayList getGlFcAllWithDefault(Integer companyCode) {

        Debug.print("AdBankAccountControllerBean getGlFcAllWithDefault");

        Collection glFunctionalCurrencies;
        LocalGlFunctionalCurrency glFunctionalCurrency;
        LocalAdCompany adCompany;
        ArrayList list = new ArrayList();
        try {

            adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            glFunctionalCurrencies = glFunctionalCurrencyHome.findFcAllEnabled(EJBCommon.getGcCurrentDateWoTime().getTime(), companyCode);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        if (glFunctionalCurrencies.isEmpty()) {

            return null;
        }
        for (Object functionalCurrency : glFunctionalCurrencies) {

            glFunctionalCurrency = (LocalGlFunctionalCurrency) functionalCurrency;

            GlModFunctionalCurrencyDetails mdetails = new GlModFunctionalCurrencyDetails(glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), adCompany.getGlFunctionalCurrency().getFcName().equals(glFunctionalCurrency.getFcName()) ? EJBCommon.TRUE : EJBCommon.FALSE);

            list.add(mdetails);
        }
        return list;
    }

    public ArrayList getAdBrResAll(Integer responsibilityCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdBankAccountControllerBean getAdBrResAll");

        LocalAdBranchResponsibility adBranchResponsibility;
        LocalAdBranch adBranch;
        Collection adBranchResponsibilities = null;
        ArrayList list = new ArrayList();
        try {

            adBranchResponsibilities = adBranchResponsibilityHome.findByAdResponsibility(responsibilityCode, companyCode);

        } catch (FinderException ex) {
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        if (adBranchResponsibilities.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }
        try {

            for (Object branchResponsibility : adBranchResponsibilities) {

                adBranchResponsibility = (LocalAdBranchResponsibility) branchResponsibility;

                adBranch = adBranchResponsibility.getAdBranch();

                AdBranchDetails details = new AdBranchDetails();

                details.setBrCode(adBranch.getBrCode());
                details.setBrName(adBranch.getBrName());

                list.add(details);
            }

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        return list;
    }

    public ArrayList getAdBrBaAll(Integer bankAccountCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdBankAccountControllerBean getAdBrBaAll");

        LocalAdBranchBankAccount adBranchBankAccount;
        LocalAdBranch adBranch;
        LocalGlChartOfAccount glChartOfAccount;
        Collection adBranchBankAccounts = null;
        ArrayList list = new ArrayList();
        try {
            adBranchBankAccounts = adBranchBankAccountHome.findBbaByBaCode(bankAccountCode, companyCode);
        } catch (FinderException ex) {
            Debug.print("1");
        } catch (Exception ex) {
            Debug.print("2");
            throw new EJBException(ex.getMessage());
        }
        if (adBranchBankAccounts.isEmpty()) {
            Debug.print("3");
            throw new GlobalNoRecordFoundException();
        }
        try {
            for (Object branchBankAccount : adBranchBankAccounts) {
                adBranchBankAccount = (LocalAdBranchBankAccount) branchBankAccount;
                adBranch = adBranchHome.findByPrimaryKey(adBranchBankAccount.getAdBranch().getBrCode());

                AdModBranchBankAccountDetails details = new AdModBranchBankAccountDetails();
                details.setBbaBranchCode(adBranch.getBrCode());
                details.setBbaBranchName(adBranch.getBrName());

                if (adBranchBankAccount.getBbaGlCoaCashAccount() != null) {
                    glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchBankAccount.getBbaGlCoaCashAccount());
                    details.setBbaCashAccountNumber(glChartOfAccount.getCoaAccountNumber());
                    details.setBbaCashAccountDescription(glChartOfAccount.getCoaAccountDescription());
                }
                if (adBranchBankAccount.getBbaGlCoaBankChargeAccount() != null) {
                    glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchBankAccount.getBbaGlCoaBankChargeAccount());
                    details.setBbaBankChargeAccountNumber(glChartOfAccount.getCoaAccountNumber());
                    details.setBbaBankChargeAccountDescription(glChartOfAccount.getCoaAccountDescription());
                }
                if (adBranchBankAccount.getBbaGlCoaInterestAccount() != null) {
                    glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchBankAccount.getBbaGlCoaInterestAccount());
                    details.setBbaInterestAccountNumber(glChartOfAccount.getCoaAccountNumber());
                    details.setBbaInterestAccountDescription(glChartOfAccount.getCoaAccountDescription());
                }
                if (adBranchBankAccount.getBbaGlCoaAdjustmentAccount() != null) {
                    glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchBankAccount.getBbaGlCoaAdjustmentAccount());
                    details.setBbaAdjustmentAccountNumber(glChartOfAccount.getCoaAccountNumber());
                    details.setBbaAdjustmentAccountDescription(glChartOfAccount.getCoaAccountDescription());
                }
                if (adBranchBankAccount.getBbaGlCoaSalesDiscountAccount() != null) {
                    glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchBankAccount.getBbaGlCoaSalesDiscountAccount());
                    details.setBbaSalesDiscountAccountNumber(glChartOfAccount.getCoaAccountNumber());
                    details.setBbaSalesDiscountAccountDescription(glChartOfAccount.getCoaAccountDescription());
                }
                if (adBranchBankAccount.getBbaGlCoaAdvanceAccount() != null) {

                    glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchBankAccount.getBbaGlCoaAdvanceAccount());
                    details.setBbaAdvanceAccountNumber(glChartOfAccount.getCoaAccountNumber());
                    details.setBbaAdvanceAccountDescription(glChartOfAccount.getCoaAccountDescription());
                }
                list.add(details);
            }
        } catch (FinderException ex) {
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        return list;
    }

    public ArrayList getAdBrDsaAll(Integer documentSequenceAssignmentCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdDocumentSequenceAssignmentControllerBean getAdBrDsaAll");

        LocalAdBranchDocumentSequenceAssignment adBranchDsa;
        LocalAdBranch adBranch;
        Collection adBranchDsas = null;
        ArrayList list = new ArrayList();
        try {
            adBranchDsas = adBranchDsaHome.findBdsByDsaCode(documentSequenceAssignmentCode, companyCode);
        } catch (FinderException ex) {
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        if (adBranchDsas.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }
        try {

            for (Object branchDsa : adBranchDsas) {

                adBranchDsa = (LocalAdBranchDocumentSequenceAssignment) branchDsa;

                adBranch = adBranchHome.findByPrimaryKey(adBranchDsa.getAdBranch().getBrCode());

                AdModBranchDocumentSequenceAssignmentDetails details = new AdModBranchDocumentSequenceAssignmentDetails();

                details.setBrCode(adBranch.getBrCode());
                details.setBdsNextSequence(adBranchDsa.getBdsNextSequence());

                list.add(details);
            }

        } catch (FinderException ex) {
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        return list;
    }

    public AdResponsibilityDetails getAdRsByRsCode(Integer responsibilityCode) throws GlobalNoRecordFoundException {

        Debug.print("AdBankAccountControllerBean getAdRsByRsCode");

        LocalAdResponsibility adRes = null;
        try {
            adRes = adResHome.findByPrimaryKey(responsibilityCode);
        } catch (FinderException ex) {
        }
        AdResponsibilityDetails details = new AdResponsibilityDetails();
        details.setRsName(adRes.getRsName());
        return details;
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("AdBankAccountControllerBean ejbCreate");
    }
}