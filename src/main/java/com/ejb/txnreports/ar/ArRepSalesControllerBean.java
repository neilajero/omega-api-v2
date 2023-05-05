/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArRepSalesControllerBean
 * @created February 02, 2005, 1:12 PM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txnreports.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.dao.ad.LocalAdLookUpValueHome;
import com.ejb.dao.ar.*;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdLookUpValue;
import com.ejb.entities.ar.*;
import com.ejb.entities.inv.LocalInvCosting;
import com.ejb.entities.inv.LocalInvLocation;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ar.ArRepSalesDetails;

import jakarta.ejb.*;

import java.util.*;

@Stateless(name = "ArRepSalesControllerEJB")
public class ArRepSalesControllerBean extends EJBContextClass implements ArRepSalesController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalArInvoiceLineItemHome arInvoiceLineItemHome;
    @EJB
    private LocalArInvoicePaymentScheduleHome arInvoicePaymentScheduleHome;
    @EJB
    private LocalArSalesOrderInvoiceLineHome arSalesOrderInvoiceLineHome;
    @EJB
    private LocalArSalesOrderLineHome arSalesOrderLineHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvPhysicalInventoryHome invPhysicalInventoryHome;
    @EJB
    private LocalInvPhysicalInventoryLineHome invPhysicalInventoryLineHome;


    public ArrayList executeArRepSales(HashMap criteria, ArrayList branchList, String GROUP_BY, String ORDER_BY, boolean INCLUDECM, boolean INCLUDEINVOICE, boolean INCLUDEMISC, boolean INCLUDECOLLECTION, boolean SHOW_ENTRIES, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepSalesControllerBean executeArRepSales");


        ArrayList list = new ArrayList();

        try {

            String customerCode = null;
            String customerBatch = null;
            String salesperson = null;
            String typeStatus = null;
            String region = null;

            if (criteria.containsKey("customerCode")) {

                customerCode = (String) criteria.get("customerCode");
            }

            if (criteria.containsKey("customerBatch")) {

                customerBatch = (String) criteria.get("customerBatch");
            }

            if (criteria.containsKey("salesperson")) {

                salesperson = (String) criteria.get("salesperson");
            }

            if (criteria.containsKey("typeStatus")) {

                typeStatus = (String) criteria.get("typeStatus");
            }

            if (criteria.containsKey("region")) {

                region = (String) criteria.get("region");
            }

            StringBuffer jbossQl = new StringBuffer();
            jbossQl.append("SELECT OBJECT(cst) FROM InvCosting cst ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = 0;

            Object[] obj = null;

            if (branchList.isEmpty()) {

                throw new GlobalNoRecordFoundException();

            } else {

                jbossQl.append(" WHERE cst.cstAdBranch in (");

                boolean firstLoop = true;

                Iterator j = branchList.iterator();

                while (j.hasNext()) {

                    if (firstLoop == false) {
                        jbossQl.append(", ");
                    } else {
                        firstLoop = false;
                    }

                    AdBranchDetails mdetails = (AdBranchDetails) j.next();

                    jbossQl.append(mdetails.getBrCode());
                }

                jbossQl.append(") ");

                firstArgument = false;
            }

            // Allocate the size of the object parameter

            if (criteria.containsKey("category")) {

                criteriaSize++;
            }

            if (criteria.containsKey("location")) {

                criteriaSize++;
            }

            if (criteria.containsKey("dateFrom")) {

                criteriaSize++;
            }

            if (criteria.containsKey("dateTo")) {

                criteriaSize++;
            }

            if (criteria.containsKey("numberFrom")) {

                criteriaSize++;
            }

            if (criteria.containsKey("numberTo")) {

                criteriaSize++;
            }

            if (criteria.containsKey("itemClass")) {

                criteriaSize++;
            }

            if (criteria.containsKey("itemName")) {

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

                jbossQl.append("cst.invItemLocation.invItem.iiName=?" + (ctr + 1) + " ");
                obj[ctr] = criteria.get("itemName");
                ctr++;
            }

            if (criteria.containsKey("category")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.invItemLocation.invItem.iiAdLvCategory=?" + (ctr + 1) + " ");
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

                jbossQl.append("cst.invItemLocation.invLocation.locName=?" + (ctr + 1) + " ");
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

                jbossQl.append("cst.cstDate>=?" + (ctr + 1) + " ");
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

                jbossQl.append("cst.cstDate<=?" + (ctr + 1) + " ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (criteria.containsKey("numberFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.arInvoiceLineItem.arInvoice.invNumber>=?" + (ctr + 1) + " ");
                obj[ctr] = criteria.get("numberFrom");
                Debug.print("Number From: " + criteria.get("numberFrom"));
                ctr++;
            }

            if (criteria.containsKey("numberTo")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.arInvoiceLineItem.arInvoice.invNumber<=?" + (ctr + 1) + " ");
                obj[ctr] = criteria.get("numberTo");
                Debug.print("Number To: " + criteria.get("numberTo"));
                ctr++;
            }

            if (criteria.containsKey("itemClass")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.invItemLocation.invItem.iiClass=?" + (ctr + 1) + " ");
                obj[ctr] = criteria.get("itemClass");
                ctr++;
            }

            if (criteria.containsKey("includeZeroes")) {

                if (criteria.get("includeZeroes").equals("NO")) {
                    jbossQl.append(" AND cst.cstQuantitySold <> 0 ");
                }
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append(" cst.cstAdCompany=" + AD_CMPNY + " ");

            jbossQl.append("ORDER BY cst.invItemLocation.invItem.iiName, cst.cstDate, cst.cstLineNumber");
            Debug.print("jbossQl.toString(): " + jbossQl.toString());
            Collection invCostings = invCostingHome.getCstByCriteria(jbossQl.toString(), obj, 0, 0);

            Debug.print("invCostingsSize-" + invCostings.size());
            Debug.print("INCLUDECM-----" + INCLUDECM);
            Debug.print("INCLUDEINVOICE-----" + INCLUDEINVOICE);
            Debug.print("INCLUDECOLLECTION-----" + INCLUDECOLLECTION);
            Debug.print("INCLUDEMISC-----" + INCLUDEMISC);
            Debug.print("SHOW_ENTRIES-----" + SHOW_ENTRIES);

            Iterator i = invCostings.iterator();

            while (i.hasNext()) {

                LocalInvCosting invCosting = (LocalInvCosting) i.next();

                if (invCosting.getArInvoiceLineItem() != null && invCosting.getArInvoiceLineItem().getArInvoice() != null && invCosting.getArInvoiceLineItem().getArInvoice().getInvVoid() == EJBCommon.FALSE || invCosting.getArInvoiceLineItem() != null && invCosting.getArInvoiceLineItem().getArReceipt() != null && invCosting.getArInvoiceLineItem().getArReceipt().getRctVoid() == EJBCommon.FALSE || invCosting.getArSalesOrderInvoiceLine() != null && invCosting.getArSalesOrderInvoiceLine().getArInvoice() != null && invCosting.getArSalesOrderInvoiceLine().getArInvoice().getInvVoid() == EJBCommon.FALSE) {
                    Debug.print("DATE--" + invCosting.getCstDate());
                    Debug.print("IICODE---" + invCosting.getCstCode());

                    if (INCLUDEINVOICE) {

                        Debug.print("INCLUDEINVOICE");

                        //	if(invCosting.getArInvoiceLineItem().getArInvoice() != null &&
                        // invCosting.getArInvoiceLineItem().getArInvoice().getInvCreditMemo() == 0){
                        Debug.print("1");
                        ArRepSalesDetails details = new ArRepSalesDetails();

                        details.setSlsItemName(invCosting.getInvItemLocation().getInvItem().getIiName());
                        details.setSlsItemDescription(invCosting.getInvItemLocation().getInvItem().getIiDescription());
                        details.setSlsDate(invCosting.getCstDate());
                        details.setSlsShowEntries(SHOW_ENTRIES);
                        Debug.print("1BRANCH=" + invCosting.getCstAdBranch());
                        details.setSlsAdBranch(invCosting.getCstAdBranch());
                        details.setSlsSource("INV");
                        details.setSlsItemAdLvCategory(invCosting.getInvItemLocation().getInvItem().getIiAdLvCategory());

                        Debug.print("2");

                        Debug.print("ar inv line item is: " + invCosting.getArInvoiceLineItem());
                        if (invCosting.getArInvoiceLineItem() != null) {
                            if (invCosting.getArInvoiceLineItem().getArInvoice() != null) {

                                if (invCosting.getArInvoiceLineItem().getArInvoice().getInvCreditMemo() == 0) {
                                    Debug.print("invCosting.getArInvoiceLineItem()--------------");

                                    Debug.print("invCosting.getArInvoiceLineItem().getIliQuantity()--" + invCosting.getArInvoiceLineItem().getIliQuantity());
                                    details.setSlsQuantitySold(invCosting.getArInvoiceLineItem().getIliQuantity());
                                    details.setSlsUnitPrice(EJBCommon.roundIt((invCosting.getArInvoiceLineItem().getIliAmount() + invCosting.getArInvoiceLineItem().getIliTaxAmount()) / details.getSlsQuantitySold(), this.getGlFcPrecisionUnit(AD_CMPNY)));
                                    Debug.print("unit: " + invCosting.getArInvoiceLineItem().getInvUnitOfMeasure().getUomName());
                                    details.setSlsUnit(invCosting.getArInvoiceLineItem().getInvUnitOfMeasure().getUomName());
                                    details.setSlsAmount(EJBCommon.roundIt(invCosting.getArInvoiceLineItem().getIliAmount() + invCosting.getArInvoiceLineItem().getIliTaxAmount(), this.getGlFcPrecisionUnit(AD_CMPNY)));
                                    details.setSlsOutputVat(invCosting.getArInvoiceLineItem().getIliTaxAmount());
                                    details.setSlsGrossUnitPrice(invCosting.getArInvoiceLineItem().getIliUnitPrice());
                                    details.setSlsDiscount(invCosting.getArInvoiceLineItem().getIliTotalDiscount());
                                    details.setSlsDefaultUnitPrice(invCosting.getArInvoiceLineItem().getInvItemLocation().getInvItem().getIiUnitCost());
                                    try {
                                        String slsRefNum = (invCosting.getArInvoiceLineItem().getArInvoice().getInvReferenceNumber());
                                        details.setSlsReferenceNumber(slsRefNum == null ? "" : slsRefNum);
                                    } catch (Exception ex) {
                                        details.setSlsReferenceNumber("");
                                    }
                                    // Debug.print("1 : "+details.getSlsReferenceNumber());
                                    if (invCosting.getArInvoiceLineItem().getArInvoice() != null) {
                                        Debug.print("invCosting.getArInvoiceLineItem().getArInvoice() != null");
                                        details.setSlsCustomerCode(invCosting.getArInvoiceLineItem().getArInvoice().getArCustomer().getCstCustomerCode());
                                        details.setSlsCustomerBatch(invCosting.getArInvoiceLineItem().getArInvoice().getArCustomer().getCstCustomerBatch());

                                        details.setSlsCustomerName(invCosting.getArInvoiceLineItem().getArInvoice().getArCustomer().getCstName());
                                        details.setSlsDocumentNumber(invCosting.getArInvoiceLineItem().getArInvoice().getInvNumber());
                                        details.setSlsCustomerAddress(invCosting.getArInvoiceLineItem().getArInvoice().getArCustomer().getCstAddress());
                                        details.setSlsCustomerStateProvince(invCosting.getArInvoiceLineItem().getArInvoice().getArCustomer().getCstStateProvince());
                                        Debug.print("REGION" + invCosting.getArInvoiceLineItem().getArInvoice().getArCustomer().getArCustomerClass().getCcName());
                                        details.setSlsCustomerClass(invCosting.getArInvoiceLineItem().getArInvoice().getArCustomer().getArCustomerClass().getCcName());

                                        Collection arInvoicePaymentSchedules = arInvoicePaymentScheduleHome.findByIpsLockAndInvCode((byte) (0), invCosting.getArInvoiceLineItem().getArInvoice().getInvCode(), invCosting.getArInvoiceLineItem().getArInvoice().getInvAdCompany());

                                        Iterator xyz = arInvoicePaymentSchedules.iterator();
                                        Date dueDate = null;
                                        while (xyz.hasNext()) {
                                            LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) xyz.next();
                                            dueDate = arInvoicePaymentSchedule.getIpsDueDate();
                                        }
                                        Debug.print("DUE DATE REAL-----" + dueDate);

                                        details.setSlsEffectivityDate(dueDate);

                                        try {
                                            if (invCosting.getArInvoiceLineItem().getArInvoice().getArSalesperson() != null) {
                                                details.setSlsSalespersonCode(invCosting.getArInvoiceLineItem().getArInvoice().getArSalesperson().getSlpSalespersonCode());
                                                details.setSlsSalespersonName(invCosting.getArInvoiceLineItem().getArInvoice().getArSalesperson().getSlpName());
                                            }
                                        } catch (Exception ex) {
                                            details.setSlsSalespersonCode("");
                                            details.setSlsSalespersonName("");
                                        }

                                        details.setSlsCustomerRegion(invCosting.getArInvoiceLineItem().getArInvoice().getArCustomer().getCstAdLvRegion());
                                    }
                                    if (invCosting.getArInvoiceLineItem().getArReceipt() != null) {
                                        Debug.print("invCosting.getArInvoiceLineItem().getArReceipt() != null");

                                        details.setSlsCustomerCode(invCosting.getArInvoiceLineItem().getArReceipt().getArCustomer().getCstCustomerCode());
                                        details.setSlsCustomerBatch(invCosting.getArInvoiceLineItem().getArReceipt().getArCustomer().getCstCustomerBatch());

                                        details.setSlsCustomerName(invCosting.getArInvoiceLineItem().getArReceipt().getArCustomer().getCstName());
                                        details.setSlsDocumentNumber(invCosting.getArInvoiceLineItem().getArReceipt().getRctNumber());
                                        details.setSlsCustomerAddress(invCosting.getArInvoiceLineItem().getArReceipt().getArCustomer().getCstAddress());

                                        try {
                                            if (invCosting.getArInvoiceLineItem().getArReceipt().getArSalesperson() != null) {
                                                details.setSlsSalespersonCode(invCosting.getArInvoiceLineItem().getArReceipt().getArSalesperson().getSlpSalespersonCode());
                                                details.setSlsSalespersonCode(invCosting.getArInvoiceLineItem().getArReceipt().getArSalesperson().getSlpName());
                                            }
                                        } catch (Exception ex) {
                                            details.setSlsSalespersonCode("");
                                            details.setSlsSalespersonName("");
                                        }

                                        try {
                                            String slsRefNum = (invCosting.getArInvoiceLineItem().getArReceipt().getRctReferenceNumber());
                                            details.setSlsReferenceNumber(slsRefNum == null ? "" : slsRefNum);
                                        } catch (Exception ex) {
                                            details.setSlsReferenceNumber("");
                                        }

                                        details.setSlsCustomerRegion(invCosting.getArInvoiceLineItem().getArReceipt().getArCustomer().getCstAdLvRegion());
                                        details.setSlsCustomerStateProvince(invCosting.getArInvoiceLineItem().getArReceipt().getArCustomer().getCstStateProvince());
                                    }

                                    if (invCosting.getArInvoiceLineItem().getArInvoice() != null && invCosting.getArInvoiceLineItem().getArInvoice().getInvCreditMemo() == 1) {

                                        details.setSlsQuantitySold(invCosting.getArInvoiceLineItem().getIliQuantity() * -1);
                                        details.setSlsAmount(EJBCommon.roundIt(invCosting.getArInvoiceLineItem().getIliAmount() + invCosting.getArInvoiceLineItem().getIliTaxAmount(), this.getGlFcPrecisionUnit(AD_CMPNY)) * -1);
                                        details.setSlsOutputVat(invCosting.getArInvoiceLineItem().getIliTaxAmount() * -1);
                                        details.setSlsDiscount(invCosting.getArInvoiceLineItem().getIliTotalDiscount() * -1);
                                    }

                                    Debug.print("3");
                                    details.setOrderBy(ORDER_BY);

                                    if (customerCode != null && !details.getSlsCustomerCode().contains(customerCode)) {
                                        continue;
                                    }
                                    if (customerBatch != null && !details.getSlsCustomerBatch().contains(customerBatch)) {
                                        continue;
                                    }
                                    if (salesperson != null && (details.getSlsSalespersonCode() == null || (details.getSlsSalespersonCode() != null && !details.getSlsSalespersonCode().contains(salesperson)))) {
                                        continue;
                                    }
                                    if (region != null && (details.getSlsCustomerRegion() == null || (details.getSlsCustomerRegion() != null && !details.getSlsCustomerRegion().equals(region)))) {
                                        continue;
                                    }
                                    Debug.print("pass");
                                    list.add(details);
                                }
                            }

                        } else if (invCosting.getArSalesOrderInvoiceLine() != null) {
                            Debug.print("invCosting.getArSalesOrderInvoiceLine()");
                            details.setSlsUnit(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getInvUnitOfMeasure().getUomName());
                            details.setSlsQuantitySold(invCosting.getArSalesOrderInvoiceLine().getSilQuantityDelivered());
                            details.setSlsUnitPrice(EJBCommon.roundIt((invCosting.getArSalesOrderInvoiceLine().getSilAmount() + invCosting.getArSalesOrderInvoiceLine().getSilTaxAmount()) / details.getSlsQuantitySold(), this.getGlFcPrecisionUnit(AD_CMPNY)));
                            details.setSlsUnit(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getInvUnitOfMeasure().getUomName());
                            Debug.print("unit: " + invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getInvUnitOfMeasure().getUomName());
                            details.setSlsAmount(EJBCommon.roundIt(invCosting.getArSalesOrderInvoiceLine().getSilAmount() + invCosting.getArSalesOrderInvoiceLine().getSilTaxAmount(), this.getGlFcPrecisionUnit(AD_CMPNY)));
                            details.setSlsOutputVat(invCosting.getArSalesOrderInvoiceLine().getSilTaxAmount());
                            details.setSlsGrossUnitPrice(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getSolUnitPrice());
                            details.setSlsDiscount(invCosting.getArSalesOrderInvoiceLine().getSilTotalDiscount());
                            details.setSlsDefaultUnitPrice(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getInvItemLocation().getInvItem().getIiUnitCost());
                            Debug.print(" ---- " + invCosting.getArSalesOrderInvoiceLine().getArInvoice());
                            if (invCosting.getArSalesOrderInvoiceLine().getArInvoice() != null) {

                                details.setSlsCustomerCode(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArCustomer().getCstCustomerCode());
                                details.setSlsCustomerBatch(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArCustomer().getCstCustomerBatch());

                                details.setSlsCustomerName(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArCustomer().getCstName());
                                details.setSlsDocumentNumber(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getInvNumber());
                                details.setSlsCustomerAddress(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArCustomer().getCstAddress());

                                try {
                                    if (invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArSalesperson() != null) {
                                        details.setSlsSalespersonCode(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArSalesperson().getSlpSalespersonCode());
                                        details.setSlsSalespersonCode(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArSalesperson().getSlpName());
                                        // details.setSlsSalespersonName(invCosting.getArInvoiceLineItem().getArInvoice().getArSalesperson().getSlpName());
                                    }
                                } catch (Exception ex) {
                                    details.setSlsSalespersonCode("");
                                    details.setSlsSalespersonName("");
                                }

                                details.setSlsCustomerRegion(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArCustomer().getCstAdLvRegion());
                                details.setSlsCustomerStateProvince(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArCustomer().getCstStateProvince());

                                details.setSlsSalesOrderNumber(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getArSalesOrder().getSoDocumentNumber());
                                details.setSlsSalesOrderDate(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getArSalesOrder().getSoDate());
                                details.setSlsSalesOrderQuantity(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getSolQuantity());
                                details.setSlsSalesOrderAmount(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getSolAmount());
                                details.setSlsSalesOrderSalesPrice(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getSolUnitPrice());

                                try {
                                    details.setSlsReferenceNumber(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getArSalesOrder().getSoReferenceNumber());
                                } catch (Exception ex) {
                                    details.setSlsReferenceNumber("");
                                }
                                // Debug.print("2 : "+details.getSlsReferenceNumber());

                                Debug.print("3");
                                details.setOrderBy(ORDER_BY);

                                if (customerCode != null && !details.getSlsCustomerCode().contains(customerCode)) {
                                    continue;
                                }
                                if (customerBatch != null && !details.getSlsCustomerBatch().contains(customerBatch)) {
                                    continue;
                                }
                                if (salesperson != null && (details.getSlsSalespersonCode() == null || (details.getSlsSalespersonCode() != null && !details.getSlsSalespersonCode().contains(salesperson)))) {
                                    continue;
                                }
                                if (region != null && (details.getSlsCustomerRegion() == null || (details.getSlsCustomerRegion() != null && !details.getSlsCustomerRegion().equals(region)))) {
                                    continue;
                                }
                                Debug.print("pass");
                                list.add(details);
                            }
                        }

                        //	}

                    }

                    if (INCLUDECM) {

                        Debug.print("INCLUDECM");

                        if (invCosting.getArInvoiceLineItem().getArInvoice() != null && invCosting.getArInvoiceLineItem().getArInvoice().getInvCreditMemo() == 1) {
                            Debug.print("1");
                            ArRepSalesDetails details = new ArRepSalesDetails();

                            details.setSlsItemName(invCosting.getInvItemLocation().getInvItem().getIiName());
                            details.setSlsItemDescription(invCosting.getInvItemLocation().getInvItem().getIiDescription());
                            details.setSlsDate(invCosting.getCstDate());
                            details.setSlsSource("CM");
                            details.setSlsShowEntries(SHOW_ENTRIES);
                            Debug.print("1BRANCH=" + invCosting.getCstAdBranch());
                            details.setSlsAdBranch(invCosting.getCstAdBranch());
                            details.setSlsItemAdLvCategory(invCosting.getInvItemLocation().getInvItem().getIiAdLvCategory());
                            Debug.print("2");
                            if (invCosting.getArInvoiceLineItem() != null) {
                                Debug.print("invCosting.getArInvoiceLineItem()--------------");

                                details.setSlsUnit(invCosting.getArInvoiceLineItem().getInvUnitOfMeasure().getUomName());
                                Debug.print("invCosting.getArInvoiceLineItem().getIliQuantity()--" + invCosting.getArInvoiceLineItem().getIliQuantity());
                                details.setSlsQuantitySold(invCosting.getArInvoiceLineItem().getIliQuantity());
                                details.setSlsUnitPrice(EJBCommon.roundIt((invCosting.getArInvoiceLineItem().getIliAmount() + invCosting.getArInvoiceLineItem().getIliTaxAmount()) / details.getSlsQuantitySold(), this.getGlFcPrecisionUnit(AD_CMPNY)));
                                details.setSlsUnit(invCosting.getArInvoiceLineItem().getInvUnitOfMeasure().getUomName());
                                details.setSlsAmount(EJBCommon.roundIt(invCosting.getArInvoiceLineItem().getIliAmount() + invCosting.getArInvoiceLineItem().getIliTaxAmount(), this.getGlFcPrecisionUnit(AD_CMPNY)));
                                details.setSlsOutputVat(invCosting.getArInvoiceLineItem().getIliTaxAmount());
                                details.setSlsGrossUnitPrice(invCosting.getArInvoiceLineItem().getIliUnitPrice());
                                details.setSlsDiscount(invCosting.getArInvoiceLineItem().getIliTotalDiscount());
                                details.setSlsDefaultUnitPrice(invCosting.getArInvoiceLineItem().getInvItemLocation().getInvItem().getIiUnitCost());
                                try {
                                    String slsRefNum = (invCosting.getArInvoiceLineItem().getArInvoice().getInvReferenceNumber());
                                    details.setSlsReferenceNumber(slsRefNum == null ? "" : slsRefNum);
                                } catch (Exception ex) {
                                    details.setSlsReferenceNumber("");
                                }
                                // Debug.print("1 : "+details.getSlsReferenceNumber());
                                if (invCosting.getArInvoiceLineItem().getArInvoice() != null) {
                                    Debug.print("invCosting.getArInvoiceLineItem().getArInvoice() != null");
                                    details.setSlsCustomerCode(invCosting.getArInvoiceLineItem().getArInvoice().getArCustomer().getCstCustomerCode());
                                    details.setSlsCustomerBatch(invCosting.getArInvoiceLineItem().getArInvoice().getArCustomer().getCstCustomerBatch());

                                    details.setSlsCustomerName(invCosting.getArInvoiceLineItem().getArInvoice().getArCustomer().getCstName());
                                    details.setSlsDocumentNumber(invCosting.getArInvoiceLineItem().getArInvoice().getInvNumber());
                                    details.setSlsCustomerAddress(invCosting.getArInvoiceLineItem().getArInvoice().getArCustomer().getCstAddress());
                                    details.setSlsCustomerStateProvince(invCosting.getArInvoiceLineItem().getArInvoice().getArCustomer().getCstStateProvince());
                                    Debug.print("description" + invCosting.getArInvoiceLineItem().getArInvoice().getInvDescription());
                                    details.setSlsDescription(invCosting.getArInvoiceLineItem().getArInvoice().getInvDescription());

                                    try {
                                        if (invCosting.getArInvoiceLineItem().getArInvoice().getArSalesperson() != null) {
                                            details.setSlsSalespersonCode(invCosting.getArInvoiceLineItem().getArInvoice().getArSalesperson().getSlpSalespersonCode());
                                            details.setSlsSalespersonName(invCosting.getArInvoiceLineItem().getArInvoice().getArSalesperson().getSlpName());
                                        } else {
                                            Debug.print("invoice number" + invCosting.getArInvoiceLineItem().getArInvoice().getInvCmInvoiceNumber());
                                            LocalArInvoice arInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(invCosting.getArInvoiceLineItem().getArInvoice().getInvCmInvoiceNumber(), (byte) (0), invCosting.getArInvoiceLineItem().getArInvoice().getInvAdBranch(), invCosting.getArInvoiceLineItem().getArInvoice().getInvAdCompany());
                                            Debug.print("getSlpSalespersonCode()" + arInvoice.getArSalesperson().getSlpSalespersonCode());
                                            details.setSlsCmInvoiceNumber(invCosting.getArInvoiceLineItem().getArInvoice().getInvCmInvoiceNumber());
                                            details.setSlsSalespersonCode(arInvoice.getArSalesperson().getSlpSalespersonCode());
                                            details.setSlsSalespersonName(arInvoice.getArSalesperson().getSlpName());
                                        }
                                    } catch (Exception ex) {
                                        details.setSlsSalespersonCode("");
                                        details.setSlsSalespersonName("");
                                    }

                                    details.setSlsCustomerRegion(invCosting.getArInvoiceLineItem().getArInvoice().getArCustomer().getCstAdLvRegion());
                                }
                                if (invCosting.getArInvoiceLineItem().getArReceipt() != null) {
                                    Debug.print("invCosting.getArInvoiceLineItem().getArReceipt() != null");

                                    details.setSlsCustomerCode(invCosting.getArInvoiceLineItem().getArReceipt().getArCustomer().getCstCustomerCode());
                                    details.setSlsCustomerBatch(invCosting.getArInvoiceLineItem().getArReceipt().getArCustomer().getCstCustomerBatch());

                                    details.setSlsCustomerName(invCosting.getArInvoiceLineItem().getArReceipt().getArCustomer().getCstName());
                                    details.setSlsDocumentNumber(invCosting.getArInvoiceLineItem().getArReceipt().getRctNumber());
                                    details.setSlsCustomerAddress(invCosting.getArInvoiceLineItem().getArReceipt().getArCustomer().getCstAddress());

                                    try {
                                        if (invCosting.getArInvoiceLineItem().getArReceipt().getArSalesperson() != null) {
                                            details.setSlsSalespersonCode(invCosting.getArInvoiceLineItem().getArReceipt().getArSalesperson().getSlpSalespersonCode());
                                            details.setSlsSalespersonCode(invCosting.getArInvoiceLineItem().getArReceipt().getArSalesperson().getSlpName());
                                        }
                                    } catch (Exception ex) {
                                        details.setSlsSalespersonCode("");
                                        details.setSlsSalespersonName("");
                                    }

                                    try {
                                        String slsRefNum = (invCosting.getArInvoiceLineItem().getArReceipt().getRctReferenceNumber());
                                        details.setSlsReferenceNumber(slsRefNum == null ? "" : slsRefNum);
                                    } catch (Exception ex) {
                                        details.setSlsReferenceNumber("");
                                    }

                                    details.setSlsCustomerRegion(invCosting.getArInvoiceLineItem().getArReceipt().getArCustomer().getCstAdLvRegion());
                                    details.setSlsCustomerStateProvince(invCosting.getArInvoiceLineItem().getArReceipt().getArCustomer().getCstStateProvince());
                                }

                                if (invCosting.getArInvoiceLineItem().getArInvoice() != null && invCosting.getArInvoiceLineItem().getArInvoice().getInvCreditMemo() == 1) {

                                    details.setSlsQuantitySold(invCosting.getArInvoiceLineItem().getIliQuantity() * -1);
                                    details.setSlsAmount(EJBCommon.roundIt(invCosting.getArInvoiceLineItem().getIliAmount() + invCosting.getArInvoiceLineItem().getIliTaxAmount(), this.getGlFcPrecisionUnit(AD_CMPNY)) * -1);
                                    details.setSlsOutputVat(invCosting.getArInvoiceLineItem().getIliTaxAmount() * -1);
                                    details.setSlsDiscount(invCosting.getArInvoiceLineItem().getIliTotalDiscount() * -1);
                                }

                            } else if (invCosting.getArSalesOrderInvoiceLine() != null) {
                                Debug.print("invCosting.getArSalesOrderInvoiceLine()");
                                details.setSlsUnit(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getInvUnitOfMeasure().getUomName());
                                details.setSlsQuantitySold(invCosting.getArSalesOrderInvoiceLine().getSilQuantityDelivered());
                                details.setSlsUnitPrice(EJBCommon.roundIt((invCosting.getArSalesOrderInvoiceLine().getSilAmount() + invCosting.getArSalesOrderInvoiceLine().getSilTaxAmount()) / details.getSlsQuantitySold(), this.getGlFcPrecisionUnit(AD_CMPNY)));
                                details.setSlsUnit(invCosting.getArInvoiceLineItem().getInvUnitOfMeasure().getUomName());
                                details.setSlsAmount(EJBCommon.roundIt(invCosting.getArSalesOrderInvoiceLine().getSilAmount() + invCosting.getArSalesOrderInvoiceLine().getSilTaxAmount(), this.getGlFcPrecisionUnit(AD_CMPNY)));
                                details.setSlsOutputVat(invCosting.getArSalesOrderInvoiceLine().getSilTaxAmount());
                                details.setSlsGrossUnitPrice(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getSolUnitPrice());
                                details.setSlsDiscount(invCosting.getArSalesOrderInvoiceLine().getSilTotalDiscount());
                                details.setSlsDefaultUnitPrice(invCosting.getArInvoiceLineItem().getInvItemLocation().getInvItem().getIiUnitCost());
                                if (invCosting.getArSalesOrderInvoiceLine().getArInvoice() != null) {

                                    details.setSlsCustomerCode(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArCustomer().getCstCustomerCode());
                                    details.setSlsCustomerBatch(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArCustomer().getCstCustomerBatch());

                                    details.setSlsCustomerName(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArCustomer().getCstName());
                                    details.setSlsDocumentNumber(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getInvNumber());
                                    details.setSlsCustomerAddress(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArCustomer().getCstAddress());

                                    try {
                                        if (invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArSalesperson() != null) {
                                            details.setSlsSalespersonCode(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArSalesperson().getSlpSalespersonCode());
                                            details.setSlsSalespersonCode(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArSalesperson().getSlpName());
                                            // details.setSlsSalespersonName(invCosting.getArInvoiceLineItem().getArInvoice().getArSalesperson().getSlpName());
                                        }
                                    } catch (Exception ex) {
                                        details.setSlsSalespersonCode("");
                                        details.setSlsSalespersonName("");
                                    }

                                    details.setSlsCustomerRegion(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArCustomer().getCstAdLvRegion());
                                    details.setSlsCustomerStateProvince(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArCustomer().getCstStateProvince());

                                    details.setSlsSalesOrderNumber(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getArSalesOrder().getSoDocumentNumber());
                                    details.setSlsSalesOrderDate(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getArSalesOrder().getSoDate());
                                    details.setSlsSalesOrderQuantity(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getSolQuantity());
                                    details.setSlsSalesOrderAmount(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getSolAmount());
                                    details.setSlsSalesOrderSalesPrice(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getSolUnitPrice());

                                    try {
                                        details.setSlsReferenceNumber(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getArSalesOrder().getSoReferenceNumber());
                                    } catch (Exception ex) {
                                        details.setSlsReferenceNumber("");
                                    }
                                    // Debug.print("2 : "+details.getSlsReferenceNumber());
                                }
                            }
                            Debug.print("3");
                            details.setOrderBy(ORDER_BY);

                            if (customerCode != null && !details.getSlsCustomerCode().contains(customerCode)) {
                                continue;
                            }
                            if (customerBatch != null && !details.getSlsCustomerBatch().contains(customerBatch)) {
                                continue;
                            }
                            Debug.print("salesperson-----" + salesperson);
                            Debug.print("getSlsSalespersonCode----" + details.getSlsSalespersonCode());

                            if (salesperson != null && (details.getSlsSalespersonCode() == null || (details.getSlsSalespersonCode() != null && !details.getSlsSalespersonCode().contains(salesperson)))) {
                                continue;
                            }
                            if (typeStatus != null && !details.getSlsDescription().contains(typeStatus)) {
                                continue;
                            }
                            if (region != null && (details.getSlsCustomerRegion() == null || (details.getSlsCustomerRegion() != null && !details.getSlsCustomerRegion().equals(region)))) {
                                continue;
                            }

                            list.add(details);
                        }
                    }

                    if (INCLUDEMISC) {
                        Debug.print("INCLUDEMISC");

                        Debug.print("ili :" + invCosting.getArInvoiceLineItem());
                        //	Debug.print("rct :" + invCosting.getArInvoiceLineItem().getArReceipt());
                        //	if(invCosting.getArInvoiceLineItem().getArReceipt() != null &&
                        // invCosting.getArInvoiceLineItem().getArReceipt().getRctType().equals("MISC")){

                        Debug.print("1");
                        ArRepSalesDetails details = new ArRepSalesDetails();

                        details.setSlsItemName(invCosting.getInvItemLocation().getInvItem().getIiName());
                        details.setSlsItemDescription(invCosting.getInvItemLocation().getInvItem().getIiDescription());
                        details.setSlsDate(invCosting.getCstDate());
                        details.setSlsSource("MISC");
                        details.setSlsShowEntries(SHOW_ENTRIES);
                        Debug.print("1BRANCH=" + invCosting.getCstAdBranch());
                        details.setSlsAdBranch(invCosting.getCstAdBranch());
                        details.setSlsItemAdLvCategory(invCosting.getInvItemLocation().getInvItem().getIiAdLvCategory());
                        Debug.print("2");
                        if (invCosting.getArInvoiceLineItem() != null) {
                            Debug.print("invCosting.getArInvoiceLineItem()--------------");

                            details.setSlsUnit(invCosting.getArInvoiceLineItem().getInvUnitOfMeasure().getUomName());
                            Debug.print("invCosting.getArInvoiceLineItem().getIliQuantity()--" + invCosting.getArInvoiceLineItem().getIliQuantity());
                            details.setSlsQuantitySold(invCosting.getArInvoiceLineItem().getIliQuantity());
                            details.setSlsUnitPrice(EJBCommon.roundIt((invCosting.getArInvoiceLineItem().getIliAmount() + invCosting.getArInvoiceLineItem().getIliTaxAmount()) / details.getSlsQuantitySold(), this.getGlFcPrecisionUnit(AD_CMPNY)));
                            details.setSlsUnit(invCosting.getArInvoiceLineItem().getInvUnitOfMeasure().getUomName());
                            details.setSlsAmount(EJBCommon.roundIt(invCosting.getArInvoiceLineItem().getIliAmount() + invCosting.getArInvoiceLineItem().getIliTaxAmount(), this.getGlFcPrecisionUnit(AD_CMPNY)));
                            details.setSlsOutputVat(invCosting.getArInvoiceLineItem().getIliTaxAmount());
                            details.setSlsGrossUnitPrice(invCosting.getArInvoiceLineItem().getIliUnitPrice());
                            details.setSlsDiscount(invCosting.getArInvoiceLineItem().getIliTotalDiscount());
                            details.setSlsDefaultUnitPrice(invCosting.getArInvoiceLineItem().getInvItemLocation().getInvItem().getIiUnitCost());
                            try {
                                String slsRefNum = (invCosting.getArInvoiceLineItem().getArInvoice().getInvReferenceNumber());
                                details.setSlsReferenceNumber(slsRefNum == null ? "" : slsRefNum);
                            } catch (Exception ex) {
                                details.setSlsReferenceNumber("");
                            }
                            // Debug.print("1 : "+details.getSlsReferenceNumber());
                            if (invCosting.getArInvoiceLineItem().getArInvoice() != null) {
                                Debug.print("invCosting.getArInvoiceLineItem().getArInvoice() != null");
                                details.setSlsCustomerCode(invCosting.getArInvoiceLineItem().getArInvoice().getArCustomer().getCstCustomerCode());
                                details.setSlsCustomerBatch(invCosting.getArInvoiceLineItem().getArInvoice().getArCustomer().getCstCustomerBatch());

                                details.setSlsCustomerName(invCosting.getArInvoiceLineItem().getArInvoice().getArCustomer().getCstName());
                                details.setSlsDocumentNumber(invCosting.getArInvoiceLineItem().getArInvoice().getInvNumber());
                                Debug.print("BRANCH=" + invCosting.getArInvoiceLineItem().getArInvoice().getInvAdBranch());
                                details.setSlsAdBranch(invCosting.getArInvoiceLineItem().getArInvoice().getInvAdBranch());
                                details.setSlsCustomerAddress(invCosting.getArInvoiceLineItem().getArInvoice().getArCustomer().getCstAddress());
                                details.setSlsCustomerStateProvince(invCosting.getArInvoiceLineItem().getArInvoice().getArCustomer().getCstStateProvince());

                                try {

                                    Debug.print("MISC SalesPersonCode=" + invCosting.getArInvoiceLineItem().getArInvoice().getArSalesperson().getSlpSalespersonCode());
                                    details.setSlsSalespersonCode(invCosting.getArInvoiceLineItem().getArInvoice().getArSalesperson().getSlpSalespersonCode());
                                    details.setSlsSalespersonName(invCosting.getArInvoiceLineItem().getArInvoice().getArSalesperson().getSlpName());

                                } catch (Exception ex) {
                                    details.setSlsSalespersonCode("DEF");
                                    details.setSlsSalespersonName("DEF");
                                }

                                details.setSlsCustomerRegion(invCosting.getArInvoiceLineItem().getArInvoice().getArCustomer().getCstAdLvRegion());
                            }
                            if (invCosting.getArInvoiceLineItem().getArReceipt() != null) {
                                Debug.print("invCosting.getArInvoiceLineItem().getArReceipt() != null");

                                if (invCosting.getArInvoiceLineItem().getArReceipt().getRctType().equals("MISC")) {
                                    details.setSlsCustomerCode(invCosting.getArInvoiceLineItem().getArReceipt().getArCustomer().getCstCustomerCode());
                                    details.setSlsCustomerBatch(invCosting.getArInvoiceLineItem().getArReceipt().getArCustomer().getCstCustomerBatch());
                                    details.setSlsCustomerName(invCosting.getArInvoiceLineItem().getArReceipt().getArCustomer().getCstName());
                                    details.setSlsDocumentNumber(invCosting.getArInvoiceLineItem().getArReceipt().getRctNumber());

                                    details.setSlsCustomerAddress(invCosting.getArInvoiceLineItem().getArReceipt().getArCustomer().getCstAddress());

                                    try {
                                        Debug.print("MISC SALES PERSON");
                                        if (invCosting.getArInvoiceLineItem().getArReceipt().getArSalesperson() != null) {
                                            Debug.print("1 MISC SALES PERSON" + invCosting.getArInvoiceLineItem().getArReceipt().getArSalesperson().getSlpSalespersonCode());
                                            details.setSlsSalespersonCode(invCosting.getArInvoiceLineItem().getArReceipt().getArSalesperson().getSlpSalespersonCode());
                                            details.setSlsSalespersonCode(invCosting.getArInvoiceLineItem().getArReceipt().getArSalesperson().getSlpName());
                                        }
                                    } catch (Exception ex) {
                                        details.setSlsSalespersonCode("");
                                        details.setSlsSalespersonName("");
                                    }

                                    try {
                                        String slsRefNum = (invCosting.getArInvoiceLineItem().getArReceipt().getRctReferenceNumber());
                                        details.setSlsReferenceNumber(slsRefNum == null ? "" : slsRefNum);
                                    } catch (Exception ex) {
                                        details.setSlsReferenceNumber("");
                                    }

                                    details.setSlsCustomerRegion(invCosting.getArInvoiceLineItem().getArReceipt().getArCustomer().getCstAdLvRegion());
                                    details.setSlsCustomerStateProvince(invCosting.getArInvoiceLineItem().getArReceipt().getArCustomer().getCstStateProvince());
                                }
                            }

                            if (invCosting.getArInvoiceLineItem().getArInvoice() != null && invCosting.getArInvoiceLineItem().getArInvoice().getInvCreditMemo() == 1) {

                                details.setSlsQuantitySold(invCosting.getArInvoiceLineItem().getIliQuantity() * -1);
                                details.setSlsAmount(EJBCommon.roundIt(invCosting.getArInvoiceLineItem().getIliAmount() + invCosting.getArInvoiceLineItem().getIliTaxAmount(), this.getGlFcPrecisionUnit(AD_CMPNY)) * -1);
                                details.setSlsOutputVat(invCosting.getArInvoiceLineItem().getIliTaxAmount() * -1);
                                details.setSlsDiscount(invCosting.getArInvoiceLineItem().getIliTotalDiscount() * -1);
                            }

                        } else if (invCosting.getArSalesOrderInvoiceLine() != null) {
                            Debug.print("invCosting.getArSalesOrderInvoiceLine()");
                            details.setSlsUnit(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getInvUnitOfMeasure().getUomName());
                            details.setSlsQuantitySold(invCosting.getArSalesOrderInvoiceLine().getSilQuantityDelivered());
                            details.setSlsUnitPrice(EJBCommon.roundIt((invCosting.getArSalesOrderInvoiceLine().getSilAmount() + invCosting.getArSalesOrderInvoiceLine().getSilTaxAmount()) / details.getSlsQuantitySold(), this.getGlFcPrecisionUnit(AD_CMPNY)));
                            details.setSlsAmount(EJBCommon.roundIt(invCosting.getArSalesOrderInvoiceLine().getSilAmount() + invCosting.getArSalesOrderInvoiceLine().getSilTaxAmount(), this.getGlFcPrecisionUnit(AD_CMPNY)));
                            details.setSlsOutputVat(invCosting.getArSalesOrderInvoiceLine().getSilTaxAmount());
                            details.setSlsGrossUnitPrice(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getSolUnitPrice());
                            details.setSlsDiscount(invCosting.getArSalesOrderInvoiceLine().getSilTotalDiscount());
                            details.setSlsDefaultUnitPrice(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getInvItemLocation().getInvItem().getIiUnitCost());
                            if (invCosting.getArSalesOrderInvoiceLine().getArInvoice() != null) {

                                details.setSlsCustomerCode(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArCustomer().getCstCustomerCode());
                                details.setSlsCustomerBatch(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArCustomer().getCstCustomerBatch());

                                details.setSlsCustomerName(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArCustomer().getCstName());
                                details.setSlsDocumentNumber(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getInvNumber());
                                details.setSlsCustomerAddress(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArCustomer().getCstAddress());

                                try {
                                    if (invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArSalesperson() != null) {
                                        details.setSlsSalespersonCode(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArSalesperson().getSlpSalespersonCode());
                                        details.setSlsSalespersonCode(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArSalesperson().getSlpName());
                                        // details.setSlsSalespersonName(invCosting.getArInvoiceLineItem().getArInvoice().getArSalesperson().getSlpName());
                                    }
                                } catch (Exception ex) {
                                    details.setSlsSalespersonCode("");
                                    details.setSlsSalespersonName("");
                                }

                                details.setSlsCustomerRegion(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArCustomer().getCstAdLvRegion());
                                details.setSlsCustomerStateProvince(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArCustomer().getCstStateProvince());

                                details.setSlsSalesOrderNumber(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getArSalesOrder().getSoDocumentNumber());
                                details.setSlsSalesOrderDate(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getArSalesOrder().getSoDate());
                                details.setSlsSalesOrderQuantity(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getSolQuantity());
                                details.setSlsSalesOrderAmount(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getSolAmount());
                                details.setSlsSalesOrderSalesPrice(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getSolUnitPrice());

                                try {
                                    details.setSlsReferenceNumber(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getArSalesOrder().getSoReferenceNumber());
                                } catch (Exception ex) {
                                    details.setSlsReferenceNumber("");
                                }
                                // Debug.print("2 : "+details.getSlsReferenceNumber());
                            }
                        }
                        Debug.print("3");
                        details.setOrderBy(ORDER_BY);

                        if (customerCode != null && !details.getSlsCustomerCode().contains(customerCode)) {
                            continue;
                        }
                        if (customerBatch != null && !details.getSlsCustomerBatch().contains(customerBatch)) {
                            continue;
                        }
                        if (salesperson != null && (details.getSlsSalespersonCode() == null || (details.getSlsSalespersonCode() != null && !details.getSlsSalespersonCode().contains(salesperson)))) {
                            continue;
                        }
                        if (region != null && (details.getSlsCustomerRegion() == null || (details.getSlsCustomerRegion() != null && !details.getSlsCustomerRegion().equals(region)))) {
                            continue;
                        }

                        list.add(details);
                    }

                    if (INCLUDECOLLECTION) {

                        Debug.print("INCLUDECOLLECTION");
                        Debug.print("cosint in ili: " + invCosting.getArInvoiceLineItem());
                        Debug.print("code costing: " + invCosting.getCstCode());
                        //	Debug.print("cosint in rct: " +
                        // invCosting.getArInvoiceLineItem().getArReceipt());

                        //	if(invCosting.getArInvoiceLineItem().getArReceipt() != null &&
                        // invCosting.getArInvoiceLineItem().getArReceipt().getRctType().equals("COLLECTION")){
                        // if(invCosting.getArInvoiceLineItem().getArReceipt() != null &&
                        // invCosting.getArInvoiceLineItem().getArReceipt().getRctType().equals("COLLECTION")){

                        Debug.print("1");
                        ArRepSalesDetails details = new ArRepSalesDetails();

                        details.setSlsItemName(invCosting.getInvItemLocation().getInvItem().getIiName());
                        details.setSlsItemDescription(invCosting.getInvItemLocation().getInvItem().getIiDescription());
                        details.setSlsDate(invCosting.getCstDate());
                        details.setSlsSource("CTN");
                        details.setSlsShowEntries(SHOW_ENTRIES);
                        Debug.print("1BRANCH=" + invCosting.getCstAdBranch());
                        details.setSlsAdBranch(invCosting.getCstAdBranch());
                        details.setSlsItemAdLvCategory(invCosting.getInvItemLocation().getInvItem().getIiAdLvCategory());
                        Debug.print("2");

                        if (invCosting.getArInvoiceLineItem() != null) {
                            Debug.print("invCosting.getArInvoiceLineItem()--------------");

                            details.setSlsUnit(invCosting.getArInvoiceLineItem().getInvUnitOfMeasure().getUomName());
                            Debug.print("invCosting.getArInvoiceLineItem().getIliQuantity()--" + invCosting.getArInvoiceLineItem().getIliQuantity());
                            details.setSlsQuantitySold(invCosting.getArInvoiceLineItem().getIliQuantity());
                            details.setSlsUnitPrice(EJBCommon.roundIt((invCosting.getArInvoiceLineItem().getIliAmount() + invCosting.getArInvoiceLineItem().getIliTaxAmount()) / details.getSlsQuantitySold(), this.getGlFcPrecisionUnit(AD_CMPNY)));
                            details.setSlsUnit(invCosting.getArInvoiceLineItem().getInvUnitOfMeasure().getUomName());
                            details.setSlsAmount(EJBCommon.roundIt(invCosting.getArInvoiceLineItem().getIliAmount() + invCosting.getArInvoiceLineItem().getIliTaxAmount(), this.getGlFcPrecisionUnit(AD_CMPNY)));
                            details.setSlsOutputVat(invCosting.getArInvoiceLineItem().getIliTaxAmount());
                            details.setSlsGrossUnitPrice(invCosting.getArInvoiceLineItem().getIliUnitPrice());
                            details.setSlsDiscount(invCosting.getArInvoiceLineItem().getIliTotalDiscount());
                            details.setSlsDefaultUnitPrice(invCosting.getArInvoiceLineItem().getInvItemLocation().getInvItem().getIiUnitCost());
                            try {
                                String slsRefNum = (invCosting.getArInvoiceLineItem().getArInvoice().getInvReferenceNumber());
                                details.setSlsReferenceNumber(slsRefNum == null ? "" : slsRefNum);
                            } catch (Exception ex) {
                                details.setSlsReferenceNumber("");
                            }
                            // Debug.print("1 : "+details.getSlsReferenceNumber());
                            if (invCosting.getArInvoiceLineItem().getArInvoice() != null) {
                                Debug.print("invCosting.getArInvoiceLineItem().getArInvoice() != null");
                                details.setSlsCustomerCode(invCosting.getArInvoiceLineItem().getArInvoice().getArCustomer().getCstCustomerCode());
                                details.setSlsCustomerBatch(invCosting.getArInvoiceLineItem().getArInvoice().getArCustomer().getCstCustomerBatch());

                                details.setSlsCustomerName(invCosting.getArInvoiceLineItem().getArInvoice().getArCustomer().getCstName());
                                details.setSlsDocumentNumber(invCosting.getArInvoiceLineItem().getArInvoice().getInvNumber());
                                details.setSlsUnit(invCosting.getArInvoiceLineItem().getInvUnitOfMeasure().getUomName());
                                details.setSlsCustomerAddress(invCosting.getArInvoiceLineItem().getArInvoice().getArCustomer().getCstAddress());
                                details.setSlsCustomerStateProvince(invCosting.getArInvoiceLineItem().getArInvoice().getArCustomer().getCstStateProvince());

                                try {
                                    if (invCosting.getArInvoiceLineItem().getArInvoice().getArSalesperson() != null) {
                                        details.setSlsSalespersonCode(invCosting.getArInvoiceLineItem().getArInvoice().getArSalesperson().getSlpSalespersonCode());
                                        details.setSlsSalespersonName(invCosting.getArInvoiceLineItem().getArInvoice().getArSalesperson().getSlpName());
                                    }
                                } catch (Exception ex) {
                                    details.setSlsSalespersonCode("");
                                    details.setSlsSalespersonName("");
                                }

                                details.setSlsCustomerRegion(invCosting.getArInvoiceLineItem().getArInvoice().getArCustomer().getCstAdLvRegion());
                            }
                            if (invCosting.getArInvoiceLineItem().getArReceipt() != null) {
                                Debug.print("invCosting.getArInvoiceLineItem().getArReceipt() != null");

                                if (invCosting.getArInvoiceLineItem().getArReceipt().getRctType().equals("COLLECTION")) {
                                    details.setSlsCustomerCode(invCosting.getArInvoiceLineItem().getArReceipt().getArCustomer().getCstCustomerCode());
                                    details.setSlsCustomerBatch(invCosting.getArInvoiceLineItem().getArReceipt().getArCustomer().getCstCustomerBatch());

                                    details.setSlsCustomerName(invCosting.getArInvoiceLineItem().getArReceipt().getArCustomer().getCstName());
                                    details.setSlsDocumentNumber(invCosting.getArInvoiceLineItem().getArReceipt().getRctNumber());
                                    details.setSlsCustomerAddress(invCosting.getArInvoiceLineItem().getArReceipt().getArCustomer().getCstAddress());
                                    details.setSlsUnit(invCosting.getArInvoiceLineItem().getInvUnitOfMeasure().getUomName());
                                    try {
                                        if (invCosting.getArInvoiceLineItem().getArReceipt().getArSalesperson() != null) {
                                            details.setSlsSalespersonCode(invCosting.getArInvoiceLineItem().getArReceipt().getArSalesperson().getSlpSalespersonCode());
                                            details.setSlsSalespersonCode(invCosting.getArInvoiceLineItem().getArReceipt().getArSalesperson().getSlpName());
                                        }
                                    } catch (Exception ex) {
                                        details.setSlsSalespersonCode("");
                                        details.setSlsSalespersonName("");
                                    }

                                    try {
                                        String slsRefNum = (invCosting.getArInvoiceLineItem().getArReceipt().getRctReferenceNumber());
                                        details.setSlsReferenceNumber(slsRefNum == null ? "" : slsRefNum);
                                    } catch (Exception ex) {
                                        details.setSlsReferenceNumber("");
                                    }

                                    details.setSlsCustomerRegion(invCosting.getArInvoiceLineItem().getArReceipt().getArCustomer().getCstAdLvRegion());
                                    details.setSlsCustomerStateProvince(invCosting.getArInvoiceLineItem().getArReceipt().getArCustomer().getCstStateProvince());
                                }
                            }

                            if (invCosting.getArInvoiceLineItem().getArInvoice() != null && invCosting.getArInvoiceLineItem().getArInvoice().getInvCreditMemo() == 1) {

                                details.setSlsQuantitySold(invCosting.getArInvoiceLineItem().getIliQuantity() * -1);
                                details.setSlsAmount(EJBCommon.roundIt(invCosting.getArInvoiceLineItem().getIliAmount() + invCosting.getArInvoiceLineItem().getIliTaxAmount(), this.getGlFcPrecisionUnit(AD_CMPNY)) * -1);
                                details.setSlsOutputVat(invCosting.getArInvoiceLineItem().getIliTaxAmount() * -1);
                                details.setSlsDiscount(invCosting.getArInvoiceLineItem().getIliTotalDiscount() * -1);
                            }

                        } else if (invCosting.getArSalesOrderInvoiceLine() != null) {
                            Debug.print("invCosting.getArSalesOrderInvoiceLine()");
                            details.setSlsUnit(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getInvUnitOfMeasure().getUomName());
                            details.setSlsQuantitySold(invCosting.getArSalesOrderInvoiceLine().getSilQuantityDelivered());
                            details.setSlsUnitPrice(EJBCommon.roundIt((invCosting.getArSalesOrderInvoiceLine().getSilAmount() + invCosting.getArSalesOrderInvoiceLine().getSilTaxAmount()) / details.getSlsQuantitySold(), this.getGlFcPrecisionUnit(AD_CMPNY)));
                            details.setSlsAmount(EJBCommon.roundIt(invCosting.getArSalesOrderInvoiceLine().getSilAmount() + invCosting.getArSalesOrderInvoiceLine().getSilTaxAmount(), this.getGlFcPrecisionUnit(AD_CMPNY)));
                            details.setSlsOutputVat(invCosting.getArSalesOrderInvoiceLine().getSilTaxAmount());
                            details.setSlsGrossUnitPrice(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getSolUnitPrice());
                            details.setSlsUnit(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getInvUnitOfMeasure().getUomName());
                            details.setSlsDiscount(invCosting.getArSalesOrderInvoiceLine().getSilTotalDiscount());
                            details.setSlsDefaultUnitPrice(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getInvItemLocation().getInvItem().getIiUnitCost());
                            if (invCosting.getArSalesOrderInvoiceLine().getArInvoice() != null) {

                                details.setSlsCustomerCode(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArCustomer().getCstCustomerCode());
                                details.setSlsCustomerBatch(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArCustomer().getCstCustomerBatch());

                                details.setSlsCustomerName(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArCustomer().getCstName());
                                details.setSlsDocumentNumber(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getInvNumber());
                                details.setSlsCustomerAddress(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArCustomer().getCstAddress());

                                try {
                                    if (invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArSalesperson() != null) {
                                        details.setSlsSalespersonCode(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArSalesperson().getSlpSalespersonCode());
                                        details.setSlsSalespersonCode(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArSalesperson().getSlpName());
                                        // details.setSlsSalespersonName(invCosting.getArInvoiceLineItem().getArInvoice().getArSalesperson().getSlpName());
                                    }
                                } catch (Exception ex) {
                                    details.setSlsSalespersonCode("");
                                    details.setSlsSalespersonName("");
                                }

                                details.setSlsCustomerRegion(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArCustomer().getCstAdLvRegion());
                                details.setSlsCustomerStateProvince(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArCustomer().getCstStateProvince());

                                details.setSlsSalesOrderNumber(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getArSalesOrder().getSoDocumentNumber());
                                details.setSlsSalesOrderDate(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getArSalesOrder().getSoDate());
                                details.setSlsSalesOrderQuantity(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getSolQuantity());
                                details.setSlsSalesOrderAmount(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getSolAmount());
                                details.setSlsSalesOrderSalesPrice(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getSolUnitPrice());

                                try {
                                    details.setSlsReferenceNumber(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getArSalesOrder().getSoReferenceNumber());
                                } catch (Exception ex) {
                                    details.setSlsReferenceNumber("");
                                }
                                // Debug.print("2 : "+details.getSlsReferenceNumber());
                            }
                        }
                        Debug.print("3");
                        details.setOrderBy(ORDER_BY);

                        if (customerCode != null && !details.getSlsCustomerCode().contains(customerCode)) {
                            continue;
                        }
                        if (customerBatch != null && !details.getSlsCustomerBatch().contains(customerBatch)) {
                            continue;
                        }
                        if (salesperson != null && (details.getSlsSalespersonCode() == null || (details.getSlsSalespersonCode() != null && !details.getSlsSalespersonCode().contains(salesperson)))) {
                            continue;
                        }
                        if (region != null && (details.getSlsCustomerRegion() == null || (details.getSlsCustomerRegion() != null && !details.getSlsCustomerRegion().equals(region)))) {
                            continue;
                        }

                        list.add(details);
                    }
                }
            }

            if (criteria.containsKey("region")) {

                criteriaSize++;
            }

            // if include unposted ---------------------

            if (criteria.get("includeUnposted").equals("YES")) {

                obj = new Object[criteriaSize];
                String orderBy = null;
                String orderStatus = null;

                // get unposted invoices

                jbossQl = new StringBuffer();
                firstArgument = true;
                ctr = 0;

                jbossQl.append("SELECT OBJECT(ili) FROM ArInvoiceLineItem ili ");

                if (branchList.isEmpty()) {

                    throw new GlobalNoRecordFoundException();

                } else {

                    jbossQl.append(" WHERE ili.arInvoice.invAdBranch in (");

                    boolean firstLoop = true;

                    Iterator j = branchList.iterator();

                    while (j.hasNext()) {

                        if (firstLoop == false) {
                            jbossQl.append(", ");
                        } else {
                            firstLoop = false;
                        }

                        AdBranchDetails mdetails = (AdBranchDetails) j.next();

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

                    jbossQl.append("ili.invItemLocation.invItem.iiName=?" + (ctr + 1) + " ");
                    obj[ctr] = criteria.get("itemName");
                    ctr++;
                }

                if (criteria.containsKey("category")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("ili.invItemLocation.invItem.iiAdLvCategory=?" + (ctr + 1) + " ");
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

                    jbossQl.append("ili.invItemLocation.invLocation.locName=?" + (ctr + 1) + " ");
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

                    jbossQl.append("ili.arInvoice.invDate>=?" + (ctr + 1) + " ");
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

                    jbossQl.append("ili.arInvoice.invDate<=?" + (ctr + 1) + " ");
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

                    jbossQl.append("ili.invItemLocation.invItem.iiClass=?" + (ctr + 1) + " ");
                    obj[ctr] = criteria.get("itemClass");
                    ctr++;
                }

                if (criteria.containsKey("customerCode")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("ili.arInvoice.arCustomer.cstCustomerCode LIKE '%" + criteria.get("customerCode") + "%' ");
                }

                if (criteria.containsKey("customerBatch")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("ili.arInvoice.arCustomer.cstCustomerBatch LIKE '%" + criteria.get("customerBatch") + "%' ");
                }

                if (criteria.containsKey("salesperson")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("ili.arInvoice.arSalesperson.slpSalespersonCode LIKE '%" + criteria.get("salesperson") + "%' ");
                }

                if (criteria.containsKey("typeStatus")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("ili.arInvoice.invDescription LIKE '%" + criteria.get("typeStatus") + "%' ");
                }

                if (criteria.containsKey("region")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("ili.arInvoice.arCustomer.cstAdLvRegion=?" + (ctr + 1) + " ");
                    obj[ctr] = criteria.get("region");
                    ctr++;
                }

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("ili.arInvoice.invPosted = 0 AND ili.arInvoice.invVoid = 0 AND ili.iliAdCompany=" + AD_CMPNY + " ");

                orderBy = null;

                if (ORDER_BY.equals("DATE")) {

                    orderBy = "ili.arInvoice.invDate";

                } else if (ORDER_BY.equals("ITEM NAME")) {

                    orderBy = "ili.invItemLocation.invItem.iiName";
                }

                if (orderBy != null) {

                    jbossQl.append("ORDER BY " + orderBy);
                }

                Collection arInvoices = arInvoiceLineItemHome.getIliByCriteria(jbossQl.toString(), obj);

                i = arInvoices.iterator();

                while (i.hasNext()) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) i.next();

                    ArRepSalesDetails details = new ArRepSalesDetails();

                    details.setSlsItemName(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                    details.setSlsItemDescription(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiDescription());
                    details.setSlsItemAdLvCategory(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiAdLvCategory());
                    details.setSlsUnit(arInvoiceLineItem.getInvUnitOfMeasure().getUomName());
                    details.setSlsUnitPrice(EJBCommon.roundIt((arInvoiceLineItem.getIliAmount() + arInvoiceLineItem.getIliTaxAmount()) / details.getSlsQuantitySold(), this.getGlFcPrecisionUnit(AD_CMPNY)));
                    details.setSlsGrossUnitPrice(arInvoiceLineItem.getIliUnitPrice());
                    details.setSlsDefaultUnitPrice(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost());
                    Debug.print("------------" + arInvoiceLineItem.getArInvoice().getInvCreditMemo());
                    details.setSlsQuantitySold(arInvoiceLineItem.getArInvoice().getInvCreditMemo() == 0 ? arInvoiceLineItem.getIliQuantity() : arInvoiceLineItem.getIliQuantity() * 0);
                    details.setSlsAmount(arInvoiceLineItem.getArInvoice().getInvCreditMemo() == 0 ? EJBCommon.roundIt(arInvoiceLineItem.getIliAmount() + arInvoiceLineItem.getIliTaxAmount(), this.getGlFcPrecisionUnit(AD_CMPNY)) : EJBCommon.roundIt(arInvoiceLineItem.getIliAmount() + arInvoiceLineItem.getIliTaxAmount(), this.getGlFcPrecisionUnit(AD_CMPNY)) * -1);
                    details.setSlsOutputVat(arInvoiceLineItem.getArInvoice().getInvCreditMemo() == 0 ? arInvoiceLineItem.getIliTaxAmount() : arInvoiceLineItem.getIliTaxAmount() * -1);
                    details.setSlsDiscount(arInvoiceLineItem.getArInvoice().getInvCreditMemo() == 0 ? arInvoiceLineItem.getIliTotalDiscount() : arInvoiceLineItem.getIliTotalDiscount() * -1);

                    details.setSlsDate(arInvoiceLineItem.getArInvoice().getInvDate());
                    details.setSlsCustomerCode(arInvoiceLineItem.getArInvoice().getArCustomer().getCstCustomerCode());
                    details.setSlsCustomerBatch(arInvoiceLineItem.getArInvoice().getArCustomer().getCstCustomerBatch());
                    details.setSlsCustomerName(arInvoiceLineItem.getArInvoice().getArCustomer().getCstName());
                    details.setSlsDocumentNumber(arInvoiceLineItem.getArInvoice().getInvNumber());
                    details.setSlsCustomerAddress(arInvoiceLineItem.getArInvoice().getArCustomer().getCstAddress());

                    if (arInvoiceLineItem.getArInvoice().getArSalesperson() != null) {
                        details.setSlsSalespersonCode(arInvoiceLineItem.getArInvoice().getArSalesperson().getSlpSalespersonCode());
                        details.setSlsSalespersonName(arInvoiceLineItem.getArInvoice().getArSalesperson().getSlpName());
                    }
                    details.setSlsCustomerRegion(arInvoiceLineItem.getArInvoice().getArCustomer().getCstAdLvRegion());
                    details.setSlsCustomerStateProvince(arInvoiceLineItem.getArInvoice().getArCustomer().getCstStateProvince());
                    try {
                        details.setSlsReferenceNumber(arInvoiceLineItem.getArInvoice().getInvReferenceNumber());
                    } catch (Exception ex) {
                        details.setSlsReferenceNumber("");
                    }
                    // Debug.print("3 : "+details.getSlsReferenceNumber());
                    details.setOrderBy(ORDER_BY);

                    list.add(details);
                }

                // get unposted misc receipts

                jbossQl = new StringBuffer();
                firstArgument = true;
                ctr = 0;

                jbossQl.append("SELECT OBJECT(ili) FROM ArInvoiceLineItem ili ");

                if (branchList.isEmpty()) {

                    throw new GlobalNoRecordFoundException();

                } else {

                    jbossQl.append(" WHERE ili.arReceipt.rctAdBranch in (");

                    boolean firstLoop = true;

                    Iterator j = branchList.iterator();

                    while (j.hasNext()) {

                        if (firstLoop == false) {
                            jbossQl.append(", ");
                        } else {
                            firstLoop = false;
                        }

                        AdBranchDetails mdetails = (AdBranchDetails) j.next();

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

                    jbossQl.append("ili.invItemLocation.invItem.iiName=?" + (ctr + 1) + " ");
                    obj[ctr] = criteria.get("itemName");
                    ctr++;
                }

                if (criteria.containsKey("category")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("ili.invItemLocation.invItem.iiAdLvCategory=?" + (ctr + 1) + " ");
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

                    jbossQl.append("ili.invItemLocation.invLocation.locName=?" + (ctr + 1) + " ");
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

                    jbossQl.append("ili.arReceipt.rctDate>=?" + (ctr + 1) + " ");
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

                    jbossQl.append("ili.arReceipt.rctDate<=?" + (ctr + 1) + " ");
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

                    jbossQl.append("ili.invItemLocation.invItem.iiClass=?" + (ctr + 1) + " ");
                    obj[ctr] = criteria.get("itemClass");
                    ctr++;
                }

                if (criteria.containsKey("customerCode")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("ili.arReceipt.arCustomer.cstCustomerCode LIKE '%" + criteria.get("customerCode") + "%' ");
                }

                if (criteria.containsKey("customerBatch")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("ili.arReceipt.arCustomer.cstCustomerBatch LIKE '%" + criteria.get("customerBatch") + "%' ");
                }

                if (criteria.containsKey("salesperson")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("ili.arReceipt.arSalesperson.slpSalespersonCode LIKE '%" + criteria.get("salesperson") + "%' ");
                }

                if (criteria.containsKey("region")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("ili.arReceipt.arCustomer.cstAdLvRegion=?" + (ctr + 1) + " ");
                    obj[ctr] = criteria.get("region");
                    ctr++;
                }

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("ili.arReceipt.rctPosted = 0 AND ili.arReceipt.rctVoid = 0 AND ili.iliAdCompany=" + AD_CMPNY + " ");

                orderBy = null;

                if (ORDER_BY.equals("DATE")) {

                    orderBy = "ili.arReceipt.rctDate";

                } else if (ORDER_BY.equals("ITEM NAME")) {

                    orderBy = "ili.invItemLocation.invItem.iiName";
                }

                if (orderBy != null) {

                    jbossQl.append("ORDER BY " + orderBy);
                }
                Debug.print("Receipts jbossQl.toString()" + jbossQl.toString());
                Collection arReceipts = arInvoiceLineItemHome.getIliByCriteria(jbossQl.toString(), obj);

                i = arReceipts.iterator();

                while (i.hasNext()) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) i.next();

                    ArRepSalesDetails details = new ArRepSalesDetails();

                    details.setSlsItemName(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                    details.setSlsItemDescription(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiDescription());
                    details.setSlsItemAdLvCategory(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiAdLvCategory());
                    details.setSlsUnit(arInvoiceLineItem.getInvUnitOfMeasure().getUomName());
                    details.setSlsQuantitySold(arInvoiceLineItem.getIliQuantity());

                    details.setSlsUnitPrice(EJBCommon.roundIt((arInvoiceLineItem.getIliAmount() + arInvoiceLineItem.getIliTaxAmount()) / details.getSlsQuantitySold(), this.getGlFcPrecisionUnit(AD_CMPNY)));
                    details.setSlsAmount(EJBCommon.roundIt(arInvoiceLineItem.getIliAmount() + arInvoiceLineItem.getIliTaxAmount(), this.getGlFcPrecisionUnit(AD_CMPNY)));
                    details.setSlsOutputVat(arInvoiceLineItem.getIliTaxAmount());
                    details.setSlsGrossUnitPrice(arInvoiceLineItem.getIliUnitPrice());
                    details.setSlsDiscount(arInvoiceLineItem.getIliTotalDiscount());
                    details.setSlsDefaultUnitPrice(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost());

                    details.setSlsDate(arInvoiceLineItem.getArReceipt().getRctDate());
                    details.setSlsCustomerCode(arInvoiceLineItem.getArReceipt().getArCustomer().getCstCustomerCode());
                    details.setSlsCustomerBatch(arInvoiceLineItem.getArReceipt().getArCustomer().getCstCustomerBatch());
                    details.setSlsCustomerName(arInvoiceLineItem.getArReceipt().getArCustomer().getCstName());
                    details.setSlsDocumentNumber(arInvoiceLineItem.getArReceipt().getRctNumber());
                    details.setSlsCustomerAddress(arInvoiceLineItem.getArReceipt().getArCustomer().getCstAddress());
                    Debug.print("unit: " + arInvoiceLineItem.getInvUnitOfMeasure().getUomName());
                    if (arInvoiceLineItem.getArReceipt().getArSalesperson() != null) {
                        details.setSlsSalespersonCode(arInvoiceLineItem.getArReceipt().getArSalesperson().getSlpSalespersonCode());
                        details.setSlsSalespersonName(arInvoiceLineItem.getArReceipt().getArSalesperson().getSlpName());
                    }

                    try {
                        String slsRefNum = (arInvoiceLineItem.getArReceipt().getRctReferenceNumber());
                        details.setSlsReferenceNumber(slsRefNum == null ? "" : slsRefNum);
                    } catch (Exception ex) {
                        details.setSlsReferenceNumber("");
                    }
                    details.setSlsCustomerRegion(arInvoiceLineItem.getArReceipt().getArCustomer().getCstAdLvRegion());
                    details.setSlsCustomerStateProvince(arInvoiceLineItem.getArReceipt().getArCustomer().getCstStateProvince());
                    try {
                        details.setSlsReferenceNumber(arInvoiceLineItem.getArInvoice().getInvReferenceNumber());
                    } catch (Exception ex) {
                        details.setSlsReferenceNumber("");
                    }
                    // Debug.print("4 : "+details.getSlsReferenceNumber());
                    details.setOrderBy(ORDER_BY);

                    list.add(details);
                }

                // get unposted sales order invoices

                jbossQl = new StringBuffer();
                firstArgument = true;
                ctr = 0;

                jbossQl.append("SELECT OBJECT(sil) FROM ArSalesOrderInvoiceLine sil ");

                if (branchList.isEmpty()) {

                    throw new GlobalNoRecordFoundException();

                } else {

                    jbossQl.append(" WHERE sil.arInvoice.invAdBranch in (");

                    boolean firstLoop = true;

                    Iterator j = branchList.iterator();

                    while (j.hasNext()) {

                        if (firstLoop == false) {
                            jbossQl.append(", ");
                        } else {
                            firstLoop = false;
                        }

                        AdBranchDetails mdetails = (AdBranchDetails) j.next();

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

                    jbossQl.append("sil.arSalesOrderLine.invItemLocation.invItem.iiName=?" + (ctr + 1) + " ");
                    obj[ctr] = criteria.get("itemName");
                    ctr++;
                }

                if (criteria.containsKey("category")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("sil.arSalesOrderLine.invItemLocation.invItem.iiAdLvCategory=?" + (ctr + 1) + " ");
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

                    jbossQl.append("sil.arSalesOrderLine.invItemLocation.invLocation.locName=?" + (ctr + 1) + " ");
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

                    jbossQl.append("sil.arInvoice.invDate>=?" + (ctr + 1) + " ");
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

                    jbossQl.append("sil.arInvoice.invDate<=?" + (ctr + 1) + " ");
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

                    jbossQl.append("sil.arSalesOrderLine.invItemLocation.invItem.iiClass=?" + (ctr + 1) + " ");
                    obj[ctr] = criteria.get("itemClass");
                    ctr++;
                }

                if (criteria.containsKey("customerCode")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("sil.arInvoice.arCustomer.cstCustomerCode LIKE '%" + criteria.get("customerCode") + "%' ");
                }

                if (criteria.containsKey("customerBatch")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("sil.arInvoice.arCustomer.cstCustomerBatch LIKE '%" + criteria.get("customerBatch") + "%' ");
                }

                if (criteria.containsKey("salesperson")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("sil.arInvoice.arSalesperson.slpSalespersonCode LIKE '%" + criteria.get("salesperson") + "%' ");
                }

                if (criteria.containsKey("salesperson")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("ili.arReceipt.arSalesperson.slpSalespersonCode LIKE '%" + criteria.get("salesperson") + "%' ");
                }

                if (criteria.containsKey("region")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("sil.arInvoice.arCustomer.cstAdLvRegion=?" + (ctr + 1) + " ");
                    obj[ctr] = criteria.get("region");
                    ctr++;
                }

                if (criteria.containsKey("orderStatus")) {

                    if (!criteria.get("orderStatus").equals("")) {
                        if (!firstArgument) {
                            jbossQl.append("AND ");
                        } else {
                            firstArgument = false;
                            jbossQl.append("WHERE ");
                        }
                        orderStatus = (String) criteria.get("orderStatus");

                        jbossQl.append("sil.arSalesOrderLine.arSalesOrder.soOrderStatus='" + orderStatus + "' ");
                    }
                }

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("sil.arInvoice.invPosted = 0 AND sil.arInvoice.invVoid = 0 AND sil.silAdCompany=" + AD_CMPNY + " ");

                orderBy = null;

                if (ORDER_BY.equals("DATE")) {

                    orderBy = "sil.arInvoice.invDate";

                } else if (ORDER_BY.equals("ITEM NAME")) {

                    orderBy = "sil.arSalesOrderLine.invItemLocation.invItem.iiName";
                }

                if (orderBy != null) {

                    jbossQl.append("ORDER BY " + orderBy);
                }

                Collection arSalesOrderInvoiceLines = arSalesOrderInvoiceLineHome.getSalesOrderInvoiceLineByCriteria(jbossQl.toString(), obj);

                i = arSalesOrderInvoiceLines.iterator();

                while (i.hasNext()) {

                    LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) i.next();

                    if (orderStatus != null && !arSalesOrderInvoiceLine.getArSalesOrderLine().getArSalesOrder().getSoOrderStatus().equals(orderStatus)) {
                        continue;
                    }

                    ArRepSalesDetails details = new ArRepSalesDetails();

                    details.setSlsItemName(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvItem().getIiName());
                    details.setSlsItemDescription(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvItem().getIiDescription());
                    details.setSlsItemAdLvCategory(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvItem().getIiAdLvCategory());
                    details.setSlsUnit(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvUnitOfMeasure().getUomName());
                    details.setSlsQuantitySold(arSalesOrderInvoiceLine.getSilQuantityDelivered());
                    details.setSlsUnitPrice(EJBCommon.roundIt((arSalesOrderInvoiceLine.getSilAmount() + arSalesOrderInvoiceLine.getSilTaxAmount()) / details.getSlsQuantitySold(), this.getGlFcPrecisionUnit(AD_CMPNY)));
                    details.setSlsAmount(EJBCommon.roundIt(arSalesOrderInvoiceLine.getSilAmount() + arSalesOrderInvoiceLine.getSilTaxAmount(), this.getGlFcPrecisionUnit(AD_CMPNY)));
                    details.setSlsOutputVat(arSalesOrderInvoiceLine.getSilTaxAmount());
                    details.setSlsGrossUnitPrice(arSalesOrderInvoiceLine.getArSalesOrderLine().getSolUnitPrice());
                    details.setSlsDiscount(arSalesOrderInvoiceLine.getSilTotalDiscount());
                    details.setSlsDefaultUnitPrice(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvItem().getIiUnitCost());

                    details.setSlsDate(arSalesOrderInvoiceLine.getArInvoice().getInvDate());
                    details.setSlsCustomerCode(arSalesOrderInvoiceLine.getArInvoice().getArCustomer().getCstCustomerCode());
                    details.setSlsCustomerBatch(arSalesOrderInvoiceLine.getArInvoice().getArCustomer().getCstCustomerBatch());
                    details.setSlsCustomerName(arSalesOrderInvoiceLine.getArInvoice().getArCustomer().getCstName());
                    details.setSlsDocumentNumber(arSalesOrderInvoiceLine.getArInvoice().getInvNumber());
                    details.setSlsCustomerAddress(arSalesOrderInvoiceLine.getArInvoice().getArCustomer().getCstAddress());

                    if (arSalesOrderInvoiceLine.getArInvoice().getArSalesperson() != null) {
                        details.setSlsSalespersonCode(arSalesOrderInvoiceLine.getArInvoice().getArSalesperson().getSlpSalespersonCode());
                        details.setSlsSalespersonName(arSalesOrderInvoiceLine.getArInvoice().getArSalesperson().getSlpName());
                    }

                    details.setSlsCustomerRegion(arSalesOrderInvoiceLine.getArInvoice().getArCustomer().getCstAdLvRegion());
                    details.setSlsCustomerStateProvince(arSalesOrderInvoiceLine.getArInvoice().getArCustomer().getCstStateProvince());

                    details.setSlsSalesOrderNumber(arSalesOrderInvoiceLine.getArSalesOrderLine().getArSalesOrder().getSoDocumentNumber());
                    details.setSlsSalesOrderDate(arSalesOrderInvoiceLine.getArSalesOrderLine().getArSalesOrder().getSoDate());
                    details.setSlsSalesOrderQuantity(arSalesOrderInvoiceLine.getArSalesOrderLine().getSolQuantity());
                    details.setSlsSalesOrderAmount(arSalesOrderInvoiceLine.getArSalesOrderLine().getSolAmount());
                    details.setSlsSalesOrderSalesPrice(arSalesOrderInvoiceLine.getArSalesOrderLine().getSolUnitPrice());

                    try {
                        details.setSlsReferenceNumber(arSalesOrderInvoiceLine.getArSalesOrderLine().getArSalesOrder().getSoDocumentNumber());
                    } catch (Exception ex) {
                        details.setSlsReferenceNumber("");
                    }
                    details.setSlsOrderStatus(arSalesOrderInvoiceLine.getArSalesOrderLine().getArSalesOrder().getSoOrderStatus());
                    details.setOrderBy(ORDER_BY);
                    // Debug.print("5 : "+details.getSlsReferenceNumber());
                    list.add(details);
                }
            }

            // if include unserved sales orders

            if (criteria.get("includeUnservedSO").equals("YES")) {

                String orderStatus = null;
                obj = new Object[criteriaSize];
                String orderBy = null;

                jbossQl = new StringBuffer();
                firstArgument = true;
                ctr = 0;

                jbossQl.append("SELECT OBJECT(sol) FROM ArSalesOrderLine sol");

                if (branchList.isEmpty()) {

                    throw new GlobalNoRecordFoundException();

                } else {

                    jbossQl.append(" WHERE sol.arSalesOrder.soAdBranch in (");

                    boolean firstLoop = true;

                    Iterator j = branchList.iterator();

                    while (j.hasNext()) {

                        if (firstLoop == false) {
                            jbossQl.append(", ");
                        } else {
                            firstLoop = false;
                        }

                        AdBranchDetails mdetails = (AdBranchDetails) j.next();

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

                    jbossQl.append("sol.invItemLocation.invItem.iiName=?" + (ctr + 1) + " ");
                    obj[ctr] = criteria.get("itemName");
                    ctr++;
                }

                if (criteria.containsKey("category")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("sol.invItemLocation.invItem.iiAdLvCategory=?" + (ctr + 1) + " ");
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

                    jbossQl.append("sol.invItemLocation.invLocation.locName=?" + (ctr + 1) + " ");
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

                    jbossQl.append("sol.arSalesOrder.soDate>=?" + (ctr + 1) + " ");
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

                    jbossQl.append("sol.arSalesOrder.soDate<=?" + (ctr + 1) + " ");
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

                    jbossQl.append("sol.invItemLocation.invItem.iiClass=?" + (ctr + 1) + " ");
                    obj[ctr] = criteria.get("itemClass");
                    ctr++;
                }

                if (criteria.containsKey("customerCode")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("sol.arSalesOrder.arCustomer.cstCustomerCode LIKE '%" + criteria.get("customerCode") + "%' ");
                }

                if (criteria.containsKey("customerBatch")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("sol.arSalesOrder.arCustomer.cstCustomerBatch LIKE '%" + criteria.get("customerBatch") + "%' ");
                }

                if (criteria.containsKey("salesperson")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("sol.arSalesOrder.arSalesperson.slpSalespersonCode LIKE '%" + criteria.get("salesperson") + "%' ");
                }

                if (criteria.containsKey("region")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("sol.arSalesOrder.arCustomer.cstAdLvRegion=?" + (ctr + 1) + " ");
                    obj[ctr] = criteria.get("region");
                    ctr++;
                }

                if (criteria.containsKey("orderStatus")) {
                    String oS = null;
                    oS = (String) criteria.get("orderStatus");

                    if (!criteria.get("orderStatus").equals("")) {
                        if (!firstArgument) {
                            jbossQl.append("AND ");
                        } else {
                            firstArgument = false;
                            jbossQl.append("WHERE ");
                        }
                        orderStatus = (String) criteria.get("orderStatus");
                        jbossQl.append("sol.arSalesOrder.soOrderStatus='" + orderStatus + "' ");
                    }
                }

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                if (criteria.get("includeUnposted").equals("YES")) {

                    jbossQl.append("sol.arSalesOrder.soVoid = 0 AND sol.solAdCompany=" + AD_CMPNY + " ");

                } else {

                    jbossQl.append("sol.arSalesOrder.soPosted = 1 AND sol.arSalesOrder.soVoid = 0 AND sol.solAdCompany=" + AD_CMPNY + " ");
                }

                orderBy = null;

                if (ORDER_BY.equals("DATE")) {

                    orderBy = "sol.arSalesOrder.soDate";

                } else if (ORDER_BY.equals("ITEM NAME")) {

                    orderBy = "sol.invItemLocation.invItem.iiName";
                }

                if (orderBy != null) {

                    jbossQl.append("ORDER BY " + orderBy + ", sol.arSalesOrder.soDocumentNumber");
                }

                Collection arSalesOrderLines = arSalesOrderLineHome.getSalesOrderLineByCriteria(jbossQl.toString(), obj, 0, 0);

                Debug.print(arSalesOrderLines.size() + " the size");
                i = arSalesOrderLines.iterator();

                while (i.hasNext()) {

                    LocalArSalesOrderLine arSalesOrderLine = (LocalArSalesOrderLine) i.next();

                    if (!arSalesOrderLine.getArSalesOrderInvoiceLines().isEmpty()) {
                        continue;
                    }

                    if (orderStatus != null && !arSalesOrderLine.getArSalesOrder().getSoOrderStatus().equals(orderStatus)) {
                        continue;
                    }

                    ArRepSalesDetails details = new ArRepSalesDetails();

                    details.setSlsItemName(arSalesOrderLine.getInvItemLocation().getInvItem().getIiName());
                    details.setSlsItemDescription(arSalesOrderLine.getInvItemLocation().getInvItem().getIiDescription());
                    details.setSlsItemAdLvCategory(arSalesOrderLine.getInvItemLocation().getInvItem().getIiAdLvCategory());
                    details.setSlsUnit(arSalesOrderLine.getInvUnitOfMeasure().getUomName());
                    details.setSlsQuantitySold(0);
                    details.setSlsUnitPrice(arSalesOrderLine.getSolUnitPrice());
                    details.setSlsDefaultUnitPrice(arSalesOrderLine.getInvItemLocation().getInvItem().getIiUnitCost());

                    details.setSlsDate(arSalesOrderLine.getArSalesOrder().getSoDate());
                    details.setSlsCustomerCode(arSalesOrderLine.getArSalesOrder().getArCustomer().getCstCustomerCode());
                    details.setSlsCustomerBatch(arSalesOrderLine.getArSalesOrder().getArCustomer().getCstCustomerBatch());

                    details.setSlsCustomerName(arSalesOrderLine.getArSalesOrder().getArCustomer().getCstName());
                    details.setSlsCustomerAddress(arSalesOrderLine.getArSalesOrder().getArCustomer().getCstAddress());

                    if (arSalesOrderLine.getArSalesOrder().getArSalesperson() != null) {
                        details.setSlsSalespersonCode(arSalesOrderLine.getArSalesOrder().getArSalesperson().getSlpSalespersonCode());
                        details.setSlsSalespersonName(arSalesOrderLine.getArSalesOrder().getArSalesperson().getSlpName());
                    }

                    details.setSlsCustomerRegion(arSalesOrderLine.getArSalesOrder().getArCustomer().getCstAdLvRegion());
                    details.setSlsCustomerStateProvince(arSalesOrderLine.getArSalesOrder().getArCustomer().getCstStateProvince());

                    details.setSlsSalesOrderNumber(arSalesOrderLine.getArSalesOrder().getSoDocumentNumber());
                    details.setSlsSalesOrderDate(arSalesOrderLine.getArSalesOrder().getSoDate());
                    details.setSlsSalesOrderQuantity(arSalesOrderLine.getSolQuantity());
                    details.setSlsSalesOrderAmount(arSalesOrderLine.getSolAmount());
                    details.setSlsSalesOrderSalesPrice(arSalesOrderLine.getSolUnitPrice());
                    try {
                        details.setSlsReferenceNumber(arSalesOrderLine.getArSalesOrder().getSoDocumentNumber());
                    } catch (Exception ex) {
                        details.setSlsReferenceNumber("");
                    }
                    details.setSlsOrderStatus(arSalesOrderLine.getArSalesOrder().getSoOrderStatus());
                    details.setOrderBy(ORDER_BY);
                    // Debug.print("6 : "+details.getSlsReferenceNumber());
                    Debug.print("unit : " + arSalesOrderLine.getInvUnitOfMeasure().getUomName());
                    list.add(details);
                }
            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            if (GROUP_BY.equalsIgnoreCase("CUSTOMER")) {

                Collections.sort(list, ArRepSalesDetails.CustomerComparator);

            } else if (GROUP_BY.equalsIgnoreCase("CATEGORY")) {

                Collections.sort(list, ArRepSalesDetails.ItemCategoryComparator);

            } else if (GROUP_BY.equalsIgnoreCase("ITEM")) {

                Collections.sort(list, ArRepSalesDetails.ItemComparator);

            } else if (GROUP_BY.equalsIgnoreCase("SALES ORDER")) {

                Collections.sort(list, ArRepSalesDetails.SalesOrderNumberComparator);

            } else if (GROUP_BY.equalsIgnoreCase("USR-SI")) {

                Collections.sort(list, ArRepSalesDetails.USRSIComparator);

            } else if (GROUP_BY.equalsIgnoreCase("USR-CS")) {

                Collections.sort(list, ArRepSalesDetails.USRCSComparator);

            } else if (GROUP_BY.equalsIgnoreCase("SIS")) {

                Collections.sort(list, ArRepSalesDetails.SISComparator);

            } else if (GROUP_BY.equalsIgnoreCase("SBS")) {

                Collections.sort(list, ArRepSalesDetails.SBSComparator);

            } else if (GROUP_BY.equalsIgnoreCase("CMS")) {

                Collections.sort(list, ArRepSalesDetails.CMSComparator);

            } else if (GROUP_BY.equalsIgnoreCase("CSS")) {

                Collections.sort(list, ArRepSalesDetails.CSSComparator);

            } else if (GROUP_BY.equalsIgnoreCase("DSS")) {

                Collections.sort(list, ArRepSalesDetails.DSSComparator);

            } else if (GROUP_BY.equalsIgnoreCase("DSS2")) {

                Collections.sort(list, ArRepSalesDetails.DSS2Comparator);

            } else if (GROUP_BY.equalsIgnoreCase("DSS3")) {

                Collections.sort(list, ArRepSalesDetails.DSS3Comparator);
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList executeArRepSalesSub(HashMap criteria, ArrayList branchList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepSalesControllerBean executeArRepSalesSub");

        ArrayList list = new ArrayList();

        try {

            String customerCode = null;
            String customerBatch = null;
            String salesperson = null;
            String region = null;

            if (criteria.containsKey("customerCode")) {

                customerCode = (String) criteria.get("customerCode");
            }

            if (criteria.containsKey("customerBatch")) {

                customerBatch = (String) criteria.get("customerBatch");
            }

            if (criteria.containsKey("salesperson")) {

                salesperson = (String) criteria.get("salesperson");
            }

            if (criteria.containsKey("region")) {

                region = (String) criteria.get("region");
            }

            StringBuffer jbossQl = new StringBuffer();
            jbossQl.append("SELECT OBJECT(cst) FROM InvCosting cst ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = 0;

            Object[] obj = null;

            if (branchList.isEmpty()) {

                throw new GlobalNoRecordFoundException();

            } else {

                jbossQl.append(" WHERE cst.cstAdBranch in (");

                boolean firstLoop = true;

                Iterator j = branchList.iterator();

                while (j.hasNext()) {

                    if (firstLoop == false) {
                        jbossQl.append(", ");
                    } else {
                        firstLoop = false;
                    }

                    AdBranchDetails mdetails = (AdBranchDetails) j.next();

                    jbossQl.append(mdetails.getBrCode());
                }

                jbossQl.append(") ");

                firstArgument = false;
            }

            // Allocate the size of the object parameter

            if (criteria.containsKey("category")) {

                criteriaSize++;
            }

            if (criteria.containsKey("location")) {

                criteriaSize++;
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

            if (criteria.containsKey("itemName")) {

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

                jbossQl.append("cst.invItemLocation.invItem.iiName=?" + (ctr + 1) + " ");
                obj[ctr] = criteria.get("itemName");
                ctr++;
            }

            if (criteria.containsKey("category")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.invItemLocation.invItem.iiAdLvCategory=?" + (ctr + 1) + " ");
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

                jbossQl.append("cst.invItemLocation.invLocation.locName=?" + (ctr + 1) + " ");
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

                jbossQl.append("cst.cstDate>=?" + (ctr + 1) + " ");
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

                jbossQl.append("cst.cstDate<=?" + (ctr + 1) + " ");
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

                jbossQl.append("cst.invItemLocation.invItem.iiClass=?" + (ctr + 1) + " ");
                obj[ctr] = criteria.get("itemClass");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append(" cst.cstQuantitySold <> 0 AND cst.cstAdCompany=" + AD_CMPNY + " ");

            jbossQl.append("ORDER BY cst.invItemLocation.invItem.iiName, cst.cstDate, cst.cstLineNumber");

            Collection invCostings = invCostingHome.getCstByCriteria(jbossQl.toString(), obj, 0, 0);

            Iterator i = invCostings.iterator();

            while (i.hasNext()) {

                LocalInvCosting invCosting = (LocalInvCosting) i.next();

                if (invCosting.getArInvoiceLineItem() != null && invCosting.getArInvoiceLineItem().getArInvoice() != null && invCosting.getArInvoiceLineItem().getArInvoice().getInvVoid() == EJBCommon.FALSE || invCosting.getArInvoiceLineItem() != null && invCosting.getArInvoiceLineItem().getArReceipt() != null && invCosting.getArInvoiceLineItem().getArReceipt().getRctVoid() == EJBCommon.FALSE || invCosting.getArSalesOrderInvoiceLine() != null && invCosting.getArSalesOrderInvoiceLine().getArInvoice() != null && invCosting.getArSalesOrderInvoiceLine().getArInvoice().getInvVoid() == EJBCommon.FALSE) {

                    ArRepSalesDetails details = new ArRepSalesDetails();

                    details.setSlsItemName(invCosting.getInvItemLocation().getInvItem().getIiName());
                    details.setSlsItemDescription(invCosting.getInvItemLocation().getInvItem().getIiDescription());
                    details.setSlsDate(invCosting.getCstDate());
                    details.setSlsItemAdLvCategory(invCosting.getInvItemLocation().getInvItem().getIiAdLvCategory());

                    if (invCosting.getArInvoiceLineItem() != null) {

                        details.setSlsUnit(invCosting.getArInvoiceLineItem().getInvUnitOfMeasure().getUomName());
                        details.setSlsQuantitySold(invCosting.getArInvoiceLineItem().getIliQuantity());
                        details.setSlsUnitPrice(EJBCommon.roundIt((invCosting.getArInvoiceLineItem().getIliAmount() + invCosting.getArInvoiceLineItem().getIliTaxAmount()) / details.getSlsQuantitySold(), this.getGlFcPrecisionUnit(AD_CMPNY)));
                        details.setSlsAmount(EJBCommon.roundIt(invCosting.getArInvoiceLineItem().getIliAmount() + invCosting.getArInvoiceLineItem().getIliTaxAmount(), this.getGlFcPrecisionUnit(AD_CMPNY)));
                        details.setSlsOutputVat(invCosting.getArInvoiceLineItem().getIliTaxAmount());
                        details.setSlsGrossUnitPrice(invCosting.getArInvoiceLineItem().getIliUnitPrice());
                        details.setSlsDiscount(invCosting.getArInvoiceLineItem().getIliTotalDiscount());
                        details.setSlsDefaultUnitPrice(invCosting.getInvItemLocation().getInvItem().getIiUnitCost());

                        if (invCosting.getArInvoiceLineItem().getArInvoice() != null) {

                            details.setSlsCustomerCode(invCosting.getArInvoiceLineItem().getArInvoice().getArCustomer().getCstCustomerCode());
                            details.setSlsCustomerBatch(invCosting.getArInvoiceLineItem().getArInvoice().getArCustomer().getCstCustomerBatch());

                            details.setSlsCustomerName(invCosting.getArInvoiceLineItem().getArInvoice().getArCustomer().getCstName());
                            details.setSlsDocumentNumber(invCosting.getArInvoiceLineItem().getArInvoice().getInvNumber());

                            if (invCosting.getArInvoiceLineItem().getArInvoice().getArSalesperson() != null) {
                                details.setSlsSalespersonCode(invCosting.getArInvoiceLineItem().getArInvoice().getArSalesperson().getSlpSalespersonCode());
                                details.setSlsSalespersonName(invCosting.getArInvoiceLineItem().getArInvoice().getArSalesperson().getSlpName());
                            }

                            details.setSlsCustomerRegion(invCosting.getArInvoiceLineItem().getArInvoice().getArCustomer().getCstAdLvRegion());
                            details.setSlsCustomerStateProvince(invCosting.getArInvoiceLineItem().getArInvoice().getArCustomer().getCstStateProvince());
                        }
                        if (invCosting.getArInvoiceLineItem().getArReceipt() != null) {

                            details.setSlsCustomerCode(invCosting.getArInvoiceLineItem().getArReceipt().getArCustomer().getCstCustomerCode());
                            details.setSlsCustomerBatch(invCosting.getArInvoiceLineItem().getArReceipt().getArCustomer().getCstCustomerBatch());

                            details.setSlsCustomerName(invCosting.getArInvoiceLineItem().getArReceipt().getArCustomer().getCstName());
                            details.setSlsDocumentNumber(invCosting.getArInvoiceLineItem().getArReceipt().getRctNumber());

                            if (invCosting.getArInvoiceLineItem().getArReceipt().getArSalesperson() != null) {
                                details.setSlsSalespersonCode(invCosting.getArInvoiceLineItem().getArReceipt().getArSalesperson().getSlpSalespersonCode());
                                details.setSlsSalespersonName(invCosting.getArInvoiceLineItem().getArReceipt().getArSalesperson().getSlpName());
                            }

                            try {
                                String slsRefNum = (invCosting.getArInvoiceLineItem().getArReceipt().getRctReferenceNumber());
                                details.setSlsReferenceNumber(slsRefNum == null ? "" : slsRefNum);
                            } catch (Exception ex) {
                                details.setSlsReferenceNumber("");
                            }
                            details.setSlsCustomerRegion(invCosting.getArInvoiceLineItem().getArReceipt().getArCustomer().getCstAdLvRegion());
                            details.setSlsCustomerStateProvince(invCosting.getArInvoiceLineItem().getArReceipt().getArCustomer().getCstStateProvince());
                        }

                        if (invCosting.getArInvoiceLineItem().getArInvoice() != null && invCosting.getArInvoiceLineItem().getArInvoice().getInvCreditMemo() == 1) {

                            details.setSlsQuantitySold(invCosting.getArInvoiceLineItem().getIliQuantity() * -1);
                            details.setSlsAmount(EJBCommon.roundIt(invCosting.getArInvoiceLineItem().getIliAmount() + invCosting.getArInvoiceLineItem().getIliTaxAmount(), this.getGlFcPrecisionUnit(AD_CMPNY)) * -1);
                            details.setSlsOutputVat(invCosting.getArInvoiceLineItem().getIliTaxAmount() * -1);
                            details.setSlsDiscount(invCosting.getArInvoiceLineItem().getIliTotalDiscount() * -1);
                        }

                    } else if (invCosting.getArSalesOrderInvoiceLine() != null) {

                        details.setSlsUnit(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getInvUnitOfMeasure().getUomName());
                        details.setSlsQuantitySold(invCosting.getArSalesOrderInvoiceLine().getSilQuantityDelivered());
                        details.setSlsUnitPrice(EJBCommon.roundIt((invCosting.getArSalesOrderInvoiceLine().getSilAmount() + invCosting.getArSalesOrderInvoiceLine().getSilTaxAmount()) / details.getSlsQuantitySold(), this.getGlFcPrecisionUnit(AD_CMPNY)));
                        details.setSlsAmount(EJBCommon.roundIt(invCosting.getArSalesOrderInvoiceLine().getSilAmount() + invCosting.getArSalesOrderInvoiceLine().getSilTaxAmount(), this.getGlFcPrecisionUnit(AD_CMPNY)));
                        details.setSlsOutputVat(invCosting.getArSalesOrderInvoiceLine().getSilTaxAmount());
                        details.setSlsGrossUnitPrice(invCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getSolUnitPrice());
                        details.setSlsDiscount(invCosting.getArSalesOrderInvoiceLine().getSilTotalDiscount());
                        details.setSlsDefaultUnitPrice(invCosting.getInvItemLocation().getInvItem().getIiUnitCost());

                        if (invCosting.getArSalesOrderInvoiceLine().getArInvoice() != null) {

                            details.setSlsCustomerCode(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArCustomer().getCstCustomerCode());
                            details.setSlsCustomerBatch(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArCustomer().getCstCustomerBatch());

                            details.setSlsCustomerName(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArCustomer().getCstName());
                            details.setSlsDocumentNumber(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getInvNumber());

                            if (invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArSalesperson() != null) {
                                details.setSlsSalespersonCode(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArSalesperson().getSlpSalespersonCode());
                                details.setSlsSalespersonName(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArSalesperson().getSlpName());
                            }

                            details.setSlsCustomerRegion(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArCustomer().getCstAdLvRegion());
                            details.setSlsCustomerStateProvince(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArCustomer().getCstStateProvince());
                        }
                    }

                    if (customerCode != null && !details.getSlsCustomerCode().contains(customerCode)) {
                        continue;
                    }
                    if (customerBatch != null && !details.getSlsCustomerBatch().contains(customerBatch)) {
                        continue;
                    }
                    if (salesperson != null && (details.getSlsSalespersonCode() == null || (details.getSlsSalespersonCode() != null && !details.getSlsSalespersonCode().contains(salesperson)))) {
                        continue;
                    }
                    if (region != null && (details.getSlsCustomerRegion() == null || (details.getSlsCustomerRegion() != null && !details.getSlsCustomerRegion().equals(region)))) {
                        continue;
                    }

                    list.add(details);
                }
            }

            if (criteria.containsKey("region")) {

                criteriaSize++;
            }

            // if include unposted

            if (criteria.get("includeUnposted").equals("YES")) {

                obj = new Object[criteriaSize];
                String orderBy = null;

                // get unposted invoices

                jbossQl = new StringBuffer();
                firstArgument = true;
                ctr = 0;

                jbossQl.append("SELECT OBJECT(ili) FROM ArInvoiceLineItem ili ");

                if (branchList.isEmpty()) {

                    throw new GlobalNoRecordFoundException();

                } else {

                    jbossQl.append(" WHERE ili.arInvoice.invAdBranch in (");

                    boolean firstLoop = true;

                    Iterator j = branchList.iterator();

                    while (j.hasNext()) {

                        if (firstLoop == false) {
                            jbossQl.append(", ");
                        } else {
                            firstLoop = false;
                        }

                        AdBranchDetails mdetails = (AdBranchDetails) j.next();

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

                    jbossQl.append("ili.invItemLocation.invItem.iiName=?" + (ctr + 1) + " ");
                    obj[ctr] = criteria.get("itemName");
                    ctr++;
                }

                if (criteria.containsKey("category")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("ili.invItemLocation.invItem.iiAdLvCategory=?" + (ctr + 1) + " ");
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

                    jbossQl.append("ili.invItemLocation.invLocation.locName=?" + (ctr + 1) + " ");
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

                    jbossQl.append("ili.arInvoice.invDate>=?" + (ctr + 1) + " ");
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

                    jbossQl.append("ili.arInvoice.invDate<=?" + (ctr + 1) + " ");
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

                    jbossQl.append("ili.invItemLocation.invItem.iiClass=?" + (ctr + 1) + " ");
                    obj[ctr] = criteria.get("itemClass");
                    ctr++;
                }

                if (criteria.containsKey("customerCode")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("ili.arInvoice.arCustomer.cstCustomerCode LIKE '%" + criteria.get("customerCode") + "%' ");
                }

                if (criteria.containsKey("customerBatch")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("ili.arInvoice.arCustomer.cstCustomerBatch LIKE '%" + criteria.get("customerBatch") + "%' ");
                }

                if (criteria.containsKey("salesperson")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("ili.arInvoice.arSalesperson.slpSalespersonCode LIKE '%" + criteria.get("salesperson") + "%' ");
                }

                if (criteria.containsKey("region")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("ili.arInvoice.arCustomer.cstAdLvRegion=?" + (ctr + 1) + " ");
                    obj[ctr] = criteria.get("region");
                    ctr++;
                }

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("ili.arInvoice.invPosted = 0 AND ili.arInvoice.invVoid = 0 AND ili.iliAdCompany=" + AD_CMPNY + " ");

                Collection arInvoices = arInvoiceLineItemHome.getIliByCriteria(jbossQl.toString(), obj);

                i = arInvoices.iterator();

                while (i.hasNext()) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) i.next();

                    ArRepSalesDetails details = new ArRepSalesDetails();

                    details.setSlsItemName(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                    details.setSlsItemDescription(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiDescription());
                    details.setSlsItemAdLvCategory(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiAdLvCategory());
                    details.setSlsUnit(arInvoiceLineItem.getInvUnitOfMeasure().getUomName());
                    details.setSlsUnitPrice(EJBCommon.roundIt((arInvoiceLineItem.getIliAmount() + arInvoiceLineItem.getIliTaxAmount()) / details.getSlsQuantitySold(), this.getGlFcPrecisionUnit(AD_CMPNY)));
                    details.setSlsGrossUnitPrice(arInvoiceLineItem.getIliUnitPrice());
                    details.setSlsDefaultUnitPrice(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost());
                    details.setSlsQuantitySold(arInvoiceLineItem.getArInvoice().getInvCreditMemo() == 0 ? arInvoiceLineItem.getIliQuantity() : arInvoiceLineItem.getIliQuantity() * 0);
                    details.setSlsAmount(arInvoiceLineItem.getArInvoice().getInvCreditMemo() == 0 ? EJBCommon.roundIt(arInvoiceLineItem.getIliAmount() + arInvoiceLineItem.getIliTaxAmount(), this.getGlFcPrecisionUnit(AD_CMPNY)) : EJBCommon.roundIt(arInvoiceLineItem.getIliAmount() + arInvoiceLineItem.getIliTaxAmount(), this.getGlFcPrecisionUnit(AD_CMPNY)) * -1);
                    details.setSlsOutputVat(arInvoiceLineItem.getArInvoice().getInvCreditMemo() == 0 ? arInvoiceLineItem.getIliTaxAmount() : arInvoiceLineItem.getIliTaxAmount() * -1);
                    details.setSlsDiscount(arInvoiceLineItem.getArInvoice().getInvCreditMemo() == 0 ? arInvoiceLineItem.getIliTotalDiscount() : arInvoiceLineItem.getIliTotalDiscount() * -1);

                    details.setSlsDate(arInvoiceLineItem.getArInvoice().getInvDate());
                    details.setSlsCustomerCode(arInvoiceLineItem.getArInvoice().getArCustomer().getCstCustomerCode());
                    details.setSlsCustomerBatch(arInvoiceLineItem.getArInvoice().getArCustomer().getCstCustomerBatch());

                    details.setSlsCustomerName(arInvoiceLineItem.getArInvoice().getArCustomer().getCstName());
                    details.setSlsDocumentNumber(arInvoiceLineItem.getArInvoice().getInvNumber());

                    list.add(details);
                }

                // get unposted misc receipts

                jbossQl = new StringBuffer();
                firstArgument = true;
                ctr = 0;

                jbossQl.append("SELECT OBJECT(ili) FROM ArInvoiceLineItem ili ");

                if (branchList.isEmpty()) {

                    throw new GlobalNoRecordFoundException();

                } else {

                    jbossQl.append(" WHERE ili.arReceipt.rctAdBranch in (");

                    boolean firstLoop = true;

                    Iterator j = branchList.iterator();

                    while (j.hasNext()) {

                        if (firstLoop == false) {
                            jbossQl.append(", ");
                        } else {
                            firstLoop = false;
                        }

                        AdBranchDetails mdetails = (AdBranchDetails) j.next();

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

                    jbossQl.append("ili.invItemLocation.invItem.iiName=?" + (ctr + 1) + " ");
                    obj[ctr] = criteria.get("itemName");
                    ctr++;
                }

                if (criteria.containsKey("category")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("ili.invItemLocation.invItem.iiAdLvCategory=?" + (ctr + 1) + " ");
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

                    jbossQl.append("ili.invItemLocation.invLocation.locName=?" + (ctr + 1) + " ");
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

                    jbossQl.append("ili.arReceipt.rctDate>=?" + (ctr + 1) + " ");
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

                    jbossQl.append("ili.arReceipt.rctDate<=?" + (ctr + 1) + " ");
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

                    jbossQl.append("ili.invItemLocation.invItem.iiClass=?" + (ctr + 1) + " ");
                    obj[ctr] = criteria.get("itemClass");
                    ctr++;
                }

                if (criteria.containsKey("customerCode")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("ili.arReceipt.arCustomer.cstCustomerCode LIKE '%" + criteria.get("customerCode") + "%' ");
                }

                if (criteria.containsKey("customerBatch")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("ili.arReceipt.arCustomer.cstCustomerBatch LIKE '%" + criteria.get("customerBatch") + "%' ");
                }

                if (criteria.containsKey("salesperson")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("ili.arReceipt.arSalesperson.slpSalespersonCode LIKE '%" + criteria.get("salesperson") + "%' ");
                }

                if (criteria.containsKey("region")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("ili.arReceipt.arCustomer.cstAdLvRegion=?" + (ctr + 1) + " ");
                    obj[ctr] = criteria.get("region");
                    ctr++;
                }

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("ili.arReceipt.rctPosted = 0 AND ili.arReceipt.rctVoid = 0 AND ili.iliAdCompany=" + AD_CMPNY + " ");

                Collection arReceipts = arInvoiceLineItemHome.getIliByCriteria(jbossQl.toString(), obj);

                i = arReceipts.iterator();

                while (i.hasNext()) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) i.next();

                    ArRepSalesDetails details = new ArRepSalesDetails();

                    details.setSlsItemName(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                    details.setSlsItemDescription(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiDescription());
                    details.setSlsItemAdLvCategory(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiAdLvCategory());
                    details.setSlsUnit(arInvoiceLineItem.getInvUnitOfMeasure().getUomName());
                    details.setSlsQuantitySold(arInvoiceLineItem.getIliQuantity());
                    details.setSlsUnitPrice(EJBCommon.roundIt((arInvoiceLineItem.getIliAmount() + arInvoiceLineItem.getIliTaxAmount()) / details.getSlsQuantitySold(), this.getGlFcPrecisionUnit(AD_CMPNY)));
                    details.setSlsAmount(EJBCommon.roundIt(arInvoiceLineItem.getIliAmount() + arInvoiceLineItem.getIliTaxAmount(), this.getGlFcPrecisionUnit(AD_CMPNY)));
                    details.setSlsOutputVat(arInvoiceLineItem.getIliTaxAmount());
                    details.setSlsGrossUnitPrice(arInvoiceLineItem.getIliUnitPrice());
                    details.setSlsDiscount(arInvoiceLineItem.getIliTotalDiscount());
                    details.setSlsDefaultUnitPrice(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost());

                    details.setSlsDate(arInvoiceLineItem.getArReceipt().getRctDate());
                    details.setSlsCustomerCode(arInvoiceLineItem.getArReceipt().getArCustomer().getCstCustomerCode());
                    details.setSlsCustomerBatch(arInvoiceLineItem.getArReceipt().getArCustomer().getCstCustomerBatch());
                    details.setSlsCustomerName(arInvoiceLineItem.getArReceipt().getArCustomer().getCstName());
                    details.setSlsDocumentNumber(arInvoiceLineItem.getArReceipt().getRctNumber());

                    try {
                        String slsRefNum = (arInvoiceLineItem.getArReceipt().getRctReferenceNumber());
                        details.setSlsReferenceNumber(slsRefNum == null ? "" : slsRefNum);
                    } catch (Exception ex) {
                        details.setSlsReferenceNumber("");
                    }

                    list.add(details);
                }

                // get unposted sales order invoices

                jbossQl = new StringBuffer();
                firstArgument = true;
                ctr = 0;

                jbossQl.append("SELECT OBJECT(sil) FROM ArSalesOrderInvoiceLine sil ");

                if (branchList.isEmpty()) {

                    throw new GlobalNoRecordFoundException();

                } else {

                    jbossQl.append(" WHERE sil.arInvoice.invAdBranch in (");

                    boolean firstLoop = true;

                    Iterator j = branchList.iterator();

                    while (j.hasNext()) {

                        if (firstLoop == false) {
                            jbossQl.append(", ");
                        } else {
                            firstLoop = false;
                        }

                        AdBranchDetails mdetails = (AdBranchDetails) j.next();

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

                    jbossQl.append("sil.arSalesOrderLine.invItemLocation.invItem.iiName=?" + (ctr + 1) + " ");
                    obj[ctr] = criteria.get("itemName");
                    ctr++;
                }

                if (criteria.containsKey("category")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("sil.arSalesOrderLine.invItemLocation.invItem.iiAdLvCategory=?" + (ctr + 1) + " ");
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

                    jbossQl.append("sil.arSalesOrderLine.invItemLocation.invLocation.locName=?" + (ctr + 1) + " ");
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

                    jbossQl.append("sil.arInvoice.invDate>=?" + (ctr + 1) + " ");
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

                    jbossQl.append("sil.arInvoice.invDate<=?" + (ctr + 1) + " ");
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

                    jbossQl.append("sil.arSalesOrderLine.invItemLocation.invItem.iiClass=?" + (ctr + 1) + " ");
                    obj[ctr] = criteria.get("itemClass");
                    ctr++;
                }

                if (criteria.containsKey("customerCode")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("sil.arInvoice.arCustomer.cstCustomerCode LIKE '%" + criteria.get("customerCode") + "%' ");
                }

                if (criteria.containsKey("customerBatch")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("sil.arInvoice.arCustomer.cstCustomerBatch LIKE '%" + criteria.get("customerBatch") + "%' ");
                }

                if (criteria.containsKey("salesperson")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("sil.arInvoice.arSalesperson.slpSalespersonCode LIKE '%" + criteria.get("salesperson") + "%' ");
                }

                if (criteria.containsKey("region")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("sil.arInvoice.arCustomer.cstAdLvRegion=?" + (ctr + 1) + " ");
                    obj[ctr] = criteria.get("region");
                    ctr++;
                }

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("sil.arInvoice.invPosted = 0 AND sil.arInvoice.invVoid = 0 AND sil.silAdCompany=" + AD_CMPNY + " ");

                Collection arSalesOrderInvoiceLines = arSalesOrderInvoiceLineHome.getSalesOrderInvoiceLineByCriteria(jbossQl.toString(), obj);

                i = arSalesOrderInvoiceLines.iterator();

                while (i.hasNext()) {

                    LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) i.next();

                    ArRepSalesDetails details = new ArRepSalesDetails();

                    details.setSlsItemName(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvItem().getIiName());
                    details.setSlsItemDescription(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvItem().getIiDescription());
                    details.setSlsItemAdLvCategory(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvItem().getIiAdLvCategory());
                    details.setSlsUnit(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvUnitOfMeasure().getUomName());
                    details.setSlsQuantitySold(arSalesOrderInvoiceLine.getSilQuantityDelivered());
                    details.setSlsUnitPrice(EJBCommon.roundIt((arSalesOrderInvoiceLine.getSilAmount() + arSalesOrderInvoiceLine.getSilTaxAmount()) / details.getSlsQuantitySold(), this.getGlFcPrecisionUnit(AD_CMPNY)));
                    details.setSlsAmount(EJBCommon.roundIt(arSalesOrderInvoiceLine.getSilAmount() + arSalesOrderInvoiceLine.getSilTaxAmount(), this.getGlFcPrecisionUnit(AD_CMPNY)));
                    details.setSlsOutputVat(arSalesOrderInvoiceLine.getSilTaxAmount());
                    details.setSlsGrossUnitPrice(arSalesOrderInvoiceLine.getArSalesOrderLine().getSolUnitPrice());
                    details.setSlsDiscount(arSalesOrderInvoiceLine.getSilTotalDiscount());
                    details.setSlsDefaultUnitPrice(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvItem().getIiUnitCost());

                    details.setSlsDate(arSalesOrderInvoiceLine.getArInvoice().getInvDate());
                    details.setSlsCustomerCode(arSalesOrderInvoiceLine.getArInvoice().getArCustomer().getCstCustomerCode());
                    details.setSlsCustomerBatch(arSalesOrderInvoiceLine.getArInvoice().getArCustomer().getCstCustomerBatch());
                    details.setSlsCustomerName(arSalesOrderInvoiceLine.getArInvoice().getArCustomer().getCstName());
                    details.setSlsDocumentNumber(arSalesOrderInvoiceLine.getArInvoice().getInvNumber());

                    list.add(details);
                }
            }

            // if include unserved sales orders

            if (criteria.get("includeUnservedSO").equals("YES")) {

                obj = new Object[criteriaSize];

                jbossQl = new StringBuffer();
                firstArgument = true;
                ctr = 0;

                jbossQl.append("SELECT OBJECT(sol) FROM ArSalesOrderLine sol");

                if (branchList.isEmpty()) {

                    throw new GlobalNoRecordFoundException();

                } else {

                    jbossQl.append(" WHERE sol.arSalesOrder.soAdBranch in (");

                    boolean firstLoop = true;

                    Iterator j = branchList.iterator();

                    while (j.hasNext()) {

                        if (firstLoop == false) {
                            jbossQl.append(", ");
                        } else {
                            firstLoop = false;
                        }

                        AdBranchDetails mdetails = (AdBranchDetails) j.next();

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

                    jbossQl.append("sol.invItemLocation.invItem.iiName=?" + (ctr + 1) + " ");
                    obj[ctr] = criteria.get("itemName");
                    ctr++;
                }

                if (criteria.containsKey("category")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("sol.invItemLocation.invItem.iiAdLvCategory=?" + (ctr + 1) + " ");
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

                    jbossQl.append("sol.invItemLocation.invLocation.locName=?" + (ctr + 1) + " ");
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

                    jbossQl.append("sol.arSalesOrder.soDate>=?" + (ctr + 1) + " ");
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

                    jbossQl.append("sol.arSalesOrder.soDate<=?" + (ctr + 1) + " ");
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

                    jbossQl.append("sol.invItemLocation.invItem.iiClass=?" + (ctr + 1) + " ");
                    obj[ctr] = criteria.get("itemClass");
                    ctr++;
                }

                if (criteria.containsKey("customerCode")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("sol.arSalesOrder.arCustomer.cstCustomerCode LIKE '%" + criteria.get("customerCode") + "%' ");
                }

                if (criteria.containsKey("customerBatch")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("sol.arSalesOrder.arCustomer.cstCustomerBatch LIKE '%" + criteria.get("customerBatch") + "%' ");
                }

                if (criteria.containsKey("salesperson")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("sol.arSalesOrder.arSalesperson.slpSalespersonCode LIKE '%" + criteria.get("salesperson") + "%' ");
                }

                if (criteria.containsKey("region")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("sol.arSalesOrder.arCustomer.cstAdLvRegion=?" + (ctr + 1) + " ");
                    obj[ctr] = criteria.get("region");
                    ctr++;
                }

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                if (criteria.get("includeUnposted").equals("YES")) {

                    jbossQl.append("sol.arSalesOrder.soVoid = 0 AND sol.solAdCompany=" + AD_CMPNY + " ");

                } else {

                    jbossQl.append("sol.arSalesOrder.soPosted = 1 AND sol.arSalesOrder.soVoid = 0 AND sol.solAdCompany=" + AD_CMPNY + " ");
                }

                Collection arSalesOrderLines = arSalesOrderLineHome.getSalesOrderLineByCriteria(jbossQl.toString(), obj, 0, 0);

                i = arSalesOrderLines.iterator();

                while (i.hasNext()) {

                    LocalArSalesOrderLine arSalesOrderLine = (LocalArSalesOrderLine) i.next();

                    if (!arSalesOrderLine.getArSalesOrderInvoiceLines().isEmpty()) {
                        continue;
                    }

                    ArRepSalesDetails details = new ArRepSalesDetails();

                    details.setSlsItemName(arSalesOrderLine.getInvItemLocation().getInvItem().getIiName());
                    details.setSlsItemDescription(arSalesOrderLine.getInvItemLocation().getInvItem().getIiDescription());
                    details.setSlsItemAdLvCategory(arSalesOrderLine.getInvItemLocation().getInvItem().getIiAdLvCategory());
                    details.setSlsUnit(arSalesOrderLine.getInvUnitOfMeasure().getUomName());
                    details.setSlsQuantitySold(0);
                    details.setSlsUnitPrice(arSalesOrderLine.getSolUnitPrice());
                    details.setSlsDefaultUnitPrice(arSalesOrderLine.getInvItemLocation().getInvItem().getIiUnitCost());

                    details.setSlsDate(arSalesOrderLine.getArSalesOrder().getSoDate());
                    details.setSlsCustomerCode(arSalesOrderLine.getArSalesOrder().getArCustomer().getCstCustomerCode());
                    details.setSlsCustomerBatch(arSalesOrderLine.getArSalesOrder().getArCustomer().getCstCustomerBatch());
                    details.setSlsCustomerName(arSalesOrderLine.getArSalesOrder().getArCustomer().getCstName());
                    details.setSlsCustomerAddress(arSalesOrderLine.getArSalesOrder().getArCustomer().getCstAddress());

                    if (arSalesOrderLine.getArSalesOrder().getArSalesperson() != null) {
                        details.setSlsSalespersonCode(arSalesOrderLine.getArSalesOrder().getArSalesperson().getSlpSalespersonCode());
                        details.setSlsSalespersonName(arSalesOrderLine.getArSalesOrder().getArSalesperson().getSlpName());
                    }
                    details.setSlsCustomerRegion(arSalesOrderLine.getArSalesOrder().getArCustomer().getCstAdLvRegion());
                    details.setSlsCustomerStateProvince(arSalesOrderLine.getArSalesOrder().getArCustomer().getCstStateProvince());

                    details.setSlsSalesOrderNumber(arSalesOrderLine.getArSalesOrder().getSoDocumentNumber());
                    details.setSlsSalesOrderDate(arSalesOrderLine.getArSalesOrder().getSoDate());
                    details.setSlsSalesOrderQuantity(arSalesOrderLine.getSolQuantity());
                    details.setSlsSalesOrderAmount(arSalesOrderLine.getSolAmount());
                    details.setSlsSalesOrderSalesPrice(arSalesOrderLine.getSolUnitPrice());
                    try {
                        details.setSlsReferenceNumber(arSalesOrderLine.getArSalesOrder().getSoReferenceNumber());
                    } catch (Exception ex) {
                        details.setSlsReferenceNumber("");
                    }
                    // Debug.print("7 : "+details.getSlsReferenceNumber());
                    list.add(details);
                }
            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            Collections.sort(list, ArRepSalesDetails.SubComparator);

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("ArRepSalesControllerBean getAdCompany");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());
            details.setCmpAddress(adCompany.getCmpAddress());
            details.setCmpCity(adCompany.getCmpCity());
            details.setCmpContact(adCompany.getCmpContact());
            details.setCmpCountry(adCompany.getCmpCountry());
            details.setCmpEmail(adCompany.getCmpEmail());
            details.setCmpTin(adCompany.getCmpTin());
            details.setCmpDescription(adCompany.getCmpDescription());
            details.setCmpPhone(adCompany.getCmpPhone());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ArRepSalesControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ArRepSalesControllerBean ejbCreate");
    }

}