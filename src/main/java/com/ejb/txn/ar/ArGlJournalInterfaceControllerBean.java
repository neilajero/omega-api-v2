/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArGlJournalInterfaceControllerBean
 * @created March 10, 2004, 01:17 PM
 * @author Dennis M. Hilario
 */
package com.ejb.txn.ar;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ar.LocalArDistributionRecord;
import com.ejb.dao.ar.LocalArDistributionRecordHome;
import com.ejb.entities.ar.LocalArInvoice;
import com.ejb.dao.ar.LocalArInvoiceHome;
import com.ejb.entities.ar.LocalArReceipt;
import com.ejb.dao.ar.LocalArReceiptHome;
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

@Stateless(name = "ArGlJournalInterfaceControllerEJB")
public class ArGlJournalInterfaceControllerBean extends EJBContextClass implements ArGlJournalInterfaceController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalGlJournalInterfaceHome glJournalInterfaceHome;
    @EJB
    private LocalGlJournalLineInterfaceHome glJournalLineInterfaceHome;

    public long executeArGlJriRun(Date JRI_DT_FRM, Date JRI_DT_TO, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ArGlJournalInterfaceControllerBean executeApRctPost");

        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSSS");
        long IMPORTED_JOURNALS = 0L;
        short lineNumber = 0;
        
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            LocalGlFunctionalCurrency glFunctionalCurrency = adCompany.getGlFunctionalCurrency();

            // invoice

            Collection arInvoices = arInvoiceHome.findPostedInvByInvCreditMemoAndInvDateRange(EJBCommon.FALSE, JRI_DT_FRM, JRI_DT_TO, AD_CMPNY);

            Iterator i = arInvoices.iterator();

            while (i.hasNext()) {

                LocalArInvoice arInvoice = (LocalArInvoice) i.next();

                Collection arDistributionRecords = arDistributionRecordHome.findImportableDrByInvCode(arInvoice.getInvCode(), AD_CMPNY);

                if (!arDistributionRecords.isEmpty()) {

                    LocalGlJournalInterface glJournalInterface = glJournalInterfaceHome.create(arInvoice.getInvReferenceNumber(), arInvoice.getInvDescription(), arInvoice.getInvDate(), "SALES INVOICES", "ACCOUNTS RECEIVABLES", glFunctionalCurrency.getFcName(), null, arInvoice.getInvNumber(), null, 1d, 'N', EJBCommon.FALSE, arInvoice.getArCustomer().getCstTin(), arInvoice.getArCustomer().getCstName(), null, AD_BRNCH, AD_CMPNY);

                    IMPORTED_JOURNALS++;
                    lineNumber = 0;

                    for (Object distributionRecord : arDistributionRecords) {

                        LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                        LocalGlJournalLineInterface glJournalLineInterface = glJournalLineInterfaceHome.create(++lineNumber, arDistributionRecord.getDrDebit(), this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), AD_CMPNY), arDistributionRecord.getGlChartOfAccount().getCoaAccountNumber(), AD_CMPNY);

                        glJournalInterface.addGlJournalLineInterface(glJournalLineInterface);

                        arDistributionRecord.setDrImported(EJBCommon.TRUE);
                    }
                }
            }

            // credit memos

            arInvoices = arInvoiceHome.findPostedInvByInvCreditMemoAndInvDateRange(EJBCommon.TRUE, JRI_DT_FRM, JRI_DT_TO, AD_CMPNY);

            i = arInvoices.iterator();

            while (i.hasNext()) {

                LocalArInvoice arInvoice = (LocalArInvoice) i.next();

                LocalArInvoice arCreditedInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arInvoice.getInvCmInvoiceNumber(), EJBCommon.FALSE, AD_BRNCH, AD_CMPNY);

                Collection arDistributionRecords = arDistributionRecordHome.findImportableDrByInvCode(arInvoice.getInvCode(), AD_CMPNY);

                if (!arDistributionRecords.isEmpty()) {

                    LocalGlJournalInterface glJournalInterface = glJournalInterfaceHome.create(arInvoice.getInvReferenceNumber(), arInvoice.getInvDescription(), arInvoice.getInvDate(), "CREDIT MEMOS", "ACCOUNTS RECEIVABLES", glFunctionalCurrency.getFcName(), null, arInvoice.getInvNumber(), null, 1d, 'N', EJBCommon.FALSE, arInvoice.getArCustomer().getCstTin(), arInvoice.getArCustomer().getCstName(), null, AD_BRNCH, AD_CMPNY);

                    IMPORTED_JOURNALS++;
                    lineNumber = 0;

                    for (Object distributionRecord : arDistributionRecords) {

                        LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                        LocalGlJournalLineInterface glJournalLineInterface = glJournalLineInterfaceHome.create(++lineNumber, arDistributionRecord.getDrDebit(), this.convertForeignToFunctionalCurrency(arCreditedInvoice.getGlFunctionalCurrency().getFcCode(), arCreditedInvoice.getGlFunctionalCurrency().getFcName(), arCreditedInvoice.getInvConversionDate(), arCreditedInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), AD_CMPNY), arDistributionRecord.getGlChartOfAccount().getCoaAccountNumber(), AD_CMPNY);

                        glJournalInterface.addGlJournalLineInterface(glJournalLineInterface);

                        arDistributionRecord.setDrImported(EJBCommon.TRUE);
                    }
                }
            }

            // receipts

            Collection arReceipts = arReceiptHome.findPostedRctByRctDateRange(JRI_DT_FRM, JRI_DT_TO, AD_CMPNY);

            i = arReceipts.iterator();

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();

                Collection arDistributionRecords = arDistributionRecordHome.findImportableDrByDrReversedAndRctCode(EJBCommon.FALSE, arReceipt.getRctCode(), AD_CMPNY);

                if (!arDistributionRecords.isEmpty()) {

                    LocalGlJournalInterface glJournalInterface = glJournalInterfaceHome.create(arReceipt.getRctReferenceNumber(), arReceipt.getRctDescription(), arReceipt.getRctDate(), "SALES RECEIPTS", "ACCOUNTS RECEIVABLES", glFunctionalCurrency.getFcName(), null, arReceipt.getRctNumber(), null, 1d, 'N', EJBCommon.FALSE, arReceipt.getArCustomer().getCstTin(), arReceipt.getArCustomer().getCstName(), null, AD_BRNCH, AD_CMPNY);

                    IMPORTED_JOURNALS++;
                    lineNumber = 0;

                    for (Object distributionRecord : arDistributionRecords) {

                        LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                        double JLI_AMNT = 0;

                        if (arDistributionRecord.getArAppliedInvoice() != null) {

                            LocalArInvoice arInvoice = arDistributionRecord.getArAppliedInvoice().getArInvoicePaymentSchedule().getArInvoice();

                            JLI_AMNT = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), AD_CMPNY);

                        } else {

                            JLI_AMNT = this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arDistributionRecord.getDrAmount(), AD_CMPNY);
                        }

                        LocalGlJournalLineInterface glJournalLineInterface = glJournalLineInterfaceHome.create(++lineNumber, arDistributionRecord.getDrDebit(), JLI_AMNT, arDistributionRecord.getGlChartOfAccount().getCoaAccountNumber(), AD_CMPNY);

                        glJournalInterface.addGlJournalLineInterface(glJournalLineInterface);

                        arDistributionRecord.setDrImported(EJBCommon.TRUE);
                    }
                }
            }

            // void/reversed receipts

            arReceipts = arReceiptHome.findVoidPostedRctByRctDateRange(JRI_DT_FRM, JRI_DT_TO, AD_CMPNY);

            i = arReceipts.iterator();

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();

                Collection arDistributionRecords = arDistributionRecordHome.findImportableDrByDrReversedAndRctCode(EJBCommon.TRUE, arReceipt.getRctCode(), AD_CMPNY);

                if (!arDistributionRecords.isEmpty()) {

                    LocalGlJournalInterface glJournalInterface = glJournalInterfaceHome.create("REVERSED " + arReceipt.getRctReferenceNumber(), arReceipt.getRctDescription(), arReceipt.getRctDate(), "SALES RECEIPTS", "ACCOUNTS RECEIVABLES", glFunctionalCurrency.getFcName(), null, arReceipt.getRctNumber(), null, 1d, 'N', EJBCommon.FALSE, arReceipt.getArCustomer().getCstTin(), arReceipt.getArCustomer().getCstName(), null, AD_BRNCH, AD_CMPNY);

                    IMPORTED_JOURNALS++;
                    lineNumber = 0;

                    for (Object distributionRecord : arDistributionRecords) {

                        LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                        double JLI_AMNT = 0;

                        if (arDistributionRecord.getArAppliedInvoice() != null) {

                            LocalArInvoice arInvoice = arDistributionRecord.getArAppliedInvoice().getArInvoicePaymentSchedule().getArInvoice();

                            JLI_AMNT = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), AD_CMPNY);

                        } else {

                            JLI_AMNT = this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arDistributionRecord.getDrAmount(), AD_CMPNY);
                        }

                        LocalGlJournalLineInterface glJournalLineInterface = glJournalLineInterfaceHome.create(++lineNumber, arDistributionRecord.getDrDebit(), JLI_AMNT, arDistributionRecord.getGlChartOfAccount().getCoaAccountNumber(), AD_CMPNY);

                        glJournalInterface.addGlJournalLineInterface(glJournalLineInterface);

                        arDistributionRecord.setDrImported(EJBCommon.TRUE);
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

        Debug.print("ArGlJournalInterfaceControllerBean convertForeignToFunctionalCurrency");

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

        Debug.print("ArGlJournalInterfaceControllerBean ejbCreate");
    }

    // private methods

}