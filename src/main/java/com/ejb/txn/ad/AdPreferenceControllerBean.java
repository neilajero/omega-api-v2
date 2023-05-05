package com.ejb.txn.ad;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.*;
import com.ejb.entities.ar.*;
import com.ejb.dao.ad.*;
import com.ejb.dao.ar.*;
import com.ejb.dao.gl.*;
import com.ejb.dao.inv.*;
import com.ejb.entities.gl.*;
import com.ejb.entities.inv.*;
import com.ejb.exception.ad.*;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.*;
import com.util.ad.AdApplicationDetails;
import com.util.mod.ad.AdModPreferenceDetails;

@Stateless(name = "AdPreferenceControllerEJB")
public class AdPreferenceControllerBean extends EJBContextClass implements AdPreferenceController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalArWithholdingTaxCodeHome arWithholdingTaxCodeHome;
    @EJB
    private LocalArTaxCodeHome arTaxCodeHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalAdApplicationHome adApplicationHome;

    public byte getAdPrfAdDisableMultipleLogin(String companyShortName) {

        Debug.print("AdPreferenceControllerBean getAdPrfAdDisableMultipleLogin");

        try {
            Debug.print("companyShortName=" + companyShortName);
            LocalAdCompany adCompany = adCompanyHome.findByCmpShortName(companyShortName);
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(adCompany.getCmpCode());
            return adPreference.getPrfAdDisableMultipleLogin();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer companyCode) {

        Debug.print("AdPreferenceControllerBean getGlFcPrecisionUnit");

        try {
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            return adCompany.getGlFunctionalCurrency().getFcPrecision();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public AdModPreferenceDetails getAdPrf(Integer companyCode) {

        Debug.print("AdPreferenceControllerBean getAdPrf");

        LocalAdPreference adPreference;

        LocalGlChartOfAccount glAccruedVatAccount = null;
        LocalGlChartOfAccount glPettyCashAccount = null;
        LocalGlChartOfAccount glPosAdjustmentAccount = null;
        LocalGlChartOfAccount glGeneralAdjustmentAccount = null;
        LocalGlChartOfAccount glIssuanceAdjustmentAccount = null;
        LocalGlChartOfAccount glProductionAdjustmentAccount = null;
        LocalGlChartOfAccount glWastageAdjustmentAccount = null;
        LocalGlChartOfAccount glPosDiscountAccount = null;
        LocalGlChartOfAccount glPosGiftCertificateAccount = null;
        LocalGlChartOfAccount glPosServiceChargeAccount = null;
        LocalGlChartOfAccount glPosDineInChargeAccount = null;
        LocalGlChartOfAccount glCoaCustomerDepositAccount = null;

        try {
            adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            AdModPreferenceDetails mdetails = new AdModPreferenceDetails();
            mdetails.setPrfAllowSuspensePosting(adPreference.getPrfAllowSuspensePosting());
            mdetails.setPrfGlJournalLineNumber(adPreference.getPrfGlJournalLineNumber());
            mdetails.setPrfApJournalLineNumber(adPreference.getPrfApJournalLineNumber());
            mdetails.setPrfArInvoiceLineNumber(adPreference.getPrfArInvoiceLineNumber());
            mdetails.setPrfApWTaxRealization(adPreference.getPrfApWTaxRealization());
            mdetails.setPrfArWTaxRealization(adPreference.getPrfArWTaxRealization());
            mdetails.setPrfEnableGlJournalBatch(adPreference.getPrfEnableGlJournalBatch());
            mdetails.setPrfEnableGlRecomputeCoaBalance(adPreference.getPrfEnableGlRecomputeCoaBalance());
            mdetails.setPrfEnableApVoucherBatch(adPreference.getPrfEnableApVoucherBatch());
            mdetails.setPrfEnableApPOBatch(adPreference.getPrfEnableApPOBatch());
            mdetails.setPrfEnableApCheckBatch(adPreference.getPrfEnableApCheckBatch());
            mdetails.setPrfEnableArInvoiceBatch(adPreference.getPrfEnableArInvoiceBatch());
            mdetails.setPrfEnableArInvoiceInterestGeneration(adPreference.getPrfEnableArInvoiceInterestGeneration());
            mdetails.setPrfEnableArReceiptBatch(adPreference.getPrfEnableArReceiptBatch());
            mdetails.setPrfEnableArMiscReceiptBatch(adPreference.getPrfEnableArMiscReceiptBatch());
            mdetails.setPrfApGlPostingType(adPreference.getPrfApGlPostingType());
            mdetails.setPrfArGlPostingType(adPreference.getPrfArGlPostingType());
            mdetails.setPrfCmGlPostingType(adPreference.getPrfCmGlPostingType());
            mdetails.setPrfCmUseBankForm(adPreference.getPrfCmUseBankForm());
            mdetails.setPrfInvInventoryLineNumber(adPreference.getPrfInvInventoryLineNumber());
            mdetails.setPrfInvQuantityPrecisionUnit(adPreference.getPrfInvQuantityPrecisionUnit());
            mdetails.setPrfInvCostPrecisionUnit(adPreference.getPrfInvCostPrecisionUnit());
            mdetails.setPrfInvGlPostingType(adPreference.getPrfInvGlPostingType());
            mdetails.setPrfGlPostingType(adPreference.getPrfGlPostingType());
            mdetails.setPrfApFindCheckDefaultType(adPreference.getPrfApFindCheckDefaultType());
            mdetails.setPrfArFindReceiptDefaultType(adPreference.getPrfArFindReceiptDefaultType());
            mdetails.setPrfInvEnableShift(adPreference.getPrfInvEnableShift());
            mdetails.setPrfEnableInvBUABatch(adPreference.getPrfEnableInvBUABatch());
            mdetails.setPrfApUseAccruedVat(adPreference.getPrfApUseAccruedVat());
            mdetails.setPrfApDefaultCheckDate(adPreference.getPrfApDefaultCheckDate());
            mdetails.setPrfApDefaultChecker(adPreference.getPrfApDefaultChecker());
            mdetails.setPrfApDefaultApprover(adPreference.getPrfApDefaultApprover());


            mdetails.setPrfWtcName(adPreference.getArWithholdingTaxCode() != null ? adPreference.getArWithholdingTaxCode().getWtcName() : null);

            if (adPreference.getPrfApGlCoaAccruedVatAccount() != null && !adPreference.getPrfApGlCoaAccruedVatAccount().equals(0)) {
                glAccruedVatAccount = glChartOfAccountHome.findByPrimaryKey(adPreference.getPrfApGlCoaAccruedVatAccount());
                mdetails.setPrfApGlCoaAccruedVatAccountNumber(glAccruedVatAccount.getCoaAccountNumber());
                mdetails.setPrfApGlCoaAccruedVatAccountDescription(glAccruedVatAccount.getCoaAccountDescription());
            }

            if (adPreference.getPrfApGlCoaPettyCashAccount() != null && !adPreference.getPrfApGlCoaPettyCashAccount().equals(0)) {
                glPettyCashAccount = glChartOfAccountHome.findByPrimaryKey(adPreference.getPrfApGlCoaPettyCashAccount());
                mdetails.setPrfApGlCoaPettyCashAccountNumber(glPettyCashAccount.getCoaAccountNumber());
                mdetails.setPrfApGlCoaPettyCashAccountDescription(glPettyCashAccount.getCoaAccountDescription());
            }

            mdetails.setPrfApUseSupplierPulldown(adPreference.getPrfApUseSupplierPulldown());
            mdetails.setPrfArUseCustomerPulldown(adPreference.getPrfArUseCustomerPulldown());
            mdetails.setPrfApAutoGenerateSupplierCode(adPreference.getPrfApAutoGenerateSupplierCode());
            mdetails.setPrfApNextSupplierCode(adPreference.getPrfApNextSupplierCode());
            mdetails.setPrfApReferenceNumberValidation(adPreference.getPrfApReferenceNumberValidation());
            mdetails.setPrfInvEnablePosIntegration(adPreference.getPrfInvEnablePosIntegration());
            mdetails.setPrfInvEnablePosAutoPostUpload(adPreference.getPrfInvEnablePosAutoPostUpload());
            mdetails.setPrfApCheckVoucherDataSource(adPreference.getPrfApCheckVoucherDataSource());
            mdetails.setPrfApDefaultPrTax(adPreference.getPrfApDefaultPrTax());
            mdetails.setPrfApDefaultPrCurrency(adPreference.getPrfApDefaultPrCurrency());
            mdetails.setPrfArSalesInvoiceDataSource(adPreference.getPrfArSalesInvoiceDataSource());
            mdetails.setPrfArAutoComputeCogs(adPreference.getPrfArAutoComputeCogs());
            mdetails.setPrfArMonthlyInterestRate(adPreference.getPrfArMonthlyInterestRate());
            mdetails.setPrfApAgingBucket(adPreference.getPrfApAgingBucket());
            mdetails.setPrfArAgingBucket(adPreference.getPrfArAgingBucket());
            mdetails.setPrfArAllowPriorDate(adPreference.getPrfArAllowPriorDate());
            mdetails.setPrfApShowPrCost(adPreference.getPrfApShowPrCost());
            mdetails.setPrfApDebitMemoOverrideCost(adPreference.getPrfApDebitMemoOverrideCost());
            mdetails.setPrfGlYearEndCloseRestriction(adPreference.getPrfGlYearEndCloseRestriction());
            mdetails.setPrfArEnablePaymentTerm(adPreference.getPrfArEnablePaymentTerm());
            mdetails.setPrfArDisableSalesPrice(adPreference.getPrfArDisableSalesPrice());
            mdetails.setPrfInvItemLocationShowAll(adPreference.getPrfInvItemLocationShowAll());
            mdetails.setPrfInvItemLocationAddByItemList(adPreference.getPrfInvItemLocationAddByItemList());
            mdetails.setPrfArValidateCustomerEmail(adPreference.getPrfArValidateCustomerEmail());
            mdetails.setPrfArCheckInsufficientStock(adPreference.getPrfArCheckInsufficientStock());
            mdetails.setPrfArDetailedReceivable(adPreference.getPrfArDetailedReceivable());
            mdetails.setPrfAdDisableMultipleLogin(adPreference.getPrfAdDisableMultipleLogin());
            mdetails.setPrfAdEnableEmailNotification(adPreference.getPrfAdEnableEmailNotification());
            mdetails.setPrfArSoSalespersonRequired(adPreference.getPrfArSoSalespersonRequired());
            mdetails.setPrfArInvcSalespersonRequired(adPreference.getPrfArInvcSalespersonRequired());
            mdetails.setPrfMailHost(adPreference.getPrfMailHost());
            mdetails.setPrfMailSocketFactoryPort(adPreference.getPrfMailSocketFactoryPort());
            mdetails.setPrfMailPort(adPreference.getPrfMailPort());
            mdetails.setPrfMailFrom(adPreference.getPrfMailFrom());
            mdetails.setPrfMailAuthenticator(adPreference.getPrfMailAuthenticator());
            mdetails.setPrfMailPassword(adPreference.getPrfMailPassword());
            mdetails.setPrfMailTo(adPreference.getPrfMailTo());
            mdetails.setPrfMailCc(adPreference.getPrfMailCc());
            mdetails.setPrfMailBcc(adPreference.getPrfMailBcc());
            mdetails.setPrfMailConfig(adPreference.getPrfMailConfig());

            if (adPreference.getPrfInvEnablePosIntegration() == EJBCommon.TRUE || adPreference.getPrfInvPosAdjustmentAccount() != null) {
                glPosAdjustmentAccount = glChartOfAccountHome.findByPrimaryKey(adPreference.getPrfInvPosAdjustmentAccount() == null ?
                        EJBCommon.TRUE : adPreference.getPrfInvPosAdjustmentAccount());
                mdetails.setPrfInvGlCoaPosAdjustmentAccountNumber(glPosAdjustmentAccount.getCoaAccountNumber());
                mdetails.setPrfInvGlCoaPosAdjustmentAccountDescription(glPosAdjustmentAccount.getCoaAccountDescription());
            }
            if (adPreference.getPrfInvGeneralAdjustmentAccount() != null && !adPreference.getPrfInvGeneralAdjustmentAccount().equals(0)) {
                glGeneralAdjustmentAccount = glChartOfAccountHome.findByPrimaryKey(adPreference.getPrfInvGeneralAdjustmentAccount() == null ?
                        EJBCommon.TRUE : adPreference.getPrfInvGeneralAdjustmentAccount());
                mdetails.setPrfInvGlCoaGeneralAdjustmentAccountNumber(glGeneralAdjustmentAccount.getCoaAccountNumber());
                mdetails.setPrfInvGlCoaGeneralAdjustmentAccountDescription(glGeneralAdjustmentAccount.getCoaAccountDescription());
            }

            if (adPreference.getPrfInvIssuanceAdjustmentAccount() != null && !adPreference.getPrfInvIssuanceAdjustmentAccount().equals(0)) {
                glIssuanceAdjustmentAccount = glChartOfAccountHome.findByPrimaryKey(adPreference.getPrfInvIssuanceAdjustmentAccount() == null ?
                        EJBCommon.TRUE : adPreference.getPrfInvIssuanceAdjustmentAccount());
                mdetails.setPrfInvGlCoaIssuanceAdjustmentAccountNumber(glIssuanceAdjustmentAccount.getCoaAccountNumber());
                mdetails.setPrfInvGlCoaIssuanceAdjustmentAccountDescription(glIssuanceAdjustmentAccount.getCoaAccountDescription());
            }

            if (adPreference.getPrfInvProductionAdjustmentAccount() != null && !adPreference.getPrfInvProductionAdjustmentAccount().equals(0)) {
                glProductionAdjustmentAccount = glChartOfAccountHome.findByPrimaryKey(adPreference.getPrfInvProductionAdjustmentAccount() == null ?
                        EJBCommon.TRUE : adPreference.getPrfInvProductionAdjustmentAccount());
                mdetails.setPrfInvGlCoaProductionAdjustmentAccountNumber(glProductionAdjustmentAccount.getCoaAccountNumber());
                mdetails.setPrfInvGlCoaProductionAdjustmentAccountDescription(glProductionAdjustmentAccount.getCoaAccountDescription());
            }

            if (adPreference.getPrfInvWastageAdjustmentAccount() != null && !adPreference.getPrfInvWastageAdjustmentAccount().equals(0)) {

                glWastageAdjustmentAccount = glChartOfAccountHome.findByPrimaryKey(adPreference.getPrfInvWastageAdjustmentAccount() == null ?
                        EJBCommon.TRUE : adPreference.getPrfInvWastageAdjustmentAccount());
                mdetails.setPrfInvGlCoaWastageAdjustmentAccountNumber(glWastageAdjustmentAccount.getCoaAccountNumber());
                mdetails.setPrfInvGlCoaWastageAdjustmentAccountDescription(glWastageAdjustmentAccount.getCoaAccountDescription());
            }

            if (adPreference.getPrfMiscPosDiscountAccount() != null && !adPreference.getPrfMiscPosDiscountAccount().equals(0)) {

                glPosDiscountAccount = glChartOfAccountHome.findByPrimaryKey(adPreference.getPrfMiscPosDiscountAccount() == null ?
                        EJBCommon.TRUE : adPreference.getPrfMiscPosDiscountAccount());
                mdetails.setPrfMiscGlCoaPosDiscountAccountNumber(glPosDiscountAccount.getCoaAccountNumber());
                mdetails.setPrfMiscGlCoaPosDiscountAccountDescription(glPosDiscountAccount.getCoaAccountDescription());
            }

            if (adPreference.getPrfMiscPosGiftCertificateAccount() != null && !adPreference.getPrfMiscPosGiftCertificateAccount().equals(0)) {

                glPosGiftCertificateAccount = glChartOfAccountHome.findByPrimaryKey(adPreference.getPrfMiscPosGiftCertificateAccount() == null ?
                        EJBCommon.TRUE : adPreference.getPrfMiscPosGiftCertificateAccount());
                mdetails.setPrfMiscGlCoaPosGiftCertificateAccountNumber(glPosGiftCertificateAccount.getCoaAccountNumber());
                mdetails.setPrfMiscGlCoaPosGiftCertificateAccountDescription(glPosGiftCertificateAccount.getCoaAccountDescription());
            }

            if (adPreference.getPrfMiscPosServiceChargeAccount() != null && !adPreference.getPrfMiscPosServiceChargeAccount().equals(0)) {

                glPosServiceChargeAccount = glChartOfAccountHome.findByPrimaryKey(adPreference.getPrfMiscPosServiceChargeAccount() == null ?
                        EJBCommon.TRUE : adPreference.getPrfMiscPosServiceChargeAccount());
                mdetails.setPrfMiscGlCoaPosServiceChargeAccountNumber(glPosServiceChargeAccount.getCoaAccountNumber());
                mdetails.setPrfMiscGlCoaPosServiceChargeAccountDescription(glPosServiceChargeAccount.getCoaAccountDescription());
            }

            if (adPreference.getPrfMiscPosDineInChargeAccount() != null && !adPreference.getPrfMiscPosDineInChargeAccount().equals(0)) {

                glPosDineInChargeAccount = glChartOfAccountHome.findByPrimaryKey(adPreference.getPrfMiscPosDineInChargeAccount() == null ?
                        EJBCommon.TRUE : adPreference.getPrfMiscPosDineInChargeAccount());
                mdetails.setPrfMiscGlCoaPosDineInChargeAccountNumber(glPosDineInChargeAccount.getCoaAccountNumber());
                mdetails.setPrfMiscGlCoaPosDineInChargeAccountDescription(glPosDineInChargeAccount.getCoaAccountDescription());
            }

            if (adPreference.getPrfArGlCoaCustomerDepositAccount() != null && !adPreference.getPrfArGlCoaCustomerDepositAccount().equals(0)) {

                glCoaCustomerDepositAccount = glChartOfAccountHome.findByPrimaryKey(adPreference.getPrfArGlCoaCustomerDepositAccount() == null ?
                        EJBCommon.TRUE : adPreference.getPrfArGlCoaCustomerDepositAccount());
                mdetails.setPrfArGlCoaCustomerDepositAccountNumber(glCoaCustomerDepositAccount.getCoaAccountNumber());
                mdetails.setPrfArGlCoaCustomerDepositAccountDescription(glCoaCustomerDepositAccount.getCoaAccountDescription());
            }

            if (adPreference.getPrfInvGlCoaVarianceAccount() != null && !adPreference.getPrfInvGlCoaVarianceAccount().equals(0)) {

                try {

                    LocalGlChartOfAccount glCoaVarianceAccount = glChartOfAccountHome.findByPrimaryKey(adPreference.getPrfInvGlCoaVarianceAccount() == null ?
                            EJBCommon.TRUE : adPreference.getPrfInvGlCoaVarianceAccount());
                    mdetails.setPrfInvGlCoaVarianceAccountNumber(glCoaVarianceAccount.getCoaAccountNumber());
                    mdetails.setPrfInvGlCoaVarianceAccountDescription(glCoaVarianceAccount.getCoaAccountDescription());

                } catch (FinderException ex) {

                }
            }
            return mdetails;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void saveAdPrfEntry(AdModPreferenceDetails mdetails, String withholdingTaxCode, Integer companyCode) throws GlobalRecordAlreadyExistException, AdPRFCoaGlAccruedVatAccountNotFoundException, AdPRFCoaGlPettyCashAccountNotFoundException, AdPRFCoaGlPOSAdjustmentAccountNotFoundException, AdPRFCoaGlPosDiscountAccountNotFoundException, AdPRFCoaGlPosGiftCertificateAccountNotFoundException, AdPRFCoaGlPosServiceChargeAccountNotFoundException, AdPRFCoaGlPosDineInChargeAccountNotFoundException, AdPRFCoaGlCustomerDepositAccountNotFoundException {

        Debug.print("AdPreferenceControllerBean saveAdPrfEntry");

        LocalGlChartOfAccount glAccruedVatAccount = null;
        LocalGlChartOfAccount glPettyCashAccount = null;
        LocalGlChartOfAccount glPosAdjustmentAccount = null;
        LocalGlChartOfAccount glGeneralAdjustmentAccount = null;
        LocalGlChartOfAccount glIssuanceAdjustmentAccount = null;
        LocalGlChartOfAccount glProductionAdjustmentAccount = null;
        LocalGlChartOfAccount glWastageAdjustmentAccount = null;
        LocalGlChartOfAccount glPosDiscountAccount = null;
        LocalGlChartOfAccount glPosGiftCertificateAccount = null;
        LocalGlChartOfAccount glPosServiceChargeAccount = null;
        LocalGlChartOfAccount glPosDineInChargeAccount = null;
        LocalGlChartOfAccount glCoaCustomerDepositAccount = null;
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            adPreference.setPrfInvCentralWarehouse(mdetails.getPrfInvCentralWarehouse());

            adPreference.setPrfAllowSuspensePosting(mdetails.getPrfAllowSuspensePosting());
            adPreference.setPrfGlJournalLineNumber(mdetails.getPrfGlJournalLineNumber());
            adPreference.setPrfApJournalLineNumber(mdetails.getPrfApJournalLineNumber());
            adPreference.setPrfArInvoiceLineNumber(mdetails.getPrfArInvoiceLineNumber());
            adPreference.setPrfApWTaxRealization(mdetails.getPrfApWTaxRealization());
            adPreference.setPrfArWTaxRealization(mdetails.getPrfArWTaxRealization());
            adPreference.setPrfEnableGlJournalBatch(mdetails.getPrfEnableGlJournalBatch());
            adPreference.setPrfEnableGlRecomputeCoaBalance(mdetails.getPrfEnableGlRecomputeCoaBalance());

            adPreference.setPrfEnableApVoucherBatch(mdetails.getPrfEnableApVoucherBatch());
            adPreference.setPrfEnableApPOBatch(mdetails.getPrfEnableApPOBatch());
            adPreference.setPrfEnableApCheckBatch(mdetails.getPrfEnableApCheckBatch());
            adPreference.setPrfEnableArInvoiceBatch(mdetails.getPrfEnableArInvoiceBatch());
            adPreference.setPrfEnableArInvoiceInterestGeneration(mdetails.getPrfEnableArInvoiceInterestGeneration());
            adPreference.setPrfEnableArReceiptBatch(mdetails.getPrfEnableArReceiptBatch());
            adPreference.setPrfEnableArMiscReceiptBatch(mdetails.getPrfEnableArMiscReceiptBatch());
            adPreference.setPrfApGlPostingType(mdetails.getPrfApGlPostingType());
            adPreference.setPrfArGlPostingType(mdetails.getPrfArGlPostingType());
            adPreference.setPrfCmGlPostingType(mdetails.getPrfCmGlPostingType());
            adPreference.setPrfCmUseBankForm(mdetails.getPrfCmUseBankForm());
            adPreference.setPrfInvInventoryLineNumber(mdetails.getPrfInvInventoryLineNumber());
            adPreference.setPrfInvQuantityPrecisionUnit(mdetails.getPrfInvQuantityPrecisionUnit());
            adPreference.setPrfInvCostPrecisionUnit(mdetails.getPrfInvCostPrecisionUnit());
            adPreference.setPrfInvGlPostingType(mdetails.getPrfInvGlPostingType());
            adPreference.setPrfGlPostingType(mdetails.getPrfGlPostingType());
            adPreference.setPrfApFindCheckDefaultType(mdetails.getPrfApFindCheckDefaultType());
            adPreference.setPrfArFindReceiptDefaultType(mdetails.getPrfArFindReceiptDefaultType());
            adPreference.setPrfInvEnableShift(mdetails.getPrfInvEnableShift());
            adPreference.setPrfEnableInvBUABatch(mdetails.getPrfEnableInvBUABatch());
            adPreference.setPrfApUseAccruedVat(mdetails.getPrfApUseAccruedVat());
            adPreference.setPrfApDefaultCheckDate(mdetails.getPrfApDefaultCheckDate());
            adPreference.setPrfApDefaultChecker(mdetails.getPrfApDefaultChecker());
            adPreference.setPrfApDefaultApprover(mdetails.getPrfApDefaultApprover());
            adPreference.setPrfApUseSupplierPulldown(mdetails.getPrfApUseSupplierPulldown());
            adPreference.setPrfArUseCustomerPulldown(mdetails.getPrfArUseCustomerPulldown());
            adPreference.setPrfApAutoGenerateSupplierCode(mdetails.getPrfApAutoGenerateSupplierCode());
            adPreference.setPrfApReferenceNumberValidation(mdetails.getPrfApReferenceNumberValidation());
            adPreference.setPrfApCheckVoucherDataSource(mdetails.getPrfApCheckVoucherDataSource());
            adPreference.setPrfApDefaultPrTax(mdetails.getPrfApDefaultPrTax());
            adPreference.setPrfApDefaultPrCurrency(mdetails.getPrfApDefaultPrCurrency());
            adPreference.setPrfArSalesInvoiceDataSource(mdetails.getPrfArSalesInvoiceDataSource());
            adPreference.setPrfArAutoComputeCogs(mdetails.getPrfArAutoComputeCogs());

            adPreference.setPrfArMonthlyInterestRate(mdetails.getPrfArMonthlyInterestRate());
            adPreference.setPrfApAgingBucket(mdetails.getPrfApAgingBucket());
            adPreference.setPrfArAgingBucket(mdetails.getPrfArAgingBucket());
            adPreference.setPrfArAllowPriorDate(mdetails.getPrfArAllowPriorDate());
            adPreference.setPrfApShowPrCost(mdetails.getPrfApShowPrCost());
            adPreference.setPrfApDebitMemoOverrideCost(mdetails.getPrfApDebitMemoOverrideCost());
            adPreference.setPrfGlYearEndCloseRestriction(mdetails.getPrfGlYearEndCloseRestriction());
            adPreference.setPrfArEnablePaymentTerm(mdetails.getPrfArEnablePaymentTerm());
            adPreference.setPrfArDisableSalesPrice(mdetails.getPrfArDisableSalesPrice());
            adPreference.setPrfInvItemLocationShowAll(mdetails.getPrfInvItemLocationShowAll());
            adPreference.setPrfInvItemLocationAddByItemList(mdetails.getPrfInvItemLocationAddByItemList());
            adPreference.setPrfArValidateCustomerEmail(mdetails.getPrfArValidateCustomerEmail());
            adPreference.setPrfArCheckInsufficientStock(mdetails.getPrfArCheckInsufficientStock());
            adPreference.setPrfArDetailedReceivable(mdetails.getPrfArDetailedReceivable());
            adPreference.setPrfAdDisableMultipleLogin(mdetails.getPrfAdDisableMultipleLogin());
            adPreference.setPrfAdEnableEmailNotification(mdetails.getPrfAdEnableEmailNotification());

            adPreference.setPrfArSoSalespersonRequired(mdetails.getPrfArSoSalespersonRequired());
            adPreference.setPrfArInvcSalespersonRequired(mdetails.getPrfArInvcSalespersonRequired());

            adPreference.setPrfMailHost(mdetails.getPrfMailHost());
            adPreference.setPrfMailSocketFactoryPort(mdetails.getPrfMailSocketFactoryPort());
            adPreference.setPrfMailPort(mdetails.getPrfMailPort());
            adPreference.setPrfMailFrom(mdetails.getPrfMailFrom());
            adPreference.setPrfMailAuthenticator(mdetails.getPrfMailAuthenticator());
            adPreference.setPrfMailPassword(mdetails.getPrfMailPassword());
            adPreference.setPrfMailTo(mdetails.getPrfMailTo());
            adPreference.setPrfMailCc(mdetails.getPrfMailCc());
            adPreference.setPrfMailBcc(mdetails.getPrfMailBcc());
            adPreference.setPrfMailConfig(mdetails.getPrfMailConfig());
            adPreference.setPrfUserRestWebService(mdetails.getPrfUserRestWebService());
            adPreference.setPrfPassRestWebService(mdetails.getPrfPassRestWebService());
            adPreference.setPrfAttachmentPath(mdetails.getPrfAttachmentPath());

            if (mdetails.getPrfApAutoGenerateSupplierCode() == EJBCommon.TRUE) {
                adPreference.setPrfApNextSupplierCode(mdetails.getPrfApNextSupplierCode());
            } else if (mdetails.getPrfApAutoGenerateSupplierCode() == EJBCommon.FALSE) {
                adPreference.setPrfApNextSupplierCode(null);
            }

            if (mdetails.getPrfApGlCoaAccruedVatAccountNumber() != null && mdetails.getPrfApGlCoaAccruedVatAccountNumber().length() > 0) {

                try {
                    Debug.print("glAccruedVatAccount");

                    glAccruedVatAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getPrfApGlCoaAccruedVatAccountNumber(), companyCode);
                    adPreference.setPrfApGlCoaAccruedVatAccount(glAccruedVatAccount.getCoaCode());

                } catch (FinderException ex) {

                    throw new AdPRFCoaGlAccruedVatAccountNotFoundException();
                }

            } else {

                adPreference.setPrfApGlCoaAccruedVatAccount(null);
            }

            if (mdetails.getPrfApGlCoaPettyCashAccountNumber() != null && mdetails.getPrfApGlCoaPettyCashAccountNumber().length() > 0) {

                try {
                    Debug.print("glPettyCashAccount");

                    glPettyCashAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getPrfApGlCoaPettyCashAccountNumber(), companyCode);
                    adPreference.setPrfApGlCoaPettyCashAccount(glPettyCashAccount.getCoaCode());

                } catch (FinderException ex) {

                    throw new AdPRFCoaGlPettyCashAccountNotFoundException();
                }

            } else {

                adPreference.setPrfApGlCoaPettyCashAccount(null);
            }

            if (mdetails.getPrfInvGlCoaGeneralAdjustmentAccountNumber() != null && mdetails.getPrfInvGlCoaGeneralAdjustmentAccountNumber().length() > 0) {

                try {
                    Debug.print("glGeneralAdjustmentAccount");

                    glGeneralAdjustmentAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getPrfInvGlCoaGeneralAdjustmentAccountNumber(), companyCode);
                    adPreference.setPrfInvGeneralAdjustmentAccount(glGeneralAdjustmentAccount.getCoaCode());

                } catch (FinderException ex) {

                    ex.printStackTrace();
                }

            } else {

                adPreference.setPrfInvGeneralAdjustmentAccount(null);
            }

            if (mdetails.getPrfInvGlCoaIssuanceAdjustmentAccountNumber() != null && mdetails.getPrfInvGlCoaIssuanceAdjustmentAccountNumber().length() > 0) {

                try {
                    Debug.print("glIssuanceAdjustmentAccount");

                    glIssuanceAdjustmentAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getPrfInvGlCoaIssuanceAdjustmentAccountNumber(), companyCode);
                    adPreference.setPrfInvIssuanceAdjustmentAccount(glIssuanceAdjustmentAccount.getCoaCode());

                } catch (FinderException ex) {

                    ex.printStackTrace();
                }

            } else {

                adPreference.setPrfInvIssuanceAdjustmentAccount(null);
            }

            if (mdetails.getPrfInvGlCoaProductionAdjustmentAccountNumber() != null && mdetails.getPrfInvGlCoaProductionAdjustmentAccountNumber().length() > 0) {

                try {
                    Debug.print("glProductionAdjustmentAccount");

                    glProductionAdjustmentAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getPrfInvGlCoaProductionAdjustmentAccountNumber(), companyCode);
                    adPreference.setPrfInvProductionAdjustmentAccount(glProductionAdjustmentAccount.getCoaCode());

                } catch (FinderException ex) {

                    ex.printStackTrace();
                }

            } else {

                adPreference.setPrfInvProductionAdjustmentAccount(null);
            }

            if (mdetails.getPrfInvGlCoaWastageAdjustmentAccountNumber() != null && mdetails.getPrfInvGlCoaWastageAdjustmentAccountNumber().length() > 0) {

                try {
                    Debug.print("glWastageAdjustmentAccount");

                    glWastageAdjustmentAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getPrfInvGlCoaWastageAdjustmentAccountNumber(), companyCode);
                    adPreference.setPrfInvWastageAdjustmentAccount(glWastageAdjustmentAccount.getCoaCode());

                } catch (FinderException ex) {

                    ex.printStackTrace();
                }

            } else {

                adPreference.setPrfInvProductionAdjustmentAccount(null);
            }

            if (adPreference.getArWithholdingTaxCode() != null) {
                adPreference.getArWithholdingTaxCode().dropAdPreference(adPreference);
            }

            if (withholdingTaxCode != null && withholdingTaxCode.length() > 0) {

                LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName(withholdingTaxCode, companyCode);
                arWithholdingTaxCode.addAdPreference(adPreference);
            }

            adPreference.setPrfInvEnablePosAutoPostUpload(mdetails.getPrfInvEnablePosAutoPostUpload());
            adPreference.setPrfInvEnablePosIntegration(mdetails.getPrfInvEnablePosIntegration());
            if (adPreference.getPrfInvEnablePosIntegration() == EJBCommon.TRUE) {

                try {
                    Debug.print("glPosAdjustmentAccount");

                    glPosAdjustmentAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getPrfInvGlCoaPosAdjustmentAccountNumber(), companyCode);
                    adPreference.setPrfInvPosAdjustmentAccount(glPosAdjustmentAccount.getCoaCode());

                } catch (FinderException ex) {

                    throw new AdPRFCoaGlPOSAdjustmentAccountNotFoundException();
                }

            } else {

                adPreference.setPrfInvPosAdjustmentAccount(null);
            }

            if (mdetails.getPrfInvGlCoaAdjustmentRequestAccountNumber() != null) {
                glPosAdjustmentAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getPrfInvGlCoaAdjustmentRequestAccountNumber(), companyCode);
                adPreference.setPrfInvPosAdjustmentAccount(glPosAdjustmentAccount.getCoaCode());
            }

            if (mdetails.getPrfMiscGlCoaPosDiscountAccountNumber() != null && mdetails.getPrfMiscGlCoaPosDiscountAccountNumber().length() > 0) {

                try {
                    Debug.print("glPosDiscountAccount");

                    glPosDiscountAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getPrfMiscGlCoaPosDiscountAccountNumber(), companyCode);
                    adPreference.setPrfMiscPosDiscountAccount(glPosDiscountAccount.getCoaCode());

                } catch (FinderException ex) {

                    throw new AdPRFCoaGlPosDiscountAccountNotFoundException();
                }

            } else {

                adPreference.setPrfMiscPosDiscountAccount(null);
            }

            if (mdetails.getPrfMiscGlCoaPosGiftCertificateAccountNumber() != null && mdetails.getPrfMiscGlCoaPosGiftCertificateAccountNumber().length() > 0) {

                try {
                    Debug.print("glPosGiftCertificateAccount");

                    glPosGiftCertificateAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getPrfMiscGlCoaPosGiftCertificateAccountNumber(), companyCode);
                    adPreference.setPrfMiscPosGiftCertificateAccount(glPosGiftCertificateAccount.getCoaCode());

                } catch (FinderException ex) {

                    throw new AdPRFCoaGlPosGiftCertificateAccountNotFoundException();
                }

            } else {

                adPreference.setPrfMiscPosGiftCertificateAccount(null);
            }

            if (mdetails.getPrfMiscGlCoaPosServiceChargeAccountNumber() != null && mdetails.getPrfMiscGlCoaPosServiceChargeAccountNumber().length() > 0) {

                try {
                    Debug.print("glPosServiceChargeAccount");

                    glPosServiceChargeAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getPrfMiscGlCoaPosServiceChargeAccountNumber(), companyCode);
                    adPreference.setPrfMiscPosServiceChargeAccount(glPosServiceChargeAccount.getCoaCode());

                } catch (FinderException ex) {

                    throw new AdPRFCoaGlPosServiceChargeAccountNotFoundException();
                }

            } else {

                adPreference.setPrfMiscPosServiceChargeAccount(null);
            }

            if (mdetails.getPrfMiscGlCoaPosDineInChargeAccountNumber() != null && mdetails.getPrfMiscGlCoaPosDineInChargeAccountNumber().length() > 0) {

                try {
                    Debug.print("glPosDineInChargeAccount");

                    glPosDineInChargeAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getPrfMiscGlCoaPosDineInChargeAccountNumber(), companyCode);
                    adPreference.setPrfMiscPosDineInChargeAccount(glPosDineInChargeAccount.getCoaCode());

                } catch (FinderException ex) {

                    throw new AdPRFCoaGlPosDineInChargeAccountNotFoundException();
                }

            } else {

                adPreference.setPrfMiscPosDineInChargeAccount(null);
            }

            if (mdetails.getPrfArGlCoaCustomerDepositAccountNumber() != null && mdetails.getPrfArGlCoaCustomerDepositAccountNumber().length() > 0) {

                try {
                    Debug.print("glCoaCustomerDepositAccount");

                    glCoaCustomerDepositAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getPrfArGlCoaCustomerDepositAccountNumber(), companyCode);
                    adPreference.setPrfArGlCoaCustomerDepositAccount(glCoaCustomerDepositAccount.getCoaCode());

                } catch (FinderException ex) {

                    throw new AdPRFCoaGlCustomerDepositAccountNotFoundException();
                }

            } else {

                adPreference.setPrfArGlCoaCustomerDepositAccount(null);
            }

            if (mdetails.getPrfInvGlCoaVarianceAccountNumber() != null && mdetails.getPrfInvGlCoaVarianceAccountNumber().length() > 0) {

                try {
                    Debug.print("glCoaVarianceAccount");

                    LocalGlChartOfAccount glCoaVarianceAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getPrfInvGlCoaVarianceAccountNumber(), companyCode);
                    adPreference.setPrfInvGlCoaVarianceAccount(glCoaVarianceAccount.getCoaCode());

                } catch (FinderException ex) {

                    throw new AdPRFCoaGlVarianceAccountNotFoundException();
                }

            } else {

                adPreference.setPrfInvGlCoaVarianceAccount(null);
            }

        } catch (AdPRFCoaGlAccruedVatAccountNotFoundException | AdPRFCoaGlCustomerDepositAccountNotFoundException |
                 AdPRFCoaGlPosDineInChargeAccountNotFoundException |
                 AdPRFCoaGlPosServiceChargeAccountNotFoundException |
                 AdPRFCoaGlPosGiftCertificateAccountNotFoundException | AdPRFCoaGlPosDiscountAccountNotFoundException |
                 AdPRFCoaGlPOSAdjustmentAccountNotFoundException | AdPRFCoaGlPettyCashAccountNotFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {
            System.out.print("ERROR! - " + ex);
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArWtcAll(Integer companyCode) {

        Debug.print("AdPreferenceControllerBean getArWtcAll");

        ArrayList list = new ArrayList();
        try {

            Collection arWithholdingTaxCodes = arWithholdingTaxCodeHome.findEnabledWtcAll(companyCode);

            for (Object withholdingTaxCode : arWithholdingTaxCodes) {

                LocalArWithholdingTaxCode arWithholdingTaxCode = (LocalArWithholdingTaxCode) withholdingTaxCode;

                list.add(arWithholdingTaxCode.getWtcName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArTcAll(Integer companyCode) {

        Debug.print("AdPreferenceControllerBean getArTcAll");

        ArrayList list = new ArrayList();
        try {

            Collection arTaxCodes = arTaxCodeHome.findEnabledTcAll(companyCode);

            for (Object taxCode : arTaxCodes) {

                LocalArTaxCode arTaxCode = (LocalArTaxCode) taxCode;

                list.add(arTaxCode.getTcName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGlFcAll(Integer companyCode) {

        Debug.print("AdPreferenceControllerBean getGlFcAll");

        ArrayList list = new ArrayList();
        try {

            Collection glFunctionalCurrencies = glFunctionalCurrencyHome.findFcAllEnabled(new java.util.Date(), companyCode);

            for (Object functionalCurrency : glFunctionalCurrencies) {

                LocalGlFunctionalCurrency glFunctionalCurrency = (LocalGlFunctionalCurrency) functionalCurrency;

                list.add(glFunctionalCurrency.getFcName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getInvLocAll(Integer companyCode) {

        Debug.print("AdPreferenceControllerBean getInvLocAll");

        Collection invLocations;
        ArrayList list = new ArrayList();
        try {

            invLocations = invLocationHome.findLocAll(companyCode);

            if (invLocations.isEmpty()) {

                return null;
            }

            for (Object location : invLocations) {

                LocalInvLocation invLocation = (LocalInvLocation) location;

                String details = null;
                if (invLocation.getLocLvType().equals(EJBCommon.WAREHOUSE)) {
                    details = invLocation.getLocName();
                    list.add(details);
                }
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdApplicationAllInstalled(Integer companyCode) {

        Debug.print("AdPreferenceControllerBean getAdApplicationAllInstalled");

        ArrayList list = new ArrayList();
        try {

            Collection adApplications = adApplicationHome.findAppAllInstalled(companyCode);

            for (Object application : adApplications) {

                LocalAdApplication adApplication = (LocalAdApplication) application;
                AdApplicationDetails details = new AdApplicationDetails(adApplication.getAppCode(), adApplication.getAppName(), adApplication.getAppDescription());
                list.add(details);
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("AdPreferenceControllerBean ejbCreate");
    }
}