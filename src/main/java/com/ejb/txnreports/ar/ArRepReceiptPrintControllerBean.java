/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArRepReceiptPrintControllerBean
 * @created March 11, 2004, 5:05 PM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txnreports.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdApprovalDocumentHome;
import com.ejb.dao.ad.LocalAdApprovalHome;
import com.ejb.dao.ar.LocalArDistributionRecordHome;
import com.ejb.dao.ar.LocalArReceiptHome;
import com.ejb.entities.ad.LocalAdApproval;
import com.ejb.entities.ad.LocalAdApprovalDocument;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ar.*;
import com.ejb.entities.inv.LocalInvTag;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.*;
import com.util.ad.AdCompanyDetails;
import com.util.mod.ar.ArModDistributionRecordDetails;
import com.util.mod.ar.ArModReceiptDetails;

import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

@Stateless(name = "ArRepReceiptPrintControllerEJB")
public class ArRepReceiptPrintControllerBean extends EJBContextClass implements ArRepReceiptPrintController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdApprovalDocumentHome adApprovalDocumentHome;

    public ArrayList executeArRepReceiptPrintSub(ArrayList rctCodeList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArRepReceiptPrintControllerBean executeArRepReceiptPrint");
        ArrayList list = new ArrayList();
        try {

            for (Object o : rctCodeList) {

                Integer RCT_CODE = (Integer) o;

                LocalArReceipt arReceipt = null;

                try {

                    arReceipt = arReceiptHome.findByPrimaryKey(RCT_CODE);

                } catch (FinderException ex) {

                    continue;
                }

                // get total vat of all receipt

                Collection arDistributionRecords = arDistributionRecordHome.findByRctCode(arReceipt.getRctCode(), AD_CMPNY);

                for (Object distributionRecord : arDistributionRecords) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                    ArModDistributionRecordDetails mdetails = new ArModDistributionRecordDetails();

                    mdetails.setDrCoaAccountNumber(arDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                    mdetails.setDrCoaAccountDescription(arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());
                    mdetails.setDrDebit(arDistributionRecord.getDrDebit());
                    mdetails.setDrAmount(arDistributionRecord.getDrAmount());
                    mdetails.setDrClass(arDistributionRecord.getDrClass());

                    list.add(mdetails);
                }
            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList executeArRepReceiptPrint(ArrayList rctCodeList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArRepReceiptPrintControllerBean executeArRepReceiptPrint");
        ArrayList list = new ArrayList();
        try {

            boolean isMiscReceiptItems = false;

            for (Object value : rctCodeList) {

                Integer RCT_CODE = (Integer) value;

                LocalArReceipt arReceipt = null;

                try {

                    arReceipt = arReceiptHome.findByPrimaryKey(RCT_CODE);

                } catch (FinderException ex) {

                    continue;
                }

                // get total vat of all receipt

                double TOTAL_TAX = 0d;
                double DEFERRED_TOTAL_TAX = 0d;

                Collection arDistributionRecords = arDistributionRecordHome.findDrsByDrClassAndRctCode("TAX", arReceipt.getRctCode(), AD_CMPNY);

                Iterator j = arDistributionRecords.iterator();

                while (j.hasNext()) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) j.next();

                    TOTAL_TAX += arDistributionRecord.getDrAmount();
                }

                arDistributionRecords = arDistributionRecordHome.findDrsByDrClassAndRctCode("DEFERRED TAX", arReceipt.getRctCode(), AD_CMPNY);

                j = arDistributionRecords.iterator();

                while (j.hasNext()) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) j.next();

                    DEFERRED_TOTAL_TAX += arDistributionRecord.getDrAmount();
                }

                Collection arReceiptInvoiceLineItems = arReceipt.getArInvoiceLineItems();
                Collection arReceiptInvoiceLines = arReceipt.getArInvoiceLines();
                Collection arReceiptAppliedInvoices = arReceipt.getArAppliedInvoices();

                double totalAmount = 0d;

                if (!arReceiptInvoiceLineItems.isEmpty()) {
                    Debug.print("MISC arReceiptInvoiceLineItems=" + arReceiptInvoiceLineItems.size());

                    j = arReceiptInvoiceLineItems.iterator();

                    while (j.hasNext()) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) j.next();

                        // misc receipt

                        LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(AD_CMPNY);
                        LocalAdApprovalDocument adApprovalDocument = adApprovalDocumentHome.findByAdcType("AR RECEIPT", AD_CMPNY);

                        if (adApprovalDocument.getAdcPrintOption().equals("PRINT APPROVED ONLY")) {

                            if (arReceipt.getRctApprovalStatus() == null || arReceipt.getRctApprovalStatus().equals("PENDING")) {

                                continue;
                            }

                        } else if (adApprovalDocument.getAdcPrintOption().equals("PRINT UNAPPROVED ONLY")) {

                            if (arReceipt.getRctApprovalStatus() != null && (arReceipt.getRctApprovalStatus().equals("N/A") || arReceipt.getRctApprovalStatus().equals("APPROVED"))) {

                                continue;
                            }
                        }

                        if (adApprovalDocument.getAdcAllowDuplicate() == EJBCommon.FALSE && arReceipt.getRctPrinted() == EJBCommon.TRUE) {

                            continue;
                        }

                        // show duplicate

                        boolean showDuplicate = adApprovalDocument.getAdcTrackDuplicate() == EJBCommon.TRUE && arReceipt.getRctPrinted() == EJBCommon.TRUE;

                        // set printed

                        arReceipt.setRctPrinted(EJBCommon.TRUE);

                        ArModReceiptDetails mdetails = new ArModReceiptDetails();

                        mdetails.setRctDescription(arReceipt.getRctDescription());
                        mdetails.setRctDate(arReceipt.getRctDate());
                        mdetails.setRctNumber(arReceipt.getRctNumber());
                        mdetails.setRctReferenceNumber(arReceipt.getRctReferenceNumber());
                        mdetails.setRctPaymentMethod(arReceipt.getRctPaymentMethod());
                        mdetails.setRctCheckNo(arReceipt.getRctCheckNo());
                        mdetails.setRctAmount(arReceipt.getRctAmount());
                        mdetails.setRctCstCustomerCode(arReceipt.getArCustomer().getCstCustomerCode());
                        mdetails.setRctCstName(arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName());
                        mdetails.setRctCstAddress(arReceipt.getRctCustomerAddress() == null ? arReceipt.getArCustomer().getCstAddress() : arReceipt.getRctCustomerAddress());
                        mdetails.setRctShowDuplicate(showDuplicate ? EJBCommon.TRUE : EJBCommon.FALSE);
                        mdetails.setRctType("MISC");
                        mdetails.setRctCstCity(arReceipt.getArCustomer().getCstCity());
                        mdetails.setRctBaBankName(arReceipt.getAdBankAccount().getAdBank().getBnkName());

                        mdetails.setRctChequeNumber(arReceipt.getRctChequeNumber());
                        mdetails.setRctVoucherNumber(arReceipt.getRctVoucherNumber());
                        mdetails.setRctCardNumber1(arReceipt.getRctCardNumber1());
                        mdetails.setRctCardNumber2(arReceipt.getRctCardNumber2());
                        mdetails.setRctCardNumber3(arReceipt.getRctCardNumber3());

                        mdetails.setRctAmountCash(arReceipt.getRctAmountCash());
                        mdetails.setRctAmountCheque(arReceipt.getRctAmountCheque());
                        mdetails.setRctAmountVoucher(arReceipt.getRctAmountVoucher());
                        mdetails.setRctAmountCard1(arReceipt.getRctAmountCard1());
                        mdetails.setRctAmountCard2(arReceipt.getRctAmountCard2());
                        mdetails.setRctAmountCard3(arReceipt.getRctAmountCard3());
                        mdetails.setRctPosted(arReceipt.getRctPosted());

                        if (arReceipt.getArSalesperson() != null) {

                            mdetails.setRctSlpSalespersonCode(arReceipt.getArSalesperson().getSlpSalespersonCode());
                        }

                        // line item details
                        mdetails.setRctInvoiceType("");
                        mdetails.setRctIlName(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                        mdetails.setRctIlDescription(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiDescription());
                        mdetails.setRctIlQuantity(arInvoiceLineItem.getIliQuantity());
                        mdetails.setRctIlUomShortName(arInvoiceLineItem.getInvUnitOfMeasure().getUomShortName());
                        mdetails.setRctIlUnitPrice(arInvoiceLineItem.getIliUnitPrice());
                        mdetails.setRctIlAmount(arInvoiceLineItem.getIliAmount() + arInvoiceLineItem.getIliTaxAmount() + arInvoiceLineItem.getIliTotalDiscount());
                        mdetails.setRctIlTax(arInvoiceLineItem.getIliTaxAmount());
                        mdetails.setRctIlAdLvCategory(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiAdLvCategory());

                        mdetails.setRctTotalTax(TOTAL_TAX);

                        // get unit price w/o VAT
                        short precisionUnit = this.getGlFcPrecisionUnit(AD_CMPNY);

                        double UNT_PRC = this.calculateIlNetUnitPrice(mdetails, arReceipt.getArTaxCode().getTcRate(), arReceipt.getArTaxCode().getTcType(), precisionUnit);

                        mdetails.setRctIlUnitPriceWoVat(UNT_PRC);

                        // get tax rate
                        mdetails.setRctTcRate(arReceipt.getArTaxCode().getTcRate());

                        // get discount
                        String discount = "";

                        if (arInvoiceLineItem.getIliDiscount1() != 0) {
                            discount = "" + arInvoiceLineItem.getIliDiscount1();
                        }

                        if (arInvoiceLineItem.getIliDiscount2() != 0) {
                            discount = discount + (!discount.equals("") ? "," : "") + arInvoiceLineItem.getIliDiscount2();
                        }

                        if (arInvoiceLineItem.getIliDiscount3() != 0) {
                            discount = discount + (!discount.endsWith(",") && !discount.equals("") ? "," : "") + arInvoiceLineItem.getIliDiscount3();
                        }

                        if (arInvoiceLineItem.getIliDiscount4() != 0) {
                            discount = discount + (!discount.endsWith(",") && !discount.equals("") ? "," : "") + arInvoiceLineItem.getIliDiscount4();
                        }

                        discount = discount.replaceAll(".0,", ",");

                        if (discount.endsWith(",")) {
                            discount = discount.substring(0, discount.length() - 1);
                        }

                        if (discount.endsWith(".0")) {
                            discount = discount.substring(0, discount.length() - 2);
                        }

                        mdetails.setRctIliDiscount(discount);
                        mdetails.setRctIliDiscount1(arInvoiceLineItem.getIliDiscount1());
                        mdetails.setRctIliDiscount2(arInvoiceLineItem.getIliDiscount2());
                        mdetails.setRctIliDiscount3(arInvoiceLineItem.getIliDiscount3());
                        mdetails.setRctIliDiscount4(arInvoiceLineItem.getIliDiscount4());
                        mdetails.setRctIliTotalDiscount(arInvoiceLineItem.getIliTotalDiscount());
                        mdetails.setRctMiscType("ITEMS");
                        mdetails.setRctInvoiceType("");

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
                                if (!invTag.getTgPropertyCode().trim().equals("")) {
                                    strBProperty.append(invTag.getTgPropertyCode());
                                    strBProperty.append(System.getProperty("line.separator"));
                                }

                                // serial

                                if (!invTag.getTgSerialNumber().trim().equals("")) {
                                    strBSerial.append(invTag.getTgSerialNumber());
                                    strBSerial.append(System.getProperty("line.separator"));
                                }

                                // spec

                                if (!invTag.getTgSpecs().trim().equals("")) {
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
                            mdetails.setRctIlItemPropertyCode(strBProperty.toString());
                            // serial number
                            mdetails.setRctIlItemSerialNumber(strBSerial.toString());
                            // specs
                            mdetails.setRctIlItemSpecs(strBSpecs.toString());
                            // custodian
                            mdetails.setRctIlItemCustodian(strBCustodian.toString());
                            // expiration date
                            mdetails.setRctIlItemExpiryDate(strBExpirationDate.toString());
                        }

                        list.add(mdetails);
                    }

                    isMiscReceiptItems = true;

                } else if (!arReceiptInvoiceLines.isEmpty()) {

                    j = arReceiptInvoiceLines.iterator();

                    while (j.hasNext()) {

                        LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) j.next();

                        // misc receipt

                        LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(AD_CMPNY);
                        LocalAdApprovalDocument adApprovalDocument = adApprovalDocumentHome.findByAdcType("AR RECEIPT", AD_CMPNY);

                        if (adApprovalDocument.getAdcPrintOption().equals("PRINT APPROVED ONLY")) {

                            if (arReceipt.getRctApprovalStatus() == null || arReceipt.getRctApprovalStatus().equals("PENDING")) {

                                continue;
                            }

                        } else if (adApprovalDocument.getAdcPrintOption().equals("PRINT UNAPPROVED ONLY")) {

                            if (arReceipt.getRctApprovalStatus() != null && (arReceipt.getRctApprovalStatus().equals("N/A") || arReceipt.getRctApprovalStatus().equals("APPROVED"))) {

                                continue;
                            }
                        }

                        if (adApprovalDocument.getAdcAllowDuplicate() == EJBCommon.FALSE && arReceipt.getRctPrinted() == EJBCommon.TRUE) {

                            continue;
                        }

                        // show duplicate

                        boolean showDuplicate = adApprovalDocument.getAdcTrackDuplicate() == EJBCommon.TRUE && arReceipt.getRctPrinted() == EJBCommon.TRUE;

                        // set printed

                        arReceipt.setRctPrinted(EJBCommon.TRUE);

                        ArModReceiptDetails mdetails = new ArModReceiptDetails();

                        mdetails.setRctDescription(arReceipt.getRctDescription());
                        mdetails.setRctDate(arReceipt.getRctDate());
                        mdetails.setRctNumber(arReceipt.getRctNumber());
                        mdetails.setRctReferenceNumber(arReceipt.getRctReferenceNumber());
                        mdetails.setRctPaymentMethod(arReceipt.getRctPaymentMethod());
                        mdetails.setRctCheckNo(arReceipt.getRctCheckNo());
                        mdetails.setRctAmount(arReceipt.getRctAmount());
                        mdetails.setRctCstCustomerCode(arReceipt.getArCustomer().getCstCustomerCode());
                        mdetails.setRctCstName(arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName());
                        mdetails.setRctCstAddress(arReceipt.getRctCustomerAddress() == null ? arReceipt.getArCustomer().getCstAddress() : arReceipt.getRctCustomerAddress());
                        mdetails.setRctShowDuplicate(showDuplicate ? EJBCommon.TRUE : EJBCommon.FALSE);
                        mdetails.setRctType("MISC");
                        mdetails.setRctCstCity(arReceipt.getArCustomer().getCstCity());
                        mdetails.setRctBaBankName(arReceipt.getAdBankAccount().getAdBank().getBnkName());

                        if (arReceipt.getArSalesperson() != null) {

                            mdetails.setRctSlpSalespersonCode(arReceipt.getArSalesperson().getSlpSalespersonCode());
                        }

                        // receipt line details
                        mdetails.setRctIlName(arInvoiceLine.getArStandardMemoLine().getSmlName());
                        mdetails.setRctIlDescription(arInvoiceLine.getIlDescription());
                        mdetails.setRctIlQuantity(arInvoiceLine.getIlQuantity());
                        mdetails.setRctIlUnitPrice(arInvoiceLine.getIlUnitPrice());
                        mdetails.setRctIlAmount(arInvoiceLine.getIlAmount());
                        mdetails.setRctMiscType("MEMO");
                        mdetails.setRctInvoiceType("");

                        list.add(mdetails);
                    }

                } else if (!arReceiptAppliedInvoices.isEmpty()) {
                    // collection

                    j = arReceiptAppliedInvoices.iterator();


                    while (j.hasNext()) {

                        LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) j.next();

                        LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(AD_CMPNY);
                        LocalAdApprovalDocument adApprovalDocument = adApprovalDocumentHome.findByAdcType("AR RECEIPT", AD_CMPNY);

                        if (adApprovalDocument.getAdcPrintOption().equals("PRINT APPROVED ONLY")) {

                            if (arReceipt.getRctApprovalStatus() == null || arReceipt.getRctApprovalStatus().equals("PENDING")) {

                                continue;
                            }

                        } else if (adApprovalDocument.getAdcPrintOption().equals("PRINT UNAPPROVED ONLY")) {

                            if (arReceipt.getRctApprovalStatus() != null && (arReceipt.getRctApprovalStatus().equals("N/A") || arReceipt.getRctApprovalStatus().equals("APPROVED"))) {

                                continue;
                            }
                        }

                        if (adApprovalDocument.getAdcAllowDuplicate() == EJBCommon.FALSE && arReceipt.getRctPrinted() == EJBCommon.TRUE) {

                            continue;
                        }

                        // show duplicate

                        boolean showDuplicate = adApprovalDocument.getAdcTrackDuplicate() == EJBCommon.TRUE && arReceipt.getRctPrinted() == EJBCommon.TRUE;

                        // set printed

                        arReceipt.setRctPrinted(EJBCommon.TRUE);

                        ArModReceiptDetails mdetails = new ArModReceiptDetails();

                        mdetails.setRctDescription(arReceipt.getRctDescription());
                        mdetails.setRctDate(arReceipt.getRctDate());
                        mdetails.setRctNumber(arReceipt.getRctNumber());
                        mdetails.setRctReferenceNumber(arReceipt.getRctReferenceNumber());
                        mdetails.setRctPaymentMethod(arReceipt.getRctPaymentMethod());
                        mdetails.setRctCheckNo(arReceipt.getRctCheckNo());

                        Iterator x = arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getArInvoiceLines().iterator();
                        String invoiceType = "";
                        while (x.hasNext()) {
                            LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) x.next();

                            Debug.print("SML_NAME=" + arInvoiceLine.getArStandardMemoLine().getSmlName());
                            String smlName = arInvoiceLine.getArStandardMemoLine().getSmlName();
                            if (smlName.contains("ASD") || smlName.contains("Association Dues")) {
                                invoiceType = "ASD";

                            } else if (smlName.contains("RPT") || smlName.contains("RealProperty Tax")) {
                                invoiceType = "RPT";

                            } else if (smlName.contains("PD") || smlName.contains("Parking Dues")) {
                                invoiceType = "PD";

                            } else if (smlName.contains("WTR") || smlName.contains("Water")) {
                                invoiceType = "WTR";

                            } else {
                                invoiceType = "OTH";
                            }
                            break;
                        }

                        double amount = arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiPenaltyApplyAmount() + arAppliedInvoice.getAiCreditBalancePaid() + arAppliedInvoice.getAiDiscountAmount();
                        mdetails.setRctInvoiceType(invoiceType);
                        Debug.print("amount=" + amount);
                        mdetails.setRctAmount(amount);
                        // mdetails.setRctAmountASD(invoiceType.equals("ASD")? amount : 0d);
                        mdetails.setRctCstCustomerCode(arReceipt.getArCustomer().getCstCustomerCode());
                        mdetails.setRctCstName(arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName());
                        mdetails.setRctCstAddress(arReceipt.getRctCustomerAddress() == null ? arReceipt.getArCustomer().getCstAddress() : arReceipt.getRctCustomerAddress());
                        mdetails.setRctShowDuplicate(showDuplicate ? EJBCommon.TRUE : EJBCommon.FALSE);
                        mdetails.setRctCstCustomerCode(arReceipt.getArCustomer().getCstCustomerCode());
                        mdetails.setRctType("COLLECTION");
                        mdetails.setRctCstCity(arReceipt.getArCustomer().getCstCity());
                        mdetails.setRctBaBankName(arReceipt.getAdBankAccount().getAdBank().getBnkName());

                        // applied invoice details
                        mdetails.setRctAiInvoiceNumber(arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvNumber());
                        mdetails.setRctAiApplyAmount(arAppliedInvoice.getAiApplyAmount());

                        if (arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getArSalesperson() != null) {

                            mdetails.setRctSlpSalespersonCode(arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getArSalesperson().getSlpSalespersonCode());
                            mdetails.setRctSlpName(arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getArSalesperson().getSlpName());
                        }

                        mdetails.setRctMiscType("");

                        mdetails.setRctCstTin(arReceipt.getArCustomer().getCstTin());
                        mdetails.setRctTotalTax(TOTAL_TAX);

                        list.add(mdetails);
                    }
                }
            }

            Debug.print("list=" + list);

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            if (isMiscReceiptItems) {

                list.sort(ArModReceiptDetails.sortByItemCategory);
                list.sort(ArModReceiptDetails.sortByReceiptNumber);
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
        Debug.print("ArRepReceiptPrintControllerBean getAdCompany");
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

    // private methods
    private short getGlFcPrecisionUnit(Integer AD_CMPNY) {
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private double calculateIlNetUnitPrice(ArModReceiptDetails mdetails, double tcRate, String tcType, short precisionUnit) {
        double amount = mdetails.getRctIlUnitPrice();
        if (tcType.equals("INCLUSIVE")) {

            amount = EJBCommon.roundIt(mdetails.getRctIlUnitPrice() / (1 + (tcRate / 100)), precisionUnit);
        }
        return amount;
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ArRepReceiptPrintControllerBean ejbCreate");
    }

}