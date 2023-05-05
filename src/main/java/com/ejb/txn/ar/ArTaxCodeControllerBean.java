/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArTaxCodeControllerBean
 * @created March 5, 2004, 1:03 PM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.LocalAdBranchArTaxCodeHome;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.dao.ad.LocalAdResponsibilityHome;
import com.ejb.dao.ar.LocalArTaxCodeHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdBranchArTaxCode;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.entities.ad.LocalAdResponsibility;
import com.ejb.entities.ar.LocalArTaxCode;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.exception.ar.ArTCCoaGlTaxCodeAccountNotFoundException;
import com.ejb.exception.ar.ArTCInterimAccountInvalidException;
import com.ejb.exception.global.*;
import com.util.*;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdResponsibilityDetails;
import com.util.ar.ArTaxCodeDetails;
import com.util.mod.ad.AdModBranchArTaxCodeDetails;
import com.util.mod.ar.ArModTaxCodeDetails;

import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

@Stateless(name = "ArTaxCodeControllerEJB")
public class ArTaxCodeControllerBean extends EJBContextClass implements ArTaxCodeController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalAdResponsibilityHome adResHome;
    @EJB
    private LocalAdBranchArTaxCodeHome adBranchArTaxCodeHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalArTaxCodeHome arTaxCodeHome;

    public AdResponsibilityDetails getAdRsByRsCode(Integer RS_CODE) throws GlobalNoRecordFoundException {
        Debug.print("ArTaxCodeControllerBean getAdRsByRsCode");
        LocalAdResponsibility adRes = null;
        try {
            adRes = adResHome.findByPrimaryKey(RS_CODE);
        } catch (FinderException ex) {
        }
        AdResponsibilityDetails details = new AdResponsibilityDetails();
        details.setRsName(adRes.getRsName());
        return details;
    }

    public ArrayList getAdBrTcAll(Integer BTC_CODE, String RS_NM, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArTaxCodeControllerBean getAdBrSMLAll");
        LocalAdBranchArTaxCode adBranchArTaxCode = null;
        LocalAdBranch adBranch = null;
        LocalGlChartOfAccount glChartOfAccount = null;
        LocalGlChartOfAccount glInterimAccount = null;
        Collection adBranchArTaxCodes = null;
        ArrayList branchList = new ArrayList();
        try {

            adBranchArTaxCodes = adBranchArTaxCodeHome.findBBTCByTcCodeAndRsName(BTC_CODE, RS_NM, AD_CMPNY);

        } catch (FinderException ex) {
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (adBranchArTaxCodes == null || adBranchArTaxCodes.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }

        try {

            for (Object branchArTaxCode : adBranchArTaxCodes) {

                adBranchArTaxCode = (LocalAdBranchArTaxCode) branchArTaxCode;

                adBranch = adBranchHome.findByPrimaryKey(adBranchArTaxCode.getAdBranch().getBrCode());

                AdModBranchArTaxCodeDetails mdetails = new AdModBranchArTaxCodeDetails();

                mdetails.setBtcBranchCode(adBranch.getBrCode());
                mdetails.setBtcBranchName(adBranch.getBrName());

                glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchArTaxCode.getBtcGlCoaTaxCode());
                mdetails.setBtcTaxCodeAccountNumber(glChartOfAccount.getCoaAccountNumber());
                mdetails.setBtcTaxCodeAccountDescription(glChartOfAccount.getCoaAccountDescription());

                try {
                    if (adBranchArTaxCode.getBtcGlCoaInterimCode() != null) {
                        glInterimAccount = glChartOfAccountHome.findByPrimaryKey(adBranchArTaxCode.getBtcGlCoaInterimCode());
                    }

                } catch (Exception ex) {

                }

                mdetails.setBtcInterimCodeAccountNumber(glInterimAccount != null ? glInterimAccount.getCoaAccountNumber() : null);
                mdetails.setBtcInterimCodeAccountDescription(glInterimAccount != null ? glInterimAccount.getCoaAccountDescription() : null);

                branchList.add(mdetails);
            }

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        return branchList;
    }

    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArTaxCodeControllerBean getAdBrResAll");
        LocalAdBranchResponsibility adBranchResponsibility = null;
        LocalAdBranch adBranch = null;
        Collection adBranchResponsibilities = null;
        ArrayList list = new ArrayList();

        try {

            adBranchResponsibilities = adBranchResponsibilityHome.findByAdResponsibility(RS_CODE, AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

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

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        return list;
    }

    public ArrayList getArTcAll(Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArTaxCodeControllerBean getArTcAll");
        ArrayList list = new ArrayList();
        try {

            Collection arTaxCodes = arTaxCodeHome.findTcAll(AD_CMPNY);

            if (arTaxCodes.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            for (Object taxCode : arTaxCodes) {

                LocalArTaxCode arTaxCode = (LocalArTaxCode) taxCode;

                ArModTaxCodeDetails mdetails = new ArModTaxCodeDetails();
                LocalGlChartOfAccount glTaxCodeChartOfAccount = null;
                LocalGlChartOfAccount glInterimChartOfAccount = null;

                mdetails.setTcCode(arTaxCode.getTcCode());
                mdetails.setTcName(arTaxCode.getTcName());
                mdetails.setTcDescription(arTaxCode.getTcDescription());
                mdetails.setTcType(arTaxCode.getTcType());
                mdetails.setTcRate(arTaxCode.getTcRate());
                mdetails.setTcEnable(arTaxCode.getTcEnable());

                // Get Cash Account Number with Description

                if (arTaxCode.getGlChartOfAccount() != null) {

                    try {

                        glTaxCodeChartOfAccount = glChartOfAccountHome.findByPrimaryKey(arTaxCode.getGlChartOfAccount().getCoaCode());

                        mdetails.setTcCoaGlTaxAccountNumber(glTaxCodeChartOfAccount.getCoaAccountNumber());
                        mdetails.setTcCoaGlTaxDescription(glTaxCodeChartOfAccount.getCoaAccountDescription());
                    } catch (FinderException ex) {

                    }
                }

                if (arTaxCode.getTcInterimAccount() != null) {

                    glInterimChartOfAccount = glChartOfAccountHome.findByPrimaryKey(arTaxCode.getTcInterimAccount());

                    mdetails.setTcInterimAccountNumber(glInterimChartOfAccount.getCoaAccountNumber());
                    mdetails.setTcInterimAccountDescription(glInterimChartOfAccount.getCoaAccountDescription());
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

    public void addArTcEntry(ArTaxCodeDetails details, String TC_COA_ACCNT_NMBR, String TC_INTRM_ACCNT_NMBR, ArrayList branchList, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException, ArTCInterimAccountInvalidException {
        Debug.print("ArTaxCodeControllerBean addArTcEntry");
        LocalAdBranch adBranch = null;
        LocalAdBranchArTaxCode adBranchArTaxCode = null;
        try {

            LocalArTaxCode arTaxCode = null;
            LocalGlChartOfAccount glChartOfAccount = null;
            LocalGlChartOfAccount glInterimChartOfAccount = null;

            try {

                arTaxCode = arTaxCodeHome.findByTcName(details.getTcName(), AD_CMPNY);

                throw new GlobalRecordAlreadyExistException();

            } catch (FinderException ex) {

            }

            // get glChartOfAccount to validate accounts

            try {

                if (TC_COA_ACCNT_NMBR != null && TC_COA_ACCNT_NMBR.length() > 0) {

                    glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(TC_COA_ACCNT_NMBR, AD_CMPNY);
                }

            } catch (FinderException ex) {

                throw new GlobalAccountNumberInvalidException();
            }

            // get glChartOfAccount to validate accounts

            try {

                if (TC_INTRM_ACCNT_NMBR != null && TC_INTRM_ACCNT_NMBR.length() > 0) {

                    glInterimChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(TC_INTRM_ACCNT_NMBR, AD_CMPNY);
                }

            } catch (FinderException ex) {

                throw new ArTCInterimAccountInvalidException();
            }

            // create new tax code

            arTaxCode = arTaxCodeHome.create(details.getTcName(), details.getTcDescription(), details.getTcType(), glInterimChartOfAccount != null ? glInterimChartOfAccount.getCoaCode() : null, details.getTcRate(), details.getTcEnable(), AD_CMPNY);

            if (TC_COA_ACCNT_NMBR != null && TC_COA_ACCNT_NMBR.length() > 0) {

                glChartOfAccount.addArTaxCode(arTaxCode);
            }

            Iterator x = branchList.iterator();
            Debug.print("branchList=" + branchList.size());
            Debug.print("--------------------->5");

            while (x.hasNext()) {

                AdModBranchArTaxCodeDetails brTcDetails = (AdModBranchArTaxCodeDetails) x.next();

                LocalGlChartOfAccount glTaxCodeCOA = null;
                LocalGlChartOfAccount glInterimCodeCOA = null;

                if (brTcDetails.getBtcTaxCodeAccountNumber() != null && brTcDetails.getBtcTaxCodeAccountNumber().length() > 0) {

                    try {

                        glTaxCodeCOA = glChartOfAccountHome.findByCoaAccountNumber(brTcDetails.getBtcTaxCodeAccountNumber(), AD_CMPNY);

                    } catch (FinderException ex) {

                        throw new ArTCCoaGlTaxCodeAccountNotFoundException();
                    }
                }

                if (brTcDetails.getBtcInterimCodeAccountNumber() != null && brTcDetails.getBtcInterimCodeAccountNumber().length() > 0) {

                    try {

                        glInterimCodeCOA = glChartOfAccountHome.findByCoaAccountNumber(brTcDetails.getBtcInterimCodeAccountNumber(), AD_CMPNY);

                    } catch (FinderException ex) {

                        throw new ArTCCoaGlTaxCodeAccountNotFoundException();
                    }
                }

                adBranchArTaxCode = adBranchArTaxCodeHome.create(glTaxCodeCOA != null ? glTaxCodeCOA.getCoaCode() : null, glInterimCodeCOA != null ? glInterimCodeCOA.getCoaCode() : null, 'N', AD_CMPNY);

                arTaxCode.addAdBranchArTaxCode(adBranchArTaxCode);
                adBranch = adBranchHome.findByPrimaryKey(brTcDetails.getBtcBranchCode());
                adBranch.addAdBranchArTaxCode(adBranchArTaxCode);
            }

        } catch (GlobalRecordAlreadyExistException | ArTCInterimAccountInvalidException |
                 GlobalAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateArTcEntry(ArTaxCodeDetails details, String TC_COA_ACCNT_NMBR, String TC_INTRM_ACCNT_NMBR, String RS_NM, ArrayList branchList, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException, GlobalRecordAlreadyAssignedException, ArTCCoaGlTaxCodeAccountNotFoundException, ArTCInterimAccountInvalidException {
        Debug.print("ArTaxCodeControllerBean updateArTcEntry");
        LocalAdBranch adBranch = null;
        LocalAdBranchArTaxCode adBranchArTaxCode = null;
        try {

            LocalArTaxCode arTaxCode = null;
            LocalArTaxCode arExistingTaxCode = null;
            LocalGlChartOfAccount glChartOfAccount = null;
            LocalGlChartOfAccount glInterimChartOfAccount = null;

            try {

                arExistingTaxCode = arTaxCodeHome.findByTcName(details.getTcName(), AD_CMPNY);

                if (!arExistingTaxCode.getTcCode().equals(details.getTcCode())) {

                    throw new GlobalRecordAlreadyExistException();
                }

            } catch (FinderException ex) {

            }

            // get glChartOfAccount to validate accounts

            try {

                if (TC_COA_ACCNT_NMBR != null && TC_COA_ACCNT_NMBR.length() > 0) {

                    glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(TC_COA_ACCNT_NMBR, AD_CMPNY);
                }

            } catch (FinderException ex) {

                throw new GlobalAccountNumberInvalidException();
            }

            // get glChartOfAccount to validate accounts

            try {

                if (TC_INTRM_ACCNT_NMBR != null && TC_INTRM_ACCNT_NMBR.length() > 0) {

                    glInterimChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(TC_INTRM_ACCNT_NMBR, AD_CMPNY);
                }

            } catch (FinderException ex) {

                throw new ArTCInterimAccountInvalidException();
            }

            // find and update tax code

            arTaxCode = arTaxCodeHome.findByPrimaryKey(details.getTcCode());

            try {

                if ((!arTaxCode.getArInvoices().isEmpty() || !arTaxCode.getArReceipts().isEmpty()) && (details.getTcRate() != arTaxCode.getTcRate())) {

                    throw new GlobalRecordAlreadyAssignedException();
                }

            } catch (GlobalRecordAlreadyAssignedException ex) {

                throw ex;
            }

            arTaxCode.setTcName(details.getTcName());
            arTaxCode.setTcDescription(details.getTcDescription());
            arTaxCode.setTcType(details.getTcType());
            arTaxCode.setTcInterimAccount(glInterimChartOfAccount != null ? glInterimChartOfAccount.getCoaCode() : null);
            Debug.print("details.getTcRate()=" + details.getTcRate());
            arTaxCode.setTcRate(details.getTcRate());
            arTaxCode.setTcEnable(details.getTcEnable());

            if (arTaxCode.getGlChartOfAccount() != null) {

                arTaxCode.getGlChartOfAccount().dropArTaxCode(arTaxCode);
            }

            if (TC_COA_ACCNT_NMBR != null && TC_COA_ACCNT_NMBR.length() > 0) {

                glChartOfAccount.addArTaxCode(arTaxCode);
            }

            Collection adBranchArTaxCodes = adBranchArTaxCodeHome.findBBTCByTcCodeAndRsName(arTaxCode.getTcCode(), RS_NM, AD_CMPNY);

            // remove all adBranchDSA lines
            for (Object branchArTaxCode : adBranchArTaxCodes) {

                adBranchArTaxCode = (LocalAdBranchArTaxCode) branchArTaxCode;

                arTaxCode.dropAdBranchArTaxCode(adBranchArTaxCode);

                adBranch = adBranchHome.findByPrimaryKey(adBranchArTaxCode.getAdBranch().getBrCode());
                adBranch.dropAdBranchArTaxCode(adBranchArTaxCode);
                //		  	      	adBranchArTaxCode.entityRemove();
                em.remove(adBranchArTaxCode);
            }

            Iterator x = branchList.iterator();
            Debug.print("branchList=" + branchList.size());
            Debug.print("--------------------->5");

            while (x.hasNext()) {

                AdModBranchArTaxCodeDetails brTcDetails = (AdModBranchArTaxCodeDetails) x.next();

                LocalGlChartOfAccount glTaxCodeCOA = null;
                LocalGlChartOfAccount glInterimCodeCOA = null;
                LocalGlChartOfAccount glBankCOA = null;

                if (brTcDetails.getBtcTaxCodeAccountNumber() != null && brTcDetails.getBtcTaxCodeAccountNumber().length() > 0) {

                    try {

                        glTaxCodeCOA = glChartOfAccountHome.findByCoaAccountNumber(brTcDetails.getBtcTaxCodeAccountNumber(), AD_CMPNY);

                    } catch (FinderException ex) {

                        throw new ArTCCoaGlTaxCodeAccountNotFoundException();
                    }
                }

                if (brTcDetails.getBtcInterimCodeAccountNumber() != null && brTcDetails.getBtcInterimCodeAccountNumber().length() > 0) {

                    try {

                        glInterimCodeCOA = glChartOfAccountHome.findByCoaAccountNumber(brTcDetails.getBtcInterimCodeAccountNumber(), AD_CMPNY);

                    } catch (FinderException ex) {

                        throw new ArTCCoaGlTaxCodeAccountNotFoundException();
                    }
                }

                adBranchArTaxCode = adBranchArTaxCodeHome.create(glTaxCodeCOA != null ? glTaxCodeCOA.getCoaCode() : null, glInterimCodeCOA != null ? glInterimCodeCOA.getCoaCode() : null, 'N', AD_CMPNY);

                arTaxCode.addAdBranchArTaxCode(adBranchArTaxCode);
                adBranch = adBranchHome.findByPrimaryKey(brTcDetails.getBtcBranchCode());
                adBranch.addAdBranchArTaxCode(adBranchArTaxCode);
            }

        } catch (GlobalRecordAlreadyExistException | ArTCInterimAccountInvalidException |
                 GlobalRecordAlreadyAssignedException | GlobalAccountNumberInvalidException |
                 ArTCCoaGlTaxCodeAccountNotFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteArTcEntry(Integer TC_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException {
        Debug.print("ArTaxCodeControllerBean deleteArTcEntry");
        try {

            LocalArTaxCode arTaxCode = null;

            try {

                arTaxCode = arTaxCodeHome.findByPrimaryKey(TC_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            if (!arTaxCode.getArCustomerClasses().isEmpty() || !arTaxCode.getArInvoices().isEmpty() || !arTaxCode.getArReceipts().isEmpty() || !arTaxCode.getArPdcs().isEmpty() || !arTaxCode.getArSalesOrders().isEmpty()) {

                throw new GlobalRecordAlreadyAssignedException();
            }

            //		    arTaxCode.entityRemove();
            em.remove(arTaxCode);

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

        Debug.print("ArTaxCodeControllerBean ejbCreate");
    }

}