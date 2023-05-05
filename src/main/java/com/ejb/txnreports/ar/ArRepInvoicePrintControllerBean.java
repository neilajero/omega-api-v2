/**
 * @copyright 2022 Omega Business Consulting, Inc.
 * @class ArRepInvoicePrintControllerBean
 * @created March 11, 2004, 9:38 AM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txnreports.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ar.LocalArDistributionRecordHome;
import com.ejb.dao.ar.LocalArInvoiceHome;
import com.ejb.dao.ar.LocalArInvoiceLineItemHome;
import com.ejb.entities.ad.*;
import com.ejb.entities.ar.*;
import com.ejb.entities.inv.LocalInvTag;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.*;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ar.ArRepInvoicePrintDetails;

import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

@Stateless(name = "ArRepInvoicePrintControllerEJB")
public class ArRepInvoicePrintControllerBean extends EJBContextClass implements ArRepInvoicePrintController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdApprovalDocumentHome adApprovalDocumentHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalArInvoiceLineItemHome arInvoiceLineItemHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;


    public ArrayList executeArRepInvoicePrint(ArrayList invCodeList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepInvoicePrintControllerBean executeArRepInvoicePrint");

        ArrayList list = new ArrayList();

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

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

                // get invoice lines

                if (adPreference.getPrfArSalesInvoiceDataSource().equals("AR DISTRIBUTION RECORD")) {

                    // ar distribution record data source

                    if (!arInvoice.getArInvoiceLines().isEmpty()) {

                        Collection arInvoiceLines = arInvoice.getArInvoiceLines();

                        for (Object invoiceLine : arInvoiceLines) {

                            LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) invoiceLine;

                            ArRepInvoicePrintDetails details = new ArRepInvoicePrintDetails();

                            details.setIpInvType(arInvoice.getInvType());
                            details.setIpInvDescription(arInvoice.getInvDescription());
                            details.setIpInvDate(arInvoice.getInvDate());
                            details.setIpInvNumber(arInvoice.getInvNumber());

                            try {
                                details.setIpInvBatch(arInvoice.getArInvoiceBatch().getIbName());

                            } catch (Exception ex) {
                                details.setIpInvBatch("");
                            }
                            details.setIpInvCustomerName(arInvoice.getArCustomer().getCstName());
                            details.setIpInvCustomerAddress(arInvoice.getArCustomer().getCstAddress());
                            details.setIpInvCustomerCity(arInvoice.getArCustomer().getCstCity());
                            details.setIpInvCustomerDealPrice(arInvoice.getArCustomer().getCstDealPrice());
                            details.setIpInvCustomerCountry(arInvoice.getArCustomer().getCstCountry());
                            details.setIpIlDescription(arInvoiceLine.getIlDescription());
                            details.setIpIlQuantity(arInvoiceLine.getIlQuantity());

                            details.setIpIlUnitPrice(arInvoiceLine.getIlUnitPrice());
                            details.setIpIlAmount(arInvoiceLine.getIlAmount() + arInvoiceLine.getIlTaxAmount());

                            details.setIpInvCurrency(arInvoice.getGlFunctionalCurrency().getFcName());
                            details.setIpInvCurrencySymbol(arInvoice.getGlFunctionalCurrency().getFcSymbol());
                            details.setIpInvCurrencyDescription(arInvoice.getGlFunctionalCurrency().getFcDescription());
                            details.setIpInvReferenceNumber(arInvoice.getInvReferenceNumber());
                            details.setIpInvCreatedBy(arInvoice.getInvCreatedBy());
                            details.setIpInvApprovalStatus(arInvoice.getInvApprovalStatus());
                            details.setIpReportParameter(arInvoice.getReportParameter());

                            details.setIpInvIsDraft(arInvoice.getInvPosted() == EJBCommon.FALSE);
                            try {
                                LocalAdUser adUser = adUserHome.findByUsrName(arInvoice.getInvCreatedBy(), AD_CMPNY);
                                details.setIpInvCreatedByDesc(adUser.getUsrDescription());
                                System.out.print("adUser.getUsrDescription(): " + adUser.getUsrDescription());
                            } catch (FinderException ex) {
                            }

                            try {
                                Debug.print("adPreference.getPrfApDefaultChecker()(): " + adPreference.getPrfApDefaultChecker());
                                LocalAdUser adUser2 = adUserHome.findByUsrName(adPreference.getPrfApDefaultChecker(), AD_CMPNY);
                                details.setIpInvCheckByDescription(adUser2.getUsrDescription());
                                System.out.print("adUser2.getUsrDescription(): " + adUser2.getUsrDescription());
                            } catch (Exception e) {
                                details.setIpInvCheckByDescription("");
                            }

                            try {
                                Debug.print("apCheck.getChkApprovedRejectedBy(): " + arInvoice.getInvApprovedRejectedBy());
                                LocalAdUser adUser3 = adUserHome.findByUsrName(arInvoice.getInvApprovedRejectedBy(), AD_CMPNY);
                                details.setIpInvApprovedRejectedByDescription(adUser3.getUsrDescription());
                                System.out.print("adUser3.getUsrDescription(): " + adUser3.getUsrDescription());
                            } catch (Exception e) {
                                details.setIpInvApprovedRejectedByDescription("");
                            }

                            details.setIpInvApprovedRejectedBy(arInvoice.getInvApprovedRejectedBy());
                            details.setIpInvAmount(arInvoice.getInvAmountDue());
                            details.setIpInvAmountUnearnedInterest(arInvoice.getInvAmountUnearnedInterest());
                            details.setIpShowDuplicate(showDuplicate ? EJBCommon.TRUE : EJBCommon.FALSE);

                            // added fields

                            details.setIpInvBillingHeader(arInvoice.getInvBillingHeader());
                            details.setIpInvBillingFooter(arInvoice.getInvBillingFooter());
                            details.setIpInvBillingHeader2(arInvoice.getInvBillingHeader2());
                            details.setIpInvBillingFooter2(arInvoice.getInvBillingFooter2());
                            details.setIpInvBillingHeader3(arInvoice.getInvBillingHeader3());
                            details.setIpInvBillingFooter3(arInvoice.getInvBillingFooter3());
                            details.setIpInvBillingSignatory(arInvoice.getInvBillingSignatory());
                            details.setIpInvSignatoryTitle(arInvoice.getInvSignatoryTitle());
                            details.setIpInvBillToAddress(arInvoice.getInvBillToAddress());
                            details.setIpInvBillToContact(arInvoice.getInvBillToContact());
                            details.setIpInvBillToAltContact(arInvoice.getInvBillToAltContact());
                            details.setIpInvBillToPhone(arInvoice.getInvBillToPhone());
                            details.setIpInvShipToAddress(arInvoice.getInvShipToAddress());
                            details.setIpInvShipToContact(arInvoice.getInvShipToContact());
                            details.setIpInvShipToAltContact(arInvoice.getInvShipToAltContact());
                            details.setIpInvShipToPhone(arInvoice.getInvShipToPhone());
                            details.setIpInvTotalTax(TOTAL_TAX);
                            details.setIpIlAmountWoVat(arInvoiceLine.getIlAmount());
                            details.setIpInvCstCustomerCode(arInvoice.getArCustomer().getCstCustomerCode());
                            details.setIpIlName(arInvoiceLine.getArStandardMemoLine().getSmlName());
                            details.setIpInvCustomerDescription(arInvoice.getArCustomer().getCstDescription());
                            details.setIpInvCustomerTin(arInvoice.getArCustomer().getCstTin());
                            details.setIpInvCustomerContactPerson(arInvoice.getArCustomer().getCstContact());
                            details.setIpInvCustomerPhoneNumber(arInvoice.getArCustomer().getCstPhone());
                            details.setIpInvCustomerMobileNumber(arInvoice.getArCustomer().getCstMobilePhone());
                            details.setIpInvCustomerFax(arInvoice.getArCustomer().getCstFax());
                            details.setIpInvCustomerEmail(arInvoice.getArCustomer().getCstEmail());
                            details.setIpInvIsDraft(arInvoice.getInvPosted() == EJBCommon.FALSE);

                            list.add(details);
                            // get unit price w/o VAT

                            short precisionUnit = this.getGlFcPrecisionUnit(AD_CMPNY);

                            double UNT_PRC = this.calculateIlNetUnitPrice(details, arInvoice.getArTaxCode().getTcRate(), arInvoice.getArTaxCode().getTcType(), precisionUnit);
                            details.setIpIlUnitPriceWoVat(UNT_PRC);

                            // add fields discount amount, discount desc, unit of measure, payment term, term desc

                            LocalArDistributionRecord arDistributionRecord = null;
                            try {

                                arDistributionRecord = arDistributionRecordHome.findByDrClassAndInvCode("DISCOUNT", arInvoice.getInvCode(), AD_CMPNY);

                                details.setIpInvDiscountAmount(arDistributionRecord.getDrAmount());
                                details.setIpInvDiscountDescription(arInvoice.getAdPaymentTerm().getPytDiscountDescription());

                            } catch (FinderException ex) {

                            }

                            details.setIpInvIlSmlUnitOfMeasure(arInvoiceLine.getArStandardMemoLine().getSmlUnitOfMeasure());

                            details.setIpInvPytName(arInvoice.getAdPaymentTerm().getPytName());
                            details.setIpInvPytDescription(arInvoice.getAdPaymentTerm().getPytDescription());

                            Collection arInvoicePaymentSchedules = arInvoice.getArInvoicePaymentSchedules();
                            ArrayList arInvoicePaymentScheduleList = new ArrayList(arInvoicePaymentSchedules);

                            LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) arInvoicePaymentScheduleList.get(arInvoicePaymentScheduleList.size() - 1);

                            details.setIpInvDueDate(arInvoicePaymentSchedule.getIpsDueDate());
                            details.setIpInvEffectivityDate(arInvoice.getInvEffectivityDate());

                            if (arInvoice.getArSalesperson() != null) {

                                details.setIpSlpSalespersonCode(arInvoice.getArSalesperson().getSlpSalespersonCode());
                                details.setIpSlpName(arInvoice.getArSalesperson().getSlpName());
                            }

                            details.setIpInvTcRate(arInvoice.getArTaxCode().getTcRate());
                            details.setIpInvTaxCode(arInvoice.getArTaxCode().getTcName());
                            details.setIpInvWithholdingTaxCode(arInvoice.getArWithholdingTaxCode().getWtcName());

                            // get withholding tax amount
                            double netAmount = EJBCommon.roundIt(arInvoice.getInvAmountDue() / (1 + (arInvoice.getArTaxCode().getTcRate() / 100)), this.getGlFcPrecisionUnit(AD_CMPNY));
                            details.setIpInvWithholdingTaxAmount(EJBCommon.roundIt(netAmount * (arInvoice.getArWithholdingTaxCode().getWtcRate() / 100), this.getGlFcPrecisionUnit(AD_CMPNY)));
                        }

                    } else if (!arInvoice.getArInvoiceLineItems().isEmpty()) {

                        Collection arInvoiceLineItems = null;

                        try {

                            arInvoiceLineItems = arInvoiceLineItemHome.findByInvCode(arInvoice.getInvCode(), AD_CMPNY);

                        } catch (FinderException ex) {

                        }

                        for (Object invoiceLineItem : arInvoiceLineItems) {

                            LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) invoiceLineItem;

                            ArRepInvoicePrintDetails details = new ArRepInvoicePrintDetails();

                            details.setIpInvType(arInvoice.getInvType());
                            details.setIpInvDescription(arInvoice.getInvDescription());
                            details.setIpInvDate(arInvoice.getInvDate());
                            details.setIpInvNumber(arInvoice.getInvNumber());
                            try {
                                details.setIpInvBatch(arInvoice.getArInvoiceBatch().getIbName());

                            } catch (Exception ex) {
                                details.setIpInvBatch("");
                            }

                            details.setIpInvCustomerName(arInvoice.getArCustomer().getCstName());
                            details.setIpInvCustomerAddress(arInvoice.getArCustomer().getCstAddress());
                            details.setIpInvCustomerCity(arInvoice.getArCustomer().getCstCity());
                            details.setIpInvCustomerCountry(arInvoice.getArCustomer().getCstCountry());
                            details.setIpInvCustomerDealPrice(arInvoice.getArCustomer().getCstDealPrice());
                            details.setIpIlDescription(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiDescription());
                            details.setIpIlQuantity(arInvoiceLineItem.getIliQuantity());
                            details.setIpInvIlSmlUnitOfMeasure(arInvoiceLineItem.getInvUnitOfMeasure().getUomName());

                            details.setIpIlUnitPrice(arInvoiceLineItem.getIliUnitPrice());
                            details.setIpIlAmount(arInvoiceLineItem.getIliAmount() + arInvoiceLineItem.getIliTaxAmount());
                            details.setIpIlIsTax(arInvoiceLineItem.getIliTax() == EJBCommon.TRUE ? "Y" : "N");
                            details.setIpInvCurrency(arInvoice.getGlFunctionalCurrency().getFcName());
                            details.setIpInvCurrencySymbol(arInvoice.getGlFunctionalCurrency().getFcSymbol());
                            details.setIpInvCurrencyDescription(arInvoice.getGlFunctionalCurrency().getFcDescription());
                            details.setIpInvReferenceNumber(arInvoice.getInvReferenceNumber());
                            details.setIpInvCreatedBy(arInvoice.getInvCreatedBy());
                            details.setIpInvApprovalStatus(arInvoice.getInvApprovalStatus());
                            details.setIpReportParameter(arInvoice.getReportParameter());

                            details.setIpInvIsDraft(arInvoice.getInvPosted() == EJBCommon.FALSE);

                            try {
                                LocalAdUser adUser = adUserHome.findByUsrName(arInvoice.getInvCreatedBy(), AD_CMPNY);
                                details.setIpInvCreatedByDesc(adUser.getUsrDescription());
                            } catch (FinderException ex) {
                            }

                            try {
                                Debug.print("adPreference.getPrfApDefaultChecker()(2): " + adPreference.getPrfApDefaultChecker());
                                LocalAdUser adUser2 = adUserHome.findByUsrName(adPreference.getPrfApDefaultChecker(), AD_CMPNY);
                                details.setIpInvCheckByDescription(adUser2.getUsrDescription());
                            } catch (Exception e) {
                                details.setIpInvCheckByDescription("");
                            }

                            try {
                                Debug.print("apCheck.getChkApprovedRejectedBy(2): " + arInvoice.getInvApprovedRejectedBy());
                                LocalAdUser adUser3 = adUserHome.findByUsrName(arInvoice.getInvApprovedRejectedBy(), AD_CMPNY);
                                details.setIpInvApprovedRejectedByDescription(adUser3.getUsrDescription());
                            } catch (Exception e) {
                                details.setIpInvApprovedRejectedByDescription("");
                            }

                            details.setIpInvApprovedRejectedBy(arInvoice.getInvApprovedRejectedBy());
                            details.setIpInvAmount(arInvoice.getInvAmountDue());
                            details.setIpInvAmountUnearnedInterest(arInvoice.getInvAmountUnearnedInterest());
                            details.setIpShowDuplicate(showDuplicate ? EJBCommon.TRUE : EJBCommon.FALSE);

                            // added fields
                            details.setIpInvBillingHeader(arInvoice.getInvBillingHeader());
                            details.setIpInvBillingFooter(arInvoice.getInvBillingFooter());
                            details.setIpInvBillingHeader2(arInvoice.getInvBillingHeader2());
                            details.setIpInvBillingFooter2(arInvoice.getInvBillingFooter2());
                            details.setIpInvBillingHeader3(arInvoice.getInvBillingHeader3());
                            details.setIpInvBillingFooter3(arInvoice.getInvBillingFooter3());
                            details.setIpInvBillingSignatory(arInvoice.getInvBillingSignatory());
                            details.setIpInvSignatoryTitle(arInvoice.getInvSignatoryTitle());
                            details.setIpInvBillToAddress(arInvoice.getInvBillToAddress());
                            details.setIpInvBillToContact(arInvoice.getInvBillToContact());
                            details.setIpInvBillToAltContact(arInvoice.getInvBillToAltContact());
                            details.setIpInvBillToPhone(arInvoice.getInvBillToPhone());
                            details.setIpInvShipToAddress(arInvoice.getInvShipToAddress());
                            details.setIpInvShipToContact(arInvoice.getInvShipToContact());
                            details.setIpInvShipToAltContact(arInvoice.getInvShipToAltContact());
                            details.setIpInvShipToPhone(arInvoice.getInvShipToPhone());
                            details.setIpIliClientPo(arInvoice.getInvClientPO());
                            details.setIpInvTotalTax(TOTAL_TAX);

                            details.setIpIlAmountWoVat(arInvoice.getArTaxCode().getTcName().contains("EXCLUSIVE") ? arInvoiceLineItem.getIliAmount() : arInvoiceLineItem.getIliAmount() + arInvoiceLineItem.getIliTaxAmount());
                            details.setIpIlCategory(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiAdLvCategory());
                            details.setIpInvCstCustomerCode(arInvoice.getArCustomer().getCstCustomerCode());
                            details.setIpIlName(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                            details.setIpIlUomShortName(arInvoiceLineItem.getInvUnitOfMeasure().getUomShortName());

                            details.setIpInvCustomerDescription(arInvoice.getArCustomer().getCstDescription());
                            details.setIpInvCustomerTin(arInvoice.getArCustomer().getCstTin());

                            details.setIpIliDiscount1(arInvoiceLineItem.getIliDiscount1());
                            details.setIpIliDiscount2(arInvoiceLineItem.getIliDiscount2());
                            details.setIpIliDiscount3(arInvoiceLineItem.getIliDiscount3());
                            details.setIpIliDiscount4(arInvoiceLineItem.getIliDiscount4());

                            details.setIpInvIsDraft(arInvoice.getInvPosted() == EJBCommon.FALSE);
                            // get unit price w/o VAT
                            short precisionUnit = this.getGlFcPrecisionUnit(AD_CMPNY);

                            double UNT_PRC = this.calculateIlNetUnitPrice(details, arInvoice.getArTaxCode().getTcRate(), arInvoice.getArTaxCode().getTcType(), precisionUnit);

                            details.setIpIlUnitPriceWoVat(UNT_PRC);

                            // add fields discount amount, discount desc, unit of measure, payment term, term desc

                            LocalArDistributionRecord arDistributionRecord = null;

                            try {

                                arDistributionRecord = arDistributionRecordHome.findByDrClassAndInvCode("DISCOUNT", arInvoice.getInvCode(), AD_CMPNY);

                                details.setIpInvDiscountAmount(arDistributionRecord.getDrAmount());
                                details.setIpInvDiscountDescription(arInvoice.getAdPaymentTerm().getPytDiscountDescription());

                            } catch (FinderException ex) {

                            }

                            details.setIpInvPytName(arInvoice.getAdPaymentTerm().getPytName());
                            details.setIpInvPytDescription(arInvoice.getAdPaymentTerm().getPytDescription());

                            Collection arInvoicePaymentSchedules = arInvoice.getArInvoicePaymentSchedules();
                            ArrayList arInvoicePaymentScheduleList = new ArrayList(arInvoicePaymentSchedules);

                            LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) arInvoicePaymentScheduleList.get(arInvoicePaymentScheduleList.size() - 1);

                            details.setIpInvDueDate(arInvoicePaymentSchedule.getIpsDueDate());
                            details.setIpInvEffectivityDate(arInvoice.getInvEffectivityDate());

                            if (arInvoice.getArSalesperson() != null) {

                                details.setIpSlpSalespersonCode(arInvoice.getArSalesperson().getSlpSalespersonCode());
                                details.setIpSlpName(arInvoice.getArSalesperson().getSlpName());
                            }

                            details.setIpInvTcRate(arInvoice.getArTaxCode().getTcRate());
                            details.setIpInvTaxCode(arInvoice.getArTaxCode().getTcName());
                            details.setIpInvWithholdingTaxCode(arInvoice.getArWithholdingTaxCode().getWtcName());

                            // get withholding tax amount
                            double netAmount = EJBCommon.roundIt(arInvoice.getInvAmountDue() / (1 + (arInvoice.getArTaxCode().getTcRate() / 100)), this.getGlFcPrecisionUnit(AD_CMPNY));
                            details.setIpInvWithholdingTaxAmount(EJBCommon.roundIt(netAmount * (arInvoice.getArWithholdingTaxCode().getWtcRate() / 100), this.getGlFcPrecisionUnit(AD_CMPNY)));
                            details.setIpIliTotalDiscount(arInvoiceLineItem.getIliTotalDiscount());

                            //                			Include Branch
                            LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(arInvoice.getInvAdBranch());
                            details.setIpInvBranchCode(adBranch.getBrBranchCode());
                            details.setIpInvBranchName(adBranch.getBrName());

                            details.setIpInvPartNumber(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiPartNumber());
                            Debug.print("Pasok1");

                            // trace misc
                            if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiTraceMisc() == EJBCommon.TRUE) {

                                if (arInvoiceLineItem.getIliMisc() != null && arInvoiceLineItem.getInvTags().size() <= 0) {
                                    Debug.print("old code");
                                    ArrayList miscArray = convertMiscToArrayList(arInvoiceLineItem.getIliMisc());
                                    // property code
                                    details.setIpIlItemPropertyCode(miscArray.get(0).toString());
                                    // serial number
                                    details.setIpIlItemSerialNumber(miscArray.get(1).toString());
                                    // specs
                                    details.setIpIlItemSpecs(miscArray.get(2).toString());
                                    // custodian
                                    details.setIpIlItemCustodian(miscArray.get(3).toString());
                                    // expiration date
                                    details.setIpIlItemExpiryDate(miscArray.get(4).toString());
                                }

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
                                            Debug.print("serial prob:" + invTag.getTgSerialNumber());
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
                                    details.setIpIlItemPropertyCode(strBProperty.toString());
                                    // serial number
                                    details.setIpIlItemSerialNumber(strBSerial.toString());
                                    // specs
                                    details.setIpIlItemSpecs(strBSpecs.toString());
                                    // custodian
                                    details.setIpIlItemCustodian(strBCustodian.toString());
                                    // expiration date
                                    details.setIpIlItemExpiryDate(strBExpirationDate.toString());

                                    Debug.print(strBSerial.toString());
                                    Debug.print(strBSpecs.toString());
                                }
                            }

                            list.add(details);
                        }

                    } else if (!arInvoice.getArSalesOrderInvoiceLines().isEmpty()) {

                        Collection arSalesOrderInvoiceLines = arInvoice.getArSalesOrderInvoiceLines();

                        for (Object salesOrderInvoiceLine : arSalesOrderInvoiceLines) {

                            LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) salesOrderInvoiceLine;
                            LocalArSalesOrderLine arSalesOrderLine = arSalesOrderInvoiceLine.getArSalesOrderLine();

                            ArRepInvoicePrintDetails details = new ArRepInvoicePrintDetails();

                            details.setIpInvType(arInvoice.getInvType());
                            details.setIpInvDescription(arInvoice.getInvDescription());
                            details.setIpInvDate(arInvoice.getInvDate());
                            details.setIpInvNumber(arInvoice.getInvNumber());
                            try {
                                details.setIpInvBatch(arInvoice.getArInvoiceBatch().getIbName());

                            } catch (Exception ex) {
                                details.setIpInvBatch("");
                            }
                            details.setIpInvCustomerName(arInvoice.getArCustomer().getCstName());
                            details.setIpInvCustomerAddress(arInvoice.getArCustomer().getCstAddress());
                            details.setIpInvCustomerCity(arInvoice.getArCustomer().getCstCity());
                            details.setIpIlCategory(arSalesOrderLine.getInvItemLocation().getInvItem().getIiAdLvCategory());
                            details.setIpInvCustomerCountry(arInvoice.getArCustomer().getCstCountry());
                            details.setIpInvCustomerDealPrice(arInvoice.getArCustomer().getCstDealPrice());
                            details.setIpIlDescription(arSalesOrderLine.getInvItemLocation().getInvItem().getIiDescription());
                            details.setIpIlQuantity(arSalesOrderInvoiceLine.getSilQuantityDelivered());

                            details.setIpInvIlSmlUnitOfMeasure(arSalesOrderLine.getInvUnitOfMeasure().getUomName());
                            details.setIpIlUnitPrice(arSalesOrderLine.getSolUnitPrice());
                            details.setIpIlAmount(arSalesOrderInvoiceLine.getSilAmount() + arSalesOrderInvoiceLine.getSilTaxAmount());

                            details.setIpIlIsTax(arSalesOrderInvoiceLine.getSilTax() == EJBCommon.TRUE ? "Y" : "N");
                            details.setIpInvCurrency(arInvoice.getGlFunctionalCurrency().getFcName());
                            details.setIpInvCurrencySymbol(arInvoice.getGlFunctionalCurrency().getFcSymbol());
                            details.setIpInvCurrencyDescription(arInvoice.getGlFunctionalCurrency().getFcDescription());
                            details.setIpInvReferenceNumber(arInvoice.getInvReferenceNumber());
                            details.setIpInvCreatedBy(arInvoice.getInvCreatedBy());
                            details.setIpInvApprovalStatus(arInvoice.getInvApprovalStatus());
                            details.setIpReportParameter(arInvoice.getReportParameter());

                            details.setIpInvIsDraft(arInvoice.getInvPosted() == EJBCommon.FALSE);
                            try {
                                LocalAdUser adUser = adUserHome.findByUsrName(arInvoice.getInvCreatedBy(), AD_CMPNY);
                                details.setIpInvCreatedByDesc(adUser.getUsrDescription());
                            } catch (FinderException ex) {
                            }
                            try {
                                Debug.print("adPreference.getPrfApDefaultChecker()(3): " + adPreference.getPrfApDefaultChecker());
                                LocalAdUser adUser2 = adUserHome.findByUsrName(adPreference.getPrfApDefaultChecker(), AD_CMPNY);
                                details.setIpInvCheckByDescription(adUser2.getUsrDescription());
                            } catch (Exception e) {
                                details.setIpInvCheckByDescription("");
                            }

                            try {
                                Debug.print("apCheck.getChkApprovedRejectedBy(3): " + arInvoice.getInvApprovedRejectedBy());
                                LocalAdUser adUser3 = adUserHome.findByUsrName(arInvoice.getInvApprovedRejectedBy(), AD_CMPNY);
                                details.setIpInvApprovedRejectedByDescription(adUser3.getUsrDescription());
                            } catch (Exception e) {
                                details.setIpInvApprovedRejectedByDescription("");
                            }

                            details.setIpInvApprovedRejectedBy(arInvoice.getInvApprovedRejectedBy());
                            details.setIpInvAmount(arInvoice.getInvAmountDue());
                            details.setIpInvAmountUnearnedInterest(arInvoice.getInvAmountUnearnedInterest());
                            details.setIpShowDuplicate(showDuplicate ? EJBCommon.TRUE : EJBCommon.FALSE);

                            // added fields
                            details.setIpInvBillingHeader(arInvoice.getInvBillingHeader());
                            details.setIpInvBillingFooter(arInvoice.getInvBillingFooter());
                            details.setIpInvBillingHeader2(arInvoice.getInvBillingHeader2());
                            details.setIpInvBillingFooter2(arInvoice.getInvBillingFooter2());
                            details.setIpInvBillingSignatory(arInvoice.getInvBillingSignatory());
                            details.setIpInvSignatoryTitle(arInvoice.getInvSignatoryTitle());
                            details.setIpInvBillToAddress(arInvoice.getInvBillToAddress());
                            details.setIpInvBillToContact(arInvoice.getInvBillToContact());
                            details.setIpInvBillToAltContact(arInvoice.getInvBillToAltContact());
                            details.setIpInvBillToPhone(arInvoice.getInvBillToPhone());
                            details.setIpInvShipToAddress(arInvoice.getInvShipToAddress());
                            details.setIpInvShipToContact(arInvoice.getInvShipToContact());
                            details.setIpInvShipToAltContact(arInvoice.getInvShipToAltContact());
                            details.setIpInvShipToPhone(arInvoice.getInvShipToPhone());
                            details.setIpIliClientPo(arInvoice.getInvClientPO());
                            details.setIpInvTotalTax(TOTAL_TAX);

                            details.setIpIlAmountWoVat(arInvoice.getArTaxCode().getTcName().contains("EXCLUSIVE") ? arSalesOrderInvoiceLine.getSilAmount() : arSalesOrderInvoiceLine.getSilAmount() + arSalesOrderInvoiceLine.getSilTaxAmount());

                            details.setIpInvCstCustomerCode(arInvoice.getArCustomer().getCstCustomerCode());
                            details.setIpIlName(arSalesOrderLine.getInvItemLocation().getInvItem().getIiName());
                            details.setIpIlUomShortName(arSalesOrderLine.getInvUnitOfMeasure().getUomShortName());

                            details.setIpInvCustomerDescription(arInvoice.getArCustomer().getCstDescription());
                            details.setIpInvCustomerTin(arInvoice.getArCustomer().getCstTin());

                            // add fields discount amount, discount desc, unit of measure, payment term, term desc

                            LocalArDistributionRecord arDistributionRecord = null;

                            try {

                                arDistributionRecord = arDistributionRecordHome.findByDrClassAndInvCode("DISCOUNT", arInvoice.getInvCode(), AD_CMPNY);

                                details.setIpInvDiscountAmount(arDistributionRecord.getDrAmount());
                                details.setIpInvDiscountDescription(arInvoice.getAdPaymentTerm().getPytDiscountDescription());

                            } catch (FinderException ex) {

                            }

                            details.setIpInvPytName(arInvoice.getAdPaymentTerm().getPytName());
                            details.setIpInvPytDescription(arInvoice.getAdPaymentTerm().getPytDescription());

                            Collection arInvoicePaymentSchedules = arInvoice.getArInvoicePaymentSchedules();
                            ArrayList arInvoicePaymentScheduleList = new ArrayList(arInvoicePaymentSchedules);

                            LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) arInvoicePaymentScheduleList.get(arInvoicePaymentScheduleList.size() - 1);

                            details.setIpInvDueDate(arInvoicePaymentSchedule.getIpsDueDate());
                            details.setIpInvEffectivityDate(arInvoice.getInvEffectivityDate());

                            if (arInvoice.getArSalesperson() != null) {

                                details.setIpSlpSalespersonCode(arInvoice.getArSalesperson().getSlpSalespersonCode());
                                details.setIpSlpName(arInvoice.getArSalesperson().getSlpName());
                            }

                            details.setIpInvSoNumber(arInvoice.getInvSoNumber());
                            details.setIpInvJoNumber(arInvoice.getInvJoNumber());

                            // get unit price w/o VAT

                            short precisionUnit = this.getGlFcPrecisionUnit(AD_CMPNY);

                            double UNT_PRC = this.calculateIlNetUnitPrice(details, arInvoice.getArTaxCode().getTcRate(), arInvoice.getArTaxCode().getTcType(), precisionUnit);

                            details.setIpIlUnitPriceWoVat(UNT_PRC);

                            details.setIpIliDiscount1(arSalesOrderInvoiceLine.getSilDiscount1());
                            details.setIpIliDiscount2(arSalesOrderInvoiceLine.getSilDiscount2());
                            details.setIpIliDiscount3(arSalesOrderInvoiceLine.getSilDiscount3());
                            details.setIpIliDiscount4(arSalesOrderInvoiceLine.getSilDiscount4());

                            details.setIpInvTcRate(arInvoice.getArTaxCode().getTcRate());
                            details.setIpInvTaxCode(arInvoice.getArTaxCode().getTcName());
                            details.setIpInvWithholdingTaxCode(arInvoice.getArWithholdingTaxCode().getWtcName());

                            // get withholding tax amount
                            double netAmount = EJBCommon.roundIt(arInvoice.getInvAmountDue() / (1 + (arInvoice.getArTaxCode().getTcRate() / 100)), this.getGlFcPrecisionUnit(AD_CMPNY));
                            details.setIpInvWithholdingTaxAmount(EJBCommon.roundIt(netAmount * (arInvoice.getArWithholdingTaxCode().getWtcRate() / 100), this.getGlFcPrecisionUnit(AD_CMPNY)));

                            details.setIpIliTotalDiscount(arSalesOrderInvoiceLine.getSilTotalDiscount());

                            //                			Include Branch
                            LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(arInvoice.getInvAdBranch());
                            details.setIpInvBranchCode(adBranch.getBrBranchCode());
                            details.setIpInvBranchName(adBranch.getBrName());

                            Debug.print("Pasok2");
                            details.setIpInvPartNumber(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvItem().getIiPartNumber());

                            details.setIpInvSoReferenceNumber(arSalesOrderInvoiceLine.getArSalesOrderLine().getArSalesOrder().getSoReferenceNumber());
                            details.setIpInvIsDraft(arInvoice.getInvPosted() == EJBCommon.FALSE);

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
                                    details.setIpIlItemPropertyCode(strBProperty.toString());
                                    // serial number
                                    details.setIpIlItemSerialNumber(strBSerial.toString());
                                    // specs
                                    details.setIpIlItemSpecs(strBSpecs.toString());
                                    // custodian
                                    details.setIpIlItemCustodian(strBCustodian.toString());
                                    // expiration date
                                    details.setIpIlItemExpiryDate(strBExpirationDate.toString());
                                }
                            }

                            list.add(details);
                        }

                    } else if (!arInvoice.getArJobOrderInvoiceLines().isEmpty()) {

                        Collection arJobOrderInvoiceLines = arInvoice.getArJobOrderInvoiceLines();

                        for (Object jobOrderInvoiceLine : arJobOrderInvoiceLines) {

                            LocalArJobOrderInvoiceLine arJobOrderInvoiceLine = (LocalArJobOrderInvoiceLine) jobOrderInvoiceLine;
                            LocalArJobOrderLine arJobOrderLine = arJobOrderInvoiceLine.getArJobOrderLine();

                            ArRepInvoicePrintDetails details = new ArRepInvoicePrintDetails();

                            details.setIpInvType(arInvoice.getInvType());
                            details.setIpInvDescription(arInvoice.getInvDescription());
                            details.setIpInvDate(arInvoice.getInvDate());
                            details.setIpInvNumber(arInvoice.getInvNumber());
                            try {
                                details.setIpInvBatch(arInvoice.getArInvoiceBatch().getIbName());

                            } catch (Exception ex) {
                                details.setIpInvBatch("");
                            }
                            details.setIpInvCustomerName(arInvoice.getArCustomer().getCstName());
                            details.setIpInvCustomerAddress(arInvoice.getArCustomer().getCstAddress());
                            details.setIpInvCustomerCity(arInvoice.getArCustomer().getCstCity());
                            details.setIpIlCategory(arJobOrderLine.getInvItemLocation().getInvItem().getIiAdLvCategory());
                            details.setIpInvCustomerCountry(arInvoice.getArCustomer().getCstCountry());
                            details.setIpInvCustomerDealPrice(arInvoice.getArCustomer().getCstDealPrice());
                            details.setIpIlDescription(arJobOrderLine.getInvItemLocation().getInvItem().getIiDescription());
                            details.setIpIlQuantity(arJobOrderInvoiceLine.getJilQuantityDelivered());

                            details.setIpInvIlSmlUnitOfMeasure(arJobOrderLine.getInvUnitOfMeasure().getUomName());
                            details.setIpIlUnitPrice(arJobOrderLine.getJolUnitPrice());
                            details.setIpIlAmount(arJobOrderInvoiceLine.getJilAmount() + arJobOrderInvoiceLine.getJilTaxAmount());

                            details.setIpIlIsTax(arJobOrderInvoiceLine.getJilTax() == EJBCommon.TRUE ? "Y" : "N");
                            details.setIpInvCurrency(arInvoice.getGlFunctionalCurrency().getFcName());
                            details.setIpInvCurrencySymbol(arInvoice.getGlFunctionalCurrency().getFcSymbol());
                            details.setIpInvCurrencyDescription(arInvoice.getGlFunctionalCurrency().getFcDescription());
                            details.setIpInvReferenceNumber(arInvoice.getInvReferenceNumber());
                            details.setIpInvCreatedBy(arInvoice.getInvCreatedBy());
                            details.setIpInvApprovalStatus(arInvoice.getInvApprovalStatus());
                            details.setIpReportParameter(arInvoice.getReportParameter());

                            details.setIpInvIsDraft(arInvoice.getInvPosted() == EJBCommon.FALSE);
                            try {
                                LocalAdUser adUser = adUserHome.findByUsrName(arInvoice.getInvCreatedBy(), AD_CMPNY);
                                details.setIpInvCreatedByDesc(adUser.getUsrDescription());
                            } catch (FinderException ex) {
                            }
                            try {
                                Debug.print("adPreference.getPrfApDefaultChecker()(3): " + adPreference.getPrfApDefaultChecker());
                                LocalAdUser adUser2 = adUserHome.findByUsrName(adPreference.getPrfApDefaultChecker(), AD_CMPNY);
                                details.setIpInvCheckByDescription(adUser2.getUsrDescription());
                            } catch (Exception e) {
                                details.setIpInvCheckByDescription("");
                            }

                            try {
                                Debug.print("apCheck.getChkApprovedRejectedBy(3): " + arInvoice.getInvApprovedRejectedBy());
                                LocalAdUser adUser3 = adUserHome.findByUsrName(arInvoice.getInvApprovedRejectedBy(), AD_CMPNY);
                                details.setIpInvApprovedRejectedByDescription(adUser3.getUsrDescription());
                            } catch (Exception e) {
                                details.setIpInvApprovedRejectedByDescription("");
                            }

                            details.setIpInvApprovedRejectedBy(arInvoice.getInvApprovedRejectedBy());
                            details.setIpInvAmount(arInvoice.getInvAmountDue());
                            details.setIpInvAmountUnearnedInterest(arInvoice.getInvAmountUnearnedInterest());
                            details.setIpShowDuplicate(showDuplicate ? EJBCommon.TRUE : EJBCommon.FALSE);

                            // added fields
                            details.setIpInvBillingHeader(arInvoice.getInvBillingHeader());
                            details.setIpInvBillingFooter(arInvoice.getInvBillingFooter());
                            details.setIpInvBillingHeader2(arInvoice.getInvBillingHeader2());
                            details.setIpInvBillingFooter2(arInvoice.getInvBillingFooter2());
                            details.setIpInvBillingSignatory(arInvoice.getInvBillingSignatory());
                            details.setIpInvSignatoryTitle(arInvoice.getInvSignatoryTitle());
                            details.setIpInvBillToAddress(arInvoice.getInvBillToAddress());
                            details.setIpInvBillToContact(arInvoice.getInvBillToContact());
                            details.setIpInvBillToAltContact(arInvoice.getInvBillToAltContact());
                            details.setIpInvBillToPhone(arInvoice.getInvBillToPhone());
                            details.setIpInvShipToAddress(arInvoice.getInvShipToAddress());
                            details.setIpInvShipToContact(arInvoice.getInvShipToContact());
                            details.setIpInvShipToAltContact(arInvoice.getInvShipToAltContact());
                            details.setIpInvShipToPhone(arInvoice.getInvShipToPhone());
                            details.setIpIliClientPo(arInvoice.getInvClientPO());
                            details.setIpInvTotalTax(TOTAL_TAX);

                            details.setIpIlAmountWoVat(arInvoice.getArTaxCode().getTcName().contains("EXCLUSIVE") ? arJobOrderInvoiceLine.getJilAmount() : arJobOrderInvoiceLine.getJilAmount() + arJobOrderInvoiceLine.getJilTaxAmount());

                            details.setIpInvCstCustomerCode(arInvoice.getArCustomer().getCstCustomerCode());
                            details.setIpIlName(arJobOrderLine.getInvItemLocation().getInvItem().getIiName());
                            details.setIpIlUomShortName(arJobOrderLine.getInvUnitOfMeasure().getUomShortName());

                            details.setIpInvCustomerDescription(arInvoice.getArCustomer().getCstDescription());
                            details.setIpInvCustomerTin(arInvoice.getArCustomer().getCstTin());

                            // add fields discount amount, discount desc, unit of measure, payment term, term desc

                            LocalArDistributionRecord arDistributionRecord = null;

                            try {

                                arDistributionRecord = arDistributionRecordHome.findByDrClassAndInvCode("DISCOUNT", arInvoice.getInvCode(), AD_CMPNY);

                                details.setIpInvDiscountAmount(arDistributionRecord.getDrAmount());
                                details.setIpInvDiscountDescription(arInvoice.getAdPaymentTerm().getPytDiscountDescription());

                            } catch (FinderException ex) {

                            }

                            details.setIpInvPytName(arInvoice.getAdPaymentTerm().getPytName());
                            details.setIpInvPytDescription(arInvoice.getAdPaymentTerm().getPytDescription());

                            Collection arInvoicePaymentSchedules = arInvoice.getArInvoicePaymentSchedules();
                            ArrayList arInvoicePaymentScheduleList = new ArrayList(arInvoicePaymentSchedules);

                            LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) arInvoicePaymentScheduleList.get(arInvoicePaymentScheduleList.size() - 1);

                            details.setIpInvDueDate(arInvoicePaymentSchedule.getIpsDueDate());
                            details.setIpInvEffectivityDate(arInvoice.getInvEffectivityDate());

                            if (arInvoice.getArSalesperson() != null) {

                                details.setIpSlpSalespersonCode(arInvoice.getArSalesperson().getSlpSalespersonCode());
                                details.setIpSlpName(arInvoice.getArSalesperson().getSlpName());
                            }

                            details.setIpInvSoNumber(arInvoice.getInvSoNumber());
                            details.setIpInvJoNumber(arInvoice.getInvJoNumber());

                            // get unit price w/o VAT

                            short precisionUnit = this.getGlFcPrecisionUnit(AD_CMPNY);

                            double UNT_PRC = this.calculateIlNetUnitPrice(details, arInvoice.getArTaxCode().getTcRate(), arInvoice.getArTaxCode().getTcType(), precisionUnit);

                            details.setIpIlUnitPriceWoVat(UNT_PRC);

                            details.setIpIliDiscount1(arJobOrderInvoiceLine.getJilDiscount1());
                            details.setIpIliDiscount2(arJobOrderInvoiceLine.getJilDiscount2());
                            details.setIpIliDiscount3(arJobOrderInvoiceLine.getJilDiscount3());
                            details.setIpIliDiscount4(arJobOrderInvoiceLine.getJilDiscount4());

                            details.setIpInvTcRate(arInvoice.getArTaxCode().getTcRate());
                            details.setIpInvTaxCode(arInvoice.getArTaxCode().getTcName());
                            details.setIpInvWithholdingTaxCode(arInvoice.getArWithholdingTaxCode().getWtcName());

                            // get withholding tax amount
                            double netAmount = EJBCommon.roundIt(arInvoice.getInvAmountDue() / (1 + (arInvoice.getArTaxCode().getTcRate() / 100)), this.getGlFcPrecisionUnit(AD_CMPNY));
                            details.setIpInvWithholdingTaxAmount(EJBCommon.roundIt(netAmount * (arInvoice.getArWithholdingTaxCode().getWtcRate() / 100), this.getGlFcPrecisionUnit(AD_CMPNY)));

                            details.setIpIliTotalDiscount(arJobOrderInvoiceLine.getJilTotalDiscount());

                            //                			Include Branch
                            LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(arInvoice.getInvAdBranch());
                            details.setIpInvBranchCode(adBranch.getBrBranchCode());
                            details.setIpInvBranchName(adBranch.getBrName());

                            Debug.print("Pasok2");
                            details.setIpInvPartNumber(arJobOrderInvoiceLine.getArJobOrderLine().getInvItemLocation().getInvItem().getIiPartNumber());

                            details.setIpInvSoReferenceNumber(arJobOrderInvoiceLine.getArJobOrderLine().getArJobOrder().getJoReferenceNumber());

                            details.setIpInvIsDraft(arInvoice.getInvPosted() == EJBCommon.FALSE);

                            details.setIpJaQuantity(0);

                            // trace misc
                            if (arJobOrderInvoiceLine.getArJobOrderLine().getInvItemLocation().getInvItem().getIiTraceMisc() == EJBCommon.TRUE) {

                                if (arJobOrderInvoiceLine.getInvTags().size() > 0) {
                                    Debug.print("new code");
                                    StringBuilder strBProperty = new StringBuilder();
                                    StringBuilder strBSerial = new StringBuilder();
                                    StringBuilder strBSpecs = new StringBuilder();
                                    StringBuilder strBCustodian = new StringBuilder();
                                    StringBuilder strBExpirationDate = new StringBuilder();

                                    for (Object o : arJobOrderInvoiceLine.getInvTags()) {

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
                                    details.setIpIlItemPropertyCode(strBProperty.toString());
                                    // serial number
                                    details.setIpIlItemSerialNumber(strBSerial.toString());
                                    // specs
                                    details.setIpIlItemSpecs(strBSpecs.toString());
                                    // custodian
                                    details.setIpIlItemCustodian(strBCustodian.toString());
                                    // expiration date
                                    details.setIpIlItemExpiryDate(strBExpirationDate.toString());
                                }
                            }

                            details.setIpIiJobServices(arJobOrderInvoiceLine.getArJobOrderLine().getInvItemLocation().getInvItem().getIiJobServices());

                            if (arJobOrderInvoiceLine.getArJobOrderLine().getArJobOrderAssignments().size() > 0) {

                                for (Object o : arJobOrderInvoiceLine.getArJobOrderLine().getArJobOrderAssignments()) {

                                    LocalArJobOrderAssignment jobOrderAssignment = (LocalArJobOrderAssignment) o;

                                    ArRepInvoicePrintDetails details2 = (ArRepInvoicePrintDetails) details.clone();

                                    if (jobOrderAssignment.getJaSo() == EJBCommon.FALSE) {

                                        continue;
                                    }

                                    details2.setIpJaLine(jobOrderAssignment.getArJobOrderLine().toString());
                                    details2.setIpJaIdNumber(jobOrderAssignment.getArPersonel().getPeIdNumber());
                                    details2.setIpJaPersonelName(jobOrderAssignment.getArPersonel().getPeName());
                                    details2.setIpJaQuantity(jobOrderAssignment.getJaQuantity());
                                    details2.setIpJaUnitCost(jobOrderAssignment.getJaUnitCost());
                                    details2.setIpJaAmount(jobOrderAssignment.getJaAmount());
                                    details2.setIpJaRemarks(jobOrderAssignment.getJaRemarks());
                                    details2.setIpJaSo(jobOrderAssignment.getJaSo());

                                    list.add(details2);
                                }

                            } else {

                                list.add(details);
                            }
                        }
                    }

                } else {

                    // ar payment schedule data source

                    if (!arInvoice.getArInvoicePaymentSchedules().isEmpty()) {

                        Collection arInvoicePaymentSchedules = arInvoice.getArInvoicePaymentSchedules();

                        for (Object invoicePaymentSchedule : arInvoicePaymentSchedules) {

                            LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) invoicePaymentSchedule;
                            LocalAdUser adUser = adUserHome.findByUsrName(arInvoice.getInvCreatedBy(), AD_CMPNY);

                            ArRepInvoicePrintDetails details = new ArRepInvoicePrintDetails();

                            details.setIpInvCstCustomerCode(arInvoice.getArCustomer().getCstCustomerCode());
                            details.setIpInvNumber(arInvoice.getInvNumber());
                            try {
                                details.setIpInvBatch(arInvoice.getArInvoiceBatch().getIbName());

                            } catch (Exception ex) {
                                details.setIpInvBatch("");
                            }
                            details.setIpInvCustomerAddress(arInvoice.getArCustomer().getCstAddress());
                            details.setIpInvCustomerCity(arInvoice.getArCustomer().getCstCity());
                            details.setIpInvBillToContact(arInvoice.getInvBillToContact());
                            details.setIpInvBillToAddress(arInvoice.getInvBillToAddress());
                            details.setIpInvPytName(arInvoice.getAdPaymentTerm().getPytName());
                            details.setIpInvCreatedBy(adUser.getUsrDescription());

                            details.setIpInvIsDraft(arInvoice.getInvPosted() == EJBCommon.FALSE);

                            try {
                                LocalAdUser adUser2 = adUserHome.findByUsrName(arInvoice.getInvCreatedBy(), AD_CMPNY);
                                details.setIpInvCreatedByDesc(adUser2.getUsrDescription());
                            } catch (FinderException ex) {
                            }
                            try {
                                Debug.print("adPreference.getPrfApDefaultChecker()(4): " + adPreference.getPrfApDefaultChecker());
                                LocalAdUser adUser2 = adUserHome.findByUsrName(adPreference.getPrfApDefaultChecker(), AD_CMPNY);
                                details.setIpInvCheckByDescription(adUser2.getUsrDescription());
                            } catch (Exception e) {
                                details.setIpInvCheckByDescription("");
                            }

                            try {
                                Debug.print("apCheck.getChkApprovedRejectedBy(4): " + arInvoice.getInvApprovedRejectedBy());
                                LocalAdUser adUser3 = adUserHome.findByUsrName(arInvoice.getInvApprovedRejectedBy(), AD_CMPNY);
                                details.setIpInvApprovedRejectedByDescription(adUser3.getUsrDescription());
                            } catch (Exception e) {
                                details.setIpInvApprovedRejectedByDescription("");
                            }
                            details.setIpInvAmount(arInvoice.getInvAmountPaid());
                            details.setIpIpsDueDate(arInvoicePaymentSchedule.getIpsDueDate());
                            details.setIpIpsAmountDue(arInvoicePaymentSchedule.getIpsAmountDue());

                            // Debug.print("Pasok3");
                            list.add(details);
                        }
                    }
                }
            }

            if (list.isEmpty()) {
                Debug.print("throw new GlobalNoRecordFoundException()");

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

    public byte getInvDebitMemo(ArrayList invCodeList, Integer AD_CMPNY) {

        Debug.print("ArRepInvoicePrintControllerBean getInvDebitMemo");
        byte debitMemo = 0;
        for (Object o : invCodeList) {

            Integer INV_CODE = (Integer) o;

            LocalArInvoice arInvoice = null;

            try {

                arInvoice = arInvoiceHome.findByPrimaryKey(INV_CODE);

            } catch (FinderException ex) {

                continue;
            }

            debitMemo = arInvoice.getInvDebitMemo();
            break;
        }

        return debitMemo;
    }

    public ArrayList executeArRepInvoicePrintSub(ArrayList invCodeList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepInvoicePrintControllerBean executeArRepInvoicePrintSub");

        ArrayList list = new ArrayList();

        try {

            for (Object o : invCodeList) {

                Integer INV_CODE = (Integer) o;

                LocalArInvoice arInvoice = null;

                Debug.print("INV_CODE=" + INV_CODE);
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

                // set printed

                arInvoice.setInvPrinted(EJBCommon.TRUE);

                // get invoice lines

                if (!arInvoice.getArInvoiceLineItems().isEmpty()) {

                    Collection adLookupValues = null;

                    try {

                        adLookupValues = adLookUpValueHome.findByLuName("INV ITEM CATEGORY", AD_CMPNY);

                    } catch (FinderException ex) {

                    }

                    for (Object adLookupValue : adLookupValues) {

                        LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) adLookupValue;
                        String CTGRY_NM = adLookUpValue.getLvName();

                        Collection arInvoiceLineItems = null;

                        try {

                            arInvoiceLineItems = arInvoiceLineItemHome.findByInvCodeAndIlIiLvCtgry(arInvoice.getInvCode(), CTGRY_NM, AD_CMPNY);

                        } catch (FinderException ex) {

                        }

                        // calculate total amount per item category

                        double TOTAL_PER_CATEGORY = 0d;

                        for (Object invoiceLineItem : arInvoiceLineItems) {

                            LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) invoiceLineItem;

                            TOTAL_PER_CATEGORY += arInvoiceLineItem.getIliAmount();
                        }

                        ArRepInvoicePrintDetails details = new ArRepInvoicePrintDetails();

                        details.setIpIlCategory(CTGRY_NM);
                        details.setIpIlAmount(TOTAL_PER_CATEGORY);
                        details.setIpInvDate(arInvoice.getInvDate());
                        list.add(details);
                    }
                }
            }

            if (list.isEmpty()) {

                Debug.print("executeArRepInvoicePrintSub throw new GlobalNoRecordFoundException()");
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

    public ArrayList executeArRepInvoicePrintSubDr(ArrayList invCodeList, Integer AD_CMPNY) {

        Debug.print("ArRepInvoicePrintControllerBean executeArRepInvoicePrintSubDr");

        ArrayList list = new ArrayList();

        try {

            for (Object o : invCodeList) {

                Integer INV_CODE = (Integer) o;

                LocalArInvoice arInvoice = null;

                try {

                    arInvoice = arInvoiceHome.findByPrimaryKey(INV_CODE);

                } catch (FinderException ex) {

                    continue;
                }

                // set printed

                arInvoice.setInvPrinted(EJBCommon.TRUE);

                // get invoice distribution records

                Collection arDistributionRecords = arInvoice.getArDistributionRecords();

                for (Object distributionRecord : arDistributionRecords) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                    ArRepInvoicePrintDetails details = new ArRepInvoicePrintDetails();

                    details.setIpDrCoaAccountNumber(arDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                    details.setIpDrAmount(arDistributionRecord.getDrAmount());
                    details.setIpDrDebit(arDistributionRecord.getDrDebit());

                    list.add(details);
                }
            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("ArRepInvoicePrintControllerBean getAdCompany");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());
            details.setCmpAddress(adCompany.getCmpAddress());
            details.setCmpCity(adCompany.getCmpCity());
            details.setCmpCountry(adCompany.getCmpCountry());
            details.setCmpTin(adCompany.getCmpTin());
            details.setCmpPhone(adCompany.getCmpPhone());
            details.setCmpFax(adCompany.getCmpFax());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public String getPrfArSalesInvoiceDataSource(Integer AD_CMPNY) {

        Debug.print("ArRepInvoicePrintControllerBean getArSalesInvoiceDataSource");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfArSalesInvoiceDataSource();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods

    private double calculateIlNetUnitPrice(ArRepInvoicePrintDetails mdetails, double tcRate, String tcType, short precisionUnit) {

        double amount = mdetails.getIpIlUnitPrice();

        if (tcType.equals("INCLUSIVE")) {

            amount = EJBCommon.roundIt(mdetails.getIpIlUnitPrice() / (1 + (tcRate / 100)), precisionUnit);
        }

        return amount;
    }

    private short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods
    private ArrayList convertMiscToArrayList(String misc) {

        Debug.print("ArRepSalesOrderControllerBean convertMiscToArrayList");

        ArrayList list = new ArrayList();

        if (misc == null) {
            return list;
        }
        // String[] propertyCode, serialNumber, specs, custodian, expiryDate, tgDocumentNumber;
        String propertyCode = "";
        String serialNumber = "";
        String specs = "";
        String custodian = "";
        String expiryDate = "";
        String tgDocumentNumber = "";

        StringBuilder strB = new StringBuilder();

        // quantity
        String[] arrayMisc = misc.split("_");

        // property code
        misc = arrayMisc[1];
        arrayMisc = misc.split("@");
        String[] valueList = arrayMisc[0].split(",");
        strB = new StringBuilder();
        for (String s1 : valueList) {
            if (!s1.trim().equals("")) {
                strB.append(s1);
                strB.append(System.getProperty("line.separator"));
            }
        }
        propertyCode = strB.toString();
        list.add(propertyCode);

        // serialNumber
        misc = arrayMisc[1];
        arrayMisc = misc.split("<");
        valueList = arrayMisc[0].split(",");
        strB = new StringBuilder();
        for (String element : valueList) {
            if (!element.trim().equals("")) {
                strB.append(element);
                strB.append(System.getProperty("line.separator"));
            }
        }
        serialNumber = strB.toString();
        Debug.print("serial no: " + serialNumber);
        list.add(serialNumber);

        // specs
        misc = arrayMisc[1];
        arrayMisc = misc.split(">");
        valueList = arrayMisc[0].split(",");
        strB = new StringBuilder();
        for (String item : valueList) {
            if (!item.trim().equals("")) {
                strB.append(item);
                strB.append(System.getProperty("line.separator"));
            }
        }
        specs = strB.toString();

        list.add(specs);

        // custodian
        misc = arrayMisc[1];
        arrayMisc = misc.split(";");
        valueList = arrayMisc[0].split(",");
        strB = new StringBuilder();
        for (String value : valueList) {
            if (!value.trim().equals("")) {
                strB.append(value);
                strB.append(System.getProperty("line.separator"));
            }
        }
        custodian = strB.toString();
        list.add(custodian);

        // expiryDate

        misc = arrayMisc[1];
        arrayMisc = misc.split("%");
        valueList = arrayMisc[0].split(",");
        strB = new StringBuilder();
        for (String s : valueList) {
            if (!s.trim().equals("")) {
                strB.append(s);
                strB.append(System.getProperty("line.separator"));
            }
        }
        expiryDate = strB.toString();
        list.add(expiryDate);

        return list;
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ArRepInvoicePrintControllerBean ejbCreate");
    }

}