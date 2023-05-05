/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArRepWithholdingTaxCodeListControllerBean
 * @created March 03, 2005, 02:53 PM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txnreports.ar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;


import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ar.LocalArWithholdingTaxCode;
import com.ejb.dao.ar.LocalArWithholdingTaxCodeHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ar.ArRepWithholdingTaxCodeListDetails;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ArRepWithholdingTaxCodeListControllerEJB")
public class ArRepWithholdingTaxCodeListControllerBean extends EJBContextClass implements ArRepWithholdingTaxCodeListController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalArWithholdingTaxCodeHome arWithholdingTaxCodeHome;

    public ArrayList executeArRepWithholdingTaxCodeList(HashMap criteria, String ORDER_BY, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepWithholdingTaxCodeListControllerBean executeArRepWithholdingTaxCodeList");

        ArrayList list = new ArrayList();

        try {

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT DISTINCT OBJECT(wtc) FROM ArWithholdingTaxCode wtc ");

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("taxName")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("taxName")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("wtc.wtcName LIKE '%").append(criteria.get("taxName")).append("%' ");
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("wtc.wtcAdCompany=").append(AD_CMPNY).append(" ");

            String orderBy = null;

            if (ORDER_BY.equals("TAX NAME")) {

                orderBy = "wtc.wtcName";
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy);
            }

            Collection arWithholdingTaxCodes = arWithholdingTaxCodeHome.getWtcByCriteria(jbossQl.toString(), obj);

            if (arWithholdingTaxCodes.size() == 0) throw new GlobalNoRecordFoundException();

            for (Object withholdingTaxCode : arWithholdingTaxCodes) {

                LocalArWithholdingTaxCode arWithholdingTaxCode = (LocalArWithholdingTaxCode) withholdingTaxCode;

                ArRepWithholdingTaxCodeListDetails details = new ArRepWithholdingTaxCodeListDetails();
                details.setWtlTaxName(arWithholdingTaxCode.getWtcName());
                details.setWtlTaxDescription(arWithholdingTaxCode.getWtcDescription());
                details.setWtlTaxRate(arWithholdingTaxCode.getWtcRate());

                if (arWithholdingTaxCode.getGlChartOfAccount() != null) {

                    details.setWtlCoaGlTaxAccountNumber(arWithholdingTaxCode.getGlChartOfAccount().getCoaAccountNumber());
                    details.setWtlCoaGlTaxAccountDescription(arWithholdingTaxCode.getGlChartOfAccount().getCoaAccountDescription());
                }

                details.setWtlEnable(arWithholdingTaxCode.getWtcEnable());

                list.add(details);
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("ArRepWithholdingTaxCodeListControllerBean getAdCompany");

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

        Debug.print("ArRepWithholdingTaxCodeListControllerBean ejbCreate");
    }
}