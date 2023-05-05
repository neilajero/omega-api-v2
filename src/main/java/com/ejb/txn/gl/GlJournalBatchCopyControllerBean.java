package com.ejb.txn.gl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalTransactionBatchCloseException;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.entities.gl.LocalGlJournal;
import com.ejb.entities.gl.LocalGlJournalBatch;
import com.ejb.dao.gl.LocalGlJournalBatchHome;
import com.ejb.dao.gl.LocalGlJournalHome;
import com.ejb.entities.gl.LocalGlJournalLine;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.mod.gl.GlModJournalDetails;

@Stateless(name = "GlJournalBatchCopyControllerEJB")
public class GlJournalBatchCopyControllerBean extends EJBContextClass implements GlJournalBatchCopyController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalGlJournalHome glJournalHome;
    @EJB
    private LocalGlJournalBatchHome glJournalBatchHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;

    public ArrayList getGlOpenJbAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("GlJournalBatchCopyControllerBean getGlOpenJbAll");

        ArrayList list = new ArrayList();

        try {

            Collection glJournalBatches = glJournalBatchHome.findOpenJbAll(AD_BRNCH, AD_CMPNY);

            for (Object journalBatch : glJournalBatches) {

                LocalGlJournalBatch glJournalBatch = (LocalGlJournalBatch) journalBatch;

                list.add(glJournalBatch.getJbName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void executeGlJrBatchCopy(ArrayList list, String JB_NM_TO, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalTransactionBatchCloseException {

        Debug.print("ApVoucherBatchCopyControllerBean executeGlJrBatchCopy");

        try {

            // find journal batch to

            LocalGlJournalBatch glJournalBatchTo = glJournalBatchHome.findByJbName(JB_NM_TO, AD_BRNCH, AD_CMPNY);

            // validate if batch to is closed

            if (glJournalBatchTo.getJbStatus().equals("CLOSED")) {

                throw new GlobalTransactionBatchCloseException();
            }

            for (Object o : list) {

                LocalGlJournal glJournal = glJournalHome.findByPrimaryKey((Integer) o);
                glJournal.getGlJournalBatch().dropGlJournal(glJournal);
                glJournalBatchTo.addGlJournal(glJournal);
            }

        } catch (GlobalTransactionBatchCloseException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGlJrByJbName(String JB_NM, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlFindJournalControllerBean getGlJrByJbName");

        ArrayList jrList = new ArrayList();

        Collection glJournals = null;

        try {

            glJournals = glJournalHome.findByJbName(JB_NM, AD_CMPNY);

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }

        if (glJournals.isEmpty()) throw new GlobalNoRecordFoundException();

        for (Object journal : glJournals) {

            double TOTAL_DEBIT = 0d;
            double TOTAL_CREDIT = 0d;

            LocalGlJournal glJournal = (LocalGlJournal) journal;
            Collection glJournalLines = glJournal.getGlJournalLines();

            for (Object journalLine : glJournalLines) {

                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) journalLine;

                if (glJournalLine.getJlDebit() == EJBCommon.TRUE) {

                    TOTAL_DEBIT += this.convertForeignToFunctionalCurrency(glJournal.getGlFunctionalCurrency().getFcCode(), glJournal.getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), glJournal.getJrConversionRate(), glJournalLine.getJlAmount(), AD_CMPNY);

                } else {

                    TOTAL_CREDIT += this.convertForeignToFunctionalCurrency(glJournal.getGlFunctionalCurrency().getFcCode(), glJournal.getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), glJournal.getJrConversionRate(), glJournalLine.getJlAmount(), AD_CMPNY);
                }
            }

            GlModJournalDetails mdetails = new GlModJournalDetails();
            mdetails.setJrCode(glJournal.getJrCode());
            mdetails.setJrName(glJournal.getJrName());
            mdetails.setJrDescription(glJournal.getJrDescription());
            mdetails.setJrEffectiveDate(glJournal.getJrEffectiveDate());
            mdetails.setJrDocumentNumber(glJournal.getJrDocumentNumber());
            mdetails.setJrTotalDebit(TOTAL_DEBIT);
            mdetails.setJrTotalCredit(TOTAL_CREDIT);
            mdetails.setJrJcName(glJournal.getGlJournalCategory().getJcName());
            mdetails.setJrJsName(glJournal.getGlJournalSource().getJsName());
            mdetails.setJrFcName(glJournal.getGlFunctionalCurrency().getFcName());

            jrList.add(mdetails);
        }

        return jrList;
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("GlJournalBatchCopyControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("GlJournalBatchCopyControllerBean convertForeignToFunctionalCurrency");

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

        Debug.print("GlJournalBatchCopyControllerBean ejbCreate");
    }
}