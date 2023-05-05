package com.ejb.txnapi.inv;

import java.util.*;

import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.dao.ad.*;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.inv.*;
import com.ejb.exception.global.*;
import com.ejb.exception.inv.*;
import com.ejb.restfulapi.*;
import com.ejb.restfulapi.inv.models.*;
import com.util.*;
import com.util.inv.InvItemDetails;
import com.util.mod.ad.AdModBranchItemLocationDetails;
import com.util.mod.inv.InvModItemLocationDetails;

@Stateless(name = "InvItemEntryApiControllerEJB")
public class InvItemEntryApiControllerBean extends EJBContextClass implements InvItemEntryApiController {

    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private LocalInvPriceLevelHome invPriceLevelHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private InvItemLocationEntryApiController invItemLocationEntryApiController;
    @EJB
    private LocalAdUserResponsibilityHome adUserResponsibilityHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalAdBranchHome adBranchHome;

    @Override
    public Integer saveInvIiEntry(InvItemDetails itemDetails, String UOM_NM, String SPL_SPPLR_CD, String CST_CSTMR_CD,
                                  Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalRecordAlreadyAssignedException,
            InvFixedAssetAccountNotFoundException, InvDepreciationAccountNotFoundException,
            InvFixedAssetInformationNotCompleteException, InvAccumulatedDepreciationAccountNotFoundException {

        Debug.print("InvItemEntryApiControllerBean saveInvIiEntry");

        LocalInvItem invItem;

        try {

            // validate if item already exists
            try {
                invItem = invItemHome.findByIiName(itemDetails.getIiName(), AD_CMPNY);
                if (itemDetails.getIiCode() == null || itemDetails.getIiCode() != null && !invItem.getIiCode().equals(itemDetails.getIiCode())) {
                    throw new GlobalRecordAlreadyExistException();
                }
            } catch (GlobalRecordAlreadyExistException ex) {
                throw ex;
            } catch (FinderException ex) {
            }

            // create new item
            invItem = invItemHome
                    .IiName(itemDetails.getIiName())
                    .IiDescription(itemDetails.getIiDescription())
                    .IiClass(itemDetails.getIiClass())
                    .IiAdLvCategory(itemDetails.getIiAdLvCategory())
                    .IiCostMethod(itemDetails.getIiCostMethod())
                    .IiUnitCost(itemDetails.getIiUnitCost())
                    .IiSalesPrice(itemDetails.getIiSalesPrice())
                    .IiPercentMarkup(itemDetails.getIiPercentMarkup())
                    .IiEnable(EJBCommon.TRUE)
                    .IiTaxCode(itemDetails.getIiTaxCode())
                    .IiDefaultLocation(itemDetails.getIiDefaultLocation())
                    .IiCreatedBy(itemDetails.getIiCreatedBy())
                    .IiDateCreated(itemDetails.getIiDateCreated())
                    .IiLastModifiedBy(itemDetails.getIiLastModifiedBy())
                    .IiDateLastModified(itemDetails.getIiDateLastModified())
                    .IiAdCompany(AD_CMPNY)
                    .buildItem();

            invItem.setIiInterestException(itemDetails.getIiInterestException());
            invItem.setIiPaymentTermException(itemDetails.getIiPaymentTermException());

            // Price Levels
            // TODO: Determine if Bmobile will provide price level markup for each look-up
            // values (e.g. Wholesale, Distributor, Cash)
            // These price levels are dynamically added in Omega ERP - System Admin
            Collection adLookUpValues = adLookUpValueHome.findByLuName("INV PRICE LEVEL", AD_CMPNY);

            for (Object lookUpValue : adLookUpValues) {
                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;
                LocalInvPriceLevel invPriceLevel = invPriceLevelHome.create(invItem.getIiSalesPrice(), 0d,
                        invItem.getIiPercentMarkup(), invItem.getIiShippingCost(), adLookUpValue.getLvName(), 'N',
                        AD_CMPNY);
                invPriceLevel.setInvItem(invItem);
            }

            LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(UOM_NM, AD_CMPNY);
            invItem.setInvUnitOfMeasure(invUnitOfMeasure);

            // Unit of Measure Conversion
            // TODO: Determine the purpose of this additional setup in Item Entry
            Collection invUnitOfMeasures = invUnitOfMeasureHome.findByUomAdLvClass(invUnitOfMeasure.getUomAdLvClass(), AD_CMPNY);

            for (Object ofMeasure : invUnitOfMeasures) {
                LocalInvUnitOfMeasure unitOfMeasure = (LocalInvUnitOfMeasure) ofMeasure;
                this.addInvUmcEntry(invItem, unitOfMeasure.getUomName(), unitOfMeasure.getUomAdLvClass(),
                        unitOfMeasure.getUomConversionFactor(), unitOfMeasure.getUomBaseUnit(), AD_CMPNY);
            }

            return invItem.getIiCode();

        } catch (GlobalRecordAlreadyExistException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void addInvUmcEntry(LocalInvItem invItem, String uomName, String uomAdLvClass, double conversionFactor,
                                byte umcBaseUnit, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("InvItemEntryApiControllerBean addInvUmcEntry");

        try {

            // create umc
            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome
                    .create(conversionFactor, umcBaseUnit, AD_CMPNY);

            try {

                // map uom
                LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomNameAndUomAdLvClass(uomName, uomAdLvClass, AD_CMPNY);
                invUnitOfMeasureConversion.setInvUnitOfMeasure(invUnitOfMeasure);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }
            invUnitOfMeasureConversion.setInvItem(invItem);

        } catch (GlobalNoRecordFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private ArrayList getAdLvInvItemCategoryAll(Integer AD_CMPNY) {

        Debug.print("InvItemEntryApiControllerBean getAdLvInvItemCategoryAll");

        ArrayList list = new ArrayList();

        try {
            Collection adLookUpValues = adLookUpValueHome.findByLuName("INV ITEM CATEGORY", AD_CMPNY);
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

    private OfsApiResponse saveIiInvItemLocation(ItemRequest itemRequest, Integer RS_CODE, String USR_NM,
                                                 Integer AD_CMPNY) throws Exception {
        Debug.print("InvItemEntryApiControllerBean saveIiInvItemLocation");

        OfsApiResponse apiResponse = new OfsApiResponse();

        try {

            String itemClass = "";
            double reorderPoint = 0;
            double reorderQuantity = 0;
            double reorderLevel = 0;
            byte subjectToCommission = EJBCommon.FALSE;

            String salesAccount = null;
            String salesReturnAccount = null;
            String inventoryAccount = null;
            String costOfSalesAccount = null;

            // TODO: Find way to upgrade this logic to be used as common setup
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            String headOffice = !adPreference.getPrfInvCentralWarehouse().equals("") && adPreference.getPrfInvCentralWarehouse() != null ?
                    adPreference.getPrfInvCentralWarehouse() : "POM WAREHOUSE LOCATION";

            List<LocalGlChartOfAccount> defaultCoa = new ArrayList(
                    glChartOfAccountHome.findHoCoaAllByCoaCategory(itemRequest.getItemCategory(), headOffice, AD_CMPNY));

            LocalGlChartOfAccount accruedExpenseCoa = glChartOfAccountHome.findHoCoaByCoaCategory("ACCRUED EXPENSES", headOffice, AD_CMPNY);
            String accruedInvAccount = accruedExpenseCoa.getCoaAccountNumber();

            LocalGlChartOfAccount wipCoa = glChartOfAccountHome.findHoCoaByCoaCategory("STOCK IN-TRANSIT", headOffice, AD_CMPNY);
            String wipAccount = wipCoa.getCoaAccountNumber();

            if (itemRequest.getItemCategory().equalsIgnoreCase("RECHARGE CARDS 3G")) {
                LocalGlChartOfAccount salesCoa = glChartOfAccountHome.findHoCoaByCoaCategory("3G DEFERRED REVENUE", headOffice, AD_CMPNY);
                salesAccount = salesCoa.getCoaAccountNumber();
                salesReturnAccount = salesCoa.getCoaAccountNumber();
            }

            if (itemRequest.getItemCategory().equalsIgnoreCase("RECHARGE CARDS 4G")) {
                LocalGlChartOfAccount salesCoa = glChartOfAccountHome.findHoCoaByCoaCategory("4G DEFERRED REVENUE", headOffice, AD_CMPNY);
                salesAccount = salesCoa.getCoaAccountNumber();
                salesReturnAccount = salesCoa.getCoaAccountNumber();
            }

            for (LocalGlChartOfAccount chartOfAccount : defaultCoa) {
                if (!itemRequest.getItemCategory().equalsIgnoreCase("RECHARGE CARDS 3G") &&
                        !itemRequest.getItemCategory().equalsIgnoreCase("RECHARGE CARDS 4G")) {
                    if (chartOfAccount.getCoaAccountType().equals("REVENUE")) {
                        salesAccount = chartOfAccount.getCoaAccountNumber();
                        salesReturnAccount = chartOfAccount.getCoaAccountNumber();
                    }
                }

                if (chartOfAccount.getCoaAccountType().equals("EXPENSE")) {
                    costOfSalesAccount = chartOfAccount.getCoaAccountNumber();
                }

                if (chartOfAccount.getCoaAccountType().equals("ASSET")) {
                    inventoryAccount = chartOfAccount.getCoaAccountNumber();
                }
            }

            InvModItemLocationDetails mdetails = new InvModItemLocationDetails();
            mdetails.setIlReorderPoint(reorderPoint);
            mdetails.setIlReorderQuantity(reorderQuantity);
            mdetails.setIlReorderLevel(reorderLevel);
            mdetails.setIlCoaGlSalesAccountNumber(salesAccount);
            mdetails.setIlCoaGlInventoryAccountNumber(inventoryAccount);
            mdetails.setIlCoaGlCostOfSalesAccountNumber(costOfSalesAccount);
            mdetails.setIlCoaGlWipAccountNumber(wipAccount);
            mdetails.setIlCoaGlAccruedInventoryAccountNumber(accruedInvAccount);
            mdetails.setIlCoaGlSalesReturnAccountNumber(salesReturnAccount);
            mdetails.setIlSubjectToCommission(subjectToCommission);
            mdetails.setIlLastModifiedBy(USR_NM);
            mdetails.setIlDateLastModified(new java.util.Date());

            ArrayList itemNameSelectedList = new ArrayList();
            ArrayList locationNameSelectedList = new ArrayList();
            ArrayList branchItemLocationList = new ArrayList();
            ArrayList categoryNameSelectedList = this.getAdLvInvItemCategoryAll(AD_CMPNY);

            itemNameSelectedList.add(itemRequest.getItemName());
            locationNameSelectedList.add("DEFAULT");

            // Setup Multiple Branch Map
            Collection branches = adBranchHome.findBrAll(AD_CMPNY);
            for (Object o : branches) {
                LocalAdBranch branch = (LocalAdBranch) o;

                // branch itemDetails
                AdModBranchItemLocationDetails mainBrDetails = new AdModBranchItemLocationDetails();
                mainBrDetails.setBilRack(null);
                mainBrDetails.setBilBin(null);
                mainBrDetails.setBilReorderPoint(reorderPoint);
                mainBrDetails.setBilReorderQuantity(reorderQuantity);

                // Default to Main COA
                mainBrDetails.setBilCoaGlWipAccountNumber(wipAccount);
                mainBrDetails.setBilCoaGlAccruedInventoryAccountNumber(accruedInvAccount);

                if (!itemRequest.getItemCategory().equalsIgnoreCase("RECHARGE CARDS 3G") ||
                        !itemRequest.getItemCategory().equalsIgnoreCase("RECHARGE CARDS 4G")) {
                    mainBrDetails.setBilCoaGlSalesAccountNumber(
                            EJBCommon.rebuildBranchMapCOA(salesAccount, branch.getBrBranchCode()));
                    mainBrDetails.setBilCoaGlSalesReturnAccountNumber(
                            EJBCommon.rebuildBranchMapCOA(salesReturnAccount, branch.getBrBranchCode()));
                } else {
                    mainBrDetails.setBilCoaGlSalesAccountNumber(salesAccount);
                    mainBrDetails.setBilCoaGlSalesReturnAccountNumber(salesReturnAccount);
                }

                mainBrDetails.setBilCoaGlInventoryAccountNumber(
                        EJBCommon.rebuildBranchMapCOA(inventoryAccount, branch.getBrBranchCode()));
                mainBrDetails.setBilCoaGlCostOfSalesAccountNumber(
                        EJBCommon.rebuildBranchMapCOA(costOfSalesAccount, branch.getBrBranchCode()));
                mainBrDetails.setBilCoaGlSalesReturnAccountNumber(
                        EJBCommon.rebuildBranchMapCOA(salesReturnAccount, branch.getBrBranchCode()));

                mainBrDetails.setBilBrName(branch.getBrName());
                mainBrDetails.setBilHist1Sales(0);
                mainBrDetails.setBilHist2Sales(0);
                mainBrDetails.setBilProjectedSales(0);
                mainBrDetails.setBilDeliveryTime(0);
                mainBrDetails.setBilDeliveryBuffer(0);
                mainBrDetails.setBilOrderPerYear(0);
                mainBrDetails.setBilSubjectToCommission(subjectToCommission);
                mainBrDetails.setBilLastModifiedBy(USR_NM);
                mainBrDetails.setBilDateLastModified(new java.util.Date());
                branchItemLocationList.add(mainBrDetails);
            }

            invItemLocationEntryApiController.saveInvIlEntry(mdetails, itemNameSelectedList, locationNameSelectedList,
                    branchItemLocationList, categoryNameSelectedList, itemClass, AD_CMPNY);

        } catch (InvILCoaGlSalesAccountNotFoundException | InvILCoaGlWipAccountNotFoundException |
                 InvILCoaGlCostOfSalesAccountNotFoundException | InvILCoaGlInventoryAccountNotFoundException |
                 InvILCoaGlSalesReturnAccountNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw ex;
        }
        return apiResponse;
    }

    @Override
    public OfsApiResponse createItem(ItemRequest itemRequest) {
        Debug.print("InvItemEntryApiControllerBean createItem");

        OfsApiResponse apiResponse = new OfsApiResponse();
        LocalAdCompany adCompany = null;
        LocalAdUser adUser = null;
        LocalAdUserResponsibility adUserRes = null;
        LocalInvItem invItem = null;
        LocalInvUnitOfMeasure invUnitOfMeasure = null;

        try {
            InvItemDetails itemDetails = new InvItemDetails();

            // Company Code
            try {
                if (itemRequest.getCompanyCode() == null || itemRequest.getCompanyCode().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_041);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_041_MSG);
                    return apiResponse;
                }
                adCompany = adCompanyHome.findByCmpShortName(itemRequest.getCompanyCode());
                itemDetails.setIiAdCompany(adCompany.getCmpCode());
            } catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
                return apiResponse;
            }

            // User and Responsibility
            try {
                if (itemRequest.getUsername() == null || itemRequest.getUsername().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_042);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_042_MSG);
                    return apiResponse;
                }
                adUser = adUserHome.findByUsrName(itemRequest.getUsername(), adCompany.getCmpCode());
                if (adUser != null) {
                    adUserRes = adUserResponsibilityHome.findRsCodeByUsrName(adUser.usrCode, adCompany.getCmpCode());
                }
            } catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_002);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_002_MSG);
                return apiResponse;
            }

            // Unit of Measure
            try {
                if (itemRequest.getUnitOfMeasure() == null || itemRequest.getUnitOfMeasure().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_043);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_043_MSG);
                    return apiResponse;
                }
                invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(itemRequest.getUnitOfMeasure(), adCompany.getCmpCode());
                itemDetails.setIiRetailUom(invUnitOfMeasure.getUomCode());
            } catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_003);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_003_MSG);
                return apiResponse;
            }

            // Category
            try {
                if (itemRequest.getItemCategory() == null || itemRequest.getItemCategory().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_044);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_044_MSG);
                    return apiResponse;
                }
                adLookUpValueHome.findByLuNameAndLvName(EJBCommon.INV_ITEM_CATEGORY, itemRequest.getItemCategory(), adCompany.getCmpCode());
                itemDetails.setIiAdLvCategory(itemRequest.getItemCategory());
            } catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_004);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_004_MSG);
                return apiResponse;
            }

            // Item Name
            try {
                if (itemRequest.getItemName() == null || itemRequest.getItemName().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_045);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_045_MSG);
                    return apiResponse;
                }
                invItem = invItemHome.findByIiName(itemRequest.getItemName(), adCompany.getCmpCode());
                if (invItem != null) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_051);
                    apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_051_MSG, itemRequest.getItemName()));
                    return apiResponse;
                }
            } catch (FinderException ex) {
            }

            // Validate unit cost is provided by TT
            if (itemRequest.getUnitCost() == 0d) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_058);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_058_MSG);
                return apiResponse;
            }

            // Validate item description is provided by TT
            if (itemRequest.getDescription() == null || itemRequest.getDescription().equals("")) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_046);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_046_MSG);
                return apiResponse;
            }

            // Validate cost method is provided by TT
            if (itemRequest.getCostMethod() == null || itemRequest.getCostMethod().equals("")) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_057);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_057_MSG);
                return apiResponse;
            }

            // Validate item class is provided by TT
            if (itemRequest.getItemClass() == null || itemRequest.getItemClass().equals("")) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_056);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_056_MSG);
                return apiResponse;
            }

            double salesPrice = itemRequest.getItemSalesPrice(); //NOTE: As discussed sales price will be input from invoice API
            double shippingCost = 0d; // TODO: Verify if shipping cost if included
            double unitCost = itemRequest.getUnitCost();
            double percentMarkup = ((salesPrice - unitCost - shippingCost) / (unitCost + shippingCost)) * 100;

            itemDetails.setIiName(itemRequest.getItemName());
            itemDetails.setIiPercentMarkup(percentMarkup);
            itemDetails.setIiShippingCost(shippingCost);
            itemDetails.setIiSalesPrice(salesPrice);
            itemDetails.setIiUnitCost(unitCost);
            itemDetails.setIiDescription(itemRequest.getDescription());
            itemDetails.setIiUmcPackaging(itemRequest.getUnitOfMeasure());
            itemDetails.setIiCostMethod(itemRequest.getCostMethod());
            itemDetails.setIiEnable(EJBCommon.TRUE);
            itemDetails.setIiNonInventoriable(EJBCommon.TRUE);
            itemDetails.setIiCreatedBy(itemRequest.getUsername());
            itemDetails.setIiDateCreated(new java.util.Date());
            itemDetails.setIiLastModifiedBy(itemRequest.getUsername());
            itemDetails.setIiDateLastModified(new java.util.Date());
            itemDetails.setIiClass(itemRequest.getItemClass());
            itemDetails.setIiTaxCode("GST INCLUSIVE"); // TODO: Verify if this should be pass from the TechnoTree

            LocalInvLocation invLocation = invLocationHome.findByLocName("DEFAULT", adCompany.getCmpCode());
            itemDetails.setIiDefaultLocation(invLocation.getLocCode());

            // Item Entry
            Integer invItemCode = this.saveInvIiEntry(itemDetails, itemRequest.getUnitOfMeasure(), null, null, adCompany.getCmpCode());
            invItem = invItemHome.findByPrimaryKey(invItemCode);

            // Item Location Entry
            apiResponse = this.saveIiInvItemLocation(itemRequest, adUserRes.getAdResponsibility().getRsCode(), adUser.usrName, adCompany.getCmpCode());

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            apiResponse.setStatus("Added New Item successfully.");

        } catch (InvILCoaGlSalesAccountNotFoundException | InvILCoaGlWipAccountNotFoundException |
                 InvILCoaGlCostOfSalesAccountNotFoundException | InvILCoaGlInventoryAccountNotFoundException |
                 InvILCoaGlSalesReturnAccountNotFoundException ex) {
            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_070);
            apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_070_MSG, ex.getMessage()));
            return apiResponse;
        } catch (GlobalRecordAlreadyAssignedException ex) {
            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_006);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_006_MSG);
            return apiResponse;
        } catch (Exception ex) {
            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return apiResponse;
        }
        return apiResponse;
    }
}