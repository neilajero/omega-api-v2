/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ApTaxCodeControllerBean
 * @created
 * @author
 */
package com.ejb.txn.ap;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.LocalAdBranchApTaxCodeHome;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.dao.ad.LocalAdResponsibilityHome;
import com.ejb.dao.ap.LocalApTaxCodeHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdBranchApTaxCode;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.entities.ad.LocalAdResponsibility;
import com.ejb.entities.ap.LocalApTaxCode;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.exception.ar.ArTCCoaGlTaxCodeAccountNotFoundException;
import com.ejb.exception.global.*;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdResponsibilityDetails;
import com.util.ap.ApTaxCodeDetails;
import com.util.mod.ad.AdModBranchApTaxCodeDetails;
import com.util.mod.ap.ApModTaxCodeDetails;
import jakarta.ejb.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

@Stateless(name = "ApTaxCodeControllerEJB")
public class ApTaxCodeControllerBean extends EJBContextClass implements ApTaxCodeController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalAdBranchApTaxCodeHome adBranchApTaxCodeHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalAdResponsibilityHome adResHome;
    @EJB
    private LocalApTaxCodeHome apTaxCodeHome;


    public AdResponsibilityDetails getAdRsByRsCode(Integer RS_CODE) throws GlobalNoRecordFoundException {

        Debug.print("ApTaxCodeControllerBean getAdRsByRsCode");

        LocalAdResponsibility adRes = null;


        try {

            adRes = adResHome.findByPrimaryKey(RS_CODE);

        }
        catch (FinderException ex) {

        }

        AdResponsibilityDetails details = new AdResponsibilityDetails();
        details.setRsName(adRes.getRsName());

        return details;
    }

    public ArrayList getAdBrTcAll(Integer BTC_CODE, String RS_NM, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApTaxCodeControllerBean getAdBrSMLAll");

        LocalAdBranchApTaxCode adBranchApTaxCode = null;
        LocalAdBranch adBranch = null;
        LocalGlChartOfAccount glChartOfAccount = null;

        Collection adBranchApTaxCodes = null;

        ArrayList branchList = new ArrayList();

        try {

            adBranchApTaxCodes = adBranchApTaxCodeHome.findBBTCByTcCodeAndRsName(BTC_CODE, RS_NM, AD_CMPNY);

        }
        catch (FinderException ex) {

        }
        catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (adBranchApTaxCodes == null || adBranchApTaxCodes.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }

        try {

            for (Object branchApTaxCode : adBranchApTaxCodes) {

                adBranchApTaxCode = (LocalAdBranchApTaxCode) branchApTaxCode;

                adBranch = adBranchHome.findByPrimaryKey(adBranchApTaxCode.getAdBranch().getBrCode());

                AdModBranchApTaxCodeDetails mdetails = new AdModBranchApTaxCodeDetails();

                mdetails.setBtcBranchCode(adBranch.getBrCode());
                mdetails.setBtcBranchName(adBranch.getBrName());

                glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchApTaxCode.getBtcGlCoaTaxCode());
                mdetails.setBtcTaxCodeAccountNumber(glChartOfAccount.getCoaAccountNumber());
                mdetails.setBtcTaxCodeAccountDescription(glChartOfAccount.getCoaAccountDescription());

                branchList.add(mdetails);
            }

        }
        catch (FinderException ex) {

        }
        catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        return branchList;
    }

    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApTaxCodeControllerBean getAdBrResAll");

        LocalAdBranchResponsibility adBranchResponsibility = null;
        LocalAdBranch adBranch = null;

        Collection adBranchResponsibilities = null;

        ArrayList list = new ArrayList();

        try {

            adBranchResponsibilities = adBranchResponsibilityHome.findByAdResponsibility(RS_CODE, AD_CMPNY);

        }
        catch (FinderException ex) {

        }
        catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (adBranchResponsibilities.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }

        try {

            for (Object branchResponsibility : adBranchResponsibilities) {

                adBranchResponsibility = (LocalAdBranchResponsibility) branchResponsibility;

                adBranch = adBranchResponsibility.getAdBranch();

                AdBranchDetails details = new AdBranchDetails();

                details.setBrCode(adBranch.getBrCode());
                details.setBrName(adBranch.getBrName());

                list.add(details);
            }

        }
        catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        return list;
    }

    public ArrayList getApTcAll(Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApTaxCodeControllerBean getApTcAll");

        ArrayList list = new ArrayList();

        try {

            Collection apTaxCodes = apTaxCodeHome.findTcAll(AD_CMPNY);

            if (apTaxCodes.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            for (Object taxCode : apTaxCodes) {

                LocalApTaxCode apTaxCode = (LocalApTaxCode) taxCode;

                ApModTaxCodeDetails mdetails = new ApModTaxCodeDetails();
                mdetails.setTcCode(apTaxCode.getTcCode());
                mdetails.setTcName(apTaxCode.getTcName());
                mdetails.setTcDescription(apTaxCode.getTcDescription());
                mdetails.setTcType(apTaxCode.getTcType());
                mdetails.setTcRate(apTaxCode.getTcRate());
                mdetails.setTcEnable(apTaxCode.getTcEnable());

                if (apTaxCode.getGlChartOfAccount() != null) {

                    mdetails.setTcCoaGlTaxAccountNumber(apTaxCode.getGlChartOfAccount().getCoaAccountNumber());
                    mdetails.setTcCoaGlTaxDescription(apTaxCode.getGlChartOfAccount().getCoaAccountDescription());
                }

                list.add(mdetails);
            }

            return list;

        }
        catch (GlobalNoRecordFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void addApTcEntry(ApTaxCodeDetails details, String TC_COA_ACCNT_NMBR, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException {

        Debug.print("ApTaxCodeControllerBean addApTcEntry");

        ArrayList list = new ArrayList();


        try {

            LocalApTaxCode apTaxCode = null;
            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                apTaxCode = apTaxCodeHome.findByTcName(details.getTcName(), AD_CMPNY);

                throw new GlobalRecordAlreadyExistException();

            }
            catch (FinderException ex) {

            }

            // get glChartOfAccount to validate accounts

            try {

                if (TC_COA_ACCNT_NMBR != null && TC_COA_ACCNT_NMBR.length() > 0) {

                    glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(TC_COA_ACCNT_NMBR, AD_CMPNY);
                }

            }
            catch (FinderException ex) {

                throw new GlobalAccountNumberInvalidException();
            }

            // create new tax code

            apTaxCode = apTaxCodeHome.create(details.getTcName(), details.getTcDescription(), details.getTcType(), details.getTcRate(), details.getTcEnable(), AD_CMPNY);

            if (TC_COA_ACCNT_NMBR != null && TC_COA_ACCNT_NMBR.length() > 0) {

                glChartOfAccount.addApTaxCode(apTaxCode);
            }

        }
        catch (GlobalRecordAlreadyExistException | GlobalAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateApTcEntry(ApTaxCodeDetails details, String TC_COA_ACCNT_NMBR, String RS_NM, ArrayList branchList, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException, GlobalRecordAlreadyAssignedException {

        Debug.print("ApTaxCodeControllerBean updateApTcEntry");

        LocalAdBranch adBranch = null;
        LocalAdBranchApTaxCode adBranchApTaxCode = null;

        ArrayList list = new ArrayList();

        try {

            LocalApTaxCode apTaxCode = null;
            LocalApTaxCode apExistingTaxCode = null;
            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                apExistingTaxCode = apTaxCodeHome.findByTcName(details.getTcName(), AD_CMPNY);

                if (!apExistingTaxCode.getTcCode().equals(details.getTcCode())) {

                    throw new GlobalRecordAlreadyExistException();
                }

            }
            catch (FinderException ex) {

            }

            // get glChartOfAccount to validate accounts

            try {

                if (TC_COA_ACCNT_NMBR != null && TC_COA_ACCNT_NMBR.length() > 0) {

                    glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(TC_COA_ACCNT_NMBR, AD_CMPNY);
                }

            }
            catch (FinderException ex) {

                throw new GlobalAccountNumberInvalidException();
            }

            // find and update tax code

            apTaxCode = apTaxCodeHome.findByPrimaryKey(details.getTcCode());

            try {

                if ((!apTaxCode.getApVouchers().isEmpty() || !apTaxCode.getApChecks().isEmpty() || !apTaxCode.getApRecurringVouchers().isEmpty()) && (details.getTcRate() != apTaxCode.getTcRate())) {

                    throw new GlobalRecordAlreadyAssignedException();
                }

            }
            catch (GlobalRecordAlreadyAssignedException ex) {

                throw ex;
            }

            apTaxCode.setTcName(details.getTcName());
            apTaxCode.setTcDescription(details.getTcDescription());
            apTaxCode.setTcType(details.getTcType());
            apTaxCode.setTcRate(details.getTcRate());
            apTaxCode.setTcEnable(details.getTcEnable());

            if (apTaxCode.getGlChartOfAccount() != null) {

                apTaxCode.getGlChartOfAccount().dropApTaxCode(apTaxCode);
            }

            if (TC_COA_ACCNT_NMBR != null && TC_COA_ACCNT_NMBR.length() > 0) {

                glChartOfAccount.addApTaxCode(apTaxCode);
            }

            Collection adBranchApTaxCodes = adBranchApTaxCodeHome.findBBTCByTcCodeAndRsName(apTaxCode.getTcCode(), RS_NM, AD_CMPNY);

            // remove all adBranchDSA lines
            for (Object branchApTaxCode : adBranchApTaxCodes) {

                adBranchApTaxCode = (LocalAdBranchApTaxCode) branchApTaxCode;

                apTaxCode.dropAdBranchApTaxCode(adBranchApTaxCode);

                adBranch = adBranchHome.findByPrimaryKey(adBranchApTaxCode.getAdBranch().getBrCode());
                adBranch.dropAdBranchApTaxCode(adBranchApTaxCode);
                //		  	      	adBranchApTaxCode.entityRemove();
                em.remove(adBranchApTaxCode);
            }

            Iterator x = branchList.iterator();

            while (x.hasNext()) {

                AdModBranchApTaxCodeDetails brTcDetails = (AdModBranchApTaxCodeDetails) x.next();

                LocalGlChartOfAccount glTaxCodeCOA = null;
                LocalGlChartOfAccount glBankCOA = null;

                if (brTcDetails.getBtcTaxCodeAccountNumber() != null && brTcDetails.getBtcTaxCodeAccountNumber().length() > 0) {

                    try {

                        glTaxCodeCOA = glChartOfAccountHome.findByCoaAccountNumber(brTcDetails.getBtcTaxCodeAccountNumber(), AD_CMPNY);

                    }
                    catch (FinderException ex) {

                        throw new ArTCCoaGlTaxCodeAccountNotFoundException();
                    }
                }

                adBranchApTaxCode = adBranchApTaxCodeHome.create(glTaxCodeCOA != null ? glTaxCodeCOA.getCoaCode() : null, 'N', AD_CMPNY);

                apTaxCode.addAdBranchApTaxCode(adBranchApTaxCode);
                adBranch = adBranchHome.findByPrimaryKey(brTcDetails.getBtcBranchCode());
                adBranch.addAdBranchApTaxCode(adBranchApTaxCode);
            }

        }
        catch (GlobalRecordAlreadyExistException | GlobalRecordAlreadyAssignedException |
               GlobalAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteApTcEntry(Integer TC_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException {

        Debug.print("ApTaxCodeControllerBean deleteApTcEntry");

        try {

            LocalApTaxCode apTaxCode = null;

            try {

                apTaxCode = apTaxCodeHome.findByPrimaryKey(TC_CODE);

            }
            catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            if (!apTaxCode.getApSupplierClasses().isEmpty() || !apTaxCode.getApVouchers().isEmpty() || !apTaxCode.getApChecks().isEmpty() || !apTaxCode.getApRecurringVouchers().isEmpty() || !apTaxCode.getApPurchaseOrders().isEmpty() || !apTaxCode.getApPurchaseRequisitions().isEmpty()) {

                throw new GlobalRecordAlreadyAssignedException();
            }

            //		    apTaxCode.entityRemove();
            em.remove(apTaxCode);

        }
        catch (GlobalRecordAlreadyDeletedException | GlobalRecordAlreadyAssignedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApTaxCodeControllerBean ejbCreate");
    }

}