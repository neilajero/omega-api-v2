package com.ejb.txnreports.gl;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;


import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.gl.LocalGlJournal;
import com.ejb.dao.gl.LocalGlJournalHome;
import com.ejb.entities.gl.LocalGlJournalLine;
import com.util.ad.AdCompanyDetails;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.reports.gl.GlRepJournalEditListDetails;

@Stateless(name = "GlRepJournalEditListControllerEJB")
public class GlRepJournalEditListControllerBean extends EJBContextClass implements GlRepJournalEditListController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalGlJournalHome glJournalHome;

    public ArrayList executeGlRepJournalEditList(ArrayList jrCodeList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlRepJournalEditListControllerBean executeGlRepJournalEditList");

        ArrayList list = new ArrayList();

        try {

            for (Object o : jrCodeList) {

                Integer JR_CODE = (Integer) o;

                LocalGlJournal glJournal = null;

                try {

                    glJournal = glJournalHome.findByPrimaryKey(JR_CODE);

                } catch (FinderException ex) {

                    continue;
                }

                // get journal lines

                Collection glJournalLines = glJournal.getGlJournalLines();

                for (Object journalLine : glJournalLines) {

                    LocalGlJournalLine glJournalLine = (LocalGlJournalLine) journalLine;

                    GlRepJournalEditListDetails details = new GlRepJournalEditListDetails();

                    details.setJelJrDateCreated(glJournal.getJrDateCreated());
                    details.setJelJrCreatedBy(glJournal.getJrCreatedBy());
                    details.setJelJrDate(glJournal.getJrEffectiveDate());
                    details.setJelJrDocumentNumber(glJournal.getJrDocumentNumber());
                    details.setJelJrReferenceNumber(glJournal.getJrName());
                    details.setJelJrDescription(glJournal.getJrDescription());
                    details.setJelJrBatchName(glJournal.getGlJournalBatch() != null ? glJournal.getGlJournalBatch().getJbName() : "N/A");
                    details.setJelJrBatchDescription(glJournal.getGlJournalBatch() != null ? glJournal.getGlJournalBatch().getJbDescription() : "N/A");
                    details.setJelJrTransactionTotal(glJournal.getGlJournalBatch() != null ? glJournal.getGlJournalBatch().getGlJournals().size() : 0);
                    details.setJelJlAccountNumber(glJournalLine.getGlChartOfAccount().getCoaAccountNumber());
                    details.setJelJlDebit(glJournalLine.getJlDebit());
                    details.setJelJlAmount(glJournalLine.getJlAmount());
                    details.setJelJlAccountDescription(glJournalLine.getGlChartOfAccount().getCoaAccountDescription());
                    details.setJelJlDescription(glJournalLine.getJlDescription());

                    list.add(details);
                }
            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("GlRepJournalEditListControllerBean getAdCompany");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlRepJournalEditListControllerBean ejbCreate");
    }
}