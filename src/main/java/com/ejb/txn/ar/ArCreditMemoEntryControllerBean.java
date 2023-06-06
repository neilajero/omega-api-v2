package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ar.*;
import com.ejb.dao.gl.*;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ar.*;
import com.ejb.entities.gl.*;
import com.ejb.entities.inv.*;
import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.ar.ArINVOverapplicationNotAllowedException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.ar.ArInvoiceDetails;
import com.util.mod.ar.ArModDistributionRecordDetails;
import com.util.mod.ar.ArModInvoiceDetails;
import com.util.mod.ar.ArModInvoiceLineItemDetails;
import com.util.mod.inv.InvModUnitOfMeasureDetails;

import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Stateless(name = "ArCreditMemoEntryControllerEJB")
public class ArCreditMemoEntryControllerBean extends EJBContextClass implements ArCreditMemoEntryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    ArApprovalController arApprovalController;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private ILocalGlSetOfBookHome glSetOfBookHome;
    @EJB
    private ILocalGlAccountingCalendarValueHome glAccountingCalendarValueHome;
    @EJB
    private ILocalGlChartOfAccountBalanceHome glChartOfAccountBalanceHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private LocalAdBranchCustomerHome adBranchCustomerHome;
    @EJB
    private LocalAdBranchArTaxCodeHome adBranchArTaxCodeHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalArCustomerBalanceHome arCustomerBalanceHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalGlSuspenseAccountHome glSuspenseAccountHome;
    @EJB
    private LocalGlJournalLineHome glJournalLineHome;
    @EJB
    private LocalGlJournalBatchHome glJournalBatchHome;
    @EJB
    private LocalGlJournalHome glJournalHome;
    @EJB
    private LocalGlJournalSourceHome glJournalSourceHome;
    @EJB
    private LocalGlJournalCategoryHome glJournalCategoryHome;
    @EJB
    private LocalInvAdjustmentHome invAdjustmentHome;
    @EJB
    private LocalInvAdjustmentLineHome invAdjustmentLineHome;
    @EJB
    private LocalInvDistributionRecordHome invDistributionRecordHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private LocalArInvoiceLineItemHome arInvoiceLineItemHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvPriceLevelHome invPriceLevelHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalArInvoicePaymentScheduleHome arInvoicePaymentScheduleHome;
    @EJB
    private LocalArInvoiceBatchHome arInvoiceBatchHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalAdDeleteAuditTrailHome adDeleteAuditTrailHome;
    @EJB
    private LocalArJobOrderInvoiceLineHome arJobOrderInvoiceLineHome;
    @EJB
    private LocalArJobOrderLineHome arJobOrderLineHome;
    @EJB
    private LocalArSalesOrderInvoiceLineHome arSalesOrderInvoiceLineHome;
    @EJB
    private LocalArSalesOrderLineHome arSalesOrderLineHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;


    public Date getArInvoiceDateByInvoiceNumber(String INV_NMBR, Integer INV_AD_BRNCH, Integer INV_AD_CMPNY) {

        Debug.print("ArCreditMemoEntryControllerBean getArInvoiceDateByInvoiceNumber");
        try {
            LocalArInvoice arInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(INV_NMBR, (byte) 0, INV_AD_BRNCH, INV_AD_CMPNY);
            return arInvoice.getInvDate();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public double getArInvoiceAmountDueByInvoiceNumber(String INV_NMBR, Integer INV_AD_BRNCH, Integer INV_AD_CMPNY) {

        Debug.print("ArCreditMemoEntryControllerBean getArInvoiceAmountDueByInvoiceNumber");
        try {
            LocalArInvoice arInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(INV_NMBR, (byte) 0, INV_AD_BRNCH, INV_AD_CMPNY);
            return arInvoice.getInvAmountDue() - arInvoice.getInvAmountPaid();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArCstAll(Integer branchCode, Integer companyCode) {

        Debug.print("ArCreditMemoEntryControllerBean getArCstAll");
        ArrayList list = new ArrayList();
        try {
            Collection arCustomers = arCustomerHome.findEnabledCstAll(branchCode, companyCode);
            for (Object customer : arCustomers) {
                LocalArCustomer arCustomer = (LocalArCustomer) customer;
                list.add(arCustomer.getCstCustomerCode());
            }
            return list;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getInvLocAll(Integer companyCode) {

        Debug.print("ArCreditMemoEntryControllerBean getInvLocAll");
        Collection invLocations = null;
        ArrayList list = new ArrayList();
        try {
            invLocations = invLocationHome.findLocAll(companyCode);
            if (invLocations.isEmpty()) {
                return null;
            }
            for (Object location : invLocations) {
                LocalInvLocation invLocation = (LocalInvLocation) location;
                String details = invLocation.getLocName();
                list.add(details);
            }
            return list;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getInvUomByIiName(String II_NM, Integer companyCode) {

        Debug.print("ArCreditMemoEntryControllerBean getInvUomByIiName");
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

        Debug.print("ArCreditMemoEntryControllerBean getInvIiSalesPriceByIiNameAndUomName");

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

    public short getInvGpQuantityPrecisionUnit(Integer companyCode) {

        Debug.print("ArCreditMemoEntryControllerBean getInvGpQuantityPrecisionUnit");
        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            return adPreference.getPrfInvQuantityPrecisionUnit();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getAdPrfArInvoiceLineNumber(Integer companyCode) {

        Debug.print("ArCreditMemoEntryControllerBean getAdPrfArInvoiceLineNumber");
        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            return adPreference.getPrfArInvoiceLineNumber();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArModInvoiceDetails getArInvByInvCode(Integer INV_CODE, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArCreditMemoEntryControllerBean getArInvByInvCode");

        try {
            LocalArInvoice arCreditMemo = null;
            LocalArInvoice arInvoiceLink = null;
            LocalArTaxCode arTaxCode = null;
            LocalArWithholdingTaxCode arWTaxCode = null;
            try {
                arCreditMemo = arInvoiceHome.findByPrimaryKey(INV_CODE);
                arInvoiceLink = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arCreditMemo.getInvCmInvoiceNumber(), EJBCommon.FALSE, branchCode, companyCode);
                arTaxCode = arCreditMemo.getArTaxCode();
                if (arTaxCode == null) {
                    arTaxCode = arInvoiceLink.getArTaxCode();
                }

                arWTaxCode = arCreditMemo.getArWithholdingTaxCode();
                if (arWTaxCode == null) {
                    arWTaxCode = arInvoiceLink.getArWithholdingTaxCode();
                }
            } catch (FinderException ex) {
                throw new GlobalNoRecordFoundException();
            }

            ArrayList list = new ArrayList();
            // get invoice line items if any
            Collection arInvoiceLineItems = arCreditMemo.getArInvoiceLineItems();

            double TOTAL_DEBIT = 0d;
            double TOTAL_CREDIT = 0d;

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
                    iliDetails.setIliTax(arInvoiceLineItem.getIliTax());

                    iliDetails.setIliMisc(arInvoiceLineItem.getIliMisc());
                    list.add(iliDetails);
                }
            } else {
                // get distribution records
                Collection arDistributionRecords = arDistributionRecordHome.findByInvCode(arCreditMemo.getInvCode(), companyCode);
                short lineNumber = 1;

                Iterator i = arDistributionRecords.iterator();

                TOTAL_DEBIT = 0d;
                TOTAL_CREDIT = 0d;

                while (i.hasNext()) {
                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();
                    ArModDistributionRecordDetails mdetails = new ArModDistributionRecordDetails();
                    mdetails.setDrCode(arDistributionRecord.getDrCode());
                    mdetails.setDrLine(lineNumber);
                    mdetails.setDrClass(arDistributionRecord.getDrClass());
                    mdetails.setDrDebit(arDistributionRecord.getDrDebit());
                    mdetails.setDrAmount(arDistributionRecord.getDrAmount());
                    mdetails.setDrCoaAccountNumber(arDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                    mdetails.setDrCoaAccountDescription(arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());
                    if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                        TOTAL_DEBIT += arDistributionRecord.getDrAmount();
                    } else {
                        TOTAL_CREDIT += arDistributionRecord.getDrAmount();
                    }
                    list.add(mdetails);
                    lineNumber++;
                }
            }

            ArModInvoiceDetails mInvDetails = new ArModInvoiceDetails();
            mInvDetails.setInvType(arCreditMemo.getInvType());
            mInvDetails.setInvCode(arCreditMemo.getInvCode());
            mInvDetails.setInvDescription(arCreditMemo.getInvDescription());
            mInvDetails.setInvDate(arCreditMemo.getInvDate());
            mInvDetails.setInvNumber(arCreditMemo.getInvNumber());
            mInvDetails.setInvCmInvoiceNumber(arCreditMemo.getInvCmInvoiceNumber());
            mInvDetails.setInvCmReferenceNumber(arCreditMemo.getInvCmReferenceNumber());
            mInvDetails.setInvAmountDue(arCreditMemo.getInvAmountDue());
            mInvDetails.setInvApprovalStatus(arCreditMemo.getInvApprovalStatus());
            mInvDetails.setInvVoidApprovalStatus(arCreditMemo.getInvVoidApprovalStatus());
            mInvDetails.setInvReasonForRejection(arCreditMemo.getInvReasonForRejection());
            mInvDetails.setInvPosted(arCreditMemo.getInvPosted());
            mInvDetails.setInvVoidPosted(arCreditMemo.getInvVoidPosted());
            mInvDetails.setInvVoid(arCreditMemo.getInvVoid());
            mInvDetails.setInvTotalDebit(TOTAL_DEBIT);
            mInvDetails.setInvTotalCredit(TOTAL_CREDIT);
            mInvDetails.setInvCreatedBy(arCreditMemo.getInvCreatedBy());
            mInvDetails.setInvDateCreated(arCreditMemo.getInvDateCreated());
            mInvDetails.setInvLastModifiedBy(arCreditMemo.getInvLastModifiedBy());
            mInvDetails.setInvDateLastModified(arCreditMemo.getInvDateLastModified());
            mInvDetails.setInvApprovedRejectedBy(arCreditMemo.getInvApprovedRejectedBy());
            mInvDetails.setInvDateApprovedRejected(arCreditMemo.getInvDateApprovedRejected());
            mInvDetails.setInvPostedBy(arCreditMemo.getInvPostedBy());
            mInvDetails.setInvDatePosted(arCreditMemo.getInvDatePosted());
            mInvDetails.setInvCstCustomerCode(arCreditMemo.getArCustomer().getCstCustomerCode());
            mInvDetails.setInvIbName(arCreditMemo.getArInvoiceBatch() != null ? arCreditMemo.getArInvoiceBatch().getIbName() : null);
            mInvDetails.setInvLvShift(arCreditMemo.getInvLvShift());
            mInvDetails.setInvSubjectToCommission(arCreditMemo.getInvSubjectToCommission());
            mInvDetails.setInvCstName(arCreditMemo.getArCustomer().getCstName());
            mInvDetails.setInvTaxCodeName(arTaxCode == null ? "" : arTaxCode.getTcName());
            mInvDetails.setInvWTaxCodeName(arWTaxCode == null ? "" : arWTaxCode.getWtcName());
            mInvDetails.setReportParameter(arCreditMemo.getReportParameter());
            mInvDetails.setInvPartialCm(arCreditMemo.getInvPartialCm());
            if (!arInvoiceLineItems.isEmpty()) {
                mInvDetails.setInvIliList(list);
                mInvDetails.setInvAmountDue(arCreditMemo.getInvAmountDue());
            } else {
                mInvDetails.setInvDrList(list);
            }
            return mInvDetails;

        } catch (GlobalNoRecordFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfEnableInvShift(Integer companyCode) {

        Debug.print("ArCreditMemoEntryControllerBean getAdPrfEnableInvShift");
        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            return adPreference.getPrfInvEnableShift();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdLvInvShiftAll(Integer companyCode) {

        Debug.print("ArCreditMemoEntryControllerBean getAdLvInvShiftAll");
        ArrayList list = new ArrayList();
        try {
            Collection adLookUpValues = adLookUpValueHome.findByLuName("INV SHIFT", companyCode);
            for (Object lookUpValue : adLookUpValues) {
                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;
                list.add(adLookUpValue.getLvName());
            }
            return list;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public Integer saveArCmInvEntry(ArInvoiceDetails details, String CST_CSTMR_CODE, String IB_NM, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ArCreditMemoEntryControllerBean saveArCmInvEntry");

        LocalArInvoice arCreditMemo;

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            arCreditMemo = arInvoiceHome
                    .InvType("ITEMS")
                    .InvCreditMemo(EJBCommon.TRUE)
                    .InvDescription(details.getInvDescription())
                    .InvDate(details.getInvDate())
                    .InvNumber(details.getInvNumber())
                    .InvReferenceNumber(details.getInvReferenceNumber())
                    .InvUploadNumber(details.getInvUploadNumber())
                    .InvCmInvoiceNumber(details.getInvCmInvoiceNumber())
                    .InvCmReferenceNumber(details.getInvCmReferenceNumber())
                    .InvAmountDue(details.getInvAmountDue())
                    .InvCreatedBy(details.getInvCreatedBy())
                    .InvDateCreated(details.getInvDateCreated())
                    .InvLastModifiedBy(details.getInvLastModifiedBy())
                    .InvDateLastModified(details.getInvDateLastModified())
                    .InvSubjectToCommission(details.getInvSubjectToCommission())
                    .InvClientPO(details.getInvClientPO())
                    .InvEffectivityDate(details.getInvEffectivityDate())
                    .InvAdBranch(AD_BRNCH)
                    .InvAdCompany(AD_CMPNY)
                    .buildInvoice(adCompany.getCmpShortName());

            LocalArInvoice arCMInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arCreditMemo.getInvCmInvoiceNumber(),
                    EJBCommon.FALSE, AD_BRNCH, AD_CMPNY);
            arCreditMemo.setArSalesperson(arCMInvoice.getArSalesperson());
            arCreditMemo.setArCustomer(arCMInvoice.getArCustomer());
            try {
                LocalArInvoiceBatch arInvoiceBatch = arInvoiceBatchHome.findByIbName(IB_NM, AD_BRNCH, AD_CMPNY);
                arCreditMemo.setArInvoiceBatch(arInvoiceBatch);
            } catch (FinderException ex) {

            }

            // create distribution records
            Iterator i;

            double ratio = arCMInvoice.getInvAmountDue() / arCreditMemo.getInvAmountDue();

            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome
                    .findByDrClassAndInvCode("RECEIVABLE",arCMInvoice.getInvCode(),AD_CMPNY);

            LocalArDistributionRecord revenueCmArDistributionRecord = arDistributionRecordHome
                    .DrLine((short)1)
                    .DrClass("REVENUE")
                    .DrDebit(EJBCommon.TRUE)
                    .DrAmount(arDistributionRecord.getDrAmount() * ratio)
                    .DrAdCompany(AD_CMPNY)
                    .buildDistributionRecords(adCompany.getCmpShortName());

            revenueCmArDistributionRecord.setGlChartOfAccount(arDistributionRecord.getGlChartOfAccount());
            arCreditMemo.addArDistributionRecord(revenueCmArDistributionRecord);

            LocalArDistributionRecord receivableCmArDistributionRecord = arDistributionRecordHome.create(
                    (short)2, "RECEIVABLE", (byte)0,
                    arDistributionRecord.getDrAmount()*ratio, EJBCommon.FALSE, EJBCommon.FALSE, AD_CMPNY);

            receivableCmArDistributionRecord.setGlChartOfAccount(arDistributionRecord.getGlChartOfAccount());
            arCreditMemo.addArDistributionRecord(receivableCmArDistributionRecord);

            // create new invoice payment schedule lock
            Collection arInvoicePaymentSchedules = arCMInvoice.getArInvoicePaymentSchedules();
            i = arInvoicePaymentSchedules.iterator();
            while (i.hasNext()) {
                LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule)i.next();
                arInvoicePaymentSchedule.setIpsLock(EJBCommon.TRUE);
            }
            return 1;

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }
    }

    public Integer saveArInvEntry(ArInvoiceDetails details, String CST_CSTMR_CODE, String IB_NM, ArrayList drList, boolean isDraft, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalNoRecordFoundException, GlobalDocumentNumberNotUniqueException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidPostedException, ArINVOverapplicationNotAllowedException, GlobalTransactionAlreadyLockedException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalAccountNumberInvalidException, GlobalBranchAccountNumberInvalidException {

        Debug.print("ArCreditMemoEntryControllerBean saveArInvEntry");

        LocalArInvoice arCreditMemo = null;
        LocalArInvoice arInvoice = null;

        try {
            // validate if credit memo is already deleted
            try {
                if (details.getInvCode() != null) {
                    arCreditMemo = arInvoiceHome.findByPrimaryKey(details.getInvCode());
                }
            } catch (FinderException ex) {
                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if credit memo is already posted, void, arproved or pending
            if (details.getInvCode() != null && details.getInvVoid() == EJBCommon.FALSE) {
                if (arCreditMemo.getInvApprovalStatus() != null) {
                    if (arCreditMemo.getInvApprovalStatus().equals("APPROVED") || arCreditMemo.getInvApprovalStatus().equals("N/A")) {
                        throw new GlobalTransactionAlreadyApprovedException();
                    } else if (arCreditMemo.getInvApprovalStatus().equals("PENDING")) {
                        throw new GlobalTransactionAlreadyPendingException();
                    }
                }
                if (arCreditMemo.getInvPosted() == EJBCommon.TRUE) {
                    throw new GlobalTransactionAlreadyPostedException();
                } else if (arCreditMemo.getInvVoid() == EJBCommon.TRUE) {
                    throw new GlobalTransactionAlreadyVoidException();
                }
            }

            // invoice void
            if (details.getInvCode() != null && details.getInvVoid() == EJBCommon.TRUE) {
                if (arCreditMemo.getInvVoid() == EJBCommon.TRUE) {
                    throw new GlobalTransactionAlreadyVoidException();
                }
                if (arCreditMemo.getInvPosted() == EJBCommon.TRUE) {
                    // generate approval status
                    String INV_APPRVL_STATUS = null;
                    if (!isDraft) {
                        LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);

                        // check if ar credit memo approval is enabled
                        if (adApproval.getAprEnableArCreditMemo() == EJBCommon.FALSE) {
                            INV_APPRVL_STATUS = "N/A";
                        } else {
                            LocalAdUser adUser = adUserHome.findByUsrName(details.getInvLastModifiedBy(), companyCode);
                            INV_APPRVL_STATUS = arApprovalController.getApprovalStatus(adUser.getUsrDept(), details.getInvLastModifiedBy(), adUser.getUsrDescription(), "AR CREDIT MEMO", arCreditMemo.getInvCode(), arCreditMemo.getInvNumber(), arCreditMemo.getInvDate(), branchCode, companyCode);
                        }
                    }

                    // reverse distribution records
                    Collection arDistributionRecords = arCreditMemo.getArDistributionRecords();
                    ArrayList list = new ArrayList();
                    Iterator i = arDistributionRecords.iterator();
                    while (i.hasNext()) {
                        LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();
                        list.add(arDistributionRecord);
                    }

                    i = list.iterator();
                    while (i.hasNext()) {
                        LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();
                        this.addArDrEntry(arCreditMemo.getArDrNextLine(), arDistributionRecord.getDrClass(), arDistributionRecord.getDrDebit() == EJBCommon.TRUE ? EJBCommon.FALSE : EJBCommon.TRUE, arDistributionRecord.getDrAmount(), arDistributionRecord.getGlChartOfAccount().getCoaCode(), arCreditMemo, branchCode, companyCode);
                    }
                    LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
                    if (INV_APPRVL_STATUS != null && INV_APPRVL_STATUS.equals("N/A") && adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {
                        arCreditMemo.setInvVoid(EJBCommon.TRUE);
                        this.executeArInvCreditMemoPost(arCreditMemo.getInvCode(), arCreditMemo.getInvLastModifiedBy(), branchCode, companyCode);
                    }
                    // set credit memo void approval status
                    arCreditMemo.setInvVoidApprovalStatus(INV_APPRVL_STATUS);
                } else {
                    LocalArInvoice arLockedInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arCreditMemo.getInvCmInvoiceNumber(), EJBCommon.FALSE, branchCode, companyCode);
                    Collection arLockedInvoicePaymentSchedules = arLockedInvoice.getArInvoicePaymentSchedules();
                    for (Object arLockedInvoicePaymentSchedule : arLockedInvoicePaymentSchedules) {
                        LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) arLockedInvoicePaymentSchedule;
                        arInvoicePaymentSchedule.setIpsLock(EJBCommon.FALSE);
                    }
                }
                arCreditMemo.setInvVoid(EJBCommon.TRUE);
                arCreditMemo.setInvLastModifiedBy(details.getInvLastModifiedBy());
                arCreditMemo.setInvDateLastModified(details.getInvDateLastModified());
                return arCreditMemo.getInvCode();
            }

            // validate if invoice number exists
            Debug.print("1--------------");
            try {
                arInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(details.getInvCmInvoiceNumber(), EJBCommon.FALSE, branchCode, companyCode);
                Debug.print("arInvoice.getInvPosted()=" + arInvoice.getInvPosted());
                if (arInvoice.getInvPosted() != EJBCommon.TRUE) {
                    throw new GlobalNoRecordFoundException();
                }
            } catch (FinderException ex) {
                throw new GlobalNoRecordFoundException();
            }

            // validate if document number is unique document number is automatic then set
            // next sequence
            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
            if (details.getInvCode() == null) {
                try {
                    adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AR CREDIT MEMO", companyCode);
                } catch (FinderException ex) {
                }

                try {
                    adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode);
                } catch (FinderException ex) {
                }

                Debug.print("2--------------");
                LocalArInvoice arExistingCreditMemo = null;
                try {
                    arExistingCreditMemo = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(details.getInvNumber(), EJBCommon.TRUE, branchCode, companyCode);
                } catch (FinderException ex) {
                }

                if (arExistingCreditMemo != null) {
                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (details.getInvNumber() == null || details.getInvNumber().trim().length() == 0)) {
                    while (true) {
                        if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {
                            try {
                                Debug.print("3--------------");
                                arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), EJBCommon.TRUE, branchCode, companyCode);
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                            } catch (FinderException ex) {
                                details.setInvNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                break;
                            }
                        } else {
                            try {
                                Debug.print("4--------------");
                                arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), EJBCommon.TRUE, branchCode, companyCode);
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                            } catch (FinderException ex) {
                                details.setInvNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                break;
                            }
                        }
                    }
                }
            } else {
                LocalArInvoice arExistingCreditMemo = null;
                try {
                    Debug.print("5--------------");
                    arExistingCreditMemo = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(details.getInvNumber(), EJBCommon.TRUE, branchCode, companyCode);
                } catch (FinderException ex) {
                }

                if (arExistingCreditMemo != null && !arExistingCreditMemo.getInvCode().equals(details.getInvCode())) {
                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (arCreditMemo.getInvNumber() != details.getInvNumber() && (details.getInvNumber() == null || details.getInvNumber().trim().length() == 0)) {

                    details.setInvNumber(arCreditMemo.getInvNumber());
                }
            }

            // validate if invoice entered is already locked by cm or receipt
            Collection apValidateInvoicePaymentSchedules = arInvoicePaymentScheduleHome.findByIpsLockAndInvCode(EJBCommon.TRUE, arInvoice.getInvCode(), companyCode);

            if (details.getInvCode() == null && !apValidateInvoicePaymentSchedules.isEmpty() || details.getInvCode() != null && !details.getInvCmInvoiceNumber().equals(arCreditMemo.getInvCmInvoiceNumber()) && !apValidateInvoicePaymentSchedules.isEmpty()) {

                throw new GlobalTransactionAlreadyLockedException();
            }

            // create invoice
            if (details.getInvCode() == null) {
                arCreditMemo = arInvoiceHome
                        .InvType(details.getInvType())
                        .InvCreditMemo(EJBCommon.TRUE)
                        .InvDescription(details.getInvDescription())
                        .InvDate(details.getInvDate())
                        .InvNumber(details.getInvNumber())
                        .InvCmInvoiceNumber(details.getInvCmInvoiceNumber())
                        .InvReferenceNumber(details.getInvReferenceNumber())
                        .InvAmountDue(details.getInvAmountDue())
                        .InvEffectivityDate(details.getInvEffectivityDate())
                        .InvConversionRate(1.0)
                        .InvCreatedBy(details.getInvCreatedBy())
                        .InvDateCreated(details.getInvDateCreated())
                        .InvAdBranch(branchCode)
                        .InvAdCompany(companyCode)
                        .buildInvoice(details.getCompanyShortName());
                LocalArInvoice arCMInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(details.getInvCmInvoiceNumber(), EJBCommon.FALSE, branchCode, companyCode);
                arCreditMemo.setArSalesperson(arCMInvoice.getArSalesperson());

            } else {

                // release lock
                LocalArInvoice arLockedInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arCreditMemo.getInvCmInvoiceNumber(), EJBCommon.FALSE, branchCode, companyCode);

                Collection arLockedInvoicePaymentSchedules = arLockedInvoice.getArInvoicePaymentSchedules();
                for (Object arLockedInvoicePaymentSchedule : arLockedInvoicePaymentSchedules) {
                    LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) arLockedInvoicePaymentSchedule;
                    arInvoicePaymentSchedule.setIpsLock(EJBCommon.FALSE);
                }

                arCreditMemo.setInvType(details.getInvType());
                arCreditMemo.setInvDescription(details.getInvDescription());
                arCreditMemo.setInvDate(details.getInvDate());
                arCreditMemo.setInvNumber(details.getInvNumber());
                arCreditMemo.setInvAmountDue(details.getInvAmountDue());
                arCreditMemo.setInvCmInvoiceNumber(details.getInvCmInvoiceNumber());
                arCreditMemo.setInvCmReferenceNumber(details.getInvCmReferenceNumber());
                arCreditMemo.setInvLastModifiedBy(details.getInvLastModifiedBy());
                arCreditMemo.setInvDateLastModified(details.getInvDateLastModified());
                arCreditMemo.setInvReasonForRejection(null);
                arCreditMemo.setInvSubjectToCommission(details.getInvSubjectToCommission());
                arCreditMemo.setArSalesperson(arLockedInvoice.getArSalesperson());
            }

            arCreditMemo.setReportParameter(details.getReportParameter());

            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(CST_CSTMR_CODE, companyCode);
            arCreditMemo.setArCustomer(arCustomer);

            LocalArTaxCode arTaxCode = arInvoice.getArTaxCode();
            LocalArWithholdingTaxCode arWithholdingTaxCode = arInvoice.getArWithholdingTaxCode();

            try {
                LocalArInvoiceBatch arInvoiceBatch = arInvoiceBatchHome.findByIbName(IB_NM, branchCode, companyCode);
                arInvoiceBatch.addArInvoice(arCreditMemo);
            } catch (FinderException ex) {
            }

            // remove all credit memo lines
            Collection arInvoiceLineItems = arCreditMemo.getArInvoiceLineItems();
            Iterator i = arInvoiceLineItems.iterator();
            while (i.hasNext()) {
                LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) i.next();
                i.remove();
                // arInvoiceLineItem.entityRemove();
                em.remove(arInvoiceLineItem);
            }

            // remove all distribution records
            Collection arDistributionRecords = arCreditMemo.getArDistributionRecords();
            i = arDistributionRecords.iterator();
            while (i.hasNext()) {
                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();
                i.remove();
                // arDistributionRecord.entityRemove();
                em.remove(arDistributionRecord);
            }

            // add new distribution records
            i = drList.iterator();
            while (i.hasNext()) {
                ArModDistributionRecordDetails mDrDetails = (ArModDistributionRecordDetails) i.next();
                if (mDrDetails.getDrClass().equals("RECEIVABLE")) {
                    double invoiceAmountDue = arInvoice.getInvAmountDue();
                    double invoiceAmountPaid = arInvoice.getInvAmountPaid();
                    double cmAmountDue = EJBCommon.roundIt(arCreditMemo.getInvAmountDue(), this.getGlFcPrecisionUnit(companyCode));
                    double invAmountDue = EJBCommon.roundIt((invoiceAmountDue - invoiceAmountPaid), this.getGlFcPrecisionUnit(companyCode));
                    if (cmAmountDue > invAmountDue) {
                        throw new ArINVOverapplicationNotAllowedException();
                    }
                }

                LocalGlChartOfAccount glChartOfAccount = null;
                try {
                    glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(mDrDetails.getDrCoaAccountNumber(), branchCode, companyCode);
                } catch (FinderException ex) {
                    throw new GlobalAccountNumberInvalidException(String.valueOf(mDrDetails.getDrLine()));
                }
                this.addArDrEntry(mDrDetails.getDrLine(), mDrDetails.getDrClass(), mDrDetails.getDrDebit(), mDrDetails.getDrAmount(), glChartOfAccount.getCoaCode(), arCreditMemo, branchCode, companyCode);
            }

            // create new invoice payment schedule lock
            Collection arInvoicePaymentSchedules = arInvoice.getArInvoicePaymentSchedules();
            i = arInvoicePaymentSchedules.iterator();
            while (i.hasNext()) {
                LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) i.next();
                arInvoicePaymentSchedule.setIpsLock(EJBCommon.TRUE);
            }

            // generate approval status
            String INV_APPRVL_STATUS = null;
            if (!isDraft) {
                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);
                // check if ar credit memo approval is enabled
                if (adApproval.getAprEnableArCreditMemo() == EJBCommon.FALSE) {
                    INV_APPRVL_STATUS = "N/A";
                } else {
                    // check if credit memo is self approved
                    LocalAdUser adUser = adUserHome.findByUsrName(details.getInvLastModifiedBy(), companyCode);
                    INV_APPRVL_STATUS = arApprovalController.getApprovalStatus(adUser.getUsrDept(), details.getInvLastModifiedBy(), adUser.getUsrDescription(), "AR CREDIT MEMO", details.getInvCode(), details.getInvNumber(), details.getInvDate(), branchCode, companyCode);
                }
            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            if (INV_APPRVL_STATUS != null && INV_APPRVL_STATUS.equals("N/A") && adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {
                this.executeArInvCreditMemoPost(arCreditMemo.getInvCode(), arCreditMemo.getInvLastModifiedBy(), branchCode, companyCode);
            }

            // set credit memo approval status
            arCreditMemo.setInvApprovalStatus(INV_APPRVL_STATUS);

            return arCreditMemo.getInvCode();

        } catch (GlobalRecordAlreadyDeletedException | GlobalBranchAccountNumberInvalidException |
                 GlobalAccountNumberInvalidException | GlobalJournalNotBalanceException |
                 GlJREffectiveDatePeriodClosedException | GlJREffectiveDateNoPeriodExistException |
                 GlobalNoApprovalApproverFoundException | GlobalNoApprovalRequesterFoundException |
                 GlobalTransactionAlreadyLockedException | ArINVOverapplicationNotAllowedException |
                 GlobalTransactionAlreadyVoidPostedException | GlobalTransactionAlreadyPostedException |
                 GlobalTransactionAlreadyPendingException | GlobalTransactionAlreadyApprovedException |
                 GlobalDocumentNumberNotUniqueException | GlobalNoRecordFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer saveArInvIliEntry(ArInvoiceDetails details, String CST_CSTMR_CODE, String IB_NM, ArrayList iliList, boolean isDraft, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalNoRecordFoundException, GlobalDocumentNumberNotUniqueException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidPostedException, ArINVOverapplicationNotAllowedException, GlobalTransactionAlreadyLockedException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ArCreditMemoEntryControllerBean saveArInvIliEntry");

        LocalArInvoice arCreditMemo = null;
        LocalArInvoice arInvoice = null;

        try {
            // validate if credit memo is already deleted
            try {
                if (details.getInvCode() != null) {
                    arCreditMemo = arInvoiceHome.findByPrimaryKey(details.getInvCode());
                }
            } catch (FinderException ex) {
                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if credit memo is already posted, void, arproved or pending
            if (details.getInvCode() != null && details.getInvVoid() == EJBCommon.FALSE) {
                if (arCreditMemo.getInvApprovalStatus() != null) {
                    if (arCreditMemo.getInvApprovalStatus().equals("APPROVED") || arCreditMemo.getInvApprovalStatus().equals("N/A")) {
                        throw new GlobalTransactionAlreadyApprovedException();
                    } else if (arCreditMemo.getInvApprovalStatus().equals("PENDING")) {
                        throw new GlobalTransactionAlreadyPendingException();
                    }
                }
                if (arCreditMemo.getInvPosted() == EJBCommon.TRUE) {
                    throw new GlobalTransactionAlreadyPostedException();
                } else if (arCreditMemo.getInvVoid() == EJBCommon.TRUE) {
                    throw new GlobalTransactionAlreadyVoidException();
                }
            }

            // invoice void
            if (details.getInvCode() != null && details.getInvVoid() == EJBCommon.TRUE) {
                if (arCreditMemo.getInvVoid() == EJBCommon.TRUE) {
                    throw new GlobalTransactionAlreadyVoidException();
                }

                if (arCreditMemo.getInvPosted() == EJBCommon.TRUE) {

                    // generate approval status
                    String INV_APPRVL_STATUS = null;

                    if (!isDraft) {
                        LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);
                        // check if ar credit memo approval is enabled
                        if (adApproval.getAprEnableArCreditMemo() == EJBCommon.FALSE) {
                            INV_APPRVL_STATUS = "N/A";
                        } else {
                            // check if credit memo is self approved
                            LocalAdUser adUser = adUserHome.findByUsrName(details.getInvLastModifiedBy(), companyCode);
                            INV_APPRVL_STATUS = arApprovalController.getApprovalStatus(adUser.getUsrDept(), details.getInvLastModifiedBy(), adUser.getUsrDescription(), "AR CREDIT MEMO", details.getInvCode(), details.getInvNumber(), details.getInvDate(), branchCode, companyCode);
                        }
                    }

                    // reverse distribution records
                    Collection arDistributionRecords = arCreditMemo.getArDistributionRecords();
                    ArrayList list = new ArrayList();
                    Iterator i = arDistributionRecords.iterator();

                    while (i.hasNext()) {
                        LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();
                        list.add(arDistributionRecord);
                    }
                    i = list.iterator();
                    while (i.hasNext()) {
                        LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();
                        this.addArDrEntry(arCreditMemo.getArDrNextLine(), arDistributionRecord.getDrClass(), arDistributionRecord.getDrDebit() == EJBCommon.TRUE ? EJBCommon.FALSE : EJBCommon.TRUE, arDistributionRecord.getDrAmount(), arDistributionRecord.getGlChartOfAccount().getCoaCode(), arCreditMemo, branchCode, companyCode);
                    }
                    LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
                    if (INV_APPRVL_STATUS != null && INV_APPRVL_STATUS.equals("N/A") && adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {
                        arCreditMemo.setInvVoid(EJBCommon.TRUE);
                        this.executeArInvCreditMemoPost(arCreditMemo.getInvCode(), arCreditMemo.getInvLastModifiedBy(), branchCode, companyCode);
                    }
                    // set credit memo approval status
                    arCreditMemo.setInvVoidApprovalStatus(INV_APPRVL_STATUS);
                } else {
                    LocalArInvoice arLockedInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arCreditMemo.getInvCmInvoiceNumber(), EJBCommon.FALSE, branchCode, companyCode);
                    Collection arLockedInvoicePaymentSchedules = arLockedInvoice.getArInvoicePaymentSchedules();
                    for (Object arLockedInvoicePaymentSchedule : arLockedInvoicePaymentSchedules) {
                        LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) arLockedInvoicePaymentSchedule;
                        arInvoicePaymentSchedule.setIpsLock(EJBCommon.FALSE);
                    }
                }
                arCreditMemo.setInvVoid(EJBCommon.TRUE);
                arCreditMemo.setInvLastModifiedBy(details.getInvLastModifiedBy());
                arCreditMemo.setInvDateLastModified(details.getInvDateLastModified());
                return arCreditMemo.getInvCode();
            }

            // validate if invoice number exists
            try {
                arInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(details.getInvCmInvoiceNumber(), EJBCommon.FALSE, branchCode, companyCode);
                if (arInvoice.getInvPosted() != EJBCommon.TRUE) {
                    throw new GlobalNoRecordFoundException();
                }
            } catch (FinderException ex) {
                throw new GlobalNoRecordFoundException();
            }

            // validate if document number is unique document number is automatic then set
            // next sequence
            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

            if (details.getInvCode() == null) {

                try {

                    adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AR CREDIT MEMO", companyCode);
                } catch (FinderException ex) {
                }

                try {
                    adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode);
                } catch (FinderException ex) {
                }

                LocalArInvoice arExistingCreditMemo = null;
                try {
                    arExistingCreditMemo = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(details.getInvNumber(), EJBCommon.TRUE, branchCode, companyCode);
                } catch (FinderException ex) {
                }

                if (arExistingCreditMemo != null) {
                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (details.getInvNumber() == null || details.getInvNumber().trim().length() == 0)) {
                    while (true) {
                        if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {
                            try {
                                arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), EJBCommon.TRUE, branchCode, companyCode);
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                            } catch (FinderException ex) {

                                details.setInvNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                break;
                            }
                        } else {
                            try {
                                arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), EJBCommon.TRUE, branchCode, companyCode);
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                            } catch (FinderException ex) {
                                details.setInvNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                break;
                            }
                        }
                    }
                }
            } else {

                LocalArInvoice arExistingCreditMemo = null;
                try {
                    arExistingCreditMemo = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(details.getInvNumber(), EJBCommon.TRUE, branchCode, companyCode);
                } catch (FinderException ex) {
                }

                if (arExistingCreditMemo != null && !arExistingCreditMemo.getInvCode().equals(details.getInvCode())) {
                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (arCreditMemo.getInvNumber() != details.getInvNumber() && (details.getInvNumber() == null || details.getInvNumber().trim().length() == 0)) {
                    details.setInvNumber(arCreditMemo.getInvNumber());
                }
            }

            // validate if invoice entered is already locked by cm or receipt
            Collection apValidateInvoicePaymentSchedules = arInvoicePaymentScheduleHome.findByIpsLockAndInvCode(EJBCommon.TRUE, arInvoice.getInvCode(), companyCode);

            if (details.getInvCode() == null && !apValidateInvoicePaymentSchedules.isEmpty() || details.getInvCode() != null && !details.getInvCmInvoiceNumber().equals(arCreditMemo.getInvCmInvoiceNumber()) && !apValidateInvoicePaymentSchedules.isEmpty()) {

                throw new GlobalTransactionAlreadyLockedException();
            }

            // used in checking if credit memo should re-generate distribution records and
            // re-calculate
            // taxes
            boolean isRecalculate = true;

            // create invoice
            if (details.getInvCode() == null) {

                arCreditMemo = arInvoiceHome.create(details.getInvType(), EJBCommon.TRUE, details.getInvDescription(), details.getInvDate(), details.getInvNumber(), details.getInvReferenceNumber(), details.getInvUploadNumber(), details.getInvCmInvoiceNumber(), details.getInvCmReferenceNumber(), 0d, 0d, 0d, 0d, 0d, 0d, details.getInvConversionDate(), details.getInvConversionRate(), details.getInvMemo(), 0d, 0d, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, null, 0d, null, null, null, null, details.getInvCreatedBy(), details.getInvDateCreated(), details.getInvLastModifiedBy(), details.getInvDateLastModified(), null, null, null, null, EJBCommon.FALSE, details.getInvLvShift(), null, null, EJBCommon.FALSE, details.getInvSubjectToCommission(), details.getInvClientPO(), details.getInvEffectivityDate(), branchCode, companyCode);

            } else {

                // release lock
                LocalArInvoice arLockedInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arCreditMemo.getInvCmInvoiceNumber(), EJBCommon.FALSE, branchCode, companyCode);

                Collection arLockedInvoicePaymentSchedules = arLockedInvoice.getArInvoicePaymentSchedules();
                for (Object arLockedInvoicePaymentSchedule : arLockedInvoicePaymentSchedules) {
                    LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) arLockedInvoicePaymentSchedule;
                    arInvoicePaymentSchedule.setIpsLock(EJBCommon.FALSE);
                }

                // check if critical fields are changed
                if (!arCreditMemo.getArCustomer().getCstCustomerCode().equals(CST_CSTMR_CODE) || !arCreditMemo.getInvCmInvoiceNumber().equals(details.getInvCmInvoiceNumber()) || iliList.size() != arCreditMemo.getArInvoiceLineItems().size() || !(arCreditMemo.getInvDate().equals(details.getInvDate()))) {

                    isRecalculate = true;

                } else if (iliList.size() == arCreditMemo.getArInvoiceLineItems().size()) {

                    Iterator ilIter = arCreditMemo.getArInvoiceLineItems().iterator();
                    Iterator ilListIter = iliList.iterator();

                    while (ilIter.hasNext()) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) ilIter.next();
                        ArModInvoiceLineItemDetails mdetails = (ArModInvoiceLineItemDetails) ilListIter.next();

                        if (!arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName().equals(mdetails.getIliIiName()) || !arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName().equals(mdetails.getIliLocName()) || !arInvoiceLineItem.getInvUnitOfMeasure().getUomName().equals(mdetails.getIliUomName()) || arInvoiceLineItem.getIliQuantity() != mdetails.getIliQuantity() || arInvoiceLineItem.getIliUnitPrice() != mdetails.getIliUnitPrice()) {

                            isRecalculate = true;
                            break;
                        }
                    }
                }

                arCreditMemo.setInvType(details.getInvType());
                arCreditMemo.setInvDescription(details.getInvDescription());
                arCreditMemo.setInvDate(details.getInvDate());
                arCreditMemo.setInvNumber(details.getInvNumber());
                arCreditMemo.setInvCmInvoiceNumber(details.getInvCmInvoiceNumber());
                arCreditMemo.setInvCmReferenceNumber(details.getInvCmReferenceNumber());
                arCreditMemo.setInvLastModifiedBy(details.getInvLastModifiedBy());
                arCreditMemo.setInvDateLastModified(details.getInvDateLastModified());
                arCreditMemo.setInvReasonForRejection(null);
                arCreditMemo.setInvLvShift(details.getInvLvShift());
                arCreditMemo.setInvSubjectToCommission(details.getInvSubjectToCommission());
            }

            arCreditMemo.setInvPartialCm(details.getInvPartialCm());
            arCreditMemo.setReportParameter(details.getReportParameter());

            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(CST_CSTMR_CODE, companyCode);
            arCustomer.addArInvoice(arCreditMemo);

            LocalArTaxCode arTaxCode = arInvoice.getArTaxCode();
            arCreditMemo.setArTaxCode(arTaxCode);

            LocalArWithholdingTaxCode arWithholdingTaxCode = arInvoice.getArWithholdingTaxCode();
            arCreditMemo.setArWithholdingTaxCode(arWithholdingTaxCode);

            try {
                LocalArInvoiceBatch arInvoiceBatch = arInvoiceBatchHome.findByIbName(IB_NM, branchCode, companyCode);
                arInvoiceBatch.addArInvoice(arCreditMemo);
            } catch (FinderException ex) {

            }
            double TOTAL_LINE = 0d;
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            if (isRecalculate) {

                // remove all credit memo lines
                Collection arInvoiceLineItems = arCreditMemo.getArInvoiceLineItems();
                Iterator i = arInvoiceLineItems.iterator();
                while (i.hasNext()) {
                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) i.next();
                    i.remove();
                    // arInvoiceLineItem.entityRemove();
                    em.remove(arInvoiceLineItem);
                }

                // remove all distribution records
                Collection arDistributionRecords = arCreditMemo.getArDistributionRecords();
                i = arDistributionRecords.iterator();
                while (i.hasNext()) {
                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();
                    i.remove();
                    // arDistributionRecord.entityRemove();
                    em.remove(arDistributionRecord);
                }

                // add new invoice lines and distribution record
                double TOTAL_TAX = 0d;
                double TOTAL_SALES_ACCOUNT_CREDIT = 0d;

                i = iliList.iterator();
                LocalInvItemLocation invItemLocation = null;

                while (i.hasNext()) {
                    ArModInvoiceLineItemDetails mIliDetails = (ArModInvoiceLineItemDetails) i.next();
                    try {
                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(mIliDetails.getIliLocName(), mIliDetails.getIliIiName(), companyCode);
                    } catch (FinderException ex) {
                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(mIliDetails.getIliLine()));
                    }

                    // start date validation
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(arCreditMemo.getInvDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }

                    LocalArInvoiceLineItem arInvoiceLineItem = this.addArIliEntry(mIliDetails, arCreditMemo, arTaxCode, invItemLocation, arInvoice, branchCode, companyCode);

                    // add cost of sales distribution and inventory
                    double COST = 0d;

                    try {

                        LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arCreditMemo.getInvDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);

                        if (invCosting.getCstRemainingQuantity() <= 0) {
                            COST = Math.abs(invItemLocation.getInvItem().getIiUnitCost());
                        } else {
                            COST = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                        }

                    } catch (FinderException ex) {

                        COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();
                    }

                    double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {

                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arInvoiceLineItem.getInvItemLocation().getIlCode(), branchCode, companyCode);

                    } catch (FinderException ex) {

                    }

                    if (adPreference.getPrfArAutoComputeCogs() == EJBCommon.TRUE && arInvoiceLineItem.getInvItemLocation().getInvItem().getIiNonInventoriable() == EJBCommon.FALSE) {

                        if (adBranchItemLocation != null) {

                            this.addArDrEntry(arCreditMemo.getArDrNextLine(), "COGS", EJBCommon.FALSE, COST * QTY_SLD, adBranchItemLocation.getBilCoaGlCostOfSalesAccount(), arCreditMemo, branchCode, companyCode);

                            this.addArDrEntry(arCreditMemo.getArDrNextLine(), "INVENTORY", EJBCommon.TRUE, COST * QTY_SLD, adBranchItemLocation.getBilCoaGlInventoryAccount(), arCreditMemo, branchCode, companyCode);

                        } else {

                            this.addArDrEntry(arCreditMemo.getArDrNextLine(), "COGS", EJBCommon.FALSE, COST * QTY_SLD, arInvoiceLineItem.getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arCreditMemo, branchCode, companyCode);

                            this.addArDrEntry(arCreditMemo.getArDrNextLine(), "INVENTORY", EJBCommon.TRUE, COST * QTY_SLD, arInvoiceLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(), arCreditMemo, branchCode, companyCode);
                        }
                    }

                    // add revenue/credit distributions
                    if (adBranchItemLocation != null) {

                        if (adBranchItemLocation.getInvItemLocation().getInvItem().getIiServices() == EJBCommon.TRUE) {
                            // this will trigger by services(DEBIT)
                            this.addArDrEntry(arCreditMemo.getArDrNextLine(), "REVENUE", EJBCommon.FALSE, arInvoiceLineItem.getIliAmount(), adBranchItemLocation.getBilCoaGlSalesAccount(), arCreditMemo, branchCode, companyCode);

                            this.addArDrEntry(arCreditMemo.getArDrNextLine(), "OTHER", EJBCommon.TRUE, arInvoiceLineItem.getIliAmount(), adBranchItemLocation.getBilCoaGlSalesReturnAccount(), arCreditMemo, branchCode, companyCode);

                            TOTAL_SALES_ACCOUNT_CREDIT += arInvoiceLineItem.getIliAmount();

                        } else {
                            this.addArDrEntry(arCreditMemo.getArDrNextLine(), "REVENUE", EJBCommon.TRUE, arInvoiceLineItem.getIliAmount(), adBranchItemLocation.getBilCoaGlSalesAccount(), arCreditMemo, branchCode, companyCode);
                        }

                    } else {

                        if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiServices() == EJBCommon.TRUE) {

                            // this will trigger by services(DEBIT)
                            this.addArDrEntry(arCreditMemo.getArDrNextLine(), "REVENUE", EJBCommon.FALSE, arInvoiceLineItem.getIliAmount(), arInvoiceLineItem.getInvItemLocation().getIlGlCoaSalesAccount(), arCreditMemo, branchCode, companyCode);

                            TOTAL_SALES_ACCOUNT_CREDIT += arInvoiceLineItem.getIliAmount();
                        } else {
                            this.addArDrEntry(arCreditMemo.getArDrNextLine(), "REVENUE", EJBCommon.TRUE, arInvoiceLineItem.getIliAmount(), arInvoiceLineItem.getInvItemLocation().getIlGlCoaSalesAccount(), arCreditMemo, branchCode, companyCode);
                        }
                    }

                    TOTAL_LINE += arInvoiceLineItem.getIliAmount();
                    TOTAL_TAX += arInvoiceLineItem.getIliTaxAmount();
                }

                // add tax distribution if necessary
                if (!arTaxCode.getTcType().equals("NONE") && !arTaxCode.getTcType().equals("EXEMPT")) {
                    if (arTaxCode.getTcInterimAccount() == null) {
                        // add branch tax code
                        LocalAdBranchArTaxCode adBranchTaxCode = null;
                        try {
                            //TODO: Review this finder method that is causing finder exception
                            adBranchTaxCode = adBranchArTaxCodeHome.findBtcByTcCodeAndBrCode(arCreditMemo.getArTaxCode().getTcCode(), branchCode, companyCode);

                            this.addArDrEntry(arCreditMemo.getArDrNextLine(), "TAX", EJBCommon.TRUE, TOTAL_TAX, adBranchTaxCode.getBtcGlCoaTaxCode(), arCreditMemo, branchCode, companyCode);

                        } catch (FinderException ex) {
                            this.addArDrEntry(arCreditMemo.getArDrNextLine(), "TAX", EJBCommon.TRUE, TOTAL_TAX, arTaxCode.getGlChartOfAccount().getCoaCode(), arCreditMemo, branchCode, companyCode);
                        }
                    } else {
                        this.addArDrEntry(arCreditMemo.getArDrNextLine(), "DEFERRED TAX", EJBCommon.TRUE, TOTAL_TAX, arTaxCode.getTcInterimAccount(), arCreditMemo, branchCode, companyCode);
                    }
                }

                // add un earned interest
                double UNEARNED_INT_AMOUNT = 0d;
                double TOTAL_DOWN_PAYMENT = arInvoice.getInvDownPayment();

                if (arInvoice.getArCustomer().getCstAutoComputeInterest() == EJBCommon.TRUE && arInvoice.getArCustomer().getCstMonthlyInterestRate() > 0 && arInvoice.getAdPaymentTerm().getPytEnableInterest() == EJBCommon.TRUE && arInvoice.getInvAmountUnearnedInterest() > 0) {

                    try {

                        LocalAdBranchCustomer adBranchCustomer = adBranchCustomerHome.findBcstByCstCodeAndBrCode(arInvoice.getArCustomer().getCstCode(), branchCode, companyCode);

                        UNEARNED_INT_AMOUNT = EJBCommon.roundIt((TOTAL_LINE + TOTAL_TAX - TOTAL_DOWN_PAYMENT) * arInvoice.getAdPaymentTerm().getAdPaymentSchedules().size() * (arInvoice.getArCustomer().getCstMonthlyInterestRate() / 100), this.getGlFcPrecisionUnit(companyCode));

                        this.addArDrIliEntry(arCreditMemo.getArDrNextLine(), "UNINTEREST", EJBCommon.TRUE, UNEARNED_INT_AMOUNT, adBranchCustomer.getBcstGlCoaUnEarnedInterestAccount(), arCreditMemo, branchCode, companyCode);

                        this.addArDrIliEntry(arInvoice.getArDrNextLine(), "RECEIVABLE INTEREST", EJBCommon.FALSE, UNEARNED_INT_AMOUNT, adBranchCustomer.getBcstGlCoaReceivableAccount(), arCreditMemo, branchCode, companyCode);

                    } catch (FinderException ex) {

                    }
                }

                // add wtax distribution if necessary
                double W_TAX_AMOUNT = 0d;

                if (arWithholdingTaxCode.getWtcRate() != 0 && adPreference.getPrfArWTaxRealization().equals("INVOICE")) {

                    W_TAX_AMOUNT = EJBCommon.roundIt(TOTAL_LINE * (arWithholdingTaxCode.getWtcRate() / 100), this.getGlFcPrecisionUnit(companyCode));

                    this.addArDrEntry(arCreditMemo.getArDrNextLine(), "W-TAX", EJBCommon.FALSE, W_TAX_AMOUNT, arWithholdingTaxCode.getGlChartOfAccount().getCoaCode(), arCreditMemo, branchCode, companyCode);
                }

                // add receivable distribution
                try {

                    LocalAdBranchCustomer adBranchCustomer = adBranchCustomerHome.findBcstByCstCodeAndBrCode(arCreditMemo.getArCustomer().getCstCode(), branchCode, companyCode);

                    // this is for Services (DEBIT)
                    this.addArDrEntry(arCreditMemo.getArDrNextLine(), "RECEIVABLE", EJBCommon.FALSE, TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT - TOTAL_SALES_ACCOUNT_CREDIT, adBranchCustomer.getBcstGlCoaReceivableAccount(), arCreditMemo, branchCode, companyCode);

                } catch (FinderException ex) {

                }

                // set invoice amount due
                arCreditMemo.setInvAmountDue(TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT + UNEARNED_INT_AMOUNT);

                if (EJBCommon.roundIt(arCreditMemo.getInvAmountDue(), this.getGlFcPrecisionUnit(companyCode)) > EJBCommon.roundIt(arInvoice.getInvAmountDue() - arInvoice.getInvAmountPaid(), this.getGlFcPrecisionUnit(companyCode))) {

                    Double amountPaidAmountDue = EJBCommon.roundIt(arInvoice.getInvAmountDue() - arInvoice.getInvAmountPaid(), this.getGlFcPrecisionUnit(companyCode));
                    Double amountPaidOnly = EJBCommon.roundIt(arCreditMemo.getInvAmountDue(), this.getGlFcPrecisionUnit(companyCode));

                    throw new ArINVOverapplicationNotAllowedException();
                }

                // create new invoice payment schedule lock
                Collection arInvoicePaymentSchedules = arInvoice.getArInvoicePaymentSchedules();
                i = arInvoicePaymentSchedules.iterator();

                while (i.hasNext()) {
                    LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) i.next();
                    arInvoicePaymentSchedule.setIpsLock(EJBCommon.TRUE);
                }

            } else {

                Iterator i = iliList.iterator();
                LocalInvItemLocation invItemLocation = null;
                while (i.hasNext()) {
                    ArModInvoiceLineItemDetails mIliDetails = (ArModInvoiceLineItemDetails) i.next();

                    try {
                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(mIliDetails.getIliLocName(), mIliDetails.getIliIiName(), companyCode);
                    } catch (FinderException ex) {
                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(mIliDetails.getIliLine()));
                    }

                    // start date validation
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(arCreditMemo.getInvDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }
                }
            }

            // generate approval status
            String INV_APPRVL_STATUS = null;
            if (!isDraft) {
                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);

                // check if ar credit memo approval is enabled
                if (adApproval.getAprEnableArCreditMemo() == EJBCommon.FALSE) {
                    INV_APPRVL_STATUS = "N/A";
                } else {
                    // check if credit memo is self approved
                    LocalAdUser adUser = adUserHome.findByUsrName(details.getInvLastModifiedBy(), companyCode);
                    INV_APPRVL_STATUS = arApprovalController.getApprovalStatus(adUser.getUsrDept(), details.getInvLastModifiedBy(), adUser.getUsrDescription(), "AR CREDIT MEMO", details.getInvCode(), details.getInvNumber(), details.getInvDate(), branchCode, companyCode);
                }
            }

            if (INV_APPRVL_STATUS != null && INV_APPRVL_STATUS.equals("N/A") && adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {
                this.executeArInvCreditMemoPost(arCreditMemo.getInvCode(), arCreditMemo.getInvLastModifiedBy(), branchCode, companyCode);
            }

            // set credit memo approval status
            arCreditMemo.setInvApprovalStatus(INV_APPRVL_STATUS);
            return arCreditMemo.getInvCode();

        } catch (GlobalRecordAlreadyDeletedException | AdPRFCoaGlVarianceAccountNotFoundException |
                 GlobalBranchAccountNumberInvalidException | GlobalInventoryDateException |
                 GlobalJournalNotBalanceException | GlJREffectiveDatePeriodClosedException |
                 GlJREffectiveDateNoPeriodExistException | GlobalNoApprovalApproverFoundException |
                 GlobalNoApprovalRequesterFoundException | GlobalTransactionAlreadyLockedException |
                 ArINVOverapplicationNotAllowedException | GlobalTransactionAlreadyVoidPostedException |
                 GlobalTransactionAlreadyPostedException | GlobalTransactionAlreadyPendingException |
                 GlobalTransactionAlreadyApprovedException | GlobalDocumentNumberNotUniqueException |
                 GlobalNoRecordFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteArInvEntry(Integer INV_CODE, String AD_USR, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException {

        Debug.print("ArCreditMemoEntryControllerBean deleteArInvEntry");


        try {

            LocalArInvoice arCreditMemo = arInvoiceHome.findByPrimaryKey(INV_CODE);

            if (arCreditMemo.getInvApprovalStatus() != null && arCreditMemo.getInvApprovalStatus().equals("PENDING")) {

                Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode("AR CREDIT MEMO", arCreditMemo.getInvCode(), companyCode);

                Iterator i = adApprovalQueues.iterator();

                while (i.hasNext()) {

                    LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) i.next();

                    i.remove();

                    // adApprovalQueue.entityRemove();
                    em.remove(adApprovalQueue);
                }
            }

            // release lock

            LocalArInvoice arInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arCreditMemo.getInvCmInvoiceNumber(), EJBCommon.FALSE, branchCode, companyCode);

            Collection arInvoicePaymentSchedules = arInvoice.getArInvoicePaymentSchedules();

            for (Object invoicePaymentSchedule : arInvoicePaymentSchedules) {

                LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) invoicePaymentSchedule;

                arInvoicePaymentSchedule.setIpsLock(EJBCommon.FALSE);
            }

            adDeleteAuditTrailHome.create("AR CREDIT MEMO", arCreditMemo.getInvDate(), arCreditMemo.getInvNumber(), arCreditMemo.getInvReferenceNumber(), arCreditMemo.getInvAmountDue(), AD_USR, new Date(), companyCode);

            // arCreditMemo.entityRemove();
            em.remove(arCreditMemo);

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

        Debug.print("ArCreditMemoEntryControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdApprovalNotifiedUsersByInvCode(Integer INV_CODE, Integer companyCode) {

        Debug.print("ArCreditMemoEntryControllerBean getAdApprovalNotifiedUsersByInvCode");

        ArrayList list = new ArrayList();

        try {

            LocalArInvoice arInvoice = arInvoiceHome.findByPrimaryKey(INV_CODE);

            if (arInvoice.getInvPosted() == EJBCommon.TRUE) {

                list.add("DOCUMENT POSTED");
                return list;
            }

            Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode("AR CREDIT MEMO", INV_CODE, companyCode);

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

    public byte getAdPrfEnableArCreditMemoBatch(Integer companyCode) {

        Debug.print("ArCreditMemoEntryControllerBean getAdPrfEnableArCreditMemoBatch");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfEnableArInvoiceBatch();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArOpenIbAll(Integer branchCode, Integer companyCode) {

        Debug.print("ArCreditMemoEntryControllerBean getArOpenIbAll");

        ArrayList list = new ArrayList();

        try {

            Collection arInvoiceBatches = arInvoiceBatchHome.findOpenIbByIbType("CREDIT MEMO", branchCode, companyCode);

            for (Object invoiceBatch : arInvoiceBatches) {

                LocalArInvoiceBatch arInvoiceBatch = (LocalArInvoiceBatch) invoiceBatch;

                list.add(arInvoiceBatch.getIbName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArSoInvLnItmByArInvNmbr(String AR_INV_NMBR, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArCreditMemoEntryControllerBean getArSoInvLnItmByArInvNmbr");

        try {
            ArrayList iliList = new ArrayList();

            LocalArInvoice arInvoice = null;
            Collection ar_so_inv_ln;
            try {
                arInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(AR_INV_NMBR, EJBCommon.FALSE, branchCode, companyCode);
                ar_so_inv_ln = arSalesOrderInvoiceLineHome.findByInvCode(arInvoice.getInvCode(), companyCode);
            } catch (Exception ex) {
                throw new GlobalNoRecordFoundException();
            }

            for (Object o : ar_so_inv_ln) {

                LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) o;

                ArModInvoiceLineItemDetails mdetails = new ArModInvoiceLineItemDetails();

                mdetails.setIliCode(arSalesOrderInvoiceLine.getSilCode());
                mdetails.setIliLine(arSalesOrderInvoiceLine.getArSalesOrderLine().getSolLine());
                mdetails.setIliIiDescription(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvItem().getIiDescription());
                mdetails.setIliIiName(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvItem().getIiName());
                mdetails.setIliLocName(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvLocation().getLocName());
                mdetails.setIliQuantity(arSalesOrderInvoiceLine.getSilQuantityDelivered());
                mdetails.setIliUomName(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvUnitOfMeasure().getUomName());
                mdetails.setIliUnitPrice(arSalesOrderInvoiceLine.getArSalesOrderLine().getSolUnitPrice());
                mdetails.setIliTax(arSalesOrderInvoiceLine.getSilTax());
                mdetails.setIliAmount(arSalesOrderInvoiceLine.getArInvoice().getArTaxCode().getTcName().contains("EXCLUSIVE") ? arSalesOrderInvoiceLine.getSilAmount() : arSalesOrderInvoiceLine.getSilAmount() + arSalesOrderInvoiceLine.getSilTaxAmount());

                mdetails.setIliMisc(arSalesOrderInvoiceLine.getArSalesOrderLine().getSolMisc());
                mdetails.setIliTaxCodeName(arSalesOrderInvoiceLine.getArInvoice().getArTaxCode().getTcName());
                mdetails.setIliWTaxCodeName(arSalesOrderInvoiceLine.getArInvoice().getArTaxCode().getTcName());
                mdetails.setIliIiClass(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvItem().getIiClass());

                iliList.add(mdetails);
            }

            return iliList;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArJoInvLnItmByArInvNmbr(String AR_INV_NMBR, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArCreditMemoEntryControllerBean getArJoInvLnItmByArInvNmbr");

        try {
            ArrayList iliList = new ArrayList();

            LocalArInvoice arInvoice = null;
            Collection ar_jo_inv_ln;
            try {
                arInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(AR_INV_NMBR, EJBCommon.FALSE, branchCode, companyCode);
                ar_jo_inv_ln = arJobOrderInvoiceLineHome.findByInvCode(arInvoice.getInvCode(), companyCode);
            } catch (Exception ex) {
                throw new GlobalNoRecordFoundException();
            }

            for (Object o : ar_jo_inv_ln) {

                LocalArJobOrderInvoiceLine arJobOrderInvoiceLine = (LocalArJobOrderInvoiceLine) o;

                ArModInvoiceLineItemDetails mdetails = new ArModInvoiceLineItemDetails();

                mdetails.setIliCode(arJobOrderInvoiceLine.getJilCode());
                mdetails.setIliLine(arJobOrderInvoiceLine.getArJobOrderLine().getJolLine());
                mdetails.setIliIiDescription(arJobOrderInvoiceLine.getArJobOrderLine().getInvItemLocation().getInvItem().getIiDescription());
                mdetails.setIliIiName(arJobOrderInvoiceLine.getArJobOrderLine().getInvItemLocation().getInvItem().getIiName());
                mdetails.setIliLocName(arJobOrderInvoiceLine.getArJobOrderLine().getInvItemLocation().getInvLocation().getLocName());
                mdetails.setIliQuantity(arJobOrderInvoiceLine.getJilQuantityDelivered());
                mdetails.setIliUomName(arJobOrderInvoiceLine.getArJobOrderLine().getInvUnitOfMeasure().getUomName());
                mdetails.setIliUnitPrice(arJobOrderInvoiceLine.getArJobOrderLine().getJolUnitPrice());
                mdetails.setIliTax(arJobOrderInvoiceLine.getJilTax());
                mdetails.setIliAmount(arJobOrderInvoiceLine.getArInvoice().getArTaxCode().getTcName().contains("EXCLUSIVE") ? arJobOrderInvoiceLine.getJilAmount() : arJobOrderInvoiceLine.getJilAmount() + arJobOrderInvoiceLine.getJilTaxAmount());

                mdetails.setIliMisc(arJobOrderInvoiceLine.getArJobOrderLine().getJolMisc());
                mdetails.setIliTaxCodeName(arJobOrderInvoiceLine.getArInvoice().getArTaxCode().getTcName());
                mdetails.setIliWTaxCodeName(arJobOrderInvoiceLine.getArInvoice().getArTaxCode().getTcName());
                mdetails.setIliIiClass(arJobOrderInvoiceLine.getArJobOrderLine().getInvItemLocation().getInvItem().getIiClass());

                iliList.add(mdetails);
            }

            return iliList;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public ArModInvoiceDetails getArInvByArInvNmbr(String AR_INV_NMBR, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArCreditMemoEntryControllerBean getArInvByArInvNmbr");

        try {
            ArrayList iliList = new ArrayList();
            LocalArInvoice arInvoice = null;
            try {
                arInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(AR_INV_NMBR, EJBCommon.FALSE, branchCode, companyCode);
            } catch (Exception ex) {
                throw new GlobalNoRecordFoundException();
            }
            ArModInvoiceDetails mInvDetails = new ArModInvoiceDetails();

            mInvDetails.setInvType(arInvoice.getInvType());
            mInvDetails.setInvCode(arInvoice.getInvCode());
            mInvDetails.setInvDescription(arInvoice.getInvDescription());
            mInvDetails.setInvDate(arInvoice.getInvDate());
            mInvDetails.setInvNumber(arInvoice.getInvNumber());
            mInvDetails.setInvCmInvoiceNumber(arInvoice.getInvCmInvoiceNumber());
            mInvDetails.setInvCmReferenceNumber(arInvoice.getInvCmReferenceNumber());
            mInvDetails.setInvAmountDue(arInvoice.getInvAmountDue());
            mInvDetails.setInvApprovalStatus(arInvoice.getInvApprovalStatus());
            mInvDetails.setInvVoidApprovalStatus(arInvoice.getInvVoidApprovalStatus());
            mInvDetails.setInvReasonForRejection(arInvoice.getInvReasonForRejection());
            mInvDetails.setInvPosted(arInvoice.getInvPosted());
            mInvDetails.setInvVoidPosted(arInvoice.getInvVoidPosted());
            mInvDetails.setInvVoid(arInvoice.getInvVoid());
            mInvDetails.setInvCreatedBy(arInvoice.getInvCreatedBy());
            mInvDetails.setInvDateCreated(arInvoice.getInvDateCreated());
            mInvDetails.setInvLastModifiedBy(arInvoice.getInvLastModifiedBy());
            mInvDetails.setInvDateLastModified(arInvoice.getInvDateLastModified());
            mInvDetails.setInvApprovedRejectedBy(arInvoice.getInvApprovedRejectedBy());
            mInvDetails.setInvDateApprovedRejected(arInvoice.getInvDateApprovedRejected());
            mInvDetails.setInvPostedBy(arInvoice.getInvPostedBy());
            mInvDetails.setInvDatePosted(arInvoice.getInvDatePosted());
            mInvDetails.setInvCstCustomerCode(arInvoice.getArCustomer().getCstCustomerCode());
            mInvDetails.setInvIbName(arInvoice.getArInvoiceBatch() != null ? arInvoice.getArInvoiceBatch().getIbName() : null);
            mInvDetails.setInvLvShift(arInvoice.getInvLvShift());
            mInvDetails.setInvSubjectToCommission(arInvoice.getInvSubjectToCommission());
            mInvDetails.setInvCstName(arInvoice.getArCustomer().getCstName());
            return mInvDetails;

        } catch (GlobalNoRecordFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArInvLnItmByArInvNmbr(String AR_INV_NMBR, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArCreditMemoEntryControllerBean getArInvLnItmByArInvNmbr");

        try {
            ArrayList iliList = new ArrayList();

            LocalArInvoice arInvoice = null;
            Collection ar_inv_ln_itm;
            try {
                arInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(AR_INV_NMBR, EJBCommon.FALSE, branchCode, companyCode);
                ar_inv_ln_itm = arInvoiceLineItemHome.findByInvCode(arInvoice.getInvCode(), companyCode);
            } catch (Exception ex) {
                throw new GlobalNoRecordFoundException();
            }

            for (Object o : ar_inv_ln_itm) {
                LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) o;

                ArModInvoiceLineItemDetails mdetails = new ArModInvoiceLineItemDetails();

                mdetails.setIliCode(arInvoiceLineItem.getIliCode());
                mdetails.setIliLine(arInvoiceLineItem.getIliLine());
                mdetails.setIliIiDescription(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiDescription());
                mdetails.setIliIiName(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                mdetails.setIliLocName(arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName());
                mdetails.setIliQuantity(arInvoiceLineItem.getIliQuantity());
                mdetails.setIliUomName(arInvoiceLineItem.getInvUnitOfMeasure().getUomName());
                mdetails.setIliUnitPrice(arInvoiceLineItem.getIliUnitPrice());
                mdetails.setIliTax(arInvoiceLineItem.getIliTax());
                mdetails.setIliAmount(arInvoiceLineItem.getArInvoice().getArTaxCode().getTcName().contains("EXCLUSIVE") ? arInvoiceLineItem.getIliAmount() : arInvoiceLineItem.getIliAmount() + arInvoiceLineItem.getIliTaxAmount());
                mdetails.setIliMisc(arInvoiceLineItem.getIliMisc());
                mdetails.setIliTaxCodeName(arInvoiceLineItem.getArInvoice().getArTaxCode().getTcName());
                mdetails.setIliWTaxCodeName(arInvoiceLineItem.getArInvoice().getArTaxCode().getTcName());
                mdetails.setIliIiClass(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiClass());

                iliList.add(mdetails);
            }

            return iliList;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArDrByInvCmInvoiceNumberAndInvBillAmountAndCstCustomerCode(String INV_CM_INVC_NMBR, double INV_BLL_AMNT, String CST_CSTMR_CODE, Integer companyCode) throws GlobalNoRecordFoundException, ArINVOverapplicationNotAllowedException {

        Debug.print("ArCreditMemoEntryControllerBean getArDrByInvCmInvoiceNumberAndInvBillAmountAndCstCustomerCode");

        ArrayList list = new ArrayList();

        try {

            LocalArInvoice arInvoice = null;

            short precisionUnit = this.getGlFcPrecisionUnit(companyCode);

            // validate if invoice exist or overapplied

            try {

                arInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndCstCustomerCode(INV_CM_INVC_NMBR, EJBCommon.FALSE, CST_CSTMR_CODE, companyCode);
                if (arInvoice.getInvPosted() != EJBCommon.TRUE) {
                    throw new GlobalNoRecordFoundException();
                } else if (INV_BLL_AMNT > EJBCommon.roundIt(arInvoice.getInvAmountDue() - arInvoice.getInvAmountPaid(), precisionUnit)) {
                    throw new ArINVOverapplicationNotAllowedException();
                }
            } catch (FinderException ex) {
                throw new GlobalNoRecordFoundException();
            }

            // get amount percent
            double AMOUNT_PERCENT = INV_BLL_AMNT / arInvoice.getInvAmountDue();
            Collection arDistributionRecords = arInvoice.getArDistributionRecords();

            // get total debit and credit for rounding difference calculation
            double TOTAL_DEBIT = 0d;
            double TOTAL_CREDIT = 0d;
            boolean isRoundingDifferenceCalculated = false;

            Iterator i = arDistributionRecords.iterator();

            while (i.hasNext()) {

                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

                if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                    TOTAL_DEBIT += EJBCommon.roundIt(arDistributionRecord.getDrAmount() * AMOUNT_PERCENT, precisionUnit);

                } else {

                    TOTAL_CREDIT += EJBCommon.roundIt(arDistributionRecord.getDrAmount() * AMOUNT_PERCENT, precisionUnit);
                }
            }

            // get default debit memo lines
            short lineNumber = 1;
            i = arDistributionRecords.iterator();
            while (i.hasNext()) {
                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();
                ArModDistributionRecordDetails mdetails = new ArModDistributionRecordDetails();
                mdetails.setDrLine(lineNumber++);
                mdetails.setDrClass(arDistributionRecord.getDrClass());
                mdetails.setDrDebit(arDistributionRecord.getDrDebit() == EJBCommon.TRUE ? EJBCommon.FALSE : EJBCommon.TRUE);

                double DR_AMNT = EJBCommon.roundIt(arDistributionRecord.getDrAmount() * AMOUNT_PERCENT, precisionUnit);

                // calculate rounding difference if necessary

                if (arDistributionRecord.getDrDebit() == EJBCommon.FALSE && TOTAL_DEBIT != TOTAL_CREDIT && !isRoundingDifferenceCalculated) {

                    DR_AMNT = DR_AMNT + TOTAL_DEBIT - TOTAL_CREDIT;

                    isRoundingDifferenceCalculated = true;
                }

                mdetails.setDrAmount(DR_AMNT);
                mdetails.setDrCoaAccountNumber(arDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                mdetails.setDrCoaAccountDescription(arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());

                list.add(mdetails);
            }

            return list;

        } catch (GlobalNoRecordFoundException | ArINVOverapplicationNotAllowedException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public String getInvItemClassByIiName(String II_NM, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArCreditMemoEntryControllerBean getInvItemClassByIiName");

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

    public String getArCstCustomerCodeByArInvNmbr(String AR_INV_NMBR, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArCreditMemoEntryControllerBean getArCstCodeByArInvNmbr");

        try {

            LocalArInvoice arInvoice = null;

            try {

                arInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(AR_INV_NMBR, EJBCommon.FALSE, branchCode, companyCode);

                return arInvoice.getArCustomer().getCstCustomerCode();

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
        // findByInvNumberAndInvCreditMemoAndBrCode
    }

    public String getArCstNameByCstCustomerCode(String CST_CSTMR_CODE, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArCreditMemoEntryControllerBean getArCstNameByCstCustomerCode");

        try {

            LocalArCustomer arCustomer = null;

            try {

                arCustomer = arCustomerHome.findByCstCustomerCode(CST_CSTMR_CODE, companyCode);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            return arCustomer.getCstName();

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods

    private void addArDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE, LocalArInvoice arInvoice, Integer branchCode, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArCreditMemoEntryControllerBean addArDrEntry");

        try {
            // get company
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, branchCode, companyCode);
            // create distribution record
            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
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

    private void executeArInvCreditMemoPost(Integer INV_CODE, String USR_NM, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ArCreditMemoEntryControllerBean executeArInvCreditMemoPost");

        LocalArInvoice arCreditMemo = null;
        LocalArInvoice arCreditedInvoice = null;

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            // validate if invoice/credit memo is already deleted

            try {

                arCreditMemo = arInvoiceHome.findByPrimaryKey(INV_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if invoice/credit memo is already posted or void

            if (arCreditMemo.getInvVoid() == EJBCommon.FALSE && arCreditMemo.getInvPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();

            } else if (arCreditMemo.getInvVoid() == EJBCommon.TRUE && arCreditMemo.getInvVoidPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyVoidPostedException();
            }

            // regenerate inventory dr
            // if (!arCreditMemo.getArInvoiceLineItems().isEmpty())
            // this.regenerateInventoryDr(arCreditMemo, branchCode, companyCode);

            // post invoice/credit memo
            if (arCreditMemo.getInvVoid() == EJBCommon.TRUE && arCreditMemo.getInvVoidPosted() == EJBCommon.FALSE) {

                // get credited invoice
                arCreditedInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arCreditMemo.getInvCmInvoiceNumber(), EJBCommon.FALSE, branchCode, companyCode);

                // increase customer balance
                double INV_AMNT = this.convertForeignToFunctionalCurrency(arCreditedInvoice.getGlFunctionalCurrency().getFcCode(), arCreditedInvoice.getGlFunctionalCurrency().getFcName(), arCreditedInvoice.getInvConversionDate(), arCreditedInvoice.getInvConversionRate(), arCreditMemo.getInvAmountDue(), companyCode);

                this.post(arCreditMemo.getInvDate(), INV_AMNT, arCreditMemo.getArCustomer(), companyCode);

                // decrease invoice and ips amounts and release lock

                double CREDIT_PERCENT = EJBCommon.roundIt(arCreditMemo.getInvAmountDue() / arCreditedInvoice.getInvAmountDue(), (short) 6);

                arCreditedInvoice.setInvAmountPaid(arCreditedInvoice.getInvAmountPaid() - arCreditMemo.getInvAmountDue());

                double TOTAL_INVOICE_PAYMENT_SCHEDULE = 0d;

                Collection arInvoicePaymentSchedules = arCreditedInvoice.getArInvoicePaymentSchedules();

                Iterator i = arInvoicePaymentSchedules.iterator();

                while (i.hasNext()) {

                    LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) i.next();

                    double INVOICE_PAYMENT_SCHEDULE_AMOUNT = 0;

                    // if last payment schedule subtract to avoid rounding difference error

                    if (i.hasNext()) {

                        INVOICE_PAYMENT_SCHEDULE_AMOUNT = EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsAmountDue() * CREDIT_PERCENT, this.getGlFcPrecisionUnit(companyCode));

                    } else {

                        INVOICE_PAYMENT_SCHEDULE_AMOUNT = arCreditMemo.getInvAmountDue() - TOTAL_INVOICE_PAYMENT_SCHEDULE;
                    }

                    arInvoicePaymentSchedule.setIpsAmountPaid(arInvoicePaymentSchedule.getIpsAmountPaid() - INVOICE_PAYMENT_SCHEDULE_AMOUNT);

                    arInvoicePaymentSchedule.setIpsLock(EJBCommon.FALSE);

                    TOTAL_INVOICE_PAYMENT_SCHEDULE += INVOICE_PAYMENT_SCHEDULE_AMOUNT;
                }

                Collection arInvoiceLineItems = arCreditMemo.getArInvoiceLineItems();

                if (arInvoiceLineItems != null && !arInvoiceLineItems.isEmpty()) {

                    for (Object invoiceLineItem : arInvoiceLineItems) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) invoiceLineItem;

                        String II_NM = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName();
                        String LOC_NM = arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName();

                        double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                        LocalInvCosting invCosting = null;

                        try {

                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arCreditMemo.getInvDate(), II_NM, LOC_NM, branchCode, companyCode);

                        } catch (FinderException ex) {

                        }

                        double COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                        if (invCosting == null) {

                            this.postToInv(arInvoiceLineItem, arCreditMemo.getInvDate(), QTY_SLD, COST * QTY_SLD, -QTY_SLD, -COST * QTY_SLD, 0d, null, branchCode, companyCode);

                        } else {

                            switch (invCosting.getInvItemLocation().getInvItem().getIiCostMethod()) {
                                case "Average":

                                    double avgCost = invCosting.getCstRemainingQuantity() <= 0 ? COST : Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                                    this.postToInv(arInvoiceLineItem, arCreditMemo.getInvDate(), QTY_SLD, avgCost * QTY_SLD, invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (avgCost * QTY_SLD), 0d, null, branchCode, companyCode);

                                    break;
                                case "FIFO":

                                    double fifoCost = invCosting.getCstRemainingQuantity() == 0 ? COST : this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), QTY_SLD, arInvoiceLineItem.getIliUnitPrice(), true, branchCode, companyCode);

                                    this.postToInv(arInvoiceLineItem, arCreditMemo.getInvDate(), QTY_SLD, fifoCost * QTY_SLD, invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (fifoCost * QTY_SLD), 0d, null, branchCode, companyCode);

                                    break;
                                case "Standard":

                                    double standardCost = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();

                                    this.postToInv(arInvoiceLineItem, arCreditMemo.getInvDate(), QTY_SLD, standardCost * QTY_SLD, invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (standardCost * QTY_SLD), 0d, null, branchCode, companyCode);
                                    break;
                            }
                        }
                    }
                }

                // set cmAdjustment post status

                arCreditMemo.setInvVoidPosted(EJBCommon.TRUE);
                arCreditMemo.setInvPostedBy(USR_NM);
                arCreditMemo.setInvDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            } else if (arCreditMemo.getInvVoid() == EJBCommon.FALSE && arCreditMemo.getInvPosted() == EJBCommon.FALSE) {

                // get credited invoice
                arCreditedInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arCreditMemo.getInvCmInvoiceNumber(), EJBCommon.FALSE, branchCode, companyCode);

                // decrease customer balance

                double INV_AMNT = this.convertForeignToFunctionalCurrency(arCreditedInvoice.getGlFunctionalCurrency().getFcCode(), arCreditedInvoice.getGlFunctionalCurrency().getFcName(), arCreditedInvoice.getInvConversionDate(), arCreditedInvoice.getInvConversionRate(), arCreditMemo.getInvAmountDue(), companyCode);

                this.post(arCreditMemo.getInvDate(), -INV_AMNT, arCreditMemo.getArCustomer(), companyCode);

                // decrease invoice and ips amounts and release lock

                double CREDIT_PERCENT = EJBCommon.roundIt(arCreditMemo.getInvAmountDue() / arCreditedInvoice.getInvAmountDue(), (short) 6);

                arCreditedInvoice.setInvAmountPaid(arCreditedInvoice.getInvAmountPaid() + arCreditMemo.getInvAmountDue());

                double TOTAL_INVOICE_PAYMENT_SCHEDULE = 0d;

                Collection arInvoicePaymentSchedules = arCreditedInvoice.getArInvoicePaymentSchedules();

                Iterator i = arInvoicePaymentSchedules.iterator();

                while (i.hasNext()) {

                    LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) i.next();

                    double INVOICE_PAYMENT_SCHEDULE_AMOUNT = 0;

                    // if last payment schedule subtract to avoid rounding difference error

                    if (i.hasNext()) {

                        INVOICE_PAYMENT_SCHEDULE_AMOUNT = EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsAmountDue() * CREDIT_PERCENT, this.getGlFcPrecisionUnit(companyCode));

                    } else {

                        INVOICE_PAYMENT_SCHEDULE_AMOUNT = arCreditMemo.getInvAmountDue() - TOTAL_INVOICE_PAYMENT_SCHEDULE;
                    }

                    arInvoicePaymentSchedule.setIpsAmountPaid(arInvoicePaymentSchedule.getIpsAmountPaid() + INVOICE_PAYMENT_SCHEDULE_AMOUNT);

                    arInvoicePaymentSchedule.setIpsLock(EJBCommon.FALSE);

                    TOTAL_INVOICE_PAYMENT_SCHEDULE += INVOICE_PAYMENT_SCHEDULE_AMOUNT;
                }

                Collection arInvoiceLineItems = arCreditMemo.getArInvoiceLineItems();

                if (arInvoiceLineItems != null && !arInvoiceLineItems.isEmpty()) {

                    for (Object invoiceLineItem : arInvoiceLineItems) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) invoiceLineItem;

                        String II_NM = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName();
                        String LOC_NM = arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName();

                        double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                        LocalInvCosting invCosting = null;

                        try {

                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arCreditMemo.getInvDate(), II_NM, LOC_NM, branchCode, companyCode);

                        } catch (FinderException ex) {
                        }

                        double COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                        if (invCosting == null) {

                            this.postToInv(arInvoiceLineItem, arCreditMemo.getInvDate(), -QTY_SLD, -COST * QTY_SLD, QTY_SLD, COST * QTY_SLD, 0d, null, branchCode, companyCode);

                        } else {

                            switch (invCosting.getInvItemLocation().getInvItem().getIiCostMethod()) {
                                case "Average":

                                    double avgCost = invCosting.getCstRemainingQuantity() <= 0 ? COST : Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                                    this.postToInv(arInvoiceLineItem, arCreditMemo.getInvDate(), -QTY_SLD, -avgCost * QTY_SLD, invCosting.getCstRemainingQuantity() + QTY_SLD, invCosting.getCstRemainingValue() + (avgCost * QTY_SLD), 0d, null, branchCode, companyCode);

                                    break;
                                case "FIFO":

                                    double fifoCost = invCosting.getCstRemainingQuantity() == 0 ? COST : this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), QTY_SLD, arInvoiceLineItem.getIliUnitPrice() * QTY_SLD, true, branchCode, companyCode);

                                    // post entries to database
                                    this.postToInv(arInvoiceLineItem, arCreditMemo.getInvDate(), -QTY_SLD, -fifoCost * QTY_SLD, invCosting.getCstRemainingQuantity() + QTY_SLD, invCosting.getCstRemainingValue() + (fifoCost * QTY_SLD), 0d, null, branchCode, companyCode);

                                    break;
                                case "Standard":
                                    double standardCost = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();

                                    // post entries to database
                                    this.postToInv(arInvoiceLineItem, arCreditMemo.getInvDate(), -QTY_SLD, -standardCost * QTY_SLD, invCosting.getCstRemainingQuantity() + QTY_SLD, invCosting.getCstRemainingValue() + (standardCost * QTY_SLD), 0d, null, branchCode, companyCode);
                                    break;
                            }
                        }
                    }
                }
            }

            // set invoice post status

            arCreditMemo.setInvPosted(EJBCommon.TRUE);
            arCreditMemo.setInvPostedBy(USR_NM);
            arCreditMemo.setInvDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            // post to gl if necessary

            if (adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                // validate if date has no period and period is closed

                LocalGlSetOfBook glJournalSetOfBook = null;

                try {

                    glJournalSetOfBook = glSetOfBookHome.findByDate(arCreditMemo.getInvDate(), companyCode);

                } catch (FinderException ex) {

                    throw new GlJREffectiveDateNoPeriodExistException();
                }

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), arCreditMemo.getInvDate(), companyCode);

                if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P') {

                    throw new GlJREffectiveDatePeriodClosedException();
                }

                // check if invoice is balance if not check suspense posting

                LocalGlJournalLine glOffsetJournalLine = null;

                Collection arDistributionRecords = arDistributionRecordHome.findImportableDrByInvCode(arCreditMemo.getInvCode(), companyCode);

                Iterator j = arDistributionRecords.iterator();

                double TOTAL_DEBIT = 0d;
                double TOTAL_CREDIT = 0d;

                while (j.hasNext()) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) j.next();

                    if (arDistributionRecord.getDrClass().equals("COGS") || arDistributionRecord.getDrClass().equals("INVENTORY")) {
                        continue;
                    }

                    double DR_AMNT = 0d;

                    if (arCreditMemo.getInvCreditMemo() == EJBCommon.FALSE) {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arCreditMemo.getGlFunctionalCurrency().getFcCode(), arCreditMemo.getGlFunctionalCurrency().getFcName(), arCreditMemo.getInvConversionDate(), arCreditMemo.getInvConversionRate(), arDistributionRecord.getDrAmount(), companyCode);

                    } else {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arCreditedInvoice.getGlFunctionalCurrency().getFcCode(), arCreditedInvoice.getGlFunctionalCurrency().getFcName(), arCreditedInvoice.getInvConversionDate(), arCreditedInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), companyCode);
                    }

                    if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

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

                        glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("ACCOUNTS RECEIVABLES", arCreditMemo.getInvCreditMemo() == EJBCommon.FALSE ? "SALES INVOICES" : "CREDIT MEMOS", companyCode);

                    } catch (FinderException ex) {

                        throw new GlobalJournalNotBalanceException();
                    }

                    if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                        glOffsetJournalLine = glJournalLineHome.create((short) (arDistributionRecords.size() + 1), EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);

                    } else {

                        glOffsetJournalLine = glJournalLineHome.create((short) (arDistributionRecords.size() + 1), EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
                    }

                    LocalGlChartOfAccount glChartOfAccount = glSuspenseAccount.getGlChartOfAccount();
                    glChartOfAccount.addGlJournalLine(glOffsetJournalLine);

                } else if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE && TOTAL_DEBIT != TOTAL_CREDIT) {

                    throw new GlobalJournalNotBalanceException();
                }

                // create journal batch if necessary

                LocalGlJournalBatch glJournalBatch = null;
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");

                try {

                    if (adPreference.getPrfEnableArInvoiceBatch() == EJBCommon.TRUE) {

                        glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " " + arCreditMemo.getArInvoiceBatch().getIbName(), branchCode, companyCode);

                    } else {

                        glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " SALES INVOICES", branchCode, companyCode);
                    }

                } catch (FinderException ex) {
                }

                if (adPreference.getPrfEnableGlJournalBatch() == EJBCommon.TRUE && glJournalBatch == null) {

                    if (adPreference.getPrfEnableArInvoiceBatch() == EJBCommon.TRUE) {

                        glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " " + arCreditMemo.getArInvoiceBatch().getIbName(), "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, branchCode, companyCode);

                    } else {

                        glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " SALES INVOICES", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, branchCode, companyCode);
                    }
                }

                // create journal entry
                String journalDesc = arCreditMemo.getInvVoid() == (byte) 1 ? " VOID" : " POST";

                LocalGlJournal glJournal = glJournalHome.create(arCreditMemo.getInvCmInvoiceNumber(), arCreditMemo.getInvDescription() + journalDesc, arCreditMemo.getInvDate(), 0.0d, null, arCreditMemo.getInvNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM, EJBCommon.getGcCurrentDateWoTime().getTime(), arCreditMemo.getArCustomer().getCstTin(), arCreditMemo.getArCustomer().getCstName(), EJBCommon.FALSE, null, branchCode, companyCode);

                LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("ACCOUNTS RECEIVABLES", companyCode);
                glJournal.setGlJournalSource(glJournalSource);

                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

                LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName(arCreditMemo.getInvCreditMemo() == EJBCommon.FALSE ? "SALES INVOICES" : "CREDIT MEMOS", companyCode);
                glJournal.setGlJournalCategory(glJournalCategory);

                if (glJournalBatch != null) {

                    glJournal.setGlJournalBatch(glJournalBatch);
                }

                // create journal lines

                j = arDistributionRecords.iterator();

                while (j.hasNext()) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) j.next();

                    double DR_AMNT = 0d;

                    if (arCreditMemo.getInvCreditMemo() == EJBCommon.FALSE) {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arCreditMemo.getGlFunctionalCurrency().getFcCode(), arCreditMemo.getGlFunctionalCurrency().getFcName(), arCreditMemo.getInvConversionDate(), arCreditMemo.getInvConversionRate(), arDistributionRecord.getDrAmount(), companyCode);

                    } else {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arCreditedInvoice.getGlFunctionalCurrency().getFcCode(), arCreditedInvoice.getGlFunctionalCurrency().getFcName(), arCreditedInvoice.getInvConversionDate(), arCreditedInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), companyCode);
                    }

                    LocalGlJournalLine glJournalLine = glJournalLineHome.create(arDistributionRecord.getDrLine(), arDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

                    arDistributionRecord.getGlChartOfAccount().addGlJournalLine(glJournalLine);

                    glJournal.addGlJournalLine(glJournalLine);

                    arDistributionRecord.setDrImported(EJBCommon.TRUE);
                }

                if (glOffsetJournalLine != null) {

                    glJournal.addGlJournalLine(glOffsetJournalLine);
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
            }

        } catch (GlJREffectiveDateNoPeriodExistException | AdPRFCoaGlVarianceAccountNotFoundException |
                 GlobalTransactionAlreadyVoidPostedException | GlobalTransactionAlreadyPostedException |
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

    private double getInvFifoCost(Date CST_DT, Integer IL_CODE, double CST_QTY, double CST_COST, boolean isAdjustFifo, Integer branchCode, Integer companyCode) {

        try {

            Collection invFifoCostings = invCostingHome.findFifoRemainingQuantityByLessThanOrEqualCstDateAndIlCodeAndBrCode(CST_DT, IL_CODE, branchCode, companyCode);

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

    private void post(Date INV_DT, double INV_AMNT, LocalArCustomer arCustomer, Integer companyCode) {

        Debug.print("ArCreditMemoEntryControllerBean post");

        try {

            // find customer balance before or equal invoice date

            Collection arCustomerBalances = arCustomerBalanceHome.findByBeforeOrEqualInvDateAndCstCustomerCode(INV_DT, arCustomer.getCstCustomerCode(), companyCode);

            if (!arCustomerBalances.isEmpty()) {

                // get last invoice

                ArrayList arCustomerBalanceList = new ArrayList(arCustomerBalances);

                LocalArCustomerBalance arCustomerBalance = (LocalArCustomerBalance) arCustomerBalanceList.get(arCustomerBalanceList.size() - 1);

                if (arCustomerBalance.getCbDate().before(INV_DT)) {

                    // create new balance

                    LocalArCustomerBalance apNewCustomerBalance = arCustomerBalanceHome.create(INV_DT, arCustomerBalance.getCbBalance() + INV_AMNT, companyCode);

                    arCustomer.addArCustomerBalance(apNewCustomerBalance);

                } else { // equals to invoice date

                    arCustomerBalance.setCbBalance(arCustomerBalance.getCbBalance() + INV_AMNT);
                }

            } else {

                // create new balance

                LocalArCustomerBalance apNewCustomerBalance = arCustomerBalanceHome.create(INV_DT, INV_AMNT, companyCode);

                arCustomer.addArCustomerBalance(apNewCustomerBalance);
            }

            // propagate to subsequent balances if necessary

            arCustomerBalances = arCustomerBalanceHome.findByAfterInvDateAndCstCustomerCode(INV_DT, arCustomer.getCstCustomerCode(), companyCode);

            for (Object customerBalance : arCustomerBalances) {

                LocalArCustomerBalance arCustomerBalance = (LocalArCustomerBalance) customerBalance;

                arCustomerBalance.setCbBalance(arCustomerBalance.getCbBalance() + INV_AMNT);
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer companyCode) {

        Debug.print("ArCreditMemoEntryControllerBean convertForeignToFunctionalCurrency");

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

    private void addArDrIliEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE, LocalArInvoice arInvoice, Integer branchCode, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArInvoiceEntryControllerBean addArDrIliEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, branchCode, companyCode);

            // create distribution record

            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, EJBCommon.FALSE, companyCode);

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

    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double JL_AMNT, Integer companyCode) {

        Debug.print("ArCreditMemoEntryControllerBean postToGl");

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

    private LocalArInvoiceLineItem addArIliEntry(ArModInvoiceLineItemDetails mdetails, LocalArInvoice arInvoice, LocalArTaxCode arTaxCode, LocalInvItemLocation invItemLocation, LocalArInvoice arCreditedInvoice, Integer branchCode, Integer companyCode) {

        Debug.print("ArCreditMemoEntryControllerBean addArIliEntry");

        try {

            short precisionUnit = this.getGlFcPrecisionUnit(companyCode);

            double ILI_AMNT = 0d;
            double ILI_TAX_AMNT = 0d;
            double ILI_DSCNT_1 = 0d;
            double ILI_DSCNT_2 = 0d;
            double ILI_DSCNT_3 = 0d;
            double ILI_DSCNT_4 = 0d;
            double ILI_TTL_DSCNT = 0d;

            // calculate net amount

            if (arTaxCode.getTcType().equals("INCLUSIVE")) {

                ILI_AMNT = EJBCommon.roundIt(mdetails.getIliAmount() / (1 + (arTaxCode.getTcRate() / 100)), precisionUnit);

            } else {

                // tax exclusive, none, zero rated or exempt

                ILI_AMNT = mdetails.getIliAmount();
            }

            // check discount details from credited invoice
            // calculate discount, if applicable

            Collection arCreditedInvoiceLineItems = arInvoiceLineItemHome.findByInvNumberAndInvCreditMemoAndInvPostedAndIlCodeAndBrCode(arCreditedInvoice.getInvCode(), EJBCommon.FALSE, EJBCommon.TRUE, invItemLocation.getIlCode(), branchCode, companyCode);

            Iterator i = arCreditedInvoiceLineItems.iterator();

            if (i.hasNext()) {

                LocalArInvoiceLineItem arCreditedInvoiceLineItem = (LocalArInvoiceLineItem) i.next();

                ILI_DSCNT_1 = arCreditedInvoiceLineItem.getIliDiscount1();
                ILI_DSCNT_2 = arCreditedInvoiceLineItem.getIliDiscount2();
                ILI_DSCNT_3 = arCreditedInvoiceLineItem.getIliDiscount3();
                ILI_DSCNT_4 = arCreditedInvoiceLineItem.getIliDiscount4();

                ILI_TTL_DSCNT = (arCreditedInvoiceLineItem.getIliTotalDiscount() / arCreditedInvoiceLineItem.getIliQuantity()) * mdetails.getIliQuantity();
            }

            // calculate tax
            if (!arTaxCode.getTcType().equals("NONE") && !arTaxCode.getTcType().equals("EXEMPT")) {

                if (mdetails.getIliTax() == EJBCommon.TRUE) {
                    if (arTaxCode.getTcType().equals("INCLUSIVE")) {
                        ILI_TAX_AMNT = EJBCommon.roundIt(mdetails.getIliAmount() - ILI_AMNT, precisionUnit);
                    } else if (arTaxCode.getTcType().equals("EXCLUSIVE")) {
                        ILI_TAX_AMNT = EJBCommon.roundIt(mdetails.getIliAmount() * arTaxCode.getTcRate() / 100, precisionUnit);
                    }
                }
            }

            LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.IliLine(mdetails.getIliLine()).IliQuantity(mdetails.getIliQuantity()).IliUnitPrice(mdetails.getIliUnitPrice()).IliAmount(ILI_AMNT).IliTaxAmount(ILI_TAX_AMNT).IliDiscount1(ILI_DSCNT_1).IliDiscount2(ILI_DSCNT_2).IliDiscount3(ILI_DSCNT_3).IliDiscount4(ILI_DSCNT_4).IliTotalDiscount(ILI_TTL_DSCNT).IliTax(mdetails.getIliTax()).IliAdCompany(companyCode).buildInvoiceLineItem();

            arInvoice.addArInvoiceLineItem(arInvoiceLineItem);

            arInvoiceLineItem.setIliTax(mdetails.getIliTax());
            invItemLocation.addArInvoiceLineItem(arInvoiceLineItem);

            LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(mdetails.getIliUomName(), companyCode);
            invUnitOfMeasure.addArInvoiceLineItem(arInvoiceLineItem);

            // validate misc
            arInvoiceLineItem.setIliMisc(mdetails.getIliMisc());
            return arInvoiceLineItem;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToInv(LocalArInvoiceLineItem arInvoiceLineItem, Date CST_DT, double CST_QTY_SLD, double CST_CST_OF_SLS, double CST_RMNNG_QTY, double CST_RMNNG_VL, double CST_VRNC_VL, String USR_NM, Integer branchCode, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ArCreditMemoEntryControllerBean postToInv");

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

                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            } catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            // void subsequent cost variance adjustments
            Collection invAdjustmentLines = invAdjustmentLineHome.findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(CST_DT, invItemLocation.getIlCode(), branchCode, companyCode);

            for (Object adjustmentLine : invAdjustmentLines) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) adjustmentLine;
                this.voidInvAdjustment(invAdjustmentLine.getInvAdjustment(), branchCode, companyCode);
            }

            String prevExpiryDates = "";
            String miscListPrpgt = "";
            double qtyPrpgt = 0;
            try {
                LocalInvCosting prevCst = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndRemainingQuantityNotEqualToZeroAndIlCode(CST_DT, invItemLocation.getIlCode(), branchCode, companyCode);

                prevExpiryDates = prevCst.getCstExpiryDate();
                qtyPrpgt = prevCst.getCstRemainingQuantity();

                if (prevExpiryDates == null) {
                    prevExpiryDates = "";
                }

            } catch (Exception ex) {

            }

            // create costing
            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d, 0d, 0d, CST_QTY_SLD, CST_CST_OF_SLS, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, 0d, branchCode, companyCode);
            // invItemLocation.addInvCosting(invCosting);
            invCosting.setArInvoiceLineItem(arInvoiceLineItem);
            invCosting.setInvItemLocation(invItemLocation);
            // Get Latest Expiry Dates

            invCosting.setCstQuantity(CST_QTY_SLD * -1);
            invCosting.setCstCost(CST_CST_OF_SLS * -1);

            // if cost variance is not 0, generate cost variance for the transaction
            if (CST_VRNC_VL != 0) {

                this.generateCostVariance(invCosting.getInvItemLocation(), CST_VRNC_VL, "ARCR" + arInvoiceLineItem.getArReceipt().getRctNumber(), arInvoiceLineItem.getArReceipt().getRctDescription(), arInvoiceLineItem.getArReceipt().getRctDate(), USR_NM, branchCode, companyCode);
            }

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);

            Iterator i = invCostings.iterator();
            String miscList = "";

            // regenerate cost variance
            this.regenerateCostVariance(invCostings, invCosting, branchCode, companyCode);

        } catch (AdPRFCoaGlVarianceAccountNotFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public String getQuantityExpiryDates(String qntty) {

        String separator = "$";

        // Remove first $ character
        qntty = qntty.substring(1);

        // Counter
        int start = 0;
        int nextIndex = qntty.indexOf(separator, start);
        int length = nextIndex - start;
        String y;
        y = (qntty.substring(start, start + length));

        return y;
    }

    public String propagateExpiryDates(String misc, double qty) throws Exception {
        // ActionErrors errors = new ActionErrors();

        Debug.print("ApReceivingItemControllerBean getExpiryDates");

        String separator = "$";

        // Remove first $ character
        misc = misc.substring(1);

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

            try {

                miscList.append("$").append(misc, start, start + length).append("$");
            } catch (Exception ex) {

                throw ex;
            }
        }

        miscList.append("$");
        return (miscList.toString());
    }

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, double QTY_SLD, Integer companyCode) {

        Debug.print("ArCreditMemoEntryControllerBean convertByUomFromAndItemAndQuantity");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), companyCode);

            return EJBCommon.roundIt(QTY_SLD * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), adPreference.getPrfInvQuantityPrecisionUnit());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void regenerateInventoryDr(LocalArInvoice arInvoice, Integer branchCode, Integer companyCode) throws GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException {

        Debug.print("ArCreditMemoEntryControllerBean regenerateInventoryDr");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            // regenerate inventory distribution records

            Collection arDistributionRecords = arDistributionRecordHome.findImportableDrByInvCode(arInvoice.getInvCode(), companyCode);

            Iterator i = arDistributionRecords.iterator();

            while (i.hasNext()) {

                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

                if (arDistributionRecord.getDrClass().equals("COGS") || arDistributionRecord.getDrClass().equals("INVENTORY")) {

                    i.remove();
                    // arDistributionRecord.entityRemove();
                    em.remove(arDistributionRecord);
                }
            }

            Collection arInvoiceLineItems = arInvoice.getArInvoiceLineItems();

            if (arInvoiceLineItems != null && !arInvoiceLineItems.isEmpty()) {

                i = arInvoiceLineItems.iterator();

                while (i.hasNext()) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) i.next();
                    LocalInvItemLocation invItemLocation = arInvoiceLineItem.getInvItemLocation();

                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }
                    // add cost of sales distribution and inventory

                    double COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                    try {

                        LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);

                        if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Average")) {
                            COST = invCosting.getCstRemainingQuantity() <= 0 ? COST : Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                        }

                        if (COST <= 0) {
                            COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();
                        } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("FIFO")) {
                            COST = invCosting.getCstRemainingQuantity() == 0 ? COST : Math.abs(this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), arInvoiceLineItem.getIliQuantity(), arInvoiceLineItem.getIliUnitPrice(), false, branchCode, companyCode));
                        } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Standard")) {
                            COST = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                        }

                    } catch (FinderException ex) {
                        COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();
                    }

                    double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                    if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiNonInventoriable() == EJBCommon.FALSE) {

                        this.addArDrIliEntry(arInvoice.getArDrNextLine(), "COGS", EJBCommon.FALSE, COST * QTY_SLD, arInvoiceLineItem.getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arInvoice, branchCode, companyCode);

                        this.addArDrIliEntry(arInvoice.getArDrNextLine(), "INVENTORY", EJBCommon.TRUE, COST * QTY_SLD, arInvoiceLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(), arInvoice, branchCode, companyCode);
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

    private double convertCostByUom(String II_NM, String UOM_NM, double unitCost, boolean isFromDefault, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean convertCostByUom");

        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NM, companyCode);
            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, UOM_NM, companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, invItem.getInvUnitOfMeasure().getUomName(), companyCode);

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

    private void voidInvAdjustment(LocalInvAdjustment invAdjustment, Integer branchCode, Integer companyCode) {

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

                this.addInvDrEntry(invAdjustment.getInvDrNextLine(), invDistributionRecord.getDrClass(), invDistributionRecord.getDrDebit() == EJBCommon.TRUE ? EJBCommon.FALSE : EJBCommon.TRUE, invDistributionRecord.getDrAmount(), EJBCommon.TRUE, invDistributionRecord.getInvChartOfAccount().getCoaCode(), invAdjustment, branchCode, companyCode);
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

            this.executeInvAdjPost(invAdjustment.getAdjCode(), invAdjustment.getAdjLastModifiedBy(), branchCode, companyCode);

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void generateCostVariance(LocalInvItemLocation invItemLocation, double CST_VRNC_VL, String ADJ_RFRNC_NMBR, String ADJ_DSCRPTN, Date ADJ_DT, String USR_NM, Integer branchCode, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

    }

    private void regenerateCostVariance(Collection invCostings, LocalInvCosting invCosting, Integer branchCode, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

    }

    private void addInvDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, byte DR_RVRSL, Integer COA_CODE, LocalInvAdjustment invAdjustment, Integer branchCode, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArMiscReceiptEntryControllerBean addInvDrEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate coa

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, branchCode, companyCode);

            } catch (FinderException ex) {

                throw new GlobalBranchAccountNumberInvalidException();
            }

            // create distribution record

            LocalInvDistributionRecord invDistributionRecord = invDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), DR_RVRSL, EJBCommon.FALSE, companyCode);

            invAdjustment.addInvDistributionRecord(invDistributionRecord);
            glChartOfAccount.addInvDistributionRecord(invDistributionRecord);

        } catch (GlobalBranchAccountNumberInvalidException ex) {

            throw new GlobalBranchAccountNumberInvalidException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void executeInvAdjPost(Integer ADJ_CODE, String USR_NM, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException {

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

                LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(invAdjustment.getAdjDate(), invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(), invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName(), branchCode, companyCode);

                this.postInvAdjustmentToInventory(invAdjustmentLine, invAdjustment.getAdjDate(), 0, invAdjustmentLine.getAlUnitCost(), invCosting.getCstRemainingQuantity(), invCosting.getCstRemainingValue() + invAdjustmentLine.getAlUnitCost(), branchCode, companyCode);
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
                glChartOfAccount.addGlJournalLine(glOffsetJournalLine);

            } else if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE && TOTAL_DEBIT != TOTAL_CREDIT) {

                throw new GlobalJournalNotBalanceException();
            }

            // create journal batch if necessary

            LocalGlJournalBatch glJournalBatch = null;
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");

            try {

                glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " INVENTORY ADJUSTMENTS", branchCode, companyCode);

            } catch (FinderException ex) {

            }

            if (glJournalBatch == null) {

                glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " INVENTORY ADJUSTMENTS", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, branchCode, companyCode);
            }

            // create journal entry

            LocalGlJournal glJournal = glJournalHome.create(invAdjustment.getAdjReferenceNumber(), invAdjustment.getAdjDescription(), invAdjustment.getAdjDate(), 0.0d, null, invAdjustment.getAdjDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM, EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, EJBCommon.FALSE, null, branchCode, companyCode);

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

                invDistributionRecord.getInvChartOfAccount().addGlJournalLine(glJournalLine);

                glJournal.addGlJournalLine(glJournalLine);

                invDistributionRecord.setDrImported(EJBCommon.TRUE);
            }

            if (glOffsetJournalLine != null) {

                glJournal.addGlJournalLine(glOffsetJournalLine);
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
                    LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(adCompany.getCmpRetainedEarnings(), branchCode, companyCode);

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
            invAdjustment.addInvAdjustmentLine(invAdjustmentLine);
            invItemLocation.getInvItem().getInvUnitOfMeasure().addInvAdjustmentLine(invAdjustmentLine);
            invItemLocation.addInvAdjustmentLine(invAdjustmentLine);

            return invAdjustmentLine;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalInvAdjustment saveInvAdjustment(String ADJ_RFRNC_NMBR, String ADJ_DSCRPTN, Date ADJ_DATE, String USR_NM, Integer branchCode, Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean saveInvAdjustment");

        try {

            // generate adj document number
            String ADJ_DCMNT_NMBR = null;

            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

            try {

                adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("INV ADJUSTMENT", companyCode);

            } catch (FinderException ex) {

            }

            try {

                adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode);

            } catch (FinderException ex) {

            }

            while (true) {

                if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                    try {

                        invAdjustmentHome.findByAdjDocumentNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), branchCode, companyCode);
                        adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                    } catch (FinderException ex) {

                        ADJ_DCMNT_NMBR = adDocumentSequenceAssignment.getDsaNextSequence();
                        adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                        break;
                    }

                } else {

                    try {

                        invAdjustmentHome.findByAdjDocumentNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), branchCode, companyCode);
                        adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                    } catch (FinderException ex) {

                        ADJ_DCMNT_NMBR = adBranchDocumentSequenceAssignment.getBdsNextSequence();
                        adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                        break;
                    }
                }
            }

            return invAdjustmentHome.create(ADJ_DCMNT_NMBR, ADJ_RFRNC_NMBR, ADJ_DSCRPTN, ADJ_DATE, "COST-VARIANCE", "N/A", EJBCommon.FALSE, USR_NM, ADJ_DATE, USR_NM, ADJ_DATE, null, null, USR_NM, ADJ_DATE, null, null, EJBCommon.TRUE, EJBCommon.FALSE, branchCode, companyCode);

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postInvAdjustmentToInventory(LocalInvAdjustmentLine invAdjustmentLine, Date CST_DT, double CST_ADJST_QTY, double CST_ADJST_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, Integer branchCode, Integer companyCode) {

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

                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            } catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d, CST_ADJST_QTY, CST_ADJST_CST, 0d, 0d, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, CST_ADJST_QTY > 0 ? CST_ADJST_QTY : 0, branchCode, companyCode);
            invItemLocation.addInvCosting(invCosting);
            invCosting.setInvAdjustmentLine(invAdjustmentLine);

            invCosting.setCstQuantity(CST_ADJST_QTY);
            invCosting.setCstCost(CST_ADJST_CST);

            // propagate balance if necessary

            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);

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

    public ArrayList getArCreditMemoReportParameters(Integer companyCode) {

        Debug.print("ArCreditMemoEntryControllerBean getArCreditMemoReportParameters");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AR PRINT INVOICE PARAMETER", companyCode);

            if (adLookUpValues.size() <= 0) {
                return list;
            }

            for (Object lookUpValue : adLookUpValues) {

                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;
                list.add(adLookUpValue.getLvName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ArCreditMemoEntryControllerBean ejbCreate");
    }

}