package com.ejb.txn.cm;

import java.util.*;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.RemoveException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.entities.ad.LocalAdAmountLimit;
import com.ejb.dao.ad.LocalAdAmountLimitHome;
import com.ejb.entities.ad.LocalAdApproval;
import com.ejb.dao.ad.LocalAdApprovalHome;
import com.ejb.entities.ad.LocalAdApprovalQueue;
import com.ejb.dao.ad.LocalAdApprovalQueueHome;
import com.ejb.entities.ad.LocalAdApprovalUser;
import com.ejb.dao.ad.LocalAdApprovalUserHome;
import com.ejb.entities.ad.LocalAdBankAccount;
import com.ejb.entities.ad.LocalAdBankAccountBalance;
import com.ejb.dao.ad.LocalAdBankAccountBalanceHome;
import com.ejb.dao.ad.LocalAdBankAccountHome;
import com.ejb.entities.ad.LocalAdBranchBankAccount;
import com.ejb.dao.ad.LocalAdBranchBankAccountHome;
import com.ejb.entities.ad.LocalAdBranchDocumentSequenceAssignment;
import com.ejb.dao.ad.LocalAdBranchDocumentSequenceAssignmentHome;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdDocumentSequenceAssignment;
import com.ejb.dao.ad.LocalAdDocumentSequenceAssignmentHome;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.dao.ad.LocalAdUserHome;
import com.ejb.dao.ap.ILocalApSupplierHome;
import com.ejb.entities.ar.LocalArCustomer;
import com.ejb.dao.ar.LocalArCustomerHome;
import com.ejb.entities.ar.LocalArSalesOrder;
import com.ejb.dao.ar.LocalArSalesOrderHome;
import com.ejb.entities.cm.LocalCmAdjustment;
import com.ejb.dao.cm.LocalCmAdjustmentHome;
import com.ejb.entities.cm.LocalCmDistributionRecord;
import com.ejb.dao.cm.LocalCmDistributionRecordHome;
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
import com.ejb.exception.global.GlobalTransactionAlreadyPendingException;
import com.ejb.exception.global.GlobalTransactionAlreadyPostedException;
import com.ejb.exception.global.GlobalTransactionAlreadyVoidException;
import com.ejb.exception.global.GlobalTransactionAlreadyVoidPostedException;
import com.ejb.dao.gl.ILocalGlAccountingCalendarValueHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountBalanceHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.dao.gl.ILocalGlSetOfBookHome;
import com.ejb.entities.gl.LocalGlAccountingCalendarValue;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.gl.LocalGlChartOfAccountBalance;
import com.ejb.entities.gl.LocalGlForexLedger;
import com.ejb.dao.gl.LocalGlForexLedgerHome;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
import com.ejb.entities.gl.LocalGlFunctionalCurrencyRate;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.entities.gl.LocalGlJournal;
import com.ejb.entities.gl.LocalGlJournalBatch;
import com.ejb.dao.gl.LocalGlJournalBatchHome;
import com.ejb.entities.gl.LocalGlJournalCategory;
import com.ejb.dao.gl.LocalGlJournalCategoryHome;
import com.ejb.dao.gl.LocalGlJournalHome;
import com.ejb.entities.gl.LocalGlJournalLine;
import com.ejb.dao.gl.LocalGlJournalLineHome;
import com.ejb.entities.gl.LocalGlJournalSource;
import com.ejb.dao.gl.LocalGlJournalSourceHome;
import com.ejb.entities.gl.LocalGlSetOfBook;
import com.ejb.entities.gl.LocalGlSuspenseAccount;
import com.ejb.dao.gl.LocalGlSuspenseAccountHome;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.cm.CmAdjustmentDetails;

@Stateless(name = "CmAdjustmentEntryApiControllerEJB")
public class CmAdjustmentEntryApiControllerBean extends EJBContextClass implements CmAdjustmentEntryApiController {

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
    private ILocalApSupplierHome apSupplierHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalCmAdjustmentHome cmAdjustmentHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalArSalesOrderHome arSalesOrderHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdAmountLimitHome adAmountLimitHome;
    @EJB
    private LocalAdApprovalUserHome adApprovalUserHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchBankAccountHome adBranchBankAccountHome;
    @EJB
    private LocalCmDistributionRecordHome cmDistributionRecordHome;
    @EJB
    private LocalGlJournalHome glJournalHome;
    @EJB
    private LocalGlJournalBatchHome glJournalBatchHome;
    @EJB
    private LocalGlSuspenseAccountHome glSuspenseAccountHome;
    @EJB
    private LocalGlJournalLineHome glJournalLineHome;
    @EJB
    private LocalGlJournalSourceHome glJournalSourceHome;
    @EJB
    private LocalGlJournalCategoryHome glJournalCategoryHome;
    @EJB
    private LocalAdBankAccountBalanceHome adBankAccountBalanceHome;
    @EJB
    private LocalGlForexLedgerHome glForexLedgerHome;
    @EJB
    private LocalAdUserHome adUserHome;


    @Override
    public Integer saveCmAdjEntry(CmAdjustmentDetails details, String BA_NM, String CST_NM, String SO_NMBR, String FC_NM, boolean isDraft, Integer PYRLL_PRD, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyApprovedException, GlobalConversionDateNotExistException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalRecordAlreadyAssignedException, GlobalDocumentNumberNotUniqueException, GlobalBranchAccountNumberInvalidException {

        Debug.print("CmAdjustmentEntryApiControllerBean saveCmAdjEntry");

        LocalCmAdjustment cmAdjustment = null;
        LocalAdCompany adCompany = null;


        try {

            adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            // validate if adjustment is already deleted
            try {

                if (details.getAdjCode() != null) {

                    cmAdjustment = cmAdjustmentHome.findByPrimaryKey(details.getAdjCode());
                }

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if adjustment is already posted, void, arproved or pending , refund

            this.validateStatus(details, cmAdjustment);

            // adjustment void

            if (details.getAdjCode() != null && details.getAdjVoid() == EJBCommon.TRUE) {

                return this.executeCmAdjustmentVoid(details, isDraft, branchCode, companyCode, cmAdjustment);
            }

            // validate if document number is unique document number is automatic then set
            // next sequence
            // This method will generate document Number either in sequence or if doc number have value,
            // check if exist
            this.generateDocumentNumber(details, branchCode, companyCode, cmAdjustment);

            // validate if conversion date exists

            this.validateConversionDate(details, FC_NM, companyCode, adCompany);

            // used in checking if adjustment should re-generate distribution records


            if (details.getAdjCode() == null) {

                cmAdjustment = cmAdjustmentHome.create(details.getAdjType(), details.getAdjDate(), details.getAdjDocumentNumber(), details.getAdjReferenceNumber(), details.getAdjCheckNumber(), details.getAdjAmount(), 0d, details.getAdjConversionDate(), details.getAdjConversionRate(), details.getAdjMemo(), null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, 0d, null, EJBCommon.FALSE, null, null, EJBCommon.FALSE, details.getAdjCreatedBy(), details.getAdjDateCreated(), details.getAdjLastModifiedBy(), details.getAdjDateLastModified(), null, null, null, null, null, branchCode, companyCode);

            } else {


                // update adjustment

                cmAdjustment.setAdjType(details.getAdjType());
                cmAdjustment.setAdjDate(details.getAdjDate());
                cmAdjustment.setAdjDocumentNumber(details.getAdjDocumentNumber());
                cmAdjustment.setAdjReferenceNumber(details.getAdjReferenceNumber());
                cmAdjustment.setAdjCheckNumber(details.getAdjCheckNumber());
                cmAdjustment.setAdjAmount(details.getAdjAmount());
                cmAdjustment.setAdjAmountApplied(details.getAdjAmountApplied());
                cmAdjustment.setAdjConversionDate(details.getAdjConversionDate());
                cmAdjustment.setAdjConversionRate(details.getAdjConversionRate());
                cmAdjustment.setAdjMemo(details.getAdjMemo());
                cmAdjustment.setAdjLastModifiedBy(details.getAdjLastModifiedBy());
                cmAdjustment.setAdjDateLastModified(details.getAdjDateLastModified());
                cmAdjustment.setAdjReasonForRejection(null);
            }

            boolean isBaDefault = false;
            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaNameAndBrCode(BA_NM, branchCode, companyCode);

            cmAdjustment.setAdBankAccount(adBankAccount);


            // Create Distribution records
            this.generateCmAdjustmentDistributionRecords(CST_NM, SO_NMBR, branchCode, companyCode, cmAdjustment);


            // generate approval status

            String ADJ_APPRVL_STATUS = null;

            if (!isDraft) {

                ADJ_APPRVL_STATUS = this.generateApprovalStatus(details, branchCode, companyCode, cmAdjustment);
            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            if (ADJ_APPRVL_STATUS != null && ADJ_APPRVL_STATUS.equals("N/A") && adPreference.getPrfCmGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                // Execute if posted
                this.executeCmAdjPost(cmAdjustment.getAdjCode(), cmAdjustment.getAdjLastModifiedBy(), branchCode, companyCode);
            }

            // set adjustment approval status
            cmAdjustment.setAdjApprovalStatus(ADJ_APPRVL_STATUS);

            return cmAdjustment.getAdjCode();

        } catch (GlobalDocumentNumberNotUniqueException | GlobalBranchAccountNumberInvalidException |
                 GlobalJournalNotBalanceException | GlobalRecordAlreadyAssignedException |
                 GlJREffectiveDatePeriodClosedException | GlJREffectiveDateNoPeriodExistException |
                 GlobalNoApprovalApproverFoundException | GlobalNoApprovalRequesterFoundException |
                 GlobalTransactionAlreadyVoidException | GlobalTransactionAlreadyPostedException |
                 GlobalTransactionAlreadyPendingException | GlobalTransactionAlreadyApprovedException |
                 GlobalConversionDateNotExistException | GlobalRecordAlreadyDeletedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }


    private void generateCmAdjustmentDistributionRecords(String CST_NM, String SO_NMBR, Integer branchCode, Integer companyCode, LocalCmAdjustment cmAdjustment) throws RemoveException, GlobalBranchAccountNumberInvalidException, FinderException {
        // remove all distribution records

        Collection cmDistributionRecords = cmAdjustment.getCmDistributionRecords();

        LocalAdBankAccount adBankAccount = cmAdjustment.getAdBankAccount();
        LocalAdBranchBankAccount adBranchBankAccount = null;


        Iterator i = cmDistributionRecords.iterator();

        while (i.hasNext()) {

            LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) i.next();
            i.remove();
            em.remove(cmDistributionRecord);
        }


        boolean defaultBaCoa = false;


        try {
            adBranchBankAccount = adBranchBankAccountHome.findBbaByBaCodeAndBrCode(adBankAccount.getBaCode(), branchCode, companyCode);
        } catch (FinderException ex) {
            defaultBaCoa = true;
        }

        // add new distribution record

        Integer coaCashAccount = defaultBaCoa ? adBankAccount.getBaIsCashAccount() : adBranchBankAccount.getBbaGlCoaCashAccount();
        Integer coaAdjustmentAccount = defaultBaCoa ? adBankAccount.getBaCoaGlAdjustmentAccount() : adBranchBankAccount.getBbaGlCoaAdjustmentAccount();
        Integer coaInterestAccount = defaultBaCoa ? adBankAccount.getBaCoaGlInterestAccount() : adBranchBankAccount.getBbaGlCoaInterestAccount();
        Integer coaBankChargeAccount = defaultBaCoa ? adBankAccount.getBaCoaGlBankChargeAccount() : adBranchBankAccount.getBbaGlCoaBankChargeAccount();
        Integer coaAdvanceAccount = defaultBaCoa ? adBankAccount.getBaCoaGlAdvanceAccount() : adBranchBankAccount.getBbaGlCoaAdvanceAccount();

        switch (cmAdjustment.getAdjType()) {
            case "DEBIT MEMO":

                this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "ADJUSTMENT", EJBCommon.TRUE, cmAdjustment.getAdjAmount(), coaAdjustmentAccount, cmAdjustment, branchCode, companyCode);

                this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "CASH", EJBCommon.FALSE, cmAdjustment.getAdjAmount(), coaCashAccount, cmAdjustment, branchCode, companyCode);

                break;
            case "CREDIT MEMO":


                this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "CASH", EJBCommon.TRUE, cmAdjustment.getAdjAmount(), coaCashAccount, cmAdjustment, branchCode, companyCode);

                this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "ADJUSTMENT", EJBCommon.FALSE, cmAdjustment.getAdjAmount(), coaAdjustmentAccount, cmAdjustment, branchCode, companyCode);

                break;
            case "INTEREST":
                Debug.print("flag 4.3 ");
                this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "CASH", EJBCommon.TRUE, cmAdjustment.getAdjAmount(), coaCashAccount, cmAdjustment, branchCode, companyCode);

                this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "INTEREST", EJBCommon.FALSE, cmAdjustment.getAdjAmount(), coaInterestAccount, cmAdjustment, branchCode, companyCode);

                break;
            case "BANK CHARGE":
                Debug.print("flag 4.4 ");
                this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "BANK CHARGE", EJBCommon.TRUE, cmAdjustment.getAdjAmount(), coaBankChargeAccount, cmAdjustment, branchCode, companyCode);

                this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "CASH", EJBCommon.FALSE, cmAdjustment.getAdjAmount(), coaCashAccount, cmAdjustment, branchCode, companyCode);

                break;
            case "ADVANCE":

                LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(CST_NM, companyCode);
                arCustomer.addCmAdjustment(cmAdjustment);

                this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "ADVANCE", EJBCommon.FALSE, cmAdjustment.getAdjAmount(), coaAdvanceAccount, cmAdjustment, branchCode, companyCode);

                this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "CASH", EJBCommon.TRUE, cmAdjustment.getAdjAmount(), coaCashAccount, cmAdjustment, branchCode, companyCode);


                break;
            case "SO ADVANCE":
                Debug.print("flag 4.5 ");
                this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "ADVANCE", EJBCommon.FALSE, cmAdjustment.getAdjAmount(), coaAdvanceAccount, cmAdjustment, branchCode, companyCode);

                this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "CASH", EJBCommon.TRUE, cmAdjustment.getAdjAmount(), coaCashAccount, cmAdjustment, branchCode, companyCode);

                LocalArSalesOrder arSalesOrder = arSalesOrderHome.findBySoDocumentNumberAndBrCode(SO_NMBR, branchCode, companyCode);

                cmAdjustment.setArSalesOrder(arSalesOrder);
                break;
        }
    }


    private void validateConversionDate(CmAdjustmentDetails details, String FC_NM, Integer companyCode, LocalAdCompany adCompany) throws GlobalConversionDateNotExistException {
        try {

            if (details.getAdjConversionDate() != null) {


                LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, companyCode);

                if (!glValidateFunctionalCurrency.getFcName().equals("USD")) {

                    LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(), details.getAdjConversionDate(), companyCode);

                } else if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                    LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), details.getAdjConversionDate(), companyCode);
                }
            }

        } catch (FinderException ex) {

            throw new GlobalConversionDateNotExistException();
        }
    }


    private void generateDocumentNumber(CmAdjustmentDetails details, Integer branchCode, Integer companyCode, LocalCmAdjustment cmAdjustment) throws GlobalDocumentNumberNotUniqueException {
        if (details.getAdjCode() == null) {

            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

            try {

                adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("CM ADJUSTMENT", companyCode);

            } catch (FinderException ex) {

            }

            try {

                adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode);

            } catch (FinderException ex) {

            }

            LocalCmAdjustment cmExistingAdjustment = null;

            try {

                cmExistingAdjustment = cmAdjustmentHome.findByAdjDocumentNumberAndBrCode(details.getAdjDocumentNumber(), branchCode, companyCode);

            } catch (FinderException ex) {
            }

            if (cmExistingAdjustment != null) {

                throw new GlobalDocumentNumberNotUniqueException();
            }

            if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (details.getAdjDocumentNumber() == null || details.getAdjDocumentNumber().trim().length() == 0)) {

                while (true) {

                    if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                        try {

                            cmAdjustmentHome.findByAdjDocumentNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), branchCode, companyCode);
                            adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                        } catch (FinderException ex) {

                            details.setAdjDocumentNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                            adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                            break;
                        }

                    } else {

                        try {

                            cmAdjustmentHome.findByAdjDocumentNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), branchCode, companyCode);
                            adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                        } catch (FinderException ex) {

                            details.setAdjDocumentNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                            adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                            break;
                        }
                    }
                }
            }

        } else {

            LocalCmAdjustment cmExistingAdjustment = null;

            try {

                cmExistingAdjustment = cmAdjustmentHome.findByAdjDocumentNumberAndBrCode(details.getAdjDocumentNumber(), branchCode, companyCode);

            } catch (FinderException ex) {

            }

            if (cmExistingAdjustment != null && !cmExistingAdjustment.getAdjCode().equals(details.getAdjCode())) {

                throw new GlobalDocumentNumberNotUniqueException();
            }

            if (cmAdjustment.getAdjDocumentNumber() != details.getAdjDocumentNumber() && (details.getAdjDocumentNumber() == null || details.getAdjDocumentNumber().trim().length() == 0)) {

                details.setAdjDocumentNumber(cmAdjustment.getAdjDocumentNumber());
            }
        }
    }


    private Integer executeCmAdjustmentVoid(CmAdjustmentDetails details, boolean isDraft, Integer branchCode, Integer companyCode, LocalCmAdjustment cmAdjustment) throws GlobalTransactionAlreadyVoidException, GlobalRecordAlreadyAssignedException, FinderException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, CreateException, GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException {
        if (cmAdjustment.getAdjVoid() == EJBCommon.TRUE) {

            throw new GlobalTransactionAlreadyVoidException();
        }

        // check if adjusment is already used


        if (!cmAdjustment.getArAppliedCredits().isEmpty()) {

            throw new GlobalRecordAlreadyAssignedException();
        }

        if (cmAdjustment.getAdjPosted() == EJBCommon.TRUE) {

            // generate approval status

            String ADJ_APPRVL_STATUS = null;

            if (!isDraft) {
                // check if adjustment approval is enabled
                ADJ_APPRVL_STATUS = generateApprovalStatus(details, branchCode, companyCode, cmAdjustment);
            }

            // reverse distribution records

            Collection cmDistributionRecords = cmAdjustment.getCmDistributionRecords();
            ArrayList list = new ArrayList();

            Iterator i = cmDistributionRecords.iterator();

            while (i.hasNext()) {

                LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) i.next();

                list.add(cmDistributionRecord);
            }

            i = list.iterator();

            while (i.hasNext()) {

                LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) i.next();
                Debug.print("Check Point A");
                try {
                    this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), cmDistributionRecord.getDrClass(), cmDistributionRecord.getDrDebit() == EJBCommon.TRUE ? EJBCommon.FALSE : EJBCommon.TRUE, cmDistributionRecord.getDrAmount(), cmDistributionRecord.getGlChartOfAccount().getCoaCode(), cmAdjustment, branchCode, companyCode);
                } catch (GlobalBranchAccountNumberInvalidException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            if (ADJ_APPRVL_STATUS != null && ADJ_APPRVL_STATUS.equals("N/A") && adPreference.getPrfCmGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                cmAdjustment.setAdjVoid(EJBCommon.TRUE);
                this.executeCmAdjPost(cmAdjustment.getAdjCode(), details.getAdjLastModifiedBy(), branchCode, companyCode);
            }

            // set void approval status

            cmAdjustment.setAdjVoidApprovalStatus(ADJ_APPRVL_STATUS);
        }

        cmAdjustment.setAdjVoid(EJBCommon.TRUE);
        cmAdjustment.setAdjLastModifiedBy(details.getAdjLastModifiedBy());
        cmAdjustment.setAdjDateLastModified(details.getAdjDateLastModified());

        return cmAdjustment.getAdjCode();
    }


    private String generateApprovalStatus(CmAdjustmentDetails details, Integer branchCode, Integer companyCode, LocalCmAdjustment cmAdjustment) throws FinderException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, CreateException {
        String ADJ_APPRVL_STATUS;
        LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);

        // check if adjustment approval is enabled

        if (adApproval.getAprEnableCmAdjustment() == EJBCommon.FALSE) {

            ADJ_APPRVL_STATUS = "N/A";

        } else {

            // check if adjustment is self approved

            LocalAdAmountLimit adAmountLimit = null;

            try {

                adAmountLimit = adAmountLimitHome.findByAdcTypeAndAuTypeAndUsrName("CM ADJUSTMENT", "REQUESTER", details.getAdjLastModifiedBy(), companyCode);

            } catch (FinderException ex) {

                throw new GlobalNoApprovalRequesterFoundException();
            }

            if (cmAdjustment.getAdjAmount() <= adAmountLimit.getCalAmountLimit()) {

                ADJ_APPRVL_STATUS = "N/A";

            } else {

                // for approval, create approval queue

                Collection adAmountLimits = adAmountLimitHome.findByAdcTypeAndGreaterThanCalAmountLimit("CM ADJUSTMENT", adAmountLimit.getCalAmountLimit(), companyCode);

                if (adAmountLimits.isEmpty()) {

                    Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adAmountLimit.getCalCode(), companyCode);

                    if (adApprovalUsers.isEmpty()) {

                        throw new GlobalNoApprovalApproverFoundException();
                    }

                    for (Object approvalUser : adApprovalUsers) {

                        LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                        LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(branchCode, companyCode, adApprovalQueueHome, cmAdjustment, adAmountLimit, adApprovalUser);

                        adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                    }

                } else {

                    boolean isApprovalUsersFound = false;

                    Iterator n = adAmountLimits.iterator();

                    while (n.hasNext()) {

                        LocalAdAmountLimit adNextAmountLimit = (LocalAdAmountLimit) n.next();

                        if (cmAdjustment.getAdjAmount() <= adNextAmountLimit.getCalAmountLimit()) {

                            Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adAmountLimit.getCalCode(), companyCode);

                            for (Object approvalUser : adApprovalUsers) {

                                isApprovalUsersFound = true;

                                LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(branchCode, companyCode, adApprovalQueueHome, cmAdjustment, adAmountLimit, adApprovalUser);

                                adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                            }

                            break;

                        } else if (!n.hasNext()) {

                            Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adNextAmountLimit.getCalCode(), companyCode);

                            for (Object approvalUser : adApprovalUsers) {

                                isApprovalUsersFound = true;

                                LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(branchCode, companyCode, adApprovalQueueHome, cmAdjustment, adNextAmountLimit, adApprovalUser);

                                adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                            }

                            break;
                        }

                        adAmountLimit = adNextAmountLimit;
                    }

                    if (!isApprovalUsersFound) {

                        throw new GlobalNoApprovalApproverFoundException();
                    }
                }

                ADJ_APPRVL_STATUS = "PENDING";
            }
        }
        return ADJ_APPRVL_STATUS;
    }


    private void validateStatus(CmAdjustmentDetails details, LocalCmAdjustment cmAdjustment) throws GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException {
        if (details.getAdjCode() != null && details.getAdjVoid() == EJBCommon.FALSE && details.getAdjRefund() == EJBCommon.FALSE) {

            if (cmAdjustment.getAdjApprovalStatus() != null) {

                if (cmAdjustment.getAdjApprovalStatus().equals("APPROVED") || cmAdjustment.getAdjApprovalStatus().equals("N/A")) {

                    throw new GlobalTransactionAlreadyApprovedException();

                } else if (cmAdjustment.getAdjApprovalStatus().equals("PENDING")) {

                    throw new GlobalTransactionAlreadyPendingException();
                }
            }

            if (cmAdjustment.getAdjPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();

            } else if (cmAdjustment.getAdjVoid() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyVoidException();

            } else if (cmAdjustment.getAdjRefund() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyVoidException();
            }
        }
    }

    // private methods

    private void addCmDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE, LocalCmAdjustment cmAdjustment, Integer branchCode, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArInvoiceEntryControllerBean addCmDrEntry");


        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, branchCode, companyCode);

            // create distribution record

            LocalCmDistributionRecord cmDistributionRecord = cmDistributionRecordHome.create(DR_LN, DR_CLSS, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), DR_DBT, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);

            cmAdjustment.addCmDistributionRecord(cmDistributionRecord);
            glChartOfAccount.addCmDistributionRecord(cmDistributionRecord);
        } catch (FinderException ex) {

            throw new GlobalBranchAccountNumberInvalidException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }


    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer companyCode) {

        Debug.print("CmAdjustmentEntryControllerBean convertForeignToFunctionalCurrency");


        LocalAdCompany adCompany = null;


        // get company and extended precision

        try {

            adCompany = adCompanyHome.findByPrimaryKey(companyCode);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // Convert to functional currency if necessary

        if (CONVERSION_RATE != 1 && CONVERSION_RATE != 0) {

            AMOUNT = AMOUNT / CONVERSION_RATE;
        }

        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }


    private void executeCmAdjPost(Integer ADJ_CODE, String USR_NM, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException {

        Debug.print("CmAdjustmentEntryControllerBean executeCmAdjPost");


        LocalCmAdjustment cmAdjustment = null;


        try {

            // validate if adjustment is already deleted

            try {

                cmAdjustment = cmAdjustmentHome.findByPrimaryKey(ADJ_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if receipt is already posted
            Debug.print("cmAdjustment.getAdjVoid()=" + cmAdjustment.getAdjVoid());
            Debug.print("cmAdjustment.getAdjPosted()=" + cmAdjustment.getAdjPosted());

            if (cmAdjustment.getAdjVoid() == EJBCommon.FALSE && cmAdjustment.getAdjPosted() == EJBCommon.TRUE) {
                Debug.print("------------------------------>");
                throw new GlobalTransactionAlreadyPostedException();

                // validate if receipt void is already posted

            } else if (cmAdjustment.getAdjVoid() == EJBCommon.TRUE && cmAdjustment.getAdjVoidPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyVoidPostedException();
            }

            // post adjustment

            if (cmAdjustment.getAdjVoid() == EJBCommon.FALSE && cmAdjustment.getAdjPosted() == EJBCommon.FALSE) {

                if (cmAdjustment.getAdjType().equals("INTEREST") || cmAdjustment.getAdjType().equals("DEBIT MEMO") || cmAdjustment.getAdjType().equals("ADVANCE")) {

                    // increase bank account balances

                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(cmAdjustment.getAdBankAccount().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(cmAdjustment.getAdjDate(), cmAdjustment.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(cmAdjustment.getAdjDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(cmAdjustment.getAdjDate(), adBankAccountBalance.getBabBalance() + cmAdjustment.getAdjAmount(), "BOOK", companyCode);

                                adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);

                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + cmAdjustment.getAdjAmount());
                            }

                        } else {

                            // create new balance

                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(cmAdjustment.getAdjDate(), (cmAdjustment.getAdjAmount()), "BOOK", companyCode);

                            adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(cmAdjustment.getAdjDate(), cmAdjustment.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + cmAdjustment.getAdjAmount());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }

                } else {

                    // decrease bank account balances

                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(cmAdjustment.getAdBankAccount().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(cmAdjustment.getAdjDate(), cmAdjustment.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(cmAdjustment.getAdjDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(cmAdjustment.getAdjDate(), adBankAccountBalance.getBabBalance() - cmAdjustment.getAdjAmount(), "BOOK", companyCode);

                                adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);

                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - cmAdjustment.getAdjAmount());
                            }

                        } else {

                            // create new balance

                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(cmAdjustment.getAdjDate(), (0 - cmAdjustment.getAdjAmount()), "BOOK", companyCode);

                            adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(cmAdjustment.getAdjDate(), cmAdjustment.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - cmAdjustment.getAdjAmount());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }
                cmAdjustment.setAdjPosted(EJBCommon.TRUE);
                cmAdjustment.setAdjPostedBy(USR_NM);
                cmAdjustment.setAdjDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            } else if (cmAdjustment.getAdjVoid() == EJBCommon.TRUE && cmAdjustment.getAdjVoidPosted() == EJBCommon.FALSE) { // void receipt

                if (cmAdjustment.getAdjType().equals("INTEREST") || cmAdjustment.getAdjType().equals("DEBIT MEMO") || cmAdjustment.getAdjType().equals("ADVANCE")) {

                    // decrease bank account balances

                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(cmAdjustment.getAdBankAccount().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(cmAdjustment.getAdjDate(), cmAdjustment.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(cmAdjustment.getAdjDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(cmAdjustment.getAdjDate(), adBankAccountBalance.getBabBalance() - cmAdjustment.getAdjAmount(), "BOOK", companyCode);

                                adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);

                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - cmAdjustment.getAdjAmount());
                            }

                        } else {

                            // create new balance

                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(cmAdjustment.getAdjDate(), (0 - cmAdjustment.getAdjAmount()), "BOOK", companyCode);

                            adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(cmAdjustment.getAdjDate(), cmAdjustment.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - cmAdjustment.getAdjAmount());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }

                } else {

                    // increase bank account balances

                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(cmAdjustment.getAdBankAccount().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(cmAdjustment.getAdjDate(), cmAdjustment.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(cmAdjustment.getAdjDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(cmAdjustment.getAdjDate(), adBankAccountBalance.getBabBalance() + cmAdjustment.getAdjAmount(), "BOOK", companyCode);

                                adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);

                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + cmAdjustment.getAdjAmount());
                            }

                        } else {

                            // create new balance

                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(cmAdjustment.getAdjDate(), (cmAdjustment.getAdjAmount()), "BOOK", companyCode);

                            adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(cmAdjustment.getAdjDate(), cmAdjustment.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + cmAdjustment.getAdjAmount());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // set cmAdjustment post status

                cmAdjustment.setAdjVoidPosted(EJBCommon.TRUE);
                cmAdjustment.setAdjPostedBy(USR_NM);
                cmAdjustment.setAdjDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
            }

            // post to gl if necessary

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            if (adPreference.getPrfCmGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                // validate if date has no period and period is closed

                LocalGlSetOfBook glJournalSetOfBook = null;

                try {

                    glJournalSetOfBook = glSetOfBookHome.findByDate(cmAdjustment.getAdjDate(), companyCode);

                } catch (FinderException ex) {

                    throw new GlJREffectiveDateNoPeriodExistException();
                }

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), cmAdjustment.getAdjDate(), companyCode);

                if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P') {

                    throw new GlJREffectiveDatePeriodClosedException();
                }

                // check if invoice is balance if not check suspense posting

                LocalGlJournalLine glOffsetJournalLine = null;

                Collection cmDistributionRecords = cmDistributionRecordHome.findByDrReversalAndDrImportedAndAdjCode(EJBCommon.FALSE, EJBCommon.FALSE, cmAdjustment.getAdjCode(), companyCode);

                Iterator j = cmDistributionRecords.iterator();

                double TOTAL_DEBIT = 0d;
                double TOTAL_CREDIT = 0d;

                while (j.hasNext()) {

                    LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) j.next();

                    double DR_AMNT = this.convertForeignToFunctionalCurrency(cmAdjustment.getAdBankAccount().getGlFunctionalCurrency().getFcCode(), cmAdjustment.getAdBankAccount().getGlFunctionalCurrency().getFcName(), cmAdjustment.getAdjConversionDate(), cmAdjustment.getAdjConversionRate(), cmDistributionRecord.getDrAmount(), companyCode);

                    if (cmDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                        TOTAL_DEBIT += DR_AMNT;

                    } else {

                        TOTAL_CREDIT += DR_AMNT;
                    }
                }

                TOTAL_DEBIT = EJBCommon.roundIt(TOTAL_DEBIT, adCompany.getGlFunctionalCurrency().getFcPrecision());
                TOTAL_CREDIT = EJBCommon.roundIt(TOTAL_CREDIT, adCompany.getGlFunctionalCurrency().getFcPrecision());

                if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.TRUE && TOTAL_DEBIT != TOTAL_CREDIT) {

                    LocalGlSuspenseAccount glSuspenseAccount = null;

                    try {

                        glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("CASH MANAGEMENT", "BANK ADJUSTMENTS", companyCode);

                    } catch (FinderException ex) {

                        throw new GlobalJournalNotBalanceException();
                    }

                    if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                        glOffsetJournalLine = glJournalLineHome.create((short) (cmDistributionRecords.size() + 1), EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);

                    } else {

                        glOffsetJournalLine = glJournalLineHome.create((short) (cmDistributionRecords.size() + 1), EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
                    }

                    LocalGlChartOfAccount glChartOfAccount = glSuspenseAccount.getGlChartOfAccount();
                    glChartOfAccount.addGlJournalLine(glOffsetJournalLine);

                } else if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE && TOTAL_DEBIT != TOTAL_CREDIT) {

                    throw new GlobalJournalNotBalanceException();
                }

                // create journal batch if necessary

                LocalGlJournalBatch glJournalBatch = null;
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");

                try {

                    glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " BANK ADJUSTMENTS", branchCode, companyCode);

                } catch (FinderException ex) {
                }

                if (adPreference.getPrfEnableGlJournalBatch() == EJBCommon.TRUE && glJournalBatch == null) {

                    glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " BANK ADJUSTMENTS", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, branchCode, companyCode);
                }

                // create journal entry

                LocalGlJournal glJournal = glJournalHome.create(cmAdjustment.getAdjReferenceNumber(), cmAdjustment.getAdjMemo(), cmAdjustment.getAdjDate(), 0.0d, null, cmAdjustment.getAdjDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM, EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, EJBCommon.FALSE, null, branchCode, companyCode);

                LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("CASH MANAGEMENT", companyCode);
                glJournal.setGlJournalSource(glJournalSource);

                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

                LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("BANK ADJUSTMENTS", companyCode);
                glJournal.setGlJournalCategory(glJournalCategory);

                if (glJournalBatch != null) {

                    glJournal.setGlJournalBatch(glJournalBatch);
                }

                // create journal lines

                j = cmDistributionRecords.iterator();

                while (j.hasNext()) {

                    LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) j.next();

                    double DR_AMNT = this.convertForeignToFunctionalCurrency(cmAdjustment.getAdBankAccount().getGlFunctionalCurrency().getFcCode(), cmAdjustment.getAdBankAccount().getGlFunctionalCurrency().getFcName(), cmAdjustment.getAdjConversionDate(), cmAdjustment.getAdjConversionRate(), cmDistributionRecord.getDrAmount(), companyCode);

                    LocalGlJournalLine glJournalLine = glJournalLineHome.create(cmDistributionRecord.getDrLine(), cmDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

                    cmDistributionRecord.getGlChartOfAccount().addGlJournalLine(glJournalLine);

                    glJournal.addGlJournalLine(glJournalLine);

                    cmDistributionRecord.setDrImported(EJBCommon.TRUE);

                    // for FOREX revaluation
                    if ((!Objects.equals(cmAdjustment.getAdBankAccount().getGlFunctionalCurrency().getFcCode(), adCompany.getGlFunctionalCurrency().getFcCode())) && glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null && (glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode().equals(cmAdjustment.getAdBankAccount().getGlFunctionalCurrency().getFcCode()))) {

                        double CONVERSION_RATE = 1;

                        if (cmAdjustment.getAdjConversionRate() != 0 && cmAdjustment.getAdjConversionRate() != 1) {

                            CONVERSION_RATE = cmAdjustment.getAdjConversionRate();

                        } else if (cmAdjustment.getAdjConversionDate() != null) {

                            CONVERSION_RATE = this.getFrRateByFrNameAndFrDate(glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), companyCode);
                        }

                        Collection glForexLedgers = null;

                        try {

                            glForexLedgers = glForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(cmAdjustment.getAdjDate(), glJournalLine.getGlChartOfAccount().getCoaCode(), companyCode);

                        } catch (FinderException ex) {

                        }

                        LocalGlForexLedger glForexLedger = (glForexLedgers.isEmpty() || glForexLedgers == null) ? null : (LocalGlForexLedger) glForexLedgers.iterator().next();

                        int FRL_LN = (glForexLedger != null && glForexLedger.getFrlDate().compareTo(cmAdjustment.getAdjDate()) == 0) ? glForexLedger.getFrlLine() + 1 : 1;

                        // compute balance
                        double COA_FRX_BLNC = glForexLedger == null ? 0 : glForexLedger.getFrlBalance();
                        double FRL_AMNT = cmDistributionRecord.getDrAmount();

                        if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET"))
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                        else FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);

                        COA_FRX_BLNC = COA_FRX_BLNC + FRL_AMNT;

                        glForexLedger = glForexLedgerHome.create(cmAdjustment.getAdjDate(), FRL_LN, "OTH", FRL_AMNT, CONVERSION_RATE, COA_FRX_BLNC, 0d, companyCode);

                        glJournalLine.getGlChartOfAccount().addGlForexLedger(glForexLedger);

                        // propagate balances
                        try {

                            glForexLedgers = glForexLedgerHome.findByGreaterThanFrlDateAndCoaCode(glForexLedger.getFrlDate(), glForexLedger.getGlChartOfAccount().getCoaCode(), glForexLedger.getFrlAdCompany());

                        } catch (FinderException ex) {

                        }

                        for (Object forexLedger : glForexLedgers) {

                            glForexLedger = (LocalGlForexLedger) forexLedger;
                            FRL_AMNT = cmDistributionRecord.getDrAmount();

                            if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET"))
                                FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                            else FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);

                            glForexLedger.setFrlBalance(glForexLedger.getFrlBalance() + FRL_AMNT);
                        }
                    }
                }

                if (glOffsetJournalLine != null) {

                    glJournal.addGlJournalLine(glOffsetJournalLine);
                }

                // post journal to gl
                Collection glJournalLines = glJournalLineHome.findByJrCode(glJournal.getJrCode(), companyCode);
                for (Object journalLine : glJournalLines) {

                    LocalGlJournalLine glJournalLine = (LocalGlJournalLine) journalLine;

                    // post current to current acv

                    this.postToGl(glAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), true, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                    // post to subsequent acvs (propagate)

                    Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), glAccountingCalendarValue.getAcvPeriodNumber(), companyCode);

                    for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {

                        LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;

                        this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                    }

                    // post to subsequent years if necessary

                    Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode);

                    if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                        adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                        LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome.findByCoaAccountNumber(adCompany.getCmpRetainedEarnings(), companyCode);

                        for (Object subsequentSetOfBook : glSubsequentSetOfBooks) {

                            LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) subsequentSetOfBook;

                            String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                            // post to subsequent acvs of subsequent set of book(propagate)

                            Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                            for (Object accountingCalendarValue : glAccountingCalendarValues) {

                                LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                                if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY") || ACCOUNT_TYPE.equals("OWNERS EQUITY")) {

                                    this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                                } else { // revenue & expense

                                    this.postToGl(glSubsequentAccountingCalendarValue, glRetainedEarningsAccount, false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                                }
                            }

                            if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) break;
                        }
                    }
                }
            }

        } catch (GlJREffectiveDateNoPeriodExistException | GlobalTransactionAlreadyVoidPostedException |
                 GlobalTransactionAlreadyPostedException | GlobalRecordAlreadyDeletedException |
                 GlobalJournalNotBalanceException | GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }


    private double getFrRateByFrNameAndFrDate(String FC_NM, Date CONVERSION_DATE, Integer companyCode) throws GlobalConversionDateNotExistException {

        Debug.print("GlJournalEntryApiControllerBean getFrRateByFrNameAndFrDate");


        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, companyCode);

            double CONVERSION_RATE = 1;

            // Get functional currency rate

            if (!FC_NM.equals("USD")) {

                LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glFunctionalCurrency.getFcCode(), CONVERSION_DATE, companyCode);

                CONVERSION_RATE = glFunctionalCurrencyRate.getFrXToUsd();
            }

            // Get set of book functional currency rate if necessary

            if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), CONVERSION_DATE, companyCode);

                CONVERSION_RATE = CONVERSION_RATE / glCompanyFunctionalCurrencyRate.getFrXToUsd();
            }

            return CONVERSION_RATE;

        } catch (FinderException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlobalConversionDateNotExistException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }


    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double JL_AMNT, Integer companyCode) {

        Debug.print("CmAdjustmentEntryApiControllerBean postToGl");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccountBalance glChartOfAccountBalance = glChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(glAccountingCalendarValue.getAcvCode(), glChartOfAccount.getCoaCode(), companyCode);

            String ACCOUNT_TYPE = glChartOfAccount.getCoaAccountType();
            short FC_EXTNDD_PRCSN = adCompany.getGlFunctionalCurrency().getFcPrecision();

            if (((ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("EXPENSE")) && isDebit == EJBCommon.TRUE) || (!ACCOUNT_TYPE.equals("ASSET") && !ACCOUNT_TYPE.equals("EXPENSE") && isDebit == EJBCommon.FALSE)) {

                glChartOfAccountBalance.setCoabEndingBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabEndingBalance() + JL_AMNT, FC_EXTNDD_PRCSN));

                if (!isCurrentAcv) {

                    glChartOfAccountBalance.setCoabBeginningBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabBeginningBalance() + JL_AMNT, FC_EXTNDD_PRCSN));
                }

            } else {

                glChartOfAccountBalance.setCoabEndingBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabEndingBalance() - JL_AMNT, FC_EXTNDD_PRCSN));

                if (!isCurrentAcv) {

                    glChartOfAccountBalance.setCoabBeginningBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabBeginningBalance() - JL_AMNT, FC_EXTNDD_PRCSN));
                }
            }

            if (isCurrentAcv) {

                if (isDebit == EJBCommon.TRUE) {

                    glChartOfAccountBalance.setCoabTotalDebit(EJBCommon.roundIt(glChartOfAccountBalance.getCoabTotalDebit() + JL_AMNT, FC_EXTNDD_PRCSN));

                } else {

                    glChartOfAccountBalance.setCoabTotalCredit(EJBCommon.roundIt(glChartOfAccountBalance.getCoabTotalCredit() + JL_AMNT, FC_EXTNDD_PRCSN));
                }
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalAdApprovalQueue createApprovalQueue(Integer branchCode, Integer companyCode, LocalAdApprovalQueueHome adApprovalQueueHome, LocalCmAdjustment cmAdjustment, LocalAdAmountLimit adAmountLimit, LocalAdApprovalUser adApprovalUser) throws CreateException {

        return adApprovalQueueHome.AqForApproval(EJBCommon.TRUE).AqDocument("CM ADJUSTMENT").AqDocumentCode(cmAdjustment.getAdjCode()).AqDocumentNumber(cmAdjustment.getAdjDocumentNumber()).AqDate(cmAdjustment.getAdjDate()).AqAndOr(adAmountLimit.getCalAndOr()).AqUserOr(adApprovalUser.getAuOr()).AqAdBranch(branchCode).AqAdCompany(companyCode).buildApprovalQueue();
    }
}