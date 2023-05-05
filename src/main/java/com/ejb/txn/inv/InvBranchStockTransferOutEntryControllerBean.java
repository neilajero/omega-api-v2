package com.ejb.txn.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ar.LocalArSalesOrderHome;
import com.ejb.dao.gl.*;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ar.LocalArSalesOrder;
import com.ejb.entities.gl.*;
import com.ejb.entities.inv.*;
import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.ejb.exception.inv.InvTagSerialNumberNotFoundException;
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
import java.util.*;

@Stateless(name = "InvBranchStockTransferOutEntryControllerEJB")
public class InvBranchStockTransferOutEntryControllerBean extends EJBContextClass
        implements InvBranchStockTransferOutEntryController {

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
    private LocalInvBranchStockTransferLineHome invBranchStockTransferLineHome;
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
    private LocalInvTagHome invTagHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalAdDeleteAuditTrailHome adDeleteAuditTrailHome;
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
    @EJB
    private LocalArSalesOrderHome arSalesOrderHome;
    @EJB
    private LocalInvItemHome invItemHome;

    public InvModBranchStockTransferDetails getInvBstByBstCode(Date BSTI_DT, Integer BST_CODE, Integer AD_BRNCH, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("InvBranchStockTransferOutEntryControllerBean getInvBstByBstCode");
        try {

            LocalInvBranchStockTransfer invBranchStockTransfer = null;

            try {
                Debug.print("BST_CODE=" + BST_CODE);
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

                mdetails.setTraceMisc(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiTraceMisc());

                if (mdetails.getTraceMisc() == 1) {

                    ArrayList tagList = new ArrayList();

                    tagList = this.getInvTagList(invBranchStockTransferLine);
                    mdetails.setBslTagList(tagList);
                    mdetails.setTraceMisc(mdetails.getTraceMisc());

                    mdetails.setBslTagList(tagList);
                }
                mdetails.setBslCode(invBranchStockTransferLine.getBslCode());
                mdetails.setBslIiName(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName());
                mdetails.setBslIiDescription(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiDescription());
                mdetails.setBslLocationName(invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName());
                mdetails.setBslUomName(invBranchStockTransferLine.getInvUnitOfMeasure().getUomName());
                mdetails.setBslUnitCost(invBranchStockTransferLine.getBslUnitCost());
                mdetails.setBslQuantity(invBranchStockTransferLine.getBslQuantity());
                mdetails.setBslAmount(invBranchStockTransferLine.getBslAmount());

                // if bst order, unit cost based on transit location

                if (invBranchStockTransfer.getBstType().equals("ORDER")) {

                    String BR_OUT_NM = this.getAdBrNameByBrCode(invBranchStockTransfer.getBstAdBranch());

                    double unitCost = this.getInvIiUnitCostByIiNameAndLocFromAndUomNameAndDate(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(), invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName(), invBranchStockTransferLine.getInvUnitOfMeasure().getUomName(), BSTI_DT, AD_BRNCH, companyCode);
                    double shippingCost = 0d;
                    shippingCost = this.getInvIiShippingCostByIiUnitCostAndAdBranch(unitCost, AD_BRNCH, companyCode);

                    mdetails.setBslShippingCost(shippingCost);
                    mdetails.setBslUnitCost(unitCost + shippingCost);

                    double AMOUNT = 0d;

                    AMOUNT = EJBCommon.roundIt(invBranchStockTransferLine.getBslQuantity() * mdetails.getBslUnitCost(), this.getGlFcPrecisionUnit(companyCode));

                    mdetails.setBslAmount(AMOUNT);

                    // get remaining qty
                    Collection invServedBsts = invBranchStockTransferLineHome.findByBstTransferOrderNumberAndIiNameAndLocNameAndBrCode(invBranchStockTransfer.getBstNumber(), invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(), invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName(), invBranchStockTransfer.getAdBranch().getBrCode(), companyCode);
                    Debug.print("invServed-" + invServedBsts.size());
                    double totalServed = 0;
                    for (Object servedBst : invServedBsts) {

                        LocalInvBranchStockTransferLine invServedBst = (LocalInvBranchStockTransferLine) servedBst;
                        totalServed += invServedBst.getBslQuantity();
                    }

                    if (totalServed == mdetails.getBslQuantity()) {
                        continue;
                    } else {
                        mdetails.setBslQuantity(mdetails.getBslQuantity() - totalServed);
                    }

                } else {
                    mdetails.setBslMisc(invBranchStockTransferLine.getBslMisc());
                    mdetails.setBslUnitCost(invBranchStockTransferLine.getBslUnitCost());
                    mdetails.setBslAmount(invBranchStockTransferLine.getBslAmount());
                }

                bslList.add(mdetails);
            }

            InvModBranchStockTransferDetails details = new InvModBranchStockTransferDetails();

            details.setBstCode(invBranchStockTransfer.getBstCode());
            details.setBstNumber(invBranchStockTransfer.getBstNumber());
            details.setBstDescription(invBranchStockTransfer.getBstDescription());
            details.setBstVoid(invBranchStockTransfer.getBstVoid());
            details.setBstDate(invBranchStockTransfer.getBstDate());
            details.setBstBranchTo(invBranchStockTransfer.getAdBranch().getBrBranchCode());
            details.setBstTransitLocation(invBranchStockTransfer.getInvLocation().getLocName());
            details.setBstTransferOrderNumber(invBranchStockTransfer.getBstTransferOrderNumber());

            try {
                this.getInvBstOutByBstNumberAndBstBranch(invBranchStockTransfer.getBstNumber(), invBranchStockTransfer.getBstAdBranch(), AD_BRNCH, companyCode);
                details.setBstType("BSOS-MATCHED");

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

            if (invBranchStockTransfer.getBstType().equals("OUT")) {

                details.setBstBranchTo(invBranchStockTransfer.getAdBranch().getBrBranchCode());
                details.setBstTransitLocation(invBranchStockTransfer.getInvLocation().getLocName());

            } else {

                details.setBstBranchTo(this.getAdBrNameByBrCode(invBranchStockTransfer.getBstAdBranch()));
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

    public LocalInvBranchStockTransfer getInvBstOrderByBstNumberAndBstBranch(String BST_NMBR, String BR_NM, Integer AD_BRNCH, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("InvBranchStockTransferOutEntryControllerBean getInvBstOrderByBstNumber");
        try {

            LocalInvBranchStockTransfer invBranchStockTransfer = null;

            try {

                LocalAdBranch adBranch = adBranchHome.findByBrName(BR_NM, companyCode);

                invBranchStockTransfer = invBranchStockTransferHome.findByBstNumberAndBrCode(BST_NMBR, adBranch.getBrCode(), companyCode);

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

    public ArrayList getInvBstNumberAllByBstTypeAndBstBranch(String BST_TYP, Integer AD_BRNCH, Integer BST_AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvBranchStockTransferOutEntryControllerBean getInvBstNumberAllByBstTypeAndBstBranch");
        ArrayList invBstNumberList = new ArrayList();
        try {

            Collection invBranchStockTransfer = null;

            try {
                invBranchStockTransfer = invBranchStockTransferHome.findBstByBstTypeAndAdBranchAndAdCompany(BST_TYP, AD_BRNCH, BST_AD_CMPNY);
                for (Object value : invBranchStockTransfer) {
                    LocalInvBranchStockTransfer transferOrder = (LocalInvBranchStockTransfer) value;
                    Collection transferOutCollection = invBranchStockTransferHome.findBstByBstTypeAndTransferOrderNumberAndAdBranchAndAdCompany("OUT", transferOrder.getBstNumber(), transferOrder.getBstAdBranch(), BST_AD_CMPNY);
                    Iterator j = transferOutCollection.iterator();
                    double sumOfServed = 0;
                    while (j.hasNext()) {
                        LocalInvBranchStockTransfer transferOut = (LocalInvBranchStockTransfer) j.next();
                        for (Object o : transferOut.getInvBranchStockTransferLines()) {
                            LocalInvBranchStockTransferLine transferOutLine = (LocalInvBranchStockTransferLine) o;
                            sumOfServed += transferOutLine.getBslQuantity();
                        }
                    }
                    Iterator k = transferOrder.getInvBranchStockTransferLines().iterator();
                    double totalOrder = 0;
                    while (k.hasNext()) {
                        LocalInvBranchStockTransferLine transferOrderLine = (LocalInvBranchStockTransferLine) (k.next());
                        totalOrder += transferOrderLine.getBslQuantity();
                    }
                    if (sumOfServed < totalOrder) {
                        invBstNumberList.add(transferOrder.getBstNumber());
                    }
                }
            }
            catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            return invBstNumberList;

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

        Debug.print("InvBranchStockTransferOutEntryControllerBean getInvBranchUomByIiName");
        ArrayList list = new ArrayList();
        try {

            LocalInvItem invBranchItem = null;
            LocalInvUnitOfMeasure invBranchItemUnitOfMeasure = null;

            invBranchItem = invBranchItemHome.findByIiName(II_NM, companyCode);
            invBranchItemUnitOfMeasure = invBranchItem.getInvUnitOfMeasure();

            for (Object o : invBranchUnitOfMeasureHome.findByUomAdLvClass(invBranchItemUnitOfMeasure.getUomAdLvClass(), companyCode)) {

                LocalInvUnitOfMeasure invBranchUnitOfMeasure = (LocalInvUnitOfMeasure) o;

                try {
                    LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, invBranchUnitOfMeasure.getUomName(), companyCode);
                    if (invUnitOfMeasureConversion.getUmcBaseUnit() == EJBCommon.FALSE && invUnitOfMeasureConversion.getUmcConversionFactor() == 1) {
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

    public Integer saveInvBstEntry(InvModBranchStockTransferDetails details, ArrayList bslList, boolean isDraft, Integer AD_BRNCH, Integer companyCode, String type) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException, GlobalInvTagMissingException, InvTagSerialNumberNotFoundException, GlobalInvTagExistingException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalDocumentNumberNotUniqueException, GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException, GlobalRecordInvalidException, GlobalNoRecordFoundException, GlobalExpiryDateNotFoundException, GlobalMiscInfoIsRequiredException {

        Debug.print("InvBranchStockTransferOutEntryControllerBean saveInvBstEntry");
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
            Debug.print("InvBranchStockTransferOutEntryControllerBean saveInvBstEntry A");
            // validate if branch stock transfer is already posted, void, approved or
            // pending
            if (details.getBstCode() != null) {

                if (invBranchStockTransfer.getBstApprovalStatus() != null) {

                    if (invBranchStockTransfer.getBstApprovalStatus().equals("APPROVED") || invBranchStockTransfer.getBstApprovalStatus().equals("N/A")) {

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

                invBranchExistingStockTransfer = invBranchStockTransferHome.findByBstNumberAndBrCode(details.getBstNumber(), AD_BRNCH, companyCode);
                Debug.print("InvBranchStockTransferOutEntryControllerBean saveInvBstEntry A01");
            }
            catch (FinderException ex) {

            }

            // validate if document number is unique and if document number is automatic
            // then set next
            // sequence
            Debug.print("InvBranchStockTransferOutEntryControllerBean saveInvBstEntry B");
            if (details.getBstCode() == null) {

                LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
                LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

                if (invBranchExistingStockTransfer != null) {

                    throw new GlobalDocumentNumberNotUniqueException();
                }

                try {

                    adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("INV BRANCH STOCK TRANSFER-OUT", companyCode);

                }
                catch (FinderException ex) {

                }

                try {

                    adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, companyCode);

                }
                catch (FinderException ex) {

                }

                if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (details.getBstNumber() == null || details.getBstNumber().trim().length() == 0)) {

                    while (true) {

                        if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                            try {

                                invBranchStockTransferHome.findByBstNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, companyCode);
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                            }
                            catch (FinderException ex) {

                                details.setBstNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                break;
                            }

                        } else {

                            try {

                                invBranchStockTransferHome.findByBstNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, companyCode);
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                            }
                            catch (FinderException ex) {

                                details.setBstNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                break;
                            }
                        }
                    }
                }
                Debug.print("InvBranchStockTransferOutEntryControllerBean saveInvBstEntry B01");
            } else {

                if (invBranchExistingStockTransfer != null && !invBranchExistingStockTransfer.getBstCode().equals(details.getBstCode())) {

                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (invBranchStockTransfer.getBstNumber() != details.getBstNumber() && (details.getBstNumber() == null || details.getBstNumber().trim().length() == 0)) {

                    details.setBstNumber(invBranchStockTransfer.getBstNumber());
                }
                Debug.print("InvBranchStockTransferOutEntryControllerBean saveInvBstEntry B02");
            }
            // check if transfer-order already has transfer out

            try {

                LocalInvBranchStockTransfer invExistingBranchStockTransferOut = invBranchStockTransferHome.findInByBstOutNumberAndBrCode(details.getBstTransferOrderNumber(), AD_BRNCH, companyCode);

                if (details.getBstCode() == null || !invExistingBranchStockTransferOut.getBstCode().equals(details.getBstCode())) {

                    throw new GlobalRecordAlreadyAssignedException();
                }

            }
            catch (FinderException ex) {

            }

            // get quantity transferred from transfer out to deduct in quantity in transfer
            // order

            Iterator x = bslList.iterator();

            double getBslQuantity = 0d;
            while (x.hasNext()) {
                InvModBranchStockTransferLineDetails invBranchStockTransferLineExisting = (InvModBranchStockTransferLineDetails) x.next();

                // get item quantity
                getBslQuantity = invBranchStockTransferLineExisting.getBslQuantity();
            }

            // used in checking if branch stock transfer should re-generate distribution
            // records

            boolean isRecalculate = true;

            // create branch stock transfer
            Debug.print("InvBranchStockTransferOutEntryControllerBean saveInvBstEntry C");
            if (details.getBstCode() == null) {

                invBranchStockTransfer = invBranchStockTransferHome.create(details.getBstDate(), "OUT", details.getBstNumber(), null, details.getBstTransferOrderNumber(), details.getBstDescription(), details.getBstApprovalStatus(), details.getBstPosted(), details.getBstReasonForRejection(), details.getBstCreatedBy(), details.getBstDateCreated(), details.getBstLastModifiedBy(), details.getBstDateLastModified(), details.getBstApprovedRejectedBy(), details.getBstDateApprovedRejected(), details.getBstPostedBy(), details.getBstDatePosted(), EJBCommon.FALSE, details.getBstVoid(), AD_BRNCH, companyCode);

            } else {

                if (bslList.size() != invBranchStockTransfer.getInvBranchStockTransferLines().size() || !(invBranchStockTransfer.getBstDate().equals(details.getBstDate()))) {

                    isRecalculate = true;

                } else if (bslList.size() == invBranchStockTransfer.getInvBranchStockTransferLines().size()) {

                    Iterator bslIter = invBranchStockTransfer.getInvBranchStockTransferLines().iterator();
                    Iterator bslIterList = bslList.iterator();

                    while (bslIter.hasNext()) {

                        LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) bslIter.next();
                        InvModBranchStockTransferLineDetails mdetails = (InvModBranchStockTransferLineDetails) bslIterList.next();

                        if (!invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName().equals(mdetails.getBslLocationName()) || invBranchStockTransferLine.getBslUnitCost() != mdetails.getBslUnitCost() || invBranchStockTransferLine.getBslQuantity() != mdetails.getBslQuantity() || invBranchStockTransferLine.getBslAmount() != mdetails.getBslAmount() || !invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName().equals(mdetails.getBslIiName()) || !invBranchStockTransferLine.getInvUnitOfMeasure().getUomName().equals(mdetails.getBslUomName()) || invBranchStockTransferLine.getBslMisc() != mdetails.getBslMisc()) {

                            isRecalculate = true;
                            break;
                        }

                        // get item cost
                        double COST = 0d;

                        try {

                            LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(invBranchStockTransfer.getBstDate(), invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(), invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName(), AD_BRNCH, companyCode);

                            if (invCosting.getCstRemainingQuantity() <= 0) {
                                COST = EJBCommon.roundIt(Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity()), this.getGlFcPrecisionUnit(companyCode));
                            } else {
                                COST = invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiUnitCost();
                            }

                        }
                        catch (FinderException ex) {

                        }

                        LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(), invBranchStockTransferLine.getInvUnitOfMeasure().getUomName(), companyCode);
                        LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(), invBranchStockTransferLine.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName(), companyCode);

                        COST = EJBCommon.roundIt(COST * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), this.getGlFcPrecisionUnit(companyCode));

                        double AMOUNT = 0d;

                        AMOUNT = EJBCommon.roundIt(invBranchStockTransferLine.getBslQuantity() * COST, this.getGlFcPrecisionUnit(companyCode));

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

                invBranchStockTransfer.setBstType("OUT");
                invBranchStockTransfer.setBstNumber(details.getBstNumber());
                invBranchStockTransfer.setBstDescription(details.getBstDescription());
                invBranchStockTransfer.setBstDate(details.getBstDate());
                invBranchStockTransfer.setBstTransferOrderNumber(details.getBstTransferOrderNumber());
                invBranchStockTransfer.setBstApprovalStatus(details.getBstApprovalStatus());
                invBranchStockTransfer.setBstLastModifiedBy(details.getBstLastModifiedBy());
                invBranchStockTransfer.setBstDateLastModified(details.getBstDateLastModified());
                invBranchStockTransfer.setBstReasonForRejection(null);
            }
            // get transfer order branch
            Debug.print("InvBranchStockTransferOutEntryControllerBean saveInvBstEntry D");
            LocalInvBranchStockTransfer invBranchStockTransferOrder = null;
            LocalAdBranch adBranchTo = null;

            // data completion for the uploaders & data collector
            if (details.getBstDescription().equals("UPLOAD")) {
                invBranchStockTransferOrder = invBranchStockTransferHome.findBstByBstNumberAndAdBranchAndAdCompany(details.getBstTransferOrderNumber(), AD_BRNCH, companyCode);
                invBranchStockTransfer.setBstDescription(invBranchStockTransferOrder.getBstDescription());
            }
            if (details.getBstBranchTo().equals("UPLOAD")) {
                invBranchStockTransferOrder = invBranchStockTransferHome.findBstByBstNumberAndAdBranchAndAdCompany(details.getBstTransferOrderNumber(), AD_BRNCH, companyCode);
                adBranchTo = adBranchHome.findByPrimaryKey(invBranchStockTransferOrder.getBstAdBranch());
            } else {
                adBranchTo = adBranchHome.findByBrBranchCode(details.getBstBranchTo(), companyCode);
            }
            Debug.print("1");
            adBranchTo.addInvBranchStockTransfer(invBranchStockTransfer);
            Debug.print("2");

            if (type.equalsIgnoreCase("BSOS-MATCHED")) {

                // lock corresponding transfer order
                Debug.print("3=" + details.getBstTransferOrderNumber());

                try {
                    invBranchStockTransferOrder = invBranchStockTransferHome.findByBstNumberAndBrCode(details.getBstTransferOrderNumber(), invBranchStockTransfer.getAdBranch().getBrCode(), companyCode);
                    invBranchStockTransferOrder.setBstLock(EJBCommon.FALSE);
                }
                catch (Exception e) {

                }
            }

            LocalInvLocation invLocation = invLocationHome.findByLocName(details.getBstTransitLocation(), companyCode);
            invLocation.addInvBranchStockTransfer(invBranchStockTransfer);

            double ABS_TOTAL_AMOUNT = 0d;

            if (isRecalculate) {
                Debug.print("isrecalculate");
                // remove all branch stock transfer lines

                Iterator i = invBranchStockTransfer.getInvBranchStockTransferLines().iterator();

                short LINE_NUMBER = 0;

                while (i.hasNext()) {

                    LINE_NUMBER++;

                    LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) i.next();

                    LocalInvItemLocation invItemLocation = null;

                    try {

                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName(), invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(), companyCode);

                    }
                    catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException("Line " + String.valueOf(LINE_NUMBER) + " - " + invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName());
                    }

                    double convertedQuantity = this.convertByUomAndQuantity(invBranchStockTransferLine.getInvUnitOfMeasure(), invItemLocation.getInvItem(), invBranchStockTransferLine.getBslQuantity(), companyCode);

                    invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - convertedQuantity);

                    // remove all inv tag inside branch stock transfer line
                    Collection invTags = invBranchStockTransferLine.getInvTags();

                    Iterator y = invTags.iterator();

                    while (y.hasNext()) {

                        LocalInvTag invTag = (LocalInvTag) y.next();

                        y.remove();

                        // invTag.entityRemove();
                        em.remove(invTag);
                    }
                    i.remove();

                    // invBranchStockTransferLine.entityRemove();
                    em.remove(invBranchStockTransferLine);
                }
                Debug.print("InvBranchStockTransferOutEntryControllerBean saveInvBstEntry E01");
                // remove all distribution records

                i = invBranchStockTransfer.getInvDistributionRecords().iterator();

                while (i.hasNext()) {

                    LocalInvDistributionRecord arDistributionRecord = (LocalInvDistributionRecord) i.next();

                    i.remove();

                    // arDistributionRecord.entityRemove();
                    em.remove(arDistributionRecord);
                }
                Debug.print("InvBranchStockTransferOutEntryControllerBean saveInvBstEntry E02");

                // add new branch stock transfer entry lines and distribution record

                byte DEBIT = 0;

                i = bslList.iterator();

                while (i.hasNext()) {
                    Debug.print("InvBranchStockTransferOutEntryControllerBean saveInvBstEntry E03");
                    InvModBranchStockTransferLineDetails mdetails = (InvModBranchStockTransferLineDetails) i.next();

                    LocalInvItemLocation invItemLocation = null;

                    try {
                        if (mdetails.getBslLocationName().equals("UPLOAD")) {
                            for (Object o : invBranchStockTransferOrder.getInvBranchStockTransferLines()) {
                                LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) o;
                                if (invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName().equals(mdetails.getBslIiName())) {
                                    invItemLocation = invBranchStockTransferLine.getInvItemLocation();
                                    mdetails.setBslLocationName(invItemLocation.getInvLocation().getLocName());
                                    mdetails.setBslUomName(invBranchStockTransferLine.getInvUnitOfMeasure().getUomName());
                                    mdetails.setBslUnitCost(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiUnitCost());
                                    mdetails.setBslAmount(mdetails.getBslQuantity() * mdetails.getBslUnitCost());
                                }
                            }
                        } else {
                            invItemLocation = invItemLocationHome.findByLocNameAndIiName(mdetails.getBslLocationName(), mdetails.getBslIiName(), companyCode);
                        }
                        Debug.print("InvBranchStockTransferOutEntryControllerBean saveInvBstEntry E04");
                    }
                    catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException("Line " + mdetails.getBslLineNumber() + " - " + mdetails.getBslLocationName());
                    }

                    LocalInvItemLocation invItemTransitLocation = null;

                    try {

                        invItemTransitLocation = invItemLocationHome.findByLocNameAndIiName(details.getBstTransitLocation(), mdetails.getBslIiName(), companyCode);
                        Debug.print("InvBranchStockTransferOutEntryControllerBean saveInvBstEntry E05");
                    }
                    catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException("Transit Location " + details.getBstTransitLocation());
                    }

                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        // start date validation
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(invBranchStockTransfer.getBstDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                        Debug.print("InvBranchStockTransferOutEntryControllerBean saveInvBstEntry E06");
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }

                    LocalInvBranchStockTransferLine invBranchStockTransferLine = this.addInvBslEntry(mdetails, invBranchStockTransfer, companyCode);

                    // add distribution records

                    double COST = 0d;

                    try {

                        LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(invBranchStockTransfer.getBstDate(), invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(), invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName(), AD_BRNCH, companyCode);
                        Debug.print("InvBranchStockTransferOutEntryControllerBean saveInvBstEntry E07");

                        if (invCosting.getCstRemainingQuantity() <= 0) {
                            COST = EJBCommon.roundIt(Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity()), this.getGlFcPrecisionUnit(companyCode));

                        } else {

                            COST = invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiUnitCost();
                        }

                    }
                    catch (FinderException ex) {

                        COST = invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiUnitCost();
                    }

                    LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(), invBranchStockTransferLine.getInvUnitOfMeasure().getUomName(), companyCode);
                    LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(), invBranchStockTransferLine.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName(), companyCode);

                    COST = EJBCommon.roundIt(COST * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), this.getGlFcPrecisionUnit(companyCode));

                    double AMOUNT = 0d;

                    AMOUNT = EJBCommon.roundIt(invBranchStockTransferLine.getBslQuantity() * COST, this.getGlFcPrecisionUnit(companyCode));

                    // check branch mapping
                    Debug.print("InvBranchStockTransferOutEntryControllerBean saveInvBstEntry E08");
                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {

                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(invItemLocation.getIlCode(), AD_BRNCH, companyCode);

                    }
                    catch (FinderException ex) {

                    }

                    LocalGlChartOfAccount glChartOfAccount = null;

                    if (adBranchItemLocation == null) {

                        glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(invItemLocation.getIlGlCoaInventoryAccount());

                    } else {

                        glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchItemLocation.getBilCoaGlInventoryAccount());
                    }

                    // add dr for inventory
                    Debug.print("InvBranchStockTransferOutEntryControllerBean saveInvBstEntry E09");
                    this.addInvDrEntry(invBranchStockTransfer.getInvDrNextLine(), "INVENTORY", EJBCommon.FALSE, Math.abs(AMOUNT), glChartOfAccount.getCoaCode(), invBranchStockTransfer, AD_BRNCH, companyCode);

                    Debug.print("InvBranchStockTransferOutEntryControllerBean saveInvBstEntry E10");
                    // check branch mapping for transit location

                    LocalAdBranchItemLocation adBranchItemTransitLocation = null;
                    Debug.print("AD_BRNCH=" + AD_BRNCH);
                    try {

                        adBranchItemTransitLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(invItemTransitLocation.getIlCode(), adBranchTo.getBrCode(), companyCode);

                    }
                    catch (FinderException ex) {

                    }

                    LocalGlChartOfAccount glChartOfAccountTransit = null;

                    if (adBranchItemTransitLocation == null) {

                        glChartOfAccountTransit = glChartOfAccountHome.findByPrimaryKey(invItemTransitLocation.getIlGlCoaInventoryAccount());

                    } else {

                        glChartOfAccountTransit = glChartOfAccountHome.findByPrimaryKey(adBranchItemTransitLocation.getBilCoaGlWipAccount());
                    }

                    // add dr for inventory transit location
                    Debug.print("InvBranchStockTransferOutEntryControllerBean saveInvBstEntry E11");
                    this.addInvDrEntry(invBranchStockTransfer.getInvDrNextLine(), "IN TRANSIT", EJBCommon.TRUE, Math.abs(AMOUNT), glChartOfAccountTransit.getCoaCode(), invBranchStockTransfer, AD_BRNCH, companyCode);
                    Debug.print("InvBranchStockTransferOutEntryControllerBean saveInvBstEntry E12");

                    ABS_TOTAL_AMOUNT += Math.abs(AMOUNT);

                    // set ilCommittedQuantity

                    double convertedQuantity = this.convertByUomAndQuantity(invBranchStockTransferLine.getInvUnitOfMeasure(), invItemLocation.getInvItem(), invBranchStockTransferLine.getBslQuantity(), companyCode);
                    Debug.print("InvBranchStockTransferOutEntryControllerBean saveInvBstEntry E13");
                    invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() + convertedQuantity);

                    // check branch to mapping

                    LocalAdBranchItemLocation adBranchToItemLocation = null;

                    try {

                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(invItemTransitLocation.getIlCode(), adBranchTo.getBrCode(), companyCode);
                        Debug.print("InvBranchStockTransferOutEntryControllerBean saveInvBstEntry E14");

                    }
                    catch (FinderException ex) {

                        throw new GlobalNoRecordFoundException(invItemTransitLocation.getInvItem().getIiName() + " - " + invItemTransitLocation.getInvLocation().getLocName());
                    }

                    // validate inventoriable and non-inventoriable items

                    if (mdetails.getTraceMisc() == 1) {
                        if (invItemLocation.getInvItem().getIiNonInventoriable() == 0 && mdetails.getBslTagList() != null && mdetails.getBslTagList().size() < mdetails.getBslQuantity()) {

                            throw new GlobalInvTagMissingException(invItemLocation.getInvItem().getIiName());
                        }
                        try {
                            Debug.print("aabot?");
                            // Iterator t = apPurchaseOrderLine.getInvTag().iterator();
                            Iterator t = mdetails.getBslTagList().iterator();

                            LocalInvTag invTag = null;
                            Debug.print("umabot?");
                            while (t.hasNext()) {
                                InvModTagListDetails tgLstDetails = (InvModTagListDetails) t.next();
                                Debug.print(tgLstDetails.getTgCustodian() + "<== custodian");
                                Debug.print(tgLstDetails.getTgSpecs() + "<== specs");
                                Debug.print(tgLstDetails.getTgPropertyCode() + "<== propertyCode");
                                Debug.print(tgLstDetails.getTgExpiryDate() + "<== expiryDate");
                                Debug.print(tgLstDetails.getTgSerialNumber() + "<== serial number");
                                if (invItemLocation.getInvItem().getIiNonInventoriable() == 0 && tgLstDetails.getTgCustodian().equals("") && tgLstDetails.getTgSpecs().equals("") && tgLstDetails.getTgPropertyCode().equals("") && tgLstDetails.getTgExpiryDate() == null && tgLstDetails.getTgSerialNumber().equals("")) {
                                    throw new GlobalInvTagMissingException(invItemLocation.getInvItem().getIiDescription());
                                }
                                if (tgLstDetails.getTgCode() == null) {
                                    Debug.print("ngcreate ba?");

                                    try {
                                        Collection invTg = invTagHome.findAllInByTgSerialNumberAndAdBranchAndAdCompany(tgLstDetails.getTgSerialNumber(), AD_BRNCH, companyCode);
                                        if (invTg.isEmpty()) {
                                            throw new InvTagSerialNumberNotFoundException(tgLstDetails.getTgSerialNumber());
                                        } else {
                                            try {
                                                invTg = invTagHome.findAllOutByTgSerialNumberAndAdBranchAndAdCompany(tgLstDetails.getTgSerialNumber(), AD_BRNCH, companyCode);
                                                if (!invTg.isEmpty()) {
                                                    throw new InvTagSerialNumberNotFoundException(tgLstDetails.getTgSerialNumber());
                                                }
                                            }
                                            catch (FinderException ex) {
                                            }
                                        }
                                    }
                                    catch (FinderException ex) {
                                    }

                                    invTag = invTagHome.create(tgLstDetails.getTgPropertyCode(), tgLstDetails.getTgSerialNumber(), null, tgLstDetails.getTgExpiryDate(), tgLstDetails.getTgSpecs(), companyCode, tgLstDetails.getTgTransactionDate(), "OUT");

                                    invTag.setInvBranchStockTransferLine(invBranchStockTransferLine);
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
                        catch (InvTagSerialNumberNotFoundException ex) {
                            Debug.print("---------------------------------->" + ex.getMessage());
                            throw new InvTagSerialNumberNotFoundException(ex.getMessage());
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                            if (invItemLocation.getInvItem().getIiNonInventoriable() == 0) {
                                throw new GlobalInvTagMissingException(mdetails.getBslIiName());
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

                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(mdetails.getBslLocationName(), mdetails.getBslIiName(), companyCode);

                    }
                    catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException("Line " + mdetails.getBslLineNumber() + " - " + mdetails.getBslLocationName());
                    }

                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        // start date validation

                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(invBranchStockTransfer.getBstDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);

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
                    LocalInvBranchStockTransferLine invBranchStockTransferLine = invBranchStockTransferLineHome.findByPrimaryKey(mdetails.getBslCode());
                    // remove all inv tag inside PO line
                    Collection invTags = invBranchStockTransferLine.getInvTags();

                    Iterator y = invTags.iterator();

                    while (y.hasNext()) {

                        LocalInvTag invTag = (LocalInvTag) y.next();

                        y.remove();

                        // invTag.entityRemove();
                        em.remove(invTag);
                    }

                    Debug.print("mgcecreate ng bago?");
                    Debug.print(mdetails + "<== mPlDetails");
                    Debug.print(mdetails.getBslTagList() + "<== mdetails.getAlTagLis");
                    // validate inventoriable and non-inventoriable items

                    if (mdetails.getTraceMisc() == 1) {
                        if (invItemLocation.getInvItem().getIiNonInventoriable() == 0 && mdetails.getBslTagList() != null && mdetails.getBslTagList().size() < mdetails.getBslQuantity()) {

                            throw new GlobalInvTagMissingException(invItemLocation.getInvItem().getIiName());
                        }
                        try {
                            for (Object o : mdetails.getBslTagList()) {
                                InvModTagListDetails tgLstDetails = (InvModTagListDetails) o;
                                Debug.print(tgLstDetails.getTgCustodian() + "<== custodian");
                                Debug.print(tgLstDetails.getTgSpecs() + "<== specs");
                                Debug.print(tgLstDetails.getTgPropertyCode() + "<== propertyCode");
                                Debug.print(tgLstDetails.getTgExpiryDate() + "<== expiryDate");
                                Debug.print("ngcreate ng bago?");
                                if (invItemLocation.getInvItem().getIiNonInventoriable() == 0 && tgLstDetails.getTgCustodian().equals("") && tgLstDetails.getTgSpecs().equals("") && tgLstDetails.getTgPropertyCode().equals("") && tgLstDetails.getTgExpiryDate() == null && tgLstDetails.getTgSerialNumber().equals("")) {
                                    throw new GlobalInvTagMissingException(invItemLocation.getInvItem().getIiDescription());
                                }
                                LocalInvTag invTag = invTagHome.create(tgLstDetails.getTgPropertyCode(), tgLstDetails.getTgSerialNumber(), null, tgLstDetails.getTgExpiryDate(), tgLstDetails.getTgSpecs(), companyCode, tgLstDetails.getTgTransactionDate(), "OUT");

                                invTag.setInvBranchStockTransferLine(invBranchStockTransferLine);
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
                        catch (Exception ex) {
                            if (invItemLocation.getInvItem().getIiNonInventoriable() == 0) {
                                throw new GlobalInvTagMissingException(mdetails.getBslIiName());
                            }
                        }
                    }
                }
            }

            // insufficient stock checking
            if (adPreference.getPrfArCheckInsufficientStock() == EJBCommon.TRUE) {
                boolean hasInsufficientItems = false;
                StringBuilder insufficientItems = new StringBuilder();

                Collection invBranchStockTransferLines = invBranchStockTransfer.getInvBranchStockTransferLines();

                for (Object branchStockTransferLine : invBranchStockTransferLines) {

                    LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) branchStockTransferLine;

                    LocalInvCosting invCosting = null;

                    double ILI_QTY = this.convertByUomAndQuantity(invBranchStockTransferLine.getInvUnitOfMeasure(), invBranchStockTransferLine.getInvItemLocation().getInvItem(), Math.abs(invBranchStockTransferLine.getBslQuantity()), companyCode);

                    try {

                        invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(invBranchStockTransfer.getBstDate(), invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(), invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName(), AD_BRNCH, companyCode);

                    }
                    catch (FinderException ex) {

                    }

                    double LOWEST_QTY = this.convertByUomAndQuantity(invBranchStockTransferLine.getInvUnitOfMeasure(), invBranchStockTransferLine.getInvItemLocation().getInvItem(), 1, companyCode);

                    if (invCosting == null || invCosting.getCstRemainingQuantity() == 0 || invCosting.getCstRemainingQuantity() - ILI_QTY <= -LOWEST_QTY) {

                        hasInsufficientItems = true;

                        insufficientItems.append(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName()).append(", ");
                    }
                }
                if (hasInsufficientItems) {

                    throw new GlobalRecordInvalidException(insufficientItems.substring(0, insufficientItems.lastIndexOf(",")));
                }
            }

            // generate approval status

            String INV_APPRVL_STATUS = null;

            if (!isDraft) {

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);

                // check if inv stock transfer approval is enabled

                if (adApproval.getAprEnableInvBranchStockTransfer() == EJBCommon.FALSE) {

                    INV_APPRVL_STATUS = "N/A";

                } else {

                    // check if invoice is self approved

                    LocalAdAmountLimit adAmountLimit = null;

                    try {

                        adAmountLimit = adAmountLimitHome.findByAdcTypeAndAuTypeAndUsrName("INV BRANCH STOCK TRANSFER", "REQUESTER", details.getBstLastModifiedBy(), companyCode);

                    }
                    catch (FinderException ex) {

                        throw new GlobalNoApprovalRequesterFoundException();
                    }

                    if (ABS_TOTAL_AMOUNT <= adAmountLimit.getCalAmountLimit()) {

                        INV_APPRVL_STATUS = "N/A";

                    } else {

                        // for approval, create approval queue

                        Collection adAmountLimits = adAmountLimitHome.findByAdcTypeAndGreaterThanCalAmountLimit("INV BRANCH STOCK TRANSFER", adAmountLimit.getCalAmountLimit(), companyCode);

                        if (adAmountLimits.isEmpty()) {

                            Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adAmountLimit.getCalCode(), companyCode);

                            if (adApprovalUsers.isEmpty()) {

                                throw new GlobalNoApprovalApproverFoundException();
                            }

                            for (Object approvalUser : adApprovalUsers) {

                                LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(AD_BRNCH, companyCode, adApprovalQueueHome, invBranchStockTransfer, adAmountLimit, adApprovalUser);

                                adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                            }

                        } else {

                            boolean isApprovalUsersFound = false;

                            Iterator i = adAmountLimits.iterator();

                            while (i.hasNext()) {

                                LocalAdAmountLimit adNextAmountLimit = (LocalAdAmountLimit) i.next();

                                if (ABS_TOTAL_AMOUNT <= adNextAmountLimit.getCalAmountLimit()) {

                                    Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adAmountLimit.getCalCode(), companyCode);

                                    for (Object approvalUser : adApprovalUsers) {

                                        isApprovalUsersFound = true;

                                        LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                        LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(AD_BRNCH, companyCode, adApprovalQueueHome, invBranchStockTransfer, adAmountLimit, adApprovalUser);

                                        adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                                    }

                                    break;

                                } else if (!i.hasNext()) {

                                    Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adNextAmountLimit.getCalCode(), companyCode);

                                    for (Object approvalUser : adApprovalUsers) {

                                        isApprovalUsersFound = true;

                                        LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                        LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(AD_BRNCH, companyCode, adApprovalQueueHome, invBranchStockTransfer, adNextAmountLimit, adApprovalUser);

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

                this.executeInvBstPost(invBranchStockTransfer.getBstCode(), invBranchStockTransfer.getBstLastModifiedBy(), AD_BRNCH, companyCode);
            }

            // set stock transfer approval status

            invBranchStockTransfer.setBstApprovalStatus(INV_APPRVL_STATUS);

            return invBranchStockTransfer.getBstCode();

        }
        catch (GlobalRecordAlreadyDeletedException | GlobalMiscInfoIsRequiredException |
               GlobalExpiryDateNotFoundException | GlobalNoRecordFoundException | GlobalRecordInvalidException |
               AdPRFCoaGlVarianceAccountNotFoundException | GlobalBranchAccountNumberInvalidException |
               GlobalDocumentNumberNotUniqueException | GlobalInventoryDateException |
               GlobalJournalNotBalanceException | GlJREffectiveDatePeriodClosedException |
               GlJREffectiveDateNoPeriodExistException | InvTagSerialNumberNotFoundException |
               GlobalInvTagMissingException | GlobalInvItemLocationNotFoundException |
               GlobalNoApprovalApproverFoundException | GlobalNoApprovalRequesterFoundException |
               GlobalTransactionAlreadyPostedException | GlobalTransactionAlreadyPendingException |
               GlobalTransactionAlreadyApprovedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public double getInvIiShippingCostByIiUnitCostAndAdBranch(double II_UNT_CST, Integer AD_BRNCH, Integer companyCode) {

        Debug.print("InvBranchStockTransferOutEntryControllerBean getInvIiMarkupUnitCostByIiUnitCostAndAdBranch");
        try {

            LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(AD_BRNCH);

            double BSL_SHPPNG_CST = 0d;

            if (adBranch.getBrApplyShipping() == EJBCommon.TRUE) {

                BSL_SHPPNG_CST = EJBCommon.roundIt(II_UNT_CST * (adBranch.getBrPercentMarkup() / 100), this.getGlFcPrecisionUnit(companyCode));
            }

            return BSL_SHPPNG_CST;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods
    private String getAdBrNameByBrCode(Integer BR_CODE) {

        Debug.print("InvBranchStockTransferOutEntryControllerBean getAdBrNameByBrCode");
        try {

            return adBranchHome.findByPrimaryKey(BR_CODE).getBrBranchCode();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteInvBstEntry(Integer BST_CODE, String AD_USR, Integer companyCode) throws GlobalRecordAlreadyDeletedException {

        Debug.print("InvBranchStockTransferOutEntryControllerBean deleteInvBstEntry");
        try {

            LocalInvBranchStockTransfer invBranchStockTransfer = invBranchStockTransferHome.findByPrimaryKey(BST_CODE);

            for (Object o : invBranchStockTransfer.getInvBranchStockTransferLines()) {

                LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) o;

                LocalInvItemLocation invItemLocation = invItemLocationHome.findByLocNameAndIiName(invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName(), invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(), companyCode);

                double convertedQuantity = this.convertByUomAndQuantity(invBranchStockTransferLine.getInvUnitOfMeasure(), invItemLocation.getInvItem(), Math.abs(invBranchStockTransferLine.getBslQuantity()), companyCode);

                invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - convertedQuantity);
            }

            if (invBranchStockTransfer.getBstApprovalStatus() != null && invBranchStockTransfer.getBstApprovalStatus().equals("PENDING")) {

                Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode("INV BRANCH STOCK TRANSFER", invBranchStockTransfer.getBstCode(), companyCode);

                for (Object approvalQueue : adApprovalQueues) {

                    LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) approvalQueue;

                    // adApprovalQueue.entityRemove();
                    em.remove(adApprovalQueue);
                }
            }

            adDeleteAuditTrailHome.create("INV BRANCH STOCK TRANSFER OUT", invBranchStockTransfer.getBstDate(), invBranchStockTransfer.getBstNumber(), invBranchStockTransfer.getBstNumber(), 0d, AD_USR, new Date(), companyCode);

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

    public LocalInvBranchStockTransfer getInvBstOutByBstNumberAndBstBranch(String BST_NMBR, Integer BR_CODE, Integer AD_BRNCH, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("InvBranchStockTransferOutEntryControllerBean getInvBstOutByBstNumber");
        try {

            LocalInvBranchStockTransfer invBranchStockTransfer = null;

            try {
                Debug.print("BST_NMBR-" + BST_NMBR);
                Debug.print("BR_CODE-" + BR_CODE);
                // LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(BR_CODE);

                invBranchStockTransfer = invBranchStockTransferHome.findByBstNumberAndBrCode(BST_NMBR, BR_CODE, companyCode);

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

    public short getGlFcPrecisionUnit(Integer companyCode) {

        Debug.print("InvBranchStockTransferOutEntryControllerBean getGlFcPrecisionUnit");
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

        Debug.print("InvBranchStockTransferOutEntryControllerBean getInvGpQuantityPrecisionUnit");
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

        Debug.print("InvBranchStockTransferOutEntryControllerBean getInvGpInventoryLineNumber");
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

        Debug.print("InvBranchStockTransferOutEntryControllerBean getAdApprovalNotifiedUsersByBstCode");
        ArrayList list = new ArrayList();
        try {

            LocalInvBranchStockTransfer invBranchStockTransfer = invBranchStockTransferHome.findByPrimaryKey(BST_CODE);

            if (invBranchStockTransfer.getBstPosted() == EJBCommon.TRUE) {

                list.add("DOCUMENT POSTED");
                return list;
            }

            Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode("INV BRANCH STOCK TRANSFER", BST_CODE, companyCode);

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

    public double getInvIiUnitCostByIiNameAndLocFromAndUomNameAndDate(String II_NM, String LOC_FRM, String UOM_NM, Date ST_DT, Integer AD_BRNCH, Integer companyCode) {

        Debug.print("InvBranchStockTransferOutEntryControllerBean getInvIiUnitCostByIiNameAndUomName");

        LocalInvItemHome invItemHome = null;
        LocalInvItemLocationHome invItemLocationHome = null;
        LocalInvCostingHome invCostingHome = null;
        LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome = null;

        // Initialize EJB Home

        try {

            invItemHome = (LocalInvItemHome) EJBHomeFactory.lookUpLocalHome(LocalInvItemHome.JNDI_NAME, LocalInvItemHome.class);
            invItemLocationHome = (LocalInvItemLocationHome) EJBHomeFactory.lookUpLocalHome(LocalInvItemLocationHome.JNDI_NAME, LocalInvItemLocationHome.class);
            invCostingHome = (LocalInvCostingHome) EJBHomeFactory.lookUpLocalHome(LocalInvCostingHome.JNDI_NAME, LocalInvCostingHome.class);
            invUnitOfMeasureConversionHome = (LocalInvUnitOfMeasureConversionHome) EJBHomeFactory.lookUpLocalHome(LocalInvUnitOfMeasureConversionHome.JNDI_NAME, LocalInvUnitOfMeasureConversionHome.class);

        }
        catch (NamingException ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NM, companyCode);

            double COST = invItem.getIiUnitCost();

            LocalInvItemLocation invItemLocation = null;

            try {

                invItemLocation = invItemLocationHome.findByLocNameAndIiName(LOC_FRM, II_NM, companyCode);

                LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(ST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);

                switch (invItemLocation.getInvItem().getIiCostMethod()) {
                    case "Average":
                        COST = invCosting.getCstRemainingQuantity() == 0 ? COST : EJBCommon.roundIt(Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity()), this.getGlFcPrecisionUnit(companyCode));

                        if (COST <= 0) {
                            COST = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                        }

                        break;
                    case "FIFO":
                        COST = invCosting.getCstRemainingQuantity() == 0 ? COST : this.getInvFifoCost(ST_DT, invItemLocation.getIlCode(), invCosting.getCstAdjustQuantity(), invCosting.getCstAdjustCost(), false, AD_BRNCH, companyCode);
                        break;
                    case "Standard":
                        COST = invItemLocation.getInvItem().getIiUnitCost();
                        break;
                }

            }
            catch (FinderException ex) {

            }

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, UOM_NM, companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, invItem.getInvUnitOfMeasure().getUomName(), companyCode);

            return EJBCommon.roundIt(COST * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), this.getGlFcPrecisionUnit(companyCode));

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods

    private LocalInvBranchStockTransferLine addInvBslEntry(InvModBranchStockTransferLineDetails mdetails, LocalInvBranchStockTransfer invBranchStockTransfer, Integer companyCode) throws GlobalMiscInfoIsRequiredException {

        Debug.print("InvBranchStockTransferOutEntryControllerBean addInvBslEntry");
        try {

            LocalInvBranchStockTransferLine invBranchStockTransferLine = invBranchStockTransferLineHome.create(mdetails.getBslQuantity(), mdetails.getBslQuantityReceived(), mdetails.getBslUnitCost(), mdetails.getBslAmount(), companyCode);

            // invBranchStockTransfer.addInvBranchStockTransferLine(invBranchStockTransferLine);
            invBranchStockTransferLine.setInvBranchStockTransfer(invBranchStockTransfer);
            LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(mdetails.getBslUomName(), companyCode);

            // invUnitOfMeasure.addInvBranchStockTransferLine(invBranchStockTransferLine);
            invBranchStockTransferLine.setInvUnitOfMeasure(invUnitOfMeasure);

            LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(mdetails.getBslIiName(), mdetails.getBslLocationName(), companyCode);

            // invItemLocation.addInvBranchStockTransferLine(invBranchStockTransferLine);
            invBranchStockTransferLine.setInvItemLocation(invItemLocation);

            if (invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiTraceMisc() == 1) {
                this.createInvTagList(invBranchStockTransferLine, mdetails.getBslTagList(), companyCode);
            }
            // validate misc

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

    private double convertByUomAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, double ADJST_QTY, Integer companyCode) {

        Debug.print("InvBranchStockTransferOutEntryControllerBean convertByUomFromAndUomToAndQuantity");
        try {
            Debug.print("InvBranchStockTransferOutEntryControllerBean convertByUomFromAndUomToAndQuantity A");
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), companyCode);
            Debug.print("InvBranchStockTransferOutEntryControllerBean convertByUomFromAndUomToAndQuantity B");
            return EJBCommon.roundIt(ADJST_QTY * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), adPreference.getPrfInvQuantityPrecisionUnit());

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void addInvDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE, LocalInvBranchStockTransfer invBranchStockTransfer, Integer AD_BRNCH, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("InvBranchStockTransferOutEntryControllerBean addInvDrEntry");
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

            LocalInvDistributionRecord invDistributionRecord = invDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, EJBCommon.FALSE, companyCode);

            // invBranchStockTransfer.addInvDistributionRecord(invDistributionRecord);
            invDistributionRecord.setInvBranchStockTransfer(invBranchStockTransfer);
            // glChartOfAccount.addInvDistributionRecord(invDistributionRecord);
            invDistributionRecord.setInvChartOfAccount(glChartOfAccount);

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

    private void executeInvBstPost(Integer BST_CODE, String USR_NM, Integer AD_BRNCH, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException, GlobalRecordInvalidException, GlobalExpiryDateNotFoundException {

        Debug.print("InvBranchStockTransferOutEntryControllerBean executeInvBstPost");
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

                LocalInvItemLocation invItemLocationFrom = invItemLocationHome.findByLocNameAndIiName(locName, invItemName, companyCode);

                Integer branchFrom = invBranchStockTransferLine.getInvBranchStockTransfer().getBstAdBranch();

                double BST_QTY = this.convertByUomAndQuantity(invBranchStockTransferLine.getInvUnitOfMeasure(), invBranchStockTransferLine.getInvItemLocation().getInvItem(), Math.abs(invBranchStockTransferLine.getBslQuantity()), companyCode);

                LocalInvCosting invCostingFrom = null;

                try {

                    invCostingFrom = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(invBranchStockTransfer.getBstDate(), invItemName, locName, branchFrom, companyCode);

                }
                catch (FinderException ex) {

                }

                double COST = invBranchStockTransferLine.getBslUnitCost();

                if (invCostingFrom == null) {

                    this.postToInv(invBranchStockTransferLine, invItemLocationFrom, invBranchStockTransfer.getBstDate(), -BST_QTY, -COST * BST_QTY, -BST_QTY, -COST * BST_QTY, 0d, null, branchFrom, companyCode);

                } else {

                    this.postToInv(invBranchStockTransferLine, invItemLocationFrom, invBranchStockTransfer.getBstDate(), -BST_QTY, -COST * BST_QTY, invCostingFrom.getCstRemainingQuantity() - BST_QTY, invCostingFrom.getCstRemainingValue() - (COST * BST_QTY), 0d, null, branchFrom, companyCode);
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

            LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), invBranchStockTransfer.getBstDate(), companyCode);

            if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P') {

                throw new GlJREffectiveDatePeriodClosedException();
            }

            // check if debit and credit is balance

            LocalGlJournalLine glOffsetJournalLine = null;

            Collection invDistributionRecords = invDistributionRecordHome.findImportableDrByBstCode(invBranchStockTransfer.getBstCode(), companyCode);

            Iterator x = invDistributionRecords.iterator();

            double TOTAL_DEBIT = 0d;
            double TOTAL_CREDIT = 0d;

            while (x.hasNext()) {

                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) x.next();

                double DR_AMNT = 0d;

                DR_AMNT = invDistributionRecord.getDrAmount();

                if (invDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    Debug.print("DR_CODE=" + invDistributionRecord.getDrCode());
                    Debug.print("DR_DEBIT=" + invDistributionRecord.getDrDebit());
                    TOTAL_DEBIT += DR_AMNT;
                    Debug.print("1 TOTAL_DEBIT=" + TOTAL_DEBIT);
                } else {

                    TOTAL_CREDIT += DR_AMNT;
                }
            }
            Debug.print("TOTAL_DEBIT=" + TOTAL_DEBIT);
            Debug.print("TOTAL_CREDIT=" + TOTAL_CREDIT);
            TOTAL_DEBIT = EJBCommon.roundIt(TOTAL_DEBIT, adCompany.getGlFunctionalCurrency().getFcPrecision());
            TOTAL_CREDIT = EJBCommon.roundIt(TOTAL_CREDIT, adCompany.getGlFunctionalCurrency().getFcPrecision());

            if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.TRUE && TOTAL_DEBIT != TOTAL_CREDIT) {

                LocalGlSuspenseAccount glSuspenseAccount = null;

                try {

                    glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("INVENTORY", "BRANCH STOCK TRANSFERS", companyCode);

                }
                catch (FinderException ex) {

                    throw new GlobalJournalNotBalanceException();
                }

                if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                    glOffsetJournalLine = glJournalLineHome.create((short) (invDistributionRecords.size() + 1), EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);

                } else {

                    glOffsetJournalLine = glJournalLineHome.create((short) (invDistributionRecords.size() + 1), EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
                }

                LocalGlChartOfAccount glChartOfAccount = glSuspenseAccount.getGlChartOfAccount();
                // glChartOfAccount.addGlJournalLine(glOffsetJournalLine);
                glOffsetJournalLine.setGlChartOfAccount(glChartOfAccount);

            } else if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE && TOTAL_DEBIT != TOTAL_CREDIT) {
                Debug.print("TOTAL_DEBIT=" + TOTAL_DEBIT);
                Debug.print("TOTAL_CREDIT=" + TOTAL_CREDIT);
                throw new GlobalJournalNotBalanceException();
            }

            // create journal batch if necessary

            LocalGlJournalBatch glJournalBatch = null;
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");

            try {

                glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " BRANCH STOCK TRANSFERS", AD_BRNCH, companyCode);

            }
            catch (FinderException ex) {

            }

            if (glJournalBatch == null) {

                glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " BRANCH STOCK TRANSFERS", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, AD_BRNCH, companyCode);
            }

            // create journal entry

            LocalGlJournal glJournal = glJournalHome.create(invBranchStockTransfer.getBstNumber(), invBranchStockTransfer.getBstDescription(), invBranchStockTransfer.getBstDate(), 0.0d, null, invBranchStockTransfer.getBstNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM, EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, EJBCommon.FALSE, null, AD_BRNCH, companyCode);
            LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("INVENTORY", companyCode);
            glJournal.setGlJournalSource(glJournalSource);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
            glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("BRANCH STOCK TRANSFERS", companyCode);
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

                LocalGlJournalLine glJournalLine = glJournalLineHome.create(invDistributionRecord.getDrLine(), invDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

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
            Debug.print("InvBranchStockTransferOutEntryControllerBean executeInvBstPost G");

            // post journal to gl
            Collection glJournalLines = glJournalLineHome.findByJrCode(glJournal.getJrCode(), companyCode);
            i = glJournalLines.iterator();

            while (i.hasNext()) {

                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) i.next();

                // post current to current acv

                this.postToGl(glAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), true, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                // post to subsequent acvs (propagate)

                Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), glAccountingCalendarValue.getAcvPeriodNumber(), companyCode);

                for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {

                    LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;

                    this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                }

                // post to subsequent years if necessary

                Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode);

                if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                    adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                    LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(adCompany.getCmpRetainedEarnings(), AD_BRNCH, companyCode);

                    for (Object subsequentSetOfBook : glSubsequentSetOfBooks) {

                        LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) subsequentSetOfBook;

                        String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                        // post to subsequent acvs of subsequent set of book(propagate)

                        Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                        for (Object accountingCalendarValue : glAccountingCalendarValues) {

                            LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                            if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY") || ACCOUNT_TYPE.equals("OWNERS EQUITY")) {

                                this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                            } else {
                                // revenue & expense

                                this.postToGl(glSubsequentAccountingCalendarValue, glRetainedEarningsAccount, false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                            }
                        }

                        if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) {
                            break;
                        }
                    }
                }
            }
            Debug.print("InvBranchStockTransferOutEntryControllerBean executeInvBstPost H");
        }
        catch (GlJREffectiveDateNoPeriodExistException | GlobalExpiryDateNotFoundException |
               AdPRFCoaGlVarianceAccountNotFoundException | GlobalBranchAccountNumberInvalidException |
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

    private double getInvFifoCost(Date CST_DT, Integer IL_CODE, double CST_QTY, double CST_COST, boolean isAdjustFifo, Integer AD_BRNCH, Integer companyCode) {

        try {

            Collection invFifoCostings = invCostingHome.findFifoRemainingQuantityByLessThanOrEqualCstDateAndIlCodeAndBrCode(CST_DT, IL_CODE, AD_BRNCH, companyCode);

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

                            if (invFifoCosting.getApPurchaseOrderLine() != null || invFifoCosting.getApVoucherLineItem() != null) {
                                cost = invFifoCosting.getCstItemCost() / invFifoCosting.getCstQuantityReceived();
                            } else if (invFifoCosting.getArInvoiceLineItem() != null) {
                                cost = invFifoCosting.getCstCostOfSales() / invFifoCosting.getCstQuantitySold();
                            } else {
                                cost = invFifoCosting.getCstAdjustCost() / invFifoCosting.getCstAdjustQuantity();
                            }

                            if (neededQty <= invFifoCosting.getCstRemainingLifoQuantity()) {

                                invFifoCosting.setCstRemainingLifoQuantity(invFifoCosting.getCstRemainingLifoQuantity() - neededQty);
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

                    if (invFifoCosting.getApPurchaseOrderLine() != null || invFifoCosting.getApVoucherLineItem() != null) {
                        return EJBCommon.roundIt(invFifoCosting.getCstItemCost() / invFifoCosting.getCstQuantityReceived(), this.getGlFcPrecisionUnit(companyCode));
                    } else if (invFifoCosting.getArInvoiceLineItem() != null) {
                        return EJBCommon.roundIt(invFifoCosting.getCstCostOfSales() / invFifoCosting.getCstQuantitySold(), this.getGlFcPrecisionUnit(companyCode));
                    } else {
                        return EJBCommon.roundIt(invFifoCosting.getCstAdjustCost() / invFifoCosting.getCstAdjustQuantity(), this.getGlFcPrecisionUnit(companyCode));
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

    private void postToInv(LocalInvBranchStockTransferLine invBranchStockTransferLine, LocalInvItemLocation invItemLocation, Date CST_DT, double CST_ST_QTY, double CST_ST_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, double CST_VRNC_VL, String USR_NM, Integer AD_BRNCH, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException {

        Debug.print("InvBranchStockTransferOutEntryControllerBean post");
        try {
            Debug.print("InvBranchStockTransferOutEntryControllerBean post A");
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

                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;
                Debug.print("InvBranchStockTransferOutEntryControllerBean post A01");

            }
            catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            // void subsequent cost variance adjustments
            Collection invAdjustmentLines = invAdjustmentLineHome.findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(CST_DT, invItemLocation.getIlCode(), AD_BRNCH, companyCode);
            Debug.print("InvBranchStockTransferOutEntryControllerBean post A02");
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
                LocalInvCosting prevCst = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndRemainingQuantityNotEqualToZeroAndIlCode(CST_DT, invItemLocation.getIlCode(), AD_BRNCH, companyCode);
                Debug.print("InvBranchStockTransferOutEntryControllerBean post A03");
                Debug.print("prevCst.getCstCode(): " + prevCst.getCstCode());
                Debug.print("CST_DT: " + CST_DT);
                Debug.print("getCstDate: " + prevCst.getCstDate());
                Debug.print("invItemLocation.getIlCode(): " + invItemLocation.getInvItem().getIiName());

                prevExpiryDates = prevCst.getCstExpiryDate();
                qtyPrpgt = prevCst.getCstRemainingQuantity();
                Debug.print("UNA prevExpiryDates: " + prevExpiryDates);
                Debug.print("UNA qtyPrpgt: " + qtyPrpgt);
                if (prevExpiryDates == null) {
                    prevExpiryDates = "";
                }

            }
            catch (Exception ex) {

            }

            // create costing
            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR,
                    0d, 0d, CST_ST_QTY, CST_ST_CST, 0d, 0d,
                    CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, 0d,
                    AD_BRNCH, companyCode);
            // invItemLocation.addInvCosting(invCosting);
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setInvBranchStockTransferLine(invBranchStockTransferLine);

            invCosting.setCstQuantity(CST_ST_QTY);
            invCosting.setCstCost(CST_ST_CST);
            String check = "";
            if (invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiTraceMisc() != 0) {
                // Get Latest Expiry Dates
                if ((prevExpiryDates != null && prevExpiryDates != "" && prevExpiryDates.length() != 0) || CST_ST_QTY > 0) {

                    String CST_EXPRY_DT = prevExpiryDates;

                    if (invBranchStockTransferLine.getBslMisc() != null && invBranchStockTransferLine.getBslMisc() != "" && invBranchStockTransferLine.getBslMisc().length() != 0) {

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
                            while (txnMiscsIter.hasNext()) {
                                String misc = (String) txnMiscsIter.next();
                                Debug.print(txnMiscsList + " COMPARE " + misc);
                                // misc = delimeter + misc + delimeter;

                                if (txnMiscsList.contains(misc)) {
                                    txnMiscsList.remove(misc);
                                } else {
                                    throw new GlobalExpiryDateNotFoundException(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName());
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
            } else {
                invCosting.setCstExpiryDate("");
            }

            Debug.print("InvBranchStockTransferOutEntryControllerBean post B");
            // if cost variance is not 0, generate cost variance for the transaction
            if (CST_VRNC_VL != 0) {

                this.generateCostVariance(invCosting.getInvItemLocation(), CST_VRNC_VL, "INVBST" + invBranchStockTransferLine.getInvBranchStockTransfer().getBstNumber(), invBranchStockTransferLine.getInvBranchStockTransfer().getBstDescription(), invBranchStockTransferLine.getInvBranchStockTransfer().getBstDate(), USR_NM, AD_BRNCH, companyCode);
                Debug.print("InvBranchStockTransferOutEntryControllerBean post B01");
            }

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
            Debug.print("InvBranchStockTransferOutEntryControllerBean post B02");
            i = invCostings.iterator();

            while (i.hasNext()) {

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) i.next();

                // Check If Previous Records Contains all Out IMEI's
                if (invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiTraceMisc() != 0) {
                    if (invBranchStockTransferLine.getBslMisc() != null && invBranchStockTransferLine.getBslMisc() != "" && invBranchStockTransferLine.getBslMisc().length() != 0) {

                        Debug.print("invAdjustmentLine.getAlMisc(): " + invBranchStockTransferLine.getBslMisc());
                        Debug.print("getAlAdjustQuantity(): " + invBranchStockTransferLine.getBslQuantity());

                        String CST_EXPRY_DT = invPropagatedCosting.getCstExpiryDate();

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
                                    throw new GlobalExpiryDateNotFoundException(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName());
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
                        CST_EXPRY_DT = str.toString();
                        invPropagatedCosting.setCstExpiryDate(CST_EXPRY_DT);
                    } else {
                        invPropagatedCosting.setCstExpiryDate(prevExpiryDates);
                        Debug.print("prevExpiryDates");
                    }
                }

                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_ST_QTY);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_ST_QTY);
            }

            // regenerate cost varaince
            this.regenerateCostVariance(invCostings, invCosting, AD_BRNCH, companyCode);

        }
        catch (AdPRFCoaGlVarianceAccountNotFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlobalExpiryDateNotFoundException ex) {
            Debug.print("Huli Ka");
            Debug.print(ex.getMessage());
            ex.printStackTrace();
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

    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double JL_AMNT, Integer companyCode) {

        Debug.print("InvBranchStockTransferOutEntryControllerBean postToGl");
        try {

            Debug.print("InvBranchStockTransferOutEntryControllerBean postToGl A");
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccountBalance glChartOfAccountBalance = glChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(glAccountingCalendarValue.getAcvCode(), glChartOfAccount.getCoaCode(), companyCode);

            String ACCOUNT_TYPE = glChartOfAccount.getCoaAccountType();
            short FC_EXTNDD_PRCSN = adCompany.getGlFunctionalCurrency().getFcPrecision();

            if (((ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("EXPENSE")) && isDebit == EJBCommon.TRUE) || (!ACCOUNT_TYPE.equals("ASSET") && !ACCOUNT_TYPE.equals("EXPENSE") && isDebit == EJBCommon.FALSE)) {

                glChartOfAccountBalance.setCoabEndingBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabEndingBalance() + JL_AMNT, FC_EXTNDD_PRCSN));

                if (!isCurrentAcv) {

                    glChartOfAccountBalance.setCoabBeginningBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabBeginningBalance() + JL_AMNT, FC_EXTNDD_PRCSN));
                }
            } else {

                glChartOfAccountBalance.setCoabEndingBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabEndingBalance() - JL_AMNT, FC_EXTNDD_PRCSN));

                if (!isCurrentAcv) {

                    glChartOfAccountBalance.setCoabBeginningBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabBeginningBalance() - JL_AMNT, FC_EXTNDD_PRCSN));
                }
            }

            if (isCurrentAcv) {

                if (isDebit == EJBCommon.TRUE) {

                    glChartOfAccountBalance.setCoabTotalDebit(EJBCommon.roundIt(glChartOfAccountBalance.getCoabTotalDebit() + JL_AMNT, FC_EXTNDD_PRCSN));

                } else {

                    glChartOfAccountBalance.setCoabTotalCredit(EJBCommon.roundIt(glChartOfAccountBalance.getCoabTotalCredit() + JL_AMNT, FC_EXTNDD_PRCSN));
                }
            }
            Debug.print("InvBranchStockTransferOutEntryControllerBean postToGl B");
        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void regenerateInventoryDr(LocalInvBranchStockTransfer invBranchStockTransfer, Integer AD_BRNCH, Integer companyCode) throws GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException {

        Debug.print("InvBranchStockTransferOutEntryControllerBean regenerateInventoryDr");

        LocalInvDistributionRecordHome invDistributionRecordHome = null;
        LocalInvCostingHome invCostingHome = null;
        LocalInvLocationHome invLocationHome = null;
        LocalInvItemLocationHome invItemLocationHome = null;
        LocalAdBranchItemLocationHome adBranchItemLocationHome = null;
        LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome = null;
        LocalAdBranchHome adBranchHome = null;

        // Initialize EJB Home

        try {

            invDistributionRecordHome = (LocalInvDistributionRecordHome) EJBHomeFactory.lookUpLocalHome(LocalInvDistributionRecordHome.JNDI_NAME, LocalInvDistributionRecordHome.class);
            invCostingHome = (LocalInvCostingHome) EJBHomeFactory.lookUpLocalHome(LocalInvCostingHome.JNDI_NAME, LocalInvCostingHome.class);
            invLocationHome = (LocalInvLocationHome) EJBHomeFactory.lookUpLocalHome(LocalInvLocationHome.JNDI_NAME, LocalInvLocationHome.class);
            invItemLocationHome = (LocalInvItemLocationHome) EJBHomeFactory.lookUpLocalHome(LocalInvItemLocationHome.JNDI_NAME, LocalInvItemLocationHome.class);
            adBranchItemLocationHome = (LocalAdBranchItemLocationHome) EJBHomeFactory.lookUpLocalHome(LocalAdBranchItemLocationHome.JNDI_NAME, LocalAdBranchItemLocationHome.class);
            invUnitOfMeasureConversionHome = (LocalInvUnitOfMeasureConversionHome) EJBHomeFactory.lookUpLocalHome(LocalInvUnitOfMeasureConversionHome.JNDI_NAME, LocalInvUnitOfMeasureConversionHome.class);
            adBranchHome = (LocalAdBranchHome) EJBHomeFactory.lookUpLocalHome(LocalAdBranchHome.JNDI_NAME, LocalAdBranchHome.class);

        }
        catch (NamingException ex) {

            throw new EJBException(ex.getMessage());
        }

        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            // regenerate inventory distribution records

            // remove all inventory distribution

            Collection invDistributionRecords = invDistributionRecordHome.findImportableDrByAdjCode(invBranchStockTransfer.getBstCode(), companyCode);

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

                    invItemTransitLocation = invItemLocationHome.findByLocNameAndIiName(invBranchStockTransfer.getInvLocation().getLocName(), invItemName, companyCode);

                }
                catch (FinderException ex) {

                    throw new GlobalInvItemLocationNotFoundException("Transit Location " + invBranchStockTransfer.getInvLocation().getLocName());
                }

                // start date validation
                if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                    Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(invBranchStockTransfer.getBstDate(), invItemName, locName, AD_BRNCH, companyCode);

                    if (!invNegTxnCosting.isEmpty()) {
                        throw new GlobalInventoryDateException(invItemName);
                    }
                }

                // add physical inventory distribution

                double COST = invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiUnitCost();

                try {

                    LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(invBranchStockTransfer.getBstDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);

                    if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Average")) {
                        COST = invCosting.getCstRemainingQuantity() <= 0 ? COST : Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                    }

                    if (COST <= 0) {
                        COST = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                    } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("FIFO")) {
                        COST = invCosting.getCstRemainingQuantity() == 0 ? COST : this.getInvFifoCost(invBranchStockTransfer.getBstDate(), invItemLocation.getIlCode(), invBranchStockTransferLine.getBslQuantity(), invBranchStockTransferLine.getBslUnitCost(), false, AD_BRNCH, companyCode);
                    } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Standard")) {
                        COST = invItemLocation.getInvItem().getIiUnitCost();
                    }

                }
                catch (FinderException ex) {
                }

                LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(), invBranchStockTransferLine.getInvUnitOfMeasure().getUomName(), companyCode);
                LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(), invBranchStockTransferLine.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName(), companyCode);

                COST = EJBCommon.roundIt(COST * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), this.getGlFcPrecisionUnit(companyCode));

                double AMOUNT = 0d;

                AMOUNT = EJBCommon.roundIt(invBranchStockTransferLine.getBslQuantity() * COST, this.getGlFcPrecisionUnit(companyCode));

                // check branch mapping

                LocalAdBranchItemLocation adBranchItemLocation = null;

                try {

                    adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(invBranchStockTransferLine.getInvItemLocation().getIlCode(), AD_BRNCH, companyCode);

                }
                catch (FinderException ex) {

                }

                LocalGlChartOfAccount glChartOfAccount = null;

                if (adBranchItemLocation == null) {

                    glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(invBranchStockTransferLine.getInvItemLocation().getIlGlCoaInventoryAccount());

                } else {

                    glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchItemLocation.getBilCoaGlInventoryAccount());
                }
                // TODO: add new inv Tag

                this.addInvDrEntry(invBranchStockTransfer.getInvDrNextLine(), "INVENTORY", EJBCommon.FALSE, Math.abs(AMOUNT), glChartOfAccount.getCoaCode(), invBranchStockTransfer, AD_BRNCH, companyCode);

                // check branch mapping for transit location

                LocalAdBranchItemLocation adBranchItemTransitLocation = null;
                LocalAdBranch adBranchTo = adBranchHome.findByBrBranchCode(invBranchStockTransfer.getAdBranch().getBrBranchCode(), companyCode);

                try {

                    adBranchItemTransitLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(invItemTransitLocation.getIlCode(), adBranchTo.getBrCode(), companyCode);

                }
                catch (FinderException ex) {

                }

                LocalGlChartOfAccount glChartOfAccountTransit = null;

                if (adBranchItemTransitLocation == null) {

                    glChartOfAccountTransit = glChartOfAccountHome.findByPrimaryKey(invItemTransitLocation.getIlGlCoaInventoryAccount());

                } else {

                    glChartOfAccountTransit = glChartOfAccountHome.findByPrimaryKey(adBranchItemTransitLocation.getBilCoaGlWipAccount());
                }

                // add dr for inventory transit location
                // TODO: add new inv Tag
                this.addInvDrEntry(invBranchStockTransfer.getInvDrNextLine(), "IN TRANSIT", EJBCommon.TRUE, Math.abs(AMOUNT), glChartOfAccountTransit.getCoaCode(), invBranchStockTransfer, AD_BRNCH, companyCode);
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

        Debug.print("InvBranchStockTransferOutEntryControllerBean voidInvAdjustment");

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

                this.addInvDrEntry(invAdjustment.getInvDrNextLine(), invDistributionRecord.getDrClass(), invDistributionRecord.getDrDebit() == EJBCommon.TRUE ? EJBCommon.FALSE : EJBCommon.TRUE, invDistributionRecord.getDrAmount(), EJBCommon.TRUE, invDistributionRecord.getInvChartOfAccount().getCoaCode(), invAdjustment, AD_BRNCH, companyCode);
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

                this.addInvAlEntry(invAdjustmentLine.getInvItemLocation(), invAdjustment, (invAdjustmentLine.getAlUnitCost()) * -1, EJBCommon.TRUE, companyCode);
            }

            invAdjustment.setAdjVoid(EJBCommon.TRUE);

            this.executeInvAdjPost(invAdjustment.getAdjCode(), invAdjustment.getAdjLastModifiedBy(), AD_BRNCH, companyCode);

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void generateCostVariance(LocalInvItemLocation invItemLocation, double CST_VRNC_VL, String ADJ_RFRNC_NMBR, String ADJ_DSCRPTN, Date ADJ_DT, String USR_NM, Integer AD_BRNCH, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

    }

    private void regenerateCostVariance(Collection invCostings, LocalInvCosting invCosting, Integer AD_BRNCH, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

    }

    private void addInvDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, byte DR_RVRSL, Integer COA_CODE, LocalInvAdjustment invAdjustment, Integer AD_BRNCH, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("InvBranchStockTransferOutEntryControllerBean addInvDrEntry");
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

            LocalInvDistributionRecord invDistributionRecord = invDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), DR_RVRSL, EJBCommon.FALSE, companyCode);

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

    private void executeInvAdjPost(Integer ADJ_CODE, String USR_NM, Integer AD_BRNCH, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException {

        Debug.print("InvBranchStockTransferOutEntryControllerBean executeInvAdjPost");
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
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.FALSE, invAdjustment.getAdjCode(), companyCode);
            } else {
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.TRUE, invAdjustment.getAdjCode(), companyCode);
            }

            for (Object adjustmentLine : invAdjustmentLines) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) adjustmentLine;

                LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(invAdjustment.getAdjDate(), invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(), invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName(), AD_BRNCH, companyCode);

                this.postInvAdjustmentToInventory(invAdjustmentLine, invAdjustment.getAdjDate(), 0, invAdjustmentLine.getAlUnitCost(), invCosting.getCstRemainingQuantity(), invCosting.getCstRemainingValue() + invAdjustmentLine.getAlUnitCost(), AD_BRNCH, companyCode);
            }

            // post to gl if necessary

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            if (adPreference.getPrfInvGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                // validate if date has no period and period is closed

                LocalGlSetOfBook glJournalSetOfBook = null;

                try {

                    glJournalSetOfBook = glSetOfBookHome.findByDate(invAdjustment.getAdjDate(), companyCode);

                }
                catch (FinderException ex) {

                    throw new GlJREffectiveDateNoPeriodExistException();
                }

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), invAdjustment.getAdjDate(), companyCode);

                if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P') {

                    throw new GlJREffectiveDatePeriodClosedException();
                }

                // check if invoice is balance if not check suspense posting

                LocalGlJournalLine glOffsetJournalLine = null;

                Collection invDistributionRecords = null;

                if (invAdjustment.getAdjVoid() == EJBCommon.FALSE) {

                    invDistributionRecords = invDistributionRecordHome.findImportableDrByDrReversedAndAdjCode(EJBCommon.FALSE, invAdjustment.getAdjCode(), companyCode);

                } else {

                    invDistributionRecords = invDistributionRecordHome.findImportableDrByDrReversedAndAdjCode(EJBCommon.TRUE, invAdjustment.getAdjCode(), companyCode);
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

                        glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("INVENTORY", "INVENTORY ADJUSTMENTS", companyCode);

                    }
                    catch (FinderException ex) {

                        throw new GlobalJournalNotBalanceException();
                    }

                    if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                        glOffsetJournalLine = glJournalLineHome.create((short) (invDistributionRecords.size() + 1), EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);

                    } else {

                        glOffsetJournalLine = glJournalLineHome.create((short) (invDistributionRecords.size() + 1), EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
                    }

                    LocalGlChartOfAccount glChartOfAccount = glSuspenseAccount.getGlChartOfAccount();
                    // glChartOfAccount.addGlJournalLine(glOffsetJournalLine);
                    glOffsetJournalLine.setGlChartOfAccount(glChartOfAccount);

                } else if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE && TOTAL_DEBIT != TOTAL_CREDIT) {
                    Debug.print("TOTAL_DEBIT=" + TOTAL_DEBIT);
                    Debug.print("TOTAL_CREDIT=" + TOTAL_CREDIT);

                    throw new GlobalJournalNotBalanceException();
                }

                // create journal batch if necessary

                LocalGlJournalBatch glJournalBatch = null;
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");

                try {

                    glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " INVENTORY ADJUSTMENTS", AD_BRNCH, companyCode);

                }
                catch (FinderException ex) {

                }

                if (glJournalBatch == null) {

                    glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " INVENTORY ADJUSTMENTS", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, AD_BRNCH, companyCode);
                }

                // create journal entry

                LocalGlJournal glJournal = glJournalHome.create(invAdjustment.getAdjReferenceNumber(), invAdjustment.getAdjDescription(), invAdjustment.getAdjDate(), 0.0d, null, invAdjustment.getAdjDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM, EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, EJBCommon.FALSE, null, AD_BRNCH, companyCode);

                LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("INVENTORY", companyCode);
                glJournal.setGlJournalSource(glJournalSource);

                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

                LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("INVENTORY ADJUSTMENTS", companyCode);
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

                    LocalGlJournalLine glJournalLine = glJournalLineHome.create(invDistributionRecord.getDrLine(), invDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

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

                    this.postToGl(glAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), true, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                    // post to subsequent acvs (propagate)

                    Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), glAccountingCalendarValue.getAcvPeriodNumber(), companyCode);

                    for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {

                        LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;

                        this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                    }

                    // post to subsequent years if necessary

                    Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode);

                    if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                        adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                        LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(adCompany.getCmpRetainedEarnings(), AD_BRNCH, companyCode);

                        for (Object subsequentSetOfBook : glSubsequentSetOfBooks) {

                            LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) subsequentSetOfBook;

                            String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                            // post to subsequent acvs of subsequent set of book(propagate)

                            Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                            for (Object accountingCalendarValue : glAccountingCalendarValues) {

                                LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                                if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY") || ACCOUNT_TYPE.equals("OWNERS EQUITY")) {

                                    this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                                } else { // revenue & expense

                                    this.postToGl(glSubsequentAccountingCalendarValue, glRetainedEarningsAccount, false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                                }
                            }

                            if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) {
                                break;
                            }
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

    private LocalInvAdjustmentLine addInvAlEntry(LocalInvItemLocation invItemLocation, LocalInvAdjustment invAdjustment, double CST_VRNC_VL, byte AL_VD, Integer companyCode) {

        Debug.print("InvBranchStockTransferOutEntryControllerBean addInvAlEntry");
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

    private void postInvAdjustmentToInventory(LocalInvAdjustmentLine invAdjustmentLine, Date CST_DT, double CST_ADJST_QTY, double CST_ADJST_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, Integer AD_BRNCH, Integer companyCode) {

        Debug.print("InvBranchStockTransferOutEntryControllerBean postInvAdjustmentToInventory");
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

                invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - Math.abs(CST_ADJST_QTY));
            }

            // create costing

            try {

                // generate line number

                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            }
            catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d,
                    0d, CST_ADJST_QTY, CST_ADJST_CST, 0d, 0d,
                    CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d,
                    CST_ADJST_QTY > 0 ? CST_ADJST_QTY : 0, AD_BRNCH, companyCode);
            // invItemLocation.addInvCosting(invCosting);
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setInvAdjustmentLine(invAdjustmentLine);

            invCosting.setCstQuantity(CST_ADJST_QTY);
            invCosting.setCstCost(CST_ADJST_CST);

            // propagate balance if necessary

            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);

            for (Object costing : invCostings) {

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) costing;

                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_ADJST_QTY);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_ADJST_CST);
            }

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArSoNumberPostedSoByBrCode(Integer AD_BRNCH, Integer companyCode) {

        Debug.print("InvBranchStockTransferOutEntryControllerBean getArSoNumberPostedSoByBrCode");
        Collection arSalesOrderList = null;
        ArrayList list = new ArrayList();
        try {

            arSalesOrderList = arSalesOrderHome.findPostedSoByMobileBrCode(AD_BRNCH, companyCode);

            for (Object o : arSalesOrderList) {

                LocalArSalesOrder arSalesOrder = (LocalArSalesOrder) o;
                list.add(arSalesOrder.getSoDocumentNumber());
            }

        }
        catch (FinderException ex) {

            ex.printStackTrace();
        }

        return list;
    }

    public boolean getInvTraceMisc(String II_NAME, Integer companyCode) {

        Debug.print("InvBranchStockTransferOrderOutEntryController getInvTraceMisc");
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

    private void createInvTagList(LocalInvBranchStockTransferLine invBranchStockTransferLine, ArrayList list, Integer companyCode) throws Exception {

        Debug.print("InvBranchStockTransferOrderOutEntryController createInvTagList");
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
                    invTag = invTagHome.create(tgLstDetails.getTgPropertyCode(), tgLstDetails.getTgSerialNumber(), null, tgLstDetails.getTgExpiryDate(), tgLstDetails.getTgSpecs(), companyCode, tgLstDetails.getTgTransactionDate(), tgLstDetails.getTgType());

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

    private LocalAdApprovalQueue createApprovalQueue(Integer branchCode, Integer companyCode, LocalAdApprovalQueueHome adApprovalQueueHome, LocalInvBranchStockTransfer invBranchStockTransfer, LocalAdAmountLimit adAmountLimit, LocalAdApprovalUser adApprovalUser) throws CreateException {

        return adApprovalQueueHome.AqForApproval(EJBCommon.TRUE).AqDocument("INV BRANCH STOCK TRANSFER").AqDocumentCode(invBranchStockTransfer.getBstCode()).AqDocumentNumber(invBranchStockTransfer.getBstNumber()).AqDate(invBranchStockTransfer.getBstDate()).AqAndOr(adAmountLimit.getCalAndOr()).AqUserOr(adApprovalUser.getAuOr()).AqAdBranch(branchCode).AqAdCompany(companyCode).buildApprovalQueue();
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("InvBranchStockTransferOutEntryControllerBean ejbCreate");
    }

}