/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArWithholdingTaxCodeControllerBean
 * @created March 5, 2004, 2:29 PM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ar.LocalArWithholdingTaxCodeHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.ar.LocalArWithholdingTaxCode;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.exception.global.*;
import com.util.ar.ArWithholdingTaxCodeDetails;
import com.util.mod.ar.ArModWithholdingTaxCodeDetails;
import com.util.Debug;
import com.util.EJBContextClass;

import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.Collection;

@Stateless(name = "ArWithholdingTaxCodeControllerEJB")
public class ArWithholdingTaxCodeControllerBean extends EJBContextClass implements ArWithholdingTaxCodeController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalArWithholdingTaxCodeHome arWithholdingTaxCodeHome;

    public ArrayList getArWtcAll(Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArWithholdingTaxCodeControllerBean getArWtcAll");
        ArrayList list = new ArrayList();
        try {

            Collection arWithholdingTaxCodes = arWithholdingTaxCodeHome.findWtcAll(AD_CMPNY);

            if (arWithholdingTaxCodes.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            for (Object withholdingTaxCode : arWithholdingTaxCodes) {

                LocalArWithholdingTaxCode arWithholdingTaxCode = (LocalArWithholdingTaxCode) withholdingTaxCode;

                ArModWithholdingTaxCodeDetails mdetails = new ArModWithholdingTaxCodeDetails();
                mdetails.setWtcCode(arWithholdingTaxCode.getWtcCode());
                mdetails.setWtcName(arWithholdingTaxCode.getWtcName());
                mdetails.setWtcDescription(arWithholdingTaxCode.getWtcDescription());
                mdetails.setWtcRate(arWithholdingTaxCode.getWtcRate());
                mdetails.setWtcEnable(arWithholdingTaxCode.getWtcEnable());

                if (arWithholdingTaxCode.getGlChartOfAccount() != null) {

                    mdetails.setWtcCoaGlWithholdingTaxAccountNumber(arWithholdingTaxCode.getGlChartOfAccount().getCoaAccountNumber());
                    mdetails.setWtcCoaGlWithholdingTaxAccountDescription(arWithholdingTaxCode.getGlChartOfAccount().getCoaAccountDescription());
                }

                list.add(mdetails);
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void addArWtcEntry(ArWithholdingTaxCodeDetails details, String WTC_COA_ACCNT_NMBR, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException {
        Debug.print("ArWithholdingTaxCodeControllerBean addArWtcEntry");
        try {

            LocalArWithholdingTaxCode arWithholdingTaxCode = null;
            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName(details.getWtcName(), AD_CMPNY);

                throw new GlobalRecordAlreadyExistException();

            } catch (FinderException ex) {

            }

            // get glChartOfAccount to validate accounts

            try {

                if (WTC_COA_ACCNT_NMBR != null && WTC_COA_ACCNT_NMBR.length() > 0) {

                    glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(WTC_COA_ACCNT_NMBR, AD_CMPNY);
                }

            } catch (FinderException ex) {

                throw new GlobalAccountNumberInvalidException();
            }

            // create new tax code

            arWithholdingTaxCode = arWithholdingTaxCodeHome.create(details.getWtcName(), details.getWtcDescription(), details.getWtcRate(), details.getWtcEnable(), AD_CMPNY);

            if (WTC_COA_ACCNT_NMBR != null && WTC_COA_ACCNT_NMBR.length() > 0) {

                glChartOfAccount.addArWithholdingTaxCode(arWithholdingTaxCode);
            }

        } catch (GlobalRecordAlreadyExistException | GlobalAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateArWtcEntry(ArWithholdingTaxCodeDetails details, String WTC_COA_ACCNT_NMBR, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException, GlobalRecordAlreadyAssignedException {
        Debug.print("ArWithholdingTaxCodeControllerBean updateArWtcEntry");
        try {

            LocalArWithholdingTaxCode arWithholdingTaxCode = null;
            LocalArWithholdingTaxCode apExistingTaxCode = null;
            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                apExistingTaxCode = arWithholdingTaxCodeHome.findByWtcName(details.getWtcName(), AD_CMPNY);

                if (!apExistingTaxCode.getWtcCode().equals(details.getWtcCode())) {

                    throw new GlobalRecordAlreadyExistException();
                }

            } catch (FinderException ex) {

            }

            // get glChartOfAccount to validate accounts

            try {

                if (WTC_COA_ACCNT_NMBR != null && WTC_COA_ACCNT_NMBR.length() > 0) {

                    glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(WTC_COA_ACCNT_NMBR, AD_CMPNY);
                }

            } catch (FinderException ex) {

                throw new GlobalAccountNumberInvalidException();
            }

            // find and update withholding tax code

            arWithholdingTaxCode = arWithholdingTaxCodeHome.findByPrimaryKey(details.getWtcCode());

            try {

                if ((!arWithholdingTaxCode.getArInvoices().isEmpty() || !arWithholdingTaxCode.getArReceipts().isEmpty() || !arWithholdingTaxCode.getAdPreferences().isEmpty()) && (details.getWtcRate() != arWithholdingTaxCode.getWtcRate())) {

                    throw new GlobalRecordAlreadyAssignedException();
                }

            } catch (GlobalRecordAlreadyAssignedException ex) {

                throw ex;
            }

            arWithholdingTaxCode.setWtcName(details.getWtcName());
            arWithholdingTaxCode.setWtcDescription(details.getWtcDescription());
            arWithholdingTaxCode.setWtcRate(details.getWtcRate());
            arWithholdingTaxCode.setWtcEnable(details.getWtcEnable());

            if (arWithholdingTaxCode.getGlChartOfAccount() != null) {

                arWithholdingTaxCode.getGlChartOfAccount().dropArWithholdingTaxCode(arWithholdingTaxCode);
            }

            if (WTC_COA_ACCNT_NMBR != null && WTC_COA_ACCNT_NMBR.length() > 0) {

                glChartOfAccount.addArWithholdingTaxCode(arWithholdingTaxCode);
            }

        } catch (GlobalRecordAlreadyExistException | GlobalRecordAlreadyAssignedException |
                 GlobalAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteArWtcEntry(Integer WTC_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException {
        Debug.print("ArWithholdingTaxCodeControllerBean deleteArWtcEntry");
        try {

            LocalArWithholdingTaxCode arWithholdingTaxCode = null;

            try {

                arWithholdingTaxCode = arWithholdingTaxCodeHome.findByPrimaryKey(WTC_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            if (!arWithholdingTaxCode.getArCustomerClasses().isEmpty() || !arWithholdingTaxCode.getArInvoices().isEmpty() || !arWithholdingTaxCode.getArReceipts().isEmpty() || !arWithholdingTaxCode.getAdPreferences().isEmpty() || !arWithholdingTaxCode.getArPdcs().isEmpty()) {

                throw new GlobalRecordAlreadyAssignedException();
            }

            //		    arWithholdingTaxCode.entityRemove();
            em.remove(arWithholdingTaxCode);

        } catch (GlobalRecordAlreadyDeletedException | GlobalRecordAlreadyAssignedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ArWithholdingTaxCodeControllerBean ejbCreate");
    }

}