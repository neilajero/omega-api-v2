package com.ejb.txn.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdApplication;
import com.ejb.dao.ad.LocalAdApplicationHome;
import com.ejb.entities.ad.LocalAdDocumentSequence;
import com.ejb.dao.ad.LocalAdDocumentSequenceHome;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;


import java.util.ArrayList;
import java.util.Collection;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;

import com.ejb.exception.ad.AdAPPNoApplicationFoundException;
import com.ejb.exception.ad.AdDSDocumentSequenceAlreadyAssignedException;
import com.ejb.exception.ad.AdDSDocumentSequenceAlreadyDeletedException;
import com.ejb.exception.ad.AdDSDocumentSequenceAlreadyExistException;
import com.ejb.exception.ad.AdDSNoDocumentSequenceFoundException;
import com.util.EJBContextClass;
import com.util.ad.AdApplicationDetails;
import com.util.ad.AdDocumentSequenceDetails;
import com.util.mod.ad.AdModDocumentSequenceDetails;
import com.util.Debug;

@Stateless(name = "AdDocumentSequenceControllerEJB")
public class AdDocumentSequenceControllerBean extends EJBContextClass implements AdDocumentSequenceController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    public LocalAdApplicationHome adApplicationHome;
    @EJB
    public LocalAdDocumentSequenceHome adDocumentSequenceHome;

    public ArrayList getAdAppAll(Integer companyCode) throws AdAPPNoApplicationFoundException {

        Debug.print("AdDocumentSequenceControllerBean getAdAppAll");

        ArrayList appAllList = new ArrayList();
        Collection adApplications;
        try {
            adApplications = adApplicationHome.findAppAll(companyCode);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        if (adApplications.isEmpty()) {
            throw new AdAPPNoApplicationFoundException();
        }
        for (Object application : adApplications) {
            LocalAdApplication adApplication = (LocalAdApplication) application;
            AdApplicationDetails details = new AdApplicationDetails(adApplication.getAppCode(), adApplication.getAppName(), adApplication.getAppDescription());
            appAllList.add(details);
        }
        return appAllList;
    }

    public ArrayList getAdDsAll(Integer companyCode) throws AdDSNoDocumentSequenceFoundException {

        Debug.print("AdDocumentSequenceControllerBean getAdDsAll");

        ArrayList dsAllList = new ArrayList();
        Collection adDocumentSequences;
        try {
            adDocumentSequences = adDocumentSequenceHome.findDsAll(companyCode);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        if (adDocumentSequences.isEmpty()) {
            throw new AdDSNoDocumentSequenceFoundException();
        }
        for (Object documentSequence : adDocumentSequences) {
            LocalAdDocumentSequence adDocumentSequence = (LocalAdDocumentSequence) documentSequence;
            AdModDocumentSequenceDetails details = new AdModDocumentSequenceDetails(adDocumentSequence.getDsCode(), adDocumentSequence.getDsSequenceName(), adDocumentSequence.getDsDateFrom(), adDocumentSequence.getDsDateTo(), adDocumentSequence.getDsNumberingType(), adDocumentSequence.getDsInitialValue(), adDocumentSequence.getAdApplication().getAppName());
            dsAllList.add(details);
        }
        return dsAllList;
    }

    public void addAdDsEntry(AdDocumentSequenceDetails details, String applicationName, Integer companyCode) throws AdAPPNoApplicationFoundException, AdDSDocumentSequenceAlreadyExistException {

        Debug.print("AdDocumentSequenceControllerBean addAdDsEntry");

        LocalAdApplication adApplication;
        LocalAdDocumentSequence adDocumentSequence;
        try {
            adApplication = adApplicationHome.findByAppName(applicationName, companyCode);
        } catch (FinderException ex) {
            throw new AdAPPNoApplicationFoundException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        try {

            adDocumentSequenceHome.findByDsName(details.getDsSequenceName(), companyCode);

            getSessionContext().setRollbackOnly();
            throw new AdDSDocumentSequenceAlreadyExistException();

        } catch (FinderException ex) {
        }
        try {
            adDocumentSequence = adDocumentSequenceHome.create(details.getDsSequenceName(), details.getDsDateFrom(), details.getDsDateTo(), details.getDsNumberingType(), details.getDsInitialValue(), companyCode);
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
        try {
            adApplication.addAdDocumentSequence(adDocumentSequence);
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateAdDsEntry(AdDocumentSequenceDetails details, String applicationName, Integer companyCode) throws AdAPPNoApplicationFoundException, AdDSDocumentSequenceAlreadyExistException, AdDSDocumentSequenceAlreadyAssignedException, AdDSDocumentSequenceAlreadyDeletedException {

        Debug.print("AdDocumentSequenceControllerBean updateAdDsEntry");

        LocalAdApplication adApplication;
        try {
            adApplication = adApplicationHome.findByAppName(applicationName, companyCode);
        } catch (FinderException ex) {
            throw new AdAPPNoApplicationFoundException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        LocalAdDocumentSequence adDocumentSequence;
        try {
            adDocumentSequence = adDocumentSequenceHome.findByPrimaryKey(details.getDsCode());
        } catch (FinderException ex) {
            throw new AdDSDocumentSequenceAlreadyDeletedException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        if (hasRelation(adDocumentSequence)) {
            throw new AdDSDocumentSequenceAlreadyAssignedException();
        } else {

            LocalAdDocumentSequence adDocumentSequence2 = null;

            try {
                adDocumentSequence2 = adDocumentSequenceHome.findByDsName(details.getDsSequenceName(), companyCode);
            } catch (FinderException ex) {
                try {
                    adDocumentSequence.setDsSequenceName(details.getDsSequenceName());
                    adDocumentSequence.setDsDateFrom(details.getDsDateFrom());
                    adDocumentSequence.setDsDateTo(details.getDsDateTo());
                    adDocumentSequence.setDsNumberingType(details.getDsNumberingType());
                    adDocumentSequence.setDsInitialValue(details.getDsInitialValue());
                } catch (Exception e) {
                    getSessionContext().setRollbackOnly();
                    throw new EJBException(ex.getMessage());
                }

                try {
                    adApplication.addAdDocumentSequence(adDocumentSequence);
                } catch (Exception e) {
                    getSessionContext().setRollbackOnly();
                    throw new EJBException(ex.getMessage());
                }
            } catch (Exception ex) {
                throw new EJBException(ex.getMessage());
            }

            if (adDocumentSequence2 != null && !adDocumentSequence.getDsCode().equals(adDocumentSequence2.getDsCode())) {
                getSessionContext().setRollbackOnly();
                throw new AdDSDocumentSequenceAlreadyExistException();
            } else if (adDocumentSequence2 != null && adDocumentSequence.getDsCode().equals(adDocumentSequence2.getDsCode())) {
                try {
                    adDocumentSequence.setDsDateFrom(details.getDsDateFrom());
                    adDocumentSequence.setDsDateTo(details.getDsDateTo());
                    adDocumentSequence.setDsNumberingType(details.getDsNumberingType());
                    adDocumentSequence.setDsInitialValue(details.getDsInitialValue());
                } catch (Exception ex) {
                    getSessionContext().setRollbackOnly();
                    throw new EJBException(ex.getMessage());
                }

                try {
                    adApplication.addAdDocumentSequence(adDocumentSequence);
                } catch (Exception ex) {
                    getSessionContext().setRollbackOnly();
                    throw new EJBException(ex.getMessage());
                }
            }
        }
    }

    public void deleteAdDsEntry(Integer documentSequenceCode, Integer companyCode) throws AdDSDocumentSequenceAlreadyAssignedException, AdDSDocumentSequenceAlreadyDeletedException {

        Debug.print("AdDocumentSequenceControllerBean deleteAdDEntry");

        LocalAdDocumentSequence adDocumentSequence;
        try {
            adDocumentSequence = adDocumentSequenceHome.findByPrimaryKey(documentSequenceCode);
        } catch (FinderException ex) {
            throw new AdDSDocumentSequenceAlreadyDeletedException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        if (hasRelation(adDocumentSequence)) {
            throw new AdDSDocumentSequenceAlreadyAssignedException();
        } else {
            try {
//	    adDocumentSequence.entityRemove();
                em.remove(adDocumentSequence);
            } catch (Exception ex) {
                getSessionContext().setRollbackOnly();
                throw new EJBException(ex.getMessage());
            }
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {
        Debug.print("AdDocumentSequenceControllerBean ejbCreate");
    }

    // private methods
    private boolean hasRelation(LocalAdDocumentSequence adDocumentSequence) {

        Debug.print("AdDocumentSequenceControllerBean hasRelation");

        Collection adDocumentSequenceAssignments = null;
        try {
            adDocumentSequenceAssignments = adDocumentSequence.getAdDocumentSequenceAssignments();
        } catch (Exception ex) {
        }
        return !adDocumentSequenceAssignments.isEmpty();
    }
}