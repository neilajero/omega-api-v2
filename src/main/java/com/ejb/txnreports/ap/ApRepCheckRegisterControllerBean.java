package com.ejb.txnreports.ap;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdBankAccountHome;
import com.ejb.dao.ap.LocalApSupplierClassHome;
import com.ejb.dao.ap.LocalApSupplierTypeHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.ad.LocalAdBankAccount;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ap.LocalApSupplierClass;
import com.ejb.entities.ap.LocalApSupplierType;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.gl.GlChartOfAccountDetails;

import jakarta.ejb.*;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Stateless(name = "ApRepCheckRegisterControllerEJB")

public class ApRepCheckRegisterControllerBean extends EJBContextClass implements ApRepCheckRegisterController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalApSupplierClassHome apSupplierClassHome;
    @EJB
    private LocalApSupplierTypeHome apSupplierTypeHome;

    public void executeSpApRepCheckRegister(String STORED_PROCEDURE, String SUPPLIER_CODE, Date DT_FRM, Date DT_TO, boolean INCLUDE_UNPOSTED, boolean DIRECT_CHECK_ONLY, String BRANCH_CODES, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepCheckRegisterControllerBean executeSpApRepCheckRegister");

        try {
            StoredProcedureQuery spQuery = em.createStoredProcedureQuery(STORED_PROCEDURE)
                    .registerStoredProcedureParameter("supplierCode", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("dateFrom", Date.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("dateTo", Date.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("includeUnposted", Boolean.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("directCheckOnly", Boolean.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("branchCode", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("adCompany", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("resultCount", Integer.class, ParameterMode.OUT);

            spQuery.setParameter("supplierCode", SUPPLIER_CODE);
            spQuery.setParameter("dateFrom", DT_FRM);
            spQuery.setParameter("dateTo", DT_TO);
            spQuery.setParameter("includeUnposted", INCLUDE_UNPOSTED);
            spQuery.setParameter("directCheckOnly", DIRECT_CHECK_ONLY);
            spQuery.setParameter("branchCode", BRANCH_CODES);
            spQuery.setParameter("adCompany", AD_CMPNY);

            spQuery.execute();

            Integer resultCount = (Integer) spQuery.getOutputParameterValue("resultCount");

            if (resultCount <= 0) {
                throw new GlobalNoRecordFoundException();
            }

        } catch (GlobalNoRecordFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

    }

    public ArrayList getApScAll(Integer AD_CMPNY) {

        Debug.print("ApRepCheckRegisterControllerBean getApScAll");

        ArrayList list = new ArrayList();
        try {
            Collection apSupplierClasses = apSupplierClassHome.findEnabledScAll(AD_CMPNY);
            for (Object supplierClass : apSupplierClasses) {
                LocalApSupplierClass apSupplierClass = (LocalApSupplierClass) supplierClass;
                list.add(apSupplierClass.getScName());
            }
            return list;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApStAll(Integer AD_CMPNY) {

        Debug.print("ApRepCheckRegisterControllerBean getApStAll");

        ArrayList list = new ArrayList();
        try {
            Collection apSupplierTypes = apSupplierTypeHome.findEnabledStAll(AD_CMPNY);
            for (Object supplierType : apSupplierTypes) {
                LocalApSupplierType apSupplierType = (LocalApSupplierType) supplierType;
                list.add(apSupplierType.getStName());
            }
            return list;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdBaAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ApRepCheckRegisterControllerBean getAdBaAll");

        ArrayList list = new ArrayList();
        try {
            Collection adBankAccounts = adBankAccountHome.findEnabledBaAll(AD_BRNCH, AD_CMPNY);
            for (Object bankAccount : adBankAccounts) {
                LocalAdBankAccount adBankAccount = (LocalAdBankAccount) bankAccount;
                list.add(adBankAccount.getBaName());
            }
            return list;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("ApRepCheckRegisterControllerBean getAdCompany");

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

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getEnableGlCoa(Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepCheckRegisterControllerBean getEnableGlCoa");

        Collection glChartOfAccounts = null;

        ArrayList list = new ArrayList();

        try {

            glChartOfAccounts = glChartOfAccountHome.findCoaAllEnabled(new java.util.Date(), AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (glChartOfAccounts.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }

        try {

            for (Object chartOfAccount : glChartOfAccounts) {

                LocalGlChartOfAccount glChartOfAccount = (LocalGlChartOfAccount) chartOfAccount;

                GlChartOfAccountDetails details = new GlChartOfAccountDetails();

                String coaName = glChartOfAccount.getCoaAccountDescription();

                list.add(coaName);
            }

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        return list;
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ApRepCheckRegisterControllerBean ejbCreate");
    }

}