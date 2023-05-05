package com.ejb.txn.ad;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.*;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import javax.naming.NamingException;

import com.ejb.dao.ad.*;
import com.ejb.entities.ad.*;
import com.ejb.exception.ad.*;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.*;
import com.util.ad.*;
import com.util.mod.ad.AdModBranchDocumentSequenceAssignmentDetails;
import com.util.mod.ad.AdModDocumentSequenceAssignmentDetails;

@Stateless(name = "AdDocumentSequenceAssignmentControllerEJB")
public class AdDocumentSequenceAssignmentControllerBean extends EJBContextClass implements AdDocumentSequenceAssignmentController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalAdDocumentCategoryHome adDocumentCategoryHome;
    @EJB
    private LocalAdDocumentSequenceHome adDocumentSequenceHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDsaHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalAdResponsibilityHome adResHome;

    public ArrayList getAdDcAll(Integer companyCode) throws AdDCNoDocumentCategoryFoundException {

        Debug.print("AdDocumentSequenceAssignmentControllerBean getAdDcAll");

        ArrayList dcAllList = new ArrayList();
        Collection adDocumentCategories;
        try {
            adDocumentCategories = adDocumentCategoryHome.findDcAll(companyCode);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        if (adDocumentCategories.size() == 0) {
            throw new AdDCNoDocumentCategoryFoundException();
        }
        for (Object documentCategory : adDocumentCategories) {
            LocalAdDocumentCategory adDocumentCategory = (LocalAdDocumentCategory) documentCategory;
            AdDocumentCategoryDetails details = new AdDocumentCategoryDetails(adDocumentCategory.getDcCode(), adDocumentCategory.getDcName(), adDocumentCategory.getDcDescription());
            dcAllList.add(details);
        }
        return dcAllList;
    }

    public ArrayList getAdDsAll(Integer companyCode) throws AdDSNoDocumentSequenceFoundException {

        Debug.print("AdDocumentSequenceAssignmentControllerBean getAdDsAll");

        ArrayList dsAllList = new ArrayList();
        Collection adDocumentSequences;
        GregorianCalendar gcCurrDate = new GregorianCalendar();
        gcCurrDate.set(gcCurrDate.get(Calendar.YEAR), gcCurrDate.get(Calendar.MONTH), gcCurrDate.get(Calendar.DATE), 0, 0, 0);
        gcCurrDate.set(Calendar.MILLISECOND, 0);
        try {
            adDocumentSequences = adDocumentSequenceHome.findDsEnabled(new Date(gcCurrDate.getTime().getTime()), companyCode);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        if (adDocumentSequences.size() == 0) {
            throw new AdDSNoDocumentSequenceFoundException();
        }
        Iterator i = adDocumentSequences.iterator();
        dsAllList.clear();
        while (i.hasNext()) {
            LocalAdDocumentSequence adDocumentSequence = (LocalAdDocumentSequence) i.next();
            AdDocumentSequenceDetails details = new AdDocumentSequenceDetails(adDocumentSequence.getDsCode(), adDocumentSequence.getDsSequenceName(), adDocumentSequence.getDsDateFrom(), adDocumentSequence.getDsDateTo(), adDocumentSequence.getDsNumberingType(), adDocumentSequence.getDsInitialValue());
            dsAllList.add(details);
        }
        return dsAllList;
    }

    public ArrayList getAdDsaAll(Integer companyCode) throws AdDSANoDocumentSequenceAssignmentFoundException {

        Debug.print("AdDocumentSequenceAssignmentControllerBean getAdDsaAll");

        ArrayList dsaAllList = new ArrayList();
        Collection adDocumentSequenceAssignments;
        try {
            adDocumentSequenceAssignments = adDocumentSequenceAssignmentHome.findDsaAll(companyCode);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        if (adDocumentSequenceAssignments.size() == 0) {
            throw new AdDSANoDocumentSequenceAssignmentFoundException();
        }
        for (Object documentSequenceAssignment : adDocumentSequenceAssignments) {
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = (LocalAdDocumentSequenceAssignment) documentSequenceAssignment;
            AdModDocumentSequenceAssignmentDetails details = new AdModDocumentSequenceAssignmentDetails(adDocumentSequenceAssignment.getDsaCode(), adDocumentSequenceAssignment.getDsaSobCode(), adDocumentSequenceAssignment.getDsaNextSequence(), adDocumentSequenceAssignment.getAdDocumentCategory().getDcName(), adDocumentSequenceAssignment.getAdDocumentSequence().getDsSequenceName());
            dsaAllList.add(details);
        }
        return dsaAllList;
    }

    public void updateAdDSASequenceByCode(Integer documentSequenceAssignmentCode, String nextSequenceNumber, Integer companyCode) throws AdDCNoDocumentCategoryFoundException, AdDSNoDocumentSequenceFoundException, AdDCDocumentCategoryAlreadyExistException {

        Debug.print("AdDocumentSequenceAssignmentControllerBean updateAdDSASequenceByCode");

        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment;
        try {

            adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByPrimaryKey(documentSequenceAssignmentCode);


            Debug.print(adDocumentSequenceAssignment.getDsaNextSequence() + " the seq");
            Debug.print("to change" + nextSequenceNumber);


        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());

        }
        try {
            adDocumentSequenceAssignment.setDsaNextSequence(nextSequenceNumber);
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void addAdDsaEntry(AdDocumentSequenceAssignmentDetails details, String documentName, String documentSequenceName, ArrayList branchList, Integer companyCode) throws AdDCNoDocumentCategoryFoundException, AdDSNoDocumentSequenceFoundException, AdDCDocumentCategoryAlreadyExistException {

        Debug.print("AdDocumentSequenceAssignmentControllerBean addAdDsaEntry");

        LocalAdDocumentCategory adDocumentCategory;
        LocalAdDocumentSequence adDocumentSequence;
        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment;
        LocalAdBranchDocumentSequenceAssignment adBranchDsa;
        LocalAdBranch adBranch;
        try {
            adDocumentCategory = adDocumentCategoryHome.findByDcName(documentName, companyCode);
        } catch (FinderException ex) {
            throw new AdDCNoDocumentCategoryFoundException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        try {
            adDocumentSequence = adDocumentSequenceHome.findByDsName(documentSequenceName, companyCode);
        } catch (FinderException ex) {
            throw new AdDSNoDocumentSequenceFoundException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        try {
            adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(1, adDocumentSequence.getDsInitialValue(), companyCode);
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
        if (adDocumentCategory.getAdDocumentSequenceAssignments().size() == 0) {
            try {
                adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
            } catch (Exception e) {
                getSessionContext().setRollbackOnly();
                throw new EJBException(e.getMessage());
            }
        } else {
            getSessionContext().setRollbackOnly();
            throw new AdDCDocumentCategoryAlreadyExistException();
        }
        try {
            adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
        // add branch dsa
        try {
            for (Object o : branchList) {

                AdModBranchDocumentSequenceAssignmentDetails brDsaDetails = (AdModBranchDocumentSequenceAssignmentDetails) o;
                adBranchDsa = adBranchDsaHome.create(brDsaDetails.getBdsNextSequence(), companyCode);

                adDocumentSequenceAssignment.addAdBranchDocumentSequenceAssignments(adBranchDsa);

                adBranch = adBranchHome.findByPrimaryKey(brDsaDetails.getBrCode());
                adBranch.addAdBranchDocumentSequenceAssignments(adBranchDsa);

            }
        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }
    }

    public void updateAdDsaEntry(AdDocumentSequenceAssignmentDetails details, String documentName, String documentSequenceName, String responsibilityName, ArrayList branchList, Integer companyCode) throws AdDCNoDocumentCategoryFoundException, AdDSNoDocumentSequenceFoundException, AdDCDocumentCategoryAlreadyExistException, AdDSADocumentSequenceAssignmentAlreadyDeletedException {

        Debug.print("AdDocumentSequenceAssignmentControllerBean updateAdDsaEntry");

        LocalAdDocumentCategory adDocumentCategory;
        LocalAdDocumentSequence adDocumentSequence;
        LocalAdBranchDocumentSequenceAssignment adBranchDsa;
        LocalAdBranch adBranch;
        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment;
        try {
            adDocumentCategory = adDocumentCategoryHome.findByDcName(documentName, companyCode);
        } catch (FinderException ex) {
            throw new AdDCNoDocumentCategoryFoundException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        try {
            adDocumentSequence = adDocumentSequenceHome.findByDsName(documentSequenceName, companyCode);
        } catch (FinderException ex) {
            throw new AdDSNoDocumentSequenceFoundException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        try {
            adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByPrimaryKey(details.getDsaCode());
        } catch (FinderException ex) {
            throw new AdDSADocumentSequenceAssignmentAlreadyDeletedException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        try {
            adDocumentSequenceAssignment.setDsaNextSequence(details.getDsaNextSequence());
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
        Collection adDocumentSequenceAssignments = adDocumentCategory.getAdDocumentSequenceAssignments();
        if (adDocumentSequenceAssignments.size() == 0) {
            try {
                adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
            } catch (Exception ex) {
                getSessionContext().setRollbackOnly();
                throw new EJBException(ex.getMessage());
            }
        } else if (adDocumentCategory.getAdDocumentSequenceAssignments().size() > 0) {
            Iterator i = adDocumentSequenceAssignments.iterator();
            boolean dsaFound = false;
            while (i.hasNext()) {
                LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment2 = (LocalAdDocumentSequenceAssignment) i.next();
                if (adDocumentSequenceAssignment2.getAdDocumentCategory().getDcCode().equals(adDocumentSequenceAssignment.getAdDocumentCategory().getDcCode())) {
                    try {
                        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
                        dsaFound = true;
                    } catch (Exception ex) {
                        getSessionContext().setRollbackOnly();
                        throw new EJBException(ex.getMessage());
                    }
                }
            }
            if (!dsaFound) {
                getSessionContext().setRollbackOnly();
                throw new AdDCDocumentCategoryAlreadyExistException();
            }
        }
        try {
            adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
        // update branch dsa
        try {

            Collection adBranchDSAs = adBranchDsaHome.findBdsByDsaCodeAndRsName(adDocumentSequenceAssignment.getDsaCode(), responsibilityName, companyCode);

            //remove all adBranchDSA lines
            for (Object adBranchDSA : adBranchDSAs) {

                adBranchDsa = (LocalAdBranchDocumentSequenceAssignment) adBranchDSA;

                adDocumentSequenceAssignment.dropAdBranchDocumentSequenceAssignments(adBranchDsa);

                adBranch = adBranchHome.findByPrimaryKey(adBranchDsa.getAdBranch().getBrCode());
                adBranch.dropAdBranchDocumentSequenceAssignments(adBranchDsa);
//	      	adBranchDsa.entityRemove();
                em.remove(adBranchDsa);
            }

            //add adBranchDSA lines

            for (Object o : branchList) {

                AdModBranchDocumentSequenceAssignmentDetails brDsaDetails = (AdModBranchDocumentSequenceAssignmentDetails) o;
                adBranchDsa = adBranchDsaHome.create(brDsaDetails.getBdsNextSequence(), companyCode);

                adDocumentSequenceAssignment.addAdBranchDocumentSequenceAssignments(adBranchDsa);

                adBranch = adBranchHome.findByPrimaryKey(brDsaDetails.getBrCode());
                adBranch.addAdBranchDocumentSequenceAssignments(adBranchDsa);

            }

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }
    }

    public void deleteAdDsaEntry(Integer documentSequenceAssignmentCode, Integer companyCode) throws AdDSADocumentSequenceAssignmentAlreadyAssignedException, AdDSADocumentSequenceAssignmentAlreadyDeletedException {

        Debug.print("AdDocumentSequenceAssignmentControllerBean deleteAdDsaEntry");

        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment;
        try {
            adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByPrimaryKey(documentSequenceAssignmentCode);
        } catch (FinderException ex) {
            throw new AdDSADocumentSequenceAssignmentAlreadyDeletedException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        if (hasRelation(adDocumentSequenceAssignment, companyCode)) {
            throw new AdDSADocumentSequenceAssignmentAlreadyAssignedException();
        } else {
            try {
//	    adDocumentSequenceAssignment.entityRemove();
                em.remove(adDocumentSequenceAssignment);
            } catch (Exception ex) {
                getSessionContext().setRollbackOnly();
                throw new EJBException(ex.getMessage());
            }
        }
    }

    public ArrayList getAdBrResAll(Integer RS_CODE, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdDocumentSequenceAssignmentControllerBean getAdBrResAll");

        LocalAdBranchResponsibility adBranchResponsibility;
        LocalAdBranch adBranch;
        Collection adBranchResponsibilities = null;
        ArrayList list = new ArrayList();
        try {
            adBranchResponsibilities = adBranchResponsibilityHome.findByAdResponsibility(RS_CODE, companyCode);
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

    public ArrayList getAdBrDsaAll(Integer documentSequenceAssignmentCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdDocumentSequenceAssignmentControllerBean getAdBrDsaAll");

        LocalAdBranchDocumentSequenceAssignment adBranchDsa;
        LocalAdBranch adBranch;
        Collection adBranchDsas = null;
        ArrayList list = new ArrayList();
        try {

            adBranchDsas = adBranchDsaHome.findBdsByDsaCode(documentSequenceAssignmentCode, companyCode);

        } catch (FinderException ex) {
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        if (adBranchDsas.isEmpty()) {

            throw new GlobalNoRecordFoundException();

        }
        try {

            for (Object branchDsa : adBranchDsas) {

                adBranchDsa = (LocalAdBranchDocumentSequenceAssignment) branchDsa;

                adBranch = adBranchHome.findByPrimaryKey(adBranchDsa.getAdBranch().getBrCode());

                AdModBranchDocumentSequenceAssignmentDetails details = new AdModBranchDocumentSequenceAssignmentDetails();

                details.setBrCode(adBranch.getBrCode());
                Debug.print("adBranchDsa.getBdsNextSequence()=" + adBranchDsa.getBdsNextSequence());
                details.setBdsNextSequence(adBranchDsa.getBdsNextSequence());

                list.add(details);

            }

        } catch (FinderException ex) {
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        return list;
    }

    public AdResponsibilityDetails getAdRsByRsCode(Integer RS_CODE) throws GlobalNoRecordFoundException {

        Debug.print("AdDocumentSequenceAssignmentControllerBean getAdRsByRsCode");

        LocalAdResponsibility adRes = null;
        try {
            adResHome = (LocalAdResponsibilityHome) EJBHomeFactory.lookUpLocalHome(LocalAdResponsibilityHome.JNDI_NAME, LocalAdResponsibilityHome.class);
        } catch (NamingException ex) {
        }
        try {
            adRes = adResHome.findByPrimaryKey(RS_CODE);
        } catch (FinderException ex) {
        }
        AdResponsibilityDetails details = new AdResponsibilityDetails();
        details.setRsName(adRes.getRsName());
        return details;
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {
        Debug.print("AdDocumentSequenceAssignmentControllerBean ejbCreate");
    }

    //  methods
    boolean hasRelation(LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment, Integer companyCode) {

        Debug.print("AdDocumentSequenceAssignmentControllerBean hasRelation");

        Collection glJournals = null;
        try {
            glJournals = adDocumentSequenceAssignment.getGlJournals();
        } catch (Exception ex) {
        }
        return glJournals.size() != 0;
    }
}