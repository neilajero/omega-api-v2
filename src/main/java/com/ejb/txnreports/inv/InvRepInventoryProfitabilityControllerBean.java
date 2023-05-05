/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class InvRepInventoryProfitabilityControllerBean
 * @created
 * @author
 */
package com.ejb.txnreports.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.ar.LocalArInvoiceHome;
import com.ejb.dao.inv.LocalInvCostingHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ar.LocalArInvoice;
import com.ejb.entities.inv.LocalInvCosting;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.reports.inv.InvRepInventoryProfitabilityDetails;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

@Stateless(name = "InvRepInventoryProfitabilityControllerEJB")
public class InvRepInventoryProfitabilityControllerBean extends EJBContextClass implements InvRepInventoryProfitabilityController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;

    public byte getAdPrfEnableInvShift(Integer AD_CMPNY) {

        Debug.print("InvRepInventoryProfitabilityControllerBean getAdPrfEnableInvShift");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfInvEnableShift();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList executeInvRepInventoryProfitability(HashMap criteria, String costingMethod, ArrayList branchList, String GROUP_BY, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvRepInventoryProfitabilityControllerBean executeInvRepInventoryProfitability");
        ArrayList list = new ArrayList();
        ArrayList tempList = new ArrayList();
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            if (branchList.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            // SALES - invoices and misc receipts
            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(cst) FROM InvCosting cst ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = 0;

            Object[] obj = null;

            // Allocate the size of the object parameter
            if (criteria.containsKey("category")) {
                criteriaSize++;
            }

            if (criteria.containsKey("location")) {
                criteriaSize++;
            }

            if (criteria.containsKey("itemClass")) {
                criteriaSize++;
            }

            if (criteria.containsKey("dateFrom")) {
                criteriaSize++;
            }

            if (criteria.containsKey("dateTo")) {
                criteriaSize++;
            }

            obj = new Object[criteriaSize];

            jbossQl.append(" WHERE cst.cstAdBranch in (");

            boolean firstLoop = true;

            Iterator i = branchList.iterator();
            while (i.hasNext()) {
                if (firstLoop == false) {
                    jbossQl.append(", ");
                } else {
                    firstLoop = false;
                }
                AdBranchDetails mdetails = (AdBranchDetails) i.next();
                jbossQl.append(mdetails.getBrCode());
            }

            jbossQl.append(") ");
            firstArgument = false;
            if (criteria.containsKey("itemName")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("cst.invItemLocation.invItem.iiName  LIKE '%").append(criteria.get("itemName")).append("%' ");
            }

            if (criteria.containsKey("category")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("cst.invItemLocation.invItem.iiAdLvCategory=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("category");
                ctr++;
            }

            if (criteria.containsKey("location")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("cst.invItemLocation.invLocation.locName=?").append(ctr + 1).append(" ");
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
                jbossQl.append("cst.invItemLocation.invItem.iiClass=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("itemClass");
                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("cst.cstDate>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateFrom");
                ctr++;
            }

            if (criteria.containsKey("dateTo")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("cst.cstDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("cst.cstQuantitySold <> 0 AND cst.cstAdCompany=").append(AD_CMPNY).append(" ORDER BY cst.invItemLocation.invItem.iiName, cst.cstDate, cst.cstLineNumber");

            Collection invCostings = invCostingHome.getCstByCriteria(jbossQl.toString(), obj, 0, 0);

            // get sales
            double SLS_QTY_SLD = 0d;
            double SLS_AMOUNT = 0d;
            double SLS_TAX_AMT = 0d;
            double TOTAL_SLS_TAX_AMT = 0d;

            Iterator itrTemp = invCostings.iterator();
            if (itrTemp.hasNext()) {
                itrTemp.next();
            }

            Iterator itrCost = invCostings.iterator();

            while (itrCost.hasNext()) {

                LocalInvCosting invCosting = (LocalInvCosting) itrCost.next();
                // get next record
                LocalInvCosting nextInvCosting = null;
                if (itrTemp.hasNext()) {
                    nextInvCosting = (LocalInvCosting) itrTemp.next();
                }

                if (invCosting.getArInvoiceLineItem() != null) {

                    if (invCosting.getArInvoiceLineItem().getArInvoice() != null) {

                        if (invCosting.getArInvoiceLineItem().getArInvoice().getInvCreditMemo() == 0) {

                            SLS_QTY_SLD += invCosting.getCstQuantitySold();
                            SLS_AMOUNT += invCosting.getArInvoiceLineItem().getIliAmount() + invCosting.getArInvoiceLineItem().getIliTaxAmount();
                            SLS_TAX_AMT += invCosting.getArInvoiceLineItem().getIliTaxAmount();

                        } else {

                            SLS_QTY_SLD += invCosting.getCstQuantitySold();
                            SLS_AMOUNT -= (invCosting.getArInvoiceLineItem().getIliAmount() + invCosting.getArInvoiceLineItem().getIliTaxAmount());
                            SLS_TAX_AMT -= invCosting.getArInvoiceLineItem().getIliTaxAmount();
                        }

                    } else {

                        SLS_QTY_SLD += invCosting.getCstQuantitySold();
                        SLS_AMOUNT += (invCosting.getArInvoiceLineItem().getIliAmount() + invCosting.getArInvoiceLineItem().getIliTaxAmount()) * (invCosting.getCstQuantitySold() > 0 ? 1 : -1);
                        SLS_TAX_AMT += invCosting.getArInvoiceLineItem().getIliTaxAmount() * (invCosting.getCstQuantitySold() > 0 ? 1 : -1);
                    }

                } else if (invCosting.getArSalesOrderInvoiceLine() != null) {

                    SLS_QTY_SLD += invCosting.getCstQuantitySold();
                    SLS_AMOUNT += invCosting.getArSalesOrderInvoiceLine().getSilAmount() + invCosting.getArSalesOrderInvoiceLine().getSilTaxAmount();
                    SLS_TAX_AMT += invCosting.getArSalesOrderInvoiceLine().getSilTaxAmount();

                } else if (invCosting.getArJobOrderInvoiceLine() != null) {

                    SLS_QTY_SLD += invCosting.getCstQuantitySold();
                    SLS_AMOUNT += invCosting.getArJobOrderInvoiceLine().getJilAmount() + invCosting.getArJobOrderInvoiceLine().getJilTaxAmount();
                    SLS_TAX_AMT += invCosting.getArJobOrderInvoiceLine().getJilTaxAmount();
                }

                if (SLS_QTY_SLD != 0 && (nextInvCosting == null || (nextInvCosting != null && !invCosting.getInvItemLocation().getInvItem().getIiName().equals(nextInvCosting.getInvItemLocation().getInvItem().getIiName())))) {

                    InvRepInventoryProfitabilityDetails details = new InvRepInventoryProfitabilityDetails();
                    details.setIpType("Sales");
                    details.setIpItemName(invCosting.getInvItemLocation().getInvItem().getIiName());
                    details.setIpItemDescription(invCosting.getInvItemLocation().getInvItem().getIiDescription());
                    details.setIpItemAdLvCategory(invCosting.getInvItemLocation().getInvItem().getIiAdLvCategory());
                    details.setIpUnitOfMeasure(invCosting.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName());
                    details.setIpQuantitySold(SLS_QTY_SLD);
                    details.setIpUnitPriceCost(SLS_AMOUNT / SLS_QTY_SLD);
                    details.setIpAmount(SLS_AMOUNT);

                    tempList.add(details);

                    TOTAL_SLS_TAX_AMT += SLS_TAX_AMT;
                    SLS_QTY_SLD = 0d;
                    SLS_AMOUNT = 0d;
                    SLS_TAX_AMT = 0d;
                }
            }

            if (!tempList.isEmpty()) {

                // group by
                if (GROUP_BY.equalsIgnoreCase("CATEGORY")) {
                    tempList.sort(InvRepInventoryProfitabilityDetails.sortByItemCategory);
                }

                // copy tempList to list
                for (Object o : tempList) {
                    InvRepInventoryProfitabilityDetails details = (InvRepInventoryProfitabilityDetails) o;
                    list.add(details);
                }
                tempList = new ArrayList();
            }

            if (TOTAL_SLS_TAX_AMT != 0) {
                // OUTPUT TAX - (INVOICES, CREDIT MEMO and MISC RECEIPTS)
                InvRepInventoryProfitabilityDetails outputDetails = new InvRepInventoryProfitabilityDetails();
                outputDetails.setIpType("Output Tax");
                outputDetails.setIpAmount(TOTAL_SLS_TAX_AMT * -1);
                list.add(outputDetails);
            }

            // get cost of goods sold
            double COGS_QTY_SLD = 0d;
            double COGS_AMOUNT = 0d;

            itrTemp = invCostings.iterator();
            if (itrTemp.hasNext()) {
                itrTemp.next();
            }

            itrCost = invCostings.iterator();

            while (itrCost.hasNext()) {

                LocalInvCosting invCosting = (LocalInvCosting) itrCost.next();
                // get next record
                LocalInvCosting nextInvCosting = null;
                if (itrTemp.hasNext()) {
                    nextInvCosting = (LocalInvCosting) itrTemp.next();
                }

                if (invCosting.getArInvoiceLineItem() != null) {

                    if (invCosting.getArInvoiceLineItem().getArInvoice() != null) {

                        if (invCosting.getArInvoiceLineItem().getArInvoice().getInvCreditMemo() == 0) {

                            double taxRate = invCosting.getArInvoiceLineItem().getArInvoice().getArTaxCode().getTcRate() / 100;
                            COGS_QTY_SLD += invCosting.getCstQuantitySold();
                            COGS_AMOUNT += costingMethod.equals("Average") ? invCosting.getCstCostOfSales() : EJBCommon.roundIt(invCosting.getInvItemLocation().getInvItem().getIiUnitCost() / (1 + taxRate), (short) 2) * invCosting.getCstQuantitySold();

                        } else {

                            LocalArInvoice arInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(invCosting.getArInvoiceLineItem().getArInvoice().getInvCmInvoiceNumber(), EJBCommon.FALSE, invCosting.getArInvoiceLineItem().getArInvoice().getInvAdBranch(), invCosting.getArInvoiceLineItem().getArInvoice().getInvAdCompany());

                            double taxRate = arInvoice.getArTaxCode().getTcRate() / 100;
                            COGS_QTY_SLD += invCosting.getCstQuantitySold();
                            COGS_AMOUNT -= costingMethod.equals("Average") ? invCosting.getCstCostOfSales() * -1 : EJBCommon.roundIt(invCosting.getInvItemLocation().getInvItem().getIiUnitCost() / (1 + taxRate), (short) 2) * invCosting.getCstQuantitySold();
                        }

                    } else {

                        double taxRate = invCosting.getArInvoiceLineItem().getArReceipt().getArTaxCode().getTcRate() / 100;
                        COGS_QTY_SLD += invCosting.getCstQuantitySold();
                        COGS_AMOUNT += costingMethod.equals("Average") ? invCosting.getCstCostOfSales() : EJBCommon.roundIt(invCosting.getInvItemLocation().getInvItem().getIiUnitCost() / (1 + taxRate), (short) 2) * invCosting.getCstQuantitySold();
                    }

                } else if (invCosting.getArSalesOrderInvoiceLine() != null) {

                    double taxRate = invCosting.getArSalesOrderInvoiceLine().getArInvoice().getArTaxCode().getTcRate() / 100;
                    COGS_QTY_SLD += invCosting.getCstQuantitySold();
                    COGS_AMOUNT += costingMethod.equals("Average") ? invCosting.getCstCostOfSales() : EJBCommon.roundIt(invCosting.getInvItemLocation().getInvItem().getIiUnitCost() / (1 + taxRate), (short) 2) * invCosting.getCstQuantitySold();

                } else if (invCosting.getArJobOrderInvoiceLine() != null) {

                    double taxRate = invCosting.getArJobOrderInvoiceLine().getArInvoice().getArTaxCode().getTcRate() / 100;
                    COGS_QTY_SLD += invCosting.getCstQuantitySold();
                    COGS_AMOUNT += costingMethod.equals("Average") ? invCosting.getCstCostOfSales() : EJBCommon.roundIt(invCosting.getInvItemLocation().getInvItem().getIiUnitCost() / (1 + taxRate), (short) 2) * invCosting.getCstQuantitySold();
                }

                if (COGS_QTY_SLD != 0 && (nextInvCosting == null || (nextInvCosting != null && !invCosting.getInvItemLocation().getInvItem().getIiName().equals(nextInvCosting.getInvItemLocation().getInvItem().getIiName())))) {

                    InvRepInventoryProfitabilityDetails details = new InvRepInventoryProfitabilityDetails();
                    details.setIpType("Cost Of Goods Sold");
                    details.setIpItemName(invCosting.getInvItemLocation().getInvItem().getIiName());
                    details.setIpItemDescription(invCosting.getInvItemLocation().getInvItem().getIiDescription());
                    details.setIpItemAdLvCategory(invCosting.getInvItemLocation().getInvItem().getIiAdLvCategory());
                    details.setIpUnitOfMeasure(invCosting.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName());
                    details.setIpQuantitySold(COGS_QTY_SLD);
                    details.setIpUnitPriceCost(COGS_AMOUNT / COGS_QTY_SLD);
                    details.setIpAmount(COGS_AMOUNT * -1);

                    tempList.add(details);

                    COGS_QTY_SLD = 0d;
                    COGS_AMOUNT = 0d;
                }
            }

            if (!tempList.isEmpty()) {

                // group by
                if (GROUP_BY.equalsIgnoreCase("CATEGORY")) {

                    tempList.sort(InvRepInventoryProfitabilityDetails.sortByItemCategory);
                }

                // copy tempList to list

                for (Object o : tempList) {

                    InvRepInventoryProfitabilityDetails details = (InvRepInventoryProfitabilityDetails) o;

                    list.add(details);
                }

                tempList = new ArrayList();
            }

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

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("InvRepInventoryProfitabilityControllerBean getAdCompany");
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

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("InvRepInventoryProfitabilityControllerBean getGlFcPrecisionUnit");
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

        Debug.print("InvRepInventoryProfitabilityControllerBean ejbCreate");
    }

}