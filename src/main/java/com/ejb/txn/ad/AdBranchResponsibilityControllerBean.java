/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class AdBranchResponsibilityControllerBean
 * @created October 21, 2005, 11:26 AM
 * @author Franco Antonio R. Roig
 */
package com.ejb.txn.ad;

import java.util.*;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.entities.ad.*;
import com.ejb.exception.global.*;
import com.util.*;
import com.util.ad.AdBranchResponsibilityDetails;

@Stateless(name = "AdBranchResponsibilityControllerEJB")
public class AdBranchResponsibilityControllerBean extends EJBContextClass implements AdBranchResponsibilityController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    LocalAdBranchHome adBranchHome;

    public ArrayList getAdBranchResponsibilityAll(int resCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdBranchResponsibilityController getAdBrnchRspnsbltyAll");

        LocalAdBranchResponsibility adBrRes;
        Collection adBranches = null;
        ArrayList list = new ArrayList();
        try {

            adBranches = adBranchResponsibilityHome.findByAdResponsibility(resCode, companyCode);

        } catch (FinderException ex) {
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        if (adBranches.isEmpty()) {
            throw new GlobalNoRecordFoundException();
        }
        for (Object adBranch : adBranches) {
            adBrRes = (LocalAdBranchResponsibility) adBranch;
            AdBranchResponsibilityDetails details = new AdBranchResponsibilityDetails();

            details.setBrsCode(adBrRes.getAdBranch().getBrCode());
            details.setBrsAdCompany(adBrRes.getBrsAdCompany());
            details.setBrCode(adBrRes.getAdBranch().getBrCode());
            details.setBrBranchCode(adBrRes.getAdBranch().getBrBranchCode());
            details.setBrName(adBrRes.getAdBranch().getBrName());
            details.setBrHeadQuarter(adBrRes.getAdBranch().getBrHeadQuarter());
            list.add(details);
        }
        return list;
    }

    public void addAdBranchResponsibility(AdBranchResponsibilityDetails details, Integer companyCode) throws GlobalRecordAlreadyExistException {

        Debug.print("AdBranchResponsibilityControllerBean addAdBranchResponsibility");

        LocalAdBranchResponsibility adBrRes = null;
        try {
            adBrRes = adBranchResponsibilityHome.findByPrimaryKey(details.getBrsCode());
            throw new GlobalRecordAlreadyExistException();
        } catch (GlobalRecordAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        try {
            // create new branch responsibility
            adBrRes = adBranchResponsibilityHome.create(details.getBrsCode(), details.getBrsAdCompany());
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateAdBranchResponsibility(AdBranchResponsibilityDetails details, Integer companyCode) throws GlobalRecordAlreadyExistException {

        Debug.print("AdBranchResponsibilityControllerBean addAdBranchResponsibility");

        LocalAdBranchResponsibility adBrRes;
        try {

            LocalAdBranchResponsibility adExistingBrRes = adBranchResponsibilityHome.findByPrimaryKey(details.getBrsCode());

            if (!adExistingBrRes.getBrsCode().equals(details.getBrsCode())) {

                throw new GlobalRecordAlreadyExistException();
            }

        } catch (GlobalRecordAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        try {

            // find and update branch responsibility

            adBrRes = adBranchResponsibilityHome.findByPrimaryKey(details.getBrsCode());

            adBrRes.setBrsAdCompany(details.getBrsAdCompany());

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public LocalAdBranch getAdBranchByBrCode(Integer branchCode) throws GlobalNoRecordFoundException {

        Debug.print("AdBranchResponsibilityControllerBean getAdBranchByBrCode");

        try {

            return adBranchHome.findByPrimaryKey(branchCode);

        } catch (FinderException ex) {

            throw new GlobalNoRecordFoundException();

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteAdBranchResponsibility(Integer BRS_CODE, Integer companyCode) throws GlobalRecordAlreadyDeletedException {

        Debug.print("AdBranchResponsibilityControllerBean deleteAdBranchResponsibility");

        LocalAdBranchResponsibility adBrRes;
        try {

            adBrRes = adBranchResponsibilityHome.findByPrimaryKey(BRS_CODE);

        } catch (FinderException ex) {

            throw new GlobalRecordAlreadyDeletedException();
        }
        try {

            em.remove(adBrRes);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("AdBranchResponsibilityControllerBean ejbCreate");
    }
}