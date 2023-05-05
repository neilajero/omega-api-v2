/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ApPurchaseRequisitionEntryControllerBean
 * @created April 18, 2006 1:00 PM
 * @author Aliza D.J. Anos
 */
package com.ejb.txn.ap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.ad.LocalAdAmountLimitHome;
import com.ejb.entities.ad.LocalAdApproval;
import com.ejb.dao.ad.LocalAdApprovalHome;
import com.ejb.entities.ad.LocalAdApprovalQueue;
import com.ejb.dao.ad.LocalAdApprovalQueueHome;
import com.ejb.dao.ad.LocalAdApprovalUserHome;
import com.ejb.entities.ad.LocalAdBranchDocumentSequenceAssignment;
import com.ejb.dao.ad.LocalAdBranchDocumentSequenceAssignmentHome;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.dao.ad.LocalAdDeleteAuditTrailHome;
import com.ejb.entities.ad.LocalAdDocumentSequenceAssignment;
import com.ejb.dao.ad.LocalAdDocumentSequenceAssignmentHome;
import com.ejb.entities.ad.LocalAdLookUpValue;
import com.ejb.dao.ad.LocalAdLookUpValueHome;
import com.ejb.entities.ad.LocalAdPaymentTerm;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ad.LocalAdUser;
import com.ejb.dao.ad.LocalAdUserHome;
import com.ejb.dao.ap.ILocalApSupplierHome;
import com.ejb.entities.ap.LocalApCanvass;
import com.ejb.dao.ap.LocalApCanvassHome;
import com.ejb.entities.ap.LocalApPurchaseOrder;
import com.ejb.dao.ap.LocalApPurchaseOrderHome;
import com.ejb.entities.ap.LocalApPurchaseOrderLine;
import com.ejb.dao.ap.LocalApPurchaseOrderLineHome;
import com.ejb.entities.ap.LocalApPurchaseRequisition;
import com.ejb.dao.ap.LocalApPurchaseRequisitionHome;
import com.ejb.entities.ap.LocalApPurchaseRequisitionLine;
import com.ejb.dao.ap.LocalApPurchaseRequisitionLineHome;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.entities.ap.LocalApTaxCode;
import com.ejb.dao.ap.LocalApTaxCodeHome;
import com.ejb.exception.global.GlobalConversionDateNotExistException;
import com.ejb.exception.global.GlobalDocumentNumberNotUniqueException;
import com.ejb.exception.global.GlobalInsufficientBudgetQuantityException;
import com.ejb.exception.global.GlobalInvItemLocationNotFoundException;
import com.ejb.exception.global.GlobalNoApprovalApproverFoundException;
import com.ejb.exception.global.GlobalNoApprovalRequesterFoundException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalPaymentTermInvalidException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordInvalidException;
import com.ejb.exception.global.GlobalSendEmailMessageException;
import com.ejb.exception.global.GlobalSupplierItemInvalidException;
import com.ejb.exception.global.GlobalTransactionAlreadyApprovedException;
import com.ejb.exception.global.GlobalTransactionAlreadyPendingException;
import com.ejb.exception.global.GlobalTransactionAlreadyPostedException;
import com.ejb.exception.global.GlobalTransactionAlreadyVoidException;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
import com.ejb.entities.gl.LocalGlFunctionalCurrencyRate;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.entities.inv.LocalInvItem;
import com.ejb.dao.inv.LocalInvItemHome;
import com.ejb.entities.inv.LocalInvItemLocation;
import com.ejb.dao.inv.LocalInvItemLocationHome;
import com.ejb.entities.inv.LocalInvLocation;
import com.ejb.dao.inv.LocalInvLocationHome;
import com.ejb.entities.inv.LocalInvTag;
import com.ejb.dao.inv.LocalInvTagHome;
import com.ejb.entities.inv.LocalInvTransactionalBudget;
import com.ejb.dao.inv.LocalInvTransactionalBudgetHome;
import com.ejb.entities.inv.LocalInvUnitOfMeasure;
import com.ejb.entities.inv.LocalInvUnitOfMeasureConversion;
import com.ejb.dao.inv.LocalInvUnitOfMeasureConversionHome;
import com.ejb.dao.inv.LocalInvUnitOfMeasureHome;
import com.ejb.txn.ad.AdApprovalDocumentEmailNotificationController;
import com.util.mod.ad.AdModApprovalQueueDetails;
import com.util.mod.ap.ApModCanvassDetails;
import com.util.mod.ap.ApModPurchaseRequisitionDetails;
import com.util.mod.ap.ApModPurchaseRequisitionLineDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.mod.gl.GlModFunctionalCurrencyDetails;
import com.util.mod.inv.InvModTagListDetails;
import com.util.mod.inv.InvModUnitOfMeasureDetails;
import com.util.SendEmailDetails;
import com.util.ap.ApPurchaseRequisitionDetails;

@Stateless(name = "ApPurchaseRequisitionEntryControllerEJB")
public class ApPurchaseRequisitionEntryControllerBean extends EJBContextClass implements ApPurchaseRequisitionEntryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    ApApprovalController apApprovalController;
    @EJB
    private AdApprovalDocumentEmailNotificationController adApprovalDocumentEmailNotification;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalApSupplierHome apSupplierHome;
    @EJB
    private LocalAdAmountLimitHome adAmountLimitHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalAdApprovalUserHome adApprovalUserHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdDeleteAuditTrailHome adDeleteAuditTrailHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalApCanvassHome apCanvassHome;
    @EJB
    private LocalApPurchaseOrderHome apPurchaseOrderHome;
    @EJB
    private LocalApPurchaseOrderLineHome apPurchaseOrderLineHome;
    @EJB
    private LocalApPurchaseRequisitionHome apPurchaseRequisitionHome;
    @EJB
    private LocalApPurchaseRequisitionLineHome apPurchaseRequisitionLineHome;
    @EJB
    private LocalApTaxCodeHome apTaxCodeHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalInvTagHome invTagHome;
    @EJB
    private LocalInvTransactionalBudgetHome invTransactionalBudgetHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;

    public ArrayList getApTcAll(Integer AD_CMPNY) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getApTcAll");

        ArrayList list = new ArrayList();


        try {

            Collection apTaxCodes = apTaxCodeHome.findEnabledTcAll(AD_CMPNY);

            for (Object taxCode : apTaxCodes) {

                LocalApTaxCode apTaxCode = (LocalApTaxCode) taxCode;

                list.add(apTaxCode.getTcName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdPrType(Integer AD_CMPNY) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getAdPrType");


        ArrayList list = new ArrayList();


        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AP PR TYPE", AD_CMPNY);

            for (Object lookUpValue : adLookUpValues) {

                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;

                list.add(adLookUpValue.getLvName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public boolean getInvTraceMisc(String II_NAME, Integer AD_CMPNY) {

        Debug.print("InvAdjustmentEntryControllerBean getInvLocAll");

        Collection invLocations = null;
        ArrayList list = new ArrayList();
        boolean isTraceMisc = false;

        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NAME, AD_CMPNY);
            if (invItem.getIiTraceMisc() == 1) {
                isTraceMisc = true;
            }
            return isTraceMisc;
        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getInvTbAllByItemName(String itemName) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getInvTbAllByItemName");

        Collection invTransactionalBudgets = null;

        ArrayList list = new ArrayList();


        try {
            invTransactionalBudgets = invTransactionalBudgetHome.findByTbItemNameAll(itemName);
            if (invTransactionalBudgets.isEmpty()) {
                return null;
            }

            for (Object transactionalBudget : invTransactionalBudgets) {

                LocalInvTransactionalBudget invTransactionalBudget = (LocalInvTransactionalBudget) transactionalBudget;

                list.add(invTransactionalBudget.getTbName());
            }

            return list;

        } catch (Exception ex) {

        }
        return list;
    }

    public double getInvTbForItemForCurrentMonth(String itemName, String userName, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getInvTrnsctnlBdgtForCurrentMonth");
        // TODO: get Transactional Budget for item for current month

        ArrayList list = new ArrayList();

        double quantityForCurrentMonth = 0.0;
        try {
            Date date = new Date();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM");
            java.text.SimpleDateFormat sdfYear = new java.text.SimpleDateFormat("yyyy");
            String month = sdf.format(date);

            LocalAdUser adUser = adUserHome.findByUsrName(userName, AD_CMPNY);
            LocalAdLookUpValue adLookUpValue = adLookUpValueHome.findByLuNameAndLvName("AD DEPARTMENT", adUser.getUsrDept(), AD_CMPNY);

            LocalInvTransactionalBudget invTransactionalBudget = invTransactionalBudgetHome.findByTbItemNameAndTbDeptAndTbYear(itemName, adLookUpValue.getLvCode(), Integer.parseInt(sdfYear.format(date)), AD_CMPNY);
            // LocalInvItem invItem = LocalInvItemHome

            switch (month) {
                case "01":

                    quantityForCurrentMonth = invTransactionalBudget.getTbQuantityJan();
                    break;
                case "02":

                    quantityForCurrentMonth = invTransactionalBudget.getTbQuantityFeb();
                    break;
                case "03":
                    quantityForCurrentMonth = invTransactionalBudget.getTbQuantityMrch();
                    break;
                case "04":
                    quantityForCurrentMonth = invTransactionalBudget.getTbQuantityAprl();
                    break;
                case "05":
                    quantityForCurrentMonth = invTransactionalBudget.getTbQuantityMay();
                    break;
                case "06":
                    quantityForCurrentMonth = invTransactionalBudget.getTbQuantityJun();
                    break;
                case "07":
                    quantityForCurrentMonth = invTransactionalBudget.getTbQuantityJul();
                    break;
                case "08":
                    quantityForCurrentMonth = invTransactionalBudget.getTbQuantityAug();
                    break;
                case "09":
                    quantityForCurrentMonth = invTransactionalBudget.getTbQuantitySep();
                    break;
                case "10":
                    quantityForCurrentMonth = invTransactionalBudget.getTbQuantityOct();
                    break;
                case "11":
                    quantityForCurrentMonth = invTransactionalBudget.getTbQuantityNov();
                    break;
                default:
                    quantityForCurrentMonth = invTransactionalBudget.getTbQuantityDec();
                    break;
            }
            Debug.print(month + "<== current month");
            return quantityForCurrentMonth;

        } catch (FinderException ex) {
            Debug.print("no item found in transactional budget table");
            // throw new GlobalNoRecordFoundException();
        }
        return quantityForCurrentMonth;
    }

    public ArrayList getGlFcAllWithDefault(Integer AD_CMPNY) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getGlFcAllWithDefault");

        Collection glFunctionalCurrencies = null;

        LocalGlFunctionalCurrency glFunctionalCurrency = null;
        LocalAdCompany adCompany = null;

        ArrayList list = new ArrayList();

        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            glFunctionalCurrencies = glFunctionalCurrencyHome.findFcAllEnabled(EJBCommon.getGcCurrentDateWoTime().getTime(), AD_CMPNY);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (glFunctionalCurrencies.isEmpty()) {

            return null;
        }

        for (Object functionalCurrency : glFunctionalCurrencies) {

            glFunctionalCurrency = (LocalGlFunctionalCurrency) functionalCurrency;

            GlModFunctionalCurrencyDetails mdetails = new GlModFunctionalCurrencyDetails(glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), adCompany.getGlFunctionalCurrency().getFcName().equals(glFunctionalCurrency.getFcName()) ? EJBCommon.TRUE : EJBCommon.FALSE);

            list.add(mdetails);
        }

        return list;
    }

    public ArrayList getInvLocAll(Integer AD_CMPNY) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getInvLocAll");

        Collection invLocations = null;
        ArrayList list = new ArrayList();

        try {

            invLocations = invLocationHome.findLocAll(AD_CMPNY);

            if (invLocations.isEmpty()) {

                return null;
            }

            for (Object location : invLocations) {

                LocalInvLocation invLocation = (LocalInvLocation) location;
                String details = invLocation.getLocName();

                list.add(details);
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getInvGpQuantityPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getInvGpQuantityPrecisionUnit");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfInvQuantityPrecisionUnit();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getAdPrfApJournalLineNumber(Integer AD_CMPNY) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getAdPrfApJournalLineNumber");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfApJournalLineNumber();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public String getAdPrfApDefaultPrTax(Integer AD_CMPNY) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getAdPrfApDefaultPrTax");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfApDefaultPrTax();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public String getAdPrfApDefaultPrCurrency(Integer AD_CMPNY) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getAdPrfApDefaultPrCurrency");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfApDefaultPrCurrency();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ApModPurchaseRequisitionDetails getApPrByPrCode(Integer PR_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getApPrByPrCode");

        try {

            LocalApPurchaseRequisition apPurchaseRequisition = null;

            try {

                apPurchaseRequisition = apPurchaseRequisitionHome.findByPrimaryKey(PR_CODE);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            String specs = null;
            String propertyCode = null;
            String expiryDate = null;
            String serialNumber = null;
            ArrayList list = new ArrayList();

            // get purchase requisition lines if any

            Collection apPurchaseRequisitionLines = apPurchaseRequisition.getApPurchaseRequisitionLines();

            Iterator i = apPurchaseRequisitionLines.iterator();

            double PR_TOTAL_AMNT = 0d;

            while (i.hasNext()) {

                LocalApPurchaseRequisitionLine apPurchaseRequisitionLine = (LocalApPurchaseRequisitionLine) i.next();

                ApModPurchaseRequisitionLineDetails prlDetails = new ApModPurchaseRequisitionLineDetails();
                prlDetails.setTraceMisc(apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiTraceMisc());
                ArrayList tagList = new ArrayList();
                if (prlDetails.getTraceMisc() == 1) {
                    Collection invTags = invTagHome.findByPrlCode(apPurchaseRequisitionLine.getPrlCode(), AD_CMPNY);
                    for (Object tag : invTags) {
                        LocalInvTag invTag = (LocalInvTag) tag;
                        InvModTagListDetails tgLstDetails = new InvModTagListDetails();
                        tgLstDetails.setTgPropertyCode(invTag.getTgPropertyCode());
                        tgLstDetails.setTgSpecs(invTag.getTgSpecs());
                        tgLstDetails.setTgExpiryDate(invTag.getTgExpiryDate());
                        tgLstDetails.setTgSerialNumber(invTag.getTgSerialNumber());
                        try {

                            tgLstDetails.setTgCustodian(invTag.getAdUser().getUsrName());
                        } catch (Exception ex) {
                            tgLstDetails.setTgCustodian("");
                        }

                        tagList.add(tgLstDetails);
                    }
                    prlDetails.setPrlTagList(tagList);
                }
                prlDetails.setPrlCode(apPurchaseRequisitionLine.getPrlCode());
                prlDetails.setPrlLine(apPurchaseRequisitionLine.getPrlLine());
                prlDetails.setPrlIlIiName(apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiName());
                prlDetails.setPrlIlLocName(apPurchaseRequisitionLine.getInvItemLocation().getInvLocation().getLocName());
                prlDetails.setPrlQuantity(apPurchaseRequisitionLine.getPrlQuantity());
                prlDetails.setPrlAmount(apPurchaseRequisitionLine.getPrlAmount());
                prlDetails.setPrlRemarks(apPurchaseRequisitionLine.getPrlRemarks());
                prlDetails.setPrlIlIiDescription(apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiDescription());
                prlDetails.setPrlUomName(apPurchaseRequisitionLine.getInvUnitOfMeasure().getUomName());

                // get canvass amount

                if (apPurchaseRequisitionLine.getApCanvasses().size() > 0) {
                    double canvassAmount = 0d;

                    for (Object o : apPurchaseRequisitionLine.getApCanvasses()) {
                        LocalApCanvass apCanvass = (LocalApCanvass) o;

                        if (apCanvass.getCnvPo() == EJBCommon.TRUE) {
                            canvassAmount += apCanvass.getCnvAmount();
                        }

                    }

                    if (canvassAmount > 0) {
                        prlDetails.setPrlAmount(canvassAmount);
                    }

                }

                PR_TOTAL_AMNT += prlDetails.getPrlAmount();

                list.add(prlDetails);
            }

            Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCodeAll("AP PURCHASE REQUISITION", apPurchaseRequisition.getPrCode(), AD_CMPNY);

            Debug.print("adApprovalQueues=" + adApprovalQueues.size());
            ArrayList approverlist = new ArrayList();

            i = adApprovalQueues.iterator();

            while (i.hasNext()) {
                LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) i.next();

                AdModApprovalQueueDetails adModApprovalQueueDetails = new AdModApprovalQueueDetails();
                adModApprovalQueueDetails.setAqApprovedDate(adApprovalQueue.getAqApprovedDate());
                adModApprovalQueueDetails.setAqApproverName(adApprovalQueue.getAdUser().getUsrDescription());
                adModApprovalQueueDetails.setAqStatus(adApprovalQueue.getAqApproved() == EJBCommon.TRUE ? adApprovalQueue.getAqLevel() + " APPROVED" : adApprovalQueue.getAqLevel() + " PENDING");

                approverlist.add(adModApprovalQueueDetails);
            }

            ApModPurchaseRequisitionDetails mPrDetails = new ApModPurchaseRequisitionDetails();

            mPrDetails.setPrCode(apPurchaseRequisition.getPrCode());
            mPrDetails.setPrDate(apPurchaseRequisition.getPrDate());
            mPrDetails.setPrNumber(apPurchaseRequisition.getPrNumber());
            mPrDetails.setPrReferenceNumber(apPurchaseRequisition.getPrReferenceNumber());
            mPrDetails.setPrDescription(apPurchaseRequisition.getPrDescription());
            mPrDetails.setPrVoid(apPurchaseRequisition.getPrVoid());
            mPrDetails.setPrConversionDate(apPurchaseRequisition.getPrConversionDate());
            mPrDetails.setPrConversionRate(apPurchaseRequisition.getPrConversionRate());
            mPrDetails.setPrApprovalStatus(apPurchaseRequisition.getPrApprovalStatus());
            mPrDetails.setPrPosted(apPurchaseRequisition.getPrPosted());
            mPrDetails.setPrGenerated(apPurchaseRequisition.getPrGenerated());
            mPrDetails.setPrCanvassReasonForRejection(apPurchaseRequisition.getPrCanvassReasonForRejection());
            mPrDetails.setPrCreatedBy(apPurchaseRequisition.getPrCreatedBy());
            mPrDetails.setPrDateCreated(apPurchaseRequisition.getPrDateCreated());
            mPrDetails.setPrLastModifiedBy(apPurchaseRequisition.getPrLastModifiedBy());
            mPrDetails.setPrDateLastModified(apPurchaseRequisition.getPrDateLastModified());
            mPrDetails.setPrApprovedRejectedBy(apPurchaseRequisition.getPrApprovedRejectedBy());
            mPrDetails.setPrDateApprovedRejected(apPurchaseRequisition.getPrDateApprovedRejected());
            mPrDetails.setPrPostedBy(apPurchaseRequisition.getPrPostedBy());
            mPrDetails.setPrDatePosted(apPurchaseRequisition.getPrDatePosted());
            mPrDetails.setPrTagStatus(apPurchaseRequisition.getPrTagStatus());
            mPrDetails.setPrType(apPurchaseRequisition.getPrType());
            mPrDetails.setPrDeliveryPeriod(apPurchaseRequisition.getPrDeliveryPeriod());
            mPrDetails.setPrSchedule(apPurchaseRequisition.getPrSchedule());
            mPrDetails.setPrNextRunDate(apPurchaseRequisition.getPrNextRunDate());
            mPrDetails.setPrLastRunDate(apPurchaseRequisition.getPrLastRunDate());

            if (apPurchaseRequisition.getPrAdUserName1() != null) {

                LocalAdUser userName1 = adUserHome.findByPrimaryKey(apPurchaseRequisition.getPrAdUserName1());
                mPrDetails.setPrAdNotifiedUser1(userName1.getUsrName());
            }

            mPrDetails.setPrCanvassApprovalStatus(apPurchaseRequisition.getPrCanvassApprovalStatus());
            mPrDetails.setPrCanvassApprovedRejectedBy(apPurchaseRequisition.getPrCanvassApprovedRejectedBy());
            mPrDetails.setPrCanvassDateApprovedRejected(apPurchaseRequisition.getPrCanvassDateApprovedRejected());
            mPrDetails.setPrCanvassDatePosted(apPurchaseRequisition.getPrCanvassDatePosted());
            mPrDetails.setPrCanvassPosted(apPurchaseRequisition.getPrCanvassPosted());
            mPrDetails.setPrCanvassPostedBy(apPurchaseRequisition.getPrCanvassPostedBy());

            mPrDetails.setPrReasonForRejection(apPurchaseRequisition.getPrReasonForRejection());
            mPrDetails.setPrCanvassReasonForRejection(apPurchaseRequisition.getPrCanvassReasonForRejection());
            mPrDetails.setPrTcName(apPurchaseRequisition.getApTaxCode().getTcName());
            mPrDetails.setPrFcName(apPurchaseRequisition.getGlFunctionalCurrency().getFcName());
            mPrDetails.setPrBrCode(apPurchaseRequisition.getPrAdBranch());
            mPrDetails.setPrAmount(PR_TOTAL_AMNT);

            mPrDetails.setPrPrlList(list);
            mPrDetails.setPrAPRList(approverlist);

            return mPrDetails;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ApModPurchaseRequisitionLineDetails getApCnvByPrlCode(ApModPurchaseRequisitionLineDetails mdetails, Integer PRL_CODE, Integer AD_CMPNY) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getApCnvByPrlCode");


        ArrayList list = new ArrayList();

        LocalApPurchaseRequisitionLine apPurchaseRequisitionLine = null;

        try {

            ApModPurchaseRequisitionLineDetails details = new ApModPurchaseRequisitionLineDetails();

            if (PRL_CODE == null) {

                // regenerate canvass lines

                details.setRegenerateCanvass(true);

            } else {

                try {

                    apPurchaseRequisitionLine = apPurchaseRequisitionLineHome.findByPrimaryKey(PRL_CODE);

                } catch (FinderException ex) {

                }

                /*
                 * Debug.print( "prl code : " + PRL_CODE); Debug.print( "iiname : " +
                 * apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiName());
                 * Debug.print( "locname : " +
                 * apPurchaseRequisitionLine.getInvItemLocation().getInvLocation().getLocName()) ;
                 * Debug.print("uom : " +
                 * apPurchaseRequisitionLine.getInvUnitOfMeasure().getUomName());
                 */
                if (!apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiName().equals(mdetails.getPrlIlIiName()) || !apPurchaseRequisitionLine.getInvItemLocation().getInvLocation().getLocName().equals(mdetails.getPrlIlLocName()) || !apPurchaseRequisitionLine.getInvUnitOfMeasure().getUomName().equals(mdetails.getPrlUomName())) {

                    details.setRegenerateCanvass(true);

                } else {

                    details.setRegenerateCanvass(false);

                    Collection apCanvasses = apPurchaseRequisitionLine.getApCanvasses();

                    for (Object canvass : apCanvasses) {

                        LocalApCanvass apCanvass = (LocalApCanvass) canvass;

                        ApModCanvassDetails cnvDetails = new ApModCanvassDetails();

                        cnvDetails.setCnvCode(apCanvass.getCnvCode());
                        cnvDetails.setCnvRemarks(apCanvass.getCnvRemarks());
                        cnvDetails.setCnvLine(apCanvass.getCnvLine());
                        cnvDetails.setCnvQuantity(apCanvass.getCnvQuantity());
                        cnvDetails.setCnvUnitCost(apCanvass.getCnvUnitCost());
                        cnvDetails.setCnvPo(apCanvass.getCnvPo());
                        cnvDetails.setCnvSplSupplierCode(apCanvass.getApSupplier().getSplSupplierCode());

                        details.saveCnvList(cnvDetails);
                    }
                }
            }

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer getApPrlCodeByPrlLineAndPrCode(short PRL_LN, Integer PR_CODE, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getApPrlCode");


        try {

            LocalApPurchaseRequisitionLine apPurchaseRequisitionLine = apPurchaseRequisitionLineHome.findByPrCodeAndPrlLineAndBrCode(PR_CODE, PRL_LN, AD_BRNCH, AD_CMPNY);

            return apPurchaseRequisitionLine.getPrlCode();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer saveApPrCanvass(ApPurchaseRequisitionDetails details, String RV_AD_NTFD_USR1, String TC_NM, String FC_NM, ArrayList prlList, boolean isDraft, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalInvItemLocationNotFoundException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalSupplierItemInvalidException, GlobalRecordInvalidException {

        Debug.print("ApPurchaseRequisitionEntryControllerBean saveApPrCanvass");

        LocalApPurchaseRequisition apPurchaseRequisition = null;

        ArrayList list = new ArrayList();


        try {

            // validate if purchase requisition is already deleted

            try {

                if (details.getPrCode() != null) {

                    apPurchaseRequisition = apPurchaseRequisitionHome.findByPrimaryKey(details.getPrCode());
                }

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if purchase requisition is already posted, void, approved or pending

            if (details.getPrCode() != null) {

                if (apPurchaseRequisition.getPrCanvassApprovalStatus() != null) {

                    if (apPurchaseRequisition.getPrCanvassApprovalStatus().equals("APPROVED") || apPurchaseRequisition.getPrCanvassApprovalStatus().equals("N/A")) {

                        throw new GlobalTransactionAlreadyApprovedException();

                    } else if (apPurchaseRequisition.getPrCanvassApprovalStatus().equals("PENDING")) {

                        throw new GlobalTransactionAlreadyPendingException();
                    }
                }

                if (apPurchaseRequisition.getPrCanvassPosted() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyPostedException();

                } else if (apPurchaseRequisition.getPrVoid() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyVoidException();
                }
            }

            // purchase requisition void

            if (details.getPrCode() != null && details.getPrVoid() == EJBCommon.TRUE && apPurchaseRequisition.getPrCanvassPosted() == EJBCommon.FALSE) {

                apPurchaseRequisition.setPrVoid(EJBCommon.TRUE);
                apPurchaseRequisition.setPrLastModifiedBy(details.getPrLastModifiedBy());
                apPurchaseRequisition.setPrDateLastModified(details.getPrDateLastModified());

                return apPurchaseRequisition.getPrCode();
            }

            // validate if document number is unique document number is automatic then set
            // next sequence

            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

            if (details.getPrCode() == null) {

                try {

                    adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AP PURCHASE REQUISITION", AD_CMPNY);

                } catch (FinderException ex) {
                }

                try {

                    adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, AD_CMPNY);

                } catch (FinderException ex) {
                }

                LocalApPurchaseRequisition apExistingPurchaseRequisition = null;

                try {

                    apExistingPurchaseRequisition = apPurchaseRequisitionHome.findByPrNumberAndBrCode(details.getPrNumber(), AD_BRNCH, AD_CMPNY);

                } catch (FinderException ex) {
                }

                if (apExistingPurchaseRequisition != null) {

                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (details.getPrNumber() == null || details.getPrNumber().trim().length() == 0)) {

                    while (true) {

                        if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                            try {

                                apPurchaseRequisitionHome.findByPrNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, AD_CMPNY);
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                            } catch (FinderException ex) {

                                details.setPrNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                break;
                            }

                        } else {

                            try {

                                apPurchaseRequisitionHome.findByPrNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, AD_CMPNY);
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                            } catch (FinderException ex) {

                                details.setPrNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                break;
                            }
                        }
                    }
                }

            } else {

                LocalApPurchaseRequisition apExistingPurchaseRequisition = null;

                try {

                    apExistingPurchaseRequisition = apPurchaseRequisitionHome.findByPrNumberAndBrCode(details.getPrNumber(), AD_BRNCH, AD_CMPNY);

                } catch (FinderException ex) {
                }

                if (apExistingPurchaseRequisition != null && !apExistingPurchaseRequisition.getPrCode().equals(details.getPrCode())) {

                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (apPurchaseRequisition.getPrNumber() != details.getPrNumber() && (details.getPrNumber() == null || details.getPrNumber().trim().length() == 0)) {

                    details.setPrNumber(apPurchaseRequisition.getPrNumber());
                }
            }

            // validate if conversion date exists

            // validate if conversion date exists

            try {

                if (details.getPrConversionDate() != null) {

                    LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);
                    LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(), details.getPrConversionDate(), AD_CMPNY);
                }

            } catch (FinderException ex) {

                throw new GlobalConversionDateNotExistException();
            }

            // get all notified users
            LocalAdUser userName1 = null;

            try {

                userName1 = adUserHome.findByUsrName(RV_AD_NTFD_USR1, AD_CMPNY);

            } catch (FinderException ex) {

            }

            boolean isRecalculate = true;

            // create purchase requisition

            if (details.getPrCode() == null) {

                apPurchaseRequisition = apPurchaseRequisitionHome.create(details.getPrDescription(), details.getPrNumber(), details.getPrDate(), details.getPrDeliveryPeriod(), details.getPrReferenceNumber(), details.getPrConversionDate(), details.getPrConversionRate(), null, EJBCommon.FALSE, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, null, null, details.getPrCreatedBy(), details.getPrDateCreated(), details.getPrLastModifiedBy(), details.getPrDateLastModified(), details.getPrApprovedRejectedBy(), details.getPrDateApprovedRejected(), details.getPrPostedBy(), details.getPrDatePosted(), details.getPrCanvassApprovedRejectedBy(), details.getPrCanvassDateApprovedRejected(), details.getPrCanvassPostedBy(), details.getPrCanvassDatePosted(), details.getPrTagStatus(), details.getPrType(), userName1 != null ? userName1.getUsrCode() : null, details.getPrSchedule(), details.getPrNextRunDate(), null, AD_BRNCH, AD_CMPNY);


                // ADD DEPARTMENT
                String department = null;
                try {
                    LocalAdUser adUser = adUserHome.findByUsrName(details.getPrCreatedBy(), AD_CMPNY);
                    department = adUser.getUsrDept();
                } catch (FinderException ex) {
                    Debug.print("NO user found, no department assigned");
                }

                apPurchaseRequisition.setPrDepartment(department);

            } else {

                // check if critical fields are changed

                if (!apPurchaseRequisition.getApTaxCode().getTcName().equals(TC_NM) || prlList.size() != apPurchaseRequisition.getApPurchaseRequisitionLines().size()) {

                    isRecalculate = true;

                } else if (prlList.size() == apPurchaseRequisition.getApPurchaseRequisitionLines().size()) {

                    Iterator ilIter = apPurchaseRequisition.getApPurchaseRequisitionLines().iterator();
                    Iterator ilListIter = prlList.iterator();

                    while (ilIter.hasNext()) {

                        LocalApPurchaseRequisitionLine apPurchaseRequisitionLine = (LocalApPurchaseRequisitionLine) ilIter.next();
                        ApModPurchaseRequisitionLineDetails mdetails = (ApModPurchaseRequisitionLineDetails) ilListIter.next();

                        if (!apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiName().equals(mdetails.getPrlIlIiName()) || !apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiDescription().equals(mdetails.getPrlIlIiDescription()) || !apPurchaseRequisitionLine.getInvItemLocation().getInvLocation().getLocName().equals(mdetails.getPrlIlLocName()) || !apPurchaseRequisitionLine.getInvUnitOfMeasure().getUomName().equals(mdetails.getPrlUomName()) || apPurchaseRequisitionLine.getPrlQuantity() != mdetails.getPrlQuantity() || apPurchaseRequisitionLine.getPrlAmount() != mdetails.getPrlAmount()) {

                            // isRecalculate = true;

                        }
                    }

                } else {

                    // isRecalculate = false;

                }

                apPurchaseRequisition.setPrDescription(details.getPrDescription());
                apPurchaseRequisition.setPrDate(details.getPrDate());
                apPurchaseRequisition.setPrNumber(details.getPrNumber());
                apPurchaseRequisition.setPrReferenceNumber(details.getPrReferenceNumber());
                apPurchaseRequisition.setPrVoid(details.getPrVoid());
                apPurchaseRequisition.setPrConversionDate(details.getPrConversionDate());
                apPurchaseRequisition.setPrConversionRate(details.getPrConversionRate());
                apPurchaseRequisition.setPrLastModifiedBy(details.getPrLastModifiedBy());
                apPurchaseRequisition.setPrDateLastModified(details.getPrDateLastModified());
                apPurchaseRequisition.setPrDeliveryPeriod(details.getPrDeliveryPeriod());
                apPurchaseRequisition.setPrAdUserName1(userName1 != null ? userName1.getUsrCode() : null);
                apPurchaseRequisition.setPrSchedule(details.getPrSchedule());
                apPurchaseRequisition.setPrNextRunDate(details.getPrNextRunDate());
            }

            LocalApTaxCode apTaxCode = apTaxCodeHome.findByTcName(TC_NM, AD_CMPNY);
            apPurchaseRequisition.setApTaxCode(apTaxCode);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);
            apPurchaseRequisition.setGlFunctionalCurrency(glFunctionalCurrency);

            double TOTAL_AMOUNT = 0d;

            if (isRecalculate) {

                // remove purchase requisition lines

                Collection apPurchaseRequisitionLines = apPurchaseRequisition.getApPurchaseRequisitionLines();

                Iterator i = apPurchaseRequisitionLines.iterator();

                while (i.hasNext()) {

                    LocalApPurchaseRequisitionLine apPurchaseRequisitionLine = (LocalApPurchaseRequisitionLine) i.next();

                    i.remove();

                    // apPurchaseRequisitionLine.entityRemove();
                    em.remove(apPurchaseRequisitionLine);
                }

                // add new purchase requisition lines

                i = prlList.iterator();

                LocalInvItemLocation invItemLocation = null;

                while (i.hasNext()) {

                    ApModPurchaseRequisitionLineDetails mPrlDetails = (ApModPurchaseRequisitionLineDetails) i.next();

                    // save purchase requisition line, qty and amount based on the retrieved unit
                    // cost

                    LocalApPurchaseRequisitionLine apPurchaseRequisitionLine = apPurchaseRequisitionLineHome.create(mPrlDetails.getPrlLine(), mPrlDetails.getPrlQuantity(), mPrlDetails.getPrlAmount(), mPrlDetails.getPrlRemarks(), AD_CMPNY);

                    apPurchaseRequisition.addApPurchaseRequisitionLine(apPurchaseRequisitionLine);

                    try {

                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(mPrlDetails.getPrlIlLocName(), mPrlDetails.getPrlIlIiName(), AD_CMPNY);

                    } catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(mPrlDetails.getPrlLine()));
                    }

                    invItemLocation.addApPurchaseRequisitionLine(apPurchaseRequisitionLine);

                    LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(mPrlDetails.getPrlUomName(), AD_CMPNY);

                    invUnitOfMeasure.addApPurchaseRequisitionLine(apPurchaseRequisitionLine);

                    short CNV_LN = 0;
                    double II_UNT_CST = mPrlDetails.getPrlAmount();
                    double CNV_QTY = mPrlDetails.getPrlQuantity();

                    if (mPrlDetails.getRegenerateCanvass()) {

                        if (invItemLocation.getInvItem().getApSupplier() != null) {

                            // generate canvass lines for purchase requisition line
                            CNV_LN++;

                            // create canvass line
                            LocalApCanvass apCanvass = apCanvassHome.create(CNV_LN, null, CNV_QTY, II_UNT_CST, II_UNT_CST, EJBCommon.TRUE, AD_CMPNY);

                            LocalApSupplier apSupplier = invItemLocation.getInvItem().getApSupplier();
                            apSupplier.addApCanvass(apCanvass);

                            apPurchaseRequisitionLine.addApCanvass(apCanvass);
                        }

                        CNV_LN++;

                        // create canvass lines for other suppliers of item
                        Collection apPurchaseOrderLines = null;

                        try {

                            apPurchaseOrderLines = apPurchaseOrderLineHome.findByPlIlCodeAndPoReceivingAndPoPostedAndBrCode(apPurchaseRequisitionLine.getInvItemLocation().getIlCode(), EJBCommon.TRUE, EJBCommon.TRUE, AD_BRNCH, AD_CMPNY);

                        } catch (FinderException ex) {

                        }

                        if (!apPurchaseOrderLines.isEmpty() && apPurchaseOrderLines.size() > 0) {

                            for (Object purchaseOrderLine : apPurchaseOrderLines) {

                                LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) purchaseOrderLine;

                                // continue if next purchase order line has existing supplier in canvass

                                Collection apCanvasses = apCanvassHome.findByPrlCodeAndSplSupplierCode(apPurchaseRequisitionLine.getPrlCode(), apPurchaseOrderLine.getApPurchaseOrder().getApSupplier().getSplSupplierCode(), AD_CMPNY);

                                if (!apCanvasses.isEmpty()) continue;

                                // convert purchase order line unit cost
                                double POL_UNT_CST = this.convertCostByUom(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiName(), apPurchaseOrderLine.getInvUnitOfMeasure().getUomName(), apPurchaseOrderLine.getPlUnitCost(), false, AD_CMPNY);
                                POL_UNT_CST = this.convertCostByUom(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiName(), apPurchaseRequisitionLine.getInvUnitOfMeasure().getUomName(), POL_UNT_CST, true, AD_CMPNY);

                                // create canvass line
                                LocalApCanvass apCanvass = apCanvassHome.create(CNV_LN, null, 0d, POL_UNT_CST, 0d, EJBCommon.FALSE, AD_CMPNY);

                                if (CNV_LN == 1) {

                                    apCanvass.setCnvQuantity(CNV_QTY);
                                    apCanvass.setCnvAmount(II_UNT_CST);
                                    apCanvass.setCnvPo(EJBCommon.TRUE);
                                }

                                LocalApSupplier apSupplier = apPurchaseOrderLine.getApPurchaseOrder().getApSupplier();
                                apSupplier.addApCanvass(apCanvass);

                                apPurchaseRequisitionLine.addApCanvass(apCanvass);

                                CNV_LN++;
                            }
                        }

                    } else {

                        CNV_LN++;
                        ArrayList cnvList = mPrlDetails.getCnvList();

                        for (Object o : cnvList) {

                            ApModCanvassDetails cnvDetails = (ApModCanvassDetails) o;

                            // create canvass line
                            LocalApCanvass apCanvass = apCanvassHome.create(CNV_LN, cnvDetails.getCnvRemarks(), cnvDetails.getCnvQuantity(), cnvDetails.getCnvUnitCost(), EJBCommon.roundIt(cnvDetails.getCnvUnitCost() * cnvDetails.getCnvQuantity(), this.getGlFcPrecisionUnit(AD_CMPNY)), cnvDetails.getCnvPo(), AD_CMPNY);

                            LocalApSupplier apSupplier = apSupplierHome.findBySplSupplierCode(cnvDetails.getCnvSplSupplierCode(), AD_CMPNY);
                            apSupplier.addApCanvass(apCanvass);

                            apPurchaseRequisitionLine.addApCanvass(apCanvass);

                            CNV_LN++;
                        }
                    }

                    try {

                        Iterator t = mPrlDetails.getPrlTagList().iterator();

                        LocalInvTag invTag = null;

                        while (t.hasNext()) {
                            InvModTagListDetails tgLstDetails = (InvModTagListDetails) t.next();

                            if (tgLstDetails.getTgCode() == null) {
                                invTag = invTagHome.create(tgLstDetails.getTgPropertyCode(), tgLstDetails.getTgSerialNumber(), null, tgLstDetails.getTgExpiryDate(), tgLstDetails.getTgSpecs(), AD_CMPNY, tgLstDetails.getTgTransactionDate(), tgLstDetails.getTgType());

                                invTag.setApPurchaseRequisitionLine(apPurchaseRequisitionLine);
                                LocalAdUser adUser = null;
                                try {
                                    adUser = adUserHome.findByUsrName(tgLstDetails.getTgCustodian(), AD_CMPNY);
                                } catch (FinderException ex) {

                                }
                                invTag.setAdUser(adUser);
                            }
                        }

                    } catch (Exception ex) {

                    }
                }

            } else {

                Iterator y = prlList.iterator();
                while (y.hasNext()) {
                    ApModPurchaseRequisitionLineDetails mPrlDetails = (ApModPurchaseRequisitionLineDetails) y.next();
                    LocalApPurchaseRequisitionLine apPurchaseRequisitionLine = apPurchaseRequisitionLineHome.findByPrimaryKey(mPrlDetails.getPrlCode());

                    // remove all inv tag inside PR line
                    Collection invTags = apPurchaseRequisitionLine.getInvTags();

                    Iterator x = invTags.iterator();

                    while (x.hasNext()) {

                        LocalInvTag invTag = (LocalInvTag) x.next();

                        x.remove();

                        // invTag.entityRemove();
                        em.remove(invTag);
                    }

                    for (Object o : mPrlDetails.getPrlTagList()) {
                        InvModTagListDetails tgLstDetails = (InvModTagListDetails) o;
                        LocalInvTag invTag = invTagHome.create(tgLstDetails.getTgPropertyCode(), tgLstDetails.getTgSerialNumber(), null, tgLstDetails.getTgExpiryDate(), tgLstDetails.getTgSpecs(), AD_CMPNY, tgLstDetails.getTgTransactionDate(), tgLstDetails.getTgType());

                        invTag.setApPurchaseRequisitionLine(apPurchaseRequisitionLine);
                        LocalAdUser adUser = null;
                        try {
                            adUser = adUserHome.findByUsrName(tgLstDetails.getTgCustodian(), AD_CMPNY);
                        } catch (FinderException ex) {

                        }
                        invTag.setAdUser(adUser);

                    }
                }
            }

            // check if all lines' canvass are complete

            Collection apCheckPurchaseRequisitionLines = apPurchaseRequisition.getApPurchaseRequisitionLines();

            for (Object apCheckPurchaseRequisitionLine : apCheckPurchaseRequisitionLines) {
                LocalApPurchaseRequisitionLine apCheckApPurchaseRequisitionLine = (LocalApPurchaseRequisitionLine) apCheckPurchaseRequisitionLine;

                Collection apCanvasses = apCanvassHome.findByPrlCodeAndCnvPo(apCheckApPurchaseRequisitionLine.getPrlCode(), EJBCommon.TRUE, AD_CMPNY);
                for (Object canvass : apCanvasses) {
                    LocalApCanvass apCanvass = (LocalApCanvass) canvass;
                    TOTAL_AMOUNT += apCanvass.getCnvAmount();
                }
                if (apCheckApPurchaseRequisitionLine.getApCanvasses().size() == 0 || (apCanvassHome.findByPrlCodeAndCnvPo(apCheckApPurchaseRequisitionLine.getPrlCode(), EJBCommon.TRUE, AD_CMPNY)).size() == 0) {
                    throw new GlobalRecordInvalidException(String.valueOf(apCheckApPurchaseRequisitionLine.getPrlLine()));
                }
            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            // set purchase requisition approval status

            String PR_APPRVL_STATUS = null;

            if (!isDraft) {

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(AD_CMPNY);

                // check if ap voucher approval is enabled

                if (adApproval.getAprEnableApCanvass() == EJBCommon.FALSE) {

                    PR_APPRVL_STATUS = "N/A";

                    System.out.print("APPROVAL SETUP FALSE");

                } else {
                    System.out.print("APPROVAL SETUP YES");

                    LocalAdUser adUser = adUserHome.findByUsrName(details.getPrLastModifiedBy(), AD_CMPNY);

                    PR_APPRVL_STATUS = apApprovalController.getApprovalStatus(adUser.getUsrDept(), apPurchaseRequisition.getPrLastModifiedBy(), adUser.getUsrDescription(), "AP CANVASS", apPurchaseRequisition.getPrCode(), apPurchaseRequisition.getPrNumber(), apPurchaseRequisition.getPrDate(), TOTAL_AMOUNT, AD_BRNCH, AD_CMPNY);
                }

                apPurchaseRequisition.setPrCanvassApprovalStatus(PR_APPRVL_STATUS);

                // set post purchase order

                if (PR_APPRVL_STATUS.equals("N/A")) {

                    apPurchaseRequisition.setPrCanvassPosted(EJBCommon.TRUE);
                    apPurchaseRequisition.setPrCanvassPosted(EJBCommon.TRUE);
                    apPurchaseRequisition.setPrCanvassPostedBy(apPurchaseRequisition.getPrLastModifiedBy());
                    apPurchaseRequisition.setPrCanvassDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
                }
            }

            return apPurchaseRequisition.getPrCode();

        } catch (GlobalRecordAlreadyDeletedException | GlobalRecordInvalidException |
                 GlobalNoApprovalApproverFoundException | GlobalNoApprovalRequesterFoundException |
                 GlobalInvItemLocationNotFoundException | GlobalTransactionAlreadyVoidException |
                 GlobalTransactionAlreadyPostedException | GlobalTransactionAlreadyPendingException |
                 GlobalTransactionAlreadyApprovedException | GlobalConversionDateNotExistException |
                 GlobalDocumentNumberNotUniqueException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // TODO: saveApPrEntry

    public Integer saveApPrEntry(ApPurchaseRequisitionDetails details, String RV_AD_NTFD_USR1, String TC_NM, String FC_NM, ArrayList prlList, boolean isDraft, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalInvItemLocationNotFoundException, GlobalSupplierItemInvalidException, GlobalRecordInvalidException, GlobalNoApprovalApproverFoundException, GlobalNoApprovalRequesterFoundException, GlobalInsufficientBudgetQuantityException, GlobalSendEmailMessageException {

        Debug.print("ApPurchaseRequisitionEntryControllerBean saveApPrEntry");

        LocalApPurchaseRequisition apPurchaseRequisition = null;

        ArrayList list = new ArrayList();


        try {

            // validate if purchase requisition is already deleted

            try {

                if (details.getPrCode() != null) {

                    apPurchaseRequisition = apPurchaseRequisitionHome.findByPrimaryKey(details.getPrCode());
                }

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if purchase requisition is already posted, void, approved or pending

            if (details.getPrCode() != null && details.getPrVoid() == EJBCommon.FALSE) {

                if (apPurchaseRequisition.getPrApprovalStatus() != null) {

                    if (apPurchaseRequisition.getPrApprovalStatus().equals("APPROVED") || apPurchaseRequisition.getPrApprovalStatus().equals("N/A")) {

                        throw new GlobalTransactionAlreadyApprovedException();

                    } else if (apPurchaseRequisition.getPrApprovalStatus().equals("PENDING")) {

                        throw new GlobalTransactionAlreadyPendingException();
                    }
                }

                if (apPurchaseRequisition.getPrPosted() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyPostedException();

                } else if (apPurchaseRequisition.getPrVoid() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyVoidException();
                }
            }

            // purchase requisition void

            if (details.getPrCode() != null && details.getPrVoid() == EJBCommon.TRUE) {

                apPurchaseRequisition.setPrVoid(EJBCommon.TRUE);
                apPurchaseRequisition.setPrLastModifiedBy(details.getPrLastModifiedBy());
                apPurchaseRequisition.setPrDateLastModified(details.getPrDateLastModified());

                return apPurchaseRequisition.getPrCode();
            }

            // validate if document number is unique document number is automatic then set
            // next sequence

            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

            if (details.getPrCode() == null) {

                try {

                    adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AP PURCHASE REQUISITION", AD_CMPNY);

                } catch (FinderException ex) {
                }

                try {

                    adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, AD_CMPNY);

                } catch (FinderException ex) {
                }

                LocalApPurchaseRequisition apExistingPurchaseRequisition = null;

                try {

                    apExistingPurchaseRequisition = apPurchaseRequisitionHome.findByPrNumberAndBrCode(details.getPrNumber(), AD_BRNCH, AD_CMPNY);

                } catch (FinderException ex) {
                }

                if (apExistingPurchaseRequisition != null) {

                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (details.getPrNumber() == null || details.getPrNumber().trim().length() == 0)) {

                    while (true) {

                        if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                            try {

                                apPurchaseRequisitionHome.findByPrNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, AD_CMPNY);
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                            } catch (FinderException ex) {

                                details.setPrNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                break;
                            }

                        } else {

                            try {

                                apPurchaseRequisitionHome.findByPrNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, AD_CMPNY);
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                            } catch (FinderException ex) {

                                details.setPrNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                break;
                            }
                        }
                    }
                }

            } else {

                LocalApPurchaseRequisition apExistingPurchaseRequisition = null;

                try {

                    apExistingPurchaseRequisition = apPurchaseRequisitionHome.findByPrNumberAndBrCode(details.getPrNumber(), AD_BRNCH, AD_CMPNY);

                } catch (FinderException ex) {
                }

                if (apExistingPurchaseRequisition != null && !apExistingPurchaseRequisition.getPrCode().equals(details.getPrCode())) {

                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (apPurchaseRequisition.getPrNumber() != details.getPrNumber() && (details.getPrNumber() == null || details.getPrNumber().trim().length() == 0)) {

                    details.setPrNumber(apPurchaseRequisition.getPrNumber());
                }
            }

            // validate if conversion date exists

            try {

                if (details.getPrConversionDate() != null) {

                    LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);
                    LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(), details.getPrConversionDate(), AD_CMPNY);

                    details.setPrConversionRate(glFunctionalCurrencyRate.getFrXToUsd());
                }

            } catch (FinderException ex) {

                throw new GlobalConversionDateNotExistException();
            }

            // get all notified users

            LocalAdUser userName1 = null;

            try {

                userName1 = adUserHome.findByUsrName(RV_AD_NTFD_USR1, AD_CMPNY);

            } catch (FinderException ex) {

            }

            boolean isRecalculate = true;

            // create purchase requisition

            if (details.getPrCode() == null) {

                apPurchaseRequisition = apPurchaseRequisitionHome.create(details.getPrDescription(), details.getPrNumber(), details.getPrDate(), details.getPrDeliveryPeriod(), details.getPrReferenceNumber(), details.getPrConversionDate(), details.getPrConversionRate(), details.getPrApprovalStatus(), EJBCommon.FALSE, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, null, null, details.getPrCreatedBy(), details.getPrDateCreated(), details.getPrLastModifiedBy(), details.getPrDateLastModified(), details.getPrApprovedRejectedBy(), details.getPrDateApprovedRejected(), details.getPrPostedBy(), details.getPrDatePosted(), null, null, null, null, details.getPrTagStatus(), details.getPrType(), userName1 != null ? userName1.getUsrCode() : null, details.getPrSchedule(), details.getPrNextRunDate(), details.getPrLastRunDate(), AD_BRNCH, AD_CMPNY);

                // ADD DEPARTMENT
                String department = null;
                try {
                    LocalAdUser adUser = adUserHome.findByUsrName(details.getPrCreatedBy(), AD_CMPNY);
                    department = adUser.getUsrDept();
                } catch (FinderException ex) {
                }

                apPurchaseRequisition.setPrDepartment(department);


            } else {

                // check if critical fields are changed

                if (!apPurchaseRequisition.getApTaxCode().getTcName().equals(TC_NM) || prlList.size() != apPurchaseRequisition.getApPurchaseRequisitionLines().size()) {

                    isRecalculate = true;

                } else if (prlList.size() == apPurchaseRequisition.getApPurchaseRequisitionLines().size()) {

                    Iterator ilIter = apPurchaseRequisition.getApPurchaseRequisitionLines().iterator();
                    Iterator ilListIter = prlList.iterator();

                    while (ilIter.hasNext()) {

                        LocalApPurchaseRequisitionLine apPurchaseRequisitionLine = (LocalApPurchaseRequisitionLine) ilIter.next();
                        ApModPurchaseRequisitionLineDetails mdetails = (ApModPurchaseRequisitionLineDetails) ilListIter.next();

                        if (!apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiName().equals(mdetails.getPrlIlIiName()) || !apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiDescription().equals(mdetails.getPrlIlIiDescription()) || !apPurchaseRequisitionLine.getInvItemLocation().getInvLocation().getLocName().equals(mdetails.getPrlIlLocName()) || !apPurchaseRequisitionLine.getInvUnitOfMeasure().getUomName().equals(mdetails.getPrlUomName()) || apPurchaseRequisitionLine.getPrlQuantity() != mdetails.getPrlQuantity() || apPurchaseRequisitionLine.getPrlAmount() != mdetails.getPrlAmount()) {

                            // isRecalculate = true;

                        }
                    }

                } else {

                    // isRecalculate = false;

                }

                apPurchaseRequisition.setPrDescription(details.getPrDescription());
                apPurchaseRequisition.setPrDate(details.getPrDate());
                apPurchaseRequisition.setPrDeliveryPeriod(details.getPrDeliveryPeriod());
                apPurchaseRequisition.setPrNumber(details.getPrNumber());
                apPurchaseRequisition.setPrReferenceNumber(details.getPrReferenceNumber());
                apPurchaseRequisition.setPrVoid(details.getPrVoid());
                apPurchaseRequisition.setPrConversionDate(details.getPrConversionDate());
                apPurchaseRequisition.setPrConversionRate(details.getPrConversionRate());
                apPurchaseRequisition.setPrTagStatus(details.getPrTagStatus());
                apPurchaseRequisition.setPrType(details.getPrType());
                apPurchaseRequisition.setPrAdUserName1(userName1 != null ? userName1.getUsrCode() : null);
                apPurchaseRequisition.setPrSchedule(details.getPrSchedule());
                apPurchaseRequisition.setPrNextRunDate(details.getPrNextRunDate());
                apPurchaseRequisition.setPrLastModifiedBy(details.getPrLastModifiedBy());
                apPurchaseRequisition.setPrDateLastModified(details.getPrDateLastModified());
                apPurchaseRequisition.setPrCanvassReasonForRejection(null);
                apPurchaseRequisition.setPrReasonForRejection(null);
            }

            LocalApTaxCode apTaxCode = apTaxCodeHome.findByTcName(TC_NM, AD_CMPNY);
            apPurchaseRequisition.setApTaxCode(apTaxCode);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);
            apPurchaseRequisition.setGlFunctionalCurrency(glFunctionalCurrency);

            double TOTAL_AMOUNT = 0d;

            if (isRecalculate) {
                // remove purchase requisition lines

                Collection apPurchaseRequisitionLines = apPurchaseRequisition.getApPurchaseRequisitionLines();

                Iterator i = apPurchaseRequisitionLines.iterator();

                while (i.hasNext()) {

                    LocalApPurchaseRequisitionLine apPurchaseRequisitionLine = (LocalApPurchaseRequisitionLine) i.next();

                    // remove all inv tag inside PR line
                    Collection invTags = apPurchaseRequisitionLine.getInvTags();

                    Iterator x = invTags.iterator();

                    while (x.hasNext()) {

                        LocalInvTag invTag = (LocalInvTag) x.next();

                        x.remove();

                        // invTag.entityRemove();
                        em.remove(invTag);
                    }

                    i.remove();

                    // apPurchaseRequisitionLine.entityRemove();
                    em.remove(apPurchaseRequisitionLine);
                }

                // add new purchase requisition lines

                i = prlList.iterator();

                LocalInvItemLocation invItemLocation = null;

                while (i.hasNext()) {

                    ApModPurchaseRequisitionLineDetails mPrlDetails = (ApModPurchaseRequisitionLineDetails) i.next();

                    // save purchase requisition line, qty and amount based on the retrieved unit
                    // cost

                    if (mPrlDetails.getPrlAmount() == 0) {
                        mPrlDetails.setPrlAmount(invItemHome.findByIiName(mPrlDetails.getPrlIlIiName(), AD_CMPNY).getIiUnitCost() * mPrlDetails.getPrlQuantity());
                    }

                    LocalApPurchaseRequisitionLine apPurchaseRequisitionLine = apPurchaseRequisitionLineHome.create(mPrlDetails.getPrlLine(), mPrlDetails.getPrlQuantity(), mPrlDetails.getPrlAmount(), mPrlDetails.getPrlRemarks(), AD_CMPNY);
                    apPurchaseRequisition.addApPurchaseRequisitionLine(apPurchaseRequisitionLine);


                    TOTAL_AMOUNT += mPrlDetails.getPrlAmount();
                    try {

                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(mPrlDetails.getPrlIlLocName(), mPrlDetails.getPrlIlIiName(), AD_CMPNY);

                    } catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(mPrlDetails.getPrlLine()));
                    }

                    invItemLocation.addApPurchaseRequisitionLine(apPurchaseRequisitionLine);

                    LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(mPrlDetails.getPrlUomName(), AD_CMPNY);
                    invUnitOfMeasure.addApPurchaseRequisitionLine(apPurchaseRequisitionLine);

                    short CNV_LN = 0;
                    double II_UNT_CST = mPrlDetails.getPrlAmount();
                    double CNV_QTY = mPrlDetails.getPrlQuantity();

                    if (mPrlDetails.getRegenerateCanvass()) {

                        if (invItemLocation.getInvItem().getApSupplier() != null) {

                            // generate canvass lines for purchase requisition line
                            CNV_LN++;

                            // create canvass line
                            LocalApCanvass apCanvass = apCanvassHome.create(CNV_LN, null, CNV_QTY, II_UNT_CST, CNV_QTY * II_UNT_CST, EJBCommon.TRUE, AD_CMPNY);

                            LocalApSupplier apSupplier = invItemLocation.getInvItem().getApSupplier();
                            apSupplier.addApCanvass(apCanvass);

                            apPurchaseRequisitionLine.addApCanvass(apCanvass);
                        }

                        CNV_LN++;

                        // create canvass lines for other suppliers of item
                        Collection apPurchaseOrderLines = null;

                        try {

                            apPurchaseOrderLines = apPurchaseOrderLineHome.findByPlIlCodeAndPoReceivingAndPoPostedAndBrCode(apPurchaseRequisitionLine.getInvItemLocation().getIlCode(), EJBCommon.TRUE, EJBCommon.TRUE, AD_BRNCH, AD_CMPNY);

                        } catch (FinderException ex) {

                        }

                        if (!apPurchaseOrderLines.isEmpty() && apPurchaseOrderLines.size() > 0) {

                            for (Object purchaseOrderLine : apPurchaseOrderLines) {

                                LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) purchaseOrderLine;

                                // continue if next purchase order line has existing supplier in canvass

                                Collection apCanvasses = apCanvassHome.findByPrlCodeAndSplSupplierCode(apPurchaseRequisitionLine.getPrlCode(), apPurchaseOrderLine.getApPurchaseOrder().getApSupplier().getSplSupplierCode(), AD_CMPNY);

                                if (!apCanvasses.isEmpty()) continue;

                                // convert purchase order line unit cost
                                double POL_UNT_CST = this.convertCostByUom(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiName(), apPurchaseOrderLine.getInvUnitOfMeasure().getUomName(), apPurchaseOrderLine.getPlUnitCost(), false, AD_CMPNY);
                                POL_UNT_CST = this.convertCostByUom(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiName(), apPurchaseRequisitionLine.getInvUnitOfMeasure().getUomName(), POL_UNT_CST, true, AD_CMPNY);

                                // create canvass line
                                LocalApCanvass apCanvass = apCanvassHome.create(CNV_LN, null, apPurchaseRequisitionLine.getPrlQuantity(), POL_UNT_CST, apPurchaseRequisitionLine.getPrlQuantity() * POL_UNT_CST, EJBCommon.FALSE, AD_CMPNY);

                                if (CNV_LN == 1) {

                                    apCanvass.setCnvQuantity(CNV_QTY);
                                    apCanvass.setCnvAmount(II_UNT_CST);
                                    apCanvass.setCnvPo(EJBCommon.TRUE);
                                }

                                LocalApSupplier apSupplier = apPurchaseOrderLine.getApPurchaseOrder().getApSupplier();
                                apSupplier.addApCanvass(apCanvass);

                                apPurchaseRequisitionLine.addApCanvass(apCanvass);

                                CNV_LN++;
                            }
                        }

                    } else {

                        CNV_LN++;
                        ArrayList cnvList = mPrlDetails.getCnvList();

                        for (Object o : cnvList) {

                            double prQuantity = apPurchaseRequisitionLine.getPrlQuantity();

                            ApModCanvassDetails cnvDetails = (ApModCanvassDetails) o;

                            // create canvass line
                            LocalApCanvass apCanvass = apCanvassHome.create(CNV_LN, cnvDetails.getCnvRemarks(),
                                    // cnvDetails.getCnvQuantity(),
                                    prQuantity, cnvDetails.getCnvUnitCost(), EJBCommon.roundIt(
                                            // cnvDetails.getCnvUnitCost() * cnvDetails.getCnvQuantity(),
                                            cnvDetails.getCnvUnitCost() * prQuantity, this.getGlFcPrecisionUnit(AD_CMPNY)), cnvDetails.getCnvPo(), AD_CMPNY);

                            LocalApSupplier apSupplier = apSupplierHome.findBySplSupplierCode(cnvDetails.getCnvSplSupplierCode(), AD_CMPNY);
                            apSupplier.addApCanvass(apCanvass);

                            apPurchaseRequisitionLine.addApCanvass(apCanvass);

                            CNV_LN++;
                        }
                    }
                    // TODO: add new inv Tag
                    if (mPrlDetails.getTraceMisc() == 1) {
                        try {
                            Iterator t = mPrlDetails.getPrlTagList().iterator();

                            LocalInvTag invTag = null;
                            while (t.hasNext()) {
                                InvModTagListDetails tgLstDetails = (InvModTagListDetails) t.next();

                                if (tgLstDetails.getTgCode() == null) {

                                    invTag = invTagHome.create(tgLstDetails.getTgPropertyCode(), tgLstDetails.getTgSerialNumber(), null, tgLstDetails.getTgExpiryDate(), tgLstDetails.getTgSpecs(), AD_CMPNY, tgLstDetails.getTgTransactionDate(), tgLstDetails.getTgType());

                                    invTag.setApPurchaseRequisitionLine(apPurchaseRequisitionLine);
                                    LocalAdUser adUser = null;
                                    try {
                                        adUser = adUserHome.findByUsrName(tgLstDetails.getTgCustodian(), AD_CMPNY);
                                    } catch (FinderException ex) {

                                    }
                                    invTag.setAdUser(adUser);

                                }
                            }

                        } catch (Exception ex) {

                        }
                    }
                }

            } else {

                Iterator y = prlList.iterator();

                while (y.hasNext()) {
                    ApModPurchaseRequisitionLineDetails mPrlDetails = (ApModPurchaseRequisitionLineDetails) y.next();

                    LocalApPurchaseRequisitionLine apPurchaseRequisitionLine = apPurchaseRequisitionLineHome.findByPrimaryKey(mPrlDetails.getPrlCode());

                    // remove all inv tag inside PR line
                    Collection invTags = apPurchaseRequisitionLine.getInvTags();

                    Iterator x = invTags.iterator();

                    while (x.hasNext()) {

                        LocalInvTag invTag = (LocalInvTag) x.next();

                        x.remove();

                        // invTag.entityRemove();
                        em.remove(invTag);
                    }


                    if (mPrlDetails.getTraceMisc() == 1) {
                        for (Object o : mPrlDetails.getPrlTagList()) {
                            InvModTagListDetails tgLstDetails = (InvModTagListDetails) o;

                            LocalInvTag invTag = invTagHome.create(tgLstDetails.getTgPropertyCode(), tgLstDetails.getTgSerialNumber(), null, tgLstDetails.getTgExpiryDate(), tgLstDetails.getTgSpecs(), AD_CMPNY, tgLstDetails.getTgTransactionDate(), tgLstDetails.getTgType());

                            invTag.setApPurchaseRequisitionLine(apPurchaseRequisitionLine);
                            LocalAdUser adUser = null;
                            try {
                                adUser = adUserHome.findByUsrName(tgLstDetails.getTgCustodian(), AD_CMPNY);
                            } catch (FinderException ex) {

                            }
                            invTag.setAdUser(adUser);

                        }
                    }
                }
            }

            Collection apCheckPurchaseRequisitionLines = apPurchaseRequisition.getApPurchaseRequisitionLines();

            for (Object apCheckPurchaseRequisitionLine : apCheckPurchaseRequisitionLines) {
                LocalApPurchaseRequisitionLine apCheckApPurchaseRequisitionLine = (LocalApPurchaseRequisitionLine) apCheckPurchaseRequisitionLine;

                // validate if enough budget quantity
                if (!isDraft) {
                    try {
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy");
                        LocalAdUser adUser = adUserHome.findByUsrName(apCheckApPurchaseRequisitionLine.getApPurchaseRequisition().getPrCreatedBy(), AD_CMPNY);
                        LocalAdLookUpValue adLookUpValue = adLookUpValueHome.findByLuNameAndLvName("AD DEPARTMENT", adUser.getUsrDept(), AD_CMPNY);
                        LocalInvTransactionalBudget invTransactionalBudget = invTransactionalBudgetHome.findByTbItemNameAndTbDeptAndTbYear(apCheckApPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiName(), adLookUpValue.getLvCode(), Integer.parseInt(sdf.format(apCheckApPurchaseRequisitionLine.getApPurchaseRequisition().getPrDate())), AD_CMPNY);
                        sdf = new java.text.SimpleDateFormat("MM");
                        String month = sdf.format(apCheckApPurchaseRequisitionLine.getApPurchaseRequisition().getPrDate());
                        double quantityForCurrentMonth = 0;
                        Debug.print("month" + month);
                        switch (month) {
                            case "01":
                                quantityForCurrentMonth = invTransactionalBudget.getTbQuantityJan();
                                break;
                            case "02":
                                quantityForCurrentMonth = invTransactionalBudget.getTbQuantityFeb();
                                break;
                            case "03":
                                quantityForCurrentMonth = invTransactionalBudget.getTbQuantityMrch();
                                break;
                            case "04":
                                quantityForCurrentMonth = invTransactionalBudget.getTbQuantityAprl();
                                break;
                            case "05":
                                quantityForCurrentMonth = invTransactionalBudget.getTbQuantityMay();
                                break;
                            case "06":
                                quantityForCurrentMonth = invTransactionalBudget.getTbQuantityJun();
                                break;
                            case "07":
                                quantityForCurrentMonth = invTransactionalBudget.getTbQuantityJul();
                                break;
                            case "08":
                                quantityForCurrentMonth = invTransactionalBudget.getTbQuantityAug();
                                break;
                            case "09":
                                quantityForCurrentMonth = invTransactionalBudget.getTbQuantitySep();
                                break;
                            case "10":
                                quantityForCurrentMonth = invTransactionalBudget.getTbQuantityOct();
                                break;
                            case "11":
                                quantityForCurrentMonth = invTransactionalBudget.getTbQuantityNov();
                                break;
                            default:
                                quantityForCurrentMonth = invTransactionalBudget.getTbQuantityDec();
                                break;
                        }


                        if (apCheckApPurchaseRequisitionLine.getPrlQuantity() > quantityForCurrentMonth)
                            throw new GlobalInsufficientBudgetQuantityException(String.valueOf(apCheckApPurchaseRequisitionLine.getPrlLine()));
                    } catch (FinderException ex) {
                    }
                }
            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            LocalAdUser adUser = adUserHome.findByUsrName(apPurchaseRequisition.getPrCreatedBy(), AD_CMPNY);

            // set purchase requisition approval status
            Double ABS_TOTAL_AMOUNT = 0d;
            String PR_APPRVL_STATUS = null;

            if (!isDraft) {

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(AD_CMPNY);

                // check if pr approval is enabled

                if (adApproval.getAprEnableApPurReq() == EJBCommon.FALSE) {

                    PR_APPRVL_STATUS = "N/A";

                } else {

                    // check if pr is self approved

                    LocalAdUser adUser2 = adUserHome.findByUsrName(details.getPrLastModifiedBy(), AD_CMPNY);

                    PR_APPRVL_STATUS = apApprovalController.getApprovalStatus(adUser.getUsrDept(), apPurchaseRequisition.getPrLastModifiedBy(), adUser2.getUsrDescription(), "AP PURCHASE REQUISITION", apPurchaseRequisition.getPrCode(), apPurchaseRequisition.getPrNumber(), apPurchaseRequisition.getPrDate(), TOTAL_AMOUNT, AD_BRNCH, AD_CMPNY);
                }

                apPurchaseRequisition.setPrApprovalStatus(PR_APPRVL_STATUS);

                // set post purchase order

                if (PR_APPRVL_STATUS.equals("N/A")) {

                    apPurchaseRequisition.setPrPosted(EJBCommon.TRUE);
                    apPurchaseRequisition.setPrGenerated(EJBCommon.FALSE);
                    apPurchaseRequisition.setPrPostedBy(apPurchaseRequisition.getPrLastModifiedBy());
                    apPurchaseRequisition.setPrDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
                }
            }

            return apPurchaseRequisition.getPrCode();

        } catch (GlobalRecordAlreadyDeletedException | GlobalNoApprovalRequesterFoundException |
                 GlobalNoApprovalApproverFoundException | GlobalInsufficientBudgetQuantityException |
                 GlobalInvItemLocationNotFoundException | GlobalTransactionAlreadyVoidException |
                 GlobalTransactionAlreadyPostedException | GlobalTransactionAlreadyPendingException |
                 GlobalTransactionAlreadyApprovedException | GlobalConversionDateNotExistException |
                 GlobalDocumentNumberNotUniqueException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteApPrEntry(Integer PR_CODE, String AD_USR, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException {

        Debug.print("ApPurchaseRequisitionEntryControllerBean deleteApPrEntry");

        try {

            LocalApPurchaseRequisition apPurchaseRequisition = apPurchaseRequisitionHome.findByPrimaryKey(PR_CODE);

            adDeleteAuditTrailHome.create("AP PURCHASE REQUISITION", apPurchaseRequisition.getPrDate(), apPurchaseRequisition.getPrNumber(), apPurchaseRequisition.getPrReferenceNumber(), 0d, AD_USR, new Date(), AD_CMPNY);

            em.remove(apPurchaseRequisition);

        } catch (FinderException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlobalRecordAlreadyDeletedException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer generateCanvassToPo(Integer PR_CODE, String CRTD_BY, Integer AD_BRNCH, Integer AD_CMPNY) {
        LocalApPurchaseOrder apPurchaseOrder;

        Debug.print("ApPurchaseRequisitionEntryControllerBean generateCanvassToPo");

        try {

            LocalApPurchaseRequisition apPurchaseRequisition = null;

            try {

                apPurchaseRequisition = apPurchaseRequisitionHome.findByPrimaryKey(PR_CODE);

            } catch (FinderException ex) {

            }

            Date CURR_DT = EJBCommon.getGcCurrentDateWoTime().getTime();

            String PO_NMBR = getAdDocumentSequence(AD_BRNCH, AD_CMPNY, null, null, null);

            String SPL_SPPLR_CODE = getSupplierCodeFromApCanvass(PR_CODE, AD_CMPNY);


            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSSS");
            String PO_DESC = "GENERATED PO " + formatter.format(new java.util.Date());
            // ADDED DESCRIPTION FROM PR TO PO
            if (!apPurchaseRequisition.getPrDescription().isEmpty() || apPurchaseRequisition.getPrDescription() != null) {
                PO_DESC = apPurchaseRequisition.getPrDescription();
            }

            apPurchaseRequisition.setPrGenerated(EJBCommon.TRUE);
            apPurchaseOrder = apPurchaseOrderHome.create(EJBCommon.FALSE, null, CURR_DT, apPurchaseRequisition.getPrDeliveryPeriod(), PO_NMBR, apPurchaseRequisition.getPrNumber(), null, PO_DESC, null, null, apPurchaseRequisition.getPrConversionDate(), apPurchaseRequisition.getPrConversionRate(), 0d, EJBCommon.FALSE, EJBCommon.FALSE, null, null, EJBCommon.FALSE, null, CRTD_BY, CURR_DT, CRTD_BY, CURR_DT, null, null, null, null, EJBCommon.FALSE, 0d, 0d, 0d, 0d, 0d, AD_BRNCH, AD_CMPNY);

            // ADD DEPARTMENT
            String DEPARTMENT = null;
            try {
                LocalAdUser adUser = adUserHome.findByUsrName(CRTD_BY, AD_CMPNY);
                DEPARTMENT = adUser.getUsrDept();
            } catch (FinderException ex) {
                Debug.print("NO user found, no department assigned");
            }

            apPurchaseOrder.setPoDepartment(DEPARTMENT);


            LocalApSupplier apSupplier = apSupplierHome.findBySplSupplierCode(SPL_SPPLR_CODE, AD_CMPNY);


            apPurchaseOrder.setPoApprovedRejectedBy(apPurchaseRequisition.getPrCanvassApprovedRejectedBy());
            apPurchaseOrder.setApSupplier(apSupplier);

            LocalAdPaymentTerm adPaymentTerm = apSupplier.getAdPaymentTerm();
            apPurchaseOrder.setAdPaymentTerm(adPaymentTerm);

            LocalGlFunctionalCurrency glFunctionalCurrency = apPurchaseRequisition.getGlFunctionalCurrency();
            apPurchaseOrder.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalApTaxCode apTaxCode = apPurchaseRequisition.getApTaxCode();
            apPurchaseOrder.setApTaxCode(apTaxCode);


            Collection apGenPoLines = apCanvassHome.findByPrCodeAndCnvPo(PR_CODE, EJBCommon.TRUE, AD_CMPNY);

            Iterator i = apGenPoLines.iterator();


            short PL_LN = 1;
            int PO_COUNT = 1;
            double TTL_AMNT = 0;

            while (i.hasNext()) {

                LocalApCanvass apCanvass = (LocalApCanvass) i.next();


                // add purchase order line

                double PL_AMNT = EJBCommon.roundIt(apCanvass.getCnvQuantity() * apCanvass.getCnvUnitCost(), this.getGlFcPrecisionUnit(AD_CMPNY));

                LocalApPurchaseOrderLine apPurchaseOrderLine = apPurchaseOrderLineHome.create(PL_LN, apCanvass.getCnvQuantity(), apCanvass.getCnvUnitCost(), PL_AMNT, null, null, null, 0d, 0d, null, 0, 0, 0, 0, 0, AD_CMPNY);

                TTL_AMNT += PL_AMNT;

                // REMARKS ADDED
                if (!apCanvass.getApPurchaseRequisitionLine().getPrlRemarks().isEmpty() || apCanvass.getApPurchaseRequisitionLine().getPrlRemarks() != null) {

                    apPurchaseOrderLine.setPlRemarks(apCanvass.getApPurchaseRequisitionLine().getPrlRemarks());
                }

                apPurchaseOrder.addApPurchaseOrderLine(apPurchaseOrderLine);

                LocalInvUnitOfMeasure invUnitOfMeasure = apCanvass.getApPurchaseRequisitionLine().getInvUnitOfMeasure();
                invUnitOfMeasure.addApPurchaseOrderLine(apPurchaseOrderLine);

                LocalInvItemLocation invItemLocation = apCanvass.getApPurchaseRequisitionLine().getInvItemLocation();
                invItemLocation.addApPurchaseOrderLine(apPurchaseOrderLine);

                try {


                    for (Object o : apCanvass.getApPurchaseRequisitionLine().getInvTags()) {
                        LocalInvTag invPlTag = (LocalInvTag) o;

                        LocalInvTag invTag = invTagHome.create(invPlTag.getTgPropertyCode(), invPlTag.getTgSerialNumber(), null, invPlTag.getTgExpiryDate(), invPlTag.getTgSpecs(), AD_CMPNY, invPlTag.getTgTransactionDate(), invPlTag.getTgType());

                        invTag.setApPurchaseOrderLine(apPurchaseOrderLine);

                        invTag.setAdUser(invPlTag.getAdUser());

                    }

                } catch (Exception ex) {

                }

                PL_LN++;
            }

            apPurchaseOrder.setPoTotalAmount(TTL_AMNT);

            return PO_COUNT;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }


    }

    private String getAdDocumentSequence(Integer AD_BRNCH, Integer AD_CMPNY, String PO_NMBR, LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment, LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment) {


        try {

            adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AP PURCHASE ORDER", AD_CMPNY);

        } catch (FinderException ex) {
        }

        try {

            adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, AD_CMPNY);

        } catch (FinderException ex) {
        }

        if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A') {

            while (true) {

                if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                    try {

                        apPurchaseOrderHome.findByPoDocumentNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, AD_CMPNY);
                        adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                    } catch (FinderException ex) {

                        PO_NMBR = adDocumentSequenceAssignment.getDsaNextSequence();
                        adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                        break;
                    }

                } else {

                    try {

                        apPurchaseOrderHome.findByPoDocumentNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, AD_CMPNY);
                        adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                    } catch (FinderException ex) {

                        PO_NMBR = adBranchDocumentSequenceAssignment.getBdsNextSequence();
                        adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                        break;
                    }
                }
            }
        }
        return PO_NMBR;
    }

    public Integer generateApPo(Integer PR_CODE, String CRTD_BY, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean generateApPo");

        LocalApPurchaseOrder apPurchaseOrder = null;

        try {

            LocalApPurchaseRequisition apPurchaseRequisition = null;

            try {

                apPurchaseRequisition = apPurchaseRequisitionHome.findByPrimaryKey(PR_CODE);

            } catch (FinderException ex) {

            }

            Date CURR_DT = EJBCommon.getGcCurrentDateWoTime().getTime();

            Collection apGenPoLines = apCanvassHome.findByPrCodeAndCnvPo(PR_CODE, EJBCommon.TRUE, AD_CMPNY);

            Iterator i = apGenPoLines.iterator();

            String SPL_SPPLR_CODE = null;
            short PL_LN = 1;
            int PO_COUNT = 0;

            while (i.hasNext()) {

                LocalApCanvass apCanvass = (LocalApCanvass) i.next();

                LocalApSupplier apSupplier = apCanvass.getApSupplier();

                if (!apSupplier.getSplSupplierCode().equals(SPL_SPPLR_CODE)) {

                    SPL_SPPLR_CODE = apSupplier.getSplSupplierCode();
                    PL_LN = 1;

                    String PO_NMBR = null;

                    // generate document number

                    LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
                    LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

                    try {

                        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AP PURCHASE ORDER", AD_CMPNY);

                    } catch (FinderException ex) {
                    }

                    try {

                        adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, AD_CMPNY);

                    } catch (FinderException ex) {
                    }

                    if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A') {

                        while (true) {

                            if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                                try {

                                    apPurchaseOrderHome.findByPoDocumentNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, AD_CMPNY);
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                                } catch (FinderException ex) {

                                    PO_NMBR = adDocumentSequenceAssignment.getDsaNextSequence();
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                    break;
                                }

                            } else {

                                try {

                                    apPurchaseOrderHome.findByPoDocumentNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, AD_CMPNY);
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                                } catch (FinderException ex) {

                                    PO_NMBR = adBranchDocumentSequenceAssignment.getBdsNextSequence();
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                    break;
                                }
                            }
                        }
                    }

                    // create new purchase order

                    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSSS");

                    String PO_DESC = "GENERATED PO " + formatter.format(new java.util.Date());

                    // ADDED DESCRIPTION FROM PR TO PO
                    if (!apPurchaseRequisition.getPrDescription().isEmpty() || apPurchaseRequisition.getPrDescription() != null) {
                        PO_DESC = apPurchaseRequisition.getPrDescription();
                    }

                    apPurchaseRequisition.setPrGenerated(EJBCommon.TRUE);
                    apPurchaseOrder = apPurchaseOrderHome.create(EJBCommon.FALSE, null, CURR_DT, apPurchaseRequisition.getPrDeliveryPeriod(), PO_NMBR, apPurchaseRequisition.getPrNumber(), null, PO_DESC, null, null, apPurchaseRequisition.getPrConversionDate(), apPurchaseRequisition.getPrConversionRate(), 0d, EJBCommon.FALSE, EJBCommon.FALSE, null, null, EJBCommon.FALSE, null, CRTD_BY, CURR_DT, CRTD_BY, CURR_DT, null, null, null, null, EJBCommon.FALSE, 0d, 0d, 0d, 0d, 0d, AD_BRNCH, AD_CMPNY);

                    // ADD DEPARTMENT
                    String department = null;
                    try {
                        LocalAdUser adUser = adUserHome.findByUsrName(CRTD_BY, AD_CMPNY);
                        department = adUser.getUsrDept();
                    } catch (FinderException ex) {

                    }

                    apPurchaseOrder.setPoDepartment(department);


                    apPurchaseOrder.setPoApprovedRejectedBy(apPurchaseRequisition.getPrCanvassApprovedRejectedBy());
                    apPurchaseOrder.setApSupplier(apSupplier);

                    LocalAdPaymentTerm adPaymentTerm = apSupplier.getAdPaymentTerm();
                    apPurchaseOrder.setAdPaymentTerm(adPaymentTerm);

                    LocalGlFunctionalCurrency glFunctionalCurrency = apPurchaseRequisition.getGlFunctionalCurrency();
                    apPurchaseOrder.setGlFunctionalCurrency(glFunctionalCurrency);

                    LocalApTaxCode apTaxCode = apPurchaseRequisition.getApTaxCode();
                    apPurchaseOrder.setApTaxCode(apTaxCode);

                    PO_COUNT++;
                }

                // add purchase order line

                double PL_AMNT = EJBCommon.roundIt(apCanvass.getCnvQuantity() * apCanvass.getCnvUnitCost(), this.getGlFcPrecisionUnit(AD_CMPNY));

                apPurchaseOrder.setPoTotalAmount(PL_AMNT);

                LocalApPurchaseOrderLine apPurchaseOrderLine = apPurchaseOrderLineHome.create(PL_LN, apCanvass.getCnvQuantity(), apCanvass.getCnvUnitCost(), PL_AMNT, null, null, null, 0d, 0d, null, 0, 0, 0, 0, 0, AD_CMPNY);

                // REMARKS ADDED
                if (!apCanvass.getApPurchaseRequisitionLine().getPrlRemarks().isEmpty() || apCanvass.getApPurchaseRequisitionLine().getPrlRemarks() != null) {

                    apPurchaseOrderLine.setPlRemarks(apCanvass.getApPurchaseRequisitionLine().getPrlRemarks());
                }

                apPurchaseOrder.addApPurchaseOrderLine(apPurchaseOrderLine);

                LocalInvUnitOfMeasure invUnitOfMeasure = apCanvass.getApPurchaseRequisitionLine().getInvUnitOfMeasure();
                invUnitOfMeasure.addApPurchaseOrderLine(apPurchaseOrderLine);

                LocalInvItemLocation invItemLocation = apCanvass.getApPurchaseRequisitionLine().getInvItemLocation();
                invItemLocation.addApPurchaseOrderLine(apPurchaseOrderLine);

                try {

                    // Iterator t = apPurchaseOrderLine.getInvTag().iterator();


                    for (Object o : apCanvass.getApPurchaseRequisitionLine().getInvTags()) {
                        LocalInvTag invPlTag = (LocalInvTag) o;

                        LocalInvTag invTag = invTagHome.create(invPlTag.getTgPropertyCode(), invPlTag.getTgSerialNumber(), null, invPlTag.getTgExpiryDate(), invPlTag.getTgSpecs(), AD_CMPNY, invPlTag.getTgTransactionDate(), invPlTag.getTgType());

                        invTag.setApPurchaseOrderLine(apPurchaseOrderLine);

                        invTag.setAdUser(invPlTag.getAdUser());

                    }

                } catch (Exception ex) {

                }

                PL_LN++;
            }

            return PO_COUNT;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getInvUomByIiName(String II_NM, Integer AD_CMPNY) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getInvUomByIiName");

        ArrayList list = new ArrayList();


        try {

            LocalInvItem invItem = null;
            LocalInvUnitOfMeasure invItemUnitOfMeasure = null;

            invItem = invItemHome.findByIiName(II_NM, AD_CMPNY);
            invItemUnitOfMeasure = invItem.getInvUnitOfMeasure();

            Collection invUnitOfMeasures = null;
            for (Object o : invUnitOfMeasureHome.findByUomAdLvClass(invItemUnitOfMeasure.getUomAdLvClass(), AD_CMPNY)) {

                LocalInvUnitOfMeasure invUnitOfMeasure = (LocalInvUnitOfMeasure) o;

                try {
                    LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, invUnitOfMeasure.getUomName(), AD_CMPNY);
                    if (invUnitOfMeasureConversion.getUmcBaseUnit() == EJBCommon.FALSE && invUnitOfMeasureConversion.getUmcConversionFactor() == 1)
                        continue;
                } catch (FinderException ex) {
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

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public double getInvIiUnitCostByIiNameAndUomName(String II_NM, String LOC_NM, String UOM_NM, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getInvIiUnitCostByIiNameAndUomName");


        try {

            LocalInvItemLocation invItemLocation = null;

            try {

                invItemLocation = invItemLocationHome.findByIiNameAndLocName(II_NM, LOC_NM, AD_CMPNY);

            } catch (FinderException ex) {

                return 0;
            }

            LocalInvItem invItem = invItemLocation.getInvItem();

            double II_UNT_CST = 0d;

            II_UNT_CST = invItemLocation.getInvItem().getIiUnitCost();

            if (II_UNT_CST == 0) {

                II_UNT_CST = invItemLocation.getInvItem().getIiUnitCost();
            }

            return this.convertCostByUom(II_NM, UOM_NM, II_UNT_CST, true, AD_CMPNY);

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public double getFrRateByFrNameAndFrDate(String FC_NM, Date CONVERSION_DATE, Integer AD_CMPNY) throws GlobalConversionDateNotExistException {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getFrRateByFrNameAndFrDate");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);

            double CONVERSION_RATE = 1;

            // Get functional currency rate

            if (!FC_NM.equals("USD")) {

                LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glFunctionalCurrency.getFcCode(), CONVERSION_DATE, AD_CMPNY);

                CONVERSION_RATE = glFunctionalCurrencyRate.getFrXToUsd();
            }

            // Get set of book functional currency rate if necessary

            if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), CONVERSION_DATE, AD_CMPNY);

                CONVERSION_RATE = CONVERSION_RATE / glCompanyFunctionalCurrencyRate.getFrXToUsd();
            }

            return CONVERSION_RATE;

        } catch (FinderException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlobalConversionDateNotExistException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfApShowPrCost(Integer AD_CMPNY) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getAdPrfApShowPrCost");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfApShowPrCost();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdApprovalNotifiedUsersByPrCode(Integer PR_CODE, SendEmailDetails sendEmailDetails, Integer AD_CMPNY) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getAdApprovalNotifiedUsersByPrCode");


        ArrayList list = new ArrayList();

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            LocalApPurchaseRequisition apPurchaseRequisition = apPurchaseRequisitionHome.findByPrimaryKey(PR_CODE);

            if (apPurchaseRequisition.getPrPosted() == EJBCommon.TRUE) {

                list.add("DOCUMENT POSTED");
                return list;
            }

            Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode("AP PURCHASE REQUISITION", PR_CODE, AD_CMPNY);

            for (Object approvalQueue : adApprovalQueues) {

                LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) approvalQueue;

                if (adPreference.getPrfAdEnableEmailNotification() == EJBCommon.TRUE) {

                    if (adApprovalQueue.getAqForApproval() == EJBCommon.FALSE) {
                        continue;
                    } else if (adApprovalQueue.getAqApproved() == EJBCommon.TRUE) {
                        continue;
                    }

                    String status = "";

                    status = adApprovalDocumentEmailNotification.sendEmailToApprover(adApprovalQueue, sendEmailDetails);
                    list.add(adApprovalQueue.getAqLevel() + " APPROVER - " + adApprovalQueue.getAdUser().getUsrDescription() + "- " + status);

                } else {
                    list.add(adApprovalQueue.getAqLevel() + " APPROVER - " + adApprovalQueue.getAdUser().getUsrDescription());

                }

            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdApprovalNotifiedUsersByPrCodeCanvass(Integer PR_CODE, Integer AD_CMPNY) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getAdApprovalNotifiedUsersByPrCodeCanvass");

        ArrayList list = new ArrayList();


        try {

            LocalApPurchaseRequisition apPurchaseRequisition = apPurchaseRequisitionHome.findByPrimaryKey(PR_CODE);

            if (apPurchaseRequisition.getPrCanvassPosted() == EJBCommon.TRUE) {

                list.add("DOCUMENT POSTED");
                return list;
            }

            Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode("AP CANVASS", PR_CODE, AD_CMPNY);

            for (Object approvalQueue : adApprovalQueues) {

                LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) approvalQueue;

                list.add(adApprovalQueue.getAdUser().getUsrDescription());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods
    private double convertCostByUom(String II_NM, String UOM_NM, double unitCost, boolean isFromDefault, Integer AD_CMPNY) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean convertCostByUom");

        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NM, AD_CMPNY);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, UOM_NM, AD_CMPNY);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, invItem.getInvUnitOfMeasure().getUomName(), AD_CMPNY);

            if (isFromDefault) {

                return EJBCommon.roundIt(unitCost * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), this.getGlFcPrecisionUnit(AD_CMPNY));

            } else {

                return EJBCommon.roundIt(unitCost * invUnitOfMeasureConversion.getUmcConversionFactor() / invDefaultUomConversion.getUmcConversionFactor(), this.getGlFcPrecisionUnit(AD_CMPNY));
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdUsrAll(Integer AD_CMPNY) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getAdUsrAll");

        ArrayList list = new ArrayList();

        try {

            Collection adUsers = adUserHome.findUsrAll(AD_CMPNY);

            for (Object user : adUsers) {

                LocalAdUser adUser = (LocalAdUser) user;

                list.add(adUser.getUsrName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public String getAdUsrDepartment(String USR_NM, Integer AD_CMPNY) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getAdUsrAll");

        LocalAdUser adUser = null;
        ArrayList list = new ArrayList();

        try {

            // Collection adUsers = adUserHome.findUsrByDepartment(USR_DEPT, AD_CMPNY);
            adUser = adUserHome.findByUsrName(USR_NM, AD_CMPNY);

            return adUser.getUsrDept();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdUsrByDepartmentAll(String USR_DEPT, Integer AD_CMPNY) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getAdUsrByDepartmentAll");

        ArrayList list = new ArrayList();
        try {
            Collection adUsers = adUserHome.findUsrByDepartment(USR_DEPT, AD_CMPNY);
            for (Object user : adUsers) {
                LocalAdUser adUser = (LocalAdUser) user;
                list.add(adUser.getUsrName());
            }
            return list;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdLvPurchaseRequisitionType(Integer AD_CMPNY) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getAdLvPurchaseRequisitionType");


        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AP PURCHASE REQUISITION TYPE", AD_CMPNY);

            for (Object lookUpValue : adLookUpValues) {

                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;

                list.add(adLookUpValue.getLvName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdLvPurchaseRequisitionMisc1(Integer AD_CMPNY) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getAdLvPurchaseRequisitionMisc1");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AP PURCHASE REQUISITION MISC1", AD_CMPNY);

            for (Object lookUpValue : adLookUpValues) {

                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;

                list.add(adLookUpValue.getLvName());
            }
            Debug.print("MISC1=" + list.size());

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdLvPurchaseRequisitionMisc2(Integer AD_CMPNY) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getAdLvPurchaseRequisitionMisc2");

        ArrayList list = new ArrayList();

        try {

            adLookUpValueHome = (LocalAdLookUpValueHome) EJBHomeFactory.lookUpLocalHome(LocalAdLookUpValueHome.JNDI_NAME, LocalAdLookUpValueHome.class);

        } catch (NamingException ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AP PURCHASE REQUISITION MISC2", AD_CMPNY);

            for (Object lookUpValue : adLookUpValues) {

                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;

                list.add(adLookUpValue.getLvName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdLvPurchaseRequisitionMisc3(Integer AD_CMPNY) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getAdLvPurchaseRequisitionMisc3");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AP PURCHASE REQUISITION MISC3", AD_CMPNY);

            for (Object lookUpValue : adLookUpValues) {

                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;

                list.add(adLookUpValue.getLvName());
            }
            Debug.print("PR T9=" + list.size());

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdLvPurchaseRequisitionMisc4(Integer AD_CMPNY) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getAdLvPurchaseRequisitionMisc4");

        ArrayList list = new ArrayList();

        try {

            adLookUpValueHome = (LocalAdLookUpValueHome) EJBHomeFactory.lookUpLocalHome(LocalAdLookUpValueHome.JNDI_NAME, LocalAdLookUpValueHome.class);

        } catch (NamingException ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AP PURCHASE REQUISITION MISC4", AD_CMPNY);

            for (Object lookUpValue : adLookUpValues) {

                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;

                list.add(adLookUpValue.getLvName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdLvPurchaseRequisitionMisc5(Integer AD_CMPNY) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getAdLvPurchaseRequisitionMisc5");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AP PURCHASE REQUISITION MISC5", AD_CMPNY);

            for (Object lookUpValue : adLookUpValues) {

                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;

                list.add(adLookUpValue.getLvName());
            }
            Debug.print("PR T9=" + list.size());

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdLvPurchaseRequisitionMisc6(Integer AD_CMPNY) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getAdLvPurchaseRequisitionMisc6");

        ArrayList list = new ArrayList();


        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AP PURCHASE REQUISITION MISC6", AD_CMPNY);

            for (Object lookUpValue : adLookUpValues) {

                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;

                list.add(adLookUpValue.getLvName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public String getSupplierCodeFromApCanvass(Integer PR_CODE, Integer AD_CMPNY) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getSupplierCodeFromApCanvass");

        String supplierCode = null;

        try {

            Collection list = apCanvassHome.findByPrCodeAndCnvPo(PR_CODE, EJBCommon.TRUE, AD_CMPNY);

            for (Object o : list) {
                LocalApCanvass apCanvass = (LocalApCanvass) o;
                if (apCanvass.getApSupplier() != null) {
                    supplierCode = apCanvass.getApSupplier().getSplSupplierCode();
                    break;
                }

            }


        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }


        return supplierCode;


    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ApPurchaseRequisitionEntryControllerBean ejbCreate");
    }
}