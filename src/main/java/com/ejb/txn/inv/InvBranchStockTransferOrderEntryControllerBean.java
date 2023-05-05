/**
 * @copyright 2022 Omega Business Consulting, Inc.
 * @class InvBranchStockTransferOrderEntryControllerBean
 * @created
 * @author KenjiGella
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
import com.util.EJBHomeFactory;
import com.util.mod.inv.InvModBranchStockTransferDetails;
import com.util.mod.inv.InvModBranchStockTransferLineDetails;
import com.util.mod.inv.InvModTagListDetails;
import com.util.mod.inv.InvModUnitOfMeasureDetails;

import jakarta.ejb.*;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Stateless(name = "InvBranchStockTransferOrderEntryControllerEJB")
public class InvBranchStockTransferOrderEntryControllerBean extends EJBContextClass
        implements InvBranchStockTransferOrderEntryController {

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
    private LocalInvBranchStockTransferHome invBranchStockTransferHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalInvUnitOfMeasureHome invBranchUnitOfMeasureHome;
    @EJB
    private LocalInvItemHome invBranchItemHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
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
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalAdDeleteAuditTrailHome adDeleteAuditTrailHome;
    @EJB
    private LocalInvBranchStockTransferLineHome invBranchStockTransferLineHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
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
    private LocalInvTagHome invTagHome;

    public InvModBranchStockTransferDetails getInvBstByBstCode(Integer BST_CODE, Integer companyCode)
            throws GlobalNoRecordFoundException {

        Debug.print("InvBranchStockTransferOrderEntryControllerBean getInvBstByBstCode");
        try {

            LocalInvBranchStockTransfer invBranchStockTransfer = null;

            try {

                invBranchStockTransfer = invBranchStockTransferHome.findByPrimaryKey(BST_CODE);

            }
            catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList bslList = new ArrayList();

            // branch stock transfer lines
            for (Object o : invBranchStockTransfer.getInvBranchStockTransferLines()) {

                LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) o;

                InvModBranchStockTransferLineDetails mdetails = new InvModBranchStockTransferLineDetails();

                mdetails.setBslCode(invBranchStockTransferLine.getBslCode());
                mdetails.setBslIiName(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName());
                mdetails.setBslIiDescription(
                        invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiDescription());
                mdetails.setBslLocationName(
                        invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName());
                mdetails.setBslUomName(invBranchStockTransferLine.getInvUnitOfMeasure().getUomName());
                mdetails.setBslUnitCost(invBranchStockTransferLine.getBslUnitCost());
                mdetails.setBslQuantity(invBranchStockTransferLine.getBslQuantity());
                mdetails.setBslAmount(invBranchStockTransferLine.getBslAmount());
                // mdetails.setBslMisc(invBranchStockTransferLine.getBslMisc().trim());
                System.out
                        .println("invBranchStockTransferLine.getBslMisc(): " + invBranchStockTransferLine.getBslMisc());

                mdetails.setTraceMisc(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiTraceMisc());
                System.out
                        .println(mdetails.getTraceMisc() + "<== getTraceMisc under getInvAdjByAdjCode controllerbean");
                if (mdetails.getTraceMisc() == 1) {

                    ArrayList tagList = new ArrayList();

                    tagList = this.getInvTagList(invBranchStockTransferLine);
                    mdetails.setBslTagList(tagList);
                    mdetails.setTraceMisc(mdetails.getTraceMisc());
                }

                bslList.add(mdetails);
            }

            InvModBranchStockTransferDetails details = new InvModBranchStockTransferDetails();

            details.setBstCode(invBranchStockTransfer.getBstCode());
            details.setBstType(invBranchStockTransfer.getBstType());
            details.setBstNumber(invBranchStockTransfer.getBstNumber());
            details.setBstDescription(invBranchStockTransfer.getBstDescription());
            details.setBstVoid(invBranchStockTransfer.getBstVoid());
            details.setBstDate(invBranchStockTransfer.getBstDate());
            details.setBstBranchFrom(invBranchStockTransfer.getAdBranch().getBrBranchCode());
            Debug.print("bstBranchFrom : " + details.getBstBranchFrom());
            details.setBstTransitLocation(invBranchStockTransfer.getInvLocation().getLocName());

            details.setBstApprovalStatus(invBranchStockTransfer.getBstApprovalStatus());
            details.setBstPosted(invBranchStockTransfer.getBstPosted());
            details.setBstCreatedBy(invBranchStockTransfer.getBstCreatedBy());
            details.setBstDateCreated(invBranchStockTransfer.getBstDateCreated());
            details.setBstLastModifiedBy(invBranchStockTransfer.getBstLastModifiedBy());
            details.setBstDateLastModified(invBranchStockTransfer.getBstDateLastModified());
            details.setBstApprovedRejectedBy(invBranchStockTransfer.getBstApprovedRejectedBy());
            details.setBstDateApprovedRejected(invBranchStockTransfer.getBstDateApprovedRejected());
            details.setBstPostedBy(invBranchStockTransfer.getBstPostedBy());
            details.setBstDatePosted(invBranchStockTransfer.getBstDatePosted());
            details.setBstReasonForRejection(invBranchStockTransfer.getBstReasonForRejection());

            details.setBstBtlList(bslList);

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

    public ArrayList getInvBranchUomByIiName(String II_NM, Integer companyCode) {

        Debug.print("InvBranchStockTransferOrderEntryControllerBean getInvBranchUomByIiName");
        ArrayList list = new ArrayList();
        try {

            LocalInvItem invBranchItem = null;
            LocalInvUnitOfMeasure invBranchItemUnitOfMeasure = null;

            invBranchItem = invBranchItemHome.findByIiName(II_NM, companyCode);
            invBranchItemUnitOfMeasure = invBranchItem.getInvUnitOfMeasure();

            for (Object o : invBranchUnitOfMeasureHome
                    .findByUomAdLvClass(invBranchItemUnitOfMeasure.getUomAdLvClass(), companyCode)) {

                LocalInvUnitOfMeasure invBranchUnitOfMeasure = (LocalInvUnitOfMeasure) o;

                try {
                    LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome
                            .findUmcByIiNameAndUomName(II_NM, invBranchUnitOfMeasure.getUomName(), companyCode);
                    if (invUnitOfMeasureConversion.getUmcBaseUnit() == EJBCommon.FALSE
                            && invUnitOfMeasureConversion.getUmcConversionFactor() == 1) {
                        continue;
                    }
                }
                catch (FinderException ex) {
                    continue;
                }

                InvModUnitOfMeasureDetails details = new InvModUnitOfMeasureDetails();
                details.setUomName(invBranchUnitOfMeasure.getUomName());

                if (invBranchUnitOfMeasure.getUomName().equals(invBranchItemUnitOfMeasure.getUomName())) {

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

    public Integer saveInvBstEntry(InvModBranchStockTransferDetails details, ArrayList bslList,
                                   boolean isDraft, Integer AD_BRNCH, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException,
            GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException,
            GlobalTransactionAlreadyPostedException, GlobalNoApprovalRequesterFoundException,
            GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException,
            GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException,
            GlobalJournalNotBalanceException, GlobalDocumentNumberNotUniqueException,
            GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException,
            GlobalRecordInvalidException, GlobalNoRecordFoundException, GlobalExpiryDateNotFoundException,
            GlobalMiscInfoIsRequiredException {

        Debug.print("InvBranchStockTransferOrderEntryControllerBean saveInvBstEntry");

        // TODO: Start here.
        Date txnStartDate = new Date();
        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            LocalInvBranchStockTransfer invBranchStockTransfer = null;

            // validate if branch stock transfer is already deleted
            try {

                if (details.getBstCode() != null) {

                    invBranchStockTransfer = invBranchStockTransferHome.findByPrimaryKey(details.getBstCode());

                    if (details.getBstVoid() == EJBCommon.TRUE) {

                        invBranchStockTransfer.setBstVoid(EJBCommon.TRUE);
                        return invBranchStockTransfer.getBstCode();
                    }
                }

            }
            catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }
            Debug.print("InvBranchStockTransferOrderEntryControllerBean saveInvBstEntry A");
            // validate if branch stock transfer is already posted, void, approved or
            // pending
            if (details.getBstCode() != null) {

                if (invBranchStockTransfer.getBstApprovalStatus() != null) {

                    if (invBranchStockTransfer.getBstApprovalStatus().equals("APPROVED")
                            || invBranchStockTransfer.getBstApprovalStatus().equals("N/A")) {

                        throw new GlobalTransactionAlreadyApprovedException();

                    } else if (invBranchStockTransfer.getBstApprovalStatus().equals("PENDING")) {

                        throw new GlobalTransactionAlreadyPendingException();
                    }
                }

                if (invBranchStockTransfer.getBstPosted() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyPostedException();
                }
            }

            LocalInvBranchStockTransfer invBranchExistingStockTransfer = null;

            try {

                invBranchExistingStockTransfer = invBranchStockTransferHome
                        .findByBstNumberAndBrCode(details.getBstNumber(), AD_BRNCH, companyCode);
                Debug.print("InvBranchStockTransferOrderEntryControllerBean saveInvBstEntry A01");
            }
            catch (FinderException ex) {

            }

            // validate if document number is unique and if document number is automatic
            // then set next
            // sequence
            Debug.print("InvBranchStockTransferOrderEntryControllerBean saveInvBstEntry B");
            if (details.getBstCode() == null) {

                LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
                LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

                if (invBranchExistingStockTransfer != null) {

                    throw new GlobalDocumentNumberNotUniqueException();
                }

                try {

                    adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome
                            .findByDcName("INV BRANCH STOCK TRANSFER ORDER", companyCode);

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
                        && (details.getBstNumber() == null || details.getBstNumber().trim().length() == 0)) {

                    while (true) {

                        if (adBranchDocumentSequenceAssignment == null
                                || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                            try {

                                invBranchStockTransferHome.findByBstNumberAndBrCode(
                                        adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, companyCode);
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon
                                        .incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                            }
                            catch (FinderException ex) {

                                details.setBstNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon
                                        .incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                break;
                            }

                        } else {

                            try {

                                invBranchStockTransferHome.findByBstNumberAndBrCode(
                                        adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, companyCode);
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(
                                        adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                            }
                            catch (FinderException ex) {

                                details.setBstNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(
                                        adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                break;
                            }
                        }
                    }
                }
                Debug.print("InvBranchStockTransferOrderEntryControllerBean saveInvBstEntry B01");
            } else {

                if (invBranchExistingStockTransfer != null
                        && !invBranchExistingStockTransfer.getBstCode().equals(details.getBstCode())) {

                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (invBranchStockTransfer.getBstNumber() != details.getBstNumber()
                        && (details.getBstNumber() == null || details.getBstNumber().trim().length() == 0)) {

                    details.setBstNumber(invBranchStockTransfer.getBstNumber());
                }
                Debug.print("InvBranchStockTransferOrderEntryControllerBean saveInvBstEntry B02");
            }

            // used in checking if branch stock transfer should re-generate distribution
            // records

            boolean isRecalculate = true;

            // create branch stock transfer
            Debug.print("InvBranchStockTransferOrderEntryControllerBean saveInvBstEntry C");
            if (details.getBstCode() == null) {

                if (details.getBstDescription().contains("UPLOAD") || isDraft) {
                    Debug.print("--Uploader--");

                    invBranchStockTransfer = invBranchStockTransferHome.create(details.getBstDate(),
                            details.getBstType(), details.getBstNumber(), null, null, details.getBstDescription(),
                            details.getBstApprovalStatus(), details.getBstPosted(), details.getBstReasonForRejection(),
                            details.getBstCreatedBy(), details.getBstDateCreated(), details.getBstLastModifiedBy(),
                            details.getBstDateLastModified(), details.getBstApprovedRejectedBy(),
                            details.getBstDateApprovedRejected(), details.getBstPostedBy(), details.getBstDatePosted(),
                            EJBCommon.FALSE, details.getBstVoid(), AD_BRNCH, companyCode);
                } else {
                    invBranchStockTransfer = invBranchStockTransferHome.create(details.getBstDate(), "ORDER",
                            details.getBstNumber(), null, null, details.getBstDescription(),
                            details.getBstApprovalStatus(), details.getBstPosted(), details.getBstReasonForRejection(),
                            details.getBstCreatedBy(), details.getBstDateCreated(), details.getBstLastModifiedBy(),
                            details.getBstDateLastModified(), details.getBstApprovedRejectedBy(),
                            details.getBstDateApprovedRejected(), details.getBstPostedBy(), details.getBstDatePosted(),
                            EJBCommon.FALSE, details.getBstVoid(), AD_BRNCH, companyCode);
                }

            } else {

                if (bslList.size() != invBranchStockTransfer.getInvBranchStockTransferLines().size()
                        || !(invBranchStockTransfer.getBstDate().equals(details.getBstDate()))) {

                    isRecalculate = true;

                } else if (bslList.size() == invBranchStockTransfer.getInvBranchStockTransferLines().size()) {

                    Iterator bslIter = invBranchStockTransfer.getInvBranchStockTransferLines().iterator();
                    Iterator bslIterList = bslList.iterator();

                    while (bslIter.hasNext()) {

                        LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) bslIter
                                .next();
                        InvModBranchStockTransferLineDetails mdetails = (InvModBranchStockTransferLineDetails) bslIterList
                                .next();

                        if (!invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName()
                                .equals(mdetails.getBslLocationName())
                                || invBranchStockTransferLine.getBslUnitCost() != mdetails.getBslUnitCost()
                                || invBranchStockTransferLine.getBslQuantity() != mdetails.getBslQuantity()
                                || invBranchStockTransferLine.getBslAmount() != mdetails.getBslAmount()
                                || !invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName()
                                .equals(mdetails.getBslIiName())
                                || !invBranchStockTransferLine.getInvUnitOfMeasure().getUomName()
                                .equals(mdetails.getBslUomName())) {

                            isRecalculate = true;
                            break;
                        }

                        // get item cost
                        double COST = 0d;

                        try {

                            LocalInvCosting invCosting = invCostingHome
                                    .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                            invBranchStockTransfer.getBstDate(),
                                            invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(),
                                            invBranchStockTransferLine.getInvItemLocation().getInvLocation()
                                                    .getLocName(),
                                            AD_BRNCH, companyCode);

                            if (invCosting.getCstRemainingQuantity() <= 0) {
                                COST = EJBCommon.roundIt(
                                        Math.abs(invCosting.getCstRemainingValue()
                                                / invCosting.getCstRemainingQuantity()),
                                        this.getGlFcPrecisionUnit(companyCode));
                            } else {
                                COST = EJBCommon.roundIt(Math.abs(
                                                invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiUnitCost()),
                                        this.getGlFcPrecisionUnit(companyCode));
                            }

                        }
                        catch (FinderException ex) {

                            COST = invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiUnitCost();
                        }

                        LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome
                                .findUmcByIiNameAndUomName(
                                        invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(),
                                        invBranchStockTransferLine.getInvUnitOfMeasure().getUomName(), companyCode);
                        LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome
                                .findUmcByIiNameAndUomName(
                                        invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(),
                                        invBranchStockTransferLine.getInvItemLocation().getInvItem()
                                                .getInvUnitOfMeasure().getUomName(),
                                        companyCode);

                        COST = EJBCommon.roundIt(
                                COST * invDefaultUomConversion.getUmcConversionFactor()
                                        / invUnitOfMeasureConversion.getUmcConversionFactor(),
                                this.getGlFcPrecisionUnit(companyCode));

                        double AMOUNT = 0d;

                        AMOUNT = EJBCommon.roundIt(invBranchStockTransferLine.getBslQuantity() * COST,
                                this.getGlFcPrecisionUnit(companyCode));

                        if (invBranchStockTransferLine.getBslUnitCost() != COST) {

                            mdetails.setBslUnitCost(COST);
                            mdetails.setBslAmount(AMOUNT);

                            isRecalculate = true;
                            break;
                        }

                        // isRecalculate = false;

                    }

                } else {

                    // isRecalculate = true;

                }

                invBranchStockTransfer.setBstType("ORDER");
                invBranchStockTransfer.setBstNumber(details.getBstNumber());
                invBranchStockTransfer.setBstDescription(details.getBstDescription());
                invBranchStockTransfer.setBstDate(details.getBstDate());
                invBranchStockTransfer.setBstApprovalStatus(details.getBstApprovalStatus());
                invBranchStockTransfer.setBstLastModifiedBy(details.getBstLastModifiedBy());
                invBranchStockTransfer.setBstDateLastModified(details.getBstDateLastModified());
                invBranchStockTransfer.setBstReasonForRejection(null);
            }

            Debug.print("InvBranchStockTransferOrderEntryControllerBean saveInvBstEntry D");
            LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(details.getBstBranchFrom(), companyCode);
            adBranch.addInvBranchStockTransfer(invBranchStockTransfer);

            LocalInvLocation invLocation = invLocationHome.findByLocName(details.getBstTransitLocation(), companyCode);
            invLocation.addInvBranchStockTransfer(invBranchStockTransfer);

            double ABS_TOTAL_AMOUNT = 0d;
            Debug.print("InvBranchStockTransferOrderEntryControllerBean saveInvBstEntry E");
            if (isRecalculate) {

                // remove all branch stock transfer lines

                Iterator i = invBranchStockTransfer.getInvBranchStockTransferLines().iterator();

                short LINE_NUMBER = 0;

                while (i.hasNext()) {

                    LINE_NUMBER++;

                    LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) i
                            .next();

                    // remove inv tags
                    Collection invTags = invBranchStockTransferLine.getInvTags();

                    Iterator x = invTags.iterator();

                    while (x.hasNext()) {

                        LocalInvTag invTag = (LocalInvTag) x.next();

                        x.remove();

                        // invTag.entityRemove();
                        em.remove(invTag);
                    }

                    LocalInvItemLocation invItemLocation = null;

                    try {

                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(
                                invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName(),
                                invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(), companyCode);

                    }
                    catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException("Line " + String.valueOf(LINE_NUMBER) + " - "
                                + invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName());
                    }

                    double convertedQuantity = this.convertByUomAndQuantity(
                            invBranchStockTransferLine.getInvUnitOfMeasure(), invItemLocation.getInvItem(),
                            invBranchStockTransferLine.getBslQuantity(), companyCode);

                    invItemLocation
                            .setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - convertedQuantity);

                    i.remove();

                    // invBranchStockTransferLine.entityRemove();
                    em.remove(invBranchStockTransferLine);
                }
                Debug.print("InvBranchStockTransferOrderEntryControllerBean saveInvBstEntry E01");
                // remove all distribution records

                Debug.print("InvBranchStockTransferOrderEntryControllerBean saveInvBstEntry E02");
                // add new branch stock transfer entry lines and distribution record

                byte DEBIT = 0;

                i = bslList.iterator();

                while (i.hasNext()) {
                    Debug.print("InvBranchStockTransferOrderEntryControllerBean saveInvBstEntry E03");
                    InvModBranchStockTransferLineDetails mdetails = (InvModBranchStockTransferLineDetails) i.next();

                    LocalInvItemLocation invItemLocation = null;

                    try {
                        if (mdetails.getBslLocationName().equals("UPLOAD")) {
                            invItemLocation = invItemLocationHome
                                    .findByLocNameAndIiName(
                                            invLocationHome.findByPrimaryKey(
                                                            invItemHome.findByIiName(mdetails.getBslIiName(), companyCode)
                                                                    .getIiDefaultLocation())
                                                    .getLocName(),
                                            mdetails.getBslIiName(), companyCode);
                            mdetails.setBslLocationName(invLocationHome.findByPrimaryKey(
                                            invItemHome.findByIiName(mdetails.getBslIiName(), companyCode).getIiDefaultLocation())
                                    .getLocName());
                        } else {
                            invItemLocation = invItemLocationHome.findByLocNameAndIiName(mdetails.getBslLocationName(),
                                    mdetails.getBslIiName(), companyCode);
                        }
                        Debug.print("InvBranchStockTransferOrderEntryControllerBean saveInvBstEntry E04");
                    }
                    catch (FinderException ex) {
                        throw new GlobalInvItemLocationNotFoundException("Line "
                                + mdetails.getBslLineNumber() + " - " + mdetails.getBslLocationName());
                    }

                    LocalInvItemLocation invItemTransitLocation = null;

                    try {

                        invItemTransitLocation = invItemLocationHome.findByLocNameAndIiName(
                                details.getBstTransitLocation(), mdetails.getBslIiName(), companyCode);
                        Debug.print("InvBranchStockTransferOrderEntryControllerBean saveInvBstEntry E05");
                    }
                    catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(
                                "Transit Location " + details.getBstTransitLocation());
                    }

                    LocalInvBranchStockTransferLine invBranchStockTransferLine = this.addInvBslEntry(mdetails,
                            invBranchStockTransfer, companyCode);

                    // add distribution records

                    double COST = 0d;

                    LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome
                            .findUmcByIiNameAndUomName(
                                    invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(),
                                    invBranchStockTransferLine.getInvUnitOfMeasure().getUomName(), companyCode);
                    LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome
                            .findUmcByIiNameAndUomName(
                                    invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(),
                                    invBranchStockTransferLine.getInvItemLocation().getInvItem().getInvUnitOfMeasure()
                                            .getUomName(),
                                    companyCode);

                    COST = EJBCommon.roundIt(
                            COST * invDefaultUomConversion.getUmcConversionFactor()
                                    / invUnitOfMeasureConversion.getUmcConversionFactor(),
                            this.getGlFcPrecisionUnit(companyCode));

                    double AMOUNT = 0d;

                    AMOUNT = EJBCommon.roundIt(invBranchStockTransferLine.getBslQuantity() * COST,
                            this.getGlFcPrecisionUnit(companyCode));

                    // check branch mapping
                    Debug.print("InvBranchStockTransferOrderEntryControllerBean saveInvBstEntry E08");
                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {

                        adBranchItemLocation = adBranchItemLocationHome
                                .findBilByIlCodeAndBrCode(invItemLocation.getIlCode(), AD_BRNCH, companyCode);

                    }
                    catch (FinderException ex) {

                    }

                    LocalGlChartOfAccount glChartOfAccount = null;

                    if (adBranchItemLocation == null) {

                        glChartOfAccount = glChartOfAccountHome
                                .findByPrimaryKey(invItemLocation.getIlGlCoaInventoryAccount());

                    } else {

                        glChartOfAccount = glChartOfAccountHome
                                .findByPrimaryKey(adBranchItemLocation.getBilCoaGlInventoryAccount());
                    }

                    Debug.print("InvBranchStockTransferOrderEntryControllerBean saveInvBstEntry E10");
                    // check branch mapping for transit location

                    LocalAdBranchItemLocation adBranchItemTransitLocation = null;

                    try {

                        adBranchItemTransitLocation = adBranchItemLocationHome
                                .findBilByIlCodeAndBrCode(invItemTransitLocation.getIlCode(), AD_BRNCH, companyCode);

                    }
                    catch (FinderException ex) {

                    }

                    LocalGlChartOfAccount glChartOfAccountTransit = null;

                    if (adBranchItemTransitLocation == null) {

                        glChartOfAccountTransit = glChartOfAccountHome
                                .findByPrimaryKey(invItemTransitLocation.getIlGlCoaInventoryAccount());

                    } else {

                        glChartOfAccountTransit = glChartOfAccountHome
                                .findByPrimaryKey(adBranchItemTransitLocation.getBilCoaGlInventoryAccount());
                    }

                    // set ilCommittedQuantity

                    double convertedQuantity = this.convertByUomAndQuantity(
                            invBranchStockTransferLine.getInvUnitOfMeasure(), invItemLocation.getInvItem(),
                            invBranchStockTransferLine.getBslQuantity(), companyCode);
                    Debug.print("InvBranchStockTransferOrderEntryControllerBean saveInvBstEntry E13");
                    invItemLocation
                            .setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() + convertedQuantity);

                    // check branch to mapping

                    LocalAdBranchItemLocation adBranchToItemLocation = null;

                    try {

                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(
                                invItemTransitLocation.getIlCode(), adBranch.getBrCode(), companyCode);
                        Debug.print("InvBranchStockTransferOrderEntryControllerBean saveInvBstEntry E14");

                    }
                    catch (FinderException ex) {

                        throw new GlobalNoRecordFoundException(invItemTransitLocation.getInvItem().getIiName() + " - "
                                + invItemTransitLocation.getInvLocation().getLocName());
                    }
                }

            } else {

                Iterator i = bslList.iterator();
                Debug.print("InvBranchStockTransferOrderEntryControllerBean saveInvBstEntry F");
                while (i.hasNext()) {
                    Debug.print("InvBranchStockTransferOrderEntryControllerBean saveInvBstEntry F01");
                    InvModBranchStockTransferLineDetails mdetails = (InvModBranchStockTransferLineDetails) i.next();

                    LocalInvItemLocation invItemLocation = null;

                    try {

                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(mdetails.getBslLocationName(),
                                mdetails.getBslIiName(), companyCode);
                        Debug.print("InvBranchStockTransferOrderEntryControllerBean saveInvBstEntry F02");
                    }
                    catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException("Line "
                                + mdetails.getBslLineNumber() + " - " + mdetails.getBslLocationName());
                    }
                }
            }

            // generate approval status

            String INV_APPRVL_STATUS = null;
            Debug.print("InvBranchStockTransferOrderEntryControllerBean saveInvBstEntry G");
            if (!isDraft) {

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);

                // check if inv stock transfer approval is enabled

                if (adApproval.getAprEnableInvBranchStockTransferOrder() == EJBCommon.FALSE) {
                    INV_APPRVL_STATUS = "N/A";

                } else if (details.getBstType().equals("EMERGENCY")) {
                    if (details.getBstDescription().contains("UPLOAD")) {
                        details.setBstDescription(details.getBstDescription().replace("UPLOAD ", ""));
                    }
                    Debug.print(details.getBstLastModifiedBy() + "<== eto ba? o eto? ==>" + companyCode);
                    LocalAdUser adUser = adUserHome.findByUsrName(details.getBstLastModifiedBy(), companyCode);

                    Collection adUsers = adUserHome.findUsrByDepartmentHead(adUser.getUsrDept(), (byte) 1, companyCode);
                    Debug.print(adUsers.size() + "<== size");
                    if (adUsers.isEmpty()) {

                        throw new GlobalNoApprovalApproverFoundException();

                    } else {

                        for (Object user : adUsers) {
                            LocalAdUser adUserHead = (LocalAdUser) user;
                            LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(AD_BRNCH, companyCode, adApprovalQueueHome, invBranchStockTransfer);
                            adUserHead.addAdApprovalQueue(adApprovalQueue);
                        }
                    }
                    INV_APPRVL_STATUS = "PENDING";
                } else {
                    Iterator i = invBranchStockTransfer.getInvBranchStockTransferLines().iterator();
                    Byte assetCode = null;
                    while (i.hasNext()) {
                        LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) i
                                .next();
                        assetCode = invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiFixedAsset();
                    }
                    LocalAdBranch ifHQ = adBranchHome.findByBrBranchCode(details.getBstBranchFrom(), companyCode);
                    Debug.print(assetCode + "<== asset ba?");
                    Debug.print(ifHQ.getBrHeadQuarter() + "<== headquarters ba?");

                    if (assetCode == 0 && ifHQ.getBrHeadQuarter() == 1
                            || details.getBstDescription().contains("UPLOAD")) {
                        INV_APPRVL_STATUS = "N/A";
                        if (details.getBstDescription().contains("UPLOAD")) {
                            details.setBstDescription(details.getBstDescription().replace("UPLOAD ", ""));
                        }
                        // check if invoice is self approved

                    } else {
                        Debug.print(details.getBstLastModifiedBy() + "<== eto ba? o eto? ==>" + companyCode);
                        LocalAdUser adUser = adUserHome.findByUsrName(details.getBstLastModifiedBy(), companyCode);

                        Collection adUsers = adUserHome.findUsrByDepartmentHead(adUser.getUsrDept(), (byte) 1,
                                companyCode);
                        Debug.print(adUsers.size() + "<== size");
                        if (adUsers.isEmpty()) {

                            throw new GlobalNoApprovalApproverFoundException();

                        } else {

                            for (Object user : adUsers) {
                                LocalAdUser adUserHead = (LocalAdUser) user;
                                LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(AD_BRNCH, companyCode, adApprovalQueueHome, invBranchStockTransfer);
                                adUserHead.addAdApprovalQueue(adApprovalQueue);
                            }
                        }
                        INV_APPRVL_STATUS = "PENDING";
                    }
                }
            }
            Debug.print("InvBranchStockTransferOrderEntryControllerBean saveInvBstEntry H");
            if (INV_APPRVL_STATUS != null && INV_APPRVL_STATUS.equals("N/A")) {

                this.executeInvBstPost(invBranchStockTransfer.getBstCode(),
                        invBranchStockTransfer.getBstLastModifiedBy(), AD_BRNCH, companyCode);
            }

            // set stock transfer approval status

            invBranchStockTransfer.setBstApprovalStatus(INV_APPRVL_STATUS);
            Debug.print("InvBranchStockTransferOrderEntryControllerBean saveInvBstEntry " + txnStartDate);
            return invBranchStockTransfer.getBstCode();

        }
        catch (GlobalRecordAlreadyDeletedException | GlobalMiscInfoIsRequiredException |
               GlobalExpiryDateNotFoundException | GlobalNoRecordFoundException | GlobalRecordInvalidException |
               AdPRFCoaGlVarianceAccountNotFoundException | GlobalDocumentNumberNotUniqueException |
               GlobalJournalNotBalanceException | GlJREffectiveDatePeriodClosedException |
               GlJREffectiveDateNoPeriodExistException | GlobalInvItemLocationNotFoundException |
               GlobalNoApprovalApproverFoundException | GlobalTransactionAlreadyPostedException |
               GlobalTransactionAlreadyPendingException | GlobalTransactionAlreadyApprovedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlobalBranchAccountNumberInvalidException ex) {
            Debug.print("account invalid ex:");
            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteInvBstEntry(Integer BST_CODE, String AD_USR, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException {

        Debug.print("InvBranchStockTransferOrderEntryControllerBean deleteInvBstEntry");
        try {

            LocalInvBranchStockTransfer invBranchStockTransfer = invBranchStockTransferHome.findByPrimaryKey(BST_CODE);

            for (Object o : invBranchStockTransfer.getInvBranchStockTransferLines()) {

                LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) o;

                LocalInvItemLocation invItemLocation = invItemLocationHome.findByLocNameAndIiName(
                        invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName(),
                        invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(), companyCode);

                double convertedQuantity = this.convertByUomAndQuantity(
                        invBranchStockTransferLine.getInvUnitOfMeasure(), invItemLocation.getInvItem(),
                        Math.abs(invBranchStockTransferLine.getBslQuantity()), companyCode);

                invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - convertedQuantity);
            }

            if (invBranchStockTransfer.getBstApprovalStatus() != null
                    && invBranchStockTransfer.getBstApprovalStatus().equals("PENDING")) {

                Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode(
                        "INV BRANCH STOCK TRANSFER ORDER", invBranchStockTransfer.getBstCode(), companyCode);

                for (Object approvalQueue : adApprovalQueues) {

                    LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) approvalQueue;

                    // adApprovalQueue.entityRemove();
                    em.remove(adApprovalQueue);
                }
            }

            adDeleteAuditTrailHome.create("INV BRANCH STOCK TRANSFER ORDER", invBranchStockTransfer.getBstDate(),
                    invBranchStockTransfer.getBstNumber(), invBranchStockTransfer.getBstNumber(), 0d, AD_USR,
                    new Date(), companyCode);

            // invBranchStockTransfer.entityRemove();
            em.remove(invBranchStockTransfer);

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

        Debug.print("InvBranchStockTransferOrderEntryControllerBean getGlFcPrecisionUnit");
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

        Debug.print("InvBranchStockTransferOrderEntryControllerBean getInvGpQuantityPrecisionUnit");
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

        Debug.print("InvBranchStockTransferOrderEntryControllerBean getInvGpInventoryLineNumber");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfInvInventoryLineNumber();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdApprovalNotifiedUsersByBstCode(Integer BST_CODE, Integer companyCode) {

        Debug.print("InvBranchStockTransferOrderEntryControllerBean getAdApprovalNotifiedUsersByBstCode");
        ArrayList list = new ArrayList();
        try {

            LocalInvBranchStockTransfer invBranchStockTransfer = invBranchStockTransferHome.findByPrimaryKey(BST_CODE);

            if (invBranchStockTransfer.getBstPosted() == EJBCommon.TRUE) {

                list.add("DOCUMENT POSTED");
                return list;
            }

            Collection adApprovalQueues = adApprovalQueueHome
                    .findByAqDocumentAndAqDocumentCode("INV BRANCH STOCK TRANSFER ORDER", BST_CODE, companyCode);

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

    public double getInvIiUnitCostByIiNameAndLocFromAndUomNameAndDate(String II_NM, String LOC_FRM, String UOM_NM,
                                                                      Date ST_DT, Integer AD_BRNCH, Integer companyCode) {

        Debug.print("InvBranchStockTransferOrderEntryControllerBean getInvIiUnitCostByIiNameAndUomName");
        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NM, companyCode);

            double COST = invItem.getIiUnitCost();

            LocalInvItemLocation invItemLocation = null;

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(II_NM, UOM_NM, companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(II_NM, invItem.getInvUnitOfMeasure().getUomName(), companyCode);

            return EJBCommon.roundIt(COST * invDefaultUomConversion.getUmcConversionFactor()
                    / invUnitOfMeasureConversion.getUmcConversionFactor(), this.getGlFcPrecisionUnit(companyCode));

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods

    private LocalInvBranchStockTransferLine addInvBslEntry(InvModBranchStockTransferLineDetails mdetails,
                                                           LocalInvBranchStockTransfer invBranchStockTransfer, Integer companyCode)
            throws GlobalMiscInfoIsRequiredException {

        Debug.print("InvBranchStockTransferOrderEntryControllerBean addInvBslEntry");
        try {

            LocalInvBranchStockTransferLine invBranchStockTransferLine = invBranchStockTransferLineHome.create(
                    mdetails.getBslQuantity(), mdetails.getBslQuantityReceived(), mdetails.getBslUnitCost(),
                    mdetails.getBslAmount(), companyCode);

            invBranchStockTransfer.addInvBranchStockTransferLine(invBranchStockTransferLine);
            invBranchStockTransferLine.setInvBranchStockTransfer(invBranchStockTransfer);

            LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(mdetails.getBslUomName(),
                    companyCode);

            invUnitOfMeasure.addInvBranchStockTransferLine(invBranchStockTransferLine);
            invBranchStockTransferLine.setInvUnitOfMeasure(invUnitOfMeasure);

            LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(mdetails.getBslIiName(),
                    mdetails.getBslLocationName(), companyCode);

            invItemLocation.addInvBranchStockTransferLine(invBranchStockTransferLine);
            invBranchStockTransferLine.setInvItemLocation(invItemLocation);

            if (invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiTraceMisc() == 1) {
                this.createInvTagList(invBranchStockTransferLine, mdetails.getBslTagList(), companyCode);
            }

            return invBranchStockTransferLine;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public String checkExpiryDates(String misc, double qty, String reverse) throws Exception {
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
        String miscList2 = "";

        for (int x = 0; x < qty; x++) {

            // Date
            start = nextIndex + 1;
            nextIndex = misc.indexOf(separator, start);
            length = nextIndex - start;
            String g = misc.substring(start, start + length);
            Debug.print("g: " + g);
            Debug.print("g length: " + g.length());
            if (g.length() != 0) {
                if (g != null || g != "" || g != "null") {
                    if (g.contains("null")) {
                        miscList2 = "Error";
                    } else {
                        miscList.append("$").append(g);
                    }
                } else {
                    miscList2 = "Error";
                }

                Debug.print("miscList G: " + miscList);
            } else {
                Debug.print("KABOOM");
                miscList2 = "Error";
            }
        }
        Debug.print("miscList2 :" + miscList2);
        if (miscList2 == "") {
            miscList.append("$");
        } else {
            miscList = new StringBuilder(miscList2);
        }

        Debug.print("miscList :" + miscList);
        return (miscList.toString());
    }

    private double convertByUomAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem,
                                           double ADJST_QTY, Integer companyCode) {

        Debug.print("InvBranchStockTransferOrderEntryControllerBean convertByUomFromAndUomToAndQuantity");

        LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome = null;

        // Initialize EJB Home

        try {

            invUnitOfMeasureConversionHome = (LocalInvUnitOfMeasureConversionHome) EJBHomeFactory.lookUpLocalHome(
                    LocalInvUnitOfMeasureConversionHome.JNDI_NAME, LocalInvUnitOfMeasureConversionHome.class);

        }
        catch (NamingException ex) {

            throw new EJBException(ex.getMessage());
        }

        try {
            Debug.print("InvBranchStockTransferOrderEntryControllerBean convertByUomFromAndUomToAndQuantity A");
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(),
                            companyCode);
            Debug.print("InvBranchStockTransferOrderEntryControllerBean convertByUomFromAndUomToAndQuantity B");
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

    private void executeInvBstPost(Integer BST_CODE, String USR_NM, Integer AD_BRNCH, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException,
            GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException,
            GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException,
            AdPRFCoaGlVarianceAccountNotFoundException, GlobalRecordInvalidException,
            GlobalExpiryDateNotFoundException {

        Debug.print("InvBranchStockTransferOrderEntryControllerBean executeInvBstPost");
        try {

            // validate if branch stock transfer is already deleted

            LocalInvBranchStockTransfer invBranchStockTransfer = null;

            try {

                invBranchStockTransfer = invBranchStockTransferHome.findByPrimaryKey(BST_CODE);

            }
            catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if branch stock transfer is already posted or void

            if (invBranchStockTransfer.getBstPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();
            }

            // regenerate inventory dr

            this.regenerateInventoryDr(invBranchStockTransfer, AD_BRNCH, companyCode);

            Iterator i = invBranchStockTransfer.getInvBranchStockTransferLines().iterator();

            while (i.hasNext()) {

                LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) i.next();

                String locName = invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName();
                String invItemName = invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName();

                String transitLocationName = invBranchStockTransfer.getInvLocation().getLocName();

                LocalInvItemLocation invItemLocationFrom = invItemLocationHome.findByLocNameAndIiName(locName,
                        invItemName, companyCode);

                LocalInvItemLocation invItemTransitLocation = invItemLocationHome
                        .findByLocNameAndIiName(transitLocationName, invItemName, companyCode);

                Debug.print("InvBranchStockTransferOrderEntryControllerBean executeInvBstPost A02");
                double BST_COST = invBranchStockTransferLine.getBslQuantity()
                        * invBranchStockTransferLine.getBslUnitCost();

                double BST_QTY = this.convertByUomAndQuantity(invBranchStockTransferLine.getInvUnitOfMeasure(),
                        invBranchStockTransferLine.getInvItemLocation().getInvItem(),
                        Math.abs(invBranchStockTransferLine.getBslQuantity()), companyCode);
                Debug.print("InvBranchStockTransferOrderEntryControllerBean executeInvBstPost A03");
            }
            Debug.print("InvBranchStockTransferOrderEntryControllerBean executeInvBstPost C");
            // set branch stock transfer post status

            invBranchStockTransfer.setBstPosted(EJBCommon.TRUE);
            invBranchStockTransfer.setBstPostedBy(USR_NM);
            invBranchStockTransfer.setBstDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            // post to GL
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate if date has no period and period is closed

            LocalGlSetOfBook glJournalSetOfBook = null;

            try {

                glJournalSetOfBook = glSetOfBookHome.findByDate(invBranchStockTransfer.getBstDate(), companyCode);

            }
            catch (FinderException ex) {

                throw new GlJREffectiveDateNoPeriodExistException();
            }

            LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome
                    .findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(),
                            invBranchStockTransfer.getBstDate(), companyCode);

            if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C'
                    || glAccountingCalendarValue.getAcvStatus() == 'P') {

                throw new GlJREffectiveDatePeriodClosedException();
            }

            // check if debit and credit is balance

            LocalGlJournalLine glOffsetJournalLine = null;

            Collection invDistributionRecords = invDistributionRecordHome
                    .findImportableDrByBstCode(invBranchStockTransfer.getBstCode(), companyCode);

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

                    glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("INVENTORY",
                            "BRANCH STOCK TRANSFERS", companyCode);

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
                // glChartOfAccount.addGlJournalLine(glOffsetJournalLine);
                glOffsetJournalLine.setGlChartOfAccount(glChartOfAccount);

            } else if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE && TOTAL_DEBIT != TOTAL_CREDIT) {

                throw new GlobalJournalNotBalanceException();
            }
            Debug.print("InvBranchStockTransferOrderEntryControllerBean executeInvBstPost D");
            // create journal batch if necessary

            LocalGlJournalBatch glJournalBatch = null;
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");

            try {

                glJournalBatch = glJournalBatchHome.findByJbName(
                        "JOURNAL IMPORT " + formatter.format(new Date()) + " BRANCH STOCK TRANSFERS", AD_BRNCH,
                        companyCode);

            }
            catch (FinderException ex) {

            }

            if (glJournalBatch == null) {

                glJournalBatch = glJournalBatchHome.create(
                        "JOURNAL IMPORT " + formatter.format(new Date()) + " BRANCH STOCK TRANSFERS", "JOURNAL IMPORT",
                        "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, AD_BRNCH, companyCode);
            }

            // create journal entry

            LocalGlJournal glJournal = glJournalHome.create(invBranchStockTransfer.getBstNumber(),
                    invBranchStockTransfer.getBstDescription(), invBranchStockTransfer.getBstDate(), 0.0d, null,
                    invBranchStockTransfer.getBstNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE,
                    USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM,
                    EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, EJBCommon.FALSE, null, AD_BRNCH,
                    companyCode);

            LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("INVENTORY", companyCode);
            glJournal.setGlJournalSource(glJournalSource);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome
                    .findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
            glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("BRANCH STOCK TRANSFERS",
                    companyCode);
            glJournal.setGlJournalCategory(glJournalCategory);

            if (glJournalBatch != null) {

                glJournal.setGlJournalBatch(glJournalBatch);
            }

            // create journal lines
            i = invDistributionRecords.iterator();

            while (i.hasNext()) {

                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) i.next();

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
            Debug.print("InvBranchStockTransferOrderEntryControllerBean executeInvBstPost G");

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
            Debug.print("InvBranchStockTransferOrderEntryControllerBean executeInvBstPost H");
        }
        catch (GlJREffectiveDateNoPeriodExistException | GlobalBranchAccountNumberInvalidException |
               GlobalTransactionAlreadyPostedException | GlobalRecordAlreadyDeletedException |
               GlobalJournalNotBalanceException | GlJREffectiveDatePeriodClosedException ex) {

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

    public double checkExpiryDates2(String misc) throws Exception {

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

    public String propagateExpiryDates(String misc, double qty, String reverse) throws Exception {
        // ActionErrors errors = new ActionErrors();
        Debug.print("InvBranchStockTransferOrderControllerBean getExpiryDates");
        // Debug.print("misc: " + misc);

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

    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue,
                          LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double JL_AMNT,
                          Integer companyCode) {

        Debug.print("InvBranchStockTransferOrderEntryControllerBean postToGl");
        try {

            Debug.print("InvBranchStockTransferOrderEntryControllerBean postToGl A");
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
            Debug.print("InvBranchStockTransferOrderEntryControllerBean postToGl B");
        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void regenerateInventoryDr(LocalInvBranchStockTransfer invBranchStockTransfer, Integer AD_BRNCH,
                                       Integer companyCode) throws GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException {

        Debug.print("InvBranchStockTransferOrderEntryControllerBean regenerateInventoryDr");
        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            // regenerate inventory distribution records

            Collection invDistributionRecords = invDistributionRecordHome
                    .findImportableDrByBstCode(invBranchStockTransfer.getBstCode(), companyCode);

            Iterator i = invDistributionRecords.iterator();

            while (i.hasNext()) {

                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) i.next();

                if (invDistributionRecord.getDrClass().equals("INVENTORY")) {

                    i.remove();
                    // invDistributionRecord.entityRemove();
                    em.remove(invDistributionRecord);
                }
            }

            Collection invBranchStockTransferLines = invBranchStockTransfer.getInvBranchStockTransferLines();

            i = invBranchStockTransferLines.iterator();

            while (i.hasNext()) {

                LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) i.next();

                LocalInvItemLocation invItemLocation = null;

                String locName = invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName();
                String invItemName = invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName();

                try {

                    invItemLocation = invItemLocationHome.findByLocNameAndIiName(locName, invItemName, companyCode);

                }
                catch (FinderException ex) {

                    throw new GlobalInvItemLocationNotFoundException(invItemName + " - " + locName);
                }

                LocalInvItemLocation invItemTransitLocation = null;

                try {

                    invItemTransitLocation = invItemLocationHome.findByLocNameAndIiName(
                            invBranchStockTransfer.getInvLocation().getLocName(), invItemName, companyCode);

                }
                catch (FinderException ex) {

                    throw new GlobalInvItemLocationNotFoundException(
                            "Transit Location " + invBranchStockTransfer.getInvLocation().getLocName());
                }

                // start date validation

                // check branch mapping

                LocalAdBranchItemLocation adBranchItemLocation = null;

                try {

                    adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(
                            invBranchStockTransferLine.getInvItemLocation().getIlCode(), AD_BRNCH, companyCode);

                }
                catch (FinderException ex) {

                }

                LocalGlChartOfAccount glChartOfAccount = null;

                if (adBranchItemLocation == null) {

                    glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(
                            invBranchStockTransferLine.getInvItemLocation().getIlGlCoaInventoryAccount());

                } else {

                    glChartOfAccount = glChartOfAccountHome
                            .findByPrimaryKey(adBranchItemLocation.getBilCoaGlInventoryAccount());
                }

                // check branch mapping for transit location

                LocalAdBranchItemLocation adBranchItemTransitLocation = null;

                try {

                    adBranchItemTransitLocation = adBranchItemLocationHome
                            .findBilByIlCodeAndBrCode(invItemTransitLocation.getIlCode(), AD_BRNCH, companyCode);

                }
                catch (FinderException ex) {

                }

                LocalGlChartOfAccount glChartOfAccountTransit = null;

                if (adBranchItemTransitLocation == null) {

                    glChartOfAccountTransit = glChartOfAccountHome
                            .findByPrimaryKey(invItemTransitLocation.getIlGlCoaInventoryAccount());

                } else {

                    glChartOfAccountTransit = glChartOfAccountHome
                            .findByPrimaryKey(adBranchItemTransitLocation.getBilCoaGlInventoryAccount());
                }

                // add dr for inventory transit location

            }

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public boolean getInvTraceMisc(String II_NAME, Integer companyCode) {

        Debug.print("InvBranchStockTransferOrderEntryController getInvTraceMisc");
        boolean isTraceMisc = false;
        try {
            Debug.print("II_NAME=" + II_NAME);
            Debug.print("companyCode=" + companyCode);
            LocalInvItem invItem = invItemHome.findByIiName(II_NAME, companyCode);
            Debug.print("invItem Name=" + invItem.getIiName() + "-" + invItem.getIiCode());
            if (invItem.getIiTraceMisc() == 1) {
                Debug.print("true");
                isTraceMisc = true;
            }
            Debug.print("isTraceMisc=" + isTraceMisc);

            return isTraceMisc;
        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void createInvTagList(LocalInvBranchStockTransferLine invBranchStockTransferLine, ArrayList list,
                                  Integer companyCode) throws Exception {

        Debug.print("InvBranchStockTransferOrderInControllerBean createInvTagList");
        try {
            Debug.print("aabot?");
            // Iterator t = apPurchaseOrderLine.getInvTag().iterator();
            Iterator t = list.iterator();

            LocalInvTag invTag = null;
            Debug.print("umabot?");
            while (t.hasNext()) {
                InvModTagListDetails tgLstDetails = (InvModTagListDetails) t.next();
                Debug.print(tgLstDetails.getTgCustodian() + "<== custodian");
                Debug.print(tgLstDetails.getTgSpecs() + "<== specs");
                Debug.print(tgLstDetails.getTgPropertyCode() + "<== propertyCode");
                Debug.print(tgLstDetails.getTgExpiryDate() + "<== expiryDate");
                Debug.print(tgLstDetails.getTgSerialNumber() + "<== serial number");

                if (tgLstDetails.getTgCode() == null) {
                    Debug.print("ngcreate ba?");
                    invTag = invTagHome.create(tgLstDetails.getTgPropertyCode(), tgLstDetails.getTgSerialNumber(), null,
                            tgLstDetails.getTgExpiryDate(), tgLstDetails.getTgSpecs(), companyCode,
                            tgLstDetails.getTgTransactionDate(), tgLstDetails.getTgType());

                    invTag.setInvBranchStockTransferLine(invBranchStockTransferLine);
                    invTag.setInvItemLocation(invBranchStockTransferLine.getInvItemLocation());
                    LocalAdUser adUser = null;
                    try {
                        adUser = adUserHome.findByUsrName(tgLstDetails.getTgCustodian(), companyCode);
                    }
                    catch (FinderException ex) {

                    }
                    invTag.setAdUser(adUser);
                    Debug.print("ngcreate ba?");
                }
            }

        }
        catch (Exception ex) {
            throw ex;
        }
    }

    private ArrayList getInvTagList(LocalInvBranchStockTransferLine arInvBranchStockTransferLine) {

        ArrayList list = new ArrayList();
        Collection invTags = arInvBranchStockTransferLine.getInvTags();
        for (Object tag : invTags) {
            LocalInvTag invTag = (LocalInvTag) tag;
            InvModTagListDetails tgLstDetails = new InvModTagListDetails();
            tgLstDetails.setTgPropertyCode(invTag.getTgPropertyCode());
            tgLstDetails.setTgSpecs(invTag.getTgSpecs());
            tgLstDetails.setTgExpiryDate(invTag.getTgExpiryDate());
            tgLstDetails.setTgSerialNumber(invTag.getTgSerialNumber());
            try {

                tgLstDetails.setTgCustodian(invTag.getAdUser().getUsrName());
            }
            catch (Exception ex) {
                tgLstDetails.setTgCustodian("");
            }

            list.add(tgLstDetails);

            Debug.print(tgLstDetails.getTgPropertyCode() + "<== property code inside controllerbean ");
            Debug.print(tgLstDetails.getTgSpecs() + "<== specs inside controllerbean ");
            Debug.print(list + "<== taglist inside controllerbean ");
        }
        return list;
    }

    private LocalAdApprovalQueue createApprovalQueue(Integer branchCode, Integer companyCode,
                                                     LocalAdApprovalQueueHome adApprovalQueueHome,
                                                     LocalInvBranchStockTransfer invBranchStockTransfer) throws CreateException {

        return adApprovalQueueHome
                .AqForApproval(EJBCommon.TRUE)
                .AqDocument("INV BRANCH STOCK TRANSFER ORDER")
                .AqDocumentCode(invBranchStockTransfer.getBstCode())
                .AqDocumentNumber(invBranchStockTransfer.getBstNumber())
                .AqDate(invBranchStockTransfer.getBstDate())
                .AqAndOr("OR")
                .AqUserOr(EJBCommon.TRUE)
                .AqAdBranch(branchCode)
                .AqAdCompany(companyCode)
                .buildApprovalQueue();
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("InvBranchStockTransferOrderEntryControllerBean ejbCreate");
    }

}