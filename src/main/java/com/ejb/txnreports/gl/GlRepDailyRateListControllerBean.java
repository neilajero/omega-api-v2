package com.ejb.txnreports.gl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;


import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.exception.gl.GlFCNoFunctionalCurrencyFoundException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
import com.ejb.entities.gl.LocalGlFunctionalCurrencyRate;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.util.ad.AdCompanyDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.reports.gl.GlRepDailyRateListDetails;

@Stateless(name = "GlRepDailyRateListControllerEJB")
public class GlRepDailyRateListControllerBean extends EJBContextClass implements GlRepDailyRateListController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("GlRepDailyRateListControllerBean getAdCompany");

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

    public ArrayList getGlFcAll(Integer AD_CMPNY) throws GlFCNoFunctionalCurrencyFoundException {

        Debug.print("GlRepDailyRateListControllerBean getGlFcAll");

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

            fcAllList.add(glFunctionalCurrency.getFcName());
        }

        return fcAllList;
    }

    public ArrayList executeDailyRateList(HashMap criteria, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(fr) FROM GlFunctionalCurrencyRate fr ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();
            Object[] obj;
            obj = new Object[criteriaSize];

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("fr.frDate >= ?").append(ctr + 1).append(" ");
                obj[ctr] = EJBCommon.convertStringToSQLDate((String) criteria.get("dateFrom"));
                ctr++;
            }

            if (criteria.containsKey("dateTo")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("fr.frDate <= ?").append(ctr + 1).append(" ");
                obj[ctr] = EJBCommon.convertStringToSQLDate((String) criteria.get("dateTo"));
                ctr++;
            }

            if (criteria.containsKey("currency")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("fr.glFunctionalCurrency.fcName = ?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("currency");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("fr.frAdCompany=").append(AD_CMPNY).append(" ORDER BY fr.glFunctionalCurrency.fcName");

            Collection glFunctionalCurrencyRates = glFunctionalCurrencyRateHome.getFrByCriteria(jbossQl.toString(), obj, 0, 0);
            Iterator i = glFunctionalCurrencyRates.iterator();
            ArrayList list = new ArrayList();

            while (i.hasNext()) {

                LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = (LocalGlFunctionalCurrencyRate) i.next();

                GlRepDailyRateListDetails mdetails = new GlRepDailyRateListDetails();

                mdetails.setDrFrConversionToUSD(glFunctionalCurrencyRate.getFrXToUsd());
                mdetails.setDrFrDate(glFunctionalCurrencyRate.getFrDate());
                mdetails.setDrFrFuncationalCurrrencyName(glFunctionalCurrencyRate.getGlFunctionalCurrency().getFcName());

                mdetails.setDrInverseRate(1 / glFunctionalCurrencyRate.getFrXToUsd());

                list.add(mdetails);
            }

            if (list.isEmpty()) throw new GlobalNoRecordFoundException();

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

        Debug.print("GlRepDailyRateListControllerBean ejbCreate");
    }
}