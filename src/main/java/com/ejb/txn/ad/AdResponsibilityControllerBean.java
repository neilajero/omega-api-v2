package com.ejb.txn.ad;

import java.util.*;

import jakarta.ejb.*;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.entities.ad.*;
import com.ejb.exception.ad.AdRSResponsibilityNotAssignedToBranchException;
import com.ejb.exception.global.*;
import com.util.*;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdResponsibilityDetails;

@Stateless(name = "AdResponsibilityControllerEJB")
public class AdResponsibilityControllerBean extends EJBContextClass implements AdResponsibilityController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    LocalAdResponsibilityHome adResponsibilityHome;
    @EJB
    LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    LocalAdFormFunctionResponsibilityHome adFormFunctionResponsibilityHome;
    @EJB
    LocalAdBranchHome adBranchHome;

    public ArrayList getAdRsAll(Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdResponsibilityControllerBean getAdRsAll");

        Collection adResponsibilities = null;
        LocalAdResponsibility adResponsibility;
        ArrayList list = new ArrayList();
        try {

            adResponsibilities = adResponsibilityHome.findRsAll(companyCode);

        } catch (FinderException ex) {
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        if (adResponsibilities.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }
        for (Object responsibility : adResponsibilities) {

            adResponsibility = (LocalAdResponsibility) responsibility;

            AdResponsibilityDetails details = new AdResponsibilityDetails();
            details.setRsCode(adResponsibility.getRsCode());
            details.setRsName(adResponsibility.getRsName());
            details.setRsDescription(adResponsibility.getRsDescription());
            details.setRsDateFrom(adResponsibility.getRsDateFrom());
            details.setRsDateTo(adResponsibility.getRsDateTo());

            list.add(details);
        }
        return list;
    }

    public LocalAdResponsibility getAdResByResNm(String RS_NM, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdResponsibilityControllerBean getAdResByResNm");

        try {

            return adResponsibilityHome.findByRsName(RS_NM, companyCode);

        } catch (FinderException ex) {

            throw new GlobalNoRecordFoundException();
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    public LocalAdResponsibility getAdResByResCode(Integer responsibilityCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdResponsibilityControllerBean getAdResByResCode");

        try {

            return adResponsibilityHome.findByPrimaryKey(responsibilityCode);

        } catch (FinderException ex) {

            throw new GlobalNoRecordFoundException();
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdBrAll(Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdResponsibilityControllerBean getAdBrAll");

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

            AdBranchDetails details = new AdBranchDetails();
            details.setBrCode(adBranch.getBrCode());
            details.setBrBranchCode(adBranch.getBrBranchCode());
            details.setBrName(adBranch.getBrName());

            list.add(details);
        }
        return list;
    }

    public ArrayList getAdBrResAll(Integer responsibilityCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdResponsibilityControllerBean getAdBrResAll");

        LocalAdBranchResponsibility adBranchResponsibility;
        LocalAdBranch adBranch;
        Collection adBranchResponsibilities = null;
        ArrayList list = new ArrayList();
        try {

            adBranchResponsibilities = adBranchResponsibilityHome.findByAdResponsibility(responsibilityCode, companyCode);

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

                adBranch = adBranchHome.findByPrimaryKey(adBranchResponsibility.getAdBranch().getBrCode());

                AdBranchDetails details = new AdBranchDetails();
                details.setBrCode(adBranch.getBrCode());
                details.setBrBranchCode(adBranch.getBrBranchCode());
                details.setBrName(adBranch.getBrName());

                list.add(details);
            }

        } catch (FinderException ex) {
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        return list;
    }

    public void addAdRsEntry(AdResponsibilityDetails details, ArrayList branchList, Integer companyCode) throws GlobalRecordAlreadyExistException, AdRSResponsibilityNotAssignedToBranchException {
        Debug.print("AdResponsibilityControllerBean addAdRsEntry");
        LocalAdResponsibility adResponsibility;
        LocalAdBranch adBranch;
        LocalAdBranchResponsibility adBranchResponsibility;
        if (branchList.size() == 0) {

            throw new AdRSResponsibilityNotAssignedToBranchException();
        }
        try {

            adResponsibility = adResponsibilityHome.findByRsName(details.getRsName(), companyCode);

            throw new GlobalRecordAlreadyExistException();

        } catch (GlobalRecordAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        try {

            // create new responsibility

            adResponsibility = adResponsibilityHome.create(details.getRsName(), details.getRsDescription(), details.getRsDateFrom(), details.getRsDateTo(), companyCode);

            for (Object o : branchList) {

                AdBranchDetails brDetails = (AdBranchDetails) o;
                adBranchResponsibility = adBranchResponsibilityHome.create(companyCode);

                adResponsibility.addAdBranchResponsibility(adBranchResponsibility);

                adBranch = adBranchHome.findByPrimaryKey(brDetails.getBrCode());
                adBranch.addAdBranchResponsibility(adBranchResponsibility);
            }

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    public void copyAdRsEntry(AdResponsibilityDetails details, ArrayList branchList, Integer companyCode) {

        Debug.print("AdResponsibilityControllerBean copyAdRsEntry");

        LocalAdResponsibility adResponsibilityNew;
        LocalAdBranch adBranchNew;
        LocalAdBranchResponsibility adBranchResponsibilityNew;
        ArrayList list = new ArrayList();
        try {
            LocalAdResponsibility adResponsibilitySource = adResponsibilityHome.findByRsName(details.getRsName(), companyCode);
            // create new responsibility

            adResponsibilityNew = adResponsibilityHome.create(("Copy of " + details.getRsName()), details.getRsDescription(), details.getRsDateFrom(), details.getRsDateTo(), companyCode);

            Iterator i = branchList.iterator();

            while (i.hasNext()) {

                AdBranchDetails brDetails = (AdBranchDetails) i.next();
                adBranchResponsibilityNew = adBranchResponsibilityHome.create(companyCode);

                adResponsibilityNew.addAdBranchResponsibility(adBranchResponsibilityNew);

                adBranchNew = adBranchHome.findByPrimaryKey(brDetails.getBrCode());
                adBranchNew.addAdBranchResponsibility(adBranchResponsibilityNew);
            }

            Collection adFormFunctionResponsibilities = adResponsibilitySource.getAdFormFunctionResponsibilities();

            i = adFormFunctionResponsibilities.iterator();

            while (i.hasNext()) {

                LocalAdFormFunctionResponsibility adFormFunctionResponsibility = (LocalAdFormFunctionResponsibility) i.next();

                // add form function

                LocalAdFormFunctionResponsibility adFormFunctionResponsibilityNew = adFormFunctionResponsibilityHome.create(adFormFunctionResponsibility.getFrParameter(), adFormFunctionResponsibility.getFrAdCompany());

                adResponsibilityNew.addAdFormFunctionResponsibility(adFormFunctionResponsibilityNew);

                adFormFunctionResponsibility.getAdFormFunction().addAdFormFunctionResponsibility(adFormFunctionResponsibilityNew);
            }

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    public void updateAdRsEntry(AdResponsibilityDetails details, ArrayList branchList, Integer companyCode) throws GlobalRecordAlreadyExistException, AdRSResponsibilityNotAssignedToBranchException {

        Debug.print("AdResponsibilityControllerBean updateAdRsEntry");

        LocalAdResponsibility adResponsibility;
        LocalAdBranchResponsibility adBranchResponsibility;
        LocalAdBranch adBranch;
        if (branchList.size() == 0) {

            throw new AdRSResponsibilityNotAssignedToBranchException();
        }
        try {

            LocalAdResponsibility arExistingResponsibility = adResponsibilityHome.findByRsName(details.getRsName(), companyCode);

            if (!arExistingResponsibility.getRsCode().equals(details.getRsCode())) {

                throw new GlobalRecordAlreadyExistException();
            }

        } catch (GlobalRecordAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        try {

            // find and update responsibility

            adResponsibility = adResponsibilityHome.findByPrimaryKey(details.getRsCode());

            adResponsibility.setRsName(details.getRsName());
            adResponsibility.setRsDescription(details.getRsDescription());
            adResponsibility.setRsDateFrom(details.getRsDateFrom());
            adResponsibility.setRsDateTo(details.getRsDateTo());

            Collection adBranchResponsibilities = adBranchResponsibilityHome.findByAdResponsibility(adResponsibility.getRsCode(), companyCode);

            // remove all adBranchResponsibility lines
            for (Object branchResponsibility : adBranchResponsibilities) {

                adBranchResponsibility = (LocalAdBranchResponsibility) branchResponsibility;

                adResponsibility.dropAdBranchResponsibility(adBranchResponsibility);

                adBranch = adBranchHome.findByPrimaryKey(adBranchResponsibility.getAdBranch().getBrCode());
                adBranch.dropAdBranchResponsibility(adBranchResponsibility);
                em.remove(adBranchResponsibility);
            }

            // add adBranchResponsibility lines

            for (Object o : branchList) {

                AdBranchDetails brDetails = (AdBranchDetails) o;

                adBranchResponsibility = adBranchResponsibilityHome.create(companyCode);
                adResponsibility.addAdBranchResponsibility(adBranchResponsibility);

                adBranch = adBranchHome.findByPrimaryKey(brDetails.getBrCode());
                adBranch.addAdBranchResponsibility(adBranchResponsibility);
            }

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteAdRsEntry(Integer responsibilityCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException {

        Debug.print("AdResponsibilityControllerBean deleteAdRsEntry");

        LocalAdResponsibility adResponsibility;
        try {

            adResponsibility = adResponsibilityHome.findByPrimaryKey(responsibilityCode);

        } catch (FinderException ex) {

            throw new GlobalRecordAlreadyDeletedException();

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        try {

            if (!adResponsibility.getAdUserResponsibilities().isEmpty()) {

                throw new GlobalRecordAlreadyAssignedException();
            }

        } catch (GlobalRecordAlreadyAssignedException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        try {

            em.remove(adResponsibility);

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();

            throw new EJBException(ex.getMessage());

        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("AdResponsibilityControllerBean ejbCreate");
    }
}