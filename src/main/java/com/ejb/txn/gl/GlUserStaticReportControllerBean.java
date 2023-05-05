package com.ejb.txn.gl;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import javax.naming.NamingException;

import com.ejb.entities.ad.LocalAdUser;
import com.ejb.dao.ad.LocalAdUserHome;
import com.ejb.entities.gl.LocalGlStaticReport;
import com.ejb.dao.gl.LocalGlStaticReportHome;
import com.ejb.entities.gl.LocalGlUserStaticReport;
import com.ejb.dao.gl.LocalGlUserStaticReportHome;
import com.util.EJBContextClass;
import com.util.Debug;
import com.util.EJBHomeFactory;
import com.util.mod.gl.GlModUserStaticReportDetails;
import com.util.gl.GlStaticReportDetails;

@Stateless(name = "GlUserStaticReportControllerEJB")
public class GlUserStaticReportControllerBean extends EJBContextClass implements GlUserStaticReportController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalGlStaticReportHome glStaticReportHome;
    @EJB
    private LocalGlUserStaticReportHome glUserStaticReportHome;

    public GlModUserStaticReportDetails getGlUstrBySrCodeAndUsrCode(Integer SR_CODE, Integer USR_CODE, Integer AD_CMPNY) {

        Debug.print("GlUserStaticReportControllerBean getGlUstrBySrCodeAndUsrCode");

        try {

            LocalGlUserStaticReport glUserStaticReport = glUserStaticReportHome.findBySrCodeAndUsrCode(SR_CODE, USR_CODE, AD_CMPNY);

            GlModUserStaticReportDetails mdetails = new GlModUserStaticReportDetails();
            mdetails.setUstrCode(glUserStaticReport.getUstrCode());
            mdetails.setUstrUsrCode(glUserStaticReport.getAdUser().getUsrCode());
            mdetails.setUstrUsrName(glUserStaticReport.getAdUser().getUsrName());

            return mdetails;

        } catch (FinderException ex) {

            return null;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

    }


    public GlStaticReportDetails getGlSrBySrCode(Integer SR_CODE) {

        Debug.print("GlUserStaticReportControllerBean getAdRsByRsCode");

        try {

            LocalGlStaticReport glStaticReport = glStaticReportHome.findByPrimaryKey(SR_CODE);
            GlStaticReportDetails details = new GlStaticReportDetails();
            details.setSrCode(glStaticReport.getSrCode());
            details.setSrName(glStaticReport.getSrName());
            details.setSrDescription(glStaticReport.getSrDescription());

            return details;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

    }


    public void saveGlUstrEntry(ArrayList list, Integer SR_CODE, Integer AD_CMPNY) {

        Debug.print("GlUserStaticReportControllerBean saveGlUstrEntry");

        try {

            LocalGlUserStaticReport glUserStaticReport = null;

            // delete all existing user static report

            Collection users = glUserStaticReportHome.findBySrCode(SR_CODE, AD_CMPNY);

            Iterator i = users.iterator();

            while (i.hasNext()) {

                glUserStaticReport = (LocalGlUserStaticReport) i.next();
                i.remove();

                em.remove(glUserStaticReport);

            }

            // create new user static report

            i = list.iterator();

            while (i.hasNext()) {

                GlModUserStaticReportDetails mdetails = (GlModUserStaticReportDetails) i.next();

                glUserStaticReport = glUserStaticReportHome.create(AD_CMPNY);

                // add user

                LocalAdUser adUser = adUserHome.findByPrimaryKey(mdetails.getUstrUsrCode());
                adUser.addGlUserStaticReport(glUserStaticReport);

                // add static report

                LocalGlStaticReport glStaticReport = glStaticReportHome.findByPrimaryKey(SR_CODE);
                glStaticReport.addGlUserStaticReport(glUserStaticReport);

            }

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }


    public ArrayList getAdUsrAll(Integer AD_CMPNY) {

        Debug.print("GlUserStaticReportControllerBean getAdUsrAll");

        Collection adUsers = null;

        LocalAdUser adUser = null;

        ArrayList list = new ArrayList();

        try {

            adUsers = adUserHome.findUsrAll(AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (adUsers.isEmpty()) {

            return null;

        }

        for (Object user : adUsers) {

            adUser = (LocalAdUser) user;

            GlModUserStaticReportDetails details = new GlModUserStaticReportDetails();

            details.setUstrUsrCode(adUser.getUsrCode());
            details.setUstrUsrName(adUser.getUsrName());

            list.add(details);

        }

        return list;

    }


    public ArrayList getGlUstrBySrCode(Integer SR_CODE, Integer AD_CMPNY) {

        Debug.print("GlUserStaticReportControllerBean getGlUstrBySrCode");

        try {

            Collection glUserStaticReports = glUserStaticReportHome.findBySrCode(SR_CODE, AD_CMPNY);
            Iterator i = glUserStaticReports.iterator();
            ArrayList list = new ArrayList();

            while (i.hasNext()) {

                LocalGlUserStaticReport glUserStaticReport = (LocalGlUserStaticReport) i.next();

                GlModUserStaticReportDetails mdetails = new GlModUserStaticReportDetails();

                mdetails.setUstrCode(glUserStaticReport.getUstrCode());
                mdetails.setUstrUsrCode(glUserStaticReport.getAdUser().getUsrCode());
                mdetails.setUstrUsrName(glUserStaticReport.getAdUser().getUsrName());

                list.add(mdetails);

            }

            return list;

        } catch (FinderException ex) {

            return null;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

    }

    // SessionBean methods


    public void ejbCreate() throws CreateException {

        Debug.print("GlUserStaticReportControllerBean ejbCreate");

    }
}