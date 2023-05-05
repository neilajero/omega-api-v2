/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArSalespersonControllerBean
 * @created January 31, 2006, 3:20 PM
 * @author Franco Antonio R. Roig
 */
package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.dao.ad.LocalAdBranchSalespersonHome;
import com.ejb.dao.ad.LocalAdResponsibilityHome;
import com.ejb.dao.ar.LocalArSalespersonHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdBranchSalesperson;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdResponsibility;
import com.ejb.entities.ar.LocalArSalesperson;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.*;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdResponsibilityDetails;
import com.util.ar.ArSalespersonDetails;

import jakarta.ejb.*;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.Collection;

@Stateless(name = "ArSalespersonControllerEJB")
public class ArSalespersonControllerBean extends EJBContextClass implements ArSalespersonController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdBranchSalespersonHome adBranchSalespersonHome;
    @EJB
    private LocalAdResponsibilityHome adResHome;
    @EJB
    private LocalArSalespersonHome arSalespersonHome;


    public ArrayList getAdBrAll(Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArSalespersonControllerBean getAdBrAll");
        LocalAdBranch adBranch = null;

        Collection adBranches = null;

        ArrayList list = new ArrayList();
        try {

            adBranches = adBranchHome.findBrAll(AD_CMPNY);

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

            details.setBrBranchCode(adBranch.getBrBranchCode());
            details.setBrCode(adBranch.getBrCode());
            details.setBrName(adBranch.getBrName());

            list.add(details);
        }

        return list;
    }

    public ArrayList getAdBrSlpAll(Integer BSLP_CODE, String RS_NM, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArSalespersonControllerBean getAdBrSlpAll");
        LocalAdBranchSalesperson adBranchSalesperson = null;
        LocalAdBranch adBranch = null;
        Collection adBranchSalespersons = null;
        ArrayList branchList = new ArrayList();

        try {

            adBranchSalespersons = adBranchSalespersonHome.findBSLPBySLPCodeAndRsName(BSLP_CODE, RS_NM, AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (adBranchSalespersons.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }

        try {

            for (Object branchSalesperson : adBranchSalespersons) {

                adBranchSalesperson = (LocalAdBranchSalesperson) branchSalesperson;

                adBranch = adBranchHome.findByPrimaryKey(adBranchSalesperson.getAdBranch().getBrCode());

                AdBranchDetails details = new AdBranchDetails();
                details.setBrCode(adBranch.getBrCode());
                details.setBrName(adBranch.getBrName());

                branchList.add(details);
            }

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        return branchList;
    }

    public AdResponsibilityDetails getAdRsByRsCode(Integer RS_CODE) throws GlobalNoRecordFoundException {
        LocalAdResponsibility adRes = null;
        try {
            adRes = adResHome.findByPrimaryKey(RS_CODE);
        } catch (FinderException ex) {
        }
        AdResponsibilityDetails details = new AdResponsibilityDetails();
        details.setRsName(adRes.getRsName());
        return details;
    }

    public void addArSlpEntry(ArSalespersonDetails details, ArrayList branchList, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException {

        Debug.print("ArSalespersonControllerBean addArSlpEntry");


        LocalAdBranchSalesperson adBranchSalesperson = null;
        LocalAdBranch adBranch = null;

        ArrayList list = new ArrayList();

        try {

            LocalArSalesperson arSalesperson = null;

            try {

                arSalesperson = arSalespersonHome.findBySlpSalespersonCode(details.getSlpSalespersonCode(), AD_CMPNY);

                throw new GlobalRecordAlreadyExistException();

            } catch (FinderException ex) {

            }

            // create new salesperson

            arSalesperson = arSalespersonHome.create(details.getSlpSalespersonCode(), details.getSlpName(), details.getSlpEntryDate(), details.getSlpAddress(), details.getSlpPhone(), details.getSlpMobilePhone(), details.getSlpEmail(), AD_CMPNY);

            // create new Branch Salesperson

            for (Object o : branchList) {

                AdBranchDetails brDetails = (AdBranchDetails) o;

                adBranchSalesperson = adBranchSalespersonHome.create(AD_CMPNY);

                arSalesperson.addAdBranchSalesperson(adBranchSalesperson);
                adBranch = adBranchHome.findByPrimaryKey(brDetails.getBrCode());
                adBranch.addAdBranchSalesperson(adBranchSalesperson);
            }

        } catch (GlobalRecordAlreadyExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateArSlpEntry(ArSalespersonDetails details, String RS_NM, ArrayList branchList, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException {
        Debug.print("ArSalespersonControllerBean updateArSlpEntry");
        LocalAdBranchSalesperson adBranchSalesperson = null;
        LocalAdBranch adBranch = null;
        try {

            LocalArSalesperson arSalesperson = null;

            try {

                LocalArSalesperson arExistingSalesperson = arSalespersonHome.findBySlpSalespersonCode(details.getSlpSalespersonCode(), AD_CMPNY);

                if (!arExistingSalesperson.getSlpCode().equals(details.getSlpCode())) {

                    throw new GlobalRecordAlreadyExistException();
                }

            } catch (FinderException ex) {

            }

            // find and update standard memo line

            arSalesperson = arSalespersonHome.findByPrimaryKey(details.getSlpCode());

            arSalesperson.setSlpSalespersonCode(details.getSlpSalespersonCode());
            arSalesperson.setSlpName(details.getSlpName());
            arSalesperson.setSlpEntryDate(details.getSlpEntryDate());
            arSalesperson.setSlpAddress(details.getSlpAddress());
            arSalesperson.setSlpPhone(details.getSlpPhone());
            arSalesperson.setSlpMobilePhone(details.getSlpMobilePhone());
            arSalesperson.setSlpEmail(details.getSlpEmail());

            Collection adBranchSalespersons = adBranchSalespersonHome.findBSLPBySLPCodeAndRsName(arSalesperson.getSlpCode(), RS_NM, AD_CMPNY);

            // remove all adBranchSalesperson lines
            for (Object branchSalesperson : adBranchSalespersons) {

                adBranchSalesperson = (LocalAdBranchSalesperson) branchSalesperson;

                arSalesperson.dropAdBranchSalesperson(adBranchSalesperson);

                adBranch = adBranchHome.findByPrimaryKey(adBranchSalesperson.getAdBranch().getBrCode());
                adBranch.dropAdBranchSalesperson(adBranchSalesperson);
                //            	adBranchSalesperson.entityRemove();
                em.remove(adBranchSalesperson);
            }

            // create new Branch Salesperson

            for (Object o : branchList) {

                AdBranchDetails brDetails = (AdBranchDetails) o;

                adBranchSalesperson = adBranchSalespersonHome.create(AD_CMPNY);

                arSalesperson.addAdBranchSalesperson(adBranchSalesperson);
                adBranch = adBranchHome.findByPrimaryKey(brDetails.getBrCode());
                adBranch.addAdBranchSalesperson(adBranchSalesperson);
            }

        } catch (GlobalRecordAlreadyExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteArSlpEntry(Integer SLP_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException {
        Debug.print("ArSalespersonControllerBean deleteArSlpEntry");
        try {

            LocalArSalesperson arSalesperson = null;

            try {

                arSalesperson = arSalespersonHome.findByPrimaryKey(SLP_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            if (!arSalesperson.getArInvoices().isEmpty() || !arSalesperson.getArReceipts().isEmpty() || !arSalesperson.getArCustomers().isEmpty() || !arSalesperson.getArSalesOrders().isEmpty()) {

                throw new GlobalRecordAlreadyAssignedException();
            }

            //	        arSalesperson.entityRemove();
            em.remove(arSalesperson);

        } catch (GlobalRecordAlreadyDeletedException | GlobalRecordAlreadyAssignedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {
        Debug.print("ArStandardMemoLineControllerBean getGlFcPrecisionUnit");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void ejbCreate() throws CreateException {

        Debug.print("ArStandardMemoLineControllerBean ejbCreate");
    }

}