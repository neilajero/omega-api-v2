package com.ejb.txnreports.gl;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;


import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.gl.LocalGlUserStaticReport;
import com.ejb.dao.gl.LocalGlUserStaticReportHome;
import com.util.EJBContextClass;
import com.util.Debug;
import com.util.EJBHomeFactory;
import com.util.mod.gl.GlModUserStaticReportDetails;

@Stateless(name = "GlRepStaticReportControllerEJB")
public class GlRepStaticReportControllerBean extends EJBContextClass implements GlRepStaticReportController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalGlUserStaticReportHome glUserStaticReportHome;


    public GlModUserStaticReportDetails getGlUstrBySrCodeAndUsrCode(Integer SR_CODE, Integer USR_CODE, Integer AD_CMPNY) {

        Debug.print("GlRepStaticReportControllerBean getGlUstrBySrCodeAndFfCode");

        LocalGlUserStaticReport glUserStaticReport = null;

        try {

            glUserStaticReport = glUserStaticReportHome.findBySrCodeAndUsrCode(SR_CODE, USR_CODE, AD_CMPNY);

            GlModUserStaticReportDetails mdetails = new GlModUserStaticReportDetails();
            mdetails.setUstrCode(glUserStaticReport.getUstrCode());
            mdetails.setUstrUsrName(glUserStaticReport.getAdUser().getUsrName());

            return mdetails;

        } catch (FinderException ex) {

            return null;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

    }


    public ArrayList getGlUstrByUsrCode(Integer USR_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlRepStaticReportControllerBean getGlUstrByUsrCode");

        try {

            Collection glUserStaticReports = null;

            try {

                glUserStaticReports = glUserStaticReportHome.findByUsrCode(USR_CODE, AD_CMPNY);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();

            }

            if (glUserStaticReports.isEmpty()) {

                throw new GlobalNoRecordFoundException();

            }

            Iterator i = glUserStaticReports.iterator();

            ArrayList list = new ArrayList();

            while (i.hasNext()) {

                LocalGlUserStaticReport glUserStaticReport = (LocalGlUserStaticReport) i.next();

                GlModUserStaticReportDetails mdetails = new GlModUserStaticReportDetails();

                Debug.print("USTR_CODE=" + glUserStaticReport.getUstrCode());
                mdetails.setUstrCode(glUserStaticReport.getUstrCode());
                mdetails.setUstrSrFlName(glUserStaticReport.getGlStaticReport().getSrFileName());
                mdetails.setUstrSrDateTo(glUserStaticReport.getGlStaticReport().getSrDateTo());
                mdetails.setUstrSrName(glUserStaticReport.getGlStaticReport().getSrName());
                mdetails.setUstrSrCode(glUserStaticReport.getGlStaticReport().getSrCode());

                list.add(mdetails);

            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }

    }

    // SessionBean methods


    public void ejbCreate() throws CreateException {

        Debug.print("GlRepStaticReportControllerBean ejbCreate");

    }
}