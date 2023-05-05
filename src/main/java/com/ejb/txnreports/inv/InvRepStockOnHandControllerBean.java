/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class InvRepStockOnHandControllerBean
 * @created
 * @author
 */
package com.ejb.txnreports.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.ad.LocalAdBranchItemLocationHome;
import com.ejb.dao.ap.LocalApPurchaseOrderLineHome;
import com.ejb.dao.ar.LocalArJobOrderInvoiceLineHome;
import com.ejb.dao.ar.LocalArJobOrderLineHome;
import com.ejb.dao.ar.LocalArSalesOrderInvoiceLineHome;
import com.ejb.dao.ar.LocalArSalesOrderLineHome;
import com.ejb.dao.inv.LocalInvCostingHome;
import com.ejb.dao.inv.LocalInvUnitOfMeasureConversionHome;
import com.ejb.dao.inv.LocalInvUnitOfMeasureHome;
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
import com.util.reports.inv.InvRepStockOnHandDetails;

import jakarta.ejb.*;
import java.util.*;
import java.util.stream.Collectors;

@Stateless(name = "InvRepStockOnHandControllerEJB")
public class InvRepStockOnHandControllerBean extends EJBContextClass implements InvRepStockOnHandController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalAdBranchItemLocationHome invStockOnHandHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalApPurchaseOrderLineHome apPurchaseOrderLineHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private LocalArSalesOrderLineHome arSalesOrderLineHome;
    @EJB
    private LocalArSalesOrderInvoiceLineHome arSalesOrderInvoiceLineHome;
    @EJB
    private LocalArJobOrderLineHome arJobOrderLineHome;
    @EJB
    private LocalArJobOrderInvoiceLineHome arJobOrderInvoiceLineHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;

    public ArrayList executeInvRepStockOnHand(HashMap criteria, boolean INCLD_ZRS, String ORDER_BY, boolean SHW_CMMTTD_QNTTY, boolean INCLD_UNPSTD, boolean INCLD_FRCST, boolean LAYERED, Date AS_OF_DT, String UOM_NM, Integer AD_BRNCH, ArrayList branchList, Integer AD_CMPNY, String rpt) throws GlobalNoRecordFoundException {

        Debug.print("InvRepStockOnHandControllerBean executeInvRepStockOnHand");
        ArrayList invStockOnHandList = new ArrayList();
        try {
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            LocalInvUnitOfMeasure invUnitOfMeasure = null;
            if (EJBCommon.validateRequired(UOM_NM)) {
                try {
                    invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(UOM_NM, AD_CMPNY);
                } catch (FinderException ex) {
                }
            }

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(bil) FROM AdBranchItemLocation bil ");

            boolean firstArgument;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

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
            if (criteria.containsKey("itemNameTo")) {
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
                jbossQl.append("bil.invItemLocation.invItem.iiName  >= '").append(criteria.get("itemName")).append("' ");
            }

            if (criteria.containsKey("itemNameTo")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("bil.invItemLocation.invItem.iiName  <= '").append(criteria.get("itemNameTo")).append("' ");
                Debug.print("Controller : itemNameTo ");
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

            String ReportType = "";
            ReportType = rpt;

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("bil.invItemLocation.invItem.iiNonInventoriable=0 AND bil.bilAdCompany=").append(AD_CMPNY).append(" ");

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
                case "ITEM CATEGORY":
                    orderBy = "bil.invItemLocation.invItem.iiAdLvCategory";
                    break;
            }

            if (ReportType.equals("SRP") || ReportType.equals("SKU") || ReportType.equals("Quantity")) {
                orderBy = "bil.invItemLocation.invItem.iiAdLvCategory";
            }
            if (orderBy != null) {
                jbossQl.append("ORDER BY ").append(orderBy);
            }

            Collection invStockOnHands;
            try {
                invStockOnHands = invStockOnHandHome.getBilByCriteria(jbossQl.toString(), obj);
            } catch (FinderException ex) {
                throw new GlobalNoRecordFoundException();
            }

            if (invStockOnHands.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }

            Iterator i = invStockOnHands.iterator();
            while (i.hasNext()) {
                LocalAdBranchItemLocation invStockOnHand = (LocalAdBranchItemLocation) i.next();
                try {
                    if (LAYERED) {
                        double negated = 0d;
                        Collection PositiveValue = invCostingHome.findByCstDateAndIiNameAndLocName2(AS_OF_DT, invStockOnHand.getInvItemLocation().getInvItem().getIiName(), invStockOnHand.getInvItemLocation().getInvLocation().getLocName(), invStockOnHand.getAdBranch().getBrCode(), AD_CMPNY);

                        Collection NegativeValue = invCostingHome.findByCstDateAndIiNameAndLocName3(AS_OF_DT, invStockOnHand.getInvItemLocation().getInvItem().getIiName(), invStockOnHand.getInvItemLocation().getInvLocation().getLocName(), invStockOnHand.getAdBranch().getBrCode(), AD_CMPNY);

                        for (Object value : NegativeValue) {
                            LocalInvCosting nega = (LocalInvCosting) value;
                            negated += nega.getCstAdjustQuantity();
                        }

                        negated = negated * -1;
                        double remain = 0d;
                        for (Object o : PositiveValue) {
                            LocalInvCosting pos = (LocalInvCosting) o;
                            remain = pos.getCstAdjustQuantity();
                            if (remain >= negated) {
                                remain -= negated;
                                negated = 0;
                            } else {
                                negated = negated - remain;
                                remain = 0;
                            }

                            if (negated == 0 && remain > 0) {
                                InvRepStockOnHandDetails details = new InvRepStockOnHandDetails();
                                details.setShItemName(invStockOnHand.getInvItemLocation().getInvItem().getIiName());
                                details.setShItemDescription(invStockOnHand.getInvItemLocation().getInvItem().getIiDescription());
                                details.setShItemClass(invStockOnHand.getInvItemLocation().getInvItem().getIiClass());
                                details.setShLocation(invStockOnHand.getInvItemLocation().getInvLocation().getLocName());
                                details.setShUnit(invStockOnHand.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName());
                                details.setShUnitCost(invStockOnHand.getInvItemLocation().getInvItem().getIiUnitCost());
                                details.setShPartNumber(invStockOnHand.getInvItemLocation().getInvItem().getIiPartNumber());
                                details.setShCategoryName(invStockOnHand.getInvItemLocation().getInvItem().getIiAdLvCategory());
                                details.setShBranchCode(invStockOnHand.getAdBranch().getBrBranchCode());

                                details.setShQuantity(remain);
                                double costOfRemain = pos.getCstAdjustCost() / pos.getCstAdjustQuantity();

                                details.setShValue(remain * costOfRemain);
                                details.setShAverageCost(costOfRemain);
                                details.setShUnitCost(costOfRemain);
                                details.setShUnservedPo(0);
                                details.setShForecastQuantity(0);

                                invStockOnHandList.add(details);
                            }
                        }

                    } else {
                        // NON LAYERED
                        InvRepStockOnHandDetails details = new InvRepStockOnHandDetails();

                        // SET ALL VALUES IN DETAILS
                        details.setShItemName(invStockOnHand.getInvItemLocation().getInvItem().getIiName());
                        details.setShItemCategory(invStockOnHand.getInvItemLocation().getInvItem().getIiAdLvCategory());
                        details.setShItemDescription(invStockOnHand.getInvItemLocation().getInvItem().getIiDescription());
                        details.setShItemClass(invStockOnHand.getInvItemLocation().getInvItem().getIiClass());
                        details.setShLocation(invStockOnHand.getInvItemLocation().getInvLocation().getLocName());
                        details.setShUnit(invStockOnHand.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName());
                        details.setShUnitCost(invStockOnHand.getInvItemLocation().getInvItem().getIiUnitCost());
                        details.setShSalesPrice(invStockOnHand.getInvItemLocation().getInvItem().getIiSalesPrice());
                        details.setShPartNumber(invStockOnHand.getInvItemLocation().getInvItem().getIiPartNumber());
                        details.setShCategoryName(invStockOnHand.getInvItemLocation().getInvItem().getIiAdLvCategory());
                        details.setShBranchCode(invStockOnHand.getAdBranch().getBrBranchCode());
                        details.setShQuantity(0);
                        details.setShValue(0);
                        details.setShAverageCost(0);
                        details.setShUnservedPo(0);
                        details.setShForecastQuantity(0);

                        // GET COMMITED QTY
                        double totalCommitedQuantity = 0;
                        if (SHW_CMMTTD_QNTTY) {

                            // details.setShCommittedQuantity(getIlCommittedQuantity(invStockOnHand, AD_CMPNY));
                            double solQty = 0d;
                            double jolQty = 0d;
                            double deliveredQty = 0d;
                            double committedQty = 0d;
                            GregorianCalendar gcDateFrom = new GregorianCalendar();
                            gcDateFrom.setTime(AS_OF_DT);
                            gcDateFrom.add(Calendar.MONTH, -1);

                            Collection arSalesOrderLines = arSalesOrderLineHome.findCommittedQtyByIiNameAndLocNameAndSoAdBranch(invStockOnHand.getInvItemLocation().getInvItem().getIiName(), invStockOnHand.getInvItemLocation().getInvLocation().getLocName(), gcDateFrom.getTime(), AS_OF_DT, invStockOnHand.getAdBranch().getBrCode(), AD_CMPNY);

                            for (Object salesOrderLine : arSalesOrderLines) {
                                LocalArSalesOrderLine arSalesOrderLine = (LocalArSalesOrderLine) salesOrderLine;
                                if (arSalesOrderLine.getArSalesOrder().getSoOrderStatus().equals("Bad")) {
                                    continue;
                                }
                                committedQty += (arSalesOrderLine.getArSalesOrderInvoiceLines().size() == 0 ? arSalesOrderLine.getSolQuantity() : 0);
                            }

                            Collection arSalesOrderInvoiceLines = arSalesOrderInvoiceLineHome.findCommittedQtyByIiNameAndLocNameAndSoAdBranch(invStockOnHand.getInvItemLocation().getInvItem().getIiName(), invStockOnHand.getInvItemLocation().getInvLocation().getLocName(), gcDateFrom.getTime(), AS_OF_DT, invStockOnHand.getAdBranch().getBrCode(), AD_CMPNY);

                            for (Object salesOrderInvoiceLine : arSalesOrderInvoiceLines) {
                                LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) salesOrderInvoiceLine;
                                solQty = arSalesOrderInvoiceLine.getArSalesOrderLine().getSolQuantity();

                                deliveredQty = arSalesOrderInvoiceLine.getSilQuantityDelivered();
                                committedQty += (solQty - deliveredQty);
                            }

                            Collection arJobOrderLines = arJobOrderLineHome.findCommittedQtyByIiNameAndLocNameAndJoAdBranch(invStockOnHand.getInvItemLocation().getInvItem().getIiName(), invStockOnHand.getInvItemLocation().getInvLocation().getLocName(), gcDateFrom.getTime(), AS_OF_DT, invStockOnHand.getAdBranch().getBrCode(), AD_CMPNY);

                            for (Object jobOrderLine : arJobOrderLines) {
                                LocalArJobOrderLine arJobOrderLine = (LocalArJobOrderLine) jobOrderLine;
                                if (arJobOrderLine.getArJobOrder().getJoJobOrderStatus().equals("Bad")) {
                                    continue;
                                }
                                committedQty += (arJobOrderLine.getArJobOrderInvoiceLines().size() == 0 ? arJobOrderLine.getJolQuantity() : 0);
                            }

                            Collection arJobOrderInvoiceLines = arJobOrderInvoiceLineHome.findCommittedQtyByIiNameAndLocNameAndJoAdBranch(invStockOnHand.getInvItemLocation().getInvItem().getIiName(), invStockOnHand.getInvItemLocation().getInvLocation().getLocName(), gcDateFrom.getTime(), AS_OF_DT, invStockOnHand.getAdBranch().getBrCode(), AD_CMPNY);

                            for (Object jobOrderInvoiceLine : arJobOrderInvoiceLines) {
                                LocalArJobOrderInvoiceLine arJobOrderInvoiceLine = (LocalArJobOrderInvoiceLine) jobOrderInvoiceLine;
                                jolQty = arJobOrderInvoiceLine.getArJobOrderLine().getJolQuantity();
                                deliveredQty = arJobOrderInvoiceLine.getJilQuantityDelivered();
                                committedQty += (jolQty - deliveredQty);
                            }
                            details.setShCommittedQuantity(committedQty);
                            totalCommitedQuantity = committedQty;

                        }

                        // GET UNPOSTED QTY
                        double totalUnpostedQuantity = 0;
                        if (INCLD_UNPSTD) {
                            double unpostedQuantity = this.getShUnpostedQuantity(invStockOnHand, AS_OF_DT, AD_CMPNY);
                            details.setShUnpostedQuantity(unpostedQuantity);
                            totalUnpostedQuantity = unpostedQuantity;
                        }

                        double netQty = totalUnpostedQuantity - totalCommitedQuantity;

                        // get last PO date
                        try {
                            LocalApPurchaseOrderLine apPurchaseOrderLine = apPurchaseOrderLineHome.getByMaxPoDateAndMaxPlCodeAndIiNameAndLocNameAndPoReceivingAndPoPostedAndLessThanEqualPoDate(details.getShItemName(), details.getShLocation(), EJBCommon.FALSE, EJBCommon.TRUE, AS_OF_DT, AD_BRNCH, AD_CMPNY);
                            details.setShLastPoDate(EJBCommon.convertSQLDateToString(apPurchaseOrderLine.getApPurchaseOrder().getPoDate()));
                        } catch (FinderException ex) {
                        }

                        // get unserved PO qty
                        Iterator iter = invStockOnHand.getInvItemLocation().getApPurchaseOrderLines().iterator();

                        if (iter.hasNext()) {
                            LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) iter.next();
                            if (apPurchaseOrderLine.getApPurchaseOrder().getPoReceiving() == EJBCommon.FALSE && apPurchaseOrderLine.getApPurchaseOrder().getPoVoid() == EJBCommon.FALSE && apPurchaseOrderLine.getApPurchaseOrder().getPoDate().compareTo(AS_OF_DT) <= 0) {
                                details.setShUnservedPo(details.getShUnservedPo() + apPurchaseOrderLine.getPlQuantity());
                            }
                        }

                        //check if commit qty and unpposted qty is present
                        if (netQty <= 0 && !INCLD_ZRS && invStockOnHand.getInvItemLocation().getInvCostings().size() <= 0) {
                            continue;
                        }

                        if (netQty > 0) {
                            if (invStockOnHand.getInvItemLocation().getInvCostings().size() <= 0) {
                                invStockOnHandList.add(details);
                                continue;
                            }
                        }

                        List<LocalInvCosting> invCostings = new ArrayList(invStockOnHand.getInvItemLocation().getInvCostings());
                        try {
                            LocalInvCosting invCosting = invCostings.stream().filter(c -> c.getCstRemainingQuantity() > 0 || c.getCstAdjustQuantity() > 0).collect(Collectors.toList()).get(0);
                            Calendar startCalendar = new GregorianCalendar();
                            startCalendar.setTime(invCosting.getCstDate());
                            Calendar endCalendar = new GregorianCalendar();
                            endCalendar.setTime(AS_OF_DT);
                            int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
                            int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
                            details.setShAgedInMonth(diffMonth);
                        } catch (Exception ex) {
                        }

                        try {
                            LocalInvCosting invCosting = invCostings.stream().filter(c -> c.getCstDate().compareTo(AS_OF_DT) <= 0).sorted(Comparator.comparing(LocalInvCosting::getCstDate).thenComparing(LocalInvCosting::getCstLineNumber).reversed()).collect(Collectors.toList()).get(0);

                            if (invUnitOfMeasure != null && invUnitOfMeasure.getUomAdLvClass().equals(invStockOnHand.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomAdLvClass())) {
                                details.setShQuantity(this.convertQuantityByUomToAndItem(invUnitOfMeasure, invStockOnHand.getInvItemLocation().getInvItem(), invCosting.getCstRemainingQuantity(), AD_CMPNY));
                                details.setShValue(invCosting.getCstRemainingValue());
                                details.setShAverageCost(details.getShQuantity() == 0 ? 0 : EJBCommon.roundIt(details.getShValue() / details.getShQuantity(), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                                details.setShUnit(UOM_NM);
                                details.setShUnitCost(details.getShAverageCost());
                            } else {
                                details.setShQuantity(invCosting.getCstRemainingQuantity());
                                details.setShValue(invCosting.getCstRemainingValue());
                                details.setShAverageCost(invCosting.getCstRemainingQuantity() == 0 ? 0 : EJBCommon.roundIt(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity(), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                                details.setShUnitCost(details.getShAverageCost());
                            }

                            if (invCosting.getCstExpiryDate() != null || invCosting.getCstExpiryDate() != "") {
                                details.setShMisc(invCosting.getCstExpiryDate());
                            } else {
                                details.setShMisc("");
                            }

                        } catch (Exception ex) {
                        }

                        // check if item is for reorder
                        details.setShItemForReorder(invStockOnHand.getBilReorderPoint() > 0 && invStockOnHand.getBilReorderPoint() >= details.getShQuantity());
                        details.setShCommittedQuantity(0);
                        netQty += details.getShQuantity();
                        details.setShOrderBy(ORDER_BY);

                        if ((netQty > 0) || (INCLD_ZRS && (netQty <= 0))) {
                            // details.setShUnpostedQuantity(unpostedQuantity);
                            invStockOnHandList.add(details);
                        }
                    }
                } catch (FinderException ex) {
                }
            }

            invStockOnHandList.sort(InvRepStockOnHandDetails.NoGroupComparator);
            if (invStockOnHandList.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            } else {
                return invStockOnHandList;
            }
        } catch (GlobalNoRecordFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("InvRepStockOnHandControllerBean getAdCompany");
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

    public ArrayList getInvUomAll(Integer AD_CMPNY) {

        Debug.print("InvRepStockOnHandControllerBean getInvUomAll");
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

    private double getShUnpostedQuantity(LocalAdBranchItemLocation adBranchItemLocation, Date AS_OF_DT, Integer AD_CMPNY) {

        Debug.print("InvRepStockOnHandControllerBean getUnpostedQuantity");
        double totalUnpostedQuantity = 0d;
        try {

            Iterator iter;

            double unpostedQuantity = 0d;

            String itemName = adBranchItemLocation.getInvItemLocation().getInvItem().getIiName();
            String locationName = adBranchItemLocation.getInvItemLocation().getInvLocation().getLocName();
            Integer locationCode = adBranchItemLocation.getInvItemLocation().getInvLocation().getLocCode();
            Integer branchCode = adBranchItemLocation.getAdBranch().getBrCode();

            // VOUCHER LINES - AP VOUCHER, AP CHECK
            byte debitMemo;
            String documentNumber = "";
            try {
                iter = adBranchItemLocation.getInvItemLocation().getApVoucherLineItems().iterator();
                while (iter.hasNext()) {
                    LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) iter.next();

                    if (apVoucherLineItem.getApVoucher() != null) {
                        documentNumber = apVoucherLineItem.getApVoucher().getVouDocumentNumber();
                        if (apVoucherLineItem.getApVoucher().getVouPosted() == EJBCommon.FALSE && apVoucherLineItem.getApVoucher().getVouVoid() == EJBCommon.FALSE && apVoucherLineItem.getApVoucher().getVouDate().compareTo(AS_OF_DT) <= 0) {
                            unpostedQuantity = apVoucherLineItem.getVliQuantity();
                            if (apVoucherLineItem.getApVoucher().getVouDebitMemo() == EJBCommon.FALSE) {
                                totalUnpostedQuantity += unpostedQuantity;
                            } else {
                                totalUnpostedQuantity -= unpostedQuantity;
                            }
                        }

                    } else if (apVoucherLineItem.getApCheck() != null) {
                        documentNumber = apVoucherLineItem.getApCheck().getChkDocumentNumber();
                        if (apVoucherLineItem.getApVoucher().getVouPosted() == EJBCommon.FALSE && apVoucherLineItem.getApVoucher().getVouVoid() == EJBCommon.FALSE && apVoucherLineItem.getApVoucher().getVouDate().compareTo(AS_OF_DT) <= 0) {
                            unpostedQuantity = apVoucherLineItem.getVliQuantity();
                            totalUnpostedQuantity += unpostedQuantity;

                        }

                    }
                }

            } catch (Exception ex) {

                Debug.print("Voucher number: " + documentNumber + " not exist");
            }

            // PO LINES - AP RECEIVING ITEM
            try {
                iter = adBranchItemLocation.getInvItemLocation().getApPurchaseOrderLines().iterator();
                while (iter.hasNext()) {
                    LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) iter.next();

                    if (apPurchaseOrderLine.getApPurchaseOrder() != null) {
                        documentNumber = apPurchaseOrderLine.getApPurchaseOrder().getPoDocumentNumber();
                        if (apPurchaseOrderLine.getApPurchaseOrder().getPoReceiving() == EJBCommon.TRUE && apPurchaseOrderLine.getApPurchaseOrder().getPoPosted() == EJBCommon.FALSE && apPurchaseOrderLine.getApPurchaseOrder().getPoVoid() == EJBCommon.FALSE && apPurchaseOrderLine.getApPurchaseOrder().getPoDate().compareTo(AS_OF_DT) <= 0) {

                            totalUnpostedQuantity += unpostedQuantity;
                        }
                    }

                }

            } catch (Exception ex) {

                Debug.print("Po number: " + documentNumber + " not exist");
            }

            // AR INVOICE LINE ITEM   AR INVOICE
            try {
                iter = adBranchItemLocation.getInvItemLocation().getArInvoiceLineItems().iterator();
                while (iter.hasNext()) {
                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) iter.next();

                    if (arInvoiceLineItem.getArInvoice() != null) {
                        documentNumber = arInvoiceLineItem.getArInvoice().getInvNumber();
                        if (arInvoiceLineItem.getArInvoice().getInvPosted() == EJBCommon.FALSE && arInvoiceLineItem.getArInvoice().getInvVoid() == EJBCommon.FALSE && arInvoiceLineItem.getArInvoice().getInvDate().compareTo(AS_OF_DT) <= 0) {
                            unpostedQuantity = arInvoiceLineItem.getIliQuantity();
                            if (arInvoiceLineItem.getArInvoice().getInvDebitMemo() == EJBCommon.FALSE) {
                                totalUnpostedQuantity += unpostedQuantity;
                            } else {
                                totalUnpostedQuantity -= unpostedQuantity;
                            }
                        }

                    } else if (arInvoiceLineItem.getArReceipt() != null) {
                        documentNumber = arInvoiceLineItem.getArReceipt().getRctNumber();
                        if (arInvoiceLineItem.getArReceipt().getRctPosted() == EJBCommon.FALSE && arInvoiceLineItem.getArReceipt().getRctVoid() == EJBCommon.FALSE && arInvoiceLineItem.getArReceipt().getRctDate().compareTo(AS_OF_DT) <= 0) {
                            unpostedQuantity = arInvoiceLineItem.getIliQuantity();
                            totalUnpostedQuantity += unpostedQuantity;

                        }

                    }
                }

            } catch (Exception ex) {

                Debug.print("Invoice/Direct check number: " + documentNumber + " not exist");
            }


            // AR SALES ORDER INVOICE LINE
            try {
                iter = adBranchItemLocation.getInvItemLocation().getArSalesOrderLines().iterator();
                while (iter.hasNext()) {
                    LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) iter.next();

                    if (arSalesOrderInvoiceLine.getArInvoice() != null) {
                        documentNumber = arSalesOrderInvoiceLine.getArInvoice().getInvNumber();
                        if (arSalesOrderInvoiceLine.getArInvoice().getInvPosted() == EJBCommon.FALSE && arSalesOrderInvoiceLine.getArInvoice().getInvVoid() == EJBCommon.FALSE && arSalesOrderInvoiceLine.getArInvoice().getInvDate().compareTo(AS_OF_DT) <= 0) {
                            unpostedQuantity = arSalesOrderInvoiceLine.getSilQuantityDelivered();
                            totalUnpostedQuantity += unpostedQuantity;

                        }

                    }
                }

            } catch (Exception ex) {

                Debug.print("Invoice/Direct check number: " + documentNumber + " not exist");
            }


            // AR JOB ORDER INVOICE LINE
            try {
                iter = adBranchItemLocation.getInvItemLocation().getArJobOrderLines().iterator();
                while (iter.hasNext()) {
                    LocalArJobOrderInvoiceLine arJobOrderInvoiceLine = (LocalArJobOrderInvoiceLine) iter.next();

                    if (arJobOrderInvoiceLine.getArInvoice() != null) {
                        documentNumber = arJobOrderInvoiceLine.getArInvoice().getInvNumber();
                        if (arJobOrderInvoiceLine.getArInvoice().getInvPosted() == EJBCommon.FALSE && arJobOrderInvoiceLine.getArInvoice().getInvVoid() == EJBCommon.FALSE && arJobOrderInvoiceLine.getArInvoice().getInvDate().compareTo(AS_OF_DT) <= 0) {
                            unpostedQuantity = arJobOrderInvoiceLine.getJilQuantityDelivered();
                            totalUnpostedQuantity += unpostedQuantity;

                        }

                    }
                }

            } catch (Exception ex) {

                Debug.print("Job Order check number: " + documentNumber + " not exist");
            }

            // INV ADJUSTMENT LINE

            try {
                iter = adBranchItemLocation.getInvItemLocation().getInvAdjustmentLines().iterator();
                while (iter.hasNext()) {
                    LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) iter.next();

                    if (invAdjustmentLine.getInvAdjustment() != null) {
                        if (invAdjustmentLine.getInvAdjustment().getAdjPosted() == EJBCommon.FALSE && invAdjustmentLine.getInvAdjustment().getAdjVoid() == EJBCommon.FALSE && invAdjustmentLine.getInvAdjustment().getAdjDate().compareTo(AS_OF_DT) <= 0) {

                            unpostedQuantity = invAdjustmentLine.getAlAdjustQuantity();


                            totalUnpostedQuantity += unpostedQuantity;
                        }

                    }
                }

            } catch (Exception ex) {

                Debug.print("Inv Adjustment number: " + documentNumber + " not exist");
            }

            // INV STOCK TRANSFER LINE
            try {
                iter = adBranchItemLocation.getInvItemLocation().getInvItem().getInvStockTransferLines().iterator();
                while (iter.hasNext()) {
                    LocalInvStockTransferLine invStockTransferLine = (LocalInvStockTransferLine) iter.next();

                    if (invStockTransferLine.getInvStockTransfer() != null) {
                        documentNumber = invStockTransferLine.getInvStockTransfer().getStDocumentNumber();
                        if (invStockTransferLine.getInvStockTransfer().getStPosted() == EJBCommon.FALSE && invStockTransferLine.getInvStockTransfer().getStDate().compareTo(AS_OF_DT) <= 0) {
                            unpostedQuantity = invStockTransferLine.getStlQuantityDelivered();


                            if (invStockTransferLine.getStlLocationTo().equals(locationCode)) {

                                totalUnpostedQuantity += unpostedQuantity;

                            } else if (invStockTransferLine.getStlLocationFrom().equals(locationCode)) {

                                totalUnpostedQuantity -= unpostedQuantity;

                            }

                        }

                    }

                }

            } catch (Exception ex) {

                Debug.print("Stock Transfer number: " + documentNumber + " not exist");
            }


            // INV BRANCH STOCK TRANSFER LINE
            try {
                iter = adBranchItemLocation.getInvItemLocation().getInvBranchStockTransferLines().iterator();
                while (iter.hasNext()) {
                    LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) iter.next();

                    if (invBranchStockTransferLine.getInvBranchStockTransfer() != null) {
                        documentNumber = invBranchStockTransferLine.getInvBranchStockTransfer().getBstNumber();
                        if (invBranchStockTransferLine.getInvBranchStockTransfer().getBstPosted() == EJBCommon.FALSE && invBranchStockTransferLine.getInvBranchStockTransfer().getBstVoid() == EJBCommon.FALSE && invBranchStockTransferLine.getInvBranchStockTransfer().getBstDate().compareTo(AS_OF_DT) <= 0) {

                            unpostedQuantity = invBranchStockTransferLine.getBslQuantity();

                            String bstType = invBranchStockTransferLine.getInvBranchStockTransfer().getBstType();

                            if (bstType.equals("IN")) {
                                totalUnpostedQuantity += unpostedQuantity;
                            } else if (bstType.equals("OUT")) {
                                totalUnpostedQuantity -= unpostedQuantity;
                            }

                        }

                    }

                }

            } catch (Exception ex) {

                Debug.print("Branch stock transfer number: " + documentNumber + " not exist");
            }

        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
        return totalUnpostedQuantity;
    }

    private double convertQuantityByUomToAndItem(LocalInvUnitOfMeasure invToUnitOfMeasure, LocalInvItem invItem, double QTY, Integer AD_CMPNY) {

        Debug.print("InvRepStockOnHandControllerBean convertQuantityByUomToAndItem");
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

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("InvRepStockOnHandControllerBean ejbCreate");
    }

}