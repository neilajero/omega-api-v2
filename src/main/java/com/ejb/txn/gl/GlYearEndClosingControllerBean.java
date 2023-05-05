package com.ejb.txn.gl;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.ad.LocalAdBranchDocumentSequenceAssignmentHome;
import com.ejb.dao.ad.LocalAdDocumentSequenceAssignmentHome;
import com.ejb.dao.ap.ILocalApSupplierHome;
import com.ejb.dao.ap.LocalApCheckHome;
import com.ejb.dao.ap.LocalApPurchaseOrderHome;
import com.ejb.dao.ap.LocalApVoucherHome;
import com.ejb.dao.ar.LocalArInvoiceHome;
import com.ejb.dao.ar.LocalArReceiptHome;
import com.ejb.dao.cm.LocalCmAdjustmentHome;
import com.ejb.dao.cm.LocalCmFundTransferHome;
import com.ejb.dao.gl.*;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.LocalAdBranchDocumentSequenceAssignment;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdDocumentSequenceAssignment;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.entities.gen.LocalGenField;
import com.ejb.entities.gl.*;
import com.ejb.entities.inv.LocalInvDepreciationLedger;
import com.ejb.entities.inv.LocalInvItem;
import com.ejb.entities.inv.LocalInvItemLocation;
import com.ejb.entities.inv.LocalInvTag;
import com.ejb.exception.gl.*;
import com.ejb.exception.global.GlobalAccountNumberInvalidException;
import com.ejb.exception.global.GlobalDocumentNumberNotUniqueException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalNotAllTransactionsArePostedException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.gl.GlAccountingCalendarDetails;
import com.util.mod.gl.GlModAccountingCalendarValueDetails;

import jakarta.ejb.*;
import java.util.*;

@Stateless(name = "GlYearEndClosingControllerEJB")
public class GlYearEndClosingControllerBean extends EJBContextClass implements GlYearEndClosingController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private ILocalGlSetOfBookHome glSetOfBookHome;
    @EJB
    private ILocalGlAccountingCalendarValueHome glAccountingCalendarValueHome;
    @EJB
    private ILocalGlChartOfAccountBalanceHome glChartOfAccountBalanceHome;
    @EJB
    private ILocalGlInvestorAccountBalanceHome glInvestorAccountBalanceHome;
    @EJB
    private ILocalApSupplierHome apSupplierHome;
    @EJB
    private ILocalGlAccountingCalendarHome glAccountingCalendarHome;
    @EJB
    private ILocalGlTransactionCalendarHome glTransactionCalendarHome;
    @EJB
    private ILocalGlTransactionCalendarValueHome glTransactionCalendarValueHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalApCheckHome apCheckHome;
    @EJB
    private LocalApPurchaseOrderHome apPurchaseOrderHome;
    @EJB
    private LocalApVoucherHome apVoucherHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalCmAdjustmentHome cmAdjustmentHome;
    @EJB
    private LocalCmFundTransferHome cmFundTransferHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalGlJournalCategoryHome glJournalCategoryHome;
    @EJB
    private LocalGlJournalHome glJournalHome;
    @EJB
    private LocalGlJournalLineHome glJournalLineHome;
    @EJB
    private LocalGlJournalSourceHome glJournalSourceHome;
    @EJB
    private LocalGlResponsibilityHome glResponsibilityHome;
    @EJB
    private LocalInvAdjustmentHome invAdjustmentHome;
    @EJB
    private LocalInvBranchStockTransferHome invBranchStockTransferHome;
    @EJB
    private LocalInvDepreciationLedgerHome invDepreciationLedgerHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvStockTransferHome invStockTransferHome;
    @EJB
    private LocalInvTagHome invTagHome;

    public GlAccountingCalendarDetails getGlAcForClosing(Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlYearEndClosingControllerBean getGlAcForClosing");

        try {

            Collection glSetOfBooks = glSetOfBookHome.findBySobYearEndClosed(EJBCommon.FALSE, AD_CMPNY);

            if (glSetOfBooks.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }

            ArrayList glSetOfBookList = new ArrayList(glSetOfBooks);

            LocalGlSetOfBook glSetOfBook = (LocalGlSetOfBook) glSetOfBookList.get(0);

            return new GlAccountingCalendarDetails(glSetOfBook.getGlAccountingCalendar().getAcCode(), glSetOfBook.getGlAccountingCalendar().getAcName(), glSetOfBook.getGlAccountingCalendar().getAcDescription());

        }
        catch (GlobalNoRecordFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGlReportableAcvAll(Integer AD_CMPNY) {

        Debug.print("GlYearEndClosingControllerBean getGlReportableAcvAll");

        ArrayList list = new ArrayList();

        try {

            Collection glSetOfBooks = glSetOfBookHome.findSobAll(AD_CMPNY);

            for (Object setOfBook : glSetOfBooks) {

                LocalGlSetOfBook glSetOfBook = (LocalGlSetOfBook) setOfBook;

                Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findReportableAcvByAcCodeAndAcvStatus(glSetOfBook.getGlAccountingCalendar().getAcCode(), 'O', 'C', 'P', AD_CMPNY);

                for (Object accountingCalendarValue : glAccountingCalendarValues) {

                    LocalGlAccountingCalendarValue glAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                    GlModAccountingCalendarValueDetails mdetails = new GlModAccountingCalendarValueDetails();

                    mdetails.setAcvPeriodPrefix(glAccountingCalendarValue.getAcvPeriodPrefix());

                    // get year

                    GregorianCalendar gc = new GregorianCalendar();
                    gc.setTime(glAccountingCalendarValue.getAcvDateTo());

                    mdetails.setAcvYear(gc.get(Calendar.YEAR));

                    // is current

                    gc = EJBCommon.getGcCurrentDateWoTime();

                    if ((glAccountingCalendarValue.getAcvDateFrom().before(gc.getTime()) || glAccountingCalendarValue.getAcvDateFrom().equals(gc.getTime())) && (glAccountingCalendarValue.getAcvDateTo().after(gc.getTime()) || glAccountingCalendarValue.getAcvDateTo().equals(gc.getTime()))) {

                        mdetails.setAcvCurrent(true);
                    }

                    list.add(mdetails);
                }
            }

            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void executeFixedAssetDepreciation(Integer AC_CODE, String DTB_PRD, int DTB_YR, String userName, Integer resCode, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException, GlobalAccountNumberInvalidException, GlJREffectiveDatePeriodClosedException, GlJREffectiveDateNoPeriodExistException, GlDepreciationAlreadyMadeForThePeriodException, GlDepreciationPeriodInvalidException {

        Debug.print("GlYearEndClosingControllerBean executeFixedAssetDepreciation");

        LocalGlJournal glJournal = null;

        ArrayList list = new ArrayList();

        try {
            short precisionUnit = this.getGlFcPrecisionUnit(AD_CMPNY);

            Collection glSetOfBooks = glSetOfBookHome.findByAcvPeriodPrefixAndDate(DTB_PRD, EJBCommon.getIntendedDate(DTB_YR), AD_CMPNY);
            ArrayList glSetOfBookList = new ArrayList(glSetOfBooks);
            LocalGlSetOfBook glSetOfBook = (LocalGlSetOfBook) glSetOfBookList.get(0);

            LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodPrefix(glSetOfBook.getGlAccountingCalendar().getAcCode(), DTB_PRD, AD_CMPNY);

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            // validate if effective date has no period and period is closed

            try {
                Debug.print("details.getJrEffectiveDate(: " + glAccountingCalendarValue.getAcvDateTo());
                Debug.print("AD_CMPNY: " + AD_CMPNY);
                LocalGlSetOfBook glJournalSetOfBook = glSetOfBookHome.findByDate(glAccountingCalendarValue.getAcvDateTo(), AD_CMPNY);

                LocalGlAccountingCalendarValue glAccountingCalendarValueCheck = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), glAccountingCalendarValue.getAcvDateTo(), AD_CMPNY);

                if ((glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P')) {

                    throw new GlJREffectiveDatePeriodClosedException();
                }

            }
            catch (GlJREffectiveDatePeriodClosedException ex) {

                throw ex;

            }
            catch (FinderException ex) {

                throw new GlJREffectiveDateNoPeriodExistException();

            }
            catch (Exception ex) {

                Debug.printStackTrace(ex);
                throw new EJBException(ex.getMessage());
            }

            // validate effective date violation

            try {

                LocalGlSetOfBook glJournalSetOfBook = glSetOfBookHome.findByDate(glAccountingCalendarValue.getAcvDateTo(), AD_CMPNY);

                LocalGlTransactionCalendarValue glTransactionCalendarValue = glTransactionCalendarValueHome.findByTcCodeAndTcvDate(glJournalSetOfBook.getGlTransactionCalendar().getTcCode(), glAccountingCalendarValue.getAcvDateTo(), AD_CMPNY);

                LocalGlJournalSource glValidateJournalSource = glJournalSourceHome.findByJsName("MANUAL", AD_CMPNY);

                if (glTransactionCalendarValue.getTcvBusinessDay() == EJBCommon.FALSE && glValidateJournalSource.getJsEffectiveDateRule() == 'F') {

                    throw new GlJREffectiveDateViolationException();
                }

            }
            catch (GlJREffectiveDateViolationException ex) {

                throw ex;

            }
            catch (Exception ex) {

                Debug.printStackTrace(ex);
                throw new EJBException(ex.getMessage());
            }

            Collection invItems = invItemHome.findFixedAssetIiAll(AD_CMPNY);
            Iterator i = invItems.iterator();

            Debug.print("1.SIZE=" + invItems.size());
            double test;

            while (i.hasNext()) {
                LocalInvItem invItem = (LocalInvItem) i.next();

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.YEAR, (int) invItem.getIiLifeSpan());
                // Validate if referenceNumber exist.
                if ((glAccountingCalendarValue.getAcvDateFrom().before(cal.getTime())) || (cal.getTime().after(glAccountingCalendarValue.getAcvDateFrom()) || cal.getTime().equals(glAccountingCalendarValue.getAcvDateFrom()))) {

                    LocalGlJournal glJournalCheck = null;
                    try {
                        glJournalCheck = glJournalHome.findByJrNameAndBrCode("FADepreciation" + " " + invItem.getIiName() + " " + DTB_PRD + " " + DTB_YR, AD_BRNCH, AD_CMPNY);
                        if (glJournalCheck != null) {
                            throw new GlDepreciationAlreadyMadeForThePeriodException();
                        }

                    }
                    catch (GlDepreciationAlreadyMadeForThePeriodException ex) {
                        // getSessionContext().setRollbackOnly();
                        throw ex;
                    }
                    catch (FinderException ex) {

                    }

                    try {
                        glJournalCheck = null;
                        glJournalCheck = glJournalHome.findByJrReferenceNumberAndBrCode("FADepreciation" + " " + invItem.getIiName() + " " + DTB_PRD + " " + DTB_YR, AD_BRNCH, AD_CMPNY);
                        if (glJournalCheck != null) {
                            throw new GlDepreciationAlreadyMadeForThePeriodException();
                        }

                    }
                    catch (GlDepreciationAlreadyMadeForThePeriodException ex) {
                        // getSessionContext().setRollbackOnly();
                        throw ex;
                    }
                    catch (FinderException ex) {

                    }

                    Collection invItemLocations = invItem.getInvItemLocations();
                    Debug.print("itemlocation SIZE=" + invItemLocations.size());
                    Iterator x = invItemLocations.iterator();

                    Debug.print("glAccountingCalendarValue.getAcvDateTo()=" + glAccountingCalendarValue.getAcvDateTo());
                    while (x.hasNext()) {
                        LocalInvItemLocation invItemLocation = (LocalInvItemLocation) x.next();

                        Collection invTags = invTagHome.findByLessThanOrEqualToTransactionDateAndItemLocationAndAdBranchFromPORecevingLine(glAccountingCalendarValue.getAcvDateTo(), invItemLocation.getIlCode(), AD_BRNCH, AD_CMPNY);

                        for (Object tag : invTags) {
                            LocalInvTag invTag = (LocalInvTag) tag;

                            LocalInvDepreciationLedger existingDepreciationLedger = null;
                            try {
                                existingDepreciationLedger = invDepreciationLedgerHome.findExistingDLbyDateAndInvTag(glAccountingCalendarValue.getAcvDateTo(), invTag.getTgCode(), AD_CMPNY);

                            }
                            catch (FinderException ex) {

                            }

                            if (existingDepreciationLedger != null) {
                                continue;
                            }

                            Debug.print("SERIAL NUM=" + invTag.getTgSerialNumber());
                            Debug.print("PROPERTY CODE=" + invTag.getTgPropertyCode());
                            String serialNumber = invTag.getTgSerialNumber();
                            String propertyCode = invTag.getTgPropertyCode();
                            String itemName = invTag.getApPurchaseOrderLine().getInvItemLocation().getInvItem().getIiDescription();

                            double dlAcquisitionCost = invTag.getApPurchaseOrderLine().getPlUnitCost();
                            double dlLifeSpanMonths = invTag.getApPurchaseOrderLine().getInvItemLocation().getInvItem().getIiLifeSpan() * 12;
                            double dlMonthlyDepreciationAmount = EJBCommon.roundIt(dlAcquisitionCost / dlLifeSpanMonths, precisionUnit);
                            double totalCurrentBalance = 0d;
                            double totalDepreciation = 0d;

                            Collection depreciationLedgers = invTag.getInvDepreciationLedgers();
                            for (Object depreciationLedger : depreciationLedgers) {
                                LocalInvDepreciationLedger invDepreciationLedger = (LocalInvDepreciationLedger) depreciationLedger;

                                totalDepreciation += invDepreciationLedger.getDlDepreciationAmount();
                                totalCurrentBalance = invDepreciationLedger.getDlCurrentBalance();
                            }
                            Debug.print("dlAcquisitionCost=" + dlAcquisitionCost);
                            Debug.print("totalDepreciation=" + totalDepreciation);
                            Debug.print("totalCurrentBalance=" + totalCurrentBalance);
                            if (totalDepreciation == 0) {
                                Debug.print("1");
                                totalCurrentBalance = EJBCommon.roundIt(dlAcquisitionCost - dlMonthlyDepreciationAmount, precisionUnit);
                            } else {
                                Debug.print("2");
                                totalCurrentBalance = EJBCommon.roundIt(totalCurrentBalance - dlMonthlyDepreciationAmount, precisionUnit);
                            }

                            Debug.print("3=" + totalCurrentBalance);

                            // add lines on depreciation ledger
                            LocalInvDepreciationLedger invDepreciationLedger = invDepreciationLedgerHome.create(glAccountingCalendarValue.getAcvDateTo(), dlAcquisitionCost, dlMonthlyDepreciationAmount, dlLifeSpanMonths, totalCurrentBalance, AD_CMPNY);

                            invDepreciationLedger.setInvTag(invTag);

                            // validate if document number is unique document number is automatic then set
                            // next
                            // sequence
                            String docNumber = "";
                            String AccumulatedDepreciation = "";
                            String Depreciation = "";
                            try {

                                LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
                                LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

                                try {

                                    adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("GL JOURNAL", AD_CMPNY);

                                }
                                catch (FinderException ex) {

                                }

                                try {

                                    adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, AD_CMPNY);

                                }
                                catch (FinderException ex) {

                                }

                                LocalGlJournal glExistingJournal = null;
                                Debug.print("DocNumber1 " + docNumber);
                                try {

                                    glExistingJournal = glJournalHome.findByJrDocumentNumberAndJsNameAndBrCode(docNumber, "MANUAL", AD_BRNCH, AD_CMPNY);

                                }
                                catch (FinderException ex) {

                                }
                                Debug.print("DocNumber2 " + docNumber);
                                try {
                                    Debug.print(glExistingJournal.getJrCode() + " glExistingJournal " + glExistingJournal.getJrDocumentNumber());
                                }
                                catch (Exception e) {
                                    Debug.print("Not Null");
                                }

                                if (glExistingJournal != null) {

                                    throw new GlobalDocumentNumberNotUniqueException();
                                }
                                Debug.print("DocNumber3 " + docNumber);
                                try {
                                    Debug.print("adBranchDocumentSequenceAssignment: " + adBranchDocumentSequenceAssignment.getBdsCode());
                                    Debug.print("adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType()" + adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType());

                                }
                                catch (Exception e) {
                                    Debug.print("Not Null");
                                }
                                if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (docNumber == "")) {

                                    while (true) {

                                        if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                                            try {

                                                glJournalHome.findByJrDocumentNumberAndJsNameAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), "MANUAL", AD_BRNCH, AD_CMPNY);
                                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                                // docNumber=(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                                Debug.print("DocNumber4 " + docNumber);
                                            }
                                            catch (FinderException ex) {

                                                docNumber = (adDocumentSequenceAssignment.getDsaNextSequence());
                                                Debug.print("DocNumber5 " + docNumber);
                                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                                break;
                                            }

                                        } else {

                                            try {

                                                glJournalHome.findByJrDocumentNumberAndJsNameAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), "MANUAL", AD_BRNCH, AD_CMPNY);
                                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                                // docNumber=(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                                            }
                                            catch (FinderException ex) {

                                                docNumber = (adBranchDocumentSequenceAssignment.getBdsNextSequence());
                                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                                break;
                                            }
                                        }
                                    }
                                }

                            }
                            catch (Exception ex) {

                                Debug.printStackTrace(ex);
                                getSessionContext().setRollbackOnly();
                                throw new EJBException(ex.getMessage());
                            }
                            double depreciationAmount = 0d;

                            try {
                                LocalGlChartOfAccount glChartOfAccount = null;
                                glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(Integer.parseInt(invItem.getGlCoaAccumulatedDepreciationAccount()));
                                AccumulatedDepreciation = glChartOfAccount.getCoaAccountNumber();

                            }
                            catch (FinderException ex) {

                                throw new GlobalNoRecordFoundException();
                            }

                            try {
                                LocalGlChartOfAccount glChartOfAccount = null;
                                glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(Integer.parseInt(invItem.getGlCoaDepreciationAccount()));
                                Depreciation = glChartOfAccount.getCoaAccountNumber();
                            }
                            catch (FinderException ex) {

                                throw new GlobalNoRecordFoundException();
                            }

                            if (docNumber == "") {
                                throw new Exception();
                            }

                            glJournal = glJournalHome.create("FADepreciation" + " " + itemName + " : " + propertyCode + " : " + serialNumber + " " + DTB_PRD + " " + DTB_YR, "Fixed Asset Depreciation for item: " + itemName + " for the period of " + DTB_PRD + " " + DTB_YR, glAccountingCalendarValue.getAcvDateTo(), 0.0d, null, docNumber, null, 1.0, null, null, 'N', EJBCommon.FALSE, EJBCommon.FALSE, userName, new java.util.Date(), userName, new java.util.Date(), null, null, null, null, null, null, EJBCommon.FALSE, "FADepreciation" + " " + itemName + " " + DTB_PRD + " " + DTB_YR, AD_BRNCH, AD_CMPNY);

                            LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("MANUAL", AD_CMPNY);
                            glJournal.setGlJournalSource(glJournalSource);

                            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adPreference.getPrfApDefaultPrCurrency(), AD_CMPNY);
                            glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

                            LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("GENERAL", AD_CMPNY);
                            glJournal.setGlJournalCategory(glJournalCategory);

                            this.addGlJlEntry(Short.parseShort("1"), Byte.parseByte("1"), dlMonthlyDepreciationAmount, Depreciation, glJournal, resCode, AD_BRNCH, AD_CMPNY);
                            this.addGlJlEntry(Short.parseShort("2"), Byte.parseByte("0"), dlMonthlyDepreciationAmount, AccumulatedDepreciation, glJournal, resCode, AD_BRNCH, AD_CMPNY);
                        }
                    }
                }
            }
        }
        catch (GlobalNoRecordFoundException ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }
        catch (GlJREffectiveDatePeriodClosedException | GlDepreciationPeriodInvalidException |
               GlDepreciationAlreadyMadeForThePeriodException ex) {
            throw ex;

        }
        catch (GlJREffectiveDateNoPeriodExistException ex) {
            // Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }  // getSessionContext().setRollbackOnly();
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods

    private void addGlJlEntry(short lineNumber, Byte debit, double amount, String coaAccount, LocalGlJournal glJournal, Integer RES_CODE, Integer AD_BRNCH, Integer AD_CMPNY) throws GlJLChartOfAccountNotFoundException, GlJLChartOfAccountPermissionDeniedException {

        Debug.print("GlJournalEntryControllerBean addGlJlEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            // validate if coa exists

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(coaAccount, AD_BRNCH, AD_CMPNY);

                if (glChartOfAccount.getCoaEnable() == EJBCommon.FALSE) {
                    throw new GlJLChartOfAccountNotFoundException();
                }

            }
            catch (FinderException ex) {

                throw new GlJLChartOfAccountNotFoundException();
            }

            // validate responsibility coa permission

            LocalGlResponsibility glResponsibility = null;

            try {

                glResponsibility = glResponsibilityHome.findByPrimaryKey(RES_CODE);

            }
            catch (FinderException ex) {

                throw new GlJLChartOfAccountPermissionDeniedException();
            }

            if (!this.isPermitted(glResponsibility.getGlOrganization(), glChartOfAccount, adCompany.getGenField(), AD_CMPNY)) {

                throw new GlJLChartOfAccountPermissionDeniedException();
            }

            // create journal

            LocalGlJournalLine glJournalLine = glJournalLineHome.create(lineNumber, debit, EJBCommon.roundIt(amount, adCompany.getGlFunctionalCurrency().getFcPrecision()), null, AD_CMPNY);
            glJournalLine.setGlJournal(glJournal);
            glJournalLine.setGlChartOfAccount(glChartOfAccount);

        }
        catch (GlJLChartOfAccountPermissionDeniedException | GlJLChartOfAccountNotFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private boolean isPermitted(LocalGlOrganization glOrganization, LocalGlChartOfAccount glChartOfAccount, LocalGenField genField, Integer AD_CMPNY) {

        Debug.print("GlJournalEntryControllerBean isPermitted");

        String strSeparator = null;
        short numberOfSegment = 0;
        short genNumberOfSegment = 0;

        try {
            char chrSeparator = genField.getFlSegmentSeparator();
            genNumberOfSegment = genField.getFlNumberOfSegment();
            strSeparator = String.valueOf(chrSeparator);
        }
        catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

        Collection glAccountRanges = glOrganization.getGlAccountRanges();

        for (Object accountRange : glAccountRanges) {

            LocalGlAccountRange glAccountRange = (LocalGlAccountRange) accountRange;

            String[] chartOfAccountSegmentValue = new String[genNumberOfSegment];
            String[] accountLowSegmentValue = new String[genNumberOfSegment];
            String[] accountHighSegmentValue = new String[genNumberOfSegment];

            // tokenize coa

            StringTokenizer st = new StringTokenizer(glChartOfAccount.getCoaAccountNumber(), strSeparator);

            int j = 0;

            while (st.hasMoreTokens()) {

                chartOfAccountSegmentValue[j] = st.nextToken();
                j++;
            }

            // tokenize account low

            st = new StringTokenizer(glAccountRange.getArAccountLow(), strSeparator);

            j = 0;

            while (st.hasMoreTokens()) {

                accountLowSegmentValue[j] = st.nextToken();
                j++;
            }

            // tokenize account high
            st = new StringTokenizer(glAccountRange.getArAccountHigh(), strSeparator);

            j = 0;

            while (st.hasMoreTokens()) {

                accountHighSegmentValue[j] = st.nextToken();
                j++;
            }

            boolean isOverlapped = false;

            for (int k = 0; k < genNumberOfSegment; k++) {

                if (chartOfAccountSegmentValue[k].compareTo(accountLowSegmentValue[k]) >= 0 && chartOfAccountSegmentValue[k].compareTo(accountHighSegmentValue[k]) <= 0) {

                    isOverlapped = true;

                } else {

                    isOverlapped = false;
                    break;
                }
            }

            if (isOverlapped) {
                return true;
            }
        }

        return false;
    }

    public void executeGlYearEndClosing(Integer AC_CODE, Integer AD_CMPNY) throws GlobalNotAllTransactionsArePostedException, GlobalAccountNumberInvalidException {

        Debug.print("GlYearEndClosingControllerBean executeGlYearEndClosing");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            LocalGlAccountingCalendar glAccountingCalendar = glAccountingCalendarHome.findByPrimaryKey(AC_CODE);
            LocalGlSetOfBook glSetOfBook = glSetOfBookHome.findByAcCode(glAccountingCalendar.getAcCode(), AD_CMPNY);
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            if (adPreference.getPrfGlYearEndCloseRestriction() == 1) {
                // check all transactions if already posted

                LocalGlAccountingCalendarValue glAcv1 = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodNumber(glAccountingCalendar.getAcCode(), (short) 1, AD_CMPNY);

                LocalGlAccountingCalendarValue glAcv2 = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodNumber(glAccountingCalendar.getAcCode(), glAccountingCalendar.getGlPeriodType().getPtPeriodPerYear(), AD_CMPNY);

                Collection glJournals = glJournalHome.findUnpostedJrByJrDateRange(glAcv1.getAcvDateFrom(), glAcv2.getAcvDateTo(), AD_CMPNY);
                Collection arInvoices = arInvoiceHome.findUnpostedInvByInvDateRange(glAcv1.getAcvDateFrom(), glAcv2.getAcvDateTo(), AD_CMPNY);
                Collection arReceipts = arReceiptHome.findUnpostedRctByRctDateRange(glAcv1.getAcvDateFrom(), glAcv2.getAcvDateTo(), AD_CMPNY);
                Collection apVouchers = apVoucherHome.findUnpostedVouByVouDateRange(glAcv1.getAcvDateFrom(), glAcv2.getAcvDateTo(), AD_CMPNY);
                Collection apChecks = apCheckHome.findUnpostedChkByChkDateRange(glAcv1.getAcvDateFrom(), glAcv2.getAcvDateTo(), AD_CMPNY);
                Collection cmAdjustments = cmAdjustmentHome.findUnpostedCmAdjByCmAdjDateRange(glAcv1.getAcvDateFrom(), glAcv2.getAcvDateTo(), AD_CMPNY);
                Collection cmFundTransfers = cmFundTransferHome.findUnpostedCmFtByCmFtDateRange(glAcv1.getAcvDateFrom(), glAcv2.getAcvDateTo(), AD_CMPNY);
                Collection invAdjustments = invAdjustmentHome.findUnpostedInvAdjByInvAdjDateRange(glAcv1.getAcvDateFrom(), glAcv2.getAcvDateTo(), AD_CMPNY);
                Collection apReceivingItems = apPurchaseOrderHome.findUnpostedPoReceivingByPoReceivingDateRange(glAcv1.getAcvDateFrom(), glAcv2.getAcvDateTo(), AD_CMPNY);
                Collection invStockTransfers = invStockTransferHome.findUnpostedStByStDateRange(glAcv1.getAcvDateFrom(), glAcv2.getAcvDateTo(), AD_CMPNY);
                Collection invBranchStockTransfers = invBranchStockTransferHome.findUnpostedBstByBstDateRange(glAcv1.getAcvDateFrom(), glAcv2.getAcvDateTo(), AD_CMPNY);

                if ((glJournals != null && !glJournals.isEmpty()) || (arInvoices != null && !arInvoices.isEmpty())
                        || (arReceipts != null && !arReceipts.isEmpty()) || (apVouchers != null && !apVouchers.isEmpty())
                        || (apChecks != null && !apChecks.isEmpty()) || (cmAdjustments != null && !cmAdjustments.isEmpty())
                        || (cmFundTransfers != null && !cmFundTransfers.isEmpty()) || (invAdjustments != null && !invAdjustments.isEmpty())
                        || (apReceivingItems != null && !apReceivingItems.isEmpty()) || (invStockTransfers != null && !invStockTransfers.isEmpty())
                        || (invBranchStockTransfers != null && !invBranchStockTransfers.isEmpty())) {

                    throw new GlobalNotAllTransactionsArePostedException();
                }
            }

            if (false) {
                throw new GlobalNotAllTransactionsArePostedException();
            }

            // create new calendar if necessary

            LocalGlSetOfBook glSubsequentSetOfBook = null;

            try {

                Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(glSetOfBook.getGlAccountingCalendar().getAcYear(), AD_CMPNY);

                if (!glSubsequentSetOfBooks.isEmpty()) {

                    ArrayList glSubsequentSetOfBookList = new ArrayList(glSubsequentSetOfBooks);
                    glSubsequentSetOfBook = (LocalGlSetOfBook) glSubsequentSetOfBookList.get(0);
                }

            }
            catch (FinderException ex) {

            }

            if (glSubsequentSetOfBook == null) {

                LocalGlAccountingCalendarValue glFirstAccountingCalendarValue = null;
                LocalGlAccountingCalendarValue glLastAccountingCalendarValue = null;

                // get new year

                LocalGlAccountingCalendarValue glYearAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodNumber(glAccountingCalendar.getAcCode(), glAccountingCalendar.getGlPeriodType().getPtPeriodPerYear(), AD_CMPNY);

                GregorianCalendar gc = new GregorianCalendar();
                gc.setTime(glYearAccountingCalendarValue.getAcvDateTo());
                int NEW_YEAR = gc.get(Calendar.YEAR) + 1;

                // create accounting calendar

                LocalGlAccountingCalendar glNewAccountingCalendar = glAccountingCalendarHome.create("CALENDAR " + NEW_YEAR, "CALENDAR " + NEW_YEAR, NEW_YEAR, AD_CMPNY);

                glAccountingCalendar.getGlPeriodType().addGlAccountingCalendar(glNewAccountingCalendar);

                // create accounting calendar values

                Collection glAccountingCalendarValues = glAccountingCalendar.getGlAccountingCalendarValues();

                Iterator i = glAccountingCalendarValues.iterator();

                while (i.hasNext()) {

                    LocalGlAccountingCalendarValue glAccountingCalendarValue = (LocalGlAccountingCalendarValue) i.next();

                    GregorianCalendar gcFrom = new GregorianCalendar();
                    GregorianCalendar gcTo = new GregorianCalendar();

                    gcFrom.setTime(glAccountingCalendarValue.getAcvDateFrom());
                    gcTo.setTime(glAccountingCalendarValue.getAcvDateTo());

                    gcFrom.add(Calendar.YEAR, 1);
                    gcTo.add(Calendar.YEAR, 1);

                    if (gcTo.isLeapYear(gcTo.get(Calendar.YEAR)) && gcTo.get(Calendar.MONTH) == 1 && gcTo.get(Calendar.DATE) == 28) {

                        gcTo.add(Calendar.DATE, 1);

                    } else if (!gcTo.isLeapYear(gcTo.get(Calendar.YEAR)) && gcTo.get(Calendar.MONTH) == 2 && gcTo.get(Calendar.DATE) == 1) {

                        gcTo.add(Calendar.DATE, -1);
                    }

                    LocalGlAccountingCalendarValue glNewAccountingCalendarValue = glAccountingCalendarValueHome.create(glAccountingCalendarValue.getAcvPeriodPrefix(), glAccountingCalendarValue.getAcvQuarter(), glAccountingCalendarValue.getAcvPeriodNumber(), gcFrom.getTime(), gcTo.getTime(), 'N', null, null, null, null, AD_CMPNY);

                    glNewAccountingCalendar.addGlAccountingCalendarValue(glNewAccountingCalendarValue);

                    // check if first to be used in tc generation

                    if (glNewAccountingCalendarValue.getAcvPeriodNumber() == 1) {

                        glFirstAccountingCalendarValue = glNewAccountingCalendarValue;
                    }

                    // check if last to be used in tc generation

                    if (!i.hasNext()) {

                        glLastAccountingCalendarValue = glNewAccountingCalendarValue;
                    }
                }

                // generate transaction calendar

                LocalGlTransactionCalendar glTransactionCalendar = glTransactionCalendarHome.create(glNewAccountingCalendar.getAcName(), glNewAccountingCalendar.getAcDescription(), AD_CMPNY);

                gc = new GregorianCalendar();
                gc.setTime(glFirstAccountingCalendarValue.getAcvDateFrom());

                while (!gc.getTime().after(glLastAccountingCalendarValue.getAcvDateTo())) {

                    LocalGlTransactionCalendarValue glTransactionCalendarValue = glTransactionCalendarValueHome.create(gc.getTime(), (short) gc.get(Calendar.DAY_OF_WEEK), EJBCommon.TRUE, AD_CMPNY);

                    glTransactionCalendar.addGlTransactionCalendarValue(glTransactionCalendarValue);

                    gc.add(Calendar.DATE, 1);
                }

                // generate set of book

                glSubsequentSetOfBook = glSetOfBookHome.create(EJBCommon.FALSE, AD_CMPNY);

                glNewAccountingCalendar.addGlSetOfBook(glSubsequentSetOfBook);
                glTransactionCalendar.addGlSetOfBook(glSubsequentSetOfBook);

                // generate coa balances

                Collection glChartOfAccounts = glChartOfAccountHome.findCoaAll(AD_CMPNY);

                glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(glNewAccountingCalendar.getAcCode(), AD_CMPNY);

                for (Object chartOfAccount : glChartOfAccounts) {

                    LocalGlChartOfAccount glChartOfAccount = (LocalGlChartOfAccount) chartOfAccount;

                    for (Object accountingCalendarValue : glAccountingCalendarValues) {

                        LocalGlAccountingCalendarValue glAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                        LocalGlChartOfAccountBalance glChartOfAccountBalance = glChartOfAccountBalanceHome.create(0d, 0d, 0d, 0d, AD_CMPNY);
                        glAccountingCalendarValue.addGlChartOfAccountBalance(glChartOfAccountBalance);
                        glChartOfAccount.addGlChartOfAccountBalance(glChartOfAccountBalance);
                    }
                }

                // generate investor balances

                Collection glInvestors = apSupplierHome.findAllSplInvestor(AD_CMPNY);
                glAccountingCalendarValues = glNewAccountingCalendar.getGlAccountingCalendarValues();

                for (Object glInvestor : glInvestors) {

                    LocalApSupplier apSupplier = (LocalApSupplier) glInvestor;

                    for (Object accountingCalendarValue : glAccountingCalendarValues) {

                        LocalGlAccountingCalendarValue glAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                        LocalGlInvestorAccountBalance glInvestorAccountBalance = glInvestorAccountBalanceHome.create(0d, 0d, (byte) 0, (byte) 0, 0d, 0d, 0d, 0d, 0d, 0d, AD_CMPNY);

                        glAccountingCalendarValue.addGlInvestorAccountBalance(glInvestorAccountBalance);
                        apSupplier.addGlInvestorAccountBalance(glInvestorAccountBalance);
                    }
                }
            }

            // carry forward balances to subsequent year

            // from and to acvs

            LocalGlAccountingCalendarValue glFromAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodNumber(glSetOfBook.getGlAccountingCalendar().getAcCode(), glSetOfBook.getGlAccountingCalendar().getGlPeriodType().getPtPeriodPerYear(), AD_CMPNY);

            LocalGlAccountingCalendarValue glToAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodNumber(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), (short) 1, AD_CMPNY);

            Collection glForwardChartOfAccounts = glChartOfAccountHome.findCoaAll(AD_CMPNY);

            for (Object glForwardChartOfAccount : glForwardChartOfAccounts) {

                LocalGlChartOfAccount glChartOfAccount = (LocalGlChartOfAccount) glForwardChartOfAccount;
                LocalGlChartOfAccount glRetainedEarningsChartOfAccount = null;

                String ACCOUNT_TYPE = glChartOfAccount.getCoaAccountType();

                // carry forward balances

                LocalGlChartOfAccountBalance glFromChartOfAccountBalance = glChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(glFromAccountingCalendarValue.getAcvCode(), glChartOfAccount.getCoaCode(), AD_CMPNY);

                Debug.print("glToAccountingCalendarValue.getAcvCode()=" + glToAccountingCalendarValue.getAcvCode());

                LocalGlChartOfAccountBalance glToChartOfAccountBalance = glChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(glToAccountingCalendarValue.getAcvCode(), glChartOfAccount.getCoaCode(), AD_CMPNY);

                System.out.println("glToChartOfAccountBalance.getCoabCode()=" + glToChartOfAccountBalance.getCoabCode());

                if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY") || ACCOUNT_TYPE.equals("OWNERS EQUITY")) {

                    glToChartOfAccountBalance.setCoabBeginningBalance(glToChartOfAccountBalance.getCoabBeginningBalance() + glFromChartOfAccountBalance.getCoabEndingBalance());

                    glToChartOfAccountBalance.setCoabEndingBalance(glToChartOfAccountBalance.getCoabEndingBalance() + glFromChartOfAccountBalance.getCoabEndingBalance());

                } else { // revenue & expense

                    try {

                        glRetainedEarningsChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(adCompany.getCmpRetainedEarnings(), AD_CMPNY);

                    }
                    catch (FinderException ex) {

                        throw new GlobalAccountNumberInvalidException();
                    }

                    LocalGlChartOfAccountBalance glRetainedEarningsBalance = glChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(glToAccountingCalendarValue.getAcvCode(), glRetainedEarningsChartOfAccount.getCoaCode(), AD_CMPNY);

                    if (ACCOUNT_TYPE.equals("REVENUE")) {

                        glRetainedEarningsBalance.setCoabBeginningBalance(glRetainedEarningsBalance.getCoabBeginningBalance() + glFromChartOfAccountBalance.getCoabEndingBalance());

                        glRetainedEarningsBalance.setCoabEndingBalance(glRetainedEarningsBalance.getCoabEndingBalance() + glFromChartOfAccountBalance.getCoabEndingBalance());

                    } else { // expense

                        glRetainedEarningsBalance.setCoabBeginningBalance(glRetainedEarningsBalance.getCoabBeginningBalance() - glFromChartOfAccountBalance.getCoabEndingBalance());

                        glRetainedEarningsBalance.setCoabEndingBalance(glRetainedEarningsBalance.getCoabEndingBalance() - glFromChartOfAccountBalance.getCoabEndingBalance());
                    }
                }

                // carry forward to subsequent periods of the subsequent year

                Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), (short) 1, AD_CMPNY);

                for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {

                    LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;

                    if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY") || ACCOUNT_TYPE.equals("OWNERS EQUITY")) {

                        LocalGlChartOfAccountBalance glChartOfAccountBalance = glChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(glSubsequentAccountingCalendarValue.getAcvCode(), glChartOfAccount.getCoaCode(), AD_CMPNY);

                        glChartOfAccountBalance.setCoabEndingBalance(glChartOfAccountBalance.getCoabEndingBalance() + glFromChartOfAccountBalance.getCoabEndingBalance());

                        glChartOfAccountBalance.setCoabBeginningBalance(glChartOfAccountBalance.getCoabBeginningBalance() + glFromChartOfAccountBalance.getCoabEndingBalance());

                    } else { // revenue & expense

                        LocalGlChartOfAccountBalance glChartOfAccountBalance = glChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(glSubsequentAccountingCalendarValue.getAcvCode(), glRetainedEarningsChartOfAccount.getCoaCode(), AD_CMPNY);

                        if (ACCOUNT_TYPE.equals("REVENUE")) {

                            glChartOfAccountBalance.setCoabEndingBalance(glChartOfAccountBalance.getCoabEndingBalance() + glFromChartOfAccountBalance.getCoabEndingBalance());

                            glChartOfAccountBalance.setCoabBeginningBalance(glChartOfAccountBalance.getCoabBeginningBalance() + glFromChartOfAccountBalance.getCoabEndingBalance());

                        } else { // expense

                            glChartOfAccountBalance.setCoabEndingBalance(glChartOfAccountBalance.getCoabEndingBalance() - glFromChartOfAccountBalance.getCoabEndingBalance());

                            glChartOfAccountBalance.setCoabBeginningBalance(glChartOfAccountBalance.getCoabBeginningBalance() - glFromChartOfAccountBalance.getCoabEndingBalance());
                        }
                    }
                }
            }

            Collection glForwardInvestor = apSupplierHome.findAllSplInvestor(AD_CMPNY);

            for (Object o : glForwardInvestor) {

                LocalApSupplier apSupplier = (LocalApSupplier) o;

                LocalGlInvestorAccountBalance glFromInvestorBalance = glInvestorAccountBalanceHome.findByAcvCodeAndSplCode(glFromAccountingCalendarValue.getAcvCode(), apSupplier.getSplCode(), AD_CMPNY);

                Debug.print("apSupplier.getSplCode()=" + apSupplier.getSplCode());
                Debug.print("glToAccountingCalendarValue.getAcvCode()=" + glToAccountingCalendarValue.getAcvCode());

                LocalGlInvestorAccountBalance glToInvestorBalance = glInvestorAccountBalanceHome.findByAcvCodeAndSplCode(glToAccountingCalendarValue.getAcvCode(), apSupplier.getSplCode(), AD_CMPNY);

                glToInvestorBalance.setIrabBeginningBalance(glToInvestorBalance.getIrabBeginningBalance() + glFromInvestorBalance.getIrabEndingBalance());

                glToInvestorBalance.setIrabEndingBalance(glToInvestorBalance.getIrabEndingBalance() + glFromInvestorBalance.getIrabEndingBalance());

                // carry forward to subsequent periods of the subsequent year

                Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), (short) 1, AD_CMPNY);

                for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {

                    LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;

                    LocalGlInvestorAccountBalance glInvestorAccountBalance = glInvestorAccountBalanceHome.findByAcvCodeAndSplCode(glSubsequentAccountingCalendarValue.getAcvCode(), apSupplier.getSplCode(), AD_CMPNY);

                    glInvestorAccountBalance.setIrabEndingBalance(glInvestorAccountBalance.getIrabEndingBalance() + glFromInvestorBalance.getIrabEndingBalance());

                    glInvestorAccountBalance.setIrabBeginningBalance(glInvestorAccountBalance.getIrabBeginningBalance() + glFromInvestorBalance.getIrabEndingBalance());
                }
            }

            // set acvs to closed

            Collection glToCloseAccountingCalendarValues = glAccountingCalendar.getGlAccountingCalendarValues();

            for (Object glToCloseAccountingCalendarValue : glToCloseAccountingCalendarValues) {

                LocalGlAccountingCalendarValue glAccountingCalendarValue = (LocalGlAccountingCalendarValue) glToCloseAccountingCalendarValue;

                glAccountingCalendarValue.setAcvStatus('C');

                if (glAccountingCalendarValue.getAcvDateClosed() == null) {

                    glAccountingCalendarValue.setAcvDateClosed(EJBCommon.getGcCurrentDateWoTime().getTime());
                }
            }

            // set sob to closed

            glSetOfBook.setSobYearEndClosed(EJBCommon.TRUE);

        }
        catch (GlobalNotAllTransactionsArePostedException | GlobalAccountNumberInvalidException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // Private Methods

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("GlYearEndClosingControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlYearEndClosingControllerBean ejbCreate");
    }

}