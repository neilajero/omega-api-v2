package com.ejb.txnreports.ap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import com.ejb.entities.ap.LocalApAppliedVoucher;
import com.ejb.dao.ap.LocalApAppliedVoucherHome;
import com.ejb.entities.ap.LocalApCheck;
import com.ejb.dao.ap.LocalApCheckHome;
import com.ejb.entities.ap.LocalApDistributionRecord;
import com.ejb.dao.ap.LocalApDistributionRecordHome;
import com.ejb.entities.ap.LocalApTaxCode;
import com.ejb.entities.ap.LocalApWithholdingTaxCode;
import com.ejb.exception.gen.GenVSVNoValueSetValueFoundException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.gen.LocalGenField;
import com.ejb.entities.gen.LocalGenSegment;
import com.ejb.dao.gen.LocalGenSegmentHome;
import com.ejb.entities.gen.LocalGenValueSet;
import com.ejb.entities.gen.LocalGenValueSetValue;
import com.ejb.dao.gen.LocalGenValueSetValueHome;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ap.ApRepCheckVoucherPrintDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;

@Stateless(name = "ApRepCheckVoucherPrintControllerEJB")
public class ApRepCheckVoucherPrintControllerBean extends EJBContextClass implements ApRepCheckVoucherPrintController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdApprovalDocumentHome adApprovalDocumentHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    private LocalGenSegmentHome genSegmentHome;
    @EJB
    private LocalGenValueSetValueHome genValueSetValueHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalApCheckHome apCheckHome;
    @EJB
    private LocalApAppliedVoucherHome apAppliedVoucherHome;

    public ArrayList executeApRepCheckVoucherPrint(ArrayList chkCodeList, Integer AD_CMPNY) throws GlobalNoRecordFoundException, GenVSVNoValueSetValueFoundException {

        Debug.print("ApRepCheckVoucherPrintControllerBean executeApRepCheckVoucherPrint");

        ArrayList list = new ArrayList();
        String asd = "a";

        int a = 1;

        try {

            for (Object value : chkCodeList) {

                Integer CHK_CODE = (Integer) value;

                double TOTAL_AMOUNT = 0;
                LocalApCheck apCheck = null;

                try {

                    apCheck = apCheckHome.findByPrimaryKey(CHK_CODE);

                } catch (FinderException ex) {

                    continue;
                }

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(AD_CMPNY);
                LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
                LocalAdApprovalDocument adApprovalDocument = adApprovalDocumentHome.findByAdcType("AP CHECK", AD_CMPNY);

                if (adApprovalDocument.getAdcPrintOption().equals("PRINT APPROVED ONLY")) {

                    if (apCheck.getChkApprovalStatus() == null || apCheck.getChkApprovalStatus().equals("PENDING")) {

                        continue;
                    }

                } else if (adApprovalDocument.getAdcPrintOption().equals("PRINT UNAPPROVED ONLY")) {

                    if (apCheck.getChkApprovalStatus() != null && (apCheck.getChkApprovalStatus().equals("N/A") || apCheck.getChkApprovalStatus().equals("APPROVED"))) {

                        continue;
                    }
                }

                if (adApprovalDocument.getAdcAllowDuplicate() == EJBCommon.FALSE && apCheck.getChkCvPrinted() == EJBCommon.TRUE) {

                    continue;
                }

                // show duplicate

                boolean showDuplicate = adApprovalDocument.getAdcTrackDuplicate() == EJBCommon.TRUE && apCheck.getChkCvPrinted() == EJBCommon.TRUE;

                // set printed

                apCheck.setChkCvPrinted(EJBCommon.TRUE);

                if (apCheck.getChkType().equals("PAYMENT") && adPreference.getPrfApCheckVoucherDataSource().equals("AP VOUCHER")) {

                    Collection apAppliedVouchers = apCheck.getApAppliedVouchers();

                    Collection apDistributionRecords = apDistributionRecordHome.findDrsByDrClassAndChkCode("CASH", apCheck.getChkCode(), AD_CMPNY);

                    Iterator iter = apDistributionRecords.iterator();

                    LocalApDistributionRecord apChkCaskDr = null;

                    while (iter.hasNext()) {

                        apChkCaskDr = (LocalApDistributionRecord) iter.next();

                        if (apChkCaskDr.getClass().equals("CASH")) break;
                    }

                    iter = apAppliedVouchers.iterator();

                    while (iter.hasNext()) {

                        LocalApAppliedVoucher apAppliedVoucher = (LocalApAppliedVoucher) iter.next();
                        boolean first = true;

                        for (Object o : apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getApDistributionRecords()) {

                            ApRepCheckVoucherPrintDetails details = new ApRepCheckVoucherPrintDetails();

                            if (!asd.equals(apCheck.getChkDocumentNumber()) && asd != "a") {
                                a++;
                            }

                            details.setCvCheckType(apCheck.getChkType());
                            details.setCvChkNumber(apCheck.getChkNumber());
                            details.setCvChkSplName(apCheck.getChkSupplierName() == null ? apCheck.getApSupplier().getSplName() : apCheck.getChkSupplierName());
                            details.setCvChkDocumentNumber(apCheck.getChkDocumentNumber());
                            details.setCvChkDate(apCheck.getChkCheckDate());
                            details.setCvChkCurrencyDescription(apCheck.getGlFunctionalCurrency().getFcDescription());
                            details.setCvChkCurrencySymbol(apCheck.getGlFunctionalCurrency().getFcSymbol());
                            details.setCvChkCreatedBy(apCheck.getChkCreatedBy());
                            details.setCvChkCheckedBy(adPreference.getPrfApDefaultChecker());
                            details.setCvChkApprovedRejectedBy(apCheck.getChkApprovedRejectedBy());
                            details.setCvChkDescription(apCheck.getChkDescription());
                            details.setCvChkReferenceNumber(apCheck.getChkReferenceNumber());
                            details.setCvChkAdBaName(apCheck.getAdBankAccount().getBaName());
                            details.setCvChkAdBaAccountNumber(apCheck.getAdBankAccount().getBaAccountNumber());
                            details.setCvChkSplAddress(apCheck.getApSupplier().getSplAddress());
                            details.setCvChkMemo(apCheck.getChkMemo());
                            details.setCvVouNumber(apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouDocumentNumber());
                            details.setCvChkSplCode(apCheck.getApSupplier().getSplSupplierCode());
                            // details.setCvChkAmount(apCheck.getChkAmount());
                            details.setCvChkAmount(this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apCheck.getChkAmount(), AD_CMPNY));
                            Debug.print("vouNumber1: " + apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouDocumentNumber());

                            // get user name description

                            Debug.print("apCheck.getChkCreatedBy(): " + apCheck.getChkCreatedBy());
                            Debug.print("apCheck.getChkApprovedRejectedBy(): " + apCheck.getChkApprovedRejectedBy());
                            try {
                                LocalAdUser adUser = adUserHome.findByUsrName(apCheck.getChkCreatedBy(), AD_CMPNY);
                                details.setCvChkCreatedByDescription(adUser.getUsrName());
                                details.setCvChkApprovedRejectedByDescription(adUser.getUsrDescription());
                            } catch (Exception e) {
                                details.setCvChkCreatedByDescription("");
                            }

                            try {
                                Debug.print("adPreference.getPrfApDefaultChecker()(): " + adPreference.getPrfApDefaultChecker());
                                LocalAdUser adUser2 = adUserHome.findByUsrName(adPreference.getPrfApDefaultChecker(), AD_CMPNY);
                                details.setCvChkCheckByDescription(adUser2.getUsrDescription());
                                details.setCvChkApprovedRejectedByDescription(adUser2.getUsrDescription());
                            } catch (Exception e) {
                                details.setCvChkCheckByDescription("");
                            }

                            try {
                                Debug.print("apCheck.getChkApprovedRejectedBy(): " + apCheck.getChkApprovedRejectedBy());
                                LocalAdUser adUser3 = adUserHome.findByUsrName(apCheck.getChkApprovedRejectedBy(), AD_CMPNY);
                                details.setCvChkApprovedRejectedByDescription(adUser3.getUsrDescription());
                            } catch (Exception e) {
                                details.setCvChkApprovedRejectedByDescription("");
                            }

                            try {
                                Debug.print("2: " + apCheck.getChkPostedBy());
                                LocalAdUser adUser4 = adUserHome.findByUsrName(apCheck.getChkPostedBy(), AD_CMPNY);
                                details.setCvVouPostedBy(adUser4.getUsrDescription());
                                Debug.print("postedBy: " + adUser4.getUsrDescription());
                            } catch (Exception e) {
                                details.setCvVouPostedBy("");
                            }

                            if (first) {
                                Debug.print("vouNumber2: " + apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouDocumentNumber());
                                details.setCvVouNumber(apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouDocumentNumber());
                                details.setCvVouDate(apAppliedVoucher.getApVoucherPaymentSchedule().getVpsDueDate());
                                details.setCvVouDescription(apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouDescription());
                                // details.setCvVouPostedBy(apCheck.getChkPostedBy());
                                details.setCvApplyAmount(apAppliedVoucher.getAvApplyAmount());
                                details.setCvVouReferenceNumber(apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouReferenceNumber());
                            }

                            LocalApTaxCode apTaxCode = apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getApTaxCode();
                            LocalApWithholdingTaxCode apWithholdingTaxCode = apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getApWithholdingTaxCode();
                            short precisionUnit = this.getGlFcPrecisionUnit(AD_CMPNY);

                            double NET_AMNT = 0d;
                            double TOTAL_NET_AMNT = 0d;
                            double TAX_AMNT = 0d;
                            double WTAX_AMNT = 0d;
                            double ratio = 0d;

                            // calculate net amount
                            NET_AMNT = EJBCommon.roundIt((apAppliedVoucher.getAvApplyAmount() / ((1 + (apTaxCode.getTcRate() / 100)) - (apWithholdingTaxCode.getWtcRate() / 100))), precisionUnit);

                            // calculate total net amount
                            LocalApDistributionRecord apDR = null;

                            // calculate tax
                            if (first && apTaxCode.getTcRate() != 0) {
                                TAX_AMNT = EJBCommon.roundIt(NET_AMNT * apTaxCode.getTcRate() / 100, precisionUnit);
                                details.setCvApplyTaxAmount(TAX_AMNT);
                            }

                            try {

                                apDR = apDistributionRecordHome.findByDrClassAndVouCode("W-TAX", apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouCode(), AD_CMPNY);

                            } catch (FinderException ex) {

                            }

                            if (apDR != null) {

                                // calculate wtax

                                if (first && apWithholdingTaxCode.getWtcRate() != 0) {
                                    WTAX_AMNT = EJBCommon.roundIt(NET_AMNT * apWithholdingTaxCode.getWtcRate() / 100, precisionUnit) + apAppliedVoucher.getAvTaxWithheld();
                                    details.setCvApplyWithholdingTaxAmount(WTAX_AMNT);
                                }
                                details.setCvApplyAmmountDue(apAppliedVoucher.getApVoucherPaymentSchedule().getVpsAmountDue());
                                TOTAL_NET_AMNT = EJBCommon.roundIt((apAppliedVoucher.getApVoucherPaymentSchedule().getVpsAmountDue() / ((1 + (apTaxCode.getTcRate() / 100)) - (apWithholdingTaxCode.getWtcRate() / 100))), precisionUnit);
                                Debug.print("TOTAL_NET_AMNT---------------->" + TOTAL_NET_AMNT);

                            } else {

                                WTAX_AMNT = apAppliedVoucher.getAvTaxWithheld();
                                details.setCvApplyWithholdingTaxAmount(WTAX_AMNT);

                                TOTAL_NET_AMNT = EJBCommon.roundIt(((apAppliedVoucher.getApVoucherPaymentSchedule().getVpsAmountDue() - WTAX_AMNT) / ((1 + (apTaxCode.getTcRate() / 100)) - (apWithholdingTaxCode.getWtcRate() / 100))), precisionUnit);
                                Debug.print("TOTAL_NET_AMNT---------------->" + TOTAL_NET_AMNT);
                            }

                            // distribution record
                            LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) o;

                            if (apDistributionRecord.getDrClass().equals("PAYABLE")) {

                                // payable account
                                details.setCvVouDrCoaAccountNumber(apDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());

                                // get natural account
                                LocalGenValueSetValue genValueSetValue = this.getGenValueSetValue(apDistributionRecord.getGlChartOfAccount().getCoaAccountNumber(), AD_CMPNY);
                                details.setCvVouDrCoaNaturalDesc(genValueSetValue.getVsvDescription());

                                // cash account
                                details.setCvChkDrCoaAccountNumber(apChkCaskDr.getGlChartOfAccount().getCoaAccountNumber());
                                details.setCvChkDrCoaAccountDescription(apChkCaskDr.getGlChartOfAccount().getCoaAccountDescription());
                                genValueSetValue = this.getGenValueSetValue(apChkCaskDr.getGlChartOfAccount().getCoaAccountNumber(), AD_CMPNY);
                                details.setCvChkDrCoaNaturalDesc(genValueSetValue.getVsvDescription());

                                // ratio
                                ratio = EJBCommon.roundIt(apDistributionRecord.getDrAmount() / TOTAL_NET_AMNT, precisionUnit);
                                Debug.print("ration=" + ratio);
                            } else {
                                continue;
                            }

                            details.setCvApplyNetAmount(NET_AMNT * ratio);

                            if (apCheck.getApTaxCode() != null) {
                                details.setCvTaxCode(apCheck.getApTaxCode().getTcName());
                            }

                            if (apCheck.getApWithholdingTaxCode() != null) {
                                details.setCvWithholdingTaxCode(apCheck.getApWithholdingTaxCode().getWtcName());
                            }

                            asd = apCheck.getChkDocumentNumber();

                            first = false;
                            list.add(details);
                        }
                    }

                } else if (apCheck.getChkType().equals("DIRECT") && adPreference.getPrfApCheckVoucherDataSource().equals("AP VOUCHER")) {

                    // get CASH dr

                    Collection apDistributionRecords = apDistributionRecordHome.findDrsByDrClassAndChkCode("CASH", apCheck.getChkCode(), AD_CMPNY);

                    Iterator drIter = apDistributionRecords.iterator();

                    LocalApDistributionRecord apChkCashDr = null;

                    if (drIter.hasNext()) {

                        apChkCashDr = (LocalApDistributionRecord) drIter.next();
                    }

                    // get distribution records

                    apDistributionRecords = apDistributionRecordHome.findByChkCode(apCheck.getChkCode(), AD_CMPNY);

                    drIter = apDistributionRecords.iterator();

                    while (drIter.hasNext()) {

                        LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) drIter.next();

                        if (apDistributionRecord.getDrClass().equals("CASH") || apDistributionRecord.getDrClass().equals("TAX") || apDistributionRecord.getDrClass().equals("W-TAX"))
                            continue;

                        ApRepCheckVoucherPrintDetails details = new ApRepCheckVoucherPrintDetails();

                        if (!asd.equals(apCheck.getChkDocumentNumber()) && asd != "a") {
                            a++;
                        }

                        details.setCvCheckType(apCheck.getChkType());
                        details.setCvChkDocumentNumber(apCheck.getChkDocumentNumber());
                        details.setCvChkReferenceNumber(apCheck.getChkReferenceNumber());
                        details.setCvChkNumber(apCheck.getChkNumber());
                        details.setCvChkDate(apCheck.getChkDate());
                        details.setCvChkCheckDate(apCheck.getChkCheckDate());
                        details.setCvChkDescription(apCheck.getChkDescription());
                        details.setCvVouDescription(apCheck.getChkDescription());
                        details.setCvChkMemo(apCheck.getChkMemo());
                        details.setCvChkAmount(apCheck.getChkAmount());
                        details.setCvChkAmount(this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apCheck.getChkAmount(), AD_CMPNY));
                        details.setCvChkCurrencySymbol(apCheck.getGlFunctionalCurrency().getFcSymbol());
                        details.setCvChkCurrencyDescription(apCheck.getGlFunctionalCurrency().getFcDescription());
                        details.setCvChkAdBaName(apCheck.getAdBankAccount().getBaName());
                        details.setCvChkAdBaAccountNumber(apCheck.getAdBankAccount().getBaAccountNumber());
                        details.setCvChkSplName(apCheck.getChkSupplierName() == null ? apCheck.getApSupplier().getSplName() : apCheck.getChkSupplierName());
                        details.setCvChkSplAddress(apCheck.getApSupplier().getSplAddress());
                        details.setCvChkCreatedBy(apCheck.getChkCreatedBy());
                        details.setCvChkApprovedRejectedBy(apCheck.getChkApprovedRejectedBy());
                        details.setCvChkCheckedBy(adPreference.getPrfApDefaultChecker());
                        details.setCvChkSplCode(apCheck.getApSupplier().getSplSupplierCode());
                        Collection apAppliedVouchers = apCheck.getApAppliedVouchers();
                        Iterator iter = apDistributionRecords.iterator();
                        iter = apAppliedVouchers.iterator();
                        double amountDue = 0d;
                        while (iter.hasNext()) {
                            LocalApAppliedVoucher apAppliedVoucher = (LocalApAppliedVoucher) iter.next();
                            details.setCvVouNumber(apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouDocumentNumber());
                            amountDue += apAppliedVoucher.getApVoucherPaymentSchedule().getVpsAmountDue();
                        }
                        details.setCvApplyAmmountDue(amountDue);
                        // log details

                        details.setCvChkCreatedBy(apCheck.getChkCreatedBy());

                        Debug.print("apCheck.getChkCreatedBy(): " + apCheck.getChkCreatedBy());
                        try {
                            LocalAdUser adUser = adUserHome.findByUsrName(apCheck.getChkCreatedBy(), AD_CMPNY);
                            details.setCvChkCreatedByDescription(adUser.getUsrName());
                            details.setCvChkApprovedRejectedByDescription(adUser.getUsrDescription());
                        } catch (Exception e) {
                            details.setCvChkCreatedByDescription("");
                        }

                        try {
                            Debug.print("adPreference.getPrfApDefaultChecker()(): " + adPreference.getPrfApDefaultChecker());
                            LocalAdUser adUser2 = adUserHome.findByUsrName(adPreference.getPrfApDefaultChecker(), AD_CMPNY);
                            details.setCvChkCheckByDescription(adUser2.getUsrDescription());
                            details.setCvChkApprovedRejectedByDescription(adUser2.getUsrDescription());
                        } catch (Exception e) {
                            details.setCvChkCheckByDescription("");
                        }

                        try {
                            Debug.print("apCheck.getChkApprovedRejectedBy()1: " + apCheck.getChkApprovedRejectedBy());
                            LocalAdUser adUser3 = adUserHome.findByUsrName(apCheck.getChkApprovedRejectedBy(), AD_CMPNY);
                            details.setCvChkApprovedRejectedByDescription(adUser3.getUsrDescription());
                        } catch (Exception e) {
                            details.setCvChkApprovedRejectedByDescription("");
                        }

                        if (apCheck.getChkApprovedRejectedBy() == null || apCheck.getChkApprovedRejectedBy().equals("")) {
                            details.setCvChkApprovedRejectedBy(adPreference.getPrfApDefaultApprover());
                        } else {
                            details.setCvChkApprovedRejectedBy(apCheck.getChkApprovedRejectedBy());
                        }

                        try {
                            Debug.print("3: " + apCheck.getChkPostedBy());
                            LocalAdUser adUser4 = adUserHome.findByUsrName(apCheck.getChkPostedBy(), AD_CMPNY);
                            details.setCvVouPostedBy(adUser4.getUsrDescription());
                            Debug.print("postedBy: " + adUser4.getUsrDescription());
                        } catch (Exception e) {
                            details.setCvVouPostedBy("");
                        }

                        details.setCvChkCheckedBy(adPreference.getPrfApDefaultChecker());
                        details.setCvChkDateCreated(apCheck.getChkDateCreated());
                        details.setCvChkDateApprovedRejected(apCheck.getChkDateApprovedRejected());
                        details.setCvApprovalStatus(apCheck.getChkApprovalStatus());
                        details.setCvPosted(apCheck.getChkPosted());

                        details.setCvChkDrDebit(apDistributionRecord.getDrDebit());
                        details.setCvChkDrAmount(apDistributionRecord.getDrAmount());
                        details.setCvApplyNetAmount(apDistributionRecord.getDrDebit() == EJBCommon.TRUE ? apDistributionRecord.getDrAmount() : (apDistributionRecord.getDrAmount() * -1));

                        // expense account
                        details.setCvVouDrCoaAccountNumber(apDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                        LocalGenValueSetValue genValueSetValue = this.getGenValueSetValue(apDistributionRecord.getGlChartOfAccount().getCoaAccountNumber(), AD_CMPNY);
                        details.setCvVouDrCoaNaturalDesc(genValueSetValue.getVsvDescription());

                        // cash account
                        details.setCvChkDrCoaAccountNumber(apChkCashDr.getGlChartOfAccount().getCoaAccountNumber());
                        genValueSetValue = this.getGenValueSetValue(apChkCashDr.getGlChartOfAccount().getCoaAccountNumber(), AD_CMPNY);
                        details.setCvChkDrCoaNaturalDesc(genValueSetValue.getVsvDescription());

                        details.setCvShowDuplicate(showDuplicate ? EJBCommon.TRUE : EJBCommon.FALSE);
                        details.setCvChkCrossCheck(apCheck.getChkCrossCheck());
                        details.setCvChkSplCode(apCheck.getApSupplier().getSplSupplierCode());
                        asd = apCheck.getChkDocumentNumber();

                        if (apCheck.getApTaxCode() != null) {
                            details.setCvTaxCode(apCheck.getApTaxCode().getTcName());
                        }
                        if (apCheck.getApWithholdingTaxCode() != null) {
                            details.setCvWithholdingTaxCode(apCheck.getApWithholdingTaxCode().getWtcName());
                        }
                        list.add(details);
                    }

                } else {
                    Debug.print("ELSE----------------->");

                    // get distribution records

                    Collection apDistributionRecords = apDistributionRecordHome.findByChkCode(apCheck.getChkCode(), AD_CMPNY);

                    for (Object distributionRecord : apDistributionRecords) {

                        LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) distributionRecord;

                        ApRepCheckVoucherPrintDetails details = new ApRepCheckVoucherPrintDetails();

                        if (!asd.equals(apCheck.getChkDocumentNumber()) && asd != "a") {
                            a++;
                        }

                        details.setCvChkDocumentNumber(apCheck.getChkDocumentNumber());
                        details.setCvChkNumber(apCheck.getChkNumber());
                        details.setCvChkDate(apCheck.getChkDate());
                        details.setCvChkCheckDate(apCheck.getChkCheckDate());
                        details.setCvChkDescription(apCheck.getChkDescription());
                        details.setCvChkAmount(apCheck.getChkAmount());
                        details.setCvChkAmount(this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apCheck.getChkAmount(), AD_CMPNY));
                        details.setCvChkCreatedBy(apCheck.getChkCreatedBy());
                        if (apCheck.getChkApprovedRejectedBy() == null || apCheck.getChkApprovedRejectedBy().equals("")) {

                            details.setCvChkApprovedRejectedBy(adPreference.getPrfApDefaultApprover());

                        } else {

                            details.setCvChkApprovedRejectedBy(apCheck.getChkApprovedRejectedBy());
                        }

                        details.setCvChkCheckedBy(adPreference.getPrfApDefaultChecker());
                        // supplier name
                        details.setCvChkSplName(apCheck.getChkSupplierName() == null ? apCheck.getApSupplier().getSplName() : apCheck.getChkSupplierName());
                        details.setCvChkCurrencySymbol(apCheck.getGlFunctionalCurrency().getFcSymbol());
                        details.setCvChkCurrencyDescription(apCheck.getGlFunctionalCurrency().getFcDescription());
                        details.setCvChkDrDebit(apDistributionRecord.getDrDebit());
                        details.setCvChkDrAmount(apDistributionRecord.getDrAmount());
                        details.setCvChkAdBaName(apCheck.getAdBankAccount().getBaName());
                        details.setCvChkAdBaAccountNumber(apCheck.getAdBankAccount().getBaAccountNumber());
                        details.setCvShowDuplicate(showDuplicate ? EJBCommon.TRUE : EJBCommon.FALSE);
                        details.setCvApprovalStatus(apCheck.getChkApprovalStatus());
                        details.setCvPosted(apCheck.getChkPosted());
                        details.setCvChkDrCoaAccountNumber(apDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                        details.setCvChkDrCoaAccountDescription(apDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());
                        details.setCvChkCrossCheck(apCheck.getChkCrossCheck());
                        details.setCvChkDateCreated(apCheck.getChkDateCreated());
                        details.setCvChkDateApprovedRejected(apCheck.getChkDateApprovedRejected());
                        details.setCvChkReferenceNumber(apCheck.getChkReferenceNumber());
                        details.setCvChkSplAddress(apCheck.getApSupplier().getSplAddress());
                        details.setCvChkMemo(apCheck.getChkMemo());
                        details.setCvChkCreatedBy(apCheck.getChkCreatedBy());
                        details.setCvChkApprovedRejectedBy(apCheck.getChkApprovedRejectedBy());
                        details.setCvChkSplCode(apCheck.getApSupplier().getSplSupplierCode());
                        Collection apAppliedVouchers = apCheck.getApAppliedVouchers();
                        Iterator iter = apDistributionRecords.iterator();
                        iter = apAppliedVouchers.iterator();
                        double amountDue = 0d;
                        while (iter.hasNext()) {
                            LocalApAppliedVoucher apAppliedVoucher = (LocalApAppliedVoucher) iter.next();
                            details.setCvVouNumber(apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouDocumentNumber());
                            amountDue += apAppliedVoucher.getApVoucherPaymentSchedule().getVpsAmountDue();
                        }
                        Debug.print("amountdue=" + amountDue);
                        details.setCvApplyAmmountDue(amountDue);
                        // set check type
                        details.setCvCheckType(apCheck.getChkType());

                        //            			Include Branch
                        LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(apCheck.getChkAdBranch());
                        details.setCvChkBranchCode(adBranch.getBrBranchCode());
                        details.setCvChkBranchName(adBranch.getBrName());

                        // get user name description

                        Debug.print("apCheck.getChkCreatedBy(): " + apCheck.getChkCreatedBy());
                        try {
                            LocalAdUser adUser = adUserHome.findByUsrName(apCheck.getChkCreatedBy(), AD_CMPNY);
                            details.setCvChkCreatedByDescription(adUser.getUsrName());
                            details.setCvChkApprovedRejectedByDescription(adUser.getUsrDescription());
                        } catch (Exception e) {
                            details.setCvChkCreatedByDescription("");
                        }

                        try {
                            Debug.print("adPreference.getPrfApDefaultChecker()(): " + adPreference.getPrfApDefaultChecker());
                            LocalAdUser adUser2 = adUserHome.findByUsrName(adPreference.getPrfApDefaultChecker(), AD_CMPNY);
                            details.setCvChkCheckByDescription(adUser2.getUsrDescription());
                            details.setCvChkApprovedRejectedByDescription(adUser2.getUsrDescription());
                        } catch (Exception e) {
                            details.setCvChkCheckByDescription("");
                        }

                        try {
                            Debug.print("apCheck.getChkApprovedRejectedBy(): " + apCheck.getChkApprovedRejectedBy());
                            LocalAdUser adUser3 = adUserHome.findByUsrName(apCheck.getChkApprovedRejectedBy(), AD_CMPNY);
                            details.setCvChkApprovedRejectedByDescription(adUser3.getUsrDescription());
                        } catch (Exception e) {
                            details.setCvChkApprovedRejectedByDescription("");
                        }

                        try {
                            Debug.print("4: " + apCheck.getChkPostedBy());
                            LocalAdUser adUser4 = adUserHome.findByUsrName(apCheck.getChkPostedBy(), AD_CMPNY);
                            details.setCvVouPostedBy(adUser4.getUsrDescription());
                            Debug.print("postedBy: " + adUser4.getUsrDescription());
                        } catch (Exception e) {
                            details.setCvVouPostedBy("");
                        }

                        asd = apCheck.getChkDocumentNumber();

                        if (apCheck.getApTaxCode() != null) {
                            details.setCvTaxCode(apCheck.getApTaxCode().getTcName());
                        }
                        if (apCheck.getApWithholdingTaxCode() != null) {
                            details.setCvWithholdingTaxCode(apCheck.getApWithholdingTaxCode().getWtcName());
                        }
                        list.add(details);
                    }
                }
            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            if (adCompanyHome.findByPrimaryKey(AD_CMPNY).getCmpShortName().equals("DNATA")) {
                // if(a==1){
                try {
                    list.sort(ApRepCheckVoucherPrintDetails.sortByAccount);
                } catch (Exception e) {

                }
                // }

            }

            return list;

        } catch (GlobalNoRecordFoundException | GenVSVNoValueSetValueFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList executeApRepCheckVoucherPrintSub(ArrayList chkCodeList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepCheckVoucherPrintControllerBean executeApRepCheckVoucherPrintSub");

        ArrayList list = new ArrayList();

        try {

            for (Object o : chkCodeList) {

                Integer CHK_CODE = (Integer) o;

                LocalApCheck apCheck = null;

                try {

                    apCheck = apCheckHome.findByPrimaryKey(CHK_CODE);

                } catch (FinderException ex) {

                    continue;
                }

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(AD_CMPNY);
                LocalAdApprovalDocument adApprovalDocument = adApprovalDocumentHome.findByAdcType("AP CHECK", AD_CMPNY);

                if (adApprovalDocument.getAdcPrintOption().equals("PRINT APPROVED ONLY")) {

                    if (apCheck.getChkApprovalStatus() == null || apCheck.getChkApprovalStatus().equals("PENDING")) {

                        continue;
                    }

                } else if (adApprovalDocument.getAdcPrintOption().equals("PRINT UNAPPROVED ONLY")) {

                    if (apCheck.getChkApprovalStatus() != null && (apCheck.getChkApprovalStatus().equals("N/A") || apCheck.getChkApprovalStatus().equals("APPROVED"))) {

                        continue;
                    }
                }

                if (adApprovalDocument.getAdcAllowDuplicate() == EJBCommon.FALSE && apCheck.getChkCvPrinted() == EJBCommon.TRUE) {

                    continue;
                }

                Collection apAppliedVouchers = null;

                try {

                    apAppliedVouchers = apAppliedVoucherHome.findByChkCode(CHK_CODE, AD_CMPNY);

                } catch (FinderException ex) {

                }

                String tempVoucher = null;

                for (Object appliedVoucher : apAppliedVouchers) {

                    LocalApAppliedVoucher apAppliedVoucher = (LocalApAppliedVoucher) appliedVoucher;

                    ApRepCheckVoucherPrintDetails details = new ApRepCheckVoucherPrintDetails();

                    details.setCvChkDocumentNumber(apCheck.getChkDocumentNumber());

                    Collection apPurchaseOrderLines = apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getApPurchaseOrderLines();
                    if (apPurchaseOrderLines.size() != 0 && apPurchaseOrderLines != null) {

                        details.setCvPoDocumentNumber(apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouPoNumber());
                        details.setCvPoDate(apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouDate());
                    }
                    Debug.print("vouNumber3: " + apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouDocumentNumber());
                    details.setCvVouNumber(apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouDocumentNumber());
                    details.setCvVouDate(apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouDate());

                    details.setCvChkAmount(apAppliedVoucher.getAvApplyAmount());
                    details.setCvVouReferenceNumber(apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouReferenceNumber());

                    if (tempVoucher == null || !tempVoucher.equals(apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouDocumentNumber())) {

                        tempVoucher = apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouDocumentNumber();

                        double WTAX_AMNT = 0;
                        double NET_AMNT = 0;

                        // set withholding tax amount, net amount & gross amount
                        Collection apDistributionRecords = apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getApDistributionRecords();

                        for (Object distributionRecord : apDistributionRecords) {

                            LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) distributionRecord;

                            if (apDistributionRecord.getDrClass().equals("W-TAX")) {
                                WTAX_AMNT += apDistributionRecord.getDrAmount();
                            } else if (apDistributionRecord.getDrClass().equals("PAYABLE")) {
                                NET_AMNT += apDistributionRecord.getDrAmount();
                            }
                        }

                        details.setCvVouGrossAmount(WTAX_AMNT + NET_AMNT);
                        details.setCvVouWithholdingTaxAmount(WTAX_AMNT);
                        details.setCvVouNetAmount(NET_AMNT);
                    }

                    if (apCheck.getApTaxCode() != null) {
                        details.setCvTaxCode(apCheck.getApTaxCode().getTcName());
                    }
                    if (apCheck.getApWithholdingTaxCode() != null) {
                        details.setCvWithholdingTaxCode(apCheck.getApWithholdingTaxCode().getWtcName());
                    }
                    list.add(details);
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

        Debug.print("ApRepCheckVoucherPrintControllerBean getAdCompany");

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

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ApRepCheckVoucherPrintControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public String getAdPrfApCheckVoucherDataSource(Integer AD_CMPNY) {

        Debug.print("ApRepCheckVoucherPrintControllerBean getAdPrfApCheckVoucherDataSource");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfApCheckVoucherDataSource();

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

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("ApRepCheckVoucherPrintControllerBean convertForeignToFunctionalCurrency");

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

        Debug.print("ApRepCheckVoucherPrintControllerBean ejbCreate");
    }
}