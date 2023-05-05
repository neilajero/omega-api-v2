/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArRepDeliveryReceiptControllerBean
 * @created
 * @author
 */
package com.ejb.txnreports.ar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;


import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.dao.ar.LocalArInvoiceHome;
import com.ejb.entities.ar.LocalArInvoiceLineItem;
import com.ejb.dao.ar.LocalArInvoiceLineItemHome;
import com.ejb.dao.ar.LocalArReceiptHome;
import com.ejb.dao.ar.LocalArSalesOrderHome;
import com.ejb.entities.ar.LocalArSalesOrderInvoiceLine;
import com.ejb.dao.ar.LocalArSalesOrderInvoiceLineHome;
import com.ejb.dao.ar.LocalArSalesOrderLineHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ar.ArRepDeliveryReceiptDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ArRepDeliveryReceiptControllerEJB")
public class ArRepDeliveryReceiptControllerBean extends EJBContextClass implements ArRepDeliveryReceiptController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalArInvoiceLineItemHome arInvoiceLineItemHome;
    @EJB
    private LocalArSalesOrderHome arSalesOrderHome;
    @EJB
    private LocalArSalesOrderInvoiceLineHome arSalesOrderInvoiceLineHome;
    @EJB
    private LocalArSalesOrderLineHome arSalesOrderLineHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;

    public ArrayList executeArRepDeliveryReceipt(HashMap criteria, String ORDER_BY, String GROUP_BY, boolean SHOW_LN_ITEMS, ArrayList adBrnchList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepDeliveryReceiptControllerBean executeArRepDeliveryReceipt");

        ArrayList list = new ArrayList();

        try {
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            String tempDocNum = null;
            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("includedUnposted")) {

                criteriaSize--;
            }

            if (criteria.containsKey("includedMiscReceipts")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(ili) FROM ArInvoiceLineItem ili ");

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("ili.arInvoice.invDate>=?").append(ctr + 1).append(" ");
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

                jbossQl.append("ili.arInvoice.invDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (criteria.get("includedUnposted").equals("NO")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("ili.arInvoice.invPosted = 1 ");
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

                jbossQl.append("ili.arInvoice.invAdBranch in (");

                boolean firstLoop = true;

                for (Object o : adBrnchList) {

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

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("ili.arInvoice.invAdCompany = ").append(AD_CMPNY).append(" ");

            String orderBy = null;

            if (ORDER_BY.equals("DATE")) {

                orderBy = "ili.arInvoice.invDate";

            } else if (ORDER_BY.equals("DOCUMENT NUMBER")) {

                orderBy = "ili.arInvoice.invNumber";
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy);
            }

            Collection arInvoiceLineItems = null;

            try {

                arInvoiceLineItems = arInvoiceLineItemHome.getIliByCriteria(jbossQl.toString(), obj);

            } catch (FinderException ex) {

            }

            Iterator i = arInvoiceLineItems.iterator();

            while (i.hasNext()) {

                LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) i.next();

                if (SHOW_LN_ITEMS) {

                    double AMOUNT = 0d;

                    ArRepDeliveryReceiptDetails details = new ArRepDeliveryReceiptDetails();

                    details.setDrCustomerName(arInvoiceLineItem.getArInvoice().getArCustomer().getCstName());
                    details.setDrCustomerCode(arInvoiceLineItem.getArInvoice().getArCustomer().getCstCustomerCode());
                    details.setDrDate(arInvoiceLineItem.getArInvoice().getInvDate());
                    details.setDrDocumentNumber(arInvoiceLineItem.getArInvoice().getInvNumber());
                    details.setDrReferenceNumber(arInvoiceLineItem.getArInvoice().getInvReferenceNumber());
                    details.setDrDescription(arInvoiceLineItem.getArInvoice().getInvDescription());

                    details.setDrItemName(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                    details.setDrLocation(arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName());
                    details.setDrQuantity(arInvoiceLineItem.getIliQuantity());
                    details.setDrUnit(arInvoiceLineItem.getInvUnitOfMeasure().getUomName());
                    details.setDrUnitPrice(arInvoiceLineItem.getIliUnitPrice());

                    AMOUNT = EJBCommon.roundIt(arInvoiceLineItem.getIliQuantity() * arInvoiceLineItem.getIliUnitPrice(), this.getGlFcPrecisionUnit(AD_CMPNY));

                    details.setDrAmount(AMOUNT);

                    details.setOrderBy(ORDER_BY);

                    list.add(details);

                } else {

                    if (tempDocNum == null || !tempDocNum.equals(arInvoiceLineItem.getArInvoice().getInvNumber())) {

                        tempDocNum = arInvoiceLineItem.getArInvoice().getInvNumber();

                        ArRepDeliveryReceiptDetails details = new ArRepDeliveryReceiptDetails();

                        details.setDrCustomerName(arInvoiceLineItem.getArInvoice().getArCustomer().getCstName());
                        details.setDrCustomerCode(arInvoiceLineItem.getArInvoice().getArCustomer().getCstCustomerCode());
                        details.setDrDate(arInvoiceLineItem.getArInvoice().getInvDate());
                        details.setDrDocumentNumber(arInvoiceLineItem.getArInvoice().getInvNumber());
                        details.setDrReferenceNumber(arInvoiceLineItem.getArInvoice().getInvReferenceNumber());
                        details.setDrDescription(arInvoiceLineItem.getArInvoice().getInvDescription());
                        details.setDrAmount(arInvoiceLineItem.getArInvoice().getInvAmountDue());
                        details.setOrderBy(ORDER_BY);

                        list.add(details);
                    }
                }
            }

            // Sales Order

            jbossQl = new StringBuilder();

            jbossQl.append("SELECT OBJECT(sil) FROM ArSalesOrderInvoiceLine sil ");

            firstArgument = true;
            ctr = 0;

            // Allocate the size of the object parameter

            obj = new Object[criteriaSize];

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("sil.arSalesOrderLine.arSalesOrder.soDate>=?").append(ctr + 1).append(" ");
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

                jbossQl.append("sil.arSalesOrderLine.arSalesOrder.soDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (criteria.get("includedUnposted").equals("NO")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("sil.arSalesOrderLine.arSalesOrder.soPosted = 1 ");
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

                jbossQl.append("sil.arSalesOrderLine.arSalesOrder.soAdBranch in (");

                boolean firstLoop = true;

                for (Object o : adBrnchList) {

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

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("sil.silAdCompany = ").append(AD_CMPNY).append(" ");

            orderBy = null;

            if (ORDER_BY.equals("DATE")) {

                orderBy = "sil.arSalesOrderLine.arSalesOrder.soDate";

            } else if (ORDER_BY.equals("DOCUMENT NUMBER")) {

                orderBy = "sil.arSalesOrderLine.arSalesOrder.soDocumentNumber";
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy);
            }

            Collection arSalesOrderInvoiceLines = null;

            try {

                arSalesOrderInvoiceLines = arSalesOrderInvoiceLineHome.getSalesOrderInvoiceLineByCriteria(jbossQl.toString(), obj);

            } catch (FinderException ex) {

            }

            i = arSalesOrderInvoiceLines.iterator();

            while (i.hasNext()) {

                LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) i.next();

                if (SHOW_LN_ITEMS) {

                    double AMOUNT = 0d;

                    ArRepDeliveryReceiptDetails details = new ArRepDeliveryReceiptDetails();

                    details.setDrCustomerName(arSalesOrderInvoiceLine.getArSalesOrderLine().getArSalesOrder().getArCustomer().getCstName());
                    details.setDrCustomerCode(arSalesOrderInvoiceLine.getArSalesOrderLine().getArSalesOrder().getArCustomer().getCstCustomerCode());
                    details.setDrDate(arSalesOrderInvoiceLine.getArSalesOrderLine().getArSalesOrder().getSoDate());
                    details.setDrDocumentNumber(arSalesOrderInvoiceLine.getArSalesOrderLine().getArSalesOrder().getSoDocumentNumber());
                    details.setDrReferenceNumber(arSalesOrderInvoiceLine.getArSalesOrderLine().getArSalesOrder().getSoReferenceNumber());
                    details.setDrDescription(arSalesOrderInvoiceLine.getArSalesOrderLine().getArSalesOrder().getSoDescription());

                    details.setDrItemName(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvItem().getIiName());
                    details.setDrLocation(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvLocation().getLocName());
                    details.setDrQuantity(arSalesOrderInvoiceLine.getSilQuantityDelivered());
                    details.setDrUnit(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvUnitOfMeasure().getUomName());
                    details.setDrUnitPrice(arSalesOrderInvoiceLine.getArSalesOrderLine().getSolUnitPrice());

                    AMOUNT = EJBCommon.roundIt(arSalesOrderInvoiceLine.getSilQuantityDelivered() * arSalesOrderInvoiceLine.getArSalesOrderLine().getSolUnitPrice(), this.getGlFcPrecisionUnit(AD_CMPNY));

                    details.setDrAmount(AMOUNT);
                    details.setOrderBy(ORDER_BY);

                    list.add(details);

                } else {

                    if (tempDocNum == null || !tempDocNum.equals(arSalesOrderInvoiceLine.getArSalesOrderLine().getArSalesOrder().getSoDocumentNumber())) {

                        tempDocNum = arSalesOrderInvoiceLine.getArSalesOrderLine().getArSalesOrder().getSoDocumentNumber();

                        ArRepDeliveryReceiptDetails details = new ArRepDeliveryReceiptDetails();

                        details.setDrCustomerName(arSalesOrderInvoiceLine.getArSalesOrderLine().getArSalesOrder().getArCustomer().getCstName());
                        details.setDrCustomerCode(arSalesOrderInvoiceLine.getArSalesOrderLine().getArSalesOrder().getArCustomer().getCstCustomerCode());
                        details.setDrDate(arSalesOrderInvoiceLine.getArSalesOrderLine().getArSalesOrder().getSoDate());
                        details.setDrDocumentNumber(arSalesOrderInvoiceLine.getArSalesOrderLine().getArSalesOrder().getSoDocumentNumber());
                        details.setDrReferenceNumber(arSalesOrderInvoiceLine.getArSalesOrderLine().getArSalesOrder().getSoReferenceNumber());
                        details.setDrDescription(arSalesOrderInvoiceLine.getArSalesOrderLine().getArSalesOrder().getSoDescription());
                        details.setDrAmount(arSalesOrderInvoiceLine.getArInvoice().getInvAmountDue());
                        details.setOrderBy(ORDER_BY);

                        list.add(details);
                    }
                }
            }

            if (criteria.get("includedMiscReceipts").equals("YES")) {

                // misc receipt

                jbossQl = new StringBuilder();
                firstArgument = true;
                ctr = 0;

                obj = new Object[criteriaSize];

                jbossQl.append("SELECT OBJECT(ili) FROM ArInvoiceLineItem ili ");

                if (criteria.containsKey("dateFrom")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("ili.arReceipt.rctDate>=?").append(ctr + 1).append(" ");
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

                    jbossQl.append("ili.arReceipt.rctDate<=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateTo");
                    ctr++;
                }

                if (criteria.get("includedUnposted").equals("NO")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("ili.arReceipt.rctPosted = 1 ");
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

                    jbossQl.append("ili.arReceipt.rctAdBranch in (");

                    boolean firstLoop = true;

                    i = adBrnchList.iterator();

                    while (i.hasNext()) {

                        if (firstLoop == false) {
                            jbossQl.append(", ");
                        } else {
                            firstLoop = false;
                        }

                        AdBranchDetails mdetails = (AdBranchDetails) i.next();

                        jbossQl.append(mdetails.getBrCode());
                    }

                    jbossQl.append(") ");

                    firstArgument = false;
                }

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("ili.arReceipt.rctVoid = 0 AND ili.arReceipt.rctAdCompany = ").append(AD_CMPNY).append(" ");

                orderBy = null;

                if (ORDER_BY.equals("DATE")) {

                    orderBy = "ili.arReceipt.rctDate";

                } else if (ORDER_BY.equals("DOCUMENT NUMBER")) {

                    orderBy = "ili.arReceipt.rctNumber";
                }

                if (orderBy != null) {

                    jbossQl.append("ORDER BY ").append(orderBy);
                }

                arInvoiceLineItems = null;

                try {

                    arInvoiceLineItems = arInvoiceLineItemHome.getIliByCriteria(jbossQl.toString(), obj);

                } catch (FinderException ex) {

                }

                i = arInvoiceLineItems.iterator();

                while (i.hasNext()) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) i.next();

                    if (SHOW_LN_ITEMS) {

                        double AMOUNT = 0d;

                        ArRepDeliveryReceiptDetails details = new ArRepDeliveryReceiptDetails();

                        details.setDrCustomerName(arInvoiceLineItem.getArReceipt().getArCustomer().getCstName());
                        details.setDrCustomerCode(arInvoiceLineItem.getArReceipt().getArCustomer().getCstCustomerCode());
                        details.setDrDate(arInvoiceLineItem.getArReceipt().getRctDate());
                        details.setDrDocumentNumber(arInvoiceLineItem.getArReceipt().getRctNumber());
                        details.setDrReferenceNumber(arInvoiceLineItem.getArReceipt().getRctReferenceNumber());
                        details.setDrDescription(arInvoiceLineItem.getArReceipt().getRctDescription());

                        details.setDrItemName(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                        details.setDrLocation(arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName());
                        details.setDrQuantity(arInvoiceLineItem.getIliQuantity());
                        details.setDrUnit(arInvoiceLineItem.getInvUnitOfMeasure().getUomName());
                        details.setDrUnitPrice(arInvoiceLineItem.getIliUnitPrice());

                        AMOUNT = EJBCommon.roundIt(arInvoiceLineItem.getIliQuantity() * arInvoiceLineItem.getIliUnitPrice(), this.getGlFcPrecisionUnit(AD_CMPNY));

                        details.setDrAmount(AMOUNT);
                        details.setOrderBy(ORDER_BY);

                        list.add(details);

                    } else {

                        if (tempDocNum == null || !tempDocNum.equals(arInvoiceLineItem.getArReceipt().getRctNumber())) {

                            tempDocNum = arInvoiceLineItem.getArReceipt().getRctNumber();

                            ArRepDeliveryReceiptDetails details = new ArRepDeliveryReceiptDetails();

                            details.setDrCustomerName(arInvoiceLineItem.getArReceipt().getArCustomer().getCstName());
                            details.setDrCustomerCode(arInvoiceLineItem.getArReceipt().getArCustomer().getCstCustomerCode());
                            details.setDrDate(arInvoiceLineItem.getArReceipt().getRctDate());
                            details.setDrDocumentNumber(arInvoiceLineItem.getArReceipt().getRctNumber());
                            details.setDrReferenceNumber(arInvoiceLineItem.getArReceipt().getRctReferenceNumber());
                            details.setDrDescription(arInvoiceLineItem.getArReceipt().getRctDescription());
                            details.setDrAmount(arInvoiceLineItem.getIliAmount());
                            details.setOrderBy(ORDER_BY);

                            list.add(details);
                        }
                    }
                }
            }

            if (list.isEmpty()) throw new GlobalNoRecordFoundException();

            // sort

            if (GROUP_BY.equals("ITEM NAME")) {

                list.sort(ArRepDeliveryReceiptDetails.ItemNameComparator);

            } else if (GROUP_BY.equals("CUSTOMER")) {

                list.sort(ArRepDeliveryReceiptDetails.CustomerComparator);

            } else {

                list.sort(ArRepDeliveryReceiptDetails.NoGroupComparator);
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

        Debug.print("ArRepDeliveryReceiptControllerBean getAdCompany");

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

        Debug.print("ArRepDeliveryReceiptControllerBean getAdBrResAll");

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

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ArRepDeliveryReceiptControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods
    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("ArRepDeliveryReceiptControllerBean convertForeignToFunctionalCurrency");

        LocalAdCompany adCompany = null;

        // get company and extended precision

        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // Convert to functional currency if necessary

        if (CONVERSION_RATE != 1 && CONVERSION_RATE != 0) {

            AMOUNT = AMOUNT / CONVERSION_RATE;
        }
        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ArRepDeliveryReceiptControllerBean ejbCreate");
    }
}