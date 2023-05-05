/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArInvoiceImportControllerBean
 * @created
 * @author
 */
package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ar.*;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ar.*;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.entities.inv.*;
import com.ejb.exception.ar.ArINVAmountExceedsCreditLimitException;
import com.ejb.exception.global.*;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.mod.ar.ArModInvoiceDetails;
import com.util.mod.ar.ArModInvoiceLineItemDetails;

import jakarta.ejb.*;
import javax.naming.NamingException;
import java.util.*;

@Stateless(name = "ArInvoiceImportControllerEJB")
public class ArInvoiceImportControllerBean extends EJBContextClass implements ArInvoiceImportController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalAdAmountLimitHome adAmountLimitHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalAdApprovalUserHome adApprovalUserHome;
    @EJB
    private LocalAdDiscountHome adDiscountHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdPaymentScheduleHome adPaymentScheduleHome;
    @EJB
    private LocalAdPaymentTermHome adPaymentTermHome;
    @EJB
    private LocalArCustomerBalanceHome arCustomerBalanceHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalArInvoiceBatchHome arInvoiceBatchHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalArInvoiceLineItemHome arInvoiceLineItemHome;
    @EJB
    private LocalArInvoicePaymentScheduleHome arInvoicePaymentScheduleHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalArTaxCodeHome arTaxCodeHome;
    @EJB
    private LocalArWithholdingTaxCodeHome arWithholdingTaxCodeHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;

    public void importInvoice(ArrayList list, String PYT_NM, String TC_NM, String WTC_NM, String FC_NM, String IB_NM, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, ArINVAmountExceedsCreditLimitException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalInvItemLocationNotFoundException, GlobalInventoryDateException, GlobalNoRecordFoundException, GlobalJournalNotBalanceException {

        Debug.print("ArInvoiceImportControllerBean importInvoice");

        LocalArInvoice arInvoice = null;

        try {

            for (Object o : list) {

                ArModInvoiceDetails details = (ArModInvoiceDetails) o;

                LocalArCustomer arInvCustomer = null;

                try {

                    arInvCustomer = arCustomerHome.findByCstName(details.getInvCstName(), AD_BRNCH, AD_CMPNY);

                } catch (FinderException ex) {

                    throw new GlobalNoRecordFoundException(details.getInvCstName());
                }

                String CST_CSTMR_CODE = arInvCustomer.getCstCustomerCode();

                // validate if document number is unique

                if (details.getInvCode() == null) {

                    LocalArInvoice arExistingInvoice = null;

                    try {

                        arExistingInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(details.getInvNumber(), EJBCommon.FALSE, AD_BRNCH, AD_CMPNY);

                    } catch (FinderException ex) {

                    }

                    if (arExistingInvoice != null) {

                        throw new GlobalDocumentNumberNotUniqueException(details.getInvNumber());
                    }
                }

                // validate if payment term has at least one payment schedule

                if (adPaymentTermHome.findByPytName(PYT_NM, AD_CMPNY).getAdPaymentSchedules().isEmpty()) {

                    throw new GlobalPaymentTermInvalidException();
                }

                // used in checking if invoice should re-generate distribution records and re-calculate
                // taxes
                boolean isRecalculate = true;

                // create invoice

                if (details.getInvCode() == null) {

                    arInvoice = arInvoiceHome.create(details.getInvType(), EJBCommon.FALSE, details.getInvDescription(), details.getInvDate(), details.getInvNumber(), details.getInvReferenceNumber(), details.getInvUploadNumber(), null, null, 0d, 0d, 0d, 0d, 0d, 0d, details.getInvConversionDate(), details.getInvConversionRate(), details.getInvMemo(), 0d, 0d, details.getInvBillToAddress(), details.getInvBillToContact(), details.getInvBillToAltContact(), details.getInvBillToPhone(), details.getInvBillingHeader(), details.getInvBillingFooter(), details.getInvBillingHeader2(), details.getInvBillingFooter2(), details.getInvBillingHeader3(), details.getInvBillingFooter3(), details.getInvBillingSignatory(), details.getInvSignatoryTitle(), details.getInvShipToAddress(), details.getInvShipToContact(), details.getInvShipToAltContact(), details.getInvShipToPhone(), details.getInvShipDate(), details.getInvLvFreight(), null, null, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, null, 0d, null, null, null, null, details.getInvCreatedBy(), details.getInvDateCreated(), details.getInvLastModifiedBy(), details.getInvDateLastModified(), null, null, null, null, EJBCommon.FALSE, details.getInvLvShift(), null, null, EJBCommon.FALSE, EJBCommon.FALSE, null, details.getInvDate(), AD_BRNCH, AD_CMPNY);
                }

                LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName(PYT_NM, AD_CMPNY);
                adPaymentTerm.addArInvoice(arInvoice);

                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);
                glFunctionalCurrency.addArInvoice(arInvoice);

                LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName(TC_NM, AD_CMPNY);
                arTaxCode.addArInvoice(arInvoice);

                LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName(WTC_NM, AD_CMPNY);
                arWithholdingTaxCode.addArInvoice(arInvoice);

                LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(CST_CSTMR_CODE, AD_CMPNY);
                arCustomer.addArInvoice(arInvoice);

                try {

                    LocalArInvoiceBatch arInvoiceBatch = arInvoiceBatchHome.findByIbName(IB_NM, AD_BRNCH, AD_CMPNY);
                    arInvoiceBatch.addArInvoice(arInvoice);

                } catch (FinderException ex) {

                    // create new batch
                    LocalArInvoiceBatch arInvoiceBatch = arInvoiceBatchHome.create(IB_NM, "", "OPEN", "INVOICE", details.getInvDateCreated(), details.getInvCreatedBy(), AD_BRNCH, AD_CMPNY);
                    arInvoiceBatch.addArInvoice(arInvoice);
                }

                if (isRecalculate) {

                    // remove all invoice line items

                    LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

                    Collection arInvoiceLineItems = arInvoice.getArInvoiceLineItems();

                    Iterator i = arInvoiceLineItems.iterator();

                    while (i.hasNext()) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) i.next();

                        double convertedQuantity = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), AD_CMPNY);
                        arInvoiceLineItem.getInvItemLocation().setIlCommittedQuantity(arInvoiceLineItem.getInvItemLocation().getIlCommittedQuantity() - convertedQuantity);

                        i.remove();

                        //						arInvoiceLineItem.entityRemove();
                        em.remove(arInvoiceLineItem);
                    }

                    // remove all invoice lines

                    Collection arInvoiceLines = arInvoice.getArInvoiceLines();

                    i = arInvoiceLines.iterator();

                    while (i.hasNext()) {

                        LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) i.next();

                        i.remove();

                        //						arInvoiceLine.entityRemove();
                        em.remove(arInvoiceLine);
                    }

                    // remove all sales order lines

                    Collection arSalesOrderInvoiceLines = arInvoice.getArSalesOrderInvoiceLines();

                    i = arSalesOrderInvoiceLines.iterator();

                    while (i.hasNext()) {

                        LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) i.next();

                        i.remove();

                        //						arSalesOrderInvoiceLine.entityRemove();
                        em.remove(arSalesOrderInvoiceLine);
                    }

                    // remove all distribution records

                    Collection arDistributionRecords = arInvoice.getArDistributionRecords();

                    i = arDistributionRecords.iterator();

                    while (i.hasNext()) {

                        LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

                        i.remove();

                        //						arDistributionRecord.entityRemove();
                        em.remove(arDistributionRecord);
                    }

                    // add new voucher line item and distribution record

                    double TOTAL_TAX = 0d;
                    double TOTAL_LINE = 0d;

                    i = details.getInvIliList().iterator();

                    while (i.hasNext()) {

                        ArModInvoiceLineItemDetails mIliDetails = (ArModInvoiceLineItemDetails) i.next();

                        Collection invItemLocations = null;

                        try {

                            invItemLocations = invItemLocationHome.findByIiName(mIliDetails.getIliIiName(), AD_CMPNY);

                        } catch (FinderException ex) {

                            throw new GlobalInvItemLocationNotFoundException(arInvoice.getInvNumber() + " - " + mIliDetails.getIliIiName());
                        }

                        if (invItemLocations == null || invItemLocations.isEmpty()) {

                            throw new GlobalInvItemLocationNotFoundException(mIliDetails.getIliIiName());
                        }

                        LocalInvItemLocation invItemLocation = null;

                        for (Object itemLocation : invItemLocations) {

                            invItemLocation = (LocalInvItemLocation) itemLocation;
                            break;
                        }

                        // set unit of measure

                        LocalInvUnitOfMeasure invUnitOfMeasure = invItemLocation.getInvItem().getInvUnitOfMeasure();
                        mIliDetails.setIliUomName(invUnitOfMeasure.getUomName());

                        LocalArInvoiceLineItem arInvoiceLineItem = this.addArIliEntry(mIliDetails, arInvoice, invItemLocation, AD_CMPNY);

                        // add cost of sales distribution and inventory

                        double COST = 0d;

                        try {

                            LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, AD_CMPNY);

                            COST = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                        } catch (FinderException ex) {

                            COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();
                        }

                        double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), AD_CMPNY);

                        this.addArDrIliEntry(arInvoice.getArDrNextLine(), "COGS", EJBCommon.TRUE, COST * QTY_SLD, arInvoiceLineItem.getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arInvoice, AD_CMPNY);

                        this.addArDrIliEntry(arInvoice.getArDrNextLine(), "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD, arInvoiceLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(), arInvoice, AD_CMPNY);

                        // add quantity to item location committed quantity
                        double convertedQuantity = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), AD_CMPNY);
                        invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() + convertedQuantity);

                        // add inventory sale distributions

                        this.addArDrIliEntry(arInvoice.getArDrNextLine(), "REVENUE", EJBCommon.FALSE, arInvoiceLineItem.getIliAmount(), arInvoiceLineItem.getInvItemLocation().getIlGlCoaSalesAccount(), arInvoice, AD_CMPNY);

                        TOTAL_LINE += arInvoiceLineItem.getIliAmount();
                        TOTAL_TAX += arInvoiceLineItem.getIliTaxAmount();

                    }

                    if (EJBCommon.roundIt(TOTAL_LINE + TOTAL_TAX, this.getGlFcPrecisionUnit(AD_CMPNY)) != details.getInvAmountDue()) {

                        throw new GlobalJournalNotBalanceException(details.getInvNumber());
                    }

                    // add tax distribution if necessary

                    if (!arTaxCode.getTcType().equals("NONE") && !arTaxCode.getTcType().equals("EXEMPT")) {

                        if (arTaxCode.getTcInterimAccount() == null) {

                            this.addArDrEntry(arInvoice.getArDrNextLine(), "TAX", EJBCommon.FALSE, TOTAL_TAX, arTaxCode.getGlChartOfAccount().getCoaCode(), arInvoice, AD_CMPNY);

                        } else {

                            this.addArDrEntry(arInvoice.getArDrNextLine(), "DEFERRED TAX", EJBCommon.FALSE, TOTAL_TAX, arTaxCode.getTcInterimAccount(), arInvoice, AD_CMPNY);
                        }
                    }

                    // add wtax distribution if necessary

                    double W_TAX_AMOUNT = 0d;

                    if (arWithholdingTaxCode.getWtcRate() != 0 && adPreference.getPrfArWTaxRealization().equals("INVOICE")) {

                        W_TAX_AMOUNT = EJBCommon.roundIt(TOTAL_LINE * (arWithholdingTaxCode.getWtcRate() / 100), this.getGlFcPrecisionUnit(AD_CMPNY));

                        this.addArDrEntry(arInvoice.getArDrNextLine(), "W-TAX", EJBCommon.TRUE, W_TAX_AMOUNT, arWithholdingTaxCode.getGlChartOfAccount().getCoaCode(), arInvoice, AD_CMPNY);
                    }

                    // add payment discount if necessary

                    double DISCOUNT_AMT = 0d;

                    if (adPaymentTerm.getPytDiscountOnInvoice() == EJBCommon.TRUE) {

                        Collection adPaymentSchedules = adPaymentScheduleHome.findByPytCode(adPaymentTerm.getPytCode(), AD_CMPNY);
                        ArrayList adPaymentScheduleList = new ArrayList(adPaymentSchedules);
                        LocalAdPaymentSchedule adPaymentSchedule = (LocalAdPaymentSchedule) adPaymentScheduleList.get(0);

                        Collection adDiscounts = adDiscountHome.findByPsCode(adPaymentSchedule.getPsCode(), AD_CMPNY);
                        ArrayList adDiscountList = new ArrayList(adDiscounts);
                        LocalAdDiscount adDiscount = (LocalAdDiscount) adDiscountList.get(0);

                        double rate = adDiscount.getDscDiscountPercent();
                        DISCOUNT_AMT = (TOTAL_LINE + TOTAL_TAX) * (rate / 100d);

                        this.addArDrEntry(arInvoice.getArDrNextLine(), "DISCOUNT", EJBCommon.TRUE, DISCOUNT_AMT, adPaymentTerm.getGlChartOfAccount().getCoaCode(), arInvoice, AD_CMPNY);
                    }

                    // add receivable distribution

                    this.addArDrIliEntry(arInvoice.getArDrNextLine(), "RECEIVABLE", EJBCommon.TRUE, TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT - DISCOUNT_AMT, arInvoice.getArCustomer().getCstGlCoaReceivableAccount(), arInvoice, AD_CMPNY);

                    // compute invoice amount due

                    double amountDue = TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT - DISCOUNT_AMT;

                    // validate if total amount + unposted invoices' amount + current balance + unposted
                    // receipts's amount
                    // does not exceed customer's credit limit

                    if (arCustomer.getCstCreditLimit() > 0) {

                        double balance = computeTotalBalance(details.getInvCode(), CST_CSTMR_CODE, AD_CMPNY);

                        balance += amountDue;

                        if (arCustomer.getCstCreditLimit() < balance) {

                            throw new ArINVAmountExceedsCreditLimitException();
                        }
                    }

                    // set invoice amount due

                    arInvoice.setInvAmountDue(TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT - DISCOUNT_AMT);

                    // remove all payment schedule

                    Collection arInvoicePaymentSchedules = arInvoice.getArInvoicePaymentSchedules();

                    i = arInvoicePaymentSchedules.iterator();

                    while (i.hasNext()) {

                        LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) i.next();

                        i.remove();

                        //						arInvoicePaymentSchedule.entityRemove();
                        em.remove(arInvoicePaymentSchedule);
                    }

                    // create invoice payment schedule

                    short precisionUnit = this.getGlFcPrecisionUnit(AD_CMPNY);
                    double TOTAL_PAYMENT_SCHEDULE = 0d;

                    Collection adPaymentSchedules = adPaymentTerm.getAdPaymentSchedules();

                    i = adPaymentSchedules.iterator();

                    while (i.hasNext()) {

                        LocalAdPaymentSchedule adPaymentSchedule = (LocalAdPaymentSchedule) i.next();

                        // get date due

                        GregorianCalendar gcDateDue = new GregorianCalendar();
                        gcDateDue.setTime(arInvoice.getInvDate());
                        gcDateDue.add(Calendar.DATE, adPaymentSchedule.getPsDueDay());

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
                }

                // set invoice approval status

                arInvoice.setInvApprovalStatus(null);
            }

        } catch (GlobalDocumentNumberNotUniqueException | GlobalJournalNotBalanceException |
                 GlobalNoRecordFoundException | GlobalInvItemLocationNotFoundException |
                 ArINVAmountExceedsCreditLimitException | GlobalPaymentTermInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ArInvoiceImportControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getInvGpQuantityPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ArInvoiceImportControllerBean getInvGpQuantityPrecisionUnit");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfInvQuantityPrecisionUnit();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void addArDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE, LocalArInvoice arInvoice, Integer AD_CMPNY) {

        Debug.print("ArInvoiceImportControllerBean addArDrEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(COA_CODE);

            // create distribution record

            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, EJBCommon.FALSE, AD_CMPNY);

            arInvoice.addArDistributionRecord(arDistributionRecord);
            glChartOfAccount.addArDistributionRecord(arDistributionRecord);

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalArInvoiceLineItem addArIliEntry(ArModInvoiceLineItemDetails mdetails, LocalArInvoice arInvoice, LocalInvItemLocation invItemLocation, Integer AD_CMPNY) {

        Debug.print("ArInvoiceImportControllerBean addArIliEntry");

        try {

            short precisionUnit = this.getGlFcPrecisionUnit(AD_CMPNY);

            double ILI_AMNT = 0d;
            double ILI_TAX_AMNT = 0d;

            LocalArTaxCode arTaxCode = arInvoice.getArTaxCode();

            // calculate net amount
            ILI_AMNT = this.calculateIliNetAmount(mdetails, arTaxCode.getTcRate(), arTaxCode.getTcType(), precisionUnit);

            // calculate tax
            ILI_TAX_AMNT = this.calculateIliTaxAmount(mdetails, arTaxCode.getTcRate(), arTaxCode.getTcType(), ILI_AMNT, precisionUnit);

            LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.IliLine(mdetails.getIliLine()).IliQuantity(mdetails.getIliQuantity()).IliUnitPrice(mdetails.getIliUnitPrice()).IliAmount(ILI_AMNT).IliTaxAmount(ILI_TAX_AMNT).IliDiscount1(mdetails.getIliDiscount1()).IliDiscount2(mdetails.getIliDiscount2()).IliDiscount3(mdetails.getIliDiscount3()).IliDiscount4(mdetails.getIliDiscount4()).IliTotalDiscount(mdetails.getIliTotalDiscount()).IliAdCompany(AD_CMPNY).IliTax(mdetails.getIliTax()).buildInvoiceLineItem();


            arInvoice.addArInvoiceLineItem(arInvoiceLineItem);

            invItemLocation.addArInvoiceLineItem(arInvoiceLineItem);

            LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(mdetails.getIliUomName(), AD_CMPNY);
            invUnitOfMeasure.addArInvoiceLineItem(arInvoiceLineItem);

            return arInvoiceLineItem;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void addArDrIliEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE, LocalArInvoice arInvoice, Integer AD_CMPNY) {

        Debug.print("ArInvoiceImportControllerBean addArDrIliEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(COA_CODE);

            // create distribution record

            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, EJBCommon.FALSE, AD_CMPNY);

            arInvoice.addArDistributionRecord(arDistributionRecord);
            glChartOfAccount.addArDistributionRecord(arDistributionRecord);

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double calculateIliNetAmount(ArModInvoiceLineItemDetails mdetails, double tcRate, String tcType, short precisionUnit) {

        double amount = 0d;

        if (tcType.equals("INCLUSIVE") && mdetails.getIliTax() == EJBCommon.TRUE) {

            amount = EJBCommon.roundIt(mdetails.getIliAmount() / (1 + (tcRate / 100)), precisionUnit);

        } else {

            // tax exclusive, none, zero rated or exempt

            amount = mdetails.getIliAmount();
        }

        return amount;
    }

    private double calculateIliTaxAmount(ArModInvoiceLineItemDetails mdetails, double tcRate, String tcType, double amount, short precisionUnit) {

        double taxAmount = 0d;

        if (!tcType.equals("NONE") && !tcType.equals("EXEMPT")) {

            if (mdetails.getIliTax() == EJBCommon.TRUE) {
                if (tcType.equals("INCLUSIVE")) {

                    taxAmount = EJBCommon.roundIt(mdetails.getIliAmount() - amount, precisionUnit);

                } else if (tcType.equals("EXCLUSIVE")) {

                    taxAmount = EJBCommon.roundIt(mdetails.getIliAmount() * tcRate / 100, precisionUnit);

                } else {

                    // tax none zero-rated or exempt

                }
            }
        }

        return taxAmount;
    }

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, double QTY_SLD, Integer AD_CMPNY) {

        Debug.print("ArInvoiceImportControllerBean convertByUomFromAndItemAndQuantity");

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

    private double computeTotalBalance(Integer invoiceCode, String CST_CSTMR_CODE, Integer AD_CMPNY) {

        double customerBalance = 0;

        try {

            // get latest balance

            Collection arCustomerBalances = arCustomerBalanceHome.findByCstCustomerCode(CST_CSTMR_CODE, AD_CMPNY);

            if (!arCustomerBalances.isEmpty()) {

                ArrayList customerBalanceList = new ArrayList(arCustomerBalances);

                customerBalance = ((LocalArCustomerBalance) customerBalanceList.get(customerBalanceList.size() - 1)).getCbBalance();
            }

            // get amount of unposted invoices/credit memos

            Collection arInvoices = arInvoiceHome.findUnpostedInvByCstCustomerCode(CST_CSTMR_CODE, AD_CMPNY);

            for (Object arInvoice : arInvoices) {

                LocalArInvoice mdetails = (LocalArInvoice) arInvoice;

                if (!mdetails.getInvCode().equals(invoiceCode)) {

                    if (mdetails.getInvCreditMemo() == EJBCommon.TRUE) {

                        customerBalance = customerBalance - mdetails.getInvAmountDue();

                    } else {

                        customerBalance = customerBalance + (mdetails.getInvAmountDue() - mdetails.getInvAmountPaid());
                    }
                }
            }

            // get amount of unposted receipts

            Collection arReceipts = arReceiptHome.findUnpostedRctByCstCustomerCode(CST_CSTMR_CODE, AD_CMPNY);

            for (Object receipt : arReceipts) {

                LocalArReceipt arReceipt = (LocalArReceipt) receipt;

                customerBalance = customerBalance - arReceipt.getRctAmount();
            }

        } catch (FinderException ex) {

        }

        return customerBalance;
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

    public void ejbCreate() throws CreateException {

        Debug.print("ArInvoiceImportControllerBean ejbCreate");
    }

}