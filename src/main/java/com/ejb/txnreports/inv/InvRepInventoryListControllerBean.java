/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class InvRepInventoryListControllerBean
 * @created
 * @author
 */
package com.ejb.txnreports.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.ad.LocalAdBranchItemLocationHome;
import com.ejb.dao.ap.LocalApPurchaseOrderLineHome;
import com.ejb.dao.ap.LocalApVoucherLineItemHome;
import com.ejb.dao.ar.*;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.LocalAdBranchItemLocation;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ap.LocalApPurchaseOrderLine;
import com.ejb.entities.ap.LocalApVoucherLineItem;
import com.ejb.entities.ar.*;
import com.ejb.entities.inv.*;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.inv.InvPriceLevelDetails;
import com.util.reports.inv.InvRepInventoryListDetails;

import jakarta.ejb.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Stateless(name = "InvRepInventoryListControllerEJB")
public class InvRepInventoryListControllerBean extends EJBContextClass implements InvRepInventoryListController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalAdBranchItemLocationHome invInventoryListHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private LocalInvPriceLevelHome invPriceLevelHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalArSalesOrderLineHome arSalesOrderLineHome;
    @EJB
    private LocalArJobOrderLineHome arJobOrderLineHome;
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
    private LocalArJobOrderInvoiceLineHome arJobOrderInvoiceLineHome;
    @EJB
    private LocalInvTagHome invTagHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;

    public ArrayList executeInvRepInventoryList(HashMap criteria, String UOM_NM, Date AS_OF_DT, boolean INCLD_ZRS, boolean SHW_CMMTTD_QNTTY, boolean INCLD_UNPSTD, boolean SHW_TGS, String ORDER_BY, ArrayList priceLevelList, ArrayList branchList, Integer AD_BRNCH, Integer AD_CMPNY, String EXPRY_DT) throws GlobalNoRecordFoundException {

        Debug.print("InvRepInventoryListControllerBean executeInvRepInventoryList");
        ArrayList invInventoryListList = new ArrayList();
        try {

            String locName = "";
            Integer locCode = null;

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            LocalInvUnitOfMeasure invUnitOfMeasure = null;
            // LocalInvTag invTag = invTagHome.findByAlCode(2, 1);

            if (UOM_NM != null || UOM_NM.length() > 0) {

                try {

                    invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(UOM_NM, AD_CMPNY);

                } catch (FinderException ex) {

                }
            }

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(bil) FROM AdBranchItemLocation bil ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

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

            // Allocate the size of the object parameter

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

            jbossQl.append("bil.invItemLocation.invItem.iiNonInventoriable=0 AND bil.bilAdCompany=").append(AD_CMPNY).append(" ");

            String expiryDate = "";
            if (criteria.containsKey("expiryDate")) {
                expiryDate = (String) criteria.get("expiryDate");
                // Debug.print((String)criteria.get("expiryDate"));

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

            Collection invInventoryLists = null;

            Debug.print("SSQL=" + jbossQl.toString());

            try {

                invInventoryLists = invInventoryListHome.getBilByCriteria(jbossQl.toString(), obj);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            if (invInventoryLists.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }

            Iterator i = invInventoryLists.iterator();
            int miscList2 = 0;

            while (i.hasNext()) {

                LocalAdBranchItemLocation invInventoryList = (LocalAdBranchItemLocation) i.next();

                InvRepInventoryListDetails details = new InvRepInventoryListDetails();

                details.setRilItemName(invInventoryList.getInvItemLocation().getInvItem().getIiName());
                details.setRilItemDescription(invInventoryList.getInvItemLocation().getInvItem().getIiDescription());
                Debug.print("CATEGORY==" + invInventoryList.getInvItemLocation().getInvItem().getIiAdLvCategory());
                details.setRilItemCategory(invInventoryList.getInvItemLocation().getInvItem().getIiAdLvCategory());
                details.setRilItemClass(invInventoryList.getInvItemLocation().getInvItem().getIiClass());
                details.setRilIiPartNumber(invInventoryList.getInvItemLocation().getInvItem().getIiPartNumber());
                details.setRilLocation(invInventoryList.getInvItemLocation().getInvLocation().getLocName());
                details.setRilUnit(invInventoryList.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName());

                double SALES_PRC = invInventoryList.getInvItemLocation().getInvItem().getIiSalesPrice();
                double QTY = 0d;
                double AMOUNT = 0d;
                double UNIT_COST = 0d;
                double AVE_COST = 0d;

                try {

                    LocalInvCosting invInventoryListCost = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(AS_OF_DT, details.getRilItemName(), details.getRilLocation(), invInventoryList.getAdBranch().getBrCode(), AD_CMPNY);

                    QTY = invInventoryListCost.getCstRemainingQuantity();
                    AMOUNT = invInventoryListCost.getCstRemainingValue();
                    UNIT_COST = invInventoryList.getInvItemLocation().getInvItem().getIiUnitCost();
                    AVE_COST = invInventoryListCost.getCstRemainingQuantity() == 0 ? 0 : invInventoryListCost.getCstRemainingValue() / invInventoryListCost.getCstRemainingQuantity();

                    Date dtePlusPlus = null;
                    Calendar dtePlus = Calendar.getInstance();
                    dtePlus.setTime(AS_OF_DT);

                    Calendar dtePlus2 = Calendar.getInstance();
                    dtePlus2.setTime(AS_OF_DT);
                    int x = 0;

                    if (invInventoryListCost.getCstExpiryDate() != null && invInventoryListCost.getCstExpiryDate() != "" && invInventoryListCost.getCstExpiryDate().length() != 0) {
                        switch (EXPRY_DT) {
                            case "30 Days":
                                dtePlus.add(Calendar.DATE, 30);
                                x = 30;
                                dtePlusPlus = dtePlus.getTime();
                                break;
                            case "60 Days":
                                dtePlus.add(Calendar.DATE, 60);
                                x = 60;
                                dtePlusPlus = dtePlus.getTime();
                                break;
                            case "90 Days":
                                dtePlus.add(Calendar.DATE, 90);
                                x = 90;
                                dtePlusPlus = dtePlus.getTime();
                                break;
                            case "120 Days":
                                dtePlus.add(Calendar.DATE, 120);
                                x = 120;
                                dtePlusPlus = dtePlus.getTime();
                                break;
                            case "150 Days":
                                dtePlus.add(Calendar.DATE, 150);
                                x = 150;
                                dtePlusPlus = dtePlus.getTime();
                                break;
                            case "180 Days":
                                dtePlus.add(Calendar.DATE, 180);
                                x = 180;
                                dtePlusPlus = dtePlus.getTime();
                                break;
                        }
                        Debug.print("1---------------------");
                        Debug.print("invInventoryListCost.getCstExpiryDate()=" + invInventoryListCost.getCstExpiryDate());
                    }

                } catch (FinderException ex) {

                    QTY = 0d;
                    AMOUNT = 0d;
                    UNIT_COST = invInventoryList.getInvItemLocation().getInvItem().getIiUnitCost();
                    AVE_COST = invInventoryList.getInvItemLocation().getInvItem().getIiUnitCost();
                }

                if (invUnitOfMeasure != null && invUnitOfMeasure.getUomAdLvClass().equals(invInventoryList.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomAdLvClass())) {

                    // convert qty, amount, unit cost, ave cost

                    SALES_PRC = this.convertCostByUomToAndItem(invUnitOfMeasure, invInventoryList.getInvItemLocation().getInvItem(), SALES_PRC, AD_CMPNY);
                    AMOUNT = this.convertAmountByUomToAndItemAndQtyAndAveCost(invUnitOfMeasure, invInventoryList.getInvItemLocation().getInvItem(), QTY, AVE_COST, AD_CMPNY);
                    QTY = this.convertQuantityByUomToAndItem(invUnitOfMeasure, invInventoryList.getInvItemLocation().getInvItem(), QTY, AD_CMPNY);
                    UNIT_COST = this.convertCostByUomToAndItem(invUnitOfMeasure, invInventoryList.getInvItemLocation().getInvItem(), UNIT_COST, AD_CMPNY);
                    AVE_COST = this.convertCostByUomToAndItem(invUnitOfMeasure, invInventoryList.getInvItemLocation().getInvItem(), AVE_COST, AD_CMPNY);

                    details.setRilUnit(invUnitOfMeasure.getUomName());
                }

                details.setRilSalesPrice(SALES_PRC);
                details.setRilQuantity(QTY);
                details.setRilAmount(AMOUNT);
                details.setRilUnitCost(UNIT_COST);
                details.setRilAverageCost(AVE_COST);

                if (SHW_TGS) {
                    try {
                        int ITEM_LOCATION_CODE = invInventoryList.getInvItemLocation().getIlCode();
                        int AD_BRANCH_CODE = invInventoryList.getAdBranch().getBrCode();
                        Collection invTagsByLatestDate = invTagHome.findByLatestDate(AS_OF_DT, AD_CMPNY, 1);

                        Iterator h = invTagsByLatestDate.iterator();
                        Date TG_LATEST_DT = null;

                        String ITEM_DESCRIPTION = "";
                        String ITEM_CATEGORY = "";
                        int PL_CODE = 0;
                        int AL_CODE = 0;
                        Collection invTags = null;
                        while (h.hasNext()) {
                            LocalInvTag invTag = (LocalInvTag) h.next();
                            TG_LATEST_DT = invTag.getTgTransactionDate();

                            try {
                                Debug.print("1-PL_CODE");
                                PL_CODE = invTag.getApPurchaseOrderLine().getPlCode();
                                Debug.print("2-PL_CODE------>" + PL_CODE);

                            } catch (Exception ex) {
                            }
                            try {
                                Debug.print("1-AL_CODE");
                                AL_CODE = invTag.getInvAdjustmentLine().getAlCode();
                                Debug.print("2-AL_CODE------>" + AL_CODE);
                            } catch (Exception ex) {
                            }
                        }
                        Debug.print("TG_LATEST_DT------>" + TG_LATEST_DT);
                        Debug.print("ITEM_LOCATION_CODE------>" + ITEM_LOCATION_CODE);
                        Debug.print("AD_BRANCH_CODE------>" + AD_BRANCH_CODE);
                        Debug.print("AD_CMPNY------>" + AD_CMPNY);

                        if (PL_CODE > 0) {
                            invTags = invTagHome.findByLessThanOrEqualToTransactionDateAndItemLocationAndAdBranchFromPOLine(TG_LATEST_DT, ITEM_LOCATION_CODE, AD_BRANCH_CODE, AD_CMPNY);
                            Debug.print("PL_CODE Size=" + invTags.size());
                            if (invTags.size() == 0) {
                                continue;
                            }
                        }
                        if (AL_CODE > 0) {
                            invTags = invTagHome.findByLessThanOrEqualToTransactionDateAndItemLocationAndAdBranchFromAdjLine(TG_LATEST_DT, ITEM_LOCATION_CODE, AD_BRANCH_CODE, AD_CMPNY);
                            Debug.print("AL_CODE Size=" + invTags.size());
                            if (invTags.size() == 0) {
                                continue;
                            }
                        }
                        Iterator z = invTags.iterator();

                        if (invTags.size() > 0) {
                            while (z.hasNext()) {
                                LocalInvTag invTag = (LocalInvTag) z.next();
                                InvRepInventoryListDetails tgDetails = new InvRepInventoryListDetails();

                                if (invTag.getTgType().equals("IN")) {
                                    tgDetails.setRilPropertyCode(invTag.getTgPropertyCode());
                                    tgDetails.setRilSpecs(invTag.getTgSpecs());
                                    tgDetails.setRilSerialNumber(invTag.getTgSerialNumber());

                                    try {
                                        ITEM_DESCRIPTION = invTag.getApPurchaseOrderLine().getInvItemLocation().getInvItem().getIiDescription();
                                    } catch (Exception ex) {
                                    }
                                    try {
                                        ITEM_DESCRIPTION = invTag.getInvAdjustmentLine().getInvItemLocation().getInvItem().getIiDescription();
                                    } catch (Exception ex) {
                                    }
                                    tgDetails.setRilItemName(ITEM_DESCRIPTION);

                                    Debug.print(ITEM_DESCRIPTION + " <==item description");

                                    try {
                                        tgDetails.setRilCustodian(invTag.getAdUser().getUsrDescription());
                                    } catch (Exception ex) {
                                        tgDetails.setRilCustodian("");
                                    }

                                    Collection depreciationLedgers = invTag.getInvDepreciationLedgers();

                                    for (Object depreciationLedger : depreciationLedgers) {
                                        LocalInvDepreciationLedger invDepreciationLedger = (LocalInvDepreciationLedger) depreciationLedger;
                                        InvRepInventoryListDetails dlDetails = new InvRepInventoryListDetails();

                                        Debug.print("CAT=" + invDepreciationLedger.getInvTag().getApPurchaseOrderLine().getInvItemLocation().getInvItem().getIiAdLvCategory());
                                        dlDetails.setRilDlItemCategory(invDepreciationLedger.getInvTag().getApPurchaseOrderLine().getInvItemLocation().getInvItem().getIiAdLvCategory());
                                        dlDetails.setRilDlItemName(invDepreciationLedger.getInvTag().getApPurchaseOrderLine().getInvItemLocation().getInvItem().getIiDescription());
                                        dlDetails.setRilDlPropertyCode(invDepreciationLedger.getInvTag().getTgPropertyCode());
                                        Debug.print("SERIAL=" + invDepreciationLedger.getInvTag().getTgSerialNumber());
                                        dlDetails.setRilDlSerialNumber(invDepreciationLedger.getInvTag().getTgSerialNumber());
                                        dlDetails.setRilDlSpecs(invDepreciationLedger.getInvTag().getTgSpecs());
                                        dlDetails.setRilDlCustodian(invDepreciationLedger.getInvTag().getAdUser().getUsrName());
                                        dlDetails.setRilDlDate(EJBCommon.convertSQLDateToString(invDepreciationLedger.getDlDate()));
                                        dlDetails.setRilDlAcquisitionCost(invDepreciationLedger.getDlAcquisitionCost());
                                        dlDetails.setRilDlAmount(invDepreciationLedger.getDlDepreciationAmount());
                                        dlDetails.setRilDlCurrentBalance(invDepreciationLedger.getDlCurrentBalance());
                                        dlDetails.setRilDlMonthLifeSpan(invDepreciationLedger.getDlMonthLifeSpan());

                                        invInventoryListList.add(dlDetails);
                                    }

                                    invInventoryListList.add(tgDetails);
                                }
                            }

                        } else {

                            break;
                        }

                    } catch (FinderException ex) {
                        Debug.print("no record found?");
                        throw new GlobalNoRecordFoundException();
                    }
                }

                // get price levels
                if (!priceLevelList.isEmpty()) {

                    ArrayList priceLevels = new ArrayList();

                    for (Object o : priceLevelList) {

                        String PL_AD_LV_PRC_LVL = (String) o;

                        InvPriceLevelDetails pdetails = new InvPriceLevelDetails();

                        try {

                            LocalInvPriceLevel invPriceLevel = invPriceLevelHome.findByIiNameAndAdLvPriceLevel(invInventoryList.getInvItemLocation().getInvItem().getIiName(), PL_AD_LV_PRC_LVL, AD_CMPNY);

                            pdetails.setPlAdLvPriceLevel(invPriceLevel.getPlAdLvPriceLevel());
                            if (invUnitOfMeasure != null && invUnitOfMeasure.getUomAdLvClass().equals(invInventoryList.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomAdLvClass())) {
                                pdetails.setPlAmount(this.convertCostByUomToAndItem(invUnitOfMeasure, invInventoryList.getInvItemLocation().getInvItem(), invPriceLevel.getPlAmount(), AD_CMPNY));
                            } else {
                                pdetails.setPlAmount(invPriceLevel.getPlAmount());
                            }

                        } catch (FinderException ex) {

                            pdetails.setPlAdLvPriceLevel(PL_AD_LV_PRC_LVL);
                            pdetails.setPlAmount(0d);
                        }

                        priceLevels.add(pdetails);
                    }

                    details.setRilPriceLevels(priceLevels);
                }

                Integer brCode = invInventoryList.getAdBranch().getBrCode();

                if (SHW_CMMTTD_QNTTY) {

                    double committedQty = 0d;
                    double committedAmount = 0d;

                    Collection arSalesOrderLines = arSalesOrderLineHome.findCommittedQtyByIiNameAndLocNameAndWithoutDateAndSoAdBranch(invInventoryList.getInvItemLocation().getInvItem().getIiName(), invInventoryList.getInvItemLocation().getInvLocation().getLocName(), brCode, AD_CMPNY);

                    for (Object salesOrderLine : arSalesOrderLines) {
                        LocalArSalesOrderLine arSalesOrderLine = (LocalArSalesOrderLine) salesOrderLine;

                        GregorianCalendar gcDateFrom = new GregorianCalendar();
                        gcDateFrom.setTime(AS_OF_DT);
                        gcDateFrom.add(Calendar.MONTH, -1);

                        if (arSalesOrderLine.getArSalesOrderInvoiceLines().size() == 0 && arSalesOrderLine.getArSalesOrder().getSoDate().compareTo(AS_OF_DT) <= 0 && arSalesOrderLine.getArSalesOrder().getSoDate().compareTo(gcDateFrom.getTime()) >= 0) {

                            if (this.filterByOptionalCriteria(criteria, arSalesOrderLine.getInvItemLocation().getInvItem(), arSalesOrderLine.getInvItemLocation().getInvLocation())) {
                                continue;
                            }

                            if (arSalesOrderLine.getArSalesOrder().getSoOrderStatus().equals("Bad")) {
                                continue;
                            }

                            committedQty -= arSalesOrderLine.getSolQuantity();
                            committedAmount -= arSalesOrderLine.getSolAmount();
                        }
                    }

                    Collection arJobOrderLines = arJobOrderLineHome.findCommittedQtyByIiNameAndLocNameAndWithoutDateAndJoAdBranch(invInventoryList.getInvItemLocation().getInvItem().getIiName(), invInventoryList.getInvItemLocation().getInvLocation().getLocName(), brCode, AD_CMPNY);

                    for (Object jobOrderLine : arJobOrderLines) {
                        LocalArJobOrderLine arJobOrderLine = (LocalArJobOrderLine) jobOrderLine;

                        GregorianCalendar gcDateFrom = new GregorianCalendar();
                        gcDateFrom.setTime(AS_OF_DT);
                        gcDateFrom.add(Calendar.MONTH, -1);

                        if (arJobOrderLine.getArJobOrderInvoiceLines().size() == 0 && arJobOrderLine.getArJobOrder().getJoDate().compareTo(AS_OF_DT) <= 0 && arJobOrderLine.getArJobOrder().getJoDate().compareTo(gcDateFrom.getTime()) >= 0) {

                            if (this.filterByOptionalCriteria(criteria, arJobOrderLine.getInvItemLocation().getInvItem(), arJobOrderLine.getInvItemLocation().getInvLocation())) {
                                continue;
                            }

                            if (arJobOrderLine.getArJobOrder().getJoOrderStatus().equals("Bad")) {
                                continue;
                            }

                            committedQty -= arJobOrderLine.getJolQuantity();
                            committedAmount -= arJobOrderLine.getJolAmount();
                        }
                    }

                    details.setRilQuantity(details.getRilQuantity() + committedQty);
                    details.setRilAmount(details.getRilAmount() + committedAmount);
                }
                int miscList = 0;
                if (INCLD_UNPSTD) {

                    double unpostedQty = 0d;
                    double unpostedAmount = 0d;

                    locName = invInventoryList.getInvItemLocation().getInvLocation().getLocName();
                    locCode = invLocationHome.findByLocName(locName, AD_CMPNY).getLocCode();
                    String iiName = invInventoryList.getInvItemLocation().getInvItem().getIiName();
                    // INV_ADJUSTMENT_LINE
                    Collection invAdjustmentLines = invAdjustmentLineHome.findUnpostedAdjByIiNameAndLocNameAndLessEqualDateAndAdjAdBranch(iiName, locName, AS_OF_DT, brCode, AD_CMPNY);

                    Iterator unpstdIter = invAdjustmentLines.iterator();

                    while (unpstdIter.hasNext()) {

                        LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) unpstdIter.next();

                        LocalInvItemLocation invItemLocation = invAdjustmentLine.getInvItemLocation();
                        LocalInvItem invItem = invItemLocation.getInvItem();
                        LocalInvLocation invLocation = invItemLocation.getInvLocation();

                        if (this.filterByOptionalCriteria(criteria, invItem, invLocation) || !invItemLocation.equals(invInventoryList.getInvItemLocation()) || invAdjustmentLine.getInvAdjustment().getAdjDate().compareTo(AS_OF_DT) > 0) {
                            continue;
                        }

                        unpostedQty += invAdjustmentLine.getAlAdjustQuantity();
                        unpostedAmount += (invAdjustmentLine.getAlAdjustQuantity() * invAdjustmentLine.getAlUnitCost());

                        try {
                            Debug.print(invAdjustmentLine.getAlMisc());
                            int qty2Prpgt = 0;
                            if (invAdjustmentLine.getAlMisc().length() != 0) {
                                qty2Prpgt = Integer.parseInt(this.getQuantityExpiryDates(invAdjustmentLine.getAlMisc()));
                            }
                            if (invAdjustmentLine.getAlAdjustQuantity() > 0) {
                                Date dtePlusPlus = null;
                                Calendar dtePlus = Calendar.getInstance();
                                dtePlus.setTime(AS_OF_DT);

                                Calendar dtePlus2 = Calendar.getInstance();
                                dtePlus2.setTime(AS_OF_DT);
                                int x = 0;

                                switch (EXPRY_DT) {
                                    case "30 Days":
                                        dtePlus.add(Calendar.DATE, 30);
                                        x = 30;
                                        dtePlusPlus = dtePlus.getTime();
                                        break;
                                    case "60 Days":
                                        dtePlus.add(Calendar.DATE, 60);
                                        x = 60;
                                        dtePlusPlus = dtePlus.getTime();
                                        break;
                                    case "90 Days":
                                        dtePlus.add(Calendar.DATE, 90);
                                        x = 90;
                                        dtePlusPlus = dtePlus.getTime();
                                        break;
                                }

                                if (invAdjustmentLine.getAlMisc() != null && invAdjustmentLine.getAlMisc() != "" && invAdjustmentLine.getAlMisc().length() != 0) {
                                    Debug.print("2---------------------");
                                    miscList = miscList + this.expiryDates(invAdjustmentLine.getAlMisc(), qty2Prpgt, dtePlusPlus);
                                }
                            }
                        } catch (Exception ex) {

                        }
                    }

                    // INV_STOCK_TRANSFER_LINE OUT
                    // Collection invStockTransferLines =
                    // invStockTransferLineHome.findUnpostedStByLocCodeAndAdBranch(locCode, brCode, AD_CMPNY);

                    // INV_STOCK_TRANSFER_LINE IN
                    Collection invStockTransferInLines = invStockTransferLineHome.findUnpostedStlByIiNameAndLocCodeAndLessEqualDateAndStAdBranch(iiName, locCode, AS_OF_DT, brCode, AD_CMPNY);
                    unpstdIter = invStockTransferInLines.iterator();

                    while (unpstdIter.hasNext()) {

                        LocalInvStockTransferLine invStockTransferLine = (LocalInvStockTransferLine) unpstdIter.next();

                        LocalInvItem invItem = invStockTransferLine.getInvItem();
                        LocalInvLocation invLocation = invLocationHome.findByPrimaryKey(locCode);

                        if (this.filterByOptionalCriteria(criteria, invItem, invLocation) || invStockTransferLine.getInvStockTransfer().getStDate().compareTo(AS_OF_DT) > 0) {
                            continue;
                        }

                        if (invStockTransferLine.getStlLocationTo().equals(locName)) {
                            unpostedQty += invStockTransferLine.getStlQuantityDelivered();
                            unpostedAmount += invStockTransferLine.getStlAmount();
                        } else {
                            unpostedQty -= invStockTransferLine.getStlQuantityDelivered();
                            unpostedAmount -= invStockTransferLine.getStlAmount();
                        }
                    }

                    // INV_BRANCH_STOCK_TRANSFER_LINE
                    Collection invBranchStockTransferLines = invBranchStockTransferLineHome.findUnpostedBstByLocNameAndAdBranch(locName, brCode, AD_CMPNY);

                    unpstdIter = invBranchStockTransferLines.iterator();

                    while (unpstdIter.hasNext()) {

                        LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) unpstdIter.next();

                        LocalInvItemLocation invItemLocation = invBranchStockTransferLine.getInvItemLocation();
                        LocalInvItem invItem = invItemLocation.getInvItem();
                        LocalInvLocation invLocation = invItemLocation.getInvLocation();

                        if (this.filterByOptionalCriteria(criteria, invItem, invLocation) || !invItemLocation.equals(invInventoryList.getInvItemLocation()) || invBranchStockTransferLine.getInvBranchStockTransfer().getBstDate().compareTo(AS_OF_DT) > 0) {
                            continue;
                        }

                        unpostedQty += invBranchStockTransferLine.getBslQuantityReceived();
                        unpostedAmount += invBranchStockTransferLine.getBslAmount();

                        try {
                            int qty2Prpgt = 0;
                            if (invBranchStockTransferLine.getBslMisc().length() != 0) {
                                qty2Prpgt = Integer.parseInt(this.getQuantityExpiryDates(invBranchStockTransferLine.getBslMisc()));
                            }
                            if (invBranchStockTransferLine.getBslQuantityReceived() > 0) {
                                Date dtePlusPlus = null;
                                Calendar dtePlus = Calendar.getInstance();
                                dtePlus.setTime(AS_OF_DT);

                                Calendar dtePlus2 = Calendar.getInstance();
                                dtePlus2.setTime(AS_OF_DT);
                                int x = 0;

                                switch (EXPRY_DT) {
                                    case "30 Days":
                                        dtePlus.add(Calendar.DATE, 30);
                                        x = 30;
                                        dtePlusPlus = dtePlus.getTime();
                                        break;
                                    case "60 Days":
                                        dtePlus.add(Calendar.DATE, 60);
                                        x = 60;
                                        dtePlusPlus = dtePlus.getTime();
                                        break;
                                    case "90 Days":
                                        dtePlus.add(Calendar.DATE, 90);
                                        x = 90;
                                        dtePlusPlus = dtePlus.getTime();
                                        break;
                                }

                                if (invBranchStockTransferLine.getBslMisc() != null && invBranchStockTransferLine.getBslMisc() != "" && invBranchStockTransferLine.getBslMisc().length() != 0) {

                                    Debug.print("5---------------------");
                                    miscList = miscList + this.expiryDates(invBranchStockTransferLine.getBslMisc(), qty2Prpgt, dtePlusPlus);
                                }
                            }
                        } catch (Exception ex) {

                        }
                    }

                    // AP_VOUCHER_LINE_ITEM
                    // a) apVoucher
                    Collection apVoucherLineItems = apVoucherLineItemHome.findUnpostedVouByIiNameAndLocNameAndLessEqualDateAndVouAdBranch(iiName, locName, AS_OF_DT, brCode, AD_CMPNY);

                    unpstdIter = apVoucherLineItems.iterator();

                    while (unpstdIter.hasNext()) {

                        LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) unpstdIter.next();

                        LocalInvItemLocation invItemLocation = apVoucherLineItem.getInvItemLocation();
                        LocalInvItem invItem = invItemLocation.getInvItem();
                        LocalInvLocation invLocation = invItemLocation.getInvLocation();

                        if (this.filterByOptionalCriteria(criteria, invItem, invLocation) || !invItemLocation.equals(invInventoryList.getInvItemLocation()) || apVoucherLineItem.getApVoucher().getVouDate().compareTo(AS_OF_DT) > 0) {
                            continue;
                        }

                        unpostedQty += apVoucherLineItem.getVliQuantity();
                        unpostedAmount += apVoucherLineItem.getVliAmount();

                        try {
                            int qty2Prpgt = 0;

                            if (apVoucherLineItem.getVliMisc().length() != 0) {
                                qty2Prpgt = Integer.parseInt(this.getQuantityExpiryDates(apVoucherLineItem.getVliMisc()));
                            }
                            Date dtePlusPlus = null;
                            Calendar dtePlus = Calendar.getInstance();
                            dtePlus.setTime(AS_OF_DT);

                            Calendar dtePlus2 = Calendar.getInstance();
                            dtePlus2.setTime(AS_OF_DT);
                            int x = 0;

                            switch (EXPRY_DT) {
                                case "30 Days":
                                    dtePlus.add(Calendar.DATE, 30);
                                    x = 30;
                                    dtePlusPlus = dtePlus.getTime();
                                    break;
                                case "60 Days":
                                    dtePlus.add(Calendar.DATE, 60);
                                    x = 60;
                                    dtePlusPlus = dtePlus.getTime();
                                    break;
                                case "90 Days":
                                    dtePlus.add(Calendar.DATE, 90);
                                    x = 90;
                                    dtePlusPlus = dtePlus.getTime();
                                    break;
                            }

                            if (apVoucherLineItem.getVliMisc() != null && apVoucherLineItem.getVliMisc() != "" && apVoucherLineItem.getVliMisc().length() != 0) {
                                Debug.print("6---------------------");
                                miscList = miscList + this.expiryDates(apVoucherLineItem.getVliMisc(), qty2Prpgt, dtePlusPlus);
                            }

                        } catch (Exception ex) {

                        }
                    }

                    // b) apCheck

                    apVoucherLineItems = apVoucherLineItemHome.findUnpostedChkByIiNameAndLocNameAndLessEqualDateAndChkAdBranch(iiName, locName, AS_OF_DT, brCode, AD_CMPNY);

                    unpstdIter = apVoucherLineItems.iterator();

                    while (unpstdIter.hasNext()) {

                        LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) unpstdIter.next();

                        LocalInvItemLocation invItemLocation = apVoucherLineItem.getInvItemLocation();
                        LocalInvItem invItem = invItemLocation.getInvItem();
                        LocalInvLocation invLocation = invItemLocation.getInvLocation();
                        Debug.print("CHKL" + "\t Item: " + invInventoryList.getInvItemLocation().getInvItem().getIiName() + "\t" + "Loc: " + invInventoryList.getInvItemLocation().getInvLocation().getLocName());
                        if (this.filterByOptionalCriteria(criteria, invItem, invLocation) || !invItemLocation.equals(invInventoryList.getInvItemLocation()) || apVoucherLineItem.getApCheck().getChkDate().compareTo(AS_OF_DT) > 0) {
                            continue;
                        }

                        unpostedQty += apVoucherLineItem.getVliQuantity();
                        unpostedAmount += apVoucherLineItem.getVliAmount();
                        try {
                            int qty2Prpgt = 0;
                            if (apVoucherLineItem.getVliMisc().length() != 0) {
                                qty2Prpgt = Integer.parseInt(this.getQuantityExpiryDates(apVoucherLineItem.getVliMisc()));
                            }
                            Date dtePlusPlus = null;
                            Calendar dtePlus = Calendar.getInstance();
                            dtePlus.setTime(AS_OF_DT);

                            Calendar dtePlus2 = Calendar.getInstance();
                            dtePlus2.setTime(AS_OF_DT);
                            int x = 0;

                            switch (EXPRY_DT) {
                                case "30 Days":
                                    dtePlus.add(Calendar.DATE, 30);
                                    x = 30;
                                    dtePlusPlus = dtePlus.getTime();
                                    break;
                                case "60 Days":
                                    dtePlus.add(Calendar.DATE, 60);
                                    x = 60;
                                    dtePlusPlus = dtePlus.getTime();
                                    break;
                                case "90 Days":
                                    dtePlus.add(Calendar.DATE, 90);
                                    x = 90;
                                    dtePlusPlus = dtePlus.getTime();
                                    break;
                            }

                            if (apVoucherLineItem.getVliMisc() != null && apVoucherLineItem.getVliMisc() != "" && apVoucherLineItem.getVliMisc().length() != 0) {
                                Debug.print("7---------------------");
                                miscList = miscList + this.expiryDates(apVoucherLineItem.getVliMisc(), qty2Prpgt, dtePlusPlus);
                            }

                        } catch (Exception ex) {

                        }
                    }

                    // AR_INVOICE_LINE_ITEM
                    // a) arInvoice

                    Collection arInvoiceLineItems = arInvoiceLineItemHome.findUnpostedInvcByIiNameAndLocNameAndLessEqualDateAndInvAdBranch(iiName, locName, AS_OF_DT, brCode, AD_CMPNY);

                    unpstdIter = arInvoiceLineItems.iterator();

                    while (unpstdIter.hasNext()) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) unpstdIter.next();

                        LocalInvItemLocation invItemLocation = arInvoiceLineItem.getInvItemLocation();
                        LocalInvItem invItem = invItemLocation.getInvItem();
                        LocalInvLocation invLocation = invItemLocation.getInvLocation();

                        if (this.filterByOptionalCriteria(criteria, invItem, invLocation) || !invItemLocation.equals(invInventoryList.getInvItemLocation()) || arInvoiceLineItem.getArInvoice().getInvDate().compareTo(AS_OF_DT) > 0) {
                            continue;
                        }

                        unpostedQty -= arInvoiceLineItem.getIliQuantity();
                        unpostedAmount -= arInvoiceLineItem.getIliAmount();
                    }

                    // b) arReceipt
                    arInvoiceLineItems = arInvoiceLineItemHome.findUnpostedRctByIiNameAndLocNameAndLessEqualDateAndRctAdBranch(iiName, locName, AS_OF_DT, brCode, AD_CMPNY);

                    unpstdIter = arInvoiceLineItems.iterator();

                    while (unpstdIter.hasNext()) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) unpstdIter.next();

                        LocalInvItemLocation invItemLocation = arInvoiceLineItem.getInvItemLocation();
                        LocalInvItem invItem = invItemLocation.getInvItem();
                        LocalInvLocation invLocation = invItemLocation.getInvLocation();

                        if (this.filterByOptionalCriteria(criteria, invItem, invLocation) || !invItemLocation.equals(invInventoryList.getInvItemLocation()) || arInvoiceLineItem.getArReceipt().getRctDate().compareTo(AS_OF_DT) > 0) {
                            continue;
                        }

                        unpostedQty -= arInvoiceLineItem.getIliQuantity();
                        unpostedAmount -= arInvoiceLineItem.getIliAmount();
                    }

                    // AP_PURCHASE_ORDER_LINE
                    Collection apPurchaseOrderLines = apPurchaseOrderLineHome.findUnpostedPoByIiNameAndLocNameAndLessEqualDateAndPoAdBranch(iiName, locName, AS_OF_DT, brCode, AD_CMPNY);

                    unpstdIter = apPurchaseOrderLines.iterator();

                    while (unpstdIter.hasNext()) {

                        LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) unpstdIter.next();

                        LocalInvItemLocation invItemLocation = apPurchaseOrderLine.getInvItemLocation();
                        LocalInvItem invItem = invItemLocation.getInvItem();
                        LocalInvLocation invLocation = invItemLocation.getInvLocation();

                        if (this.filterByOptionalCriteria(criteria, invItem, invLocation) || !invItemLocation.equals(invInventoryList.getInvItemLocation()) || apPurchaseOrderLine.getApPurchaseOrder().getPoDate().compareTo(AS_OF_DT) > 0) {
                            continue;
                        }

                        unpostedQty += apPurchaseOrderLine.getPlQuantity();
                        unpostedAmount += apPurchaseOrderLine.getPlAmount();

                        int qty2Prpgt = 0;
                        try {

                            if (apPurchaseOrderLine.getPlMisc().length() != 0 || apPurchaseOrderLine.getPlMisc().equals(null)) {
                                qty2Prpgt = Integer.parseInt(this.getQuantityExpiryDates(apPurchaseOrderLine.getPlMisc()));
                            }

                            Date dtePlusPlus = null;
                            Calendar dtePlus = Calendar.getInstance();
                            dtePlus.setTime(AS_OF_DT);

                            Calendar dtePlus2 = Calendar.getInstance();
                            dtePlus2.setTime(AS_OF_DT);
                            int x = 0;

                            switch (EXPRY_DT) {
                                case "30 Days":
                                    dtePlus.add(Calendar.DATE, 30);
                                    x = 30;
                                    dtePlusPlus = dtePlus.getTime();
                                    break;
                                case "60 Days":
                                    dtePlus.add(Calendar.DATE, 60);
                                    x = 60;
                                    dtePlusPlus = dtePlus.getTime();
                                    break;
                                case "90 Days":
                                    dtePlus.add(Calendar.DATE, 90);
                                    x = 90;
                                    dtePlusPlus = dtePlus.getTime();
                                    break;
                            }

                            if (apPurchaseOrderLine.getPlMisc() != null && apPurchaseOrderLine.getPlMisc() != "" && apPurchaseOrderLine.getPlMisc().length() != 0) {
                                Debug.print("8---------------------");
                                miscList = miscList + this.expiryDates(apPurchaseOrderLine.getPlMisc(), qty2Prpgt, dtePlusPlus);
                            }

                        } catch (Exception ex) {

                        }
                    }

                    // AR_SALES_ORDER_INVOICE_LINE
                    Collection arSalesOrderInvoiceLines = arSalesOrderInvoiceLineHome.findUnpostedInvcByIiNameAndLocNameAndLessEqualDateAndInvAdBranch(iiName, locName, AS_OF_DT, brCode, AD_CMPNY);

                    unpstdIter = arSalesOrderInvoiceLines.iterator();

                    while (unpstdIter.hasNext()) {

                        LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) unpstdIter.next();

                        LocalInvItemLocation invItemLocation = arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation();
                        LocalInvItem invItem = invItemLocation.getInvItem();
                        LocalInvLocation invLocation = invItemLocation.getInvLocation();

                        if (this.filterByOptionalCriteria(criteria, invItem, invLocation) || !invItemLocation.equals(invInventoryList.getInvItemLocation()) || arSalesOrderInvoiceLine.getArInvoice().getInvDate().compareTo(AS_OF_DT) > 0) {
                            continue;
                        }

                        unpostedQty -= arSalesOrderInvoiceLine.getSilQuantityDelivered();
                        unpostedAmount -= arSalesOrderInvoiceLine.getSilAmount();
                    }

                    // AR_JOB_ORDER_INVOICE_LINE
                    Collection arJobOrderInvoiceLines = arJobOrderInvoiceLineHome.findUnpostedInvcByIiNameAndLocNameAndLessEqualDateAndInvAdBranch(iiName, locName, AS_OF_DT, brCode, AD_CMPNY);

                    unpstdIter = arJobOrderInvoiceLines.iterator();

                    while (unpstdIter.hasNext()) {

                        LocalArJobOrderInvoiceLine arJobOrderInvoiceLine = (LocalArJobOrderInvoiceLine) unpstdIter.next();

                        LocalInvItemLocation invItemLocation = arJobOrderInvoiceLine.getArJobOrderLine().getInvItemLocation();
                        LocalInvItem invItem = invItemLocation.getInvItem();
                        LocalInvLocation invLocation = invItemLocation.getInvLocation();

                        if (this.filterByOptionalCriteria(criteria, invItem, invLocation) || !invItemLocation.equals(invInventoryList.getInvItemLocation()) || arJobOrderInvoiceLine.getArInvoice().getInvDate().compareTo(AS_OF_DT) > 0) {
                            continue;
                        }

                        unpostedQty -= arJobOrderInvoiceLine.getJilQuantityDelivered();
                        unpostedAmount -= arJobOrderInvoiceLine.getJilAmount();
                    }

                    details.setRilQuantity(details.getRilQuantity() + unpostedQty);
                    details.setRilAmount(details.getRilAmount() + unpostedAmount);
                }

                if ((details.getRilQuantity() > 0) || (INCLD_ZRS && (details.getRilQuantity() <= 0))) {
                    int combineMiscNumber2 = 0;
                    combineMiscNumber2 = miscList2 + miscList;
                    String combineMiscNumber = Integer.toString(combineMiscNumber2);
                    if (combineMiscNumber2 != 0) {
                        details.setRilExpiryDate(combineMiscNumber);
                    }
                    if (!SHW_TGS) {
                        Debug.print("ng print p dn?");
                        invInventoryListList.add(details);
                    }
                }
            }

            if (invInventoryListList.isEmpty()) {
                Debug.print("GlobalNoRecordFoundException");
                throw new GlobalNoRecordFoundException();
            } else {
                return invInventoryListList;
            }
        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public String getQuantityExpiryDates(String qntty) {

        String separator = "$";

        // Remove first $ character
        qntty = qntty.substring(1);

        // Counter
        int start = 0;
        int nextIndex = qntty.indexOf(separator, start);
        int length = nextIndex - start;
        String y;
        y = (qntty.substring(start, start + length));
        // Debug.print("Y " + y);

        return y;
    }

    private int expiryDates(String misc, double qty, Date dateUsed) throws Exception {

        Debug.print("ApReceivingItemControllerBean getExpiryDates");
        // Debug.print("misc: " + misc);
        String separator = "$";

        Calendar dtePlus = Calendar.getInstance();
        dtePlus.setTime(dateUsed);

        // Remove first $ character
        misc = misc.substring(1);

        // Counter
        int start = 0;
        int nextIndex = misc.indexOf(separator, start);
        int length = nextIndex - start;

        // Debug.print("qty" + qty);
        ArrayList miscList = new ArrayList();
        int returnNumberDate = 0;
        for (int x = 0; x < qty; x++) {

            // Date
            start = nextIndex + 1;
            nextIndex = misc.indexOf(separator, start);
            length = nextIndex - start;
            String checker = misc.substring(start, start + length);
            // Debug.print("checker: "+checker);
            try {
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                // Get Date 1
                Date d1 = df.parse(checker);
                if (dateUsed.after(d1) || dateUsed.equals(d1)) {
                    if (checker.length() != 0 || checker != "null") {
                        miscList.add(checker);
                        returnNumberDate++;
                    }
                }
            } catch (Exception ex) {

            }
        }

        // Debug.print("miscList :" + returnNumberDate);
        return returnNumberDate;
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("InvRepInventoryListControllerBean getAdCompany");

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

    public ArrayList getInvUomAll(Integer AD_CMPNY) {

        Debug.print("InvRepInventoryListControllerBean getInvUomAll");
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

        Debug.print("InvRepInventoryListControllerBean convertQuantityByUomToAndItem");
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

        Debug.print("InvRepInventoryListControllerBean convertCostByUomToAndItem");
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

        Debug.print("InvRepInventoryListControllerBean convertAmountByUomToAndItemAndQtyAndAveCost");
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

    private boolean filterByOptionalCriteria(HashMap criteria, LocalInvItem invItem, LocalInvLocation invLocation) {

        Debug.print("InvRepItemCostingControllerBean filterByOptionalCriteria");
        try {

            String itemName = "";
            String itemClass = "";
            String itemCategory = "";
            String locationType = "";
            String location = "";

            if (criteria.containsKey("itemName")) {
                itemName = (String) criteria.get("itemName");
            }

            if (criteria.containsKey("itemClass")) {
                itemClass = (String) criteria.get("itemClass");
            }

            if (criteria.containsKey("itemCategory")) {
                itemCategory = (String) criteria.get("itemCategory");
            }

            if (criteria.containsKey("locationType")) {
                locationType = (String) criteria.get("locationType");
            }

            if (criteria.containsKey("location")) {
                location = (String) criteria.get("location");
            }

            if (!itemName.equals("") && !invItem.getIiName().toUpperCase().contains(itemName.toUpperCase())) {
                return true;
            }

            if (!itemClass.equals("") && !invItem.getIiClass().equals(itemClass)) {
                return true;
            }

            if (!itemCategory.equals("") && !invItem.getIiAdLvCategory().equals(itemCategory)) {
                return true;
            }

            if (!locationType.equals("") && !invLocation.getLocLvType().equals(locationType)) {
                return true;
            }

            return !location.equals("") && !invLocation.getLocName().equals(location);

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("InvRepInventoryListControllerBean ejbCreate");
    }

}