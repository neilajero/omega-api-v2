/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ApVoucherEntryControllerBean
 * @created
 * @author
 */
package com.ejb.txn.ap;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ap.*;
import com.ejb.dao.gen.LocalGenSegmentHome;
import com.ejb.dao.gen.LocalGenValueSetValueHome;
import com.ejb.dao.gl.*;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ap.*;
import com.ejb.entities.gl.*;
import com.ejb.entities.inv.*;
import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.ejb.txn.OmegaCommonDataController;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.ap.ApTaxCodeDetails;
import com.util.ap.ApVoucherDetails;
import com.util.mod.ap.*;
import com.util.mod.inv.InvModLineItemDetails;
import com.util.mod.inv.InvModTagListDetails;

import jakarta.ejb.*;
import java.util.*;

@Stateless(name = "ApVoucherEntryControllerEJB")
public class ApVoucherEntryControllerBean extends EJBContextClass implements ApVoucherEntryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    ApApprovalController apApprovalController;
    @EJB
    private OmegaCommonDataController commonData;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalAdPaymentTermHome adPaymentTermHome;
    @EJB
    private LocalApTaxCodeHome apTaxCodeHome;
    @EJB
    private LocalApWithholdingTaxCodeHome apWithholdingTaxCodeHome;
    @EJB
    private ILocalApSupplierHome apSupplierHome;
    @EJB
    private LocalApVoucherBatchHome apVoucherBatchHome;
    @EJB
    private LocalApPurchaseOrderLineHome apPurchaseOrderLineHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalApVoucherHome apVoucherHome;
    @EJB
    private LocalGenValueSetValueHome genValueSetValueHome;
    @EJB
    private LocalGenSegmentHome genSegmentHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    private LocalAdBranchSupplierHome adBranchSupplierHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalApVoucherPaymentScheduleHome apVoucherPaymentScheduleHome;
    @EJB
    private LocalAdAmountLimitHome adAmountLimitHome;
    @EJB
    private LocalAdApprovalUserHome adApprovalUserHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private LocalAdBranchApTaxCodeHome adBranchApTaxCodeHome;
    @EJB
    private LocalAdDeleteAuditTrailHome adDeleteAuditTrailHome;
    @EJB
    private LocalInvLineItemTemplateHome invLineItemTemplateHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private ILocalGlSetOfBookHome glSetOfBookHome;
    @EJB
    private ILocalGlAccountingCalendarValueHome glAccountingCalendarValueHome;
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
    private LocalGlForexLedgerHome glForexLedgerHome;
    @EJB
    private LocalApSupplierBalanceHome apSupplierBalanceHome;
    @EJB
    private LocalApVoucherLineItemHome apVoucherLineItemHome;
    @EJB
    private ILocalGlChartOfAccountBalanceHome glChartOfAccountBalanceHome;
    @EJB
    private ILocalGlInvestorAccountBalanceHome glInvestorAccountBalanceHome;
    @EJB
    private LocalInvAdjustmentLineHome invAdjustmentLineHome;
    @EJB
    private LocalInvAdjustmentHome invAdjustmentHome;
    @EJB
    private LocalInvDistributionRecordHome invDistributionRecordHome;
    @EJB
    private LocalInvTagHome invTagHome;
    @EJB
    private LocalAdBranchHome adBranchHome;

    public ApTaxCodeDetails getApTcByTcName(String TC_NM, Integer companyCode) {

        Debug.print("ApPurchaseOrderEntryControllerBean getArTcByTcName");

        ArrayList list = new ArrayList();

        try {

            LocalApTaxCode apTaxCode = apTaxCodeHome.findByTcName(TC_NM, companyCode);

            ApTaxCodeDetails details = new ApTaxCodeDetails();
            details.setTcType(apTaxCode.getTcType());
            details.setTcRate(apTaxCode.getTcRate());

            return details;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public String getApNoneTc(Integer companyCode) {

        Debug.print("ApVoucherEntryControllerBean getApNoneTc");

        ArrayList list = new ArrayList();

        String taxCode = null;

        try {

            Collection apTaxCodes = apTaxCodeHome.findNoneTc(companyCode);

            for (Object code : apTaxCodes) {

                LocalApTaxCode apTaxCode = (LocalApTaxCode) code;

                taxCode = apTaxCode.getTcName();

                break;
            }

            return taxCode;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }


    public ArrayList getApSplTradeAll(Integer AD_BRNCH, Integer companyCode) {

        Debug.print("ApVoucherEntryControllerBean getApSplTradeAll");

        ArrayList list = new ArrayList();

        try {

            Collection apSuppliers = apSupplierHome.findEnabledSplTradeAll(AD_BRNCH, companyCode);

            for (Object supplier : apSuppliers) {

                LocalApSupplier apSupplier = (LocalApSupplier) supplier;

                list.add(apSupplier.getSplSupplierCode());
            }

            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApOpenVbAll(String DPRTMNT, Integer AD_BRNCH, Integer companyCode) {

        Debug.print("ApVoucherEntryControllerBean getApOpenVbAll");

        ArrayList list = new ArrayList();

        try {
            Collection apVoucherBatches = null;

            if (DPRTMNT.equals("") || DPRTMNT.equals("default") || DPRTMNT.equals("NO RECORD FOUND")) {
                Debug.print("------------>");
                apVoucherBatches = apVoucherBatchHome.findOpenVbByVbType("VOUCHER", AD_BRNCH, companyCode);

            } else {
                Debug.print("------------>else");
                apVoucherBatches = apVoucherBatchHome.findOpenVbByVbTypeDepartment("VOUCHER", DPRTMNT, AD_BRNCH, companyCode);
            }

            for (Object voucherBatch : apVoucherBatches) {

                LocalApVoucherBatch apVoucherBatch = (LocalApVoucherBatch) voucherBatch;

                list.add(apVoucherBatch.getVbName());
            }

            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApOpenPlByPoDcmntNmbr(String PO_DCMNT_NMBR, Integer AD_BRNCH, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ApVoucherEntryControllerBean getApOpenPlByPoDcmntNmbr");

        try {

            Collection apPurchaseOrderLines = null;

            try {

                apPurchaseOrderLines = apPurchaseOrderLineHome.findOpenPlByPoDcmntNmbrAndBrCode(PO_DCMNT_NMBR, AD_BRNCH, companyCode);

            }
            catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }
            Debug.print("SIZE=" + apPurchaseOrderLines.size());
            ArrayList list = new ArrayList();

            for (Object purchaseOrderLine : apPurchaseOrderLines) {

                LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) purchaseOrderLine;

                if (apPurchaseOrderLine.getPlQuantity() == 0d) continue;

                ApModPurchaseOrderLineDetails plDetails = new ApModPurchaseOrderLineDetails();

                plDetails.setPlCode(apPurchaseOrderLine.getPlCode());
                plDetails.setPlQuantity(apPurchaseOrderLine.getPlQuantity());
                plDetails.setPlUnitCost(apPurchaseOrderLine.getPlUnitCost());
                plDetails.setPlAmount(apPurchaseOrderLine.getPlAmount() + apPurchaseOrderLine.getPlTaxAmount());
                plDetails.setPlPoDocumentNumber(apPurchaseOrderLine.getApPurchaseOrder().getPoDocumentNumber());
                plDetails.setPlPoReceivingPoNumber(apPurchaseOrderLine.getApPurchaseOrder().getPoRcvPoNumber());
                Debug.print("II_NM=" + apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiName());
                Debug.print("IL_CODE=" + apPurchaseOrderLine.getInvItemLocation().getIlCode());
                Debug.print("UOM_NM=" + apPurchaseOrderLine.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName());

                plDetails.setPlUomName(apPurchaseOrderLine.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName());
                plDetails.setPlLocName(apPurchaseOrderLine.getInvItemLocation().getInvLocation().getLocName());
                plDetails.setPlIiName(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiName());
                plDetails.setPlIiDescription(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiDescription());

                plDetails.setPlCurrency(apPurchaseOrderLine.getApPurchaseOrder().getGlFunctionalCurrency().getFcName());
                Debug.print("getPoDocumentNumber" + apPurchaseOrderLine.getApPurchaseOrder().getPoDocumentNumber());
                Debug.print("getPlCode" + apPurchaseOrderLine.getPlCode());

                plDetails.setPlPoQuantity(0d);
                plDetails.setPlPoAmount(0d);

                if (apPurchaseOrderLine.getPlPlCode() != null) {
                    Debug.print("apPurchaseOrderLine.getPlPlCode()" + apPurchaseOrderLine.getPlPlCode());
                    try {
                        LocalApPurchaseOrderLine apOrigPurchaseOrderLine = apPurchaseOrderLineHome.findByPrimaryKey(apPurchaseOrderLine.getPlPlCode());
                        Debug.print("apOrigPurchaseOrderLine-" + apOrigPurchaseOrderLine.getPlQuantity());
                        plDetails.setPlPoQuantity(apOrigPurchaseOrderLine.getPlQuantity());
                        plDetails.setPlPoAmount(apOrigPurchaseOrderLine.getPlAmount());
                    }
                    catch (FinderException ex) {

                    }
                }

                plDetails.setPlPoConversionRate(apPurchaseOrderLine.getApPurchaseOrder().getPoConversionRate());
                plDetails.setPlPoConversionDate(apPurchaseOrderLine.getApPurchaseOrder().getPoConversionDate());

                plDetails.setPlDiscount1(apPurchaseOrderLine.getPlDiscount1());
                plDetails.setPlDiscount2(apPurchaseOrderLine.getPlDiscount2());
                plDetails.setPlDiscount3(apPurchaseOrderLine.getPlDiscount3());
                plDetails.setPlDiscount4(apPurchaseOrderLine.getPlDiscount4());
                plDetails.setPlTotalDiscount(apPurchaseOrderLine.getPlTotalDiscount());

                plDetails.setPlMisc(apPurchaseOrderLine.getPlMisc());

                plDetails.setPlPoSupplierCode(apPurchaseOrderLine.getApPurchaseOrder().getApSupplier().getSplSupplierCode());
                plDetails.setPlPoCode(apPurchaseOrderLine.getApPurchaseOrder().getPoCode());

                ArrayList tagList = new ArrayList();
                plDetails.setTraceMisc(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiTraceMisc());
                // Debug.print(vliDetails.getTraceMisc() + "<== getTraceMisc under
                // getInvAdjByAdjCode
                // controllerbean");
                if (plDetails.getTraceMisc() == 1) {

                    tagList = this.getInvTagList(apPurchaseOrderLine);

                    plDetails.setPlTagList(tagList);
                }

                list.add(plDetails);
            }

            return list;

        }
        catch (GlobalNoRecordFoundException ex) {

            Debug.printStackTrace(ex);
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApOpenPlBySplSupplierCode(String SPL_SPPLR_CODE, Integer AD_BRNCH, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ApVoucherEntryControllerBean getApOpenPlBySplSupplierCode");

        try {

            Collection apPurchaseOrderLines = null;

            try {

                apPurchaseOrderLines = apPurchaseOrderLineHome.findOpenPlBySplSupplierCodeAndBrCode(SPL_SPPLR_CODE, AD_BRNCH, companyCode);

            }
            catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }
            Debug.print("SIZE=" + apPurchaseOrderLines.size());
            ArrayList list = new ArrayList();

            for (Object purchaseOrderLine : apPurchaseOrderLines) {

                LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) purchaseOrderLine;

                if (apPurchaseOrderLine.getPlQuantity() == 0d) continue;

                ApModPurchaseOrderLineDetails plDetails = new ApModPurchaseOrderLineDetails();

                plDetails.setPlCode(apPurchaseOrderLine.getPlCode());
                plDetails.setPlQuantity(apPurchaseOrderLine.getPlQuantity());
                plDetails.setPlUnitCost(apPurchaseOrderLine.getPlUnitCost());
                plDetails.setPlAmount(apPurchaseOrderLine.getPlAmount() + apPurchaseOrderLine.getPlTaxAmount());
                plDetails.setPlPoDocumentNumber(apPurchaseOrderLine.getApPurchaseOrder().getPoDocumentNumber());
                plDetails.setPlPoReceivingPoNumber(apPurchaseOrderLine.getApPurchaseOrder().getPoRcvPoNumber());
                Debug.print("II_NM=" + apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiName());
                Debug.print("IL_CODE=" + apPurchaseOrderLine.getInvItemLocation().getIlCode());
                Debug.print("UOM_NM=" + apPurchaseOrderLine.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName());

                plDetails.setPlUomName(apPurchaseOrderLine.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName());
                plDetails.setPlLocName(apPurchaseOrderLine.getInvItemLocation().getInvLocation().getLocName());
                plDetails.setPlIiName(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiName());
                plDetails.setPlIiDescription(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiDescription());

                plDetails.setPlCurrency(apPurchaseOrderLine.getApPurchaseOrder().getGlFunctionalCurrency().getFcName());
                Debug.print("getPoDocumentNumber" + apPurchaseOrderLine.getApPurchaseOrder().getPoDocumentNumber());
                Debug.print("getPlCode" + apPurchaseOrderLine.getPlCode());

                plDetails.setPlPoQuantity(0d);
                plDetails.setPlPoAmount(0d);

                if (apPurchaseOrderLine.getPlPlCode() != null) {
                    Debug.print("apPurchaseOrderLine.getPlPlCode()" + apPurchaseOrderLine.getPlPlCode());
                    try {
                        LocalApPurchaseOrderLine apOrigPurchaseOrderLine = apPurchaseOrderLineHome.findByPrimaryKey(apPurchaseOrderLine.getPlPlCode());
                        Debug.print("apOrigPurchaseOrderLine-" + apOrigPurchaseOrderLine.getPlQuantity());
                        plDetails.setPlPoQuantity(apOrigPurchaseOrderLine.getPlQuantity());
                        plDetails.setPlPoAmount(apOrigPurchaseOrderLine.getPlAmount());
                    }
                    catch (FinderException ex) {

                    }
                }

                plDetails.setPlPoConversionRate(apPurchaseOrderLine.getApPurchaseOrder().getPoConversionRate());
                plDetails.setPlPoConversionDate(apPurchaseOrderLine.getApPurchaseOrder().getPoConversionDate());

                plDetails.setPlDiscount1(apPurchaseOrderLine.getPlDiscount1());
                plDetails.setPlDiscount2(apPurchaseOrderLine.getPlDiscount2());
                plDetails.setPlDiscount3(apPurchaseOrderLine.getPlDiscount3());
                plDetails.setPlDiscount4(apPurchaseOrderLine.getPlDiscount4());
                plDetails.setPlTotalDiscount(apPurchaseOrderLine.getPlTotalDiscount());
                plDetails.setPlPoSupplierCode(apPurchaseOrderLine.getApPurchaseOrder().getApSupplier().getSplSupplierCode());
                plDetails.setPlMisc(apPurchaseOrderLine.getPlMisc());

                ArrayList tagList = new ArrayList();
                plDetails.setTraceMisc(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiTraceMisc());
                // Debug.print(vliDetails.getTraceMisc() + "<== getTraceMisc under
                // getInvAdjByAdjCode
                // controllerbean");
                if (plDetails.getTraceMisc() == 1) {

                    tagList = this.getInvTagList(apPurchaseOrderLine);

                    plDetails.setPlTagList(tagList);
                }

                plDetails.setPlPoCode(apPurchaseOrderLine.getApPurchaseOrder().getPoCode());

                list.add(plDetails);
            }

            return list;

        }
        catch (GlobalNoRecordFoundException ex) {

            Debug.printStackTrace(ex);
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApOpenPl(Integer AD_BRNCH, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ApVoucherEntryControllerBean getApOpenPl");

        try {

            Collection apPurchaseOrderLines = null;

            try {

                apPurchaseOrderLines = apPurchaseOrderLineHome.findOpenPlByBrCode(AD_BRNCH, companyCode);

            }
            catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }
            Debug.print("SIZE=" + apPurchaseOrderLines.size());
            ArrayList list = new ArrayList();

            HashMap hm = new HashMap();

            for (Object purchaseOrderLine : apPurchaseOrderLines) {

                LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) purchaseOrderLine;

                if (apPurchaseOrderLine.getPlQuantity() == 0d) continue;

                ApModPurchaseOrderLineDetails plDetails = new ApModPurchaseOrderLineDetails();
                plDetails.setPlIiName(apPurchaseOrderLine.getApPurchaseOrder().getApSupplier().getSplSupplierCode());
                plDetails.setPlIiDescription(apPurchaseOrderLine.getApPurchaseOrder().getApSupplier().getSplName());

                plDetails.setPlPoCode(apPurchaseOrderLine.getApPurchaseOrder().getPoCode());
                Debug.print("1 po code: " + apPurchaseOrderLine.getApPurchaseOrder().getPoCode());
                hm.put(apPurchaseOrderLine.getApPurchaseOrder().getApSupplier().getSplCode(), plDetails);
            }

            return new ArrayList(hm.values());

        }
        catch (GlobalNoRecordFoundException ex) {

            Debug.printStackTrace(ex);
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getInvIiIsVatReliefByIiName(String II_NM, Integer companyCode) {

        Debug.print("ApVoucherEntryControllerBean getInvIiIsVatReliefByIiName");

        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NM, companyCode);

            return invItem.getIiIsVatRelief();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }


    public byte getInvIiIsProjectByIiName(String II_NM, Integer companyCode) {

        Debug.print("ApVoucherEntryControllerBean getInvIiIsProjectByIiName");

        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NM, companyCode);

            return invItem.getIiIsProject();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getAdPrfApJournalLineNumber(Integer companyCode) {

        Debug.print("ApDebitMemoEntryControllerBean getAdPrfApJournalLineNumber");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfApJournalLineNumber();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfEnableApVoucherBatch(Integer companyCode) {

        Debug.print("ApDebitMemoEntryControllerBean getAdPrfApEnableApVoucherBatch");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfEnableApVoucherBatch();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ApModVoucherDetails getApVouByVouCode(Integer VOU_CODE, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ApVoucherEntryControllerBean getApVouByVouCode");

        try {

            LocalApVoucher apVoucher = null;

            try {
                Debug.print("1");
                Debug.print("VOU_CODE" + VOU_CODE);
                apVoucher = apVoucherHome.findByPrimaryKey(VOU_CODE);
                Debug.print("2");
            }
            catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList list = new ArrayList();

            // get voucher line items if any

            Collection apVoucherLineItems = apVoucher.getApVoucherLineItems();
            Debug.print("3");
            Collection apPurchaseOrderLines = apVoucher.getApPurchaseOrderLines();
            Debug.print("4");
            double TOTAL_DEBIT = 0d;
            double TOTAL_CREDIT = 0d;

            if (!apVoucherLineItems.isEmpty()) {

                for (Object voucherLineItem : apVoucherLineItems) {

                    LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) voucherLineItem;

                    ApModVoucherLineItemDetails vliDetails = new ApModVoucherLineItemDetails();

                    vliDetails.setVliCode(apVoucherLineItem.getVliCode());
                    vliDetails.setVliLine(apVoucherLineItem.getVliLine());
                    vliDetails.setVliQuantity(apVoucherLineItem.getVliQuantity());
                    vliDetails.setVliUnitCost(apVoucherLineItem.getVliUnitCost());
                    vliDetails.setVliIiName(apVoucherLineItem.getInvItemLocation().getInvItem().getIiName());
                    vliDetails.setVliLocName(apVoucherLineItem.getInvItemLocation().getInvLocation().getLocName());
                    vliDetails.setVliUomName(apVoucherLineItem.getInvUnitOfMeasure().getUomName());
                    vliDetails.setVliIiDescription(apVoucherLineItem.getInvItemLocation().getInvItem().getIiDescription());
                    vliDetails.setVliDiscount1(apVoucherLineItem.getVliDiscount1());
                    vliDetails.setVliDiscount2(apVoucherLineItem.getVliDiscount2());
                    vliDetails.setVliDiscount3(apVoucherLineItem.getVliDiscount3());
                    vliDetails.setVliDiscount4(apVoucherLineItem.getVliDiscount4());
                    vliDetails.setVliTotalDiscount(apVoucherLineItem.getVliTotalDiscount());
                    vliDetails.setVliMisc(apVoucherLineItem.getVliMisc());
                    vliDetails.setVliTax(apVoucherLineItem.getVliTax());

                    vliDetails.setVliIsVatRelief(apVoucherLineItem.getInvItemLocation().getInvItem().getIiIsVatRelief() == (byte) 1);
                    vliDetails.setVliIsProject(apVoucherLineItem.getInvItemLocation().getInvItem().getIiIsProject() == (byte) 1);

                    vliDetails.setVliSplName(apVoucherLineItem.getVliSplName());
                    vliDetails.setVliSplTin(apVoucherLineItem.getVliSplTin());
                    vliDetails.setVliSplAddress(apVoucherLineItem.getVliSplAddress());
                    /*
                     * if(apVoucherLineItem.getPmProject()!=null) {
                     * vliDetails.setVliProjectCode(apVoucherLineItem.getPmProject().
                     * getPrjProjectCode()); }
                     *
                     * if(apVoucherLineItem.getPmProjectTypeType()!=null) {
                     * vliDetails.setVliProjectTypeCode(apVoucherLineItem.getPmProjectTypeType().
                     * getPmProjectType().getPtProjectTypeCode()); }
                     *
                     * if(apVoucherLineItem.getPmProjectPhase()!=null) {
                     * vliDetails.setVliProjectPhaseName(apVoucherLineItem.getPmProjectPhase().
                     * getPpName()); }
                     */

                    if (apVoucher.getApTaxCode().getTcType().equalsIgnoreCase("INCLUSIVE"))
                        vliDetails.setVliAmount(apVoucherLineItem.getVliAmount() + apVoucherLineItem.getVliTaxAmount());
                    else vliDetails.setVliAmount(apVoucherLineItem.getVliAmount());

                    ArrayList tagList = new ArrayList();
                    vliDetails.setIsTraceMisc(apVoucherLineItem.getInvItemLocation().getInvItem().getIiTraceMisc() == EJBCommon.TRUE);
                    // Debug.print(vliDetails.getTraceMisc() + "<== getTraceMisc under
                    // getInvAdjByAdjCode controllerbean");
                    if (vliDetails.getIsTraceMic()) {

                        tagList = this.getInvTagList(apVoucherLineItem);

                        vliDetails.setTagList(tagList);
                    }

                    list.add(vliDetails);
                }

            } else if (!apVoucher.getApPurchaseOrderLines().isEmpty()) {
                // PO MATCHED

                for (Object purchaseOrderLine : apPurchaseOrderLines) {

                    LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) purchaseOrderLine;

                    ApModPurchaseOrderLineDetails plDetails = new ApModPurchaseOrderLineDetails();

                    plDetails.setPlCode(apPurchaseOrderLine.getPlCode());
                    plDetails.setPlPoDocumentNumber(apPurchaseOrderLine.getApPurchaseOrder().getPoDocumentNumber());
                    plDetails.setPlPoReceivingPoNumber(apPurchaseOrderLine.getApPurchaseOrder().getPoRcvPoNumber());
                    plDetails.setPlQuantity(apPurchaseOrderLine.getPlQuantity());
                    plDetails.setPlUnitCost(apPurchaseOrderLine.getPlUnitCost());
                    plDetails.setPlAmount(apPurchaseOrderLine.getPlAmount() + apPurchaseOrderLine.getPlTaxAmount());
                    plDetails.setPlIiName(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiName());
                    plDetails.setPlLocName(apPurchaseOrderLine.getInvItemLocation().getInvLocation().getLocName());
                    plDetails.setPlUomName(apPurchaseOrderLine.getInvUnitOfMeasure().getUomName());
                    plDetails.setPlIiDescription(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiDescription());
                    plDetails.setPlPoQuantity(0d);
                    plDetails.setPlPoAmount(0d);

                    if (apPurchaseOrderLine.getPlPlCode() != null) {
                        try {
                            LocalApPurchaseOrderLine apOrigPurchaseOrderLine = apPurchaseOrderLineHome.findByPrimaryKey(apPurchaseOrderLine.getPlPlCode());
                            Debug.print("apOrigPurchaseOrderLine-" + apOrigPurchaseOrderLine.getPlQuantity());
                            plDetails.setPlPoQuantity(apOrigPurchaseOrderLine.getPlQuantity());
                            plDetails.setPlPoAmount(apOrigPurchaseOrderLine.getPlAmount());
                        }
                        catch (FinderException ex) {

                        }
                    }

                    plDetails.setPlDiscount1(apPurchaseOrderLine.getPlDiscount1());
                    plDetails.setPlDiscount2(apPurchaseOrderLine.getPlDiscount2());
                    plDetails.setPlDiscount3(apPurchaseOrderLine.getPlDiscount3());
                    plDetails.setPlDiscount4(apPurchaseOrderLine.getPlDiscount4());
                    plDetails.setPlTotalDiscount(apPurchaseOrderLine.getPlTotalDiscount());
                    plDetails.setPlMisc(apPurchaseOrderLine.getPlMisc());

                    plDetails.setPlCurrency(apPurchaseOrderLine.getApPurchaseOrder().getGlFunctionalCurrency().getFcName());
                    apPurchaseOrderLine.getApPurchaseOrder().getGlFunctionalCurrency().getFcName();
                    plDetails.setPlPoConversionRate(apPurchaseOrderLine.getApPurchaseOrder().getPoConversionRate());
                    plDetails.setPlPoConversionDate(apPurchaseOrderLine.getApPurchaseOrder().getPoConversionDate());

                    ArrayList tagList = new ArrayList();
                    plDetails.setTraceMisc(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiTraceMisc());
                    // Debug.print(vliDetails.getTraceMisc() + "<== getTraceMisc under
                    // getInvAdjByAdjCode controllerbean");
                    if (plDetails.getTraceMisc() == 1) {

                        tagList = this.getInvTagList(apPurchaseOrderLine);

                        plDetails.setPlTagList(tagList);
                    }

                    plDetails.setPlPoCode(apPurchaseOrderLine.getApPurchaseOrder().getPoCode());

                    list.add(plDetails);
                }

            } else {

                // get distribution records

                Collection apDistributionRecords = apDistributionRecordHome.findByVouCode(apVoucher.getVouCode(), companyCode);

                short lineNumber = 1;

                for (Object distributionRecord : apDistributionRecords) {

                    LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) distributionRecord;

                    ApModDistributionRecordDetails mdetails = new ApModDistributionRecordDetails();

                    mdetails.setDrCode(apDistributionRecord.getDrCode());
                    mdetails.setDrLine(lineNumber);
                    mdetails.setDrClass(apDistributionRecord.getDrClass());
                    mdetails.setDrDebit(apDistributionRecord.getDrDebit());
                    mdetails.setDrAmount(apDistributionRecord.getDrAmount());
                    mdetails.setDrCoaAccountNumber(apDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                    mdetails.setDrCoaAccountDescription(apDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());

                    if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                        TOTAL_DEBIT += apDistributionRecord.getDrAmount();

                    } else {

                        TOTAL_CREDIT += apDistributionRecord.getDrAmount();
                    }

                    list.add(mdetails);

                    lineNumber++;
                }
            }

            ApModVoucherDetails mVouDetails = new ApModVoucherDetails();

            mVouDetails.setVouCode(apVoucher.getVouCode());
            mVouDetails.setVouDescription(apVoucher.getVouDescription());
            mVouDetails.setVouDate(apVoucher.getVouDate());
            mVouDetails.setVouDocumentNumber(apVoucher.getVouDocumentNumber());
            mVouDetails.setVouReferenceNumber(apVoucher.getVouReferenceNumber());
            mVouDetails.setVouConversionDate(apVoucher.getVouConversionDate());
            mVouDetails.setVouConversionRate(apVoucher.getVouConversionRate());
            mVouDetails.setVouBillAmount(apVoucher.getVouBillAmount());
            mVouDetails.setVouAmountDue(apVoucher.getVouAmountDue());
            mVouDetails.setVouAmountPaid(apVoucher.getVouAmountPaid());
            mVouDetails.setVouApprovalStatus(apVoucher.getVouApprovalStatus());
            mVouDetails.setVouReasonForRejection(apVoucher.getVouReasonForRejection());
            mVouDetails.setVouPosted(apVoucher.getVouPosted());
            mVouDetails.setVouVoid(apVoucher.getVouVoid());
            mVouDetails.setVouTotalDebit(TOTAL_DEBIT);
            mVouDetails.setVouTotalCredit(TOTAL_CREDIT);
            mVouDetails.setVouCreatedBy(apVoucher.getVouCreatedBy());
            mVouDetails.setVouDateCreated(apVoucher.getVouDateCreated());
            mVouDetails.setVouLastModifiedBy(apVoucher.getVouLastModifiedBy());
            mVouDetails.setVouDateLastModified(apVoucher.getVouDateLastModified());
            mVouDetails.setVouApprovedRejectedBy(apVoucher.getVouApprovedRejectedBy());
            mVouDetails.setVouDateApprovedRejected(apVoucher.getVouDateApprovedRejected());
            mVouDetails.setVouPostedBy(apVoucher.getVouPostedBy());
            mVouDetails.setVouDatePosted(apVoucher.getVouDatePosted());
            mVouDetails.setVouFcName(apVoucher.getGlFunctionalCurrency().getFcName());
            mVouDetails.setVouTcName(apVoucher.getApTaxCode().getTcName());
            mVouDetails.setVouTcType(apVoucher.getApTaxCode().getTcType());
            mVouDetails.setVouTcRate(apVoucher.getApTaxCode().getTcRate());
            mVouDetails.setVouWtcName(apVoucher.getApWithholdingTaxCode().getWtcName());
            mVouDetails.setVouSplSupplierCode(apVoucher.getApSupplier().getSplSupplierCode());

            mVouDetails.setVouPytName(apVoucher.getAdPaymentTerm().getPytName());

            mVouDetails.setVouVbName(apVoucher.getApVoucherBatch() != null ? apVoucher.getApVoucherBatch().getVbName() : null);
            mVouDetails.setVouPoNumber(apVoucher.getVouPoNumber());
            mVouDetails.setVouSplName(apVoucher.getApSupplier().getSplName());
            mVouDetails.setVouSplClassName(apVoucher.getApSupplier().getApSupplierClass().getScName());
            mVouDetails.setVouScLoan(apVoucher.getApSupplier().getApSupplierClass().getScIsLoan());
            mVouDetails.setVouLoan(apVoucher.getVouLoan());
            mVouDetails.setVouScVatReliefVoucherItem(apVoucher.getApSupplier().getApSupplierClass().getScIsVatReliefVoucherItem());
            mVouDetails.setVouLoanGenerated(apVoucher.getVouLoanGenerated());
            mVouDetails.setVouPytName2(apVoucher.getAdPaymentTerm2() != null ? apVoucher.getAdPaymentTerm2().getPytName() : null);

            if (!apVoucherLineItems.isEmpty()) {

                mVouDetails.setVouVliList(list);

            } else if (!apVoucher.getApPurchaseOrderLines().isEmpty()) {

                mVouDetails.setVouPlList(list);

            } else {

                mVouDetails.setVouDrList(list);
            }

            return mVouDetails;

        }
        catch (GlobalNoRecordFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ApModSupplierDetails getApSplBySplSupplierCode(String SPL_SPPLR_CODE, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ApVoucherEntryControllerBean getApSplBySplSupplierCode");

        try {

            LocalApSupplier apSupplier = null;

            try {

                apSupplier = apSupplierHome.findBySplSupplierCode(SPL_SPPLR_CODE, companyCode);

            }
            catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ApModSupplierDetails mdetails = new ApModSupplierDetails();

            mdetails.setSplPytName(apSupplier.getAdPaymentTerm() != null ? apSupplier.getAdPaymentTerm().getPytName() : null);
            mdetails.setSplScWtcName(apSupplier.getApSupplierClass().getApWithholdingTaxCode() != null ? apSupplier.getApSupplierClass().getApWithholdingTaxCode().getWtcName() : null);
            mdetails.setSplName(apSupplier.getSplName());
            mdetails.setSplScName(apSupplier.getApSupplierClass().getScName());
            mdetails.setSplScLoan(apSupplier.getApSupplierClass().getScIsLoan());
            mdetails.setSplScVatReliefVoucherItem(apSupplier.getApSupplierClass().getScIsVatReliefVoucherItem());

            if (apSupplier.getApSupplierClass().getApTaxCode() != null) {

                mdetails.setSplScTcName(apSupplier.getApSupplierClass().getApTaxCode().getTcName());
                mdetails.setSplScTcType(apSupplier.getApSupplierClass().getApTaxCode().getTcType());
                mdetails.setSplScTcRate(apSupplier.getApSupplierClass().getApTaxCode().getTcRate());
            }

            if (apSupplier.getInvLineItemTemplate() != null) {
                mdetails.setSplLitName(apSupplier.getInvLineItemTemplate().getLitName());
            }

            return mdetails;

        }
        catch (GlobalNoRecordFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApDrBySplSupplierCodeAndTcNameAndWtcNameAndVouBillAmount(String SPL_SPPLR_CODE, String TC_NM, String WTC_NM, double VOU_BLL_AMNT, Integer AD_BRNCH, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ApVoucherEntryControllerBean getApDrBySplSupplierCodeAndTcNameAndWtcNameAndVouBillAmount");
        Debug.print("VOU_BLL_AMNT=" + VOU_BLL_AMNT);

        ArrayList list = new ArrayList();

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            LocalApSupplier apSupplier = null;
            LocalApTaxCode apTaxCode = null;
            LocalApWithholdingTaxCode apWithholdingTaxCode = null;

            try {

                apSupplier = apSupplierHome.findBySplSupplierCode(SPL_SPPLR_CODE, companyCode);
                apTaxCode = apTaxCodeHome.findByTcName(TC_NM, companyCode);
                apWithholdingTaxCode = apWithholdingTaxCodeHome.findByWtcName(WTC_NM, companyCode);

            }
            catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            double NET_AMOUNT = 0d;
            double TAX_AMOUNT = 0d;
            double W_TAX_AMOUNT = 0d;
            short LINE_NUMBER = 0;
            short precisionUnit = commonData.getGlFcPrecisionUnit(companyCode);

            // create dr net expense

            if (apTaxCode.getTcType().equals("INCLUSIVE")) {

                NET_AMOUNT = EJBCommon.roundIt(VOU_BLL_AMNT / (1 + (apTaxCode.getTcRate() / 100)), adPreference.getPrfInvCostPrecisionUnit());

            } else {

                // tax exclusive, none, zero rated or exempt

                NET_AMOUNT = VOU_BLL_AMNT;
            }

            ApModDistributionRecordDetails mdetails = new ApModDistributionRecordDetails();
            mdetails.setDrLine(++LINE_NUMBER);
            mdetails.setDrClass("EXPENSE");
            mdetails.setDrDebit(EJBCommon.TRUE);
            mdetails.setDrAmount(NET_AMOUNT);

            LocalGlChartOfAccount glChartOfAccount = null;
            LocalAdBranchSupplier adBranchSupplier = null;

            try {

                adBranchSupplier = adBranchSupplierHome.findBSplBySplCodeAndBrCode(apSupplier.getSplCode(), AD_BRNCH, companyCode);

            }
            catch (FinderException ex) {
            }

            if (adBranchSupplier != null) {

                glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchSupplier.getBsplGlCoaExpenseAccount());

            } else {

                glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(apSupplier.getSplCoaGlExpenseAccount());
            }

            mdetails.setDrCoaAccountNumber(glChartOfAccount.getCoaAccountNumber());
            mdetails.setDrCoaAccountDescription(glChartOfAccount.getCoaAccountDescription());

            list.add(mdetails);

            // create tax line if necessary

            if (!apTaxCode.getTcType().equals("NONE") && !apTaxCode.getTcType().equals("EXEMPT")) {

                if (apTaxCode.getTcType().equals("INCLUSIVE")) {

                    TAX_AMOUNT = EJBCommon.roundIt(VOU_BLL_AMNT - NET_AMOUNT, precisionUnit);

                } else if (apTaxCode.getTcType().equals("EXCLUSIVE")) {

                    TAX_AMOUNT = EJBCommon.roundIt(VOU_BLL_AMNT * apTaxCode.getTcRate() / 100, precisionUnit);

                } else {

                    // tax none zero-rated or exempt

                }

                mdetails = new ApModDistributionRecordDetails();
                mdetails.setDrLine(++LINE_NUMBER);
                mdetails.setDrClass("TAX");
                mdetails.setDrDebit(EJBCommon.TRUE);
                mdetails.setDrAmount(TAX_AMOUNT);

                mdetails.setDrCoaAccountNumber(apTaxCode.getGlChartOfAccount().getCoaAccountNumber());
                mdetails.setDrCoaAccountDescription(apTaxCode.getGlChartOfAccount().getCoaAccountDescription());

                list.add(mdetails);
            }

            // create withholding tax if necessary

            if (apWithholdingTaxCode.getWtcRate() != 0 && adPreference.getPrfApWTaxRealization().equals("VOUCHER")) {

                W_TAX_AMOUNT = EJBCommon.roundIt(NET_AMOUNT * (apWithholdingTaxCode.getWtcRate() / 100), precisionUnit);

                mdetails = new ApModDistributionRecordDetails();
                mdetails.setDrLine(++LINE_NUMBER);
                mdetails.setDrClass("W-TAX");
                mdetails.setDrDebit(EJBCommon.FALSE);
                mdetails.setDrAmount(W_TAX_AMOUNT);

                mdetails.setDrCoaAccountNumber(apWithholdingTaxCode.getGlChartOfAccount().getCoaAccountNumber());
                mdetails.setDrCoaAccountDescription(apWithholdingTaxCode.getGlChartOfAccount().getCoaAccountDescription());

                list.add(mdetails);
            }

            // create accounts payable

            mdetails = new ApModDistributionRecordDetails();
            mdetails.setDrLine(++LINE_NUMBER);
            mdetails.setDrClass("PAYABLE");
            mdetails.setDrDebit(EJBCommon.FALSE);
            mdetails.setDrAmount(NET_AMOUNT + TAX_AMOUNT - W_TAX_AMOUNT);

            if (adBranchSupplier != null) {

                glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchSupplier.getBsplGlCoaPayableAccount());

            } else {

                glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(apSupplier.getSplCoaGlPayableAccount());
            }

            mdetails.setDrCoaAccountNumber(glChartOfAccount.getCoaAccountNumber());
            mdetails.setDrCoaAccountDescription(glChartOfAccount.getCoaAccountDescription());

            list.add(mdetails);

            return list;

        }
        catch (GlobalNoRecordFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer saveApVouEntry(ApVoucherDetails details, String PYT_NM, String PYT_NM2, String TC_NM, String WTC_NM, String FC_NM, String SPL_SPPLR_CODE, String VB_NM, ArrayList drList, boolean isDraft, Integer AD_BRNCH, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalBranchAccountNumberInvalidException, GlobalPaymentTermInvalidException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalReferenceNumberNotUniqueException {

        Debug.print("ApVoucherEntryControllerBean saveApVouEntry");

        LocalApVoucher apVoucher = null;

        try {

            // validate if voucher is already deleted

            try {

                if (details.getVouCode() != null) {

                    apVoucher = apVoucherHome.findByPrimaryKey(details.getVouCode());
                }

            }
            catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if voucher is already posted, void, approved or pending

            if (details.getVouCode() != null) {

                if (apVoucher.getVouApprovalStatus() != null) {

                    if (apVoucher.getVouApprovalStatus().equals("APPROVED") || apVoucher.getVouApprovalStatus().equals("N/A")) {

                        throw new GlobalTransactionAlreadyApprovedException();

                    } else if (apVoucher.getVouApprovalStatus().equals("PENDING")) {

                        throw new GlobalTransactionAlreadyPendingException();
                    }
                }

                if (apVoucher.getVouPosted() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyPostedException();

                } else if (apVoucher.getVouVoid() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyVoidException();
                }
            }

            // voucher void

            if (details.getVouCode() != null && details.getVouVoid() == EJBCommon.TRUE && apVoucher.getVouPosted() == EJBCommon.FALSE) {

                apVoucher.setVouVoid(EJBCommon.TRUE);
                apVoucher.setVouLastModifiedBy(details.getVouLastModifiedBy());
                apVoucher.setVouDateLastModified(details.getVouDateLastModified());

                return apVoucher.getVouCode();
            }

            // validate if document number is unique document number is automatic then set
            // next sequence

            try {

                LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
                LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

                if (details.getVouCode() == null) {

                    try {

                        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AP VOUCHER", companyCode);

                    }
                    catch (FinderException ex) {
                    }

                    try {

                        adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, companyCode);

                    }
                    catch (FinderException ex) {
                    }

                    LocalApVoucher apExistingVoucher = null;

                    try {

                        apExistingVoucher = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(details.getVouDocumentNumber(), EJBCommon.FALSE, AD_BRNCH, companyCode);

                    }
                    catch (FinderException ex) {
                    }

                    if (apExistingVoucher != null) {

                        throw new GlobalDocumentNumberNotUniqueException();
                    }

                    if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (details.getVouDocumentNumber() == null || details.getVouDocumentNumber().trim().length() == 0)) {

                        while (true) {

                            if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                                try {

                                    apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), EJBCommon.FALSE, AD_BRNCH, companyCode);
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                                }
                                catch (FinderException ex) {

                                    details.setVouDocumentNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                    break;
                                }

                            } else {

                                try {

                                    apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), EJBCommon.FALSE, AD_BRNCH, companyCode);
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                                }
                                catch (FinderException ex) {

                                    details.setVouDocumentNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                    break;
                                }
                            }
                        }
                    }

                } else {

                    LocalApVoucher apExistingVoucher = null;

                    try {

                        apExistingVoucher = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(details.getVouDocumentNumber(), EJBCommon.FALSE, AD_BRNCH, companyCode);

                    }
                    catch (FinderException ex) {
                    }

                    if (apExistingVoucher != null && !apExistingVoucher.getVouCode().equals(details.getVouCode())) {

                        throw new GlobalDocumentNumberNotUniqueException();
                    }

                    if (apVoucher.getVouDocumentNumber() != details.getVouDocumentNumber() && (details.getVouDocumentNumber() == null || details.getVouDocumentNumber().trim().length() == 0)) {

                        details.setVouDocumentNumber(apVoucher.getVouDocumentNumber());
                    }
                }

            }
            catch (GlobalDocumentNumberNotUniqueException ex) {

                getSessionContext().setRollbackOnly();
                throw ex;

            }
            catch (Exception ex) {

                Debug.printStackTrace(ex);
                getSessionContext().setRollbackOnly();
                throw new EJBException(ex.getMessage());
            }

            // validate if conversion date exists

            try {

                if (details.getVouConversionDate() != null) {

                    LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, companyCode);
                    LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(), details.getVouConversionDate(), companyCode);

                    details.setVouConversionRate(glFunctionalCurrencyRate.getFrXToUsd());
                }

            }
            catch (FinderException ex) {

                throw new GlobalConversionDateNotExistException();
            }

            // validate if payment term has at least one payment schedule

            if (adPaymentTermHome.findByPytName(PYT_NM, companyCode).getAdPaymentSchedules().isEmpty()) {

                throw new GlobalPaymentTermInvalidException();
            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            // validate reference number

            if (adPreference.getPrfApReferenceNumberValidation() == EJBCommon.TRUE) {

                Collection apExistingVouchers = null;

                try {

                    apExistingVouchers = apVoucherHome.findByVouReferenceNumber(details.getVouReferenceNumber(), AD_BRNCH, companyCode);

                }
                catch (FinderException ex) {
                }

                if (apExistingVouchers != null && !apExistingVouchers.isEmpty()) {

                    for (Object existingVoucher : apExistingVouchers) {

                        LocalApVoucher apExistingVoucher = (LocalApVoucher) existingVoucher;

                        if (!apExistingVoucher.getVouCode().equals(details.getVouCode()))
                            throw new GlobalReferenceNumberNotUniqueException();
                    }
                }
            }

            // create voucher

            if (details.getVouCode() == null) {

                apVoucher = apVoucherHome.create(details.getVouType(), EJBCommon.FALSE, details.getVouDescription(), details.getVouDate(), details.getVouDocumentNumber(), details.getVouReferenceNumber(), null, details.getVouConversionDate(), details.getVouConversionRate(), details.getVouBillAmount(), details.getVouAmountDue(), 0d, null, null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, details.getVouCreatedBy(), details.getVouDateCreated(), details.getVouLastModifiedBy(), details.getVouDateLastModified(), null, null, null, null, EJBCommon.FALSE, details.getVouPoNumber(), details.getVouLoan(), details.getVouLoanGenerated(), AD_BRNCH, companyCode);

            } else {

                apVoucher.setVouDescription(details.getVouDescription());
                apVoucher.setVouDate(details.getVouDate());
                apVoucher.setVouDocumentNumber(details.getVouDocumentNumber());
                apVoucher.setVouReferenceNumber(details.getVouReferenceNumber());
                apVoucher.setVouConversionDate(details.getVouConversionDate());
                apVoucher.setVouConversionRate(details.getVouConversionRate());
                apVoucher.setVouBillAmount(details.getVouBillAmount());
                apVoucher.setVouAmountDue(details.getVouAmountDue());
                apVoucher.setVouLastModifiedBy(details.getVouLastModifiedBy());
                apVoucher.setVouDateLastModified(details.getVouDateLastModified());
                apVoucher.setVouReasonForRejection(null);
                apVoucher.setVouPoNumber(details.getVouPoNumber());
                apVoucher.setVouLoan(details.getVouLoan());
                apVoucher.setVouLoanGenerated(details.getVouLoanGenerated());
            }

            LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName(PYT_NM, companyCode);
            apVoucher.setAdPaymentTerm(adPaymentTerm);

            try {

                LocalAdPaymentTerm adPaymentTerm2 = adPaymentTermHome.findByPytName(PYT_NM2, companyCode);
                apVoucher.setAdPaymentTerm2(adPaymentTerm2);

            }
            catch (FinderException ex) {
                apVoucher.setAdPaymentTerm2(null);
            }

            LocalApTaxCode apTaxCode = apTaxCodeHome.findByTcName(TC_NM, companyCode);
            apVoucher.setApTaxCode(apTaxCode);

            LocalApWithholdingTaxCode apWithholdingTaxCode = apWithholdingTaxCodeHome.findByWtcName(WTC_NM, companyCode);
            apVoucher.setApWithholdingTaxCode(apWithholdingTaxCode);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, companyCode);
            apVoucher.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalApSupplier apSupplier = apSupplierHome.findBySplSupplierCode(SPL_SPPLR_CODE, companyCode);
            apVoucher.setApSupplier(apSupplier);

            // Generate Description if null
            if (apVoucher.getVouDescription().isEmpty() || apVoucher.getVouDescription() == null) {
                String description = apSupplier.getSplName() + " Date created: " + apVoucher.getVouDateCreated();
                if (apSupplier.getApSupplierType() != null) {
                    description = apSupplier.getApSupplierType().getStName() + " Date created: " + apVoucher.getVouDateCreated();
                }
                apVoucher.setVouDescription(description);

            }

            try {

                Debug.print("-----------------VB_NM=" + VB_NM);

                LocalApVoucherBatch apVoucherBatch = apVoucherBatchHome.findVoucherByVbName(VB_NM, AD_BRNCH, companyCode);
                Debug.print("----------------BATCH NAME=" + apVoucherBatch.getVbName());
                apVoucher.setApVoucherBatch(apVoucherBatch);

            }
            catch (FinderException ex) {

            }

            // remove all voucher line items

            Collection apVoucherLineItems = apVoucher.getApVoucherLineItems();

            Iterator i = apVoucherLineItems.iterator();

            while (i.hasNext()) {

                LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) i.next();

                i.remove();

                // apVoucherLineItem.entityRemove();
                em.remove(apVoucherLineItem);
            }

            // remove all purchase order lines

            Collection apPurchaseOrderLines = apVoucher.getApPurchaseOrderLines();

            i = apPurchaseOrderLines.iterator();

            while (i.hasNext()) {

                LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) i.next();

                i.remove();

                apVoucher.dropApPurchaseOrderLine(apPurchaseOrderLine);
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

            // add new distribution records

            i = drList.iterator();

            while (i.hasNext()) {

                ApModDistributionRecordDetails mDrDetails = (ApModDistributionRecordDetails) i.next();

                this.addApDrEntry(mDrDetails, apVoucher, AD_BRNCH, companyCode);
            }

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

            short precisionUnit = commonData.getGlFcPrecisionUnit(companyCode);

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

                LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = apVoucherPaymentScheduleHome.create(gcDateDue.getTime(), adPaymentSchedule.getPsLineNumber(), PAYMENT_SCHEDULE_AMOUNT, 0d, EJBCommon.FALSE, companyCode);

                // apVoucher.addApVoucherPaymentSchedule(apVoucherPaymentSchedule);
                apVoucherPaymentSchedule.setApVoucher(apVoucher);

                TOTAL_PAYMENT_SCHEDULE += PAYMENT_SCHEDULE_AMOUNT;
            }

            // generate approval status

            String VOU_APPRVL_STATUS = null;

            if (!isDraft) {

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);

                // check if ap voucher approval is enabled

                if (adApproval.getAprEnableApVoucher() == EJBCommon.FALSE && adApproval.getAprEnableApVoucherDepartment() == EJBCommon.FALSE) {

                    VOU_APPRVL_STATUS = "N/A";

                } else {

                    // check if voucher is self approved

                    LocalAdUser adUser2 = adUserHome.findByUsrName(details.getVouLastModifiedBy(), companyCode);

                    VOU_APPRVL_STATUS = apApprovalController.getApprovalStatus(adUser2.getUsrDept(), details.getVouLastModifiedBy(), adUser2.getUsrDescription(), "AP VOUCHER", apVoucher.getVouCode(), apVoucher.getVouDocumentNumber(), apVoucher.getVouDate(), apVoucher.getVouBillAmount(), AD_BRNCH, companyCode);
                }
            }

            if (VOU_APPRVL_STATUS != null && VOU_APPRVL_STATUS.equals("N/A") && adPreference.getPrfApGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                this.executeApVouPost(apVoucher.getVouCode(), apVoucher.getVouLastModifiedBy(), AD_BRNCH, companyCode);
            }

            // set voucher approval status

            apVoucher.setVouApprovalStatus(VOU_APPRVL_STATUS);

            return apVoucher.getVouCode();

        }
        catch (GlobalRecordAlreadyDeletedException | GlobalReferenceNumberNotUniqueException |
               GlobalJournalNotBalanceException | GlJREffectiveDatePeriodClosedException |
               GlJREffectiveDateNoPeriodExistException | GlobalNoApprovalApproverFoundException |
               GlobalNoApprovalRequesterFoundException | GlobalBranchAccountNumberInvalidException |
               GlobalTransactionAlreadyVoidException | GlobalTransactionAlreadyPostedException |
               GlobalTransactionAlreadyPendingException | GlobalTransactionAlreadyApprovedException |
               GlobalPaymentTermInvalidException | GlobalConversionDateNotExistException |
               GlobalDocumentNumberNotUniqueException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer saveApVouVliEntry(ApVoucherDetails details, String PYT_NM, String PYT_NM2, String TC_NM, String WTC_NM, String FC_NM, String SPL_SPPLR_CODE, String VB_NM, ArrayList vliList, boolean isDraft, Integer AD_BRNCH, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalInventoryDateException, GlobalReferenceNumberNotUniqueException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException, GlobalMiscInfoIsRequiredException {

        Debug.print("ApVoucherEntryControllerBean saveApVouVliEntry");

        LocalApVoucher apVoucher = null;

        try {

            // validate if voucher is already deleted

            try {

                if (details.getVouCode() != null) {

                    apVoucher = apVoucherHome.findByPrimaryKey(details.getVouCode());
                }

            }
            catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if voucher is already posted, void, approved or pending

            if (details.getVouCode() != null) {

                if (apVoucher.getVouApprovalStatus() != null) {

                    if (apVoucher.getVouApprovalStatus().equals("APPROVED") || apVoucher.getVouApprovalStatus().equals("N/A")) {

                        throw new GlobalTransactionAlreadyApprovedException();

                    } else if (apVoucher.getVouApprovalStatus().equals("PENDING")) {

                        throw new GlobalTransactionAlreadyPendingException();
                    }
                }

                if (apVoucher.getVouPosted() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyPostedException();

                } else if (apVoucher.getVouVoid() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyVoidException();
                }
            }

            // voucher void

            if (details.getVouCode() != null && details.getVouVoid() == EJBCommon.TRUE && apVoucher.getVouPosted() == EJBCommon.FALSE) {

                apVoucher.setVouVoid(EJBCommon.TRUE);
                apVoucher.setVouLastModifiedBy(details.getVouLastModifiedBy());
                apVoucher.setVouDateLastModified(details.getVouDateLastModified());

                return apVoucher.getVouCode();
            }

            // validate if document number is unique document number is automatic then set
            // next sequence

            try {

                LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
                LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

                if (details.getVouCode() == null) {

                    try {

                        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AP VOUCHER", companyCode);

                    }
                    catch (FinderException ex) {
                    }

                    try {

                        adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, companyCode);

                    }
                    catch (FinderException ex) {

                    }

                    LocalApVoucher apExistingVoucher = null;

                    try {

                        apExistingVoucher = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(details.getVouDocumentNumber(), EJBCommon.FALSE, AD_BRNCH, companyCode);

                    }
                    catch (FinderException ex) {
                    }

                    if (apExistingVoucher != null) {

                        throw new GlobalDocumentNumberNotUniqueException();
                    }

                    if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (details.getVouDocumentNumber() == null || details.getVouDocumentNumber().trim().length() == 0)) {

                        while (true) {

                            if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                                try {

                                    apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), EJBCommon.FALSE, AD_BRNCH, companyCode);
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                                }
                                catch (FinderException ex) {

                                    details.setVouDocumentNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                    break;
                                }

                            } else {

                                try {

                                    apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), EJBCommon.FALSE, AD_BRNCH, companyCode);
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                                }
                                catch (FinderException ex) {

                                    details.setVouDocumentNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                    break;
                                }
                            }
                        }
                    }

                } else {

                    LocalApVoucher apExistingVoucher = null;

                    try {

                        apExistingVoucher = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(details.getVouDocumentNumber(), EJBCommon.FALSE, AD_BRNCH, companyCode);

                    }
                    catch (FinderException ex) {
                    }

                    if (apExistingVoucher != null && !apExistingVoucher.getVouCode().equals(details.getVouCode())) {

                        throw new GlobalDocumentNumberNotUniqueException();
                    }

                    if (apVoucher.getVouDocumentNumber() != details.getVouDocumentNumber() && (details.getVouDocumentNumber() == null || details.getVouDocumentNumber().trim().length() == 0)) {

                        details.setVouDocumentNumber(apVoucher.getVouDocumentNumber());
                    }
                }

            }
            catch (GlobalDocumentNumberNotUniqueException ex) {

                getSessionContext().setRollbackOnly();
                throw ex;

            }
            catch (Exception ex) {

                Debug.printStackTrace(ex);
                getSessionContext().setRollbackOnly();
                throw new EJBException(ex.getMessage());
            }

            // validate if conversion date exists

            try {

                if (details.getVouConversionDate() != null) {

                    LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, companyCode);
                    LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(), details.getVouConversionDate(), companyCode);

                    details.setVouConversionRate(glFunctionalCurrencyRate.getFrXToUsd());
                }

            }
            catch (FinderException ex) {

                throw new GlobalConversionDateNotExistException();
            }

            // validate if payment term has at least one payment schedule

            if (adPaymentTermHome.findByPytName(PYT_NM, companyCode).getAdPaymentSchedules().isEmpty()) {

                throw new GlobalPaymentTermInvalidException();
            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            // validate reference number

            if (adPreference.getPrfApReferenceNumberValidation() == EJBCommon.TRUE) {

                Collection apExistingVouchers = null;

                try {

                    apExistingVouchers = apVoucherHome.findByVouReferenceNumber(details.getVouReferenceNumber(), AD_BRNCH, companyCode);

                }
                catch (FinderException ex) {
                }

                if (apExistingVouchers != null && !apExistingVouchers.isEmpty()) {

                    for (Object existingVoucher : apExistingVouchers) {

                        LocalApVoucher apExistingVoucher = (LocalApVoucher) existingVoucher;

                        if (!apExistingVoucher.getVouCode().equals(details.getVouCode()))
                            throw new GlobalReferenceNumberNotUniqueException();
                    }
                }
            }

            // used in checking if voucher should re-generate distribution records and
            // re-calculate taxes
            boolean isRecalculate = true;

            // create voucher

            if (details.getVouCode() == null) {

                apVoucher = apVoucherHome.create(details.getVouType(), EJBCommon.FALSE, details.getVouDescription(), details.getVouDate(), details.getVouDocumentNumber(), details.getVouReferenceNumber(), null, details.getVouConversionDate(), details.getVouConversionRate(), 0d, 0d, 0d, null, null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, details.getVouCreatedBy(), details.getVouDateCreated(), details.getVouLastModifiedBy(), details.getVouDateLastModified(), null, null, null, null, EJBCommon.FALSE, details.getVouPoNumber(), details.getVouLoan(), details.getVouLoanGenerated(), AD_BRNCH, companyCode);

            } else {

                // check if critical fields are changed

                if (!apVoucher.getApTaxCode().getTcName().equals(TC_NM) || !apVoucher.getApWithholdingTaxCode().getWtcName().equals(WTC_NM) || !apVoucher.getApSupplier().getSplSupplierCode().equals(SPL_SPPLR_CODE) || !apVoucher.getAdPaymentTerm().getPytName().equals(PYT_NM) || vliList.size() != apVoucher.getApVoucherLineItems().size() || !(apVoucher.getVouDate().equals(details.getVouDate()))) {

                    isRecalculate = true;

                } else if (vliList.size() == apVoucher.getApVoucherLineItems().size()) {

                    Iterator ilIter = apVoucher.getApVoucherLineItems().iterator();
                    Iterator ilListIter = vliList.iterator();

                    while (ilIter.hasNext()) {

                        LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) ilIter.next();
                        ApModVoucherLineItemDetails mdetails = (ApModVoucherLineItemDetails) ilListIter.next();

                        if (apVoucherLineItem.getInvItemLocation().getInvItem().getIiIsVatRelief() == EJBCommon.TRUE) {

                            if (!apVoucherLineItem.getVliSplName().equals(mdetails.getVliSplName()) || !apVoucherLineItem.getVliSplTin().equals(mdetails.getVliSplTin()) || !apVoucherLineItem.getVliSplAddress().equals(mdetails.getVliSplAddress())) {

                                isRecalculate = true;
                                break;
                            }
                        }
                        Debug.print("mdetails.getVliProjectCode()=" + mdetails.getVliProjectCode());
                        if (mdetails.getVliProjectCode() != null) {
                            // !apVoucherLineItem.getPmProject().getPrjProjectCode().equals(mdetails.getVliProjectCode())

                            isRecalculate = true;
                            break;
                        }

                        if (!apVoucherLineItem.getInvItemLocation().getInvItem().getIiName().equals(mdetails.getVliIiName()) || !apVoucherLineItem.getInvItemLocation().getInvLocation().getLocName().equals(mdetails.getVliLocName()) || !apVoucherLineItem.getInvUnitOfMeasure().getUomName().equals(mdetails.getVliUomName()) || apVoucherLineItem.getVliQuantity() != mdetails.getVliQuantity() || apVoucherLineItem.getVliUnitCost() != mdetails.getVliUnitCost() || apVoucherLineItem.getVliTotalDiscount() != mdetails.getVliTotalDiscount() || apVoucherLineItem.getVliMisc() != mdetails.getVliMisc() || apVoucherLineItem.getVliTax() != mdetails.getVliTax()) {

                            isRecalculate = true;
                            break;
                        }

                        isRecalculate = false;
                    }

                } else {

                    isRecalculate = false;
                }

                apVoucher.setVouDescription(details.getVouDescription());
                apVoucher.setVouDate(details.getVouDate());
                apVoucher.setVouDocumentNumber(details.getVouDocumentNumber());
                apVoucher.setVouReferenceNumber(details.getVouReferenceNumber());
                apVoucher.setVouConversionDate(details.getVouConversionDate());
                apVoucher.setVouConversionRate(details.getVouConversionRate());
                apVoucher.setVouLastModifiedBy(details.getVouLastModifiedBy());
                apVoucher.setVouDateLastModified(details.getVouDateLastModified());
                apVoucher.setVouReasonForRejection(null);
                apVoucher.setVouPoNumber(details.getVouPoNumber());
                apVoucher.setVouLoan(details.getVouLoan());
                apVoucher.setVouLoanGenerated(details.getVouLoanGenerated());
            }

            LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName(PYT_NM, companyCode);
            apVoucher.setAdPaymentTerm(adPaymentTerm);

            try {

                LocalAdPaymentTerm adPaymentTerm2 = adPaymentTermHome.findByPytName(PYT_NM2, companyCode);
                apVoucher.setAdPaymentTerm2(adPaymentTerm2);

            }
            catch (FinderException ex) {
                apVoucher.setAdPaymentTerm2(null);
            }

            LocalApTaxCode apTaxCode = apTaxCodeHome.findByTcName(TC_NM, companyCode);
            apVoucher.setApTaxCode(apTaxCode);

            LocalApWithholdingTaxCode apWithholdingTaxCode = apWithholdingTaxCodeHome.findByWtcName(WTC_NM, companyCode);
            apVoucher.setApWithholdingTaxCode(apWithholdingTaxCode);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, companyCode);
            apVoucher.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalApSupplier apSupplier = apSupplierHome.findBySplSupplierCode(SPL_SPPLR_CODE, companyCode);
            apVoucher.setApSupplier(apSupplier);

            try {

                LocalApVoucherBatch apVoucherBatch = apVoucherBatchHome.findVoucherByVbName(VB_NM, AD_BRNCH, companyCode);
                apVoucher.setApVoucherBatch(apVoucherBatch);

            }
            catch (FinderException ex) {

            }

            if (isRecalculate) {

                // remove all voucher line items

                Collection apVoucherLineItems = apVoucher.getApVoucherLineItems();

                Iterator i = apVoucherLineItems.iterator();

                while (i.hasNext()) {

                    LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) i.next();

                    // remove all inv tag inside PO line
                    Collection invTags = apVoucherLineItem.getInvTags();

                    Iterator x = invTags.iterator();

                    while (x.hasNext()) {

                        LocalInvTag invTag = (LocalInvTag) x.next();

                        x.remove();

                        // invTag.entityRemove();
                        em.remove(invTag);
                    }

                    i.remove();

                    // apVoucherLineItem.entityRemove();
                    em.remove(apVoucherLineItem);
                }

                // remove all purchase order lines

                Collection apPurchaseOrderLines = apVoucher.getApPurchaseOrderLines();

                i = apPurchaseOrderLines.iterator();

                while (i.hasNext()) {

                    LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) i.next();

                    i.remove();

                    apVoucher.dropApPurchaseOrderLine(apPurchaseOrderLine);
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

                i = vliList.iterator();

                LocalInvItemLocation invItemLocation = null;

                while (i.hasNext()) {

                    ApModVoucherLineItemDetails mVliDetails = (ApModVoucherLineItemDetails) i.next();

                    try {

                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(mVliDetails.getVliLocName(), mVliDetails.getVliIiName(), companyCode);

                    }
                    catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(mVliDetails.getVliLine()));
                    }

                    // start date validation
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {

                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(apVoucher.getVouDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                        if (!invNegTxnCosting.isEmpty())
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                    }

                    LocalApVoucherLineItem apVoucherLineItem = this.addApVliEntry(mVliDetails, apVoucher, invItemLocation, companyCode);

                    // add inventory distributions

                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {

                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(apVoucherLineItem.getInvItemLocation().getIlCode(), AD_BRNCH, companyCode);

                    }
                    catch (FinderException ex) {
                    }

                    if (adBranchItemLocation != null) {

                        this.addApDrVliEntry(apVoucher.getApDrNextLine(), "EXPENSE", EJBCommon.TRUE, apVoucherLineItem.getVliAmount(), adBranchItemLocation.getBilCoaGlInventoryAccount(), apVoucher, AD_BRNCH, companyCode);

                    } else {

                        this.addApDrVliEntry(apVoucher.getApDrNextLine(), "EXPENSE", EJBCommon.TRUE, apVoucherLineItem.getVliAmount(), apVoucherLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(), apVoucher, AD_BRNCH, companyCode);
                    }

                    TOTAL_LINE += apVoucherLineItem.getVliAmount();
                    TOTAL_TAX += apVoucherLineItem.getVliTaxAmount();
                }

                // add tax distribution if necessary

                if (!apTaxCode.getTcType().equals("NONE") && !apTaxCode.getTcType().equals("EXEMPT")) {

                    LocalAdBranchApTaxCode adBranchTaxCode = null;
                    Debug.print("ApVoucherEntryControllerBean saveApVouVliEntry tax");
                    try {
                        adBranchTaxCode = adBranchApTaxCodeHome.findBtcByTcCodeAndBrCode(apVoucher.getApTaxCode().getTcCode(), AD_BRNCH, companyCode);

                    }
                    catch (FinderException ex) {

                    }

                    if (adBranchTaxCode != null) {

                        this.addApDrVliEntry(apVoucher.getApDrNextLine(), "TAX", EJBCommon.TRUE, TOTAL_TAX, adBranchTaxCode.getBtcGlCoaTaxCode(), apVoucher, AD_BRNCH, companyCode);

                    } else {

                        this.addApDrVliEntry(apVoucher.getApDrNextLine(), "TAX", EJBCommon.TRUE, TOTAL_TAX, apTaxCode.getGlChartOfAccount().getCoaCode(), apVoucher, AD_BRNCH, companyCode);
                    }
                }

                // add wtax distribution if necessary

                double W_TAX_AMOUNT = 0d;

                if (apWithholdingTaxCode.getWtcRate() != 0 && adPreference.getPrfApWTaxRealization().equals("VOUCHER")) {

                    W_TAX_AMOUNT = EJBCommon.roundIt(TOTAL_LINE * (apWithholdingTaxCode.getWtcRate() / 100), commonData.getGlFcPrecisionUnit(companyCode));

                    this.addApDrVliEntry(apVoucher.getApDrNextLine(), "W-TAX", EJBCommon.FALSE, W_TAX_AMOUNT, apWithholdingTaxCode.getGlChartOfAccount().getCoaCode(), apVoucher, AD_BRNCH, companyCode);
                }

                // add payable distribution

                LocalAdBranchSupplier adBranchSupplier = null;

                try {

                    adBranchSupplier = adBranchSupplierHome.findBSplBySplCodeAndBrCode(apVoucher.getApSupplier().getSplCode(), AD_BRNCH, companyCode);

                }
                catch (FinderException ex) {
                }

                if (adBranchSupplier != null) {

                    this.addApDrVliEntry(apVoucher.getApDrNextLine(), "PAYABLE", EJBCommon.FALSE, TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT, adBranchSupplier.getBsplGlCoaPayableAccount(), apVoucher, AD_BRNCH, companyCode);

                } else {

                    this.addApDrVliEntry(apVoucher.getApDrNextLine(), "PAYABLE", EJBCommon.FALSE, TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT, apVoucher.getApSupplier().getSplCoaGlPayableAccount(), apVoucher, AD_BRNCH, companyCode);
                }

                // set voucher amount due

                apVoucher.setVouAmountDue(TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT);

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

                short precisionUnit = commonData.getGlFcPrecisionUnit(companyCode);
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

                    LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = apVoucherPaymentScheduleHome.create(gcDateDue.getTime(), adPaymentSchedule.getPsLineNumber(), PAYMENT_SCHEDULE_AMOUNT, 0d, EJBCommon.FALSE, companyCode);
                    // apVoucher.addApVoucherPaymentSchedule(apVoucherPaymentSchedule);
                    apVoucherPaymentSchedule.setApVoucher(apVoucher);

                    TOTAL_PAYMENT_SCHEDULE += PAYMENT_SCHEDULE_AMOUNT;
                }

            } else {

                Iterator i = vliList.iterator();

                LocalInvItemLocation invItemLocation = null;

                while (i.hasNext()) {

                    ApModVoucherLineItemDetails mVliDetails = (ApModVoucherLineItemDetails) i.next();

                    try {

                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(mVliDetails.getVliLocName(), mVliDetails.getVliIiName(), companyCode);

                    }
                    catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(mVliDetails.getVliLine()));
                    }

                    // start date validation
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(apVoucher.getVouDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                        if (!invNegTxnCosting.isEmpty())
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                    }
                }
            }

            // generate approval status

            String VOU_APPRVL_STATUS = null;

            if (!isDraft) {

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);

                // check if ap voucher approval is enabled

                if (adApproval.getAprEnableApVoucher() == EJBCommon.FALSE && adApproval.getAprEnableApVoucherDepartment() == EJBCommon.FALSE) {

                    VOU_APPRVL_STATUS = "N/A";

                } else {

                    // check if voucher is self approved

                    LocalAdUser adUser2 = adUserHome.findByUsrName(details.getVouLastModifiedBy(), companyCode);

                    VOU_APPRVL_STATUS = apApprovalController.getApprovalStatus(adUser2.getUsrDept(), details.getVouLastModifiedBy(), adUser2.getUsrDescription(), "AP VOUCHER", apVoucher.getVouCode(), apVoucher.getVouDocumentNumber(), apVoucher.getVouDate(), apVoucher.getVouBillAmount(), AD_BRNCH, companyCode);
                }
            }

            if (VOU_APPRVL_STATUS != null && VOU_APPRVL_STATUS.equals("N/A") && adPreference.getPrfApGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                this.executeApVouPost(apVoucher.getVouCode(), apVoucher.getVouLastModifiedBy(), AD_BRNCH, companyCode);
            }

            // set voucher approval status

            apVoucher.setVouApprovalStatus(VOU_APPRVL_STATUS);

            return apVoucher.getVouCode();

        }
        catch (GlobalRecordAlreadyDeletedException | GlobalMiscInfoIsRequiredException |
               AdPRFCoaGlVarianceAccountNotFoundException | GlobalReferenceNumberNotUniqueException |
               GlobalBranchAccountNumberInvalidException | GlobalInventoryDateException |
               GlobalJournalNotBalanceException | GlJREffectiveDatePeriodClosedException |
               GlJREffectiveDateNoPeriodExistException | GlobalInvItemLocationNotFoundException |
               GlobalNoApprovalApproverFoundException | GlobalNoApprovalRequesterFoundException |
               GlobalTransactionAlreadyVoidException | GlobalTransactionAlreadyPostedException |
               GlobalTransactionAlreadyPendingException | GlobalTransactionAlreadyApprovedException |
               GlobalPaymentTermInvalidException | GlobalConversionDateNotExistException |
               GlobalDocumentNumberNotUniqueException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer saveApVouPlEntry(ApVoucherDetails details, String PYT_NM, String TC_NM, String WTC_NM, String FC_NM, String SPL_SPPLR_CODE, String VB_NM, ArrayList plList, boolean isDraft, Integer AD_BRNCH, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalPaymentTermInvalidException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalInventoryDateException, GlobalReferenceNumberNotUniqueException, GlobalBranchAccountNumberInvalidException {

        Debug.print("ApVoucherEntryControllerBean saveApVouPlEntry");

        LocalApVoucher apVoucher = null;

        try {

            // validate if voucher is already deleted

            try {

                if (details.getVouCode() != null) {

                    apVoucher = apVoucherHome.findByPrimaryKey(details.getVouCode());
                }

            }
            catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if voucher is already posted, void, approved or pending

            if (details.getVouCode() != null) {

                if (apVoucher.getVouApprovalStatus() != null) {

                    if (apVoucher.getVouApprovalStatus().equals("APPROVED") || apVoucher.getVouApprovalStatus().equals("N/A")) {

                        throw new GlobalTransactionAlreadyApprovedException();

                    } else if (apVoucher.getVouApprovalStatus().equals("PENDING")) {

                        throw new GlobalTransactionAlreadyPendingException();
                    }
                }

                if (apVoucher.getVouPosted() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyPostedException();

                } else if (apVoucher.getVouVoid() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyVoidException();
                }
            }

            // voucher void

            if (details.getVouCode() != null && details.getVouVoid() == EJBCommon.TRUE && apVoucher.getVouPosted() == EJBCommon.FALSE) {

                apVoucher.setVouVoid(EJBCommon.TRUE);
                apVoucher.setVouLastModifiedBy(details.getVouLastModifiedBy());
                apVoucher.setVouDateLastModified(details.getVouDateLastModified());

                return apVoucher.getVouCode();
            }

            // validate if document number is unique document number is automatic then set
            // next sequence

            try {

                LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
                LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

                if (details.getVouCode() == null) {

                    try {

                        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AP VOUCHER", companyCode);

                    }
                    catch (FinderException ex) {
                    }

                    try {

                        adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, companyCode);

                    }
                    catch (FinderException ex) {
                    }

                    LocalApVoucher apExistingVoucher = null;

                    try {

                        apExistingVoucher = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(details.getVouDocumentNumber(), EJBCommon.FALSE, AD_BRNCH, companyCode);

                    }
                    catch (FinderException ex) {
                    }

                    if (apExistingVoucher != null) {

                        throw new GlobalDocumentNumberNotUniqueException();
                    }

                    if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (details.getVouDocumentNumber() == null || details.getVouDocumentNumber().trim().length() == 0)) {

                        while (true) {

                            if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                                try {

                                    apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), EJBCommon.FALSE, AD_BRNCH, companyCode);
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                                }
                                catch (FinderException ex) {

                                    details.setVouDocumentNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                    break;
                                }

                            } else {

                                try {

                                    apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), EJBCommon.FALSE, AD_BRNCH, companyCode);
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                                }
                                catch (FinderException ex) {

                                    details.setVouDocumentNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                    break;
                                }
                            }
                        }
                    }

                } else {

                    LocalApVoucher apExistingVoucher = null;

                    try {

                        apExistingVoucher = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(details.getVouDocumentNumber(), EJBCommon.FALSE, AD_BRNCH, companyCode);

                    }
                    catch (FinderException ex) {
                    }

                    if (apExistingVoucher != null && !apExistingVoucher.getVouCode().equals(details.getVouCode())) {

                        throw new GlobalDocumentNumberNotUniqueException();
                    }

                    if (apVoucher.getVouDocumentNumber() != details.getVouDocumentNumber() && (details.getVouDocumentNumber() == null || details.getVouDocumentNumber().trim().length() == 0)) {

                        details.setVouDocumentNumber(apVoucher.getVouDocumentNumber());
                    }
                }

            }
            catch (GlobalDocumentNumberNotUniqueException ex) {

                getSessionContext().setRollbackOnly();
                throw ex;

            }
            catch (Exception ex) {

                Debug.printStackTrace(ex);
                getSessionContext().setRollbackOnly();
                throw new EJBException(ex.getMessage());
            }

            // validate if conversion date exists

            try {

                if (details.getVouConversionDate() != null) {

                    LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, companyCode);
                    LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(), details.getVouConversionDate(), companyCode);

                    details.setVouConversionRate(glFunctionalCurrencyRate.getFrXToUsd());
                }

            }
            catch (FinderException ex) {

                throw new GlobalConversionDateNotExistException();
            }

            // validate if payment term has at least one payment schedule

            if (adPaymentTermHome.findByPytName(PYT_NM, companyCode).getAdPaymentSchedules().isEmpty()) {

                throw new GlobalPaymentTermInvalidException();
            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            // validate reference number

            if (adPreference.getPrfApReferenceNumberValidation() == EJBCommon.TRUE) {

                Collection apExistingVouchers = null;

                try {

                    apExistingVouchers = apVoucherHome.findByVouReferenceNumber(details.getVouReferenceNumber(), AD_BRNCH, companyCode);

                }
                catch (FinderException ex) {
                }

                if (apExistingVouchers != null && !apExistingVouchers.isEmpty()) {

                    for (Object existingVoucher : apExistingVouchers) {

                        LocalApVoucher apExistingVoucher = (LocalApVoucher) existingVoucher;

                        if (!apExistingVoucher.getVouCode().equals(details.getVouCode()))
                            throw new GlobalReferenceNumberNotUniqueException();
                    }
                }
            }

            // used in checking if voucher should re-generate distribution records and
            // re-calculate taxes

            boolean isRecalculate = true;

            // create voucher (header)

            if (details.getVouCode() == null) {

                apVoucher = apVoucherHome.create(details.getVouType(), EJBCommon.FALSE, details.getVouDescription(), details.getVouDate(), details.getVouDocumentNumber(), details.getVouReferenceNumber(), null, details.getVouConversionDate(), details.getVouConversionRate(), 0d, 0d, 0d, null, null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, details.getVouCreatedBy(), details.getVouDateCreated(), details.getVouLastModifiedBy(), details.getVouDateLastModified(), null, null, null, null, EJBCommon.FALSE, details.getVouPoNumber(), details.getVouLoan(), details.getVouLoanGenerated(), AD_BRNCH, companyCode);

            } else {

                // check if critical fields are changed

                if (!apVoucher.getApTaxCode().getTcName().equals(TC_NM) || !apVoucher.getApWithholdingTaxCode().getWtcName().equals(WTC_NM) || !apVoucher.getApSupplier().getSplSupplierCode().equals(SPL_SPPLR_CODE) || !apVoucher.getAdPaymentTerm().getPytName().equals(PYT_NM) || plList.size() != apVoucher.getApPurchaseOrderLines().size() || !(apVoucher.getVouDate().equals(details.getVouDate()))) {

                    isRecalculate = true;

                } else if (plList.size() == apVoucher.getApPurchaseOrderLines().size()) {

                    Iterator ilIter = apVoucher.getApPurchaseOrderLines().iterator();
                    Iterator ilListIter = plList.iterator();

                    while (ilIter.hasNext()) {

                        LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) ilIter.next();
                        LocalApPurchaseOrderLine apPurchaseOrderLineList = apPurchaseOrderLineHome.findByPrimaryKey((Integer) ilListIter.next());

                        if (!apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiName().equals(apPurchaseOrderLineList.getInvItemLocation().getInvItem().getIiName()) || !apPurchaseOrderLine.getInvItemLocation().getInvLocation().getLocName().equals(apPurchaseOrderLineList.getInvItemLocation().getInvLocation().getLocName()) || !apPurchaseOrderLine.getInvUnitOfMeasure().getUomName().equals(apPurchaseOrderLineList.getInvUnitOfMeasure().getUomName()) || apPurchaseOrderLine.getPlQuantity() != apPurchaseOrderLineList.getPlQuantity() || apPurchaseOrderLine.getPlUnitCost() != apPurchaseOrderLineList.getPlUnitCost() || apPurchaseOrderLine.getPlAmount() != apPurchaseOrderLineList.getPlAmount() || !apPurchaseOrderLine.getApPurchaseOrder().getPoDocumentNumber().equals(apPurchaseOrderLineList.getApPurchaseOrder().getPoDocumentNumber()) || !apPurchaseOrderLine.getApPurchaseOrder().getPoDescription().equals(apPurchaseOrderLineList.getApPurchaseOrder().getPoDescription())) {

                            isRecalculate = true;
                            break;
                        }

                        if ((apPurchaseOrderLine.getApPurchaseOrder().getPoRcvPoNumber() != null && apPurchaseOrderLineList.getApPurchaseOrder().getPoRcvPoNumber() == null) || (apPurchaseOrderLine.getApPurchaseOrder().getPoRcvPoNumber() == null && apPurchaseOrderLineList.getApPurchaseOrder().getPoRcvPoNumber() != null) || (apPurchaseOrderLine.getApPurchaseOrder().getPoRcvPoNumber() != null && apPurchaseOrderLineList.getApPurchaseOrder().getPoRcvPoNumber() != null && !apPurchaseOrderLine.getApPurchaseOrder().getPoRcvPoNumber().equals(apPurchaseOrderLineList.getApPurchaseOrder().getPoRcvPoNumber()))) {

                            isRecalculate = true;
                            break;
                        }

                        // isRecalculate = false;

                    }

                } else {

                    // isRecalculate = false;

                }

                apVoucher.setVouDescription(details.getVouDescription());
                apVoucher.setVouDate(details.getVouDate());
                apVoucher.setVouDocumentNumber(details.getVouDocumentNumber());
                apVoucher.setVouReferenceNumber(details.getVouReferenceNumber());
                apVoucher.setVouConversionDate(details.getVouConversionDate());
                apVoucher.setVouConversionRate(details.getVouConversionRate());
                apVoucher.setVouLastModifiedBy(details.getVouLastModifiedBy());
                apVoucher.setVouDateLastModified(details.getVouDateLastModified());
                apVoucher.setVouReasonForRejection(null);
                apVoucher.setVouPoNumber(details.getVouPoNumber());
                apVoucher.setVouLoan(details.getVouLoan());
                apVoucher.setVouLoanGenerated(details.getVouLoanGenerated());
            }

            LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName(PYT_NM, companyCode);
            apVoucher.setAdPaymentTerm(adPaymentTerm);

            LocalApTaxCode apTaxCode = apTaxCodeHome.findByTcName(TC_NM, companyCode);
            apVoucher.setApTaxCode(apTaxCode);

            LocalApWithholdingTaxCode apWithholdingTaxCode = apWithholdingTaxCodeHome.findByWtcName(WTC_NM, companyCode);
            apVoucher.setApWithholdingTaxCode(apWithholdingTaxCode);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, companyCode);
            apVoucher.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalApSupplier apSupplier = apSupplierHome.findBySplSupplierCode(SPL_SPPLR_CODE, companyCode);
            apVoucher.setApSupplier(apSupplier);

            try {

                Debug.print("-----------------VB_NM=" + VB_NM);

                LocalApVoucherBatch apVoucherBatch = apVoucherBatchHome.findVoucherByVbName(VB_NM, AD_BRNCH, companyCode);
                Debug.print("----------------BATCH NAME=" + apVoucherBatch.getVbName());
                apVoucher.setApVoucherBatch(apVoucherBatch);
            }
            catch (FinderException ex) {

            }

            // create voucher(lines)

            if (isRecalculate) {

                // remove all voucher line items

                Collection apVoucherLineItems = apVoucher.getApVoucherLineItems();

                Iterator i = apVoucherLineItems.iterator();

                while (i.hasNext()) {

                    LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) i.next();

                    // remove all inv tag inside PO line
                    Collection invTags = apVoucherLineItem.getInvTags();

                    Iterator x = invTags.iterator();

                    while (x.hasNext()) {

                        LocalInvTag invTag = (LocalInvTag) x.next();

                        x.remove();

                        // invTag.entityRemove();
                        em.remove(invTag);
                    }

                    i.remove();

                    // apVoucherLineItem.entityRemove();
                    em.remove(apVoucherLineItem);
                }

                // remove all purchase order lines

                Collection apPurchaseOrderLines = apVoucher.getApPurchaseOrderLines();

                i = apPurchaseOrderLines.iterator();

                while (i.hasNext()) {

                    LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) i.next();

                    i.remove();

                    apVoucher.dropApPurchaseOrderLine(apPurchaseOrderLine);
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

                double vatAmount = 0;
                double totalAmountDue = 0;

                i = plList.iterator();
                double TOTAL_LINE = 0d;
                double TOTAL_TAX = 0d;

                while (i.hasNext()) {

                    LocalApPurchaseOrderLine apPurchaseOrderLine = apPurchaseOrderLineHome.findByPrimaryKey((Integer) i.next());
                    LocalInvItemLocation invItemLocation = null;
                    try {

                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(apPurchaseOrderLine.getInvItemLocation().getInvLocation().getLocName(), apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiName(), companyCode);

                    }
                    catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(apPurchaseOrderLine.getPlLine()));
                    }

                    // start date validation
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(apVoucher.getVouDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                        if (!invNegTxnCosting.isEmpty())
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                    }

                    apPurchaseOrderLine.setApVoucher(apVoucher);

                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {

                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(apPurchaseOrderLine.getInvItemLocation().getIlCode(), AD_BRNCH, companyCode);

                    }
                    catch (FinderException ex) {
                    }

                    double PL_AMOUNT = this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apPurchaseOrderLine.getPlAmount(), companyCode);

                    double TAX = this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apPurchaseOrderLine.getPlTaxAmount(), companyCode);

                    if (adBranchItemLocation != null) {

                        this.addApDrVliEntry(apVoucher.getApDrNextLine(), "ACC INV", EJBCommon.TRUE, PL_AMOUNT + TAX, adBranchItemLocation.getBilCoaGlAccruedInventoryAccount(), apVoucher, AD_BRNCH, companyCode);

                    } else {

                        this.addApDrVliEntry(apVoucher.getApDrNextLine(), "ACC INV", EJBCommon.TRUE, PL_AMOUNT + TAX, apPurchaseOrderLine.getInvItemLocation().getIlGlCoaAccruedInventoryAccount(), apVoucher, AD_BRNCH, companyCode);
                    }

                    TOTAL_LINE += PL_AMOUNT;
                    TOTAL_TAX += TAX;

                    totalAmountDue += apPurchaseOrderLine.getPlAmount() + apPurchaseOrderLine.getPlTaxAmount();

                    Debug.print("currency : " + apVoucher.getGlFunctionalCurrency().getFcName());
                    Debug.print("curr rate: " + apVoucher.getVouConversionRate());
                    Debug.print("amount is: " + PL_AMOUNT);
                    Debug.print("2nd amount is : " + totalAmountDue);
                    System.out.print("tax is: " + TAX);
                }

                // add tax distribution if necessary

                if (!apTaxCode.getTcType().equals("NONE") && !apTaxCode.getTcType().equals("EXEMPT")) {

                    LocalAdBranchApTaxCode adBranchTaxCode = null;

                    try {
                        adBranchTaxCode = adBranchApTaxCodeHome.findBtcByTcCodeAndBrCode(apVoucher.getApTaxCode().getTcCode(), AD_BRNCH, companyCode);

                    }
                    catch (FinderException ex) {

                    }

                    if (adBranchTaxCode != null) {

                        this.addApDrVliEntry(apVoucher.getApDrNextLine(), "TAX", EJBCommon.TRUE, TOTAL_TAX, adBranchTaxCode.getBtcGlCoaTaxCode(), apVoucher, AD_BRNCH, companyCode);

                    } else {

                        this.addApDrVliEntry(apVoucher.getApDrNextLine(), "TAX", EJBCommon.TRUE, TOTAL_TAX, apTaxCode.getGlChartOfAccount().getCoaCode(), apVoucher, AD_BRNCH, companyCode);
                    }
                }

                LocalAdBranchSupplier adBranchSupplier = null;

                try {

                    adBranchSupplier = adBranchSupplierHome.findBSplBySplCodeAndBrCode(apVoucher.getApSupplier().getSplCode(), AD_BRNCH, companyCode);

                }
                catch (FinderException ex) {
                }

                if (adBranchSupplier != null) {

                    this.addApDrVliEntry(apVoucher.getApDrNextLine(), "PAYABLE", EJBCommon.FALSE, TOTAL_LINE + TOTAL_TAX, // totalAmountDue,
                            adBranchSupplier.getBsplGlCoaPayableAccount(), apVoucher, AD_BRNCH, companyCode);

                } else {

                    this.addApDrVliEntry(apVoucher.getApDrNextLine(), "PAYABLE", EJBCommon.FALSE, TOTAL_LINE + TOTAL_TAX, // totalAmountDue,
                            apVoucher.getApSupplier().getSplCoaGlPayableAccount(), apVoucher, AD_BRNCH, companyCode);
                }

                // set voucher amount due

                apVoucher.setVouAmountDue(totalAmountDue);

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

                short precisionUnit = commonData.getGlFcPrecisionUnit(companyCode);
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

                    LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = apVoucherPaymentScheduleHome.create(gcDateDue.getTime(), adPaymentSchedule.getPsLineNumber(), PAYMENT_SCHEDULE_AMOUNT, 0d, EJBCommon.FALSE, companyCode);

                    apVoucherPaymentSchedule.setApVoucher(apVoucher);

                    TOTAL_PAYMENT_SCHEDULE += PAYMENT_SCHEDULE_AMOUNT;
                }
            }

            // generate approval status

            String VOU_APPRVL_STATUS = null;

            if (!isDraft) {

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);

                // check if ap voucher approval is enabled

                if (adApproval.getAprEnableApVoucher() == EJBCommon.FALSE) {

                    VOU_APPRVL_STATUS = "N/A";

                } else {

                    // check if voucher is self approved

                    LocalAdUser adUser2 = adUserHome.findByUsrName(details.getVouLastModifiedBy(), companyCode);

                    VOU_APPRVL_STATUS = apApprovalController.getApprovalStatus(adUser2.getUsrDept(), details.getVouLastModifiedBy(), adUser2.getUsrDescription(), "AP VOUCHER", apVoucher.getVouCode(), apVoucher.getVouDocumentNumber(), details.getVouDate(), apVoucher.getVouBillAmount(), AD_BRNCH, companyCode);
                }
            }

            if (VOU_APPRVL_STATUS != null && VOU_APPRVL_STATUS.equals("N/A") && adPreference.getPrfApGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                this.executeApVouPost(apVoucher.getVouCode(), apVoucher.getVouLastModifiedBy(), AD_BRNCH, companyCode);
            }

            // set voucher approval status

            apVoucher.setVouApprovalStatus(VOU_APPRVL_STATUS);

            return apVoucher.getVouCode();

        }
        catch (GlobalRecordAlreadyDeletedException | GlobalReferenceNumberNotUniqueException |
               GlobalBranchAccountNumberInvalidException | GlobalInventoryDateException |
               GlobalJournalNotBalanceException | GlJREffectiveDatePeriodClosedException |
               GlJREffectiveDateNoPeriodExistException | GlobalNoApprovalApproverFoundException |
               GlobalNoApprovalRequesterFoundException | GlobalTransactionAlreadyVoidException |
               GlobalTransactionAlreadyPostedException | GlobalTransactionAlreadyPendingException |
               GlobalTransactionAlreadyApprovedException | GlobalPaymentTermInvalidException |
               GlobalConversionDateNotExistException | GlobalDocumentNumberNotUniqueException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteApVouEntry(Integer VOU_CODE, String AD_USR, Integer companyCode) throws GlobalRecordAlreadyDeletedException {

        Debug.print("ApVoucherEntryControllerBean deleteApVouEntry");

        try {

            LocalApVoucher apVoucher = apVoucherHome.findByPrimaryKey(VOU_CODE);

            if (apVoucher.getVouApprovalStatus() != null && apVoucher.getVouApprovalStatus().equals("PENDING")) {

                Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode("AP VOUCHER", apVoucher.getVouCode(), companyCode);

                for (Object approvalQueue : adApprovalQueues) {

                    LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) approvalQueue;

                    // adApprovalQueue.entityRemove();
                    em.remove(adApprovalQueue);
                }
            }

            adDeleteAuditTrailHome.create("AP VOUCHER", apVoucher.getVouDate(), apVoucher.getVouDocumentNumber(), apVoucher.getVouReferenceNumber(), apVoucher.getVouAmountDue(), AD_USR, new Date(), companyCode);

            // apVoucher.entityRemove();
            em.remove(apVoucher);

        }
        catch (FinderException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlobalRecordAlreadyDeletedException();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdApprovalNotifiedUsersByVouCode(Integer VOU_CODE, Integer companyCode) {

        Debug.print("ApVoucherEntryControllerBean getAdApprovalNotifiedUsersByVouCode");

        ArrayList list = new ArrayList();

        try {

            LocalApVoucher apVoucher = apVoucherHome.findByPrimaryKey(VOU_CODE);

            if (apVoucher.getVouPosted() == EJBCommon.TRUE) {

                list.add("DOCUMENT POSTED");
                return list;
            }

            Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode("AP VOUCHER", VOU_CODE, companyCode);

            for (Object approvalQueue : adApprovalQueues) {

                LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) approvalQueue;

                list.add(adApprovalQueue.getAdUser().getUsrDescription());
            }

            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }


    public ArrayList getInvLitByCstLitName(String CST_LIT_NAME, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ApVoucherEntryControllerBean getInvLitByCstLitName");

        try {

            LocalInvLineItemTemplate invLineItemTemplate = null;

            try {

                invLineItemTemplate = invLineItemTemplateHome.findByLitName(CST_LIT_NAME, companyCode);

            }
            catch (FinderException ex) {

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

        }
        catch (GlobalNoRecordFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }


    // private methods

    private void addApDrEntry(ApModDistributionRecordDetails mdetails, LocalApVoucher apVoucher, Integer AD_BRNCH, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ApVoucherEntryControllerBean addApDrEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate if coa exists

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(mdetails.getDrCoaAccountNumber(), AD_BRNCH, companyCode);

                if (glChartOfAccount.getCoaEnable() == EJBCommon.FALSE)
                    throw new GlobalBranchAccountNumberInvalidException();

            }
            catch (FinderException ex) {

                throw new GlobalBranchAccountNumberInvalidException();
            }

            // create distribution record

            LocalApDistributionRecord apDistributionRecord = apDistributionRecordHome.create(mdetails.getDrLine(), mdetails.getDrClass(), EJBCommon.roundIt(mdetails.getDrAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()), mdetails.getDrDebit(), EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
            apDistributionRecord.setApVoucher(apVoucher);
            apDistributionRecord.setGlChartOfAccount(glChartOfAccount);

        }
        catch (GlobalBranchAccountNumberInvalidException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void addApDrVliEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE, LocalApVoucher apVoucher, Integer AD_BRNCH, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ApVoucherEntryControllerBean addApDrVliEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, AD_BRNCH, companyCode);

                if (glChartOfAccount.getCoaEnable() == EJBCommon.FALSE)
                    throw new GlobalBranchAccountNumberInvalidException();

            }
            catch (FinderException ex) {

                throw new GlobalBranchAccountNumberInvalidException();
            }

            // create distribution record

            LocalApDistributionRecord apDistributionRecord = apDistributionRecordHome.create(DR_LN, DR_CLSS, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), DR_DBT, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);

            // apVoucher.addApDistributionRecord(apDistributionRecord);
            apDistributionRecord.setApVoucher(apVoucher);
            // glChartOfAccount.addApDistributionRecord(apDistributionRecord);
            apDistributionRecord.setGlChartOfAccount(glChartOfAccount);

        }
        catch (GlobalBranchAccountNumberInvalidException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void executeApVouPost(Integer VOU_CODE, String USR_NM, Integer AD_BRNCH, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ApVoucherEntryControllerBean executeApVouPost");

        LocalApVoucher apVoucher = null;
        LocalApVoucher apDebitedVoucher = null;

        try {

            double VOU_AMNT = 0d;

            // validate if voucher/debit memo is already deleted

            try {

                apVoucher = apVoucherHome.findByPrimaryKey(VOU_CODE);

            }
            catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if voucher/debit memo is already posted or void

            if (apVoucher.getVouPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();

            } else if (apVoucher.getVouVoid() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyVoidException();
            }

            // post voucher/debit memo

            if (apVoucher.getVouVoid() == EJBCommon.FALSE && apVoucher.getVouPosted() == EJBCommon.FALSE) {

                // increase supplier balance

                VOU_AMNT = this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apVoucher.getVouAmountDue(), companyCode);

                this.post(apVoucher.getVouDate(), VOU_AMNT, apVoucher.getApSupplier(), companyCode);

                Collection apVoucherLineItems = apVoucher.getApVoucherLineItems();

                if (apVoucherLineItems != null && !apVoucherLineItems.isEmpty()) {

                    for (Object voucherLineItem : apVoucherLineItems) {

                        LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) voucherLineItem;

                        String II_NM = apVoucherLineItem.getInvItemLocation().getInvItem().getIiName();
                        String LOC_NM = apVoucherLineItem.getInvItemLocation().getInvLocation().getLocName();

                        double QTY_RCVD = this.convertByUomFromAndItemAndQuantity(apVoucherLineItem.getInvUnitOfMeasure(), apVoucherLineItem.getInvItemLocation().getInvItem(), apVoucherLineItem.getVliQuantity(), companyCode);

                        LocalInvCosting invCosting = null;

                        try {

                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(apVoucher.getVouDate(), II_NM, LOC_NM, AD_BRNCH, companyCode);

                        }
                        catch (FinderException ex) {

                        }

                        double COST = apVoucherLineItem.getVliUnitCost();

                        if (invCosting == null) {

                            this.postToInv(apVoucherLineItem, apVoucher.getVouDate(), QTY_RCVD, COST * QTY_RCVD, QTY_RCVD, COST * QTY_RCVD, 0d, null, AD_BRNCH, companyCode);

                        } else {

                            this.postToInv(apVoucherLineItem, apVoucher.getVouDate(), QTY_RCVD, COST * QTY_RCVD, invCosting.getCstRemainingQuantity() + QTY_RCVD, invCosting.getCstRemainingValue() + (COST * QTY_RCVD), 0d, null, AD_BRNCH, companyCode);
                        }

                        /*
                         * // POST TO PROJECTING
                         *
                         * if(apVoucherLineItem.getInvItemLocation().getInvItem().getIiIsVatRelief() ==
                         * EJBCommon.TRUE && apVoucherLineItem.getPmProject() != null ) {
                         *
                         * this.postToPjt(apVoucherLineItem, apVoucher.getVouDate(), COST * QTY_RCVD,
                         * null, AD_BRNCH, companyCode);
                         *
                         * }
                         *
                         */

                    }
                }
            }

            // set voucher post status

            apVoucher.setVouPosted(EJBCommon.TRUE);
            apVoucher.setVouPostedBy(USR_NM);
            apVoucher.setVouDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            // post to gl if necessary

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            if (adPreference.getPrfApGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                // validate if date has no period and period is closed

                LocalGlSetOfBook glJournalSetOfBook = null;

                try {

                    glJournalSetOfBook = glSetOfBookHome.findByDate(apVoucher.getVouDate(), companyCode);

                }
                catch (FinderException ex) {

                    throw new GlJREffectiveDateNoPeriodExistException();
                }

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), apVoucher.getVouDate(), companyCode);
                //

                if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P') {

                    throw new GlJREffectiveDatePeriodClosedException();
                }

                // check if voucher is balance if not check suspense posting

                LocalGlJournalLine glOffsetJournalLine = null;

                Collection apDistributionRecords = apDistributionRecordHome.findImportableDrByVouCode(apVoucher.getVouCode(), companyCode);

                Iterator j = apDistributionRecords.iterator();

                double TOTAL_DEBIT = 0d;
                double TOTAL_CREDIT = 0d;

                while (j.hasNext()) {

                    LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) j.next();

                    double DR_AMNT = apDistributionRecord.getDrAmount();

                    if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

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

                        glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("ACCOUNTS PAYABLES", apVoucher.getVouDebitMemo() == EJBCommon.FALSE ? "VOUCHERS" : "DEBIT MEMOS", companyCode);

                    }
                    catch (FinderException ex) {

                        throw new GlobalJournalNotBalanceException();
                    }

                    if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                        glOffsetJournalLine = glJournalLineHome.create((short) (apDistributionRecords.size() + 1), EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);

                    } else {

                        glOffsetJournalLine = glJournalLineHome.create((short) (apDistributionRecords.size() + 1), EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
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

                    if (adPreference.getPrfEnableApVoucherBatch() == EJBCommon.TRUE) {

                        glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " " + apVoucher.getApVoucherBatch().getVbName(), AD_BRNCH, companyCode);

                    } else {

                        glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " VOUCHERS", AD_BRNCH, companyCode);
                    }

                }
                catch (FinderException ex) {
                }

                if (adPreference.getPrfEnableGlJournalBatch() == EJBCommon.TRUE && glJournalBatch == null) {

                    if (adPreference.getPrfEnableApVoucherBatch() == EJBCommon.TRUE) {

                        glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " " + apVoucher.getApVoucherBatch().getVbName(), "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, AD_BRNCH, companyCode);

                    } else {

                        glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " VOUCHERS", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, AD_BRNCH, companyCode);
                    }
                }

                // create journal entry

                LocalGlJournal glJournal = glJournalHome.create(apVoucher.getVouReferenceNumber(), apVoucher.getVouDescription(), apVoucher.getVouDate(), 0.0d, null, apVoucher.getVouDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM, EJBCommon.getGcCurrentDateWoTime().getTime(), apVoucher.getApSupplier().getSplTin(), apVoucher.getApSupplier().getSplName(), EJBCommon.FALSE, null, AD_BRNCH, companyCode);

                LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("ACCOUNTS PAYABLES", companyCode);
                glJournal.setGlJournalSource(glJournalSource);

                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

                LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName(apVoucher.getVouDebitMemo() == EJBCommon.FALSE ? "VOUCHERS" : "DEBIT MEMOS", companyCode);
                glJournal.setGlJournalCategory(glJournalCategory);

                if (glJournalBatch != null) {

                    glJournal.setGlJournalBatch(glJournalBatch);
                }

                // create journal lines

                j = apDistributionRecords.iterator();

                while (j.hasNext()) {

                    LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) j.next();

                    double DR_AMNT = apDistributionRecord.getDrAmount();

                    LocalGlJournalLine glJournalLine = glJournalLineHome.create(apDistributionRecord.getDrLine(), apDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

                    glJournalLine.setGlChartOfAccount(apDistributionRecord.getGlChartOfAccount());
                    glJournalLine.setGlJournal(glJournal);

                    apDistributionRecord.setDrImported(EJBCommon.TRUE);

                    // for FOREX revaluation

                    LocalApVoucher apVoucherTemp = apVoucher.getVouDebitMemo() == EJBCommon.FALSE ? apVoucher : apDebitedVoucher;

                    if ((!Objects.equals(apVoucherTemp.getGlFunctionalCurrency().getFcCode(), adCompany.getGlFunctionalCurrency().getFcCode())) && glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null && (glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode().equals(apVoucherTemp.getGlFunctionalCurrency().getFcCode()))) {

                        double CONVERSION_RATE = 1;

                        if (apVoucherTemp.getVouConversionRate() != 0 && apVoucherTemp.getVouConversionRate() != 1) {

                            CONVERSION_RATE = apVoucherTemp.getVouConversionRate();

                        } else if (apVoucherTemp.getVouConversionDate() != null) {

                            CONVERSION_RATE = commonData.getFrRateByFrNameAndFrDate(glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), companyCode);
                        }

                        Collection glForexLedgers = null;

                        try {

                            glForexLedgers = glForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(apVoucherTemp.getVouDate(), glJournalLine.getGlChartOfAccount().getCoaCode(), companyCode);

                        }
                        catch (FinderException ex) {

                        }

                        LocalGlForexLedger glForexLedger = (glForexLedgers.isEmpty() || glForexLedgers == null) ? null : (LocalGlForexLedger) glForexLedgers.iterator().next();

                        int FRL_LN = (glForexLedger != null && glForexLedger.getFrlDate().compareTo(apVoucherTemp.getVouDate()) == 0) ? glForexLedger.getFrlLine() + 1 : 1;

                        // compute balance
                        double COA_FRX_BLNC = glForexLedger == null ? 0 : glForexLedger.getFrlBalance();
                        double FRL_AMNT = apDistributionRecord.getDrAmount();

                        if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET"))
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                        else FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);

                        COA_FRX_BLNC = COA_FRX_BLNC + FRL_AMNT;

                        glForexLedger = glForexLedgerHome.create(apVoucherTemp.getVouDate(), FRL_LN, "APV", FRL_AMNT, CONVERSION_RATE, COA_FRX_BLNC, 0d, companyCode);

                        // glJournalLine.getGlChartOfAccount().addGlForexLedger(glForexLedger);
                        glForexLedger.setGlChartOfAccount(glJournalLine.getGlChartOfAccount());
                        // propagate balances
                        try {

                            glForexLedgers = glForexLedgerHome.findByGreaterThanFrlDateAndCoaCode(glForexLedger.getFrlDate(), glForexLedger.getGlChartOfAccount().getCoaCode(), glForexLedger.getFrlAdCompany());

                        }
                        catch (FinderException ex) {

                        }

                        for (Object forexLedger : glForexLedgers) {

                            glForexLedger = (LocalGlForexLedger) forexLedger;
                            FRL_AMNT = apDistributionRecord.getDrAmount();

                            if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET"))
                                FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                            else FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);

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

                            if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) break;
                        }
                    }
                }
            }

        }
        catch (GlJREffectiveDateNoPeriodExistException | AdPRFCoaGlVarianceAccountNotFoundException |
               GlobalTransactionAlreadyVoidException | GlobalTransactionAlreadyPostedException |
               GlobalRecordAlreadyDeletedException | GlobalJournalNotBalanceException |
               GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

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
                        return EJBCommon.roundIt(invFifoCosting.getCstItemCost() / invFifoCosting.getCstQuantityReceived(), commonData.getInvGpCostPrecisionUnit(companyCode));
                    } else if (invFifoCosting.getArInvoiceLineItem() != null) {
                        return EJBCommon.roundIt(invFifoCosting.getCstCostOfSales() / invFifoCosting.getCstQuantitySold(), commonData.getInvGpCostPrecisionUnit(companyCode));
                    } else {
                        return EJBCommon.roundIt(invFifoCosting.getCstAdjustCost() / invFifoCosting.getCstAdjustQuantity(), commonData.getInvGpCostPrecisionUnit(companyCode));
                    }
                }
            } else {

                // most applicable in 1st entries of data
                LocalInvItemLocation invItemLocation = invItemLocationHome.findByPrimaryKey(IL_CODE);
                return invItemLocation.getInvItem().getIiUnitCost();
            }

        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void post(Date VOU_DT, double VOU_AMNT, LocalApSupplier apSupplier, Integer companyCode) {

        Debug.print("ApVoucherEntryControllerBean post");

        try {

            // find supplier balance before or equal voucher date

            Collection apSupplierBalances = apSupplierBalanceHome.findByBeforeOrEqualVouDateAndSplSupplierCode(VOU_DT, apSupplier.getSplSupplierCode(), companyCode);

            if (!apSupplierBalances.isEmpty()) {

                // get last voucher

                ArrayList apSupplierBalanceList = new ArrayList(apSupplierBalances);

                LocalApSupplierBalance apSupplierBalance = (LocalApSupplierBalance) apSupplierBalanceList.get(apSupplierBalanceList.size() - 1);

                if (apSupplierBalance.getSbDate().before(VOU_DT)) {

                    // create new balance

                    LocalApSupplierBalance apNewSupplierBalance = apSupplierBalanceHome.create(VOU_DT, apSupplierBalance.getSbBalance() + VOU_AMNT, companyCode);

                    // apSupplier.addApSupplierBalance(apNewSupplierBalance);
                    apNewSupplierBalance.setApSupplier(apSupplier);

                } else { // equals to voucher date

                    apSupplierBalance.setSbBalance(apSupplierBalance.getSbBalance() + VOU_AMNT);
                }

            } else {

                // create new balance

                LocalApSupplierBalance apNewSupplierBalance = apSupplierBalanceHome.create(VOU_DT, VOU_AMNT, companyCode);

                // apSupplier.addApSupplierBalance(apNewSupplierBalance);
                apNewSupplierBalance.setApSupplier(apSupplier);
            }

            // propagate to subsequent balances if necessary

            apSupplierBalances = apSupplierBalanceHome.findByAfterVouDateAndSplSupplierCode(VOU_DT, apSupplier.getSplSupplierCode(), companyCode);

            for (Object supplierBalance : apSupplierBalances) {

                LocalApSupplierBalance apSupplierBalance = (LocalApSupplierBalance) supplierBalance;

                apSupplierBalance.setSbBalance(apSupplierBalance.getSbBalance() + VOU_AMNT);
            }

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer companyCode) {

        Debug.print("ApVoucherEntryControllerBean convertForeignToFunctionalCurrency");

        LocalAdCompany adCompany = null;

        // get company and extended precision

        try {

            adCompany = adCompanyHome.findByPrimaryKey(companyCode);

        }
        catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // Convert to functional currency if necessary

        if (CONVERSION_RATE != 1 && CONVERSION_RATE != 0) {

            AMOUNT = AMOUNT / CONVERSION_RATE;
        }
        return EJBCommon.roundIt(AMOUNT, commonData.getInvGpCostPrecisionUnit(companyCode));
    }

    private LocalApVoucherLineItem addApVliEntry(ApModVoucherLineItemDetails mdetails, LocalApVoucher apVoucher, LocalInvItemLocation invItemLocation, Integer companyCode) throws GlobalMiscInfoIsRequiredException {

        Debug.print("ApVoucherEntryControllerBean addApVliEntry");

        try {

            short precisionUnit = commonData.getGlFcPrecisionUnit(companyCode);

            double VLI_AMNT = 0d;
            double VLI_TAX_AMNT = 0d;

            if (mdetails.getVliTax() == EJBCommon.TRUE) {

                LocalApTaxCode apTaxCode = apVoucher.getApTaxCode();

                // calculate net amount
                VLI_AMNT = this.calculateIlNetAmount(mdetails, apTaxCode.getTcRate(), apTaxCode.getTcType(), precisionUnit);

                // calculate tax
                VLI_TAX_AMNT = this.calculateIlTaxAmount(mdetails, apTaxCode.getTcRate(), apTaxCode.getTcType(), VLI_AMNT, precisionUnit);

            } else {

                VLI_AMNT = mdetails.getVliAmount();
            }

            // calculate net amount

            LocalApVoucherLineItem apVoucherLineItem = apVoucherLineItemHome.create(mdetails.getVliLine(), mdetails.getVliQuantity(), mdetails.getVliUnitCost(), VLI_AMNT, VLI_TAX_AMNT, mdetails.getVliDiscount1(), mdetails.getVliDiscount2(), mdetails.getVliDiscount3(), mdetails.getVliDiscount4(), mdetails.getVliTotalDiscount(), null, null, null, mdetails.getVliTax(), companyCode);
            apVoucherLineItem.setApVoucher(apVoucher);
            apVoucherLineItem.setInvItemLocation(invItemLocation);

            LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(mdetails.getVliUomName(), companyCode);
            apVoucherLineItem.setInvUnitOfMeasure(invUnitOfMeasure);

            if (mdetails.getIsTraceMic()) {

                this.createInvTagList(apVoucherLineItem, mdetails.getTagList(), companyCode);
            }

            if (mdetails.getVliIsVatRelief()) {

                apVoucherLineItem.setVliSplName(mdetails.getVliSplName());
                apVoucherLineItem.setVliSplTin(mdetails.getVliSplTin());
                apVoucherLineItem.setVliSplAddress(mdetails.getVliSplAddress());
            }


            return apVoucherLineItem;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
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
                Debug.print("KABOOM");
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

    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double JL_AMNT, Integer companyCode) {

        Debug.print("ApVoucherEntryControllerBean postToGl");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccountBalance glChartOfAccountBalance = glChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(glAccountingCalendarValue.getAcvCode(), glChartOfAccount.getCoaCode(), companyCode);

            String ACCOUNT_TYPE = glChartOfAccount.getCoaAccountType();
            short FC_EXTNDD_PRCSN = adCompany.getGlFunctionalCurrency().getFcExtendedPrecision();

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

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToGlInvestorAccountBalance(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalApSupplier apSupplier, boolean isCurrentAcv, byte isDebit, double AMNT, Integer companyCode) {

        Debug.print("ApVoucherEntryControllerBean postToGlInvestorAccountBalance");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlInvestorAccountBalance glInvestorAccountBalance = glInvestorAccountBalanceHome.findByAcvCodeAndSplCode(glAccountingCalendarValue.getAcvCode(), apSupplier.getSplCode(), companyCode);

            short FC_EXTNDD_PRCSN = adCompany.getGlFunctionalCurrency().getFcExtendedPrecision();

            Debug.print("AMNT=" + AMNT);
            Debug.print("glAccountingCalendarValue.getAcvPeriodPrefix()" + glAccountingCalendarValue.getAcvPeriodPrefix());

            if (isDebit == EJBCommon.TRUE) {

                glInvestorAccountBalance.setIrabEndingBalance(EJBCommon.roundIt(glInvestorAccountBalance.getIrabEndingBalance() + AMNT, FC_EXTNDD_PRCSN));

                if (!isCurrentAcv) {

                    glInvestorAccountBalance.setIrabBeginningBalance(EJBCommon.roundIt(glInvestorAccountBalance.getIrabBeginningBalance() + AMNT, FC_EXTNDD_PRCSN));
                }

            } else {

                glInvestorAccountBalance.setIrabEndingBalance(EJBCommon.roundIt(glInvestorAccountBalance.getIrabEndingBalance() - AMNT, FC_EXTNDD_PRCSN));

                if (!isCurrentAcv) {

                    glInvestorAccountBalance.setIrabBeginningBalance(EJBCommon.roundIt(glInvestorAccountBalance.getIrabBeginningBalance() - AMNT, FC_EXTNDD_PRCSN));
                }
            }

            if (isCurrentAcv) {

                if (isDebit == EJBCommon.TRUE) {

                    glInvestorAccountBalance.setIrabTotalDebit(EJBCommon.roundIt(glInvestorAccountBalance.getIrabTotalDebit() + AMNT, FC_EXTNDD_PRCSN));

                } else {

                    glInvestorAccountBalance.setIrabTotalCredit(EJBCommon.roundIt(glInvestorAccountBalance.getIrabTotalCredit() + AMNT, FC_EXTNDD_PRCSN));
                }
            }

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToInv(LocalApVoucherLineItem apVoucherLineItem, Date CST_DT, double CST_QTY_RCVD, double CST_ITM_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, double CST_VRNC_VL, String USR_NM, Integer AD_BRNCH, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ApVoucherEntryControllerBean postToInv");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalInvItemLocation invItemLocation = apVoucherLineItem.getInvItemLocation();
            int CST_LN_NMBR = 0;

            CST_QTY_RCVD = EJBCommon.roundIt(CST_QTY_RCVD, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_ITM_CST = EJBCommon.roundIt(CST_ITM_CST, adPreference.getPrfInvCostPrecisionUnit());
            CST_RMNNG_QTY = EJBCommon.roundIt(CST_RMNNG_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_RMNNG_VL = EJBCommon.roundIt(CST_RMNNG_VL, adPreference.getPrfInvCostPrecisionUnit());

            try {

                // generate line number

                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            }
            catch (FinderException ex) {

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

                prevExpiryDates = prevCst.getCstExpiryDate();
                qtyPrpgt = prevCst.getCstRemainingQuantity();

                if (prevExpiryDates == null) {
                    prevExpiryDates = "";
                }

            }
            catch (Exception ex) {

            }

            // create costing
            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, CST_QTY_RCVD,
                    CST_ITM_CST, 0d, 0d, 0d, 0d,
                    CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, 0d, AD_BRNCH, companyCode);
            // invItemLocation.addInvCosting(invCosting);
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setApVoucherLineItem(apVoucherLineItem);

            invCosting.setCstQuantity(CST_QTY_RCVD);
            invCosting.setCstCost(CST_ITM_CST);
            // Get Latest Expiry Dates

            if (prevExpiryDates != null && prevExpiryDates != "" && prevExpiryDates.length() != 0) {
                Debug.print("apPurchaseOrderLine.getPlMisc(): " + apVoucherLineItem.getVliMisc());
                if (apVoucherLineItem.getVliMisc() != null && apVoucherLineItem.getVliMisc() != "" && apVoucherLineItem.getVliMisc().length() != 0) {
                    int qty2Prpgt = Integer.parseInt(this.getQuantityExpiryDates(apVoucherLineItem.getVliMisc()));
                    String miscList2Prpgt = this.propagateExpiryDates(apVoucherLineItem.getVliMisc(), qty2Prpgt);
                    prevExpiryDates = prevExpiryDates.substring(1);
                    String propagateMiscPrpgt = miscList2Prpgt + prevExpiryDates;

                    invCosting.setCstExpiryDate(propagateMiscPrpgt);
                } else {
                    invCosting.setCstExpiryDate(prevExpiryDates);
                }
            } else {
                if (apVoucherLineItem.getVliMisc() != null && apVoucherLineItem.getVliMisc() != "" && apVoucherLineItem.getVliMisc().length() != 0) {
                    int initialQty = Integer.parseInt(this.getQuantityExpiryDates(apVoucherLineItem.getVliMisc()));
                    String initialPrpgt = this.propagateExpiryDates(apVoucherLineItem.getVliMisc(), initialQty);

                    invCosting.setCstExpiryDate(initialPrpgt);
                } else {
                    invCosting.setCstExpiryDate(prevExpiryDates);
                }
            }

            // if cost variance is not 0, generate cost variance for the transaction
            if (CST_VRNC_VL != 0) {

                this.generateCostVariance(invCosting.getInvItemLocation(), CST_VRNC_VL, "APVOU" + apVoucherLineItem.getApVoucher().getVouDocumentNumber(), apVoucherLineItem.getApVoucher().getVouDescription(), apVoucherLineItem.getApVoucher().getVouDate(), USR_NM, AD_BRNCH, companyCode);
            }

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);

            i = invCostings.iterator();

            String miscList = "";
            if (apVoucherLineItem.getVliMisc() != null && apVoucherLineItem.getVliMisc() != "" && apVoucherLineItem.getVliMisc().length() != 0) {
                double qty = Double.parseDouble(this.getQuantityExpiryDates(apVoucherLineItem.getVliMisc()));
                miscList = this.propagateExpiryDates(apVoucherLineItem.getVliMisc(), qty);
            }

            Debug.print("miscList Propagate:" + miscList);
            while (i.hasNext()) {

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) i.next();

                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_QTY_RCVD);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_ITM_CST);
                if (apVoucherLineItem.getVliMisc() != null && apVoucherLineItem.getVliMisc() != "" && apVoucherLineItem.getVliMisc().length() != 0) {
                    miscList = miscList.substring(1);
                    // String miscList2 =
                    // propagateMisc;//this.propagateExpiryDates(invCosting.getCstExpiryDate(),
                    // qty);
                    Debug.print("invPropagatedCosting.getCstExpiryDate() : " + invPropagatedCosting.getCstExpiryDate());
                    String propagateMisc = invPropagatedCosting.getCstExpiryDate() + miscList;

                    invPropagatedCosting.setCstExpiryDate(propagateMisc);
                } else {
                    invPropagatedCosting.setCstExpiryDate(prevExpiryDates);
                }
            }

            // regenerate cost variance
            this.regenerateCostVariance(invCostings, invCosting, AD_BRNCH, companyCode);

        }
        catch (AdPRFCoaGlVarianceAccountNotFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

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
        Debug.print("Y " + y);

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

        Debug.print("qty" + qty);
        StringBuilder miscList = new StringBuilder();

        for (int x = 0; x < qty; x++) {

            // Date
            start = nextIndex + 1;
            nextIndex = misc.indexOf(separator, start);
            length = nextIndex - start;

            try {

                miscList.append("$").append(misc, start, start + length);
            }
            catch (Exception ex) {

                throw ex;
            }
        }

        miscList.append("$");
        Debug.print("miscList :" + miscList);
        return (miscList.toString());
    }

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, double QTY_RCVD, Integer companyCode) {

        Debug.print("ApVoucherEntryControllerBean convertByUomFromAndItemAndQuantity");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), companyCode);

            return EJBCommon.roundIt(QTY_RCVD * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), adPreference.getPrfInvQuantityPrecisionUnit());

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void voidInvAdjustment(LocalInvAdjustment invAdjustment, Integer AD_BRNCH, Integer companyCode) {

        Debug.print("ApVoucherEntryController voidInvAdjustment");

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

            Collection invAjustmentLines = invAdjustment.getInvAdjustmentLines();
            i = invAjustmentLines.iterator();
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

        }
        catch (Exception ex) {

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

        Debug.print("ApVoucherEntryController addInvDrEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate coa

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, AD_BRNCH, companyCode);

            }
            catch (FinderException ex) {

                throw new GlobalBranchAccountNumberInvalidException();
            }

            // create distribution record

            LocalInvDistributionRecord invDistributionRecord = invDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), DR_RVRSL, EJBCommon.FALSE, companyCode);

            // invAdjustment.addInvDistributionRecord(invDistributionRecord);
            invDistributionRecord.setInvAdjustment(invAdjustment);
            // glChartOfAccount.addInvDistributionRecord(invDistributionRecord);
            invDistributionRecord.setInvChartOfAccount(glChartOfAccount);

        }
        catch (GlobalBranchAccountNumberInvalidException ex) {

            throw new GlobalBranchAccountNumberInvalidException();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void executeInvAdjPost(Integer ADJ_CODE, String USR_NM, Integer AD_BRNCH, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException {

        Debug.print("ApVoucherEntryController executeInvAdjPost");

        try {

            // validate if adjustment is already deleted

            LocalInvAdjustment invAdjustment = null;

            try {

                invAdjustment = invAdjustmentHome.findByPrimaryKey(ADJ_CODE);

            }
            catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if adjustment is already posted or void

            if (invAdjustment.getAdjPosted() == EJBCommon.TRUE) {

                if (invAdjustment.getAdjVoid() != EJBCommon.TRUE) throw new GlobalTransactionAlreadyPostedException();
            }

            Collection invAdjustmentLines = null;

            if (invAdjustment.getAdjVoid() == EJBCommon.FALSE)
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.FALSE, invAdjustment.getAdjCode(), companyCode);
            else
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.TRUE, invAdjustment.getAdjCode(), companyCode);

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

            }
            catch (FinderException ex) {

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

                }
                catch (FinderException ex) {

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

            }
            catch (FinderException ex) {

            }

            if (glJournalBatch == null) {

                glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " INVENTORY ADJUSTMENTS", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, AD_BRNCH, companyCode);
            }

            // create journal entry

            LocalGlJournal glJournal = glJournalHome.create(invAdjustment.getAdjReferenceNumber(), invAdjustment.getAdjDescription(), invAdjustment.getAdjDate(), 0.0d, null, invAdjustment.getAdjDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM, EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, EJBCommon.FALSE, null, AD_BRNCH, companyCode);

            LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("INVENTORY", companyCode);
            // glJournalSource.addGlJournal(glJournal);
            glJournal.setGlJournalSource(glJournalSource);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
            // glFunctionalCurrency.addGlJournal(glJournal);
            glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("INVENTORY ADJUSTMENTS", companyCode);
            // glJournalCategory.addGlJournal(glJournal);
            glJournal.setGlJournalCategory(glJournalCategory);

            if (glJournalBatch != null) {

                // glJournalBatch.addGlJournal(glJournal);
                glJournal.setGlJournalBatch(glJournalBatch);
            }

            // create journal lines

            j = invDistributionRecords.iterator();

            while (j.hasNext()) {

                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) j.next();

                double DR_AMNT = 0d;

                DR_AMNT = invDistributionRecord.getDrAmount();

                LocalGlJournalLine glJournalLine = glJournalLineHome.create(invDistributionRecord.getDrLine(), invDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

                // invDistributionRecord.getInvChartOfAccount().addGlJournalLine(glJournalLine);
                glJournalLine.setGlChartOfAccount(invDistributionRecord.getInvChartOfAccount());

                // glJournal.addGlJournalLine(glJournalLine);
                glJournalLine.setGlJournal(glJournal);

                invDistributionRecord.setDrImported(EJBCommon.TRUE);
            }

            if (glOffsetJournalLine != null) {

                // glJournal.addGlJournalLine(glOffsetJournalLine);
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

                        if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) break;
                    }
                }
            }

            invAdjustment.setAdjPosted(EJBCommon.TRUE);

        }
        catch (GlJREffectiveDateNoPeriodExistException | GlobalTransactionAlreadyPostedException |
               GlobalRecordAlreadyDeletedException | GlobalJournalNotBalanceException |
               GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

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

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalInvAdjustment saveInvAdjustment(String ADJ_RFRNC_NMBR, String ADJ_DSCRPTN, Date ADJ_DATE, String USR_NM, Integer AD_BRNCH, Integer companyCode) {

        Debug.print("ApVoucherEntryController saveInvAdjustment");

        try {

            // generate adj document number
            String ADJ_DCMNT_NMBR = null;

            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

            try {

                adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("INV ADJUSTMENT", companyCode);

            }
            catch (FinderException ex) {

            }

            try {

                adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, companyCode);

            }
            catch (FinderException ex) {

            }

            while (true) {

                if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                    try {

                        invAdjustmentHome.findByAdjDocumentNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, companyCode);
                        adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                    }
                    catch (FinderException ex) {

                        ADJ_DCMNT_NMBR = adDocumentSequenceAssignment.getDsaNextSequence();
                        adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                        break;
                    }

                } else {

                    try {

                        invAdjustmentHome.findByAdjDocumentNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, companyCode);
                        adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                    }
                    catch (FinderException ex) {

                        ADJ_DCMNT_NMBR = adBranchDocumentSequenceAssignment.getBdsNextSequence();
                        adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                        break;
                    }
                }
            }

            return invAdjustmentHome.create(ADJ_DCMNT_NMBR, ADJ_RFRNC_NMBR, ADJ_DSCRPTN, ADJ_DATE, "COST-VARIANCE", "N/A", EJBCommon.FALSE, USR_NM, ADJ_DATE, USR_NM, ADJ_DATE, null, null, USR_NM, ADJ_DATE, null, null, EJBCommon.TRUE, EJBCommon.FALSE, AD_BRNCH, companyCode);

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postInvAdjustmentToInventory(LocalInvAdjustmentLine invAdjustmentLine, Date CST_DT, double CST_ADJST_QTY, double CST_ADJST_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, Integer AD_BRNCH, Integer companyCode) {

        Debug.print("ApVoucherEntryController postInvAdjustmentToInventory");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalInvItemLocation invItemLocation = invAdjustmentLine.getInvItemLocation();
            int CST_LN_NMBR = 0;

            CST_ADJST_QTY = EJBCommon.roundIt(CST_ADJST_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_ADJST_CST = EJBCommon.roundIt(CST_ADJST_CST, adPreference.getPrfInvCostPrecisionUnit());
            CST_RMNNG_QTY = EJBCommon.roundIt(CST_RMNNG_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_RMNNG_VL = EJBCommon.roundIt(CST_RMNNG_VL, adPreference.getPrfInvCostPrecisionUnit());

            if (CST_ADJST_QTY < 0) {

                invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - Math.abs(CST_ADJST_QTY));
            }

            // create costing

            try {

                // generate line number

                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            }
            catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR,
                    0d, 0d, CST_ADJST_QTY, CST_ADJST_CST, 0d,
                    0d, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d,
                    CST_ADJST_QTY > 0 ? CST_ADJST_QTY : 0, AD_BRNCH, companyCode);
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

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double calculateIlNetAmount(ApModVoucherLineItemDetails mdetails, double tcRate, String tcType, short precisionUnit) {

        double amount = 0d;

        if (tcType.equals("INCLUSIVE")) {

            amount = EJBCommon.roundIt(mdetails.getVliAmount() / (1 + (tcRate / 100)), precisionUnit);

        } else {

            // tax exclusive, none, zero rated or exempt

            amount = mdetails.getVliAmount();
        }

        return amount;
    }

    private double calculateIlTaxAmount(ApModVoucherLineItemDetails mdetails, double tcRate, String tcType, double amount, short precisionUnit) {

        double taxAmount = 0d;

        if (!tcType.equals("NONE") && !tcType.equals("EXEMPT")) {

            if (tcType.equals("INCLUSIVE")) {

                taxAmount = EJBCommon.roundIt(mdetails.getVliAmount() - amount, precisionUnit);

            } else if (tcType.equals("EXCLUSIVE")) {

                taxAmount = EJBCommon.roundIt(mdetails.getVliAmount() * tcRate / 100, precisionUnit);

            } else {

                // tax none zero-rated or exempt

            }
        }

        return taxAmount;
    }

    private void createInvTagList(LocalApVoucherLineItem apVoucherLineItem, ArrayList list, Integer companyCode) throws Exception {

        Debug.print("ApVoucherEntryController createInvTagList");

        try {
            Debug.print("aabot?");
            // Iterator t = apVoucherLineItem.getInvTag().iterator();
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

                    invTag.setApVoucherLineItem(apVoucherLineItem);
                    invTag.setInvItemLocation(apVoucherLineItem.getInvItemLocation());
                    LocalAdUser adUser = null;
                    try {
                        adUser = adUserHome.findByUsrName(tgLstDetails.getTgCustodian(), companyCode);
                    }
                    catch (FinderException ex) {

                    }
                    invTag.setAdUser(adUser);
                    Debug.print("ngcreate ba?");
                }
            }

        }
        catch (Exception ex) {
            throw ex;
        }
    }

    private ArrayList getInvTagList(LocalApVoucherLineItem apVoucherLineItem) {

        ArrayList list = new ArrayList();

        Collection invTags = apVoucherLineItem.getInvTags();
        for (Object tag : invTags) {
            LocalInvTag invTag = (LocalInvTag) tag;
            InvModTagListDetails tgLstDetails = new InvModTagListDetails();
            tgLstDetails.setTgPropertyCode(invTag.getTgPropertyCode());
            tgLstDetails.setTgSpecs(invTag.getTgSpecs());
            tgLstDetails.setTgExpiryDate(invTag.getTgExpiryDate());
            tgLstDetails.setTgSerialNumber(invTag.getTgSerialNumber());
            try {

                tgLstDetails.setTgCustodian(invTag.getAdUser().getUsrName());
            }
            catch (Exception ex) {
                tgLstDetails.setTgCustodian("");
            }

            list.add(tgLstDetails);

            Debug.print(tgLstDetails.getTgPropertyCode() + "<== property code inside controllerbean ");
            Debug.print(tgLstDetails.getTgSpecs() + "<== specs inside controllerbean ");
            Debug.print(list + "<== taglist inside controllerbean ");
        }

        return list;
    }

    private ArrayList getInvTagList(LocalApPurchaseOrderLine apPurchaseOrderLine) {

        ArrayList list = new ArrayList();

        Collection invTags = apPurchaseOrderLine.getInvTags();
        for (Object tag : invTags) {
            LocalInvTag invTag = (LocalInvTag) tag;
            InvModTagListDetails tgLstDetails = new InvModTagListDetails();
            tgLstDetails.setTgPropertyCode(invTag.getTgPropertyCode());
            tgLstDetails.setTgSpecs(invTag.getTgSpecs());
            tgLstDetails.setTgExpiryDate(invTag.getTgExpiryDate());
            tgLstDetails.setTgSerialNumber(invTag.getTgSerialNumber());
            try {

                tgLstDetails.setTgCustodian(invTag.getAdUser().getUsrName());
            }
            catch (Exception ex) {
                tgLstDetails.setTgCustodian("");
            }

            list.add(tgLstDetails);

            Debug.print(tgLstDetails.getTgPropertyCode() + "<== property code inside controllerbean ");
            Debug.print(tgLstDetails.getTgSpecs() + "<== specs inside controllerbean ");
            Debug.print(list + "<== taglist inside controllerbean ");
        }

        return list;
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApVoucherEntryControllerBean ejbCreate");
    }

}