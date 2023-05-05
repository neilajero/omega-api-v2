/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ApGlJournalInterfaceControllerBean
 * @created February 26, 2004, 09:43 AM
 * @author Dennis M. Hilario
 */
package com.ejb.txn.ap;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ap.LocalApCheck;
import com.ejb.dao.ap.LocalApCheckHome;
import com.ejb.entities.ap.LocalApDistributionRecord;
import com.ejb.dao.ap.LocalApDistributionRecordHome;
import com.ejb.entities.ap.LocalApVoucher;
import com.ejb.dao.ap.LocalApVoucherHome;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.entities.gl.LocalGlFunctionalCurrencyRate;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.entities.gl.LocalGlJournalInterface;
import com.ejb.dao.gl.LocalGlJournalInterfaceHome;
import com.ejb.entities.gl.LocalGlJournalLineInterface;
import com.ejb.dao.gl.LocalGlJournalLineInterfaceHome;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ApGlJournalInterfaceControllerEJB")
public class ApGlJournalInterfaceControllerBean extends EJBContextClass implements ApGlJournalInterfaceController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    private LocalApVoucherHome apVoucherHome;
    @EJB
    private LocalApCheckHome apCheckHome;
    @EJB
    private LocalGlJournalInterfaceHome glJournalInterfaceHome;
    @EJB
    private LocalGlJournalLineInterfaceHome glJournalLineInterfaceHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;

    public long executeApGlJriRun(Date JRI_DT_FRM, Date JRI_DT_TO, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ApGlJournalInterfaceControllerBean executeApChkPost");

        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSSS");
        long IMPORTED_JOURNALS = 0L;
        short lineNumber = 0;

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            LocalGlFunctionalCurrency glFunctionalCurrency = adCompany.getGlFunctionalCurrency();

            // voucher

            Collection apVouchers = apVoucherHome.findPostedVouByVouDebitMemoAndVouDateRange(EJBCommon.FALSE, JRI_DT_FRM, JRI_DT_TO, AD_CMPNY);

            Iterator i = apVouchers.iterator();

            while (i.hasNext()) {

                LocalApVoucher apVoucher = (LocalApVoucher) i.next();

                Collection apDistributionRecords = apDistributionRecordHome.findImportableDrByVouCode(apVoucher.getVouCode(), AD_CMPNY);

                if (!apDistributionRecords.isEmpty()) {

                    LocalGlJournalInterface glJournalInterface = glJournalInterfaceHome.create(apVoucher.getVouReferenceNumber(), apVoucher.getVouDescription(), apVoucher.getVouDate(), "VOUCHERS", "ACCOUNTS PAYABLES", glFunctionalCurrency.getFcName(), null, apVoucher.getVouDocumentNumber(), null, 1d, 'N', EJBCommon.FALSE, apVoucher.getApSupplier().getSplTin(), apVoucher.getApSupplier().getSplName(), null, AD_BRNCH, AD_CMPNY);

                    IMPORTED_JOURNALS++;
                    lineNumber = 0;

                    for (Object distributionRecord : apDistributionRecords) {

                        LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) distributionRecord;

                        LocalGlJournalLineInterface glJournalLineInterface = glJournalLineInterfaceHome.create(++lineNumber, apDistributionRecord.getDrDebit(), this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apDistributionRecord.getDrAmount(), AD_CMPNY), apDistributionRecord.getGlChartOfAccount().getCoaAccountNumber(), AD_CMPNY);

                        glJournalInterface.addGlJournalLineInterface(glJournalLineInterface);

                        apDistributionRecord.setDrImported(EJBCommon.TRUE);
                    }
                }
            }

            // debit memo

            apVouchers = apVoucherHome.findPostedVouByVouDebitMemoAndVouDateRange(EJBCommon.TRUE, JRI_DT_FRM, JRI_DT_TO, AD_CMPNY);

            i = apVouchers.iterator();

            while (i.hasNext()) {

                LocalApVoucher apVoucher = (LocalApVoucher) i.next();

                LocalApVoucher apDebitedVoucher = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(apVoucher.getVouDmVoucherNumber(), EJBCommon.FALSE, AD_BRNCH, AD_CMPNY);

                Collection apDistributionRecords = apDistributionRecordHome.findImportableDrByVouCode(apVoucher.getVouCode(), AD_CMPNY);

                if (!apDistributionRecords.isEmpty()) {

                    LocalGlJournalInterface glJournalInterface = glJournalInterfaceHome.create(apVoucher.getVouReferenceNumber(), apVoucher.getVouDescription(), apVoucher.getVouDate(), "DEBIT MEMOS", "ACCOUNTS PAYABLES", glFunctionalCurrency.getFcName(), null, apVoucher.getVouDocumentNumber(), null, 1d, 'N', EJBCommon.FALSE, apVoucher.getApSupplier().getSplTin(), apVoucher.getApSupplier().getSplName(), null, AD_BRNCH, AD_CMPNY);

                    IMPORTED_JOURNALS++;
                    lineNumber = 0;

                    for (Object distributionRecord : apDistributionRecords) {

                        LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) distributionRecord;

                        LocalGlJournalLineInterface glJournalLineInterface = glJournalLineInterfaceHome.create(++lineNumber, apDistributionRecord.getDrDebit(), this.convertForeignToFunctionalCurrency(apDebitedVoucher.getGlFunctionalCurrency().getFcCode(), apDebitedVoucher.getGlFunctionalCurrency().getFcName(), apDebitedVoucher.getVouConversionDate(), apDebitedVoucher.getVouConversionRate(), apDistributionRecord.getDrAmount(), AD_CMPNY), apDistributionRecord.getGlChartOfAccount().getCoaAccountNumber(), AD_CMPNY);

                        glJournalInterface.addGlJournalLineInterface(glJournalLineInterface);

                        apDistributionRecord.setDrImported(EJBCommon.TRUE);
                    }
                }
            }

            // checks

            Collection apChecks = apCheckHome.findPostedChkByChkDateRange(JRI_DT_FRM, JRI_DT_TO, AD_CMPNY);

            i = apChecks.iterator();

            while (i.hasNext()) {

                LocalApCheck apCheck = (LocalApCheck) i.next();

                Collection apDistributionRecords = apDistributionRecordHome.findImportableDrByDrReversedAndChkCode(EJBCommon.FALSE, apCheck.getChkCode(), AD_CMPNY);

                if (!apDistributionRecords.isEmpty()) {

                    LocalGlJournalInterface glJournalInterface = glJournalInterfaceHome.create(apCheck.getChkReferenceNumber(), apCheck.getChkDescription(), apCheck.getChkDate(), "CHECKS", "ACCOUNTS PAYABLES", glFunctionalCurrency.getFcName(), null, apCheck.getChkDocumentNumber(), null, 1d, 'N', EJBCommon.FALSE, apCheck.getApSupplier().getSplTin(), apCheck.getApSupplier().getSplName(), null, AD_BRNCH, AD_CMPNY);

                    IMPORTED_JOURNALS++;
                    lineNumber = 0;

                    for (Object distributionRecord : apDistributionRecords) {

                        LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) distributionRecord;

                        double JLI_AMNT = 0;

                        if (apDistributionRecord.getApAppliedVoucher() != null) {

                            LocalApVoucher apVoucher = apDistributionRecord.getApAppliedVoucher().getApVoucherPaymentSchedule().getApVoucher();

                            JLI_AMNT = this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apDistributionRecord.getDrAmount(), AD_CMPNY);

                        } else {

                            JLI_AMNT = this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apDistributionRecord.getDrAmount(), AD_CMPNY);
                        }

                        LocalGlJournalLineInterface glJournalLineInterface = glJournalLineInterfaceHome.create(++lineNumber, apDistributionRecord.getDrDebit(), JLI_AMNT, apDistributionRecord.getGlChartOfAccount().getCoaAccountNumber(), AD_CMPNY);

                        glJournalInterface.addGlJournalLineInterface(glJournalLineInterface);

                        apDistributionRecord.setDrImported(EJBCommon.TRUE);
                    }
                }
            }

            // void/reversed checks

            apChecks = apCheckHome.findVoidPostedChkByChkDateRange(JRI_DT_FRM, JRI_DT_TO, AD_CMPNY);

            i = apChecks.iterator();

            while (i.hasNext()) {

                LocalApCheck apCheck = (LocalApCheck) i.next();

                Collection apDistributionRecords = apDistributionRecordHome.findImportableDrByDrReversedAndChkCode(EJBCommon.TRUE, apCheck.getChkCode(), AD_CMPNY);

                if (!apDistributionRecords.isEmpty()) {

                    LocalGlJournalInterface glJournalInterface = glJournalInterfaceHome.create("REVERSED " + apCheck.getChkReferenceNumber(), apCheck.getChkDescription(), apCheck.getChkDate(), "CHECKS", "ACCOUNTS PAYABLES", glFunctionalCurrency.getFcName(), null, apCheck.getChkDocumentNumber(), null, 1d, 'N', EJBCommon.FALSE, apCheck.getApSupplier().getSplTin(), apCheck.getApSupplier().getSplName(), null, AD_BRNCH, AD_CMPNY);

                    IMPORTED_JOURNALS++;
                    lineNumber = 0;

                    for (Object distributionRecord : apDistributionRecords) {

                        LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) distributionRecord;

                        double JLI_AMNT = 0;

                        if (apDistributionRecord.getApAppliedVoucher() != null) {

                            LocalApVoucher apVoucher = apDistributionRecord.getApAppliedVoucher().getApVoucherPaymentSchedule().getApVoucher();

                            JLI_AMNT = this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apDistributionRecord.getDrAmount(), AD_CMPNY);

                        } else {

                            JLI_AMNT = this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apDistributionRecord.getDrAmount(), AD_CMPNY);
                        }

                        LocalGlJournalLineInterface glJournalLineInterface = glJournalLineInterfaceHome.create(++lineNumber, apDistributionRecord.getDrDebit(), JLI_AMNT, apDistributionRecord.getGlChartOfAccount().getCoaAccountNumber(), AD_CMPNY);

                        glJournalInterface.addGlJournalLineInterface(glJournalLineInterface);

                        apDistributionRecord.setDrImported(EJBCommon.TRUE);
                    }
                }
            }

            return IMPORTED_JOURNALS;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("ApGlJournalInterfaceControllerBean convertForeignToFunctionalCurrency");

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

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApGlJournalInterfaceControllerBean ejbCreate");
    }

    // private methods

}