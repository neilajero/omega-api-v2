package com.ejb.txnreports.gl;

import java.util.Date;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

import com.ejb.PersistenceBeanClass;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBContextClass;

@Stateless(name = "GlRepDetailIncomeStatementControllerEJB")
public class GlRepDetailIncomeStatementControllerBean extends EJBContextClass implements GlRepDetailIncomeStatementController {

    @EJB
    public PersistenceBeanClass em;

    public void executeSpGlRepDetailIncomeStatement(String STORED_PROCEDURE, String GL_ACCNT_NMBR_FRM, String GL_ACCNT_NMBR_TO, Date DT_FRM, Date DT_TO, boolean INCLUDE_UNPOSTED, boolean INCLUDE_UNPOSTED_SL, boolean SHOW_ZEROES, String AMOUNT_TYP, String BRANCH_CODES, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlRepDetailIncomeStatementControllerBean executeSpGlRepDetailIncomeStatement");

        try {
            StoredProcedureQuery spQuery = em.createStoredProcedureQuery(STORED_PROCEDURE).registerStoredProcedureParameter("accountFrom", String.class, ParameterMode.IN).registerStoredProcedureParameter("accountTo", String.class, ParameterMode.IN).registerStoredProcedureParameter("dateFrom", Date.class, ParameterMode.IN).registerStoredProcedureParameter("dateTo", Date.class, ParameterMode.IN).registerStoredProcedureParameter("includeUnposted", Boolean.class, ParameterMode.IN).registerStoredProcedureParameter("includeUnpostedSl", Boolean.class, ParameterMode.IN).registerStoredProcedureParameter("showZeroes", Boolean.class, ParameterMode.IN).registerStoredProcedureParameter("amountType", String.class, ParameterMode.IN).registerStoredProcedureParameter("branchCode", String.class, ParameterMode.IN).registerStoredProcedureParameter("adCompany", Integer.class, ParameterMode.IN).registerStoredProcedureParameter("resultCount", Integer.class, ParameterMode.OUT);
            spQuery.setParameter("accountFrom", GL_ACCNT_NMBR_FRM);
            spQuery.setParameter("accountTo", GL_ACCNT_NMBR_TO);
            spQuery.setParameter("dateFrom", DT_FRM);
            spQuery.setParameter("dateTo", DT_TO);
            spQuery.setParameter("includeUnposted", INCLUDE_UNPOSTED);
            spQuery.setParameter("includeUnpostedSl", INCLUDE_UNPOSTED_SL);
            spQuery.setParameter("showZeroes", SHOW_ZEROES);
            spQuery.setParameter("amountType", AMOUNT_TYP);
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

    // SessionBean methods
    public void ejbCreate() throws CreateException {
        Debug.print("GlRepDetailIncomeStatementControllerBean ejbCreate");
    }
}