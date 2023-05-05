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
import com.ejb.entities.ap.LocalApSupplierClass;
import com.ejb.dao.ap.LocalApSupplierClassHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ap.ApRepSupplierClassListDetails;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ApRepSupplierClassListControllerEJB")
public class ApRepSupplierClassListControllerBean extends EJBContextClass implements ApRepSupplierClassListController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalApSupplierClassHome apSupplierClassHome;

    public ArrayList executeApRepSupplierClassList(HashMap criteria, String ORDER_BY, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepSupplierClassListControllerBean executeApRepSupplierClassList");

        ArrayList list = new ArrayList();

        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(sc) FROM ApSupplierClass sc ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("supplierClassName")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("supplierClassName")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("sc.scName LIKE '%").append(criteria.get("supplierClassName")).append("%' ");
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("sc.scAdCompany=").append(AD_CMPNY).append(" ");

            String orderBy = null;

            if (ORDER_BY.equals("SUPPLIER CLASS NAME")) {

                orderBy = "sc.scName";
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy);
            }

            Collection apSupplierClasses = apSupplierClassHome.getScByCriteria(jbossQl.toString(), obj);

            if (apSupplierClasses.size() == 0) throw new GlobalNoRecordFoundException();

            for (Object supplierClass : apSupplierClasses) {

                LocalApSupplierClass apSupplierClass = (LocalApSupplierClass) supplierClass;

                ApRepSupplierClassListDetails details = new ApRepSupplierClassListDetails();
                details.setSclScName(apSupplierClass.getScName());
                details.setSclScDescription(apSupplierClass.getScDescription());
                details.setSclTaxName(apSupplierClass.getApTaxCode().getTcName());
                details.setSclWithholdingTaxName(apSupplierClass.getApWithholdingTaxCode().getWtcName());

                LocalGlChartOfAccount glPayableAccount = glChartOfAccountHome.findByPrimaryKey(apSupplierClass.getScGlCoaPayableAccount());
                LocalGlChartOfAccount glExpenseAccount = null;

                if (apSupplierClass.getScGlCoaExpenseAccount() != null) {

                    glExpenseAccount = glChartOfAccountHome.findByPrimaryKey(apSupplierClass.getScGlCoaExpenseAccount());
                }

                details.setSclPayableAccountNumber(glPayableAccount.getCoaAccountNumber());
                details.setSclExpenseAccountNumber(glExpenseAccount != null ? glExpenseAccount.getCoaAccountNumber() : null);
                details.setSclPayableAccountDescription(glPayableAccount.getCoaAccountDescription());

                if (glExpenseAccount != null) {

                    details.setSclExpenseAccountDescription(glExpenseAccount.getCoaAccountDescription());
                }

                details.setSclEnable(apSupplierClass.getScEnable());

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

        Debug.print("ApRepSupplierClassListControllerBean getAdCompany");

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

        Debug.print("ApRepSupplierClassListControllerBean ejbCreate");
    }
}