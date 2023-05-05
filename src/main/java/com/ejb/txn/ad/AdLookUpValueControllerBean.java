package com.ejb.txn.ad;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.*;

import jakarta.ejb.*;

import com.ejb.dao.ad.*;
import com.ejb.dao.ar.*;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ar.*;
import com.ejb.entities.inv.*;
import com.ejb.exception.ad.*;
import com.ejb.exception.global.*;
import com.util.*;
import com.util.ad.AdLookUpValueDetails;

@Stateless(name = "AdLookUpValueControllerEJB")
public class AdLookUpValueControllerBean extends EJBContextClass implements AdLookUpValueController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalAdLookUpHome adLookUpHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private LocalInvPhysicalInventoryHome invPhysicalInventoryHome;
    @EJB
    private LocalInvPriceLevelHome invPriceLevelHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;

    public ArrayList getAdLuAll(Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdLookUpValueControllerBean getAdLuAll");

        Collection adLookUps = null;
        LocalAdLookUp adLookUp;
        ArrayList list = new ArrayList();
        try {
            adLookUps = adLookUpHome.findLuAll(companyCode);
        } catch (FinderException ex) {
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        if (adLookUps.isEmpty()) {
            throw new GlobalNoRecordFoundException();
        }
        for (Object lookUp : adLookUps) {
            adLookUp = (LocalAdLookUp) lookUp;
            list.add(adLookUp.getLuName());
        }
        return list;
    }

    public ArrayList getAdLvByLuName(String lookUpName, Integer companyCode) throws AdLVNoLookUpValueFoundException, AdLUNoLookUpFoundException {

        Debug.print("AdLookUpValueControllerBean getAdLvByLuName");

        ArrayList lvAllList = new ArrayList();
        Collection adLookUpValues;
        try {
            adLookUpHome.findByLookUpName(lookUpName, companyCode);
        } catch (FinderException ex) {
            throw new AdLUNoLookUpFoundException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        try {
            adLookUpValues = adLookUpValueHome.findByLuName(lookUpName, companyCode);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        if (adLookUpValues.isEmpty()) {
            throw new AdLVNoLookUpValueFoundException();
        }
        for (Object lookUpValue : adLookUpValues) {
            LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;
            AdLookUpValueDetails details = new AdLookUpValueDetails();
            details.setLvCode(adLookUpValue.getLvCode());
            details.setLvName(adLookUpValue.getLvName());
            details.setLvDescription(adLookUpValue.getLvDescription());
            details.setLvMasterCode(adLookUpValue.getLvMasterCode());
            details.setLvEnable(adLookUpValue.getLvEnable());
            details.setLvDownloadStatus(adLookUpValue.getLvDownloadStatus());
            details.setLvAdCompany(adLookUpValue.getLvAdCompany());
            lvAllList.add(details);
        }
        return lvAllList;
    }

    public void addAdLvEntry(AdLookUpValueDetails details, String lookUpName, Integer companyCode) throws GlobalRecordAlreadyExistException, AdLVMasterCodeNotFoundException {

        Debug.print("AdLookUpValueControllerBean addAdLvEntry");

        LocalAdLookUpValue adLookUpValue;
        try {
            adLookUpValue = adLookUpValueHome.findByLuNameAndLvName(lookUpName, details.getLvName(), companyCode);
            throw new GlobalRecordAlreadyExistException();
        } catch (GlobalRecordAlreadyExistException ex) {
            throw ex;
        } catch (FinderException ex) {
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        try {
            // create new look up value
            adLookUpValue = adLookUpValueHome.create(details.getLvName(), details.getLvDescription(), details.getLvMasterCode(), details.getLvEnable(), 'N', companyCode);
            LocalAdLookUp adLookUp = adLookUpHome.findByLookUpName(lookUpName, companyCode);
            adLookUp.addAdLookUpValue(adLookUpValue);
            // if lookup value is INV PRICE LEVELS then add in AdPriceLevelBean
            if (lookUpName.equals("INV PRICE LEVEL")) {
                // loop in InvItem
                Collection invItems = invItemHome.findIiAll(companyCode);
                for (Object item : invItems) {
                    LocalInvItem invItem = (LocalInvItem) item;
                    LocalInvPriceLevel invPriceLevel = invPriceLevelHome.create(invItem.getIiSalesPrice(), 0d, invItem.getIiPercentMarkup(), invItem.getIiShippingCost(), adLookUpValue.getLvName(), 'N', companyCode);
                    invItem.addInvPriceLevel(invPriceLevel);
                }
            }
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateAdLvEntry(AdLookUpValueDetails details, String lookUpName, Integer companyCode) throws GlobalRecordAlreadyExistException, AdLVMasterCodeNotFoundException {

        Debug.print("AdLookUpValueControllerBean updateAdLvEntry");

        LocalAdLookUpValue adLookUpValue;
        LocalAdLookUp adLookUp;
        try {
            adLookUpValue = adLookUpValueHome.findByLuNameAndLvName(lookUpName, details.getLvName(), companyCode);
            if (!adLookUpValue.getLvCode().equals(details.getLvCode())) {
                throw new GlobalRecordAlreadyExistException();
            }
        } catch (GlobalRecordAlreadyExistException ex) {
            throw ex;
        } catch (FinderException ex) {
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        try {
            // find and update look up value
            adLookUpValue = adLookUpValueHome.findByPrimaryKey(details.getLvCode());
            String originalLvName = adLookUpValue.getLvName();
            adLookUpValue.setLvName(details.getLvName());
            adLookUpValue.setLvDescription(details.getLvDescription());
            adLookUpValue.setLvMasterCode(details.getLvMasterCode());
            adLookUpValue.setLvEnable(details.getLvEnable());
            adLookUpValue.setLvDownloadStatus('U');
            adLookUp = adLookUpHome.findByLookUpName(lookUpName, companyCode);
            adLookUp.addAdLookUpValue(adLookUpValue);
            switch (lookUpName) {
                case "AR FREIGHT": {
                    // update invoices
                    Collection arInvoices = arInvoiceHome.findByInvLvFreight(originalLvName, companyCode);
                    for (Object invoice : arInvoices) {
                        LocalArInvoice arInvoice = (LocalArInvoice) invoice;
                        arInvoice.setInvLvFreight(details.getLvName());
                    }
                    break;
                }
                case "AR REGION": {
                    // update customers
                    Collection arCustomers = arCustomerHome.findByCstRegion(originalLvName, companyCode);
                    for (Object customer : arCustomers) {
                        LocalArCustomer arCustomer = (LocalArCustomer) customer;
                        arCustomer.setCstAdLvRegion(details.getLvName());
                    }
                    break;
                }
                case "INV LOCATION TYPE":
                    // update locations
                    Collection invLocations = invLocationHome.findByLocAdLvType(originalLvName, companyCode);
                    for (Object location : invLocations) {
                        LocalInvLocation invLocation = (LocalInvLocation) location;
                        invLocation.setLocLvType(details.getLvName());
                    }
                    break;
                case "INV ITEM CATEGORY": {
                    // update items
                    Collection invItems = invItemHome.findEnabledIiByIiAdLvCategory(originalLvName, companyCode);
                    Iterator i = invItems.iterator();
                    while (i.hasNext()) {
                        LocalInvItem invItem = (LocalInvItem) i.next();
                        invItem.setIiAdLvCategory(details.getLvName());
                    }
                    // update physical inventories
                    Collection invPhysicalInventories = invPhysicalInventoryHome.findByPiAdLvCategory(originalLvName, companyCode);
                    i = invPhysicalInventories.iterator();
                    while (i.hasNext()) {
                        LocalInvPhysicalInventory invPhysicalInventory = (LocalInvPhysicalInventory) i.next();
                        invPhysicalInventory.setPiAdLvCategory(details.getLvName());
                    }
                    break;
                }
                case "INV UNIT OF MEASURE CLASS":
                    // update unit measures
                    Collection invUnitOfMeasures = invUnitOfMeasureHome.findByUomAdLvClass(originalLvName, companyCode);
                    for (Object unitOfMeasure : invUnitOfMeasures) {
                        LocalInvUnitOfMeasure invUnitOfMeasure = (LocalInvUnitOfMeasure) unitOfMeasure;
                        invUnitOfMeasure.setUomAdLvClass(details.getLvName());
                    }
                    break;
                case "INV SHIFT": {
                    // update invoices
                    Collection arInvoices = arInvoiceHome.findByInvLvShift(originalLvName, companyCode);
                    Iterator i = arInvoices.iterator();
                    while (i.hasNext()) {
                        LocalArInvoice arInvoice = (LocalArInvoice) i.next();
                        arInvoice.setInvLvShift(details.getLvName());
                    }
                    // update receipts
                    Collection arReceipts = arReceiptHome.findByRctLvShift(originalLvName, companyCode);
                    i = arReceipts.iterator();
                    while (i.hasNext()) {
                        LocalArReceipt arReceipt = (LocalArReceipt) i.next();
                        arReceipt.setRctLvShift(details.getLvName());
                    }
                    break;
                }
                case "INV DONENESS": {
                    // update items
                    Collection invItems = invItemHome.findByIiDoneness(originalLvName, companyCode);
                    for (Object item : invItems) {
                        LocalInvItem invItem = (LocalInvItem) item;
                        invItem.setIiDoneness(details.getLvName());
                    }
                    break;
                }
                case "INV PRICE LEVEL": {
                    // update price levels and customer deal price
                    Collection invPriceLevels = invPriceLevelHome.findByPlAdLvPriceLevel(originalLvName, companyCode);
                    Iterator i = invPriceLevels.iterator();
                    while (i.hasNext()) {
                        LocalInvPriceLevel invPriceLevel = (LocalInvPriceLevel) i.next();
                        invPriceLevel.setPlAdLvPriceLevel(details.getLvName());
                    }
                    Collection arCustomers = arCustomerHome.findByCstDealPrice(originalLvName, companyCode);
                    i = arCustomers.iterator();
                    while (i.hasNext()) {
                        LocalArCustomer arCustomer = (LocalArCustomer) i.next();
                        arCustomer.setCstDealPrice(details.getLvName());
                    }
                    break;
                }
            }
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteAdLvEntry(Integer lookUpValueCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException {

        Debug.print("AdLookUpValueControllerBean deleteAdLvEntry");

        LocalAdLookUpValue adLookUpValue;
        try {
            adLookUpValue = adLookUpValueHome.findByPrimaryKey(lookUpValueCode);
        } catch (FinderException ex) {
            throw new GlobalRecordAlreadyDeletedException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        try {
            Collection arInvoices1 = arInvoiceHome.findByInvLvFreight(adLookUpValue.getLvName(), companyCode);
            Collection arInvoices2 = arInvoiceHome.findByInvLvShift(adLookUpValue.getLvName(), companyCode);
            Collection arReceipt = arReceiptHome.findByRctLvShift(adLookUpValue.getLvName(), companyCode);
            Collection invItems1 = invItemHome.findEnabledIiByIiAdLvCategory(adLookUpValue.getLvName(), companyCode);
            Collection invItems2 = invItemHome.findByIiDoneness(adLookUpValue.getLvName(), companyCode);
            Collection invLocations = invLocationHome.findByLocAdLvType(adLookUpValue.getLvName(), companyCode);
            Collection invUnitOfMeasures = invUnitOfMeasureHome.findByUomAdLvClass(adLookUpValue.getLvName(), companyCode);
            Collection invPhysicalInventories = invPhysicalInventoryHome.findByPiAdLvCategory(adLookUpValue.getLvName(), companyCode);
            Collection arCustomers1 = arCustomerHome.findByCstDealPrice(adLookUpValue.getLvName(), companyCode);
            Collection arCustomers2 = arCustomerHome.findByCstRegion(adLookUpValue.getLvName(), companyCode);
            if (!arInvoices1.isEmpty() || !arInvoices2.isEmpty() || !arReceipt.isEmpty() || !invItems1.isEmpty() || !invItems2.isEmpty() || !invLocations.isEmpty() || !invUnitOfMeasures.isEmpty() || !invPhysicalInventories.isEmpty() || !arCustomers1.isEmpty() || !arCustomers2.isEmpty()) {
                throw new GlobalRecordAlreadyAssignedException();
            }
            if (arCustomers1.isEmpty()) {
                Collection invPriceLevels = invPriceLevelHome.findByPlAdLvPriceLevel(adLookUpValue.getLvName(), companyCode);
                for (Object priceLevel : invPriceLevels) {
                    LocalInvPriceLevel invPriceLevel = (LocalInvPriceLevel) priceLevel;
                    try {
                        em.remove(invPriceLevel);
                    } catch (RemoveException ex) {
                        getSessionContext().setRollbackOnly();
                        throw new EJBException(ex.getMessage());
                    }
                }
            }
        } catch (FinderException ex) {
        }
        try {
            em.remove(adLookUpValue);
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {
        Debug.print("AdLookUpValueControllerBean ejbCreate");
    }
}