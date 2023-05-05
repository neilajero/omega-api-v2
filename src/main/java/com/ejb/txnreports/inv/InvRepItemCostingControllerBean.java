/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class InvRepItemCostingControllerBean
 * @created
 * @author
 */
package com.ejb.txnreports.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.dao.ad.LocalAdLookUpValueHome;
import com.ejb.dao.ap.LocalApPurchaseOrderLineHome;
import com.ejb.dao.ap.LocalApVoucherLineItemHome;
import com.ejb.dao.ar.LocalArInvoiceLineItemHome;
import com.ejb.dao.ar.LocalArSalesOrderInvoiceLineHome;
import com.ejb.dao.ar.LocalArSalesOrderLineHome;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdLookUpValue;
import com.ejb.entities.ap.LocalApPurchaseOrderLine;
import com.ejb.entities.ap.LocalApVoucherLineItem;
import com.ejb.entities.ar.LocalArInvoiceLineItem;
import com.ejb.entities.ar.LocalArSalesOrderInvoiceLine;
import com.ejb.entities.ar.LocalArSalesOrderLine;
import com.ejb.entities.inv.*;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.ad.AdBranchDetails;
import com.util.reports.inv.InvRepItemCostingDetails;

import jakarta.ejb.*;
import java.util.*;

@Stateless(name = "InvRepItemCostingControllerEJB")
public class InvRepItemCostingControllerBean extends EJBContextClass implements InvRepItemCostingController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalArSalesOrderLineHome arSalesOrderLineHome;
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
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalAdBranchHome adBranchHome;

    public ArrayList getAdLvInvItemCategoryAll(Integer AD_CMPNY) {

        Debug.print("InvRepItemCostingControllerBean getAdLvInvItemCategoryAll");

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

        Debug.print("InvRepItemCostingControllerBean getInvLocAll");

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

    public ArrayList executeInvRepItemCosting(HashMap criteria, String LCTN, String CTGRY, boolean SHW_CMMTTD_QNTTY,
                                              boolean INCLD_UNPSTD, boolean SHW_ZRS, ArrayList branchList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvRepItemCostingControllerBean executeInvRepItemCosting");

        ArrayList list = new ArrayList();

        try {

            Integer brCode = null;
            Integer locCode = null;
            String locName = "";

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(cst) FROM InvCosting cst WHERE (");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = 0;

            Object[] obj = null;

            if (branchList.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }

            Iterator brIter = branchList.iterator();

            AdBranchDetails bdetails = (AdBranchDetails) brIter.next();
            jbossQl.append("cst.cstAdBranch=").append(bdetails.getBrCode());

            while (brIter.hasNext()) {

                bdetails = (AdBranchDetails) brIter.next();

                jbossQl.append(" OR cst.cstAdBranch=").append(bdetails.getBrCode());
            }

            jbossQl.append(") ");

            firstArgument = false;

            // Allocate the size of the object parameter

            if (criteria.containsKey("category")) {

                criteriaSize++;
            }

            if (criteria.containsKey("location")) {

                criteriaSize++;
                locName = (String) criteria.get("location");
            }

            if (criteria.containsKey("dateFrom")) {

                criteriaSize++;
            }

            if (criteria.containsKey("dateTo")) {

                criteriaSize++;
            }

            if (criteria.containsKey("itemClass")) {

                criteriaSize++;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("itemName")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.invItemLocation.invItem.iiName  LIKE '%").append(criteria.get("itemName")).append("%' ");
            }

            if (criteria.containsKey("category")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.invItemLocation.invItem.iiAdLvCategory=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("category");
                ctr++;
            }

            if (criteria.containsKey("location")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.invItemLocation.invLocation.locName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("location");
                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.cstDate>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateFrom");
                ctr++;
            }

            if (criteria.containsKey("dateTo")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.cstDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (criteria.containsKey("itemClass")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.invItemLocation.invItem.iiClass=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("itemClass");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("cst.cstAdCompany=").append(AD_CMPNY).append(" ");

            jbossQl.append("ORDER BY cst.invItemLocation.invItem.iiName, cst.cstDate, cst.cstDateToLong, cst.cstLineNumber");

            Collection invCostings = invCostingHome.getCstByCriteria(jbossQl.toString(), obj, 0, 0);

            for (Object costing : invCostings) {

                LocalInvCosting invCosting = (LocalInvCosting) costing;

                InvRepItemCostingDetails details = new InvRepItemCostingDetails();
                details.setIcItemName(invCosting.getInvItemLocation().getInvItem().getIiName() + "-" + invCosting.getInvItemLocation().getInvItem().getIiDescription());
                details.setIcItemCategory(invCosting.getInvItemLocation().getInvItem().getIiAdLvCategory());
                details.setIcItemUnitOfMeasure(invCosting.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName());
                details.setIcItemLocation(invCosting.getInvItemLocation().getInvLocation().getLocName());
                details.setIcDate(invCosting.getCstDate());
                details.setIcItemLocation1(LCTN);
                details.setIcItemCategory1(CTGRY);

                // beginning
                LocalInvCosting invBeginningCosting = this.getCstIlBeginningBalanceByItemLocationAndDate(invCosting.getInvItemLocation(), (Date) criteria.get("dateFrom"), brCode, AD_CMPNY);
                if (invBeginningCosting != null) {

                    details.setIcBeginningQuantity(invBeginningCosting.getCstRemainingQuantity());
                    details.setIcBeginningUnitCost(invBeginningCosting.getCstRemainingValue() / invBeginningCosting.getCstRemainingQuantity());
                    details.setIcBeginningValue(invBeginningCosting.getCstRemainingValue());
                }

                // get item cost
                double ITEM_COST = EJBCommon.roundIt(invCosting.getCstItemCost() / invCosting.getCstQuantityReceived(), this.getGlFcPrecisionUnit(AD_CMPNY));
                details.setIcItemCost(ITEM_COST);
                details.setIcActualCost(invCosting.getCstItemCost());

                details.setIcQuantityReceived(invCosting.getCstQuantityReceived());
                details.setIcAdjustQuantity(invCosting.getCstAdjustQuantity());
                details.setIcAdjustCost(invCosting.getCstAdjustCost());
                details.setIcQuantitySold(invCosting.getCstQuantitySold());
                details.setIcCostOfSales(invCosting.getCstCostOfSales());
                details.setIcRemainingQuantity(invCosting.getCstRemainingQuantity());
                details.setIcRemainingValue(invCosting.getCstRemainingValue());
                details.setIcRemainingUnitCost(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                if (invCosting.getInvAdjustmentLine() != null) {

                    details.setIcSourceDocumentNumber(invCosting.getInvAdjustmentLine().getInvAdjustment().getAdjDocumentNumber());
                    details.setIcReferenceNumber(invCosting.getInvAdjustmentLine().getInvAdjustment().getAdjReferenceNumber());
                    details.setIcSourceDocument("INV");

                } else if (invCosting.getInvStockTransferLine() != null) {

                    details.setIcSourceDocumentNumber(invCosting.getInvStockTransferLine().getInvStockTransfer().getStDocumentNumber());
                    details.setIcReferenceNumber(invCosting.getInvStockTransferLine().getInvStockTransfer().getStReferenceNumber());
                    details.setIcSourceDocument("INV");

                } else if (invCosting.getInvBranchStockTransferLine() != null) {

                    details.setIcSourceDocumentNumber(invCosting.getInvBranchStockTransferLine().getInvBranchStockTransfer().getBstNumber());
                    details.setIcReferenceNumber(invCosting.getInvBranchStockTransferLine().getInvBranchStockTransfer().getBstTransferOutNumber());
                    details.setIcSourceDocument("INV");

                } else if (invCosting.getApVoucherLineItem() != null) {

                    if (invCosting.getApVoucherLineItem().getApVoucher() != null) {

                        details.setIcSourceDocumentNumber(invCosting.getApVoucherLineItem().getApVoucher().getVouDocumentNumber());
                        details.setIcReferenceNumber(invCosting.getApVoucherLineItem().getApVoucher().getVouReferenceNumber());
                        details.setIcSourceDocument("AP");

                    } else if (invCosting.getApVoucherLineItem().getApCheck() != null) {

                        details.setIcSourceDocumentNumber(invCosting.getApVoucherLineItem().getApCheck().getChkDocumentNumber());
                        details.setIcReferenceNumber(invCosting.getApVoucherLineItem().getApCheck().getChkNumber());
                        details.setIcSourceDocument("AP");
                    }

                } else if (invCosting.getArInvoiceLineItem() != null) {

                    if (invCosting.getArInvoiceLineItem().getArInvoice() != null) {

                        details.setIcSourceDocumentNumber(invCosting.getArInvoiceLineItem().getArInvoice().getInvNumber());
                        details.setIcReferenceNumber(invCosting.getArInvoiceLineItem().getArInvoice().getInvReferenceNumber());
                        details.setIcSourceDocument("AR");

                    } else if (invCosting.getArInvoiceLineItem().getArReceipt() != null) {

                        details.setIcSourceDocumentNumber(invCosting.getArInvoiceLineItem().getArReceipt().getRctNumber());
                        details.setIcReferenceNumber(invCosting.getArInvoiceLineItem().getArReceipt().getRctReferenceNumber());
                        details.setIcSourceDocument("AR");
                    }

                } else if (invCosting.getApPurchaseOrderLine() != null) {

                    details.setIcSourceDocumentNumber(invCosting.getApPurchaseOrderLine().getApPurchaseOrder().getPoDocumentNumber());
                    details.setIcReferenceNumber(invCosting.getApPurchaseOrderLine().getApPurchaseOrder().getPoReferenceNumber());
                    details.setIcSourceDocument("AP");

                } else if (invCosting.getArSalesOrderInvoiceLine() != null) {

                    details.setIcSourceDocumentNumber(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getInvNumber());
                    details.setIcReferenceNumber(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getInvNumber());
                    details.setIcSourceDocument("AR");
                }

                double COST = 0d;

                if (invCosting.getCstQuantityReceived() != 0d) {

                    if (invCosting.getCstQuantityReceived() > 0) {

                        details.setIcInQuantity(invCosting.getCstQuantityReceived());
                        details.setIcInAmount(invCosting.getCstItemCost());
                        COST = Math.abs(invCosting.getCstItemCost() / invCosting.getCstQuantityReceived());
                        details.setIcInUnitCost(COST);

                    } else if (invCosting.getCstQuantityReceived() < 0) {

                        details.setIcOutQuantity(invCosting.getCstQuantityReceived() * -1);
                        details.setIcOutAmount(invCosting.getCstItemCost() * -1);
                        COST = Math.abs(invCosting.getCstItemCost() / invCosting.getCstQuantityReceived());
                        details.setIcOutUnitCost(COST);
                    }

                } else if (invCosting.getCstAdjustQuantity() != 0d) {

                    if (invCosting.getCstAdjustQuantity() > 0) {

                        details.setIcInQuantity(invCosting.getCstAdjustQuantity());
                        details.setIcInAmount(invCosting.getCstAdjustCost());
                        COST = Math.abs(invCosting.getCstAdjustCost() / invCosting.getCstAdjustQuantity());
                        details.setIcInUnitCost(COST);

                    } else if (invCosting.getCstAdjustQuantity() < 0) {

                        details.setIcOutQuantity(invCosting.getCstAdjustQuantity() * -1);
                        details.setIcOutAmount(invCosting.getCstAdjustCost() * -1);
                        COST = Math.abs(invCosting.getCstAdjustCost() / invCosting.getCstAdjustQuantity());
                        details.setIcOutUnitCost(COST);
                    }

                } else if (invCosting.getCstQuantitySold() != 0d) {

                    if (invCosting.getCstQuantitySold() > 0) {

                        details.setIcOutQuantity(invCosting.getCstQuantitySold());
                        details.setIcOutAmount(invCosting.getCstCostOfSales());
                        COST = Math.abs(invCosting.getCstCostOfSales() / invCosting.getCstQuantitySold());
                        details.setIcOutUnitCost(COST);

                    } else if (invCosting.getCstQuantitySold() < 0) {

                        details.setIcInQuantity(invCosting.getCstQuantitySold() * -1);
                        details.setIcInAmount(invCosting.getCstCostOfSales() * -1);
                        COST = Math.abs(invCosting.getCstCostOfSales() / invCosting.getCstQuantitySold());
                        details.setIcInUnitCost(COST);
                    }
                }

                list.add(details);
                // Collections.sort(list, InvRepItemCostingDetails.ItemCostingComparator);
            }

            // start

            if (SHW_CMMTTD_QNTTY) {

                Collection arSalesOrderLines = arSalesOrderLineHome.findOpenSolAll(AD_CMPNY);

                for (Object salesOrderLine : arSalesOrderLines) {

                    LocalArSalesOrderLine arSalesOrderLine = (LocalArSalesOrderLine) salesOrderLine;
                    InvRepItemCostingDetails details = new InvRepItemCostingDetails();

                    if (arSalesOrderLine.getArSalesOrderInvoiceLines().size() == 0 && arSalesOrderLine.getArSalesOrder().getSoAdBranch().equals(brCode) && arSalesOrderLine.getInvItemLocation().getInvLocation().getLocName().equals(locName)) {

                        Date earliestDate = null;
                        Date soDate = arSalesOrderLine.getArSalesOrder().getSoDate();
                        LocalInvItem invItem = arSalesOrderLine.getInvItemLocation().getInvItem();

                        LocalInvCosting invBeginningCosting = this.getCstIlBeginningBalanceByItemLocationAndDate(arSalesOrderLine.getInvItemLocation(), (Date) criteria.get("dateFrom"), brCode, AD_CMPNY);
                        if (invBeginningCosting != null) {

                            details.setIcBeginningQuantity(invBeginningCosting.getCstRemainingQuantity());
                            details.setIcBeginningUnitCost(invBeginningCosting.getCstRemainingValue() / invBeginningCosting.getCstRemainingQuantity());
                            details.setIcBeginningValue(invBeginningCosting.getCstRemainingValue());
                            earliestDate = invBeginningCosting.getCstDate();
                        }

                        GregorianCalendar gcDateFrom = new GregorianCalendar();
                        Date dateTo = (Date) criteria.get("dateTo");
                        if (dateTo == null) {
                            dateTo = new Date();
                        }
                        Date soDateFrom = null;
                        gcDateFrom.setTime(dateTo != null ? dateTo : new Date());
                        gcDateFrom.add(Calendar.MONTH, -1);

                        soDateFrom = gcDateFrom.getTime();

                        if (soDate.compareTo(soDateFrom) < 0) {
                            continue;
                        }

                        if (dateTo != null && soDate.compareTo(dateTo) > 0) {
                            continue;
                        }

                        if (this.filterByOptionalCriteria(criteria, invItem, soDate, earliestDate)) {
                            continue;
                        }

                        details.setIcItemName(invItem.getIiName() + "-" + invItem.getIiDescription());
                        details.setIcDate(soDate);
                        details.setIcQuantitySold(arSalesOrderLine.getSolQuantity());
                        details.setIcCostOfSales(arSalesOrderLine.getSolAmount());
                        details.setIcSourceDocument("SO");
                        details.setIcSourceDocumentNumber(arSalesOrderLine.getArSalesOrder().getSoDocumentNumber());
                        details.setIcReferenceNumber(arSalesOrderLine.getArSalesOrder().getSoReferenceNumber());
                        details.setIcItemCategory(invItem.getIiAdLvCategory());
                        details.setIcItemUnitOfMeasure(invItem.getInvUnitOfMeasure().getUomName());
                        details.setIcItemLocation(invBeginningCosting.getInvItemLocation().getInvLocation().getLocName());
                        details.setIcItemLocation1(LCTN);
                        details.setIcItemCategory1(CTGRY);

                        list.add(details);
                    }
                }
                // Collections.sort(list, InvRepItemCostingDetails.ItemCostingComparator);
            }

            if (INCLD_UNPSTD) {

                // INV_ADJUSTMENT_LINE
                Collection invAdjustmentLines = invAdjustmentLineHome.findUnpostedAdjByLocNameAndAdBranch(locName, brCode, AD_CMPNY);

                Iterator unpstdIter = invAdjustmentLines.iterator();

                while (unpstdIter.hasNext()) {

                    LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) unpstdIter.next();

                    InvRepItemCostingDetails details = new InvRepItemCostingDetails();

                    Date earliestDate = null;
                    Date adjDate = invAdjustmentLine.getInvAdjustment().getAdjDate();
                    LocalInvItem invItem = invAdjustmentLine.getInvItemLocation().getInvItem();

                    LocalInvCosting invBeginningCosting = this.getCstIlBeginningBalanceByItemLocationAndDate(invAdjustmentLine.getInvItemLocation(), (Date) criteria.get("dateFrom"), brCode, AD_CMPNY);
                    if (invBeginningCosting != null) {

                        details.setIcBeginningQuantity(invBeginningCosting.getCstRemainingQuantity());
                        details.setIcBeginningUnitCost(invBeginningCosting.getCstRemainingValue() / invBeginningCosting.getCstRemainingQuantity());
                        details.setIcBeginningValue(invBeginningCosting.getCstRemainingValue());
                        earliestDate = invBeginningCosting.getCstDate();
                    }

                    if (this.filterByOptionalCriteria(criteria, invItem, adjDate, earliestDate)) {
                        continue;
                    }
                    if (invAdjustmentLine.getAlAdjustQuantity() > 0) {

                        details.setIcInQuantity(invAdjustmentLine.getAlAdjustQuantity());
                        details.setIcInAmount(invAdjustmentLine.getAlUnitCost() * invAdjustmentLine.getAlAdjustQuantity());
                        details.setIcInUnitCost(invAdjustmentLine.getAlUnitCost());

                    } else if (invAdjustmentLine.getAlAdjustQuantity() < 0) {

                        details.setIcOutQuantity(invAdjustmentLine.getAlAdjustQuantity() * -1);
                        details.setIcOutAmount(invAdjustmentLine.getAlUnitCost() * invAdjustmentLine.getAlAdjustQuantity() * -1);
                        details.setIcOutUnitCost(invAdjustmentLine.getAlUnitCost());
                    }
                    details.setIcItemName(invItem.getIiName() + "-" + invItem.getIiDescription());
                    details.setIcDate(adjDate);
                    details.setIcAdjustQuantity(invAdjustmentLine.getAlAdjustQuantity());
                    details.setIcAdjustCost(invAdjustmentLine.getAlAdjustQuantity() * invAdjustmentLine.getAlUnitCost());
                    details.setIcSourceDocument("ADJ");
                    details.setIcSourceDocumentNumber(invAdjustmentLine.getInvAdjustment().getAdjDocumentNumber());
                    details.setIcReferenceNumber(invAdjustmentLine.getInvAdjustment().getAdjReferenceNumber());
                    details.setIcItemCategory(invItem.getIiAdLvCategory());
                    details.setIcItemUnitOfMeasure(invItem.getInvUnitOfMeasure().getUomName());
                    details.setIcItemLocation(invBeginningCosting.getInvItemLocation().getInvLocation().getLocName());
                    details.setIcItemLocation1(LCTN);
                    details.setIcItemCategory1(CTGRY);

                    list.add(details);
                }

                // INV_STOCK_TRANSFER_LINE OUT
                locCode = invLocationHome.findByLocName(locName, AD_CMPNY).getLocCode();
                Collection invStockTransferLines = invStockTransferLineHome.findUnpostedStByLocCodeAndAdBranch(locCode, brCode, AD_CMPNY);

                unpstdIter = invStockTransferLines.iterator();

                while (unpstdIter.hasNext()) {

                    LocalInvStockTransferLine invStockTransferLine = (LocalInvStockTransferLine) unpstdIter.next();

                    InvRepItemCostingDetails details = new InvRepItemCostingDetails();

                    Date earliestDate = null;
                    Date stDate = invStockTransferLine.getInvStockTransfer().getStDate();
                    LocalInvItem invItem = invStockTransferLine.getInvItem();
                    LocalInvLocation invLocation = invLocationHome.findByPrimaryKey(invStockTransferLine.getStlLocationFrom());
                    LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(invItem.getIiName(), invLocation.getLocName(), AD_CMPNY);

                    LocalInvCosting invBeginningCosting = this.getCstIlBeginningBalanceByItemLocationAndDate(invItemLocation, (Date) criteria.get("dateFrom"), brCode, AD_CMPNY);
                    if (invBeginningCosting != null) {

                        details.setIcBeginningQuantity(invBeginningCosting.getCstRemainingQuantity());
                        details.setIcBeginningUnitCost(invBeginningCosting.getCstRemainingValue() / invBeginningCosting.getCstRemainingQuantity());
                        details.setIcBeginningValue(invBeginningCosting.getCstRemainingValue());
                        earliestDate = invBeginningCosting.getCstDate();
                    }

                    if (this.filterByOptionalCriteria(criteria, invItem, stDate, earliestDate)) {
                        continue;
                    }

                    if (invStockTransferLine.getStlQuantityDelivered() < 0) {

                        details.setIcInQuantity(invStockTransferLine.getStlQuantityDelivered());
                        details.setIcInAmount(invStockTransferLine.getStlAmount());
                        details.setIcInUnitCost(invStockTransferLine.getStlUnitCost());

                    } else if (invStockTransferLine.getStlQuantityDelivered() > 0) {

                        details.setIcOutQuantity(invStockTransferLine.getStlQuantityDelivered());
                        details.setIcOutAmount(invStockTransferLine.getStlAmount());
                        details.setIcOutUnitCost(invStockTransferLine.getStlUnitCost());
                    }

                    details.setIcItemName(invItem.getIiName() + "-" + invItem.getIiDescription());
                    details.setIcDate(stDate);
                    details.setIcAdjustQuantity(invStockTransferLine.getStlQuantityDelivered() * (-1));
                    details.setIcAdjustCost(invStockTransferLine.getStlAmount());
                    details.setIcSourceDocument("ST");
                    details.setIcSourceDocumentNumber(invStockTransferLine.getInvStockTransfer().getStDocumentNumber());
                    details.setIcItemCategory(invItem.getIiAdLvCategory());
                    details.setIcItemUnitOfMeasure(invItem.getInvUnitOfMeasure().getUomName());
                    details.setIcReferenceNumber(invStockTransferLine.getInvStockTransfer().getStReferenceNumber());
                    details.setIcItemLocation(invBeginningCosting.getInvItemLocation().getInvLocation().getLocName());
                    details.setIcItemLocation1(LCTN);
                    details.setIcItemCategory1(CTGRY);

                    list.add(details);
                }

                // INV_STOCK_TRANSFER_LINE IN
                locCode = invLocationHome.findByLocName(locName, AD_CMPNY).getLocCode();

                Collection invStockTransferToLines = invStockTransferLineHome.findUnpostedStByLocToCodeAndAdBranch(locCode, brCode, AD_CMPNY);

                unpstdIter = invStockTransferToLines.iterator();

                while (unpstdIter.hasNext()) {

                    LocalInvStockTransferLine invStockTransferLine = (LocalInvStockTransferLine) unpstdIter.next();

                    InvRepItemCostingDetails details = new InvRepItemCostingDetails();

                    Date earliestDate = null;
                    Date stDate = invStockTransferLine.getInvStockTransfer().getStDate();
                    LocalInvItem invItem = invStockTransferLine.getInvItem();
                    LocalInvLocation invLocation = invLocationHome.findByPrimaryKey(invStockTransferLine.getStlLocationFrom());
                    LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(invItem.getIiName(), invLocation.getLocName(), AD_CMPNY);

                    LocalInvCosting invBeginningCosting = this.getCstIlBeginningBalanceByItemLocationAndDate(invItemLocation, (Date) criteria.get("dateFrom"), brCode, AD_CMPNY);
                    if (invBeginningCosting != null) {

                        details.setIcBeginningQuantity(invBeginningCosting.getCstRemainingQuantity());
                        details.setIcBeginningUnitCost(invBeginningCosting.getCstRemainingValue() / invBeginningCosting.getCstRemainingQuantity());
                        details.setIcBeginningValue(invBeginningCosting.getCstRemainingValue());
                        earliestDate = invBeginningCosting.getCstDate();
                    }

                    if (this.filterByOptionalCriteria(criteria, invItem, stDate, earliestDate)) {
                        continue;
                    }

                    if (invStockTransferLine.getStlQuantityDelivered() > 0) {

                        details.setIcInQuantity(invStockTransferLine.getStlQuantityDelivered());
                        details.setIcInAmount(invStockTransferLine.getStlAmount());
                        details.setIcInUnitCost(invStockTransferLine.getStlUnitCost());

                    } else if (invStockTransferLine.getStlQuantityDelivered() < 0) {

                        details.setIcOutQuantity(invStockTransferLine.getStlQuantityDelivered());
                        details.setIcOutAmount(invStockTransferLine.getStlAmount());
                        details.setIcOutUnitCost(invStockTransferLine.getStlUnitCost());
                    }

                    details.setIcItemName(invItem.getIiName() + "-" + invItem.getIiDescription());
                    details.setIcDate(stDate);
                    details.setIcAdjustQuantity(invStockTransferLine.getStlQuantityDelivered());
                    details.setIcAdjustCost(invStockTransferLine.getStlAmount());
                    details.setIcSourceDocument("ST");
                    details.setIcSourceDocumentNumber(invStockTransferLine.getInvStockTransfer().getStDocumentNumber());
                    details.setIcItemCategory(invItem.getIiAdLvCategory());
                    details.setIcItemUnitOfMeasure(invItem.getInvUnitOfMeasure().getUomName());
                    details.setIcReferenceNumber(invStockTransferLine.getInvStockTransfer().getStReferenceNumber());
                    details.setIcItemLocation(invItemLocation.getInvLocation().getLocName());
                    details.setIcItemLocation1(LCTN);
                    details.setIcItemCategory1(CTGRY);

                    list.add(details);
                }

                // INV_BRANCH_STOCK_TRANSFER_LINE
                Collection invBranchStockTransferLines = invBranchStockTransferLineHome.findUnpostedBstByLocNameAndAdBranch(locName, brCode, AD_CMPNY);

                unpstdIter = invBranchStockTransferLines.iterator();

                while (unpstdIter.hasNext()) {

                    LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) unpstdIter.next();

                    InvRepItemCostingDetails details = new InvRepItemCostingDetails();

                    Date earliestDate = null;
                    Date bstDate = invBranchStockTransferLine.getInvBranchStockTransfer().getBstDate();
                    LocalInvItem invItem = invBranchStockTransferLine.getInvItemLocation().getInvItem();

                    LocalInvCosting invBeginningCosting = this.getCstIlBeginningBalanceByItemLocationAndDate(invBranchStockTransferLine.getInvItemLocation(), (Date) criteria.get("dateFrom"), brCode, AD_CMPNY);
                    if (invBeginningCosting != null) {

                        details.setIcBeginningQuantity(invBeginningCosting.getCstRemainingQuantity());
                        details.setIcBeginningUnitCost(invBeginningCosting.getCstRemainingValue() / invBeginningCosting.getCstRemainingQuantity());
                        details.setIcBeginningValue(invBeginningCosting.getCstRemainingValue());
                        earliestDate = invBeginningCosting.getCstDate();
                    }

                    if (this.filterByOptionalCriteria(criteria, invItem, bstDate, earliestDate)) {
                        continue;
                    }

                    if (invBranchStockTransferLine.getBslQuantityReceived() > 0) {

                        details.setIcInQuantity(invBranchStockTransferLine.getBslQuantityReceived());
                        details.setIcInAmount(invBranchStockTransferLine.getBslAmount());
                        details.setIcInUnitCost(invBranchStockTransferLine.getBslUnitCost());

                    } else if (invBranchStockTransferLine.getBslQuantityReceived() < 0) {

                        details.setIcOutQuantity(invBranchStockTransferLine.getBslQuantityReceived() * -1);
                        details.setIcOutAmount(invBranchStockTransferLine.getBslAmount() * -1);
                        details.setIcOutUnitCost(invBranchStockTransferLine.getBslUnitCost());
                    }

                    details.setIcItemName(invItem.getIiName() + "-" + invItem.getIiDescription());
                    details.setIcDate(bstDate);
                    details.setIcAdjustQuantity(invBranchStockTransferLine.getBslQuantityReceived());
                    details.setIcAdjustCost(invBranchStockTransferLine.getBslAmount());
                    details.setIcSourceDocument(invBranchStockTransferLine.getInvBranchStockTransfer().getBstType().equals("OUT") ? "BO" : "BI");
                    details.setIcSourceDocumentNumber(invBranchStockTransferLine.getInvBranchStockTransfer().getBstNumber());
                    details.setIcItemCategory(invItem.getIiAdLvCategory());
                    details.setIcItemUnitOfMeasure(invItem.getInvUnitOfMeasure().getUomName());
                    details.setIcReferenceNumber("");
                    details.setIcItemLocation(invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName());
                    details.setIcItemLocation1(LCTN);
                    details.setIcItemCategory1(CTGRY);

                    list.add(details);
                }

                // AP_VOUCHER_LINE_ITEM
                // a) apVoucher
                Collection apVoucherLineItems = apVoucherLineItemHome.findUnpostedVouByLocNameAndAdBranch(locName, brCode, AD_CMPNY);

                unpstdIter = apVoucherLineItems.iterator();

                while (unpstdIter.hasNext()) {

                    LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) unpstdIter.next();

                    InvRepItemCostingDetails details = new InvRepItemCostingDetails();

                    Date earliestDate = null;
                    Date vouDate = apVoucherLineItem.getApVoucher().getVouDate();
                    LocalInvItem invItem = apVoucherLineItem.getInvItemLocation().getInvItem();

                    LocalInvCosting invBeginningCosting = this.getCstIlBeginningBalanceByItemLocationAndDate(apVoucherLineItem.getInvItemLocation(), (Date) criteria.get("dateFrom"), brCode, AD_CMPNY);
                    if (invBeginningCosting != null) {

                        details.setIcBeginningQuantity(invBeginningCosting.getCstRemainingQuantity());
                        details.setIcBeginningUnitCost(invBeginningCosting.getCstRemainingValue() / invBeginningCosting.getCstRemainingQuantity());
                        details.setIcBeginningValue(invBeginningCosting.getCstRemainingValue());
                        earliestDate = invBeginningCosting.getCstDate();
                    }

                    if (this.filterByOptionalCriteria(criteria, invItem, vouDate, earliestDate)) {
                        continue;
                    }

                    double ITEM_COST = EJBCommon.roundIt(apVoucherLineItem.getVliAmount() / apVoucherLineItem.getVliQuantity(), this.getGlFcPrecisionUnit(AD_CMPNY));
                    details.setIcItemCost(ITEM_COST);

                    details.setIcItemName(invItem.getIiName() + "-" + invItem.getIiDescription());
                    details.setIcDate(vouDate);
                    details.setIcQuantityReceived(apVoucherLineItem.getVliQuantity());
                    details.setIcActualCost(apVoucherLineItem.getVliAmount());
                    details.setIcSourceDocument("VOU");
                    details.setIcSourceDocumentNumber(apVoucherLineItem.getApVoucher().getVouDocumentNumber());
                    details.setIcItemCategory(invItem.getIiAdLvCategory());
                    details.setIcItemUnitOfMeasure(invItem.getInvUnitOfMeasure().getUomName());
                    details.setIcReferenceNumber(apVoucherLineItem.getApVoucher().getVouReferenceNumber());
                    details.setIcItemLocation(invBeginningCosting.getInvItemLocation().getInvLocation().getLocName());
                    details.setIcItemLocation1(LCTN);
                    details.setIcItemCategory1(CTGRY);

                    list.add(details);
                }

                // b) apCheck
                apVoucherLineItems = apVoucherLineItemHome.findUnpostedChkByLocNameAndAdBranch(locName, brCode, AD_CMPNY);

                unpstdIter = apVoucherLineItems.iterator();

                while (unpstdIter.hasNext()) {

                    LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) unpstdIter.next();

                    InvRepItemCostingDetails details = new InvRepItemCostingDetails();

                    Date earliestDate = null;
                    Date chkDate = apVoucherLineItem.getApCheck().getChkDate();
                    LocalInvItem invItem = apVoucherLineItem.getInvItemLocation().getInvItem();

                    LocalInvCosting invBeginningCosting = this.getCstIlBeginningBalanceByItemLocationAndDate(apVoucherLineItem.getInvItemLocation(), (Date) criteria.get("dateFrom"), brCode, AD_CMPNY);
                    if (invBeginningCosting != null) {

                        details.setIcBeginningQuantity(invBeginningCosting.getCstRemainingQuantity());
                        details.setIcBeginningUnitCost(invBeginningCosting.getCstRemainingValue() / invBeginningCosting.getCstRemainingQuantity());
                        details.setIcBeginningValue(invBeginningCosting.getCstRemainingValue());
                        earliestDate = invBeginningCosting.getCstDate();
                    }

                    if (this.filterByOptionalCriteria(criteria, invItem, chkDate, earliestDate)) {
                        continue;
                    }

                    double ITEM_COST = EJBCommon.roundIt(apVoucherLineItem.getVliAmount() / apVoucherLineItem.getVliQuantity(), this.getGlFcPrecisionUnit(AD_CMPNY));
                    details.setIcItemCost(ITEM_COST);

                    details.setIcItemName(invItem.getIiName() + "-" + invItem.getIiDescription());
                    details.setIcDate(chkDate);
                    details.setIcQuantityReceived(apVoucherLineItem.getVliQuantity());
                    details.setIcActualCost(apVoucherLineItem.getVliAmount());
                    details.setIcSourceDocument("CHK");
                    details.setIcSourceDocumentNumber(apVoucherLineItem.getApCheck().getChkDocumentNumber());
                    details.setIcItemCategory(invItem.getIiAdLvCategory());
                    details.setIcItemUnitOfMeasure(invItem.getInvUnitOfMeasure().getUomName());
                    details.setIcReferenceNumber(apVoucherLineItem.getApVoucher().getVouReferenceNumber());
                    details.setIcItemLocation(invBeginningCosting.getInvItemLocation().getInvLocation().getLocName());
                    details.setIcItemLocation1(LCTN);
                    details.setIcItemCategory1(CTGRY);

                    list.add(details);
                }

                // AR_INVOICE_LINE_ITEM
                // a) arInvoice
                Collection arInvoiceLineItems = arInvoiceLineItemHome.findUnpostedInvcByLocNameAndAdBranch(locName, brCode, AD_CMPNY);

                unpstdIter = arInvoiceLineItems.iterator();

                while (unpstdIter.hasNext()) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) unpstdIter.next();

                    InvRepItemCostingDetails details = new InvRepItemCostingDetails();

                    Date earliestDate = null;
                    Date invDate = arInvoiceLineItem.getArInvoice().getInvDate();
                    LocalInvItem invItem = arInvoiceLineItem.getInvItemLocation().getInvItem();

                    LocalInvCosting invBeginningCosting = this.getCstIlBeginningBalanceByItemLocationAndDate(arInvoiceLineItem.getInvItemLocation(), (Date) criteria.get("dateFrom"), brCode, AD_CMPNY);
                    if (invBeginningCosting != null) {

                        details.setIcBeginningQuantity(invBeginningCosting.getCstRemainingQuantity());
                        details.setIcBeginningUnitCost(invBeginningCosting.getCstRemainingValue() / invBeginningCosting.getCstRemainingQuantity());
                        details.setIcBeginningValue(invBeginningCosting.getCstRemainingValue());
                        earliestDate = invBeginningCosting.getCstDate();
                    }

                    if (this.filterByOptionalCriteria(criteria, invItem, invDate, earliestDate)) {
                        continue;
                    }

                    // Get Last Costing
                    LocalInvCosting invLastCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(invDate, arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName(), arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName(), brCode, AD_CMPNY);

                    double COST = Math.abs(invLastCosting.getCstRemainingValue() / invLastCosting.getCstRemainingQuantity());
                    COST = EJBCommon.roundIt(COST, this.getGlFcPrecisionUnit(AD_CMPNY));

                    double CST_CST_OF_SLS = COST * arInvoiceLineItem.getIliQuantity();
                    CST_CST_OF_SLS = EJBCommon.roundIt(CST_CST_OF_SLS, this.getGlFcPrecisionUnit(AD_CMPNY));

                    details.setIcOutQuantity(arInvoiceLineItem.getIliQuantity());
                    details.setIcOutUnitCost(COST);
                    details.setIcOutAmount(CST_CST_OF_SLS);

                    details.setIcItemName(invItem.getIiName() + "-" + invItem.getIiDescription());
                    details.setIcDate(invDate);
                    details.setIcQuantitySold(arInvoiceLineItem.getIliQuantity());
                    // details.setIcCostOfSales(arInvoiceLineItem.getIliAmount());
                    details.setIcCostOfSales(CST_CST_OF_SLS);
                    details.setIcSourceDocument("SI");
                    details.setIcSourceDocumentNumber(arInvoiceLineItem.getArInvoice().getInvNumber());
                    details.setIcItemCategory(invItem.getIiAdLvCategory());
                    details.setIcItemUnitOfMeasure(invItem.getInvUnitOfMeasure().getUomName());
                    details.setIcReferenceNumber(arInvoiceLineItem.getArInvoice().getInvReferenceNumber());
                    details.setIcItemLocation(invBeginningCosting.getInvItemLocation().getInvLocation().getLocName());
                    details.setIcItemLocation1(LCTN);
                    details.setIcItemCategory1(CTGRY);

                    list.add(details);
                }

                // b) arReceipt
                arInvoiceLineItems = arInvoiceLineItemHome.findUnpostedRctByLocNameAndAdBranch(locName, brCode, AD_CMPNY);

                unpstdIter = arInvoiceLineItems.iterator();

                while (unpstdIter.hasNext()) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) unpstdIter.next();

                    InvRepItemCostingDetails details = new InvRepItemCostingDetails();

                    Date earliestDate = null;
                    Date rctDate = arInvoiceLineItem.getArReceipt().getRctDate();
                    LocalInvItem invItem = arInvoiceLineItem.getInvItemLocation().getInvItem();

                    LocalInvCosting invBeginningCosting = this.getCstIlBeginningBalanceByItemLocationAndDate(arInvoiceLineItem.getInvItemLocation(), (Date) criteria.get("dateFrom"), brCode, AD_CMPNY);
                    if (invBeginningCosting != null) {

                        details.setIcBeginningQuantity(invBeginningCosting.getCstRemainingQuantity());
                        details.setIcBeginningUnitCost(invBeginningCosting.getCstRemainingValue() / invBeginningCosting.getCstRemainingQuantity());
                        details.setIcBeginningValue(invBeginningCosting.getCstRemainingValue());
                        earliestDate = invBeginningCosting.getCstDate();
                    }

                    if (this.filterByOptionalCriteria(criteria, invItem, rctDate, earliestDate)) {
                        continue;
                    }

                    // Get Last Costing

                    double COST = 0;
                    try {
                        LocalInvCosting invLastCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(rctDate, arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName(), arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName(), brCode, AD_CMPNY);

                        COST = Math.abs(invLastCosting.getCstRemainingValue() / invLastCosting.getCstRemainingQuantity());

                    } catch (Exception e) {
                        COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();
                    }

                    double CST_CST_OF_SLS = COST * arInvoiceLineItem.getIliQuantity();
                    CST_CST_OF_SLS = EJBCommon.roundIt(CST_CST_OF_SLS, this.getGlFcPrecisionUnit(AD_CMPNY));

                    details.setIcOutQuantity(arInvoiceLineItem.getIliQuantity());
                    details.setIcOutAmount(CST_CST_OF_SLS);
                    details.setIcOutUnitCost(COST);

                    details.setIcItemName(invItem.getIiName() + "-" + invItem.getIiDescription());
                    details.setIcDate(rctDate);
                    details.setIcQuantitySold(arInvoiceLineItem.getIliQuantity());
                    // details.setIcCostOfSales(arInvoiceLineItem.getIliAmount());
                    details.setIcCostOfSales(CST_CST_OF_SLS);
                    details.setIcSourceDocument("RCT");
                    details.setIcSourceDocumentNumber(arInvoiceLineItem.getArReceipt().getRctNumber());
                    details.setIcItemCategory(invItem.getIiAdLvCategory());
                    details.setIcItemUnitOfMeasure(invItem.getInvUnitOfMeasure().getUomName());
                    details.setIcReferenceNumber(arInvoiceLineItem.getArInvoice().getInvReferenceNumber());
                    details.setIcItemLocation(invBeginningCosting.getInvItemLocation().getInvLocation().getLocName());
                    details.setIcItemLocation1(LCTN);
                    details.setIcItemCategory1(CTGRY);

                    list.add(details);
                }

                // AP_PURCHASE_ORDER_LINE
                Collection apPurchaseOrderLines = apPurchaseOrderLineHome.findUnpostedPoByLocNameAndAdBranch(locName, brCode, AD_CMPNY);

                unpstdIter = apPurchaseOrderLines.iterator();

                while (unpstdIter.hasNext()) {

                    LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) unpstdIter.next();

                    InvRepItemCostingDetails details = new InvRepItemCostingDetails();

                    Date earliestDate = null;
                    Date poDate = apPurchaseOrderLine.getApPurchaseOrder().getPoDate();
                    LocalInvItem invItem = apPurchaseOrderLine.getInvItemLocation().getInvItem();

                    LocalInvCosting invBeginningCosting = this.getCstIlBeginningBalanceByItemLocationAndDate(apPurchaseOrderLine.getInvItemLocation(), (Date) criteria.get("dateFrom"), brCode, AD_CMPNY);
                    if (invBeginningCosting != null) {

                        details.setIcBeginningQuantity(invBeginningCosting.getCstRemainingQuantity());
                        details.setIcBeginningUnitCost(invBeginningCosting.getCstRemainingValue() / invBeginningCosting.getCstRemainingQuantity());
                        details.setIcBeginningValue(invBeginningCosting.getCstRemainingValue());
                        earliestDate = invBeginningCosting.getCstDate();
                    }

                    if (this.filterByOptionalCriteria(criteria, invItem, poDate, earliestDate)) {
                        continue;
                    }

                    double ITEM_COST = EJBCommon.roundIt(apPurchaseOrderLine.getPlAmount() / apPurchaseOrderLine.getPlQuantity(), this.getGlFcPrecisionUnit(AD_CMPNY));
                    details.setIcItemCost(ITEM_COST);

                    details.setIcInQuantity(apPurchaseOrderLine.getPlQuantity());
                    details.setIcInAmount(apPurchaseOrderLine.getPlAmount());
                    // details.setRilInUnitCost(apPurchaseOrderLine.getPlUnitCost());
                    details.setIcInUnitCost(apPurchaseOrderLine.getPlAmount() / apPurchaseOrderLine.getPlQuantity());

                    details.setIcItemName(invItem.getIiName() + "-" + invItem.getIiDescription());
                    details.setIcDate(poDate);
                    details.setIcQuantityReceived(apPurchaseOrderLine.getPlQuantity());
                    details.setIcActualCost(apPurchaseOrderLine.getPlAmount());
                    details.setIcSourceDocument("RR");
                    details.setIcSourceDocumentNumber(apPurchaseOrderLine.getApPurchaseOrder().getPoDocumentNumber());
                    details.setIcItemCategory(invItem.getIiAdLvCategory());
                    details.setIcItemUnitOfMeasure(invItem.getInvUnitOfMeasure().getUomName());
                    details.setIcReferenceNumber(apPurchaseOrderLine.getApPurchaseOrder().getPoReferenceNumber());
                    details.setIcItemLocation(invBeginningCosting.getInvItemLocation().getInvLocation().getLocName());
                    details.setIcItemLocation1(LCTN);
                    details.setIcItemCategory1(CTGRY);

                    list.add(details);
                }

                // AR_SALES_ORDER_INVOICE_LINE
                Collection arSalesOrderInvoiceLines = arSalesOrderInvoiceLineHome.findUnpostedSoInvByLocNameAndAdBranch(locName, brCode, AD_CMPNY);

                unpstdIter = arSalesOrderInvoiceLines.iterator();

                while (unpstdIter.hasNext()) {

                    LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) unpstdIter.next();

                    InvRepItemCostingDetails details = new InvRepItemCostingDetails();

                    Date earliestDate = null;
                    Date invDate = arSalesOrderInvoiceLine.getArInvoice().getInvDate();
                    LocalInvItem invItem = arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvItem();

                    LocalInvCosting invBeginningCosting = this.getCstIlBeginningBalanceByItemLocationAndDate(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation(), (Date) criteria.get("dateFrom"), brCode, AD_CMPNY);
                    if (invBeginningCosting != null) {

                        details.setIcBeginningQuantity(invBeginningCosting.getCstRemainingQuantity());
                        details.setIcBeginningUnitCost(invBeginningCosting.getCstRemainingValue() / invBeginningCosting.getCstRemainingQuantity());
                        details.setIcBeginningValue(invBeginningCosting.getCstRemainingValue());
                        earliestDate = invBeginningCosting.getCstDate();
                    }

                    if (this.filterByOptionalCriteria(criteria, invItem, invDate, earliestDate)) {
                        continue;
                    }

                    details.setIcOutQuantity(arSalesOrderInvoiceLine.getSilQuantityDelivered());
                    details.setIcOutAmount(arSalesOrderInvoiceLine.getSilAmount());
                    details.setIcOutUnitCost(Math.abs(arSalesOrderInvoiceLine.getSilAmount() / arSalesOrderInvoiceLine.getSilQuantityDelivered()));

                    details.setIcItemName(invItem.getIiName() + "-" + invItem.getIiDescription());
                    details.setIcDate(invDate);
                    details.setIcQuantitySold(arSalesOrderInvoiceLine.getSilQuantityDelivered());
                    details.setIcCostOfSales(arSalesOrderInvoiceLine.getSilAmount());
                    details.setIcSourceDocument("SI");
                    details.setIcSourceDocumentNumber(arSalesOrderInvoiceLine.getArInvoice().getInvNumber());
                    details.setIcItemCategory(invItem.getIiAdLvCategory());
                    details.setIcItemUnitOfMeasure(invItem.getInvUnitOfMeasure().getUomName());
                    details.setIcReferenceNumber(arSalesOrderInvoiceLine.getArInvoice().getInvReferenceNumber());
                    details.setIcItemLocation(invBeginningCosting.getInvItemLocation().getInvLocation().getLocName());
                    details.setIcItemLocation1(LCTN);
                    details.setIcItemCategory1(CTGRY);

                    list.add(details);
                }
                // Collections.sort(list, InvRepItemCostingDetails.ItemCostingComparator);
            }

            if (list.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }

            if (INCLD_UNPSTD || SHW_CMMTTD_QNTTY) {
                list.sort(InvRepItemCostingDetails.ItemCostingComparator);
            }

            if ((INCLD_UNPSTD || SHW_CMMTTD_QNTTY) && criteria.get("dateFrom") != null) {

                Date dateFrom = (Date) criteria.get("dateFrom");

                ArrayList newList = new ArrayList();

                InvRepItemCostingDetails firstDetail = (InvRepItemCostingDetails) list.get(0);

                String currentItem = firstDetail.getIcItemName();
                double beginningQty = firstDetail.getIcBeginningQuantity();
                double beginningValue = firstDetail.getIcBeginningValue();

                for (Object value : list) {

                    InvRepItemCostingDetails details = (InvRepItemCostingDetails) value;

                    if (!currentItem.equals(details.getIcItemName())) {
                        currentItem = details.getIcItemName();
                        beginningQty = details.getIcBeginningQuantity();
                        beginningValue = details.getIcBeginningValue();
                    }

                    if (details.getIcDate().getTime() < dateFrom.getTime()) {

                        beginningQty += (details.getIcAdjustQuantity() + details.getIcQuantityReceived() - details.getIcQuantitySold());
                        beginningValue += (details.getIcAdjustCost() + details.getIcActualCost() - details.getIcCostOfSales());

                    } else {

                        details.setIcBeginningQuantity(beginningQty);
                        details.setIcBeginningValue(beginningValue);

                        newList.add(details);
                    }
                }

                if (SHW_ZRS) {

                    Collection invAdjustmentLines = invAdjustmentLineHome.findPostedAdjByLessOrEqualDateAndAdjAdBranch(brCode, AD_CMPNY);

                    for (Object adjustmentLine : invAdjustmentLines) {
                        LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) adjustmentLine;

                        InvRepItemCostingDetails details = new InvRepItemCostingDetails();

                        LocalInvItem invItem = invAdjustmentLine.getInvItemLocation().getInvItem();
                        String location = invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName();
                        int locationCode = invAdjustmentLine.getInvItemLocation().getInvLocation().getLocCode();

                        Collection invStockTransferLines = invStockTransferLineHome.findByIiCodeAndLocCodeAndAdBranch(invItem.getIiCode(), locationCode, brCode, AD_CMPNY);
                        Collection invBranchStockTransferLines = invBranchStockTransferLineHome.findByIiCodeAndLocNameAndAdBranch(invItem.getIiCode(), location, brCode, AD_CMPNY);
                        Collection apVoucherLineItems = apVoucherLineItemHome.findVouByIiCodeAndLocNameAndAdBranch(invItem.getIiCode(), location, brCode, AD_CMPNY);
                        Collection arInvoiceLineItems = arInvoiceLineItemHome.findInvcByIiCodeAndLocNameAndAdBranch(invItem.getIiCode(), location, brCode, AD_CMPNY);
                        Collection apPurchaseOrderLines = apPurchaseOrderLineHome.findPoByIiCodeAndLocNameAndAdBranch(invItem.getIiCode(), location, brCode, AD_CMPNY);
                        Collection arSalesOrderInvoiceLines = arSalesOrderInvoiceLineHome.findSoInvByIiCodeAndLocNameAndAdBranch(invItem.getIiCode(), location, brCode, AD_CMPNY);

                        if (invStockTransferLines.isEmpty() && invBranchStockTransferLines.isEmpty() && apVoucherLineItems.isEmpty() && arInvoiceLineItems.isEmpty() && apPurchaseOrderLines.isEmpty() && arSalesOrderInvoiceLines.isEmpty()) {
                            Collection invCosting = invCostingHome.findByIiNameAndLocNameAndAdjLineCode((Date) criteria.get("dateFrom"), invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(), invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName(), invAdjustmentLine.getAlCode(), brCode, AD_CMPNY);

                            for (Object o : invCosting) {

                                LocalInvCosting invCosting1 = (LocalInvCosting) o;
                                InvRepItemCostingDetails mdetails = new InvRepItemCostingDetails();

                                mdetails.setIcBeginningQuantity(invCosting1.getCstRemainingQuantity());
                                mdetails.setIcBeginningUnitCost(invCosting1.getCstRemainingValue() / invCosting1.getCstRemainingQuantity());
                                mdetails.setIcBeginningValue(invCosting1.getCstRemainingValue());
                                mdetails.setIcDate(invAdjustmentLine.getInvAdjustment().getAdjDate());
                                mdetails.setIcSourceDocumentNumber(invAdjustmentLine.getInvAdjustment().getAdjDocumentNumber());
                                mdetails.setIcSourceDocument("INV");
                                mdetails.setIcReferenceNumber(invAdjustmentLine.getInvAdjustment().getAdjReferenceNumber());
                                mdetails.setIcItemCategory(invItem.getIiAdLvCategory());
                                mdetails.setIcItemUnitOfMeasure(invItem.getInvUnitOfMeasure().getUomName());
                                mdetails.setIcItemName(invItem.getIiName() + "-" + invItem.getIiDescription());
                                mdetails.setIcItemLocation(invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName());
                                mdetails.setIcItemLocation1(LCTN);
                                mdetails.setIcItemCategory1(CTGRY);

                                newList.add(mdetails);
                            }
                        }
                    }
                    // Collections.sort(newList, InvRepItemCostingDetails.ItemCostingComparator);

                }
                // Collections.sort(list, InvRepItemCostingDetails.ItemCostingComparator);
                return newList;
            } else {
                if (SHW_ZRS) {

                    Collection invAdjustmentLines = invAdjustmentLineHome.findPostedAdjByLessOrEqualDateAndAdjAdBranch(brCode, AD_CMPNY);

                    for (Object adjustmentLine : invAdjustmentLines) {
                        LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) adjustmentLine;

                        InvRepItemCostingDetails details = new InvRepItemCostingDetails();

                        LocalInvItem invItem = invAdjustmentLine.getInvItemLocation().getInvItem();
                        String location = invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName();
                        int locationCode = invAdjustmentLine.getInvItemLocation().getInvLocation().getLocCode();

                        Collection invStockTransferLines = invStockTransferLineHome.findByIiCodeAndLocCodeAndAdBranch(invItem.getIiCode(), locationCode, brCode, AD_CMPNY);
                        Collection invBranchStockTransferLines = invBranchStockTransferLineHome.findByIiCodeAndLocNameAndAdBranch(invItem.getIiCode(), location, brCode, AD_CMPNY);
                        Collection apVoucherLineItems = apVoucherLineItemHome.findVouByIiCodeAndLocNameAndAdBranch(invItem.getIiCode(), location, brCode, AD_CMPNY);
                        Collection arInvoiceLineItems = arInvoiceLineItemHome.findInvcByIiCodeAndLocNameAndAdBranch(invItem.getIiCode(), location, brCode, AD_CMPNY);
                        Collection apPurchaseOrderLines = apPurchaseOrderLineHome.findPoByIiCodeAndLocNameAndAdBranch(invItem.getIiCode(), location, brCode, AD_CMPNY);
                        Collection arSalesOrderInvoiceLines = arSalesOrderInvoiceLineHome.findSoInvByIiCodeAndLocNameAndAdBranch(invItem.getIiCode(), location, brCode, AD_CMPNY);

                        if (invStockTransferLines.isEmpty() && invBranchStockTransferLines.isEmpty() && apVoucherLineItems.isEmpty() && arInvoiceLineItems.isEmpty() && apPurchaseOrderLines.isEmpty() && arSalesOrderInvoiceLines.isEmpty()) {
                            Collection invCosting = invCostingHome.findByIiNameAndLocNameAndAdjLineCode((Date) criteria.get("dateFrom"), invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(), invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName(), invAdjustmentLine.getAlCode(), brCode, AD_CMPNY);

                            for (Object o : invCosting) {

                                LocalInvCosting invCosting1 = (LocalInvCosting) o;
                                InvRepItemCostingDetails mdetails = new InvRepItemCostingDetails();

                                mdetails.setIcBeginningQuantity(invCosting1.getCstRemainingQuantity());
                                mdetails.setIcBeginningUnitCost(invCosting1.getCstRemainingValue() / invCosting1.getCstRemainingQuantity());
                                mdetails.setIcBeginningValue(invCosting1.getCstRemainingValue());
                                mdetails.setIcDate(invAdjustmentLine.getInvAdjustment().getAdjDate());
                                mdetails.setIcSourceDocumentNumber(invAdjustmentLine.getInvAdjustment().getAdjDocumentNumber());
                                mdetails.setIcSourceDocument("INV");
                                mdetails.setIcReferenceNumber(invAdjustmentLine.getInvAdjustment().getAdjReferenceNumber());
                                mdetails.setIcItemCategory(invItem.getIiAdLvCategory());
                                mdetails.setIcItemUnitOfMeasure(invItem.getInvUnitOfMeasure().getUomName());
                                mdetails.setIcItemName(invItem.getIiName() + "-" + invItem.getIiDescription());
                                mdetails.setIcItemLocation(invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName());
                                mdetails.setIcItemLocation1(LCTN);
                                mdetails.setIcItemCategory1(CTGRY);

                                list.add(mdetails);
                            }
                        }
                    }
                    // Collections.sort(list, InvRepItemCostingDetails.ItemCostingComparator);

                }
                // Collections.sort(list, InvRepItemCostingDetails.ItemCostingComparator);
                return list;
            }

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

    public void executeInvFixItemCosting(HashMap criteria, ArrayList branchList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvRepItemCostingControllerBean executeInvFixItemCosting");

        ArrayList list = new ArrayList();

        try {

            Integer brCode = null;
            Integer locCode = null;
            String locName = "";

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(cst) FROM InvCosting  cst WHERE (");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = 0;

            Object[] obj = null;

            if (branchList.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }

            Iterator brIter = branchList.iterator();

            AdBranchDetails bdetails = (AdBranchDetails) brIter.next();
            jbossQl.append("cst.cstAdBranch=").append(bdetails.getBrCode());

            while (brIter.hasNext()) {

                bdetails = (AdBranchDetails) brIter.next();

                jbossQl.append(" OR cst.cstAdBranch=").append(bdetails.getBrCode());
            }

            jbossQl.append(") ");

            firstArgument = false;

            // Allocate the size of the object parameter

            if (criteria.containsKey("category")) {

                criteriaSize++;
            }

            if (criteria.containsKey("location")) {

                criteriaSize++;
                locName = (String) criteria.get("location");
            }

            if (criteria.containsKey("dateFrom")) {

                criteriaSize++;
            }

            if (criteria.containsKey("dateTo")) {

                criteriaSize++;
            }

            if (criteria.containsKey("itemClass")) {

                criteriaSize++;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("itemName")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.invItemLocation.invItem.iiName  LIKE '%").append(criteria.get("itemName")).append("%' ");
            }

            if (criteria.containsKey("category")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.invItemLocation.invItem.iiAdLvCategory=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("category");
                ctr++;
            }

            if (criteria.containsKey("location")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.invItemLocation.invLocation.locName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("location");
                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.cstDate>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateFrom");
                ctr++;
            }

            if (criteria.containsKey("dateTo")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.cstDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (criteria.containsKey("itemClass")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.invItemLocation.invItem.iiClass=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("itemClass");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("cst.cstAdCompany=").append(AD_CMPNY).append(" ");

            jbossQl.append("ORDER BY cst.invItemLocation.invItem.iiName, cst.cstDate, cst.cstDateToLong, cst.cstLineNumber");

            Collection invCostings = invCostingHome.getCstByCriteria(jbossQl.toString(), obj, 0, 0);
            Iterator i = invCostings.iterator();

            Integer CURRENT_II_CODE = 0;
            double UNIT_COST = 0d;

            double CST_RMNNG_QTY = 0d;
            double CST_RMNNG_VL = 0d;

            double CURRENT_RMNNG_QTY = 0d;
            double CURRENT_RMNNG_VL = 0d;

            int CST_LN_NMBR = 0;
            double TXN_QTY = 0;
            double TXN_CST = 0;
            double PREV_UNT_CST = 0;
            double PREV_RMNG_VL = 0;

            double QUANTITY_RECEIVED = 0d;
            double ASSEMBLY_QUANTITY = 0d;
            double ADJUST_QUANTITY = 0d;
            double QUANTITY_SOLD = 0d;

            double ITEM_COST = 0d;
            double ASSEMBLY_COST = 0d;
            double ADJUST_COST = 0d;
            double COST_OF_SALES = 0;

            Date CST_DATE;

            boolean isFirst = true;

            while (i.hasNext()) {

                LocalInvCosting invCosting = (LocalInvCosting) i.next();
                UNIT_COST = EJBCommon.roundIt(invCosting.getInvItemLocation().getInvItem().getIiUnitCost(), (short) 2);

                QUANTITY_RECEIVED = EJBCommon.roundIt(invCosting.getCstQuantityReceived(), (short) 2);
                ADJUST_QUANTITY = EJBCommon.roundIt(invCosting.getCstAdjustQuantity(), (short) 2);
                QUANTITY_SOLD = EJBCommon.roundIt(invCosting.getCstQuantitySold(), (short) 2);

                ITEM_COST = invCosting.getCstItemCost();
                ADJUST_COST = invCosting.getCstAdjustCost();
                COST_OF_SALES = invCosting.getCstCostOfSales();

                // cleaning values of all
                if (QUANTITY_RECEIVED == 0) {
                    ITEM_COST = 0;
                } else {
                    if (ITEM_COST == 0) {
                        ITEM_COST = EJBCommon.roundIt((CST_RMNNG_VL / CST_RMNNG_QTY) * QUANTITY_RECEIVED, (short) 2);
                    }
                }

                if (ADJUST_QUANTITY == 0) {
                    ADJUST_COST = 0;
                } else {
                    if (ADJUST_COST == 0) {
                        ADJUST_COST = EJBCommon.roundIt((CST_RMNNG_VL / CST_RMNNG_QTY) * ADJUST_QUANTITY, (short) 2);
                    }
                }

                if (QUANTITY_SOLD == 0) {
                    COST_OF_SALES = 0;
                } else {
                    if (COST_OF_SALES == 0) {
                        COST_OF_SALES = EJBCommon.roundIt((CST_RMNNG_VL / CST_RMNNG_QTY) * QUANTITY_SOLD, (short) 2);
                    }
                }

                // InvRepItemCostingDetails details = new InvRepItemCostingDetails();

                if (CURRENT_II_CODE == 0) {
                    CURRENT_II_CODE = invCosting.getInvItemLocation().getIlCode();

                } else {
                    if (!CURRENT_II_CODE.equals(invCosting.getInvItemLocation().getIlCode())) {
                        CST_RMNNG_QTY = 0d;
                        CST_RMNNG_VL = 0d;
                        CURRENT_RMNNG_QTY = 0d;
                        CURRENT_RMNNG_VL = 0d;
                        TXN_QTY = 0;
                        TXN_CST = 0;
                        isFirst = true;
                        CURRENT_II_CODE = invCosting.getInvItemLocation().getIlCode();
                    }
                }

                if (isFirst) {

                    COST_OF_SALES = 0d;

                    CST_LN_NMBR = 1;
                    // CST_QTY_RCVD+CST_ASSMBLY_QTY+CST_ADJST_QTY-CST_QTY_SLD
                    CURRENT_RMNNG_QTY = QUANTITY_RECEIVED + ASSEMBLY_QUANTITY + ADJUST_QUANTITY - QUANTITY_SOLD;

                    // CST_ITM_CST+CST_ASSMBLY_CST+CST_ADJST_CST-CST_CST_OF_SLS

                    // invCosting.getArInvoiceLineItem().get
                    CURRENT_RMNNG_VL = ITEM_COST + ASSEMBLY_COST + ADJUST_COST - COST_OF_SALES;

                    ITEM_COST = UNIT_COST * QUANTITY_RECEIVED;

                } else {

                    CURRENT_RMNNG_QTY = CST_RMNNG_QTY + QUANTITY_RECEIVED + ASSEMBLY_QUANTITY + ADJUST_QUANTITY - QUANTITY_SOLD;

                    // if both are zero
                    if (CST_RMNNG_QTY == 0 && CST_RMNNG_VL == 0) {

                        if (QUANTITY_SOLD != 0) {
                            COST_OF_SALES = EJBCommon.roundIt(UNIT_COST * QUANTITY_SOLD, (short) 2);
                        }

                        if (QUANTITY_RECEIVED != 0) {
                            ITEM_COST = UNIT_COST * QUANTITY_RECEIVED;
                        }

                        if (ADJUST_QUANTITY != 0) {
                            ADJUST_COST = UNIT_COST * ADJUST_QUANTITY;
                        }

                        if (ASSEMBLY_QUANTITY != 0) {
                            ASSEMBLY_COST = UNIT_COST * ASSEMBLY_QUANTITY;
                        }

                    } else {

                        double UNIT_COST_VALUE = CST_RMNNG_VL / CST_RMNNG_QTY;

                        if ((CST_RMNNG_QTY <= 0 && CURRENT_RMNNG_QTY > 0) || (CURRENT_RMNNG_QTY < 0) || (CST_RMNNG_QTY >= 0 && CURRENT_RMNNG_QTY < 0)) {

                            UNIT_COST_VALUE = UNIT_COST;
                        }

                        if (QUANTITY_SOLD != 0) {
                            COST_OF_SALES = EJBCommon.roundIt(UNIT_COST_VALUE * QUANTITY_SOLD, (short) 2);
                        }

                        if (QUANTITY_RECEIVED != 0) {
                            ITEM_COST = UNIT_COST_VALUE * QUANTITY_RECEIVED;
                        }

                        if (ADJUST_QUANTITY != 0) {
                            ADJUST_COST = UNIT_COST_VALUE * ADJUST_QUANTITY;
                        }

                        if (ASSEMBLY_QUANTITY != 0) {
                            ASSEMBLY_COST = UNIT_COST_VALUE * ASSEMBLY_QUANTITY;
                        }
                    }

                    if (CURRENT_RMNNG_QTY == 0) {
                        CURRENT_RMNNG_VL = 0;
                    } else {
                        CURRENT_RMNNG_VL = (CST_RMNNG_VL + ITEM_COST + ASSEMBLY_COST + ADJUST_COST) - (COST_OF_SALES);
                    }
                }

                CST_RMNNG_QTY = EJBCommon.roundIt(CURRENT_RMNNG_QTY, (short) 2);
                CST_RMNNG_VL = EJBCommon.roundIt(CURRENT_RMNNG_VL, (short) 2);
                COST_OF_SALES = EJBCommon.roundIt(COST_OF_SALES, (short) 2);
                ADJUST_COST = EJBCommon.roundIt(ADJUST_COST, (short) 2);
                ITEM_COST = EJBCommon.roundIt(ITEM_COST, (short) 2);

                // invCosting.setCstRemainingQuantity(1);

                invCosting.setCstRemainingQuantity(CST_RMNNG_QTY);
                invCosting.setCstRemainingValue(CST_RMNNG_VL);
                invCosting.setCstCostOfSales(COST_OF_SALES);
                invCosting.setCstItemCost(ITEM_COST);
                invCosting.setCstAdjustCost(ADJUST_COST);

                isFirst = false;

                // invCosting.getArInvoiceLineItem()

            }

        } catch (GlobalNoRecordFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public void executeInvFixItemCosting(HashMap criteria, ArrayList branchList, Integer AD_CMPNY, String companyShortName) throws GlobalNoRecordFoundException {
        Debug.print("InvRepItemCostingControllerBean executeInvFixItemCosting");

        ArrayList list = new ArrayList();

        try {

            Integer brCode = null;
            Integer locCode = null;
            String locName = "";

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(cst) FROM InvCosting  cst WHERE (");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = 0;

            Object[] obj = null;

            if (branchList.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }

            Iterator brIter = branchList.iterator();

            AdBranchDetails bdetails = (AdBranchDetails) brIter.next();
            jbossQl.append("cst.cstAdBranch=").append(bdetails.getBrCode());

            while (brIter.hasNext()) {

                bdetails = (AdBranchDetails) brIter.next();

                jbossQl.append(" OR cst.cstAdBranch=").append(bdetails.getBrCode());
            }

            jbossQl.append(") ");

            firstArgument = false;

            // Allocate the size of the object parameter

            if (criteria.containsKey("category")) {

                criteriaSize++;
            }

            if (criteria.containsKey("location")) {

                criteriaSize++;
                locName = (String) criteria.get("location");
            }

            if (criteria.containsKey("dateFrom")) {

                criteriaSize++;
            }

            if (criteria.containsKey("dateTo")) {

                criteriaSize++;
            }

            if (criteria.containsKey("itemClass")) {

                criteriaSize++;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("itemName")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.invItemLocation.invItem.iiName  LIKE '%").append(criteria.get("itemName")).append("%' ");
            }

            if (criteria.containsKey("category")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.invItemLocation.invItem.iiAdLvCategory=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("category");
                ctr++;
            }

            if (criteria.containsKey("location")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.invItemLocation.invLocation.locName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("location");
                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.cstDate>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateFrom");
                ctr++;
            }

            if (criteria.containsKey("dateTo")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.cstDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (criteria.containsKey("itemClass")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.invItemLocation.invItem.iiClass=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("itemClass");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("cst.cstAdCompany=").append(AD_CMPNY).append(" ");

            jbossQl.append("ORDER BY cst.invItemLocation.invItem.iiName, cst.cstDate, cst.cstDateToLong, cst.cstLineNumber");

            Collection invCostings = invCostingHome.getCstByCriteria(jbossQl.toString(), obj, 0, 0, companyShortName);
            Iterator i = invCostings.iterator();

            Integer CURRENT_II_CODE = 0;
            double UNIT_COST = 0d;

            double CST_RMNNG_QTY = 0d;
            double CST_RMNNG_VL = 0d;

            double CURRENT_RMNNG_QTY = 0d;
            double CURRENT_RMNNG_VL = 0d;

            int CST_LN_NMBR = 0;
            double TXN_QTY = 0;
            double TXN_CST = 0;
            double PREV_UNT_CST = 0;
            double PREV_RMNG_VL = 0;

            double QUANTITY_RECEIVED = 0d;
            double ASSEMBLY_QUANTITY = 0d;
            double ADJUST_QUANTITY = 0d;
            double QUANTITY_SOLD = 0d;

            double ITEM_COST = 0d;
            double ASSEMBLY_COST = 0d;
            double ADJUST_COST = 0d;
            double COST_OF_SALES = 0;

            Date CST_DATE;

            boolean isFirst = true;

            while (i.hasNext()) {

                LocalInvCosting invCosting = (LocalInvCosting) i.next();
                UNIT_COST = EJBCommon.roundIt(invCosting.getInvItemLocation().getInvItem().getIiUnitCost(), (short) 2);

                QUANTITY_RECEIVED = EJBCommon.roundIt(invCosting.getCstQuantityReceived(), (short) 2);
                ADJUST_QUANTITY = EJBCommon.roundIt(invCosting.getCstAdjustQuantity(), (short) 2);
                QUANTITY_SOLD = EJBCommon.roundIt(invCosting.getCstQuantitySold(), (short) 2);

                ITEM_COST = invCosting.getCstItemCost();
                ADJUST_COST = invCosting.getCstAdjustCost();
                COST_OF_SALES = invCosting.getCstCostOfSales();

                // cleaning values of all
                if (QUANTITY_RECEIVED == 0) {
                    ITEM_COST = 0;
                } else {
                    if (ITEM_COST == 0) {
                        ITEM_COST = EJBCommon.roundIt((CST_RMNNG_VL / CST_RMNNG_QTY) * QUANTITY_RECEIVED, (short) 2);
                    }
                }

                if (ADJUST_QUANTITY == 0) {
                    ADJUST_COST = 0;
                } else {
                    if (ADJUST_COST == 0) {
                        ADJUST_COST = EJBCommon.roundIt((CST_RMNNG_VL / CST_RMNNG_QTY) * ADJUST_QUANTITY, (short) 2);
                    }
                }

                if (QUANTITY_SOLD == 0) {
                    COST_OF_SALES = 0;
                } else {
                    if (COST_OF_SALES == 0) {
                        COST_OF_SALES = EJBCommon.roundIt((CST_RMNNG_VL / CST_RMNNG_QTY) * QUANTITY_SOLD, (short) 2);
                    }
                }

                // InvRepItemCostingDetails details = new InvRepItemCostingDetails();

                if (CURRENT_II_CODE == 0) {
                    CURRENT_II_CODE = invCosting.getInvItemLocation().getIlCode();

                } else {
                    if (!CURRENT_II_CODE.equals(invCosting.getInvItemLocation().getIlCode())) {
                        CST_RMNNG_QTY = 0d;
                        CST_RMNNG_VL = 0d;
                        CURRENT_RMNNG_QTY = 0d;
                        CURRENT_RMNNG_VL = 0d;
                        TXN_QTY = 0;
                        TXN_CST = 0;
                        isFirst = true;
                        CURRENT_II_CODE = invCosting.getInvItemLocation().getIlCode();
                    }
                }

                if (isFirst) {

                    COST_OF_SALES = 0d;

                    CST_LN_NMBR = 1;
                    // CST_QTY_RCVD+CST_ASSMBLY_QTY+CST_ADJST_QTY-CST_QTY_SLD
                    CURRENT_RMNNG_QTY = QUANTITY_RECEIVED + ASSEMBLY_QUANTITY + ADJUST_QUANTITY - QUANTITY_SOLD;

                    // CST_ITM_CST+CST_ASSMBLY_CST+CST_ADJST_CST-CST_CST_OF_SLS

                    // invCosting.getArInvoiceLineItem().get
                    CURRENT_RMNNG_VL = ITEM_COST + ASSEMBLY_COST + ADJUST_COST - COST_OF_SALES;

                    ITEM_COST = UNIT_COST * QUANTITY_RECEIVED;

                } else {

                    CURRENT_RMNNG_QTY = CST_RMNNG_QTY + QUANTITY_RECEIVED + ASSEMBLY_QUANTITY + ADJUST_QUANTITY - QUANTITY_SOLD;

                    // if both are zero
                    if (CST_RMNNG_QTY == 0 && CST_RMNNG_VL == 0) {

                        if (QUANTITY_SOLD != 0) {
                            COST_OF_SALES = EJBCommon.roundIt(UNIT_COST * QUANTITY_SOLD, (short) 2);
                        }

                        if (QUANTITY_RECEIVED != 0) {
                            ITEM_COST = UNIT_COST * QUANTITY_RECEIVED;
                        }

                        if (ADJUST_QUANTITY != 0) {
                            ADJUST_COST = UNIT_COST * ADJUST_QUANTITY;
                        }

                        if (ASSEMBLY_QUANTITY != 0) {
                            ASSEMBLY_COST = UNIT_COST * ASSEMBLY_QUANTITY;
                        }

                    } else {

                        double UNIT_COST_VALUE = CST_RMNNG_VL / CST_RMNNG_QTY;

                        if ((CST_RMNNG_QTY <= 0 && CURRENT_RMNNG_QTY > 0) || (CURRENT_RMNNG_QTY < 0) || (CST_RMNNG_QTY >= 0 && CURRENT_RMNNG_QTY < 0)) {

                            UNIT_COST_VALUE = UNIT_COST;
                        }

                        if (QUANTITY_SOLD != 0) {
                            COST_OF_SALES = EJBCommon.roundIt(UNIT_COST_VALUE * QUANTITY_SOLD, (short) 2);
                        }

                        if (QUANTITY_RECEIVED != 0) {
                            ITEM_COST = UNIT_COST_VALUE * QUANTITY_RECEIVED;
                        }

                        if (ADJUST_QUANTITY != 0) {
                            ADJUST_COST = UNIT_COST_VALUE * ADJUST_QUANTITY;
                        }

                        if (ASSEMBLY_QUANTITY != 0) {
                            ASSEMBLY_COST = UNIT_COST_VALUE * ASSEMBLY_QUANTITY;
                        }
                    }

                    if (CURRENT_RMNNG_QTY == 0) {
                        CURRENT_RMNNG_VL = 0;
                    } else {
                        CURRENT_RMNNG_VL = (CST_RMNNG_VL + ITEM_COST + ASSEMBLY_COST + ADJUST_COST) - (COST_OF_SALES);
                    }
                }

                CST_RMNNG_QTY = EJBCommon.roundIt(CURRENT_RMNNG_QTY, (short) 2);
                CST_RMNNG_VL = EJBCommon.roundIt(CURRENT_RMNNG_VL, (short) 2);
                COST_OF_SALES = EJBCommon.roundIt(COST_OF_SALES, (short) 2);
                ADJUST_COST = EJBCommon.roundIt(ADJUST_COST, (short) 2);
                ITEM_COST = EJBCommon.roundIt(ITEM_COST, (short) 2);

                // invCosting.setCstRemainingQuantity(1);

                invCosting.setCstRemainingQuantity(CST_RMNNG_QTY);
                invCosting.setCstRemainingValue(CST_RMNNG_VL);
                invCosting.setCstCostOfSales(COST_OF_SALES);
                invCosting.setCstItemCost(ITEM_COST);
                invCosting.setCstAdjustCost(ADJUST_COST);

                isFirst = false;

                // invCosting.getArInvoiceLineItem()

            }

        } catch (GlobalNoRecordFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvRepItemCostingControllerBean getAdBrResAll");

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

    private LocalInvCosting getCstIlBeginningBalanceByItemLocationAndDate(LocalInvItemLocation invItemLocation, Date date, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("InvRepItemCostingControllerBean getCstIlBeginningBalanceByItemLocationAndDate");

        LocalInvCosting invCosting = null;

        try {

            GregorianCalendar calendar = new GregorianCalendar();

            if (date != null) {

                calendar.setTime(date);
                calendar.add(GregorianCalendar.DATE, -1);

                Date CST_DT = calendar.getTime();

                try {

                    invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, AD_CMPNY);

                } catch (FinderException ex) {

                }
            }

            return invCosting;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private boolean filterByOptionalCriteria(HashMap criteria, LocalInvItem invItem, Date txnDate, Date earliestDate) {

        Debug.print("InvRepItemCostingControllerBean filterByOptionalCriteria");

        try {

            String itemName = "";
            String itemClass = "";
            String itemCategory = "";
            Date dateFrom = null;
            Date dateTo = null;

            if (criteria.containsKey("itemName")) {
                itemName = (String) criteria.get("itemName");
            }

            if (criteria.containsKey("itemClass")) {
                itemClass = (String) criteria.get("itemClass");
            }

            if (criteria.containsKey("category")) {
                itemCategory = (String) criteria.get("category");
            }

            if (criteria.containsKey("dateFrom")) {
                if (earliestDate == null) {
                    dateFrom = (Date) criteria.get("dateFrom");
                } else {
                    dateFrom = earliestDate;
                }
            }

            if (criteria.containsKey("dateTo")) {
                dateTo = (Date) criteria.get("dateTo");
            }

            if (!itemName.equals("") && !invItem.getIiName().equals(itemName)) {
                return true;
            }

            if (!itemClass.equals("") && !invItem.getIiClass().equals(itemClass)) {
                return true;
            }

            if (!itemCategory.equals("") && !invItem.getIiAdLvCategory().equals(itemCategory)) {
                return true;
            }

            if (dateFrom != null && dateTo != null) {
                if (!(txnDate.getTime() >= dateFrom.getTime() && txnDate.getTime() <= dateTo.getTime())) {
                    return true;
                }
            }

            if (dateFrom != null && dateTo == null) {
                if (!(txnDate.getTime() >= dateFrom.getTime())) {
                    return true;
                }
            }

            if (dateFrom == null && dateTo != null) {
                return txnDate.getTime() > dateTo.getTime();
            }

            return false;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("InvRepItemCostingControllerBean ejbCreate");
    }

}