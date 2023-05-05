/**
 * @copyright 2022 Omega Business Consulting, Inc.
 * @class InvStockTransferEntryControllerBean
 */
package com.ejb.txn.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.gl.*;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.gl.*;
import com.ejb.entities.inv.*;
import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.inv.InvStockTransferDetails;
import com.util.mod.inv.InvModStockTransferDetails;
import com.util.mod.inv.InvModStockTransferLineDetails;
import com.util.mod.inv.InvModUnitOfMeasureDetails;

import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Stateless(name = "InvStockTransferEntryControllerEJB")
public class InvStockTransferEntryControllerBean extends EJBContextClass
        implements InvStockTransferEntryController {

    @EJB
    public PersistenceBeanClass em;
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
    private LocalInvStockTransferHome invStockTransferHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdAmountLimitHome adAmountLimitHome;
    @EJB
    private LocalAdApprovalUserHome adApprovalUserHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalAdDeleteAuditTrailHome adDeleteAuditTrailHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalInvStockTransferLineHome invStockTransferLineHome;
    @EJB
    private LocalInvDistributionRecordHome invDistributionRecordHome;
    @EJB
    private LocalGlJournalBatchHome glJournalBatchHome;
    @EJB
    private LocalGlJournalHome glJournalHome;
    @EJB
    private LocalGlJournalSourceHome glJournalSourceHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalGlJournalCategoryHome glJournalCategoryHome;
    @EJB
    private LocalGlJournalLineHome glJournalLineHome;
    @EJB
    private LocalGlSuspenseAccountHome glSuspenseAccountHome;
    @EJB
    private LocalInvAdjustmentLineHome invAdjustmentLineHome;
    @EJB
    private LocalInvAdjustmentHome invAdjustmentHome;

    public InvModStockTransferDetails getInvStByStCode(Integer ST_CODE, Integer companyCode)
            throws GlobalNoRecordFoundException {

        Debug.print("InvStockTransferEntryControllerBean getInvStByStCode");
        try {

            LocalInvStockTransfer invStockTransfer = null;

            try {

                invStockTransfer = invStockTransferHome.findByPrimaryKey(ST_CODE);

            }
            catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList stlList = new ArrayList();

            for (Object o : invStockTransfer.getInvStockTransferLines()) {

                LocalInvStockTransferLine invStockTransferLine = (LocalInvStockTransferLine) o;

                InvModStockTransferLineDetails mdetails = new InvModStockTransferLineDetails();

                mdetails.setStlCode(invStockTransferLine.getStlCode());

                mdetails.setStlLocationNameFrom(this.getInvLocNameByLocCode(invStockTransferLine.getStlLocationFrom()));
                mdetails.setStlLocationNameTo(this.getInvLocNameByLocCode(invStockTransferLine.getStlLocationTo()));
                mdetails.setStlIiName(invStockTransferLine.getInvItem().getIiName());
                mdetails.setStlIiDescription(invStockTransferLine.getInvItem().getIiDescription());
                mdetails.setStlUomName(invStockTransferLine.getInvUnitOfMeasure().getUomName());
                mdetails.setStlUnitCost(invStockTransferLine.getStlUnitCost());
                mdetails.setStlQuantityDelivered(invStockTransferLine.getStlQuantityDelivered());
                mdetails.setStlAmount(invStockTransferLine.getStlAmount());
                mdetails.setStlMisc(invStockTransferLine.getStlMisc());
                stlList.add(mdetails);
            }

            InvModStockTransferDetails details = new InvModStockTransferDetails();
            details.setStCode(invStockTransfer.getStCode());
            details.setStDocumentNumber(invStockTransfer.getStDocumentNumber());
            details.setStReferenceNumber(invStockTransfer.getStReferenceNumber());
            details.setStDescription(invStockTransfer.getStDescription());
            details.setStDate(invStockTransfer.getStDate());
            details.setStApprovalStatus(invStockTransfer.getStApprovalStatus());
            details.setStPosted(invStockTransfer.getStPosted());
            details.setStCreatedBy(invStockTransfer.getStCreatedBy());
            details.setStDateCreated(invStockTransfer.getStDateCreated());
            details.setStLastModifiedBy(invStockTransfer.getStLastModifiedBy());
            details.setStDateLastModified(invStockTransfer.getStDateLastModified());
            details.setStApprovedRejectedBy(invStockTransfer.getStApprovedRejectedBy());
            details.setStDateApprovedRejected(invStockTransfer.getStDateApprovedRejected());
            details.setStPostedBy(invStockTransfer.getStPostedBy());
            details.setStDatePosted(invStockTransfer.getStDatePosted());
            details.setStReasonForRejection(invStockTransfer.getStReasonForRejection());
            details.setStStlList(stlList);

            return details;

        }
        catch (GlobalNoRecordFoundException ex) {

            Debug.printStackTrace(ex);
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getInvUomByIiName(String II_NM, Integer companyCode) {

        Debug.print("InvStockTransferEntryControllerBean getInvUomByIiName");
        ArrayList list = new ArrayList();
        try {

            LocalInvItem invItem = null;
            LocalInvUnitOfMeasure invItemUnitOfMeasure = null;

            invItem = invItemHome.findByIiName(II_NM, companyCode);
            invItemUnitOfMeasure = invItem.getInvUnitOfMeasure();

            for (Object o : invUnitOfMeasureHome.findByUomAdLvClass(invItemUnitOfMeasure.getUomAdLvClass(), companyCode)) {

                LocalInvUnitOfMeasure invUnitOfMeasure = (LocalInvUnitOfMeasure) o;
                InvModUnitOfMeasureDetails details = new InvModUnitOfMeasureDetails();
                details.setUomName(invUnitOfMeasure.getUomName());

                if (invUnitOfMeasure.getUomName().equals(invItemUnitOfMeasure.getUomName())) {

                    details.setDefault(true);
                }

                list.add(details);
            }

            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer saveInvStEntry(InvStockTransferDetails details, ArrayList stlList, boolean isDraft,
                                  Integer AD_BRNCH, Integer companyCode) throws GlobalRecordAlreadyDeletedException,
            GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException,
            GlobalTransactionAlreadyPostedException, GlobalNoApprovalRequesterFoundException,
            GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException,
            GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException,
            GlobalJournalNotBalanceException, GlobalDocumentNumberNotUniqueException, GlobalInventoryDateException,
            GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException,
            GlobalRecordInvalidException {

        Debug.print("InvStockTransferEntryControllerBean saveInvStEntry");

        // switch if the post type is from draft to post or not
        boolean IfDraftToPost = true;

        try {

            LocalInvStockTransfer invStockTransfer = null;

            // validate if stock transfer is already deleted

            try {

                if (details.getStCode() != null) {

                    invStockTransfer = invStockTransferHome.findByPrimaryKey(details.getStCode());
                }

            }
            catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if stock transfer is already posted, void, approved or pending

            if (details.getStCode() != null) {

                if (invStockTransfer.getStApprovalStatus() != null) {

                    if (invStockTransfer.getStApprovalStatus().equals("APPROVED")
                            || invStockTransfer.getStApprovalStatus().equals("N/A")) {

                        throw new GlobalTransactionAlreadyApprovedException();

                    } else if (invStockTransfer.getStApprovalStatus().equals("PENDING")) {

                        throw new GlobalTransactionAlreadyPendingException();
                    }
                }

                if (invStockTransfer.getStPosted() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyPostedException();
                }
            }

            LocalInvStockTransfer invExistingStockTransfer = null;

            try {

                invExistingStockTransfer = invStockTransferHome
                        .findByStDocumentNumberAndBrCode(details.getStDocumentNumber(), AD_BRNCH, companyCode);

            }
            catch (FinderException ex) {

            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            // validate if document number is unique document number is automatic then set
            // next sequence

            if (details.getStCode() == null) {

                LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
                LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

                if (invExistingStockTransfer != null) {

                    throw new GlobalDocumentNumberNotUniqueException();
                }

                try {

                    adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("INV STOCK TRANSFER",
                            companyCode);

                }
                catch (FinderException ex) {

                }

                try {

                    adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome
                            .findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, companyCode);

                }
                catch (FinderException ex) {

                }

                if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A'
                        && (details.getStDocumentNumber() == null
                        || details.getStDocumentNumber().trim().length() == 0)) {

                    while (true) {

                        if (adBranchDocumentSequenceAssignment == null
                                || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                            try {

                                invStockTransferHome.findByStDocumentNumberAndBrCode(
                                        adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, companyCode);
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon
                                        .incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                            }
                            catch (FinderException ex) {

                                details.setStDocumentNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon
                                        .incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                break;
                            }

                        } else {

                            try {

                                invStockTransferHome.findByStDocumentNumberAndBrCode(
                                        adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, companyCode);
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(
                                        adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                            }
                            catch (FinderException ex) {

                                details.setStDocumentNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(
                                        adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                break;
                            }
                        }
                    }
                }

            } else {

                IfDraftToPost = false;
                if (invExistingStockTransfer != null
                        && !invExistingStockTransfer.getStCode().equals(details.getStCode())) {

                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (invStockTransfer.getStDocumentNumber() != details.getStDocumentNumber()
                        && (details.getStDocumentNumber() == null
                        || details.getStDocumentNumber().trim().length() == 0)) {

                    details.setStDocumentNumber(invStockTransfer.getStDocumentNumber());
                }
            }

            // used in checking if invoice should re-generate distribution records

            boolean isRecalculate = true;

            // create stock transfer

            if (details.getStCode() == null) {

                invStockTransfer = invStockTransferHome.create(details.getStDocumentNumber(),
                        details.getStReferenceNumber(), details.getStDescription(), details.getStDate(),
                        details.getStApprovalStatus(), EJBCommon.FALSE, details.getStCreatedBy(),
                        details.getStDateCreated(), details.getStLastModifiedBy(), details.getStDateLastModified(),
                        null, null, null, null, null, AD_BRNCH, companyCode);

            } else {

                if (stlList.size() != invStockTransfer.getInvStockTransferLines().size()
                        || !(invStockTransfer.getStDate().equals(details.getStDate()))) {

                    isRecalculate = true;

                } else if (stlList.size() == invStockTransfer.getInvStockTransferLines().size()) {

                    Iterator stlIter = invStockTransfer.getInvStockTransferLines().iterator();
                    Iterator stlIterList = stlList.iterator();

                    while (stlIter.hasNext()) {

                        LocalInvStockTransferLine invStockTransferLine = (LocalInvStockTransferLine) stlIter.next();
                        InvModStockTransferLineDetails mdetails = (InvModStockTransferLineDetails) stlIterList.next();

                        String invLocationFrom = this.getInvLocNameByLocCode(invStockTransferLine.getStlLocationFrom());
                        String invLocationTo = this.getInvLocNameByLocCode(invStockTransferLine.getStlLocationTo());

                        if (!invLocationFrom.equals(mdetails.getStlLocationNameFrom())
                                || !invLocationTo.equals(mdetails.getStlLocationNameTo())
                                || invStockTransferLine.getStlUnitCost() != mdetails.getStlUnitCost()
                                || invStockTransferLine.getStlQuantityDelivered() != mdetails.getStlQuantityDelivered()
                                || invStockTransferLine.getStlAmount() != mdetails.getStlAmount()
                                || !invStockTransferLine.getInvItem().getIiName().equals(mdetails.getStlIiName())
                                || !invStockTransferLine.getInvUnitOfMeasure().getUomName()
                                .equals(mdetails.getStlUomName())) {

                            isRecalculate = true;
                            break;
                        }

                        // get item cost

                        LocalInvItemLocation invItemLocFrom = null;

                        try {

                            invItemLocFrom = invItemLocationHome.findByLocNameAndIiName(invLocationFrom,
                                    invStockTransferLine.getInvItem().getIiName(), companyCode);

                        }
                        catch (FinderException ex) {

                            throw new GlobalInvItemLocationNotFoundException(invLocationFrom);
                        }

                        double COST = 0d;

                        try {

                            LocalInvCosting invCosting = invCostingHome
                                    .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                            invStockTransfer.getStDate(), invItemLocFrom.getInvItem().getIiName(),
                                            invItemLocFrom.getInvLocation().getLocName(), AD_BRNCH, companyCode);

                            if (invCosting.getCstRemainingQuantity() <= 0) {
                                COST = EJBCommon.roundIt(
                                        Math.abs(invCosting.getCstRemainingValue()
                                                / invCosting.getCstRemainingQuantity()),
                                        this.getGlFcPrecisionUnit(companyCode));

                            } else {
                                COST = invItemLocFrom.getInvItem().getIiUnitCost();
                            }

                        }
                        catch (FinderException ex) {

                            COST = invItemLocFrom.getInvItem().getIiUnitCost();
                        }

                        LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome
                                .findUmcByIiNameAndUomName(invItemLocFrom.getInvItem().getIiName(),
                                        invStockTransferLine.getInvUnitOfMeasure().getUomName(), companyCode);
                        LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome
                                .findUmcByIiNameAndUomName(invItemLocFrom.getInvItem().getIiName(),
                                        invItemLocFrom.getInvItem().getInvUnitOfMeasure().getUomName(), companyCode);

                        COST = EJBCommon.roundIt(
                                COST * invDefaultUomConversion.getUmcConversionFactor()
                                        / invUnitOfMeasureConversion.getUmcConversionFactor(),
                                this.getGlFcPrecisionUnit(companyCode));

                        double AMOUNT = 0d;

                        AMOUNT = EJBCommon.roundIt(invStockTransferLine.getStlQuantityDelivered() * COST,
                                this.getGlFcPrecisionUnit(companyCode));

                        if (invStockTransferLine.getStlUnitCost() != COST) {

                            mdetails.setStlUnitCost(COST);
                            mdetails.setStlAmount(AMOUNT);

                            isRecalculate = true;
                            break;
                        }

                        isRecalculate = false;
                    }

                } else {

                    isRecalculate = true;
                }

                invStockTransfer.setStDocumentNumber(details.getStDocumentNumber());
                invStockTransfer.setStReferenceNumber(details.getStReferenceNumber());
                invStockTransfer.setStDescription(details.getStDescription());
                invStockTransfer.setStDate(details.getStDate());
                invStockTransfer.setStApprovalStatus(details.getStApprovalStatus());
                invStockTransfer.setStLastModifiedBy(details.getStLastModifiedBy());
                invStockTransfer.setStDateLastModified(details.getStDateLastModified());
                invStockTransfer.setStReasonForRejection(null);
            }

            double ABS_TOTAL_AMOUNT = 0d;

            if (isRecalculate) {

                // remove all stock transfer lines

                Iterator i = invStockTransfer.getInvStockTransferLines().iterator();

                short LINE_NUMBER = 0;

                while (i.hasNext()) {

                    LINE_NUMBER++;

                    LocalInvStockTransferLine invStockTransferLine = (LocalInvStockTransferLine) i.next();

                    String invLocation = this.getInvLocNameByLocCode(invStockTransferLine.getStlLocationFrom());

                    LocalInvItemLocation invItemLocation = null;

                    try {

                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(invLocation,
                                invStockTransferLine.getInvItem().getIiName(), companyCode);

                    }
                    catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(LINE_NUMBER));
                    }

                    double convertedQuantity = this.convertByUomFromAndItemAndQuantity(
                            invStockTransferLine.getInvUnitOfMeasure(), invItemLocation.getInvItem(),
                            invStockTransferLine.getStlQuantityDelivered(), companyCode);

                    invItemLocation
                            .setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - convertedQuantity);

                    i.remove();

                    // invStockTransferLine.entityRemove();
                    em.remove(invStockTransferLine);
                }

                // remove all distribution records

                i = invStockTransfer.getInvDistributionRecords().iterator();

                while (i.hasNext()) {

                    LocalInvDistributionRecord arDistributionRecord = (LocalInvDistributionRecord) i.next();

                    i.remove();

                    // arDistributionRecord.entityRemove();
                    em.remove(arDistributionRecord);
                }

                // add new stock transfer entry lines and distribution record

                byte DEBIT = 0;

                i = stlList.iterator();

                while (i.hasNext()) {

                    InvModStockTransferLineDetails mdetails = (InvModStockTransferLineDetails) i.next();

                    LocalInvItemLocation invItemLocFrom = null;
                    LocalInvItemLocation invItemLocTo = null;

                    try {

                        invItemLocFrom = invItemLocationHome.findByLocNameAndIiName(mdetails.getStlLocationNameFrom(),
                                mdetails.getStlIiName(), companyCode);

                    }
                    catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(mdetails.getStlLineNumber() + " - " + mdetails.getStlLocationNameFrom());
                    }

                    try {

                        invItemLocTo = invItemLocationHome.findByLocNameAndIiName(mdetails.getStlLocationNameTo(),
                                mdetails.getStlIiName(), companyCode);

                    }
                    catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(
                                mdetails.getStlLineNumber() + " - " + mdetails.getStlLocationNameTo());
                    }

                    // start date validation
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(
                                invStockTransfer.getStDate(), invItemLocFrom.getInvItem().getIiName(),
                                invItemLocFrom.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocFrom.getInvItem().getIiName());
                        }

                        invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(
                                invStockTransfer.getStDate(), invItemLocTo.getInvItem().getIiName(),
                                invItemLocTo.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocTo.getInvItem().getIiName());
                        }
                    }
                    LocalInvStockTransferLine invStockTransferLine = this.addInvStlEntry(mdetails, invStockTransfer,
                            companyCode);

                    // add physical inventory distribution

                    double COST = this.getInvIiUnitCostByIiNameAndLocFromAndUomNameAndDate(
                            invItemLocFrom.getInvItem().getIiName(), invItemLocFrom.getInvLocation().getLocName(),
                            invStockTransferLine.getInvUnitOfMeasure().getUomName(), invStockTransfer.getStDate(),
                            AD_BRNCH, companyCode);

                    double AMOUNT = 0d;

                    AMOUNT = EJBCommon.roundIt(invStockTransferLine.getStlQuantityDelivered() * COST,
                            this.getGlFcPrecisionUnit(companyCode));

                    // dr to locationTo

                    // check branch mapping

                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {

                        adBranchItemLocation = adBranchItemLocationHome
                                .findBilByIlCodeAndBrCode(invItemLocTo.getIlCode(), AD_BRNCH, companyCode);

                    }
                    catch (FinderException ex) {

                    }

                    LocalGlChartOfAccount glChartOfAccountTo = null;

                    if (adBranchItemLocation == null) {

                        glChartOfAccountTo = glChartOfAccountHome
                                .findByPrimaryKey(invItemLocTo.getIlGlCoaInventoryAccount());

                    } else {

                        glChartOfAccountTo = glChartOfAccountHome
                                .findByPrimaryKey(adBranchItemLocation.getBilCoaGlInventoryAccount());
                    }

                    this.addInvDrEntry(invStockTransfer.getInvDrNextLine(), "INVENTORY", EJBCommon.TRUE,
                            Math.abs(AMOUNT), glChartOfAccountTo.getCoaCode(), invStockTransfer, AD_BRNCH, companyCode);

                    // cr to locationFrom

                    // check branch mapping

                    try {

                        adBranchItemLocation = adBranchItemLocationHome
                                .findBilByIlCodeAndBrCode(invItemLocFrom.getIlCode(), AD_BRNCH, companyCode);

                    }
                    catch (FinderException ex) {

                    }

                    LocalGlChartOfAccount glChartOfAccountFrom = null;

                    if (adBranchItemLocation == null) {

                        glChartOfAccountFrom = glChartOfAccountHome
                                .findByPrimaryKey(invItemLocFrom.getIlGlCoaInventoryAccount());

                    } else {

                        glChartOfAccountFrom = glChartOfAccountHome
                                .findByPrimaryKey(adBranchItemLocation.getBilCoaGlInventoryAccount());
                    }

                    this.addInvDrEntry(invStockTransfer.getInvDrNextLine(), "INVENTORY", EJBCommon.FALSE,
                            Math.abs(AMOUNT), glChartOfAccountFrom.getCoaCode(), invStockTransfer, AD_BRNCH, companyCode);

                    ABS_TOTAL_AMOUNT += Math.abs(AMOUNT);

                    // set ilCommittedQuantity

                    double convertedQuantity = this.convertByUomFromAndItemAndQuantity(
                            invStockTransferLine.getInvUnitOfMeasure(), invItemLocFrom.getInvItem(),
                            invStockTransferLine.getStlQuantityDelivered(), companyCode);

                    invItemLocFrom.setIlCommittedQuantity(invItemLocFrom.getIlCommittedQuantity() + convertedQuantity);
                }

            } else {

                Iterator i = stlList.iterator();

                while (i.hasNext()) {

                    InvModStockTransferLineDetails mdetails = (InvModStockTransferLineDetails) i.next();

                    LocalInvItemLocation invItemLocFrom = null;
                    LocalInvItemLocation invItemLocTo = null;

                    try {

                        invItemLocFrom = invItemLocationHome.findByLocNameAndIiName(mdetails.getStlLocationNameFrom(),
                                mdetails.getStlIiName(), companyCode);

                    }
                    catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(mdetails.getStlLineNumber() + " - " + mdetails.getStlLocationNameFrom());
                    }

                    try {

                        invItemLocTo = invItemLocationHome.findByLocNameAndIiName(mdetails.getStlLocationNameTo(),
                                mdetails.getStlIiName(), companyCode);

                    }
                    catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(
                                mdetails.getStlLineNumber() + " - " + mdetails.getStlLocationNameTo());
                    }

                    // start date validation
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(
                                invStockTransfer.getStDate(), invItemLocFrom.getInvItem().getIiName(),
                                invItemLocFrom.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocFrom.getInvItem().getIiName());
                        }

                        invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(
                                invStockTransfer.getStDate(), invItemLocTo.getInvItem().getIiName(),
                                invItemLocTo.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocTo.getInvItem().getIiName());
                        }
                    }
                }

                i = invStockTransfer.getInvDistributionRecords().iterator();

                while (i.hasNext()) {

                    LocalInvDistributionRecord distributionRecord = (LocalInvDistributionRecord) i.next();

                    if (distributionRecord.getDrDebit() == 1) {

                        ABS_TOTAL_AMOUNT += distributionRecord.getDrAmount();
                    }
                }
            }

            // insufficient stock checking
            if (adPreference.getPrfArCheckInsufficientStock() == EJBCommon.TRUE) {
                boolean hasInsufficientItems = false;
                StringBuilder insufficientItems = new StringBuilder();

                Collection invStockTransferLines = invStockTransfer.getInvStockTransferLines();

                for (Object stockTransferLine : invStockTransferLines) {

                    LocalInvStockTransferLine invStockTransferLine = (LocalInvStockTransferLine) stockTransferLine;

                    String LOC_NM_FRM = this.getInvLocNameByLocCode(invStockTransferLine.getStlLocationFrom());
                    String LOC_NM_TO = this.getInvLocNameByLocCode(invStockTransferLine.getStlLocationTo());

                    LocalInvItemLocation invItemLocationFrom = invItemLocationHome.findByLocNameAndIiName(LOC_NM_FRM,
                            invStockTransferLine.getInvItem().getIiName(), companyCode);

                    double ST_QTY = this.convertByUomFromAndItemAndQuantity(invStockTransferLine.getInvUnitOfMeasure(),
                            invStockTransferLine.getInvItem(), invStockTransferLine.getStlQuantityDelivered(),
                            companyCode);

                    String II_NM = invStockTransferLine.getInvItem().getIiName();

                    LocalInvCosting invCosting = null;

                    try {

                        invCosting = invCostingHome
                                .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                        invStockTransfer.getStDate(), II_NM, LOC_NM_FRM, AD_BRNCH, companyCode);

                    }
                    catch (FinderException ex) {

                    }

                    double LOWEST_QTY = this.convertByUomAndQuantity(invStockTransferLine.getInvUnitOfMeasure(),
                            invStockTransferLine.getInvItem(), 1, companyCode);

                    if (invCosting == null || invCosting.getCstRemainingQuantity() == 0
                            || invCosting.getCstRemainingQuantity() - ST_QTY <= -LOWEST_QTY) {

                        hasInsufficientItems = true;

                        insufficientItems.append(invStockTransferLine.getInvItem().getIiName()).append(", ");
                    }
                }
                if (hasInsufficientItems) {

                    throw new GlobalRecordInvalidException(
                            insufficientItems.substring(0, insufficientItems.lastIndexOf(",")));
                }
            }

            // generate approval status

            String INV_APPRVL_STATUS = null;

            if (!isDraft) {

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);

                // check if inv stock transfer approval is enabled

                if (adApproval.getAprEnableInvStockTransfer() == EJBCommon.FALSE) {

                    INV_APPRVL_STATUS = "N/A";

                } else {

                    // check if invoice is self approved

                    LocalAdAmountLimit adAmountLimit = null;

                    try {

                        adAmountLimit = adAmountLimitHome.findByAdcTypeAndAuTypeAndUsrName("INV STOCK TRANSFER",
                                "REQUESTER", details.getStLastModifiedBy(), companyCode);

                    }
                    catch (FinderException ex) {

                        throw new GlobalNoApprovalRequesterFoundException();
                    }

                    if (ABS_TOTAL_AMOUNT <= adAmountLimit.getCalAmountLimit()) {

                        INV_APPRVL_STATUS = "N/A";

                    } else {

                        // for approval, create approval queue

                        Collection adAmountLimits = adAmountLimitHome.findByAdcTypeAndGreaterThanCalAmountLimit(
                                "INV STOCK TRANSFER", adAmountLimit.getCalAmountLimit(), companyCode);

                        if (adAmountLimits.isEmpty()) {

                            Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER",
                                    adAmountLimit.getCalCode(), companyCode);

                            if (adApprovalUsers.isEmpty()) {

                                throw new GlobalNoApprovalApproverFoundException();
                            }

                            for (Object approvalUser : adApprovalUsers) {

                                LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(AD_BRNCH, companyCode,
                                        adApprovalQueueHome, invStockTransfer, adAmountLimit, adApprovalUser);

                                adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                            }

                        } else {

                            boolean isApprovalUsersFound = false;

                            Iterator i = adAmountLimits.iterator();

                            while (i.hasNext()) {

                                LocalAdAmountLimit adNextAmountLimit = (LocalAdAmountLimit) i.next();

                                if (ABS_TOTAL_AMOUNT <= adNextAmountLimit.getCalAmountLimit()) {

                                    Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER",
                                            adAmountLimit.getCalCode(), companyCode);

                                    for (Object approvalUser : adApprovalUsers) {

                                        isApprovalUsersFound = true;

                                        LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                        LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(AD_BRNCH, companyCode,
                                                adApprovalQueueHome, invStockTransfer, adAmountLimit, adApprovalUser);

                                        adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                                    }

                                    break;

                                } else if (!i.hasNext()) {

                                    Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER",
                                            adNextAmountLimit.getCalCode(), companyCode);

                                    for (Object approvalUser : adApprovalUsers) {

                                        isApprovalUsersFound = true;

                                        LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                        LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(AD_BRNCH, companyCode,
                                                adApprovalQueueHome, invStockTransfer, adNextAmountLimit, adApprovalUser);

                                        adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                                    }

                                    break;
                                }

                                adAmountLimit = adNextAmountLimit;
                            }

                            if (!isApprovalUsersFound) {

                                throw new GlobalNoApprovalApproverFoundException();
                            }
                        }

                        INV_APPRVL_STATUS = "PENDING";
                    }
                }
            }

            if (INV_APPRVL_STATUS != null && INV_APPRVL_STATUS.equals("N/A")) {

                this.executeInvStPost(invStockTransfer.getStCode(), invStockTransfer.getStLastModifiedBy(), AD_BRNCH,
                        companyCode, IfDraftToPost);
            }

            // set stock transfer approval status

            invStockTransfer.setStApprovalStatus(INV_APPRVL_STATUS);

            return invStockTransfer.getStCode();

        }
        catch (GlobalRecordAlreadyDeletedException | GlobalRecordInvalidException |
               AdPRFCoaGlVarianceAccountNotFoundException | GlobalBranchAccountNumberInvalidException |
               GlobalInventoryDateException | GlobalJournalNotBalanceException |
               GlJREffectiveDatePeriodClosedException | GlJREffectiveDateNoPeriodExistException |
               GlobalInvItemLocationNotFoundException | GlobalNoApprovalApproverFoundException |
               GlobalNoApprovalRequesterFoundException | GlobalTransactionAlreadyPostedException |
               GlobalTransactionAlreadyPendingException | GlobalTransactionAlreadyApprovedException |
               GlobalDocumentNumberNotUniqueException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteInvStEntry(Integer ST_CODE, String AD_USR, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException {

        Debug.print("InvStockTransferEntryControllerBean deleteInvStEntry");
        try {

            LocalInvStockTransfer invStockTransfer = invStockTransferHome.findByPrimaryKey(ST_CODE);

            for (Object o : invStockTransfer.getInvStockTransferLines()) {

                LocalInvStockTransferLine invStockTransferLine = (LocalInvStockTransferLine) o;

                String invLocation = this.getInvLocNameByLocCode(invStockTransferLine.getStlLocationFrom());

                LocalInvItemLocation invItemLocation = invItemLocationHome.findByLocNameAndIiName(invLocation,
                        invStockTransferLine.getInvItem().getIiName(), companyCode);

                double convertedQuantity = this.convertByUomFromAndItemAndQuantity(
                        invStockTransferLine.getInvUnitOfMeasure(), invItemLocation.getInvItem(),
                        Math.abs(invStockTransferLine.getStlQuantityDelivered()), companyCode);

                invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - convertedQuantity);
            }

            if (invStockTransfer.getStApprovalStatus() != null
                    && invStockTransfer.getStApprovalStatus().equals("PENDING")) {

                Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode(
                        "INV STOCK TRANSFER", invStockTransfer.getStCode(), companyCode);

                for (Object approvalQueue : adApprovalQueues) {

                    LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) approvalQueue;

                    // adApprovalQueue.entityRemove();
                    em.remove(adApprovalQueue);
                }
            }

            adDeleteAuditTrailHome.create("INV STOCK TRANSFER", invStockTransfer.getStDate(),
                    invStockTransfer.getStDocumentNumber(), invStockTransfer.getStReferenceNumber(), 0d, AD_USR,
                    new Date(), companyCode);

            // invStockTransfer.entityRemove();
            em.remove(invStockTransfer);

        }
        catch (FinderException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlobalRecordAlreadyDeletedException();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer companyCode) {

        Debug.print("InvStockTransferEntryControllerBean getGlFcPrecisionUnit");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getInvGpQuantityPrecisionUnit(Integer companyCode) {

        Debug.print("InvStockTransferEntryControllerBean getInvGpQuantityPrecisionUnit");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfInvQuantityPrecisionUnit();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getInvGpInventoryLineNumber(Integer companyCode) {

        Debug.print("InvStockTransferEntryControllerBean getInvGpInventoryLineNumber");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfInvInventoryLineNumber();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdApprovalNotifiedUsersByAdjCode(Integer ST_CODE, Integer companyCode) {

        Debug.print("InvStockTransferEntryControllerBean getAdApprovalNotifiedUsersByAdjCode");
        ArrayList list = new ArrayList();
        try {

            LocalInvStockTransfer invStockTransfer = invStockTransferHome.findByPrimaryKey(ST_CODE);

            if (invStockTransfer.getStPosted() == EJBCommon.TRUE) {

                list.add("DOCUMENT POSTED");
                return list;
            }

            Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode("INV STOCK TRANSFER",
                    ST_CODE, companyCode);

            for (Object approvalQueue : adApprovalQueues) {

                LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) approvalQueue;

                list.add(adApprovalQueue.getAdUser().getUsrDescription());
            }

            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public double getInvIiUnitCostByIiNameAndLocFromAndUomNameAndDate(
            String II_NM, String LOC_FRM, String UOM_NM, Date ST_DT, Integer AD_BRNCH, Integer companyCode)
            throws GlobalInvItemLocationNotFoundException {

        Debug.print("InvStockTransferEntryControllerBean getInvIiUnitCostByIiNameAndLocFromAndUomNameAndDate");
        try {

            LocalInvItemLocation invItemLocation = null;

            try {

                invItemLocation = invItemLocationHome.findByLocNameAndIiName(LOC_FRM, II_NM, companyCode);

            }
            catch (FinderException ex) {

                throw new GlobalInvItemLocationNotFoundException(LOC_FRM + " for item " + II_NM);
            }

            double COST = invItemLocation.getInvItem().getIiUnitCost();

            try {

                LocalInvCosting invCosting = invCostingHome
                        .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(ST_DT,
                                invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(),
                                AD_BRNCH, companyCode);

                switch (invCosting.getInvItemLocation().getInvItem().getIiCostMethod()) {
                    case "Average":

                        if (invCosting.getCstRemainingQuantity() <= 0) {
                            COST = invItemLocation.getInvItem().getIiUnitCost();

                        } else {
                            COST = EJBCommon.roundIt(
                                    Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity()),
                                    this.getGlFcPrecisionUnit(companyCode));
                            if (COST <= 0) {
                                COST = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                            }
                        }

                        break;
                    case "FIFO":
                        COST = this.getInvFifoCost(ST_DT, invItemLocation.getIlCode(), invCosting.getCstAdjustQuantity(),
                                invCosting.getCstAdjustCost(), false, AD_BRNCH, companyCode);
                        break;
                    case "Standard":
                        COST = invItemLocation.getInvItem().getIiUnitCost();
                        break;
                }

            }
            catch (FinderException ex) {

                COST = invItemLocation.getInvItem().getIiUnitCost();
            }
            Debug.print("COST: " + COST);
            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(II_NM, UOM_NM, companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(II_NM, invItemLocation.getInvItem().getInvUnitOfMeasure().getUomName(), companyCode);
            return EJBCommon.roundIt(COST * invDefaultUomConversion.getUmcConversionFactor()
                    / invUnitOfMeasureConversion.getUmcConversionFactor(), this.getGlFcPrecisionUnit(companyCode));

        }
        catch (GlobalInvItemLocationNotFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods
    private String getInvLocNameByLocCode(Integer LOC_CODE) {

        Debug.print("InvStockTransferEntryControllerBean getInvLocNameByLocCode");
        try {

            return invLocationHome.findByPrimaryKey(LOC_CODE).getLocName();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalInvStockTransferLine addInvStlEntry(InvModStockTransferLineDetails mdetails,
                                                     LocalInvStockTransfer invStockTransfer, Integer companyCode) {

        Debug.print("InvStockTransferEntryControllerBean addInvStlEntry");
        try {

            LocalInvLocation invLocFrom = invLocationHome.findByLocName(mdetails.getStlLocationNameFrom(), companyCode);
            LocalInvLocation invLocTo = invLocationHome.findByLocName(mdetails.getStlLocationNameTo(), companyCode);

            LocalInvStockTransferLine invStockTransferLine = invStockTransferLineHome.create(invLocFrom.getLocCode(),
                    invLocTo.getLocCode(), mdetails.getStlUnitCost(), mdetails.getStlQuantityDelivered(),
                    mdetails.getStlAmount(), companyCode);

            invStockTransfer.addInvStockTransferLine(invStockTransferLine);

            LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(mdetails.getStlUomName(),
                    companyCode);

            invUnitOfMeasure.addInvStockTransferLine(invStockTransferLine);

            LocalInvItem invItem = invItemHome.findByIiName(mdetails.getStlIiName(), companyCode);

            invItem.addInvStockTransferLine(invStockTransferLine);

            invStockTransferLine.setStlMisc(mdetails.getStlMisc());

            return invStockTransferLine;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem,
                                                      double ADJST_QTY, Integer companyCode) {

        Debug.print("InvStockTransferEntryControllerBean convertByUomFromAndUomToAndQuantity");
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
                    adPreference.getPrfInvQuantityPrecisionUnit());

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
                    adPreference.getPrfInvQuantityPrecisionUnit());

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void addInvDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE,
                               LocalInvStockTransfer invStockTransfer, Integer AD_BRNCH, Integer companyCode)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("InvStockTransferEntryControllerBean addInvDrEntry");
        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate coa

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, AD_BRNCH, companyCode);

            }
            catch (FinderException ex) {

                throw new GlobalBranchAccountNumberInvalidException();
            }

            // create distribution record

            LocalInvDistributionRecord invDistributionRecord = invDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT,
                    EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE,
                    EJBCommon.FALSE, companyCode);

            invStockTransfer.addInvDistributionRecord(invDistributionRecord);
            glChartOfAccount.addInvDistributionRecord(invDistributionRecord);

        }
        catch (GlobalBranchAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void executeInvStPost(Integer ST_CODE, String USR_NM, Integer AD_BRNCH, Integer companyCode,
                                  boolean IfDraftToPost) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException,
            GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException,
            GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException,
            AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("InvStockTransferEntryControllerBean executeInvStPost");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate if stock transfer is already deleted

            LocalInvStockTransfer invStockTransfer = null;

            try {

                invStockTransfer = invStockTransferHome.findByPrimaryKey(ST_CODE);

            }
            catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if stock transfer is already posted or void

            if (invStockTransfer.getStPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();
            }

            // regenearte inventory dr

            boolean hasInsufficientItems = false;
            String insufficientItems = "";

            if (IfDraftToPost) {
                this.regenerateInventoryDr(invStockTransfer, AD_BRNCH, companyCode);
            }

            Iterator i = invStockTransfer.getInvStockTransferLines().iterator();

            while (i.hasNext()) {

                LocalInvStockTransferLine invStockTransferLine = (LocalInvStockTransferLine) i.next();

                double ST_QTY = this.convertByUomFromAndItemAndQuantity(invStockTransferLine.getInvUnitOfMeasure(),
                        invStockTransferLine.getInvItem(), invStockTransferLine.getStlQuantityDelivered(), companyCode);

                String II_NM = invStockTransferLine.getInvItem().getIiName();

                String LOC_NM_FRM = this.getInvLocNameByLocCode(invStockTransferLine.getStlLocationFrom());

                LocalInvItemLocation invItemLocationFrom = invItemLocationHome.findByLocNameAndIiName(LOC_NM_FRM,
                        invStockTransferLine.getInvItem().getIiName(), companyCode);

                LocalInvCosting invCostingFrom = null;

                try {

                    invCostingFrom = invCostingHome
                            .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                    invStockTransfer.getStDate(), II_NM, LOC_NM_FRM, AD_BRNCH, companyCode);

                }
                catch (FinderException ex) {

                }

                double COST = invStockTransferLine.getInvItem().getIiUnitCost();

                if (invCostingFrom == null) {

                    this.postToInv(invStockTransferLine, invItemLocationFrom, invStockTransfer.getStDate(), -ST_QTY,
                            -COST * ST_QTY, -ST_QTY, -COST * ST_QTY, 0d, null, AD_BRNCH, companyCode);

                } else {

                    this.postToInv(invStockTransferLine, invItemLocationFrom, invStockTransfer.getStDate(), -ST_QTY,
                            -COST * ST_QTY, invCostingFrom.getCstRemainingQuantity() - ST_QTY,
                            invCostingFrom.getCstRemainingValue() - (COST * ST_QTY), 0d, null, AD_BRNCH, companyCode);
                }

                String LOC_NM_TO = this.getInvLocNameByLocCode(invStockTransferLine.getStlLocationTo());

                LocalInvItemLocation invItemLocationTo = invItemLocationHome.findByLocNameAndIiName(LOC_NM_TO,
                        invStockTransferLine.getInvItem().getIiName(), companyCode);

                LocalInvCosting invCostingTo = null;
                try {

                    invCostingTo = invCostingHome
                            .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                    invStockTransfer.getStDate(), II_NM, LOC_NM_TO, AD_BRNCH, companyCode);

                }
                catch (FinderException ex) {

                }

                if (invCostingTo == null) {

                    this.postToInv(invStockTransferLine, invItemLocationTo, invStockTransfer.getStDate(), ST_QTY,
                            COST * ST_QTY, ST_QTY, COST * ST_QTY, 0d, null, AD_BRNCH, companyCode);

                } else {

                    this.postToInv(invStockTransferLine, invItemLocationTo, invStockTransfer.getStDate(), ST_QTY,
                            ST_QTY * COST, invCostingTo.getCstRemainingQuantity() + ST_QTY,
                            invCostingTo.getCstRemainingValue() + (COST * ST_QTY), 0d, USR_NM, AD_BRNCH, companyCode);
                }
            }

            // set invoice post status

            invStockTransfer.setStPosted(EJBCommon.TRUE);
            invStockTransfer.setStPostedBy(USR_NM);
            invStockTransfer.setStDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            // post to GL

            // validate if date has no period and period is closed

            LocalGlSetOfBook glJournalSetOfBook = null;

            try {

                glJournalSetOfBook = glSetOfBookHome.findByDate(invStockTransfer.getStDate(), companyCode);

            }
            catch (FinderException ex) {

                throw new GlJREffectiveDateNoPeriodExistException();
            }

            LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome
                    .findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(),
                            invStockTransfer.getStDate(), companyCode);

            if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C'
                    || glAccountingCalendarValue.getAcvStatus() == 'P') {

                throw new GlJREffectiveDatePeriodClosedException();
            }

            // check if debit and credit is balance

            LocalGlJournalLine glOffsetJournalLine = null;

            Collection invDistributionRecords = invDistributionRecordHome
                    .findImportableDrByStCode(invStockTransfer.getStCode(), companyCode);

            i = invDistributionRecords.iterator();

            double TOTAL_DEBIT = 0d;
            double TOTAL_CREDIT = 0d;

            while (i.hasNext()) {

                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) i.next();

                double DR_AMNT = 0d;

                DR_AMNT = invDistributionRecord.getDrAmount();

                if (invDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                    TOTAL_DEBIT += DR_AMNT;

                } else {

                    TOTAL_CREDIT += DR_AMNT;
                }
            }

            TOTAL_DEBIT = EJBCommon.roundIt(TOTAL_DEBIT, adCompany.getGlFunctionalCurrency().getFcPrecision());
            TOTAL_CREDIT = EJBCommon.roundIt(TOTAL_CREDIT, adCompany.getGlFunctionalCurrency().getFcPrecision());

            if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.TRUE && TOTAL_DEBIT != TOTAL_CREDIT) {

                LocalGlSuspenseAccount glSuspenseAccount = null;

                try {

                    glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("INVENTORY", "STOCK TRANSFERS",
                            companyCode);

                }
                catch (FinderException ex) {

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
                glChartOfAccount.addGlJournalLine(glOffsetJournalLine);

            } else if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE && TOTAL_DEBIT != TOTAL_CREDIT) {

                throw new GlobalJournalNotBalanceException();
            }

            // create journal batch if necessary

            LocalGlJournalBatch glJournalBatch = null;
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");

            try {

                glJournalBatch = glJournalBatchHome.findByJbName(
                        "JOURNAL IMPORT " + formatter.format(new Date()) + " STOCK TRANSFERS", AD_BRNCH, companyCode);

            }
            catch (FinderException ex) {

            }

            if (glJournalBatch == null) {

                glJournalBatch = glJournalBatchHome.create(
                        "JOURNAL IMPORT " + formatter.format(new Date()) + " STOCK TRANSFERS", "JOURNAL IMPORT",
                        "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, AD_BRNCH, companyCode);
            }

            // create journal entry

            LocalGlJournal glJournal = glJournalHome.create(invStockTransfer.getStReferenceNumber(),
                    invStockTransfer.getStDescription(), invStockTransfer.getStDate(), 0.0d, null,
                    invStockTransfer.getStDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE,
                    USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM,
                    EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, EJBCommon.FALSE, null, AD_BRNCH,
                    companyCode);

            LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("INVENTORY", companyCode);
            glJournal.setGlJournalSource(glJournalSource);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome
                    .findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
            glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("STOCK TRANSFERS", companyCode);
            glJournal.setGlJournalCategory(glJournalCategory);

            if (glJournalBatch != null) {

                glJournalBatch.addGlJournal(glJournal);
            }

            // create journal lines

            i = invDistributionRecords.iterator();

            while (i.hasNext()) {

                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) i.next();

                double DR_AMNT = 0d;

                DR_AMNT = invDistributionRecord.getDrAmount();

                LocalGlJournalLine glJournalLine = glJournalLineHome.create(invDistributionRecord.getDrLine(),
                        invDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

                invDistributionRecord.getInvChartOfAccount().addGlJournalLine(glJournalLine);

                glJournal.addGlJournalLine(glJournalLine);

                invDistributionRecord.setDrImported(EJBCommon.TRUE);
            }

            if (glOffsetJournalLine != null) {

                glJournal.addGlJournalLine(glOffsetJournalLine);
            }

            // post journal to gl
            Collection glJournalLines = glJournalLineHome.findByJrCode(glJournal.getJrCode(), companyCode);
            i = glJournalLines.iterator();

            while (i.hasNext()) {

                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) i.next();

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

                Collection glSubsequentSetOfBooks = glSetOfBookHome
                        .findSubsequentSobByAcYear(glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode);

                if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                    adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                    LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome
                            .findByCoaAccountNumberAndBranchCode(adCompany.getCmpRetainedEarnings(), AD_BRNCH,
                                    companyCode);

                    for (Object subsequentSetOfBook : glSubsequentSetOfBooks) {

                        LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) subsequentSetOfBook;

                        String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                        // post to subsequent acvs of subsequent set of book(propagate)

                        Collection glAccountingCalendarValues = glAccountingCalendarValueHome
                                .findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                        for (Object accountingCalendarValue : glAccountingCalendarValues) {

                            LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                            if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY")
                                    || ACCOUNT_TYPE.equals("OWNERS EQUITY")) {

                                this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(),
                                        false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                            } else {
                                // revenue & expense

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
        catch (GlJREffectiveDateNoPeriodExistException | AdPRFCoaGlVarianceAccountNotFoundException |
               GlobalTransactionAlreadyPostedException | GlobalRecordAlreadyDeletedException |
               GlobalJournalNotBalanceException | GlJREffectiveDatePeriodClosedException ex) {

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

    private double getInvFifoCost(Date CST_DT, Integer IL_CODE, double CST_QTY, double CST_COST, boolean isAdjustFifo,
                                  Integer AD_BRNCH, Integer companyCode) {

        try {

            Collection invFifoCostings = invCostingHome
                    .findFifoRemainingQuantityByLessThanOrEqualCstDateAndIlCodeAndBrCode(CST_DT, IL_CODE, AD_BRNCH,
                            companyCode);

            if (invFifoCostings.size() > 0) {

                Iterator x = invFifoCostings.iterator();

                if (isAdjustFifo) {

                    // executed during POST transaction

                    double totalCost = 0d;
                    double cost;

                    if (CST_QTY < 0) {

                        // for negative quantities
                        double neededQty = -(CST_QTY);

                        while (x.hasNext() && neededQty != 0) {

                            LocalInvCosting invFifoCosting = (LocalInvCosting) x.next();

                            if (invFifoCosting.getApPurchaseOrderLine() != null
                                    || invFifoCosting.getApVoucherLineItem() != null) {
                                cost = invFifoCosting.getCstItemCost() / invFifoCosting.getCstQuantityReceived();
                            } else if (invFifoCosting.getArInvoiceLineItem() != null) {
                                cost = invFifoCosting.getCstCostOfSales() / invFifoCosting.getCstQuantitySold();
                            } else {
                                cost = invFifoCosting.getCstAdjustCost() / invFifoCosting.getCstAdjustQuantity();
                            }

                            if (neededQty <= invFifoCosting.getCstRemainingLifoQuantity()) {

                                invFifoCosting.setCstRemainingLifoQuantity(
                                        invFifoCosting.getCstRemainingLifoQuantity() - neededQty);
                                totalCost += (neededQty * cost);
                                neededQty = 0d;
                            } else {

                                neededQty -= invFifoCosting.getCstRemainingLifoQuantity();
                                totalCost += (invFifoCosting.getCstRemainingLifoQuantity() * cost);
                                invFifoCosting.setCstRemainingLifoQuantity(0);
                            }
                        }

                        // if needed qty is not yet satisfied but no more quantities to fetch, get the
                        // default
                        // cost
                        if (neededQty != 0) {

                            LocalInvItemLocation invItemLocation = invItemLocationHome.findByPrimaryKey(IL_CODE);
                            totalCost += (neededQty * invItemLocation.getInvItem().getIiUnitCost());
                        }

                        cost = totalCost / -CST_QTY;
                    } else {

                        // for positive quantities
                        cost = CST_COST;
                    }
                    return cost;
                } else {

                    // executed during ENTRY transaction

                    LocalInvCosting invFifoCosting = (LocalInvCosting) x.next();

                    if (invFifoCosting.getApPurchaseOrderLine() != null
                            || invFifoCosting.getApVoucherLineItem() != null) {
                        return EJBCommon.roundIt(
                                invFifoCosting.getCstItemCost() / invFifoCosting.getCstQuantityReceived(),
                                this.getGlFcPrecisionUnit(companyCode));
                    } else if (invFifoCosting.getArInvoiceLineItem() != null) {
                        return EJBCommon.roundIt(
                                invFifoCosting.getCstCostOfSales() / invFifoCosting.getCstQuantitySold(),
                                this.getGlFcPrecisionUnit(companyCode));
                    } else {
                        return EJBCommon.roundIt(
                                invFifoCosting.getCstAdjustCost() / invFifoCosting.getCstAdjustQuantity(),
                                this.getGlFcPrecisionUnit(companyCode));
                    }
                }
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

    private void postToInv(LocalInvStockTransferLine invStockTransferLine, LocalInvItemLocation invItemLocation,
                           Date CST_DT, double CST_ST_QTY, double CST_ST_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL,
                           double CST_VRNC_VL, String USR_NM, Integer AD_BRNCH, Integer companyCode)
            throws AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("InvStockTransferEntryControllerBean post");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            int CST_LN_NMBR = 0;

            CST_ST_QTY = EJBCommon.roundIt(CST_ST_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_ST_CST = EJBCommon.roundIt(CST_ST_CST, adCompany.getGlFunctionalCurrency().getFcPrecision());
            CST_RMNNG_QTY = EJBCommon.roundIt(CST_RMNNG_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_RMNNG_VL = EJBCommon.roundIt(CST_RMNNG_VL, adCompany.getGlFunctionalCurrency().getFcPrecision());

            if (CST_ST_QTY < 0) {

                invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - Math.abs(CST_ST_QTY));
            }

            try {

                // generate line number
                LocalInvCosting invCurrentCosting = invCostingHome
                        .getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(),
                                invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(),
                                AD_BRNCH, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            }
            catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            // void subsequent cost variance adjustments
            Collection invAdjustmentLines = invAdjustmentLineHome
                    .findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(CST_DT,
                            invItemLocation.getIlCode(), AD_BRNCH, companyCode);
            Iterator i = invAdjustmentLines.iterator();

            while (i.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();
                this.voidInvAdjustment(invAdjustmentLine.getInvAdjustment(), AD_BRNCH, companyCode);
            }

            String prevExpiryDates = "";
            String miscListPrpgt = "";
            double qtyPrpgt = 0;
            try {
                LocalInvCosting prevCst = invCostingHome
                        .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndRemainingQuantityNotEqualToZeroAndIlCode(
                                CST_DT, invItemLocation.getIlCode(), AD_BRNCH, companyCode);

                prevExpiryDates = prevCst.getCstExpiryDate();
                qtyPrpgt = prevCst.getCstRemainingQuantity();

                if (prevExpiryDates == null) {
                    prevExpiryDates = "";
                }

            }
            catch (Exception ex) {

            }

            // create costing
            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d,
                    CST_ST_QTY, CST_ST_CST, 0d, 0d, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d,
                    CST_ST_QTY > 0 ? CST_ST_QTY : 0, AD_BRNCH, companyCode);
            invItemLocation.addInvCosting(invCosting);
            invCosting.setInvStockTransferLine(invStockTransferLine);

            invCosting.setCstQuantity(CST_ST_QTY);
            invCosting.setCstCost(CST_ST_CST);

            // Get Latest Expiry Dates
            if (invStockTransferLine.getInvItem().getIiTraceMisc() != 0) {
                if (prevExpiryDates != null && prevExpiryDates != "" && prevExpiryDates.length() != 0) {
                    System.out
                            .println("apPurchaseOrderLine.getPlMisc(): " + invStockTransferLine.getStlMisc().length());

                    if (invStockTransferLine.getStlMisc() != null && invStockTransferLine.getStlMisc() != ""
                            && invStockTransferLine.getStlMisc().length() != 0) {
                        int qty2Prpgt = Integer
                                .parseInt(this.getQuantityExpiryDates(invStockTransferLine.getStlMisc()));

                        String miscList2Prpgt = this.propagateExpiryDates(invStockTransferLine.getStlMisc(), qty2Prpgt,
                                "False");
                        ArrayList miscList = this.expiryDates(invStockTransferLine.getStlMisc(), qty2Prpgt);
                        String propagateMiscPrpgt = "";
                        StringBuilder ret = new StringBuilder();
                        String check = "";
                        Debug.print("CST_ST_QTY Before Trans: " + CST_ST_QTY);
                        // ArrayList miscList2 = null;
                        if (CST_ST_QTY > 0) {
                            prevExpiryDates = prevExpiryDates.substring(1);
                            propagateMiscPrpgt = miscList2Prpgt + prevExpiryDates;
                        } else {
                            Iterator mi = miscList.iterator();
                            propagateMiscPrpgt = prevExpiryDates;
                            ret = new StringBuilder(propagateMiscPrpgt);
                            String Checker = "";
                            while (mi.hasNext()) {
                                String miscStr = (String) mi.next();

                                ArrayList miscList2 = this.expiryDates("$" + ret, qtyPrpgt);
                                Iterator m2 = miscList2.iterator();
                                ret = new StringBuilder();
                                String ret2 = "false";
                                int a = 0;

                                while (m2.hasNext()) {
                                    String miscStr2 = (String) m2.next();

                                    if (ret2 == "1st") {
                                        ret2 = "false";
                                    }

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
                            // propagateMiscPrpgt = propagateMiscPrpgt.replace(" ", "$");
                            propagateMiscPrpgt = ret.toString();
                            Debug.print("propagateMiscPrpgt: " + propagateMiscPrpgt);
                            if (Checker == "true") {
                                // invCosting.setCstExpiryDate(ret);
                                Debug.print("check: " + check);
                            } else {
                                throw new GlobalExpiryDateNotFoundException();
                            }
                        }
                        invCosting.setCstExpiryDate(propagateMiscPrpgt);

                    } else {
                        invCosting.setCstExpiryDate(prevExpiryDates);
                        Debug.print("prevExpiryDates");
                    }
                } else {
                    if (invStockTransferLine.getStlMisc() != null && invStockTransferLine.getStlMisc() != ""
                            && invStockTransferLine.getStlMisc().length() != 0) {
                        int initialQty = Integer
                                .parseInt(this.getQuantityExpiryDates(invStockTransferLine.getStlMisc()));
                        String initialPrpgt = this.propagateExpiryDates(invStockTransferLine.getStlMisc(), initialQty,
                                "False");

                        invCosting.setCstExpiryDate(initialPrpgt);
                    } else {
                        invCosting.setCstExpiryDate(prevExpiryDates);
                        Debug.print("prevExpiryDates");
                    }
                }
            }

            // if cost variance is not 0, generate cost variance for the transaction
            if (CST_VRNC_VL != 0) {

                this.generateCostVariance(invCosting.getInvItemLocation(), CST_VRNC_VL,
                        "INVST" + invStockTransferLine.getInvStockTransfer().getStDocumentNumber(),
                        invStockTransferLine.getInvStockTransfer().getStDescription(),
                        invStockTransferLine.getInvStockTransfer().getStDate(), USR_NM, AD_BRNCH, companyCode);
            }

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT,
                    invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH,
                    companyCode);

            i = invCostings.iterator();

            String miscList = "";
            ArrayList miscList2 = null;
            if (invStockTransferLine.getStlMisc() != null && invStockTransferLine.getStlMisc() != ""
                    && invStockTransferLine.getStlMisc().length() != 0) {
                double qty = Double.parseDouble(this.getQuantityExpiryDates(invStockTransferLine.getStlMisc()));
                miscList = this.propagateExpiryDates(invStockTransferLine.getStlMisc(), qty, "False");
                miscList2 = this.expiryDates(invStockTransferLine.getStlMisc(), qty);
            }

            Debug.print("miscList Propagate:" + miscList);
            String propagateMisc = "";
            StringBuilder ret = new StringBuilder();
            String Checker = "";

            Debug.print("CST_ST_QTY: " + CST_ST_QTY);

            while (i.hasNext()) {

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) i.next();

                invPropagatedCosting
                        .setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_ST_QTY);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_ST_CST);
                if (invStockTransferLine.getInvItem().getIiTraceMisc() != 0) {
                    if (invStockTransferLine.getStlMisc() != null && invStockTransferLine.getStlMisc() != ""
                            && invStockTransferLine.getStlMisc().length() != 0) {
                        if (CST_ST_QTY > 0) {
                            miscList = miscList.substring(1);
                            propagateMisc = invPropagatedCosting.getCstExpiryDate() + miscList;
                            Debug.print("propagateMiscPrpgt : " + propagateMisc);
                        } else {
                            Iterator mi = miscList2.iterator();

                            propagateMisc = prevExpiryDates;
                            ret = new StringBuilder(propagateMisc);
                            while (mi.hasNext()) {
                                String miscStr = (String) mi.next();
                                ArrayList miscList3 = this.expiryDates("$" + ret, qtyPrpgt);
                                Iterator m2 = miscList3.iterator();
                                ret = new StringBuilder();
                                String ret2 = "false";
                                int a = 0;
                                while (m2.hasNext()) {
                                    String miscStr2 = (String) m2.next();

                                    if (ret2 == "1st") {
                                        ret2 = "false";
                                    }

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
                            Debug.print("propagateMiscPrpgt: " + propagateMisc);

                            if (Checker == "true") {
                                // invPropagatedCosting.setCstExpiryDate(propagateMisc);
                            } else {
                                throw new GlobalExpiryDateNotFoundException();
                            }
                        }

                        invPropagatedCosting.setCstExpiryDate(propagateMisc);
                    } else {
                        invPropagatedCosting.setCstExpiryDate(prevExpiryDates);
                        Debug.print("prevExpiryDates");
                    }
                }
            }

            // regenerate cost varaince
            this.regenerateCostVariance(invCostings, invCosting, AD_BRNCH, companyCode);

        }
        catch (AdPRFCoaGlVarianceAccountNotFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
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
        Debug.print("Y " + y);

        return y;
    }

    private ArrayList expiryDates(String misc, double qty) throws Exception {

        Debug.print("ApReceivingItemControllerBean getExpiryDates");
        Debug.print("misc: " + misc);
        String separator = "$";

        // Remove first $ character
        misc = misc.substring(1);

        // Counter
        int start = 0;
        int nextIndex = misc.indexOf(separator, start);
        int length = nextIndex - start;

        Debug.print("qty" + qty);
        ArrayList miscList = new ArrayList();

        for (int x = 0; x < qty; x++) {
            try {
                // Date
                start = nextIndex + 1;
                nextIndex = misc.indexOf(separator, start);
                length = nextIndex - start;
                Debug.print("x" + x);
                String checker = misc.substring(start, start + length);
                Debug.print("checker" + checker);
                if (checker.length() != 0 || checker != "null") {
                    miscList.add(checker);
                } else {
                    miscList.add("null");
                }
            }
            catch (Exception ex) {

            }
        }

        Debug.print("miscList :" + miscList);
        return miscList;
    }

    public String propagateExpiryDates(String misc, double qty, String reverse) throws Exception {
        // ActionErrors errors = new ActionErrors();
        Debug.print("ApReceivingItemControllerBean getExpiryDates");
        Debug.print("misc: " + misc);

        String separator = "";
        if (reverse == "False") {
            separator = "$";
        } else {
            separator = " ";
        }

        // Remove first $ character
        misc = misc.substring(1);
        Debug.print("misc: " + misc);
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
            Debug.print("g: " + g);
            Debug.print("g length: " + g.length());
            if (g.length() != 0) {
                miscList.append("$").append(g);
                Debug.print("miscList G: " + miscList);
            }
        }

        miscList.append("$");
        Debug.print("miscList :" + miscList);
        return (miscList.toString());
    }

    //TODO: This is a redundant function across OFS systems. Do a review and refactor.
    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue,
                          LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double JL_AMNT,
                          Integer companyCode) {

        Debug.print("InvStockTransferEntryControllerBean postToGl");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccountBalance glChartOfAccountBalance = glChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(
                    glAccountingCalendarValue.getAcvCode(), glChartOfAccount.getCoaCode(), companyCode);

            String ACCOUNT_TYPE = glChartOfAccount.getCoaAccountType();
            short FC_EXTNDD_PRCSN = adCompany.getGlFunctionalCurrency().getFcPrecision();

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

    private void regenerateInventoryDr(LocalInvStockTransfer invStockTransfer, Integer AD_BRNCH, Integer companyCode)
            throws GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException {

        Debug.print("InvStockTransferEntryControllerBean regenerateInventoryDr");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            // regenerate inventory distribution records

            Collection invDistributionRecords = invDistributionRecordHome
                    .findImportableDrByStCode(invStockTransfer.getStCode(), companyCode);

            Iterator i = invDistributionRecords.iterator();

            while (i.hasNext()) {

                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) i.next();

                if (invDistributionRecord.getDrClass().equals("INVENTORY")) {

                    i.remove();
                    // invDistributionRecord.entityRemove();
                    em.remove(invDistributionRecord);
                }
            }

            Collection invStockTransferLines = invStockTransfer.getInvStockTransferLines();

            i = invStockTransferLines.iterator();

            while (i.hasNext()) {

                LocalInvStockTransferLine invStockTransferLine = (LocalInvStockTransferLine) i.next();

                LocalInvLocation invLocFrom = null;
                LocalInvLocation invLocTo = null;

                try {

                    invLocFrom = invLocationHome.findByPrimaryKey(invStockTransferLine.getStlLocationFrom());
                    invLocTo = invLocationHome.findByPrimaryKey(invStockTransferLine.getStlLocationTo());

                }
                catch (FinderException ex) {

                }

                LocalInvItemLocation invItemLocFrom = null;
                LocalInvItemLocation invItemLocTo = null;

                try {

                    invItemLocFrom = invItemLocationHome.findByLocNameAndIiName(invLocFrom.getLocName(),
                            invStockTransferLine.getInvItem().getIiName(), companyCode);

                }
                catch (FinderException ex) {

                    throw new GlobalInvItemLocationNotFoundException(
                            invStockTransferLine.getInvItem().getIiName() + " - " + invLocFrom.getLocName());
                }

                try {

                    invItemLocTo = invItemLocationHome.findByLocNameAndIiName(invLocTo.getLocName(),
                            invStockTransferLine.getInvItem().getIiName(), companyCode);

                }
                catch (FinderException ex) {

                    throw new GlobalInvItemLocationNotFoundException(
                            invStockTransferLine.getInvItem().getIiName() + " - " + invLocTo.getLocName());
                }

                // start date validation
                if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                    Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(
                            invStockTransfer.getStDate(), invItemLocFrom.getInvItem().getIiName(),
                            invItemLocFrom.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                    if (!invNegTxnCosting.isEmpty()) {
                        throw new GlobalInventoryDateException(invItemLocFrom.getInvItem().getIiName());
                    }

                    invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(
                            invStockTransfer.getStDate(), invItemLocTo.getInvItem().getIiName(),
                            invItemLocTo.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                    if (!invNegTxnCosting.isEmpty()) {
                        throw new GlobalInventoryDateException(invItemLocTo.getInvItem().getIiName());
                    }
                }
                // add physical inventory distribution

                double COST = this.getInvIiUnitCostByIiNameAndLocFromAndUomNameAndDate(
                        invItemLocFrom.getInvItem().getIiName(), invItemLocFrom.getInvLocation().getLocName(),
                        invStockTransferLine.getInvUnitOfMeasure().getUomName(), invStockTransfer.getStDate(), AD_BRNCH,
                        companyCode);

                double AMOUNT = 0d;

                AMOUNT = EJBCommon.roundIt(invStockTransferLine.getStlQuantityDelivered() * COST,
                        this.getGlFcPrecisionUnit(companyCode));

                // dr to locationTo

                // check branch mapping

                LocalAdBranchItemLocation adBranchItemLocation = null;

                try {

                    adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(invItemLocTo.getIlCode(),
                            AD_BRNCH, companyCode);

                }
                catch (FinderException ex) {

                }

                LocalGlChartOfAccount glChartOfAccountTo = null;

                if (adBranchItemLocation == null) {

                    glChartOfAccountTo = glChartOfAccountHome
                            .findByPrimaryKey(invItemLocTo.getIlGlCoaInventoryAccount());

                } else {

                    glChartOfAccountTo = glChartOfAccountHome
                            .findByPrimaryKey(adBranchItemLocation.getBilCoaGlInventoryAccount());
                }

                this.addInvDrEntry(invStockTransfer.getInvDrNextLine(), "INVENTORY", EJBCommon.TRUE, Math.abs(AMOUNT),
                        glChartOfAccountTo.getCoaCode(), invStockTransfer, AD_BRNCH, companyCode);

                // cr to locationFrom

                // check branch mapping

                try {

                    adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(invItemLocFrom.getIlCode(),
                            AD_BRNCH, companyCode);

                }
                catch (FinderException ex) {

                }

                LocalGlChartOfAccount glChartOfAccountFrom = null;

                if (adBranchItemLocation == null) {

                    glChartOfAccountFrom = glChartOfAccountHome
                            .findByPrimaryKey(invItemLocFrom.getIlGlCoaInventoryAccount());

                } else {

                    glChartOfAccountFrom = glChartOfAccountHome
                            .findByPrimaryKey(adBranchItemLocation.getBilCoaGlInventoryAccount());
                }

                this.addInvDrEntry(invStockTransfer.getInvDrNextLine(), "INVENTORY", EJBCommon.FALSE, Math.abs(AMOUNT),
                        glChartOfAccountFrom.getCoaCode(), invStockTransfer, AD_BRNCH, companyCode);
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

    private void voidInvAdjustment(LocalInvAdjustment invAdjustment, Integer AD_BRNCH, Integer companyCode) {

        Debug.print("InvStockTransferEntryControllerBean voidInvAdjustment");
        try {

            Collection invDistributionRecords = invAdjustment.getInvDistributionRecords();
            ArrayList list = new ArrayList();

            Iterator i = invDistributionRecords.iterator();

            while (i.hasNext()) {

                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) i.next();

                list.add(invDistributionRecord);
            }

            i = list.iterator();

            while (i.hasNext()) {

                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) i.next();

                this.addInvDrEntry(invAdjustment.getInvDrNextLine(), invDistributionRecord.getDrClass(),
                        invDistributionRecord.getDrDebit() == EJBCommon.TRUE ? EJBCommon.FALSE : EJBCommon.TRUE,
                        invDistributionRecord.getDrAmount(), EJBCommon.TRUE,
                        invDistributionRecord.getInvChartOfAccount().getCoaCode(), invAdjustment, AD_BRNCH, companyCode);
            }

            Collection invAdjustmentLines = invAdjustment.getInvAdjustmentLines();
            i = invAdjustmentLines.iterator();
            list.clear();

            while (i.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();

                list.add(invAdjustmentLine);
            }

            i = list.iterator();

            while (i.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();

                this.addInvAlEntry(invAdjustmentLine.getInvItemLocation(), invAdjustment,
                        (invAdjustmentLine.getAlUnitCost()) * -1, EJBCommon.TRUE, companyCode);
            }

            invAdjustment.setAdjVoid(EJBCommon.TRUE);

            this.executeInvAdjPost(invAdjustment.getAdjCode(), invAdjustment.getAdjLastModifiedBy(), AD_BRNCH,
                    companyCode);

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void generateCostVariance(LocalInvItemLocation invItemLocation, double CST_VRNC_VL, String ADJ_RFRNC_NMBR,
                                      String ADJ_DSCRPTN, Date ADJ_DT, String USR_NM, Integer AD_BRNCH, Integer companyCode)
            throws AdPRFCoaGlVarianceAccountNotFoundException {

    }

    private void regenerateCostVariance(Collection invCostings, LocalInvCosting invCosting, Integer AD_BRNCH,
                                        Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

    }

    private void addInvDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, byte DR_RVRSL,
                               Integer COA_CODE, LocalInvAdjustment invAdjustment, Integer AD_BRNCH, Integer companyCode)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("InvStockTransferEntryControllerBean addInvDrEntry");
        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate coa

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, AD_BRNCH, companyCode);

            }
            catch (FinderException ex) {

                throw new GlobalBranchAccountNumberInvalidException();
            }

            // create distribution record

            LocalInvDistributionRecord invDistributionRecord = invDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT,
                    EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), DR_RVRSL,
                    EJBCommon.FALSE, companyCode);

            invAdjustment.addInvDistributionRecord(invDistributionRecord);
            glChartOfAccount.addInvDistributionRecord(invDistributionRecord);

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

    private void executeInvAdjPost(Integer ADJ_CODE, String USR_NM, Integer AD_BRNCH, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException,
            GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException,
            GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException {

        Debug.print("InvStockTransferEntryControllerBean executeInvAdjPost");
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

            Collection invAdjustmentLines = null;

            if (invAdjustment.getAdjVoid() == EJBCommon.FALSE) {
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.FALSE,
                        invAdjustment.getAdjCode(), companyCode);
            } else {
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.TRUE,
                        invAdjustment.getAdjCode(), companyCode);
            }

            Iterator i = invAdjustmentLines.iterator();

            while (i.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();

                LocalInvCosting invCosting = invCostingHome
                        .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                invAdjustment.getAdjDate(),
                                invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(),
                                invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName(), AD_BRNCH,
                                companyCode);

                this.postInvAdjustmentToInventory(invAdjustmentLine, invAdjustment.getAdjDate(), 0,
                        invAdjustmentLine.getAlUnitCost(), invCosting.getCstRemainingQuantity(),
                        invCosting.getCstRemainingValue() + invAdjustmentLine.getAlUnitCost(), AD_BRNCH, companyCode);
            }

            // post to gl if necessary

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

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

                invDistributionRecords = invDistributionRecordHome
                        .findImportableDrByDrReversedAndAdjCode(EJBCommon.FALSE, invAdjustment.getAdjCode(), companyCode);

            } else {

                invDistributionRecords = invDistributionRecordHome
                        .findImportableDrByDrReversedAndAdjCode(EJBCommon.TRUE, invAdjustment.getAdjCode(), companyCode);
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
                }
            }

            TOTAL_DEBIT = EJBCommon.roundIt(TOTAL_DEBIT, adCompany.getGlFunctionalCurrency().getFcPrecision());
            TOTAL_CREDIT = EJBCommon.roundIt(TOTAL_CREDIT, adCompany.getGlFunctionalCurrency().getFcPrecision());

            if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.TRUE && TOTAL_DEBIT != TOTAL_CREDIT) {

                LocalGlSuspenseAccount glSuspenseAccount = null;

                try {

                    glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("INVENTORY",
                            "INVENTORY ADJUSTMENTS", companyCode);

                }
                catch (FinderException ex) {

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
                glChartOfAccount.addGlJournalLine(glOffsetJournalLine);

            } else if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE && TOTAL_DEBIT != TOTAL_CREDIT) {

                throw new GlobalJournalNotBalanceException();
            }

            // create journal batch if necessary

            LocalGlJournalBatch glJournalBatch = null;
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");

            try {

                glJournalBatch = glJournalBatchHome.findByJbName(
                        "JOURNAL IMPORT " + formatter.format(new Date()) + " INVENTORY ADJUSTMENTS", AD_BRNCH,
                        companyCode);

            }
            catch (FinderException ex) {

            }

            if (glJournalBatch == null) {

                glJournalBatch = glJournalBatchHome.create(
                        "JOURNAL IMPORT " + formatter.format(new Date()) + " INVENTORY ADJUSTMENTS", "JOURNAL IMPORT",
                        "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, AD_BRNCH, companyCode);
            }

            // create journal entry

            LocalGlJournal glJournal = glJournalHome.create(invAdjustment.getAdjReferenceNumber(),
                    invAdjustment.getAdjDescription(), invAdjustment.getAdjDate(), 0.0d, null,
                    invAdjustment.getAdjDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE,
                    USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM,
                    EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, EJBCommon.FALSE, null, AD_BRNCH,
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

                invDistributionRecord.getInvChartOfAccount().addGlJournalLine(glJournalLine);

                glJournal.addGlJournalLine(glJournalLine);

                invDistributionRecord.setDrImported(EJBCommon.TRUE);
            }

            if (glOffsetJournalLine != null) {

                glJournal.addGlJournalLine(glOffsetJournalLine);
            }

            // post journal to gl
            Collection glJournalLines = glJournalLineHome.findByJrCode(glJournal.getJrCode(), companyCode);
            i = glJournalLines.iterator();

            while (i.hasNext()) {

                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) i.next();

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

                Collection glSubsequentSetOfBooks = glSetOfBookHome
                        .findSubsequentSobByAcYear(glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode);

                if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                    adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                    LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome
                            .findByCoaAccountNumberAndBranchCode(adCompany.getCmpRetainedEarnings(), AD_BRNCH,
                                    companyCode);

                    for (Object subsequentSetOfBook : glSubsequentSetOfBooks) {

                        LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) subsequentSetOfBook;

                        String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                        // post to subsequent acvs of subsequent set of book(propagate)

                        Collection glAccountingCalendarValues = glAccountingCalendarValueHome
                                .findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                        for (Object accountingCalendarValue : glAccountingCalendarValues) {

                            LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                            if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY")
                                    || ACCOUNT_TYPE.equals("OWNERS EQUITY")) {

                                this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(),
                                        false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

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

            invAdjustment.setAdjPosted(EJBCommon.TRUE);

        }
        catch (GlJREffectiveDateNoPeriodExistException | GlobalTransactionAlreadyPostedException |
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

    private LocalInvAdjustmentLine addInvAlEntry(LocalInvItemLocation invItemLocation, LocalInvAdjustment invAdjustment,
                                                 double CST_VRNC_VL, byte AL_VD, Integer companyCode) {

        Debug.print("InvStockTransferEntryControllerBean addInvAlEntry");
        try {

            // create dr entry
            LocalInvAdjustmentLine invAdjustmentLine = null;
            invAdjustmentLine = invAdjustmentLineHome.create(CST_VRNC_VL, null, null, 0, 0, AL_VD, companyCode);

            // map adjustment, unit of measure, item location
            invAdjustment.addInvAdjustmentLine(invAdjustmentLine);
            invItemLocation.getInvItem().getInvUnitOfMeasure().addInvAdjustmentLine(invAdjustmentLine);
            invItemLocation.addInvAdjustmentLine(invAdjustmentLine);

            return invAdjustmentLine;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postInvAdjustmentToInventory(LocalInvAdjustmentLine invAdjustmentLine, Date CST_DT,
                                              double CST_ADJST_QTY, double CST_ADJST_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, Integer AD_BRNCH,
                                              Integer companyCode) {

        Debug.print("InvStockTransferEntryControllerBean postInvAdjustmentToInventory");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalInvItemLocation invItemLocation = invAdjustmentLine.getInvItemLocation();
            int CST_LN_NMBR = 0;

            CST_ADJST_QTY = EJBCommon.roundIt(CST_ADJST_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_ADJST_CST = EJBCommon.roundIt(CST_ADJST_CST, adCompany.getGlFunctionalCurrency().getFcPrecision());
            CST_RMNNG_QTY = EJBCommon.roundIt(CST_RMNNG_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_RMNNG_VL = EJBCommon.roundIt(CST_RMNNG_VL, adCompany.getGlFunctionalCurrency().getFcPrecision());

            if (CST_ADJST_QTY < 0) {

                invItemLocation
                        .setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - Math.abs(CST_ADJST_QTY));
            }

            // create costing

            try {

                // generate line number

                LocalInvCosting invCurrentCosting = invCostingHome
                        .getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(),
                                invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(),
                                AD_BRNCH, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            }
            catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d,
                    CST_ADJST_QTY, CST_ADJST_CST, 0d, 0d, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d,
                    CST_ADJST_QTY > 0 ? CST_ADJST_QTY : 0, AD_BRNCH, companyCode);
            invItemLocation.addInvCosting(invCosting);
            invCosting.setInvAdjustmentLine(invAdjustmentLine);

            invCosting.setCstQuantity(CST_ADJST_QTY);
            invCosting.setCstCost(CST_ADJST_CST);

            // propagate balance if necessary

            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT,
                    invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH,
                    companyCode);

            for (Object costing : invCostings) {

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) costing;

                invPropagatedCosting
                        .setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_ADJST_QTY);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_ADJST_CST);
            }

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalAdApprovalQueue createApprovalQueue(Integer branchCode, Integer companyCode,
                                                     LocalAdApprovalQueueHome adApprovalQueueHome,
                                                     LocalInvStockTransfer invStockTransfer,
                                                     LocalAdAmountLimit adAmountLimit, LocalAdApprovalUser adApprovalUser) throws CreateException {

        return adApprovalQueueHome
                .AqForApproval(EJBCommon.TRUE)
                .AqDocument("INV STOCK TRANSFER")
                .AqDocumentCode(invStockTransfer.getStCode())
                .AqDocumentNumber(invStockTransfer.getStDocumentNumber())
                .AqDate(invStockTransfer.getStDate())
                .AqAndOr(adAmountLimit.getCalAndOr())
                .AqUserOr(adApprovalUser.getAuOr())
                .AqAdBranch(branchCode)
                .AqAdCompany(companyCode)
                .buildApprovalQueue();
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("InvStockTransferEntryControllerBean ejbCreate");
    }

}