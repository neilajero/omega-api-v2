/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class InvItemEntryControllerBean
 * @created May 24, 2004, 6:29 PM
 * @author Enrico C. Yap
 */
package com.ejb.txn.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ap.ILocalApSupplierHome;
import com.ejb.dao.ar.LocalArCustomerHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.entities.ar.LocalArCustomer;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.inv.*;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.exception.inv.InvAccumulatedDepreciationAccountNotFoundException;
import com.ejb.exception.inv.InvDepreciationAccountNotFoundException;
import com.ejb.exception.inv.InvFixedAssetAccountNotFoundException;
import com.ejb.exception.inv.InvFixedAssetInformationNotCompleteException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.inv.InvItemDetails;
import com.util.mod.inv.InvModItemDetails;

import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Stateless(name = "InvItemEntryControllerEJB")
public class InvItemEntryControllerBean extends EJBContextClass implements InvItemEntryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private LocalInvPriceLevelHome invPriceLevelHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private ILocalApSupplierHome apSupplierHome;

    public Integer saveInvIiEntry(InvItemDetails itemDetails, String unitOfMeasureName, String supplierCode,
                                  String customerCode, String retailUnitOfMeasureName, String defaultLocation,
                                  String fixedAssetAccount, String depreciationAccount,
                                  String accumulatedDepreciation, boolean trackMisc, Integer companyCode)
            throws GlobalRecordAlreadyExistException, GlobalRecordAlreadyAssignedException,
            InvFixedAssetAccountNotFoundException, InvDepreciationAccountNotFoundException,
            InvFixedAssetInformationNotCompleteException, InvAccumulatedDepreciationAccountNotFoundException {

        Debug.print("InvItemEntryControllerBean saveInvIiEntry");

        LocalInvItem invItem;
        LocalGlChartOfAccount glFixedAssetAccount = null;
        LocalGlChartOfAccount glDepreciationAccount = null;
        LocalGlChartOfAccount glAccumulatedDepreciationAccount = null;

        try {

            // validate if item already exists
            validateItemExists(itemDetails, companyCode);

            if (itemDetails.getIiFixedAsset() == 1) {
                glFixedAssetAccount = getFixedAssetAccount(fixedAssetAccount, companyCode);
                glDepreciationAccount = getDepreciationAccount(depreciationAccount, companyCode);
                glAccumulatedDepreciationAccount = getAccumulatedDepreciationAccount(accumulatedDepreciation, companyCode);
            }

            Integer retailUom = null;
            if (retailUnitOfMeasureName != null && !retailUnitOfMeasureName.equals("")) {
                LocalInvUnitOfMeasure invRetailUom = invUnitOfMeasureHome.findByUomName(retailUnitOfMeasureName, companyCode);
                retailUom = invRetailUom.getUomCode();
            }

            Integer defaultLocationCode = null;
            if (defaultLocation != null && !defaultLocation.equals("")) {
                LocalInvLocation invLocation = invLocationHome.findByLocName(defaultLocation, companyCode);
                defaultLocationCode = invLocation.getLocCode();
            }

            boolean isRecalculate = false;

            if (itemDetails.getIiCode() == null) {
                if (itemDetails.getIiFixedAsset() == 1 && itemDetails.getIiLifeSpan() == 0) {
                    throw new InvFixedAssetInformationNotCompleteException();
                }

                // create a new item
                invItem = createInvItem(itemDetails, companyCode, glFixedAssetAccount,
                        glDepreciationAccount, glAccumulatedDepreciationAccount,
                        retailUom, defaultLocationCode);

                // setup item price levels
                setupItemPriceLevels(companyCode, invItem);
                isRecalculate = true;

            } else {

                // update item
                invItem = invItemHome.findByPrimaryKey(itemDetails.getIiCode());

                if (itemDetails.getIiFixedAsset() == 1 && itemDetails.getIiLifeSpan() == 0) {
                    throw new InvFixedAssetInformationNotCompleteException();
                }

                // validate if already assigned
                if (!invItem.getInvUnitOfMeasure().getUomName().equals(unitOfMeasureName)) {
                    validateItemAlreadyAssigned(invItem);
                }

                if (!invItem.getInvUnitOfMeasure().getUomName().equals(unitOfMeasureName) ||
                        invItem.getInvUnitOfMeasureConversions().isEmpty()) {
                    isRecalculate = true;
                }

                double oldUnitCost = invItem.getIiUnitCost();
                updateItemDetails(itemDetails, trackMisc, invItem, glFixedAssetAccount,
                        glDepreciationAccount, glAccumulatedDepreciationAccount, retailUom, defaultLocationCode);
                setupBranchItemLocation(itemDetails, companyCode);
            }

            LocalInvUnitOfMeasure invUnitOfMeasure = getUnitOfMeasure(unitOfMeasureName, companyCode, invItem);
            setSupplier(supplierCode, companyCode, invItem);
            setCustomer(customerCode, companyCode, invItem);

            if (isRecalculate) {
                // remove all umc by item
                Collection unitOfMeasureConversions = invItem.getInvUnitOfMeasureConversions();
                Iterator i = unitOfMeasureConversions.iterator();
                while (i.hasNext()) {
                    LocalInvUnitOfMeasureConversion unitOfMeasureConversion = (LocalInvUnitOfMeasureConversion) i.next();
                    i.remove();
                    // unitOfMeasureConversion.entityRemove();
                    em.remove(unitOfMeasureConversion);
                }

                // add new umc
                Collection invUnitOfMeasures = invUnitOfMeasureHome.findByUomAdLvClass(
                        invUnitOfMeasure.getUomAdLvClass(), companyCode);
                i = invUnitOfMeasures.iterator();
                while (i.hasNext()) {
                    LocalInvUnitOfMeasure unitOfMeasure = (LocalInvUnitOfMeasure) i.next();
                    this.addInvUmcEntry(invItem, unitOfMeasure.getUomName(), unitOfMeasure.getUomAdLvClass(),
                            unitOfMeasure.getUomConversionFactor(), unitOfMeasure.getUomBaseUnit(), companyCode);
                }
            }
            return invItem.getIiCode();
        }
        catch (GlobalRecordAlreadyExistException | InvAccumulatedDepreciationAccountNotFoundException
               | InvFixedAssetInformationNotCompleteException | InvDepreciationAccountNotFoundException
               | InvFixedAssetAccountNotFoundException | GlobalRecordAlreadyAssignedException ex) {
            throw ex;
        }
        catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteInvIiEntry(Integer itemCode, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException {

        Debug.print("InvItemEntryControllerBean deleteInvIiEntry");

        try {
            LocalInvItem invItem;
            try {
                invItem = invItemHome.findByPrimaryKey(itemCode);
            }
            catch (FinderException ex) {
                throw new GlobalRecordAlreadyDeletedException();
            }

            // if already in used in stock transfers
            if (!invItem.getInvStockTransferLines().isEmpty()) {
                throw new GlobalRecordAlreadyAssignedException();
            }


            // if item location is already assigned
            if (!invItem.getInvItemLocations().isEmpty()) {
                Collection itemLocations = invItem.getInvItemLocations();
                Iterator locIter = itemLocations.iterator();
                while (locIter.hasNext()) {
                    LocalInvItemLocation itemLocation = (LocalInvItemLocation) locIter.next();
                    if (!itemLocation.getApVoucherLineItems().isEmpty()
                            || !itemLocation.getArInvoiceLineItems().isEmpty()
                            || !itemLocation.getInvAdjustmentLines().isEmpty()
                            || !itemLocation.getInvCostings().isEmpty()
                            || !itemLocation.getInvPhysicalInventoryLines().isEmpty()
                            || !itemLocation.getInvBranchStockTransferLines().isEmpty()
                            || !itemLocation.getApPurchaseOrderLines().isEmpty()
                            || !itemLocation.getApPurchaseRequisitionLines().isEmpty()
                            || !itemLocation.getAdBranchItemLocations().isEmpty()
                            || !itemLocation.getArSalesOrderLines().isEmpty()
                            || !itemLocation.getInvLineItems().isEmpty()) {
                        throw new GlobalRecordAlreadyAssignedException();
                    }

                    // branch item locations
                    Collection adBranchItemLocations = itemLocation.getAdBranchItemLocations();
                    for (Object branchItemLocation : adBranchItemLocations) {
                        LocalAdBranchItemLocation adBranchItemLocation = (LocalAdBranchItemLocation) branchItemLocation;
                        // if already downloaded to pos
                        if (adBranchItemLocation.getBilItemDownloadStatus() == 'D'
                                || adBranchItemLocation.getBilItemDownloadStatus() == 'X'
                                || adBranchItemLocation.getBilItemLocationDownloadStatus() == 'D'
                                || adBranchItemLocation.getBilItemLocationDownloadStatus() == 'X'
                                || adBranchItemLocation.getBilLocationDownloadStatus() == 'D'
                                || adBranchItemLocation.getBilLocationDownloadStatus() == 'X') {
                            throw new GlobalRecordAlreadyAssignedException();
                        }
                    }

                    // remove item location
                    locIter.remove();
                    // itemLocation.entityRemove();
                    em.remove(itemLocation);
                }
            }
            // invItem.entityRemove();
            em.remove(invItem);
        }
        catch (GlobalRecordAlreadyDeletedException | GlobalRecordAlreadyAssignedException ex) {
            getSessionContext().setRollbackOnly();
            throw ex;
        }
        catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    private void setupBranchItemLocation(InvItemDetails itemDetails, Integer companyCode) {

        LocalAdBranchItemLocation adBranchItemLocation;
        try {
            LocalInvItemLocation invItemLocation;
            Collection invItemLocations = invItemLocationHome.findByIiName(itemDetails.getIiName(), companyCode);
            for (Object itemLocation : invItemLocations) {
                invItemLocation = (LocalInvItemLocation) itemLocation;
                Collection adBranchItemLocations = adBranchItemLocationHome
                        .findByInvIlAll(invItemLocation.getIlCode(), companyCode);
                for (Object branchItemLocation : adBranchItemLocations) {
                    adBranchItemLocation = (LocalAdBranchItemLocation) branchItemLocation;
                    if (adBranchItemLocation.getBilItemDownloadStatus() == 'N') {
                        adBranchItemLocation.setBilItemDownloadStatus('N');
                    } else if (adBranchItemLocation.getBilItemDownloadStatus() == 'D') {
                        adBranchItemLocation.setBilItemDownloadStatus('X');
                    } else if (adBranchItemLocation.getBilItemDownloadStatus() == 'U') {
                        adBranchItemLocation.setBilItemDownloadStatus('U');
                    } else if (adBranchItemLocation.getBilItemDownloadStatus() == 'X') {
                        adBranchItemLocation.setBilItemDownloadStatus('X');
                    }
                }
            }
        }
        catch (FinderException ex) {

        }
    }

    private void updateItemDetails(InvItemDetails itemDetails, boolean trackMisc, LocalInvItem invItem,
                                   LocalGlChartOfAccount glFixedAssetAccount,
                                   LocalGlChartOfAccount glDepreciationAccount,
                                   LocalGlChartOfAccount glAccumulatedDepreciationAccount,
                                   Integer retailUom, Integer defaultLocation) {

        invItem.setIiName(itemDetails.getIiName());
        invItem.setIiDescription(itemDetails.getIiDescription());
        invItem.setIiPartNumber(itemDetails.getIiPartNumber());
        invItem.setIiShortName(itemDetails.getIiShortName());
        invItem.setIiBarCode1(itemDetails.getIiBarCode1());
        invItem.setIiBarCode2(itemDetails.getIiBarCode2());
        invItem.setIiBarCode3(itemDetails.getIiBarCode3());
        invItem.setIiBrand(itemDetails.getIiBrand());
        invItem.setIiClass(itemDetails.getIiClass());
        invItem.setIiAdLvCategory(itemDetails.getIiAdLvCategory());
        invItem.setIiCostMethod(itemDetails.getIiCostMethod());
        invItem.setIiUnitCost(itemDetails.getIiUnitCost());
        invItem.setIiSalesPrice(itemDetails.getIiSalesPrice());
        invItem.setIiEnable(itemDetails.getIiEnable());
        invItem.setIiVirtualStore(itemDetails.getIiVirtualStore());
        invItem.setIiDoneness(itemDetails.getIiDoneness());
        invItem.setIiSidings(itemDetails.getIiSidings());
        invItem.setIiRemarks(itemDetails.getIiRemarks());
        invItem.setIiServiceCharge(itemDetails.getIiServiceCharge());
        invItem.setIiNonInventoriable(itemDetails.getIiNonInventoriable());
        invItem.setIiServices(itemDetails.getIiServices());
        invItem.setIiInterestException(itemDetails.getIiInterestException());
        invItem.setIiPaymentTermException(itemDetails.getIiPaymentTermException());
        invItem.setIiJobServices(itemDetails.getIiJobServices());
        invItem.setIiIsVatRelief(itemDetails.getIiIsVatRelief());
        invItem.setIiIsTax(itemDetails.getIiIsTax());
        invItem.setIiIsProject(itemDetails.getIiIsProject());
        invItem.setIiPercentMarkup(itemDetails.getIiPercentMarkup());
        invItem.setIiShippingCost(itemDetails.getIiShippingCost());
        invItem.setIiSpecificGravity(itemDetails.getIiSpecificGravity());
        invItem.setIiStandardFillSize(itemDetails.getIiStandardFillSize());
        invItem.setIiYield(itemDetails.getIiYield());
        invItem.setIiLossPercentage(itemDetails.getIiLossPercentage());
        invItem.setIiMarket(itemDetails.getIiMarket());
        invItem.setIiEnablePo(itemDetails.getIiEnablePo());
        invItem.setIiPoCycle(itemDetails.getIiPoCycle());
        invItem.setIiUmcPackaging(itemDetails.getIiUmcPackaging());
        invItem.setIiRetailUom(retailUom);
        invItem.setIiOpenProduct(itemDetails.getIiOpenProduct());
        invItem.setIiFixedAsset(itemDetails.getIiFixedAsset());
        invItem.setIiDateAcquired(itemDetails.getIiDateAcquired());
        invItem.setIiDefaultLocation(defaultLocation);
        invItem.setIiAcquisitionCost(itemDetails.getIiAcquisitionCost());
        invItem.setIiLifeSpan(itemDetails.getIiLifeSpan());
        invItem.setIiResidualValue(itemDetails.getIiResidualValue());
        invItem.setGlCoaFixedAssetAccount(glFixedAssetAccount != null
                ? glFixedAssetAccount.getCoaCode().toString() : null);
        invItem.setGlCoaDepreciationAccount(glDepreciationAccount != null
                ? glDepreciationAccount.getCoaCode().toString() : null);
        invItem.setGlCoaAccumulatedDepreciationAccount(glAccumulatedDepreciationAccount != null
                ? glAccumulatedDepreciationAccount.getCoaCode().toString() : null);
        invItem.setIiCondition(itemDetails.getIiCondition());
        invItem.setIiTaxCode(itemDetails.getIiTaxCode());
        invItem.setIiTraceMisc(trackMisc == true ? (byte) 1 : (byte) 0);

        invItem.setIiScSunday(itemDetails.getIiScSunday());
        invItem.setIiScMonday(itemDetails.getIiScMonday());
        invItem.setIiScTuesday(itemDetails.getIiScTuesday());
        invItem.setIiScWednesday(itemDetails.getIiScWednesday());
        invItem.setIiScThursday(itemDetails.getIiScThursday());
        invItem.setIiScFriday(itemDetails.getIiScFriday());
        invItem.setIiScSaturday(itemDetails.getIiScSaturday());

        invItem.setIiLastModifiedBy(itemDetails.getIiLastModifiedBy());
        invItem.setIiDateLastModified(itemDetails.getIiDateLastModified());
    }

    private void validateItemAlreadyAssigned(LocalInvItem invItem)
            throws GlobalRecordAlreadyAssignedException {
        // if item location is already assigned
        if (!invItem.getInvItemLocations().isEmpty()) {
            Collection invItemLocations = invItem.getInvItemLocations();
            for (Object itemLocation : invItemLocations) {
                LocalInvItemLocation invItemLocation = (LocalInvItemLocation) itemLocation;
                if (!invItemLocation.getApVoucherLineItems().isEmpty()
                        || !invItemLocation.getArInvoiceLineItems().isEmpty()
                        || !invItemLocation.getInvAdjustmentLines().isEmpty()
                        || !invItemLocation.getInvCostings().isEmpty()
                        || !invItemLocation.getInvPhysicalInventoryLines().isEmpty()
                        || !invItemLocation.getInvBranchStockTransferLines().isEmpty()
                        || !invItem.getInvStockTransferLines().isEmpty()) {
                    throw new GlobalRecordAlreadyAssignedException();
                }
            }
        }
    }

    private LocalInvItem createInvItem(InvItemDetails itemDetails, Integer companyCode,
                                       LocalGlChartOfAccount glFixedAssetAccount,
                                       LocalGlChartOfAccount glDepreciationAccount,
                                       LocalGlChartOfAccount glAccumulatedDepreciationAccount,
                                       Integer retailUom, Integer defaultLocation)
            throws CreateException {

        LocalInvItem invItem;
        invItem = invItemHome
                .IiName(itemDetails.getIiName())
                .IiDescription(itemDetails.getIiDescription())
                .IiPartNumber(itemDetails.getIiPartNumber())
                .IiShortName(itemDetails.getIiShortName())
                .IiBarCode1(itemDetails.getIiBarCode1())
                .IiBarCode2(itemDetails.getIiBarCode2())
                .IiBarCode3(itemDetails.getIiBarCode3())
                .IiBrand(itemDetails.getIiBrand())
                .IiClass(itemDetails.getIiClass())
                .IiAdLvCategory(itemDetails.getIiAdLvCategory())
                .IiCostMethod(itemDetails.getIiCostMethod())
                .IiUnitCost(itemDetails.getIiUnitCost())
                .IiSalesPrice(itemDetails.getIiSalesPrice())
                .IiEnable(EJBCommon.TRUE)
                .IiVirtualStore(itemDetails.getIiVirtualStore())
                .IiDoneness(itemDetails.getIiDoneness())
                .IiSidings(itemDetails.getIiSidings())
                .IiRemarks(itemDetails.getIiRemarks())
                .IiServiceCharge(itemDetails.getIiServiceCharge())
                .IiNonInventoriable(itemDetails.getIiNonInventoriable())
                .IiServices(itemDetails.getIiServices())
                .IiJobServices(itemDetails.getIiJobServices())
                .IiIsVatRelief(itemDetails.getIiIsVatRelief())
                .IiIsTax(itemDetails.getIiIsTax())
                .IiIsProject(itemDetails.getIiIsProject())
                .IiPercentMarkup(itemDetails.getIiPercentMarkup())
                .IiShippingCost(itemDetails.getIiShippingCost())
                .IiStandardFillSize(itemDetails.getIiStandardFillSize())
                .IiSpecificGravity(itemDetails.getIiSpecificGravity())
                .IiYeild(itemDetails.getIiYield())
                .IiLossPercentage(itemDetails.getIiLossPercentage())
                .IiMarkupValue(itemDetails.getIiMarkupValue())
                .IiMarket(itemDetails.getIiMarket())
                .IiEnablePo(itemDetails.getIiEnablePo())
                .IiPoCycle(itemDetails.getIiPoCycle())
                .IiUmcPackaging(itemDetails.getIiUmcPackaging())
                .IiRetailUom(retailUom)
                .IiOpenProduct(itemDetails.getIiOpenProduct())
                .IiFixedAsset(itemDetails.getIiFixedAsset())
                .IiDateAcquired(itemDetails.getIiDateAcquired())
                .IiDefaultLocation(defaultLocation)
                .IiTraceMisc(itemDetails.getIiTraceMisc())

                .IiScSunday(itemDetails.getIiScSunday())
                .IiScMonday(itemDetails.getIiScMonday())
                .IiScTuesday(itemDetails.getIiScTuesday())
                .IiScWednesday(itemDetails.getIiScWednesday())
                .IiScThursday(itemDetails.getIiScThursday())
                .IiScFriday(itemDetails.getIiScFriday())
                .IiScSaturday(itemDetails.getIiScSaturday())

                .IiAcquisitionCost(itemDetails.getIiAcquisitionCost())
                .IiLifeSpan(itemDetails.getIiLifeSpan())
                .IiResidualValue(itemDetails.getIiResidualValue())

                .IiCoaFixedAccount(glFixedAssetAccount != null
                        ? glFixedAssetAccount.getCoaCode().toString() : null)
                .IiCoaDepreciationAccount(glDepreciationAccount != null
                        ? glDepreciationAccount.getCoaCode().toString() : null)
                .IiCoaAccumDepreciationAccount(glAccumulatedDepreciationAccount != null
                        ? glAccumulatedDepreciationAccount.getCoaCode().toString()
                        : null)

                .IiCondition(itemDetails.getIiCondition())
                .IiTaxCode(itemDetails.getIiTaxCode())

                .IiCreatedBy(itemDetails.getIiCreatedBy())
                .IiDateCreated(itemDetails.getIiDateCreated())
                .IiLastModifiedBy(itemDetails.getIiLastModifiedBy())
                .IiDateLastModified(itemDetails.getIiDateLastModified())

                .IiInterestException(itemDetails.getIiInterestException())
                .IiPaymentTermException(itemDetails.getIiPaymentTermException())

                .IiAdCompany(companyCode)
                .buildItem();
        return invItem;
    }

    private LocalInvUnitOfMeasure getUnitOfMeasure(String unitOfMeasureName, Integer companyCode, LocalInvItem invItem)
            throws FinderException {

        LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(unitOfMeasureName, companyCode);
        invItem.setInvUnitOfMeasure(invUnitOfMeasure);
        return invUnitOfMeasure;
    }

    private void setCustomer(String customerCode, Integer companyCode, LocalInvItem invItem) {

        try {
            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(customerCode, companyCode);
            invItem.setArCustomer(arCustomer);
        }
        catch (FinderException ex) {
            if (invItem.getArCustomer() != null) {
                invItem.getArCustomer().dropInvItem(invItem);
            }
        }
    }

    private void setSupplier(String supplierCode, Integer companyCode, LocalInvItem invItem) {

        try {
            LocalApSupplier apSupplier = apSupplierHome.findBySplSupplierCode(supplierCode, companyCode);
            invItem.setApSupplier(apSupplier);
        }
        catch (FinderException ex) {
            if (invItem.getApSupplier() != null) {
                invItem.getApSupplier().dropInvItem(invItem);
            }
        }
    }

    private void setupItemPriceLevels(Integer companyCode, LocalInvItem invItem)
            throws FinderException, CreateException {

        Collection adLookUpValues = adLookUpValueHome.findByLuName("INV PRICE LEVEL", companyCode);

        for (Object lookUpValue : adLookUpValues) {
            LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;

            LocalInvPriceLevel invPriceLevel = invPriceLevelHome
                    .PlAmount(invItem.getIiSalesPrice())
                    .PlPercentMarkup(invItem.getIiPercentMarkup())
                    .PlShippingCost(invItem.getIiShippingCost())
                    .PlAdLvPriceLevel(adLookUpValue.getLvName())
                    .PlAdCompany(companyCode)
                    .buildPriceLevel();

            invPriceLevel.setInvItem(invItem);
            // invItem.addInvPriceLevel(invPriceLevel);
        }
    }

    private LocalGlChartOfAccount getAccumulatedDepreciationAccount(String AccumulatedDepreciation, Integer companyCode)
            throws InvAccumulatedDepreciationAccountNotFoundException {

        LocalGlChartOfAccount glAccumulatedDepreciationAccount;
        try {
            glAccumulatedDepreciationAccount =
                    glChartOfAccountHome.findByCoaAccountNumber(AccumulatedDepreciation, companyCode);
        }
        catch (FinderException ex) {
            throw new InvAccumulatedDepreciationAccountNotFoundException();
        }
        return glAccumulatedDepreciationAccount;
    }

    private LocalGlChartOfAccount getDepreciationAccount(String DepreciationAccount, Integer companyCode)
            throws InvDepreciationAccountNotFoundException {

        LocalGlChartOfAccount glDepreciationAccount;
        try {
            glDepreciationAccount =
                    glChartOfAccountHome.findByCoaAccountNumber(DepreciationAccount, companyCode);
        }
        catch (FinderException ex) {
            throw new InvDepreciationAccountNotFoundException();
        }
        return glDepreciationAccount;
    }

    private LocalGlChartOfAccount getFixedAssetAccount(String FixedAssetAccount, Integer companyCode)
            throws InvFixedAssetAccountNotFoundException {

        LocalGlChartOfAccount glFixedAssetAccount;
        try {
            glFixedAssetAccount =
                    glChartOfAccountHome.findByCoaAccountNumber(FixedAssetAccount, companyCode);
        }
        catch (FinderException ex) {
            throw new InvFixedAssetAccountNotFoundException();
        }
        return glFixedAssetAccount;
    }

    private void validateItemExists(InvItemDetails details, Integer companyCode)
            throws GlobalRecordAlreadyExistException {

        LocalInvItem invItem;
        try {
            invItem = invItemHome.findByIiName(details.getIiName(), companyCode);
            if (details.getIiCode() == null || details.getIiCode() != null
                    && !invItem.getIiCode().equals(details.getIiCode())) {
                throw new GlobalRecordAlreadyExistException();
            }
        }
        catch (GlobalRecordAlreadyExistException ex) {
            throw ex;
        }
        catch (FinderException ex) {
        }
    }

    public InvModItemDetails getInvIiByIiCode(Integer itemCode, Integer branchCode, Integer companyCode)
            throws GlobalNoRecordFoundException {

        Debug.print("InvItemEntryControllerBean getInvIiByIiCode");

        LocalInvItem invItem = null;
        LocalGlChartOfAccount glChartOfAccount = null;

        try {
            try {
                invItem = invItemHome.findByPrimaryKey(itemCode);
            }
            catch (FinderException ex) {
                throw new GlobalNoRecordFoundException();
            }

            InvModItemDetails iiDetails = new InvModItemDetails();
            if (invItem.getIiFixedAsset() == 1) {
                if (invItem.getGlCoaFixedAssetAccount() != null) {
                    try {
                        glChartOfAccount
                                = glChartOfAccountHome.findByPrimaryKey(
                                Integer.parseInt(invItem.getGlCoaFixedAssetAccount()));
                    }
                    catch (FinderException ex) {
                        throw new GlobalNoRecordFoundException();
                    }
                    iiDetails.setFixedAssetAccount(glChartOfAccount.getCoaAccountNumber());
                    iiDetails.setFixedAssetAccountDescription(glChartOfAccount.getCoaAccountDescription());
                }

                if (invItem.getGlCoaDepreciationAccount() != null) {
                    try {
                        glChartOfAccount
                                = glChartOfAccountHome.findByPrimaryKey(
                                Integer.parseInt(invItem.getGlCoaDepreciationAccount()));
                    }
                    catch (FinderException ex) {
                        throw new GlobalNoRecordFoundException();
                    }
                    iiDetails.setDepreciationAccount(glChartOfAccount.getCoaAccountNumber());
                    iiDetails.setDepreciationAccountDescription(glChartOfAccount.getCoaAccountDescription());
                }

                if (invItem.getGlCoaAccumulatedDepreciationAccount() != null) {
                    try {
                        glChartOfAccount
                                = glChartOfAccountHome.findByPrimaryKey(
                                Integer.parseInt(invItem.getGlCoaAccumulatedDepreciationAccount()));
                    }
                    catch (FinderException ex) {
                        throw new GlobalNoRecordFoundException();
                    }

                    iiDetails.setAccumulatedDepreciationAccount(glChartOfAccount.getCoaAccountNumber());
                    iiDetails.setAccumulatedDepreciationAccountDescription(glChartOfAccount.getCoaAccountDescription());
                }
            }

            iiDetails.setIiCode(invItem.getIiCode());
            iiDetails.setIiName(invItem.getIiName());
            iiDetails.setIiDescription(invItem.getIiDescription());
            iiDetails.setIiPartNumber(invItem.getIiPartNumber());
            iiDetails.setIiShortName(invItem.getIiShortName());
            iiDetails.setIiBarCode1(invItem.getIiBarCode1());
            iiDetails.setIiBarCode2(invItem.getIiBarCode2());
            iiDetails.setIiBarCode3(invItem.getIiBarCode3());
            iiDetails.setIiBrand(invItem.getIiBrand());
            iiDetails.setIiClass(invItem.getIiClass());
            iiDetails.setIiAdLvCategory(invItem.getIiAdLvCategory());
            iiDetails.setIiCostMethod(invItem.getIiCostMethod());
            iiDetails.setIiUnitCost(invItem.getIiUnitCost());
            iiDetails.setIiAveCost(this.getIiAveCost(invItem, new Date(), branchCode, companyCode));
            iiDetails.setIiPercentMarkup(invItem.getIiPercentMarkup());
            iiDetails.setIiShippingCost(invItem.getIiShippingCost());
            iiDetails.setIiSalesPrice(invItem.getIiSalesPrice());
            iiDetails.setIiEnable(invItem.getIiEnable());
            iiDetails.setIiUomName(invItem.getInvUnitOfMeasure() != null
                    ? invItem.getInvUnitOfMeasure().getUomName() : null);
            iiDetails.setIiDoneness(invItem.getIiDoneness());
            iiDetails.setIiSidings(invItem.getIiSidings());
            iiDetails.setIiRemarks(invItem.getIiRemarks());
            iiDetails.setIiServiceCharge(invItem.getIiServiceCharge());
            iiDetails.setIiNonInventoriable(invItem.getIiNonInventoriable());
            iiDetails.setIiServices(invItem.getIiServices());
            iiDetails.setIiInterestException(invItem.getIiInterestException());
            iiDetails.setIiPaymentTermException(invItem.getIiPaymentTermException());
            iiDetails.setIiJobServices(invItem.getIiJobServices());
            iiDetails.setIiIsVatRelief(invItem.getIiIsVatRelief());
            iiDetails.setIiIsTax(invItem.getIiIsTax());
            iiDetails.setIiIsProject(invItem.getIiIsProject());
            iiDetails.setIiSplSpplrCode(invItem.getApSupplier() != null
                    ? invItem.getApSupplier().getSplSupplierCode() : null);
            iiDetails.setIiCstCustomerCode(invItem.getArCustomer() != null
                    ? invItem.getArCustomer().getCstCustomerCode() : null);

            iiDetails.setIiMarket(invItem.getIiMarket());
            iiDetails.setIiEnablePo(invItem.getIiEnablePo());
            iiDetails.setIiPoCycle(invItem.getIiPoCycle());
            iiDetails.setIiUmcPackaging(invItem.getIiUmcPackaging());
            iiDetails.setIiOpenProduct(invItem.getIiOpenProduct());

            iiDetails.setIiFixedAsset(invItem.getIiFixedAsset());
            iiDetails.setIiDateAcquired(invItem.getIiDateAcquired());
            iiDetails.setIiTraceMisc(invItem.getIiTraceMisc());

            iiDetails.setIiScSunday(invItem.getIiScSunday());
            iiDetails.setIiScMonday(invItem.getIiScMonday());
            iiDetails.setIiScTuesday(invItem.getIiScTuesday());
            iiDetails.setIiScWednesday(invItem.getIiScWednesday());
            iiDetails.setIiScThursday(invItem.getIiScThursday());
            iiDetails.setIiScFriday(invItem.getIiScFriday());
            iiDetails.setIiScSaturday(invItem.getIiScSaturday());

            iiDetails.setAcquisitionCost(invItem.getIiAcquisitionCost());
            iiDetails.setLifeSpan(invItem.getIiLifeSpan());
            iiDetails.setResidualValue(invItem.getIiResidualValue());
            iiDetails.setIiCondition(invItem.getIiCondition());
            iiDetails.setIiTaxCode(invItem.getIiTaxCode());

            iiDetails.setIiDateCreated(invItem.getIiDateCreated());
            iiDetails.setIiCreatedBy(invItem.getIiCreatedBy());
            iiDetails.setIiDateLastModified(invItem.getIiDateLastModified());
            iiDetails.setIiLastModifiedBy(invItem.getIiLastModifiedBy());

            String retailUom = null;
            if (invItem.getIiRetailUom() != null) {
                LocalInvUnitOfMeasure invRetailUom = invUnitOfMeasureHome.findByPrimaryKey(invItem.getIiRetailUom());
                retailUom = invRetailUom.getUomName();
            }
            iiDetails.setIiRetailUomName(retailUom);

            String defaultLocation = null;
            if (invItem.getIiDefaultLocation() != null) {
                LocalInvLocation invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                defaultLocation = invLocation.getLocName();
            }
            iiDetails.setIiDefaultLocationName(defaultLocation);

            return iiDetails;

        }
        catch (GlobalNoRecordFoundException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public double getIiUnitCostByIiName(String itemName, Integer companyCode) {

        Debug.print("InvItemEntryControllerBean getIiUnitCostByIiName");

        try {
            LocalInvItem invItem = invItemHome.findByIiName(itemName, companyCode);
            return invItem.getIiUnitCost();
        }
        catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer companyCode) {

        Debug.print("InvItemEntryControllerBean getGlFcPrecisionUnit");

        try {
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            return adCompany.getGlFunctionalCurrency().getFcPrecision();
        }
        catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    private double getIiAveCost(LocalInvItem invItem, Date currentDate, Integer branchCode, Integer companyCode) {

        Debug.print("InvItemEntryControllerBean getIiAveCost");

        double COST = 0d;
        LocalInvCosting invCosting;
        LocalAdBranch adBranch;
        LocalAdPreference adPreference;

        try {

            adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            adPreference.getPrfInvCentralWarehouse();

            // TODO: Need to make sure central warehouse has value to avoid this hard-coded
            // fallback
            String centralWareHouse = !adPreference.getPrfInvCentralWarehouse().equals("")
                    && adPreference.getPrfInvCentralWarehouse() != null
                    ? adPreference.getPrfInvCentralWarehouse() : "HO";
            String itemName = invItem.getIiName();

            // TODO: This is a temporary setup to overwrite the selected branch
            adBranch = adBranchHome.findByBrName(centralWareHouse, companyCode);
            if (adBranch != null) {
                invCosting = invCostingHome
                        .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(currentDate,
                                itemName, centralWareHouse, adBranch.getBrCode(), companyCode);
                if (invCosting != null) {
                    COST = invCosting.getCstRemainingQuantity() == 0 ? COST
                            : Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                }
            }
        }
        catch (FinderException ex) {
            // TODO: This exception will return zero cost if an item is not yet setup in
            // ITEM LOCATION ENTRY
        }
        catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        return COST;
    }

    private void addInvUmcEntry(LocalInvItem invItem, String uomName, String uomAdLvClass, double conversionFactor,
                                byte umcBaseUnit, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("InvUnitOfMeasureConversionControllerBean addInvUmcEntry");

        try {

            // create umc
            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome
                    .create(conversionFactor, umcBaseUnit, companyCode);
            try {
                // map uom
                LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomNameAndUomAdLvClass(uomName,
                        uomAdLvClass, companyCode);
                invUnitOfMeasureConversion.setInvUnitOfMeasure(invUnitOfMeasure);
                // invUnitOfMeasure.addInvUnitOfMeasureConversion(invUnitOfMeasureConversion);
            }
            catch (FinderException ex) {
                throw new GlobalNoRecordFoundException();
            }
            // map item
            invUnitOfMeasureConversion.setInvItem(invItem);
            // invItem.addInvUnitOfMeasureConversion(invUnitOfMeasureConversion);

        }
        catch (GlobalNoRecordFoundException ex) {
            getSessionContext().setRollbackOnly();
            throw ex;
        }
        catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getInvIiAll(Integer companyCode) {

        Debug.print("InvItemEntryControllerBean getInvIiAll");
        LocalInvItem invItem;
        ArrayList list = new ArrayList();
        try {
            Collection invItems = invItemHome.findEnabledIiAll(companyCode);
            for (Object item : invItems) {
                invItem = (LocalInvItem) item;
                list.add(invItem.getIiName());
            }
            return list;
        }
        catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getInvUomAllByUnitMeasureClass(String unitOfMeasureName, Integer companyCode) {

        Debug.print("InvItemEntryControllerBean getInvUomAllByUnitMeasureClass");

        ArrayList list = new ArrayList();

        try {

            LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(unitOfMeasureName, companyCode);
            Collection invUnitOfMeasures = invUnitOfMeasureHome.findByUomAdLvClass(invUnitOfMeasure.getUomAdLvClass(),
                    companyCode);
            for (Object ofMeasure : invUnitOfMeasures) {
                LocalInvUnitOfMeasure unitOfMeasure = (LocalInvUnitOfMeasure) ofMeasure;
                list.add(unitOfMeasure.getUomName());
            }
            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void ejbCreate() throws CreateException {

        Debug.print("InvItemEntryControllerBean ejbCreate");
    }

}