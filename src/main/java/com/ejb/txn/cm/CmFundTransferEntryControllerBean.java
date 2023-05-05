/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class CmFundTransferEntryControllerBean
 * @created November 24, 2003, 8:35 AM
 * @author Neil Andrew M. Ajero
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
import com.ejb.entities.ad.LocalAdBranchDocumentSequenceAssignment;
import com.ejb.dao.ad.LocalAdBranchDocumentSequenceAssignmentHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.dao.ad.LocalAdDeleteAuditTrailHome;
import com.ejb.entities.ad.LocalAdDocumentSequenceAssignment;
import com.ejb.dao.ad.LocalAdDocumentSequenceAssignmentHome;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ar.LocalArReceipt;
import com.ejb.dao.ar.LocalArReceiptHome;
import com.ejb.entities.cm.LocalCmDistributionRecord;
import com.ejb.dao.cm.LocalCmDistributionRecordHome;
import com.ejb.entities.cm.LocalCmFundTransfer;
import com.ejb.dao.cm.LocalCmFundTransferHome;
import com.ejb.entities.cm.LocalCmFundTransferReceipt;
import com.ejb.dao.cm.LocalCmFundTransferReceiptHome;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.GlobalBranchAccountNumberInvalidException;
import com.ejb.exception.global.GlobalConversionDateNotExistException;
import com.ejb.exception.global.GlobalDocumentNumberNotUniqueException;
import com.ejb.exception.global.GlobalJournalNotBalanceException;
import com.ejb.exception.global.GlobalNoApprovalApproverFoundException;
import com.ejb.exception.global.GlobalNoApprovalRequesterFoundException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalOverapplicationNotAllowedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalTransactionAlreadyApprovedException;
import com.ejb.exception.global.GlobalTransactionAlreadyPendingException;
import com.ejb.exception.global.GlobalTransactionAlreadyPostedException;
import com.ejb.exception.global.GlobalTransactionAlreadyVoidException;
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
import com.util.cm.CmFundTransferDetails;
import com.util.mod.ad.AdModBankAccountDetails;
import com.util.mod.cm.CmModFundTransferEntryDetails;
import com.util.mod.cm.CmModFundTransferReceiptDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;

@Stateless(name = "CmFundTransferEntryControllerEJB")
public class CmFundTransferEntryControllerBean extends EJBContextClass implements CmFundTransferEntryController {

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
    private LocalAdAmountLimitHome adAmountLimitHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalAdApprovalUserHome adApprovalUserHome;
    @EJB
    private LocalAdBankAccountBalanceHome adBankAccountBalanceHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdDeleteAuditTrailHome adDeleteAuditTrailHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalCmDistributionRecordHome cmDistributionRecordHome;
    @EJB
    private LocalCmFundTransferHome cmFundTransferHome;
    @EJB
    private LocalCmFundTransferReceiptHome cmFundTransferReceiptHome;
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
    private LocalGlSuspenseAccountHome glSuspenseAccountHome;

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

    public CmModFundTransferEntryDetails getCmFtByFtCode(Integer FT_CODE, Integer companyCode) {

        Debug.print("CmFundTransferEntryControllerBean getCmFtByFtCode");

        LocalCmFundTransfer cmFundTransfer = null;
        LocalAdBankAccount adBankAccountFrom = null;
        LocalAdBankAccount adBankAccountTo = null;

        try {

            cmFundTransfer = cmFundTransferHome.findByPrimaryKey(FT_CODE);

            try {

                adBankAccountFrom = adBankAccountHome.findByPrimaryKey(cmFundTransfer.getFtAdBaAccountFrom());

            } catch (FinderException ex) {

            }

            try {

                adBankAccountTo = adBankAccountHome.findByPrimaryKey(cmFundTransfer.getFtAdBaAccountTo());

            } catch (FinderException ex) {

            }

            CmModFundTransferEntryDetails ftDetails = new CmModFundTransferEntryDetails();
            ftDetails.setFtCode(cmFundTransfer.getFtCode());
            ftDetails.setFtDate(cmFundTransfer.getFtDate());
            ftDetails.setFtDocumentNumber(cmFundTransfer.getFtDocumentNumber());
            ftDetails.setFtReferenceNumber(cmFundTransfer.getFtReferenceNumber());
            ftDetails.setFtMemo(cmFundTransfer.getFtMemo());
            ftDetails.setFtAdBankAccountNameFrom(adBankAccountFrom.getBaName());
            ftDetails.setFtCurrencyFrom(adBankAccountFrom.getGlFunctionalCurrency().getFcName());
            ftDetails.setFtAdBankAccountNameTo(adBankAccountTo.getBaName());
            ftDetails.setFtCurrencyTo(adBankAccountTo.getGlFunctionalCurrency().getFcName());
            ftDetails.setFtAmount(cmFundTransfer.getFtAmount());
            ftDetails.setFtConversionDate(cmFundTransfer.getFtConversionDate());
            ftDetails.setFtConversionRateFrom(cmFundTransfer.getFtConversionRateFrom());
            ftDetails.setFtVoid(cmFundTransfer.getFtVoid());
            ftDetails.setFtApprovalStatus(cmFundTransfer.getFtApprovalStatus());
            ftDetails.setFtPosted(cmFundTransfer.getFtPosted());
            ftDetails.setFtCreatedBy(cmFundTransfer.getFtCreatedBy());
            ftDetails.setFtDateCreated(cmFundTransfer.getFtDateCreated());
            ftDetails.setFtLastModifiedBy(cmFundTransfer.getFtLastModifiedBy());
            ftDetails.setFtDateLastModified(cmFundTransfer.getFtDateLastModified());
            ftDetails.setFtApprovedRejectedBy(cmFundTransfer.getFtApprovedRejectedBy());
            ftDetails.setFtDateApprovedRejected(cmFundTransfer.getFtDateApprovedRejected());
            ftDetails.setFtPostedBy(cmFundTransfer.getFtPostedBy());
            ftDetails.setFtDatePosted(cmFundTransfer.getFtDatePosted());
            ftDetails.setFtReasonForRejection(cmFundTransfer.getFtReasonForRejection());
            ftDetails.setFtType(cmFundTransfer.getFtType());
            ftDetails.setFtConversionRateTo(cmFundTransfer.getFtConversionRateTo());

            if (cmFundTransfer.getFtType().equals("DEPOSIT")) {

                ArrayList cmFTRList = new ArrayList();
                Collection fundTransferReceipts = cmFundTransfer.getCmFundTransferReceipts();

                for (Object transferReceipt : fundTransferReceipts) {

                    LocalCmFundTransferReceipt cmFundTransferReceipt = (LocalCmFundTransferReceipt) transferReceipt;

                    CmModFundTransferReceiptDetails mdetails = new CmModFundTransferReceiptDetails();

                    mdetails.setFtrCode(cmFundTransferReceipt.getFtrCode());
                    mdetails.setFtrTotalAmount(cmFundTransferReceipt.getArReceipt().getRctAmount());
                    mdetails.setFtrReceiptNumber(cmFundTransferReceipt.getArReceipt().getRctNumber());
                    mdetails.setFtrAmountDeposited(cmFundTransferReceipt.getFtrAmountDeposited());
                    mdetails.setFtrReceiptDate(cmFundTransferReceipt.getArReceipt().getRctDate());

                    Collection cmFundTransferReceipts = cmFundTransferReceipt.getArReceipt().getCmFundTransferReceipts();

                    if (cmFundTransferReceipts == null) {

                        mdetails.setFtrAmountUndeposited(cmFundTransferReceipt.getArReceipt().getRctAmount());

                    } else {

                        double TTL_DPSTD_AMNT = 0;

                        for (Object fundTransferReceipt : cmFundTransferReceipts) {

                            LocalCmFundTransferReceipt existingCmFundTransferReceipt = (LocalCmFundTransferReceipt) fundTransferReceipt;

                            if (existingCmFundTransferReceipt.getCmFundTransfer().getFtPosted() == EJBCommon.TRUE)
                                TTL_DPSTD_AMNT = TTL_DPSTD_AMNT + existingCmFundTransferReceipt.getFtrAmountDeposited();
                        }

                        mdetails.setFtrAmountUndeposited(cmFundTransferReceipt.getArReceipt().getRctAmount() - TTL_DPSTD_AMNT);
                    }

                    cmFTRList.add(mdetails);
                }

                ftDetails.setFtCmFTRList(cmFTRList);
            }

            return ftDetails;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer saveCmFtEntry(CmFundTransferDetails details, String BNK_NM_FRM, String BNK_NM_TO, String FC_NM, boolean isDraft, ArrayList list, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyApprovedException, GlobalConversionDateNotExistException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalDocumentNumberNotUniqueException, GlobalBranchAccountNumberInvalidException, GlobalNoRecordFoundException, GlobalOverapplicationNotAllowedException {

        Debug.print("CmFundTransferEntryControllerBean saveCmFtEntry");

        LocalCmFundTransfer cmFundTransfer = null;
        LocalAdBankAccount adBankAccountFrom = null;
        LocalAdBankAccount adBankAccountTo = null;
        LocalCmDistributionRecord cmDistributionRecord = null;

        Collection cmDistributionRecords = null;

        try {

            // find bank account from

            try {

                adBankAccountFrom = adBankAccountHome.findByBaName(BNK_NM_FRM, companyCode);

            } catch (FinderException ex) {

            }

            // find bank account to

            try {

                adBankAccountTo = adBankAccountHome.findByBaName(BNK_NM_TO, companyCode);

            } catch (FinderException ex) {

            }

            // validate if fund transfer is already deleted

            try {

                if (details.getFtCode() != null) {

                    cmFundTransfer = cmFundTransferHome.findByPrimaryKey(details.getFtCode());
                }

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if fund transfer is already posted, void, arproved or pending

            if (details.getFtCode() != null) {

                if (cmFundTransfer.getFtApprovalStatus() != null) {

                    if (cmFundTransfer.getFtApprovalStatus().equals("APPROVED") || cmFundTransfer.getFtApprovalStatus().equals("N/A")) {

                        throw new GlobalTransactionAlreadyApprovedException();

                    } else if (cmFundTransfer.getFtApprovalStatus().equals("PENDING")) {

                        throw new GlobalTransactionAlreadyPendingException();
                    }
                }

                if (cmFundTransfer.getFtPosted() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyPostedException();

                } else if (cmFundTransfer.getFtVoid() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyVoidException();
                }
            }

            // fund tranfer void

            if (details.getFtCode() != null && details.getFtVoid() == EJBCommon.TRUE && cmFundTransfer.getFtPosted() == EJBCommon.FALSE) {

                cmFundTransfer.setFtVoid(EJBCommon.TRUE);
                cmFundTransfer.setFtLastModifiedBy(details.getFtLastModifiedBy());
                cmFundTransfer.setFtDateLastModified(details.getFtDateLastModified());

                return cmFundTransfer.getFtCode();
            }

            // validate if document number is unique document number is automatic then set
            // next sequence

            if (details.getFtCode() == null) {

                LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
                LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

                try {

                    adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("CM FUND TRANSFER", companyCode);

                } catch (FinderException ex) {

                }

                try {

                    adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode);

                } catch (FinderException ex) {

                }

                LocalCmFundTransfer cmExistingFundTransfer = null;

                try {

                    cmExistingFundTransfer = cmFundTransferHome.findByFtDocumentNumberAndBrCode(details.getFtDocumentNumber(), branchCode, companyCode);

                } catch (FinderException ex) {
                }

                if (cmExistingFundTransfer != null) {

                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (details.getFtDocumentNumber() == null || details.getFtDocumentNumber().trim().length() == 0)) {

                    while (true) {

                        if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                            try {

                                cmFundTransferHome.findByFtDocumentNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), branchCode, companyCode);
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                            } catch (FinderException ex) {

                                details.setFtDocumentNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                break;
                            }

                        } else {

                            try {

                                cmFundTransferHome.findByFtDocumentNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), branchCode, companyCode);
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                            } catch (FinderException ex) {

                                details.setFtDocumentNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                break;
                            }
                        }
                    }
                }

            } else {

                LocalCmFundTransfer cmExistingTransfer = null;

                try {

                    cmExistingTransfer = cmFundTransferHome.findByFtDocumentNumberAndBrCode(details.getFtDocumentNumber(), branchCode, companyCode);

                } catch (FinderException ex) {

                }

                if (cmExistingTransfer != null && !cmExistingTransfer.getFtCode().equals(details.getFtCode())) {

                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (cmFundTransfer.getFtDocumentNumber() != details.getFtDocumentNumber() && (details.getFtDocumentNumber() == null || details.getFtDocumentNumber().trim().length() == 0)) {

                    details.setFtDocumentNumber(cmFundTransfer.getFtDocumentNumber());
                }
            }

            // validate if conversion date exists

            try {

                if (details.getFtConversionDate() != null) {

                    LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                    LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, companyCode);

                    if (!glValidateFunctionalCurrency.getFcName().equals("USD")) {

                        LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(), details.getFtConversionDate(), companyCode);

                    } else if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                        LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), details.getFtConversionDate(), companyCode);
                    }
                }

            } catch (FinderException ex) {

                throw new GlobalConversionDateNotExistException();
            }

            // used in checking if fund transfer should re-generate distribution records

            boolean isRecalculate = true;

            if (details.getFtCode() == null) {

                // create new fund transfer

                cmFundTransfer = cmFundTransferHome.create(details.getFtDate(), details.getFtDocumentNumber(), details.getFtReferenceNumber(), details.getFtMemo(), adBankAccountFrom.getBaCode(), adBankAccountTo.getBaCode(), details.getFtAmount(), details.getFtConversionDate(), details.getFtConversionRateFrom(), EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, null, EJBCommon.FALSE, details.getFtCreatedBy(), details.getFtDateCreated(), details.getFtLastModifiedBy(), details.getFtDateLastModified(), null, null, null, null, null, details.getFtType(), details.getFtConversionRateTo(), null, null, branchCode, companyCode);

            } else {

                // update fund transfer

                cmFundTransfer = cmFundTransferHome.findByPrimaryKey(details.getFtCode());

                // check if critical fields are changed

                if (!cmFundTransfer.getFtAdBaAccountFrom().equals(adBankAccountFrom.getBaCode()) || !cmFundTransfer.getFtAdBaAccountTo().equals(adBankAccountTo.getBaCode()) || cmFundTransfer.getFtAmount() != details.getFtAmount() || !cmFundTransfer.getFtType().equals(details.getFtType()) || (cmFundTransfer.getFtType().equals("DEPOSIT") && list.size() != cmFundTransfer.getCmFundTransferReceipts().size())) {

                    isRecalculate = true;

                } else if (cmFundTransfer.getFtType().equals("DEPOSIT") && list.size() == cmFundTransfer.getCmFundTransferReceipts().size()) {

                    Iterator iter = cmFundTransfer.getCmFundTransferReceipts().iterator();
                    Iterator listIter = list.iterator();

                    while (iter.hasNext()) {

                        LocalCmFundTransferReceipt cmFundTransferReceipt = (LocalCmFundTransferReceipt) iter.next();
                        CmModFundTransferReceiptDetails mFtrDetails = (CmModFundTransferReceiptDetails) listIter.next();

                        if (!cmFundTransferReceipt.getArReceipt().getRctNumber().equals(mFtrDetails.getFtrReceiptNumber()) || cmFundTransferReceipt.getFtrAmountDeposited() != mFtrDetails.getFtrAmountDeposited()) {

                            isRecalculate = true;
                            break;
                        }

                        isRecalculate = false;
                    }

                } else {

                    isRecalculate = false;
                }

                cmFundTransfer.setFtDate(details.getFtDate());
                cmFundTransfer.setFtDocumentNumber(details.getFtDocumentNumber());
                cmFundTransfer.setFtReferenceNumber(details.getFtReferenceNumber());
                cmFundTransfer.setFtMemo(details.getFtMemo());
                cmFundTransfer.setFtAdBaAccountFrom(adBankAccountFrom.getBaCode());
                cmFundTransfer.setFtAdBaAccountTo(adBankAccountTo.getBaCode());
                cmFundTransfer.setFtAmount(details.getFtAmount());
                cmFundTransfer.setFtConversionDate(details.getFtConversionDate());
                cmFundTransfer.setFtConversionRateFrom(details.getFtConversionRateFrom());
                cmFundTransfer.setFtVoid(details.getFtVoid());
                cmFundTransfer.setFtAccountFromReconciled(EJBCommon.FALSE);
                cmFundTransfer.setFtAccountToReconciled(EJBCommon.FALSE);
                cmFundTransfer.setFtAccountFromDateReconciled(null);
                cmFundTransfer.setFtAccountToDateReconciled(null);
                cmFundTransfer.setFtLastModifiedBy(details.getFtLastModifiedBy());
                cmFundTransfer.setFtDateLastModified(details.getFtDateLastModified());
                cmFundTransfer.setFtReasonForRejection(null);
                cmFundTransfer.setFtType(details.getFtType());
                cmFundTransfer.setFtConversionRateTo(details.getFtConversionRateTo());
            }

            if (isRecalculate) {

                // remove all distribution records

                cmDistributionRecords = cmFundTransfer.getCmDistributionRecords();

                Iterator i = cmDistributionRecords.iterator();

                while (i.hasNext()) {

                    cmDistributionRecord = (LocalCmDistributionRecord) i.next();

                    i.remove();

                    // cmDistributionRecord.entityRemove();
                    em.remove(cmDistributionRecord);
                }

                // add new distribution record

                this.addCmDrEntry(cmFundTransfer.getCmDrNextLine(), "CASH", EJBCommon.TRUE, cmFundTransfer.getFtAmount(), adBankAccountTo.getBaCoaGlCashAccount(), cmFundTransfer, branchCode, companyCode);

                this.addCmDrEntry(cmFundTransfer.getCmDrNextLine(), "CASH", EJBCommon.FALSE, cmFundTransfer.getFtAmount(), adBankAccountFrom.getBaCoaGlCashAccount(), cmFundTransfer, branchCode, companyCode);

                if (cmFundTransfer.getFtType().equals("DEPOSIT")) {

                    // remove all cmFundTransferReceipts

                    Collection cmFundTransferReceipts = cmFundTransfer.getCmFundTransferReceipts();

                    i = cmFundTransferReceipts.iterator();

                    while (i.hasNext()) {

                        LocalCmFundTransferReceipt cmFundTransferReceipt = (LocalCmFundTransferReceipt) i.next();

                        i.remove();
                        cmFundTransferReceipt.getArReceipt().setRctLock(EJBCommon.FALSE);
                        // cmFundTransferReceipt.entityRemove();
                        em.remove(cmFundTransferReceipt);
                    }

                    // add new cmFundTransferReceipt

                    i = list.iterator();

                    while (i.hasNext()) {

                        CmModFundTransferReceiptDetails mFtrDetails = (CmModFundTransferReceiptDetails) i.next();

                        if (mFtrDetails.getFtrAmountDeposited() > mFtrDetails.getFtrAmountUndeposited()) {

                            throw new GlobalOverapplicationNotAllowedException();
                        }

                        LocalCmFundTransferReceipt cmFundTransferReceipt = cmFundTransferReceiptHome.create(mFtrDetails.getFtrAmountDeposited(), companyCode);

                        cmFundTransfer.addCmFundTransferReceipt(cmFundTransferReceipt);
                        cmFundTransferReceipt.setCmFundTransfer(cmFundTransfer);

                        LocalArReceipt arReceipt = null;

                        try {

                            arReceipt = arReceiptHome.findByRctNumberAndBrCode(mFtrDetails.getFtrReceiptNumber(), branchCode, companyCode);

                        } catch (FinderException ex) {

                            throw new GlobalNoRecordFoundException();
                        }

                        arReceipt.addCmFundTransferReceipt(cmFundTransferReceipt);
                        cmFundTransferReceipt.setArReceipt(arReceipt);

                        arReceipt.setRctLock(EJBCommon.TRUE);
                    }
                }
            }

            // generate approval status

            String FT_APPPRVL_STATUS = null;

            if (!isDraft) {

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);

                // check if fund transfer approval is enabled

                if (adApproval.getAprEnableCmFundTransfer() == EJBCommon.FALSE) {

                    FT_APPPRVL_STATUS = "N/A";

                } else {

                    // check if fund transfer is self approved

                    LocalAdAmountLimit adAmountLimit = null;

                    try {

                        adAmountLimit = adAmountLimitHome.findByAdcTypeAndAuTypeAndUsrName("CM FUND TRANSFER", "REQUESTER", details.getFtLastModifiedBy(), companyCode);

                    } catch (FinderException ex) {

                        throw new GlobalNoApprovalRequesterFoundException();
                    }

                    if (cmFundTransfer.getFtAmount() <= adAmountLimit.getCalAmountLimit()) {

                        FT_APPPRVL_STATUS = "N/A";

                    } else {

                        // for approval, create approval queue

                        Collection adAmountLimits = adAmountLimitHome.findByAdcTypeAndGreaterThanCalAmountLimit("CM FUND TRANSFER", adAmountLimit.getCalAmountLimit(), companyCode);

                        if (adAmountLimits.isEmpty()) {

                            Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adAmountLimit.getCalCode(), companyCode);

                            if (adApprovalUsers.isEmpty()) {

                                throw new GlobalNoApprovalApproverFoundException();
                            }

                            for (Object approvalUser : adApprovalUsers) {

                                LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(branchCode, companyCode, adApprovalQueueHome, cmFundTransfer, adAmountLimit, adApprovalUser);

                                adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                            }

                        } else {

                            boolean isApprovalUsersFound = false;

                            Iterator n = adAmountLimits.iterator();

                            while (n.hasNext()) {

                                LocalAdAmountLimit adNextAmountLimit = (LocalAdAmountLimit) n.next();

                                if (cmFundTransfer.getFtAmount() <= adNextAmountLimit.getCalAmountLimit()) {

                                    Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adAmountLimit.getCalCode(), companyCode);

                                    for (Object approvalUser : adApprovalUsers) {

                                        isApprovalUsersFound = true;

                                        LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                        LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(branchCode, companyCode, adApprovalQueueHome, cmFundTransfer, adAmountLimit, adApprovalUser);

                                        adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                                    }

                                    break;

                                } else if (!n.hasNext()) {

                                    Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adNextAmountLimit.getCalCode(), companyCode);

                                    for (Object approvalUser : adApprovalUsers) {

                                        isApprovalUsersFound = true;

                                        LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                        LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(branchCode, companyCode, adApprovalQueueHome, cmFundTransfer, adNextAmountLimit, adApprovalUser);

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

                        FT_APPPRVL_STATUS = "PENDING";
                    }
                }
            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            if (FT_APPPRVL_STATUS != null && FT_APPPRVL_STATUS.equals("N/A") && adPreference.getPrfCmGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                this.executeCmFtPost(cmFundTransfer.getFtCode(), cmFundTransfer.getFtLastModifiedBy(), branchCode, companyCode);
            }

            // set fund transfer approval status

            cmFundTransfer.setFtApprovalStatus(FT_APPPRVL_STATUS);

            return cmFundTransfer.getFtCode();

        } catch (GlobalDocumentNumberNotUniqueException | GlobalOverapplicationNotAllowedException |
                 GlobalNoRecordFoundException | GlobalBranchAccountNumberInvalidException |
                 GlobalJournalNotBalanceException | GlJREffectiveDatePeriodClosedException |
                 GlJREffectiveDateNoPeriodExistException | GlobalNoApprovalApproverFoundException |
                 GlobalNoApprovalRequesterFoundException | GlobalTransactionAlreadyVoidException |
                 GlobalTransactionAlreadyPostedException | GlobalTransactionAlreadyPendingException |
                 GlobalTransactionAlreadyApprovedException | GlobalConversionDateNotExistException |
                 GlobalRecordAlreadyDeletedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteCmFtEntry(Integer FT_CODE, String AD_USR, Integer companyCode) throws GlobalRecordAlreadyDeletedException {

        Debug.print("CmFundTransferEntryControllerBean deleteCmFtEntry");

        try {

            LocalCmFundTransfer cmFundTransfer = cmFundTransferHome.findByPrimaryKey(FT_CODE);

            if (cmFundTransfer.getFtApprovalStatus() != null && cmFundTransfer.getFtApprovalStatus().equals("PENDING")) {

                Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode("CM FUND TRANSFER", cmFundTransfer.getFtCode(), companyCode);

                for (Object approvalQueue : adApprovalQueues) {

                    LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) approvalQueue;

                    // adApprovalQueue.entityRemove();
                    em.remove(adApprovalQueue);
                }
            }

            // remove all cmFundTransferReceipts

            Collection cmFundTransferReceipts = cmFundTransfer.getCmFundTransferReceipts();

            for (Object fundTransferReceipt : cmFundTransferReceipts) {

                LocalCmFundTransferReceipt cmFundTransferReceipt = (LocalCmFundTransferReceipt) fundTransferReceipt;

                cmFundTransferReceipt.getArReceipt().setRctLock(EJBCommon.FALSE);
            }

            adDeleteAuditTrailHome.create("CM FUND TRANSFER", cmFundTransfer.getFtDate(), cmFundTransfer.getFtDocumentNumber(), cmFundTransfer.getFtReferenceNumber(), cmFundTransfer.getFtAmount(), AD_USR, new Date(), companyCode);

            // cmFundTransfer.entityRemove();
            em.remove(cmFundTransfer);

        } catch (FinderException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlobalRecordAlreadyDeletedException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdApprovalNotifiedUsersByFtCode(Integer FT_CODE, Integer companyCode) {

        Debug.print("CmFundTransferEntryControllerBean getAdApprovalNotifiedUsersByFtCode");

        ArrayList list = new ArrayList();

        try {

            LocalCmFundTransfer cmFundTransfer = cmFundTransferHome.findByPrimaryKey(FT_CODE);

            if (cmFundTransfer.getFtPosted() == EJBCommon.TRUE) {

                list.add("DOCUMENT POSTED");
                return list;
            }

            Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode("CM FUND TRANSFER", FT_CODE, companyCode);

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

        Debug.print("CmFundTransferEntryControllerBean getAdBaByBaName");

        try {

            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(BA_NM, companyCode);

            AdModBankAccountDetails mdetails = new AdModBankAccountDetails();

            if (adBankAccount.getGlFunctionalCurrency() != null) {

                mdetails.setBaFcName(adBankAccount.getGlFunctionalCurrency().getFcName());
                mdetails.setBaIsCashAccount(adBankAccount.getBaIsCashAccount());
            }

            return mdetails;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer companyCode) {

        Debug.print("CmFundTransferEntryControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public double getFrRateByFrNameAndFrDate(String FC_NM, Date CONVERSION_DATE, Integer companyCode) throws GlobalConversionDateNotExistException {

        Debug.print("CmFundTransferEntryControllerBean getFrRateByFrNameAndFrDate");

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

    public ArrayList getPostedArReceiptByRctDateFromAndRctDateToAndBankAccountName(Date RCT_DT_FRM, Date RCT_DT_TO, String BA_NM, String ORDER_BY, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException {

        ArrayList list = new ArrayList();

        Collection arReceipts = null;

        try {

            if (RCT_DT_FRM != null && RCT_DT_TO != null) {

                arReceipts = arReceiptHome.findUndepositedPostedRctByBaNameAndBrCodeAndRctDateRange(RCT_DT_FRM, RCT_DT_TO, BA_NM, branchCode, companyCode);

            } else {

                arReceipts = arReceiptHome.findUndepositedPostedRctByBaNameAndBrCode(BA_NM, branchCode, companyCode);
            }
        } catch (FinderException ex) {

            throw new GlobalNoRecordFoundException();
        }

        if (arReceipts.size() == 0) {

            throw new GlobalNoRecordFoundException();
        }

        try {

            for (Object receipt : arReceipts) {

                LocalArReceipt arReceipt = (LocalArReceipt) receipt;

                CmModFundTransferReceiptDetails details = new CmModFundTransferReceiptDetails();

                details.setFtrReceiptDate(arReceipt.getRctDate());
                details.setFtrAdCompany(arReceipt.getRctAdCompany());
                details.setFtrReceiptNumber(arReceipt.getRctNumber());
                details.setFtrTotalAmount(arReceipt.getRctAmount());

                Collection cmFundTransferReceipts = arReceipt.getCmFundTransferReceipts();

                if (cmFundTransferReceipts == null) {

                    details.setFtrAmountDeposited(arReceipt.getRctAmount());
                    details.setFtrAmountUndeposited(arReceipt.getRctAmount());

                } else {

                    double TTL_DPSTD_AMNT = 0;

                    for (Object fundTransferReceipt : cmFundTransferReceipts) {

                        LocalCmFundTransferReceipt cmFundTransferReceipt = (LocalCmFundTransferReceipt) fundTransferReceipt;

                        if (cmFundTransferReceipt.getCmFundTransfer().getFtPosted() == EJBCommon.TRUE) {

                            TTL_DPSTD_AMNT = TTL_DPSTD_AMNT + cmFundTransferReceipt.getFtrAmountDeposited();
                        }
                    }

                    details.setFtrAmountDeposited(arReceipt.getRctAmount() - TTL_DPSTD_AMNT);
                    details.setFtrAmountUndeposited(arReceipt.getRctAmount() - TTL_DPSTD_AMNT);
                }

                details.setOrderBy(ORDER_BY);

                if (details.getFtrAmountDeposited() != 0) {

                    list.add(details);
                }
            }

            if (list.size() == 0) {

                throw new GlobalNoRecordFoundException();

            } else {

                list.sort(CmModFundTransferReceiptDetails.NoGroupComparator);

                return list;
            }

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void clearCmFundTransferReceipts(Integer FT_CODE, Integer companyCode) throws GlobalRecordAlreadyDeletedException {

        Debug.print("CmFundTransferEntryControllerBean clearCmFundTransferReceipts");

        try {

            LocalCmFundTransfer cmFundTransfer = cmFundTransferHome.findByPrimaryKey(FT_CODE);

            // remove all cmFundTransferReceipts

            Collection cmFundTransferReceipts = cmFundTransfer.getCmFundTransferReceipts();

            Iterator i = cmFundTransferReceipts.iterator();

            while (i.hasNext()) {

                LocalCmFundTransferReceipt cmFundTransferReceipt = (LocalCmFundTransferReceipt) i.next();

                i.remove();
                cmFundTransferReceipt.getArReceipt().setRctLock(EJBCommon.FALSE);
                // cmFundTransferReceipt.entityRemove();
                em.remove(cmFundTransferReceipt);
            }
        } catch (FinderException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlobalRecordAlreadyDeletedException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods
    private void addCmDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE, LocalCmFundTransfer cmFundTransfer, Integer branchCode, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("CmFundTransferEntryControllerBean addCmDrEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, branchCode, companyCode);

            // create distribution record

            LocalCmDistributionRecord cmDistributionRecord = cmDistributionRecordHome.create(DR_LN, DR_CLSS, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), DR_DBT, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);

            cmFundTransfer.addCmDistributionRecord(cmDistributionRecord);
            glChartOfAccount.addCmDistributionRecord(cmDistributionRecord);

        } catch (FinderException ex) {

            throw new GlobalBranchAccountNumberInvalidException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void executeCmFtPost(Integer FT_CODE, String USR_NM, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException {

        Debug.print("CmFundTransferEntryControllerBean executeCmFtPost");

        LocalCmFundTransfer cmFundTransfer = null;

        try {

            // validate if fund transfer is already deleted

            try {

                cmFundTransfer = cmFundTransferHome.findByPrimaryKey(FT_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if fund transfer is already posted or void

            if (cmFundTransfer.getFtPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();

            } else if (cmFundTransfer.getFtVoid() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyVoidException();
            }

            // post fund transfer

            if (cmFundTransfer.getFtVoid() == EJBCommon.FALSE && cmFundTransfer.getFtPosted() == EJBCommon.FALSE) {

                // update bank account from balance (decrease)

                LocalAdBankAccount adBankAccountFrom = adBankAccountHome.findByPrimaryKey(cmFundTransfer.getFtAdBaAccountFrom());

                try {

                    // find bankaccount balance before or equal receipt date

                    Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(cmFundTransfer.getFtDate(), cmFundTransfer.getFtAdBaAccountFrom(), "BOOK", companyCode);

                    if (!adBankAccountBalances.isEmpty()) {

                        // get last check

                        ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                        if (adBankAccountBalance.getBabDate().before(cmFundTransfer.getFtDate())) {

                            // create new balance

                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(cmFundTransfer.getFtDate(), adBankAccountBalance.getBabBalance() - cmFundTransfer.getFtAmount(), "BOOK", companyCode);

                            adBankAccountFrom.addAdBankAccountBalance(adNewBankAccountBalance);

                        } else { // equals to check date

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - cmFundTransfer.getFtAmount());
                        }

                    } else {

                        // create new balance

                        LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(cmFundTransfer.getFtDate(), (0 - cmFundTransfer.getFtAmount()), "BOOK", companyCode);

                        adBankAccountFrom.addAdBankAccountBalance(adNewBankAccountBalance);
                    }

                    // propagate to subsequent balances if necessary

                    adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(cmFundTransfer.getFtDate(), cmFundTransfer.getFtAdBaAccountFrom(), "BOOK", companyCode);

                    for (Object bankAccountBalance : adBankAccountBalances) {

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                        adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - cmFundTransfer.getFtAmount());
                    }

                } catch (Exception ex) {

                    ex.printStackTrace();
                }

                // update bank account to balance (increase)

                LocalAdBankAccount adBankAccountTo = adBankAccountHome.findByPrimaryKey(cmFundTransfer.getFtAdBaAccountTo());

                try {

                    // find bankaccount balance before or equal receipt date

                    Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(cmFundTransfer.getFtDate(), cmFundTransfer.getFtAdBaAccountTo(), "BOOK", companyCode);

                    // get convert amount to bank to currency
                    double CNVRSN_RT_FRM_TO = cmFundTransfer.getFtConversionRateFrom() / cmFundTransfer.getFtConversionRateTo();
                    double CNVRTD_AMNT = EJBCommon.roundIt(cmFundTransfer.getFtAmount() * CNVRSN_RT_FRM_TO, getGlFcPrecisionUnit(companyCode));

                    if (!adBankAccountBalances.isEmpty()) {

                        // get last check

                        ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                        if (adBankAccountBalance.getBabDate().before(cmFundTransfer.getFtDate())) {

                            // create new balance

                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(cmFundTransfer.getFtDate(), adBankAccountBalance.getBabBalance() + CNVRTD_AMNT, "BOOK", companyCode);

                            adBankAccountTo.addAdBankAccountBalance(adNewBankAccountBalance);

                        } else { // equals to check date

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + CNVRTD_AMNT);
                        }

                    } else {

                        // create new balance

                        LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(cmFundTransfer.getFtDate(), (CNVRTD_AMNT), "BOOK", companyCode);

                        adBankAccountTo.addAdBankAccountBalance(adNewBankAccountBalance);
                    }

                    // propagate to subsequent balances if necessary

                    adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(cmFundTransfer.getFtDate(), cmFundTransfer.getFtAdBaAccountTo(), "BOOK", companyCode);

                    for (Object bankAccountBalance : adBankAccountBalances) {

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                        adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + CNVRTD_AMNT);
                    }

                } catch (Exception ex) {

                    ex.printStackTrace();
                }
            }

            // set post status

            cmFundTransfer.setFtPosted(EJBCommon.TRUE);
            cmFundTransfer.setFtPostedBy(USR_NM);
            cmFundTransfer.setFtDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            // post to gl if necessary

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            if (adPreference.getPrfCmGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                // validate if date has no period and period is closed

                LocalGlSetOfBook glJournalSetOfBook = null;

                try {

                    glJournalSetOfBook = glSetOfBookHome.findByDate(cmFundTransfer.getFtDate(), companyCode);

                } catch (FinderException ex) {

                    throw new GlJREffectiveDateNoPeriodExistException();
                }

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), cmFundTransfer.getFtDate(), companyCode);

                if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P') {

                    throw new GlJREffectiveDatePeriodClosedException();
                }

                // check if invoice is balance if not check suspense posting

                LocalGlJournalLine glOffsetJournalLine = null;

                Collection cmDistributionRecords = cmDistributionRecordHome.findByDrReversalAndDrImportedAndFtCode(EJBCommon.FALSE, EJBCommon.FALSE, cmFundTransfer.getFtCode(), companyCode);

                Iterator j = cmDistributionRecords.iterator();

                double TOTAL_DEBIT = 0d;
                double TOTAL_CREDIT = 0d;

                while (j.hasNext()) {

                    LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) j.next();

                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(cmFundTransfer.getFtAdBaAccountFrom());

                    double DR_AMNT = this.convertForeignToFunctionalCurrency(adBankAccount.getGlFunctionalCurrency().getFcCode(), adBankAccount.getGlFunctionalCurrency().getFcName(), cmFundTransfer.getFtConversionDate(), cmFundTransfer.getFtConversionRateFrom(), cmDistributionRecord.getDrAmount(), companyCode);

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

                        glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("CASH MANAGEMENT", "FUND TRANSFERS", companyCode);

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

                    glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " FUND TRANSFERS", branchCode, companyCode);

                } catch (FinderException ex) {
                }

                if (adPreference.getPrfEnableGlJournalBatch() == EJBCommon.TRUE && glJournalBatch == null) {

                    glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " FUND TRANSFERS", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, branchCode, companyCode);
                }

                // create journal entry

                LocalGlJournal glJournal = glJournalHome.create(cmFundTransfer.getFtReferenceNumber(), cmFundTransfer.getFtMemo(), cmFundTransfer.getFtDate(), 0.0d, null, cmFundTransfer.getFtDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM, EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, EJBCommon.FALSE, null, branchCode, companyCode);

                LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("CASH MANAGEMENT", companyCode);
                glJournal.setGlJournalSource(glJournalSource);

                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

                LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("FUND TRANSFERS", companyCode);
                glJournal.setGlJournalCategory(glJournalCategory);

                if (glJournalBatch != null) {

                    glJournal.setGlJournalBatch(glJournalBatch);
                }

                // create journal lines

                j = cmDistributionRecords.iterator();

                while (j.hasNext()) {

                    LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) j.next();

                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(cmFundTransfer.getFtAdBaAccountFrom());

                    double DR_AMNT = this.convertForeignToFunctionalCurrency(adBankAccount.getGlFunctionalCurrency().getFcCode(), adBankAccount.getGlFunctionalCurrency().getFcName(), cmFundTransfer.getFtConversionDate(), cmFundTransfer.getFtConversionRateFrom(), cmDistributionRecord.getDrAmount(), companyCode);

                    LocalGlJournalLine glJournalLine = glJournalLineHome.create(cmDistributionRecord.getDrLine(), cmDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

                    cmDistributionRecord.getGlChartOfAccount().addGlJournalLine(glJournalLine);

                    glJournal.addGlJournalLine(glJournalLine);

                    cmDistributionRecord.setDrImported(EJBCommon.TRUE);

                    LocalAdBankAccount adBankAccountTo = adBankAccountHome.findByPrimaryKey(cmFundTransfer.getFtAdBaAccountTo());

                    // for FOREX revaluation
                    if (((!Objects.equals(adBankAccount.getGlFunctionalCurrency().getFcCode(), adCompany.getGlFunctionalCurrency().getFcCode())) && glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null && (glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode().equals(adBankAccount.getGlFunctionalCurrency().getFcCode()))) || ((!Objects.equals(adBankAccountTo.getGlFunctionalCurrency().getFcCode(), adCompany.getGlFunctionalCurrency().getFcCode())) && glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null && (glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode().equals(adBankAccountTo.getGlFunctionalCurrency().getFcCode())))) {

                        double CONVERSION_RATE = 1;

                        if (glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode().equals(adBankAccount.getGlFunctionalCurrency().getFcCode()) && cmFundTransfer.getFtConversionRateFrom() != 0 && cmFundTransfer.getFtConversionRateFrom() != 1) {

                            CONVERSION_RATE = cmFundTransfer.getFtConversionRateFrom();

                        } else if (glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode().equals(adBankAccount.getGlFunctionalCurrency().getFcCode()) && cmFundTransfer.getFtConversionRateTo() != 0 && cmFundTransfer.getFtConversionRateTo() != 1) {

                            CONVERSION_RATE = cmFundTransfer.getFtConversionRateTo();

                        } else if (cmFundTransfer.getFtConversionDate() != null) {

                            CONVERSION_RATE = this.getFrRateByFrNameAndFrDate(glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), companyCode);
                        }

                        Collection glForexLedgers = null;

                        try {

                            glForexLedgers = glForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(cmFundTransfer.getFtDate(), glJournalLine.getGlChartOfAccount().getCoaCode(), companyCode);

                        } catch (FinderException ex) {

                        }

                        LocalGlForexLedger glForexLedger = (glForexLedgers.isEmpty() || glForexLedgers == null) ? null : (LocalGlForexLedger) glForexLedgers.iterator().next();

                        int FRL_LN = (glForexLedger != null && glForexLedger.getFrlDate().compareTo(cmFundTransfer.getFtDate()) == 0) ? glForexLedger.getFrlLine() + 1 : 1;

                        // compute balance
                        double COA_FRX_BLNC = glForexLedger == null ? 0 : glForexLedger.getFrlBalance();
                        double FRL_AMNT = cmDistributionRecord.getDrAmount();

                        if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET"))
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                        else FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);

                        COA_FRX_BLNC = COA_FRX_BLNC + FRL_AMNT;

                        glForexLedger = glForexLedgerHome.create(cmFundTransfer.getFtDate(), FRL_LN, "OTH", FRL_AMNT, CONVERSION_RATE, COA_FRX_BLNC, 0d, companyCode);

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
                Iterator i = glJournalLines.iterator();

                while (i.hasNext()) {

                    LocalGlJournalLine glJournalLine = (LocalGlJournalLine) i.next();

                    // post current to current acv

                    this.postToGl(glAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), true, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), branchCode, companyCode);

                    // post to subsequent acvs (propagate)

                    Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), glAccountingCalendarValue.getAcvPeriodNumber(), companyCode);

                    for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {

                        LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;

                        this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), branchCode, companyCode);
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

                                    this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), branchCode, companyCode);

                                } else { // revenue & expense

                                    this.postToGl(glSubsequentAccountingCalendarValue, glRetainedEarningsAccount, false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), branchCode, companyCode);
                                }
                            }

                            if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) break;
                        }
                    }
                }

                Collection cmFundTransferReceipts = cmFundTransfer.getCmFundTransferReceipts();

                i = cmFundTransferReceipts.iterator();

                while (i.hasNext()) {

                    LocalCmFundTransferReceipt cmFundTransferReceipt = (LocalCmFundTransferReceipt) i.next();

                    LocalArReceipt arReceipt = cmFundTransferReceipt.getArReceipt();

                    arReceipt.setRctLock(EJBCommon.FALSE);
                }
            }

        } catch (GlJREffectiveDateNoPeriodExistException | GlobalTransactionAlreadyVoidException |
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

        Debug.print("CmFundTransferEntryControllerBean convertForeignToFunctionalCurrency");

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

    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double JL_AMNT, Integer branchCode, Integer companyCode) {

        Debug.print("CmFundTransferEntryControllerBean postToGl");

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

    private LocalAdApprovalQueue createApprovalQueue(Integer branchCode, Integer companyCode, LocalAdApprovalQueueHome adApprovalQueueHome, LocalCmFundTransfer cmFundTransfer, LocalAdAmountLimit adAmountLimit, LocalAdApprovalUser adApprovalUser) throws CreateException {

        return adApprovalQueueHome.AqForApproval(EJBCommon.TRUE).AqDocument("CM FUND TRANSFER").AqDocumentCode(cmFundTransfer.getFtCode()).AqDocumentNumber(cmFundTransfer.getFtDocumentNumber()).AqDate(cmFundTransfer.getFtDate()).AqAndOr(adAmountLimit.getCalAndOr()).AqUserOr(adApprovalUser.getAuOr()).AqAdBranch(branchCode).AqAdCompany(companyCode).buildApprovalQueue();
    }

    public void ejbCreate() throws CreateException {

        Debug.print("CmFundTransferEntryControllerBean ejbCreate");
    }
}