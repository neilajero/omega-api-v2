/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArPdcInvoiceGenerationControllerBean
 * @created July 1, 2005, 5:00 PM
 * @author Jolly T. Martin
 */
package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ap.LocalApDistributionRecordHome;
import com.ejb.dao.ar.*;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ar.*;
import com.ejb.entities.gen.LocalGenField;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.inv.*;
import com.ejb.exception.ad.AdPRFCoaGlCustomerDepositAccountNotFoundException;
import com.ejb.exception.global.GlobalBranchAccountNumberInvalidException;
import com.ejb.exception.global.GlobalDocumentNumberNotUniqueException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.mod.ar.ArModPdcDetails;

import jakarta.ejb.*;
import java.util.*;

@Stateless(name = "ArPdcInvoiceGenerationControllerEJB")
public class ArPdcInvoiceGenerationControllerBean extends EJBContextClass implements ArPdcInvoiceGenerationController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalArPdcHome arPdcHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    private LocalArInvoicePaymentScheduleHome arInvoicePaymentScheduleHome;
    @EJB
    private LocalArInvoiceBatchHome arInvoiceBatchHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalArReceiptBatchHome arReceiptBatchHome;
    @EJB
    private LocalArAppliedInvoiceHome arAppliedInvoiceHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalAdPaymentScheduleHome adPaymentScheduleHome;
    @EJB
    private LocalAdDiscountHome adDiscountHome;
    @EJB
    private LocalArTaxCodeHome arTaxCodeHome;
    @EJB
    private LocalArWithholdingTaxCodeHome arWithholdingTaxCodeHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalAdAmountLimitHome adAmountLimitHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private LocalAdBranchCustomerHome adBranchCustomerHome;
    @EJB
    private LocalArInvoiceLineHome arInvoiceLineHome;
    @EJB
    private LocalArStandardMemoLineHome arStandardMemoLineHome;
    @EJB
    private LocalArInvoiceLineItemHome arInvoiceLineItemHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private LocalArAutoAccountingSegmentHome arAutoAccountingSegmentHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;

    public ArrayList getArPdcByCriteria(HashMap criteria, String ORDER_BY, Integer OFFSET, Integer LIMIT, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArPdcInvoiceGenerationControllerBean getArPdcByCriteria");
        ArrayList pdcList = new ArrayList();
        StringBuilder jbossQl = new StringBuilder();
        jbossQl.append("SELECT OBJECT(pdc) FROM ArPdc pdc ");

        boolean firstArgument = true;
        short ctr = 0;
        int criteriaSize = criteria.size();
        Object[] obj;

        // Allocate the size of the object parameter

        if (criteria.containsKey("customerCode")) {

            criteriaSize--;
        }

        obj = new Object[criteriaSize];

        if (criteria.containsKey("customerCode")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("pdc.arCustomer.cstCustomerCode LIKE '%").append(criteria.get("customerCode")).append("%' ");
        }

        if (criteria.containsKey("maturityDateFrom")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("pdc.pdcMaturityDate>=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("maturityDateFrom");
            ctr++;
        }

        if (criteria.containsKey("maturityDateTo")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("pdc.pdcMaturityDate<=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("maturityDateTo");
            ctr++;
        }

        if (!firstArgument) {
            jbossQl.append("AND ");
        } else {
            firstArgument = false;
            jbossQl.append("WHERE ");
        }

        jbossQl.append("pdc.pdcStatus='MATURED' ");

        if (!firstArgument) {
            jbossQl.append("AND ");
        } else {
            firstArgument = false;
            jbossQl.append("WHERE ");
        }

        jbossQl.append("pdc.pdcPosted=" + EJBCommon.TRUE + " ");

        if (!firstArgument) {
            jbossQl.append("AND ");
        } else {
            firstArgument = false;
            jbossQl.append("WHERE ");
        }

        jbossQl.append("pdc.pdcAdBranch=").append(AD_BRNCH).append(" AND pdc.pdcAdCompany=").append(AD_CMPNY).append(" ");

        Collection arPdcs = null;

        try {

            arPdcs = arPdcHome.getPdcByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (arPdcs.isEmpty()) {
            throw new GlobalNoRecordFoundException();
        }

        for (Object pdc : arPdcs) {

            LocalArPdc arPdc = (LocalArPdc) pdc;

            ArModPdcDetails mdetails = new ArModPdcDetails();
            mdetails.setPdcCode(arPdc.getPdcCode());
            mdetails.setPdcCstCustomerCode(arPdc.getArCustomer().getCstCustomerCode());
            mdetails.setPdcReferenceNumber(arPdc.getPdcReferenceNumber());
            mdetails.setPdcCheckNumber(arPdc.getPdcCheckNumber());
            mdetails.setPdcDateReceived(arPdc.getPdcDateReceived());
            mdetails.setPdcMaturityDate(arPdc.getPdcMaturityDate());
            mdetails.setPdcAmount(arPdc.getPdcAmount());

            if (!arPdc.getArInvoiceLines().isEmpty()) {

                ArrayList list = new ArrayList();
                Collection arInvLines = arPdc.getArInvoiceLines();

                for (Object arInvLine : arInvLines) {

                    LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) arInvLine;
                    list.add(arInvoiceLine);
                }

                mdetails.setPdcIlList(list);

            } else {

                ArrayList list = new ArrayList();
                Collection arInvLineItems = arPdc.getArInvoiceLineItems();

                for (Object arInvLineItem : arInvLineItems) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) arInvLineItem;
                    list.add(arInvoiceLineItem);
                }

                mdetails.setPdcIliList(list);
            }

            pdcList.add(mdetails);
        }

        return pdcList;
    }

    public void executeArPdcInvoiceGeneration(Integer PDC_CODE, String INV_NMBR, String USR_NM, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalBranchAccountNumberInvalidException {

        Debug.print("ArPdcInvoiceControllerBean executeArPdcInvoiceGeneration");
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSSS");
        try {

            // get post dated check
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            LocalArPdc arPdc = null;

            try {

                arPdc = arPdcHome.findByPrimaryKey(PDC_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // if statement (invoice (ITEMS/MEMO LINE)
            if (arPdc.getArTaxCode() != null && arPdc.getArWithholdingTaxCode() != null && arPdc.getAdPaymentTerm() != null) {

                // validate if invoice number is unique

                try {

                    LocalArInvoice arExistingInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(INV_NMBR, EJBCommon.FALSE, AD_BRNCH, AD_CMPNY);

                    throw new GlobalDocumentNumberNotUniqueException();

                } catch (FinderException ex) {

                }

                // generate document number if necessary

                LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AR INVOICE", AD_CMPNY);

                if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (INV_NMBR == null || INV_NMBR.trim().length() == 0)) {

                    while (true) {

                        try {

                            arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), EJBCommon.FALSE, AD_BRNCH, AD_CMPNY);
                            adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                        } catch (FinderException ex) {

                            INV_NMBR = adDocumentSequenceAssignment.getDsaNextSequence();
                            adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                            break;
                        }
                    }
                }

                // generate approval status

                LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

                String INV_APPRVL_STATUS = null;

                // generate invoice

                LocalArInvoice arInvoice = arInvoiceHome.create("ITEMS", EJBCommon.FALSE, arPdc.getPdcDescription(), arPdc.getPdcMaturityDate(), INV_NMBR, arPdc.getPdcReferenceNumber(), null, null, null, arPdc.getPdcAmount(), 0d, 0d, 0d, 0d, 0d, arPdc.getPdcConversionDate(), arPdc.getPdcConversionRate(), null, 0d, 0d, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, arPdc.getPdcLvFreight(), INV_APPRVL_STATUS, null, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, null, 0d, null, null, null, null, USR_NM, new Date(), USR_NM, new Date(), null, null, null, null, EJBCommon.FALSE, arPdc.getPdcLvShift(), null, null, EJBCommon.FALSE, EJBCommon.FALSE, null, arPdc.getPdcEffectivityDate(), AD_BRNCH, AD_CMPNY);

                // payment term
                arPdc.getAdPaymentTerm().addArInvoice(arInvoice);
                // functional currency
                adCompany.getGlFunctionalCurrency().addArInvoice(arInvoice);
                // tax code
                arPdc.getArTaxCode().addArInvoice(arInvoice);
                // withholding tax
                arPdc.getArWithholdingTaxCode().addArInvoice(arInvoice);
                // customer
                arPdc.getArCustomer().addArInvoice(arInvoice);

                // generate invoice batch if necessary

                LocalArInvoiceBatch arInvoiceBatch = null;

                if (adPreference.getPrfEnableArInvoiceBatch() == EJBCommon.TRUE) {

                    arInvoiceBatch = arInvoiceBatchHome.create("Post Dated Checks " + formatter.format(new java.util.Date()), "POST DATED CHECKS", "OPEN", "INVOICE", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, AD_BRNCH, AD_CMPNY);
                }

                if (arInvoiceBatch != null) {

                    arInvoiceBatch.addArInvoice(arInvoice);
                }

                // add invoice lines & distribution records

                if (!arPdc.getArInvoiceLines().isEmpty()) {

                    this.addLinesDrEntry(arPdc.getArInvoiceLines(), false, arInvoice, arPdc, adPreference, AD_BRNCH, AD_CMPNY);

                } else {

                    this.addLinesDrEntry(arPdc.getArInvoiceLineItems(), true, arInvoice, arPdc, adPreference, AD_BRNCH, AD_CMPNY);
                }

                // create invoice payment schedule

                short precisionUnit = this.getGlFcPrecisionUnit(AD_CMPNY);
                double TOTAL_PAYMENT_SCHEDULE = 0d;

                LocalAdPaymentTerm adPaymentTerm = arPdc.getAdPaymentTerm();

                GregorianCalendar gcPrevDateDue = new GregorianCalendar();
                GregorianCalendar gcDateDue = new GregorianCalendar();
                gcPrevDateDue.setTime(arInvoice.getInvEffectivityDate());

                Collection adPaymentSchedules = adPaymentTerm.getAdPaymentSchedules();

                Iterator i = adPaymentSchedules.iterator();

                while (i.hasNext()) {

                    LocalAdPaymentSchedule adPaymentSchedule = (LocalAdPaymentSchedule) i.next();

                    // get date due

                    switch (arInvoice.getAdPaymentTerm().getPytScheduleBasis()) {
                        case "DEFAULT":

                            gcDateDue.setTime(arInvoice.getInvEffectivityDate());
                            gcDateDue.add(Calendar.DATE, adPaymentSchedule.getPsDueDay());

                            break;
                        case "MONTHLY":

                            gcDateDue = gcPrevDateDue;
                            gcDateDue.add(Calendar.MONTH, 1);
                            gcPrevDateDue = gcDateDue;

                            break;
                        case "BI-MONTHLY":

                            gcDateDue = gcPrevDateDue;

                            if (gcPrevDateDue.get(Calendar.MONTH) != 1) {
                                if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 31 && gcPrevDateDue.get(Calendar.DATE) > 15 && gcPrevDateDue.get(Calendar.DATE) < 31) {
                                    gcDateDue.add(Calendar.DATE, 16);
                                } else {
                                    gcDateDue.add(Calendar.DATE, 15);
                                }
                            } else if (gcPrevDateDue.get(Calendar.MONTH) == 1) {
                                if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 28 && gcPrevDateDue.get(Calendar.DATE) == 14) {
                                    gcDateDue.add(Calendar.DATE, 14);
                                } else if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 28 && gcPrevDateDue.get(Calendar.DATE) >= 15 && gcPrevDateDue.get(Calendar.DATE) < 28) {
                                    gcDateDue.add(Calendar.DATE, 13);
                                } else if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 29 && gcPrevDateDue.get(Calendar.DATE) >= 15 && gcPrevDateDue.get(Calendar.DATE) < 29) {
                                    gcDateDue.add(Calendar.DATE, 14);
                                } else {
                                    gcDateDue.add(Calendar.DATE, 15);
                                }
                            }

                            gcPrevDateDue = gcDateDue;
                            break;
                    }

                    // create a payment schedule

                    double PAYMENT_SCHEDULE_AMOUNT = 0;

                    // if last payment schedule subtract to avoid rounding difference error

                    if (i.hasNext()) {

                        PAYMENT_SCHEDULE_AMOUNT = EJBCommon.roundIt((adPaymentSchedule.getPsRelativeAmount() / adPaymentTerm.getPytBaseAmount()) * arInvoice.getInvAmountDue(), precisionUnit);

                    } else {

                        PAYMENT_SCHEDULE_AMOUNT = arInvoice.getInvAmountDue() - TOTAL_PAYMENT_SCHEDULE;
                    }

                    LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = arInvoicePaymentScheduleHome.create(gcDateDue.getTime(), adPaymentSchedule.getPsLineNumber(), PAYMENT_SCHEDULE_AMOUNT, 0d, EJBCommon.FALSE, (short) 0, gcDateDue.getTime(), 0d, 0d, AD_CMPNY);

                    arInvoice.addArInvoicePaymentSchedule(arInvoicePaymentSchedule);

                    TOTAL_PAYMENT_SCHEDULE += PAYMENT_SCHEDULE_AMOUNT;
                }

            } else if (arPdc.getAdBankAccount() != null) {

                // check if receipt is existing
                // validate if receipt number is unique
                String RCT_NMBR = INV_NMBR;

                try {

                    LocalArReceipt arExistingReceipt = arReceiptHome.findByRctNumberAndBrCode(RCT_NMBR, AD_BRNCH, AD_CMPNY);

                    throw new GlobalDocumentNumberNotUniqueException();

                } catch (FinderException ex) {

                }

                // generate AR RECEIPT number
                // generate receipt number if necessary

                LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AR RECEIPT", AD_CMPNY);

                if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (RCT_NMBR == null || RCT_NMBR.trim().length() == 0)) {

                    while (true) {

                        try {

                            arReceiptHome.findByRctNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, AD_CMPNY);
                            adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                        } catch (FinderException ex) {

                            RCT_NMBR = adDocumentSequenceAssignment.getDsaNextSequence();
                            adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                            break;
                        }
                    }
                }

                // generate approval status

                LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

                // generate receipt

                LocalArReceipt arReceipt = arReceiptHome.create("COLLECTION", arPdc.getPdcDescription(), arPdc.getPdcMaturityDate(), RCT_NMBR, arPdc.getPdcReferenceNumber(), arPdc.getPdcCheckNumber(), null, null, null, null, null, null, arPdc.getPdcAmount(), arPdc.getPdcAmount(), 0d, 0d, 0d, 0d, 0d, arPdc.getPdcConversionDate(), arPdc.getPdcConversionRate(), null, null, EJBCommon.FALSE, 0d, null, null, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, null, null, null, null, null, arPdc.getPdcCreatedBy(), arPdc.getPdcDateCreated(), arPdc.getPdcLastModifiedBy(), arPdc.getPdcDateLastModified(), null, null, null, null, EJBCommon.FALSE, arPdc.getPdcLvShift(), EJBCommon.FALSE, EJBCommon.FALSE, arPdc.getArCustomer().getCstName(), null, EJBCommon.FALSE, EJBCommon.FALSE, null, AD_BRNCH, AD_CMPNY);

                // functional currency
                adCompany.getGlFunctionalCurrency().addArReceipt(arReceipt);
                // customer
                arPdc.getArCustomer().addArReceipt(arReceipt);
                // bank account
                arPdc.getAdBankAccount().addArReceipt(arReceipt);

                // generate receipt batch if necessary

                LocalArReceiptBatch arReceiptBatch = null;

                if (adPreference.getPrfEnableArReceiptBatch() == EJBCommon.TRUE) {

                    arReceiptBatch = arReceiptBatchHome.create("PDC " + formatter.format(new java.util.Date()), "POST DATED CHECKS", "OPEN", "INVOICE", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, AD_BRNCH, AD_CMPNY);
                }

                if (arReceiptBatch != null) {

                    arReceiptBatch.addArReceipt(arReceipt);
                }

                Collection arAppliedInvoices = arPdc.getArAppliedInvoices();

                Iterator i = arAppliedInvoices.iterator();
                // create applied invoice and distribution record (refer from ar receipt)

                while (i.hasNext()) {
                    LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) i.next();
                    LocalArAppliedInvoice arNewAppliedInvoice = arAppliedInvoiceHome.create(arAppliedInvoice.getAiApplyAmount(), arAppliedInvoice.getAiPenaltyApplyAmount(), arAppliedInvoice.getAiCreditableWTax(), arAppliedInvoice.getAiDiscountAmount(), arAppliedInvoice.getAiRebate(), arAppliedInvoice.getAiAppliedDeposit(), arAppliedInvoice.getAiAllocatedPaymentAmount(), arAppliedInvoice.getAiForexGainLoss(), EJBCommon.FALSE, AD_CMPNY);

                    arReceipt.addArAppliedInvoice(arNewAppliedInvoice);
                    arAppliedInvoice.getArInvoicePaymentSchedule().addArAppliedInvoice(arNewAppliedInvoice);
                }

                // create cash distribution record

                if (arReceipt.getRctAmount() != 0) {
                    this.addArDrEntry(arReceipt.getArDrNextLine(), "CASH", EJBCommon.TRUE, arReceipt.getRctAmount(), EJBCommon.FALSE, arReceipt.getAdBankAccount().getBaCoaGlCashAccount(), arReceipt, null, AD_BRNCH, AD_CMPNY);
                }

                // add new applied vouchers and distribution record
                arAppliedInvoices = arReceipt.getArAppliedInvoices();
                i = arAppliedInvoices.iterator();
                while (i.hasNext()) {

                    LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) i.next();

                    // create cred. withholding tax distribution record if necessary

                    if (arAppliedInvoice.getAiCreditableWTax() > 0) {

                        Integer WTC_COA_CODE = null;

                        if (arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getArWithholdingTaxCode().getGlChartOfAccount() != null) {

                            WTC_COA_CODE = arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getArWithholdingTaxCode().getGlChartOfAccount().getCoaCode();

                        } else {

                            WTC_COA_CODE = adPreference.getArWithholdingTaxCode().getGlChartOfAccount().getCoaCode();
                        }

                        this.addArDrEntry(arReceipt.getArDrNextLine(), "W-TAX", EJBCommon.TRUE, arAppliedInvoice.getAiCreditableWTax(), EJBCommon.FALSE, WTC_COA_CODE, arReceipt, arAppliedInvoice, AD_BRNCH, AD_CMPNY);
                    }

                    // create discount distribution records if necessary

                    if (arAppliedInvoice.getAiDiscountAmount() != 0) {

                        short precisionUnit = this.getGlFcPrecisionUnit(AD_CMPNY);

                        // get discount percent

                        double DISCOUNT_PERCENT = EJBCommon.roundIt(arAppliedInvoice.getAiDiscountAmount() / (arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiCreditableWTax() + arAppliedInvoice.getAiDiscountAmount() + arAppliedInvoice.getAiAppliedDeposit()), (short) 6);
                        DISCOUNT_PERCENT = EJBCommon.roundIt(DISCOUNT_PERCENT * ((arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiCreditableWTax() + arAppliedInvoice.getAiDiscountAmount() + arAppliedInvoice.getAiAppliedDeposit()) / arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvAmountDue()), (short) 6);

                        Collection arDiscountDistributionRecords = arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getArDistributionRecords();

                        // get total debit and credit for rounding difference calculation

                        double TOTAL_DEBIT = 0d;
                        double TOTAL_CREDIT = 0d;
                        boolean isRoundingDifferenceCalculated = false;

                        Iterator j = arDiscountDistributionRecords.iterator();

                        while (j.hasNext()) {

                            LocalArDistributionRecord arDiscountDistributionRecord = (LocalArDistributionRecord) j.next();

                            if (arDiscountDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                                TOTAL_DEBIT += EJBCommon.roundIt(arDiscountDistributionRecord.getDrAmount() * DISCOUNT_PERCENT, precisionUnit);

                            } else {

                                TOTAL_CREDIT += EJBCommon.roundIt(arDiscountDistributionRecord.getDrAmount() * DISCOUNT_PERCENT, precisionUnit);
                            }
                        }

                        j = arDiscountDistributionRecords.iterator();

                        while (j.hasNext()) {

                            LocalArDistributionRecord arDiscountDistributionRecord = (LocalArDistributionRecord) j.next();

                            if (arDiscountDistributionRecord.getDrClass().equals("RECEIVABLE")) {
                                continue;
                            }

                            double DR_AMNT = EJBCommon.roundIt(arDiscountDistributionRecord.getDrAmount() * DISCOUNT_PERCENT, precisionUnit);

                            // calculate rounding difference if necessary

                            if (arDiscountDistributionRecord.getDrDebit() == EJBCommon.FALSE && TOTAL_DEBIT != TOTAL_CREDIT && !isRoundingDifferenceCalculated) {

                                DR_AMNT = DR_AMNT + TOTAL_DEBIT - TOTAL_CREDIT;

                                isRoundingDifferenceCalculated = true;
                            }

                            if (arDiscountDistributionRecord.getDrClass().equals("REVENUE")) {

                                this.addArDrEntry(arReceipt.getArDrNextLine(), "SALES DISCOUNT", EJBCommon.TRUE, DR_AMNT, EJBCommon.FALSE, arReceipt.getAdBankAccount().getBaCoaGlSalesDiscount(), arReceipt, arAppliedInvoice, AD_BRNCH, AD_CMPNY);

                            } else {

                                this.addArDrEntry(arReceipt.getArDrNextLine(), arDiscountDistributionRecord.getDrClass(), arDiscountDistributionRecord.getDrDebit() == EJBCommon.TRUE ? EJBCommon.FALSE : EJBCommon.TRUE, DR_AMNT, EJBCommon.FALSE, arDiscountDistributionRecord.getGlChartOfAccount().getCoaCode(), arReceipt, arAppliedInvoice, AD_BRNCH, AD_CMPNY);
                            }
                        }
                    }

                    // create applied deposit distribution records if necessary

                    if (arAppliedInvoice.getAiAppliedDeposit() != 0) {

                        if (adPreference.getPrfArGlCoaCustomerDepositAccount() == null) {
                            throw new AdPRFCoaGlCustomerDepositAccountNotFoundException();
                        }

                        this.addArDrEntry(arReceipt.getArDrNextLine(), "APPLIED DEPOSIT", EJBCommon.TRUE, arAppliedInvoice.getAiAppliedDeposit(), EJBCommon.FALSE, adPreference.getPrfArGlCoaCustomerDepositAccount(), arReceipt, arAppliedInvoice, AD_BRNCH, AD_CMPNY);
                    }

                    // get receivable account

                    LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.findByDrClassAndInvCode("RECEIVABLE", arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvCode(), AD_CMPNY);

                    this.addArDrEntry(arReceipt.getArDrNextLine(), "RECEIVABLE", EJBCommon.FALSE, arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiCreditableWTax() + arAppliedInvoice.getAiDiscountAmount() + arAppliedInvoice.getAiAppliedDeposit(), EJBCommon.FALSE, arDistributionRecord.getGlChartOfAccount().getCoaCode(), arReceipt, arAppliedInvoice, AD_BRNCH, AD_CMPNY);

                    // reverse deferred tax if necessary

                    LocalArTaxCode arTaxCode = arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getArTaxCode();

                    if (!arTaxCode.getTcType().equals("NONE") && !arTaxCode.getTcType().equals("EXEMPT") && arTaxCode.getTcInterimAccount() != null) {

                        try {

                            LocalArDistributionRecord arDeferredDistributionRecord = arDistributionRecordHome.findByDrClassAndInvCode("DEFERRED TAX", arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvCode(), AD_CMPNY);

                            double DR_AMNT = EJBCommon.roundIt((arDeferredDistributionRecord.getDrAmount() / arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvAmountDue()) * (arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiCreditableWTax()), this.getGlFcPrecisionUnit(AD_CMPNY));

                            this.addArDrEntry(arReceipt.getArDrNextLine(), "DEFERRED TAX", EJBCommon.TRUE, DR_AMNT, EJBCommon.FALSE, arDeferredDistributionRecord.getGlChartOfAccount().getCoaCode(), arReceipt, arAppliedInvoice, AD_BRNCH, AD_CMPNY);

                            this.addArDrEntry(arReceipt.getArDrNextLine(), "TAX", EJBCommon.FALSE, DR_AMNT, EJBCommon.FALSE, arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getArTaxCode().getGlChartOfAccount().getCoaCode(), arReceipt, arAppliedInvoice, AD_BRNCH, AD_CMPNY);

                        } catch (FinderException ex) {

                        }
                    }
                }
            }

            arPdc.setPdcStatus("CLEARED");

        } catch (GlobalRecordAlreadyDeletedException | GlobalBranchAccountNumberInvalidException |
                 GlobalDocumentNumberNotUniqueException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ArPdcInvoiceGenerationControllerBean getGlFcPrecisionUnit");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getInvGpQuantityPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ArPdcInvoiceGenerationControllerBean getInvGpQuantityPrecisionUnit");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfInvQuantityPrecisionUnit();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private method
    private void addLinesDrEntry(Collection arPdcLines, boolean isLineItems, LocalArInvoice arInvoice, LocalArPdc arPdc, LocalAdPreference adPreference, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArPdcInvoiceGenerationControllerBean addIlDrEntry");
        try {

            double TOTAL_TAX = 0d;
            double TOTAL_LINE = 0d;

            // invoice line items

            if (isLineItems) {

                for (Object arPdcLine : arPdcLines) {

                    LocalArInvoiceLineItem arPdcInvoiceLineItem = (LocalArInvoiceLineItem) arPdcLine;

                    LocalInvItemLocation invItemLocation = arPdcInvoiceLineItem.getInvItemLocation();

                    // line

                    LocalArInvoiceLineItem arNewInvoiceLineItem = this.addArIliEntry(arPdcInvoiceLineItem, arInvoice, invItemLocation, AD_CMPNY);

                    // add cost of sales distribution and inventory

                    double COST = 0d;

                    try {

                        LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, AD_CMPNY);

                        COST = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                    } catch (FinderException ex) {

                        COST = arNewInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();
                    }

                    double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arNewInvoiceLineItem.getInvUnitOfMeasure(), arNewInvoiceLineItem.getInvItemLocation().getInvItem(), arNewInvoiceLineItem.getIliQuantity(), AD_CMPNY);

                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {

                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arNewInvoiceLineItem.getInvItemLocation().getIlCode(), AD_BRNCH, AD_CMPNY);

                    } catch (FinderException ex) {

                    }

                    if (arNewInvoiceLineItem.getInvItemLocation().getInvItem().getIiClass().equals("Stock")) {

                        if (adBranchItemLocation != null) {

                            // Use AdBranchItemLocation Coa
                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), "COGS", EJBCommon.TRUE, COST * QTY_SLD, adBranchItemLocation.getBilCoaGlCostOfSalesAccount(), arInvoice, AD_BRNCH, AD_CMPNY);

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD, adBranchItemLocation.getBilCoaGlInventoryAccount(), arInvoice, AD_BRNCH, AD_CMPNY);

                        } else {

                            // Use default Coa
                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), "COGS", EJBCommon.TRUE, COST * QTY_SLD, arNewInvoiceLineItem.getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arInvoice, AD_BRNCH, AD_CMPNY);

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD, arNewInvoiceLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(), arInvoice, AD_BRNCH, AD_CMPNY);
                        }
                        // add quantity to item location committed quantity

                        double convertedQuantity = this.convertByUomFromAndItemAndQuantity(arNewInvoiceLineItem.getInvUnitOfMeasure(), arNewInvoiceLineItem.getInvItemLocation().getInvItem(), arNewInvoiceLineItem.getIliQuantity(), AD_CMPNY);
                        invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() + convertedQuantity);
                    }

                    // add inventory sale distributions
                    if (adBranchItemLocation != null) {

                        // Use AdBranchItemLocation Coa
                        this.addArDrIliEntry(arInvoice.getArDrNextLine(), "REVENUE", EJBCommon.FALSE, arNewInvoiceLineItem.getIliAmount(), adBranchItemLocation.getBilCoaGlSalesAccount(), arInvoice, AD_BRNCH, AD_CMPNY);

                    } else {

                        // Use default
                        this.addArDrIliEntry(arInvoice.getArDrNextLine(), "REVENUE", EJBCommon.FALSE, arNewInvoiceLineItem.getIliAmount(), arNewInvoiceLineItem.getInvItemLocation().getIlGlCoaSalesAccount(), arInvoice, AD_BRNCH, AD_CMPNY);
                    }

                    TOTAL_LINE += arNewInvoiceLineItem.getIliAmount();
                    TOTAL_TAX += arNewInvoiceLineItem.getIliTaxAmount();

                }

                // add tax distribution if necessary

                if (!arPdc.getArTaxCode().getTcType().equals("NONE") && !arPdc.getArTaxCode().getTcType().equals("EXEMPT")) {

                    if (arPdc.getArTaxCode().getTcInterimAccount() == null) {

                        this.addArDrEntry(arInvoice.getArDrNextLine(), "TAX", EJBCommon.FALSE, TOTAL_TAX, arPdc.getArTaxCode().getGlChartOfAccount().getCoaCode(), arInvoice, AD_BRNCH, AD_CMPNY);

                    } else {

                        this.addArDrEntry(arInvoice.getArDrNextLine(), "DEFERRED TAX", EJBCommon.FALSE, TOTAL_TAX, arPdc.getArTaxCode().getTcInterimAccount(), arInvoice, AD_BRNCH, AD_CMPNY);
                    }
                }

                // add wtax distribution if necessary

                double W_TAX_AMOUNT = 0d;

                if (arPdc.getArWithholdingTaxCode().getWtcRate() != 0 && adPreference.getPrfArWTaxRealization().equals("INVOICE")) {

                    W_TAX_AMOUNT = EJBCommon.roundIt(TOTAL_LINE * (arPdc.getArWithholdingTaxCode().getWtcRate() / 100), this.getGlFcPrecisionUnit(AD_CMPNY));

                    this.addArDrEntry(arInvoice.getArDrNextLine(), "W-TAX", EJBCommon.TRUE, W_TAX_AMOUNT, arPdc.getArWithholdingTaxCode().getGlChartOfAccount().getCoaCode(), arInvoice, AD_BRNCH, AD_CMPNY);
                }

                // add payment discount if necessary

                double DISCOUNT_AMT = 0d;

                if (arPdc.getAdPaymentTerm().getPytDiscountOnInvoice() == EJBCommon.TRUE) {

                    Collection adPaymentSchedules = adPaymentScheduleHome.findByPytCode(arPdc.getAdPaymentTerm().getPytCode(), AD_CMPNY);
                    ArrayList adPaymentScheduleList = new ArrayList(adPaymentSchedules);
                    LocalAdPaymentSchedule adPaymentSchedule = (LocalAdPaymentSchedule) adPaymentScheduleList.get(0);

                    Collection adDiscounts = adDiscountHome.findByPsCode(adPaymentSchedule.getPsCode(), AD_CMPNY);
                    ArrayList adDiscountList = new ArrayList(adDiscounts);
                    LocalAdDiscount adDiscount = (LocalAdDiscount) adDiscountList.get(0);

                    double rate = adDiscount.getDscDiscountPercent();
                    DISCOUNT_AMT = (TOTAL_LINE + TOTAL_TAX) * (rate / 100d);

                    this.addArDrEntry(arInvoice.getArDrNextLine(), "DISCOUNT", EJBCommon.TRUE, DISCOUNT_AMT, arPdc.getAdPaymentTerm().getGlChartOfAccount().getCoaCode(), arInvoice, AD_BRNCH, AD_CMPNY);
                }

                // add receivable distribution

                LocalAdBranchCustomer adBranchCustomer = null;

                try {

                    adBranchCustomer = adBranchCustomerHome.findBcstByCstCodeAndBrCode(arInvoice.getArCustomer().getCstCode(), AD_BRNCH, AD_CMPNY);

                } catch (FinderException ex) {

                }

                if (adBranchCustomer != null) {

                    // Use AdBranchCustomer Account
                    this.addArDrIliEntry(arInvoice.getArDrNextLine(), "RECEIVABLE", EJBCommon.TRUE, TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT - DISCOUNT_AMT, adBranchCustomer.getBcstGlCoaReceivableAccount(), arInvoice, AD_BRNCH, AD_CMPNY);

                } else {

                    // Use default Account
                    this.addArDrIliEntry(arInvoice.getArDrNextLine(), "RECEIVABLE", EJBCommon.TRUE, TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT - DISCOUNT_AMT, arInvoice.getArCustomer().getCstGlCoaReceivableAccount(), arInvoice, AD_BRNCH, AD_CMPNY);
                }

                // set invoice amount due

                arInvoice.setInvAmountDue(TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT - DISCOUNT_AMT);

                // invoice lines

            } else {

                for (Object arPdcLine : arPdcLines) {

                    LocalArInvoiceLine arPdcInvoiceLine = (LocalArInvoiceLine) arPdcLine;

                    // line

                    LocalArInvoiceLine arNewInvoiceLine = this.addArIlEntry(arPdcInvoiceLine, arInvoice, AD_CMPNY);

                    // add revenue/credit distributions

                    this.addArDrEntry(arInvoice.getArDrNextLine(), "REVENUE", EJBCommon.FALSE, arNewInvoiceLine.getIlAmount(), this.getArGlCoaRevenueAccount(arNewInvoiceLine, AD_BRNCH, AD_CMPNY), arInvoice, AD_BRNCH, AD_CMPNY);

                    TOTAL_LINE += arNewInvoiceLine.getIlAmount();
                    TOTAL_TAX += arNewInvoiceLine.getIlTaxAmount();
                }

                // add tax distribution if necessary

                if (!arPdc.getArTaxCode().getTcType().equals("NONE") && !arPdc.getArTaxCode().getTcType().equals("EXEMPT")) {

                    if (arPdc.getArTaxCode().getTcInterimAccount() == null) {

                        this.addArDrEntry(arInvoice.getArDrNextLine(), "TAX", EJBCommon.FALSE, TOTAL_TAX, arPdc.getArTaxCode().getGlChartOfAccount().getCoaCode(), arInvoice, AD_BRNCH, AD_CMPNY);

                    } else {

                        this.addArDrEntry(arInvoice.getArDrNextLine(), "DEFERRED TAX", EJBCommon.FALSE, TOTAL_TAX, arPdc.getArTaxCode().getTcInterimAccount(), arInvoice, AD_BRNCH, AD_CMPNY);
                    }
                }

                // add wtax distribution if necessary

                double W_TAX_AMOUNT = 0d;

                if (arPdc.getArWithholdingTaxCode().getWtcRate() != 0 && adPreference.getPrfArWTaxRealization().equals("INVOICE")) {

                    W_TAX_AMOUNT = EJBCommon.roundIt(TOTAL_LINE * (arPdc.getArWithholdingTaxCode().getWtcRate() / 100), this.getGlFcPrecisionUnit(AD_CMPNY));

                    this.addArDrEntry(arInvoice.getArDrNextLine(), "W-TAX", EJBCommon.TRUE, W_TAX_AMOUNT, arPdc.getArWithholdingTaxCode().getGlChartOfAccount().getCoaCode(), arInvoice, AD_BRNCH, AD_CMPNY);
                }

                // add payment discount if necessary

                double DISCOUNT_AMT = 0d;

                if (arPdc.getAdPaymentTerm().getPytDiscountOnInvoice() == EJBCommon.TRUE) {

                    Collection adPaymentSchedules = adPaymentScheduleHome.findByPytCode(arPdc.getAdPaymentTerm().getPytCode(), AD_CMPNY);
                    ArrayList adPaymentScheduleList = new ArrayList(adPaymentSchedules);
                    LocalAdPaymentSchedule adPaymentSchedule = (LocalAdPaymentSchedule) adPaymentScheduleList.get(0);

                    Collection adDiscounts = adDiscountHome.findByPsCode(adPaymentSchedule.getPsCode(), AD_CMPNY);
                    ArrayList adDiscountList = new ArrayList(adDiscounts);
                    LocalAdDiscount adDiscount = (LocalAdDiscount) adDiscountList.get(0);

                    double rate = adDiscount.getDscDiscountPercent();
                    DISCOUNT_AMT = (TOTAL_LINE + TOTAL_TAX) * (rate / 100d);

                    this.addArDrIliEntry(arInvoice.getArDrNextLine(), "DISCOUNT", EJBCommon.TRUE, DISCOUNT_AMT, arPdc.getAdPaymentTerm().getGlChartOfAccount().getCoaCode(), arInvoice, AD_BRNCH, AD_CMPNY);
                }

                // add receivable distribution

                LocalAdBranchCustomer adBranchCustomer = null;

                try {

                    adBranchCustomer = adBranchCustomerHome.findBcstByCstCodeAndBrCode(arInvoice.getArCustomer().getCstCode(), AD_BRNCH, AD_CMPNY);

                } catch (FinderException ex) {

                }

                if (adBranchCustomer != null) {

                    // Use AdBranchCustomer Account
                    this.addArDrEntry(arInvoice.getArDrNextLine(), "RECEIVABLE", EJBCommon.TRUE, TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT - DISCOUNT_AMT, adBranchCustomer.getBcstGlCoaReceivableAccount(), arInvoice, AD_BRNCH, AD_CMPNY);

                } else {

                    // Use default account
                    this.addArDrEntry(arInvoice.getArDrNextLine(), "RECEIVABLE", EJBCommon.TRUE, TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT - DISCOUNT_AMT, arInvoice.getArCustomer().getCstGlCoaReceivableAccount(), arInvoice, AD_BRNCH, AD_CMPNY);
                }

                // set invoice amount due

                arInvoice.setInvAmountDue(TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT - DISCOUNT_AMT);
            }

        } catch (GlobalBranchAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalArInvoiceLine addArIlEntry(LocalArInvoiceLine arPdcInvoiceLine, LocalArInvoice arInvoice, Integer AD_CMPNY) {

        Debug.print("ArPdcInvoiceGenerationControllerBean addArIlEntry");
        try {

            LocalArInvoiceLine arNewInvoiceLine = arInvoiceLineHome.create(arPdcInvoiceLine.getIlDescription(), arPdcInvoiceLine.getIlQuantity(), arPdcInvoiceLine.getIlUnitPrice(), arPdcInvoiceLine.getIlAmount(), arPdcInvoiceLine.getIlTaxAmount(), arPdcInvoiceLine.getIlTax(), AD_CMPNY);

            arInvoice.addArInvoiceLine(arNewInvoiceLine);

            arPdcInvoiceLine.getArStandardMemoLine().addArInvoiceLine(arNewInvoiceLine);

            return arNewInvoiceLine;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void addArDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, byte DR_RVRSD, Integer COA_CODE, LocalArReceipt arReceipt, LocalArAppliedInvoice arAppliedInvoice, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArPdcInvoiceGenerationControllerBean addArDrEntry");
        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, AD_BRNCH, AD_CMPNY);

            // create distribution record

            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, DR_RVRSD, AD_CMPNY);

            arReceipt.addArDistributionRecord(arDistributionRecord);
            glChartOfAccount.addArDistributionRecord(arDistributionRecord);

            // to be used by gl journal interface for cross currency receipts
            if (arAppliedInvoice != null) {

                arAppliedInvoice.addArDistributionRecord(arDistributionRecord);
            }

        } catch (FinderException ex) {

            throw new GlobalBranchAccountNumberInvalidException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void addArDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE, LocalArInvoice arInvoice, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArPdcInvoiceGenerationControllerBean addArDrEntry");
        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, AD_BRNCH, AD_CMPNY);

            // create distribution record

            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, EJBCommon.FALSE, AD_CMPNY);

            arInvoice.addArDistributionRecord(arDistributionRecord);
            glChartOfAccount.addArDistributionRecord(arDistributionRecord);

        } catch (FinderException ex) {

            throw new GlobalBranchAccountNumberInvalidException(ex.getMessage());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalArInvoiceLineItem addArIliEntry(LocalArInvoiceLineItem arPdcInvoiceLineItem, LocalArInvoice arInvoice, LocalInvItemLocation invItemLocation, Integer AD_CMPNY) {

        Debug.print("ArPdcInvoiceGenerationControllerBean addArIliEntry");
        try {

            LocalArInvoiceLineItem arNewInvoiceLineItem = arInvoiceLineItemHome.IliLine(arPdcInvoiceLineItem.getIliLine()).IliQuantity(arPdcInvoiceLineItem.getIliQuantity()).IliUnitPrice(arPdcInvoiceLineItem.getIliUnitPrice()).IliAmount(arPdcInvoiceLineItem.getIliAmount()).IliTaxAmount(arPdcInvoiceLineItem.getIliTaxAmount()).IliDiscount1(arPdcInvoiceLineItem.getIliDiscount1()).IliDiscount2(arPdcInvoiceLineItem.getIliDiscount2()).IliDiscount3(arPdcInvoiceLineItem.getIliDiscount3()).IliDiscount4(arPdcInvoiceLineItem.getIliDiscount4()).IliTotalDiscount(arPdcInvoiceLineItem.getIliTotalDiscount()).IliTax(arPdcInvoiceLineItem.getIliTax()).IliAdCompany(AD_CMPNY).buildInvoiceLineItem();

            arInvoice.addArInvoiceLineItem(arNewInvoiceLineItem);

            invItemLocation.addArInvoiceLineItem(arNewInvoiceLineItem);

            arPdcInvoiceLineItem.getInvUnitOfMeasure().addArInvoiceLineItem(arNewInvoiceLineItem);

            return arNewInvoiceLineItem;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void addArDrIliEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE, LocalArInvoice arInvoice, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArPdcInvoiceGenerationControllerBean addArDrIliEntry");
        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, AD_BRNCH, AD_CMPNY);

            // create distribution record

            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, EJBCommon.FALSE, AD_CMPNY);

            arInvoice.addArDistributionRecord(arDistributionRecord);
            glChartOfAccount.addArDistributionRecord(arDistributionRecord);

        } catch (FinderException ex) {

            throw new GlobalBranchAccountNumberInvalidException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private Integer getArGlCoaRevenueAccount(LocalArInvoiceLine arInvoiceLine, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ArPdcInvoiceGenerationControllerBean getArGlCoaRevenueAccount");
        // generate revenue account
        try {

            StringBuilder GL_COA_ACCNT = new StringBuilder();

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            LocalGenField genField = adCompany.getGenField();

            String FL_SGMNT_SPRTR = String.valueOf(genField.getFlSegmentSeparator());

            Collection arAutoAccountingSegments = arAutoAccountingSegmentHome.findByAaAccountType("REVENUE", AD_CMPNY);

            for (Object autoAccountingSegment : arAutoAccountingSegments) {

                LocalArAutoAccountingSegment arAutoAccountingSegment = (LocalArAutoAccountingSegment) autoAccountingSegment;

                LocalGlChartOfAccount glChartOfAccount = null;

                if (arAutoAccountingSegment.getAasClassType().equals("AR CUSTOMER")) {

                    LocalAdBranchCustomer adBranchCustomer = null;

                    try {

                        adBranchCustomer = adBranchCustomerHome.findBcstByCstCodeAndBrCode(arInvoiceLine.getArInvoice().getArCustomer().getCstCode(), AD_BRNCH, AD_CMPNY);

                    } catch (FinderException ex) {

                    }

                    if (adBranchCustomer != null) {

                        // Use AdBranchCustomer Receivable Account
                        glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchCustomer.getBcstGlCoaRevenueAccount());

                    } else {

                        // Use default Receivable Account
                        glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(arInvoiceLine.getArInvoice().getArCustomer().getCstGlCoaRevenueAccount());
                    }
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

                } else if (arAutoAccountingSegment.getAasClassType().equals("AR STANDARD MEMO LINE")) {

                    glChartOfAccount = arInvoiceLine.getArStandardMemoLine().getGlChartOfAccount();

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

            GL_COA_ACCNT = new StringBuilder(GL_COA_ACCNT.substring(1));

            try {

                LocalGlChartOfAccount glGeneratedChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(GL_COA_ACCNT.toString(), AD_CMPNY);

                return glGeneratedChartOfAccount.getCoaCode();

            } catch (FinderException ex) {

                return arInvoiceLine.getArStandardMemoLine().getGlChartOfAccount().getCoaCode();
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, double QTY_SLD, Integer AD_CMPNY) {

        Debug.print("ArPdcInvoiceGenerationControllerBean convertByUomFromAndItemAndQuantity");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), AD_CMPNY);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), AD_CMPNY);

            return EJBCommon.roundIt(QTY_SLD * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), adPreference.getPrfInvQuantityPrecisionUnit());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertCostByUom(String II_NM, String UOM_NM, double unitCost, boolean isFromDefault, Integer AD_CMPNY) {

        Debug.print("ArInvoiceEntryControllerBean convertCostByUom");
        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NM, AD_CMPNY);
            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, UOM_NM, AD_CMPNY);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, invItem.getInvUnitOfMeasure().getUomName(), AD_CMPNY);

            if (isFromDefault) {

                return unitCost * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor();

            } else {

                return unitCost * invUnitOfMeasureConversion.getUmcConversionFactor() / invDefaultUomConversion.getUmcConversionFactor();
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ArPdcInvoiceGenerationControllerBean ejbCreate");
    }

}