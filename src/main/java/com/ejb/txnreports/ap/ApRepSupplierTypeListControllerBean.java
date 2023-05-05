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
import com.ejb.entities.ap.LocalApSupplierType;
import com.ejb.dao.ap.LocalApSupplierTypeHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ap.ApRepSupplierTypeListDetails;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ApRepSupplierTypeListControllerEJB")
public class ApRepSupplierTypeListControllerBean extends EJBContextClass implements ApRepSupplierTypeListController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalApSupplierTypeHome apSupplierTypeHome;

    public ArrayList executeApRepSupplierTypeList(HashMap criteria, String ORDER_BY, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepSupplierTypeListControllerBean executeApRepSupplierTypeList");

        ArrayList list = new ArrayList();

        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(st) FROM ApSupplierType st ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("supplierTypeName")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("supplierTypeName")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("st.stName LIKE '%").append(criteria.get("supplierTypeName")).append("%' ");
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("st.stAdCompany=").append(AD_CMPNY).append(" ");

            String orderBy = null;

            if (ORDER_BY.equals("SUPPLIER TYPE NAME")) {

                orderBy = "st.stName";
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy);
            }

            Collection apSupplierTypes = apSupplierTypeHome.getStByCriteria(jbossQl.toString(), obj);

            if (apSupplierTypes.size() == 0) throw new GlobalNoRecordFoundException();

            for (Object supplierType : apSupplierTypes) {

                LocalApSupplierType apSupplierType = (LocalApSupplierType) supplierType;

                ApRepSupplierTypeListDetails details = new ApRepSupplierTypeListDetails();
                details.setStlStName(apSupplierType.getStName());
                details.setStlStDescription(apSupplierType.getStDescription());
                details.setStlBankAccount(apSupplierType.getAdBankAccount().getBaName());
                details.setStlEnable(apSupplierType.getStEnable());

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

        Debug.print("ApRepSupplierTypeListControllerBean getAdCompany");

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

        Debug.print("ApRepSupplierTypeListControllerBean ejbCreate");
    }
}