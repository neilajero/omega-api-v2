package com.ejb.txnreports.ap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;


import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdLookUpValue;
import com.ejb.dao.ad.LocalAdLookUpValueHome;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ap.LocalApPurchaseOrderLine;
import com.ejb.dao.ap.LocalApPurchaseOrderLineHome;
import com.ejb.entities.ap.LocalApVoucherLineItem;
import com.ejb.dao.ap.LocalApVoucherLineItemHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.inv.LocalInvItem;
import com.ejb.entities.inv.LocalInvLocation;
import com.ejb.dao.inv.LocalInvLocationHome;
import com.ejb.entities.inv.LocalInvUnitOfMeasure;
import com.ejb.entities.inv.LocalInvUnitOfMeasureConversion;
import com.ejb.dao.inv.LocalInvUnitOfMeasureConversionHome;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ap.ApRepReceivingItemsDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ApRepReceivingItemsControllerEJB")
public class ApRepReceivingItemsControllerBean extends EJBContextClass implements ApRepReceivingItemsController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private LocalApPurchaseOrderLineHome apReceivingItemLineHome;
    @EJB
    private LocalApVoucherLineItemHome apVoucherLineItemHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;


    public ArrayList getAdLvInvItemCategoryAll(Integer AD_CMPNY) {

        Debug.print("ApRepReceivingItemsControllerBean getAdLvInvItemCategoryAll");

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

        Debug.print("ApRepReceivingItemsControllerBean getInvLocAll");

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

    public ArrayList executeApRepReceivingItems(HashMap criteria, String ORDER_BY, String GROUP_BY, ArrayList adBrnchList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepReceivingItemsControllerBean executeApRepReceivingItems");

        ArrayList apReceivingItemList = new ArrayList();

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            StringBuilder jbossQl = new StringBuilder();

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("itemName")) {

                criteriaSize--;
            }

            if (criteria.containsKey("supplierCode")) {

                criteriaSize--;
            }

            if (criteria.containsKey("includeUnposted")) {

                criteriaSize--;
            }

            if (criteria.containsKey("includedPoReceiving")) {

                criteriaSize--;
            }

            if (criteria.containsKey("includedVoucher")) {

                criteriaSize--;
            }

            if (criteria.containsKey("includedDirectCheck")) {

                criteriaSize--;
            }

            if (criteria.containsKey("poReceivingDocumentNumberFrom")) {

                criteriaSize--;
            }

            if (criteria.containsKey("poReceivingDocumentNumberTo")) {

                criteriaSize--;
            }

            if (criteria.containsKey("voucherDocumentNumberFrom")) {

                criteriaSize--;
            }

            if (criteria.containsKey("voucherDocumentNumberTo")) {

                criteriaSize--;
            }

            if (criteria.containsKey("checkDocumentNumberFrom")) {

                criteriaSize--;
            }

            if (criteria.containsKey("checkDocumentNumberTo")) {

                criteriaSize--;
            }

            // receiving items
            if (criteria.get("includedPoReceiving").equals("YES")) {

                int objSize = criteriaSize;

                if (criteria.containsKey("poReceivingDocumentNumberFrom")) {

                    objSize++;
                }

                if (criteria.containsKey("poReceivingDocumentNumberTo")) {

                    objSize++;
                }

                obj = new Object[objSize];

                jbossQl.append("SELECT OBJECT (pl) FROM ApPurchaseOrderLine pl ");

                if (criteria.containsKey("itemName")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("pl.invItemLocation.invItem.iiName LIKE '%").append(criteria.get("itemName")).append("%' ");
                }

                if (criteria.get("includeUnposted").equals("NO")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("pl.apPurchaseOrder.poPosted=1");
                }

                if (criteria.containsKey("supplierCode")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("pl.apPurchaseOrder.apSupplier.splSupplierCode LIKE '%").append(criteria.get("supplierCode")).append("%' ");
                }

                if (criteria.containsKey("category")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("pl.invItemLocation.invItem.iiAdLvCategory=?").append(ctr + 1).append(" ");
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

                    jbossQl.append("pl.invItemLocation.invLocation.locName=?").append(ctr + 1).append(" ");
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

                    jbossQl.append("pl.apPurchaseOrder.poDate>=?").append(ctr + 1).append(" ");
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

                    jbossQl.append("pl.apPurchaseOrder.poDate<=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateTo");
                    ctr++;
                }

                if (criteria.containsKey("poReceivingDocumentNumberFrom")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("pl.apPurchaseOrder.poDocumentNumber>=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("poReceivingDocumentNumberFrom");
                    ctr++;
                }

                if (criteria.containsKey("poReceivingDocumentNumberTo")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("pl.apPurchaseOrder.poDocumentNumber<=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("poReceivingDocumentNumberTo");
                    ctr++;
                }

                if (adBrnchList.isEmpty()) {

                    throw new GlobalNoRecordFoundException();

                } else {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("pl.apPurchaseOrder.poAdBranch in (");

                    boolean firstLoop = true;

                    for (Object o : adBrnchList) {

                        if (!firstLoop) {
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

                jbossQl.append("AND pl.apPurchaseOrder.poReceiving = 1 AND pl.plAdCompany=").append(AD_CMPNY).append(" ");

                String orderBy = null;

                switch (ORDER_BY) {
                    case "DATE":

                        orderBy = "pl.apPurchaseOrder.poDate";

                        break;
                    case "ITEM NAME":

                        orderBy = "pl.invItemLocation.invItem.iiName";

                        break;
                    case "ITEM DESCRIPTION":

                        orderBy = "pl.invItemLocation.invItem.iiDescription";

                        break;
                    case "DOCUMENT NUMBER":

                        orderBy = "pl.apPurchaseOrder.poDocumentNumber";

                        break;
                    case "LOCATION":

                        orderBy = "pl.invItemLocation.invLocation.locName";
                        break;
                }

                if (orderBy != null) {

                    jbossQl.append("ORDER BY ").append(orderBy);
                }

                Collection apReceivingItemLines = null;

                try {

                    apReceivingItemLines = apReceivingItemLineHome.getPolByCriteria(jbossQl.toString(), obj);

                } catch (FinderException ex) {

                }

                for (Object receivingItemLine : apReceivingItemLines) {

                    LocalApPurchaseOrderLine apReceivingItemLine = (LocalApPurchaseOrderLine) receivingItemLine;

                    ApRepReceivingItemsDetails details = new ApRepReceivingItemsDetails();

                    details.setRrDate(apReceivingItemLine.getApPurchaseOrder().getPoDate());
                    details.setRrItemName(apReceivingItemLine.getInvItemLocation().getInvItem().getIiName());
                    details.setRrItemDescription(apReceivingItemLine.getInvItemLocation().getInvItem().getIiDescription());
                    details.setRrLocation(apReceivingItemLine.getInvItemLocation().getInvLocation().getLocName());
                    details.setRrSupplierName(apReceivingItemLine.getApPurchaseOrder().getApSupplier().getSplName());
                    details.setRrDocNo(apReceivingItemLine.getApPurchaseOrder().getPoDocumentNumber());
                    details.setRrUnit(apReceivingItemLine.getInvUnitOfMeasure().getUomName());
                    details.setRrQuantity(apReceivingItemLine.getPlQuantity());
                    details.setRrUnitCost(apReceivingItemLine.getPlUnitCost());
                    details.setRrAmount(apReceivingItemLine.getPlAmount());
                    details.setRrQcNumber(apReceivingItemLine.getPlQcNumber());
                    details.setRrQcExpiryDate(apReceivingItemLine.getPlQcExpiryDate());
                    details.setOrderBy(ORDER_BY);
                    details.setRrRefNo(apReceivingItemLine.getApPurchaseOrder().getPoReferenceNumber());
                    details.setRrDesc(apReceivingItemLine.getApPurchaseOrder().getPoDescription());

                    apReceivingItemList.add(details);
                }
            }

            // voucher
            if (criteria.get("includedVoucher").equals("YES")) {

                int objSize = criteriaSize;

                if (criteria.containsKey("voucherDocumentNumberFrom")) {

                    objSize++;
                }

                if (criteria.containsKey("voucherDocumentNumberTo")) {

                    objSize++;
                }

                obj = new Object[objSize];

                jbossQl = new StringBuilder();
                firstArgument = true;

                ctr = 0;

                jbossQl.append("SELECT OBJECT (vli) FROM ApVoucherLineItem vli ");

                if (criteria.containsKey("itemName")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("vli.invItemLocation.invItem.iiName LIKE '%").append(criteria.get("itemName")).append("%' ");
                }

                if (criteria.get("includeUnposted").equals("NO")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("vli.apVoucher.vouPosted=1");
                }

                if (criteria.containsKey("supplierCode")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("vli.apVoucher.apSupplier.splSupplierCode LIKE '%").append(criteria.get("supplierCode")).append("%' ");
                }

                if (criteria.containsKey("category")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("vli.invItemLocation.invItem.iiAdLvCategory=?").append(ctr + 1).append(" ");
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

                    jbossQl.append("vli.invItemLocation.invLocation.locName=?").append(ctr + 1).append(" ");
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

                    jbossQl.append("vli.apVoucher.vouDate>=?").append(ctr + 1).append(" ");
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

                    jbossQl.append("vli.apVoucher.vouDate<=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateTo");
                    ctr++;
                }

                if (criteria.containsKey("voucherDocumentNumberFrom")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("vli.apVoucher.vouDocumentNumber>=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("voucherDocumentNumberFrom");
                    ctr++;
                }

                if (criteria.containsKey("voucherDocumentNumberTo")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("vli.apVoucher.vouDocumentNumber<=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("voucherDocumentNumberTo");
                    ctr++;
                }

                if (adBrnchList.isEmpty()) {

                    throw new GlobalNoRecordFoundException();

                } else {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("vli.apVoucher.vouAdBranch in (");

                    boolean firstLoop = true;

                    for (Object o : adBrnchList) {

                        if (!firstLoop) {
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

                jbossQl.append("AND vli.apVoucher.vouDebitMemo = 0 AND vli.vliAdCompany=").append(AD_CMPNY).append(" ");

                String orderBy = null;

                switch (ORDER_BY) {
                    case "DATE":

                        orderBy = "vli.apVoucher.vouDate";

                        break;
                    case "ITEM NAME":

                        orderBy = "vli.invItemLocation.invItem.iiName";

                        break;
                    case "ITEM DESCRIPTION":

                        orderBy = "vli.invItemLocation.invItem.iiDescription";

                        break;
                    case "DOCUMENT NUMBER":

                        orderBy = "vli.apVoucher.vouDocumentNumber";

                        break;
                    case "LOCATION":

                        orderBy = "vli.invItemLocation.invLocation.locName";
                        break;
                }

                if (orderBy != null) {

                    jbossQl.append("ORDER BY ").append(orderBy);
                }

                Collection apVoucherLineItems = null;

                try {

                    apVoucherLineItems = apVoucherLineItemHome.getVliByCriteria(jbossQl.toString(), obj);

                } catch (FinderException ex) {

                }

                for (Object voucherLineItem : apVoucherLineItems) {

                    LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) voucherLineItem;

                    ApRepReceivingItemsDetails details = new ApRepReceivingItemsDetails();

                    details.setRrDate(apVoucherLineItem.getApVoucher().getVouDate());
                    details.setRrItemName(apVoucherLineItem.getInvItemLocation().getInvItem().getIiName());
                    details.setRrItemDescription(apVoucherLineItem.getInvItemLocation().getInvItem().getIiDescription());
                    details.setRrLocation(apVoucherLineItem.getInvItemLocation().getInvLocation().getLocName());
                    details.setRrSupplierName(apVoucherLineItem.getApVoucher().getApSupplier().getSplName());
                    details.setRrDocNo(apVoucherLineItem.getApVoucher().getVouDocumentNumber());
                    details.setRrUnit(apVoucherLineItem.getInvUnitOfMeasure().getUomName());
                    details.setRrUnitCost(apVoucherLineItem.getVliUnitCost());
                    details.setRrAmount(apVoucherLineItem.getVliAmount());
                    details.setRrPosted(apVoucherLineItem.getApVoucher().getVouPosted());

                    details.setOrderBy(ORDER_BY);

                    double quantity = apVoucherLineItem.getVliQuantity();

                    // get debit memo
                    Collection apDebitMemoLineItems = apVoucherLineItemHome.findByVouDebitMemoAndVouPostedVouDmVoucherNumberAndIlCodeAndBrCode(EJBCommon.TRUE, EJBCommon.TRUE, apVoucherLineItem.getApVoucher().getVouDocumentNumber(), apVoucherLineItem.getInvItemLocation().getIlCode(), apVoucherLineItem.getApVoucher().getVouAdBranch(), AD_CMPNY);

                    for (Object debitMemoLineItem : apDebitMemoLineItems) {

                        LocalApVoucherLineItem apDebitMemoLineItem = (LocalApVoucherLineItem) debitMemoLineItem;

                        double convertedQty = apDebitMemoLineItem.getVliQuantity() * (apVoucherLineItem.getInvUnitOfMeasure().getUomConversionFactor() / apDebitMemoLineItem.getInvUnitOfMeasure().getUomConversionFactor());

                        quantity = quantity - convertedQty;
                    }

                    if (!criteria.get("includeUnposted").equals("NO")) {

                        apDebitMemoLineItems = apVoucherLineItemHome.findByVouDebitMemoAndVouPostedVouDmVoucherNumberAndIlCodeAndBrCode(EJBCommon.TRUE, EJBCommon.FALSE, apVoucherLineItem.getApVoucher().getVouDocumentNumber(), apVoucherLineItem.getInvItemLocation().getIlCode(), apVoucherLineItem.getApVoucher().getVouAdBranch(), AD_CMPNY);

                        for (Object debitMemoLineItem : apDebitMemoLineItems) {

                            LocalApVoucherLineItem apDebitMemoLineItem = (LocalApVoucherLineItem) debitMemoLineItem;

                            double convertedQty = this.convertByUomFromAndUomToAndInvItemAndQuantity(apDebitMemoLineItem.getInvUnitOfMeasure(), apVoucherLineItem.getInvUnitOfMeasure(), apVoucherLineItem.getInvItemLocation().getInvItem(), apDebitMemoLineItem.getVliQuantity(), AD_CMPNY);

                            quantity = quantity - convertedQty;
                        }
                    }

                    details.setRrQuantity(quantity);
                    details.setRrRefNo(apVoucherLineItem.getApVoucher().getVouReferenceNumber());
                    details.setRrDesc(apVoucherLineItem.getApVoucher().getVouDescription());

                    apReceivingItemList.add(details);
                }
            }

            // Check
            if (criteria.get("includedDirectCheck").equals("YES")) {

                int objSize = criteriaSize;

                if (criteria.containsKey("checkDocumentNumberFrom")) {

                    objSize++;
                }

                if (criteria.containsKey("checkDocumentNumberTo")) {

                    objSize++;
                }

                obj = new Object[objSize];

                jbossQl = new StringBuilder();
                firstArgument = true;

                ctr = 0;

                jbossQl.append("SELECT OBJECT (vli) FROM ApVoucherLineItem vli ");

                if (criteria.containsKey("itemName")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("vli.invItemLocation.invItem.iiName LIKE '%").append(criteria.get("itemName")).append("%' ");
                }

                if (criteria.get("includeUnposted").equals("NO")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("vli.apCheck.chkPosted=1");
                }

                if (criteria.containsKey("supplierCode")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("vli.apCheck.apSupplier.splSupplierCode LIKE '%").append(criteria.get("supplierCode")).append("%' ");
                }

                if (criteria.containsKey("category")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("vli.invItemLocation.invItem.iiAdLvCategory=?").append(ctr + 1).append(" ");
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

                    jbossQl.append("vli.invItemLocation.invLocation.locName=?").append(ctr + 1).append(" ");
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

                    jbossQl.append("vli.apCheck.chkDate>=?").append(ctr + 1).append(" ");
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

                    jbossQl.append("vli.apCheck.chkDate<=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateTo");
                    ctr++;
                }

                if (criteria.containsKey("checkDocumentNumberFrom")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("vli.apCheck.chkDocumentNumber>=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("checkDocumentNumberFrom");
                    ctr++;
                }

                if (criteria.containsKey("checkDocumentNumberTo")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("vli.apCheck.chkDocumentNumber<=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("checkDocumentNumberTo");
                    ctr++;
                }

                if (adBrnchList.isEmpty()) {

                    throw new GlobalNoRecordFoundException();

                } else {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("vli.apCheck.chkAdBranch in (");

                    boolean firstLoop = true;

                    for (Object o : adBrnchList) {

                        if (!firstLoop) {
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

                jbossQl.append("AND vli.apCheck.chkType = 'DIRECT' AND vli.vliAdCompany=").append(AD_CMPNY).append(" ");

                String orderBy = null;

                switch (ORDER_BY) {
                    case "DATE":

                        orderBy = "vli.apCheck.chkDate";

                        break;
                    case "ITEM NAME":

                        orderBy = "vli.invItemLocation.invItem.iiName";

                        break;
                    case "ITEM DESCRIPTION":

                        orderBy = "vli.invItemLocation.invItem.iiDescription";

                        break;
                    case "DOCUMENT NUMBER":

                        orderBy = "vli.apCheck.chkDocumentNumber";

                        break;
                    case "LOCATION":

                        orderBy = "vli.invItemLocation.invLocation.locName";
                        break;
                }

                if (orderBy != null) {

                    jbossQl.append("ORDER BY ").append(orderBy);
                }

                Collection apReceivingItemLines = null;

                try {

                    apReceivingItemLines = apVoucherLineItemHome.getVliByCriteria(jbossQl.toString(), obj);

                } catch (FinderException ex) {

                }

                for (Object apReceivingItemLine : apReceivingItemLines) {

                    LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) apReceivingItemLine;

                    ApRepReceivingItemsDetails details = new ApRepReceivingItemsDetails();

                    details.setRrDate(apVoucherLineItem.getApCheck().getChkDate());
                    details.setRrItemName(apVoucherLineItem.getInvItemLocation().getInvItem().getIiName());
                    details.setRrItemDescription(apVoucherLineItem.getInvItemLocation().getInvItem().getIiDescription());
                    details.setRrLocation(apVoucherLineItem.getInvItemLocation().getInvLocation().getLocName());
                    details.setRrSupplierName(apVoucherLineItem.getApCheck().getApSupplier().getSplName());
                    details.setRrDocNo(apVoucherLineItem.getApCheck().getChkDocumentNumber());
                    details.setRrUnit(apVoucherLineItem.getInvUnitOfMeasure().getUomName());
                    details.setRrQuantity(apVoucherLineItem.getVliQuantity());
                    details.setRrUnitCost(apVoucherLineItem.getVliUnitCost());
                    details.setRrAmount(apVoucherLineItem.getVliAmount());

                    details.setOrderBy(ORDER_BY);
                    details.setRrRefNo(apVoucherLineItem.getApCheck().getChkReferenceNumber());
                    details.setRrDesc(apVoucherLineItem.getApCheck().getChkDescription());

                    apReceivingItemList.add(details);
                }
            }

            // sort

            switch (GROUP_BY) {
                case "SUPPLIER CODE":

                    apReceivingItemList.sort(ApRepReceivingItemsDetails.SupplierNameComparator);

                    break;
                case "ITEM CODE":

                    apReceivingItemList.sort(ApRepReceivingItemsDetails.ItemNameComparator);

                    break;
                case "DATE":

                    apReceivingItemList.sort(ApRepReceivingItemsDetails.DateComparator);

                    break;
                default:

                    apReceivingItemList.sort(ApRepReceivingItemsDetails.NoGroupComparator);
                    break;
            }

            if (apReceivingItemList.isEmpty()) throw new GlobalNoRecordFoundException();

            return apReceivingItemList;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("ApRepReceivingItemsControllerBean getAdCompany");

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

    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepReceivingItemsControllerBean getAdBrResAll");

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

                adBranch = adBranchResponsibility.getAdBranch();

                AdBranchDetails details = new AdBranchDetails();

                details.setBrCode(adBranch.getBrCode());
                details.setBrBranchCode(adBranch.getBrBranchCode());
                details.setBrName(adBranch.getBrName());
                details.setBrHeadQuarter(adBranch.getBrHeadQuarter());

                list.add(details);
            }

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        return list;
    }

    private double convertByUomFromAndUomToAndInvItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvUnitOfMeasure invToUnitOfMeasure, LocalInvItem invItem, double QTY, Integer AD_CMPNY) {

        Debug.print("ApRepReceivingItemsControllerBean convertByUomFromAndUomToAndInvItemAndQuantity");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            LocalInvUnitOfMeasureConversion invFromUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), AD_CMPNY);
            LocalInvUnitOfMeasureConversion invToUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invToUnitOfMeasure.getUomName(), AD_CMPNY);

            return EJBCommon.roundIt(QTY * invToUnitOfMeasureConversion.getUmcConversionFactor() / invFromUnitOfMeasureConversion.getUmcConversionFactor(), adPreference.getPrfInvQuantityPrecisionUnit());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApRepReceivingItemsControllerBean ejbCreate");
    }
}