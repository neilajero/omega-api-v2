/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class InvPriceLevelControllerBean
 * @created May 08, 2006, 5:30 PM
 * @author Neville P. Tagle
 */
package com.ejb.txn.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.dao.ad.LocalAdBranchItemLocationHome;
import com.ejb.dao.inv.LocalInvCostingHome;
import com.ejb.dao.inv.LocalInvItemHome;
import com.ejb.dao.inv.LocalInvItemLocationHome;
import com.ejb.dao.inv.LocalInvPriceLevelHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdBranchItemLocation;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.inv.LocalInvCosting;
import com.ejb.entities.inv.LocalInvItem;
import com.ejb.entities.inv.LocalInvItemLocation;
import com.ejb.entities.inv.LocalInvPriceLevel;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.inv.InvPriceLevelDetails;
import com.util.mod.inv.InvModItemDetails;

import jakarta.ejb.*;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Stateless(name = "InvPriceLevelControllerEJB")
public class InvPriceLevelControllerBean extends EJBContextClass implements InvPriceLevelController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvPriceLevelHome invPriceLevelHome;

    public ArrayList getInvPriceLevelsByIiCode(Integer II_CODE, Integer AD_CMPNY) {

        Debug.print("InvPriceLevelControllerBean getInvPriceLevelsByIiCode");

        ArrayList list = new ArrayList();

        try {

            Collection invPriceLevels = null;

            try {

                invPriceLevels = invPriceLevelHome.findByIiCode(II_CODE, AD_CMPNY);

            }
            catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            for (Object priceLevel : invPriceLevels) {

                LocalInvPriceLevel invPriceLevel = (LocalInvPriceLevel) priceLevel;

                InvPriceLevelDetails details = new InvPriceLevelDetails();

                details.setPlAmount(invPriceLevel.getPlAmount());
                details.setPlAdLvPriceLevel(invPriceLevel.getPlAdLvPriceLevel());
                details.setPlMargin(invPriceLevel.getPlMargin());
                details.setPlPercentMarkup(invPriceLevel.getPlPercentMarkup());
                details.setPlShippingCost(invPriceLevel.getPlShippingCost());

                list.add(details);
            }

            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void saveInvPl(ArrayList plList, Integer II_CODE, Integer AD_CMPNY) {

        Debug.print("InvPriceLevelControllerBean saveInvPl");

        LocalInvItemHome invItemHome = null;
        LocalInvPriceLevelHome invPriceLevelHome = null;
        LocalInvItemLocationHome invItemLocationHome = null;
        LocalAdBranchItemLocationHome adBranchItemLocationHome = null;

        try {

            invItemHome = (LocalInvItemHome) EJBHomeFactory.lookUpLocalHome(LocalInvItemHome.JNDI_NAME,
                    LocalInvItemHome.class);
            invPriceLevelHome = (LocalInvPriceLevelHome) EJBHomeFactory
                    .lookUpLocalHome(LocalInvPriceLevelHome.JNDI_NAME, LocalInvPriceLevelHome.class);
            invItemLocationHome = (LocalInvItemLocationHome) EJBHomeFactory
                    .lookUpLocalHome(LocalInvItemLocationHome.JNDI_NAME, LocalInvItemLocationHome.class);
            adBranchItemLocationHome = (LocalAdBranchItemLocationHome) EJBHomeFactory
                    .lookUpLocalHome(LocalAdBranchItemLocationHome.JNDI_NAME, LocalAdBranchItemLocationHome.class);

        }
        catch (NamingException ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            LocalInvItem invItem = null;

            try {

                invItem = invItemHome.findByPrimaryKey(II_CODE);

            }
            catch (FinderException ex) {

            }

            for (Object o : plList) {

                InvPriceLevelDetails details = (InvPriceLevelDetails) o;

                LocalInvPriceLevel invExistingPriceLevel = null;

                try {

                    invExistingPriceLevel = invPriceLevelHome.findByIiNameAndAdLvPriceLevel(invItem.getIiName(),
                            details.getPlAdLvPriceLevel(), AD_CMPNY);

                }
                catch (FinderException ex) {

                }

                if (invExistingPriceLevel == null) {

                    LocalInvPriceLevel invPriceLevel = invPriceLevelHome.create(details.getPlAmount(),
                            details.getPlMargin(), details.getPlPercentMarkup(), details.getPlShippingCost(),
                            details.getPlAdLvPriceLevel(), 'N', AD_CMPNY);

                    invPriceLevel.setInvItem(invItem);

                } else {

                    // update
                    invExistingPriceLevel.setPlAmount(details.getPlAmount());
                    invExistingPriceLevel.setPlMargin(details.getPlMargin());
                    invExistingPriceLevel.setPlPercentMarkup(details.getPlPercentMarkup());
                    invExistingPriceLevel.setPlShippingCost(details.getPlShippingCost());
                    invExistingPriceLevel.setPlAdLvPriceLevel(details.getPlAdLvPriceLevel());
                    invExistingPriceLevel.setPlAdCompany(AD_CMPNY);

                    if (invExistingPriceLevel.getPlDownloadStatus() == 'N') {
                        invExistingPriceLevel.setPlDownloadStatus('N');
                    } else if (invExistingPriceLevel.getPlDownloadStatus() == 'D') {
                        invExistingPriceLevel.setPlDownloadStatus('X');
                    }

                    invExistingPriceLevel.setInvItem(invItem);
                }
            }

            try {
                LocalInvItemLocation invItemLocation = null;

                Collection invItemLocations = invItemLocationHome.findByIiName(invItem.getIiName(), AD_CMPNY);

                for (Object itemLocation : invItemLocations) {

                    invItemLocation = (LocalInvItemLocation) itemLocation;

                    Collection adBranchItemLocations = adBranchItemLocationHome
                            .findByInvIlAll(invItemLocation.getIlCode(), AD_CMPNY);

                    for (Object branchItemLocation : adBranchItemLocations) {
                        LocalAdBranchItemLocation adBranchItemLocation = (LocalAdBranchItemLocation) branchItemLocation;

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
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("InvPriceLevelControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public InvModItemDetails getInvIiMarkupValueByIiCode(Integer II_CODE, Integer AD_CMPNY) {

        Debug.print("InvPriceLevelControllerBean getInvIiMarkupValueByIiCode");

        try {

            LocalInvItem invItem = null;

            try {

                invItem = invItemHome.findByPrimaryKey(II_CODE);

            }
            catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            InvModItemDetails mdetails = new InvModItemDetails();

            mdetails.setIiUnitCost(invItem.getIiUnitCost());
            mdetails.setIiShippingCost(invItem.getIiShippingCost());
            mdetails.setIiAveCost(this.getIiAveCost(invItem, new Date(), AD_CMPNY));
            mdetails.setIiPercentMarkup(invItem.getIiPercentMarkup());
            mdetails.setIiSalesPrice(invItem.getIiSalesPrice());

            return mdetails;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private double getIiAveCost(LocalInvItem invItem, Date currentDate, Integer AD_CMPNY) {

        Debug.print("InvPriceLevelEntryControllerBean getIiAveCost");

        double COST = 0d;
        LocalInvCosting invCosting = null;
        LocalAdBranch adBranch = null;
        LocalAdPreference adPreference = null;

        try {

            adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            adPreference.getPrfInvCentralWarehouse();

            // TODO: Need to make sure central warehouse has value to avoid this hard-coded
            // fallback
            String centralWareHouse = !adPreference.getPrfInvCentralWarehouse().equals("") && adPreference.getPrfInvCentralWarehouse() != null
                    ? adPreference.getPrfInvCentralWarehouse()
                    : "HO";
            String itemName = invItem.getIiName();

            // TODO: This is a temporary setup to overwrite the selected branch
            adBranch = adBranchHome.findByBrName(centralWareHouse, AD_CMPNY);

            if (adBranch != null) {

                invCosting = invCostingHome
                        .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(currentDate,
                                itemName, centralWareHouse, adBranch.getBrCode(), AD_CMPNY);

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

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }

        return COST;
    }

    private ArrayList getAdBrAll(Integer AD_CMPNY) {

        Debug.print("InvPriceLevlControllerBean getAdBrAll");

        ArrayList list = new ArrayList();

        try {

            Collection adBranches = adBranchHome.findBrAll(AD_CMPNY);
            for (Object branch : adBranches) {
                LocalAdBranch adBranch = (LocalAdBranch) branch;
                list.add(adBranch.getBrCode());
            }

            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("InvPriceLevelControllerBean ejbCreate");
    }

}