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
import com.ejb.exception.inv.InvATRAssemblyQtyGreaterThanAvailableQtyException;
import com.ejb.exception.inv.InvTagSerialNumberNotFoundException;
import com.ejb.txn.OmegaCommonDataController;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.mod.inv.InvModBranchStockTransferDetails;
import com.util.mod.inv.InvModBranchStockTransferLineDetails;
import com.util.mod.inv.InvModTagListDetails;
import com.util.mod.inv.InvModUnitOfMeasureDetails;

import jakarta.ejb.*;
import java.util.*;

@Stateless(name = "InvBranchStockTransferInEntryControllerEJB")
public class InvBranchStockTransferInEntryControllerBean extends EJBContextClass
        implements InvBranchStockTransferInEntryController {

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
    private OmegaCommonDataController commonData;
    @EJB
    private LocalInvBranchStockTransferHome invBranchStockTransferHome;
    @EJB
    private LocalInvBranchStockTransferLineHome invBranchStockTransferLineHome;
    @EJB
    private LocalInvUnitOfMeasureHome invBranchUnitOfMeasureHome;
    @EJB
    private LocalInvItemHome invBranchItemHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalInvTagHome invTagHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalAdDeleteAuditTrailHome adDeleteAuditTrailHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
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
    private LocalInvAdjustmentLineHome invAdjustmentLineHome;
    @EJB
    private LocalInvAdjustmentHome invAdjustmentHome;

    public InvModBranchStockTransferDetails getInvBstByBstCode(Date BSTI_DT, Integer BST_CODE, Integer AD_BRNCH,
                                                               Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("InvBranchStockTransferInEntryControllerBean getInvBstByBstCode");

        try {
            LocalInvBranchStockTransfer invBranchStockTransfer = null;
            try {
                invBranchStockTransfer = invBranchStockTransferHome.findByPrimaryKey(BST_CODE);
            }
            catch (FinderException ex) {
                throw new GlobalNoRecordFoundException();
            }

            String specs = null;
            String propertyCode = null;
            String expiryDate = null;
            String serialNumber = null;
            ArrayList bslList = new ArrayList();

            // branch stock transfer lines
            for (Object o : invBranchStockTransfer.getInvBranchStockTransferLines()) {
                LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) o;
                InvModBranchStockTransferLineDetails mdetails = new InvModBranchStockTransferLineDetails();
                ArrayList tagList = new ArrayList();
                mdetails.setTraceMisc(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiTraceMisc());
                if (mdetails.getTraceMisc() == 1) {
                    tagList = this.getInvTagList(invBranchStockTransferLine);
                    mdetails.setBslTagList(tagList);
                }

                mdetails.setBslCode(invBranchStockTransferLine.getBslCode());
                mdetails.setBslIiName(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName());
                mdetails.setBslLocationName(invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName());
                mdetails.setBslQuantity(invBranchStockTransferLine.getBslQuantity());
                mdetails.setBslQuantityReceived(invBranchStockTransferLine.getBslQuantityReceived());
                mdetails.setBslUomName(invBranchStockTransferLine.getInvUnitOfMeasure().getUomName());
                mdetails.setBslIiDescription(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiDescription());
                mdetails.setBslMisc(invBranchStockTransferLine.getBslMisc());

                // if bst out, unit cost based on transit location
                if (invBranchStockTransfer.getBstType().equals("OUT")) {
                    String BR_OUT_NM = this.getAdBrNameByBrCode(invBranchStockTransfer.getBstAdBranch());
                    double unitCost = this.getInvIiUnitCostByIiNameAndLocFromAndUomNameAndDate(
                            invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(),
                            invBranchStockTransfer.getInvLocation().getLocName(),
                            invBranchStockTransferLine.getInvUnitOfMeasure().getUomName(), BSTI_DT, BR_OUT_NM,
                            companyCode);
                    double shippingCost = 0d;

                    shippingCost = this.getInvIiShippingCostByIiUnitCostAndAdBranch(unitCost, AD_BRNCH, companyCode);
                    mdetails.setBslShippingCost(shippingCost);
                    mdetails.setBslUnitCost(unitCost + shippingCost);

                    double AMOUNT = 0d;
                    AMOUNT = EJBCommon.roundIt(invBranchStockTransferLine.getBslQuantity() * mdetails.getBslUnitCost(),
                            commonData.getGlFcPrecisionUnit(companyCode));
                    mdetails.setBslAmount(AMOUNT);

                } else {
                    mdetails.setBslUnitCost(invBranchStockTransferLine.getBslUnitCost());
                    mdetails.setBslAmount(invBranchStockTransferLine.getBslAmount());
                }
                bslList.add(mdetails);
            }

            InvModBranchStockTransferDetails details = new InvModBranchStockTransferDetails();

            details.setBstCode(invBranchStockTransfer.getBstCode());
            details.setBstNumber(invBranchStockTransfer.getBstNumber());
            details.setBstDescription(invBranchStockTransfer.getBstDescription());
            details.setBstDate(invBranchStockTransfer.getBstDate());
            details.setBstTransferOutNumber(invBranchStockTransfer.getBstTransferOutNumber());
            details.setBstBranchFrom(invBranchStockTransfer.getAdBranch().getBrName());

            try {
                String BR_OUT_NM = this.getAdBrNameByBrCode(invBranchStockTransfer.getBstAdBranch());
                this.getInvBstOutByBstNumberAndBstBranch(invBranchStockTransfer.getBstTransferOutNumber(), BR_OUT_NM,
                        AD_BRNCH, companyCode);
                details.setBstType("BST-OUT MATCHED");

            }
            catch (GlobalNoRecordFoundException ex) {

                details.setBstType("ITEMS");
            }

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

            if (invBranchStockTransfer.getBstType().equals("IN")) {

                details.setBstBranchFrom(invBranchStockTransfer.getAdBranch().getBrName());
                details.setBstTransitLocation(invBranchStockTransfer.getInvLocation().getLocName());

            } else {

                details.setBstBranchFrom(this.getAdBrNameByBrCode(invBranchStockTransfer.getBstAdBranch()));
                details.setBstTransitLocation(invBranchStockTransfer.getInvLocation().getLocName());
            }

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

    public InvModBranchStockTransferDetails getInvBstOutByBstCode(Integer BST_OUT_CODE, Integer AD_BRNCH,
                                                                  Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("InvBranchStockTransferInEntryControllerBean getInvBstOutByBstCode");

        try {
            LocalInvBranchStockTransfer invBranchStockTransferOut = null;
            LocalAdBranch adBranch = null;
            try {
                invBranchStockTransferOut = invBranchStockTransferHome.findByPrimaryKey(BST_OUT_CODE);
            }
            catch (FinderException ex) {
                throw new GlobalNoRecordFoundException();
            }

            ArrayList bslList = new ArrayList();

            // branch stock transfer lines
            for (Object o : invBranchStockTransferOut.getInvBranchStockTransferLines()) {
                LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) o;
                InvModBranchStockTransferLineDetails mdetails = new InvModBranchStockTransferLineDetails();

                Collection invAllBstReceives = invBranchStockTransferLineHome.findInBslByBstTransferOutNumberAndItemLocAndAdBranchAndAdCompany(
                        invBranchStockTransferOut.getBstNumber(), invBranchStockTransferLine.getInvItemLocation().getIlCode(), AD_BRNCH, companyCode);

                Iterator x = invAllBstReceives.iterator();
                double totalQuantityReceived = 0d;
                while (x.hasNext()) {
                    LocalInvBranchStockTransferLine invBranchStockTransferLine2 = (LocalInvBranchStockTransferLine) x.next();
                    totalQuantityReceived += invBranchStockTransferLine2.getBslQuantityReceived();
                }
                double totalRemaining = invBranchStockTransferLine.getBslQuantity() - totalQuantityReceived;

                mdetails.setBslCode(invBranchStockTransferLine.getBslCode());
                mdetails.setBslIiName(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName());
                mdetails.setBslLocationName(invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName());
                mdetails.setBslQuantity(totalRemaining);
                mdetails.setBslQuantityReceived(totalRemaining);
                mdetails.setBslUomName(invBranchStockTransferLine.getInvUnitOfMeasure().getUomName());
                mdetails.setBslIiDescription(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiDescription());
                mdetails.setBslUnitCost(invBranchStockTransferLine.getBslUnitCost());

                ArrayList tagList = new ArrayList();
                mdetails.setTraceMisc(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiTraceMisc());
                if (mdetails.getTraceMisc() == 1) {
                    tagList = this.getInvTagList(invBranchStockTransferLine);
                    mdetails.setBslTagList(tagList);
                }
                bslList.add(mdetails);
            }

            InvModBranchStockTransferDetails details = new InvModBranchStockTransferDetails();
            details.setBstCode(invBranchStockTransferOut.getBstCode());
            details.setBstNumber(invBranchStockTransferOut.getBstNumber());
            details.setBstBranchFrom(invBranchStockTransferOut.getAdBranch().getBrName());
            details.setBstTransitLocation(invBranchStockTransferOut.getInvLocation().getLocName());
            details.setBstTransferOutNumber(invBranchStockTransferOut.getBstTransferOutNumber());
            details.setBstBranchFrom(this.getAdBrNameByBrCode(invBranchStockTransferOut.getBstAdBranch()));
            details.setBstTransitLocation(invBranchStockTransferOut.getInvLocation().getLocName());
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

        Debug.print("InvBranchStockTransferInEntryControllerBean getInvBranchUomByIiName");

        ArrayList list = new ArrayList();
        try {

            LocalInvItem invBranchItem = null;
            LocalInvUnitOfMeasure invBranchItemUnitOfMeasure = null;

            invBranchItem = invBranchItemHome.findByIiName(II_NM, companyCode);
            invBranchItemUnitOfMeasure = invBranchItem.getInvUnitOfMeasure();
            Collection branchUomList = invBranchUnitOfMeasureHome.findByUomAdLvClass(invBranchItemUnitOfMeasure.getUomAdLvClass(), companyCode);
            for (Object o : branchUomList) {
                LocalInvUnitOfMeasure invBranchUnitOfMeasure = (LocalInvUnitOfMeasure) o;
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
                                   boolean isDraft, String type, Integer AD_BRNCH, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyApprovedException,
            GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException,
            GlobalInvItemLocationNotFoundException, GlobalInvTagMissingException, GlobalInvTagExistingException,
            GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException,
            GlobalJournalNotBalanceException, GlobalDocumentNumberNotUniqueException, GlobalInventoryDateException,
            GlobalBranchAccountNumberInvalidException, InvATRAssemblyQtyGreaterThanAvailableQtyException,
            AdPRFCoaGlVarianceAccountNotFoundException, GlobalMiscInfoIsRequiredException,
            GlobalExpiryDateNotFoundException, InvTagSerialNumberNotFoundException {

        Debug.print("InvBranchStockTransferInEntryControllerBean saveInvBstEntry");

        Date txnStartDate = new Date();
        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalInvBranchStockTransfer invBranchStockTransfer = null;

            // Validate if branch stock transfer is already deleted
            try {
                if (details.getBstCode() != null) {
                    invBranchStockTransfer = invBranchStockTransferHome.findByPrimaryKey(details.getBstCode());
                }
            }
            catch (FinderException ex) {
                throw new GlobalRecordAlreadyDeletedException();
            }

            // Validate if branch stock transfer is already posted, void, approved or pending
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

            // invoice void
            if (details.getBstCode() != null && details.getBstVoid() == EJBCommon.TRUE
                    && invBranchStockTransfer.getBstPosted() == EJBCommon.FALSE) {

                invBranchStockTransfer.setBstVoid(EJBCommon.TRUE);
                invBranchStockTransfer.setBstLastModifiedBy(details.getBstLastModifiedBy());
                invBranchStockTransfer.setBstDateLastModified(details.getBstDateLastModified());

                return invBranchStockTransfer.getBstCode();
            }

            // validate if document number is unique and if document number is automatic then set next sequence
            if (details.getBstCode() == null) {
                LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
                LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
                try {
                    adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("INV BRANCH STOCK TRANSFER-IN", companyCode);
                    adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(
                            adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, companyCode);
                }
                catch (FinderException ex) {
                }

                LocalInvBranchStockTransfer invBranchExistingStockTransfer = null;
                try {
                    invBranchExistingStockTransfer = invBranchStockTransferHome.findByBstNumberAndBrCode(details.getBstNumber(), AD_BRNCH, companyCode);
                }
                catch (FinderException ex) {
                }

                if (invBranchExistingStockTransfer != null) {
                    throw new GlobalDocumentNumberNotUniqueException();
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

            } else {

                LocalInvBranchStockTransfer invBranchExistingStockTransfer = null;

                try {

                    invBranchExistingStockTransfer = invBranchStockTransferHome
                            .findByBstNumberAndBrCode(details.getBstNumber(), AD_BRNCH, companyCode);

                }
                catch (FinderException ex) {
                }

                if (invBranchExistingStockTransfer != null
                        && !invBranchExistingStockTransfer.getBstCode().equals(details.getBstCode())) {

                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (invBranchStockTransfer.getBstNumber() != details.getBstNumber()
                        && (details.getBstNumber() == null || details.getBstNumber().trim().length() == 0)) {

                    details.setBstNumber(invBranchStockTransfer.getBstNumber());
                }
            }

            // lock branch stock out
            LocalInvBranchStockTransfer invBranchExistingStockTransfer = null;
            try {
                invBranchExistingStockTransfer = invBranchStockTransferHome.findByBstNumberAndBrCode(
                        details.getBstNumber(), AD_BRNCH, companyCode);

                if ((invBranchStockTransfer == null || (invBranchStockTransfer != null
                        && invBranchStockTransfer.getBstNumber() != null
                        && (!invBranchStockTransfer.getBstNumber().equals(details.getBstNumber()))))) {
                    if (invBranchExistingStockTransfer.getBstLock() == EJBCommon.TRUE) {
                        throw new GlobalTransactionAlreadyLockedException();
                    }
                    invBranchExistingStockTransfer.setBstLock(EJBCommon.TRUE);
                }
            }
            catch (Exception ex) {

            }

            // used in checking if branch stock transfer should re-generate distribution records
            boolean isRecalculate = true;

            // create branch stock transfer
            if (details.getBstCode() == null) {

                invBranchStockTransfer = invBranchStockTransferHome.create(details.getBstDate(), "IN",
                        details.getBstNumber(), details.getBstTransferOutNumber(), null, details.getBstDescription(),
                        details.getBstApprovalStatus(), details.getBstPosted(), details.getBstReasonForRejection(),
                        details.getBstCreatedBy(), details.getBstDateCreated(), details.getBstLastModifiedBy(),
                        details.getBstDateLastModified(), details.getBstApprovedRejectedBy(),
                        details.getBstDateApprovedRejected(), details.getBstPostedBy(), details.getBstDatePosted(),
                        EJBCommon.FALSE, EJBCommon.FALSE, AD_BRNCH, companyCode);

            } else {

                if (bslList.size() != invBranchStockTransfer.getInvBranchStockTransferLines().size()
                        || !(invBranchStockTransfer.getBstDate().equals(details.getBstDate()))) {

                    isRecalculate = true;

                } else if (bslList.size() == invBranchStockTransfer.getInvBranchStockTransferLines().size()) {

                    Iterator bslIter = invBranchStockTransfer.getInvBranchStockTransferLines().iterator();
                    Iterator bslIterList = bslList.iterator();

                    while (bslIter.hasNext()) {
                        LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) bslIter.next();
                        InvModBranchStockTransferLineDetails mdetails = (InvModBranchStockTransferLineDetails) bslIterList.next();
                        if (!invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName().equals(mdetails.getBslLocationName())
                                || invBranchStockTransferLine.getBslQuantityReceived() != mdetails.getBslQuantityReceived()
                                || invBranchStockTransferLine.getBslMisc() != mdetails.getBslMisc()) {
                            isRecalculate = true;
                            break;
                        }
                    }
                }

                invBranchStockTransfer.setBstType("IN");
                invBranchStockTransfer.setBstNumber(details.getBstNumber());
                invBranchStockTransfer.setBstDescription(details.getBstDescription());
                invBranchStockTransfer.setBstDate(details.getBstDate());
                invBranchStockTransfer.setBstTransferOutNumber(details.getBstTransferOutNumber());
                invBranchStockTransfer.setBstApprovalStatus(details.getBstApprovalStatus());
                invBranchStockTransfer.setBstLastModifiedBy(details.getBstLastModifiedBy());
                invBranchStockTransfer.setBstDateLastModified(details.getBstDateLastModified());
                invBranchStockTransfer.setBstReasonForRejection(null);
            }

            // get transfer out branch
            LocalInvBranchStockTransfer invBranchStockTransferOut = null;
            if (details.getBstBranchFrom().equals("")) {
                details.setBstBranchFrom(adBranchHome.findByPrimaryKey(
                        invBranchStockTransferHome.findBstByBstNumberAndAdBranchAndAdCompany(
                                details.getBstTransferOutNumber(), AD_BRNCH, companyCode).getBstAdBranch()).getBrName());
            }

            LocalAdBranch adBranchFrom = adBranchHome.findByBrName(details.getBstBranchFrom(), companyCode);
            adBranchFrom.addInvBranchStockTransfer(invBranchStockTransfer);

            LocalInvLocation invLocation = invLocationHome.findByLocName(details.getBstTransitLocation(), companyCode);
            invLocation.addInvBranchStockTransfer(invBranchStockTransfer);

            invBranchStockTransfer.setInvLocation(invLocation);

            double ABS_TOTAL_AMOUNT = 0d;
            if (isRecalculate) {
                // remove all branch stock transfer lines
                Iterator i = invBranchStockTransfer.getInvBranchStockTransferLines().iterator();
                short LINE_NUMBER = 0;
                while (i.hasNext()) {
                    LINE_NUMBER++;
                    LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) i.next();
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
                            invBranchStockTransferLine.getBslQuantityReceived(), companyCode);

                    invItemLocation
                            .setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - convertedQuantity);

                    // remove all inv tag inside branch stock transfer line
                    Collection invTags = invBranchStockTransferLine.getInvTags();
                    Iterator y = invTags.iterator();
                    while (y.hasNext()) {
                        LocalInvTag invTag = (LocalInvTag) y.next();
                        y.remove();
                        em.remove(invTag);
                    }
                    i.remove();
                    em.remove(invBranchStockTransferLine);
                }

                // remove all distribution records
                i = invBranchStockTransfer.getInvDistributionRecords().iterator();
                while (i.hasNext()) {
                    LocalInvDistributionRecord arDistributionRecord = (LocalInvDistributionRecord) i.next();
                    i.remove();
                    em.remove(arDistributionRecord);
                }

                // remove all inv branch stock transfer lines add new branch stock transfer entry lines and distribution record
                byte DEBIT = 0;
                i = bslList.iterator();
                while (i.hasNext()) {
                    InvModBranchStockTransferLineDetails mdetails = (InvModBranchStockTransferLineDetails) i.next();
                    LocalInvItemLocation invItemLocation = null;
                    try {

                        if (mdetails.getBslLocationName().equals("")) {
                            mdetails.setBslLocationName(invLocationHome.findByPrimaryKey(
                                            invItemHome.findByIiName(mdetails.getBslIiName(), companyCode).getIiDefaultLocation())
                                    .getLocName());
                        }

                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(mdetails.getBslLocationName(),
                                mdetails.getBslIiName(), companyCode);

                    }
                    catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException("Line "
                                + mdetails.getBslLineNumber() + " - " + mdetails.getBslLocationName());
                    }

                    if (mdetails.getBslUnitCost() == 0) {
                        mdetails.setBslUnitCost(
                                invItemHome.findByIiName(mdetails.getBslIiName(), companyCode).getIiUnitCost());
                    }
                    if (mdetails.getBslAmount() == 0) {
                        mdetails.setBslAmount(mdetails.getBslQuantityReceived() * mdetails.getBslUnitCost());
                    }

                    if (mdetails.getBslQuantity() == mdetails.getBslQuantityReceived()) {

                        for (Object o : invBranchStockTransferHome
                                .findBstByBstNumberAndAdBranchAndAdCompany(details.getBstTransferOutNumber(), AD_BRNCH,
                                        companyCode)
                                .getInvBranchStockTransferLines()) {
                            LocalInvBranchStockTransferLine line = (LocalInvBranchStockTransferLine) o;

                            // && mdetails.getBslQuantity()== line.getBslQuantity() &&
                            // mdetails.getBslUomName().equals(line.getInvUnitOfMeasure().getUomName())
                            if (mdetails.getBslIiName().equals(line.getInvItemLocation().getInvItem().getIiName())
                                    && mdetails.getBslUomName().equals(line.getInvUnitOfMeasure().getUomName())) {

                                mdetails.setBslQuantity(line.getBslQuantity());
                            }
                        }
                    }

                    LocalInvItemLocation invItemTransitLocation = null;

                    try {

                        invItemTransitLocation = invItemLocationHome.findByLocNameAndIiName(
                                details.getBstTransitLocation(), mdetails.getBslIiName(), companyCode);

                    }
                    catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(
                                "Transit Location " + details.getBstTransitLocation());
                    }

                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        // Start date validation
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(
                                invBranchStockTransfer.getBstDate(), invItemLocation.getInvItem().getIiName(),
                                invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);

                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }

                    LocalInvBranchStockTransferLine invBranchStockTransferLine = this.addInvBslEntry(mdetails,
                            invBranchStockTransfer, companyCode);

                    // add physical inventory distribution

                    double COST = invBranchStockTransferLine.getBslUnitCost();

                    double SHIPPING = mdetails.getBslShippingCost();

                    double AMOUNT = 0d;

                    AMOUNT = EJBCommon.roundIt(invBranchStockTransferLine.getBslQuantityReceived() * COST,
                            commonData.getGlFcPrecisionUnit(companyCode));

                    double SHIPPING_AMNT = 0d;

                    SHIPPING_AMNT = EJBCommon.roundIt(invBranchStockTransferLine.getBslQuantityReceived() * SHIPPING,
                            commonData.getGlFcPrecisionUnit(companyCode));

                    // check branch mapping

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

                    // add dr for inventory

                    this.addInvDrEntry(invBranchStockTransfer.getInvDrNextLine(), "INVENTORY", EJBCommon.TRUE,
                            Math.abs(AMOUNT), glChartOfAccount.getCoaCode(), invBranchStockTransfer, AD_BRNCH,
                            companyCode);

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
                                .findByPrimaryKey(adBranchItemTransitLocation.getBilCoaGlWipAccount());
                    }

                    // add dr for inventory transit location

                    this.addInvDrEntry(invBranchStockTransfer.getInvDrNextLine(), "IN TRANSIT", EJBCommon.FALSE,
                            Math.abs(AMOUNT - SHIPPING_AMNT), glChartOfAccountTransit.getCoaCode(),
                            invBranchStockTransfer, AD_BRNCH, companyCode);

                    // add dr for shipping if shipping amount > 0

                    if (SHIPPING_AMNT > 0d) {

                        LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(AD_BRNCH);
                        LocalGlChartOfAccount glChartOfAccountShipping = adBranch.getGlChartOfAccount();

                        this.addInvDrEntry(invBranchStockTransfer.getInvDrNextLine(), "INVENTORY", EJBCommon.FALSE,
                                Math.abs(SHIPPING_AMNT), glChartOfAccountShipping.getCoaCode(), invBranchStockTransfer,
                                AD_BRNCH, companyCode);
                    }

                    ABS_TOTAL_AMOUNT += Math.abs(AMOUNT);

                    // set ilCommittedQuantity

                    double convertedQuantity = this.convertByUomAndQuantity(
                            invBranchStockTransferLine.getInvUnitOfMeasure(), invItemLocation.getInvItem(),
                            invBranchStockTransferLine.getBslQuantityReceived(), companyCode);

                    invItemLocation
                            .setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() + convertedQuantity);

                    // TODO: add new inv Tag
                    // validate inventoriable and non-inventoriable items

                    if (mdetails.getTraceMisc() == 1) {
                        if (invItemLocation.getInvItem().getIiNonInventoriable() == 0
                                && mdetails.getBslTagList() != null
                                && mdetails.getBslTagList().size() < mdetails.getBslQuantityReceived()) {

                            throw new GlobalInvTagMissingException(invItemLocation.getInvItem().getIiName());
                        }

                        boolean isSerialNumberFound = false;
                        String serialNumber = "";
                        try {
                            Iterator t = mdetails.getBslTagList().iterator();
                            LocalInvTag invTag = null;

                            while (t.hasNext()) {
                                InvModTagListDetails tgLstDetails = (InvModTagListDetails) t.next();

                                if (invBranchStockTransferOut != null) {
                                    Iterator s = invBranchStockTransferOut.getInvBranchStockTransferLines().iterator();
                                    // check if serial number exists in the transfer out transaction
                                    isSerialNumberFound = false;
                                    serialNumber = tgLstDetails.getTgSerialNumber();
                                    while (s.hasNext()) {
                                        LocalInvBranchStockTransferLine tl = (LocalInvBranchStockTransferLine) s.next();
                                        for (Object o : tl.getInvTags()) {
                                            LocalInvTag iT = (LocalInvTag) o;
                                            if (tgLstDetails.getTgSerialNumber().equals(iT.getTgSerialNumber())) {
                                                isSerialNumberFound = true;
                                                break;
                                            }
                                        }
                                        if (isSerialNumberFound) {
                                            break;
                                        }
                                    }
                                }
                                if (invItemLocation.getInvItem().getIiNonInventoriable() == 0
                                        && tgLstDetails.getTgCustodian().equals("")
                                        && tgLstDetails.getTgSpecs().equals("")
                                        && tgLstDetails.getTgPropertyCode().equals("")
                                        && tgLstDetails.getTgExpiryDate() == null
                                        && tgLstDetails.getTgSerialNumber().equals("")) {
                                    throw new GlobalInvTagMissingException(
                                            invItemLocation.getInvItem().getIiDescription());
                                }
                                if ((!isSerialNumberFound) && (invBranchStockTransferOut != null)) {
                                    throw new InvTagSerialNumberNotFoundException(serialNumber);
                                }
                                if (tgLstDetails.getTgCode() == null) {

                                    invTag = invTagHome.create(tgLstDetails.getTgPropertyCode(),
                                            tgLstDetails.getTgSerialNumber(), null, tgLstDetails.getTgExpiryDate(),
                                            tgLstDetails.getTgSpecs(), companyCode, tgLstDetails.getTgTransactionDate(),
                                            tgLstDetails.getTgType());

                                    invTag.setInvBranchStockTransferLine(invBranchStockTransferLine);
                                    LocalAdUser adUser = null;
                                    try {
                                        adUser = adUserHome.findByUsrName(tgLstDetails.getTgCustodian(), companyCode);
                                    }
                                    catch (FinderException ex) {

                                    }
                                    invTag.setAdUser(adUser);
                                }
                            }

                        }
                        catch (GlobalInvTagMissingException ex) {
                            if (invItemLocation.getInvItem().getIiNonInventoriable() == 0) {
                                throw new GlobalInvTagMissingException(mdetails.getBslIiName());
                            }
                        }
                        catch (InvTagSerialNumberNotFoundException e) {
                            if (!isSerialNumberFound) {
                                throw new InvTagSerialNumberNotFoundException(serialNumber);
                            }
                        }
                    }
                }

            } else {

                Iterator i = bslList.iterator();

                while (i.hasNext()) {

                    InvModBranchStockTransferLineDetails mdetails = (InvModBranchStockTransferLineDetails) i.next();

                    LocalInvItemLocation invItemLocation = null;

                    try {

                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(mdetails.getBslLocationName(),
                                mdetails.getBslIiName(), companyCode);

                    }
                    catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException("Line "
                                + mdetails.getBslLineNumber() + " - " + mdetails.getBslLocationName());
                    }

                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        // start date validation

                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(
                                invBranchStockTransfer.getBstDate(), invItemLocation.getInvItem().getIiName(),
                                invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);

                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }

                    i = invBranchStockTransfer.getInvDistributionRecords().iterator();

                    while (i.hasNext()) {

                        LocalInvDistributionRecord distributionRecord = (LocalInvDistributionRecord) i.next();

                        if (distributionRecord.getDrDebit() == 1) {

                            ABS_TOTAL_AMOUNT += distributionRecord.getDrAmount();
                        }
                    }
                    LocalInvBranchStockTransferLine invBranchStockTransferLine = invBranchStockTransferLineHome
                            .findByPrimaryKey(mdetails.getBslCode());
                    // remove all inv tag inside PO line
                    Collection invTags = invBranchStockTransferLine.getInvTags();

                    Iterator y = invTags.iterator();

                    while (y.hasNext()) {

                        LocalInvTag invTag = (LocalInvTag) y.next();

                        y.remove();

                        // invTag.entityRemove();
                        em.remove(invTag);
                    }

                    if (mdetails.getTraceMisc() == 1) {

                        if (invItemLocation.getInvItem().getIiNonInventoriable() == 0
                                && mdetails.getBslTagList() != null
                                && mdetails.getBslTagList().size() < mdetails.getBslQuantityReceived()) {

                            throw new GlobalInvTagMissingException(invItemLocation.getInvItem().getIiName());
                        }
                        try {

                            for (Object o : mdetails.getBslTagList()) {
                                InvModTagListDetails tgLstDetails = (InvModTagListDetails) o;

                                if (invItemLocation.getInvItem().getIiNonInventoriable() == 0
                                        && tgLstDetails.getTgCustodian().equals("")
                                        && tgLstDetails.getTgSpecs().equals("")
                                        && tgLstDetails.getTgPropertyCode().equals("")
                                        && tgLstDetails.getTgExpiryDate() == null
                                        && tgLstDetails.getTgSerialNumber().equals("")) {
                                    throw new GlobalInvTagMissingException(
                                            invItemLocation.getInvItem().getIiDescription());
                                }
                                LocalInvTag invTag = invTagHome.create(tgLstDetails.getTgPropertyCode(),
                                        tgLstDetails.getTgSerialNumber(), null, tgLstDetails.getTgExpiryDate(),
                                        tgLstDetails.getTgSpecs(), companyCode, tgLstDetails.getTgTransactionDate(),
                                        tgLstDetails.getTgType());

                                invTag.setInvBranchStockTransferLine(invBranchStockTransferLine);
                                LocalAdUser adUser = null;
                                try {
                                    adUser = adUserHome.findByUsrName(tgLstDetails.getTgCustodian(), companyCode);
                                }
                                catch (FinderException ex) {

                                }
                                invTag.setAdUser(adUser);
                            }
                        }
                        catch (Exception ex) {
                            if (invItemLocation.getInvItem().getIiNonInventoriable() == 0) {
                                throw new GlobalInvTagMissingException(mdetails.getBslIiName());
                            }
                        }
                    }
                }
            }

            // generate approval status

            String INV_APPRVL_STATUS = !isDraft ? "N/A" : null;

            if (INV_APPRVL_STATUS != null && INV_APPRVL_STATUS.equals("N/A")
                    && adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                this.executeInvBstInPost(invBranchStockTransfer.getBstCode(),
                        invBranchStockTransfer.getBstLastModifiedBy(), adBranchFrom.getBrCode(), AD_BRNCH, companyCode);

                // Set SO Lock if Fully Served
                Debug.print("details.getBstTransferOutNumber()=" + details.getBstTransferOutNumber());

                invBranchExistingStockTransfer = invBranchStockTransferHome
                        .findByBstNumberAndAdCompany(details.getBstTransferOutNumber(), companyCode);

                Iterator stOter = invBranchExistingStockTransfer.getInvBranchStockTransferLines().iterator();
                Debug.print("invBranchExistingStockTransfer.getBstNumber()="
                        + invBranchExistingStockTransfer.getBstNumber());
                boolean isOpenBST = false;

                while (stOter.hasNext()) {

                    LocalInvBranchStockTransferLine invBranchStockTransferLineOut = (LocalInvBranchStockTransferLine) stOter
                            .next();

                    Collection invAllBstReceives = invBranchStockTransferLineHome
                            .findInBslByBstTransferOutNumberAndItemLocAndAdBranchAndAdCompany(
                                    details.getBstTransferOutNumber(),
                                    invBranchStockTransferLineOut.getInvItemLocation().getIlCode(), AD_BRNCH, companyCode);

                    Iterator stIter = invAllBstReceives.iterator();

                    double QUANTITY_RCVD = 0d;

                    while (stIter.hasNext()) {

                        LocalInvBranchStockTransferLine invBranchStockTransferLineIn = (LocalInvBranchStockTransferLine) stIter
                                .next();

                        if (invBranchStockTransferLineIn.getInvBranchStockTransfer().getBstPosted() == EJBCommon.TRUE) {

                            QUANTITY_RCVD += invBranchStockTransferLineIn.getBslQuantityReceived();
                        }
                    }

                    double TOTAL_REMAINING_QTY = invBranchStockTransferLineOut.getBslQuantity() - QUANTITY_RCVD;

                    Debug.print("TOTAL_REMAINING_QTY=" + TOTAL_REMAINING_QTY);

                    if (TOTAL_REMAINING_QTY > 0) {
                        isOpenBST = true;
                        break;
                    }
                }

                if (isOpenBST) {
                    invBranchExistingStockTransfer.setBstLock(EJBCommon.FALSE);
                } else {
                    invBranchExistingStockTransfer.setBstLock(EJBCommon.TRUE);
                }
            }

            // set stock transfer approval status

            invBranchStockTransfer.setBstApprovalStatus(INV_APPRVL_STATUS);
            Debug.print("InvBranchStockTransferInEntryControllerBean saveInvBstEntry " + txnStartDate);
            return invBranchStockTransfer.getBstCode();

        }
        catch (InvATRAssemblyQtyGreaterThanAvailableQtyException | InvTagSerialNumberNotFoundException |
               GlobalExpiryDateNotFoundException | GlobalMiscInfoIsRequiredException |
               AdPRFCoaGlVarianceAccountNotFoundException | GlobalBranchAccountNumberInvalidException |
               GlobalDocumentNumberNotUniqueException | GlobalInventoryDateException |
               GlobalJournalNotBalanceException | GlJREffectiveDatePeriodClosedException |
               GlJREffectiveDateNoPeriodExistException | GlobalInvTagMissingException |
               GlobalInvItemLocationNotFoundException | GlobalTransactionAlreadyPostedException |
               GlobalTransactionAlreadyPendingException | GlobalTransactionAlreadyApprovedException |
               GlobalRecordAlreadyDeletedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteInvBstEntry(Integer BST_CODE, String type, String AD_USR, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException {

        Debug.print("InvBranchStockTransferInEntryControllerBean deleteInvBstEntry");
        try {

            LocalInvBranchStockTransfer invBranchStockTransfer = invBranchStockTransferHome.findByPrimaryKey(BST_CODE);

            for (Object o : invBranchStockTransfer.getInvBranchStockTransferLines()) {

                LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) o;

                LocalInvItemLocation invItemLocation = invItemLocationHome.findByLocNameAndIiName(
                        invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName(),
                        invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(), companyCode);

                double convertedQuantity = this.convertByUomAndQuantity(
                        invBranchStockTransferLine.getInvUnitOfMeasure(), invItemLocation.getInvItem(),
                        Math.abs(invBranchStockTransferLine.getBslQuantityReceived()), companyCode);

                invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - convertedQuantity);
            }

            if (invBranchStockTransfer.getBstApprovalStatus() != null
                    && invBranchStockTransfer.getBstApprovalStatus().equals("PENDING")) {

                Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode(
                        "INV BRANCH STOCK TRANSFER", invBranchStockTransfer.getBstCode(), companyCode);

                for (Object approvalQueue : adApprovalQueues) {

                    LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) approvalQueue;

                    // adApprovalQueue.entityRemove();
                    em.remove(adApprovalQueue);
                }
            }

            // unlock correponding transfer out

            LocalInvBranchStockTransfer invBranchExistingStockTransfer = null;

            try {

                Debug.print("LOCK ---------------->");

                invBranchExistingStockTransfer = invBranchStockTransferHome
                        .findByBstNumberAndAdCompany(invBranchStockTransfer.getBstTransferOutNumber(), companyCode);

                invBranchExistingStockTransfer.setBstLock(EJBCommon.FALSE);

            }
            catch (Exception ex) {

            }

            adDeleteAuditTrailHome.create("INV BRANCH STOCK TRANSFER IN", invBranchStockTransfer.getBstDate(),
                    invBranchStockTransfer.getBstNumber(), invBranchStockTransfer.getBstTransferOutNumber(), 0d, AD_USR,
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

    public short getInvGpQuantityPrecisionUnit(Integer companyCode) {

        Debug.print("InvBranchStockTransferInEntryControllerBean getInvGpQuantityPrecisionUnit");
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

        Debug.print("InvBranchStockTransferInEntryControllerBean getInvGpInventoryLineNumber");
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

        Debug.print("InvBranchStockTransferInEntryControllerBean getAdApprovalNotifiedUsersByBstCode");
        ArrayList list = new ArrayList();
        try {

            LocalInvBranchStockTransfer invBranchStockTransfer = invBranchStockTransferHome.findByPrimaryKey(BST_CODE);

            if (invBranchStockTransfer.getBstPosted() == EJBCommon.TRUE) {

                list.add("DOCUMENT POSTED");
                return list;
            }

            Collection adApprovalQueues = adApprovalQueueHome
                    .findByAqDocumentAndAqDocumentCode("INV BRANCH STOCK TRANSFER", BST_CODE, companyCode);

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
                                                                      Date ST_DT, String BR_NM, Integer companyCode) {

        Debug.print("InvBranchStockTransferOutEntryControllerBean getInvIiUnitCostByIiNameAndUomName");
        try {

            LocalAdBranch adBranch = adBranchHome.findByBrName(BR_NM, companyCode);
            Debug.print(companyCode + ":companyCode **** II_NM: " + II_NM);
            LocalInvItem invItem = invItemHome.findByIiName(II_NM, companyCode);

            double COST = invItem.getIiUnitCost();

            LocalInvItemLocation invItemLocation = null;

            try {

                invItemLocation = invItemLocationHome.findByLocNameAndIiName(LOC_FRM, II_NM, companyCode);

                LocalInvCosting invCosting = invCostingHome
                        .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(ST_DT,
                                invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(),
                                adBranch.getBrCode(), companyCode);

                switch (invItemLocation.getInvItem().getIiCostMethod()) {
                    case "Average":

                        if (invCosting.getCstRemainingQuantity() <= 0) {
                            COST = EJBCommon.roundIt(Math.abs(invItemLocation.getInvItem().getIiUnitCost()),
                                    commonData.getGlFcPrecisionUnit(companyCode));
                        } else {
                            COST = EJBCommon.roundIt(
                                    Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity()),
                                    commonData.getGlFcPrecisionUnit(companyCode));
                            if (COST <= 0) {
                                COST = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                            }
                        }

                        break;
                    case "FIFO":
                        COST = this.getInvFifoCost(ST_DT, invItemLocation.getIlCode(), invCosting.getCstAdjustQuantity(),
                                invCosting.getCstAdjustCost(), false, adBranch.getBrCode(), companyCode);
                        break;
                    case "Standard":
                        COST = invItemLocation.getInvItem().getIiUnitCost();
                        break;
                }

            }
            catch (FinderException ex) {

            }

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(II_NM, UOM_NM, companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(II_NM, invItem.getInvUnitOfMeasure().getUomName(), companyCode);

            return EJBCommon.roundIt(COST * invDefaultUomConversion.getUmcConversionFactor()
                    / invUnitOfMeasureConversion.getUmcConversionFactor(), commonData.getGlFcPrecisionUnit(companyCode));

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public LocalInvBranchStockTransfer getInvBstOutByBstNumberAndBstBranch(String BST_NMBR, String BR_NM,
                                                                           Integer AD_BRNCH, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("InvBranchStockTransferInEntryControllerBean getInvBstOutByBstNumber");
        try {

            LocalInvBranchStockTransfer invBranchStockTransfer = null;

            try {

                LocalAdBranch adBranch = adBranchHome.findByBrName(BR_NM, companyCode);

                invBranchStockTransfer = invBranchStockTransferHome.findByBstNumberAndBrCode(BST_NMBR,
                        adBranch.getBrCode(), companyCode);

            }
            catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            return invBranchStockTransfer;

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

    public double getInvIiShippingCostByIiUnitCostAndAdBranch(double II_UNT_CST, Integer AD_BRNCH, Integer companyCode) {

        Debug.print("InvBranchStockTransferOutEntryControllerBean getInvIiMarkupUnitCostByIiUnitCostAndAdBranch");
        try {

            LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(AD_BRNCH);

            double BSL_SHPPNG_CST = 0d;

            if (adBranch.getBrApplyShipping() == EJBCommon.TRUE) {

                BSL_SHPPNG_CST = EJBCommon.roundIt(II_UNT_CST * (adBranch.getBrPercentMarkup() / 100),
                        commonData.getGlFcPrecisionUnit(companyCode));
            }

            return BSL_SHPPNG_CST;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private String getAdBrNameByBrCode(Integer BR_CODE) {

        Debug.print("InvBranchStockTransferInEntryControllerBean getAdBrNameByBrCode");
        try {

            return adBranchHome.findByPrimaryKey(BR_CODE).getBrName();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalInvBranchStockTransferLine addInvBslEntry(InvModBranchStockTransferLineDetails mdetails,
                                                           LocalInvBranchStockTransfer invBranchStockTransfer, Integer companyCode)
            throws InvATRAssemblyQtyGreaterThanAvailableQtyException, GlobalMiscInfoIsRequiredException {

        Debug.print("InvBranchStockTransferInEntryControllerBean addInvBslEntry");
        try {
            Debug.print("mdetails.getBslQuantity()=" + mdetails.getBslQuantity());
            Debug.print("mdetails.getBslQuantityReceived()=" + mdetails.getBslQuantityReceived());
            if (mdetails.getBslQuantity() < mdetails.getBslQuantityReceived()) {
                throw new InvATRAssemblyQtyGreaterThanAvailableQtyException();
            }

            LocalInvBranchStockTransferLine invBranchStockTransferLine = invBranchStockTransferLineHome.create(
                    mdetails.getBslQuantity(), mdetails.getBslQuantityReceived(), mdetails.getBslUnitCost(),
                    mdetails.getBslAmount(), companyCode);

            // invBranchStockTransfer.addInvBranchStockTransferLine(invBranchStockTransferLine);
            invBranchStockTransferLine.setInvBranchStockTransfer(invBranchStockTransfer);

            if (mdetails.getBslUomName().equals("")) {
                mdetails.setBslUomName(invItemHome.findByPartNumber(mdetails.getBslIiName(), companyCode)
                        .getInvUnitOfMeasure().getUomName());
            }

            LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(mdetails.getBslUomName(),
                    companyCode);

            // invUnitOfMeasure.addInvBranchStockTransferLine(invBranchStockTransferLine);
            invBranchStockTransferLine.setInvUnitOfMeasure(invUnitOfMeasure);

            LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(mdetails.getBslIiName(),
                    mdetails.getBslLocationName(), companyCode);

            // invItemLocation.addInvBranchStockTransferLine(invBranchStockTransferLine);
            invBranchStockTransferLine.setInvItemLocation(invItemLocation);

            if (invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiTraceMisc() == 1) {
                // TODO: add new inv Tag

                this.createInvTagList(invBranchStockTransferLine, mdetails.getBslTagList(), companyCode);
            }
            // validate misc

            LocalInvBranchStockTransfer invBranchExistingStockTransfer = null;

            try {

                Debug.print("LOCK ---------------->");

                invBranchExistingStockTransfer = invBranchStockTransferHome.findByBstNumberAndAdCompany(
                        invBranchStockTransferLine.getInvBranchStockTransfer().getBstTransferOutNumber(), companyCode);
                invBranchExistingStockTransfer.setBstLock(EJBCommon.TRUE);

            }
            catch (Exception ex) {

            }

            return invBranchStockTransferLine;

        }
        catch (InvATRAssemblyQtyGreaterThanAvailableQtyException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

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

        Debug.print("InvBranchStockTransferOutEntryControllerBean convertByUomFromAndUomToAndQuantity");
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
                               LocalInvBranchStockTransfer invBranchStockTransfer, Integer AD_BRNCH, Integer companyCode)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("InvBranchStockTransferInEntryControllerBean addInvDrEntry");
        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate coa

            LocalGlChartOfAccount glChartOfAccount = null;
            Debug.print("COA_CODE: " + COA_CODE);
            Debug.print("AD_BRNCH: " + AD_BRNCH);
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

            // invBranchStockTransfer.addInvDistributionRecord(invDistributionRecord);
            invDistributionRecord.setInvBranchStockTransfer(invBranchStockTransfer);
            invDistributionRecord.setInvBranchStockTransfer(invBranchStockTransfer);

            // glChartOfAccount.addInvDistributionRecord(invDistributionRecord);
            invDistributionRecord.setInvChartOfAccount(glChartOfAccount);

        }
        catch (GlobalBranchAccountNumberInvalidException ex) {

            Debug.print("invalid coa: " + COA_CODE);

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void executeInvBstInPost(Integer BST_CODE, String USR_NM, Integer BRNCH_FRM, Integer AD_BRNCH,
                                     Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException,
            GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException,
            GlobalJournalNotBalanceException, AdPRFCoaGlVarianceAccountNotFoundException,
            GlobalExpiryDateNotFoundException {

        Debug.print("InvBranchStockTransferInEntryControllerBean executeInvBstInPost");
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

                double BST_QTY = this.convertByUomAndQuantity(invBranchStockTransferLine.getInvUnitOfMeasure(),
                        invBranchStockTransferLine.getInvItemLocation().getInvItem(),
                        invBranchStockTransferLine.getBslQuantityReceived(), companyCode);

                LocalInvCosting invCosting = null;

                try {

                    invCosting = invCostingHome
                            .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                    invBranchStockTransfer.getBstDate(), invItemName, locName, AD_BRNCH, companyCode);

                }
                catch (FinderException ex) {
                }

                double BST_COST = invBranchStockTransferLine.getBslUnitCost();

                if (invCosting == null) {

                    this.postToInv(invBranchStockTransferLine, invItemLocationFrom, invBranchStockTransfer.getBstDate(),
                            BST_QTY, BST_COST * BST_QTY, BST_QTY, BST_COST * BST_QTY, 0d, null, AD_BRNCH, companyCode);

                } else {

                    this.postToInv(invBranchStockTransferLine, invItemLocationFrom, invBranchStockTransfer.getBstDate(),
                            BST_QTY, BST_COST, invCosting.getCstRemainingQuantity() + BST_QTY,
                            invCosting.getCstRemainingValue() + (BST_QTY * BST_COST), 0d, USR_NM, AD_BRNCH, companyCode);
                }
            }

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
        catch (GlJREffectiveDateNoPeriodExistException | GlobalExpiryDateNotFoundException |
               AdPRFCoaGlVarianceAccountNotFoundException | GlobalTransactionAlreadyPostedException |
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
                                commonData.getGlFcPrecisionUnit(companyCode));
                    } else if (invFifoCosting.getArInvoiceLineItem() != null) {
                        return EJBCommon.roundIt(
                                invFifoCosting.getCstCostOfSales() / invFifoCosting.getCstQuantitySold(),
                                commonData.getGlFcPrecisionUnit(companyCode));
                    } else {
                        return EJBCommon.roundIt(
                                invFifoCosting.getCstAdjustCost() / invFifoCosting.getCstAdjustQuantity(),
                                commonData.getGlFcPrecisionUnit(companyCode));
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

    private void postToInv(LocalInvBranchStockTransferLine invBranchStockTransferLine,
                           LocalInvItemLocation invItemLocation, Date CST_DT, double CST_ST_QTY, double CST_ST_CST,
                           double CST_RMNNG_QTY, double CST_RMNNG_VL, double CST_VRNC_VL, String USR_NM, Integer AD_BRNCH,
                           Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException {

        Debug.print("InvBranchStockTransferInEntryControllerBean post");
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
            double qtyPrpgt2 = 0;
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
                Debug.print("prevExpiryDates CATCH: " + prevExpiryDates);
                prevExpiryDates = "";
            }

            // create costing
            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d,
                    CST_ST_QTY, CST_ST_CST, 0d, 0d, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, 0d, AD_BRNCH, companyCode);
            // invItemLocation.addInvCosting(invCosting);
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setInvBranchStockTransferLine(invBranchStockTransferLine);

            invCosting.setCstQuantity(CST_ST_QTY);
            invCosting.setCstCost(CST_ST_CST);

            // Get Latest Expiry Dates

            if (prevExpiryDates != null && prevExpiryDates != "" && prevExpiryDates.length() != 0
                    && invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiTraceMisc() != 0) {

                String CST_EXPRY_DT = prevExpiryDates;

                if (invBranchStockTransferLine.getBslMisc() != null && invBranchStockTransferLine.getBslMisc() != ""
                        && invBranchStockTransferLine.getBslMisc().length() != 0) {

                    String TXN_MISC = invBranchStockTransferLine.getBslMisc();

                    TXN_MISC = TXN_MISC.substring(1);
                    TXN_MISC = TXN_MISC.substring(TXN_MISC.indexOf(EJBCommon.DELIMETER));

                    Iterator txnMiscsIter = EJBCommon.miscList(TXN_MISC).iterator();
                    List<String> txnMiscsList = EJBCommon.miscList(CST_EXPRY_DT);
                    Debug.print(CST_ST_QTY + " :CST_EXPRY_DT: " + CST_EXPRY_DT);
                    StringBuilder str = new StringBuilder(CST_EXPRY_DT);

                    if (CST_ST_QTY > 0) {
                        while (txnMiscsIter.hasNext()) {
                            String misc = (String) txnMiscsIter.next();
                            str.append(misc);
                            str.append(EJBCommon.DELIMETER);
                        }

                    } else {

                        // Iterator outMiscIter = txnMiscsList.iterator();

                        while (txnMiscsIter.hasNext()) {

                            String misc = (String) txnMiscsIter.next();
                            Debug.print("misc: " + misc);

                            if (txnMiscsList.contains(misc)) {
                                txnMiscsList.remove(misc);
                            } else {
                                throw new GlobalExpiryDateNotFoundException(
                                        invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName());
                            }

                            Debug.print("CST_EXPRY_DT: " + CST_EXPRY_DT);
                        }

                        if (txnMiscsList.size() > 0) {
                            str = new StringBuilder(EJBCommon.DELIMETER);
                        } else {
                            str = new StringBuilder();
                        }

                        for (Object o : txnMiscsList) {
                            String misc = (String) o;
                            str.append(misc);
                            str.append(EJBCommon.DELIMETER);
                        }
                    }
                    CST_EXPRY_DT = str.toString();
                    Debug.print("FIN CST_EXPRY_DT: " + CST_EXPRY_DT);
                    invCosting.setCstExpiryDate(CST_EXPRY_DT);

                } else {
                    invCosting.setCstExpiryDate(prevExpiryDates);
                    Debug.print("prevExpiryDates");
                }
            } else {

                invCosting.setCstExpiryDate("");
            }

            // if cost variance is not 0, generate cost variance for the transaction
            if (CST_VRNC_VL != 0) {
            }

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT,
                    invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH,
                    companyCode);

            i = invCostings.iterator();

            String miscList = "";
            ArrayList miscList2 = null;

            // Debug.print("miscList Propagate:" + miscList);
            String propagateMisc = "";
            String ret = "";

            // String CST_EXPRY_DT = "";
            while (i.hasNext()) {
                String Checker = "";
                String Checker2 = "";

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) i.next();

                if (invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiTraceMisc() != 0) {
                    if (invBranchStockTransferLine.getBslMisc() != null && invBranchStockTransferLine.getBslMisc() != ""
                            && invBranchStockTransferLine.getBslMisc().length() != 0
                            && invPropagatedCosting.getCstExpiryDate() != null
                            && invPropagatedCosting.getCstExpiryDate() != "") {

                        Debug.print(
                                "invBranchStockTransferLine.getBslMisc(): " + invBranchStockTransferLine.getBslMisc());
                        Debug.print("getAlAdjustQuantity(): " + invBranchStockTransferLine.getBslQuantity());

                        String CST_EXPRY_DT = invPropagatedCosting.getCstExpiryDate();

                        Debug.print("invPropagatedCosting.getCstCode(): " + invPropagatedCosting.getCstCode());
                        Debug.print("CST_EXPRY_DT: " + CST_EXPRY_DT);

                        String TXN_MISC = invBranchStockTransferLine.getBslMisc();

                        TXN_MISC = TXN_MISC.substring(1);
                        TXN_MISC = TXN_MISC.substring(TXN_MISC.indexOf(EJBCommon.DELIMETER));

                        Iterator txnMiscsIter = EJBCommon.miscList(TXN_MISC).iterator();
                        List<String> txnMiscsList = EJBCommon.miscList(CST_EXPRY_DT);

                        StringBuilder str = new StringBuilder(CST_EXPRY_DT);

                        if (CST_ST_QTY > 0) {
                            while (txnMiscsIter.hasNext()) {
                                String misc = (String) txnMiscsIter.next();
                                str.append(misc);
                                str.append(EJBCommon.DELIMETER);
                            }

                        } else {

                            // Iterator outMiscIter = txnMiscsList.iterator();

                            while (txnMiscsIter.hasNext()) {

                                String misc = (String) txnMiscsIter.next();
                                Debug.print("misc: " + misc);

                                if (txnMiscsList.contains(misc)) {
                                    txnMiscsList.remove(misc);
                                } else {
                                    throw new GlobalExpiryDateNotFoundException(
                                            invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName());
                                }
                            }

                            if (txnMiscsList.size() > 0) {
                                str = new StringBuilder(EJBCommon.DELIMETER);
                            } else {
                                str = new StringBuilder();
                            }

                            for (Object o : txnMiscsList) {
                                String misc = (String) o;
                                str.append(misc);
                                str.append(EJBCommon.DELIMETER);
                            }
                        }
                        Debug.print("str.toString(): " + str);
                        if (invPropagatedCosting.getCstExpiryDate() != null
                                && invPropagatedCosting.getCstExpiryDate() != "") {
                            CST_EXPRY_DT = str.toString();
                            invPropagatedCosting.setCstExpiryDate(CST_EXPRY_DT);
                        }
                    } else {
                        invPropagatedCosting.setCstExpiryDate(prevExpiryDates);
                        Debug.print("prevExpiryDates");
                    }
                }

                invPropagatedCosting
                        .setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_ST_QTY);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_ST_QTY);
            }

            // regenerate cost varaince
            // this.regenerateCostVariance(invCostings, invCosting, AD_BRNCH, companyCode);

        }
        catch (GlobalExpiryDateNotFoundException ex) {
            Debug.print("Huli Ka");
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

    public String propagateExpiryDates(String misc, double qty, String reverse) throws Exception {
        // ActionErrors errors = new ActionErrors();
        Debug.print("InvBranchStockTransferOutControllerBean getExpiryDates");
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

        Debug.print("InvBranchStockTransferInEntryControllerBean postToGl");
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

    private void regenerateInventoryDr(LocalInvBranchStockTransfer invBranchStockTransfer, Integer AD_BRNCH,
                                       Integer companyCode) throws GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException {

        Debug.print("InvBranchStockTransferInEntryControllerBean regenerateInventoryDr");
        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            // regenerate inventory distribution records

            Collection invDistributionRecords = invDistributionRecordHome
                    .findImportableDrByBstCode(invBranchStockTransfer.getBstCode(), companyCode);

            Iterator i = invDistributionRecords.iterator();

            while (i.hasNext()) {

                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) i.next();

                i.remove();
                // invDistributionRecord.entityRemove();
                em.remove(invDistributionRecord);
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

                if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                    // start date validation

                    Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(
                            invBranchStockTransfer.getBstDate(), invItemName, locName, AD_BRNCH, companyCode);

                    if (!invNegTxnCosting.isEmpty()) {
                        throw new GlobalInventoryDateException(invItemName);
                    }
                }

                // add physical inventory distribution

                double COST = invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiUnitCost();

                try {

                    LocalInvCosting invCosting = invCostingHome
                            .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                    invBranchStockTransfer.getBstDate(), invItemLocation.getInvItem().getIiName(),
                                    invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);

                    if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Average")) {
                        COST = invCosting.getCstRemainingQuantity() <= 0 ? COST
                                : Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                    }

                    if (COST <= 0) {
                        COST = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                    } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("FIFO")) {
                        COST = invCosting.getCstRemainingQuantity() <= 0 ? COST
                                : this.getInvFifoCost(invBranchStockTransfer.getBstDate(), invItemLocation.getIlCode(),
                                invBranchStockTransferLine.getBslQuantity(),
                                invBranchStockTransferLine.getBslUnitCost(), false, AD_BRNCH, companyCode);
                    } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Standard")) {
                        COST = invItemLocation.getInvItem().getIiUnitCost();
                    }

                }
                catch (FinderException ex) {
                }

                double AMOUNT = 0d;

                AMOUNT = EJBCommon.roundIt(invBranchStockTransferLine.getBslQuantityReceived() * COST,
                        commonData.getGlFcPrecisionUnit(companyCode));

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

                this.addInvDrEntry(invBranchStockTransfer.getInvDrNextLine(), "INVENTORY", EJBCommon.TRUE,
                        Math.abs(AMOUNT), glChartOfAccount.getCoaCode(), invBranchStockTransfer, AD_BRNCH, companyCode);

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
                            .findByPrimaryKey(invItemTransitLocation.getIlGlCoaWipAccount());

                } else {

                    glChartOfAccountTransit = glChartOfAccountHome
                            .findByPrimaryKey(adBranchItemTransitLocation.getBilCoaGlWipAccount());
                }

                // add dr for inventory transit location

                this.addInvDrEntry(invBranchStockTransfer.getInvDrNextLine(), "IN TRANSIT", EJBCommon.FALSE,
                        Math.abs(AMOUNT), glChartOfAccountTransit.getCoaCode(), invBranchStockTransfer, AD_BRNCH,
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

    private void voidInvAdjustment(LocalInvAdjustment invAdjustment, Integer AD_BRNCH, Integer companyCode) {

        Debug.print("InvBranchStockTransferInEntryControllerBean voidInvAdjustment");

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

    private void addInvDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, byte DR_RVRSL,
                               Integer COA_CODE, LocalInvAdjustment invAdjustment, Integer AD_BRNCH, Integer companyCode)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("InvBranchStockTransferInEntryControllerBean addInvDrEntry");
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

    private void executeInvAdjPost(Integer ADJ_CODE, String USR_NM, Integer AD_BRNCH, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException,
            GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException,
            GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException {

        Debug.print("InvBranchStockTransferInEntryControllerBean executeInvAdjPost");
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
                // glChartOfAccount.addGlJournalLine(glOffsetJournalLine);
                glOffsetJournalLine.setGlChartOfAccount(glChartOfAccount);

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

            LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("BRANCH STOCK TRANSFERS",
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

        Debug.print("InvBranchStockTransferInEntryControllerBean addInvAlEntry");
        try {

            // create dr entry
            LocalInvAdjustmentLine invAdjustmentLine = null;
            invAdjustmentLine = invAdjustmentLineHome.create(CST_VRNC_VL, null, null, 0, 0, AL_VD, companyCode);

            // map adjustment, unit of measure, item location
            // invAdjustment.addInvAdjustmentLine(invAdjustmentLine);
            invAdjustmentLine.setInvAdjustment(invAdjustment);
            // invItemLocation.getInvItem().getInvUnitOfMeasure().addInvAdjustmentLine(invAdjustmentLine);
            invAdjustmentLine.setInvUnitOfMeasure(invItemLocation.getInvItem().getInvUnitOfMeasure());
            // invItemLocation.addInvAdjustmentLine(invAdjustmentLine);
            invAdjustmentLine.setInvItemLocation(invItemLocation);

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

        Debug.print("InvBranchStockTransferInEntryControllerBean postInvAdjustmentToInventory");
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
            // invItemLocation.addInvCosting(invCosting);
            invCosting.setInvItemLocation(invItemLocation);
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

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("InvBranchStockTransferInEntryControllerBean ejbCreate");
    }

}