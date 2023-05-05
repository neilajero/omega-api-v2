package com.ejb.txnreports.ap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.entities.ad.LocalAdApproval;
import com.ejb.entities.ad.LocalAdApprovalDocument;
import com.ejb.dao.ad.LocalAdApprovalDocumentHome;
import com.ejb.dao.ad.LocalAdApprovalHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ad.LocalAdUser;
import com.ejb.dao.ad.LocalAdUserHome;
import com.ejb.entities.ap.LocalApDistributionRecord;
import com.ejb.dao.ap.LocalApDistributionRecordHome;
import com.ejb.dao.ap.LocalApPurchaseOrderHome;
import com.ejb.entities.ap.LocalApPurchaseOrderLine;
import com.ejb.dao.ap.LocalApPurchaseOrderLineHome;
import com.ejb.entities.ap.LocalApTaxCode;
import com.ejb.entities.ap.LocalApVoucher;
import com.ejb.dao.ap.LocalApVoucherHome;
import com.ejb.entities.ap.LocalApVoucherLineItem;
import com.ejb.entities.ap.LocalApVoucherPaymentSchedule;
import com.ejb.exception.gen.GenVSVNoValueSetValueFoundException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.gen.LocalGenField;
import com.ejb.entities.gen.LocalGenSegment;
import com.ejb.dao.gen.LocalGenSegmentHome;
import com.ejb.entities.gen.LocalGenValueSet;
import com.ejb.entities.gen.LocalGenValueSetValue;
import com.ejb.dao.gen.LocalGenValueSetValueHome;
import com.ejb.entities.inv.LocalInvTag;
import com.ejb.dao.inv.LocalInvTagHome;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ap.ApRepVoucherPrintDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;

@Stateless(name = "ApRepVoucherPrintControllerEJB")
public class ApRepVoucherPrintControllerBean extends EJBContextClass implements ApRepVoucherPrintController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalGenValueSetValueHome genValueSetValueHome;
    @EJB
    private LocalGenSegmentHome genSegmentHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdApprovalDocumentHome adApprovalDocumentHome;
    @EJB
    private LocalApVoucherHome apVoucherHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    private LocalApPurchaseOrderHome apPurchaseOrderHome;
    @EJB
    private LocalApPurchaseOrderLineHome apPurchaseOrderLineHome;
    @EJB
    private LocalInvTagHome invTagHome;


    public ArrayList executeApRepVoucherPrint(ArrayList vouCodeList, Integer AD_CMPNY) throws GlobalNoRecordFoundException, GenVSVNoValueSetValueFoundException {

        Debug.print("ApRepVoucherPrintControllerBean executeApRepVoucherPrint");

        ArrayList list = new ArrayList();


        try {

            for (Object value : vouCodeList) {

                Integer VOU_CODE = (Integer) value;

                LocalApVoucher apVoucher = null;

                try {

                    apVoucher = apVoucherHome.findByPrimaryKey(VOU_CODE);

                } catch (FinderException ex) {

                    continue;
                }

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(AD_CMPNY);
                LocalAdApprovalDocument adApprovalDocument = adApprovalDocumentHome.findByAdcType("AP VOUCHER", AD_CMPNY);

                if (adApprovalDocument.getAdcPrintOption().equals("PRINT APPROVED ONLY")) {

                    if (apVoucher.getVouApprovalStatus() == null || apVoucher.getVouApprovalStatus().equals("PENDING")) {

                        continue;
                    }

                } else if (adApprovalDocument.getAdcPrintOption().equals("PRINT UNAPPROVED ONLY")) {

                    if (apVoucher.getVouApprovalStatus() != null && (apVoucher.getVouApprovalStatus().equals("N/A") || apVoucher.getVouApprovalStatus().equals("APPROVED"))) {

                        continue;
                    }
                }

                if (adApprovalDocument.getAdcAllowDuplicate() == EJBCommon.FALSE && apVoucher.getVouPrinted() == EJBCommon.TRUE) {

                    continue;
                }

                // show duplicate

                boolean showDuplicate = adApprovalDocument.getAdcTrackDuplicate() == EJBCommon.TRUE && apVoucher.getVouPrinted() == EJBCommon.TRUE;

                // set printed

                apVoucher.setVouPrinted(EJBCommon.TRUE);

                String voucherType = apVoucher.getVouType();

                if (voucherType.equals("ITEMS")) {

                    // get voucher line items

                    Collection voucherLineItems = apVoucher.getApVoucherLineItems();

                    Iterator vliIter = voucherLineItems.iterator();

                    LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

                    while (vliIter.hasNext()) {

                        LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) vliIter.next();

                        ApRepVoucherPrintDetails details = new ApRepVoucherPrintDetails();

                        details.setVpVouType(apVoucher.getVouType());
                        details.setVpVouSplName(apVoucher.getApSupplier().getSplName());
                        details.setVpScSupplierClassName(apVoucher.getApSupplier().getApSupplierClass().getScName());
                        details.setVpVouDate(apVoucher.getVouDate());
                        Debug.print("getVouDateCreated(): " + apVoucher.getVouDateCreated());
                        details.setVpVouDateCreated(apVoucher.getVouDateCreated());
                        details.setVpVouSplAddress(apVoucher.getApSupplier().getSplAddress());
                        details.setVpVouDocumentNumber(apVoucher.getVouDocumentNumber());
                        details.setVpVouSplTin(apVoucher.getApSupplier().getSplTin());
                        details.setVpVouReferenceNumber(apVoucher.getVouReferenceNumber());
                        details.setVpVouDescription(apVoucher.getVouDescription());
                        details.setVpVouAmountDue(apVoucher.getVouAmountDue() * apVoucher.getVouConversionRate());
                        LocalAdUser adUsers = adUserHome.findByUsrName(apVoucher.getVouCreatedBy(), AD_CMPNY);
                        details.setVpVouCreatedBy(adUsers.getUsrName());
                        details.setVpVouSplCode(apVoucher.getApSupplier().getSplSupplierCode());
                        details.setVpVouScVatReliefVoucherItem(apVoucher.getApSupplier().getApSupplierClass().getScIsVatReliefVoucherItem());
                        details.setVpVliItemCode(apVoucherLineItem.getInvItemLocation().getInvItem().getIiName());
                        details.setVpVliItemDescription(apVoucherLineItem.getInvItemLocation().getInvItem().getIiDescription());
                        details.setVpVliLocation(apVoucherLineItem.getInvItemLocation().getInvLocation().getLocName());
                        details.setVpVliUnit(apVoucherLineItem.getInvUnitOfMeasure().getUomShortName());
                        details.setVpVliUnitAmount(apVoucherLineItem.getVliUnitCost());
                        details.setVpVliAmount(apVoucherLineItem.getVliAmount());
                        details.setVpVliDiscount(apVoucherLineItem.getVliTotalDiscount());

                        details.setVpVliVatRelief(apVoucherLineItem.getInvItemLocation().getInvItem().getIiIsVatRelief());
                        details.setVpVliSupplierName(apVoucherLineItem.getVliSplName());
                        details.setVpVliTin(apVoucherLineItem.getVliSplTin());
                        details.setVpVliAddress(apVoucherLineItem.getVliSplAddress());
                        details.setVpVliTax(apVoucherLineItem.getVliTax());

                        if (apVoucher.getVouApprovedRejectedBy() == null || apVoucher.getVouApprovedRejectedBy().equals("")) {

                            details.setVpVouApprovedBy(adPreference.getPrfApDefaultApprover());

                        } else {

                            details.setVpVouApprovedBy(apVoucher.getVouApprovedRejectedBy());
                        }

                        double tax = 0d;
                        double quantity = 0d;

                        // get purchase order lines

                        Collection apPurchaseOrderLines = apVoucher.getApPurchaseOrderLines();

                        for (Object purchaseOrderLine : apPurchaseOrderLines) {

                            LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) purchaseOrderLine;
                            Debug.print("AP POcode: " + apPurchaseOrderLine.getApPurchaseOrder().getPoCode().toString());
                            details.setVpPoReferenceNumber(apPurchaseOrderLine.getApPurchaseOrder().getPoReferenceNumber());

                            if (apPurchaseOrderLine.getApVoucher().getVouCode() != null) {
                                if (apVoucher.getVouCode().toString().equals(apPurchaseOrderLine.getApVoucher().getVouCode().toString())) {
                                    quantity = apPurchaseOrderLine.getPlQuantity();
                                    Debug.print("quantity: " + apPurchaseOrderLine.getPlQuantity());
                                    details.setVpPlAmount(apPurchaseOrderLine.getPlAmount());
                                    tax += apPurchaseOrderLine.getPlTaxAmount();
                                }

                            } else {
                                details.setVpPlAmount(0d);
                                details.setVpPlTaxAmount(0d);
                            }
                        }

                        details.setVpPlQuantity(quantity);
                        details.setVpPlTaxAmount(tax);

                        details.setVpVouCheckedBy(adPreference.getPrfApDefaultChecker());

                        details.setVpVouTerms(apVoucher.getAdPaymentTerm().getPytName());
                        details.setVpShowDuplicate(showDuplicate ? EJBCommon.TRUE : EJBCommon.FALSE);
                        details.setVpPoNumber(apVoucher.getVouPoNumber());

                        Collection apVoucherPaymentSchedules = apVoucher.getApVoucherPaymentSchedules();
                        ArrayList apVoucherPaymentScheduleList = new ArrayList(apVoucherPaymentSchedules);

                        LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = (LocalApVoucherPaymentSchedule) apVoucherPaymentScheduleList.get(apVoucherPaymentScheduleList.size() - 1);

                        details.setVpVouDueDate(apVoucherPaymentSchedule.getVpsDueDate());
                        details.setVpApprovalStatus(apVoucher.getVouApprovalStatus());
                        details.setVpPosted(apVoucher.getVouPosted());

                        // amount w/o tax
                        double NET_AMOUNT = 0d;
                        LocalApTaxCode apTaxCode = apVoucher.getApTaxCode();

                        if (apTaxCode.getTcType().equals("INCLUSIVE") || apTaxCode.getTcType().equals("EXCLUSIVE")) {

                            NET_AMOUNT = EJBCommon.roundIt(apVoucher.getVouAmountDue() / (1 + (apTaxCode.getTcRate() / 100)), this.getGlFcPrecisionUnit(AD_CMPNY));

                        } else {

                            NET_AMOUNT = apVoucher.getVouAmountDue();
                        }

                        details.setVpAmountWoTax(NET_AMOUNT);
                        details.setVpVouWithholdingTaxName(apVoucher.getApWithholdingTaxCode().getWtcName());
                        details.setVpVouTaxName(apVoucher.getApTaxCode().getTcName());
                        // details.setVpVouPostedBy(apVoucher.getVouPostedBy());

                        details.setVpVouFcSymbol(apVoucher.getGlFunctionalCurrency().getFcSymbol());

                        // Include Branch
                        LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(apVoucher.getVouAdBranch());
                        details.setVpBrName(adBranch.getBrName());
                        details.setVpBrCode(adBranch.getBrBranchCode());

                        // get user name description

                        Debug.print("apVoucher.getChkCreatedBy(): " + apVoucher.getVouCreatedBy());
                        try {
                            LocalAdUser adUser = adUserHome.findByUsrName(apVoucher.getVouCreatedBy(), AD_CMPNY);
                            details.setVpChkCreatedByDescription(adUser.getUsrName());
                        } catch (Exception e) {
                            details.setVpChkCreatedByDescription("");
                        }

                        try {
                            Debug.print("adPreference.getPrfApDefaultChecker(): " + adPreference.getPrfApDefaultChecker());
                            LocalAdUser adUser2 = adUserHome.findByUsrName(adPreference.getPrfApDefaultChecker(), AD_CMPNY);
                            details.setVpChkCheckByDescription(adUser2.getUsrDescription());
                        } catch (Exception e) {
                            details.setVpChkCheckByDescription("");
                        }

                        try {
                            Debug.print("apVoucher.getVouApprovedRejectedBy(): " + apVoucher.getVouApprovedRejectedBy());
                            LocalAdUser adUser3 = adUserHome.findByUsrName(apVoucher.getVouApprovedRejectedBy(), AD_CMPNY);
                            details.setVpChkApprovedRejectedByDescription(adUser3.getUsrDescription());

                        } catch (Exception e) {
                            details.setVpChkApprovedRejectedByDescription("");
                        }

                        try {
                            Debug.print("4: " + apVoucher.getVouPostedBy());
                            LocalAdUser adUser4 = adUserHome.findByUsrName(apVoucher.getVouPostedBy(), AD_CMPNY);
                            details.setVpVouPostedBy(adUser4.getUsrDescription());
                            Debug.print("postedBy: " + adUser4.getUsrDescription());
                        } catch (Exception e) {
                            details.setVpVouPostedBy("");
                        }

                        // trace misc
                        if (apVoucherLineItem.getInvItemLocation().getInvItem().getIiTraceMisc() == EJBCommon.TRUE) {

                            if (apVoucherLineItem.getInvTags().size() > 0) {
                                Debug.print("new code");
                                StringBuilder strBProperty = new StringBuilder();
                                StringBuilder strBSerial = new StringBuilder();
                                StringBuilder strBSpecs = new StringBuilder();
                                StringBuilder strBCustodian = new StringBuilder();
                                StringBuilder strBExpirationDate = new StringBuilder();

                                for (Object o : apVoucherLineItem.getInvTags()) {

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
                                details.setVpVliPropertyCode(strBProperty.toString());
                                // serial number
                                details.setVpVliSerialNumber(strBSerial.toString());
                                // specs
                                details.setVpVliSpecs(strBSpecs.toString());
                                // custodian
                                details.setVpVliCustodian(strBCustodian.toString());
                                // expiration date
                                details.setVpVliExpiryDate(strBExpirationDate.toString());
                            }
                        }

                        list.add(details);
                    }

                } else {

                    // get distribution records

                    Collection apDistributionRecords = apDistributionRecordHome.findByVouCode(apVoucher.getVouCode(), AD_CMPNY);

                    Iterator drIter = apDistributionRecords.iterator();

                    LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

                    while (drIter.hasNext()) {

                        LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) drIter.next();

                        ApRepVoucherPrintDetails details = new ApRepVoucherPrintDetails();
                        details.setVpVouType(apVoucher.getVouType());
                        details.setVpVouSplName(apVoucher.getApSupplier().getSplName());
                        details.setVpScSupplierClassName(apVoucher.getApSupplier().getApSupplierClass().getScName());
                        details.setVpVouDate(apVoucher.getVouDate());
                        Debug.print("getVouDateCreated(): " + apVoucher.getVouDateCreated());
                        details.setVpVouDateCreated(apVoucher.getVouDateCreated());
                        details.setVpVouSplAddress(apVoucher.getApSupplier().getSplAddress());
                        details.setVpVouDocumentNumber(apVoucher.getVouDocumentNumber());
                        details.setVpVouSplTin(apVoucher.getApSupplier().getSplTin());
                        details.setVpVouReferenceNumber(apVoucher.getVouReferenceNumber());
                        details.setVpVouDescription(apVoucher.getVouDescription());
                        //
                        //	details.setVpVouAmountDue(apVoucher.getVouAmountDue()*apVoucher.getVouConversionRate());

                        details.setVpVouAmountDue(apVoucher.getVouAmountDue());
                        LocalAdUser adUsers = adUserHome.findByUsrName(apVoucher.getVouCreatedBy(), AD_CMPNY);
                        details.setVpVouCreatedBy(adUsers.getUsrName());
                        details.setVpVouSplCode(apVoucher.getApSupplier().getSplSupplierCode());

                        if (apVoucher.getVouApprovedRejectedBy() == null || apVoucher.getVouApprovedRejectedBy().equals("")) {

                            details.setVpVouApprovedBy(adPreference.getPrfApDefaultApprover());

                        } else {

                            details.setVpVouApprovedBy(apVoucher.getVouApprovedRejectedBy());
                        }

                        double tax = 0d;
                        double quantity = 0d;

                        // get purchase order lines

                        Collection apPurchaseOrderLines = apVoucher.getApPurchaseOrderLines();

                        for (Object purchaseOrderLine : apPurchaseOrderLines) {

                            LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) purchaseOrderLine;
                            Debug.print("AP POcode: " + apPurchaseOrderLine.getApPurchaseOrder().getPoCode().toString());
                            details.setVpPoReferenceNumber(apPurchaseOrderLine.getApPurchaseOrder().getPoReferenceNumber());

                            if (apPurchaseOrderLine.getApVoucher().getVouCode() != null) {
                                if (apVoucher.getVouCode().toString().equals(apPurchaseOrderLine.getApVoucher().getVouCode().toString())) {
                                    quantity = apPurchaseOrderLine.getPlQuantity();
                                    Debug.print("quantity: " + apPurchaseOrderLine.getPlQuantity());
                                    details.setVpPlAmount(apPurchaseOrderLine.getPlAmount());
                                    tax += apPurchaseOrderLine.getPlTaxAmount();
                                }

                            } else {
                                details.setVpPlAmount(0d);
                                details.setVpPlTaxAmount(0d);
                            }

                            if (apDistributionRecord.getApPurchaseOrder() != null && (apPurchaseOrderLine.getApPurchaseOrder().getPoCode().equals(apDistributionRecord.getApPurchaseOrder().getPoCode()))) {
                                Debug.print("aaaaaaaaaa");
                                details.setVpDrTaxCoaAccountDescription(apDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());
                            }
                        }

                        details.setVpPlQuantity(quantity);
                        details.setVpPlTaxAmount(tax);

                        details.setVpVouCheckedBy(adPreference.getPrfApDefaultChecker());
                        details.setVpDrCoaAccountNumber(apDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                        details.setVpDrCoaAccountDescription(apDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());

                        details.setVpDrCoaAccountDescription(apDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());
                        details.setVpDrDebit(apDistributionRecord.getDrDebit());
                        //	details.setVpDrAmount(apDistributionRecord.getDrAmount());

                        details.setVpDrAmount(apDistributionRecord.getDrAmount() * apVoucher.getVouConversionRate());
                        details.setVpVouTerms(apVoucher.getAdPaymentTerm().getPytName());
                        details.setVpShowDuplicate(showDuplicate ? EJBCommon.TRUE : EJBCommon.FALSE);
                        details.setVpPoNumber(apVoucher.getVouPoNumber());

                        Collection apVoucherPaymentSchedules = apVoucher.getApVoucherPaymentSchedules();
                        ArrayList apVoucherPaymentScheduleList = new ArrayList(apVoucherPaymentSchedules);

                        LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = (LocalApVoucherPaymentSchedule) apVoucherPaymentScheduleList.get(apVoucherPaymentScheduleList.size() - 1);

                        details.setVpVouDueDate(apVoucherPaymentSchedule.getVpsDueDate());
                        details.setVpApprovalStatus(apVoucher.getVouApprovalStatus());
                        details.setVpPosted(apVoucher.getVouPosted());

                        // amount w/o tax
                        double NET_AMOUNT = 0d;
                        LocalApTaxCode apTaxCode = apVoucher.getApTaxCode();

                        if (apTaxCode.getTcType().equals("INCLUSIVE") || apTaxCode.getTcType().equals("EXCLUSIVE")) {

                            NET_AMOUNT = EJBCommon.roundIt(apVoucher.getVouAmountDue() / (1 + (apTaxCode.getTcRate() / 100)), this.getGlFcPrecisionUnit(AD_CMPNY));

                        } else {

                            NET_AMOUNT = apVoucher.getVouAmountDue();
                        }

                        details.setVpAmountWoTax(NET_AMOUNT);
                        details.setVpVouWithholdingTaxName(apVoucher.getApWithholdingTaxCode().getWtcName());
                        details.setVpVouTaxName(apVoucher.getApTaxCode().getTcName());
                        // details.setVpVouPostedBy(apVoucher.getVouPostedBy());

                        // get natural account desc
                        LocalGenValueSetValue genValueSetValue = this.getGenValueSetValue(apDistributionRecord.getGlChartOfAccount().getCoaAccountNumber(), AD_CMPNY);
                        details.setVpDrCoaNaturalDesc(genValueSetValue.getVsvDescription());

                        details.setVpVouFcSymbol(apVoucher.getGlFunctionalCurrency().getFcSymbol());

                        // Include Branch
                        LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(apVoucher.getVouAdBranch());
                        details.setVpBrName(adBranch.getBrName());
                        details.setVpBrCode(adBranch.getBrBranchCode());

                        // get user name description

                        Debug.print("apVoucher.getChkCreatedBy(): " + apVoucher.getVouCreatedBy());
                        try {
                            LocalAdUser adUser = adUserHome.findByUsrName(apVoucher.getVouCreatedBy(), AD_CMPNY);
                            details.setVpChkCreatedByDescription(adUser.getUsrName());
                        } catch (Exception e) {
                            details.setVpChkCreatedByDescription("");
                        }

                        try {
                            Debug.print("adPreference.getPrfApDefaultChecker(): " + adPreference.getPrfApDefaultChecker());
                            LocalAdUser adUser2 = adUserHome.findByUsrName(adPreference.getPrfApDefaultChecker(), AD_CMPNY);
                            details.setVpChkCheckByDescription(adUser2.getUsrDescription());
                        } catch (Exception e) {
                            details.setVpChkCheckByDescription("");
                        }

                        try {
                            Debug.print("apVoucher.getVouApprovedRejectedBy(): " + apVoucher.getVouApprovedRejectedBy());
                            LocalAdUser adUser3 = adUserHome.findByUsrName(apVoucher.getVouApprovedRejectedBy(), AD_CMPNY);
                            details.setVpChkApprovedRejectedByDescription(adUser3.getUsrDescription());

                        } catch (Exception e) {
                            details.setVpChkApprovedRejectedByDescription("");
                        }

                        try {
                            Debug.print("4: " + apVoucher.getVouPostedBy());
                            LocalAdUser adUser4 = adUserHome.findByUsrName(apVoucher.getVouPostedBy(), AD_CMPNY);
                            details.setVpVouPostedBy(adUser4.getUsrDescription());
                            Debug.print("postedBy: " + adUser4.getUsrDescription());
                        } catch (Exception e) {
                            details.setVpVouPostedBy("");
                        }

                        list.add(details);
                    }
                }
            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            list.sort(ApRepVoucherPrintDetails.sortByAccount);

            return list;

        } catch (GlobalNoRecordFoundException | GenVSVNoValueSetValueFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("ApRepVoucherPrintControllerBean getAdCompany");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());
            details.setCmpAddress(adCompany.getCmpAddress());
            details.setCmpCity(adCompany.getCmpCity());
            details.setCmpCountry(adCompany.getCmpCountry());
            details.setCmpPhone(adCompany.getCmpPhone());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ApRepVoucherPrintControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalGenValueSetValue getGenValueSetValue(String COA, Integer AD_CMPNY) throws GlobalNoRecordFoundException, GenVSVNoValueSetValueFoundException {

        Debug.print("ApRepVoucherPrintControllerBean getGenValueSetValue");


        LocalAdCompany adCompany = null;
        LocalGenValueSetValue genValueSetValue = null;

        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            // get coa separator
            LocalGenField genField = adCompany.getGenField();
            char chrSeparator = genField.getFlSegmentSeparator();
            String strSeparator = String.valueOf(chrSeparator);

            // get natural account segment
            Collection genSegments = genSegmentHome.findByFlCode(genField.getFlCode(), AD_CMPNY);
            Iterator i = genSegments.iterator();
            LocalGenValueSet genValueSet = null;
            while (i.hasNext()) {

                LocalGenSegment genSegment = (LocalGenSegment) i.next();
                if (genSegment.getSgSegmentType() == 'N') {
                    genValueSet = genSegment.getGenValueSet();
                    break;
                }
            }

            // get value set value
            StringTokenizer st = new StringTokenizer(COA, strSeparator);
            while (st.hasMoreTokens()) {

                try {
                    genValueSetValue = genValueSetValueHome.findByVsCodeAndVsvValue(genValueSet.getVsCode(), st.nextToken(), AD_CMPNY);
                } catch (Exception ex) {

                }

                if (genValueSetValue != null) break;
            }

            if (genValueSetValue == null) throw new GenVSVNoValueSetValueFoundException();

            return genValueSetValue;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApRepVoucherPrintControllerBean ejbCreate");
    }
}