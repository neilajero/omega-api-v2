/**
 * @copyright 2022 Omega Business Consulting, Inc.
 * @class InvRepMarkupListControllerBean
 */
package com.ejb.txnreports.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.inv.*;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.inv.InvPriceLevelDetails;
import com.util.reports.inv.InvRepMarkupListDetails;

import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Stateless(name = "InvRepMarkupListControllerEJB")
public class InvRepMarkupListControllerBean extends EJBContextClass implements InvRepMarkupListController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalAdBranchItemLocationHome invMarkupListHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private LocalInvPriceLevelHome invPriceLevelHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;

    public ArrayList executeInvRepMarkupList(HashMap criteria, String UOM_NM, boolean INCLD_ZRS, boolean recalcMarkup, String ORDER_BY, ArrayList priceLevelList, ArrayList branchList, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvRepMarkupListControllerBean executeInvRepMarkupList");
        ArrayList invMarkupListList = new ArrayList();
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            LocalInvUnitOfMeasure invUnitOfMeasure = null;

            // Unit
            if (UOM_NM != null || UOM_NM.length() > 0) {

                try {

                    invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(UOM_NM, AD_CMPNY);

                } catch (FinderException ex) {

                }
            }

            StringBuffer jbossQl = new StringBuffer();
            jbossQl.append("SELECT OBJECT(bil) FROM AdBranchItemLocation bil ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj = null;

            // Branch Map

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

            // Allocate the size of the object parameter
            // Item Name
            if (criteria.containsKey("itemName")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("itemName")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("bil.invItemLocation.invItem.iiName  LIKE '%").append(criteria.get("itemName")).append("%' ");
            }
            // Item Class
            if (criteria.containsKey("itemClass")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("bil.invItemLocation.invItem.iiClass=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("itemClass");
                ctr++;
            }

            // Item Category
            if (criteria.containsKey("itemCategory")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("bil.invItemLocation.invItem.iiAdLvCategory=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("itemCategory");
                ctr++;
            }

            // Location
            if (criteria.containsKey("location")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("bil.invItemLocation.invLocation.locName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("location");
                ctr++;
            }

            // Location Type
            if (criteria.containsKey("locationType")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("bil.invItemLocation.invLocation.locLvType=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("locationType");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("bil.invItemLocation.invItem.iiNonInventoriable=0 AND bil.bilAdCompany=").append(AD_CMPNY).append(" ");

            // Order By

            String orderBy = null;

            switch (ORDER_BY) {
                case "ITEM NAME":

                    orderBy = "bil.invItemLocation.invItem.iiName";

                    break;
                case "ITEM DESCRIPTION":

                    orderBy = "bil.invItemLocation.invItem.iiDescription";

                    break;
                case "ITEM CLASS":

                    orderBy = "bil.invItemLocation.invItem.iiClass";
                    break;
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy);
            }

            Collection invMarkupLists = null;
            try {

                invMarkupLists = invMarkupListHome.getBilByCriteria(jbossQl.toString(), obj);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            if (invMarkupLists.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }

            for (Object markupList : invMarkupLists) {

                LocalAdBranchItemLocation invMarkupList = (LocalAdBranchItemLocation) markupList;

                InvRepMarkupListDetails details = new InvRepMarkupListDetails();
                double unitCost = 0d;
                double shppngCost = 0d;
                double salesPrice = 0d;
                double II_MRKP_VL = 0d;
                double costQty = 0d;

                if (recalcMarkup) {

                    invMarkupList.getInvItemLocation().getInvItem().getIiClass();
                    if (costQty == 0.0) {
                        costQty = invMarkupList.getInvItemLocation().getInvItem().getIiUnitCost();
                    }

                    shppngCost = invMarkupList.getInvItemLocation().getInvItem().getIiShippingCost();
                    salesPrice = invMarkupList.getInvItemLocation().getInvItem().getIiSalesPrice();
                    II_MRKP_VL = (((salesPrice - costQty - shppngCost) / (costQty + shppngCost)) * 100);
                    LocalInvItem invItem = invItemHome.findByIiName(invMarkupList.getInvItemLocation().getInvItem().getIiName(), AD_CMPNY);
                    II_MRKP_VL = EJBCommon.roundIt(II_MRKP_VL, (short) 3);
                    invItem.setIiPercentMarkup(II_MRKP_VL);
                    invItem.setIiUnitCost(costQty);
                }

                details.setMlItemName(invMarkupList.getInvItemLocation().getInvItem().getIiName());
                details.setMlItemDescription(invMarkupList.getInvItemLocation().getInvItem().getIiDescription());
                details.setMlItemClass(invMarkupList.getInvItemLocation().getInvItem().getIiClass());
                details.setMlIiPartNumber(invMarkupList.getInvItemLocation().getInvItem().getIiPartNumber());
                details.setMlLocation(invMarkupList.getInvItemLocation().getInvLocation().getLocName());
                details.setMlUnit(invMarkupList.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName());

                double SALES_PRC = invMarkupList.getInvItemLocation().getInvItem().getIiSalesPrice();
                double QTY = 0d;
                double AMOUNT = 0d;
                double UNIT_COST = 0d;
                double AVE_COST = 0d;

                double MU_PCT = 0d;
                double SHPPNG_CST = 0d;
                double GRS_PFT = 0d;

                try {

                    LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndIiNameAndLocName(details.getMlItemName(), details.getMlLocation(), invMarkupList.getAdBranch().getBrCode(), AD_CMPNY);

                    QTY = invCosting.getCstRemainingQuantity();
                    AMOUNT = invCosting.getCstRemainingValue();
                    UNIT_COST = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                    AVE_COST = invCosting.getCstRemainingQuantity() == 0 ? 0 : invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity();

                } catch (FinderException ex) {

                    QTY = 0d;
                    AMOUNT = 0d;
                    UNIT_COST = invMarkupList.getInvItemLocation().getInvItem().getIiUnitCost();
                    AVE_COST = invMarkupList.getInvItemLocation().getInvItem().getIiUnitCost();
                }

                MU_PCT = invMarkupList.getInvItemLocation().getInvItem().getIiPercentMarkup();
                SHPPNG_CST = invMarkupList.getInvItemLocation().getInvItem().getIiShippingCost();
                GRS_PFT = SALES_PRC - (AVE_COST + SHPPNG_CST);
                Debug.print("***************************");
                Debug.print("UNIT_COST: " + UNIT_COST);
                Debug.print("***************************");

                if (invUnitOfMeasure != null && invUnitOfMeasure.getUomAdLvClass().equals(invMarkupList.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomAdLvClass())) {

                    // convert qty, amount, unit cost, ave cost

                    SALES_PRC = this.convertCostByUomToAndItem(invUnitOfMeasure, invMarkupList.getInvItemLocation().getInvItem(), SALES_PRC, AD_CMPNY);
                    AMOUNT = this.convertAmountByUomToAndItemAndQtyAndAveCost(invUnitOfMeasure, invMarkupList.getInvItemLocation().getInvItem(), QTY, AVE_COST, AD_CMPNY);
                    QTY = this.convertQuantityByUomToAndItem(invUnitOfMeasure, invMarkupList.getInvItemLocation().getInvItem(), QTY, AD_CMPNY);
                    UNIT_COST = this.convertCostByUomToAndItem(invUnitOfMeasure, invMarkupList.getInvItemLocation().getInvItem(), UNIT_COST, AD_CMPNY);
                    AVE_COST = this.convertCostByUomToAndItem(invUnitOfMeasure, invMarkupList.getInvItemLocation().getInvItem(), AVE_COST, AD_CMPNY);

                    SHPPNG_CST = this.convertCostByUomToAndItem(invUnitOfMeasure, invMarkupList.getInvItemLocation().getInvItem(), SHPPNG_CST, AD_CMPNY);
                    GRS_PFT = this.convertCostByUomToAndItem(invUnitOfMeasure, invMarkupList.getInvItemLocation().getInvItem(), GRS_PFT, AD_CMPNY);

                    details.setMlUnit(invUnitOfMeasure.getUomName());
                }

                Debug.print("***************************");
                Debug.print("UNIT_COST: " + UNIT_COST);
                Debug.print("***************************");
                details.setMlSalesPrice(SALES_PRC);
                details.setMlQuantity(QTY);
                details.setMlAmount(AMOUNT);
                details.setMlUnitCost(UNIT_COST);
                details.setMlAverageCost(AVE_COST);
                details.setMlMarkupPercent(MU_PCT);
                details.setMlShippingCost(SHPPNG_CST);
                details.setMlGrossProfit(GRS_PFT);

                // get price levels
                if (!priceLevelList.isEmpty()) {
                    double II_MRKP_VL2 = 0d;
                    double prcAmnt = 0d;
                    double prcShppngCst = 0d;

                    ArrayList priceLevels = new ArrayList();

                    for (Object o : priceLevelList) {

                        String PL_AD_LV_PRC_LVL = (String) o;

                        InvPriceLevelDetails pdetails = new InvPriceLevelDetails();

                        try {

                            LocalInvPriceLevel invPriceLevel = invPriceLevelHome.findByIiNameAndAdLvPriceLevel(invMarkupList.getInvItemLocation().getInvItem().getIiName(), PL_AD_LV_PRC_LVL, AD_CMPNY);

                            pdetails.setPlAdLvPriceLevel(invPriceLevel.getPlAdLvPriceLevel());
                            if (invUnitOfMeasure != null && invUnitOfMeasure.getUomAdLvClass().equals(invMarkupList.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomAdLvClass())) {

                                pdetails.setPlAmount(this.convertCostByUomToAndItem(invUnitOfMeasure, invMarkupList.getInvItemLocation().getInvItem(), invPriceLevel.getPlAmount(), AD_CMPNY));
                                pdetails.setPlShippingCost(this.convertCostByUomToAndItem(invUnitOfMeasure, invMarkupList.getInvItemLocation().getInvItem(), invPriceLevel.getPlShippingCost(), AD_CMPNY));

                                prcAmnt = this.convertCostByUomToAndItem(invUnitOfMeasure, invMarkupList.getInvItemLocation().getInvItem(), invPriceLevel.getPlAmount(), AD_CMPNY);
                                prcShppngCst = this.convertCostByUomToAndItem(invUnitOfMeasure, invMarkupList.getInvItemLocation().getInvItem(), invPriceLevel.getPlShippingCost(), AD_CMPNY);

                                if (recalcMarkup) {
                                    // invPriceLevel.setPlAmount(costQty);
                                    II_MRKP_VL2 = (((prcAmnt - costQty - prcShppngCst) / (costQty + prcShppngCst)) * 100);
                                    II_MRKP_VL2 = EJBCommon.roundIt(II_MRKP_VL2, (short) 3);
                                    Debug.print("II_MRKP_VL2: " + II_MRKP_VL2);
                                    invPriceLevel.setPlPercentMarkup(II_MRKP_VL2);
                                }
                                pdetails.setPlPercentMarkup(invPriceLevel.getPlPercentMarkup());
                            } else {
                                pdetails.setPlAmount(invPriceLevel.getPlAmount());
                                pdetails.setPlShippingCost(invPriceLevel.getPlShippingCost());

                                prcAmnt = invPriceLevel.getPlAmount();
                                prcShppngCst = invPriceLevel.getPlShippingCost();

                                if (recalcMarkup) {
                                    // invPriceLevel.setPlAmount(costQty);
                                    II_MRKP_VL2 = (((prcAmnt - costQty - prcShppngCst) / (costQty + prcShppngCst)) * 100);
                                    II_MRKP_VL2 = EJBCommon.roundIt(II_MRKP_VL2, (short) 3);
                                    Debug.print("II_MRKP_VL3: " + II_MRKP_VL2);
                                    invPriceLevel.setPlPercentMarkup(II_MRKP_VL2);
                                }
                                pdetails.setPlPercentMarkup(invPriceLevel.getPlPercentMarkup());
                            }

                        } catch (FinderException ex) {

                            pdetails.setPlAdLvPriceLevel(PL_AD_LV_PRC_LVL);
                            pdetails.setPlAmount(0d);
                        }

                        priceLevels.add(pdetails);
                    }

                    details.setMlPriceLevels(priceLevels);
                }

                if ((details.getMlQuantity() > 0) || (INCLD_ZRS && (details.getMlQuantity() <= 0))) {
                    invMarkupListList.add(details);
                }
            }

            if (invMarkupListList.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            } else {
                return invMarkupListList;
            }

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("InvRepMarkupListControllerBean getAdCompany");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvRepMarkupListControllerBean getAdBrResAll");
        LocalAdBranchResponsibility adBranchResponsibility;
        LocalAdBranch adBranch;
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

    public ArrayList getInvUomAll(Integer AD_CMPNY) {

        Debug.print("InvRepMarkupListControllerBean getInvUomAll");
        ArrayList list = new ArrayList();
        try {

            Collection invUnitOfMeasures = invUnitOfMeasureHome.findEnabledUomAll(AD_CMPNY);

            for (Object unitOfMeasure : invUnitOfMeasures) {

                LocalInvUnitOfMeasure invUnitOfMeasure = (LocalInvUnitOfMeasure) unitOfMeasure;

                list.add(invUnitOfMeasure.getUomName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods
    private double convertQuantityByUomToAndItem(LocalInvUnitOfMeasure invToUnitOfMeasure, LocalInvItem invItem, double QTY, Integer AD_CMPNY) {

        Debug.print("InvRepMarkupListControllerBean convertQuantityByUomToAndItem");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invToUnitOfMeasure.getUomName(), AD_CMPNY);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), AD_CMPNY);

            return EJBCommon.roundIt(QTY * invUnitOfMeasureConversion.getUmcConversionFactor() / invDefaultUomConversion.getUmcConversionFactor(), adPreference.getPrfInvQuantityPrecisionUnit());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertCostByUomToAndItem(LocalInvUnitOfMeasure invToUnitOfMeasure, LocalInvItem invItem, double COST, Integer AD_CMPNY) {

        Debug.print("InvRepMarkupListControllerBean convertCostByUomToAndItem");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invToUnitOfMeasure.getUomName(), AD_CMPNY);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), AD_CMPNY);

            return EJBCommon.roundIt(COST * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), adCompany.getGlFunctionalCurrency().getFcPrecision());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertAmountByUomToAndItemAndQtyAndAveCost(LocalInvUnitOfMeasure invToUnitOfMeasure, LocalInvItem invItem, double QTY, double AVE_COST, Integer AD_CMPNY) {

        Debug.print("InvRepMarkupListControllerBean convertAmountByUomToAndItemAndQtyAndAveCost");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invToUnitOfMeasure.getUomName(), AD_CMPNY);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), AD_CMPNY);

            return EJBCommon.roundIt((AVE_COST * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor()) * (QTY * invUnitOfMeasureConversion.getUmcConversionFactor() / invDefaultUomConversion.getUmcConversionFactor()), adCompany.getGlFunctionalCurrency().getFcPrecision());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("InvRepMarkupListControllerBean ejbCreate");
    }

}