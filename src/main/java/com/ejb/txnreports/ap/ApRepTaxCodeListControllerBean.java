package com.ejb.txnreports.ap;

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
import com.ejb.entities.ap.LocalApTaxCode;
import com.ejb.dao.ap.LocalApTaxCodeHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ap.ApRepTaxCodeListDetails;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ApRepTaxCodeListControllerEJB")
public class ApRepTaxCodeListControllerBean extends EJBContextClass implements ApRepTaxCodeListController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalApTaxCodeHome apTaxCodeHome;

    public ArrayList executeApRepTaxCodeList(HashMap criteria, String ORDER_BY, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepTaxCodeListControllerBean executeApRepTaxCodeList");

        ArrayList list = new ArrayList();


        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(tc) FROM ApTaxCode tc ");

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

                jbossQl.append("tc.tcName LIKE '%").append(criteria.get("taxName")).append("%' ");
            }

            if (criteria.containsKey("type")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("tc.tcType=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("type");
                ctr++;
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("tc.tcAdCompany=").append(AD_CMPNY).append(" ");

            String orderBy = null;

            if (ORDER_BY.equals("TAX NAME")) {

                orderBy = "tc.tcName";

            } else if (ORDER_BY.equals("TAX TYPE")) {

                orderBy = "tc.tcType";
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy);
            }

            Collection apTaxCodes = apTaxCodeHome.getTcByCriteria(jbossQl.toString(), obj);

            if (apTaxCodes.size() == 0) throw new GlobalNoRecordFoundException();

            for (Object taxCode : apTaxCodes) {

                LocalApTaxCode apTaxCode = (LocalApTaxCode) taxCode;

                ApRepTaxCodeListDetails details = new ApRepTaxCodeListDetails();
                details.setTclTaxName(apTaxCode.getTcName());
                details.setTclTaxDescription(apTaxCode.getTcDescription());
                details.setTclTaxType(apTaxCode.getTcType());
                details.setTclTaxRate(apTaxCode.getTcRate());

                if (apTaxCode.getGlChartOfAccount() != null) {

                    details.setTclCoaGlTaxAccountNumber(apTaxCode.getGlChartOfAccount().getCoaAccountNumber());
                    details.setTclCoaGlTaxAccountDescription(apTaxCode.getGlChartOfAccount().getCoaAccountDescription());
                }

                details.setTclEnable(apTaxCode.getTcEnable());

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

        Debug.print("ApRepTaxCodeListControllerBean getAdCompany");

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

        Debug.print("ApRepTaxCodeListControllerBean ejbCreate");
    }
}