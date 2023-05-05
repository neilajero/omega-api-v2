/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class GlDailyRateControllerBean
 * @created
 * @author
 **/
package com.ejb.txn.gl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.RemoveException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.exception.gl.GlFCNoFunctionalCurrencyFoundException;
import com.ejb.exception.gl.GlFCRFunctionalCurrencyRateAlreadyDeletedException;
import com.ejb.exception.gl.GlFCRFunctionalCurrencyRateAlreadyExistException;
import com.ejb.exception.gl.GlFCRNoFunctionalCurrencyRateFoundException;
import com.ejb.exception.global.GlobalConversionDateNotExistException;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
import com.ejb.entities.gl.LocalGlFunctionalCurrencyRate;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.gl.GlFunctionalCurrencyDetails;
import com.util.gl.GlFunctionalCurrencyRateDetails;
import com.util.mod.gl.GlModFunctionalCurrencyRateDetails;

@Stateless(name = "GlDailyRateControllerEJB")
public class GlDailyRateControllerBean extends EJBContextClass implements GlDailyRateController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;

    public GlModFunctionalCurrencyRateDetails getFrRateByFrName(String FC_NM, Integer AD_CMPNY) throws GlobalConversionDateNotExistException {

        Debug.print("GlDailyRateControllerBean getFrRateByFrName");

        try {

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);

            // Get functional currency rate

            GlModFunctionalCurrencyRateDetails mdetails = new GlModFunctionalCurrencyRateDetails(null, 1d, null, FC_NM);

            try {

                Collection glFunctionalCurrencyRates = glFunctionalCurrencyRateHome.findPriorByFcCode(glFunctionalCurrency.getFcCode(), AD_CMPNY);

                ArrayList glFunctionalCurrencyRateList = new ArrayList(glFunctionalCurrencyRates);
                LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = (LocalGlFunctionalCurrencyRate) glFunctionalCurrencyRateList.get(0);

                mdetails = new GlModFunctionalCurrencyRateDetails(glFunctionalCurrencyRate.getFrCode(), glFunctionalCurrencyRate.getFrXToUsd(), glFunctionalCurrencyRate.getFrDate(), glFunctionalCurrencyRate.getGlFunctionalCurrency().getFcName());

            } catch (Exception e) {
                // TODO: handle exception
            }


            return mdetails;

        } catch (FinderException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlobalConversionDateNotExistException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }


    public double getFrRateByFrNameAndFrDate(String FC_NM, Date CONVERSION_DATE, Integer AD_CMPNY) throws GlobalConversionDateNotExistException {

        Debug.print("GlDailyRateControllerBean getFrRateByFrNameAndFrDate");

        try {

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);

            double CONVERSION_RATE = 1;

            LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glFunctionalCurrency.getFcCode(), CONVERSION_DATE, AD_CMPNY);

            CONVERSION_RATE = glFunctionalCurrencyRate.getFrXToUsd();

            return CONVERSION_RATE;

        } catch (FinderException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlobalConversionDateNotExistException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }


    public ArrayList getGlFcAll(Integer AD_CMPNY) throws GlFCNoFunctionalCurrencyFoundException {

        Debug.print("GlDailyRateControllerBean getGlFcAll");

        ArrayList fcAllList = new ArrayList();
        Collection glFunctionalCurrencies = null;

        try {
            glFunctionalCurrencies = glFunctionalCurrencyHome.findFcAllEnabled(new Date(EJBCommon.getGcCurrentDateWoTime().getTime().getTime()), AD_CMPNY);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (glFunctionalCurrencies.size() == 0) throw new GlFCNoFunctionalCurrencyFoundException();

        for (Object functionalCurrency : glFunctionalCurrencies) {
            LocalGlFunctionalCurrency glFunctionalCurrency = (LocalGlFunctionalCurrency) functionalCurrency;
            GlFunctionalCurrencyDetails details = new GlFunctionalCurrencyDetails(glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), glFunctionalCurrency.getFcDescription(), glFunctionalCurrency.getFcCountry(), glFunctionalCurrency.getFcSymbol(), glFunctionalCurrency.getFcPrecision(), glFunctionalCurrency.getFcExtendedPrecision(), glFunctionalCurrency.getFcMinimumAccountUnit(), glFunctionalCurrency.getFcDateFrom(), glFunctionalCurrency.getFcDateTo(), glFunctionalCurrency.getFcEnable());
            fcAllList.add(details);
        }

        return fcAllList;
    }


    public GlModFunctionalCurrencyRateDetails getGlFcrByFrCode(Integer FR_CODE, Integer AD_CMPNY) throws GlFCRNoFunctionalCurrencyRateFoundException {

        Debug.print("GlDailyRateControllerBean getGlFcrByFrCode");

        LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = null;

        try {

            glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByPrimaryKey(FR_CODE);

        } catch (FinderException ex) {

            throw new GlFCRNoFunctionalCurrencyRateFoundException();

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }


        return new GlModFunctionalCurrencyRateDetails(glFunctionalCurrencyRate.getFrCode(), glFunctionalCurrencyRate.getFrXToUsd(), glFunctionalCurrencyRate.getFrDate(), glFunctionalCurrencyRate.getGlFunctionalCurrency().getFcName());

    }


    public void addGlFcrEntry(GlFunctionalCurrencyRateDetails details, String FC_NM, Integer AD_CMPNY) throws GlFCNoFunctionalCurrencyFoundException, GlFCRFunctionalCurrencyRateAlreadyExistException {

        Debug.print("GlDailyRateControllerBean addGlFcrEntry");

        LocalGlFunctionalCurrency glFunctionalCurrency = null;
        LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = null;

        try {
            glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);
        } catch (FinderException ex) {
            throw new GlFCNoFunctionalCurrencyFoundException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        try {

            glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glFunctionalCurrency.getFcCode(), details.getFrDate(), AD_CMPNY);
            throw new GlFCRFunctionalCurrencyRateAlreadyExistException();

        } catch (FinderException ex) {

        } catch (GlFCRFunctionalCurrencyRateAlreadyExistException ex) {

            throw ex;

        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }


        try {
            Debug.print("Rates: " + details.getFrXToUsd());
            glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.create(details.getFrXToUsd(), details.getFrDate(), AD_CMPNY);
            Debug.print("Date: " + glFunctionalCurrencyRate.getFrXToUsd());


        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

        try {
            glFunctionalCurrency.addGlFunctionalCurrencyRate(glFunctionalCurrencyRate);
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

    }


    public void updateGlFcrEntry(GlFunctionalCurrencyRateDetails details, String FC_NM, Integer AD_CMPNY) throws GlFCNoFunctionalCurrencyFoundException, GlFCRFunctionalCurrencyRateAlreadyExistException, GlFCRFunctionalCurrencyRateAlreadyDeletedException {

        Debug.print("GlFunctionalCurrencyRateBean updateGlFcrEntry");

        LocalGlFunctionalCurrency glFunctionalCurrency = null;
        LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = null;

        try {
            glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);
        } catch (FinderException ex) {
            throw new GlFCNoFunctionalCurrencyFoundException();
        }

        try {
            glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByPrimaryKey(details.getFrCode());
        } catch (FinderException ex) {
            throw new GlFCRFunctionalCurrencyRateAlreadyDeletedException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        try {

            LocalGlFunctionalCurrencyRate glExistingFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glFunctionalCurrency.getFcCode(), details.getFrDate(), AD_CMPNY);

            if (!glExistingFunctionalCurrencyRate.getFrCode().equals(glFunctionalCurrencyRate.getFrCode())) {

                throw new GlFCRFunctionalCurrencyRateAlreadyExistException();

            }

        } catch (FinderException ex) {

        } catch (GlFCRFunctionalCurrencyRateAlreadyExistException ex) {

            throw ex;

        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        try {

            glFunctionalCurrencyRate.setFrXToUsd(details.getFrXToUsd());
            glFunctionalCurrencyRate.setFrDate(details.getFrDate());
            glFunctionalCurrency.addGlFunctionalCurrencyRate(glFunctionalCurrencyRate);


        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

    }


    public void deleteGlFcrEntry(Integer FR_CODE, Integer AD_CMPNY) throws GlFCRFunctionalCurrencyRateAlreadyDeletedException {

        Debug.print("GlFunctionalCurrencyRateBean deleteGlFcrEntry");

        LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = null;

        try {
            glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByPrimaryKey(FR_CODE);
        } catch (FinderException ex) {
            throw new GlFCRFunctionalCurrencyRateAlreadyDeletedException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        try {
            em.remove(glFunctionalCurrencyRate);
        } catch (RemoveException ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

    }

    // SessionBean methods


    public void ejbCreate() throws CreateException {

        Debug.print("GlFunctionalCurrencyRateControllerBean ejbCreate");

    }

    // private methods


}