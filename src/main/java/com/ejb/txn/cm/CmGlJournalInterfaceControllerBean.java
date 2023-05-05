/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class CmGlJournalInterfaceControllerBean
 * @created December 19, 2003, 10:47 AM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txn.cm;

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
import com.ejb.entities.ad.LocalAdBankAccount;
import com.ejb.dao.ad.LocalAdBankAccountHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.cm.LocalCmAdjustment;
import com.ejb.dao.cm.LocalCmAdjustmentHome;
import com.ejb.entities.cm.LocalCmDistributionRecord;
import com.ejb.dao.cm.LocalCmDistributionRecordHome;
import com.ejb.entities.cm.LocalCmFundTransfer;
import com.ejb.dao.cm.LocalCmFundTransferHome;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.entities.gl.LocalGlJournalInterface;
import com.ejb.dao.gl.LocalGlJournalInterfaceHome;
import com.ejb.entities.gl.LocalGlJournalLineInterface;
import com.ejb.dao.gl.LocalGlJournalLineInterfaceHome;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "CmGlJournalInterfaceControllerEJB")
public class CmGlJournalInterfaceControllerBean extends EJBContextClass implements CmGlJournalInterfaceController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalCmAdjustmentHome cmAdjustmentHome;
    @EJB
    private LocalCmDistributionRecordHome cmDistributionRecordHome;
    @EJB
    private LocalCmFundTransferHome cmFundTransferHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalGlJournalInterfaceHome glJournalInterfaceHome;
    @EJB
    private LocalGlJournalLineInterfaceHome glJournalLineInterfaceHome;

    public long executeCmGlJriImport(Date JRI_DT_FRM, Date JRI_DT_TO, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("CmGlJournalInterfaceControllerBean executeCmGlJriImport");

        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSSS");

        long IMPORTED_JOURNALS = 0L;
        short lineNumber = 0;

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            LocalGlFunctionalCurrency glFunctionalCurrency = adCompany.getGlFunctionalCurrency();

            // fund transfers

            Collection cmFundTransfers = cmFundTransferHome.findPostedFtByFtDateRange(JRI_DT_FRM, JRI_DT_TO, AD_CMPNY);

            Iterator i = cmFundTransfers.iterator();

            while (i.hasNext()) {

                LocalCmFundTransfer cmFundTransfer = (LocalCmFundTransfer) i.next();

                Collection cmFtDistributionRecords = cmDistributionRecordHome.findByDrReversalAndDrImportedAndFtCode(EJBCommon.FALSE, EJBCommon.FALSE, cmFundTransfer.getFtCode(), AD_CMPNY);

                if (!cmFtDistributionRecords.isEmpty()) {

                    // create journal interface

                    LocalGlJournalInterface glJournalInterface = glJournalInterfaceHome.create(cmFundTransfer.getFtReferenceNumber(), cmFundTransfer.getFtMemo(), cmFundTransfer.getFtDate(), "FUND TRANSFERS", "CASH MANAGEMENT", glFunctionalCurrency.getFcName(), null, cmFundTransfer.getFtDocumentNumber(), null, 1d, 'N', EJBCommon.FALSE, null, null, null, AD_BRNCH, AD_CMPNY);

                    IMPORTED_JOURNALS++;
                    lineNumber = 0;

                    // create journal lines for the fund transfer

                    for (Object cmFtDistributionRecord : cmFtDistributionRecords) {

                        LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(cmFundTransfer.getFtAdBaAccountFrom());

                        LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) cmFtDistributionRecord;

                        LocalGlJournalLineInterface glJournalLineInterface = glJournalLineInterfaceHome.create(++lineNumber, cmDistributionRecord.getDrDebit(), this.convertForeignToFunctionalCurrency(adBankAccount.getGlFunctionalCurrency().getFcCode(), adBankAccount.getGlFunctionalCurrency().getFcName(), cmFundTransfer.getFtConversionDate(), cmFundTransfer.getFtConversionRateFrom(), cmDistributionRecord.getDrAmount(), AD_CMPNY), cmDistributionRecord.getGlChartOfAccount().getCoaAccountNumber(), AD_CMPNY);

                        glJournalInterface.addGlJournalLineInterface(glJournalLineInterface);

                        cmDistributionRecord.setDrImported(EJBCommon.TRUE);
                    }
                }
            }

            // adjustments

            Collection cmAdjustments = cmAdjustmentHome.findPostedAdjByAdjDateRange(JRI_DT_FRM, JRI_DT_TO, AD_CMPNY);

            i = cmAdjustments.iterator();

            while (i.hasNext()) {

                LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();

                Collection cmAdjDistributionRecords = cmDistributionRecordHome.findByDrReversalAndDrImportedAndAdjCode(EJBCommon.FALSE, EJBCommon.FALSE, cmAdjustment.getAdjCode(), AD_CMPNY);

                if (!cmAdjDistributionRecords.isEmpty()) {

                    // create journal interface	interest

                    LocalGlJournalInterface glJournalInterface = glJournalInterfaceHome.create(cmAdjustment.getAdjReferenceNumber(), cmAdjustment.getAdjMemo(), cmAdjustment.getAdjDate(), "BANK ADJUSTMENTS", "CASH MANAGEMENT", glFunctionalCurrency.getFcName(), null, cmAdjustment.getAdjDocumentNumber(), null, 1d, 'N', EJBCommon.FALSE, null, null, null, AD_BRNCH, AD_CMPNY);

                    IMPORTED_JOURNALS++;
                    lineNumber = 0;

                    // create journal lines for the adjusment interest

                    for (Object cmAdjDistributionRecord : cmAdjDistributionRecords) {

                        LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) cmAdjDistributionRecord;

                        LocalGlJournalLineInterface glJournalLineInterface = glJournalLineInterfaceHome.create(++lineNumber, cmDistributionRecord.getDrDebit(), this.convertForeignToFunctionalCurrency(cmAdjustment.getAdBankAccount().getGlFunctionalCurrency().getFcCode(), cmAdjustment.getAdBankAccount().getGlFunctionalCurrency().getFcName(), cmAdjustment.getAdjConversionDate(), cmAdjustment.getAdjConversionRate(), cmDistributionRecord.getDrAmount(), AD_CMPNY), cmDistributionRecord.getGlChartOfAccount().getCoaAccountNumber(), AD_CMPNY);

                        glJournalInterface.addGlJournalLineInterface(glJournalLineInterface);

                        cmDistributionRecord.setDrImported(EJBCommon.TRUE);
                    }
                }
            }

            return IMPORTED_JOURNALS;

        } catch (Exception ex) {

            ex.printStackTrace();
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("CmGlJournalInterfaceControllerBean convertForeignToFunctionalCurrency");

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

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("CmGlJournalInterfaceControllerBean ejbCreate");
    }
}