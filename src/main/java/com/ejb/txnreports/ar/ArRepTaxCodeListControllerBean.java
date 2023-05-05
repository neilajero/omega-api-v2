/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArRepTaxCodeListControllerBean
 * @created March 02, 2005, 04:06 PM
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
import com.ejb.entities.ar.LocalArTaxCode;
import com.ejb.dao.ar.LocalArTaxCodeHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ar.ArRepTaxCodeListDetails;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ArRepTaxCodeListControllerEJB")
public class ArRepTaxCodeListControllerBean extends EJBContextClass implements ArRepTaxCodeListController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalArTaxCodeHome arTaxCodeHome;

    public ArrayList executeArRepTaxCodeList(HashMap criteria, String ORDER_BY, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepTaxCodeListControllerBean executeArRepTaxCodeList");

        ArrayList list = new ArrayList();

        try {

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT DISTINCT OBJECT(tc) FROM ArTaxCode tc ");

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

            Collection arTaxCodes = arTaxCodeHome.getTcByCriteria(jbossQl.toString(), obj);

            if (arTaxCodes.size() == 0) throw new GlobalNoRecordFoundException();

            for (Object taxCode : arTaxCodes) {

                LocalArTaxCode arTaxCode = (LocalArTaxCode) taxCode;

                ArRepTaxCodeListDetails details = new ArRepTaxCodeListDetails();
                details.setTclTaxName(arTaxCode.getTcName());
                details.setTclTaxDescription(arTaxCode.getTcDescription());
                details.setTclTaxType(arTaxCode.getTcType());
                details.setTclTaxRate(arTaxCode.getTcRate());

                if (arTaxCode.getGlChartOfAccount() != null) {

                    details.setTclCoaGlTaxAccountNumber(arTaxCode.getGlChartOfAccount().getCoaAccountNumber());
                    details.setTclCoaGlTaxAccountDescription(arTaxCode.getGlChartOfAccount().getCoaAccountDescription());
                }

                if (arTaxCode.getTcInterimAccount() != null) {

                    LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(arTaxCode.getTcInterimAccount());

                    details.setTclCoaGlInterimAccountNumber(glChartOfAccount.getCoaAccountNumber());
                    details.setTclCoaGlInterimAccountDescription(glChartOfAccount.getCoaAccountDescription());
                }

                details.setTclEnable(arTaxCode.getTcEnable());

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

        Debug.print("ArRepTaxCodeListControllerBean getAdCompany");

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

        Debug.print("ArRepTaxCodeListControllerBean ejbCreate");
    }
}