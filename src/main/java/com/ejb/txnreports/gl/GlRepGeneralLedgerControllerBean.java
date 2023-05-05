package com.ejb.txnreports.gl;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;
import com.util.Debug;
import com.util.EJBContextClass;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import java.util.Date;

@Stateless(name = "GlRepGeneralLedgerControllerEJB")
public class GlRepGeneralLedgerControllerBean extends EJBContextClass implements GlRepGeneralLedgerController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;

    @Override
    public void executeSpGlRepGeneralLedger(String STORED_PROCEDURE, String GL_ACCNT_NMBR_FRM, String GL_ACCNT_NMBR_TO, Date DT_FRM, Date DR_TO, String ACCOUNT_TYPE, boolean INCLUDE_UNPOSTED, String BRANCH_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlRepGeneralLedgerControllerBean executeSpGlRepGeneralLedger");

        try {

            StoredProcedureQuery spQuery = em.createStoredProcedureQuery(STORED_PROCEDURE).registerStoredProcedureParameter("accountFrom", String.class, ParameterMode.IN).registerStoredProcedureParameter("accountTo", String.class, ParameterMode.IN).registerStoredProcedureParameter("dateFrom", Date.class, ParameterMode.IN).registerStoredProcedureParameter("dateTo", Date.class, ParameterMode.IN).registerStoredProcedureParameter("branchCode", String.class, ParameterMode.IN).registerStoredProcedureParameter("accountType", String.class, ParameterMode.IN).registerStoredProcedureParameter("includeUnposted", Boolean.class, ParameterMode.IN).registerStoredProcedureParameter("includeUnpostedSl", Boolean.class, ParameterMode.IN).registerStoredProcedureParameter("adCompany", Integer.class, ParameterMode.IN).registerStoredProcedureParameter("resultCount", Integer.class, ParameterMode.OUT);

            spQuery.setParameter("accountFrom", GL_ACCNT_NMBR_FRM);
            spQuery.setParameter("accountTo", GL_ACCNT_NMBR_TO);
            spQuery.setParameter("dateFrom", DT_FRM);
            spQuery.setParameter("dateTo", DR_TO);
            spQuery.setParameter("accountType", ACCOUNT_TYPE);
            spQuery.setParameter("includeUnposted", INCLUDE_UNPOSTED);
            spQuery.setParameter("includeUnpostedSl", INCLUDE_UNPOSTED);
            spQuery.setParameter("branchCode", BRANCH_CODE);
            spQuery.setParameter("adCompany", AD_CMPNY);

            spQuery.execute();

            Integer resultCount = (Integer) spQuery.getOutputParameterValue("resultCount");

            // IF NO RESULT FOUND
            if (resultCount <= 0) {
                throw new GlobalNoRecordFoundException();
            }
        } catch (GlobalNoRecordFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer companyCode) {
        Debug.print("GlRepGeneralLedgerControllerBean getAdCompany");
        try {
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());
            details.setCmpTaxPayerName(adCompany.getCmpTaxPayerName());
            details.setCmpContact(adCompany.getCmpContact());
            details.setCmpPhone(adCompany.getCmpPhone());
            details.setCmpEmail(adCompany.getCmpEmail());
            details.setCmpTin(adCompany.getCmpTin());

            details.setCmpMailSectionNo(adCompany.getCmpMailSectionNo());
            details.setCmpMailLotNo(adCompany.getCmpMailLotNo());
            details.setCmpMailStreet(adCompany.getCmpMailStreet());
            details.setCmpMailPoBox(adCompany.getCmpMailPoBox());
            details.setCmpMailCountry(adCompany.getCmpMailCountry());
            details.setCmpMailProvince(adCompany.getCmpMailProvince());
            details.setCmpMailPostOffice(adCompany.getCmpMailPostOffice());
            details.setCmpMailCareOff(adCompany.getCmpMailCareOff());
            details.setCmpTaxPeriodFrom(adCompany.getCmpTaxPeriodFrom());
            details.setCmpTaxPeriodTo(adCompany.getCmpTaxPeriodTo());
            details.setCmpPublicOfficeName(adCompany.getCmpPublicOfficeName());
            details.setCmpDateAppointment(adCompany.getCmpDateAppointment());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {
        Debug.print("GlRepGeneralLedgerControllerBean ejbCreate");
    }
}