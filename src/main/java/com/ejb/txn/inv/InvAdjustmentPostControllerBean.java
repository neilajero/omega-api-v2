/**
 * @copyright 2022 Omega Business Consulting, Inc.
 * @class InvAdjustmentPostControllerBean
 * @created August 09 2004, 11:14 AM
 * @author Enrico C. Yap
 */
package com.ejb.txn.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.ad.LocalAdBranchItemLocationHome;
import com.ejb.dao.gl.*;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.LocalAdBranchItemLocation;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.gl.*;
import com.ejb.entities.inv.*;
import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.ejb.txnreports.inv.InvRepItemCostingController;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.ad.AdBranchDetails;
import com.util.mod.inv.InvModAdjustmentDetails;

import jakarta.ejb.*;
import javax.naming.NamingException;
import java.util.*;

@Stateless(name = "InvAdjustmentPostControllerEJB")
public class InvAdjustmentPostControllerBean extends EJBContextClass
        implements InvAdjustmentPostController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    public InvRepItemCostingController ejbRIC;
    double CostA = 0d;
    double CostB = 0d;
    double UnitA = 0d;
    double UnitB = 0d;
    double[] CostAmount = new double[10];
    double[] UnitValue = new double[10];
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private ILocalGlSetOfBookHome glSetOfBookHome;
    @EJB
    private ILocalGlAccountingCalendarValueHome glAccountingCalendarValueHome;
    @EJB
    private ILocalGlChartOfAccountBalanceHome glChartOfAccountBalanceHome;
    @EJB
    private LocalInvAdjustmentHome invAdjustmentHome;
    @EJB
    private LocalGlJournalHome glJournalHome;
    @EJB
    private LocalGlJournalBatchHome glJournalBatchHome;
    @EJB
    private LocalGlSuspenseAccountHome glSuspenseAccountHome;
    @EJB
    private LocalGlJournalLineHome glJournalLineHome;
    @EJB
    private LocalGlJournalSourceHome glJournalSourceHome;
    @EJB
    private LocalGlJournalCategoryHome glJournalCategoryHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalInvDistributionRecordHome invDistributionRecordHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalInvAdjustmentLineHome invAdjustmentLineHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalInvItemHome invItemHome;

    public ArrayList getInvAdjPostableByCriteria(HashMap criteria, Integer OFFSET, Integer LIMIT, String ORDER_BY,
                                                 Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("InvAdjustmentPostControllerBean getInvAdjPostableByCriteria");
        ArrayList list = new ArrayList();
        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(adj) FROM InvAdjustment adj ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("referenceNumber")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("referenceNumber")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("adj.adjReferenceNumber LIKE '%").append(criteria.get("referenceNumber")).append("%' ");
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

            if (criteria.containsKey("approvalStatus")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("adj.adjApprovalStatus=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("approvalStatus");
                ctr++;

            } else {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("(adj.adjApprovalStatus='APPROVED' OR adj.adjApprovalStatus='N/A') ");
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("adj.adjPosted = 0 AND adj.adjAdBranch=").append(branchCode).append(" AND adj.adjAdCompany=").append(companyCode).append(" ");

            String orderBy = null;

            if (ORDER_BY.equals("REFERENCE NUMBER")) {

                orderBy = "adj.adjReferenceNumber";

            } else if (ORDER_BY.equals("DOCUMENT NUMBER")) {

                orderBy = "adj.adjDocumentNumber";
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy).append(", adj.adjDate");

            } else {

                jbossQl.append("ORDER BY adj.adjDate, adj.adjReferenceNumber, adj.adjDocumentNumber");
            }

            Collection invAdjustments = invAdjustmentHome.getAdjByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

            if (invAdjustments.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            for (Object adjustment : invAdjustments) {

                LocalInvAdjustment invAdjustment = (LocalInvAdjustment) adjustment;

                InvModAdjustmentDetails mdetails = new InvModAdjustmentDetails();
                mdetails.setAdjCode(invAdjustment.getAdjCode());
                mdetails.setAdjDate(invAdjustment.getAdjDate());
                mdetails.setAdjDocumentNumber(invAdjustment.getAdjDocumentNumber());
                mdetails.setAdjReferenceNumber(invAdjustment.getAdjReferenceNumber());

                LocalInvDistributionRecord invDistributionRecord = null;

                try {
                    invDistributionRecord = invDistributionRecordHome.findByDrClassAndAdjCode("ADJUSTMENT",
                            invAdjustment.getAdjCode(), companyCode);
                    mdetails.setAdjAmount(invDistributionRecord.getDrAmount());
                }
                catch (FinderException ex) {

                }

                list.add(mdetails);
            }

            return list;

        }
        catch (GlobalNoRecordFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public void executeInvAdjPost(Integer ADJ_CODE, String USR_NM, Integer branchCode, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException,
            GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException,
            GlobalJournalNotBalanceException, GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException,
            GlobalAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException,
            GlobalExpiryDateNotFoundException, GlobalRecordInvalidException {

        Debug.print("InvAdjustmentPostControllerBean executeInvAdjPost");
        try {

            // validate if adjustment is already deleted

            LocalInvAdjustment invAdjustment = null;

            try {

                invAdjustment = invAdjustmentHome.findByPrimaryKey(ADJ_CODE);

            }
            catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if adjustment is already posted or void

            if (invAdjustment.getAdjPosted() == EJBCommon.TRUE) {

                if (invAdjustment.getAdjVoid() != EJBCommon.TRUE) {
                    throw new GlobalTransactionAlreadyPostedException();
                }
            }

            // regenerate inventory dr

            this.regenerateInventoryDr(invAdjustment, branchCode, companyCode);

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            boolean hasInsufficientItems = false;
            StringBuilder insufficientItems = new StringBuilder();

            Collection invAdjustmentLines = null;

            if (invAdjustment.getAdjVoid() == EJBCommon.FALSE) {
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.FALSE,
                        invAdjustment.getAdjCode(), companyCode);
            } else {
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.TRUE,
                        invAdjustment.getAdjCode(), companyCode);
            }

            Iterator c2 = invAdjustmentLines.iterator();

            int line = 1;

            while (c2.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) c2.next();

                if (invAdjustmentLine.getInvAdjustment().getAdjType().equals("ISSUANCE")) {

                    LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(
                            invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(),
                            invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName(), companyCode);

                    double zxc = this.getInvFifoCost2(invAdjustment.getAdjDate(), invItemLocation.getIlCode(),
                            invAdjustmentLine.getAlAdjustQuantity(), false, branchCode, companyCode);

                    System.out.print("ISSUANCE");
                    if (zxc <= 1) {

                        System.out.print("NO LAYER");
                        System.out.print("SAVE REQUEST2");

                    } else {

                        zxc--;
                        System.out.print("IS LAYER");
                        System.out.print("SAVE REQUEST2");

                        invAdjustmentLine.setAlAdjustQuantity(UnitValue[0] * -1);
                        invAdjustmentLine.setAlUnitCost(CostAmount[0] / UnitValue[0]);

                        int indexValue = 1;
                        while (zxc > 0) {
                            System.out.print("SAVE REQUEST2");
                            LocalInvAdjustmentLine invAdjustmentLine2 = invAdjustmentLineHome.create(
                                    CostAmount[indexValue] / UnitValue[indexValue], invAdjustmentLine.getAlQcNumber(),
                                    invAdjustmentLine.getAlQcExpiryDate(), UnitValue[indexValue] * -1, 0, (byte) 0,
                                    companyCode);
                            invAdjustmentLine2.setInvAdjustment(invAdjustmentLine.getInvAdjustment());
                            invAdjustmentLine2.setInvItemLocation(invAdjustmentLine.getInvItemLocation());
                            invAdjustmentLine2.setInvUnitOfMeasure(invAdjustmentLine.getInvUnitOfMeasure());
                            zxc--;
                            indexValue++;
                        }
                    }

                } else {
                    System.out.print("NOT ISSUANCE");
                }
            }
            Collection invAdjustmentLines2 = null;
            if (invAdjustment.getAdjVoid() == EJBCommon.FALSE) {
                invAdjustmentLines2 = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.FALSE,
                        invAdjustment.getAdjCode(), companyCode);
            } else {
                invAdjustmentLines2 = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.TRUE,
                        invAdjustment.getAdjCode(), companyCode);
            }
            Debug.print("*********************************************invAdjustmentLines2.size(): "
                    + invAdjustmentLines2.size());
            for (Object o : invAdjustmentLines2) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) o;

                // INSUFFICIENT STOCKS

                if (invAdjustmentLine.getAlAdjustQuantity() < 0
                        && adPreference.getPrfArCheckInsufficientStock() == EJBCommon.TRUE) {

                    double CURR_QTY = 0;
                    double ILI_QTY = this.convertByUomAndQuantity(invAdjustmentLine.getInvUnitOfMeasure(),
                            invAdjustmentLine.getInvItemLocation().getInvItem(),
                            Math.abs(invAdjustmentLine.getAlAdjustQuantity()), companyCode);

                    LocalInvCosting invCosting = null;

                    try {

                        invCosting = invCostingHome
                                .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                        invAdjustment.getAdjDate(),
                                        invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(),
                                        invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName(), branchCode,
                                        companyCode);

                    }
                    catch (FinderException ex) {

                    }

                    double LOWEST_QTY = this.convertByUomAndQuantity(invAdjustmentLine.getInvUnitOfMeasure(),
                            invAdjustmentLine.getInvItemLocation().getInvItem(), 1, companyCode);

                    if (invCosting != null) {

                        CURR_QTY = this.convertByUomAndQuantity(invAdjustmentLine.getInvUnitOfMeasure(),
                                invAdjustmentLine.getInvItemLocation().getInvItem(),
                                Math.abs(invCosting.getCstRemainingQuantity()), companyCode);
                    }

                    if (invCosting == null || CURR_QTY == 0 || CURR_QTY - ILI_QTY <= -LOWEST_QTY) {

                        hasInsufficientItems = true;

                        insufficientItems.append(invAdjustmentLine.getInvItemLocation().getInvItem().getIiName()).append(", ");
                    }
                }

                if (hasInsufficientItems) {

                    throw new GlobalRecordInvalidException(
                            insufficientItems.substring(0, insufficientItems.lastIndexOf(",")));
                }

                String II_NM = invAdjustmentLine.getInvItemLocation().getInvItem().getIiName();
                String LOC_NM = invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName();

                double ADJUST_QTY = this.convertByUomFromAndItemAndQuantity(invAdjustmentLine.getInvUnitOfMeasure(),
                        invAdjustmentLine.getInvItemLocation().getInvItem(), invAdjustmentLine.getAlAdjustQuantity(),
                        companyCode);

                LocalInvCosting invCosting = null;

                try {

                    invCosting = invCostingHome
                            .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                    invAdjustment.getAdjDate(), II_NM, LOC_NM, branchCode, companyCode);

                }
                catch (FinderException ex) {

                }

                double COST = invAdjustmentLine.getAlUnitCost();

                if (invCosting == null) {

                    this.postToInv(invAdjustmentLine, invAdjustment.getAdjDate(), ADJUST_QTY, COST * ADJUST_QTY,
                            ADJUST_QTY, COST * ADJUST_QTY, 0d, null, branchCode, companyCode);

                } else {

                    this.postToInv(invAdjustmentLine, invAdjustment.getAdjDate(), ADJUST_QTY, COST * ADJUST_QTY,
                            invCosting.getCstRemainingQuantity() + ADJUST_QTY,
                            invCosting.getCstRemainingValue() + (COST * ADJUST_QTY), 0d, null, branchCode, companyCode);
                }
            }

            // post adjustment

            invAdjustment.setAdjPosted(EJBCommon.TRUE);
            invAdjustment.setAdjPostedBy(USR_NM);
            invAdjustment.setAdjDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            // post to gl if necessary

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            if (adPreference.getPrfInvGlPostingType().equals("USE SL POSTING")
                    || invAdjustment.getAdjIsCostVariance() == EJBCommon.TRUE) {

                // validate if date has no period and period is closed

                LocalGlSetOfBook glJournalSetOfBook = null;

                try {

                    glJournalSetOfBook = glSetOfBookHome.findByDate(invAdjustment.getAdjDate(), companyCode);

                }
                catch (FinderException ex) {

                    throw new GlJREffectiveDateNoPeriodExistException();
                }

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome
                        .findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(),
                                invAdjustment.getAdjDate(), companyCode);

                if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C'
                        || glAccountingCalendarValue.getAcvStatus() == 'P') {

                    throw new GlJREffectiveDatePeriodClosedException();
                }

                // check if invoice is balance if not check suspense posting

                LocalGlJournalLine glOffsetJournalLine = null;

                Collection invDistributionRecords = null;

                if (invAdjustment.getAdjVoid() == EJBCommon.FALSE) {

                    invDistributionRecords = invDistributionRecordHome.findImportableDrByDrReversedAndAdjCode(
                            EJBCommon.FALSE, invAdjustment.getAdjCode(), companyCode);

                } else {

                    invDistributionRecords = invDistributionRecordHome.findImportableDrByDrReversedAndAdjCode(
                            EJBCommon.TRUE, invAdjustment.getAdjCode(), companyCode);
                }

                Iterator j = invDistributionRecords.iterator();

                double TOTAL_DEBIT = 0d;
                double TOTAL_CREDIT = 0d;

                while (j.hasNext()) {

                    LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) j.next();

                    double DR_AMNT = 0d;

                    DR_AMNT = invDistributionRecord.getDrAmount();

                    if (invDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                        TOTAL_DEBIT += DR_AMNT;

                    } else {

                        TOTAL_CREDIT += DR_AMNT;
                        Debug.print(DR_AMNT + "-------------CRDT");
                    }
                }

                Debug.print("TOTAL_DEBIT------------>" + TOTAL_DEBIT);
                Debug.print("TOTAL_CREDIT------------>" + TOTAL_CREDIT);
                TOTAL_DEBIT = EJBCommon.roundIt(TOTAL_DEBIT, adCompany.getGlFunctionalCurrency().getFcPrecision());
                TOTAL_CREDIT = EJBCommon.roundIt(TOTAL_CREDIT, adCompany.getGlFunctionalCurrency().getFcPrecision());

                if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.TRUE && TOTAL_DEBIT != TOTAL_CREDIT) {

                    LocalGlSuspenseAccount glSuspenseAccount = null;

                    try {

                        glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("INVENTORY",
                                "INVENTORY ADJUSTMENTS", companyCode);

                    }
                    catch (FinderException ex) {
                        Debug.print("here: ");
                        throw new GlobalJournalNotBalanceException();
                    }

                    if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                        glOffsetJournalLine = glJournalLineHome.create((short) (invDistributionRecords.size() + 1),
                                EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);

                    } else {

                        glOffsetJournalLine = glJournalLineHome.create((short) (invDistributionRecords.size() + 1),
                                EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
                    }

                    LocalGlChartOfAccount glChartOfAccount = glSuspenseAccount.getGlChartOfAccount();

                    glOffsetJournalLine.setGlChartOfAccount(glChartOfAccount);

                } else if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE
                        && TOTAL_DEBIT != TOTAL_CREDIT) {

                    throw new GlobalJournalNotBalanceException();
                }

                // create journal batch if necessary

                LocalGlJournalBatch glJournalBatch = null;
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");

                try {

                    glJournalBatch = glJournalBatchHome.findByJbName(
                            "JOURNAL IMPORT " + formatter.format(new Date()) + " INVENTORY ADJUSTMENTS", branchCode,
                            companyCode);

                }
                catch (FinderException ex) {

                }

                if (glJournalBatch == null) {

                    glJournalBatch = glJournalBatchHome.create(
                            "JOURNAL IMPORT " + formatter.format(new Date()) + " INVENTORY ADJUSTMENTS",
                            "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, branchCode,
                            companyCode);
                }

                // create journal entry

                LocalGlJournal glJournal = glJournalHome.create(invAdjustment.getAdjReferenceNumber(),
                        invAdjustment.getAdjDescription(), invAdjustment.getAdjDate(), 0.0d, null,
                        invAdjustment.getAdjDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE,
                        EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM,
                        EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, EJBCommon.FALSE, null, branchCode,
                        companyCode);

                LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("INVENTORY", companyCode);
                glJournal.setGlJournalSource(glJournalSource);

                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome
                        .findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

                LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("INVENTORY ADJUSTMENTS",
                        companyCode);
                glJournal.setGlJournalCategory(glJournalCategory);

                if (glJournalBatch != null) {

                    glJournal.setGlJournalBatch(glJournalBatch);
                }

                // create journal lines

                j = invDistributionRecords.iterator();

                while (j.hasNext()) {

                    LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) j.next();

                    double DR_AMNT = 0d;

                    DR_AMNT = invDistributionRecord.getDrAmount();

                    LocalGlJournalLine glJournalLine = glJournalLineHome.create(invDistributionRecord.getDrLine(),
                            invDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

                    // invDistributionRecord.getInvChartOfAccount().addGlJournalLine(glJournalLine);
                    glJournalLine.setGlChartOfAccount(invDistributionRecord.getInvChartOfAccount());

                    // glJournal.addGlJournalLine(glJournalLine);
                    glJournalLine.setGlJournal(glJournal);

                    invDistributionRecord.setDrImported(EJBCommon.TRUE);
                }

                if (glOffsetJournalLine != null) {

                    // glJournal.addGlJournalLine(glOffsetJournalLine);
                    glOffsetJournalLine.setGlJournal(glJournal);
                }

                // post journal to gl
                Collection glJournalLines = glJournalLineHome.findByJrCode(glJournal.getJrCode(), companyCode);
                for (Object journalLine : glJournalLines) {

                    LocalGlJournalLine glJournalLine = (LocalGlJournalLine) journalLine;

                    // post current to current acv

                    this.postToGl(glAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), true,
                            glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                    // post to subsequent acvs (propagate)

                    Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome
                            .findSubsequentAcvByAcCodeAndAcvPeriodNumber(
                                    glJournalSetOfBook.getGlAccountingCalendar().getAcCode(),
                                    glAccountingCalendarValue.getAcvPeriodNumber(), companyCode);

                    for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {

                        LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;

                        this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false,
                                glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                    }

                    // post to subsequent years if necessary

                    Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(
                            glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode);

                    if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                        adCompany = adCompanyHome.findByPrimaryKey(companyCode);

                        LocalGlChartOfAccount glRetainedEarningsAccount = null;

                        try {

                            glRetainedEarningsAccount = glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(
                                    adCompany.getCmpRetainedEarnings(), branchCode, companyCode);

                        }
                        catch (FinderException ex) {

                            throw new GlobalAccountNumberInvalidException();
                        }

                        for (Object subsequentSetOfBook : glSubsequentSetOfBooks) {

                            LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) subsequentSetOfBook;

                            String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                            // post to subsequent acvs of subsequent set of book(propagate)

                            Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(
                                    glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                            for (Object accountingCalendarValue : glAccountingCalendarValues) {

                                LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                                if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY")
                                        || ACCOUNT_TYPE.equals("OWNERS EQUITY")) {

                                    this.postToGl(glSubsequentAccountingCalendarValue,
                                            glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(),
                                            glJournalLine.getJlAmount(), companyCode);

                                } else { // revenue & expense

                                    this.postToGl(glSubsequentAccountingCalendarValue, glRetainedEarningsAccount, false,
                                            glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                                }
                            }

                            if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) {
                                break;
                            }
                        }
                    }
                }
            }

        }
        catch (GlJREffectiveDateNoPeriodExistException | GlobalRecordInvalidException |
               GlobalExpiryDateNotFoundException | AdPRFCoaGlVarianceAccountNotFoundException |
               GlobalAccountNumberInvalidException | GlobalBranchAccountNumberInvalidException |
               GlobalInventoryDateException | GlobalTransactionAlreadyPostedException |
               GlobalRecordAlreadyDeletedException | GlobalJournalNotBalanceException |
               GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer companyCode) {

        Debug.print("InvAdjustmentPostControllerBean getGlFcPrecisionUnit");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods

    private double getInvFifoCost(Date CST_DT, Integer IL_CODE, double CST_QTY, double CST_COST, boolean isAdjustFifo,
                                  Integer branchCode, Integer companyCode) {

        Debug.print("InvAdjustmentPostControllerBean getInvFifoCost");
        try {

            Collection invFifoCostings = invCostingHome
                    .findFifoRemainingQuantityByLessThanOrEqualCstDateAndIlCodeAndBrCode(CST_DT, IL_CODE, branchCode,
                            companyCode);

            if (invFifoCostings.size() > 0) {
                double fifoCost = 0;
                double runningQty = Math.abs(CST_QTY);
                for (Object fifoCosting : invFifoCostings) {

                    LocalInvCosting invFifoCosting = (LocalInvCosting) fifoCosting;
                    LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
                    double newRemainingLifoQuantity = 0;
                    if (invFifoCosting.getCstRemainingLifoQuantity() <= runningQty) {
                        if (invFifoCosting.getApPurchaseOrderLine() != null
                                || invFifoCosting.getApVoucherLineItem() != null) {
                            fifoCost += invFifoCosting.getCstItemCost() / invFifoCosting.getCstQuantityReceived()
                                    * invFifoCosting.getCstRemainingLifoQuantity();
                        } else if (invFifoCosting.getArInvoiceLineItem() != null) {
                            fifoCost += invFifoCosting.getCstCostOfSales() / invFifoCosting.getCstQuantitySold()
                                    * invFifoCosting.getCstRemainingLifoQuantity();
                        } else {
                            fifoCost += invFifoCosting.getCstAdjustCost() / invFifoCosting.getCstAdjustQuantity()
                                    * invFifoCosting.getCstRemainingLifoQuantity();
                        }
                        runningQty = runningQty - invFifoCosting.getCstRemainingLifoQuantity();
                    } else {
                        if (invFifoCosting.getApPurchaseOrderLine() != null
                                || invFifoCosting.getApVoucherLineItem() != null) {
                            fifoCost += invFifoCosting.getCstItemCost() / invFifoCosting.getCstQuantityReceived()
                                    * runningQty;
                        } else if (invFifoCosting.getArInvoiceLineItem() != null) {
                            fifoCost += invFifoCosting.getCstCostOfSales() / invFifoCosting.getCstQuantitySold()
                                    * runningQty;
                        } else {
                            fifoCost += invFifoCosting.getCstAdjustCost() / invFifoCosting.getCstAdjustQuantity()
                                    * runningQty;
                        }
                        newRemainingLifoQuantity = invFifoCosting.getCstRemainingLifoQuantity() - runningQty;
                        runningQty = 0;
                    }
                    if (isAdjustFifo) {
                        invFifoCosting.setCstRemainingLifoQuantity(newRemainingLifoQuantity);
                    }
                    if (runningQty <= 0) {
                        break;
                    }
                }
                Debug.print("fifoCost" + fifoCost);
                Debug.print("CST_QTY" + CST_QTY);
                LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
                return EJBCommon.roundIt(fifoCost / CST_QTY, adPreference.getPrfInvCostPrecisionUnit());

            } else {

                // most applicable in 1st entries of data
                LocalInvItemLocation invItemLocation = invItemLocationHome.findByPrimaryKey(IL_CODE);
                return invItemLocation.getInvItem().getIiUnitCost();
            }

        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToInv(LocalInvAdjustmentLine invAdjustmentLine, Date CST_DT, double CST_ADJST_QTY,
                           double CST_ADJST_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, double CST_VRNC_VL, String USR_NM,
                           Integer branchCode, Integer companyCode)
            throws AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException {

        Debug.print("InvAdjustmentPostControllerBean postToInv");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalInvItemLocation invItemLocation = invAdjustmentLine.getInvItemLocation();
            int CST_LN_NMBR = 0;
            Debug.print(">>CST_ADJST_CST1: " + CST_ADJST_CST);
            CST_ADJST_QTY = EJBCommon.roundIt(CST_ADJST_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_ADJST_CST = EJBCommon.roundIt(CST_ADJST_CST, adPreference.getPrfInvCostPrecisionUnit());
            CST_RMNNG_QTY = EJBCommon.roundIt(CST_RMNNG_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_RMNNG_VL = EJBCommon.roundIt(CST_RMNNG_VL, adPreference.getPrfInvCostPrecisionUnit());
            Debug.print(">>CST_ADJST_CST: " + CST_ADJST_CST);

            if (CST_ADJST_QTY < 0) {

                invItemLocation
                        .setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - Math.abs(CST_ADJST_QTY));
            }

            try {
                // generate line number
                LocalInvCosting invCurrentCosting = invCostingHome
                        .getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(),
                                invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(),
                                branchCode, companyCode);

                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;
            }
            catch (FinderException ex) {
                try {
                    LocalInvCosting invCosting = invCostingHome.findByCstDateAndIiNameAndLocNameLimit1(CST_DT,
                            invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(),
                            invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName(), branchCode, companyCode);
                    CST_LN_NMBR = invCosting.getCstLineNumber() + 1;
                }
                catch (Exception e) {
                    CST_LN_NMBR = 1;
                }
            }

            String prevExpiryDates = "";
            String miscListPrpgt = "";
            double qtyPrpgt = 0;
            try {
                LocalInvCosting prevCst = invCostingHome
                        .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndRemainingQuantityNotEqualToZeroAndIlCode(
                                CST_DT, invItemLocation.getIlCode(), branchCode, companyCode);
                Debug.print(prevCst.getCstCode() + "   " + prevCst.getCstRemainingQuantity());
                prevExpiryDates = prevCst.getCstExpiryDate();
                qtyPrpgt = prevCst.getCstRemainingQuantity();
                if (prevExpiryDates == null) {
                    prevExpiryDates = "";
                }
                Debug.print("prevExpiryDates: " + prevExpiryDates);
                Debug.print("qtyPrpgt: " + qtyPrpgt);
            }
            catch (Exception ex) {
                Debug.print("prevExpiryDates CATCH: " + prevExpiryDates);
                prevExpiryDates = "";
            }

            // create costing

            // CST_ADJST_CST = invAdjustmentLine.getAlUnitCost() * CST_ADJST_QTY;
            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d,
                    CST_ADJST_QTY, CST_ADJST_CST, 0d, 0d, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, 0d, branchCode, companyCode);
            // invItemLocation.addInvCosting(invCosting);
            invCosting.setCstQCNumber(invAdjustmentLine.getAlQcNumber());
            invCosting.setCstQCExpiryDate(invAdjustmentLine.getAlQcExpiryDate());
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setInvAdjustmentLine(invAdjustmentLine);

            invCosting.setCstQuantity(CST_ADJST_QTY);
            invCosting.setCstCost(CST_ADJST_CST);

            Debug.print("CST_ADJST_QTY>: " + CST_ADJST_QTY);
            Debug.print("CST_ADJST_CST>: " + CST_ADJST_CST);
            // Get Latest Expiry Dates
            if (invAdjustmentLine.getInvItemLocation().getInvItem().getIiTraceMisc() != 0) {
                if (prevExpiryDates != null && prevExpiryDates != "" && prevExpiryDates.length() != 0) {

                    if (invAdjustmentLine.getAlMisc() != null && invAdjustmentLine.getAlMisc() != ""
                            && invAdjustmentLine.getAlMisc().length() != 0) {
                        int qty2Prpgt = Integer.parseInt(this.getQuantityExpiryDates(invAdjustmentLine.getAlMisc()));

                        String miscList2Prpgt = this.propagateExpiryDates(invAdjustmentLine.getAlMisc(), qty2Prpgt,
                                "False");
                        ArrayList miscList = this.expiryDates(invAdjustmentLine.getAlMisc(), qty2Prpgt);
                        String propagateMiscPrpgt = "";
                        StringBuilder ret = new StringBuilder();
                        StringBuilder exp = new StringBuilder();
                        String Checker = "";

                        // ArrayList miscList2 = null;
                        if (CST_ADJST_QTY > 0) {
                            prevExpiryDates = prevExpiryDates.substring(1);
                            propagateMiscPrpgt = miscList2Prpgt + prevExpiryDates;
                        } else {
                            Iterator mi = miscList.iterator();

                            propagateMiscPrpgt = prevExpiryDates;
                            ret = new StringBuilder(propagateMiscPrpgt);
                            while (mi.hasNext()) {
                                String miscStr = (String) mi.next();

                                int qTest = checkExpiryDates(ret + "fin$");
                                ArrayList miscList2 = this.expiryDates("$" + ret, Double.parseDouble(Integer.toString(qTest)));

                                // ArrayList miscList2 = this.expiryDates("$" + ret, qtyPrpgt);
                                Iterator m2 = miscList2.iterator();
                                ret = new StringBuilder();
                                String ret2 = "false";
                                int a = 0;
                                while (m2.hasNext()) {
                                    String miscStr2 = (String) m2.next();

                                    if (ret2 == "1st") {
                                        ret2 = "false";
                                    }
                                    Debug.print("miscStr1: " + miscStr.toUpperCase());
                                    Debug.print("miscStr2: " + miscStr2);

                                    if (miscStr2.toUpperCase().trim().equals(miscStr.toUpperCase().trim())) {
                                        if (a == 0) {
                                            a = 1;
                                            ret2 = "1st";
                                            Checker = "true";
                                        } else {
                                            a = a + 1;
                                            ret2 = "true";
                                        }
                                    }
                                    Debug.print("Checker: " + Checker);
                                    Debug.print("ret2: " + ret2);
                                    if (!miscStr2.toUpperCase().trim().equals(miscStr.toUpperCase().trim()) || a > 1) {
                                        if ((ret2 != "1st") && ((ret2 == "false") || (ret2 == "true"))) {
                                            if (miscStr2 != "") {
                                                miscStr2 = "$" + miscStr2;
                                                ret.append(miscStr2);
                                                Debug.print("ret " + ret);
                                                ret2 = "false";
                                            }
                                        }
                                    }
                                }
                                if (ret.toString() != "") {
                                    ret.append("$");
                                }
                                Debug.print("ret una: " + ret);
                                exp.append(ret);
                                qtyPrpgt = qtyPrpgt - 1;
                            }
                            Debug.print("ret fin " + ret);
                            Debug.print("exp fin " + exp);
                            // propagateMiscPrpgt = propagateMiscPrpgt.replace(" ", "$");
                            propagateMiscPrpgt = ret.toString();
                            if (Checker == "true") {
                                // invCosting.setCstExpiryDate(propagateMiscPrpgt);
                            } else {
                                Debug.print("Exp Not Found");
                                throw new GlobalExpiryDateNotFoundException(
                                        invAdjustmentLine.getInvItemLocation().getInvItem().getIiName());
                            }
                        }
                        invCosting.setCstExpiryDate(propagateMiscPrpgt);

                    } else {
                        invCosting.setCstExpiryDate(prevExpiryDates);
                    }

                } else {
                    Debug.print("invAdjustmentLine ETO NA: " + invAdjustmentLine.getAlAdjustQuantity());
                    if (invAdjustmentLine.getAlAdjustQuantity() > 0) {
                        if (invAdjustmentLine.getAlMisc() != null && invAdjustmentLine.getAlMisc() != ""
                                && invAdjustmentLine.getAlMisc().length() != 0) {
                            int initialQty = Integer
                                    .parseInt(this.getQuantityExpiryDates(invAdjustmentLine.getAlMisc()));
                            String initialPrpgt = this.propagateExpiryDates(invAdjustmentLine.getAlMisc(), initialQty,
                                    "False");

                            invCosting.setCstExpiryDate(initialPrpgt);
                        } else {
                            invCosting.setCstExpiryDate(prevExpiryDates);
                        }
                    } else {
                        invCosting.setCstExpiryDate(prevExpiryDates);
                    }
                }
            }

            // if cost variance is not 0, generate cost variance for the transaction

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT,
                    invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode,
                    companyCode);

            String miscList = "";
            ArrayList miscList2 = null;
            Iterator i = invCostings.iterator();

            String propagateMisc = "";
            StringBuilder ret = new StringBuilder();
            // String Checker = "";

            double PREV_QTY = invCosting.getCstRemainingQuantity();
            double PREV_RMNG_VL = invCosting.getCstRemainingValue();
            double PREV_CST = 0;
            if (invCosting.getCstRemainingQuantity() != 0) {
                PREV_CST = invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity();
            }

            while (i.hasNext()) {
                String Checker = "";
                String Checker2 = "";

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) i.next();

                if (invAdjustmentLine.getInvItemLocation().getInvItem().getIiTraceMisc() != 0) {
                    if (invAdjustmentLine.getAlMisc() != null && invAdjustmentLine.getAlMisc() != ""
                            && invAdjustmentLine.getAlMisc().length() != 0) {
                        double qty = Double.parseDouble(this.getQuantityExpiryDates(invAdjustmentLine.getAlMisc()));
                        // invPropagatedCosting.getInvAdjustmentLine().getAlMisc();
                        miscList = this.propagateExpiryDates(invAdjustmentLine.getAlMisc(), qty, "False");
                        miscList2 = this.expiryDates(invAdjustmentLine.getAlMisc(), qty);
                        Debug.print("invAdjustmentLine.getAlMisc(): " + invAdjustmentLine.getAlMisc());
                        Debug.print("getAlAdjustQuantity(): " + invAdjustmentLine.getAlAdjustQuantity());

                        if (invAdjustmentLine.getAlAdjustQuantity() < 0) {
                            Iterator mi = miscList2.iterator();
                            propagateMisc = invPropagatedCosting.getCstExpiryDate();
                            ret = new StringBuilder(invPropagatedCosting.getCstExpiryDate());
                            while (mi.hasNext()) {
                                String miscStr = (String) mi.next();

                                int qTest = checkExpiryDates(ret + "fin$");
                                ArrayList miscList3 = this.expiryDates("$" + ret, Double.parseDouble(Integer.toString(qTest)));

                                // ArrayList miscList3 = this.expiryDates("$" + ret, qtyPrpgt);
                                Debug.print("ret: " + ret);
                                Iterator m2 = miscList3.iterator();
                                ret = new StringBuilder();
                                String ret2 = "false";
                                int a = 0;
                                while (m2.hasNext()) {
                                    String miscStr2 = (String) m2.next();

                                    if (ret2 == "1st") {
                                        ret2 = "false";
                                    }
                                    Debug.print("2 miscStr: " + miscStr);
                                    Debug.print("2 miscStr2: " + miscStr2);
                                    if (miscStr2.equals(miscStr)) {
                                        if (a == 0) {
                                            a = 1;
                                            ret2 = "1st";
                                            Checker2 = "true";
                                        } else {
                                            a = a + 1;
                                            ret2 = "true";
                                        }
                                    }
                                    Debug.print("Checker: " + Checker2);
                                    if (!miscStr2.equals(miscStr) || a > 1) {
                                        if ((ret2 != "1st") || (ret2 == "false") || (ret2 == "true")) {
                                            if (miscStr2 != "") {
                                                miscStr2 = "$" + miscStr2;
                                                ret.append(miscStr2);
                                                ret2 = "false";
                                            }
                                        }
                                    }
                                }
                                if (Checker2 != "true") {
                                } else {

                                }

                                ret.append("$");
                                qtyPrpgt = qtyPrpgt - 1;
                            }
                        }
                    }
                }

                Debug.print("2invPropagatedCosting.getCstRemainingQuantity(): "
                        + invPropagatedCosting.getCstRemainingQuantity());
                Debug.print("2CST_ADJST_QTY(): " + CST_ADJST_QTY);

                if (CST_ADJST_QTY < 0) {
                    invPropagatedCosting
                            .setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_ADJST_QTY);
                    invPropagatedCosting
                            .setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_ADJST_CST);
                } else {

                    invPropagatedCosting.setCstRemainingLifoQuantity(
                            invPropagatedCosting.getCstRemainingLifoQuantity() + CST_ADJST_QTY);

                    invPropagatedCosting
                            .setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_ADJST_QTY);
                    // fix ave cost computation for negative inventory and cost of sales only
                    // CST_ASSMBLY_CST = previous remaining value - new ave cost total

                    if (invPropagatedCosting.getCstAdjustQuantity() < 0) {
                        double prevTxnCost = invPropagatedCosting.getCstAdjustCost();
                        invPropagatedCosting.setCstAdjustCost(PREV_CST * invPropagatedCosting.getCstAdjustQuantity());
                        invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue()
                                + CST_ADJST_CST - Math.abs(prevTxnCost - invPropagatedCosting.getCstAdjustCost()));

                    } else if (invPropagatedCosting.getCstQuantitySold() > 0) {
                        double prevTxnCost = invPropagatedCosting.getCstCostOfSales();
                        invPropagatedCosting.setCstCostOfSales(PREV_CST * invPropagatedCosting.getCstQuantitySold());
                        invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue()
                                + CST_ADJST_CST - Math.abs(prevTxnCost - invPropagatedCosting.getCstCostOfSales()));
                    } else {
                        invPropagatedCosting
                                .setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_ADJST_CST);
                    }

                    PREV_QTY = invPropagatedCosting.getCstRemainingQuantity();
                    PREV_RMNG_VL = invPropagatedCosting.getCstRemainingValue();
                    if (PREV_QTY != 0) {
                        PREV_CST = invPropagatedCosting.getCstRemainingValue()
                                / invPropagatedCosting.getCstRemainingQuantity();
                    }
                }

                if (invAdjustmentLine.getInvItemLocation().getInvItem().getIiTraceMisc() != 0) {
                    Debug.print("getAlAdjustQuantity(): " + invAdjustmentLine.getAlAdjustQuantity());
                    if (invAdjustmentLine.getAlAdjustQuantity() < 0 && invAdjustmentLine.getAlMisc() != ""
                            && invAdjustmentLine.getAlMisc() != null && !invAdjustmentLine.getAlMisc().equals("")) {

                        Iterator mi = miscList2.iterator();

                        propagateMisc = invPropagatedCosting.getCstExpiryDate();
                        ret = new StringBuilder(propagateMisc);
                        while (mi.hasNext()) {
                            String miscStr = (String) mi.next();

                            int qTest = checkExpiryDates(ret + "fin$");
                            ArrayList miscList3 = this.expiryDates("$" + ret, Double.parseDouble(Integer.toString(qTest)));

                            // ArrayList miscList3 = this.expiryDates("$" + ret, qtyPrpgt);
                            Debug.print("ret: " + ret);
                            Iterator m2 = miscList3.iterator();
                            ret = new StringBuilder();
                            String ret2 = "false";
                            int a = 0;
                            while (m2.hasNext()) {
                                String miscStr2 = (String) m2.next();

                                if (ret2 == "1st") {
                                    ret2 = "false";
                                }
                                Debug.print("2 miscStr: " + miscStr);
                                Debug.print("2 miscStr2: " + miscStr2);
                                if (miscStr2.equals(miscStr)) {
                                    if (a == 0) {
                                        a = 1;
                                        ret2 = "1st";
                                        Checker = "true";
                                    } else {
                                        a = a + 1;
                                        ret2 = "true";
                                    }
                                }
                                Debug.print("Checker: " + Checker);
                                if (!miscStr2.equals(miscStr) || a > 1) {
                                    if ((ret2 != "1st") || (ret2 == "false") || (ret2 == "true")) {
                                        if (miscStr2 != "") {
                                            miscStr2 = "$" + miscStr2;
                                            ret.append(miscStr2);
                                            ret2 = "false";
                                        }
                                    }
                                }
                            }
                            ret.append("$");
                            qtyPrpgt = qtyPrpgt - 1;
                        }
                        propagateMisc = ret.toString();
                    } else {
                        try {
                            propagateMisc = miscList + invPropagatedCosting.getCstExpiryDate().substring(1
                            );
                        }
                        catch (Exception e) {
                            propagateMisc = miscList;
                        }
                    }

                    invPropagatedCosting.setCstExpiryDate(propagateMisc);
                }
            }

            // regenerate cost variance
            // this.regenerateCostVariance(invCostings, invCosting, branchCode, companyCode);
            if (false) {
                throw new AdPRFCoaGlVarianceAccountNotFoundException();
            }

        }
        catch (AdPRFCoaGlVarianceAccountNotFoundException | GlobalExpiryDateNotFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public int checkExpiryDates(String misc) throws Exception {

        String separator = "$";

        // Remove first $ character
        misc = misc.substring(1);
        // Debug.print("misc: " + misc);
        // Counter
        int start = 0;
        int nextIndex = misc.indexOf(separator, start);
        int length = nextIndex - start;
        int numberExpry = 0;
        StringBuilder miscList = new StringBuilder();
        String miscList2 = "";
        String g = "";
        try {
            while (g != "fin") {
                // Date
                start = nextIndex + 1;
                nextIndex = misc.indexOf(separator, start);
                length = nextIndex - start;
                g = misc.substring(start, start + length);
                if (g.length() != 0) {
                    if (g != null || g != "" || g != "null") {
                        if (g.contains("null")) {
                            miscList2 = "Error";
                        } else {
                            miscList.append("$").append(g);
                            numberExpry++;
                        }
                    } else {
                        miscList2 = "Error";
                    }

                } else {
                    miscList2 = "Error";
                }
            }
        }
        catch (Exception e) {

        }

        if (miscList2 == "") {
            miscList.append("$");
        } else {
            miscList = new StringBuilder(miscList2);
        }

        return (numberExpry);
    }

    public String getQuantityExpiryDates(String qntty) {

        String separator = "$";

        // Remove first $ character
        qntty = qntty.substring(1);

        // Counter
        int start = 0;
        int nextIndex = qntty.indexOf(separator, start);
        int length = nextIndex - start;
        String y;
        y = (qntty.substring(start, start + length));

        return y;
    }

    private ArrayList expiryDates(String misc, double qty) throws Exception {

        Debug.print("ApReceivingItemControllerBean getExpiryDates");
        String separator = "$";

        // Remove first $ character
        misc = misc.substring(1);

        // Counter
        int start = 0;
        int nextIndex = misc.indexOf(separator, start);
        int length = nextIndex - start;

        ArrayList miscList = new ArrayList();

        for (int x = 0; x < qty; x++) {

            // Date
            start = nextIndex + 1;
            nextIndex = misc.indexOf(separator, start);
            length = nextIndex - start;

            try {
                String checker = misc.substring(start, start + length);
                if (checker.length() != 0 || checker != "null") {
                    miscList.add(checker);
                } else {
                    miscList.add("null");
                    qty++;
                }
            }
            catch (Exception e) {
                break;
            }
        }

        return miscList;
    }

    public String propagateExpiryDates(String misc, double qty, String reverse) throws Exception {
        // ActionErrors errors = new ActionErrors();
        Debug.print("ApReceivingItemControllerBean getExpiryDates");

        String separator = "";
        if (reverse == "False") {
            separator = "$";
        } else {
            separator = " ";
        }

        // Remove first $ character
        misc = misc.substring(1);
        // Counter
        int start = 0;
        int nextIndex = misc.indexOf(separator, start);
        int length = nextIndex - start;

        StringBuilder miscList = new StringBuilder();

        for (int x = 0; x < qty; x++) {

            // Date
            start = nextIndex + 1;
            nextIndex = misc.indexOf(separator, start);
            length = nextIndex - start;
            String g = misc.substring(start, start + length);
            if (g.length() != 0) {
                miscList.append("$").append(g);
            }
        }

        miscList.append("$");
        return (miscList.toString());
    }

    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue,
                          LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double JL_AMNT,
                          Integer companyCode) {

        Debug.print("InvAdjustmentPostControllerBean postToGl");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccountBalance glChartOfAccountBalance = glChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(
                    glAccountingCalendarValue.getAcvCode(), glChartOfAccount.getCoaCode(), companyCode);

            String ACCOUNT_TYPE = glChartOfAccount.getCoaAccountType();
            short FC_EXTNDD_PRCSN = adCompany.getGlFunctionalCurrency().getFcExtendedPrecision();

            if (((ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("EXPENSE")) && isDebit == EJBCommon.TRUE)
                    || (!ACCOUNT_TYPE.equals("ASSET") && !ACCOUNT_TYPE.equals("EXPENSE")
                    && isDebit == EJBCommon.FALSE)) {

                glChartOfAccountBalance.setCoabEndingBalance(
                        EJBCommon.roundIt(glChartOfAccountBalance.getCoabEndingBalance() + JL_AMNT, FC_EXTNDD_PRCSN));

                if (!isCurrentAcv) {

                    glChartOfAccountBalance.setCoabBeginningBalance(EJBCommon
                            .roundIt(glChartOfAccountBalance.getCoabBeginningBalance() + JL_AMNT, FC_EXTNDD_PRCSN));
                }

            } else {

                glChartOfAccountBalance.setCoabEndingBalance(
                        EJBCommon.roundIt(glChartOfAccountBalance.getCoabEndingBalance() - JL_AMNT, FC_EXTNDD_PRCSN));

                if (!isCurrentAcv) {

                    glChartOfAccountBalance.setCoabBeginningBalance(EJBCommon
                            .roundIt(glChartOfAccountBalance.getCoabBeginningBalance() - JL_AMNT, FC_EXTNDD_PRCSN));
                }
            }

            if (isCurrentAcv) {

                if (isDebit == EJBCommon.TRUE) {

                    glChartOfAccountBalance.setCoabTotalDebit(
                            EJBCommon.roundIt(glChartOfAccountBalance.getCoabTotalDebit() + JL_AMNT, FC_EXTNDD_PRCSN));

                } else {

                    glChartOfAccountBalance.setCoabTotalCredit(
                            EJBCommon.roundIt(glChartOfAccountBalance.getCoabTotalCredit() + JL_AMNT, FC_EXTNDD_PRCSN));
                }
            }

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem,
                                                      double ADJST_QTY, Integer companyCode) {

        Debug.print("InvAdjustmentPostControllerBean convertByUomFromAndItemAndQuantity");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            Debug.print("invItem.getIiName()-" + invItem.getIiName());
            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(),
                            companyCode);

            return EJBCommon.roundIt(
                    ADJST_QTY * invDefaultUomConversion.getUmcConversionFactor()
                            / invUnitOfMeasureConversion.getUmcConversionFactor(),
                    adPreference.getPrfInvCostPrecisionUnit());

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void regenerateInventoryDr(LocalInvAdjustment invAdjustment, Integer branchCode, Integer companyCode)
            throws GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException {

        Debug.print("InvAdjustmentPostControllerBean regenerateInventoryDr");

        LocalInvDistributionRecordHome invDistributionRecordHome = null;
        LocalInvCostingHome invCostingHome = null;
        LocalInvItemHome invItemHome = null;
        LocalInvItemLocationHome invItemLocationHome = null;
        LocalAdBranchItemLocationHome adBranchItemLocationHome = null;

        // Initialize EJB Home

        try {

            invDistributionRecordHome = (LocalInvDistributionRecordHome) EJBHomeFactory
                    .lookUpLocalHome(LocalInvDistributionRecordHome.JNDI_NAME, LocalInvDistributionRecordHome.class);
            invCostingHome = (LocalInvCostingHome) EJBHomeFactory.lookUpLocalHome(LocalInvCostingHome.JNDI_NAME,
                    LocalInvCostingHome.class);
            invItemHome = (LocalInvItemHome) EJBHomeFactory.lookUpLocalHome(LocalInvItemHome.JNDI_NAME,
                    LocalInvItemHome.class);
            invItemLocationHome = (LocalInvItemLocationHome) EJBHomeFactory
                    .lookUpLocalHome(LocalInvItemLocationHome.JNDI_NAME, LocalInvItemLocationHome.class);
            adBranchItemLocationHome = (LocalAdBranchItemLocationHome) EJBHomeFactory
                    .lookUpLocalHome(LocalAdBranchItemLocationHome.JNDI_NAME, LocalAdBranchItemLocationHome.class);

        }
        catch (NamingException ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            // regenerate inventory distribution records

            // remove all inventory distribution

            Collection invDistributionRecords = invDistributionRecordHome
                    .findImportableDrByAdjCode(invAdjustment.getAdjCode(), companyCode);

            Iterator i = invDistributionRecords.iterator();

            while (i.hasNext()) {

                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) i.next();
                i.remove();
                // invDistributionRecord.entityRemove();
                em.remove(invDistributionRecord);
            }

            // remove all adjustment lines committed qty

            Collection invAdjustmentLines = invAdjustment.getInvAdjustmentLines();

            i = invAdjustmentLines.iterator();

            while (i.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();

                if (invAdjustmentLine.getAlAdjustQuantity() < 0) {

                    double convertedQuantity = this.convertByUomFromAndItemAndQuantity(
                            invAdjustmentLine.getInvUnitOfMeasure(),
                            invAdjustmentLine.getInvItemLocation().getInvItem(),
                            Math.abs(invAdjustmentLine.getAlAdjustQuantity()), companyCode);

                    invAdjustmentLine.getInvItemLocation().setIlCommittedQuantity(
                            invAdjustmentLine.getInvItemLocation().getIlCommittedQuantity() - convertedQuantity);
                }
            }

            invAdjustmentLines = invAdjustment.getInvAdjustmentLines();

            if (invAdjustmentLines != null && !invAdjustmentLines.isEmpty()) {

                byte DEBIT = 0;
                double TOTAL_AMOUNT = 0d;

                i = invAdjustmentLines.iterator();

                while (i.hasNext()) {

                    LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();
                    LocalInvItemLocation invItemLocation = invAdjustmentLine.getInvItemLocation();

                    // start date validation
                    LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(
                                invAdjustment.getAdjDate(), invItemLocation.getInvItem().getIiName(),
                                invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }

                    // add physical inventory distribution

                    double AMOUNT = 0d;

                    if (invAdjustmentLine.getAlAdjustQuantity() > 0
                            && !invAdjustmentLine.getInvAdjustment().getAdjType().equals("ISSUANCE")) {

                        AMOUNT = EJBCommon.roundIt(
                                invAdjustmentLine.getAlAdjustQuantity() * invAdjustmentLine.getAlUnitCost(),
                                adPreference.getPrfInvQuantityPrecisionUnit());
                        DEBIT = EJBCommon.TRUE;

                    } else {

                        double COST = invAdjustmentLine.getInvItemLocation().getInvItem().getIiUnitCost();

                        try {

                            LocalInvCosting invCosting = invCostingHome
                                    .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                            invAdjustment.getAdjDate(), invItemLocation.getInvItem().getIiName(),
                                            invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);

                            if (invCosting.getCstRemainingQuantity() <= 0 && invCosting.getCstRemainingValue() <= 0) {
                                Debug.print("RE CALC");
                                HashMap criteria = new HashMap();
                                criteria.put("itemName", invItemLocation.getInvItem().getIiName());
                                criteria.put("location", invItemLocation.getInvLocation().getLocName());

                                ArrayList branchList = new ArrayList();

                                AdBranchDetails mdetails = new AdBranchDetails();
                                mdetails.setBrCode(branchCode);
                                branchList.add(mdetails);

                                ejbRIC.executeInvFixItemCosting(criteria, branchList, companyCode);

                                invCosting = invCostingHome
                                        .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                                invAdjustment.getAdjDate(), invItemLocation.getInvItem().getIiName(),
                                                invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                            }

                            if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Average")) {
                                COST = invCosting.getCstRemainingQuantity() <= 0 ? COST
                                        : Math.abs(invCosting.getCstRemainingValue()
                                        / invCosting.getCstRemainingQuantity());
                            }

                            if (COST <= 0) {
                                COST = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                            } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("FIFO")) {
                                COST = invCosting.getCstRemainingQuantity() == 0 ? COST
                                        : Math.abs(this.getInvFifoCost(invCosting.getCstDate(),
                                        invCosting.getInvItemLocation().getIlCode(),
                                        invAdjustmentLine.getAlAdjustQuantity(),
                                        invAdjustmentLine.getAlUnitCost(), false, branchCode, companyCode));
                            } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Standard")) {
                                COST = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                            }

                        }
                        catch (FinderException ex) {
                        }

                        COST = this.convertCostByUom(invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(),
                                invAdjustmentLine.getInvUnitOfMeasure().getUomName(), COST, true, companyCode);

                        AMOUNT = EJBCommon.roundIt(invAdjustmentLine.getAlAdjustQuantity() * COST,
                                adPreference.getPrfInvCostPrecisionUnit());

                        DEBIT = EJBCommon.FALSE;
                    }

                    // check for branch mapping

                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {

                        adBranchItemLocation = adBranchItemLocationHome
                                .findBilByIlCodeAndBrCode(invItemLocation.getIlCode(), branchCode, companyCode);

                    }
                    catch (FinderException ex) {

                    }

                    LocalGlChartOfAccount glInventoryChartOfAccount = null;

                    if (adBranchItemLocation == null) {

                        glInventoryChartOfAccount = glChartOfAccountHome
                                .findByPrimaryKey(invAdjustmentLine.getInvItemLocation().getIlGlCoaInventoryAccount());
                    } else {

                        glInventoryChartOfAccount = glChartOfAccountHome
                                .findByPrimaryKey(adBranchItemLocation.getBilCoaGlInventoryAccount());
                    }

                    this.addInvDrEntry(invAdjustment.getInvDrNextLine(), "INVENTORY", DEBIT, Math.abs(AMOUNT),
                            EJBCommon.FALSE, glInventoryChartOfAccount.getCoaCode(), invAdjustment, branchCode, companyCode);

                    TOTAL_AMOUNT += AMOUNT;
                    // ABS_TOTAL_AMOUNT += Math.abs(AMOUNT);

                    // add adjust quantity to item location committed quantity if negative

                    if (invAdjustmentLine.getAlAdjustQuantity() < 0) {

                        double convertedQuantity = this.convertByUomFromAndItemAndQuantity(
                                invAdjustmentLine.getInvUnitOfMeasure(),
                                invAdjustmentLine.getInvItemLocation().getInvItem(),
                                Math.abs(invAdjustmentLine.getAlAdjustQuantity()), companyCode);

                        invItemLocation = invAdjustmentLine.getInvItemLocation();

                        invItemLocation
                                .setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() + convertedQuantity);
                    }
                }

                // add variance or transfer/debit distribution

                DEBIT = (TOTAL_AMOUNT > 0 && !invAdjustment.getAdjType().equals("ISSUANCE")) ? EJBCommon.FALSE
                        : EJBCommon.TRUE;

                this.addInvDrEntry(invAdjustment.getInvDrNextLine(), "ADJUSTMENT", DEBIT, Math.abs(TOTAL_AMOUNT),
                        EJBCommon.FALSE, invAdjustment.getGlChartOfAccount().getCoaCode(), invAdjustment, branchCode,
                        companyCode);
            }

        }
        catch (GlobalInventoryDateException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlobalBranchAccountNumberInvalidException ex) {

            throw new GlobalBranchAccountNumberInvalidException();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void addInvDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, byte DR_RVRSL,
                               Integer COA_CODE, LocalInvAdjustment invAdjustment, Integer branchCode, Integer companyCode)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("InvAdjustmentPostControllerBean addInvDrEntry");
        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate coa

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, branchCode, companyCode);

            }
            catch (FinderException ex) {

                throw new GlobalBranchAccountNumberInvalidException();
            }

            // create distribution record

            LocalInvDistributionRecord invDistributionRecord = invDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT,
                    EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), DR_RVRSL,
                    EJBCommon.FALSE, companyCode);

            // invAdjustment.addInvDistributionRecord(invDistributionRecord);
            invDistributionRecord.setInvAdjustment(invAdjustment);

            // glChartOfAccount.addInvDistributionRecord(invDistributionRecord);
            invDistributionRecord.setInvChartOfAccount(glChartOfAccount);

        }
        catch (GlobalBranchAccountNumberInvalidException ex) {

            throw new GlobalBranchAccountNumberInvalidException();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertByUomAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem,
                                           double ADJST_QTY, Integer companyCode) {

        Debug.print("ArInvoicePostControllerBean convertByUomFromAndUomToAndQuantity");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(),
                            companyCode);

            return EJBCommon.roundIt(
                    ADJST_QTY * invDefaultUomConversion.getUmcConversionFactor()
                            / invUnitOfMeasureConversion.getUmcConversionFactor(),
                    adPreference.getPrfInvCostPrecisionUnit());

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertCostByUom(String II_NM, String UOM_NM, double unitCost, boolean isFromDefault,
                                    Integer companyCode) {

        Debug.print("InvAdjustmentPostControllerBean convertCostByUom");
        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NM, companyCode);
            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(II_NM, UOM_NM, companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(II_NM, invItem.getInvUnitOfMeasure().getUomName(), companyCode);

            if (isFromDefault) {

                return unitCost * invDefaultUomConversion.getUmcConversionFactor()
                        / invUnitOfMeasureConversion.getUmcConversionFactor();

            } else {

                return unitCost * invUnitOfMeasureConversion.getUmcConversionFactor()
                        / invDefaultUomConversion.getUmcConversionFactor();
            }

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double getInvFifoCost2(Date CST_DT, Integer IL_CODE, double CST_QTY, boolean isAdjustFifo, Integer branchCode, Integer companyCode) {

        int isNotSingle = 0;
        try {

            CostA = 0d;
            CostB = 0d;
            UnitA = 0d;
            UnitB = 0d;
            CostAmount[0] = 0d;
            CostAmount[1] = 0d;
            CostAmount[2] = 0d;
            CostAmount[3] = 0d;
            CostAmount[4] = 0d;
            CostAmount[5] = 0d;
            CostAmount[6] = 0d;
            CostAmount[7] = 0d;
            CostAmount[8] = 0d;
            CostAmount[9] = 0d;
            UnitValue[0] = 0d;
            UnitValue[1] = 0d;
            UnitValue[2] = 0d;
            UnitValue[3] = 0d;
            UnitValue[4] = 0d;
            UnitValue[5] = 0d;
            UnitValue[6] = 0d;
            UnitValue[7] = 0d;
            UnitValue[8] = 0d;
            UnitValue[9] = 0d;

            Collection invFifoCostings = invCostingHome
                    .findFifoRemainingQuantityByLessThanOrEqualCstDateAndIlCodeAndBrCode(CST_DT, IL_CODE, branchCode,
                            companyCode);
            Debug.print("TEST " + CST_DT + " " + IL_CODE + " " + CST_QTY + " " + branchCode + " " + companyCode);
            if (invFifoCostings.size() > 0) {
                double fifoCost = 0;
                double runningQty = Math.abs(CST_QTY);
                double xStart = runningQty;
                double value = runningQty;
                double costvalue = 0d;
                double xStartCost = 0d;
                double totalUnitCommited = 0d;
                Debug.print("Start ----:" + CST_QTY);
                for (Object fifoCosting : invFifoCostings) {

                    LocalInvCosting invFifoCosting = (LocalInvCosting) fifoCosting;
                    LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
                    double newRemainingLifoQuantity = 0;
                    if (invFifoCosting.getCstRemainingLifoQuantity() <= runningQty) {
                        if (invFifoCosting.getApPurchaseOrderLine() != null
                                || invFifoCosting.getApVoucherLineItem() != null) {
                            fifoCost += invFifoCosting.getCstItemCost() / invFifoCosting.getCstQuantityReceived()
                                    * invFifoCosting.getCstRemainingLifoQuantity();
                        } else if (invFifoCosting.getArInvoiceLineItem() != null) {
                            fifoCost += invFifoCosting.getCstCostOfSales() / invFifoCosting.getCstQuantitySold()
                                    * invFifoCosting.getCstRemainingLifoQuantity();
                        } else {
                            fifoCost += invFifoCosting.getCstAdjustCost() / invFifoCosting.getCstAdjustQuantity()
                                    * invFifoCosting.getCstRemainingLifoQuantity();
                        }

                        runningQty = runningQty - invFifoCosting.getCstRemainingLifoQuantity();
                        // Debug.print("runningQty ----:" + runningQty );
                        double xxxxx = Math.abs(CST_QTY) - runningQty;
                        costvalue = fifoCost - xStartCost;
                        Debug.print("cost ----:" + costvalue);
                        xStartCost += costvalue;
                        Debug.print("costvalue ----:" + xStartCost);
                        value = xStart - runningQty;
                        // Debug.print("value ----:" + value );
                        xStart = runningQty;
                        totalUnitCommited += value;
                        CostA = fifoCost;
                        UnitA = xxxxx;

                        CostAmount[isNotSingle] = costvalue;
                        UnitValue[isNotSingle] = value;
                        invFifoCosting
                                .setCstRemainingLifoQuantity(invFifoCosting.getCstRemainingLifoQuantity() - value);
                        Debug.print("invFifoCosting.getCstRemainingLifoQuantity() ----:"
                                + invFifoCosting.getCstRemainingLifoQuantity());
                        Debug.print("commited() ----:" + value);

                        Debug.print("Unit ----:" + UnitValue[isNotSingle]);
                        Debug.print("Cost ---:" + CostAmount[isNotSingle]);
                        Debug.print("isNotSingle" + isNotSingle);
                        isNotSingle++;
                    } else {
                        Debug.print("CostForward ---:" + fifoCost);
                        double fifoCost2 = 0d;
                        if (invFifoCosting.getApPurchaseOrderLine() != null
                                || invFifoCosting.getApVoucherLineItem() != null) {
                            fifoCost2 += invFifoCosting.getCstItemCost() / invFifoCosting.getCstQuantityReceived()
                                    * runningQty;
                        } else if (invFifoCosting.getArInvoiceLineItem() != null) {
                            fifoCost2 += invFifoCosting.getCstCostOfSales() / invFifoCosting.getCstQuantitySold()
                                    * runningQty;
                        } else {
                            fifoCost2 += invFifoCosting.getCstAdjustCost() / invFifoCosting.getCstAdjustQuantity()
                                    * runningQty;
                        }
                        newRemainingLifoQuantity = invFifoCosting.getCstRemainingLifoQuantity() - runningQty;

                        CostB = fifoCost2;
                        Debug.print("fifoCost2 ----:" + fifoCost2);
                        UnitB = runningQty;
                        invFifoCosting
                                .setCstRemainingLifoQuantity(invFifoCosting.getCstRemainingLifoQuantity() - runningQty);
                        Debug.print("invFifoCosting.getCstRemainingLifoQuantity() ----:"
                                + invFifoCosting.getCstRemainingLifoQuantity());
                        Debug.print("commited() ----:" + runningQty);

                        if (isNotSingle == 0) {
                            CostAmount[isNotSingle] = fifoCost2;
                            UnitValue[isNotSingle] = runningQty;
                        } else {
                            CostAmount[isNotSingle] = fifoCost2;
                            UnitValue[isNotSingle] = runningQty;
                        }
                        totalUnitCommited += runningQty;
                        Debug.print("UnitA ----:" + UnitValue[isNotSingle]);
                        Debug.print("CostA---:" + CostAmount[isNotSingle]);
                        Debug.print("isNotSingle" + isNotSingle);
                        runningQty = 0;
                        isNotSingle++;
                    }

                    if (isAdjustFifo) {
                        // invFifoCosting.setCstRemainingLifoQuantity(newRemainingLifoQuantity);
                        Debug.print("Commited value" + totalUnitCommited);
                    }
                    if (runningQty <= 0) {
                        break;
                    }
                }
                LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
                Debug.print("---------------------->isNotSingle" + isNotSingle);
                return isNotSingle;

            } else {

                // most applicable in 1st entries of data
                LocalInvItemLocation invItemLocation = invItemLocationHome.findByPrimaryKey(IL_CODE);
                Debug.print("---------------------->isNotSingle" + isNotSingle);
                return isNotSingle;
            }

        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("InvAdjustmentPostControllerBean ejbCreate");
    }

}