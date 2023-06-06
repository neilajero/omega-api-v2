package com.ejb.txnsync.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ap.LocalApPurchaseOrder;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.inv.*;
import com.ejb.exception.global.GlobalBranchAccountNumberInvalidException;
import com.ejb.txn.OmegaCommonDataController;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.mod.inv.InvModAdjustmentLineDetails;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import java.util.*;

@Stateless(name = "InvReceivingSyncControllerBeanEJB")
public class InvReceivingSyncControllerBean extends EJBContextClass implements InvReceivingSyncController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalInvAdjustmentHome invAdjustmentHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvAdjustmentLineHome invAdjustmentLineHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvPhysicalInventoryHome invPhysicalInventoryHome;
    @EJB
    private LocalInvPhysicalInventoryLineHome invPhysicalInventoryLineHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalInvDistributionRecordHome invDistributionRecordHome;
    @EJB
    private OmegaCommonDataController commonData;

    @Override
    public int setInvReceivingAllNewAndVoid(String[] newRR, String[] voidRR, String BR_BRNCH_CODE, Integer AD_CMPNY, String INV_LOCATION, String CASHIER) {

        Debug.print("InvReceivingSyncControllerBean setInvReceivingAllNewAndVoid");

        LocalAdBranch adBranch = null;
        LocalAdPreference adPreference = null;
        LocalApPurchaseOrder apPurchaseOrder = null;

        int success = 0;

        try {

            try {
                adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            }
            catch (FinderException ex) {

            }

            assert adPreference != null;
            if (adPreference.getPrfInvEnablePosIntegration() == EJBCommon.TRUE) {
                try {
                    adBranch = adBranchHome.findByBrBranchCode(BR_BRNCH_CODE, AD_CMPNY);
                }
                catch (FinderException ex) {
                }

                assert adBranch != null;
                Integer AD_BRNCH = adBranch.getBrCode();

                Debug.print(" ================DELIVERY================ ");

                ArrayList strArrayAdj = null;
                ArrayList strArrayPInv = null;
                String[] rowsAdj = null;
                String[] rowsPInv = null;
                int count = 0;

                if (!newRR[0].equals("NULL")) {

                    strArrayAdj = new ArrayList();
                    strArrayPInv = new ArrayList();

                    for (int i = 0; i < newRR.length; i++) {

                        StringTokenizer strToken = new StringTokenizer(newRR[i], "~", false);
                        String strArr[] = this.receivingHeaderDecode(strToken.nextToken());

                        String TRANSACTION_TYPE = strArr[1];

                        if (TRANSACTION_TYPE.equals("TRANSFER-IN") || TRANSACTION_TYPE.equals("TRANSFER-OUT")) {

                            strArrayAdj.add(newRR[i]);

                        } else {

                            strArrayPInv.add(newRR[i]);

                        }

                    }

                    rowsAdj = this.groupByTransactionType(strArrayAdj);
                    rowsPInv = this.groupPhysicalInvByCategory(strArrayPInv, AD_CMPNY);

                    ArrayList newRRList = new ArrayList();

                    for (int i = 0; i < rowsAdj.length; i++) {

                        if (rowsAdj[i] != null) {

                            newRRList.add(rowsAdj[i]);

                        }

                    }

                    for (int i = 0; i < rowsPInv.length; i++) {

                        if (rowsPInv[i] != null) {

                            newRRList.add(rowsPInv[i]);

                        }

                    }

                    Iterator newRRIter = newRRList.iterator();
                    count = 0;
                    while (newRRIter.hasNext()) {

                        String row = (String) newRRIter.next();

                        Debug.print("rowsAdj[" + count + "]" + row);

                        count++;

                        if (row == null) {

                            continue;

                        }

                        StringTokenizer strToken = new StringTokenizer(row, "~", false);
                        //get header
                        String drHeader = strToken.nextToken();
                        String[] results = receivingHeaderDecode(drHeader);

                        String DR_NUM = results[0];
                        String TRANSACTION_TYPE = results[1];
                        Date DATE_RECEIVED = EJBCommon.convertStringToSQLDate(results[4]);

                        if (TRANSACTION_TYPE.equals("TRANSFER-IN") || TRANSACTION_TYPE.equals("TRANSFER-OUT")) {

                            String DOC_NUM = this.generateDocumentNumber(AD_BRNCH, AD_CMPNY);

                            //create adjustment

                            LocalInvAdjustment invAdjustment = invAdjustmentHome.create(DOC_NUM, DR_NUM, null, DATE_RECEIVED,
                                    "GENERAL", null, EJBCommon.FALSE, CASHIER, DATE_RECEIVED, CASHIER, DATE_RECEIVED, null, null,
                                    null, null, null, null, EJBCommon.FALSE, EJBCommon.FALSE, AD_BRNCH, AD_CMPNY);

                            LocalGlChartOfAccount glPosAdjustmentAccount = null;

                            try {

                                glPosAdjustmentAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(adPreference.getPrfInvPosAdjustmentAccount(), AD_BRNCH, AD_CMPNY);
                                glPosAdjustmentAccount.addInvAdjustment(invAdjustment);

                            }
                            catch (FinderException ex) {

                            }

                            if (glPosAdjustmentAccount == null) {

                                try {

                                    glPosAdjustmentAccount = glChartOfAccountHome.findByCoaCode(adPreference.getPrfInvPosAdjustmentAccount(), AD_CMPNY);
                                    glPosAdjustmentAccount.addInvAdjustment(invAdjustment);

                                }
                                catch (FinderException ex) {

                                }

                            }

                            //add adjustment lines

                            while (strToken.hasMoreElements()) {

                                String drLine = strToken.nextToken();

                                results = receivingLineDecode(drLine);

                                double ADJ_QTTY = Double.parseDouble(results[1]);
                                String ITEM_NAME = results[2];
                                String UNIT_OF_MEASURE = results[3];

                                LocalInvItem invItem = null;

                                try {

                                    invItem = invItemHome.findByIiName(ITEM_NAME, AD_CMPNY);

                                }
                                catch (FinderException ex) {

                                    // jolly
                                    System.out.println("ITEM : " + ITEM_NAME);
                                    throw ex;
                                    // continue;

                                }

                                LocalInvItemLocation invItemLocation = null;

                                try {

                                    invItemLocation = invItemLocationHome.findByLocNameAndIiName(INV_LOCATION, ITEM_NAME, AD_CMPNY);

                                }
                                catch (FinderException ex) {

                                    continue;

                                }

                                //use negative adjustments for transactions of type 'TRANSFER-OUT'

                                if (TRANSACTION_TYPE.equals("TRANSFER-OUT")) {

                                    ADJ_QTTY = ADJ_QTTY * -1;

                                }

                                //check if adjustment line with the same item location already exists

                                LocalInvAdjustmentLine existingInvAdjustmentLine = null;

                                try {

                                    existingInvAdjustmentLine = invAdjustmentLineHome.findByAdjCodeAndIlCode(invAdjustment.getAdjCode(), invItemLocation.getIlCode(), AD_CMPNY);

                                }
                                catch (FinderException ex) {

                                }

                                if (existingInvAdjustmentLine == null) {

                                    //create adjustment line

                                    InvModAdjustmentLineDetails mdetails = new InvModAdjustmentLineDetails();
                                    mdetails.setAlAdjustQuantity(ADJ_QTTY);
                                    mdetails.setAlUnitCost(commonData.getInvIiUnitCostByIiNameAndUomName(invItem.getIiName(), UNIT_OF_MEASURE, AD_CMPNY));
                                    mdetails.setAlUomName(UNIT_OF_MEASURE);
                                    mdetails.setAlLocName(INV_LOCATION);
                                    mdetails.setAlIiName(invItem.getIiName());

                                    LocalInvAdjustmentLine invAdjustmentLine = this.addInvAlEntry(mdetails, invAdjustment, AD_CMPNY);

                                } else {

                                    //add ADJ_QTTY to adjustment line quantity
                                    if (existingInvAdjustmentLine.getInvUnitOfMeasure().getUomName().equalsIgnoreCase(UNIT_OF_MEASURE)) {
                                        existingInvAdjustmentLine.setAlAdjustQuantity(existingInvAdjustmentLine.getAlAdjustQuantity() + ADJ_QTTY);
                                    } else {

                                        // just create another adjustment line

                                        InvModAdjustmentLineDetails mdetails = new InvModAdjustmentLineDetails();
                                        mdetails.setAlAdjustQuantity(ADJ_QTTY);
                                        mdetails.setAlUnitCost(commonData.getInvIiUnitCostByIiNameAndUomName(invItem.getIiName(), UNIT_OF_MEASURE, AD_CMPNY));
                                        mdetails.setAlUomName(UNIT_OF_MEASURE);
                                        mdetails.setAlLocName(INV_LOCATION);
                                        mdetails.setAlIiName(invItem.getIiName());

                                        LocalInvAdjustmentLine invAdjustmentLine = this.addInvAlEntry(mdetails, invAdjustment, AD_CMPNY);
                                    }
                                }
                            }

                            this.createDistributionRecords(invAdjustment, DATE_RECEIVED, AD_BRNCH, AD_CMPNY);

                        } else {

                            //create physical inventory, set category to " " temporarily
                            LocalInvPhysicalInventory invPhysicalInventory = invPhysicalInventoryHome.create(DATE_RECEIVED, DR_NUM,
                                    null, CASHIER, DATE_RECEIVED, CASHIER, DATE_RECEIVED, " ", EJBCommon.FALSE, EJBCommon.FALSE,
                                    AD_BRNCH, AD_CMPNY);

                            LocalInvLocation invLocation = invLocationHome.findByLocName(INV_LOCATION, AD_CMPNY);
                            invLocation.addInvPhysicalInventory(invPhysicalInventory);

                            LocalGlChartOfAccount glPosAdjustmentAccount = null;

                            try {

                                glPosAdjustmentAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(adPreference.getPrfInvPosAdjustmentAccount(), AD_BRNCH, AD_CMPNY);

                            }
                            catch (FinderException ex) {

                            }

                            if (glPosAdjustmentAccount == null) {

                                try {

                                    glPosAdjustmentAccount = glChartOfAccountHome.findByCoaCode(adPreference.getPrfInvPosAdjustmentAccount(), AD_CMPNY);

                                }
                                catch (FinderException ex) {

                                }

                            }

                            boolean categorySet = false;

                            while (strToken.hasMoreElements()) {

                                String drLine = strToken.nextToken();
                                results = receivingLineDecode(drLine);

                                double PI_QTTY = Double.parseDouble(results[1]);
                                String ITEM_NAME = results[2];

                                LocalInvItemLocation invItemLocation = null;

                                try {

                                    invItemLocation = invItemLocationHome.findByLocNameAndIiName(INV_LOCATION, ITEM_NAME, AD_CMPNY);

                                }
                                catch (FinderException ex) {

                                    continue;

                                }

                                //set category

                                if (categorySet == false) {

                                    invPhysicalInventory.setPiAdLvCategory(invItemLocation.getInvItem().getIiAdLvCategory());
                                    categorySet = true;

                                }

                                LocalInvPhysicalInventoryLine existingInvPhysicalInventoryLine = null;

                                try {

                                    existingInvPhysicalInventoryLine = invPhysicalInventoryLineHome.findByPiCodeAndIlCode(invPhysicalInventory.getPiCode(), invItemLocation.getIlCode(), AD_CMPNY);

                                }
                                catch (FinderException ex) {

                                }

                                double ACTUAL = 0d;

                                try {

                                    LocalInvCosting invCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(EJBCommon.getGcCurrentDateWoTime().getTime().getTime(),
                                            invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, AD_CMPNY);

                                    ACTUAL = invCosting.getCstRemainingQuantity();

                                }
                                catch (FinderException ex) {

                                }

                                double VARIANCE = ACTUAL - PI_QTTY;

                                if (existingInvPhysicalInventoryLine == null) {

                                    LocalInvPhysicalInventoryLine invPhysicalInventoryLine = invPhysicalInventoryLineHome.create(PI_QTTY, 0d, VARIANCE, null, AD_CMPNY);

                                    invPhysicalInventory.addInvPhysicalInventoryLine(invPhysicalInventoryLine);
                                    invItemLocation.addInvPhysicalInventoryLine(invPhysicalInventoryLine);

                                } else {

                                    existingInvPhysicalInventoryLine.setPilEndingInventory(existingInvPhysicalInventoryLine.getPilEndingInventory() + PI_QTTY);
                                    existingInvPhysicalInventoryLine.setPilVariance(VARIANCE - existingInvPhysicalInventoryLine.getPilEndingInventory());

                                }

                            }

                            this.executeInvVarianceGeneration(invPhysicalInventory.getPiCode(), glPosAdjustmentAccount.getCoaAccountNumber(), CASHIER, AD_BRNCH, AD_CMPNY);

                        }

                    }

                }

                Debug.print(" ============VOID DELIVERY============== ");

                //if(!voidRR[0].equals("NULL")) {
                if (false) {    // skip this part first... ofs does not support void Adjustments/Physical Inventory

                    strArrayAdj = new ArrayList();
                    strArrayPInv = new ArrayList();

                    for (int i = 0; i < voidRR.length; i++) {

                        StringTokenizer strToken = new StringTokenizer(voidRR[i], "~", false);
                        String strArr[] = this.receivingHeaderDecode(strToken.nextToken());

                        String TRANSACTION_TYPE = strArr[1];

                        if (TRANSACTION_TYPE.equals("TRANSFER-IN") || TRANSACTION_TYPE.equals("TRANSFER-OUT")) {

                            strArrayAdj.add(voidRR[i]);

                        } else {

                            strArrayPInv.add(voidRR[i]);

                        }

                    }

                    rowsAdj = this.groupByTransactionType(strArrayAdj);
                    rowsPInv = this.groupPhysicalInvByCategory(strArrayPInv, AD_CMPNY);

                    ArrayList voidRRList = new ArrayList();

                    for (int i = 0; i < rowsAdj.length; i++) {

                        if (rowsAdj[i] != null) {

                            voidRRList.add(rowsAdj[i]);

                        }

                    }

                    for (int i = 0; i < rowsPInv.length; i++) {

                        if (rowsPInv[i] != null) {

                            voidRRList.add(rowsPInv[i]);

                        }

                    }

                    Iterator voidRRIter = voidRRList.iterator();
                    count = 0;

                    while (voidRRIter.hasNext()) {

                        String row = (String) voidRRIter.next();

                        Debug.print("rowsAdj[" + count + "]" + row);

                        count++;

                        if (row == null) {

                            continue;

                        }

                        StringTokenizer strToken = new StringTokenizer(row, "~", false);

                        String drHeader = strToken.nextToken();
                        String[] results = receivingHeaderDecode(drHeader);

                        String DR_NUM = results[0];
                        String TRANSACTION_TYPE = results[1];
                        Date DATE_RECEIVED = EJBCommon.convertStringToSQLDate(results[4]);

                        if (TRANSACTION_TYPE.equals("TRANSFER-IN") || TRANSACTION_TYPE.equals("TRANSFER-OUT")) {

                            String DOC_NUM = this.generateDocumentNumber(AD_BRNCH, AD_CMPNY);

                            //create adjustment

                            LocalInvAdjustment invAdjustment = invAdjustmentHome.create(DOC_NUM, DR_NUM, null, DATE_RECEIVED,
                                    "GENERAL", null, EJBCommon.FALSE, CASHIER, DATE_RECEIVED, CASHIER, DATE_RECEIVED, null, null, null,
                                    null, null, null, EJBCommon.FALSE, EJBCommon.FALSE, AD_BRNCH, AD_CMPNY);

                            LocalGlChartOfAccount glPosAdjustmentAccount = null;

                            try {

                                glPosAdjustmentAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(adPreference.getPrfInvPosAdjustmentAccount(), AD_BRNCH, AD_CMPNY);
                                glPosAdjustmentAccount.addInvAdjustment(invAdjustment);

                            }
                            catch (FinderException ex) {

                            }

                            if (glPosAdjustmentAccount == null) {

                                try {

                                    glPosAdjustmentAccount = glChartOfAccountHome.findByCoaCode(adPreference.getPrfInvPosAdjustmentAccount(), AD_CMPNY);
                                    glPosAdjustmentAccount.addInvAdjustment(invAdjustment);

                                }
                                catch (FinderException ex) {

                                }

                            }

                            //add adjustment lines

                            double TOTAL_AMOUNT = 0d;
                            double ABS_TOTAL_AMOUNT = 0d;

                            while (strToken.hasMoreElements()) {

                                String drLine = strToken.nextToken();
                                results = receivingLineDecode(drLine);

                                double ADJ_QTTY = Double.parseDouble(results[1]);
                                String ITEM_NAME = results[2];
                                String UNIT_OF_MEASURE = results[3];

                                LocalInvItem invItem = null;

                                try {

                                    invItem = invItemHome.findByIiName(ITEM_NAME, AD_CMPNY);

                                }
                                catch (FinderException ex) {

                                    continue;

                                }

                                LocalInvItemLocation invItemLocation = null;

                                try {

                                    invItemLocation = invItemLocationHome.findByLocNameAndIiName(INV_LOCATION, ITEM_NAME, AD_CMPNY);

                                }
                                catch (FinderException ex) {

                                    continue;

                                }

                                //use negative adjustments for transactions of type 'TRANSFER-OUT'

                                if (TRANSACTION_TYPE.equals("TRANSFER-OUT")) {

                                    ADJ_QTTY = ADJ_QTTY * -1;

                                }

                                //check if adjustment line with the same item location already exists

                                LocalInvAdjustmentLine existingInvAdjustmentLine = null;

                                try {

                                    existingInvAdjustmentLine = invAdjustmentLineHome.findByAdjCodeAndIlCode(invAdjustment.getAdjCode(), invItemLocation.getIlCode(), AD_CMPNY);

                                }
                                catch (FinderException ex) {

                                }

                                if (existingInvAdjustmentLine == null) {

                                    //create adjustment line

                                    InvModAdjustmentLineDetails mdetails = new InvModAdjustmentLineDetails();
                                    mdetails.setAlAdjustQuantity(ADJ_QTTY);
                                    mdetails.setAlUnitCost(commonData.getInvIiUnitCostByIiNameAndUomName(invItem.getIiName(), UNIT_OF_MEASURE, AD_CMPNY));
                                    mdetails.setAlUomName(UNIT_OF_MEASURE);
                                    mdetails.setAlLocName(INV_LOCATION);
                                    mdetails.setAlIiName(invItem.getIiName());

                                    LocalInvAdjustmentLine invAdjustmentLine = this.addInvAlEntry(mdetails, invAdjustment, AD_CMPNY);

                                } else {

                                    //add ADJ_QTTY to adjustment line quantity

                                    if (existingInvAdjustmentLine.getInvUnitOfMeasure().getUomName().equalsIgnoreCase(UNIT_OF_MEASURE)) {
                                        existingInvAdjustmentLine.setAlAdjustQuantity(existingInvAdjustmentLine.getAlAdjustQuantity() + ADJ_QTTY);
                                    } else {

                                        // just create another adjustment line

                                        InvModAdjustmentLineDetails mdetails = new InvModAdjustmentLineDetails();
                                        mdetails.setAlAdjustQuantity(ADJ_QTTY);
                                        mdetails.setAlUnitCost(commonData.getInvIiUnitCostByIiNameAndUomName(invItem.getIiName(), UNIT_OF_MEASURE, AD_CMPNY));
                                        mdetails.setAlUomName(UNIT_OF_MEASURE);
                                        mdetails.setAlLocName(INV_LOCATION);
                                        mdetails.setAlIiName(invItem.getIiName());

                                        LocalInvAdjustmentLine invAdjustmentLine = this.addInvAlEntry(mdetails, invAdjustment, AD_CMPNY);

                                    }
                                }

                            }

                            this.createDistributionRecords(invAdjustment, DATE_RECEIVED, AD_BRNCH, AD_CMPNY);

                        } else {

                            //create physical inventory, set category to " " temporarily
                            LocalInvPhysicalInventory invPhysicalInventory = invPhysicalInventoryHome.create(DATE_RECEIVED, DR_NUM,
                                    null, CASHIER, DATE_RECEIVED, CASHIER, DATE_RECEIVED, " ", EJBCommon.FALSE, EJBCommon.FALSE,
                                    AD_BRNCH, AD_CMPNY);

                            LocalInvLocation invLocation = invLocationHome.findByLocName(INV_LOCATION, AD_CMPNY);
                            invLocation.addInvPhysicalInventory(invPhysicalInventory);

                            LocalGlChartOfAccount glPosAdjustmentAccount = null;

                            try {

                                glPosAdjustmentAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(adPreference.getPrfInvPosAdjustmentAccount(), AD_BRNCH, AD_CMPNY);

                            }
                            catch (FinderException ex) {

                            }

                            if (glPosAdjustmentAccount == null) {

                                try {

                                    glPosAdjustmentAccount = glChartOfAccountHome.findByCoaCode(adPreference.getPrfInvPosAdjustmentAccount(), AD_CMPNY);

                                }
                                catch (FinderException ex) {

                                }

                            }

                            boolean categorySet = false;

                            while (strToken.hasMoreElements()) {

                                String drLine = strToken.nextToken();
                                results = receivingLineDecode(drLine);

                                double PI_QTTY = Double.parseDouble(results[1]);
                                String ITEM_NAME = results[2];

                                LocalInvItemLocation invItemLocation = null;

                                try {

                                    invItemLocation = invItemLocationHome.findByLocNameAndIiName(INV_LOCATION, ITEM_NAME, AD_CMPNY);

                                }
                                catch (FinderException ex) {

                                    continue;

                                }

                                //set category
                                if (categorySet == false) {

                                    invPhysicalInventory.setPiAdLvCategory(invItemLocation.getInvItem().getIiAdLvCategory());
                                    categorySet = true;

                                }

                                LocalInvPhysicalInventoryLine existingInvPhysicalInventoryLine = null;

                                try {

                                    existingInvPhysicalInventoryLine = invPhysicalInventoryLineHome.findByPiCodeAndIlCode(invPhysicalInventory.getPiCode(), invItemLocation.getIlCode(), AD_CMPNY);

                                }
                                catch (FinderException ex) {

                                }

                                double ACTUAL = 0d;

                                try {

                                    LocalInvCosting invCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(EJBCommon.getGcCurrentDateWoTime().getTime().getTime(),
                                            invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, AD_CMPNY);

                                    ACTUAL = invCosting.getCstRemainingQuantity();

                                }
                                catch (FinderException ex) {

                                }

                                double VARIANCE = ACTUAL - PI_QTTY;

                                if (existingInvPhysicalInventoryLine == null) {

                                    LocalInvPhysicalInventoryLine invPhysicalInventoryLine = invPhysicalInventoryLineHome.create(PI_QTTY, 0d, VARIANCE, null, AD_CMPNY);

                                    invPhysicalInventory.addInvPhysicalInventoryLine(invPhysicalInventoryLine);
                                    invItemLocation.addInvPhysicalInventoryLine(invPhysicalInventoryLine);

                                } else {

                                    existingInvPhysicalInventoryLine.setPilEndingInventory(existingInvPhysicalInventoryLine.getPilEndingInventory() + PI_QTTY);
                                    existingInvPhysicalInventoryLine.setPilVariance(VARIANCE - existingInvPhysicalInventoryLine.getPilEndingInventory());

                                }

                            }

                            this.executeInvVarianceGeneration(invPhysicalInventory.getPiCode(), glPosAdjustmentAccount.getCoaAccountNumber(), CASHIER, AD_BRNCH, AD_CMPNY);


                        }

                        String DOC_NUM = this.generateDocumentNumber(AD_BRNCH, AD_CMPNY);

                        //create reverse adjustment

                        LocalInvAdjustment invAdjustment = invAdjustmentHome.create(DOC_NUM, DR_NUM, null, DATE_RECEIVED,
                                "GENERAL", null, EJBCommon.FALSE, CASHIER, DATE_RECEIVED, CASHIER, DATE_RECEIVED, null, null, null,
                                null, null, null, EJBCommon.FALSE, EJBCommon.FALSE, AD_BRNCH, AD_CMPNY);

                        LocalGlChartOfAccount glPosAdjustmentAccount = null;

                        try {

                            glPosAdjustmentAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(adPreference.getPrfInvPosAdjustmentAccount(), AD_BRNCH, AD_CMPNY);
                            glPosAdjustmentAccount.addInvAdjustment(invAdjustment);

                        }
                        catch (FinderException ex) {

                        }

                        if (glPosAdjustmentAccount == null) {

                            try {

                                glPosAdjustmentAccount = glChartOfAccountHome.findByCoaCode(adPreference.getPrfInvPosAdjustmentAccount(), AD_CMPNY);
                                glPosAdjustmentAccount.addInvAdjustment(invAdjustment);

                            }
                            catch (FinderException ex) {

                            }

                        }

                        //add adjustment lines

                        StringTokenizer strTokenVoid = new StringTokenizer(row, "~", false);
                        strTokenVoid.nextToken();

                        double TOTAL_AMOUNT = 0d;
                        double ABS_TOTAL_AMOUNT = 0d;

                        while (strTokenVoid.hasMoreElements()) {

                            String drLine = strTokenVoid.nextToken();
                            results = receivingLineDecode(drLine);

                            double ADJ_QTTY = Double.parseDouble(results[1]);
                            String ITEM_NAME = results[2];

                            LocalInvItem invItem = null;

                            try {

                                invItem = invItemHome.findByIiName(ITEM_NAME, AD_CMPNY);

                            }
                            catch (FinderException ex) {

                                continue;

                            }

                            LocalInvItemLocation invItemLocation = null;

                            try {

                                invItemLocation = invItemLocationHome.findByLocNameAndIiName(INV_LOCATION, ITEM_NAME, AD_CMPNY);

                            }
                            catch (FinderException ex) {

                                continue;

                            }

                            //use negative adjustments to reverse transactions of type 'TRANSFER-OUT' and 'PHYSICAL INVENTORY'

                            if (!TRANSACTION_TYPE.equals("TRANSFER-OUT")) {

                                ADJ_QTTY = ADJ_QTTY * -1;

                            }

                            //check if adjustment line with the same item location already exists

                            LocalInvAdjustmentLine existingInvAdjustmentLine = null;

                            try {

                                existingInvAdjustmentLine = invAdjustmentLineHome.findByAdjCodeAndIlCode(invAdjustment.getAdjCode(), invItemLocation.getIlCode(), AD_CMPNY);

                            }
                            catch (FinderException ex) {

                            }

                            if (existingInvAdjustmentLine == null) {

                                //create adjustment line

                                InvModAdjustmentLineDetails mdetails = new InvModAdjustmentLineDetails();
                                mdetails.setAlAdjustQuantity(ADJ_QTTY);
                                mdetails.setAlUnitCost(invItem.getIiUnitCost());
                                mdetails.setAlUomName(invItem.getInvUnitOfMeasure().getUomName());
                                mdetails.setAlLocName(INV_LOCATION);
                                mdetails.setAlIiName(invItem.getIiName());

                                LocalInvAdjustmentLine invAdjustmentLine = this.addInvAlEntry(mdetails, invAdjustment, AD_CMPNY);

                            } else {

                                //add ADJ_QTTY to adjustment line quantity

                                existingInvAdjustmentLine.setAlAdjustQuantity(existingInvAdjustmentLine.getAlAdjustQuantity() + ADJ_QTTY);

                            }

                        }

                        this.createDistributionRecords(invAdjustment, DATE_RECEIVED, AD_BRNCH, AD_CMPNY);

                    }

                }

                success = 1;
                return success;

            }

        }
        catch (Exception ex) {

            ctx.setRollbackOnly();
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

        return success;
    }

    private String[] receivingHeaderDecode(String receiving) {

        Debug.print("InvReceivingSyncControllerBean receivingHeaderDecode");

        String separator = new String(EJBCommon.SEPARATOR + "");
        String[] decodedResult = new String[5];
        boolean isEmptyString = false;

        StringTokenizer strToken = new StringTokenizer(receiving, separator, true);

        String sep = null;

        sep = strToken.nextToken();
        //dr number
        decodedResult[0] = strToken.nextToken();
        if (decodedResult[0].equals(separator)) {

            decodedResult[0] = "";

        } else {

            sep = strToken.nextToken();

        }

        //transaction type
        decodedResult[1] = strToken.nextToken();
        if (decodedResult[1].equals(separator)) {

            decodedResult[1] = "";

        } else {

            sep = strToken.nextToken();

        }

        //void
        decodedResult[2] = strToken.nextToken();
        if (decodedResult[2].equals(separator)) {

            decodedResult[2] = "";

        } else {

            sep = strToken.nextToken();

        }

        //void reason
        decodedResult[3] = strToken.nextToken();
        if (decodedResult[3].equals(separator)) {

            decodedResult[3] = "";

        } else {

            sep = strToken.nextToken();

        }

        //date received
        decodedResult[4] = strToken.nextToken();
        if (decodedResult[4].equals(separator)) {

            decodedResult[4] = "";

        } else {

            sep = strToken.nextToken();

        }

        return decodedResult;

    }

    private String[] receivingLineDecode(String receivingLine) {

        Debug.print("InvReceivingSyncControllerBean receivingLineDecode");

        String separator = new String(EJBCommon.SEPARATOR + "");
        String[] decodedResult = new String[4];

        StringTokenizer strToken = new StringTokenizer(receivingLine, separator, true);

        String sep = null;

        sep = strToken.nextToken();

        //line number
        decodedResult[0] = strToken.nextToken();
        if (decodedResult[0].equals(separator)) {

            decodedResult[0] = "";

        } else {

            sep = strToken.nextToken();

        }

        //quantity
        decodedResult[1] = strToken.nextToken();
        if (decodedResult[1].equals(separator)) {

            decodedResult[1] = "";

        } else {

            sep = strToken.nextToken();

        }

        //product
        decodedResult[2] = strToken.nextToken();
        if (decodedResult[2].equals(separator)) {

            decodedResult[2] = "";

        } else {

            sep = strToken.nextToken();

        }

        //unit of measure
        decodedResult[3] = strToken.nextToken();
        if (decodedResult[3].equals(separator)) {

            decodedResult[3] = "";

        } else {

            sep = strToken.nextToken();

        }

        return decodedResult;

    }

    private String[] groupByTransactionType(ArrayList strArray) {

        Debug.print("InvReceivingSyncControllerBean groupByTransactionType");

        String[] rows = new String[2];
        //TRANSFER-IN
        rows[0] = null;
        //TRANSFER-OUT
        rows[1] = null;

        String transferIn = "";
        String transferOut = "";

        Iterator i = strArray.iterator();


        while (i.hasNext()) {

            String str = (String) i.next();

            StringTokenizer strToken = new StringTokenizer(str, "~", false);

            String header = strToken.nextToken();
            String results[] = this.receivingHeaderDecode(header);

            String TRANSACTION_TYPE = results[1];

            if (TRANSACTION_TYPE.equals("TRANSFER-IN")) {

                transferIn = transferIn + "~" + str;

            } else if (TRANSACTION_TYPE.equals("TRANSFER-OUT")) {

                transferOut = transferOut + "~" + str;
            }

        }

        if (!transferIn.equals("")) {

            //transfer in
            StringTokenizer strToken = new StringTokenizer(transferIn, "~", false);

            //header
            rows[0] = strToken.nextToken();

            while (strToken.hasMoreTokens()) {

                String token = strToken.nextToken();

                if (countSeparator(token) <= 5) {

                    rows[0] = rows[0] + "~" + token;

                }

            }

        }

        if (!transferOut.equals("")) {

            //transfer out
            StringTokenizer strToken = new StringTokenizer(transferOut, "~", false);

            //header
            rows[1] = strToken.nextToken();

            while (strToken.hasMoreTokens()) {

                String token = strToken.nextToken();

                if (countSeparator(token) <= 5) {

                    rows[1] = rows[1] + "~" + token;

                }

            }

        }

        return rows;

    }

    private String[] groupPhysicalInvByCategory(ArrayList strArray, Integer AD_CMPNY) {

        Debug.print("InvReceivingSyncControllerBean groupPhysicalInvByCategory");

        ArrayList invCategories = this.getCategories(strArray, AD_CMPNY);

        String[] rows = new String[invCategories.size()];

        for (int ctr = 0; ctr < rows.length; ctr++) {
            rows[ctr] = null;
        }

        String[] invCategory = new String[invCategories.size()];

        for (int ctr = 0; ctr < invCategory.length; ctr++) {
            invCategory[ctr] = "";
        }


        Iterator i = strArray.iterator();

        while (i.hasNext()) {

            String str = (String) i.next();

            StringTokenizer strToken = new StringTokenizer(str, "~", false);

            String header = strToken.nextToken();
            String results[] = this.receivingHeaderDecode(header);

            while (strToken.hasMoreTokens()) {

                String line = (String) strToken.nextToken();

                String[] lineResults = this.receivingLineDecode(line);
                String PRODUCT = lineResults[2];

                LocalInvItem invItem = null;

                try {

                    invItem = invItemHome.findByIiName(PRODUCT, AD_CMPNY);

                }
                catch (FinderException ex) {

                }

                for (int ctr = 0; ctr < invCategories.size(); ctr++) {

                    if (invItem.getIiAdLvCategory().equals((String) invCategories.get(ctr))) {

                        if (invCategory[ctr].equals("")) {
                            invCategory[ctr] = header;
                        }

                        invCategory[ctr] = invCategory[ctr] + "~" + line;

                        break;

                    }

                }

            }

        }

        for (int ctr = 0; ctr < invCategory.length; ctr++) {

            if (!invCategory[ctr].equals("")) {

                StringTokenizer strToken = new StringTokenizer(invCategory[ctr], "~", false);

                //header
                rows[ctr] = strToken.nextToken();

                while (strToken.hasMoreTokens()) {

                    String token = strToken.nextToken();

                    if (countSeparator(token) <= 5) {

                        rows[ctr] = rows[ctr] + "~" + token;

                    }

                }

            }
        }

        return rows;

    }

    private int countSeparator(String str) {

        int count = 0;

        for (int i = 0; i < str.length(); i++) {

            if (str.charAt(i) == '$') {
                count++;
            }

        }

        return count;
    }

    private ArrayList getCategories(ArrayList strArray, Integer AD_CMPNY) {

        Debug.print("InvReceivingSyncControllerBean getCategories");

        ArrayList invCategories = new ArrayList();

        Iterator i = strArray.iterator();

        while (i.hasNext()) {

            String str = (String) i.next();

            StringTokenizer strToken = new StringTokenizer(str, "~", false);

            // discard header

            strToken.nextToken();

            // loop through lines and add lvCategories

            while (strToken.hasMoreTokens()) {

                String line = (String) strToken.nextToken();

                String[] lineResults = this.receivingLineDecode(line);
                String PRODUCT = lineResults[2];

                LocalInvItem invItem = null;

                try {
                    invItem = invItemHome.findByIiName(PRODUCT, AD_CMPNY);
                }
                catch (FinderException ex) {
                }

                if (!invCategories.contains(invItem.getIiAdLvCategory())) {
                    invCategories.add(invItem.getIiAdLvCategory());
                }
            }

        }

        return invCategories;

    }

    private String generateDocumentNumber(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("InvReceivingSyncControllerBean generateDocumentNumber");

        try {

            String DOC_NUM = null;

            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

            try {

                adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("INV ADJUSTMENT", AD_CMPNY);

            }
            catch (FinderException ex) {

            }

            try {

                adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome
                        .findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, AD_CMPNY);

            }
            catch (FinderException ex) {

            }

            if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A') {

                while (true) {

                    if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                        try {

                            invAdjustmentHome.findByAdjDocumentNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, AD_CMPNY);
                            adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                        }
                        catch (FinderException ex) {

                            DOC_NUM = adDocumentSequenceAssignment.getDsaNextSequence();
                            adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                            break;

                        }

                    } else {

                        try {

                            invAdjustmentHome.findByAdjDocumentNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, AD_CMPNY);
                            adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                        }
                        catch (FinderException ex) {

                            DOC_NUM = adBranchDocumentSequenceAssignment.getBdsNextSequence();
                            adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                            break;

                        }

                    }

                }

            }

            return DOC_NUM;

        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalInvAdjustmentLine addInvAlEntry(InvModAdjustmentLineDetails mdetails,
                                                 LocalInvAdjustment invAdjustment, Integer AD_CMPNY) {

        Debug.print("InvReceivingSyncControllerBean addInvAlEntry");

        try {

            LocalInvAdjustmentLine invAdjustmentLine = invAdjustmentLineHome.create(
                    mdetails.getAlUnitCost(), null, null, mdetails.getAlAdjustQuantity(), 0, EJBCommon.FALSE, AD_CMPNY);

            invAdjustment.addInvAdjustmentLine(invAdjustmentLine);

            LocalInvUnitOfMeasure invUnitOfMeasure =
                    invUnitOfMeasureHome.findByUomName(mdetails.getAlUomName(), AD_CMPNY);
            invUnitOfMeasure.addInvAdjustmentLine(invAdjustmentLine);

            LocalInvItemLocation invItemLocation =
                    invItemLocationHome.findByLocNameAndIiName(mdetails.getAlLocName(),
                            mdetails.getAlIiName(), AD_CMPNY);
            invItemLocation.addInvAdjustmentLine(invAdjustmentLine);

            return invAdjustmentLine;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }

    private void createDistributionRecords(LocalInvAdjustment invAdjustment, Date DATE_RECEIVED, Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("InvReceivingSyncControllerBean createDistributionRecords");

        try {

            Collection invAdjustmentLines = invAdjustment.getInvAdjustmentLines();

            Iterator i = invAdjustmentLines.iterator();
            byte DEBIT = EJBCommon.FALSE;
            double TOTAL_AMOUNT = 0d;
            double ABS_TOTAL_AMOUNT = 0d;

            while (i.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();

                // add physical inventory distribution

                double AMOUNT = 0d;

                if (invAdjustmentLine.getAlAdjustQuantity() > 0) {

                    AMOUNT = EJBCommon.roundIt(
                            invAdjustmentLine.getAlAdjustQuantity() * invAdjustmentLine.getAlUnitCost(),
                            commonData.getGlFcPrecisionUnit(AD_CMPNY));
                    DEBIT = EJBCommon.TRUE;

                } else {

                    double COST = 0d;

                    try {

                        LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(invAdjustment.getAdjDate(), invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(),
                                invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName(), AD_BRNCH, AD_CMPNY);

                        COST = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                    }
                    catch (FinderException ex) {

                        COST = invAdjustmentLine.getInvItemLocation().getInvItem().getIiUnitCost();

                    }

                    AMOUNT = EJBCommon.roundIt(
                            invAdjustmentLine.getAlAdjustQuantity() * COST,
                            commonData.getGlFcPrecisionUnit(AD_CMPNY));

                    DEBIT = EJBCommon.FALSE;

                }

                //check for branch mapping

                LocalAdBranchItemLocation adBranchItemLocation = null;

                try {

                    adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(
                            invAdjustmentLine.getInvItemLocation().getIlCode(), AD_BRNCH, AD_CMPNY);

                }
                catch (FinderException ex) {

                }

                LocalGlChartOfAccount glInventoryChartOfAccount = null;

                if (adBranchItemLocation == null) {

                    glInventoryChartOfAccount = glChartOfAccountHome.findByPrimaryKey(
                            invAdjustmentLine.getInvItemLocation().getIlGlCoaInventoryAccount());

                } else {

                    glInventoryChartOfAccount = glChartOfAccountHome.findByPrimaryKey(
                            adBranchItemLocation.getBilCoaGlInventoryAccount());

                }

                this.addInvDrEntry(invAdjustment.getInvDrNextLine(), "INVENTORY", DEBIT, Math.abs(AMOUNT),
                        glInventoryChartOfAccount.getCoaCode(), invAdjustment, AD_BRNCH, AD_CMPNY);

                TOTAL_AMOUNT += AMOUNT;
                ABS_TOTAL_AMOUNT += Math.abs(AMOUNT);

                // add adjust quantity to item location committed quantity if negative

                if (invAdjustmentLine.getAlAdjustQuantity() < 0) {

                    double convertedQuantity = commonData.convertByUomFromAndUomToAndQuantity(invAdjustmentLine.getInvUnitOfMeasure(), invAdjustmentLine.getInvItemLocation().getInvItem().getInvUnitOfMeasure(), Math.abs(invAdjustmentLine.getAlAdjustQuantity()), AD_CMPNY);

                    LocalInvItemLocation invItemLocation = invAdjustmentLine.getInvItemLocation();

                    invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() + convertedQuantity);

                }

            }

            // add variance or transfer/debit distribution

            DEBIT = TOTAL_AMOUNT > 0 ? EJBCommon.FALSE : EJBCommon.TRUE;

            this.addInvDrEntry(invAdjustment.getInvDrNextLine(), "ADJUSTMENT", DEBIT, Math.abs(TOTAL_AMOUNT),
                    invAdjustment.getGlChartOfAccount().getCoaCode(), invAdjustment, AD_BRNCH, AD_CMPNY);

        }
        catch (GlobalBranchAccountNumberInvalidException ex) {

            throw new GlobalBranchAccountNumberInvalidException();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }

    private Integer executeInvVarianceGeneration(Integer PI_CODE, String VRNC_ACCNT, String USR_NM, Integer AD_BRNCH, Integer AD_CMPNY) throws
            GlobalBranchAccountNumberInvalidException {

        Debug.print("InvReceivingSyncControllerBean executeInvVarianceGeneration");

        LocalInvPhysicalInventory invPhysicalInventory = null;
        LocalInvCosting invCosting = null;

        ArrayList list = new ArrayList();

        try {

            // 	auto save physical inventory

            try {

                invPhysicalInventory = invPhysicalInventoryHome.findByPrimaryKey(PI_CODE);

            }
            catch (FinderException ex) {

            }

            // check if physical inventory has variance

            Collection invPhysicalInventoryLines = invPhysicalInventory.getInvPhysicalInventoryLines();

            double TOTAL_VARIANCE = 0d;

            Iterator i = invPhysicalInventoryLines.iterator();

            while (i.hasNext()) {

                LocalInvPhysicalInventoryLine invPhysicalInventoryLine = (LocalInvPhysicalInventoryLine) i.next();

                TOTAL_VARIANCE += invPhysicalInventoryLine.getPilVariance();

            }

            if (TOTAL_VARIANCE == 0d) {

                // set variance adjusted to true

                invPhysicalInventory.setPiVarianceAdjusted(EJBCommon.TRUE);

                return null;

            }

            // generate adjustment as VARIANCE

            LocalInvAdjustment invAdjustment = invAdjustmentHome.create(invPhysicalInventory.getPiReferenceNumber(),
                    invPhysicalInventory.getPiReferenceNumber(), null, invPhysicalInventory.getPiDate(), "VARIANCE", null,
                    EJBCommon.FALSE, USR_NM, invPhysicalInventory.getPiDate(), USR_NM, invPhysicalInventory.getPiDate(), null, null,
                    null, null, null, null, EJBCommon.FALSE, EJBCommon.FALSE, AD_BRNCH, AD_CMPNY);

            try {

                LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(VRNC_ACCNT, AD_BRNCH, AD_CMPNY);
                glChartOfAccount.addInvAdjustment(invAdjustment);

            }
            catch (FinderException ex) {

            }

            LocalGlChartOfAccount glPosAdjustmentAccount = null;

            try {

                glPosAdjustmentAccount = glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(VRNC_ACCNT, AD_BRNCH, AD_CMPNY);
                glPosAdjustmentAccount.addInvAdjustment(invAdjustment);

            }
            catch (FinderException ex) {

            }

            if (glPosAdjustmentAccount == null) {

                try {

                    glPosAdjustmentAccount = glChartOfAccountHome.findByCoaAccountNumber(VRNC_ACCNT, AD_CMPNY);
                    glPosAdjustmentAccount.addInvAdjustment(invAdjustment);

                }
                catch (FinderException ex) {

                }

            }

            double TOTAL_ADJUSTMENT_AMOUNT = 0d;
            byte DEBIT = 0;

            i = invPhysicalInventoryLines.iterator();

            while (i.hasNext()) {

                LocalInvPhysicalInventoryLine invPhysicalInventoryLine = (LocalInvPhysicalInventoryLine) i.next();

                if (invPhysicalInventoryLine.getPilVariance() == 0d) {
                    continue;
                }

                // generate inventory adjustment line

                InvModAdjustmentLineDetails mdetails = new InvModAdjustmentLineDetails();
                mdetails.setAlAdjustQuantity(invPhysicalInventoryLine.getPilEndingInventory());
                mdetails.setAlUnitCost(invPhysicalInventoryLine.getInvItemLocation().getInvItem().getIiUnitCost());
                mdetails.setAlUomName(invPhysicalInventoryLine.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName());
                mdetails.setAlLocName(invPhysicalInventoryLine.getInvItemLocation().getInvLocation().getLocName());
                mdetails.setAlIiName(invPhysicalInventoryLine.getInvItemLocation().getInvItem().getIiName());

                LocalInvAdjustmentLine invAdjustmentLine = this.addInvAlEntry(mdetails, invAdjustment, AD_CMPNY);

                // generate inventory adjustment distribution record

                double TOTAL_AMOUNT = 0d;
                double AMOUNT = 0d;
                double COST = 0d;

                try {

                    invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(invPhysicalInventory.getPiDate(), invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(),
                            invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName(), AD_BRNCH, AD_CMPNY);

                    COST = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                }
                catch (FinderException ex) {

                    COST = invAdjustmentLine.getInvItemLocation().getInvItem().getIiUnitCost();

                }

                AMOUNT = EJBCommon.roundIt(
                        invAdjustmentLine.getAlAdjustQuantity() * COST,
                        commonData.getGlFcPrecisionUnit(AD_CMPNY));

                DEBIT = AMOUNT > 0 ? EJBCommon.TRUE : EJBCommon.FALSE;

                // check for branch mapping

                LocalAdBranchItemLocation adBranchItemLocation = null;

                try {

                    adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(
                            invAdjustmentLine.getInvItemLocation().getIlCode(), AD_BRNCH, AD_CMPNY);

                }
                catch (FinderException ex) {

                }

                LocalGlChartOfAccount glInventoryChartOfAccount = null;

                if (adBranchItemLocation == null) {

                    glInventoryChartOfAccount = glChartOfAccountHome.findByPrimaryKey(
                            invAdjustmentLine.getInvItemLocation().getIlGlCoaInventoryAccount());

                } else {

                    glInventoryChartOfAccount = glChartOfAccountHome.findByPrimaryKey(
                            adBranchItemLocation.getBilCoaGlInventoryAccount());

                }


                this.addInvDrEntry(invAdjustment.getInvDrNextLine(), "INVENTORY", DEBIT, Math.abs(AMOUNT),
                        glInventoryChartOfAccount.getCoaCode(), invAdjustment, AD_BRNCH, AD_CMPNY);

                TOTAL_AMOUNT += AMOUNT;

                // add adjust quantity to item location committed quantity

                if (invAdjustmentLine.getAlAdjustQuantity() < 0) {

                    double convertedQuantity = commonData.convertByUomFromAndUomToAndQuantity(invAdjustmentLine.getInvUnitOfMeasure(), invAdjustmentLine.getInvItemLocation().getInvItem().getInvUnitOfMeasure(), Math.abs(invAdjustmentLine.getAlAdjustQuantity()), AD_CMPNY);

                    LocalInvItemLocation invItemLocation = invAdjustmentLine.getInvItemLocation();

                    invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() + convertedQuantity);

                }

                TOTAL_ADJUSTMENT_AMOUNT += TOTAL_AMOUNT;

            }

            // add adjustment distribution

            DEBIT = TOTAL_ADJUSTMENT_AMOUNT > 0 ? EJBCommon.FALSE : EJBCommon.TRUE;

            this.addInvDrEntry(invAdjustment.getInvDrNextLine(), "ADJUSTMENT", DEBIT, Math.abs(TOTAL_ADJUSTMENT_AMOUNT),
                    invAdjustment.getGlChartOfAccount().getCoaCode(), invAdjustment, AD_BRNCH, AD_CMPNY);


            // set variance adjusted to true

            invPhysicalInventory.setPiVarianceAdjusted(EJBCommon.TRUE);

            return invAdjustment.getAdjCode();

        }
        catch (GlobalBranchAccountNumberInvalidException ex) {

            throw new GlobalBranchAccountNumberInvalidException();

        }
        catch (Exception ex) {

            ctx.setRollbackOnly();
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }


    private void addInvDrEntry(short DR_LN, String DR_CLSS,
                               byte DR_DBT, double DR_AMNT, Integer COA_CODE, LocalInvAdjustment invAdjustment, Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("InvReceivingSyncControllerBean addInvDrEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            // validate coa

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, AD_BRNCH, AD_CMPNY);

            }
            catch (FinderException ex) {

            }


            if (glChartOfAccount == null) {

                try {

                    glChartOfAccount = glChartOfAccountHome.findByCoaCode(COA_CODE, AD_CMPNY);

                }
                catch (FinderException ex) {

                    throw new GlobalBranchAccountNumberInvalidException();

                }

            }

            // create distribution record

            LocalInvDistributionRecord invDistributionRecord = invDistributionRecordHome.create(
                    DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()),
                    EJBCommon.FALSE, EJBCommon.FALSE, AD_CMPNY);

            invAdjustment.addInvDistributionRecord(invDistributionRecord);
            glChartOfAccount.addInvDistributionRecord(invDistributionRecord);

        }
        catch (GlobalBranchAccountNumberInvalidException ex) {

            throw new GlobalBranchAccountNumberInvalidException();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }

}