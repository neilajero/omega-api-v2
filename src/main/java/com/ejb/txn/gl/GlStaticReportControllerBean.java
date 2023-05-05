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

import com.ejb.entities.gl.LocalGlStaticReport;
import com.ejb.dao.gl.LocalGlStaticReportHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.EJBContextClass;
import com.util.gl.GlStaticReportDetails;
import com.util.Debug;
import com.util.EJBHomeFactory;

@Stateless(name = "GlStaticReportControllerEJB")
public class GlStaticReportControllerBean extends EJBContextClass implements GlStaticReportController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalGlStaticReportHome glStaticReportHome;

    public ArrayList getGlSrAll(Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlStaticReportControllerBean getGlSrAll");

        Collection glStaticReports = null;

        LocalGlStaticReport glStaticReport = null;

        ArrayList list = new ArrayList();

        try {

            glStaticReports = glStaticReportHome.findSrAll(AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (glStaticReports.isEmpty()) {

            throw new GlobalNoRecordFoundException();

        } else {

            for (Object staticReport : glStaticReports) {

                glStaticReport = (LocalGlStaticReport) staticReport;

                GlStaticReportDetails details = new GlStaticReportDetails();
                details.setSrCode(glStaticReport.getSrCode());
                details.setSrName(glStaticReport.getSrName());
                details.setSrDescription(glStaticReport.getSrDescription());
                details.setSrFileName(glStaticReport.getSrFileName());
                details.setSrDateFrom(glStaticReport.getSrDateFrom());
                details.setSrDateTo(glStaticReport.getSrDateTo());

                list.add(details);
            }
        }
        return list;

    }


    public void addGlSrEntry(GlStaticReportDetails details, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException {

        Debug.print("GlStaticReportControllerBean addGlSrEntry");

        LocalGlStaticReport glStaticReport = null;

        ArrayList list = new ArrayList();

        try {

            glStaticReport = glStaticReportHome.findBySrName(details.getSrName(), AD_CMPNY);

            throw new GlobalRecordAlreadyExistException();

        } catch (GlobalRecordAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

        try {

            // create new responsibility

            glStaticReport = glStaticReportHome.create(details.getSrName(), details.getSrDescription(), details.getSrFileName(), details.getSrDateFrom(), details.getSrDateTo(), AD_CMPNY);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

    }


    public void updateGlSrEntry(GlStaticReportDetails details, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException {

        Debug.print("GlStaticReportControllerBean updateGlSrEntry");

        LocalGlStaticReport glStaticReport = null;

        ArrayList list = new ArrayList();

        try {

            LocalGlStaticReport arExistingResponsibility = glStaticReportHome.findBySrName(details.getSrName(), AD_CMPNY);

            if (!arExistingResponsibility.getSrCode().equals(details.getSrCode())) {

                throw new GlobalRecordAlreadyExistException();

            }

        } catch (GlobalRecordAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

        try {

            // find and update responsibility

            glStaticReport = glStaticReportHome.findByPrimaryKey(details.getSrCode());

            glStaticReport.setSrName(details.getSrName());
            glStaticReport.setSrDescription(details.getSrDescription());
            glStaticReport.setSrDateFrom(details.getSrDateFrom());
            glStaticReport.setSrDateTo(details.getSrDateTo());

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

    }


    public void deleteGlSrEntry(Integer SR_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException {

        Debug.print("GlStaticReportControllerBean deleteGlSrEntry");

        LocalGlStaticReport glStaticReport = null;

        try {

            glStaticReport = glStaticReportHome.findByPrimaryKey(SR_CODE);

        } catch (FinderException ex) {

            throw new GlobalRecordAlreadyDeletedException();

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

        try {

            if (!glStaticReport.getGlUserStaticReports().isEmpty()) {

                throw new GlobalRecordAlreadyAssignedException();

            }

        } catch (GlobalRecordAlreadyAssignedException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

        try {

            em.remove(glStaticReport);

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();

            throw new EJBException(ex.getMessage());

        }

    }


    public GlStaticReportDetails getGlSrBySrCode(Integer SR_CODE) throws GlobalNoRecordFoundException {

        Debug.print("GlStaticReportControllerBean getGlSrAll");

        ArrayList list = new ArrayList();

        GlStaticReportDetails details = new GlStaticReportDetails();

        try {

            LocalGlStaticReport glStaticReport = glStaticReportHome.findByPrimaryKey(SR_CODE);

            details.setSrCode(glStaticReport.getSrCode());
            details.setSrName(glStaticReport.getSrName());
            details.setSrDescription(glStaticReport.getSrDescription());
            details.setSrFileName(glStaticReport.getSrFileName());
            details.setSrDateFrom(glStaticReport.getSrDateFrom());
            details.setSrDateTo(glStaticReport.getSrDateTo());

        } catch (FinderException ex) {

            throw new GlobalNoRecordFoundException();

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        return details;

    }

    // SessionBean methods


    public void ejbCreate() throws CreateException {

        Debug.print("GlStaticReportControllerBean ejbCreate");

    }
}