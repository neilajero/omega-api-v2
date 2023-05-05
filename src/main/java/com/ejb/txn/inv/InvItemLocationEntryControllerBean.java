/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class InvItemLocationEntryControllerBean
 * @created June 03, 2004, 4:33 PM
 * @author Enrico C. Yap
 */
package com.ejb.txn.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ar.LocalArInvoice;
import com.ejb.entities.ar.LocalArReceipt;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.gl.LocalGlFunctionalCurrencyRate;
import com.ejb.entities.inv.*;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.inv.*;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.ad.AdBranchDetails;
import com.util.inv.InvItemDetails;
import com.util.mod.ad.AdModBranchItemLocationDetails;
import com.util.mod.inv.InvModItemDetails;
import com.util.mod.inv.InvModItemLocationDetails;

import jakarta.ejb.*;
import java.util.*;

@Stateless(name = "InvItemLocationEntryControllerEJB")
public class InvItemLocationEntryControllerBean extends EJBContextClass
        implements InvItemLocationEntryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;

    public ArrayList getInvLocAll(Integer AD_CMPNY) {

        Debug.print("InvItemLocationEntryControllerBean getInvLocAll");

        LocalInvLocation invLocation = null;

        ArrayList list = new ArrayList();

        try {

            Collection invLocations = invLocationHome.findLocAll(AD_CMPNY);

            for (Object location : invLocations) {

                invLocation = (LocalInvLocation) location;

                // Filter only WAREHOUSE(BAU) and STORE
                if (invLocation.getLocLvType().equals(EJBCommon.WAREHOUSE)
                        || invLocation.getLocLvType().equals(EJBCommon.STORE)) {

                    list.add(invLocation.getLocName());

                }
            }

            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getInvIiAll(Integer AD_CMPNY) {

        Debug.print("InvItemLocationEntryControllerBean getInvIiAll");

        LocalInvItem invItem = null;

        ArrayList list = new ArrayList();

        try {

            Collection invItemLocations = invItemHome.findEnabledIiAll(AD_CMPNY);

            for (Object invItemLocation : invItemLocations) {

                invItem = (LocalInvItem) invItemLocation;

                InvModItemDetails details = new InvModItemDetails();

                details.setIiName(invItem.getIiName());
                details.setIiDescription(invItem.getIiDescription());
                details.setIiAdLvCategory(invItem.getIiAdLvCategory());

                list.add(details);
            }

            list.sort(InvModItemDetails.ItemCategoryGroupComparator);

            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public InvItemDetails getInvItemByIiName(String II_NM, Integer AD_CMPNY) {

        Debug.print("InvItemLocationEntryControllerBean getInvItemByIiName");

        LocalInvItem invItem = null;

        try {

            invItem = invItemHome.findByIiName(II_NM, AD_CMPNY);

            InvItemDetails details = new InvItemDetails();

            details.setIiName(invItem.getIiName());
            details.setIiDescription(invItem.getIiDescription());

            return details;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfInvItemLocationShowAll(Integer AD_CMPNY) {

        Debug.print("InvItemLocationEntryControllerBean getAdPrfInvItemLocationShowAll");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfInvItemLocationShowAll();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfInvItemLocationAddByItemList(Integer AD_CMPNY) {

        Debug.print("InvItemLocationEntryControllerBean getAdPrfInvItemLocationAddItemList");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfInvItemLocationAddByItemList();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdLvInvItemCategoryAll(Integer AD_CMPNY) {

        Debug.print("InvItemLocationEntryControllerBean getAdLvInvItemCategoryAll");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("INV ITEM CATEGORY", AD_CMPNY);

            for (Object lookUpValue : adLookUpValues) {

                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;

                list.add(adLookUpValue.getLvName());
            }

            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvItemLocationEntryControllerBean getAdBrResAll");

        LocalAdBranchResponsibility adBranchResponsibility = null;
        LocalAdBranch adBranch = null;

        Collection adBranchResponsibilities = null;

        ArrayList list = new ArrayList();

        try {

            adBranchResponsibilities = adBranchResponsibilityHome.findByAdResponsibility(RS_CODE, AD_CMPNY);

        }
        catch (FinderException ex) {

        }
        catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (adBranchResponsibilities.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }

        try {

            for (Object branchResponsibility : adBranchResponsibilities) {

                adBranchResponsibility = (LocalAdBranchResponsibility) branchResponsibility;

                adBranch = adBranchHome.findByPrimaryKey(adBranchResponsibility.getAdBranch().getBrCode());

                AdBranchDetails details = new AdBranchDetails();
                details.setBrCode(adBranch.getBrCode());
                details.setBrBranchCode(adBranch.getBrBranchCode());
                details.setBrName(adBranch.getBrName());
                details.setBrCoaSegment(adBranch.getBrCoaSegment());
                details.setBrType(adBranch.getBrType());

                list.add(details);
            }

        }
        catch (FinderException ex) {

        }
        catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        return list;
    }

    public void saveInvIlEntry(InvModItemLocationDetails mdetails, ArrayList itemList, ArrayList locationList,
                               ArrayList branchItemLocationList, ArrayList categoryList, String II_CLSS, Integer AD_CMPNY)
            throws InvILCoaGlSalesAccountNotFoundException, InvILCoaGlSalesReturnAccountNotFoundException,
            InvILCoaGlInventoryAccountNotFoundException, InvILCoaGlCostOfSalesAccountNotFoundException,
            InvILCoaGlWipAccountNotFoundException, InvILCoaGlAccruedInventoryAccountNotFoundException {

        Debug.print("InvItemLocationEntryControllerBean saveInvIlEntry");

        try {

            String locationName = null;

            for (Object element : locationList) {

                locationName = (String) element;

                for (Object item : itemList) {

                    String itemName = (String) item;

                    for (Object value : categoryList) {

                        String category = (String) value;

                        LocalInvItem invItem = null;

                        try {

                            invItem = invItemHome.findByIiNameAndIiAdLvCategory(itemName, category, AD_CMPNY);

                        }
                        catch (FinderException ex) {

                            continue;
                        }

                        LocalGlChartOfAccount glSalesAccount = null;
                        LocalGlChartOfAccount glInventoryAccount = null;
                        LocalGlChartOfAccount glCostOfSalesAccount = null;
                        LocalGlChartOfAccount glWipAccount = null;
                        LocalGlChartOfAccount glAccrdInvAccount = null;
                        LocalGlChartOfAccount glSalesReturnAccount = null;
                        LocalInvItemLocation invItemLocation = null;

                        // get sales account, inventory account and cost of sales account to validate
                        // accounts

                        try {

                            glSalesAccount = glChartOfAccountHome
                                    .findByCoaAccountNumber(mdetails.getIlCoaGlSalesAccountNumber(), AD_CMPNY);

                        }
                        catch (FinderException ex) {

                            throw new InvILCoaGlSalesAccountNotFoundException();
                        }

                        try {

                            glSalesReturnAccount = glChartOfAccountHome
                                    .findByCoaAccountNumber(mdetails.getIlCoaGlSalesReturnAccountNumber(), AD_CMPNY);

                        }
                        catch (FinderException ex) {

                            throw new InvILCoaGlSalesReturnAccountNotFoundException();
                        }

                        try {

                            glInventoryAccount = glChartOfAccountHome
                                    .findByCoaAccountNumber(mdetails.getIlCoaGlInventoryAccountNumber(), AD_CMPNY);

                        }
                        catch (FinderException ex) {

                            throw new InvILCoaGlInventoryAccountNotFoundException();
                        }

                        try {

                            glCostOfSalesAccount = glChartOfAccountHome
                                    .findByCoaAccountNumber(mdetails.getIlCoaGlCostOfSalesAccountNumber(), AD_CMPNY);

                        }
                        catch (FinderException ex) {

                            throw new InvILCoaGlCostOfSalesAccountNotFoundException();
                        }

                        try {

                            glAccrdInvAccount = glChartOfAccountHome.findByCoaAccountNumber(
                                    mdetails.getIlCoaGlAccruedInventoryAccountNumber(), AD_CMPNY);

                        }
                        catch (FinderException ex) {

                            throw new InvILCoaGlAccruedInventoryAccountNotFoundException();
                        }

                        try {

                            glWipAccount = glChartOfAccountHome
                                    .findByCoaAccountNumber(mdetails.getIlCoaGlWipAccountNumber(), AD_CMPNY);

                        }
                        catch (FinderException ex) {

                            throw new InvILCoaGlWipAccountNotFoundException();
                        }

                        if (II_CLSS != null && II_CLSS.length() > 0 && !invItem.getIiClass().equals(II_CLSS)) {
                            continue;
                        }

                        // create new item location

                        try {

                            invItemLocation = invItemLocationHome.findByLocNameAndIiName(locationName, itemName,
                                    AD_CMPNY);

                        }
                        catch (FinderException ex) {

                        }

                        if (invItemLocation == null) {

                            invItemLocation = invItemLocationHome.IlRack(mdetails.getIlRack())
                                    .IlBin(mdetails.getIlBin()).IlReorderPoint(mdetails.getIlReorderPoint())
                                    .IlReorderQuantity(mdetails.getIlReorderQuantity())
                                    .IlReorderLevel(mdetails.getIlReorderLevel())
                                    .IlGlCoaSalesAccount(glSalesAccount.getCoaCode())
                                    .IlGlCoaInventoryAccount(glInventoryAccount.getCoaCode())
                                    .IlGlCoaCostOfSalesAccount(glCostOfSalesAccount.getCoaCode())
                                    .IlGlCoaWipAccount(glWipAccount.getCoaCode())
                                    .IlGlCoaAccruedInventoryAccount(glAccrdInvAccount.getCoaCode())
                                    .IlGlCoaSalesReturnAccount(glSalesReturnAccount.getCoaCode())
                                    .IlSubjectToCommission(mdetails.getIlSubjectToCommission()).IlAdCompany(AD_CMPNY)
                                    .buildItemLocation();

                            invItemLocation.setInvItem(invItem);

                            LocalInvLocation invLocation = invLocationHome.findByLocName(locationName, AD_CMPNY);
                            invItemLocation.setInvLocation(invLocation);

                            invItemLocation.getInvItem().setIiLastModifiedBy(mdetails.getIlLastModifiedBy());
                            invItemLocation.getInvItem().setIiDateLastModified(mdetails.getIlDateLastModified());

                            for (Object o : branchItemLocationList) {

                                AdModBranchItemLocationDetails details = (AdModBranchItemLocationDetails) o;

                                //TODO: Fix this NULL concern with POM WAREHOUSE COA - Deferred Revenue - Liability
                                Debug.print("Sales Account : " + details.getBilCoaGlSalesAccountNumber());
                                if (details.getBilCoaGlSalesAccountNumber() != null) {
                                    try {

                                        glSalesAccount = glChartOfAccountHome
                                                .findByCoaAccountNumber(details.getBilCoaGlSalesAccountNumber(), AD_CMPNY);

                                    }
                                    catch (FinderException ex) {

                                        throw new InvILCoaGlSalesAccountNotFoundException();
                                    }
                                }

                                //TODO: Fix this NULL concern with POM WAREHOUSE COA - Deferred Revenue - Liability
                                Debug.print("Sales Return Account : " + details.getBilCoaGlSalesReturnAccountNumber());
                                if (details.getBilCoaGlSalesReturnAccountNumber() != null) {
                                    try {

                                        glSalesReturnAccount = glChartOfAccountHome.findByCoaAccountNumber(
                                                details.getBilCoaGlSalesReturnAccountNumber(), AD_CMPNY);

                                    }
                                    catch (FinderException ex) {

                                        throw new InvILCoaGlSalesReturnAccountNotFoundException();
                                    }
                                }

                                try {

                                    glInventoryAccount = glChartOfAccountHome.findByCoaAccountNumber(
                                            details.getBilCoaGlInventoryAccountNumber(), AD_CMPNY);

                                }
                                catch (FinderException ex) {

                                    throw new InvILCoaGlInventoryAccountNotFoundException();
                                }

                                try {

                                    glCostOfSalesAccount = glChartOfAccountHome.findByCoaAccountNumber(
                                            details.getBilCoaGlCostOfSalesAccountNumber(), AD_CMPNY);

                                }
                                catch (FinderException ex) {

                                    throw new InvILCoaGlCostOfSalesAccountNotFoundException();
                                }

                                try {

                                    glAccrdInvAccount = glChartOfAccountHome.findByCoaAccountNumber(
                                            details.getBilCoaGlAccruedInventoryAccountNumber(), AD_CMPNY);

                                }
                                catch (FinderException ex) {

                                    throw new InvILCoaGlAccruedInventoryAccountNotFoundException();
                                }

                                try {

                                    glWipAccount = glChartOfAccountHome
                                            .findByCoaAccountNumber(details.getBilCoaGlWipAccountNumber(), AD_CMPNY);

                                }
                                catch (FinderException ex) {

                                    throw new InvILCoaGlWipAccountNotFoundException();
                                }

                                // create new branch item location
                                LocalAdBranchItemLocation adBranchItemLocation = adBranchItemLocationHome
                                        .BilRack(details.getBilRack()).BilBin(details.getBilBin())
                                        .BilReorderPoint(details.getBilReorderPoint())
                                        .BilReorderQuantity(details.getBilReorderQuantity())
                                        .BilGlCoaAccruedInventoryAccount(glAccrdInvAccount.getCoaCode())
                                        .BilGlCoaSalesAccount(glSalesAccount.getCoaCode())
                                        .BilGlCoaInventoryAccount(glInventoryAccount.getCoaCode())
                                        .BilGlCoaCostOfSalesAccount(glCostOfSalesAccount.getCoaCode())
                                        .BilGlCoaWipAccount(glWipAccount.getCoaCode())
                                        .BilGlCoaSalesReturnAccount(glSalesReturnAccount.getCoaCode())
                                        .BilSubjectToCommission(details.getBilSubjectToCommission())
                                        .BilHist1Sales(details.getBilHist1Sales())
                                        .BilHist2Sales(details.getBilHist2Sales())
                                        .BilProjectedSales(details.getBilProjectedSales())
                                        .BilDeliveryTime(details.getBilDeliveryTime())
                                        .BilDeliveryBuffer(details.getBilDeliveryBuffer())
                                        .BilOrderPerYear(details.getBilOrderPerYear()).BilAdCompany(AD_CMPNY)
                                        .buildBranchItemLocation();

                                adBranchItemLocation.setInvItemLocation(invItemLocation);
                                LocalAdBranch adBranch = adBranchHome.findByBrName(details.getBilBrName(), AD_CMPNY);
                                adBranchItemLocation.setAdBranch(adBranch);
                            }

                        } else {

                            // update item location

                            invItemLocation.setIlRack(mdetails.getIlRack());
                            invItemLocation.setIlBin(mdetails.getIlBin());
                            invItemLocation.setIlReorderPoint(mdetails.getIlReorderPoint());
                            invItemLocation.setIlReorderQuantity(mdetails.getIlReorderQuantity());
                            invItemLocation.setIlReorderLevel(mdetails.getIlReorderLevel());
                            invItemLocation.setIlGlCoaSalesAccount(glSalesAccount.getCoaCode());
                            invItemLocation.setIlGlCoaInventoryAccount(glInventoryAccount.getCoaCode());
                            invItemLocation.setIlGlCoaCostOfSalesAccount(glCostOfSalesAccount.getCoaCode());
                            invItemLocation.setIlGlCoaWipAccount(glWipAccount.getCoaCode());
                            invItemLocation.setIlGlCoaAccruedInventoryAccount(glAccrdInvAccount.getCoaCode());
                            invItemLocation.setIlGlCoaSalesReturnAccount(glSalesReturnAccount.getCoaCode());
                            invItemLocation.setIlSubjectToCommission(mdetails.getIlSubjectToCommission());
                            invItemLocation.getInvItem().setIiLastModifiedBy(mdetails.getIlLastModifiedBy());
                            invItemLocation.getInvItem().setIiDateLastModified(mdetails.getIlDateLastModified());
                            // set download status

                            AdModBranchItemLocationDetails details = null;

                            Iterator brIter = branchItemLocationList.iterator();

                            while (brIter.hasNext()) {

                                details = (AdModBranchItemLocationDetails) brIter.next();

                                LocalAdBranch adBranch = adBranchHome.findByBrName(details.getBilBrName(), AD_CMPNY);

                                LocalAdBranchItemLocation adBranchItemLocation = null;

                                try {

                                    adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(
                                            invItemLocation.getIlCode(), adBranch.getBrCode(), AD_CMPNY);

                                }
                                catch (FinderException ex) {

                                }

                                if (adBranchItemLocation == null) {

                                    details.setBilItemDownloadStatus('N');
                                    details.setBilLocationDownloadStatus('N');
                                    details.setBilItemLocationDownloadStatus('N');

                                } else {

                                    if (adBranchItemLocation.getBilItemLocationDownloadStatus() == 'N') {
                                        details.setBilItemLocationDownloadStatus('N');
                                    } else if (adBranchItemLocation.getBilItemLocationDownloadStatus() == 'D') {
                                        details.setBilItemLocationDownloadStatus('X');
                                    } else if (adBranchItemLocation.getBilItemLocationDownloadStatus() == 'U') {
                                        details.setBilItemLocationDownloadStatus('U');
                                    } else if (adBranchItemLocation.getBilItemLocationDownloadStatus() == 'X') {
                                        details.setBilItemLocationDownloadStatus('X');
                                    }
                                    details.setBilItemDownloadStatus(adBranchItemLocation.getBilItemDownloadStatus());
                                    details.setBilLocationDownloadStatus(
                                            adBranchItemLocation.getBilLocationDownloadStatus());
                                }
                            }

                            // remove all branch item locations

                            try {

                                Collection adBranchItemLocations = adBranchItemLocationHome
                                        .findByInvIlAll(invItemLocation.getIlCode(), AD_CMPNY);

                                for (Object branchItemLocation : adBranchItemLocations) {

                                    LocalAdBranchItemLocation adBranchItemLocation = (LocalAdBranchItemLocation) branchItemLocation;
                                    invItemLocation.dropAdBranchItemLocation(adBranchItemLocation);

                                    LocalAdBranch adBranch = adBranchHome
                                            .findByPrimaryKey(adBranchItemLocation.getAdBranch().getBrCode());
                                    adBranch.dropAdBranchItemLocation(adBranchItemLocation);

                                    // adBranchItemLocation.remove();
                                    em.remove(adBranchItemLocation);
                                }

                            }
                            catch (FinderException ex) {

                            }

                            // add branch item locations

                            brIter = branchItemLocationList.iterator();

                            while (brIter.hasNext()) {

                                // AdModBranchItemLocationDetails
                                details = (AdModBranchItemLocationDetails) brIter.next();


                                try {

                                    glSalesAccount = glChartOfAccountHome
                                            .findByCoaAccountNumber(details.getBilCoaGlSalesAccountNumber(), AD_CMPNY);

                                }
                                catch (FinderException ex) {

                                    throw new InvILCoaGlSalesAccountNotFoundException();
                                }

                                try {

                                    glSalesReturnAccount = glChartOfAccountHome.findByCoaAccountNumber(
                                            details.getBilCoaGlSalesReturnAccountNumber(), AD_CMPNY);

                                }
                                catch (FinderException ex) {

                                    throw new InvILCoaGlSalesReturnAccountNotFoundException();
                                }

                                try {

                                    glInventoryAccount = glChartOfAccountHome.findByCoaAccountNumber(
                                            details.getBilCoaGlInventoryAccountNumber(), AD_CMPNY);

                                }
                                catch (FinderException ex) {

                                    throw new InvILCoaGlInventoryAccountNotFoundException();
                                }

                                try {

                                    glCostOfSalesAccount = glChartOfAccountHome.findByCoaAccountNumber(
                                            details.getBilCoaGlCostOfSalesAccountNumber(), AD_CMPNY);

                                }
                                catch (FinderException ex) {

                                    throw new InvILCoaGlCostOfSalesAccountNotFoundException();
                                }

                                try {

                                    glAccrdInvAccount = glChartOfAccountHome.findByCoaAccountNumber(
                                            details.getBilCoaGlAccruedInventoryAccountNumber(), AD_CMPNY);

                                }
                                catch (FinderException ex) {

                                    throw new InvILCoaGlAccruedInventoryAccountNotFoundException();
                                }

                                try {

                                    glWipAccount = glChartOfAccountHome
                                            .findByCoaAccountNumber(details.getBilCoaGlWipAccountNumber(), AD_CMPNY);

                                }
                                catch (FinderException ex) {

                                    throw new InvILCoaGlWipAccountNotFoundException();
                                }

                                // create new branch item location
                                LocalAdBranchItemLocation adBranchItemLocation = adBranchItemLocationHome
                                        .BilRack(details.getBilRack()).BilBin(details.getBilBin())
                                        .BilReorderPoint(details.getBilReorderPoint())
                                        .BilReorderQuantity(details.getBilReorderQuantity())
                                        .BilGlCoaAccruedInventoryAccount(glAccrdInvAccount.getCoaCode())
                                        .BilGlCoaSalesAccount(glSalesAccount.getCoaCode())
                                        .BilGlCoaInventoryAccount(glInventoryAccount.getCoaCode())
                                        .BilGlCoaCostOfSalesAccount(glCostOfSalesAccount.getCoaCode())
                                        .BilGlCoaWipAccount(glWipAccount.getCoaCode())
                                        .BilGlCoaSalesReturnAccount(glSalesReturnAccount.getCoaCode())
                                        .BilSubjectToCommission(details.getBilSubjectToCommission())
                                        .BilHist1Sales(details.getBilHist1Sales())
                                        .BilHist2Sales(details.getBilHist2Sales())
                                        .BilProjectedSales(details.getBilProjectedSales())
                                        .BilDeliveryTime(details.getBilDeliveryTime())
                                        .BilDeliveryBuffer(details.getBilDeliveryBuffer())
                                        .BilOrderPerYear(details.getBilOrderPerYear())
                                        .BilItemDownloadStatus(details.getBilItemDownloadStatus())
                                        .BilLocationDownloadStatus(details.getBilLocationDownloadStatus())
                                        .BilItemLocationDownloadStatus(details.getBilItemLocationDownloadStatus())
                                        .BilAdCompany(AD_CMPNY).buildBranchItemLocation();

                                adBranchItemLocation.setInvItemLocation(invItemLocation);
                                LocalAdBranch adBranch = adBranchHome.findByBrName(details.getBilBrName(), AD_CMPNY);
                                adBranchItemLocation.setAdBranch(adBranch);

                                // assign modified item
                                adBranchItemLocation.getInvItemLocation().getInvItem()
                                        .setIiLastModifiedBy(details.getBilLastModifiedBy());
                                adBranchItemLocation.getInvItemLocation().getInvItem()
                                        .setIiDateLastModified(details.getBilDateLastModified());
                            }
                        }
                    }
                }
            }

        }
        catch (InvILCoaGlSalesAccountNotFoundException | InvILCoaGlWipAccountNotFoundException |
               InvILCoaGlCostOfSalesAccountNotFoundException | InvILCoaGlInventoryAccountNotFoundException |
               InvILCoaGlSalesReturnAccountNotFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void saveInvNonIlEntry(InvModItemLocationDetails mdetails, java.util.ArrayList itemList,
                                  java.util.ArrayList locationList, java.util.ArrayList branchItemLocationList,
                                  java.util.ArrayList categoryList, java.lang.String II_CLSS, java.lang.Integer AD_CMPNY)
            throws InvILCoaGlAccruedInventoryAccountNotFoundException {

        Debug.print("InvItemLocationEntryControllerBean saveInvNonIlEntry");

        try {

            String locationName = null;

            for (Object element : locationList) {

                locationName = (String) element;

                for (Object item : itemList) {

                    String itemName = (String) item;

                    for (Object value : categoryList) {

                        String category = (String) value;

                        LocalInvItem invItem = null;

                        try {

                            invItem = invItemHome.findByIiNameAndIiAdLvCategory(itemName, category, AD_CMPNY);

                        }
                        catch (FinderException ex) {

                            continue;
                        }

                        LocalGlChartOfAccount glAccrdInvAccount = null;
                        LocalGlChartOfAccount glExpenseAccount = null;
                        LocalInvItemLocation invItemLocation = null;

                        try {

                            glAccrdInvAccount = glChartOfAccountHome.findByCoaAccountNumber(
                                    mdetails.getIlCoaGlAccruedInventoryAccountNumber(), AD_CMPNY);

                        }
                        catch (FinderException ex) {

                            throw new InvILCoaGlAccruedInventoryAccountNotFoundException();
                        }

                        try {

                            glExpenseAccount = glChartOfAccountHome
                                    .findByCoaAccountNumber(mdetails.getIlCoaGlExpenseAccountNumber(), AD_CMPNY);

                        }
                        catch (FinderException ex) {

                            throw ex;
                        }

                        if (II_CLSS != null && II_CLSS.length() > 0 && !invItem.getIiClass().equals(II_CLSS)) {
                            continue;
                        }

                        // create new item location

                        try {

                            invItemLocation = invItemLocationHome.findByLocNameAndIiName(locationName, itemName,
                                    AD_CMPNY);

                        }
                        catch (FinderException ex) {

                        }

                        if (invItemLocation == null) {

                            invItemLocation = invItemLocationHome.IlRack(mdetails.getIlRack())
                                    .IlBin(mdetails.getIlBin()).IlReorderPoint(mdetails.getIlReorderPoint())
                                    .IlReorderQuantity(mdetails.getIlReorderQuantity())
                                    .IlReorderLevel(mdetails.getIlReorderLevel())
                                    .IlGlCoaAccruedInventoryAccount(glAccrdInvAccount.getCoaCode())
                                    .IlGlCoaExpenseAccount(glExpenseAccount.getCoaCode())
                                    .IlSubjectToCommission(mdetails.getIlSubjectToCommission()).IlAdCompany(AD_CMPNY)
                                    .buildItemLocation();

                            invItemLocation.setInvItem(invItem);

                            LocalInvLocation invLocation = invLocationHome.findByLocName(locationName, AD_CMPNY);
                            invItemLocation.setInvLocation(invLocation);

                            invItemLocation.getInvItem().setIiLastModifiedBy(mdetails.getIlLastModifiedBy());
                            invItemLocation.getInvItem().setIiDateLastModified(mdetails.getIlDateLastModified());

                            for (Object o : branchItemLocationList) {

                                AdModBranchItemLocationDetails details = (AdModBranchItemLocationDetails) o;

                                try {

                                    glAccrdInvAccount = glChartOfAccountHome.findByCoaAccountNumber(
                                            details.getBilCoaGlAccruedInventoryAccountNumber(), AD_CMPNY);

                                }
                                catch (FinderException ex) {

                                    throw new InvILCoaGlAccruedInventoryAccountNotFoundException();
                                }

                                try {

                                    glExpenseAccount = glChartOfAccountHome.findByCoaAccountNumber(
                                            details.getBilCoaGlExpenseAccountNumber(), AD_CMPNY);

                                }
                                catch (FinderException ex) {

                                    // -- If Expense Account doesn't exist revert to Header Expense Account
                                    // TODO: Review and find another to handle this case
                                    glExpenseAccount = glChartOfAccountHome.findByCoaAccountNumber(
                                            mdetails.getIlCoaGlExpenseAccountNumber(), AD_CMPNY);
                                }

                                // create new branch item location
                                LocalAdBranchItemLocation adBranchItemLocation = adBranchItemLocationHome
                                        .BilRack(details.getBilRack()).BilBin(details.getBilBin())
                                        .BilReorderPoint(details.getBilReorderPoint())
                                        .BilReorderQuantity(details.getBilReorderQuantity())

                                        .BilGlCoaAccruedInventoryAccount(glAccrdInvAccount.getCoaCode())
                                        .BilGlCoaExpenseAccount(glExpenseAccount.getCoaCode())

                                        .BilSubjectToCommission(details.getBilSubjectToCommission())
                                        .BilHist1Sales(details.getBilHist1Sales())
                                        .BilHist2Sales(details.getBilHist2Sales())
                                        .BilProjectedSales(details.getBilProjectedSales())
                                        .BilDeliveryTime(details.getBilDeliveryTime())
                                        .BilDeliveryBuffer(details.getBilDeliveryBuffer())
                                        .BilOrderPerYear(details.getBilOrderPerYear()).BilAdCompany(AD_CMPNY)
                                        .buildBranchItemLocation();

                                adBranchItemLocation.setInvItemLocation(invItemLocation);
                                LocalAdBranch adBranch = adBranchHome.findByBrName(details.getBilBrName(), AD_CMPNY);
                                adBranchItemLocation.setAdBranch(adBranch);
                            }

                        } else {

                            // update item location

                            invItemLocation.setIlRack(mdetails.getIlRack());
                            invItemLocation.setIlBin(mdetails.getIlBin());
                            invItemLocation.setIlReorderPoint(mdetails.getIlReorderPoint());
                            invItemLocation.setIlReorderQuantity(mdetails.getIlReorderQuantity());
                            invItemLocation.setIlReorderLevel(mdetails.getIlReorderLevel());
                            invItemLocation.setIlGlCoaAccruedInventoryAccount(glAccrdInvAccount.getCoaCode());
                            invItemLocation.setIlGlCoaExpenseAccount(glExpenseAccount.getCoaCode());
                            invItemLocation.setIlSubjectToCommission(mdetails.getIlSubjectToCommission());
                            invItemLocation.getInvItem().setIiLastModifiedBy(mdetails.getIlLastModifiedBy());
                            invItemLocation.getInvItem().setIiDateLastModified(mdetails.getIlDateLastModified());
                            // set download status

                            AdModBranchItemLocationDetails details = null;

                            Iterator brIter = branchItemLocationList.iterator();

                            while (brIter.hasNext()) {

                                details = (AdModBranchItemLocationDetails) brIter.next();

                                LocalAdBranch adBranch = adBranchHome.findByBrName(details.getBilBrName(), AD_CMPNY);

                                LocalAdBranchItemLocation adBranchItemLocation = null;

                                try {

                                    adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(
                                            invItemLocation.getIlCode(), adBranch.getBrCode(), AD_CMPNY);

                                }
                                catch (FinderException ex) {

                                }

                                if (adBranchItemLocation == null) {

                                    details.setBilItemDownloadStatus('N');
                                    details.setBilLocationDownloadStatus('N');
                                    details.setBilItemLocationDownloadStatus('N');

                                } else {

                                    if (adBranchItemLocation.getBilItemLocationDownloadStatus() == 'N') {
                                        details.setBilItemLocationDownloadStatus('N');
                                    } else if (adBranchItemLocation.getBilItemLocationDownloadStatus() == 'D') {
                                        details.setBilItemLocationDownloadStatus('X');
                                    } else if (adBranchItemLocation.getBilItemLocationDownloadStatus() == 'U') {
                                        details.setBilItemLocationDownloadStatus('U');
                                    } else if (adBranchItemLocation.getBilItemLocationDownloadStatus() == 'X') {
                                        details.setBilItemLocationDownloadStatus('X');
                                    }
                                    details.setBilItemDownloadStatus(adBranchItemLocation.getBilItemDownloadStatus());
                                    details.setBilLocationDownloadStatus(
                                            adBranchItemLocation.getBilLocationDownloadStatus());
                                }
                            }

                            // remove all branch item locations

                            try {

                                Collection adBranchItemLocations = adBranchItemLocationHome
                                        .findByInvIlAll(invItemLocation.getIlCode(), AD_CMPNY);

                                for (Object branchItemLocation : adBranchItemLocations) {

                                    LocalAdBranchItemLocation adBranchItemLocation = (LocalAdBranchItemLocation) branchItemLocation;
                                    invItemLocation.dropAdBranchItemLocation(adBranchItemLocation);

                                    LocalAdBranch adBranch = adBranchHome
                                            .findByPrimaryKey(adBranchItemLocation.getAdBranch().getBrCode());
                                    adBranch.dropAdBranchItemLocation(adBranchItemLocation);

                                    // adBranchItemLocation.remove();
                                    em.remove(adBranchItemLocation);
                                }

                            }
                            catch (FinderException ex) {

                            }

                            // add branch item locations

                            brIter = branchItemLocationList.iterator();

                            while (brIter.hasNext()) {

                                // AdModBranchItemLocationDetails
                                details = (AdModBranchItemLocationDetails) brIter.next();

                                try {

                                    glAccrdInvAccount = glChartOfAccountHome.findByCoaAccountNumber(
                                            details.getBilCoaGlAccruedInventoryAccountNumber(), AD_CMPNY);

                                }
                                catch (FinderException ex) {

                                    throw new InvILCoaGlAccruedInventoryAccountNotFoundException();
                                }

                                try {

                                    glExpenseAccount = glChartOfAccountHome.findByCoaAccountNumber(
                                            details.getBilCoaGlExpenseAccountNumber(), AD_CMPNY);

                                }
                                catch (FinderException ex) {

                                    // -- If Expense Account doesn't exist revert to Header Expense Account
                                    // TODO: Review and find another to handle this case
                                    glExpenseAccount = glChartOfAccountHome.findByCoaAccountNumber(
                                            mdetails.getIlCoaGlExpenseAccountNumber(), AD_CMPNY);
                                }

                                // create new branch item location
                                LocalAdBranchItemLocation adBranchItemLocation = adBranchItemLocationHome
                                        .BilRack(details.getBilRack()).BilBin(details.getBilBin())
                                        .BilReorderPoint(details.getBilReorderPoint())
                                        .BilReorderQuantity(details.getBilReorderQuantity())
                                        .BilGlCoaAccruedInventoryAccount(glAccrdInvAccount.getCoaCode())
                                        .BilGlCoaExpenseAccount(glExpenseAccount.getCoaCode())
                                        .BilSubjectToCommission(details.getBilSubjectToCommission())
                                        .BilHist1Sales(details.getBilHist1Sales())
                                        .BilHist2Sales(details.getBilHist2Sales())
                                        .BilProjectedSales(details.getBilProjectedSales())
                                        .BilDeliveryTime(details.getBilDeliveryTime())
                                        .BilDeliveryBuffer(details.getBilDeliveryBuffer())
                                        .BilOrderPerYear(details.getBilOrderPerYear())
                                        .BilItemDownloadStatus(details.getBilItemDownloadStatus())
                                        .BilLocationDownloadStatus(details.getBilLocationDownloadStatus())
                                        .BilAdCompany(AD_CMPNY).buildBranchItemLocation();

                                adBranchItemLocation.setInvItemLocation(invItemLocation);
                                LocalAdBranch adBranch = adBranchHome.findByBrName(details.getBilBrName(), AD_CMPNY);
                                adBranchItemLocation.setAdBranch(adBranch);

                                // assign modified item
                                adBranchItemLocation.getInvItemLocation().getInvItem()
                                        .setIiLastModifiedBy(details.getBilLastModifiedBy());
                                adBranchItemLocation.getInvItemLocation().getInvItem()
                                        .setIiDateLastModified(details.getBilDateLastModified());
                            }
                        }
                    }
                }
            }

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

    }

    public void updateInvIliAveSales(ArrayList itemList, ArrayList locationList, ArrayList branchItemLocationList,
                                     ArrayList categoryList, String II_CLSS, Integer RS_CODE, Integer AD_CMPNY) {

        Debug.print("InvItemLocationEntryControllerBean updateInvIliAveSales");

        try {

            String locationName = null;

            for (Object element : locationList) {

                locationName = (String) element;

                for (Object item : itemList) {

                    String itemName = (String) item;

                    for (Object value : categoryList) {

                        String category = (String) value;

                        LocalInvItem invItem = null;

                        try {

                            invItem = invItemHome.findByIiNameAndIiAdLvCategory(itemName, category, AD_CMPNY);

                        }
                        catch (FinderException ex) {

                            continue;
                        }

                        if (II_CLSS != null && II_CLSS.length() > 0 && !invItem.getIiClass().equals(II_CLSS)) {
                            continue;
                        }

                        // create new item location
                        LocalInvItemLocation invItemLocation = null;

                        try {

                            invItemLocation = invItemLocationHome.findByLocNameAndIiName(locationName, itemName,
                                    AD_CMPNY);

                        }
                        catch (FinderException ex) {
                            continue;
                        }

                        // update item location

                        for (Object o : branchItemLocationList) {

                            AdModBranchItemLocationDetails details = (AdModBranchItemLocationDetails) o;

                            LocalAdBranch adBranch = adBranchHome.findByBrName(details.getBilBrName(), AD_CMPNY);

                            LocalAdBranchItemLocation adBranchItemLocation = null;

                            try {

                                adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(
                                        invItemLocation.getIlCode(), adBranch.getBrCode(), AD_CMPNY);

                            }
                            catch (FinderException ex) {

                                continue;
                            }

                            adBranchItemLocation.setBilHist1Sales(details.getBilHist1Sales());
                            adBranchItemLocation.setBilHist2Sales(details.getBilHist1Sales());
                            adBranchItemLocation.setBilProjectedSales(details.getBilProjectedSales());
                            adBranchItemLocation.setBilDeliveryTime(details.getBilDeliveryTime());
                            adBranchItemLocation.setBilDeliveryBuffer(details.getBilDeliveryBuffer());
                            adBranchItemLocation.setBilOrderPerYear(details.getBilOrderPerYear());
                        }
                    }
                }
            }

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteInvIlEntry(ArrayList itemList, ArrayList locationList, ArrayList categoryList, String II_CLSS,
                                 Integer AD_CMPNY) throws GlobalRecordAlreadyAssignedException {

        Debug.print("InvItemLocationEntryControllerBean deleteInvIlEntry");

        try {

            boolean isFirstArgument = false;
            int ctr = 1;
            Iterator i = itemList.iterator();
            StringBuilder item = new StringBuilder();

            while (i.hasNext()) {

                if (ctr == 1) {
                    item.append("(");
                }

                item.append("il.invItem.iiName='").append(i.next()).append("'");

                if (ctr < itemList.size()) {
                    item.append(" OR ");
                } else {
                    item.append(")");
                }

                ctr++;
                isFirstArgument = true;
            }

            ctr = 1;
            i = locationList.iterator();
            StringBuilder location = new StringBuilder();

            while (i.hasNext()) {

                if (ctr == 1 && isFirstArgument) {
                    location.append(" AND (");
                } else if (ctr == 1 && !isFirstArgument) {
                    location.append("(");
                }

                location.append("il.invLocation.locName='").append(i.next()).append("'");

                if (ctr < locationList.size()) {
                    location.append(" OR ");
                } else {
                    location.append(")");
                }

                ctr++;
                isFirstArgument = true;
            }

            ctr = 1;
            i = categoryList.iterator();
            StringBuilder category = new StringBuilder();

            while (i.hasNext()) {

                if (ctr == 1 && isFirstArgument) {
                    category.append(" AND (");
                } else if (ctr == 1 && !isFirstArgument) {
                    category.append("(");
                }

                category.append("il.invItem.iiAdLvCategory='").append(i.next()).append("'");

                if (ctr < categoryList.size()) {
                    category.append(" OR ");
                } else {
                    category.append(")");
                }

                ctr++;
                isFirstArgument = true;
            }

            String company = "";
            if (isFirstArgument) {
                company = " AND il.ilAdCompany=" + AD_CMPNY;
            } else {
                company = "il.ilAdCompany=" + AD_CMPNY;
            }

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(il) FROM InvItemLocation il WHERE ").append(item).append(location).append(category).append(company);

            Collection invItemLocations = invItemLocationHome.getIlByCriteria(jbossQl.toString(), new Object[0], 0, 0);

            Iterator iter = invItemLocations.iterator();

            boolean isException = false;

            while (iter.hasNext()) {

                LocalInvItemLocation invItemLocation = (LocalInvItemLocation) iter.next();

                if (!invItemLocation.getApVoucherLineItems().isEmpty()
                        || !invItemLocation.getArInvoiceLineItems().isEmpty()
                        || !invItemLocation.getInvAdjustmentLines().isEmpty()
                        || !invItemLocation.getInvCostings().isEmpty()
                        || !invItemLocation.getInvPhysicalInventoryLines().isEmpty()
                        || !invItemLocation.getInvBranchStockTransferLines().isEmpty()
                        || !invItemLocation.getApPurchaseOrderLines().isEmpty()
                        || !invItemLocation.getApPurchaseRequisitionLines().isEmpty()
                        || !invItemLocation.getArSalesOrderLines().isEmpty()
                        || !invItemLocation.getInvLineItems().isEmpty()) {

                    isException = true;
                    continue;
                }

                // invItemLocation.remove();
                em.remove(invItemLocation);
            }

            if (isException) {
                throw new GlobalRecordAlreadyAssignedException();
            }

        }
        catch (GlobalRecordAlreadyAssignedException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public InvModItemLocationDetails getInvIlByIlCode(Integer IL_CODE, Integer AD_CMPNY)
            throws GlobalNoRecordFoundException {

        Debug.print("InvItemLocationEntryControllerBean getInvIlByIlCode");

        LocalInvItemLocation invItemLocation = null;
        String invItemCategory = null;

        try {

            try {

                invItemLocation = invItemLocationHome.findByPrimaryKey(IL_CODE);
                invItemCategory = invItemLocation.getInvItem().getIiAdLvCategory();

            }
            catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            InvModItemLocationDetails ilDetails = new InvModItemLocationDetails();
            ilDetails.setIlAdLvCategory(invItemCategory);
            ilDetails.setIlCode(invItemLocation.getIlCode());
            ilDetails.setIlRack(invItemLocation.getIlRack());
            ilDetails.setIlBin(invItemLocation.getIlBin());
            ilDetails.setIlReorderPoint(invItemLocation.getIlReorderPoint());
            ilDetails.setIlReorderQuantity(invItemLocation.getIlReorderQuantity());
            ilDetails.setIlReorderLevel(invItemLocation.getIlReorderLevel());

            LocalGlChartOfAccount glAccrdInvAccount = glChartOfAccountHome
                    .findByPrimaryKey(invItemLocation.getIlGlCoaAccruedInventoryAccount());

            LocalGlChartOfAccount glSalesAccount = null;
            LocalGlChartOfAccount glSalesReturnAccount = null;
            LocalGlChartOfAccount glInventoryAccount = null;
            LocalGlChartOfAccount glCostOfSalesAccount = null;
            LocalGlChartOfAccount glWipAccount = null;

            // TODO: Revisit this logic and make necessary changes
            if (!invItemCategory.equals(EJBCommon.DEFAULT)) {

                glSalesAccount = glChartOfAccountHome.findByPrimaryKey(invItemLocation.getIlGlCoaSalesAccount());
                glSalesReturnAccount = glChartOfAccountHome
                        .findByPrimaryKey(invItemLocation.getIlGlCoaSalesReturnAccount());
                glInventoryAccount = glChartOfAccountHome
                        .findByPrimaryKey(invItemLocation.getIlGlCoaInventoryAccount());
                glCostOfSalesAccount = glChartOfAccountHome
                        .findByPrimaryKey(invItemLocation.getIlGlCoaCostOfSalesAccount());
                glWipAccount = glChartOfAccountHome.findByPrimaryKey(invItemLocation.getIlGlCoaWipAccount());

                ilDetails.setIlCoaGlSalesAccountNumber(glSalesAccount.getCoaAccountNumber());
                ilDetails.setIlCoaGlSalesAccountDescription(glSalesAccount.getCoaAccountDescription());
                ilDetails.setIlCoaGlSalesReturnAccountNumber(glSalesReturnAccount.getCoaAccountNumber());
                ilDetails.setIlCoaGlSalesReturnAccountDescription(glSalesReturnAccount.getCoaAccountDescription());
                ilDetails.setIlCoaGlInventoryAccountNumber(glInventoryAccount.getCoaAccountNumber());
                ilDetails.setIlCoaGlInventoryAccountDescription(glInventoryAccount.getCoaAccountDescription());
                ilDetails.setIlCoaGlCostOfSalesAccountNumber(glCostOfSalesAccount.getCoaAccountNumber());
                ilDetails.setIlCoaGlCostOfSalesAccountDescription(glCostOfSalesAccount.getCoaAccountDescription());
                ilDetails.setIlCoaGlWipAccountNumber(glWipAccount.getCoaAccountNumber());
                ilDetails.setIlCoaGlWipAccountDescription(glWipAccount.getCoaAccountDescription());

            }

            ilDetails.setIlCoaGlAccruedInventoryAccountNumber(glAccrdInvAccount.getCoaAccountNumber());
            ilDetails.setIlCoaGlAccruedInventoryAccountDescription(glAccrdInvAccount.getCoaAccountDescription());

            // - NEW SETTINGS
            LocalGlChartOfAccount glExpenseAccount = null;
            if (invItemLocation.getIlGlCoaExpenseAccount() != null) {

                glExpenseAccount = glChartOfAccountHome.findByPrimaryKey(invItemLocation.getIlGlCoaExpenseAccount());

                ilDetails.setIlCoaGlExpenseAccountNumber(glExpenseAccount.getCoaAccountNumber());
                ilDetails.setIlCoaGlExpenseAccountDescription(glExpenseAccount.getCoaAccountDescription());
            }

            ilDetails.setIlSubjectToCommission(invItemLocation.getIlSubjectToCommission());

            ilDetails.setIlIiName(
                    invItemLocation.getInvItem() != null ? invItemLocation.getInvItem().getIiName() : null);
            ilDetails.setIlIiDescription(
                    invItemLocation.getInvItem() != null ? invItemLocation.getInvItem().getIiDescription() : null);
            ilDetails.setIlLocName(
                    invItemLocation.getInvLocation() != null ? invItemLocation.getInvLocation().getLocName() : null);

            Collection adBranchItemLocations = invItemLocation.getAdBranchItemLocations();

            for (Object branchItemLocation : adBranchItemLocations) {

                LocalAdBranchItemLocation adBranchItemLocation = (LocalAdBranchItemLocation) branchItemLocation;

                AdModBranchItemLocationDetails bilDetails = new AdModBranchItemLocationDetails();

                bilDetails.setBilBrCode(adBranchItemLocation.getAdBranch().getBrCode());
                bilDetails.setBilBrName(adBranchItemLocation.getAdBranch().getBrName());
                bilDetails.setBilRack(adBranchItemLocation.getBilRack());
                bilDetails.setBilBin(adBranchItemLocation.getBilBin());
                bilDetails.setBilReorderPoint(adBranchItemLocation.getBilReorderPoint());
                bilDetails.setBilReorderQuantity(adBranchItemLocation.getBilReorderQuantity());

                // TODO: Review this logic and apply necessary adjustments
                if (!invItemCategory.equals(EJBCommon.DEFAULT)) {

                    glSalesAccount = glChartOfAccountHome
                            .findByPrimaryKey(adBranchItemLocation.getBilCoaGlSalesAccount());
                    glSalesReturnAccount = glChartOfAccountHome
                            .findByPrimaryKey(adBranchItemLocation.getBilCoaGlSalesReturnAccount());
                    glInventoryAccount = glChartOfAccountHome
                            .findByPrimaryKey(adBranchItemLocation.getBilCoaGlInventoryAccount());
                    glCostOfSalesAccount = glChartOfAccountHome
                            .findByPrimaryKey(adBranchItemLocation.getBilCoaGlCostOfSalesAccount());
                    glWipAccount = glChartOfAccountHome.findByPrimaryKey(adBranchItemLocation.getBilCoaGlWipAccount());

                    bilDetails.setBilCoaGlSalesAccountNumber(glSalesAccount.getCoaAccountNumber());
                    bilDetails.setBilCoaGlSalesAccountDescription(glSalesAccount.getCoaAccountDescription());
                    bilDetails.setBilCoaGlSalesReturnAccountNumber(glSalesReturnAccount.getCoaAccountNumber());
                    bilDetails
                            .setBilCoaGlSalesReturnAccountDescription(glSalesReturnAccount.getCoaAccountDescription());
                    bilDetails.setBilCoaGlInventoryAccountNumber(glInventoryAccount.getCoaAccountNumber());
                    bilDetails.setBilCoaGlInventoryAccountDescription(glInventoryAccount.getCoaAccountDescription());
                    bilDetails.setBilCoaGlCostOfSalesAccountNumber(glCostOfSalesAccount.getCoaAccountNumber());
                    bilDetails
                            .setBilCoaGlCostOfSalesAccountDescription(glCostOfSalesAccount.getCoaAccountDescription());
                    bilDetails.setBilCoaGlWipAccountNumber(glWipAccount.getCoaAccountNumber());
                    bilDetails.setBilCoaGlWipAccountDescription(glWipAccount.getCoaAccountDescription());

                } else {

                    // -- Expense Account for non-inventory item
                    // -- Make use to column BIL_COA_GL_EXPNS_ACCOUNT has a default value
                    // TODO: Review the default value that should be aligned with Branch Code e.g.
                    // 1100
                    glExpenseAccount = glChartOfAccountHome
                            .findByPrimaryKey(adBranchItemLocation.getBilCoaGlExpenseAccount());
                    bilDetails.setBilCoaGlExpenseAccountNumber(glExpenseAccount.getCoaAccountNumber());
                    bilDetails.setBilCoaGlExpenseAccountDescription(glExpenseAccount.getCoaAccountDescription());

                }

                // -- By default Accrued Expense is setup either inventory or non-inventory
                glAccrdInvAccount = glChartOfAccountHome
                        .findByPrimaryKey(adBranchItemLocation.getBilCoaGlAccruedInventoryAccount());
                bilDetails.setBilCoaGlAccruedInventoryAccountNumber(glAccrdInvAccount.getCoaAccountNumber());
                bilDetails.setBilCoaGlAccruedInventoryAccountDescription(glAccrdInvAccount.getCoaAccountDescription());

                bilDetails.setBilSubjectToCommission(adBranchItemLocation.getBilSubjectToCommission());
                bilDetails.setBilHist1Sales(adBranchItemLocation.getBilHist1Sales());
                bilDetails.setBilHist2Sales(adBranchItemLocation.getBilHist2Sales());
                bilDetails.setBilProjectedSales(adBranchItemLocation.getBilProjectedSales());
                bilDetails.setBilDeliveryTime(adBranchItemLocation.getBilDeliveryTime());
                bilDetails.setBilDeliveryBuffer(adBranchItemLocation.getBilDeliveryBuffer());
                bilDetails.setBilOrderPerYear(adBranchItemLocation.getBilOrderPerYear());

                ilDetails.saveBrIlList(bilDetails);
            }

            return ilDetails;

        }
        catch (GlobalNoRecordFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getAdPrfInvQuantityPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("InvItemLocationEntryControllerBean getAdPrfInvQuantityPrecisionUnit");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfInvQuantityPrecisionUnit();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void saveInvIlReorderPointAndReorderQuantity(ArrayList locationList, ArrayList itemList, Integer RS_CODE,
                                                        Integer AD_CMPNY) {

        Debug.print("InvItemLocationEntryControllerBean saveInvIlReorderPointAndReorderQuantity");

        try {

            String locationName = null;

            for (Object item : locationList) {

                locationName = (String) item;

                for (Object value : itemList) {

                    String itemName = (String) value;

                    // get inv item location
                    LocalInvItemLocation invItemLocation = null;

                    try {

                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(locationName, itemName, AD_CMPNY);

                    }
                    catch (FinderException ex) {

                    }

                    if (invItemLocation == null) {
                        continue;
                    }

                    // get all branch item locations

                    try {

                        LocalAdBranchItemLocation adHqBranchItemLocation = null;

                        double hqAverageSales = 0;

                        Collection adBranchItemLocations = adBranchItemLocationHome
                                .findBilByIlCodeAndRsCode(invItemLocation.getIlCode(), RS_CODE, AD_CMPNY);

                        for (Object branchItemLocation : adBranchItemLocations) {

                            LocalAdBranchItemLocation adBranchItemLocation = (LocalAdBranchItemLocation) branchItemLocation;

                            // set default variables
                            double histSales1 = adBranchItemLocation.getBilHist1Sales();
                            double histSales2 = adBranchItemLocation.getBilHist2Sales();
                            double projectedSales = adBranchItemLocation.getBilProjectedSales();

                            // find hist 1 actual sales
                            GregorianCalendar gcHistDateFrom = new GregorianCalendar();
                            gcHistDateFrom.set(Calendar.MONTH, 0);
                            gcHistDateFrom.set(Calendar.DATE, 1);
                            gcHistDateFrom.set(Calendar.HOUR, 0);
                            gcHistDateFrom.set(Calendar.MINUTE, 0);
                            gcHistDateFrom.set(Calendar.SECOND, 0);
                            gcHistDateFrom.add(Calendar.YEAR, -1);
                            GregorianCalendar gcHistDateTo = new GregorianCalendar();
                            gcHistDateTo.set(Calendar.MONTH, 11);
                            gcHistDateTo.set(Calendar.DATE, 31);
                            gcHistDateTo.set(Calendar.HOUR, 0);
                            gcHistDateTo.set(Calendar.MINUTE, 0);
                            gcHistDateTo.set(Calendar.SECOND, 0);
                            gcHistDateTo.add(Calendar.YEAR, -1);
                            double tempHistSales1 = 0;
                            Collection invHist1Costing = invCostingHome.findQtySoldByCstDateFromAndCstDateToAndIlCode(
                                    gcHistDateFrom.getTime(), gcHistDateTo.getTime(), invItemLocation.getIlCode(),
                                    adBranchItemLocation.getAdBranch().getBrCode(), AD_CMPNY);
                            Iterator invHist1CostingIter = invHist1Costing.iterator();
                            while (invHist1CostingIter.hasNext()) {

                                LocalInvCosting invCosting = (LocalInvCosting) invHist1CostingIter.next();
                                if (invCosting.getArSalesOrderInvoiceLine() != null) {

                                    LocalArInvoice arInvoice = invCosting.getArSalesOrderInvoiceLine().getArInvoice();
                                    tempHistSales1 += this.convertForeignToFunctionalCurrency(
                                            arInvoice.getGlFunctionalCurrency().getFcCode(),
                                            arInvoice.getGlFunctionalCurrency().getFcName(),
                                            arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(),
                                            invCosting.getArSalesOrderInvoiceLine().getSilAmount()
                                                    + invCosting.getArSalesOrderInvoiceLine().getSilTaxAmount(),
                                            AD_CMPNY);

                                } else if (invCosting.getArInvoiceLineItem() != null) {

                                    if (invCosting.getArInvoiceLineItem().getArInvoice() != null) {
                                        LocalArInvoice arInvoice = invCosting.getArInvoiceLineItem().getArInvoice();
                                        tempHistSales1 += this.convertForeignToFunctionalCurrency(
                                                arInvoice.getGlFunctionalCurrency().getFcCode(),
                                                arInvoice.getGlFunctionalCurrency().getFcName(),
                                                arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(),
                                                invCosting.getArInvoiceLineItem().getIliAmount()
                                                        + invCosting.getArInvoiceLineItem().getIliTaxAmount(),
                                                AD_CMPNY);
                                    } else {
                                        LocalArReceipt arReceipt = invCosting.getArInvoiceLineItem().getArReceipt();
                                        tempHistSales1 += this.convertForeignToFunctionalCurrency(
                                                arReceipt.getGlFunctionalCurrency().getFcCode(),
                                                arReceipt.getGlFunctionalCurrency().getFcName(),
                                                arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(),
                                                invCosting.getArInvoiceLineItem().getIliAmount()
                                                        + invCosting.getArInvoiceLineItem().getIliTaxAmount(),
                                                AD_CMPNY);
                                    }
                                }
                            }

                            if (tempHistSales1 > 0) {
                                histSales1 = tempHistSales1;
                            }

                            // find hist 2 actual sales
                            gcHistDateFrom.add(Calendar.YEAR, -1);
                            gcHistDateTo.add(Calendar.YEAR, -1);
                            double tempHistSales2 = 0;
                            Collection invHist2Costing = invCostingHome.findQtySoldByCstDateFromAndCstDateToAndIlCode(
                                    gcHistDateFrom.getTime(), gcHistDateTo.getTime(), invItemLocation.getIlCode(),
                                    adBranchItemLocation.getAdBranch().getBrCode(), AD_CMPNY);
                            Iterator invHist2CostingIter = invHist1Costing.iterator();
                            while (invHist1CostingIter.hasNext()) {

                                LocalInvCosting invCosting = (LocalInvCosting) invHist2CostingIter.next();
                                if (invCosting.getArSalesOrderInvoiceLine() != null) {

                                    LocalArInvoice arInvoice = invCosting.getArSalesOrderInvoiceLine().getArInvoice();
                                    tempHistSales2 += this.convertForeignToFunctionalCurrency(
                                            arInvoice.getGlFunctionalCurrency().getFcCode(),
                                            arInvoice.getGlFunctionalCurrency().getFcName(),
                                            arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(),
                                            invCosting.getArSalesOrderInvoiceLine().getSilAmount()
                                                    + invCosting.getArSalesOrderInvoiceLine().getSilTaxAmount(),
                                            AD_CMPNY);

                                } else if (invCosting.getArInvoiceLineItem() != null) {

                                    if (invCosting.getArInvoiceLineItem().getArInvoice() != null) {
                                        LocalArInvoice arInvoice = invCosting.getArInvoiceLineItem().getArInvoice();
                                        tempHistSales2 += this.convertForeignToFunctionalCurrency(
                                                arInvoice.getGlFunctionalCurrency().getFcCode(),
                                                arInvoice.getGlFunctionalCurrency().getFcName(),
                                                arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(),
                                                invCosting.getArInvoiceLineItem().getIliAmount()
                                                        + invCosting.getArInvoiceLineItem().getIliTaxAmount(),
                                                AD_CMPNY);
                                    } else {
                                        LocalArReceipt arReceipt = invCosting.getArInvoiceLineItem().getArReceipt();
                                        tempHistSales2 += this.convertForeignToFunctionalCurrency(
                                                arReceipt.getGlFunctionalCurrency().getFcCode(),
                                                arReceipt.getGlFunctionalCurrency().getFcName(),
                                                arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(),
                                                invCosting.getArInvoiceLineItem().getIliAmount()
                                                        + invCosting.getArInvoiceLineItem().getIliTaxAmount(),
                                                AD_CMPNY);
                                    }
                                }
                            }

                            if (tempHistSales2 > 0) {
                                histSales2 = tempHistSales2;
                            }

                            // get actual sales per month

                            GregorianCalendar gcDateFrom = new GregorianCalendar();
                            gcDateFrom.set(Calendar.MONTH, 0);
                            gcDateFrom.set(Calendar.DATE, 1);
                            gcDateFrom.set(Calendar.HOUR, 0);
                            gcDateFrom.set(Calendar.MINUTE, 0);
                            gcDateFrom.set(Calendar.SECOND, 0);
                            GregorianCalendar gcDateTo = new GregorianCalendar();
                            gcDateTo.set(Calendar.MONTH, 0);
                            gcDateTo.set(Calendar.DATE, 31);
                            gcDateTo.set(Calendar.HOUR, 0);
                            gcDateTo.set(Calendar.MINUTE, 0);
                            gcDateTo.set(Calendar.SECOND, 0);

                            double actualSales = 0;
                            int projectedSalesMonths = 0;
                            int actualSalesMonths = 0;
                            boolean isSalesFound = false;
                            for (int j = 0; j <= 12; j++) {

                                double runningActual = 0;
                                Collection invCostings = invCostingHome.findQtySoldByCstDateFromAndCstDateToAndIlCode(
                                        gcDateFrom.getTime(), gcDateTo.getTime(), invItemLocation.getIlCode(),
                                        adBranchItemLocation.getAdBranch().getBrCode(), AD_CMPNY);
                                for (Object costing : invCostings) {

                                    LocalInvCosting invCosting = (LocalInvCosting) costing;
                                    if (invCosting.getArSalesOrderInvoiceLine() != null) {

                                        LocalArInvoice arInvoice = invCosting.getArSalesOrderInvoiceLine()
                                                .getArInvoice();
                                        runningActual += this.convertForeignToFunctionalCurrency(
                                                arInvoice.getGlFunctionalCurrency().getFcCode(),
                                                arInvoice.getGlFunctionalCurrency().getFcName(),
                                                arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(),
                                                invCosting.getArSalesOrderInvoiceLine().getSilAmount()
                                                        + invCosting.getArSalesOrderInvoiceLine().getSilTaxAmount(),
                                                AD_CMPNY);

                                    } else if (invCosting.getArInvoiceLineItem() != null) {

                                        if (invCosting.getArInvoiceLineItem().getArInvoice() != null) {
                                            LocalArInvoice arInvoice = invCosting.getArInvoiceLineItem().getArInvoice();
                                            runningActual += this.convertForeignToFunctionalCurrency(
                                                    arInvoice.getGlFunctionalCurrency().getFcCode(),
                                                    arInvoice.getGlFunctionalCurrency().getFcName(),
                                                    arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(),
                                                    invCosting.getArInvoiceLineItem().getIliAmount()
                                                            + invCosting.getArInvoiceLineItem().getIliTaxAmount(),
                                                    AD_CMPNY);
                                        } else {
                                            LocalArReceipt arReceipt = invCosting.getArInvoiceLineItem().getArReceipt();
                                            runningActual += this.convertForeignToFunctionalCurrency(
                                                    arReceipt.getGlFunctionalCurrency().getFcCode(),
                                                    arReceipt.getGlFunctionalCurrency().getFcName(),
                                                    arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(),
                                                    invCosting.getArInvoiceLineItem().getIliAmount()
                                                            + invCosting.getArInvoiceLineItem().getIliTaxAmount(),
                                                    AD_CMPNY);
                                        }
                                    }
                                }

                                actualSales += runningActual;
                                if (runningActual == 0 && !isSalesFound) {
                                    projectedSalesMonths++;
                                } else {
                                    isSalesFound = true;
                                }
                                if (runningActual > 0) {
                                    actualSalesMonths++;
                                }

                                gcDateFrom.add(Calendar.MONTH, 1);
                                gcDateTo.add(Calendar.MONTH, 1);
                                gcDateTo.set(Calendar.DATE, gcDateTo.getActualMaximum(Calendar.DATE));
                            }

                            if (actualSales > 0) {
                                double aveMonthlyProjectedSales = projectedSales / 12;
                                aveMonthlyProjectedSales = aveMonthlyProjectedSales * projectedSalesMonths;
                                projectedSales = ((actualSales + aveMonthlyProjectedSales)
                                        / (projectedSalesMonths + actualSalesMonths) * 12);
                            }

                            int divisor = 0;

                            if (histSales1 != 0) {
                                divisor++;
                            }
                            if (histSales2 != 0) {
                                divisor++;
                            }
                            if (projectedSales != 0) {
                                divisor++;
                            }
                            if (divisor == 0) {
                                divisor = 1;
                            }

                            double averageSales = (histSales1 + histSales2 + projectedSales) / divisor;

                            if (adBranchItemLocation.getAdBranch().getBrHeadQuarter() == EJBCommon.FALSE) {

                                double deliveryBuffer = adBranchItemLocation.getBilDeliveryBuffer();
                                double deliveryTime = adBranchItemLocation.getBilDeliveryTime();
                                double dailyCons = averageSales / 360;
                                double minimumInventory = dailyCons * deliveryBuffer;
                                double reorderPoint = (dailyCons * deliveryTime) + minimumInventory;
                                double reorderQty = averageSales / adBranchItemLocation.getBilOrderPerYear();

                                reorderPoint = EJBCommon.roundIt(reorderPoint, (short) 3);
                                reorderQty = EJBCommon.roundIt(reorderQty, (short) 3);

                                // set reorder point & quantity
                                adBranchItemLocation.setBilReorderPoint(reorderPoint);
                                adBranchItemLocation.setBilReorderQuantity(reorderQty);
                                hqAverageSales += averageSales;

                            } else {

                                adHqBranchItemLocation = adBranchItemLocation;
                            }
                        }

                        if (adHqBranchItemLocation != null) {

                            double deliveryBuffer = adHqBranchItemLocation.getBilDeliveryBuffer();
                            double deliveryTime = adHqBranchItemLocation.getBilDeliveryTime();
                            double dailyCons = hqAverageSales / 360;
                            double minimumInventory = dailyCons * deliveryBuffer;
                            double reorderPoint = (dailyCons * deliveryTime) + minimumInventory;
                            double reorderQty = hqAverageSales / adHqBranchItemLocation.getBilOrderPerYear();

                            reorderPoint = EJBCommon.roundIt(reorderPoint, (short) 3);
                            reorderQty = EJBCommon.roundIt(reorderQty, (short) 3);

                            // set reorder point & quantity
                            adHqBranchItemLocation.setBilReorderPoint(reorderPoint);
                            adHqBranchItemLocation.setBilReorderQuantity(reorderQty);
                            adHqBranchItemLocation = null;
                        }

                    }
                    catch (FinderException ex) {

                    }
                }
            }

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private method
    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem,
                                                      double ADJST_QTY, Integer AD_CMPNY) {

        Debug.print("InvItemLocationEntryControllerBean convertByUomFromAndItemAndQuantity");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), AD_CMPNY);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(),
                            AD_CMPNY);

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

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE,
                                                      double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("InvItemLocationEntryControllerBean convertForeignToFunctionalCurrency");

        LocalAdCompany adCompany = null;

        // get company and extended precision

        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

        }
        catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // Convert to functional currency if necessary

        if (CONVERSION_RATE != 1 && CONVERSION_RATE != 0) {

            AMOUNT = AMOUNT * CONVERSION_RATE;

        } else if (CONVERSION_DATE != null) {

            try {

                // Get functional currency rate

                LocalGlFunctionalCurrencyRate glReceiptFunctionalCurrencyRate = null;

                if (!FC_NM.equals("USD")) {

                    glReceiptFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(FC_CODE,
                            CONVERSION_DATE, AD_CMPNY);

                    AMOUNT = AMOUNT * glReceiptFunctionalCurrencyRate.getFrXToUsd();
                }

                // Get set of book functional currency rate if necessary

                if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                    LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate = glFunctionalCurrencyRateHome
                            .findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), CONVERSION_DATE,
                                    AD_CMPNY);

                    AMOUNT = AMOUNT / glCompanyFunctionalCurrencyRate.getFrXToUsd();
                }

            }
            catch (Exception ex) {

                throw new EJBException(ex.getMessage());
            }
        }

        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    public void ejbCreate() throws CreateException {

        Debug.print("InvItemLocationEntryControllerBean ejbCreate");
    }

}