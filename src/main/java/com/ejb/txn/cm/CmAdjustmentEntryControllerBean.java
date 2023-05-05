/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class CmAdjustmentEntryControllerBean
 * @created November 19, 2003, 01:11 PM
 * @author Dennis M. Hilario
 */
package com.ejb.txn.cm;

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
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.dao.ad.LocalAdDeleteAuditTrailHome;
import com.ejb.entities.ad.LocalAdDocumentSequenceAssignment;
import com.ejb.dao.ad.LocalAdDocumentSequenceAssignmentHome;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.dao.ap.ILocalApSupplierHome;
import com.ejb.entities.ap.LocalApCheck;
import com.ejb.dao.ap.LocalApCheckHome;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.entities.ar.LocalArAppliedCredit;
import com.ejb.entities.ar.LocalArCustomer;
import com.ejb.dao.ar.LocalArCustomerHome;
import com.ejb.entities.ar.LocalArSalesOrder;
import com.ejb.dao.ar.LocalArSalesOrderHome;
import com.ejb.entities.ar.LocalArSalesperson;
import com.ejb.dao.ar.LocalArSalespersonHome;
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
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalReferenceNumberNotUniqueException;
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
import com.util.cm.CmAdjustmentDetails;
import com.util.mod.ad.AdModBankAccountDetails;
import com.util.mod.ar.ArModCustomerDetails;
import com.util.mod.ar.ArModSalesOrderDetails;
import com.util.mod.cm.CmModAdjustmentDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.ap.ApCheckDetails;

@Stateless(name = "CmAdjustmentEntryControllerEJB")
public class CmAdjustmentEntryControllerBean extends EJBContextClass implements CmAdjustmentEntryController {

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
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalCmAdjustmentHome cmAdjustmentHome;
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
    private LocalApCheckHome apCheckHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalArSalesOrderHome arSalesOrderHome;
    @EJB
    private LocalAdBranchBankAccountHome adBranchBankAccountHome;
    @EJB
    private LocalArSalespersonHome arSalespersonHome;
    @EJB
    private LocalAdDeleteAuditTrailHome adDeleteAuditTrailHome;
    @EJB
    LocalCmDistributionRecordHome cmDistributionRecordHome;
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


    public ArrayList getAdBaAll(Integer branchCode, Integer companyCode) {

        Debug.print("CmFundTransferEntryControllerBean getAdBaAll");

        ArrayList list = new ArrayList();

        try {

            Collection adBankAccounts = adBankAccountHome.findEnabledBaAll(branchCode, companyCode);

            for (Object bankAccount : adBankAccounts) {

                LocalAdBankAccount adBankAccount = (LocalAdBankAccount) bankAccount;

                list.add(adBankAccount.getBaName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public CmModAdjustmentDetails getCmAdjByAdjCode(Integer ADJ_CODE, Integer companyCode) {

        Debug.print("CmAdjustmentEntryControllerBean getCmAdjByAdjCode");

        try {

            LocalCmAdjustment cmAdjustment = cmAdjustmentHome.findByPrimaryKey(ADJ_CODE);

            CmModAdjustmentDetails mdetails = new CmModAdjustmentDetails();

            mdetails.setAdjCode(cmAdjustment.getAdjCode());
            mdetails.setAdjType(cmAdjustment.getAdjType());
            mdetails.setAdjDate(cmAdjustment.getAdjDate());

            if (cmAdjustment.getArCustomer() != null) {
                mdetails.setAdjCustomerCode(cmAdjustment.getArCustomer().getCstCustomerCode());
                mdetails.setAdjCustomerName(cmAdjustment.getArCustomer().getCstName());
            }

            mdetails.setAdjDocumentNumber(cmAdjustment.getAdjDocumentNumber());
            mdetails.setAdjReferenceNumber(cmAdjustment.getAdjReferenceNumber());
            mdetails.setAdjCheckNumber(cmAdjustment.getAdjCheckNumber());
            mdetails.setAdjAmount(cmAdjustment.getAdjAmount());

            if (cmAdjustment.getArSalesOrder() != null) {
                mdetails.setAdjSoNumber(cmAdjustment.getArSalesOrder().getSoDocumentNumber());
                mdetails.setAdjSoReferenceNumber(cmAdjustment.getAdjReferenceNumber());
            }

            // get applied credit

            Collection arAppliedCredits = cmAdjustment.getArAppliedCredits();

            Iterator x = arAppliedCredits.iterator();

            double totalAppliedCredit = 0d;

            while (x.hasNext()) {

                LocalArAppliedCredit arAppliedCredit = (LocalArAppliedCredit) x.next();

                totalAppliedCredit += arAppliedCredit.getAcApplyCredit();
            }

            mdetails.setAdjAmountApplied(totalAppliedCredit);
            mdetails.setAdjConversionDate(cmAdjustment.getAdjConversionDate());
            mdetails.setAdjConversionRate(cmAdjustment.getAdjConversionRate());
            mdetails.setAdjMemo(cmAdjustment.getAdjMemo());
            mdetails.setAdjVoid(cmAdjustment.getAdjVoid());

            mdetails.setAdjRefund(cmAdjustment.getAdjRefund());
            mdetails.setAdjRefundAmount(cmAdjustment.getAdjRefundAmount());
            mdetails.setAdjRefundReferenceNumber(cmAdjustment.getAdjRefundReferenceNumber());

            mdetails.setAdjApprovalStatus(cmAdjustment.getAdjApprovalStatus());
            mdetails.setAdjPosted(cmAdjustment.getAdjPosted());
            mdetails.setAdjVoidApprovalStatus(cmAdjustment.getAdjVoidApprovalStatus());
            mdetails.setAdjVoidPosted(cmAdjustment.getAdjVoidPosted());
            mdetails.setAdjCreatedBy(cmAdjustment.getAdjCreatedBy());
            mdetails.setAdjDateCreated(cmAdjustment.getAdjDateCreated());
            mdetails.setAdjLastModifiedBy(cmAdjustment.getAdjLastModifiedBy());
            mdetails.setAdjDateLastModified(cmAdjustment.getAdjDateLastModified());
            mdetails.setAdjApprovedRejectedBy(cmAdjustment.getAdjApprovedRejectedBy());
            mdetails.setAdjDateApprovedRejected(cmAdjustment.getAdjDateApprovedRejected());
            mdetails.setAdjPostedBy(cmAdjustment.getAdjPostedBy());
            mdetails.setAdjDatePosted(cmAdjustment.getAdjDatePosted());
            mdetails.setAdjBaName(cmAdjustment.getAdBankAccount().getBaName());
            mdetails.setAdjBaFcName(cmAdjustment.getAdBankAccount().getGlFunctionalCurrency().getFcName());
            mdetails.setAdjReasonForRejection(cmAdjustment.getAdjReasonForRejection());

            Collection cmAdjustments = cmAdjustment.getCmDistributionRecords();
            Iterator y = cmAdjustments.iterator();
            double totalDebit = 0;
            double totalCredit = 0;
            while (y.hasNext()) {
                LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) y.next();
                if (cmDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    totalDebit += cmDistributionRecord.getDrAmount();
                } else {
                    totalCredit += cmDistributionRecord.getDrAmount();
                }
            }


            mdetails.setAdjTotalDebit(totalDebit);
            mdetails.setAdjTotalCredit(totalCredit);

            return mdetails;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    public void checkCmAdjEntryUpload(CmAdjustmentDetails details, Integer branchCode, Integer companyCode) throws GlobalReferenceNumberNotUniqueException {

        Debug.print("CmAdjustmentEntryControllerBean checkCmAdjEntryUpload");


        try {

            Collection cmAdjustments = cmAdjustmentHome.findAllAdjByReferenceNumber(details.getAdjReferenceNumber(), branchCode, companyCode);

            if (cmAdjustments.size() > 0) {
                throw new GlobalReferenceNumberNotUniqueException();
            }


        } catch (GlobalReferenceNumberNotUniqueException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void saveCmAdjRefundDetails(ApCheckDetails details, Integer CHK_CODE, String ADJ_DCMNT_NMBR, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException {

        LocalCmAdjustment cmAdjustment = null;
        LocalApCheck apCheck = null;

        try {

            try {

                if (CHK_CODE != null) {

                    apCheck = apCheckHome.findByPrimaryKey(CHK_CODE);
                }

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if adjustment is already deleted

            try {

                if (ADJ_DCMNT_NMBR != null) {

                    cmAdjustment = cmAdjustmentHome.findByAdjDocumentNumberAndBrCode(ADJ_DCMNT_NMBR, branchCode, companyCode);
                }

                cmAdjustment.setAdjRefund(EJBCommon.TRUE);
                cmAdjustment.setAdjRefundAmount(apCheck.getChkAmount());
                cmAdjustment.setAdjRefundReferenceNumber(apCheck.getChkDocumentNumber());

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

        } catch (GlobalRecordAlreadyDeletedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;
        }
    }

    public void saveCmAdjRefundDetailsByAdjCode(ApCheckDetails details, Integer CHK_CODE, Integer ADJ_CODE, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException {

        LocalCmAdjustment cmAdjustment = null;
        LocalApCheck apCheck = null;

        try {

            try {

                if (CHK_CODE != null) {

                    apCheck = apCheckHome.findByPrimaryKey(CHK_CODE);
                }

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if adjustment is already deleted

            try {

                if (ADJ_CODE != null) {

                    cmAdjustment = cmAdjustmentHome.findByPrimaryKey(ADJ_CODE);
                }

                // get applied credit

                Collection arAppliedCredits = cmAdjustment.getArAppliedCredits();

                Iterator y = arAppliedCredits.iterator();

                double totalAppliedCredit = 0d;

                while (y.hasNext()) {

                    LocalArAppliedCredit arAppliedCredit = (LocalArAppliedCredit) y.next();

                    totalAppliedCredit += arAppliedCredit.getAcApplyCredit();
                }

                cmAdjustment.setAdjRefund(EJBCommon.TRUE);
                // cmAdjustment.setAdjRefundAmount(apCheck.getChkAmount());
                cmAdjustment.setAdjRefundAmount(cmAdjustment.getAdjAmount() - totalAppliedCredit - cmAdjustment.getAdjRefundAmount());
                cmAdjustment.setAdjRefundReferenceNumber(apCheck.getChkDocumentNumber());

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

        } catch (GlobalRecordAlreadyDeletedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;
        }
    }

    public Integer saveCmAdjEntry(CmAdjustmentDetails details, String BA_NM, String CST_NM, String SO_NMBR, String FC_NM, boolean isDraft, Integer PYRLL_PRD, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyApprovedException, GlobalConversionDateNotExistException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalRecordAlreadyAssignedException, GlobalDocumentNumberNotUniqueException, GlobalBranchAccountNumberInvalidException {

        Debug.print("CmAdjustmentEntryControllerBean saveCmAdjEntry");


        LocalCmAdjustment cmAdjustment = null;

        try {

            // validate if adjustment is already deleted

            try {

                if (details.getAdjCode() != null) {

                    cmAdjustment = cmAdjustmentHome.findByPrimaryKey(details.getAdjCode());
                }

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if adjustment is already posted, void, arproved or pending , refund

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

            // adjustment void

            if (details.getAdjCode() != null && details.getAdjVoid() == EJBCommon.TRUE) {

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

                                                LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(branchCode, companyCode, adApprovalQueueHome, cmAdjustment, adAmountLimit, adApprovalUser);

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

                        this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), cmDistributionRecord.getDrClass(), cmDistributionRecord.getDrDebit() == EJBCommon.TRUE ? EJBCommon.FALSE : EJBCommon.TRUE, cmDistributionRecord.getDrAmount(), cmDistributionRecord.getGlChartOfAccount().getCoaCode(), cmAdjustment, branchCode, companyCode);
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

            // validate if document number is unique document number is automatic then set
            // next sequence

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

            // validate if conversion date exists

            try {

                if (details.getAdjConversionDate() != null) {

                    LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
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

            // used in checking if adjustment should re-generate distribution records

            boolean isRecalculate = true;

            if (details.getAdjCode() == null) {

                cmAdjustment = cmAdjustmentHome.create(details.getAdjType(), details.getAdjDate(), details.getAdjDocumentNumber(), details.getAdjReferenceNumber(), details.getAdjCheckNumber(), details.getAdjAmount(), 0d, details.getAdjConversionDate(), details.getAdjConversionRate(), details.getAdjMemo(), null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, 0d, null, EJBCommon.FALSE, null, null, EJBCommon.FALSE, details.getAdjCreatedBy(), details.getAdjDateCreated(), details.getAdjLastModifiedBy(), details.getAdjDateLastModified(), null, null, null, null, null, branchCode, companyCode);

            } else {

                // check if critical fields are changed

                isRecalculate = !cmAdjustment.getAdjType().equals(details.getAdjType()) || cmAdjustment.getAdjAmount() != details.getAdjAmount() || !cmAdjustment.getAdBankAccount().getBaName().equals(BA_NM);

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


            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaNameAndBrCode(BA_NM, branchCode, companyCode);

            LocalAdBranchBankAccount adBranchBankAccount = null;
            Debug.print("ArInvoiceEntryControllerBean saveArInvIliEntry J");
            try {
                adBranchBankAccount = adBranchBankAccountHome.findBbaByBaCodeAndBrCode(adBankAccount.getBaCode(), branchCode, companyCode);

            } catch (FinderException ex) {

            }
            adBranchBankAccount.getAdBankAccount().addCmAdjustment(cmAdjustment);


            if (isRecalculate) {

                // remove all distribution records

                Collection cmDistributionRecords = cmAdjustment.getCmDistributionRecords();

                Iterator i = cmDistributionRecords.iterator();

                while (i.hasNext()) {

                    LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) i.next();

                    i.remove();

                    // cmDistributionRecord.entityRemove();
                    em.remove(cmDistributionRecord);
                }

                // add new distribution record

                switch (cmAdjustment.getAdjType()) {
                    case "DEBIT MEMO":


                        this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "ADJUSTMENT", EJBCommon.TRUE, cmAdjustment.getAdjAmount(), adBranchBankAccount.getBbaGlCoaAdjustmentAccount(), cmAdjustment, branchCode, companyCode);

                        this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "CASH", EJBCommon.FALSE, cmAdjustment.getAdjAmount(), adBranchBankAccount.getBbaGlCoaCashAccount(), cmAdjustment, branchCode, companyCode);

                        break;
                    case "CREDIT MEMO":


                        this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "CASH", EJBCommon.TRUE, cmAdjustment.getAdjAmount(), adBranchBankAccount.getBbaGlCoaCashAccount(), cmAdjustment, branchCode, companyCode);

                        this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "ADJUSTMENT", EJBCommon.FALSE, cmAdjustment.getAdjAmount(), adBranchBankAccount.getBbaGlCoaAdjustmentAccount(), cmAdjustment, branchCode, companyCode);

                        break;
                    case "INTEREST":

                        this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "CASH", EJBCommon.TRUE, cmAdjustment.getAdjAmount(), adBranchBankAccount.getBbaGlCoaCashAccount(), cmAdjustment, branchCode, companyCode);

                        this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "INTEREST", EJBCommon.FALSE, cmAdjustment.getAdjAmount(), adBranchBankAccount.getBbaGlCoaInterestAccount(), cmAdjustment, branchCode, companyCode);

                        break;
                    case "BANK CHARGE":

                        this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "BANK CHARGE", EJBCommon.TRUE, cmAdjustment.getAdjAmount(), adBranchBankAccount.getBbaGlCoaBankChargeAccount(), cmAdjustment, branchCode, companyCode);

                        this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "CASH", EJBCommon.FALSE, cmAdjustment.getAdjAmount(), adBranchBankAccount.getBbaGlCoaCashAccount(), cmAdjustment, branchCode, companyCode);

                        break;
                    case "ADVANCE":


                        this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "ADVANCE", EJBCommon.FALSE, cmAdjustment.getAdjAmount(), adBranchBankAccount.getBbaGlCoaAdvanceAccount(), cmAdjustment, branchCode, companyCode);

                        this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "CASH", EJBCommon.TRUE, cmAdjustment.getAdjAmount(), adBranchBankAccount.getBbaGlCoaCashAccount(), cmAdjustment, branchCode, companyCode);


                        LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(CST_NM, companyCode);

                        arCustomer.addCmAdjustment(cmAdjustment);

                        break;
                    case "SO ADVANCE":

                        this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "ADVANCE", EJBCommon.FALSE, cmAdjustment.getAdjAmount(), adBranchBankAccount.getBbaGlCoaAdvanceAccount(), cmAdjustment, branchCode, companyCode);

                        this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "CASH", EJBCommon.TRUE, cmAdjustment.getAdjAmount(), adBranchBankAccount.getBbaGlCoaCashAccount(), cmAdjustment, branchCode, companyCode);

                        LocalArSalesOrder arSalesOrder = arSalesOrderHome.findBySoDocumentNumberAndBrCode(SO_NMBR, branchCode, companyCode);

                        cmAdjustment.setArSalesOrder(arSalesOrder);
                        break;
                }
            }

            // generate approval status

            String ADJ_APPRVL_STATUS = null;

            if (!isDraft) {

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
            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            if (ADJ_APPRVL_STATUS != null && ADJ_APPRVL_STATUS.equals("N/A") && adPreference.getPrfCmGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

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

    public ArrayList getCmCstAll(Integer branchCode, Integer companyCode) {

        Debug.print("CmAdjustmentBean getCmCstAll");

        ArrayList list = new ArrayList();

        try {

            Collection arCustomers = arCustomerHome.findEnabledCstAll(branchCode, companyCode);

            for (Object customer : arCustomers) {

                LocalArCustomer arCustomer = (LocalArCustomer) customer;
                list.add(arCustomer.getCstCustomerCode());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getCmSplAll(Integer branchCode, Integer companyCode) {

        Debug.print("CmAdjustmentBean getCmSplAll");

        ArrayList list = new ArrayList();

        try {

            Collection apSuppliers = apSupplierHome.findEnabledSplAll(branchCode, companyCode);

            for (Object supplier : apSuppliers) {

                LocalApSupplier apSupplier = (LocalApSupplier) supplier;
                list.add(apSupplier.getSplSupplierCode());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArModCustomerDetails getCmCstByCstCustomerCode(String CST_CSTMR_CODE, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("CmAdjustment getCmCstByCstCustomerCode");

        try {

            LocalArCustomer arCustomer = null;

            try {

                arCustomer = arCustomerHome.findByCstCustomerCode(CST_CSTMR_CODE, companyCode);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArModCustomerDetails mdetails = new ArModCustomerDetails();

            mdetails.setCstCustomerCode(arCustomer.getCstCustomerCode());
            mdetails.setCstPytName(arCustomer.getAdPaymentTerm() != null ? arCustomer.getAdPaymentTerm().getPytName() : null);
            mdetails.setCstCcWtcName(arCustomer.getArCustomerClass().getArWithholdingTaxCode() != null ? arCustomer.getArCustomerClass().getArWithholdingTaxCode().getWtcName() : null);
            mdetails.setCstBillToAddress(arCustomer.getCstBillToAddress());
            mdetails.setCstShipToAddress(arCustomer.getCstShipToAddress());
            mdetails.setCstBillToAddress(arCustomer.getCstBillToAddress());
            mdetails.setCstBillToContact(arCustomer.getCstBillToContact());
            mdetails.setCstBillToAltContact(arCustomer.getCstBillToAltContact());
            mdetails.setCstBillToPhone(arCustomer.getCstBillToPhone());
            mdetails.setCstBillingHeader(arCustomer.getCstBillingHeader());
            mdetails.setCstBillingFooter(arCustomer.getCstBillingFooter());
            mdetails.setCstBillingHeader2(arCustomer.getCstBillingHeader2());
            mdetails.setCstBillingFooter2(arCustomer.getCstBillingFooter2());
            mdetails.setCstBillingHeader3(arCustomer.getCstBillingHeader3());
            mdetails.setCstBillingFooter3(arCustomer.getCstBillingFooter3());
            mdetails.setCstBillingSignatory(arCustomer.getCstBillingSignatory());
            mdetails.setCstSignatoryTitle(arCustomer.getCstSignatoryTitle());
            mdetails.setCstShipToAddress(arCustomer.getCstShipToAddress());
            mdetails.setCstShipToContact(arCustomer.getCstShipToContact());
            mdetails.setCstShipToAltContact(arCustomer.getCstShipToAltContact());
            mdetails.setCstShipToPhone(arCustomer.getCstShipToPhone());
            mdetails.setCstName(arCustomer.getCstName());
            mdetails.setCstNumbersParking(arCustomer.getCstNumbersParking());
            mdetails.setCstSquareMeter(arCustomer.getCstSquareMeter());
            mdetails.setCstRealPropertyTaxRate(arCustomer.getCstRealPropertyTaxRate());
            if (arCustomer.getArSalesperson() != null && arCustomer.getCstArSalesperson2() == null) {

                mdetails.setCstSlpSalespersonCode(arCustomer.getArSalesperson().getSlpSalespersonCode());
                mdetails.setCstSlpName(arCustomer.getArSalesperson().getSlpName());

            } else if (arCustomer.getArSalesperson() == null && arCustomer.getCstArSalesperson2() != null) {

                LocalArSalesperson arSalesperson2 = null;
                arSalesperson2 = arSalespersonHome.findByPrimaryKey(arCustomer.getCstArSalesperson2());

                mdetails.setCstSlpSalespersonCode(arSalesperson2.getSlpSalespersonCode());
                mdetails.setCstSlpName(arSalesperson2.getSlpName());
            }
            if (arCustomer.getArSalesperson() != null && arCustomer.getCstArSalesperson2() != null) {

                mdetails.setCstSlpSalespersonCode(arCustomer.getArSalesperson().getSlpSalespersonCode());
                mdetails.setCstSlpName(arCustomer.getArSalesperson().getSlpName());
                LocalArSalesperson arSalesperson2 = null;
                arSalesperson2 = arSalespersonHome.findByPrimaryKey(arCustomer.getCstArSalesperson2());

                mdetails.setCstSlpSalespersonCode2(arSalesperson2.getSlpSalespersonCode());
                mdetails.setCstSlpName2(arSalesperson2.getSlpName());
            }

            if (arCustomer.getArCustomerClass().getArTaxCode() != null) {

                mdetails.setCstCcTcName(arCustomer.getArCustomerClass().getArTaxCode().getTcName());
                mdetails.setCstCcTcRate(arCustomer.getArCustomerClass().getArTaxCode().getTcRate());
                mdetails.setCstCcTcType(arCustomer.getArCustomerClass().getArTaxCode().getTcType());
            }

            if (arCustomer.getInvLineItemTemplate() != null) {
                mdetails.setCstLitName(arCustomer.getInvLineItemTemplate().getLitName());
            }

            return mdetails;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArModSalesOrderDetails getCmSoBySoNumber(String SO_NMBR, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("CmAdjustment getCmSoBySoNumber");

        try {

            LocalArSalesOrder arSalesOrder = null;

            try {

                arSalesOrder = arSalesOrderHome.findBySoDocumentNumberAndBrCode(SO_NMBR, branchCode, companyCode);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArModSalesOrderDetails mdetails = new ArModSalesOrderDetails();

            mdetails.setSoCode(arSalesOrder.getSoCode());
            mdetails.setSoDate(arSalesOrder.getSoDate());
            mdetails.setSoDocumentNumber(arSalesOrder.getSoDocumentNumber());
            mdetails.setSoReferenceNumber(arSalesOrder.getSoReferenceNumber());
            mdetails.setSoDescription(arSalesOrder.getSoDescription());
            mdetails.setSoBoLock(arSalesOrder.getSoBoLock());
            mdetails.setSoLock(arSalesOrder.getSoLock());
            mdetails.setSoPosted(arSalesOrder.getSoPosted());

            mdetails.setSoVoid(arSalesOrder.getSoVoid());

            mdetails.setSoMemo(arSalesOrder.getSoMemo());

            return mdetails;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteCmAdjEntry(Integer ADJ_CODE, String AD_USR, Integer companyCode) throws GlobalRecordAlreadyDeletedException {

        Debug.print("CmAdjustmentEntryControllerBean deleteCmAdjEntry");

        try {

            LocalCmAdjustment cmAdjustment = cmAdjustmentHome.findByPrimaryKey(ADJ_CODE);

            if (cmAdjustment.getAdjApprovalStatus() != null && cmAdjustment.getAdjApprovalStatus().equals("PENDING")) {

                Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode("CM ADJUSTMENT", cmAdjustment.getAdjCode(), companyCode);

                for (Object approvalQueue : adApprovalQueues) {

                    LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) approvalQueue;

                    // adApprovalQueue.entityRemove();
                    em.remove(adApprovalQueue);
                }
            }

            adDeleteAuditTrailHome.create("CM ADJUSTMENT", cmAdjustment.getAdjDate(), cmAdjustment.getAdjDocumentNumber(), cmAdjustment.getAdjReferenceNumber(), cmAdjustment.getAdjAmount(), AD_USR, new Date(), companyCode);

            // cmAdjustment.entityRemove();
            em.remove(cmAdjustment);

        } catch (FinderException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlobalRecordAlreadyDeletedException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdApprovalNotifiedUsersByAdjCode(Integer ADJ_CODE, Integer companyCode) {

        Debug.print("CmAdjustmentEntryControllerBean getAdApprovalNotifiedUsersByAdjCode");

        ArrayList list = new ArrayList();

        try {

            LocalCmAdjustment cmAdjustment = cmAdjustmentHome.findByPrimaryKey(ADJ_CODE);

            if (cmAdjustment.getAdjPosted() == EJBCommon.TRUE) {

                list.add("DOCUMENT POSTED");
                return list;
            }

            Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode("CM ADJUSTMENT", ADJ_CODE, companyCode);

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

    public AdModBankAccountDetails getAdBaByBaName(String BA_NM, Integer companyCode) {

        Debug.print("CmAdjustmentEntryControllerBean getAdBaByBaName");

        try {

            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(BA_NM, companyCode);

            AdModBankAccountDetails mdetails = new AdModBankAccountDetails();

            if (adBankAccount.getGlFunctionalCurrency() != null) {

                mdetails.setBaFcName(adBankAccount.getGlFunctionalCurrency().getFcName());
            }

            return mdetails;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer companyCode) {

        Debug.print("CmAdjustmentEntryControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public double getFrRateByFrNameAndFrDate(String FC_NM, Date CONVERSION_DATE, Integer companyCode) throws GlobalConversionDateNotExistException {

        Debug.print("GlJournalEntryControllerBean getFrRateByFrNameAndFrDate");

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

            if (cmAdjustment.getAdjVoid() == EJBCommon.FALSE && cmAdjustment.getAdjPosted() == EJBCommon.TRUE) {

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

    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double JL_AMNT, Integer companyCode) {

        Debug.print("CmAdjustmentEntryControllerBean postToGl");

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

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("CmAdjustmentEntryControllerBean ejbCreate");
    }
}