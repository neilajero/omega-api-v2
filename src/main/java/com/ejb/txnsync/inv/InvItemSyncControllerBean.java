package com.ejb.txnsync.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.dao.inv.LocalInvBillOfMaterialHome;
import com.ejb.dao.inv.LocalInvItemHome;
import com.ejb.dao.inv.LocalInvUnitOfMeasureHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.inv.*;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Stateless(name = "InvItemSyncControllerBeanEJB")
public class InvItemSyncControllerBean extends EJBContextClass implements InvItemSyncController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private LocalInvBillOfMaterialHome invBillOfMaterialHome;

    @Override
    public int getInvItemAllNewLength(String branchCodeName, String itemLocation, Integer companyCode) {

        Debug.print("InvItemSyncControllerBean getInvItemAllNewLength");

        try {
            LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(branchCodeName, companyCode);
            Collection invItemsNew = invItemHome.findIiByIiNewAndUpdated(
                    adBranch.getBrCode(), itemLocation, companyCode, 'N', 'N', 'N');
            return invItemsNew.size();
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public int getInvItemAllNewLengthAnyLoc(String branchCodeName, Integer companyCode) {

        Debug.print("InvItemSyncControllerBean getInvItemAllNewLength");

        try {
            LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(branchCodeName, companyCode);
            Collection invItemsNew = invItemHome.findIiByIiNewAndUpdated(
                    adBranch.getBrCode(), companyCode, 'N', 'N', 'N');
            return invItemsNew.size();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public int getInvItemAllUpdatedLength(String branchCodeName, String itemLocation, Integer companyCode) {

        Debug.print("InvItemSyncControllerBean getInvItemAllUpdatedLength");

        try {
            LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(branchCodeName, companyCode);
            Collection invItemsNew = invItemHome.findIiByIiNewAndUpdated(
                    adBranch.getBrCode(), itemLocation, companyCode, 'U', 'U', 'X');
            return invItemsNew.size();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public int getInvItemAllUpdatedLengthAnyLoc(String branchCodeName, Integer companyCode) {

        Debug.print("InvItemSyncControllerBean getInvItemAllUpdatedLength");

        try {
            LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(branchCodeName, companyCode);
            Collection invItemsNew = invItemHome.findIiByIiNewAndUpdated(
                    adBranch.getBrCode(), companyCode, 'U', 'U', 'X');
            return invItemsNew.size();
        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }
    }

    @Override
    public String getInvItemAllDownloadedAnyLoc(String branchCodeName, Integer companyCode) {

        Debug.print("InvItemSyncControllerBean getInvItemAllDownloadedAnyLoc");

        char separator = EJBCommon.SEPARATOR;
        StringBuilder InvItemAllDownloaded = new StringBuilder();

        try {

            LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(branchCodeName, companyCode);
            Collection invDownloadedItems = invItemHome.findIiByIiNewAndUpdated(
                    adBranch.getBrCode(), companyCode, 'D', 'D', 'D');

            Iterator i = invDownloadedItems.iterator();
            while (i.hasNext()) {
                LocalInvItem invItem = (LocalInvItem)i.next();
                InvItemAllDownloaded.append(separator);
                InvItemAllDownloaded.append(invItem.getIiCode());
            }
            InvItemAllDownloaded.append(separator);
            return InvItemAllDownloaded.toString();

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public String getInvItemAllDownloaded(String branchCodeName, String itemLocation, Integer companyCode) {

        Debug.print("InvItemSyncControllerBean getInvItemAllDownloaded");

        char separator = EJBCommon.SEPARATOR;
        StringBuilder InvItemAllDownloaded = new StringBuilder();

        try {

            LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(branchCodeName, companyCode);
            Collection invDownloadedItems = invItemHome.findIiByIiNewAndUpdated(
                    adBranch.getBrCode(), itemLocation, companyCode, 'D', 'D', 'D');

            Iterator i = invDownloadedItems.iterator();
            while (i.hasNext()) {
                LocalInvItem invItem = (LocalInvItem)i.next();
                InvItemAllDownloaded.append(separator);
                InvItemAllDownloaded.append(invItem.getIiCode());
            }

            InvItemAllDownloaded.append(separator);
            return InvItemAllDownloaded.toString();

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public String getInvItemAllDownloadedWithUnitCost(String branchCodeName, String itemLocation, Integer companyCode) {

        Debug.print("InvItemSyncControllerBean getInvItemAllDownloadedWithUnitCost");

        char separator = EJBCommon.SEPARATOR;
        StringBuilder InvItemAllDownloaded = new StringBuilder();

        try {

            LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(branchCodeName, companyCode);
            Collection invDownloadedItems = invItemHome.findIiByIiNewAndUpdated(
                    adBranch.getBrCode(), itemLocation, companyCode, 'D', 'D', 'D');

            Iterator i = invDownloadedItems.iterator();
            while (i.hasNext()) {
                LocalInvItem invItem = (LocalInvItem)i.next();
                InvItemAllDownloaded.append(separator);
                InvItemAllDownloaded.append(invItem.getIiCode());
            }

            InvItemAllDownloaded.append(separator);
            return InvItemAllDownloaded.toString();

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public String[] getInvItemAllNewAndUpdatedAnyLoc(String branchCodeName, Integer companyCode) {

        Debug.print("InvItemSyncControllerBean getInvItemAllNewAndUpdatedAnyLoc");

        String II_IL_LOCATION = "";

        try {

            LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(branchCodeName, companyCode);
            Collection invItems = invItemHome.findIiByIiNewAndUpdated(
                    adBranch.getBrCode(), companyCode, 'N', 'N', 'N');
            Collection invUpdatedItems = invItemHome.findIiByIiNewAndUpdated(
                    adBranch.getBrCode(), companyCode, 'U', 'U', 'X');

            String[] results = new String[invItems.size() + invUpdatedItems.size()];

            Iterator i = invItems.iterator();
            int ctr = 0;

            while (i.hasNext()) {

                LocalInvItem invItem = (LocalInvItem)i.next();

                String retailUom = null;
                if (invItem.getIiRetailUom() != null) {
                    LocalInvUnitOfMeasure invRetailUom = invUnitOfMeasureHome.findByPrimaryKey(invItem.getIiRetailUom());
                    retailUom = invRetailUom.getUomName();
                }

                Collection invBillOfMaterials = invBillOfMaterialHome.findByBomIiName(invItem.getIiName() , companyCode);
                if(invBillOfMaterials.size() <=0 ) {
                    results[ctr] = itemRowEncode(invItem, 0, II_IL_LOCATION, EJBCommon.FALSE, 0, 0, "NA", retailUom);
                }else {
                    results[ctr] = itemRowEncode(invItem, 0, II_IL_LOCATION, EJBCommon.TRUE, 0, 0, "NA", retailUom);
                }
                ctr++;
            }

            i = invUpdatedItems.iterator();
            while (i.hasNext()) {
                LocalInvItem invItem = (LocalInvItem)i.next();

                String retailUom = null;
                if (invItem.getIiRetailUom() != null) {
                    LocalInvUnitOfMeasure invRetailUom = invUnitOfMeasureHome.findByPrimaryKey(invItem.getIiRetailUom());
                    retailUom = invRetailUom.getUomName();
                }

                Collection invBillOfMaterials = invBillOfMaterialHome.findByBomIiName(invItem.getIiName() , companyCode);
                if(invBillOfMaterials.size() == 0) {
                    results[ctr] = itemRowEncode(invItem, 0, II_IL_LOCATION, EJBCommon.FALSE, 0, 0, "NA", retailUom);
                }else {
                    results[ctr] = itemRowEncode(invItem, 0, II_IL_LOCATION, EJBCommon.TRUE, 0, 0, "NA", retailUom);
                }
                ctr++;
            }
            return results;

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public String[] getInvItemAllNewAndUpdated(String branchCodeName, String itemLocation, Integer companyCode) {

        return new String[0];
    }

    @Override
    public String[] getInvItemAllNewAndUpdatedPosUs(String branchCodeName, String itemLocation, Integer companyCode) {

        return new String[0];
    }

    @Override
    public String[] getInvItemAllNewAndUpdatedWithUnitPrice(String branchCodeName, String itemLocation, Integer companyCode) {

        return new String[0];
    }

    @Override
    public int setInvItemAllNewAndUpdatedSuccessConfirmationAnyLoc(String branchCodeName, Integer companyCode) {

        return 0;
    }

    @Override
    public int setInvItemAllNewAndUpdatedSuccessConfirmation(String branchCodeName, String itemLocation, Integer companyCode) {

        return 0;
    }

    @Override
    public String[] getAllMemoLineInvoice(String branchCodeName, Integer companyCode) {

        return new String[0];
    }

    @Override
    public String[] getAllConsolidatedItems(String[] ItemStr, Integer companyCode) {

        return new String[0];
    }

    @Override
    public String[] getAllPostedArSoMatchedInvoice(String branchCodeName, Integer companyCode) {

        return new String[0];
    }

    @Override
    public int setInvAdjustment(String[] ADJ, String branchCodeName, Integer companyCode) {

        return 0;
    }

    @Override
    public int setArBillingInvoiceAndCreditMemos(String[] cmTxn, String[] invTxn, String branchCodeName, Integer companyCode) {

        return 0;
    }

    @Override
    public String[] convertBulkToLooseUom(Double qty, String II_NM, String BULK_UOM_NM, String LOOSE_UOM_NM, Integer companyCode) {

        return new String[0];
    }

    @Override
    public int setApReceivingItem(String[] RR, String branchCodeName, Integer companyCode) {

        return 0;
    }

    @Override
    public int setApReceivingItemPoCondition(String[] RR, String branchCodeName, Integer companyCode) {

        return 0;
    }

    @Override
    public int setApPurchaseOrder(String[] PO, Boolean isReceiving, String branchCodeName, Integer companyCode) {

        return 0;
    }

    @Override
    public String setInvBST(String[] BST, String branchCodeName, Integer companyCode) {

        return null;
    }

    @Override
    public String[] getInvBranchStockTransferAllIncoming(String branchCodeName, Integer companyCode) {

        return new String[0];
    }

    @Override
    public String[] getInvStockOnHand(String branchCodeName, String itemLocation, Integer companyCode) {

        return new String[0];
    }

    @Override
    public String[] getInvStockOnHandWithExpiryDate(String branchCodeName, String itemLocation, Integer companyCode) {

        return new String[0];
    }

    @Override
    public String[] getInvStockOnHandOnly(String[] invUploadOrig, Integer companyCode) {

        return new String[0];
    }

    @Override
    public String[] getLastPo(String invoiceDate, String location, String[] invUploadOrig, String branchCode, Integer companyCode) {

        return new String[0];
    }

    @Override
    public String[] getLastPoPerItem(String invoiceDate, String location, String[] invUploadOrig, String branchCode, Integer companyCode) {

        return new String[0];
    }

    @Override
    public String getCheckInsufficientStocks(String invoiceDateFrom, String invoiceDateTo, String location, String user, String invAdjAccount, String transactionType, String branchCode, Integer companyCode) {

        return null;
    }

    @Override
    public String[] getSoMatchedInvBOAllPosted(String DateFrom, String DateTo, String branchCodeName, Integer companyCode) {

        return new String[0];
    }

    @Override
    public String[] getApPOAllPostedAndUnreceived(String branchCodeName, Integer companyCode) {

        return new String[0];
    }

    @Override
    public String getPaymentTermsAll(Integer companyCode) {

        return null;
    }

    @Override
    public String getTaxCodesAll(Integer companyCode) {

        return null;
    }

    @Override
    public String getFunctionalCurrenciesAll(Integer companyCode) {

        return null;
    }


    private String itemRowEncode(LocalInvItem invItem, Integer INV_LOCATION, String II_IL_LOCATION,
                                 byte isItemRaw, int quantity, int unitValue, String baseUnitUOM, String retailUom) {


        char separator = EJBCommon.SEPARATOR;
        StringBuilder encodedResult = new StringBuilder();

        // Start separator
        encodedResult.append(separator);

        // Primary Key
        encodedResult.append(invItem.getIiCode().toString());
        encodedResult.append(separator);

        // Name / OPOS: Item Code
        encodedResult.append(invItem.getIiName());
        encodedResult.append(separator);

        // Part Number
        if (invItem.getIiPartNumber() == null || invItem.getIiPartNumber().length() < 1) {
            encodedResult.append("none");
            encodedResult.append(separator);
        } else {
            encodedResult.append(invItem.getIiPartNumber());
            encodedResult.append(separator);
        }

        // Unit of Measure / OPOS: UOM
        encodedResult.append(invItem.getInvUnitOfMeasure().getUomName());
        //		encodedResult.append(invItem.getInvUnitOfMeasure().getUomCode().toString());
        encodedResult.append(separator);

        // Description / OPOS: Item Description
        encodedResult.append(invItem.getIiDescription());
        encodedResult.append(separator);

        // Sales Price / OPOS: Unit Price
        encodedResult.append(Double.toString(invItem.getIiSalesPrice()));
        encodedResult.append(separator);

        // Location / OPOS: Location
        encodedResult.append(II_IL_LOCATION);
        encodedResult.append(separator);

        // Sub Location / OPOS: Location
        encodedResult.append(INV_LOCATION.toString());
        encodedResult.append(separator);

        // Category / OPOS: Product Category
        encodedResult.append(invItem.getIiAdLvCategory());
        encodedResult.append(separator);

        // Doneness
        if (invItem.getIiDoneness()== null || invItem.getIiDoneness().length() < 1) {
            encodedResult.append("NA");
            encodedResult.append(separator);
        } else {
            encodedResult.append(invItem.getIiDoneness());
            encodedResult.append(separator);
        }

        // Remarks
        if (invItem.getIiRemarks() == null || invItem.getIiRemarks().length() < 1) {
            encodedResult.append("none");
            encodedResult.append(separator);
        } else {
            encodedResult.append(invItem.getIiRemarks());
            encodedResult.append(separator);
        }

        // Sidings
        if (invItem.getIiSidings() == null || invItem.getIiSidings().length() < 1) {
            encodedResult.append("none");
            encodedResult.append(separator);
        } else {
            encodedResult.append(invItem.getIiSidings());
            encodedResult.append(separator);
        }

        // Service Charge
        encodedResult.append(invItem.getIiServiceCharge());
        encodedResult.append(separator);

        // Non Inventoriable
        encodedResult.append(invItem.getIiNonInventoriable());
        encodedResult.append(separator);

        // isItemRaw
        if (isItemRaw==EJBCommon.TRUE){
            encodedResult.append("1");
            encodedResult.append(separator);
        }else{
            encodedResult.append("0");
            encodedResult.append(separator);
        }

        // Open Product
        encodedResult.append(invItem.getIiOpenProduct());
        encodedResult.append(separator);

        // Quantity
        encodedResult.append(Integer.toString(quantity));
        encodedResult.append(separator);

        //UnitValue
        encodedResult.append(Integer.toString(unitValue));
        encodedResult.append(separator);

        //BaseUnitUOM
        encodedResult.append(baseUnitUOM);
        encodedResult.append(separator);


        // retail UOM
        if (retailUom == null || retailUom.length() < 1) {
            encodedResult.append("none");
            encodedResult.append(separator);
        } else {
            encodedResult.append(retailUom);
            encodedResult.append(separator);
        }

        // Unit Cost
        encodedResult.append(invItem.getIiUnitCost());
        encodedResult.append(separator);

        // Shipping Cost
        encodedResult.append(invItem.getIiShippingCost());
        encodedResult.append(separator);

        // Track Misc
        encodedResult.append(invItem.getIiTraceMisc());
        encodedResult.append(separator);

        // Enable Item
        encodedResult.append(invItem.getIiEnable());
        encodedResult.append(separator);

        // INV_PRC_LVL
        encodedResult.append("INV_PRC_LVL");
        encodedResult.append(separator);

        Collection invPriceLevels = invItem.getInvPriceLevels();
        Collection invPriceLevelDates = invItem.getInvPriceLevelsDate();

        Iterator i = invPriceLevels.iterator();
        while (i.hasNext()) {
            LocalInvPriceLevel invPriceLevel = (LocalInvPriceLevel)i.next();
            String priceLevelAmount = Double.toString(invPriceLevel.getPlAmount());
            Date dateNow =  new Date();

            Iterator iPd = invPriceLevelDates.iterator();
            while(iPd.hasNext()){
                LocalInvPriceLevelDate invPriceLevelDate = (LocalInvPriceLevelDate)iPd.next();
                if(invPriceLevel.getPlAdLvPriceLevel().equals(invPriceLevelDate.getPdAdLvPriceLevel()) && invPriceLevelDate.getPdStatus().equals("ENABLE")){
                    System.out.println("hello");
                    if(dateNow.equals(invPriceLevelDate.getPdDateFrom()) || dateNow.equals(invPriceLevelDate.getPdDateTo())){
                        priceLevelAmount =  Double.toString(invPriceLevelDate.getPdAmount());
                        break;
                    }

                    if(dateNow.after(invPriceLevelDate.getPdDateFrom()) && dateNow.before(invPriceLevelDate.getPdDateTo())){
                        priceLevelAmount =  Double.toString(invPriceLevelDate.getPdAmount());
                        break;
                    }
                }
            }

            // primary key
            encodedResult.append(invPriceLevel.getPlCode().toString());
            encodedResult.append(separator);

            // amount
            encodedResult.append(priceLevelAmount);
            encodedResult.append(separator);

            // price level
            encodedResult.append(invPriceLevel.getPlAdLvPriceLevel());
            encodedResult.append(separator);

            // item
            encodedResult.append(invPriceLevel.getInvItem().getIiCode());
            encodedResult.append(separator);

        }

        // INV_UNT_OF_MSR_CNVRSN
        encodedResult.append("INV_UNT_OF_MSR_CNVRSN");
        encodedResult.append(separator);

        Collection invUnitOfMeasureConversions = invItem.getInvUnitOfMeasureConversions();

        i = invUnitOfMeasureConversions.iterator();

        while (i.hasNext()) {

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = (LocalInvUnitOfMeasureConversion)i.next();

            // base unit
            encodedResult.append(String.valueOf(invUnitOfMeasureConversion.getUmcBaseUnit()));
            encodedResult.append(separator);

            // conversion factor
            encodedResult.append(Double.toString(invUnitOfMeasureConversion.getUmcConversionFactor()));
            encodedResult.append(separator);

            // item
            encodedResult.append(invUnitOfMeasureConversion.getInvItem().getIiCode());
            encodedResult.append(separator);

            // uom
            encodedResult.append(invUnitOfMeasureConversion.getInvUnitOfMeasure().getUomCode());
            encodedResult.append(separator);

        }

        // INV_BLL_OF_MTRL
        encodedResult.append("INV_BLL_OF_MTRL");
        encodedResult.append(separator);

        Collection invBillOfMaterials = invItem.getInvBillOfMaterials();

        i = invBillOfMaterials.iterator();

        while (i.hasNext()) {

            LocalInvBillOfMaterial invBillOfMaterial = (LocalInvBillOfMaterial)i.next();

            // primary key
            encodedResult.append(invBillOfMaterial.getBomCode());
            encodedResult.append(separator);

            // raw item
            encodedResult.append(invBillOfMaterial.getBomIiName());
            encodedResult.append(separator);

            // qty
            encodedResult.append(invBillOfMaterial.getBomQuantityNeeded());
            encodedResult.append(separator);

            // item
            encodedResult.append(invBillOfMaterial.getInvItem().getIiCode());
            encodedResult.append(separator);

        }

        // item end
        encodedResult.append("ITEM_END");
        encodedResult.append(separator);

        // End separator
        encodedResult.append(separator);

        return encodedResult.toString();


    }
    // End separator

}