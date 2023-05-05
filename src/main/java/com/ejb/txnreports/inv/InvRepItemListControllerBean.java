/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class InvRepItemListControllerBean
 * @created Aug 26, 2004, 9:59 AM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txnreports.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.inv.LocalInvItemHome;
import com.ejb.dao.inv.LocalInvLocationHome;
import com.ejb.dao.inv.LocalInvPriceLevelHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.inv.LocalInvItem;
import com.ejb.entities.inv.LocalInvLocation;
import com.ejb.entities.inv.LocalInvPriceLevel;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.ad.AdCompanyDetails;
import com.util.inv.InvPriceLevelDetails;
import com.util.reports.inv.InvRepItemListDetails;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

@Stateless(name = "InvRepItemListControllerEJB")
public class InvRepItemListControllerBean extends EJBContextClass implements InvRepItemListController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalInvPriceLevelHome invPriceLevelHome;

    public ArrayList executeInvRepItemList(HashMap criteria, String ORDER_BY, ArrayList priceLevelList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvRepItemListControllerBean executeInvRepItemList");
        ArrayList list = new ArrayList();
        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(ii) FROM InvItem ii ");

            boolean fixedAsset = false;
            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("itemName")) {

                criteriaSize--;
            }

            if (criteria.containsKey("supplier")) {

                criteriaSize--;
            }

            if ((Byte) criteria.get("fixedAsset") == 0) {
                criteriaSize--;
            }

            if (criteria.containsKey("location")) {

                jbossQl.append(", IN(ii.invItemLocations) il ");
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("itemName")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("ii.iiName LIKE '%").append(criteria.get("itemName")).append("%' ");
            }
            Debug.print("CheckPoint executeInvRepStockOnHand B");
            if (criteria.containsKey("itemCategory")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("ii.iiAdLvCategory=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("itemCategory");
                ctr++;
            }

            if (criteria.containsKey("itemClass")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("ii.iiClass=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("itemClass");
                ctr++;
            }

            if (criteria.containsKey("fixedAsset")) {

                if ((Byte) criteria.get("fixedAsset") != 0) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("ii.iiFixedAsset=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("fixedAsset");
                    ctr++;
                    fixedAsset = true;
                }
            }

            if (criteria.containsKey("enable") && criteria.containsKey("disable")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("(ii.iiEnable=?").append(ctr + 1).append(" OR ");
                obj[ctr] = EJBCommon.TRUE;
                ctr++;

                jbossQl.append("ii.iiEnable=?").append(ctr + 1).append(") ");
                obj[ctr] = EJBCommon.FALSE;
                ctr++;

            } else {
                Debug.print("CheckPoint executeInvRepStockOnHand C");
                if (criteria.containsKey("enable")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("ii.iiEnable=?").append(ctr + 1).append(" ");
                    obj[ctr] = EJBCommon.TRUE;
                    ctr++;
                }

                if (criteria.containsKey("disable")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("ii.iiEnable=?").append(ctr + 1).append(" ");
                    obj[ctr] = EJBCommon.FALSE;
                    ctr++;
                }
            }

            if (criteria.containsKey("location")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("il.invLocation.locName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("location");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("ii.iiNonInventoriable=0 AND ii.iiAdCompany=").append(AD_CMPNY).append(" ");

            String orderBy = null;

            switch (ORDER_BY) {
                case "ITEM NAME":

                    orderBy = "ii.iiName";

                    break;
                case "ITEM CLASS":

                    orderBy = "ii.iiClass";

                    break;
                case "ITEM CATEGORY":

                    orderBy = "ii.iiAdLvCategory";
                    break;
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy);
            }

            Collection invItems = invItemHome.getIiByCriteria(jbossQl.toString(), obj, 0, 0);

            if (invItems.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            Iterator i = invItems.iterator();
            Debug.print("CheckPoint executeInvRepStockOnHand D");
            while (i.hasNext()) {

                LocalInvItem invItem = (LocalInvItem) i.next();
                LocalInvLocation itemlocation = null;
                String locName = null;
                if (invItem.getIiDefaultLocation() != null) {
                    itemlocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                }

                InvRepItemListDetails details = new InvRepItemListDetails();
                details.setIlIiName(invItem.getIiName());
                details.setIlIiDescription(invItem.getIiDescription());
                details.setIlIiClass(invItem.getIiClass());
                details.setIlIiEnable(invItem.getIiEnable());
                details.setIlIiUomName(invItem.getInvUnitOfMeasure().getUomName());
                details.setIlIiUnitCost(invItem.getIiUnitCost());
                details.setIlIiPrice(invItem.getIiSalesPrice());
                details.setIlIiCostMethod(invItem.getIiCostMethod());
                details.setIlIiPartNumber(invItem.getIiPartNumber());

                details.setIlIiCategoryName(invItem.getIiAdLvCategory());
                details.setIiDateAcquired(invItem.getIiDateAcquired());

                try {
                    details.setIlIiLctn(itemlocation.getLocName());
                    details.setIlIiDprtmnt(itemlocation.getLocDepartment());
                    details.setIlIiBrnch(itemlocation.getLocBranch());
                } catch (Exception e) {
                    details.setIlIiLctn("");
                    details.setIlIiDprtmnt("");
                    details.setIlIiBrnch("");
                }

                if (criteria.get("supplier").toString().equals("1")) {
                    if (invItem.getApSupplier() != null) {
                        details.setIlIiSupplierName(invItem.getApSupplier().getSplName());
                    }
                }

                if (!priceLevelList.isEmpty()) {

                    ArrayList priceLevels = new ArrayList();

                    for (Object o : priceLevelList) {

                        String PL_AD_LV_PRC_LVL = (String) o;

                        LocalInvPriceLevel invPriceLevel = invPriceLevelHome.findByIiNameAndAdLvPriceLevel(invItem.getIiName(), PL_AD_LV_PRC_LVL, AD_CMPNY);

                        InvPriceLevelDetails pdetails = new InvPriceLevelDetails();

                        pdetails.setPlAdLvPriceLevel(invPriceLevel.getPlAdLvPriceLevel());
                        pdetails.setPlAmount(invPriceLevel.getPlAmount());
                        priceLevels.add(pdetails);
                    }
                    details.setIiPriceLevels(priceLevels);
                }

                list.add(details);
            }

            if (list.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("InvRepItemListControllerBean getAdCompany");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("InvRepItemListControllerBean ejbCreate");
    }

}