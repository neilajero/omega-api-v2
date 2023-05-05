package com.ejb.txn.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.exception.ad.AdBRBranchCodeAlreadyExistsException;
import com.ejb.exception.ad.AdBRBranchNameAlreadyExistsException;
import com.ejb.exception.ad.AdBRHeadQuarterAlreadyExistsException;
import com.ejb.exception.global.GlobalAccountNumberInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.ad.AdBranchDetails;
import com.util.mod.ad.AdModBranchDetails;

import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.Collection;

@Stateless(name = "AdBranchControllerEJB")
public class AdBranchControllerBean extends EJBContextClass implements AdBranchController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalAdBranchHome adBranchHome;

    public ArrayList getAdBrAll(Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdBranchControllerBean getAdBrAll");

        LocalAdBranch adBranch;
        Collection adBranches = null;
        ArrayList list = new ArrayList();
        try {
            adBranches = adBranchHome.findBrAll(companyCode);
        } catch (FinderException ex) {
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        if (adBranches.isEmpty()) {
            throw new GlobalNoRecordFoundException();
        }
        for (Object branch : adBranches) {
            adBranch = (LocalAdBranch) branch;
            AdModBranchDetails mdetails = new AdModBranchDetails();
            mdetails.setBrCode(adBranch.getBrCode());
            mdetails.setBrBranchCode(adBranch.getBrBranchCode());
            mdetails.setBrName(adBranch.getBrName());
            mdetails.setBrDescription(adBranch.getBrDescription());
            mdetails.setBrType(adBranch.getBrType());
            mdetails.setBrRegionName(adBranch.getBrRegionName());
            mdetails.setBrIsBauBranch(adBranch.getBrIsBauBranch());
            mdetails.setBrBauName(adBranch.getBrBauName());
            mdetails.setBrHeadQuarter(adBranch.getBrHeadQuarter());
            mdetails.setBrAddress(adBranch.getBrAddress());
            mdetails.setBrContactPerson(adBranch.getBrContactPerson());
            mdetails.setBrContactNumber(adBranch.getBrContactNumber());
            mdetails.setBrCoaSegment(adBranch.getBrCoaSegment());
            mdetails.setBrDownloadStatus(adBranch.getBrDownloadStatus());
            mdetails.setBrApplyShipping(adBranch.getBrApplyShipping());
            mdetails.setBrPercentMarkup(adBranch.getBrPercentMarkup());
            if (adBranch.getBrApplyShipping() == EJBCommon.TRUE) {
                mdetails.setBrGlCoaAccountNumber(adBranch.getGlChartOfAccount().getCoaAccountNumber());
                mdetails.setBrGlCoaAccountDescription(adBranch.getGlChartOfAccount().getCoaAccountDescription());
            }
            list.add(mdetails);
        }
        return list;
    }

    public void addAdBrEntry(AdBranchDetails details, String coaAccountNumber, Integer companyCode) throws AdBRHeadQuarterAlreadyExistsException, AdBRBranchCodeAlreadyExistsException, AdBRBranchNameAlreadyExistsException, GlobalAccountNumberInvalidException {

        Debug.print("AdBranchControllerBean addAdBrEntry");

        LocalAdBranch adBranch;
        LocalGlChartOfAccount glAccount = null;
        // check if branch code already exists
        try {
            adBranch = adBranchHome.findByBrBranchCode(details.getBrBranchCode(), companyCode);
            throw new AdBRBranchCodeAlreadyExistsException();
        } catch (AdBRBranchCodeAlreadyExistsException ex) {
            throw ex;
        } catch (FinderException ex) {
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        // check if branch name already exists
        try {
            adBranch = adBranchHome.findByBrName(details.getBrName(), companyCode);
            throw new AdBRBranchNameAlreadyExistsException();
        } catch (AdBRBranchNameAlreadyExistsException ex) {
            throw ex;
        } catch (FinderException ex) {
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        if (details.getBrHeadQuarter() == EJBCommon.TRUE) {
            try {
                adBranch = adBranchHome.findByBrHeadQuarter(companyCode);
                throw new AdBRHeadQuarterAlreadyExistsException();
            } catch (AdBRHeadQuarterAlreadyExistsException ex) {
                throw ex;
            } catch (FinderException ex) {
            } catch (Exception ex) {
                throw new EJBException(ex.getMessage());
            }
        }
        try {
            try {
                if (coaAccountNumber != null && coaAccountNumber.length() > 0) {
                    glAccount = glChartOfAccountHome.findByCoaAccountNumber(coaAccountNumber, companyCode);
                }
            } catch (FinderException ex) {
                throw new GlobalAccountNumberInvalidException();
            }

            // create new branch
            adBranch = adBranchHome.BrBranchCode(details.getBrBranchCode()).BrName(details.getBrName()).BrDescription(details.getBrDescription()).BrType(details.getBrType()).BrRegionName(details.getBrRegionName()).BrIsBauBranch(details.getBrIsBauBranch()).BrBauName(details.getBrBauName()).BrHeadQuarter(details.getBrHeadQuarter()).BrAddress(details.getBrAddress()).BrContactPerson(details.getBrContactPerson()).BrContactNumber(details.getBrContactNumber()).BrDownloadStatus('N').BrApplyShipping(details.getBrApplyShipping()).BrPercentMarkup(details.getBrPercentMarkup()).BrAdCompany(companyCode).buildBranch();

            if (glAccount != null) {
                glAccount.addAdBranch(adBranch);
            }

        } catch (GlobalAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateAdBrEntry(AdBranchDetails details, String coaAccountNumber, Integer companyCode) throws AdBRHeadQuarterAlreadyExistsException, AdBRBranchCodeAlreadyExistsException, AdBRBranchNameAlreadyExistsException, GlobalAccountNumberInvalidException {

        Debug.print("AdBranchControllerBean updateAdBrEntry");

        LocalAdBranch adBranch;
        LocalGlChartOfAccount glAccount = null;
        try {
            LocalAdBranch arExistingBranch = adBranchHome.findByBrBranchCode(details.getBrBranchCode(), companyCode);
            if (!arExistingBranch.getBrCode().equals(details.getBrCode())) {
                throw new AdBRBranchCodeAlreadyExistsException();
            }
        } catch (AdBRBranchCodeAlreadyExistsException ex) {
            throw ex;
        } catch (FinderException ex) {
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        try {
            LocalAdBranch arExistingBranch = adBranchHome.findByBrName(details.getBrName(), companyCode);
            if (!arExistingBranch.getBrCode().equals(details.getBrCode())) {
                throw new AdBRBranchNameAlreadyExistsException();
            }
        } catch (AdBRBranchNameAlreadyExistsException ex) {
            throw ex;
        } catch (FinderException ex) {
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        if (details.getBrHeadQuarter() == EJBCommon.TRUE) {
            try {
                LocalAdBranch adBranchHeadQuarter = adBranchHome.findByBrHeadQuarter(companyCode);
                if (!adBranchHeadQuarter.getBrCode().equals(details.getBrCode())) {
                    throw new AdBRHeadQuarterAlreadyExistsException();
                }
            } catch (AdBRHeadQuarterAlreadyExistsException ex) {
                throw ex;
            } catch (FinderException ex) {
            } catch (Exception ex) {
                throw new EJBException(ex.getMessage());
            }
        }
        try {
            try {
                if (coaAccountNumber != null && coaAccountNumber.length() > 0) {
                    glAccount = glChartOfAccountHome.findByCoaAccountNumber(coaAccountNumber, companyCode);
                }
            } catch (FinderException ex) {
                throw new GlobalAccountNumberInvalidException();
            }

            // find and update branch
            adBranch = adBranchHome.findByPrimaryKey(details.getBrCode());

            adBranch.setBrBranchCode(details.getBrBranchCode());
            adBranch.setBrName(details.getBrName());
            adBranch.setBrDescription(details.getBrDescription());
            adBranch.setBrType(details.getBrType());
            adBranch.setBrRegionName(details.getBrRegionName());
            adBranch.setBrIsBauBranch(details.getBrIsBauBranch());
            adBranch.setBrBauName(details.getBrBauName());
            adBranch.setBrHeadQuarter(details.getBrHeadQuarter());
            adBranch.setBrAddress(details.getBrAddress());
            adBranch.setBrContactPerson(details.getBrContactPerson());
            adBranch.setBrContactNumber(details.getBrContactNumber());

            if (adBranch.getBrDownloadStatus() == 'N') {
                adBranch.setBrDownloadStatus('U');
            } else if (adBranch.getBrDownloadStatus() == 'D') {
                adBranch.setBrDownloadStatus('X');
            }

            adBranch.setBrApplyShipping(details.getBrApplyShipping());
            adBranch.setBrPercentMarkup(details.getBrPercentMarkup());

            if (glAccount != null) {
                glAccount.addAdBranch(adBranch);
            } else if (glAccount == null && adBranch.getGlChartOfAccount() != null) {
                adBranch.getGlChartOfAccount().dropAdBranch(adBranch);
            }

        } catch (GlobalAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            ex.printStackTrace();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteAdBrEntry(Integer BR_CODE, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException {

        Debug.print("AdBranchControllerBean deleteAdBrEntry");

        LocalAdBranch adBranch;
        try {
            adBranch = adBranchHome.findByPrimaryKey(BR_CODE);
            if (adBranch.getBrDownloadStatus() == 'D' || adBranch.getBrDownloadStatus() == 'X') {
                throw new GlobalRecordAlreadyAssignedException();
            }
        } catch (GlobalRecordAlreadyAssignedException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        try {
            if (!adBranch.getAdBranchResponsibilities().isEmpty() || !adBranch.getAdBranchBankAccounts().isEmpty() || !adBranch.getAdBranchCoas().isEmpty() || !adBranch.getAdBranchCustomer().isEmpty() || !adBranch.getAdBranchDocumentSequenceAssignments().isEmpty() || !adBranch.getAdBranchItemLocation().isEmpty() || !adBranch.getAdBranchStandardMemoLine().isEmpty() || !adBranch.getAdBranchSupplier().isEmpty() || !adBranch.getInvBranchStockTransfers().isEmpty()) {
                throw new GlobalRecordAlreadyAssignedException();
            }
        } catch (GlobalRecordAlreadyAssignedException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        try {
            em.remove(adBranch);
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer companyCode) {

        Debug.print("AdBranchControllerBean getGlFcPrecisionUnit");

        try {
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("AdBranchControllerBean ejbCreate");
    }

}