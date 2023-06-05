package com.ejb.txnsync.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.dao.ad.LocalAdBranchItemLocationHome;
import com.ejb.dao.ar.LocalArInvoiceHome;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdBranchItemLocation;
import com.ejb.entities.ar.LocalArDistributionRecord;
import com.ejb.entities.ar.LocalArInvoice;
import com.ejb.entities.ar.LocalArInvoiceLine;
import com.ejb.entities.inv.*;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.mod.inv.InvModItemDetails;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import java.text.SimpleDateFormat;
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
                    invItem = invItemHome.create(details.getIiName(), details.getIiDescription(),
                            details.getIiPartNumber(), details.getIiShortName(), details.getIiBarCode1(),
                            details.getIiBarCode2(), details.getIiBarCode3(), details.getIiBrand(), details.getIiClass(), details.getIiAdLvCategory(),
                            details.getIiCostMethod(), details.getIiUnitCost(), details.getIiSalesPrice(),
                            details.getIiEnable(), details.getIiVirtualStore(), details.getIiEnableAutoBuild(), details.getIiDoneness(),
                            details.getIiSidings(), details.getIiRemarks(), details.getIiServiceCharge(),
                            details.getIiNonInventoriable(), details.getIiServices(), details.getIiJobServices(),
                            details.getIiIsVatRelief(), details.getIiIsTax(), details.getIiIsProject(),
                            details.getIiPercentMarkup(), details.getIiShippingCost(), details.getIiSpecificGravity(),
                            details.getIiStandardFillSize(), details.getIiYield(),
                            0d, 0d, 0d, 0d, 0d,
                            null, null, null, null, null,
                            details.getIiLossPercentage(), details.getIiMarkupValue(),
                            details.getIiMarket(), details.getIiEnablePo(), details.getIiPoCycle(),
                            details.getIiUmcPackaging(), invUnitOfMeasure.getUomCode(), details.getIiOpenProduct(),
                            details.getIiFixedAsset(), details.getIiDateAcquired(), invLocation.getLocCode(), details.getIiTaxCode(),
                            details.getIiScSunday(), details.getIiScMonday(), details.getIiScTuesday(),
                            details.getIiScWednesday(), details.getIiScThursday(), details.getIiScFriday(), details.getIiScSaturday(),
                            companyCode);

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

}