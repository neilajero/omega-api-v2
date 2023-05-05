package com.ejb.txnapi.inv;

import com.ejb.ConfigurationClass;
import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.gl.*;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.gl.*;
import com.ejb.entities.inv.*;
import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.ejb.restfulapi.OfsApiResponse;
import com.ejb.restfulapi.inv.models.AdjustmentRequest;
import com.ejb.restfulapi.inv.models.ItemRequest;
import com.ejb.txnreports.inv.InvRepItemCostingController;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBCommonAPIErrCodes;
import com.util.EJBContextClass;
import com.util.inv.InvAdjustmentDetails;
import com.util.mod.inv.InvModAdjustmentLineDetails;

import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Stateless(name = "InvAdjustmentEntryApiControllerEJB")
public class InvAdjustmentEntryApiControllerBean extends EJBContextClass implements InvAdjustmentEntryApiController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    public InvRepItemCostingController ejbRIC;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalInvAdjustmentHome invAdjustmentHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalInvAdjustmentLineHome invAdjustmentLineHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalInvDistributionRecordHome invDistributionRecordHome;
    @EJB
    private ILocalGlSetOfBookHome glSetOfBookHome;
    @EJB
    private ILocalGlAccountingCalendarValueHome glAccountingCalendarValueHome;
    @EJB
    private LocalGlJournalHome glJournalHome;
    @EJB
    private LocalGlJournalLineHome glJournalLineHome;
    @EJB
    private LocalGlJournalSourceHome glJournalSourceHome;
    @EJB
    private LocalGlJournalCategoryHome glJournalCategoryHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private ILocalGlChartOfAccountBalanceHome glChartOfAccountBalanceHome;
    @EJB
    private LocalGlSuspenseAccountHome glSuspenseAccountHome;
    @EJB
    private LocalGlJournalBatchHome glJournalBatchHome;

    public Integer saveInventoryAdjustment(InvAdjustmentDetails details, ArrayList lineItems)
            throws GlobalInvItemLocationNotFoundException, GlJREffectiveDateNoPeriodExistException,
            GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException,
            AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException,
            GlobalMiscInfoIsRequiredException, GlobalRecordAlreadyDeletedException,
            GlobalBranchAccountNumberInvalidException {

        Debug.print("InvAdjustmentEntryControllerBean saveInvAdjEntry");

        Integer companyCode = details.getAdjAdCompany();
        Integer branchCode = details.getAdjAdBranch();

        try {

            LocalInvAdjustment invAdjustment;

            // Setup document sequence assignment
            generateDocumentNumber(details, companyCode, branchCode);

            // Create an inventory adjustment
            invAdjustment = invAdjustmentHome
                    .AdjDocumentNumber(details.getAdjDocumentNumber())
                    .AdjReferenceNumber(details.getAdjReferenceNumber())
                    .AdjDescription(details.getAdjDescription())
                    .AdjDate(details.getAdjDate())
                    .AdjType(details.getAdjType())
                    .AdjApprovalStatus(details.getAdjApprovalStatus())
                    .AdjCreatedBy(details.getAdjCreatedBy())
                    .AdjDateCreated(details.getAdjDateCreated())
                    .AdjAdBranch(branchCode)
                    .AdjAdCompany(companyCode)
                    .buildAdjustment();

            // Retrieve default adjustment account
            LocalGlChartOfAccount adjustmentCoa = setupDefaultAdjustmentAccount(details.getAdjType(), companyCode);
            invAdjustment.setGlChartOfAccount(adjustmentCoa);

            // Setup adjustment line and distribution records
            double TOTAL_AMOUNT = 0d;

            for (Object o : lineItems) {

                InvModAdjustmentLineDetails mdetails = (InvModAdjustmentLineDetails) o;
                LocalInvItemLocation invItemLocation = null;

                try {
                    invItemLocation = invItemLocationHome.findByLocNameAndIiName(mdetails.getAlLocName(),
                            mdetails.getAlIiName(), companyCode);
                }
                catch (FinderException ex) {
                    throw new GlobalInvItemLocationNotFoundException(String.valueOf(mdetails.getAlLineNumber()));
                }

                double COST = invItemLocation.getInvItem().getIiUnitCost();
                LocalInvCosting invCosting = invCostingHome.getItemAverageCost(
                        invAdjustment.getAdjDate(), invItemLocation.getInvItem().getIiName(),
                        invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);

                if (invCosting != null) {
                    if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Average")) {
                        if (invCosting.getCstRemainingQuantity() <= 0) {
                            COST = invItemLocation.getInvItem().getIiUnitCost();
                        } else {
                            COST = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                            if (COST <= 0) {
                                COST = mdetails.getInvItemLocation().getInvItem().getIiUnitCost();
                            }
                        }
                    }
                }

                mdetails.setAlUnitCost(COST);

                LocalInvAdjustmentLine invAdjustmentLine = this.addInvAlEntry(mdetails, invAdjustment, EJBCommon.FALSE, companyCode);

                // Inventory Journal
                TOTAL_AMOUNT = inventoryDR(companyCode, branchCode, invAdjustment, TOTAL_AMOUNT, invAdjustmentLine);

                // Negative committed quantity
                if (invAdjustmentLine.getAlAdjustQuantity() < 0) {

                    double convertedQuantity = this.convertByUomFromAndItemAndQuantity(
                            invAdjustmentLine.getInvUnitOfMeasure(),
                            invAdjustmentLine.getInvItemLocation().getInvItem(),
                            Math.abs(invAdjustmentLine.getAlAdjustQuantity()), companyCode);

                    invItemLocation = invAdjustmentLine.getInvItemLocation();
                    invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() + convertedQuantity);
                }
            }

            // Adjustment Journal
            adjustmentDR(companyCode, branchCode, invAdjustment, TOTAL_AMOUNT);

            invAdjustment.setAdjApprovalStatus("N/A");
            this.executeInvAdjPost(invAdjustment.getAdjCode(), invAdjustment.getAdjLastModifiedBy(), branchCode,
                    companyCode);

            return invAdjustment.getAdjCode();

        }
        catch (GlobalRecordAlreadyDeletedException | GlobalMiscInfoIsRequiredException |
               GlobalExpiryDateNotFoundException | AdPRFCoaGlVarianceAccountNotFoundException |
               GlobalJournalNotBalanceException | GlJREffectiveDatePeriodClosedException |
               GlJREffectiveDateNoPeriodExistException | GlobalInvItemLocationNotFoundException |
               GlobalBranchAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

    }

    public OfsApiResponse createAdjustment(AdjustmentRequest adjustmentRequest) {

        Debug.print("InvAdjustmentEntryApiControllerBean createAdjustment");

        OfsApiResponse apiResponse = new OfsApiResponse();
        LocalAdCompany adCompany = null;
        LocalAdBranch adBranch = null;
        LocalAdUser adUser = null;
        Date adjustmentDate = EJBCommon.convertStringToSQLDate(adjustmentRequest.getAdjustmentDate(), ConfigurationClass.DEFAULT_DATE_FORMAT);
        String defaultLocation = "DEFAULT";

        // Accepts only "I"-increase or "D"-decrease
        if ((!adjustmentRequest.getIdentifier().equalsIgnoreCase("D")) &&
                (!adjustmentRequest.getIdentifier().equalsIgnoreCase("I"))) {
            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_027);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_027_MSG);
            return apiResponse;
        }
        byte isDecrease = adjustmentRequest.getIdentifier().equalsIgnoreCase("D") ? EJBCommon.TRUE : EJBCommon.FALSE;

        try {
            InvAdjustmentDetails details = new InvAdjustmentDetails();
            // Company Code
            try {
                if (adjustmentRequest.getCompanyCode() == null || adjustmentRequest.getCompanyCode().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_041);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_041_MSG);
                    return apiResponse;
                }
                adCompany = adCompanyHome.findByCmpShortName(adjustmentRequest.getCompanyCode());
                details.setAdjAdCompany(adCompany.getCmpCode());
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
                return apiResponse;
            }

            // Branch Code
            try {
                if (adjustmentRequest.getBranchCode() == null || adjustmentRequest.getBranchCode().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return apiResponse;
                }
                adBranch = adBranchHome.findByBrBranchCode(adjustmentRequest.getBranchCode(), adCompany.getCmpCode());
                if (adBranch != null) {
                    details.setAdjAdBranch(adBranch.getBrCode());
                }
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_065);
                apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_065_MSG, adjustmentRequest.getBranchCode()));
                return apiResponse;
            }

            // User
            try {
                if (adjustmentRequest.getUsername() == null || adjustmentRequest.getUsername().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_042);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_042_MSG);
                    return apiResponse;
                }
                adUser = adUserHome.findByUsrName(adjustmentRequest.getUsername(), adCompany.getCmpCode());
                details.setAdjCreatedBy(adUser.getUsrName());
                details.setAdjLastModifiedBy(adUser.getUsrName());
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_002);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_002_MSG);
                return apiResponse;
            }

            ArrayList lineItems = new ArrayList();
            int lineItem = 1;

            // ITEMS
            for (ItemRequest itemRequest : adjustmentRequest.getItems()) {
                InvModAdjustmentLineDetails mdetails = new InvModAdjustmentLineDetails();
                LocalInvItem invItem = null;
                LocalInvItemLocation invItemLocation = null;
                LocalInvLocation invLocation = null;
                mdetails.setAlCode(null);
                mdetails.setAlLineNumber((short) lineItem);

                // Item Name
                try {
                    if (itemRequest.getItemName() == null || itemRequest.getItemName().equals("")) {
                        apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_045);
                        apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_045_MSG);
                        return apiResponse;
                    }
                    invItem = invItemHome.findByIiName(itemRequest.getItemName(), adCompany.getCmpCode());
                    mdetails.setAlIiName(invItem.getIiName());
                    mdetails.setAlIiDescription(invItem.getIiDescription());
                }
                catch (FinderException ex) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_010);
                    apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_010_MSG, itemRequest.getItemName()));
                    return apiResponse;
                }

                // Item Location
                mdetails.setAlLocName(defaultLocation);

                // confirm if item is in location
                try {
                    invItemLocation = invItemLocationHome.findByIiNameAndLocNameAndAdBranch(itemRequest.getItemName(),
                            defaultLocation, adBranch.getBrCode(), adCompany.getCmpCode());
                }
                catch (FinderException ex) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_011);
                    apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_011_MSG, itemRequest.getItemName()));
                    return apiResponse;
                }

                mdetails.setAlUomName(invItem.getInvUnitOfMeasure().getUomName());
                mdetails.setAlAdjustQuantity(isDecrease == EJBCommon.TRUE ? (itemRequest.getItemQuantity() * -1) : itemRequest.getItemQuantity());
                mdetails.setInvItemLocation(invItemLocation);

                if (invItem.getIiSalesPrice() == 0) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_026);
                    apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_026_MSG, itemRequest.getItemName()));
                    return apiResponse;
                }

                lineItems.add(mdetails);
                lineItem++;
            }

            String systemGenRefNumber = adjustmentRequest.getReferenceNumber();
            String systemGenDesc = adjustmentRequest.getDescription();
            if (systemGenRefNumber == null || systemGenRefNumber.equals("")) {
                systemGenRefNumber = "ADJ-REF-TT";
            }

            if (systemGenDesc == null || systemGenDesc.equals("")) {
                systemGenDesc = "DESC-TT" + adjustmentDate.toString();
            }

            details.setAdjType(adjustmentRequest.getAdjustmentType());
            details.setAdjReferenceNumber(systemGenRefNumber);
            details.setAdjDescription(systemGenDesc);
            details.setAdjDate(adjustmentDate);
            details.setAdjCreatedBy(adjustmentRequest.getUsername());
            details.setAdjDateCreated(new Date());

            int invAdjustmentCode = this.saveInventoryAdjustment(details, lineItems);

            LocalInvAdjustment invAdjustment = invAdjustmentHome.findByPrimaryKey(invAdjustmentCode);
            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            apiResponse.setDocumentNumber(invAdjustment.getAdjDocumentNumber());
            apiResponse.setStatus("Item updated successfully.");

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

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_028);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_028_MSG);
            return apiResponse;

        }
        catch (Exception ex) {
            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return apiResponse;
        }

        return apiResponse;
    }

    private void generateDocumentNumber(InvAdjustmentDetails details, Integer companyCode, Integer branchCode) {

        LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

        try {
            adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("INV ADJUSTMENT", companyCode);
            adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(
                    adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode);
        }
        catch (FinderException ex) {
        }

        if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' &&
                (details.getAdjDocumentNumber() == null || details.getAdjDocumentNumber().trim().length() == 0)) {

            while (true) {
                if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {
                    try {
                        invAdjustmentHome.findByAdjDocumentNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), branchCode, companyCode);
                        adDocumentSequenceAssignment.setDsaNextSequence(
                                EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                    }
                    catch (FinderException ex) {
                        details.setAdjDocumentNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                        adDocumentSequenceAssignment.setDsaNextSequence(
                                EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                        break;
                    }
                } else {
                    try {
                        invAdjustmentHome.findByAdjDocumentNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), branchCode, companyCode);
                        adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon
                                .incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                    }
                    catch (FinderException ex) {
                        details.setAdjDocumentNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                        adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon
                                .incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                        break;
                    }
                }
            }
        }
    }

    private void adjustmentDR(Integer companyCode, Integer branchCode, LocalInvAdjustment invAdjustment, double TOTAL_AMOUNT) throws GlobalBranchAccountNumberInvalidException {

        byte DEBIT;
        // add variance or transfer/debit distribution
        DEBIT = (TOTAL_AMOUNT >= 0 && !invAdjustment.getAdjType().equals("ISSUANCE")) ? EJBCommon.FALSE : EJBCommon.TRUE;

        this.addInvDrEntry(invAdjustment.getInvDrNextLine(), "ADJUSTMENT", DEBIT, Math.abs(TOTAL_AMOUNT),
                EJBCommon.FALSE, invAdjustment.getGlChartOfAccount().getCoaCode(), invAdjustment, branchCode, companyCode);
    }

    private double inventoryDR(Integer companyCode, Integer branchCode, LocalInvAdjustment invAdjustment, double TOTAL_AMOUNT, LocalInvAdjustmentLine invAdjustmentLine)
            throws FinderException, GlobalBranchAccountNumberInvalidException {

        byte DEBIT;
        double AMOUNT = EJBCommon.roundIt(invAdjustmentLine.getAlAdjustQuantity() * invAdjustmentLine.getAlUnitCost(), this.getGlFcPrecisionUnit(companyCode));
        DEBIT = (AMOUNT >= 0) ? EJBCommon.TRUE : EJBCommon.FALSE;

        // check for branch mapping
        LocalAdBranchItemLocation adBranchItemLocation = null;
        try {
            adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(invAdjustmentLine.getInvItemLocation().getIlCode(), branchCode, companyCode);
        }
        catch (FinderException ex) {
        }

        LocalGlChartOfAccount glInventoryChartOfAccount = null;
        if (adBranchItemLocation == null) {
            glInventoryChartOfAccount = glChartOfAccountHome.findByPrimaryKey(invAdjustmentLine.getInvItemLocation().getIlGlCoaInventoryAccount());
        } else {
            glInventoryChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchItemLocation.getBilCoaGlInventoryAccount());
        }

        this.addInvDrEntry(invAdjustment.getInvDrNextLine(), "INVENTORY", DEBIT, Math.abs(AMOUNT), EJBCommon.FALSE,
                glInventoryChartOfAccount.getCoaCode(), invAdjustment, branchCode, companyCode);

        TOTAL_AMOUNT += AMOUNT;
        return TOTAL_AMOUNT;
    }

    private LocalGlChartOfAccount setupDefaultAdjustmentAccount(String ADJ_TYP, Integer companyCode) {

        Debug.print("InvAdjustmentEntryApiControllerBean setupDefaultAdjustmentAccount");

        LocalGlChartOfAccount glChartOfAccount = null;

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            switch (ADJ_TYP) {
                case "GENERAL":
                    glChartOfAccount = glChartOfAccountHome
                            .findByPrimaryKey(adPreference.getPrfInvGeneralAdjustmentAccount());
                    break;
                case "WASTAGE":
                    glChartOfAccount = glChartOfAccountHome
                            .findByPrimaryKey(adPreference.getPrfInvWastageAdjustmentAccount());
                    break;
                case "VARIANCE":
                    glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adPreference.getPrfInvGlCoaVarianceAccount());
                    break;
                case "ISSUANCE":
                    glChartOfAccount = glChartOfAccountHome
                            .findByPrimaryKey(adPreference.getPrfInvIssuanceAdjustmentAccount());
                    break;
                case "PRODUCTION":
                    glChartOfAccount = glChartOfAccountHome
                            .findByPrimaryKey(adPreference.getPrfInvProductionAdjustmentAccount());
                    break;
            }

        }
        catch (FinderException ex) {
        }

        return glChartOfAccount;
    }

    private void addInvDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, byte DR_RVRSL,
                               Integer COA_CODE, LocalInvAdjustment invAdjustment, Integer branchCode, Integer companyCode)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("InvAdjustmentEntryApiControllerBean addInvDrEntry");

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

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem,
                                                      double ADJST_QTY, Integer companyCode) {

        Debug.print("InvAdjustmentEntryApiControllerBean convertByUomFromAndItemAndQuantity");

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
                    adPreference.getPrfInvCostPrecisionUnit());

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalInvAdjustmentLine addInvAlEntry(InvModAdjustmentLineDetails mdetails, LocalInvAdjustment invAdjustment,
                                                 byte AL_VD, Integer companyCode) throws GlobalMiscInfoIsRequiredException {

        Debug.print("InvAdjustmentEntryApiControllerBean addInvAlEntry");

        LocalInvAdjustmentLine invAdjustmentLine = null;
        LocalInvUnitOfMeasure invUnitOfMeasure = null;
        LocalInvItemLocation invItemLocation = null;

        try {

            invAdjustmentLine = invAdjustmentLineHome
                    .AlUnitCost(mdetails.getAlUnitCost())
                    .AlAdjustQuantity(mdetails.getAlAdjustQuantity())
                    .AlAdCompany(companyCode)
                    .buildAdjustmentLine();
            invAdjustmentLine.setInvAdjustment(invAdjustment);

            invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(mdetails.getAlUomName(), companyCode);
            if (invUnitOfMeasure != null) {
                invAdjustmentLine.setInvUnitOfMeasure(invUnitOfMeasure);
            }

            invItemLocation = invItemLocationHome.findByLocNameAndIiName(mdetails.getAlLocName(), mdetails.getAlIiName(), companyCode);
            if (invItemLocation != null) {
                invAdjustmentLine.setInvItemLocation(invItemLocation);
            }

            return invAdjustmentLine;

        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void executeInvAdjPost(Integer ADJ_CODE, String USR_NM, Integer branchCode, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException, GlJREffectiveDateNoPeriodExistException,
            GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException,
            GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException,
            GlobalExpiryDateNotFoundException, GlobalMiscInfoIsRequiredException {

        Debug.print("InvAdjustmentEntryControllerBean executeInvAdjPost");

        try {

            // validate if adjustment is already deleted
            LocalInvAdjustment invAdjustment = null;

            try {
                invAdjustment = invAdjustmentHome.findByPrimaryKey(ADJ_CODE);
            }
            catch (FinderException ex) {
                throw new GlobalRecordAlreadyDeletedException();
            }

            Collection invAdjustmentLines = null;
            if (invAdjustment.getAdjVoid() == EJBCommon.FALSE) {
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.FALSE, invAdjustment.getAdjCode(), companyCode);
            } else {
                invAdjustmentLines = invAdjustment.getInvAdjustmentLines();
            }

            for (Object adjustmentLine : invAdjustmentLines) {
                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) adjustmentLine;
                String II_NM = invAdjustmentLine.getInvItemLocation().getInvItem().getIiName();
                String LOC_NM = invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName();
                double ADJUST_QTY = this.convertByUomFromAndItemAndQuantity(invAdjustmentLine.getInvUnitOfMeasure(),
                        invAdjustmentLine.getInvItemLocation().getInvItem(), invAdjustmentLine.getAlAdjustQuantity(),
                        companyCode);
                LocalInvCosting invCosting = null;

                try {
                    invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                            invAdjustment.getAdjDate(), II_NM, LOC_NM, branchCode, companyCode);
                }
                catch (FinderException ex) {
                }

                double COST = invAdjustmentLine.getAlUnitCost();
                if (invCosting == null) {
                    if (invAdjustment.getAdjVoid() == EJBCommon.FALSE) {
                        this.postToInv(invAdjustmentLine, invAdjustment.getAdjDate(), ADJUST_QTY, COST * ADJUST_QTY,
                                ADJUST_QTY, COST * ADJUST_QTY, 0d, null, branchCode, companyCode);
                    } else {
                        this.postToInv(invAdjustmentLine, invAdjustment.getAdjDate(), -ADJUST_QTY, -COST * ADJUST_QTY,
                                -ADJUST_QTY, -COST * ADJUST_QTY, 0d, null, branchCode, companyCode);
                    }
                } else {
                    if (invAdjustment.getAdjVoid() == EJBCommon.FALSE) {
                        this.postToInv(invAdjustmentLine, invAdjustment.getAdjDate(), ADJUST_QTY, COST * ADJUST_QTY,
                                invCosting.getCstRemainingQuantity() + ADJUST_QTY,
                                invCosting.getCstRemainingValue() + (COST * ADJUST_QTY), 0d, null, branchCode, companyCode);
                    } else {
                        this.postToInv(invAdjustmentLine, invAdjustment.getAdjDate(), -ADJUST_QTY, -COST * ADJUST_QTY,
                                invCosting.getCstRemainingQuantity() - ADJUST_QTY,
                                invCosting.getCstRemainingValue() - (COST * ADJUST_QTY), 0d, null, branchCode, companyCode);
                    }
                }
            }

            invAdjustment.setAdjPosted(EJBCommon.TRUE);
            invAdjustment.setAdjPostedBy(USR_NM);
            invAdjustment.setAdjDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            // post to gl if necessary
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            if (adPreference.getPrfInvGlPostingType().equals("AUTO-POST UPON APPROVAL") || invAdjustment.getAdjIsCostVariance() == EJBCommon.TRUE) {

                // validate if date has no period and period is closed
                LocalGlSetOfBook glJournalSetOfBook = null;
                try {
                    glJournalSetOfBook = glSetOfBookHome.findByDate(invAdjustment.getAdjDate(), companyCode);
                }
                catch (FinderException ex) {
                    throw new GlJREffectiveDateNoPeriodExistException();
                }

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(
                        glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), invAdjustment.getAdjDate(), companyCode);

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
                        glOffsetJournalLine = glJournalLineHome.create((short) (invDistributionRecords.size() + 1),
                                EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);
                    } else {
                        glOffsetJournalLine = glJournalLineHome.create((short) (invDistributionRecords.size() + 1),
                                EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
                    }

                    LocalGlChartOfAccount glChartOfAccount = glSuspenseAccount.getGlChartOfAccount();
                    glOffsetJournalLine.setGlChartOfAccount(glChartOfAccount);

                } else if (Math.abs(EJBCommon.roundIt(TOTAL_DEBIT - TOTAL_CREDIT, adCompany.getGlFunctionalCurrency().getFcPrecision())) == 0.01 ||
                        (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE && TOTAL_DEBIT != TOTAL_CREDIT)) {
                    throw new GlobalJournalNotBalanceException();
                }

                // create journal batch if necessary
                LocalGlJournalBatch glJournalBatch = null;
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");
                try {
                    glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " INVENTORY ADJUSTMENTS", branchCode, companyCode);
                }
                catch (FinderException ex) {
                }

                if (glJournalBatch == null) {
                    glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " INVENTORY ADJUSTMENTS",
                            "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, branchCode, companyCode);
                }

                // create journal entry
                LocalGlJournal glJournal = glJournalHome.create(invAdjustment.getAdjReferenceNumber(),
                        invAdjustment.getAdjDescription(), invAdjustment.getAdjDate(), 0.0d, null,
                        invAdjustment.getAdjDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE,
                        EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM,
                        EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, EJBCommon.FALSE, null, branchCode,
                        companyCode);

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
                    //TODO: Validated why there is a setting to set a DR to imported...
                    //invDistributionRecord.setDrImported(EJBCommon.TRUE);
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
                    Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(
                            glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), glAccountingCalendarValue.getAcvPeriodNumber(), companyCode);

                    for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {
                        LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;
                        this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false,
                                glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                    }

                    // post to subsequent years if necessary
                    Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(
                            glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode);

                    if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {
                        adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                        LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome
                                .findByCoaAccountNumberAndBranchCode(adCompany.getCmpRetainedEarnings(), branchCode, companyCode);

                        for (Object subsequentSetOfBook : glSubsequentSetOfBooks) {
                            LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) subsequentSetOfBook;
                            String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                            // post to subsequent acvs of subsequent set of book(propagate)
                            Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(
                                    glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                            for (Object accountingCalendarValue : glAccountingCalendarValues) {
                                LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                                if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY") || ACCOUNT_TYPE.equals("OWNERS EQUITY")) {
                                    this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(),
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
               AdPRFCoaGlVarianceAccountNotFoundException | GlobalRecordAlreadyDeletedException |
               GlobalJournalNotBalanceException | GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToInv(LocalInvAdjustmentLine invAdjustmentLine, Date CST_DT, double CST_ADJST_QTY,
                           double CST_ADJST_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, double CST_VRNC_VL, String USR_NM,
                           Integer branchCode, Integer companyCode)
            throws AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException {

        Debug.print("InvAdjustmentEntryApiControllerBean postToInv");

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

            try {
                // generate line number
                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(
                        CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;
            }
            catch (FinderException ex) {
                CST_LN_NMBR = 1;
            }

            if (CST_VRNC_VL != 0) {
                // void subsequent cost variance adjustments
                Collection invAdjustmentLines = invAdjustmentLineHome.findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(
                        CST_DT, invItemLocation.getIlCode(), branchCode, companyCode);
                for (Object adjustmentLine : invAdjustmentLines) {
                    LocalInvAdjustmentLine invAdjustmentLineTemp = (LocalInvAdjustmentLine) adjustmentLine;
                    this.voidInvAdjustment(invAdjustmentLineTemp.getInvAdjustment(), branchCode, companyCode);
                }
            }

            String prevExpiryDates = "";
            String miscListPrpgt = "";
            double qtyPrpgt = 0;
            try {
                LocalInvCosting prevCst = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndRemainingQuantityNotEqualToZeroAndIlCode(
                        CST_DT, invItemLocation.getIlCode(), branchCode, companyCode);
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
                    CST_ADJST_QTY, CST_ADJST_CST, 0d, 0d, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, 0d, branchCode, companyCode);

            invCosting.setCstQCNumber(invAdjustmentLine.getAlQcNumber());
            invCosting.setCstQCExpiryDate(invAdjustmentLine.getAlQcExpiryDate());
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setInvAdjustmentLine(invAdjustmentLine);
            invCosting.setCstQuantity(CST_ADJST_QTY);
            invCosting.setCstCost(CST_ADJST_CST);

            // if cost variance is not 0, generate cost variance for the transaction
            if (CST_VRNC_VL != 0) {
                this.generateCostVariance(invCosting.getInvItemLocation(), CST_VRNC_VL,
                        "INVADJ" + invAdjustmentLine.getInvAdjustment().getAdjDocumentNumber(),
                        invAdjustmentLine.getInvAdjustment().getAdjDescription(),
                        invAdjustmentLine.getInvAdjustment().getAdjDate(), USR_NM, branchCode, companyCode);
            }

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT,
                    invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode,
                    companyCode);
            for (Object costing : invCostings) {
                LocalInvCosting invPropagatedCosting = (LocalInvCosting) costing;
                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_ADJST_QTY);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_ADJST_CST);
                if (CST_ADJST_QTY > 0) {
                    invPropagatedCosting.setCstRemainingLifoQuantity(invPropagatedCosting.getCstRemainingLifoQuantity() + CST_ADJST_QTY);
                }
            }
            // regenerate cost variance
            this.regenerateCostVariance(invCostings, invCosting, branchCode, companyCode);

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

    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue,
                          LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double JL_AMNT,
                          Integer companyCode) {

        Debug.print("InvAdjustmentEntryApiControllerBean postToGl");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccountBalance glChartOfAccountBalance = glChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(
                    glAccountingCalendarValue.getAcvCode(), glChartOfAccount.getCoaCode(), companyCode);

            String ACCOUNT_TYPE = glChartOfAccount.getCoaAccountType();
            short FC_EXTNDD_PRCSN = adCompany.getGlFunctionalCurrency().getFcExtendedPrecision();

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

    private void voidInvAdjustment(LocalInvAdjustment invAdjustment, Integer branchCode, Integer companyCode) {

        Debug.print("ApVoucherPostController voidInvAdjustment");

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
                InvModAdjustmentLineDetails details = new InvModAdjustmentLineDetails();

                details.setAlAdjustQuantity(0);
                details.setAlUnitCost(invAdjustmentLine.getAlUnitCost() * -1);
                details.setAlIiName(invAdjustmentLine.getInvItemLocation().getInvItem().getIiName());
                details.setAlLocName(invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName());
                details.setAlUomName(
                        invAdjustmentLine.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName());

                this.addInvAlEntry(details, invAdjustment, EJBCommon.TRUE, companyCode);
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

    private void generateCostVariance(LocalInvItemLocation invItemLocation, double CST_VRNC_VL, String ADJ_RFRNC_NMBR,
                                      String ADJ_DSCRPTN, Date ADJ_DT, String USR_NM, Integer branchCode, Integer companyCode)
            throws AdPRFCoaGlVarianceAccountNotFoundException {

    }

    private void regenerateCostVariance(Collection invCostings, LocalInvCosting invCosting, Integer branchCode,
                                        Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

    }

    private short getGlFcPrecisionUnit(Integer companyCode) {

        Debug.print("InvAdjustmentEntryApiControllerBean getGlFcPrecisionUnit");

        try {
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            return adCompany.getGlFunctionalCurrency().getFcPrecision();
        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

}