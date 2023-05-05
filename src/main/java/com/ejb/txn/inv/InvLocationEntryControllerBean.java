/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class InvLocationEntryControllerBean
 * @created June 01, 2004, 6:16 PM
 * @author Enrico C. Yap
 **/

package com.ejb.txn.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.dao.ad.LocalAdBranchItemLocationHome;
import com.ejb.dao.inv.LocalInvItemLocationHome;
import com.ejb.dao.inv.LocalInvLocationHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdBranchItemLocation;
import com.ejb.entities.inv.LocalInvItemLocation;
import com.ejb.entities.inv.LocalInvLocation;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.exception.inv.InvLocFixedAssetException;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.inv.InvLocationDetails;

import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

@Stateless(name = "InvLocationEntryControllerEJB")
public class InvLocationEntryControllerBean extends EJBContextClass implements InvLocationEntryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;

    public ArrayList getAllInvLocName(Integer companyCode) {

        Debug.print("InvLocationEntryControllerBean getAllInvLocations");

        ArrayList list = new ArrayList();

        try {

            Collection invLocations = invLocationHome.findLocAll(companyCode);
            for (Object location : invLocations) {
                LocalInvLocation invLocation = (LocalInvLocation) location;
                list.add(invLocation.getLocName());
            }
            return list;
        }
        catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdLvInvLocationBranchAll(Integer companyCode) {

        Debug.print("InvLocationEntryControllerBean getAdLvInvLocationTypeAll");

        ArrayList list = new ArrayList();

        try {
            Collection adbranchs = adBranchHome.findBrAll(companyCode);
            for (Object adbranch : adbranchs) {
                LocalAdBranch adBranch = (LocalAdBranch) adbranch;
                list.add(adBranch.getBrBranchCode());
            }
            return list;
        }
        catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public void saveInvLocEntry(InvLocationDetails details, Integer companyCode)
            throws GlobalRecordAlreadyExistException, InvLocFixedAssetException {

        Debug.print("InvLocationEntryControllerBean saveInvLocEntry");

        LocalAdBranchItemLocation adBranchItemLocation;
        LocalInvLocation invLocation;

        try {

            // validate if customer already exists
            try {
                invLocation = invLocationHome.findByLocName(details.getLocName(), companyCode);
                if (details.getLocCode() == null || details.getLocCode() != null && !invLocation.getLocCode().equals(details.getLocCode())) {
                    throw new GlobalRecordAlreadyExistException();
                }
            }
            catch (GlobalRecordAlreadyExistException ex) {
                throw ex;
            }
            catch (FinderException ex) {
            }

            // create new customer
            if (details.getLocCode() == null) {
                if (details.getLocLvType().trim().equalsIgnoreCase("FIXED ASSETS")
                        && (details.getLocDepartment().trim() == "" && details.getLocBranch().trim() == "")) {
                    throw new InvLocFixedAssetException();
                }

                invLocation = invLocationHome.create(details.getLocName(), details.getLocDescription(),
                        details.getLocLvType(), details.getLocAddress(), details.getLocContactPerson(),
                        details.getLocContactNumber(), details.getLocEmailAddress(), details.getLocBranch(),
                        details.getLocDepartment(), details.getLocPosition(), details.getLocDateHired(),
                        details.getLocEmploymentStatus(), companyCode);

            } else {
                // update item
                if (details.getLocLvType().trim().equalsIgnoreCase("FIXED ASSETS")
                        && (details.getLocDepartment().trim() == "" && details.getLocBranch().trim() == "")) {
                    throw new InvLocFixedAssetException();
                }
                invLocation = invLocationHome.findByPrimaryKey(details.getLocCode());
                invLocation.setLocName(details.getLocName());
                invLocation.setLocDescription(details.getLocDescription());
                invLocation.setLocLvType(details.getLocLvType());
                invLocation.setLocAddress(details.getLocAddress());
                invLocation.setLocContactPerson(details.getLocContactPerson());
                invLocation.setLocContactNumber(details.getLocContactNumber());
                invLocation.setLocEmailAddress(details.getLocEmailAddress());
                invLocation.setLocDepartment(details.getLocDepartment());
                invLocation.setLocBranch(details.getLocBranch());
                invLocation.setLocPosition(details.getLocPosition());
                invLocation.setLocDateHired(details.getLocDateHired());
                invLocation.setLocEmploymentStatus(details.getLocEmploymentStatus());

                try {
                    LocalInvItemLocation invItemLocation;

                    Collection invItemLocations = invItemLocationHome.findByLocName(details.getLocName(), companyCode);

                    for (Object itemLocation : invItemLocations) {
                        invItemLocation = (LocalInvItemLocation) itemLocation;
                        Collection adBranchItemLocations = adBranchItemLocationHome.findByInvIlAll(invItemLocation.getIlCode(), companyCode);
                        for (Object branchItemLocation : adBranchItemLocations) {
                            adBranchItemLocation = (LocalAdBranchItemLocation) branchItemLocation;
                            if (adBranchItemLocation.getBilLocationDownloadStatus() == 'N') {
                                adBranchItemLocation.setBilLocationDownloadStatus('N');
                            } else if (adBranchItemLocation.getBilLocationDownloadStatus() == 'D') {
                                adBranchItemLocation.setBilLocationDownloadStatus('X');
                            } else if (adBranchItemLocation.getBilLocationDownloadStatus() == 'U') {
                                adBranchItemLocation.setBilLocationDownloadStatus('U');
                            } else if (adBranchItemLocation.getBilLocationDownloadStatus() == 'X') {
                                adBranchItemLocation.setBilLocationDownloadStatus('X');
                            }
                        }
                    }
                }
                catch (FinderException ex) {
                }
            }
        }
        catch (GlobalRecordAlreadyExistException | InvLocFixedAssetException ex) {
            throw ex;
        }
        catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public InvLocationDetails getInvLocByLocCode(Integer LOC_CODE, Integer companyCode)
            throws GlobalNoRecordFoundException {

        Debug.print("InvLocationEntryControllerBean getInvLocByLocCode");

        LocalInvLocation invLocation;

        try {

            try {
                invLocation = invLocationHome.findByPrimaryKey(LOC_CODE);
            }
            catch (FinderException ex) {
                throw new GlobalNoRecordFoundException();
            }

            InvLocationDetails details = new InvLocationDetails();
            details.setLocCode(invLocation.getLocCode());
            details.setLocName(invLocation.getLocName());
            details.setLocDescription(invLocation.getLocDescription());
            details.setLocLvType(invLocation.getLocLvType());
            details.setLocAddress(invLocation.getLocAddress());
            details.setLocContactPerson(invLocation.getLocContactPerson());
            details.setLocContactNumber(invLocation.getLocContactNumber());
            details.setLocEmailAddress(invLocation.getLocEmailAddress());
            details.setLocDepartment(invLocation.getLocDepartment());
            details.setLocPosition(invLocation.getLocPosition());
            details.setLocDateHired(invLocation.getLocDateHired());
            details.setLocEmploymentStatus(invLocation.getLocEmploymentStatus());
            details.setLocBranch(invLocation.getLocBranch());
            return details;

        }
        catch (GlobalNoRecordFoundException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteInvLocEntry(Integer LOC_CODE, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException {

        Debug.print("InvLocationEntryControllerBean deleteInvLocEntry");

        try {

            LocalInvLocation invLocation;
            try {
                invLocation = invLocationHome.findByPrimaryKey(LOC_CODE);
            }
            catch (FinderException ex) {
                throw new GlobalRecordAlreadyDeletedException();

            }

            // if already in used in buildorder stock & stock transfers
            if (!invLocation.getInvItemLocations().isEmpty()
                    || !invLocation.getInvPhysicalInventories().isEmpty()
                    || !invLocation.getInvBranchStockTransfers().isEmpty()) {

                throw new GlobalRecordAlreadyAssignedException();

            }

            // if item location is already assigned
            if (!invLocation.getInvItemLocations().isEmpty()) {
                Collection itemLocations = invLocation.getInvItemLocations();
                Iterator locIter = itemLocations.iterator();
                while (locIter.hasNext()) {
                    LocalInvItemLocation itemLocation = (LocalInvItemLocation) locIter.next();
                    if (!itemLocation.getApVoucherLineItems().isEmpty()
                            || !itemLocation.getArInvoiceLineItems().isEmpty()
                            || !itemLocation.getInvAdjustmentLines().isEmpty()
                            || !itemLocation.getInvCostings().isEmpty()
                            || !itemLocation.getInvPhysicalInventoryLines().isEmpty()
                            || !itemLocation.getInvBranchStockTransferLines().isEmpty()
                            || !itemLocation.getApPurchaseOrderLines().isEmpty()
                            || !itemLocation.getApPurchaseRequisitionLines().isEmpty()
                            || !itemLocation.getAdBranchItemLocations().isEmpty()
                            || !itemLocation.getArSalesOrderLines().isEmpty()
                            || !itemLocation.getInvLineItems().isEmpty()) {

                        throw new GlobalRecordAlreadyAssignedException();

                    }

                    // branch item locations
                    Collection adBranchItemLocations = itemLocation.getAdBranchItemLocations();
                    for (Object branchItemLocation : adBranchItemLocations) {
                        LocalAdBranchItemLocation adBranchItemLocation = (LocalAdBranchItemLocation) branchItemLocation;

                        // if already downloaded to pos
                        if (adBranchItemLocation.getBilItemDownloadStatus() == 'D'
                                || adBranchItemLocation.getBilItemDownloadStatus() == 'X'
                                || adBranchItemLocation.getBilItemLocationDownloadStatus() == 'D'
                                || adBranchItemLocation.getBilItemLocationDownloadStatus() == 'X'
                                || adBranchItemLocation.getBilLocationDownloadStatus() == 'D'
                                || adBranchItemLocation.getBilLocationDownloadStatus() == 'X') {
                            throw new GlobalRecordAlreadyAssignedException();
                        }
                    }
                    // remove item location
                    locIter.remove();
//                    itemLocation.entityRemove();
                    em.remove(itemLocation);
                }
            }
//            invLocation.entityRemove();
            em.remove(invLocation);

        }
        catch (GlobalRecordAlreadyDeletedException | GlobalRecordAlreadyAssignedException ex) {
            getSessionContext().setRollbackOnly();
            throw ex;
        }
        catch (Exception ex) {
            throw new EJBException(ex.getMessage());

        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("InvLocationEntryControllerBean ejbCreate");

    }

}