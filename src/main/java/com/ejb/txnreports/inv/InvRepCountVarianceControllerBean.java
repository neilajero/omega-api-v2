/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class InvRepCountVarianceControllerBean
 * @created
 * @author
 **/
package com.ejb.txnreports.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.dao.ad.LocalAdBranchItemLocationHome;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.dao.ad.LocalAdLookUpValueHome;
import com.ejb.dao.inv.LocalInvCostingHome;
import com.ejb.dao.inv.LocalInvLocationHome;
import com.ejb.dao.inv.LocalInvPhysicalInventoryHome;
import com.ejb.dao.inv.LocalInvPhysicalInventoryLineHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdBranchItemLocation;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.entities.ad.LocalAdLookUpValue;
import com.ejb.entities.inv.LocalInvCosting;
import com.ejb.entities.inv.LocalInvLocation;
import com.ejb.entities.inv.LocalInvPhysicalInventory;
import com.ejb.entities.inv.LocalInvPhysicalInventoryLine;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.ad.AdBranchDetails;
import com.util.reports.inv.InvRepCountVarianceDetails;
import jakarta.ejb.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Stateless(name = "InvRepCountVarianceControllerEJB")
public class InvRepCountVarianceControllerBean extends EJBContextClass implements InvRepCountVarianceController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalInvPhysicalInventoryHome invPhysicalInventoryHome;
    @EJB
    private LocalInvPhysicalInventoryLineHome invPhysicalInventoryLineHome;


    public ArrayList getAdLvInvItemCategoryAll(Integer AD_CMPNY) {

        Debug.print("InvRepCountVarianceControllerBean getAdLvInvItemCategoryAll");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("INV ITEM CATEGORY", AD_CMPNY);

            for (Object lookUpValue : adLookUpValues) {

                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;

                list.add(adLookUpValue.getLvName());

            }

            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }


    public ArrayList getInvLocAll(Integer AD_CMPNY) {

        Debug.print("InvRepCountVarianceControllerBean getInvLocAll");

        Collection invLocations = null;
        ArrayList list = new ArrayList();


        try {

            invLocations = invLocationHome.findLocAll(AD_CMPNY);

            if (invLocations.isEmpty()) {

                return null;

            }

            for (Object location : invLocations) {

                LocalInvLocation invLocation = (LocalInvLocation) location;
                String details = invLocation.getLocName();

                list.add(details);

            }

            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }


    public ArrayList executeInvRepCountVariance(String CTGRY_NM, String LOC_NM, Date UV_DT, Integer AD_BRNCH, ArrayList branchList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvRepCountVarianceControllerBean executeInvRepCountVariance");

        ArrayList list = new ArrayList();

        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(bil) FROM AdBranchItemLocation bil ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = 0;

            if (CTGRY_NM != null && CTGRY_NM.length() > 0) {
                criteriaSize++;
            }
            if (LOC_NM != null && LOC_NM.length() > 0) {
                criteriaSize++;
            }

            Object[] obj = null;
            obj = new Object[criteriaSize];

            if (branchList.isEmpty()) {

                throw new GlobalNoRecordFoundException();

            } else {

                jbossQl.append("WHERE bil.adBranch.brCode in (");

                boolean firstLoop = true;

                for (Object o : branchList) {

                    if (firstLoop == false) {
                        jbossQl.append(", ");
                    } else {
                        firstLoop = false;
                    }

                    AdBranchDetails mdetails = (AdBranchDetails) o;

                    jbossQl.append(mdetails.getBrCode());

                }

                jbossQl.append(") ");

                firstArgument = false;

            }

            if (CTGRY_NM != null && CTGRY_NM.length() > 0) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("bil.invItemLocation.invItem.iiAdLvCategory=?").append(ctr + 1).append(" ");
                obj[ctr] = CTGRY_NM;
                ctr++;

            }

            if (LOC_NM != null && LOC_NM.length() > 0) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("bil.invItemLocation.invLocation.locName=?").append(ctr + 1).append(" ");
                obj[ctr] = LOC_NM;
                ctr++;

            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("bil.invItemLocation.invItem.iiClass = 'Stock' AND bil.bilAdCompany=").append(AD_CMPNY).append(" ");

            Collection adBranchItemLocations = null;

            try {

                adBranchItemLocations = adBranchItemLocationHome.getBilByCriteria(jbossQl.toString(), obj);

            }
            catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();

            }

            if (adBranchItemLocations == null) {

                throw new GlobalNoRecordFoundException();

            }

            Iterator i = adBranchItemLocations.iterator();

            LocalInvCosting invCosting = null;

            Collection invCostings = null;

            while (i.hasNext()) {

                double BEG_INVENTORY = 0d;
                double DELIVERIES = 0d;
                double ADJUST_QTY = 0d;
                double STANDARD = 0d;
                double END_INVENTORY = 0d;
                double PHYSCL_INVENTORY = 0d;
                double WASTAGE = 0d;
                double COUNT_VARIANCE = 0d;

                LocalAdBranchItemLocation adBranchItemLocation = (LocalAdBranchItemLocation) i.next();

                InvRepCountVarianceDetails details = new InvRepCountVarianceDetails();
                details.setCvItemName(adBranchItemLocation.getInvItemLocation().getInvItem().getIiName() + "-" + adBranchItemLocation.getInvItemLocation().getInvItem().getIiDescription());

                // get remaining quantity of previous date

                try {

                    invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanCstDateAndIiNameAndLocName(UV_DT, adBranchItemLocation.getInvItemLocation().getInvItem().getIiName(), LOC_NM, AD_BRNCH, AD_CMPNY);

                    BEG_INVENTORY = invCosting.getCstRemainingQuantity();

                }
                catch (FinderException ex) {

                    BEG_INVENTORY = 0d;

                }

                // get quantity received, adjust quantity, assembly quantity and quantity sold

                try {

                    invCostings = invCostingHome.findByCstDateAndIiNameAndLocName(UV_DT, adBranchItemLocation.getInvItemLocation().getInvItem().getIiName(), LOC_NM, AD_BRNCH, AD_CMPNY);

                    for (Object costing : invCostings) {

                        invCosting = (LocalInvCosting) costing;

                        DELIVERIES += invCosting.getCstQuantityReceived();
                        ADJUST_QTY += invCosting.getCstAdjustQuantity();
                        STANDARD += invCosting.getCstQuantitySold();


                    }

                }
                catch (FinderException ex) {

                    DELIVERIES = 0d;
                    ADJUST_QTY = 0d;
                    STANDARD = 0d;

                }

                // get ending inventory of current date

                try {

                    LocalInvPhysicalInventory invPhysicalInventory = invPhysicalInventoryHome.findByPiDateAndLocNameAndBrCode(UV_DT, LOC_NM, null, AD_CMPNY);

                    if (invPhysicalInventory != null) {

                        LocalInvPhysicalInventoryLine invPhysicalInventoryLine = invPhysicalInventoryLineHome.findByPiCodeAndIlCode(invPhysicalInventory.getPiCode(), adBranchItemLocation.getInvItemLocation().getIlCode(), AD_CMPNY);

                        PHYSCL_INVENTORY = invPhysicalInventoryLine.getPilEndingInventory();
                        WASTAGE = invPhysicalInventoryLine.getPilWastage();

                    }

                }
                catch (FinderException ex) {

                    PHYSCL_INVENTORY = 0d;
                    WASTAGE = 0d;

                }

                END_INVENTORY = (BEG_INVENTORY + DELIVERIES + ADJUST_QTY) - STANDARD - WASTAGE;
                COUNT_VARIANCE = END_INVENTORY - PHYSCL_INVENTORY;

                details.setCvBegInventory(BEG_INVENTORY);
                details.setCvDeliveries(DELIVERIES);
                details.setCvAdjustQuantity(ADJUST_QTY);
                details.setCvStandardUsage(STANDARD);
                details.setCvEndInventory(END_INVENTORY);
                details.setCvPhysicalInventory(PHYSCL_INVENTORY);
                details.setCvWastage(WASTAGE);
                details.setCvVariance(COUNT_VARIANCE);

                list.add(details);

            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();

            }

            return list;

        }
        catch (GlobalNoRecordFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }
    }


    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvFindItemLocationControllerBean getAdBrResAll");

        LocalAdBranchResponsibility adBranchResponsibility = null;
        LocalAdBranch adBranch = null;

        Collection adBranchResponsibilities = null;

        ArrayList list = new ArrayList();

        try {

            adBranchResponsibilities = adBranchResponsibilityHome.findByAdResponsibility(RS_CODE, AD_CMPNY);

        }
        catch (FinderException ex) {

        }
        catch (Exception ex) {

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
                details.setBrHeadQuarter(adBranch.getBrHeadQuarter());

                list.add(details);

            }

        }
        catch (FinderException ex) {

        }
        catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        return list;

    }

    // SessionBean methods


    public void ejbCreate() throws CreateException {

        Debug.print("InvRepCountVarianceControllerBean ejbCreate");

    }

}