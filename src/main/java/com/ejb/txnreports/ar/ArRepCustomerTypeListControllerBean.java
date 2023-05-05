/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArRepCustomerTypeListControllerBean
 * @created March 03, 2005, 04:44 PM
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
import com.ejb.entities.ar.LocalArCustomerType;
import com.ejb.dao.ar.LocalArCustomerTypeHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ar.ArRepCustomerTypeListDetails;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ArRepCustomerTypeListControllerEJB")
public class ArRepCustomerTypeListControllerBean extends EJBContextClass implements ArRepCustomerTypeListController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalArCustomerTypeHome arCustomerTypeHome;

    public ArrayList executeArRepCustomerTypeList(HashMap criteria, String ORDER_BY, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepCustomerTypeListControllerBean executeArRepCustomerTypeList");

        ArrayList list = new ArrayList();

        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(ct) FROM ArCustomerType ct ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("customerTypeName")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("customerTypeName")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("ct.ctName LIKE '%").append(criteria.get("customerTypeName")).append("%' ");
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("ct.ctAdCompany=").append(AD_CMPNY).append(" ");

            String orderBy = null;

            if (ORDER_BY.equals("CUSTOMER TYPE NAME")) {

                orderBy = "ct.ctName";
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy);
            }

            Collection arCustomerTypes = arCustomerTypeHome.getCtByCriteria(jbossQl.toString(), obj);

            if (arCustomerTypes.size() == 0) throw new GlobalNoRecordFoundException();

            for (Object customerType : arCustomerTypes) {

                LocalArCustomerType arCustomerType = (LocalArCustomerType) customerType;

                ArRepCustomerTypeListDetails details = new ArRepCustomerTypeListDetails();
                details.setCtlCtName(arCustomerType.getCtName());
                details.setCtlCtDescription(arCustomerType.getCtDescription());
                details.setCtlBankAccount(arCustomerType.getAdBankAccount().getBaName());
                details.setCtlEnable(arCustomerType.getCtEnable());

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

        Debug.print("ArRepCustomerTypeListControllerBean getAdCompany");

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

        Debug.print("ArRepCustomerTypeListControllerBean ejbCreate");
    }
}