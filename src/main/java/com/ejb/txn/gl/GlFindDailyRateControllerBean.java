/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class GlFindDailyRateControllerBean
 * @created
 * @author
 **/
package com.ejb.txn.gl;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;
import javax.naming.NamingException;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
import com.ejb.entities.gl.LocalGlFunctionalCurrencyRate;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.util.EJBContextClass;
import com.util.Debug;
import com.util.EJBHomeFactory;
import com.util.mod.gl.GlModFunctionalCurrencyRateDetails;

@Stateless(name = "GlFindDailyRateControllerEJB")
public class GlFindDailyRateControllerBean extends EJBContextClass implements GlFindDailyRateController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;

    public ArrayList getGlFrByCriteria(HashMap criteria, Integer OFFSET, Integer LIMIT, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlFindDailyRateControllerBean getGlFrByCriteria");

        ArrayList list = new ArrayList();

        StringBuilder jbossQl = new StringBuilder();
        jbossQl.append("SELECT OBJECT(fr) FROM GlFunctionalCurrencyRate fr ");

        boolean firstArgument = true;
        short ctr = 0;
        Object[] obj = new Object[criteria.size()];


        if (criteria.containsKey("frDate")) {

            firstArgument = false;

            jbossQl.append("WHERE fr.frDate=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("frDate");
            ctr++;

        }

        if (criteria.containsKey("fcName")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                jbossQl.append("WHERE ");
                firstArgument = false;
            }
            jbossQl.append("fr.glFunctionalCurrency.fcName=?").append(ctr + 1);
            obj[ctr] = criteria.get("fcName");
            ctr++;
        }

        if (!firstArgument) {

            jbossQl.append("AND ");

        } else {

            firstArgument = false;
            jbossQl.append("WHERE ");

        }

        jbossQl.append("fr.frAdCompany=").append(AD_CMPNY).append(" ");

        jbossQl.append(" ORDER BY fr.frDate");


        Collection glFunctionalCurrencyRates = null;

        try {

            glFunctionalCurrencyRates = glFunctionalCurrencyRateHome.getFrByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

        if (glFunctionalCurrencyRates.size() == 0) throw new GlobalNoRecordFoundException();

        for (Object functionalCurrencyRate : glFunctionalCurrencyRates) {

            LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = (LocalGlFunctionalCurrencyRate) functionalCurrencyRate;

            GlModFunctionalCurrencyRateDetails mdetails = new GlModFunctionalCurrencyRateDetails(glFunctionalCurrencyRate.getFrCode(), glFunctionalCurrencyRate.getFrXToUsd(), glFunctionalCurrencyRate.getFrDate(), glFunctionalCurrencyRate.getGlFunctionalCurrency().getFcName());


            list.add(mdetails);


        }

        return list;

    }


    public Integer getGlFrSizeByCriteria(HashMap criteria, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlFindDailyRateControllerBean getGlFrSizeByCriteria");

        StringBuilder jbossQl = new StringBuilder();
        jbossQl.append("SELECT OBJECT(fr) FROM GlFunctionalCurrencyRate fr ");

        boolean firstArgument = true;
        short ctr = 0;
        Object[] obj = new Object[criteria.size()];


        if (criteria.containsKey("frDate")) {

            firstArgument = false;

            jbossQl.append("WHERE fr.frDate=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("frDate");
            ctr++;

        }

        if (criteria.containsKey("fcName")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                jbossQl.append("WHERE ");
                firstArgument = false;
            }
            jbossQl.append("fr.glFunctionalCurrency.fcName=?").append(ctr + 1);
            obj[ctr] = criteria.get("fcName");
            ctr++;
        }

        if (!firstArgument) {

            jbossQl.append("AND ");

        } else {

            firstArgument = false;
            jbossQl.append("WHERE ");

        }

        jbossQl.append("fr.frAdCompany=").append(AD_CMPNY).append(" ");

        Collection glFunctionalCurrencyRates = null;

        try {

            glFunctionalCurrencyRates = glFunctionalCurrencyRateHome.getFrByCriteria(jbossQl.toString(), obj, 0, 0);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

        if (glFunctionalCurrencyRates.size() == 0) throw new GlobalNoRecordFoundException();

        return glFunctionalCurrencyRates.size();

    }


    public ArrayList getGlFcAll(Integer AD_CMPNY) {

        Debug.print("GlFindDailyRateControllerBean getGlFcAll");

        ArrayList list = new ArrayList();
        Collection glFunctionalCurrencies = null;

        try {

            glFunctionalCurrencies = glFunctionalCurrencyHome.findFcAll(AD_CMPNY);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

        for (Object functionalCurrency : glFunctionalCurrencies) {

            LocalGlFunctionalCurrency glFunctionalCurrency = (LocalGlFunctionalCurrency) functionalCurrency;

            list.add(glFunctionalCurrency.getFcName());

        }

        return list;
    }

    // SessionBean methods


    public void ejbCreate() throws CreateException {

        Debug.print("GlJournalBatchControllerBean ejbCreate");

    }

}