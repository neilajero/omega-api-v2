/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArRepCustomerClassListControllerBean
 * @created March 02, 2005, 10:07 AM
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
import com.ejb.entities.ar.LocalArCustomerClass;
import com.ejb.dao.ar.LocalArCustomerClassHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ar.ArRepCustomerClassListDetails;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ArRepCustomerClassListControllerEJB")
public class ArRepCustomerClassListControllerBean extends EJBContextClass implements ArRepCustomerClassListController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalArCustomerClassHome arCustomerClassHome;

    public ArrayList executeArRepCustomerClassList(HashMap criteria, String ORDER_BY, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepCustomerClassListControllerBean executeArRepCustomerClassList");

        ArrayList list = new ArrayList();

        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(cc) FROM ArCustomerClass cc ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("customerClassName")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("customerClassName")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cc.ccName LIKE '%").append(criteria.get("customerClassName")).append("%' ");
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("cc.ccAdCompany=").append(AD_CMPNY).append(" ");

            String orderBy = null;

            if (ORDER_BY.equals("CUSTOMER CLASS NAME")) {

                orderBy = "cc.ccName";
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy);
            }

            Collection arCustomerClasses = arCustomerClassHome.getCcByCriteria(jbossQl.toString(), obj);

            if (arCustomerClasses.size() == 0) throw new GlobalNoRecordFoundException();

            for (Object customerClass : arCustomerClasses) {

                LocalArCustomerClass arCustomerClass = (LocalArCustomerClass) customerClass;

                ArRepCustomerClassListDetails details = new ArRepCustomerClassListDetails();
                details.setCclCcName(arCustomerClass.getCcName());
                details.setCclCcDescription(arCustomerClass.getCcDescription());
                details.setCclTaxName(arCustomerClass.getArTaxCode().getTcName());
                details.setCclWithholdingTaxName(arCustomerClass.getArWithholdingTaxCode().getWtcName());

                LocalGlChartOfAccount glReceivableAccount = glChartOfAccountHome.findByPrimaryKey(arCustomerClass.getCcGlCoaReceivableAccount());
                LocalGlChartOfAccount glRevenueAccount = null;

                if (arCustomerClass.getCcGlCoaRevenueAccount() != null) {

                    glRevenueAccount = glChartOfAccountHome.findByPrimaryKey(arCustomerClass.getCcGlCoaRevenueAccount());
                }

                details.setCclReceivableAccountNumber(glReceivableAccount.getCoaAccountNumber());
                details.setCclRevenueAccountNumber(glRevenueAccount != null ? glRevenueAccount.getCoaAccountNumber() : null);
                details.setCclReceivableAccountDescription(glReceivableAccount.getCoaAccountDescription());

                if (glRevenueAccount != null) {

                    details.setCclRevenueAccountDescription(glRevenueAccount.getCoaAccountDescription());
                }

                details.setCclEnable(arCustomerClass.getCcEnable());

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

        Debug.print("ArRepCustomerClassListControllerBean getAdCompany");

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

        Debug.print("ArRepCustomerClassListControllerBean ejbCreate");
    }
}