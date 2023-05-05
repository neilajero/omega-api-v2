/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class InvRepCostOfSalesControllerBean
 * @created
 * @author
 */
package com.ejb.txnreports.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.inv.LocalInvCostingHome;
import com.ejb.entities.ad.LocalAdBranchItemLocation;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.inv.LocalInvItemLocation;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.reports.inv.InvRepCostOfSaleDetails;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import java.util.*;

@Stateless(name = "InvRepCostOfSalesControllerEJB")
public class InvRepCostOfSalesControllerBean extends EJBContextClass implements InvRepCostOfSalesController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalInvCostingHome invCostingHome;

    public ArrayList executeInvRepCostOfSales(HashMap criteria, String costingMethod, ArrayList categoryList, ArrayList branchList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvRepCostOfSalesControllerBean executeInvRepCostOfSales");
        
        ArrayList list = new ArrayList();
        try {
            StringBuffer jbossQl = new StringBuffer();
            Object[] obj = this.generateCriteriaQueryItemLocation(criteria, categoryList, branchList, AD_CMPNY, jbossQl);
            Collection adBranchItemLocations = invCostingHome.getCstByCriteria(jbossQl.toString(), obj, 0, 0);
            this.getInvRepCostingOfSalesDetailsByItemLocation(criteria, AD_CMPNY, list, adBranchItemLocations);

            if (list.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }
            return list;

        } catch (GlobalNoRecordFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private Object[] generateCriteriaQueryItemLocation(HashMap criteria, ArrayList categoryList, ArrayList branchList, Integer AD_CMPNY, StringBuffer jbossQl) throws GlobalNoRecordFoundException {

        Object[] obj;
        jbossQl.append("SELECT OBJECT(bil) FROM AdBranchItemLocation bil ");

        boolean firstArgument = true;
        short ctr = 0;
        int criteriaSize = 0;


        if (branchList.isEmpty()) {

            throw new GlobalNoRecordFoundException();

        } else {

            jbossQl.append(" WHERE bil.adBranch.brCode in (");

            boolean firstLoop = true;

            for (Object o : branchList) {

                if (firstLoop == false) {
                    jbossQl.append(", ");
                } else {
                    firstLoop = false;
                }

                AdBranchDetails mdetails = (AdBranchDetails) o;

                jbossQl.append(mdetails.getBrCode());
            }

            jbossQl.append(") ");

            firstArgument = false;
        }

        // Allocate the size of the object parameter

        if (criteria.containsKey("location")) {

            criteriaSize++;
        }

        if (criteria.containsKey("dateFrom")) {

            //  criteriaSize++;
        }

        if (criteria.containsKey("dateTo")) {

            //  criteriaSize++;
        }

        if (criteria.containsKey("itemClass")) {

            criteriaSize++;
        }


        obj = new Object[criteriaSize + categoryList.size()];

        if (criteria.containsKey("itemName")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("bil.invItemLocation.invItem.iiName  LIKE '%").append(criteria.get("itemName")).append("%' ");
        }

        Debug.print("categoryList.size(): " + categoryList.size());

        if (!categoryList.isEmpty()) {

            Iterator iter = categoryList.iterator();

            jbossQl.append(" AND ( ");

            boolean isfirstcategory = true;

            while (iter.hasNext()) {

                String category = (String) iter.next();

                if (!isfirstcategory) {
                    jbossQl.append(" OR");
                }

                isfirstcategory = false;

                jbossQl.append(" bil.invItemLocation.invItem.iiAdLvCategory=?").append(ctr + 1);
                obj[ctr] = category;
                ctr++;
            }

            jbossQl.append(" ) ");
        }

        if (criteria.containsKey("location")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("bil.invItemLocation.invLocation.locName=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("location");
            ctr++;
        }
        if (criteria.containsKey("itemClass")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("bil.invItemLocation.invItem.iiClass=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("itemClass");
            ctr++;
        }

        if (!firstArgument) {

            jbossQl.append("AND ");

        } else {

            firstArgument = false;
            jbossQl.append("WHERE ");
        }

        //   jbossQl.append("cst.cstQuantitySold <> 0 AND bil.bilAdCompany=" + AD_CMPNY + " ");

        jbossQl.append("bil.bilAdCompany=").append(AD_CMPNY).append(" ");

        jbossQl.append("ORDER BY bil.invItemLocation.invItem.iiName, bil.adBranch.brCode");
        return obj;
    }

    private void getInvRepCostingOfSalesDetailsByItemLocation(HashMap criteria, Integer AD_CMPNY, ArrayList list, Collection adBranchItemLocations) {
        // double SLS_QTY_SLD = 0d;
        double SLS_AMOUNT = 0d;
        double COS = 0d;

        double taxRate = 0d;

        for (Object branchItemLocation : adBranchItemLocations) {

            LocalAdBranchItemLocation adBranchItemLocation = (LocalAdBranchItemLocation) branchItemLocation;

            InvRepCostOfSaleDetails details = new InvRepCostOfSaleDetails();

            LocalInvItemLocation invItemLocation = adBranchItemLocation.getInvItemLocation();

            details.setCsItemName(invItemLocation.getInvItem().getIiName());
            details.setCsItemDescription(invItemLocation.getInvItem().getIiDescription());
            details.setCsDate(new Date());
            details.setCsQuantitySold(0);
            details.setCsUnitCost(0);
            details.setCsUnit(invItemLocation.getInvItem().getInvUnitOfMeasure().getUomName());
            details.setCsBranchCode(adBranchItemLocation.getAdBranch().getBrBranchCode());

            details.setCsStockOnHand(1);


            SLS_AMOUNT = 0;

            details.setCsSalesAmount(SLS_AMOUNT);
            details.setCsGrossProfit(SLS_AMOUNT - details.getCsCostOfSales());
            details.setCsPercentProfit(details.getCsGrossProfit() / SLS_AMOUNT);


            list.add(details);
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("InvRepCostOfSalesControllerBean getAdCompany");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());
            details.setCmpAddress(adCompany.getCmpAddress());
            details.setCmpCity(adCompany.getCmpCity());
            details.setCmpContact(adCompany.getCmpContact());
            details.setCmpCountry(adCompany.getCmpCountry());
            details.setCmpEmail(adCompany.getCmpEmail());
            details.setCmpTin(adCompany.getCmpTin());
            details.setCmpDescription(adCompany.getCmpDescription());
            details.setCmpPhone(adCompany.getCmpPhone());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("InvRepCostOfSalesControllerBean getGlFcPrecisionUnit");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("InvRepCostOfSalesControllerBean ejbCreate");
    }

}