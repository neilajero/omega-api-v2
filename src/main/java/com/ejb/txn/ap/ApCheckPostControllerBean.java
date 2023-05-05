/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ApCheckPostControllerBean
 * @created February 24, 2004, 10:19 AM
 * @author Dennis M. Hilario
 */
package com.ejb.txn.ap;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ap.*;
import com.ejb.dao.gl.*;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ap.*;
import com.ejb.entities.gl.*;
import com.ejb.entities.inv.*;
import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.mod.ap.ApModCheckDetails;

import jakarta.ejb.*;
import java.util.*;

@Stateless(name = "ApCheckPostControllerEJB")
public class ApCheckPostControllerBean extends EJBContextClass implements ApCheckPostController {

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
    private LocalAdBankAccountBalanceHome adBankAccountBalanceHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalApCheckBatchHome apCheckBatchHome;
    @EJB
    private LocalApCheckHome apCheckHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    private LocalApSupplierBalanceHome apSupplierBalanceHome;
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
    private LocalInvAdjustmentHome invAdjustmentHome;
    @EJB
    private LocalInvAdjustmentLineHome invAdjustmentLineHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalInvDistributionRecordHome invDistributionRecordHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;


    // TODO: This is a redundant functions to review
    public ArrayList getApSplAll(Integer branchCode, Integer companyCode) {

        Debug.print("ApCheckPostControllerBean getApSplAll");

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

    public ArrayList getAdBaAll(Integer branchCode, Integer companyCode) {

        Debug.print("ApCheckPostControllerBean getAdBaAll");


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

    public ArrayList getGlFcAll(Integer companyCode) {

        Debug.print("ApCheckPostControllerBean getGlFcAll");

        ArrayList list = new ArrayList();

        try {

            Collection glFunctionalCurrencies = glFunctionalCurrencyHome.findFcAll(companyCode);

            for (Object functionalCurrency : glFunctionalCurrencies) {

                LocalGlFunctionalCurrency glFunctionalCurrency = (LocalGlFunctionalCurrency) functionalCurrency;

                list.add(glFunctionalCurrency.getFcName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApOpenCbAll(Integer branchCode, Integer companyCode) {

        Debug.print("ApCheckPostControllerBean getApOpenCbAll");

        ArrayList list = new ArrayList();

        try {

            Collection apCheckBatches = apCheckBatchHome.findOpenCbAll(branchCode, companyCode);

            for (Object checkBatch : apCheckBatches) {

                LocalApCheckBatch apCheckBatch = (LocalApCheckBatch) checkBatch;

                list.add(apCheckBatch.getCbName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfEnableApCheckBatch(Integer companyCode) {

        Debug.print("ApCheckPostControllerBean getAdPrfEnableApCheckBatch");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfEnableApCheckBatch();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApChkPostableByCriteria(HashMap criteria, String ORDER_BY, Integer OFFSET, Integer LIMIT, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ApCheckPostControllerBean getApChkPostableByCriteria");

        try {

            ArrayList chkList = new ArrayList();

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(chk) FROM ApCheck chk ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size() - 1;

            Object[] obj = new Object[criteriaSize];

            if (criteria.containsKey("bankAccount")) {

                firstArgument = false;

                jbossQl.append("WHERE chk.adBankAccount.baName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("bankAccount");
                ctr++;
            }

            if (criteria.containsKey("currency")) {

                firstArgument = false;

                jbossQl.append("WHERE chk.glFunctionalCurrency.fcName=?").append(ctr + 1).append(" ");
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

                jbossQl.append("chk.apCheckBatch.cbName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("batchName");
                ctr++;
            }

            if (criteria.containsKey("supplierCode")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.apSupplier.splSupplierCode=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("supplierCode");
                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkDate>=?").append(ctr + 1).append(" ");
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

                jbossQl.append("chk.chkDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (criteria.containsKey("checkNumberFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkNumber>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("checkNumberFrom");
                ctr++;
            }

            if (criteria.containsKey("checkNumberTo")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("checkNumberTo");
                ctr++;
            }

            if (criteria.containsKey("documentNumberFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkDocumentNumber>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("documentNumberFrom");
                ctr++;
            }

            if (criteria.containsKey("documentNumberTo")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkDocumentNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("documentNumberTo");
                ctr++;
            }

            byte checkVoid = (Byte) criteria.get("checkVoid");

            if (checkVoid == EJBCommon.FALSE) {

                if (criteria.containsKey("approvalStatus")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("chk.chkApprovalStatus=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("approvalStatus");
                    ctr++;

                } else {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("(chk.chkApprovalStatus='APPROVED' OR chk.chkApprovalStatus='N/A') ");
                }

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkAdBranch=").append(branchCode).append(" ");

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkPosted = 0 AND chk.chkVoid = 0 AND chk.chkAdCompany=").append(companyCode).append(" ");

            } else {

                if (criteria.containsKey("approvalStatus")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("chk.chkApprovalStatus=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("approvalStatus");
                    ctr++;
                }

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkAdBranch=").append(branchCode).append(" ");

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("(chk.chkVoidApprovalStatus='APPROVED' OR chk.chkVoidApprovalStatus='N/A') AND chk.chkPosted = 1 AND chk.chkVoid = 1 AND chk.chkVoidPosted = 0 AND chk.chkAdCompany=").append(companyCode).append(" ");
            }

            String orderBy = null;

            switch (ORDER_BY) {
                case "BANK ACCOUNT":

                    orderBy = "chk.adBankAccount.baName";

                    break;
                case "SUPPLIER CODE":

                    orderBy = "chk.apSupplier.splSupplierCode";

                    break;
                case "DOCUMENT NUMBER":

                    orderBy = "chk.chkDocumentNumber";

                    break;
                case "CHECK NUMBER":

                    orderBy = "chk.chkNumber";
                    break;
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy).append(", chk.chkDate");

            } else {

                jbossQl.append("ORDER BY chk.chkDate");
            }

            Debug.print("QL + " + jbossQl);

            Collection apChecks = null;

            try {

                apChecks = apCheckHome.getChkByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

            } catch (Exception ex) {

                throw new EJBException(ex.getMessage());
            }

            if (apChecks.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }

            for (Object check : apChecks) {

                LocalApCheck apCheck = (LocalApCheck) check;

                ApModCheckDetails mdetails = new ApModCheckDetails();
                mdetails.setChkCode(apCheck.getChkCode());
                mdetails.setChkDate(apCheck.getChkDate());
                mdetails.setChkNumber(apCheck.getChkNumber());
                mdetails.setChkDocumentNumber(apCheck.getChkDocumentNumber());
                mdetails.setChkAmount(apCheck.getChkAmount());
                mdetails.setChkVoid(apCheck.getChkVoid());
                mdetails.setChkSplSupplierCode(apCheck.getApSupplier().getSplSupplierCode());
                mdetails.setChkBaName(apCheck.getAdBankAccount().getBaName());

                if (!apCheck.getApVoucherLineItems().isEmpty()) {

                    mdetails.setChkType("ITEMS");

                } else {

                    mdetails.setChkType("EXPENSES");
                }

                chkList.add(mdetails);
            }

            return chkList;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public void executeApChkPost(Integer CHK_CODE, String USR_NM, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ApCheckPostControllerBean executeApChkPost");

        LocalApCheck apCheck = null;

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            // validate if check is already deleted

            try {

                apCheck = apCheckHome.findByPrimaryKey(CHK_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if check is already posted

            if (apCheck.getChkVoid() == EJBCommon.FALSE && apCheck.getChkPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();

                // validate if check void is already posted

            } else if (apCheck.getChkVoid() == EJBCommon.TRUE && apCheck.getChkVoidPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyVoidPostedException();
            }

            // post check

            if (apCheck.getChkVoid() == EJBCommon.FALSE && apCheck.getChkPosted() == EJBCommon.FALSE) {

                if (apCheck.getChkType().equals("PAYMENT")) {

                    // increase amount paid in voucher payment schedules and voucher

                    double CHK_CRDTS = 0d;

                    Collection apAppliedVouchers = apCheck.getApAppliedVouchers();

                    for (Object appliedVoucher : apAppliedVouchers) {

                        LocalApAppliedVoucher apAppliedVoucher = (LocalApAppliedVoucher) appliedVoucher;

                        LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = apAppliedVoucher.getApVoucherPaymentSchedule();

                        double AMOUNT_PAID = apAppliedVoucher.getAvApplyAmount() + apAppliedVoucher.getAvDiscountAmount() + apAppliedVoucher.getAvTaxWithheld();

                        CHK_CRDTS += this.convertForeignToFunctionalCurrency(apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getGlFunctionalCurrency().getFcCode(), apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getGlFunctionalCurrency().getFcName(), apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouConversionDate(), apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouConversionRate(), apAppliedVoucher.getAvDiscountAmount() + apAppliedVoucher.getAvTaxWithheld(), companyCode);

                        apVoucherPaymentSchedule.setVpsAmountPaid(EJBCommon.roundIt(apVoucherPaymentSchedule.getVpsAmountPaid() + AMOUNT_PAID, this.getGlFcPrecisionUnit(companyCode)));

                        apVoucherPaymentSchedule.getApVoucher().setVouAmountPaid(EJBCommon.roundIt(apVoucherPaymentSchedule.getApVoucher().getVouAmountPaid() + AMOUNT_PAID, this.getGlFcPrecisionUnit(companyCode)));

                        // release voucher lock

                        apVoucherPaymentSchedule.setVpsLock(EJBCommon.FALSE);
                    }

                    // decrease supplier balance

                    double CHK_AMNT = this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apCheck.getChkAmount(), companyCode);

                    this.post(apCheck.getChkDate(), (CHK_AMNT + CHK_CRDTS) * -1, apCheck.getApSupplier(), companyCode);

                } else if (apCheck.getChkType().equals("DIRECT") && !apCheck.getApVoucherLineItems().isEmpty()) {

                    for (Object o : apCheck.getApVoucherLineItems()) {

                        LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) o;

                        String II_NM = apVoucherLineItem.getInvItemLocation().getInvItem().getIiName();
                        String LOC_NM = apVoucherLineItem.getInvItemLocation().getInvLocation().getLocName();

                        double QTY_RCVD = this.convertByUomFromAndItemAndQuantity(apVoucherLineItem.getInvUnitOfMeasure(), apVoucherLineItem.getInvItemLocation().getInvItem(), apVoucherLineItem.getVliQuantity(), companyCode);

                        LocalInvCosting invCosting = null;
                        // CREATE COSTING
                        try {

                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(apCheck.getChkDate(), II_NM, LOC_NM, branchCode, companyCode);

                        } catch (FinderException ex) {

                        }

                        double COST = apVoucherLineItem.getVliUnitCost();

                        if (invCosting == null) {

                            this.postToInv(apVoucherLineItem, apCheck.getChkDate(), QTY_RCVD, COST * QTY_RCVD, QTY_RCVD, COST * QTY_RCVD, 0d, null, branchCode, companyCode);

                        } else {

                            this.postToInv(apVoucherLineItem, apCheck.getChkDate(), QTY_RCVD, COST * QTY_RCVD, invCosting.getCstRemainingQuantity() + QTY_RCVD, invCosting.getCstRemainingValue() + (QTY_RCVD * COST), 0d, null, branchCode, companyCode);
                        }
                    }
                }

                // decrease bank balance

                LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(apCheck.getAdBankAccount().getBaCode());

                try {

                    // find bankaccount balance before or equal check date

                    Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(apCheck.getChkCheckDate(), apCheck.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                    if (!adBankAccountBalances.isEmpty()) {

                        // get last check

                        ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                        if (adBankAccountBalance.getBabDate().before(apCheck.getChkCheckDate())) {

                            // create new balance

                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(apCheck.getChkCheckDate(), adBankAccountBalance.getBabBalance() - apCheck.getChkAmount(), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);

                        } else { // equals to check date

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - apCheck.getChkAmount());
                        }

                    } else {

                        // create new balance

                        LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(apCheck.getChkCheckDate(), (0 - apCheck.getChkAmount()), "BOOK", companyCode);

                        // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                        adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                    }

                    // propagate to subsequent balances if necessary

                    adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(apCheck.getChkCheckDate(), apCheck.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                    for (Object bankAccountBalance : adBankAccountBalances) {

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                        adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - apCheck.getChkAmount());
                    }

                } catch (Exception ex) {

                    ex.printStackTrace();
                }

                // set check post status

                apCheck.setChkPosted(EJBCommon.TRUE);
                apCheck.setChkPostedBy(USR_NM);
                apCheck.setChkDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

                // set Relese Check Upon Posting if Bank Account is CASH

                if (apCheck.getAdBankAccount().getBaIsCashAccount() == EJBCommon.TRUE) {

                    apCheck.setChkReleased(EJBCommon.TRUE);
                    apCheck.setChkDateReleased(apCheck.getChkCheckDate());
                }

            } else if (apCheck.getChkVoid() == EJBCommon.TRUE && apCheck.getChkVoidPosted() == EJBCommon.FALSE) { // void
                // check

                if (apCheck.getChkType().equals("PAYMENT")) {

                    // decrease amount paid in voucher payment schedules and voucher

                    double CHK_CRDTS = 0d;

                    Collection apAppliedVouchers = apCheck.getApAppliedVouchers();

                    for (Object appliedVoucher : apAppliedVouchers) {

                        LocalApAppliedVoucher apAppliedVoucher = (LocalApAppliedVoucher) appliedVoucher;

                        LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = apAppliedVoucher.getApVoucherPaymentSchedule();

                        double AMOUNT_PAID = apAppliedVoucher.getAvApplyAmount() + apAppliedVoucher.getAvDiscountAmount() + apAppliedVoucher.getAvTaxWithheld();

                        CHK_CRDTS += this.convertForeignToFunctionalCurrency(apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getGlFunctionalCurrency().getFcCode(), apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getGlFunctionalCurrency().getFcName(), apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouConversionDate(), apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouConversionRate(), apAppliedVoucher.getAvDiscountAmount() + apAppliedVoucher.getAvTaxWithheld(), companyCode);

                        apVoucherPaymentSchedule.setVpsAmountPaid(EJBCommon.roundIt(apVoucherPaymentSchedule.getVpsAmountPaid() - AMOUNT_PAID, this.getGlFcPrecisionUnit(companyCode)));

                        apVoucherPaymentSchedule.getApVoucher().setVouAmountPaid(EJBCommon.roundIt(apVoucherPaymentSchedule.getApVoucher().getVouAmountPaid() - AMOUNT_PAID, this.getGlFcPrecisionUnit(companyCode)));
                    }

                    // increase supplier balance

                    double CHK_AMNT = this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apCheck.getChkAmount(), companyCode);

                    this.post(apCheck.getChkDate(), CHK_AMNT + CHK_CRDTS, apCheck.getApSupplier(), companyCode);

                } else if (apCheck.getChkType().equals("DIRECT") && !apCheck.getApVoucherLineItems().isEmpty()) {
                    // VOIDING DIRECT PAYMENT

                    for (Object o : apCheck.getApVoucherLineItems()) {

                        LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) o;

                        String II_NM = apVoucherLineItem.getInvItemLocation().getInvItem().getIiName();
                        String LOC_NM = apVoucherLineItem.getInvItemLocation().getInvLocation().getLocName();

                        double QTY_RCVD = this.convertByUomFromAndItemAndQuantity(apVoucherLineItem.getInvUnitOfMeasure(), apVoucherLineItem.getInvItemLocation().getInvItem(), apVoucherLineItem.getVliQuantity(), companyCode);

                        LocalInvCosting invCosting = null;
                        // CREATE COSTING
                        try {

                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(apCheck.getChkDate(), II_NM, LOC_NM, branchCode, companyCode);

                        } catch (FinderException ex) {

                        }

                        double COST = apVoucherLineItem.getVliUnitCost();

                        if (invCosting == null) {

                            this.postToInv(apVoucherLineItem, apCheck.getChkDate(), -QTY_RCVD, -COST * QTY_RCVD, -QTY_RCVD, -COST * QTY_RCVD, 0d, null, branchCode, companyCode);

                        } else {

                            this.postToInv(apVoucherLineItem, apCheck.getChkDate(), -QTY_RCVD, -(QTY_RCVD * COST), invCosting.getCstRemainingQuantity() - QTY_RCVD, invCosting.getCstRemainingValue() - (QTY_RCVD * COST), 0d, null, branchCode, companyCode);
                        }
                    }
                }

                // increase bank balance

                LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(apCheck.getAdBankAccount().getBaCode());

                try {

                    // find bankaccount balance before or equal check date

                    Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(apCheck.getChkCheckDate(), apCheck.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                    if (!adBankAccountBalances.isEmpty()) {

                        // get last check

                        ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                        if (adBankAccountBalance.getBabDate().before(apCheck.getChkCheckDate())) {

                            // create new balance

                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(apCheck.getChkCheckDate(), adBankAccountBalance.getBabBalance() + apCheck.getChkAmount(), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);

                        } else { // equals to check date

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + apCheck.getChkAmount());
                        }

                    } else {

                        // create new balance

                        LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(apCheck.getChkCheckDate(), (apCheck.getChkAmount()), "BOOK", companyCode);

                        // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                        adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                    }

                    // propagate to subsequent balances if necessary

                    adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(apCheck.getChkCheckDate(), apCheck.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                    for (Object bankAccountBalance : adBankAccountBalances) {

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                        adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + apCheck.getChkAmount());
                    }

                } catch (Exception ex) {

                    ex.printStackTrace();
                }

                // set check post status

                apCheck.setChkVoidPosted(EJBCommon.TRUE);
                apCheck.setChkPostedBy(USR_NM);
                apCheck.setChkDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
            }

            // post to gl if necessary

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            if (adPreference.getPrfApGlPostingType().equals("USE SL POSTING")) {

                // validate if date has no period and period is closed

                LocalGlSetOfBook glJournalSetOfBook = null;

                try {

                    glJournalSetOfBook = glSetOfBookHome.findByDate(apCheck.getChkDate(), companyCode);

                } catch (FinderException ex) {

                    throw new GlJREffectiveDateNoPeriodExistException();
                }

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), apCheck.getChkDate(), companyCode);

                if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P') {

                    throw new GlJREffectiveDatePeriodClosedException();
                }

                // check if check is balance if not check suspense posting

                LocalGlJournalLine glOffsetJournalLine = null;

                Collection apDistributionRecords = null;

                if (apCheck.getChkVoid() == EJBCommon.FALSE) {

                    apDistributionRecords = apDistributionRecordHome.findImportableDrByDrReversedAndChkCode(EJBCommon.FALSE, apCheck.getChkCode(), companyCode);

                } else {

                    apDistributionRecords = apDistributionRecordHome.findImportableDrByDrReversedAndChkCode(EJBCommon.TRUE, apCheck.getChkCode(), companyCode);
                }

                Iterator j = apDistributionRecords.iterator();

                double TOTAL_DEBIT = 0d;
                double TOTAL_CREDIT = 0d;

                while (j.hasNext()) {

                    LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) j.next();

                    double DR_AMNT = 0d;

                    if (apDistributionRecord.getApAppliedVoucher() != null) {

                        LocalApVoucher apVoucher = apDistributionRecord.getApAppliedVoucher().getApVoucherPaymentSchedule().getApVoucher();

                        DR_AMNT = this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apDistributionRecord.getDrAmount(), companyCode);

                    } else {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apDistributionRecord.getDrAmount(), companyCode);
                    }

                    if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

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

                        glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("ACCOUNTS PAYABLES", "CHECKS", companyCode);

                    } catch (FinderException ex) {

                        throw new GlobalJournalNotBalanceException();
                    }

                    if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                        glOffsetJournalLine = glJournalLineHome.create((short) (apDistributionRecords.size() + 1), EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);

                    } else {

                        glOffsetJournalLine = glJournalLineHome.create((short) (apDistributionRecords.size() + 1), EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
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

                    if (adPreference.getPrfEnableApCheckBatch() == EJBCommon.TRUE && apCheck.getApCheckBatch() != null) {

                        glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " " + apCheck.getApCheckBatch().getCbName(), branchCode, companyCode);

                    } else {

                        glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " CHECKS", branchCode, companyCode);
                    }

                } catch (FinderException ex) {
                }

                if (adPreference.getPrfEnableGlJournalBatch() == EJBCommon.TRUE && glJournalBatch == null) {

                    if (adPreference.getPrfEnableApCheckBatch() == EJBCommon.TRUE && apCheck.getApCheckBatch() != null) {

                        glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " " + apCheck.getApCheckBatch().getCbName(), "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, branchCode, companyCode);

                    } else {

                        glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " CHECKS", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, branchCode, companyCode);
                    }
                }

                // create journal entry
                String supplierName = apCheck.getChkSupplierName() == null ? apCheck.getApSupplier().getSplName() : apCheck.getChkSupplierName();

                LocalGlJournal glJournal = glJournalHome.create(apCheck.getChkReferenceNumber(), apCheck.getChkDescription(), apCheck.getChkDate(), 0.0d, null, apCheck.getChkDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM, EJBCommon.getGcCurrentDateWoTime().getTime(), apCheck.getApSupplier().getSplTin(), supplierName, EJBCommon.FALSE, null, branchCode, companyCode);

                LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("ACCOUNTS PAYABLES", companyCode);
                glJournal.setGlJournalSource(glJournalSource);

                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

                LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("CHECKS", companyCode);
                glJournal.setGlJournalCategory(glJournalCategory);

                if (glJournalBatch != null) {

                    glJournal.setGlJournalBatch(glJournalBatch);
                }

                // create journal lines

                j = apDistributionRecords.iterator();
                boolean firstFlag = true;

                while (j.hasNext()) {

                    LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) j.next();

                    double DR_AMNT = 0d;
                    LocalApVoucher apVoucher = null;

                    if (apDistributionRecord.getApAppliedVoucher() != null) {

                        apVoucher = apDistributionRecord.getApAppliedVoucher().getApVoucherPaymentSchedule().getApVoucher();

                        DR_AMNT = this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apDistributionRecord.getDrAmount(), companyCode);

                    } else {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apDistributionRecord.getDrAmount(), companyCode);
                    }

                    LocalGlJournalLine glJournalLine = glJournalLineHome.create(apDistributionRecord.getDrLine(), apDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

                    // apDistributionRecord.getGlChartOfAccount().addGlJournalLine(glJournalLine);
                    glJournalLine.setGlChartOfAccount(apDistributionRecord.getGlChartOfAccount());

                    // glJournal.addGlJournalLine(glJournalLine);
                    glJournalLine.setGlJournal(glJournal);

                    apDistributionRecord.setDrImported(EJBCommon.TRUE);

                    // for FOREX revaluation

                    int FC_CODE = apDistributionRecord.getApAppliedVoucher() != null ? apVoucher.getGlFunctionalCurrency().getFcCode() : apCheck.getGlFunctionalCurrency().getFcCode();

                    if ((FC_CODE != adCompany.getGlFunctionalCurrency().getFcCode()) && glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null && (FC_CODE == glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode())) {

                        double CONVERSION_RATE = apDistributionRecord.getApAppliedVoucher() != null ? apVoucher.getVouConversionRate() : apCheck.getChkConversionRate();

                        Date DATE = apDistributionRecord.getApAppliedVoucher() != null ? apVoucher.getVouConversionDate() : apCheck.getChkConversionDate();

                        if (DATE != null && (CONVERSION_RATE == 0 || CONVERSION_RATE == 1)) {

                            CONVERSION_RATE = this.getFrRateByFrNameAndFrDate(glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), companyCode);

                        } else if (CONVERSION_RATE == 0) {

                            CONVERSION_RATE = 1;
                        }

                        Collection glForexLedgers = null;

                        DATE = apDistributionRecord.getApAppliedVoucher() != null ? apVoucher.getVouDate() : apCheck.getChkDate();

                        try {

                            glForexLedgers = glForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(DATE, glJournalLine.getGlChartOfAccount().getCoaCode(), companyCode);

                        } catch (FinderException ex) {

                        }

                        LocalGlForexLedger glForexLedger = (glForexLedgers.isEmpty() || glForexLedgers == null) ? null : (LocalGlForexLedger) glForexLedgers.iterator().next();

                        int FRL_LN = (glForexLedger != null && glForexLedger.getFrlDate().compareTo(DATE) == 0) ? glForexLedger.getFrlLine() + 1 : 1;

                        // compute balance
                        double COA_FRX_BLNC = glForexLedger == null ? 0 : glForexLedger.getFrlBalance();
                        double FRL_AMNT = apDistributionRecord.getDrAmount();

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

                        glForexLedger = glForexLedgerHome.create(DATE, FRL_LN, "CHK", FRL_AMNT, CONVERSION_RATE, COA_FRX_BLNC, FRX_GN_LSS, companyCode);

                        // glJournalLine.getGlChartOfAccount().addGlForexLedger(glForexLedger);
                        glForexLedger.setGlChartOfAccount(glJournalLine.getGlChartOfAccount());

                        // propagate balances
                        try {

                            glForexLedgers = glForexLedgerHome.findByGreaterThanFrlDateAndCoaCode(glForexLedger.getFrlDate(), glForexLedger.getGlChartOfAccount().getCoaCode(), glForexLedger.getFrlAdCompany());

                        } catch (FinderException ex) {

                        }

                        for (Object forexLedger : glForexLedgers) {

                            glForexLedger = (LocalGlForexLedger) forexLedger;
                            FRL_AMNT = apDistributionRecord.getDrAmount();

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

                            if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) {
                                break;
                            }
                        }
                    }
                }
            }

        } catch (GlJREffectiveDateNoPeriodExistException | AdPRFCoaGlVarianceAccountNotFoundException |
                 GlobalTransactionAlreadyVoidPostedException | GlobalTransactionAlreadyPostedException |
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

    public short getGlFcPrecisionUnit(Integer companyCode) {

        Debug.print("ApCheckPostControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
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
                        return EJBCommon.roundIt(invFifoCosting.getCstItemCost() / invFifoCosting.getCstQuantityReceived(), this.getInvGpCostPrecisionUnit(companyCode));
                    } else if (invFifoCosting.getArInvoiceLineItem() != null) {
                        return EJBCommon.roundIt(invFifoCosting.getCstCostOfSales() / invFifoCosting.getCstQuantitySold(), this.getInvGpCostPrecisionUnit(companyCode));
                    } else {
                        return EJBCommon.roundIt(invFifoCosting.getCstAdjustCost() / invFifoCosting.getCstAdjustQuantity(), this.getInvGpCostPrecisionUnit(companyCode));
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

    public short getInvGpCostPrecisionUnit(Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean getInvGpCostPrecisionUnit");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfInvCostPrecisionUnit();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfApUseSupplierPulldown(Integer companyCode) {

        Debug.print("ApCheckPostControllerBean getAdPrfApUseSupplierPulldown");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfApUseSupplierPulldown();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods

    private void post(Date CHK_DT, double CHK_AMNT, LocalApSupplier apSupplier, Integer companyCode) {

        Debug.print("ApCheckPostControllerBean post");

        try {

            // find supplier balance before or equal voucher date

            Collection apSupplierBalances = apSupplierBalanceHome.findByBeforeOrEqualVouDateAndSplSupplierCode(CHK_DT, apSupplier.getSplSupplierCode(), companyCode);

            if (!apSupplierBalances.isEmpty()) {

                // get last voucher

                ArrayList apSupplierBalanceList = new ArrayList(apSupplierBalances);

                LocalApSupplierBalance apSupplierBalance = (LocalApSupplierBalance) apSupplierBalanceList.get(apSupplierBalanceList.size() - 1);

                if (apSupplierBalance.getSbDate().before(CHK_DT)) {

                    // create new balance

                    LocalApSupplierBalance apNewSupplierBalance = apSupplierBalanceHome.create(CHK_DT, apSupplierBalance.getSbBalance() + CHK_AMNT, companyCode);

                    // apSupplier.addApSupplierBalance(apNewSupplierBalance);
                    apNewSupplierBalance.setApSupplier(apSupplier);

                } else { // equals to voucher date

                    apSupplierBalance.setSbBalance(apSupplierBalance.getSbBalance() + CHK_AMNT);
                }

            } else {

                // create new balance

                LocalApSupplierBalance apNewSupplierBalance = apSupplierBalanceHome.create(CHK_DT, CHK_AMNT, companyCode);

                apSupplier.addApSupplierBalance(apNewSupplierBalance);
                apNewSupplierBalance.setApSupplier(apSupplier);
            }

            // propagate to subsequent balances if necessary

            apSupplierBalances = apSupplierBalanceHome.findByAfterVouDateAndSplSupplierCode(CHK_DT, apSupplier.getSplSupplierCode(), companyCode);

            for (Object supplierBalance : apSupplierBalances) {

                LocalApSupplierBalance apSupplierBalance = (LocalApSupplierBalance) supplierBalance;

                apSupplierBalance.setSbBalance(apSupplierBalance.getSbBalance() + CHK_AMNT);
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer companyCode) {

        Debug.print("ApCheckPostControllerBean convertForeignToFunctionalCurrency");

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

        Debug.print("ApCheckPostControllerBean postToGl");

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

    private void postToInv(LocalApVoucherLineItem apVoucherLineItem, Date CST_DT, double CST_QTY_RCVD, double CST_ITM_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, double CST_VRNC_VL, String USR_NM, Integer branchCode, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ApCheckPostControllerBean postToInv");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalInvItemLocation invItemLocation = apVoucherLineItem.getInvItemLocation();
            int CST_LN_NMBR = 0;

            CST_QTY_RCVD = EJBCommon.roundIt(CST_QTY_RCVD, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_ITM_CST = EJBCommon.roundIt(CST_ITM_CST, adCompany.getGlFunctionalCurrency().getFcPrecision());
            CST_RMNNG_QTY = EJBCommon.roundIt(CST_RMNNG_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_RMNNG_VL = EJBCommon.roundIt(CST_RMNNG_VL, adCompany.getGlFunctionalCurrency().getFcPrecision());

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

            String prevExpiryDates = "";
            String miscListPrpgt = "";
            double qtyPrpgt = 0;

            if (apVoucherLineItem.getApCheck().getChkType().equals("DIRECT")) {
                try {
                    LocalInvCosting prevCst = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndRemainingQuantityNotEqualToZeroAndIlCode(CST_DT, invItemLocation.getIlCode(), branchCode, companyCode);

                    prevExpiryDates = prevCst.getCstExpiryDate();
                    qtyPrpgt = prevCst.getCstRemainingQuantity();

                    if (prevExpiryDates == null) {
                        prevExpiryDates = "";
                    }

                } catch (Exception ex) {

                }
            }

            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, CST_QTY_RCVD, CST_ITM_CST, 0d, 0d, 0d, 0d, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, 0d, branchCode, companyCode);
            // invItemLocation.addInvCosting(invCosting);
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setApVoucherLineItem(apVoucherLineItem);

            invCosting.setCstQuantity(CST_QTY_RCVD);
            invCosting.setCstCost(CST_ITM_CST);

            // Get Latest Expiry Dates
            if (apVoucherLineItem.getApCheck().getChkType().equals("DIRECT")) {
                if (prevExpiryDates != null && prevExpiryDates != "" && prevExpiryDates.length() != 0) {
                    Debug.print("apPurchaseOrderLine.getPlMisc(): " + apVoucherLineItem.getVliMisc().length());
                    if (apVoucherLineItem.getVliMisc() != null && apVoucherLineItem.getVliMisc() != "" && apVoucherLineItem.getVliMisc().length() != 0) {
                        int qty2Prpgt = Integer.parseInt(this.getQuantityExpiryDates(apVoucherLineItem.getVliMisc()));
                        String miscList2Prpgt = this.propagateExpiryDates(apVoucherLineItem.getVliMisc(), qty2Prpgt);

                        String propagateMiscPrpgt = miscList2Prpgt + prevExpiryDates;

                        invCosting.setCstExpiryDate(propagateMiscPrpgt);
                    } else {
                        invCosting.setCstExpiryDate(prevExpiryDates);
                    }

                } else {
                    if (apVoucherLineItem.getVliMisc() != null && apVoucherLineItem.getVliMisc() != "" && apVoucherLineItem.getVliMisc().length() != 0) {
                        int initialQty = Integer.parseInt(this.getQuantityExpiryDates(apVoucherLineItem.getVliMisc()));
                        String initialPrpgt = this.propagateExpiryDates(apVoucherLineItem.getVliMisc(), initialQty);

                        invCosting.setCstExpiryDate(initialPrpgt);
                    } else {
                        invCosting.setCstExpiryDate(prevExpiryDates);
                    }
                }
            }

            // if cost variance is not 0, generate cost variance for the transaction
            if (CST_VRNC_VL != 0) {

                this.generateCostVariance(invCosting.getInvItemLocation(), CST_VRNC_VL, "APCHK" + apVoucherLineItem.getApCheck().getChkDocumentNumber(), apVoucherLineItem.getApCheck().getChkDescription(), apVoucherLineItem.getApCheck().getChkDate(), USR_NM, branchCode, companyCode);
            }

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
            String miscList = "";
            i = invCostings.iterator();
            if (apVoucherLineItem.getApCheck().getChkType().equals("DIRECT")) {
                if (apVoucherLineItem.getVliMisc() != null && apVoucherLineItem.getVliMisc() != "" && apVoucherLineItem.getVliMisc().length() != 0) {
                    double qty = Double.parseDouble(this.getQuantityExpiryDates(apVoucherLineItem.getVliMisc()));
                    miscList = this.propagateExpiryDates(apVoucherLineItem.getVliMisc(), qty);

                    Debug.print("miscList Propagate:" + miscList);
                }
            }

            while (i.hasNext()) {

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) i.next();

                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_QTY_RCVD);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_ITM_CST);
                if (apVoucherLineItem.getApCheck().getChkType().equals("DIRECT")) {
                    if (apVoucherLineItem.getVliMisc() != null && apVoucherLineItem.getVliMisc() != "" && apVoucherLineItem.getVliMisc().length() != 0) {
                        // String miscList2 =
                        // propagateMisc;//this.propagateExpiryDates(invCosting.getCstExpiryDate(),
                        // qty);
                        Debug.print("invPropagatedCosting.getCstExpiryDate() : " + invPropagatedCosting.getCstExpiryDate());
                        String propagateMisc = invPropagatedCosting.getCstExpiryDate() + miscList;

                        invPropagatedCosting.setCstExpiryDate(propagateMisc);
                    } else {
                        invPropagatedCosting.setCstExpiryDate(prevExpiryDates);
                    }
                }
            }

            // regenerate cost variance
            this.regenerateCostVariance(invCostings, invCosting, branchCode, companyCode);

        } catch (AdPRFCoaGlVarianceAccountNotFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
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

    public String propagateExpiryDates(String misc, double qty) throws Exception {
        // ActionErrors errors = new ActionErrors();

        Debug.print("ApReceivingItemControllerBean getExpiryDates");

        String separator = "$";

        // Remove first $ character
        misc = misc.substring(1);

        // Counter
        int start = 0;
        int nextIndex = misc.indexOf(separator, start);
        int length = nextIndex - start;

        Debug.print("qty" + qty);
        StringBuilder miscList = new StringBuilder();

        for (int x = 0; x < qty; x++) {

            // Date
            start = nextIndex + 1;
            nextIndex = misc.indexOf(separator, start);
            length = nextIndex - start;

            try {

                miscList.append("$").append(misc, start, start + length);
            } catch (Exception ex) {

                throw ex;
            }
        }

        miscList.append("$");
        Debug.print("miscList :" + miscList);
        return (miscList.toString());
    }

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, double QTY_RCVD, Integer companyCode) {

        Debug.print("ApCheckPostControllerBean convertByUomFromAndItemAndQuantity");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), companyCode);

            return EJBCommon.roundIt(QTY_RCVD * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), adPreference.getPrfInvQuantityPrecisionUnit());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double getFrRateByFrNameAndFrDate(String FC_NM, Date CONVERSION_DATE, Integer companyCode) throws GlobalConversionDateNotExistException {

        Debug.print("ApCheckPostControllerBean getFrRateByFrNameAndFrDate");

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

        Debug.print("ApCheckPostControllerBean voidInvAdjustment");

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

            Collection invAjustmentLines = invAdjustment.getInvAdjustmentLines();
            i = invAjustmentLines.iterator();
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

    }

    private void regenerateCostVariance(Collection invCostings, LocalInvCosting invCosting, Integer branchCode, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

    }

    private void addInvDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, byte DR_RVRSL, Integer COA_CODE, LocalInvAdjustment invAdjustment, Integer branchCode, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ApCheckPostControllerBean addInvDrEntry");

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

            invDistributionRecord.setInvAdjustment(invAdjustment);
            invDistributionRecord.setInvChartOfAccount(glChartOfAccount);

        } catch (GlobalBranchAccountNumberInvalidException ex) {

            throw new GlobalBranchAccountNumberInvalidException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void executeInvAdjPost(Integer ADJ_CODE, String USR_NM, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException {

        Debug.print("ApCheckPostControllerBean executeInvAdjPost");

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
                // glChartOfAccount.addGlJournalLine(glOffsetJournalLine);
                glOffsetJournalLine.setGlChartOfAccount(glChartOfAccount);

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

                glJournalLine.setGlChartOfAccount(invDistributionRecord.getInvChartOfAccount());

                glJournalLine.setGlJournal(glJournal);

                invDistributionRecord.setDrImported(EJBCommon.TRUE);
            }

            if (glOffsetJournalLine != null) {

                // glJournal.addGlJournalLine(glOffsetJournalLine);
                glOffsetJournalLine.setGlJournal(glJournal);
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

        Debug.print("ArMiscReceiptEntryControllerBean addInvAlEntry");

        try {

            // create dr entry
            LocalInvAdjustmentLine invAdjustmentLine = null;
            invAdjustmentLine = invAdjustmentLineHome.create(CST_VRNC_VL, null, null, 0, 0, AL_VD, companyCode);

            // map adjustment, unit of measure, item location

            invAdjustmentLine.setInvAdjustment(invAdjustment);
            invAdjustmentLine.setInvUnitOfMeasure(invItemLocation.getInvItem().getInvUnitOfMeasure());
            invAdjustmentLine.setInvItemLocation(invItemLocation);

            return invAdjustmentLine;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalInvAdjustment saveInvAdjustment(String ADJ_RFRNC_NMBR, String ADJ_DSCRPTN, Date ADJ_DATE, String USR_NM, Integer branchCode, Integer companyCode) {

        Debug.print("ApCheckPostControllerBean saveInvAdjustment");

        try {

            // generate adj document number
            String ADJ_DCMNT_NMBR = null;

            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

            try {

                adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("INV ADJUSTMENT", companyCode);

            } catch (FinderException ex) {

            }

            try {

                adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode);

            } catch (FinderException ex) {

            }

            while (true) {

                if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                    try {

                        invAdjustmentHome.findByAdjDocumentNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), branchCode, companyCode);
                        adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                    } catch (FinderException ex) {

                        ADJ_DCMNT_NMBR = adDocumentSequenceAssignment.getDsaNextSequence();
                        adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                        break;
                    }

                } else {

                    try {

                        invAdjustmentHome.findByAdjDocumentNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), branchCode, companyCode);
                        adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                    } catch (FinderException ex) {

                        ADJ_DCMNT_NMBR = adBranchDocumentSequenceAssignment.getBdsNextSequence();
                        adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                        break;
                    }
                }
            }

            return invAdjustmentHome.create(ADJ_DCMNT_NMBR, ADJ_RFRNC_NMBR, ADJ_DSCRPTN, ADJ_DATE, "COST-VARIANCE", "N/A", EJBCommon.FALSE, USR_NM, ADJ_DATE, USR_NM, ADJ_DATE, null, null, USR_NM, ADJ_DATE, null, null, EJBCommon.TRUE, EJBCommon.FALSE, branchCode, companyCode);

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postInvAdjustmentToInventory(LocalInvAdjustmentLine invAdjustmentLine, Date CST_DT, double CST_ADJST_QTY, double CST_ADJST_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, Integer branchCode, Integer companyCode) {

        Debug.print("ApCheckPostControllerBean postInvAdjustmentToInventory");


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

            try {

                // generate line number

                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            } catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d, CST_ADJST_QTY, CST_ADJST_CST, 0d, 0d, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, 0d, branchCode, companyCode);
            invCosting.setInvItemLocation(invItemLocation);
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

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApCheckPostControllerBean ejbCreate");
    }

    // private methods

}