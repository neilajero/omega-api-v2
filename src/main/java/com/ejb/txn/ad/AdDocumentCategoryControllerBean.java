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
import com.util.ad.AdDocumentCategoryDetails;
import com.util.mod.ad.AdModDocumentCategoryDetails;

@Stateless(name = "AdDocumentCategoryControllerEJB")
public class AdDocumentCategoryControllerBean extends EJBContextClass implements AdDocumentCategoryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalAdApplicationHome adApplicationHome;
    @EJB
    private LocalAdDocumentCategoryHome adDocumentCategoryHome;

    public ArrayList getAdAppAll(Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdDocumentCategoryControllerBean getAdAppAll");

        Collection adApplications = null;
        LocalAdApplication adApplication;
        ArrayList list = new ArrayList();
        try {

            adApplications = adApplicationHome.findAppAll(companyCode);

        } catch (FinderException ex) {
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        if (adApplications.isEmpty()) {

            throw new GlobalNoRecordFoundException();

        }
        for (Object application : adApplications) {

            adApplication = (LocalAdApplication) application;

            list.add(adApplication.getAppName());

        }
        return list;
    }

    public ArrayList getAdDcAll(Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdDocumentCategoryControllerBean getAdDcAll");

        LocalAdDocumentCategory adDocumentCategory;
        Collection adDocumentCategories = null;
        ArrayList list = new ArrayList();
        try {

            adDocumentCategories = adDocumentCategoryHome.findDcAll(companyCode);

        } catch (FinderException ex) {
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        if (adDocumentCategories.isEmpty()) {

            throw new GlobalNoRecordFoundException();

        }
        for (Object documentCategory : adDocumentCategories) {

            adDocumentCategory = (LocalAdDocumentCategory) documentCategory;

            AdModDocumentCategoryDetails mdetails = new AdModDocumentCategoryDetails(adDocumentCategory.getDcCode(), adDocumentCategory.getDcName(), adDocumentCategory.getDcDescription(), adDocumentCategory.getAdApplication().getAppName());

            list.add(mdetails);
        }
        return list;
    }

    public void addAdDcEntry(AdDocumentCategoryDetails details, String applicationName, Integer companyCode) throws GlobalRecordAlreadyExistException {

        Debug.print("AdDocumentCategoryControllerBean addAdDcEntry");

        LocalAdDocumentCategory adDocumentCategory;
        LocalAdApplication adApplication;
        try {

            adDocumentCategory = adDocumentCategoryHome.findByDcName(details.getDcName(), companyCode);

            throw new GlobalRecordAlreadyExistException();

        } catch (GlobalRecordAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }
        try {

            // create new document category

            adDocumentCategory = adDocumentCategoryHome.create(details.getDcCode(), details.getDcName(), details.getDcDescription(), companyCode);

            adApplication = adApplicationHome.findByAppName(applicationName, companyCode);
            adApplication.addAdDocumentCategory(adDocumentCategory);


        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }
    }

    public void updateAdDcEntry(AdDocumentCategoryDetails details, String applicationName, Integer companyCode) throws GlobalRecordAlreadyExistException {

        Debug.print("AdDocumentCategoryControllerBean updateAdDcEntry");

        LocalAdDocumentCategory adDocumentCategory;
        LocalAdApplication adApplication;
        try {

            LocalAdDocumentCategory adExistingDocumentCategory = adDocumentCategoryHome.findByDcName(details.getDcName(), companyCode);

            if (!adExistingDocumentCategory.getDcCode().equals(details.getDcCode())) {

                throw new GlobalRecordAlreadyExistException();

            }

        } catch (GlobalRecordAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }
        try {

            // find and update document category

            adDocumentCategory = adDocumentCategoryHome.findByPrimaryKey(details.getDcCode());

            adDocumentCategory.setDcName(details.getDcName());
            adDocumentCategory.setDcDescription(details.getDcDescription());

            adApplication = adApplicationHome.findByAppName(applicationName, companyCode);
            adApplication.addAdDocumentCategory(adDocumentCategory);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }
    }

    public void deleteAdDcEntry(Integer documentCategoryName, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException {

        Debug.print("AdDocumentCategoryControllerBean deleteAdDcEntry");

        LocalAdDocumentCategory adDocumentCategory;
        try {

            adDocumentCategory = adDocumentCategoryHome.findByPrimaryKey(documentCategoryName);

        } catch (FinderException ex) {

            throw new GlobalRecordAlreadyDeletedException();

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }
        try {

            if (!adDocumentCategory.getAdDocumentSequenceAssignments().isEmpty()) {

                throw new GlobalRecordAlreadyAssignedException();

            }

        } catch (GlobalRecordAlreadyAssignedException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }
        try {

//	        adDocumentCategory.entityRemove();
            em.remove(adDocumentCategory);

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();

            throw new EJBException(ex.getMessage());

        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("AdDocumentCategoryControllerBean ejbCreate");

    }
}