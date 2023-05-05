package com.ejb.txn.ad;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.*;

import jakarta.ejb.*;

import com.ejb.dao.ad.*;
import com.ejb.entities.ad.*;
import com.ejb.exception.global.*;
import com.util.*;
import com.util.ad.AdUserDetails;
import com.util.ad.AdUserResponsibilityDetails;
import com.util.mod.ad.AdModUserResponsibilityDetails;

@Stateless(name = "AdUserResponsibilityControllerEJB")
public class AdUserResponsibilityControllerBean extends EJBContextClass implements AdUserResponsibilityController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalAdResponsibilityHome adResponsibilityHome;
    @EJB
    private LocalAdUserResponsibilityHome adUserResponsibilityHome;
    @EJB
    private LocalAdUserHome adUserHome;

    public ArrayList getAdRsAll(Integer companyCode) {

        Debug.print("ArCustomerEntryControllerBean getAdRsAll");

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

            return null;
        }
        for (Object responsibility : adResponsibilities) {

            adResponsibility = (LocalAdResponsibility) responsibility;

            list.add(adResponsibility.getRsName());
        }
        return list;
    }

    public ArrayList getAdUrByUsrCode(Integer userCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdUserResponsibilityControllerBean getAdUrByUsrCode");

        Collection adUserResponsibilities = null;
        LocalAdUserResponsibility adUserResponsibility;
        ArrayList list = new ArrayList();
        try {

            adUserResponsibilities = adUserResponsibilityHome.findByUsrCode(userCode, companyCode);

        } catch (FinderException ex) {
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        if (adUserResponsibilities.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }
        for (Object userResponsibility : adUserResponsibilities) {

            adUserResponsibility = (LocalAdUserResponsibility) userResponsibility;

            AdModUserResponsibilityDetails mdetails = new AdModUserResponsibilityDetails();
            mdetails.setUrCode(adUserResponsibility.getUrCode());
            mdetails.setUrResCode(adUserResponsibility.getAdResponsibility().getRsCode());
            mdetails.setUrDateFrom(adUserResponsibility.getUrDateFrom());
            mdetails.setUrDateTo(adUserResponsibility.getUrDateTo());
            mdetails.setUrResponsibilityName(adUserResponsibility.getAdResponsibility().getRsName());

            list.add(mdetails);
        }
        return list;
    }

    public AdUserDetails getAdUsrByUsrCode(Integer userCode, Integer companyCode) {

        Debug.print("AdUserResponsibilityControllerBean getAdUsrByUsrCode");

        try {
            LocalAdUser adUser = adUserHome.findByPrimaryKey(userCode);
            AdUserDetails details = new AdUserDetails();
            details.setUsrCode(adUser.getUsrCode());
            details.setUsrName(adUser.getUsrName());
            details.setUsrDescription(adUser.getUsrDescription());
            details.setUsrPassword(adUser.getUsrPassword());
            details.setUsrPasswordExpirationCode(adUser.getUsrPasswordExpirationCode());
            details.setUsrPasswordExpirationDays(adUser.getUsrPasswordExpirationDays());
            details.setUsrPasswordExpirationAccess(adUser.getUsrPasswordExpirationAccess());
            details.setUsrDateFrom(adUser.getUsrDateFrom());
            details.setUsrDateTo(adUser.getUsrDateTo());
            return details;
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    public void addAdUrEntry(AdUserResponsibilityDetails details, Integer userCode, String responsibilityCode, Integer companyCode) throws GlobalRecordAlreadyExistException {

        Debug.print("AdUserResponsibilityControllerBean addAdUrEntry");

        LocalAdUserResponsibility adUserResponsibility;
        LocalAdUser adUser;
        LocalAdResponsibility adResponsibility;
        try {
            adUserResponsibility = adUserResponsibilityHome.findByRsNameAndUsrCode(responsibilityCode, userCode, companyCode);
            throw new GlobalRecordAlreadyExistException();
        } catch (GlobalRecordAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        try {

            // Create New User Responsibilities

            adUserResponsibility = adUserResponsibilityHome.create(details.getUrDateFrom(), details.getUrDateTo(), companyCode);

            adUser = adUserHome.findByPrimaryKey(userCode);
            adUser.addAdUserResponsibility(adUserResponsibility);

            adResponsibility = adResponsibilityHome.findByRsName(responsibilityCode, companyCode);
            adResponsibility.addAdUserResponsibility(adUserResponsibility);

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateAdUrEntry(AdUserResponsibilityDetails details, Integer userCode, String responsibilityCode, Integer companyCode) throws GlobalRecordAlreadyExistException {

        Debug.print("AdUserResponsibilityControllerBean updateAdUrEntry");

        LocalAdUserResponsibility adUserResponsibility;
        LocalAdUser adUser;
        LocalAdResponsibility adResponsibility;
        try {
            adUserResponsibility = adUserResponsibilityHome.findByRsNameAndUsrCode(responsibilityCode, userCode, companyCode);
            if (adUserResponsibility != null && !adUserResponsibility.getUrCode().equals(details.getUrCode())) {

                throw new GlobalRecordAlreadyExistException();
            }
        } catch (GlobalRecordAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        // Find and Update User Responsibilities
        try {

            adUserResponsibility = adUserResponsibilityHome.findByPrimaryKey(details.getUrCode());

            adUserResponsibility.setUrDateFrom(details.getUrDateFrom());
            adUserResponsibility.setUrDateTo(details.getUrDateTo());

            adUser = adUserHome.findByPrimaryKey(userCode);
            adUser.addAdUserResponsibility(adUserResponsibility);

            adResponsibility = adResponsibilityHome.findByRsName(responsibilityCode, companyCode);
            adResponsibility.addAdUserResponsibility(adUserResponsibility);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteAdUrEntry(Integer userResponsibilityCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException {

        Debug.print("AdUserResponsibilityControllerBean deleteAdUrEntry");

        LocalAdUserResponsibility adUserResponsibility;
        try {
            adUserResponsibility = adUserResponsibilityHome.findByPrimaryKey(userResponsibilityCode);
        } catch (FinderException ex) {

            throw new GlobalRecordAlreadyDeletedException();

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        try {

            em.remove(adUserResponsibility);

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("AdUserResponsibilityControllerBean ejbCreate");
    }
}