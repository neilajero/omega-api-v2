/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class InvRepItemLedgerControllerBean
 * @created
 * @author
 */
package com.ejb.txnreports.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.dao.ad.LocalAdLookUpValueHome;
import com.ejb.dao.ap.ILocalApSupplierHome;
import com.ejb.dao.ap.LocalApPurchaseOrderLineHome;
import com.ejb.dao.ap.LocalApVoucherLineItemHome;
import com.ejb.dao.ar.*;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdLookUpValue;
import com.ejb.entities.ap.LocalApPurchaseOrderLine;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.entities.ap.LocalApVoucherLineItem;
import com.ejb.entities.ar.*;
import com.ejb.entities.inv.*;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.reports.inv.InvRepItemLedgerDetails;

import jakarta.ejb.*;
import java.util.*;

@Stateless(name = "InvRepItemLedgerControllerEJB")
public class InvRepItemLedgerControllerBean extends EJBContextClass implements InvRepItemLedgerController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    LocalArCustomerHome arCustomerHome;
    @EJB
    LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    LocalInvLocationHome invLocationHome;
    @EJB
    LocalInvItemLocationHome invItemLocationHome;
    @EJB
    LocalInvCostingHome invCostingHome;
    @EJB
    LocalArSalesOrderLineHome arSalesOrderLineHome;
    @EJB
    LocalArJobOrderLineHome arJobOrderLineHome;
    @EJB
    LocalInvAdjustmentLineHome invAdjustmentLineHome;
    @EJB
    LocalInvStockTransferLineHome invStockTransferLineHome;
    @EJB
    LocalInvBranchStockTransferLineHome invBranchStockTransferLineHome;
    @EJB
    LocalApVoucherLineItemHome apVoucherLineItemHome;
    @EJB
    LocalArInvoiceLineItemHome arInvoiceLineItemHome;
    @EJB
    LocalApPurchaseOrderLineHome apPurchaseOrderLineHome;
    @EJB
    LocalArSalesOrderInvoiceLineHome arSalesOrderInvoiceLineHome;
    @EJB
    LocalArJobOrderInvoiceLineHome arJobOrderInvoiceLineHome;
    @EJB
    LocalInvTagHome invTagHome;
    @EJB
    LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    LocalAdBranchHome adBranchHome;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalApSupplierHome apSupplierHome;

    public ArrayList getApSplAll(Integer AD_CMPNY) {

        Debug.print("InvRepItemLedgerControllerBean getApSplAll");

        ArrayList list = new ArrayList();

        try {

            Collection apSuppliers = apSupplierHome.findEnabledSplAllOrderBySplSupplierCode(AD_CMPNY);

            for (Object supplier : apSuppliers) {
                LocalApSupplier apSupplier = (LocalApSupplier) supplier;
                list.add(apSupplier.getSplSupplierCode());
            }
            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArCstAll(Integer AD_CMPNY) {

        Debug.print("InvRepItemLedgerControllerBean getArCstAll");

        ArrayList list = new ArrayList();

        try {

            Collection arCustomers = arCustomerHome.findEnabledCstAllOrderByCstName(AD_CMPNY);
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

    public ArrayList getAdLvReportTypeAll(Integer AD_CMPNY) {

        Debug.print("InvRepItemLedgerControllerBean getAdLvReportTypeAll");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("INV REPORT TYPE - ITEM LEDGER", AD_CMPNY);
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

    public ArrayList getAdLvInvItemCategoryAll(Integer AD_CMPNY) {

        Debug.print("InvRepItemLedgerControllerBean getAdLvInvItemCategoryAll");

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

        Debug.print("InvRepItemLedgerControllerBean getInvLocAll");

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

    public ArrayList executeInvRepItemLedger(HashMap criteria, boolean SHW_CMMTTD_QNTTY, String referenceNumber, boolean INCLD_UNPSTD, ArrayList branchList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvRepItemLedgerControllerBean executeInvRepItemLedger");

        ArrayList list = new ArrayList();

        try {

            Integer brCode = null;
            Integer locCode = null;
            String locName = "";

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(cst) FROM InvCosting cst ");

            boolean firstArgument = true;

            Object[] obj = new Object[0];

            if (branchList.isEmpty()) {

                throw new GlobalNoRecordFoundException();

            } else {

                jbossQl.append(" WHERE cst.cstAdBranch in (");

                boolean firstLoop = true;

                for (Object o : branchList) {

                    if (firstLoop == false) {
                        jbossQl.append(", ");
                    } else {
                        firstLoop = false;
                    }

                    AdBranchDetails mdetails = (AdBranchDetails) o;

                    jbossQl.append(mdetails.getBrCode());

                    brCode = mdetails.getBrCode();
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

                jbossQl.append("cst.invItemLocation.invItem.iiName  >= '").append(criteria.get("itemName")).append("' ");
            }

            if (criteria.containsKey("itemNameTo")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.invItemLocation.invItem.iiName  <= '").append(criteria.get("itemNameTo")).append("' ");
            }

            if (criteria.containsKey("customerCode")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.invItemLocation.invItem.arCustomer.cstCustomerCode = '").append(criteria.get("customerCode")).append("' ");

            }

            if (criteria.containsKey("supplierCode")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.invItemLocation.invItem.apSupplier.splSupplierCode = '").append(criteria.get("supplierCode")).append("' ");

            }

            if (criteria.containsKey("category")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.invItemLocation.invItem.iiAdLvCategory= '").append(criteria.get("category")).append("' ");

            }

            if (criteria.containsKey("location")) {

                String loc = (String) criteria.get("location");

                if (!loc.equalsIgnoreCase("ALL")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("cst.invItemLocation.invLocation.locName= '").append(criteria.get("location")).append("' ");
                    locName = (String) criteria.get("location");
                } else {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("cst.invItemLocation.invLocation.locName <> 'IN TRANSIT' ");

                }

            }

            List<Date> dateQuery = new ArrayList<>();
            int index = 1;

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.cstDate>=?").append(index++).append(" ");
                dateQuery.add((Date) criteria.get("dateFrom"));

            }

            if (criteria.containsKey("dateTo")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.cstDate<=?").append(index).append(" ");
                dateQuery.add((Date) criteria.get("dateTo"));

            }

            if (dateQuery.size() > 0) {
                obj = new Object[index];
                for (int x = 0; x < dateQuery.size(); x++) {
                    obj[x] = dateQuery.get(x);

                }

            }

            if (criteria.containsKey("itemClass")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.invItemLocation.invItem.iiClass= '").append(criteria.get("itemClass")).append("' ");

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

                InvRepItemLedgerDetails details = new InvRepItemLedgerDetails();

                // beginning
                LocalInvCosting invBeginningCosting = this.getCstIlBeginningBalanceByItemLocationAndDate(invCosting.getInvItemLocation(), (Date) criteria.get("dateFrom"), invCosting.getCstAdBranch(), AD_CMPNY);

                if (invBeginningCosting != null) {

                    details.setRilBeginningQuantity(invBeginningCosting.getCstRemainingQuantity());
                    details.setRilBeginningUnitCost(invBeginningCosting.getCstRemainingValue() / invBeginningCosting.getCstRemainingQuantity());
                    details.setRilBeginningAmount(invBeginningCosting.getCstRemainingValue());

                } else {

                    details.setRilBeginningQuantity(0);
                    details.setRilBeginningUnitCost(0);
                    details.setRilBeginningAmount(0);
                }

                details.setRilItemName(invCosting.getInvItemLocation().getInvItem().getIiName() + "-" + invCosting.getInvItemLocation().getInvItem().getIiDescription());
                details.setRilItemDesc(invCosting.getInvItemLocation().getInvItem().getIiDescription());
                details.setRilUnit(invCosting.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName());
                details.setRilDate(invCosting.getCstDate());
                details.setRilItemCategory(invCosting.getInvItemLocation().getInvItem().getIiAdLvCategory());
                details.setRilReferenceNumber1(referenceNumber);

                if (invCosting.getInvAdjustmentLine() != null) {

                    details.setRilAccount(invCosting.getInvAdjustmentLine().getInvAdjustment().getGlChartOfAccount().getCoaAccountDescription());
                }

                if (invCosting.getInvAdjustmentLine() != null) {

                    details.setRilDocumentNumber(invCosting.getInvAdjustmentLine().getInvAdjustment().getAdjDocumentNumber());
                    details.setRilReferenceNumber(invCosting.getInvAdjustmentLine().getInvAdjustment().getAdjReferenceNumber());
                    details.setRilQcNumber(invCosting.getCstQCNumber());
                    details.setRilLocationName(invCosting.getInvAdjustmentLine().getInvItemLocation().getInvLocation().getLocName());
                    try {
                        details.setRilSupplierName(invCosting.getInvAdjustmentLine().getInvAdjustment().getApSupplier().getSplName());

                    } catch (Exception ex) {
                        details.setRilSupplierName("");
                    }
                    details.setRilSource("INV");

                } else if (invCosting.getInvStockTransferLine() != null) {

                    details.setRilDocumentNumber(invCosting.getInvStockTransferLine().getInvStockTransfer().getStDocumentNumber());
                    details.setRilReferenceNumber(invCosting.getInvStockTransferLine().getInvStockTransfer().getStReferenceNumber());

                    LocalInvLocation invLocation = invLocationHome.findByPrimaryKey(invCosting.getInvStockTransferLine().getStlLocationTo());

                    details.setRilLocationName(invLocation.getLocName());
                    details.setRilQcNumber(invCosting.getCstQCNumber());
                    details.setRilSource("INV");

                } else if (invCosting.getInvBranchStockTransferLine() != null) {
                    details.setRilDocumentNumber(invCosting.getInvBranchStockTransferLine().getInvBranchStockTransfer().getBstNumber());
                    details.setRilReferenceNumber(invCosting.getInvBranchStockTransferLine().getInvBranchStockTransfer().getBstTransferOutNumber());
                    details.setRilSource("INV");

                } else if (invCosting.getApVoucherLineItem() != null) {

                    if (invCosting.getApVoucherLineItem().getApVoucher() != null) {

                        details.setRilDocumentNumber(invCosting.getApVoucherLineItem().getApVoucher().getVouDocumentNumber());
                        details.setRilReferenceNumber(invCosting.getApVoucherLineItem().getApVoucher().getVouReferenceNumber());
                        details.setRilSource("AP");

                    } else if (invCosting.getApVoucherLineItem().getApCheck() != null) {

                        details.setRilDocumentNumber(invCosting.getApVoucherLineItem().getApCheck().getChkDocumentNumber());
                        details.setRilReferenceNumber(invCosting.getApVoucherLineItem().getApCheck().getChkNumber());
                        details.setRilSource("AP");
                    }

                } else if (invCosting.getArInvoiceLineItem() != null) {

                    if (invCosting.getArInvoiceLineItem().getArInvoice() != null) {

                        details.setRilDocumentNumber(invCosting.getArInvoiceLineItem().getArInvoice().getInvNumber());
                        details.setRilReferenceNumber(invCosting.getArInvoiceLineItem().getArInvoice().getInvReferenceNumber());
                        details.setRilSource("AR");

                    } else if (invCosting.getArInvoiceLineItem().getArReceipt() != null) {

                        details.setRilDocumentNumber(invCosting.getArInvoiceLineItem().getArReceipt().getRctNumber());
                        details.setRilReferenceNumber(invCosting.getArInvoiceLineItem().getArReceipt().getRctReferenceNumber());
                        details.setRilQcNumber(invCosting.getCstQCNumber());
                        details.setRilSource("AR");
                    }

                } else if (invCosting.getApPurchaseOrderLine() != null) {

                    details.setRilDocumentNumber(invCosting.getApPurchaseOrderLine().getApPurchaseOrder().getPoDocumentNumber());
                    details.setRilReferenceNumber(invCosting.getApPurchaseOrderLine().getApPurchaseOrder().getPoReferenceNumber());
                    details.setRilSupplierName(invCosting.getApPurchaseOrderLine().getApPurchaseOrder().getApSupplier().getSplSupplierCode());
                    details.setRilQcNumber(invCosting.getCstQCNumber());
                    details.setRilSource("AP");

                } else if (invCosting.getArSalesOrderInvoiceLine() != null) {

                    details.setRilDocumentNumber(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getInvNumber());
                    details.setRilReferenceNumber(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getInvNumber());
                    details.setRilQcNumber(invCosting.getCstQCNumber());
                    details.setRilSource("AR");

                } else if (invCosting.getArJobOrderInvoiceLine() != null) {

                    details.setRilDocumentNumber(invCosting.getArJobOrderInvoiceLine().getArInvoice().getInvNumber());
                    details.setRilReferenceNumber(invCosting.getArJobOrderInvoiceLine().getArInvoice().getInvNumber());
                    details.setRilQcNumber(invCosting.getCstQCNumber());
                    details.setRilSource("AR");
                }

                details.setRilInQuantity(0d);
                details.setRilInAmount(0d);
                details.setRilOutQuantity(0d);
                details.setRilOutAmount(0d);

                double COST = 0d;

                if (invCosting.getCstQuantityReceived() != 0d) {

                    if (invCosting.getCstQuantityReceived() > 0) {

                        details.setRilInQuantity(invCosting.getCstQuantityReceived());
                        details.setRilInAmount(invCosting.getCstItemCost());
                        COST = Math.abs(invCosting.getCstItemCost() / invCosting.getCstQuantityReceived());
                        details.setRilInUnitCost(COST);

                    } else if (invCosting.getCstQuantityReceived() < 0) {

                        details.setRilOutQuantity(invCosting.getCstQuantityReceived() * -1);
                        details.setRilOutAmount(invCosting.getCstItemCost() * -1);
                        COST = Math.abs(invCosting.getCstItemCost() / invCosting.getCstQuantityReceived());
                        details.setRilOutUnitCost(COST);
                    }

                } else if (invCosting.getCstAdjustQuantity() != 0d) {

                    if (invCosting.getCstAdjustQuantity() > 0) {

                        details.setRilInQuantity(invCosting.getCstAdjustQuantity());
                        details.setRilInAmount(invCosting.getCstAdjustCost());
                        COST = Math.abs(invCosting.getCstAdjustCost() / invCosting.getCstAdjustQuantity());
                        details.setRilInUnitCost(COST);

                    } else if (invCosting.getCstAdjustQuantity() < 0) {

                        details.setRilOutQuantity(invCosting.getCstAdjustQuantity() * -1);
                        details.setRilOutAmount(invCosting.getCstAdjustCost() * -1);
                        COST = Math.abs(invCosting.getCstAdjustCost() / invCosting.getCstAdjustQuantity());
                        details.setRilOutUnitCost(COST);
                    }

                } else if (invCosting.getCstQuantitySold() != 0d) {

                    if (invCosting.getCstQuantitySold() > 0) {

                        details.setRilOutQuantity(invCosting.getCstQuantitySold());
                        details.setRilOutAmount(invCosting.getCstCostOfSales());
                        COST = Math.abs(invCosting.getCstCostOfSales() / invCosting.getCstQuantitySold());
                        details.setRilOutUnitCost(COST);

                    } else if (invCosting.getCstQuantitySold() < 0) {

                        details.setRilInQuantity(invCosting.getCstQuantitySold() * -1);
                        details.setRilInAmount(invCosting.getCstCostOfSales() * -1);
                        COST = Math.abs(invCosting.getCstCostOfSales() / invCosting.getCstQuantitySold());
                        details.setRilInUnitCost(COST);
                    }
                }
                // d2
                LocalInvCosting invBeginningCosting1 = this.getCstIlBeginningBalanceByItemLocationAndDate(invCosting.getInvItemLocation(), (Date) criteria.get("dateFrom"), invCosting.getCstAdBranch(), AD_CMPNY);
                if (invBeginningCosting1 != null) {
                    details.setRilBeginningQuantity(invBeginningCosting1.getCstRemainingQuantity());
                    details.setRilBeginningUnitCost(invBeginningCosting1.getCstRemainingValue() / invBeginningCosting1.getCstRemainingQuantity());
                    details.setRilBeginningAmount(invBeginningCosting1.getCstRemainingValue());

                } else {
                    details.setRilRemainingQuantity(0);
                    details.setRilRemainingUnitCost(0);
                    details.setRilRemainingAmount(0);
                }

                if (invCosting.getCstRemainingQuantity() != 0 && invCosting.getCstRemainingValue() != 0) {

                    details.setRilRemainingQuantity(invCosting.getCstRemainingQuantity());
                    details.setRilRemainingUnitCost(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                    details.setRilRemainingAmount(invCosting.getCstRemainingValue());
                }

                list.add(details);
            }

            if (SHW_CMMTTD_QNTTY) {

                Collection arSalesOrderLines = arSalesOrderLineHome.findOpenSolAll(AD_CMPNY);

                for (Object salesOrderLine : arSalesOrderLines) {

                    LocalArSalesOrderLine arSalesOrderLine = (LocalArSalesOrderLine) salesOrderLine;

                    InvRepItemLedgerDetails details = new InvRepItemLedgerDetails();

                    if (arSalesOrderLine.getArSalesOrderInvoiceLines().size() == 0 && arSalesOrderLine.getArSalesOrder().getSoAdBranch().equals(brCode) && arSalesOrderLine.getInvItemLocation().getInvLocation().getLocName().equals(locName)) {

                        Date earliestDate = null;
                        Date soDate = arSalesOrderLine.getArSalesOrder().getSoDate();
                        LocalInvItem invItem = arSalesOrderLine.getInvItemLocation().getInvItem();

                        LocalInvCosting invBeginningCosting = this.getCstIlBeginningBalanceByItemLocationAndDate(arSalesOrderLine.getInvItemLocation(), (Date) criteria.get("dateFrom"), brCode, AD_CMPNY);
                        if (invBeginningCosting != null) {

                            details.setRilBeginningQuantity(invBeginningCosting.getCstRemainingQuantity());
                            details.setRilBeginningUnitCost(invBeginningCosting.getCstRemainingValue() / invBeginningCosting.getCstRemainingQuantity());
                            details.setRilBeginningAmount(invBeginningCosting.getCstRemainingValue());
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

                        details.setRilDate(soDate);
                        details.setRilDocumentNumber(arSalesOrderLine.getArSalesOrder().getSoDocumentNumber());
                        details.setRilItemName(invItem.getIiName() + " - " + invItem.getIiDescription());
                        details.setRilItemDesc(invItem.getIiDescription());
                        details.setRilOutAmount(arSalesOrderLine.getSolAmount());
                        details.setRilOutQuantity(arSalesOrderLine.getSolQuantity());
                        details.setRilOutUnitCost(arSalesOrderLine.getSolUnitPrice());
                        details.setRilReferenceNumber(arSalesOrderLine.getArSalesOrder().getSoReferenceNumber());
                        details.setRilSource("AR");
                        details.setRilUnit(arSalesOrderLine.getInvUnitOfMeasure().getUomName());
                        details.setRilReferenceNumber1(referenceNumber);
                        details.setRilItemCategory(invItem.getIiAdLvCategory());
                        list.add(details);
                    }
                }

                Collection arJobOrderLines = arJobOrderLineHome.findOpenJolAll(AD_CMPNY);

                for (Object jobOrderLine : arJobOrderLines) {

                    LocalArJobOrderLine arJobOrderLine = (LocalArJobOrderLine) jobOrderLine;

                    InvRepItemLedgerDetails details = new InvRepItemLedgerDetails();

                    if (arJobOrderLine.getArJobOrderInvoiceLines().size() == 0 && arJobOrderLine.getArJobOrder().getJoAdBranch().equals(brCode) && arJobOrderLine.getInvItemLocation().getInvLocation().getLocName().equals(locName)) {

                        Date earliestDate = null;
                        Date joDate = arJobOrderLine.getArJobOrder().getJoDate();
                        LocalInvItem invItem = arJobOrderLine.getInvItemLocation().getInvItem();

                        LocalInvCosting invBeginningCosting = this.getCstIlBeginningBalanceByItemLocationAndDate(arJobOrderLine.getInvItemLocation(), (Date) criteria.get("dateFrom"), brCode, AD_CMPNY);
                        if (invBeginningCosting != null) {

                            details.setRilBeginningQuantity(invBeginningCosting.getCstRemainingQuantity());
                            details.setRilBeginningUnitCost(invBeginningCosting.getCstRemainingValue() / invBeginningCosting.getCstRemainingQuantity());
                            details.setRilBeginningAmount(invBeginningCosting.getCstRemainingValue());
                            earliestDate = invBeginningCosting.getCstDate();
                        }

                        GregorianCalendar gcDateFrom = new GregorianCalendar();
                        Date dateTo = (Date) criteria.get("dateTo");
                        if (dateTo == null) {
                            dateTo = new Date();
                        }
                        Date joDateFrom = null;
                        gcDateFrom.setTime(dateTo != null ? dateTo : new Date());
                        gcDateFrom.add(Calendar.MONTH, -1);

                        joDateFrom = gcDateFrom.getTime();

                        if (joDate.compareTo(joDateFrom) < 0) {
                            continue;
                        }

                        if (dateTo != null && joDate.compareTo(dateTo) > 0) {
                            continue;
                        }

                        if (this.filterByOptionalCriteria(criteria, invItem, joDate, earliestDate)) {
                            continue;
                        }

                        details.setRilDate(joDate);
                        details.setRilDocumentNumber(arJobOrderLine.getArJobOrder().getJoDocumentNumber());
                        details.setRilItemName(invItem.getIiName() + " - " + invItem.getIiDescription());
                        details.setRilItemDesc(invItem.getIiDescription());
                        details.setRilOutAmount(arJobOrderLine.getJolAmount());
                        details.setRilOutQuantity(arJobOrderLine.getJolQuantity());
                        details.setRilOutUnitCost(arJobOrderLine.getJolUnitPrice());
                        details.setRilReferenceNumber(arJobOrderLine.getArJobOrder().getJoReferenceNumber());
                        details.setRilSource("AR");
                        details.setRilUnit(arJobOrderLine.getInvUnitOfMeasure().getUomName());
                        details.setRilReferenceNumber1(referenceNumber);
                        details.setRilItemCategory(invItem.getIiAdLvCategory());
                        list.add(details);
                    }
                }
            }

            if (criteria.containsKey("propertyCode")) {
                try {
                    Collection invTags = invTagHome.findByTgPropertyCode((String) criteria.get("propertyCode"), AD_CMPNY);
                    for (Object tag : invTags) {
                        LocalInvTag invTag = (LocalInvTag) tag;
                        InvRepItemLedgerDetails details = new InvRepItemLedgerDetails();

                        try {
                            if (invTag.getApPurchaseOrderLine().getPlCode() != null && invTag.getApPurchaseOrderLine().getInvItemLocation().getInvLocation().getLocName().equals(locName)) {
                                details.setRilSource("AP");
                                details.setRilDocumentNumber(invTag.getApPurchaseOrderLine().getApPurchaseOrder().getPoDocumentNumber());
                                details.setRilReferenceNumber(invTag.getApPurchaseOrderLine().getApPurchaseOrder().getPoReferenceNumber());
                                details.setRilItemName(invTag.getApPurchaseOrderLine().getInvItemLocation().getInvItem().getIiName() + "-" + invTag.getApPurchaseOrderLine().getInvItemLocation().getInvItem().getIiDescription());
                                details.setRilUnit(invTag.getApPurchaseOrderLine().getInvUnitOfMeasure().getUomName());
                                details.setRilInAmount(invTag.getApPurchaseOrderLine().getPlAmount());
                            } else {
                                break;
                            }
                        } catch (Exception ex) {
                        }
                        try {
                            if (invTag.getInvAdjustmentLine().getAlCode() != null && invTag.getInvAdjustmentLine().getInvItemLocation().getInvLocation().getLocName().equals(locName)) {

                                details.setRilSource("INV");
                                details.setRilDocumentNumber(invTag.getInvAdjustmentLine().getInvAdjustment().getAdjDocumentNumber());
                                details.setRilReferenceNumber(invTag.getInvAdjustmentLine().getInvAdjustment().getAdjReferenceNumber());
                                details.setRilItemName(invTag.getInvAdjustmentLine().getInvItemLocation().getInvItem().getIiName() + "-" + invTag.getInvAdjustmentLine().getInvItemLocation().getInvItem().getIiDescription());
                                details.setRilUnit(invTag.getInvAdjustmentLine().getInvUnitOfMeasure().getUomName());
                                if (invTag.getTgType().equals("IN")) {
                                    details.setRilInAmount(invTag.getInvAdjustmentLine().getAlUnitCost());
                                }
                                if (invTag.getTgType().equals("OUT")) {
                                    details.setRilOutAmount(invTag.getInvAdjustmentLine().getAlUnitCost());
                                }
                            } else {
                                break;
                            }
                        } catch (Exception ex) {
                        }
                        details.setRilDate(invTag.getTgTransactionDate());
                        details.setRilCustodian(invTag.getAdUser().getUsrDescription());

                        if (invTag.getTgType().equals("IN")) {
                            details.setRilInQuantity(1);
                        }
                        if (invTag.getTgType().equals("OUT")) {
                            details.setRilOutQuantity(1);
                        }
                        list.add(details);
                    }
                } catch (Exception ex) {
                    throw new GlobalNoRecordFoundException();
                }
            }

            if (INCLD_UNPSTD) {

                // AP_VOUCHER_LINE_ITEM
                // a) apVoucher
                Collection apVoucherLineItems = apVoucherLineItemHome.findUnpostedVouByLocNameAndAdBranch(locName, brCode, AD_CMPNY);

                Iterator unpstdIter = apVoucherLineItems.iterator();

                while (unpstdIter.hasNext()) {

                    LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) unpstdIter.next();

                    InvRepItemLedgerDetails details = new InvRepItemLedgerDetails();

                    Date earliestDate = null;
                    Date vouDate = apVoucherLineItem.getApVoucher().getVouDate();
                    LocalInvItem invItem = apVoucherLineItem.getInvItemLocation().getInvItem();

                    LocalInvCosting invBeginningCosting = this.getCstIlBeginningBalanceByItemLocationAndDate(apVoucherLineItem.getInvItemLocation(), (Date) criteria.get("dateFrom"), brCode, AD_CMPNY);
                    if (invBeginningCosting != null) {

                        details.setRilBeginningQuantity(invBeginningCosting.getCstRemainingQuantity());
                        details.setRilBeginningUnitCost(invBeginningCosting.getCstRemainingValue() / invBeginningCosting.getCstRemainingQuantity());
                        details.setRilBeginningAmount(invBeginningCosting.getCstRemainingValue());

                        earliestDate = invBeginningCosting.getCstDate();
                    }

                    if (this.filterByOptionalCriteria(criteria, invItem, vouDate, earliestDate)) {
                        continue;
                    }

                    details.setRilInQuantity(apVoucherLineItem.getVliQuantity());
                    details.setRilInAmount(apVoucherLineItem.getVliAmount());
                    details.setRilInUnitCost(apVoucherLineItem.getVliUnitCost());

                    details.setRilDate(vouDate);
                    details.setRilDocumentNumber(apVoucherLineItem.getApVoucher().getVouDocumentNumber());
                    details.setRilItemName(invItem.getIiName() + "-" + invItem.getIiDescription());
                    details.setRilReferenceNumber(apVoucherLineItem.getApVoucher().getVouReferenceNumber());
                    details.setRilSource("AP");
                    details.setRilUnit(apVoucherLineItem.getInvUnitOfMeasure().getUomName());
                    details.setRilReferenceNumber1(referenceNumber);
                    details.setRilItemCategory(invItem.getIiAdLvCategory());
                    list.add(details);
                }

                // b) apCheck
                apVoucherLineItems = apVoucherLineItemHome.findUnpostedChkByLocNameAndAdBranch(locName, brCode, AD_CMPNY);

                unpstdIter = apVoucherLineItems.iterator();

                while (unpstdIter.hasNext()) {

                    LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) unpstdIter.next();

                    InvRepItemLedgerDetails details = new InvRepItemLedgerDetails();

                    Date earliestDate = null;
                    Date chkDate = apVoucherLineItem.getApCheck().getChkDate();
                    LocalInvItem invItem = apVoucherLineItem.getInvItemLocation().getInvItem();

                    LocalInvCosting invBeginningCosting = this.getCstIlBeginningBalanceByItemLocationAndDate(apVoucherLineItem.getInvItemLocation(), (Date) criteria.get("dateFrom"), brCode, AD_CMPNY);
                    if (invBeginningCosting != null) {

                        details.setRilBeginningQuantity(invBeginningCosting.getCstRemainingQuantity());
                        details.setRilBeginningUnitCost(invBeginningCosting.getCstRemainingValue() / invBeginningCosting.getCstRemainingQuantity());
                        details.setRilBeginningAmount(invBeginningCosting.getCstRemainingValue());
                        earliestDate = invBeginningCosting.getCstDate();
                    }

                    if (this.filterByOptionalCriteria(criteria, invItem, chkDate, earliestDate)) {
                        continue;
                    }

                    details.setRilInQuantity(apVoucherLineItem.getVliQuantity());
                    details.setRilInAmount(apVoucherLineItem.getVliAmount());
                    details.setRilInUnitCost(apVoucherLineItem.getVliUnitCost());

                    details.setRilDate(chkDate);
                    details.setRilDocumentNumber(apVoucherLineItem.getApCheck().getChkDocumentNumber());
                    details.setRilItemName(invItem.getIiName() + "-" + invItem.getIiDescription());
                    details.setRilReferenceNumber(apVoucherLineItem.getApCheck().getChkReferenceNumber());
                    details.setRilSource("AP");
                    details.setRilUnit(apVoucherLineItem.getInvUnitOfMeasure().getUomName());
                    details.setRilReferenceNumber1(referenceNumber);
                    details.setRilItemCategory(invItem.getIiAdLvCategory());
                    list.add(details);
                }

                // AP_PURCHASE_ORDER_LINE
                Collection apPurchaseOrderLines = apPurchaseOrderLineHome.findUnpostedPoByLocNameAndAdBranch(locName, brCode, AD_CMPNY);

                unpstdIter = apPurchaseOrderLines.iterator();

                while (unpstdIter.hasNext()) {

                    LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) unpstdIter.next();

                    InvRepItemLedgerDetails details = new InvRepItemLedgerDetails();

                    Date earliestDate = null;
                    Date poDate = apPurchaseOrderLine.getApPurchaseOrder().getPoDate();
                    LocalInvItem invItem = apPurchaseOrderLine.getInvItemLocation().getInvItem();

                    LocalInvCosting invBeginningCosting = this.getCstIlBeginningBalanceByItemLocationAndDate(apPurchaseOrderLine.getInvItemLocation(), (Date) criteria.get("dateFrom"), brCode, AD_CMPNY);
                    if (invBeginningCosting != null) {

                        details.setRilBeginningQuantity(invBeginningCosting.getCstRemainingQuantity());
                        details.setRilBeginningUnitCost(invBeginningCosting.getCstRemainingValue() / invBeginningCosting.getCstRemainingQuantity());
                        details.setRilBeginningAmount(invBeginningCosting.getCstRemainingValue());
                        earliestDate = invBeginningCosting.getCstDate();
                    }

                    if (this.filterByOptionalCriteria(criteria, invItem, poDate, earliestDate)) {
                        continue;
                    }

                    details.setRilInQuantity(apPurchaseOrderLine.getPlQuantity());
                    details.setRilQcNumber(apPurchaseOrderLine.getPlQcNumber());
                    details.setRilLocationName(apPurchaseOrderLine.getInvItemLocation().getInvLocation().getLocName());
                    details.setRilInAmount(apPurchaseOrderLine.getPlAmount());
                    // details.setRilInUnitCost(apPurchaseOrderLine.getPlUnitCost());
                    details.setRilInUnitCost(apPurchaseOrderLine.getPlAmount() / apPurchaseOrderLine.getPlQuantity());

                    details.setRilDate(poDate);
                    details.setRilDocumentNumber(apPurchaseOrderLine.getApPurchaseOrder().getPoDocumentNumber());
                    details.setRilItemName(invItem.getIiName() + " - " + invItem.getIiDescription());
                    details.setRilItemDesc(invItem.getIiDescription());
                    details.setRilQcNumber(apPurchaseOrderLine.getPlQcNumber());
                    details.setRilSupplierName(apPurchaseOrderLine.getApPurchaseOrder().getApSupplier().getSplName());
                    details.setRilReferenceNumber(apPurchaseOrderLine.getApPurchaseOrder().getPoReferenceNumber());
                    details.setRilSource("AP");
                    details.setRilUnit(apPurchaseOrderLine.getInvUnitOfMeasure().getUomName());
                    details.setRilReferenceNumber1(referenceNumber);
                    details.setRilItemCategory(invItem.getIiAdLvCategory());
                    list.add(details);
                }

                // INV_ADJUSTMENT_LINE
                Collection invAdjustmentLines = invAdjustmentLineHome.findUnpostedAdjByLocNameAndAdBranch(locName, brCode, AD_CMPNY);

                unpstdIter = invAdjustmentLines.iterator();

                while (unpstdIter.hasNext()) {

                    LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) unpstdIter.next();

                    InvRepItemLedgerDetails details = new InvRepItemLedgerDetails();

                    Date earliestDate = null;
                    Date adjDate = invAdjustmentLine.getInvAdjustment().getAdjDate();
                    LocalInvItem invItem = invAdjustmentLine.getInvItemLocation().getInvItem();

                    LocalInvCosting invBeginningCosting = this.getCstIlBeginningBalanceByItemLocationAndDate(invAdjustmentLine.getInvItemLocation(), (Date) criteria.get("dateFrom"), brCode, AD_CMPNY);

                    if (invBeginningCosting != null) {

                        details.setRilBeginningQuantity(invBeginningCosting.getCstRemainingQuantity());
                        details.setRilBeginningUnitCost(invBeginningCosting.getCstRemainingValue() / invBeginningCosting.getCstRemainingQuantity());
                        details.setRilBeginningAmount(invBeginningCosting.getCstRemainingValue());
                        earliestDate = invBeginningCosting.getCstDate();
                    }

                    if (this.filterByOptionalCriteria(criteria, invItem, adjDate, earliestDate)) {
                        continue;
                    }

                    if (invAdjustmentLine.getAlAdjustQuantity() > 0) {

                        details.setRilInQuantity(invAdjustmentLine.getAlAdjustQuantity());
                        details.setRilInAmount(invAdjustmentLine.getAlUnitCost() * invAdjustmentLine.getAlAdjustQuantity());
                        details.setRilInUnitCost(invAdjustmentLine.getAlUnitCost());

                    } else if (invAdjustmentLine.getAlAdjustQuantity() < 0) {

                        details.setRilOutQuantity(invAdjustmentLine.getAlAdjustQuantity() * -1);
                        details.setRilOutAmount(invAdjustmentLine.getAlUnitCost() * invAdjustmentLine.getAlAdjustQuantity() * -1);
                        details.setRilOutUnitCost(invAdjustmentLine.getAlUnitCost());
                    }

                    details.setRilDate(adjDate);
                    details.setRilDocumentNumber(invAdjustmentLine.getInvAdjustment().getAdjDocumentNumber());
                    details.setRilItemName(invItem.getIiName() + " - " + invItem.getIiDescription());
                    details.setRilItemDesc(invItem.getIiDescription());
                    details.setRilReferenceNumber(invAdjustmentLine.getInvAdjustment().getAdjReferenceNumber());
                    details.setRilSource("INV");
                    details.setRilQcNumber(invAdjustmentLine.getAlQcNumber());
                    try {
                        details.setRilSupplierName(invAdjustmentLine.getInvAdjustment().getApSupplier().getSplName());

                    } catch (Exception ex) {
                        details.setRilSupplierName("");
                    }
                    details.setRilUnit(invAdjustmentLine.getInvUnitOfMeasure().getUomName());
                    details.setRilReferenceNumber1(referenceNumber);
                    details.setRilItemCategory(invItem.getIiAdLvCategory());

                    list.add(details);
                }

                // INV_STOCK_TRANSFER_LINE OUT
                try {
                    locCode = invLocationHome.findByLocName(locName, AD_CMPNY).getLocCode();
                } catch (Exception ex) {
                    throw new GlobalNoRecordFoundException();
                }

                Collection invStockTransferLines = invStockTransferLineHome.findUnpostedStByLocCodeAndAdBranch(locCode, brCode, AD_CMPNY);

                unpstdIter = invStockTransferLines.iterator();

                while (unpstdIter.hasNext()) {

                    LocalInvStockTransferLine invStockTransferLine = (LocalInvStockTransferLine) unpstdIter.next();

                    InvRepItemLedgerDetails details = new InvRepItemLedgerDetails();

                    Date earliestDate = null;
                    Date stDate = invStockTransferLine.getInvStockTransfer().getStDate();
                    LocalInvItem invItem = invStockTransferLine.getInvItem();
                    LocalInvLocation invLocation = invLocationHome.findByPrimaryKey(invStockTransferLine.getStlLocationFrom());
                    LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(invItem.getIiName(), invLocation.getLocName(), AD_CMPNY);

                    LocalInvCosting invBeginningCosting = this.getCstIlBeginningBalanceByItemLocationAndDate(invItemLocation, criteria.get("dateFrom") == null ? new java.util.Date() : (Date) criteria.get("dateFrom"), brCode, AD_CMPNY);

                    if (invBeginningCosting != null) {
                        details.setRilBeginningQuantity(invBeginningCosting.getCstRemainingQuantity());
                        details.setRilBeginningUnitCost(invBeginningCosting.getCstRemainingValue() / invBeginningCosting.getCstRemainingQuantity());
                        details.setRilBeginningAmount(invBeginningCosting.getCstRemainingValue());
                        earliestDate = invBeginningCosting.getCstDate();
                    }

                    if (this.filterByOptionalCriteria(criteria, invItem, stDate, earliestDate)) {
                        continue;
                    }

                    if (invStockTransferLine.getStlQuantityDelivered() < 0) {

                        details.setRilInQuantity(invStockTransferLine.getStlQuantityDelivered());
                        details.setRilInAmount(invStockTransferLine.getStlAmount());
                        details.setRilInUnitCost(invStockTransferLine.getStlUnitCost() / invStockTransferLine.getStlQuantityDelivered());

                    } else if (invStockTransferLine.getStlQuantityDelivered() > 0) {

                        details.setRilOutQuantity(invStockTransferLine.getStlQuantityDelivered());
                        details.setRilOutAmount(invStockTransferLine.getStlAmount());
                        details.setRilOutUnitCost(invStockTransferLine.getStlUnitCost() / invStockTransferLine.getStlQuantityDelivered());
                    }

                    details.setRilDate(stDate);
                    details.setRilDocumentNumber(invStockTransferLine.getInvStockTransfer().getStDocumentNumber());
                    details.setRilItemName(invItem.getIiName() + " - " + invItem.getIiDescription());
                    details.setRilItemDesc(invItem.getIiDescription());

                    LocalInvLocation invLocationTo = invLocationHome.findByPrimaryKey(invStockTransferLine.getStlLocationTo());

                    details.setRilReferenceNumber(invStockTransferLine.getInvStockTransfer().getStReferenceNumber());
                    details.setRilSource("INV");
                    details.setRilUnit(invStockTransferLine.getInvUnitOfMeasure().getUomName());
                    details.setRilReferenceNumber1(referenceNumber);
                    details.setRilItemCategory(invItem.getIiAdLvCategory());
                    list.add(details);
                }

                // INV_STOCK_TRANSFER_LINE IN
                try {
                    locCode = invLocationHome.findByLocName(locName, AD_CMPNY).getLocCode();
                } catch (Exception ex) {
                    throw new GlobalNoRecordFoundException();
                }
                Collection invStockTransferToLines = invStockTransferLineHome.findUnpostedStByLocToCodeAndAdBranch(locCode, brCode, AD_CMPNY);

                unpstdIter = invStockTransferToLines.iterator();

                while (unpstdIter.hasNext()) {

                    LocalInvStockTransferLine invStockTransferLine = (LocalInvStockTransferLine) unpstdIter.next();

                    InvRepItemLedgerDetails details = new InvRepItemLedgerDetails();

                    Date earliestDate = null;
                    Date stDate = invStockTransferLine.getInvStockTransfer().getStDate();
                    LocalInvItem invItem = invStockTransferLine.getInvItem();
                    LocalInvLocation invLocation = invLocationHome.findByPrimaryKey(invStockTransferLine.getStlLocationFrom());
                    LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(invItem.getIiName(), invLocation.getLocName(), AD_CMPNY);

                    LocalInvCosting invBeginningCosting = this.getCstIlBeginningBalanceByItemLocationAndDate(invItemLocation, (Date) criteria.get("dateFrom"), brCode, AD_CMPNY);
                    if (invBeginningCosting != null) {

                        details.setRilBeginningQuantity(invBeginningCosting.getCstRemainingQuantity());
                        details.setRilBeginningUnitCost(invBeginningCosting.getCstRemainingValue() / invBeginningCosting.getCstRemainingQuantity());
                        details.setRilBeginningAmount(invBeginningCosting.getCstRemainingValue());
                        earliestDate = invBeginningCosting.getCstDate();
                    }

                    if (this.filterByOptionalCriteria(criteria, invItem, stDate, earliestDate)) {
                        continue;
                    }

                    if (invStockTransferLine.getStlQuantityDelivered() > 0) {

                        details.setRilInQuantity(invStockTransferLine.getStlQuantityDelivered());
                        details.setRilInAmount(invStockTransferLine.getStlAmount());
                        details.setRilInUnitCost(invStockTransferLine.getStlUnitCost());

                    } else if (invStockTransferLine.getStlQuantityDelivered() < 0) {

                        details.setRilOutQuantity(invStockTransferLine.getStlQuantityDelivered());
                        details.setRilOutAmount(invStockTransferLine.getStlAmount());
                        details.setRilOutUnitCost(invStockTransferLine.getStlUnitCost());
                    }

                    details.setRilDate(stDate);
                    details.setRilDocumentNumber(invStockTransferLine.getInvStockTransfer().getStDocumentNumber());
                    details.setRilItemName(invItem.getIiName() + " - " + invItem.getIiDescription());
                    details.setRilItemDesc(invItem.getIiDescription());

                    details.setRilReferenceNumber(invStockTransferLine.getInvStockTransfer().getStReferenceNumber());

                    details.setRilQcNumber(invBeginningCosting != null ? invBeginningCosting.getCstQCNumber() : "");
                    details.setRilSource("INV");
                    details.setRilUnit(invStockTransferLine.getInvUnitOfMeasure().getUomName());
                    details.setRilReferenceNumber1(referenceNumber);
                    details.setRilItemCategory(invItem.getIiAdLvCategory());
                    list.add(details);
                }

                // INV_BRANCH_STOCK_TRANSFER_LINE
                Collection invBranchStockTransferLines = invBranchStockTransferLineHome.findUnpostedBstByLocNameAndAdBranch(locName, brCode, AD_CMPNY);

                unpstdIter = invBranchStockTransferLines.iterator();

                while (unpstdIter.hasNext()) {

                    LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) unpstdIter.next();

                    InvRepItemLedgerDetails details = new InvRepItemLedgerDetails();

                    Date earliestDate = null;
                    Date bstDate = invBranchStockTransferLine.getInvBranchStockTransfer().getBstDate();
                    LocalInvItem invItem = invBranchStockTransferLine.getInvItemLocation().getInvItem();

                    LocalInvCosting invBeginningCosting = this.getCstIlBeginningBalanceByItemLocationAndDate(invBranchStockTransferLine.getInvItemLocation(), (Date) criteria.get("dateFrom"), brCode, AD_CMPNY);
                    if (invBeginningCosting != null) {

                        details.setRilBeginningQuantity(invBeginningCosting.getCstRemainingQuantity());
                        details.setRilBeginningUnitCost(invBeginningCosting.getCstRemainingValue() / invBeginningCosting.getCstRemainingQuantity());
                        details.setRilBeginningAmount(invBeginningCosting.getCstRemainingValue());
                        earliestDate = invBeginningCosting.getCstDate();
                    }

                    if (this.filterByOptionalCriteria(criteria, invItem, bstDate, earliestDate)) {
                        continue;
                    }

                    if (invBranchStockTransferLine.getBslQuantityReceived() > 0) {

                        details.setRilInQuantity(invBranchStockTransferLine.getBslQuantityReceived());
                        details.setRilInAmount(invBranchStockTransferLine.getBslAmount());
                        details.setRilInUnitCost(invBranchStockTransferLine.getBslUnitCost());

                    } else if (invBranchStockTransferLine.getBslQuantityReceived() < 0) {

                        details.setRilOutQuantity(invBranchStockTransferLine.getBslQuantityReceived() * -1);
                        details.setRilOutAmount(invBranchStockTransferLine.getBslAmount() * -1);
                        details.setRilOutUnitCost(invBranchStockTransferLine.getBslUnitCost());
                    }

                    details.setRilDate(bstDate);
                    details.setRilDocumentNumber(invBranchStockTransferLine.getInvBranchStockTransfer().getBstNumber());
                    details.setRilItemName(invItem.getIiName() + "-" + invItem.getIiDescription());
                    details.setRilSource("INV");
                    details.setRilUnit(invBranchStockTransferLine.getInvUnitOfMeasure().getUomName());
                    details.setRilReferenceNumber1(referenceNumber);
                    details.setRilItemCategory(invItem.getIiAdLvCategory());
                    list.add(details);
                }

                // AR_INVOICE_LINE_ITEM
                // a) arInvoice
                Collection arInvoiceLineItems = arInvoiceLineItemHome.findUnpostedInvcByLocNameAndAdBranch(locName, brCode, AD_CMPNY);

                unpstdIter = arInvoiceLineItems.iterator();

                while (unpstdIter.hasNext()) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) unpstdIter.next();

                    InvRepItemLedgerDetails details = new InvRepItemLedgerDetails();
                    details.setRilReferenceNumber1(referenceNumber);
                    Date earliestDate = null;
                    Date invDate = arInvoiceLineItem.getArInvoice().getInvDate();
                    LocalInvItem invItem = arInvoiceLineItem.getInvItemLocation().getInvItem();

                    LocalInvCosting invBeginningCosting = this.getCstIlBeginningBalanceByItemLocationAndDate(arInvoiceLineItem.getInvItemLocation(), (Date) criteria.get("dateFrom"), brCode, AD_CMPNY);
                    if (invBeginningCosting != null) {

                        details.setRilBeginningQuantity(invBeginningCosting.getCstRemainingQuantity());
                        details.setRilBeginningUnitCost(invBeginningCosting.getCstRemainingValue() / invBeginningCosting.getCstRemainingQuantity());
                        details.setRilBeginningAmount(invBeginningCosting.getCstRemainingValue());
                        earliestDate = invBeginningCosting.getCstDate();
                    }

                    if (this.filterByOptionalCriteria(criteria, invItem, invDate, earliestDate)) {
                        continue;
                    }

                    // Get Last Costing
                    double COST = 0;
                    try {
                        LocalInvCosting invLastCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(invDate, arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName(), arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName(), brCode, AD_CMPNY);

                        COST = Math.abs(invLastCosting.getCstRemainingValue() / invLastCosting.getCstRemainingQuantity());

                    } catch (Exception e) {
                        COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();
                    }

                    COST = EJBCommon.roundIt(COST, this.getGlFcPrecisionUnit(AD_CMPNY));
                    double CST_CST_OF_SLS = COST * arInvoiceLineItem.getIliQuantity();
                    CST_CST_OF_SLS = EJBCommon.roundIt(CST_CST_OF_SLS, this.getGlFcPrecisionUnit(AD_CMPNY));

                    details.setRilOutQuantity(arInvoiceLineItem.getIliQuantity());
                    details.setRilOutUnitCost(COST);
                    details.setRilOutAmount(CST_CST_OF_SLS);

                    details.setRilDate(invDate);
                    details.setRilDocumentNumber(arInvoiceLineItem.getArInvoice().getInvNumber());
                    details.setRilItemName(invItem.getIiName() + "-" + invItem.getIiDescription());
                    details.setRilReferenceNumber(arInvoiceLineItem.getArInvoice().getInvReferenceNumber());
                    details.setRilSource("AR");
                    details.setRilUnit(arInvoiceLineItem.getInvUnitOfMeasure().getUomName());
                    details.setRilItemCategory(invItem.getIiAdLvCategory());
                    list.add(details);
                }

                // b) arReceipt
                arInvoiceLineItems = arInvoiceLineItemHome.findUnpostedRctByLocNameAndAdBranch(locName, brCode, AD_CMPNY);

                unpstdIter = arInvoiceLineItems.iterator();

                while (unpstdIter.hasNext()) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) unpstdIter.next();

                    InvRepItemLedgerDetails details = new InvRepItemLedgerDetails();

                    Date earliestDate = null;
                    Date rctDate = arInvoiceLineItem.getArReceipt().getRctDate();
                    LocalInvItem invItem = arInvoiceLineItem.getInvItemLocation().getInvItem();

                    LocalInvCosting invBeginningCosting = this.getCstIlBeginningBalanceByItemLocationAndDate(arInvoiceLineItem.getInvItemLocation(), (Date) criteria.get("dateFrom"), brCode, AD_CMPNY);
                    if (invBeginningCosting != null) {

                        details.setRilBeginningQuantity(invBeginningCosting.getCstRemainingQuantity());
                        details.setRilBeginningUnitCost(invBeginningCosting.getCstRemainingValue() / invBeginningCosting.getCstRemainingQuantity());
                        details.setRilBeginningAmount(invBeginningCosting.getCstRemainingValue());
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

                    details.setRilOutQuantity(arInvoiceLineItem.getIliQuantity());
                    details.setRilOutAmount(CST_CST_OF_SLS);
                    details.setRilOutUnitCost(COST);

                    details.setRilDate(rctDate);
                    details.setRilDocumentNumber(arInvoiceLineItem.getArReceipt().getRctNumber());
                    details.setRilItemName(invItem.getIiName() + "-" + invItem.getIiDescription());
                    details.setRilReferenceNumber(arInvoiceLineItem.getArReceipt().getRctReferenceNumber());
                    details.setRilSource("AR");
                    details.setRilUnit(arInvoiceLineItem.getInvUnitOfMeasure().getUomName());
                    details.setRilReferenceNumber1(referenceNumber);
                    details.setRilItemCategory(invItem.getIiAdLvCategory());
                    list.add(details);
                }

                // AR_SALES_ORDER_INVOICE_LINE
                Collection arSalesOrderInvoiceLines = arSalesOrderInvoiceLineHome.findUnpostedSoInvByLocNameAndAdBranch(locName, brCode, AD_CMPNY);

                unpstdIter = arSalesOrderInvoiceLines.iterator();

                while (unpstdIter.hasNext()) {

                    LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) unpstdIter.next();

                    InvRepItemLedgerDetails details = new InvRepItemLedgerDetails();

                    Date earliestDate = null;
                    Date invDate = arSalesOrderInvoiceLine.getArInvoice().getInvDate();
                    LocalInvItem invItem = arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvItem();

                    LocalInvCosting invBeginningCosting = this.getCstIlBeginningBalanceByItemLocationAndDate(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation(), (Date) criteria.get("dateFrom"), brCode, AD_CMPNY);
                    if (invBeginningCosting != null) {

                        details.setRilBeginningQuantity(invBeginningCosting.getCstRemainingQuantity());
                        details.setRilBeginningUnitCost(invBeginningCosting.getCstRemainingValue() / invBeginningCosting.getCstRemainingQuantity());
                        details.setRilBeginningAmount(invBeginningCosting.getCstRemainingValue());
                        earliestDate = invBeginningCosting.getCstDate();
                    }

                    if (this.filterByOptionalCriteria(criteria, invItem, invDate, earliestDate)) {
                        continue;
                    }

                    details.setRilOutQuantity(arSalesOrderInvoiceLine.getSilQuantityDelivered());
                    details.setRilOutAmount(arSalesOrderInvoiceLine.getSilAmount());
                    details.setRilOutUnitCost(Math.abs(arSalesOrderInvoiceLine.getSilAmount() / arSalesOrderInvoiceLine.getSilQuantityDelivered()));

                    details.setRilDate(invDate);
                    details.setRilDocumentNumber(arSalesOrderInvoiceLine.getArInvoice().getInvNumber());
                    details.setRilItemName(invItem.getIiName() + "-" + invItem.getIiDescription());
                    details.setRilReferenceNumber(arSalesOrderInvoiceLine.getArInvoice().getInvReferenceNumber());
                    details.setRilSource("AR");
                    details.setRilUnit(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvUnitOfMeasure().getUomName());
                    details.setRilReferenceNumber1(referenceNumber);
                    details.setRilItemCategory(invItem.getIiAdLvCategory());
                    list.add(details);
                }

                // AR_SALES_ORDER_INVOICE_LINE
                Collection arJobOrderInvoiceLines = arJobOrderInvoiceLineHome.findUnpostedJoInvByLocNameAndAdBranch(locName, brCode, AD_CMPNY);

                unpstdIter = arJobOrderInvoiceLines.iterator();

                while (unpstdIter.hasNext()) {

                    LocalArJobOrderInvoiceLine arJobOrderInvoiceLine = (LocalArJobOrderInvoiceLine) unpstdIter.next();

                    InvRepItemLedgerDetails details = new InvRepItemLedgerDetails();

                    Date earliestDate = null;
                    Date invDate = arJobOrderInvoiceLine.getArInvoice().getInvDate();
                    LocalInvItem invItem = arJobOrderInvoiceLine.getArJobOrderLine().getInvItemLocation().getInvItem();

                    LocalInvCosting invBeginningCosting = this.getCstIlBeginningBalanceByItemLocationAndDate(arJobOrderInvoiceLine.getArJobOrderLine().getInvItemLocation(), (Date) criteria.get("dateFrom"), brCode, AD_CMPNY);
                    if (invBeginningCosting != null) {

                        details.setRilBeginningQuantity(invBeginningCosting.getCstRemainingQuantity());
                        details.setRilBeginningUnitCost(invBeginningCosting.getCstRemainingValue() / invBeginningCosting.getCstRemainingQuantity());
                        details.setRilBeginningAmount(invBeginningCosting.getCstRemainingValue());
                        earliestDate = invBeginningCosting.getCstDate();
                    }

                    if (this.filterByOptionalCriteria(criteria, invItem, invDate, earliestDate)) {
                        continue;
                    }

                    details.setRilOutQuantity(arJobOrderInvoiceLine.getJilQuantityDelivered());
                    details.setRilOutAmount(arJobOrderInvoiceLine.getJilAmount());
                    details.setRilOutUnitCost(Math.abs(arJobOrderInvoiceLine.getJilAmount() / arJobOrderInvoiceLine.getJilQuantityDelivered()));

                    details.setRilDate(invDate);
                    details.setRilDocumentNumber(arJobOrderInvoiceLine.getArInvoice().getInvNumber());
                    details.setRilItemName(invItem.getIiName() + "-" + invItem.getIiDescription());
                    details.setRilReferenceNumber(arJobOrderInvoiceLine.getArInvoice().getInvReferenceNumber());
                    details.setRilSource("AR");
                    details.setRilUnit(arJobOrderInvoiceLine.getArJobOrderLine().getInvUnitOfMeasure().getUomName());
                    details.setRilReferenceNumber1(referenceNumber);
                    details.setRilItemCategory(invItem.getIiAdLvCategory());
                    list.add(details);
                }
            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            list.sort(InvRepItemLedgerDetails.ItemLedgerComparator);

            if (INCLD_UNPSTD || SHW_CMMTTD_QNTTY) {
                list.sort(InvRepItemLedgerDetails.ItemLedgerComparator);
            }

            if ((INCLD_UNPSTD || SHW_CMMTTD_QNTTY) && criteria.get("dateFrom") != null) {

                Date dateFrom = (Date) criteria.get("dateFrom");

                ArrayList newList = new ArrayList();

                InvRepItemLedgerDetails firstDetail = (InvRepItemLedgerDetails) list.get(0);

                String currentItem = firstDetail.getRilItemName();
                double beginningQty = firstDetail.getRilBeginningQuantity();
                double beginningAmount = firstDetail.getRilBeginningAmount();

                for (Object o : list) {

                    InvRepItemLedgerDetails details = (InvRepItemLedgerDetails) o;

                    if (!currentItem.equals(details.getRilItemName())) {
                        currentItem = details.getRilItemName();
                        beginningQty = details.getRilBeginningQuantity();
                        beginningAmount = details.getRilBeginningAmount();
                    }

                    if (details.getRilDate().getTime() < dateFrom.getTime()) {

                        beginningQty += (details.getRilInQuantity() - details.getRilOutQuantity());
                        beginningAmount += (details.getRilInAmount() - details.getRilOutAmount());

                    } else {

                        details.setRilBeginningQuantity(beginningQty);
                        details.setRilBeginningAmount(beginningAmount);

                        newList.add(details);
                    }
                }
                return newList;
            } else {
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

        Debug.print("InvRepItemLedgerControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvRepItemLedgerControllerBean getAdBrResAll");

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

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("InvRepItemLedgerControllerBean getAdCompany");

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

    // private method

    private LocalInvCosting getCstIlBeginningBalanceByItemLocationAndDate(LocalInvItemLocation invItemLocation, Date date, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("InvRepItemLedgerControllerBean getCstIlBeginningBalanceByItemLocationAndDate");

        LocalInvCosting invCosting = null;

        try {

            GregorianCalendar calendar = new GregorianCalendar();

            if (date != null) {

                calendar.setTime(date);
                // calendar.add(GregorianCalendar.DATE, -1);

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

        Debug.print("InvRepItemLedgerControllerBean filterByOptionalCriteria");

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

        Debug.print("InvRepItemLedgerControllerBean ejbCreate");
    }

}