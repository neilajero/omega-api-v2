/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class InvRepReorderItemsControllerBean
 * @created
 * @author
 */
package com.ejb.txnreports.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.ad.LocalAdBranchItemLocationHome;
import com.ejb.dao.ap.LocalApPurchaseOrderLineHome;
import com.ejb.dao.inv.LocalInvCostingHome;
import com.ejb.entities.ad.LocalAdBranchItemLocation;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.inv.LocalInvCosting;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.reports.inv.InvRepReorderItemsDetails;

import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Stateless(name = "InvRepReorderItemsControllerEJB")
public class InvRepReorderItemsControllerBean extends EJBContextClass implements InvRepReorderItemsController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private LocalInvCostingHome invReorderItemCostHome;
    @EJB
    private LocalApPurchaseOrderLineHome apPurchaseOrderLineHome;

    public ArrayList executeInvRepReorderItems(HashMap criteria, String ORDER_BY, String GROUP_BY, String type, ArrayList branchList, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvRepReorderItemsControllerBean executeInvRepReorderItems");
        ArrayList invReorderItemsList = new ArrayList();
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(bil) FROM AdBranchItemLocation bil ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("itemName")) {

                criteriaSize--;
            }

            if (criteria.containsKey("supplier")) {

                criteriaSize--;
            }

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

            if (criteria.containsKey("itemName")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("bil.invItemLocation.invItem.iiName  LIKE '%").append(criteria.get("itemName")).append("%' ");
            }

            if (criteria.containsKey("supplier")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                Debug.print("supplier: " + criteria.get("supplier"));
                jbossQl.append("bil.invItemLocation.invItem.apSupplier.splSupplierCode LIKE '%").append(criteria.get("supplier")).append("%' ");
            }

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

            if (type.equals("ALL") || type.equals("NO ACTIVITY")) {

                jbossQl.append("bil.bilAdCompany=").append(AD_CMPNY).append(" ");
            }
            if (type.equals("SELLING ITEMS")) {

                jbossQl.append("bil.invItemLocation.invItem.iiRetailUom IS NOT NULL AND bil.bilAdCompany=").append(AD_CMPNY).append(" ");
            }
            if (type.equals("NON-SELLING ITEMS")) {

                jbossQl.append("bil.invItemLocation.invItem.iiRetailUom IS NULL AND bil.bilAdCompany=").append(AD_CMPNY).append(" ");

            } else {

                jbossQl.append("AND bil.bilAdCompany=").append(AD_CMPNY).append(" ");
            }

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

            Collection adBranchItemLocations = null;

            try {

                adBranchItemLocations = adBranchItemLocationHome.getBilByCriteria(jbossQl.toString(), obj);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            if (adBranchItemLocations == null) {

                throw new GlobalNoRecordFoundException();
            }

            for (Object branchItemLocation : adBranchItemLocations) {

                LocalAdBranchItemLocation adBranchItemLocation = (LocalAdBranchItemLocation) branchItemLocation;

                InvRepReorderItemsDetails details = new InvRepReorderItemsDetails();

                if (type.equals("NO ACTIVITY")) {

                    // type = "NO ACTIVITY" -> no PO

                    Collection apPurchaseOrderLines = apPurchaseOrderLineHome.findByPlIlCodeAndPoReceivingAndBrCode(adBranchItemLocation.getInvItemLocation().getIlCode(), EJBCommon.FALSE, adBranchItemLocation.getAdBranch().getBrCode(), AD_CMPNY);

                    if (apPurchaseOrderLines.isEmpty()) {

                        details.setRiItemName(adBranchItemLocation.getInvItemLocation().getInvItem().getIiName());
                        details.setRiItemDescription(adBranchItemLocation.getInvItemLocation().getInvItem().getIiDescription());
                        details.setRiItemClass(adBranchItemLocation.getInvItemLocation().getInvItem().getIiClass());
                        details.setRiLocation(adBranchItemLocation.getAdBranch().getBrBranchCode() + "-" + adBranchItemLocation.getInvItemLocation().getInvLocation().getLocName());
                        details.setRiUomName(adBranchItemLocation.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName());
                        details.setRiReorderPoint(adBranchItemLocation.getBilReorderPoint());
                        details.setRiBranch(adBranchItemLocation.getAdBranch().getBrBranchCode());

                        try {

                            LocalInvCosting invReorderItemCost = invReorderItemCostHome.getByMaxCstDateToLongAndMaxCstLineNumberAndIiNameAndLocName(adBranchItemLocation.getInvItemLocation().getInvItem().getIiName(), adBranchItemLocation.getInvItemLocation().getInvLocation().getLocName(), adBranchItemLocation.getAdBranch().getBrCode(), AD_CMPNY);

                            details.setRiQuantity(invReorderItemCost.getCstRemainingQuantity());
                            System.out.print("invReorderItemCost.getCstRemainingQuantity():" + invReorderItemCost.getCstRemainingQuantity());

                        } catch (FinderException ex) {

                            details.setRiQuantity(0);
                        }

                        details.setRiReorderQuantity(adBranchItemLocation.getBilReorderQuantity());
                        details.setOrderBy(ORDER_BY);
                        try {
                            details.setRiSupplierCode(adBranchItemLocation.getInvItemLocation().getInvItem().getApSupplier().getSplSupplierCode());
                        } catch (Exception ex) {
                        }

                        invReorderItemsList.add(details);
                    }

                } else {

                    // type = "ALL" or "SELLING ITEMS" or "NON-SELLING ITEMS"

                    details.setRiItemName(adBranchItemLocation.getInvItemLocation().getInvItem().getIiName());
                    details.setRiItemDescription(adBranchItemLocation.getInvItemLocation().getInvItem().getIiDescription());
                    details.setRiItemClass(adBranchItemLocation.getInvItemLocation().getInvItem().getIiClass());
                    details.setRiLocation(adBranchItemLocation.getAdBranch().getBrBranchCode() + "-" + adBranchItemLocation.getInvItemLocation().getInvLocation().getLocName());
                    details.setRiUomName(adBranchItemLocation.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName());
                    details.setRiReorderPoint(adBranchItemLocation.getBilReorderPoint());
                    details.setRiBranch(adBranchItemLocation.getAdBranch().getBrBranchCode());
                    try {

                        LocalInvCosting invReorderItemCost = invReorderItemCostHome.getByMaxCstDateToLongAndMaxCstLineNumberAndIiNameAndLocName(adBranchItemLocation.getInvItemLocation().getInvItem().getIiName(), adBranchItemLocation.getInvItemLocation().getInvLocation().getLocName(), adBranchItemLocation.getAdBranch().getBrCode(), AD_CMPNY);

                        details.setRiQuantity(invReorderItemCost.getCstRemainingQuantity());
                        System.out.print("invReorderItemCost.getCstRemainingQuantity():" + invReorderItemCost.getCstRemainingQuantity());

                    } catch (FinderException ex) {

                        details.setRiQuantity(0);
                    }

                    details.setRiReorderQuantity(adBranchItemLocation.getBilReorderQuantity());
                    details.setOrderBy(ORDER_BY);
                    try {
                        details.setRiSupplierCode(adBranchItemLocation.getInvItemLocation().getInvItem().getApSupplier().getSplSupplierCode());
                    } catch (Exception ex) {
                    }

                    invReorderItemsList.add(details);
                }
            }

            if (invReorderItemsList.size() == 0) {

                throw new GlobalNoRecordFoundException();
            }

            if (GROUP_BY.equals("ITEM NAME")) {

                invReorderItemsList.sort(InvRepReorderItemsDetails.ItemNameComparator);

            } else {

                invReorderItemsList.sort(InvRepReorderItemsDetails.NoClassComparator);
            }

            return invReorderItemsList;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("InvRepItemCostingControllerBean getGlFcPrecisionUnit");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("InvRepReorderItemsControllerBean getAdCompany");
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

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("InvRepReorderItemsControllerBean ejbCreate");
    }

}