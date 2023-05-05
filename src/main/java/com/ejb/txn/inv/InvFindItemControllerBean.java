/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class InvFindItemControllerBean
 * @created June 3, 2004, 10:44 AM
 * @author Enrico C. Yap
 */
package com.ejb.txn.inv;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdLookUpValue;
import com.ejb.dao.ad.LocalAdLookUpValueHome;
import com.ejb.entities.ar.LocalArCustomer;
import com.ejb.dao.ar.LocalArCustomerHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.inv.LocalInvCosting;
import com.ejb.dao.inv.LocalInvCostingHome;
import com.ejb.entities.inv.LocalInvItem;
import com.ejb.dao.inv.LocalInvItemHome;
import com.ejb.entities.inv.LocalInvLocation;
import com.ejb.dao.inv.LocalInvLocationHome;
import com.ejb.entities.inv.LocalInvPriceLevel;
import com.ejb.dao.inv.LocalInvPriceLevelHome;
import com.ejb.entities.inv.LocalInvUnitOfMeasureConversion;
import com.ejb.dao.inv.LocalInvUnitOfMeasureConversionHome;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.mod.inv.InvModItemDetails;

@Stateless(name = "InvFindItemControllerEJB")
public class InvFindItemControllerBean extends EJBContextClass implements InvFindItemController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalInvPriceLevelHome invPriceLevelHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;

    public double getIiSalesPriceByInvCstCustomerCodeAndIiNameAndUomName(String CST_CSTMR_CODE, String II_NM,
                                                                         String UOM_NM, Integer AD_CMPNY) {

        Debug.print("InvFindItemControllerBean getInvIiSalesPriceByIiNameAndUomName");

        try {

            LocalArCustomer arCustomer = null;
            try {
                arCustomer = arCustomerHome.findByCstCustomerCode(CST_CSTMR_CODE, AD_CMPNY);
            } catch (FinderException ex) {
                // return 0d;
            }

            double unitPrice = 0d;

            LocalInvItem invItem = invItemHome.findByIiName(II_NM, AD_CMPNY);
            LocalInvPriceLevel invPriceLevel;
            if (arCustomer != null) {
                try {
                    invPriceLevel = invPriceLevelHome.findByIiNameAndAdLvPriceLevel(II_NM, arCustomer.getCstDealPrice(), AD_CMPNY);
                    if (invPriceLevel.getPlAmount() == 0) {
                        unitPrice = invItem.getIiSalesPrice();
                    } else {
                        unitPrice = invPriceLevel.getPlAmount();
                    }
                } catch (FinderException ex) {
                    unitPrice = invItem.getIiSalesPrice();
                }
            } else {
                unitPrice = invItem.getIiSalesPrice();
            }

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(II_NM, UOM_NM, AD_CMPNY);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(II_NM, invItem.getInvUnitOfMeasure().getUomName(), AD_CMPNY);

            return EJBCommon.roundIt(unitPrice * invDefaultUomConversion.getUmcConversionFactor()
                    / invUnitOfMeasureConversion.getUmcConversionFactor(), this.getGlFcPrecisionUnit(AD_CMPNY));

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public String getIiDealPriceByInvCstCustomerCodeAndIiNameAndUomName(String CST_CSTMR_CODE, String II_NM,
                                                                        String UOM_NM, Integer AD_CMPNY) {

        Debug.print("InvFindItemControllerBean getIiDealPriceByInvCstCustomerCodeAndIiNameAndUomName");

        String dealPrice = "";
        try {
            LocalArCustomer arCustomer = null;
            try {
                arCustomer = arCustomerHome.findByCstCustomerCode(CST_CSTMR_CODE, AD_CMPNY);
            } catch (FinderException ex) {
                // return 0d;
            }
            if (arCustomer != null) {
                dealPrice = arCustomer.getCstDealPrice();
            }
            return dealPrice;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public double getInvCstRemainingQuantityByIiNameAndLocNameAndUomName(String II_NM, String LOC_NM, String UOM_NM,
                                                                         Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("InvFindItemControllerBean getInvCstRemainingQuantityByIiNameAndLocNameAndUomName");

        LocalInvCosting invCosting;
        double QTY;

        try {
            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndIiNameAndLocName(II_NM, LOC_NM, AD_BRNCH, AD_CMPNY);
            QTY = invCosting.getCstRemainingQuantity();
            return QTY;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdLvInvItemCategoryAll(Integer AD_CMPNY) {
        Debug.print("InvFindItemControllerBean getAdLvInvItemCategoryAll");

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

    public ArrayList getInvIiByCriteria(HashMap criteria, Integer OFFSET, Integer LIMIT, String ORDER_BY,
                                        Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("InvFindItemControllerBean getInvIiByCriteria");

        ArrayList list = new ArrayList();

        try {

            StringBuilder jbossQl = new StringBuilder();
            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();
            Object[] obj = generateSQLCriteria(criteria, ORDER_BY, AD_CMPNY, jbossQl, firstArgument, ctr, criteriaSize);
            Collection invItems = invItemHome.getIiByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

            String customerCode = (String) criteria.get("customer");
            if (invItems.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            for (Object item : invItems) {
                LocalInvItem invItem = (LocalInvItem) item;
                InvModItemDetails mdetails = new InvModItemDetails();
                mdetails.setIiCode(invItem.getIiCode());
                mdetails.setIiName(invItem.getIiName());
                mdetails.setIiDescription(invItem.getIiDescription());
                mdetails.setIiClass(invItem.getIiClass());
                mdetails.setIiPartNumber(invItem.getIiPartNumber());
                mdetails.setIiShortName(invItem.getIiShortName());
                mdetails.setIiBarCode1(invItem.getIiBarCode1());
                mdetails.setIiBarCode2(invItem.getIiBarCode2());
                mdetails.setIiBarCode3(invItem.getIiBarCode3());
                mdetails.setIiUomName(invItem.getInvUnitOfMeasure().getUomName());
                mdetails.setIiAdLvCategory(invItem.getIiAdLvCategory());
                mdetails.setIiCostMethod(invItem.getIiCostMethod());
                mdetails.setIiUnitCost(invItem.getIiUnitCost());
                mdetails.setIiPercentMarkup(invItem.getIiPercentMarkup());
                mdetails.setIiEnable(invItem.getIiEnable());

                String defaultLocation;
                if (invItem.getIiDefaultLocation() != null) {
                    LocalInvLocation invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                    defaultLocation = invLocation.getLocName();
                } else {
                    // This is to make sure the default location will be assigned to the selected branch (warehouse, store)
                    LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(AD_BRNCH);
                    defaultLocation = adBranch.getBrName();
                }
                mdetails.setIiDefaultLocationName(defaultLocation);

                double quantityOnHand = 0d;
                try {
                    quantityOnHand = this.getInvCstRemainingQuantityByIiNameAndLocNameAndUomName(invItem.getIiName(),
                            defaultLocation, invItem.getInvUnitOfMeasure().getUomName(), AD_BRNCH, AD_CMPNY);
                } catch (Exception ex) {
                }

                double salesPriceCustomer = this.getIiSalesPriceByInvCstCustomerCodeAndIiNameAndUomName(customerCode,
                        invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), AD_CMPNY);

                mdetails.setIiSalesPrice(salesPriceCustomer);

                String dealPrice = this.getIiDealPriceByInvCstCustomerCodeAndIiNameAndUomName(customerCode,
                        invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), AD_CMPNY);

                mdetails.setIiDealPrice(dealPrice);
                mdetails.setIiQuantityOnHand(quantityOnHand);

                list.add(mdetails);
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer getInvIiSizeByCriteria(HashMap criteria, Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalNoRecordFoundException {
        Debug.print("InvFindItemControllerBean getInvIiSizeByCriteria");

        try {
            StringBuilder jbossQl = new StringBuilder();
            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();
            Object[] obj = generateSQLCriteria(criteria, "", AD_CMPNY, jbossQl, firstArgument, ctr, criteriaSize);
            Collection invItems = invItemHome.getIiByCriteria(jbossQl.toString(), obj, 0, 0);
            if (invItems.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }
            return invItems.size();

        } catch (GlobalNoRecordFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {
        Debug.print("InvFindItemControllerBean getGlFcPrecisionUnit");
        try {
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            return adCompany.getGlFunctionalCurrency().getFcPrecision();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private Object[] generateSQLCriteria(HashMap criteria, String ORDER_BY, Integer AD_CMPNY, StringBuilder jbossQl,
                                         boolean firstArgument, short ctr, int criteriaSize) {
        Debug.print("InvFindItemControllerBean generateSQLCriteria");

        Object[] obj;
        jbossQl.append("SELECT OBJECT(ii) FROM InvItem ii ");

        // Allocate the size of the object parameter
        if (criteria.containsKey("customer")) {
            criteriaSize--;
        }

        if (criteria.containsKey("itemName")) {
            criteriaSize--;
        }

        if (criteria.containsKey("itemClass")) {
            criteriaSize--;
        }

        if (criteria.containsKey("category")) {
            criteriaSize--;
        }

        if (criteria.containsKey("costMethod")) {
            criteriaSize--;
        }

        if (criteria.containsKey("itemDescription")) {
            criteriaSize--;
        }

        obj = new Object[criteriaSize];
        if (criteria.containsKey("itemName")) {
            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append(" WHERE ");
            }
            jbossQl.append("ii.iiName LIKE '%").append(criteria.get("itemName")).append("%' ");
        }

        if (criteria.containsKey("itemClass")) {
            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append(" WHERE ");
            }
            jbossQl.append("ii.iiClass LIKE '%").append(criteria.get("itemClass")).append("%' ");
        }

        if (criteria.containsKey("category")) {
            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append(" WHERE ");
            }
            jbossQl.append("ii.iiAdLvCategory = '").append(criteria.get("category")).append("' ");
        }

        if (criteria.containsKey("enable") && criteria.containsKey("disable")) {
            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append(" WHERE ");
            }
            Byte enable = (Byte) criteria.get("enable");
            Byte disable = (Byte) criteria.get("disable");

            if (enable == EJBCommon.TRUE && disable == EJBCommon.TRUE) {
                jbossQl.append("(ii.iiEnable=?").append(ctr + 1).append(" OR ");
                obj[ctr] = EJBCommon.TRUE;
                ctr++;
                jbossQl.append("ii.iiEnable=?").append(ctr + 1).append(") ");
                obj[ctr] = EJBCommon.FALSE;
                ctr++;
            } else {
                jbossQl.append("(ii.iiEnable=?").append(ctr + 1).append(" OR ");
                obj[ctr] = EJBCommon.TRUE;
                ctr++;
                jbossQl.append("ii.iiEnable=?").append(ctr + 1).append(") ");
                obj[ctr] = EJBCommon.TRUE;
                ctr++;
            }
        } else {
            if (criteria.containsKey("enable")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append(" WHERE ");
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
                    jbossQl.append(" WHERE ");
                }
                jbossQl.append("ii.iiEnable=?").append(ctr + 1).append(" ");
                obj[ctr] = EJBCommon.FALSE;
                ctr++;
            }
        }

        if (criteria.containsKey("ScMonday")) {
            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append(" WHERE ");
            }
            jbossQl.append("ii.iiScMonday=?").append(ctr + 1).append(" ");
            obj[ctr] = EJBCommon.TRUE;
            ctr++;
        }

        if (criteria.containsKey("ScTuesday")) {
            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append(" WHERE ");
            }
            jbossQl.append("ii.iiScTuesday=?").append(ctr + 1).append(" ");
            obj[ctr] = EJBCommon.TRUE;
            ctr++;
        }

        if (criteria.containsKey("ScWednesday")) {
            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append(" WHERE ");
            }
            jbossQl.append("ii.iiScWednesday=?").append(ctr + 1).append(" ");
            obj[ctr] = EJBCommon.TRUE;
            ctr++;
        }

        if (criteria.containsKey("ScThursday")) {
            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append(" WHERE ");
            }
            jbossQl.append("ii.iiScThursday=?").append(ctr + 1).append(" ");
            obj[ctr] = EJBCommon.TRUE;
            ctr++;
        }

        if (criteria.containsKey("ScFriday")) {
            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append(" WHERE ");
            }
            jbossQl.append("ii.iiScFriday=?").append(ctr + 1).append(" ");
            obj[ctr] = EJBCommon.TRUE;
            ctr++;
        }

        if (criteria.containsKey("ScSaturday")) {
            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append(" WHERE ");
            }
            jbossQl.append("ii.iiScSaturday=?").append(ctr + 1).append(" ");
            obj[ctr] = EJBCommon.TRUE;
            ctr++;
        }

        if (criteria.containsKey("ScSunday")) {
            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append(" WHERE ");
            }
            jbossQl.append("ii.iiScSunday=?").append(ctr + 1).append(" ");
            obj[ctr] = EJBCommon.TRUE;
            ctr++;
        }

        if (criteria.containsKey("isInventoriable")) {
            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append(" WHERE ");
            }

            jbossQl.append("ii.iiNonInventoriable=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("isInventoriable");
            ctr++;
        }

        if (criteria.containsKey("itemDescription")) {
            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append(" WHERE ");
            }
            jbossQl.append("ii.iiDescription LIKE '%").append(criteria.get("itemDescription")).append("%' ");
        }

        if (criteria.containsKey("partNumber")) {
            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append(" WHERE ");
            }
            jbossQl.append("ii.iiPartNumber=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("partNumber");
            ctr++;
        }

        if (!firstArgument) {
            jbossQl.append("AND ");
        } else {
            jbossQl.append(" WHERE ");
        }
        jbossQl.append("ii.iiAdCompany=").append(AD_CMPNY).append(" ");

        if (ORDER_BY != null && ORDER_BY.equalsIgnoreCase("")) {
            String orderBy;

            switch (ORDER_BY) {
                case "ITEM CLASS":
                    orderBy = "ii.iiClass";
                    break;
                case "ITEM CATEGORY":
                    orderBy = "ii.iiAdLvCategory";
                    break;
                case "ITEM NAME":
                    orderBy = "ii.iiName";
                    break;
                default:
                    orderBy = "ii.iiName";
                    break;
            }
            jbossQl.append("ORDER BY ").append(orderBy);
        }

        return obj;
    }

    public void ejbCreate() throws CreateException {
        Debug.print("InvFindItemControllerBean ejbCreate");
    }
}