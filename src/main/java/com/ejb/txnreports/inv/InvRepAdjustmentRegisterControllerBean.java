/**
 * @copyright 2022 Omega Business Consulting, Inc.
 * @class InvRepAdjustmentRegisterControllerBean
 */
package com.ejb.txnreports.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdBranchItemLocationHome;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.dao.gen.LocalGenValueSetValueHome;
import com.ejb.dao.inv.LocalInvAdjustmentHome;
import com.ejb.dao.inv.LocalInvAdjustmentLineHome;
import com.ejb.dao.inv.LocalInvCostingHome;
import com.ejb.dao.inv.LocalInvItemHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdBranchItemLocation;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.gen.LocalGenValueSetValue;
import com.ejb.entities.inv.LocalInvAdjustment;
import com.ejb.entities.inv.LocalInvAdjustmentLine;
import com.ejb.entities.inv.LocalInvCosting;
import com.ejb.entities.inv.LocalInvItem;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.reports.inv.*;

import jakarta.ejb.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Stateless(name = "InvRepAdjustmentRegisterControllerEJB")
public class InvRepAdjustmentRegisterControllerBean extends EJBContextClass implements InvRepAdjustmentRegisterController {

    @EJB
    public PersistenceBeanClass em;
    Double forwardedBal = 0d;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalInvAdjustmentHome invAdjustmentHome;
    @EJB
    private LocalInvAdjustmentLineHome invAdjustmentLineHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalGenValueSetValueHome genValueSetValueHome;
    @EJB
    private LocalInvItemHome invItemHome;

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("InvRepAdjustmentRegisterControllerBean getAdCompany");
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

    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvRepAdjustmentRegisterControllerBean getAdBrResAll");
        LocalAdBranchResponsibility adBranchResponsibility;
        LocalAdBranch adBranch;
        Collection adBranchResponsibilities = null;
        ArrayList list = new ArrayList();
        try {
            adBranchResponsibilities = adBranchResponsibilityHome.findByAdResponsibility(RS_CODE, AD_CMPNY);
        } catch (FinderException ex) {
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (adBranchResponsibilities.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }
        try {

            for (Object branchResponsibility : adBranchResponsibilities) {

                adBranchResponsibility = (LocalAdBranchResponsibility) branchResponsibility;

                adBranch = adBranchResponsibility.getAdBranch();

                AdBranchDetails details = new AdBranchDetails();

                details.setBrCode(adBranch.getBrCode());
                details.setBrBranchCode(adBranch.getBrBranchCode());
                details.setBrName(adBranch.getBrName());
                details.setBrHeadQuarter(adBranch.getBrHeadQuarter());

                list.add(details);
            }

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        return list;
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("InvRepAdjustmentRegisterControllerBean getGlFcPrecisionUnit");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public Double getforwardedBal() {

        return forwardedBal;
    }

    public ArrayList executeInvRepAdjustmentRegister(HashMap criteria, String ORDER_BY, String GROUP_BY, boolean SHOW_LN_ITEMS, String DateFrom, String DateTo, ArrayList adBrnchList, String REPORT_TYPE, Integer AD_CMPNY) throws GlobalNoRecordFoundException, FinderException {

        Debug.print("InvRepAdjustmentRegisterControllerBean executeInvRepAdjustmentRegister");
        ArrayList list = new ArrayList();
        Debug.print("REPORT_TYPE" + REPORT_TYPE);
        switch (REPORT_TYPE) {
            case "RM_USED": {

                LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
                Collection invItems = invItemHome.findStockIiAll(AD_CMPNY);

                if (invItems.size() == 0) {
                    throw new GlobalNoRecordFoundException();
                }
                Date enteredDate = (Date) criteria.get("dateTo");

                for (Object item : invItems) {

                    LocalInvItem invItem = (LocalInvItem) item;

                    InvRepAdjustmentRegisterRmUsedDetails details = new InvRepAdjustmentRegisterRmUsedDetails();
                    details.setIlIiName(invItem.getIiName());
                    details.setIlIiDescription(invItem.getIiDescription());
                    details.setIlIiUomName(invItem.getInvUnitOfMeasure().getUomShortName());
                    for (int loop = 1; loop <= 12; loop++) {
                        // get date range for month 12
                        GregorianCalendar gcCurrDate = new GregorianCalendar();
                        gcCurrDate.setTime(enteredDate);
                        gcCurrDate.add(Calendar.MONTH, (loop - 1) * -1);

                        // date from
                        GregorianCalendar gcDateFrom = new GregorianCalendar();
                        gcDateFrom.set(gcCurrDate.get(Calendar.YEAR), gcCurrDate.get(Calendar.MONTH), 0, 0, 0, 0);
                        gcDateFrom.set(Calendar.MILLISECOND, 0);
                        criteria.put("dateFrom", gcDateFrom.getTime());
                        // date to
                        GregorianCalendar gcDateTo = new GregorianCalendar();
                        gcDateTo.set(gcCurrDate.get(Calendar.YEAR), gcCurrDate.get(Calendar.MONTH), gcCurrDate.getMaximum(Calendar.DATE), 0, 0, 0);
                        gcDateTo.set(Calendar.MILLISECOND, 0);
                        criteria.put("dateTo", gcDateTo.getTime());

                        double qty = 0d;
                        criteria.put("type", "ISSUANCE");
                        criteria.put("itemName", invItem.getIiName());
                        Collection invAdjustments = this.getAdjByCriteria(criteria, ORDER_BY, adBrnchList, AD_CMPNY);
                        for (Object adjustment : invAdjustments) {
                            LocalInvAdjustment invAdjustment = (LocalInvAdjustment) adjustment;
                            for (Object o : invAdjustment.getInvAdjustmentLines()) {

                                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) o;
                                if (invAdjustmentLine.getInvItemLocation().getInvItem().getIiName().equals(invItem.getIiName())) {
                                    qty += invAdjustmentLine.getAlAdjustQuantity();
                                }
                            }
                        }
                        qty = Math.abs(qty);
                        switch (loop) {
                            case 12:
                                details.setIlIiMonth1(qty);

                                break;
                            case 11:
                                details.setIlIiMonth2(qty);
                                break;
                            case 10:
                                details.setIlIiMonth3(qty);
                                break;
                            case 9:
                                details.setIlIiMonth4(qty);
                                break;
                            case 8:
                                details.setIlIiMonth5(qty);
                                break;
                            case 7:
                                details.setIlIiMonth6(qty);
                                break;
                            case 6:
                                details.setIlIiMonth7(qty);
                                break;
                            case 5:
                                details.setIlIiMonth8(qty);
                                break;
                            case 4:
                                details.setIlIiMonth9(qty);
                                break;
                            case 3:
                                details.setIlIiMonth10(qty);
                                break;
                            case 2:
                                details.setIlIiMonth11(qty);
                                break;
                            case 1:
                                details.setIlIiMonth12(qty);
                                break;
                        }
                    }

                    list.add(details);
                }

                if (list.size() == 0) {
                    throw new GlobalNoRecordFoundException();
                }

                return list;
            }
            case "PROD_LIST": {
                Debug.print("PROD_LIST");
                LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

                Collection invItems = invItemHome.findAssemblyIiAll(AD_CMPNY);
                Date enteredDate = (Date) criteria.get("dateTo");
                if (invItems.size() == 0) {
                    throw new GlobalNoRecordFoundException();
                }

                for (Object item : invItems) {

                    LocalInvItem invItem = (LocalInvItem) item;

                    InvRepAdjustmentRegisterProdListDetails details = new InvRepAdjustmentRegisterProdListDetails();
                    details.setIlIiName(invItem.getIiName());
                    details.setIlIiDescription(invItem.getIiDescription());
                    details.setIlIiYield(invItem.getIiYield());
                    details.setIlIiUomName(invItem.getInvUnitOfMeasure().getUomShortName());

                    for (int loop = 1; loop <= 12; loop++) {
                        // get date range for month 12
                        GregorianCalendar gcCurrDate = new GregorianCalendar();
                        gcCurrDate.setTime(enteredDate);
                        gcCurrDate.add(Calendar.MONTH, (loop - 1) * -1);

                        // date from
                        GregorianCalendar gcDateFrom = new GregorianCalendar();
                        gcDateFrom.set(gcCurrDate.get(Calendar.YEAR), gcCurrDate.get(Calendar.MONTH), 0, 0, 0, 0);
                        gcDateFrom.set(Calendar.MILLISECOND, 0);
                        criteria.put("dateFrom", gcDateFrom.getTime());
                        // date to
                        GregorianCalendar gcDateTo = new GregorianCalendar();
                        gcDateTo.set(gcCurrDate.get(Calendar.YEAR), gcCurrDate.get(Calendar.MONTH), gcCurrDate.getMaximum(Calendar.DATE), 0, 0, 0);
                        gcDateTo.set(Calendar.MILLISECOND, 0);
                        criteria.put("dateTo", gcDateTo.getTime());

                        double qty = 0d;
                        criteria.put("type", "PRODUCTION");
                        criteria.put("itemName", invItem.getIiName());
                        Collection invAdjustments = this.getAdjByCriteria(criteria, ORDER_BY, adBrnchList, AD_CMPNY);
                        for (Object adjustment : invAdjustments) {
                            LocalInvAdjustment invAdjustment = (LocalInvAdjustment) adjustment;
                            for (Object o : invAdjustment.getInvAdjustmentLines()) {

                                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) o;
                                if (invAdjustmentLine.getInvItemLocation().getInvItem().getIiName().equals(invItem.getIiName())) {
                                    qty += invAdjustmentLine.getAlAdjustQuantity();
                                }
                            }
                        }
                        qty = Math.abs(qty);
                        switch (loop) {
                            case 12:
                                details.setIlIiMonth1(qty);

                                break;
                            case 11:
                                details.setIlIiMonth2(qty);
                                break;
                            case 10:
                                details.setIlIiMonth3(qty);
                                break;
                            case 9:
                                details.setIlIiMonth4(qty);
                                break;
                            case 8:
                                details.setIlIiMonth5(qty);
                                break;
                            case 7:
                                details.setIlIiMonth6(qty);
                                break;
                            case 6:
                                details.setIlIiMonth7(qty);
                                break;
                            case 5:
                                details.setIlIiMonth8(qty);
                                break;
                            case 4:
                                details.setIlIiMonth9(qty);
                                break;
                            case 3:
                                details.setIlIiMonth10(qty);
                                break;
                            case 2:
                                details.setIlIiMonth11(qty);
                                break;
                            case 1:
                                details.setIlIiMonth12(qty);
                                break;
                        }
                    }
                    list.add(details);
                }

                if (list.size() == 0) {
                    throw new GlobalNoRecordFoundException();
                }

                return list;

            }
            case "RM_VAR": {

                Collection invItems = invItemHome.findStockIiAll(AD_CMPNY);

                if (invItems.size() == 0) {
                    throw new GlobalNoRecordFoundException();
                }

                for (Object item : invItems) {

                    LocalInvItem invItem = (LocalInvItem) item;

                    double actualQtyUsed = 0d;
                    double stdQty = 0d;
                    Double varianceQty = 0d;
                    double actualAmnt = 0d;
                    double stdAmnt = 0d;
                    Double varianceAmnt = 0d;

                    InvRepAdjustmentRegisterRmVarDetails details = new InvRepAdjustmentRegisterRmVarDetails();
                    details.setIlIiName(invItem.getIiName());
                    details.setIlIiDescription(invItem.getIiDescription());
                    details.setIlIiUomName(invItem.getInvUnitOfMeasure().getUomShortName());

                    criteria.put("type", "ISSUANCE");
                    criteria.put("itemName", invItem.getIiName());
                    Collection invAdjustments = this.getAdjByCriteria(criteria, ORDER_BY, adBrnchList, AD_CMPNY);
                    if (invItem.getIiName().equals("121SUG0002")) {
                        Debug.print("invAdjustments-" + invAdjustments.size());
                    }
                    for (Object adjustment : invAdjustments) {
                        LocalInvAdjustment invAdjustment = (LocalInvAdjustment) adjustment;
                        for (Object o : invAdjustment.getInvAdjustmentLines()) {

                            LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) o;
                            if (invAdjustmentLine.getInvItemLocation().getInvItem().getIiName().equals(invItem.getIiName())) {
                                actualQtyUsed += Math.abs(invAdjustmentLine.getAlAdjustQuantity());
                                actualAmnt += Math.abs(invAdjustmentLine.getAlUnitCost() * invAdjustmentLine.getAlAdjustQuantity());
                            }
                        }
                    }
                    if (invItem.getIiName().equals("121SUG0002")) {
                        Debug.print("actualQtyUsed-" + actualQtyUsed);
                    }
                    details.setIlIiActualQtyUsed(actualQtyUsed);
                    details.setIlIiActualAmnt(actualAmnt);

                    //TODO: Verify to where this value should be pull out
                    details.setIlIiStdQty(stdQty);
                    details.setIlIiStdAmnt(stdAmnt);

                    details.setIlIiVarianceQty(actualQtyUsed - stdQty);
                    details.setIlIiVarianceAmnt(EJBCommon.roundIt(actualAmnt - stdAmnt, (short) 2));
                    details.setIlIiStdUnitCost(EJBCommon.roundIt(stdAmnt / stdQty, (short) 2));

                    // details.setIlIiQtyUsed(quantityUsed);

                    list.add(details);
                }

                if (list.size() == 0) {
                    throw new GlobalNoRecordFoundException();
                }

                return list;
            }
            case "Daily Balance Sheet": {

                Debug.print("Daily Balance Sheet");
                double forwardedBalance = 0d;
                StringBuilder jbossQl = new StringBuilder();
                jbossQl.append("SELECT OBJECT(bil) FROM AdBranchItemLocation bil ");
                short ctr = 0;
                int criteriaSize = 1;
                Object[] obj = null;
                if (adBrnchList.isEmpty()) {
                    forwardedBalance = 0;
                } else {
                    Debug.print("CheckPoint executeInvRepStockOnHand B");
                    jbossQl.append("WHERE bil.adBranch.brCode in (");

                    boolean firstLoop = true;

                    for (Object o : adBrnchList) {

                        if (firstLoop == false) {
                            jbossQl.append(", ");
                        } else {
                            firstLoop = false;
                        }

                        AdBranchDetails mdetails = (AdBranchDetails) o;
                        jbossQl.append(mdetails.getBrCode());
                    }
                    jbossQl.append(") ");
                }

                jbossQl.append("AND ");

                jbossQl.append("bil.bilAdCompany = ").append(AD_CMPNY).append(" ");

                Debug.print("jbossQl: " + jbossQl);
                Collection adBranchItemLocations = null;

                try {
                    adBranchItemLocations = adBranchItemLocationHome.getBilByCriteria(jbossQl.toString(), obj);
                } catch (FinderException ex) {
                    throw new GlobalNoRecordFoundException();
                }

                if (adBranchItemLocations.isEmpty()) {
                    forwardedBalance = 0;
                }
                // Debug.print("jbossQl: "+adBranchItemLocations.size());
                Iterator i = adBranchItemLocations.iterator();
                GregorianCalendar gcCurrDate = new GregorianCalendar();
                gcCurrDate.setTime(EJBCommon.convertStringToSQLDate(DateFrom));
                gcCurrDate.set(gcCurrDate.get(Calendar.YEAR), gcCurrDate.get(Calendar.MONTH), gcCurrDate.get(Calendar.DATE), 0, 0, 0);
                gcCurrDate.set(Calendar.MILLISECOND, 0);
                gcCurrDate.add(Calendar.DATE, -1);
                Date AS_OF_DT = gcCurrDate.getTime();

                while (i.hasNext()) {

                    LocalAdBranchItemLocation adBranchItemLocation = (LocalAdBranchItemLocation) i.next();

                    try {
                        LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(AS_OF_DT, adBranchItemLocation.getInvItemLocation().getInvItem().getIiName(), adBranchItemLocation.getInvItemLocation().getInvLocation().getLocName(), adBranchItemLocation.getAdBranch().getBrCode(), AD_CMPNY);

                        forwardedBalance += invCosting.getCstRemainingValue();
                    } catch (Exception ex) {
                    }
                }
                StringBuilder DocNumbers = new StringBuilder();
                StringBuilder DocNumbers2 = new StringBuilder();
                String tempDocs = "";
                String tempDocs2 = "";
                forwardedBal = forwardedBalance;
                // Debug.print("outside: ");
                int xxx = 0;
                int Day = 1;
                while (Day < 32) {

                    String newDate = DateFrom.substring(0, DateFrom.indexOf("/")) + "/" + Day + DateFrom.substring(DateFrom.lastIndexOf("/"));
                    Debug.print(newDate);
                    try {
                        xxx = isValidDate(newDate);
                    } catch (Exception ex) {
                        Debug.print("ERROR");
                    }
                    double General = 0d;
                    double adjCost = 0d;
                    double Issuance = 0d;
                    DocNumbers = new StringBuilder();
                    DocNumbers2 = new StringBuilder();
                    try {

                        Collection invAdjustments2 = invAdjustmentLineHome.findGeneral(AD_CMPNY, EJBCommon.convertStringToSQLDate(newDate));

                        for (Object o : invAdjustments2) {
                            LocalInvAdjustmentLine invItem1 = (LocalInvAdjustmentLine) o;

                            if (invItem1.getInvAdjustment().getGlChartOfAccount().getCoaCode() == 1419) {
                                General += invItem1.getAlUnitCost() * invItem1.getAlAdjustQuantity();
                            } else {
                                adjCost += invItem1.getAlUnitCost() * invItem1.getAlAdjustQuantity();
                                Debug.print("adjCost xd " + adjCost);

                                if (tempDocs2 == invItem1.getInvAdjustment().getAdjDocumentNumber()) {
                                } else {
                                    tempDocs2 = invItem1.getInvAdjustment().getAdjDocumentNumber();
                                    if (DocNumbers.length() > 1) {
                                        DocNumbers2.append("/").append(invItem1.getInvAdjustment().getAdjDocumentNumber().substring(4));
                                    } else {
                                        DocNumbers2 = new StringBuilder(invItem1.getInvAdjustment().getAdjDocumentNumber());
                                    }
                                }

                                continue;
                            }

                            if (tempDocs == invItem1.getInvAdjustment().getAdjDocumentNumber()) {
                            } else {
                                tempDocs = invItem1.getInvAdjustment().getAdjDocumentNumber();
                                if (DocNumbers.length() > 1) {
                                    DocNumbers.append("/").append(invItem1.getInvAdjustment().getAdjDocumentNumber().substring(4));
                                } else {
                                    DocNumbers = new StringBuilder(invItem1.getInvAdjustment().getAdjDocumentNumber());
                                }
                            }
                        }
                    } catch (Exception ex) {
                    }

                    try {

                        Collection invAdjustments3 = invAdjustmentLineHome.findPostedIssuance(AD_CMPNY, EJBCommon.convertStringToSQLDate(newDate));

                        for (Object o : invAdjustments3) {
                            LocalInvAdjustmentLine invItem2 = (LocalInvAdjustmentLine) o;
                            double z = 0d;
                            z = invItem2.getAlUnitCost() * invItem2.getAlAdjustQuantity();
                            Issuance += Math.abs(z);
                        }
                    } catch (Exception ex) {

                    }

                    InvRepAdjustmentRegisterBalanceSheetDetails details = new InvRepAdjustmentRegisterBalanceSheetDetails();
                    details.setIlDate(newDate);
                    details.setIlForwardedBalance(forwardedBalance);
                    details.setIlDeliveries(General);
                    Debug.print("General - " + General);
                    details.setIlDocNumber(DocNumbers.toString());
                    details.setIlAjustmentCost(Math.abs(adjCost));
                    details.setIlReferenceNumber(DocNumbers2.toString());
                    details.setIlWithdrawals(Issuance);
                    Debug.print("Issuance - " + Issuance);
                    forwardedBalance = forwardedBalance + General + Math.abs(adjCost);
                    forwardedBalance = forwardedBalance - Math.abs(Issuance);
                    details.setIlEndingBalance(forwardedBalance);
                    if (xxx == 1) {
                        list.add(details);
                    }
                    Day++;
                }
                if (list.size() == 0) {
                    throw new GlobalNoRecordFoundException();
                }
                return list;
            }
            case "Posted Deliveries":

                Debug.print("Posted Deliveries");

                if (DateFrom.length() > 1 && DateTo.length() > 1) {
                    Date dtFrom = EJBCommon.convertStringToSQLDate(DateFrom);

                    Date dtTo = EJBCommon.convertStringToSQLDate(DateTo);
                    Debug.print("S");
                    Collection Generals = invCostingHome.findByCstDateGeneralOnlyByMonth(dtFrom, dtTo, AD_CMPNY);
                    Debug.print("GENERAL SIZE = " + Generals.size());

                    for (Object general : Generals) {
                        LocalInvCosting postedDeliveries = (LocalInvCosting) general;
                        InvRepAdjustmentRegisterPostedDeliveriesDetails details = new InvRepAdjustmentRegisterPostedDeliveriesDetails();
                        details.setIL_DOC_NUM(postedDeliveries.getInvAdjustmentLine().getInvAdjustment().getAdjDocumentNumber());
                        Debug.print("DOC_NUM = " + postedDeliveries.getInvAdjustmentLine().getInvAdjustment().getAdjDocumentNumber());
                        details.setIL_ITEM_CODE(postedDeliveries.getInvItemLocation().getInvItem().getIiName());
                        Debug.print("ITEM_CODE = " + postedDeliveries.getInvItemLocation().getInvItem().getIiName());
                        details.setIL_ITEM_DESC(postedDeliveries.getInvItemLocation().getInvItem().getIiDescription());
                        Debug.print("ITEM_DESC = " + postedDeliveries.getInvItemLocation().getInvItem().getIiDescription());
                        details.setIL_ITEM_UNIT(postedDeliveries.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName());
                        Debug.print("ITEM_UNIT = " + postedDeliveries.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName());
                        details.setIL_ITEM_QUANTITY(postedDeliveries.getInvAdjustmentLine().getAlAdjustQuantity());
                        details.setIL_ITEM_COST(postedDeliveries.getInvAdjustmentLine().getAlUnitCost());
                        Debug.print("details.setIL_DOC_NUM = " + postedDeliveries.getInvAdjustmentLine().getAlCode());
                        if (postedDeliveries.getInvAdjustmentLine().getInvAdjustment().getGlChartOfAccount().getCoaCode() == 1419) {
                            list.add(details);
                        }
                    }
                } else {
                    throw new GlobalNoRecordFoundException();
                }

                if (list.size() == 0) {
                    throw new GlobalNoRecordFoundException();
                }
                return list;
            case "Summary of Issuance": {

                Debug.print("Summary of Issuance");

                double forwardedBalance = 0d;

                StringBuilder jbossQl2 = new StringBuilder();
                jbossQl2.append("SELECT OBJECT(bil) FROM AdBranchItemLocation bil ");

                short ctr = 0;
                int criteriaSize = 1;

                Object[] obj2 = null;

                if (adBrnchList.isEmpty()) {

                    forwardedBalance = 0;

                } else {
                    Debug.print("CheckPoint executeInvRepStockOnHand B");
                    jbossQl2.append("WHERE bil.adBranch.brCode in (");

                    boolean firstLoop = true;

                    for (Object o : adBrnchList) {

                        if (firstLoop == false) {
                            jbossQl2.append(", ");
                        } else {
                            firstLoop = false;
                        }

                        AdBranchDetails mdetails = (AdBranchDetails) o;

                        jbossQl2.append(mdetails.getBrCode());
                    }

                    jbossQl2.append(") ");
                }

                if (criteria.containsKey("itemFrom")) {

                    jbossQl2.append("AND ");

                    jbossQl2.append("bil.invItemLocation.invItem.iiName  >= '").append(criteria.get("itemFrom")).append("' ");
                }

                if (criteria.containsKey("itemTo")) {

                    jbossQl2.append("AND ");

                    jbossQl2.append("bil.invItemLocation.invItem.iiName  <= '").append(criteria.get("itemTo")).append("' ");
                }

                jbossQl2.append("AND ");

                jbossQl2.append("bil.bilAdCompany = ").append(AD_CMPNY).append(" ORDER BY bil.invItemLocation.invItem.iiName ");

                Debug.print("jbossQl: " + jbossQl2);
                Collection adBranchItemLocations = null;

                try {

                    adBranchItemLocations = adBranchItemLocationHome.getBilByCriteria(jbossQl2.toString(), obj2);

                } catch (FinderException ex) {

                    throw new GlobalNoRecordFoundException();
                }

                if (adBranchItemLocations.isEmpty()) {
                    forwardedBalance = 0;
                }
                Debug.print("jbossQl: " + adBranchItemLocations.size());
                Iterator i = adBranchItemLocations.iterator();

                int xxx = 0;
                String newDate = DateFrom.substring(0, DateFrom.indexOf("/")) + "/01/" + DateFrom.substring(DateFrom.lastIndexOf("/") + 1);
                try {
                    xxx = isValidDate(newDate);
                } catch (Exception ex) {
                }
                xxx = 0;
                String newDate2 = "";
                String az = DateFrom.substring(0, DateFrom.indexOf("/")) + "/28/" + DateFrom.substring(DateFrom.lastIndexOf("/") + 1);
                try {
                    xxx = isValidDate(az);
                    if (xxx == 1) {
                        newDate2 = az;
                    }
                } catch (Exception ex3) {

                }

                az = DateFrom.substring(0, DateFrom.indexOf("/")) + "/29/" + DateFrom.substring(DateFrom.lastIndexOf("/") + 1);
                try {
                    xxx = isValidDate(az);
                    if (xxx == 1) {
                        newDate2 = az;
                    }
                } catch (Exception ex2) {

                }

                az = DateFrom.substring(0, DateFrom.indexOf("/")) + "/30/" + DateFrom.substring(DateFrom.lastIndexOf("/") + 1);
                try {
                    xxx = isValidDate(az);
                    if (xxx == 1) {
                        newDate2 = az;
                    }
                } catch (Exception ex1) {

                }

                az = DateFrom.substring(0, DateFrom.indexOf("/")) + "/31/" + DateFrom.substring(DateFrom.lastIndexOf("/") + 1);
                Debug.print(newDate);
                try {
                    xxx = isValidDate(az);
                    if (xxx == 1) {
                        newDate2 = az;
                    }
                } catch (Exception ex) {

                }
                // Debug.print("newDate2: "+newDate2);

                String DocNumbers = "";
                String tempDocs = "";
                forwardedBal = forwardedBalance;

                Object[] obj = null;
                int Day = 1;
                double total = 0d;
                while (i.hasNext()) {
                    double Issuance = 0d;
                    try {
                        LocalAdBranchItemLocation invItem = (LocalAdBranchItemLocation) i.next();
                        StringBuilder jbossQl = new StringBuilder();
                        obj = new Object[2];
                        obj[0] = newDate;
                        obj[1] = newDate2;

                        Collection invAdjustments4 = invAdjustmentHome.findPostedAdjByAdjDateRange(EJBCommon.convertStringToSQLDate(newDate), EJBCommon.convertStringToSQLDate(newDate2), AD_CMPNY);

                        for (Object item : invAdjustments4) {
                            LocalInvAdjustment adjustment = (LocalInvAdjustment) item;
                            if (adjustment.getAdjType().equals("ISSUANCE")) {

                                Collection invAdjustmentsLines99 = invAdjustmentLineHome.findByAlVoidAndAdjCode((byte) 0, adjustment.getAdjCode(), AD_CMPNY);

                                for (Object value : invAdjustmentsLines99) {
                                    LocalInvAdjustmentLine adjustmentLine999 = (LocalInvAdjustmentLine) value;
                                    if (adjustmentLine999.getInvItemLocation().getInvItem().getIiName() == invItem.getInvItemLocation().getInvItem().getIiName()) {
                                        total += adjustmentLine999.getAlAdjustQuantity() * adjustmentLine999.getAlUnitCost() * -1;
                                        InvRepAdjustmentRegisterSummaryOfIssuanceDetails details = new InvRepAdjustmentRegisterSummaryOfIssuanceDetails();

                                        Collection BrList = genValueSetValueHome.findByVsName("BRANCH", AD_CMPNY);

                                        String sample = adjustment.getGlChartOfAccount().getCoaAccountDescription();

                                        for (Object o : BrList) {
                                            LocalGenValueSetValue glvsv = (LocalGenValueSetValue) o;
                                            if (sample.contains(glvsv.getVsvDescription() + "-")) {
                                                Debug.print(glvsv.getVsvDescription());
                                                sample = glvsv.getVsvDescription();
                                                break;
                                            }
                                        }

                                        details.setIlDepartment(sample);
                                        details.setIlDescription(invItem.getInvItemLocation().getInvItem().getIiDescription());
                                        details.setIlName(invItem.getInvItemLocation().getInvItem().getIiName());
                                        details.setIlQuantity(adjustmentLine999.getAlAdjustQuantity() * -1);
                                        details.setIlTotalCost(adjustmentLine999.getAlAdjustQuantity() * adjustmentLine999.getAlUnitCost() * -1);
                                        details.setOrderBy(details.getIlName());
                                        details.setDATE(adjustment.getAdjDate());
                                        details.setIL_REFERENCE(adjustment.getAdjReferenceNumber());
                                        list.add(details);
                                    }
                                }
                            }
                        }
                    } catch (Exception ex) {
                    }
                }
                Debug.print("Total" + total);
                if (list.size() == 0) {
                    throw new GlobalNoRecordFoundException();
                }

                list.sort(InvRepAdjustmentRegisterSummaryOfIssuanceDetails.ItemNameComparator);

                return list;

            }
            default:
                try {
                    LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
                    StringBuilder jbossQl = new StringBuilder();
                    jbossQl.append("SELECT OBJECT(adj) FROM InvAdjustment adj ");
                    boolean firstArgument = true;
                    short ctr = 0;
                    int criteriaSize = criteria.size();
                    Object[] obj;

                    // Allocate the size of the object parameter
                    if (criteria.containsKey("includedUnposted")) {
                        criteriaSize--;
                    }
                    obj = new Object[criteriaSize];
                    if (criteria.containsKey("type")) {
                        if (!firstArgument) {
                            jbossQl.append("AND ");
                        } else {
                            firstArgument = false;
                            jbossQl.append("WHERE ");
                        }
                        jbossQl.append("adj.adjType=?").append(ctr + 1).append(" ");
                        obj[ctr] = criteria.get("type");
                        ctr++;
                    }
                    if (criteria.containsKey("accountDescription")) {
                        if (!firstArgument) {
                            jbossQl.append("AND ");
                        } else {
                            firstArgument = false;
                            jbossQl.append("WHERE ");
                        }
                        System.out.print("hhhhhererhere" + criteria.get("accountDescription"));
                        jbossQl.append("adj.glChartOfAccount.coaAccountNumber=?").append(ctr + 1).append(" ");
                        obj[ctr] = criteria.get("accountDescription");
                        ctr++;
                    }
                    if (criteria.containsKey("dateFrom")) {
                        if (!firstArgument) {
                            jbossQl.append("AND ");
                        } else {
                            firstArgument = false;
                            jbossQl.append("WHERE ");
                        }
                        jbossQl.append("adj.adjDate>=?").append(ctr + 1).append(" ");
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
                        jbossQl.append("adj.adjDate<=?").append(ctr + 1).append(" ");
                        obj[ctr] = criteria.get("dateTo");
                        ctr++;
                    }
                    if (criteria.containsKey("documentNumberFrom")) {
                        if (!firstArgument) {
                            jbossQl.append("AND ");
                        } else {
                            firstArgument = false;
                            jbossQl.append("WHERE ");
                        }
                        jbossQl.append("adj.adjDocumentNumber>=?").append(ctr + 1).append(" ");
                        obj[ctr] = criteria.get("documentNumberFrom");
                        ctr++;
                    }
                    if (criteria.containsKey("documentNumberTo")) {
                        if (!firstArgument) {
                            jbossQl.append("AND ");
                        } else {
                            firstArgument = false;
                            jbossQl.append("WHERE ");
                        }
                        jbossQl.append("adj.adjDocumentNumber<=?").append(ctr + 1).append(" ");
                        obj[ctr] = criteria.get("documentNumberTo");
                        ctr++;
                    }
                    if (criteria.containsKey("referenceNumber")) {
                        if (!firstArgument) {
                            jbossQl.append("AND ");
                        } else {
                            firstArgument = false;
                            jbossQl.append("WHERE ");
                        }
                        jbossQl.append("adj.adjReferenceNumber>=?").append(ctr + 1).append(" ");
                        obj[ctr] = criteria.get("referenceNumber");
                        ctr++;
                    }
                    if (criteria.get("includedUnposted").equals("NO")) {
                        if (!firstArgument) {
                            jbossQl.append("AND ");
                        } else {
                            firstArgument = false;
                            jbossQl.append("WHERE ");
                        }
                        jbossQl.append("adj.adjPosted = 1 ");
                    }
                    if (adBrnchList.isEmpty()) {
                        throw new GlobalNoRecordFoundException();
                    } else {
                        if (!firstArgument) {
                            jbossQl.append("AND ");
                        } else {
                            firstArgument = false;
                            jbossQl.append("WHERE ");
                        }
                        jbossQl.append("adj.adjAdBranch in (");
                        boolean firstLoop = true;
                        for (Object o : adBrnchList) {
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
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("adj.adjAdCompany = ").append(AD_CMPNY).append(" ");
                    String orderBy = null;
                    if (ORDER_BY.equals("DATE")) {
                        orderBy = "adj.adjDate";
                    } else if (ORDER_BY.equals("DOCUMENT NUMBER")) {
                        orderBy = "adj.adjDocumentNumber";
                    }
                    if (orderBy != null) {
                        jbossQl.append("ORDER BY ").append(orderBy);
                    }
                    Collection invAdjustments = null;
                    try {
                        Debug.print("jbossQl.toString()" + jbossQl);
                        invAdjustments = invAdjustmentHome.getAdjByCriteria(jbossQl.toString(), obj, 0, 0);
                        Debug.print("invAdjustments=" + invAdjustments.size());
                    } catch (FinderException ex) {
                    }
                    if (invAdjustments.size() == 0) {
                        throw new GlobalNoRecordFoundException();
                    }
                    for (Object adjustment : invAdjustments) {
                        LocalInvAdjustment invAdjustment = (LocalInvAdjustment) adjustment;
                        if (SHOW_LN_ITEMS) {
                            double AMOUNT = 0d;
                            Collection invAdjustmentLines = invAdjustment.getInvAdjustmentLines();
                            for (Object adjustmentLine : invAdjustmentLines) {
                                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) adjustmentLine;
                                InvRepAdjustmentRegisterDetails details = new InvRepAdjustmentRegisterDetails();
                                details.setAdjDate(invAdjustment.getAdjDate());
                                details.setAdjDocumentNumber(invAdjustment.getAdjDocumentNumber());
                                details.setAdjReferenceNumber(invAdjustment.getAdjReferenceNumber());
                                details.setAdjType(invAdjustment.getAdjType());
                                details.setAdjDescription(invAdjustment.getAdjDescription());
                                details.setAdjItemName(invAdjustmentLine.getInvItemLocation().getInvItem().getIiName());
                                details.setAdjItemDesc(invAdjustmentLine.getInvItemLocation().getInvItem().getIiDescription());
                                details.setAdjLocation(invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName());
                                details.setAdjQuantity(invAdjustmentLine.getAlAdjustQuantity());
                                details.setAdjUnit(invAdjustmentLine.getInvUnitOfMeasure().getUomName());
                                details.setAdjUnitCost(invAdjustmentLine.getAlUnitCost());
                                details.setAdjAccountDescription(invAdjustment.getGlChartOfAccount().getCoaAccountDescription());
                                System.out.print(invAdjustment.getGlChartOfAccount().getCoaAccountDescription() + "here" + invAdjustment.getGlChartOfAccount().getCoaAccountNumber());
                                AMOUNT = EJBCommon.roundIt(invAdjustmentLine.getAlAdjustQuantity() * invAdjustmentLine.getAlUnitCost(), this.getGlFcPrecisionUnit(AD_CMPNY));
                                details.setAdjAmount(AMOUNT);
                                details.setOrderBy(ORDER_BY);
                                Debug.print("1");
                                list.add(details);
                            }
                        } else {
                            Debug.print("ELSE-------------------------");
                            InvRepAdjustmentRegisterDetails details = new InvRepAdjustmentRegisterDetails();
                            details.setAdjDate(invAdjustment.getAdjDate());
                            details.setAdjDocumentNumber(invAdjustment.getAdjDocumentNumber());
                            details.setAdjReferenceNumber(invAdjustment.getAdjReferenceNumber());
                            details.setAdjType(invAdjustment.getAdjType());
                            details.setAdjDescription(invAdjustment.getAdjDescription());
                            details.setAdjAccountDescription(invAdjustment.getGlChartOfAccount().getCoaAccountDescription());
                            System.out.print(invAdjustment.getGlChartOfAccount().getCoaAccountDescription() + "here" + invAdjustment.getGlChartOfAccount().getCoaAccountNumber());
                            double AMOUNT = 0d;
                            Collection invAdjustmentLines = invAdjustment.getInvAdjustmentLines();
                            for (Object adjustmentLine : invAdjustmentLines) {
                                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) adjustmentLine;
                                AMOUNT += EJBCommon.roundIt(invAdjustmentLine.getAlAdjustQuantity() * invAdjustmentLine.getAlUnitCost(), this.getGlFcPrecisionUnit(AD_CMPNY));
                            }
                            details.setAdjAmount(AMOUNT);
                            details.setOrderBy(ORDER_BY);
                            list.add(details);
                        }
                    }

                    // sort
                    Debug.print("2");
                    if (GROUP_BY.equals("ITEM NAME")) {
                        list.sort(InvRepAdjustmentRegisterDetails.ItemNameComparator);
                    } else if (GROUP_BY.equals("DATE")) {
                        list.sort(InvRepAdjustmentRegisterDetails.DateComparator);
                    } else {
                        list.sort(InvRepAdjustmentRegisterDetails.NoGroupComparator);
                    }
                    Debug.print("3");
                    return list;
                } catch (GlobalNoRecordFoundException ex) {
                    throw ex;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    throw new EJBException(ex.getMessage());
                }
        }
    }

    // private methods
    private int isValidDate(String input) {

        String formatString = "MM/dd/yyyy";
        int x = 0;
        try {
            SimpleDateFormat format = new SimpleDateFormat(formatString);
            format.setLenient(false);
            format.parse(input);
        } catch (ParseException | IllegalArgumentException e) {
            return x;
        }
        x = 1;
        return x;
    }

    private Collection getAdjByCriteria(HashMap criteria, String ORDER_BY, ArrayList adBrnchList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvRepAdjustmentRegisterControllerBean getAdjByCriteria");

        LocalInvAdjustmentHome invAdjustmentHome = null;

        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(adj) FROM InvAdjustment adj ");
            if (criteria.containsKey("itemName")) {
                jbossQl.append(", IN(adj.invAdjustmentLines) al ");
            }
            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();
            Object[] obj;

            // Allocate the size of the object parameter
            if (criteria.containsKey("includedUnposted")) {
                criteriaSize--;
            }

            obj = new Object[criteriaSize];
            if (criteria.containsKey("type")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("adj.adjType=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("type");
                ctr++;
            }
            if (criteria.containsKey("accountDescription")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                System.out.print("hhhhhererhere" + criteria.get("accountDescription"));
                jbossQl.append("adj.glChartOfAccount.coaAccountNumber=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("accountDescription");
                ctr++;
            }
            if (criteria.containsKey("dateFrom")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("adj.adjDate>=?").append(ctr + 1).append(" ");
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
                jbossQl.append("adj.adjDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }
            if (criteria.containsKey("documentNumberFrom")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("adj.adjDocumentNumber>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("documentNumberFrom");
                ctr++;
            }
            if (criteria.containsKey("documentNumberTo")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("adj.adjDocumentNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("documentNumberTo");
                ctr++;
            }
            if (criteria.containsKey("referenceNumber")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("adj.adjReferenceNumber>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("referenceNumber");
                ctr++;
            }

            if (criteria.containsKey("itemName")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("al.invItemLocation.invItem.iiName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("itemName");
                ctr++;
            }
            if (criteria.get("includedUnposted").equals("NO")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("adj.adjPosted = 1 ");
            }
            if (adBrnchList.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            } else {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("adj.adjAdBranch in (");
                boolean firstLoop = true;
                for (Object o : adBrnchList) {
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
            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("adj.adjAdCompany = ").append(AD_CMPNY).append(" ");
            String orderBy = null;
            if (ORDER_BY.equals("DATE")) {
                orderBy = "adj.adjDate";
            } else if (ORDER_BY.equals("DOCUMENT NUMBER")) {
                orderBy = "adj.adjDocumentNumber";
            }
            if (orderBy != null) {
                jbossQl.append("ORDER BY ").append(orderBy);
            }
            Collection invAdjustments = null;
            try {
                invAdjustments = invAdjustmentHome.getAdjByCriteria(jbossQl.toString(), obj, 0, 0);
            } catch (FinderException ex) {
            }
            if (!criteria.containsKey("itemName")) {
                Debug.print("jbossQl-" + jbossQl);
                Debug.print("criteria-" + criteria.get("type"));
                Debug.print("private-" + invAdjustments.size());
            }
            return invAdjustments;
        } catch (GlobalNoRecordFoundException ex) {
            throw ex;
        } catch (EJBException ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public void ejbCreate() throws CreateException {

        Debug.print("InvRepAdjustmentRegisterControllerBean ejbCreate");
    }

}