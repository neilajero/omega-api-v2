package com.ejb.txnapi.inv;

import com.ejb.ConfigurationClass;
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
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBCommonAPIErrCodes;
import com.util.EJBContextClass;
import com.util.inv.InvStockTransferDetails;
import com.util.mod.inv.InvModStockTransferLineDetails;

import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Stateless(name = "InvStockTransferEntryApiControllerEJB")
public class InvStockTransferEntryApiControllerBean extends EJBContextClass
        implements InvStockTransferEntryApiController {

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
    private LocalInvStockTransferHome invStockTransferHome;
    @EJB
    private LocalInvStockTransferLineHome invStockTransferLineHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvDistributionRecordHome invDistributionRecordHome;
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
    private LocalInvAdjustmentHome invAdjustmentHome;
    @EJB
    private LocalInvAdjustmentLineHome invAdjustmentLineHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalAdBranchHome adBranchHome;

    @Override
    public Integer saveStockTransfer(InvStockTransferDetails details, ArrayList lineItems)
            throws GlobalRecordAlreadyDeletedException, GlobalInvItemLocationNotFoundException, GlJREffectiveDateNoPeriodExistException,
            GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalInventoryDateException,
            AdPRFCoaGlVarianceAccountNotFoundException, GlobalRecordInvalidException,
            GlobalBranchAccountNumberInvalidException {

        Debug.print("InvStockTransferEntryApiControllerBean saveStockTransfer");

        Integer companyCode = details.getStAdCompany();
        Integer branchCode = details.getStAdBranch();

        try {

            LocalInvStockTransfer invStockTransfer = null;
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

            try {
                adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("INV STOCK TRANSFER", companyCode);
                adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(
                        adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode);
            }
            catch (FinderException ex) {
            }

            if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' &&
                    (details.getStDocumentNumber() == null || details.getStDocumentNumber().trim().length() == 0)) {
                while (true) {
                    if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {
                        try {
                            invStockTransferHome.findByStDocumentNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), branchCode, companyCode);
                            adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                        }
                        catch (FinderException ex) {
                            details.setStDocumentNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                            adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                            break;
                        }
                    } else {
                        try {
                            invStockTransferHome.findByStDocumentNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), branchCode, companyCode);
                            adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                        }
                        catch (FinderException ex) {
                            details.setStDocumentNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                            adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                            break;
                        }
                    }
                }
            }

            // create stock transfer
            invStockTransfer = invStockTransferHome
                    .StDocumentNumber(details.getStDocumentNumber())
                    .StReferenceNumber(details.getStReferenceNumber())
                    .StDescription(details.getStDescription())
                    .StDate(details.getStDate())
                    .StApprovalStatus("N/A")
                    .StCreatedBy(details.getStCreatedBy())
                    .StDateCreated(details.getStDateCreated())
                    .StAdBranch(branchCode)
                    .StAdCompany(companyCode)
                    .buildStockTransfer();

            double ABS_TOTAL_AMOUNT = 0d;

            // add new stock transfer entry lines and distribution record
            byte DEBIT = 0;

            for (Object o : lineItems) {
                InvModStockTransferLineDetails mdetails = (InvModStockTransferLineDetails) o;
                LocalInvItemLocation invItemLocFrom = null;
                LocalInvItemLocation invItemLocTo = null;

                // Item Location From
                try {
                    invItemLocFrom = invItemLocationHome.findByLocNameAndIiName(mdetails.getStlLocationNameFrom(), mdetails.getStlIiName(), companyCode);
                }
                catch (FinderException ex) {
                    throw new GlobalInvItemLocationNotFoundException(mdetails.getStlLineNumber() + " - " + mdetails.getStlLocationNameFrom());
                }

                // Item Location To
                try {
                    invItemLocTo = invItemLocationHome.findByLocNameAndIiName(mdetails.getStlLocationNameTo(), mdetails.getStlIiName(), companyCode);
                }
                catch (FinderException ex) {
                    throw new GlobalInvItemLocationNotFoundException(mdetails.getStlLineNumber() + " - " + mdetails.getStlLocationNameTo());
                }

                // start date validation
                if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                    Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(
                            invStockTransfer.getStDate(), invItemLocFrom.getInvItem().getIiName(),
                            invItemLocFrom.getInvLocation().getLocName(), branchCode, companyCode);

                    if (!invNegTxnCosting.isEmpty()) {
                        throw new GlobalInventoryDateException(invItemLocFrom.getInvItem().getIiName());
                    }

                    invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(
                            invStockTransfer.getStDate(), invItemLocTo.getInvItem().getIiName(),
                            invItemLocTo.getInvLocation().getLocName(), branchCode, companyCode);

                    if (!invNegTxnCosting.isEmpty()) {
                        throw new GlobalInventoryDateException(invItemLocTo.getInvItem().getIiName());
                    }
                }

                LocalInvStockTransferLine invStockTransferLine = this.addInvStlEntry(mdetails, invStockTransfer, companyCode);

                // add physical inventory distribution
                double COST = this.getInvIiUnitCostByIiNameAndLocFromAndUomNameAndDate(
                        invItemLocFrom.getInvItem().getIiName(), invItemLocFrom.getInvLocation().getLocName(),
                        invStockTransferLine.getInvUnitOfMeasure().getUomName(), invStockTransfer.getStDate(), branchCode,
                        companyCode);

                double AMOUNT = 0d;
                AMOUNT = EJBCommon.roundIt(invStockTransferLine.getStlQuantityDelivered() * COST, this.getGlFcPrecisionUnit(companyCode));

                // check branch mapping
                LocalAdBranchItemLocation adBranchItemLocation = null;
                //TODO: Since this is stock transfer the branch name should be changed
                LocalAdBranch adBranchTo = adBranchHome.findByBrName(mdetails.getStlLocationNameTo(), companyCode);
                try {
                    adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(invItemLocTo.getIlCode(), adBranchTo.getBrCode(), companyCode);
                }
                catch (FinderException ex) {
                }

                LocalGlChartOfAccount glChartOfAccountTo = null;
                if (adBranchItemLocation == null) {
                    glChartOfAccountTo = glChartOfAccountHome.findByPrimaryKey(invItemLocTo.getIlGlCoaInventoryAccount());
                } else {
                    glChartOfAccountTo = glChartOfAccountHome.findByPrimaryKey(adBranchItemLocation.getBilCoaGlInventoryAccount());
                }
                this.addInvDrEntry(invStockTransfer.getInvDrNextLine(), "INVENTORY", EJBCommon.TRUE, Math.abs(AMOUNT),
                        glChartOfAccountTo.getCoaCode(), invStockTransfer, adBranchTo.getBrCode(), companyCode);

                try {
                    adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(invItemLocFrom.getIlCode(), branchCode, companyCode);
                }
                catch (FinderException ex) {
                }

                LocalGlChartOfAccount glChartOfAccountFrom = null;
                if (adBranchItemLocation == null) {
                    glChartOfAccountFrom = glChartOfAccountHome.findByPrimaryKey(invItemLocFrom.getIlGlCoaInventoryAccount());
                } else {
                    glChartOfAccountFrom = glChartOfAccountHome.findByPrimaryKey(adBranchItemLocation.getBilCoaGlInventoryAccount());
                }
                this.addInvDrEntry(invStockTransfer.getInvDrNextLine(), "INVENTORY", EJBCommon.FALSE, Math.abs(AMOUNT),
                        glChartOfAccountFrom.getCoaCode(), invStockTransfer, branchCode, companyCode);

                ABS_TOTAL_AMOUNT += Math.abs(AMOUNT);

                // set ilCommittedQuantity
                double convertedQuantity = this.convertByUomFromAndItemAndQuantity(
                        invStockTransferLine.getInvUnitOfMeasure(), invItemLocFrom.getInvItem(),
                        invStockTransferLine.getStlQuantityDelivered(), companyCode);

                invItemLocFrom.setIlCommittedQuantity(invItemLocFrom.getIlCommittedQuantity() + convertedQuantity);
            }

            // insufficient stock checking
            if (adPreference.getPrfArCheckInsufficientStock() == EJBCommon.TRUE) {

                boolean hasInsufficientItems = false;
                StringBuilder insufficientItems = new StringBuilder();

                Collection invStockTransferLines = null;
                try {
                    invStockTransferLines = invStockTransferLineHome.findByStCode(invStockTransfer.getStCode(), companyCode);
                }
                catch (FinderException ex) {
                    throw new GlobalRecordAlreadyDeletedException();
                }

                for (Object stockTransferLine : invStockTransferLines) {
                    LocalInvStockTransferLine invStockTransferLine = (LocalInvStockTransferLine) stockTransferLine;
                    String LOC_NM_FRM = this.getInvLocNameByLocCode(invStockTransferLine.getStlLocationFrom());
                    double ST_QTY = this.convertByUomFromAndItemAndQuantity(invStockTransferLine.getInvUnitOfMeasure(),
                            invStockTransferLine.getInvItem(), invStockTransferLine.getStlQuantityDelivered(), companyCode);
                    String II_NM = invStockTransferLine.getInvItem().getIiName();

                    LocalInvCosting invCosting = null;
                    try {
                        invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                invStockTransfer.getStDate(), II_NM, LOC_NM_FRM, branchCode, companyCode);
                    }
                    catch (FinderException ex) {
                    }

                    double LOWEST_QTY = this.convertByUomAndQuantity(invStockTransferLine.getInvUnitOfMeasure(), invStockTransferLine.getInvItem(), 1, companyCode);
                    if (invCosting == null ||
                            invCosting.getCstRemainingQuantity() == 0 ||
                            invCosting.getCstRemainingQuantity() - ST_QTY <= -LOWEST_QTY) {
                        hasInsufficientItems = true;
                        insufficientItems.append(invStockTransferLine.getInvItem().getIiName()).append(", ");
                    }
                }
                if (hasInsufficientItems) {
                    throw new GlobalRecordInvalidException(insufficientItems.substring(0, insufficientItems.lastIndexOf(",")));
                }
            }

            this.executeInvStPost(invStockTransfer.getStCode(), invStockTransfer.getStLastModifiedBy(), branchCode, companyCode);

            // set stock transfer approval status
            invStockTransfer.setStApprovalStatus("N/A");
            return invStockTransfer.getStCode();

        }
        catch (GlobalRecordAlreadyDeletedException | GlobalRecordInvalidException |
               AdPRFCoaGlVarianceAccountNotFoundException | GlobalBranchAccountNumberInvalidException |
               GlobalInventoryDateException | GlJREffectiveDatePeriodClosedException |
               GlJREffectiveDateNoPeriodExistException | GlobalInvItemLocationNotFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalInvStockTransferLine addInvStlEntry(InvModStockTransferLineDetails mdetails, LocalInvStockTransfer invStockTransfer, Integer companyCode) {

        Debug.print("InvStockTransferEntryApiControllerBean addInvStlEntry");

        try {

            // Find Data
            LocalInvLocation invLocFrom = invLocationHome.findByLocName(mdetails.getStlLocationNameFrom(), companyCode);
            LocalInvLocation invLocTo = invLocationHome.findByLocName(mdetails.getStlLocationNameTo(), companyCode);

            LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(mdetails.getStlUomName(), companyCode);
            LocalInvItem invItem = invItemHome.findByIiName(mdetails.getStlIiName(), companyCode);

            // Create Stock Transfer Line
            LocalInvStockTransferLine invStockTransferLine = invStockTransferLineHome
                    .StlLocationFrom(invLocFrom.getLocCode())
                    .StlLocationTo(invLocTo.getLocCode())
                    .StlUnitCost(mdetails.getStlUnitCost())
                    .StlQuantityDelivered(mdetails.getStlQuantityDelivered())
                    .StlAmount(mdetails.getStlAmount())
                    .StlAdCompany(companyCode)
                    .build();

            // Add Stock Transfer Line
            invStockTransferLine.setInvStockTransfer(invStockTransfer);
            invStockTransferLine.setInvUnitOfMeasure(invUnitOfMeasure);
            invStockTransferLine.setInvItem(invItem);

            return invStockTransferLine;

        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double getInvIiUnitCostByIiNameAndLocFromAndUomNameAndDate(String II_NM, String LOC_FRM, String UOM_NM,
                                                                       Date ST_DT, Integer branchCode, Integer companyCode)
            throws GlobalInvItemLocationNotFoundException {

        Debug.print("InvStockTransferEntryApiControllerBean getInvIiUnitCostByIiNameAndLocFromAndUomNameAndDate");

        try {
            LocalInvItemLocation invItemLocation = null;
            try {
                invItemLocation = invItemLocationHome.findByLocNameAndIiName(LOC_FRM, II_NM, companyCode);
            }
            catch (FinderException ex) {
                throw new GlobalInvItemLocationNotFoundException(LOC_FRM + " for item " + II_NM);
            }

            double COST = invItemLocation.getInvItem().getIiUnitCost();
            try {

                LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                        ST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);

                if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Average")) {
                    if (invCosting.getCstRemainingQuantity() <= 0) {
                        COST = invItemLocation.getInvItem().getIiUnitCost();
                    } else {
                        COST = EJBCommon.roundIt(Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity()), this.getGlFcPrecisionUnit(companyCode));
                        if (COST <= 0) {
                            COST = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                        }
                    }
                }
            }
            catch (FinderException ex) {
                COST = invItemLocation.getInvItem().getIiUnitCost();
            }

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, UOM_NM, companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(
                    II_NM, invItemLocation.getInvItem().getInvUnitOfMeasure().getUomName(), companyCode);

            return EJBCommon.roundIt(COST * invDefaultUomConversion.getUmcConversionFactor() /
                    invUnitOfMeasureConversion.getUmcConversionFactor(), this.getGlFcPrecisionUnit(companyCode));

        }
        catch (GlobalInvItemLocationNotFoundException ex) {
            throw ex;
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private short getGlFcPrecisionUnit(Integer companyCode) {

        Debug.print("InvStockTransferEntryApiControllerBean getGlFcPrecisionUnit");

        try {
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            return adCompany.getGlFunctionalCurrency().getFcPrecision();
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void addInvDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE,
                               LocalInvStockTransfer invStockTransfer, Integer branchCode, Integer companyCode)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("InvStockTransferEntryApiControllerBean addInvDrEntry");

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
                    EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE,
                    EJBCommon.FALSE, companyCode);

            invStockTransfer.addInvDistributionRecord(invDistributionRecord);
            glChartOfAccount.addInvDistributionRecord(invDistributionRecord);

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

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, double ADJST_QTY, Integer companyCode) {

        Debug.print("InvStockTransferEntryApiControllerBean convertByUomFromAndUomToAndQuantity");

        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(
                    invItem.getIiName(), invFromUnitOfMeasure.getUomName(), companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(
                    invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), companyCode);

            return EJBCommon.roundIt(ADJST_QTY * invDefaultUomConversion.getUmcConversionFactor() /
                    invUnitOfMeasureConversion.getUmcConversionFactor(), adPreference.getPrfInvQuantityPrecisionUnit());

        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private String getInvLocNameByLocCode(Integer LOC_CODE) {

        Debug.print("InvStockTransferEntryApiControllerBean getInvLocNameByLocCode");

        try {
            return invLocationHome.findByPrimaryKey(LOC_CODE).getLocName();
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertByUomAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, double ADJST_QTY, Integer companyCode) {

        Debug.print("InvStockTransferEntryApiControllerBean convertByUomFromAndUomToAndQuantity");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(
                    invItem.getIiName(), invFromUnitOfMeasure.getUomName(), companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(
                    invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), companyCode);

            return EJBCommon.roundIt(ADJST_QTY * invDefaultUomConversion.getUmcConversionFactor() /
                    invUnitOfMeasureConversion.getUmcConversionFactor(), adPreference.getPrfInvQuantityPrecisionUnit());

        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void executeInvStPost(Integer ST_CODE, String USR_NM, Integer branchCode, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException,
            GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("InvStockTransferEntryApiControllerBean executeInvStPost");

        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate if stock transfer is already deleted
            LocalInvStockTransfer invStockTransfer = null;
            try {
                invStockTransfer = invStockTransferHome.findByPrimaryKey(ST_CODE);
            }
            catch (FinderException ex) {
                throw new GlobalRecordAlreadyDeletedException();
            }

            Collection invStockTransferLines = null;
            try {
                invStockTransferLines = invStockTransferLineHome.findByStCode(ST_CODE, companyCode);
            }
            catch (FinderException ex) {
                throw new GlobalRecordAlreadyDeletedException();
            }

            // regenearte inventory dr
            boolean hasInsufficientItems = false;
            String insufficientItems = "";
            Iterator i = invStockTransferLines.iterator();
            while (i.hasNext()) {
                LocalInvStockTransferLine invStockTransferLine = (LocalInvStockTransferLine) i.next();
                double ST_QTY = this.convertByUomFromAndItemAndQuantity(invStockTransferLine.getInvUnitOfMeasure(),
                        invStockTransferLine.getInvItem(), invStockTransferLine.getStlQuantityDelivered(), companyCode);

                String II_NM = invStockTransferLine.getInvItem().getIiName();
                String LOC_NM_FRM = this.getInvLocNameByLocCode(invStockTransferLine.getStlLocationFrom());
                LocalInvItemLocation invItemLocationFrom = invItemLocationHome.findByLocNameAndIiName(LOC_NM_FRM, invStockTransferLine.getInvItem().getIiName(), companyCode);

                LocalInvCosting invCostingFrom = null;
                try {
                    invCostingFrom = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                            invStockTransfer.getStDate(), II_NM, LOC_NM_FRM, branchCode, companyCode);
                }
                catch (FinderException ex) {
                }

                double COST = invStockTransferLine.getInvItem().getIiUnitCost();
                if (invCostingFrom == null) {
                    this.postToInv(invStockTransferLine, invItemLocationFrom, invStockTransfer.getStDate(), -ST_QTY,
                            -COST * ST_QTY, -ST_QTY, -COST * ST_QTY, 0d, null, branchCode, companyCode);
                } else {
                    this.postToInv(invStockTransferLine, invItemLocationFrom, invStockTransfer.getStDate(), -ST_QTY,
                            -COST * ST_QTY, invCostingFrom.getCstRemainingQuantity() - ST_QTY,
                            invCostingFrom.getCstRemainingValue() - (COST * ST_QTY), 0d, null, branchCode, companyCode);
                }

                String LOC_NM_TO = this.getInvLocNameByLocCode(invStockTransferLine.getStlLocationTo());
                LocalInvItemLocation invItemLocationTo = invItemLocationHome.findByLocNameAndIiName(LOC_NM_TO, invStockTransferLine.getInvItem().getIiName(), companyCode);

                //TODO: Since this is stock transfer the branch name should be changed
                LocalAdBranch adBranchTo = adBranchHome.findByBrName(LOC_NM_TO, companyCode);

                LocalInvCosting invCostingTo = null;
                try {
                    invCostingTo = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                            invStockTransfer.getStDate(), II_NM, LOC_NM_TO, adBranchTo.getBrCode(), companyCode);
                }
                catch (FinderException ex) {
                }

                if (invCostingTo == null) {
                    this.postToInv(invStockTransferLine, invItemLocationTo, invStockTransfer.getStDate(), ST_QTY,
                            COST * ST_QTY, ST_QTY, COST * ST_QTY, 0d, null, adBranchTo.getBrCode(), companyCode);
                } else {
                    this.postToInv(invStockTransferLine, invItemLocationTo, invStockTransfer.getStDate(), ST_QTY,
                            ST_QTY * COST, invCostingTo.getCstRemainingQuantity() + ST_QTY,
                            invCostingTo.getCstRemainingValue() + (COST * ST_QTY), 0d, USR_NM, adBranchTo.getBrCode(), companyCode);
                }
            }

            // set invoice post status
            invStockTransfer.setStPosted(EJBCommon.TRUE);
            invStockTransfer.setStPostedBy(USR_NM);
            invStockTransfer.setStDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            // post to GL
            // validate if date has no period and period is closed
            LocalGlSetOfBook glJournalSetOfBook = null;
            try {
                glJournalSetOfBook = glSetOfBookHome.findByDate(invStockTransfer.getStDate(), companyCode);
            }
            catch (FinderException ex) {
                throw new GlJREffectiveDateNoPeriodExistException();
            }

            LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(
                    glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), invStockTransfer.getStDate(), companyCode);

            if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P') {
                throw new GlJREffectiveDatePeriodClosedException();
            }

            // check if debit and credit is balance
            LocalGlJournalLine glOffsetJournalLine = null;
            Collection invDistributionRecords = invDistributionRecordHome.findImportableDrByStCode(invStockTransfer.getStCode(), companyCode);
            i = invDistributionRecords.iterator();
            double TOTAL_DEBIT = 0d;
            double TOTAL_CREDIT = 0d;

            while (i.hasNext()) {
                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) i.next();
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

            // create journal entry
            LocalGlJournal glJournal = glJournalHome.create(invStockTransfer.getStReferenceNumber(),
                    invStockTransfer.getStDescription(), invStockTransfer.getStDate(), 0.0d, null,
                    invStockTransfer.getStDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE,
                    USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM,
                    EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, EJBCommon.FALSE, null, branchCode,
                    companyCode);

            LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("INVENTORY", companyCode);
            glJournal.setGlJournalSource(glJournalSource);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
            glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("STOCK TRANSFERS", companyCode);
            glJournal.setGlJournalCategory(glJournalCategory);

            // create journal lines
            i = invDistributionRecords.iterator();
            while (i.hasNext()) {
                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) i.next();
                double DR_AMNT = 0d;
                DR_AMNT = invDistributionRecord.getDrAmount();
                LocalGlJournalLine glJournalLine = glJournalLineHome.create(invDistributionRecord.getDrLine(), invDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);
                invDistributionRecord.getInvChartOfAccount().addGlJournalLine(glJournalLine);
                glJournal.addGlJournalLine(glJournalLine);
                //invDistributionRecord.setDrImported(EJBCommon.TRUE);
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
                Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(
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
                    LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(
                            adCompany.getCmpRetainedEarnings(), branchCode, companyCode);

                    for (Object subsequentSetOfBook : glSubsequentSetOfBooks) {
                        LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) subsequentSetOfBook;
                        String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                        // post to subsequent acvs of subsequent set of book(propagate)
                        Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(
                                glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                        for (Object accountingCalendarValue : glAccountingCalendarValues) {
                            LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                            if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY") || ACCOUNT_TYPE.equals("OWNERS EQUITY")) {
                                this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(),
                                        false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
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
        catch (GlJREffectiveDateNoPeriodExistException | AdPRFCoaGlVarianceAccountNotFoundException |
               GlobalRecordAlreadyDeletedException | GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToInv(LocalInvStockTransferLine invStockTransferLine, LocalInvItemLocation invItemLocation,
                           Date CST_DT, double CST_ST_QTY, double CST_ST_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL,
                           double CST_VRNC_VL, String USR_NM, Integer branchCode, Integer companyCode)
            throws AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("InvStockTransferEntryApiControllerBean postToInv");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            int CST_LN_NMBR = 0;

            CST_ST_QTY = EJBCommon.roundIt(CST_ST_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_ST_CST = EJBCommon.roundIt(CST_ST_CST, adCompany.getGlFunctionalCurrency().getFcPrecision());
            CST_RMNNG_QTY = EJBCommon.roundIt(CST_RMNNG_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_RMNNG_VL = EJBCommon.roundIt(CST_RMNNG_VL, adCompany.getGlFunctionalCurrency().getFcPrecision());

            if (CST_ST_QTY < 0) {
                invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - Math.abs(CST_ST_QTY));
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
                    LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) adjustmentLine;
                    this.voidInvAdjustment(invAdjustmentLine.getInvAdjustment(), branchCode, companyCode);
                }
            }

            String prevExpiryDates = "";
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
            }

            // create costing
            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d,
                    CST_ST_QTY, CST_ST_CST, 0d, 0d, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d,
                    CST_ST_QTY > 0 ? CST_ST_QTY : 0, branchCode, companyCode);

            invItemLocation.addInvCosting(invCosting);
            invCosting.setInvStockTransferLine(invStockTransferLine);
            invCosting.setCstQuantity(CST_ST_QTY);
            invCosting.setCstCost(CST_ST_CST);

            // if cost variance is not 0, generate cost variance for the transaction
            if (CST_VRNC_VL != 0) {
                this.generateCostVariance(invCosting.getInvItemLocation(), CST_VRNC_VL,
                        "INVST" + invStockTransferLine.getInvStockTransfer().getStDocumentNumber(),
                        invStockTransferLine.getInvStockTransfer().getStDescription(),
                        invStockTransferLine.getInvStockTransfer().getStDate(), USR_NM, branchCode, companyCode);
            }

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT,
                    invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode,
                    companyCode);
            for (Object costing : invCostings) {
                LocalInvCosting invPropagatedCosting = (LocalInvCosting) costing;
                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_ST_QTY);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_ST_CST);
                if (CST_ST_QTY > 0) {
                    invPropagatedCosting.setCstRemainingLifoQuantity(invPropagatedCosting.getCstRemainingLifoQuantity() + CST_ST_QTY);
                }
            }
            // regenerate cost variance
            this.regenerateCostVariance(invCostings, invCosting, branchCode, companyCode);

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

    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue,
                          LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double JL_AMNT,
                          Integer companyCode) {

        Debug.print("InvStockTransferEntryApiControllerBean postToGl");

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

    private void voidInvAdjustment(LocalInvAdjustment invAdjustment, Integer branchCode, Integer companyCode) {

        Debug.print("InvStockTransferEntryApiControllerBean voidInvAdjustment");

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

    private String getQuantityExpiryDates(String qntty) {

        Debug.print("InvStockTransferEntryApiControllerBean getQuantityExpiryDates");

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

    private LocalInvAdjustmentLine addInvAlEntry(LocalInvItemLocation invItemLocation, LocalInvAdjustment invAdjustment,
                                                 double CST_VRNC_VL, byte AL_VD, Integer companyCode) {

        Debug.print("InvStockTransferEntryApiControllerBean addInvAlEntry");

        try {

            // create dr entry
            LocalInvAdjustmentLine invAdjustmentLine = null;
            invAdjustmentLine = invAdjustmentLineHome.create(CST_VRNC_VL, null, null, 0, 0, AL_VD, companyCode);

            // map adjustment, unit of measure, item location
            invAdjustment.addInvAdjustmentLine(invAdjustmentLine);
            invItemLocation.getInvItem().getInvUnitOfMeasure().addInvAdjustmentLine(invAdjustmentLine);
            invItemLocation.addInvAdjustmentLine(invAdjustmentLine);

            return invAdjustmentLine;

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

    private void addInvDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, byte DR_RVRSL,
                               Integer COA_CODE, LocalInvAdjustment invAdjustment, Integer branchCode, Integer companyCode)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("InvStockTransferEntryApiControllerBean addInvDrEntry");

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
            invAdjustment.addInvDistributionRecord(invDistributionRecord);
            glChartOfAccount.addInvDistributionRecord(invDistributionRecord);

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

    private void executeInvAdjPost(Integer ADJ_CODE, String USR_NM, Integer branchCode, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException {

        Debug.print("InvStockTransferEntryApiControllerBean executeInvAdjPost");

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
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.TRUE, invAdjustment.getAdjCode(), companyCode);
            }

            Iterator i = invAdjustmentLines.iterator();
            while (i.hasNext()) {
                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();
                LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
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

            // create journal entry
            LocalGlJournal glJournal = glJournalHome.create(invAdjustment.getAdjReferenceNumber(),
                    invAdjustment.getAdjDescription(), invAdjustment.getAdjDate(), 0.0d, null,
                    invAdjustment.getAdjDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE,
                    USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM,
                    EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, EJBCommon.FALSE, null, branchCode,
                    companyCode);

            LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("INVENTORY", companyCode);
            glJournal.setGlJournalSource(glJournalSource);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
            glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("INVENTORY ADJUSTMENTS", companyCode);
            glJournal.setGlJournalCategory(glJournalCategory);

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
                Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(
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
                    LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(
                            adCompany.getCmpRetainedEarnings(), branchCode, companyCode);

                    for (Object subsequentSetOfBook : glSubsequentSetOfBooks) {
                        LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) subsequentSetOfBook;
                        String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();
                        // post to subsequent acvs of subsequent set of book(propagate)
                        Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(
                                glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                        for (Object accountingCalendarValue : glAccountingCalendarValues) {
                            LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;
                            if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY") || ACCOUNT_TYPE.equals("OWNERS EQUITY")) {
                                this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(),
                                        false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
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
            invAdjustment.setAdjPosted(EJBCommon.TRUE);

        }
        catch (GlJREffectiveDateNoPeriodExistException | GlobalRecordAlreadyDeletedException |
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
                                              double CST_ADJST_QTY, double CST_ADJST_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, Integer branchCode, Integer companyCode) {

        Debug.print("InvStockTransferEntryApiControllerBean postInvAdjustmentToInventory");

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
                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(
                        CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            }
            catch (FinderException ex) {
                CST_LN_NMBR = 1;
            }

            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d,
                    CST_ADJST_QTY, CST_ADJST_CST, 0d, 0d, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d,
                    CST_ADJST_QTY > 0 ? CST_ADJST_QTY : 0, branchCode, companyCode);
            invItemLocation.addInvCosting(invCosting);
            invCosting.setInvAdjustmentLine(invAdjustmentLine);

            invCosting.setCstQuantity(CST_ADJST_QTY);
            invCosting.setCstCost(CST_ADJST_CST);

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT,
                    invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);

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

    @Override
    public OfsApiResponse createStockTransfer(AdjustmentRequest adjustmentRequest) {

        Debug.print("InvStockTransferEntryApiControllerBean createStockTransfer");

        OfsApiResponse apiResponse = new OfsApiResponse();
        LocalAdCompany adCompany;
        LocalAdBranch adBranchFrom;
        LocalAdBranch adBranchTo;
        LocalAdUser adUser;
        Date adjustmentDate = EJBCommon.convertStringToSQLDate(adjustmentRequest.getAdjustmentDate(), ConfigurationClass.DEFAULT_DATE_FORMAT);

        String branchNameFrom;
        String branchNameTo = adjustmentRequest.getBranchCode();
        boolean isBranchFromCentral = false;

        try {

            InvStockTransferDetails details = new InvStockTransferDetails();

            // Company Code
            try {
                if (adjustmentRequest.getCompanyCode() == null || adjustmentRequest.getCompanyCode().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_041);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_041_MSG);
                    return apiResponse;
                }
                adCompany = adCompanyHome.findByCmpShortName(adjustmentRequest.getCompanyCode());
                details.setStAdCompany(adCompany.getCmpCode());

                // TODO: Find way to upgrade this logic to be used as common setup
                if (adjustmentRequest.getBranchCodeFrom() == null || adjustmentRequest.getBranchCodeFrom().equals("")) {
                    LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(adCompany.getCmpCode());
                    branchNameFrom = adPreference.getPrfInvCentralWarehouse() != null ? adPreference.getPrfInvCentralWarehouse() : "POM WAREHOUSE LOCATION";
                    isBranchFromCentral = true;
                } else {
                    branchNameFrom = adjustmentRequest.getBranchCodeFrom();
                }
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
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
                if (adUser != null) {
                    details.setStCreatedBy(adUser.getUsrName());
                    details.setStLastModifiedBy(adUser.getUsrName());
                }

            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_002);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_002_MSG);
                return apiResponse;
            }

            // Branch Codes
            try {
                if (branchNameTo == null || branchNameTo.equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return apiResponse;
                }

                try {
                    adBranchFrom = (isBranchFromCentral == true) ?
                            adBranchHome.findByBrName(branchNameFrom, adCompany.getCmpCode()) :
                            adBranchHome.findByBrBranchCode(branchNameFrom, adCompany.getCmpCode());

                }
                catch (FinderException ex) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_065);
                    apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_065_MSG, branchNameFrom));
                    return apiResponse;
                }

                adBranchTo = adBranchHome.findByBrBranchCode(branchNameTo, adCompany.getCmpCode());
                if (adBranchFrom != null) {
                    details.setStAdBranch(adBranchFrom.getBrCode());
                }
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_065);
                apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_065_MSG, branchNameTo));
                return apiResponse;
            }

            ArrayList lineItems = new ArrayList();
            int lineItem = 1;

            // ITEMS
            for (ItemRequest itemRequest : adjustmentRequest.getItems()) {

                InvModStockTransferLineDetails mdetails = new InvModStockTransferLineDetails();
                LocalInvItem invItem;
                LocalInvLocation invLocationTo;
                mdetails.setStlCode(null);
                mdetails.setStlLineNumber((short) lineItem);

                // Item Name
                try {
                    if (itemRequest.getItemName() == null || itemRequest.getItemName().equals("")) {
                        apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_045);
                        apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_045_MSG);
                        return apiResponse;
                    }
                    invItem = invItemHome.findByIiName(itemRequest.getItemName(), adCompany.getCmpCode());
                    mdetails.setStlIiName(invItem.getIiName());
                    mdetails.setStlIiDescription(invItem.getIiDescription());
                    mdetails.setStlUnitCost(invItem.getIiUnitCost());

                }
                catch (FinderException ex) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_010);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_010_MSG + lineItem);
                    return apiResponse;
                }
                mdetails.setStlLocationNameFrom(adBranchFrom.getBrName());
                mdetails.setStlLocationNameTo(adBranchTo.getBrName());
                mdetails.setStlQuantityDelivered(itemRequest.getItemQuantity());
                mdetails.setStlAmount(mdetails.getStlUnitCost() * itemRequest.getItemQuantity());
                mdetails.setStlUomName(invItem.getInvUnitOfMeasure().getUomName());
                mdetails.setStlMisc("Unknown"); // Validate this use case.
                lineItems.add(mdetails);
                lineItem++;
            }

            String systemGenRefNumber = adjustmentRequest.getReferenceNumber();
            String systemGenDesc = adjustmentRequest.getDescription();
            if (systemGenRefNumber == null || systemGenRefNumber.equals("")) {
                systemGenRefNumber = "ST-REF-TT";
            }

            if (systemGenDesc == null || systemGenDesc.equals("")) {
                systemGenDesc = "DESC-TT" + adjustmentDate.toString();
            }

            details.setStReferenceNumber(systemGenRefNumber);
            details.setStDescription(systemGenDesc);
            details.setStDate(adjustmentDate);
            details.setStDateCreated(new Date());

            int stockTransfer = this.saveStockTransfer(details, lineItems);
            LocalInvStockTransfer invStockTransfer = invStockTransferHome.findByPrimaryKey(stockTransfer);

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            apiResponse.setDocumentNumber(invStockTransfer.getStDocumentNumber());
            apiResponse.setStatus("Stock transfer successfully.");

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
        catch (GlobalRecordInvalidException ex) {

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_055);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_055_MSG);
            return apiResponse;

        }
        catch (Exception ex) {

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return apiResponse;

        }

        return apiResponse;
    }

}