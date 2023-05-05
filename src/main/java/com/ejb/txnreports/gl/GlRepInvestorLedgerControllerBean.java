package com.ejb.txnreports.gl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;


import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.ad.LocalAdBankAccountHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdLookUpValue;
import com.ejb.dao.ad.LocalAdLookUpValueHome;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.dao.ap.ILocalApSupplierHome;
import com.ejb.entities.ap.LocalApCheck;
import com.ejb.dao.ap.LocalApCheckHome;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.entities.ap.LocalApVoucher;
import com.ejb.dao.ap.LocalApVoucherHome;
import com.ejb.dao.ar.LocalArCustomerHome;
import com.ejb.entities.ar.LocalArReceipt;
import com.ejb.dao.ar.LocalArReceiptHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.gen.LocalGenQualifier;
import com.ejb.dao.gen.LocalGenQualifierHome;
import com.ejb.dao.gl.ILocalGlAccountingCalendarValueHome;
import com.ejb.dao.gl.ILocalGlInvestorAccountBalanceHome;
import com.ejb.dao.gl.ILocalGlSetOfBookHome;
import com.ejb.entities.gl.LocalGlAccountingCalendarValue;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
import com.ejb.entities.gl.LocalGlFunctionalCurrencyRate;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.entities.gl.LocalGlInvestorAccountBalance;
import com.ejb.entities.gl.LocalGlSetOfBook;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.mod.ap.ApModSupplierDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.mod.gl.GlModFunctionalCurrencyDetails;
import com.util.reports.gl.GlRepInvestorLedgerDetails;

@Stateless(name = "GlRepInvestorLedgerControllerEJB")
public class GlRepInvestorLedgerControllerBean extends EJBContextClass implements GlRepInvestorLedgerController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlSetOfBookHome glSetOfBookHome;
    @EJB
    private ILocalGlAccountingCalendarValueHome glAccountingCalendarValueHome;
    @EJB
    private ILocalApSupplierHome apSupplierHome;
    @EJB
    private ILocalGlInvestorAccountBalanceHome glInvestorAccountBalanceHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private LocalApCheckHome apCheckHome;
    @EJB
    private LocalApVoucherHome apVoucherHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalGenQualifierHome genQualifierHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;

    public ArrayList getApSplAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("GlRepInvestorLedgerControllerBean getApSplAll");

        ArrayList list = new ArrayList();

        try {

            Collection apSuppliers = apSupplierHome.findAllEnabledSplScLedger(AD_CMPNY);

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

    public byte getAdPrfApUseSupplierPulldown(Integer AD_CMPNY) {

        Debug.print("GlRepInvestorLedgerControllerBean getAdPrfApUseSupplierPulldown");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfApUseSupplierPulldown();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGenQlfrAll(Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlRepInvestorLedgerControllerBean getGenQlfrAll");

        Collection genQualifiers = null;

        LocalGenQualifier genQualifier = null;

        ArrayList list = new ArrayList();


        try {

            genQualifiers = genQualifierHome.findQlfrAll(AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (genQualifiers.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }

        for (Object qualifier : genQualifiers) {

            genQualifier = (LocalGenQualifier) qualifier;

            list.add(genQualifier.getQlAccountType());
        }

        return list;
    }

    public ArrayList getAdLvReportTypeAll(Integer AD_CMPNY) {

        Debug.print("GlRepInvestorLedgerControllerBean getAdLvReportTypeAll");


        ArrayList list = new ArrayList();


        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("GL REPORT TYPE - INVESTOR LEDGER", AD_CMPNY);

            for (Object lookUpValue : adLookUpValues) {

                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;

                list.add(adLookUpValue.getLvName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList executeGlRepInvestorLedger(String SUPPLIER_CODE, Date GL_DT, boolean SL_INCLD_UNPSTD, String ORDER_BY, ArrayList branchList, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlRepInvestorLedgerControllerBean executeGlRepInvestorLedger");


        ArrayList list = new ArrayList();

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            LocalGlSetOfBook glSetOfBook = null;

            try {

                glSetOfBook = glSetOfBookHome.findByDate(GL_DT, AD_CMPNY);

            } catch (FinderException ex) {
                Debug.print("CHECK A");
                throw new GlobalNoRecordFoundException();
            }

            Debug.print("GL_DT=" + GL_DT);
            Debug.print("SOB=" + glSetOfBook.getGlAccountingCalendar().getAcName());

            // get coa selected

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT DISTINCT OBJECT(spl) FROM ApSupplier spl ");

            // add branch criteria

            if (branchList.isEmpty()) {
                Debug.print("CHECK B");
                throw new GlobalNoRecordFoundException();

            } else {

                jbossQl.append(", in (spl.adBranchSuppliers) bspl WHERE bspl.adBranch.brCode in (");

                boolean firstLoop = true;

                for (Object o : branchList) {

                    if (!firstLoop) {
                        jbossQl.append(", ");
                    } else {
                        firstLoop = false;
                    }

                    AdBranchDetails mdetails = (AdBranchDetails) o;

                    jbossQl.append(mdetails.getBrCode());
                }

                jbossQl.append(") AND ");
            }

            if (!SUPPLIER_CODE.equals("")) {

                jbossQl.append("spl.splSupplierCode LIKE '%").append(SUPPLIER_CODE).append("%' AND ");
            }

            jbossQl.append("spl.splEnable=1 AND spl.apSupplierClass.scLedger = 1 AND spl.splAdCompany=").append(AD_CMPNY).append(" ORDER BY spl.splSupplierCode ");

            Object[] obj = new Object[0];

            Debug.print("Ssql=" + jbossQl);
            Collection apSuppliers = apSupplierHome.getSplByCriteria(jbossQl.toString(), obj, 0, 0);

            // get previous

            Collection glPreviousSetOfBooks = glSetOfBookHome.findPrecedingSobByAcYear(glSetOfBook.getGlAccountingCalendar().getAcYear(), AD_CMPNY);
            Iterator prevItr = glPreviousSetOfBooks.iterator();
            LocalGlSetOfBook glPreviousSetOfBook = null;
            while (prevItr.hasNext()) {

                glPreviousSetOfBook = (LocalGlSetOfBook) prevItr.next();
                break;
            }

            // check if previous year is closed

            if (glPreviousSetOfBook != null && glPreviousSetOfBook.getSobYearEndClosed() == EJBCommon.FALSE) {

            } else {

                for (Object supplier : apSuppliers) {

                    LocalApSupplier apSupplier = (LocalApSupplier) supplier;

                    // get coa debit or credit in coa balance

                    LocalGlAccountingCalendarValue glAccountingCalendarValue = null;

                    try {

                        glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glSetOfBook.getGlAccountingCalendar().getAcCode(), GL_DT, AD_CMPNY);

                    } catch (FinderException ex) {

                        throw new GlobalNoRecordFoundException();
                    }

                    // get investor beginning balance

                    double INVTR_BGNNG_BLNC = 0d;

                    LocalGlAccountingCalendarValue glBeginningAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodNumber(glSetOfBook.getGlAccountingCalendar().getAcCode(), (short) 1, AD_CMPNY);

                    LocalGlInvestorAccountBalance glBeginningInvestorBalance = glInvestorAccountBalanceHome.findByAcvCodeAndSplCode(glBeginningAccountingCalendarValue.getAcvCode(), apSupplier.getSplCode(), AD_CMPNY);

                    INVTR_BGNNG_BLNC = glBeginningInvestorBalance.getIrabBeginningBalance();

                    Debug.print("glBeginningInvestorBalance.getIrabCode()=" + glBeginningInvestorBalance.getIrabCode());
                    Debug.print("INVTR_BGNNG_BLNC=" + INVTR_BGNNG_BLNC);

                    // get coa debit or credit balance

                    Collection arPostedRCTDrs = new ArrayList();
                    Collection apPostedCHKDrs = new ArrayList();

                    Collection apPostedVOUDrs = new ArrayList();

                    Debug.print("SL_INCLD_UNPSTD=" + SL_INCLD_UNPSTD);

                    // if (!SL_INCLD_UNPSTD) {}

                    arPostedRCTDrs = arReceiptHome.findPostedRctByRctDateRangeAndSplCode(glBeginningAccountingCalendarValue.getAcvDateFrom(), GL_DT, apSupplier.getSplCode(), AD_BRNCH, AD_CMPNY);

                    apPostedCHKDrs = apCheckHome.findPostedChkByChkDateRangeAndSplCode(glBeginningAccountingCalendarValue.getAcvDateFrom(), GL_DT, apSupplier.getSplCode(), AD_BRNCH, AD_CMPNY);

                    apPostedVOUDrs = apVoucherHome.findPostedVouByVouDateRangeAndSplCode(EJBCommon.FALSE, glBeginningAccountingCalendarValue.getAcvDateFrom(), GL_DT, apSupplier.getSplCode(), AD_BRNCH, AD_CMPNY);

                    Collection arUnpostedRCTDrs = new ArrayList();
                    Collection apUnpostedCHKDrs = new ArrayList();

                    Collection apUnpostedVOUDrs = new ArrayList();

                    if (SL_INCLD_UNPSTD) {

                        arUnpostedRCTDrs = arReceiptHome.findUnPostedRctByRctDateRangeAndSplCode(glBeginningAccountingCalendarValue.getAcvDateFrom(), GL_DT, apSupplier.getSplCode(), AD_BRNCH, AD_CMPNY);

                        apUnpostedCHKDrs = apCheckHome.findUnPostedChkByChkDateRangeAndSplCode(glBeginningAccountingCalendarValue.getAcvDateFrom(), GL_DT, apSupplier.getSplCode(), AD_BRNCH, AD_CMPNY);

                        apUnpostedVOUDrs = apVoucherHome.findUnPostedVouByVouDateRangeAndSplCode(EJBCommon.FALSE, glBeginningAccountingCalendarValue.getAcvDateFrom(), GL_DT, apSupplier.getSplCode(), AD_BRNCH, AD_CMPNY);
                    }

                    if (arPostedRCTDrs.isEmpty() && apPostedCHKDrs.isEmpty() && apPostedVOUDrs.isEmpty() && (SL_INCLD_UNPSTD && arUnpostedRCTDrs.isEmpty() && apUnpostedCHKDrs.isEmpty() && apUnpostedVOUDrs.isEmpty())) {

                    } else {

                        Iterator j = null;
                        Debug.print("arPostedRCTDrs=" + arPostedRCTDrs.size());

                        if (arPostedRCTDrs != null) {
                            j = arPostedRCTDrs.iterator();

                            while (j.hasNext()) {

                                LocalArReceipt arReceipt = (LocalArReceipt) j.next();

                                double AMNT = this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmount(), AD_CMPNY);
                                if (AMNT == 0) continue;

                                GlRepInvestorLedgerDetails details = new GlRepInvestorLedgerDetails();

                                details.setIlInvestorCode(apSupplier.getSplSupplierCode());
                                details.setIlInvestorName(apSupplier.getSplName());
                                details.setIlInvestorContact(apSupplier.getSplContact());
                                details.setIlInvestorAddress(apSupplier.getSplAddress());

                                details.setIlEffectiveDate(arReceipt.getRctDate());
                                details.setIlDocumentNumber(arReceipt.getRctNumber());
                                details.setIlReferenceNumber(arReceipt.getRctReferenceNumber());
                                details.setIlSourceName("ACCOUNTS RECEIVABLES");
                                details.setIlDescription(arReceipt.getRctDescription());
                                details.setIlDebit((byte) 0);
                                details.setIlAmount(AMNT);
                                details.setIlBeginningBalance(INVTR_BGNNG_BLNC);

                                details.setIlType("ADD");
                                details.setIlPosted(arReceipt.getRctPosted());

                                list.add(details);
                            }
                        }

                        if (apPostedCHKDrs != null) {
                            j = apPostedCHKDrs.iterator();

                            while (j.hasNext()) {

                                LocalApCheck apCheck = (LocalApCheck) j.next();

                                double AMNT = this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apCheck.getChkAmount(), AD_CMPNY);
                                if (AMNT == 0) continue;

                                GlRepInvestorLedgerDetails details = new GlRepInvestorLedgerDetails();

                                details.setIlInvestorCode(apSupplier.getSplSupplierCode());
                                details.setIlInvestorName(apSupplier.getSplName());
                                details.setIlInvestorContact(apSupplier.getSplContact());
                                details.setIlInvestorAddress(apSupplier.getSplAddress());

                                details.setIlEffectiveDate(apCheck.getChkDate());
                                details.setIlDocumentNumber(apCheck.getChkDocumentNumber());
                                details.setIlReferenceNumber(apCheck.getChkReferenceNumber());
                                details.setIlSourceName("ACCOUNTS PAYABLES");
                                details.setIlDescription(apCheck.getChkDescription());
                                details.setIlDebit((byte) 1);
                                details.setIlAmount(AMNT);
                                details.setIlBeginningBalance(INVTR_BGNNG_BLNC);
                                details.setIlType("SUB");
                                details.setIlPosted(apCheck.getChkPosted());

                                list.add(details);
                            }
                        }

                        if (apPostedVOUDrs != null) {
                            j = apUnpostedVOUDrs.iterator();

                            while (j.hasNext()) {

                                LocalApVoucher apVoucher = (LocalApVoucher) j.next();

                                Collection apDebitMemos = apVoucherHome.findByVouDebitMemoAndVouDmVoucherNumberAndVouVoid(apVoucher.getVouDmVoucherNumber(), AD_BRNCH, AD_CMPNY);

                                Iterator x = apDebitMemos.iterator();

                                double debitMemoAmount = 0d;

                                while (x.hasNext()) {

                                    LocalApVoucher apDebitMemo = (LocalApVoucher) x.next();
                                    debitMemoAmount += apDebitMemo.getVouBillAmount();
                                }

                                double AMNT = this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apVoucher.getVouAmountDue() - debitMemoAmount, AD_CMPNY);

                                if (AMNT == 0) continue;
                                GlRepInvestorLedgerDetails details = new GlRepInvestorLedgerDetails();

                                details.setIlInvestorCode(apSupplier.getSplSupplierCode());
                                details.setIlInvestorName(apSupplier.getSplName());
                                details.setIlInvestorContact(apSupplier.getSplContact());
                                details.setIlInvestorAddress(apSupplier.getSplAddress());

                                details.setIlEffectiveDate(apVoucher.getVouDate());
                                details.setIlDocumentNumber(apVoucher.getVouDocumentNumber());
                                details.setIlReferenceNumber(apVoucher.getVouReferenceNumber());
                                details.setIlSourceName("ACCOUNTS PAYABLES");
                                details.setIlDescription(apVoucher.getVouDescription());
                                details.setIlDebit((byte) 0);
                                details.setIlAmount(AMNT);
                                details.setIlBeginningBalance(INVTR_BGNNG_BLNC);
                                details.setIlType("ADD");
                                details.setIlPosted(apVoucher.getVouPosted());

                                list.add(details);
                            }
                        }

                        // include unposted subledger transactions

                        if (SL_INCLD_UNPSTD) {

                            if (arUnpostedRCTDrs != null) {
                                j = arUnpostedRCTDrs.iterator();

                                while (j.hasNext()) {

                                    LocalArReceipt arReceipt = (LocalArReceipt) j.next();

                                    double AMNT = this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmount(), AD_CMPNY);
                                    if (AMNT == 0) continue;

                                    GlRepInvestorLedgerDetails details = new GlRepInvestorLedgerDetails();

                                    details.setIlInvestorCode(apSupplier.getSplSupplierCode());
                                    details.setIlInvestorName(apSupplier.getSplName());
                                    details.setIlInvestorContact(apSupplier.getSplContact());
                                    details.setIlInvestorAddress(apSupplier.getSplAddress());

                                    details.setIlEffectiveDate(arReceipt.getRctDate());
                                    details.setIlDocumentNumber(arReceipt.getRctNumber());
                                    details.setIlReferenceNumber(arReceipt.getRctReferenceNumber());
                                    details.setIlSourceName("ACCOUNTS RECEIVABLES");
                                    details.setIlDescription(arReceipt.getRctDescription());
                                    details.setIlDebit((byte) 0);
                                    details.setIlAmount(AMNT);
                                    details.setIlBeginningBalance(INVTR_BGNNG_BLNC);
                                    details.setIlType("ADD");
                                    details.setIlPosted(arReceipt.getRctPosted());

                                    list.add(details);
                                }
                            }

                            if (apUnpostedCHKDrs != null) {
                                j = apUnpostedCHKDrs.iterator();

                                while (j.hasNext()) {

                                    LocalApCheck apCheck = (LocalApCheck) j.next();

                                    double AMNT = this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apCheck.getChkAmount(), AD_CMPNY);

                                    if (AMNT == 0) continue;
                                    GlRepInvestorLedgerDetails details = new GlRepInvestorLedgerDetails();

                                    details.setIlInvestorCode(apSupplier.getSplSupplierCode());
                                    details.setIlInvestorName(apSupplier.getSplName());
                                    details.setIlInvestorContact(apSupplier.getSplContact());
                                    details.setIlInvestorAddress(apSupplier.getSplAddress());

                                    details.setIlEffectiveDate(apCheck.getChkDate());
                                    details.setIlDocumentNumber(apCheck.getChkDocumentNumber());
                                    details.setIlReferenceNumber(apCheck.getChkReferenceNumber());
                                    details.setIlSourceName("ACCOUNTS PAYABLES");
                                    details.setIlDescription(apCheck.getChkDescription());
                                    details.setIlDebit((byte) 1);
                                    details.setIlAmount(AMNT);
                                    details.setIlBeginningBalance(INVTR_BGNNG_BLNC);
                                    details.setIlType("SUB");
                                    details.setIlPosted(apCheck.getChkPosted());

                                    list.add(details);
                                }
                            }

                            if (apUnpostedVOUDrs != null) {
                                j = apUnpostedVOUDrs.iterator();

                                while (j.hasNext()) {

                                    LocalApVoucher apVoucher = (LocalApVoucher) j.next();

                                    double AMNT = this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apVoucher.getVouAmountDue(), AD_CMPNY);

                                    if (AMNT == 0) continue;
                                    GlRepInvestorLedgerDetails details = new GlRepInvestorLedgerDetails();

                                    details.setIlInvestorCode(apSupplier.getSplSupplierCode());
                                    details.setIlInvestorName(apSupplier.getSplName());
                                    details.setIlInvestorContact(apSupplier.getSplContact());
                                    details.setIlInvestorAddress(apSupplier.getSplAddress());

                                    details.setIlEffectiveDate(apVoucher.getVouDate());
                                    details.setIlDocumentNumber(apVoucher.getVouDocumentNumber());
                                    details.setIlReferenceNumber(apVoucher.getVouReferenceNumber());
                                    details.setIlSourceName("ACCOUNTS PAYABLES");
                                    details.setIlDescription(apVoucher.getVouDescription());
                                    details.setIlDebit((byte) 0);
                                    details.setIlAmount(AMNT);
                                    details.setIlBeginningBalance(INVTR_BGNNG_BLNC);
                                    details.setIlType("ADD");
                                    details.setIlPosted(apVoucher.getVouPosted());

                                    list.add(details);
                                }
                            }
                        }
                    }
                }
            }

            if (list.isEmpty()) {
                Debug.print("CHECK E");
                throw new GlobalNoRecordFoundException();
            }

            if (ORDER_BY.equals("INVESTOR NAME")) {

                list.sort(GlRepInvestorLedgerDetails.InvestorNameGroupComparator);

            } else {

                list.sort(GlRepInvestorLedgerDetails.NoGroupComparator);
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
        //
    }

    public ApModSupplierDetails getApSplBySplSupplierCode(String SPL_SPPLR_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlRepInvestorLedgerControllerBean getApSplBySplSupplierCode");

        try {

            LocalApSupplier apSupplier = null;

            try {

                apSupplier = apSupplierHome.findBySplSupplierCode(SPL_SPPLR_CODE, AD_CMPNY);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ApModSupplierDetails mdetails = new ApModSupplierDetails();

            mdetails.setSplPytName(apSupplier.getAdPaymentTerm() != null ? apSupplier.getAdPaymentTerm().getPytName() : null);
            mdetails.setSplScWtcName(apSupplier.getApSupplierClass().getApWithholdingTaxCode() != null ? apSupplier.getApSupplierClass().getApWithholdingTaxCode().getWtcName() : null);
            mdetails.setSplName(apSupplier.getSplName());

            if (apSupplier.getApSupplierClass().getApTaxCode() != null) {

                mdetails.setSplScTcName(apSupplier.getApSupplierClass().getApTaxCode().getTcName());
                mdetails.setSplScTcType(apSupplier.getApSupplierClass().getApTaxCode().getTcType());
                mdetails.setSplScTcRate(apSupplier.getApSupplierClass().getApTaxCode().getTcRate());
            }

            if (apSupplier.getInvLineItemTemplate() != null) {
                mdetails.setSplLitName(apSupplier.getInvLineItemTemplate().getLitName());
            }

            return mdetails;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    //

    public ArrayList getGlFcAllWithDefault(Integer AD_CMPNY) {

        Debug.print("GlRepInvestorLedgerControllerBean getGlFcAllWithDefault");

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

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("GlRepInvestorLedgerControllerBean getGlReportableAcvAll");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());
            details.setCmpTaxPayerName(adCompany.getCmpTaxPayerName());
            details.setCmpContact(adCompany.getCmpContact());
            details.setCmpPhone(adCompany.getCmpPhone());
            details.setCmpEmail(adCompany.getCmpEmail());
            details.setCmpTin(adCompany.getCmpTin());

            details.setCmpMailSectionNo(adCompany.getCmpMailSectionNo());
            details.setCmpMailLotNo(adCompany.getCmpMailLotNo());
            details.setCmpMailStreet(adCompany.getCmpMailStreet());
            details.setCmpMailPoBox(adCompany.getCmpMailPoBox());
            details.setCmpMailCountry(adCompany.getCmpMailCountry());
            details.setCmpMailProvince(adCompany.getCmpMailProvince());
            details.setCmpMailPostOffice(adCompany.getCmpMailPostOffice());
            details.setCmpMailCareOff(adCompany.getCmpMailCareOff());
            details.setCmpTaxPeriodFrom(adCompany.getCmpTaxPeriodFrom());
            details.setCmpTaxPeriodTo(adCompany.getCmpTaxPeriodTo());
            details.setCmpPublicOfficeName(adCompany.getCmpPublicOfficeName());
            details.setCmpDateAppointment(adCompany.getCmpDateAppointment());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlRepInvestorLedgerControllerBean getAdBrResAll");

        LocalAdBranchResponsibility adBranchResponsibility = null;
        LocalAdBranch adBranch = null;

        Collection adBranchResponsibilities = null;

        ArrayList list = new ArrayList();


        try {

            adBranchResponsibilities = adBranchResponsibilityHome.findByAdResponsibility(RS_CODE, AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (adBranchResponsibilities.isEmpty()) {
            Debug.print("CHECK F");
            throw new GlobalNoRecordFoundException();
        }

        try {

            for (Object branchResponsibility : adBranchResponsibilities) {

                adBranchResponsibility = (LocalAdBranchResponsibility) branchResponsibility;

                adBranch = adBranchResponsibility.getAdBranch();

                AdBranchDetails details = new AdBranchDetails();

                details.setBrCode(adBranch.getBrCode());
                details.setBrBranchCode(adBranch.getBrBranchCode());
                details.setBrName(adBranch.getBrName());
                details.setBrHeadQuarter(adBranch.getBrHeadQuarter());

                list.add(details);
            }

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        return list;
    }

    // private methods

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

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

        } else if (CONVERSION_DATE != null) {

            try {

                // Get functional currency rate

                LocalGlFunctionalCurrencyRate glReceiptFunctionalCurrencyRate = null;

                if (!FC_NM.equals("USD")) {

                    glReceiptFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(FC_CODE, CONVERSION_DATE, AD_CMPNY);

                    AMOUNT = AMOUNT * glReceiptFunctionalCurrencyRate.getFrXToUsd();
                }

                // Get set of book functional currency rate if necessary

                if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                    LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), CONVERSION_DATE, AD_CMPNY);

                    AMOUNT = AMOUNT / glCompanyFunctionalCurrencyRate.getFrXToUsd();
                }

            } catch (Exception ex) {

                throw new EJBException(ex.getMessage());
            }
        }

        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    // private methods from FRG Run

    private double convertFunctionalToForeignCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("GlRepInvestorLedgerControllerBean convertFunctionalToForeignCurrency");

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

        } else if (CONVERSION_DATE != null) {

            try {

                double FC_CONVERSION_RATE = 1f;
                double BK_CONVERSION_RATE = 1f;

                // Get functional currency rate

                if (!FC_NM.equals("USD")) {

                    LocalGlFunctionalCurrencyRate glReceiptFunctionalCurrencyRate = null;

                    glReceiptFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(FC_CODE, CONVERSION_DATE, AD_CMPNY);

                    if (glReceiptFunctionalCurrencyRate != null) {

                        FC_CONVERSION_RATE = glReceiptFunctionalCurrencyRate.getFrXToUsd();

                    } else {

                        // get latest daily rate prior to conversion date

                        Collection glReceiptFunctionalCurrencyRates = glFunctionalCurrencyRateHome.findPriorByFcCodeAndDate(FC_CODE, CONVERSION_DATE, AD_CMPNY);

                        Iterator i = glReceiptFunctionalCurrencyRates.iterator();

                        if (i.hasNext()) {

                            glReceiptFunctionalCurrencyRate = (LocalGlFunctionalCurrencyRate) i.next();
                            FC_CONVERSION_RATE = glReceiptFunctionalCurrencyRate.getFrXToUsd();
                        }
                    }

                    AMOUNT = AMOUNT / FC_CONVERSION_RATE;
                }

                // Get set of book functional currency rate if necessary

                if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                    LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate = null;

                    glCompanyFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), CONVERSION_DATE, AD_CMPNY);

                    if (glCompanyFunctionalCurrencyRate != null) {

                        BK_CONVERSION_RATE = glCompanyFunctionalCurrencyRate.getFrXToUsd();

                    } else {

                        // get latest daily rate prior to conversion date

                        Collection glCompanyFunctionalCurrencyRates = glFunctionalCurrencyRateHome.findPriorByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), CONVERSION_DATE, AD_CMPNY);

                        Iterator i = glCompanyFunctionalCurrencyRates.iterator();

                        if (i.hasNext()) {

                            glCompanyFunctionalCurrencyRate = (LocalGlFunctionalCurrencyRate) i.next();
                            BK_CONVERSION_RATE = glCompanyFunctionalCurrencyRate.getFrXToUsd();
                        }
                    }

                    // AMOUNT = AMOUNT * (1 / EJBCommon.roundIt(1 / BK_CONVERSION_RATE, (short)3));
                    AMOUNT = AMOUNT * BK_CONVERSION_RATE;
                }

            } catch (Exception ex) {

            }
        }

        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlRepInvestorLedgerControllerBean ejbCreate");
    }
}