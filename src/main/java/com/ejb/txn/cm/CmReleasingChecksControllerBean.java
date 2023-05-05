/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class CmReleasingChecksControllerBean
 * @created December 08, 2005, 04:43 PM
 * @author Farrah S. Garing
 */
package com.ejb.txn.cm;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.ad.LocalAdBankAccountHome;
import com.ejb.dao.ap.*;
import com.ejb.dao.gl.*;
import com.ejb.dao.inv.LocalInvCostingHome;
import com.ejb.entities.ad.LocalAdBankAccount;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ap.*;
import com.ejb.entities.gl.LocalGlAccountingCalendarValue;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.gl.LocalGlChartOfAccountBalance;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.entities.inv.LocalInvCosting;
import com.ejb.entities.inv.LocalInvItemLocation;
import com.ejb.exception.ap.ApCHKCheckNumberNotUniqueException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.mod.ap.ApModCheckDetails;

import jakarta.ejb.*;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

@Stateless(name = "CmReleasingChecksControllerEJB")
public class CmReleasingChecksControllerBean extends EJBContextClass implements CmReleasingChecksController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlChartOfAccountBalanceHome glChartOfAccountBalanceHome;
    @EJB
    private ILocalApSupplierHome apSupplierHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalApCheckBatchHome apCheckBatchHome;
    @EJB
    private LocalApCheckHome apCheckHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    private LocalApSupplierBalanceHome apSupplierBalanceHome;
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

    public ArrayList getApSplAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("CmReleasingChecksControllerBean getApSplAll");

        ArrayList list = new ArrayList();

        try {

            Collection apSuppliers = apSupplierHome.findEnabledSplAll(AD_BRNCH, AD_CMPNY);

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

    public ArrayList getAdBaAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("CmReleasingChecksControllerBean getAdBaAll");

        ArrayList list = new ArrayList();

        try {

            Collection adBankAccounts = adBankAccountHome.findEnabledBaAll(AD_BRNCH, AD_CMPNY);

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

    public ArrayList getGlFcAll(Integer AD_CMPNY) {

        Debug.print("CmReleasingChecksControllerBean getGlFcAll");

        ArrayList list = new ArrayList();

        try {

            Collection glFunctionalCurrencies = glFunctionalCurrencyHome.findFcAll(AD_CMPNY);

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

    public ArrayList getApOpenCbAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("CmReleasingChecksControllerBean getApOpenCbAll");

        ArrayList list = new ArrayList();

        try {

            Collection apCheckBatches = apCheckBatchHome.findOpenCbAll(AD_BRNCH, AD_CMPNY);

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

    public byte getAdPrfEnableApCheckBatch(Integer AD_CMPNY) {

        Debug.print("CmReleasingChecksControllerBean getAdPrfEnableApCheckBatch");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfEnableApCheckBatch();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getCmChkReleasableByCriteria(HashMap criteria, String ORDER_BY, Integer OFFSET, Integer LIMIT, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("CmReleasingChecksControllerBean getCmChkReleasableByCriteria");

        try {

            ArrayList chkList = new ArrayList();

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(chk) FROM ApCheck chk ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

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

                jbossQl.append("chk.chkCheckDate>=?").append(ctr + 1).append(" ");
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

                jbossQl.append("chk.chkCheckDate<=?").append(ctr + 1).append(" ");
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

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("chk.chkAdBranch=").append(AD_BRNCH).append(" ");

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("chk.chkReleased = 0 AND chk.chkPosted = 1 AND chk.chkAdCompany=").append(AD_CMPNY).append(" ");

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
                mdetails.setChkSplSupplierCode(apCheck.getApSupplier().getSplSupplierCode() + "-" + apCheck.getApSupplier().getSplName());
                mdetails.setChkBaName(apCheck.getAdBankAccount().getBaName());
                mdetails.setChkCheckDate(apCheck.getChkCheckDate());

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

    public void executeCmReleasingChecks(Integer CHK_CODE, String CHK_NMBR, Date CHK_CHCK_DT, Date CHK_DT_RLSD, String CHK_RMRKS, String USR_NM, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, ApCHKCheckNumberNotUniqueException {

        Debug.print("CmReleasingChecksControllerBean executeCmReleasingChecks");

        LocalApCheck apCheck = null;

        try {

            // validate if check is already deleted

            try {

                apCheck = apCheckHome.findByPrimaryKey(CHK_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if check is already posted

            if (apCheck.getChkReleased() == EJBCommon.TRUE) {

                // throw new GlobalTransactionAlreadyPostedException();

            }

            // validate if check number is unique

            LocalApCheck apExistingCheck = null;

            try {
                apExistingCheck = apCheckHome.findByChkNumberAndBaName(CHK_NMBR, apCheck.getAdBankAccount().getBaName(), AD_CMPNY);
            } catch (FinderException ex) {
            }

            Debug.print("CHK_NMBR					  : " + CHK_NMBR);

            if (apExistingCheck != null && !apCheck.getChkNumber().equals(CHK_NMBR)) {
                Debug.print("apExistingCheck.getChkNumber(): " + apExistingCheck.getChkNumber());
                Debug.print("CHK_NMBR					  : " + CHK_NMBR);
                throw new ApCHKCheckNumberNotUniqueException();
            }

            apCheck.setChkNumber(CHK_NMBR);
            apCheck.setChkCheckDate(CHK_CHCK_DT);
            apCheck.setChkReleased(EJBCommon.TRUE);
            apCheck.setChkDateReleased(CHK_DT_RLSD);

            Debug.print("CHK_RMRKS : " + CHK_RMRKS);
            Debug.print("apCheck.getChkMemo() : " + apCheck.getChkMemo());
            apCheck.setChkMemo(apCheck.getChkMemo() + "\n" + CHK_RMRKS);

            Debug.print("AFTER : apCheck.getChkMemo() : " + apCheck.getChkMemo());

        } catch (GlobalRecordAlreadyDeletedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

            // } catch (GlobalTransactionAlreadyPostedException ex) {
            //
            // getSessionContext().setRollbackOnly();
            // throw ex;

        } catch (ApCHKCheckNumberNotUniqueException ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("CmReleasingChecksControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfApUseSupplierPulldown(Integer AD_CMPNY) {

        Debug.print("CmReleasingChecksControllerBean getAdPrfApUseSupplierPulldown");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfApUseSupplierPulldown();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods

    private void post(Date CHK_DT, double CHK_AMNT, LocalApSupplier apSupplier, Integer AD_CMPNY) {

        Debug.print("CmReleasingChecksControllerBean post");

        try {

            // find supplier balance before or equal voucher date

            Collection apSupplierBalances = apSupplierBalanceHome.findByBeforeOrEqualVouDateAndSplSupplierCode(CHK_DT, apSupplier.getSplSupplierCode(), AD_CMPNY);

            if (!apSupplierBalances.isEmpty()) {

                // get last voucher

                ArrayList apSupplierBalanceList = new ArrayList(apSupplierBalances);

                LocalApSupplierBalance apSupplierBalance = (LocalApSupplierBalance) apSupplierBalanceList.get(apSupplierBalanceList.size() - 1);

                if (apSupplierBalance.getSbDate().before(CHK_DT)) {

                    // create new balance

                    LocalApSupplierBalance apNewSupplierBalance = apSupplierBalanceHome.create(CHK_DT, apSupplierBalance.getSbBalance() + CHK_AMNT, AD_CMPNY);

                    apSupplier.addApSupplierBalance(apNewSupplierBalance);

                } else { // equals to voucher date

                    apSupplierBalance.setSbBalance(apSupplierBalance.getSbBalance() + CHK_AMNT);
                }

            } else {

                // create new balance

                LocalApSupplierBalance apNewSupplierBalance = apSupplierBalanceHome.create(CHK_DT, CHK_AMNT, AD_CMPNY);

                apSupplier.addApSupplierBalance(apNewSupplierBalance);
            }

            // propagate to subsequent balances if necessary

            apSupplierBalances = apSupplierBalanceHome.findByAfterVouDateAndSplSupplierCode(CHK_DT, apSupplier.getSplSupplierCode(), AD_CMPNY);

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

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("CmReleasingChecksControllerBean convertForeignToFunctionalCurrency");

        LocalAdCompany adCompany = null;

        // get company and extended precision

        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // Convert to functional currency if necessary

        if (CONVERSION_RATE != 1 && CONVERSION_RATE != 0) {

            AMOUNT = AMOUNT * CONVERSION_RATE;
        }
        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double JL_AMNT, Integer AD_CMPNY) {

        Debug.print("ApVoucherPostControllerBean postToGl");

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

    private void postToInv(LocalApVoucherLineItem apVoucherLineItem, Date CST_DT, double CST_QTY_RCVD, double CST_ITM_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("CmReleasingChecksControllerBean postToInv");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            LocalInvItemLocation invItemLocation = apVoucherLineItem.getInvItemLocation();
            int CST_LN_NMBR = 0;

            CST_QTY_RCVD = EJBCommon.roundIt(CST_QTY_RCVD, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_ITM_CST = EJBCommon.roundIt(CST_ITM_CST, adCompany.getGlFunctionalCurrency().getFcPrecision());
            CST_RMNNG_QTY = EJBCommon.roundIt(CST_RMNNG_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_RMNNG_VL = EJBCommon.roundIt(CST_RMNNG_VL, adCompany.getGlFunctionalCurrency().getFcPrecision());

            // create costing

            try {

                // generate line number

                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, AD_CMPNY);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            } catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, CST_QTY_RCVD, CST_ITM_CST, 0d, 0d, 0d, 0d, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, CST_QTY_RCVD > 0 ? CST_QTY_RCVD : 0, AD_BRNCH, AD_CMPNY);
            invItemLocation.addInvCosting(invCosting);
            invCosting.setApVoucherLineItem(apVoucherLineItem);

            // propagate balance if necessary

            invCosting.setCstQuantity(CST_QTY_RCVD);
            invCosting.setCstCost(CST_ITM_CST);

            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, AD_CMPNY);

            for (Object costing : invCostings) {

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) costing;

                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_QTY_RCVD);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_ITM_CST);
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("CmReleasingChecksControllerBean ejbCreate");
    }

    // private methods

}