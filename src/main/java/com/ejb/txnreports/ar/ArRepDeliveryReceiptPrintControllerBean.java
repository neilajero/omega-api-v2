/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArRepDeliveryReceiptPrintControllerBean
 * @created October 19, 2005, 3:54 PM
 * @author Jolly T. Martin
 */
package com.ejb.txnreports.ar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;


import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdApproval;
import com.ejb.entities.ad.LocalAdApprovalDocument;
import com.ejb.dao.ad.LocalAdApprovalDocumentHome;
import com.ejb.dao.ad.LocalAdApprovalHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdUser;
import com.ejb.dao.ad.LocalAdUserHome;
import com.ejb.entities.ar.LocalArDistributionRecord;
import com.ejb.dao.ar.LocalArDistributionRecordHome;
import com.ejb.entities.ar.LocalArInvoice;
import com.ejb.dao.ar.LocalArInvoiceHome;
import com.ejb.entities.ar.LocalArInvoiceLineItem;
import com.ejb.entities.ar.LocalArSalesOrderInvoiceLine;
import com.ejb.entities.ar.LocalArSalesOrderLine;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.inv.LocalInvTag;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ar.ArRepDeliveryReceiptPrintDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ArRepDeliveryReceiptPrintControllerEJB")
public class ArRepDeliveryReceiptPrintControllerBean extends EJBContextClass implements ArRepDeliveryReceiptPrintController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdApprovalDocumentHome adApprovalDocumentHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;


    public ArrayList executeArRepDeliveryReceiptPrint(ArrayList invCodeList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepDeliveryReceiptPrintControllerBean executeArRepDeliveryReceiptPrint");

        ArrayList list = new ArrayList();

        try {

            for (Object value : invCodeList) {

                Integer INV_CODE = (Integer) value;

                LocalArInvoice arInvoice = null;

                try {

                    arInvoice = arInvoiceHome.findByPrimaryKey(INV_CODE);

                } catch (FinderException ex) {

                    continue;
                }

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(AD_CMPNY);
                LocalAdApprovalDocument adApprovalDocument = adApprovalDocumentHome.findByAdcType("AR INVOICE", AD_CMPNY);

                if (adApprovalDocument.getAdcPrintOption().equals("PRINT APPROVED ONLY")) {

                    if (arInvoice.getInvApprovalStatus() == null || arInvoice.getInvApprovalStatus().equals("PENDING")) {

                        continue;
                    }

                } else if (adApprovalDocument.getAdcPrintOption().equals("PRINT UNAPPROVED ONLY")) {

                    if (arInvoice.getInvApprovalStatus() != null && (arInvoice.getInvApprovalStatus().equals("N/A") || arInvoice.getInvApprovalStatus().equals("APPROVED"))) {

                        continue;
                    }
                }

                if (adApprovalDocument.getAdcAllowDuplicate() == EJBCommon.FALSE && arInvoice.getInvPrinted() == EJBCommon.TRUE) {

                    continue;
                }

                // show duplicate

                boolean showDuplicate = adApprovalDocument.getAdcTrackDuplicate() == EJBCommon.TRUE && arInvoice.getInvPrinted() == EJBCommon.TRUE;

                // set printed

                arInvoice.setInvPrinted(EJBCommon.TRUE);

                // get total vat of all invoices

                double TOTAL_TAX = 0d;

                Collection arDistributionRecords = arDistributionRecordHome.findDrsByDrClassAndInvCode("TAX", arInvoice.getInvCode(), AD_CMPNY);

                Iterator j = arDistributionRecords.iterator();

                while (j.hasNext()) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) j.next();

                    TOTAL_TAX += arDistributionRecord.getDrAmount();
                }

                arDistributionRecords = arDistributionRecordHome.findDrsByDrClassAndInvCode("DEFERRED TAX", arInvoice.getInvCode(), AD_CMPNY);

                j = arDistributionRecords.iterator();

                while (j.hasNext()) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) j.next();

                    TOTAL_TAX += arDistributionRecord.getDrAmount();
                }

                // get sales order invoice lines

                if (!arInvoice.getArSalesOrderInvoiceLines().isEmpty()) {

                    Collection arSalesOrderInvoiceLines = arInvoice.getArSalesOrderInvoiceLines();

                    for (Object salesOrderInvoiceLine : arSalesOrderInvoiceLines) {

                        LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) salesOrderInvoiceLine;
                        LocalArSalesOrderLine arSalesOrderLine = arSalesOrderInvoiceLine.getArSalesOrderLine();

                        ArRepDeliveryReceiptPrintDetails details = new ArRepDeliveryReceiptPrintDetails();

                        details.setDrpInvNumber(arInvoice.getInvNumber());
                        details.setDrpInvDate(arInvoice.getInvDate());
                        details.setDrpInvApprovedRejectedBy(arInvoice.getInvApprovedRejectedBy());
                        details.setDrpInvCreatedBy(arInvoice.getInvCreatedBy());
                        details.setDrpInvCustomerName(arInvoice.getArCustomer().getCstName());
                        details.setDrpInvCustomerAddress(arInvoice.getArCustomer().getCstAddress() + ", " + arInvoice.getArCustomer().getCstCity() + ", " + arInvoice.getArCustomer().getCstCountry());

                        details.setDrpIliAmount(arSalesOrderInvoiceLine.getSilAmount());
                        details.setDrpIliIiName(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvItem().getIiName());
                        details.setDrpIliDescription(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvItem().getIiDescription());
                        details.setDrpIliQuantity(arSalesOrderInvoiceLine.getSilQuantityDelivered());
                        details.setDrpIliUnitPrice(arSalesOrderInvoiceLine.getArSalesOrderLine().getSolUnitPrice());
                        details.setDrpIliUom(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvUnitOfMeasure().getUomName());
                        details.setDrpIliUomShortName(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvUnitOfMeasure().getUomShortName());
                        details.setDrpInvCustomerCode(arInvoice.getArCustomer().getCstCustomerCode());
                        details.setDrpSoDocumentNumber(arInvoice.getInvSoNumber());
                        details.setDrpInvPaymentTerm(arInvoice.getAdPaymentTerm().getPytName());

                        details.setDrpIliDiscount1(arSalesOrderInvoiceLine.getSilDiscount1());
                        details.setDrpIliDiscount2(arSalesOrderInvoiceLine.getSilDiscount2());
                        details.setDrpIliDiscount3(arSalesOrderInvoiceLine.getSilDiscount3());
                        details.setDrpIliDiscount4(arSalesOrderInvoiceLine.getSilDiscount4());

                        if (arInvoice.getArSalesperson() != null) {

                            details.setDrpSlpSalespersonCode(arInvoice.getArSalesperson().getSlpSalespersonCode());
                        }

                        // get created by user description
                        LocalAdUser adUser = adUserHome.findByUsrName(arInvoice.getInvCreatedBy(), AD_CMPNY);
                        details.setDrpInvCreatedByDescription(adUser.getUsrDescription());

                        details.setDrpInvBillingHeader(arInvoice.getInvBillingHeader());
                        details.setDrpInvBillingFooter(arInvoice.getInvBillingFooter());
                        details.setDrpInvBillingHeader2(arInvoice.getInvBillingHeader2());
                        details.setDrpInvBillingFooter2(arInvoice.getInvBillingFooter2());
                        details.setDrpInvBillingHeader3(arInvoice.getInvBillingHeader3());
                        details.setDrpInvBillingFooter3(arInvoice.getInvBillingFooter3());
                        details.setDrpInvReferenceNumber(arInvoice.getInvReferenceNumber());

                        details.SetDrpInvSoReferenceNumber(arSalesOrderLine.getArSalesOrder().getSoReferenceNumber());
                        details.setDrpInvShipToAddress(arInvoice.getInvShipToAddress());

                        details.setDrpInvClientPo(arInvoice.getInvClientPO());

                        details.setReportParameters(arInvoice.getReportParameter());

                        // trace misc
                        if (arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvItem().getIiTraceMisc() == EJBCommon.TRUE) {

                            if (arSalesOrderInvoiceLine.getInvTags().size() > 0) {
                                Debug.print("new code");
                                StringBuilder strBProperty = new StringBuilder();
                                StringBuilder strBSerial = new StringBuilder();
                                StringBuilder strBSpecs = new StringBuilder();
                                StringBuilder strBCustodian = new StringBuilder();
                                StringBuilder strBExpirationDate = new StringBuilder();

                                for (Object o : arSalesOrderInvoiceLine.getInvTags()) {

                                    LocalInvTag invTag = (LocalInvTag) o;

                                    // property code
                                    if (!invTag.getTgPropertyCode().equals("")) {
                                        strBProperty.append(invTag.getTgPropertyCode());
                                        strBProperty.append(System.getProperty("line.separator"));
                                    }

                                    // serial

                                    if (!invTag.getTgSerialNumber().equals("")) {
                                        strBSerial.append(invTag.getTgSerialNumber());
                                        strBSerial.append(System.getProperty("line.separator"));
                                    }

                                    // spec

                                    if (!invTag.getTgSpecs().equals("")) {
                                        strBSpecs.append(invTag.getTgSpecs());
                                        strBSpecs.append(System.getProperty("line.separator"));
                                    }

                                    // custodian

                                    if (invTag.getAdUser() != null) {
                                        strBCustodian.append(invTag.getAdUser().getUsrName());
                                        strBCustodian.append(System.getProperty("line.separator"));
                                    }

                                    // exp date

                                    if (invTag.getTgExpiryDate() != null) {
                                        strBExpirationDate.append(invTag.getTgExpiryDate());
                                        strBExpirationDate.append(System.getProperty("line.separator"));
                                    }
                                }
                                // property code
                                details.setDrpIliPropertyCode(strBProperty.toString());
                                // serial number
                                details.setDrpIliSerialNumber(strBSerial.toString());
                                // specs
                                details.setDrpIliSpecs(strBSpecs.toString());
                                // custodian
                                details.setDrpIliCustodian(strBCustodian.toString());
                                // expiration date
                                details.setDrpIliExpiryDate(strBExpirationDate.toString());
                            }
                        }

                        list.add(details);
                    }

                } else if (!arInvoice.getArInvoiceLineItems().isEmpty()) {

                    Collection arInvoiceLineItems = arInvoice.getArInvoiceLineItems();

                    for (Object invoiceLineItem : arInvoiceLineItems) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) invoiceLineItem;

                        ArRepDeliveryReceiptPrintDetails details = new ArRepDeliveryReceiptPrintDetails();

                        details.setDrpInvNumber(arInvoice.getInvNumber());
                        details.setDrpInvDate(arInvoice.getInvDate());
                        details.setDrpInvApprovedRejectedBy(arInvoice.getInvApprovedRejectedBy());
                        details.setDrpInvCreatedBy(arInvoice.getInvCreatedBy());
                        details.setDrpInvCustomerName(arInvoice.getArCustomer().getCstName());
                        details.setDrpInvCustomerAddress(arInvoice.getArCustomer().getCstAddress() + ", " + arInvoice.getArCustomer().getCstCity() + ", " + arInvoice.getArCustomer().getCstCountry());

                        // added
                        details.setDrpInvReferenceNumber(arInvoice.getInvReferenceNumber());
                        details.setDrpInvCustomerName(arInvoice.getArCustomer().getCstName());
                        details.setDrpInvDate(arInvoice.getInvDate());
                        details.setDrpInvShipToAddress(arInvoice.getInvShipToAddress());
                        details.setDrpInvInvDescription(arInvoice.getInvDescription());
                        System.out.print(arInvoice.getInvDescription() + "description");
                        System.out.print("ReferenceNumber " + arInvoice.getInvReferenceNumber() + "ship" + arInvoice.getInvShipToAddress() + "Date" + arInvoice.getInvDate() + "customer" + arInvoice.getArCustomer().getCstName());
                        // end
                        details.setDrpInvAmountDue(arInvoice.getInvAmountDue());

                        details.setDrpIliAmount(arInvoiceLineItem.getIliAmount());
                        details.setDrpIliTaxAmount(arInvoiceLineItem.getIliTaxAmount());
                        details.setDrpIliIiName(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                        details.setDrpIliDescription(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiDescription());
                        details.setDrpIliQuantity(arInvoiceLineItem.getIliQuantity());
                        details.setDrpIliUnitPrice(arInvoiceLineItem.getIliUnitPrice());
                        details.setDrpIliUom(arInvoiceLineItem.getInvUnitOfMeasure().getUomName());
                        details.setDrpIliUomShortName(arInvoiceLineItem.getInvUnitOfMeasure().getUomShortName());
                        details.setDrpInvCustomerCode(arInvoice.getArCustomer().getCstCustomerCode());
                        details.setDrpSoDocumentNumber(arInvoice.getInvSoNumber());
                        details.setDrpInvPaymentTerm(arInvoice.getAdPaymentTerm().getPytName());

                        if (arInvoice.getArSalesperson() != null) {

                            details.setDrpSlpSalespersonCode(arInvoice.getArSalesperson().getSlpSalespersonCode());
                        }

                        details.setDrpIliDiscount1(arInvoiceLineItem.getIliDiscount1());
                        details.setDrpIliDiscount2(arInvoiceLineItem.getIliDiscount2());
                        details.setDrpIliDiscount3(arInvoiceLineItem.getIliDiscount3());
                        details.setDrpIliDiscount4(arInvoiceLineItem.getIliDiscount4());
                        details.setDrpIliTotalDiscount(arInvoiceLineItem.getIliTotalDiscount());

                        // get created by user description
                        LocalAdUser adUser = adUserHome.findByUsrName(arInvoice.getInvCreatedBy(), AD_CMPNY);
                        details.setDrpInvCreatedByDescription(adUser.getUsrDescription());

                        details.setDrpInvBillingHeader(arInvoice.getInvBillingHeader());
                        details.setDrpInvBillingFooter(arInvoice.getInvBillingFooter());
                        details.setDrpInvBillingHeader2(arInvoice.getInvBillingHeader2());
                        details.setDrpInvBillingFooter2(arInvoice.getInvBillingFooter2());
                        details.setDrpInvBillingHeader3(arInvoice.getInvBillingHeader3());
                        details.setDrpInvBillingFooter3(arInvoice.getInvBillingFooter3());
                        details.setDrpInvShipToAddress(arInvoice.getInvShipToAddress());
                        details.setDrpInvClientPo(arInvoice.getInvClientPO());
                        // added
                        details.setDrpInvInvDescription(arInvoice.getInvDescription());
                        details.setDrpInvReferenceNumber(arInvoice.getInvReferenceNumber());
                        details.setDrpInvCustomerName(arInvoice.getArCustomer().getCstName());
                        details.setDrpInvDate(arInvoice.getInvDate());
                        details.setDrpInvShipToAddress(arInvoice.getInvShipToAddress());
                        System.out.print("ReferenceNumber " + arInvoice.getInvReferenceNumber() + "ship" + arInvoice.getInvShipToAddress() + "Date" + arInvoice.getInvDate() + "customer" + arInvoice.getArCustomer().getCstName());
                        // end

                        details.setReportParameters(arInvoice.getReportParameter());

                        // trace misc
                        if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiTraceMisc() == EJBCommon.TRUE) {

                            if (arInvoiceLineItem.getInvTags().size() > 0) {
                                Debug.print("new code");
                                StringBuilder strBProperty = new StringBuilder();
                                StringBuilder strBSerial = new StringBuilder();
                                StringBuilder strBSpecs = new StringBuilder();
                                StringBuilder strBCustodian = new StringBuilder();
                                StringBuilder strBExpirationDate = new StringBuilder();

                                for (Object o : arInvoiceLineItem.getInvTags()) {

                                    LocalInvTag invTag = (LocalInvTag) o;

                                    // property code
                                    if (!invTag.getTgPropertyCode().equals("")) {
                                        strBProperty.append(invTag.getTgPropertyCode());
                                        strBProperty.append(System.getProperty("line.separator"));
                                    }

                                    // serial

                                    if (!invTag.getTgSerialNumber().equals("")) {
                                        strBSerial.append(invTag.getTgSerialNumber());
                                        strBSerial.append(System.getProperty("line.separator"));
                                    }

                                    // spec

                                    if (!invTag.getTgSpecs().equals("")) {
                                        strBSpecs.append(invTag.getTgSpecs());
                                        strBSpecs.append(System.getProperty("line.separator"));
                                    }

                                    // custodian

                                    if (invTag.getAdUser() != null) {
                                        strBCustodian.append(invTag.getAdUser().getUsrName());
                                        strBCustodian.append(System.getProperty("line.separator"));
                                    }

                                    // exp date

                                    if (invTag.getTgExpiryDate() != null) {
                                        strBExpirationDate.append(invTag.getTgExpiryDate());
                                        strBExpirationDate.append(System.getProperty("line.separator"));
                                    }
                                }
                                // property code
                                details.setDrpIliPropertyCode(strBProperty.toString());
                                // serial number
                                details.setDrpIliSerialNumber(strBSerial.toString());
                                // specs
                                details.setDrpIliSpecs(strBSpecs.toString());
                                // custodian
                                details.setDrpIliCustodian(strBCustodian.toString());
                                // expiration date
                                details.setDrpIliExpiryDate(strBExpirationDate.toString());
                            }
                        }

                        list.add(details);
                    }
                }
            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("ArRepDeliveryReceiptPrintControllerBean getAdCompany");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());
            details.setCmpAddress(adCompany.getCmpAddress());
            details.setCmpCity(adCompany.getCmpCity());
            details.setCmpCountry(adCompany.getCmpCountry());
            details.setCmpTin(adCompany.getCmpTin());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ArRepDeliveryReceiptPrintControllerBean ejbCreate");
    }
}