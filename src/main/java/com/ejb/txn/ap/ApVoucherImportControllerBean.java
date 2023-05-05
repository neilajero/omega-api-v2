/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ApVoucherImportControllerBean
 * @created
 * @author
 */
package com.ejb.txn.ap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.ad.LocalAdAmountLimitHome;
import com.ejb.dao.ad.LocalAdApprovalHome;
import com.ejb.dao.ad.LocalAdApprovalQueueHome;
import com.ejb.dao.ad.LocalAdApprovalUserHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.dao.ad.LocalAdDocumentSequenceAssignmentHome;
import com.ejb.entities.ad.LocalAdPaymentSchedule;
import com.ejb.entities.ad.LocalAdPaymentTerm;
import com.ejb.dao.ad.LocalAdPaymentTermHome;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.dao.ap.ILocalApSupplierHome;
import com.ejb.entities.ap.LocalApDistributionRecord;
import com.ejb.dao.ap.LocalApDistributionRecordHome;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.entities.ap.LocalApTaxCode;
import com.ejb.dao.ap.LocalApTaxCodeHome;
import com.ejb.entities.ap.LocalApVoucher;
import com.ejb.dao.ap.LocalApVoucherBatch;
import com.ejb.dao.ap.LocalApVoucherBatchHome;
import com.ejb.dao.ap.LocalApVoucherHome;
import com.ejb.entities.ap.LocalApVoucherLineItem;
import com.ejb.dao.ap.LocalApVoucherLineItemHome;
import com.ejb.entities.ap.LocalApVoucherPaymentSchedule;
import com.ejb.dao.ap.LocalApVoucherPaymentScheduleHome;
import com.ejb.entities.ap.LocalApWithholdingTaxCode;
import com.ejb.dao.ap.LocalApWithholdingTaxCodeHome;
import com.ejb.exception.global.GlobalDocumentNumberNotUniqueException;
import com.ejb.exception.global.GlobalInvItemLocationNotFoundException;
import com.ejb.exception.global.GlobalJournalNotBalanceException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalPaymentTermInvalidException;
import com.ejb.exception.global.GlobalReferenceNumberNotUniqueException;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.dao.inv.LocalInvCostingHome;
import com.ejb.entities.inv.LocalInvItemLocation;
import com.ejb.dao.inv.LocalInvItemLocationHome;
import com.ejb.entities.inv.LocalInvUnitOfMeasure;
import com.ejb.dao.inv.LocalInvUnitOfMeasureHome;
import com.util.mod.ap.ApModVoucherDetails;
import com.util.mod.ap.ApModVoucherLineItemDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ApVoucherImportControllerEJB")
public class ApVoucherImportControllerBean extends EJBContextClass implements ApVoucherImportController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private ILocalApSupplierHome apSupplierHome;
    @EJB
    private LocalAdAmountLimitHome adAmountLimitHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalAdApprovalUserHome adApprovalUserHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdPaymentTermHome adPaymentTermHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    private LocalApTaxCodeHome apTaxCodeHome;
    @EJB
    private LocalApVoucherBatchHome apVoucherBatchHome;
    @EJB
    private LocalApVoucherHome apVoucherHome;
    @EJB
    private LocalApVoucherLineItemHome apVoucherLineItemHome;
    @EJB
    private LocalApVoucherPaymentScheduleHome apVoucherPaymentScheduleHome;
    @EJB
    private LocalApWithholdingTaxCodeHome apWithholdingTaxCodeHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;

    public void importVoucher(ArrayList list, String PYT_NM, String TC_NM, String WTC_NM, String FC_NM, String VB_NM, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalDocumentNumberNotUniqueException, GlobalPaymentTermInvalidException, GlobalInvItemLocationNotFoundException, GlobalReferenceNumberNotUniqueException, GlobalNoRecordFoundException, GlobalJournalNotBalanceException {

        Debug.print("ApVoucherImportControllerBean saveApVouVliEntry");

        LocalApVoucher apVoucher = null;

        try {

            for (Object o : list) {

                ApModVoucherDetails mdetails = (ApModVoucherDetails) o;

                // validate if document number is unique

                if (mdetails.getVouCode() == null) {

                    LocalApVoucher apExistingVoucher = null;

                    try {

                        apExistingVoucher = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(mdetails.getVouDocumentNumber(), EJBCommon.FALSE, AD_BRNCH, AD_CMPNY);

                    } catch (FinderException ex) {
                    }

                    if (apExistingVoucher != null) {

                        throw new GlobalDocumentNumberNotUniqueException(mdetails.getVouDocumentNumber());
                    }
                }

                // validate if payment term has at least one payment schedule

                if (adPaymentTermHome.findByPytName(PYT_NM, AD_CMPNY).getAdPaymentSchedules().isEmpty()) {

                    throw new GlobalPaymentTermInvalidException();
                }

                LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

                // validate reference number

                if (adPreference.getPrfApReferenceNumberValidation() == EJBCommon.TRUE) {

                    Collection apExistingVouchers = null;

                    try {

                        apExistingVouchers = apVoucherHome.findByVouReferenceNumber(mdetails.getVouReferenceNumber(), AD_BRNCH, AD_CMPNY);

                    } catch (FinderException ex) {
                    }

                    if (apExistingVouchers != null && !apExistingVouchers.isEmpty()) {

                        for (Object existingVoucher : apExistingVouchers) {

                            LocalApVoucher apExistingVoucher = (LocalApVoucher) existingVoucher;

                            if (!apExistingVoucher.getVouCode().equals(mdetails.getVouCode()))
                                throw new GlobalReferenceNumberNotUniqueException();
                        }
                    }
                }

                // used in checking if voucher should re-generate distribution records and
                // re-calculate
                // taxes
                boolean isRecalculate = true;

                // create voucher

                if (mdetails.getVouCode() == null) {
                    apVoucher = apVoucherHome.create(EJBCommon.FALSE, mdetails.getVouDescription(), mdetails.getVouDate(), mdetails.getVouDocumentNumber(), mdetails.getVouReferenceNumber(), null, mdetails.getVouConversionDate(), mdetails.getVouConversionRate(), 0d, 0d, 0d, null, null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, mdetails.getVouCreatedBy(), mdetails.getVouDateCreated(), mdetails.getVouLastModifiedBy(), mdetails.getVouDateLastModified(), null, null, null, null, EJBCommon.FALSE, mdetails.getVouPoNumber(), EJBCommon.FALSE, EJBCommon.FALSE, AD_BRNCH, AD_CMPNY);
                }

                LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName(PYT_NM, AD_CMPNY);
                apVoucher.setAdPaymentTerm(adPaymentTerm);

                LocalApTaxCode apTaxCode = apTaxCodeHome.findByTcName(TC_NM, AD_CMPNY);
                apVoucher.setApTaxCode(apTaxCode);

                LocalApWithholdingTaxCode apWithholdingTaxCode = apWithholdingTaxCodeHome.findByWtcName(WTC_NM, AD_CMPNY);
                apVoucher.setApWithholdingTaxCode(apWithholdingTaxCode);

                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);
                apVoucher.setGlFunctionalCurrency(glFunctionalCurrency);

                try {

                    LocalApSupplier apSupplier = apSupplierHome.findBySplName(mdetails.getVouSplName(), AD_BRNCH, AD_CMPNY);
                    apVoucher.setApSupplier(apSupplier);

                } catch (FinderException ex) {

                    throw new GlobalNoRecordFoundException(mdetails.getVouSplName());
                }

                LocalApVoucherBatch apVoucherBatch = null;

                try {

                    apVoucherBatch = apVoucherBatchHome.findByVbName(VB_NM, AD_BRNCH, AD_CMPNY);
                    apVoucher.setApVoucherBatch(apVoucherBatch);

                } catch (FinderException ex) {

                    // create new batch and validate

                    apVoucherBatch = apVoucherBatchHome.create(VB_NM, null, "OPEN", "VOUCHER", mdetails.getVouDateCreated(), mdetails.getVouCreatedBy(), "", AD_BRNCH, AD_CMPNY);
                    apVoucherBatch.addApVoucher(apVoucher);
                }

                if (isRecalculate) {

                    // remove all voucher line items

                    Collection apVoucherLineItems = apVoucher.getApVoucherLineItems();

                    Iterator i = apVoucherLineItems.iterator();

                    while (i.hasNext()) {

                        LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) i.next();

                        i.remove();

                        // apVoucherLineItem.entityRemove();
                        em.remove(apVoucherLineItem);
                    }

                    // remove all distribution records

                    Collection apDistributionRecords = apVoucher.getApDistributionRecords();

                    i = apDistributionRecords.iterator();

                    while (i.hasNext()) {

                        LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                        i.remove();

                        // apDistributionRecord.entityRemove();
                        em.remove(apDistributionRecord);
                    }

                    // add new voucher line item and distribution record

                    double TOTAL_TAX = 0d;
                    double TOTAL_LINE = 0d;

                    i = mdetails.getVouVliList().iterator();

                    while (i.hasNext()) {

                        ApModVoucherLineItemDetails mVliDetails = (ApModVoucherLineItemDetails) i.next();

                        Collection invItemLocations = null;

                        try {

                            invItemLocations = invItemLocationHome.findByIiName(mVliDetails.getVliIiName(), AD_CMPNY);

                        } catch (FinderException ex) {
                        }

                        if (invItemLocations == null || invItemLocations.isEmpty()) {

                            throw new GlobalInvItemLocationNotFoundException(mVliDetails.getVliIiName());
                        }

                        LocalInvItemLocation invItemLocation = null;

                        for (Object itemLocation : invItemLocations) {

                            invItemLocation = (LocalInvItemLocation) itemLocation;
                            break;
                        }

                        // set unit of measure

                        LocalInvUnitOfMeasure invUnitOfMeasure = invItemLocation.getInvItem().getInvUnitOfMeasure();
                        mVliDetails.setVliUomName(invUnitOfMeasure.getUomName());

                        LocalApVoucherLineItem apVoucherLineItem = this.addApVliEntry(mVliDetails, apVoucher, invItemLocation, AD_CMPNY);

                        // add inventory distributions

                        this.addApDrVliEntry(apVoucher.getApDrNextLine(), "EXPENSE", EJBCommon.TRUE, apVoucherLineItem.getVliAmount(), apVoucherLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(), apVoucher, AD_CMPNY);

                        TOTAL_LINE += apVoucherLineItem.getVliAmount();
                        TOTAL_TAX += apVoucherLineItem.getVliTaxAmount();
                    }

                    // check amount if balance

                    if (EJBCommon.roundIt(TOTAL_LINE + TOTAL_TAX, (short) (10)) != mdetails.getVouAmountDue()) {

                        throw new GlobalJournalNotBalanceException(mdetails.getVouDocumentNumber());
                    }

                    // add tax distribution if necessary

                    if (!apTaxCode.getTcType().equals("NONE") && !apTaxCode.getTcType().equals("EXEMPT")) {

                        this.addApDrVliEntry(apVoucher.getApDrNextLine(), "TAX", EJBCommon.TRUE, TOTAL_TAX, apTaxCode.getGlChartOfAccount().getCoaCode(), apVoucher, AD_CMPNY);
                    }

                    // add wtax distribution if necessary

                    double W_TAX_AMOUNT = 0d;

                    if (apWithholdingTaxCode.getWtcRate() != 0 && adPreference.getPrfApWTaxRealization().equals("VOUCHER")) {

                        W_TAX_AMOUNT = EJBCommon.roundIt(TOTAL_LINE * (apWithholdingTaxCode.getWtcRate() / 100), (short) (10));

                        this.addApDrVliEntry(apVoucher.getApDrNextLine(), "W-TAX", EJBCommon.FALSE, W_TAX_AMOUNT, apWithholdingTaxCode.getGlChartOfAccount().getCoaCode(), apVoucher, AD_CMPNY);
                    }

                    // add payable distribution

                    this.addApDrVliEntry(apVoucher.getApDrNextLine(), "PAYABLE", EJBCommon.FALSE, TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT, apVoucher.getApSupplier().getSplCoaGlPayableAccount(), apVoucher, AD_CMPNY);

                    // set voucher amount due

                    apVoucher.setVouAmountDue(mdetails.getVouAmountDue());

                    // remove all payment schedule

                    Collection apVoucherPaymentSchedules = apVoucher.getApVoucherPaymentSchedules();

                    i = apVoucherPaymentSchedules.iterator();

                    while (i.hasNext()) {

                        LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = (LocalApVoucherPaymentSchedule) i.next();

                        i.remove();

                        // apVoucherPaymentSchedule.entityRemove();
                        em.remove(apVoucherPaymentSchedule);
                    }

                    // create voucher payment schedule

                    short precisionUnit = (short) (10);
                    double TOTAL_PAYMENT_SCHEDULE = 0d;

                    Collection adPaymentSchedules = adPaymentTerm.getAdPaymentSchedules();

                    i = adPaymentSchedules.iterator();

                    while (i.hasNext()) {

                        LocalAdPaymentSchedule adPaymentSchedule = (LocalAdPaymentSchedule) i.next();

                        // get date due

                        GregorianCalendar gcDateDue = new GregorianCalendar();
                        gcDateDue.setTime(apVoucher.getVouDate());
                        gcDateDue.add(Calendar.DATE, adPaymentSchedule.getPsDueDay());

                        // create a payment schedule

                        double PAYMENT_SCHEDULE_AMOUNT = 0;

                        // if last payment schedule subtract to avoid rounding difference error

                        if (i.hasNext()) {

                            PAYMENT_SCHEDULE_AMOUNT = EJBCommon.roundIt((adPaymentSchedule.getPsRelativeAmount() / adPaymentTerm.getPytBaseAmount()) * apVoucher.getVouAmountDue(), precisionUnit);

                        } else {

                            PAYMENT_SCHEDULE_AMOUNT = apVoucher.getVouAmountDue() - TOTAL_PAYMENT_SCHEDULE;
                        }

                        LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = apVoucherPaymentScheduleHome.create(gcDateDue.getTime(), adPaymentSchedule.getPsLineNumber(), PAYMENT_SCHEDULE_AMOUNT, 0d, EJBCommon.FALSE, AD_CMPNY);

                        apVoucher.addApVoucherPaymentSchedule(apVoucherPaymentSchedule);

                        TOTAL_PAYMENT_SCHEDULE += PAYMENT_SCHEDULE_AMOUNT;
                    }
                }

                // set voucher approval status

                apVoucher.setVouApprovalStatus(null);
            }

        } catch (GlobalDocumentNumberNotUniqueException | GlobalJournalNotBalanceException |
                 GlobalNoRecordFoundException | GlobalReferenceNumberNotUniqueException |
                 GlobalInvItemLocationNotFoundException | GlobalPaymentTermInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ApVoucherImportControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return (short) (10);

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getInvGpQuantityPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ApVoucherImportControllerBean getInvGpQuantityPrecisionUnit");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return (short) (10);

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void addApDrVliEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE, LocalApVoucher apVoucher, Integer AD_CMPNY) {

        Debug.print("ApVoucherImportControllerBean addApDrVliEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(COA_CODE);

            // create distribution record

            LocalApDistributionRecord apDistributionRecord = apDistributionRecordHome.create(DR_LN, DR_CLSS, EJBCommon.roundIt(DR_AMNT, (short) (10)), DR_DBT, EJBCommon.FALSE, EJBCommon.FALSE, AD_CMPNY);

            apVoucher.addApDistributionRecord(apDistributionRecord);
            glChartOfAccount.addApDistributionRecord(apDistributionRecord);

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalApVoucherLineItem addApVliEntry(ApModVoucherLineItemDetails mdetails, LocalApVoucher apVoucher, LocalInvItemLocation invItemLocation, Integer AD_CMPNY) {

        Debug.print("ApVoucherImportControllerBean addApVliEntry");

        try {

            short precisionUnit = (short) (10);

            double VLI_AMNT = 0d;
            double VLI_TAX_AMNT = 0d;

            // calculate net amount

            LocalApTaxCode apTaxCode = apVoucher.getApTaxCode();

            if (apTaxCode.getTcType().equals("INCLUSIVE")) {

                VLI_AMNT = EJBCommon.roundIt(mdetails.getVliAmount() / (1 + (apTaxCode.getTcRate() / 100)), precisionUnit);

            } else {

                // tax exclusive, none, zero rated or exempt

                VLI_AMNT = mdetails.getVliAmount();
            }

            // calculate tax

            if (!apTaxCode.getTcType().equals("NONE") && !apTaxCode.getTcType().equals("EXEMPT")) {

                if (apTaxCode.getTcType().equals("INCLUSIVE")) {

                    VLI_TAX_AMNT = EJBCommon.roundIt(mdetails.getVliAmount() - VLI_AMNT, precisionUnit);

                } else if (apTaxCode.getTcType().equals("EXCLUSIVE")) {

                    VLI_TAX_AMNT = EJBCommon.roundIt(mdetails.getVliAmount() * apTaxCode.getTcRate() / 100, precisionUnit);

                } else {

                    // tax none zero-rated or exempt

                }
            }

            LocalApVoucherLineItem apVoucherLineItem = apVoucherLineItemHome.create(mdetails.getVliLine(), mdetails.getVliQuantity(), mdetails.getVliUnitCost(), VLI_AMNT, VLI_TAX_AMNT, 0, 0, 0, 0, 0, null, null, null, mdetails.getVliTax(), AD_CMPNY);

            apVoucher.addApVoucherLineItem(apVoucherLineItem);

            invItemLocation.addApVoucherLineItem(apVoucherLineItem);

            LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(mdetails.getVliUomName(), AD_CMPNY);
            invUnitOfMeasure.addApVoucherLineItem(apVoucherLineItem);

            return apVoucherLineItem;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApVoucherImportControllerBean ejbCreate");
    }
}