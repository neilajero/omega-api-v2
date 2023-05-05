/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArReceiptBatchSubmitControllerBean
 * @created May 18, 2004, 6:07 PM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ar.LocalArCustomerBalanceHome;
import com.ejb.dao.ar.LocalArCustomerHome;
import com.ejb.dao.ar.LocalArDistributionRecordHome;
import com.ejb.dao.ar.LocalArReceiptHome;
import com.ejb.dao.cm.LocalCmAdjustmentHome;
import com.ejb.dao.cm.LocalCmDistributionRecordHome;
import com.ejb.dao.gl.*;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.entities.ar.*;
import com.ejb.entities.cm.LocalCmAdjustment;
import com.ejb.entities.cm.LocalCmDistributionRecord;
import com.ejb.entities.gl.*;
import com.ejb.entities.inv.*;
import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.ejb.txnreports.inv.InvRepItemCostingController;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.ad.AdBranchDetails;
import com.util.cm.CmAdjustmentDetails;
import com.util.mod.ar.ArModReceiptDetails;

import jakarta.ejb.*;
import java.util.*;

@Stateless(name = "ArReceiptBatchSubmitControllerEJB")
public class ArReceiptBatchSubmitControllerBean extends EJBContextClass implements ArReceiptBatchSubmitController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    public InvRepItemCostingController ejbRIC;
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
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdAmountLimitHome adAmountLimitHome;
    @EJB
    private LocalAdApprovalUserHome adApprovalUserHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
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
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalAdBankAccountBalanceHome adBankAccountBalanceHome;
    @EJB
    private LocalGlForexLedgerHome glForexLedgerHome;
    @EJB
    private LocalCmAdjustmentHome cmAdjustmentHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalCmDistributionRecordHome cmDistributionRecordHome;
    @EJB
    private LocalArCustomerBalanceHome arCustomerBalanceHome;
    @EJB
    private LocalInvAdjustmentLineHome invAdjustmentLineHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private LocalInvDistributionRecordHome invDistributionRecordHome;
    @EJB
    private LocalInvAdjustmentHome invAdjustmentHome;

    public byte getAdPrfEnableArReceiptBatch(Integer companyCode) {

        Debug.print("ArReceiptBatchSubmitControllerBean getAdPrfEnableArReceiptBatch");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfEnableArReceiptBatch();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArRctByCriteria(HashMap criteria, String ORDER_BY, Integer OFFSET, Integer LIMIT, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArReceiptBatchSubmitControllerBean getArRctByCriteria");
        try {

            ArrayList rctList = new ArrayList();

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(rct) FROM ArReceipt rct ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj = new Object[criteriaSize];

            if (criteria.containsKey("bankAccount")) {

                firstArgument = false;

                jbossQl.append("WHERE rct.adBankAccount.baName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("bankAccount");
                ctr++;
            }

            if (criteria.containsKey("currency")) {

                firstArgument = false;

                jbossQl.append("WHERE rct.glFunctionalCurrency.fcName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("currency");
                ctr++;
            }

            if (criteria.containsKey("batchName")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.arReceiptBatch.rbName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("batchName");
                ctr++;
            }

            if (criteria.containsKey("customerCode")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.arCustomer.cstCustomerCode=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerCode");
                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.rctDate>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateFrom");
                ctr++;
            }

            if (criteria.containsKey("dateTo")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.rctDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (criteria.containsKey("receiptNumberFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.rctNumber>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("receiptNumberFrom");
                ctr++;
            }

            if (criteria.containsKey("receiptNumberTo")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.rctNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("receiptNumberTo");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("rct.rctApprovalStatus IS NULL AND rct.rctPosted = 0 AND rct.rctVoid = 0 AND rct.rctAdBranch=").append(branchCode).append(" AND rct.rctAdCompany=").append(companyCode).append(" ");

            String orderBy = null;

            switch (ORDER_BY) {
                case "BANK ACCOUNT":

                    orderBy = "rct.adBankAccount.baName";

                    break;
                case "CUSTOMER CODE":

                    orderBy = "rct.arCustomer.cstCustomerCode";

                    break;
                case "CHECK NUMBER":

                    orderBy = "rct.rctNumber";
                    break;
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy).append(", rct.rctDate");

            } else {

                jbossQl.append("ORDER BY rct.rctDate");
            }

            Collection arReceipts = arReceiptHome.getRctByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

            if (arReceipts.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }

            for (Object receipt : arReceipts) {

                LocalArReceipt arReceipt = (LocalArReceipt) receipt;

                ArModReceiptDetails mdetails = new ArModReceiptDetails();
                mdetails.setRctCode(arReceipt.getRctCode());
                mdetails.setRctDate(arReceipt.getRctDate());
                mdetails.setRctNumber(arReceipt.getRctNumber());
                mdetails.setRctReferenceNumber(arReceipt.getRctReferenceNumber());
                mdetails.setRctAmount(arReceipt.getRctAmount());
                mdetails.setRctVoid(arReceipt.getRctVoid());
                mdetails.setRctCstCustomerCode(arReceipt.getArCustomer().getCstCustomerCode());
                mdetails.setRctBaName(arReceipt.getAdBankAccount().getBaName());
                mdetails.setRctEnableAdvancePayment(arReceipt.getRctEnableAdvancePayment());
                mdetails.setRctExcessAmount(arReceipt.getRctExcessAmount());

                if (!arReceipt.getArInvoiceLineItems().isEmpty()) {

                    mdetails.setRctType("ITEMS");

                } else {

                    mdetails.setRctType("MEMO LINES");
                }

                rctList.add(mdetails);
            }

            return rctList;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public void executeArRctBatchSubmit(Integer RCT_CODE, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalTransactionAlreadyVoidPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ArReceiptBatchSubmitControllerBean executeArRctBatchSubmit");
        try {

            LocalArReceipt arReceipt = null;

            // validate if receipt is already deleted

            try {

                arReceipt = arReceiptHome.findByPrimaryKey(RCT_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate receipt

            if (arReceipt.getRctApprovalStatus() != null) {

                if (arReceipt.getRctApprovalStatus().equals("APPROVED") || arReceipt.getRctApprovalStatus().equals("N/A")) {

                    throw new GlobalTransactionAlreadyApprovedException();

                } else if (arReceipt.getRctApprovalStatus().equals("PENDING")) {

                    throw new GlobalTransactionAlreadyPendingException();
                }
            }

            if (arReceipt.getRctPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();
            }

            // generate approval status

            String RCT_APPRVL_STATUS = null;

            LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);

            // check if ar receipt approval is enabled

            if (adApproval.getAprEnableArReceipt() == EJBCommon.FALSE) {

                RCT_APPRVL_STATUS = "N/A";

            } else {

                // check if receipt is self approved

                LocalAdAmountLimit adAmountLimit = null;

                try {

                    adAmountLimit = adAmountLimitHome.findByAdcTypeAndAuTypeAndUsrName("AR RECEIPT", "REQUESTER", arReceipt.getRctLastModifiedBy(), companyCode);

                } catch (FinderException ex) {

                    throw new GlobalNoApprovalRequesterFoundException();
                }

                if (arReceipt.getRctAmount() <= adAmountLimit.getCalAmountLimit()) {

                    RCT_APPRVL_STATUS = "N/A";

                } else {

                    // for approval, create approval queue

                    Collection adAmountLimits = adAmountLimitHome.findByAdcTypeAndGreaterThanCalAmountLimit("AR RECEIPT", adAmountLimit.getCalAmountLimit(), companyCode);

                    if (adAmountLimits.isEmpty()) {

                        Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adAmountLimit.getCalCode(), companyCode);

                        if (adApprovalUsers.isEmpty()) {

                            throw new GlobalNoApprovalApproverFoundException();
                        }

                        for (Object approvalUser : adApprovalUsers) {

                            LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                            LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(branchCode, companyCode, EJBCommon.FALSE, adApprovalQueueHome, arReceipt.getRctCode(), arReceipt.getRctNumber(), arReceipt.getRctDate(), adAmountLimit, adApprovalUser);

                            adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                        }

                    } else {

                        boolean isApprovalUsersFound = false;

                        Iterator i = adAmountLimits.iterator();

                        while (i.hasNext()) {

                            LocalAdAmountLimit adNextAmountLimit = (LocalAdAmountLimit) i.next();

                            if (arReceipt.getRctAmount() <= adNextAmountLimit.getCalAmountLimit()) {

                                Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adAmountLimit.getCalCode(), companyCode);

                                for (Object approvalUser : adApprovalUsers) {

                                    isApprovalUsersFound = true;

                                    LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                    LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(branchCode, companyCode, EJBCommon.FALSE, adApprovalQueueHome, arReceipt.getRctCode(), arReceipt.getRctNumber(), arReceipt.getRctDate(), adAmountLimit, adApprovalUser);

                                    adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                                }

                                break;

                            } else if (!i.hasNext()) {

                                Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("AR RECEIPT", adNextAmountLimit.getCalCode(), companyCode);

                                for (Object approvalUser : adApprovalUsers) {

                                    isApprovalUsersFound = true;

                                    LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                    LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(branchCode, companyCode, EJBCommon.FALSE, adApprovalQueueHome, arReceipt.getRctCode(), arReceipt.getRctNumber(), arReceipt.getRctDate(), adAmountLimit, adApprovalUser);

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

                    RCT_APPRVL_STATUS = "PENDING";
                }
            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            // start date validation

            if (arReceipt != null || arReceipt.getRctType().equals("MISC")) {

                Collection arInvoiceLineItems = arReceipt.getArInvoiceLineItems();

                for (Object invoiceLineItem : arInvoiceLineItems) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) invoiceLineItem;
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(arReceipt.getRctDate(), arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName(), arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName(), branchCode, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                        }
                    }
                }
            }

            if (RCT_APPRVL_STATUS != null && RCT_APPRVL_STATUS.equals("N/A") && adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                // create cm adjustment for inserting advance payment
                if (arReceipt.getRctEnableAdvancePayment() != 0) {

                    this.postAdvncPymntByCmAdj(arReceipt, arReceipt.getAdBankAccount().getBaName(), branchCode, companyCode);
                }

                this.executeArRctPost(arReceipt.getRctCode(), arReceipt.getRctLastModifiedBy(), branchCode, companyCode);
            }

            // set receipt approval status

            arReceipt.setRctApprovalStatus(RCT_APPRVL_STATUS);

        } catch (GlobalRecordAlreadyDeletedException | AdPRFCoaGlVarianceAccountNotFoundException |
                 GlobalBranchAccountNumberInvalidException | GlobalInventoryDateException |
                 GlobalJournalNotBalanceException | GlJREffectiveDatePeriodClosedException |
                 GlJREffectiveDateNoPeriodExistException | GlobalTransactionAlreadyVoidPostedException |
                 GlobalNoApprovalApproverFoundException | GlobalNoApprovalRequesterFoundException |
                 GlobalTransactionAlreadyPostedException | GlobalTransactionAlreadyPendingException |
                 GlobalTransactionAlreadyApprovedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer companyCode) {

        Debug.print("ArReceiptBatchSubmitControllerBean getGlFcPrecisionUnit");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods
    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer companyCode) {

        Debug.print("ArReceiptBatchSubmitControllerBean convertForeignToFunctionalCurrency");
        // get company and extended precision
        LocalAdCompany adCompany = null;
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

    private void executeArRctPost(Integer RCT_CODE, String USR_NM, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ArReceiptBatchSubmitControllerBean executeArRctPost");
        LocalArReceipt arReceipt = null;
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            // validate if receipt is already deleted

            try {

                arReceipt = arReceiptHome.findByPrimaryKey(RCT_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if receipt is already posted

            if (arReceipt.getRctVoid() == EJBCommon.FALSE && arReceipt.getRctPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();

                // validate if receipt void is already posted

            } else if (arReceipt.getRctVoid() == EJBCommon.TRUE && arReceipt.getRctVoidPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyVoidPostedException();
            }

            if (arReceipt.getRctVoid() == EJBCommon.FALSE && arReceipt.getRctType().equals("MISC") && !arReceipt.getArInvoiceLineItems().isEmpty()) {

                // regenerate inventory dr

                this.regenerateInventoryDr(arReceipt, branchCode, companyCode);
            }

            // post receipt

            if (arReceipt.getRctVoid() == EJBCommon.FALSE && arReceipt.getRctPosted() == EJBCommon.FALSE) {

                if (arReceipt.getRctType().equals("COLLECTION")) {

                    double RCT_CRDTS = 0d;

                    // increase amount paid in invoice payment schedules and invoice

                    Collection arAppliedInvoices = arReceipt.getArAppliedInvoices();

                    for (Object appliedInvoice : arAppliedInvoices) {

                        LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) appliedInvoice;

                        LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = arAppliedInvoice.getArInvoicePaymentSchedule();

                        double AMOUNT_PAID = arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiCreditableWTax() + arAppliedInvoice.getAiDiscountAmount();

                        RCT_CRDTS += this.convertForeignToFunctionalCurrency(arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcCode(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcName(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(), arAppliedInvoice.getAiDiscountAmount() + arAppliedInvoice.getAiCreditableWTax(), companyCode);

                        arInvoicePaymentSchedule.setIpsAmountPaid(EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsAmountPaid() + AMOUNT_PAID, this.getGlFcPrecisionUnit(companyCode)));

                        arInvoicePaymentSchedule.getArInvoice().setInvAmountPaid(EJBCommon.roundIt(arInvoicePaymentSchedule.getArInvoice().getInvAmountPaid() + AMOUNT_PAID, this.getGlFcPrecisionUnit(companyCode)));

                        // release invoice lock

                        arInvoicePaymentSchedule.setIpsLock(EJBCommon.FALSE);
                    }

                    // decrease customer balance

                    double RCT_AMNT = this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmount(), companyCode);

                    this.post(arReceipt.getRctDate(), (RCT_AMNT + RCT_CRDTS) * -1, arReceipt.getArCustomer(), companyCode);

                } else if (arReceipt.getRctType().equals("MISC") && !arReceipt.getArInvoiceLineItems().isEmpty()) {

                    for (Object o : arReceipt.getArInvoiceLineItems()) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) o;

                        String II_NM = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName();
                        String LOC_NM = arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName();

                        double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                        LocalInvCosting invCosting = null;

                        try {

                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arReceipt.getRctDate(), II_NM, LOC_NM, branchCode, companyCode);

                        } catch (FinderException ex) {

                        }

                        double COST = 0d;

                        if (invCosting == null) {

                            COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                            this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), QTY_SLD, QTY_SLD * COST, -QTY_SLD, -(QTY_SLD * COST), 0d, null, branchCode, companyCode);

                        } else {

                            switch (invCosting.getInvItemLocation().getInvItem().getIiCostMethod()) {
                                case "Average":
                                    COST = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                                    this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), QTY_SLD, QTY_SLD * COST, invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (QTY_SLD * COST), 0d, null, branchCode, companyCode);

                                    break;
                                case "FIFO":

                                    double fifoCost = this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), -QTY_SLD, arInvoiceLineItem.getIliUnitPrice(), true, branchCode, companyCode);

                                    // post entries to database
                                    this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), QTY_SLD, fifoCost * QTY_SLD, invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (fifoCost * QTY_SLD), 0d, null, branchCode, companyCode);

                                    break;
                                case "Standard":

                                    double standardCost = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();

                                    // post entries to database
                                    this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), QTY_SLD, standardCost * QTY_SLD, invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (standardCost * QTY_SLD), 0d, null, branchCode, companyCode);
                                    break;
                            }
                        }
                    }
                }

                // increase bank balance CASH
                if (arReceipt.getRctAmountCash() > 0) {

                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccount().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCash(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCash());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCash()=" + arReceipt.getRctAmountCash());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (arReceipt.getRctAmountCash()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCash());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // increase bank balance CARD 1
                if (arReceipt.getRctAmountCard1() > 0) {

                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccountCard1().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard1().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard1(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard1());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCard1()=" + arReceipt.getRctAmountCard1());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (arReceipt.getRctAmountCard1()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard1().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard1());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // increase bank balance CARD 2
                if (arReceipt.getRctAmountCard2() > 0) {
                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccountCard2().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard2().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard2(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard2());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCard2()=" + arReceipt.getRctAmountCard2());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (arReceipt.getRctAmountCard2()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard2().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard2());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // increase bank balance CARD 3
                if (arReceipt.getRctAmountCard3() > 0) {
                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccountCard3().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard3().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard3(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard3());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCard3()=" + arReceipt.getRctAmountCard3());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (arReceipt.getRctAmountCard3()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard3().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard3());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }
                // increase bank balance CHEQUE
                if (arReceipt.getRctAmountCheque() > 0) {
                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccount().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCheque(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCheque());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCheque()=" + arReceipt.getRctAmountCheque());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (arReceipt.getRctAmountCheque()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCheque());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // set receipt post status

                arReceipt.setRctPosted(EJBCommon.TRUE);
                arReceipt.setRctPostedBy(USR_NM);
                arReceipt.setRctDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
            }

            // post to gl if necessary

            if (adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                // validate if date has no period and period is closed

                LocalGlSetOfBook glJournalSetOfBook = null;

                try {

                    glJournalSetOfBook = glSetOfBookHome.findByDate(arReceipt.getRctDate(), companyCode);

                } catch (FinderException ex) {

                    throw new GlJREffectiveDateNoPeriodExistException();
                }

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), arReceipt.getRctDate(), companyCode);

                if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P') {

                    throw new GlJREffectiveDatePeriodClosedException();
                }

                // check if invoice is balance if not check suspense posting

                LocalGlJournalLine glOffsetJournalLine = null;

                Collection arDistributionRecords = null;

                if (arReceipt.getRctVoid() == EJBCommon.FALSE) {

                    arDistributionRecords = arDistributionRecordHome.findImportableDrByDrReversedAndRctCode(EJBCommon.FALSE, arReceipt.getRctCode(), companyCode);

                } else {

                    arDistributionRecords = arDistributionRecordHome.findImportableDrByDrReversedAndRctCode(EJBCommon.TRUE, arReceipt.getRctCode(), companyCode);
                }

                Iterator j = arDistributionRecords.iterator();

                double TOTAL_DEBIT = 0d;
                double TOTAL_CREDIT = 0d;

                while (j.hasNext()) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) j.next();

                    double DR_AMNT = 0d;

                    if (arDistributionRecord.getArAppliedInvoice() != null) {

                        LocalArInvoice arInvoice = arDistributionRecord.getArAppliedInvoice().getArInvoicePaymentSchedule().getArInvoice();

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), companyCode);

                    } else {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arDistributionRecord.getDrAmount(), companyCode);
                    }

                    if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

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

                        glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("ACCOUNTS RECEIVABLES", "SALES RECEIPTS", companyCode);

                    } catch (FinderException ex) {

                        throw new GlobalJournalNotBalanceException();
                    }

                    if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                        glOffsetJournalLine = glJournalLineHome.create((short) (arDistributionRecords.size() + 1), EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);

                    } else {

                        glOffsetJournalLine = glJournalLineHome.create((short) (arDistributionRecords.size() + 1), EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
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

                    if (adPreference.getPrfEnableArReceiptBatch() == EJBCommon.TRUE) {

                        glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " " + arReceipt.getArReceiptBatch().getRbName(), branchCode, companyCode);

                    } else {

                        glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " SALES RECEIPTS", branchCode, companyCode);
                    }

                } catch (FinderException ex) {
                }

                if (adPreference.getPrfEnableGlJournalBatch() == EJBCommon.TRUE && glJournalBatch == null) {

                    if (adPreference.getPrfEnableArInvoiceBatch() == EJBCommon.TRUE) {

                        glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " " + arReceipt.getArReceiptBatch().getRbName(), "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, branchCode, companyCode);

                    } else {

                        glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " SALES RECEIPTS", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, branchCode, companyCode);
                    }
                }

                // create journal entry

                LocalGlJournal glJournal = glJournalHome.create(arReceipt.getRctReferenceNumber(), arReceipt.getRctDescription(), arReceipt.getRctDate(), 0.0d, null, arReceipt.getRctNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM, EJBCommon.getGcCurrentDateWoTime().getTime(), arReceipt.getArCustomer().getCstTin(), arReceipt.getArCustomer().getCstName(), EJBCommon.FALSE, null, branchCode, companyCode);

                LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("ACCOUNTS RECEIVABLES", companyCode);
                glJournal.setGlJournalSource(glJournalSource);

                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

                LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("SALES RECEIPTS", companyCode);
                glJournal.setGlJournalCategory(glJournalCategory);

                if (glJournalBatch != null) {

                    glJournal.setGlJournalBatch(glJournalBatch);
                }

                // create journal lines

                j = arDistributionRecords.iterator();
                boolean firstFlag = true;

                while (j.hasNext()) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) j.next();

                    double DR_AMNT = 0d;
                    LocalArInvoice arInvoice = null;

                    if (arDistributionRecord.getArAppliedInvoice() != null) {

                        arInvoice = arDistributionRecord.getArAppliedInvoice().getArInvoicePaymentSchedule().getArInvoice();

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), companyCode);

                    } else {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arDistributionRecord.getDrAmount(), companyCode);
                    }

                    LocalGlJournalLine glJournalLine = glJournalLineHome.create(arDistributionRecord.getDrLine(), arDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

                    arDistributionRecord.getGlChartOfAccount().addGlJournalLine(glJournalLine);

                    glJournal.addGlJournalLine(glJournalLine);

                    arDistributionRecord.setDrImported(EJBCommon.TRUE);

                    // for FOREX revaluation

                    int FC_CODE = arDistributionRecord.getArAppliedInvoice() != null ? arInvoice.getGlFunctionalCurrency().getFcCode() : arReceipt.getGlFunctionalCurrency().getFcCode();

                    if ((FC_CODE != adCompany.getGlFunctionalCurrency().getFcCode()) && glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null && (FC_CODE == glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode())) {

                        double CONVERSION_RATE = arDistributionRecord.getArAppliedInvoice() != null ? arInvoice.getInvConversionRate() : arReceipt.getRctConversionRate();

                        Date DATE = arDistributionRecord.getArAppliedInvoice() != null ? arInvoice.getInvConversionDate() : arReceipt.getRctConversionDate();

                        if (DATE != null && (CONVERSION_RATE == 0 || CONVERSION_RATE == 1)) {

                            CONVERSION_RATE = this.getFrRateByFrNameAndFrDate(glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), companyCode);

                        } else if (CONVERSION_RATE == 0) {

                            CONVERSION_RATE = 1;
                        }

                        Collection glForexLedgers = null;

                        DATE = arDistributionRecord.getArAppliedInvoice() != null ? arInvoice.getInvDate() : arReceipt.getRctDate();

                        try {

                            glForexLedgers = glForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(DATE, glJournalLine.getGlChartOfAccount().getCoaCode(), companyCode);

                        } catch (FinderException ex) {

                        }

                        LocalGlForexLedger glForexLedger = (glForexLedgers.isEmpty() || glForexLedgers == null) ? null : (LocalGlForexLedger) glForexLedgers.iterator().next();

                        int FRL_LN = (glForexLedger != null && glForexLedger.getFrlDate().compareTo(DATE) == 0) ? glForexLedger.getFrlLine() + 1 : 1;

                        // compute balance
                        double COA_FRX_BLNC = glForexLedger == null ? 0 : glForexLedger.getFrlBalance();
                        double FRL_AMNT = arDistributionRecord.getDrAmount();

                        if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET")) {
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                        } else {
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);
                        }

                        COA_FRX_BLNC = COA_FRX_BLNC + FRL_AMNT;

                        double FRX_GN_LSS = 0d;

                        if (glOffsetJournalLine != null && firstFlag) {

                            if (glOffsetJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET")) {
                                FRX_GN_LSS = (glOffsetJournalLine.getJlDebit() == EJBCommon.TRUE ? glOffsetJournalLine.getJlAmount() : (-1 * glOffsetJournalLine.getJlAmount()));
                            } else {
                                FRX_GN_LSS = (glOffsetJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * glOffsetJournalLine.getJlAmount()) : glOffsetJournalLine.getJlAmount());
                            }

                            firstFlag = false;
                        }

                        glForexLedger = glForexLedgerHome.create(DATE, FRL_LN, "OR", FRL_AMNT, CONVERSION_RATE, COA_FRX_BLNC, FRX_GN_LSS, companyCode);

                        glJournalLine.getGlChartOfAccount().addGlForexLedger(glForexLedger);

                        // propagate balances
                        try {

                            glForexLedgers = glForexLedgerHome.findByGreaterThanFrlDateAndCoaCode(glForexLedger.getFrlDate(), glForexLedger.getGlChartOfAccount().getCoaCode(), glForexLedger.getFrlAdCompany());

                        } catch (FinderException ex) {

                        }

                        for (Object forexLedger : glForexLedgers) {

                            glForexLedger = (LocalGlForexLedger) forexLedger;
                            FRL_AMNT = arDistributionRecord.getDrAmount();

                            if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET")) {
                                FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                            } else {
                                FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);
                            }

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

                            if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) {
                                break;
                            }
                        }
                    }
                }

                if (arReceipt.getArCustomer().getApSupplier() != null) {

                    // Post Investors Account balance
                    String supplierClass = arReceipt.getArCustomer().getApSupplier().getApSupplierClass().getScName();
                    // post current to current acv
                    if (supplierClass.contains("Investors")) {

                        Debug.print(glAccountingCalendarValue + " 1");
                        Debug.print(arReceipt.getArCustomer().getApSupplier() + " 2");
                        this.postToGlInvestor(glAccountingCalendarValue, arReceipt.getArCustomer().getApSupplier(), true, arReceipt.getRctInvtrBeginningBalance(), (byte) 0, arReceipt.getRctAmount(), companyCode);

                        // post to subsequent acvs (propagate)

                        Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), glAccountingCalendarValue.getAcvPeriodNumber(), companyCode);

                        for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {

                            LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;

                            this.postToGlInvestor(glSubsequentAccountingCalendarValue, arReceipt.getArCustomer().getApSupplier(), false, (byte) 0, (byte) 0, arReceipt.getRctAmount(), companyCode);
                        }

                        // post to subsequent years if necessary

                        Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode);

                        if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                            adCompany = adCompanyHome.findByPrimaryKey(companyCode);

                            for (Object subsequentSetOfBook : glSubsequentSetOfBooks) {

                                LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) subsequentSetOfBook;

                                // post to subsequent acvs of subsequent set of book(propagate)

                                Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                                for (Object accountingCalendarValue : glAccountingCalendarValues) {

                                    LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                                    this.postToGlInvestor(glSubsequentAccountingCalendarValue, arReceipt.getArCustomer().getApSupplier(), false, arReceipt.getRctInvtrBeginningBalance(), (byte) 0, arReceipt.getRctAmount(), companyCode);
                                }

                                if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }

        } catch (GlJREffectiveDateNoPeriodExistException | AdPRFCoaGlVarianceAccountNotFoundException |
                 GlobalBranchAccountNumberInvalidException | GlobalTransactionAlreadyVoidPostedException |
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

    private void postAdvncPymntByCmAdj(LocalArReceipt arReceipt, String BA_NM, Integer branchCode, Integer companyCode) throws Exception {

        Debug.print("ArReceiptBatchSubmitControllerBean saveCmAdjEntry");

        CmAdjustmentDetails details = new CmAdjustmentDetails();

        details.setAdjCode(null);
        details.setAdjType("ADVANCE");
        details.setAdjDate(arReceipt.getRctDate());
        details.setAdjDocumentNumber("");
        details.setAdjReferenceNumber(arReceipt.getRctNumber());
        details.setAdjAmount(arReceipt.getRctExcessAmount());
        details.setAdjConversionDate(arReceipt.getRctConversionDate());
        details.setAdjConversionRate(arReceipt.getRctConversionRate());
        details.setAdjMemo("");
        details.setAdjVoid(arReceipt.getRctVoid());

        details.setAdjCreatedBy(arReceipt.getRctCreatedBy());
        details.setAdjDateCreated(new java.util.Date());

        details.setAdjLastModifiedBy(arReceipt.getRctLastModifiedBy());
        details.setAdjDateLastModified(new java.util.Date());

        /// validate document number is unique

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

        // validate if conversion date exists

        try {

            if (details.getAdjConversionDate() != null) {

                LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);

                if (!glValidateFunctionalCurrency.getFcName().equals("USD")) {

                    LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(), details.getAdjConversionDate(), companyCode);

                } else if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                    LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), details.getAdjConversionDate(), companyCode);
                }
            }

        } catch (FinderException ex) {

            throw new GlobalConversionDateNotExistException();
        }

        LocalCmAdjustment cmAdjustment = null;
        try {

            cmAdjustment = cmAdjustmentHome.create(details.getAdjType(), details.getAdjDate(), details.getAdjDocumentNumber(), details.getAdjReferenceNumber(), details.getAdjCheckNumber(), details.getAdjAmount(), 0d, details.getAdjConversionDate(), details.getAdjConversionRate(), details.getAdjMemo(), null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, 0d, null, EJBCommon.FALSE, null, null, EJBCommon.FALSE, details.getAdjCreatedBy(), details.getAdjDateCreated(), details.getAdjLastModifiedBy(), details.getAdjDateLastModified(), null, null, null, null, null, branchCode, companyCode);

            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(BA_NM, companyCode);
            adBankAccount.addCmAdjustment(cmAdjustment);

            // remove all distribution records
            Debug.print("flag 3");
            Collection cmDistributionRecords = cmAdjustment.getCmDistributionRecords();

            Iterator i = cmDistributionRecords.iterator();

            while (i.hasNext()) {

                LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) i.next();

                i.remove();

                // cmDistributionRecord.entityRemove();
                em.remove(cmDistributionRecord);
            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "ADJUSTMENT", EJBCommon.TRUE, cmAdjustment.getAdjAmount(), adBankAccount.getBaCoaGlAdjustmentAccount(), cmAdjustment, branchCode, companyCode);

            this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "CASH", EJBCommon.FALSE, cmAdjustment.getAdjAmount(), adPreference.getPrfArGlCoaCustomerDepositAccount(), cmAdjustment, branchCode, companyCode);

            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(arReceipt.getArCustomer().getCstCustomerCode(), companyCode);

            arCustomer.addCmAdjustment(cmAdjustment);

            String ADJ_APPRVL_STATUS = "N/A";

            this.executeCmAdjPost(cmAdjustment.getAdjCode(), cmAdjustment.getAdjLastModifiedBy(), branchCode, companyCode);
        } catch (Exception ex) {
            throw ex;
        }
    }

    private void executeCmAdjPost(Integer ADJ_CODE, String USR_NM, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException {

        Debug.print("ArReceiptBatchSubmitControllerBean executeCmAdjPost");
        LocalCmAdjustment cmAdjustment = null;
        try {

            // validate if adjustment is already deleted

            try {

                cmAdjustment = cmAdjustmentHome.findByPrimaryKey(ADJ_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if adjustment is already posted or void

            if (cmAdjustment.getAdjPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();

            } else if (cmAdjustment.getAdjVoid() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyVoidException();
            }

            // post adjustment

            if (cmAdjustment.getAdjVoid() == EJBCommon.FALSE && cmAdjustment.getAdjPosted() == EJBCommon.FALSE) {

                if (cmAdjustment.getAdjType().equals("INTEREST") || cmAdjustment.getAdjType().equals("DEBIT MEMO")) {

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
            }

            // set adjcher post status

            cmAdjustment.setAdjPosted(EJBCommon.TRUE);
            cmAdjustment.setAdjPostedBy(USR_NM);
            cmAdjustment.setAdjDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

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

                        if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET")) {
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                        } else {
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);
                        }

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

                            if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET")) {
                                FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                            } else {
                                FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);
                            }

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

                            if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) {
                                break;
                            }
                        }
                    }
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

    private double getInvFifoCost(Date CST_DT, Integer IL_CODE, double CST_QTY, double CST_COST, boolean isAdjustFifo, Integer branchCode, Integer companyCode) {

        try {

            Collection invFifoCostings = invCostingHome.findFifoRemainingQuantityByLessThanOrEqualCstDateAndIlCodeAndBrCode(CST_DT, IL_CODE, branchCode, companyCode);

            if (invFifoCostings.size() > 0) {

                Iterator x = invFifoCostings.iterator();

                if (isAdjustFifo) {

                    // executed during POST transaction

                    double totalCost = 0d;
                    double cost;

                    if (CST_QTY < 0) {

                        // for negative quantities
                        double neededQty = -(CST_QTY);

                        while (x.hasNext() && neededQty != 0) {

                            LocalInvCosting invFifoCosting = (LocalInvCosting) x.next();

                            if (invFifoCosting.getApPurchaseOrderLine() != null || invFifoCosting.getApVoucherLineItem() != null) {
                                cost = invFifoCosting.getCstItemCost() / invFifoCosting.getCstQuantityReceived();
                            } else if (invFifoCosting.getArInvoiceLineItem() != null) {
                                cost = invFifoCosting.getCstCostOfSales() / invFifoCosting.getCstQuantitySold();
                            } else {
                                cost = invFifoCosting.getCstAdjustCost() / invFifoCosting.getCstAdjustQuantity();
                            }

                            if (neededQty <= invFifoCosting.getCstRemainingLifoQuantity()) {

                                invFifoCosting.setCstRemainingLifoQuantity(invFifoCosting.getCstRemainingLifoQuantity() - neededQty);
                                totalCost += (neededQty * cost);
                                neededQty = 0d;
                            } else {

                                neededQty -= invFifoCosting.getCstRemainingLifoQuantity();
                                totalCost += (invFifoCosting.getCstRemainingLifoQuantity() * cost);
                                invFifoCosting.setCstRemainingLifoQuantity(0);
                            }
                        }

                        // if needed qty is not yet satisfied but no more quantities to fetch, get the
                        // default
                        // cost
                        if (neededQty != 0) {

                            LocalInvItemLocation invItemLocation = invItemLocationHome.findByPrimaryKey(IL_CODE);
                            totalCost += (neededQty * invItemLocation.getInvItem().getIiUnitCost());
                        }

                        cost = totalCost / -CST_QTY;
                    } else {

                        // for positive quantities
                        cost = CST_COST;
                    }
                    return cost;
                } else {

                    // executed during ENTRY transaction

                    LocalInvCosting invFifoCosting = (LocalInvCosting) x.next();

                    if (invFifoCosting.getApPurchaseOrderLine() != null || invFifoCosting.getApVoucherLineItem() != null) {
                        return EJBCommon.roundIt(invFifoCosting.getCstItemCost() / invFifoCosting.getCstQuantityReceived(), this.getGlFcPrecisionUnit(companyCode));
                    } else if (invFifoCosting.getArInvoiceLineItem() != null) {
                        return EJBCommon.roundIt(invFifoCosting.getCstCostOfSales() / invFifoCosting.getCstQuantitySold(), this.getGlFcPrecisionUnit(companyCode));
                    } else {
                        return EJBCommon.roundIt(invFifoCosting.getCstAdjustCost() / invFifoCosting.getCstAdjustQuantity(), this.getGlFcPrecisionUnit(companyCode));
                    }
                }
            } else {

                // most applicable in 1st entries of data
                LocalInvItemLocation invItemLocation = invItemLocationHome.findByPrimaryKey(IL_CODE);
                return invItemLocation.getInvItem().getIiUnitCost();
            }

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void post(Date RCT_DT, double RCT_AMNT, LocalArCustomer arCustomer, Integer companyCode) {

        Debug.print("ArReceiptBatchSubmitControllerBean post");
        try {

            // find customer balance before or equal invoice date

            Collection arCustomerBalances = arCustomerBalanceHome.findByBeforeOrEqualInvDateAndCstCustomerCode(RCT_DT, arCustomer.getCstCustomerCode(), companyCode);

            if (!arCustomerBalances.isEmpty()) {

                // get last invoice

                ArrayList arCustomerBalanceList = new ArrayList(arCustomerBalances);

                LocalArCustomerBalance arCustomerBalance = (LocalArCustomerBalance) arCustomerBalanceList.get(arCustomerBalanceList.size() - 1);

                if (arCustomerBalance.getCbDate().before(RCT_DT)) {

                    // create new balance

                    LocalArCustomerBalance arNewCustomerBalance = arCustomerBalanceHome.create(RCT_DT, arCustomerBalance.getCbBalance() + RCT_AMNT, companyCode);

                    arCustomer.addArCustomerBalance(arNewCustomerBalance);

                } else { // equals to invoice date

                    arCustomerBalance.setCbBalance(arCustomerBalance.getCbBalance() + RCT_AMNT);
                }

            } else {

                // create new balance

                LocalArCustomerBalance arNewCustomerBalance = arCustomerBalanceHome.create(RCT_DT, RCT_AMNT, companyCode);

                arCustomer.addArCustomerBalance(arNewCustomerBalance);
            }

            // propagate to subsequent balances if necessary

            arCustomerBalances = arCustomerBalanceHome.findByAfterInvDateAndCstCustomerCode(RCT_DT, arCustomer.getCstCustomerCode(), companyCode);

            for (Object customerBalance : arCustomerBalances) {

                LocalArCustomerBalance arCustomerBalance = (LocalArCustomerBalance) customerBalance;

                arCustomerBalance.setCbBalance(arCustomerBalance.getCbBalance() + RCT_AMNT);
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double JL_AMNT, Integer companyCode) {

        Debug.print("ArReceiptBatchSubmitControllerBean postToGl");
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

    private void postToInv(LocalArInvoiceLineItem arInvoiceLineItem, Date CST_DT, double CST_QTY_SLD, double CST_CST_OF_SLS, double CST_RMNNG_QTY, double CST_RMNNG_VL, double CST_VRNC_VL, String USR_NM, Integer branchCode, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ArReceiptBatchSubmitControllerBean postToInv");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalInvItemLocation invItemLocation = arInvoiceLineItem.getInvItemLocation();
            int CST_LN_NMBR = 0;

            CST_QTY_SLD = EJBCommon.roundIt(CST_QTY_SLD, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_CST_OF_SLS = EJBCommon.roundIt(CST_CST_OF_SLS, adCompany.getGlFunctionalCurrency().getFcPrecision());
            CST_RMNNG_QTY = EJBCommon.roundIt(CST_RMNNG_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_RMNNG_VL = EJBCommon.roundIt(CST_RMNNG_VL, adCompany.getGlFunctionalCurrency().getFcPrecision());

            if (CST_QTY_SLD > 0) {

                invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - CST_QTY_SLD);
            }

            try {

                // generate line number
                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            } catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            // void subsequent cost variance adjustments
            Collection invAdjustmentLines = invAdjustmentLineHome.findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(CST_DT, invItemLocation.getIlCode(), branchCode, companyCode);
            Iterator i = invAdjustmentLines.iterator();

            while (i.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();
                this.voidInvAdjustment(invAdjustmentLine.getInvAdjustment(), branchCode, companyCode);
            }

            // create costing
            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d, 0d, 0d, CST_QTY_SLD, CST_CST_OF_SLS, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, 0d, branchCode, companyCode);
            invItemLocation.addInvCosting(invCosting);
            invCosting.setArInvoiceLineItem(arInvoiceLineItem);

            invCosting.setCstQuantity(CST_QTY_SLD * -1);
            invCosting.setCstCost(CST_CST_OF_SLS * -1);

            // if cost variance is not 0, generate cost variance for the transaction
            if (CST_VRNC_VL != 0) {

                this.generateCostVariance(invCosting.getInvItemLocation(), CST_VRNC_VL, "ARMR" + arInvoiceLineItem.getArReceipt().getRctNumber(), arInvoiceLineItem.getArReceipt().getRctDescription(), arInvoiceLineItem.getArReceipt().getRctDate(), USR_NM, branchCode, companyCode);
            }

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);

            i = invCostings.iterator();

            while (i.hasNext()) {

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) i.next();

                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() - CST_QTY_SLD);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() - CST_CST_OF_SLS);
            }

            if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                // regenerate cost variance
                this.regenerateCostVariance(invCostings, invCosting, branchCode, companyCode);
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, double QTY_SLD, Integer companyCode) {

        Debug.print("ArReceiptBatchSubmitControllerBean convertByUomFromAndItemAndQuantity");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), companyCode);

            return EJBCommon.roundIt(QTY_SLD * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), adPreference.getPrfInvQuantityPrecisionUnit());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void regenerateInventoryDr(LocalArReceipt arReceipt, Integer branchCode, Integer companyCode) throws GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException {

        Debug.print("ArReceiptBatchSubmitControllerBean regenerateInventoryDr");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            // regenerate inventory distribution records

            Collection arDistributionRecords = null;

            if (arReceipt.getRctVoid() == EJBCommon.FALSE) {

                arDistributionRecords = arDistributionRecordHome.findImportableDrByDrReversedAndRctCode(EJBCommon.FALSE, arReceipt.getRctCode(), companyCode);

            } else {

                arDistributionRecords = arDistributionRecordHome.findImportableDrByDrReversedAndRctCode(EJBCommon.TRUE, arReceipt.getRctCode(), companyCode);
            }

            Iterator i = arDistributionRecords.iterator();

            while (i.hasNext()) {

                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

                if (arDistributionRecord.getDrClass().equals("COGS") || arDistributionRecord.getDrClass().equals("INVENTORY")) {

                    i.remove();
                    // arDistributionRecord.entityRemove();
                    em.remove(arDistributionRecord);
                }
            }

            Collection arInvoiceLineItems = arReceipt.getArInvoiceLineItems();

            if (arInvoiceLineItems != null && !arInvoiceLineItems.isEmpty()) {

                i = arInvoiceLineItems.iterator();

                while (i.hasNext()) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) i.next();
                    LocalInvItemLocation invItemLocation = arInvoiceLineItem.getInvItemLocation();
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(arReceipt.getRctDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }

                    // add cost of sales distribution and inventory

                    double COST = 0d;

                    try {

                        LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arReceipt.getRctDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);

                        // check if rmning vl is not zero and rmng qty is 0
                        Debug.print("COGS--------------------------------------->");
                        Debug.print("invCosting.getCstRemainingQuantity()=" + invCosting.getCstRemainingQuantity());
                        Debug.print("invCosting.getCstRemainingValue()=" + invCosting.getCstRemainingValue());

                        if (invCosting.getCstRemainingQuantity() <= 0 && invCosting.getCstRemainingValue() <= 0) {
                            Debug.print("RE CALC");
                            HashMap criteria = new HashMap();
                            criteria.put("itemName", invItemLocation.getInvItem().getIiName());
                            criteria.put("location", invItemLocation.getInvLocation().getLocName());

                            ArrayList branchList = new ArrayList();

                            AdBranchDetails mdetails = new AdBranchDetails();
                            mdetails.setBrCode(branchCode);
                            branchList.add(mdetails);

                            ejbRIC.executeInvFixItemCosting(criteria, branchList, companyCode);

                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arReceipt.getRctDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                        }

                        switch (invCosting.getInvItemLocation().getInvItem().getIiCostMethod()) {
                            case "Average":
                                COST = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                                break;
                            case "FIFO":
                                COST = Math.abs(this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), arInvoiceLineItem.getIliQuantity(), arInvoiceLineItem.getIliUnitPrice(), false, branchCode, companyCode));
                                break;
                            case "Standard":
                                COST = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                                break;
                        }

                    } catch (FinderException ex) {

                        COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();
                    }

                    double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {

                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arInvoiceLineItem.getInvItemLocation().getIlCode(), branchCode, companyCode);

                    } catch (FinderException ex) {

                    }

                    if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiClass().equals("Stock") && arInvoiceLineItem.getInvItemLocation().getInvItem().getIiNonInventoriable() == EJBCommon.FALSE) {

                        if (adBranchItemLocation != null) {

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(), "COGS", EJBCommon.TRUE, COST * QTY_SLD, adBranchItemLocation.getBilCoaGlCostOfSalesAccount(), arReceipt, branchCode, companyCode);

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(), "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD, adBranchItemLocation.getBilCoaGlInventoryAccount(), arReceipt, branchCode, companyCode);

                        } else {

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(), "COGS", EJBCommon.TRUE, COST * QTY_SLD, arInvoiceLineItem.getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arReceipt, branchCode, companyCode);

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(), "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD, arInvoiceLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(), arReceipt, branchCode, companyCode);
                        }

                        // add quantity to item location committed quantity

                        double convertedQuantity = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);
                        invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() + convertedQuantity);
                    }

                    // add inventory sale distributions

                }
            }

        } catch (GlobalInventoryDateException | GlobalBranchAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void addArDrIliEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE, LocalArReceipt arReceipt, Integer branchCode, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArReceiptBatchSubmitControllerBean addArDrIliEntry");
        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, branchCode, companyCode);

            // create distribution record

            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, EJBCommon.FALSE, companyCode);

            arReceipt.addArDistributionRecord(arDistributionRecord);
            glChartOfAccount.addArDistributionRecord(arDistributionRecord);

        } catch (FinderException ex) {

            throw new GlobalBranchAccountNumberInvalidException(ex.getMessage());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double getFrRateByFrNameAndFrDate(String FC_NM, Date CONVERSION_DATE, Integer companyCode) throws GlobalConversionDateNotExistException {

        Debug.print("ArReceiptBatchSubmitControllerBean getFrRateByFrNameAndFrDate");
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

    private void voidInvAdjustment(LocalInvAdjustment invAdjustment, Integer branchCode, Integer companyCode) {

        Debug.print("ArReceiptBatchSubmitControllerBean voidInvAdjustment");
        try {

            Collection invDistributionRecords = invAdjustment.getInvDistributionRecords();
            ArrayList list = new ArrayList();

            Iterator i = invDistributionRecords.iterator();

            while (i.hasNext()) {

                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) i.next();

                list.add(invDistributionRecord);
            }

            i = list.iterator();

            while (i.hasNext()) {

                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) i.next();

                this.addInvDrEntry(invAdjustment.getInvDrNextLine(), invDistributionRecord.getDrClass(), invDistributionRecord.getDrDebit() == EJBCommon.TRUE ? EJBCommon.FALSE : EJBCommon.TRUE, invDistributionRecord.getDrAmount(), EJBCommon.TRUE, invDistributionRecord.getInvChartOfAccount().getCoaCode(), invAdjustment, branchCode, companyCode);
            }

            Collection invAdjustmentLines = invAdjustment.getInvAdjustmentLines();
            i = invAdjustmentLines.iterator();
            list.clear();

            while (i.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();

                list.add(invAdjustmentLine);
            }

            i = list.iterator();

            while (i.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();

                this.addInvAlEntry(invAdjustmentLine.getInvItemLocation(), invAdjustment, (invAdjustmentLine.getAlUnitCost()) * -1, EJBCommon.TRUE, companyCode);
            }

            invAdjustment.setAdjVoid(EJBCommon.TRUE);

            this.executeInvAdjPost(invAdjustment.getAdjCode(), invAdjustment.getAdjLastModifiedBy(), branchCode, companyCode);

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void generateCostVariance(LocalInvItemLocation invItemLocation, double CST_VRNC_VL, String ADJ_RFRNC_NMBR, String ADJ_DSCRPTN, Date ADJ_DT, String USR_NM, Integer branchCode, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ArReceiptBatchSubmitControllerBean generateCostVariance");
    }

    private void regenerateCostVariance(Collection invCostings, LocalInvCosting invCosting, Integer branchCode, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ArReceiptBatchSubmitControllerBean regenerateCostVariance");
    }

    private void addInvDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, byte DR_RVRSL, Integer COA_CODE, LocalInvAdjustment invAdjustment, Integer branchCode, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArReceiptBatchSubmitControllerBean addInvDrEntry");
        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate coa

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, branchCode, companyCode);

            } catch (FinderException ex) {

                throw new GlobalBranchAccountNumberInvalidException();
            }

            // create distribution record

            LocalInvDistributionRecord invDistributionRecord = invDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), DR_RVRSL, EJBCommon.FALSE, companyCode);

            invAdjustment.addInvDistributionRecord(invDistributionRecord);
            glChartOfAccount.addInvDistributionRecord(invDistributionRecord);

        } catch (GlobalBranchAccountNumberInvalidException ex) {

            throw new GlobalBranchAccountNumberInvalidException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void executeInvAdjPost(Integer ADJ_CODE, String USR_NM, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException {

        Debug.print("ArReceiptBatchSubmitControllerBean executeInvAdjPost");

        try {

            // validate if adjustment is already deleted

            LocalInvAdjustment invAdjustment = null;

            try {

                invAdjustment = invAdjustmentHome.findByPrimaryKey(ADJ_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if adjustment is already posted or void

            if (invAdjustment.getAdjPosted() == EJBCommon.TRUE) {

                if (invAdjustment.getAdjVoid() != EJBCommon.TRUE) {
                    throw new GlobalTransactionAlreadyPostedException();
                }
            }

            Collection invAdjustmentLines = null;

            if (invAdjustment.getAdjVoid() == EJBCommon.FALSE) {
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.FALSE, invAdjustment.getAdjCode(), companyCode);
            } else {
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.TRUE, invAdjustment.getAdjCode(), companyCode);
            }

            Iterator i = invAdjustmentLines.iterator();

            while (i.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();

                LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(invAdjustment.getAdjDate(), invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(), invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName(), branchCode, companyCode);

                this.postInvAdjustmentToInventory(invAdjustmentLine, invAdjustment.getAdjDate(), 0, invAdjustmentLine.getAlUnitCost(), invCosting.getCstRemainingQuantity(), invCosting.getCstRemainingValue() + invAdjustmentLine.getAlUnitCost(), branchCode, companyCode);
            }

            // post to gl if necessary

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate if date has no period and period is closed

            LocalGlSetOfBook glJournalSetOfBook = null;

            try {

                glJournalSetOfBook = glSetOfBookHome.findByDate(invAdjustment.getAdjDate(), companyCode);

            } catch (FinderException ex) {

                throw new GlJREffectiveDateNoPeriodExistException();
            }

            LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), invAdjustment.getAdjDate(), companyCode);

            if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P') {

                throw new GlJREffectiveDatePeriodClosedException();
            }

            // check if invoice is balance if not check suspense posting

            LocalGlJournalLine glOffsetJournalLine = null;

            Collection invDistributionRecords = null;

            if (invAdjustment.getAdjVoid() == EJBCommon.FALSE) {

                invDistributionRecords = invDistributionRecordHome.findImportableDrByDrReversedAndAdjCode(EJBCommon.FALSE, invAdjustment.getAdjCode(), companyCode);

            } else {

                invDistributionRecords = invDistributionRecordHome.findImportableDrByDrReversedAndAdjCode(EJBCommon.TRUE, invAdjustment.getAdjCode(), companyCode);
            }

            Iterator j = invDistributionRecords.iterator();

            double TOTAL_DEBIT = 0d;
            double TOTAL_CREDIT = 0d;

            while (j.hasNext()) {

                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) j.next();

                double DR_AMNT = 0d;

                DR_AMNT = invDistributionRecord.getDrAmount();

                if (invDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

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

                    glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("INVENTORY", "INVENTORY ADJUSTMENTS", companyCode);

                } catch (FinderException ex) {

                    throw new GlobalJournalNotBalanceException();
                }

                if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                    glOffsetJournalLine = glJournalLineHome.create((short) (invDistributionRecords.size() + 1), EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);

                } else {

                    glOffsetJournalLine = glJournalLineHome.create((short) (invDistributionRecords.size() + 1), EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
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

                glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " INVENTORY ADJUSTMENTS", branchCode, companyCode);

            } catch (FinderException ex) {

            }

            if (glJournalBatch == null) {

                glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " INVENTORY ADJUSTMENTS", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, branchCode, companyCode);
            }

            // create journal entry

            LocalGlJournal glJournal = glJournalHome.create(invAdjustment.getAdjReferenceNumber(), invAdjustment.getAdjDescription(), invAdjustment.getAdjDate(), 0.0d, null, invAdjustment.getAdjDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM, EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, EJBCommon.FALSE, null, branchCode, companyCode);

            LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("INVENTORY", companyCode);
            glJournal.setGlJournalSource(glJournalSource);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
            glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("INVENTORY ADJUSTMENTS", companyCode);
            glJournal.setGlJournalCategory(glJournalCategory);

            if (glJournalBatch != null) {

                glJournal.setGlJournalBatch(glJournalBatch);
            }

            // create journal lines

            j = invDistributionRecords.iterator();

            while (j.hasNext()) {

                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) j.next();

                double DR_AMNT = 0d;

                DR_AMNT = invDistributionRecord.getDrAmount();

                LocalGlJournalLine glJournalLine = glJournalLineHome.create(invDistributionRecord.getDrLine(), invDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

                invDistributionRecord.getInvChartOfAccount().addGlJournalLine(glJournalLine);

                glJournal.addGlJournalLine(glJournalLine);

                invDistributionRecord.setDrImported(EJBCommon.TRUE);
            }

            if (glOffsetJournalLine != null) {

                glJournal.addGlJournalLine(glOffsetJournalLine);
            }

            // post journal to gl
            Collection glJournalLines = glJournalLineHome.findByJrCode(glJournal.getJrCode(), companyCode);
            i = glJournalLines.iterator();

            while (i.hasNext()) {

                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) i.next();

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
                    LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(adCompany.getCmpRetainedEarnings(), branchCode, companyCode);

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

                        if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) {
                            break;
                        }
                    }
                }
            }

            invAdjustment.setAdjPosted(EJBCommon.TRUE);

        } catch (GlJREffectiveDateNoPeriodExistException | GlobalTransactionAlreadyPostedException |
                 GlobalRecordAlreadyDeletedException | GlobalJournalNotBalanceException |
                 GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalInvAdjustmentLine addInvAlEntry(LocalInvItemLocation invItemLocation, LocalInvAdjustment invAdjustment, double CST_VRNC_VL, byte AL_VD, Integer companyCode) {

        Debug.print("ArReceiptBatchSubmitControllerBean addInvAlEntry");
        try {

            // create dr entry
            LocalInvAdjustmentLine invAdjustmentLine = null;
            invAdjustmentLine = invAdjustmentLineHome.create(CST_VRNC_VL, null, null, 0, 0, AL_VD, companyCode);

            // map adjustment, unit of measure, item location
            invAdjustment.addInvAdjustmentLine(invAdjustmentLine);
            invItemLocation.getInvItem().getInvUnitOfMeasure().addInvAdjustmentLine(invAdjustmentLine);
            invItemLocation.addInvAdjustmentLine(invAdjustmentLine);

            return invAdjustmentLine;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postInvAdjustmentToInventory(LocalInvAdjustmentLine invAdjustmentLine, Date CST_DT, double CST_ADJST_QTY, double CST_ADJST_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, Integer branchCode, Integer companyCode) {

        Debug.print("ArReceiptBatchSubmitControllerBean postInvAdjustmentToInventory");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalInvItemLocation invItemLocation = invAdjustmentLine.getInvItemLocation();
            int CST_LN_NMBR = 0;

            CST_ADJST_QTY = EJBCommon.roundIt(CST_ADJST_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_ADJST_CST = EJBCommon.roundIt(CST_ADJST_CST, adCompany.getGlFunctionalCurrency().getFcPrecision());
            CST_RMNNG_QTY = EJBCommon.roundIt(CST_RMNNG_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_RMNNG_VL = EJBCommon.roundIt(CST_RMNNG_VL, adCompany.getGlFunctionalCurrency().getFcPrecision());

            if (CST_ADJST_QTY < 0) {

                invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - Math.abs(CST_ADJST_QTY));
            }

            // create costing

            try {

                // generate line number

                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            } catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d, CST_ADJST_QTY, CST_ADJST_CST, 0d, 0d, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, CST_ADJST_QTY > 0 ? CST_ADJST_QTY : 0, branchCode, companyCode);
            invItemLocation.addInvCosting(invCosting);
            invCosting.setInvAdjustmentLine(invAdjustmentLine);

            invCosting.setCstQuantity(CST_ADJST_QTY);
            invCosting.setCstCost(CST_ADJST_CST);

            // propagate balance if necessary

            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);

            for (Object costing : invCostings) {

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) costing;

                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_ADJST_QTY);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_ADJST_CST);
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToGlInvestor(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalApSupplier apSupplier, boolean isCurrentAcv, byte isBeginningBalance, byte isDebit, double JL_AMNT, Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean postToGlInvestor");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlInvestorAccountBalance glInvestorAccountBalance = glInvestorAccountBalanceHome.findByAcvCodeAndSplCode(glAccountingCalendarValue.getAcvCode(), apSupplier.getSplCode(), companyCode);

            short FC_EXTNDD_PRCSN = adCompany.getGlFunctionalCurrency().getFcPrecision();

            glInvestorAccountBalance.setIrabEndingBalance(EJBCommon.roundIt(glInvestorAccountBalance.getIrabEndingBalance() + JL_AMNT, FC_EXTNDD_PRCSN));

            if (!isCurrentAcv) {

                glInvestorAccountBalance.setIrabBeginningBalance(EJBCommon.roundIt(glInvestorAccountBalance.getIrabBeginningBalance() + JL_AMNT, FC_EXTNDD_PRCSN));
            }

            if (isCurrentAcv) {

                glInvestorAccountBalance.setIrabTotalCredit(EJBCommon.roundIt(glInvestorAccountBalance.getIrabTotalCredit() + JL_AMNT, FC_EXTNDD_PRCSN));
            }

            if (isBeginningBalance != 0) {
                glInvestorAccountBalance.setIrabBonus(EJBCommon.TRUE);
                glInvestorAccountBalance.setIrabInterest(EJBCommon.TRUE);
            } else {
                glInvestorAccountBalance.setIrabBonus(EJBCommon.FALSE);
                glInvestorAccountBalance.setIrabInterest(EJBCommon.FALSE);
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalAdApprovalQueue createApprovalQueue(Integer branchCode, Integer companyCode, byte isInvoice, LocalAdApprovalQueueHome adApprovalQueueHome, Integer documentCode, String documentNumber, Date documentDate, LocalAdAmountLimit adAmountLimit, LocalAdApprovalUser adApprovalUser) throws CreateException {

        String[] document = {"AR INVOICE", "AR RECEIPT"};
        String selectedDocument = (isInvoice == EJBCommon.TRUE) ? document[0] : document[1];
        return adApprovalQueueHome.AqForApproval(EJBCommon.TRUE).AqDocument(selectedDocument).AqDocumentCode(documentCode).AqDocumentNumber(documentNumber).AqDate(documentDate).AqAndOr(adAmountLimit.getCalAndOr()).AqUserOr(adApprovalUser.getAuOr()).AqAdBranch(branchCode).AqAdCompany(companyCode).buildApprovalQueue();

    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ArReceiptBatchSubmitControllerBean ejbCreate");
    }

}