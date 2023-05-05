package com.ejb.txnreports.ap;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJBException;


import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ap.LocalApWithholdingTaxCode;
import com.ejb.dao.ap.LocalApWithholdingTaxCodeHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.EJBContextClass;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ap.ApRepWithholdingTaxCodeListDetails;
import com.util.Debug;
import com.util.EJBHomeFactory;

@Stateless(name = "ApRepWithholdingTaxCodeListControllerEJB")
public class ApRepWithholdingTaxCodeListControllerBean extends EJBContextClass implements ApRepWithholdingTaxCodeListController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalApWithholdingTaxCodeHome apWithholdingTaxCodeHome;

    public ArrayList executeApRepWithholdingTaxCodeList(HashMap criteria, String ORDER_BY, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepWithholdingTaxCodeListControllerBean executeApRepWithholdingTaxCodeList");

        ArrayList list = new ArrayList();

        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(wtc) FROM ApWithholdingTaxCode wtc ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

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

            Collection apWithholdingTaxCodes = apWithholdingTaxCodeHome.getWtcByCriteria(jbossQl.toString(), obj);

            if (apWithholdingTaxCodes.size() == 0) throw new GlobalNoRecordFoundException();

            for (Object withholdingTaxCode : apWithholdingTaxCodes) {

                LocalApWithholdingTaxCode apWithholdingTaxCode = (LocalApWithholdingTaxCode) withholdingTaxCode;

                ApRepWithholdingTaxCodeListDetails details = new ApRepWithholdingTaxCodeListDetails();
                details.setWtlTaxName(apWithholdingTaxCode.getWtcName());
                details.setWtlTaxDescription(apWithholdingTaxCode.getWtcDescription());
                details.setWtlTaxRate(apWithholdingTaxCode.getWtcRate());

                if (apWithholdingTaxCode.getGlChartOfAccount() != null) {

                    details.setWtlCoaGlTaxAccountNumber(apWithholdingTaxCode.getGlChartOfAccount().getCoaAccountNumber());
                    details.setWtlCoaGlTaxAccountDescription(apWithholdingTaxCode.getGlChartOfAccount().getCoaAccountDescription());
                }

                details.setWtlEnable(apWithholdingTaxCode.getWtcEnable());

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

        Debug.print("ApRepWithholdingTaxCodeListControllerBean getAdCompany");

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

        Debug.print("ApRepWithholdingTaxCodeListControllerBean ejbCreate");
    }
}