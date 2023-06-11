package com.ejb.txnsync.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ar.*;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.dao.inv.LocalInvItemHome;
import com.ejb.dao.inv.LocalInvItemLocationHome;
import com.ejb.dao.inv.LocalInvUnitOfMeasureHome;
import com.ejb.entities.ad.*;
import com.ejb.entities.ar.*;
import com.ejb.entities.gen.LocalGenField;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.entities.gl.LocalGlFunctionalCurrencyRate;
import com.ejb.entities.inv.LocalInvBillOfMaterial;
import com.ejb.entities.inv.LocalInvItemLocation;
import com.ejb.entities.inv.LocalInvUnitOfMeasure;
import com.ejb.exception.global.*;
import com.ejb.restfulapi.sync.ar.models.ArCustomerSyncResponse;
import com.ejb.restfulapi.sync.ar.models.ArInvoiceSyncRequest;
import com.ejb.restfulapi.sync.ar.models.ArInvoiceSyncResponse;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBCommonAPIErrCodes;
import com.util.EJBContextClass;
import com.util.mod.ar.ArModInvoiceDetails;
import com.util.mod.ar.ArModInvoiceLineDetails;
import com.util.mod.ar.ArModSalesOrderDetails;
import com.util.mod.ar.ArModSalesOrderLineDetails;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import java.text.SimpleDateFormat;
import java.util.*;

@Stateless(name = "ArInvoiceSyncControllerBeanEJB")
public class ArInvoiceSyncControllerBean extends EJBContextClass implements ArInvoiceSyncController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalArTaxCodeHome arTaxCodeHome;
    @EJB
    private LocalArWithholdingTaxCodeHome arWithholdingTaxCodeHome;
    @EJB
    private LocalArInvoiceBatchHome arInvoiceBatchHome;
    @EJB
    private LocalAdPaymentTermHome adPaymentTermHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalArStandardMemoLineHome arStandardMemoLineHome;
    @EJB
    private LocalArInvoicePaymentScheduleHome arInvoicePaymentScheduleHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalAdPaymentScheduleHome adPaymentScheduleHome;
    @EJB
    private LocalAdBranchCustomerHome adBranchCustomerHome;
    @EJB
    private LocalAdDiscountHome adDiscountHome;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalArInvoiceLineHome arInvoiceLineHome;
    @EJB
    private LocalAdBranchStandardMemoLineHome adBranchStandardMemoLineHome;
    @EJB
    private LocalArAutoAccountingSegmentHome arAutoAccountingSegmentHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalArSalesOrderHome arSalesOrderHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalArSalespersonHome arSalespersonHome;
    @EJB
    private LocalArSalesOrderLineHome arSalesOrderLineHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private LocalAdAmountLimitHome adAmountLimitHome;
    @EJB
    private LocalAdApprovalUserHome adApprovalUserHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;

    @Override
    public ArInvoiceSyncResponse setArInvoiceAllNewAndVoid(ArInvoiceSyncRequest request) {

        Debug.print("ArInvoiceSyncControllerBean setArInvoiceAllNewAndVoid");

        ArInvoiceSyncResponse response = new ArInvoiceSyncResponse();

        LocalAdCompany adCompany;
        LocalAdBranch adBranch;
        Integer companyCode = null;
        Integer branchCode = null;
        String companyShortName = null;

        try {
            // Company Code
            try {
                if (request.getCompanyCode() == null) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_041);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_041_MSG);
                    return response;
                }
                adCompany = adCompanyHome.findByCmpShortName(request.getCompanyCode());
                if (adCompany != null) {
                    companyCode = adCompany.getCmpCode();
                    companyShortName = adCompany.getCmpShortName();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
                return response;
            }

            // Branch Code
            try {
                if (request.getBranchCode() == null || request.getBranchCode().equals("")) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return response;
                }
                assert adCompany != null;
                adBranch = adBranchHome.findByBrBranchCode(request.getBranchCode(), adCompany.getCmpCode(), companyShortName);
                if (adBranch != null) {
                    branchCode = adBranch.getBrCode();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_065);
                response.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_065_MSG, request.getBranchCode()));
                return response;
            }

            String[] invoices = request.getInvoices();
            int count = this.setArInvoiceAllNewAndVoid(invoices, branchCode, companyCode, companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setCount(count);
            response.setStatus("Set all invoice new and void data successfully.");
        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    @Override
    public ArInvoiceSyncResponse setArSoNew(ArInvoiceSyncRequest request) {

        Debug.print("ArInvoiceSyncControllerBean setArSoNew");

        ArInvoiceSyncResponse response = new ArInvoiceSyncResponse();

        LocalAdCompany adCompany;
        LocalAdBranch adBranch;
        Integer companyCode = null;
        Integer branchCode = null;
        String companyShortName = null;

        try {
            // Company Code
            try {
                if (request.getCompanyCode() == null) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_041);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_041_MSG);
                    return response;
                }
                adCompany = adCompanyHome.findByCmpShortName(request.getCompanyCode());
                if (adCompany != null) {
                    companyCode = adCompany.getCmpCode();
                    companyShortName = adCompany.getCmpShortName();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
                return response;
            }

            // Branch Code
            try {
                if (request.getBranchCode() == null || request.getBranchCode().equals("")) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return response;
                }
                assert adCompany != null;
                adBranch = adBranchHome.findByBrBranchCode(request.getBranchCode(), adCompany.getCmpCode(), companyShortName);
                if (adBranch != null) {
                    branchCode = adBranch.getBrCode();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_065);
                response.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_065_MSG, request.getBranchCode()));
                return response;
            }

            String[] salesOrders = request.getInvoices();
            int count = this.setArSoNew(salesOrders, branchCode, companyCode, companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setCount(count);
            response.setStatus("Set all invoice new and void data successfully.");
        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    private int setArInvoiceAllNewAndVoid(String[] invoices, Integer branchCode, Integer companyCode, String companyShortName) {

        Debug.print("ArInvoiceSyncControllerBean setArInvoiceAllNewAndVoid");

        try {
            int success = 0;
            for (int ctr = 0; ctr < invoices.length; ctr++) {

                ArModInvoiceDetails arModInvoiceDetails = invoiceDecode(invoices[ctr]);

                // generate invoice number
                LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
                LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;

                try {
                    adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AR INVOICE", companyCode);
                }
                catch (FinderException ex) {
                }

                try {
                    adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome
                            .findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode);
                }
                catch (FinderException ex) {
                }

                LocalArInvoice arExistingInvoice = null;
                try {
                    arExistingInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(
                            arModInvoiceDetails.getInvNumber(), EJBCommon.FALSE, branchCode, companyCode);
                }
                catch (FinderException ex) {
                }

                if (arExistingInvoice != null) {
                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' &&
                        (arModInvoiceDetails.getInvNumber() == null || arModInvoiceDetails.getInvNumber().trim().length() == 0)) {
                    while (true) {
                        if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {
                            try {
                                arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(
                                        adDocumentSequenceAssignment.getDsaNextSequence(), EJBCommon.FALSE, branchCode, companyCode);
                                adDocumentSequenceAssignment.setDsaNextSequence(
                                        EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                            }
                            catch (FinderException ex) {
                                arModInvoiceDetails.setInvNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                                adDocumentSequenceAssignment.setDsaNextSequence(
                                        EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                break;
                            }
                        } else {
                            try {
                                arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(
                                        adBranchDocumentSequenceAssignment.getBdsNextSequence(), EJBCommon.FALSE, branchCode, companyCode);
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(
                                        EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                            }
                            catch (FinderException ex) {
                                arModInvoiceDetails.setInvNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(
                                        adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                break;
                            }
                        }
                    }
                }

                double totalAmount = EJBCommon.roundIt((arModInvoiceDetails.getInvAmountDue()) * arModInvoiceDetails.getInvConversionRate(), (short) 2);

                arModInvoiceDetails.setInvAdBranch(branchCode);
                arModInvoiceDetails.setInvAdCompany(companyCode);
                arModInvoiceDetails.setInvAmountDue(totalAmount);
                arModInvoiceDetails.setInvTotalCredit(totalAmount);
                arModInvoiceDetails.setInvTotalDebit(totalAmount);

                LocalArInvoice arInvoice = arInvoiceHome.create(arModInvoiceDetails.getInvType(), EJBCommon.FALSE,
                        arModInvoiceDetails.getInvDescription(), arModInvoiceDetails.getInvDate(),
                        arModInvoiceDetails.getInvNumber(), arModInvoiceDetails.getInvReferenceNumber(), arModInvoiceDetails.getInvUploadNumber(), null, null,
                        0d, 0d, 0d, 0d, 0d, 0d, arModInvoiceDetails.getInvConversionDate(), arModInvoiceDetails.getInvConversionRate(),
                        arModInvoiceDetails.getInvMemo(),
                        0d, 0d, arModInvoiceDetails.getInvBillToAddress(), arModInvoiceDetails.getInvBillToContact(), arModInvoiceDetails.getInvBillToAltContact(),
                        arModInvoiceDetails.getInvBillToPhone(), arModInvoiceDetails.getInvBillingHeader(), arModInvoiceDetails.getInvBillingFooter(),
                        arModInvoiceDetails.getInvBillingHeader2(), arModInvoiceDetails.getInvBillingFooter2(), arModInvoiceDetails.getInvBillingHeader3(),
                        arModInvoiceDetails.getInvBillingFooter3(), arModInvoiceDetails.getInvBillingSignatory(), arModInvoiceDetails.getInvSignatoryTitle(),
                        arModInvoiceDetails.getInvShipToAddress(), arModInvoiceDetails.getInvShipToContact(), arModInvoiceDetails.getInvShipToAltContact(),
                        arModInvoiceDetails.getInvShipToPhone(), arModInvoiceDetails.getInvShipDate(), arModInvoiceDetails.getInvLvFreight(),
                        null, null,
                        EJBCommon.FALSE,
                        null, EJBCommon.FALSE, EJBCommon.FALSE,
                        EJBCommon.FALSE, EJBCommon.FALSE,
                        EJBCommon.FALSE, null, 0d, null, null, null, null,
                        arModInvoiceDetails.getInvCreatedBy(), arModInvoiceDetails.getInvDateCreated(),
                        arModInvoiceDetails.getInvLastModifiedBy(), arModInvoiceDetails.getInvDateLastModified(),
                        null, null, null, null, EJBCommon.FALSE, null, null, null, arModInvoiceDetails.getInvDebitMemo(),
                        arModInvoiceDetails.getInvSubjectToCommission(), null, arModInvoiceDetails.getInvEffectivityDate(), branchCode, companyCode);

                //Foreign Keys
                try {
                    LocalGlFunctionalCurrency glFunctionalCurrency =
                            glFunctionalCurrencyHome.findByFcName(arModInvoiceDetails.getInvFcName(), companyCode);
                    arInvoice.setGlFunctionalCurrency(glFunctionalCurrency);
                }
                catch (Exception ex) {
                    Debug.print("Error Functional Currency:" + ex.getMessage());
                    throw new Exception("Error Functional Currency");
                }

                LocalArTaxCode arTaxCode;
                try {
                    arTaxCode = arTaxCodeHome.findByTcName(arModInvoiceDetails.getInvTcName(), companyCode);
                    arInvoice.setArTaxCode(arTaxCode);
                }
                catch (Exception ex) {
                    Debug.print("Error Tax Code:" + ex.getMessage());
                    throw new Exception("Error Functional Currency");
                }

                LocalArWithholdingTaxCode arWithholdingTaxCode;
                try {
                    arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName(arModInvoiceDetails.getInvWtcName(), companyCode);
                    arInvoice.setArWithholdingTaxCode(arWithholdingTaxCode);
                }
                catch (Exception ex) {
                    Debug.print("Error Withholding Tax Code:" + ex.getMessage());
                    throw new Exception("Error Withholding Tax Code");
                }

                try {
                    LocalArInvoiceBatch arInvoiceBatch = arInvoiceBatchHome.findByIbName(arModInvoiceDetails.getInvIbName(), branchCode, companyCode);
                    arInvoice.setArInvoiceBatch(arInvoiceBatch);

                }
                catch (Exception ex) {
                    Debug.print("Error Invoice Batch:" + ex.getMessage());

                }

                LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName(arModInvoiceDetails.getInvPytName(), companyCode);
                adPaymentTerm.addArInvoice(arInvoice);

                try {
                    LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(arModInvoiceDetails.getInvCstCustomerCode(), companyCode);
                    arInvoice.setArCustomer(arCustomer);
                }
                catch (Exception ex) {
                    Debug.print("Error Customer:" + ex.getMessage());
                    throw new Exception("Error Customer");
                }

                double TOTAL_TAX = 0d;
                double TOTAL_LINE = 0d;
                double TOTAL_UNTAXABLE = 0d;

                // Add Invoice Lines
                Iterator iter = arModInvoiceDetails.getInvIlList().iterator();
                while (iter.hasNext()) {
                    ArModInvoiceLineDetails arModInvoiceLineDetails = (ArModInvoiceLineDetails) iter.next();
                    LocalArInvoiceLine arInvoiceLine = this.addArIlEntry(arModInvoiceLineDetails, arInvoice, companyCode);
                    arInvoice.addArInvoiceLine(arInvoiceLine);

                    // Add Standard Memo Line
                    try {
                        LocalArStandardMemoLine arStandardMemoLine =
                                arStandardMemoLineHome.findBySmlName(arModInvoiceLineDetails.getIlSmlName(), companyCode);
                        arStandardMemoLine.addArInvoiceLine(arInvoiceLine);
                    }
                    catch (Exception ex) {
                        Debug.print("Error Standard Memo Line:" + ex.getMessage());
                    }

                    Integer COA_CODE = this.getArGlCoaRevenueAccount(arInvoiceLine, branchCode, companyCode);
                    this.addArDrEntry(arInvoice.getArDrNextLine(),
                            "REVENUE", EJBCommon.FALSE, arInvoiceLine.getIlAmount(),
                            COA_CODE, arInvoice, branchCode, companyCode);

                    TOTAL_LINE += arInvoiceLine.getIlAmount();
                    TOTAL_TAX += arInvoiceLine.getIlTaxAmount();

                    if (arInvoiceLine.getIlTax() == EJBCommon.FALSE) {
                        TOTAL_UNTAXABLE += arInvoiceLine.getIlAmount();
                    }

                }// end of while

                //	add tax distribution if necessary
                if (!arTaxCode.getTcType().equals("NONE") &&
                        !arTaxCode.getTcType().equals("EXEMPT")) {

                    if (arTaxCode.getTcInterimAccount() == null) {

                        this.addArDrEntry(arInvoice.getArDrNextLine(),
                                "TAX", EJBCommon.FALSE, TOTAL_TAX, arTaxCode.getGlChartOfAccount().getCoaCode(),
                                arInvoice, branchCode, companyCode);
                    } else {
                        this.addArDrEntry(arInvoice.getArDrNextLine(),
                                "DEFERRED TAX", EJBCommon.FALSE, TOTAL_TAX, arTaxCode.getTcInterimAccount(),
                                arInvoice, branchCode, companyCode);
                    }
                }

                // add wtax distribution if necessary
                LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
                double W_TAX_AMOUNT = 0d;
                if (arWithholdingTaxCode.getWtcRate() != 0 && adPreference.getPrfArWTaxRealization().equals("INVOICE")) {
                    W_TAX_AMOUNT = EJBCommon.roundIt((TOTAL_LINE - TOTAL_UNTAXABLE) * (arWithholdingTaxCode.getWtcRate() / 100),
                            this.getGlFcPrecisionUnit(companyCode));
                    this.addArDrEntry(arInvoice.getArDrNextLine(), "W-TAX",
                            EJBCommon.TRUE, W_TAX_AMOUNT, arWithholdingTaxCode.getGlChartOfAccount().getCoaCode(),
                            arInvoice, branchCode, companyCode);
                }

                //  add payment discount if necessary
                double DISCOUNT_AMT = 0d;

                if (adPaymentTerm.getPytDiscountOnInvoice() == EJBCommon.TRUE) {

                    Collection adPaymentSchedules = adPaymentScheduleHome.findByPytCode(adPaymentTerm.getPytCode(), companyCode);
                    ArrayList adPaymentScheduleList = new ArrayList(adPaymentSchedules);
                    LocalAdPaymentSchedule adPaymentSchedule = (LocalAdPaymentSchedule) adPaymentScheduleList.get(0);

                    Collection adDiscounts = adDiscountHome.findByPsCode(adPaymentSchedule.getPsCode(), companyCode);
                    ArrayList adDiscountList = new ArrayList(adDiscounts);
                    LocalAdDiscount adDiscount = (LocalAdDiscount) adDiscountList.get(0);

                    double rate = adDiscount.getDscDiscountPercent();
                    DISCOUNT_AMT = (TOTAL_LINE + TOTAL_TAX) * (rate / 100d);

                    this.addArDrIliEntry(arInvoice.getArDrNextLine(), "DISCOUNT",
                            EJBCommon.TRUE, DISCOUNT_AMT,
                            adPaymentTerm.getGlChartOfAccount().getCoaCode(),
                            arInvoice, branchCode, companyCode);

                }

                // add receivable distribution
                try {
                    LocalAdBranchCustomer adBranchCustomer = adBranchCustomerHome
                            .findBcstByCstCodeAndBrCode(arInvoice.getArCustomer().getCstCode(), branchCode, companyCode);
                    this.addArDrEntry(arInvoice.getArDrNextLine(), "RECEIVABLE",
                            EJBCommon.TRUE, TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT - DISCOUNT_AMT,
                            adBranchCustomer.getBcstGlCoaReceivableAccount(),
                            arInvoice, branchCode, companyCode);
                }
                catch (FinderException ex) {
                }

                double amountDue = 0;

                // compute invoice amount due
                amountDue = TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT - DISCOUNT_AMT;

                //set invoice amount due
                arInvoice.setInvAmountDue(amountDue);

                // create invoice payment schedule
                short precisionUnit = this.getGlFcPrecisionUnit(companyCode);
                double TOTAL_PAYMENT_SCHEDULE = 0d;

                GregorianCalendar gcPrevDateDue = new GregorianCalendar();
                GregorianCalendar gcDateDue = new GregorianCalendar();
                gcPrevDateDue.setTime(arInvoice.getInvEffectivityDate());
                Collection adPaymentSchedules = adPaymentTerm.getAdPaymentSchedules();
                Iterator i = adPaymentSchedules.iterator();
                while (i.hasNext()) {
                    LocalAdPaymentSchedule adPaymentSchedule = (LocalAdPaymentSchedule) i.next();

                    // get date due
                    if (arInvoice.getAdPaymentTerm().getPytScheduleBasis().equals("DEFAULT")) {
                        gcDateDue.setTime(arInvoice.getInvEffectivityDate());
                        gcDateDue.add(Calendar.DATE, adPaymentSchedule.getPsDueDay());
                    } else if (arInvoice.getAdPaymentTerm().getPytScheduleBasis().equals("MONTHLY")) {
                        gcDateDue = gcPrevDateDue;
                        gcDateDue.add(Calendar.MONTH, 1);
                        gcPrevDateDue = gcDateDue;
                    } else if (arInvoice.getAdPaymentTerm().getPytScheduleBasis().equals("BI-MONTHLY")) {
                        gcDateDue = gcPrevDateDue;
                        if (gcPrevDateDue.get(Calendar.MONTH) != 1) {
                            if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 31
                                    && gcPrevDateDue.get(Calendar.DATE) > 15
                                    && gcPrevDateDue.get(Calendar.DATE) < 31) {
                                gcDateDue.add(Calendar.DATE, 16);
                            } else {
                                gcDateDue.add(Calendar.DATE, 15);
                            }
                        } else if (gcPrevDateDue.get(Calendar.MONTH) == 1) {
                            if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 28
                                    && gcPrevDateDue.get(Calendar.DATE) == 14) {
                                gcDateDue.add(Calendar.DATE, 14);
                            } else if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 28
                                    && gcPrevDateDue.get(Calendar.DATE) >= 15 && gcPrevDateDue.get(Calendar.DATE) < 28) {
                                gcDateDue.add(Calendar.DATE, 13);
                            } else if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 29
                                    && gcPrevDateDue.get(Calendar.DATE) >= 15 && gcPrevDateDue.get(Calendar.DATE) < 29) {
                                gcDateDue.add(Calendar.DATE, 14);
                            } else {
                                gcDateDue.add(Calendar.DATE, 15);
                            }
                        }
                        gcPrevDateDue = gcDateDue;
                    }

                    // create a payment schedule
                    double PAYMENT_SCHEDULE_AMOUNT = 0;

                    // if last payment schedule subtract to avoid rounding difference error
                    if (i.hasNext()) {
                        PAYMENT_SCHEDULE_AMOUNT = EJBCommon.roundIt((adPaymentSchedule.getPsRelativeAmount()
                                / adPaymentTerm.getPytBaseAmount()) * arInvoice.getInvAmountDue(), precisionUnit);
                    } else {
                        PAYMENT_SCHEDULE_AMOUNT = arInvoice.getInvAmountDue() - TOTAL_PAYMENT_SCHEDULE;
                    }
                    LocalArInvoicePaymentSchedule arInvoicePaymentSchedule =
                            arInvoicePaymentScheduleHome.create(gcDateDue.getTime(),
                                    adPaymentSchedule.getPsLineNumber(),
                                    PAYMENT_SCHEDULE_AMOUNT,
                                    0d, EJBCommon.FALSE,
                                    (short) 0, gcDateDue.getTime(), 0d, 0d,
                                    companyCode);
                    arInvoice.addArInvoicePaymentSchedule(arInvoicePaymentSchedule);
                    TOTAL_PAYMENT_SCHEDULE += PAYMENT_SCHEDULE_AMOUNT;
                }
            }
            success = 1;
            return success;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private int setArSoNew(String[] salesOrders, Integer branchCode, Integer companyCode, String companyShortName) {

        Debug.print("ArInvoiceSyncControllerBean setArSoNew");

        try {
            for (int i = 0; i < salesOrders.length; i++) {
                Integer success;
                try {

                    ArModSalesOrderDetails details = soDecode(salesOrders[i]);
                    LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(details.getSoCstCustomerCode(), companyCode);
                    //details.setSoPytName(arCustomer.getAdPaymentTerm().getPytName());

                    ArrayList convertedItmList = new ArrayList();
                    Iterator itmList = details.getSoSolList().iterator();

                    short solCtr = 1;
                    while (itmList.hasNext()) {
                        ArModSalesOrderLineDetails arSol = (ArModSalesOrderLineDetails) itmList.next();
                        Collection bomCollection = invItemHome.findByIiName(arSol.getSolIiName(), companyCode).getInvBillOfMaterials();
                        Iterator bomItr = bomCollection.iterator();
                        while (bomItr.hasNext()) {
                            LocalInvBillOfMaterial invModBillOfMaterialDetails = (LocalInvBillOfMaterial) bomItr.next();
                            ArModSalesOrderLineDetails stockArSalesOrderLine = (ArModSalesOrderLineDetails) arSol.clone();
                            stockArSalesOrderLine.setSolLine(solCtr++);
                            stockArSalesOrderLine.setSolIiName(invModBillOfMaterialDetails.getBomIiName());
                            stockArSalesOrderLine.setSolLocName(invModBillOfMaterialDetails.getBomLocName());
                            stockArSalesOrderLine.setSolUomName(invModBillOfMaterialDetails.getInvUnitOfMeasure().getUomName());
                            stockArSalesOrderLine.setSolQuantity(invModBillOfMaterialDetails.getBomQuantityNeeded() * arSol.getSolQuantity());
                            stockArSalesOrderLine.setSolUnitPrice(invItemHome.findByIiName(invModBillOfMaterialDetails.getBomIiName(), companyCode).getIiSalesPrice());
                            stockArSalesOrderLine.setSolAmount(stockArSalesOrderLine.getSolQuantity() * stockArSalesOrderLine.getSolUnitPrice());
                            convertedItmList.add(stockArSalesOrderLine);
                        }

                        // Add Actual Item If BOM is Not Found
                        if (bomCollection.size() == 0) {
                            arSol.setSolLine(solCtr++);
                            convertedItmList.add(arSol);
                        }
                    }

                    // Combine Quantities of Same Items
                    ArrayList itmNameList = new ArrayList();
                    Iterator iter = ((ArrayList) convertedItmList.clone()).iterator();
                    convertedItmList = new ArrayList();
                    while (iter.hasNext()) {
                        ArModSalesOrderLineDetails stockArSalesOrderLine = (ArModSalesOrderLineDetails) iter.next();
                        int index = itmNameList.indexOf(stockArSalesOrderLine.getSolIiName());
                        if (index < 0) {
                            convertedItmList.add(stockArSalesOrderLine);
                            itmNameList.add(stockArSalesOrderLine.getSolIiName());
                        } else {
                            ArModSalesOrderLineDetails existingSol = (ArModSalesOrderLineDetails) details.getSoSolList().toArray()[index];
                            stockArSalesOrderLine.setSolQuantity(existingSol.getSolQuantity() + stockArSalesOrderLine.getSolQuantity());
                            convertedItmList.remove(index);
                            convertedItmList.add(stockArSalesOrderLine);
                        }
                    }

                    details.setSoSolList(convertedItmList);
                    success = saveArSoEntry(details, details.getSoPytName(),
                            details.getSoTcName(), details.getSoFcName(),
                            details.getSoCstCustomerCode(), true,
                            branchCode, companyCode);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    throw new Exception("Error on Uploading No: " + i + "\n" + ex.getMessage());
                }
                if (success == 0) {
                    throw new Exception("Error on Uploading No: " + i);
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
        return 1;
    }

    private ArModInvoiceDetails invoiceDecode(String invoice) throws Exception {

        Debug.print("ArInvoiceSyncControllerBean invoiceDecode");

        String separator = "$";
        ArModInvoiceDetails arModInvoiceDetails = new ArModInvoiceDetails();

        // Remove first $ character
        invoice = invoice.substring(1);

        // Invoice Credit Memo
        int start = 0;
        int nextIndex = invoice.indexOf(separator, start);
        int length = nextIndex - start;
        arModInvoiceDetails.setInvCreditMemo((byte) Integer.parseInt(invoice.substring(start, start + length)));
        Debug.print("Invoice Credit Memo = " + arModInvoiceDetails.getInvCreditMemo());

        // Invoice Description
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        arModInvoiceDetails.setInvDescription(invoice.substring(start, start + length));
        Debug.print("Invoice Description = " + arModInvoiceDetails.getInvDescription());

        // Invoice Date
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        sdf.setLenient(false);
        try {
            arModInvoiceDetails.setInvDate(sdf.parse(invoice.substring(start, start + length)));
            Debug.print("Date = " + arModInvoiceDetails.getInvDate());
        }
        catch (Exception ex) {
            throw ex;
        }

        // Invoice Number
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        arModInvoiceDetails.setInvNumber(invoice.substring(start, start + length));
        Debug.print("Invoice Number = " + arModInvoiceDetails.getInvNumber());

        // Invoice Reference Number
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        arModInvoiceDetails.setInvReferenceNumber(invoice.substring(start, start + length));
        Debug.print("Invoice Reference Number = " + arModInvoiceDetails.getInvReferenceNumber());

        // Invoice Amount Due
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        arModInvoiceDetails.setInvAmountDue(Double.parseDouble(invoice.substring(start, start + length)));
        Debug.print("Invoice Amount Due = " + arModInvoiceDetails.getInvAmountDue());

        //Invoice Amount Paid
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        arModInvoiceDetails.setInvAmountPaid(Double.parseDouble(invoice.substring(start, start + length)));
        Debug.print("Invoice Amount Paid = " + arModInvoiceDetails.getInvAmountPaid());

        //Invoice Conversion Date
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        sdf.setLenient(false);
        try {
            //arModInvoiceDetails.setInvConversionDate(sdf.parse(invoice.substring(start, start + length)));
            Debug.print("Invoice Conversion Date = " + arModInvoiceDetails.getInvConversionDate());
        }
        catch (Exception ex) {
            throw ex;
        }

        // Invoice Conversion Rate
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        arModInvoiceDetails.setInvConversionRate(Double.parseDouble(invoice.substring(start, start + length)));
        Debug.print("Invoice Conversion Rate = " + arModInvoiceDetails.getInvConversionRate());

        // Invoice Bill To Address
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        Debug.print("Invoice Bill To Address = " + arModInvoiceDetails.getInvBillToAddress());

        // Invoice Bill To Contact
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        //arModInvoiceDetails.setInvBillToContact(invoice.substring(start, start + length));
        Debug.print("Invoice Bill To Contact = " + arModInvoiceDetails.getInvBillToContact());

        // Invoice Bill Alternate Contact
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        //arModInvoiceDetails.setInvBillToAltContact(invoice.substring(start, start + length));
        Debug.print("Invoice Bill Alternate Contact = " + arModInvoiceDetails.getInvBillToContact());

        // Invoice Bill To Phone
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        //arModInvoiceDetails.setInvBillToPhone(invoice.substring(start, start + length));
        Debug.print("Invoice Bill To Phone = " + arModInvoiceDetails.getInvBillToPhone());

        //  Invoice Billing Header
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        //arModInvoiceDetails.setInvBillingHeader(invoice.substring(start, start + length));
        Debug.print("Invoice Billing Header = " + arModInvoiceDetails.getInvBillingHeader());

        // Invoice Billing Footer
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        //arModInvoiceDetails.setInvBillingFooter(invoice.substring(start, start + length));
        Debug.print("Invoice Billing Footer = " + arModInvoiceDetails.getInvBillingFooter());

        // Invoice Billing Header 2
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        //arModInvoiceDetails.setInvBillingHeader2(invoice.substring(start, start + length));
        Debug.print("Invoice Billing Header 2 = " + arModInvoiceDetails.getInvBillingHeader2());

        // Invoice Billing Footer 2
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        //arModInvoiceDetails.setInvBillingFooter2(invoice.substring(start, start + length));
        Debug.print("Invoice Billing Footer 2 = " + arModInvoiceDetails.getInvBillingFooter2());

        // Invoice Billing Header 3
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        //arModInvoiceDetails.setInvBillingHeader3(invoice.substring(start, start + length));
        Debug.print("Invoice Billing Header 3 = " + arModInvoiceDetails.getInvBillingHeader3());

        // Invoice Billing Footer 3
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        //arModInvoiceDetails.setInvBillingFooter3(invoice.substring(start, start + length));
        Debug.print("Invoice Billing Footer 3 = " + arModInvoiceDetails.getInvBillingFooter3());

        // Invoice Billing Signatory
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        //arModInvoiceDetails.setInvBillingSignatory(invoice.substring(start, start + length));
        Debug.print("Invoice Billing Signatory = " + arModInvoiceDetails.getInvBillingSignatory());

        // Invoice Signatory Title
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        //arModInvoiceDetails.setInvSignatoryTitle(invoice.substring(start, start + length));
        System.out.println("Invoice Signatory Title = " + arModInvoiceDetails.getInvSignatoryTitle());

        // Invoice Ship To Address
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        //arModInvoiceDetails.setInvShipToAddress(invoice.substring(start, start + length));
        System.out.println("Invoice Ship To Address = " + arModInvoiceDetails.getInvShipToAddress());

        // Invoice Ship To Contact
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        //arModInvoiceDetails.setInvShipToContact(invoice.substring(start, start + length));
        System.out.println("Invoice Ship To Contact = " + arModInvoiceDetails.getInvShipToContact());

        // Invoice Ship To Alternate Contact
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        //arModInvoiceDetails.setInvShipToAltContact(invoice.substring(start, start + length));
        System.out.println("Invoice Ship To Alternate Contact = " + arModInvoiceDetails.getInvShipToAltContact());

        // Invoice Ship To Phone
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        //arModInvoiceDetails.setInvShipToPhone(invoice.substring(start, start + length));
        System.out.println("Invoice Ship To Phone = " + arModInvoiceDetails.getInvShipToPhone());

        // Invoice Ship Date
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        sdf.setLenient(false);
        try {
            //arModInvoiceDetails.setInvShipDate(sdf.parse(invoice.substring(start, start + length)));
            System.out.println("Date = " + arModInvoiceDetails.getInvDateLastModified());
        }
        catch (Exception ex) {
            throw ex;
        }

        // Invoice Freight
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        //arModInvoiceDetails.setInvLvFreight(invoice.substring(start, start + length));
        System.out.println("Invoice Freight = " + arModInvoiceDetails.getInvLvFreight());

        // Invoice Approval Status
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        //arModInvoiceDetails.setInvApprovalStatus(invoice.substring(start, start + length));
        System.out.println("Invoice Approval Status = " + arModInvoiceDetails.getInvApprovalStatus());

        // Invoice Reason For Rejection
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        //arModInvoiceDetails.setInvReasonForRejection(invoice.substring(start, start + length));
        System.out.println("Invoice Reason For Rejection = " + arModInvoiceDetails.getInvReasonForRejection());

        // Invoice Posted
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        arModInvoiceDetails.setInvPosted((byte) Integer.parseInt(invoice.substring(start, start + length)));
        System.out.println("Invoice Posted = " + arModInvoiceDetails.getInvPosted());

        // Invoice Created By
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        arModInvoiceDetails.setInvCreatedBy(invoice.substring(start, start + length));
        System.out.println("Invoice Created By = " + arModInvoiceDetails.getInvCreatedBy());

        // Invoice Date Created
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        sdf.setLenient(false);
        try {
            arModInvoiceDetails.setInvDateCreated(sdf.parse(invoice.substring(start, start + length)));
            System.out.println("Date = " + arModInvoiceDetails.getInvDateLastModified());
        }
        catch (Exception ex) {
            throw ex;
        }

        // Invoice Last Modified By
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        arModInvoiceDetails.setInvLastModifiedBy(invoice.substring(start, start + length));
        System.out.println("Invoice Last Modified By = " + arModInvoiceDetails.getInvLastModifiedBy());

        // Invoice Date Last Modified
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        sdf.setLenient(false);
        try {
            arModInvoiceDetails.setInvDateLastModified(sdf.parse(invoice.substring(start, start + length)));
            System.out.println("Date=" + arModInvoiceDetails.getInvDateLastModified());
        }
        catch (Exception ex) {
            throw ex;
        }

        // Invoice Approved Rejected By
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        //arModInvoiceDetails.setInvApprovedRejectedBy(invoice.substring(start, start + length));
        System.out.println("Invoice Approved Rejected By = " + arModInvoiceDetails.getInvApprovedRejectedBy());

        // Invoice Date Approved Rejected
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        sdf.setLenient(false);
        try {
            //arModInvoiceDetails.setInvDateApprovedRejected(sdf.parse(invoice.substring(start, start + length)));
            System.out.println("Date=" + arModInvoiceDetails.getInvDateApprovedRejected());
        }
        catch (Exception ex) {
            throw ex;
        }

        // Invoice Posted By
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        //arModInvoiceDetails.setInvPostedBy(invoice.substring(start, start + length));
        System.out.println("Invoice Posted By = " + arModInvoiceDetails.getInvPostedBy());

        // Invoice Date Posted
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        sdf.setLenient(false);
        try {
            //arModInvoiceDetails.setInvDatePosted(sdf.parse(invoice.substring(start, start + length)));
            System.out.println("Date =" + arModInvoiceDetails.getInvDatePosted());
        }
        catch (Exception ex) {
            throw ex;
        }

        // Effectivity Date Posted
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        sdf.setLenient(false);
        try {
            arModInvoiceDetails.setInvEffectivityDate(sdf.parse(invoice.substring(start, start + length)));
            System.out.println("Effectivity Date =" + arModInvoiceDetails.getInvEffectivityDate());
        }
        catch (Exception ex) {

            throw ex;
        }
        // GL Functional Currency
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        arModInvoiceDetails.setInvFcName(invoice.substring(start, start + length));
        System.out.println("GL Functional Currency = " + arModInvoiceDetails.getInvFcName());

        // Tax Code
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        arModInvoiceDetails.setInvTcName(invoice.substring(start, start + length));
        System.out.println("Tax Code = " + arModInvoiceDetails.getInvTcName());

        // Withholding Tax Code
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        arModInvoiceDetails.setInvWtcName(invoice.substring(start, start + length));
        System.out.println("Withholding Tax Code = " + arModInvoiceDetails.getInvWtcName());

        // Invoice Batch
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        arModInvoiceDetails.setInvIbName(invoice.substring(start, start + length));
        System.out.println("Invoice Batch = " + arModInvoiceDetails.getInvIbName());

        // Payment Term
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        arModInvoiceDetails.setInvPytName(invoice.substring(start, start + length));
        System.out.println("Payment Term = " + arModInvoiceDetails.getInvPytName());

        // Customer Name
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;
        System.out.println("Actual Cst = " + invoice.substring(start, start + length));
        arModInvoiceDetails.setInvCstCustomerCode(invoice.substring(start, start + length));
        System.out.println("Customer Name = " + arModInvoiceDetails.getInvCstCustomerCode());

        // end separator
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(separator, start);
        length = nextIndex - start;

        String lineSeparator = "~";

        // begin lineSeparator
        start = nextIndex + 1;
        nextIndex = invoice.indexOf(lineSeparator, start);
        length = nextIndex - start;

        ArrayList invIlList = new ArrayList();

        while (true) {

            ArModInvoiceLineDetails arModInvoiceLineDetails = new ArModInvoiceLineDetails();

            // begin separator
            start = nextIndex + 1;
            nextIndex = invoice.indexOf(separator, start);
            length = nextIndex - start;

            // Description
            start = nextIndex + 1;
            nextIndex = invoice.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineDetails.setIlDescription(invoice.substring(start, start + length));
            System.out.println("Description =" + arModInvoiceLineDetails.getIlDescription());

            // quantity
            start = nextIndex + 1;
            nextIndex = invoice.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineDetails.setIlQuantity(Double.parseDouble(invoice.substring(start, start + length)));
            System.out.println("Quantity =" + arModInvoiceLineDetails.getIlQuantity());

            // price
            start = nextIndex + 1;
            nextIndex = invoice.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineDetails.setIlUnitPrice(Double.parseDouble(invoice.substring(start, start + length)));
            System.out.println("Price =" + arModInvoiceLineDetails.getIlUnitPrice());

            // Amount
            start = nextIndex + 1;
            nextIndex = invoice.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineDetails.setIlAmount(Double.parseDouble(invoice.substring(start, start + length)));
            System.out.println(" Amount  =" + arModInvoiceLineDetails.getIlAmount());

            // Tax Amount
            start = nextIndex + 1;
            nextIndex = invoice.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineDetails.setIlTaxAmount(Double.parseDouble(invoice.substring(start, start + length)));
            System.out.println(" Tax Amount  =" + arModInvoiceLineDetails.getIlTaxAmount());

            // Taxable
            start = nextIndex + 1;
            nextIndex = invoice.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineDetails.setIlTax((byte) Integer.parseInt(invoice.substring(start, start + length)));
            System.out.println(" Taxable  =" + arModInvoiceLineDetails.getIlTax());

            // Standard Memo Name
            start = nextIndex + 1;
            nextIndex = invoice.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineDetails.setIlSmlName(invoice.substring(start, start + length));
            System.out.println(" Standard Memo Name  =" + arModInvoiceLineDetails.getIlSmlName());

            invIlList.add(arModInvoiceLineDetails);

            // begin lineSeparator
            start = nextIndex + 1;
            nextIndex = invoice.indexOf(lineSeparator, start);
            length = nextIndex - start;

            int tempStart = nextIndex + 1;
            if (invoice.indexOf(separator, tempStart) == -1) {
                break;
            }
        }
        arModInvoiceDetails.setInvIlList(invIlList);
        return arModInvoiceDetails;

    }

    private LocalArInvoiceLine addArIlEntry(ArModInvoiceLineDetails mdetails, LocalArInvoice arInvoice, Integer AD_CMPNY) {

        Debug.print("ArInvoiceSyncControllerBean addArIlEntry");
        try {
            short precisionUnit = this.getGlFcPrecisionUnit(AD_CMPNY);
            double IL_AMNT = 0d;
            double IL_TAX_AMNT = 0d;
            if (mdetails.getIlTax() == EJBCommon.TRUE) {
                LocalArTaxCode arTaxCode = arInvoice.getArTaxCode();
                // calculate net amount
                IL_AMNT = this.calculateIlNetAmount(mdetails, arTaxCode.getTcRate(), arTaxCode.getTcType(), precisionUnit);
                // calculate tax
                IL_TAX_AMNT = this.calculateIlTaxAmount(mdetails, arTaxCode.getTcRate(), arTaxCode.getTcType(), IL_AMNT, precisionUnit);
            } else {
                IL_AMNT = mdetails.getIlAmount();
            }

            LocalArInvoiceLine arInvoiceLine = arInvoiceLineHome.create(
                    mdetails.getIlDescription(), mdetails.getIlQuantity(),
                    mdetails.getIlUnitPrice(), IL_AMNT,
                    IL_TAX_AMNT, mdetails.getIlTax(), AD_CMPNY);

            arInvoice.addArInvoiceLine(arInvoiceLine);

            LocalArStandardMemoLine arStandardMemoLine = arStandardMemoLineHome.findBySmlName(mdetails.getIlSmlName(), AD_CMPNY);
            arStandardMemoLine.addArInvoiceLine(arInvoiceLine);
            return arInvoiceLine;
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double calculateIlNetAmount(ArModInvoiceLineDetails mdetails, double tcRate, String tcType, short precisionUnit) {

        Debug.print("ArInvoiceSyncControllerBean calculateIlNetAmount");
        double amount = 0d;
        if (tcType.equals("INCLUSIVE")) {
            amount = EJBCommon.roundIt(mdetails.getIlAmount() / (1 + (tcRate / 100)), precisionUnit);
        } else {
            // tax exclusive, none, zero rated or exempt
            amount = mdetails.getIlAmount();
        }
        return amount;
    }

    private double calculateIlTaxAmount(ArModInvoiceLineDetails mdetails, double tcRate, String tcType, double amount, short precisionUnit) {

        Debug.print("ArInvoiceSyncControllerBean calculateIlTaxAmount");
        double taxAmount = 0d;
        if (!tcType.equals("NONE") && !tcType.equals("EXEMPT")) {
            if (tcType.equals("INCLUSIVE")) {
                taxAmount = EJBCommon.roundIt(mdetails.getIlAmount() - amount, precisionUnit);
            } else if (tcType.equals("EXCLUSIVE")) {
                taxAmount = EJBCommon.roundIt(mdetails.getIlAmount() * tcRate / 100, precisionUnit);
            }
        }
        return taxAmount;
    }

    private Integer getArGlCoaRevenueAccount(LocalArInvoiceLine arInvoiceLine, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ArInvoiceSyncControllerBean getArGlCoaRevenueAccount");

        // generate revenue account
        try {

            StringBuilder GL_COA_ACCNT = new StringBuilder();
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            LocalGenField genField = adCompany.getGenField();
            String FL_SGMNT_SPRTR = String.valueOf(genField.getFlSegmentSeparator());
            LocalAdBranchStandardMemoLine adBranchStandardMemoLine = null;
            try {
                adBranchStandardMemoLine = adBranchStandardMemoLineHome
                        .findBSMLBySMLCodeAndBrCode(arInvoiceLine.getArStandardMemoLine().getSmlCode(), AD_BRNCH, AD_CMPNY);
            }
            catch (FinderException ex) {
                System.out.println(ex.getMessage());
            }

            Collection arAutoAccountingSegments = arAutoAccountingSegmentHome.findByAaAccountType("REVENUE", AD_CMPNY);
            Iterator i = arAutoAccountingSegments.iterator();
            while (i.hasNext()) {
                LocalArAutoAccountingSegment arAutoAccountingSegment =
                        (LocalArAutoAccountingSegment) i.next();
                LocalGlChartOfAccount glChartOfAccount = null;
                if (arAutoAccountingSegment.getAasClassType().equals("AR CUSTOMER")) {

                    glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(
                            arInvoiceLine.getArInvoice().getArCustomer().getCstGlCoaRevenueAccount());

                    StringTokenizer st = new StringTokenizer(
                            glChartOfAccount.getCoaAccountNumber(), FL_SGMNT_SPRTR);

                    int ctr = 0;
                    while (st.hasMoreTokens()) {
                        ++ctr;
                        if (ctr == arAutoAccountingSegment.getAasSegmentNumber()) {
                            GL_COA_ACCNT.append(FL_SGMNT_SPRTR).append(st.nextToken());
                            break;
                        } else {
                            st.nextToken();
                        }
                    }
                } else if (arAutoAccountingSegment.getAasClassType().equals("AR STANDARD MEMO LINE")) {
                    if (adBranchStandardMemoLine != null) {
                        try {
                            glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchStandardMemoLine.getBsmlGlAccount());
                        }
                        catch (FinderException ex) {
                        }
                    } else {
                        glChartOfAccount = arInvoiceLine.getArStandardMemoLine().getGlChartOfAccount();
                    }

                    assert glChartOfAccount != null;
                    StringTokenizer st = new StringTokenizer(glChartOfAccount.getCoaAccountNumber(), FL_SGMNT_SPRTR);
                    int ctr = 0;
                    while (st.hasMoreTokens()) {
                        ++ctr;
                        if (ctr == arAutoAccountingSegment.getAasSegmentNumber()) {
                            GL_COA_ACCNT.append(FL_SGMNT_SPRTR).append(st.nextToken());
                            break;
                        } else {
                            st.nextToken();
                        }
                    }
                }
            }

            GL_COA_ACCNT = new StringBuilder(GL_COA_ACCNT.substring(1, GL_COA_ACCNT.length()));
            try {
                LocalGlChartOfAccount glGeneratedChartOfAccount =
                        glChartOfAccountHome.findByCoaAccountNumber(GL_COA_ACCNT.toString(), AD_CMPNY);
                return glGeneratedChartOfAccount.getCoaCode();
            }
            catch (FinderException ex) {
                if (adBranchStandardMemoLine != null) {
                    LocalGlChartOfAccount glChartOfAccount = null;
                    try {
                        glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchStandardMemoLine.getBsmlGlAccount());
                        ;
                    }
                    catch (FinderException e) {
                    }
                    return glChartOfAccount.getCoaCode();
                } else {
                    return arInvoiceLine.getArStandardMemoLine().getGlChartOfAccount().getCoaCode();
                }
            }
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void addArDrEntry(
            short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE,
            LocalArInvoice arInvoice, Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArInvoiceSyncControllerBean addArDrEntry");

        try {

            // get company
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, AD_BRNCH, AD_CMPNY);

            // create distribution record
            if (DR_AMNT < 0) {
                DR_AMNT = DR_AMNT * -1;
                if (DR_DBT == 0) {
                    DR_DBT = 1;
                } else if (DR_DBT == 1) {
                    DR_DBT = 0;
                }
            }

            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(
                    DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()),
                    EJBCommon.FALSE, EJBCommon.FALSE, AD_CMPNY);

            arInvoice.addArDistributionRecord(arDistributionRecord);
            glChartOfAccount.addArDistributionRecord(arDistributionRecord);

        }
        catch (FinderException ex) {
            throw new GlobalBranchAccountNumberInvalidException();
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void addArDrIliEntry(
            short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE,
            LocalArInvoice arInvoice, Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArInvoiceSyncControllerBean addArDrIliEntry");

        try {

            // get company
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(COA_CODE);

            // create distribution record
            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(
                    DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()),
                    EJBCommon.FALSE, EJBCommon.FALSE, AD_CMPNY);

            arInvoice.addArDistributionRecord(arDistributionRecord);
            glChartOfAccount.addArDistributionRecord(arDistributionRecord);

        }
        catch (FinderException ex) {
            throw new GlobalBranchAccountNumberInvalidException(ex.getMessage());
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private ArModSalesOrderDetails soDecode(String SO) throws Exception {

        Debug.print("ArInvoiceSyncControllerBean soDecode");

        try {
            String separator = "$";
            ArModSalesOrderDetails arModSalesOrderDetails = new ArModSalesOrderDetails();

            // Remove first $ character
            SO = SO.substring(1);

            // SO Description
            int start = 0;
            int nextIndex = SO.indexOf(separator, start);
            int length = nextIndex - start;
            arModSalesOrderDetails.setSoDescription(SO.substring(start, start + length));
            Debug.print("SO Description = " + arModSalesOrderDetails.getSoDescription());


            // SO Date
            start = nextIndex + 1;
            nextIndex = SO.indexOf(separator, start);
            length = nextIndex - start;
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            sdf.setLenient(false);
            try {
                arModSalesOrderDetails.setSoDate(sdf.parse(SO.substring(start, start + length)));
                Debug.print("SO Date Date = " + arModSalesOrderDetails.getSoDate());
            }
            catch (Exception ex) {
                Debug.print("SO Date Date Error");
                throw ex;
            }

            // SO Reference Number
            start = nextIndex + 1;
            nextIndex = SO.indexOf(separator, start);
            length = nextIndex - start;
            arModSalesOrderDetails.setSoReferenceNumber(SO.substring(start, start + length));
            Debug.print("SO Reference Number = " + arModSalesOrderDetails.getSoReferenceNumber());

            //SO Conversion Date
            start = nextIndex + 1;
            nextIndex = SO.indexOf(separator, start);
            length = nextIndex - start;
            sdf.setLenient(false);
            try {
                Debug.print("SO Conversion Date = " + arModSalesOrderDetails.getSoConversionDate());
            }
            catch (Exception ex) {

                throw ex;
            }

            // SO Conversion Rate
            start = nextIndex + 1;
            nextIndex = SO.indexOf(separator, start);
            length = nextIndex - start;
            arModSalesOrderDetails.setSoConversionRate(Double.parseDouble(SO.substring(start, start + length)));
            Debug.print("SO Conversion Rate = " + arModSalesOrderDetails.getSoConversionRate());

            // SO Posted
            start = nextIndex + 1;
            nextIndex = SO.indexOf(separator, start);
            length = nextIndex - start;
            arModSalesOrderDetails.setSoPosted((byte) 0);
            System.out.println("SO Posted = " + arModSalesOrderDetails.getSoPosted());

            // SO Created By
            start = nextIndex + 1;
            nextIndex = SO.indexOf(separator, start);
            length = nextIndex - start;
            arModSalesOrderDetails.setSoCreatedBy(SO.substring(start, start + length));
            System.out.println("SO Created By = " + arModSalesOrderDetails.getSoCreatedBy());

            // SO Date Created
            start = nextIndex + 1;
            nextIndex = SO.indexOf(separator, start);
            length = nextIndex - start;
            sdf.setLenient(false);
            try {
                arModSalesOrderDetails.setSoDateCreated(sdf.parse(SO.substring(start, start + length)));
                System.out.println("SO Date Created = " + arModSalesOrderDetails.getSoDateLastModified());
            }
            catch (Exception ex) {

                throw ex;
            }

            // SO Last Modified By
            start = nextIndex + 1;
            nextIndex = SO.indexOf(separator, start);
            length = nextIndex - start;
            arModSalesOrderDetails.setSoLastModifiedBy(SO.substring(start, start + length));
            System.out.println("SO Last Modified By = " + arModSalesOrderDetails.getSoLastModifiedBy());

            // Invoice Date Last Modified
            start = nextIndex + 1;
            nextIndex = SO.indexOf(separator, start);
            length = nextIndex - start;
            sdf.setLenient(false);
            try {
                arModSalesOrderDetails.setSoDateLastModified(sdf.parse(SO.substring(start, start + length)));
                System.out.println("Invoice Date Last Modified =" + arModSalesOrderDetails.getSoDateLastModified());
            }
            catch (Exception ex) {

                throw ex;
            }

            // SO Memo
            start = nextIndex + 1;
            nextIndex = SO.indexOf(separator, start);
            length = nextIndex - start;
            arModSalesOrderDetails.setSoMemo(SO.substring(start, start + length));
            System.out.println("Memo: - " + arModSalesOrderDetails.getSoMemo());

            // GL Functional Currency
            start = nextIndex + 1;
            nextIndex = SO.indexOf(separator, start);
            length = nextIndex - start;
            arModSalesOrderDetails.setSoFcName(SO.substring(start, start + length));
            System.out.println("GL Functional Currency = " + arModSalesOrderDetails.getSoFcName());

            // Tax Code
            start = nextIndex + 1;
            nextIndex = SO.indexOf(separator, start);
            length = nextIndex - start;
            arModSalesOrderDetails.setSoTcName(SO.substring(start, start + length));
            System.out.println("Tax Code = " + arModSalesOrderDetails.getSoTcName());

            // Payment Term
            start = nextIndex + 1;
            nextIndex = SO.indexOf(separator, start);
            length = nextIndex - start;
            arModSalesOrderDetails.setSoPytName(SO.substring(start, start + length));
            System.out.println("Payment Term = " + arModSalesOrderDetails.getSoPytName());

            // Customer Name
            start = nextIndex + 1;
            nextIndex = SO.indexOf(separator, start);
            length = nextIndex - start;
            arModSalesOrderDetails.setSoCstCustomerCode(SO.substring(start, start + length));
            System.out.println("Customer Name = " + arModSalesOrderDetails.getSoCstCustomerCode());

            // Salesperson
            start = nextIndex + 1;
            nextIndex = SO.indexOf(separator, start);
            length = nextIndex - start;
            arModSalesOrderDetails.setSoSlpSalespersonCode(SO.substring(start, start + length));

            // end separator
            start = nextIndex + 1;
            nextIndex = SO.indexOf(separator, start);
            length = nextIndex - start;

            String lineSeparator = "~";

            // begin lineSeparator
            start = nextIndex + 1;
            nextIndex = SO.indexOf(lineSeparator, start);
            length = nextIndex - start;

            ArrayList solList = new ArrayList();
            int x = 0;
            while (true) {

                ArModSalesOrderLineDetails arModSalesOrderLineDetails = new ArModSalesOrderLineDetails();

                // begin separator
                start = nextIndex + 1;
                nextIndex = SO.indexOf(separator, start);
                length = nextIndex - start;

                // Item Name
                start = nextIndex + 1;
                nextIndex = SO.indexOf(separator, start);
                length = nextIndex - start;
                arModSalesOrderLineDetails.setSolIiName(SO.substring(start, start + length));
                System.out.println("Item Name =" + arModSalesOrderLineDetails.getSolIiName());

                // Location
                start = nextIndex + 1;
                nextIndex = SO.indexOf(separator, start);
                length = nextIndex - start;
                arModSalesOrderLineDetails.setSolLocName(SO.substring(start, start + length));
                System.out.println("Location =" + arModSalesOrderLineDetails.getSolLocName());

                // quantity
                start = nextIndex + 1;
                nextIndex = SO.indexOf(separator, start);
                length = nextIndex - start;
                arModSalesOrderLineDetails.setSolQuantity(Double.parseDouble(SO.substring(start, start + length)));
                System.out.println("Quantity =" + arModSalesOrderLineDetails.getSolQuantity());

                // UOM
                start = nextIndex + 1;
                nextIndex = SO.indexOf(separator, start);
                length = nextIndex - start;
                arModSalesOrderLineDetails.setSolUomName(SO.substring(start, start + length));
                System.out.println("UOM =" + arModSalesOrderLineDetails.getSolUomName());

                // Unit Price
                start = nextIndex + 1;
                nextIndex = SO.indexOf(separator, start);
                length = nextIndex - start;
                arModSalesOrderLineDetails.setSolUnitPrice(Double.parseDouble(SO.substring(start, start + length)));
                System.out.println("Unit Price =" + arModSalesOrderLineDetails.getSolUnitPrice());

                // Amount
                start = nextIndex + 1;
                nextIndex = SO.indexOf(separator, start);
                length = nextIndex - start;
                arModSalesOrderLineDetails.setSolAmount(Double.parseDouble(SO.substring(start, start + length)));
                System.out.println(" Amount  =" + arModSalesOrderLineDetails.getSolAmount());

                // Discount 1
                start = nextIndex + 1;
                nextIndex = SO.indexOf(separator, start);
                length = nextIndex - start;
                arModSalesOrderLineDetails.setSolDiscount1(Double.parseDouble(SO.substring(start, start + length)));
                System.out.println("Discount 1  =" + arModSalesOrderLineDetails.getSolDiscount1());

                // Discount 2
                start = nextIndex + 1;
                nextIndex = SO.indexOf(separator, start);
                length = nextIndex - start;
                arModSalesOrderLineDetails.setSolDiscount2(Double.parseDouble(SO.substring(start, start + length)));
                System.out.println("Discount 2  =" + arModSalesOrderLineDetails.getSolDiscount2());

                // Discount 3
                start = nextIndex + 1;
                nextIndex = SO.indexOf(separator, start);
                length = nextIndex - start;
                arModSalesOrderLineDetails.setSolDiscount1(Double.parseDouble(SO.substring(start, start + length)));
                System.out.println("Discount 3  =" + arModSalesOrderLineDetails.getSolDiscount3());

                // Discount 4
                start = nextIndex + 1;
                nextIndex = SO.indexOf(separator, start);
                length = nextIndex - start;
                arModSalesOrderLineDetails.setSolDiscount4(Double.parseDouble(SO.substring(start, start + length)));
                System.out.println("Discount 4  =" + arModSalesOrderLineDetails.getSolDiscount4());

                // Total Discount
                start = nextIndex + 1;
                nextIndex = SO.indexOf(separator, start);
                length = nextIndex - start;
                arModSalesOrderLineDetails.setSolTotalDiscount(Double.parseDouble(SO.substring(start, start + length)));
                System.out.println("Total Discount  =" + arModSalesOrderLineDetails.getSolTotalDiscount());

                solList.add(arModSalesOrderLineDetails);

                // begin lineSeparator
                start = nextIndex + 1;
                nextIndex = SO.indexOf(lineSeparator, start);
                length = nextIndex - start;

                int tempStart = nextIndex + 1;
                if (SO.indexOf(separator, tempStart) == -1) {
                    break;
                }

            }

            arModSalesOrderDetails.setSoSolList(solList);

            return arModSalesOrderDetails;
        }
        catch (Exception ex) {

            throw ex;

        }

    }

    private Integer saveArSoEntry(ArModSalesOrderDetails details, String PYT_NM, String TC_NM, String FC_NM, String CST_CSTMR_CODE, boolean isDraft, Integer AD_BRNCH, Integer AD_CMPNY) throws
            GlobalRecordAlreadyDeletedException,
            GlobalDocumentNumberNotUniqueException,
            GlobalConversionDateNotExistException,
            GlobalPaymentTermInvalidException,
            GlobalTransactionAlreadyPendingException,
            GlobalTransactionAlreadyVoidException,
            GlobalInvItemLocationNotFoundException,
            GlobalNoApprovalApproverFoundException,
            GlobalNoApprovalRequesterFoundException,
            GlobalRecordAlreadyAssignedException {

        Debug.print("ArInvoiceSyncControllerBean saveArSoEntry");


        ArrayList solList = details.getSoSolList();

        LocalArSalesOrder arSalesOrder = null;

        try {

            // validate if sales order is already deleted
            try {
                if (details.getSoCode() != null) {
                    arSalesOrder = arSalesOrderHome.findByPrimaryKey(details.getSoCode());
                }
            }
            catch (FinderException ex) {
                throw new GlobalRecordAlreadyDeletedException();
            }

            // sales order void
            if (details.getSoCode() != null && details.getSoVoid() == EJBCommon.TRUE) {
                Collection arSalesOrderLines = arSalesOrder.getArSalesOrderLines();
                Iterator i = arSalesOrderLines.iterator();
                while (i.hasNext()) {
                    LocalArSalesOrderLine arSalesOrderLine = (LocalArSalesOrderLine) i.next();
                    if (!arSalesOrderLine.getArSalesOrderInvoiceLines().isEmpty()) {
                        throw new GlobalRecordAlreadyAssignedException();
                    }
                }
                arSalesOrder.setSoVoid(EJBCommon.TRUE);
                arSalesOrder.setSoLastModifiedBy(details.getSoLastModifiedBy());
                arSalesOrder.setSoDateLastModified(details.getSoDateLastModified());
                return arSalesOrder.getSoCode();

            }

            // validate if sales order is already posted, void, approved or pending
            if (details.getSoCode() != null) {
                if (arSalesOrder.getSoApprovalStatus() != null) {
                    if (arSalesOrder.getSoApprovalStatus().equals("APPROVED") ||
                            arSalesOrder.getSoApprovalStatus().equals("N/A")) {
                        return arSalesOrder.getSoCode();
                    } else if (arSalesOrder.getSoApprovalStatus().equals("PENDING")) {
                        throw new GlobalTransactionAlreadyPendingException();
                    }
                }

                if (arSalesOrder.getSoPosted() == EJBCommon.TRUE) {
                    return arSalesOrder.getSoCode();
                } else if (arSalesOrder.getSoVoid() == EJBCommon.TRUE) {
                    throw new GlobalTransactionAlreadyVoidException();
                }
            }

            // validate if document number is unique document number is automatic then set next sequence
            LocalArSalesOrder arExistingSalesOrder = null;
            try {
                arExistingSalesOrder = arSalesOrderHome.findBySoDocumentNumberAndBrCode(
                        details.getSoDocumentNumber(), AD_BRNCH, AD_CMPNY);
            }
            catch (FinderException ex) {
            }

            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
            if (details.getSoCode() == null) {
                try {
                    adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AR SALES ORDER", AD_CMPNY);
                }
                catch (FinderException ex) {
                }

                try {
                    adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome
                            .findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, AD_CMPNY);
                }
                catch (FinderException ex) {

                }

                if (arExistingSalesOrder != null) {
                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' &&
                        (details.getSoDocumentNumber() == null || details.getSoDocumentNumber().trim().length() == 0)) {

                    while (true) {
                        if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {
                            try {
                                arSalesOrderHome.findBySoDocumentNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, AD_CMPNY);
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                            }
                            catch (FinderException ex) {
                                details.setSoDocumentNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                break;
                            }
                        } else {
                            try {
                                arSalesOrderHome.findBySoDocumentNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, AD_CMPNY);
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                            }
                            catch (FinderException ex) {
                                details.setSoDocumentNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                break;

                            }

                        }

                    }

                }

            } else {

                if (arExistingSalesOrder != null &&
                        !arExistingSalesOrder.getSoCode().equals(details.getSoCode())) {

                    throw new GlobalDocumentNumberNotUniqueException();

                }

                if (arSalesOrder.getSoDocumentNumber() != details.getSoDocumentNumber() &&
                        (details.getSoDocumentNumber() == null || details.getSoDocumentNumber().trim().length() == 0)) {

                    details.setSoDocumentNumber(arSalesOrder.getSoDocumentNumber());

                }

            }

            // validate if conversion date exists

            try {

                if (details.getSoConversionDate() != null) {

                    LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);
                    LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

                    if (!glValidateFunctionalCurrency.getFcName().equals("USD")) {
                        LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate =
                                glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(),
                                        details.getSoConversionDate(), AD_CMPNY);

                    } else if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                        LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate =
                                glFunctionalCurrencyRateHome.findByFcCodeAndDate(
                                        adCompany.getGlFunctionalCurrency().getFcCode(), details.getSoConversionDate(), AD_CMPNY);

                    }

                }

            }
            catch (FinderException ex) {

                throw new GlobalConversionDateNotExistException();

            }

            // validate if payment term has at least one payment schedule

            if (adPaymentTermHome.findByPytName(PYT_NM, AD_CMPNY).getAdPaymentSchedules().isEmpty()) {

                throw new GlobalPaymentTermInvalidException();

            }

            boolean isRecalculate = true;

            // create sales order
            if (details.getSoCode() == null) {

                arSalesOrder = arSalesOrderHome.create(details.getSoDate(), details.getSoDocumentNumber(),
                        details.getSoReferenceNumber(), details.getSoTransactionType(), details.getSoDescription(),
                        0, 0, 0, details.getSoShippingLine(), details.getSoPort(), details.getSoBillTo(),
                        details.getSoShipTo(), details.getSoConversionDate(), details.getSoConversionRate(),
                        EJBCommon.FALSE, EJBCommon.FALSE, null, EJBCommon.FALSE, null, details.getSoCreatedBy(),
                        details.getSoDateCreated(), details.getSoLastModifiedBy(), details.getSoDateLastModified(),
                        null, null, null, null, EJBCommon.FALSE, EJBCommon.FALSE, details.getSoMemo(), details.getSoTransactionStatus(), AD_BRNCH, AD_CMPNY);

            } else {

                // check if critical fields are changed

                if (!arSalesOrder.getArTaxCode().getTcName().equals(TC_NM) ||
                        !arSalesOrder.getArCustomer().getCstCustomerCode().equals(CST_CSTMR_CODE) ||
                        !arSalesOrder.getAdPaymentTerm().getPytName().equals(PYT_NM) ||
                        solList.size() != arSalesOrder.getArSalesOrderLines().size()) {

                    isRecalculate = true;

                } else if (solList.size() == arSalesOrder.getArSalesOrderLines().size()) {

                    Iterator ilIter = arSalesOrder.getArSalesOrderLines().iterator();
                    Iterator ilListIter = solList.iterator();

                    while (ilIter.hasNext()) {

                        LocalArSalesOrderLine arSalesOrderLine = (LocalArSalesOrderLine) ilIter.next();
                        ArModSalesOrderLineDetails mdetails = (ArModSalesOrderLineDetails) ilListIter.next();

                        if (!arSalesOrderLine.getInvItemLocation().getInvItem().getIiName().equals(mdetails.getSolIiName()) ||
                                !arSalesOrderLine.getInvItemLocation().getInvItem().getIiDescription().equals(mdetails.getSolIiDescription()) ||
                                !arSalesOrderLine.getInvItemLocation().getInvLocation().getLocName().equals(mdetails.getSolLocName()) ||
                                !arSalesOrderLine.getInvUnitOfMeasure().getUomName().equals(mdetails.getSolUomName()) ||
                                arSalesOrderLine.getSolQuantity() != mdetails.getSolQuantity() ||
                                arSalesOrderLine.getSolUnitPrice() != mdetails.getSolUnitPrice()) {

                            isRecalculate = true;
                            break;

                        }

                        isRecalculate = false;

                    }

                } else {

                    isRecalculate = false;

                }

                arSalesOrder.setSoDate(details.getSoDate());
                arSalesOrder.setSoDocumentNumber(details.getSoDocumentNumber());
                arSalesOrder.setSoReferenceNumber(details.getSoReferenceNumber());
                arSalesOrder.setSoTransactionType(details.getSoTransactionType());
                arSalesOrder.setSoDescription(details.getSoDescription());
                arSalesOrder.setSoBillTo(details.getSoBillTo());
                arSalesOrder.setSoShipTo(details.getSoShipTo());
                arSalesOrder.setSoVoid(details.getSoVoid());
                arSalesOrder.setSoMobile(details.getSoMobile());
                arSalesOrder.setSoConversionDate(details.getSoConversionDate());
                arSalesOrder.setSoConversionRate(details.getSoConversionRate());
                arSalesOrder.setSoLastModifiedBy(details.getSoLastModifiedBy());
                arSalesOrder.setSoDateLastModified(details.getSoDateLastModified());
                arSalesOrder.setSoReasonForRejection(null);
                arSalesOrder.setSoMemo(details.getSoMemo());

            }

            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(CST_CSTMR_CODE, AD_CMPNY);
            arCustomer.addArSalesOrder(arSalesOrder);

            LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName(PYT_NM, AD_CMPNY);
            adPaymentTerm.addArSalesOrder(arSalesOrder);

            LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName(TC_NM, AD_CMPNY);
            arTaxCode.addArSalesOrder(arSalesOrder);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);
            glFunctionalCurrency.addArSalesOrder(arSalesOrder);

            LocalArSalesperson arSalesperson = details.getSoSlpSalespersonCode() == null ? null :
                    arSalespersonHome.findBySlpSalespersonCode(details.getSoSlpSalespersonCode(), AD_CMPNY);

            if (arSalesperson != null) {
                arSalesperson.addArSalesOrder(arSalesOrder);
            }

            double ABS_TOTAL_AMOUNT = 0d;

            if (isRecalculate) {

                // remove all sales order line items
                Collection arSalesOrderLines = arSalesOrder.getArSalesOrderLines();
                Iterator i = arSalesOrderLines.iterator();
                while (i.hasNext()) {
                    LocalArSalesOrderLine arSalesOrderLine = (LocalArSalesOrderLine) i.next();
                    i.remove();
                    //arSalesOrderLine.remove();
                }

                // add new purchase order line item
                i = solList.iterator();
                LocalInvItemLocation invItemLocation = null;
                while (i.hasNext()) {
                    ArModSalesOrderLineDetails mSolDetails = (ArModSalesOrderLineDetails) i.next();
                    LocalArSalesOrderLine arSalesOrderLine = arSalesOrderLineHome.create(
                            mSolDetails.getSolLine(), mSolDetails.getSolIiDescription(), mSolDetails.getSolQuantity(),
                            mSolDetails.getSolUnitPrice(), mSolDetails.getSolAmount(), mSolDetails.getSolGrossAmount(),
                            mSolDetails.getSolTaxAmount(), mSolDetails.getSolDiscount1(),
                            mSolDetails.getSolDiscount2(), mSolDetails.getSolDiscount3(), mSolDetails.getSolDiscount4(),
                            mSolDetails.getSolTotalDiscount(), 0d, mSolDetails.getSolMisc(), mSolDetails.getSolTax(), AD_CMPNY);

                    arSalesOrder.addArSalesOrderLine(arSalesOrderLine);

                    try {

                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(
                                mSolDetails.getSolLocName(), mSolDetails.getSolIiName(), AD_CMPNY);

                    }
                    catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(mSolDetails.getSolLine()));

                    }

                    ABS_TOTAL_AMOUNT += arSalesOrderLine.getSolAmount();

                    invItemLocation.addArSalesOrderLine(arSalesOrderLine);

                    LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(
                            mSolDetails.getSolUomName(), AD_CMPNY);

                    invUnitOfMeasure.addArSalesOrderLine(arSalesOrderLine);

                }

            }

            // set sales order approval status

            String SO_APPRVL_STATUS = null;

            if (!isDraft) {

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(AD_CMPNY);

                // check if ar sales order approval is enabled

                if (adApproval.getAprEnableArSalesOrder() == EJBCommon.FALSE) {

                    SO_APPRVL_STATUS = "N/A";

                } else {

                    // check if sales order is self approved

                    LocalAdAmountLimit adAmountLimit = null;

                    try {

                        adAmountLimit = adAmountLimitHome.findByAdcTypeAndAuTypeAndUsrName("AR SALES ORDER", "REQUESTER", details.getSoLastModifiedBy(), AD_CMPNY);

                    }
                    catch (FinderException ex) {

                        throw new GlobalNoApprovalRequesterFoundException();

                    }

                    if (ABS_TOTAL_AMOUNT <= adAmountLimit.getCalAmountLimit()) {

                        SO_APPRVL_STATUS = "N/A";

                    } else {

                        // for approval, create approval queue

                        Collection adAmountLimits = adAmountLimitHome.findByAdcTypeAndGreaterThanCalAmountLimit("AR SALES ORDER", adAmountLimit.getCalAmountLimit(), AD_CMPNY);

                        if (adAmountLimits.isEmpty()) {

                            Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adAmountLimit.getCalCode(), AD_CMPNY);

                            if (adApprovalUsers.isEmpty()) {

                                throw new GlobalNoApprovalApproverFoundException();

                            }

                            Iterator j = adApprovalUsers.iterator();

                            while (j.hasNext()) {

                                LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) j.next();

                                LocalAdApprovalQueue adApprovalQueue = adApprovalQueueHome.create(EJBCommon.TRUE, "AR SALES ORDER", arSalesOrder.getSoCode(),
                                        arSalesOrder.getSoDocumentNumber(), arSalesOrder.getSoDate(), adAmountLimit.getCalAndOr(), adApprovalUser.getAuOr(), AD_BRNCH, AD_CMPNY);

                                adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);

                            }

                        } else {

                            boolean isApprovalUsersFound = false;

                            Iterator i = adAmountLimits.iterator();

                            while (i.hasNext()) {

                                LocalAdAmountLimit adNextAmountLimit = (LocalAdAmountLimit) i.next();

                                if (ABS_TOTAL_AMOUNT <= adNextAmountLimit.getCalAmountLimit()) {

                                    Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adAmountLimit.getCalCode(), AD_CMPNY);

                                    Iterator j = adApprovalUsers.iterator();

                                    while (j.hasNext()) {

                                        isApprovalUsersFound = true;

                                        LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) j.next();

                                        LocalAdApprovalQueue adApprovalQueue = adApprovalQueueHome.create(EJBCommon.TRUE, "AR SALES ORDER", arSalesOrder.getSoCode(),
                                                arSalesOrder.getSoDocumentNumber(), arSalesOrder.getSoDate(), adAmountLimit.getCalAndOr(), adApprovalUser.getAuOr(), AD_BRNCH, AD_CMPNY);

                                        adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);

                                    }

                                    break;

                                } else if (!i.hasNext()) {

                                    Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adNextAmountLimit.getCalCode(), AD_CMPNY);

                                    Iterator j = adApprovalUsers.iterator();

                                    while (j.hasNext()) {

                                        isApprovalUsersFound = true;

                                        LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) j.next();

                                        LocalAdApprovalQueue adApprovalQueue = adApprovalQueueHome.create(EJBCommon.TRUE, "AR SALES ORDER", arSalesOrder.getSoCode(),
                                                arSalesOrder.getSoDocumentNumber(), arSalesOrder.getSoDate(), adNextAmountLimit.getCalAndOr(), adApprovalUser.getAuOr(), AD_BRNCH, AD_CMPNY);

                                        adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);

                                    }

                                    break;

                                }

                                adAmountLimit = adNextAmountLimit;

                            }

                            if (!isApprovalUsersFound) {

                                throw new GlobalNoApprovalApproverFoundException();

                            }

                        }

                        SO_APPRVL_STATUS = "PENDING";

                    }

                }

                arSalesOrder.setSoApprovalStatus(SO_APPRVL_STATUS);

                // set post purchase order

                if (SO_APPRVL_STATUS.equals("N/A")) {

                    arSalesOrder.setSoPosted(EJBCommon.TRUE);
                    arSalesOrder.setSoPosted(EJBCommon.TRUE);
                    arSalesOrder.setSoPostedBy(arSalesOrder.getSoLastModifiedBy());
                    arSalesOrder.setSoDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

                }

            }

            return 1;

        }
        catch (GlobalRecordAlreadyDeletedException ex) {
            throw ex;

        }
        catch (GlobalDocumentNumberNotUniqueException ex) {

            throw ex;

        }
        catch (GlobalConversionDateNotExistException ex) {

            throw ex;

        }
        catch (GlobalPaymentTermInvalidException ex) {

            throw ex;

        }
        catch (GlobalTransactionAlreadyPendingException ex) {

            throw ex;

        }
        catch (GlobalTransactionAlreadyVoidException ex) {

            throw ex;

        }
        catch (GlobalInvItemLocationNotFoundException ex) {

            throw ex;

        }
        catch (GlobalNoApprovalApproverFoundException ex) {

            throw ex;

        }
        catch (GlobalNoApprovalRequesterFoundException ex) {

            throw ex;

        }
        catch (GlobalRecordAlreadyAssignedException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }

    private short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ArInvoiceEntryControllerBean getGlFcPrecisionUnit");

        try {
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            return adCompany.getGlFunctionalCurrency().getFcPrecision();
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

}