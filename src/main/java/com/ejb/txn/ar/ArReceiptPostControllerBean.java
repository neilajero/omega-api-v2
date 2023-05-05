/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArReceiptPostControllerBean
 * @created March 11, 2004, 9:21 AM
 * @author Dennis M. Hilario
 */
package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ar.LocalArCustomerBalanceHome;
import com.ejb.dao.ar.LocalArDistributionRecordHome;
import com.ejb.dao.ar.LocalArReceiptHome;
import com.ejb.dao.gl.*;
import com.ejb.dao.inv.LocalInvCostingHome;
import com.ejb.dao.inv.LocalInvItemHome;
import com.ejb.dao.inv.LocalInvItemLocationHome;
import com.ejb.dao.inv.LocalInvUnitOfMeasureConversionHome;
import com.ejb.entities.ad.*;
import com.ejb.entities.ar.*;
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
import com.util.EJBHomeFactory;
import com.util.ad.AdBranchDetails;
import com.util.mod.ar.ArModReceiptDetails;

import jakarta.ejb.*;
import javax.naming.NamingException;
import java.util.*;

@Stateless(name = "ArReceiptPostControllerEJB")
public class ArReceiptPostControllerBean extends EJBContextClass implements ArReceiptPostController {

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
    private LocalAdBankAccountBalanceHome adBankAccountBalanceHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private LocalArCustomerBalanceHome arCustomerBalanceHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
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
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;


    public byte getAdPrfEnableArReceiptBatch(Integer companyCode) {

        Debug.print("ArReceiptPostControllerBean getAdPrfEnableArReceiptBatch");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfEnableArReceiptBatch();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArRctPostableByCriteria(HashMap criteria, String ORDER_BY, Integer OFFSET, Integer LIMIT, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArReceiptPostControllerBean getArRctPostableByCriteria");
        try {

            ArrayList rctList = new ArrayList();

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(rct) FROM ArReceipt rct ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size() - 1;

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

            if (criteria.containsKey("receiptType")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.rctType=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("receiptType");
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

            byte receiptVoid = (Byte) criteria.get("receiptVoid");

            if (receiptVoid == EJBCommon.FALSE) {

                if (criteria.containsKey("approvalStatus")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("rct.rctApprovalStatus=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("approvalStatus");
                    ctr++;

                } else {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("(rct.rctApprovalStatus='APPROVED' OR rct.rctApprovalStatus='N/A') ");
                }

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.rctPosted = 0 AND rct.rctVoid = 0 ");

            } else {

                if (criteria.containsKey("approvalStatus")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("rct.rctApprovalStatus=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("approvalStatus");
                    ctr++;
                }

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("(rct.rctVoidApprovalStatus='APPROVED' OR rct.rctVoidApprovalStatus='N/A') AND rct.rctPosted = 1 AND rct.rctVoid = 1 AND rct.rctVoidPosted = 0");
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("rct.rctAdBranch=").append(branchCode).append(" AND rct.rctAdCompany=").append(companyCode).append(" ");

            String orderBy = null;

            switch (ORDER_BY) {
                case "BANK ACCOUNT":

                    orderBy = "rct.adBankAccount.baName";

                    break;
                case "CUSTOMER CODE":

                    orderBy = "rct.arCustomer.cstCustomerCode";

                    break;
                case "RECEIPT NUMBER":

                    orderBy = "rct.rctNumber";
                    break;
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy).append(", rct.rctDate");

            } else {

                jbossQl.append("ORDER BY rct.rctDate");
            }

            Debug.print("QL + " + jbossQl);

            Collection arReceipts = null;

            try {

                arReceipts = arReceiptHome.getRctByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

            } catch (Exception ex) {

                throw new EJBException(ex.getMessage());
            }

            if (arReceipts.isEmpty()) throw new GlobalNoRecordFoundException();

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
                mdetails.setRctExcessAmount(arReceipt.getRctExcessAmount());
                mdetails.setRctEnableAdvancePayment(arReceipt.getRctEnableAdvancePayment());

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

    public void executeArRctPost(Integer RCT_CODE, String USR_NM, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException {

        Debug.print("ArReceiptPostControllerBean executeArRctPost");

        LocalArReceipt arReceipt = null;
        Date txnStartDate = new Date();

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
            Collection arDepositReceipts = null;
            LocalArReceipt arDepositReceipt = null;
            LocalArCustomer arCustomer = arReceipt.getArCustomer();

            // post receipt

            if (arReceipt.getRctVoid() == EJBCommon.FALSE && arReceipt.getRctPosted() == EJBCommon.FALSE) {

                if (arReceipt.getRctType().equals("COLLECTION")) {

                    double RCT_CRDTS = 0d;
                    double RCT_AI_APPLD_DPSTS = 0d;
                    double RCT_EXCSS_AMNT = arReceipt.getRctExcessAmount();

                    // create adjustment for advance payment
                    if (arReceipt.getRctEnableAdvancePayment() != 0) {
                    }

                    // increase amount paid in invoice payment schedules and invoice

                    Collection arAppliedInvoices = arReceipt.getArAppliedInvoices();

                    for (Object appliedInvoice : arAppliedInvoices) {

                        LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) appliedInvoice;

                        LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = arAppliedInvoice.getArInvoicePaymentSchedule();

                        double AMOUNT_PAID = arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiCreditableWTax() + arAppliedInvoice.getAiDiscountAmount() + arAppliedInvoice.getAiAppliedDeposit() + arAppliedInvoice.getAiCreditBalancePaid() + arAppliedInvoice.getAiRebate();

                        double PENALTY_PAID = arAppliedInvoice.getAiPenaltyApplyAmount();

                        RCT_CRDTS += this.convertForeignToFunctionalCurrency(arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcCode(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcName(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(), arAppliedInvoice.getAiDiscountAmount() + arAppliedInvoice.getAiCreditableWTax(), companyCode);

                        RCT_AI_APPLD_DPSTS += this.convertForeignToFunctionalCurrency(arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcCode(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcName(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(), arAppliedInvoice.getAiAppliedDeposit(), companyCode);

                        arInvoicePaymentSchedule.setIpsAmountPaid(EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsAmountPaid() + AMOUNT_PAID, this.getGlFcPrecisionUnit(companyCode)));
                        arInvoicePaymentSchedule.setIpsPenaltyPaid(EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsPenaltyPaid() + PENALTY_PAID, this.getGlFcPrecisionUnit(companyCode)));

                        arInvoicePaymentSchedule.getArInvoice().setInvAmountPaid(EJBCommon.roundIt(arInvoicePaymentSchedule.getArInvoice().getInvAmountPaid() + AMOUNT_PAID, this.getGlFcPrecisionUnit(companyCode)));

                        arInvoicePaymentSchedule.getArInvoice().setInvPenaltyPaid(EJBCommon.roundIt(arInvoicePaymentSchedule.getArInvoice().getInvPenaltyPaid() + PENALTY_PAID, this.getGlFcPrecisionUnit(companyCode)));

                        // release invoice lock

                        arInvoicePaymentSchedule.setIpsLock(EJBCommon.FALSE);
                    }

                    // decrease customer balance

                    double RCT_AMNT = this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmount(), companyCode);

                    this.post(arReceipt.getRctDate(), (RCT_AMNT + RCT_CRDTS + RCT_AI_APPLD_DPSTS) * -1, arReceipt.getArCustomer(), companyCode);

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

                        double COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                        if (invCosting == null) {

                            this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), QTY_SLD, COST * QTY_SLD, -QTY_SLD, -COST * QTY_SLD, 0d, null, branchCode, companyCode);
                        } else {

                            switch (invCosting.getInvItemLocation().getInvItem().getIiCostMethod()) {
                                case "Average":

                                    double avgCost = invCosting.getCstRemainingQuantity() == 0 ? COST : Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                                    this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), QTY_SLD, avgCost * QTY_SLD, invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (avgCost * QTY_SLD), 0d, null, branchCode, companyCode);

                                    break;
                                case "FIFO":

                                    double fifoCost = invCosting.getCstRemainingQuantity() == 0 ? COST : this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), QTY_SLD, arInvoiceLineItem.getIliUnitPrice(), true, branchCode, companyCode);

                                    this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), QTY_SLD, fifoCost * QTY_SLD, invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (fifoCost * QTY_SLD), 0d, null, branchCode, companyCode);

                                    break;
                                case "Standard":

                                    double standardCost = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();

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

                // increase bank balance

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

                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmount(), "BOOK", companyCode);

                            adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);

                        } else { // equals to check date

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmount());
                        }

                    } else {

                        // create new balance

                        LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (arReceipt.getRctAmount()), "BOOK", companyCode);

                        adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                    }

                    // propagate to subsequent balances if necessary

                    adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                    for (Object bankAccountBalance : adBankAccountBalances) {

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                        adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmount());
                    }

                } catch (Exception ex) {

                    ex.printStackTrace();
                }

                // set receipt post status

                arReceipt.setRctPosted(EJBCommon.TRUE);
                arReceipt.setRctPostedBy(USR_NM);
                arReceipt.setRctDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            } else if (arReceipt.getRctVoid() == EJBCommon.TRUE && arReceipt.getRctVoidPosted() == EJBCommon.FALSE) {

                if (arReceipt.getRctType().equals("COLLECTION")) {

                    double RCT_CRDTS = 0d;
                    double RCT_AI_APPLD_DPSTS = 0d;

                    // decrease amount paid in invoice payment schedules and invoice

                    Collection arAppliedInvoices = arReceipt.getArAppliedInvoices();

                    for (Object appliedInvoice : arAppliedInvoices) {

                        LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) appliedInvoice;

                        LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = arAppliedInvoice.getArInvoicePaymentSchedule();

                        double AMOUNT_PAID = arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiCreditableWTax() + arAppliedInvoice.getAiDiscountAmount() + arAppliedInvoice.getAiAppliedDeposit() + arAppliedInvoice.getAiCreditBalancePaid() + arAppliedInvoice.getAiRebate();

                        double PENALTY_PAID = arAppliedInvoice.getAiPenaltyApplyAmount();

                        RCT_CRDTS += this.convertForeignToFunctionalCurrency(arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcCode(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcName(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(), arAppliedInvoice.getAiDiscountAmount() + arAppliedInvoice.getAiCreditableWTax(), companyCode);

                        RCT_AI_APPLD_DPSTS += this.convertForeignToFunctionalCurrency(arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcCode(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcName(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(), arAppliedInvoice.getAiAppliedDeposit(), companyCode);

                        try {

                            Collection arAppliedCredits = arAppliedInvoice.getArAppliedCredits();

                            Debug.print("arAppliedCredits=" + arAppliedCredits.size());
                            Iterator x = arAppliedCredits.iterator();

                            while (x.hasNext()) {

                                LocalArAppliedCredit arAppliedCredit = (LocalArAppliedCredit) x.next();

                                x.remove();

                                // arAppliedCredit.entityRemove();
                                em.remove(arAppliedCredit);
                            }

                        } catch (Exception ex) {

                        }

                        arInvoicePaymentSchedule.setIpsAmountPaid(EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsAmountPaid() - AMOUNT_PAID, this.getGlFcPrecisionUnit(companyCode)));
                        arInvoicePaymentSchedule.setIpsPenaltyPaid(EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsPenaltyPaid() - PENALTY_PAID, this.getGlFcPrecisionUnit(companyCode)));

                        arInvoicePaymentSchedule.getArInvoice().setInvAmountPaid(EJBCommon.roundIt(arInvoicePaymentSchedule.getArInvoice().getInvAmountPaid() - AMOUNT_PAID, this.getGlFcPrecisionUnit(companyCode)));
                        arInvoicePaymentSchedule.getArInvoice().setInvPenaltyPaid(EJBCommon.roundIt(arInvoicePaymentSchedule.getArInvoice().getInvPenaltyPaid() - PENALTY_PAID, this.getGlFcPrecisionUnit(companyCode)));
                    }

                    // increase customer balance

                    double RCT_AMNT = this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmount(), companyCode);

                    this.post(arReceipt.getRctDate(), RCT_AMNT + RCT_CRDTS + RCT_AI_APPLD_DPSTS, arReceipt.getArCustomer(), companyCode);

                    // set receipt post status
                    arReceipt.setRctVoidPosted(EJBCommon.TRUE);
                    arReceipt.setRctPostedBy(USR_NM);
                    arReceipt.setRctDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

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

                        double COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                        if (invCosting == null) {

                            this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), -QTY_SLD, -COST * QTY_SLD, QTY_SLD, COST * QTY_SLD, 0d, null, branchCode, companyCode);

                        } else {

                            switch (invCosting.getInvItemLocation().getInvItem().getIiCostMethod()) {
                                case "Average":

                                    double avgCost = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                                    this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), -QTY_SLD, -avgCost * QTY_SLD, invCosting.getCstRemainingQuantity() + QTY_SLD, invCosting.getCstRemainingValue() + (avgCost * QTY_SLD), 0d, USR_NM, branchCode, companyCode);

                                    break;
                                case "FIFO":

                                    double fifoCost = this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), QTY_SLD, arInvoiceLineItem.getIliUnitPrice(), true, branchCode, companyCode);

                                    this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), -QTY_SLD, -fifoCost * QTY_SLD, invCosting.getCstRemainingQuantity() + QTY_SLD, invCosting.getCstRemainingValue() + (fifoCost * QTY_SLD), 0d, USR_NM, branchCode, companyCode);

                                    break;
                                case "Standard":

                                    double standardCost = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();

                                    this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), -QTY_SLD, -standardCost * QTY_SLD, invCosting.getCstRemainingQuantity() + QTY_SLD, invCosting.getCstRemainingValue() + (standardCost * QTY_SLD), 0d, USR_NM, branchCode, companyCode);
                                    break;
                            }
                        }
                    }
                }

                // decrease bank balance cash

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

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCash(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCash());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCash()=" + arReceipt.getRctAmountCash());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (-arReceipt.getRctAmountCash()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCash());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // decrease bank balance CARD 1
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

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard1(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard1());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCard1()=" + arReceipt.getRctAmountCard1());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (-arReceipt.getRctAmountCard1()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard1().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard1());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // decrease bank balance CARD 2
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

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard2(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard2());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCard2()=" + arReceipt.getRctAmountCard2());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (-arReceipt.getRctAmountCard2()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard2().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard2());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // decrease bank balance CARD 3
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

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard3(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard3());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCard3()=" + arReceipt.getRctAmountCard3());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (-arReceipt.getRctAmountCard3()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard3().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard3());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }
                // decrease bank balance CHEQUE
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

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCheque(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCheque());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCheque()=" + arReceipt.getRctAmountCheque());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (-arReceipt.getRctAmountCheque()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCheque());
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

            if (adPreference.getPrfArGlPostingType().equals("USE SL POSTING")) {

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
                boolean firstFlag = true;

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
                    // glChartOfAccount.addGlJournalLine(glOffsetJournalLine);
                    glOffsetJournalLine.setGlChartOfAccount(glChartOfAccount);

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
                String customerName = arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName();

                LocalGlJournal glJournal = glJournalHome.create(arReceipt.getRctReferenceNumber(), arReceipt.getRctDescription(), arReceipt.getRctDate(), 0.0d, null, arReceipt.getRctNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM, EJBCommon.getGcCurrentDateWoTime().getTime(), arReceipt.getArCustomer().getCstTin(), customerName, EJBCommon.FALSE, null, branchCode, companyCode);

                LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("ACCOUNTS RECEIVABLES", companyCode);
                glJournal.setGlJournalSource(glJournalSource);

                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

                LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("SALES RECEIPTS", companyCode);
                glJournal.setGlJournalCategory(glJournalCategory);

                if (glJournalBatch != null) {

                    // glJournalBatch.addGlJournal(glJournal);
                    glJournal.setGlJournalBatch(glJournalBatch);
                }

                // create journal lines

                j = arDistributionRecords.iterator();

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

                    // arDistributionRecord.getGlChartOfAccount().addGlJournalLine(glJournalLine);
                    glJournalLine.setGlChartOfAccount(arDistributionRecord.getGlChartOfAccount());
                    // glJournal.addGlJournalLine(glJournalLine);
                    glJournalLine.setGlJournal(glJournal);

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

                        if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET"))
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                        else FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);

                        COA_FRX_BLNC = COA_FRX_BLNC + FRL_AMNT;

                        double FRX_GN_LSS = 0d;

                        if (glOffsetJournalLine != null && firstFlag) {

                            if (glOffsetJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET"))
                                FRX_GN_LSS = (glOffsetJournalLine.getJlDebit() == EJBCommon.TRUE ? glOffsetJournalLine.getJlAmount() : (-1 * glOffsetJournalLine.getJlAmount()));
                            else
                                FRX_GN_LSS = (glOffsetJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * glOffsetJournalLine.getJlAmount()) : glOffsetJournalLine.getJlAmount());

                            firstFlag = false;
                        }

                        glForexLedger = glForexLedgerHome.create(DATE, FRL_LN, "OR", FRL_AMNT, CONVERSION_RATE, COA_FRX_BLNC, FRX_GN_LSS, companyCode);

                        // glJournalLine.getGlChartOfAccount().addGlForexLedger(glForexLedger);
                        glForexLedger.setGlChartOfAccount(glJournalLine.getGlChartOfAccount());

                        // propagate balances
                        try {

                            glForexLedgers = glForexLedgerHome.findByGreaterThanFrlDateAndCoaCode(glForexLedger.getFrlDate(), glForexLedger.getGlChartOfAccount().getCoaCode(), glForexLedger.getFrlAdCompany());

                        } catch (FinderException ex) {

                        }

                        for (Object forexLedger : glForexLedgers) {

                            glForexLedger = (LocalGlForexLedger) forexLedger;
                            FRL_AMNT = arDistributionRecord.getDrAmount();

                            if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET"))
                                FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                            else FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);

                            glForexLedger.setFrlBalance(glForexLedger.getFrlBalance() + FRL_AMNT);
                        }
                    }
                }

                if (glOffsetJournalLine != null) {

                    // glJournal.addGlJournalLine(glOffsetJournalLine);
                    glOffsetJournalLine.setGlJournal(glJournal);
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

            Debug.print("ArReceiptPostControllerBean executeArRctPost " + txnStartDate);

        } catch (GlJREffectiveDateNoPeriodExistException | GlobalExpiryDateNotFoundException |
                 AdPRFCoaGlVarianceAccountNotFoundException | GlobalBranchAccountNumberInvalidException |
                 GlobalInventoryDateException | GlobalTransactionAlreadyVoidPostedException |
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

    public short getGlFcPrecisionUnit(Integer companyCode) {

        Debug.print("ArReceiptPostControllerBean getGlFcPrecisionUnit");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods
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

        Debug.print("ArReceiptPostControllerBean post");
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

                    // arCustomer.addArCustomerBalance(arNewCustomerBalance);
                    arNewCustomerBalance.setArCustomer(arCustomer);

                } else { // equals to invoice date

                    arCustomerBalance.setCbBalance(arCustomerBalance.getCbBalance() + RCT_AMNT);
                }

            } else {

                // create new balance

                LocalArCustomerBalance arNewCustomerBalance = arCustomerBalanceHome.create(RCT_DT, RCT_AMNT, companyCode);

                // arCustomer.addArCustomerBalance(arNewCustomerBalance);
                arNewCustomerBalance.setArCustomer(arCustomer);
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

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer companyCode) {

        Debug.print("ArReceiptPostControllerBean convertForeignToFunctionalCurrency");
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

        Debug.print("ArReceiptPostControllerBean postToGl");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccountBalance glChartOfAccountBalance = glChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(glAccountingCalendarValue.getAcvCode(), glChartOfAccount.getCoaCode(), companyCode);
            Debug.print("ArReceiptPostControllerBean postToGl A");
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

    private void postToInv(LocalArInvoiceLineItem arInvoiceLineItem, Date CST_DT, double CST_QTY_SLD, double CST_CST_OF_SLS, double CST_RMNNG_QTY, double CST_RMNNG_VL, double CST_VRNC_VL, String USR_NM, Integer branchCode, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException {

        Debug.print("ArReceiptPostControllerBean postToInv");
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
                Debug.print("ArReceiptPostControllerBean postToInv A");
            } catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            String prevExpiryDates = "";
            String miscListPrpgt = "";
            double qtyPrpgt = 0;
            try {
                LocalInvCosting prevCst = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndRemainingQuantityNotEqualToZeroAndIlCode(CST_DT, invItemLocation.getIlCode(), branchCode, companyCode);
                Debug.print("ArReceiptPostControllerBean postToInv B");
                prevExpiryDates = prevCst.getCstExpiryDate();
                qtyPrpgt = prevCst.getCstRemainingQuantity();

                if (prevExpiryDates == null) {
                    prevExpiryDates = "";
                }

            } catch (Exception ex) {
                Debug.print("prevExpiryDates CATCH: " + prevExpiryDates);
                prevExpiryDates = "";
            }

            // create costing
            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d, 0d, 0d, CST_QTY_SLD, CST_CST_OF_SLS, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, 0d, branchCode, companyCode);
            // invItemLocation.addInvCosting(invCosting);
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setArInvoiceLineItem(arInvoiceLineItem);

            invCosting.setCstQuantity(CST_QTY_SLD * -1);
            invCosting.setCstCost(CST_CST_OF_SLS * -1);

            if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiTraceMisc() != 0) {

                if (arInvoiceLineItem.getArReceipt().getRctType().equals("MISC")) {
                    if (arInvoiceLineItem.getIliMisc() != null && arInvoiceLineItem.getIliMisc() != "" && arInvoiceLineItem.getIliMisc().length() != 0) {
                        if (prevExpiryDates != null && prevExpiryDates != "" && prevExpiryDates.length() != 0) {
                            int qty2Prpgt = Integer.parseInt(this.getQuantityExpiryDates(arInvoiceLineItem.getIliMisc()));

                            String miscList2Prpgt = this.propagateExpiryDates(arInvoiceLineItem.getIliMisc(), qty2Prpgt, "False");
                            ArrayList miscList = this.expiryDates(arInvoiceLineItem.getIliMisc(), qty2Prpgt);
                            String propagateMiscPrpgt = "";
                            StringBuilder ret = new StringBuilder();
                            StringBuilder exp = new StringBuilder();
                            String Checker = "";

                            Iterator mi = miscList.iterator();

                            propagateMiscPrpgt = prevExpiryDates;
                            ret = new StringBuilder(propagateMiscPrpgt);
                            while (mi.hasNext()) {
                                String miscStr = (String) mi.next();

                                int qTest = checkExpiryDates(ret + "fin$");
                                ArrayList miscList2 = this.expiryDates("$" + ret, Double.parseDouble(Integer.toString(qTest)));

                                // ArrayList miscList2 = this.expiryDates("$" + ret, qtyPrpgt);
                                Iterator m2 = miscList2.iterator();
                                ret = new StringBuilder();
                                String ret2 = "false";
                                int a = 0;
                                while (m2.hasNext()) {
                                    String miscStr2 = (String) m2.next();

                                    if (ret2 == "1st") {
                                        ret2 = "false";
                                    }
                                    Debug.print("miscStr: " + miscStr);
                                    Debug.print("miscStr2: " + miscStr2);

                                    if (miscStr2.equals(miscStr)) {
                                        if (a == 0) {
                                            a = 1;
                                            ret2 = "1st";
                                            Checker = "true";
                                        } else {
                                            a = a + 1;
                                            ret2 = "true";
                                        }
                                    }
                                    Debug.print("Checker: " + Checker);
                                    Debug.print("ret2: " + ret2);
                                    if (!miscStr2.equals(miscStr) || a > 1) {
                                        if ((ret2 != "1st") && ((ret2 == "false") || (ret2 == "true"))) {
                                            if (miscStr2 != "") {
                                                miscStr2 = "$" + miscStr2;
                                                ret.append(miscStr2);
                                                Debug.print("ret " + ret);
                                                ret2 = "false";
                                            }
                                        }
                                    }
                                }
                                if (ret.toString() != "") {
                                    ret.append("$");
                                }
                                Debug.print("ret una: " + ret);
                                exp.append(ret);
                                qtyPrpgt = qtyPrpgt - 1;
                            }
                            Debug.print("ret fin " + ret);
                            Debug.print("exp fin " + exp);
                            // propagateMiscPrpgt = propagateMiscPrpgt.replace(" ", "$");
                            propagateMiscPrpgt = ret.toString();
                            if (Checker == "true") {
                                // invCosting.setCstExpiryDate(propagateMiscPrpgt);
                            } else {
                                Debug.print("Exp Not Found");
                                throw new GlobalExpiryDateNotFoundException(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                            }
                            invCosting.setCstExpiryDate(propagateMiscPrpgt);
                        } else {
                            if (arInvoiceLineItem.getIliMisc() != null && arInvoiceLineItem.getIliMisc() != "" && arInvoiceLineItem.getIliMisc().length() != 0) {
                                int initialQty = Integer.parseInt(this.getQuantityExpiryDates(arInvoiceLineItem.getIliMisc()));
                                String initialPrpgt = this.propagateExpiryDates(arInvoiceLineItem.getIliMisc(), initialQty, "False");

                                invCosting.setCstExpiryDate(initialPrpgt);
                            } else {
                                invCosting.setCstExpiryDate(prevExpiryDates);
                            }
                        }

                    } else {
                        invCosting.setCstExpiryDate(prevExpiryDates);
                    }
                }
            }

            // if cost variance is not 0, generate cost variance for the transaction

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
            String miscList = "";
            ArrayList miscList2 = null;
            Iterator i = invCostings.iterator();
            String propagateMisc = "";
            StringBuilder ret = new StringBuilder();
            while (i.hasNext()) {
                String Checker = "";
                String Checker2 = "";

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) i.next();

                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() - CST_QTY_SLD);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() - CST_CST_OF_SLS);

                if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiTraceMisc() != 0) {
                    if (arInvoiceLineItem.getIliMisc() != null && arInvoiceLineItem.getIliMisc() != "" && arInvoiceLineItem.getIliMisc().length() != 0) {
                        double qty = Double.parseDouble(this.getQuantityExpiryDates(arInvoiceLineItem.getIliMisc()));
                        // invPropagatedCosting.getInvAdjustmentLine().getAlMisc();
                        miscList = this.propagateExpiryDates(arInvoiceLineItem.getIliMisc(), qty, "False");
                        miscList2 = this.expiryDates(arInvoiceLineItem.getIliMisc(), qty);
                        Debug.print("arInvoiceLineItem.getIliMisc(): " + arInvoiceLineItem.getIliMisc());
                        Debug.print("getAlAdjustQuantity(): " + arInvoiceLineItem.getIliQuantity());

                        if (arInvoiceLineItem.getIliQuantity() < 0) {
                            Iterator mi = miscList2.iterator();

                            propagateMisc = invPropagatedCosting.getCstExpiryDate();
                            ret = new StringBuilder(invPropagatedCosting.getCstExpiryDate());
                            while (mi.hasNext()) {
                                String miscStr = (String) mi.next();

                                int qTest = checkExpiryDates(ret + "fin$");
                                ArrayList miscList3 = this.expiryDates("$" + ret, Double.parseDouble(Integer.toString(qTest)));

                                // ArrayList miscList3 = this.expiryDates("$" + ret, qtyPrpgt);
                                Debug.print("ret: " + ret);
                                Iterator m2 = miscList3.iterator();
                                ret = new StringBuilder();
                                String ret2 = "false";
                                int a = 0;
                                while (m2.hasNext()) {
                                    String miscStr2 = (String) m2.next();

                                    if (ret2 == "1st") {
                                        ret2 = "false";
                                    }
                                    Debug.print("2 miscStr: " + miscStr);
                                    Debug.print("2 miscStr2: " + miscStr2);
                                    if (miscStr2.equals(miscStr)) {
                                        if (a == 0) {
                                            a = 1;
                                            ret2 = "1st";
                                            Checker2 = "true";
                                        } else {
                                            a = a + 1;
                                            ret2 = "true";
                                        }
                                    }
                                    Debug.print("Checker: " + Checker2);
                                    if (!miscStr2.equals(miscStr) || a > 1) {
                                        if ((ret2 != "1st") || (ret2 == "false") || (ret2 == "true")) {
                                            if (miscStr2 != "") {
                                                miscStr2 = "$" + miscStr2;
                                                ret.append(miscStr2);
                                                ret2 = "false";
                                            }
                                        }
                                    }
                                }
                                if (Checker2 != "true") {
                                    throw new GlobalExpiryDateNotFoundException(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                                } else {
                                    Debug.print("TAE");
                                }

                                ret.append("$");
                                qtyPrpgt = qtyPrpgt - 1;
                            }
                        }
                    }

                    if (arInvoiceLineItem.getArReceipt().getRctType().equals("MISC")) {
                        if (arInvoiceLineItem.getIliMisc() != null && arInvoiceLineItem.getIliMisc() != "" && arInvoiceLineItem.getIliMisc().length() != 0) {
                            if (prevExpiryDates != null && prevExpiryDates != "" && prevExpiryDates.length() != 0) {

                                Iterator mi = miscList2.iterator();

                                propagateMisc = invPropagatedCosting.getCstExpiryDate();
                                ret = new StringBuilder(propagateMisc);
                                while (mi.hasNext()) {
                                    String miscStr = (String) mi.next();

                                    int qTest = checkExpiryDates(ret + "fin$");
                                    ArrayList miscList3 = this.expiryDates("$" + ret, Double.parseDouble(Integer.toString(qTest)));

                                    // ArrayList miscList3 = this.expiryDates("$" + ret, qtyPrpgt);
                                    Debug.print("ret: " + ret);
                                    Iterator m2 = miscList3.iterator();
                                    ret = new StringBuilder();
                                    String ret2 = "false";
                                    int a = 0;
                                    while (m2.hasNext()) {
                                        String miscStr2 = (String) m2.next();

                                        if (ret2 == "1st") {
                                            ret2 = "false";
                                        }
                                        Debug.print("2 miscStr: " + miscStr);
                                        Debug.print("2 miscStr2: " + miscStr2);
                                        if (miscStr2.equals(miscStr)) {
                                            if (a == 0) {
                                                a = 1;
                                                ret2 = "1st";
                                                Checker = "true";
                                            } else {
                                                a = a + 1;
                                                ret2 = "true";
                                            }
                                        }
                                        Debug.print("Checker: " + Checker);
                                        if (!miscStr2.equals(miscStr) || a > 1) {
                                            if ((ret2 != "1st") || (ret2 == "false") || (ret2 == "true")) {
                                                if (miscStr2 != "") {
                                                    miscStr2 = "$" + miscStr2;
                                                    ret.append(miscStr2);
                                                    ret2 = "false";
                                                }
                                            }
                                        }
                                    }
                                    ret.append("$");
                                    qtyPrpgt = qtyPrpgt - 1;
                                }
                                propagateMisc = ret.toString();
                            }

                        } else {
                            try {
                                propagateMisc = miscList + invPropagatedCosting.getCstExpiryDate().substring(1);
                            } catch (Exception e) {
                                propagateMisc = miscList;
                            }
                        }
                        invPropagatedCosting.setCstExpiryDate(propagateMisc);
                    }
                }
            }

        } catch (GlobalExpiryDateNotFoundException ex) {
            Debug.print("Huli Ka");
            throw ex;
        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public int checkExpiryDates(String misc) throws Exception {

        String separator = "$";

        // Remove first $ character
        misc = misc.substring(1);
        // Debug.print("misc: " + misc);
        // Counter
        int start = 0;
        int nextIndex = misc.indexOf(separator, start);
        int length = nextIndex - start;
        int numberExpry = 0;
        StringBuilder miscList = new StringBuilder();
        String miscList2 = "";
        String g = "";
        try {
            while (g != "fin") {
                // Date
                start = nextIndex + 1;
                nextIndex = misc.indexOf(separator, start);
                length = nextIndex - start;
                g = misc.substring(start, start + length);
                if (g.length() != 0) {
                    if (g != null || g != "" || g != "null") {
                        if (g.contains("null")) {
                            miscList2 = "Error";
                        } else {
                            miscList.append("$").append(g);
                            numberExpry++;
                        }
                    } else {
                        miscList2 = "Error";
                    }

                } else {
                    miscList2 = "Error";
                }
            }
        } catch (Exception e) {

        }

        if (miscList2 == "") {
            miscList.append("$");
        } else {
            miscList = new StringBuilder(miscList2);
        }

        return (numberExpry);
    }

    public String getQuantityExpiryDates(String qntty) {

        String separator = "$";

        // Remove first $ character
        qntty = qntty.substring(1);

        // Counter
        int start = 0;
        int nextIndex = qntty.indexOf(separator, start);
        int length = nextIndex - start;
        String y;
        y = (qntty.substring(start, start + length));
        Debug.print("Y " + y);

        return y;
    }

    private ArrayList expiryDates(String misc, double qty) throws Exception {

        Debug.print("ApReceivingItemControllerBean getExpiryDates");
        Debug.print("misc: " + misc);
        String separator = "$";

        // Remove first $ character
        misc = misc.substring(1);

        // Counter
        int start = 0;
        int nextIndex = misc.indexOf(separator, start);
        int length = nextIndex - start;

        Debug.print("qty" + qty);
        ArrayList miscList = new ArrayList();

        for (int x = 0; x < qty; x++) {
            try {
                // Date
                start = nextIndex + 1;
                nextIndex = misc.indexOf(separator, start);
                length = nextIndex - start;
                Debug.print("x" + x);
                String checker = misc.substring(start, start + length);
                Debug.print("checker" + checker);
                if (checker.length() != 0 || checker != "null") {
                    miscList.add(checker);
                } else {
                    miscList.add("null");
                }
            } catch (Exception e) {

            }
        }

        Debug.print("miscList :" + miscList);
        return miscList;
    }

    public String propagateExpiryDates(String misc, double qty, String reverse) throws Exception {
        // ActionErrors errors = new ActionErrors();
        Debug.print("ApReceivingItemControllerBean getExpiryDates");
        Debug.print("misc: " + misc);

        String separator = "";
        if (reverse == "False") {
            separator = "$";
        } else {
            separator = " ";
        }

        // Remove first $ character
        misc = misc.substring(1);
        Debug.print("misc: " + misc);
        // Counter
        int start = 0;
        int nextIndex = misc.indexOf(separator, start);
        int length = nextIndex - start;

        StringBuilder miscList = new StringBuilder();

        for (int x = 0; x < qty; x++) {

            // Date
            start = nextIndex + 1;
            nextIndex = misc.indexOf(separator, start);
            length = nextIndex - start;
            String g = misc.substring(start, start + length);
            Debug.print("g: " + g);
            Debug.print("g length: " + g.length());
            if (g.length() != 0) {
                miscList.append("$").append(g);
                Debug.print("miscList G: " + miscList);
            }
        }

        miscList.append("$");
        Debug.print("miscList :" + miscList);
        return (miscList.toString());
    }

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, double QTY_SLD, Integer companyCode) {

        Debug.print("ArReceiptPostControllerBean convertByUomFromAndItemAndQuantity");
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

        Debug.print("ArReceiptPostControllerBean regenerateInventoryDr");

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
                        if (!invNegTxnCosting.isEmpty())
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                    }

                    // add cost of sales distribution and inventory

                    double COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                    try {

                        LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arReceipt.getRctDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);

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

                        if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Average"))
                            COST = invCosting.getCstRemainingQuantity() <= 0 ? COST : Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                        if (COST <= 0) {
                            COST = invItemLocation.getInvItem().getIiUnitCost();
                        } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("FIFO"))
                            COST = invCosting.getCstRemainingQuantity() == 0 ? COST : Math.abs(this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), arInvoiceLineItem.getIliQuantity(), arInvoiceLineItem.getIliUnitPrice(), false, branchCode, companyCode));
                        else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Standard"))
                            COST = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();

                    } catch (FinderException ex) {
                    }

                    double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {

                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arInvoiceLineItem.getInvItemLocation().getIlCode(), branchCode, companyCode);

                    } catch (FinderException ex) {

                    }

                    if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiNonInventoriable() == EJBCommon.FALSE) {

                        if (adBranchItemLocation != null) {

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(), "COGS", EJBCommon.TRUE, COST * QTY_SLD, adBranchItemLocation.getBilCoaGlCostOfSalesAccount(), arReceipt, branchCode, companyCode);

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(), "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD, adBranchItemLocation.getBilCoaGlInventoryAccount(), arReceipt, branchCode, companyCode);

                        } else {

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(), "COGS", EJBCommon.TRUE, COST * QTY_SLD, arInvoiceLineItem.getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arReceipt, branchCode, companyCode);

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(), "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD, arInvoiceLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(), arReceipt, branchCode, companyCode);
                        }
                    }
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

        Debug.print("ArReceiptPostControllerBean addArDrIliEntry");
        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            Debug.print("COA_CODE: " + COA_CODE);
            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, branchCode, companyCode);

            // create distribution record

            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, EJBCommon.FALSE, companyCode);

            // arReceipt.addArDistributionRecord(arDistributionRecord);
            arDistributionRecord.setArReceipt(arReceipt);
            // glChartOfAccount.addArDistributionRecord(arDistributionRecord);
            arDistributionRecord.setGlChartOfAccount(glChartOfAccount);

        } catch (FinderException ex) {

            throw new GlobalBranchAccountNumberInvalidException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double getFrRateByFrNameAndFrDate(String FC_NM, Date CONVERSION_DATE, Integer companyCode) throws GlobalConversionDateNotExistException {

        Debug.print("ArReceiptPostControllerBean getFrRateByFrNameAndFrDate");
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

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ArReceiptPostControllerBean ejbCreate");
    }

}