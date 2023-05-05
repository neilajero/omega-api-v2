/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArRepSalesOrderControllerBean
 * @created April 23, 2007, 5:52 PM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txnreports.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ar.*;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdLookUpValue;
import com.ejb.entities.ar.*;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ar.ArRepSalesOrderDetails;

import jakarta.ejb.*;

import java.util.*;

@Stateless(name = "ArRepSalesOrderControllerEJB")
public class ArRepSalesOrderControllerBean extends EJBContextClass implements ArRepSalesOrderController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalArSalesOrderHome arSalesOrderHome;
    @EJB
    private LocalArSalesOrderInvoiceLineHome arSalesOrderInvoiceLineHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;

    public ArrayList executeArRepSalesOrder(HashMap criteria, ArrayList branchList, String invoiceStatus, String ORDER_BY, String GROUP_BY, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepSalesOrderControllerBean executeArRepSalesOrder");

        ArrayList list = new ArrayList();
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size() - 1;

            StringBuilder jbossQl = new StringBuilder();
            Iterator brIter = null;
            AdBranchDetails details = null;
            Object[] obj;
            Collection arSalesOrders = null;

            if (criteria.containsKey("customerCode")) {

                criteriaSize--;
            }

            if (criteria.get("approvalStatus").equals("")) {

                criteriaSize--;
            }

            criteriaSize--;

            jbossQl.append("SELECT OBJECT(so) FROM ArSalesOrder so WHERE (");

            if (branchList.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }

            brIter = branchList.iterator();

            details = (AdBranchDetails) brIter.next();
            jbossQl.append("so.soAdBranch=").append(details.getBrCode());

            while (brIter.hasNext()) {

                details = (AdBranchDetails) brIter.next();

                jbossQl.append(" OR so.soAdBranch=").append(details.getBrCode());
            }

            jbossQl.append(") ");
            firstArgument = false;

            // Allocate the size of the object parameter

            obj = new Object[criteriaSize];

            if (criteria.containsKey("customerCode")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("so.arCustomer.cstCustomerCode LIKE '%").append(criteria.get("customerCode")).append("%' ");
            }

            if (criteria.containsKey("customerType")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("so.arCustomer.arCustomerType.ctName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerType");
                ctr++;
            }

            if (criteria.containsKey("customerBatch")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("so.arCustomer.cstCustomerBatch=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerBatch");
                ctr++;
            }

            if (criteria.containsKey("customerClass")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("so.arCustomer.arCustomerClass.ccName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerClass");
                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("so.soDate>=?").append(ctr + 1).append(" ");
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
                jbossQl.append("so.soDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (criteria.containsKey("documentNumberFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("so.soDocumentNumber>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("documentNumberFrom");
                ctr++;
            }

            if (criteria.containsKey("documentNumberTo")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("so.soDocumentNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("documentNumberTo");
                ctr++;
            }

            if (criteria.containsKey("referenceNumberFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("so.soReferenceNumber>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("referenceNumberFrom");
                ctr++;
            }

            if (criteria.containsKey("referenceNumberTo")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("so.soReferenceNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("referenceNumberTo");
                ctr++;
            }

            if (criteria.containsKey("approvalStatus")) {

                if (criteria.get("approvalStatus").equals("N/A") || criteria.get("approvalStatus").equals("PENDING") || criteria.get("approvalStatus").equals("APPROVED")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("so.soApprovalStatus=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("approvalStatus");
                    ctr++;
                }
            }

            if (criteria.containsKey("includedUnposted")) {

                String unposted = (String) criteria.get("includedUnposted");

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                if (unposted.equals("NO")) {

                    jbossQl.append("so.soPosted = 1 ");

                } else {

                    jbossQl.append("so.soVoid = 0 ");
                }
            }

            if (criteria.containsKey("salesperson")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("so.arSalesperson.slpName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("salesperson");
                ctr++;
            }

            if (criteria.containsKey("orderStatus")) {
                Debug.print("ORDER STATUS : " + criteria.get("orderStatus"));

                if (!criteria.get("orderStatus").equals("")) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    if (criteria.get("orderStatus").equals("Good")) {
                        jbossQl.append("so.soOrderStatus = 'Good' ");
                    } else {
                        // Debug.print("ORDER STATUS : " + (String)criteria.get("orderStatus"));
                        jbossQl.append("so.soOrderStatus LIKE 'Bad%' ");
                    }
                }
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("so.soAdCompany=").append(AD_CMPNY).append(" ");

            Debug.print("jbossQl-" + jbossQl);

            arSalesOrders = arSalesOrderHome.getSOByCriteria(jbossQl.toString(), obj, 0, 0);

            if (!arSalesOrders.isEmpty()) {

                for (Object salesOrder : arSalesOrders) {

                    LocalArSalesOrder arSalesOrder = (LocalArSalesOrder) salesOrder;

                    ArRepSalesOrderDetails mdetails = new ArRepSalesOrderDetails();
                    mdetails.setSoDate(arSalesOrder.getSoDate());
                    mdetails.setSoDocumentNumber(arSalesOrder.getSoDocumentNumber());
                    mdetails.setSoReferenceNumber(arSalesOrder.getSoReferenceNumber());
                    mdetails.setSoDescription(arSalesOrder.getSoDescription());
                    mdetails.setSoCstCustomerCode(arSalesOrder.getArCustomer().getCstCustomerCode() + "-" + arSalesOrder.getArCustomer().getCstName());
                    mdetails.setSoCstCustomerClass(arSalesOrder.getArCustomer().getArCustomerClass().getCcName());
                    mdetails.setSoCstCustomerCode2(arSalesOrder.getArCustomer().getCstCustomerCode());
                    mdetails.setSoSlsSalespersonCode(arSalesOrder.getArSalesperson() != null ? arSalesOrder.getArSalesperson().getSlpSalespersonCode() : null);

                    mdetails.setSoSlsName(arSalesOrder.getArSalesperson() != null ? arSalesOrder.getArSalesperson().getSlpName() : null);

                    // select customer type
                    if (arSalesOrder.getArCustomer().getArCustomerType() == null) {

                        mdetails.setSoCstCustomerType("UNDEFINED");

                    } else {

                        mdetails.setSoCstCustomerType(arSalesOrder.getArCustomer().getArCustomerType().getCtName());
                    }

                    double ORDER_AMOUNT = 0d;
                    double ORDER_TAX_AMOUNT = 0d;
                    double ORDER_QTY = 0d;

                    if (!arSalesOrder.getArSalesOrderLines().isEmpty()) {

                        for (Object o : arSalesOrder.getArSalesOrderLines()) {

                            LocalArSalesOrderLine arSalesOrderLine = (LocalArSalesOrderLine) o;

                            ORDER_QTY += arSalesOrderLine.getSolQuantity();

                            if (arSalesOrderLine.getArSalesOrder().getArTaxCode().getTcType().equals("INCLUSIVE")) {

                                ORDER_TAX_AMOUNT += arSalesOrderLine.getSolAmount() - EJBCommon.roundIt(arSalesOrderLine.getSolAmount() / (1 + (arSalesOrderLine.getArSalesOrder().getArTaxCode().getTcRate() / 100)), adCompany.getGlFunctionalCurrency().getFcPrecision());
                                ORDER_AMOUNT += arSalesOrderLine.getSolAmount();

                            } else if (arSalesOrderLine.getArSalesOrder().getArTaxCode().getTcType().equals("EXCLUSIVE")) {

                                // tax exclusive, none, zero rated or exempt

                                ORDER_TAX_AMOUNT += EJBCommon.roundIt(arSalesOrderLine.getSolAmount() * arSalesOrderLine.getArSalesOrder().getArTaxCode().getTcRate() / 100, adCompany.getGlFunctionalCurrency().getFcPrecision());
                                ORDER_AMOUNT += arSalesOrderLine.getSolAmount() + ORDER_TAX_AMOUNT;

                            } else {

                                ORDER_AMOUNT += arSalesOrderLine.getSolAmount();
                            }
                        }
                    }

                    mdetails.setSoOrderQty(ORDER_QTY);
                    mdetails.setSoAmount(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arSalesOrder.getGlFunctionalCurrency().getFcCode(), arSalesOrder.getGlFunctionalCurrency().getFcName(), arSalesOrder.getSoConversionDate(), arSalesOrder.getSoConversionRate(), ORDER_AMOUNT, AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    mdetails.setSoTaxAmount(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arSalesOrder.getGlFunctionalCurrency().getFcCode(), arSalesOrder.getGlFunctionalCurrency().getFcName(), arSalesOrder.getSoConversionDate(), arSalesOrder.getSoConversionRate(), ORDER_TAX_AMOUNT, AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                    mdetails.setOrderBy(ORDER_BY);
                    mdetails.setSoOrderStatus(arSalesOrder.getSoOrderStatus());
                    mdetails.setSoApprovalStatus(arSalesOrder.getSoApprovalStatus());
                    mdetails.setSoApprovedRejectedBy(arSalesOrder.getSoApprovedRejectedBy());

                    double INVOICE_QTY = 0d;

                    Collection arSalesOrderInvoiceLines = arSalesOrderInvoiceLineHome.findSolBySoCodeAndInvAdBranch(arSalesOrder.getSoCode(), AD_BRNCH, AD_CMPNY);

                    for (Object salesOrderInvoiceLine : arSalesOrderInvoiceLines) {

                        LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) salesOrderInvoiceLine;

                        INVOICE_QTY += arSalesOrderInvoiceLine.getSilQuantityDelivered();
                        Debug.print("arSalesOrderInvoiceLine.getSilQuantityDelivered()=" + arSalesOrderInvoiceLine.getSilQuantityDelivered());
                    }

                    mdetails.setSoInvoiceQty(INVOICE_QTY);

                    list.add(mdetails);
                }
            }

            if (list.isEmpty() || list.size() == 0) {

                throw new GlobalNoRecordFoundException();
            }

            // sort

            switch (GROUP_BY) {
                case "CUSTOMER CODE":

                    list.sort(ArRepSalesOrderDetails.CustomerCodeComparator);

                    break;
                case "CUSTOMER TYPE":

                    list.sort(ArRepSalesOrderDetails.CustomerTypeComparator);

                    break;
                case "CUSTOMER CLASS":

                    list.sort(ArRepSalesOrderDetails.CustomerClassComparator);

                    break;
                case "SO NUMBER":

                    list.sort(ArRepSalesOrderDetails.SONumberComparator);

                    break;
                default:

                    list.sort(ArRepSalesOrderDetails.NoGroupComparator);
                    break;
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

        Debug.print("ArRepSalesOrderControllerBean getAdCompany");

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

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("ArRepSalesOrderControllerBean convertForeignToFunctionalCurrency");

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

        Debug.print("ArRepSalesOrderControllerBean ejbCreate");
    }

}