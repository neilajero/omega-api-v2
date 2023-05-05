package com.ejb.txn.gl;

import java.util.*;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.entities.ad.LocalAdAmountLimit;
import com.ejb.dao.ad.LocalAdAmountLimitHome;
import com.ejb.entities.ad.LocalAdApproval;
import com.ejb.dao.ad.LocalAdApprovalCoaLineHome;
import com.ejb.dao.ad.LocalAdApprovalHome;
import com.ejb.entities.ad.LocalAdApprovalQueue;
import com.ejb.dao.ad.LocalAdApprovalQueueHome;
import com.ejb.entities.ad.LocalAdApprovalUser;
import com.ejb.dao.ad.LocalAdApprovalUserHome;
import com.ejb.entities.ad.LocalAdBranchDocumentSequenceAssignment;
import com.ejb.dao.ad.LocalAdBranchDocumentSequenceAssignmentHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.dao.ad.LocalAdDeleteAuditTrailHome;
import com.ejb.entities.ad.LocalAdDocumentSequenceAssignment;
import com.ejb.dao.ad.LocalAdDocumentSequenceAssignmentHome;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.exception.gl.GlJLChartOfAccountNotFoundException;
import com.ejb.exception.gl.GlJLChartOfAccountPermissionDeniedException;
import com.ejb.exception.gl.GlJRDateReversalNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.gl.GlJREffectiveDateViolationException;
import com.ejb.exception.global.GlobalConversionDateNotExistException;
import com.ejb.exception.global.GlobalDocumentNumberNotUniqueException;
import com.ejb.exception.global.GlobalNoApprovalApproverFoundException;
import com.ejb.exception.global.GlobalNoApprovalRequesterFoundException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalTransactionAlreadyApprovedException;
import com.ejb.exception.global.GlobalTransactionAlreadyPendingException;
import com.ejb.exception.global.GlobalTransactionAlreadyPostedException;
import com.ejb.entities.gen.LocalGenField;
import com.ejb.dao.gen.LocalGenSegmentHome;
import com.ejb.dao.gen.LocalGenValueSetValueHome;
import com.ejb.dao.gl.ILocalGlAccountingCalendarValueHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountBalanceHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.dao.gl.ILocalGlSetOfBookHome;
import com.ejb.dao.gl.ILocalGlTransactionCalendarValueHome;
import com.ejb.entities.gl.LocalGlAccountRange;
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
import com.ejb.entities.gl.LocalGlOrganization;
import com.ejb.entities.gl.LocalGlResponsibility;
import com.ejb.dao.gl.LocalGlResponsibilityHome;
import com.ejb.entities.gl.LocalGlSetOfBook;
import com.ejb.entities.gl.LocalGlSuspenseAccount;
import com.ejb.dao.gl.LocalGlSuspenseAccountHome;
import com.ejb.entities.gl.LocalGlTransactionCalendarValue;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.mod.gl.GlModFunctionalCurrencyDetails;
import com.util.mod.gl.GlModJournalDetails;
import com.util.mod.gl.GlModJournalLineDetails;
import com.util.gl.GlJournalDetails;

@Stateless(name = "GlJournalEntryControllerEJB")
public class GlJournalEntryControllerBean extends EJBContextClass implements GlJournalEntryController {

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
    private ILocalGlTransactionCalendarValueHome glTransactionCalendarValueHome;
    @EJB
    private LocalAdAmountLimitHome adAmountLimitHome;
    @EJB
    private LocalAdApprovalCoaLineHome adApprovalCoaLineHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalAdApprovalUserHome adApprovalUserHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdDeleteAuditTrailHome adDeleteAuditTrailHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalGenSegmentHome genSegmentHome;
    @EJB
    private LocalGenValueSetValueHome genValueSetValueHome;
    @EJB
    private LocalGlForexLedgerHome glForexLedgerHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalGlJournalBatchHome glJournalBatchHome;
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
    private LocalGlSuspenseAccountHome glSuspenseAccountHome;

    public ArrayList getGlFcAllWithDefault(Integer AD_CMPNY) {

        Debug.print("GlJournalEntryControllerBean getGlFcAllWithDefault");

        Collection glFunctionalCurrencies = null;

        LocalGlFunctionalCurrency glFunctionalCurrency = null;
        LocalAdCompany adCompany = null;

        ArrayList list = new ArrayList();

        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            glFunctionalCurrencies = glFunctionalCurrencyHome.findFcAllEnabled(EJBCommon.getGcCurrentDateWoTime().getTime(), AD_CMPNY);

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

    public ArrayList getGlJcAll(Integer AD_CMPNY) {

        Debug.print("GlJournalEntryControllerBean getGlJcAll");

        ArrayList list = new ArrayList();

        try {

            Collection glJournalCategories = glJournalCategoryHome.findJcAll(AD_CMPNY);

            for (Object journalCategory : glJournalCategories) {

                LocalGlJournalCategory glJournalCategory = (LocalGlJournalCategory) journalCategory;

                list.add(glJournalCategory.getJcName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getAdPrfGlJournalLineNumber(Integer AD_CMPNY) {

        Debug.print("GlJournalEntryControllerBean getAdPrfGlJournalLineNumber");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfGlJournalLineNumber();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public GlModJournalDetails getGlJrByJrCode(Integer JR_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlJournalEntryControllerBean getGlJrByJrCode");

        try {

            LocalGlJournal glJournal = null;

            try {

                glJournal = glJournalHome.findByPrimaryKey(JR_CODE);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList jrJlList = new ArrayList();

            // get journal lines

            Collection glJournalLines = glJournalLineHome.findByJrCode(glJournal.getJrCode(), AD_CMPNY);

            short lineNumber = 1;

            Iterator i = glJournalLines.iterator();

            double TOTAL_DEBIT = 0;
            double TOTAL_CREDIT = 0;

            while (i.hasNext()) {

                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) i.next();

                if (glJournalLine.getJlDebit() == EJBCommon.TRUE) {

                    TOTAL_DEBIT += this.convertForeignToFunctionalCurrency(glJournal.getGlFunctionalCurrency().getFcCode(), glJournal.getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), glJournal.getJrConversionRate(), glJournalLine.getJlAmount(), AD_CMPNY);

                } else {

                    TOTAL_CREDIT += this.convertForeignToFunctionalCurrency(glJournal.getGlFunctionalCurrency().getFcCode(), glJournal.getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), glJournal.getJrConversionRate(), glJournalLine.getJlAmount(), AD_CMPNY);
                }

                GlModJournalLineDetails mdetails = new GlModJournalLineDetails();

                mdetails.setJlCode(glJournalLine.getJlCode());
                mdetails.setJlLineNumber(lineNumber);
                mdetails.setJlDebit(glJournalLine.getJlDebit());
                mdetails.setJlAmount(glJournalLine.getJlAmount());
                mdetails.setJlCoaAccountNumber(glJournalLine.getGlChartOfAccount().getCoaAccountNumber());
                mdetails.setJlCoaAccountDescription(glJournalLine.getGlChartOfAccount().getCoaAccountDescription());
                mdetails.setJlDescription(glJournalLine.getJlDescription());

                jrJlList.add(mdetails);

                lineNumber++;
            }

            GlModJournalDetails mJrDetails = new GlModJournalDetails();

            mJrDetails.setJrCode(glJournal.getJrCode());
            mJrDetails.setJrName(glJournal.getJrName());
            mJrDetails.setJrDescription(glJournal.getJrDescription());
            mJrDetails.setJrEffectiveDate(glJournal.getJrEffectiveDate());
            mJrDetails.setJrDateReversal(glJournal.getJrDateReversal());
            mJrDetails.setJrDocumentNumber(glJournal.getJrDocumentNumber());
            mJrDetails.setJrConversionDate(glJournal.getJrConversionDate());
            mJrDetails.setJrConversionRate(glJournal.getJrConversionRate());
            mJrDetails.setJrApprovalStatus(glJournal.getJrApprovalStatus());
            mJrDetails.setJrReasonForRejection(glJournal.getJrReasonForRejection());
            mJrDetails.setJrPosted(glJournal.getJrPosted());
            mJrDetails.setJrReversed(glJournal.getJrReversed());
            mJrDetails.setJrCreatedBy(glJournal.getJrCreatedBy());
            mJrDetails.setJrDateCreated(glJournal.getJrDateCreated());
            mJrDetails.setJrLastModifiedBy(glJournal.getJrLastModifiedBy());
            mJrDetails.setJrDateLastModified(glJournal.getJrDateLastModified());
            mJrDetails.setJrApprovedRejectedBy(glJournal.getJrApprovedRejectedBy());
            mJrDetails.setJrDateApprovedRejected(glJournal.getJrDateApprovedRejected());
            mJrDetails.setJrPostedBy(glJournal.getJrPostedBy());
            mJrDetails.setJrDatePosted(glJournal.getJrDatePosted());
            mJrDetails.setJrJcName(glJournal.getGlJournalCategory().getJcName());
            mJrDetails.setJrFcName(glJournal.getGlFunctionalCurrency().getFcName());
            mJrDetails.setJrJsName(glJournal.getGlJournalSource().getJsName());
            mJrDetails.setJrTotalDebit(TOTAL_DEBIT);
            mJrDetails.setJrTotalCredit(TOTAL_CREDIT);
            mJrDetails.setJrFrozen(glJournal.getGlJournalSource().getJsFreezeJournal());
            mJrDetails.setJrJlList(jrJlList);
            mJrDetails.setJrJbName(glJournal.getGlJournalBatch() != null ? glJournal.getGlJournalBatch().getJbName() : null);

            return mJrDetails;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer saveGlJrEntry(GlJournalDetails details, String JC_NM, String FC_NM, Integer RES_CODE, String JS_NM, String JB_NM, ArrayList jlList, boolean isDraft, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlJREffectiveDateViolationException, GlobalDocumentNumberNotUniqueException, GlJRDateReversalNoPeriodExistException, GlobalConversionDateNotExistException, GlJLChartOfAccountNotFoundException, GlJLChartOfAccountPermissionDeniedException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalNoRecordFoundException {

        Debug.print("GlJournalEntryControllerBean saveGlJrEntry");

        LocalGlJournal glJournal = null;

        // validate if journal is already deleted

        try {

            if (details.getJrCode() != null) {

                glJournal = glJournalHome.findByPrimaryKey(details.getJrCode());
            }

        } catch (FinderException ex) {

            throw new GlobalRecordAlreadyDeletedException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }

        // validate if effective date has no period and period is closed

        try {

            LocalGlSetOfBook glJournalSetOfBook = glSetOfBookHome.findByDate(details.getJrEffectiveDate(), AD_CMPNY);
            LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), details.getJrEffectiveDate(), AD_CMPNY);

            if (!isDraft && (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P')) {

                throw new GlJREffectiveDatePeriodClosedException();
            }

        } catch (GlJREffectiveDatePeriodClosedException ex) {

            throw ex;

        } catch (FinderException ex) {

            throw new GlJREffectiveDateNoPeriodExistException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }

        // validate effective date violation

        try {

            LocalGlSetOfBook glJournalSetOfBook = glSetOfBookHome.findByDate(details.getJrEffectiveDate(), AD_CMPNY);

            LocalGlTransactionCalendarValue glTransactionCalendarValue = glTransactionCalendarValueHome.findByTcCodeAndTcvDate(glJournalSetOfBook.getGlTransactionCalendar().getTcCode(), details.getJrEffectiveDate(), AD_CMPNY);

            LocalGlJournalSource glValidateJournalSource = glJournalSourceHome.findByJsName(JS_NM, AD_CMPNY);

            if (glTransactionCalendarValue.getTcvBusinessDay() == EJBCommon.FALSE && glValidateJournalSource.getJsEffectiveDateRule() == 'F') {

                throw new GlJREffectiveDateViolationException();
            }

        } catch (GlJREffectiveDateViolationException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }

        // validate if document number is unique document number is automatic then set
        // next sequence

        try {

            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

            if (details.getJrCode() == null) {

                try {

                    adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("GL JOURNAL", AD_CMPNY);

                } catch (FinderException ex) {

                }

                try {

                    adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, AD_CMPNY);

                } catch (FinderException ex) {

                }

                LocalGlJournal glExistingJournal = null;

                try {

                    glExistingJournal = glJournalHome.findByJrDocumentNumberAndJsNameAndBrCode(details.getJrDocumentNumber(), "MANUAL", AD_BRNCH, AD_CMPNY);

                } catch (FinderException ex) {

                }

                if (glExistingJournal != null) {

                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (details.getJrDocumentNumber() == null || details.getJrDocumentNumber().trim().length() == 0)) {

                    while (true) {

                        if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                            try {

                                glJournalHome.findByJrDocumentNumberAndJsNameAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), "MANUAL", AD_BRNCH, AD_CMPNY);
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                            } catch (FinderException ex) {

                                details.setJrDocumentNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                break;
                            }

                        } else {

                            try {

                                glJournalHome.findByJrDocumentNumberAndJsNameAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), "MANUAL", AD_BRNCH, AD_CMPNY);
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                            } catch (FinderException ex) {

                                details.setJrDocumentNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                break;
                            }
                        }
                    }
                }

            } else {

                LocalGlJournal glExistingJournal = null;

                try {

                    glExistingJournal = glJournalHome.findByJrDocumentNumberAndJsNameAndBrCode(details.getJrDocumentNumber(), "MANUAL", AD_BRNCH, AD_CMPNY);

                } catch (FinderException ex) {

                }

                if (glExistingJournal != null && !glExistingJournal.getJrCode().equals(details.getJrCode())) {

                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (glJournal.getJrDocumentNumber() != details.getJrDocumentNumber() && (details.getJrDocumentNumber() == null || details.getJrDocumentNumber().trim().length() == 0)) {

                    details.setJrDocumentNumber(glJournal.getJrDocumentNumber());
                }
            }

        } catch (GlobalDocumentNumberNotUniqueException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

        // validate if date reversal has period

        try {

            if (details.getJrDateReversal() != null) {

                LocalGlSetOfBook glJrDateReversalSetOfBook = glSetOfBookHome.findByDate(details.getJrDateReversal(), AD_CMPNY);

                LocalGlAccountingCalendarValue glReversalPeriodAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJrDateReversalSetOfBook.getGlAccountingCalendar().getAcCode(), details.getJrDateReversal(), AD_CMPNY);
            }

        } catch (FinderException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlJRDateReversalNoPeriodExistException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

        // validate if conversion date exists

        try {

            if (details.getJrConversionDate() != null) {

                LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
                LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);

                if (!glValidateFunctionalCurrency.getFcName().equals("USD")) {

                    LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(), details.getJrConversionDate(), AD_CMPNY);

                } else if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                    LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), details.getJrConversionDate(), AD_CMPNY);
                }
            }

        } catch (FinderException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlobalConversionDateNotExistException();

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

        // validate if journal is already posted, approved or pending

        try {

            if (details.getJrCode() != null) {

                if (glJournal.getJrApprovalStatus() != null) {

                    if (glJournal.getJrApprovalStatus().equals("APPROVED") || glJournal.getJrApprovalStatus().equals("N/A")) {

                        throw new GlobalTransactionAlreadyApprovedException();

                    } else if (glJournal.getJrApprovalStatus().equals("PENDING")) {

                        throw new GlobalTransactionAlreadyPendingException();
                    }
                }

                if (glJournal.getJrPosted() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyPostedException();
                }
            }

        } catch (GlobalTransactionAlreadyApprovedException | GlobalTransactionAlreadyPostedException |
                 GlobalTransactionAlreadyPendingException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

        // create journal

        GlModJournalLineDetails mJlDetails = null;

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            if (details.getJrCode() == null) {

                glJournal = glJournalHome.create(details.getJrName(), details.getJrDescription(), details.getJrEffectiveDate(), 0.0d, details.getJrDateReversal(), details.getJrDocumentNumber(), details.getJrConversionDate(), details.getJrConversionRate(), null, null, 'N', EJBCommon.FALSE, EJBCommon.FALSE, details.getJrCreatedBy(), details.getJrDateCreated(), details.getJrLastModifiedBy(), details.getJrDateLastModified(), null, null, null, null, null, null, EJBCommon.FALSE, details.getJrReferenceNumber(), AD_BRNCH, AD_CMPNY);

            } else {

                glJournal.setJrName(details.getJrName());
                glJournal.setJrDescription(details.getJrDescription());
                glJournal.setJrEffectiveDate(details.getJrEffectiveDate());
                glJournal.setJrDateReversal(details.getJrDateReversal());
                glJournal.setJrDocumentNumber(details.getJrDocumentNumber());
                glJournal.setJrConversionDate(details.getJrConversionDate());
                glJournal.setJrConversionRate(details.getJrConversionRate());
                glJournal.setJrLastModifiedBy(details.getJrLastModifiedBy());
                glJournal.setJrDateLastModified(details.getJrDateLastModified());
                glJournal.setJrReasonForRejection(null);
            }

            LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName(JS_NM, AD_CMPNY);
            glJournal.setGlJournalSource(glJournalSource);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);
            glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName(JC_NM, AD_CMPNY);
            glJournal.setGlJournalCategory(glJournalCategory);

            try {

                LocalGlJournalBatch glJournalBatch = glJournalBatchHome.findByJbName(JB_NM, AD_BRNCH, AD_CMPNY);
                glJournal.setGlJournalBatch(glJournalBatch);

            } catch (FinderException ex) {

            }

            // remove all journal lines

            Collection glJournalLines = glJournal.getGlJournalLines();

            Iterator i = glJournalLines.iterator();

            while (i.hasNext()) {

                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) i.next();

                i.remove();

                // glJournalLine.entityRemove();
                em.remove(glJournalLine);
            }

            // add new journal lines

            double TOTAL_DEBIT = 0;
            double TOTAL_CREDIT = 0;
            short lineNumber = 0;

            i = jlList.iterator();

            while (i.hasNext()) {

                mJlDetails = (GlModJournalLineDetails) i.next();

                if (mJlDetails.getJlDebit() == 1) {

                    TOTAL_DEBIT += mJlDetails.getJlAmount();

                } else {

                    TOTAL_CREDIT += mJlDetails.getJlAmount();
                }

                TOTAL_DEBIT = EJBCommon.roundIt(TOTAL_DEBIT, this.getGlFcPrecisionUnit(AD_CMPNY));
                TOTAL_CREDIT = EJBCommon.roundIt(TOTAL_CREDIT, this.getGlFcPrecisionUnit(AD_CMPNY));

                lineNumber = mJlDetails.getJlLineNumber();

                this.addGlJlEntry(mJlDetails, glJournal, RES_CODE, AD_BRNCH, AD_CMPNY);
            }

            if (!(adPreference.getPrfAllowSuspensePosting() == 1) && TOTAL_DEBIT != TOTAL_CREDIT) {

                try {

                    LocalGlSuspenseAccount glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName(JS_NM, JC_NM, AD_CMPNY);
                    String coaAccountNumber = glSuspenseAccount.getGlChartOfAccount().getCoaAccountNumber();

                    GlModJournalLineDetails jlModDetails = new GlModJournalLineDetails();

                    jlModDetails.setJlLineNumber(++lineNumber);
                    jlModDetails.setJlCoaAccountNumber(coaAccountNumber);

                    double AMOUNT = 0;

                    if (TOTAL_DEBIT < TOTAL_CREDIT) {

                        AMOUNT = TOTAL_CREDIT - TOTAL_DEBIT;
                        jlModDetails.setJlDebit(EJBCommon.TRUE);
                        jlModDetails.setJlAmount(AMOUNT);
                        this.addGlJlEntry(jlModDetails, glJournal, RES_CODE, AD_BRNCH, AD_CMPNY);

                    } else if (TOTAL_DEBIT > TOTAL_CREDIT) {

                        AMOUNT = TOTAL_DEBIT - TOTAL_CREDIT;
                        jlModDetails.setJlDebit(EJBCommon.FALSE);
                        jlModDetails.setJlAmount(AMOUNT);
                        this.addGlJlEntry(jlModDetails, glJournal, RES_CODE, AD_BRNCH, AD_CMPNY);
                    }

                } catch (FinderException ex) {

                    throw new GlobalNoRecordFoundException();
                }
            }

            // generate approval status

            String JR_APPRVL_STATUS = null;

            if (!isDraft) {

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(AD_CMPNY);

                // check if gl journal approval is enabled

                if (adApproval.getAprEnableGlJournal() == EJBCommon.FALSE) {

                    JR_APPRVL_STATUS = "N/A";

                } else {

                    // get highest coa amount coa line

                    Integer HGHST_COA = null;
                    double HGHST_COA_AMNT = 0d;

                    Collection glHighestJournalLines = glJournal.getGlJournalLines();

                    for (Object glHighestJournalLine : glHighestJournalLines) {

                        LocalGlJournalLine glJournalLine = (LocalGlJournalLine) glHighestJournalLine;

                        Collection adApprovalCoaLines = glJournalLine.getGlChartOfAccount().getAdApprovalCoaLines();

                        if (!adApprovalCoaLines.isEmpty()) {

                            if (glJournalLine.getJlAmount() > HGHST_COA_AMNT) {

                                HGHST_COA = glJournalLine.getGlChartOfAccount().getCoaCode();
                                HGHST_COA_AMNT = glJournalLine.getJlAmount();
                            }
                        }
                    }

                    if (HGHST_COA == null) {

                        JR_APPRVL_STATUS = "N/A";

                    } else {

                        // check if journal is self approved

                        LocalAdAmountLimit adAmountLimit = null;

                        try {

                            adAmountLimit = adAmountLimitHome.findByCoaCodeAndAuTypeAndUsrName(HGHST_COA, "REQUESTER", details.getJrLastModifiedBy(), AD_CMPNY);

                        } catch (FinderException ex) {

                            throw new GlobalNoApprovalRequesterFoundException();
                        }

                        if (HGHST_COA_AMNT <= adAmountLimit.getCalAmountLimit()) {

                            JR_APPRVL_STATUS = "N/A";

                        } else {

                            // for approval, create approval queue

                            Collection adAmountLimits = adAmountLimitHome.findByCoaCodeAndGreaterThanCalAmountLimit(HGHST_COA, adAmountLimit.getCalAmountLimit(), AD_CMPNY);

                            if (adAmountLimits.isEmpty()) {

                                Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adAmountLimit.getCalCode(), AD_CMPNY);

                                if (adApprovalUsers.isEmpty()) {

                                    throw new GlobalNoApprovalApproverFoundException();
                                }

                                for (Object approvalUser : adApprovalUsers) {

                                    LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                    LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(AD_BRNCH, AD_CMPNY, adApprovalQueueHome, glJournal, adAmountLimit, adApprovalUser);

                                    adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                                }

                            } else {

                                boolean isApprovalUsersFound = false;

                                i = adAmountLimits.iterator();

                                while (i.hasNext()) {

                                    LocalAdAmountLimit adNextAmountLimit = (LocalAdAmountLimit) i.next();

                                    if (HGHST_COA_AMNT <= adNextAmountLimit.getCalAmountLimit()) {

                                        Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adAmountLimit.getCalCode(), AD_CMPNY);

                                        for (Object approvalUser : adApprovalUsers) {

                                            isApprovalUsersFound = true;

                                            LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                            LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(AD_BRNCH, AD_CMPNY, adApprovalQueueHome, glJournal, adAmountLimit, adApprovalUser);

                                            adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                                        }

                                        break;

                                    } else if (!i.hasNext()) {

                                        Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adNextAmountLimit.getCalCode(), AD_CMPNY);

                                        for (Object approvalUser : adApprovalUsers) {

                                            isApprovalUsersFound = true;

                                            LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                            LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(AD_BRNCH, AD_CMPNY, adApprovalQueueHome, glJournal, adNextAmountLimit, adApprovalUser);

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

                            JR_APPRVL_STATUS = "PENDING";
                        }
                    }
                }
            }

            if (JR_APPRVL_STATUS != null && JR_APPRVL_STATUS.equals("N/A") && adPreference.getPrfGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                this.executeGlJrPost(glJournal.getJrCode(), glJournal.getJrLastModifiedBy(), AD_CMPNY);
            }

            // set journal approval status

            glJournal.setJrApprovalStatus(JR_APPRVL_STATUS);

            return glJournal.getJrCode();

        } catch (GlJLChartOfAccountNotFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlJLChartOfAccountNotFoundException(String.valueOf(mJlDetails.getJlLineNumber()));

        } catch (GlJLChartOfAccountPermissionDeniedException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlJLChartOfAccountPermissionDeniedException(String.valueOf(mJlDetails.getJlLineNumber()));

        } catch (GlobalNoApprovalRequesterFoundException | GlobalNoRecordFoundException |
                 GlobalTransactionAlreadyPostedException | GlJREffectiveDatePeriodClosedException |
                 GlobalRecordAlreadyDeletedException | GlobalNoApprovalApproverFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteGlJrEntry(Integer JR_CODE, String AD_USR, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException {

        Debug.print("GlJournalEntryControllerBean deleteGlJrEntry");

        try {

            LocalGlJournal glJournal = glJournalHome.findByPrimaryKey(JR_CODE);

            double total = 0d;
            Collection glJournalLines = glJournal.getGlJournalLines();
            for (Object journalLine : glJournalLines) {
                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) journalLine;
                if (glJournalLine.getJlDebit() == EJBCommon.TRUE) {
                    total += glJournalLine.getJlAmount();
                }
            }

            adDeleteAuditTrailHome.create("GL JOURNAL", glJournal.getJrEffectiveDate(), glJournal.getJrDocumentNumber(), glJournal.getJrReferenceNumber(), total, AD_USR, new Date(), AD_CMPNY);

            // glJournal.entityRemove();
            em.remove(glJournal);

        } catch (FinderException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlobalRecordAlreadyDeletedException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("GlJournalEntryControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdApprovalNotifiedUsersByJrCode(Integer JR_CODE, Integer AD_CMPNY) {

        Debug.print("GlJournalEntryControllerBean getAdApprovalNotifiedUsersByJrCode");

        ArrayList list = new ArrayList();

        try {

            LocalGlJournal glJournal = glJournalHome.findByPrimaryKey(JR_CODE);

            if (glJournal.getJrPosted() == EJBCommon.TRUE) {

                list.add("DOCUMENT POSTED");
                return list;
            }

            Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode("GL JOURNAL", JR_CODE, AD_CMPNY);

            for (Object approvalQueue : adApprovalQueues) {

                LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) approvalQueue;

                list.add(adApprovalQueue.getAdUser().getUsrDescription());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfEnableGlJournalBatch(Integer AD_CMPNY) {

        Debug.print("GlJournalEntryControllerBean getAdPrfApEnableApVoucherBatch");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfEnableGlJournalBatch();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfEnableAllowSuspensePosting(Integer AD_CMPNY) {

        Debug.print("GlJournalEntryControllerBean getAdPrfEnableAllowSuspensePosting");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfAllowSuspensePosting();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGlOpenJbAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("GlJournalEntryControllerBean getGlOpenJbAll");

        ArrayList list = new ArrayList();

        try {

            Collection glJournalBatches = glJournalBatchHome.findOpenJbAll(AD_BRNCH, AD_CMPNY);

            for (Object journalBatch : glJournalBatches) {

                LocalGlJournalBatch glJournalBatch = (LocalGlJournalBatch) journalBatch;

                list.add(glJournalBatch.getJbName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public double getFrRateByFrNameAndFrDate(String FC_NM, Date CONVERSION_DATE, Integer AD_CMPNY) throws GlobalConversionDateNotExistException {

        Debug.print("GlJournalEntryControllerBean getFrRateByFrNameAndFrDate");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);

            double CONVERSION_RATE = 1;

            // Get functional currency rate

            if (!FC_NM.equals("USD")) {

                LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glFunctionalCurrency.getFcCode(), CONVERSION_DATE, AD_CMPNY);

                CONVERSION_RATE = glFunctionalCurrencyRate.getFrXToUsd();
            }

            // Get set of book functional currency rate if necessary

            if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), CONVERSION_DATE, AD_CMPNY);

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

    // private methods

    private void addGlJlEntry(GlModJournalLineDetails mdetails, LocalGlJournal glJournal, Integer RES_CODE, Integer AD_BRNCH, Integer AD_CMPNY) throws GlJLChartOfAccountNotFoundException, GlJLChartOfAccountPermissionDeniedException {

        Debug.print("GlJournalEntryControllerBean addGlJlEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            // validate if coa exists

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(mdetails.getJlCoaAccountNumber(), AD_BRNCH, AD_CMPNY);

                if (glChartOfAccount.getCoaEnable() == EJBCommon.FALSE) throw new GlJLChartOfAccountNotFoundException();

            } catch (FinderException ex) {

                throw new GlJLChartOfAccountNotFoundException();
            }

            // validate responsibility coa permission

            LocalGlResponsibility glResponsibility = null;

            try {

                glResponsibility = glResponsibilityHome.findByPrimaryKey(RES_CODE);

            } catch (FinderException ex) {

                throw new GlJLChartOfAccountPermissionDeniedException();
            }

            if (!this.isPermitted(glResponsibility.getGlOrganization(), glChartOfAccount, adCompany.getGenField(), AD_CMPNY)) {
                Debug.print("respnsibility problem");
                throw new GlJLChartOfAccountPermissionDeniedException();
            }

            // create journal

            LocalGlJournalLine glJournalLine = glJournalLineHome.create(mdetails.getJlLineNumber(), mdetails.getJlDebit(), EJBCommon.roundIt(mdetails.getJlAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()), mdetails.getJlDescription(), AD_CMPNY);

            glJournalLine.setGlJournal(glJournal);
            glJournalLine.setGlChartOfAccount(glChartOfAccount);

        } catch (GlJLChartOfAccountPermissionDeniedException | GlJLChartOfAccountNotFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

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
        } catch (Exception ex) {
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

            if (isOverlapped) return true;
        }

        return false;
    }

    private LocalAdApprovalQueue createApprovalQueue(Integer branchCode, Integer companyCode, LocalAdApprovalQueueHome adApprovalQueueHome, LocalGlJournal glJournal, LocalAdAmountLimit adAmountLimit, LocalAdApprovalUser adApprovalUser) throws CreateException {
        return adApprovalQueueHome.AqForApproval(EJBCommon.TRUE).AqDocument("GL JOURNAL").AqDocumentCode(glJournal.getJrCode()).AqDocumentNumber(glJournal.getJrDocumentNumber()).AqDate(glJournal.getJrEffectiveDate()).AqAndOr(adAmountLimit.getCalAndOr()).AqUserOr(adApprovalUser.getAuOr()).AqAdBranch(branchCode).AqAdCompany(companyCode).buildApprovalQueue();
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlJournalEntryControllerBean ejbCreate");
    }

    // private methods

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("GlJournalEntryControllerBean convertForeignToFunctionalCurrency");

        LocalAdCompany adCompany = null;

        // get company and extended precision

        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // Convert to functional currency if necessary

        if (CONVERSION_RATE != 1 && CONVERSION_RATE != 0) {

            AMOUNT = AMOUNT / CONVERSION_RATE;
        }
        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    private void executeGlJrPost(Integer JR_CODE, String USR_NM, Integer AD_CMPNY) throws GlobalTransactionAlreadyPostedException, GlJREffectiveDatePeriodClosedException, GlobalRecordAlreadyDeletedException {

        Debug.print("GlJournalEntryControllerBean executeGlJrPost");

        try {

            // get journal

            LocalGlJournal glJournal = null;

            try {

                glJournal = glJournalHome.findByPrimaryKey(JR_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if period is closed

            LocalGlSetOfBook glSetOfBook = glSetOfBookHome.findByDate(glJournal.getJrEffectiveDate(), AD_CMPNY);

            LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glSetOfBook.getGlAccountingCalendar().getAcCode(), glJournal.getJrEffectiveDate(), AD_CMPNY);

            if (glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P' || glAccountingCalendarValue.getAcvStatus() == 'N') {

                throw new GlJREffectiveDatePeriodClosedException(glJournal.getJrName());
            }

            // validate if journal is already posted

            if (glJournal.getJrPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException(glJournal.getJrName());
            }

            // post journal

            Collection glJournalLines = glJournal.getGlJournalLines();

            for (Object journalLine : glJournalLines) {

                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) journalLine;

                double JL_AMNT = this.convertForeignToFunctionalCurrency(glJournal.getGlFunctionalCurrency().getFcCode(), glJournal.getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), glJournal.getJrConversionRate(), glJournalLine.getJlAmount(), AD_CMPNY);

                // post current to current acv

                this.post(glAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), true, glJournalLine.getJlDebit(), JL_AMNT, AD_CMPNY);

                // post to subsequent acvs (propagate)

                Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(glSetOfBook.getGlAccountingCalendar().getAcCode(), glAccountingCalendarValue.getAcvPeriodNumber(), AD_CMPNY);

                for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {

                    LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;

                    this.post(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), JL_AMNT, AD_CMPNY);
                }

                // post to subsequent years if necessary

                Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(glSetOfBook.getGlAccountingCalendar().getAcYear(), AD_CMPNY);
                LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

                if (!glSubsequentSetOfBooks.isEmpty() && glSetOfBook.getSobYearEndClosed() == 1) {

                    LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome.findByCoaAccountNumber(adCompany.getCmpRetainedEarnings(), AD_CMPNY);

                    for (Object subsequentSetOfBook : glSubsequentSetOfBooks) {

                        LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) subsequentSetOfBook;

                        String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                        // post to subsequent acvs of subsequent set of book(propagate)

                        Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), AD_CMPNY);

                        for (Object accountingCalendarValue : glAccountingCalendarValues) {

                            LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                            if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY") || ACCOUNT_TYPE.equals("OWNERS EQUITY")) {

                                this.post(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), JL_AMNT, AD_CMPNY);

                            } else { // revenue & expense

                                this.post(glSubsequentAccountingCalendarValue, glRetainedEarningsAccount, false, glJournalLine.getJlDebit(), JL_AMNT, AD_CMPNY);
                            }
                        }

                        if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) break;
                    }
                }

                // for FOREX revaluation
                if ((!Objects.equals(glJournal.getGlFunctionalCurrency().getFcCode(), adCompany.getGlFunctionalCurrency().getFcCode())) && glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null && (glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode().equals(glJournal.getGlFunctionalCurrency().getFcCode()))) {

                    double CONVERSION_RATE = 1;

                    if (glJournal.getJrConversionRate() != 0 && glJournal.getJrConversionRate() != 1) {

                        CONVERSION_RATE = glJournal.getJrConversionRate();

                    } else if (glJournal.getJrConversionDate() != null) {

                        CONVERSION_RATE = this.getFrRateByFrNameAndFrDate(glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), AD_CMPNY);
                    }

                    Collection glForexLedgers = null;

                    try {

                        glForexLedgers = glForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(glJournal.getJrEffectiveDate(), glJournalLine.getGlChartOfAccount().getCoaCode(), AD_CMPNY);

                    } catch (FinderException ex) {

                    }

                    LocalGlForexLedger glForexLedger = (glForexLedgers.isEmpty() || glForexLedgers == null) ? null : (LocalGlForexLedger) glForexLedgers.iterator().next();

                    int FRL_LN = (glForexLedger != null && glForexLedger.getFrlDate().compareTo(glJournal.getJrEffectiveDate()) == 0) ? glForexLedger.getFrlLine() + 1 : 1;

                    // compute balance
                    double COA_FRX_BLNC = glForexLedger == null ? 0 : glForexLedger.getFrlBalance();
                    double FRL_AMNT = glJournalLine.getJlAmount();

                    if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET"))
                        FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                    else FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);

                    COA_FRX_BLNC = COA_FRX_BLNC + FRL_AMNT;

                    glForexLedger = glForexLedgerHome.create(glJournal.getJrEffectiveDate(), FRL_LN, "JOURNAL", FRL_AMNT, CONVERSION_RATE, COA_FRX_BLNC, 0, AD_CMPNY);

                    // glJournalLine.getGlChartOfAccount().addGlForexLedger(glForexLedger);
                    glForexLedger.setGlChartOfAccount(glJournalLine.getGlChartOfAccount());

                    // propagate balances
                    try {

                        glForexLedgers = glForexLedgerHome.findByGreaterThanFrlDateAndCoaCode(glForexLedger.getFrlDate(), glForexLedger.getGlChartOfAccount().getCoaCode(), glForexLedger.getFrlAdCompany());

                    } catch (FinderException ex) {

                    }

                    for (Object forexLedger : glForexLedgers) {

                        glForexLedger = (LocalGlForexLedger) forexLedger;
                        FRL_AMNT = glJournalLine.getJlAmount();

                        if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET"))
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                        else FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);

                        glForexLedger.setFrlBalance(glForexLedger.getFrlBalance() + FRL_AMNT);
                    }
                }
            }

            // set journal post status

            glJournal.setJrPosted(EJBCommon.TRUE);
            glJournal.setJrPostedBy(USR_NM);
            glJournal.setJrDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

        } catch (GlobalRecordAlreadyDeletedException | GlobalTransactionAlreadyPostedException |
                 GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void post(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double JL_AMNT, Integer AD_CMPNY) {

        Debug.print("GlJournalEntryControllerBean post");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            LocalGlChartOfAccountBalance glChartOfAccountBalance = glChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(glAccountingCalendarValue.getAcvCode(), glChartOfAccount.getCoaCode(), AD_CMPNY);

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
}