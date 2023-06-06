package com.ejb.txnsync.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.dao.ad.LocalAdBranchItemLocationHome;
import com.ejb.dao.ap.LocalApPurchaseOrderLineHome;
import com.ejb.dao.ar.LocalArInvoiceHome;
import com.ejb.dao.ar.LocalArReceiptHome;
import com.ejb.dao.ar.LocalArStandardMemoLineHome;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdBranchItemLocation;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ap.LocalApPurchaseOrderLine;
import com.ejb.entities.ar.*;
import com.ejb.entities.inv.*;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.txn.OmegaCommonDataController;
import com.ejb.txn.ap.ApPurchaseOrderEntryController;
import com.ejb.txn.ar.ArCreditMemoEntryController;
import com.ejb.txn.ar.ArInvoiceEntryController;
import com.ejb.txn.inv.InvAdjustmentEntryController;
import com.ejb.txn.inv.InvBranchStockTransferInEntryController;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.mod.ap.ApModPurchaseOrderDetails;
import com.util.mod.ap.ApModPurchaseOrderLineDetails;
import com.util.mod.ar.ArModInvoiceDetails;
import com.util.mod.ar.ArModInvoiceLineDetails;
import com.util.mod.inv.*;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import java.text.SimpleDateFormat;
import java.util.*;

@Stateless(name = "InvItemSyncControllerBeanEJB")
public class InvItemSyncControllerBean extends EJBContextClass implements InvItemSyncController {

    private final String DATE_FORMAT_OUTPUT = "MM/dd/yyyy";
    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private LocalInvBillOfMaterialHome invBillOfMaterialHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalArStandardMemoLineHome arStandardMemoLineHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalInvBranchStockTransferHome invBranchStockTransferHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalApPurchaseOrderLineHome apPurchaseOrderLineHome;
    @EJB
    private LocalInvAdjustmentHome invAdjustmentHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private InvAdjustmentEntryController invAdjustmentEntryController;
    @EJB
    private ArCreditMemoEntryController arCreditMemoEntryController;
    @EJB
    private ArInvoiceEntryController arInvoiceEntryController;
    @EJB
    private ApPurchaseOrderEntryController apPurchaseOrderEntryController;
    @EJB
    private InvBranchStockTransferInEntryController invBranchStockTransferInEntryController;
    @EJB
    private OmegaCommonDataController commonData;

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
        }
        catch (Exception ex) {
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
        }
        catch (Exception ex) {
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
        }
        catch (Exception ex) {

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

            for (Object invDownloadedItem : invDownloadedItems) {
                LocalInvItem invItem = (LocalInvItem) invDownloadedItem;
                InvItemAllDownloaded.append(separator);
                InvItemAllDownloaded.append(invItem.getIiCode());
            }
            InvItemAllDownloaded.append(separator);
            return InvItemAllDownloaded.toString();

        }
        catch (Exception ex) {
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

            for (Object invDownloadedItem : invDownloadedItems) {
                LocalInvItem invItem = (LocalInvItem) invDownloadedItem;
                InvItemAllDownloaded.append(separator);
                InvItemAllDownloaded.append(invItem.getIiCode());
            }

            InvItemAllDownloaded.append(separator);
            return InvItemAllDownloaded.toString();

        }
        catch (Exception ex) {
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

            for (Object invDownloadedItem : invDownloadedItems) {
                LocalInvItem invItem = (LocalInvItem) invDownloadedItem;
                InvItemAllDownloaded.append(separator);
                InvItemAllDownloaded.append(invItem.getIiCode());
            }

            InvItemAllDownloaded.append(separator);
            return InvItemAllDownloaded.toString();

        }
        catch (Exception ex) {
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

                LocalInvItem invItem = (LocalInvItem) i.next();

                String retailUom = null;
                if (invItem.getIiRetailUom() != null) {
                    LocalInvUnitOfMeasure invRetailUom = invUnitOfMeasureHome.findByPrimaryKey(invItem.getIiRetailUom());
                    retailUom = invRetailUom.getUomName();
                }

                Collection invBillOfMaterials = invBillOfMaterialHome.findByBomIiName(invItem.getIiName(), companyCode);
                if (invBillOfMaterials.size() == 0) {
                    results[ctr] = itemRowEncode(invItem, 0, II_IL_LOCATION, EJBCommon.FALSE, 0, 0, "NA", retailUom);
                } else {
                    results[ctr] = itemRowEncode(invItem, 0, II_IL_LOCATION, EJBCommon.TRUE, 0, 0, "NA", retailUom);
                }
                ctr++;
            }

            i = invUpdatedItems.iterator();
            while (i.hasNext()) {
                LocalInvItem invItem = (LocalInvItem) i.next();

                String retailUom = null;
                if (invItem.getIiRetailUom() != null) {
                    LocalInvUnitOfMeasure invRetailUom = invUnitOfMeasureHome.findByPrimaryKey(invItem.getIiRetailUom());
                    retailUom = invRetailUom.getUomName();
                }

                Collection invBillOfMaterials = invBillOfMaterialHome.findByBomIiName(invItem.getIiName(), companyCode);
                if (invBillOfMaterials.size() == 0) {
                    results[ctr] = itemRowEncode(invItem, 0, II_IL_LOCATION, EJBCommon.FALSE, 0, 0, "NA", retailUom);
                } else {
                    results[ctr] = itemRowEncode(invItem, 0, II_IL_LOCATION, EJBCommon.TRUE, 0, 0, "NA", retailUom);
                }
                ctr++;
            }
            return results;

        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public String[] getInvItemAllNewAndUpdated(String branchCodeName, String itemLocation, Integer companyCode) {

        Debug.print("InvItemSyncControllerBean getInvItemAllNewAndUpdated");

        try {

            LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(branchCodeName, companyCode);

            Collection invItems = invItemHome.findIiByIiNewAndUpdated(
                    adBranch.getBrCode(), itemLocation, companyCode, 'N', 'N', 'N');
            Collection invUpdatedItems = invItemHome.findIiByIiNewAndUpdated(
                    adBranch.getBrCode(), itemLocation, companyCode, 'U', 'U', 'X');

            String[] results = new String[invItems.size() + invUpdatedItems.size()];

            Iterator i = invItems.iterator();
            int ctr = 0;
            while (i.hasNext()) {

                LocalInvItem invItem = (LocalInvItem) i.next();
                String retailUom = null;
                if (invItem.getIiRetailUom() != null) {
                    LocalInvUnitOfMeasure invRetailUom = invUnitOfMeasureHome.findByPrimaryKey(invItem.getIiRetailUom());
                    retailUom = invRetailUom.getUomName();
                }

                Collection invBillOfMaterials = invBillOfMaterialHome.findByBomIiName(invItem.getIiName(), companyCode);
                if (invBillOfMaterials.size() == 0) {
                    results[ctr] = itemRowEncode(invItem, 0, itemLocation, EJBCommon.FALSE, 0, 0, "NA", retailUom);
                } else {
                    results[ctr] = itemRowEncode(invItem, 0, itemLocation, EJBCommon.TRUE, 0, 0, "NA", retailUom);
                }
                ctr++;
            }

            i = invUpdatedItems.iterator();
            while (i.hasNext()) {
                LocalInvItem invItem = (LocalInvItem) i.next();
                String retailUom = null;
                if (invItem.getIiRetailUom() != null) {
                    LocalInvUnitOfMeasure invRetailUom = invUnitOfMeasureHome.findByPrimaryKey(invItem.getIiRetailUom());
                    retailUom = invRetailUom.getUomName();
                }

                Collection invBillOfMaterials = invBillOfMaterialHome.findByBomIiName(invItem.getIiName(), companyCode);
                if (invBillOfMaterials.size() == 0) {
                    results[ctr] = itemRowEncode(invItem, 0, itemLocation, EJBCommon.FALSE, 0, 0, "NA", retailUom);
                } else {
                    results[ctr] = itemRowEncode(invItem, 0, itemLocation, EJBCommon.TRUE, 0, 0, "NA", retailUom);
                }
                ctr++;
            }
            return results;
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public String[] getInvItemAllNewAndUpdatedPosUs(String branchCodeName, String itemLocation, Integer companyCode) {

        Debug.print("InvItemSyncControllerBean getInvItemAllNewAndUpdatedPosUs");

        try {

            LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(branchCodeName, companyCode);

            Collection invItems = invItemHome.findIiByIiNewAndUpdated(
                    adBranch.getBrCode(), itemLocation, companyCode, 'N', 'N', 'N');
            Collection invUpdatedItems = invItemHome.findIiByIiNewAndUpdated(
                    adBranch.getBrCode(), itemLocation, companyCode, 'U', 'U', 'X');

            String[] results = new String[invItems.size() + invUpdatedItems.size()];

            Iterator i = invItems.iterator();
            int ctr = 0;

            while (i.hasNext()) {

                LocalInvItem invItem = (LocalInvItem) i.next();

                String retailUom = null;
                if (invItem.getIiRetailUom() != null) {
                    LocalInvUnitOfMeasure invRetailUom = invUnitOfMeasureHome.findByPrimaryKey(invItem.getIiRetailUom());
                    retailUom = invRetailUom.getUomName();
                }

                Collection invBillOfMaterials = invBillOfMaterialHome.findByBomIiName(invItem.getIiName(), companyCode);

                if (invBillOfMaterials.size() == 0) {
                    results[ctr] = itemRowEncodePosUs(invItem, 0, itemLocation, EJBCommon.FALSE, 0, 0, "NA", retailUom);
                } else {
                    results[ctr] = itemRowEncodePosUs(invItem, 0, itemLocation, EJBCommon.TRUE, 0, 0, "NA", retailUom);
                }
                ctr++;
            }

            i = invUpdatedItems.iterator();
            while (i.hasNext()) {

                LocalInvItem invItem = (LocalInvItem) i.next();
                String retailUom = null;
                if (invItem.getIiRetailUom() != null) {
                    LocalInvUnitOfMeasure invRetailUom = invUnitOfMeasureHome.findByPrimaryKey(invItem.getIiRetailUom());
                    retailUom = invRetailUom.getUomName();
                }

                Collection invBillOfMaterials = invBillOfMaterialHome.findByBomIiName(invItem.getIiName(), companyCode);
                if (invBillOfMaterials.size() == 0) {
                    results[ctr] = itemRowEncodePosUs(invItem, 0, itemLocation, EJBCommon.FALSE, 0, 0, "NA", retailUom);
                } else {
                    results[ctr] = itemRowEncodePosUs(invItem, 0, itemLocation, EJBCommon.TRUE, 0, 0, "NA", retailUom);
                }
                ctr++;
            }
            return results;
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public String[] getInvItemAllNewAndUpdatedWithUnitPrice(String branchCodeName, String itemLocation, Integer companyCode) {

        Debug.print("InvItemSyncControllerBean getInvItemAllNewAndUpdatedWithUnitPrice");

        try {

            LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(branchCodeName, companyCode);

            Collection invItems = invItemHome.findIiByIiNewAndUpdated(
                    adBranch.getBrCode(), itemLocation, companyCode, 'N', 'N', 'N');
            Collection invUpdatedItems = invItemHome.findIiByIiNewAndUpdated(
                    adBranch.getBrCode(), itemLocation, companyCode, 'U', 'U', 'X');

            String[] results = new String[invItems.size() + invUpdatedItems.size()];

            Iterator i = invItems.iterator();
            int ctr = 0;

            while (i.hasNext()) {

                LocalInvItem invItem = (LocalInvItem) i.next();

                String retailUom = null;
                if (invItem.getIiRetailUom() != null) {
                    LocalInvUnitOfMeasure invRetailUom = invUnitOfMeasureHome.findByPrimaryKey(invItem.getIiRetailUom());
                    retailUom = invRetailUom.getUomName();
                }

                Collection invBillOfMaterials = invBillOfMaterialHome.findByBomIiName(invItem.getIiName(), companyCode);
                if (invBillOfMaterials.size() == 0) {
                    results[ctr] = itemRowEncodeWithUnitPrice(invItem, 0, itemLocation, EJBCommon.FALSE, 0, 0, "NA", retailUom);
                } else {
                    results[ctr] = itemRowEncodeWithUnitPrice(invItem, 0, itemLocation, EJBCommon.TRUE, 0, 0, "NA", retailUom);
                }
                ctr++;
            }

            i = invUpdatedItems.iterator();

            while (i.hasNext()) {

                LocalInvItem invItem = (LocalInvItem) i.next();
                String retailUom = null;
                if (invItem.getIiRetailUom() != null) {
                    LocalInvUnitOfMeasure invRetailUom = invUnitOfMeasureHome.findByPrimaryKey(invItem.getIiRetailUom());
                    retailUom = invRetailUom.getUomName();
                }

                Collection invBillOfMaterials = invBillOfMaterialHome.findByBomIiName(invItem.getIiName(), companyCode);
                if (invBillOfMaterials.size() == 0) {
                    results[ctr] = itemRowEncodeWithUnitPrice(invItem, 0, itemLocation, EJBCommon.FALSE, 0, 0, "NA", retailUom);
                } else {
                    results[ctr] = itemRowEncodeWithUnitPrice(invItem, 0, itemLocation, EJBCommon.TRUE, 0, 0, "NA", retailUom);
                }
                ctr++;
            }
            return results;

        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public void setInvItemAllNewAndUpdatedSuccessConfirmationAnyLoc(String branchCodeName, Integer companyCode) {

        Debug.print("InvItemSyncControllerBean setInvItemAllNewAndUpdatedSuccessConfirmation");

        try {

            LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(branchCodeName, companyCode);
            Collection adBranchItemLocations = adBranchItemLocationHome
                    .findEnabledIiByIiNewAndUpdated(adBranch.getBrCode(), companyCode, 'N', 'U', 'X');
            for (Object branchItemLocation : adBranchItemLocations) {
                LocalAdBranchItemLocation adBranchItemLocation = (LocalAdBranchItemLocation) branchItemLocation;
                adBranchItemLocation.setBilItemDownloadStatus('D');
            }

        }
        catch (Exception ex) {
            ctx.setRollbackOnly();
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public void setInvItemAllNewAndUpdatedSuccessConfirmation(String branchCodeName, String itemLocation, Integer companyCode) {

        Debug.print("InvItemSyncControllerBean setInvItemAllNewAndUpdatedSuccessConfirmation");

        try {
            LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(branchCodeName, companyCode);
            Collection adBranchItemLocations = adBranchItemLocationHome
                    .findAllIiByIiNewAndUpdated(adBranch.getBrCode(), itemLocation, companyCode, 'N', 'U', 'X');
            for (Object branchItemLocation : adBranchItemLocations) {
                LocalAdBranchItemLocation adBranchItemLocation = (LocalAdBranchItemLocation) branchItemLocation;
                adBranchItemLocation.setBilItemDownloadStatus('D');
            }
        }
        catch (Exception ex) {
            ctx.setRollbackOnly();
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public String[] getAllMemoLineInvoice(String branchCodeName, Integer companyCode) {

        Debug.print("InvItemSyncControllerBean getAllMemoLineInvoice");

        try {

            Integer AD_BRNCH;
            String dateFrom;
            String dateTo;

            //Parse Date
            dateFrom = branchCodeName.substring(0, branchCodeName.indexOf("$"));
            branchCodeName = branchCodeName.substring(dateFrom.length() + 1);
            dateTo = branchCodeName.substring(0, branchCodeName.indexOf("$"));
            branchCodeName = branchCodeName.substring(dateTo.length() + 1);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);

            Object[] obj = new Object[2];
            obj[0] = sdf.parse(dateFrom);
            obj[1] = sdf.parse(dateTo);

            LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(branchCodeName, companyCode);
            AD_BRNCH = adBranch.getBrCode();

            //TODO: This SQL script should be placed in arInvoiceHome as query string.
            String ilSql = "SELECT OBJECT(inv) FROM ArInvoice inv, IN(inv.arInvoiceLines) il "
                    + "WHERE inv.invPosted = 1 AND inv.invVoid = 0 AND inv.invCreditMemo=0 "
                    + "AND inv.invDate>=?1 AND inv.invDate<=?2 AND inv.invAdBranch = " + AD_BRNCH + " AND inv.invAdCompany = " + companyCode;

            Collection arItemInvoices = arInvoiceHome.getInvByCriteria(ilSql, obj);

            String[] results = new String[arItemInvoices.size()];

            int ctr = 0;
            String currInvNumber = "";

            for (Object arItemInvoice : arItemInvoices) {
                LocalArInvoice arInvoice = (LocalArInvoice) arItemInvoice;
                if (currInvNumber.equals(arInvoice.getInvNumber())) {
                    continue;
                }
                currInvNumber = arInvoice.getInvNumber();
                results[ctr] = mmoInvRowEncode(arInvoice);
                ctr++;
            }
            return results;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public String[] getAllConsolidatedItems(String[] ItemStr, Integer companyCode) {

        Debug.print("InvItemSyncControllerBean getAllConsolidatedItems");

        try {
            String[] results = new String[ItemStr.length];
            for (int i = 0; i < ItemStr.length; i++) {
                InvModItemDetails details = decodeConsolidatedItem(ItemStr[i]);
                try {
                    LocalInvItem invItem;
                    try {
                        invItemHome.findByIiName(details.getIiName(), companyCode);
                        ctx.setRollbackOnly();
                        throw new GlobalRecordAlreadyExistException();
                    }
                    catch (FinderException ex) {
                        Debug.print("Finder Exception : " + ex.getMessage());
                    }

                    LocalInvLocation invLocation = invLocationHome.findByLocName(details.getIiDefaultLocationName(), companyCode);
                    LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(details.getIiUomName(), companyCode);

                    // Create Item
                    invItem = invItemHome
                            .IiName(details.getIiName())
                            .IiDescription(details.getIiDescription())
                            .IiPartNumber(details.getIiPartNumber())
                            .IiShortName(details.getIiShortName())
                            .IiBarCode1(details.getIiBarCode1())
                            .IiBarCode2(details.getIiBarCode2())
                            .IiBarCode3(details.getIiBarCode3())
                            .IiBrand(details.getIiBrand())
                            .IiClass(details.getIiClass())
                            .IiAdLvCategory(details.getIiAdLvCategory())
                            .IiCostMethod(details.getIiCostMethod())
                            .IiUnitCost(details.getIiUnitCost())
                            .IiSalesPrice(details.getIiSalesPrice())
                            .IiEnable(details.getIiEnable())
                            .IiVirtualStore(details.getIiVirtualStore())
                            .IiEnableAutoBuild(details.getIiEnableAutoBuild())
                            .IiDoneness(details.getIiDoneness())
                            .IiSidings(details.getIiSidings())
                            .IiRemarks(details.getIiRemarks())
                            .IiServiceCharge(details.getIiServiceCharge())
                            .IiNonInventoriable(details.getIiNonInventoriable())
                            .IiServices(details.getIiServices())
                            .IiJobServices(details.getIiJobServices())
                            .IiIsVatRelief(details.getIiIsVatRelief())
                            .IiIsTax(details.getIiIsTax())
                            .IiIsProject(details.getIiIsProject())
                            .IiPercentMarkup(details.getIiPercentMarkup())
                            .IiShippingCost(details.getIiShippingCost())
                            .IiSpecificGravity(details.getIiSpecificGravity())
                            .IiStandardFillSize(details.getIiStandardFillSize())
                            .IiYeild(details.getIiYield())
                            .IiLossPercentage(details.getIiLossPercentage())
                            .IiMarkupValue(details.getIiMarkupValue())
                            .IiMarket(details.getIiMarket())
                            .IiEnablePo(details.getIiEnablePo())
                            .IiPoCycle(details.getIiPoCycle())
                            .IiUmcPackaging(details.getIiUmcPackaging())
                            .IiRetailUom(invUnitOfMeasure.getUomCode())
                            .IiOpenProduct(details.getIiOpenProduct())
                            .IiFixedAsset(details.getIiFixedAsset())
                            .IiDateAcquired(details.getIiDateAcquired())
                            .IiDefaultLocation(invLocation.getLocCode())
                            .IiTaxCode(details.getIiTaxCode())

                            .IiScSunday(details.getIiScSunday())
                            .IiScMonday(details.getIiScMonday())
                            .IiScTuesday(details.getIiScTuesday())
                            .IiScWednesday(details.getIiScWednesday())
                            .IiScThursday(details.getIiScThursday())
                            .IiScFriday(details.getIiScFriday())
                            .IiScSaturday(details.getIiScSaturday())

                            .IiAdCompany(companyCode)

                            .buildItem();

                    //Add UOM and UMC
                    invItem.setInvUnitOfMeasure(invUnitOfMeasure);
                    this.addInvUmcEntry(invItem, invUnitOfMeasure.getUomName(), invUnitOfMeasure.getUomAdLvClass(),
                            invUnitOfMeasure.getUomConversionFactor(), invUnitOfMeasure.getUomBaseUnit(), companyCode);

                    LocalInvItem prevItem = (LocalInvItem) invItemHome.findEnabledIiByIiAdLvCategory(invItem.getIiAdLvCategory(), companyCode).toArray()[0];
                    LocalInvItemLocation prevIl = (LocalInvItemLocation) prevItem.getInvItemLocations().toArray()[0];

                    LocalInvItemLocation invItemLocation = invItemLocationHome
                            .IlReorderPoint(prevIl.getIlReorderPoint())
                            .IlReorderQuantity(prevIl.getIlReorderQuantity())
                            .IlReorderLevel(prevIl.getIlReorderLevel())
                            .IlGlCoaSalesAccount(prevIl.getIlGlCoaSalesAccount())
                            .IlGlCoaInventoryAccount(prevIl.getIlGlCoaInventoryAccount())
                            .IlGlCoaCostOfSalesAccount(prevIl.getIlGlCoaCostOfSalesAccount())
                            .IlGlCoaWipAccount(prevIl.getIlGlCoaWipAccount())
                            .IlGlCoaAccruedInventoryAccount(prevIl.getIlGlCoaAccruedInventoryAccount())
                            .IlGlCoaSalesReturnAccount(prevIl.getIlGlCoaSalesReturnAccount())
                            .IlSubjectToCommission(prevIl.getIlSubjectToCommission())
                            .IlAdCompany(companyCode)
                            .buildItemLocation();

                    invItemLocation.setInvItem(invItem);
                    invItemLocation.setInvLocation(invLocation);

                    for (LocalAdBranch adBranch : adBranchHome.findBrAll(companyCode)) {

                        LocalAdBranchItemLocation adBranchItemLocation = adBranchItemLocationHome
                                .BilReorderPoint(prevIl.getIlReorderPoint())
                                .BilReorderQuantity(prevIl.getIlReorderQuantity())
                                .BilGlCoaSalesAccount(prevIl.getIlGlCoaSalesAccount())
                                .BilGlCoaInventoryAccount(prevIl.getIlGlCoaInventoryAccount())
                                .BilGlCoaCostOfSalesAccount(prevIl.getIlGlCoaCostOfSalesAccount())
                                .BilGlCoaWipAccount(prevIl.getIlGlCoaWipAccount())
                                .BilGlCoaAccruedInventoryAccount(prevIl.getIlGlCoaAccruedInventoryAccount())
                                .BilGlCoaSalesReturnAccount(prevIl.getIlGlCoaSalesReturnAccount())
                                .BilItemDownloadStatus('D')
                                .BilLocationDownloadStatus('D')
                                .BilItemLocationDownloadStatus('D')
                                .BilAdCompany(companyCode)
                                .buildBranchItemLocation();

                        adBranchItemLocation.setInvItemLocation(invItemLocation);
                        adBranchItemLocation.setAdBranch(adBranch);
                    }

                    results[i] = itemRowEncode(invItem, 0, invLocation.getLocLvType(), EJBCommon.FALSE,
                            0, 0, "NA", invItem.getInvUnitOfMeasure().getUomName());

                }
                catch (Exception e) {
                    e.printStackTrace();
                    ctx.setRollbackOnly();
                    return null;
                }
            }
            return results;

        }
        catch (Exception ex) {
            ex.printStackTrace();
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public String[] getAllPostedArSoMatchedInvoice(String branchCodeName, Integer companyCode) {

        Debug.print("InvItemSyncControllerBean getAllPostedArSoMatchedInvoice");

        try {

            Integer AD_BRNCH;
            String dateFrom;
            String dateTo;

            //Parse Date
            dateFrom = branchCodeName.substring(0, branchCodeName.indexOf("$"));
            branchCodeName = branchCodeName.substring(dateFrom.length() + 1);
            dateTo = branchCodeName.substring(0, branchCodeName.indexOf("$"));
            branchCodeName = branchCodeName.substring(dateTo.length() + 1);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);

            Object obj[] = new Object[2];
            obj[0] = sdf.parse(dateFrom);
            obj[1] = sdf.parse(dateTo);

            LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(branchCodeName, companyCode);
            AD_BRNCH = adBranch.getBrCode();

            String iliSql = "SELECT OBJECT(inv) FROM ArInvoice inv, IN(inv.arInvoiceLineItems) ili "
                    + "WHERE inv.invPosted = 1 AND inv.invVoid = 0 AND inv.invCreditMemo=0 AND inv.invDate>=?1 "
                    + "AND inv.invDate<=?2 AND inv.invAdBranch = " + AD_BRNCH + " AND inv.invAdCompany = " + companyCode;
            Collection arItemInvoices = arInvoiceHome.getInvByCriteria(iliSql, obj);

            String solSql = "SELECT OBJECT(inv) FROM ArInvoice inv, IN(inv.arSalesOrderInvoiceLines) sil "
                    + "WHERE inv.invPosted = 1 AND inv.invVoid = 0 AND inv.invCreditMemo=0 AND inv.invDate>=?1 "
                    + "AND inv.invDate<=?2 AND inv.invAdBranch = " + AD_BRNCH + " AND inv.invAdCompany = " + companyCode;

            Collection arSolInvoices = arInvoiceHome.getInvByCriteria(solSql, obj);
            String[] results = new String[(arItemInvoices.size() + arSolInvoices.size())];

            int ctr = 0;
            String currInvNumber = "";

            for (Object arItemInvoice : arItemInvoices) {
                LocalArInvoice arInvoice = (LocalArInvoice) arItemInvoice;
                if (currInvNumber.equals(arInvoice.getInvNumber())) {
                    continue;
                }

                currInvNumber = arInvoice.getInvNumber();
                results[ctr] = invRowEncode(arInvoice);
                ctr++;
            }

            for (Object arSolInvoice : arSolInvoices) {
                LocalArInvoice arInvoice = (LocalArInvoice) arSolInvoice;
                if (currInvNumber.equals(arInvoice.getInvNumber())) {
                    continue;
                }

                currInvNumber = arInvoice.getInvNumber();
                results[ctr] = invRowEncode(arInvoice);
                ctr++;
            }

            Debug.print("Sending : " + (ctr) + " Invoice(s).....");
            return results;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public int setInvAdjustment(String[] ADJ, String branchCodeName, Integer companyCode) {

        Debug.print("InvItemSyncControllerBean setInvAdjustment");

        Integer AD_BRNCH;
        try {
            LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(branchCodeName, companyCode);
            AD_BRNCH = adBranch.getBrCode();
        }
        catch (Exception e) {
            return 0;
        }

        for (String s : ADJ) {
            InvModAdjustmentDetails invModAdjustmentDetails = adjRowDecode(s);
            try {
                invModAdjustmentDetails.setAdjVoid(EJBCommon.FALSE);
                invModAdjustmentDetails.setAdjPosted(EJBCommon.FALSE);
                invModAdjustmentDetails.setAdjCode(null);
                invAdjustmentEntryController.saveInvAdjEntry(invModAdjustmentDetails,
                        invModAdjustmentDetails.getAdjCoaAccountNumber(), invModAdjustmentDetails.getAdjAlList(),
                        true, AD_BRNCH, companyCode);
            }
            catch (Exception e) {
                e.printStackTrace();
                ctx.setRollbackOnly();
                return 0;
            }
        }
        return 1;
    }

    @Override
    public int setArBillingInvoiceAndCreditMemos(String[] cmTxn, String[] invTxn, String branchCodeName, Integer companyCode) {

        Debug.print("InvItemSyncControllerBean setArMemoLineReceiptAndCreditMemos");

        Integer AD_BRNCH;
        try {
            LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(branchCodeName, companyCode);
            AD_BRNCH = adBranch.getBrCode();
        }
        catch (Exception e) {
            return 0;
        }

        HashMap<String, Object> arInvDrLineMap = new HashMap<>();
        for (String s : cmTxn) {
            ArModInvoiceDetails arModInvoiceDetails = cmRowDecode(s);
            try {
                if (arCreditMemoEntryController.saveArCmInvEntry(arModInvoiceDetails, arModInvoiceDetails.getInvCstCustomerCode(),
                        arModInvoiceDetails.getInvIbName(), AD_BRNCH, companyCode) != 1) {
                    throw new Exception("UPLOAD FAILED");
                } else {

                    if (arInvDrLineMap.containsKey(arModInvoiceDetails.getInvReferenceNumber())) {
                        HashMap<String, Object> arMemoLineAmountMap = (HashMap<String, Object>) arInvDrLineMap.get(arModInvoiceDetails.getInvReferenceNumber());
                        arInvDrLineMap.remove(arModInvoiceDetails.getInvReferenceNumber());
                        arInvDrLineMap.put(arModInvoiceDetails.getInvReferenceNumber(),
                                getArMemoLineAmount(arMemoLineAmountMap, arModInvoiceDetails.getInvCmInvoiceNumber(), AD_BRNCH, companyCode));
                    } else {

                        arInvDrLineMap.put(arModInvoiceDetails.getInvReferenceNumber(),
                                getArMemoLineAmount(new HashMap<>(), arModInvoiceDetails.getInvCmInvoiceNumber(), AD_BRNCH, companyCode));
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                ctx.setRollbackOnly();
                return 0;
            }
        }

        for (String s : invTxn) {
            ArModInvoiceDetails arInvoiceDetails = scRowDecode(s);
            try {
                HashMap<String, Object> arMemoLineAmountMap = (HashMap<String, Object>) arInvDrLineMap.get(arInvoiceDetails.getInvNumber());
                double drTotalAmount = getDRAmount(arMemoLineAmountMap);
                if (arInvoiceEntryController.saveArInvEntry(arInvoiceDetails, arInvoiceDetails.getInvTcName(),
                        Integer.parseInt(arInvoiceDetails.getInvCstCustomerCode()), drTotalAmount, AD_BRNCH, companyCode) != 1) {
                    throw new Exception("UPLOAD FAILED");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                ctx.setRollbackOnly();
                return 0;
            }
        }
        return 1;
    }

    @Override
    public String[] convertBulkToLooseUom(Double qty, String II_NM, String BULK_UOM_NM, String LOOSE_UOM_NM, Integer companyCode) {

        Debug.print("InvItemSyncControllerBean convertBulkToLooseUom");

        String[] results = new String[1];

        try {

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = null;
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = null;

            LocalInvItem invItem = invItemHome.findByIiName(II_NM, companyCode);
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            if (BULK_UOM_NM.equals("")) {
                Iterator i = invItem.getInvUnitOfMeasureConversions().iterator();
                while (i.hasNext()) {
                    LocalInvUnitOfMeasureConversion cnv = (LocalInvUnitOfMeasureConversion) i.next();
                    if (cnv.getUmcBaseUnit() == (byte) 1) {
                        invUnitOfMeasureConversion = invUnitOfMeasureConversionHome
                                .findUmcByIiNameAndUomName(invItem.getIiName(), cnv.getInvUnitOfMeasure().getUomName(), companyCode);
                        BULK_UOM_NM = cnv.getInvUnitOfMeasure().getUomName();
                        break;
                    }
                }
            }
            if (LOOSE_UOM_NM.equals("")) {
                invDefaultUomConversion = invUnitOfMeasureConversionHome
                        .findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), companyCode);
            }

            Double convertedQty = EJBCommon.roundIt(
                    qty * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(),
                    adPreference.getPrfInvQuantityPrecisionUnit());
            results[0] = convertedQty.toString();

        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
        return results;
    }

    @Override
    public int setApReceivingItem(String[] RR, String branchCodeName, Integer companyCode) {

        Debug.print("InvItemSyncControllerBean setApReceivingItem");

        Integer AD_BRNCH;
        try {
            LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(branchCodeName, companyCode);
            AD_BRNCH = adBranch.getBrCode();
        }
        catch (Exception e) {
            return 0;
        }

        for (String s : RR) {
            ApModPurchaseOrderDetails apModPurchaseOrderDetails = poRowDecode(s);
            try {
                apModPurchaseOrderDetails.setPoVoid(EJBCommon.FALSE);
                apModPurchaseOrderDetails.setPoPosted(EJBCommon.FALSE);
                apModPurchaseOrderDetails.setPoTcType("ITEMS");
                apModPurchaseOrderDetails.setPoCode(null);

                apPurchaseOrderEntryController.saveApPoEntry(apModPurchaseOrderDetails, apModPurchaseOrderDetails.getPoPytName(),
                        apModPurchaseOrderDetails.getPoTcName(), apModPurchaseOrderDetails.getPoFcName(),
                        apModPurchaseOrderDetails.getPoSplSupplierCode(), apModPurchaseOrderDetails.getPoPlList(), true,
                        AD_BRNCH, companyCode);

            }
            catch (Exception e) {
                e.printStackTrace();
                ctx.setRollbackOnly();
                return 0;
            }
        }

        return 1;
    }

    @Override
    public int setApReceivingItemPoCondition(String[] RR, String branchCodeName, Integer companyCode) {

        Debug.print("InvItemSyncControllerBean setApReceivingItemPoCondition");

        Integer AD_BRNCH;
        try {
            LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(branchCodeName, companyCode);
            AD_BRNCH = adBranch.getBrCode();
        }
        catch (Exception e) {
            return 0;
        }

        for (String s : RR) {
            ApModPurchaseOrderDetails apModPurchaseOrderDetails = poRowDecode(s);
            try {

                apModPurchaseOrderDetails.setPoVoid(EJBCommon.FALSE);
                apModPurchaseOrderDetails.setPoPosted(EJBCommon.FALSE);
                apModPurchaseOrderDetails.setPoTcType("ITEMS");
                apModPurchaseOrderDetails.setPoCode(null);

                apPurchaseOrderEntryController.saveApPoEntry2(apModPurchaseOrderDetails, apModPurchaseOrderDetails.getPoPytName(),
                        apModPurchaseOrderDetails.getPoTcName(), apModPurchaseOrderDetails.getPoFcName(), apModPurchaseOrderDetails.getPoSplSupplierCode(),
                        apModPurchaseOrderDetails.getPoPlList(), true, AD_BRNCH, companyCode);

            }
            catch (Exception e) {
                e.printStackTrace();
                ctx.setRollbackOnly();
                return 0;
            }
        }
        return 1;
    }

    @Override
    public int setApPurchaseOrder(String[] PO, Boolean isReceiving, String branchCodeName, Integer companyCode) {

        Debug.print("InvItemSyncControllerBean setApPurchaseOrder");

        Integer AD_BRNCH;
        try {
            LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(branchCodeName, companyCode);
            AD_BRNCH = adBranch.getBrCode();
        }
        catch (Exception e) {
            return 0;
        }

        for (String s : PO) {

            ApModPurchaseOrderDetails apModPurchaseOrderDetails = poRowDecode(s);
            try {

                apModPurchaseOrderDetails.setPoVoid(EJBCommon.FALSE);
                apModPurchaseOrderDetails.setPoPosted(EJBCommon.FALSE);
                apModPurchaseOrderDetails.setPoTcType("ITEMS");
                apModPurchaseOrderDetails.setPoCode(null);
                System.out.println(apModPurchaseOrderDetails.getPoSplSupplierCode());
                if (isReceiving) {
                    apPurchaseOrderEntryController.saveApPoEntry(apModPurchaseOrderDetails, apModPurchaseOrderDetails.getPoPytName(),
                            apModPurchaseOrderDetails.getPoTcName(), apModPurchaseOrderDetails.getPoFcName(), apModPurchaseOrderDetails.getPoSplSupplierCode(),
                            apModPurchaseOrderDetails.getPoPlList(), true,
                            AD_BRNCH, companyCode);
                } else {
                    apPurchaseOrderEntryController.saveApPoSyncEntry(apModPurchaseOrderDetails, apModPurchaseOrderDetails.getPoPytName(),
                            apModPurchaseOrderDetails.getPoTcName(), apModPurchaseOrderDetails.getPoFcName(), apModPurchaseOrderDetails.getPoSplSupplierCode(),
                            apModPurchaseOrderDetails.getPoPlList(), true,
                            AD_BRNCH, companyCode);
                }

            }
            catch (Exception e) {
                e.printStackTrace();
                ctx.setRollbackOnly();
                return 0;
            }
        }

        return 1;
    }

    @Override
    public String setInvBST(String[] BST, String branchCodeName, Integer companyCode) {

        Debug.print("InvItemSyncControllerBean setInvBST");

        Integer AD_BRNCH;
        LocalAdBranch adBranch;
        try {

            adBranch = adBranchHome.findByBrBranchCode(branchCodeName, companyCode);
            AD_BRNCH = adBranch.getBrCode();

        }
        catch (Exception e) {
            return "$ERROR[Branch Code Not Found: " + branchCodeName + "]$";
        }

        String returnString = "";

        for (String s : BST) {

            try {

                // Insert Initial Error
                returnString = "Parse Error";
                InvModBranchStockTransferDetails invModBranchStockTransferDetails = bstRowDecode(s);

                returnString = invModBranchStockTransferDetails.getBstNumber();

                invModBranchStockTransferDetails.setBstVoid(EJBCommon.FALSE);
                invModBranchStockTransferDetails.setBstPosted(EJBCommon.FALSE);


                if (invModBranchStockTransferDetails.getBstType().equals("IN")) {

                    String branchFromName = "";
                    try {
                        branchFromName = adBranchHome.findByBrBranchCode(invModBranchStockTransferDetails.getBstBranchFrom(), companyCode).getBrName();
                    }
                    catch (Exception ex) {
                        throw ex;
                    }

                    invModBranchStockTransferDetails.setBstBranchFrom(branchFromName);
                    invModBranchStockTransferDetails.setBstBranchTo(adBranch.getBrName());

                    invBranchStockTransferInEntryController.saveInvBstInEntry(invModBranchStockTransferDetails, invModBranchStockTransferDetails.getBstBtlList(),
                            invModBranchStockTransferDetails.getBstType(), AD_BRNCH, companyCode);

                    returnString = "";
                } else if (invModBranchStockTransferDetails.getBstType().equals("OUT")) {

                    String branchFromName = "";
                    Integer branchFromCode;
                    try {
                        LocalAdBranch brFrom = adBranchHome.findByBrBranchCode(invModBranchStockTransferDetails.getBstBranchFrom(), companyCode);
                        branchFromName = brFrom.getBrName();
                        branchFromCode = brFrom.getBrCode();
                    }
                    catch (Exception ex) {
                        throw ex;
                    }

                    invModBranchStockTransferDetails.setBstBranchFrom(adBranch.getBrName());
                    invModBranchStockTransferDetails.setBstBranchTo(branchFromName);

                    invBranchStockTransferInEntryController.saveInvBstInEntry(invModBranchStockTransferDetails, invModBranchStockTransferDetails.getBstBtlList(),
                            invModBranchStockTransferDetails.getBstType(), branchFromCode, companyCode);

                    returnString = "";
                }

            }
            catch (Exception e) {
                e.printStackTrace();
                ctx.setRollbackOnly();
                break;
            }
        }

        if (returnString.equals("")) {

            returnString = "$SUCCESS$";

        } else {
            returnString = "$" + returnString + "$";
        }

        return returnString;
    }

    @Override
    public String[] getInvBranchStockTransferAllIncoming(String branchCodeName, Integer companyCode) {

        Debug.print("InvItemSyncControllerBean getInvBranchStockTransferAllIncoming");

        ArrayList AllIncomingBstList = new ArrayList();

        Integer AD_BRNCH;
        try {
            LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(branchCodeName, companyCode);
            AD_BRNCH = adBranch.getBrCode();
        }
        catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        try {

            Collection invBranchStockTransfers = invBranchStockTransferHome.findPostedIncomingBstByAdBranchAndBstAdCompany(AD_BRNCH, companyCode);
            Iterator i = invBranchStockTransfers.iterator();

            int ctr = 1, size = invBranchStockTransfers.size(), all = 0;

            while (i.hasNext()) {
                LocalInvBranchStockTransfer invBranchStockTransfer = (LocalInvBranchStockTransfer) i.next();
                try {
                    LocalInvBranchStockTransfer incomingBST = invBranchStockTransferHome
                            .findInByBstOutNumberAndBrCode(invBranchStockTransfer.getBstNumber(), AD_BRNCH, companyCode);
                    all++;
                }
                catch (Exception ex) {
                    AllIncomingBstList.add(invBranchStockTransfer);
                }

                Debug.print("Status: Processing " + (ctr++) + "/" + size);

            }
            Debug.print("Sending " + all + "/" + invBranchStockTransfers.size());
        }
        catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        String[] results = new String[AllIncomingBstList.size()];
        Iterator bstIter = AllIncomingBstList.iterator();
        int ctr = 0;

        while (bstIter.hasNext()) {
            LocalInvBranchStockTransfer invBranchStockTransfer = (LocalInvBranchStockTransfer) bstIter.next();
            ;
            results[ctr++] = bstRowEncode(invBranchStockTransfer);
        }

        return results;
    }

    @Override
    public String[] getInvStockOnHand(String branchCodeName, String itemLocation, Integer companyCode) {

        Debug.print("InvItemSyncControllerBean getInvStockOnHand");

        Integer AD_BRNCH;
        LocalAdCompany adCompany;

        try {
            LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(branchCodeName, companyCode);
            AD_BRNCH = adBranch.getBrCode();

            adCompany = adCompanyHome.findByPrimaryKey(companyCode);

        }
        catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        Collection invStockOnHands;
        String[] results;
        Date currentDate = new Date();

        try {
            invStockOnHands = adBranchItemLocationHome.findEnabledIiByIiNewAndUpdated(AD_BRNCH, itemLocation, companyCode, 'U', 'D', 'X');
            results = new String[invStockOnHands.size()];
            Iterator i = invStockOnHands.iterator();
            int ctr = 0;
            while (i.hasNext()) {
                LocalAdBranchItemLocation invStockOnHand = (LocalAdBranchItemLocation) i.next();
                char separator = EJBCommon.SEPARATOR;
                StringBuffer encodedResult = new StringBuffer();

                try {
                    LocalInvCosting invStockOnHandCost =
                            invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                    currentDate, invStockOnHand.getInvItemLocation().getInvItem().getIiName(), invStockOnHand.getInvItemLocation().getInvLocation().getLocName(),
                                    invStockOnHand.getAdBranch().getBrCode(), companyCode);

                    // Start Separator
                    encodedResult.append(separator);

                    // Qty
                    encodedResult.append(invStockOnHandCost.getCstRemainingQuantity());
                    encodedResult.append(separator);

                    // Value
                    encodedResult.append(invStockOnHandCost.getCstRemainingValue());
                    encodedResult.append(separator);

                    // Average Cost
                    encodedResult.append(invStockOnHandCost.getCstRemainingQuantity() == 0 ? 0 :
                            EJBCommon.roundIt(invStockOnHandCost.getCstRemainingValue() / invStockOnHandCost.getCstRemainingQuantity(), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                    encodedResult.append(separator);

                    // Item
                    encodedResult.append(invStockOnHand.getInvItemLocation().getInvItem().getIiCode());
                    encodedResult.append(separator);

                    // Location
                    encodedResult.append(invStockOnHand.getInvItemLocation().getInvLocation().getLocCode());
                    encodedResult.append(separator);

                }
                catch (FinderException ex) {
                    encodedResult = new StringBuffer();

                    // Start Separator
                    encodedResult.append(separator);

                    // Qty
                    encodedResult.append(0);
                    encodedResult.append(separator);

                    // Value
                    encodedResult.append(0);
                    encodedResult.append(separator);

                    // Average Cost
                    encodedResult.append(0);
                    encodedResult.append(separator);

                    // Item
                    encodedResult.append(invStockOnHand.getInvItemLocation().getInvItem().getIiCode());
                    encodedResult.append(separator);

                    // Location
                    encodedResult.append(invStockOnHand.getInvItemLocation().getInvLocation().getLocCode());
                    encodedResult.append(separator);
                }

                results[ctr++] = encodedResult.toString();
                encodedResult = new StringBuffer();

            }
        }
        catch (FinderException ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

        return results;
    }

    @Override
    public String[] getInvStockOnHandWithExpiryDate(String branchCodeName, String itemLocation, Integer companyCode) {

        Debug.print("InvItemSyncControllerBean getInvStockOnHandWithExpiryDate");

        Integer AD_BRNCH;
        LocalAdCompany adCompany = null;

        try {
            LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(branchCodeName, companyCode);
            AD_BRNCH = adBranch.getBrCode();

            adCompany = adCompanyHome.findByPrimaryKey(companyCode);

        }
        catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        Collection invStockOnHands = null;
        String[] results = null;
        Date currentDate = new Date();

        try {

            invStockOnHands = adBranchItemLocationHome.findEnabledIiByIiNewAndUpdated(AD_BRNCH, itemLocation, companyCode, 'U', 'D', 'X');

            results = new String[invStockOnHands.size()];

            Iterator i = invStockOnHands.iterator();

            int ctr = 0;

            while (i.hasNext()) {

                LocalAdBranchItemLocation invStockOnHand = (LocalAdBranchItemLocation) i.next();

                char separator = EJBCommon.SEPARATOR;
                StringBuffer encodedResult = new StringBuffer();

                try {
                    LocalInvCosting invStockOnHandCost =
                            invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                    currentDate, invStockOnHand.getInvItemLocation().getInvItem().getIiName(), invStockOnHand.getInvItemLocation().getInvLocation().getLocName(),
                                    invStockOnHand.getAdBranch().getBrCode(), companyCode);

                    // Start Separator
                    encodedResult.append(separator);

                    // Qty
                    encodedResult.append(invStockOnHandCost.getCstRemainingQuantity());
                    encodedResult.append(separator);

                    // Value
                    encodedResult.append(invStockOnHandCost.getCstRemainingValue());
                    encodedResult.append(separator);

                    // Average Cost
                    encodedResult.append(invStockOnHandCost.getCstRemainingQuantity() == 0 ? 0 :
                            EJBCommon.roundIt(invStockOnHandCost.getCstRemainingValue() / invStockOnHandCost.getCstRemainingQuantity(), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                    encodedResult.append(separator);

                    // Item
                    encodedResult.append(invStockOnHand.getInvItemLocation().getInvItem().getIiCode());
                    encodedResult.append(separator);

                    // Location
                    encodedResult.append(invStockOnHand.getInvItemLocation().getInvLocation().getLocCode());
                    encodedResult.append(separator);

                    // Expiry Date
                    encodedResult.append("~");
                    encodedResult.append(invStockOnHandCost.getCstExpiryDate());
                    System.out.println("(invStockOnHandCost.getCstExpiryDate(): " + invStockOnHandCost.getCstExpiryDate());
                    encodedResult.append("~");

                }
                catch (FinderException ex) {
                    encodedResult = new StringBuffer();

                    // Start Separator
                    encodedResult.append(separator);

                    // Qty
                    encodedResult.append(0);
                    encodedResult.append(separator);

                    // Value
                    encodedResult.append(0);
                    encodedResult.append(separator);

                    // Average Cost
                    encodedResult.append(0);
                    encodedResult.append(separator);

                    // Item
                    encodedResult.append(invStockOnHand.getInvItemLocation().getInvItem().getIiCode());
                    encodedResult.append(separator);

                    // Location
                    encodedResult.append(invStockOnHand.getInvItemLocation().getInvLocation().getLocCode());
                    encodedResult.append(separator);

                    // Expiry Date
                    encodedResult.append("~");
                    encodedResult.append("~");
                }

                results[ctr++] = encodedResult.toString();
                encodedResult = new StringBuffer();

            }


        }
        catch (FinderException ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

        return results;
    }

    @Override
    public String[] getInvStockOnHandOnly(String[] invUploadOrig, Integer companyCode) {

        Debug.print("InvItemSyncControllerBean getInvStockOnHandOnly");

        String[] results = null;
        int ctr = 0;
        char separator = EJBCommon.SEPARATOR;
        int lengthOriginal = invUploadOrig.length;
        results = new String[lengthOriginal];

        for (int i = 0; i < lengthOriginal; i++) {
            // Remove first $ character
            String invUpload = invUploadOrig[i].substring(1);

            // Item
            int start = 0;
            int nextIndex = invUpload.indexOf(separator, start);
            int length = nextIndex - start;
            String Item = (invUpload.substring(start, start + length));

            // Qty
            start = nextIndex + 1;
            nextIndex = invUpload.indexOf(separator, start);
            length = nextIndex - start;
            double qty = Double.parseDouble((invUpload.substring(start, start + length)));

            // Date
            start = nextIndex + 1;
            nextIndex = invUpload.indexOf(separator, start);
            length = nextIndex - start;
            String datetxn = (invUpload.substring(start, start + length));
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date dateTransact = null;
            try {
                dateTransact = sdf.parse(datetxn);
                System.out.println("dateTransact = " + sdf.format(dateTransact));
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            // Location
            start = nextIndex + 1;
            nextIndex = invUpload.indexOf(separator, start);
            length = nextIndex - start;
            String location = (invUpload.substring(start, start + length));

            // Branch
            start = nextIndex + 1;
            nextIndex = invUpload.indexOf(separator, start);
            length = nextIndex - start;
            String branch = (invUpload.substring(start, start + length));

            // Open Bottle
            start = nextIndex + 1;
            nextIndex = invUpload.indexOf(separator, start);
            length = nextIndex - start;
            String openBottle = (invUpload.substring(start, start + length));

            // Orig Qty
            start = nextIndex + 1;
            nextIndex = invUpload.indexOf(separator, start);
            length = nextIndex - start;
            double origQty = Double.parseDouble((invUpload.substring(start, start + length)));

            Integer AD_BRNCH;
            LocalAdCompany adCompany;

            try {
                LocalAdBranch adBranch = adBranchHome.findByBrName(branch, companyCode);
                AD_BRNCH = adBranch.getBrCode();
                adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            }
            catch (Exception ex) {
                throw new EJBException(ex.getMessage());
            }
            StringBuffer encodedResult = new StringBuffer();
            StringBuffer encodedResult2 = new StringBuffer();
            try {
                LocalInvCosting invStockOnHandCost =
                        invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                dateTransact, Item, location, AD_BRNCH, companyCode);
                double qtyNew = 0d;
                double qtyNew2 = 0d;

                if (origQty != qty && invStockOnHandCost.getCstRemainingQuantity() > origQty) {
                    qty = origQty;
                }
                System.out.println("qty: " + qty);
                qtyNew2 = qty;
                if (openBottle == "T") {
                    qty = qty * -1;
                }
                if (invStockOnHandCost.getCstRemainingQuantity() < 0 && invStockOnHandCost.getCstRemainingQuantity() < qty) {
                    System.out.println("CHECKPOINT A");
                    qtyNew = ((invStockOnHandCost.getCstRemainingQuantity() - qty) * -1) + invStockOnHandCost.getCstRemainingQuantity();
                } else if (invStockOnHandCost.getCstRemainingQuantity() >= 0 && invStockOnHandCost.getCstRemainingQuantity() < qty) {
                    System.out.println("CHECKPOINT B");
                    qtyNew = qty - invStockOnHandCost.getCstRemainingQuantity();
                }
                System.out.println("qtyNew: " + qtyNew);

                if (openBottle.equals("T")) {
                    if (qtyNew != 0) {
                        qtyNew = qtyNew * -1;
                    } else {
                        qtyNew = qtyNew2;
                    }
                }
                System.out.println("qty: " + qty);
                System.out.println("qtyNew B4 CHeck C Before: " + qtyNew);
                if (openBottle.equals("V") && invStockOnHandCost.getCstRemainingQuantity() < qtyNew) {
                    qtyNew = qtyNew2;
                }
                System.out.println("qtyNew B4 CHeck C: " + qtyNew);
                if (qtyNew != 0) {
                    System.out.println("CHECKPOINT C");
                    // Start Separator
                    encodedResult.append(separator);

                    // Qty
                    encodedResult.append(qtyNew);
                    encodedResult.append(separator);

                    // Item
                    encodedResult.append(Item);
                    encodedResult.append(separator);

                    // Date
                    encodedResult.append(datetxn);
                    encodedResult.append(separator);

                    // Location
                    encodedResult.append(location);
                    encodedResult.append(separator);

                    // Branch
                    encodedResult.append(branch);
                    encodedResult.append(separator);

                    // Value
                    encodedResult.append(invStockOnHandCost.getCstRemainingValue());
                    encodedResult.append(separator);

                    // Average Cost
                    encodedResult.append(invStockOnHandCost.getCstRemainingQuantity() == 0 ? 0 :
                            EJBCommon.roundIt(invStockOnHandCost.getCstRemainingValue() / invStockOnHandCost.getCstRemainingQuantity(), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                    encodedResult.append(separator);

                    // Unit Cost
                    encodedResult.append(invStockOnHandCost.getInvItemLocation().getInvItem().getIiUnitCost());
                    encodedResult.append(separator);

                    // UOM
                    encodedResult.append(invStockOnHandCost.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomShortName());
                    encodedResult.append(separator);

                    System.out.println(ctr + " CHECKPOINT D: " + encodedResult.toString());
                    results[ctr++] = encodedResult.toString();
                    encodedResult = new StringBuffer();
                    System.out.println("******************BREAK********************");
                }


                // Expiry Date
				/*encodedResult.append("~");
					encodedResult.append(invStockOnHandCost.getCstExpiryDate());
					System.out.println("(invStockOnHandCost.getCstExpiryDate(): " + invStockOnHandCost.getCstExpiryDate());
					encodedResult.append("~");
				 */
            }
            catch (FinderException ex) {
                Debug.printStackTrace(ex);
                throw new EJBException(ex.getMessage());
            }


        }
        System.out.println(results.length);
        return results;
    }

    @Override
    public String[] getLastPo(String invoiceDate, String location, String[] invUploadOrig, String branchCode, Integer companyCode) {

        Debug.print("InvItemSyncControllerBean getLastPo");

        String[] results = null;
        int ctr = 0;
        char separator = EJBCommon.SEPARATOR;
        int lengthOriginal = invUploadOrig.length;
        results = new String[lengthOriginal];
        double unitCost = 0;
        String Item = "";
        double qty = 0;
        double qty2 = 0;
        int branch = 0;

        LocalAdCompany adCompany = null;

        for (String s : invUploadOrig) {
            // Remove first $ character
            String invUpload = s.substring(1);

            // Item
            int start = 0;
            int nextIndex = invUpload.indexOf(separator, start);
            int length = nextIndex - start;
            Item = (invUpload.substring(start, start + length));

            // Qty
            start = nextIndex + 1;
            nextIndex = invUpload.indexOf(separator, start);
            length = nextIndex - start;
            qty = Double.parseDouble((invUpload.substring(start, start + length)));

            try {
                LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(branchCode, companyCode);
                branch = adBranch.getBrCode();
                adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            }
            catch (Exception ex) {
                throw new EJBException(ex.getMessage());
            }
            Collection invStockOnHands = null;

            Date currentDate = new Date();

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            sdf.setLenient(false);
            Date datetxn = null;
            try {
                datetxn = sdf.parse(invoiceDate);
                System.out.println("Date-" + datetxn);
            }
            catch (Exception ex) {
                System.out.println("Error On PO Date: " + ex.getMessage());
                //throw ex;
            }

            //char separator2 = EJBCommon.SEPARATOR;
			/*StringBuffer encodedResult = new StringBuffer();
			StringBuffer encodedResult2 = new StringBuffer();*/
            try {
                try {
                    LocalApPurchaseOrderLine apPurchaseOrderLine =
                            apPurchaseOrderLineHome.getByMaxPoDateAndMaxPlCodeAndIiNameAndLocNameAndPoReceivingAndPoPostedAndLessThanEqualPoDate(
                                    Item, location, EJBCommon.FALSE, EJBCommon.TRUE, datetxn, branch, companyCode);

                    unitCost = unitCost + (apPurchaseOrderLine.getPlUnitCost() * qty);
                }
                catch (Exception e) {
                    LocalInvItem invItem = invItemHome.findByIiName(Item.trim(), companyCode);
                    unitCost = unitCost + (invItem.getIiUnitCost() * qty);
                }
                qty2 = qty2 + qty;
            }
            catch (FinderException ex) {
                Debug.printStackTrace(ex);
                throw new EJBException(ex.getMessage() + ". ITEM NOT FOUND: " + Item);
            }
        }

        StringBuilder encodedResult = new StringBuilder();

        // Start Separator
        encodedResult.append(separator);

        // Date
        encodedResult.append(invoiceDate);
        encodedResult.append(separator);

        // Qty
        encodedResult.append(qty2);
        encodedResult.append(separator);

        // Unit Cost
        encodedResult.append(unitCost);
        encodedResult.append(separator);

        results[ctr++] = encodedResult.toString();

        return results;
    }

    @Override
    public String[] getLastPoPerItem(String invoiceDate, String location, String[] invUploadOrig, String branchCode, Integer companyCode) {

        Debug.print("InvItemSyncControllerBean getLastPoPerItem");

        String[] results = null;
        int ctr = 0;
        char separator = EJBCommon.SEPARATOR;
        int lengthOriginal = invUploadOrig.length;
        results = new String[lengthOriginal];
        double unitCost = 0;
        String Item = "";
        String ItemOutput = "";
        String ItemDesc = "";
        double qty = 0;
        double qty2 = 0;
        int branch = 0;

        for (String s : invUploadOrig) {
            // Remove first $ character
            String invUpload = s.substring(1);

            // Item
            int start = 0;
            int nextIndex = invUpload.indexOf(separator, start);
            int length = nextIndex - start;
            Item = (invUpload.substring(start, start + length));

            // Qty
            start = nextIndex + 1;
            nextIndex = invUpload.indexOf(separator, start);
            length = nextIndex - start;
            qty = Double.parseDouble((invUpload.substring(start, start + length)));

            LocalAdCompany adCompany = null;

            try {
                LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(branchCode, companyCode);
                branch = adBranch.getBrCode();
                adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            }
            catch (Exception ex) {
                throw new EJBException("Branch Not Found");
            }
            Collection invStockOnHands = null;

            Date currentDate = new Date();

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            sdf.setLenient(false);
            Date datetxn = null;
            try {
                datetxn = sdf.parse(invoiceDate);
                System.out.println("Date-" + datetxn);
            }
            catch (Exception ex) {
                System.out.println("Error On PO Date: " + ex.getMessage());
                //throw ex;
            }

            //char separator2 = EJBCommon.SEPARATOR;
			/*StringBuffer encodedResult = new StringBuffer();
			StringBuffer encodedResult2 = new StringBuffer();*/
            try {
                try {
                    LocalApPurchaseOrderLine apPurchaseOrderLine =
                            apPurchaseOrderLineHome.getByMaxPoDateAndMaxPlCodeAndIiNameAndLocNameAndPoReceivingAndPoPostedAndLessThanEqualPoDate(
                                    Item, location, EJBCommon.FALSE, EJBCommon.TRUE, datetxn, branch, companyCode);

                    unitCost = (apPurchaseOrderLine.getPlUnitCost());
                    ItemOutput = apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiName();
                    ItemDesc = apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiDescription();

                }
                catch (Exception e) {
                    System.out.println("CATCH: " + Item);
                    LocalInvItem invItem = invItemHome.findByIiName(Item.trim(), companyCode);

                    unitCost = (invItem.getIiUnitCost());
                    ItemOutput = invItem.getIiName();
                    ItemDesc = invItem.getIiDescription();
                }

                System.out.println("CHECKPOINT C");
                qty2 = qty2 + qty;


                StringBuffer encodedResult = new StringBuffer();
                // Start Separator
                encodedResult.append(separator);

                // Item Name
                encodedResult.append(ItemOutput);
                encodedResult.append(separator);

                // Item Description
                encodedResult.append(ItemDesc);
                encodedResult.append(separator);

                // Date
                encodedResult.append(invoiceDate);
                encodedResult.append(separator);

                // Qty
                encodedResult.append(qty);
                encodedResult.append(separator);
                System.out.println("***qty: " + qty);
                // Unit Cost
                encodedResult.append(unitCost);
                encodedResult.append(separator);

                System.out.println(ctr + " CHECKPOINT D: " + encodedResult.toString());
                results[ctr++] = encodedResult.toString();
                encodedResult = new StringBuffer();
                System.out.println("******************BREAK********************");

            }
            catch (FinderException ex) {
				/*results = new String[0];
				results[0]="Item not Found: " + Item;*/
                Debug.printStackTrace(ex);
                throw new EJBException("Item Not Found:" + Item);
            }


        }

        return results;
    }

    @Override
    public String getCheckInsufficientStocks(String invoiceDateFrom, String invoiceDateTo, String location,
                                             String user, String invAdjAccount, String transactionType,
                                             String branchCode, Integer companyCode) {

        Debug.print("InvItemSyncControllerBean getCheckInsufficientStocks");

        int count = 0;
        double insufficientFix = 0;
        LocalAdCompany adCompany = null;

        int branch = 0;
        try {
            LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(branchCode, companyCode);
            branch = adBranch.getBrCode();
            adCompany = adCompanyHome.findByPrimaryKey(companyCode);

        }
        catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        String[] insufficentItemsToFix = null;
        String results = null;
        //arInvoice = arInvoiceHome.findByPrimaryKey(details.getInvCode());
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date dateFrom = null;
            try {
                dateFrom = sdf.parse(invoiceDateFrom);
                System.out.println("dateFrom = " + sdf.format(dateFrom));
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            Date dateTo = null;
            try {
                dateTo = sdf.parse(invoiceDateTo);
                System.out.println("dateTransact = " + sdf.format(dateTo));
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("transactionType: " + transactionType);

            if (transactionType.equals("Issuance")) {
                System.out.println("Issuance");
                Collection invAdjustments = invAdjustmentHome.findUnpostedInvAdjByInvAdjDateRangeByBranch(dateFrom, dateTo, branch, companyCode);
                HashMap cstMap = null;
                Iterator i = invAdjustments.iterator();
                int runVar = 0;
                while (i.hasNext()) {
                    LocalInvAdjustment invAdjustment = (LocalInvAdjustment) i.next();

                    System.out.println("*************************CHECKING ISSUANCE INSUFFICIENT STOCKS******************************");

                    boolean hasInsufficientItems = false;
                    String insufficientItems = "";

                    Collection invAdjustmentLineItems = invAdjustment.getInvAdjustmentLines();

                    Iterator p = invAdjustmentLineItems.iterator();
                    int a = 0;
                    ArrayList insufficient = new ArrayList();
                    ArrayList insufficientQty = new ArrayList();
                    ArrayList insufficientUOM = new ArrayList();
                    ArrayList insufficientUnitCost = new ArrayList();
                    ArrayList itemLocation = new ArrayList();
                    runVar++;

                    if ((invAdjustment.getAdjDate().equals(dateTo) && dateFrom.equals(dateTo)) || runVar == 1) {
                        cstMap = new HashMap();
                    }
                    while (p.hasNext()) {

                        LocalInvAdjustmentLine invAdjustmentLineItem = (LocalInvAdjustmentLine) p.next();
                        String II_NM = invAdjustmentLineItem.getInvItemLocation().getInvItem().getIiName();
                        String LOC_NM = invAdjustmentLineItem.getInvItemLocation().getInvLocation().getLocName();
                        if (invAdjustmentLineItem.getAlAdjustQuantity() < 0) {
                            //double CURR_QTY = 0;
                            if (invAdjustmentLineItem.getInvItemLocation().getInvItem().getIiEnableAutoBuild() == EJBCommon.TRUE && invAdjustmentLineItem.getInvItemLocation().getInvItem().getIiClass().equals("Assembly")) {

                                Collection invBillOfMaterials = invAdjustmentLineItem.getInvItemLocation().getInvItem().getInvBillOfMaterials();

                                Iterator j = invBillOfMaterials.iterator();
                                Debug.print("ArInvoiceEntryControllerBean saveArInvIliEntry N01");
                                while (j.hasNext()) {
                                    double CURR_QTY = 0;
                                    LocalInvBillOfMaterial invBillOfMaterial = (LocalInvBillOfMaterial) j.next();

                                    LocalInvItemLocation invItemLocation = invItemLocationHome.findByLocNameAndIiName(invBillOfMaterial.getBomLocName(),
                                            invBillOfMaterial.getBomIiName(), companyCode);

                                    LocalInvCosting invBomCosting = null;

                                    double ILI_QTY = commonData.convertByUomFromAndItemAndQuantity(
                                            invAdjustmentLineItem.getInvUnitOfMeasure(),
                                            invAdjustmentLineItem.getInvItemLocation().getInvItem(),
                                            Math.abs(invAdjustmentLineItem.getAlAdjustQuantity()), companyCode);
                                    Debug.print("ArInvoiceEntryControllerBean saveArInvIliEntry N02");
                                    boolean isIlFound = false;


                                    if (cstMap.containsKey(invItemLocation.getIlCode().toString())) {

                                        isIlFound = true;
                                        CURR_QTY = ((Double) cstMap.get(invItemLocation.getIlCode().toString())).doubleValue();

                                    } else {
                                        try {

											/*invBomCosting  = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndRemainingQuantityNotEqualToZeroAndIiNameAndLocName(
																arInvoice.getInvDate(), invBillOfMaterial.getBomIiName(),
																invItemLocation.getInvLocation().getLocName(), AD_BRNCH, AD_CMPNY);
											 */
                                            invBomCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                                    invAdjustment.getAdjDate(), invBillOfMaterial.getBomIiName(),
                                                    invItemLocation.getInvLocation().getLocName(), branch, companyCode);
                                            CURR_QTY = invBomCosting.getCstRemainingQuantity();
                                        }
                                        catch (FinderException ex) {

                                        }
                                    }


                                    LocalInvItem bomItm = invItemHome.findByIiName(invBillOfMaterial.getBomIiName(), companyCode);


                                    double NEEDED_QTY = commonData.convertByUomFromAndItemAndQuantity(invBillOfMaterial.getInvUnitOfMeasure(), bomItm,
                                            invBillOfMaterial.getBomQuantityNeeded(), companyCode);
                                    Debug.print("ArInvoiceEntryControllerBean saveArInvIliEntry N03");


                                    try {
                                        if (invBomCosting != null) {
											/*
												CURR_QTY = this.convertByUomAndQuantity(invBomCosting.getInvItemLocation().getInvItem().getInvUnitOfMeasure(),
														invBomCosting.getInvItemLocation().getInvItem(),
														invBomCosting.getCstRemainingQuantity(), AD_CMPNY);*/

                                            CURR_QTY = commonData.convertByUomAndQuantity(
                                                    invAdjustmentLineItem.getInvUnitOfMeasure(),
                                                    invAdjustmentLineItem.getInvItemLocation().getInvItem(),
                                                    CURR_QTY, companyCode);
                                        }
                                    }
                                    catch (Exception e) {

                                    }


                                    if ((invBomCosting == null && isIlFound == false) || CURR_QTY == 0 || CURR_QTY < (NEEDED_QTY * ILI_QTY)) {


                                        a++;
                                        hasInsufficientItems = true;
                                        System.out.println("Doc Number: " + invAdjustment.getAdjDocumentNumber());
                                        System.out.println("Item: " + invAdjustmentLineItem.getInvItemLocation().getInvItem().getIiName());
                                        System.out.println("Adjust Qty: " + invAdjustmentLineItem.getAlAdjustQuantity());
                                        double remQty = 0;
                                        double unitCost = 0;
                                        try {
                                            remQty = CURR_QTY;
                                        }
                                        catch (Exception e) {
                                            remQty = 0;
                                        }
                                        System.out.println("Remaining Qty: " + remQty);
                                        //insufficientItems += invAdjustmentLineItem.getInvItemLocation().getInvItem().getIiName() + " ";
                                        insufficientFix = Math.abs(invAdjustmentLineItem.getAlAdjustQuantity()) - CURR_QTY;
                                        insufficient.add(invAdjustmentLineItem.getInvItemLocation().getInvItem().getIiName());
                                        insufficientQty.add(insufficientFix);
                                        insufficientUOM.add(invAdjustmentLineItem.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName());
                                        insufficientUnitCost.add(invAdjustmentLineItem.getAlUnitCost());
                                        itemLocation.add(invAdjustmentLineItem.getInvItemLocation().getInvLocation().getLocName());
                                        System.out.println("insufficientFix: " + Math.abs(insufficientFix));
                                        CURR_QTY = CURR_QTY + insufficientFix;
                                        insufficientItems += invAdjustmentLineItem.getInvItemLocation().getInvItem().getIiName()
                                                + "-" + invBillOfMaterial.getBomIiName() + ", ";
                                    }

                                    //CURR_QTY -= (NEEDED_QTY * ILI_QTY);
                                    CURR_QTY -= ILI_QTY;
                                    if (!isIlFound) {
                                        cstMap.remove(invItemLocation.getIlCode().toString());
                                    }

                                    cstMap.put(invItemLocation.getIlCode().toString(), CURR_QTY);

                                    Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry  L03");
                                }
                            } else {

                                LocalInvCosting invCosting = null;
                                double ILI_QTY = commonData.convertByUomAndQuantity(
                                        invAdjustmentLineItem.getInvUnitOfMeasure(),
                                        invAdjustmentLineItem.getInvItemLocation().getInvItem(),
                                        Math.abs(invAdjustmentLineItem.getAlAdjustQuantity()), companyCode);

                                double CURR_QTY = 0;
                                boolean isIlFound = false;

                                if (cstMap.containsKey(invAdjustmentLineItem.getInvItemLocation().getIlCode().toString())) {
                                    Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry  L04 - A");
                                    isIlFound = true;
                                    CURR_QTY = ((Double) cstMap.get(invAdjustmentLineItem.getInvItemLocation().getIlCode().toString())).doubleValue();

                                } else {

                                    try {

		 		  	   	    			/*LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIlCode(
		 		  	   	    					arReceipt.getRctDate(), arInvoiceLineItem.getInvItemLocation().getIlCode(), AD_BRNCH, AD_CMPNY);
		 		  	   	    			 */
                                        invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                                invAdjustment.getAdjDate(), II_NM, LOC_NM, branch, companyCode);
                                        Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry  L04");
                                        CURR_QTY = invCosting.getCstRemainingQuantity();
                                    }
                                    catch (FinderException ex) {
                                        System.out.println("CATCH");
                                    }
                                }

                                if (invCosting != null) {

		 		  	   	    		/*CURR_QTY = this.convertByUomAndQuantity(arReceiptLineItem.getInvItemLocation().getInvItem().getInvUnitOfMeasure(),
		 		  	   	    			arReceiptLineItem.getInvItemLocation().getInvItem(),
		 		  	   	    				invCosting.getCstRemainingQuantity(), AD_CMPNY);*/

                                    CURR_QTY = commonData.convertByUomAndQuantity(
                                            invAdjustmentLineItem.getInvUnitOfMeasure(),
                                            invAdjustmentLineItem.getInvItemLocation().getInvItem(),
                                            CURR_QTY, companyCode);

                                    Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry  L05");
                                }

                                double LOWEST_QTY = commonData.convertByUomAndQuantity(
                                        invAdjustmentLineItem.getInvUnitOfMeasure(),
                                        invAdjustmentLineItem.getInvItemLocation().getInvItem(),
                                        1, companyCode);
                                Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry  L06");
                                System.out.println("CURRENT QTY: " + CURR_QTY);
                                System.out.println("ILI QTY: " + ILI_QTY);
                                System.out.println("LOWEST_QTY: " + LOWEST_QTY);
                                System.out.println("invCosting: " + invCosting);
                                if ((invCosting == null && isIlFound == false) || CURR_QTY == 0 ||
                                        CURR_QTY - ILI_QTY <= -LOWEST_QTY) {
                                    a++;
                                    hasInsufficientItems = true;
                                    System.out.println("Doc Number: " + invAdjustment.getAdjDocumentNumber());
                                    System.out.println("Item: " + invAdjustmentLineItem.getInvItemLocation().getInvItem().getIiName());
                                    System.out.println("Adjust Qty: " + invAdjustmentLineItem.getAlAdjustQuantity());
                                    double remQty = 0;
                                    double unitCost = 0;
                                    try {
                                        remQty = CURR_QTY;
                                    }
                                    catch (Exception e) {
                                        remQty = 0;
                                    }
                                    System.out.println("Remaining Qty: " + remQty);
                                    insufficientItems += invAdjustmentLineItem.getInvItemLocation().getInvItem().getIiName() + " ";
                                    insufficientFix = Math.abs(invAdjustmentLineItem.getAlAdjustQuantity()) - CURR_QTY;
                                    CURR_QTY = CURR_QTY + insufficientFix;
                                    insufficient.add(invAdjustmentLineItem.getInvItemLocation().getInvItem().getIiName());
                                    insufficientQty.add(insufficientFix);
                                    insufficientUOM.add(invAdjustmentLineItem.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName());
                                    insufficientUnitCost.add(invAdjustmentLineItem.getAlUnitCost());
                                    itemLocation.add(invAdjustmentLineItem.getInvItemLocation().getInvLocation().getLocName());
                                    System.out.println("insufficientFix: " + Math.abs(insufficientFix));


                                }

                                CURR_QTY -= ILI_QTY;

                                if (isIlFound) {
                                    cstMap.remove(invAdjustmentLineItem.getInvItemLocation().getIlCode().toString());
                                }

                                cstMap.put(invAdjustmentLineItem.getInvItemLocation().getIlCode().toString(), CURR_QTY);
                            }


                        }

                    }

                    String[] insuItem;
                    insuItem = new String[a];

                    Double[] insuQty;
                    insuQty = new Double[a];

                    String[] insuUOM;
                    insuUOM = new String[a];

                    Double[] insuCost;
                    insuCost = new Double[a];

                    String[] insuItemLoc;
                    insuItemLoc = new String[a];

                    if (hasInsufficientItems) {
                        results = "hasInsufficientItems";
                        System.out.println("insufficientItems: " + insufficientItems);
                        System.out.println(insufficient.size());

                        Iterator mi = insufficient.iterator();
                        int b = 0;
                        while (mi.hasNext()) {

                            String miscStr = (String) mi.next();
                            System.out.println("Items: " + miscStr);
                            insuItem[b] = miscStr;
                            System.out.println("insuTest[b]: " + insuItem[b]);
                            b++;
                        }

                        Iterator mi2 = insufficientQty.iterator();
                        b = 0;
                        while (mi2.hasNext()) {

                            Double miscStr = (Double) mi2.next();
                            insuQty[b] = miscStr;
                            System.out.println("insuTest[b]: " + insuQty[b]);
                            b++;
                        }

                        Iterator mi3 = insufficientUOM.iterator();
                        b = 0;
                        while (mi3.hasNext()) {

                            String miscStr = (String) mi3.next();
                            insuUOM[b] = miscStr;
                            System.out.println("insuTest[b]: " + insuUOM[b]);
                            b++;
                        }

                        Iterator mi4 = insufficientUnitCost.iterator();
                        b = 0;
                        while (mi4.hasNext()) {

                            Double miscStr = (Double) mi4.next();
                            insuCost[b] = miscStr;
                            System.out.println("insuItemLoc[b]: " + insuCost[b]);
                            b++;
                        }

                        Iterator mi5 = itemLocation.iterator();
                        b = 0;
                        while (mi5.hasNext()) {

                            String miscStr = (String) mi5.next();
                            insuItemLoc[b] = miscStr;
                            System.out.println("insuCost[b]: " + insuItemLoc[b]);
                            b++;
                        }

                        String[] adjEncode = new String[1];
                        //int ctr = 0;
                        //for(int x=0; x < a ; x++){
                        adjEncode[0] = invAdjRowEncode(invAdjustment.getAdjDate(), user, insuItemLoc, invAdjAccount, insuItem, insuQty,
                                insuUOM, insuCost, a, invAdjustment.getAdjDocumentNumber());
                        //}
                        int Result = 0;
                        try {
                            Result = setInvAdjustment(adjEncode, branchCode, companyCode);
                            count++;
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                            throw new EJBException(ex.getMessage());
                        }


                        System.out.println("Result: " + Result);
                        results = Integer.toString(Result);

                        //throw new GlobalRecordInvalidException(insufficientItems.substring(0, insufficientItems.lastIndexOf(",")));
                    }

                }
            } else if (transactionType.equals("Sales")) {
                System.out.println("SALES");
                Collection arReceipts = arReceiptHome.findUnpostedRctByRctDateRangeByBranch(dateFrom, dateTo, branch, companyCode);
                HashMap cstMapBatch = new HashMap();
                Date oldDate = null;
                Iterator i = arReceipts.iterator();
                HashMap cstMap = null;
                int runVar = 0;
                while (i.hasNext()) {

                    LocalArReceipt arReceipt = (LocalArReceipt) i.next();

                    System.out.println("*************************CHECKING INSUFFICIENT SALES STOCKS******************************");

                    boolean hasInsufficientItems = false;
                    String insufficientItems = "";

                    Collection arReceiptLineItems = arReceipt.getArInvoiceLineItems();

                    Iterator p = arReceiptLineItems.iterator();
                    int a = 0;
                    ArrayList insufficient = new ArrayList();
                    ArrayList insufficientQty = new ArrayList();
                    ArrayList insufficientUOM = new ArrayList();
                    ArrayList insufficientUnitCost = new ArrayList();
                    ArrayList itemLocation = new ArrayList();
                    double totalAmount = 0;
                    runVar++;

                    if ((arReceipt.getRctDate().equals(dateTo) && dateFrom.equals(dateTo)) || runVar == 1) {
                        System.out.println("INSTANTIATE");
                        cstMap = new HashMap();
                    }
                    //HashMap cstMap = new HashMap();
                    while (p.hasNext()) {

                        LocalArInvoiceLineItem arReceiptLineItem = (LocalArInvoiceLineItem) p.next();
                        String II_NM = arReceiptLineItem.getInvItemLocation().getInvItem().getIiName();
                        String LOC_NM = arReceiptLineItem.getInvItemLocation().getInvLocation().getLocName();

                        if (arReceipt.getRctType().equals("MISC")) {
                            if (arReceiptLineItem.getIliEnableAutoBuild() == EJBCommon.TRUE && arReceiptLineItem.getInvItemLocation().getInvItem().getIiClass().equals("Assembly")) {


                                Collection invBillOfMaterials = arReceiptLineItem.getInvItemLocation().getInvItem().getInvBillOfMaterials();

                                Iterator j = invBillOfMaterials.iterator();

                                while (j.hasNext()) {

                                    LocalInvBillOfMaterial invBillOfMaterial = (LocalInvBillOfMaterial) j.next();

                                    LocalInvItemLocation invItemLocation = invItemLocationHome.findByLocNameAndIiName(invBillOfMaterial.getBomLocName(),
                                            invBillOfMaterial.getBomIiName(), companyCode);

                                    double ILI_QTY = commonData.convertByUomFromAndItemAndQuantity(
                                            arReceiptLineItem.getInvUnitOfMeasure(),
                                            arReceiptLineItem.getInvItemLocation().getInvItem(),
                                            Math.abs(arReceiptLineItem.getIliQuantity()), companyCode);
                                    Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry  L01");


                                    LocalInvItem bomItm = invItemHome.findByIiName(invBillOfMaterial.getBomIiName(), companyCode);

                                    double NEEDED_QTY = commonData.convertByUomFromAndItemAndQuantity(invBillOfMaterial.getInvUnitOfMeasure(), bomItm,
                                            invBillOfMaterial.getBomQuantityNeeded(), companyCode);

                                    double CURR_QTY = 0;

                                    boolean isIlFound = false;

                                    double remQty = 0;

                                    LocalInvCosting invBomCosting = null;

                                    if (cstMap.containsKey(invItemLocation.getIlCode().toString())) {

                                        isIlFound = true;
                                        CURR_QTY = ((Double) cstMap.get(invItemLocation.getIlCode().toString())).doubleValue();

                                    } else {

                                        try {

                                            invBomCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIlCode(
                                                    arReceipt.getRctDate(), invItemLocation.getIlCode(), branch, companyCode);

                                            Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry  L02");
                                            CURR_QTY = invBomCosting.getCstRemainingQuantity();
                                        }
                                        catch (FinderException ex) {


                                        }

                                    }

                                    if (invBomCosting != null || cstMap.containsKey(arReceiptLineItem.getInvItemLocation().getIlCode().toString())) {

                                        CURR_QTY = commonData.convertByUomAndQuantity(
                                                arReceiptLineItem.getInvUnitOfMeasure(),
                                                arReceiptLineItem.getInvItemLocation().getInvItem(),
                                                CURR_QTY, companyCode);

                                        Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry  L04");
                                        try {
                                            remQty = CURR_QTY;
                                        }
                                        catch (Exception e) {
                                            remQty = 0;
                                        }
                                    }
                                    if (CURR_QTY == 0 || CURR_QTY < (NEEDED_QTY * ILI_QTY)) {


                                        a++;
                                        hasInsufficientItems = true;
                                        try {

                                            System.out.println("Doc Number: " + arReceipt.getRctNumber());
                                            System.out.println("Item: " + arReceiptLineItem.getInvItemLocation().getInvItem().getIiName());
                                            System.out.println("Adjust Qty: " + arReceiptLineItem.getIliQuantity());

                                        }
                                        catch (Exception e) {
                                            System.out.println("Catch");
                                        }
                                        double unitCost = 0;

                                        System.out.println("Remaining Qty: " + remQty);
                                        //insufficientItems += invAdjustmentLineItem.getInvItemLocation().getInvItem().getIiName() + " ";
                                        insufficientFix = arReceiptLineItem.getIliQuantity() - CURR_QTY;
                                        //CURR_QTY = insufficientFix;

                                        insufficient.add(arReceiptLineItem.getInvItemLocation().getInvItem().getIiName());
                                        insufficientQty.add(insufficientFix);
                                        insufficientUOM.add(arReceiptLineItem.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName());
                                        insufficientUnitCost.add(arReceiptLineItem.getIliUnitPrice());
                                        itemLocation.add(arReceiptLineItem.getInvItemLocation().getInvLocation().getLocName());
                                        System.out.println("insufficientFix: " + insufficientFix);
                                        CURR_QTY = CURR_QTY + insufficientFix;
                                        insufficientItems += arReceiptLineItem.getInvItemLocation().getInvItem().getIiName()
                                                + "-" + invBillOfMaterial.getBomIiName() + ", ";
                                    }

                                    //CURR_QTY -= (NEEDED_QTY * ILI_QTY);
                                    CURR_QTY -= ILI_QTY;

                                    if (!isIlFound) {
                                        cstMap.remove(invItemLocation.getIlCode().toString());
                                    }

                                    cstMap.put(invItemLocation.getIlCode().toString(), CURR_QTY);

                                    Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry  L03");


                                }

                            } else {


                                LocalInvCosting invCosting = null;
                                double ILI_QTY = commonData.convertByUomAndQuantity(
                                        arReceiptLineItem.getInvUnitOfMeasure(),
                                        arReceiptLineItem.getInvItemLocation().getInvItem(),
                                        Math.abs(arReceiptLineItem.getIliQuantity()), companyCode);

                                double CURR_QTY = 0;
                                boolean isIlFound = false;

                                if (cstMap.containsKey(arReceiptLineItem.getInvItemLocation().getIlCode().toString())) {
                                    Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry  L04 - A");
                                    isIlFound = true;
                                    CURR_QTY = ((Double) cstMap.get(arReceiptLineItem.getInvItemLocation().getIlCode().toString())).doubleValue();

                                } else {

                                    try {

                                        invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                                arReceipt.getRctDate(), II_NM, LOC_NM, branch, companyCode);
                                        Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry  L04");
                                        CURR_QTY = invCosting.getCstRemainingQuantity();
                                    }
                                    catch (FinderException ex) {
                                        System.out.println("CATCH");
                                    }
                                }

                                if (invCosting != null || cstMap.containsKey(arReceiptLineItem.getInvItemLocation().getIlCode().toString())) {

                                    CURR_QTY = commonData.convertByUomAndQuantity(
                                            arReceiptLineItem.getInvUnitOfMeasure(),
                                            arReceiptLineItem.getInvItemLocation().getInvItem(),
                                            CURR_QTY, companyCode);

                                    Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry  L05");
                                }

                                double LOWEST_QTY = commonData.convertByUomAndQuantity(
                                        arReceiptLineItem.getInvUnitOfMeasure(),
                                        arReceiptLineItem.getInvItemLocation().getInvItem(),
                                        1, companyCode);
                                Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry  L06");
                                if ((invCosting == null && isIlFound == false) || CURR_QTY == 0 ||
                                        CURR_QTY - ILI_QTY <= -LOWEST_QTY) {
                                    a++;
                                    hasInsufficientItems = true;

                                    double remQty = 0;
                                    double unitCost = 0;
                                    try {
                                        System.out.println("Doc Number: " + arReceipt.getRctNumber());
                                        System.out.println("Item: " + arReceiptLineItem.getInvItemLocation().getInvItem().getIiName());
                                        System.out.println("Adjust Qty: " + arReceiptLineItem.getIliQuantity());
                                        remQty = invCosting.getCstRemainingQuantity();
                                    }
                                    catch (Exception e) {
                                        System.out.println("BOOM!");
                                        remQty = 0;
                                    }
                                    System.out.println("Remaining Qty: " + remQty);
                                    insufficientItems += arReceiptLineItem.getInvItemLocation().getInvItem().getIiName() + " ";
                                    insufficientFix = arReceiptLineItem.getIliQuantity() - CURR_QTY;
                                    CURR_QTY = CURR_QTY + insufficientFix;
                                    insufficient.add(arReceiptLineItem.getInvItemLocation().getInvItem().getIiName());
                                    insufficientQty.add(insufficientFix);
                                    insufficientUOM.add(arReceiptLineItem.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName());
                                    insufficientUnitCost.add(arReceiptLineItem.getIliUnitPrice());
                                    itemLocation.add(arReceiptLineItem.getInvItemLocation().getInvLocation().getLocName());
                                    System.out.println("insufficientFix: " + insufficientFix);


                                }

                                CURR_QTY -= ILI_QTY;

                                if (isIlFound) {
                                    cstMap.remove(arReceiptLineItem.getInvItemLocation().getIlCode().toString());
                                }

                                cstMap.put(arReceiptLineItem.getInvItemLocation().getIlCode().toString(), CURR_QTY);

                            }
                        }

                    }

                    String[] insuItem;
                    insuItem = new String[a];

                    Double[] insuQty;
                    insuQty = new Double[a];

                    String[] insuUOM;
                    insuUOM = new String[a];

                    Double[] insuCost;
                    insuCost = new Double[a];

                    String[] insuLocation;
                    insuLocation = new String[a];

                    if (hasInsufficientItems) {
                        results = "hasInsufficientItems";
                        System.out.println("insufficientItems: " + insufficientItems);
                        System.out.println(insufficient.size());

                        Iterator mi = insufficient.iterator();
                        int b = 0;
                        while (mi.hasNext()) {

                            String miscStr = (String) mi.next();
                            System.out.println("Items: " + miscStr);
                            insuItem[b] = miscStr;
                            System.out.println("insuTest[b]: " + insuItem[b]);
                            b++;
                        }

                        Iterator mi2 = insufficientQty.iterator();
                        b = 0;
                        while (mi2.hasNext()) {

                            Double miscStr = (Double) mi2.next();
                            insuQty[b] = miscStr;
                            System.out.println("insuTest[b]: " + insuQty[b]);
                            b++;
                        }

                        Iterator mi3 = insufficientUOM.iterator();
                        b = 0;
                        while (mi3.hasNext()) {

                            String miscStr = (String) mi3.next();
                            insuUOM[b] = miscStr;
                            System.out.println("insuTest[b]: " + insuUOM[b]);
                            b++;
                        }

                        Iterator mi4 = insufficientUnitCost.iterator();
                        b = 0;
                        while (mi4.hasNext()) {

                            Double miscStr = (Double) mi4.next();
                            insuCost[b] = miscStr;
                            System.out.println("insuCost[b]: " + insuCost[b]);
                            b++;
                        }

                        Iterator mi5 = itemLocation.iterator();
                        b = 0;
                        while (mi5.hasNext()) {

                            String miscStr = (String) mi5.next();
                            insuLocation[b] = miscStr;
                            System.out.println("insuCost[b]: " + insuLocation[b]);
                            b++;
                        }

                        String[] adjEncode = new String[1];
						/*int ctr = 0;
						for(int x=0; x < a ; x++){
						System.out.println(arReceipt.getRctNumber());
						System.out.println(ctr);*/
                        adjEncode[0] = invAdjRowEncode(arReceipt.getRctDate(), user, insuLocation, invAdjAccount, insuItem, insuQty,
                                insuUOM, insuCost, a, arReceipt.getRctNumber());
                        //}
                        int Result = 0;

                        try {
                            System.out.println("adjEncode: " + adjEncode.length);
                            if (adjEncode != null) {
                                System.out.println("SAVING");
                                Result = setInvAdjustment(adjEncode, branchCode, companyCode);
                                count++;
                            }
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                            throw new EJBException(ex.getMessage());
                        }


                        System.out.println("Result: " + Result);
                        results = Integer.toString(Result);

                        //throw new GlobalRecordInvalidException(insufficientItems.substring(0, insufficientItems.lastIndexOf(",")));
                    }

                }

                System.out.println("CHECKING INVOICES");
                Collection arInvoices = arInvoiceHome.findUnpostedInvByInvDateRangeByBranch(dateFrom, dateTo, branch, companyCode);

                Iterator q = arInvoices.iterator();
                HashMap cstMapInvoice = null;
                int runVar2 = 0;
                while (q.hasNext()) {

                    LocalArInvoice arInvoice = (LocalArInvoice) q.next();

                    System.out.println("*************************CHECKING INSUFFICIENT SALES STOCKS******************************");

                    boolean hasInsufficientItems = false;
                    String insufficientItems = "";

                    Collection arInvoiceLineItems = arInvoice.getArInvoiceLineItems();

                    Iterator p = arInvoiceLineItems.iterator();
                    int a = 0;
                    ArrayList insufficient = new ArrayList();
                    ArrayList insufficientQty = new ArrayList();
                    ArrayList insufficientUOM = new ArrayList();
                    ArrayList insufficientUnitCost = new ArrayList();
                    ArrayList itemLocation = new ArrayList();
                    double totalAmount = 0;
                    runVar2++;
                    if ((arInvoice.getInvDate().equals(dateTo) && dateFrom.equals(dateTo)) || runVar2 == 1) {
                        cstMapInvoice = new HashMap();
                        cstMap = new HashMap();
                    }
                    //HashMap cstMap = new HashMap();
                    while (p.hasNext()) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) p.next();

                        if (arInvoiceLineItem.getIliEnableAutoBuild() == EJBCommon.TRUE && arInvoiceLineItem.getInvItemLocation().getInvItem().getIiClass().equals("Assembly")) {

                            Collection invBillOfMaterials = arInvoiceLineItem.getInvItemLocation().getInvItem().getInvBillOfMaterials();

                            Iterator j = invBillOfMaterials.iterator();
                            Debug.print("ArInvoiceEntryControllerBean saveArInvIliEntry N01");
                            while (j.hasNext()) {

                                LocalInvBillOfMaterial invBillOfMaterial = (LocalInvBillOfMaterial) j.next();

                                LocalInvItemLocation invItemLocation = invItemLocationHome.findByLocNameAndIiName(invBillOfMaterial.getBomLocName(),
                                        invBillOfMaterial.getBomIiName(), companyCode);

                                LocalInvCosting invBomCosting = null;
                                double CURR_QTY = 0;
                                double ILI_QTY = commonData.convertByUomFromAndItemAndQuantity(
                                        arInvoiceLineItem.getInvUnitOfMeasure(),
                                        arInvoiceLineItem.getInvItemLocation().getInvItem(),
                                        Math.abs(arInvoiceLineItem.getIliQuantity()), companyCode);
                                Debug.print("ArInvoiceEntryControllerBean saveArInvIliEntry N02");
                                boolean isIlFound = false;
                                if (cstMap.containsKey(invItemLocation.getIlCode().toString())) {

                                    isIlFound = true;
                                    CURR_QTY = ((Double) cstMap.get(invItemLocation.getIlCode().toString())).doubleValue();

                                } else {
                                    try {
                                        invBomCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                                arInvoice.getInvDate(), invBillOfMaterial.getBomIiName(), invItemLocation.getInvLocation().getLocName(), branch, companyCode);
                                        CURR_QTY = invBomCosting.getCstRemainingQuantity();
                                    }
                                    catch (FinderException ex) {

                                    }
                                }


                                LocalInvItem bomItm = invItemHome.findByIiName(invBillOfMaterial.getBomIiName(), companyCode);


                                double NEEDED_QTY = commonData.convertByUomFromAndItemAndQuantity(invBillOfMaterial.getInvUnitOfMeasure(), bomItm,
                                        invBillOfMaterial.getBomQuantityNeeded(), companyCode);
                                Debug.print("ArInvoiceEntryControllerBean saveArInvIliEntry N03");


                                try {
                                    if (invBomCosting != null || cstMap.containsKey(arInvoiceLineItem.getInvItemLocation().getIlCode().toString())) {

                                        CURR_QTY = commonData.convertByUomAndQuantity(
                                                arInvoiceLineItem.getInvUnitOfMeasure(),
                                                arInvoiceLineItem.getInvItemLocation().getInvItem(),
                                                CURR_QTY, companyCode);
                                    }
                                }
                                catch (Exception e) {

                                }
                                if (invBomCosting == null || CURR_QTY == 0 || CURR_QTY < (NEEDED_QTY * ILI_QTY)) {


                                    a++;
                                    hasInsufficientItems = true;
                                    System.out.println("Doc Number: " + arInvoice.getInvNumber());
                                    System.out.println("Item: " + arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                                    System.out.println("Adjust Qty: " + arInvoiceLineItem.getIliQuantity());
                                    double remQty = 0;
                                    double unitCost = 0;
                                    try {
                                        remQty = invBomCosting.getCstRemainingQuantity();
                                    }
                                    catch (Exception e) {
                                        remQty = 0;
                                    }
                                    System.out.println("Remaining Qty: " + CURR_QTY);
                                    //insufficientItems += invAdjustmentLineItem.getInvItemLocation().getInvItem().getIiName() + " ";
                                    insufficientFix = arInvoiceLineItem.getIliQuantity() - CURR_QTY;
                                    CURR_QTY = CURR_QTY + insufficientFix;
                                    insufficient.add(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                                    insufficientQty.add(insufficientFix);
                                    insufficientUOM.add(arInvoiceLineItem.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName());
                                    insufficientUnitCost.add(arInvoiceLineItem.getIliUnitPrice());
                                    System.out.println("insufficientFix: " + insufficientFix);
                                    itemLocation.add(arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName());
                                    insufficientItems += arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName()
                                            + "-" + invBillOfMaterial.getBomIiName() + ", ";
                                }

                                CURR_QTY -= ILI_QTY;

                                if (isIlFound) {
                                    cstMap.remove(arInvoiceLineItem.getInvItemLocation().getIlCode().toString());
                                }

                                cstMap.put(arInvoiceLineItem.getInvItemLocation().getIlCode().toString(), CURR_QTY);
                            }
                        } else {
                            double CURR_QTY = 0;
                            LocalInvCosting invCosting = null;

                            double ILI_QTY = commonData.convertByUomAndQuantity(
                                    arInvoiceLineItem.getInvUnitOfMeasure(),
                                    arInvoiceLineItem.getInvItemLocation().getInvItem(),
                                    Math.abs(arInvoiceLineItem.getIliQuantity()), companyCode);

                            boolean isIlFound = false;

                            if (cstMap.containsKey(arInvoiceLineItem.getInvItemLocation().getIlCode().toString())) {
                                Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry  L04 - A");
                                isIlFound = true;
                                CURR_QTY = ((Double) cstMap.get(arInvoiceLineItem.getInvItemLocation().getIlCode().toString())).doubleValue();

                            } else {
                                try {
                                    invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                            arInvoice.getInvDate(), arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName()
                                            , arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName(), branch, companyCode);
                                    Debug.print("ArInvoiceEntryControllerBean saveArInvIliEntry N04");
                                    CURR_QTY = invCosting.getCstRemainingQuantity();
                                }
                                catch (FinderException ex) {

                                }
                            }


                            if (invCosting != null || cstMap.containsKey(arInvoiceLineItem.getInvItemLocation().getIlCode().toString())) {
                                CURR_QTY = commonData.convertByUomAndQuantity(
                                        arInvoiceLineItem.getInvUnitOfMeasure(),
                                        arInvoiceLineItem.getInvItemLocation().getInvItem(),
                                        CURR_QTY, companyCode);

                                Debug.print("ArMiscReceiptEntryControllerBean saveArRctIliEntry  L05");
                            }

                            double LOWEST_QTY = commonData.convertByUomAndQuantity(
                                    arInvoiceLineItem.getInvUnitOfMeasure(),
                                    arInvoiceLineItem.getInvItemLocation().getInvItem(),
                                    1, companyCode);

                            Debug.print("ArInvoiceEntryControllerBean saveArInvIliEntry N05");

                            if ((invCosting == null && !isIlFound) || CURR_QTY == 0 ||
                                    CURR_QTY - ILI_QTY <= -LOWEST_QTY) {
                                a++;
                                hasInsufficientItems = true;
                                System.out.println("Doc Number: " + arInvoice.getInvNumber());
                                System.out.println("Item: " + arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                                System.out.println("Adjust Qty: " + arInvoiceLineItem.getIliQuantity());
                                double remQty = 0;
                                double unitCost = 0;
                                try {
                                    remQty = CURR_QTY;
                                }
                                catch (Exception e) {
                                    remQty = 0;
                                }
                                System.out.println("Remaining Qty: " + remQty);
                                insufficientItems += arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName() + " ";
                                insufficientFix = arInvoiceLineItem.getIliQuantity() - remQty;
                                insufficient.add(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                                insufficientQty.add(insufficientFix);
                                insufficientUOM.add(arInvoiceLineItem.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName());
                                insufficientUnitCost.add(arInvoiceLineItem.getIliUnitPrice());
                                System.out.println("insufficientFix: " + insufficientFix);
                                itemLocation.add(arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName());

                            }

                            CURR_QTY -= ILI_QTY;

                            if (isIlFound) {
                                cstMap.remove(arInvoiceLineItem.getInvItemLocation().getIlCode().toString());
                            }

                            cstMap.put(arInvoiceLineItem.getInvItemLocation().getIlCode().toString(), CURR_QTY);
                        }

                    }

                    String[] insuItem;
                    insuItem = new String[a];

                    Double[] insuQty;
                    insuQty = new Double[a];

                    String[] insuUOM;
                    insuUOM = new String[a];

                    Double[] insuCost;
                    insuCost = new Double[a];

                    String[] insuLocation;
                    insuLocation = new String[a];

                    if (hasInsufficientItems) {
                        results = "hasInsufficientItems";
                        System.out.println("insufficientItems: " + insufficientItems);
                        System.out.println(insufficient.size());

                        Iterator mi = insufficient.iterator();
                        int b = 0;
                        while (mi.hasNext()) {

                            String miscStr = (String) mi.next();
                            System.out.println("Items: " + miscStr);
                            insuItem[b] = miscStr;
                            System.out.println("insuTest[b]: " + insuItem[b]);
                            b++;
                        }

                        Iterator mi2 = insufficientQty.iterator();
                        b = 0;
                        while (mi2.hasNext()) {

                            Double miscStr = (Double) mi2.next();
                            insuQty[b] = miscStr;
                            System.out.println("insuTest[b]: " + insuQty[b]);
                            b++;
                        }

                        Iterator mi3 = insufficientUOM.iterator();
                        b = 0;
                        while (mi3.hasNext()) {

                            String miscStr = (String) mi3.next();
                            insuUOM[b] = miscStr;
                            System.out.println("insuTest[b]: " + insuUOM[b]);
                            b++;
                        }

                        Iterator mi4 = insufficientUnitCost.iterator();
                        b = 0;
                        while (mi4.hasNext()) {

                            Double miscStr = (Double) mi4.next();
                            insuCost[b] = miscStr;
                            System.out.println("insuCost[b]: " + insuCost[b]);
                            b++;
                        }

                        Iterator mi5 = itemLocation.iterator();
                        b = 0;
                        while (mi5.hasNext()) {

                            String miscStr = (String) mi5.next();
                            insuLocation[b] = miscStr;
                            System.out.println("insuTest[b]: " + insuLocation[b]);
                            b++;
                        }

                        String[] adjEncode = new String[1];
						/*int ctr = 0;
						for(int x=0; x < a ; x++){
						System.out.println(arReceipt.getRctNumber());
						System.out.println(ctr);*/
                        adjEncode[0] = invAdjRowEncode(arInvoice.getInvDate(), user, insuLocation, invAdjAccount, insuItem, insuQty,
                                insuUOM, insuCost, a, arInvoice.getInvNumber());
                        //}
                        int Result = 0;

                        try {
                            System.out.println("adjEncode: " + adjEncode.length);
                            if (adjEncode != null) {
                                System.out.println("SAVING");
                                Result = setInvAdjustment(adjEncode, branchCode, companyCode);
                                count++;
                            }
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                            throw new EJBException(ex.getMessage());
                        }


                        System.out.println("Result: " + Result);
                        results = Integer.toString(Result);

                        //throw new GlobalRecordInvalidException(insufficientItems.substring(0, insufficientItems.lastIndexOf(",")));
                    }

                }
            }


        }
        catch (Exception ex) {


            ex.printStackTrace();
            throw new EJBException(ex.getMessage());

        }

        System.out.println("*************************INSUFFICIENT STOCKS Fixed******************************");
        System.out.println("Number of Insufficiency: " + count);
        return results;
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
        encodedResult.append(invItem.getIiSalesPrice());
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
        if (invItem.getIiDoneness() == null || invItem.getIiDoneness().length() < 1) {
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

        // Non-Inventoriable
        encodedResult.append(invItem.getIiNonInventoriable());
        encodedResult.append(separator);

        // isItemRaw
        if (isItemRaw == EJBCommon.TRUE) {
            encodedResult.append("1");
            encodedResult.append(separator);
        } else {
            encodedResult.append("0");
            encodedResult.append(separator);
        }

        // Open Product
        encodedResult.append(invItem.getIiOpenProduct());
        encodedResult.append(separator);

        // Quantity
        encodedResult.append(quantity);
        encodedResult.append(separator);

        //UnitValue
        encodedResult.append(unitValue);
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
            LocalInvPriceLevel invPriceLevel = (LocalInvPriceLevel) i.next();
            String priceLevelAmount = Double.toString(invPriceLevel.getPlAmount());
            Date dateNow = new Date();

            for (Object priceLevelDate : invPriceLevelDates) {
                LocalInvPriceLevelDate invPriceLevelDate = (LocalInvPriceLevelDate) priceLevelDate;
                if (invPriceLevel.getPlAdLvPriceLevel().equals(invPriceLevelDate.getPdAdLvPriceLevel()) && invPriceLevelDate.getPdStatus().equals("ENABLE")) {
                    System.out.println("hello");
                    if (dateNow.equals(invPriceLevelDate.getPdDateFrom()) || dateNow.equals(invPriceLevelDate.getPdDateTo())) {
                        priceLevelAmount = Double.toString(invPriceLevelDate.getPdAmount());
                        break;
                    }

                    if (dateNow.after(invPriceLevelDate.getPdDateFrom()) && dateNow.before(invPriceLevelDate.getPdDateTo())) {
                        priceLevelAmount = Double.toString(invPriceLevelDate.getPdAmount());
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

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = (LocalInvUnitOfMeasureConversion) i.next();

            // base unit
            encodedResult.append(String.valueOf(invUnitOfMeasureConversion.getUmcBaseUnit()));
            encodedResult.append(separator);

            // conversion factor
            encodedResult.append(invUnitOfMeasureConversion.getUmcConversionFactor());
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

            LocalInvBillOfMaterial invBillOfMaterial = (LocalInvBillOfMaterial) i.next();

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

    private String itemRowEncodePosUs(LocalInvItem invItem, Integer INV_LOCATION, String II_IL_LOCATION,
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
        encodedResult.append(invItem.getIiSalesPrice());
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
        if (invItem.getIiDoneness() == null || invItem.getIiDoneness().length() < 1) {
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

        // Non-Inventoriable
        encodedResult.append(invItem.getIiNonInventoriable());
        encodedResult.append(separator);

        // isItemRaw
        if (isItemRaw == EJBCommon.TRUE) {
            encodedResult.append("1");
            encodedResult.append(separator);
        } else {
            encodedResult.append("0");
            encodedResult.append(separator);
        }

        // Open Product
        encodedResult.append(invItem.getIiOpenProduct());
        encodedResult.append(separator);

        // Tax Code
        encodedResult.append(invItem.getIiTaxCode());
        encodedResult.append(separator);

        // Quantity
        encodedResult.append(quantity);
        encodedResult.append(separator);

        //UnitValue
        encodedResult.append(unitValue);
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

        // INV_PRC_LVL
        encodedResult.append("INV_PRC_LVL");
        encodedResult.append(separator);

        Collection invPriceLevels = invItem.getInvPriceLevels();

        Iterator i = invPriceLevels.iterator();

        while (i.hasNext()) {

            LocalInvPriceLevel invPriceLevel = (LocalInvPriceLevel) i.next();

            // primary key
            encodedResult.append(invPriceLevel.getPlCode().toString());
            encodedResult.append(separator);

            // amount
            encodedResult.append(invPriceLevel.getPlAmount());
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

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = (LocalInvUnitOfMeasureConversion) i.next();

            // base unit
            encodedResult.append(String.valueOf(invUnitOfMeasureConversion.getUmcBaseUnit()));
            encodedResult.append(separator);

            // conversion factor
            encodedResult.append(invUnitOfMeasureConversion.getUmcConversionFactor());
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

            LocalInvBillOfMaterial invBillOfMaterial = (LocalInvBillOfMaterial) i.next();

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

    private String itemRowEncodeWithUnitPrice(LocalInvItem invItem, Integer INV_LOCATION, String II_IL_LOCATION,
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
        encodedResult.append(invItem.getIiSalesPrice());
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
        if (invItem.getIiDoneness() == null || invItem.getIiDoneness().length() < 1) {
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

        // Non-Inventoriable
        encodedResult.append(invItem.getIiNonInventoriable());
        encodedResult.append(separator);

        // isItemRaw
        if (isItemRaw == EJBCommon.TRUE) {
            encodedResult.append("1");
            encodedResult.append(separator);
        } else {
            encodedResult.append("0");
            encodedResult.append(separator);
        }

        // Open Product
        encodedResult.append(invItem.getIiOpenProduct());
        encodedResult.append(separator);

        // Quantity
        encodedResult.append(quantity);
        encodedResult.append(separator);

        //UnitValue
        encodedResult.append(unitValue);
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

        // INV_PRC_LVL
        encodedResult.append("INV_PRC_LVL");
        encodedResult.append(separator);

        Collection invPriceLevels = invItem.getInvPriceLevels();

        for (Object priceLevel : invPriceLevels) {

            LocalInvPriceLevel invPriceLevel = (LocalInvPriceLevel) priceLevel;

            // primary key
            encodedResult.append(invPriceLevel.getPlCode().toString());
            encodedResult.append(separator);

            // amount
            encodedResult.append(invPriceLevel.getPlAmount());
            encodedResult.append(separator);

            // price level
            encodedResult.append(invPriceLevel.getPlAdLvPriceLevel());
            encodedResult.append(separator);

            // item
            encodedResult.append(invPriceLevel.getInvItem().getIiCode());

        }

        // item end
        encodedResult.append("ITEM_END");
        encodedResult.append(separator);

        // End separator
        encodedResult.append(separator);

        return encodedResult.toString();


    }

    private String mmoInvRowEncode(LocalArInvoice arInvoice) {

        char separator = EJBCommon.SEPARATOR;
        StringBuilder encodedResult = new StringBuilder();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Start separator
        encodedResult.append(separator);

        // INV Code
        encodedResult.append(arInvoice.getInvCode());
        encodedResult.append(separator);

        // Description
        encodedResult.append(arInvoice.getInvDescription());
        encodedResult.append(separator);

        // INV Date
        encodedResult.append(sdf.format(arInvoice.getInvDate()));
        encodedResult.append(separator);

        // INV Number
        encodedResult.append(arInvoice.getInvNumber());
        encodedResult.append(separator);

        // Reference Number
        encodedResult.append(arInvoice.getInvReferenceNumber());
        encodedResult.append(separator);

        // Amount Due
        encodedResult.append(arInvoice.getInvAmountDue());
        encodedResult.append(separator);

        // Amount Paid
        encodedResult.append(arInvoice.getInvAmountPaid());
        encodedResult.append(separator);

        // Conversion Date
        encodedResult.append(arInvoice.getInvConversionDate());
        encodedResult.append(separator);

        // Conversion Rate
        encodedResult.append(arInvoice.getInvConversionRate());
        encodedResult.append(separator);

        //INV_BLL_TO_ADDRSS
        encodedResult.append(arInvoice.getInvBillToAddress());
        encodedResult.append(separator);

        //INV_BLL_TO_CNTCT
        encodedResult.append(arInvoice.getInvBillToContact());
        encodedResult.append(separator);

        //INV_BLL_TO_ALT_CNTCT
        encodedResult.append(arInvoice.getInvBillToAltContact());
        encodedResult.append(separator);

        //INV_BLL_TO_PHN
        encodedResult.append(arInvoice.getInvBillToPhone());
        encodedResult.append(separator);

        //INV_BLLNG_HDR
        encodedResult.append(arInvoice.getInvBillingHeader());
        encodedResult.append(separator);

        //INV_BLLNG_FTR
        encodedResult.append(arInvoice.getInvBillingFooter());
        encodedResult.append(separator);

        //INV_BLLNG_HDR2
        encodedResult.append(arInvoice.getInvBillingHeader2());
        encodedResult.append(separator);

        //INV_BLLNG_FTR2
        encodedResult.append(arInvoice.getInvBillingFooter2());
        encodedResult.append(separator);

        //INV_BLLNG_HDR3
        encodedResult.append(arInvoice.getInvBillingHeader3());
        encodedResult.append(separator);

        //INV_BLLNG_FTR3
        encodedResult.append(arInvoice.getInvBillingFooter3());
        encodedResult.append(separator);

        //INV_BLLNG_SGNTRY
        encodedResult.append(arInvoice.getInvBillingSignatory());
        encodedResult.append(separator);

        //INV_SGNTRY_TTL
        encodedResult.append(arInvoice.getInvSignatoryTitle());
        encodedResult.append(separator);

        //INV_SHP_TO_ADDRSS
        encodedResult.append(arInvoice.getInvShipToAddress());
        encodedResult.append(separator);

        //INV_SHP_TO_CNTCT
        encodedResult.append(arInvoice.getInvShipToContact());
        encodedResult.append(separator);

        //INV_SHP_TO_ALT_CNTCT
        encodedResult.append(arInvoice.getInvShipToAltContact());
        encodedResult.append(separator);

        //INV_SHP_TO_PHN
        encodedResult.append(arInvoice.getInvShipToPhone());
        encodedResult.append(separator);

        //INV_SHP_DT
        encodedResult.append(arInvoice.getInvShipDate());
        encodedResult.append(separator);

        //INV_LV_FRGHT
        encodedResult.append(arInvoice.getInvLvFreight());
        encodedResult.append(separator);

        //INV_LV_SHFT
        encodedResult.append(arInvoice.getInvLvShift());
        encodedResult.append(separator);

        //INV_SO_NMBR
        encodedResult.append(arInvoice.getInvSoNumber());
        encodedResult.append(separator);

        //INV_CLNT_PO
        encodedResult.append(arInvoice.getInvClientPO());
        encodedResult.append(separator);

        //INV_EFFCTVTY_DT
        encodedResult.append(arInvoice.getInvEffectivityDate());
        encodedResult.append(separator);

        //GL_FUNCTIONAL_CURRENCY
        encodedResult.append(arInvoice.getGlFunctionalCurrency().getFcName());
        encodedResult.append(separator);

        //AR_TAX_CODE
        encodedResult.append(arInvoice.getArTaxCode().getTcName());
        encodedResult.append(separator);

        //AR_WITHHOLDING_TAX_CODE
        encodedResult.append(arInvoice.getArWithholdingTaxCode().getWtcName());
        encodedResult.append(separator);

        //AR_CUSTOMER
        encodedResult.append(arInvoice.getArCustomer().getCstCustomerCode());
        encodedResult.append(separator);

        //AD_PAYMENT_TERM
        encodedResult.append(arInvoice.getAdPaymentTerm().getPytName());
        encodedResult.append(separator);

        // end separator
        encodedResult.append(separator);

        String lineSeparator = "~";

        // begin lineSeparator
        encodedResult.append(lineSeparator);

        // Append Invoice Lines
        for (Object o : arInvoice.getArInvoiceLines()) {

            LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) o;

            // begin separator
            encodedResult.append(separator);

            // IL_CODE
            encodedResult.append(arInvoiceLine.getIlCode());
            encodedResult.append(separator);

            // IL_DESC
            encodedResult.append(arInvoiceLine.getIlDescription());
            encodedResult.append(separator);

            // IL_QNTTY
            encodedResult.append(arInvoiceLine.getIlQuantity());
            encodedResult.append(separator);

            // IL_UNT_PRC
            encodedResult.append(arInvoiceLine.getIlUnitPrice());
            encodedResult.append(separator);

            // IL_AMNT
            encodedResult.append(arInvoiceLine.getIlAmount());
            encodedResult.append(separator);

            // IL_TX_AMNT
            encodedResult.append(arInvoiceLine.getIlTaxAmount());
            encodedResult.append(separator);

            // IL_TX
            encodedResult.append(arInvoiceLine.getIlTax());
            encodedResult.append(separator);

            // AR_STANDARD_MEMO_LINE
            encodedResult.append(arInvoiceLine.getArStandardMemoLine().getSmlCode());
            encodedResult.append(separator);

            // begin lineSeparator
            encodedResult.append(lineSeparator);
        }

        // Append Distribution Records
        encodedResult.append("$JOURNAL$");
        for (Object o : arInvoice.getArDistributionRecords()) {

            LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) o;

            // begin separator
            encodedResult.append(separator);

            // Dr Code
            encodedResult.append(arDistributionRecord.getDrCode());
            encodedResult.append(separator);

            // Dr Line
            encodedResult.append(arDistributionRecord.getDrLine());
            encodedResult.append(separator);

            // Dr Class
            encodedResult.append(arDistributionRecord.getDrClass());
            encodedResult.append(separator);

            // Dr Debit
            encodedResult.append(arDistributionRecord.getDrDebit());
            encodedResult.append(separator);

            // Dr Amount
            encodedResult.append(arDistributionRecord.getDrAmount());
            encodedResult.append(separator);

            // Dr COA
            encodedResult.append(arDistributionRecord.getGlChartOfAccount().getCoaCode());
            encodedResult.append(separator);

            // Dr Invoice
            encodedResult.append(arInvoice.getInvCode());
            encodedResult.append(separator);

            // begin lineSeparator
            encodedResult.append(lineSeparator);
        }

        return encodedResult.toString();


    }

    private InvModItemDetails decodeConsolidatedItem(String itemStr) {

        Debug.print("InvItemSyncControllerBean decodeConsolidatedItem");

        String separator = "$";
        InvModItemDetails details = new InvModItemDetails();

        // Remove first $ character
        itemStr = itemStr.substring(1);

        // II_NM
        int start = 0;
        int nextIndex = itemStr.indexOf(separator, start);
        int length = nextIndex - start;
        details.setIiName(itemStr.substring(start, start + length));

        // II_PRT_NMBR
        start = nextIndex + 1;
        nextIndex = itemStr.indexOf(separator, start);
        length = nextIndex - start;
        details.setIiPartNumber(itemStr.substring(start, start + length));

        // II_DESC
        start = nextIndex + 1;
        nextIndex = itemStr.indexOf(separator, start);
        length = nextIndex - start;
        details.setIiDescription(itemStr.substring(start, start + length));

        // II_UOM
        start = nextIndex + 1;
        nextIndex = itemStr.indexOf(separator, start);
        length = nextIndex - start;
        details.setIiUomName(itemStr.substring(start, start + length));


        // II_AD_LVL_CTGRY
        start = nextIndex + 1;
        nextIndex = itemStr.indexOf(separator, start);
        length = nextIndex - start;
        details.setIiAdLvCategory(itemStr.substring(start, start + length));

        // II_DFLT_LCTN
        start = nextIndex + 1;
        nextIndex = itemStr.indexOf(separator, start);
        length = nextIndex - start;
        details.setIiDefaultLocationName(itemStr.substring(start, start + length));

        // II_RMRKS
        start = nextIndex + 1;
        nextIndex = itemStr.indexOf(separator, start);
        length = nextIndex - start;
        details.setIiRemarks(itemStr.substring(start, start + length));

        details.setIiClass("Stock");
        details.setIiCostMethod("FIFO");
        details.setIiUnitCost(0);
        details.setIiSalesPrice(0);
        details.setIiEnable((byte) 1);
        details.setIiEnableAutoBuild((byte) 0);
        details.setIiDoneness(null);
        details.setIiSidings(null);
        details.setIiServiceCharge((byte) 0);
        details.setIiNonInventoriable((byte) 0);
        details.setIiPercentMarkup(0);
        details.setIiShippingCost(0);
        details.setIiMarkupValue(0);
        details.setIiMarket(null);
        details.setIiEnablePo((byte) 0);
        details.setIiPoCycle((byte) 0);
        details.setIiUmcPackaging(null);
        details.setIiOpenProduct((byte) 0);
        details.setIiFixedAsset((byte) 0);
        details.setIiDateAcquired(null);

        return details;


    }

    private void addInvUmcEntry(LocalInvItem invItem, String uomName, String uomAdLvClass, double conversionFactor, byte umcBaseUnit, Integer AD_CMPNY)
            throws GlobalNoRecordFoundException {

        Debug.print("InvItemSyncControllerBean addInvUmcEntry");

        try {

            // create umc
            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.create(conversionFactor, umcBaseUnit, AD_CMPNY);

            try {

                // map uom
                LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomNameAndUomAdLvClass(uomName, uomAdLvClass, AD_CMPNY);
                invUnitOfMeasureConversion.setInvUnitOfMeasure(invUnitOfMeasure);

                //invUnitOfMeasure.addInvUnitOfMeasureConversion(invUnitOfMeasureConversion);

            }
            catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();

            }

            // map item
            invUnitOfMeasureConversion.setInvItem(invItem);
            //invItem.addInvUnitOfMeasureConversion(invUnitOfMeasureConversion);

        }
        catch (GlobalNoRecordFoundException ex) {
            ex.printStackTrace();
            ctx.setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }

    private String invRowEncode(LocalArInvoice arInvoice) {

        char separator = EJBCommon.SEPARATOR;
        StringBuilder encodedResult = new StringBuilder();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Start separator
        encodedResult.append(separator);

        // INV Code
        encodedResult.append(arInvoice.getInvCode());
        encodedResult.append(separator);

        // Description
        encodedResult.append(arInvoice.getInvDescription());
        encodedResult.append(separator);

        // INV Date
        encodedResult.append(sdf.format(arInvoice.getInvDate()));
        encodedResult.append(separator);

        // INV Number
        encodedResult.append(arInvoice.getInvNumber());
        encodedResult.append(separator);

        // Reference Number
        encodedResult.append(arInvoice.getInvReferenceNumber());
        encodedResult.append(separator);

        // Amount Due
        encodedResult.append(arInvoice.getInvAmountDue());
        encodedResult.append(separator);

        // Amount Paid
        encodedResult.append(arInvoice.getInvAmountPaid());
        encodedResult.append(separator);

        // Conversion Date
        encodedResult.append(arInvoice.getInvConversionDate());
        encodedResult.append(separator);

        // Conversion Rate
        encodedResult.append(arInvoice.getInvConversionRate());
        encodedResult.append(separator);

        //INV_BLL_TO_ADDRSS
        encodedResult.append(arInvoice.getInvBillToAddress());
        encodedResult.append(separator);

        //INV_BLL_TO_CNTCT
        encodedResult.append(arInvoice.getInvBillToContact());
        encodedResult.append(separator);

        //INV_BLL_TO_ALT_CNTCT
        encodedResult.append(arInvoice.getInvBillToAltContact());
        encodedResult.append(separator);

        //INV_BLL_TO_PHN
        encodedResult.append(arInvoice.getInvBillToPhone());
        encodedResult.append(separator);

        //INV_BLLNG_HDR
        encodedResult.append(arInvoice.getInvBillingHeader());
        encodedResult.append(separator);

        //INV_BLLNG_FTR
        encodedResult.append(arInvoice.getInvBillingFooter());
        encodedResult.append(separator);

        //INV_BLLNG_HDR2
        encodedResult.append(arInvoice.getInvBillingHeader2());
        encodedResult.append(separator);

        //INV_BLLNG_FTR2
        encodedResult.append(arInvoice.getInvBillingFooter2());
        encodedResult.append(separator);

        //INV_BLLNG_HDR3
        encodedResult.append(arInvoice.getInvBillingHeader3());
        encodedResult.append(separator);

        //INV_BLLNG_FTR3
        encodedResult.append(arInvoice.getInvBillingFooter3());
        encodedResult.append(separator);

        //INV_BLLNG_SGNTRY
        encodedResult.append(arInvoice.getInvBillingSignatory());
        encodedResult.append(separator);

        //INV_SGNTRY_TTL
        encodedResult.append(arInvoice.getInvSignatoryTitle());
        encodedResult.append(separator);

        //INV_SHP_TO_ADDRSS
        encodedResult.append(arInvoice.getInvShipToAddress());
        encodedResult.append(separator);

        //INV_SHP_TO_CNTCT
        encodedResult.append(arInvoice.getInvShipToContact());
        encodedResult.append(separator);

        //INV_SHP_TO_ALT_CNTCT
        encodedResult.append(arInvoice.getInvShipToAltContact());
        encodedResult.append(separator);

        //INV_SHP_TO_PHN
        encodedResult.append(arInvoice.getInvShipToPhone());
        encodedResult.append(separator);

        //INV_SHP_DT
        encodedResult.append(arInvoice.getInvShipDate());
        encodedResult.append(separator);

        //INV_LV_FRGHT
        encodedResult.append(arInvoice.getInvLvFreight());
        encodedResult.append(separator);

        //INV_LV_SHFT
        encodedResult.append(arInvoice.getInvLvShift());
        encodedResult.append(separator);

        //INV_SO_NMBR
        encodedResult.append(arInvoice.getInvSoNumber());
        encodedResult.append(separator);

        //INV_CLNT_PO
        encodedResult.append(arInvoice.getInvClientPO());
        encodedResult.append(separator);

        //INV_EFFCTVTY_DT
        encodedResult.append(arInvoice.getInvEffectivityDate());
        encodedResult.append(separator);

        //GL_FUNCTIONAL_CURRENCY
        encodedResult.append(arInvoice.getGlFunctionalCurrency().getFcName());
        encodedResult.append(separator);

        //AR_TAX_CODE
        encodedResult.append(arInvoice.getArTaxCode().getTcName());
        encodedResult.append(separator);

        //AR_WITHHOLDING_TAX_CODE
        encodedResult.append(arInvoice.getArWithholdingTaxCode().getWtcName());
        encodedResult.append(separator);

        //AR_CUSTOMER
        encodedResult.append(arInvoice.getArCustomer().getCstCustomerCode());
        encodedResult.append(separator);

        //AD_PAYMENT_TERM
        encodedResult.append(arInvoice.getAdPaymentTerm().getPytName());
        encodedResult.append(separator);

        // end separator
        encodedResult.append(separator);

        String lineSeparator = "~";

        // begin lineSeparator
        encodedResult.append(lineSeparator);

        // Append Sales Order Invoice Lines
        Iterator solIter = arInvoice.getArSalesOrderInvoiceLines().iterator();

        while (solIter.hasNext()) {

            LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) solIter.next();

            // begin separator
            encodedResult.append(separator);

            // Line Code
            encodedResult.append(arSalesOrderInvoiceLine.getSilCode());
            encodedResult.append(separator);

            // Quantity Remaining
            encodedResult.append(arSalesOrderInvoiceLine.getArSalesOrderLine().getSolQuantity());
            encodedResult.append(separator);

            // Quantity Delivered
            encodedResult.append(arSalesOrderInvoiceLine.getSilQuantityDelivered());
            encodedResult.append(separator);

            // Unit Price
            encodedResult.append(arSalesOrderInvoiceLine.getArSalesOrderLine().getSolUnitPrice());
            encodedResult.append(separator);

            // Amount
            encodedResult.append(arSalesOrderInvoiceLine.getSilAmount());
            encodedResult.append(separator);

            // Discount 1
            encodedResult.append(arSalesOrderInvoiceLine.getSilDiscount1());
            encodedResult.append(separator);

            // Discount 2
            encodedResult.append(arSalesOrderInvoiceLine.getSilDiscount2());
            encodedResult.append(separator);

            // Discount 3
            encodedResult.append(arSalesOrderInvoiceLine.getSilDiscount3());
            encodedResult.append(separator);

            // Discount 4
            encodedResult.append(arSalesOrderInvoiceLine.getSilDiscount4());
            encodedResult.append(separator);


            // Total Discount
            encodedResult.append(arSalesOrderInvoiceLine.getSilTotalDiscount());
            encodedResult.append(separator);

            // Unit of Measure
            encodedResult.append(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvUnitOfMeasure().getUomName());
            encodedResult.append(separator);

            // Item Name
            encodedResult.append(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvItem().getIiName());
            encodedResult.append(separator);

            // Location Name
            encodedResult.append(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvLocation().getLocName());
            encodedResult.append(separator);

            // begin lineSeparator
            encodedResult.append(lineSeparator);
        }

        // Append Sales Order Invoice Lines
        Iterator iliIter = arInvoice.getArInvoiceLineItems().iterator();

        while (iliIter.hasNext()) {

            LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) iliIter.next();

            // begin separator
            encodedResult.append(separator);

            // Line Code
            encodedResult.append(arInvoiceLineItem.getIliCode());
            encodedResult.append(separator);

            // Quantity Remaining
            encodedResult.append(arInvoiceLineItem.getIliQuantity());
            encodedResult.append(separator);

            // Quantity Delivered
            encodedResult.append("0");
            encodedResult.append(separator);

            // Unit Price
            encodedResult.append(arInvoiceLineItem.getIliUnitPrice());
            encodedResult.append(separator);

            // Amount
            encodedResult.append(arInvoiceLineItem.getIliAmount());
            encodedResult.append(separator);

            // Discount 1
            encodedResult.append(arInvoiceLineItem.getIliDiscount1());
            encodedResult.append(separator);

            // Discount 2
            encodedResult.append(arInvoiceLineItem.getIliDiscount1());
            encodedResult.append(separator);

            // Discount 3
            encodedResult.append(arInvoiceLineItem.getIliDiscount1());
            encodedResult.append(separator);

            // Discount 4
            encodedResult.append(arInvoiceLineItem.getIliDiscount1());
            encodedResult.append(separator);


            // Total Discount
            encodedResult.append(arInvoiceLineItem.getIliTotalDiscount());
            encodedResult.append(separator);

            // Unit of Measure
            encodedResult.append(arInvoiceLineItem.getInvUnitOfMeasure().getUomName());
            encodedResult.append(separator);

            // Item Name
            encodedResult.append(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
            encodedResult.append(separator);

            // Location Name
            encodedResult.append(arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName());
            encodedResult.append(separator);

            // begin lineSeparator
            encodedResult.append(lineSeparator);
        }

        // Append Distribution Records
        encodedResult.append("$JOURNAL$");

        Iterator drIter = arInvoice.getArDistributionRecords().iterator();

        while (drIter.hasNext()) {

            LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) drIter.next();

            // begin separator
            encodedResult.append(separator);

            // Dr Code
            encodedResult.append(arDistributionRecord.getDrCode());
            encodedResult.append(separator);

            // Dr Line
            encodedResult.append(arDistributionRecord.getDrLine());
            encodedResult.append(separator);

            // Dr Class
            encodedResult.append(arDistributionRecord.getDrClass());
            encodedResult.append(separator);

            // Dr Debit
            encodedResult.append(arDistributionRecord.getDrDebit());
            encodedResult.append(separator);

            // Dr Amount
            encodedResult.append(arDistributionRecord.getDrAmount());
            encodedResult.append(separator);

            // Dr COA
            encodedResult.append(arDistributionRecord.getGlChartOfAccount().getCoaCode());
            encodedResult.append(separator);

            // Dr Invoice
            encodedResult.append(arInvoice.getInvCode());
            encodedResult.append(separator);

            // begin lineSeparator
            encodedResult.append(lineSeparator);
        }

        return encodedResult.toString();

    }

    private InvModAdjustmentDetails adjRowDecode(String adj) {

        String separator = "$";
        InvModAdjustmentDetails invAdjustmentDetails = new InvModAdjustmentDetails();

        // Remove first $ character
        adj = adj.substring(1);

        // ADJ Code
        int start = 0;
        int nextIndex = adj.indexOf(separator, start);
        int length = nextIndex - start;
        invAdjustmentDetails.setAdjCode(Integer.parseInt(adj.substring(start, start + length)));

        // ADJ Type
        start = nextIndex + 1;
        nextIndex = adj.indexOf(separator, start);
        length = nextIndex - start;
        invAdjustmentDetails.setAdjType(adj.substring(start, start + length));

        // Document No
        start = nextIndex + 1;
        nextIndex = adj.indexOf(separator, start);
        length = nextIndex - start;
        //invAdjustmentDetails.setPoDocumentNumber(adj.substring(start, start + length));
        invAdjustmentDetails.setAdjDocumentNumber("");

        // Reference No
        start = nextIndex + 1;
        nextIndex = adj.indexOf(separator, start);
        length = nextIndex - start;
        invAdjustmentDetails.setAdjReferenceNumber(adj.substring(start, start + length));

        // PO Date
        start = nextIndex + 1;
        nextIndex = adj.indexOf(separator, start);
        length = nextIndex - start;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        sdf.setLenient(false);
        try {
            invAdjustmentDetails.setAdjDate(sdf.parse(adj.substring(start, start + length)));
            System.out.println("Date-" + invAdjustmentDetails.getAdjDate());
        }
        catch (Exception ex) {
            System.out.println("Error On PO Date: " + ex.getMessage());
            //throw ex;
        }

        // Description
        start = nextIndex + 1;
        nextIndex = adj.indexOf(separator, start);
        length = nextIndex - start;
        invAdjustmentDetails.setAdjDescription(adj.substring(start, start + length));

        /************************** Misc Details***************************/

        // Created By
        start = nextIndex + 1;
        nextIndex = adj.indexOf(separator, start);
        length = nextIndex - start;
        invAdjustmentDetails.setAdjCreatedBy(adj.substring(start, start + length));

        // Date Created
        start = nextIndex + 1;
        nextIndex = adj.indexOf(separator, start);
        length = nextIndex - start;
        try {
            invAdjustmentDetails.setAdjDateCreated(sdf.parse(adj.substring(start, start + length)));
            System.out.println("Date Created-" + invAdjustmentDetails.getAdjDateCreated());
        }
        catch (Exception ex) {
            System.out.println("Error On Date Created");
            //throw ex;
        }

        // Last Modified By
        start = nextIndex + 1;
        nextIndex = adj.indexOf(separator, start);
        length = nextIndex - start;
        invAdjustmentDetails.setAdjLastModifiedBy(adj.substring(start, start + length));

        // Date Last Modified
        start = nextIndex + 1;
        nextIndex = adj.indexOf(separator, start);
        length = nextIndex - start;
        try {
            invAdjustmentDetails.setAdjDateLastModified(sdf.parse(adj.substring(start, start + length)));
            System.out.println("Date Last Modified-" + invAdjustmentDetails.getAdjDateLastModified());
        }
        catch (Exception ex) {
            System.out.println("Error On Date Last Modified");
            //throw ex;
        }

        // COA ACCOUNT NO
        start = nextIndex + 1;
        nextIndex = adj.indexOf(separator, start);
        length = nextIndex - start;
        invAdjustmentDetails.setAdjCoaAccountNumber(adj.substring(start, start + length));

        // end separator
        start = nextIndex + 1;
        nextIndex = adj.indexOf(separator, start);
        length = nextIndex - start;

        String lineSeparator = "~";

        // begin lineSeparator
        start = nextIndex + 1;
        nextIndex = adj.indexOf(lineSeparator, start);
        length = nextIndex - start;

        invAdjustmentDetails.setAdjAlList(new ArrayList());

        while (true) {
            InvModAdjustmentLineDetails invModAdjustmentLineDetails = new InvModAdjustmentLineDetails();

            // begin separator
            start = nextIndex + 1;
            nextIndex = adj.indexOf(separator, start);
            length = nextIndex - start;

            // Unit Cost
            start = nextIndex + 1;
            nextIndex = adj.indexOf(separator, start);
            length = nextIndex - start;
            invModAdjustmentLineDetails.setAlUnitCost((double) Double.parseDouble(adj.substring(start, start + length)));
            System.out.println("Unit Cost: " + invModAdjustmentLineDetails.getAlUnitCost());

            // Adj Quantity
            start = nextIndex + 1;
            nextIndex = adj.indexOf(separator, start);
            length = nextIndex - start;
            invModAdjustmentLineDetails.setAlAdjustQuantity((double) Double.parseDouble(adj.substring(start, start + length)));

            // Unit of Measure
            start = nextIndex + 1;
            nextIndex = adj.indexOf(separator, start);
            length = nextIndex - start;
            invModAdjustmentLineDetails.setAlUomName(adj.substring(start, start + length));

            // Item
            start = nextIndex + 1;
            nextIndex = adj.indexOf(separator, start);
            length = nextIndex - start;
            invModAdjustmentLineDetails.setAlIiName(adj.substring(start, start + length));

            // Location
            start = nextIndex + 1;
            nextIndex = adj.indexOf(separator, start);
            length = nextIndex - start;
            invModAdjustmentLineDetails.setAlLocName(adj.substring(start, start + length));

            System.out.println("item Location: " + invModAdjustmentLineDetails.getAlIiName() + " " + invModAdjustmentLineDetails.getAlLocName());

            // begin lineSeparator
            start = nextIndex + 1;
            nextIndex = adj.indexOf(lineSeparator, start);
            length = nextIndex - start;

            invAdjustmentDetails.getAdjAlList().add(invModAdjustmentLineDetails);

            int tempStart = nextIndex + 1;
            if (adj.indexOf(separator, tempStart) == -1) {
                break;
            }

        }

        return invAdjustmentDetails;


    }

    private ArModInvoiceDetails cmRowDecode(String mrcm) {

        Debug.print("InvItemSyncControllerBean cmRowDecode");

        String separator = "$";
        ArModInvoiceDetails arCmDetails = new ArModInvoiceDetails();

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_OUTPUT);
        sdf.setLenient(false);

        // Remove first $ character
        mrcm = mrcm.substring(1);

        // CM DESC
        int start = 0;
        int nextIndex = mrcm.indexOf(separator, start);
        int length = nextIndex - start;
        arCmDetails.setInvDescription(mrcm.substring(start, start + length));

        // CM DATE
        start = nextIndex + 1;
        nextIndex = mrcm.indexOf(separator, start);
        length = nextIndex - start;
        try {

            arCmDetails.setInvDate(sdf.parse(mrcm.substring(start, start + length)));

        }
        catch (Exception ex) {

            //throw ex;
        }

        // CM NUMBER
        start = nextIndex + 1;
        nextIndex = mrcm.indexOf(separator, start);
        length = nextIndex - start;
        arCmDetails.setInvNumber(mrcm.substring(start, start + length));

        // CM REF NUMBER
        start = nextIndex + 1;
        nextIndex = mrcm.indexOf(separator, start);
        length = nextIndex - start;
        arCmDetails.setInvReferenceNumber(mrcm.substring(start, start + length));

        // CM INVOICE NUMBER
        start = nextIndex + 1;
        nextIndex = mrcm.indexOf(separator, start);
        length = nextIndex - start;
        arCmDetails.setInvCmInvoiceNumber(mrcm.substring(start, start + length));

        // CM AMOUNT DUE
        start = nextIndex + 1;
        nextIndex = mrcm.indexOf(separator, start);
        length = nextIndex - start;
        arCmDetails.setInvAmountDue(Double.parseDouble(mrcm.substring(start, start + length)));

        // CM CREATED BY
        start = nextIndex + 1;
        nextIndex = mrcm.indexOf(separator, start);
        length = nextIndex - start;
        arCmDetails.setInvCreatedBy(mrcm.substring(start, start + length));

        // CM BATCH
        start = nextIndex + 1;
        nextIndex = mrcm.indexOf(separator, start);
        length = nextIndex - start;
        arCmDetails.setInvIbName(mrcm.substring(start, start + length));

        // end separator
        start = nextIndex + 1;
        nextIndex = mrcm.indexOf(separator, start);
        length = nextIndex - start;

        arCmDetails.setInvLastModifiedBy(arCmDetails.getInvCreatedBy());
        arCmDetails.setInvDateCreated(new Date());
        arCmDetails.setInvDateLastModified(arCmDetails.getInvDateCreated());

        return arCmDetails;


    }

    private ArModInvoiceDetails scRowDecode(String sc) {

        Debug.print("InvItemSyncControllerBean scRowDecode");

        String separator = "$";
        String lineSeparator = "~";
        ArModInvoiceDetails scDetails = new ArModInvoiceDetails();

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_OUTPUT);
        sdf.setLenient(false);

        // Remove first $ character
        sc = sc.substring(1);

        // INV_DATE
        int start = 0;
        int nextIndex = sc.indexOf(separator, start);
        int length = nextIndex - start;
        try {
            scDetails.setInvDate(sdf.parse(sc.substring(start, start + length)));
        }
        catch (Exception ex) {
        }

        // INV_NUMBER
        start = nextIndex + 1;
        nextIndex = sc.indexOf(separator, start);
        length = nextIndex - start;
        scDetails.setInvNumber(sc.substring(start, start + length));

        // INV_REFERENCE
        start = nextIndex + 1;
        nextIndex = sc.indexOf(separator, start);
        length = nextIndex - start;
        scDetails.setInvReferenceNumber(sc.substring(start, start + length));

        // INV_DESC
        start = nextIndex + 1;
        nextIndex = sc.indexOf(separator, start);
        length = nextIndex - start;
        scDetails.setInvDescription(sc.substring(start, start + length));

        // AR_CUSTOMER
        start = nextIndex + 1;
        nextIndex = sc.indexOf(separator, start);
        length = nextIndex - start;
        scDetails.setInvCstCustomerCode(sc.substring(start, start + length));

        // TAX_CODE
        start = nextIndex + 1;
        nextIndex = sc.indexOf(separator, start);
        length = nextIndex - start;
        scDetails.setInvTcName(sc.substring(start, start + length));

        scDetails.setInvIlList(new ArrayList());

        while (true) {

            int tempStart = nextIndex + 1;
            if (sc.indexOf(separator, tempStart) == -1) {
                break;
            }

            ArModInvoiceLineDetails mInvDetails = new ArModInvoiceLineDetails();

            // begin lineSeparator
            start = nextIndex + 1;
            nextIndex = sc.indexOf(lineSeparator, start);
            length = nextIndex - start;

            // begin separator
            start = nextIndex + 1;
            nextIndex = sc.indexOf(separator, start);
            length = nextIndex - start;

            // IL_DESC
            start = nextIndex + 1;
            nextIndex = sc.indexOf(separator, start);
            length = nextIndex - start;
            System.out.println(" IL_DESC: " + sc.substring(start, start + length));
            mInvDetails.setIlDescription(sc.substring(start, start + length));

            // IL_QNTTY
            start = nextIndex + 1;
            nextIndex = sc.indexOf(separator, start);
            length = nextIndex - start;
            System.out.println("IL_QNTTY: " + sc.substring(start, start + length));
            mInvDetails.setIlQuantity((double) Double.parseDouble(sc.substring(start, start + length)));

            // IL_UNT_PRC
            start = nextIndex + 1;
            nextIndex = sc.indexOf(separator, start);
            length = nextIndex - start;
            System.out.println("IL_UNT_PRC: " + sc.substring(start, start + length));
            mInvDetails.setIlUnitPrice((double) Double.parseDouble(sc.substring(start, start + length)));

            mInvDetails.setIlAmount(mInvDetails.getIlQuantity() * mInvDetails.getIlUnitPrice());

            // AR_STANDARD_MEMO_LINE
            start = nextIndex + 1;
            nextIndex = sc.indexOf(separator, start);
            length = nextIndex - start;
            System.out.println(" AR_STANDARD_MEMO_LINE: " + sc.substring(start, start + length));
            mInvDetails.setIlSmlName(sc.substring(start, start + length));

            scDetails.getInvIlList().add(mInvDetails);

            tempStart = nextIndex + 1;
            if (sc.indexOf(separator, tempStart) == -1) {
                break;
            }
        }

        return scDetails;
    }

    private HashMap<String, Object> getArMemoLineAmount(HashMap<String, Object> arMemoLineAmountMap, String ArInvoiceNumber, Integer AD_BRNCH, Integer AD_CMPNY) {

        try {

            ArrayList arStandardMemoLineList = new ArrayList();
            ArrayList arStandardMemoLineCoaList = new ArrayList();

            for (Object o : arStandardMemoLineHome.findEnabledSmlAll(AD_BRNCH, AD_CMPNY)) {
                LocalArStandardMemoLine arStandardMemoLine = (LocalArStandardMemoLine) o;
                arStandardMemoLineList.add(arStandardMemoLine.getSmlName());
                arStandardMemoLineCoaList.add(arStandardMemoLine.getGlChartOfAccount().getCoaCode());

            }

            LocalArInvoice arInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(ArInvoiceNumber,
                    EJBCommon.FALSE, AD_BRNCH, AD_CMPNY);

            Iterator i = arInvoice.getArInvoiceLineItems().iterator();
            while (i.hasNext()) {
                LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) i.next();
                LocalAdBranchItemLocation adBranchItemLocation = null;
                try {
                    adBranchItemLocation = adBranchItemLocationHome
                            .findBilByIlCodeAndBrCode(arInvoiceLineItem.getInvItemLocation().getIlCode(), AD_BRNCH, AD_CMPNY);
                }
                catch (FinderException ex) {

                }

                Integer COA_CODE;
                String SML_NAME;

                if (adBranchItemLocation != null) {
                    COA_CODE = adBranchItemLocation.getBilCoaGlSalesAccount();
                } else {
                    COA_CODE = arInvoiceLineItem.getInvItemLocation().getIlGlCoaSalesAccount();
                }

                SML_NAME = (arStandardMemoLineList.toArray()[arStandardMemoLineCoaList.indexOf(COA_CODE)]).toString();

                if (arMemoLineAmountMap.containsKey(SML_NAME)) {
                    double iliAmount = Double.parseDouble(arMemoLineAmountMap.get(SML_NAME).toString());
                    arMemoLineAmountMap.remove(SML_NAME);
                    arMemoLineAmountMap.put(SML_NAME, arInvoiceLineItem.getIliAmount() + iliAmount);
                } else {
                    arMemoLineAmountMap.put(SML_NAME, arInvoiceLineItem.getIliAmount());
                }
            }

            i = arInvoice.getArSalesOrderInvoiceLines().iterator();
            while (i.hasNext()) {
                LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) i.next();
                LocalAdBranchItemLocation adBranchItemLocation = null;
                try {
                    adBranchItemLocation = adBranchItemLocationHome
                            .findBilByIlCodeAndBrCode(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getIlCode(), AD_BRNCH, AD_CMPNY);
                }
                catch (FinderException ex) {

                }

                Integer COA_CODE;
                String SML_NAME;
                if (adBranchItemLocation != null) {
                    COA_CODE = adBranchItemLocation.getBilCoaGlSalesAccount();
                } else {
                    COA_CODE = arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getIlGlCoaSalesAccount();
                }

                SML_NAME = (arStandardMemoLineList.toArray()[arStandardMemoLineCoaList.indexOf(COA_CODE)]).toString();
                if (arMemoLineAmountMap.containsKey(SML_NAME)) {
                    double iliAmount = Double.parseDouble(arMemoLineAmountMap.get(SML_NAME).toString());
                    arMemoLineAmountMap.remove(SML_NAME);
                    arMemoLineAmountMap.put(SML_NAME, arSalesOrderInvoiceLine.getSilAmount() + iliAmount);
                } else {
                    arMemoLineAmountMap.put(SML_NAME, arSalesOrderInvoiceLine.getSilAmount());
                }
            }
            return arMemoLineAmountMap;
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double getDRAmount(HashMap arMemoLineAmountMap) {

        double drTotalAmount = 0;

        Iterator smlIter = arMemoLineAmountMap.keySet().iterator();

        while (smlIter.hasNext()) {

            String smlName = smlIter.next().toString();
            drTotalAmount += (double) Double.parseDouble(arMemoLineAmountMap.get(smlName).toString());

        }

        return drTotalAmount;
    }

    private ApModPurchaseOrderDetails poRowDecode(String po) {

        Debug.print("InvItemSyncControllerBean poRowDecode");

        String separator = "$";
        ApModPurchaseOrderDetails apModPurchaseOrderDetails = new ApModPurchaseOrderDetails();

        // Remove first $ character
        po = po.substring(1);

        // PO Code
        int start = 0;
        int nextIndex = po.indexOf(separator, start);
        int length = nextIndex - start;
        System.out.print("PO CODE:" + (po.substring(start, start + length)));
        apModPurchaseOrderDetails.setPoCode(Integer.parseInt(po.substring(start, start + length)));

        // PO Type
        start = nextIndex + 1;
        nextIndex = po.indexOf(separator, start);
        length = nextIndex - start;
        System.out.print("PO TYPE:" + (po.substring(start, start + length)));
        apModPurchaseOrderDetails.setPoType(po.substring(start, start + length));

        // Currency
        start = nextIndex + 1;
        nextIndex = po.indexOf(separator, start);
        length = nextIndex - start;
        System.out.print("PO CURRENCY:" + (po.substring(start, start + length)));
        apModPurchaseOrderDetails.setPoFcName(po.substring(start, start + length));

        // Tax Code
        start = nextIndex + 1;
        nextIndex = po.indexOf(separator, start);
        length = nextIndex - start;
        System.out.print("PO TAXCODE:" + (po.substring(start, start + length)));
        apModPurchaseOrderDetails.setPoTcName(po.substring(start, start + length));

        // Supplier Code
        start = nextIndex + 1;
        nextIndex = po.indexOf(separator, start);
        length = nextIndex - start;
        System.out.print("PO SPLSUPPLIERCODE:" + (po.substring(start, start + length)));
        apModPurchaseOrderDetails.setPoSplSupplierCode(po.substring(start, start + length));

        // Payment Term
        start = nextIndex + 1;
        nextIndex = po.indexOf(separator, start);
        length = nextIndex - start;
        System.out.print("PO PAYMENTTERM:" + (po.substring(start, start + length)));
        apModPurchaseOrderDetails.setPoPytName(po.substring(start, start + length));

        // Document No
        start = nextIndex + 1;
        nextIndex = po.indexOf(separator, start);
        length = nextIndex - start;
        System.out.print("PO DOC#:" + (po.substring(start, start + length)));
        //apModPurchaseOrderDetails.setPoDocumentNumber(po.substring(start, start + length));
        apModPurchaseOrderDetails.setPoDocumentNumber("");

        // Po Receiving PO No
        start = nextIndex + 1;
        nextIndex = po.indexOf(separator, start);
        length = nextIndex - start;
        System.out.print("PO RCVPO#:" + (po.substring(start, start + length)));
        apModPurchaseOrderDetails.setPoRcvPoNumber(po.substring(start, start + length));

        // Reference No
        start = nextIndex + 1;
        nextIndex = po.indexOf(separator, start);
        length = nextIndex - start;
        System.out.print("PO REF#:" + (po.substring(start, start + length)));
        apModPurchaseOrderDetails.setPoReferenceNumber(po.substring(start, start + length));

        // PO Date
        start = nextIndex + 1;
        nextIndex = po.indexOf(separator, start);
        length = nextIndex - start;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        sdf.setLenient(false);
        try {
            System.out.print("PO DATE:" + (po.substring(start, start + length)));
            apModPurchaseOrderDetails.setPoDate(sdf.parse(po.substring(start, start + length)));
            System.out.println("Date-" + apModPurchaseOrderDetails.getPoDate());
        }
        catch (Exception ex) {
            System.out.println("Error On PO Date: " + ex.getMessage());
            //throw ex;
        }

        // Description
        start = nextIndex + 1;
        nextIndex = po.indexOf(separator, start);
        length = nextIndex - start;
        System.out.print("PO DESC:" + (po.substring(start, start + length)));
        apModPurchaseOrderDetails.setPoDescription(po.substring(start, start + length));

        // Conversion Rate
        start = nextIndex + 1;
        nextIndex = po.indexOf(separator, start);
        length = nextIndex - start;
        System.out.print("PO CONVRATE:" + (po.substring(start, start + length)));
        apModPurchaseOrderDetails.setPoConversionRate(Double.parseDouble(po.substring(start, start + length)));

        // Conversion Date
        start = nextIndex + 1;
        nextIndex = po.indexOf(separator, start);
        length = nextIndex - start;
        try {
            //apModPurchaseOrderDetails.setPoConversionDate(sdf.parse(po.substring(start, start + length)));
            System.out.println("Conversion Date-" + apModPurchaseOrderDetails.getPoConversionDate());
        }
        catch (Exception ex) {
            System.out.println("Error On Conversion Date");
            //throw ex;
        }


        /************************** Misc Details***************************/

        // Created By
        start = nextIndex + 1;
        nextIndex = po.indexOf(separator, start);
        length = nextIndex - start;
        System.out.print("PO CREATEDBY:" + (po.substring(start, start + length)));
        apModPurchaseOrderDetails.setPoCreatedBy(po.substring(start, start + length));

        // Date Created
        start = nextIndex + 1;
        nextIndex = po.indexOf(separator, start);
        length = nextIndex - start;
        try {
            System.out.print("PO DATECREATED:" + (po.substring(start, start + length)));
            apModPurchaseOrderDetails.setPoDateCreated(sdf.parse(po.substring(start, start + length)));
            System.out.println("Date Created-" + apModPurchaseOrderDetails.getPoDateCreated());
        }
        catch (Exception ex) {
            System.out.println("Error On Date Created");
            //throw ex;
        }

        // Last Modified By
        start = nextIndex + 1;
        nextIndex = po.indexOf(separator, start);
        length = nextIndex - start;
        System.out.print("PO LASTMODIFIEDBY:" + (po.substring(start, start + length)));
        apModPurchaseOrderDetails.setPoLastModifiedBy(po.substring(start, start + length));

        // Date Last Modified
        start = nextIndex + 1;
        nextIndex = po.indexOf(separator, start);
        length = nextIndex - start;
        try {
            System.out.print("PO DATELASTMODIFIED:" + (po.substring(start, start + length)));
            apModPurchaseOrderDetails.setPoDateLastModified(sdf.parse(po.substring(start, start + length)));
            System.out.println("Date Last Modified-" + apModPurchaseOrderDetails.getPoDateLastModified());
        }
        catch (Exception ex) {
            System.out.println("Error On Date Last Modified");
            //throw ex;
        }

        // end separator
        start = nextIndex + 1;
        nextIndex = po.indexOf(separator, start);
        length = nextIndex - start;

        String lineSeparator = "~";

        // begin lineSeparator
        start = nextIndex + 1;
        nextIndex = po.indexOf(lineSeparator, start);
        length = nextIndex - start;

        apModPurchaseOrderDetails.setPoPlList(new ArrayList());

        while (true) {
            ApModPurchaseOrderLineDetails apModPurchaseOrderLineDetails = new ApModPurchaseOrderLineDetails();

            // begin separator
            start = nextIndex + 1;
            nextIndex = po.indexOf(separator, start);
            length = nextIndex - start;

            // Line Code
            start = nextIndex + 1;
            nextIndex = po.indexOf(separator, start);
            length = nextIndex - start;
            System.out.print("PL CODE:" + (po.substring(start, start + length)));
            apModPurchaseOrderLineDetails.setPlCode(Integer.parseInt(po.substring(start, start + length)));

            // Line Number
            start = nextIndex + 1;
            nextIndex = po.indexOf(separator, start);
            length = nextIndex - start;
            System.out.print("PL LINE:" + (po.substring(start, start + length)));
            apModPurchaseOrderLineDetails.setPlLine((Short.parseShort(po.substring(start, start + length))));

            // Item Name
            start = nextIndex + 1;
            nextIndex = po.indexOf(separator, start);
            length = nextIndex - start;
            System.out.print("PL ITEMNAME:" + (po.substring(start, start + length)));
            apModPurchaseOrderLineDetails.setPlIiName(po.substring(start, start + length));

            // Location Name
            start = nextIndex + 1;
            nextIndex = po.indexOf(separator, start);
            length = nextIndex - start;
            System.out.print("PL LOCNAME:" + (po.substring(start, start + length)));
            apModPurchaseOrderLineDetails.setPlLocName(po.substring(start, start + length));

            // Quantity
            start = nextIndex + 1;
            nextIndex = po.indexOf(separator, start);
            length = nextIndex - start;
            System.out.print("PL QTY:" + (po.substring(start, start + length)));
            apModPurchaseOrderLineDetails.setPlQuantity((double) Double.parseDouble(po.substring(start, start + length)));

            // Unit of Measure
            start = nextIndex + 1;
            nextIndex = po.indexOf(separator, start);
            length = nextIndex - start;
            System.out.print("PL UOMNAME:" + (po.substring(start, start + length)));
            apModPurchaseOrderLineDetails.setPlUomName(po.substring(start, start + length));

            // Unit Cost
            start = nextIndex + 1;
            nextIndex = po.indexOf(separator, start);
            length = nextIndex - start;
            System.out.print("PL UNITCOST:" + (po.substring(start, start + length)));
            apModPurchaseOrderLineDetails.setPlUnitCost((double) Double.parseDouble(po.substring(start, start + length)));

            // Amount
            start = nextIndex + 1;
            nextIndex = po.indexOf(separator, start);
            length = nextIndex - start;
            System.out.print("PL AMOUNT:" + (po.substring(start, start + length)));
            apModPurchaseOrderLineDetails.setPlAmount((double) Double.parseDouble(po.substring(start, start + length)));

            // PL PL CODE
            start = nextIndex + 1;
            nextIndex = po.indexOf(separator, start);
            length = nextIndex - start;
            System.out.print("PL PLCODE:" + (po.substring(start, start + length)));
            apModPurchaseOrderLineDetails.setPlPlCode(Integer.parseInt(po.substring(start, start + length)));

            // Discount 1
            start = nextIndex + 1;
            nextIndex = po.indexOf(separator, start);
            length = nextIndex - start;
            System.out.print("PL DISCOUNT1:" + (po.substring(start, start + length)));
            apModPurchaseOrderLineDetails.setPlDiscount1((double) Double.parseDouble(po.substring(start, start + length)));

            // Discount 2
            start = nextIndex + 1;
            nextIndex = po.indexOf(separator, start);
            length = nextIndex - start;
            System.out.print("PL DISCOUNT2:" + (po.substring(start, start + length)));
            apModPurchaseOrderLineDetails.setPlDiscount2((double) Double.parseDouble(po.substring(start, start + length)));

            // Discount 3
            start = nextIndex + 1;
            nextIndex = po.indexOf(separator, start);
            length = nextIndex - start;
            System.out.print("PL DISCOUNT3:" + (po.substring(start, start + length)));
            apModPurchaseOrderLineDetails.setPlDiscount3((double) Double.parseDouble(po.substring(start, start + length)));

            // Discount 4
            start = nextIndex + 1;
            nextIndex = po.indexOf(separator, start);
            length = nextIndex - start;
            System.out.print("PL DISCOUNT4:" + (po.substring(start, start + length)));
            apModPurchaseOrderLineDetails.setPlDiscount4((double) Double.parseDouble(po.substring(start, start + length)));

            // Total Discount
            start = nextIndex + 1;
            nextIndex = po.indexOf(separator, start);
            length = nextIndex - start;
            System.out.print("PL TOTALDISCOUNT:" + (po.substring(start, start + length)));
            apModPurchaseOrderLineDetails.setPlTotalDiscount((double) Double.parseDouble(po.substring(start, start + length)));

            // begin lineSeparator
            start = nextIndex + 1;
            nextIndex = po.indexOf(lineSeparator, start);
            length = nextIndex - start;

            apModPurchaseOrderDetails.getPoPlList().add(apModPurchaseOrderLineDetails);

            int tempStart = nextIndex + 1;
            if (po.indexOf(separator, tempStart) == -1) {
                break;
            }

        }

        return apModPurchaseOrderDetails;

    }

    private InvModBranchStockTransferDetails bstRowDecode(String bst) {

        //Debug.print("InvItemSyncControllerBean adjRowDecode");

        String separator = "$";
        InvModBranchStockTransferDetails invBranchStockTransferDetails = new InvModBranchStockTransferDetails();

        // Remove first $ character
        bst = bst.substring(1);

        // bst number
        int start = 0;
        int nextIndex = bst.indexOf(separator, start);
        int length = nextIndex - start;
        invBranchStockTransferDetails.setBstNumber(bst.substring(start, start + length));
        //invBranchStockTransferDetails.setBstCode(new Integer(bst.substring(start, start + length)));

        // BST Date
        start = nextIndex + 1;
        nextIndex = bst.indexOf(separator, start);
        length = nextIndex - start;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        sdf.setLenient(false);
        try {
            invBranchStockTransferDetails.setBstDate(sdf.parse(bst.substring(start, start + length)));
            System.out.println("Date-" + invBranchStockTransferDetails.getBstDate());
        }
        catch (Exception ex) {
            System.out.println("Error On PO Date: " + ex.getMessage());
            //throw ex;
        }

        // BST Type
        start = nextIndex + 1;
        nextIndex = bst.indexOf(separator, start);
        length = nextIndex - start;
        invBranchStockTransferDetails.setBstType(bst.substring(start, start + length));

        // BST Transfer Out Number
        start = nextIndex + 1;
        nextIndex = bst.indexOf(separator, start);
        length = nextIndex - start;
        invBranchStockTransferDetails.setBstTransferOutNumber(bst.substring(start, start + length));

        // Description
        start = nextIndex + 1;
        nextIndex = bst.indexOf(separator, start);
        length = nextIndex - start;
        invBranchStockTransferDetails.setBstDescription(bst.substring(start, start + length));

        // Location
        start = nextIndex + 1;
        nextIndex = bst.indexOf(separator, start);
        length = nextIndex - start;
        invBranchStockTransferDetails.setBstTransitLocation(bst.substring(start, start + length));

        // Branch From
        start = nextIndex + 1;
        nextIndex = bst.indexOf(separator, start);
        length = nextIndex - start;
        invBranchStockTransferDetails.setBstBranchFrom(bst.substring(start, start + length));

        // Branch To
        start = nextIndex + 1;
        nextIndex = bst.indexOf(separator, start);
        length = nextIndex - start;
        invBranchStockTransferDetails.setBstBranchTo(bst.substring(start, start + length));

        /************************** Misc Details***************************/

        // Created By
        start = nextIndex + 1;
        nextIndex = bst.indexOf(separator, start);
        length = nextIndex - start;
        invBranchStockTransferDetails.setBstCreatedBy(bst.substring(start, start + length));

        // Date Created
        start = nextIndex + 1;
        nextIndex = bst.indexOf(separator, start);
        length = nextIndex - start;
        try {
            invBranchStockTransferDetails.setBstDateCreated(sdf.parse(bst.substring(start, start + length)));
            System.out.println("Date Created-" + invBranchStockTransferDetails.getBstDateCreated());
        }
        catch (Exception ex) {
            System.out.println("Error On Date Created");
            //throw ex;
        }

        // Last Modified By
        start = nextIndex + 1;
        nextIndex = bst.indexOf(separator, start);
        length = nextIndex - start;
        invBranchStockTransferDetails.setBstLastModifiedBy(bst.substring(start, start + length));

        // Date Last Modified
        start = nextIndex + 1;
        nextIndex = bst.indexOf(separator, start);
        length = nextIndex - start;
        try {
            invBranchStockTransferDetails.setBstDateLastModified(sdf.parse(bst.substring(start, start + length)));
            System.out.println("Date Last Modified-" + invBranchStockTransferDetails.getBstDateLastModified());
        }
        catch (Exception ex) {
            System.out.println("Error On Date Last Modified");
            //throw ex;
        }

        // end separator
        start = nextIndex + 1;
        nextIndex = bst.indexOf(separator, start);
        length = nextIndex - start;

        String lineSeparator = "~";

        // begin lineSeparator
        start = nextIndex + 1;
        nextIndex = bst.indexOf(lineSeparator, start);
        length = nextIndex - start;

        invBranchStockTransferDetails.setBstBtlList(new ArrayList());

        while (true) {
            InvModBranchStockTransferLineDetails invModBranchStockTransferLineDetails = new InvModBranchStockTransferLineDetails();

            // begin separator
            start = nextIndex + 1;
            nextIndex = bst.indexOf(separator, start);
            length = nextIndex - start;

            // bst Quantity
            start = nextIndex + 1;
            nextIndex = bst.indexOf(separator, start);
            length = nextIndex - start;
            System.out.println(" bst Quantity: " + bst.substring(start, start + length));
            invModBranchStockTransferLineDetails.setBslQuantity((double) Double.parseDouble(bst.substring(start, start + length)));


            // bst Quantity Received
            start = nextIndex + 1;
            nextIndex = bst.indexOf(separator, start);
            length = nextIndex - start;
            System.out.println("bst Quantity Received: " + bst.substring(start, start + length));
            invModBranchStockTransferLineDetails.setBslQuantityReceived((double) Double.parseDouble(bst.substring(start, start + length)));

            // Unit Cost
            start = nextIndex + 1;
            nextIndex = bst.indexOf(separator, start);
            length = nextIndex - start;
            System.out.println("Unit Cost: " + bst.substring(start, start + length));
            invModBranchStockTransferLineDetails.setBslUnitCost((double) Double.parseDouble(bst.substring(start, start + length)));

            // Amount
            start = nextIndex + 1;
            nextIndex = bst.indexOf(separator, start);
            length = nextIndex - start;
            invModBranchStockTransferLineDetails.setBslAmount((double) Double.parseDouble(bst.substring(start, start + length)));

            // Item
            start = nextIndex + 1;
            nextIndex = bst.indexOf(separator, start);
            length = nextIndex - start;
            invModBranchStockTransferLineDetails.setBslIiName(bst.substring(start, start + length));

            // Location
            start = nextIndex + 1;
            nextIndex = bst.indexOf(separator, start);
            length = nextIndex - start;
            invModBranchStockTransferLineDetails.setBslLocationName(bst.substring(start, start + length));

            // Unit of Measure
            start = nextIndex + 1;
            nextIndex = bst.indexOf(separator, start);
            length = nextIndex - start;
            invModBranchStockTransferLineDetails.setBslUomName(bst.substring(start, start + length));

            // begin lineSeparator
            start = nextIndex + 1;
            nextIndex = bst.indexOf(lineSeparator, start);
            length = nextIndex - start;

            invBranchStockTransferDetails.getBstBtlList().add(invModBranchStockTransferLineDetails);

            int tempStart = nextIndex + 1;
            if (bst.indexOf(separator, tempStart) == -1) {
                break;
            }

        }

        return invBranchStockTransferDetails;


    }

    private String bstRowEncode(LocalInvBranchStockTransfer invBranchStockTransfer) {

        //Debug.print("InvItemSyncControllerBean poRowEncode");

        char separator = EJBCommon.SEPARATOR;
        StringBuffer encodedResult = new StringBuffer();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_OUTPUT);

        // Start separator
        encodedResult.append(separator);

        // BST Code
        encodedResult.append(invBranchStockTransfer.getBstCode());
        encodedResult.append(separator);

        // BST date
        encodedResult.append(sdf.format(invBranchStockTransfer.getBstDate()));
        encodedResult.append(separator);

        // BST Type
        encodedResult.append(invBranchStockTransfer.getBstType());
        encodedResult.append(separator);

        // Number
        encodedResult.append(invBranchStockTransfer.getBstNumber());
        encodedResult.append(separator);

        // Description
        encodedResult.append(invBranchStockTransfer.getBstDescription());
        encodedResult.append(separator);

        // Location
        encodedResult.append(invBranchStockTransfer.getInvLocation().getLocName());
        encodedResult.append(separator);

        // Branch From
        encodedResult.append(invBranchStockTransfer.getBstAdBranch());
        encodedResult.append(separator);

        // end separator
        encodedResult.append(separator);

        String lineSeparator = "~";

        // begin lineSeparator
        encodedResult.append(lineSeparator);

        Iterator i = invBranchStockTransfer.getInvBranchStockTransferLines().iterator();

        while (i.hasNext()) {

            LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) i.next();

            // begin separator
            encodedResult.append(separator);

            // Line Code
            encodedResult.append(invBranchStockTransferLine.getBslCode());
            encodedResult.append(separator);

            // Quantity
            encodedResult.append(invBranchStockTransferLine.getBslQuantity());
            encodedResult.append(separator);

            // Unit Cost
            encodedResult.append(invBranchStockTransferLine.getBslUnitCost());
            encodedResult.append(separator);

            // Amount
            encodedResult.append(invBranchStockTransferLine.getBslAmount());
            encodedResult.append(separator);

            // Item Location
            encodedResult.append(invBranchStockTransferLine.getInvItemLocation().getIlCode());
            encodedResult.append(separator);

            // Item Name
            encodedResult.append(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName());
            encodedResult.append(separator);

            // Location Name
            encodedResult.append(invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName());
            encodedResult.append(separator);

            // Unit of Measure
            encodedResult.append(invBranchStockTransferLine.getInvUnitOfMeasure().getUomName());
            encodedResult.append(separator);

            // BST Code
            encodedResult.append(invBranchStockTransfer.getBstCode());
            encodedResult.append(separator);

            // begin lineSeparator
            encodedResult.append(lineSeparator);
        }

        return encodedResult.toString();

    }

    private String invAdjRowEncode(Date invAdjDate, String User, String[] location,
                                   String adjustmentAccount, String[] insuItem, Double[] insuQty, String[] insuUOM, Double[] insuCost, int arrayCount,
                                   String documentNumber) {

        //Debug.print("InvItemSyncControllerBean poRowEncode");

        char separator = EJBCommon.SEPARATOR;
        StringBuffer encodedResult = new StringBuffer();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_OUTPUT);

        // Start separator
        encodedResult.append(separator);

        // Adj Code
        encodedResult.append("0");
        encodedResult.append(separator);

        // Adj Type
        encodedResult.append("GENERAL");
        encodedResult.append(separator);

        // Document Number
        encodedResult.append("");
        encodedResult.append(separator);

        // Reference Number
        encodedResult.append("Exception");
        encodedResult.append(separator);

        // Date
        encodedResult.append(sdf.format(invAdjDate));
        encodedResult.append(separator);

        //Description
        encodedResult.append("Insufficient Stock Fix for " + documentNumber);
        encodedResult.append(separator);


        // Created By
        encodedResult.append(User); // Temporarily Admin
        encodedResult.append(separator);

        Date date = new Date();

        // Date Created
        encodedResult.append(sdf.format(date));
        encodedResult.append(separator);

        // Last Modified By
        encodedResult.append(User); // Temporarily Admin
        encodedResult.append(separator);

        // Date Last Modified
        encodedResult.append(sdf.format(date));
        encodedResult.append(separator);

        // COA ACCOUNT NO
        encodedResult.append(adjustmentAccount);
        encodedResult.append(separator);

        // End Separator heading
        encodedResult.append(separator);

        String lineSeparator = "~";

        // begin lineSeparator
        encodedResult.append(lineSeparator);

        for (int x = 0; x < arrayCount; x++) {
            // Start separator
            encodedResult.append(separator);

            System.out.println("insuCost[x]: " + insuCost[x]);
            // Unit Cost
            encodedResult.append(insuCost[x]);
            encodedResult.append(separator);

            // Adjusted Quantity
            encodedResult.append(insuQty[x]);
            encodedResult.append(separator);
            //MessageBox.Show("ADJ QUANTITY : " + lineDr["INV_USED_QTY"]);

            // Unit of Measure
            encodedResult.append(insuUOM[x]);
            encodedResult.append(separator);

            // Item Name
            encodedResult.append(insuItem[x]);
            encodedResult.append(separator);

            // Location Name
            encodedResult.append(location[x]);
            encodedResult.append(separator);

            encodedResult.append(lineSeparator);

        }

        return encodedResult.toString();

    }

}