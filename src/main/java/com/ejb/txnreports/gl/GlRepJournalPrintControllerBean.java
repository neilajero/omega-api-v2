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
import com.ejb.entities.ad.LocalAdApproval;
import com.ejb.entities.ad.LocalAdApprovalDocument;
import com.ejb.dao.ad.LocalAdApprovalDocumentHome;
import com.ejb.dao.ad.LocalAdApprovalHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdUser;
import com.ejb.dao.ad.LocalAdUserHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.gl.LocalGlJournal;
import com.ejb.dao.gl.LocalGlJournalHome;
import com.ejb.entities.gl.LocalGlJournalLine;
import com.ejb.dao.gl.LocalGlJournalLineHome;
import com.util.ad.AdCompanyDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.reports.gl.GlRepJournalPrintDetails;

@Stateless(name = "GlRepJournalPrintControllerEJB")
public class GlRepJournalPrintControllerBean extends EJBContextClass implements GlRepJournalPrintController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdApprovalDocumentHome adApprovalDocumentHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalGlJournalHome glJournalHome;
    @EJB
    private LocalGlJournalLineHome glJournalLineHome;


    public ArrayList executeGlRepJournalPrint(ArrayList jrCodeList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlRepJournalPrintControllerBean executeGlRepJournalPrint");

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
                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(AD_CMPNY);
                LocalAdApprovalDocument adApprovalDocument = adApprovalDocumentHome.findByAdcType("GL JOURNAL", AD_CMPNY);

                if (adApprovalDocument.getAdcPrintOption().equals("PRINT APPROVED ONLY")) {

                    if (glJournal.getJrApprovalStatus() == null || glJournal.getJrApprovalStatus().equals("PENDING")) {

                        continue;
                    }

                } else if (adApprovalDocument.getAdcPrintOption().equals("PRINT UNAPPROVED ONLY")) {

                    if (glJournal.getJrApprovalStatus() != null && (glJournal.getJrApprovalStatus().equals("N/A") || glJournal.getJrApprovalStatus().equals("APPROVED"))) {

                        continue;
                    }
                }

                if (adApprovalDocument.getAdcAllowDuplicate() == EJBCommon.FALSE && glJournal.getJrPrinted() == EJBCommon.TRUE) {

                    continue;
                }

                // show duplicate

                boolean showDuplicate = adApprovalDocument.getAdcTrackDuplicate() == EJBCommon.TRUE && glJournal.getJrPrinted() == EJBCommon.TRUE;

                // set jr printed

                glJournal.setJrPrinted(EJBCommon.TRUE);

                // get journal lines

                Collection glJournalLines = glJournalLineHome.findByJrCode(glJournal.getJrCode(), AD_CMPNY);

                for (Object journalLine : glJournalLines) {

                    LocalGlJournalLine glJournalLine = (LocalGlJournalLine) journalLine;

                    GlRepJournalPrintDetails details = new GlRepJournalPrintDetails();

                    details.setJpJrEffectiveDate(glJournal.getJrEffectiveDate());
                    details.setJpJrDocumentNumber(glJournal.getJrDocumentNumber());
                    details.setJpJrCreatedBy(glJournal.getJrCreatedBy());
                    details.setJpJrApprovedRejectedBy(glJournal.getJrApprovedRejectedBy());
                    details.setJpJlDebit(glJournalLine.getJlDebit());
                    details.setJpJlAmount(glJournalLine.getJlAmount());
                    details.setJpJlCoaAccountNumber(glJournalLine.getGlChartOfAccount().getCoaAccountNumber());
                    details.setJpJlCoaAccountDescription(glJournalLine.getGlChartOfAccount().getCoaAccountDescription());
                    details.setJpShowDuplicate(showDuplicate ? EJBCommon.TRUE : EJBCommon.FALSE);
                    details.setJpJrApprovalStatus(glJournal.getJrApprovalStatus());
                    details.setJpJrPosted(glJournal.getJrPosted());
                    details.setJpJrDescription(glJournal.getJrDescription());
                    details.setJpJrDateReversal(glJournal.getJrDateReversal());
                    details.setJpJrReversed(glJournal.getJrReversed());
                    details.setJpJrPostedBy(glJournal.getJrPostedBy());
                    details.setJpJrJcName(glJournal.getGlJournalCategory().getJcName());
                    details.setJpJrJsName(glJournal.getGlJournalSource().getJsName());
                    details.setJpJrFcName(glJournal.getGlFunctionalCurrency().getFcName());
                    details.setJpJrFcSymbol(glJournal.getGlFunctionalCurrency().getFcSymbol());
                    details.setJpJrJbName(glJournal.getGlJournalBatch() != null ? glJournal.getGlJournalBatch().getJbName() : null);
                    details.setJpJrName(glJournal.getJrName());

                    // get created by user description
                    try {
                        LocalAdUser adUser = adUserHome.findByUsrName(glJournal.getJrCreatedBy(), AD_CMPNY);
                        details.setJpJrCreatedByDescription(adUser.getUsrDescription());
                    } catch (Exception e) {
                        details.setJpJrCreatedByDescription(glJournal.getJrCreatedBy());
                    }

                    try {
                        Debug.print("setJpJrApprovedByDescription(): " + glJournal.getJrApprovedRejectedBy());
                        LocalAdUser adUser3 = adUserHome.findByUsrName(glJournal.getJrApprovedRejectedBy(), AD_CMPNY);
                        details.setJpJrApprovedByDescription(adUser3.getUsrDescription());

                    } catch (Exception e) {
                        details.setJpJrApprovedByDescription("");
                    }

                    // Include Branch
                    LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(glJournal.getJrAdBranch());
                    details.setBrBranchCode(adBranch.getBrBranchCode());
                    details.setBrName(adBranch.getBrName());

                    details.setJpJlDescription(glJournalLine.getJlDescription());

                    list.add(details);
                }
            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            list.sort(GlRepJournalPrintDetails.accountDescriptionComparator);
            list.sort(GlRepJournalPrintDetails.debitComparator);

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("GlRepJournalPrintControllerBean getAdCompany");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());
            details.setCmpAddress(adCompany.getCmpAddress());
            details.setCmpCity(adCompany.getCmpCity());
            details.setCmpContact(adCompany.getCmpContact());
            details.setCmpCountry(adCompany.getCmpCountry());
            details.setCmpEmail(adCompany.getCmpEmail());
            details.setCmpTin(adCompany.getCmpTin());
            details.setCmpDescription(adCompany.getCmpDescription());
            details.setCmpPhone(adCompany.getCmpPhone());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlRepJournalPrintControllerBean ejbCreate");
    }
}