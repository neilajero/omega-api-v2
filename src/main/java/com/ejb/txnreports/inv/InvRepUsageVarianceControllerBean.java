/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class InvRepUsageVarianceControllerBean
 * @created
 * @author
 **/
package com.ejb.txnreports.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.dao.ad.LocalAdBranchItemLocationHome;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.dao.ad.LocalAdLookUpValueHome;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdBranchItemLocation;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.entities.ad.LocalAdLookUpValue;
import com.ejb.entities.inv.*;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.ad.AdBranchDetails;
import com.util.reports.inv.InvRepUsageVarianceDetails;

import jakarta.ejb.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;


@Stateless(name = "InvRepUsageVarianceControllerEJB")
public class InvRepUsageVarianceControllerBean extends EJBContextClass implements InvRepUsageVarianceController {

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
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalInvPhysicalInventoryHome invPhysicalInventoryHome;
    @EJB
    private LocalInvPhysicalInventoryLineHome invPhysicalInventoryLineHome;


    public ArrayList getAdLvInvItemCategoryAll(Integer AD_CMPNY) {

        Debug.print("InvRepUsageVarianceControllerBean getAdLvInvItemCategoryAll");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("INV ITEM CATEGORY", AD_CMPNY);

            for (Object lookUpValue : adLookUpValues) {

                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;

                list.add(adLookUpValue.getLvName());

            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }


    public ArrayList getInvLocAll(Integer AD_CMPNY) {

        Debug.print("InvRepUsageVarianceControllerBean getInvLocAll");

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

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }


    public ArrayList executeInvRepUsageVariance(String CTGRY_NM, String LOC_NM, Date UV_DT, boolean INCLD_ASSMBLY, ArrayList branchList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvRepUsageVarianceControllerBean executeInvRepUsageVariance");

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

            obj = new Object[criteriaSize];


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

            if (INCLD_ASSMBLY) {

                jbossQl.append("bil.bilAdCompany=").append(AD_CMPNY).append(" ORDER BY bil.invItemLocation.invItem.iiName");

            } else {

                jbossQl.append("bil.invItemLocation.invItem.iiClass = 'Stock' AND bil.bilAdCompany=").append(AD_CMPNY).append(" ORDER BY bil.invItemLocation.invItem.iiName");

            }

            Collection adBranchItemLocations = null;

            try {

                adBranchItemLocations = adBranchItemLocationHome.getBilByCriteria(jbossQl.toString(), obj);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();

            }

            if (adBranchItemLocations == null) {

                throw new GlobalNoRecordFoundException();

            }

            Iterator i = adBranchItemLocations.iterator();

            String PREVIOUS_ITEM = null;

            while (i.hasNext()) {

                double BEG_INVENTORY = 0d;
                double DELIVERIES = 0d;
                double ADJUST_QTY = 0d;
                double END_INVENTORY = 0d;
                double ACTUAL = 0d;
                double STANDARD = 0d;
                double WASTAGE = 0d;
                double VARIANCE = 0d;

                LocalAdBranchItemLocation adBranchItemLocation = (LocalAdBranchItemLocation) i.next();

                InvRepUsageVarianceDetails details = new InvRepUsageVarianceDetails();
                details.setUvItemName(adBranchItemLocation.getInvItemLocation().getInvItem().getIiName() + "-" + adBranchItemLocation.getInvItemLocation().getInvItem().getIiDescription());

                // consolidate locations

                if (LOC_NM == null || LOC_NM.length() == 0) {

                    if (PREVIOUS_ITEM != null && PREVIOUS_ITEM.equals(adBranchItemLocation.getInvItemLocation().getInvItem().getIiName())) {
                        continue;
                    }

                    Collection invItemsToConsolidate = null;

                    if (CTGRY_NM != null && CTGRY_NM.length() > 0) {

                        invItemsToConsolidate = invItemLocationHome.findByIiNameAndIiAdLvCategory(adBranchItemLocation.getInvItemLocation().getInvItem().getIiName(), adBranchItemLocation.getInvItemLocation().getInvItem().getIiAdLvCategory(), AD_CMPNY);
                    } else {

                        invItemsToConsolidate = invItemLocationHome.findByIiName(adBranchItemLocation.getInvItemLocation().getInvItem().getIiName(), AD_CMPNY);

                    }

                    for (Object o : invItemsToConsolidate) {

                        //  get remaining quantity of previous date

                        LocalInvItemLocation invItemToConsolidate = (LocalInvItemLocation) o;

                        try {

                            LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanCstDateAndIiNameAndLocName(UV_DT, invItemToConsolidate.getInvItem().getIiName(), invItemToConsolidate.getInvLocation().getLocName(), adBranchItemLocation.getBilAdCompany(), AD_CMPNY);

                            BEG_INVENTORY += invCosting.getCstRemainingQuantity();

                        } catch (FinderException ex) {

                            BEG_INVENTORY += 0d;

                        }

                        // get quantity received, adjust quantity, assembly quantity and quantity sold

                        try {

                            Collection invCostings = invCostingHome.findByCstDateAndIiNameAndLocName(UV_DT, invItemToConsolidate.getInvItem().getIiName(), invItemToConsolidate.getInvLocation().getLocName(), adBranchItemLocation.getBilAdCompany(), AD_CMPNY);

                            for (Object costing : invCostings) {

                                LocalInvCosting invCosting = (LocalInvCosting) costing;

                                DELIVERIES += invCosting.getCstQuantityReceived();
                                ADJUST_QTY += invCosting.getCstAdjustQuantity();
                                STANDARD += invCosting.getCstQuantitySold();

                            }

                        } catch (FinderException ex) {

                            DELIVERIES += 0d;
                            ADJUST_QTY += 0d;
                            STANDARD += 0d;

                        }

                        // get ending inventory of current date

                        try {

                            LocalInvPhysicalInventory invPhysicalInventory = invPhysicalInventoryHome.findByPiDateAndLocNameAndBrCode(UV_DT, invItemToConsolidate.getInvLocation().getLocName(), adBranchItemLocation.getAdBranch().getBrCode(), AD_CMPNY);

                            if (invPhysicalInventory != null) {

                                LocalInvPhysicalInventoryLine invPhysicalInventoryLine = invPhysicalInventoryLineHome.findByPiCodeAndIlCode(invPhysicalInventory.getPiCode(), invItemToConsolidate.getIlCode(), AD_CMPNY);

                                END_INVENTORY += invPhysicalInventoryLine.getPilEndingInventory();
                                WASTAGE += invPhysicalInventoryLine.getPilWastage();

                            }

                        } catch (FinderException ex) {

                            END_INVENTORY += 0d;
                            WASTAGE += 0d;

                        }

                    }

                } else {

                    //   get remaining quantity of previous date

                    try {

                        LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanCstDateAndIiNameAndLocName(UV_DT, adBranchItemLocation.getInvItemLocation().getInvItem().getIiName(), LOC_NM, adBranchItemLocation.getAdBranch().getBrCode(), AD_CMPNY);

                        BEG_INVENTORY = invCosting.getCstRemainingQuantity();

                    } catch (FinderException ex) {

                        BEG_INVENTORY = 0d;

                    }

                    // get quantity received, adjust quantity, assembly quantity and quantity sold

                    try {

                        Collection invCostings = invCostingHome.findByCstDateAndIiNameAndLocName(UV_DT, adBranchItemLocation.getInvItemLocation().getInvItem().getIiName(), LOC_NM, adBranchItemLocation.getAdBranch().getBrCode(), AD_CMPNY);

                        for (Object costing : invCostings) {

                            LocalInvCosting invCosting = (LocalInvCosting) costing;

                            DELIVERIES += invCosting.getCstQuantityReceived();
                            ADJUST_QTY += invCosting.getCstAdjustQuantity();
                            STANDARD += invCosting.getCstQuantitySold();


                        }

                    } catch (FinderException ex) {

                        DELIVERIES = 0d;
                        ADJUST_QTY = 0d;
                        STANDARD = 0d;

                    }

                    // get ending inventory of current date

                    try {

                        LocalInvPhysicalInventory invPhysicalInventory = invPhysicalInventoryHome.findByPiDateAndLocNameAndBrCode(UV_DT, LOC_NM, adBranchItemLocation.getAdBranch().getBrCode(), AD_CMPNY);

                        if (invPhysicalInventory != null) {

                            LocalInvPhysicalInventoryLine invPhysicalInventoryLine = invPhysicalInventoryLineHome.findByPiCodeAndIlCode(invPhysicalInventory.getPiCode(), adBranchItemLocation.getInvItemLocation().getIlCode(), AD_CMPNY);

                            END_INVENTORY = invPhysicalInventoryLine.getPilEndingInventory();
                            WASTAGE = invPhysicalInventoryLine.getPilWastage();

                        }

                    } catch (FinderException ex) {

                        END_INVENTORY = 0d;
                        WASTAGE = 0d;

                    }

                }

                ACTUAL = (BEG_INVENTORY + DELIVERIES + ADJUST_QTY) - END_INVENTORY;
                VARIANCE = (ACTUAL - WASTAGE) - STANDARD;

                details.setUvBegInventory(BEG_INVENTORY);
                details.setUvDeliveries(DELIVERIES);
                details.setUvAdjustQuantity(ADJUST_QTY);
                details.setUvEndInventory(END_INVENTORY);
                details.setUvActual(ACTUAL);
                details.setUvStandardUsage(STANDARD);
                details.setUvWastage(WASTAGE);
                details.setUvVariance(VARIANCE);

                list.add(details);

                PREVIOUS_ITEM = adBranchItemLocation.getInvItemLocation().getInvItem().getIiName();

            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();

            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

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
                details.setBrHeadQuarter(adBranch.getBrHeadQuarter());

                list.add(details);

            }

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        return list;

    }

    // SessionBean methods


    public void ejbCreate() throws CreateException {

        Debug.print("InvRepUsageVarianceControllerBean ejbCreate");

    }

}