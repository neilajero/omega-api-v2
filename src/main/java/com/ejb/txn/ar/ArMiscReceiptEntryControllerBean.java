/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArMiscReceiptEntryControllerBean
 * @created March 09, 2004, 1:31 PM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ar.*;
import com.ejb.dao.gl.*;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.entities.ar.*;
import com.ejb.entities.gen.LocalGenField;
import com.ejb.entities.gl.*;
import com.ejb.entities.inv.*;
import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.ejb.txnreports.inv.InvRepItemCostingController;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.ad.AdBranchDetails;
import com.util.ar.ArReceiptDetails;
import com.util.ar.ArStandardMemoLineDetails;
import com.util.ar.ArTaxCodeDetails;
import com.util.mod.ar.ArModCustomerDetails;
import com.util.mod.ar.ArModInvoiceLineDetails;
import com.util.mod.ar.ArModInvoiceLineItemDetails;
import com.util.mod.ar.ArModReceiptDetails;
import com.util.mod.inv.InvModLineItemDetails;
import com.util.mod.inv.InvModTagListDetails;
import com.util.mod.inv.InvModUnitOfMeasureDetails;

import jakarta.ejb.*;
import javax.naming.NamingException;
import java.util.*;

@Stateless(name = "ArMiscReceiptEntryControllerEJB")
public class ArMiscReceiptEntryControllerBean extends EJBContextClass implements ArMiscReceiptEntryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private ArApprovalController arApprovalController;
    @EJB
    private InvRepItemCostingController ejbRIC;
    @EJB
    private ILocalGlSetOfBookHome glSetOfBookHome;
    @EJB
    private ILocalGlAccountingCalendarValueHome glAccountingCalendarValueHome;
    @EJB
    private ILocalGlChartOfAccountBalanceHome glChartOfAccountBalanceHome;
    @EJB
    private ILocalGlInvestorAccountBalanceHome glInvestorAccountBalanceHome;
    @EJB
    private LocalArTaxCodeHome arTaxCodeHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvPriceLevelHome invPriceLevelHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalArSalespersonHome arSalespersonHome;
    @EJB
    private LocalArStandardMemoLineHome arStandardMemoLineHome;
    @EJB
    private LocalArStandardMemoLineClassHome arStandardMemoLineClassHome;
    @EJB
    private LocalArReceiptBatchHome arReceiptBatchHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalArWithholdingTaxCodeHome arWithholdingTaxCodeHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalArAppliedInvoiceHome arAppliedInvoiceHome;
    @EJB
    private LocalAdBranchBankAccountHome adBranchBankAccountHome;
    @EJB
    private LocalAdBranchArTaxCodeHome adBranchArTaxCodeHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private LocalArInvoiceLineItemHome arInvoiceLineItemHome;
    @EJB
    private LocalAdDeleteAuditTrailHome adDeleteAuditTrailHome;
    @EJB
    private LocalInvLineItemTemplateHome invLineItemTemplateHome;
    @EJB
    private LocalArInvoiceLineHome arInvoiceLineHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalArAutoAccountingSegmentHome arAutoAccountingSegmentHome;
    @EJB
    private LocalAdBranchStandardMemoLineHome adBranchStandardMemoLineHome;
    @EJB
    private LocalGlJournalHome glJournalHome;
    @EJB
    private LocalGlJournalBatchHome glJournalBatchHome;
    @EJB
    private LocalGlSuspenseAccountHome glSuspenseAccountHome;
    @EJB
    private LocalGlJournalLineHome glJournalLineHome;
    @EJB
    private LocalGlJournalSourceHome glJournalSourceHome;
    @EJB
    private LocalGlJournalCategoryHome glJournalCategoryHome;
    @EJB
    private LocalAdBankAccountBalanceHome adBankAccountBalanceHome;
    @EJB
    private LocalGlForexLedgerHome glForexLedgerHome;
    @EJB
    private LocalInvDistributionRecordHome invDistributionRecordHome;
    @EJB
    private LocalInvAdjustmentLineHome invAdjustmentLineHome;
    @EJB
    private LocalInvAdjustmentHome invAdjustmentHome;
    @EJB
    private LocalInvTagHome invTagHome;

    public ArTaxCodeDetails getArTcByTcName(String TC_NM, Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean getArTcByTcName");
        try {

            LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName(TC_NM, companyCode);

            ArTaxCodeDetails details = new ArTaxCodeDetails();
            details.setTcType(arTaxCode.getTcType());
            details.setTcRate(arTaxCode.getTcRate());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getInvUomByIiName(String II_NM, Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean getInvUomByIiName");
        ArrayList list = new ArrayList();
        try {

            LocalInvItem invItem = null;
            LocalInvUnitOfMeasure invItemUnitOfMeasure = null;

            invItem = invItemHome.findByIiName(II_NM, companyCode);
            invItemUnitOfMeasure = invItem.getInvUnitOfMeasure();

            Collection invUnitOfMeasures = null;
            for (Object o : invUnitOfMeasureHome.findByUomAdLvClass(invItemUnitOfMeasure.getUomAdLvClass(), companyCode)) {

                LocalInvUnitOfMeasure invUnitOfMeasure = (LocalInvUnitOfMeasure) o;
                InvModUnitOfMeasureDetails details = new InvModUnitOfMeasureDetails();
                details.setUomName(invUnitOfMeasure.getUomName());

                if (invUnitOfMeasure.getUomName().equals(invItemUnitOfMeasure.getUomName())) {

                    details.setDefault(true);
                }

                list.add(details);
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public double getIiSalesPriceByInvCstCustomerCodeAndIiNameAndUomName(String CST_CSTMR_CODE, String II_NM, String UOM_NM, Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean getInvIiSalesPriceByIiNameAndUomName");
        try {

            LocalArCustomer arCustomer = null;

            try {

                arCustomer = arCustomerHome.findByCstCustomerCode(CST_CSTMR_CODE, companyCode);

            } catch (FinderException ex) {

                return 0d;
            }

            double unitPrice = 0d;

            LocalInvItem invItem = invItemHome.findByIiName(II_NM, companyCode);

            LocalInvPriceLevel invPriceLevel = null;

            try {

                invPriceLevel = invPriceLevelHome.findByIiNameAndAdLvPriceLevel(II_NM, arCustomer.getCstDealPrice(), companyCode);

                if (invPriceLevel.getPlAmount() == 0) {

                    unitPrice = invItem.getIiSalesPrice();

                } else {

                    unitPrice = invPriceLevel.getPlAmount();
                }

            } catch (FinderException ex) {

                unitPrice = invItem.getIiSalesPrice();
            }

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, UOM_NM, companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, invItem.getInvUnitOfMeasure().getUomName(), companyCode);

            return EJBCommon.roundIt(unitPrice * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), this.getGlFcPrecisionUnit(companyCode));

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getAdPrfArInvoiceLineNumber(Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean getAdPrfArInvoiceLineNumber");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfArInvoiceLineNumber();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArModReceiptDetails getArRctByRctCode(Integer RCT_CODE, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArMiscReceiptEntryControllerBean getArRctByRctCode");
        try {

            LocalArReceipt arReceipt = null;
            LocalArTaxCode arTaxCode = null;

            try {

                arReceipt = arReceiptHome.findByPrimaryKey(RCT_CODE);
                arTaxCode = arReceipt.getArTaxCode();

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList list = new ArrayList();

            // get invoice line items if any

            Collection arInvoiceLineItems = arReceipt.getArInvoiceLineItems();

            if (!arInvoiceLineItems.isEmpty()) {

                for (Object invoiceLineItem : arInvoiceLineItems) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) invoiceLineItem;

                    ArModInvoiceLineItemDetails iliDetails = new ArModInvoiceLineItemDetails();

                    iliDetails.setIliCode(arInvoiceLineItem.getIliCode());
                    iliDetails.setIliLine(arInvoiceLineItem.getIliLine());
                    iliDetails.setIliQuantity(arInvoiceLineItem.getIliQuantity());
                    iliDetails.setIliUnitPrice(arInvoiceLineItem.getIliUnitPrice());
                    iliDetails.setIliIiName(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                    iliDetails.setIliLocName(arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName());
                    iliDetails.setIliUomName(arInvoiceLineItem.getInvUnitOfMeasure().getUomName());
                    iliDetails.setIliIiDescription(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiDescription());
                    iliDetails.setIliDiscount1(arInvoiceLineItem.getIliDiscount1());
                    iliDetails.setIliDiscount2(arInvoiceLineItem.getIliDiscount2());
                    iliDetails.setIliDiscount3(arInvoiceLineItem.getIliDiscount3());
                    iliDetails.setIliDiscount4(arInvoiceLineItem.getIliDiscount4());
                    iliDetails.setIliTotalDiscount(arInvoiceLineItem.getIliTotalDiscount());
                    iliDetails.setIliTax(arInvoiceLineItem.getIliTax());

                    if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiTraceMisc() == 1) {

                        // taglist if exist
                        // new code
                        if (arInvoiceLineItem.getInvTags().size() > 0) {
                            ArrayList tagList = this.getInvTagList(arInvoiceLineItem);

                            iliDetails.setIliTagList(tagList);
                            iliDetails.setTraceMisc((byte) 1);
                        }
                    }

                    String misc = "";
                    try {
                        misc = getQuantityExpiryDates(arInvoiceLineItem.getIliMisc());
                        misc = arInvoiceLineItem.getIliMisc();
                    } catch (Exception e) {
                        misc = "$" + arInvoiceLineItem.getIliQuantity() + "$" + arInvoiceLineItem.getIliMisc();
                    }

                    iliDetails.setIliMisc(misc);
                    if (arTaxCode.getTcType().equals("INCLUSIVE")) {

                        iliDetails.setIliAmount(arInvoiceLineItem.getIliAmount() + arInvoiceLineItem.getIliTaxAmount());

                    } else {

                        iliDetails.setIliAmount(arInvoiceLineItem.getIliAmount());
                    }

                    list.add(iliDetails);
                }

            } else {

                // get receipt lines

                Collection arInvoiceLines = arReceipt.getArInvoiceLines();

                for (Object invoiceLine : arInvoiceLines) {

                    LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) invoiceLine;

                    ArModInvoiceLineDetails mdetails = new ArModInvoiceLineDetails();

                    mdetails.setIlCode(arInvoiceLine.getIlCode());
                    mdetails.setIlDescription(arInvoiceLine.getIlDescription());
                    mdetails.setIlQuantity(arInvoiceLine.getIlQuantity());
                    mdetails.setIlUnitPrice(arInvoiceLine.getIlUnitPrice());
                    if (arTaxCode.getTcType().equals("INCLUSIVE")) {

                        mdetails.setIlAmount(arInvoiceLine.getIlAmount() + arInvoiceLine.getIlTaxAmount());

                    } else {

                        mdetails.setIlAmount(arInvoiceLine.getIlAmount());
                    }
                    mdetails.setIlTax(arInvoiceLine.getIlTax());
                    mdetails.setIlSmlName(arInvoiceLine.getArStandardMemoLine().getSmlName());

                    list.add(mdetails);
                }
            }

            ArModReceiptDetails mRctDetails = new ArModReceiptDetails();

            mRctDetails.setRctCode(arReceipt.getRctCode());
            mRctDetails.setRctType(arReceipt.getRctType());
            mRctDetails.setRctDocumentType(arReceipt.getRctDocumentType());
            mRctDetails.setRctDescription(arReceipt.getRctDescription());
            mRctDetails.setRctDate(arReceipt.getRctDate());
            mRctDetails.setRctNumber(arReceipt.getRctNumber());
            mRctDetails.setRctReferenceNumber(arReceipt.getRctReferenceNumber());

            mRctDetails.setRctChequeNumber(arReceipt.getRctChequeNumber());
            mRctDetails.setRctVoucherNumber(arReceipt.getRctVoucherNumber());
            mRctDetails.setRctCardNumber1(arReceipt.getRctCardNumber1());
            mRctDetails.setRctCardNumber2(arReceipt.getRctCardNumber2());
            mRctDetails.setRctCardNumber3(arReceipt.getRctCardNumber3());

            mRctDetails.setRctAmount(arReceipt.getRctAmount());
            mRctDetails.setRctConversionDate(arReceipt.getRctConversionDate());
            mRctDetails.setRctConversionRate(arReceipt.getRctConversionRate());
            mRctDetails.setRctSoldTo(arReceipt.getArCustomer().getCstBillToAddress());

            if (arReceipt.getRctPaymentMethod() == null) {
                mRctDetails.setRctPaymentMethod(arReceipt.getArCustomer().getCstPaymentMethod());
            } else {
                mRctDetails.setRctPaymentMethod(arReceipt.getRctPaymentMethod());
            }

            mRctDetails.setRctCustomerDeposit(arReceipt.getRctCustomerDeposit());
            mRctDetails.setRctAppliedDeposit(arReceipt.getRctAppliedDeposit());
            mRctDetails.setRctApprovalStatus(arReceipt.getRctApprovalStatus());
            mRctDetails.setRctPosted(arReceipt.getRctPosted());
            mRctDetails.setRctVoidApprovalStatus(arReceipt.getRctVoidApprovalStatus());
            mRctDetails.setRctVoidPosted(arReceipt.getRctVoidPosted());
            mRctDetails.setRctReasonForRejection(arReceipt.getRctReasonForRejection());
            mRctDetails.setRctVoid(arReceipt.getRctVoid());
            mRctDetails.setRctReconciled(arReceipt.getRctReconciled());
            mRctDetails.setRctCreatedBy(arReceipt.getRctCreatedBy());
            mRctDetails.setRctDateCreated(arReceipt.getRctDateCreated());
            mRctDetails.setRctLastModifiedBy(arReceipt.getRctLastModifiedBy());
            mRctDetails.setRctDateLastModified(arReceipt.getRctDateLastModified());
            mRctDetails.setRctApprovedRejectedBy(arReceipt.getRctApprovedRejectedBy());
            mRctDetails.setRctDateApprovedRejected(arReceipt.getRctDateApprovedRejected());
            mRctDetails.setRctPostedBy(arReceipt.getRctPostedBy());
            mRctDetails.setRctDatePosted(arReceipt.getRctDatePosted());
            mRctDetails.setRctFcName(arReceipt.getGlFunctionalCurrency().getFcName());
            mRctDetails.setRctTcName(arReceipt.getArTaxCode().getTcName());
            mRctDetails.setRctWtcName(arReceipt.getArWithholdingTaxCode().getWtcName());
            mRctDetails.setRctCstCustomerCode(arReceipt.getArCustomer().getCstCustomerCode());
            mRctDetails.setRctInvtrInvestorFund(arReceipt.getRctInvtrInvestorFund());
            mRctDetails.setRctInvtrNextRunDate(arReceipt.getRctInvtrNextRunDate());

            mRctDetails.setRctBaName(arReceipt.getAdBankAccount().getBaName());
            try {
                mRctDetails.setRctBaCard1Name(arReceipt.getAdBankAccountCard1().getBaName() == null ? "NONE" : arReceipt.getAdBankAccountCard1().getBaName());
            } catch (Exception ex) {
            }
            try {
                mRctDetails.setRctBaCard2Name(arReceipt.getAdBankAccountCard2().getBaName() == null ? "NONE" : arReceipt.getAdBankAccountCard2().getBaName());
            } catch (Exception ex) {
            }
            try {
                mRctDetails.setRctBaCard3Name(arReceipt.getAdBankAccountCard3().getBaName() == null ? "NONE" : arReceipt.getAdBankAccountCard3().getBaName());
            } catch (Exception ex) {
            }

            mRctDetails.setRctAmountCash(arReceipt.getRctAmountCash());
            mRctDetails.setRctAmountCheque(arReceipt.getRctAmountCheque());
            mRctDetails.setRctAmountVoucher(arReceipt.getRctAmountVoucher());
            mRctDetails.setRctAmountCard1(arReceipt.getRctAmountCard1());
            mRctDetails.setRctAmountCard2(arReceipt.getRctAmountCard2());
            mRctDetails.setRctAmountCard3(arReceipt.getRctAmountCard3());

            mRctDetails.setRctRbName(arReceipt.getArReceiptBatch() != null ? arReceipt.getArReceiptBatch().getRbName() : null);
            mRctDetails.setRctLvShift(arReceipt.getRctLvShift());
            mRctDetails.setRctSubjectToCommission(arReceipt.getRctSubjectToCommission());
            mRctDetails.setRctCstName(arReceipt.getRctCustomerName() == null || arReceipt.getRctCustomerName().equals("") ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName());
            mRctDetails.setRctTcRate(arReceipt.getArTaxCode().getTcRate());
            mRctDetails.setRctTcType(arReceipt.getArTaxCode().getTcType());

            mRctDetails.setReportParameter(arReceipt.getReportParameter());

            if (arReceipt.getArSalesperson() != null) {

                mRctDetails.setRctSlpSalespersonCode(arReceipt.getArSalesperson().getSlpSalespersonCode());
                mRctDetails.setRctSlpName(arReceipt.getArSalesperson().getSlpName());
            }

            mRctDetails.setIsInvestorSupplier(arReceipt.getArCustomer().getApSupplier() != null);

            if (!arInvoiceLineItems.isEmpty()) {

                mRctDetails.setInvIliList(list);

            } else {

                mRctDetails.setInvIlList(list);
            }

            return mRctDetails;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArModCustomerDetails getArCstByCstCustomerCode(String CST_CSTMR_CODE, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArMiscReceiptEntryControllerBean getArCstByCstCustomerCode");
        try {

            LocalArCustomer arCustomer = null;

            try {

                arCustomer = arCustomerHome.findByCstCustomerCode(CST_CSTMR_CODE, companyCode);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArModCustomerDetails mdetails = new ArModCustomerDetails();

            mdetails.setCstCustomerCode(arCustomer.getCstCustomerCode());
            mdetails.setCstAdBaName(arCustomer.getAdBankAccount() != null ? arCustomer.getAdBankAccount().getBaName() : null);
            mdetails.setCstCcWtcName(arCustomer.getArCustomerClass().getArWithholdingTaxCode() != null ? arCustomer.getArCustomerClass().getArWithholdingTaxCode().getWtcName() : null);
            mdetails.setCstBillToAddress(arCustomer.getCstBillToAddress());
            mdetails.setCstPaymentMethod(arCustomer.getCstPaymentMethod());
            mdetails.setCstName(arCustomer.getCstName());

            if (arCustomer.getApSupplier() != null) {
                mdetails.setCstSupplierCode(arCustomer.getApSupplier().getSplName());
            } else {
                mdetails.setCstSupplierCode(null);
            }

            if (arCustomer.getArSalesperson() != null && arCustomer.getCstArSalesperson2() == null) {

                mdetails.setCstSlpSalespersonCode(arCustomer.getArSalesperson().getSlpSalespersonCode());
                mdetails.setCstSlpName(arCustomer.getArSalesperson().getSlpName());

            } else if (arCustomer.getArSalesperson() == null && arCustomer.getCstArSalesperson2() != null) {

                LocalArSalesperson arSalesperson2 = null;
                arSalesperson2 = arSalespersonHome.findByPrimaryKey(arCustomer.getCstArSalesperson2());

                mdetails.setCstSlpSalespersonCode(arSalesperson2.getSlpSalespersonCode());
                mdetails.setCstSlpName(arSalesperson2.getSlpName());
            }
            if (arCustomer.getArSalesperson() != null && arCustomer.getCstArSalesperson2() != null) {

                mdetails.setCstSlpSalespersonCode(arCustomer.getArSalesperson().getSlpSalespersonCode());
                mdetails.setCstSlpName(arCustomer.getArSalesperson().getSlpName());
                LocalArSalesperson arSalesperson2 = null;
                arSalesperson2 = arSalespersonHome.findByPrimaryKey(arCustomer.getCstArSalesperson2());

                mdetails.setCstSlpSalespersonCode2(arSalesperson2.getSlpSalespersonCode());
                mdetails.setCstSlpName2(arSalesperson2.getSlpName());
            }

            if (arCustomer.getArCustomerClass().getArTaxCode() != null) {

                mdetails.setCstCcTcName(arCustomer.getArCustomerClass().getArTaxCode().getTcName());
                mdetails.setCstCcTcRate(arCustomer.getArCustomerClass().getArTaxCode().getTcRate());
                mdetails.setCstCcTcType(arCustomer.getArCustomerClass().getArTaxCode().getTcType());
            }

            if (arCustomer.getInvLineItemTemplate() != null) {
                mdetails.setCstLitName(arCustomer.getInvLineItemTemplate().getLitName());
            }

            return mdetails;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArStandardMemoLineDetails getArSmlByCstCstmrCodeSmlNm(String CST_CSTMR_CODE, String SML_NM, int AD_BRNCH, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArMiscReceiptEntryControllerBean getArSmlByCcNmSmlNm");
        try {
            LocalArStandardMemoLine arStandardMemoLine = null;
            LocalArStandardMemoLineClass arStandardMemoLineClass = null;
            LocalArCustomer arCustomer = null;

            ArStandardMemoLineDetails details = new ArStandardMemoLineDetails();

            try {

                arStandardMemoLine = arStandardMemoLineHome.findBySmlName(SML_NM, companyCode);
                details.setSmlDescription(arStandardMemoLine.getSmlDescription());
                details.setSmlUnitPrice(arStandardMemoLine.getSmlUnitPrice());
                details.setSmlTax(arStandardMemoLine.getSmlTax());

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            try {

                arCustomer = arCustomerHome.findByCstCustomerCode(CST_CSTMR_CODE, companyCode);

                if (arCustomer.getArCustomerClass() != null) {
                    String CC_NM = arCustomer.getArCustomerClass().getCcName();

                    // find memo line class in null customer code

                    try {
                        Debug.print("CST CODE:" + CST_CSTMR_CODE);
                        arStandardMemoLineClass = arStandardMemoLineClassHome.findSmcByCcNameSmlNameCstCstmrCodeAdBrnch(CC_NM, SML_NM, CST_CSTMR_CODE, AD_BRNCH, companyCode);
                        details.setSmlDescription(arStandardMemoLineClass.getSmcStandardMemoLineDescription());
                        details.setSmlUnitPrice(arStandardMemoLineClass.getSmcUnitPrice());

                    } catch (FinderException ex) {
                        Debug.print("no memo line class with cst code");
                        // find memo line class in not null customre code
                        arStandardMemoLineClass = arStandardMemoLineClassHome.findSmcByCcNameSmlNameAdBrnch(CC_NM, SML_NM, AD_BRNCH, companyCode);
                        details.setSmlDescription(arStandardMemoLineClass.getSmcStandardMemoLineDescription());
                        details.setSmlUnitPrice(arStandardMemoLineClass.getSmcUnitPrice());
                    }
                }
            } catch (FinderException ex) {

                Debug.print("no smc found");
                // no smc exist

            }

            return details;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArStandardMemoLineDetails getArSmlBySmlName(String SML_NM, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArMiscReceiptEntryControllerBean getArSmlBySmlName");
        try {

            LocalArStandardMemoLine arStandardMemoLine = null;

            try {

                arStandardMemoLine = arStandardMemoLineHome.findBySmlName(SML_NM, companyCode);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArStandardMemoLineDetails details = new ArStandardMemoLineDetails();
            details.setSmlDescription(arStandardMemoLine.getSmlDescription());
            details.setSmlUnitPrice(arStandardMemoLine.getSmlUnitPrice());
            details.setSmlTax(arStandardMemoLine.getSmlTax());

            return details;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer saveArRctEntry(ArReceiptDetails details, String BA_NM, String TC_NM, String WTC_NM, String FC_NM, String CST_CSTMR_CODE, String RB_NM, ArrayList ilList, boolean isDraft, String SLP_SLSPRSN_CODE, Integer AD_BRNCH, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException, GlobalRecordAlreadyAssignedException {

        Debug.print("ArMiscReceiptEntryControllerBean saveArRctEntry");

        LocalArReceipt arReceipt = null;

        try {

            // validate if misc receipt is already deleted

            try {

                if (details.getRctCode() != null) {

                    arReceipt = arReceiptHome.findByPrimaryKey(details.getRctCode());
                }

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if misc receipt is already posted, void, approved or pending

            if (details.getRctCode() != null && details.getRctVoid() == EJBCommon.FALSE) {

                if (arReceipt.getRctApprovalStatus() != null) {

                    if (arReceipt.getRctApprovalStatus().equals("APPROVED") || arReceipt.getRctApprovalStatus().equals("N/A")) {

                        throw new GlobalTransactionAlreadyApprovedException();

                    } else if (arReceipt.getRctApprovalStatus().equals("PENDING")) {

                        throw new GlobalTransactionAlreadyPendingException();
                    }
                }

                if (arReceipt.getRctPosted() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyPostedException();

                } else if (arReceipt.getRctVoid() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyVoidException();
                }
            }

            // misc receipt is void

            if (details.getRctCode() != null && details.getRctVoid() == EJBCommon.TRUE) {

                if (arReceipt.getRctVoid() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyVoidException();
                }

                // check if misc receipt is applied for customer deposit

                if (arReceipt.getRctType().equals("MISC") && arReceipt.getRctCustomerDeposit() == EJBCommon.TRUE) {

                    if (arReceipt.getRctAppliedDeposit() > 0) {

                        throw new GlobalRecordAlreadyAssignedException();
                    }

                    double draftAppliedDeposit = 0d;

                    Collection arUnpostedAppliedInvoices = null;

                    try {

                        arUnpostedAppliedInvoices = arAppliedInvoiceHome.findUnpostedAiWithDepositByCstCustomerCode(arReceipt.getArCustomer().getCstCustomerCode(), AD_BRNCH, companyCode);

                    } catch (FinderException ex) {

                    }

                    Iterator i = arUnpostedAppliedInvoices.iterator();

                    while (i.hasNext()) {

                        LocalArAppliedInvoice arUnpostedAppliedInvoice = (LocalArAppliedInvoice) i.next();

                        draftAppliedDeposit += arUnpostedAppliedInvoice.getAiAppliedDeposit();
                    }

                    Collection arDepositReceipts = null;

                    try {

                        arDepositReceipts = arReceiptHome.findOpenDepositEnabledPostedRctByCstCustomerCode(arReceipt.getArCustomer().getCstCustomerCode(), companyCode);

                    } catch (FinderException ex) {

                    }

                    i = arDepositReceipts.iterator();

                    while (i.hasNext()) {

                        LocalArReceipt arDepositReceipt = (LocalArReceipt) i.next();

                        if (arDepositReceipt.getRctCode().equals(arReceipt.getRctCode()) && draftAppliedDeposit > 0) {

                            throw new GlobalRecordAlreadyAssignedException();
                        }

                        draftAppliedDeposit -= arDepositReceipt.getRctAmount() - arDepositReceipt.getRctAppliedDeposit();
                    }
                }

                // check if receipt is already deposited

                if (!arReceipt.getCmFundTransferReceipts().isEmpty()) {

                    throw new GlobalRecordAlreadyAssignedException();
                }

                if (arReceipt.getRctPosted() == EJBCommon.TRUE) {

                    // generate approval status

                    String RCT_APPRVL_STATUS = null;

                    if (!isDraft) {

                        LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);

                        // check if ar receipt approval is enabled

                        if (adApproval.getAprEnableArReceipt() == EJBCommon.FALSE) {

                            RCT_APPRVL_STATUS = "N/A";

                        } else {

                            // check if receipt is self approved

                            LocalAdUser adUser = adUserHome.findByUsrName(details.getRctLastModifiedBy(), companyCode);

                            RCT_APPRVL_STATUS = arApprovalController.getApprovalStatus(adUser.getUsrDept(), details.getRctLastModifiedBy(), adUser.getUsrDescription(), "AR RECEIPT", arReceipt.getRctCode(), arReceipt.getRctNumber(), arReceipt.getRctDate(), AD_BRNCH, companyCode);
                        }
                    }

                    // reverse distribution records

                    Collection arDistributionRecords = arReceipt.getArDistributionRecords();
                    ArrayList list = new ArrayList();

                    Iterator i = arDistributionRecords.iterator();

                    while (i.hasNext()) {

                        LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

                        list.add(arDistributionRecord);
                    }

                    i = list.iterator();

                    while (i.hasNext()) {

                        LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

                        this.addArDrEntry(arReceipt.getArDrNextLine(), arDistributionRecord.getDrClass(), arDistributionRecord.getDrDebit() == EJBCommon.TRUE ? EJBCommon.FALSE : EJBCommon.TRUE, arDistributionRecord.getDrAmount(), arDistributionRecord.getGlChartOfAccount().getCoaCode(), EJBCommon.TRUE, arReceipt, AD_BRNCH, companyCode);
                    }

                    LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

                    if (RCT_APPRVL_STATUS != null && RCT_APPRVL_STATUS.equals("N/A") && adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                        arReceipt.setRctVoid(EJBCommon.TRUE);
                        this.executeArRctPost(arReceipt.getRctCode(), details.getRctLastModifiedBy(), AD_BRNCH, companyCode);
                    }

                    // set void approval status

                    arReceipt.setRctVoidApprovalStatus(RCT_APPRVL_STATUS);
                }

                arReceipt.setRctVoid(EJBCommon.TRUE);
                arReceipt.setRctLastModifiedBy(details.getRctLastModifiedBy());
                arReceipt.setRctDateLastModified(details.getRctDateLastModified());

                return arReceipt.getRctCode();
            }

            // validate if receipt number is unique receipt number is automatic then set
            // next sequence

            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

            if (details.getRctCode() == null) {

                String documentType = details.getRctDocumentType();

                try {
                    if (documentType != null) {
                        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName(documentType, companyCode);
                    } else {
                        documentType = "AR RECEIPT";
                    }
                } catch (FinderException ex) {
                    documentType = "AR RECEIPT";
                }

                try {

                    adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName(documentType, companyCode);

                } catch (FinderException ex) {

                }

                try {

                    adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, companyCode);

                } catch (FinderException ex) {

                }

                LocalArReceipt arExistingReceipt = null;

                try {

                    arExistingReceipt = arReceiptHome.findByRctNumberAndBrCode(details.getRctNumber(), AD_BRNCH, companyCode);

                } catch (FinderException ex) {
                }

                if (arExistingReceipt != null) {

                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (details.getRctNumber() == null || details.getRctNumber().trim().length() == 0)) {

                    while (true) {

                        if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                            try {

                                arReceiptHome.findByRctNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, companyCode);
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                            } catch (FinderException ex) {

                                details.setRctNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                break;
                            }

                        } else {

                            try {

                                arReceiptHome.findByRctNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, companyCode);
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                            } catch (FinderException ex) {

                                details.setRctNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                break;
                            }
                        }
                    }
                }

            } else {

                LocalArReceipt arExistingReceipt = null;

                try {

                    arExistingReceipt = arReceiptHome.findByRctNumberAndBrCode(details.getRctNumber(), AD_BRNCH, companyCode);

                } catch (FinderException ex) {
                }

                if (arExistingReceipt != null && !arExistingReceipt.getRctCode().equals(details.getRctCode())) {

                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (arReceipt.getRctNumber() != details.getRctNumber() && (details.getRctNumber() == null || details.getRctNumber().trim().length() == 0)) {

                    details.setRctNumber(arReceipt.getRctNumber());
                }
            }

            // validate if conversion date exists

            try {

                if (details.getRctConversionDate() != null) {

                    LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, companyCode);
                    LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

                    if (!glValidateFunctionalCurrency.getFcName().equals("USD")) {

                        LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(), details.getRctConversionDate(), companyCode);

                    } else if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                        LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), details.getRctConversionDate(), companyCode);
                    }
                }

            } catch (FinderException ex) {

                throw new GlobalConversionDateNotExistException();
            }

            // used in checking if receipt should re-generate distribution records and
            // re-calculate taxes
            boolean isRecalculate = true;

            // create misc receipt

            if (details.getRctCode() == null) {

                arReceipt = arReceiptHome.create("MISC", details.getRctDescription(), details.getRctDate(), details.getRctNumber(), details.getRctReferenceNumber(), details.getRctCheckNo(), details.getRctPayfileReferenceNumber(), details.getRctChequeNumber(), null, null, null, null, 0d, 0d, 0d, 0d, 0d, 0d, 0d, details.getRctConversionDate(), details.getRctConversionRate(), details.getRctSoldTo(), details.getRctPaymentMethod(), details.getRctCustomerDeposit(), 0d, null, null, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, null, null, null, null, null, details.getRctCreatedBy(), details.getRctDateCreated(), details.getRctLastModifiedBy(), details.getRctDateLastModified(), null, null, null, null, EJBCommon.FALSE, null, EJBCommon.FALSE, details.getRctSubjectToCommission(), null, null, details.getRctInvtrBeginningBalance(), details.getRctInvtrInvestorFund(), details.getRctInvtrNextRunDate(), AD_BRNCH, companyCode);

            } else {

                // check if critical fields are changed

                if (!arReceipt.getArTaxCode().getTcName().equals(TC_NM) || !arReceipt.getArWithholdingTaxCode().getWtcName().equals(WTC_NM) || !arReceipt.getArCustomer().getCstCustomerCode().equals(CST_CSTMR_CODE) || !arReceipt.getAdBankAccount().getBaName().equals(BA_NM) || ilList.size() != arReceipt.getArInvoiceLines().size()) {

                    isRecalculate = true;

                } else if (ilList.size() == arReceipt.getArInvoiceLines().size()) {

                    Iterator ilIter = arReceipt.getArInvoiceLines().iterator();
                    Iterator ilListIter = ilList.iterator();

                    while (ilIter.hasNext()) {

                        LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) ilIter.next();
                        ArModInvoiceLineDetails mdetails = (ArModInvoiceLineDetails) ilListIter.next();

                        if (!arInvoiceLine.getArStandardMemoLine().getSmlName().equals(mdetails.getIlSmlName()) || arInvoiceLine.getIlQuantity() != mdetails.getIlQuantity() || arInvoiceLine.getIlUnitPrice() != mdetails.getIlUnitPrice() || arInvoiceLine.getIlTax() != mdetails.getIlTax()) {

                            isRecalculate = true;
                            break;
                        }

                        isRecalculate = false;
                    }

                } else {

                    isRecalculate = false;
                }

                arReceipt.setRctDescription(details.getRctDescription());
                arReceipt.setRctDocumentType(details.getRctDocumentType());
                arReceipt.setRctDate(details.getRctDate());
                arReceipt.setRctNumber(details.getRctNumber());
                arReceipt.setRctReferenceNumber(details.getRctReferenceNumber());
                arReceipt.setRctConversionDate(details.getRctConversionDate());
                arReceipt.setRctConversionRate(details.getRctConversionRate());
                arReceipt.setRctSoldTo(details.getRctSoldTo());
                arReceipt.setRctPaymentMethod(details.getRctPaymentMethod());
                arReceipt.setRctCustomerDeposit(details.getRctCustomerDeposit());
                arReceipt.setRctLastModifiedBy(details.getRctLastModifiedBy());
                arReceipt.setRctDateLastModified(details.getRctDateLastModified());
                arReceipt.setRctReasonForRejection(null);
                arReceipt.setRctSubjectToCommission(details.getRctSubjectToCommission());
                arReceipt.setRctCustomerName(null);
                arReceipt.setRctInvtrInvestorFund(details.getRctInvtrInvestorFund());
                arReceipt.setRctInvtrNextRunDate(details.getRctInvtrNextRunDate());
                arReceipt.setRctInvtrBeginningBalance(details.getRctInvtrBeginningBalance());
            }

            arReceipt.setRctDocumentType(details.getRctDocumentType());
            arReceipt.setReportParameter(details.getReportParameter());

            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(BA_NM, companyCode);
            // adBankAccount.addArReceipt(arReceipt);
            arReceipt.setAdBankAccount(adBankAccount);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, companyCode);
            // glFunctionalCurrency.addArReceipt(arReceipt);
            arReceipt.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(CST_CSTMR_CODE, companyCode);
            // arCustomer.addArReceipt(arReceipt);
            arReceipt.setArCustomer(arCustomer);

            if (details.getRctCustomerName().length() > 0 && !arCustomer.getCstName().equals(details.getRctCustomerName())) {
                arReceipt.setRctCustomerName(details.getRctCustomerName());
            }

            LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName(TC_NM, companyCode);
            // arTaxCode.addArReceipt(arReceipt);
            arReceipt.setArTaxCode(arTaxCode);

            LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName(WTC_NM, companyCode);
            // arWithholdingTaxCode.addArReceipt(arReceipt);
            arReceipt.setArWithholdingTaxCode(arWithholdingTaxCode);

            LocalArSalesperson arSalesperson = null;

            if (SLP_SLSPRSN_CODE != null && SLP_SLSPRSN_CODE.length() > 0 && !SLP_SLSPRSN_CODE.equalsIgnoreCase("NO RECORD FOUND")) {

                // if he tagged a salesperson for this receipt
                arSalesperson = arSalespersonHome.findBySlpSalespersonCode(SLP_SLSPRSN_CODE, companyCode);
                // arSalesperson.addArReceipt(arReceipt);
                arReceipt.setArSalesperson(arSalesperson);

            } else {

                // if he untagged a salesperson for this receipt
                if (arReceipt.getArSalesperson() != null) {

                    arSalesperson = arSalespersonHome.findBySlpSalespersonCode(arReceipt.getArSalesperson().getSlpSalespersonCode(), companyCode);
                    arSalesperson.dropArReceipt(arReceipt);
                }

                // if no salesperson is set, invoice should not be subject to commission
                arReceipt.setRctSubjectToCommission((byte) 0);
            }

            try {

                LocalArReceiptBatch arReceiptBatch = arReceiptBatchHome.findByRbName(RB_NM, AD_BRNCH, companyCode);
                // arReceiptBatch.addArReceipt(arReceipt);
                arReceipt.setArReceiptBatch(arReceiptBatch);

            } catch (FinderException ex) {

            }

            if (isRecalculate) {

                // remove all invoice line items

                Collection arInvoiceLineItems = arReceipt.getArInvoiceLineItems();

                Iterator i = arInvoiceLineItems.iterator();

                while (i.hasNext()) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) i.next();

                    double convertedQuantity = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);
                    arInvoiceLineItem.getInvItemLocation().setIlCommittedQuantity(arInvoiceLineItem.getInvItemLocation().getIlCommittedQuantity() - convertedQuantity);

                    Collection invTags = arInvoiceLineItem.getInvTags();

                    Iterator x = invTags.iterator();

                    while (x.hasNext()) {

                        LocalInvTag invTag = (LocalInvTag) x.next();

                        x.remove();

                        // invTag.entityRemove();
                        em.remove(invTag);
                    }

                    i.remove();

                    // arInvoiceLineItem.entityRemove();
                    em.remove(arInvoiceLineItem);
                }

                // remove all invoice lines

                Collection arInvoiceLines = arReceipt.getArInvoiceLines();

                i = arInvoiceLines.iterator();

                while (i.hasNext()) {

                    LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) i.next();

                    i.remove();

                    // arInvoiceLine.entityRemove();
                    em.remove(arInvoiceLine);
                }

                // remove all distribution records

                Collection arDistributionRecords = arReceipt.getArDistributionRecords();

                i = arDistributionRecords.iterator();

                while (i.hasNext()) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

                    i.remove();

                    // arDistributionRecord.entityRemove();
                    em.remove(arDistributionRecord);
                }

                // add new invoice lines and distribution record

                double TOTAL_TAX = 0d;
                double TOTAL_LINE = 0d;
                double TOTAL_UNTAXABLE = 0d;

                i = ilList.iterator();

                while (i.hasNext()) {

                    ArModInvoiceLineDetails mInvDetails = (ArModInvoiceLineDetails) i.next();

                    LocalArInvoiceLine arInvoiceLine = this.addArIlEntry(mInvDetails, arReceipt, companyCode);

                    // add revenue/credit distributions

                    this.addArDrEntry(arReceipt.getArDrNextLine(), "REVENUE", EJBCommon.FALSE, arInvoiceLine.getIlAmount(), this.getArGlCoaRevenueAccount(arInvoiceLine, AD_BRNCH, companyCode), EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

                    TOTAL_LINE += arInvoiceLine.getIlAmount();
                    TOTAL_TAX += arInvoiceLine.getIlTaxAmount();

                    if (arInvoiceLine.getIlTax() == EJBCommon.FALSE) {
                        TOTAL_UNTAXABLE += arInvoiceLine.getIlAmount();
                    }
                }

                // add tax distribution if necessary

                if (!arTaxCode.getTcType().equals("NONE") && !arTaxCode.getTcType().equals("EXEMPT")) {
                    // add branch tax code
                    LocalAdBranchArTaxCode adBranchTaxCode = null;
                    Debug.print("ArInvoiceEntryControllerBean saveArInvIliEntry J");
                    try {
                        adBranchTaxCode = adBranchArTaxCodeHome.findBtcByTcCodeAndBrCode(arReceipt.getArTaxCode().getTcCode(), AD_BRNCH, companyCode);

                    } catch (FinderException ex) {

                    }

                    if (adBranchTaxCode != null) {
                        this.addArDrEntry(arReceipt.getArDrNextLine(), "TAX", EJBCommon.FALSE, TOTAL_TAX, adBranchTaxCode.getBtcGlCoaTaxCode(), EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

                    } else {

                        this.addArDrEntry(arReceipt.getArDrNextLine(), "TAX", EJBCommon.FALSE, TOTAL_TAX, arReceipt.getArTaxCode().getGlChartOfAccount().getCoaCode(), EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                    }
                }

                // add wtax distribution if necessary

                double W_TAX_AMOUNT = 0d;

                if (arWithholdingTaxCode.getWtcRate() != 0) {

                    W_TAX_AMOUNT = EJBCommon.roundIt((TOTAL_LINE - TOTAL_UNTAXABLE) * (arWithholdingTaxCode.getWtcRate() / 100), this.getGlFcPrecisionUnit(companyCode));

                    this.addArDrEntry(arReceipt.getArDrNextLine(), "W-TAX", EJBCommon.TRUE, W_TAX_AMOUNT, arWithholdingTaxCode.getGlChartOfAccount().getCoaCode(), EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                }

                // add cash distribution
                LocalAdBranchBankAccount adBranchBankAccount = null;
                Debug.print("ArInvoiceEntryControllerBean saveArInvIliEntry J");
                try {
                    adBranchBankAccount = adBranchBankAccountHome.findBbaByBaCodeAndBrCode(arReceipt.getAdBankAccount().getBaCode(), AD_BRNCH, companyCode);

                } catch (FinderException ex) {

                }

                if (adBranchBankAccount != null) {

                    this.addArDrEntry(arReceipt.getArDrNextLine(), "CASH", EJBCommon.TRUE, TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT, adBranchBankAccount.getBbaGlCoaCashAccount(), EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

                } else {

                    this.addArDrEntry(arReceipt.getArDrNextLine(), "CASH", EJBCommon.TRUE, TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT, arReceipt.getAdBankAccount().getBaCoaGlCashAccount(), EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                }

                // set receipt amount

                arReceipt.setRctAmount(TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT);
            }

            // generate approval status

            String RCT_APPRVL_STATUS = null;

            if (!isDraft) {

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);

                // check if ar receipt approval is enabled

                if (adApproval.getAprEnableArReceipt() == EJBCommon.FALSE) {

                    RCT_APPRVL_STATUS = "N/A";

                } else {

                    // check if receipt is self approved

                    LocalAdUser adUser = adUserHome.findByUsrName(details.getRctLastModifiedBy(), companyCode);

                    RCT_APPRVL_STATUS = arApprovalController.getApprovalStatus(adUser.getUsrDept(), details.getRctLastModifiedBy(), adUser.getUsrDescription(), "AR RECEIPT", arReceipt.getRctCode(), arReceipt.getRctNumber(), arReceipt.getRctDate(), AD_BRNCH, companyCode);
                }
            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            if (RCT_APPRVL_STATUS != null && RCT_APPRVL_STATUS.equals("N/A") && adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                this.executeArRctPost(arReceipt.getRctCode(), arReceipt.getRctLastModifiedBy(), AD_BRNCH, companyCode);
            }

            // set receipt approval status

            arReceipt.setRctApprovalStatus(RCT_APPRVL_STATUS);

            return arReceipt.getRctCode();

        } catch (GlobalRecordAlreadyDeletedException | GlobalRecordAlreadyAssignedException |
                 GlobalBranchAccountNumberInvalidException | GlobalJournalNotBalanceException |
                 GlJREffectiveDatePeriodClosedException | GlJREffectiveDateNoPeriodExistException |
                 GlobalNoApprovalApproverFoundException | GlobalNoApprovalRequesterFoundException |
                 GlobalTransactionAlreadyVoidException | GlobalTransactionAlreadyPostedException |
                 GlobalTransactionAlreadyPendingException | GlobalTransactionAlreadyApprovedException |
                 GlobalConversionDateNotExistException | GlobalDocumentNumberNotUniqueException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer saveArRctIliEntry(ArReceiptDetails details, String BA_NM, String BA_CRD1_NM, String BA_CRD2_NM, String BA_CRD3_NM, String TC_NM, String WTC_NM, String FC_NM, String CST_CSTMR_CODE, String RB_NM, ArrayList iliList, boolean isDraft, String SLP_SLSPRSN_CODE, Integer AD_BRNCH, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException, GlobalRecordAlreadyAssignedException, AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException, GlobalMiscInfoIsRequiredException, GlobalRecordInvalidException {

        Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry");

        LocalArReceipt arReceipt = null;
        Date txnStartDate = new Date();

        int lineNumberError = 0;

        try {

            // validate if misc receipt is already deleted

            try {

                if (details.getRctCode() != null) {

                    arReceipt = arReceiptHome.findByPrimaryKey(details.getRctCode());
                }

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if misc receipt is already posted, void, approved or pending

            if (details.getRctCode() != null && details.getRctVoid() == EJBCommon.FALSE) {

                if (arReceipt.getRctApprovalStatus() != null) {

                    if (arReceipt.getRctApprovalStatus().equals("APPROVED") || arReceipt.getRctApprovalStatus().equals("N/A")) {

                        throw new GlobalTransactionAlreadyApprovedException();

                    } else if (arReceipt.getRctApprovalStatus().equals("PENDING")) {

                        throw new GlobalTransactionAlreadyPendingException();
                    }
                }

                if (arReceipt.getRctPosted() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyPostedException();

                } else if (arReceipt.getRctVoid() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyVoidException();
                }
            }

            // misc receipt is void

            if (details.getRctCode() != null && details.getRctVoid() == EJBCommon.TRUE) {

                if (arReceipt.getRctVoid() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyVoidException();
                }

                // check if receipt is already deposited

                if (!arReceipt.getCmFundTransferReceipts().isEmpty()) {

                    throw new GlobalRecordAlreadyAssignedException();
                }
                Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry A");
                if (arReceipt.getRctPosted() == EJBCommon.TRUE) {

                    // generate approval status

                    String RCT_APPRVL_STATUS = null;

                    if (!isDraft) {

                        LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);

                        // check if ar receipt approval is enabled
                        Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry B");
                        if (adApproval.getAprEnableArReceipt() == EJBCommon.FALSE) {

                            RCT_APPRVL_STATUS = "N/A";

                        } else {

                            // check if receipt is self approved

                            LocalAdAmountLimit adAmountLimit = null;

                            LocalAdUser adUser = adUserHome.findByUsrName(details.getRctLastModifiedBy(), companyCode);

                            RCT_APPRVL_STATUS = arApprovalController.getApprovalStatus(adUser.getUsrDept(), details.getRctLastModifiedBy(), adUser.getUsrDescription(), "AR RECEIPT", arReceipt.getRctCode(), arReceipt.getRctNumber(), arReceipt.getRctDate(), AD_BRNCH, companyCode);
                        }
                    }

                    // reverse distribution records

                    Collection arDistributionRecords = arReceipt.getArDistributionRecords();
                    ArrayList list = new ArrayList();

                    Iterator i = arDistributionRecords.iterator();

                    while (i.hasNext()) {

                        LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

                        list.add(arDistributionRecord);
                    }

                    i = list.iterator();

                    Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry C");
                    while (i.hasNext()) {

                        LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

                        this.addArDrEntry(arReceipt.getArDrNextLine(), arDistributionRecord.getDrClass(), arDistributionRecord.getDrDebit() == EJBCommon.TRUE ? EJBCommon.FALSE : EJBCommon.TRUE, arDistributionRecord.getDrAmount(), arDistributionRecord.getGlChartOfAccount().getCoaCode(), EJBCommon.TRUE, arReceipt, AD_BRNCH, companyCode);
                    }

                    LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

                    if (RCT_APPRVL_STATUS != null && RCT_APPRVL_STATUS.equals("N/A") && adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                        arReceipt.setRctVoid(EJBCommon.TRUE);
                        this.executeArRctPost(arReceipt.getRctCode(), details.getRctLastModifiedBy(), AD_BRNCH, companyCode);
                    }

                    // set void approval status

                    arReceipt.setRctVoidApprovalStatus(RCT_APPRVL_STATUS);
                }

                arReceipt.setRctVoid(EJBCommon.TRUE);
                arReceipt.setRctLastModifiedBy(details.getRctLastModifiedBy());
                arReceipt.setRctDateLastModified(details.getRctDateLastModified());
                Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry D");
                return arReceipt.getRctCode();
            }

            // validate if receipt number is unique receipt number is automatic then set
            // next sequence

            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
            Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry E");
            if (details.getRctCode() == null) {

                String documentType = details.getRctDocumentType();

                try {
                    if (documentType != null) {
                        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName(documentType, companyCode);
                    } else {
                        documentType = "AR RECEIPT";
                    }
                } catch (FinderException ex) {
                    documentType = "AR RECEIPT";
                }

                try {

                    adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName(documentType, companyCode);

                } catch (FinderException ex) {

                }

                try {

                    adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, companyCode);

                } catch (FinderException ex) {

                }

                LocalArReceipt arExistingReceipt = null;

                try {

                    arExistingReceipt = arReceiptHome.findByRctNumberAndBrCode(details.getRctNumber(), AD_BRNCH, companyCode);

                } catch (FinderException ex) {
                }

                if (arExistingReceipt != null) {

                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (details.getRctNumber() == null || details.getRctNumber().trim().length() == 0)) {

                    while (true) {

                        if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                            try {

                                arReceiptHome.findByRctNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, companyCode);
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                            } catch (FinderException ex) {

                                details.setRctNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                break;
                            }

                        } else {

                            try {

                                arReceiptHome.findByRctNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, companyCode);
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                            } catch (FinderException ex) {

                                details.setRctNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                break;
                            }
                        }
                    }
                }
                Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry E Done");
            } else {

                LocalArReceipt arExistingReceipt = null;

                try {

                    arExistingReceipt = arReceiptHome.findByRctNumberAndBrCode(details.getRctNumber(), AD_BRNCH, companyCode);

                } catch (FinderException ex) {
                }

                if (arExistingReceipt != null && !arExistingReceipt.getRctCode().equals(details.getRctCode())) {

                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (arReceipt.getRctNumber() != details.getRctNumber() && (details.getRctNumber() == null || details.getRctNumber().trim().length() == 0)) {

                    details.setRctNumber(arReceipt.getRctNumber());
                }
                Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry E02");
            }

            // validate if conversion date exists

            try {

                if (details.getRctConversionDate() != null) {

                    LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, companyCode);
                    LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(), details.getRctConversionDate(), companyCode);
                }

            } catch (FinderException ex) {

                throw new GlobalConversionDateNotExistException();
            }

            // used in checking if receipt should re-generate distribution records and
            // re-calculate taxes
            boolean isRecalculate = true;

            // create misc receipt

            Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry F");
            if (details.getRctCode() == null) {

                arReceipt = arReceiptHome.create("MISC", details.getRctDescription(), details.getRctDate(), details.getRctNumber(), details.getRctReferenceNumber(), details.getRctCheckNo(), details.getRctPayfileReferenceNumber(), details.getRctChequeNumber(), details.getRctVoucherNumber(), details.getRctCardNumber1(), details.getRctCardNumber2(), details.getRctCardNumber3(), 0d, details.getRctAmountCash(), details.getRctAmountCheque(), details.getRctAmountVoucher(), details.getRctAmountCard1(), details.getRctAmountCard2(), details.getRctAmountCard3(), details.getRctConversionDate(), details.getRctConversionRate(), details.getRctSoldTo(), details.getRctPaymentMethod(), EJBCommon.FALSE, 0d, null, null, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, null, null, null, null, null, details.getRctCreatedBy(), details.getRctDateCreated(), details.getRctLastModifiedBy(), details.getRctDateLastModified(), null, null, null, null, EJBCommon.FALSE, details.getRctLvShift(), EJBCommon.FALSE, details.getRctSubjectToCommission(), null, null, details.getRctInvtrBeginningBalance(), EJBCommon.FALSE, null, AD_BRNCH, companyCode);

            } else {

                // check if critical fields are changed

                if (!arReceipt.getArTaxCode().getTcName().equals(TC_NM) || !arReceipt.getArWithholdingTaxCode().getWtcName().equals(WTC_NM) || !arReceipt.getArCustomer().getCstCustomerCode().equals(CST_CSTMR_CODE) || iliList.size() != arReceipt.getArInvoiceLineItems().size() || !(arReceipt.getRctDate().equals(details.getRctDate()))) {

                    isRecalculate = true;

                } else if (iliList.size() == arReceipt.getArInvoiceLineItems().size()) {

                    Iterator iliIter = arReceipt.getArInvoiceLineItems().iterator();
                    Iterator iliListIter = iliList.iterator();

                    while (iliIter.hasNext()) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) iliIter.next();
                        ArModInvoiceLineItemDetails mdetails = (ArModInvoiceLineItemDetails) iliListIter.next();
                        Debug.print("arInvoiceLineItem.getIliTotalDiscount()=" + arInvoiceLineItem.getIliTotalDiscount());
                        Debug.print("mdetails.getIliTotalDiscount()=" + mdetails.getIliTotalDiscount());
                        if (!arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName().equals(mdetails.getIliIiName()) || !arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName().equals(mdetails.getIliLocName()) || !arInvoiceLineItem.getInvUnitOfMeasure().getUomName().equals(mdetails.getIliUomName()) || arInvoiceLineItem.getIliQuantity() != mdetails.getIliQuantity() || arInvoiceLineItem.getIliUnitPrice() != mdetails.getIliUnitPrice() || arInvoiceLineItem.getIliTotalDiscount() != mdetails.getIliTotalDiscount() || arInvoiceLineItem.getIliTax() != mdetails.getIliTax()) {

                            Debug.print("------------------->");
                            isRecalculate = true;
                            break;
                        }

                        // isRecalculate = false;

                    }

                } else {

                    // isRecalculate = false;

                }

                arReceipt.setRctDocumentType(details.getRctDocumentType());
                arReceipt.setRctDescription(details.getRctDescription());
                arReceipt.setRctDate(details.getRctDate());
                arReceipt.setRctNumber(details.getRctNumber());
                arReceipt.setRctReferenceNumber(details.getRctReferenceNumber());
                arReceipt.setRctConversionDate(details.getRctConversionDate());
                arReceipt.setRctConversionRate(details.getRctConversionRate());
                arReceipt.setRctSoldTo(details.getRctSoldTo());
                arReceipt.setRctPaymentMethod(details.getRctPaymentMethod());
                arReceipt.setRctLastModifiedBy(details.getRctLastModifiedBy());
                arReceipt.setRctDateLastModified(details.getRctDateLastModified());
                arReceipt.setRctReasonForRejection(null);
                arReceipt.setRctLvShift(details.getRctLvShift());
                arReceipt.setRctSubjectToCommission(details.getRctSubjectToCommission());
                arReceipt.setRctCustomerName(details.getRctCustomerName());
                arReceipt.setRctCustomerAddress(details.getRctCustomerAddress());

                arReceipt.setRctChequeNumber(details.getRctChequeNumber());
                arReceipt.setRctVoucherNumber(details.getRctVoucherNumber());
                arReceipt.setRctCardNumber1(details.getRctCardNumber1());
                arReceipt.setRctCardNumber2(details.getRctCardNumber2());
                arReceipt.setRctCardNumber3(details.getRctCardNumber3());

                arReceipt.setRctAmountCash(details.getRctAmountCash());
                arReceipt.setRctAmountCheque(details.getRctAmountCheque());
                arReceipt.setRctAmountVoucher(details.getRctAmountVoucher());
                arReceipt.setRctAmountCard1(details.getRctAmountCard1());
                arReceipt.setRctAmountCard2(details.getRctAmountCard2());
                arReceipt.setRctAmountCard3(details.getRctAmountCard3());
            }

            arReceipt.setRctDocumentType(details.getRctDocumentType());
            arReceipt.setReportParameter(details.getReportParameter());
            Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry  G");
            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(BA_NM, companyCode);
            arReceipt.setAdBankAccount(adBankAccount);
            Debug.print("BA_CRD1_NM=" + BA_CRD1_NM);
            if (!BA_CRD1_NM.equals("") && arReceipt.getRctAmountCard1() > 0) {

                LocalAdBankAccount adBankAccountCard1 = adBankAccountHome.findByBaName(BA_CRD1_NM, companyCode);
                arReceipt.setAdBankAccountCard1(adBankAccountCard1);
            }
            try {
                LocalAdBankAccount adBankAccountCard2 = adBankAccountHome.findByBaName(BA_CRD2_NM, companyCode);
                arReceipt.setAdBankAccountCard2(adBankAccountCard2);
            } catch (Exception ex) {
            }

            try {
                LocalAdBankAccount adBankAccountCard3 = adBankAccountHome.findByBaName(BA_CRD3_NM, companyCode);
                arReceipt.setAdBankAccountCard3(adBankAccountCard3);
            } catch (Exception ex) {
            }

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, companyCode);
            // glFunctionalCurrency.addArReceipt(arReceipt);
            arReceipt.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(CST_CSTMR_CODE, companyCode);
            // arCustomer.addArReceipt(arReceipt);
            arReceipt.setArCustomer(arCustomer);

            if (details.getRctCustomerName().length() > 0 && !arCustomer.getCstName().equals(details.getRctCustomerName())) {
                arReceipt.setRctCustomerName(details.getRctCustomerName());
            }

            Debug.print("txn details " + details.getRctCustomerName());
            Debug.print("txn receipt " + arReceipt.getRctCustomerName());

            LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName(TC_NM, companyCode);
            // arTaxCode.addArReceipt(arReceipt);
            arReceipt.setArTaxCode(arTaxCode);

            LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName(WTC_NM, companyCode);
            // arWithholdingTaxCode.addArReceipt(arReceipt);
            arReceipt.setArWithholdingTaxCode(arWithholdingTaxCode);

            LocalArSalesperson arSalesperson = null;

            if (SLP_SLSPRSN_CODE != null && SLP_SLSPRSN_CODE.length() > 0 && !SLP_SLSPRSN_CODE.equalsIgnoreCase("NO RECORD FOUND")) {

                // if he tagged a salesperson for this receipt
                arSalesperson = arSalespersonHome.findBySlpSalespersonCode(SLP_SLSPRSN_CODE, companyCode);
                // arSalesperson.addArReceipt(arReceipt);
                arReceipt.setArSalesperson(arSalesperson);

            } else {

                // if he untagged a salesperson for this receipt
                if (arReceipt.getArSalesperson() != null) {

                    arSalesperson = arSalespersonHome.findBySlpSalespersonCode(arReceipt.getArSalesperson().getSlpSalespersonCode(), companyCode);
                    arSalesperson.dropArReceipt(arReceipt);
                }

                // if no salesperson is set, invoice should not be subject to commission
                arReceipt.setRctSubjectToCommission((byte) 0);
            }

            try {

                LocalArReceiptBatch arReceiptBatch = arReceiptBatchHome.findByRbName(RB_NM, AD_BRNCH, companyCode);
                // arReceiptBatch.addArReceipt(arReceipt);
                arReceipt.setArReceiptBatch(arReceiptBatch);

            } catch (FinderException ex) {

            }

            Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry  H");
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            Debug.print("isRecalculate=" + isRecalculate);
            if (isRecalculate) {
                // remove all voucher line items

                Collection arInvoiceLineItems = arReceipt.getArInvoiceLineItems();

                Iterator i = arInvoiceLineItems.iterator();

                while (i.hasNext()) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) i.next();
                    double convertedQuantity = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);
                    arInvoiceLineItem.getInvItemLocation().setIlCommittedQuantity(arInvoiceLineItem.getInvItemLocation().getIlCommittedQuantity() - convertedQuantity);

                    // remove all inv tag
                    Collection invTags = arInvoiceLineItem.getInvTags();

                    Iterator x = invTags.iterator();

                    while (x.hasNext()) {

                        LocalInvTag invTag = (LocalInvTag) x.next();

                        x.remove();

                        // invTag.entityRemove();
                        em.remove(invTag);
                    }

                    i.remove();

                    // arInvoiceLineItem.entityRemove();
                    em.remove(arInvoiceLineItem);
                }

                // remove all invoice lines
                Collection arInvoiceLines = arReceipt.getArInvoiceLines();

                i = arInvoiceLines.iterator();

                while (i.hasNext()) {

                    LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) i.next();

                    i.remove();

                    // arInvoiceLine.entityRemove();
                    em.remove(arInvoiceLine);
                }

                // remove all distribution records

                Collection arDistributionRecords = arReceipt.getArDistributionRecords();

                i = arDistributionRecords.iterator();

                while (i.hasNext()) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

                    i.remove();

                    // arDistributionRecord.entityRemove();
                    em.remove(arDistributionRecord);
                }
                // add new voucher line item and distribution record

                double TOTAL_TAX = 0d;
                double TOTAL_LINE = 0d;
                double TOTAL_SA_CREDIT = 0d;
                double TOTAL_DISCOUNT = 0d;

                i = iliList.iterator();

                LocalInvItemLocation invItemLocation = null;

                Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry  I");
                while (i.hasNext()) {

                    ArModInvoiceLineItemDetails mIliDetails = (ArModInvoiceLineItemDetails) i.next();

                    try {

                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(mIliDetails.getIliLocName(), mIliDetails.getIliIiName(), companyCode);
                        Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry  I01");
                    } catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(mIliDetails.getIliLine()));
                    }

                    // start date validation
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(arReceipt.getRctDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }

                    LocalArInvoiceLineItem arInvoiceLineItem = this.addArIliEntry(mIliDetails, arReceipt, invItemLocation, companyCode);

                    // add cost of sales distribution and inventory

                    double COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                    try {

                        LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arReceipt.getRctDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);

                        // check if rmning vl is not zero and rmng qty is 0
                        Debug.print("COGS--------------------------------------->");
                        Debug.print("invCosting.getCstRemainingQuantity()=" + invCosting.getCstRemainingQuantity());
                        Debug.print("invCosting.getCstRemainingValue()=" + invCosting.getCstRemainingValue());

                        if (invCosting.getCstRemainingQuantity() <= 0 && invCosting.getCstRemainingValue() <= 0) {
                            Debug.print("RE CALC");
                            HashMap criteria = new HashMap();
                            criteria.put("itemName", invItemLocation.getInvItem().getIiName());
                            criteria.put("location", invItemLocation.getInvLocation().getLocName());

                            ArrayList branchList = new ArrayList();

                            AdBranchDetails mdetails = new AdBranchDetails();
                            mdetails.setBrCode(AD_BRNCH);
                            branchList.add(mdetails);

                            ejbRIC.executeInvFixItemCosting(criteria, branchList, companyCode);

                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arReceipt.getRctDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                        }

                        if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Average")) {
                            COST = invCosting.getCstRemainingQuantity() <= 0 ? invCosting.getInvItemLocation().getInvItem().getIiUnitCost() : Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                        }

                        if (COST <= 0) {
                            COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();
                        } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("FIFO")) {
                            COST = Math.abs(this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), arInvoiceLineItem.getIliQuantity(), arInvoiceLineItem.getIliUnitPrice(), false, AD_BRNCH, companyCode));
                        } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Standard")) {
                            COST = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                        }

                    } catch (FinderException ex) {

                        COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();
                    }

                    double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);
                    Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry  I02");
                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {
                        lineNumberError = arInvoiceLineItem.getIliLine();
                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arInvoiceLineItem.getInvItemLocation().getIlCode(), AD_BRNCH, companyCode);

                    } catch (FinderException ex) {

                    }

                    if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiClass().equals("Stock") && arInvoiceLineItem.getInvItemLocation().getInvItem().getIiNonInventoriable() == EJBCommon.FALSE) {

                        if (adBranchItemLocation != null) {

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(), "COGS", EJBCommon.TRUE, COST * QTY_SLD, adBranchItemLocation.getBilCoaGlCostOfSalesAccount(), arReceipt, AD_BRNCH, companyCode);

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(), "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD, adBranchItemLocation.getBilCoaGlInventoryAccount(), arReceipt, AD_BRNCH, companyCode);

                        } else {

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(), "COGS", EJBCommon.TRUE, COST * QTY_SLD, arInvoiceLineItem.getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arReceipt, AD_BRNCH, companyCode);

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(), "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD, arInvoiceLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(), arReceipt, AD_BRNCH, companyCode);
                        }

                        // add quantity to item location committed quantity

                        double convertedQuantity = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);
                        invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() + convertedQuantity);
                        Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry  I03");
                    }

                    // add inventory sale distributions

                    if (adBranchItemLocation != null) {

                        this.addArDrIliEntry(arReceipt.getArDrNextLine(), "REVENUE", EJBCommon.FALSE, arInvoiceLineItem.getIliAmount(), adBranchItemLocation.getBilCoaGlSalesAccount(), arReceipt, AD_BRNCH, companyCode);

                    } else {

                        this.addArDrIliEntry(arReceipt.getArDrNextLine(), "REVENUE", EJBCommon.FALSE, arInvoiceLineItem.getIliAmount(), arInvoiceLineItem.getInvItemLocation().getIlGlCoaSalesAccount(), arReceipt, AD_BRNCH, companyCode);
                    }

                    TOTAL_LINE += arInvoiceLineItem.getIliAmount();
                    TOTAL_TAX += arInvoiceLineItem.getIliTaxAmount();
                    TOTAL_DISCOUNT += arInvoiceLineItem.getIliTotalDiscount();
                }

                // add tax distribution if necessary

                if (!arTaxCode.getTcType().equals("NONE") && !arTaxCode.getTcType().equals("EXEMPT")) {

                    LocalAdBranchArTaxCode adBranchTaxCode = null;
                    Debug.print("ArInvoiceEntryControllerBean saveArInvIliEntry J");
                    try {
                        adBranchTaxCode = adBranchArTaxCodeHome.findBtcByTcCodeAndBrCode(arReceipt.getArTaxCode().getTcCode(), AD_BRNCH, companyCode);

                    } catch (FinderException ex) {

                    }

                    if (adBranchTaxCode != null) {
                        this.addArDrEntry(arReceipt.getArDrNextLine(), "TAX", EJBCommon.FALSE, TOTAL_TAX, adBranchTaxCode.getBtcGlCoaTaxCode(), EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

                    } else {

                        this.addArDrEntry(arReceipt.getArDrNextLine(), "TAX", EJBCommon.FALSE, TOTAL_TAX, arReceipt.getArTaxCode().getGlChartOfAccount().getCoaCode(), EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                    }
                    Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry  J09");
                }

                // add wtax distribution if necessary

                double W_TAX_AMOUNT = 0d;

                if (arWithholdingTaxCode.getWtcRate() != 0) {

                    W_TAX_AMOUNT = EJBCommon.roundIt(TOTAL_LINE * (arWithholdingTaxCode.getWtcRate() / 100), this.getGlFcPrecisionUnit(companyCode));

                    this.addArDrEntry(arReceipt.getArDrNextLine(), "W-TAX", EJBCommon.TRUE, W_TAX_AMOUNT, arWithholdingTaxCode.getGlChartOfAccount().getCoaCode(), EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                    Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry  J10");
                }

                // add cash distribution
                LocalAdBranchBankAccount adBranchBankAccount = null;
                LocalAdBranchBankAccount adBranchBankAccountCard1 = null;
                LocalAdBranchBankAccount adBranchBankAccountCard2 = null;
                LocalAdBranchBankAccount adBranchBankAccountCard3 = null;

                if (arReceipt.getRctAmountCard1() > 0) {

                    try {
                        adBranchBankAccountCard1 = adBranchBankAccountHome.findBbaByBaCodeAndBrCode(arReceipt.getAdBankAccountCard1().getBaCode(), AD_BRNCH, companyCode);

                    } catch (FinderException ex) {
                    }

                    if (adBranchBankAccount != null) {

                        this.addArDrEntry(arReceipt.getArDrNextLine(), "CARD 1", EJBCommon.TRUE, arReceipt.getRctAmountCard1(), adBranchBankAccountCard1.getBbaGlCoaCashAccount(), EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

                    } else {

                        this.addArDrEntry(arReceipt.getArDrNextLine(), "CARD 1", EJBCommon.TRUE, arReceipt.getRctAmountCard1(), arReceipt.getAdBankAccountCard1().getBaCoaGlCashAccount(), EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                    }
                }

                if (arReceipt.getRctAmountCard2() > 0) {

                    try {
                        adBranchBankAccountCard2 = adBranchBankAccountHome.findBbaByBaCodeAndBrCode(arReceipt.getAdBankAccountCard2().getBaCode(), AD_BRNCH, companyCode);

                    } catch (FinderException ex) {
                    }

                    if (adBranchBankAccount != null) {

                        this.addArDrEntry(arReceipt.getArDrNextLine(), "CARD 2", EJBCommon.TRUE, arReceipt.getRctAmountCard2(), adBranchBankAccountCard2.getBbaGlCoaCashAccount(), EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

                    } else {

                        this.addArDrEntry(arReceipt.getArDrNextLine(), "CARD 2", EJBCommon.TRUE, arReceipt.getRctAmountCard2(), arReceipt.getAdBankAccountCard2().getBaCoaGlCashAccount(), EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                    }
                }

                if (arReceipt.getRctAmountCard3() > 0) {

                    try {
                        adBranchBankAccountCard3 = adBranchBankAccountHome.findBbaByBaCodeAndBrCode(arReceipt.getAdBankAccountCard3().getBaCode(), AD_BRNCH, companyCode);

                    } catch (FinderException ex) {
                    }

                    if (adBranchBankAccount != null) {

                        this.addArDrEntry(arReceipt.getArDrNextLine(), "CARD 3", EJBCommon.TRUE, arReceipt.getRctAmountCard3(), adBranchBankAccountCard3.getBbaGlCoaCashAccount(), EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

                    } else {

                        this.addArDrEntry(arReceipt.getArDrNextLine(), "CARD 3", EJBCommon.TRUE, arReceipt.getRctAmountCard3(), arReceipt.getAdBankAccountCard3().getBaCoaGlCashAccount(), EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                    }
                }

                try {
                    adBranchBankAccount = adBranchBankAccountHome.findBbaByBaCodeAndBrCode(arReceipt.getAdBankAccount().getBaCode(), AD_BRNCH, companyCode);

                } catch (FinderException ex) {
                }

                if (adBranchBankAccount != null) {

                    this.addArDrEntry(arReceipt.getArDrNextLine(), "CASH", EJBCommon.TRUE, TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT - TOTAL_DISCOUNT - arReceipt.getRctAmountVoucher() - arReceipt.getRctAmountCheque() - arReceipt.getRctAmountCard1() - arReceipt.getRctAmountCard2() - arReceipt.getRctAmountCard3(), adBranchBankAccount.getBbaGlCoaCashAccount(), EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

                    if (arReceipt.getRctAmountCheque() > 0) {
                        this.addArDrEntry(arReceipt.getArDrNextLine(), "CHEQUE", EJBCommon.TRUE, arReceipt.getRctAmountCheque(), adBranchBankAccount.getBbaGlCoaCashAccount(), EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                    }

                    if (arReceipt.getRctAmountVoucher() > 0) {

                        this.addArDrEntry(arReceipt.getArDrNextLine(), "VOUCHER", EJBCommon.TRUE, arReceipt.getRctAmountVoucher(), adBranchBankAccount.getBbaGlCoaSalesDiscountAccount(), EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                    }

                    if (TOTAL_DISCOUNT > 0) {
                        this.addArDrEntry(arReceipt.getArDrNextLine(), "SALES DISCOUNT", EJBCommon.TRUE, TOTAL_DISCOUNT, adBranchBankAccount.getBbaGlCoaSalesDiscountAccount(), EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                    }

                } else {

                    this.addArDrEntry(arReceipt.getArDrNextLine(), "CASH", EJBCommon.TRUE, TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT - TOTAL_DISCOUNT - arReceipt.getRctAmountVoucher() - arReceipt.getRctAmountCheque() - arReceipt.getRctAmountCard1() - arReceipt.getRctAmountCard2() - arReceipt.getRctAmountCard3(), arReceipt.getAdBankAccount().getBaCoaGlCashAccount(), EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

                    if (arReceipt.getRctAmountCheque() > 0) {

                        this.addArDrEntry(arReceipt.getArDrNextLine(), "CHEQUE", EJBCommon.TRUE, arReceipt.getRctAmountCheque(), arReceipt.getAdBankAccount().getBaCoaGlCashAccount(), EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                    }

                    if (arReceipt.getRctAmountVoucher() > 0) {

                        this.addArDrEntry(arReceipt.getArDrNextLine(), "VOUCHER", EJBCommon.TRUE, arReceipt.getRctAmountVoucher(), arReceipt.getAdBankAccount().getBaCoaGlSalesDiscount(), EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                    }

                    if (TOTAL_DISCOUNT > 0) {
                        this.addArDrEntry(arReceipt.getArDrNextLine(), "SALES DISCOUNT", EJBCommon.TRUE, TOTAL_DISCOUNT, arReceipt.getAdBankAccount().getBaCoaGlSalesDiscount(), EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                    }
                }

                Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry  J11");
                // set receipt amount

                arReceipt.setRctAmount(TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT);
                arReceipt.setRctAmountCash(arReceipt.getRctAmountCash() == 0 ? arReceipt.getRctAmount() : arReceipt.getRctAmountCash());

            } else {

                // InvTag reupdate

                Iterator i = iliList.iterator();

                LocalInvItemLocation invItemLocation = null;

                Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry  K");
                while (i.hasNext()) {

                    ArModInvoiceLineItemDetails mIliDetails = (ArModInvoiceLineItemDetails) i.next();

                    try {
                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(mIliDetails.getIliLocName(), mIliDetails.getIliIiName(), companyCode);
                        try {
                            LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.findByRctCodeAndIlCodeAndUomName(arReceipt.getRctCode(), invItemLocation.getIlCode(), mIliDetails.getIliUomName(), companyCode);
                        } catch (FinderException ex) {
                        }

                    } catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(mIliDetails.getIliLine()));
                    }

                    // start date validation
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(arReceipt.getRctDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                        Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry  K02");
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }
                }
            }

            // insufficient stock checking
            Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry  L");
            // if (adPreference.getPrfArAllowPriorDate() == EJBCommon.TRUE && !isDraft) {
            if (adPreference.getPrfArCheckInsufficientStock() == EJBCommon.TRUE && !isDraft) {
                boolean hasInsufficientItems = false;
                StringBuilder insufficientItems = new StringBuilder();

                Collection arInvoiceLineItems = arReceipt.getArInvoiceLineItems();

                Iterator i = arInvoiceLineItems.iterator();

                HashMap cstMap = new HashMap();

                while (i.hasNext()) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) i.next();
                    String II_NM = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName();
                    String LOC_NM = arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName();
                    if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiNonInventoriable() == EJBCommon.FALSE) {

                        LocalInvCosting invCosting = null;
                        double ILI_QTY = this.convertByUomAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), Math.abs(arInvoiceLineItem.getIliQuantity()), companyCode);

                        double CURR_QTY = 0;
                        boolean isIlFound = false;

                        if (cstMap.containsKey(arInvoiceLineItem.getInvItemLocation().getIlCode().toString())) {
                            Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry  L04 - A");
                            isIlFound = true;
                            CURR_QTY = (Double) cstMap.get(arInvoiceLineItem.getInvItemLocation().getIlCode().toString());

                        } else {

                            try {

                                invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arReceipt.getRctDate(), II_NM, LOC_NM, AD_BRNCH, companyCode);
                                Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry  L04");
                            } catch (FinderException ex) {
                                Debug.print("CATCH");
                            }
                        }

                        if (invCosting != null) {

                            CURR_QTY = this.convertByUomAndQuantity(arInvoiceLineItem.getInvItemLocation().getInvItem().getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), invCosting.getCstRemainingQuantity(), companyCode);

                            Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry  L05");
                        }

                        double LOWEST_QTY = this.convertByUomAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), 1, companyCode);
                        Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry  L06");
                        Debug.print("CURRENT QTY: " + CURR_QTY);
                        Debug.print("ILI QTY: " + ILI_QTY);
                        Debug.print("LOWEST_QTY: " + LOWEST_QTY);
                        Debug.print("invCosting: " + invCosting);
                        if ((invCosting == null && isIlFound == false) || CURR_QTY == 0 || CURR_QTY - ILI_QTY <= -LOWEST_QTY) {
                            hasInsufficientItems = true;
                            Debug.print("Insufficient Item: " + arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                            insufficientItems.append(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName()).append(", ");
                        }

                        CURR_QTY -= ILI_QTY;

                        if (isIlFound) {
                            cstMap.remove(arInvoiceLineItem.getInvItemLocation().getIlCode().toString());
                        }

                        cstMap.put(arInvoiceLineItem.getInvItemLocation().getIlCode().toString(), CURR_QTY);
                    }
                }
                if (hasInsufficientItems) {

                    throw new GlobalRecordInvalidException(insufficientItems.substring(0, insufficientItems.lastIndexOf(",")));
                }
            }

            // generate approval status

            String RCT_APPRVL_STATUS = null;

            if (!isDraft) {

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);

                // check if ar receipt approval is enabled

                if (adApproval.getAprEnableArReceipt() == EJBCommon.FALSE) {

                    RCT_APPRVL_STATUS = "N/A";

                } else {

                    // check if receipt is self approved

                    LocalAdUser adUser = adUserHome.findByUsrName(details.getRctLastModifiedBy(), companyCode);

                    RCT_APPRVL_STATUS = arApprovalController.getApprovalStatus(adUser.getUsrDept(), details.getRctLastModifiedBy(), adUser.getUsrDescription(), "AR RECEIPT", arReceipt.getRctCode(), arReceipt.getRctNumber(), arReceipt.getRctDate(), AD_BRNCH, companyCode);
                }
            }

            if (RCT_APPRVL_STATUS != null && RCT_APPRVL_STATUS.equals("N/A") && adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                this.executeArRctPost(arReceipt.getRctCode(), arReceipt.getRctLastModifiedBy(), AD_BRNCH, companyCode);
            }

            // set receipt approval status

            arReceipt.setRctApprovalStatus(RCT_APPRVL_STATUS);

            Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry " + txnStartDate);

            return arReceipt.getRctCode();

        } catch (GlobalRecordAlreadyDeletedException | GlobalMiscInfoIsRequiredException |
                 GlobalExpiryDateNotFoundException | AdPRFCoaGlVarianceAccountNotFoundException |
                 GlobalRecordInvalidException | GlobalRecordAlreadyAssignedException | GlobalInventoryDateException |
                 GlobalJournalNotBalanceException | GlJREffectiveDatePeriodClosedException |
                 GlJREffectiveDateNoPeriodExistException | GlobalInvItemLocationNotFoundException |
                 GlobalNoApprovalApproverFoundException | GlobalNoApprovalRequesterFoundException |
                 GlobalTransactionAlreadyVoidException | GlobalTransactionAlreadyPostedException |
                 GlobalTransactionAlreadyPendingException | GlobalTransactionAlreadyApprovedException |
                 GlobalConversionDateNotExistException | GlobalDocumentNumberNotUniqueException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (GlobalBranchAccountNumberInvalidException ex) {

            // Retrieve Line Number
            ex.setLineNumberError(lineNumberError);
            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteArRctEntry(Integer RCT_CODE, String AD_USR, Integer companyCode) throws GlobalRecordAlreadyDeletedException {

        Debug.print("ArMiscReceiptEntryControllerBean deleteArRctEntry");
        try {

            LocalArReceipt arReceipt = arReceiptHome.findByPrimaryKey(RCT_CODE);

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            Collection arInvoiceLineItems = arReceipt.getArInvoiceLineItems();

            for (Object invoiceLineItem : arInvoiceLineItems) {

                LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) invoiceLineItem;
                double convertedQuantity = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);
                arInvoiceLineItem.getInvItemLocation().setIlCommittedQuantity(arInvoiceLineItem.getInvItemLocation().getIlCommittedQuantity() - convertedQuantity);
            }

            if (arReceipt.getRctApprovalStatus() != null && arReceipt.getRctApprovalStatus().equals("PENDING")) {

                Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode("AR RECEIPT", arReceipt.getRctCode(), companyCode);

                for (Object approvalQueue : adApprovalQueues) {

                    LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) approvalQueue;

                    // adApprovalQueue.entityRemove();
                    em.remove(adApprovalQueue);
                }
            }

            adDeleteAuditTrailHome.create("AR RECEIPT", arReceipt.getRctDate(), arReceipt.getRctNumber(), arReceipt.getRctReferenceNumber(), arReceipt.getRctAmount(), AD_USR, new Date(), companyCode);

            // arReceipt.entityRemove();
            em.remove(arReceipt);

        } catch (FinderException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlobalRecordAlreadyDeletedException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean getGlFcPrecisionUnit");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdApprovalNotifiedUsersByRctCode(Integer RCT_CODE, Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean getAdApprovalNotifiedUsersByRctCode");
        ArrayList list = new ArrayList();
        try {

            LocalArReceipt arReceipt = arReceiptHome.findByPrimaryKey(RCT_CODE);

            if (arReceipt.getRctVoid() == EJBCommon.FALSE && arReceipt.getRctPosted() == EJBCommon.TRUE) {

                list.add("DOCUMENT POSTED");
                return list;

            } else if (arReceipt.getRctVoid() == EJBCommon.TRUE && arReceipt.getRctVoidPosted() == EJBCommon.TRUE) {

                list.add("DOCUMENT POSTED");
                return list;
            }

            Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode("AR RECEIPT", RCT_CODE, companyCode);

            for (Object approvalQueue : adApprovalQueues) {

                LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) approvalQueue;

                list.add(adApprovalQueue.getAdUser().getUsrDescription());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfEnableArMiscReceiptBatch(Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean getAdPrfEnableArReceiptBatch");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfEnableArMiscReceiptBatch();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfEnableInvShift(Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean getAdPrfEnableInvShift");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfInvEnableShift();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArOpenRbAll(Integer AD_BRNCH, Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean getArOpenRbAll");
        ArrayList list = new ArrayList();
        try {

            Collection arReceiptBatches = arReceiptBatchHome.findOpenRbByRbType("MISC", AD_BRNCH, companyCode);

            for (Object receiptBatch : arReceiptBatches) {

                LocalArReceiptBatch arReceiptBatch = (LocalArReceiptBatch) receiptBatch;

                list.add(arReceiptBatch.getRbName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getInvGpQuantityPrecisionUnit(Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean getInvGpQuantityPrecisionUnit");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfInvQuantityPrecisionUnit();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfArDisableSalesPrice(Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean getAdPrfArDisableSalesPrice");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfArDisableSalesPrice();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public String getInvItemClassByIiName(String II_NM, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArMiscReceiptEntryControllerBean getInvItemClassByIiCode");
        try {

            try {
                Debug.print("II_NM=" + II_NM + "=II_NM");
                LocalInvItem invItem = invItemHome.findByIiName(II_NM, companyCode);

                return invItem.getIiClass();

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;
        }
    }

    public double getInvIiSalesPriceByIiNameAndUomName(String II_NM, String UOM_NM, Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean getInvIiSalesPriceByIiNameAndUomName");
        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NM, companyCode);
            LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(UOM_NM, companyCode);

            return EJBCommon.roundIt(invItem.getIiSalesPrice() * invUnitOfMeasure.getUomConversionFactor() / invItem.getInvUnitOfMeasure().getUomConversionFactor(), this.getGlFcPrecisionUnit(companyCode));

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public String getInvIiClassByIiName(String II_NM, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArMiscReceiptEntryControllerBean getInvItemClassByIiCode");
        try {

            try {

                LocalInvItem invItem = invItemHome.findByIiName(II_NM, companyCode);

                return invItem.getIiClass();

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;
        }
    }

    public ArrayList getInvLitByCstLitName(String CST_LIT_NAME, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArMiscReceiptEntryControllerBean getInvLitByCstLitName");
        try {

            LocalInvLineItemTemplate invLineItemTemplate = null;

            try {

                invLineItemTemplate = invLineItemTemplateHome.findByLitName(CST_LIT_NAME, companyCode);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList list = new ArrayList();

            // get line items if any

            Collection invLineItems = invLineItemTemplate.getInvLineItems();

            if (!invLineItems.isEmpty()) {

                for (Object lineItem : invLineItems) {

                    LocalInvLineItem invLineItem = (LocalInvLineItem) lineItem;

                    InvModLineItemDetails liDetails = new InvModLineItemDetails();

                    liDetails.setLiCode(invLineItem.getLiCode());
                    liDetails.setLiLine(invLineItem.getLiLine());
                    liDetails.setLiIiName(invLineItem.getInvItemLocation().getInvItem().getIiName());
                    liDetails.setLiLocName(invLineItem.getInvItemLocation().getInvLocation().getLocName());
                    liDetails.setLiUomName(invLineItem.getInvUnitOfMeasure().getUomName());
                    liDetails.setLiIiDescription(invLineItem.getInvItemLocation().getInvItem().getIiDescription());

                    list.add(liDetails);
                }
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public double getFrRateByFrNameAndFrDate(String FC_NM, Date CONVERSION_DATE, Integer companyCode) throws GlobalConversionDateNotExistException {

        Debug.print("ArMiscReceiptEntryControllerBean getFrRateByFrNameAndFrDate");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, companyCode);

            double CONVERSION_RATE = 1;

            // Get functional currency rate

            if (!FC_NM.equals("USD")) {

                LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glFunctionalCurrency.getFcCode(), CONVERSION_DATE, companyCode);

                CONVERSION_RATE = glFunctionalCurrencyRate.getFrXToUsd();
            }

            // Get set of book functional currency rate if necessary

            if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), CONVERSION_DATE, companyCode);

                CONVERSION_RATE = CONVERSION_RATE / glCompanyFunctionalCurrencyRate.getFrXToUsd();
            }

            return CONVERSION_RATE;

        } catch (FinderException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlobalConversionDateNotExistException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods
    private LocalArInvoiceLine addArIlEntry(ArModInvoiceLineDetails mdetails, LocalArReceipt arReceipt, Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean addArIlEntry");
        try {

            short precisionUnit = this.getGlFcPrecisionUnit(companyCode);

            double IL_AMNT = 0d;
            double IL_TAX_AMNT = 0d;

            if (mdetails.getIlTax() == EJBCommon.TRUE) {

                // calculate net amount

                LocalArTaxCode arTaxCode = arReceipt.getArTaxCode();

                if (arTaxCode.getTcType().equals("INCLUSIVE")) {

                    IL_AMNT = EJBCommon.roundIt(mdetails.getIlAmount() / (1 + (arTaxCode.getTcRate() / 100)), precisionUnit);

                } else {

                    // tax exclusive, none, zero rated or exempt

                    IL_AMNT = mdetails.getIlAmount();
                }

                // calculate tax

                if (!arTaxCode.getTcType().equals("NONE") && !arTaxCode.getTcType().equals("EXEMPT")) {

                    if (arTaxCode.getTcType().equals("INCLUSIVE")) {

                        IL_TAX_AMNT = EJBCommon.roundIt(mdetails.getIlAmount() - IL_AMNT, precisionUnit);

                    } else if (arTaxCode.getTcType().equals("EXCLUSIVE")) {

                        IL_TAX_AMNT = EJBCommon.roundIt(mdetails.getIlAmount() * arTaxCode.getTcRate() / 100, precisionUnit);

                    } else {

                        // tax none zero-rated or exempt

                    }
                }

            } else {

                IL_AMNT = mdetails.getIlAmount();
            }

            LocalArInvoiceLine arInvoiceLine = arInvoiceLineHome.create(mdetails.getIlDescription(), mdetails.getIlQuantity(), mdetails.getIlUnitPrice(), IL_AMNT, IL_TAX_AMNT, mdetails.getIlTax(), companyCode);

            // arReceipt.addArInvoiceLine(arInvoiceLine);
            arInvoiceLine.setArReceipt(arReceipt);

            LocalArStandardMemoLine arStandardMemoLine = arStandardMemoLineHome.findBySmlName(mdetails.getIlSmlName(), companyCode);
            // arStandardMemoLine.addArInvoiceLine(arInvoiceLine);
            arInvoiceLine.setArStandardMemoLine(arStandardMemoLine);

            return arInvoiceLine;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void addArDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE, byte DR_RVRSD, LocalArReceipt arReceipt, Integer AD_BRNCH, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArMiscReceiptEntryControllerBean addArDrEntry");
        try {

            // get company

            Debug.print("COA_CODE:2 " + COA_CODE);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, AD_BRNCH, companyCode);

            // create distribution record

            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, DR_RVRSD, companyCode);

            // arReceipt.addArDistributionRecord(arDistributionRecord);
            arDistributionRecord.setArReceipt(arReceipt);
            // glChartOfAccount.addArDistributionRecord(arDistributionRecord);

            System.out.print(glChartOfAccount.toString());

            arDistributionRecord.setGlChartOfAccount(glChartOfAccount);

        } catch (FinderException ex) {

            throw new GlobalBranchAccountNumberInvalidException(ex.getMessage());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private Integer getArGlCoaRevenueAccount(LocalArInvoiceLine arInvoiceLine, Integer AD_BRNCH, Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean getArGlCoaRevenueAccount");
        // generate revenue account
        try {

            StringBuilder GL_COA_ACCNT = new StringBuilder();

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGenField genField = adCompany.getGenField();

            String FL_SGMNT_SPRTR = String.valueOf(genField.getFlSegmentSeparator());

            LocalAdBranchStandardMemoLine adBranchStandardMemoLine = null;

            try {

                adBranchStandardMemoLine = adBranchStandardMemoLineHome.findBSMLBySMLCodeAndBrCode(arInvoiceLine.getArStandardMemoLine().getSmlCode(), AD_BRNCH, companyCode);

            } catch (FinderException ex) {

            }

            Collection arAutoAccountingSegments = arAutoAccountingSegmentHome.findByAaAccountType("REVENUE", companyCode);

            for (Object autoAccountingSegment : arAutoAccountingSegments) {

                LocalArAutoAccountingSegment arAutoAccountingSegment = (LocalArAutoAccountingSegment) autoAccountingSegment;

                LocalGlChartOfAccount glChartOfAccount = null;

                if (arAutoAccountingSegment.getAasClassType().equals("AR CUSTOMER")) {

                    glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(arInvoiceLine.getArReceipt().getArCustomer().getCstGlCoaRevenueAccount());

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

                    if (adBranchStandardMemoLine != null) {

                        try {

                            glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchStandardMemoLine.getBsmlGlAccount());

                        } catch (FinderException ex) {

                        }

                    } else {

                        glChartOfAccount = arInvoiceLine.getArStandardMemoLine().getGlChartOfAccount();
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
                }
            }

            GL_COA_ACCNT = new StringBuilder(GL_COA_ACCNT.substring(1));

            try {

                LocalGlChartOfAccount glGeneratedChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(GL_COA_ACCNT.toString(), companyCode);

                return glGeneratedChartOfAccount.getCoaCode();

            } catch (FinderException ex) {

                if (adBranchStandardMemoLine != null) {

                    LocalGlChartOfAccount glChartOfAccount = null;

                    try {

                        glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchStandardMemoLine.getBsmlGlAccount());

                    } catch (FinderException e) {

                    }

                    return glChartOfAccount.getCoaCode();

                } else {

                    return arInvoiceLine.getArStandardMemoLine().getGlChartOfAccount().getCoaCode();
                }
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean convertForeignToFunctionalCurrency");
        LocalAdCompany adCompany = null;
        // get company and extended precision
        try {

            adCompany = adCompanyHome.findByPrimaryKey(companyCode);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        // Convert to functional currency if necessary
        if (CONVERSION_RATE != 1 && CONVERSION_RATE != 0) {

            AMOUNT = AMOUNT / CONVERSION_RATE;
        }
        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    private void executeArRctPost(Integer RCT_CODE, String USR_NM, Integer AD_BRNCH, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException {

        Debug.print("ArMiscReceiptEntryControllerBean executeArRctPost");
        LocalArReceipt arReceipt = null;
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            // validate if receipt is already deleted

            try {

                arReceipt = arReceiptHome.findByPrimaryKey(RCT_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if receipt is already posted

            if (arReceipt.getRctVoid() == EJBCommon.FALSE && arReceipt.getRctPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();

                // validate if receipt void is already posted

            } else if (arReceipt.getRctVoid() == EJBCommon.TRUE && arReceipt.getRctVoidPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyVoidPostedException();
            }

            if (arReceipt.getRctVoid() == EJBCommon.FALSE && arReceipt.getRctType().equals("MISC") && !arReceipt.getArInvoiceLineItems().isEmpty()) {

                // regenerate inventory dr

                this.regenerateInventoryDr(arReceipt, AD_BRNCH, companyCode);
            }

            // post receipt

            if (arReceipt.getRctVoid() == EJBCommon.FALSE && arReceipt.getRctPosted() == EJBCommon.FALSE) {

                if (arReceipt.getRctType().equals("MISC") && !arReceipt.getArInvoiceLineItems().isEmpty()) {

                    for (Object o : arReceipt.getArInvoiceLineItems()) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) o;

                        String II_NM = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName();
                        String LOC_NM = arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName();

                        double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                        LocalInvCosting invCosting = null;

                        try {

                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arReceipt.getRctDate(), II_NM, LOC_NM, AD_BRNCH, companyCode);

                        } catch (FinderException ex) {

                        }

                        double COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                        if (invCosting == null) {

                            this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), QTY_SLD, COST * QTY_SLD, -QTY_SLD, -COST * QTY_SLD, 0d, null, AD_BRNCH, companyCode);
                        } else {

                            switch (invCosting.getInvItemLocation().getInvItem().getIiCostMethod()) {
                                case "Average":

                                    double avgCost = invCosting.getCstRemainingQuantity() <= 0 ? COST : Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                                    this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), QTY_SLD, avgCost * QTY_SLD, invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (avgCost * QTY_SLD), 0d, null, AD_BRNCH, companyCode);

                                    break;
                                case "FIFO":

                                    double fifoCost = invCosting.getCstRemainingQuantity() == 0 ? COST : this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), QTY_SLD, arInvoiceLineItem.getIliUnitPrice(), true, AD_BRNCH, companyCode);

                                    this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), QTY_SLD, fifoCost * QTY_SLD, invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (fifoCost * QTY_SLD), 0d, null, AD_BRNCH, companyCode);

                                    break;
                                case "Standard":

                                    double standardCost = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();

                                    this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), QTY_SLD, standardCost * QTY_SLD, invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (standardCost * QTY_SLD), 0d, null, AD_BRNCH, companyCode);
                                    break;
                            }
                        }
                    }
                }

                // increase bank balance CASH
                if (arReceipt.getRctAmountCash() > 0) {

                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccount().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCash(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCash());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCash()=" + arReceipt.getRctAmountCash());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (arReceipt.getRctAmountCash()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCash());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // increase bank balance CARD 1
                if (arReceipt.getRctAmountCard1() > 0) {

                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccountCard1().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard1().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard1(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard1());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCard1()=" + arReceipt.getRctAmountCard1());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (arReceipt.getRctAmountCard1()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard1().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard1());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // increase bank balance CARD 2
                if (arReceipt.getRctAmountCard2() > 0) {
                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccountCard2().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard2().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard2(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard2());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCard2()=" + arReceipt.getRctAmountCard2());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (arReceipt.getRctAmountCard2()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard2().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard2());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // increase bank balance CARD 3
                if (arReceipt.getRctAmountCard3() > 0) {
                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccountCard3().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard3().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard3(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard3());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCard3()=" + arReceipt.getRctAmountCard3());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (arReceipt.getRctAmountCard3()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard3().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard3());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }
                // increase bank balance CHEQUE
                if (arReceipt.getRctAmountCheque() > 0) {
                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccount().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCheque(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCheque());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCheque()=" + arReceipt.getRctAmountCheque());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (arReceipt.getRctAmountCheque()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCheque());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // set receipt post status

                arReceipt.setRctPosted(EJBCommon.TRUE);
                arReceipt.setRctPostedBy(USR_NM);
                arReceipt.setRctDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            } else if (arReceipt.getRctVoid() == EJBCommon.TRUE && arReceipt.getRctVoidPosted() == EJBCommon.FALSE) { // void
                // receipt
                // VOIDING MISC RECEIPT
                if (arReceipt.getRctType().equals("MISC") && !arReceipt.getArInvoiceLineItems().isEmpty()) {

                    for (Object o : arReceipt.getArInvoiceLineItems()) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) o;

                        String II_NM = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName();
                        String LOC_NM = arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName();

                        double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                        LocalInvCosting invCosting = null;

                        try {

                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arReceipt.getRctDate(), II_NM, LOC_NM, AD_BRNCH, companyCode);

                        } catch (FinderException ex) {

                        }

                        double COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                        if (invCosting == null) {

                            this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), -QTY_SLD, -COST * QTY_SLD, QTY_SLD, COST * QTY_SLD, 0d, null, AD_BRNCH, companyCode);

                        } else {

                            switch (invCosting.getInvItemLocation().getInvItem().getIiCostMethod()) {
                                case "Average":

                                    double avgCost = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                                    if (invCosting.getCstRemainingQuantity() <= 0) {
                                        avgCost = arInvoiceLineItem.getIliUnitPrice();
                                    }

                                    this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), -QTY_SLD, -avgCost * QTY_SLD, invCosting.getCstRemainingQuantity() + QTY_SLD, invCosting.getCstRemainingValue() + (avgCost * QTY_SLD), 0d, USR_NM, AD_BRNCH, companyCode);

                                    break;
                                case "FIFO":

                                    double fifoCost = this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), QTY_SLD, arInvoiceLineItem.getIliUnitPrice(), true, AD_BRNCH, companyCode);

                                    this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), -QTY_SLD, -fifoCost * QTY_SLD, invCosting.getCstRemainingQuantity() + QTY_SLD, invCosting.getCstRemainingValue() + (fifoCost * QTY_SLD), 0d, USR_NM, AD_BRNCH, companyCode);

                                    break;
                                case "Standard":

                                    double standardCost = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();

                                    this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), -QTY_SLD, -standardCost * QTY_SLD, invCosting.getCstRemainingQuantity() + QTY_SLD, invCosting.getCstRemainingValue() + (standardCost * QTY_SLD), 0d, USR_NM, AD_BRNCH, companyCode);
                                    break;
                            }
                        }
                    }
                }

                // decrease bank balance cash

                if (arReceipt.getRctAmountCash() > 0) {

                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccount().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCash(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCash());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCash()=" + arReceipt.getRctAmountCash());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (-arReceipt.getRctAmountCash()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCash());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // decrease bank balance CARD 1
                if (arReceipt.getRctAmountCard1() > 0) {

                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccountCard1().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard1().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard1(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard1());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCard1()=" + arReceipt.getRctAmountCard1());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (-arReceipt.getRctAmountCard1()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard1().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard1());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // decrease bank balance CARD 2
                if (arReceipt.getRctAmountCard2() > 0) {
                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccountCard2().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard2().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard2(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard2());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCard2()=" + arReceipt.getRctAmountCard2());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (-arReceipt.getRctAmountCard2()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard2().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard2());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // decrease bank balance CARD 3
                if (arReceipt.getRctAmountCard3() > 0) {
                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccountCard3().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard3().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard3(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard3());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCard3()=" + arReceipt.getRctAmountCard3());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (-arReceipt.getRctAmountCard3()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard3().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard3());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }
                // decrease bank balance CHEQUE
                if (arReceipt.getRctAmountCheque() > 0) {
                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccount().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCheque(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCheque());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCheque()=" + arReceipt.getRctAmountCheque());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (-arReceipt.getRctAmountCheque()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCheque());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // set receipt post status

                arReceipt.setRctVoidPosted(EJBCommon.TRUE);
                arReceipt.setRctPostedBy(USR_NM);
                arReceipt.setRctDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
            }

            // post to gl if necessary

            if (adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                // validate if date has no period and period is closed

                LocalGlSetOfBook glJournalSetOfBook = null;

                try {

                    glJournalSetOfBook = glSetOfBookHome.findByDate(arReceipt.getRctDate(), companyCode);

                } catch (FinderException ex) {

                    throw new GlJREffectiveDateNoPeriodExistException();
                }

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), arReceipt.getRctDate(), companyCode);

                if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P') {

                    throw new GlJREffectiveDatePeriodClosedException();
                }

                // check if invoice is balance if not check suspense posting

                LocalGlJournalLine glOffsetJournalLine = null;

                Collection arDistributionRecords = null;

                if (arReceipt.getRctVoid() == EJBCommon.FALSE) {

                    arDistributionRecords = arDistributionRecordHome.findImportableDrByDrReversedAndRctCode(EJBCommon.FALSE, arReceipt.getRctCode(), companyCode);

                } else {

                    arDistributionRecords = arDistributionRecordHome.findImportableDrByDrReversedAndRctCode(EJBCommon.TRUE, arReceipt.getRctCode(), companyCode);
                }

                Iterator j = arDistributionRecords.iterator();

                double TOTAL_DEBIT = 0d;
                double TOTAL_CREDIT = 0d;

                while (j.hasNext()) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) j.next();
                    if (arDistributionRecord.getDrClass().equals("COGS") || arDistributionRecord.getDrClass().equals("INVENTORY")) {
                        continue;
                    }
                    double DR_AMNT = 0d;

                    if (arDistributionRecord.getArAppliedInvoice() != null) {

                        LocalArInvoice arInvoice = arDistributionRecord.getArAppliedInvoice().getArInvoicePaymentSchedule().getArInvoice();

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), companyCode);

                    } else {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arDistributionRecord.getDrAmount(), companyCode);
                    }

                    if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                        TOTAL_DEBIT += DR_AMNT;

                    } else {

                        TOTAL_CREDIT += DR_AMNT;
                    }
                }

                TOTAL_DEBIT = EJBCommon.roundIt(TOTAL_DEBIT, adCompany.getGlFunctionalCurrency().getFcPrecision());
                TOTAL_CREDIT = EJBCommon.roundIt(TOTAL_CREDIT, adCompany.getGlFunctionalCurrency().getFcPrecision());

                Debug.print("TOTAL_DEBIT=" + TOTAL_DEBIT);
                Debug.print("TOTAL_CREDIT=" + TOTAL_CREDIT);
                if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.TRUE && TOTAL_DEBIT != TOTAL_CREDIT) {

                    LocalGlSuspenseAccount glSuspenseAccount = null;

                    try {

                        glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("ACCOUNTS RECEIVABLES", "SALES RECEIPTS", companyCode);

                    } catch (FinderException ex) {

                        throw new GlobalJournalNotBalanceException();
                    }

                    if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                        glOffsetJournalLine = glJournalLineHome.create((short) (arDistributionRecords.size() + 1), EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);

                    } else {

                        glOffsetJournalLine = glJournalLineHome.create((short) (arDistributionRecords.size() + 1), EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
                    }

                    LocalGlChartOfAccount glChartOfAccount = glSuspenseAccount.getGlChartOfAccount();
                    // glChartOfAccount.addGlJournalLine(glOffsetJournalLine);
                    glOffsetJournalLine.setGlChartOfAccount(glChartOfAccount);

                } else if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE && TOTAL_DEBIT != TOTAL_CREDIT) {

                    Debug.print("GlobalJournalNotBalanceException()");
                    throw new GlobalJournalNotBalanceException();
                }

                // create journal batch if necessary

                LocalGlJournalBatch glJournalBatch = null;
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");

                try {

                    if (adPreference.getPrfEnableArReceiptBatch() == EJBCommon.TRUE) {

                        glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " " + arReceipt.getArReceiptBatch().getRbName(), AD_BRNCH, companyCode);

                    } else {

                        glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " SALES RECEIPTS", AD_BRNCH, companyCode);
                    }

                } catch (FinderException ex) {
                }

                if (adPreference.getPrfEnableGlJournalBatch() == EJBCommon.TRUE && glJournalBatch == null) {

                    if (adPreference.getPrfEnableArReceiptBatch() == EJBCommon.TRUE) {

                        glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " " + arReceipt.getArReceiptBatch().getRbName(), "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, AD_BRNCH, companyCode);

                    } else {

                        glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " SALES RECEIPTS", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, AD_BRNCH, companyCode);
                    }
                }

                // create journal entry
                String customerName = arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName();

                LocalGlJournal glJournal = glJournalHome.create(arReceipt.getRctReferenceNumber(), arReceipt.getRctDescription(), arReceipt.getRctDate(), 0.0d, null, arReceipt.getRctNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM, EJBCommon.getGcCurrentDateWoTime().getTime(), arReceipt.getArCustomer().getCstTin(), customerName, EJBCommon.FALSE, null, AD_BRNCH, companyCode);

                LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("ACCOUNTS RECEIVABLES", companyCode);
                glJournal.setGlJournalSource(glJournalSource);

                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

                LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("SALES RECEIPTS", companyCode);
                glJournal.setGlJournalCategory(glJournalCategory);

                if (glJournalBatch != null) {

                    glJournal.setGlJournalBatch(glJournalBatch);
                }

                // create journal lines

                j = arDistributionRecords.iterator();

                while (j.hasNext()) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) j.next();

                    double DR_AMNT = 0d;

                    LocalArInvoice arInvoice = null;

                    if (arDistributionRecord.getArAppliedInvoice() != null) {

                        arInvoice = arDistributionRecord.getArAppliedInvoice().getArInvoicePaymentSchedule().getArInvoice();

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), companyCode);

                    } else {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arDistributionRecord.getDrAmount(), companyCode);
                    }

                    LocalGlJournalLine glJournalLine = glJournalLineHome.create(arDistributionRecord.getDrLine(), arDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

                    // arDistributionRecord.getGlChartOfAccount().addGlJournalLine(glJournalLine);
                    glJournalLine.setGlChartOfAccount(arDistributionRecord.getGlChartOfAccount());

                    // glJournal.addGlJournalLine(glJournalLine);
                    glJournalLine.setGlJournal(glJournal);

                    arDistributionRecord.setDrImported(EJBCommon.TRUE);

                    // for FOREX revaluation

                    int FC_CODE = arDistributionRecord.getArAppliedInvoice() != null ? arInvoice.getGlFunctionalCurrency().getFcCode() : arReceipt.getGlFunctionalCurrency().getFcCode();

                    if ((FC_CODE != adCompany.getGlFunctionalCurrency().getFcCode()) && glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null && (FC_CODE == glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode())) {

                        double CONVERSION_RATE = arDistributionRecord.getArAppliedInvoice() != null ? arInvoice.getInvConversionRate() : arReceipt.getRctConversionRate();

                        Date DATE = arDistributionRecord.getArAppliedInvoice() != null ? arInvoice.getInvConversionDate() : arReceipt.getRctConversionDate();

                        if (DATE != null && (CONVERSION_RATE == 0 || CONVERSION_RATE == 1)) {

                            CONVERSION_RATE = this.getFrRateByFrNameAndFrDate(glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), companyCode);

                        } else if (CONVERSION_RATE == 0) {

                            CONVERSION_RATE = 1;
                        }

                        Collection glForexLedgers = null;

                        DATE = arDistributionRecord.getArAppliedInvoice() != null ? arInvoice.getInvDate() : arReceipt.getRctDate();

                        try {

                            glForexLedgers = glForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(DATE, glJournalLine.getGlChartOfAccount().getCoaCode(), companyCode);

                        } catch (FinderException ex) {

                        }

                        LocalGlForexLedger glForexLedger = (glForexLedgers.isEmpty() || glForexLedgers == null) ? null : (LocalGlForexLedger) glForexLedgers.iterator().next();

                        int FRL_LN = (glForexLedger != null && glForexLedger.getFrlDate().compareTo(DATE) == 0) ? glForexLedger.getFrlLine() + 1 : 1;

                        // compute balance
                        double COA_FRX_BLNC = glForexLedger == null ? 0 : glForexLedger.getFrlBalance();
                        double FRL_AMNT = arDistributionRecord.getDrAmount();

                        if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET")) {
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                        } else {
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);
                        }

                        COA_FRX_BLNC = COA_FRX_BLNC + FRL_AMNT;

                        glForexLedger = glForexLedgerHome.create(DATE, FRL_LN, "OR", FRL_AMNT, CONVERSION_RATE, COA_FRX_BLNC, 0d, companyCode);

                        // glJournalLine.getGlChartOfAccount().addGlForexLedger(glForexLedger);
                        glForexLedger.setGlChartOfAccount(glJournalLine.getGlChartOfAccount());

                        // propagate balances
                        try {

                            glForexLedgers = glForexLedgerHome.findByGreaterThanFrlDateAndCoaCode(glForexLedger.getFrlDate(), glForexLedger.getGlChartOfAccount().getCoaCode(), glForexLedger.getFrlAdCompany());

                        } catch (FinderException ex) {

                        }

                        for (Object forexLedger : glForexLedgers) {

                            glForexLedger = (LocalGlForexLedger) forexLedger;
                            FRL_AMNT = arDistributionRecord.getDrAmount();

                            if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET")) {
                                FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                            } else {
                                FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);
                            }

                            glForexLedger.setFrlBalance(glForexLedger.getFrlBalance() + FRL_AMNT);
                        }
                    }
                }

                if (glOffsetJournalLine != null) {

                    // glJournal.addGlJournalLine(glOffsetJournalLine);
                    glOffsetJournalLine.setGlJournal(glJournal);
                }

                // post journal to gl
                Collection glJournalLines = glJournalLineHome.findByJrCode(glJournal.getJrCode(), companyCode);
                for (Object journalLine : glJournalLines) {

                    LocalGlJournalLine glJournalLine = (LocalGlJournalLine) journalLine;

                    // post current to current acv

                    this.postToGl(glAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), true, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                    // post to subsequent acvs (propagate)

                    Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), glAccountingCalendarValue.getAcvPeriodNumber(), companyCode);

                    for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {

                        LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;

                        this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                    }

                    // post to subsequent years if necessary

                    Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode);

                    if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                        adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                        LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome.findByCoaAccountNumber(adCompany.getCmpRetainedEarnings(), companyCode);

                        for (Object subsequentSetOfBook : glSubsequentSetOfBooks) {

                            LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) subsequentSetOfBook;

                            String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                            // post to subsequent acvs of subsequent set of book(propagate)

                            Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                            for (Object accountingCalendarValue : glAccountingCalendarValues) {

                                LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                                if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY") || ACCOUNT_TYPE.equals("OWNERS EQUITY")) {

                                    this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                                } else { // revenue & expense

                                    this.postToGl(glSubsequentAccountingCalendarValue, glRetainedEarningsAccount, false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                                }
                            }

                            if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) {
                                break;
                            }
                        }
                    }
                }

                if (arReceipt.getArCustomer().getApSupplier() != null) {

                    // Post Investors Account balance
                    byte scLedger = arReceipt.getArCustomer().getApSupplier().getApSupplierClass().getScLedger();

                    // post current to current acv
                    if (scLedger == EJBCommon.TRUE) {

                        this.postToGlInvestor(glAccountingCalendarValue, arReceipt.getArCustomer().getApSupplier(), true, arReceipt.getRctInvtrBeginningBalance(), (byte) 0, arReceipt.getRctAmount(), companyCode);

                        // post to subsequent acvs (propagate)

                        Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), glAccountingCalendarValue.getAcvPeriodNumber(), companyCode);

                        for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {

                            LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;

                            this.postToGlInvestor(glSubsequentAccountingCalendarValue, arReceipt.getArCustomer().getApSupplier(), false, (byte) 0, (byte) 0, arReceipt.getRctAmount(), companyCode);
                        }

                        // post to subsequent years if necessary

                        Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode);

                        if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                            adCompany = adCompanyHome.findByPrimaryKey(companyCode);

                            for (Object subsequentSetOfBook : glSubsequentSetOfBooks) {

                                LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) subsequentSetOfBook;

                                // post to subsequent acvs of subsequent set of book(propagate)

                                Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                                for (Object accountingCalendarValue : glAccountingCalendarValues) {

                                    LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                                    this.postToGlInvestor(glSubsequentAccountingCalendarValue, arReceipt.getArCustomer().getApSupplier(), false, arReceipt.getRctInvtrBeginningBalance(), (byte) 0, arReceipt.getRctAmount(), companyCode);
                                }

                                if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }

        } catch (GlJREffectiveDateNoPeriodExistException | GlobalExpiryDateNotFoundException |
                 GlobalTransactionAlreadyVoidPostedException | GlobalTransactionAlreadyPostedException |
                 GlobalRecordAlreadyDeletedException | GlobalJournalNotBalanceException |
                 GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (AdPRFCoaGlVarianceAccountNotFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double getInvFifoCost(Date CST_DT, Integer IL_CODE, double CST_QTY, double CST_COST, boolean isAdjustFifo, Integer AD_BRNCH, Integer companyCode) {

        try {

            Collection invFifoCostings = invCostingHome.findFifoRemainingQuantityByLessThanOrEqualCstDateAndIlCodeAndBrCode(CST_DT, IL_CODE, AD_BRNCH, companyCode);

            if (invFifoCostings.size() > 0) {

                Iterator x = invFifoCostings.iterator();

                if (isAdjustFifo) {

                    // executed during POST transaction

                    double totalCost = 0d;
                    double cost;

                    if (CST_QTY < 0) {

                        // for negative quantities
                        double neededQty = -(CST_QTY);

                        while (x.hasNext() && neededQty != 0) {

                            LocalInvCosting invFifoCosting = (LocalInvCosting) x.next();

                            if (invFifoCosting.getApPurchaseOrderLine() != null || invFifoCosting.getApVoucherLineItem() != null) {
                                cost = invFifoCosting.getCstItemCost() / invFifoCosting.getCstQuantityReceived();
                            } else if (invFifoCosting.getArInvoiceLineItem() != null) {
                                cost = invFifoCosting.getCstCostOfSales() / invFifoCosting.getCstQuantitySold();
                            } else {
                                cost = invFifoCosting.getCstAdjustCost() / invFifoCosting.getCstAdjustQuantity();
                            }

                            if (neededQty <= invFifoCosting.getCstRemainingLifoQuantity()) {

                                invFifoCosting.setCstRemainingLifoQuantity(invFifoCosting.getCstRemainingLifoQuantity() - neededQty);
                                totalCost += (neededQty * cost);
                                neededQty = 0d;
                            } else {

                                neededQty -= invFifoCosting.getCstRemainingLifoQuantity();
                                totalCost += (invFifoCosting.getCstRemainingLifoQuantity() * cost);
                                invFifoCosting.setCstRemainingLifoQuantity(0);
                            }
                        }

                        // if needed qty is not yet satisfied but no more quantities to fetch, get the
                        // default
                        // cost
                        if (neededQty != 0) {

                            LocalInvItemLocation invItemLocation = invItemLocationHome.findByPrimaryKey(IL_CODE);
                            totalCost += (neededQty * invItemLocation.getInvItem().getIiUnitCost());
                        }

                        cost = totalCost / -CST_QTY;
                    } else {

                        // for positive quantities
                        cost = CST_COST;
                    }
                    return cost;
                } else {

                    // executed during ENTRY transaction

                    LocalInvCosting invFifoCosting = (LocalInvCosting) x.next();

                    if (invFifoCosting.getApPurchaseOrderLine() != null || invFifoCosting.getApVoucherLineItem() != null) {
                        return EJBCommon.roundIt(invFifoCosting.getCstItemCost() / invFifoCosting.getCstQuantityReceived(), this.getGlFcPrecisionUnit(companyCode));
                    } else if (invFifoCosting.getArInvoiceLineItem() != null) {
                        return EJBCommon.roundIt(invFifoCosting.getCstCostOfSales() / invFifoCosting.getCstQuantitySold(), this.getGlFcPrecisionUnit(companyCode));
                    } else {
                        return EJBCommon.roundIt(invFifoCosting.getCstAdjustCost() / invFifoCosting.getCstAdjustQuantity(), this.getGlFcPrecisionUnit(companyCode));
                    }
                }
            } else {

                // most applicable in 1st entries of data
                LocalInvItemLocation invItemLocation = invItemLocationHome.findByPrimaryKey(IL_CODE);
                return invItemLocation.getInvItem().getIiUnitCost();
            }

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double JL_AMNT, Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean postToGl");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccountBalance glChartOfAccountBalance = glChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(glAccountingCalendarValue.getAcvCode(), glChartOfAccount.getCoaCode(), companyCode);

            String ACCOUNT_TYPE = glChartOfAccount.getCoaAccountType();
            short FC_EXTNDD_PRCSN = adCompany.getGlFunctionalCurrency().getFcPrecision();

            if (((ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("EXPENSE")) && isDebit == EJBCommon.TRUE) || (!ACCOUNT_TYPE.equals("ASSET") && !ACCOUNT_TYPE.equals("EXPENSE") && isDebit == EJBCommon.FALSE)) {

                glChartOfAccountBalance.setCoabEndingBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabEndingBalance() + JL_AMNT, FC_EXTNDD_PRCSN));

                if (!isCurrentAcv) {

                    glChartOfAccountBalance.setCoabBeginningBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabBeginningBalance() + JL_AMNT, FC_EXTNDD_PRCSN));
                }

            } else {

                glChartOfAccountBalance.setCoabEndingBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabEndingBalance() - JL_AMNT, FC_EXTNDD_PRCSN));

                if (!isCurrentAcv) {

                    glChartOfAccountBalance.setCoabBeginningBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabBeginningBalance() - JL_AMNT, FC_EXTNDD_PRCSN));
                }
            }

            if (isCurrentAcv) {

                if (isDebit == EJBCommon.TRUE) {

                    glChartOfAccountBalance.setCoabTotalDebit(EJBCommon.roundIt(glChartOfAccountBalance.getCoabTotalDebit() + JL_AMNT, FC_EXTNDD_PRCSN));

                } else {

                    glChartOfAccountBalance.setCoabTotalCredit(EJBCommon.roundIt(glChartOfAccountBalance.getCoabTotalCredit() + JL_AMNT, FC_EXTNDD_PRCSN));
                }
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToGlInvestor(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalApSupplier apSupplier, boolean isCurrentAcv, byte isBeginningBalance, byte isDebit, double JL_AMNT, Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean postToGlInvestor");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlInvestorAccountBalance glInvestorAccountBalance = glInvestorAccountBalanceHome.findByAcvCodeAndSplCode(glAccountingCalendarValue.getAcvCode(), apSupplier.getSplCode(), companyCode);

            short FC_EXTNDD_PRCSN = adCompany.getGlFunctionalCurrency().getFcPrecision();

            glInvestorAccountBalance.setIrabEndingBalance(EJBCommon.roundIt(glInvestorAccountBalance.getIrabEndingBalance() + JL_AMNT, FC_EXTNDD_PRCSN));

            if (!isCurrentAcv) {

                glInvestorAccountBalance.setIrabBeginningBalance(EJBCommon.roundIt(glInvestorAccountBalance.getIrabBeginningBalance() + JL_AMNT, FC_EXTNDD_PRCSN));
            }

            if (isCurrentAcv) {

                glInvestorAccountBalance.setIrabTotalCredit(EJBCommon.roundIt(glInvestorAccountBalance.getIrabTotalCredit() + JL_AMNT, FC_EXTNDD_PRCSN));
            }

            if (isBeginningBalance != 0) {
                glInvestorAccountBalance.setIrabBonus(EJBCommon.TRUE);
                glInvestorAccountBalance.setIrabInterest(EJBCommon.TRUE);
            } else {
                glInvestorAccountBalance.setIrabBonus(EJBCommon.FALSE);
                glInvestorAccountBalance.setIrabInterest(EJBCommon.FALSE);
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, double ADJST_QTY, Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean convertByUomFromAndItemAndQuantity");
        try {
            Debug.print("ArMiscReceiptEntryControllerBean convertByUomFromAndItemAndQuantity A");
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), companyCode);
            Debug.print("ArMiscReceiptEntryControllerBean convertByUomFromAndItemAndQuantity B");
            return EJBCommon.roundIt(ADJST_QTY * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), adPreference.getPrfInvQuantityPrecisionUnit());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertByUomAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, double ADJST_QTY, Integer companyCode) {

        Debug.print("ArInvoicePostControllerBean convertByUomAndQuantity");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), companyCode);

            return EJBCommon.roundIt(ADJST_QTY * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), adPreference.getPrfInvQuantityPrecisionUnit());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalArInvoiceLineItem addArIliEntry(ArModInvoiceLineItemDetails mdetails, LocalArReceipt arReceipt, LocalInvItemLocation invItemLocation, Integer companyCode) throws GlobalMiscInfoIsRequiredException {

        Debug.print("ArMiscReceiptEntryControllerBean addArIliEntry");
        try {

            short precisionUnit = this.getGlFcPrecisionUnit(companyCode);

            double ILI_AMNT = 0d;
            double ILI_TAX_AMNT = 0d;

            // calculate net amount

            LocalArTaxCode arTaxCode = arReceipt.getArTaxCode();

            if (arTaxCode.getTcType().equals("INCLUSIVE") && mdetails.getIliTax() == EJBCommon.TRUE) {

                ILI_AMNT = EJBCommon.roundIt(mdetails.getIliAmount() / (1 + (arTaxCode.getTcRate() / 100)), precisionUnit);

            } else {

                // tax exclusive, none, zero rated or exempt

                ILI_AMNT = mdetails.getIliAmount();
            }

            // calculate tax

            if (!arTaxCode.getTcType().equals("NONE") && !arTaxCode.getTcType().equals("EXEMPT")) {

                if (mdetails.getIliTax() == EJBCommon.TRUE) {
                    if (arTaxCode.getTcType().equals("INCLUSIVE")) {

                        ILI_TAX_AMNT = EJBCommon.roundIt(mdetails.getIliAmount() - ILI_AMNT, precisionUnit);

                    } else if (arTaxCode.getTcType().equals("EXCLUSIVE")) {

                        ILI_TAX_AMNT = EJBCommon.roundIt(mdetails.getIliAmount() * arTaxCode.getTcRate() / 100, precisionUnit);

                    } else {

                        // tax none zero-rated or exempt

                    }
                }
            }
            LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.IliLine(mdetails.getIliLine()).IliQuantity(mdetails.getIliQuantity()).IliUnitPrice(mdetails.getIliUnitPrice()).IliAmount(ILI_AMNT).IliTaxAmount(ILI_TAX_AMNT).IliDiscount1(mdetails.getIliDiscount1()).IliDiscount2(mdetails.getIliDiscount2()).IliDiscount3(mdetails.getIliDiscount3()).IliDiscount4(mdetails.getIliDiscount4()).IliTotalDiscount(mdetails.getIliTotalDiscount()).IliTax(mdetails.getIliTax()).IliAdCompany(companyCode).buildInvoiceLineItem();

            // arReceipt.addArInvoiceLineItem(arInvoiceLineItem);
            arInvoiceLineItem.setArReceipt(arReceipt);
            // invItemLocation.addArInvoiceLineItem(arInvoiceLineItem);
            arInvoiceLineItem.setInvItemLocation(invItemLocation);

            LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(mdetails.getIliUomName(), companyCode);
            // invUnitOfMeasure.addArInvoiceLineItem(arInvoiceLineItem);
            arInvoiceLineItem.setInvUnitOfMeasure(invUnitOfMeasure);

            if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiTraceMisc() == 1) {

                this.createInvTagList(arInvoiceLineItem, mdetails.getiIliTagList(), companyCode);
            }

            // validate misc

            return arInvoiceLineItem;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public int checkExpiryDates(String misc) throws Exception {

        String separator = "$";

        // Remove first $ character
        misc = misc.substring(1);
        // Debug.print("misc: " + misc);
        // Counter
        int start = 0;
        int nextIndex = misc.indexOf(separator, start);
        int length = nextIndex - start;
        int numberExpry = 0;
        StringBuilder miscList = new StringBuilder();
        String miscList2 = "";
        String g = "";
        try {
            while (g != "fin") {
                // Date
                start = nextIndex + 1;
                nextIndex = misc.indexOf(separator, start);
                length = nextIndex - start;
                g = misc.substring(start, start + length);
                if (g.length() != 0) {
                    if (g != null || g != "" || g != "null") {
                        if (g.contains("null")) {
                            miscList2 = "Error";
                        } else {
                            miscList.append("$").append(g);
                            numberExpry++;
                        }
                    } else {
                        miscList2 = "Error";
                    }

                } else {
                    miscList2 = "Error";
                }
            }
        } catch (Exception e) {

        }

        if (miscList2 == "") {
            miscList.append("$");
        } else {
            miscList = new StringBuilder(miscList2);
        }

        return (numberExpry);
    }

    public String checkExpiryDates(String misc, double qty, String reverse) throws Exception {
        // ActionErrors errors = new ActionErrors();
        Debug.print("ApReceivingItemControllerBean getExpiryDates");
        Debug.print("misc: " + misc);

        String separator = "";
        if (reverse == "False") {
            separator = "$";
        } else {
            separator = " ";
        }

        // Remove first $ character
        misc = misc.substring(1);
        Debug.print("misc: " + misc);
        // Counter
        int start = 0;
        int nextIndex = misc.indexOf(separator, start);
        int length = nextIndex - start;

        StringBuilder miscList = new StringBuilder();
        String miscList2 = "";

        for (int x = 0; x < qty; x++) {

            // Date
            start = nextIndex + 1;
            nextIndex = misc.indexOf(separator, start);
            length = nextIndex - start;
            String g = misc.substring(start, start + length);
            Debug.print("g: " + g);
            Debug.print("g length: " + g.length());
            if (g.length() != 0) {
                if (g != null || g != "" || g != "null") {
                    if (g.contains("null")) {
                        miscList2 = "Error";
                    } else {
                        miscList.append("$").append(g);
                    }
                } else {
                    miscList2 = "Error";
                }

                Debug.print("miscList G: " + miscList);
            } else {
                miscList2 = "Error";
            }
        }
        Debug.print("miscList2 :" + miscList2);
        if (miscList2 == "") {
            miscList.append("$");
        } else {
            miscList = new StringBuilder(miscList2);
        }

        Debug.print("miscList :" + miscList);
        return (miscList.toString());
    }

    private void addArDrIliEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE, LocalArReceipt arReceipt, Integer AD_BRNCH, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArMiscReceiptEntryControllerBean addArDrIliEntry");
        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, AD_BRNCH, companyCode);

            // create distribution record

            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, EJBCommon.FALSE, companyCode);

            // arReceipt.addArDistributionRecord(arDistributionRecord);
            arDistributionRecord.setArReceipt(arReceipt);
            // glChartOfAccount.addArDistributionRecord(arDistributionRecord);
            arDistributionRecord.setGlChartOfAccount(glChartOfAccount);

        } catch (FinderException ex) {

            // throw new GlobalBranchAccountNumberInvalidException(ex.getMessage(),DR_LN);
            throw new GlobalBranchAccountNumberInvalidException("" + DR_LN);

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToInv(LocalArInvoiceLineItem arInvoiceLineItem, Date CST_DT, double CST_QTY_SLD, double CST_CST_OF_SLS, double CST_RMNNG_QTY, double CST_RMNNG_VL, double CST_VRNC_VL, String USR_NM, Integer AD_BRNCH, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException {

        Debug.print("ArMiscReceiptEntryControllerBean postToInv");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalInvItemLocation invItemLocation = arInvoiceLineItem.getInvItemLocation();
            int CST_LN_NMBR = 0;

            CST_QTY_SLD = EJBCommon.roundIt(CST_QTY_SLD, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_CST_OF_SLS = EJBCommon.roundIt(CST_CST_OF_SLS, adCompany.getGlFunctionalCurrency().getFcPrecision());
            CST_RMNNG_QTY = EJBCommon.roundIt(CST_RMNNG_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_RMNNG_VL = EJBCommon.roundIt(CST_RMNNG_VL, adCompany.getGlFunctionalCurrency().getFcPrecision());

            if (CST_QTY_SLD > 0) {

                invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - CST_QTY_SLD);
            }

            try {

                // generate line number
                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            } catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            // void subsequent cost variance adjustments
            Collection invAdjustmentLines = invAdjustmentLineHome.findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(CST_DT, invItemLocation.getIlCode(), AD_BRNCH, companyCode);
            Iterator i = invAdjustmentLines.iterator();

            while (i.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();
                this.voidInvAdjustment(invAdjustmentLine.getInvAdjustment(), AD_BRNCH, companyCode);
            }

            String prevExpiryDates = "";
            String miscListPrpgt = "";
            double qtyPrpgt = 0;
            try {
                LocalInvCosting prevCst = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndRemainingQuantityNotEqualToZeroAndIlCode(CST_DT, invItemLocation.getIlCode(), AD_BRNCH, companyCode);
                Debug.print("ArReceiptPostControllerBean postToInv B");
                prevExpiryDates = prevCst.getCstExpiryDate();
                qtyPrpgt = prevCst.getCstRemainingQuantity();

                if (prevExpiryDates == null) {
                    prevExpiryDates = "";
                }

            } catch (Exception ex) {
                Debug.print("prevExpiryDates CATCH: " + prevExpiryDates);
                prevExpiryDates = "";
            }

            // create costing
            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d, 0d, 0d, CST_QTY_SLD, CST_CST_OF_SLS, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, 0d, AD_BRNCH, companyCode);
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setArInvoiceLineItem(arInvoiceLineItem);

            invCosting.setCstQuantity(CST_QTY_SLD * -1);
            invCosting.setCstCost(CST_CST_OF_SLS * -1);

            // Get Latest Expiry Dates
            if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiTraceMisc() != 0) {
                if (prevExpiryDates != null && prevExpiryDates != "" && prevExpiryDates.length() != 0) {
                    if (arInvoiceLineItem.getIliMisc() != null && arInvoiceLineItem.getIliMisc() != "" && arInvoiceLineItem.getIliMisc().length() != 0) {
                        int qty2Prpgt = Integer.parseInt(this.getQuantityExpiryDates(arInvoiceLineItem.getIliMisc()));

                        String miscList2Prpgt = this.propagateExpiryDates(arInvoiceLineItem.getIliMisc(), qty2Prpgt, "False");
                        ArrayList miscList = this.expiryDates(arInvoiceLineItem.getIliMisc(), qty2Prpgt);
                        String propagateMiscPrpgt = "";
                        StringBuilder ret = new StringBuilder();
                        StringBuilder exp = new StringBuilder();
                        String Checker = "";

                        Iterator mi = miscList.iterator();

                        propagateMiscPrpgt = prevExpiryDates;
                        ret = new StringBuilder(propagateMiscPrpgt);
                        while (mi.hasNext()) {
                            String miscStr = (String) mi.next();

                            int qTest = checkExpiryDates(ret + "fin$");
                            ArrayList miscList2 = this.expiryDates("$" + ret, Double.parseDouble(Integer.toString(qTest)));

                            // ArrayList miscList2 = this.expiryDates("$" + ret, qtyPrpgt);
                            Iterator m2 = miscList2.iterator();
                            ret = new StringBuilder();
                            String ret2 = "false";
                            int a = 0;
                            while (m2.hasNext()) {
                                String miscStr2 = (String) m2.next();

                                if (ret2 == "1st") {
                                    ret2 = "false";
                                }
                                Debug.print("miscStr: " + miscStr);
                                Debug.print("miscStr2: " + miscStr2);

                                if (miscStr2.equals(miscStr)) {
                                    if (a == 0) {
                                        a = 1;
                                        ret2 = "1st";
                                        Checker = "true";
                                    } else {
                                        a = a + 1;
                                        ret2 = "true";
                                    }
                                }
                                Debug.print("Checker: " + Checker);
                                Debug.print("ret2: " + ret2);
                                if (!miscStr2.equals(miscStr) || a > 1) {
                                    if ((ret2 != "1st") && ((ret2 == "false") || (ret2 == "true"))) {
                                        if (miscStr2 != "") {
                                            miscStr2 = "$" + miscStr2;
                                            ret.append(miscStr2);
                                            Debug.print("ret " + ret);
                                            ret2 = "false";
                                        }
                                    }
                                }
                            }
                            if (ret.toString() != "") {
                                ret.append("$");
                            }
                            Debug.print("ret una: " + ret);
                            exp.append(ret);
                            qtyPrpgt = qtyPrpgt - 1;
                        }
                        Debug.print("ret fin " + ret);
                        Debug.print("exp fin " + exp);
                        // propagateMiscPrpgt = propagateMiscPrpgt.replace(" ", "$");
                        propagateMiscPrpgt = ret.toString();
                        if (Checker == "true") {
                            // invCosting.setCstExpiryDate(propagateMiscPrpgt);
                        } else {
                            Debug.print("Exp Not Found");
                            throw new GlobalExpiryDateNotFoundException(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                        }
                        invCosting.setCstExpiryDate(propagateMiscPrpgt);
                    } else {
                        invCosting.setCstExpiryDate(prevExpiryDates);
                    }

                } else {
                    if (arInvoiceLineItem.getIliMisc() != null && arInvoiceLineItem.getIliMisc() != "" && arInvoiceLineItem.getIliMisc().length() != 0) {
                        int initialQty = Integer.parseInt(this.getQuantityExpiryDates(arInvoiceLineItem.getIliMisc()));
                        String initialPrpgt = this.propagateExpiryDates(arInvoiceLineItem.getIliMisc(), initialQty, "False");

                        invCosting.setCstExpiryDate(initialPrpgt);
                    } else {
                        invCosting.setCstExpiryDate(prevExpiryDates);
                    }
                }
            }

            // if cost variance is not 0, generate cost variance for the transaction
            if (CST_VRNC_VL != 0) {

                this.generateCostVariance(invCosting.getInvItemLocation(), CST_VRNC_VL, "APMR" + arInvoiceLineItem.getArReceipt().getRctNumber(), arInvoiceLineItem.getArReceipt().getRctDescription(), arInvoiceLineItem.getArReceipt().getRctDate(), USR_NM, AD_BRNCH, companyCode);
            }

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
            String miscList = "";
            ArrayList miscList2 = null;
            i = invCostings.iterator();
            String propagateMisc = "";
            StringBuilder ret = new StringBuilder();
            while (i.hasNext()) {
                String Checker = "";
                String Checker2 = "";

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) i.next();

                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() - CST_QTY_SLD);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() - CST_CST_OF_SLS);

                if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiTraceMisc() != 0) {
                    if (arInvoiceLineItem.getIliMisc() != null && arInvoiceLineItem.getIliMisc() != "" && arInvoiceLineItem.getIliMisc().length() != 0) {
                        double qty = Double.parseDouble(this.getQuantityExpiryDates(arInvoiceLineItem.getIliMisc()));
                        // invPropagatedCosting.getInvAdjustmentLine().getAlMisc();
                        miscList = this.propagateExpiryDates(arInvoiceLineItem.getIliMisc(), qty, "False");
                        miscList2 = this.expiryDates(arInvoiceLineItem.getIliMisc(), qty);
                        Debug.print("arInvoiceLineItem.getIliMisc(): " + arInvoiceLineItem.getIliMisc());
                        Debug.print("getAlAdjustQuantity(): " + arInvoiceLineItem.getIliQuantity());

                        if (arInvoiceLineItem.getIliQuantity() < 0) {
                            Iterator mi = miscList2.iterator();

                            propagateMisc = invPropagatedCosting.getCstExpiryDate();
                            ret = new StringBuilder(invPropagatedCosting.getCstExpiryDate());
                            while (mi.hasNext()) {
                                String miscStr = (String) mi.next();

                                int qTest = checkExpiryDates(ret + "fin$");
                                ArrayList miscList3 = this.expiryDates("$" + ret, Double.parseDouble(Integer.toString(qTest)));

                                // ArrayList miscList3 = this.expiryDates("$" + ret, qtyPrpgt);
                                Debug.print("ret: " + ret);
                                Iterator m2 = miscList3.iterator();
                                ret = new StringBuilder();
                                String ret2 = "false";
                                int a = 0;
                                while (m2.hasNext()) {
                                    String miscStr2 = (String) m2.next();

                                    if (ret2 == "1st") {
                                        ret2 = "false";
                                    }
                                    Debug.print("2 miscStr: " + miscStr);
                                    Debug.print("2 miscStr2: " + miscStr2);
                                    if (miscStr2.equals(miscStr)) {
                                        if (a == 0) {
                                            a = 1;
                                            ret2 = "1st";
                                            Checker2 = "true";
                                        } else {
                                            a = a + 1;
                                            ret2 = "true";
                                        }
                                    }
                                    Debug.print("Checker: " + Checker2);
                                    if (!miscStr2.equals(miscStr) || a > 1) {
                                        if ((ret2 != "1st") || (ret2 == "false") || (ret2 == "true")) {
                                            if (miscStr2 != "") {
                                                miscStr2 = "$" + miscStr2;
                                                ret.append(miscStr2);
                                                ret2 = "false";
                                            }
                                        }
                                    }
                                }
                                if (Checker2 != "true") {
                                    throw new GlobalExpiryDateNotFoundException(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                                }

                                ret.append("$");
                                qtyPrpgt = qtyPrpgt - 1;
                            }
                        }
                    }

                    if (arInvoiceLineItem.getIliMisc() != null && arInvoiceLineItem.getIliMisc() != "" && arInvoiceLineItem.getIliMisc().length() != 0) {
                        if (prevExpiryDates != null && prevExpiryDates != "" && prevExpiryDates.length() != 0) {

                            Iterator mi = miscList2.iterator();

                            propagateMisc = invPropagatedCosting.getCstExpiryDate();
                            ret = new StringBuilder(propagateMisc);
                            while (mi.hasNext()) {
                                String miscStr = (String) mi.next();

                                int qTest = checkExpiryDates(ret + "fin$");
                                ArrayList miscList3 = this.expiryDates("$" + ret, Double.parseDouble(Integer.toString(qTest)));

                                // ArrayList miscList3 = this.expiryDates("$" + ret, qtyPrpgt);
                                Debug.print("ret: " + ret);
                                Iterator m2 = miscList3.iterator();
                                ret = new StringBuilder();
                                String ret2 = "false";
                                int a = 0;
                                while (m2.hasNext()) {
                                    String miscStr2 = (String) m2.next();

                                    if (ret2 == "1st") {
                                        ret2 = "false";
                                    }
                                    Debug.print("2 miscStr: " + miscStr);
                                    Debug.print("2 miscStr2: " + miscStr2);
                                    if (miscStr2.equals(miscStr)) {
                                        if (a == 0) {
                                            a = 1;
                                            ret2 = "1st";
                                            Checker = "true";
                                        } else {
                                            a = a + 1;
                                            ret2 = "true";
                                        }
                                    }
                                    Debug.print("Checker: " + Checker);
                                    if (!miscStr2.equals(miscStr) || a > 1) {
                                        if ((ret2 != "1st") || (ret2 == "false") || (ret2 == "true")) {
                                            if (miscStr2 != "") {
                                                miscStr2 = "$" + miscStr2;
                                                ret.append(miscStr2);
                                                ret2 = "false";
                                            }
                                        }
                                    }
                                }
                                ret.append("$");
                                qtyPrpgt = qtyPrpgt - 1;
                            }
                            propagateMisc = ret.toString();
                        }

                    } else {
                        try {
                            propagateMisc = miscList + invPropagatedCosting.getCstExpiryDate().substring(1);
                        } catch (Exception e) {
                            propagateMisc = miscList;
                        }
                    }
                    invPropagatedCosting.setCstExpiryDate(propagateMisc);
                }
            }

            if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                // regenerate cost variance
                this.regenerateCostVariance(invCostings, invCosting, AD_BRNCH, companyCode);
            }

        } catch (AdPRFCoaGlVarianceAccountNotFoundException | GlobalExpiryDateNotFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public String getQuantityExpiryDates(String qntty) {

        String separator = "$";
        String y = "";
        try {
            // Remove first $ character
            qntty = qntty.substring(1);

            // Counter
            int start = 0;
            int nextIndex = qntty.indexOf(separator, start);
            int length = nextIndex - start;

            y = (qntty.substring(start, start + length));
            Debug.print("Y " + y);
        } catch (Exception e) {
            y = "0";
        }

        return y;
    }

    private ArrayList expiryDates(String misc, double qty) throws Exception {

        Debug.print("ApReceivingItemControllerBean getExpiryDates");
        Debug.print("misc: " + misc);
        String separator = "$";

        // Remove first $ character
        misc = misc.substring(1);

        // Counter
        int start = 0;
        int nextIndex = misc.indexOf(separator, start);
        int length = nextIndex - start;

        Debug.print("qty" + qty);
        ArrayList miscList = new ArrayList();

        for (int x = 0; x < qty; x++) {

            // Date
            start = nextIndex + 1;
            nextIndex = misc.indexOf(separator, start);
            length = nextIndex - start;
            Debug.print("x" + x);
            String checker = misc.substring(start, start + length);
            Debug.print("checker" + checker);
            if (checker.length() != 0 || checker != "null") {
                miscList.add(checker);
            } else {
                miscList.add("null");
            }
        }

        Debug.print("miscList :" + miscList);
        return miscList;
    }

    public String propagateExpiryDates(String misc, double qty, String reverse) throws Exception {
        // ActionErrors errors = new ActionErrors();
        Debug.print("ApReceivingItemControllerBean getExpiryDates");
        Debug.print("misc: " + misc);

        String separator = "";
        if (reverse == "False") {
            separator = "$";
        } else {
            separator = " ";
        }

        // Remove first $ character
        misc = misc.substring(1);
        Debug.print("misc: " + misc);
        // Counter
        int start = 0;
        int nextIndex = misc.indexOf(separator, start);
        int length = nextIndex - start;

        StringBuilder miscList = new StringBuilder();

        for (int x = 0; x < qty; x++) {

            // Date
            start = nextIndex + 1;
            nextIndex = misc.indexOf(separator, start);
            length = nextIndex - start;
            String g = misc.substring(start, start + length);
            Debug.print("g: " + g);
            Debug.print("g length: " + g.length());
            if (g.length() != 0) {
                miscList.append("$").append(g);
                Debug.print("miscList G: " + miscList);
            }
        }

        miscList.append("$");
        Debug.print("miscList :" + miscList);
        return (miscList.toString());
    }

    private void regenerateInventoryDr(LocalArReceipt arReceipt, Integer AD_BRNCH, Integer companyCode) throws GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException {

        Debug.print("ArMiscReceiptEntryControllerBean regenerateInventoryDr");

        LocalArDistributionRecordHome arDistributionRecordHome = null;
        LocalInvCostingHome invCostingHome = null;
        LocalInvItemHome invItemHome = null;
        LocalInvItemLocationHome invItemLocationHome = null;
        LocalAdBranchItemLocationHome adBranchItemLocationHome = null;

        // Initialize EJB Home

        try {

            arDistributionRecordHome = (LocalArDistributionRecordHome) EJBHomeFactory.lookUpLocalHome(LocalArDistributionRecordHome.JNDI_NAME, LocalArDistributionRecordHome.class);
            invCostingHome = (LocalInvCostingHome) EJBHomeFactory.lookUpLocalHome(LocalInvCostingHome.JNDI_NAME, LocalInvCostingHome.class);
            invItemHome = (LocalInvItemHome) EJBHomeFactory.lookUpLocalHome(LocalInvItemHome.JNDI_NAME, LocalInvItemHome.class);
            invItemLocationHome = (LocalInvItemLocationHome) EJBHomeFactory.lookUpLocalHome(LocalInvItemLocationHome.JNDI_NAME, LocalInvItemLocationHome.class);
            adBranchItemLocationHome = (LocalAdBranchItemLocationHome) EJBHomeFactory.lookUpLocalHome(LocalAdBranchItemLocationHome.JNDI_NAME, LocalAdBranchItemLocationHome.class);

        } catch (NamingException ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            // regenerate inventory distribution records

            Collection arDistributionRecords = null;

            if (arReceipt.getRctVoid() == EJBCommon.FALSE) {

                arDistributionRecords = arDistributionRecordHome.findImportableDrByDrReversedAndRctCode(EJBCommon.FALSE, arReceipt.getRctCode(), companyCode);

            } else {

                arDistributionRecords = arDistributionRecordHome.findImportableDrByDrReversedAndRctCode(EJBCommon.TRUE, arReceipt.getRctCode(), companyCode);
            }

            Iterator i = arDistributionRecords.iterator();

            while (i.hasNext()) {

                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

                if (arDistributionRecord.getDrClass().equals("COGS") || arDistributionRecord.getDrClass().equals("INVENTORY")) {

                    i.remove();
                    // arDistributionRecord.entityRemove();
                    em.remove(arDistributionRecord);
                }
            }

            Collection arInvoiceLineItems = arReceipt.getArInvoiceLineItems();

            if (arInvoiceLineItems != null && !arInvoiceLineItems.isEmpty()) {

                i = arInvoiceLineItems.iterator();

                while (i.hasNext()) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) i.next();
                    LocalInvItemLocation invItemLocation = arInvoiceLineItem.getInvItemLocation();
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(arReceipt.getRctDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }

                    // add cost of sales distribution and inventory

                    double COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                    try {

                        LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arReceipt.getRctDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);

                        if (invCosting.getCstRemainingQuantity() <= 0 && invCosting.getCstRemainingValue() <= 0) {
                            Debug.print("RE CALC");
                            HashMap criteria = new HashMap();
                            criteria.put("itemName", invItemLocation.getInvItem().getIiName());
                            criteria.put("location", invItemLocation.getInvLocation().getLocName());

                            ArrayList branchList = new ArrayList();

                            AdBranchDetails mdetails = new AdBranchDetails();
                            mdetails.setBrCode(AD_BRNCH);
                            branchList.add(mdetails);

                            ejbRIC.executeInvFixItemCosting(criteria, branchList, companyCode);

                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arReceipt.getRctDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                        }

                        if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Average")) {
                            COST = invCosting.getCstRemainingQuantity() <= 0 ? COST : Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                        }

                        if (COST <= 0) {
                            invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                        } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("FIFO")) {
                            COST = invCosting.getCstRemainingQuantity() == 0 ? COST : Math.abs(this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), arInvoiceLineItem.getIliQuantity(), arInvoiceLineItem.getIliUnitPrice(), false, AD_BRNCH, companyCode));
                        } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Standard")) {
                            COST = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                        }

                    } catch (FinderException ex) {
                    }

                    double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {

                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arInvoiceLineItem.getInvItemLocation().getIlCode(), AD_BRNCH, companyCode);

                    } catch (FinderException ex) {

                    }

                    if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiNonInventoriable() == EJBCommon.FALSE) {

                        if (adBranchItemLocation != null) {

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(), "COGS", EJBCommon.TRUE, COST * QTY_SLD, adBranchItemLocation.getBilCoaGlCostOfSalesAccount(), arReceipt, AD_BRNCH, companyCode);

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(), "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD, adBranchItemLocation.getBilCoaGlInventoryAccount(), arReceipt, AD_BRNCH, companyCode);

                        } else {

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(), "COGS", EJBCommon.TRUE, COST * QTY_SLD, arInvoiceLineItem.getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arReceipt, AD_BRNCH, companyCode);

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(), "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD, arInvoiceLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(), arReceipt, AD_BRNCH, companyCode);
                        }
                    }
                }
            }

        } catch (GlobalInventoryDateException | GlobalBranchAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void voidInvAdjustment(LocalInvAdjustment invAdjustment, Integer AD_BRNCH, Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean voidInvAdjustment");

        try {

            Collection invDistributionRecords = invAdjustment.getInvDistributionRecords();
            ArrayList list = new ArrayList();

            Iterator i = invDistributionRecords.iterator();

            while (i.hasNext()) {

                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) i.next();

                list.add(invDistributionRecord);
            }

            i = list.iterator();

            while (i.hasNext()) {

                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) i.next();

                this.addInvDrEntry(invAdjustment.getInvDrNextLine(), invDistributionRecord.getDrClass(), invDistributionRecord.getDrDebit() == EJBCommon.TRUE ? EJBCommon.FALSE : EJBCommon.TRUE, invDistributionRecord.getDrAmount(), EJBCommon.TRUE, invDistributionRecord.getInvChartOfAccount().getCoaCode(), invAdjustment, AD_BRNCH, companyCode);
            }

            Collection invAdjustmentLines = invAdjustment.getInvAdjustmentLines();
            i = invAdjustmentLines.iterator();
            list.clear();

            while (i.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();

                list.add(invAdjustmentLine);
            }

            i = list.iterator();

            while (i.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();

                this.addInvAlEntry(invAdjustmentLine.getInvItemLocation(), invAdjustment, (invAdjustmentLine.getAlUnitCost()) * -1, EJBCommon.TRUE, companyCode);
            }

            invAdjustment.setAdjVoid(EJBCommon.TRUE);

            this.executeInvAdjPost(invAdjustment.getAdjCode(), invAdjustment.getAdjLastModifiedBy(), AD_BRNCH, companyCode);

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void generateCostVariance(LocalInvItemLocation invItemLocation, double CST_VRNC_VL, String ADJ_RFRNC_NMBR, String ADJ_DSCRPTN, Date ADJ_DT, String USR_NM, Integer AD_BRNCH, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

    }

    private void regenerateCostVariance(Collection invCostings, LocalInvCosting invCosting, Integer AD_BRNCH, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

    }

    private void addInvDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, byte DR_RVRSL, Integer COA_CODE, LocalInvAdjustment invAdjustment, Integer AD_BRNCH, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArMiscReceiptEntryControllerBean addInvDrEntry");
        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate coa

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, AD_BRNCH, companyCode);

            } catch (FinderException ex) {

                throw new GlobalBranchAccountNumberInvalidException();
            }

            // create distribution record

            LocalInvDistributionRecord invDistributionRecord = invDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), DR_RVRSL, EJBCommon.FALSE, companyCode);

            // invAdjustment.addInvDistributionRecord(invDistributionRecord);
            invDistributionRecord.setInvAdjustment(invAdjustment);
            // glChartOfAccount.addInvDistributionRecord(invDistributionRecord);
            invDistributionRecord.setInvChartOfAccount(glChartOfAccount);

        } catch (GlobalBranchAccountNumberInvalidException ex) {

            throw new GlobalBranchAccountNumberInvalidException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void executeInvAdjPost(Integer ADJ_CODE, String USR_NM, Integer AD_BRNCH, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException {

        Debug.print("ArMiscReceiptEntryControllerBean executeInvAdjPost");
        try {

            // validate if adjustment is already deleted

            LocalInvAdjustment invAdjustment = null;

            try {

                invAdjustment = invAdjustmentHome.findByPrimaryKey(ADJ_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if adjustment is already posted or void

            if (invAdjustment.getAdjPosted() == EJBCommon.TRUE) {

                if (invAdjustment.getAdjVoid() != EJBCommon.TRUE) {
                    throw new GlobalTransactionAlreadyPostedException();
                }
            }

            Collection invAdjustmentLines = null;

            if (invAdjustment.getAdjVoid() == EJBCommon.FALSE) {
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.FALSE, invAdjustment.getAdjCode(), companyCode);
            } else {
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.TRUE, invAdjustment.getAdjCode(), companyCode);
            }

            Iterator i = invAdjustmentLines.iterator();

            while (i.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();

                LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(invAdjustment.getAdjDate(), invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(), invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName(), AD_BRNCH, companyCode);

                this.postInvAdjustmentToInventory(invAdjustmentLine, invAdjustment.getAdjDate(), 0, invAdjustmentLine.getAlUnitCost(), invCosting.getCstRemainingQuantity(), invCosting.getCstRemainingValue() + invAdjustmentLine.getAlUnitCost(), AD_BRNCH, companyCode);
            }

            // post to gl if necessary

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate if date has no period and period is closed

            LocalGlSetOfBook glJournalSetOfBook = null;

            try {

                glJournalSetOfBook = glSetOfBookHome.findByDate(invAdjustment.getAdjDate(), companyCode);

            } catch (FinderException ex) {

                throw new GlJREffectiveDateNoPeriodExistException();
            }

            LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), invAdjustment.getAdjDate(), companyCode);

            if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P') {

                throw new GlJREffectiveDatePeriodClosedException();
            }

            // check if invoice is balance if not check suspense posting

            LocalGlJournalLine glOffsetJournalLine = null;

            Collection invDistributionRecords = null;

            if (invAdjustment.getAdjVoid() == EJBCommon.FALSE) {

                invDistributionRecords = invDistributionRecordHome.findImportableDrByDrReversedAndAdjCode(EJBCommon.FALSE, invAdjustment.getAdjCode(), companyCode);

            } else {

                invDistributionRecords = invDistributionRecordHome.findImportableDrByDrReversedAndAdjCode(EJBCommon.TRUE, invAdjustment.getAdjCode(), companyCode);
            }

            Iterator j = invDistributionRecords.iterator();

            double TOTAL_DEBIT = 0d;
            double TOTAL_CREDIT = 0d;

            while (j.hasNext()) {

                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) j.next();

                double DR_AMNT = 0d;

                DR_AMNT = invDistributionRecord.getDrAmount();

                if (invDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                    TOTAL_DEBIT += DR_AMNT;

                } else {

                    TOTAL_CREDIT += DR_AMNT;
                }
            }

            TOTAL_DEBIT = EJBCommon.roundIt(TOTAL_DEBIT, adCompany.getGlFunctionalCurrency().getFcPrecision());
            TOTAL_CREDIT = EJBCommon.roundIt(TOTAL_CREDIT, adCompany.getGlFunctionalCurrency().getFcPrecision());

            if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.TRUE && TOTAL_DEBIT != TOTAL_CREDIT) {

                LocalGlSuspenseAccount glSuspenseAccount = null;

                try {

                    glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("INVENTORY", "INVENTORY ADJUSTMENTS", companyCode);

                } catch (FinderException ex) {

                    throw new GlobalJournalNotBalanceException();
                }

                if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                    glOffsetJournalLine = glJournalLineHome.create((short) (invDistributionRecords.size() + 1), EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);

                } else {

                    glOffsetJournalLine = glJournalLineHome.create((short) (invDistributionRecords.size() + 1), EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
                }

                LocalGlChartOfAccount glChartOfAccount = glSuspenseAccount.getGlChartOfAccount();
                // glChartOfAccount.addGlJournalLine(glOffsetJournalLine);
                glOffsetJournalLine.setGlChartOfAccount(glChartOfAccount);

            } else if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE && TOTAL_DEBIT != TOTAL_CREDIT) {

                throw new GlobalJournalNotBalanceException();
            }

            // create journal batch if necessary

            LocalGlJournalBatch glJournalBatch = null;
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");

            try {

                glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " INVENTORY ADJUSTMENTS", AD_BRNCH, companyCode);

            } catch (FinderException ex) {

            }

            if (glJournalBatch == null) {

                glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " INVENTORY ADJUSTMENTS", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, AD_BRNCH, companyCode);
            }

            // create journal entry

            LocalGlJournal glJournal = glJournalHome.create(invAdjustment.getAdjReferenceNumber(), invAdjustment.getAdjDescription(), invAdjustment.getAdjDate(), 0.0d, null, invAdjustment.getAdjDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM, EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, EJBCommon.FALSE, null, AD_BRNCH, companyCode);

            LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("INVENTORY", companyCode);
            glJournal.setGlJournalSource(glJournalSource);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
            glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("INVENTORY ADJUSTMENTS", companyCode);
            glJournal.setGlJournalCategory(glJournalCategory);

            if (glJournalBatch != null) {

                glJournal.setGlJournalBatch(glJournalBatch);
            }

            // create journal lines

            j = invDistributionRecords.iterator();

            while (j.hasNext()) {

                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) j.next();

                double DR_AMNT = 0d;

                DR_AMNT = invDistributionRecord.getDrAmount();

                LocalGlJournalLine glJournalLine = glJournalLineHome.create(invDistributionRecord.getDrLine(), invDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

                glJournalLine.setGlChartOfAccount(invDistributionRecord.getInvChartOfAccount());

                glJournalLine.setGlJournal(glJournal);

                invDistributionRecord.setDrImported(EJBCommon.TRUE);
            }

            if (glOffsetJournalLine != null) {

                glOffsetJournalLine.setGlJournal(glJournal);
            }

            // post journal to gl
            Collection glJournalLines = glJournalLineHome.findByJrCode(glJournal.getJrCode(), companyCode);
            i = glJournalLines.iterator();

            while (i.hasNext()) {

                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) i.next();

                // post current to current acv

                this.postToGl(glAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), true, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                // post to subsequent acvs (propagate)

                Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), glAccountingCalendarValue.getAcvPeriodNumber(), companyCode);

                for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {

                    LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;

                    this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                }

                // post to subsequent years if necessary

                Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode);

                if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                    adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                    LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(adCompany.getCmpRetainedEarnings(), AD_BRNCH, companyCode);

                    for (Object subsequentSetOfBook : glSubsequentSetOfBooks) {

                        LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) subsequentSetOfBook;

                        String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                        // post to subsequent acvs of subsequent set of book(propagate)

                        Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                        for (Object accountingCalendarValue : glAccountingCalendarValues) {

                            LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                            if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY") || ACCOUNT_TYPE.equals("OWNERS EQUITY")) {

                                this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                            } else { // revenue & expense

                                this.postToGl(glSubsequentAccountingCalendarValue, glRetainedEarningsAccount, false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                            }
                        }

                        if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) {
                            break;
                        }
                    }
                }
            }

            invAdjustment.setAdjPosted(EJBCommon.TRUE);

        } catch (GlJREffectiveDateNoPeriodExistException | GlobalTransactionAlreadyPostedException |
                 GlobalRecordAlreadyDeletedException | GlobalJournalNotBalanceException |
                 GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalInvAdjustmentLine addInvAlEntry(LocalInvItemLocation invItemLocation, LocalInvAdjustment invAdjustment, double CST_VRNC_VL, byte AL_VD, Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean addInvAlEntry");
        try {

            // create dr entry
            LocalInvAdjustmentLine invAdjustmentLine = null;
            invAdjustmentLine = invAdjustmentLineHome.create(CST_VRNC_VL, null, null, 0, 0, AL_VD, companyCode);

            // map adjustment, unit of measure, item location
            // invAdjustment.addInvAdjustmentLine(invAdjustmentLine);
            invAdjustmentLine.setInvAdjustment(invAdjustment);
            // invItemLocation.getInvItem().getInvUnitOfMeasure().addInvAdjustmentLine(invAdjustmentLine);
            invAdjustmentLine.setInvUnitOfMeasure(invItemLocation.getInvItem().getInvUnitOfMeasure());
            // invItemLocation.addInvAdjustmentLine(invAdjustmentLine);
            invAdjustmentLine.setInvItemLocation(invItemLocation);

            return invAdjustmentLine;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postInvAdjustmentToInventory(LocalInvAdjustmentLine invAdjustmentLine, Date CST_DT, double CST_ADJST_QTY, double CST_ADJST_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, Integer AD_BRNCH, Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean postInvAdjustmentToInventory");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalInvItemLocation invItemLocation = invAdjustmentLine.getInvItemLocation();
            int CST_LN_NMBR = 0;

            CST_ADJST_QTY = EJBCommon.roundIt(CST_ADJST_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_ADJST_CST = EJBCommon.roundIt(CST_ADJST_CST, adCompany.getGlFunctionalCurrency().getFcPrecision());
            CST_RMNNG_QTY = EJBCommon.roundIt(CST_RMNNG_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_RMNNG_VL = EJBCommon.roundIt(CST_RMNNG_VL, adCompany.getGlFunctionalCurrency().getFcPrecision());

            if (CST_ADJST_QTY < 0) {

                invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - Math.abs(CST_ADJST_QTY));
            }

            // create costing

            try {

                // generate line number

                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            } catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d, CST_ADJST_QTY, CST_ADJST_CST, 0d, 0d, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, CST_ADJST_QTY > 0 ? CST_ADJST_QTY : 0, AD_BRNCH, companyCode);
            // invItemLocation.addInvCosting(invCosting);
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setInvAdjustmentLine(invAdjustmentLine);

            invCosting.setCstQuantity(CST_ADJST_QTY);
            invCosting.setCstCost(CST_ADJST_CST);

            // propagate balance if necessary

            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);

            for (Object costing : invCostings) {

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) costing;

                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_ADJST_QTY);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_ADJST_CST);
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public boolean getArTraceMisc(String II_NAME, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean getArTraceMisc");
        boolean isTraceMisc = false;
        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NAME, companyCode);
            if (invItem.getIiTraceMisc() == 1) {
                isTraceMisc = true;
            }
            return isTraceMisc;
        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void createInvTagList(LocalArInvoiceLineItem arInvoiceLineItem, ArrayList list, Integer companyCode) throws Exception {

        Debug.print("ArMiscReceiptEntryController createInvTagList");
        try {
            Debug.print("aabot?");
            // Iterator t = apPurchaseOrderLine.getInvTag().iterator();
            Iterator t = list.iterator();

            LocalInvTag invTag = null;
            Debug.print("umabot?");
            while (t.hasNext()) {
                InvModTagListDetails tgLstDetails = (InvModTagListDetails) t.next();
                Debug.print(tgLstDetails.getTgCustodian() + "<== custodian");
                Debug.print(tgLstDetails.getTgSpecs() + "<== specs");
                Debug.print(tgLstDetails.getTgPropertyCode() + "<== propertyCode");
                Debug.print(tgLstDetails.getTgExpiryDate() + "<== expiryDate");
                Debug.print(tgLstDetails.getTgSerialNumber() + "<== serial number");

                if (tgLstDetails.getTgCode() == null) {
                    Debug.print("ngcreate ba?");
                    invTag = invTagHome.create(tgLstDetails.getTgPropertyCode(), tgLstDetails.getTgSerialNumber(), null, tgLstDetails.getTgExpiryDate(), tgLstDetails.getTgSpecs(), companyCode, tgLstDetails.getTgTransactionDate(), tgLstDetails.getTgType());

                    invTag.setArInvoiceLineItem(arInvoiceLineItem);
                    invTag.setInvItemLocation(arInvoiceLineItem.getInvItemLocation());
                    LocalAdUser adUser = null;
                    try {
                        adUser = adUserHome.findByUsrName(tgLstDetails.getTgCustodian(), companyCode);
                    } catch (FinderException ex) {

                    }
                    invTag.setAdUser(adUser);
                    Debug.print("ngcreate ba?");
                }
            }

        } catch (Exception ex) {
            throw ex;
        }
    }

    private ArrayList getInvTagList(LocalArInvoiceLineItem arInvoiceLineItem) {

        ArrayList list = new ArrayList();
        Collection invTags = arInvoiceLineItem.getInvTags();
        for (Object tag : invTags) {
            LocalInvTag invTag = (LocalInvTag) tag;
            InvModTagListDetails tgLstDetails = new InvModTagListDetails();
            tgLstDetails.setTgPropertyCode(invTag.getTgPropertyCode());
            tgLstDetails.setTgSpecs(invTag.getTgSpecs());
            tgLstDetails.setTgExpiryDate(invTag.getTgExpiryDate());
            tgLstDetails.setTgSerialNumber(invTag.getTgSerialNumber());
            try {

                tgLstDetails.setTgCustodian(invTag.getAdUser().getUsrName());
            } catch (Exception ex) {
                tgLstDetails.setTgCustodian("");
            }

            list.add(tgLstDetails);

            Debug.print(tgLstDetails.getTgPropertyCode() + "<== property code inside controllerbean ");
            Debug.print(tgLstDetails.getTgSpecs() + "<== specs inside controllerbean ");
            Debug.print(list + "<== taglist inside controllerbean ");
        }
        return list;
    }

    public byte getAdPrfArUseCustomerPulldown(Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean getAdPrfArUseCustomerPulldown");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfArUseCustomerPulldown();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ArMiscReceiptEntryControllerBean ejbCreate");
    }

}