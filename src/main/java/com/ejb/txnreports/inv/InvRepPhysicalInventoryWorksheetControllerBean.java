/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class InvRepPhysicalInventoryWorksheetControllerBean
 * @created September 11,2006 11:27 AM
 * @author Rey B. Limosenero
 */
package com.ejb.txnreports.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.dao.ap.LocalApPurchaseOrderLineHome;
import com.ejb.dao.ap.LocalApVoucherLineItemHome;
import com.ejb.dao.ar.LocalArCustomerHome;
import com.ejb.dao.ar.LocalArInvoiceLineItemHome;
import com.ejb.dao.ar.LocalArSalesOrderInvoiceLineHome;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ap.LocalApPurchaseOrderLine;
import com.ejb.entities.ap.LocalApVoucherLineItem;
import com.ejb.entities.ar.LocalArCustomer;
import com.ejb.entities.ar.LocalArInvoiceLineItem;
import com.ejb.entities.ar.LocalArSalesOrderInvoiceLine;
import com.ejb.entities.inv.*;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.ad.AdCompanyDetails;
import com.util.reports.inv.InvRepPhysicalInventoryWorksheetDetails;

import jakarta.ejb.*;
import java.util.*;

@Stateless(name = "InvRepPhysicalInventoryWorksheetControllerEJB")
public class InvRepPhysicalInventoryWorksheetControllerBean extends EJBContextClass implements InvRepPhysicalInventoryWorksheetController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalInvPhysicalInventoryHome invPhysicalInventoryHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalInvAdjustmentLineHome invAdjustmentLineHome;
    @EJB
    private LocalInvStockTransferLineHome invStockTransferLineHome;
    @EJB
    private LocalInvBranchStockTransferLineHome invBranchStockTransferLineHome;
    @EJB
    private LocalApVoucherLineItemHome apVoucherLineItemHome;
    @EJB
    private LocalArInvoiceLineItemHome arInvoiceLineItemHome;
    @EJB
    private LocalApPurchaseOrderLineHome apPurchaseOrderLineHome;
    @EJB
    private LocalArSalesOrderInvoiceLineHome arSalesOrderInvoiceLineHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalInvPriceLevelHome invPriceLevelHome;
    @EJB
    private LocalAdBranchHome adBranchHome;

    public ArrayList getInvCstAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("InvRepPhysicalInventoryWorksheetControllerBean getInvCstAll");
        ArrayList list = new ArrayList();
        try {

            Collection arCustomers = arCustomerHome.findEnabledCstAll(AD_BRNCH, AD_CMPNY);

            for (Object customer : arCustomers) {

                LocalArCustomer arCustomer = (LocalArCustomer) customer;

                list.add(arCustomer.getCstCustomerCode());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList executeInvRepPhysicalInventoryWorksheet(HashMap criteria, Date PI_DT, boolean INCLD_UNPSTD, boolean INCLD_ENCDD, Integer AD_CMPNY, boolean INCLD_INTRNST) throws GlobalNoRecordFoundException {

        Debug.print("InvRepPhysicalInventoryWorksheetControllerBean executeInvRepPhysicalInventoryWorksheet");
        
        ArrayList list = new ArrayList();
        String paramCategory = "";
        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(il) FROM InvItemLocation il, IN(il.adBranchItemLocations)bil ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            Integer AD_BRNCH = null;
            String PriceLevel = "";

            if (criteria.containsKey("branch")) {

                criteriaSize--;

                AD_BRNCH = this.getAdBrCodeByBrName((String) criteria.get("branch"), AD_CMPNY);

                if (AD_BRNCH == null) {
                    throw new GlobalNoRecordFoundException();
                }
            }

            if (criteria.containsKey("customer")) {

                criteriaSize--;

                String cstmr = (String) criteria.get("customer");

                PriceLevel = arCustomerHome.findByCstName(cstmr, AD_CMPNY).getCstDealPrice();

                Debug.print("customer: " + criteria.get("customer"));
                Debug.print("PriceLevel: " + PriceLevel);
            }

            // Allocate the size of the object parameter

            obj = new Object[criteriaSize];
            String loc = (String) criteria.get("location");
            if (criteria.containsKey("location")) {
                if (loc.equalsIgnoreCase("ALL")) {
                    Debug.print("XD");
                    obj[ctr] = criteria.get("location");
                    ctr++;
                } else {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("il.invLocation.locName=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("location");
                    ctr++;
                }
            }

            if (criteria.containsKey("itemCategory")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("il.invItem.iiAdLvCategory=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("itemCategory");
                paramCategory = obj[ctr].toString();
                ctr++;
            }

            if (criteria.containsKey("itemClass")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("il.invItem.iiClass=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("itemClass");
                ctr++;
            }

            if (criteria.containsKey("branch")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("bil.adBranch.brCode=").append(AD_BRNCH).append(" ");
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("il.invItem.iiNonInventoriable=0 AND il.invItem.iiEnable=1 AND il.ilAdCompany=").append(AD_CMPNY).append(" ORDER BY il.invItem.iiAdLvCategory, il.invItem.iiName");
            Debug.print("jbossQl.toString()=" + jbossQl.toString());
            Collection invItemLocations = invItemLocationHome.getIlByCriteria(jbossQl.toString(), obj, 0, 0);

            if (invItemLocations.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            for (Object itemLocation : invItemLocations) {

                LocalInvItemLocation invItemLocation = (LocalInvItemLocation) itemLocation;

                InvRepPhysicalInventoryWorksheetDetails mPiwDetails = new InvRepPhysicalInventoryWorksheetDetails();

                if (INCLD_ENCDD) {

                    LocalInvPhysicalInventory invPhysicalInventory = null;
                    try {
                        if (paramCategory.length() == 0) {
                            invPhysicalInventory = invPhysicalInventoryHome.findByPiDateAndLocNameAndBrCode(PI_DT, invItemLocation.getInvLocation().getLocName(), AD_BRNCH, AD_CMPNY);
                        } else {
                            invPhysicalInventory = invPhysicalInventoryHome.findByPiDateAndLocNameAndCategoryNameAndBrCode(PI_DT, invItemLocation.getInvLocation().getLocName(), paramCategory, AD_BRNCH, AD_CMPNY);
                        }

                        for (Object o : invPhysicalInventory.getInvPhysicalInventoryLines()) {
                            LocalInvPhysicalInventoryLine invPhysicalInventoryLine = (LocalInvPhysicalInventoryLine) o;
                            if (Objects.equals(invPhysicalInventoryLine.getInvItemLocation().getIlCode(), invItemLocation.getIlCode())) {
                                Debug.print("invPhysicalInventoryLine.getInvPhysicalInventory().getPiCode(): " + invPhysicalInventoryLine.getInvPhysicalInventory().getPiCode());
                                mPiwDetails.setPiwPilEndingInv(invPhysicalInventoryLine.getPilEndingInventory());
                                Debug.print("invPhysicalInventoryLine.getPilEndingInventory(): " + invPhysicalInventoryLine.getPilEndingInventory());
                                mPiwDetails.setPiwPilWastage(invPhysicalInventoryLine.getPilWastage());
                                Debug.print("invPhysicalInventoryLine.getPilWastage(): " + invPhysicalInventoryLine.getPilWastage());
                                mPiwDetails.setPiwPilVariance(invPhysicalInventoryLine.getPilVariance());
                                Debug.print("invPhysicalInventoryLine.getPilVariance(): " + invPhysicalInventoryLine.getPilVariance());
                                break;
                            }
                        }
                    } catch (Exception ex) {

                    }
                }

                try {

                    Debug.print("invItemLocation.getInvItem().getIiName(): " + invItemLocation.getInvItem().getIiName());
                    Debug.print("invItemLocation.getInvLocation().getLocName(): " + invItemLocation.getInvLocation().getLocName());
                    Debug.print("invItemLocation.getInvItem().getIiUnitCost(): " + invItemLocation.getInvItem().getIiUnitCost());

                    LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(PI_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, AD_CMPNY);

                    mPiwDetails.setPiwPilActualQty(invCosting.getCstRemainingQuantity());

                    if (invCosting.getCstRemainingValue() == 0) {
                        mPiwDetails.setPiwPilActualCost(invItemLocation.getInvItem().getIiUnitCost());
                    } else {
                        mPiwDetails.setPiwPilActualCost(invCosting.getCstRemainingValue());
                    }

                    Debug.print("invCosting.getCstRemainingQuantity(): " + invCosting.getCstRemainingQuantity());
                    Debug.print("invCosting.getCstRemainingValue(): " + invCosting.getCstRemainingValue());

                } catch (FinderException ex) {
                    mPiwDetails.setPiwPilActualQty(0d);
                    mPiwDetails.setPiwPilActualCost(invItemLocation.getInvItem().getIiUnitCost());
                }

                mPiwDetails.setPiwPilItemName(invItemLocation.getInvItem().getIiName());
                mPiwDetails.setPiwPilItemDescription(invItemLocation.getInvItem().getIiDescription());
                mPiwDetails.setPiwPilItemCategory(invItemLocation.getInvItem().getIiAdLvCategory());
                mPiwDetails.setPiwPilUnitMeasureName(invItemLocation.getInvItem().getInvUnitOfMeasure().getUomName());

                if (INCLD_UNPSTD) {

                    double unpostedQty = 0d;
                    double unpostedCost = 0d;

                    String locName = invItemLocation.getInvLocation().getLocName();
                    Integer locCode = invLocationHome.findByLocName(locName, AD_CMPNY).getLocCode();

                    // INV_ADJUSTMENT_LINE
                    Collection invAdjustmentLines = invAdjustmentLineHome.findUnpostedAdjByLocNameAndAdBranch(locName, AD_BRNCH, AD_CMPNY);

                    Iterator unpstdIter = invAdjustmentLines.iterator();

                    while (unpstdIter.hasNext()) {

                        LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) unpstdIter.next();

                        LocalInvItemLocation invItemLocationAdj = invAdjustmentLine.getInvItemLocation();
                        LocalInvItem invItem = invItemLocationAdj.getInvItem();

                        if (this.filterByOptionalCriteria(criteria, invItem) || !invItemLocationAdj.equals(invItemLocation)) {
                            continue;
                        }
                        unpostedQty += invAdjustmentLine.getAlAdjustQuantity();

                        Collection invCostings = invAdjustmentLine.getInvCostings();

                        if (invCostings.size() > 0) {
                            Iterator<LocalInvCosting> ii = invCostings.iterator();
                            ii.next();
                            LocalInvCosting invCosting = ii.next();
                            unpostedCost = invCosting.getCstRemainingValue();
                        } else {
                            unpostedCost = invAdjustmentLine.getInvItemLocation().getInvItem().getIiUnitCost();
                        }
                    }

                    // INV_STOCK_TRANSFER_LINE
                    Collection invStockTransferLines = invStockTransferLineHome.findUnpostedStByLocCodeAndAdBranch(locCode, AD_BRNCH, AD_CMPNY);

                    unpstdIter = invStockTransferLines.iterator();

                    while (unpstdIter.hasNext()) {

                        LocalInvStockTransferLine invStockTransferLine = (LocalInvStockTransferLine) unpstdIter.next();

                        LocalInvItem invItem = invStockTransferLine.getInvItem();

                        if (this.filterByOptionalCriteria(criteria, invItem)) {
                            continue;
                        }

                        unpostedQty += invStockTransferLine.getStlQuantityDelivered();
                    }

                    // INV_BRANCH_STOCK_TRANSFER_LINE
                    Collection invBranchStockTransferLines = invBranchStockTransferLineHome.findUnpostedBstByLocNameAndAdBranch(locName, AD_BRNCH, AD_CMPNY);

                    unpstdIter = invBranchStockTransferLines.iterator();

                    while (unpstdIter.hasNext()) {

                        LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) unpstdIter.next();

                        LocalInvItemLocation invItemLocationBst = invBranchStockTransferLine.getInvItemLocation();
                        LocalInvItem invItem = invItemLocationBst.getInvItem();

                        if (this.filterByOptionalCriteria(criteria, invItem) || !invItemLocationBst.equals(invItemLocation)) {
                            continue;
                        }

                        unpostedQty += invBranchStockTransferLine.getBslQuantityReceived();
                    }

                    // AP_VOUCHER_LINE_ITEM
                    // a) apVoucher
                    Collection apVoucherLineItems = apVoucherLineItemHome.findUnpostedVouByLocNameAndAdBranch(locName, AD_BRNCH, AD_CMPNY);

                    unpstdIter = apVoucherLineItems.iterator();

                    while (unpstdIter.hasNext()) {

                        LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) unpstdIter.next();

                        LocalInvItemLocation invItemLocationVou = apVoucherLineItem.getInvItemLocation();
                        LocalInvItem invItem = invItemLocationVou.getInvItem();

                        if (this.filterByOptionalCriteria(criteria, invItem) || !invItemLocationVou.equals(invItemLocation)) {
                            continue;
                        }

                        unpostedQty += apVoucherLineItem.getVliQuantity();
                    }

                    // b) apCheck
                    apVoucherLineItems = apVoucherLineItemHome.findUnpostedChkByLocNameAndAdBranch(locName, AD_BRNCH, AD_CMPNY);

                    unpstdIter = apVoucherLineItems.iterator();

                    while (unpstdIter.hasNext()) {

                        LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) unpstdIter.next();

                        LocalInvItemLocation invItemLocationChk = apVoucherLineItem.getInvItemLocation();
                        LocalInvItem invItem = invItemLocationChk.getInvItem();

                        if (this.filterByOptionalCriteria(criteria, invItem) || !invItemLocationChk.equals(invItemLocation)) {
                            continue;
                        }

                        unpostedQty += apVoucherLineItem.getVliQuantity();
                    }

                    // AR_INVOICE_LINE_ITEM
                    // a) arInvoice
                    Collection arInvoiceLineItems = arInvoiceLineItemHome.findUnpostedInvcByLocNameAndAdBranch(locName, AD_BRNCH, AD_CMPNY);

                    unpstdIter = arInvoiceLineItems.iterator();

                    while (unpstdIter.hasNext()) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) unpstdIter.next();

                        LocalInvItemLocation invItemLocationInv = arInvoiceLineItem.getInvItemLocation();
                        LocalInvItem invItem = invItemLocationInv.getInvItem();

                        if (this.filterByOptionalCriteria(criteria, invItem) || !invItemLocationInv.equals(invItemLocation)) {
                            continue;
                        }

                        unpostedQty -= arInvoiceLineItem.getIliQuantity();
                    }

                    // b) arReceipt
                    arInvoiceLineItems = arInvoiceLineItemHome.findUnpostedRctByLocNameAndAdBranch(locName, AD_BRNCH, AD_CMPNY);

                    unpstdIter = arInvoiceLineItems.iterator();

                    while (unpstdIter.hasNext()) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) unpstdIter.next();

                        LocalInvItemLocation invItemLocationRct = arInvoiceLineItem.getInvItemLocation();
                        LocalInvItem invItem = invItemLocationRct.getInvItem();

                        if (this.filterByOptionalCriteria(criteria, invItem) || !invItemLocationRct.equals(invItemLocation)) {
                            continue;
                        }

                        unpostedQty -= arInvoiceLineItem.getIliQuantity();
                    }

                    // AP_PURCHASE_ORDER_LINE
                    Collection apPurchaseOrderLines = apPurchaseOrderLineHome.findUnpostedPoByLocNameAndAdBranch(locName, AD_BRNCH, AD_CMPNY);

                    unpstdIter = apPurchaseOrderLines.iterator();

                    while (unpstdIter.hasNext()) {

                        LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) unpstdIter.next();

                        LocalInvItemLocation invItemLocationPo = apPurchaseOrderLine.getInvItemLocation();
                        LocalInvItem invItem = invItemLocationPo.getInvItem();

                        if (this.filterByOptionalCriteria(criteria, invItem) || !invItemLocationPo.equals(invItemLocation)) {
                            continue;
                        }

                        unpostedQty += apPurchaseOrderLine.getPlQuantity();
                        Collection invCostings = apPurchaseOrderLine.getInvCostings();

                        if (invCostings.size() > 0) {
                            Iterator<LocalInvCosting> ii = invCostings.iterator();
                            ii.next();
                            LocalInvCosting invCosting = ii.next();
                            unpostedCost = invCosting.getCstRemainingValue();
                        } else {
                            unpostedCost = apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiUnitCost();
                        }
                    }

                    // AR_SALES_ORDER_INVOICE_LINE
                    Collection arSalesOrderInvoiceLines = arSalesOrderInvoiceLineHome.findUnpostedSoInvByLocNameAndAdBranch(locName, AD_BRNCH, AD_CMPNY);

                    unpstdIter = arSalesOrderInvoiceLines.iterator();

                    while (unpstdIter.hasNext()) {

                        LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) unpstdIter.next();

                        LocalInvItemLocation invItemLocationSo = arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation();
                        LocalInvItem invItem = invItemLocationSo.getInvItem();

                        if (this.filterByOptionalCriteria(criteria, invItem) || !invItemLocationSo.equals(invItemLocation)) {
                            continue;
                        }

                        unpostedQty -= arSalesOrderInvoiceLine.getSilQuantityDelivered();
                        Collection invCostings = arSalesOrderInvoiceLine.getInvCostings();

                        if (invCostings.size() > 0) {
                            Iterator<LocalInvCosting> ii = invCostings.iterator();
                            ii.next();
                            LocalInvCosting invCosting = ii.next();
                            unpostedCost = invCosting.getCstRemainingValue();
                        } else {
                            unpostedCost = arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvItem().getIiUnitCost();
                        }
                    }

                    mPiwDetails.setPiwPilActualQty(mPiwDetails.getPiwPilActualQty() + unpostedQty);
                    mPiwDetails.setPiwPilActualCost(unpostedCost);
                }

                if (INCLD_INTRNST) {
                    Debug.print("INCLD_INTRNST: " + invItemLocation.getInvItem().getIiName());
                    double actQty = mPiwDetails.getPiwPilActualQty();
                    byte pstd = 1;
                    try {

                        Object[] newObj = null;
                        newObj = new Object[4];

                        newObj[0] = pstd;
                        newObj[1] = invItemLocation.getIlCode();
                        newObj[2] = AD_BRNCH;
                        newObj[3] = AD_CMPNY;

                        // INV_BRANCH_STOCK_TRANSFER_LINE
                        Collection invBranchStockTransferLines = invBranchStockTransferLineHome.getBstlByCriteria("SELECT OBJECT(bsl) FROM InvBranchStockTransferLine bsl WHERE bsl.invBranchStockTransfer.bstType = 'OUT' AND bsl.invBranchStockTransfer.bstPosted=?1 AND bsl.invItemLocation.ilCode=?2 AND bsl.invBranchStockTransfer.adBranch.brCode=?3 AND bsl.bslAdCompany=?4", newObj);

                        for (Object branchStockTransferLine : invBranchStockTransferLines) {
                            Debug.print("actQty: " + actQty);
                            LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) branchStockTransferLine;
                            double bstQty = invBranchStockTransferLine.getBslQuantity() - invBranchStockTransferLine.getBslQuantityReceived();
                            Debug.print("bstQty: " + bstQty);
                            mPiwDetails.setPiwPilActualQty(actQty + bstQty);
                            Debug.print("pilActQty: " + mPiwDetails.getPiwPilActualQty());
                        }

                    } catch (FinderException ex) {

                        mPiwDetails.setPiwPilActualQty(actQty);
                        Debug.print("pilActQty2: " + mPiwDetails.getPiwPilActualQty());
                    }
                }

                if (!PriceLevel.equals("")) {
                    double slsPrc = invPriceLevelHome.findByIiNameAndAdLvPriceLevel(mPiwDetails.getPiwPilItemName(), PriceLevel, AD_CMPNY).getPlAmount();
                    mPiwDetails.setPiwPilSalesPrice(slsPrc);
                    Debug.print("slsPrc: " + slsPrc);
                } else {
                    mPiwDetails.setPiwPilSalesPrice(invItemLocation.getInvItem().getIiSalesPrice());
                    Debug.print("getIiSalesPrice");
                }

                list.add(mPiwDetails);
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("InvRepPhysicalInventoryWorksheetControllerBean getAdCompany");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());
            details.setCmpAddress(adCompany.getCmpAddress());
            details.setCmpCity(adCompany.getCmpCity());
            details.setCmpCountry(adCompany.getCmpCountry());
            details.setCmpPhone(adCompany.getCmpPhone());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private Integer getAdBrCodeByBrName(String BR_NM, Integer AD_CMPNY) {

        Debug.print("InvRepPhysicalInventoryWorksheetControllerBean getAdBrCodeByBrName");
        try {

            LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(BR_NM, AD_CMPNY);

            return adBranch.getBrCode();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private boolean filterByOptionalCriteria(HashMap criteria, LocalInvItem invItem) {

        Debug.print("InvRepItemCostingControllerBean filterByOptionalCriteria");
        try {

            String itemClass = "";
            String itemCategory = "";

            if (criteria.containsKey("itemClass")) {
                itemClass = (String) criteria.get("itemClass");
            }

            if (criteria.containsKey("category")) {
                itemCategory = (String) criteria.get("category");
            }

            if (!itemClass.equals("") && !invItem.getIiClass().equals(itemClass)) {
                return true;
            }

            return !itemCategory.equals("") && !invItem.getIiAdLvCategory().equals(itemCategory);

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("InvPhysicalInventoryWorksheetControllerBean ejbCreate");
    }

}