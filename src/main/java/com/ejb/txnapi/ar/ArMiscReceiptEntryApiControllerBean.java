package com.ejb.txnapi.ar;

import com.ejb.ConfigurationClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ar.*;
import com.ejb.dao.gl.*;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ar.*;
import com.ejb.entities.gen.LocalGenField;
import com.ejb.entities.gl.*;
import com.ejb.entities.inv.*;
import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.ar.ArREDuplicatePayfileReferenceNumberException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.ejb.restfulapi.ar.models.LineItemRequest;
import com.ejb.restfulapi.ar.models.MemolineDetails;
import com.ejb.restfulapi.ar.models.ReceiptApiResponse;
import com.ejb.restfulapi.ar.models.ReceiptRequest;
import com.ejb.txnreports.inv.InvRepItemCostingController;
import com.util.*;
import com.util.ad.AdBranchDetails;
import com.util.ar.ArReceiptDetails;
import com.util.mod.ar.ArModInvoiceLineDetails;
import com.util.mod.ar.ArModInvoiceLineItemDetails;

import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import javax.naming.NamingException;
import java.util.*;

@Stateless(name = "ArMiscReceiptEntryApiControllerEJB")
public class ArMiscReceiptEntryApiControllerBean extends EJBContextClass implements ArMiscReceiptEntryApiController {

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
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalAdBranchBankAccountHome adBranchBankAccountHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
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
    private LocalArTaxCodeHome arTaxCodeHome;
    @EJB
    private LocalArWithholdingTaxCodeHome arWithholdingTaxCodeHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private InvRepItemCostingController ejbRIC;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvAdjustmentHome invAdjustmentHome;
    @EJB
    private LocalInvAdjustmentLineHome invAdjustmentLineHome;
    @EJB
    private LocalInvDistributionRecordHome invDistributionRecordHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private LocalArInvoiceLineItemHome arInvoiceLineItemHome;
    @EJB
    private LocalAdBranchArTaxCodeHome adBranchArTaxCodeHome;
    @EJB
    private LocalArInvoiceLineHome arInvoiceLineHome;
    @EJB
    private LocalArStandardMemoLineHome arStandardMemoLineHome;
    @EJB
    private LocalAdBranchStandardMemoLineHome adBranchStandardMemoLineHome;
    @EJB
    private LocalArAutoAccountingSegmentHome arAutoAccountingSegmentHome;

    @Override
    public Integer saveArRctIliEntry(ArReceiptDetails details, String bankAccountName, String bankAccountCard1Name, String bankAccountCard2Name,
                                     String bankAccountCard3Name, String taxCode, String wTaxCode, String foreignCurrency,
                                     String customerCode, ArrayList receiptLines,
                                     Integer branchCode, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException,
            GlobalConversionDateNotExistException, GlobalTransactionAlreadyApprovedException,
            GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException,
            GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException,
            GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException,
            GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException,
            GlobalJournalNotBalanceException, GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException,
            GlobalRecordAlreadyAssignedException, AdPRFCoaGlVarianceAccountNotFoundException,
            GlobalExpiryDateNotFoundException, GlobalMiscInfoIsRequiredException, GlobalRecordInvalidException {

        Debug.print("ArMiscReceiptEntryApiControllerBean saveArRctIliEntry");

        LocalArReceipt arReceipt = null;

        try {

            // validate if misc receipt is already deleted
            try {
                if (details.getRctCode() != null) {
                    arReceipt = arReceiptHome.findByPrimaryKey(details.getRctCode());
                }
            }
            catch (FinderException ex) {
                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if receipt number is unique receipt number is automatic then set next sequence
            this.receiptNumberValidation(details, branchCode, companyCode, arReceipt);

            // validate if conversion date exists
            this.checkConversionDateValidation(details, foreignCurrency, companyCode);

            // create misc receipt
            arReceipt = arReceiptHome
                    .RctType("MISC")
                    .RctDescription(details.getRctDescription())
                    .RctDate(details.getRctDate())
                    .RctNumber(details.getRctNumber())
                    .RctReferenceNumber(details.getRctReferenceNumber())
                    .RctCheckNo(details.getRctCheckNo())
                    .RctPayfileReferenceNumber(details.getRctPayfileReferenceNumber())
                    .RctAmount(details.getRctAmount())
                    .RctAmountCash(details.getRctAmount())
                    .RctConversionDate(details.getRctConversionDate())
                    .RctConversionRate(details.getRctConversionRate())
                    .RctPaymentMethod(details.getRctPaymentMethod())
                    .RctCreatedBy(details.getRctCreatedBy())
                    .RctDateCreated(details.getRctDateCreated())
                    .RctAdBranch(branchCode)
                    .RctAdCompany(companyCode)
                    .buildReceipt();

            arReceipt.setRctDocumentType(details.getRctDocumentType());
            arReceipt.setReportParameter(details.getReportParameter());

            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(bankAccountName, companyCode, details.getCompanyShortName());
            arReceipt.setAdBankAccount(adBankAccount);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(foreignCurrency, companyCode, details.getCompanyShortName());
            arReceipt.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(customerCode, companyCode, details.getCompanyShortName());
            arReceipt.setArCustomer(arCustomer);

            LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName(taxCode, companyCode, details.getCompanyShortName());
            arReceipt.setArTaxCode(arTaxCode);

            LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName(wTaxCode, companyCode, details.getCompanyShortName());
            arReceipt.setArWithholdingTaxCode(arWithholdingTaxCode);

            setBankAccountCards(bankAccountCard1Name, bankAccountCard2Name, bankAccountCard3Name, companyCode, arReceipt, details.getCompanyShortName());

            if (details.getRctCustomerName().length() > 0 && !arCustomer.getCstName().equals(details.getRctCustomerName())) {
                arReceipt.setRctCustomerName(details.getRctCustomerName());
            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode, details.getCompanyShortName());

            // add receipt line items and distribution records
            double totalTaxAmount = 0d;
            double totalAmount = 0d;
            double totalDiscount = 0d;

            // TODO: Make sure Global AD Preference apply validation to avoid this fall back value.
            String centralWarehouse = !adPreference.getPrfInvCentralWarehouse().equals("") && adPreference.getPrfInvCentralWarehouse() != null
                    ? adPreference.getPrfInvCentralWarehouse()
                    : "POM WAREHOUSE LOCATION";
            LocalAdBranch centralWarehouseBranchCode = adBranchHome.findByBrName(centralWarehouse, companyCode, details.getCompanyShortName());

            if (centralWarehouseBranchCode == null) {
                throw new Exception();
            }

            Iterator itemList = receiptLines.iterator();
            LocalInvItemLocation itemLocation;

            while (itemList.hasNext()) {

                ArModInvoiceLineItemDetails lineDetails = (ArModInvoiceLineItemDetails) itemList.next();

                itemLocation = getItemLocation(companyCode, lineDetails);
                isPriorDateAllowed(companyCode, branchCode, arReceipt, adPreference, itemLocation);

                LocalArInvoiceLineItem arInvoiceLineItem = this.addArIliEntry(lineDetails, arReceipt, itemLocation, companyCode, details.getCompanyShortName());

                double COST = getItemCosting(companyCode, centralWarehouseBranchCode.getBrCode(), arReceipt, itemLocation, arInvoiceLineItem, details.getCompanyShortName());
                double quantitySold = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(),
                        arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode, details.getCompanyShortName());

                LocalAdBranchItemLocation adBranchItemLocation = null;
                try {
                    adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(
                            arInvoiceLineItem.getInvItemLocation().getIlCode(), branchCode, companyCode, details.getCompanyShortName());
                }
                catch (FinderException ex) {
                }

                costOfGoodsSoldJournals(companyCode, branchCode, arReceipt, adPreference, itemLocation,
                        arInvoiceLineItem, COST, quantitySold, adBranchItemLocation, details.getCompanyShortName());
                inventoryRevenueJournals(companyCode, branchCode, arReceipt, arInvoiceLineItem,
                        adBranchItemLocation, details.getCompanyShortName());

                if (lineDetails.getIliTotalDiscount() > 0) {
                    discountJournals(companyCode, branchCode, arReceipt, lineDetails, details.getCompanyShortName());
                    totalDiscount += lineDetails.getIliTotalDiscount();
                }

                totalAmount += arInvoiceLineItem.getIliAmount();
                totalTaxAmount += arInvoiceLineItem.getIliTaxAmount();
            }

            double totalConvertedTaxAmount = this.convertForeignToFunctionalCurrency(
                    arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(),
                    arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), totalTaxAmount, companyCode, details.getCompanyShortName());

            // add tax DR
            if (!arTaxCode.getTcType().equals("NONE") && !arTaxCode.getTcType().equals("EXEMPT")) {
                LocalAdBranchArTaxCode adBranchTaxCode;
                try {
                    //TODO: Review this finder method that is causing finder exception
                    adBranchTaxCode = adBranchArTaxCodeHome.findBtcByTcCodeAndBrCode(arReceipt.getArTaxCode().getTcCode(), branchCode, companyCode);
                    this.addArDrEntry(arReceipt.getArDrNextLine(), "TAX", EJBCommon.FALSE, totalConvertedTaxAmount,
                            adBranchTaxCode.getBtcGlCoaTaxCode(), EJBCommon.FALSE, arReceipt, branchCode, companyCode, details.getCompanyShortName());
                }
                catch (FinderException ex) {
                    this.addArDrEntry(arReceipt.getArDrNextLine(), "TAX", EJBCommon.FALSE, totalConvertedTaxAmount,
                            arTaxCode.getGlChartOfAccount().getCoaCode(), EJBCommon.FALSE, arReceipt, branchCode, companyCode, details.getCompanyShortName());
                }
            }

            // add withholding tax DR
            double withholdingTaxAmount = 0d;
            if (arWithholdingTaxCode.getWtcRate() != 0) {
                withholdingTaxAmount = EJBCommon.roundIt(totalAmount * (arWithholdingTaxCode.getWtcRate() / 100),
                        this.getGlFcPrecisionUnit(companyCode, details.getCompanyShortName()));
                this.addArDrEntry(arReceipt.getArDrNextLine(), "W-TAX", EJBCommon.TRUE, withholdingTaxAmount,
                        arWithholdingTaxCode.getGlChartOfAccount().getCoaCode(), EJBCommon.FALSE, arReceipt,
                        branchCode, companyCode, details.getCompanyShortName());
            }

            // add cash distribution
            LocalAdBranchBankAccount adBranchBankAccount = null;
            LocalAdBranchBankAccount adBranchBankAccountCard1 = null;
            LocalAdBranchBankAccount adBranchBankAccountCard2 = null;
            LocalAdBranchBankAccount adBranchBankAccountCard3 = null;

            bankAccountCardJournals(branchCode, companyCode, arReceipt, adBranchBankAccount,
                    adBranchBankAccountCard1, adBranchBankAccountCard2, adBranchBankAccountCard3, details.getCompanyShortName());

            try {
                adBranchBankAccount = adBranchBankAccountHome
                        .findBbaByBaCodeAndBrCode(arReceipt.getAdBankAccount().getBaCode(), branchCode, companyCode);
            }
            catch (FinderException ex) {
            }

            bankAccountJournals(branchCode, companyCode, arReceipt, totalTaxAmount,
                    totalAmount, totalDiscount, withholdingTaxAmount, adBranchBankAccount, details.getCompanyShortName());

            // set receipt amount
            arReceipt.setRctAmount(totalAmount + totalTaxAmount - withholdingTaxAmount - totalDiscount);
            arReceipt.setRctAmountCash(arReceipt.getRctAmountCash() == 0 ? arReceipt.getRctAmount() : arReceipt.getRctAmountCash());

            // insufficient stock checking
            if (adPreference.getPrfArCheckInsufficientStock() == EJBCommon.TRUE) {
                boolean hasInsufficientItems = false;
                StringBuilder insufficientItems = new StringBuilder();

                Collection arInvoiceLineItems = arReceipt.getArInvoiceLineItems();
                Iterator i = arInvoiceLineItems.iterator();
                HashMap cstMap = new HashMap();

                while (i.hasNext()) {
                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) i.next();
                    String itemName = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName();
                    String locationName = arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName();
                    if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiNonInventoriable() == EJBCommon.FALSE) {
                        LocalInvCosting invCosting = null;
                        double itemQuantity = this.convertByUomAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(),
                                arInvoiceLineItem.getInvItemLocation().getInvItem(),
                                Math.abs(arInvoiceLineItem.getIliQuantity()), companyCode);
                        double currentQuantity = 0;
                        boolean isItemFound = false;

                        if (cstMap.containsKey(arInvoiceLineItem.getInvItemLocation().getIlCode().toString())) {
                            isItemFound = true;
                            currentQuantity = (Double) cstMap.get(arInvoiceLineItem.getInvItemLocation().getIlCode().toString());
                        } else {
                            invCosting = invCostingHome.getItemAverageCost(arReceipt.getRctDate(), itemName, locationName, branchCode, companyCode);
                        }

                        if (invCosting != null) {
                            currentQuantity = this.convertByUomAndQuantity(
                                    arInvoiceLineItem.getInvItemLocation().getInvItem().getInvUnitOfMeasure(),
                                    arInvoiceLineItem.getInvItemLocation().getInvItem(),
                                    invCosting.getCstRemainingQuantity(), companyCode);
                        }

                        double lowestQuantity = this.convertByUomAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(),
                                arInvoiceLineItem.getInvItemLocation().getInvItem(), 1, companyCode);
                        if ((invCosting == null && isItemFound == false) ||
                                currentQuantity == 0 ||
                                currentQuantity - itemQuantity <= -lowestQuantity) {
                            hasInsufficientItems = true;
                        }

                        currentQuantity -= itemQuantity;
                        if (isItemFound) {
                            cstMap.remove(arInvoiceLineItem.getInvItemLocation().getIlCode().toString());
                        }
                        cstMap.put(arInvoiceLineItem.getInvItemLocation().getIlCode().toString(), currentQuantity);
                    }
                }
                if (hasInsufficientItems) {
                    throw new GlobalRecordInvalidException(
                            insufficientItems.substring(0, insufficientItems.lastIndexOf(",")));
                }
            }

            this.executeArRctPost(arReceipt.getRctCode(), arReceipt.getRctLastModifiedBy(), branchCode, companyCode, details.getCompanyShortName());
            arReceipt.setRctApprovalStatus("N/A");

            return arReceipt.getRctCode();

        }
        catch (GlobalRecordAlreadyDeletedException | GlobalMiscInfoIsRequiredException |
               GlobalExpiryDateNotFoundException | AdPRFCoaGlVarianceAccountNotFoundException |
               GlobalRecordInvalidException |
               GlobalInventoryDateException | GlobalJournalNotBalanceException |
               GlJREffectiveDatePeriodClosedException |
               GlJREffectiveDateNoPeriodExistException | GlobalInvItemLocationNotFoundException |
               GlobalTransactionAlreadyPostedException |
               GlobalConversionDateNotExistException | GlobalDocumentNumberNotUniqueException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlobalBranchAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer saveArRctEntry(ArReceiptDetails details, String bankAccountName, String taxCode, String wTaxCode,
                                  String foreignCurrency, String customerCode, ArrayList receiptLines,
                                  Integer branchCode, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException,
            GlobalConversionDateNotExistException, GlobalTransactionAlreadyApprovedException,
            GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException,
            GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException,
            GlobalNoApprovalApproverFoundException, GlJREffectiveDateNoPeriodExistException,
            GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException,
            GlobalBranchAccountNumberInvalidException, GlobalRecordAlreadyAssignedException {

        Debug.print("ArMiscReceiptEntryApiControllerBean saveArRctEntry");

        LocalArReceipt arReceipt = null;

        try {

            // validate if receipt number is unique receipt number is automatic then set next sequence
            this.receiptNumberValidation(details, branchCode, companyCode, arReceipt);

            // validate if conversion date exists
            this.checkConversionDateValidation(details, foreignCurrency, companyCode);

            // create misc receipt
            arReceipt = arReceiptHome
                    .RctType("MISC")
                    .RctDescription(details.getRctDescription())
                    .RctDate(details.getRctDate())
                    .RctNumber(details.getRctNumber())
                    .RctReferenceNumber(details.getRctReferenceNumber())
                    .RctCheckNo(details.getRctCheckNo())
                    .RctPayfileReferenceNumber(details.getRctPayfileReferenceNumber())
                    .RctAmount(details.getRctAmount())
                    .RctAmountCash(details.getRctAmount())
                    .RctConversionDate(details.getRctConversionDate())
                    .RctConversionRate(details.getRctConversionRate())
                    .RctPaymentMethod(details.getRctPaymentMethod())
                    .RctCreatedBy(details.getRctCreatedBy())
                    .RctDateCreated(details.getRctDateCreated())
                    .RctAdBranch(branchCode)
                    .RctAdCompany(companyCode)
                    .buildReceipt(details.getCompanyShortName());

            arReceipt.setRctDocumentType(details.getRctDocumentType());
            arReceipt.setReportParameter(details.getReportParameter());

            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(bankAccountName, companyCode, details.getCompanyShortName());
            arReceipt.setAdBankAccount(adBankAccount);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(foreignCurrency, companyCode, details.getCompanyShortName());
            arReceipt.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(customerCode, companyCode, details.getCompanyShortName());
            arReceipt.setArCustomer(arCustomer);

            LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName(taxCode, companyCode, details.getCompanyShortName());
            arReceipt.setArTaxCode(arTaxCode);

            LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName(wTaxCode, companyCode, details.getCompanyShortName());
            arReceipt.setArWithholdingTaxCode(arWithholdingTaxCode);

            // add new invoice lines and distribution record
            double TOTAL_TAX = 0d;
            double TOTAL_LINE = 0d;
            double TOTAL_UNTAXABLE = 0d;

            for (Object o : receiptLines) {

                ArModInvoiceLineDetails mInvDetails = (ArModInvoiceLineDetails) o;
                LocalArInvoiceLine arInvoiceLine = this.addArIlEntry(mInvDetails, arReceipt, companyCode, details.getCompanyShortName());

                // add revenue/credit distributions
                this.addArDrEntry(arReceipt.getArDrNextLine(), "REVENUE", EJBCommon.FALSE,
                        arInvoiceLine.getIlAmount(), this.getArGlCoaRevenueAccount(arInvoiceLine, branchCode, companyCode, details.getCompanyShortName()), EJBCommon.FALSE,
                        arReceipt, branchCode, companyCode, details.getCompanyShortName());

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
                try {
                    adBranchTaxCode = adBranchArTaxCodeHome
                            .findBtcByTcCodeAndBrCode(arReceipt.getArTaxCode().getTcCode(), branchCode, companyCode, details.getCompanyShortName());
                }
                catch (FinderException ex) {

                }

                if (adBranchTaxCode != null) {
                    this.addArDrEntry(arReceipt.getArDrNextLine(), "TAX", EJBCommon.FALSE, TOTAL_TAX,
                            adBranchTaxCode.getBtcGlCoaTaxCode(), EJBCommon.FALSE, arReceipt, branchCode, companyCode, details.getCompanyShortName());

                } else {
                    this.addArDrEntry(arReceipt.getArDrNextLine(), "TAX", EJBCommon.FALSE, TOTAL_TAX,
                            arReceipt.getArTaxCode().getGlChartOfAccount().getCoaCode(), EJBCommon.FALSE, arReceipt,
                            branchCode, companyCode, details.getCompanyShortName());
                }
            }

            // add wtax distribution if necessary
            double W_TAX_AMOUNT = 0d;
            if (arWithholdingTaxCode.getWtcRate() != 0) {

                W_TAX_AMOUNT = EJBCommon.roundIt(
                        (TOTAL_LINE - TOTAL_UNTAXABLE) * (arWithholdingTaxCode.getWtcRate() / 100),
                        this.getGlFcPrecisionUnit(companyCode, details.getCompanyShortName()));

                this.addArDrEntry(arReceipt.getArDrNextLine(), "W-TAX", EJBCommon.TRUE, W_TAX_AMOUNT,
                        arWithholdingTaxCode.getGlChartOfAccount().getCoaCode(), EJBCommon.FALSE, arReceipt,
                        branchCode, companyCode, details.getCompanyShortName());
            }

            // add cash distribution
            LocalAdBranchBankAccount adBranchBankAccount = null;
            try {
                adBranchBankAccount = adBranchBankAccountHome
                        .findBbaByBaCodeAndBrCode(arReceipt.getAdBankAccount().getBaCode(), branchCode, companyCode, details.getCompanyShortName());

            }
            catch (FinderException ex) {

            }

            if (adBranchBankAccount != null) {

                this.addArDrEntry(arReceipt.getArDrNextLine(), "CASH", EJBCommon.TRUE,
                        TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT, adBranchBankAccount.getBbaGlCoaCashAccount(),
                        EJBCommon.FALSE, arReceipt, branchCode, companyCode, details.getCompanyShortName());

            } else {

                this.addArDrEntry(arReceipt.getArDrNextLine(), "CASH", EJBCommon.TRUE,
                        TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT, arReceipt.getAdBankAccount().getBaCoaGlCashAccount(),
                        EJBCommon.FALSE, arReceipt, branchCode, companyCode, details.getCompanyShortName());
            }

            // set receipt amount
            arReceipt.setRctAmount(TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT);

            this.executeArRctPost(arReceipt.getRctCode(), arReceipt.getRctLastModifiedBy(), branchCode, companyCode, details.getCompanyShortName());
            arReceipt.setRctApprovalStatus("N/A");

            return arReceipt.getRctCode();

        }
        catch (GlobalRecordAlreadyDeletedException |
               GlobalBranchAccountNumberInvalidException | GlobalJournalNotBalanceException |
               GlJREffectiveDatePeriodClosedException | GlJREffectiveDateNoPeriodExistException
               | GlobalTransactionAlreadyPostedException |
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

    private Integer getArGlCoaRevenueAccount(LocalArInvoiceLine arInvoiceLine, Integer branchCode, Integer companyCode, String companyShortName) {

        Debug.print("ArMiscReceiptEntryApiControllerBean getArGlCoaRevenueAccount");

        // generate revenue account
        try {

            StringBuilder GL_COA_ACCNT = new StringBuilder();
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode, companyShortName);
            LocalGenField genField = adCompany.getGenField();
            String FL_SGMNT_SPRTR = String.valueOf(genField.getFlSegmentSeparator());
            LocalAdBranchStandardMemoLine adBranchStandardMemoLine = null;

            try {

                adBranchStandardMemoLine = adBranchStandardMemoLineHome.findBSMLBySMLCodeAndBrCode(
                        arInvoiceLine.getArStandardMemoLine().getSmlCode(), branchCode, companyCode, companyShortName);

            }
            catch (FinderException ex) {

            }

            Collection arAutoAccountingSegments = arAutoAccountingSegmentHome.findByAaAccountType("REVENUE", companyCode, companyShortName);
            for (Object autoAccountingSegment : arAutoAccountingSegments) {
                LocalArAutoAccountingSegment arAutoAccountingSegment = (LocalArAutoAccountingSegment) autoAccountingSegment;
                LocalGlChartOfAccount glChartOfAccount = null;

                if (arAutoAccountingSegment.getAasClassType().equals("AR CUSTOMER")) {
                    glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(arInvoiceLine.getArReceipt().getArCustomer().getCstGlCoaRevenueAccount(), companyShortName);

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
                            glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchStandardMemoLine.getBsmlGlAccount(), companyShortName);
                        }
                        catch (FinderException ex) {
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
                LocalGlChartOfAccount glGeneratedChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(GL_COA_ACCNT.toString(), companyCode, companyShortName);
                return glGeneratedChartOfAccount.getCoaCode();
            }
            catch (FinderException ex) {
                if (adBranchStandardMemoLine != null) {
                    LocalGlChartOfAccount glChartOfAccount = null;
                    try {
                        glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchStandardMemoLine.getBsmlGlAccount(), companyShortName);
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

    private void bankAccountCardJournals(Integer branchCode, Integer companyCode, LocalArReceipt arReceipt,
                                         LocalAdBranchBankAccount adBranchBankAccount, LocalAdBranchBankAccount adBranchBankAccountCard1,
                                         LocalAdBranchBankAccount adBranchBankAccountCard2, LocalAdBranchBankAccount adBranchBankAccountCard3,
                                         String companyShortName)
            throws GlobalBranchAccountNumberInvalidException {

        if (arReceipt.getRctAmountCard1() > 0) {
            try {
                adBranchBankAccountCard1 = adBranchBankAccountHome.findBbaByBaCodeAndBrCode(
                        arReceipt.getAdBankAccountCard1().getBaCode(), branchCode, companyCode);
            }
            catch (FinderException ex) {
            }

            if (adBranchBankAccount != null) {
                this.addArDrEntry(arReceipt.getArDrNextLine(), "CARD 1", EJBCommon.TRUE,
                        arReceipt.getRctAmountCard1(), adBranchBankAccountCard1.getBbaGlCoaCashAccount(),
                        EJBCommon.FALSE, arReceipt, branchCode, companyCode, companyShortName);
            } else {

                this.addArDrEntry(arReceipt.getArDrNextLine(), "CARD 1", EJBCommon.TRUE,
                        arReceipt.getRctAmountCard1(),
                        arReceipt.getAdBankAccountCard1().getBaCoaGlCashAccount(), EJBCommon.FALSE, arReceipt,
                        branchCode, companyCode, companyShortName);
            }
        }

        if (arReceipt.getRctAmountCard2() > 0) {
            try {
                adBranchBankAccountCard2 = adBranchBankAccountHome.findBbaByBaCodeAndBrCode(
                        arReceipt.getAdBankAccountCard2().getBaCode(), branchCode, companyCode, companyShortName);
            }
            catch (FinderException ex) {
            }

            if (adBranchBankAccount != null) {
                this.addArDrEntry(arReceipt.getArDrNextLine(), "CARD 2", EJBCommon.TRUE,
                        arReceipt.getRctAmountCard2(), adBranchBankAccountCard2.getBbaGlCoaCashAccount(),
                        EJBCommon.FALSE, arReceipt, branchCode, companyCode, companyShortName);
            } else {
                this.addArDrEntry(arReceipt.getArDrNextLine(), "CARD 2", EJBCommon.TRUE,
                        arReceipt.getRctAmountCard2(),
                        arReceipt.getAdBankAccountCard2().getBaCoaGlCashAccount(), EJBCommon.FALSE, arReceipt,
                        branchCode, companyCode, companyShortName);
            }
        }

        if (arReceipt.getRctAmountCard3() > 0) {
            try {
                adBranchBankAccountCard3 = adBranchBankAccountHome.findBbaByBaCodeAndBrCode(
                        arReceipt.getAdBankAccountCard3().getBaCode(), branchCode, companyCode);
            }
            catch (FinderException ex) {
            }

            if (adBranchBankAccount != null) {
                this.addArDrEntry(arReceipt.getArDrNextLine(), "CARD 3", EJBCommon.TRUE,
                        arReceipt.getRctAmountCard3(), adBranchBankAccountCard3.getBbaGlCoaCashAccount(),
                        EJBCommon.FALSE, arReceipt, branchCode, companyCode, companyShortName);
            } else {
                this.addArDrEntry(arReceipt.getArDrNextLine(), "CARD 3", EJBCommon.TRUE,
                        arReceipt.getRctAmountCard3(),
                        arReceipt.getAdBankAccountCard3().getBaCoaGlCashAccount(), EJBCommon.FALSE, arReceipt,
                        branchCode, companyCode, companyShortName);
            }
        }
    }

    private void bankAccountJournals(Integer branchCode, Integer companyCode, LocalArReceipt arReceipt,
                                     double totalTaxAmount, double totalAmount, double totalDiscount,
                                     double withholdingTaxAmount, LocalAdBranchBankAccount adBranchBankAccount, String companyShortName)
            throws GlobalBranchAccountNumberInvalidException {

        if (adBranchBankAccount != null) {

            this.addArDrEntry(arReceipt.getArDrNextLine(), "CASH", EJBCommon.TRUE,
                    totalAmount + totalTaxAmount - withholdingTaxAmount - totalDiscount - arReceipt.getRctAmountVoucher()
                            - arReceipt.getRctAmountCheque() - arReceipt.getRctAmountCard1()
                            - arReceipt.getRctAmountCard2() - arReceipt.getRctAmountCard3(),
                    adBranchBankAccount.getBbaGlCoaCashAccount(), EJBCommon.FALSE, arReceipt, branchCode,
                    companyCode, companyShortName);

            if (arReceipt.getRctAmountCheque() > 0) {
                this.addArDrEntry(arReceipt.getArDrNextLine(), "CHEQUE", EJBCommon.TRUE,
                        arReceipt.getRctAmountCheque(), adBranchBankAccount.getBbaGlCoaCashAccount(),
                        EJBCommon.FALSE, arReceipt, branchCode, companyCode, companyShortName);
            }

            if (arReceipt.getRctAmountVoucher() > 0) {
                this.addArDrEntry(arReceipt.getArDrNextLine(), "VOUCHER", EJBCommon.TRUE,
                        arReceipt.getRctAmountVoucher(), adBranchBankAccount.getBbaGlCoaSalesDiscountAccount(),
                        EJBCommon.FALSE, arReceipt, branchCode, companyCode, companyShortName);
            }

        } else {

            this.addArDrEntry(arReceipt.getArDrNextLine(), "CASH", EJBCommon.TRUE,
                    totalAmount + totalTaxAmount - withholdingTaxAmount - totalDiscount - arReceipt.getRctAmountVoucher()
                            - arReceipt.getRctAmountCheque() - arReceipt.getRctAmountCard1()
                            - arReceipt.getRctAmountCard2() - arReceipt.getRctAmountCard3(),
                    arReceipt.getAdBankAccount().getBaCoaGlCashAccount(), EJBCommon.FALSE, arReceipt, branchCode,
                    companyCode, companyShortName);

            if (arReceipt.getRctAmountCheque() > 0) {
                this.addArDrEntry(arReceipt.getArDrNextLine(), "CHEQUE", EJBCommon.TRUE,
                        arReceipt.getRctAmountCheque(), arReceipt.getAdBankAccount().getBaCoaGlCashAccount(),
                        EJBCommon.FALSE, arReceipt, branchCode, companyCode, companyShortName);
            }

            if (arReceipt.getRctAmountVoucher() > 0) {
                this.addArDrEntry(arReceipt.getArDrNextLine(), "VOUCHER", EJBCommon.TRUE,
                        arReceipt.getRctAmountVoucher(), arReceipt.getAdBankAccount().getBaCoaGlSalesDiscount(),
                        EJBCommon.FALSE, arReceipt, branchCode, companyCode, companyShortName);
            }
        }
    }

    @Override
    public ReceiptApiResponse createMiscReceiptMemoLines(ReceiptRequest receiptRequest) {

        Debug.print("ArMiscReceiptEntryApiControllerBean createMiscReceiptMemoLines");

        ReceiptApiResponse apiResponse = new ReceiptApiResponse();
        LocalAdCompany adCompany;
        LocalAdBranch adBranch;
        LocalAdUser adUser;
        LocalArCustomer arCustomer;
        String currencyCode = receiptRequest.getCurrency();
        String paymentMethod = "CASH";
        String bankAccount;
        Date receiptDate = EJBCommon.convertStringToSQLDate(receiptRequest.getReceiptDate(), ConfigurationClass.DEFAULT_DATE_FORMAT);
        double applyAmount = 0d;

        try {
            ArReceiptDetails details = new ArReceiptDetails();

            // Company Code
            try {
                if (receiptRequest.getCompanyCode() == null || receiptRequest.getCompanyCode().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_041);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_041_MSG);
                    return apiResponse;
                }
                adCompany = adCompanyHome.findByCmpShortName(receiptRequest.getCompanyCode());
                details.setRctAdCompany(adCompany.getCmpCode());
                details.setCompanyShortName(adCompany.getCmpShortName());
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
                return apiResponse;
            }

            // Branch Code
            try {
                if (receiptRequest.getBranchCode() == null || receiptRequest.getBranchCode().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return apiResponse;
                }
                adBranch = adBranchHome.findByBrBranchCode(receiptRequest.getBranchCode(), adCompany.getCmpCode(), details.getCompanyShortName());
                details.setRctAdBranch(adBranch.getBrCode());
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_065);
                apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_065_MSG, receiptRequest.getBranchCode()));
                return apiResponse;
            }

            // User
            try {
                if (receiptRequest.getUsername() == null || receiptRequest.getUsername().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_042);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_042_MSG);
                    return apiResponse;
                }
                adUser = adUserHome.findByUsrName(receiptRequest.getUsername(), adCompany.getCmpCode(), details.getCompanyShortName());
                details.setRctCreatedBy(adUser.getUsrName());
                details.setRctLastModifiedBy(adUser.getUsrName());
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_002);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_002_MSG);
                return apiResponse;
            }

            // Customer Code
            try {
                if (receiptRequest.getCustomerCode() == null || receiptRequest.getCustomerCode().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return apiResponse;
                }
                arCustomer = arCustomerHome.findByCstCustomerCode(receiptRequest.getCustomerCode(), adCompany.getCmpCode(), details.getCompanyShortName());
            }
            catch (FinderException ex) {
                // Get error not exist customer
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_009);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_009_MSG);
                return apiResponse;
            }

            // Get Bank Account
            bankAccount = arCustomer.getAdBankAccount().getBaName();

            // Set receipt info
            details.setRctDate(receiptDate);
            details.setRctReferenceNumber(receiptRequest.getReferenceNumber());
            details.setRctVoid(EJBCommon.FALSE);
            details.setRctDescription(receiptRequest.getDescription());
            details.setRctPaymentMethod(paymentMethod);
            details.setRctCustomerName(arCustomer.getCstName());
            details.setRctConversionRate(1);
            details.setRctConversionDate(null);
            details.setRctEnableAdvancePayment(EJBCommon.FALSE);
            details.setRctCreatedBy(adUser.getUsrName());
            details.setRctDateCreated(new java.util.Date());
            details.setRctLastModifiedBy(adUser.getUsrName());
            details.setRctDateLastModified(new java.util.Date());

            String taxCode = "VAT INCLUSIVE";
            String wTaxCode = "NONE";

            ArrayList memoLines = new ArrayList();
            for (MemolineDetails memoLineDetails : receiptRequest.getMemoLineDetails()) {
                ArModInvoiceLineDetails lineDetails = new ArModInvoiceLineDetails();
                lineDetails.setIlSmlName(memoLineDetails.getMemoLineName());
                lineDetails.setIlQuantity(1); //TODO: Check if the quantity is needed
                lineDetails.setIlUnitPrice(memoLineDetails.getApplyAmount());
                lineDetails.setIlAmount(memoLineDetails.getApplyAmount());
                lineDetails.setIlTax(EJBCommon.TRUE); //TODO: Verify if the amount is automatically taxable
                memoLines.add(lineDetails);
                applyAmount+=memoLineDetails.getApplyAmount();
            }

            // Get total receipt amount
            details.setRctAmount(applyAmount);

            Integer receiptCode = this.saveArRctEntry(details, bankAccount,
                    taxCode, wTaxCode, currencyCode, arCustomer.getCstCustomerCode(), memoLines,
                    adBranch.getBrCode(), adCompany.getCmpCode());

            LocalArReceipt arReceipt = arReceiptHome.findByPrimaryKey(receiptCode, details.getCompanyShortName());

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            apiResponse.setDocumentNumber(arReceipt.getRctNumber());
            apiResponse.setStatus("Created receipt transaction successfully.");

        }
        catch (GlJREffectiveDateNoPeriodExistException ex) {

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_015);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_015_MSG);
            return apiResponse;
        }
        catch (GlJREffectiveDatePeriodClosedException ex) {

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_016);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_016_MSG);
            return apiResponse;

        }
        catch (GlobalJournalNotBalanceException ex) {

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_017);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_017_MSG);
            return apiResponse;

        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return apiResponse;
        }
        return apiResponse;
    }

    @Override
    public ReceiptApiResponse createMiscReceipt(ReceiptRequest receiptRequest) {

        Debug.print("ArMiscReceiptEntryApiControllerBean createMiscReceipt");

        ReceiptApiResponse apiResponse = new ReceiptApiResponse();
        ArrayList list = new ArrayList();
        LocalAdCompany adCompany = null;
        LocalAdBranch adBranch = null;
        LocalAdUser adUser = null;
        LocalArCustomer arCustomer = null;
        String currencyCode = receiptRequest.getCurrency();
        String paymentMethod = "CASH";
        String bankAccount = null;
        Date receiptDate = EJBCommon.convertStringToSQLDate(receiptRequest.getReceiptDate(), ConfigurationClass.DEFAULT_DATE_FORMAT);
        String defaultLocation = "DEFAULT";
        double applyAmount = 0;

        try {
            ArReceiptDetails details = new ArReceiptDetails();

            // Company Code
            try {
                if (receiptRequest.getCompanyCode() == null || receiptRequest.getCompanyCode().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_041);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_041_MSG);
                    return apiResponse;
                }
                adCompany = adCompanyHome.findByCmpShortName(receiptRequest.getCompanyCode());
                details.setRctAdCompany(adCompany.getCmpCode());
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
                return apiResponse;
            }

            // Branch Code
            try {
                if (receiptRequest.getBranchCode() == null || receiptRequest.getBranchCode().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return apiResponse;
                }
                adBranch = adBranchHome.findByBrBranchCode(receiptRequest.getBranchCode(), adCompany.getCmpCode());
                details.setRctAdBranch(adBranch.getBrCode());
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_065);
                apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_065_MSG, receiptRequest.getBranchCode()));
                return apiResponse;
            }

            // User
            try {
                if (receiptRequest.getUsername() == null || receiptRequest.getUsername().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_042);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_042_MSG);
                    return apiResponse;
                }
                adUser = adUserHome.findByUsrName(receiptRequest.getUsername(), adCompany.getCmpCode());
                details.setRctCreatedBy(adUser.getUsrName());
                details.setRctLastModifiedBy(adUser.getUsrName());
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_002);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_002_MSG);
                return apiResponse;
            }

            // Customer Code from TT
            try {
                if (receiptRequest.getCustomerCode() == null || receiptRequest.getCustomerCode().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return apiResponse;
                }
                arCustomer = arCustomerHome.findByCstRefCustomerCode(receiptRequest.getCustomerCode(), adBranch.getBrCode(), adCompany.getCmpCode());
                apiResponse.setErpCustomerCode(arCustomer.getCstCustomerCode());
            }
            catch (FinderException ex) {
                // Get error not exist customer
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_009);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_009_MSG);
                return apiResponse;
            }

            // Get Apply amount from the receiptRequest
            if (receiptRequest.getApplyAmount() == null && receiptRequest.getApplyAmount().equals("")) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_049);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_049_MSG);
                return apiResponse;
            }
            applyAmount = receiptRequest.getApplyAmount();

            // Get Credit Balance of Customer
            // NOTE: Credit Balance disabled. Receipt now need to full amount due of an invoice
            // creditBalance = this.getArRctCreditBalanceByCstCustomerCode(arCustomer.getCstCustomerCode(), adBranch.getBrCode(), adCompany.getCmpCode());

            // Get Bank Account
            bankAccount = arCustomer.getAdBankAccount().getBaName();

            // Set receipt info
            details.setRctDate(receiptDate);
            details.setRctReferenceNumber(receiptRequest.getReferenceNumber());
            details.setRctVoid(EJBCommon.FALSE);
            details.setRctDescription(receiptRequest.getDescription());
            details.setRctPaymentMethod(paymentMethod);
            details.setRctCustomerName(arCustomer.getCstName());
            details.setRctConversionRate(1);
            details.setRctConversionDate(null);
            details.setRctEnableAdvancePayment(EJBCommon.FALSE);
            details.setRctAmount(applyAmount);
            details.setRctCreatedBy(adUser.getUsrName());
            details.setRctDateCreated(new java.util.Date());
            details.setRctLastModifiedBy(adUser.getUsrName());
            details.setRctDateLastModified(new java.util.Date());

            String bankAccountCard1Name = "";
            String bankAccountCard2Name = "";
            String bankAccountCard3Name = "";

            String taxCode = "VAT INCLUSIVE";
            String wTaxCode = "NONE";

            double totalAmount = 0;
            ArrayList receiptLineItems = new ArrayList();
            int lineItem = 1;

            if (receiptRequest.getReceiptItems() == null || receiptRequest.getReceiptItems().size() == 0) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_061);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_061_MSG);
                return apiResponse;
            }

            for (LineItemRequest itemRequest : receiptRequest.getReceiptItems()) {
                ArModInvoiceLineItemDetails mdetails = new ArModInvoiceLineItemDetails();
                LocalInvItem invItem = null;

                // itemName
                try {
                    if (itemRequest.getItemName() == null || itemRequest.getItemName().equals("")) {
                        apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_045);
                        apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_045_MSG);
                        return apiResponse;
                    }
                    invItem = invItemHome.findByIiName(itemRequest.getItemName(), adCompany.getCmpCode());
                    mdetails.setIliIiName(invItem.getIiName());
                    mdetails.setIliIiDescription(invItem.getIiDescription());
                }
                catch (FinderException ex) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_010);
                    apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_010_MSG, itemRequest.getItemName()));
                    return apiResponse;
                }

                // itemLocation
                mdetails.setIliLocName(defaultLocation);

                // confirm if item is in location
                try {
                    invItemLocationHome.findByIiNameAndLocNameAndAdBranch(itemRequest.getItemName(), defaultLocation,
                            adBranch.getBrCode(), adCompany.getCmpCode());
                }
                catch (FinderException ex) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_011);
                    apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_011_MSG, itemRequest.getItemName()));
                    return apiResponse;
                }

                mdetails.setIliTax(EJBCommon.TRUE);
                mdetails.setIliUomName(invItem.getInvUnitOfMeasure().getUomName());
                mdetails.setIliQuantity(itemRequest.getItemQuantity());

                // Calculate the Invoice Amount based on the item invoiceDetails saved in OFS
                mdetails.setIliUnitPrice(itemRequest.getItemSalesPrice());
                mdetails.setIliAmount(itemRequest.getItemSalesPrice() * itemRequest.getItemQuantity());

                // Additional requirements from TT
                mdetails.setIliTotalDiscount(itemRequest.getNormalDiscAmt());
                mdetails.setIliDiscount1(itemRequest.getTransDiscAmt());
                mdetails.setIliTaxAmount(itemRequest.getTaxAmt());

                // Update Item Sales Price and Percent Markup
                if (invItem.getIiUnitCost() > 0) {
                    updateSalesPrice(itemRequest, invItem);
                } else {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_031);
                    apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_031_MSG, lineItem));
                    return apiResponse;
                }
                totalAmount += mdetails.getIliAmount();
                receiptLineItems.add(mdetails);
            }

            Integer receiptCode = this.saveArRctIliEntry(details, bankAccount,
                    bankAccountCard1Name, bankAccountCard2Name, bankAccountCard3Name,
                    taxCode, wTaxCode, currencyCode, arCustomer.getCstCustomerCode(), receiptLineItems,
                    adBranch.getBrCode(), adCompany.getCmpCode());

            LocalArReceipt arReceipt = arReceiptHome.findByPrimaryKey(receiptCode);

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            apiResponse.setDocumentNumber(arReceipt.getRctNumber());
            apiResponse.setStatus("Created receipt transaction successfully.");

        }
        catch (GlJREffectiveDateNoPeriodExistException ex) {

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_015);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_015_MSG);
            return apiResponse;
        }
        catch (GlJREffectiveDatePeriodClosedException ex) {

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_016);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_016_MSG);
            return apiResponse;

        }
        catch (GlobalJournalNotBalanceException ex) {

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_017);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_017_MSG);
            return apiResponse;

        }
        catch (Exception ex) {
            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return apiResponse;
        }
        return apiResponse;
    }

    private LocalArInvoiceLine addArIlEntry(ArModInvoiceLineDetails mdetails, LocalArReceipt arReceipt, Integer companyCode, String companyShortName) {

        Debug.print("ArMiscReceiptEntryApiControllerBean addArIlEntry");

        try {

            short precisionUnit = this.getGlFcPrecisionUnit(companyCode, companyShortName);
            double IL_AMNT = 0d;
            double IL_TAX_AMNT = 0d;

            if (mdetails.getIlTax() == EJBCommon.TRUE) {

                // calculate net amount
                LocalArTaxCode arTaxCode = arReceipt.getArTaxCode();
                if (arTaxCode.getTcType().equals("INCLUSIVE")) {
                    IL_AMNT = EJBCommon.roundIt(mdetails.getIlAmount() / (1 + (arTaxCode.getTcRate() / 100)), precisionUnit);
                } else {
                    IL_AMNT = mdetails.getIlAmount();
                }

                // calculate tax
                if (!arTaxCode.getTcType().equals("NONE") && !arTaxCode.getTcType().equals("EXEMPT")) {
                    if (arTaxCode.getTcType().equals("INCLUSIVE")) {
                        IL_TAX_AMNT = EJBCommon.roundIt(mdetails.getIlAmount() - IL_AMNT, precisionUnit);
                    } else if (arTaxCode.getTcType().equals("EXCLUSIVE")) {
                        IL_TAX_AMNT = EJBCommon.roundIt(mdetails.getIlAmount() * arTaxCode.getTcRate() / 100, precisionUnit);
                    }
                }

            } else {
                IL_AMNT = mdetails.getIlAmount();
            }

            LocalArInvoiceLine arInvoiceLine = arInvoiceLineHome
                    .IlDescription(mdetails.getIlDescription())
                    .IlQuantity(mdetails.getIlQuantity())
                    .IlUnitPrice(mdetails.getIlUnitPrice())
                    .IlAmount(IL_AMNT)
                    .IlTaxAmount(IL_TAX_AMNT)
                    .IlTax(mdetails.getIlTax())
                    .IlAdCompany(companyCode)
                    .buildInvoiceLine(companyShortName);
            arInvoiceLine.setArReceipt(arReceipt);

            LocalArStandardMemoLine arStandardMemoLine = arStandardMemoLineHome.findBySmlName(mdetails.getIlSmlName(), companyCode, companyShortName);
            arInvoiceLine.setArStandardMemoLine(arStandardMemoLine);

            return arInvoiceLine;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void setBankAccountCards(String bankAccountCard1Name, String bankAccountCard2Name, String bankAccountCard3Name,
                                     Integer companyCode, LocalArReceipt arReceipt, String companyShortName)
            throws FinderException {

        if (!bankAccountCard1Name.equals("") && arReceipt.getRctAmountCard1() > 0) {
            LocalAdBankAccount adBankAccountCard1 = adBankAccountHome.findByBaName(bankAccountCard1Name, companyCode, companyShortName);
            arReceipt.setAdBankAccountCard1(adBankAccountCard1);
        }
        try {
            LocalAdBankAccount adBankAccountCard2 = adBankAccountHome.findByBaName(bankAccountCard2Name, companyCode, companyShortName);
            arReceipt.setAdBankAccountCard2(adBankAccountCard2);
        }
        catch (Exception ex) {
        }
        try {
            LocalAdBankAccount adBankAccountCard3 = adBankAccountHome.findByBaName(bankAccountCard3Name, companyCode, companyShortName);
            arReceipt.setAdBankAccountCard3(adBankAccountCard3);
        }
        catch (Exception ex) {
        }
    }

    private void isReceiptPostedOrVoid(LocalArReceipt arReceipt)
            throws GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException {

        if (arReceipt.getRctPosted() == EJBCommon.TRUE) {
            throw new GlobalTransactionAlreadyPostedException();
        } else if (arReceipt.getRctVoid() == EJBCommon.TRUE) {
            throw new GlobalTransactionAlreadyVoidException();
        }
    }

    private void updateSalesPrice(LineItemRequest itemRequest, LocalInvItem invItem) {

        Debug.print("ArMiscReceiptEntryApiControllerBean updateSalesPrice");
        double percentMarkup = ((itemRequest.getItemSalesPrice() - invItem.getIiUnitCost()) / invItem.getIiUnitCost())
                * 100;
        invItem.setIiSalesPrice(itemRequest.getItemSalesPrice());
        invItem.setIiPercentMarkup(percentMarkup);
        invItemHome.updateItem(invItem);
    }

    private void executeArRctPost(Integer RCT_CODE, String USR_NM, Integer branchCode, Integer companyCode, String companyShortName)
            throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException,
            GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException,
            AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException {

        Debug.print("ArMiscReceiptEntryApiControllerBean executeArRctPost");

        LocalArReceipt arReceipt;

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode, companyShortName);
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode, companyShortName);

            // validate if receipt is already deleted
            try {
                arReceipt = arReceiptHome.findByPrimaryKey(RCT_CODE, companyShortName);
            }
            catch (FinderException ex) {
                throw new GlobalRecordAlreadyDeletedException();
            }

            isReceiptPostedOrVoid(arReceipt);

            // post receipt
            if (arReceipt.getRctVoid() == EJBCommon.FALSE && arReceipt.getRctPosted() == EJBCommon.FALSE) {
                if (arReceipt.getRctType().equals("MISC") && !arReceipt.getArInvoiceLineItems().isEmpty()) {
                    for (Object o : arReceipt.getArInvoiceLineItems()) {
                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) o;
                        String itemName = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName();
                        String locationName = arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName();
                        double quantitySold = this.convertByUomFromAndItemAndQuantity(
                                arInvoiceLineItem.getInvUnitOfMeasure(),
                                arInvoiceLineItem.getInvItemLocation().getInvItem(),
                                arInvoiceLineItem.getIliQuantity(),
                                companyCode, adCompany.getCmpShortName());

                        LocalInvCosting invCosting = invCostingHome.getItemAverageCost(arReceipt.getRctDate(), itemName,
                                locationName, branchCode, companyCode, adCompany.getCmpShortName());
                        double COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                        if (invCosting == null) {
                            this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), quantitySold, COST * quantitySold, -quantitySold,
                                    -COST * quantitySold, 0d, null, branchCode, companyCode, adCompany.getCmpShortName());
                        } else {
                            switch (invCosting.getInvItemLocation().getInvItem().getIiCostMethod()) {
                                case "Average":
                                    double avgCost = invCosting.getCstRemainingQuantity() <= 0 ? COST : Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                                    this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), quantitySold, avgCost * quantitySold,
                                            invCosting.getCstRemainingQuantity() - quantitySold,
                                            invCosting.getCstRemainingValue() - (avgCost * quantitySold), 0d, null, branchCode,
                                            companyCode, adCompany.getCmpShortName());
                                    break;
                                case "Standard":
                                    double standardCost = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                                    this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), quantitySold,
                                            standardCost * quantitySold, invCosting.getCstRemainingQuantity() - quantitySold,
                                            invCosting.getCstRemainingValue() - (standardCost * quantitySold), 0d, null,
                                            branchCode, companyCode, adCompany.getCmpShortName());
                                    break;
                            }
                        }
                    }
                }

                increaseAccountBalances(companyCode, arReceipt, adCompany.getCmpShortName());

                // set receipt post status
                arReceipt.setRctPosted(EJBCommon.TRUE);
                arReceipt.setRctPostedBy(USR_NM);
                arReceipt.setRctDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            }

            // post to gl if necessary
            if (adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                // validate if date has no period and period is closed
                LocalGlSetOfBook glJournalSetOfBook;

                try {
                    glJournalSetOfBook = glSetOfBookHome.findByDate(arReceipt.getRctDate(), companyCode);
                }
                catch (FinderException ex) {
                    throw new GlJREffectiveDateNoPeriodExistException();
                }

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(
                        glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), arReceipt.getRctDate(), companyCode);

                if (glAccountingCalendarValue.getAcvStatus() == 'N' ||
                        glAccountingCalendarValue.getAcvStatus() == 'C' ||
                        glAccountingCalendarValue.getAcvStatus() == 'P') {
                    throw new GlJREffectiveDatePeriodClosedException();
                }

                // check if invoice is balance if not check suspense posting
                LocalGlJournalLine glOffsetJournalLine = null;
                Collection arDistributionRecords = arDistributionRecordHome.findImportableDrByDrReversedAndRctCode(
                        (arReceipt.getRctVoid() == EJBCommon.FALSE ? EJBCommon.FALSE : EJBCommon.TRUE), arReceipt.getRctCode(), companyCode);

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
                        DR_AMNT = this.convertForeignToFunctionalCurrency(
                                arInvoice.getGlFunctionalCurrency().getFcCode(),
                                arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(),
                                arInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), companyCode, adCompany.getCmpShortName());
                    } else {
                        DR_AMNT = this.convertForeignToFunctionalCurrency(
                                arReceipt.getGlFunctionalCurrency().getFcCode(),
                                arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(),
                                arReceipt.getRctConversionRate(), arDistributionRecord.getDrAmount(), companyCode, adCompany.getCmpShortName());
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
                    LocalGlSuspenseAccount glSuspenseAccount;
                    try {
                        glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("ACCOUNTS RECEIVABLES", "SALES RECEIPTS", companyCode);
                    }
                    catch (FinderException ex) {
                        throw new GlobalJournalNotBalanceException();
                    }

                    if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {
                        glOffsetJournalLine = glJournalLineHome.create((short) (arDistributionRecords.size() + 1),
                                EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);
                    } else {

                        glOffsetJournalLine = glJournalLineHome.create((short) (arDistributionRecords.size() + 1),
                                EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
                    }

                    LocalGlChartOfAccount glChartOfAccount = glSuspenseAccount.getGlChartOfAccount();
                    glOffsetJournalLine.setGlChartOfAccount(glChartOfAccount);

                } else if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE && TOTAL_DEBIT != TOTAL_CREDIT) {
                    throw new GlobalJournalNotBalanceException();
                }

                // create journal batch if necessary
                LocalGlJournalBatch glJournalBatch = null;
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");

                try {
                    if (adPreference.getPrfEnableArReceiptBatch() == EJBCommon.TRUE) {
                        glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " " + arReceipt.getArReceiptBatch().getRbName(), branchCode, companyCode);
                    } else {
                        glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " SALES RECEIPTS", branchCode, companyCode);
                    }

                }
                catch (FinderException ex) {
                }

                if (adPreference.getPrfEnableGlJournalBatch() == EJBCommon.TRUE && glJournalBatch == null) {
                    if (adPreference.getPrfEnableArReceiptBatch() == EJBCommon.TRUE) {
                        glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " " + arReceipt.getArReceiptBatch().getRbName(),
                                "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, branchCode, companyCode);
                    } else {

                        glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " SALES RECEIPTS", "JOURNAL IMPORT",
                                "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, branchCode, companyCode);
                    }
                }

                // create journal entry
                String customerName = arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName();

                LocalGlJournal glJournal = glJournalHome.create(arReceipt.getRctReferenceNumber(),
                        arReceipt.getRctDescription(), arReceipt.getRctDate(), 0.0d, null, arReceipt.getRctNumber(),
                        null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, USR_NM, new Date(), USR_NM,
                        new Date(), null, null, USR_NM, EJBCommon.getGcCurrentDateWoTime().getTime(),
                        arReceipt.getArCustomer().getCstTin(), customerName, EJBCommon.FALSE, null, branchCode, companyCode);

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
                        DR_AMNT = this.convertForeignToFunctionalCurrency(
                                arInvoice.getGlFunctionalCurrency().getFcCode(),
                                arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(),
                                arInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), companyCode, adCompany.getCmpShortName());
                    } else {
                        DR_AMNT = this.convertForeignToFunctionalCurrency(
                                arReceipt.getGlFunctionalCurrency().getFcCode(),
                                arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(),
                                arReceipt.getRctConversionRate(), arDistributionRecord.getDrAmount(), companyCode, adCompany.getCmpShortName());
                    }

                    LocalGlJournalLine glJournalLine = glJournalLineHome.create(arDistributionRecord.getDrLine(),
                            arDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);
                    glJournalLine.setGlChartOfAccount(arDistributionRecord.getGlChartOfAccount());
                    glJournalLine.setGlJournal(glJournal);
                    arDistributionRecord.setDrImported(EJBCommon.TRUE);

                    // for FOREX revaluation
                    int FC_CODE = arDistributionRecord.getArAppliedInvoice() != null ? arInvoice.getGlFunctionalCurrency().getFcCode() : arReceipt.getGlFunctionalCurrency().getFcCode();

                    if ((FC_CODE != adCompany.getGlFunctionalCurrency().getFcCode()) &&
                            glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null &&
                            (FC_CODE == glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode())) {

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
                        }
                        catch (FinderException ex) {
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

                        glForexLedger.setGlChartOfAccount(glJournalLine.getGlChartOfAccount());

                        // propagate balances
                        try {
                            glForexLedgers = glForexLedgerHome.findByGreaterThanFrlDateAndCoaCode(
                                    glForexLedger.getFrlDate(), glForexLedger.getGlChartOfAccount().getCoaCode(),
                                    glForexLedger.getFrlAdCompany());

                        }
                        catch (FinderException ex) {

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
                    glOffsetJournalLine.setGlJournal(glJournal);
                }

                // post journal to gl
                Collection glJournalLines = glJournalLineHome.findByJrCode(glJournal.getJrCode(), companyCode);
                for (Object journalLine : glJournalLines) {

                    LocalGlJournalLine glJournalLine = (LocalGlJournalLine) journalLine;

                    // post current to current acv
                    this.postToGl(glAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), true,
                            glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                    // post to subsequent acvs (propagate)
                    Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome
                            .findSubsequentAcvByAcCodeAndAcvPeriodNumber(
                                    glJournalSetOfBook.getGlAccountingCalendar().getAcCode(),
                                    glAccountingCalendarValue.getAcvPeriodNumber(), companyCode);

                    for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {

                        LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;

                        this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false,
                                glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
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
                            Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(
                                    glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                            for (Object accountingCalendarValue : glAccountingCalendarValues) {

                                LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                                if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY") || ACCOUNT_TYPE.equals("OWNERS EQUITY")) {
                                    this.postToGl(glSubsequentAccountingCalendarValue,
                                            glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(),
                                            glJournalLine.getJlAmount(), companyCode);
                                } else {
                                    // revenue & expense
                                    this.postToGl(glSubsequentAccountingCalendarValue, glRetainedEarningsAccount, false,
                                            glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                                }
                            }
                            if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) {
                                break;
                            }
                        }
                    }
                }
            }

        }
        catch (GlJREffectiveDateNoPeriodExistException | GlobalExpiryDateNotFoundException |
               GlobalTransactionAlreadyPostedException |
               GlobalRecordAlreadyDeletedException | GlobalJournalNotBalanceException |
               GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (AdPRFCoaGlVarianceAccountNotFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void increaseAccountBalances(Integer companyCode, LocalArReceipt arReceipt, String companyShortName) throws FinderException {
        // increase bank balance CASH
        if (arReceipt.getRctAmountCash() > 0) {
            increaseBankAccountBalance(companyCode, arReceipt, arReceipt.getAdBankAccount().getBaCode(), arReceipt.getRctAmountCash(), companyShortName);
        }

        // increase bank balance CARD 1
        if (arReceipt.getRctAmountCard1() > 0) {
            increaseBankAccountBalance(companyCode, arReceipt, arReceipt.getAdBankAccountCard1().getBaCode(), arReceipt.getRctAmountCard1(), companyShortName);
        }

        // increase bank balance CARD 2
        if (arReceipt.getRctAmountCard2() > 0) {
            increaseBankAccountBalance(companyCode, arReceipt, arReceipt.getAdBankAccountCard2().getBaCode(), arReceipt.getRctAmountCard2(), companyShortName);
        }

        // increase bank balance CARD 3
        if (arReceipt.getRctAmountCard3() > 0) {
            increaseBankAccountBalance(companyCode, arReceipt, arReceipt.getAdBankAccountCard3().getBaCode(), arReceipt.getRctAmountCard3(), companyShortName);
        }

        // increase bank balance CHEQUE
        if (arReceipt.getRctAmountCheque() > 0) {
            increaseBankAccountBalance(companyCode, arReceipt, arReceipt.getAdBankAccount().getBaCode(), arReceipt.getRctAmountCheque(), companyShortName);
        }
    }

    private void increaseBankAccountBalance(Integer companyCode, LocalArReceipt arReceipt,
                                            Integer bankAccountCode, double cardAmount, String companyShortName) throws FinderException {

        LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(bankAccountCode, companyShortName);
        try {
            // find bank account balance before or equal receipt date
            Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(
                    arReceipt.getRctDate(), bankAccountCode, "BOOK", companyCode, companyShortName);

            if (!adBankAccountBalances.isEmpty()) {
                // get last check
                ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);
                LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {
                    // create new balance
                    LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome
                            .BabDate(arReceipt.getRctDate())
                            .BabBalance(adBankAccountBalance.getBabBalance() + cardAmount)
                            .BabType("BOOK")
                            .BabAdCompany(companyCode)
                            .buildBankAccountBalance(companyShortName);

                    // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                    adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                } else {
                    // equals to check date
                    adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard1());
                }
            } else {
                // create new balance
                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome
                        .BabDate(arReceipt.getRctDate())
                        .BabBalance(cardAmount)
                        .BabType("BOOK")
                        .BabAdCompany(companyCode)
                        .buildBankAccountBalance(companyShortName);
                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
            }

            // propagate to subsequent balances if necessary
            adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(
                    arReceipt.getRctDate(), bankAccountCode, "BOOK", companyCode, companyShortName);

            for (Object bankAccountBalance : adBankAccountBalances) {
                LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;
                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + cardAmount);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void postToInv(LocalArInvoiceLineItem arInvoiceLineItem, Date CST_DT, double CST_QTY_SLD,
                           double CST_CST_OF_SLS, double CST_RMNNG_QTY, double CST_RMNNG_VL, double CST_VRNC_VL, String USR_NM,
                           Integer branchCode, Integer companyCode, String companyShortName)
            throws AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException {

        Debug.print("ArMiscReceiptEntryApiControllerBean postToInv");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode, companyShortName);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode, companyShortName);
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
                LocalInvCosting invCurrentCosting = invCostingHome
                        .getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(),
                                invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(),
                                branchCode, companyCode, companyShortName);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            }
            catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            // void subsequent cost variance adjustments
            Collection invAdjustmentLines = invAdjustmentLineHome
                    .findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(CST_DT,
                            invItemLocation.getIlCode(), branchCode, companyCode, companyShortName);
            Iterator i = invAdjustmentLines.iterator();

            while (i.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();
                this.voidInvAdjustment(invAdjustmentLine.getInvAdjustment(), branchCode, companyCode);
            }

            String prevExpiryDates = "";
            String miscListPrpgt = "";
            double qtyPrpgt = 0;
            try {
                LocalInvCosting prevCst = invCostingHome
                        .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndRemainingQuantityNotEqualToZeroAndIlCode(
                                CST_DT, invItemLocation.getIlCode(), branchCode, companyCode);
                Debug.print("ArReceiptPostControllerBean postToInv B");
                prevExpiryDates = prevCst.getCstExpiryDate();
                qtyPrpgt = prevCst.getCstRemainingQuantity();

                if (prevExpiryDates == null) {
                    prevExpiryDates = "";
                }

            }
            catch (Exception ex) {
                prevExpiryDates = "";
            }

            // create costing
            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d,
                    0d, 0d, CST_QTY_SLD, CST_CST_OF_SLS, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, 0d, branchCode, companyCode);
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setArInvoiceLineItem(arInvoiceLineItem);

            invCosting.setCstQuantity(CST_QTY_SLD * -1);
            invCosting.setCstCost(CST_CST_OF_SLS * -1);

            // Get Latest Expiry Dates
            if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiTraceMisc() != 0) {
                if (prevExpiryDates != null && prevExpiryDates != "" && prevExpiryDates.length() != 0) {
                    if (arInvoiceLineItem.getIliMisc() != null && arInvoiceLineItem.getIliMisc() != ""
                            && arInvoiceLineItem.getIliMisc().length() != 0) {
                        int qty2Prpgt = Integer.parseInt(this.getQuantityExpiryDates(arInvoiceLineItem.getIliMisc()));

                        String miscList2Prpgt = this.propagateExpiryDates(arInvoiceLineItem.getIliMisc(), qty2Prpgt,
                                "False");
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
                                if (!miscStr2.equals(miscStr) || a > 1) {
                                    if ((ret2 != "1st") && ((ret2 == "false") || (ret2 == "true"))) {
                                        if (miscStr2 != "") {
                                            miscStr2 = "$" + miscStr2;
                                            ret.append(miscStr2);
                                            ret2 = "false";
                                        }
                                    }
                                }
                            }
                            if (ret.toString() != "") {
                                ret.append("$");
                            }
                            exp.append(ret);
                            qtyPrpgt = qtyPrpgt - 1;
                        }
                        propagateMiscPrpgt = ret.toString();
                        if (Checker == "true") {
                        } else {
                            throw new GlobalExpiryDateNotFoundException(
                                    arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                        }
                        invCosting.setCstExpiryDate(propagateMiscPrpgt);
                    } else {
                        invCosting.setCstExpiryDate(prevExpiryDates);
                    }

                } else {
                    if (arInvoiceLineItem.getIliMisc() != null && arInvoiceLineItem.getIliMisc() != ""
                            && arInvoiceLineItem.getIliMisc().length() != 0) {
                        int initialQty = Integer.parseInt(this.getQuantityExpiryDates(arInvoiceLineItem.getIliMisc()));
                        String initialPrpgt = this.propagateExpiryDates(arInvoiceLineItem.getIliMisc(), initialQty,
                                "False");

                        invCosting.setCstExpiryDate(initialPrpgt);
                    } else {
                        invCosting.setCstExpiryDate(prevExpiryDates);
                    }
                }
            }

            // if cost variance is not 0, generate cost variance for the transaction
            if (CST_VRNC_VL != 0) {

                this.generateCostVariance(invCosting.getInvItemLocation(), CST_VRNC_VL,
                        "APMR" + arInvoiceLineItem.getArReceipt().getRctNumber(),
                        arInvoiceLineItem.getArReceipt().getRctDescription(),
                        arInvoiceLineItem.getArReceipt().getRctDate(), USR_NM, branchCode, companyCode);
            }

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT,
                    invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode,
                    companyCode);
            String miscList = "";
            ArrayList miscList2 = null;
            i = invCostings.iterator();
            String propagateMisc = "";
            StringBuilder ret = new StringBuilder();
            while (i.hasNext()) {
                String Checker = "";
                String Checker2 = "";

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) i.next();

                invPropagatedCosting
                        .setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() - CST_QTY_SLD);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() - CST_CST_OF_SLS);

                if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiTraceMisc() != 0) {
                    if (arInvoiceLineItem.getIliMisc() != null && arInvoiceLineItem.getIliMisc() != "" && arInvoiceLineItem.getIliMisc().length() != 0) {

                        double qty = Double.parseDouble(this.getQuantityExpiryDates(arInvoiceLineItem.getIliMisc()));
                        miscList = this.propagateExpiryDates(arInvoiceLineItem.getIliMisc(), qty, "False");
                        miscList2 = this.expiryDates(arInvoiceLineItem.getIliMisc(), qty);

                        if (arInvoiceLineItem.getIliQuantity() < 0) {
                            Iterator mi = miscList2.iterator();

                            propagateMisc = invPropagatedCosting.getCstExpiryDate();
                            ret = new StringBuilder(invPropagatedCosting.getCstExpiryDate());
                            while (mi.hasNext()) {
                                String miscStr = (String) mi.next();

                                int qTest = checkExpiryDates(ret + "fin$");
                                ArrayList miscList3 = this.expiryDates("$" + ret, Double.parseDouble(Integer.toString(qTest)));
                                Iterator m2 = miscList3.iterator();
                                ret = new StringBuilder();
                                String ret2 = "false";
                                int a = 0;
                                while (m2.hasNext()) {
                                    String miscStr2 = (String) m2.next();

                                    if (ret2 == "1st") {
                                        ret2 = "false";
                                    }

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
                                    throw new GlobalExpiryDateNotFoundException(
                                            arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                                }

                                ret.append("$");
                                qtyPrpgt = qtyPrpgt - 1;
                            }
                        }
                    }

                    if (arInvoiceLineItem.getIliMisc() != null && arInvoiceLineItem.getIliMisc() != ""
                            && arInvoiceLineItem.getIliMisc().length() != 0) {
                        if (prevExpiryDates != null && prevExpiryDates != "" && prevExpiryDates.length() != 0) {

                            Iterator mi = miscList2.iterator();

                            propagateMisc = invPropagatedCosting.getCstExpiryDate();
                            ret = new StringBuilder(propagateMisc);
                            while (mi.hasNext()) {
                                String miscStr = (String) mi.next();

                                int qTest = checkExpiryDates(ret + "fin$");
                                ArrayList miscList3 = this.expiryDates("$" + ret, Double.parseDouble(Integer.toString(qTest)));
                                Iterator m2 = miscList3.iterator();
                                ret = new StringBuilder();
                                String ret2 = "false";
                                int a = 0;
                                while (m2.hasNext()) {
                                    String miscStr2 = (String) m2.next();

                                    if (ret2 == "1st") {
                                        ret2 = "false";
                                    }

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
                            propagateMisc = miscList + invPropagatedCosting.getCstExpiryDate().substring(1
                            );
                        }
                        catch (Exception e) {
                            propagateMisc = miscList;
                        }
                    }
                    invPropagatedCosting.setCstExpiryDate(propagateMisc);
                }
            }

            if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                // regenerate cost variance
                this.regenerateCostVariance(invCostings, invCosting, branchCode, companyCode);
            }

        }
        catch (AdPRFCoaGlVarianceAccountNotFoundException | GlobalExpiryDateNotFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void voidInvAdjustment(LocalInvAdjustment invAdjustment, Integer branchCode, Integer companyCode) {

        Debug.print("ArMiscReceiptEntryApiControllerBean voidInvAdjustment");

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

                this.addInvDrEntry(invAdjustment.getInvDrNextLine(), invDistributionRecord.getDrClass(),
                        invDistributionRecord.getDrDebit() == EJBCommon.TRUE ? EJBCommon.FALSE : EJBCommon.TRUE,
                        invDistributionRecord.getDrAmount(), EJBCommon.TRUE,
                        invDistributionRecord.getInvChartOfAccount().getCoaCode(), invAdjustment, branchCode, companyCode);
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

                this.addInvAlEntry(invAdjustmentLine.getInvItemLocation(), invAdjustment,
                        (invAdjustmentLine.getAlUnitCost()) * -1, EJBCommon.TRUE, companyCode);
            }

            invAdjustment.setAdjVoid(EJBCommon.TRUE);

            this.executeInvAdjPost(invAdjustment.getAdjCode(), invAdjustment.getAdjLastModifiedBy(), branchCode,
                    companyCode);

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void receiptNumberValidation(ArReceiptDetails details, Integer branchCode, Integer companyCode, LocalArReceipt arReceipt)
            throws GlobalDocumentNumberNotUniqueException, ArREDuplicatePayfileReferenceNumberException {

        Debug.print("ArMiscReceiptEntryApiControllerBean receiptNumberValidation");

        LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

        if (details.getRctCode() == null) {
            String documentType = details.getRctDocumentType();
            try {
                if (documentType != null) {
                    adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName(documentType, companyCode, details.getCompanyShortName());
                } else {
                    documentType = "AR RECEIPT";
                }
            }
            catch (FinderException ex) {
                documentType = "AR RECEIPT";
            }

            try {
                adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName(documentType, companyCode, details.getCompanyShortName());
                adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(
                        adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode, details.getCompanyShortName());
            }
            catch (FinderException ex) {
            }

            LocalArReceipt arExistingReceipt = null;
            try {
                arExistingReceipt = arReceiptHome.findByRctNumberAndBrCode(details.getRctNumber(), branchCode, companyCode, details.getCompanyShortName());
            }
            catch (FinderException ex) {
            }

            if (arExistingReceipt != null) {
                throw new GlobalDocumentNumberNotUniqueException();
            }

            if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' &&
                    (details.getRctNumber() == null || details.getRctNumber().trim().length() == 0)) {
                while (true) {
                    if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {
                        try {
                            arReceiptHome.findByRctNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), branchCode, companyCode, details.getCompanyShortName());
                            adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                        }
                        catch (FinderException ex) {
                            details.setRctNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                            adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                            break;
                        }
                    } else {
                        try {
                            arReceiptHome.findByRctNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), branchCode, companyCode, details.getCompanyShortName());
                            adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                        }
                        catch (FinderException ex) {
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
                arExistingReceipt = arReceiptHome.findByRctNumberAndBrCode(details.getRctNumber(), branchCode, companyCode, details.getCompanyShortName());
            }
            catch (FinderException ex) {
            }
            if (arExistingReceipt != null && !arExistingReceipt.getRctCode().equals(details.getRctCode())) {
                throw new GlobalDocumentNumberNotUniqueException();
            }

            if (arReceipt.getRctNumber() != details.getRctNumber() && (details.getRctNumber() == null || details.getRctNumber().trim().length() == 0)) {
                details.setRctNumber(arReceipt.getRctNumber());
            }
        }

        // validate if payfile Reference Number exist
        if (details.getRctPayfileReferenceNumber() != null) {
            if (!details.getRctPayfileReferenceNumber().equals("")) {
                try {
                    arReceiptHome.findByPayfileReferenceNumberAndCompanyCode(details.getRctPayfileReferenceNumber(), companyCode, details.getCompanyShortName());
                    throw new ArREDuplicatePayfileReferenceNumberException();
                }
                catch (FinderException ex) {
                }
            }
        }
    }

    private void checkConversionDateValidation(ArReceiptDetails details, String foreignCurrency, Integer companyCode)
            throws GlobalConversionDateNotExistException {

        Debug.print("ArMiscReceiptEntryApiControllerBean checkConversionDateValidation");
        try {

            if (details.getRctConversionDate() != null) {
                LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(foreignCurrency, companyCode, details.getCompanyShortName());
                LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode, details.getCompanyShortName());
                if (!glValidateFunctionalCurrency.getFcName().equals("USD")) {
                    LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(
                            glValidateFunctionalCurrency.getFcCode(), details.getRctConversionDate(), companyCode, details.getCompanyShortName());
                } else if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {
                    LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(
                            adCompany.getGlFunctionalCurrency().getFcCode(), details.getRctConversionDate(), companyCode, details.getCompanyShortName());
                }
            }
        }
        catch (FinderException ex) {
            throw new GlobalConversionDateNotExistException();
        }
    }

    private LocalInvItemLocation getItemLocation(Integer companyCode, ArModInvoiceLineItemDetails lineDetails)
            throws GlobalInvItemLocationNotFoundException {

        Debug.print("ArMiscReceiptEntryApiControllerBean getItemLocation");

        LocalInvItemLocation itemLocation;
        try {
            itemLocation = invItemLocationHome.findByLocNameAndIiName(lineDetails.getIliLocName(), lineDetails.getIliIiName(), companyCode);
        }
        catch (FinderException ex) {
            throw new GlobalInvItemLocationNotFoundException(String.valueOf(lineDetails.getIliLine()));
        }
        return itemLocation;
    }

    private void isPriorDateAllowed(Integer companyCode, Integer branchCode, LocalArReceipt arReceipt, LocalAdPreference adPreference, LocalInvItemLocation itemLocation)
            throws FinderException, GlobalInventoryDateException {

        Debug.print("ArMiscReceiptEntryApiControllerBean isPriorDateAllowed");

        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(
                arReceipt.getRctDate(), itemLocation.getInvItem().getIiName(),
                itemLocation.getInvLocation().getLocName(), branchCode, companyCode);
        if (!invNegTxnCosting.isEmpty()) {
            throw new GlobalInventoryDateException(itemLocation.getInvItem().getIiName());
        }
    }

    private LocalArInvoiceLineItem addArIliEntry(ArModInvoiceLineItemDetails mdetails, LocalArReceipt arReceipt,
                                                 LocalInvItemLocation itemLocation, Integer companyCode, String companyShortName)
            throws GlobalMiscInfoIsRequiredException {

        Debug.print("ArMiscReceiptEntryApiControllerBean addArIliEntry");

        try {

            short precisionUnit = this.getGlFcPrecisionUnit(companyCode, companyShortName);
            double itemAmount = 0d;
            double itemTaxAmount = 0d;

            LocalArTaxCode arTaxCode = arReceipt.getArTaxCode();

            // calculate net amount
            itemAmount = EJBCommon.calculateNetAmount(mdetails.getIliAmount(), mdetails.getIliTax(), arTaxCode.getTcRate(), arTaxCode.getTcType(), precisionUnit);

            // calculate tax
            itemTaxAmount = EJBCommon.calculateTaxAmount(mdetails.getIliAmount(), mdetails.getIliTax(), arTaxCode.getTcRate(), arTaxCode.getTcType(), itemAmount, precisionUnit);

            LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome
                    .IliLine(mdetails.getIliLine())
                    .IliQuantity(mdetails.getIliQuantity())
                    .IliUnitPrice(mdetails.getIliUnitPrice())
                    .IliAmount(itemAmount)
                    .IliTaxAmount(itemTaxAmount)
                    .IliDiscount1(mdetails.getIliDiscount1())
                    .IliDiscount2(mdetails.getIliDiscount2())
                    .IliDiscount3(mdetails.getIliDiscount3())
                    .IliDiscount4(mdetails.getIliDiscount4())
                    .IliTotalDiscount(mdetails.getIliTotalDiscount()) // Normal Discount Amount
                    .IliTax(mdetails.getIliTax())
                    .IliAdCompany(companyCode)
                    .buildInvoiceLineItem(companyShortName);
            arInvoiceLineItem.setArReceipt(arReceipt);
            arInvoiceLineItem.setInvItemLocation(itemLocation);

            LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(mdetails.getIliUomName(), companyCode, companyShortName);
            arInvoiceLineItem.setInvUnitOfMeasure(invUnitOfMeasure);

            return arInvoiceLineItem;

        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer companyCode, String companyShortName) {

        Debug.print("ArMiscReceiptEntryApiControllerBean getGlFcPrecisionUnit");
        try {
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode, companyShortName);
            return adCompany.getGlFunctionalCurrency().getFcPrecision();
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private double getItemCosting(Integer companyCode, Integer branchCode, LocalArReceipt arReceipt,
                                  LocalInvItemLocation itemLocation, LocalArInvoiceLineItem arInvoiceLineItem,
                                  String companyShortName)
            throws GlobalNoRecordFoundException {

        Debug.print("ArMiscReceiptEntryApiControllerBean getItemCosting");

        double COST = itemLocation.getInvItem().getIiUnitCost();

        try {
            LocalInvCosting invCosting = invCostingHome.getItemAverageCost(
                    arReceipt.getRctDate(), itemLocation.getInvItem().getIiName(),
                    itemLocation.getInvLocation().getLocName(), branchCode, companyCode,
                    companyShortName);

            if (invCosting != null) {
                // Check if Remaining Value is not zero and Remaining Quantity is zero
                if (itemLocation.getInvItem().getIiNonInventoriable() == (byte) 0 &&
                        invCosting.getCstRemainingQuantity() <= 0 && invCosting.getCstRemainingValue() <= 0) {
                    HashMap criteria = new HashMap();
                    criteria.put("itemName", itemLocation.getInvItem().getIiName());
                    criteria.put("location", itemLocation.getInvLocation().getLocName());

                    ArrayList branchList = new ArrayList();

                    AdBranchDetails mdetails = new AdBranchDetails();
                    mdetails.setBrCode(branchCode);
                    branchList.add(mdetails);

                    ejbRIC.executeInvFixItemCosting(criteria, branchList, companyCode, companyShortName);

                    invCosting = invCostingHome.getItemAverageCost(arReceipt.getRctDate(), itemLocation.getInvItem().getIiName(),
                            itemLocation.getInvLocation().getLocName(), branchCode, companyCode, companyShortName);
                }

                if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Average")) {
                    if (invCosting.getCstRemainingQuantity() <= 0) {
                        COST = itemLocation.getInvItem().getIiUnitCost();
                    } else {
                        COST = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                        if (COST <= 0) {
                            COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();
                        }
                    }
                }
            }

        }
        catch (GlobalNoRecordFoundException ex) {
            throw ex;
        }
        return COST;
    }

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem,
                                                      double quantitySold, Integer companyCode, String companyShortName) {

        Debug.print("ArMiscReceiptEntryApiControllerBean convertByUomFromAndItemAndQuantity");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode, companyShortName);
            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), companyCode, companyShortName);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(),
                            companyCode, companyShortName);

            return EJBCommon.roundIt(
                    quantitySold * invDefaultUomConversion.getUmcConversionFactor()
                            / invUnitOfMeasureConversion.getUmcConversionFactor(),
                    adPreference.getPrfInvQuantityPrecisionUnit());

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void costOfGoodsSoldJournals(Integer companyCode, Integer branchCode, LocalArReceipt arReceipt, LocalAdPreference adPreference,
                                         LocalInvItemLocation itemLocation, LocalArInvoiceLineItem arInvoiceLineItem, double COST, double quantitySold,
                                         LocalAdBranchItemLocation adBranchItemLocation, String companyShortName)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArMiscReceiptEntryApiControllerBean costOfGoodsSoldJournals");

        if (adPreference.getPrfArAutoComputeCogs() == EJBCommon.TRUE &&
                arInvoiceLineItem.getInvItemLocation().getInvItem().getIiNonInventoriable() == EJBCommon.FALSE) {

            if (adBranchItemLocation != null) {
                this.addArDrIliEntry(arReceipt.getArDrNextLine(), "COGS", EJBCommon.TRUE, COST * quantitySold,
                        adBranchItemLocation.getBilCoaGlCostOfSalesAccount(), arReceipt, branchCode, companyCode, companyShortName);
                this.addArDrIliEntry(arReceipt.getArDrNextLine(), "INVENTORY", EJBCommon.FALSE,
                        COST * quantitySold, adBranchItemLocation.getBilCoaGlInventoryAccount(),
                        arReceipt, branchCode, companyCode, companyShortName);
            } else {

                this.addArDrIliEntry(arReceipt.getArDrNextLine(), "COGS", EJBCommon.TRUE, COST * quantitySold,
                        arInvoiceLineItem.getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arReceipt, branchCode, companyCode, companyShortName);
                this.addArDrIliEntry(arReceipt.getArDrNextLine(), "INVENTORY", EJBCommon.FALSE,
                        COST * quantitySold, arInvoiceLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(),
                        arReceipt, branchCode, companyCode, companyShortName);
            }

            // add quantity to item location committed quantity
            double convertedQuantity = this.convertByUomFromAndItemAndQuantity(
                    arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(),
                    arInvoiceLineItem.getIliQuantity(), companyCode, companyShortName);
            itemLocation.setIlCommittedQuantity(itemLocation.getIlCommittedQuantity() + convertedQuantity);
        }
    }

    private void addArDrIliEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE,
                                 LocalArReceipt arReceipt, Integer branchCode, Integer companyCode, String companyShortName)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArMiscReceiptEntryApiControllerBean addArDrIliEntry");

        try {

            // get company
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode, companyShortName);
            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, branchCode,
                    companyCode, companyShortName);

            // create distribution record
            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome
                    .DrLine(DR_LN)
                    .DrClass(DR_CLSS)
                    .DrDebit(DR_DBT)
                    .DrAmount(EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()))
                    .DrAdCompany(companyCode)
                    .buildDistributionRecords(companyShortName);
            arDistributionRecord.setArReceipt(arReceipt);
            arDistributionRecord.setGlChartOfAccount(glChartOfAccount);

        }
        catch (FinderException ex) {

            throw new GlobalBranchAccountNumberInvalidException();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void inventoryRevenueJournals(Integer companyCode, Integer branchCode, LocalArReceipt arReceipt,
                                          LocalArInvoiceLineItem arInvoiceLineItem, LocalAdBranchItemLocation adBranchItemLocation,
                                          String companyShortName)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArMiscReceiptEntryApiControllerBean inventoryRevenueJournals");

        double convertedItemAmount = this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(),
                arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(),
                arReceipt.getRctConversionRate(), arInvoiceLineItem.getIliAmount(), companyCode, companyShortName);

        if (adBranchItemLocation != null) {
            if (adBranchItemLocation.getInvItemLocation().getInvItem().getIiServices() == EJBCommon.TRUE) {
                this.addArDrIliEntry(arReceipt.getArDrNextLine(), "REVENUE", EJBCommon.TRUE, convertedItemAmount,
                        adBranchItemLocation.getBilCoaGlSalesAccount(), arReceipt, branchCode, companyCode, companyShortName);
                this.addArDrIliEntry(arReceipt.getArDrNextLine(), "OTHER", EJBCommon.FALSE, convertedItemAmount,
                        adBranchItemLocation.getBilCoaGlSalesReturnAccount(), arReceipt, branchCode, companyCode, companyShortName);
            } else {
                this.addArDrIliEntry(arReceipt.getArDrNextLine(), "REVENUE", EJBCommon.FALSE, convertedItemAmount,
                        adBranchItemLocation.getBilCoaGlSalesAccount(), arReceipt, branchCode, companyCode, companyShortName);
            }
        } else {
            if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiServices() == EJBCommon.TRUE) {
                this.addArDrIliEntry(arReceipt.getArDrNextLine(), "REVENUE", EJBCommon.TRUE, convertedItemAmount,
                        arInvoiceLineItem.getInvItemLocation().getIlGlCoaSalesAccount(), arReceipt, branchCode, companyCode, companyShortName);
                this.addArDrIliEntry(arReceipt.getArDrNextLine(), "OTHER", EJBCommon.FALSE, convertedItemAmount,
                        arInvoiceLineItem.getInvItemLocation().getIlGlCoaSalesReturnAccount(), arReceipt, branchCode, companyCode, companyShortName);
            } else {
                this.addArDrIliEntry(arReceipt.getArDrNextLine(), "REVENUE", EJBCommon.FALSE, convertedItemAmount,
                        arInvoiceLineItem.getInvItemLocation().getIlGlCoaSalesAccount(), arReceipt, branchCode, companyCode, companyShortName);
            }
        }
    }

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String foreignCurrency, Date CONVERSION_DATE,
                                                      double CONVERSION_RATE, double AMOUNT, Integer companyCode, String companyShortName) {

        Debug.print("ArMiscReceiptEntryApiControllerBean convertForeignToFunctionalCurrency");

        LocalAdCompany adCompany = null;

        // get company and extended precision
        try {
            adCompany = adCompanyHome.findByPrimaryKey(companyCode, companyShortName);
        }
        catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        // Convert to functional currency if necessary
        if (CONVERSION_RATE != 1 && CONVERSION_RATE != 0) {
            AMOUNT = AMOUNT / CONVERSION_RATE;
        }
        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    private void addArDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE, byte DR_RVRSD,
                              LocalArReceipt arReceipt, Integer branchCode, Integer companyCode, String companyShortName)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArMiscReceiptEntryApiControllerBean addArDrEntry");

        try {

            // get company
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode, companyShortName);
            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, branchCode,
                    companyCode, companyShortName);

            // create distribution record
            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome
                    .DrLine(DR_LN)
                    .DrClass(DR_CLSS)
                    .DrDebit(DR_DBT)
                    .DrAmount(EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()))
                    .DrReversed(DR_RVRSD)
                    .DrAdCompany(companyCode)
                    .buildDistributionRecords(companyShortName);

            arDistributionRecord.setArReceipt(arReceipt);
            arDistributionRecord.setGlChartOfAccount(glChartOfAccount);

        }
        catch (FinderException ex) {

            throw new GlobalBranchAccountNumberInvalidException(ex.getMessage());

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertByUomAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem,
                                           double ADJST_QTY, Integer companyCode) {

        Debug.print("ArMiscReceiptEntryApiControllerBean convertByUomAndQuantity");


        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(),
                            companyCode);

            return EJBCommon.roundIt(
                    ADJST_QTY * invDefaultUomConversion.getUmcConversionFactor()
                            / invUnitOfMeasureConversion.getUmcConversionFactor(),
                    adPreference.getPrfInvQuantityPrecisionUnit());

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue,
                          LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double JL_AMNT,
                          Integer companyCode) {

        Debug.print("ArMiscReceiptEntryApiControllerBean postToGl");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccountBalance glChartOfAccountBalance = glChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(
                    glAccountingCalendarValue.getAcvCode(), glChartOfAccount.getCoaCode(), companyCode);

            String ACCOUNT_TYPE = glChartOfAccount.getCoaAccountType();
            short FC_EXTNDD_PRCSN = adCompany.getGlFunctionalCurrency().getFcPrecision();

            if (((ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("EXPENSE")) && isDebit == EJBCommon.TRUE)
                    || (!ACCOUNT_TYPE.equals("ASSET") && !ACCOUNT_TYPE.equals("EXPENSE")
                    && isDebit == EJBCommon.FALSE)) {

                glChartOfAccountBalance.setCoabEndingBalance(
                        EJBCommon.roundIt(glChartOfAccountBalance.getCoabEndingBalance() + JL_AMNT, FC_EXTNDD_PRCSN));

                if (!isCurrentAcv) {

                    glChartOfAccountBalance.setCoabBeginningBalance(EJBCommon
                            .roundIt(glChartOfAccountBalance.getCoabBeginningBalance() + JL_AMNT, FC_EXTNDD_PRCSN));
                }

            } else {

                glChartOfAccountBalance.setCoabEndingBalance(
                        EJBCommon.roundIt(glChartOfAccountBalance.getCoabEndingBalance() - JL_AMNT, FC_EXTNDD_PRCSN));

                if (!isCurrentAcv) {

                    glChartOfAccountBalance.setCoabBeginningBalance(EJBCommon
                            .roundIt(glChartOfAccountBalance.getCoabBeginningBalance() - JL_AMNT, FC_EXTNDD_PRCSN));
                }
            }

            if (isCurrentAcv) {

                if (isDebit == EJBCommon.TRUE) {

                    glChartOfAccountBalance.setCoabTotalDebit(
                            EJBCommon.roundIt(glChartOfAccountBalance.getCoabTotalDebit() + JL_AMNT, FC_EXTNDD_PRCSN));

                } else {

                    glChartOfAccountBalance.setCoabTotalCredit(
                            EJBCommon.roundIt(glChartOfAccountBalance.getCoabTotalCredit() + JL_AMNT, FC_EXTNDD_PRCSN));
                }
            }

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private double getFrRateByFrNameAndFrDate(String FC_NM, Date CONVERSION_DATE, Integer companyCode)
            throws GlobalConversionDateNotExistException {

        Debug.print("ArMiscReceiptEntryApiControllerBean getFrRateByFrNameAndFrDate");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, companyCode);

            double CONVERSION_RATE = 1;

            // Get functional currency rate

            if (!FC_NM.equals("USD")) {

                LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome
                        .findByFcCodeAndDate(glFunctionalCurrency.getFcCode(), CONVERSION_DATE, companyCode);

                CONVERSION_RATE = glFunctionalCurrencyRate.getFrXToUsd();
            }

            // Get set of book functional currency rate if necessary

            if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate = glFunctionalCurrencyRateHome
                        .findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), CONVERSION_DATE,
                                companyCode);

                CONVERSION_RATE = CONVERSION_RATE / glCompanyFunctionalCurrencyRate.getFrXToUsd();
            }

            return CONVERSION_RATE;

        }
        catch (FinderException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlobalConversionDateNotExistException();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private String getQuantityExpiryDates(String qntty) {

        Debug.print("ArMiscReceiptEntryApiControllerBean getQuantityExpiryDates");

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
        }
        catch (Exception e) {
            y = "0";
        }

        return y;
    }

    private String propagateExpiryDates(String misc, double qty, String reverse) throws Exception {

        Debug.print("ArMiscReceiptEntryApiControllerBean propagateExpiryDates");

        String separator = "";
        if (reverse == "False") {
            separator = "$";
        } else {
            separator = " ";
        }

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
            String g = misc.substring(start, start + length);
            if (g.length() != 0) {
                miscList.append("$").append(g);
            }
        }

        miscList.append("$");
        return (miscList.toString());
    }

    private ArrayList expiryDates(String misc, double qty) throws Exception {

        Debug.print("ArMiscReceiptEntryApiControllerBean expiryDates");
        String separator = "$";

        // Remove first $ character
        misc = misc.substring(1);

        // Counter
        int start = 0;
        int nextIndex = misc.indexOf(separator, start);
        int length = nextIndex - start;

        ArrayList miscList = new ArrayList();

        for (int x = 0; x < qty; x++) {

            // Date
            start = nextIndex + 1;
            nextIndex = misc.indexOf(separator, start);
            length = nextIndex - start;
            String checker = misc.substring(start, start + length);
            if (checker.length() != 0 || checker != "null") {
                miscList.add(checker);
            } else {
                miscList.add("null");
            }
        }

        return miscList;
    }

    private int checkExpiryDates(String misc) throws Exception {

        Debug.print("ArMiscReceiptEntryApiControllerBean checkExpiryDates");

        String separator = "$";

        // Remove first $ character
        misc = misc.substring(1);

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
        }
        catch (Exception e) {

        }

        if (miscList2 == "") {
            miscList.append("$");
        } else {
            miscList = new StringBuilder(miscList2);
        }

        return (numberExpry);
    }

    private void generateCostVariance(LocalInvItemLocation invItemLocation, double CST_VRNC_VL, String ADJ_RFRNC_NMBR,
                                      String ADJ_DSCRPTN, Date ADJ_DT, String USR_NM, Integer branchCode, Integer companyCode)
            throws AdPRFCoaGlVarianceAccountNotFoundException {

    }

    private void regenerateCostVariance(Collection invCostings, LocalInvCosting invCosting, Integer branchCode,
                                        Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

    }

    private void addInvDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, byte DR_RVRSL,
                               Integer COA_CODE, LocalInvAdjustment invAdjustment, Integer branchCode, Integer companyCode)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArMiscReceiptEntryApiControllerBean addInvDrEntry");


        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate coa

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, branchCode, companyCode);

            }
            catch (FinderException ex) {

                throw new GlobalBranchAccountNumberInvalidException();
            }

            // create distribution record

            LocalInvDistributionRecord invDistributionRecord = invDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT,
                    EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), DR_RVRSL,
                    EJBCommon.FALSE, companyCode);

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

    private LocalInvAdjustmentLine addInvAlEntry(LocalInvItemLocation invItemLocation, LocalInvAdjustment invAdjustment,
                                                 double CST_VRNC_VL, byte AL_VD, Integer companyCode) {

        Debug.print("ArMiscReceiptEntryApiControllerBean addInvAlEntry");

        LocalInvAdjustmentLineHome invAdjustmentLineHome = null;

        // Initialize EJB Home

        try {

            invAdjustmentLineHome = (LocalInvAdjustmentLineHome) EJBHomeFactory
                    .lookUpLocalHome(LocalInvAdjustmentLineHome.JNDI_NAME, LocalInvAdjustmentLineHome.class);

        }
        catch (NamingException ex) {

            throw new EJBException(ex.getMessage());
        }

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

    private void executeInvAdjPost(Integer ADJ_CODE, String USR_NM, Integer branchCode, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException,
            GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException,
            GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException {

        Debug.print("ArMiscReceiptEntryApiControllerBean executeInvAdjPost");

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

                if (invAdjustment.getAdjVoid() != EJBCommon.TRUE) {
                    throw new GlobalTransactionAlreadyPostedException();
                }
            }

            Collection invAdjustmentLines = null;

            if (invAdjustment.getAdjVoid() == EJBCommon.FALSE) {
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.FALSE,
                        invAdjustment.getAdjCode(), companyCode);
            } else {
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.TRUE,
                        invAdjustment.getAdjCode(), companyCode);
            }

            Iterator i = invAdjustmentLines.iterator();

            while (i.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();

                LocalInvCosting invCosting = invCostingHome
                        .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                invAdjustment.getAdjDate(),
                                invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(),
                                invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName(), branchCode,
                                companyCode);

                this.postInvAdjustmentToInventory(invAdjustmentLine, invAdjustment.getAdjDate(), 0,
                        invAdjustmentLine.getAlUnitCost(), invCosting.getCstRemainingQuantity(),
                        invCosting.getCstRemainingValue() + invAdjustmentLine.getAlUnitCost(), branchCode, companyCode);
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

            LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome
                    .findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(),
                            invAdjustment.getAdjDate(), companyCode);

            if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C'
                    || glAccountingCalendarValue.getAcvStatus() == 'P') {

                throw new GlJREffectiveDatePeriodClosedException();
            }

            // check if invoice is balance if not check suspense posting

            LocalGlJournalLine glOffsetJournalLine = null;

            Collection invDistributionRecords = null;

            if (invAdjustment.getAdjVoid() == EJBCommon.FALSE) {

                invDistributionRecords = invDistributionRecordHome
                        .findImportableDrByDrReversedAndAdjCode(EJBCommon.FALSE, invAdjustment.getAdjCode(), companyCode);

            } else {

                invDistributionRecords = invDistributionRecordHome
                        .findImportableDrByDrReversedAndAdjCode(EJBCommon.TRUE, invAdjustment.getAdjCode(), companyCode);
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

                    glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("INVENTORY",
                            "INVENTORY ADJUSTMENTS", companyCode);

                }
                catch (FinderException ex) {

                    throw new GlobalJournalNotBalanceException();
                }

                if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                    glOffsetJournalLine = glJournalLineHome.create((short) (invDistributionRecords.size() + 1),
                            EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);

                } else {

                    glOffsetJournalLine = glJournalLineHome.create((short) (invDistributionRecords.size() + 1),
                            EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
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

                glJournalBatch = glJournalBatchHome.findByJbName(
                        "JOURNAL IMPORT " + formatter.format(new Date()) + " INVENTORY ADJUSTMENTS", branchCode,
                        companyCode);

            }
            catch (FinderException ex) {

            }

            if (glJournalBatch == null) {

                glJournalBatch = glJournalBatchHome.create(
                        "JOURNAL IMPORT " + formatter.format(new Date()) + " INVENTORY ADJUSTMENTS", "JOURNAL IMPORT",
                        "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, branchCode, companyCode);
            }

            // create journal entry

            LocalGlJournal glJournal = glJournalHome.create(invAdjustment.getAdjReferenceNumber(),
                    invAdjustment.getAdjDescription(), invAdjustment.getAdjDate(), 0.0d, null,
                    invAdjustment.getAdjDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE,
                    USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM,
                    EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, EJBCommon.FALSE, null, branchCode,
                    companyCode);

            LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("INVENTORY", companyCode);
            glJournal.setGlJournalSource(glJournalSource);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome
                    .findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
            glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("INVENTORY ADJUSTMENTS",
                    companyCode);
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

                LocalGlJournalLine glJournalLine = glJournalLineHome.create(invDistributionRecord.getDrLine(),
                        invDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

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

                this.postToGl(glAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), true,
                        glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                // post to subsequent acvs (propagate)

                Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome
                        .findSubsequentAcvByAcCodeAndAcvPeriodNumber(
                                glJournalSetOfBook.getGlAccountingCalendar().getAcCode(),
                                glAccountingCalendarValue.getAcvPeriodNumber(), companyCode);

                for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {

                    LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;

                    this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false,
                            glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                }

                // post to subsequent years if necessary

                Collection glSubsequentSetOfBooks = glSetOfBookHome
                        .findSubsequentSobByAcYear(glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode);

                if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                    adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                    LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome
                            .findByCoaAccountNumberAndBranchCode(adCompany.getCmpRetainedEarnings(), branchCode,
                                    companyCode);

                    for (Object subsequentSetOfBook : glSubsequentSetOfBooks) {

                        LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) subsequentSetOfBook;

                        String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                        // post to subsequent acvs of subsequent set of book(propagate)

                        Collection glAccountingCalendarValues = glAccountingCalendarValueHome
                                .findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                        for (Object accountingCalendarValue : glAccountingCalendarValues) {

                            LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                            if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY")
                                    || ACCOUNT_TYPE.equals("OWNERS EQUITY")) {

                                this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(),
                                        false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                            } else { // revenue & expense

                                this.postToGl(glSubsequentAccountingCalendarValue, glRetainedEarningsAccount, false,
                                        glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                            }
                        }

                        if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) {
                            break;
                        }
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

    private void postInvAdjustmentToInventory(LocalInvAdjustmentLine invAdjustmentLine, Date CST_DT,
                                              double CST_ADJST_QTY, double CST_ADJST_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, Integer branchCode,
                                              Integer companyCode) {

        Debug.print("ArMiscReceiptEntryApiControllerBean postInvAdjustmentToInventory");

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

                invItemLocation
                        .setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - Math.abs(CST_ADJST_QTY));
            }

            // create costing

            try {

                // generate line number

                LocalInvCosting invCurrentCosting = invCostingHome
                        .getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(),
                                invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(),
                                branchCode, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            }
            catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d,
                    CST_ADJST_QTY, CST_ADJST_CST, 0d, 0d, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d,
                    CST_ADJST_QTY > 0 ? CST_ADJST_QTY : 0, branchCode, companyCode);
            // invItemLocation.addInvCosting(invCosting);
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setInvAdjustmentLine(invAdjustmentLine);

            invCosting.setCstQuantity(CST_ADJST_QTY);
            invCosting.setCstCost(CST_ADJST_CST);

            // propagate balance if necessary

            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT,
                    invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode,
                    companyCode);

            for (Object costing : invCostings) {

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) costing;

                invPropagatedCosting
                        .setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_ADJST_QTY);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_ADJST_CST);
            }

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void discountJournals(Integer companyCode, Integer branchCode, LocalArReceipt arReceipt, ArModInvoiceLineItemDetails lineDetails, String companyShortName)
            throws GlobalBranchAccountNumberInvalidException, FinderException {

        LocalAdBranch branch = null;
        try {
            branch = adBranchHome.findByPrimaryKey(branchCode);
        }
        catch (FinderException ex) {

        }

        Integer discountCoa = null;
        try {
            List<LocalGlChartOfAccount> defaultCoa = new ArrayList(
                    glChartOfAccountHome.findHoCoaAllByCoaCategory("DISCOUNT", branch.getBrName(), companyCode, companyShortName));

            for (LocalGlChartOfAccount chartOfAccount : defaultCoa) {

                // Get only DISCOUNT with REVENUE class type
                if (chartOfAccount.getCoaAccountType().equals("REVENUE")) {
                    discountCoa = chartOfAccount.getCoaCode();
                }
            }

        }
        catch (FinderException ex) {
            throw new GlobalBranchAccountNumberInvalidException();
        }

        short precisionUnit = this.getGlFcPrecisionUnit(companyCode, companyShortName);
        double itemDiscountAmount = 0d;
        double itemDiscountTaxAmount = 0d;

        LocalArTaxCode arTaxCode = arReceipt.getArTaxCode();

        // calculate net amount
        itemDiscountAmount = EJBCommon.calculateNetAmount(lineDetails.getIliTotalDiscount(),
                lineDetails.getIliTax(), arTaxCode.getTcRate(), arTaxCode.getTcType(), precisionUnit);

        this.addArDrEntry(arReceipt.getArDrNextLine(), "SALES DISCOUNT", EJBCommon.TRUE, itemDiscountAmount,
                discountCoa, EJBCommon.FALSE, arReceipt, branchCode, companyCode, companyShortName);

        // calculate tax
        itemDiscountTaxAmount = EJBCommon.calculateTaxAmount(lineDetails.getIliTotalDiscount(),
                lineDetails.getIliTax(), arTaxCode.getTcRate(), arTaxCode.getTcType(), itemDiscountAmount, precisionUnit);

        LocalAdBranchArTaxCode adBranchTaxCode = null;

        try {
            //TODO: Review this finder method that is causing finder exception
            adBranchTaxCode = adBranchArTaxCodeHome.findBtcByTcCodeAndBrCode(arReceipt.getArTaxCode().getTcCode(), branchCode, companyCode);
            this.addArDrEntry(arReceipt.getArDrNextLine(), "TAX", EJBCommon.TRUE, itemDiscountTaxAmount,
                    adBranchTaxCode.getBtcGlCoaTaxCode(), EJBCommon.FALSE, arReceipt, branchCode, companyCode, companyShortName);
        }
        catch (FinderException ex) {
            this.addArDrEntry(arReceipt.getArDrNextLine(), "TAX", EJBCommon.TRUE, itemDiscountTaxAmount,
                    arTaxCode.getGlChartOfAccount().getCoaCode(), EJBCommon.FALSE, arReceipt, branchCode, companyCode, companyShortName);
        }
    }

}