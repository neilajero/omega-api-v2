/**
 * @copyright 2022 Omega Business Consulting, Inc.
 * @class InvAdjustmentEntryControllerBean
 */
package com.ejb.txn.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ap.ILocalApSupplierHome;
import com.ejb.dao.gl.*;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.entities.gl.*;
import com.ejb.entities.inv.*;
import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.ejb.exception.inv.InvTagSerialNumberNotFoundException;
import com.ejb.txnreports.inv.InvRepItemCostingController;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.ad.AdBranchDetails;
import com.util.inv.InvAdjustmentDetails;
import com.util.mod.ad.AdModApprovalQueueDetails;
import com.util.mod.ap.ApModSupplierDetails;
import com.util.mod.inv.InvModAdjustmentDetails;
import com.util.mod.inv.InvModAdjustmentLineDetails;
import com.util.mod.inv.InvModTagListDetails;
import com.util.mod.inv.InvModUnitOfMeasureDetails;
import jakarta.ejb.*;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.NamingException;
import java.util.*;

@Stateless(name = "InvAdjustmentEntryControllerEJB")
public class InvAdjustmentEntryControllerBean extends EJBContextClass implements InvAdjustmentEntryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    public InvRepItemCostingController ejbRIC;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalInvAdjustmentHome invAdjustmentHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdAmountLimitHome adAmountLimitHome;
    @EJB
    private LocalAdApprovalUserHome adApprovalUserHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvTagHome invTagHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalInvAdjustmentLineHome invAdjustmentLineHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalAdDeleteAuditTrailHome adDeleteAuditTrailHome;
    @EJB
    private LocalInvDistributionRecordHome invDistributionRecordHome;
    @EJB
    private ILocalGlSetOfBookHome glSetOfBookHome;
    @EJB
    private ILocalGlAccountingCalendarValueHome glAccountingCalendarValueHome;
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
    private ILocalGlChartOfAccountBalanceHome glChartOfAccountBalanceHome;
    @EJB
    private ILocalApSupplierHome apSupplierHome;

    public ArrayList getApSplAll(Integer branchCode, Integer companyCode) {

        Debug.print("InvAdjustmentEntryControllerBean getApSplAll");
        ArrayList list = new ArrayList();
        try {

            Collection apSuppliers = apSupplierHome.findEnabledSplAll(branchCode, companyCode);

            for (Object supplier : apSuppliers) {

                LocalApSupplier apSupplier = (LocalApSupplier) supplier;

                list.add(apSupplier.getSplSupplierCode());
            }

            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ApModSupplierDetails getApSplBySplSupplierCode(String SPL_SPPLR_CODE, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("InvAdjustmentEntryControllerBean getApSplBySplSupplierCode");
        try {

            LocalApSupplier apSupplier = null;

            try {

                apSupplier = apSupplierHome.findBySplSupplierCode(SPL_SPPLR_CODE, companyCode);

            }
            catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ApModSupplierDetails mdetails = new ApModSupplierDetails();

            mdetails.setSplPytName(
                    apSupplier.getAdPaymentTerm() != null ? apSupplier.getAdPaymentTerm().getPytName() : null);
            mdetails.setSplScWtcName(apSupplier.getApSupplierClass().getApWithholdingTaxCode() != null
                    ? apSupplier.getApSupplierClass().getApWithholdingTaxCode().getWtcName()
                    : null);
            mdetails.setSplName(apSupplier.getSplName());

            if (apSupplier.getApSupplierClass().getApTaxCode() != null) {

                mdetails.setSplScTcName(apSupplier.getApSupplierClass().getApTaxCode().getTcName());
                mdetails.setSplScTcType(apSupplier.getApSupplierClass().getApTaxCode().getTcType());
                mdetails.setSplScTcRate(apSupplier.getApSupplierClass().getApTaxCode().getTcRate());
            }

            return mdetails;

        }
        catch (GlobalNoRecordFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfApUseSupplierPulldown(Integer companyCode) {

        Debug.print("InvAdjustmentEntryControllerBean getAdPrfApUseSupplierPulldown");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfApUseSupplierPulldown();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getInvLocAll(Integer companyCode) {

        Debug.print("InvAdjustmentEntryControllerBean getInvLocAll");
        Collection invLocations;
        ArrayList list = new ArrayList();
        try {

            invLocations = invLocationHome.findLocAll(companyCode);

            if (invLocations.isEmpty()) {

                return null;
            }

            for (Object location : invLocations) {

                LocalInvLocation invLocation = (LocalInvLocation) location;
                String details = invLocation.getLocName();

                list.add(details);
            }

            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void executeInvAdjSubmit(Integer ADJ_CODE, Integer branchCode, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException, GlobalBranchAccountNumberInvalidException,
            GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException,
            GlobalTransactionAlreadyPostedException, GlobalNoApprovalRequesterFoundException,
            GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException,
            GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException,
            GlobalJournalNotBalanceException, GlobalDocumentNumberNotUniqueException, GlobalInventoryDateException,
            GlobalInvTagMissingException, InvTagSerialNumberNotFoundException, GlobalInvTagExistingException,
            GlobalAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException,
            GlobalExpiryDateNotFoundException, GlobalMiscInfoIsRequiredException, GlobalRecordInvalidException {

        Debug.print("InvAdjustmentEntryControllerBean executeInvAdjSubmit");
        try {

            LocalInvAdjustment invAdjustment = null;
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            // validate if Adjustment is already deleted

            try {

                invAdjustment = invAdjustmentHome.findByPrimaryKey(ADJ_CODE);

            }
            catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if adjustment is already posted, void, approved or pending

            if (invAdjustment.getAdjApprovalStatus() != null) {

                if (invAdjustment.getAdjApprovalStatus().equals("APPROVED")
                        || invAdjustment.getAdjApprovalStatus().equals("N/A")) {

                    throw new GlobalTransactionAlreadyApprovedException();

                } else if (invAdjustment.getAdjApprovalStatus().equals("PENDING")) {

                    throw new GlobalTransactionAlreadyPendingException();
                }
            }

            if (invAdjustment.getAdjPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();
            }

            Collection invAdjustments = invAdjustment.getInvAdjustmentLines();
            double ABS_AMOUNT = 0d;

            for (Object adjustment : invAdjustments) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) adjustment;
                ABS_AMOUNT += invAdjustmentLine.getAlAdjustQuantity() * invAdjustmentLine.getAlUnitCost();
            }

            // Insufficient Stocks
            if (adPreference.getPrfArCheckInsufficientStock() == EJBCommon.TRUE) {
                Debug.print("CHECK A");
                boolean hasInsufficientItems = false;
                StringBuilder insufficientItems = new StringBuilder();

                Collection invAdjustmentLines = invAdjustment.getInvAdjustmentLines();

                Iterator i = invAdjustmentLines.iterator();

                HashMap cstMap = new HashMap();

                while (i.hasNext()) {

                    LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();

                    if (invAdjustmentLine.getAlAdjustQuantity() < 0) {

                        LocalInvCosting invCosting = null;
                        double CURR_QTY = 0;
                        boolean isIlFound = false;

                        double ILI_QTY = this.convertByUomAndQuantity(invAdjustmentLine.getInvUnitOfMeasure(),
                                invAdjustmentLine.getInvItemLocation().getInvItem(),
                                Math.abs(invAdjustmentLine.getAlAdjustQuantity()), companyCode);

                        if (cstMap.containsKey(invAdjustmentLine.getInvItemLocation().getIlCode().toString())) {

                            isIlFound = true;
                            CURR_QTY = (Double) cstMap
                                    .get(invAdjustmentLine.getInvItemLocation().getIlCode().toString());

                        } else {

                            try {

                                invCosting = invCostingHome
                                        .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                                invAdjustment.getAdjDate(),
                                                invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(),
                                                invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName(),
                                                branchCode, companyCode);
                                CURR_QTY = invCosting.getCstRemainingQuantity();

                            }
                            catch (FinderException ex) {

                            }
                        }

                        if (invCosting != null) {

                            CURR_QTY = this.convertByUomAndQuantity(invAdjustmentLine.getInvUnitOfMeasure(),
                                    invAdjustmentLine.getInvItemLocation().getInvItem(),
                                    Math.abs(invCosting.getCstRemainingQuantity()), companyCode);
                        }

                        double LOWEST_QTY = this.convertByUomAndQuantity(invAdjustmentLine.getInvUnitOfMeasure(),
                                invAdjustmentLine.getInvItemLocation().getInvItem(), 1, companyCode);

                        if ((invCosting == null && isIlFound == false) || CURR_QTY == 0
                                || CURR_QTY - ILI_QTY <= -LOWEST_QTY) {

                            hasInsufficientItems = true;
                            insufficientItems.append(invAdjustmentLine.getInvItemLocation().getInvItem().getIiName()).append(", ");
                        }

                        CURR_QTY -= ILI_QTY;

                        if (isIlFound) {
                            cstMap.remove(invAdjustmentLine.getInvItemLocation().getIlCode().toString());
                        }

                        cstMap.put(invAdjustmentLine.getInvItemLocation().getIlCode().toString(), CURR_QTY);
                    }
                }
                if (hasInsufficientItems) {

                    throw new GlobalRecordInvalidException(
                            insufficientItems.substring(0, insufficientItems.lastIndexOf(",")));
                }
            }

            // generate approval status

            String INV_APPRVL_STATUS = null;

            LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);

            // check if ar invoice approval is enabled

            if (adApproval.getAprEnableInvAdjustment() == EJBCommon.FALSE) {

                INV_APPRVL_STATUS = "N/A";

            } else {

                // check if invoice is self approved

                LocalAdAmountLimit adAmountLimit = null;

                try {

                    adAmountLimit = adAmountLimitHome.findByAdcTypeAndAuTypeAndUsrName("INV ADJUSTMENT", "REQUESTER",
                            invAdjustment.getAdjLastModifiedBy(), companyCode);

                }
                catch (FinderException ex) {

                    throw new GlobalNoApprovalRequesterFoundException();
                }

                if (ABS_AMOUNT < adAmountLimit.getCalAmountLimit()) {
                    System.out.print("2 <----hehre");
                    INV_APPRVL_STATUS = "N/A";

                } else {

                    // for approval, create approval queue

                    Collection adAmountLimits = adAmountLimitHome.findByAdcTypeAndGreaterThanCalAmountLimit(
                            "INV ADJUSTMENT", adAmountLimit.getCalAmountLimit(), companyCode);

                    if (adAmountLimits.isEmpty()) {

                        Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER",
                                adAmountLimit.getCalCode(), companyCode);

                        if (adApprovalUsers.isEmpty()) {

                            throw new GlobalNoApprovalApproverFoundException();
                        }

                        for (Object approvalUser : adApprovalUsers) {

                            LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                            LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(branchCode, companyCode,
                                    adApprovalQueueHome, invAdjustment, adAmountLimit, adApprovalUser);

                            adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                        }

                    } else {

                        boolean isApprovalUsersFound = false;
                        Iterator i = adAmountLimits.iterator();

                        while (i.hasNext()) {

                            LocalAdAmountLimit adNextAmountLimit = (LocalAdAmountLimit) i.next();

                            if (ABS_AMOUNT < adNextAmountLimit.getCalAmountLimit()) {

                                Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER",
                                        adAmountLimit.getCalCode(), companyCode);

                                for (Object approvalUser : adApprovalUsers) {

                                    isApprovalUsersFound = true;

                                    LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                    LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(branchCode, companyCode,
                                            adApprovalQueueHome, invAdjustment, adAmountLimit, adApprovalUser);

                                    adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                                }

                                break;

                            } else if (!i.hasNext()) {

                                Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER",
                                        adNextAmountLimit.getCalCode(), companyCode);

                                for (Object approvalUser : adApprovalUsers) {

                                    isApprovalUsersFound = true;

                                    LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                    LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(branchCode, companyCode,
                                            adApprovalQueueHome, invAdjustment, adNextAmountLimit, adApprovalUser);

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

            invAdjustment.setAdjApprovalStatus(INV_APPRVL_STATUS);

            // set adjustment approval status

            if (INV_APPRVL_STATUS != null && INV_APPRVL_STATUS.equals("N/A")
                    && adPreference.getPrfInvGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                this.executeInvAdjPost(invAdjustment.getAdjCode(), invAdjustment.getAdjLastModifiedBy(), branchCode,
                        companyCode);
            }

        }
        catch (GlobalRecordAlreadyDeletedException | GlobalMiscInfoIsRequiredException
               | GlobalExpiryDateNotFoundException | AdPRFCoaGlVarianceAccountNotFoundException
               | GlobalJournalNotBalanceException | GlJREffectiveDatePeriodClosedException
               | GlJREffectiveDateNoPeriodExistException | GlobalNoApprovalApproverFoundException
               | GlobalNoApprovalRequesterFoundException | GlobalTransactionAlreadyPostedException
               | GlobalTransactionAlreadyPendingException | GlobalTransactionAlreadyApprovedException
               | GlobalBranchAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public boolean getInvTraceMisc(String II_NAME, Integer companyCode) {

        Debug.print("InvAdjustmentEntryControllerBean getInvTraceMisc");
        Collection invLocations = null;
        ArrayList list = new ArrayList();
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

    public InvModAdjustmentDetails getInvAdjByAdjCode(Integer ADJ_CODE, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("InvAdjustmentEntryControllerBean getInvAdjByAdjCode");
        try {

            LocalInvAdjustment invAdjustment = null;

            try {

                invAdjustment = invAdjustmentHome.findByPrimaryKey(ADJ_CODE);

            }
            catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }
            String specs = null;
            String propertyCode = null;
            String expiryDate = null;
            String serialNumber = null;
            ArrayList alList = new ArrayList();
            ArrayList alList2 = new ArrayList();

            Collection invAdjustmentLines = invAdjustment.getInvAdjustmentLines();

            Iterator i = invAdjustmentLines.iterator();

            while (i.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();

                InvModAdjustmentLineDetails mdetails = new InvModAdjustmentLineDetails();
                // TODO: getInvAdjByAdjCode
                System.out
                        .println(mdetails.getTraceMisc() + "<== getTraceMisc under getInvAdjByAdjCode controllerbean");

                mdetails.setTraceMisc(invAdjustmentLine.getInvItemLocation().getInvItem().getIiTraceMisc());
                // ArrayList tagList = new ArrayList();

                if (mdetails.getTraceMisc() == 1) {

                    ArrayList tagList = new ArrayList();

                    tagList = this.getInvTagList(invAdjustmentLine);
                    mdetails.setAlTagList(tagList);
                    mdetails.setTraceMisc(mdetails.getTraceMisc());
                }

                mdetails.setAlCode(invAdjustmentLine.getAlCode());
                mdetails.setAlUnitCost(invAdjustmentLine.getAlUnitCost());
                mdetails.setAlQcNumber(invAdjustmentLine.getAlQcNumber());
                mdetails.setAlQcExpiryDate(invAdjustmentLine.getAlQcExpiryDate());
                mdetails.setAlAdjustQuantity(invAdjustmentLine.getAlAdjustQuantity());
                mdetails.setAlUomName(invAdjustmentLine.getInvUnitOfMeasure().getUomName());
                mdetails.setAlLocName(invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName());
                mdetails.setAlIiName(invAdjustmentLine.getInvItemLocation().getInvItem().getIiName());
                mdetails.setAlIiDescription(invAdjustmentLine.getInvItemLocation().getInvItem().getIiDescription());
                mdetails.setAlMisc(invAdjustmentLine.getAlMisc());

                alList.add(mdetails);
            }

            Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCodeAll("INV ADJUSTMENT",
                    invAdjustment.getAdjCode(), companyCode);

            Debug.print("adApprovalQueues=" + adApprovalQueues.size());
            ArrayList approverlist = new ArrayList();

            i = adApprovalQueues.iterator();

            while (i.hasNext()) {
                LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) i.next();

                AdModApprovalQueueDetails adModApprovalQueueDetails = new AdModApprovalQueueDetails();
                adModApprovalQueueDetails.setAqApprovedDate(adApprovalQueue.getAqApprovedDate());
                adModApprovalQueueDetails.setAqApproverName(adApprovalQueue.getAdUser().getUsrDescription());
                adModApprovalQueueDetails.setAqStatus(
                        adApprovalQueue.getAqApproved() == EJBCommon.TRUE ? adApprovalQueue.getAqLevel() + " APPROVED"
                                : adApprovalQueue.getAqLevel() + " PENDING");

                approverlist.add(adModApprovalQueueDetails);
            }

            InvModAdjustmentDetails details = new InvModAdjustmentDetails();
            details.setAdjCode(invAdjustment.getAdjCode());
            details.setAdjDocumentNumber(invAdjustment.getAdjDocumentNumber());
            details.setAdjReferenceNumber(invAdjustment.getAdjReferenceNumber());
            details.setAdjDescription(invAdjustment.getAdjDescription());
            details.setAdjDate(invAdjustment.getAdjDate());

            try {

                details.setAdjSplSupplierCode(invAdjustment.getApSupplier().getSplSupplierCode());
                details.setAdjSplSupplierName(invAdjustment.getApSupplier().getSplName());

            }
            catch (Exception ex) {

                details.setAdjSplSupplierCode("NO RECORD FOUND");
                details.setAdjSplSupplierName("NO RECORD FOUND");
            }
            details.setAdjType(invAdjustment.getAdjType());
            details.setAdjApprovalStatus(invAdjustment.getAdjApprovalStatus());
            details.setAdjPosted(invAdjustment.getAdjPosted());
            details.setAdjCreatedBy(invAdjustment.getAdjCreatedBy());
            details.setAdjDateCreated(invAdjustment.getAdjDateCreated());
            details.setAdjLastModifiedBy(invAdjustment.getAdjLastModifiedBy());
            details.setAdjDateLastModified(invAdjustment.getAdjDateLastModified());
            details.setAdjApprovedRejectedBy(invAdjustment.getAdjApprovedRejectedBy());
            details.setAdjDateApprovedRejected(invAdjustment.getAdjDateApprovedRejected());
            details.setAdjPostedBy(invAdjustment.getAdjPostedBy());
            details.setAdjDatePosted(invAdjustment.getAdjDatePosted());
            details.setAdjCoaAccountNumber(invAdjustment.getGlChartOfAccount().getCoaAccountNumber());
            details.setAdjCoaAccountDescription(invAdjustment.getGlChartOfAccount().getCoaAccountDescription());
            details.setAdjReasonForRejection(invAdjustment.getAdjReasonForRejection());
            details.setAdjIsCostVariance(invAdjustment.getAdjIsCostVariance());
            details.setAdjVoid(invAdjustment.getAdjVoid());
            details.setAdjNotedBy(invAdjustment.getAdjNotedBy());
            details.setAdjAlList(alList);
            details.setAdjAPRList(approverlist);

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

    public ArrayList getAdUsrAll(Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean getAdUsrAll");
        LocalAdUser adUser;
        Collection adUsers = null;
        ArrayList list = new ArrayList();
        try {

            adUsers = adUserHome.findUsrAll(companyCode);

        }
        catch (FinderException ex) {
        }
        catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        if (adUsers.isEmpty()) {

            return null;
        }
        for (Object user : adUsers) {

            adUser = (LocalAdUser) user;

            list.add(adUser.getUsrName());
        }
        return list;
    }

    public String getNotedByMe(Integer companyCode) {

        Debug.print("InvAdjustmentEntryControllerBean getInvLocAll");
        LocalAdPreference adPreference;
        String result;
        try {

            adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            result = adPreference.getPrfApDefaultChecker();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
        return result;
    }

    public ArrayList getInvUomByIiName(String II_NM, Integer companyCode) {

        Debug.print("InvAdjustmentEntryControllerBean getInvUomByIiName");
        ArrayList list = new ArrayList();
        try {

            LocalInvItem invItem = null;
            LocalInvUnitOfMeasure invItemUnitOfMeasure = null;

            invItem = invItemHome.findByIiName(II_NM, companyCode);
            invItemUnitOfMeasure = invItem.getInvUnitOfMeasure();

            for (Object o : invUnitOfMeasureHome.findByUomAdLvClass(invItemUnitOfMeasure.getUomAdLvClass(), companyCode)) {

                LocalInvUnitOfMeasure invUnitOfMeasure = (LocalInvUnitOfMeasure) o;

                try {
                    LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome
                            .findUmcByIiNameAndUomName(II_NM, invUnitOfMeasure.getUomName(), companyCode);
                    if (invUnitOfMeasureConversion.getUmcBaseUnit() == EJBCommon.FALSE
                            && invUnitOfMeasureConversion.getUmcConversionFactor() == 1) {
                        continue;
                    }
                }
                catch (FinderException ex) {
                    continue;
                }

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

    public Integer saveInvAdjEntryMobile(
            InvModAdjustmentDetails details, ArrayList alList, boolean isDraft, Integer branchCode, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException, GlobalBranchAccountNumberInvalidException,
            GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException,
            GlobalTransactionAlreadyPostedException, GlobalNoApprovalRequesterFoundException,
            GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException,
            GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException,
            GlobalJournalNotBalanceException, GlobalDocumentNumberNotUniqueException, GlobalInventoryDateException,
            GlobalAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException,
            GlobalExpiryDateNotFoundException, GlobalMiscInfoIsRequiredException, GlobalRecordInvalidException {

        Debug.print("InvAdjustmentEntryControllerBean saveInvAdjEntryMobile");

        try {

            LocalInvAdjustment invAdjustment = null;

            // validate if Adjustment is already deleted
            try {
                if (details.getAdjCode() != null) {
                    invAdjustment = invAdjustmentHome.findByPrimaryKey(details.getAdjCode());
                }
            }
            catch (FinderException ex) {
                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if adjustment is already posted, void, approved or pending
            if (details.getAdjCode() != null) {
                if (invAdjustment.getAdjApprovalStatus() != null) {
                    if (invAdjustment.getAdjApprovalStatus().equals("APPROVED")
                            || invAdjustment.getAdjApprovalStatus().equals("N/A")) {
                        throw new GlobalTransactionAlreadyApprovedException();
                    } else if (invAdjustment.getAdjApprovalStatus().equals("PENDING")) {
                        throw new GlobalTransactionAlreadyPendingException();
                    }
                }
                if (invAdjustment.getAdjPosted() == EJBCommon.TRUE) {
                    throw new GlobalTransactionAlreadyPostedException();
                }
            }

            LocalInvAdjustment invExistingAdjustment = null;
            try {
                invExistingAdjustment = invAdjustmentHome.findByAdjDocumentNumberAndBrCode(details.getAdjDocumentNumber(), branchCode, companyCode);
            }
            catch (FinderException ex) {
                Debug.print("Finder Exception : " + ex.getMessage());
            }

            // validate if document number is unique document number is automatic then set next sequence
            if (details.getAdjCode() == null) {
                LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
                LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
                if (invExistingAdjustment != null) {
                    throw new GlobalDocumentNumberNotUniqueException();
                }

                try {
                    adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("INV ADJUSTMENT", companyCode);
                }
                catch (FinderException ex) {
                    Debug.print("Finder Exception : " + ex.getMessage());
                }

                try {
                    adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome
                            .findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode);
                }
                catch (FinderException ex) {
                    Debug.print("Finder Exception : " + ex.getMessage());
                }

                if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A'
                        && (details.getAdjDocumentNumber() == null
                        || details.getAdjDocumentNumber().trim().length() == 0)) {

                    while (true) {
                        if (adBranchDocumentSequenceAssignment == null
                                || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {
                            try {
                                invAdjustmentHome.findByAdjDocumentNumberAndBrCode(
                                        adDocumentSequenceAssignment.getDsaNextSequence(), branchCode, companyCode);
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon
                                        .incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                            }
                            catch (FinderException ex) {
                                details.setAdjDocumentNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon
                                        .incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                break;
                            }
                        } else {
                            try {
                                invAdjustmentHome.findByAdjDocumentNumberAndBrCode(
                                        adBranchDocumentSequenceAssignment.getBdsNextSequence(), branchCode, companyCode);
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(
                                        adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                            }
                            catch (FinderException ex) {
                                details.setAdjDocumentNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(
                                        adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                break;
                            }
                        }
                    }
                }

            } else {

                if (invExistingAdjustment != null && !invExistingAdjustment.getAdjCode().equals(details.getAdjCode())) {
                    throw new GlobalDocumentNumberNotUniqueException();
                }

                assert invAdjustment != null;
                if (!Objects.equals(invAdjustment.getAdjDocumentNumber(), details.getAdjDocumentNumber())
                        && (details.getAdjDocumentNumber() == null
                        || details.getAdjDocumentNumber().trim().length() == 0)) {
                    details.setAdjDocumentNumber(invAdjustment.getAdjDocumentNumber());
                }
            }

            // used in checking if invoice should re-generate distribution records and re-calculate taxes
            boolean isRecalculate = false;
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalGlChartOfAccount glChartOfAccount = null;
            try {
                switch (details.getAdjType()) {
                    case "GENERAL" -> glChartOfAccount = glChartOfAccountHome
                            .findByPrimaryKey(adPreference.getPrfInvGeneralAdjustmentAccount());
                    case "ISSUANCE" -> glChartOfAccount = glChartOfAccountHome
                            .findByPrimaryKey(adPreference.getPrfInvIssuanceAdjustmentAccount());
                    case "PRODUCTION" -> glChartOfAccount = glChartOfAccountHome
                            .findByPrimaryKey(adPreference.getPrfInvProductionAdjustmentAccount());
                }
            }
            catch (FinderException ex) {
                throw new GlobalAccountNumberInvalidException();
            }

            // create adjustment
            if (details.getAdjCode() == null) {
                invAdjustment = invAdjustmentHome.create(details.getAdjDocumentNumber(),
                        details.getAdjReferenceNumber(), details.getAdjDescription(), details.getAdjDate(),
                        details.getAdjType(), details.getAdjApprovalStatus(), EJBCommon.FALSE,
                        details.getAdjCreatedBy(), details.getAdjDateCreated(), details.getAdjLastModifiedBy(),
                        details.getAdjDateLastModified(), null, null,
                        null, null, null, null, EJBCommon.FALSE,
                        EJBCommon.FALSE, branchCode, companyCode);

            } else {

                // check if critical fields are changed
                if (!invAdjustment.getGlChartOfAccount().getCoaAccountNumber()
                        .equals(glChartOfAccount.getCoaAccountNumber())
                        || alList.size() != invAdjustment.getInvAdjustmentLines().size()
                        || !(invAdjustment.getAdjDate().equals(details.getAdjDate()))) {
                    isRecalculate = true;

                } else if (alList.size() == invAdjustment.getInvAdjustmentLines().size()) {

                    Iterator alIter = invAdjustment.getInvAdjustmentLines().iterator();
                    Iterator alListIter = alList.iterator();
                    while (alIter.hasNext()) {
                        LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) alIter.next();
                        InvModAdjustmentLineDetails mdetails = (InvModAdjustmentLineDetails) alListIter.next();
                        if (!invAdjustmentLine.getInvItemLocation().getInvItem().getIiName()
                                .equals(mdetails.getAlIiName())
                                || !invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName()
                                .equals(mdetails.getAlLocName())
                                || !invAdjustmentLine.getInvUnitOfMeasure().getUomName().equals(mdetails.getAlUomName())
                                || invAdjustmentLine.getAlAdjustQuantity() != mdetails.getAlAdjustQuantity()
                                || invAdjustmentLine.getAlUnitCost() != mdetails.getAlUnitCost()
                                || !Objects.equals(invAdjustmentLine.getAlMisc(), mdetails.getAlMisc())) {
                            isRecalculate = true;
                            break;
                        }
                    }
                }

                invAdjustment.setAdjDocumentNumber(details.getAdjDocumentNumber());
                invAdjustment.setAdjReferenceNumber(details.getAdjReferenceNumber());
                invAdjustment.setAdjDescription(details.getAdjDescription());
                invAdjustment.setAdjDate(details.getAdjDate());
                invAdjustment.setAdjType(details.getAdjType());
                invAdjustment.setAdjApprovalStatus(details.getAdjApprovalStatus());
                invAdjustment.setAdjLastModifiedBy(details.getAdjLastModifiedBy());
                invAdjustment.setAdjDateLastModified(details.getAdjDateLastModified());
                invAdjustment.setAdjReasonForRejection(null);
            }

            invAdjustment.setGlChartOfAccount(glChartOfAccount);

            double ABS_TOTAL_AMOUNT = 0d;

            if (isRecalculate) {

                // remove all adjustment lines
                Collection invAdjustmentLines = invAdjustment.getInvAdjustmentLines();
                Iterator i = invAdjustmentLines.iterator();
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
                    i.remove();
                    em.remove(invAdjustmentLine);
                }

                // remove all distribution records
                Collection arDistributionRecords = invAdjustment.getInvDistributionRecords();
                i = arDistributionRecords.iterator();
                while (i.hasNext()) {
                    LocalInvDistributionRecord arDistributionRecord = (LocalInvDistributionRecord) i.next();
                    i.remove();
                    em.remove(arDistributionRecord);
                }

                // add new adjustment lines and distribution record
                double TOTAL_AMOUNT = 0d;
                byte DEBIT;
                i = alList.iterator();
                while (i.hasNext()) {
                    InvModAdjustmentLineDetails mdetails = (InvModAdjustmentLineDetails) i.next();
                    LocalInvItemLocation invItemLocation;
                    LocalInvItem invItem;
                    try {

                        invItem = invItemHome.findByPartNumber(mdetails.getAlPartNumber(), companyCode);
                        LocalInvLocation invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(invLocation.getLocName(),
                                invItem.getIiName(), companyCode);
                        mdetails.setAlIiName(invItem.getIiName());

                        if (details.getAdjPurchaseUnit()) {
                            Iterator iter = null;
                            try {
                                iter = invUnitOfMeasureHome
                                        .findByUomAdLvClass(invItem.getInvUnitOfMeasure().getUomAdLvClass(), companyCode)
                                        .iterator();
                            }
                            catch (FinderException ex) {
                                Debug.print("Finder Exception : " + ex.getMessage());
                            }
                            while (true) {
                                assert iter != null;
                                if (!iter.hasNext()) {
                                    break;
                                }
                                LocalInvUnitOfMeasure invUnitOfMeasure = (LocalInvUnitOfMeasure) iter.next();
                                LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome
                                        .findUmcByIiNameAndUomName(invItem.getIiName(), invUnitOfMeasure.getUomName(),
                                                companyCode);
                                if (invUnitOfMeasureConversion.getUmcBaseUnit() == 1) {
                                    mdetails.setAlUomName(invUnitOfMeasure.getUomName());
                                    break;
                                }
                            }
                        } else {
                            mdetails.setAlUomName(invItem.getInvUnitOfMeasure().getUomName());
                        }
                        mdetails.setAlUnitCost(invItem.getIiUnitCost());
                        mdetails.setAlLocName(invLocation.getLocName());
                    }
                    catch (FinderException ex) {
                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(mdetails.getAlLineNumber()));
                    }

                    // start date validation
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(
                                invAdjustment.getAdjDate(), invItemLocation.getInvItem().getIiName(),
                                invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }

                    LocalInvAdjustmentLine invAdjustmentLine = this.addInvAlEntry(mdetails, invAdjustment,
                            EJBCommon.FALSE, companyCode);

                    // add physical inventory distribution
                    double AMOUNT;
                    if (invAdjustmentLine.getAlAdjustQuantity() > 0) {
                        AMOUNT = EJBCommon.roundIt(
                                invAdjustmentLine.getAlAdjustQuantity() * invAdjustmentLine.getAlUnitCost(),
                                this.getGlFcPrecisionUnit(companyCode));
                        DEBIT = EJBCommon.TRUE;
                    } else {
                        // TODO: CHECK WHY GETTING COST IF ADJUST BY IS NEGATIVE
                        double COST = 0d;
                        try {
                            LocalInvCosting invCosting = invCostingHome
                                    .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                            details.getAdjDate(),
                                            invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(),
                                            invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName(),
                                            branchCode, companyCode);

                            if (invCosting.getCstRemainingQuantity() <= 0 && invCosting.getCstRemainingValue() <= 0) {

                                HashMap<String, Object> criteria = new HashMap<>();
                                criteria.put("itemName", invItemLocation.getInvItem().getIiName());
                                criteria.put("location", invItemLocation.getInvLocation().getLocName());

                                ArrayList branchList = new ArrayList();
                                AdBranchDetails mdetailsb = new AdBranchDetails();
                                mdetailsb.setBrCode(branchCode);
                                branchList.add(mdetailsb);

                                ejbRIC.executeInvFixItemCosting(criteria, branchList, companyCode);

                                invCosting = invCostingHome
                                        .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                                details.getAdjDate(),
                                                invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(),
                                                invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName(),
                                                branchCode, companyCode);
                            }

                            // Validate if AVERAGE or FIFO
                            switch (invCosting.getInvItemLocation().getInvItem().getIiCostMethod()) {
                                case "Average" -> {
                                    if (invCosting.getCstRemainingQuantity() <= 0) {
                                        COST = invItemLocation.getInvItem().getIiUnitCost();
                                    } else {

                                        COST = invCosting.getCstRemainingQuantity() <= 0 ? COST
                                                : Math.abs(invCosting.getCstRemainingValue()
                                                / invCosting.getCstRemainingQuantity());

                                        if (COST <= 0) {
                                            COST = invItemLocation.getInvItem().getIiUnitCost();
                                        }
                                    }
                                }
                                case "FIFO" ->
                                        COST = this.getInvFifoCost(invAdjustment.getAdjDate(), invItemLocation.getIlCode(),
                                                invAdjustmentLine.getAlAdjustQuantity(), invCosting.getCstAdjustCost(), false,
                                                branchCode, companyCode);
                                case "Standard" -> COST = invItemLocation.getInvItem().getIiUnitCost();
                            }

                        }
                        catch (FinderException ex) {
                            COST = invAdjustmentLine.getInvItemLocation().getInvItem().getIiUnitCost();
                        }

                        COST = this.convertCostByUom(invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(),
                                invAdjustmentLine.getInvUnitOfMeasure().getUomName(), COST, true, companyCode);

                        invAdjustmentLine.setAlUnitCost(COST);
                        AMOUNT = EJBCommon.roundIt(invAdjustmentLine.getAlAdjustQuantity() * COST,
                                adPreference.getPrfInvCostPrecisionUnit());
                        DEBIT = EJBCommon.FALSE;
                    }

                    // check for branch mapping
                    LocalAdBranchItemLocation adBranchItemLocation = null;
                    try {
                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(
                                invAdjustmentLine.getInvItemLocation().getIlCode(), branchCode, companyCode);
                    }
                    catch (FinderException ex) {
                        Debug.print("Finder Exception : " + ex.getMessage());
                    }

                    LocalGlChartOfAccount glInventoryChartOfAccount;
                    if (adBranchItemLocation == null) {
                        glInventoryChartOfAccount = glChartOfAccountHome
                                .findByPrimaryKey(invAdjustmentLine.getInvItemLocation().getIlGlCoaInventoryAccount());
                    } else {
                        glInventoryChartOfAccount = glChartOfAccountHome
                                .findByPrimaryKey(adBranchItemLocation.getBilCoaGlInventoryAccount());
                    }
                    // TODO: IF ADJUST BY IS NEGATIVE. INVENTORY CLASS MUST BE CREDIT
                    this.addInvDrEntry(invAdjustment.getInvDrNextLine(), "INVENTORY", DEBIT, Math.abs(AMOUNT),
                            EJBCommon.FALSE, glInventoryChartOfAccount.getCoaCode(), invAdjustment, branchCode, companyCode);

                    TOTAL_AMOUNT += AMOUNT;
                    ABS_TOTAL_AMOUNT += Math.abs(AMOUNT);

                    // add adjust quantity to item location committed quantity if negative
                    if (invAdjustmentLine.getAlAdjustQuantity() < 0) {
                        double convertedQuantity = this.convertByUomFromAndItemAndQuantity(
                                invAdjustmentLine.getInvUnitOfMeasure(),
                                invAdjustmentLine.getInvItemLocation().getInvItem(),
                                Math.abs(invAdjustmentLine.getAlAdjustQuantity()), companyCode);
                        invItemLocation = invAdjustmentLine.getInvItemLocation();
                        invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() + convertedQuantity);
                    }

                    try {
                        Iterator t = mdetails.getAlTagList().iterator();
                        LocalInvTag invTag;
                        if (invItem.getIiPartNumber().charAt(2) == '1') {
                            while (t.hasNext()) {
                                InvModTagListDetails tgLstDetails = (InvModTagListDetails) t.next();
                                if (tgLstDetails.getTgCode() == null) {
                                    invTag = invTagHome.create(tgLstDetails.getTgPropertyCode(),
                                            tgLstDetails.getTgSerialNumber(), null, tgLstDetails.getTgExpiryDate(),
                                            tgLstDetails.getTgSpecs(), companyCode, tgLstDetails.getTgTransactionDate(),
                                            tgLstDetails.getTgType());
                                    invTag.setInvAdjustmentLine(invAdjustmentLine);
                                    LocalAdUser adUser = null;
                                    try {
                                        adUser = adUserHome.findByUsrName(tgLstDetails.getTgCustodian(), companyCode);
                                    }
                                    catch (FinderException ex) {
                                        Debug.print("Finder Exception : " + ex.getMessage());
                                    }
                                    invTag.setAdUser(adUser);
                                }
                            }
                        }
                    }
                    catch (Exception ex) {
                        Debug.print("Exception : " + ex.getMessage());
                    }
                }

                // add variance or transfer/debit distribution
                DEBIT = TOTAL_AMOUNT > 0 ? EJBCommon.FALSE : EJBCommon.TRUE;
                this.addInvDrEntry(invAdjustment.getInvDrNextLine(), "ADJUSTMENT", DEBIT, Math.abs(TOTAL_AMOUNT),
                        EJBCommon.FALSE, invAdjustment.getGlChartOfAccount().getCoaCode(), invAdjustment, branchCode,
                        companyCode);

            } else {

                Iterator i = alList.iterator();
                while (i.hasNext()) {
                    InvModAdjustmentLineDetails mdetails = (InvModAdjustmentLineDetails) i.next();
                    LocalInvItemLocation invItemLocation;
                    try {
                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(mdetails.getAlLocName(),
                                mdetails.getAlIiName(), companyCode);
                    }
                    catch (FinderException ex) {
                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(mdetails.getAlLineNumber()));
                    }

                    // start date validation
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(
                                invAdjustment.getAdjDate(), invItemLocation.getInvItem().getIiName(),
                                invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }
                }

                Collection invAdjDistributionRecords = invAdjustment.getInvDistributionRecords();
                i = invAdjDistributionRecords.iterator();
                while (i.hasNext()) {
                    LocalInvDistributionRecord distributionRecord = (LocalInvDistributionRecord) i.next();
                    if (distributionRecord.getDrDebit() == 1) {
                        ABS_TOTAL_AMOUNT += distributionRecord.getDrAmount();
                    }
                }
            }

            // Insufficient Stocks
            if (adPreference.getPrfArCheckInsufficientStock() == EJBCommon.TRUE && !isDraft) {
                boolean hasInsufficientItems = false;
                StringBuilder insufficientItems = new StringBuilder();

                Collection invAdjustmentLines = invAdjustment.getInvAdjustmentLines();
                Iterator i = invAdjustmentLines.iterator();
                HashMap<String, Object> cstMap = new HashMap<>();
                while (i.hasNext()) {

                    LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();

                    if (invAdjustmentLine.getAlAdjustQuantity() < 0) {

                        LocalInvCosting invCosting = null;
                        double CURR_QTY = 0;
                        boolean isIlFound = false;

                        double ILI_QTY = this.convertByUomAndQuantity(invAdjustmentLine.getInvUnitOfMeasure(),
                                invAdjustmentLine.getInvItemLocation().getInvItem(),
                                Math.abs(invAdjustmentLine.getAlAdjustQuantity()), companyCode);

                        if (cstMap.containsKey(invAdjustmentLine.getInvItemLocation().getIlCode().toString())) {
                            isIlFound = true;
                            CURR_QTY = (Double) cstMap
                                    .get(invAdjustmentLine.getInvItemLocation().getIlCode().toString());
                        } else {

                            try {
                                invCosting = invCostingHome
                                        .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                                invAdjustment.getAdjDate(),
                                                invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(),
                                                invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName(),
                                                branchCode, companyCode);
                                CURR_QTY = invCosting.getCstRemainingQuantity();
                            }
                            catch (FinderException ex) {
                                Debug.print("Finder Exception : " + ex.getMessage());
                            }
                        }

                        if (invCosting != null) {
                            CURR_QTY = this.convertByUomAndQuantity(invAdjustmentLine.getInvUnitOfMeasure(),
                                    invAdjustmentLine.getInvItemLocation().getInvItem(),
                                    Math.abs(invCosting.getCstRemainingQuantity()), companyCode);
                        }

                        double LOWEST_QTY = this.convertByUomAndQuantity(invAdjustmentLine.getInvUnitOfMeasure(),
                                invAdjustmentLine.getInvItemLocation().getInvItem(), 1, companyCode);

                        if ((invCosting == null && !isIlFound) || CURR_QTY == 0 || CURR_QTY - ILI_QTY <= -LOWEST_QTY) {
                            hasInsufficientItems = true;
                            insufficientItems.append(invAdjustmentLine.getInvItemLocation().getInvItem().getIiName()).append(", ");
                        }

                        CURR_QTY -= ILI_QTY;
                        if (isIlFound) {
                            cstMap.remove(invAdjustmentLine.getInvItemLocation().getIlCode().toString());
                        }
                        cstMap.put(invAdjustmentLine.getInvItemLocation().getIlCode().toString(), CURR_QTY);
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

                // check if ar invoice approval is enabled
                if (adApproval.getAprEnableInvAdjustment() == EJBCommon.FALSE) {
                    INV_APPRVL_STATUS = "N/A";
                } else {
                    // check if invoice is self approved
                    LocalAdAmountLimit adAmountLimit;
                    try {
                        adAmountLimit = adAmountLimitHome.findByAdcTypeAndAuTypeAndUsrName("INV ADJUSTMENT",
                                "REQUESTER", details.getAdjLastModifiedBy(), companyCode);
                    }
                    catch (FinderException ex) {
                        throw new GlobalNoApprovalRequesterFoundException();
                    }

                    if (ABS_TOTAL_AMOUNT <= adAmountLimit.getCalAmountLimit()) {
                        Debug.print("ABS_TOTAL_AMOUNT=" + ABS_TOTAL_AMOUNT);
                        INV_APPRVL_STATUS = "N/A";

                    } else {

                        // for approval, create approval queue
                        Collection adAmountLimits = adAmountLimitHome.findByAdcTypeAndGreaterThanCalAmountLimit(
                                "INV ADJUSTMENT", adAmountLimit.getCalAmountLimit(), companyCode);

                        if (adAmountLimits.isEmpty()) {
                            Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER",
                                    adAmountLimit.getCalCode(), companyCode);

                            if (adApprovalUsers.isEmpty()) {
                                throw new GlobalNoApprovalApproverFoundException();
                            }

                            for (Object approvalUser : adApprovalUsers) {
                                LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;
                                LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(branchCode, companyCode,
                                        adApprovalQueueHome, invAdjustment, adAmountLimit, adApprovalUser);
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
                                        LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(branchCode, companyCode,
                                                adApprovalQueueHome, invAdjustment, adNextAmountLimit, adApprovalUser);
                                        adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                                    }
                                    break;

                                } else if (!i.hasNext()) {
                                    Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER",
                                            adNextAmountLimit.getCalCode(), companyCode);
                                    for (Object approvalUser : adApprovalUsers) {
                                        isApprovalUsersFound = true;
                                        LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;
                                        LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(branchCode, companyCode,
                                                adApprovalQueueHome, invAdjustment, adNextAmountLimit, adApprovalUser);
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

            if (INV_APPRVL_STATUS != null && INV_APPRVL_STATUS.equals("N/A")
                    && adPreference.getPrfInvGlPostingType().equals("AUTO-POST UPON APPROVAL")) {
                this.executeInvAdjPost(invAdjustment.getAdjCode(), invAdjustment.getAdjLastModifiedBy(), branchCode, companyCode);
            }

            // set adjustment approval status
            invAdjustment.setAdjApprovalStatus(INV_APPRVL_STATUS);
            return invAdjustment.getAdjCode();

        }
        catch (GlobalRecordAlreadyDeletedException | GlobalRecordInvalidException | GlobalMiscInfoIsRequiredException
               | GlobalExpiryDateNotFoundException | AdPRFCoaGlVarianceAccountNotFoundException |
               GlobalAccountNumberInvalidException
               | GlobalInventoryDateException | GlobalJournalNotBalanceException |
               GlJREffectiveDatePeriodClosedException
               | GlJREffectiveDateNoPeriodExistException | GlobalInvItemLocationNotFoundException |
               GlobalNoApprovalApproverFoundException
               | GlobalNoApprovalRequesterFoundException | GlobalTransactionAlreadyPostedException |
               GlobalTransactionAlreadyPendingException
               | GlobalTransactionAlreadyApprovedException | GlobalDocumentNumberNotUniqueException |
               GlobalBranchAccountNumberInvalidException ex) {
            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public String getInvPrfDefaultAdjustmentAccount(String ADJ_TYP, Integer companyCode, String USR_NM) {

        Debug.print("InvAdjustmentEntryControllerBean getInvPrfDefaultAdjustmentAccount");
        String COA_ACCNT_NMBR = "";
        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalGlChartOfAccount glChartOfAccount = null;

            switch (ADJ_TYP) {
                case "GENERAL":
                    glChartOfAccount = glChartOfAccountHome
                            .findByPrimaryKey(adPreference.getPrfInvGeneralAdjustmentAccount());
                    break;
                case "WASTAGE":
                    glChartOfAccount = glChartOfAccountHome
                            .findByPrimaryKey(adPreference.getPrfInvWastageAdjustmentAccount());
                    break;
                case "VARIANCE":
                    glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adPreference.getPrfInvGlCoaVarianceAccount());
                    break;
                case "ISSUANCE":
                    glChartOfAccount = glChartOfAccountHome
                            .findByPrimaryKey(adPreference.getPrfInvIssuanceAdjustmentAccount());
                    break;
                case "PRODUCTION":
                    glChartOfAccount = glChartOfAccountHome
                            .findByPrimaryKey(adPreference.getPrfInvProductionAdjustmentAccount());
                    break;
            }

            LocalAdUser adUser = adUserHome.findByUsrName(USR_NM, companyCode);
            LocalGlChartOfAccount glChartOfAccount2 = glChartOfAccountHome
                    .findByCoaAccountNumber(
                            glChartOfAccount.getCoaAccountNumber().substring(0,
                                    glChartOfAccount.getCoaAccountNumber().indexOf("-")) + "-" + adUser.getUsrDept(),
                            companyCode);

            // COA_ACCNT_NMBR = glChartOfAccount.getCoaAccountNumber();

            COA_ACCNT_NMBR = glChartOfAccount.getCoaAccountNumber().substring(0,
                    glChartOfAccount.getCoaAccountNumber().indexOf("-")) + "-" + adUser.getUsrDept();

        }
        catch (FinderException ex) {
        }
        return COA_ACCNT_NMBR;
    }

    @Override
    public Integer saveInvAdjEntry(
            InvAdjustmentDetails details, String COA_ACCOUNT_NUMBER, ArrayList alList,
            boolean isDraft, Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalRecordAlreadyDeletedException, GlobalBranchAccountNumberInvalidException,
            GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException,
            GlobalTransactionAlreadyPostedException, GlobalNoApprovalRequesterFoundException,
            GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException,
            GlobalDocumentNumberNotUniqueException {

        Debug.print("InvAdjustmentEntryControllerBean saveInvAdjEntry");

        try {

            LocalInvAdjustment invAdjustment = null;

            // validate if Adjustment is already deleted
            try {
                if (details.getAdjCode() != null) {
                    invAdjustment = invAdjustmentHome.findByPrimaryKey(details.getAdjCode());
                }
            }
            catch (FinderException ex) {
                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if adjustment is already posted, void, approved or pending
            if (details.getAdjCode() != null) {
                if (invAdjustment.getAdjApprovalStatus() != null) {
                    if (invAdjustment.getAdjApprovalStatus().equals("APPROVED") ||
                            invAdjustment.getAdjApprovalStatus().equals("N/A")) {
                        throw new GlobalTransactionAlreadyApprovedException();
                    } else if (invAdjustment.getAdjApprovalStatus().equals("PENDING")) {
                        throw new GlobalTransactionAlreadyPendingException();
                    }
                }
                if (invAdjustment.getAdjPosted() == EJBCommon.TRUE) {
                    throw new GlobalTransactionAlreadyPostedException();
                }
            }

            LocalInvAdjustment invExistingAdjustment = null;
            try {
                invExistingAdjustment = invAdjustmentHome
                        .findByAdjDocumentNumberAndBrCode(details.getAdjDocumentNumber(), AD_BRNCH, AD_CMPNY);
            }
            catch (FinderException ex) {

            }

            // 	validate if document number is unique document number is automatic then set next sequence
            if (details.getAdjCode() == null) {
                LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
                LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
                if (invExistingAdjustment != null) {
                    throw new GlobalDocumentNumberNotUniqueException();
                }

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

                if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' &&
                        (details.getAdjDocumentNumber() == null || details.getAdjDocumentNumber().trim().length() == 0)) {

                    while (true) {
                        if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {
                            try {
                                invAdjustmentHome.findByAdjDocumentNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, AD_CMPNY);
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                            }
                            catch (FinderException ex) {
                                details.setAdjDocumentNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                break;
                            }

                        } else {

                            try {
                                invAdjustmentHome.findByAdjDocumentNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, AD_CMPNY);
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                            }
                            catch (FinderException ex) {
                                details.setAdjDocumentNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                break;
                            }
                        }
                    }
                }

            } else {

                if (invExistingAdjustment != null &&
                        !invExistingAdjustment.getAdjCode().equals(details.getAdjCode())) {
                    throw new GlobalDocumentNumberNotUniqueException();
                }

                assert invAdjustment != null;
                if (!Objects.equals(invAdjustment.getAdjDocumentNumber(), details.getAdjDocumentNumber()) &&
                        (details.getAdjDocumentNumber() == null || details.getAdjDocumentNumber().trim().length() == 0)) {
                    details.setAdjDocumentNumber(invAdjustment.getAdjDocumentNumber());
                }
            }

            // used in checking if invoice should re-generate distribution records and re-calculate taxes
            boolean isRecalculate = true;

            // create adjustment
            if (details.getAdjCode() == null) {

                invAdjustment = invAdjustmentHome.create(details.getAdjDocumentNumber(), details.getAdjReferenceNumber(), details.getAdjDescription(),
                        details.getAdjDate(), details.getAdjType(), details.getAdjApprovalStatus(),
                        EJBCommon.FALSE, details.getAdjCreatedBy(), details.getAdjDateCreated(),
                        details.getAdjLastModifiedBy(), details.getAdjDateLastModified(),
                        null, null, null, null,
                        null, null, EJBCommon.FALSE, EJBCommon.FALSE, AD_BRNCH, AD_CMPNY);

            } else {

                // check if critical fields are changed
                assert invAdjustment != null;
                if (!invAdjustment.getGlChartOfAccount().getCoaAccountNumber().equals(COA_ACCOUNT_NUMBER) ||
                        alList.size() != invAdjustment.getInvAdjustmentLines().size() ||
                        !(invAdjustment.getAdjDate().equals(details.getAdjDate()))) {
                    isRecalculate = true;

                } else if (alList.size() == invAdjustment.getInvAdjustmentLines().size()) {

                    Iterator alIter = invAdjustment.getInvAdjustmentLines().iterator();
                    Iterator alListIter = alList.iterator();

                    while (alIter.hasNext()) {
                        LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) alIter.next();
                        InvModAdjustmentLineDetails mdetails = (InvModAdjustmentLineDetails) alListIter.next();

                        if (!invAdjustmentLine.getInvItemLocation().getInvItem().getIiName().equals(mdetails.getAlIiName()) ||
                                !invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName().equals(mdetails.getAlLocName()) ||
                                !invAdjustmentLine.getInvUnitOfMeasure().getUomName().equals(mdetails.getAlUomName()) ||
                                invAdjustmentLine.getAlAdjustQuantity() != mdetails.getAlAdjustQuantity() ||
                                invAdjustmentLine.getAlUnitCost() != mdetails.getAlUnitCost()) {
                            isRecalculate = true;
                            break;

                        }
                        isRecalculate = false;
                    }
                } else {
                    isRecalculate = false;
                }

                invAdjustment.setAdjDocumentNumber(details.getAdjDocumentNumber());
                invAdjustment.setAdjReferenceNumber(details.getAdjReferenceNumber());
                invAdjustment.setAdjDescription(details.getAdjDescription());
                invAdjustment.setAdjDate(details.getAdjDate());
                invAdjustment.setAdjType(details.getAdjType());
                invAdjustment.setAdjApprovalStatus(details.getAdjApprovalStatus());
                invAdjustment.setAdjLastModifiedBy(details.getAdjLastModifiedBy());
                invAdjustment.setAdjDateLastModified(details.getAdjDateLastModified());
                invAdjustment.setAdjReasonForRejection(null);
            }

            try {
                LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(COA_ACCOUNT_NUMBER, AD_BRNCH, AD_CMPNY);
                invAdjustment.setGlChartOfAccount(glChartOfAccount);
            }
            catch (FinderException ex) {
                throw new GlobalAccountNumberInvalidException();
            }

            double ABS_TOTAL_AMOUNT = 0d;
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            if (isRecalculate) {
                // remove all adjustment lines
                Collection invAdjustmentLines = invAdjustment.getInvAdjustmentLines();
                Iterator i = invAdjustmentLines.iterator();
                while (i.hasNext()) {
                    LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();
                    if (invAdjustmentLine.getAlAdjustQuantity() < 0) {
                        double convertedQuantity = this.convertByUomFromAndItemAndQuantity(invAdjustmentLine.getInvUnitOfMeasure(),
                                invAdjustmentLine.getInvItemLocation().getInvItem(), Math.abs(invAdjustmentLine.getAlAdjustQuantity()), AD_CMPNY);
                        invAdjustmentLine.getInvItemLocation().setIlCommittedQuantity(invAdjustmentLine.getInvItemLocation().getIlCommittedQuantity() - convertedQuantity);
                    }
                    i.remove();
                    em.remove(invAdjustmentLine);
                }

                // remove all distribution records
                Collection arDistributionRecords = invAdjustment.getInvDistributionRecords();
                i = arDistributionRecords.iterator();
                while (i.hasNext()) {
                    LocalInvDistributionRecord arDistributionRecord = (LocalInvDistributionRecord) i.next();
                    i.remove();
                    em.remove(arDistributionRecord);
                }

                // add new adjustment lines and distribution record
                double TOTAL_AMOUNT = 0d;
                byte DEBIT;
                i = alList.iterator();
                while (i.hasNext()) {
                    InvModAdjustmentLineDetails mdetails = (InvModAdjustmentLineDetails) i.next();
                    LocalInvItemLocation invItemLocation;
                    try {
                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(mdetails.getAlLocName(), mdetails.getAlIiName(), AD_CMPNY);
                    }
                    catch (FinderException ex) {
                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(mdetails.getAlLineNumber()));
                    }

                    //	start date validation
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(
                                invAdjustment.getAdjDate(), invItemLocation.getInvItem().getIiName(),
                                invItemLocation.getInvLocation().getLocName(), AD_BRNCH, AD_CMPNY);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }

                    LocalInvAdjustmentLine invAdjustmentLine = this.addInvAlEntry(mdetails, invAdjustment, EJBCommon.FALSE, AD_CMPNY);

                    // add physical inventory distribution
                    double AMOUNT;
                    if (invAdjustmentLine.getAlAdjustQuantity() > 0) {
                        AMOUNT = EJBCommon.roundIt(
                                invAdjustmentLine.getAlAdjustQuantity() * invAdjustmentLine.getAlUnitCost(),
                                this.getGlFcPrecisionUnit(AD_CMPNY));
                        DEBIT = EJBCommon.TRUE;
                    } else {
                        double COST;
                        try {
                            if (invAdjustmentLine.getAlUnitCost() == 0 || invAdjustmentLine.getAlUnitCost() < 0) {
                                LocalInvCosting invCosting = invCostingHome
                                        .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                                details.getAdjDate(), invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(),
                                        invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName(), AD_BRNCH, AD_CMPNY);
                                COST = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                            } else {
                                COST = invAdjustmentLine.getAlUnitCost();
                            }
                        }
                        catch (FinderException ex) {
                            COST = invAdjustmentLine.getInvItemLocation().getInvItem().getIiUnitCost();
                        }

                        COST = this.convertCostByUom(invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(),
                                invAdjustmentLine.getInvUnitOfMeasure().getUomName(),
                                COST, true, AD_CMPNY);
                        invAdjustmentLine.setAlUnitCost(COST);

                        AMOUNT = EJBCommon.roundIt(
                                invAdjustmentLine.getAlAdjustQuantity() * COST,
                                this.getGlFcPrecisionUnit(AD_CMPNY));
                        DEBIT = EJBCommon.FALSE;

                    }

                    // check for branch mapping
                    LocalAdBranchItemLocation adBranchItemLocation = null;
                    try {
                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(
                                invAdjustmentLine.getInvItemLocation().getIlCode(), AD_BRNCH, AD_CMPNY);
                    }
                    catch (FinderException ex) {

                    }

                    LocalGlChartOfAccount glInventoryChartOfAccount;
                    if (adBranchItemLocation == null) {

                        glInventoryChartOfAccount = glChartOfAccountHome.findByPrimaryKey(
                                invAdjustmentLine.getInvItemLocation().getIlGlCoaInventoryAccount());
                    } else {

                        glInventoryChartOfAccount = glChartOfAccountHome.findByPrimaryKey(
                                adBranchItemLocation.getBilCoaGlInventoryAccount());

                    }

                    this.addInvDrEntry(invAdjustment.getInvDrNextLine(), "INVENTORY", DEBIT, Math.abs(AMOUNT), EJBCommon.FALSE,
                            glInventoryChartOfAccount.getCoaCode(), invAdjustment, AD_BRNCH, AD_CMPNY);

                    TOTAL_AMOUNT += AMOUNT;
                    ABS_TOTAL_AMOUNT += Math.abs(AMOUNT);

                    // add adjust quantity to item location committed quantity if negative
                    if (invAdjustmentLine.getAlAdjustQuantity() < 0) {
                        double convertedQuantity = this.convertByUomFromAndItemAndQuantity(
                                invAdjustmentLine.getInvUnitOfMeasure(), invAdjustmentLine.getInvItemLocation().getInvItem(),
                                Math.abs(invAdjustmentLine.getAlAdjustQuantity()), AD_CMPNY);
                        invItemLocation = invAdjustmentLine.getInvItemLocation();
                        invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() + convertedQuantity);
                    }
                }

                // add variance or transfer/debit distribution
                DEBIT = TOTAL_AMOUNT > 0 ? EJBCommon.FALSE : EJBCommon.TRUE;
                this.addInvDrEntry(invAdjustment.getInvDrNextLine(), "ADJUSTMENT", DEBIT, Math.abs(TOTAL_AMOUNT), EJBCommon.FALSE,
                        invAdjustment.getGlChartOfAccount().getCoaCode(), invAdjustment, AD_BRNCH, AD_CMPNY);

            } else {

                Iterator i = alList.iterator();
                while (i.hasNext()) {
                    InvModAdjustmentLineDetails mdetails = (InvModAdjustmentLineDetails) i.next();
                    LocalInvItemLocation invItemLocation;
                    try {
                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(mdetails.getAlLocName(), mdetails.getAlIiName(), AD_CMPNY);
                    }
                    catch (FinderException ex) {
                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(mdetails.getAlLineNumber()));
                    }

                    //	start date validation
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(
                                invAdjustment.getAdjDate(), invItemLocation.getInvItem().getIiName(),
                                invItemLocation.getInvLocation().getLocName(), AD_BRNCH, AD_CMPNY);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }

                }

                Collection invAdjDistributionRecords = invAdjustment.getInvDistributionRecords();
                i = invAdjDistributionRecords.iterator();
                while (i.hasNext()) {
                    LocalInvDistributionRecord distributionRecord = (LocalInvDistributionRecord) i.next();
                    if (distributionRecord.getDrDebit() == 1) {
                        ABS_TOTAL_AMOUNT += distributionRecord.getDrAmount();
                    }
                }
            }

            // generate approval status
            String INV_APPRVL_STATUS = null;

            if (!isDraft) {

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(AD_CMPNY);

                // check if ar invoice approval is enabled
                if (adApproval.getAprEnableInvAdjustment() == EJBCommon.FALSE) {
                    INV_APPRVL_STATUS = "N/A";
                } else {
                    // check if invoice is self approved
                    LocalAdAmountLimit adAmountLimit;
                    try {
                        adAmountLimit = adAmountLimitHome.findByAdcTypeAndAuTypeAndUsrName(
                                "INV ADJUSTMENT", "REQUESTER", details.getAdjLastModifiedBy(), AD_CMPNY);
                    }
                    catch (FinderException ex) {
                        throw new GlobalNoApprovalRequesterFoundException();
                    }

                    if (ABS_TOTAL_AMOUNT <= adAmountLimit.getCalAmountLimit()) {
                        INV_APPRVL_STATUS = "N/A";
                    } else {

                        // for approval, create approval queue
                        Collection adAmountLimits = adAmountLimitHome
                                .findByAdcTypeAndGreaterThanCalAmountLimit("INV ADJUSTMENT", adAmountLimit.getCalAmountLimit(), AD_CMPNY);

                        if (adAmountLimits.isEmpty()) {
                            Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adAmountLimit.getCalCode(), AD_CMPNY);
                            if (adApprovalUsers.isEmpty()) {
                                throw new GlobalNoApprovalApproverFoundException();
                            }
                            for (Object approvalUser : adApprovalUsers) {
                                LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;
                                LocalAdApprovalQueue adApprovalQueue = adApprovalQueueHome.create(EJBCommon.TRUE, "INV ADJUSTMENT",
                                        invAdjustment.getAdjCode(), invAdjustment.getAdjDocumentNumber(), invAdjustment.getAdjDate(),
                                        adAmountLimit.getCalAndOr(), adApprovalUser.getAuOr(), AD_BRNCH, AD_CMPNY);
                                adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                            }

                        } else {

                            boolean isApprovalUsersFound = false;
                            Iterator i = adAmountLimits.iterator();
                            while (i.hasNext()) {
                                LocalAdAmountLimit adNextAmountLimit = (LocalAdAmountLimit) i.next();
                                if (ABS_TOTAL_AMOUNT <= adNextAmountLimit.getCalAmountLimit()) {
                                    Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER",
                                            adAmountLimit.getCalCode(), AD_CMPNY);
                                    for (Object approvalUser : adApprovalUsers) {
                                        isApprovalUsersFound = true;
                                        LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;
                                        LocalAdApprovalQueue adApprovalQueue = adApprovalQueueHome.create(EJBCommon.TRUE, "INV ADJUSTMENT",
                                                invAdjustment.getAdjCode(), invAdjustment.getAdjDocumentNumber(), invAdjustment.getAdjDate(),
                                                adAmountLimit.getCalAndOr(), adApprovalUser.getAuOr(), AD_BRNCH, AD_CMPNY);
                                        adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                                    }
                                    break;
                                } else if (!i.hasNext()) {

                                    Collection adApprovalUsers =
                                            adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adNextAmountLimit.getCalCode(), AD_CMPNY);
                                    for (Object approvalUser : adApprovalUsers) {
                                        isApprovalUsersFound = true;
                                        LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;
                                        LocalAdApprovalQueue adApprovalQueue = adApprovalQueueHome.create(EJBCommon.TRUE, "INV ADJUSTMENT",
                                                invAdjustment.getAdjCode(), invAdjustment.getAdjDocumentNumber(), invAdjustment.getAdjDate(),
                                                adNextAmountLimit.getCalAndOr(), adApprovalUser.getAuOr(), AD_BRNCH, AD_CMPNY);
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

            if (INV_APPRVL_STATUS != null && INV_APPRVL_STATUS.equals("N/A") && adPreference.getPrfInvGlPostingType().equals("AUTO-POST UPON APPROVAL")) {
                //this.executeInvAdjPost(invAdjustment.getAdjCode(), invAdjustment.getAdjLastModifiedBy(), AD_BRNCH, AD_CMPNY);
            }

            // set adjustment approval status
            invAdjustment.setAdjApprovalStatus(INV_APPRVL_STATUS);
            return invAdjustment.getAdjCode();

        }
        catch (GlobalRecordAlreadyDeletedException | GlobalBranchAccountNumberInvalidException |
               GlobalDocumentNumberNotUniqueException | GlobalTransactionAlreadyApprovedException |
               GlobalTransactionAlreadyPendingException | GlobalTransactionAlreadyPostedException |
               GlobalNoApprovalRequesterFoundException | GlobalNoApprovalApproverFoundException |
               GlobalInvItemLocationNotFoundException ex) {
            ctx.setRollbackOnly();
            throw ex;
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer saveInvAdjEntry(
            InvAdjustmentDetails details, String COA_ACCOUNT_NUMBER, String SPL_SPPLR_CODE, ArrayList alList,
            boolean isDraft, Integer branchCode, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException, GlobalBranchAccountNumberInvalidException,
            GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException,
            GlobalTransactionAlreadyPostedException, GlobalNoApprovalRequesterFoundException,
            GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException,
            GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException,
            GlobalJournalNotBalanceException, GlobalDocumentNumberNotUniqueException, GlobalInventoryDateException,
            GlobalInvTagMissingException, InvTagSerialNumberNotFoundException, GlobalInvTagExistingException,
            GlobalAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException,
            GlobalExpiryDateNotFoundException, GlobalMiscInfoIsRequiredException, GlobalRecordInvalidException {

        Debug.print("InvAdjustmentEntryControllerBean saveInvAdjEntry");
        try {

            LocalInvAdjustment invAdjustment = null;

            // validate if Adjustment is already deleted

            try {

                if (details.getAdjCode() != null) {

                    invAdjustment = invAdjustmentHome.findByPrimaryKey(details.getAdjCode());
                }

            }
            catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if adjustment is already posted, void, approved or pending

            if (details.getAdjCode() != null && details.getAdjVoid() == EJBCommon.FALSE) {

                Debug.print("posted with void = false");
                if (invAdjustment.getAdjApprovalStatus() != null) {

                    if (invAdjustment.getAdjApprovalStatus().equals("APPROVED")
                            || invAdjustment.getAdjApprovalStatus().equals("N/A")) {

                        throw new GlobalTransactionAlreadyApprovedException();

                    } else if (invAdjustment.getAdjApprovalStatus().equals("PENDING")) {

                        throw new GlobalTransactionAlreadyPendingException();
                    }
                }

                if (invAdjustment.getAdjPosted() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyPostedException();
                }
            }

            // validate if receipt is already posted, void, approved or pending

            if (details.getAdjCode() != null && details.getAdjVoid() == EJBCommon.FALSE) {

                Debug.print("posted with void = true");
                if (invAdjustment.getAdjApprovalStatus() != null) {

                    if (invAdjustment.getAdjApprovalStatus().equals("APPROVED")
                            || invAdjustment.getAdjApprovalStatus().equals("N/A")) {

                        throw new GlobalTransactionAlreadyApprovedException();

                    } else if (invAdjustment.getAdjApprovalStatus().equals("PENDING")) {

                        throw new GlobalTransactionAlreadyPendingException();
                    }
                }

                if (invAdjustment.getAdjPosted() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyPostedException();

                } else if (invAdjustment.getAdjVoid() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyVoidException();
                }
            }

            if (details.getAdjCode() != null && details.getAdjVoid() == EJBCommon.TRUE) {

                if (invAdjustment.getAdjVoid() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyVoidException();
                }

                if (invAdjustment.getAdjPosted() == EJBCommon.TRUE) {
                    // generate approval status

                    String RCT_APPRVL_STATUS = null;

                    this.executeVoidInvAdjustment(invAdjustment, branchCode, companyCode);
                }

                invAdjustment.setAdjVoid(EJBCommon.TRUE);
                invAdjustment.setAdjLastModifiedBy(details.getAdjLastModifiedBy());
                invAdjustment.setAdjDateLastModified(details.getAdjDateLastModified());

                return invAdjustment.getAdjCode();
            }

            LocalInvAdjustment invExistingAdjustment = null;

            try {

                invExistingAdjustment = invAdjustmentHome
                        .findByAdjDocumentNumberAndBrCode(details.getAdjDocumentNumber(), branchCode, companyCode);

            }
            catch (FinderException ex) {

            }

            // validate if document number is unique document number is automatic then set
            // next sequence

            if (details.getAdjCode() == null) {

                LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
                LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

                if (invExistingAdjustment != null) {

                    throw new GlobalDocumentNumberNotUniqueException();
                }

                try {

                    adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("INV ADJUSTMENT",
                            companyCode);

                }
                catch (FinderException ex) {

                }

                try {

                    adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome
                            .findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode);

                }
                catch (FinderException ex) {

                }

                if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A'
                        && (details.getAdjDocumentNumber() == null
                        || details.getAdjDocumentNumber().trim().length() == 0)) {

                    while (true) {

                        if (adBranchDocumentSequenceAssignment == null
                                || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                            try {

                                invAdjustmentHome.findByAdjDocumentNumberAndBrCode(
                                        adDocumentSequenceAssignment.getDsaNextSequence(), branchCode, companyCode);
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon
                                        .incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                            }
                            catch (FinderException ex) {

                                details.setAdjDocumentNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon
                                        .incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                break;
                            }

                        } else {

                            try {

                                invAdjustmentHome.findByAdjDocumentNumberAndBrCode(
                                        adBranchDocumentSequenceAssignment.getBdsNextSequence(), branchCode, companyCode);
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(
                                        adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                            }
                            catch (FinderException ex) {

                                details.setAdjDocumentNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(
                                        adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                break;
                            }
                        }
                    }
                }

            } else {

                if (invExistingAdjustment != null && !invExistingAdjustment.getAdjCode().equals(details.getAdjCode())) {

                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (invAdjustment.getAdjDocumentNumber() != details.getAdjDocumentNumber()
                        && (details.getAdjDocumentNumber() == null
                        || details.getAdjDocumentNumber().trim().length() == 0)) {

                    details.setAdjDocumentNumber(invAdjustment.getAdjDocumentNumber());
                }
            }

            // used in checking if invoice should re-generate distribution records and
            // re-calculate taxes
            boolean isRecalculate = true;

            // create adjustment

            if (details.getAdjCode() == null) {

                invAdjustment = invAdjustmentHome.create(details.getAdjDocumentNumber(),
                        details.getAdjReferenceNumber(), details.getAdjDescription(), details.getAdjDate(),
                        details.getAdjType(), details.getAdjApprovalStatus(), EJBCommon.FALSE,
                        details.getAdjCreatedBy(), details.getAdjDateCreated(), details.getAdjLastModifiedBy(),
                        details.getAdjDateLastModified(), null, null, null, null, details.getAdjNotedBy(), null,
                        EJBCommon.FALSE, EJBCommon.FALSE, branchCode, companyCode);

            } else {

                // check if critical fields are changed

                if (!invAdjustment.getGlChartOfAccount().getCoaAccountNumber().equals(COA_ACCOUNT_NUMBER)
                        || alList.size() != invAdjustment.getInvAdjustmentLines().size()
                        || !(invAdjustment.getAdjDate().equals(details.getAdjDate()))) {

                    isRecalculate = true;

                } else if (alList.size() == invAdjustment.getInvAdjustmentLines().size()) {

                    Iterator alIter = invAdjustment.getInvAdjustmentLines().iterator();
                    Iterator alListIter = alList.iterator();

                    while (alIter.hasNext()) {

                        LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) alIter.next();
                        InvModAdjustmentLineDetails mdetails = (InvModAdjustmentLineDetails) alListIter.next();

                        if (!invAdjustmentLine.getInvItemLocation().getInvItem().getIiName()
                                .equals(mdetails.getAlIiName())
                                || !invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName()
                                .equals(mdetails.getAlLocName())
                                || !invAdjustmentLine.getInvUnitOfMeasure().getUomName().equals(mdetails.getAlUomName())
                                || invAdjustmentLine.getAlAdjustQuantity() != mdetails.getAlAdjustQuantity()
                                || invAdjustmentLine.getAlUnitCost() != mdetails.getAlUnitCost()
                                || invAdjustmentLine.getAlQcNumber() != mdetails.getAlQcNumber()
                                || invAdjustmentLine.getAlQcExpiryDate() != mdetails.getAlQcExpiryDate()
                                || invAdjustmentLine.getAlMisc() != mdetails.getAlMisc()) {

                            isRecalculate = true;
                            break;
                        }

                        // isRecalculate = false;

                    }

                } else {

                    // isRecalculate = false;

                }

                invAdjustment.setAdjDocumentNumber(details.getAdjDocumentNumber());
                invAdjustment.setAdjReferenceNumber(details.getAdjReferenceNumber());
                invAdjustment.setAdjDescription(details.getAdjDescription());
                invAdjustment.setAdjDate(details.getAdjDate());
                invAdjustment.setAdjType(details.getAdjType());
                invAdjustment.setAdjApprovalStatus(details.getAdjApprovalStatus());
                invAdjustment.setAdjLastModifiedBy(details.getAdjLastModifiedBy());
                invAdjustment.setAdjDateLastModified(details.getAdjDateLastModified());
                invAdjustment.setAdjReasonForRejection(null);
                Debug.print("y");
                invAdjustment.setAdjNotedBy(details.getAdjNotedBy());
            }

            try {

                LocalApSupplier apSupplier = apSupplierHome.findBySplSupplierCode(SPL_SPPLR_CODE, companyCode);
                invAdjustment.setApSupplier(apSupplier);

            }
            catch (Exception ex) {

            }

            if (COA_ACCOUNT_NUMBER != null && COA_ACCOUNT_NUMBER.equals("UPLOAD")) {
                COA_ACCOUNT_NUMBER = getInvPrfDefaultAdjustmentAccount(details.getAdjType(), companyCode,
                        details.getAdjCreatedBy());
            }

            try {

                LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome
                        .findByCoaAccountNumberAndBranchCode(COA_ACCOUNT_NUMBER, branchCode, companyCode);
                // glChartOfAccount.addInvAdjustment(invAdjustment);
                invAdjustment.setGlChartOfAccount(glChartOfAccount);

            }
            catch (FinderException ex) {

                throw new GlobalAccountNumberInvalidException();
            }

            double ABS_TOTAL_AMOUNT = 0d;

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            if (isRecalculate) {

                // remove all adjustment lines

                Collection invAdjustmentLines = invAdjustment.getInvAdjustmentLines();

                Iterator i = invAdjustmentLines.iterator();

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
                    // remove all inv tag inside adjustment line
                    Collection invTags = invAdjustmentLine.getInvTags();

                    Iterator x = invTags.iterator();

                    while (x.hasNext()) {

                        LocalInvTag invTag = (LocalInvTag) x.next();

                        x.remove();

                        // invTag.entityRemove();
                        em.remove(invTag);
                    }
                    i.remove();

                    // invAdjustmentLine.entityRemove();
                    em.remove(invAdjustmentLine);
                }

                // remove all distribution records

                Collection arDistributionRecords = invAdjustment.getInvDistributionRecords();

                i = arDistributionRecords.iterator();

                while (i.hasNext()) {

                    LocalInvDistributionRecord arDistributionRecord = (LocalInvDistributionRecord) i.next();

                    i.remove();

                    // arDistributionRecord.entityRemove();
                    em.remove(arDistributionRecord);
                }

                // add new adjustment lines and distribution record

                double TOTAL_AMOUNT = 0d;

                byte DEBIT = 0;

                i = alList.iterator();

                while (i.hasNext()) {

                    InvModAdjustmentLineDetails mdetails = (InvModAdjustmentLineDetails) i.next();

                    LocalInvItemLocation invItemLocation = null;

                    try {
                        if (mdetails.getAlLocName().equals("UPLOAD")) {
                            mdetails.setAlLocName(invLocationHome.findByPrimaryKey(
                                            invItemHome.findByIiName(mdetails.getAlIiName(), companyCode).getIiDefaultLocation())
                                    .getLocName());
                        }
                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(mdetails.getAlLocName(),
                                mdetails.getAlIiName(), companyCode);

                    }
                    catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(mdetails.getAlLineNumber()));
                    }

                    if (mdetails.getAlUnitCost() == 0) {
                        mdetails.setAlUnitCost(invItemLocation.getInvItem().getIiUnitCost());
                    }

                    // start date validation
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(
                                invAdjustment.getAdjDate(), invItemLocation.getInvItem().getIiName(),
                                invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }

                    LocalInvAdjustmentLine invAdjustmentLine = this.addInvAlEntry(mdetails, invAdjustment,
                            EJBCommon.FALSE, companyCode);

                    // add physical inventory distribution

                    double AMOUNT = 0d;

                    if (invAdjustmentLine.getAlAdjustQuantity() > 0
                            && !invAdjustmentLine.getInvAdjustment().getAdjType().equals("ISSUANCE")) {

                        AMOUNT = EJBCommon.roundIt(
                                invAdjustmentLine.getAlAdjustQuantity() * invAdjustmentLine.getAlUnitCost(),
                                this.getGlFcPrecisionUnit(companyCode));
                        DEBIT = EJBCommon.TRUE;

                    } else {

                        double COST = invItemLocation.getInvItem().getIiUnitCost();

                        try {

                            LocalInvCosting invCosting = invCostingHome
                                    .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                            details.getAdjDate(),
                                            invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(),
                                            invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName(),
                                            branchCode, companyCode);

                            if (invCosting.getCstRemainingQuantity() <= 0 && invCosting.getCstRemainingValue() <= 0) {
                                Debug.print("RE CALC");
                                HashMap criteria = new HashMap();
                                criteria.put("itemName", invItemLocation.getInvItem().getIiName());
                                criteria.put("location", invItemLocation.getInvLocation().getLocName());

                                ArrayList branchList = new ArrayList();

                                AdBranchDetails mdetailsb = new AdBranchDetails();
                                mdetailsb.setBrCode(branchCode);
                                branchList.add(mdetailsb);

                                ejbRIC.executeInvFixItemCosting(criteria, branchList, companyCode);

                                invCosting = invCostingHome
                                        .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                                details.getAdjDate(),
                                                invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(),
                                                invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName(),
                                                branchCode, companyCode);
                            }

                            // test if AVERAGE or FIFO
                            switch (invCosting.getInvItemLocation().getInvItem().getIiCostMethod()) {
                                case "Average":

                                    if (invCosting.getCstRemainingQuantity() <= 0) {
                                        COST = invItemLocation.getInvItem().getIiUnitCost();

                                    } else {
                                        COST = invCosting.getCstRemainingQuantity() <= 0 ? COST
                                                : Math.abs(invCosting.getCstRemainingValue()
                                                / invCosting.getCstRemainingQuantity());

                                        if (COST <= 0) {
                                            COST = invItemLocation.getInvItem().getIiUnitCost();
                                        }
                                    }
                                    break;
                                case "FIFO":
                                    // COST = invAdjustmentLine.getAlUnitCost();
                                    COST = this.getInvFifoCost(invAdjustment.getAdjDate(), invItemLocation.getIlCode(),
                                            invAdjustmentLine.getAlAdjustQuantity(), invCosting.getCstAdjustCost(), false,
                                            branchCode, companyCode);
                                    break;
                                case "Standard":
                                    COST = invItemLocation.getInvItem().getIiUnitCost();
                                    break;
                            }

                        }
                        catch (FinderException ex) {

                            COST = invAdjustmentLine.getInvItemLocation().getInvItem().getIiUnitCost();
                        }

                        COST = this.convertCostByUom(invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(),
                                invAdjustmentLine.getInvUnitOfMeasure().getUomName(), COST, true, companyCode);

                        invAdjustmentLine.setAlUnitCost(Math.abs(COST));

                        AMOUNT = EJBCommon.roundIt(invAdjustmentLine.getAlAdjustQuantity() * COST,
                                adPreference.getPrfInvCostPrecisionUnit());
                        // AMOUNT = invAdjustmentLine.getAlAdjustQuantity() * COST;
                        Debug.print("AMOUNT>>: " + AMOUNT);
                        DEBIT = EJBCommon.FALSE;
                    }

                    // check for branch mapping

                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {

                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(
                                invAdjustmentLine.getInvItemLocation().getIlCode(), branchCode, companyCode);

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
                    ABS_TOTAL_AMOUNT += Math.abs(AMOUNT);

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

                    // TODO: add new inv Tag

                    if (mdetails.getTraceMisc() == 1 && !details.getAdjType().equals("VARIANCE")) {

                        if (invItemLocation.getInvItem().getIiNonInventoriable() == 0 && mdetails.getAlTagList() != null
                                && mdetails.getAlTagList().size() < mdetails.getAlAdjustQuantity()) {

                            throw new GlobalInvTagMissingException(invItemLocation.getInvItem().getIiName());
                        }
                        try {
                            // Iterator t = apPurchaseOrderLine.getInvTag().iterator();
                            Iterator t = mdetails.getAlTagList().iterator();
                            LocalInvItem invItem = invItemHome.findByIiName(mdetails.getAlIiName(), companyCode);
                            LocalInvTag invTag = null;
                            while (t.hasNext()) {
                                InvModTagListDetails tgLstDetails = (InvModTagListDetails) t.next();

                                if (invItemLocation.getInvItem().getIiNonInventoriable() == 0
                                        && tgLstDetails.getTgCustodian().equals("")
                                        && tgLstDetails.getTgSpecs().equals("")
                                        && tgLstDetails.getTgPropertyCode().equals("")
                                        && tgLstDetails.getTgExpiryDate() == null
                                        && tgLstDetails.getTgSerialNumber().equals("")) {
                                    throw new GlobalInvTagMissingException(
                                            invItemLocation.getInvItem().getIiDescription());
                                }
                                if (tgLstDetails.getTgCode() == null) {

                                    if (details.getAdjType().equals("ISSUANCE")) {
                                        try {
                                            Collection invTg = invTagHome
                                                    .findAllInByTgSerialNumberAndAdBranchAndAdCompany(
                                                            tgLstDetails.getTgSerialNumber(), branchCode, companyCode);
                                            if (invTg.isEmpty()) {
                                                throw new InvTagSerialNumberNotFoundException(
                                                        tgLstDetails.getTgSerialNumber());
                                            } else {
                                                try {
                                                    invTg = invTagHome
                                                            .findAllOutByTgSerialNumberAndAdBranchAndAdCompany(
                                                                    tgLstDetails.getTgSerialNumber(), branchCode,
                                                                    companyCode);
                                                    if (!invTg.isEmpty()) {
                                                        throw new InvTagSerialNumberNotFoundException(
                                                                tgLstDetails.getTgSerialNumber());
                                                    }
                                                }
                                                catch (FinderException ex) {
                                                }
                                            }
                                        }
                                        catch (FinderException ex) {
                                        }
                                    }
                                    invTag = invTagHome.create(tgLstDetails.getTgPropertyCode(),
                                            tgLstDetails.getTgSerialNumber(), null, tgLstDetails.getTgExpiryDate(),
                                            tgLstDetails.getTgSpecs(), companyCode, tgLstDetails.getTgTransactionDate(),
                                            tgLstDetails.getTgType());

                                    invTag.setInvAdjustmentLine(invAdjustmentLine);
                                    LocalAdUser adUser = null;
                                    try {
                                        adUser = adUserHome.findByUsrName(tgLstDetails.getTgCustodian(), companyCode);
                                    }
                                    catch (FinderException ex) {

                                    }
                                    if (tgLstDetails.getTgDocumentNumber().equals("")) {
                                        if (invItemLocation.getInvItem().getIiNonInventoriable() == 1) {
                                            invTag.setTgDocumentNumber(adPreference.getPrfInvNextCustodianNumber2());
                                            adPreference.setPrfInvNextCustodianNumber2(EJBCommon.incrementStringNumber(
                                                    adPreference.getPrfInvNextCustodianNumber2()));
                                        } else {
                                            invTag.setTgDocumentNumber(adPreference.getPrfInvNextCustodianNumber1());
                                            adPreference.setPrfInvNextCustodianNumber1(EJBCommon.incrementStringNumber(
                                                    adPreference.getPrfInvNextCustodianNumber1()));
                                        }
                                    } else {
                                        invTag.setTgDocumentNumber(tgLstDetails.getTgDocumentNumber());
                                    }
                                    invTag.setAdUser(adUser);
                                }
                            }

                        }
                        catch (InvTagSerialNumberNotFoundException ex) {
                            throw new InvTagSerialNumberNotFoundException(ex.getMessage());
                        }
                        catch (Exception ex) {
                            if (invItemLocation.getInvItem().getIiNonInventoriable() == 0) {
                                throw new GlobalInvTagMissingException(invItemLocation.getInvItem().getIiName());
                            }
                        }
                    }
                }

                // add variance or transfer/debit distribution

                DEBIT = (TOTAL_AMOUNT >= 0 && !invAdjustment.getAdjType().equals("ISSUANCE")) ? EJBCommon.FALSE
                        : EJBCommon.TRUE;

                this.addInvDrEntry(invAdjustment.getInvDrNextLine(), "ADJUSTMENT", DEBIT, Math.abs(TOTAL_AMOUNT),
                        EJBCommon.FALSE, invAdjustment.getGlChartOfAccount().getCoaCode(), invAdjustment, branchCode,
                        companyCode);

            } else {

                Iterator i = alList.iterator();

                while (i.hasNext()) {

                    InvModAdjustmentLineDetails mdetails = (InvModAdjustmentLineDetails) i.next();

                    LocalInvItemLocation invItemLocation = null;

                    try {

                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(mdetails.getAlLocName(),
                                mdetails.getAlIiName(), companyCode);

                    }
                    catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(mdetails.getAlLineNumber()));
                    }

                    // start date validation

                    LocalInvAdjustmentLine invAdjustmentLine = invAdjustmentLineHome
                            .findByPrimaryKey(mdetails.getAlCode());

                    // remove all inv tag inside PO line
                    Collection invTags = invAdjustmentLine.getInvTags();

                    Iterator x = invTags.iterator();

                    while (x.hasNext()) {

                        LocalInvTag invTag = (LocalInvTag) x.next();

                        x.remove();

                        // invTag.entityRemove();
                        em.remove(invTag);
                    }

                    if (mdetails.getTraceMisc() == 1) {

                        if (invItemLocation.getInvItem().getIiNonInventoriable() == 0 && mdetails.getAlTagList() != null
                                && mdetails.getAlTagList().size() < mdetails.getAlAdjustQuantity()) {

                            throw new GlobalInvTagMissingException(invItemLocation.getInvItem().getIiName());
                        }
                        try {
                            for (Object o : mdetails.getAlTagList()) {
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

                                invTag.setInvAdjustmentLine(invAdjustmentLine);
                                LocalAdUser adUser = null;
                                try {
                                    adUser = adUserHome.findByUsrName(tgLstDetails.getTgCustodian(), companyCode);
                                }
                                catch (FinderException ex) {

                                }
                                if (tgLstDetails.getTgDocumentNumber().equals("")) {
                                    Debug.print("tg document number empty?");
                                    if (invItemLocation.getInvItem().getIiNonInventoriable() == 1) {
                                        invTag.setTgDocumentNumber(adPreference.getPrfInvNextCustodianNumber2());
                                        adPreference.setPrfInvNextCustodianNumber2(EJBCommon
                                                .incrementStringNumber(adPreference.getPrfInvNextCustodianNumber2()));
                                    } else {
                                        invTag.setTgDocumentNumber(adPreference.getPrfInvNextCustodianNumber1());
                                        adPreference.setPrfInvNextCustodianNumber1(EJBCommon
                                                .incrementStringNumber(adPreference.getPrfInvNextCustodianNumber1()));
                                    }
                                } else {
                                    Debug.print("tg document number exist?");
                                    invTag.setTgDocumentNumber(tgLstDetails.getTgDocumentNumber());
                                }
                                invTag.setAdUser(adUser);
                                Debug.print("ngcreate ba?");
                            }
                        }
                        catch (Exception ex) {
                            if (invItemLocation.getInvItem().getIiNonInventoriable() == 0) {
                                throw new GlobalInvTagMissingException(invItemLocation.getInvItem().getIiName());
                            }
                        }
                    }
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(
                                invAdjustment.getAdjDate(), invItemLocation.getInvItem().getIiName(),
                                invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }
                }

                Collection invAdjDistributionRecords = invAdjustment.getInvDistributionRecords();

                i = invAdjDistributionRecords.iterator();

                while (i.hasNext()) {

                    LocalInvDistributionRecord distributionRecord = (LocalInvDistributionRecord) i.next();

                    if (distributionRecord.getDrDebit() == 1) {

                        ABS_TOTAL_AMOUNT += distributionRecord.getDrAmount();
                    }
                }
            }

            // Insufficient Stocks
            //TODO: Review this logic on how to check on insufficient stocks
            if (adPreference.getPrfArCheckInsufficientStock() == EJBCommon.TRUE && !isDraft) {
                boolean hasInsufficientItems = false;
                StringBuilder insufficientItems = new StringBuilder();
                Collection invAdjustmentLines = invAdjustment.getInvAdjustmentLines();
                Iterator i = invAdjustmentLines.iterator();
                HashMap cstMap = new HashMap();
                while (i.hasNext()) {
                    LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();
                    if (invAdjustmentLine.getAlAdjustQuantity() < 0) {
                        hasInsufficientItems = true;
                    }
                }
                if (hasInsufficientItems) {
                    throw new GlobalRecordInvalidException(insufficientItems.substring(0, insufficientItems.lastIndexOf(",")));
                }
            }

            LocalAdUser adUser = adUserHome.findByUsrName(invAdjustment.getAdjCreatedBy(), companyCode);

            // generate approval status

            String INV_APPRVL_STATUS = null;

            if (!isDraft) {

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);

                // check if ar invoice approval is enabled

                if (adApproval.getAprEnableInvAdjustment() == EJBCommon.FALSE) {

                    INV_APPRVL_STATUS = "N/A";

                } else {

                    // check if invoice is self approved
                    LocalAdAmountLimit adAmountLimit = null;

                    try {

                        adAmountLimit = adAmountLimitHome.findAmountLimitPerApprovalUser(adUser.getUsrDept(),
                                "INV ADJUSTMENT", "REQUESTER", details.getAdjLastModifiedBy(), companyCode);

                    }
                    catch (FinderException ex) {

                        throw new GlobalNoApprovalRequesterFoundException();
                    }

                    if (ABS_TOTAL_AMOUNT < adAmountLimit.getCalAmountLimit()) {

                        INV_APPRVL_STATUS = "N/A";

                    } else {

                        // for approval, create approval queue

                        Collection adAmountLimits = adAmountLimitHome
                                .findAmountLimitsPerDepartment(adUser.getUsrDept(),
                                        "INV ADJUSTMENT", adAmountLimit.getCalAmountLimit(), companyCode);

                        if (adAmountLimits.isEmpty()) {

                            Collection adApprovalUsers = adApprovalUserHome.findApprovalUsersPerDepartment(
                                    adUser.getUsrDept(), "APPROVER", adAmountLimit.getCalCode(), companyCode);

                            if (adApprovalUsers.isEmpty()) {

                                throw new GlobalNoApprovalApproverFoundException();
                            }

                            for (Object approvalUser : adApprovalUsers) {

                                LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                LocalAdApprovalQueue adApprovalQueue = adApprovalQueueHome
                                        .AqDepartment(adUser.getUsrDept())
                                        .AqLevel(adApprovalUser.getAuLevel())
                                        .AqNextLevel(EJBCommon.incrementStringNumber(adApprovalUser.getAuLevel()))
                                        .AqDocument("INV ADJUSTMENT")
                                        .AqDocumentCode(invAdjustment.getAdjCode())
                                        .AqDocumentNumber(invAdjustment.getAdjDocumentNumber())
                                        .AqDate(invAdjustment.getAdjDate())
                                        .AqAndOr(adAmountLimit.getCalAndOr())
                                        .AqUserOr(adApprovalUser.getAuOr())
                                        .AqRequesterName(adUser.getUsrDescription())
                                        .AqAdBranch(branchCode)
                                        .AqAdCompany(companyCode)
                                        .buildApprovalQueue();

                                adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);

                                if (adApprovalUser.getAuLevel().equals("LEVEL 1")) {
                                    adApprovalQueue.setAqForApproval(EJBCommon.TRUE);
                                    if ((!adApprovalQueue.getAdUser().getUsrEmailAddress().equals(""))
                                            && (adPreference.getPrfAdEnableEmailNotification() == EJBCommon.TRUE)) {
                                        this.sendEmail(adApprovalQueue, companyCode);
                                    }
                                }
                            }

                        } else {

                            boolean isApprovalUsersFound = false;
                            Iterator i = adAmountLimits.iterator();

                            while (i.hasNext()) {

                                LocalAdAmountLimit adNextAmountLimit = (LocalAdAmountLimit) i.next();

                                if (ABS_TOTAL_AMOUNT < adNextAmountLimit.getCalAmountLimit()) {

                                    Collection adApprovalUsers = adApprovalUserHome.findApprovalUsersPerDepartment(
                                            adUser.getUsrDept(), "APPROVER", adAmountLimit.getCalCode(), companyCode);

                                    for (Object approvalUser : adApprovalUsers) {

                                        isApprovalUsersFound = true;

                                        LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                        LocalAdApprovalQueue adApprovalQueue = adApprovalQueueHome
                                                .AqDepartment(adUser.getUsrDept())
                                                .AqLevel(adApprovalUser.getAuLevel())
                                                .AqNextLevel(EJBCommon.incrementStringNumber(adApprovalUser.getAuLevel()))
                                                .AqDocument("INV ADJUSTMENT")
                                                .AqDocumentCode(invAdjustment.getAdjCode())
                                                .AqDocumentNumber(invAdjustment.getAdjDocumentNumber())
                                                .AqDate(invAdjustment.getAdjDate())
                                                .AqAndOr(adAmountLimit.getCalAndOr())
                                                .AqUserOr(adApprovalUser.getAuOr())
                                                .AqRequesterName(adUser.getUsrDescription())
                                                .AqAdBranch(branchCode)
                                                .AqAdCompany(companyCode)
                                                .buildApprovalQueue();

                                        adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);

                                        if (adApprovalUser.getAuLevel().equals("LEVEL 1")) {
                                            adApprovalQueue.setAqForApproval(EJBCommon.TRUE);
                                            if ((!adApprovalQueue.getAdUser().getUsrEmailAddress().equals(""))
                                                    && (adPreference
                                                    .getPrfAdEnableEmailNotification() == EJBCommon.TRUE)) {
                                                this.sendEmail(adApprovalQueue, companyCode);
                                            }
                                        }
                                    }

                                    break;

                                } else if (!i.hasNext()) {

                                    Collection adApprovalUsers = adApprovalUserHome.findApprovalUsersPerDepartment(
                                            adUser.getUsrDept(), "APPROVER", adNextAmountLimit.getCalCode(), companyCode);

                                    for (Object approvalUser : adApprovalUsers) {

                                        isApprovalUsersFound = true;

                                        LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                        LocalAdApprovalQueue adApprovalQueue = adApprovalQueueHome
                                                .AqDepartment(adUser.getUsrDept())
                                                .AqLevel(adApprovalUser.getAuLevel())
                                                .AqNextLevel(EJBCommon.incrementStringNumber(adApprovalUser.getAuLevel()))
                                                .AqDocument("INV ADJUSTMENT")
                                                .AqDocumentCode(invAdjustment.getAdjCode())
                                                .AqDocumentNumber(invAdjustment.getAdjDocumentNumber())
                                                .AqDate(invAdjustment.getAdjDate())
                                                .AqAndOr(adAmountLimit.getCalAndOr())
                                                .AqUserOr(adApprovalUser.getAuOr())
                                                .AqRequesterName(adUser.getUsrDescription())
                                                .AqAdBranch(branchCode)
                                                .AqAdCompany(companyCode)
                                                .buildApprovalQueue();

                                        adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);

                                        if (adApprovalUser.getAuLevel().equals("LEVEL 1")) {
                                            adApprovalQueue.setAqForApproval(EJBCommon.TRUE);
                                            if ((!adApprovalQueue.getAdUser().getUsrEmailAddress().equals(""))
                                                    && (adPreference.getPrfAdEnableEmailNotification() == EJBCommon.TRUE)) {
                                                this.sendEmail(adApprovalQueue, companyCode);
                                            }
                                        }
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

                invAdjustment.setAdjApprovalStatus(INV_APPRVL_STATUS);

                // set adjustment approval status
                if (INV_APPRVL_STATUS != null && INV_APPRVL_STATUS.equals("N/A") && adPreference.getPrfInvGlPostingType().equals("AUTO-POST UPON APPROVAL")) {
                    this.executeInvAdjPost(invAdjustment.getAdjCode(), invAdjustment.getAdjLastModifiedBy(), branchCode, companyCode);
                }
            }

            return invAdjustment.getAdjCode();

        }
        catch (GlobalRecordAlreadyDeletedException | GlobalRecordInvalidException | GlobalMiscInfoIsRequiredException
               | GlobalExpiryDateNotFoundException | AdPRFCoaGlVarianceAccountNotFoundException |
               GlobalAccountNumberInvalidException
               | GlobalInventoryDateException | GlobalJournalNotBalanceException |
               GlJREffectiveDatePeriodClosedException
               | GlJREffectiveDateNoPeriodExistException | InvTagSerialNumberNotFoundException |
               GlobalInvTagMissingException
               | GlobalInvItemLocationNotFoundException | GlobalNoApprovalApproverFoundException |
               GlobalNoApprovalRequesterFoundException
               | GlobalTransactionAlreadyPostedException | GlobalTransactionAlreadyPendingException |
               GlobalTransactionAlreadyApprovedException
               | GlobalDocumentNumberNotUniqueException | GlobalBranchAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteInvAdjEntry(Integer ADJ_CODE, String AD_USR, Integer companyCode) throws GlobalRecordAlreadyDeletedException {

        Debug.print("InvAdjustmentEntryControllerBean deleteInvAdjEntry");
        try {

            LocalInvAdjustment invAdjustment = invAdjustmentHome.findByPrimaryKey(ADJ_CODE);

            Collection invAdjustmentLines = invAdjustment.getInvAdjustmentLines();

            for (Object adjustmentLine : invAdjustmentLines) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) adjustmentLine;

                if (invAdjustmentLine.getAlAdjustQuantity() < 0) {

                    double convertedQuantity = this.convertByUomFromAndItemAndQuantity(
                            invAdjustmentLine.getInvUnitOfMeasure(),
                            invAdjustmentLine.getInvItemLocation().getInvItem(),
                            Math.abs(invAdjustmentLine.getAlAdjustQuantity()), companyCode);

                    invAdjustmentLine.getInvItemLocation().setIlCommittedQuantity(
                            invAdjustmentLine.getInvItemLocation().getIlCommittedQuantity() - convertedQuantity);
                }
            }

            if (invAdjustment.getAdjApprovalStatus() != null
                    && invAdjustment.getAdjApprovalStatus().equals("PENDING")) {

                Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode("INV ADJUSTMENT",
                        invAdjustment.getAdjCode(), companyCode);

                for (Object approvalQueue : adApprovalQueues) {

                    LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) approvalQueue;

                    // adApprovalQueue.entityRemove();
                    em.remove(adApprovalQueue);
                }
            }

            adDeleteAuditTrailHome.create("INV ADJUSTMENT", invAdjustment.getAdjDate(),
                    invAdjustment.getAdjDocumentNumber(), invAdjustment.getAdjReferenceNumber(), 0d, AD_USR, new Date(),
                    companyCode);

            // invAdjustment.entityRemove();
            em.remove(invAdjustment);

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

        Debug.print("InvAdjustmentEntryControllerBean getGlFcPrecisionUnit");
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

        Debug.print("InvAdjustmentEntryControllerBean getInvGpQuantityPrecisionUnit");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfInvQuantityPrecisionUnit();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getInvGpCostPrecisionUnit(Integer companyCode) {

        Debug.print("InvAdjustmentEntryControllerBean getInvGpCostPrecisionUnit");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfInvCostPrecisionUnit();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getInvGpInventoryLineNumber(Integer companyCode) {

        Debug.print("InvAdjustmentEntryControllerBean getInvGpInventoryLineNumber");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfInvInventoryLineNumber();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdApprovalNotifiedUsersByAdjCode(Integer ADJ_CODE, Integer companyCode) {

        Debug.print("InvAdjustmentEntryControllerBean getAdApprovalNotifiedUsersByAdjCode");
        ArrayList list = new ArrayList();
        try {

            LocalInvAdjustment invAdjustment = invAdjustmentHome.findByPrimaryKey(ADJ_CODE);

            if (invAdjustment.getAdjPosted() == EJBCommon.TRUE) {

                list.add("DOCUMENT POSTED");
                return list;
            }

            Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode("INV ADJUSTMENT",
                    ADJ_CODE, companyCode);

            for (Object approvalQueue : adApprovalQueues) {

                LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) approvalQueue;

                list.add(adApprovalQueue.getAqLevel() + " APPROVER - "
                        + adApprovalQueue.getAdUser().getUsrDescription());
            }

            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public double getInvIiUnitCostByIiNameAndUomNameAndLocNameAndDateAndQty(String II_NM, String UOM_NM, String LOC_NM,
                                                                            Date ADJ_DT, double quantity, Integer branchCode, Integer companyCode) {

        Debug.print("InvAdjustmentEntryControllerBean getInvIiUnitCostByIiNameAndUomNameAndLocNameAndDateAndQty");
        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NM, companyCode);
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            double COST = invItem.getIiUnitCost();

            // get ave cost

            if (LOC_NM != null && ADJ_DT != null) {

                try {

                    LocalInvItemLocation invItemLocation = invItemLocationHome.findByLocNameAndIiName(LOC_NM, II_NM,
                            companyCode);

                    LocalInvCosting invCosting = invCostingHome
                            .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(ADJ_DT,
                                    invItemLocation.getInvItem().getIiName(),
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
                                        ADJ_DT, invItemLocation.getInvItem().getIiName(),
                                        invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                    }

                    switch (invCosting.getInvItemLocation().getInvItem().getIiCostMethod()) {
                        case "Average":
                            Debug.print("remaining value : " + Math.abs(invCosting.getCstRemainingValue()));

                            Debug.print("remaining value : " + invCosting.getCstRemainingQuantity());

                            if (invCosting.getCstRemainingQuantity() <= 0) {
                                COST = EJBCommon.roundIt(invItemLocation.getInvItem().getIiUnitCost(),
                                        adPreference.getPrfInvCostPrecisionUnit());
                            } else {
                                COST = invCosting.getCstRemainingQuantity() <= 0 ? COST
                                        : Math.abs(
                                        invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                                if (COST <= 0) {
                                    COST = invItemLocation.getInvItem().getIiUnitCost();
                                }
                            }

                            Debug.print("cost is  value : " + COST);
                            break;
                        case "FIFO":
                            Debug.print("invCosting.getCstAdjustQuantity()" + quantity);
                            COST = this.getInvFifoCost(ADJ_DT, invItemLocation.getIlCode(), quantity,
                                    invCosting.getCstAdjustCost(), false, branchCode, companyCode);
                            break;
                        case "Standard":
                            COST = invItemLocation.getInvItem().getIiUnitCost();
                            break;
                    }

                }
                catch (FinderException ex) {

                }
            }

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(II_NM, UOM_NM, companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(II_NM, invItem.getInvUnitOfMeasure().getUomName(), companyCode);

            return EJBCommon.roundIt(
                    COST * invDefaultUomConversion.getUmcConversionFactor()
                            / invUnitOfMeasureConversion.getUmcConversionFactor(),
                    adPreference.getPrfInvCostPrecisionUnit());

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods
    private double getInvFifoCost(Date CST_DT, Integer IL_CODE, double CST_QTY, double CST_COST, boolean isAdjustFifo,
                                  Integer branchCode, Integer companyCode) {

        try {

            Collection invFifoCostings = invCostingHome
                    .findFifoRemainingQuantityByLessThanOrEqualCstDateAndIlCodeAndBrCode2(CST_DT, IL_CODE, branchCode,
                            companyCode);
            Debug.print("TEST " + CST_DT + " " + IL_CODE + " " + CST_QTY + " " + CST_COST + " " + branchCode + " "
                    + companyCode);
            Debug.print("invFifoCostings size " + invFifoCostings.size());
            if (invFifoCostings.size() > 0) {
                double fifoCost = 0;
                double runningQty = Math.abs(CST_QTY);
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
                        Debug.print("Unit ----:" + xxxxx);
                        Debug.print("Cost ---:" + fifoCost);
                    } else {
                        Debug.print("CostForward ---:" + fifoCost);
                        double fifoCost2 = fifoCost;
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

                        fifoCost2 = fifoCost - fifoCost2;
                        Debug.print("Unit ----:" + runningQty);
                        Debug.print("Cost ---:" + fifoCost2);
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

    private LocalInvAdjustmentLine addInvAlEntry(InvModAdjustmentLineDetails mdetails, LocalInvAdjustment invAdjustment,
                                                 byte AL_VD, Integer companyCode) throws GlobalMiscInfoIsRequiredException {

        Debug.print("InvAdjustmentEntryControllerBean addInvAlEntry");
        try {

            LocalInvAdjustmentLine invAdjustmentLine = invAdjustmentLineHome.create(mdetails.getAlUnitCost(),
                    mdetails.getAlQcNumber(), mdetails.getAlQcExpiryDate(), mdetails.getAlAdjustQuantity(), 0, AL_VD,
                    companyCode);

            // invAdjustment.addInvAdjustmentLine(invAdjustmentLine);
            invAdjustmentLine.setInvAdjustment(invAdjustment);
            Debug.print("UON Name=" + mdetails.getAlUomName());
            LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(mdetails.getAlUomName(),
                    companyCode);
            // invUnitOfMeasure.addInvAdjustmentLine(invAdjustmentLine);
            invAdjustmentLine.setInvUnitOfMeasure(invUnitOfMeasure);

            LocalInvItemLocation invItemLocation = invItemLocationHome.findByLocNameAndIiName(mdetails.getAlLocName(),
                    mdetails.getAlIiName(), companyCode);
            // invItemLocation.addInvAdjustmentLine(invAdjustmentLine);
            invAdjustmentLine.setInvItemLocation(invItemLocation);

            // validate misc

            if (invAdjustmentLine.getInvItemLocation().getInvItem().getIiTraceMisc() == 1) {

                this.createInvTagList(invAdjustmentLine, mdetails.getAlTagList(), companyCode);
            }

            Debug.print("mdetails.getPlMisc() : " + mdetails.getAlMisc());

            return invAdjustmentLine;

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

        Debug.print("InvAdjustmentEntryControllerBean addInvDrEntry");
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

        Debug.print("InvAdjustmentEntryControllerBean convertByUomFromAndUomToAndQuantity");
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

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem,
                                                      double ADJST_QTY, Integer companyCode) {

        Debug.print("InvAdjustmentEntryControllerBean convertByUomFromAndItemAndQuantity");
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

    private void executeInvAdjPost(Integer ADJ_CODE, String USR_NM, Integer branchCode, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException,
            GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException,
            GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException,
            AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException,
            GlobalMiscInfoIsRequiredException {

        Debug.print("InvAdjustmentEntryControllerBean executeInvAdjPost");
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

            } else if (invAdjustment.getAdjVoid() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyVoidException();
            }

            if (invAdjustment.getAdjVoid() == EJBCommon.FALSE && invAdjustment.getAdjPosted() == EJBCommon.FALSE) {

            } else if (invAdjustment.getAdjVoid() == EJBCommon.TRUE
                    && invAdjustment.getAdjPosted() == EJBCommon.FALSE) { // void adj

                this.regenerateInventoryDr(invAdjustment, branchCode, companyCode);
            }

            // regenerate inventory dr

            Collection invAdjustmentLines = null;

            if (invAdjustment.getAdjVoid() == EJBCommon.FALSE) {
                Debug.print("get not voided lines");
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.FALSE,
                        invAdjustment.getAdjCode(), companyCode);
            } else {
                Debug.print("get voided lines");

                invAdjustmentLines = invAdjustment.getInvAdjustmentLines();
            }
            Debug.print("adj lines : " + invAdjustmentLines.size());

            for (Object adjustmentLine : invAdjustmentLines) {

                Debug.print("void line exec");
                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) adjustmentLine;

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

                    if (invAdjustment.getAdjVoid() == EJBCommon.FALSE) {
                        this.postToInv(invAdjustmentLine, invAdjustment.getAdjDate(), ADJUST_QTY, COST * ADJUST_QTY,
                                ADJUST_QTY, COST * ADJUST_QTY, 0d, null, branchCode, companyCode);
                    } else {
                        this.postToInv(invAdjustmentLine, invAdjustment.getAdjDate(), -ADJUST_QTY, -COST * ADJUST_QTY,
                                -ADJUST_QTY, -COST * ADJUST_QTY, 0d, null, branchCode, companyCode);
                    }

                } else {

                    if (invAdjustment.getAdjVoid() == EJBCommon.FALSE) {

                        this.postToInv(invAdjustmentLine, invAdjustment.getAdjDate(), ADJUST_QTY, COST * ADJUST_QTY,
                                invCosting.getCstRemainingQuantity() + ADJUST_QTY,
                                invCosting.getCstRemainingValue() + (COST * ADJUST_QTY), 0d, null, branchCode, companyCode);
                    } else {
                        Debug.print("void costing ");
                        this.postToInv(invAdjustmentLine, invAdjustment.getAdjDate(), -ADJUST_QTY, -COST * ADJUST_QTY,
                                invCosting.getCstRemainingQuantity() - ADJUST_QTY,
                                invCosting.getCstRemainingValue() - (COST * ADJUST_QTY), 0d, null, branchCode, companyCode);
                    }
                }
            }

            invAdjustment.setAdjPosted(EJBCommon.TRUE);
            invAdjustment.setAdjPostedBy(USR_NM);
            invAdjustment.setAdjDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            // post to gl if necessary

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            if (adPreference.getPrfInvGlPostingType().equals("AUTO-POST UPON APPROVAL")
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
                        Debug.print("DEBIT= " + invDistributionRecord.getDrAmount());
                        TOTAL_DEBIT += DR_AMNT;

                    } else {
                        Debug.print("CREDIT= " + invDistributionRecord.getDrAmount());
                        TOTAL_CREDIT += DR_AMNT;
                    }
                }
                Debug.print("1 TOTAL_DEBIT=" + TOTAL_DEBIT);
                Debug.print("1 TOTAL_CREDIT=" + TOTAL_CREDIT);

                TOTAL_DEBIT = EJBCommon.roundIt(TOTAL_DEBIT, adCompany.getGlFunctionalCurrency().getFcPrecision());
                TOTAL_CREDIT = EJBCommon.roundIt(TOTAL_CREDIT, adCompany.getGlFunctionalCurrency().getFcPrecision());
                Debug.print(
                        "(Math.abs(EJBCommon.roundIt(TOTAL_DEBIT - TOTAL_CREDIT,adCompany.getGlFunctionalCurrency().getFcPrecision()))="
                                + Math.abs(EJBCommon.roundIt(TOTAL_DEBIT - TOTAL_CREDIT,
                                adCompany.getGlFunctionalCurrency().getFcPrecision())));
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

                } else if (Math
                        .abs(EJBCommon.roundIt(TOTAL_DEBIT - TOTAL_CREDIT,
                                adCompany.getGlFunctionalCurrency().getFcPrecision())) == 0.01
                        || (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE
                        && TOTAL_DEBIT != TOTAL_CREDIT)) {
                    Debug.print("2 TOTAL_DEBIT=" + TOTAL_DEBIT);
                    Debug.print("2 TOTAL_CREDIT=" + TOTAL_CREDIT);
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
                        LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome
                                .findByCoaAccountNumberAndBranchCode(adCompany.getCmpRetainedEarnings(), branchCode,
                                        companyCode);

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
        catch (GlJREffectiveDateNoPeriodExistException | GlobalExpiryDateNotFoundException |
               AdPRFCoaGlVarianceAccountNotFoundException | GlobalTransactionAlreadyPostedException |
               GlobalRecordAlreadyDeletedException | GlobalJournalNotBalanceException |
               GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlobalBranchAccountNumberInvalidException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToInv(LocalInvAdjustmentLine invAdjustmentLine, Date CST_DT, double CST_ADJST_QTY,
                           double CST_ADJST_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, double CST_VRNC_VL, String USR_NM,
                           Integer branchCode, Integer companyCode)
            throws AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException {

        Debug.print("InvAdjustmentEntryControllerBean postToInv");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalInvItemLocation invItemLocation = invAdjustmentLine.getInvItemLocation();
            int CST_LN_NMBR = 0;

            CST_ADJST_QTY = EJBCommon.roundIt(CST_ADJST_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_ADJST_CST = EJBCommon.roundIt(CST_ADJST_CST, adPreference.getPrfInvCostPrecisionUnit());
            CST_RMNNG_QTY = EJBCommon.roundIt(CST_RMNNG_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_RMNNG_VL = EJBCommon.roundIt(CST_RMNNG_VL, adPreference.getPrfInvCostPrecisionUnit());

            if (CST_ADJST_QTY < 0) {

                invItemLocation
                        .setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - Math.abs(CST_ADJST_QTY));
            }

            try {
                Debug.print("generate line number");
                // generate line number
                LocalInvCosting invCurrentCosting = invCostingHome
                        .getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(),
                                invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(),
                                branchCode, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;
                Debug.print("generate line number: " + CST_LN_NMBR);
            }
            catch (FinderException ex) {
                Debug.print("generate line number CATCH");
                CST_LN_NMBR = 1;
            }

            if (CST_VRNC_VL != 0) {

                // void subsequent cost variance adjustments
                Collection invAdjustmentLines = invAdjustmentLineHome
                        .findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(CST_DT,
                                invItemLocation.getIlCode(), branchCode, companyCode);

                for (Object adjustmentLine : invAdjustmentLines) {

                    LocalInvAdjustmentLine invAdjustmentLineTemp = (LocalInvAdjustmentLine) adjustmentLine;
                    this.voidInvAdjustment(invAdjustmentLineTemp.getInvAdjustment(), branchCode, companyCode);
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
            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d,
                    CST_ADJST_QTY, CST_ADJST_CST, 0d, 0d, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, 0d, branchCode, companyCode);
            invCosting.setCstQCNumber(invAdjustmentLine.getAlQcNumber());
            invCosting.setCstQCExpiryDate(invAdjustmentLine.getAlQcExpiryDate());
            // invItemLocation.addInvCosting(invCosting);
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setInvAdjustmentLine(invAdjustmentLine);

            invCosting.setCstQuantity(CST_ADJST_QTY);
            invCosting.setCstCost(CST_ADJST_CST);

            // Get Latest Expiry Dates

            if (invAdjustmentLine.getInvItemLocation().getInvItem().getIiTraceMisc() != 0) {
                // removing expiry date code

            }

            // if cost variance is not 0, generate cost variance for the transaction
            if (CST_VRNC_VL != 0) {

                this.generateCostVariance(invCosting.getInvItemLocation(), CST_VRNC_VL,
                        "INVADJ" + invAdjustmentLine.getInvAdjustment().getAdjDocumentNumber(),
                        invAdjustmentLine.getInvAdjustment().getAdjDescription(),
                        invAdjustmentLine.getInvAdjustment().getAdjDate(), USR_NM, branchCode, companyCode);
            }

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT,
                    invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode,
                    companyCode);

            Iterator i = invCostings.iterator();

            String miscList = "";
            ArrayList miscList2 = null;
            // double qty = 0d;

            Debug.print("miscList Propagate:" + miscList);
            String propagateMisc = "";
            String ret = "";

            while (i.hasNext()) {
                String Checker = "";
                String Checker2 = "";

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) i.next();

                invPropagatedCosting
                        .setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_ADJST_QTY);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_ADJST_CST);
                if (CST_ADJST_QTY > 0) {
                    invPropagatedCosting.setCstRemainingLifoQuantity(
                            invPropagatedCosting.getCstRemainingLifoQuantity() + CST_ADJST_QTY);
                }
                Debug.print(
                        "invPropagatedCosting.getCstExpiryDate() : " + invPropagatedCosting.getCstExpiryDate());
                if (invAdjustmentLine.getInvItemLocation().getInvItem().getIiTraceMisc() != 0) {
                }
            }

            // regenerate cost variance
            this.regenerateCostVariance(invCostings, invCosting, branchCode, companyCode);

        }
        catch (AdPRFCoaGlVarianceAccountNotFoundException ex) {

            throw ex;

            // } catch (GlobalExpiryDateNotFoundException ex){

            // throw ex;

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

            // Date
            start = nextIndex + 1;
            nextIndex = misc.indexOf(separator, start);
            length = nextIndex - start;

            String checker = misc.substring(start, start + length);
            if (checker.length() != 0 || checker != "null") {
                miscList.add(checker);
            } else {
                miscList.add("null");
                qty++;
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

    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue,
                          LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double JL_AMNT,
                          Integer companyCode) {

        Debug.print("InvAdjustmentEntryControllerBean postToGl");

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

    private void regenerateInventoryDr(LocalInvAdjustment invAdjustment, Integer branchCode, Integer companyCode)
            throws GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException {

        Debug.print("InvAdjustmentEntryControllerBean regenerateInventoryDr");
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
                                adPreference.getPrfInvCostPrecisionUnit());
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
                        // AMOUNT = invAdjustmentLine.getAlAdjustQuantity() * COST;
                        DEBIT = EJBCommon.FALSE;
                    }

                    // check for branch mapping

                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {

                        adBranchItemLocation = adBranchItemLocationHome
                                .findBilByIlCodeAndBrCode(invItemLocation.getIlCode(), branchCode, companyCode);

                    }
                    catch (FinderException ex) {

                        throw new GlobalBranchAccountNumberInvalidException();
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

                    // add adjust quantity to item location committed
                    // quantity if negative

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

                DEBIT = (TOTAL_AMOUNT >= 0 && !invAdjustment.getAdjType().equals("ISSUANCE")) ? EJBCommon.FALSE
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

    private double convertCostByUom(String II_NM, String UOM_NM, double unitCost, boolean isFromDefault, Integer companyCode) {

        Debug.print("InvAdjustmentEntryControllerBean convertCostByUom");
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

    private void executeVoidInvAdjustment(LocalInvAdjustment invAdjustment, Integer branchCode, Integer companyCode) {

        Debug.print("ApVoucherPostController voidInvAdjustment");

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
                        invDistributionRecord.getInvChartOfAccount().getCoaCode(), invAdjustment, branchCode, companyCode);
            }

            invAdjustment.setAdjVoid(EJBCommon.TRUE);

            this.executeInvAdjPost(invAdjustment.getAdjCode(), invAdjustment.getAdjLastModifiedBy(), branchCode,
                    companyCode);

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void voidInvAdjustment(LocalInvAdjustment invAdjustment, Integer branchCode, Integer companyCode) {

        Debug.print("ApVoucherPostController voidInvAdjustment");

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
                        invDistributionRecord.getInvChartOfAccount().getCoaCode(), invAdjustment, branchCode, companyCode);
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

                InvModAdjustmentLineDetails details = new InvModAdjustmentLineDetails();

                details.setAlAdjustQuantity(0);
                details.setAlUnitCost(invAdjustmentLine.getAlUnitCost() * -1);
                details.setAlIiName(invAdjustmentLine.getInvItemLocation().getInvItem().getIiName());
                details.setAlLocName(invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName());
                details.setAlUomName(
                        invAdjustmentLine.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName());

                this.addInvAlEntry(details, invAdjustment, EJBCommon.TRUE, companyCode);
            }

            invAdjustment.setAdjVoid(EJBCommon.TRUE);

            this.executeInvAdjPost(invAdjustment.getAdjCode(), invAdjustment.getAdjLastModifiedBy(), branchCode,
                    companyCode);

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void generateCostVariance(LocalInvItemLocation invItemLocation, double CST_VRNC_VL, String ADJ_RFRNC_NMBR,
                                      String ADJ_DSCRPTN, Date ADJ_DT, String USR_NM, Integer branchCode, Integer companyCode)
            throws AdPRFCoaGlVarianceAccountNotFoundException {

    }

    private void regenerateCostVariance(Collection invCostings, LocalInvCosting invCosting, Integer branchCode,
                                        Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

    }

    private void createInvTagList(LocalInvAdjustmentLine invAdjustmentLine, ArrayList list, Integer companyCode) throws Exception {

        Debug.print("InvAdjustmentEntryControllerBean createInvTagList");
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

                    invTag.setInvAdjustmentLine(invAdjustmentLine);
                    invTag.setInvItemLocation(invAdjustmentLine.getInvItemLocation());
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

    private ArrayList getInvTagList(LocalInvAdjustmentLine arInvAdjustmentLine) {

        Debug.print("InvAdjustmentEntryControllerBean getInvTagList");
        ArrayList list = new ArrayList();
        Collection invTags = arInvAdjustmentLine.getInvTags();
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

    private LocalInvAdjustment saveInvAdjustment(String ADJ_RFRNC_NMBR, String ADJ_DSCRPTN, Date ADJ_DATE,
                                                 String USR_NM, String NT_BY, Integer branchCode, Integer companyCode) {

        Debug.print("InvAdjustmentEntryControllerBean saveInvAdjustment");
        try {

            // generate adj document number
            String ADJ_DCMNT_NMBR = null;

            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

            try {

                adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("INV ADJUSTMENT",
                        companyCode);

            }
            catch (FinderException ex) {

            }

            try {

                adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome
                        .findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode);

            }
            catch (FinderException ex) {

            }

            while (true) {

                if (adBranchDocumentSequenceAssignment == null
                        || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                    try {

                        invAdjustmentHome.findByAdjDocumentNumberAndBrCode(
                                adDocumentSequenceAssignment.getDsaNextSequence(), branchCode, companyCode);
                        adDocumentSequenceAssignment.setDsaNextSequence(
                                EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                    }
                    catch (FinderException ex) {

                        ADJ_DCMNT_NMBR = adDocumentSequenceAssignment.getDsaNextSequence();
                        adDocumentSequenceAssignment.setDsaNextSequence(
                                EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                        break;
                    }

                } else {

                    try {

                        invAdjustmentHome.findByAdjDocumentNumberAndBrCode(
                                adBranchDocumentSequenceAssignment.getBdsNextSequence(), branchCode, companyCode);
                        adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon
                                .incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                    }
                    catch (FinderException ex) {

                        ADJ_DCMNT_NMBR = adBranchDocumentSequenceAssignment.getBdsNextSequence();
                        adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon
                                .incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                        break;
                    }
                }
            }

            return invAdjustmentHome.create(ADJ_DCMNT_NMBR, ADJ_RFRNC_NMBR, ADJ_DSCRPTN,
                    ADJ_DATE, "COST-VARIANCE", "N/A", EJBCommon.FALSE, USR_NM, ADJ_DATE, USR_NM, ADJ_DATE, null, null,
                    USR_NM, ADJ_DATE, null, null, EJBCommon.TRUE, EJBCommon.FALSE, branchCode, companyCode);

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private byte sendEmail(LocalAdApprovalQueue adApprovalQueue, Integer companyCode) {

        Debug.print("InvApprovalControllerBean sendEmail");
        StringBuilder composedEmail = new StringBuilder();
        LocalInvAdjustment invAdjustment;
        try {

            invAdjustment = invAdjustmentHome.findByPrimaryKey(adApprovalQueue.getAqDocumentCode());

            HashMap hm = new HashMap();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
        String emailTo = adApprovalQueue.getAdUser().getUsrEmailAddress();
        Properties props = new Properties();

        props.put("mail.smtp.host", "180.150.253.101");
        props.put("mail.smtp.socketFactory.port", "25");
        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.port", "25");

        Session session = Session.getDefaultInstance(props, null);

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("ofs-notifcation@daltron.net.pg"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));

            message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("cromero@wrcpng.com"));

            message.setSubject("DALTRON - OFS - INV ADJUSTMENT APPROVAL IA #:" + invAdjustment.getAdjDocumentNumber());

            Debug.print("adApprovalQueue.getAqRequesterName()=" + adApprovalQueue.getAqRequesterName());
            Debug.print("adApprovalQueue.getAqDocumentNumber()=" + adApprovalQueue.getAqDocumentNumber());
            Debug.print("adApprovalQueue.getAdUser().getUsrDescription()="
                    + adApprovalQueue.getAdUser().getUsrDescription());

            message.setContent("Dear " + adApprovalQueue.getAdUser().getUsrDescription() + ",<br><br>"
                    + "A stocktake adjusment request was raised by " + adApprovalQueue.getAqRequesterName()
                    + " for your approval.<br>" + "Adj Number: " + adApprovalQueue.getAqDocumentNumber() + ".<br>"
                    + "Description: " + invAdjustment.getAdjDescription() + ".<br>" + composedEmail
                    + "Please click the link <a href=\"http://180.150.253.99:8080/daltron\">http://180.150.253.99:8080/daltron</a>.<br><br><br>"
                    + "This is an automated message and was sent from a notification-only email address.<br><br>"
                    + "Please do not reply to this message.<br><br>", "text/html");

            Transport.send(message);
            adApprovalQueue.setAqEmailSent(EJBCommon.TRUE);

            Debug.print("Done");

        }
        catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return 0;
    }

    private LocalAdApprovalQueue createApprovalQueue(Integer branchCode, Integer companyCode,
                                                     LocalAdApprovalQueueHome adApprovalQueueHome, LocalInvAdjustment invAdjustment,
                                                     LocalAdAmountLimit adAmountLimit, LocalAdApprovalUser adApprovalUser) throws CreateException {

        return adApprovalQueueHome
                .AqForApproval(EJBCommon.TRUE)
                .AqDocument("INV ADJUSTMENT")
                .AqDocumentCode(invAdjustment.getAdjCode())
                .AqDocumentNumber(invAdjustment.getAdjDocumentNumber())
                .AqDate(invAdjustment.getAdjDate())
                .AqAndOr(adAmountLimit.getCalAndOr())
                .AqUserOr(adApprovalUser.getAuOr())
                .AqAdBranch(branchCode)
                .AqAdCompany(companyCode)
                .buildApprovalQueue();
    }


    public Integer saveInvAdjEntry1(ArrayList arList, Integer ADJ_CODE, String CRTD_BY, Integer branchCode,
                                    Integer companyCode) {

        Debug.print("InvAdjustmentEntryControllerBean saveInvAdjEntry1");
        System.out.print("printC7");
        LocalInvAdjustmentHome invAdjustmentHome = null;
        LocalInvAdjustmentLineHome invAdjustmentLineHome = null;
        LocalAdApprovalHome adApprovalHome = null;
        LocalAdAmountLimitHome adAmountLimitHome = null;
        LocalAdApprovalUserHome adApprovalUserHome = null;
        LocalAdApprovalQueueHome adApprovalQueueHome = null;
        LocalInvItemLocationHome invItemLocationHome = null;
        LocalInvCostingHome invCostingHome = null;
        LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome = null;
        LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome = null;
        LocalAdBranchItemLocationHome adBranchItemLocationHome = null;
        LocalInvItemHome invItemHome = null;

        // Initialize EJB Home

        try {

            invAdjustmentHome = (LocalInvAdjustmentHome) EJBHomeFactory
                    .lookUpLocalHome(LocalInvAdjustmentHome.JNDI_NAME, LocalInvAdjustmentHome.class);
            invAdjustmentLineHome = (LocalInvAdjustmentLineHome) EJBHomeFactory
                    .lookUpLocalHome(LocalInvAdjustmentLineHome.JNDI_NAME, LocalInvAdjustmentLineHome.class);
            adApprovalHome = (LocalAdApprovalHome) EJBHomeFactory.lookUpLocalHome(LocalAdApprovalHome.JNDI_NAME,
                    LocalAdApprovalHome.class);
            adAmountLimitHome = (LocalAdAmountLimitHome) EJBHomeFactory
                    .lookUpLocalHome(LocalAdAmountLimitHome.JNDI_NAME, LocalAdAmountLimitHome.class);
            adApprovalUserHome = (LocalAdApprovalUserHome) EJBHomeFactory
                    .lookUpLocalHome(LocalAdApprovalUserHome.JNDI_NAME, LocalAdApprovalUserHome.class);
            adApprovalQueueHome = (LocalAdApprovalQueueHome) EJBHomeFactory
                    .lookUpLocalHome(LocalAdApprovalQueueHome.JNDI_NAME, LocalAdApprovalQueueHome.class);
            invItemLocationHome = (LocalInvItemLocationHome) EJBHomeFactory
                    .lookUpLocalHome(LocalInvItemLocationHome.JNDI_NAME, LocalInvItemLocationHome.class);
            invCostingHome = (LocalInvCostingHome) EJBHomeFactory.lookUpLocalHome(LocalInvCostingHome.JNDI_NAME,
                    LocalInvCostingHome.class);
            adDocumentSequenceAssignmentHome = (LocalAdDocumentSequenceAssignmentHome) EJBHomeFactory.lookUpLocalHome(
                    LocalAdDocumentSequenceAssignmentHome.JNDI_NAME, LocalAdDocumentSequenceAssignmentHome.class);
            adBranchDocumentSequenceAssignmentHome = (LocalAdBranchDocumentSequenceAssignmentHome) EJBHomeFactory
                    .lookUpLocalHome(LocalAdBranchDocumentSequenceAssignmentHome.JNDI_NAME,
                            LocalAdBranchDocumentSequenceAssignmentHome.class);
            adBranchItemLocationHome = (LocalAdBranchItemLocationHome) EJBHomeFactory
                    .lookUpLocalHome(LocalAdBranchItemLocationHome.JNDI_NAME, LocalAdBranchItemLocationHome.class);
            invItemHome = (LocalInvItemHome) EJBHomeFactory.lookUpLocalHome(LocalInvItemHome.JNDI_NAME,
                    LocalInvItemHome.class);

        }
        catch (NamingException ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            LocalInvAdjustment invAdjustment = null;

            // validate if Adjustment is already deleted

            // validate if adjustment is already posted, void, approved or pending

            LocalInvAdjustment invAdjustmentRequest = null;
            LocalInvAdjustmentLine invAdjustmentRequestLine = null;
            try {

                invAdjustmentRequest = invAdjustmentHome.findByPrimaryKey(ADJ_CODE);

            }
            catch (FinderException ex) {

            }
            // try {

            // invAdjustmentRequestLine = invAdjustmentLineHome.findByPrimaryKey(ADJ_CODE);

            // } catch (FinderException ex) {

            // }

            // validate if document number is unique document number is automatic then set
            // next sequence

            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

            try {

                adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("INV ADJUSTMENT REQUEST",
                        companyCode);

            }
            catch (FinderException ex) {

            }

            try {

                adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome
                        .findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode);

            }
            catch (FinderException ex) {

            }
            String ADJ_NMBR = null;
            if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A') {

                while (true) {

                    if (adBranchDocumentSequenceAssignment == null
                            || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                        try {

                            invAdjustmentHome.findByAdjDocumentNumberAndBrCode(
                                    adDocumentSequenceAssignment.getDsaNextSequence(), branchCode, companyCode);
                            adDocumentSequenceAssignment.setDsaNextSequence(
                                    EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                        }
                        catch (FinderException ex) {

                            ADJ_NMBR = adDocumentSequenceAssignment.getDsaNextSequence();
                            adDocumentSequenceAssignment.setDsaNextSequence(
                                    EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                            break;
                        }

                    } else {

                        try {

                            invAdjustmentHome.findByAdjDocumentNumberAndBrCode(
                                    adBranchDocumentSequenceAssignment.getBdsNextSequence(), branchCode, companyCode);
                            adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon
                                    .incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                        }
                        catch (FinderException ex) {

                            ADJ_NMBR = adBranchDocumentSequenceAssignment.getBdsNextSequence();
                            adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon
                                    .incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                            break;
                        }
                    }
                }
            }

            // used in checking if invoice should re-generate distribution records and
            // re-calculate taxes
            // boolean isRecalculate = true;

            // create adjustment

            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSSS");
            String ADJ_DESC = "GENERATED ADJUSTMENT REQUEST " + formatter.format(new java.util.Date());
            Date CURR_DT = EJBCommon.getGcCurrentDateWoTime().getTime();
            invAdjustment = invAdjustmentHome.create(ADJ_NMBR, invAdjustmentRequest.getAdjDocumentNumber(), ADJ_DESC,
                    CURR_DT, "ISSUANCE", null, EJBCommon.FALSE, CRTD_BY, CURR_DT, CRTD_BY, CURR_DT, null, null, null,
                    null, invAdjustmentRequest.getAdjNotedBy(), null, EJBCommon.FALSE, EJBCommon.FALSE, branchCode,
                    companyCode);

            // invAdjustmentRequest.setAdjApprovalStatus(null);
            // invAdjustmentRequest.setAdjPosted(EJBCommon.FALSE);

            // check if critical fields are changed

            try {

                LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(
                        invAdjustmentRequest.getGlChartOfAccount().getCoaAccountNumber(), branchCode, companyCode);
                // glChartOfAccount.addInvAdjustment(invAdjustment);
                invAdjustment.setGlChartOfAccount(glChartOfAccount);

            }
            catch (FinderException ex) {

                throw new GlobalAccountNumberInvalidException();
            }

            double ABS_TOTAL_AMOUNT = 0d;

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            Collection invAdjustmentLines = invAdjustmentRequest.getInvAdjustmentLines();

            Iterator i = invAdjustmentLines.iterator();

            // remove all distribution records

            Collection arDistributionRecords = invAdjustmentRequest.getInvDistributionRecords();

            // add new adjustment lines and distribution record

            double TOTAL_AMOUNT = 0d;

            byte DEBIT = 0;

            i = arList.iterator();

            while (i.hasNext()) {

                InvModAdjustmentLineDetails mdetails = (InvModAdjustmentLineDetails) i.next();
                Debug.print("hey" + mdetails.getAlCode());
                Debug.print("hey" + mdetails.getAlIiName());
                Debug.print("hey" + mdetails.getAlLocName());
                LocalInvItemLocation invItemLocation = null;
                // try to save adjustment request "served"
                // invAdjustmentRequestLine.setAlServed(mdetails.getAlServed());
                try {

                    LocalInvAdjustmentLine adjustmentRequestLine = invAdjustmentLineHome
                            .findByAlCode(mdetails.getAlCode(), companyCode);
                    adjustmentRequestLine.setAlServed(mdetails.getAlServed());
                }
                catch (FinderException ex) {

                    throw new GlobalInvItemLocationNotFoundException(String.valueOf(mdetails.getAlLineNumber()));
                }

                try {

                    invItemLocation = invItemLocationHome.findByLocNameAndIiName(mdetails.getAlLocName(),
                            mdetails.getAlIiName(), companyCode);

                }
                catch (FinderException ex) {

                    throw new GlobalInvItemLocationNotFoundException(String.valueOf(mdetails.getAlLineNumber()));
                }

                LocalInvAdjustmentLine invAdjustmentLine = this.addInvAlEntry(mdetails, invAdjustment, EJBCommon.FALSE,
                        companyCode);

                // add physical inventory distribution

                double AMOUNT = 0d;

                // check for branch mapping

                LocalAdBranchItemLocation adBranchItemLocation = null;

                try {

                    adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(
                            invAdjustmentLine.getInvItemLocation().getIlCode(), branchCode, companyCode);

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
                ABS_TOTAL_AMOUNT += Math.abs(AMOUNT);

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

            DEBIT = TOTAL_AMOUNT > 0 ? EJBCommon.FALSE : EJBCommon.TRUE;

            i = arList.iterator();

            while (i.hasNext()) {

                InvModAdjustmentLineDetails mdetails = (InvModAdjustmentLineDetails) i.next();

                LocalInvItemLocation invItemLocation = null;

                try {

                    invItemLocation = invItemLocationHome.findByLocNameAndIiName(mdetails.getAlLocName(),
                            mdetails.getAlIiName(), companyCode);

                }
                catch (FinderException ex) {

                    throw new GlobalInvItemLocationNotFoundException(String.valueOf(mdetails.getAlLineNumber()));
                }

                // start date validation

            }

            Collection invAdjDistributionRecords = invAdjustment.getInvDistributionRecords();

            i = invAdjDistributionRecords.iterator();

            while (i.hasNext()) {

                LocalInvDistributionRecord distributionRecord = (LocalInvDistributionRecord) i.next();

                if (distributionRecord.getDrDebit() == 1) {

                    ABS_TOTAL_AMOUNT += distributionRecord.getDrAmount();
                }
            }

            // }

            return invAdjustment.getAdjCode();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public double getQuantityByIiNameAndUomName(String II_NM, String LOC_NM, String UOM_NM, Date PI_DT,
                                                Integer branchCode, Integer companyCode) {

        Debug.print("InvAdjustmentEntryControllerBean getQuantityByIiNameAndUomName");

        LocalInvItemLocationHome invItemLocationHome = null;
        LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome = null;
        LocalInvCostingHome invCostingHome = null;
        LocalInvAdjustmentLineHome invAdjustmentLineHome = null;
        System.out.print("printC2");
        // Initialize EJB Home
        double x = 0;
        try {
            invAdjustmentLineHome = (LocalInvAdjustmentLineHome) EJBHomeFactory
                    .lookUpLocalHome(LocalInvAdjustmentLineHome.JNDI_NAME, LocalInvAdjustmentLineHome.class);
            invItemLocationHome = (LocalInvItemLocationHome) EJBHomeFactory
                    .lookUpLocalHome(LocalInvItemLocationHome.JNDI_NAME, LocalInvItemLocationHome.class);
            invUnitOfMeasureConversionHome = (LocalInvUnitOfMeasureConversionHome) EJBHomeFactory.lookUpLocalHome(
                    LocalInvUnitOfMeasureConversionHome.JNDI_NAME, LocalInvUnitOfMeasureConversionHome.class);
            invCostingHome = (LocalInvCostingHome) EJBHomeFactory.lookUpLocalHome(LocalInvCostingHome.JNDI_NAME,
                    LocalInvCostingHome.class);

        }
        catch (NamingException ex) {

            throw new EJBException(ex.getMessage());
        }
        // double actualQuantity = 0d;
        try {

            double actualQuantity = 0d;

            LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(II_NM, LOC_NM, companyCode);

            try {
                Debug.print("old PI_DT : " + PI_DT);
                PI_DT = new java.util.Date();
                LocalInvCosting invCosting = invCostingHome
                        .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(PI_DT,
                                invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(),
                                branchCode, companyCode);
                Debug.print("actualPI_DT : " + PI_DT);
                actualQuantity = invCosting.getCstRemainingQuantity();
                Debug.print("actual : " + actualQuantity);

            }
            catch (FinderException ex) {

            }

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(II_NM, UOM_NM, companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(II_NM, invItemLocation.getInvItem().getInvUnitOfMeasure().getUomName(),
                            companyCode);
            Debug.print("actual : " + actualQuantity);

            x = EJBCommon.roundIt(
                    actualQuantity * invUnitOfMeasureConversion.getUmcConversionFactor()
                            / invDefaultUomConversion.getUmcConversionFactor(),
                    this.getInvGpQuantityPrecisionUnit(companyCode));
            System.out.print("x = " + x);
        }
        catch (Exception ex) {
            Debug.print("s");

        }
        return x;
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("InvAdjustmentEntryControllerBean ejbCreate");
    }

}