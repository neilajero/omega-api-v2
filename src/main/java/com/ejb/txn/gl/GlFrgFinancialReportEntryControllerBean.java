package com.ejb.txn.gl;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;


import java.util.ArrayList;
import java.util.Collection;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import javax.naming.NamingException;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.entities.gl.LocalGlFrgColumnSet;
import com.ejb.dao.gl.LocalGlFrgColumnSetHome;
import com.ejb.entities.gl.LocalGlFrgFinancialReport;
import com.ejb.dao.gl.LocalGlFrgFinancialReportHome;
import com.ejb.entities.gl.LocalGlFrgRowSet;
import com.ejb.dao.gl.LocalGlFrgRowSetHome;
import com.util.EJBContextClass;
import com.util.Debug;
import com.util.EJBHomeFactory;
import com.util.mod.gl.GlModFrgFinancialReportDetails;
import com.util.gl.GlFrgFinancialReportDetails;

@Stateless(name = "GlFrgFinancialReportEntryControllerEJB")
public class GlFrgFinancialReportEntryControllerBean extends EJBContextClass implements GlFrgFinancialReportEntryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalGlFrgColumnSetHome glFrgColumnSetHome;
    @EJB
    private LocalGlFrgFinancialReportHome glFrgFinancialReportHome;
    @EJB
    private LocalGlFrgRowSetHome glFrgRowSetHome;


    public ArrayList getGlFrgCsAll(Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlFrgFinancialReportEntryControllerBean getGlFrgCsAll");

        Collection glFrgColumns = null;

        LocalGlFrgColumnSet glFrgColumnSet = null;

        ArrayList list = new ArrayList();


        try {

            glFrgColumns = glFrgColumnSetHome.findCsAll(AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (glFrgColumns.isEmpty()) {

            return null;

        }

        for (Object glFrgColumn : glFrgColumns) {

            glFrgColumnSet = (LocalGlFrgColumnSet) glFrgColumn;

            list.add(glFrgColumnSet.getCsName());

        }

        return list;

    }


    public ArrayList getGlFrgRsAll(Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlFrgFinancialReportEntryControllerBean getGlFrgRsAll");

        Collection glFrgRows = null;

        LocalGlFrgRowSet glFrgRowSet = null;

        ArrayList list = new ArrayList();

        try {

            glFrgRows = glFrgRowSetHome.findRsAll(AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (glFrgRows.isEmpty()) {

            return null;

        }

        for (Object glFrgRow : glFrgRows) {

            glFrgRowSet = (LocalGlFrgRowSet) glFrgRow;

            list.add(glFrgRowSet.getRsName());

        }

        return list;

    }


    public void addGlFrgFrEntry(GlFrgFinancialReportDetails details, String RS_NM, String CS_NM, Integer AD_CMPNY)

            throws GlobalRecordAlreadyExistException {

        Debug.print("GlFrgFinancialReportEntryControllerBean addGlFrgFrEntry");

        LocalGlFrgFinancialReport glFrgFinancialReport = null;

        ArrayList list = new ArrayList();

        try {

            glFrgFinancialReport = glFrgFinancialReportHome.findByFrName(details.getFrName(), AD_CMPNY);

            throw new GlobalRecordAlreadyExistException();

        } catch (GlobalRecordAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

        try {

            // create new financial report

            glFrgFinancialReport = glFrgFinancialReportHome.create(details.getFrName(), details.getFrDescription(), details.getFrTitle(), details.getFrFontSize(), details.getFrFontStyle(), details.getFrHorizontalAlign(), AD_CMPNY);

            LocalGlFrgRowSet glFrgRowSet = glFrgRowSetHome.findByRsName(RS_NM, AD_CMPNY);
            glFrgRowSet.addGlFrgFinancialReport(glFrgFinancialReport);

            LocalGlFrgColumnSet glFrgColumnSet = glFrgColumnSetHome.findByCsName(CS_NM, AD_CMPNY);
            glFrgColumnSet.addGlFrgFinancialReport(glFrgFinancialReport);

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }


    public void updateGlFrgFrEntry(GlFrgFinancialReportDetails details, String RS_NM, String CS_NM, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException {

        Debug.print("GlFrgFinancialReportEntryControllerBean updateGlFrgFrEntry");

        LocalGlFrgFinancialReport glFrgFinancialReport = null;

        ArrayList list = new ArrayList();

        try {

            LocalGlFrgFinancialReport glExistingFinancialReport = glFrgFinancialReportHome.findByFrName(details.getFrName(), AD_CMPNY);

            if (!glExistingFinancialReport.getFrCode().equals(details.getFrCode())) {

                throw new GlobalRecordAlreadyExistException();

            }

        } catch (GlobalRecordAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

        try {

            // find and update financial report

            glFrgFinancialReport = glFrgFinancialReportHome.findByPrimaryKey(details.getFrCode());

            glFrgFinancialReport.setFrName(details.getFrName());
            glFrgFinancialReport.setFrDescription(details.getFrDescription());
            glFrgFinancialReport.setFrTitle(details.getFrTitle());
            glFrgFinancialReport.setFrFontSize(details.getFrFontSize());
            glFrgFinancialReport.setFrFontStyle(details.getFrFontStyle());
            glFrgFinancialReport.setFrHorizontalAlign(details.getFrHorizontalAlign());

            try {

                LocalGlFrgRowSet glFrgRowSet = glFrgRowSetHome.findByRsName(RS_NM, AD_CMPNY);
                glFrgRowSet.addGlFrgFinancialReport(glFrgFinancialReport);

            } catch (FinderException ex) {

            }

            try {

                LocalGlFrgColumnSet glFrgColumnSet = glFrgColumnSetHome.findByCsName(CS_NM, AD_CMPNY);
                glFrgColumnSet.addGlFrgFinancialReport(glFrgFinancialReport);

            } catch (FinderException ex) {

            }


        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

    }


    public void deleteGlFrgFrEntry(Integer FR_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException {

        Debug.print("GlFrgFinancialReportEntryControllerBean deleteGlFrgFrEntry");

        Collection arPaymentMethods = null;

        LocalGlFrgFinancialReport glFrgFinancialReport = null;

        try {

            glFrgFinancialReport = glFrgFinancialReportHome.findByPrimaryKey(FR_CODE);

        } catch (FinderException ex) {

            throw new GlobalRecordAlreadyDeletedException();

        }

        try {

            em.remove(glFrgFinancialReport);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

    }


    public ArrayList getGlFrgFrAll(Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlFrgFinancialReportEntryControllerBean getGlFrgFrAll");

        Collection glFrgFinancialReports = null;

        LocalGlFrgFinancialReport glFrgFinancialReport = null;

        ArrayList list = new ArrayList();

        try {

            glFrgFinancialReports = glFrgFinancialReportHome.findFrAll(AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (glFrgFinancialReports.isEmpty()) {

            throw new GlobalNoRecordFoundException();

        }

        for (Object frgFinancialReport : glFrgFinancialReports) {

            glFrgFinancialReport = (LocalGlFrgFinancialReport) frgFinancialReport;

            GlModFrgFinancialReportDetails mdetails = new GlModFrgFinancialReportDetails();
            mdetails.setFrCode(glFrgFinancialReport.getFrCode());
            mdetails.setFrName(glFrgFinancialReport.getFrName());
            mdetails.setFrDescription(glFrgFinancialReport.getFrDescription());
            mdetails.setFrTitle(glFrgFinancialReport.getFrTitle());
            mdetails.setFrRowName(glFrgFinancialReport.getGlFrgRowSet().getRsName());
            mdetails.setFrColumnName(glFrgFinancialReport.getGlFrgColumnSet().getCsName());
            mdetails.setFrFontSize(glFrgFinancialReport.getFrFontSize());
            mdetails.setFrFontStyle(glFrgFinancialReport.getFrFontStyle());
            mdetails.setFrHorizontalAlign(glFrgFinancialReport.getFrHorizontalAlign());

            list.add(mdetails);
        }

        return list;

    }


    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlFrgFinancialReportEntryControllerBean ejbCreate");

    }
}