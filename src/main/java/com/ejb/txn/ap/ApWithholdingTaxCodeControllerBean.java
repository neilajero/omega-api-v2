/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ApWithholdingTaxCodeControllerBean
 * @created
 * @author
 */
package com.ejb.txn.ap;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ap.LocalApWithholdingTaxCode;
import com.ejb.dao.ap.LocalApWithholdingTaxCodeHome;
import com.ejb.exception.global.GlobalAccountNumberInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.util.ap.ApWithholdingTaxCodeDetails;
import com.util.mod.ap.ApModWithholdingTaxCodeDetails;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ApWithholdingTaxCodeControllerEJB")
public class ApWithholdingTaxCodeControllerBean extends EJBContextClass implements ApWithholdingTaxCodeController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalApWithholdingTaxCodeHome apWithholdingTaxCodeHome;

    public ArrayList getApWtcAll(Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApWithholdingTaxCodeControllerBean getApWtcAll");

        ArrayList list = new ArrayList();

        try {

            Collection apWithholdingTaxCodes = apWithholdingTaxCodeHome.findWtcAll(AD_CMPNY);

            if (apWithholdingTaxCodes.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            for (Object withholdingTaxCode : apWithholdingTaxCodes) {

                LocalApWithholdingTaxCode apWithholdingTaxCode = (LocalApWithholdingTaxCode) withholdingTaxCode;

                ApModWithholdingTaxCodeDetails mdetails = new ApModWithholdingTaxCodeDetails();
                mdetails.setWtcCode(apWithholdingTaxCode.getWtcCode());
                mdetails.setWtcName(apWithholdingTaxCode.getWtcName());
                mdetails.setWtcDescription(apWithholdingTaxCode.getWtcDescription());
                mdetails.setWtcRate(apWithholdingTaxCode.getWtcRate());
                mdetails.setWtcEnable(apWithholdingTaxCode.getWtcEnable());

                if (apWithholdingTaxCode.getGlChartOfAccount() != null) {

                    mdetails.setWtcCoaGlWithholdingTaxAccountNumber(apWithholdingTaxCode.getGlChartOfAccount().getCoaAccountNumber());
                    mdetails.setWtcCoaGlWithholdingTaxAccountDescription(apWithholdingTaxCode.getGlChartOfAccount().getCoaAccountDescription());
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

    public void addApWtcEntry(ApWithholdingTaxCodeDetails details, String WTC_COA_ACCNT_NMBR, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException {

        Debug.print("ApWithholdingTaxCodeControllerBean addApWtcEntry");

        ArrayList list = new ArrayList();

        try {

            LocalApWithholdingTaxCode apWithholdingTaxCode = null;
            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                apWithholdingTaxCode = apWithholdingTaxCodeHome.findByWtcName(details.getWtcName(), AD_CMPNY);

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

            apWithholdingTaxCode = apWithholdingTaxCodeHome.create(details.getWtcName(), details.getWtcDescription(), details.getWtcRate(), details.getWtcEnable(), AD_CMPNY);

            if (WTC_COA_ACCNT_NMBR != null && WTC_COA_ACCNT_NMBR.length() > 0) {

                glChartOfAccount.addApWithholdingTaxCode(apWithholdingTaxCode);
            }

        } catch (GlobalRecordAlreadyExistException | GlobalAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateApWtcEntry(ApWithholdingTaxCodeDetails details, String WTC_COA_ACCNT_NMBR, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException, GlobalRecordAlreadyAssignedException {

        Debug.print("ApWithholdingTaxCodeControllerBean updateApWtcEntry");

        ArrayList list = new ArrayList();

        try {

            LocalApWithholdingTaxCode apWithholdingTaxCode = null;
            LocalApWithholdingTaxCode apExistingTaxCode = null;
            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                apExistingTaxCode = apWithholdingTaxCodeHome.findByWtcName(details.getWtcName(), AD_CMPNY);

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

            apWithholdingTaxCode = apWithholdingTaxCodeHome.findByPrimaryKey(details.getWtcCode());

            try {

                if ((!apWithholdingTaxCode.getApVouchers().isEmpty() || !apWithholdingTaxCode.getApChecks().isEmpty() || !apWithholdingTaxCode.getApRecurringVouchers().isEmpty()) && (details.getWtcRate() != apWithholdingTaxCode.getWtcRate())) {

                    throw new GlobalRecordAlreadyAssignedException();
                }

            } catch (GlobalRecordAlreadyAssignedException ex) {

                throw ex;
            }

            apWithholdingTaxCode.setWtcName(details.getWtcName());
            apWithholdingTaxCode.setWtcDescription(details.getWtcDescription());
            apWithholdingTaxCode.setWtcRate(details.getWtcRate());
            apWithholdingTaxCode.setWtcEnable(details.getWtcEnable());

            if (apWithholdingTaxCode.getGlChartOfAccount() != null) {

                apWithholdingTaxCode.getGlChartOfAccount().dropApWithholdingTaxCode(apWithholdingTaxCode);
            }

            if (WTC_COA_ACCNT_NMBR != null && WTC_COA_ACCNT_NMBR.length() > 0) {

                glChartOfAccount.addApWithholdingTaxCode(apWithholdingTaxCode);
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

    public void deleteApWtcEntry(Integer WTC_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException {

        Debug.print("ApWithholdingTaxCodeControllerBean deleteApWtcEntry");

        try {

            LocalApWithholdingTaxCode apWithholdingTaxCode = null;

            try {

                apWithholdingTaxCode = apWithholdingTaxCodeHome.findByPrimaryKey(WTC_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            if (!apWithholdingTaxCode.getApSupplierClasses().isEmpty() || !apWithholdingTaxCode.getApVouchers().isEmpty() || !apWithholdingTaxCode.getApChecks().isEmpty() || !apWithholdingTaxCode.getApRecurringVouchers().isEmpty()) {

                throw new GlobalRecordAlreadyAssignedException();
            }

            //		    apWithholdingTaxCode.entityRemove();
            em.remove(apWithholdingTaxCode);

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

        Debug.print("ApWithholdingTaxCodeControllerBean ejbCreate");
    }
}