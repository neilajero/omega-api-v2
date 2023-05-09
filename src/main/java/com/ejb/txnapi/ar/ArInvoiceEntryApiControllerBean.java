package com.ejb.txnapi.ar;

import com.ejb.ConfigurationClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ar.*;
import com.ejb.dao.gen.LocalGenValueSetValueHome;
import com.ejb.dao.gl.*;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ar.*;
import com.ejb.entities.gen.LocalGenField;
import com.ejb.entities.gen.LocalGenValueSetValue;
import com.ejb.entities.gl.*;
import com.ejb.entities.inv.*;
import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.ar.ArINVAmountExceedsCreditLimitException;
import com.ejb.exception.ar.ArInvDuplicateUploadNumberException;
import com.ejb.exception.ar.ArInvoiceStandardMemolineDoesNotExist;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.ejb.restfulapi.OfsApiResponse;
import com.ejb.restfulapi.ar.models.InvoiceItemRequest;
import com.ejb.restfulapi.ar.models.InvoiceMemoLineRequest;
import com.ejb.restfulapi.ar.models.LineItemRequest;
import com.ejb.restfulapi.ar.models.MemolineDetails;
import com.ejb.txnreports.inv.InvRepItemCostingController;
import com.util.*;
import com.util.ad.AdBranchDetails;
import com.util.ar.ArInvoiceDetails;
import com.util.mod.ar.ArModInvoiceLineDetails;
import com.util.mod.ar.ArModInvoiceLineItemDetails;
import jakarta.ejb.*;

import javax.naming.NamingException;
import java.util.*;

@Stateless(name = "ArInvoiceEntryApiControllerEJB")
public class ArInvoiceEntryApiControllerBean extends EJBContextClass implements ArInvoiceEntryApiController {
    private static final String INVOICE = "INVOICE";
    private static final String REVENUE = "REVENUE";
    private static final String RECEIVABLE = "RECEIVABLE";
    private static final String TAX = "TAX";
    private static final String W_TAX = "W-TAX";
    private static final String ASSET = "ASSET";
    private static final String LIABILITY = "LIABILITY";
    private static final String OWNERS_EQUITY = "OWNERS EQUITY";
    private static final String INVENTORY = "INVENTORY";
    private static final String COGS = "COGS";
    private static final String Average = "Average";
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalAdPaymentTermHome adPaymentTermHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalArTaxCodeHome arTaxCodeHome;
    @EJB
    private LocalArWithholdingTaxCodeHome arWithholdingTaxCodeHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalArCustomerBalanceHome arCustomerBalanceHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalArPdcHome arPdcHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private ILocalGlSetOfBookHome glSetOfBookHome;
    @EJB
    private ILocalGlAccountingCalendarValueHome glAccountingCalendarValueHome;
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
    private LocalGlForexLedgerHome glForexLedgerHome;
    @EJB
    private ILocalGlChartOfAccountBalanceHome glChartOfAccountBalanceHome;
    @EJB
    private InvRepItemCostingController ejbRIC;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
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
    private LocalAdBranchArTaxCodeHome adBranchArTaxCodeHome;
    @EJB
    private LocalAdBranchCustomerHome adBranchCustomerHome;
    @EJB
    private LocalArInvoiceLineHome arInvoiceLineHome;
    @EJB
    private LocalArStandardMemoLineHome arStandardMemoLineHome;
    @EJB
    private LocalAdBranchStandardMemoLineHome adBranchStandardMemoLineHome;
    @EJB
    private LocalArAutoAccountingSegmentHome arAutoAccountingSegmentHome;
    @EJB
    private LocalArInvoicePaymentScheduleHome arInvoicePaymentScheduleHome;
    @EJB
    private LocalGenValueSetValueHome genValueSetValueHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdApprovalDocumentHome adApprovalDocumentHome;

    @Override
    public OfsApiResponse createInvoiceItems(InvoiceItemRequest invoiceItemRequest) {

        Debug.print("ArInvoiceEntryApiControllerBean createInvoiceItems");

        OfsApiResponse apiResponse = new OfsApiResponse();

        LocalAdCompany adCompany;
        LocalAdBranch adBranch;
        LocalAdUser adUser;
        LocalArCustomer arCustomer;
        LocalArInvoice arInvoice;
        Date invoiceDate = EJBCommon.convertStringToSQLDate(invoiceItemRequest.getInvoiceDate(), ConfigurationClass.DEFAULT_DATE_FORMAT);
        String branchName = invoiceItemRequest.getBranchCode();
        String defaultLocation = "DEFAULT";

        try {

            ArInvoiceDetails invoiceDetails = new ArInvoiceDetails();

            // Company Code
            try {
                if (invoiceItemRequest.getCompanyCode() == null || invoiceItemRequest.getCompanyCode().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_041);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_041_MSG);
                    return apiResponse;
                }
                adCompany = adCompanyHome.findByCmpShortName(invoiceItemRequest.getCompanyCode());
                if (adCompany != null) {
                    invoiceDetails.setInvAdCompany(adCompany.getCmpCode());
                }
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
                return apiResponse;
            }

            // Branch Code
            try {
                if (invoiceItemRequest.getBranchCode() == null || invoiceItemRequest.getBranchCode().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return apiResponse;
                }
                adBranch = adBranchHome.findByBrBranchCode(invoiceItemRequest.getBranchCode(), adCompany.getCmpCode());
                if (adBranch != null) {
                    invoiceDetails.setInvAdBranch(adBranch.getBrCode());
                }
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_065);
                apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_065_MSG, invoiceItemRequest.getBranchCode()));
                return apiResponse;
            }

            // User
            try {
                if (invoiceItemRequest.getUsername() == null || invoiceItemRequest.getUsername().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_042);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_042_MSG);
                    return apiResponse;
                }
                adUser = adUserHome.findByUsrName(invoiceItemRequest.getUsername(), adCompany.getCmpCode());
                if (adUser != null) {
                    invoiceDetails.setInvCreatedBy(adUser.getUsrName());
                    invoiceDetails.setInvLastModifiedBy(adUser.getUsrName());
                }
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_002);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_002_MSG);
                return apiResponse;
            }

            // Customer Code from TT
            // WARNING: Do not adopt this code because Tecnotree has issues at their end
            try {
                if (invoiceItemRequest.getCustomerCode() == null || invoiceItemRequest.getCustomerCode().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return apiResponse;
                }
                arCustomer = arCustomerHome.findByCstRefCustomerCode(invoiceItemRequest.getCustomerCode(), adBranch.getBrCode(), adCompany.getCmpCode());
            }
            catch (FinderException ex) {
                try {
                    arCustomer = arCustomerHome.findByCstCustomerCode(invoiceItemRequest.getCustomerCode(), adBranch.getBrCode(), adCompany.getCmpCode());
                }
                catch (FinderException e) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_009);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_009_MSG);
                    return apiResponse;
                }
            }

            // Invoice Number from TT
            try {
                if (invoiceItemRequest.getReferenceNumber() == null || invoiceItemRequest.getReferenceNumber().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return apiResponse;
                }
                arInvoice = arInvoiceHome.findByTTInvNumber(invoiceItemRequest.getReferenceNumber(), adBranch.getBrCode(), adCompany.getCmpCode());
                if (arInvoice != null) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_040);
                    apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_040_MSG, invoiceItemRequest.getReferenceNumber()));
                    return apiResponse;
                }
            }
            catch (FinderException ex) {

            }

            ArrayList invoiceLineItems = new ArrayList();

            if (invoiceItemRequest.getInvoiceItems() == null || invoiceItemRequest.getInvoiceItems().size() == 0) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_061);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_061_MSG);
                return apiResponse;
            }

            for (LineItemRequest itemRequest : invoiceItemRequest.getInvoiceItems()) {
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
                    apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_010_MSG, invItem.getIiName()));
                    return apiResponse;
                }

                // itemLocation
                mdetails.setIliLocName(defaultLocation);

                // confirm if item is in location
                try {
                    invItemLocationHome.findByIiNameAndLocNameAndAdBranch(itemRequest.getItemName(), defaultLocation, adBranch.getBrCode(), adCompany.getCmpCode());
                }
                catch (FinderException ex) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_011);
                    apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_011_MSG, invItem.getIiName()));
                    return apiResponse;
                }

                mdetails.setIliTax(EJBCommon.TRUE);
                mdetails.setIliUomName(invItem.getInvUnitOfMeasure().getUomName());
                mdetails.setIliQuantity(itemRequest.getItemQuantity());

                // Calculate the Invoice Amount based on the item invoiceDetails saved in OFS
                mdetails.setIliUnitPrice(itemRequest.getItemSalesPrice());
                mdetails.setIliAmount(itemRequest.getItemSalesPrice() * itemRequest.getItemQuantity());

                // Additional requirements from TT
                mdetails.setIliTotalDiscount(itemRequest.getTransDiscAmt());
                mdetails.setIliDiscount2(itemRequest.getNormalDiscAmt()); // This value is collected but ignored in Omega ERP
                mdetails.setIliTaxAmount(itemRequest.getTaxAmt()); // This value is collected but ignored in Omega ERP

                // Update Item Sales Price and Percent Markup
                if (invItem.getIiUnitCost() > 0) {
                    updateSalesPrice(itemRequest, invItem);
                } else {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_031);
                    apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_031_MSG, invItem.getIiName()));
                    return apiResponse;
                }
                invoiceLineItems.add(mdetails);
            }

            if (!invoiceItemRequest.getInvoiceType().equals("ITEMS")) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_037);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_037_MSG);
                return apiResponse;
            }

            String systemGenDesc = "DESC-TT";
            if (invoiceItemRequest.getDescription() != null) {
                systemGenDesc = invoiceItemRequest.getDescription();
            }

            invoiceDetails.setInvType(invoiceItemRequest.getInvoiceType());
            invoiceDetails.setInvDate(invoiceDate);
            invoiceDetails.setInvReferenceNumber(invoiceItemRequest.getReferenceNumber());
            invoiceDetails.setInvDescription(systemGenDesc);
            invoiceDetails.setInvEffectivityDate(invoiceDate);
            invoiceDetails.setInvReceiveDate(invoiceDate);
            invoiceDetails.setInvDateCreated(new java.util.Date());
            invoiceDetails.setInvDateLastModified(new java.util.Date());
            invoiceDetails.setReportParameter("");
            invoiceDetails.setPaymentTerm("IMMEDIATE");
            invoiceDetails.setTaxCode("GST INCLUSIVE");
            invoiceDetails.setWithholdingTaxCode("NONE");
            invoiceDetails.setCurrencyCode("PGK");
            invoiceDetails.setCustomerCode(arCustomer.getCstCustomerCode());

            Integer invoiceCode = 0;

            // ITEMS
            invoiceCode = this.saveInvoiceItems(invoiceDetails, invoiceLineItems);

            arInvoice = arInvoiceHome.findByPrimaryKey(invoiceCode);

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            apiResponse.setDocumentNumber(arInvoice.getInvNumber());
            apiResponse.setStatus("Created invoice transaction successfully.");

        }
        catch (GlobalPaymentTermInvalidException ex) {

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_012);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_012_MSG);
            return apiResponse;

        }
        catch (ArINVAmountExceedsCreditLimitException ex) {

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_013);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_013_MSG);
            return apiResponse;

        }
        catch (GlobalInvItemLocationNotFoundException ex) {

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_014);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_014_MSG);
            return apiResponse;

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
        catch (GlobalBranchAccountNumberInvalidException ex) {

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_018);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_018_MSG);
            return apiResponse;

        }
        catch (AdPRFCoaGlVarianceAccountNotFoundException ex) {

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_019);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_019_MSG);
            return apiResponse;

        }
        catch (GlobalRecordInvalidException ex) {

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_020);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_020_MSG);
            return apiResponse;

        }
        catch (GlobalInvItemCostingNotFoundException ex) {

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_032);
            apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_032_MSG, ex.getMessage()));
            return apiResponse;

        }
        catch (GlobalInventoryDateException ex) {

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_060);
            apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_060_MSG, ex.getMessage()));
            return apiResponse;

        }
        catch (Exception ex) {

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return apiResponse;
        }

        return apiResponse;
    }

    @Override
    public OfsApiResponse createInvoiceMemoLines(InvoiceMemoLineRequest invoiceMemoLineRequest) {

        Debug.print("ArInvoiceEntryApiControllerBean createInvoiceMemoLines");

        OfsApiResponse apiResponse = new OfsApiResponse();
        LocalAdCompany adCompany;
        LocalAdBranch adBranch;
        LocalAdUser adUser;
        LocalArCustomer arCustomer;
        LocalArInvoice arInvoice;
        LocalGlFunctionalCurrency functionalCurrency;
        LocalArTaxCode taxCode = null;
        LocalArWithholdingTaxCode withholdingTaxCode = null;
        LocalAdPaymentTerm adPaymentTerm = null;
        Date invoiceDate = EJBCommon.convertStringToSQLDate(invoiceMemoLineRequest.getInvoiceDate(), ConfigurationClass.DEFAULT_DATE_FORMAT);

        try {

            ArInvoiceDetails invoiceDetails = new ArInvoiceDetails();

            // Company Code
            try {
                if (invoiceMemoLineRequest.getCompanyCode() == null || invoiceMemoLineRequest.getCompanyCode().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_041);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_041_MSG);
                    return apiResponse;
                }
                adCompany = adCompanyHome.findByCmpShortName(invoiceMemoLineRequest.getCompanyCode());
                if (adCompany != null) {
                    invoiceDetails.setInvAdCompany(adCompany.getCmpCode());
                    invoiceDetails.setCompanyShortName(adCompany.getCmpShortName());
                }
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
                return apiResponse;
            }

            // Branch Code
            try {
                if (invoiceMemoLineRequest.getBranchCode() == null || invoiceMemoLineRequest.getBranchCode().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return apiResponse;
                }
                adBranch = adBranchHome.findByBrBranchCode(invoiceMemoLineRequest.getBranchCode(),
                        invoiceDetails.getInvAdCompany(), invoiceDetails.getCompanyShortName());
                if (adBranch != null) {
                    invoiceDetails.setInvAdBranch(adBranch.getBrCode());
                }
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_065);
                apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_065_MSG, invoiceMemoLineRequest.getBranchCode()));
                return apiResponse;
            }

            // User
            try {
                if (invoiceMemoLineRequest.getUsername() == null || invoiceMemoLineRequest.getUsername().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_042);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_042_MSG);
                    return apiResponse;
                }
                adUser = adUserHome.findByUsrName(invoiceMemoLineRequest.getUsername(), invoiceDetails.getInvAdCompany(),
                        invoiceDetails.getCompanyShortName());
                if (adUser != null) {
                    invoiceDetails.setInvCreatedBy(adUser.getUsrName());
                    invoiceDetails.setInvLastModifiedBy(adUser.getUsrName());
                }

            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_002);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_002_MSG);
                return apiResponse;
            }

            // Customer Code
            try {
                if (invoiceMemoLineRequest.getCustomerCode() == null || invoiceMemoLineRequest.getCustomerCode().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return apiResponse;
                }
                arCustomer = arCustomerHome.findByCstCustomerCode(invoiceMemoLineRequest.getCustomerCode(),
                        invoiceDetails.getInvAdCompany(), invoiceDetails.getCompanyShortName());
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_009);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_009_MSG);
                return apiResponse;
            }

            // Currency
            try {
                if (invoiceMemoLineRequest.getCurrency() == null || invoiceMemoLineRequest.getCurrency().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_071);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_071_MSG);
                    return apiResponse;
                }
                functionalCurrency = glFunctionalCurrencyHome.findByFcName(invoiceMemoLineRequest.getCurrency(),
                        invoiceDetails.getInvAdCompany(), invoiceDetails.getCompanyShortName());
                if (functionalCurrency != null) {
                    invoiceDetails.setCurrencyCode(functionalCurrency.getFcName());
                }
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
                return apiResponse;
            }

            // Tax Code
            try {
                if (invoiceMemoLineRequest.getTaxCode() != null) {
                    if (!invoiceMemoLineRequest.getTaxCode().isEmpty()) {
                        taxCode = arTaxCodeHome.findByTcName(invoiceMemoLineRequest.getTaxCode(),
                                invoiceDetails.getInvAdCompany(), invoiceDetails.getCompanyShortName());
                    }
                }
                invoiceDetails.setTaxCode((taxCode == null) ? "NONE" : taxCode.getTcName());
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
                return apiResponse;
            }

            // Withholding Tax Code
            try {
                if (invoiceMemoLineRequest.getWithholdingTaxCode() != null) {
                    if (!invoiceMemoLineRequest.getWithholdingTaxCode().isEmpty()) {
                        withholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName(invoiceMemoLineRequest.getWithholdingTaxCode(),
                                invoiceDetails.getInvAdCompany(), invoiceDetails.getCompanyShortName());
                    }
                }
                invoiceDetails.setWithholdingTaxCode((withholdingTaxCode == null) ? "NONE" : withholdingTaxCode.getWtcName());
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
                return apiResponse;
            }

            // Payment Term
            try {
                if (invoiceMemoLineRequest.getPaymentTerm() != null) {
                    if (!invoiceMemoLineRequest.getPaymentTerm().isEmpty()) {
                        adPaymentTerm = adPaymentTermHome.findByPytName(invoiceMemoLineRequest.getPaymentTerm(),
                                invoiceDetails.getInvAdCompany(), invoiceDetails.getCompanyShortName());
                    }
                }
                invoiceDetails.setPaymentTerm((adPaymentTerm == null) ? "IMMEDIATE" : adPaymentTerm.getPytName());
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
                return apiResponse;
            }

            double totalAmount = 0;
            ArrayList<ArModInvoiceLineDetails> memoLines = new ArrayList<ArModInvoiceLineDetails>();

            for (MemolineDetails memoLineDetails : invoiceMemoLineRequest.getMemoLineDetails()) {
                ArModInvoiceLineDetails details = new ArModInvoiceLineDetails();
                details.setIlSmlName(memoLineDetails.getMemoLineName());
                details.setIlQuantity(1); // Default for Omega ERP purposes
                details.setIlUnitPrice(memoLineDetails.getApplyAmount());
                details.setIlAmount(memoLineDetails.getApplyAmount());
                details.setIlTax(EJBCommon.TRUE); // Default tax included
                memoLines.add(details);
            }

            invoiceDetails.setInvCreditMemo(EJBCommon.FALSE); // Default invoice transaction
            invoiceDetails.setInvType("MEMO LINES"); // Default invoice type
            invoiceDetails.setInvDate(invoiceDate);
            invoiceDetails.setInvReferenceNumber(invoiceMemoLineRequest.getReferenceNumber());
            invoiceDetails.setInvVoid(EJBCommon.FALSE);
            invoiceDetails.setInvDescription(invoiceMemoLineRequest.getDescription());
            invoiceDetails.setInvDebitMemo(EJBCommon.FALSE); //TODO: Verify the redundancy of this field against InvCreditMemo field
            invoiceDetails.setInvSubjectToCommission(EJBCommon.FALSE); //TODO: What is the purpose of this field in Omega ERP
            invoiceDetails.setInvEffectivityDate(invoiceDate);
            invoiceDetails.setInvReceiveDate(invoiceDate);
            invoiceDetails.setInvDateCreated(new java.util.Date());
            invoiceDetails.setInvDateLastModified(new java.util.Date());
            invoiceDetails.setInvAmountDue(totalAmount);
            invoiceDetails.setReportParameter(""); //TODO: What is the purpose of this field in Omega ERP
            invoiceDetails.setCustomerCode(arCustomer.getCstCustomerCode());

            Integer invoiceCode = this.saveInvoiceMemoLines(invoiceDetails, memoLines);
            arInvoice = arInvoiceHome.findByPrimaryKey(invoiceCode, invoiceDetails.getCompanyShortName());
            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            apiResponse.setDocumentNumber(arInvoice.getInvNumber());
            apiResponse.setStatus("Created invoice memo line(s) transaction successfully.");

        }
        catch (ArInvoiceStandardMemolineDoesNotExist ex) {
            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_080);
            apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_080_MSG, ex.getMessage()));
            return apiResponse;
        }
        catch (GlobalPaymentTermInvalidException ex) {
            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_012);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_012_MSG);
            return apiResponse;
        }
        catch (ArINVAmountExceedsCreditLimitException ex) {
            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_013);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_013_MSG);
            return apiResponse;
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
        catch (GlobalBranchAccountNumberInvalidException ex) {
            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_018);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_018_MSG);
            return apiResponse;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return apiResponse;
        }

        return apiResponse;
    }

    @Override
    public Integer saveInvoiceItems(ArInvoiceDetails invoiceDetails, ArrayList<ArModInvoiceLineItemDetails> invoiceLines)
            throws GlobalInvItemLocationNotFoundException, GlobalInventoryDateException, GlobalMiscInfoIsRequiredException,
            GlobalBranchAccountNumberInvalidException, GlobalRecordInvalidException, ArINVAmountExceedsCreditLimitException,
            GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException,
            GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException,
            AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException, GlobalPaymentTermInvalidException,
            GlobalNoRecordFoundException, GlobalInvItemCostingNotFoundException {

        Debug.print("ArInvoiceEntryApiControllerBean saveInvoiceItems");

        Integer companyCode = invoiceDetails.getInvAdCompany();
        Integer branchCode = invoiceDetails.getInvAdBranch();
        String companyShortName = invoiceDetails.getCompanyShortName();

        try {
            LocalArInvoice arInvoice;
            String documentType = invoiceDetails.getInvDocumentType() == null ? "AR INVOICE" : invoiceDetails.getInvDocumentType();

            generateDocumentNumber(invoiceDetails, companyCode, branchCode, documentType);

            // validate if payment term has at least one payment schedule
            LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName(invoiceDetails.getPaymentTerm(), companyCode);
            if (adPaymentTerm != null && adPaymentTerm.getAdPaymentSchedules().isEmpty()) {
                throw new GlobalPaymentTermInvalidException();
            }

            arInvoice = arInvoiceHome
                    .InvType(invoiceDetails.getInvType())
                    .InvDescription(invoiceDetails.getInvDescription())
                    .InvDate(invoiceDetails.getInvDate())
                    .InvNumber(invoiceDetails.getInvNumber())
                    .InvReferenceNumber(invoiceDetails.getInvReferenceNumber())
                    .InvCmInvoiceNumber(null)
                    .InvCmReferenceNumber(null)
                    .InvCreditMemo(EJBCommon.FALSE)
                    .InvEffectivityDate(invoiceDetails.getInvEffectivityDate())
                    .InvConversionRate(1.0)
                    .InvCreatedBy(invoiceDetails.getInvCreatedBy())
                    .InvDateCreated(invoiceDetails.getInvDateCreated())
                    .InvAdBranch(branchCode)
                    .InvAdCompany(companyCode)
                    .buildInvoice(companyShortName);

            arInvoice.setInvDocumentType(invoiceDetails.getInvDocumentType());
            arInvoice.setAdPaymentTerm(adPaymentTerm);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(invoiceDetails.getCurrencyCode(), companyCode, companyShortName);
            arInvoice.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName(invoiceDetails.getTaxCode(), companyCode, companyShortName);
            arInvoice.setArTaxCode(arTaxCode);

            LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName(invoiceDetails.getWithholdingTaxCode(), companyCode, companyShortName);
            arInvoice.setArWithholdingTaxCode(arWithholdingTaxCode);

            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(invoiceDetails.getCustomerCode(), companyCode, companyShortName);
            arInvoice.setArCustomer(arCustomer);

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode, companyShortName);
            double amountDue = 0;

            // add new invoice line item and distribution record
            double totalTaxAmount = 0d;
            double totalDiscountTaxAmount = 0d;
            double totalAmount = 0d;
            double totalDiscount = 0d;

            double totalInterestExceptionAmount = 0d;
            double totalPaymentTermExceptionAmount = 0d;
            double totalDownpayment = arInvoice.getInvDownPayment();

            // Make sure Global AD Preference apply validation to avoid this fall back value.
            String centralWarehouse = !adPreference.getPrfInvCentralWarehouse().equals("") &&
                    adPreference.getPrfInvCentralWarehouse() != null ?
                    adPreference.getPrfInvCentralWarehouse() : "POM WAREHOUSE LOCATION";
            LocalAdBranch centralWarehouseBranchCode = adBranchHome.findByBrName(centralWarehouse, companyCode, companyShortName);

            if (centralWarehouseBranchCode == null) {
                throw new Exception();
            }

            Iterator itemList = invoiceLines.iterator();
            LocalInvItemLocation itemLocation;

            while (itemList.hasNext()) {

                ArModInvoiceLineItemDetails lineDetails = (ArModInvoiceLineItemDetails) itemList.next();

                itemLocation = getItemLocation(companyCode, lineDetails);
                isPriorDateAllowed(companyCode, branchCode, arInvoice, adPreference, itemLocation);

                LocalArInvoiceLineItem arInvoiceLineItem = this.addArIliEntry(lineDetails, arInvoice, itemLocation, companyCode, companyShortName);

                // Get average costing from Main Warehouse only
                double COST = getItemCosting(companyCode, centralWarehouseBranchCode.getBrCode(), arInvoice, itemLocation, arInvoiceLineItem);
                double quantitySold = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(),
                        arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode, companyShortName);

                LocalAdBranchItemLocation adBranchItemLocation = null;
                try {
                    adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arInvoiceLineItem.getInvItemLocation().getIlCode(), branchCode, companyCode);
                }
                catch (FinderException ex) {
                }

                String distributionRecordClass = getDistributionRecordClass(companyCode, adBranchItemLocation);
                costOfGoodsSoldJournals(companyCode, branchCode, arInvoice, adPreference, itemLocation, arInvoiceLineItem, COST,
                        quantitySold, adBranchItemLocation, companyShortName);
                inventoryRevenueJournals(companyCode, branchCode, arInvoice, arInvoiceLineItem, adBranchItemLocation, distributionRecordClass, companyShortName);

                if (lineDetails.getIliTotalDiscount() > 0) {
                    discountJournals(companyCode, branchCode, arInvoice, lineDetails, companyShortName);
                    totalDiscount += lineDetails.getIliTotalDiscount();
                }

                totalAmount += arInvoiceLineItem.getIliAmount();

                if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiInterestException() == EJBCommon.TRUE) {
                    totalInterestExceptionAmount += arInvoiceLineItem.getIliAmount();
                }
                if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiPaymentTermException() == EJBCommon.TRUE) {
                    totalPaymentTermExceptionAmount += arInvoiceLineItem.getIliAmount();
                }
                totalTaxAmount += arInvoiceLineItem.getIliTaxAmount();
            }
            double totalConvertedTaxAmount = this.convertForeignToFunctionalCurrency(arInvoice.getInvConversionRate(),
                    totalTaxAmount, companyCode, invoiceDetails.getCompanyShortName());
            if (!arTaxCode.getTcType().equals("NONE") && !arTaxCode.getTcType().equals("EXEMPT")) {
                if (arTaxCode.getTcInterimAccount() == null) {
                    LocalAdBranchArTaxCode adBranchTaxCode = null;
                    try {
                        adBranchTaxCode = adBranchArTaxCodeHome.findBtcByTcCodeAndBrCode(arInvoice.getArTaxCode().getTcCode(), branchCode, companyCode);
                    }
                    catch (FinderException ex) {
                        Debug.print(ex.getMessage());
                    }
                    this.addArDrEntry(arInvoice.getArDrNextLine(), TAX, EJBCommon.FALSE, totalConvertedTaxAmount,
                            (adBranchTaxCode != null) ? adBranchTaxCode.getBtcGlCoaTaxCode() : arTaxCode.getGlChartOfAccount().getCoaCode(),
                            null, arInvoice, (adBranchTaxCode != null) ? branchCode : centralWarehouseBranchCode.getBrCode(), companyCode, companyShortName);
                } else {
                    this.addArDrEntry(arInvoice.getArDrNextLine(), "DEFERRED TAX", EJBCommon.FALSE, totalConvertedTaxAmount,
                            arTaxCode.getTcInterimAccount(), null, arInvoice, branchCode, companyCode, companyShortName);
                }
            }

            // add unearned interest
            double unearnedInterestAmount = 0d;
            unearnedInterestAmount = getUnearnedInterestAmount(companyCode, branchCode, arInvoice, adPaymentTerm,
                    totalTaxAmount, totalAmount, totalInterestExceptionAmount, totalDownpayment, unearnedInterestAmount, companyShortName);
            arInvoice.setInvAmountUnearnedInterest(unearnedInterestAmount);

            // add wtax distribution if necessary
            double withholdingTaxAmount = 0d;

            if (arWithholdingTaxCode.getWtcRate() != 0 && adPreference.getPrfArWTaxRealization().equals(INVOICE)) {
                withholdingTaxAmount = EJBCommon.roundIt(totalAmount * (arWithholdingTaxCode.getWtcRate() / 100),
                        this.getGlFcPrecisionUnit(companyCode, companyShortName));
                double convertedWithholdingTax = this.convertForeignToFunctionalCurrency(arInvoice.getInvConversionRate(),
                        withholdingTaxAmount, companyCode, invoiceDetails.getCompanyShortName());
                this.addArDrEntry(arInvoice.getArDrNextLine(), W_TAX, EJBCommon.TRUE, convertedWithholdingTax,
                        arWithholdingTaxCode.getGlChartOfAccount().getCoaCode(), null, arInvoice, branchCode, companyCode, companyShortName);
            }

            amountDue = totalAmount + totalTaxAmount - withholdingTaxAmount - totalDiscount;

            // add receivable distribution
            try {
                LocalAdBranchCustomer adBranchCustomer = adBranchCustomerHome.findBcstByCstCodeAndBrCode(arInvoice.getArCustomer().getCstCode(), branchCode, companyCode);
                double convertedAmountDue = this.convertForeignToFunctionalCurrency(arInvoice.getInvConversionRate(), amountDue,
                        companyCode, invoiceDetails.getCompanyShortName());

                this.addArDrIliEntry(arInvoice.getArDrNextLine(), RECEIVABLE, EJBCommon.TRUE, convertedAmountDue,
                        adBranchCustomer.getBcstGlCoaReceivableAccount(), arInvoice, branchCode, companyCode, invoiceDetails.getCompanyShortName());
            }
            catch (FinderException ex) {
            }

            // compute invoice amount due set invoice amount due inclusive interest
            arInvoice.setInvAmountDue(amountDue);

            // create invoice payment schedule
            invoicePaymentSchedule(companyCode, arInvoice, adPaymentTerm, totalTaxAmount, totalAmount,
                    totalPaymentTermExceptionAmount, totalDownpayment, companyShortName);

            // TODO: This functionality is not yet setup and implemented correctly.
            // The default UOM of each items are all setup to EACH with conversion factor are all set to 1.
            // Check the convertByUomAndQuantity function this is private method in this class
            // -- Check Insufficient Stock
            if (adPreference.getPrfArCheckInsufficientStock() == EJBCommon.TRUE) {
                boolean hasInsufficientItems = false;
                StringBuilder insufficientItems = new StringBuilder();

                Collection arInvoiceLineItems = arInvoiceLineItemHome.findByInvCode(arInvoice.getInvCode(), companyCode);
                for (Object invoiceLineItem : arInvoiceLineItems) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) invoiceLineItem;
                    // -- Skip non-inventoriable
                    if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiNonInventoriable() == EJBCommon.TRUE) {
                        continue;
                    }

                    double ILI_QTY = this.convertByUomAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(),
                            arInvoiceLineItem.getInvItemLocation().getInvItem(), Math.abs(arInvoiceLineItem.getIliQuantity()), companyCode);

                    LocalInvCosting invCosting = invCostingHome.getItemAverageCost(arInvoice.getInvDate(),
                            arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName(),
                            arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName(), branchCode, companyCode);

                    double LOWEST_QTY = this.convertByUomAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(),
                            arInvoiceLineItem.getInvItemLocation().getInvItem(), 1, companyCode);

                    if ((invCosting == null || invCosting.getCstRemainingQuantity() == 0 ||
                            invCosting.getCstRemainingQuantity() - ILI_QTY <= -LOWEST_QTY) &&
                            arInvoiceLineItem.getInvItemLocation().getInvItem().getIiNonInventoriable() == (byte) 0) {
                        hasInsufficientItems = true;
                        insufficientItems.append(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName()).append(", ");
                    }
                }
                if (hasInsufficientItems) {
                    throw new GlobalRecordInvalidException(insufficientItems.substring(0, insufficientItems.lastIndexOf(",")));
                }
            }

            // validate if total amount + unposted invoices' amount + current balance + unposted receipts's amount does not exceed customer's credit limit
            double balance = 0;
            if (arCustomer.getCstCreditLimit() > 0) {
                balance = computeTotalBalance(invoiceDetails.getInvCode(), invoiceDetails.getCustomerCode(), companyCode);
                balance += amountDue;
                if (arCustomer.getCstCreditLimit() < balance) {
                    throw new ArINVAmountExceedsCreditLimitException();
                }
            }

            this.executeArInvPost(arInvoice, branchCode, companyCode, companyShortName);
            // set invoice approval status
            arInvoice.setInvApprovalStatus("N/A");
            return arInvoice.getInvCode();

        }
        catch (GlobalPaymentTermInvalidException ex) {
            getSessionContext().setRollbackOnly();
            throw ex;
        }
        catch (GlobalRecordInvalidException ex) {
            getSessionContext().setRollbackOnly();
            throw ex;
        }
        catch (GlobalInventoryDateException ex) {
            getSessionContext().setRollbackOnly();
            throw ex;
        }
        catch (GlobalBranchAccountNumberInvalidException ex) {
            getSessionContext().setRollbackOnly();
            throw ex;
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Integer saveInvoiceMemoLines(ArInvoiceDetails invoiceDetails, ArrayList<ArModInvoiceLineDetails> invoiceLines)
            throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException,
            GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, ArINVAmountExceedsCreditLimitException,
            GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException,
            GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException,
            GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException,
            GlobalBranchAccountNumberInvalidException, ArInvDuplicateUploadNumberException,
            ArInvoiceStandardMemolineDoesNotExist {

        Debug.print("ArInvoiceEntryApiControllerBean saveInvoice");

        Integer companyCode = invoiceDetails.getInvAdCompany();
        Integer branchCode = invoiceDetails.getInvAdBranch();
        String companyShortName = invoiceDetails.getCompanyShortName();

        try {
            LocalArInvoice arInvoice;
            String documentType = invoiceDetails.getInvDocumentType() == null ? "AR INVOICE" : invoiceDetails.getInvDocumentType();

            generateDocumentNumber(invoiceDetails, companyCode, branchCode, documentType);

            // create invoice
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(invoiceDetails.getInvDate());
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
            calendar.add(Calendar.DAY_OF_MONTH, 1);

            invoiceDetails.setInvInterestNextRunDate(calendar.getTime());

            arInvoice = arInvoiceHome.InvType(
                    invoiceDetails.getInvType())
                    .InvDescription(invoiceDetails.getInvDescription())
                    .InvDate(invoiceDetails.getInvDate())
                    .InvNumber(invoiceDetails.getInvNumber())
                    .InvReferenceNumber(invoiceDetails.getInvReferenceNumber())
                    .InvEffectivityDate(invoiceDetails.getInvEffectivityDate())
                    .InvConversionRate(1.0)
                    .InvCreatedBy(invoiceDetails.getInvCreatedBy())
                    .InvDateCreated(invoiceDetails.getInvDateCreated())
                    .InvAdBranch(branchCode)
                    .InvAdCompany(companyCode)
                    .buildInvoice(companyShortName);

            arInvoice.setReportParameter(invoiceDetails.getReportParameter());
            arInvoice.setInvDocumentType(invoiceDetails.getInvDocumentType());

            LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName(invoiceDetails.getPaymentTerm(), companyCode, companyShortName);
            arInvoice.setAdPaymentTerm(adPaymentTerm);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(invoiceDetails.getCurrencyCode(), companyCode, companyShortName);
            arInvoice.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName(invoiceDetails.getTaxCode(), companyCode, companyShortName);
            arInvoice.setArTaxCode(arTaxCode);
            LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName(invoiceDetails.getWithholdingTaxCode(), companyCode, companyShortName);

            arInvoice.setArWithholdingTaxCode(arWithholdingTaxCode);

            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(invoiceDetails.getCustomerCode(), companyCode, companyShortName);
            arInvoice.setArCustomer(arCustomer);

            // add new invoice lines and distribution record
            double totalTaxAmount = 0d;
            double totalAmount = 0d;
            double totalUntaxableAmount = 0d;

            for (Object o : invoiceLines) {

                ArModInvoiceLineDetails mInvDetails = (ArModInvoiceLineDetails) o;
                LocalArInvoiceLine arInvoiceLine = this.addArIlEntry(mInvDetails, arInvoice, companyCode, companyShortName);

                double itemAmount = this.convertForeignToFunctionalCurrency(arInvoice.getInvConversionRate(),
                        arInvoiceLine.getIlAmount(), companyCode, companyShortName);

                // add revenue/credit distributions
                this.addArDrEntry(arInvoice.getArDrNextLine(), REVENUE, EJBCommon.FALSE, itemAmount,
                        this.getArGlCoaRevenueAccount(arInvoiceLine, branchCode, companyCode, companyShortName),
                        null, arInvoice, branchCode, companyCode, companyShortName);

                totalAmount += arInvoiceLine.getIlAmount();
                totalTaxAmount += arInvoiceLine.getIlTaxAmount();

                if (arInvoiceLine.getIlTax() == EJBCommon.FALSE) {
                    totalUntaxableAmount += arInvoiceLine.getIlAmount();
                }
            }

            // add tax distribution if necessary
            if (!arTaxCode.getTcType().equals("NONE") && !arTaxCode.getTcType().equals("EXEMPT")) {
                double totalConvertedTaxAmount = this.convertForeignToFunctionalCurrency(arInvoice.getInvConversionRate(),
                        totalTaxAmount, companyCode, companyShortName);
                if (arTaxCode.getTcInterimAccount() == null) {
                    // add branch tax code
                    LocalAdBranchArTaxCode adBranchTaxCode;
                    try {
                        adBranchTaxCode = adBranchArTaxCodeHome.findBtcByTcCodeAndBrCode(arInvoice.getArTaxCode().getTcCode(), branchCode, companyCode, companyShortName);
                        this.addArDrEntry(arInvoice.getArDrNextLine(), TAX, EJBCommon.FALSE, totalConvertedTaxAmount,
                                adBranchTaxCode.getBtcGlCoaTaxCode(), null, arInvoice, branchCode, companyCode, companyShortName);
                    }
                    catch (FinderException ex) {
                        this.addArDrEntry(arInvoice.getArDrNextLine(), TAX, EJBCommon.FALSE, totalConvertedTaxAmount,
                                arTaxCode.getGlChartOfAccount().getCoaCode(), null, arInvoice, branchCode, companyCode, companyShortName);
                    }
                } else {
                    this.addArDrEntry(arInvoice.getArDrNextLine(), "DEFERRED TAX", EJBCommon.FALSE, totalConvertedTaxAmount,
                            arTaxCode.getTcInterimAccount(), null, arInvoice, branchCode, companyCode, companyShortName);
                }
            }

            // add withholding distribution if necessary
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode, companyShortName);
            double withholdingTaxAmount = 0d;
            if (arWithholdingTaxCode.getWtcRate() != 0 && adPreference.getPrfArWTaxRealization().equals(INVOICE)) {
                withholdingTaxAmount = EJBCommon.roundIt((totalAmount - totalUntaxableAmount) * (arWithholdingTaxCode.getWtcRate() / 100),
                        this.getGlFcPrecisionUnit(companyCode, companyShortName));
                double W_TAX_AMOUNT_CONV = this.convertForeignToFunctionalCurrency(arInvoice.getInvConversionRate(), withholdingTaxAmount, companyCode, companyShortName);
                this.addArDrEntry(arInvoice.getArDrNextLine(), W_TAX, EJBCommon.TRUE, W_TAX_AMOUNT_CONV,
                        arWithholdingTaxCode.getGlChartOfAccount().getCoaCode(), null, arInvoice, branchCode, companyCode, companyShortName);
            }

            // add receivable distribution
            double amountDue = totalAmount + totalTaxAmount - withholdingTaxAmount;
            try {
                LocalAdBranchCustomer adBranchCustomer = adBranchCustomerHome.findBcstByCstCodeAndBrCode(arInvoice.getArCustomer().getCstCode(),
                        branchCode, companyCode, companyShortName);
                double convertedAmountDue = this.convertForeignToFunctionalCurrency(arInvoice.getInvConversionRate(), amountDue, companyCode, companyShortName);
                this.addArDrEntry(arInvoice.getArDrNextLine(), RECEIVABLE, EJBCommon.TRUE, convertedAmountDue,
                        adBranchCustomer.getBcstGlCoaReceivableAccount(), null, arInvoice, branchCode, companyCode, companyShortName);
            }
            catch (FinderException ex) {
            }

            // Compute and set invoice amount due
            arInvoice.setInvAmountDue(amountDue);

            // Invoice payment schedule
            invoicePaymentSchedule(companyCode, arInvoice, adPaymentTerm, totalTaxAmount, totalAmount, 0d, 0d, companyShortName);

            // Invoice posting process
            this.executeArInvPost(arInvoice, branchCode, companyCode, companyShortName);

            // Approval process
            String invoiceApprovalStatus = "N/A";
            if (!invoiceDetails.getIsDraft()) {
                arInvoice.setInvApprovalStatus(invoiceApprovalStatus);
            } else {
                String customerCode = arInvoice.getArCustomer().getCstCustomerCode();
                String invoiceLastModifiedBy = arInvoice.getInvLastModifiedBy();
                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode, companyShortName);
                // validate if amount due + un-posted invoices + current balance + un-posted receipts does not exceed customer's credit limit
                double balance = 0;
                LocalAdApprovalDocument adInvoiceApprovalDocument = adApprovalDocumentHome.findByAdcType(EJBCommon.AR_INVOICE, companyCode);
                if (arCustomer.getCstCreditLimit() > 0) {
                    balance = computeTotalBalance(arInvoice.getInvCode(), customerCode, companyCode);
                    balance += amountDue;
                    if (arCustomer.getCstCreditLimit() < balance && (adApproval.getAprEnableArInvoice() == EJBCommon.FALSE || (adApproval.getAprEnableArInvoice() == EJBCommon.TRUE && adInvoiceApprovalDocument.getAdcEnableCreditLimitChecking() == EJBCommon.FALSE))) {
                        throw new ArINVAmountExceedsCreditLimitException();
                    }
                }

                // find overdue invoices
                Collection arOverdueInvoices = arInvoicePaymentScheduleHome.findOverdueIpsByInvDateAndCstCustomerCode(arInvoice.getInvDate(), customerCode, companyCode);

                // check if ar invoice approval is enabled
                if (adApproval.getAprEnableArInvoice() != EJBCommon.FALSE || (arCustomer.getCstCreditLimit() < balance && arOverdueInvoices.size() > 0)) {
                    // check if invoice is self approved
                    LocalAdUser adUser = adUserHome.findByUsrName(invoiceLastModifiedBy, companyCode);
                    //TODO: Continue code review of this process
//                    invoiceApprovalStatus = arApprovalController.getApprovalStatus(adUser.getUsrDept(),
//                            invoiceLastModifiedBy, adUser.getUsrDescription(), EJBCommon.AR_INVOICE,
//                            arInvoice.getInvCode(), arInvoice.getInvNumber(), arInvoice.getInvDate(),
//                            branchCode, companyCode);
                }
            }

            return arInvoice.getInvCode();

        }
        catch (GlobalRecordAlreadyDeletedException | GlobalBranchAccountNumberInvalidException |
               GlobalJournalNotBalanceException | GlJREffectiveDatePeriodClosedException |
               GlJREffectiveDateNoPeriodExistException | GlobalTransactionAlreadyVoidException |
               ArInvoiceStandardMemolineDoesNotExist | GlobalTransactionAlreadyPostedException ex) {
            getSessionContext().setRollbackOnly();
            throw ex;
        }
        catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private String getDistributionRecordClass(Integer companyCode, LocalAdBranchItemLocation adBranchItemLocation) {

        LocalGlChartOfAccount glSalesCoa;
        LocalGenValueSetValue genValueSetValue = null;

        try {
            glSalesCoa = glChartOfAccountHome.findByCoaCode(adBranchItemLocation.getBilCoaGlSalesAccount(), companyCode);
            genValueSetValue = genValueSetValueHome.findByVsvValue(glSalesCoa.getCoaSegment2(), companyCode);
        }
        catch (FinderException ex) {
        }

        String distributionRecordClass = REVENUE;
        if (genValueSetValue.getVsvDescription().equalsIgnoreCase("3G DEFERRED REVENUE") || genValueSetValue.getVsvDescription().equalsIgnoreCase("4G DEFERRED REVENUE")) {
            distributionRecordClass = LIABILITY;
        }
        return distributionRecordClass;
    }

    private void discountJournals(Integer companyCode, Integer branchCode, LocalArInvoice arInvoice,
                                  ArModInvoiceLineItemDetails lineDetails, String companyShortName)
            throws GlobalBranchAccountNumberInvalidException, FinderException {

        LocalAdBranch branch = null;
        try {
            branch = adBranchHome.findByPrimaryKey(branchCode);
        }
        catch (FinderException ex) {

        }

        Integer discountCoa = null;
        try {
            List<LocalGlChartOfAccount> defaultCoa = new ArrayList(glChartOfAccountHome.findHoCoaAllByCoaCategory("DISCOUNT", branch.getBrName(), companyCode));

            for (LocalGlChartOfAccount chartOfAccount : defaultCoa) {

                // Get only DISCOUNT with REVENUE class type
                if (chartOfAccount.getCoaAccountType().equals(REVENUE)) {
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

        LocalArTaxCode arTaxCode = arInvoice.getArTaxCode();

        // calculate net amount
        itemDiscountAmount = EJBCommon.calculateNetAmount(lineDetails.getIliTotalDiscount(), lineDetails.getIliTax(), arTaxCode.getTcRate(), arTaxCode.getTcType(), precisionUnit);

        this.addArDrEntry(arInvoice.getArDrNextLine(), "SALES DISCOUNT", EJBCommon.TRUE, itemDiscountAmount, discountCoa, null, arInvoice, branchCode, companyCode, companyShortName);

        // calculate tax
        itemDiscountTaxAmount = EJBCommon.calculateTaxAmount(lineDetails.getIliTotalDiscount(), lineDetails.getIliTax(), arTaxCode.getTcRate(), arTaxCode.getTcType(), itemDiscountAmount, precisionUnit);

        LocalAdBranchArTaxCode adBranchTaxCode = null;

        try {
            //TODO: Review this finder method that is causing finder exception
            adBranchTaxCode = adBranchArTaxCodeHome.findBtcByTcCodeAndBrCode(arInvoice.getArTaxCode().getTcCode(), branchCode, companyCode);
            this.addArDrEntry(arInvoice.getArDrNextLine(), TAX, EJBCommon.TRUE, itemDiscountTaxAmount, adBranchTaxCode.getBtcGlCoaTaxCode(), null, arInvoice, branchCode, companyCode, companyShortName);
        }
        catch (FinderException ex) {
            this.addArDrEntry(arInvoice.getArDrNextLine(), TAX, EJBCommon.TRUE, itemDiscountTaxAmount, arTaxCode.getGlChartOfAccount().getCoaCode(), null, arInvoice, branchCode, companyCode, companyShortName);
        }
    }

    private void invoicePaymentSchedule(Integer companyCode, LocalArInvoice arInvoice, LocalAdPaymentTerm adPaymentTerm,
                                        double totalTaxAmount, double totalAmount, double totalPaymentTermExceptionAmount,
                                        double totalDownpayment, String companyShortName) throws CreateException {

        short precisionUnit = this.getGlFcPrecisionUnit(companyCode, companyShortName);
        double totalPaymentSchedule = 0d;
        double totalPaymentInt = 0d;
        double totalPaymentScheduleInt = totalAmount + totalTaxAmount - totalDownpayment;

        GregorianCalendar gcPrevDateDue = new GregorianCalendar();
        GregorianCalendar gcDateDue = new GregorianCalendar();
        gcPrevDateDue.setTime(arInvoice.getInvEffectivityDate());

        Collection adPaymentSchedules = adPaymentTerm.getAdPaymentSchedules();
        boolean first = true;

        Iterator i = adPaymentSchedules.iterator();
        while (i.hasNext()) {
            LocalAdPaymentSchedule adPaymentSchedule = (LocalAdPaymentSchedule) i.next();
            // get date due
            switch (arInvoice.getAdPaymentTerm().getPytScheduleBasis()) {
                case EJBCommon.MONTHLY:
                    gcDateDue = gcPrevDateDue;
                    gcDateDue.add(Calendar.MONTH, 1);
                    gcPrevDateDue = gcDateDue;
                    break;
                case EJBCommon.BI_MONTHLY:
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
                default:
                    gcDateDue.setTime(arInvoice.getInvEffectivityDate());
                    gcDateDue.add(Calendar.DATE, adPaymentSchedule.getPsDueDay());
                    break;
            }
            if (first == true) {
                LocalArInvoicePaymentSchedule arInvoicePaymentScheduleDownPayment;
                // Down payment
                if (arInvoice.getInvDownPayment() > 0) {
                    arInvoicePaymentScheduleDownPayment = arInvoicePaymentScheduleHome.IpsDueDate(arInvoice.getInvEffectivityDate()).IpsAmountDue(arInvoice.getInvDownPayment()).IpsPenaltyDueDate(arInvoice.getInvEffectivityDate()).IpsAdCompany(companyCode).buildInvoicePaymentSchedule(companyShortName);
                    arInvoice.addArInvoicePaymentSchedule(arInvoicePaymentScheduleDownPayment);
                }
                // Payment term exception amount
                if (totalPaymentTermExceptionAmount > 0) {
                    arInvoicePaymentScheduleDownPayment = arInvoicePaymentScheduleHome.IpsDueDate(arInvoice.getInvEffectivityDate()).IpsAmountDue(totalPaymentTermExceptionAmount).IpsPenaltyDueDate(arInvoice.getInvEffectivityDate()).IpsAdCompany(companyCode).buildInvoicePaymentSchedule(companyShortName);
                    arInvoice.addArInvoicePaymentSchedule(arInvoicePaymentScheduleDownPayment);
                }
                first = false;
            }

            // create a payment schedule
            double PAYMENT_SCHEDULE_AMOUNT;
            double PAYMENT_SCHEDULE_INT;
            double PAYMENT_SCHEDULE_PRINCIPAL;

            // if last payment schedule subtract to avoid rounding difference error
            if (i.hasNext()) {
                PAYMENT_SCHEDULE_AMOUNT = EJBCommon.roundIt((adPaymentSchedule.getPsRelativeAmount() / adPaymentTerm.getPytBaseAmount()) * (arInvoice.getInvAmountDue() - arInvoice.getInvDownPayment()), precisionUnit);
                PAYMENT_SCHEDULE_INT = EJBCommon.roundIt((totalPaymentScheduleInt) * (adPaymentTerm.getPytMonthlyInterestRate() / 100), precisionUnit);
                PAYMENT_SCHEDULE_PRINCIPAL = -(PAYMENT_SCHEDULE_AMOUNT) + PAYMENT_SCHEDULE_INT;
            } else {
                PAYMENT_SCHEDULE_AMOUNT = (arInvoice.getInvAmountDue() - arInvoice.getInvDownPayment()) - totalPaymentSchedule;
                PAYMENT_SCHEDULE_INT = EJBCommon.roundIt((totalPaymentScheduleInt) * (adPaymentTerm.getPytMonthlyInterestRate() / 100), precisionUnit);
                PAYMENT_SCHEDULE_PRINCIPAL = -(PAYMENT_SCHEDULE_AMOUNT) + PAYMENT_SCHEDULE_INT;
            }

            LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = arInvoicePaymentScheduleHome.IpsDueDate(gcDateDue.getTime()).IpsNumber(adPaymentSchedule.getPsLineNumber()).IpsAmountDue(PAYMENT_SCHEDULE_AMOUNT).IpsPenaltyDueDate(gcDateDue.getTime()).IpsAdCompany(companyCode).buildInvoicePaymentSchedule(companyShortName);
            arInvoicePaymentSchedule.setIpsInterestDue(PAYMENT_SCHEDULE_INT);
            arInvoice.addArInvoicePaymentSchedule(arInvoicePaymentSchedule);

            totalPaymentSchedule += PAYMENT_SCHEDULE_AMOUNT;
            totalPaymentInt += PAYMENT_SCHEDULE_INT;
            totalPaymentScheduleInt += PAYMENT_SCHEDULE_PRINCIPAL;
        }
    }

    private double getUnearnedInterestAmount(Integer companyCode, Integer branchCode, LocalArInvoice arInvoice,
                                             LocalAdPaymentTerm adPaymentTerm, double totalTaxAmount, double totalAmount,
                                             double totalInterestExceptionAmount, double totalDownpayment, double unearnedInterestAmount, String companyShortName) throws GlobalBranchAccountNumberInvalidException {

        if (arInvoice.getArCustomer().getCstAutoComputeInterest() == EJBCommon.TRUE && adPaymentTerm.getPytEnableInterest() == EJBCommon.TRUE) {

            try {
                double monthlyInterestRate = 0;
                monthlyInterestRate = adPaymentTerm.getPytMonthlyInterestRate();
                if (arInvoice.getArCustomer().getCstMonthlyInterestRate() > 0) {
                    monthlyInterestRate = arInvoice.getArCustomer().getCstMonthlyInterestRate();
                }

                LocalAdBranchCustomer adBranchCustomer = adBranchCustomerHome.findBcstByCstCodeAndBrCode(arInvoice.getArCustomer().getCstCode(), branchCode, companyCode);
                unearnedInterestAmount = EJBCommon.roundIt((totalAmount + totalTaxAmount - totalDownpayment - totalInterestExceptionAmount) * adPaymentTerm.getAdPaymentSchedules().size() * (monthlyInterestRate / 100), this.getGlFcPrecisionUnit(companyCode, companyShortName));
                double UNEARNED_INT_AMOUNT_CONV = this.convertForeignToFunctionalCurrency(arInvoice.getInvConversionRate(), unearnedInterestAmount, companyCode, companyShortName);
                this.addArDrEntry(arInvoice.getArDrNextLine(), "UNINTEREST", EJBCommon.FALSE, UNEARNED_INT_AMOUNT_CONV, adBranchCustomer.getBcstGlCoaUnEarnedInterestAccount(), null, arInvoice, branchCode, companyCode, companyShortName);
                this.addArDrIliEntry(arInvoice.getArDrNextLine(), "RECEIVABLE INTEREST", EJBCommon.TRUE, UNEARNED_INT_AMOUNT_CONV, adBranchCustomer.getBcstGlCoaReceivableAccount(), arInvoice, branchCode, companyCode, companyShortName);
            }
            catch (FinderException ex) {
            }
        }
        return unearnedInterestAmount;
    }

    private void inventoryRevenueJournals(Integer companyCode, Integer branchCode, LocalArInvoice arInvoice,
                                          LocalArInvoiceLineItem arInvoiceLineItem, LocalAdBranchItemLocation adBranchItemLocation, String distributionRecordClass, String companyShortName) throws GlobalBranchAccountNumberInvalidException {

        double convertedItemAmount = this.convertForeignToFunctionalCurrency(arInvoice.getInvConversionRate(), arInvoiceLineItem.getIliAmount(), companyCode, companyShortName);

        if (adBranchItemLocation != null) {
            if (adBranchItemLocation.getInvItemLocation().getInvItem().getIiServices() == EJBCommon.TRUE) {
                this.addArDrIliEntry(arInvoice.getArDrNextLine(), distributionRecordClass, EJBCommon.TRUE, convertedItemAmount, adBranchItemLocation.getBilCoaGlSalesAccount(), arInvoice, branchCode, companyCode, companyShortName);
                this.addArDrIliEntry(arInvoice.getArDrNextLine(), "OTHER", EJBCommon.FALSE, convertedItemAmount, adBranchItemLocation.getBilCoaGlSalesReturnAccount(), arInvoice, branchCode, companyCode, companyShortName);
            } else {
                this.addArDrIliEntry(arInvoice.getArDrNextLine(), distributionRecordClass, EJBCommon.FALSE, convertedItemAmount, adBranchItemLocation.getBilCoaGlSalesAccount(), arInvoice, branchCode, companyCode, companyShortName);
            }
        } else {
            if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiServices() == EJBCommon.TRUE) {
                this.addArDrIliEntry(arInvoice.getArDrNextLine(), distributionRecordClass, EJBCommon.TRUE, convertedItemAmount, arInvoiceLineItem.getInvItemLocation().getIlGlCoaSalesAccount(), arInvoice, branchCode, companyCode, companyShortName);
                this.addArDrIliEntry(arInvoice.getArDrNextLine(), "OTHER", EJBCommon.FALSE, convertedItemAmount, arInvoiceLineItem.getInvItemLocation().getIlGlCoaSalesReturnAccount(), arInvoice, branchCode, companyCode, companyShortName);
            } else {
                this.addArDrIliEntry(arInvoice.getArDrNextLine(), distributionRecordClass, EJBCommon.FALSE, convertedItemAmount, arInvoiceLineItem.getInvItemLocation().getIlGlCoaSalesAccount(), arInvoice, branchCode, companyCode, companyShortName);
            }
        }
    }

    private void costOfGoodsSoldJournals(Integer companyCode, Integer branchCode, LocalArInvoice arInvoice, LocalAdPreference adPreference, LocalInvItemLocation itemLocation, LocalArInvoiceLineItem arInvoiceLineItem, double COST, double quantitySold, LocalAdBranchItemLocation adBranchItemLocation, String companyShortName) throws GlobalBranchAccountNumberInvalidException {

        if (adPreference.getPrfArAutoComputeCogs() == EJBCommon.TRUE && arInvoiceLineItem.getInvItemLocation().getInvItem().getIiNonInventoriable() == EJBCommon.FALSE) {

            if (adBranchItemLocation != null) {
                this.addArDrIliEntry(arInvoice.getArDrNextLine(), COGS, EJBCommon.TRUE, COST * quantitySold, adBranchItemLocation.getBilCoaGlCostOfSalesAccount(), arInvoice, branchCode, companyCode, companyShortName);
                this.addArDrIliEntry(arInvoice.getArDrNextLine(), INVENTORY, EJBCommon.FALSE, COST * quantitySold, adBranchItemLocation.getBilCoaGlInventoryAccount(), arInvoice, branchCode, companyCode, companyShortName);
            } else {
                this.addArDrIliEntry(arInvoice.getArDrNextLine(), COGS, EJBCommon.TRUE, COST * quantitySold, arInvoiceLineItem.getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arInvoice, branchCode, companyCode, companyShortName);
                this.addArDrIliEntry(arInvoice.getArDrNextLine(), INVENTORY, EJBCommon.FALSE, COST * quantitySold, arInvoiceLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(), arInvoice, branchCode, companyCode, companyShortName);
            }

            // add quantity to item location committed quantity
            double convertedQuantity = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode, companyShortName);
            itemLocation.setIlCommittedQuantity(itemLocation.getIlCommittedQuantity() + convertedQuantity);
        }
    }

    private double getItemCosting(Integer companyCode, Integer branchCode, LocalArInvoice arInvoice, LocalInvItemLocation itemLocation, LocalArInvoiceLineItem arInvoiceLineItem) throws GlobalNoRecordFoundException {

        double COST = itemLocation.getInvItem().getIiUnitCost();

        try {
            LocalInvCosting invCosting = invCostingHome.getItemAverageCost(arInvoice.getInvDate(), itemLocation.getInvItem().getIiName(), itemLocation.getInvLocation().getLocName(), branchCode, companyCode);

            if (invCosting != null) {
                // Check if Remaining Value is not zero and Remaining Quantity is zero
                if (itemLocation.getInvItem().getIiNonInventoriable() == (byte) 0 && invCosting.getCstRemainingQuantity() <= 0 && invCosting.getCstRemainingValue() <= 0) {
                    HashMap criteria = new HashMap();
                    criteria.put("itemName", itemLocation.getInvItem().getIiName());
                    criteria.put("location", itemLocation.getInvLocation().getLocName());

                    ArrayList branchList = new ArrayList();

                    AdBranchDetails mdetails = new AdBranchDetails();
                    mdetails.setBrCode(branchCode);
                    branchList.add(mdetails);

                    ejbRIC.executeInvFixItemCosting(criteria, branchList, companyCode);

                    invCosting = invCostingHome.getItemAverageCost(arInvoice.getInvDate(), itemLocation.getInvItem().getIiName(), itemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                }

                if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals(Average)) {
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

    private void isPriorDateAllowed(Integer companyCode, Integer branchCode, LocalArInvoice arInvoice, LocalAdPreference adPreference, LocalInvItemLocation itemLocation) throws FinderException, GlobalInventoryDateException {

        Debug.print("ArInvoiceEntryApiControllerBean isPriorDateAllowed");

        if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
            Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(arInvoice.getInvDate(), itemLocation.getInvItem().getIiName(), itemLocation.getInvLocation().getLocName(), branchCode, companyCode);

            if (!invNegTxnCosting.isEmpty()) {
                throw new GlobalInventoryDateException(itemLocation.getInvItem().getIiName());
            }
        }
    }

    private LocalInvItemLocation getItemLocation(Integer companyCode, ArModInvoiceLineItemDetails lineDetails) throws GlobalInvItemLocationNotFoundException {

        LocalInvItemLocation itemLocation;
        try {
            itemLocation = invItemLocationHome.findByLocNameAndIiName(lineDetails.getIliLocName(), lineDetails.getIliIiName(), companyCode);
        }
        catch (FinderException ex) {
            throw new GlobalInvItemLocationNotFoundException(String.valueOf(lineDetails.getIliLine()));
        }
        return itemLocation;
    }

    private void generateDocumentNumber(ArInvoiceDetails invoiceDetails, Integer companyCode, Integer branchCode, String documentType) {

        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
        LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
        String companyShortName = invoiceDetails.getCompanyShortName();

        try {
            adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName(documentType, companyCode, companyShortName);
            adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome
                    .findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode, companyShortName);
        }
        catch (FinderException ex) {
        }

        if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' &&
                (invoiceDetails.getInvNumber() == null || invoiceDetails.getInvNumber().trim().length() == 0)) {

            if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                System.out.println("Next Document Sequence : " + adDocumentSequenceAssignment.getDsaNextSequence());

                try {
                    // Validate if the next invoice number sequence is already been used?
                    arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(),
                            EJBCommon.FALSE, branchCode, companyCode, companyShortName);
                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                }
                catch (FinderException ex) {
                    invoiceDetails.setInvNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                }
            } else {
                try {
                    arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(),
                            EJBCommon.FALSE, branchCode, companyCode, companyShortName);
                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                }
                catch (FinderException ex) {
                    invoiceDetails.setInvNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                }
            }
        }
    }

    private Integer getArGlCoaRevenueAccount(LocalArInvoiceLine arInvoiceLine, Integer branchCode, Integer companyCode, String companyShortName) {

        Debug.print("ArInvoiceLineEntryControllerBean getArGlCoaRevenueAccount");

        // generate revenue account
        try {

            StringBuilder GL_COA_ACCNT = new StringBuilder();
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode, companyShortName);
            LocalGenField genField = adCompany.getGenField();
            String FL_SGMNT_SPRTR = String.valueOf(genField.getFlSegmentSeparator());
            LocalAdBranchStandardMemoLine adBranchStandardMemoLine = null;

            try {
                adBranchStandardMemoLine = adBranchStandardMemoLineHome.findBSMLBySMLCodeAndBrCode(arInvoiceLine.getArStandardMemoLine().getSmlCode(), branchCode, companyCode, companyShortName);
            }
            catch (FinderException ex) {
            }

            Collection<LocalArAutoAccountingSegment> arAutoAccountingSegments = arAutoAccountingSegmentHome.findByAaAccountType(REVENUE, companyCode, companyShortName);
            for (LocalArAutoAccountingSegment autoAccountingSegment : arAutoAccountingSegments) {
                LocalGlChartOfAccount glChartOfAccount = null;
                if (autoAccountingSegment.getAasClassType().equals("AR CUSTOMER")) {
                    glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(arInvoiceLine.getArInvoice().getArCustomer().getCstGlCoaRevenueAccount(), companyShortName);
                    coaTokenizer(GL_COA_ACCNT, FL_SGMNT_SPRTR, autoAccountingSegment, glChartOfAccount);
                } else if (autoAccountingSegment.getAasClassType().equals("AR STANDARD MEMO LINE")) {
                    if (adBranchStandardMemoLine != null) {
                        try {
                            glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchStandardMemoLine.getBsmlGlCoaRevenueAccount(), companyShortName);
                        }
                        catch (FinderException ex) {
                        }
                    } else {
                        glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(arInvoiceLine.getArStandardMemoLine().getSmlGlCoaReceivableAccount(), companyShortName);
                    }
                    coaTokenizer(GL_COA_ACCNT, FL_SGMNT_SPRTR, autoAccountingSegment, glChartOfAccount);
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
                        glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchStandardMemoLine.getBsmlGlCoaRevenueAccount(), companyShortName);
                    }
                    catch (FinderException e) {
                    }
                    return glChartOfAccount.getCoaCode();
                } else {
                    return arInvoiceLine.getArStandardMemoLine().getSmlGlCoaRevenueAccount();
                }
            }
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void coaTokenizer(StringBuilder GL_COA_ACCNT, String FL_SGMNT_SPRTR, LocalArAutoAccountingSegment autoAccountingSegment, LocalGlChartOfAccount glChartOfAccount) {

        StringTokenizer st = new StringTokenizer(glChartOfAccount.getCoaAccountNumber(), FL_SGMNT_SPRTR);
        int ctr = 0;
        while (st.hasMoreTokens()) {
            ++ctr;
            if (ctr == autoAccountingSegment.getAasSegmentNumber()) {
                GL_COA_ACCNT.append(FL_SGMNT_SPRTR).append(st.nextToken());
                break;
            } else {
                st.nextToken();
            }
        }
    }

    private double convertByUomAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, double ADJST_QTY, Integer companyCode) {

        Debug.print("ArInvoiceEntryApiControllerBean convertByUomAndQuantity");
        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), companyCode);
            return EJBCommon.roundIt(ADJST_QTY * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), adPreference.getPrfInvQuantityPrecisionUnit());
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double computeTotalBalance(Integer invoiceCode, String customerCode, Integer companyCode) {

        Debug.print("ArInvoiceEntryApiControllerBean computeTotalBalance");
        double customerBalance = 0;
        try {
            // get latest balance
            Collection<LocalArCustomerBalance> customerBalances = arCustomerBalanceHome.findByCstCustomerCode(customerCode, companyCode);
            if (!customerBalances.isEmpty()) {
                List<LocalArCustomerBalance> customerBalanceList = new ArrayList<>(customerBalances);
                customerBalance = customerBalanceList.get(customerBalanceList.size() - 1).getCbBalance();
            }

            // get amount of unposted invoices/credit memos
            Collection arInvoices = arInvoiceHome.findUnpostedInvByCstCustomerCode(customerCode, companyCode);
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
            Collection arReceipts = arReceiptHome.findUnpostedRctByCstCustomerCode(customerCode, companyCode);
            for (Object receipt : arReceipts) {
                LocalArReceipt arReceipt = (LocalArReceipt) receipt;
                customerBalance = customerBalance - arReceipt.getRctAmount();
            }

            // get amount of pdc (unposted or posted) type PR findPdcByPdcType("PR",companyCode)
            Collection arPdcs = arPdcHome.findPdcByPdcType(companyCode);
            for (Object pdc : arPdcs) {
                LocalArPdc arPdc = (LocalArPdc) pdc;
                customerBalance = customerBalance - arPdc.getPdcAmount();
            }
        }
        catch (FinderException ex) {
        }
        return customerBalance;
    }

    private double getInvFifoCost(Date CST_DT, Integer IL_CODE, double CST_QTY, double CST_COST, boolean isAdjustFifo, Integer branchCode, Integer companyCode, String companyShortName) {

        Debug.print("ArInvoiceEntryApiControllerBean getInvFifoCost");
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
                        // if needed qty is not yet satisfied but no more quantities to fetch, get the default cost
                        if (neededQty != 0) {
                            LocalInvItemLocation itemLocation = invItemLocationHome.findByPrimaryKey(IL_CODE);
                            totalCost += (neededQty * itemLocation.getInvItem().getIiUnitCost());
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
                        return EJBCommon.roundIt(invFifoCosting.getCstItemCost() / invFifoCosting.getCstQuantityReceived(), this.getGlFcPrecisionUnit(companyCode, companyShortName));
                    } else if (invFifoCosting.getArInvoiceLineItem() != null) {
                        return EJBCommon.roundIt(invFifoCosting.getCstCostOfSales() / invFifoCosting.getCstQuantitySold(), this.getGlFcPrecisionUnit(companyCode, companyShortName));
                    } else {
                        return EJBCommon.roundIt(invFifoCosting.getCstAdjustCost() / invFifoCosting.getCstAdjustQuantity(), this.getGlFcPrecisionUnit(companyCode, companyShortName));
                    }
                }
            } else {
                // most applicable in 1st entries of data
                LocalInvItemLocation itemLocation = invItemLocationHome.findByPrimaryKey(IL_CODE);
                return itemLocation.getInvItem().getIiUnitCost();
            }
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer companyCode, String companyShortName) {

        Debug.print("ArInvoiceEntryApiControllerBean getGlFcPrecisionUnit");
        try {
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode, companyShortName);
            return adCompany.getGlFunctionalCurrency().getFcPrecision();
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void executeArInvPost(LocalArInvoice arInvoice, Integer branchCode, Integer companyCode, String companyShortName)
            throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException,
            GlobalTransactionAlreadyVoidException, GlJREffectiveDateNoPeriodExistException,
            GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException,
            GlobalBranchAccountNumberInvalidException,
            AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException {
        Debug.print("ArInvoiceEntryApiControllerBean executeArInvPost");
        String USR_NM = arInvoice.getInvLastModifiedBy();
        try {
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode, companyShortName);
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode, companyShortName);
            isInvoicePostedOrVoid(arInvoice);

            // -- Post invoice and adjust customer balance
            double INV_AMNT = arInvoice.getInvAmountDue();
            this.updateCustomerBalance(arInvoice.getInvDate(), INV_AMNT, arInvoice.getArCustomer(), companyCode, companyShortName);
            Collection arInvoiceLineItems = arInvoiceLineItemHome.findByInvCode(arInvoice.getInvCode(), companyCode, companyShortName);
            Collection arSalesOrderInvoiceLines = arInvoice.getArSalesOrderInvoiceLines();
            Collection arJobOrderInvoiceLines = arInvoice.getArJobOrderInvoiceLines();

            if (arInvoiceLineItems != null && !arInvoiceLineItems.isEmpty()) {
                for (Object invoiceLineItem : arInvoiceLineItems) {
                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) invoiceLineItem;
                    String II_NM = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName();
                    String LOC_NM = arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName();
                    double quantitySold = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(),
                            arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode, companyShortName);
                    LocalInvCosting invCosting = invCostingHome.getItemAverageCost(arInvoice.getInvDate(), II_NM, LOC_NM, branchCode, companyCode);
                    double COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();
                    if (invCosting == null) {
                        this.postToInv(arInvoiceLineItem, arInvoice.getInvDate(), quantitySold,
                                COST * quantitySold, -quantitySold, -COST * quantitySold, 0d, null, branchCode, companyCode);
                    } else {
                        String costingMethod = invCosting.getInvItemLocation().getInvItem().getIiCostMethod();
                        if (costingMethod.equals(Average)) {
                            double avgCost = invCosting.getCstRemainingQuantity() <= 0 ? COST :
                                    Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                            this.postToInv(arInvoiceLineItem, arInvoice.getInvDate(), quantitySold,
                                    avgCost * quantitySold, invCosting.getCstRemainingQuantity() - quantitySold,
                                    invCosting.getCstRemainingValue() - (quantitySold * avgCost), 0d, null, branchCode, companyCode);
                        } else if (costingMethod.equals("FIFO")) {
                            double fifoCost = invCosting.getCstRemainingQuantity() == 0 ? COST :
                                    this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), quantitySold, arInvoiceLineItem.getIliUnitPrice(), true, branchCode, companyCode, companyShortName);
                            this.postToInv(arInvoiceLineItem, arInvoice.getInvDate(), quantitySold,
                                    fifoCost * quantitySold, invCosting.getCstRemainingQuantity() - quantitySold,
                                    invCosting.getCstRemainingValue() - (fifoCost * quantitySold), 0d, null, branchCode, companyCode);
                        } else {
                            // Standard Costing
                            double standardCost = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                            this.postToInv(arInvoiceLineItem, arInvoice.getInvDate(), quantitySold, standardCost * quantitySold,
                                    invCosting.getCstRemainingQuantity() - quantitySold,
                                    invCosting.getCstRemainingValue() - (standardCost * quantitySold),
                                    0d, null, branchCode, companyCode);
                        }
                    }
                }
            } else if (arSalesOrderInvoiceLines != null && !arSalesOrderInvoiceLines.isEmpty()) {
                for (Object salesOrderInvoiceLine : arSalesOrderInvoiceLines) {
                    LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) salesOrderInvoiceLine;
                    LocalArSalesOrderLine arSalesOrderLine = arSalesOrderInvoiceLine.getArSalesOrderLine();
                    String II_NM = arSalesOrderLine.getInvItemLocation().getInvItem().getIiName();
                    String LOC_NM = arSalesOrderLine.getInvItemLocation().getInvLocation().getLocName();
                    double quantitySold = this.convertByUomFromAndItemAndQuantity(arSalesOrderLine.getInvUnitOfMeasure(),
                            arSalesOrderLine.getInvItemLocation().getInvItem(), arSalesOrderInvoiceLine.getSilQuantityDelivered(),
                            companyCode, companyShortName);

                    LocalInvCosting invCosting = null;
                    try {
                        invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                arInvoice.getInvDate(), II_NM, LOC_NM, branchCode, companyCode);
                    }
                    catch (FinderException ex) {
                    }

                    double COST = arSalesOrderLine.getInvItemLocation().getInvItem().getIiUnitCost();
                    if (invCosting == null) {
                        this.postToInvSo(arSalesOrderInvoiceLine, arInvoice.getInvDate(), quantitySold,
                                COST * quantitySold, -quantitySold, -(quantitySold * COST), 0d, null, branchCode, companyCode);
                    } else {
                        String costingMethod = invCosting.getInvItemLocation().getInvItem().getIiCostMethod();
                        if (costingMethod.equals(Average)) {
                            double avgCost = invCosting.getCstRemainingQuantity() <= 0 ? COST :
                                    Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                            this.postToInvSo(arSalesOrderInvoiceLine, arInvoice.getInvDate(), quantitySold,
                                    avgCost * quantitySold, invCosting.getCstRemainingQuantity() - quantitySold,
                                    invCosting.getCstRemainingValue() - (quantitySold * avgCost), 0d, null, branchCode, companyCode);
                        } else if (costingMethod.equals("FIFO")) {
                            double fifoCost = this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), quantitySold, arSalesOrderInvoiceLine.getArSalesOrderLine().getSolUnitPrice(), true, branchCode, companyCode, companyShortName);
                            this.postToInvSo(arSalesOrderInvoiceLine, arInvoice.getInvDate(), quantitySold,
                                    fifoCost * quantitySold, invCosting.getCstRemainingQuantity() - quantitySold,
                                    invCosting.getCstRemainingValue() - (fifoCost * quantitySold), 0d, null, branchCode, companyCode);
                        } else {
                            // Standard
                            double standardCost = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                            this.postToInvSo(arSalesOrderInvoiceLine, arInvoice.getInvDate(), quantitySold, standardCost * quantitySold,
                                    invCosting.getCstRemainingQuantity() - quantitySold,
                                    invCosting.getCstRemainingValue() - (standardCost * quantitySold),
                                    0d, null, branchCode, companyCode);
                        }
                    }
                }
            } else if (arJobOrderInvoiceLines != null && !arJobOrderInvoiceLines.isEmpty()) {
                for (Object jobOrderInvoiceLine : arJobOrderInvoiceLines) {
                    LocalArJobOrderInvoiceLine arJobOrderInvoiceLine = (LocalArJobOrderInvoiceLine) jobOrderInvoiceLine;
                    LocalArJobOrderLine arJobOrderLine = arJobOrderInvoiceLine.getArJobOrderLine();
                    String II_NM = arJobOrderLine.getInvItemLocation().getInvItem().getIiName();
                    String LOC_NM = arJobOrderLine.getInvItemLocation().getInvLocation().getLocName();
                    double quantitySold = this.convertByUomFromAndItemAndQuantity(arJobOrderLine.getInvUnitOfMeasure(),
                            arJobOrderLine.getInvItemLocation().getInvItem(), arJobOrderInvoiceLine.getJilQuantityDelivered(), companyCode, companyShortName);

                    LocalInvCosting invCosting = null;
                    try {
                        invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                arInvoice.getInvDate(), II_NM, LOC_NM, branchCode, companyCode);
                    }
                    catch (FinderException ex) {
                    }

                    double COST = arJobOrderLine.getInvItemLocation().getInvItem().getIiUnitCost();
                    if (invCosting == null) {
                        this.postToInvJo(arJobOrderInvoiceLine, arInvoice.getInvDate(), quantitySold,
                                COST * quantitySold, -quantitySold, -(quantitySold * COST),
                                0d, null, branchCode, companyCode);
                    } else {
                        switch (invCosting.getInvItemLocation().getInvItem().getIiCostMethod()) {
                            case Average:
                                double avgCost = invCosting.getCstRemainingQuantity() <= 0 ? COST :
                                        Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                                this.postToInvJo(arJobOrderInvoiceLine, arInvoice.getInvDate(), quantitySold,
                                        avgCost * quantitySold, invCosting.getCstRemainingQuantity() - quantitySold,
                                        invCosting.getCstRemainingValue() - (quantitySold * avgCost), 0d, null, branchCode, companyCode);
                                break;
                            case "FIFO":
                                double fifoCost = this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(),
                                        quantitySold, arJobOrderInvoiceLine.getArJobOrderLine().getJolUnitPrice(),
                                        true, branchCode, companyCode, companyShortName);
                                this.postToInvJo(arJobOrderInvoiceLine, arInvoice.getInvDate(), quantitySold, fifoCost * quantitySold,
                                        invCosting.getCstRemainingQuantity() - quantitySold,
                                        invCosting.getCstRemainingValue() - (fifoCost * quantitySold), 0d, null, branchCode, companyCode);
                                break;
                            case "Standard":
                                double standardCost = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                                this.postToInvJo(arJobOrderInvoiceLine, arInvoice.getInvDate(), quantitySold,
                                        standardCost * quantitySold, invCosting.getCstRemainingQuantity() - quantitySold,
                                        invCosting.getCstRemainingValue() - (standardCost * quantitySold),
                                        0d, null, branchCode, companyCode);
                                break;
                        }
                    }
                }
            }
            // set invoice post status
            arInvoice.setInvPosted(EJBCommon.TRUE);
            arInvoice.setInvPostedBy(USR_NM);
            arInvoice.setInvDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            // post to gl if necessary
            if (adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                // validate if date has no period and period is closed
                LocalGlSetOfBook glJournalSetOfBook;
                try {
                    glJournalSetOfBook = glSetOfBookHome.findByDate(arInvoice.getInvDate(), companyCode);
                }
                catch (FinderException ex) {
                    throw new GlJREffectiveDateNoPeriodExistException();
                }

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(
                        glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), arInvoice.getInvDate(), companyCode);

                if (glAccountingCalendarValue.getAcvStatus() == 'N'
                        || glAccountingCalendarValue.getAcvStatus() == 'C'
                        || glAccountingCalendarValue.getAcvStatus() == 'P') {
                    throw new GlJREffectiveDatePeriodClosedException();
                }

                // check if invoice is balance if not check suspense posting
                LocalGlJournalLine glOffsetJournalLine = null;
                Collection arDistributionRecords = arDistributionRecordHome.findImportableDrByInvCode(arInvoice.getInvCode(), companyCode);
                Iterator j = arDistributionRecords.iterator();

                double TOTAL_DEBIT = 0d;
                double TOTAL_CREDIT = 0d;

                while (j.hasNext()) {
                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) j.next();
                    double DR_AMNT = 0d;
                    if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {
                        DR_AMNT = arDistributionRecord.getDrAmount();
                    } else {
                        DR_AMNT = arDistributionRecord.getDrAmount();
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
                        glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("ACCOUNTS RECEIVABLES",
                                arInvoice.getInvCreditMemo() == EJBCommon.FALSE ? "SALES INVOICES" : "CREDIT MEMOS", companyCode);
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

                // create journal entry
                LocalGlJournal glJournal = glJournalHome
                        .JrName(arInvoice.getInvReferenceNumber())
                        .JrDescription(arInvoice.getInvDescription())
                        .JrEffectiveDate(arInvoice.getInvDate())
                        .JrDocumentNumber(arInvoice.getInvNumber())
                        .JrConversionRate(1d)
                        .JrPosted(EJBCommon.TRUE)
                        .JrCreatedBy(USR_NM)
                        .JrLastModifiedBy(USR_NM)
                        .JrPostedBy(USR_NM)
                        .JrDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime())
                        .JrTin(arInvoice.getArCustomer().getCstTin())
                        .JrSubLedger(arInvoice.getArCustomer().getCstName())
                        .JrAdBranch(branchCode)
                        .JrAdCompany(companyCode)
                        .buildJournal();

                LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("ACCOUNTS RECEIVABLES", companyCode);
                glJournal.setGlJournalSource(glJournalSource);

                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(
                        adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

                LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName(
                        arInvoice.getInvCreditMemo() == EJBCommon.FALSE ? "SALES INVOICES" : "CREDIT MEMOS", companyCode);
                glJournal.setGlJournalCategory(glJournalCategory);

                // create journal lines
                j = arDistributionRecords.iterator();
                while (j.hasNext()) {
                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) j.next();
                    double DR_AMNT = 0d;
                    if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {
                        DR_AMNT = arDistributionRecord.getDrAmount();
                    } else {
                        DR_AMNT = arDistributionRecord.getDrAmount();
                    }

                    LocalGlJournalLine glJournalLine = glJournalLineHome
                            .JlLineNumber(arDistributionRecord.getDrLine())
                            .JlDebit(arDistributionRecord.getDrDebit())
                            .JlAmount(DR_AMNT)
                            .JlAdCompany(companyCode)
                            .buildJournalLine();

                    glJournalLine.setGlChartOfAccount(arDistributionRecord.getGlChartOfAccount());
                    glJournalLine.setGlJournal(glJournal);

                    arDistributionRecord.setDrImported(EJBCommon.TRUE);

                    // for FOREX revaluation
                    if ((!Objects.equals(arInvoice.getGlFunctionalCurrency().getFcCode(), adCompany.getGlFunctionalCurrency().getFcCode()))
                            && glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null
                            && (glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode().equals(arInvoice.getGlFunctionalCurrency().getFcCode()))) {

                        double conversionRate = 1;

                        if (arInvoice.getInvConversionRate() != 0 && arInvoice.getInvConversionRate() != 1) {
                            conversionRate = arInvoice.getInvConversionRate();
                        } else if (arInvoice.getInvConversionDate() != null) {
                            conversionRate = this.getFrRateByFrNameAndFrDate(
                                    glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcName(),
                                    glJournal.getJrConversionDate(), companyCode);
                        }

                        Collection glForexLedgers = null;

                        try {
                            glForexLedgers = glForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(
                                    arInvoice.getInvDate(), glJournalLine.getGlChartOfAccount().getCoaCode(), companyCode);
                        }
                        catch (FinderException ex) {
                        }

                        LocalGlForexLedger glForexLedger = (glForexLedgers.isEmpty() || glForexLedgers == null) ? null :
                                (LocalGlForexLedger) glForexLedgers.iterator().next();

                        int FRL_LN = (glForexLedger != null && glForexLedger.getFrlDate().compareTo(arInvoice.getInvDate()) == 0) ? glForexLedger.getFrlLine() + 1 : 1;

                        // compute balance
                        double COA_FRX_BLNC = glForexLedger == null ? 0 : glForexLedger.getFrlBalance();
                        double FRL_AMNT = arDistributionRecord.getDrAmount();

                        if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase(ASSET)) {
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                        } else {
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);
                        }

                        COA_FRX_BLNC = COA_FRX_BLNC + FRL_AMNT;

                        glForexLedger = glForexLedgerHome.create(arInvoice.getInvDate(), FRL_LN, "SI",
                                FRL_AMNT, conversionRate, COA_FRX_BLNC, 0d, companyCode);
                        glForexLedger.setGlChartOfAccount(glJournalLine.getGlChartOfAccount());

                        // propagate balances
                        try {
                            glForexLedgers = glForexLedgerHome.findByGreaterThanFrlDateAndCoaCode(glForexLedger.getFrlDate(),
                                    glForexLedger.getGlChartOfAccount().getCoaCode(), glForexLedger.getFrlAdCompany());
                        }
                        catch (FinderException ex) {
                        }

                        for (Object forexLedger : glForexLedgers) {
                            glForexLedger = (LocalGlForexLedger) forexLedger;
                            FRL_AMNT = arDistributionRecord.getDrAmount();
                            if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase(ASSET)) {
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

                    // post to subsequent accounting calendar values (propagate)
                    Collection glSubsequentAccountingCalendarValues =
                            glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(
                                    glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), glAccountingCalendarValue.getAcvPeriodNumber(), companyCode);

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
                            Collection glAccountingCalendarValues =
                                    glAccountingCalendarValueHome.findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);
                            for (Object accountingCalendarValue : glAccountingCalendarValues) {
                                LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                                if (ACCOUNT_TYPE.equals(ASSET) || ACCOUNT_TYPE.equals(LIABILITY) || ACCOUNT_TYPE.equals(OWNERS_EQUITY)) {
                                    this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(),
                                            false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                                } else {
                                    // revenue & expense
                                    this.postToGl(glSubsequentAccountingCalendarValue, glRetainedEarningsAccount,
                                            false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
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
               AdPRFCoaGlVarianceAccountNotFoundException | GlobalTransactionAlreadyVoidException |
               GlobalTransactionAlreadyPostedException | GlobalJournalNotBalanceException |
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

    private void isInvoicePostedOrVoid(LocalArInvoice arInvoice)
            throws GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException {

        if (arInvoice.getInvPosted() == EJBCommon.TRUE) {
            throw new GlobalTransactionAlreadyPostedException();
        } else if (arInvoice.getInvVoid() == EJBCommon.TRUE) {
            throw new GlobalTransactionAlreadyVoidException();
        }
    }

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure,
                                                      LocalInvItem invItem, double quantitySold,
                                                      Integer companyCode, String companyShortName) {

        Debug.print("ArInvoiceEntryApiControllerBean convertByUomFromAndItemAndQuantity");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode, companyShortName);
            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion =
                    invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), companyCode, companyShortName);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion =
                    invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), companyCode, companyShortName);

            return EJBCommon.roundIt(quantitySold * invDefaultUomConversion.getUmcConversionFactor() /
                    invUnitOfMeasureConversion.getUmcConversionFactor(), adPreference.getPrfInvQuantityPrecisionUnit());

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void updateCustomerBalance(Date INV_DT, double INV_AMNT, LocalArCustomer arCustomer, Integer companyCode, String companyShortName) {

        Debug.print("ArInvoiceEntryApiControllerBean updateCustomerBalance");

        try {

            // find customer balance before or equal invoice date
            Collection arCustomerBalances = arCustomerBalanceHome.findByBeforeOrEqualInvDateAndCstCustomerCode(
                    INV_DT, arCustomer.getCstCustomerCode(), companyCode, companyShortName);

            if (!arCustomerBalances.isEmpty()) {

                // get last invoice
                ArrayList arCustomerBalanceList = new ArrayList(arCustomerBalances);
                LocalArCustomerBalance arCustomerBalance = (LocalArCustomerBalance) arCustomerBalanceList.get(arCustomerBalanceList.size() - 1);

                if (arCustomerBalance.getCbDate().before(INV_DT)) {

                    // create new balance
                    LocalArCustomerBalance apNewCustomerBalance = arCustomerBalanceHome
                            .CbDate(INV_DT)
                            .CbBalance(arCustomerBalance.getCbBalance() + INV_AMNT)
                            .CbAdCompany(companyCode)
                            .buildCustomerBalance(companyShortName);
                    // arCustomer.addArCustomerBalance(apNewCustomerBalance);
                    apNewCustomerBalance.setArCustomer(arCustomer);

                } else { // equals to invoice date

                    arCustomerBalance.setCbBalance(arCustomerBalance.getCbBalance() + INV_AMNT);
                }

            } else {

                // create new balance
                LocalArCustomerBalance apNewCustomerBalance = arCustomerBalanceHome
                        .CbDate(INV_DT)
                        .CbBalance(INV_AMNT)
                        .CbAdCompany(companyCode)
                        .buildCustomerBalance(companyShortName);

                // arCustomer.addArCustomerBalance(apNewCustomerBalance);
                apNewCustomerBalance.setArCustomer(arCustomer);
            }

            // propagate to subsequent balances if necessary
            arCustomerBalances = arCustomerBalanceHome.findByAfterInvDateAndCstCustomerCode(INV_DT, arCustomer.getCstCustomerCode(), companyCode, companyShortName);
            for (Object customerBalance : arCustomerBalances) {
                LocalArCustomerBalance arCustomerBalance = (LocalArCustomerBalance) customerBalance;
                arCustomerBalance.setCbBalance(arCustomerBalance.getCbBalance() + INV_AMNT);
            }

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToInv(LocalArInvoiceLineItem arInvoiceLineItem, Date CST_DT, double CST_QTY_SLD,
                           double CST_CST_OF_SLS, double CST_RMNNG_QTY, double CST_RMNNG_VL,
                           double CST_VRNC_VL, String USR_NM, Integer branchCode, Integer companyCode)
            throws AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException {

        Debug.print("ArInvoiceEntryApiControllerBean postToInv");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalInvItemLocation itemLocation = arInvoiceLineItem.getInvItemLocation();
            int CST_LN_NMBR = 0;

            CST_QTY_SLD = EJBCommon.roundIt(CST_QTY_SLD, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_CST_OF_SLS = EJBCommon.roundIt(CST_CST_OF_SLS, adCompany.getGlFunctionalCurrency().getFcPrecision());
            CST_RMNNG_QTY = EJBCommon.roundIt(CST_RMNNG_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_RMNNG_VL = EJBCommon.roundIt(CST_RMNNG_VL, adCompany.getGlFunctionalCurrency().getFcPrecision());

            if (CST_QTY_SLD > 0) {
                itemLocation.setIlCommittedQuantity(itemLocation.getIlCommittedQuantity() - CST_QTY_SLD);
            }

            try {

                // generate line number
                LocalInvCosting invCurrentCosting =
                        invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(
                                CST_DT.getTime(), itemLocation.getInvItem().getIiName(),
                                itemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            }
            catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                // void subsequent cost variance adjustments
                Collection invAdjustmentLines =
                        invAdjustmentLineHome.findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(
                                CST_DT, itemLocation.getIlCode(), branchCode, companyCode);

                for (Object adjustmentLine : invAdjustmentLines) {

                    LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) adjustmentLine;
                    this.voidInvAdjustment(invAdjustmentLine.getInvAdjustment(), branchCode, companyCode);
                }
            }

            String prevExpiryDates = "";
            double qtyPrpgt = 0;
            double qtyPrpgt2 = 0;
            try {
                LocalInvCosting prevCst =
                        invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndRemainingQuantityNotEqualToZeroAndIlCode(
                                CST_DT, itemLocation.getIlCode(), branchCode, companyCode);
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
            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR,
                    0d, 0d, 0d, 0d, CST_QTY_SLD, CST_CST_OF_SLS,
                    CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, 0d, branchCode, companyCode);
            // itemLocation.addInvCosting(invCosting);
            invCosting.setInvItemLocation(itemLocation);
            invCosting.setArInvoiceLineItem(arInvoiceLineItem);

            invCosting.setCstQuantity(CST_QTY_SLD * -1);
            invCosting.setCstCost(CST_CST_OF_SLS * -1);

            // Get Latest Expiry Dates
            if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiTraceMisc() != 0) {
                if (prevExpiryDates != null && prevExpiryDates != "" && prevExpiryDates.length() != 0) {
                    if (arInvoiceLineItem.getIliMisc() != null && arInvoiceLineItem.getIliMisc() != "" && arInvoiceLineItem.getIliMisc().length() != 0) {
                        int qty2Prpgt = Integer.parseInt(this.getQuantityExpiryDates(arInvoiceLineItem.getIliMisc()));
                        String miscList2Prpgt = this.propagateExpiryDates(arInvoiceLineItem.getIliMisc(), qty2Prpgt, "False");
                        List<String> miscList = this.expiryDates(arInvoiceLineItem.getIliMisc(), qty2Prpgt);
                        String propagateMiscPrpgt = "";
                        StringBuilder ret = new StringBuilder();

                        // ArrayList miscList2 = null;
                        if (CST_QTY_SLD < 0) {
                            prevExpiryDates = prevExpiryDates.substring(1);
                            propagateMiscPrpgt = miscList2Prpgt + prevExpiryDates;
                        } else {
                            Iterator mi = miscList.iterator();
                            propagateMiscPrpgt = prevExpiryDates;
                            ret = new StringBuilder(propagateMiscPrpgt);
                            String Checker = "";
                            while (mi.hasNext()) {
                                String miscStr = (String) mi.next();

                                int qTest = this.checkExpiryDates(ret + "fin$");
                                List<String> miscList2 = this.expiryDates("$" + ret, Double.parseDouble(Integer.toString(qTest)));
                                Iterator m2 = miscList2.iterator();
                                ret = new StringBuilder();
                                String ret2 = "false";
                                int a = 0;

                                while (m2.hasNext()) {
                                    String miscStr2 = (String) m2.next();

                                    if (ret2 == "1st") {
                                        ret2 = "false";
                                    }

                                    if (miscStr2.trim().equals(miscStr.trim())) {
                                        if (a == 0) {
                                            a = 1;
                                            ret2 = "1st";
                                            Checker = "true";
                                        } else {
                                            a = a + 1;
                                            ret2 = "true";
                                        }
                                    }

                                    if (!miscStr2.trim().equals(miscStr.trim()) || a > 1) {
                                        if ((ret2 != "1st") || (ret2 == "false") || (ret2 == "true")) {
                                            if (miscStr2 != "") {
                                                miscStr2 = "$" + miscStr2.trim();
                                                ret.append(miscStr2);
                                                qtyPrpgt2++;
                                                ret2 = "false";
                                            }
                                        }
                                    }
                                }
                                ret.append("$");
                                if (qtyPrpgt2 == 0) {
                                    qtyPrpgt2 = qtyPrpgt;
                                }
                                qtyPrpgt = qtyPrpgt - 1;
                            }
                            propagateMiscPrpgt = ret.toString();
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

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(
                    CST_DT, itemLocation.getInvItem().getIiName(), itemLocation.getInvLocation().getLocName(), branchCode, companyCode);

            Iterator i = invCostings.iterator();
            String miscList = "";
            List<String> miscList2 = null;

            String propagateMisc = "";
            StringBuilder ret = new StringBuilder();

            while (i.hasNext()) {
                LocalInvCosting invPropagatedCosting = (LocalInvCosting) i.next();
                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() - CST_QTY_SLD);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() - CST_CST_OF_SLS);
                if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiTraceMisc() != 0) {
                    if (arInvoiceLineItem.getIliMisc() != null && arInvoiceLineItem.getIliMisc() != "" && arInvoiceLineItem.getIliMisc().length() != 0) {
                        double qty = Double.parseDouble(this.getQuantityExpiryDates(arInvoiceLineItem.getIliMisc()));
                        miscList = this.propagateExpiryDates(arInvoiceLineItem.getIliMisc(), qty, "False");
                        miscList2 = this.expiryDates(arInvoiceLineItem.getIliMisc(), qty);
                        if (arInvoiceLineItem.getIliQuantity() < 0) {
                            Iterator mi = miscList2.iterator();
                            ret = new StringBuilder(invPropagatedCosting.getCstExpiryDate());
                            while (mi.hasNext()) {
                                String miscStr = (String) mi.next();
                                int qTest = this.checkExpiryDates(ret + "fin$");
                                List<String> miscList3 = this.expiryDates("$" + ret, Double.parseDouble(Integer.toString(qTest)));
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
                        }
                    }
                    if (arInvoiceLineItem.getIliMisc() != null && arInvoiceLineItem.getIliMisc() != "" && arInvoiceLineItem.getIliMisc().length() != 0) {
                        if (CST_QTY_SLD < 0) {
                            propagateMisc = miscList + invPropagatedCosting.getCstExpiryDate().substring(1);
                        } else {
                            Iterator mi = miscList2.iterator();
                            propagateMisc = prevExpiryDates;
                            ret = new StringBuilder(propagateMisc);
                            while (mi.hasNext()) {
                                String miscStr = (String) mi.next();
                                if (qtyPrpgt <= 0) {
                                    qtyPrpgt = qtyPrpgt2;
                                }
                                int qTest = this.checkExpiryDates(ret + "fin$");
                                List<String> miscList3 = this.expiryDates("$" + ret, Double.parseDouble(Integer.toString(qTest)));
                                Iterator m2 = miscList3.iterator();
                                ret = new StringBuilder();
                                String ret2 = "false";
                                int a = 0;
                                while (m2.hasNext()) {
                                    String miscStr2 = (String) m2.next();
                                    if (ret2 == "1st") {
                                        ret2 = "false";
                                    }
                                    if (miscStr2.trim().equals(miscStr.trim())) {
                                        if (a == 0) {
                                            a = 1;
                                            ret2 = "1st";
                                        } else {
                                            a = a + 1;
                                            ret2 = "true";
                                        }
                                    }
                                    if (!miscStr2.trim().equals(miscStr.trim()) || a > 1) {
                                        if ((ret2 != "1st") || (ret2 == "false") || (ret2 == "true")) {
                                            if (miscStr2 != "") {
                                                miscStr2 = "$" + miscStr2.trim();
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

                        invPropagatedCosting.setCstExpiryDate(propagateMisc);
                    } else {
                        invPropagatedCosting.setCstExpiryDate(prevExpiryDates);
                    }
                }
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

    private void postToInvSo(LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine, Date CST_DT,
                             double CST_QTY_SLD, double CST_CST_OF_SLS, double CST_RMNNG_QTY, double CST_RMNNG_VL,
                             double CST_VRNC_VL, String USR_NM, Integer branchCode, Integer companyCode)
            throws AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ArInvoiceEntryApiControllerBean postToInvSo");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalArSalesOrderLine arSalesOrderLine = arSalesOrderInvoiceLine.getArSalesOrderLine();
            LocalInvItemLocation itemLocation = arSalesOrderLine.getInvItemLocation();
            int CST_LN_NMBR = 0;

            CST_QTY_SLD = EJBCommon.roundIt(CST_QTY_SLD, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_CST_OF_SLS = EJBCommon.roundIt(CST_CST_OF_SLS, adCompany.getGlFunctionalCurrency().getFcPrecision());
            CST_RMNNG_QTY = EJBCommon.roundIt(CST_RMNNG_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_RMNNG_VL = EJBCommon.roundIt(CST_RMNNG_VL, adCompany.getGlFunctionalCurrency().getFcPrecision());

            try {

                // generate line number

                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(
                        CST_DT.getTime(), itemLocation.getInvItem().getIiName(), itemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            }
            catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                // void subsequent cost variance adjustments
                Collection invAdjustmentLines = invAdjustmentLineHome.findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(
                        CST_DT, itemLocation.getIlCode(), branchCode, companyCode);

                for (Object adjustmentLine : invAdjustmentLines) {

                    LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) adjustmentLine;
                    this.voidInvAdjustment(invAdjustmentLine.getInvAdjustment(), branchCode, companyCode);
                }
            }

            // create costing
            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR,
                    0d, 0d, 0d, 0d, CST_QTY_SLD,
                    CST_CST_OF_SLS, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, 0d, branchCode, companyCode);
            // itemLocation.addInvCosting(invCosting);
            invCosting.setInvItemLocation(itemLocation);
            invCosting.setArSalesOrderInvoiceLine(arSalesOrderInvoiceLine);

            invCosting.setCstQuantity(CST_QTY_SLD * -1);
            invCosting.setCstCost(CST_CST_OF_SLS * -1);

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(
                    CST_DT, itemLocation.getInvItem().getIiName(), itemLocation.getInvLocation().getLocName(), branchCode, companyCode);

            for (Object costing : invCostings) {

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) costing;

                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_QTY_SLD);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_CST_OF_SLS);
            }
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToInvJo(LocalArJobOrderInvoiceLine arJobOrderInvoiceLine, Date CST_DT, double CST_QTY_SLD,
                             double CST_CST_OF_SLS, double CST_RMNNG_QTY, double CST_RMNNG_VL, double CST_VRNC_VL,
                             String USR_NM, Integer branchCode, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ArInvoiceEntryApiControllerBean postToInvJo");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalArJobOrderLine arJobOrderLine = arJobOrderInvoiceLine.getArJobOrderLine();
            LocalInvItemLocation itemLocation = arJobOrderLine.getInvItemLocation();
            int CST_LN_NMBR = 0;

            CST_QTY_SLD = EJBCommon.roundIt(CST_QTY_SLD, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_CST_OF_SLS = EJBCommon.roundIt(CST_CST_OF_SLS, adCompany.getGlFunctionalCurrency().getFcPrecision());
            CST_RMNNG_QTY = EJBCommon.roundIt(CST_RMNNG_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_RMNNG_VL = EJBCommon.roundIt(CST_RMNNG_VL, adCompany.getGlFunctionalCurrency().getFcPrecision());

            try {

                // generate line number
                LocalInvCosting invCurrentCosting =
                        invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(
                                CST_DT.getTime(), itemLocation.getInvItem().getIiName(),
                                itemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            }
            catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                // void subsequent cost variance adjustments
                Collection invAdjustmentLines =
                        invAdjustmentLineHome.findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(
                                CST_DT, itemLocation.getIlCode(), branchCode, companyCode);

                for (Object adjustmentLine : invAdjustmentLines) {

                    LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) adjustmentLine;
                    this.voidInvAdjustment(invAdjustmentLine.getInvAdjustment(), branchCode, companyCode);
                }
            }

            // create costing
            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR,
                    0d, 0d, 0d, 0d, CST_QTY_SLD,
                    CST_CST_OF_SLS, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d,
                    0d, branchCode, companyCode);

            // itemLocation.addInvCosting(invCosting);
            invCosting.setInvItemLocation(itemLocation);
            invCosting.setArJobOrderInvoiceLine(arJobOrderInvoiceLine);

            invCosting.setCstQuantity(CST_QTY_SLD * -1);
            invCosting.setCstCost(CST_CST_OF_SLS * -1);

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(
                    CST_DT, itemLocation.getInvItem().getIiName(),
                    itemLocation.getInvLocation().getLocName(), branchCode, companyCode);

            for (Object costing : invCostings) {
                LocalInvCosting invPropagatedCosting = (LocalInvCosting) costing;
                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_QTY_SLD);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_CST_OF_SLS);
            }
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void voidInvAdjustment(LocalInvAdjustment invAdjustment, Integer branchCode, Integer companyCode) {

        Debug.print("ArInvoiceEntryApiControllerBean voidInvAdjustment");
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
            this.executeInvAdjPost(invAdjustment.getAdjCode(), invAdjustment.getAdjLastModifiedBy(), branchCode, companyCode);
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void addInvDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, byte DR_RVRSL,
                               Integer COA_CODE, LocalInvAdjustment invAdjustment, Integer branchCode, Integer companyCode)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArInvoiceEntryApiControllerBean addInvDrEntry");
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
            LocalInvDistributionRecord invDistributionRecord = invDistributionRecordHome.create(
                    DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()),
                    DR_RVRSL, EJBCommon.FALSE, companyCode);
            invDistributionRecord.setInvAdjustment(invAdjustment);
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

    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue,
                          LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv,
                          byte isDebit, double JL_AMNT, Integer companyCode) {

        Debug.print("ArInvoiceEntryApiControllerBean postToGl");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalGlChartOfAccountBalance glChartOfAccountBalance =
                    glChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(glAccountingCalendarValue.getAcvCode(), glChartOfAccount.getCoaCode(), companyCode);

            String ACCOUNT_TYPE = glChartOfAccount.getCoaAccountType();
            short FC_EXTNDD_PRCSN = adCompany.getGlFunctionalCurrency().getFcPrecision();

            if (((ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("EXPENSE")) && isDebit == EJBCommon.TRUE)
                    || (!ACCOUNT_TYPE.equals("ASSET") && !ACCOUNT_TYPE.equals("EXPENSE") && isDebit == EJBCommon.FALSE)) {
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

    private LocalInvAdjustmentLine addInvAlEntry(LocalInvItemLocation itemLocation,
                                                 LocalInvAdjustment invAdjustment,
                                                 double CST_VRNC_VL, byte AL_VD, Integer companyCode) {

        Debug.print("ArInvoiceEntryApiControllerBean addInvAlEntry");
        try {

            // create dr entry
            LocalInvAdjustmentLine invAdjustmentLine = null;
            invAdjustmentLine = invAdjustmentLineHome.create(CST_VRNC_VL, null, null, 0, 0, AL_VD, companyCode);
            // map adjustment, unit of measure, item location
            invAdjustmentLine.setInvAdjustment(invAdjustment);
            invAdjustmentLine.setInvItemLocation(itemLocation);
            invAdjustmentLine.setInvItemLocation(itemLocation);
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
            GlobalJournalNotBalanceException {

        Debug.print("ArInvoiceEntryApiControllerBean executeInvAdjPost");

        try {

            // validate if adjustment is already deleted
            LocalInvAdjustment invAdjustment;

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

            Collection invAdjustmentLines;
            if (invAdjustment.getAdjVoid() == EJBCommon.FALSE) {
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.FALSE, invAdjustment.getAdjCode(), companyCode);
            } else {
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.TRUE, invAdjustment.getAdjCode(), companyCode);
            }

            Iterator i = invAdjustmentLines.iterator();
            while (i.hasNext()) {
                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();
                LocalInvCosting invCosting =
                        invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                invAdjustment.getAdjDate(), invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(),
                                invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName(), branchCode, companyCode);

                this.postInvAdjustmentToInventory(invAdjustmentLine, invAdjustment.getAdjDate(), 0,
                        invAdjustmentLine.getAlUnitCost(), invCosting.getCstRemainingQuantity(),
                        invCosting.getCstRemainingValue() + invAdjustmentLine.getAlUnitCost(), branchCode, companyCode);
            }

            // post to gl if necessary
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate if date has no period and period is closed
            LocalGlSetOfBook glJournalSetOfBook;
            try {
                glJournalSetOfBook = glSetOfBookHome.findByDate(invAdjustment.getAdjDate(), companyCode);
            }
            catch (FinderException ex) {
                throw new GlJREffectiveDateNoPeriodExistException();
            }

            LocalGlAccountingCalendarValue glAccountingCalendarValue =
                    glAccountingCalendarValueHome.findByAcCodeAndDate(
                            glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), invAdjustment.getAdjDate(), companyCode);

            if (glAccountingCalendarValue.getAcvStatus() == 'N'
                    || glAccountingCalendarValue.getAcvStatus() == 'C'
                    || glAccountingCalendarValue.getAcvStatus() == 'P') {
                throw new GlJREffectiveDatePeriodClosedException();
            }

            // check if invoice is balance if not check suspense posting
            LocalGlJournalLine glOffsetJournalLine = null;
            Collection invDistributionRecords;
            if (invAdjustment.getAdjVoid() == EJBCommon.FALSE) {
                invDistributionRecords = invDistributionRecordHome.findImportableDrByDrReversedAndAdjCode(
                        EJBCommon.FALSE, invAdjustment.getAdjCode(), companyCode);
            } else {
                invDistributionRecords = invDistributionRecordHome.findImportableDrByDrReversedAndAdjCode(
                        EJBCommon.TRUE, invAdjustment.getAdjCode(), companyCode);
            }

            Iterator j = invDistributionRecords.iterator();
            double TOTAL_DEBIT = 0d;
            double TOTAL_CREDIT = 0d;
            while (j.hasNext()) {
                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) j.next();
                double DR_AMNT;
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
                LocalGlSuspenseAccount glSuspenseAccount;
                try {
                    glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName(INVENTORY, "INVENTORY ADJUSTMENTS", companyCode);
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
                glOffsetJournalLine.setGlChartOfAccount(glChartOfAccount);
            } else if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE && TOTAL_DEBIT != TOTAL_CREDIT) {
                throw new GlobalJournalNotBalanceException();
            }

            // create journal batch if necessary
            LocalGlJournalBatch glJournalBatch = null;
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");
            try {
                glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " +
                        formatter.format(new Date()) + " INVENTORY ADJUSTMENTS", branchCode, companyCode);
            }
            catch (FinderException ex) {
            }

            if (glJournalBatch == null) {
                glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " +
                        formatter.format(new Date()) + " INVENTORY ADJUSTMENTS", "JOURNAL IMPORT",
                        "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, branchCode, companyCode);
            }

            // create journal entry
            LocalGlJournal glJournal = glJournalHome.create(invAdjustment.getAdjReferenceNumber(),
                    invAdjustment.getAdjDescription(), invAdjustment.getAdjDate(), 0.0d,
                    null, invAdjustment.getAdjDocumentNumber(), null, 1d,
                    "N/A", null, 'N', EJBCommon.TRUE,
                    EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null,
                    null, USR_NM, EJBCommon.getGcCurrentDateWoTime().getTime(),
                    null, null, EJBCommon.FALSE, null, branchCode, companyCode);

            LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName(INVENTORY, companyCode);
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
                double DR_AMNT;
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
                this.postToGl(glAccountingCalendarValue, glJournalLine.getGlChartOfAccount(),
                        true, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                // post to subsequent acvs (propagate)
                Collection glSubsequentAccountingCalendarValues =
                        glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(
                                glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), glAccountingCalendarValue.getAcvPeriodNumber(), companyCode);

                for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {
                    LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;
                    this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(),
                            false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                }

                // post to subsequent years if necessary
                Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(
                        glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode);

                if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                    adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                    LocalGlChartOfAccount glRetainedEarningsAccount =
                            glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(adCompany.getCmpRetainedEarnings(), branchCode, companyCode);

                    for (Object subsequentSetOfBook : glSubsequentSetOfBooks) {

                        LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) subsequentSetOfBook;

                        String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                        // post to subsequent acvs of subsequent set of book(propagate)
                        Collection glAccountingCalendarValues =
                                glAccountingCalendarValueHome.findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                        for (Object accountingCalendarValue : glAccountingCalendarValues) {
                            LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;
                            if (ACCOUNT_TYPE.equals(ASSET) || ACCOUNT_TYPE.equals(LIABILITY) || ACCOUNT_TYPE.equals(OWNERS_EQUITY)) {
                                this.postToGl(glSubsequentAccountingCalendarValue,
                                        glJournalLine.getGlChartOfAccount(), false,
                                        glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                            } else {
                                this.postToGl(glSubsequentAccountingCalendarValue,
                                        glRetainedEarningsAccount, false,
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

    private void postInvAdjustmentToInventory(LocalInvAdjustmentLine invAdjustmentLine,
                                              Date CST_DT, double CST_ADJST_QTY, double CST_ADJST_CST,
                                              double CST_RMNNG_QTY, double CST_RMNNG_VL, Integer branchCode, Integer companyCode) {

        Debug.print("ArInvoiceEntryApiControllerBean postInvAdjustmentToInventory");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalInvItemLocation itemLocation = invAdjustmentLine.getInvItemLocation();
            int CST_LN_NMBR;
            CST_ADJST_QTY = EJBCommon.roundIt(CST_ADJST_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_ADJST_CST = EJBCommon.roundIt(CST_ADJST_CST, adCompany.getGlFunctionalCurrency().getFcPrecision());
            CST_RMNNG_QTY = EJBCommon.roundIt(CST_RMNNG_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_RMNNG_VL = EJBCommon.roundIt(CST_RMNNG_VL, adCompany.getGlFunctionalCurrency().getFcPrecision());
            if (CST_ADJST_QTY < 0) {
                itemLocation.setIlCommittedQuantity(itemLocation.getIlCommittedQuantity() - Math.abs(CST_ADJST_QTY));
            }
            // create costing
            try {
                // generate line number
                LocalInvCosting invCurrentCosting =
                        invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(
                                CST_DT.getTime(), itemLocation.getInvItem().getIiName(),
                                itemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            }
            catch (FinderException ex) {
                CST_LN_NMBR = 1;
            }
            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR,
                    0d, 0d, CST_ADJST_QTY, CST_ADJST_CST, 0d,
                    0d, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, CST_ADJST_QTY > 0 ? CST_ADJST_QTY : 0, branchCode, companyCode);
            invCosting.setInvItemLocation(itemLocation);
            invCosting.setInvAdjustmentLine(invAdjustmentLine);
            invCosting.setCstQuantity(CST_ADJST_QTY);
            invCosting.setCstCost(CST_ADJST_CST);
            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(
                    CST_DT, itemLocation.getInvItem().getIiName(), itemLocation.getInvLocation().getLocName(), branchCode, companyCode);
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

    private void addArDrIliEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT,
                                 Integer COA_CODE, LocalArInvoice arInvoice, Integer branchCode,
                                 Integer companyCode, String companyShortName)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArInvoiceEntryApiControllerBean addArDrIliEntry");

        try {

            // get company
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode, companyShortName);
            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, branchCode, companyCode, companyShortName);

            // create distribution record
            LocalArDistributionRecord arDistributionRecord =
                    arDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT,
                            EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()),
                            EJBCommon.FALSE, EJBCommon.FALSE, companyCode);

            arDistributionRecord.setArInvoice(arInvoice);
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

    private String getQuantityExpiryDates(String qntty) {

        Debug.print("ArInvoiceEntryApiControllerBean getQuantityExpiryDates");
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

        Debug.print("ArInvoiceEntryApiControllerBean propagateExpiryDates");
        StringBuilder miscList = new StringBuilder();
        try {
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
            int length;
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
        }
        catch (Exception e) {
            miscList = new StringBuilder();
        }
        return (miscList.toString());
    }

    private List<String> expiryDates(String misc, double qty) {

        Debug.print("ArInvoiceEntryApiControllerBean expiryDates");
        List<String> miscList = new ArrayList<>();
        try {
            String separator = "$";
            // Remove first $ character
            misc = misc.substring(1);
            // Counter
            int start = 0;
            int nextIndex = misc.indexOf(separator, start);
            int length;
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
        }
        catch (Exception e) {
            Debug.print(e.getMessage());
        }
        return miscList;
    }

    private int checkExpiryDates(String misc) {

        Debug.print("ArInvoiceEntryApiControllerBean checkExpiryDates");
        String separator = "$";
        // Remove first $ character
        misc = misc.substring(1);
        // Counter
        int start = 0;
        int nextIndex = misc.indexOf(separator, start);
        int length = nextIndex - start;
        int numberExpry = 0;
        StringBuilder miscList = new StringBuilder();
        String g = "";
        try {
            while (g != "fin") {
                // Date
                start = nextIndex + 1;
                nextIndex = misc.indexOf(separator, start);
                length = nextIndex - start;
                g = misc.substring(start, start + length);
                miscList.append("$").append(g);
                numberExpry++;
            }
        }
        catch (Exception e) {
            Debug.print(e.getMessage());
        }
        return (numberExpry);
    }

    private double getFrRateByFrNameAndFrDate(String foreignCurrency, Date conversionDate, Integer companyCode) throws GlobalConversionDateNotExistException {

        Debug.print("ArInvoiceEntryApiControllerBean getFrRateByFrNameAndFrDate");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalGlFunctionalCurrency glFunctionalCurrency =
                    glFunctionalCurrencyHome.findByFcName(foreignCurrency, companyCode);
            double conversionRate = 1;
            // Get functional currency rate
            if (!foreignCurrency.equals("USD")) {
                LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate =
                        glFunctionalCurrencyRateHome.findByFcCodeAndDate(glFunctionalCurrency.getFcCode(), conversionDate, companyCode);
                conversionRate = glFunctionalCurrencyRate.getFrXToUsd();
            }

            // Get set of book functional currency rate if necessary
            if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {
                LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate =
                        glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), conversionDate, companyCode);
                conversionRate = conversionRate / glCompanyFunctionalCurrencyRate.getFrXToUsd();
            }
            return conversionRate;
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

    private void updateSalesPrice(LineItemRequest itemRequest, LocalInvItem invItem) {
        Debug.print("ArInvoiceEntryApiControllerBean updateSalesPrice");
        double percentMarkup = ((itemRequest.getItemSalesPrice() - invItem.getIiUnitCost()) / invItem.getIiUnitCost()) * 100;
        invItem.setIiSalesPrice(itemRequest.getItemSalesPrice());
        invItem.setIiPercentMarkup(percentMarkup);
        invItemHome.updateItem(invItem);
    }

    private LocalArInvoiceLine addArIlEntry(ArModInvoiceLineDetails mdetails, LocalArInvoice arInvoice, Integer companyCode, String companyShortName)
            throws ArInvoiceStandardMemolineDoesNotExist {
        Debug.print("ArInvoiceEntryApiControllerBean addArIlEntry");
        try {
            short precisionUnit = this.getGlFcPrecisionUnit(companyCode, companyShortName);
            double IL_AMNT = mdetails.getIlAmount();
            double IL_TAX_AMNT = 0d;
            if (mdetails.getIlTax() == EJBCommon.TRUE) {
                LocalArTaxCode arTaxCode = arInvoice.getArTaxCode();
                // calculate net amount
                IL_AMNT = EJBCommon.calculateNetAmount(mdetails.getIlAmount(), mdetails.getIlTax(),
                        arTaxCode.getTcRate(), arTaxCode.getTcType(), precisionUnit);
                // calculate tax
                IL_TAX_AMNT = EJBCommon.calculateTaxAmount(mdetails.getIlAmount(), mdetails.getIlTax(),
                        arTaxCode.getTcRate(), arTaxCode.getTcType(), IL_AMNT, precisionUnit);
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
            arInvoiceLine.setArInvoice(arInvoice);

            try {
                LocalArStandardMemoLine arStandardMemoLine = arStandardMemoLineHome.findBySmlName(
                        mdetails.getIlSmlName(), companyCode, companyShortName);
                arInvoiceLine.setArStandardMemoLine(arStandardMemoLine);
            } catch (FinderException ex) {
                throw new ArInvoiceStandardMemolineDoesNotExist();
            }

            return arInvoiceLine;
        }
        catch (ArInvoiceStandardMemolineDoesNotExist ex) {
            throw new ArInvoiceStandardMemolineDoesNotExist(mdetails.getIlSmlName());
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalArInvoiceLineItem addArIliEntry(ArModInvoiceLineItemDetails mdetails,
                                                 LocalArInvoice arInvoice, LocalInvItemLocation itemLocation,
                                                 Integer companyCode, String companyShortName) {

        Debug.print("ArInvoiceEntryApiControllerBean addArIliEntry");
        try {
            short precisionUnit = this.getGlFcPrecisionUnit(companyCode, companyShortName);
            double itemAmount;
            double itemTaxAmount;
            LocalArTaxCode arTaxCode = arInvoice.getArTaxCode();
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
                    .buildInvoiceLineItem();
            arInvoiceLineItem.setArInvoice(arInvoice);
            arInvoiceLineItem.setInvItemLocation(itemLocation);
            LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(mdetails.getIliUomName(), companyCode);
            arInvoiceLineItem.setInvUnitOfMeasure(invUnitOfMeasure);
            return arInvoiceLineItem;
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertForeignToFunctionalCurrency(double conversionRate, double AMOUNT, Integer companyCode, String companyShortName) {

        Debug.print("ArInvoiceEntryApiControllerBean convertForeignToFunctionalCurrency");
        LocalAdCompany adCompany;
        // get company and extended precision
        try {
            adCompany = adCompanyHome.findByPrimaryKey(companyCode, companyShortName);
        }
        catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        // Convert to functional currency if necessary
        if (conversionRate != 1 && conversionRate != 0) {
            AMOUNT = AMOUNT / conversionRate;
        }
        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    private void addArDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT,
                              double DR_AMNT, Integer COA_CODE, Integer SC_COA,
                              LocalArInvoice arInvoice, Integer branchCode, Integer companyCode, String companyShortName)
            throws GlobalBranchAccountNumberInvalidException {
        Debug.print("ArInvoiceEntryApiControllerBean addArDrEntry");
        try {
            // get company
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode, companyShortName);
            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, branchCode, companyCode, companyShortName);
            // create distribution record
            if (DR_AMNT < 0) {
                DR_AMNT = DR_AMNT * -1;
                if (DR_DBT == 0) {
                    DR_DBT = 1;
                } else if (DR_DBT == 1) {
                    DR_DBT = 0;
                }
            }
            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome
                    .DrLine(DR_LN)
                    .DrClass(DR_CLSS)
                    .DrDebit(DR_DBT)
                    .DrAmount(EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()))
                    .DrAdCompany(companyCode)
                    .buildDistributionRecords(companyShortName);
            arDistributionRecord.setArInvoice(arInvoice);
            arDistributionRecord.setGlChartOfAccount(glChartOfAccount);
            if (DR_CLSS.equals("SC")) {
                if (SC_COA == null) {
                    throw new GlobalBranchAccountNumberInvalidException();
                } else {
                    arDistributionRecord.setDrScAccount(SC_COA);
                }
            }
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
}