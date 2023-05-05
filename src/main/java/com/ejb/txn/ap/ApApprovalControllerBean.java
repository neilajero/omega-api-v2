/**
 * @copyright 2022 Omega Business Consulting, Inc.
 * @class ApApprovalControllerBean
 */
package com.ejb.txn.ap;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ap.*;
import com.ejb.dao.gl.*;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ap.*;
import com.ejb.entities.gl.*;
import com.ejb.entities.inv.*;
import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.ejb.txn.ad.AdApprovalDocumentEmailNotificationController;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.SendEmailDetails;
import com.util.mod.ad.AdModApprovalQueueDetails;

import jakarta.ejb.*;
import java.util.*;

@Stateless(name = "ApApprovalControllerEJB")
public class ApApprovalControllerBean extends EJBContextClass implements ApApprovalController {

    @EJB
    public PersistenceBeanClass em;
    AdApprovalDocumentEmailNotificationController adApprovalDocumentEmailNotificationController;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlAccountingCalendarValueHome glAccountingCalendarValueHome;
    @EJB
    private ILocalGlChartOfAccountBalanceHome glChartOfAccountBalanceHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private ILocalGlSetOfBookHome glSetOfBookHome;
    @EJB
    private LocalAdAmountLimitHome adAmountLimitHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalAdApprovalUserHome adApprovalUserHome;
    @EJB
    private LocalAdBankAccountBalanceHome adBankAccountBalanceHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalApCheckHome apCheckHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    private LocalApPurchaseOrderHome apPurchaseOrderHome;
    @EJB
    private LocalApPurchaseRequisitionHome apPurchaseRequisitionHome;
    @EJB
    private LocalApSupplierBalanceHome apSupplierBalanceHome;
    @EJB
    private LocalApVoucherHome apVoucherHome;
    @EJB
    private LocalGlForexLedgerHome glForexLedgerHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalGlJournalBatchHome glJournalBatchHome;
    @EJB
    private LocalGlJournalCategoryHome glJournalCategoryHome;
    @EJB
    private LocalGlJournalHome glJournalHome;
    @EJB
    private LocalGlJournalLineHome glJournalLineHome;
    @EJB
    private LocalGlJournalSourceHome glJournalSourceHome;
    @EJB
    private LocalGlSuspenseAccountHome glSuspenseAccountHome;
    @EJB
    private LocalInvAdjustmentHome invAdjustmentHome;
    @EJB
    private LocalInvAdjustmentLineHome invAdjustmentLineHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalInvDistributionRecordHome invDistributionRecordHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;

    public String getApprovalStatus(String userDepartment, String username, String userDesc, String approvalQueueDocument, Integer approvalQueueDocumentCode, String approvalQueueDocumentNumber, Date approvalQueueDate, double totalAmount, Integer branchCode, Integer companyCode) throws GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException {

        Debug.print("ApApprovalControllerBean getApprovalStatus");

        String prApprovalStatus = "N/A";
        String[] approvalUserType = {"REQUESTER", "APPROVER"};
        Double absTotalAmount = totalAmount;
        try {
            LocalAdAmountLimit adAmountLimit = null;
            try {
                //Get all amount limits by department by Requester user if exist
                Collection adAmountLimits = adAmountLimitHome.findAllByAmountCalDeptAdcTypeAndAuTypeAndUsrName(userDepartment, approvalQueueDocument, approvalUserType[0], username, companyCode);
                if (adAmountLimits.isEmpty()) {
                    throw new FinderException();
                } else {
                    LocalAdAmountLimit adAmountLimitDefault = null;
                    Iterator i = adAmountLimits.iterator();
                    while (i.hasNext()) {
                        LocalAdAmountLimit adNextAmountLimit = (LocalAdAmountLimit) i.next();
                        if (adNextAmountLimit.getCalAmountLimit() == 0) {
                            //This will assign approver list if only 0 limits and only 1
                            adAmountLimit = adNextAmountLimit;
                        } else if (absTotalAmount > adNextAmountLimit.getCalAmountLimit()) {
                            //This will assign approver list if approver list is more than 1 and over limit amount
                            adAmountLimit = adNextAmountLimit;
                        }
                        //loop assigned. the list ordered amount lowest to highest
                        if (absTotalAmount <= adNextAmountLimit.getCalAmountLimit()) {
                            adAmountLimit = adNextAmountLimit;
                            break;
                        }
                    }
                }
            } catch (FinderException ex) {
                //If approver list dont have requester assigned
                throw new GlobalNoApprovalRequesterFoundException();
            }
            if (adAmountLimit != null) {
                boolean isApprovalUsersFound = false;
                Collection adApprovalUsers = adApprovalUserHome.findApprovalUsersPerDepartment(userDepartment, approvalUserType[1], adAmountLimit.getCalCode(), companyCode);
                Iterator j = adApprovalUsers.iterator();
                while (j.hasNext()) {
                    isApprovalUsersFound = true;
                    LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) j.next();

                    LocalAdApprovalQueue adApprovalQueue = adApprovalQueueHome.AqDepartment(userDepartment).AqLevel(adApprovalUser.getAuLevel()).AqNextLevel(EJBCommon.incrementStringNumber(adApprovalUser.getAuLevel())).AqDocument(approvalQueueDocument).AqDocumentCode(approvalQueueDocumentCode).AqDocumentNumber(approvalQueueDocumentNumber).AqDate(approvalQueueDate).AqAndOr(adAmountLimit.getCalAndOr()).AqUserOr(adApprovalUser.getAuOr()).AqRequesterName(userDesc).AqAdBranch(branchCode).AqAdCompany(companyCode).buildApprovalQueue();

                    adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);

                    if (adApprovalUser.getAuLevel().equals("LEVEL 1")) {
                        adApprovalQueue.setAqForApproval(EJBCommon.TRUE);
                    }
                }

                if (!isApprovalUsersFound) {
                    throw new GlobalNoApprovalApproverFoundException();
                }
                prApprovalStatus = "PENDING";
            }
            return prApprovalStatus;
        } catch (GlobalNoApprovalApproverFoundException ex) {
            throw new GlobalNoApprovalApproverFoundException();
        } catch (GlobalNoApprovalRequesterFoundException ex) {
            throw new GlobalNoApprovalRequesterFoundException();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApApproverList(String approvalQueueDocument, Integer approvalQueueDocumentCode, Integer companyCode) {

        Debug.print("ApApprovalControllerBean getApApproverList");

        ArrayList list = new ArrayList();
        try {

            Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCodeAll(approvalQueueDocument, approvalQueueDocumentCode, companyCode);

            Debug.print("adApprovalQueues=" + adApprovalQueues.size());

            Iterator i = adApprovalQueues.iterator();

            while (i.hasNext()) {
                LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) i.next();

                AdModApprovalQueueDetails adModApprovalQueueDetails = new AdModApprovalQueueDetails();
                adModApprovalQueueDetails.setAqApprovedDate(adApprovalQueue.getAqApprovedDate());
                adModApprovalQueueDetails.setAqApproverName(adApprovalQueue.getAdUser().getUsrDescription());
                adModApprovalQueueDetails.setAqStatus(adApprovalQueue.getAqApproved() == EJBCommon.TRUE ? adApprovalQueue.getAqLevel() + " APPROVED" : adApprovalQueue.getAqLevel() + " PENDING");

                list.add(adModApprovalQueueDetails);
            }

            return list;

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdApprovalNotifiedUsersByAqCode(Integer approvalQueueCode, SendEmailDetails sendEmailDetails, Integer companyCode) {

        Debug.print("ApApprovalControllerBean getAdApprovalNotifiedUsersByAqCode");

        ArrayList list = new ArrayList();

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdApprovalQueue adApprovalQueue = null;

            Collection approverQueueList = null;
            try {

                adApprovalQueue = adApprovalQueueHome.findByPrimaryKey(approvalQueueCode);

                if (adApprovalQueue.getAqApproved() == EJBCommon.TRUE) {
                    Debug.print("approved succesfull 1");

                    approverQueueList = adApprovalQueueHome.findListByAqDeptAndAqLevelAndAqDocumentCode(adApprovalQueue.getAqDepartment(), adApprovalQueue.getAqNextLevel(), adApprovalQueue.getAqDocumentCode(), companyCode);

                    if (approverQueueList.size() <= 0) {
                        list.add("DOCUMENT POSTED");
                    }

                }

            } catch (Exception e) {
                list.add("DOCUMENT REJECTED");
                return list;
            }

            if (adApprovalQueue.getAqNextLevel() == null) {
                return list;
            }
            Integer aqDocumentCode = adApprovalQueue.getAqDocumentCode();

            if (adApprovalQueue.getAqDocument().equals("AP PURCHASE REQUISITION")) {

                LocalApPurchaseRequisition apPurchaseRequisition = apPurchaseRequisitionHome.findByPrimaryKey(aqDocumentCode);
                if (apPurchaseRequisition.getPrPosted() == EJBCommon.TRUE) {

                    list.add("DOCUMENT POSTED");
                    return list;
                }
            }

            if (adApprovalQueue.getAqDocument().equals("AP CANVASS")) {

                LocalApPurchaseRequisition apPurchaseRequisition = apPurchaseRequisitionHome.findByPrimaryKey(aqDocumentCode);
                if (apPurchaseRequisition.getPrPosted() == EJBCommon.TRUE) {

                    list.add("DOCUMENT POSTED");
                    return list;
                }
            }

            if (adApprovalQueue.getAqDocument().equals("AP RECEIVING ITEM") || adApprovalQueue.getAqDocument().equals("AP PURCHASE ORDER")) {

                LocalApPurchaseOrder apPurchaseOrder = apPurchaseOrderHome.findByPrimaryKey(aqDocumentCode);
                if (apPurchaseOrder.getPoPosted() == EJBCommon.TRUE) {

                    list.add("DOCUMENT POSTED");
                    return list;
                }
            }

            if (adApprovalQueue.getAqDocument().equals("AP VOUCHER") || adApprovalQueue.getAqDocument().equals("AP DEBIT MEMO") || adApprovalQueue.getAqDocument().equals("AP CHECK PAYMENT REQUEST")) {

                LocalApVoucher apVoucher = apVoucherHome.findByPrimaryKey(aqDocumentCode);
                if (apVoucher.getVouPosted() == EJBCommon.TRUE) {

                    list.add("DOCUMENT POSTED");
                    return list;
                }
            }

            if (adApprovalQueue.getAqDocument().equals("AP CHECK")) {

                LocalApCheck apCheck = apCheckHome.findByPrimaryKey(aqDocumentCode);
                if (apCheck.getChkPosted() == EJBCommon.TRUE) {

                    list.add("DOCUMENT POSTED");
                    return list;
                }
            }

            Iterator i = approverQueueList.iterator();

            while (i.hasNext()) {
                LocalAdApprovalQueue adNextApprovalQueue = (LocalAdApprovalQueue) i.next();
                adNextApprovalQueue.setAqForApproval(EJBCommon.TRUE);
                if (adPreference.getPrfAdEnableEmailNotification() == EJBCommon.TRUE) {

                    if (adNextApprovalQueue.getAqForApproval() == EJBCommon.FALSE) {
                        continue;
                    } else if (adNextApprovalQueue.getAqApproved() == EJBCommon.TRUE) {
                        continue;
                    }

                    String status = "";

                    status = adApprovalDocumentEmailNotificationController.sendEmailToApprover(adNextApprovalQueue, sendEmailDetails);
                    list.add(adNextApprovalQueue.getAqLevel() + " APPROVER - " + adNextApprovalQueue.getAdUser().getUsrDescription() + "- " + status);

                } else {

                    list.add(adNextApprovalQueue.getAdUser().getUsrName());
                }

            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdAqByAqDocumentAndUserName(HashMap criteria, String username, Integer offset, Integer limit, String orderBy, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ApApprovalControllerBean getAdAqByAqDocumentAndUserName");

        ArrayList list = new ArrayList();


        try {

            StringBuffer jbossQl = new StringBuffer();
            jbossQl.append("SELECT OBJECT(aq) FROM AdApprovalQueue aq ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            obj = new Object[criteriaSize];
            if (criteria.containsKey("document")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("aq.aqDocument=?" + (ctr + 1) + " ");
                obj[ctr] = criteria.get("document");
                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("aq.aqDate>=?" + (ctr + 1) + " ");
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
                jbossQl.append("aq.aqDate<=?" + (ctr + 1) + " ");
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
                jbossQl.append("aq.aqDocumentNumber>=?" + (ctr + 1) + " ");
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
                jbossQl.append("aq.aqDocumentNumber<=?" + (ctr + 1) + " ");
                obj[ctr] = criteria.get("documentNumberTo");
                ctr++;
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("aq.aqForApproval = 1 AND aq.aqApproved = 0 AND aq.aqAdCompany=" + companyCode + " " + "AND aq.adUser.usrName='" + username + "'");

            if (orderBy.equals("DOCUMENT NUMBER")) {
                orderBy = "aq.aqDocumentNumber";
            } else if (orderBy.equals("DATE")) {
                orderBy = "aq.aqDate";
            }

            if (orderBy != null) {
                jbossQl.append("ORDER BY " + orderBy + ", aq.aqDate");
            } else {
                jbossQl.append("ORDER BY aq.aqDate");
            }

            Debug.print("jbossQl.toString()=" + jbossQl);
            Collection adApprovalQueues = adApprovalQueueHome.getAqByCriteria(jbossQl.toString(), obj, limit, offset);

            String approvalQueueDocument = (String) criteria.get("document");

            Debug.print("approvalQueueDocument------------->" + approvalQueueDocument);

            if (adApprovalQueues.size() == 0) throw new GlobalNoRecordFoundException();

            Iterator i = adApprovalQueues.iterator();

            while (i.hasNext()) {

                LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) i.next();

                AdModApprovalQueueDetails details = new AdModApprovalQueueDetails();

                details.setAqCode(adApprovalQueue.getAqCode());
                details.setAqDocument(adApprovalQueue.getAqDocument());
                details.setAqDocumentCode(adApprovalQueue.getAqDocumentCode());
                details.setAqDepartment(adApprovalQueue.getAqDepartment());

                if (approvalQueueDocument.equals("AP VOUCHER") || approvalQueueDocument.equals("AP DEBIT MEMO") || approvalQueueDocument.equals("AP CHECK PAYMENT REQUEST")) {

                    getApVoucherApprovalQueue(apVoucherHome, approvalQueueDocument, adApprovalQueue, details);

                } else if (approvalQueueDocument.equals("AP PURCHASE ORDER") || approvalQueueDocument.equals("AP RECEIVING ITEM")) {

                    getApPurchaseOrderApprovalQueue(apPurchaseOrderHome, approvalQueueDocument, adApprovalQueue, details);

                } else if (approvalQueueDocument.equals("AP PURCHASE REQUISITION")) {

                    getApPurchaseRequisitionApprovalQueue(apPurchaseRequisitionHome, adApprovalQueue, details);

                } else if (approvalQueueDocument.equals("AP CANVASS")) {

                    getApCanvassApprovalQueue(apPurchaseRequisitionHome, adApprovalQueue, details);

                } else {
                    //AP CHECK
                    getApCheckApprovalQueue(apCheckHome, adApprovalQueue, details);
                }

                list.add(details);
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    private void getApCheckApprovalQueue(LocalApCheckHome apCheckHome, LocalAdApprovalQueue adApprovalQueue, AdModApprovalQueueDetails details) throws FinderException {

        LocalApCheck apCheck = apCheckHome.findByPrimaryKey(adApprovalQueue.getAqDocumentCode());

        details.setAqDate(apCheck.getChkDate());
        details.setAqDocumentNumber(apCheck.getChkDocumentNumber());
        details.setAqAmount(apCheck.getChkAmount());
        details.setAqSupplierCode(apCheck.getApSupplier().getSplSupplierCode());
        details.setAqDocumentType(apCheck.getChkType());
        details.setAqBrCode(apCheck.getChkAdBranch());
        details.setAqDescription(apCheck.getChkDescription());

        if (!apCheck.getApVoucherLineItems().isEmpty()) {

            details.setAqType("ITEMS");

        } else {

            details.setAqType("EXPENSES");
        }

        details.setAqReferenceNumber(apCheck.getChkReferenceNumber());
        details.setAqSupplierName(apCheck.getApSupplier().getSplName());
    }

    private void getApCanvassApprovalQueue(LocalApPurchaseRequisitionHome apPurchaseRequisitionHome, LocalAdApprovalQueue adApprovalQueue, AdModApprovalQueueDetails details) throws FinderException {

        LocalApPurchaseRequisition apPurchaseRequisition = apPurchaseRequisitionHome.findByPrimaryKey(adApprovalQueue.getAqDocumentCode());

        details.setAqDate(apPurchaseRequisition.getPrDate());
        details.setAqDocumentNumber(apPurchaseRequisition.getPrNumber());
        details.setAqBrCode(apPurchaseRequisition.getPrAdBranch());
        Collection apPurchaseRequisitionLines = apPurchaseRequisition.getApPurchaseRequisitionLines();

        Iterator x = apPurchaseRequisitionLines.iterator();
        double TTL_AMNT_CNV = 0d;

        while (x.hasNext()) {
            LocalApPurchaseRequisitionLine apPurchaseRequisitionLine = (LocalApPurchaseRequisitionLine) x.next();
            Collection apCanvasses = apPurchaseRequisitionLine.getApCanvasses();

            Iterator j = apCanvasses.iterator();

            while (j.hasNext()) {

                LocalApCanvass apCanvass = (LocalApCanvass) j.next();
                if (apCanvass.getCnvPo() == 1) {
                    TTL_AMNT_CNV += apCanvass.getCnvAmount();
                }
            }
        }

        details.setAqAmount(TTL_AMNT_CNV);
        details.setAqSupplierCode(null);
        details.setAqDocumentType(apPurchaseRequisition.getPrType());
        details.setAqType("ITEMS");
        details.setAqReferenceNumber(apPurchaseRequisition.getPrReferenceNumber());
        details.setAqSupplierName(null);
        details.setAqDescription(apPurchaseRequisition.getPrDescription());
    }

    private void getApPurchaseRequisitionApprovalQueue(LocalApPurchaseRequisitionHome apPurchaseRequisitionHome, LocalAdApprovalQueue adApprovalQueue, AdModApprovalQueueDetails details) throws FinderException {

        LocalApPurchaseRequisition apPurchaseRequisition = apPurchaseRequisitionHome.findByPrimaryKey(adApprovalQueue.getAqDocumentCode());

        details.setAqDate(apPurchaseRequisition.getPrDate());
        details.setAqDocumentNumber(apPurchaseRequisition.getPrNumber());
        details.setAqBrCode(apPurchaseRequisition.getPrAdBranch());

        Collection apPurchaseRequisitionLines = apPurchaseRequisition.getApPurchaseRequisitionLines();
        Iterator x = apPurchaseRequisitionLines.iterator();
        double TTL_AMNTs = 0d;

        while (x.hasNext()) {

            LocalApPurchaseRequisitionLine apPurchaseRequisitionLine = (LocalApPurchaseRequisitionLine) x.next();

            TTL_AMNTs += apPurchaseRequisitionLine.getPrlAmount();

            Collection apCanvasses = apPurchaseRequisitionLine.getApCanvasses();

            Iterator y = apCanvasses.iterator();

            while (y.hasNext()) {

                LocalApCanvass apCanvass = (LocalApCanvass) y.next();

                if (apCanvass.getCnvPo() == EJBCommon.TRUE) {

                    details.setAqSupplierCode(apCanvass.getApSupplier().getSplSupplierCode());
                    details.setAqSupplierName(apCanvass.getApSupplier().getSplName());
                }
            }
        }

        details.setAqAmount(TTL_AMNTs);
        details.setAqDocumentType(apPurchaseRequisition.getPrType());
        details.setAqType("ITEMS");
        details.setAqReferenceNumber(apPurchaseRequisition.getPrReferenceNumber());
        details.setAqDescription(apPurchaseRequisition.getPrDescription());

    }

    private void getApPurchaseOrderApprovalQueue(LocalApPurchaseOrderHome apPurchaseOrderHome, String approvalQueueDocument, LocalAdApprovalQueue adApprovalQueue, AdModApprovalQueueDetails details) throws FinderException {

        Debug.print("approvalQueueDocument=" + approvalQueueDocument);

        LocalApPurchaseOrder apPurchaseOrder = apPurchaseOrderHome.findByPrimaryKey(adApprovalQueue.getAqDocumentCode());
        Debug.print("apPurchaseOrder=" + apPurchaseOrder.getPoDocumentNumber());
        details.setAqDate(apPurchaseOrder.getPoDate());
        details.setAqDocumentNumber(apPurchaseOrder.getPoDocumentNumber());
        details.setAqBrCode(apPurchaseOrder.getPoAdBranch());
        Collection apPurchaseOrderLines = apPurchaseOrder.getApPurchaseOrderLines();
        Iterator j = apPurchaseOrderLines.iterator();
        double TTL_AMNT = 0d;

        while (j.hasNext()) {

            LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) j.next();

            TTL_AMNT += apPurchaseOrderLine.getPlAmount();
        }

        details.setAqAmount(TTL_AMNT);
        details.setAqSupplierCode(apPurchaseOrder.getApSupplier().getSplSupplierCode());
        details.setAqDocumentType(apPurchaseOrder.getPoType());
        details.setAqType("ITEMS");
        details.setAqReferenceNumber(apPurchaseOrder.getPoReferenceNumber());
        details.setAqSupplierName(apPurchaseOrder.getApSupplier().getSplName());
        details.setAqDescription(apPurchaseOrder.getPoDescription());
    }

    private void getApVoucherApprovalQueue(LocalApVoucherHome apVoucherHome, String approvalQueueDocument, LocalAdApprovalQueue adApprovalQueue, AdModApprovalQueueDetails details) throws FinderException {

        LocalApVoucher apVoucher = apVoucherHome.findByPrimaryKey(adApprovalQueue.getAqDocumentCode());

        details.setAqDate(apVoucher.getVouDate());
        details.setAqDocumentNumber(apVoucher.getVouDocumentNumber());
        details.setAqBrCode(apVoucher.getVouAdBranch());


        if (approvalQueueDocument.equals("AP VOUCHER") || approvalQueueDocument.equals("AP CHECK PAYMENT REQUEST")) {

            details.setAqAmount(apVoucher.getVouAmountDue());

        } else {

            details.setAqAmount(apVoucher.getVouBillAmount());
        }

        details.setAqSupplierCode(apVoucher.getApSupplier().getSplSupplierCode());

        if (!apVoucher.getApVoucherLineItems().isEmpty()) {

            details.setAqType("ITEMS");

        } else if (!apVoucher.getApPurchaseOrderLines().isEmpty()) {

            details.setAqType("PO MATCHED");

        } else {

            details.setAqType("EXPENSES");
        }

        details.setAqReferenceNumber(apVoucher.getVouReferenceNumber());
        details.setAqSupplierName(apVoucher.getApSupplier().getSplName());
        details.setAqDescription(apVoucher.getVouDescription());
    }

    public void executeApApproval(String approvalQueueDocument, Integer approvalQueueDocumentCode, String username, boolean isApproved, String reasonForRejection, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalTransactionAlreadyVoidPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalInventoryDateException, AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ApApprovalControllerBean executeApApproval");


        LocalAdApprovalQueue adApprovalQueue = null;


        try {
            // validate if approval queue is already deleted
            try {
                adApprovalQueue = adApprovalQueueHome.findByAqDocumentAndAqDocumentCodeAndUsrName(approvalQueueDocument, approvalQueueDocumentCode, username, companyCode);
            } catch (FinderException ex) {
                throw new GlobalRecordAlreadyDeletedException();
            }

            // approve/reject

            Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode(approvalQueueDocument, approvalQueueDocumentCode, companyCode);
            Collection adApprovalQueuesDesc = adApprovalQueueHome.findByAqDocumentAndAqDocumentCodeLessThanDesc(approvalQueueDocument, approvalQueueDocumentCode, adApprovalQueue.getAqCode(), companyCode);
            Collection adApprovalQueuesAsc = adApprovalQueueHome.findByAqDocumentAndAqDocumentCodeGreaterThanAsc(approvalQueueDocument, approvalQueueDocumentCode, adApprovalQueue.getAqCode(), companyCode);

            Collection adAllApprovalQueues = adApprovalQueueHome.findAllByAqDocumentAndAqDocumentCode(approvalQueueDocument, approvalQueueDocumentCode, companyCode);

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            if (approvalQueueDocument.equals("AP VOUCHER") || approvalQueueDocument.equals("AP DEBIT MEMO")) {
                LocalApVoucher apVoucher = apVoucherHome.findByPrimaryKey(approvalQueueDocumentCode);
                // start of date validation
                Collection apVoucherLineItems = apVoucher.getApVoucherLineItems();
                Iterator i = apVoucherLineItems.iterator();

                while (i.hasNext()) {
                    LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) i.next();
                    // start date validation
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(apVoucher.getVouDate(), apVoucherLineItem.getInvItemLocation().getInvItem().getIiName(), apVoucherLineItem.getInvItemLocation().getInvLocation().getLocName(), branchCode, companyCode);
                        if (!invNegTxnCosting.isEmpty())
                            throw new GlobalInventoryDateException(apVoucherLineItem.getInvItemLocation().getInvItem().getIiName());
                    }
                }

                if (isApproved) {
                    if (adApprovalQueue.getAqAndOr().equals("AND")) {
                        if (adApprovalQueues.size() == 1) {
                            apVoucher.setVouApprovalStatus("APPROVED");
                            apVoucher.setVouApprovedRejectedBy(username);
                            apVoucher.setVouDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                            adApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adApprovalQueue.setAqApprovedDate(new java.util.Date());

                            if (adPreference.getPrfApGlPostingType().equals("AUTO-POST UPON APPROVAL")) {
                                this.executeApVouPost(apVoucher.getVouCode(), username, branchCode, companyCode);
                            }

                        } else {
                            // looping up
                            i = adApprovalQueuesDesc.iterator();
                            while (i.hasNext()) {
                                LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();
                                if (adRemoveApprovalQueue.getAqUserOr() == (byte) 1) {
                                    i.remove();
                                    adRemoveApprovalQueue.setAqApproved(EJBCommon.TRUE);
                                    adRemoveApprovalQueue.setAqApprovedDate(EJBCommon.getGcCurrentDateWTime().getTime());

                                } else {
                                    break;
                                }
                            }

                            // looping down
                            if (adApprovalQueue.getAqUserOr() == (byte) 1) {
                                boolean first = true;
                                i = adApprovalQueuesAsc.iterator();
                                while (i.hasNext()) {
                                    LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();
                                    if (first || adRemoveApprovalQueue.getAqUserOr() == (byte) 1) {
                                        i.remove();
                                        adRemoveApprovalQueue.setAqApproved(EJBCommon.TRUE);
                                        adRemoveApprovalQueue.setAqApprovedDate(EJBCommon.getGcCurrentDateWTime().getTime());
                                        if (first) first = false;
                                    } else {
                                        break;
                                    }
                                }
                            }

                            adApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adApprovalQueue.setAqApprovedDate(new java.util.Date());
                            Collection adRemoveApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode(approvalQueueDocument, approvalQueueDocumentCode, companyCode);

                            if (adRemoveApprovalQueues.size() == 0) {
                                apVoucher.setVouApprovalStatus("APPROVED");
                                apVoucher.setVouApprovedRejectedBy(username);
                                apVoucher.setVouDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());
                                if (adPreference.getPrfApGlPostingType().equals("AUTO-POST UPON APPROVAL")) {
                                    this.executeApVouPost(apVoucher.getVouCode(), username, branchCode, companyCode);
                                }
                            }
                        }
                    } else if (adApprovalQueue.getAqAndOr().equals("OR")) {
                        apVoucher.setVouApprovalStatus("APPROVED");
                        apVoucher.setVouApprovedRejectedBy(username);
                        apVoucher.setVouDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                        i = adApprovalQueues.iterator();
                        while (i.hasNext()) {
                            LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();
                            i.remove();
                            // adRemoveApprovalQueue.entityRemove();
                            adRemoveApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adRemoveApprovalQueue.setAqApprovedDate(EJBCommon.getGcCurrentDateWTime().getTime());
                            // em.remove(adRemoveApprovalQueue);
                        }

                        if (adPreference.getPrfApGlPostingType().equals("AUTO-POST UPON APPROVAL")) {
                            this.executeApVouPost(apVoucher.getVouCode(), username, branchCode, companyCode);
                        }
                    }
                } else if (!isApproved) {

                    apVoucher.setVouApprovalStatus(null);
                    apVoucher.setVouReasonForRejection(reasonForRejection);
                    apVoucher.setVouApprovedRejectedBy(username);
                    apVoucher.setVouDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                    i = adAllApprovalQueues.iterator();
                    while (i.hasNext()) {
                        LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();
                        i.remove();
                        // adRemoveApprovalQueue.entityRemove();
                        em.remove(adRemoveApprovalQueue);
                    }
                }
            } else if (approvalQueueDocument.equals("AP CHECK PAYMENT REQUEST")) {

                LocalApVoucher apVoucher = apVoucherHome.findByPrimaryKey(approvalQueueDocumentCode);
                Iterator i = null;
                if (isApproved) {
                    if (adApprovalQueue.getAqAndOr().equals("AND")) {
                        if (adApprovalQueues.size() == 1) {
                            apVoucher.setVouApprovalStatus("APPROVED");
                            apVoucher.setVouApprovedRejectedBy(username);
                            apVoucher.setVouDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());
                            apVoucher.setVouPostedBy(username);
                            apVoucher.setVouDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
                            apVoucher.setVouPosted(EJBCommon.TRUE);
                            adApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adApprovalQueue.setAqApprovedDate(EJBCommon.getGcCurrentDateWTime().getTime());
                            // adApprovalQueue.remove();
                        } else {
                            // looping up
                            i = adApprovalQueuesDesc.iterator();
                            while (i.hasNext()) {
                                LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();
                                if (adRemoveApprovalQueue.getAqUserOr() == (byte) 1) {
                                    i.remove();
                                    adRemoveApprovalQueue.setAqApproved(EJBCommon.TRUE);
                                    adRemoveApprovalQueue.setAqApprovedDate(new java.util.Date());
                                    // em.remove(adRemoveApprovalQueue);
                                } else {
                                    break;
                                }
                            }

                            // looping down
                            if (adApprovalQueue.getAqUserOr() == (byte) 1) {
                                boolean first = true;
                                i = adApprovalQueuesAsc.iterator();
                                while (i.hasNext()) {
                                    LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();
                                    if (first || adRemoveApprovalQueue.getAqUserOr() == (byte) 1) {
                                        i.remove();
                                        adRemoveApprovalQueue.setAqApproved(EJBCommon.TRUE);
                                        adRemoveApprovalQueue.setAqApprovedDate(EJBCommon.getGcCurrentDateWTime().getTime());
                                        // em.remove(adRemoveApprovalQueue);
                                        if (first) first = false;
                                    } else {
                                        break;
                                    }
                                }
                            }

                            adApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adApprovalQueue.setAqApprovedDate(new java.util.Date());
                            // em.remove(adApprovalQueue);
                            Collection adRemoveApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode(approvalQueueDocument, approvalQueueDocumentCode, companyCode);

                            if (adRemoveApprovalQueues.size() == 0) {
                                apVoucher.setVouApprovalStatus("APPROVED");
                                apVoucher.setVouApprovedRejectedBy(username);
                                apVoucher.setVouDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());
                                apVoucher.setVouPostedBy(username);
                                apVoucher.setVouDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
                                apVoucher.setVouPosted(EJBCommon.TRUE);
                            }
                        }
                    } else if (adApprovalQueue.getAqAndOr().equals("OR")) {
                        apVoucher.setVouApprovalStatus("APPROVED");
                        apVoucher.setVouApprovedRejectedBy(username);
                        apVoucher.setVouDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());
                        apVoucher.setVouPostedBy(username);
                        apVoucher.setVouDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
                        apVoucher.setVouPosted(EJBCommon.TRUE);

                        i = adApprovalQueues.iterator();
                        while (i.hasNext()) {
                            LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();
                            i.remove();
                            // adRemoveApprovalQueue.remove();
                            adRemoveApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adRemoveApprovalQueue.setAqApprovedDate(EJBCommon.getGcCurrentDateWTime().getTime());
                        }
                    }
                } else if (!isApproved) {

                    apVoucher.setVouApprovalStatus(null);
                    apVoucher.setVouReasonForRejection(reasonForRejection);
                    apVoucher.setVouApprovedRejectedBy(username);
                    apVoucher.setVouDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                    i = adAllApprovalQueues.iterator();
                    while (i.hasNext()) {
                        LocalAdApprovalQueue adAllRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();
                        i.remove();
                        // adRemoveApprovalQueue.entityRemove();
                        em.remove(adAllRemoveApprovalQueue);
                    }
                }

            } else if (approvalQueueDocument.equals("AP PURCHASE ORDER")) {

                LocalApPurchaseOrder apPurchaseOrder = apPurchaseOrderHome.findByPrimaryKey(approvalQueueDocumentCode);

                if (isApproved) {
                    if (adApprovalQueue.getAqAndOr().equals("AND")) {
                        if (adApprovalQueues.size() == 1) {
                            apPurchaseOrder.setPoApprovalStatus("APPROVED");

                            apPurchaseOrder.setPoApprovedRejectedBy(username);
                            apPurchaseOrder.setPoDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());
                            apPurchaseOrder.setPoPosted(EJBCommon.TRUE);
                            apPurchaseOrder.setPoPosted(EJBCommon.TRUE);
                            apPurchaseOrder.setPoPostedBy(apPurchaseOrder.getPoLastModifiedBy());
                            apPurchaseOrder.setPoDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
                            adApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adApprovalQueue.setAqApprovedDate(EJBCommon.getGcCurrentDateWTime().getTime());

                            // em.remove(adApprovalQueue);

                        } else {

                            // looping up
                            Iterator i = adApprovalQueuesDesc.iterator();

                            while (i.hasNext()) {
                                LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();
                                if (adRemoveApprovalQueue.getAqUserOr() == (byte) 1) {
                                    i.remove();
                                    adRemoveApprovalQueue.setAqApproved(EJBCommon.TRUE);
                                    adRemoveApprovalQueue.setAqApprovedDate(new java.util.Date());

                                } else {
                                    break;
                                }
                            }

                            // looping down
                            if (adApprovalQueue.getAqUserOr() == (byte) 1) {

                                boolean first = true;

                                i = adApprovalQueuesAsc.iterator();

                                while (i.hasNext()) {

                                    LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                                    if (first || adRemoveApprovalQueue.getAqUserOr() == (byte) 1) {

                                        i.remove();

                                        adRemoveApprovalQueue.setAqApproved(EJBCommon.TRUE);
                                        adRemoveApprovalQueue.setAqApprovedDate(EJBCommon.getGcCurrentDateWTime().getTime());
                                        // em.remove(adRemoveApprovalQueue);

                                        if (first) first = false;

                                    } else {

                                        break;
                                    }
                                }
                            }

                            adApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adApprovalQueue.setAqApprovedDate(new java.util.Date());
                            // em.remove(adApprovalQueue);

                            Collection adRemoveApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode(approvalQueueDocument, approvalQueueDocumentCode, companyCode);

                            if (adRemoveApprovalQueues.size() == 0) {
                                apPurchaseOrder.setPoApprovalStatus("APPROVED");

                                LocalAdUser approver = null;
                                try {
                                    approver = adUserHome.findByUsrName(username, companyCode);
                                    approver.setUsrPurchaseOrderApprovalCounter(EJBCommon.incrementStringNumber(approver.getUsrPurchaseOrderApprovalCounter()));
                                } catch (Exception ex) {
                                    Debug.printStackTrace(ex);
                                }

                                apPurchaseOrder.setPoApprovedRejectedBy(username);
                                apPurchaseOrder.setPoDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());
                                apPurchaseOrder.setPoPosted(EJBCommon.TRUE);
                                apPurchaseOrder.setPoPostedBy(apPurchaseOrder.getPoLastModifiedBy());
                                apPurchaseOrder.setPoDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
                                adApprovalQueue.setAqApproved(EJBCommon.TRUE);
                                adApprovalQueue.setAqApprovedDate(EJBCommon.getGcCurrentDateWTime().getTime());
                            }
                        }
                    } else if (adApprovalQueue.getAqAndOr().equals("OR")) {

                        apPurchaseOrder.setPoApprovalStatus("APPROVED");

                        LocalAdUser approver = null;
                        try {
                            approver = adUserHome.findByUsrName(username, companyCode);
                            approver.setUsrPurchaseOrderApprovalCounter(EJBCommon.incrementStringNumber(approver.getUsrPurchaseOrderApprovalCounter() == null ? "0" : approver.getUsrPurchaseOrderApprovalCounter()));
                        } catch (Exception ex) {
                            Debug.printStackTrace(ex);
                        }

                        apPurchaseOrder.setPoApprovedRejectedBy(username);
                        apPurchaseOrder.setPoDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                        Iterator i = adApprovalQueues.iterator();

                        while (i.hasNext()) {
                            LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();
                            i.remove();
                            adRemoveApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adRemoveApprovalQueue.setAqApprovedDate(EJBCommon.getGcCurrentDateWTime().getTime());
                            // em.remove(adRemoveApprovalQueue);

                        }

                        apPurchaseOrder.setPoPosted(EJBCommon.TRUE);
                        apPurchaseOrder.setPoPostedBy(apPurchaseOrder.getPoLastModifiedBy());
                        apPurchaseOrder.setPoDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
                    }

                    if (apPurchaseOrder.getPoPosted() == EJBCommon.TRUE) {
                        // update transactional budget

                    }

                } else if (!isApproved) {
                    apPurchaseOrder.setPoApprovalStatus(null);
                    apPurchaseOrder.setPoReasonForRejection(reasonForRejection);
                    apPurchaseOrder.setPoApprovedRejectedBy(username);
                    apPurchaseOrder.setPoDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());
                    Iterator i = adAllApprovalQueues.iterator();

                    while (i.hasNext()) {
                        LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();
                        i.remove();
                        // adRemoveApprovalQueue.entityRemove();
                        em.remove(adRemoveApprovalQueue);
                    }
                }

            } else if (approvalQueueDocument.equals("AP RECEIVING ITEM")) {

                LocalApPurchaseOrder apPurchaseOrder = apPurchaseOrderHome.findByPrimaryKey(approvalQueueDocumentCode);

                if (isApproved) {
                    if (adApprovalQueue.getAqAndOr().equals("AND")) {
                        if (adApprovalQueues.size() == 1) {
                            apPurchaseOrder.setPoApprovalStatus("APPROVED");
                            apPurchaseOrder.setPoApprovedRejectedBy(username);
                            apPurchaseOrder.setPoDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());
                            // apPurchaseOrder.setPoPosted(EJBCommon.TRUE);
                            apPurchaseOrder.setPoPostedBy(apPurchaseOrder.getPoLastModifiedBy());
                            apPurchaseOrder.setPoDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

                            // adApprovalQueue.remove();
                            adApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adApprovalQueue.setAqApprovedDate(EJBCommon.getGcCurrentDateWTime().getTime());

                            if (adPreference.getPrfApGlPostingType().equals("AUTO-POST UPON APPROVAL")) {
                                this.executeApPoPost(apPurchaseOrder.getPoCode(), username, branchCode, companyCode);
                            }

                        } else {

                            // looping up
                            Iterator i = adApprovalQueuesDesc.iterator();

                            while (i.hasNext()) {
                                LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();
                                if (adRemoveApprovalQueue.getAqUserOr() == (byte) 1) {
                                    i.remove();
                                    // adRemoveApprovalQueue.remove();
                                    adRemoveApprovalQueue.setAqApproved(EJBCommon.TRUE);
                                    adRemoveApprovalQueue.setAqApprovedDate(new java.util.Date());
                                } else {
                                    break;
                                }
                            }

                            // looping down
                            if (adApprovalQueue.getAqUserOr() == (byte) 1) {

                                boolean first = true;

                                i = adApprovalQueuesAsc.iterator();

                                while (i.hasNext()) {

                                    LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                                    if (first || adRemoveApprovalQueue.getAqUserOr() == (byte) 1) {

                                        i.remove();

                                        adRemoveApprovalQueue.setAqApproved(EJBCommon.TRUE);
                                        adRemoveApprovalQueue.setAqApprovedDate(EJBCommon.getGcCurrentDateWTime().getTime());
                                        // adRemoveApprovalQueue.remove();

                                        if (first) first = false;

                                    } else {

                                        break;
                                    }
                                }
                            }

                            // adApprovalQueue.remove();
                            adApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adApprovalQueue.setAqApprovedDate(new java.util.Date());
                            Collection adRemoveApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode(approvalQueueDocument, approvalQueueDocumentCode, companyCode);

                            if (adRemoveApprovalQueues.size() == 0) {
                                apPurchaseOrder.setPoApprovalStatus("APPROVED");

                                apPurchaseOrder.setPoApprovedRejectedBy(username);
                                apPurchaseOrder.setPoDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());
                                // apPurchaseOrder.setPoPosted(EJBCommon.TRUE);
                                apPurchaseOrder.setPoPostedBy(apPurchaseOrder.getPoLastModifiedBy());
                                apPurchaseOrder.setPoDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

                                if (adPreference.getPrfApGlPostingType().equals("AUTO-POST UPON APPROVAL")) {
                                    this.executeApPoPost(apPurchaseOrder.getPoCode(), username, branchCode, companyCode);
                                }
                            }
                        }
                    } else if (adApprovalQueue.getAqAndOr().equals("OR")) {

                        apPurchaseOrder.setPoApprovalStatus("APPROVED");

                        apPurchaseOrder.setPoApprovedRejectedBy(username);
                        apPurchaseOrder.setPoDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                        Iterator i = adApprovalQueues.iterator();

                        while (i.hasNext()) {
                            LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();
                            i.remove();
                            adRemoveApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adRemoveApprovalQueue.setAqApprovedDate(EJBCommon.getGcCurrentDateWTime().getTime());
                            // adRemoveApprovalQueue.remove();

                        }

                        // apPurchaseOrder.setPoPosted(EJBCommon.TRUE);
                        apPurchaseOrder.setPoPostedBy(apPurchaseOrder.getPoLastModifiedBy());
                        apPurchaseOrder.setPoDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
                        if (adPreference.getPrfApGlPostingType().equals("AUTO-POST UPON APPROVAL")) {
                            this.executeApPoPost(apPurchaseOrder.getPoCode(), username, branchCode, companyCode);
                        }
                    }

                } else if (!isApproved) {
                    apPurchaseOrder.setPoApprovalStatus(null);
                    apPurchaseOrder.setPoReasonForRejection(reasonForRejection);
                    apPurchaseOrder.setPoApprovedRejectedBy(username);
                    apPurchaseOrder.setPoDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());
                    Iterator i = adAllApprovalQueues.iterator();

                    while (i.hasNext()) {
                        LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();
                        i.remove();
                        // adRemoveApprovalQueue.entityRemove();
                        em.remove(adRemoveApprovalQueue);
                    }
                }

            } else if (approvalQueueDocument.equals("AP CANVASS")) {
                LocalApPurchaseRequisition apPurchaseRequisition = apPurchaseRequisitionHome.findByPrimaryKey(approvalQueueDocumentCode);
                if (isApproved) {
                    if (adApprovalQueue.getAqAndOr().equals("AND")) {
                        if (adApprovalQueues.size() == 1) {
                            apPurchaseRequisition.setPrCanvassApprovalStatus("APPROVED");

                            LocalAdUser approver = null;
                            try {
                                approver = adUserHome.findByUsrName(username, companyCode);
                                approver.setUsrPurchaseOrderApprovalCounter(EJBCommon.incrementStringNumber(approver.getUsrPurchaseOrderApprovalCounter()));
                            } catch (Exception ex) {
                                Debug.printStackTrace(ex);
                            }

                            apPurchaseRequisition.setPrCanvassApprovedRejectedBy(username);
                            apPurchaseRequisition.setPrCanvassDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());
                            apPurchaseRequisition.setPrCanvassPosted(EJBCommon.TRUE);
                            apPurchaseRequisition.setPrCanvassPosted(EJBCommon.TRUE);
                            apPurchaseRequisition.setPrCanvassPostedBy(apPurchaseRequisition.getPrLastModifiedBy());
                            apPurchaseRequisition.setPrCanvassDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
                            adApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adApprovalQueue.setAqApprovedDate(EJBCommon.getGcCurrentDateWTime().getTime());

                        } else {
                            // looping up
                            Iterator i = adApprovalQueuesDesc.iterator();
                            while (i.hasNext()) {
                                LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();
                                if (adRemoveApprovalQueue.getAqUserOr() == (byte) 1) {
                                    i.remove();

                                    adRemoveApprovalQueue.setAqApproved(EJBCommon.TRUE);
                                    adRemoveApprovalQueue.setAqApprovedDate(EJBCommon.getGcCurrentDateWTime().getTime());
                                    // em.remove(adRemoveApprovalQueue);
                                } else {
                                    break;
                                }
                            }

                            // looping down
                            if (adApprovalQueue.getAqUserOr() == (byte) 1) {
                                boolean first = true;
                                i = adApprovalQueuesAsc.iterator();
                                while (i.hasNext()) {
                                    LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();
                                    if (first || adRemoveApprovalQueue.getAqUserOr() == (byte) 1) {
                                        i.remove();
                                        adRemoveApprovalQueue.setAqApproved(EJBCommon.TRUE);
                                        adRemoveApprovalQueue.setAqApprovedDate(EJBCommon.getGcCurrentDateWTime().getTime());
                                        // em.remove(adRemoveApprovalQueue);
                                        if (first) first = false;
                                    } else {
                                        break;
                                    }
                                }
                            }

                            // adApprovalQueue.entityRemove();
                            adApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adApprovalQueue.setAqApprovedDate(EJBCommon.getGcCurrentDateWTime().getTime());
                            // em.remove(adApprovalQueue);
                            Collection adRemoveApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode(approvalQueueDocument, approvalQueueDocumentCode, companyCode);
                            if (adRemoveApprovalQueues.size() == 0) {
                                apPurchaseRequisition.setPrCanvassApprovalStatus("APPROVED");

                                LocalAdUser approver = null;
                                try {
                                    approver = adUserHome.findByUsrName(username, companyCode);
                                    approver.setUsrPurchaseOrderApprovalCounter(EJBCommon.incrementStringNumber(approver.getUsrPurchaseOrderApprovalCounter()));
                                } catch (Exception ex) {
                                    Debug.printStackTrace(ex);
                                }

                                apPurchaseRequisition.setPrCanvassApprovedRejectedBy(username);
                                apPurchaseRequisition.setPrCanvassDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());
                                apPurchaseRequisition.setPrCanvassPosted(EJBCommon.TRUE);
                                apPurchaseRequisition.setPrCanvassPosted(EJBCommon.TRUE);
                                apPurchaseRequisition.setPrCanvassPostedBy(apPurchaseRequisition.getPrLastModifiedBy());
                                apPurchaseRequisition.setPrCanvassDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
                            }
                        }
                    } else if (adApprovalQueue.getAqAndOr().equals("OR")) {

                        apPurchaseRequisition.setPrCanvassApprovalStatus("APPROVED");

                        LocalAdUser approver = null;
                        try {
                            approver = adUserHome.findByUsrName(username, companyCode);
                            approver.setUsrPurchaseOrderApprovalCounter(EJBCommon.incrementStringNumber(approver.getUsrPurchaseOrderApprovalCounter()));
                        } catch (Exception ex) {
                            Debug.printStackTrace(ex);
                        }

                        apPurchaseRequisition.setPrCanvassApprovedRejectedBy(username);
                        apPurchaseRequisition.setPrCanvassDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                        Iterator i = adApprovalQueues.iterator();

                        while (i.hasNext()) {
                            LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();
                            i.remove();
                            adRemoveApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adRemoveApprovalQueue.setAqApprovedDate(EJBCommon.getGcCurrentDateWTime().getTime());
                            // em.remove(adRemoveApprovalQueue);

                        }
                        apPurchaseRequisition.setPrCanvassPosted(EJBCommon.TRUE);
                        apPurchaseRequisition.setPrCanvassPosted(EJBCommon.TRUE);
                        apPurchaseRequisition.setPrCanvassPostedBy(apPurchaseRequisition.getPrLastModifiedBy());
                        apPurchaseRequisition.setPrCanvassDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
                    }
                } else if (!isApproved) {

                    apPurchaseRequisition.setPrCanvassApprovalStatus(null);

                    // apPurchaseRequisition.setPrReasonForRejection(reasonForRejection);
                    apPurchaseRequisition.setPrCanvassReasonForRejection(reasonForRejection);
                    apPurchaseRequisition.setPrCanvassApprovedRejectedBy(username);
                    apPurchaseRequisition.setPrCanvassDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());
                    Iterator i = adAllApprovalQueues.iterator();

                    while (i.hasNext()) {
                        LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();
                        i.remove();
                        // adRemoveApprovalQueue.entityRemove();
                        em.remove(adRemoveApprovalQueue);
                    }
                }

            } else if (approvalQueueDocument.equals("AP PURCHASE REQUISITION")) {

                LocalApPurchaseRequisition apPurchaseRequisition = apPurchaseRequisitionHome.findByPrimaryKey(approvalQueueDocumentCode);
                if (isApproved) {
                    if (adApprovalQueue.getAqAndOr().equals("AND")) {
                        System.out.println("-----------------adApprovalQueues.size()=" + adApprovalQueues.size());
                        if (adApprovalQueues.size() == 1) {

                            apPurchaseRequisition.setPrApprovalStatus("APPROVED");
                            apPurchaseRequisition.setPrApprovedRejectedBy(username);
                            apPurchaseRequisition.setPrDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());
                            apPurchaseRequisition.setPrPosted(EJBCommon.TRUE);
                            apPurchaseRequisition.setPrPostedBy(apPurchaseRequisition.getPrLastModifiedBy());
                            apPurchaseRequisition.setPrDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
                            // adApprovalQueue.remove();
                            adApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adApprovalQueue.setAqApprovedDate(new java.util.Date());
                        } else {
                            // looping up
                            Iterator i = adApprovalQueuesDesc.iterator();
                            while (i.hasNext()) {
                                LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();
                                if (adRemoveApprovalQueue.getAqUserOr() == (byte) 1) {
                                    i.remove();
                                    adRemoveApprovalQueue.setAqApproved(EJBCommon.TRUE);
                                } else {
                                    break;
                                }
                            }

                            // looping down
                            if (adApprovalQueue.getAqUserOr() == (byte) 1) {
                                boolean first = true;
                                i = adApprovalQueuesAsc.iterator();
                                while (i.hasNext()) {
                                    LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();
                                    if (first || adRemoveApprovalQueue.getAqUserOr() == (byte) 1) {
                                        i.remove();
                                        adRemoveApprovalQueue.setAqApproved(EJBCommon.TRUE);
                                        adRemoveApprovalQueue.setAqApprovedDate(EJBCommon.getGcCurrentDateWTime().getTime());
                                        if (first) first = false;
                                    } else {
                                        break;
                                    }
                                }
                            }

                            // adApprovalQueue.remove();
                            adApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adApprovalQueue.setAqApprovedDate(new java.util.Date());

                            Collection adRemoveApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode(approvalQueueDocument, approvalQueueDocumentCode, companyCode);
                            Debug.print("-----------------adRemoveApprovalQueues.size()=" + adRemoveApprovalQueues.size());
                            if (adRemoveApprovalQueues.size() == 0) {
                                apPurchaseRequisition.setPrApprovalStatus("APPROVED");

                                apPurchaseRequisition.setPrApprovedRejectedBy(username);
                                apPurchaseRequisition.setPrDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());
                                apPurchaseRequisition.setPrPosted(EJBCommon.TRUE);
                                apPurchaseRequisition.setPrPostedBy(apPurchaseRequisition.getPrLastModifiedBy());
                                apPurchaseRequisition.setPrDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
                            }
                        }
                    } else if (adApprovalQueue.getAqAndOr().equals("OR")) {

                        apPurchaseRequisition.setPrApprovalStatus("APPROVED");
                        apPurchaseRequisition.setPrApprovedRejectedBy(username);
                        apPurchaseRequisition.setPrDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                        Iterator i = adApprovalQueues.iterator();

                        while (i.hasNext()) {
                            LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();
                            i.remove();
                            // adRemoveApprovalQueue.remove();
                            adRemoveApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adRemoveApprovalQueue.setAqApprovedDate(EJBCommon.getGcCurrentDateWTime().getTime());
                        }
                        apPurchaseRequisition.setPrPosted(EJBCommon.TRUE);
                        apPurchaseRequisition.setPrPostedBy(apPurchaseRequisition.getPrLastModifiedBy());
                        apPurchaseRequisition.setPrDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
                    }
                } else if (!isApproved) {
                    apPurchaseRequisition.setPrApprovalStatus(null);
                    apPurchaseRequisition.setPrReasonForRejection(reasonForRejection);
                    apPurchaseRequisition.setPrApprovedRejectedBy(username);
                    apPurchaseRequisition.setPrDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                    Iterator x = adAllApprovalQueues.iterator();

                    while (x.hasNext()) {
                        LocalAdApprovalQueue adRemoveAllApprovalQueue = (LocalAdApprovalQueue) x.next();
                        x.remove();
                        // adRemoveAllApprovalQueue.entityRemove();
                        em.remove(adRemoveAllApprovalQueue);
                    }
                }

            } else {

                // CHECK
                LocalApCheck apCheck = apCheckHome.findByPrimaryKey(approvalQueueDocumentCode);

                // start of date validation

                Collection apVoucherLineItems = apCheck.getApVoucherLineItems();
                Iterator i = apVoucherLineItems.iterator();
                while (i.hasNext()) {

                    LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) i.next();

                    // start date validation
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(apCheck.getChkDate(), apVoucherLineItem.getInvItemLocation().getInvItem().getIiName(), apVoucherLineItem.getInvItemLocation().getInvLocation().getLocName(), branchCode, companyCode);
                        if (!invNegTxnCosting.isEmpty())
                            throw new GlobalInventoryDateException(apVoucherLineItem.getInvItemLocation().getInvItem().getIiName());
                    }
                }

                if (isApproved) {

                    if (adApprovalQueue.getAqAndOr().equals("AND")) {

                        if (adApprovalQueues.size() == 1) {
                            if (apCheck.getChkVoid() == EJBCommon.FALSE) {
                                apCheck.setChkApprovalStatus("APPROVED");
                            } else {
                                apCheck.setChkVoidApprovalStatus("APPROVED");
                            }
                            apCheck.setChkApprovedRejectedBy(username);
                            apCheck.setChkDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());
                            adApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adApprovalQueue.setAqApprovedDate(EJBCommon.getGcCurrentDateWoTime().getTime());
                            if (adPreference.getPrfApGlPostingType().equals("AUTO-POST UPON APPROVAL")) {
                                this.executeApChkPost(apCheck.getChkCode(), username, branchCode, companyCode);
                            }
                            // adApprovalQueue.entityRemove();
                            // .em.remove(adApprovalQueue);
                        } else {
                            // looping up
                            i = adApprovalQueuesDesc.iterator();

                            while (i.hasNext()) {
                                LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();
                                if (adRemoveApprovalQueue.getAqUserOr() == (byte) 1) {
                                    i.remove();
                                    adRemoveApprovalQueue.setAqApproved(EJBCommon.TRUE);
                                    adRemoveApprovalQueue.setAqApprovedDate(EJBCommon.getGcCurrentDateWoTime().getTime());
                                    // em.remove(adRemoveApprovalQueue);
                                } else {
                                    break;
                                }
                            }

                            // looping down
                            if (adApprovalQueue.getAqUserOr() == (byte) 1) {

                                boolean first = true;

                                i = adApprovalQueuesAsc.iterator();

                                while (i.hasNext()) {

                                    LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                                    if (first || adRemoveApprovalQueue.getAqUserOr() == (byte) 1) {

                                        i.remove();
                                        adRemoveApprovalQueue.setAqApproved(EJBCommon.TRUE);
                                        adRemoveApprovalQueue.setAqApprovedDate(EJBCommon.getGcCurrentDateWoTime().getTime());
                                        // em.remove(adRemoveApprovalQueue);

                                        if (first) first = false;

                                    } else {

                                        break;
                                    }
                                }
                            }

                            // adApprovalQueue.entityRemove();
                            adApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adApprovalQueue.setAqApprovedDate(new java.util.Date());
                            // em.remove(adApprovalQueue);

                            Collection adRemoveApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode(approvalQueueDocument, approvalQueueDocumentCode, companyCode);

                            if (adRemoveApprovalQueues.size() == 0) {

                                if (apCheck.getChkVoid() == EJBCommon.FALSE) {

                                    apCheck.setChkApprovalStatus("APPROVED");

                                } else {

                                    apCheck.setChkVoidApprovalStatus("APPROVED");
                                }

                                apCheck.setChkApprovedRejectedBy(username);
                                apCheck.setChkDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                                if (adPreference.getPrfApGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                                    this.executeApChkPost(apCheck.getChkCode(), username, branchCode, companyCode);
                                }
                            }
                        }

                    } else if (adApprovalQueue.getAqAndOr().equals("OR")) {

                        if (apCheck.getChkVoid() == EJBCommon.FALSE) {

                            apCheck.setChkApprovalStatus("APPROVED");

                        } else {

                            apCheck.setChkVoidApprovalStatus("APPROVED");
                        }

                        apCheck.setChkApprovedRejectedBy(username);
                        apCheck.setChkDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                        i = adApprovalQueues.iterator();

                        while (i.hasNext()) {

                            LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                            i.remove();
                            adRemoveApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adRemoveApprovalQueue.setAqApprovedDate(EJBCommon.getGcCurrentDateWoTime().getTime());
                            // em.remove(adRemoveApprovalQueue);

                        }

                        if (adPreference.getPrfApGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                            this.executeApChkPost(apCheck.getChkCode(), username, branchCode, companyCode);
                        }
                    }

                } else if (!isApproved) {

                    if (apCheck.getChkVoid() == EJBCommon.FALSE) {

                        apCheck.setChkApprovalStatus(null);

                    } else {

                        apCheck.setChkVoidApprovalStatus(null);
                    }

                    apCheck.setChkApprovedRejectedBy(username);
                    apCheck.setChkReasonForRejection(reasonForRejection);
                    apCheck.setChkDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                    i = adAllApprovalQueues.iterator();

                    while (i.hasNext()) {

                        LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                        i.remove();
                        // adRemoveApprovalQueue.entityRemove();
                        em.remove(adRemoveApprovalQueue);
                    }
                }
            }

        } catch (GlobalRecordAlreadyDeletedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (GlobalTransactionAlreadyPostedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (GlobalTransactionAlreadyVoidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (GlobalTransactionAlreadyVoidPostedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (GlJREffectiveDateNoPeriodExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (GlobalJournalNotBalanceException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (GlobalInventoryDateException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (AdPRFCoaGlVarianceAccountNotFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer companyCode) {

        Debug.print("ApApprovalControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods
    private double convertForeignToFunctionalCurrency(Integer currencyCode, String currencyName, Date conversionDate, double conversionRate, double AMOUNT, Integer companyCode) {

        Debug.print("ApApprovalControllerBean convertForeignToFunctionalCurrency");

        LocalAdCompany adCompany = null;

        // get company and extended precision

        try {

            adCompany = adCompanyHome.findByPrimaryKey(companyCode);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // Convert to functional currency if necessary

        if (conversionRate != 1 && conversionRate != 0) {

            AMOUNT = AMOUNT * conversionRate;

        } else if (conversionDate != null) {

            try {

                // Get functional currency rate

                LocalGlFunctionalCurrencyRate glReceiptFunctionalCurrencyRate = null;

                if (!currencyName.equals("USD")) {

                    glReceiptFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(currencyCode, conversionDate, companyCode);

                    AMOUNT = AMOUNT * glReceiptFunctionalCurrencyRate.getFrXToUsd();
                }

                // Get set of book functional currency rate if necessary

                if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                    LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), conversionDate, companyCode);

                    AMOUNT = AMOUNT / glCompanyFunctionalCurrencyRate.getFrXToUsd();
                }

            } catch (Exception ex) {

                throw new EJBException(ex.getMessage());
            }
        }

        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    private void executeApPoPost(Integer PO_CODE, String username, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ApReceivingItemEntryControllerBean executeApPoPost");

        LocalApPurchaseOrder apPurchaseOrder = null;

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            // validate if receiving item/debit memo is already deleted

            try {

                apPurchaseOrder = apPurchaseOrderHome.findByPrimaryKey(PO_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if receiving item is already posted or void

            if (apPurchaseOrder.getPoPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();

            } else if (apPurchaseOrder.getPoVoid() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyVoidException();
            }

            // post receiving item

            Collection apPurchaseOrderLines = apPurchaseOrder.getApPurchaseOrderLines();

            Iterator c = apPurchaseOrderLines.iterator();

            while (c.hasNext()) {

                LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) c.next();
                LocalInvItemLocation invItemLocation = apPurchaseOrderLine.getInvItemLocation();

                if (apPurchaseOrderLine.getPlQuantity() == 0) continue;

                // start date validation
                if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                    Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(apPurchaseOrder.getPoDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                    if (!invNegTxnCosting.isEmpty())
                        throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                }

                String II_NM = apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiName();
                String LOC_NM = apPurchaseOrderLine.getInvItemLocation().getInvLocation().getLocName();

                double QTY_RCVD = this.convertByUomFromAndItemAndQuantity(apPurchaseOrderLine.getInvUnitOfMeasure(), apPurchaseOrderLine.getInvItemLocation().getInvItem(), apPurchaseOrderLine.getPlQuantity(), apPurchaseOrderLine.getPlConversionFactor(), companyCode);

                LocalInvCosting invCosting = null;

                try {

                    invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(apPurchaseOrder.getPoDate(), II_NM, LOC_NM, branchCode, companyCode);

                } catch (FinderException ex) {

                }

                double ADDON_COST = apPurchaseOrderLine.getPlAddonCost();
                double COST = this.convertForeignToFunctionalCurrency(apPurchaseOrder.getGlFunctionalCurrency().getFcCode(), apPurchaseOrder.getGlFunctionalCurrency().getFcName(), apPurchaseOrder.getPoConversionDate(), apPurchaseOrder.getPoConversionRate(), apPurchaseOrderLine.getPlUnitCost(), companyCode);

                double AMOUNT = (COST * QTY_RCVD) + ADDON_COST;

                // CREATE COSTING
                if (invCosting == null) {

                    this.postToInvPo(apPurchaseOrderLine, apPurchaseOrder.getPoDate(), QTY_RCVD, AMOUNT, QTY_RCVD, AMOUNT, 0d, null, branchCode, companyCode);

                } else {

                    this.postToInvPo(apPurchaseOrderLine, apPurchaseOrder.getPoDate(), QTY_RCVD, AMOUNT, invCosting.getCstRemainingQuantity() + QTY_RCVD, invCosting.getCstRemainingValue() + (AMOUNT), 0d, null, branchCode, companyCode);
                }
            }

            // set receiving item post status

            apPurchaseOrder.setPoPosted(EJBCommon.TRUE);
            apPurchaseOrder.setPoPostedBy(username);
            apPurchaseOrder.setPoDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            // post to gl if necessary

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate if date has no period and period is closed

            LocalGlSetOfBook glJournalSetOfBook = null;

            try {

                glJournalSetOfBook = glSetOfBookHome.findByDate(apPurchaseOrder.getPoDate(), companyCode);

            } catch (FinderException ex) {

                throw new GlJREffectiveDateNoPeriodExistException();
            }

            LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), apPurchaseOrder.getPoDate(), companyCode);

            if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P') {

                throw new GlJREffectiveDatePeriodClosedException();
            }

            // check if receiving item is balance if not check suspense posting

            LocalGlJournalLine glOffsetJournalLine = null;

            Collection apDistributionRecords = apDistributionRecordHome.findImportableDrByPoCode(apPurchaseOrder.getPoCode(), companyCode);

            Iterator j = apDistributionRecords.iterator();

            double TOTAL_DEBIT = 0d;
            double TOTAL_CREDIT = 0d;

            while (j.hasNext()) {

                LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) j.next();

                double DR_AMNT = EJBCommon.roundIt(apDistributionRecord.getDrAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision());

                if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

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

                    glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("ACCOUNTS PAYABLES", "RECEIVING ITEM", companyCode);

                } catch (FinderException ex) {

                    throw new GlobalJournalNotBalanceException();
                }

                if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                    glOffsetJournalLine = glJournalLineHome.create((short) (apDistributionRecords.size() + 1), EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);

                } else {

                    glOffsetJournalLine = glJournalLineHome.create((short) (apDistributionRecords.size() + 1), EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
                }

                LocalGlChartOfAccount glChartOfAccount = glSuspenseAccount.getGlChartOfAccount();
                // glChartOfAccount.addGlJournalLine(glOffsetJournalLine);
                glOffsetJournalLine.setGlChartOfAccount(glChartOfAccount);

            } else if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE && TOTAL_DEBIT != TOTAL_CREDIT) {

                throw new GlobalJournalNotBalanceException();
            }

            // create journal entry

            LocalGlJournal glJournal = glJournalHome.create(apPurchaseOrder.getPoReferenceNumber(), apPurchaseOrder.getPoDescription(), apPurchaseOrder.getPoDate(), 0.0d, null, apPurchaseOrder.getPoDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, username, new Date(), username, new Date(), null, null, username, EJBCommon.getGcCurrentDateWoTime().getTime(), apPurchaseOrder.getApSupplier().getSplTin(), apPurchaseOrder.getApSupplier().getSplName(), EJBCommon.FALSE, null, branchCode, companyCode);

            LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("ACCOUNTS PAYABLES", companyCode);
            glJournal.setGlJournalSource(glJournalSource);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
            glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("RECEIVING ITEMS", companyCode);
            glJournal.setGlJournalCategory(glJournalCategory);

            // create journal lines

            j = apDistributionRecords.iterator();

            while (j.hasNext()) {

                LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) j.next();

                double DR_AMNT = EJBCommon.roundIt(apDistributionRecord.getDrAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision());

                LocalGlJournalLine glJournalLine = glJournalLineHome.create(apDistributionRecord.getDrLine(), apDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

                glJournalLine.setGlChartOfAccount(apDistributionRecord.getGlChartOfAccount());
                glJournalLine.setGlJournal(glJournal);
                apDistributionRecord.setDrImported(EJBCommon.TRUE);

                // for FOREX revaluation

                if ((!Objects.equals(apPurchaseOrder.getGlFunctionalCurrency().getFcCode(), adCompany.getGlFunctionalCurrency().getFcCode())) && glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null && (glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode().equals(apPurchaseOrder.getGlFunctionalCurrency().getFcCode()))) {

                    double conversionRate = 1;

                    if (apPurchaseOrder.getPoConversionRate() != 0 && apPurchaseOrder.getPoConversionRate() != 1) {

                        conversionRate = apPurchaseOrder.getPoConversionRate();

                    } else if (apPurchaseOrder.getPoConversionDate() != null) {

                        conversionRate = this.getFrRateByFrNameAndFrDate(glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), companyCode);
                    }

                    Collection glForexLedgers = null;

                    try {

                        glForexLedgers = glForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(apPurchaseOrder.getPoDate(), glJournalLine.getGlChartOfAccount().getCoaCode(), companyCode);

                    } catch (FinderException ex) {

                    }

                    LocalGlForexLedger glForexLedger = (glForexLedgers.isEmpty() || glForexLedgers == null) ? null : (LocalGlForexLedger) glForexLedgers.iterator().next();

                    int FRL_LN = (glForexLedger != null && glForexLedger.getFrlDate().compareTo(apPurchaseOrder.getPoDate()) == 0) ? glForexLedger.getFrlLine().intValue() + 1 : 1;

                    // compute balance
                    double COA_FRX_BLNC = glForexLedger == null ? 0 : glForexLedger.getFrlBalance();
                    double FRL_AMNT = apDistributionRecord.getDrAmount();

                    if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET"))
                        FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                    else FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);

                    COA_FRX_BLNC = COA_FRX_BLNC + FRL_AMNT;

                    glForexLedger = glForexLedgerHome.create(apPurchaseOrder.getPoDate(), new Integer(FRL_LN), "OTH", FRL_AMNT, conversionRate, COA_FRX_BLNC, 0d, companyCode);

                    // glJournalLine.getGlChartOfAccount().addGlForexLedger(glForexLedger);
                    glForexLedger.setGlChartOfAccount(glJournalLine.getGlChartOfAccount());

                    // propagate balances
                    try {

                        glForexLedgers = glForexLedgerHome.findByGreaterThanFrlDateAndCoaCode(glForexLedger.getFrlDate(), glForexLedger.getGlChartOfAccount().getCoaCode(), glForexLedger.getFrlAdCompany());

                    } catch (FinderException ex) {

                    }

                    Iterator itrFrl = glForexLedgers.iterator();

                    while (itrFrl.hasNext()) {

                        glForexLedger = (LocalGlForexLedger) itrFrl.next();
                        FRL_AMNT = apDistributionRecord.getDrAmount();

                        if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET"))
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                        else FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);

                        glForexLedger.setFrlBalance(glForexLedger.getFrlBalance() + FRL_AMNT);
                    }
                }
            }

            if (glOffsetJournalLine != null) {

                glOffsetJournalLine.setGlJournal(glJournal);
            }

            // post journal to gl

            Collection glJournalLines = glJournalLineHome.findByJrCode(glJournal.getJrCode(), companyCode);
            Iterator i = glJournalLines.iterator();

            while (i.hasNext()) {

                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) i.next();

                // post current to current acv

                this.postToGl(glAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), true, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                // post to subsequent acvs (propagate)

                Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), glAccountingCalendarValue.getAcvPeriodNumber(), companyCode);

                Iterator acvsIter = glSubsequentAccountingCalendarValues.iterator();

                while (acvsIter.hasNext()) {

                    LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) acvsIter.next();

                    this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                }

                // post to subsequent years if necessary

                Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode);

                if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                    adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                    LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome.findByCoaAccountNumber(adCompany.getCmpRetainedEarnings(), companyCode);

                    Iterator sobIter = glSubsequentSetOfBooks.iterator();

                    while (sobIter.hasNext()) {

                        LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) sobIter.next();

                        String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                        // post to subsequent acvs of subsequent set of book(propagate)

                        Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                        Iterator acvIter = glAccountingCalendarValues.iterator();

                        while (acvIter.hasNext()) {

                            LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) acvIter.next();

                            if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY") || ACCOUNT_TYPE.equals("OWNERS EQUITY")) {

                                this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                            } else { // revenue & expense

                                this.postToGl(glSubsequentAccountingCalendarValue, glRetainedEarningsAccount, false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                            }
                        }

                        if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) break;
                    }
                }
            }

        } catch (GlJREffectiveDateNoPeriodExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (GlobalJournalNotBalanceException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (GlobalRecordAlreadyDeletedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (GlobalTransactionAlreadyPostedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (GlobalTransactionAlreadyVoidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (AdPRFCoaGlVarianceAccountNotFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void executeApVouPost(Integer VOU_CODE, String username, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ApApprovalControllerBean executeApVouPost");

        LocalApVoucher apVoucher = null;
        LocalApVoucher apDebitedVoucher = null;

        try {

            // validate if voucher/debit memo is already deleted

            try {

                apVoucher = apVoucherHome.findByPrimaryKey(VOU_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if voucher/debit memo is already posted or void

            if (apVoucher.getVouPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();

            } else if (apVoucher.getVouVoid() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyVoidException();
            }

            // post voucher/debit memo
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            if (apVoucher.getVouDebitMemo() == EJBCommon.FALSE) {

                // increase supplier balance

                double VOU_AMNT = this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apVoucher.getVouAmountDue(), companyCode);

                this.post(apVoucher.getVouDate(), VOU_AMNT, apVoucher.getApSupplier(), companyCode);

                Collection apVoucherLineItems = apVoucher.getApVoucherLineItems();

                if (apVoucherLineItems != null && !apVoucherLineItems.isEmpty()) {

                    Iterator c = apVoucherLineItems.iterator();

                    while (c.hasNext()) {

                        LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) c.next();

                        String II_NM = apVoucherLineItem.getInvItemLocation().getInvItem().getIiName();
                        String LOC_NM = apVoucherLineItem.getInvItemLocation().getInvLocation().getLocName();

                        double QTY_RCVD = this.convertByUomFromAndItemAndQuantity(apVoucherLineItem.getInvUnitOfMeasure(), apVoucherLineItem.getInvItemLocation().getInvItem(), apVoucherLineItem.getVliQuantity(), companyCode);

                        LocalInvCosting invCosting = null;

                        try {

                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(apVoucher.getVouDate(), II_NM, LOC_NM, branchCode, companyCode);

                        } catch (FinderException ex) {

                        }

                        double COST = apVoucherLineItem.getVliUnitCost();
                        // CREATE COSTING
                        if (invCosting == null) {

                            this.postToInvVou(apVoucherLineItem, apVoucher.getVouDate(), QTY_RCVD, COST * QTY_RCVD, QTY_RCVD, (QTY_RCVD * COST), 0d, null, branchCode, companyCode);

                        } else {

                            this.postToInvVou(apVoucherLineItem, apVoucher.getVouDate(), QTY_RCVD, COST * QTY_RCVD, invCosting.getCstRemainingQuantity() + QTY_RCVD, invCosting.getCstRemainingValue() + (QTY_RCVD * COST), 0d, null, branchCode, companyCode);
                        }

                        /*
                         * // POST TO PROJECTING
                         *
                         * if(apVoucherLineItem.getInvItemLocation().getInvItem().getIiIsVatRelief() ==
                         * EJBCommon.TRUE && apVoucherLineItem.getPmProject() != null ) {
                         *
                         * this.postToPjt(apVoucherLineItem, apVoucher.getVouDate(), COST * QTY_RCVD, null,
                         * branchCode, companyCode);
                         *
                         * }
                         */

                    }
                }

            } else {

                // get debited voucher

                apDebitedVoucher = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(apVoucher.getVouDmVoucherNumber(), EJBCommon.FALSE, branchCode, companyCode);

                // decrease supplier balance

                double VOU_AMNT = this.convertForeignToFunctionalCurrency(apDebitedVoucher.getGlFunctionalCurrency().getFcCode(), apDebitedVoucher.getGlFunctionalCurrency().getFcName(), apDebitedVoucher.getVouConversionDate(), apDebitedVoucher.getVouConversionRate(), apVoucher.getVouBillAmount(), companyCode) * -1;

                this.post(apVoucher.getVouDate(), VOU_AMNT, apVoucher.getApSupplier(), companyCode);

                // decrease voucher and vps amounts and release lock

                double DEBIT_PERCENT = EJBCommon.roundIt(apVoucher.getVouBillAmount() / apDebitedVoucher.getVouAmountDue(), (short) 6);

                apDebitedVoucher.setVouAmountPaid(apDebitedVoucher.getVouAmountPaid() + apVoucher.getVouBillAmount());

                double TOTAL_VOUCHER_PAYMENT_SCHEDULE = 0d;

                Collection apVoucherPaymentSchedules = apDebitedVoucher.getApVoucherPaymentSchedules();

                Iterator i = apVoucherPaymentSchedules.iterator();

                while (i.hasNext()) {

                    LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = (LocalApVoucherPaymentSchedule) i.next();

                    double VOUCHER_PAYMENT_SCHEDULE_AMOUNT = 0;

                    // if last payment schedule subtract to avoid rounding difference error

                    if (i.hasNext()) {

                        VOUCHER_PAYMENT_SCHEDULE_AMOUNT = EJBCommon.roundIt(apVoucherPaymentSchedule.getVpsAmountDue() * DEBIT_PERCENT, this.getGlFcPrecisionUnit(companyCode));

                    } else {

                        VOUCHER_PAYMENT_SCHEDULE_AMOUNT = apVoucher.getVouBillAmount() - TOTAL_VOUCHER_PAYMENT_SCHEDULE;
                    }

                    apVoucherPaymentSchedule.setVpsAmountPaid(apVoucherPaymentSchedule.getVpsAmountPaid() + VOUCHER_PAYMENT_SCHEDULE_AMOUNT);

                    apVoucherPaymentSchedule.setVpsLock(EJBCommon.FALSE);

                    TOTAL_VOUCHER_PAYMENT_SCHEDULE += VOUCHER_PAYMENT_SCHEDULE_AMOUNT;
                }

                Collection apVoucherLineItems = apVoucher.getApVoucherLineItems();

                if (apVoucherLineItems != null && !apVoucherLineItems.isEmpty()) {

                    Iterator c = apVoucherLineItems.iterator();

                    while (c.hasNext()) {

                        LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) c.next();

                        String II_NM = apVoucherLineItem.getInvItemLocation().getInvItem().getIiName();
                        String LOC_NM = apVoucherLineItem.getInvItemLocation().getInvLocation().getLocName();

                        double QTY_RCVD = this.convertByUomFromAndItemAndQuantity(apVoucherLineItem.getInvUnitOfMeasure(), apVoucherLineItem.getInvItemLocation().getInvItem(), apVoucherLineItem.getVliQuantity(), companyCode);

                        LocalInvCosting invCosting = null;
                        // CREATE COSTING
                        try {

                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(apVoucher.getVouDate(), II_NM, LOC_NM, branchCode, companyCode);

                        } catch (FinderException ex) {

                        }

                        double COST = apVoucherLineItem.getVliUnitCost();

                        if (invCosting == null) {

                            this.postToInvVou(apVoucherLineItem, apVoucher.getVouDate(), -QTY_RCVD, -COST * QTY_RCVD, -QTY_RCVD, -COST * QTY_RCVD, 0d, null, branchCode, companyCode);

                        } else {

                            this.postToInvVou(apVoucherLineItem, apVoucher.getVouDate(), -QTY_RCVD, -COST * QTY_RCVD, invCosting.getCstRemainingQuantity() - QTY_RCVD, invCosting.getCstRemainingValue() - (QTY_RCVD * COST), 0d, null, branchCode, companyCode);
                        }
                    }
                }
            }

            // set voucher post status

            apVoucher.setVouPosted(EJBCommon.TRUE);
            apVoucher.setVouPostedBy(username);
            apVoucher.setVouDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            // post to gl if necessary

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            if (adPreference.getPrfApGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                // validate if date has no period and period is closed

                LocalGlSetOfBook glJournalSetOfBook = null;

                try {

                    glJournalSetOfBook = glSetOfBookHome.findByDate(apVoucher.getVouDate(), companyCode);

                } catch (FinderException ex) {

                    throw new GlJREffectiveDateNoPeriodExistException();
                }

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), apVoucher.getVouDate(), companyCode);

                if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P') {

                    throw new GlJREffectiveDatePeriodClosedException();
                }

                // check if voucher is balance if not check suspense posting

                LocalGlJournalLine glOffsetJournalLine = null;

                Collection apDistributionRecords = apDistributionRecordHome.findImportableDrByVouCode(apVoucher.getVouCode(), companyCode);

                Iterator j = apDistributionRecords.iterator();

                double TOTAL_DEBIT = 0d;
                double TOTAL_CREDIT = 0d;

                while (j.hasNext()) {

                    LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) j.next();

                    double DR_AMNT = 0d;

                    if (apVoucher.getVouDebitMemo() == EJBCommon.FALSE) {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apDistributionRecord.getDrAmount(), companyCode);

                    } else {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(apDebitedVoucher.getGlFunctionalCurrency().getFcCode(), apDebitedVoucher.getGlFunctionalCurrency().getFcName(), apDebitedVoucher.getVouConversionDate(), apDebitedVoucher.getVouConversionRate(), apDistributionRecord.getDrAmount(), companyCode);
                    }

                    if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

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

                        glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("ACCOUNTS PAYABLES", apVoucher.getVouDebitMemo() == EJBCommon.FALSE ? "VOUCHERS" : "DEBIT MEMOS", companyCode);

                    } catch (FinderException ex) {

                        throw new GlobalJournalNotBalanceException();
                    }

                    if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                        glOffsetJournalLine = glJournalLineHome.create((short) (apDistributionRecords.size() + 1), EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);

                    } else {

                        glOffsetJournalLine = glJournalLineHome.create((short) (apDistributionRecords.size() + 1), EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
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

                String VB_NNAME = "";

                try {

                    VB_NNAME = apVoucher.getApVoucherBatch().getVbName();

                } catch (Exception ex) {

                    VB_NNAME = "";
                }

                try {

                    if (adPreference.getPrfEnableApVoucherBatch() == EJBCommon.TRUE) {

                        glJournalBatch = glJournalBatchHome.findByJbName(VB_NNAME, branchCode, companyCode);

                    } else {

                        glJournalBatch = glJournalBatchHome.findByJbName(VB_NNAME, branchCode, companyCode);
                    }

                } catch (FinderException ex) {
                }

                if (adPreference.getPrfEnableGlJournalBatch() == EJBCommon.TRUE && glJournalBatch == null && !VB_NNAME.equals("")) {

                    if (adPreference.getPrfEnableApVoucherBatch() == EJBCommon.TRUE) {

                        glJournalBatch = glJournalBatchHome.create(apVoucher.getApVoucherBatch().getVbName(), apVoucher.getApVoucherBatch().getVbName(), "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), username, branchCode, companyCode);

                    } else {

                        glJournalBatch = glJournalBatchHome.create(apVoucher.getApVoucherBatch().getVbName(), apVoucher.getApVoucherBatch().getVbName(), "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), username, branchCode, companyCode);
                    }
                }

                // create journal entry

                LocalGlJournal glJournal = glJournalHome.create(apVoucher.getVouDebitMemo() == 1 ? apVoucher.getVouDmVoucherNumber() : apVoucher.getVouReferenceNumber(), apVoucher.getVouDescription(), apVoucher.getVouDate(), 0.0d, null, apVoucher.getVouDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, username, new Date(), username, new Date(), null, null, username, EJBCommon.getGcCurrentDateWoTime().getTime(), apVoucher.getApSupplier().getSplTin(), apVoucher.getApSupplier().getSplName(), EJBCommon.FALSE, null, branchCode, companyCode);

                LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("ACCOUNTS PAYABLES", companyCode);
                glJournal.setGlJournalSource(glJournalSource);

                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

                LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName(apVoucher.getVouDebitMemo() == EJBCommon.FALSE ? "VOUCHERS" : "DEBIT MEMOS", companyCode);
                glJournal.setGlJournalCategory(glJournalCategory);

                if (glJournalBatch != null) {

                    // glJournalBatch.addGlJournal(glJournal);
                    glJournal.setGlJournalBatch(glJournalBatch);
                }

                // create journal lines

                j = apDistributionRecords.iterator();

                while (j.hasNext()) {

                    LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) j.next();

                    double DR_AMNT = 0d;

                    if (apVoucher.getVouDebitMemo() == EJBCommon.FALSE) {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apDistributionRecord.getDrAmount(), companyCode);

                    } else {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(apDebitedVoucher.getGlFunctionalCurrency().getFcCode(), apDebitedVoucher.getGlFunctionalCurrency().getFcName(), apDebitedVoucher.getVouConversionDate(), apDebitedVoucher.getVouConversionRate(), apDistributionRecord.getDrAmount(), companyCode);
                    }

                    LocalGlJournalLine glJournalLine = glJournalLineHome.create(apDistributionRecord.getDrLine(), apDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);
                    glJournalLine.setGlChartOfAccount(apDistributionRecord.getGlChartOfAccount());
                    glJournalLine.setGlJournal(glJournal);

                    apDistributionRecord.setDrImported(EJBCommon.TRUE);

                    // for FOREX revaluation

                    LocalApVoucher apVoucherTemp = apVoucher.getVouDebitMemo() == EJBCommon.FALSE ? apVoucher : apDebitedVoucher;

                    if ((!Objects.equals(apVoucherTemp.getGlFunctionalCurrency().getFcCode(), adCompany.getGlFunctionalCurrency().getFcCode())) && glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null && (glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode().equals(apVoucherTemp.getGlFunctionalCurrency().getFcCode()))) {

                        double conversionRate = 1;

                        if (apVoucherTemp.getVouConversionRate() != 0 && apVoucherTemp.getVouConversionRate() != 1) {

                            conversionRate = apVoucherTemp.getVouConversionRate();

                        } else if (apVoucherTemp.getVouConversionDate() != null) {

                            conversionRate = this.getFrRateByFrNameAndFrDate(glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), companyCode);
                        }

                        Collection glForexLedgers = null;

                        try {

                            glForexLedgers = glForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(apVoucherTemp.getVouDate(), glJournalLine.getGlChartOfAccount().getCoaCode(), companyCode);

                        } catch (FinderException ex) {

                        }

                        LocalGlForexLedger glForexLedger = (glForexLedgers.isEmpty() || glForexLedgers == null) ? null : (LocalGlForexLedger) glForexLedgers.iterator().next();

                        int FRL_LN = (glForexLedger != null && glForexLedger.getFrlDate().compareTo(apVoucherTemp.getVouDate()) == 0) ? glForexLedger.getFrlLine().intValue() + 1 : 1;

                        // compute balance
                        double COA_FRX_BLNC = glForexLedger == null ? 0 : glForexLedger.getFrlBalance();
                        double FRL_AMNT = apDistributionRecord.getDrAmount();

                        if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET"))
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                        else FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);

                        COA_FRX_BLNC = COA_FRX_BLNC + FRL_AMNT;

                        glForexLedger = glForexLedgerHome.create(apVoucherTemp.getVouDate(), new Integer(FRL_LN), apVoucher.getVouDebitMemo() == EJBCommon.FALSE ? "APV" : "DM", FRL_AMNT, conversionRate, COA_FRX_BLNC, 0d, companyCode);

                        // glJournalLine.getGlChartOfAccount().addGlForexLedger(glForexLedger);
                        glForexLedger.setGlChartOfAccount(glJournalLine.getGlChartOfAccount());

                        // propagate balances
                        try {

                            glForexLedgers = glForexLedgerHome.findByGreaterThanFrlDateAndCoaCode(glForexLedger.getFrlDate(), glForexLedger.getGlChartOfAccount().getCoaCode(), glForexLedger.getFrlAdCompany());

                        } catch (FinderException ex) {

                        }

                        Iterator itrFrl = glForexLedgers.iterator();

                        while (itrFrl.hasNext()) {

                            glForexLedger = (LocalGlForexLedger) itrFrl.next();
                            FRL_AMNT = apDistributionRecord.getDrAmount();

                            if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET"))
                                FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                            else FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);

                            glForexLedger.setFrlBalance(glForexLedger.getFrlBalance() + FRL_AMNT);
                        }
                    }
                }

                if (glOffsetJournalLine != null) {

                    // glJournal.addGlJournalLine(glOffsetJournalLine);
                    glOffsetJournalLine.setGlJournal(glJournal);
                }

                // post journal to gl

                Collection glJournalLines = glJournalLineHome.findByJrCode(glJournal.getJrCode(), companyCode);
                Iterator i = glJournalLines.iterator();

                while (i.hasNext()) {

                    LocalGlJournalLine glJournalLine = (LocalGlJournalLine) i.next();

                    // post current to current acv

                    this.postToGl(glAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), true, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                    // post to subsequent acvs (propagate)

                    Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), glAccountingCalendarValue.getAcvPeriodNumber(), companyCode);

                    Iterator acvsIter = glSubsequentAccountingCalendarValues.iterator();

                    while (acvsIter.hasNext()) {

                        LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) acvsIter.next();

                        this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                    }

                    // post to subsequent years if necessary

                    Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode);

                    if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                        adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                        LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome.findByCoaAccountNumber(adCompany.getCmpRetainedEarnings(), companyCode);

                        Iterator sobIter = glSubsequentSetOfBooks.iterator();

                        while (sobIter.hasNext()) {

                            LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) sobIter.next();

                            String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                            // post to subsequent acvs of subsequent set of book(propagate)

                            Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                            Iterator acvIter = glAccountingCalendarValues.iterator();

                            while (acvIter.hasNext()) {

                                LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) acvIter.next();

                                if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY") || ACCOUNT_TYPE.equals("OWNERS EQUITY")) {

                                    this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                                } else { // revenue & expense

                                    this.postToGl(glSubsequentAccountingCalendarValue, glRetainedEarningsAccount, false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                                }
                            }

                            if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) break;
                        }
                    }
                }
            }

        } catch (GlJREffectiveDateNoPeriodExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (GlobalJournalNotBalanceException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (GlobalRecordAlreadyDeletedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (GlobalTransactionAlreadyPostedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (GlobalTransactionAlreadyVoidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (AdPRFCoaGlVarianceAccountNotFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void executeApChkPost(Integer CHK_CODE, String username, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException {

        Debug.print("ApApprovalControllerBean executeApChkPost");


        LocalApCheck apCheck = null;

        try {

            // validate if check is already deleted

            try {

                apCheck = apCheckHome.findByPrimaryKey(CHK_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if check is already posted

            if (apCheck.getChkVoid() == EJBCommon.FALSE && apCheck.getChkPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();

                // validate if check void is already posted

            } else if (apCheck.getChkVoid() == EJBCommon.TRUE && apCheck.getChkVoidPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyVoidPostedException();
            }

            // post check

            if (apCheck.getChkVoid() == EJBCommon.FALSE && apCheck.getChkPosted() == EJBCommon.FALSE) {

                if (apCheck.getChkType().equals("PAYMENT")) {

                    // increase amount paid in voucher payment schedules and voucher

                    double CHK_CRDTS = 0d;

                    Collection apAppliedVouchers = apCheck.getApAppliedVouchers();

                    Iterator i = apAppliedVouchers.iterator();

                    while (i.hasNext()) {

                        LocalApAppliedVoucher apAppliedVoucher = (LocalApAppliedVoucher) i.next();

                        LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = apAppliedVoucher.getApVoucherPaymentSchedule();

                        double AMOUNT_PAID = apAppliedVoucher.getAvApplyAmount() + apAppliedVoucher.getAvDiscountAmount() + apAppliedVoucher.getAvTaxWithheld();

                        CHK_CRDTS += this.convertForeignToFunctionalCurrency(apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getGlFunctionalCurrency().getFcCode(), apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getGlFunctionalCurrency().getFcName(), apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouConversionDate(), apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouConversionRate(), apAppliedVoucher.getAvDiscountAmount() + apAppliedVoucher.getAvTaxWithheld(), companyCode);

                        apVoucherPaymentSchedule.setVpsAmountPaid(EJBCommon.roundIt(apVoucherPaymentSchedule.getVpsAmountPaid() + AMOUNT_PAID, this.getGlFcPrecisionUnit(companyCode)));

                        apVoucherPaymentSchedule.getApVoucher().setVouAmountPaid(EJBCommon.roundIt(apVoucherPaymentSchedule.getApVoucher().getVouAmountPaid() + AMOUNT_PAID, this.getGlFcPrecisionUnit(companyCode)));

                        // release voucher lock

                        apVoucherPaymentSchedule.setVpsLock(EJBCommon.FALSE);
                    }

                    // decrease supplier balance

                    double CHK_AMNT = this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apCheck.getChkAmount(), companyCode);

                    this.post(apCheck.getChkDate(), (CHK_AMNT + CHK_CRDTS) * -1, apCheck.getApSupplier(), companyCode);

                } else if (apCheck.getChkType().equals("DIRECT") && !apCheck.getApVoucherLineItems().isEmpty()) {

                    Iterator c = apCheck.getApVoucherLineItems().iterator();

                    while (c.hasNext()) {

                        LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) c.next();

                        String II_NM = apVoucherLineItem.getInvItemLocation().getInvItem().getIiName();
                        String LOC_NM = apVoucherLineItem.getInvItemLocation().getInvLocation().getLocName();

                        double QTY_RCVD = this.convertByUomFromAndItemAndQuantity(apVoucherLineItem.getInvUnitOfMeasure(), apVoucherLineItem.getInvItemLocation().getInvItem(), apVoucherLineItem.getVliQuantity(), companyCode);

                        LocalInvCosting invCosting = null;
                        // CREATE COSTING
                        try {

                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(apCheck.getChkDate(), II_NM, LOC_NM, branchCode, companyCode);

                        } catch (FinderException ex) {

                        }

                        double COST = apVoucherLineItem.getVliUnitCost();
                        // CREATE COSTING
                        if (invCosting == null) {

                            this.postToInvVou(apVoucherLineItem, apCheck.getChkDate(), QTY_RCVD, COST * QTY_RCVD, QTY_RCVD, COST * QTY_RCVD, 0d, null, branchCode, companyCode);

                        } else {

                            this.postToInvVou(apVoucherLineItem, apCheck.getChkDate(), QTY_RCVD, QTY_RCVD * COST, invCosting.getCstRemainingQuantity() + QTY_RCVD, invCosting.getCstRemainingValue() + (QTY_RCVD * COST), 0d, null, branchCode, companyCode);
                        }
                    }
                }

                // decrease bank balance

                LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(apCheck.getAdBankAccount().getBaCode());

                try {

                    // find bankaccount balance before or equal check date

                    Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(apCheck.getChkCheckDate(), apCheck.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                    if (!adBankAccountBalances.isEmpty()) {

                        // get last check

                        ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                        if (adBankAccountBalance.getBabDate().before(apCheck.getChkCheckDate())) {

                            // create new balance

                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(apCheck.getChkCheckDate(), adBankAccountBalance.getBabBalance() - apCheck.getChkAmount(), "BOOK", companyCode);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);

                        } else { // equals to check date

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - apCheck.getChkAmount());
                        }

                    } else {

                        // create new balance

                        LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(apCheck.getChkCheckDate(), (0 - apCheck.getChkAmount()), "BOOK", companyCode);
                        adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                    }

                    // propagate to subsequent balances if necessary

                    adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(apCheck.getChkCheckDate(), apCheck.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                    Iterator i = adBankAccountBalances.iterator();

                    while (i.hasNext()) {

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) i.next();

                        adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - apCheck.getChkAmount());
                    }

                } catch (Exception ex) {

                    ex.printStackTrace();
                }

                // set check post status

                apCheck.setChkPosted(EJBCommon.TRUE);
                apCheck.setChkPostedBy(username);
                apCheck.setChkDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            } else if (apCheck.getChkVoid() == EJBCommon.TRUE && apCheck.getChkVoidPosted() == EJBCommon.FALSE) { // void
                // check

                if (apCheck.getChkType().equals("PAYMENT")) {

                    // decrease amount paid in voucher payment schedules and voucher

                    double CHK_CRDTS = 0d;

                    Collection apAppliedVouchers = apCheck.getApAppliedVouchers();

                    Iterator i = apAppliedVouchers.iterator();

                    while (i.hasNext()) {

                        LocalApAppliedVoucher apAppliedVoucher = (LocalApAppliedVoucher) i.next();

                        LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = apAppliedVoucher.getApVoucherPaymentSchedule();

                        double AMOUNT_PAID = apAppliedVoucher.getAvApplyAmount() + apAppliedVoucher.getAvDiscountAmount() + apAppliedVoucher.getAvTaxWithheld();

                        CHK_CRDTS += this.convertForeignToFunctionalCurrency(apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getGlFunctionalCurrency().getFcCode(), apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getGlFunctionalCurrency().getFcName(), apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouConversionDate(), apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouConversionRate(), apAppliedVoucher.getAvDiscountAmount() + apAppliedVoucher.getAvTaxWithheld(), companyCode);

                        apVoucherPaymentSchedule.setVpsAmountPaid(EJBCommon.roundIt(apVoucherPaymentSchedule.getVpsAmountPaid() - AMOUNT_PAID, this.getGlFcPrecisionUnit(companyCode)));

                        apVoucherPaymentSchedule.getApVoucher().setVouAmountPaid(EJBCommon.roundIt(apVoucherPaymentSchedule.getApVoucher().getVouAmountPaid() - AMOUNT_PAID, this.getGlFcPrecisionUnit(companyCode)));
                    }

                    // increase supplier balance

                    double CHK_AMNT = this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apCheck.getChkAmount(), companyCode);

                    this.post(apCheck.getChkDate(), CHK_AMNT + CHK_CRDTS, apCheck.getApSupplier(), companyCode);

                } else if (apCheck.getChkType().equals("DIRECT") && !apCheck.getApVoucherLineItems().isEmpty()) {

                    // VOIDING DIRECT PAYMENT

                    Iterator c = apCheck.getApVoucherLineItems().iterator();

                    while (c.hasNext()) {

                        LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) c.next();

                        String II_NM = apVoucherLineItem.getInvItemLocation().getInvItem().getIiName();
                        String LOC_NM = apVoucherLineItem.getInvItemLocation().getInvLocation().getLocName();

                        double QTY_RCVD = this.convertByUomFromAndItemAndQuantity(apVoucherLineItem.getInvUnitOfMeasure(), apVoucherLineItem.getInvItemLocation().getInvItem(), apVoucherLineItem.getVliQuantity(), companyCode);

                        LocalInvCosting invCosting = null;
                        // CREATE COSTING
                        try {

                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(apCheck.getChkDate(), II_NM, LOC_NM, branchCode, companyCode);

                        } catch (FinderException ex) {

                        }

                        double COST = apVoucherLineItem.getVliUnitCost();

                        if (invCosting == null) {

                            this.postToInvVou(apVoucherLineItem, apCheck.getChkDate(), -QTY_RCVD, -COST * QTY_RCVD, -QTY_RCVD, -COST * QTY_RCVD, 0d, null, branchCode, companyCode);

                        } else {

                            this.postToInvVou(apVoucherLineItem, apCheck.getChkDate(), -QTY_RCVD, -(COST * QTY_RCVD), invCosting.getCstRemainingQuantity() - QTY_RCVD, invCosting.getCstRemainingValue() - (QTY_RCVD * COST), 0d, null, branchCode, companyCode);
                        }
                    }
                }

                // increase bank balance

                LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(apCheck.getAdBankAccount().getBaCode());

                try {

                    // find bankaccount balance before or equal check date

                    Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(apCheck.getChkCheckDate(), apCheck.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                    if (!adBankAccountBalances.isEmpty()) {

                        // get last check

                        ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                        if (adBankAccountBalance.getBabDate().before(apCheck.getChkCheckDate())) {

                            // create new balance

                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(apCheck.getChkCheckDate(), adBankAccountBalance.getBabBalance() + apCheck.getChkAmount(), "BOOK", companyCode);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);

                        } else { // equals to check date

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + apCheck.getChkAmount());
                        }

                    } else {

                        // create new balance

                        LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(apCheck.getChkCheckDate(), (apCheck.getChkAmount()), "BOOK", companyCode);
                        adNewBankAccountBalance.setAdBankAccount(adBankAccount);

                    }

                    // propagate to subsequent balances if necessary

                    adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(apCheck.getChkCheckDate(), apCheck.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                    Iterator i = adBankAccountBalances.iterator();

                    while (i.hasNext()) {

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) i.next();

                        adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + apCheck.getChkAmount());
                    }

                } catch (Exception ex) {

                    ex.printStackTrace();
                }

                // set check post status

                apCheck.setChkVoidPosted(EJBCommon.TRUE);
                apCheck.setChkPostedBy(username);
                apCheck.setChkDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
            }

            // post to gl if necessary

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            if (adPreference.getPrfApGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                // validate if date has no period and period is closed

                LocalGlSetOfBook glJournalSetOfBook = null;

                try {

                    glJournalSetOfBook = glSetOfBookHome.findByDate(apCheck.getChkDate(), companyCode);

                } catch (FinderException ex) {

                    throw new GlJREffectiveDateNoPeriodExistException();
                }

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), apCheck.getChkDate(), companyCode);

                if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P') {

                    throw new GlJREffectiveDatePeriodClosedException();
                }

                // check if check is balance if not check suspense posting

                LocalGlJournalLine glOffsetJournalLine = null;

                Collection apDistributionRecords = null;

                if (apCheck.getChkVoid() == EJBCommon.FALSE) {

                    apDistributionRecords = apDistributionRecordHome.findImportableDrByDrReversedAndChkCode(EJBCommon.FALSE, apCheck.getChkCode(), companyCode);

                } else {

                    apDistributionRecords = apDistributionRecordHome.findImportableDrByDrReversedAndChkCode(EJBCommon.TRUE, apCheck.getChkCode(), companyCode);
                }

                Iterator j = apDistributionRecords.iterator();

                double TOTAL_DEBIT = 0d;
                double TOTAL_CREDIT = 0d;

                while (j.hasNext()) {

                    LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) j.next();

                    double DR_AMNT = 0d;

                    if (apDistributionRecord.getApAppliedVoucher() != null) {

                        LocalApVoucher apVoucher = apDistributionRecord.getApAppliedVoucher().getApVoucherPaymentSchedule().getApVoucher();

                        DR_AMNT = this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apDistributionRecord.getDrAmount(), companyCode);

                    } else {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apDistributionRecord.getDrAmount(), companyCode);
                    }

                    if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

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

                        glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("ACCOUNTS PAYABLES", "CHECKS", companyCode);

                    } catch (FinderException ex) {

                        throw new GlobalJournalNotBalanceException();
                    }

                    if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                        glOffsetJournalLine = glJournalLineHome.create((short) (apDistributionRecords.size() + 1), EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);

                    } else {

                        glOffsetJournalLine = glJournalLineHome.create((short) (apDistributionRecords.size() + 1), EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
                    }

                    LocalGlChartOfAccount glChartOfAccount = glSuspenseAccount.getGlChartOfAccount();
                    glOffsetJournalLine.setGlChartOfAccount(glChartOfAccount);

                } else if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE && TOTAL_DEBIT != TOTAL_CREDIT) {

                    throw new GlobalJournalNotBalanceException();
                }

                // create journal batch if necessary

                LocalGlJournalBatch glJournalBatch = null;
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");

                String CB_NAME = "";

                try {

                    CB_NAME = apCheck.getApCheckBatch().getCbName();

                } catch (Exception ex) {
                    CB_NAME = "";
                }

                try {

                    if (adPreference.getPrfEnableApCheckBatch() == EJBCommon.TRUE) {

                        glJournalBatch = glJournalBatchHome.findByJbName(CB_NAME, branchCode, companyCode);

                    } else {

                        glJournalBatch = glJournalBatchHome.findByJbName(CB_NAME, branchCode, companyCode);
                    }

                } catch (FinderException ex) {
                }

                if (adPreference.getPrfEnableGlJournalBatch() == EJBCommon.TRUE && glJournalBatch == null && !CB_NAME.equals("")) {

                    if (adPreference.getPrfEnableApCheckBatch() == EJBCommon.TRUE) {

                        glJournalBatch = glJournalBatchHome.create(apCheck.getApCheckBatch().getCbName(), apCheck.getApCheckBatch().getCbName(), "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), username, branchCode, companyCode);

                    } else {

                        glJournalBatch = glJournalBatchHome.create(apCheck.getApCheckBatch().getCbName(), apCheck.getApCheckBatch().getCbName(), "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), username, branchCode, companyCode);
                    }
                }

                // create journal entry

                LocalGlJournal glJournal = glJournalHome.create(apCheck.getChkReferenceNumber(), apCheck.getChkDescription(), apCheck.getChkDate(), 0.0d, null, apCheck.getChkDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, username, new Date(), username, new Date(), null, null, username, EJBCommon.getGcCurrentDateWoTime().getTime(), apCheck.getApSupplier().getSplTin(), apCheck.getApSupplier().getSplName(), EJBCommon.FALSE, null, branchCode, companyCode);

                LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("ACCOUNTS PAYABLES", companyCode);
                glJournal.setGlJournalSource(glJournalSource);

                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

                LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("CHECKS", companyCode);
                glJournal.setGlJournalCategory(glJournalCategory);

                if (glJournalBatch != null) {

                    // glJournalBatch.addGlJournal(glJournal);
                    glJournal.setGlJournalBatch(glJournalBatch);
                }

                // create journal lines

                j = apDistributionRecords.iterator();
                boolean firstFlag = true;

                while (j.hasNext()) {

                    LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) j.next();

                    double DR_AMNT = 0d;
                    LocalApVoucher apVoucher = null;

                    if (apDistributionRecord.getApAppliedVoucher() != null) {

                        apVoucher = apDistributionRecord.getApAppliedVoucher().getApVoucherPaymentSchedule().getApVoucher();

                        DR_AMNT = this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apDistributionRecord.getDrAmount(), companyCode);

                    } else {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apDistributionRecord.getDrAmount(), companyCode);
                    }

                    LocalGlJournalLine glJournalLine = glJournalLineHome.create(apDistributionRecord.getDrLine(), apDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);
                    glJournalLine.setGlChartOfAccount(apDistributionRecord.getGlChartOfAccount());
                    glJournalLine.setGlJournal(glJournal);

                    apDistributionRecord.setDrImported(EJBCommon.TRUE);

                    // for FOREX revaluation

                    int currencyCode = apDistributionRecord.getApAppliedVoucher() != null ? apVoucher.getGlFunctionalCurrency().getFcCode().intValue() : apCheck.getGlFunctionalCurrency().getFcCode().intValue();

                    if ((currencyCode != adCompany.getGlFunctionalCurrency().getFcCode().intValue()) && glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null && (currencyCode == glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode().intValue())) {

                        double conversionRate = apDistributionRecord.getApAppliedVoucher() != null ? apVoucher.getVouConversionRate() : apCheck.getChkConversionRate();

                        Date DATE = apDistributionRecord.getApAppliedVoucher() != null ? apVoucher.getVouConversionDate() : apCheck.getChkConversionDate();

                        if (DATE != null && (conversionRate == 0 || conversionRate == 1)) {

                            conversionRate = this.getFrRateByFrNameAndFrDate(glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), companyCode);

                        } else if (conversionRate == 0) {

                            conversionRate = 1;
                        }

                        Collection glForexLedgers = null;

                        DATE = apDistributionRecord.getApAppliedVoucher() != null ? apVoucher.getVouDate() : apCheck.getChkDate();

                        try {

                            glForexLedgers = glForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(DATE, glJournalLine.getGlChartOfAccount().getCoaCode(), companyCode);

                        } catch (FinderException ex) {

                        }

                        LocalGlForexLedger glForexLedger = (glForexLedgers.isEmpty() || glForexLedgers == null) ? null : (LocalGlForexLedger) glForexLedgers.iterator().next();

                        int FRL_LN = (glForexLedger != null && glForexLedger.getFrlDate().compareTo(DATE) == 0) ? glForexLedger.getFrlLine().intValue() + 1 : 1;

                        // compute balance
                        double COA_FRX_BLNC = glForexLedger == null ? 0 : glForexLedger.getFrlBalance();
                        double FRL_AMNT = apDistributionRecord.getDrAmount();

                        if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET"))
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                        else FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);

                        COA_FRX_BLNC = COA_FRX_BLNC + FRL_AMNT;

                        double FRX_GN_LSS = 0d;

                        if (glOffsetJournalLine != null && firstFlag) {

                            if (glOffsetJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET"))
                                FRX_GN_LSS = (glOffsetJournalLine.getJlDebit() == EJBCommon.TRUE ? glOffsetJournalLine.getJlAmount() : (-1 * glOffsetJournalLine.getJlAmount()));
                            else
                                FRX_GN_LSS = (glOffsetJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * glOffsetJournalLine.getJlAmount()) : glOffsetJournalLine.getJlAmount());

                            firstFlag = false;
                        }

                        glForexLedger = glForexLedgerHome.create(DATE, new Integer(FRL_LN), "CHK", FRL_AMNT, conversionRate, COA_FRX_BLNC, FRX_GN_LSS, companyCode);
                        glForexLedger.setGlChartOfAccount(glJournalLine.getGlChartOfAccount());

                        // propagate balances
                        try {

                            glForexLedgers = glForexLedgerHome.findByGreaterThanFrlDateAndCoaCode(glForexLedger.getFrlDate(), glForexLedger.getGlChartOfAccount().getCoaCode(), glForexLedger.getFrlAdCompany());

                        } catch (FinderException ex) {

                        }

                        Iterator itrFrl = glForexLedgers.iterator();

                        while (itrFrl.hasNext()) {

                            glForexLedger = (LocalGlForexLedger) itrFrl.next();
                            FRL_AMNT = apDistributionRecord.getDrAmount();

                            if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET"))
                                FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                            else FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);

                            glForexLedger.setFrlBalance(glForexLedger.getFrlBalance() + FRL_AMNT);
                        }
                    }
                }

                if (glOffsetJournalLine != null) {

                    glOffsetJournalLine.setGlJournal(glJournal);
                }

                // post journal to gl

                Collection glJournalLines = glJournalLineHome.findByJrCode(glJournal.getJrCode(), companyCode);
                Iterator i = glJournalLines.iterator();

                while (i.hasNext()) {

                    LocalGlJournalLine glJournalLine = (LocalGlJournalLine) i.next();

                    // post current to current acv

                    this.postToGl(glAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), true, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                    // post to subsequent acvs (propagate)

                    Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), glAccountingCalendarValue.getAcvPeriodNumber(), companyCode);

                    Iterator acvsIter = glSubsequentAccountingCalendarValues.iterator();

                    while (acvsIter.hasNext()) {

                        LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) acvsIter.next();

                        this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                    }

                    // post to subsequent years if necessary

                    Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode);

                    if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                        adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                        LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome.findByCoaAccountNumber(adCompany.getCmpRetainedEarnings(), companyCode);

                        Iterator sobIter = glSubsequentSetOfBooks.iterator();

                        while (sobIter.hasNext()) {

                            LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) sobIter.next();

                            String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                            // post to subsequent acvs of subsequent set of book(propagate)

                            Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                            Iterator acvIter = glAccountingCalendarValues.iterator();

                            while (acvIter.hasNext()) {

                                LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) acvIter.next();

                                if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY") || ACCOUNT_TYPE.equals("OWNERS EQUITY")) {

                                    this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                                } else { // revenue & expense

                                    this.postToGl(glSubsequentAccountingCalendarValue, glRetainedEarningsAccount, false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                                }
                            }

                            if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) break;
                        }
                    }
                }
            }

        } catch (GlJREffectiveDateNoPeriodExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (GlobalJournalNotBalanceException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (GlobalRecordAlreadyDeletedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (GlobalTransactionAlreadyPostedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (GlobalTransactionAlreadyVoidPostedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void post(Date CHK_DT, double CHK_AMNT, LocalApSupplier apSupplier, Integer companyCode) {

        Debug.print("ApApprovalControllerBean post");

        try {

            // find supplier balance before or equal voucher date

            Collection apSupplierBalances = apSupplierBalanceHome.findByBeforeOrEqualVouDateAndSplSupplierCode(CHK_DT, apSupplier.getSplSupplierCode(), companyCode);

            if (!apSupplierBalances.isEmpty()) {

                // get last voucher

                ArrayList apSupplierBalanceList = new ArrayList(apSupplierBalances);

                LocalApSupplierBalance apSupplierBalance = (LocalApSupplierBalance) apSupplierBalanceList.get(apSupplierBalanceList.size() - 1);

                if (apSupplierBalance.getSbDate().before(CHK_DT)) {

                    // create new balance

                    LocalApSupplierBalance apNewSupplierBalance = apSupplierBalanceHome.create(CHK_DT, apSupplierBalance.getSbBalance() + CHK_AMNT, companyCode);

                    // apSupplier.addApSupplierBalance(apNewSupplierBalance);
                    apNewSupplierBalance.setApSupplier(apSupplier);

                } else { // equals to voucher date

                    apSupplierBalance.setSbBalance(apSupplierBalance.getSbBalance() + CHK_AMNT);
                }

            } else {

                // create new balance

                LocalApSupplierBalance apNewSupplierBalance = apSupplierBalanceHome.create(CHK_DT, CHK_AMNT, companyCode);
                apNewSupplierBalance.setApSupplier(apSupplier);
            }

            // propagate to subsequent balances if necessary

            apSupplierBalances = apSupplierBalanceHome.findByAfterVouDateAndSplSupplierCode(CHK_DT, apSupplier.getSplSupplierCode(), companyCode);

            Iterator i = apSupplierBalances.iterator();

            while (i.hasNext()) {

                LocalApSupplierBalance apSupplierBalance = (LocalApSupplierBalance) i.next();

                apSupplierBalance.setSbBalance(apSupplierBalance.getSbBalance() + CHK_AMNT);
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double JL_AMNT, Integer companyCode) {

        Debug.print("ApApprovalControllerBean postToGl");

        try {

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

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToInvVou(LocalApVoucherLineItem apVoucherLineItem, Date CST_DT, double CST_QTY_RCVD, double CST_ITM_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, double CST_VRNC_VL, String username, Integer branchCode, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ApApprovalControllerBean postToInv");

        // Initialize EJB Home

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalInvItemLocation invItemLocation = apVoucherLineItem.getInvItemLocation();
            int CST_LN_NMBR = 0;

            CST_QTY_RCVD = EJBCommon.roundIt(CST_QTY_RCVD, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_ITM_CST = EJBCommon.roundIt(CST_ITM_CST, adCompany.getGlFunctionalCurrency().getFcPrecision());
            CST_RMNNG_QTY = EJBCommon.roundIt(CST_RMNNG_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_RMNNG_VL = EJBCommon.roundIt(CST_RMNNG_VL, adCompany.getGlFunctionalCurrency().getFcPrecision());

            try {

                // generate line number

                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            } catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            // void subsequent cost variance adjustments
            Collection invAdjustmentLines = invAdjustmentLineHome.findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(CST_DT, invItemLocation.getIlCode(), branchCode, companyCode);
            Iterator i = invAdjustmentLines.iterator();

            while (i.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();
                this.voidInvAdjustment(invAdjustmentLine.getInvAdjustment(), branchCode, companyCode);
            }

            String prevExpiryDates = "";
            String miscListPrpgt = "";
            double qtyPrpgt = 0;
            try {
                LocalInvCosting prevCst = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndRemainingQuantityNotEqualToZeroAndIlCode(CST_DT, invItemLocation.getIlCode(), branchCode, companyCode);

                prevExpiryDates = prevCst.getCstExpiryDate();
                qtyPrpgt = prevCst.getCstRemainingQuantity();

                if (prevExpiryDates == null) {
                    prevExpiryDates = "";
                }

            } catch (Exception ex) {

            }

            // create costing
            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, CST_QTY_RCVD, CST_ITM_CST, 0d, 0d, 0d, 0d, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, 0d, branchCode, companyCode);

            invCosting.setCstQuantity(CST_QTY_RCVD);
            invCosting.setCstCost(CST_ITM_CST);

            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setApVoucherLineItem(apVoucherLineItem);

            // Get Latest Expiry Dates

            if (prevExpiryDates != null && prevExpiryDates != "" && prevExpiryDates != "") {
                Debug.print("apPurchaseOrderLine.getPlMisc(): " + apVoucherLineItem.getVliMisc());
                if (apVoucherLineItem.getVliMisc() != null && apVoucherLineItem.getVliMisc() != "" && apVoucherLineItem.getVliMisc().length() != 0) {
                    int qty2Prpgt = Integer.parseInt(this.getQuantityExpiryDates(apVoucherLineItem.getVliMisc()));
                    String miscList2Prpgt = this.propagateExpiryDates(apVoucherLineItem.getVliMisc(), qty2Prpgt);
                    prevExpiryDates = prevExpiryDates.substring(1);
                    String propagateMiscPrpgt = miscList2Prpgt + prevExpiryDates;

                    invCosting.setCstExpiryDate(propagateMiscPrpgt);
                } else {

                    invCosting.setCstExpiryDate(prevExpiryDates);
                }

            } else {
                if (apVoucherLineItem.getVliMisc() != null && apVoucherLineItem.getVliMisc() != "" && apVoucherLineItem.getVliMisc().length() != 0) {
                    int initialQty = Integer.parseInt(this.getQuantityExpiryDates(apVoucherLineItem.getVliMisc()));
                    String initialPrpgt = this.propagateExpiryDates(apVoucherLineItem.getVliMisc(), initialQty);

                    invCosting.setCstExpiryDate(initialPrpgt);
                } else {
                    invCosting.setCstExpiryDate(prevExpiryDates);
                }
            }

            // if cost variance is not 0, generate cost variance for the transaction
            if (CST_VRNC_VL != 0) {

                if (apVoucherLineItem.getApVoucher() != null)
                    this.generateCostVariance(invCosting.getInvItemLocation(), CST_VRNC_VL, "APVOU" + apVoucherLineItem.getApVoucher().getVouDocumentNumber(), apVoucherLineItem.getApVoucher().getVouDescription(), apVoucherLineItem.getApVoucher().getVouDate(), username, branchCode, companyCode);
                else if (apVoucherLineItem.getApCheck() != null)
                    this.generateCostVariance(invCosting.getInvItemLocation(), CST_VRNC_VL, "APCHK" + apVoucherLineItem.getApCheck().getChkDocumentNumber(), apVoucherLineItem.getApCheck().getChkDescription(), apVoucherLineItem.getApCheck().getChkDate(), username, branchCode, companyCode);
            }

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);

            i = invCostings.iterator();

            String miscList = "";
            if (apVoucherLineItem.getVliMisc() != null && apVoucherLineItem.getVliMisc() != "" && apVoucherLineItem.getVliMisc().length() != 0) {
                double qty = Double.parseDouble(this.getQuantityExpiryDates(apVoucherLineItem.getVliMisc()));
                miscList = this.propagateExpiryDates(apVoucherLineItem.getVliMisc(), qty);
            }

            Debug.print("miscList Propagate:" + miscList);

            while (i.hasNext()) {

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) i.next();

                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_QTY_RCVD);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_ITM_CST);
                if (apVoucherLineItem.getVliMisc() != null && apVoucherLineItem.getVliMisc() != "" && apVoucherLineItem.getVliMisc().length() != 0) {
                    miscList = miscList.substring(1);
                    // String miscList2 =
                    // propagateMisc;//this.propagateExpiryDates(invCosting.getCstExpiryDate(),
                    // qty);
                    Debug.print("invPropagatedCosting.getCstExpiryDate() : " + invPropagatedCosting.getCstExpiryDate());
                    String propagateMisc = invPropagatedCosting.getCstExpiryDate() + miscList;
                    invPropagatedCosting.setCstExpiryDate(propagateMisc);
                } else {
                    invPropagatedCosting.setCstExpiryDate(prevExpiryDates);
                }
            }

            // regenerate cost variance
            this.regenerateCostVariance(invCostings, invCosting, branchCode, companyCode);

        } catch (AdPRFCoaGlVarianceAccountNotFoundException ex) {

            throw ex;

        } catch (Exception ex) {

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

    public String propagateExpiryDates(String misc, double qty) throws Exception {
        // ActionErrors errors = new ActionErrors();

        Debug.print("ApReceivingItemControllerBean getExpiryDates");

        String separator = "$";

        // Remove first $ character
        misc = misc.substring(1);

        // Counter
        int start = 0;
        int nextIndex = misc.indexOf(separator, start);
        int length = nextIndex - start;

        Debug.print("qty" + qty);
        String miscList = "";

        for (int x = 0; x < qty; x++) {

            // Date
            start = nextIndex + 1;
            nextIndex = misc.indexOf(separator, start);
            length = nextIndex - start;

            try {

                miscList = miscList + "$" + (misc.substring(start, start + length));
            } catch (Exception ex) {

                throw ex;
            }
        }

        miscList = miscList + "$";
        Debug.print("miscList :" + miscList);
        return (miscList);
    }

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, double QTY_RCVD, Integer companyCode) {

        Debug.print("ApApprovalControllerBean convertByUomFromAndItemAndQuantity");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), companyCode);

            return EJBCommon.roundIt(QTY_RCVD * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), adPreference.getPrfInvQuantityPrecisionUnit());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double getFrRateByFrNameAndFrDate(String currencyName, Date conversionDate, Integer companyCode) throws GlobalConversionDateNotExistException {

        Debug.print("ApApprovalControllerBean getFrRateByFrNameAndFrDate");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(currencyName, companyCode);

            double conversionRate = 1;

            // Get functional currency rate

            if (!currencyName.equals("USD")) {

                LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glFunctionalCurrency.getFcCode(), conversionDate, companyCode);

                conversionRate = glFunctionalCurrencyRate.getFrXToUsd();
            }

            // Get set of book functional currency rate if necessary

            if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), conversionDate, companyCode);

                conversionRate = conversionRate / glCompanyFunctionalCurrencyRate.getFrXToUsd();
            }

            return conversionRate;

        } catch (FinderException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlobalConversionDateNotExistException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void voidInvAdjustment(LocalInvAdjustment invAdjustment, Integer branchCode, Integer companyCode) {

        Debug.print("ApApprovalControllerBean voidInvAdjustment");

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

                this.addInvDrEntry(invAdjustment.getInvDrNextLine(), invDistributionRecord.getDrClass(), invDistributionRecord.getDrDebit() == EJBCommon.TRUE ? EJBCommon.FALSE : EJBCommon.TRUE, invDistributionRecord.getDrAmount(), EJBCommon.TRUE, invDistributionRecord.getInvChartOfAccount().getCoaCode(), invAdjustment, branchCode, companyCode);
            }

            Collection invAjustmentLines = invAdjustment.getInvAdjustmentLines();
            i = invAjustmentLines.iterator();
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

            this.executeInvAdjPost(invAdjustment.getAdjCode(), invAdjustment.getAdjLastModifiedBy(), branchCode, companyCode);

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void generateCostVariance(LocalInvItemLocation invItemLocation, double CST_VRNC_VL, String ADJ_RFRNC_NMBR, String ADJ_DSCRPTN, Date ADJ_DT, String username, Integer branchCode, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

    }

    private void regenerateCostVariance(Collection invCostings, LocalInvCosting invCosting, Integer branchCode, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

    }

    private void addInvDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, byte DR_RVRSL, Integer COA_CODE, LocalInvAdjustment invAdjustment, Integer branchCode, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ApApprovalControllerBean addInvDrEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate coa

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, branchCode, companyCode);

            } catch (FinderException ex) {

                throw new GlobalBranchAccountNumberInvalidException();
            }

            // create distribution record

            LocalInvDistributionRecord invDistributionRecord = invDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), DR_RVRSL, EJBCommon.FALSE, companyCode);
            invDistributionRecord.setInvAdjustment(invAdjustment);
            invDistributionRecord.setInvChartOfAccount(glChartOfAccount);

        } catch (GlobalBranchAccountNumberInvalidException ex) {

            throw new GlobalBranchAccountNumberInvalidException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void executeInvAdjPost(Integer ADJ_CODE, String username, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException {

        Debug.print("ApApprovalControllerBean executeInvAdjPost");

        try {

            // validate if adjustment is already deleted

            LocalInvAdjustment invAdjustment = null;

            try {

                invAdjustment = invAdjustmentHome.findByPrimaryKey(ADJ_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if adjustment is already posted or void

            if (invAdjustment.getAdjPosted() == EJBCommon.TRUE) {

                if (invAdjustment.getAdjVoid() != EJBCommon.TRUE) throw new GlobalTransactionAlreadyPostedException();
            }

            Collection invAdjustmentLines = null;

            if (invAdjustment.getAdjVoid() == EJBCommon.FALSE)
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.FALSE, invAdjustment.getAdjCode(), companyCode);
            else
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.TRUE, invAdjustment.getAdjCode(), companyCode);

            Iterator i = invAdjustmentLines.iterator();

            while (i.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();

                LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(invAdjustment.getAdjDate(), invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(), invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName(), branchCode, companyCode);

                this.postInvAdjustmentToInventory(invAdjustmentLine, invAdjustment.getAdjDate(), 0, invAdjustmentLine.getAlUnitCost(), invCosting.getCstRemainingQuantity(), invCosting.getCstRemainingValue() + invAdjustmentLine.getAlUnitCost(), branchCode, companyCode);
            }

            // post to gl if necessary

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate if date has no period and period is closed

            LocalGlSetOfBook glJournalSetOfBook = null;

            try {

                glJournalSetOfBook = glSetOfBookHome.findByDate(invAdjustment.getAdjDate(), companyCode);

            } catch (FinderException ex) {

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

                } catch (FinderException ex) {

                    throw new GlobalJournalNotBalanceException();
                }

                if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                    glOffsetJournalLine = glJournalLineHome.create((short) (invDistributionRecords.size() + 1), EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);

                } else {

                    glOffsetJournalLine = glJournalLineHome.create((short) (invDistributionRecords.size() + 1), EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
                }

                LocalGlChartOfAccount glChartOfAccount = glSuspenseAccount.getGlChartOfAccount();
                glOffsetJournalLine.setGlChartOfAccount(glChartOfAccount);

            } else if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE && TOTAL_DEBIT != TOTAL_CREDIT) {

                throw new GlobalJournalNotBalanceException();
            }

            // create journal batch if necessary

            LocalGlJournalBatch glJournalBatch = null;
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");

            try {

                glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " INVENTORY ADJUSTMENTS", branchCode, companyCode);

            } catch (FinderException ex) {

            }

            if (glJournalBatch == null) {

                glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " INVENTORY ADJUSTMENTS", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), username, branchCode, companyCode);
            }

            // create journal entry

            LocalGlJournal glJournal = glJournalHome.create(invAdjustment.getAdjReferenceNumber(), invAdjustment.getAdjDescription(), invAdjustment.getAdjDate(), 0.0d, null, invAdjustment.getAdjDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, username, new Date(), username, new Date(), null, null, username, EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, EJBCommon.FALSE, null, branchCode, companyCode);

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

                invDistributionRecord.getInvChartOfAccount().addGlJournalLine(glJournalLine);

                // glJournal.addGlJournalLine(glJournalLine);
                glJournalLine.setGlJournal(glJournal);

                invDistributionRecord.setDrImported(EJBCommon.TRUE);
            }

            if (glOffsetJournalLine != null) {

                glOffsetJournalLine.setGlJournal(glJournal);
            }

            // post journal to gl

            Collection glJournalLines = glJournalLineHome.findByJrCode(glJournal.getJrCode(), companyCode);
            i = glJournalLines.iterator();

            while (i.hasNext()) {

                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) i.next();

                // post current to current acv

                this.postToGl(glAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), true, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                // post to subsequent acvs (propagate)

                Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), glAccountingCalendarValue.getAcvPeriodNumber(), companyCode);

                Iterator acvsIter = glSubsequentAccountingCalendarValues.iterator();

                while (acvsIter.hasNext()) {

                    LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) acvsIter.next();

                    this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                }

                // post to subsequent years if necessary

                Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode);

                if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                    adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                    LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(adCompany.getCmpRetainedEarnings(), branchCode, companyCode);

                    Iterator sobIter = glSubsequentSetOfBooks.iterator();

                    while (sobIter.hasNext()) {

                        LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) sobIter.next();

                        String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                        // post to subsequent acvs of subsequent set of book(propagate)

                        Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                        Iterator acvIter = glAccountingCalendarValues.iterator();

                        while (acvIter.hasNext()) {

                            LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) acvIter.next();

                            if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY") || ACCOUNT_TYPE.equals("OWNERS EQUITY")) {

                                this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                            } else { // revenue & expense

                                this.postToGl(glSubsequentAccountingCalendarValue, glRetainedEarningsAccount, false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                            }
                        }

                        if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) break;
                    }
                }
            }

            invAdjustment.setAdjPosted(EJBCommon.TRUE);

        } catch (GlJREffectiveDateNoPeriodExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (GlobalJournalNotBalanceException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (GlobalRecordAlreadyDeletedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (GlobalTransactionAlreadyPostedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalInvAdjustmentLine addInvAlEntry(LocalInvItemLocation invItemLocation, LocalInvAdjustment invAdjustment, double CST_VRNC_VL, byte AL_VD, Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean addInvAlEntry");


        try {

            // create dr entry
            LocalInvAdjustmentLine invAdjustmentLine = null;
            invAdjustmentLine = invAdjustmentLineHome.create(CST_VRNC_VL, null, null, 0, 0, AL_VD, companyCode);

            // map adjustment, unit of measure, item location

            invAdjustmentLine.setInvAdjustment(invAdjustment);
            invAdjustmentLine.setInvUnitOfMeasure(invItemLocation.getInvItem().getInvUnitOfMeasure());
            invAdjustmentLine.setInvItemLocation(invItemLocation);

            return invAdjustmentLine;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postInvAdjustmentToInventory(LocalInvAdjustmentLine invAdjustmentLine, Date CST_DT, double CST_ADJST_QTY, double CST_ADJST_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, Integer branchCode, Integer companyCode) {

        Debug.print("ApApprovalControllerBean postInvAdjustmentToInventory");

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

                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            } catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d, CST_ADJST_QTY, CST_ADJST_CST, 0d, 0d, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, CST_ADJST_QTY > 0 ? CST_ADJST_QTY : 0, branchCode, companyCode);
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setInvAdjustmentLine(invAdjustmentLine);

            invCosting.setCstQuantity(CST_ADJST_QTY);
            invCosting.setCstCost(CST_ADJST_CST);

            // propagate balance if necessary

            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);

            Iterator i = invCostings.iterator();

            while (i.hasNext()) {

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) i.next();

                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_ADJST_QTY);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_ADJST_CST);
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, double QTY_RCVD, double conversionFactor, Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean convertByUomFromAndUomToAndQuantity");


        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), companyCode);

            return EJBCommon.roundIt(QTY_RCVD * conversionFactor, adPreference.getPrfInvQuantityPrecisionUnit());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToInvPo(LocalApPurchaseOrderLine apPurchaseOrderLine, Date CST_DT, double CST_QTY_RCVD, double CST_ITM_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, double CST_VRNC_VL, String username, Integer branchCode, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ApReceivingItemEntryControllerBean postToInv");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalInvItemLocation invItemLocation = apPurchaseOrderLine.getInvItemLocation();
            int CST_LN_NMBR = 0;

            CST_QTY_RCVD = EJBCommon.roundIt(CST_QTY_RCVD, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_ITM_CST = EJBCommon.roundIt(CST_ITM_CST, adPreference.getPrfInvCostPrecisionUnit());
            CST_RMNNG_QTY = EJBCommon.roundIt(CST_RMNNG_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_RMNNG_VL = EJBCommon.roundIt(CST_RMNNG_VL, adPreference.getPrfInvCostPrecisionUnit());

            try {

                // generate line number
                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            } catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            // void subsequent cost variance adjustments
            Collection invAdjustmentLines = invAdjustmentLineHome.findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(CST_DT, invItemLocation.getIlCode(), branchCode, companyCode);
            Iterator i = invAdjustmentLines.iterator();

            while (i.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();
                this.voidInvAdjustment(invAdjustmentLine.getInvAdjustment(), branchCode, companyCode);
            }

            // computation

            // CST_ITM_CST = amount in pesos

            String prevExpiryDates = "";
            String miscListPrpgt = "";
            double qtyPrpgt = 0;
            try {
                LocalInvCosting prevCst = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndRemainingQuantityNotEqualToZeroAndIlCode(CST_DT, invItemLocation.getIlCode(), branchCode, companyCode);

                prevExpiryDates = prevCst.getCstExpiryDate();
                qtyPrpgt = prevCst.getCstRemainingQuantity();
                if (prevExpiryDates == null) {
                    prevExpiryDates = "";
                }

            } catch (Exception ex) {
                prevExpiryDates = "";
            }
            // create costing
            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, CST_QTY_RCVD, CST_ITM_CST, 0d, 0d, 0d, 0d, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, CST_QTY_RCVD > 0 ? CST_QTY_RCVD : 0, branchCode, companyCode);
            invCosting.setCstQCNumber(apPurchaseOrderLine.getPlQcNumber());
            invCosting.setCstExpiryDate(apPurchaseOrderLine.getPlQcExpiryDate().toString());

            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setApPurchaseOrderLine(apPurchaseOrderLine);

            invCosting.setCstQuantity(CST_QTY_RCVD);
            invCosting.setCstCost(CST_ITM_CST);
            // Get Latest Expiry Dates

            if (prevExpiryDates != null && prevExpiryDates != "" && prevExpiryDates.length() != 0) {

                if (apPurchaseOrderLine.getPlMisc() != null && apPurchaseOrderLine.getPlMisc() != "" && apPurchaseOrderLine.getPlMisc().length() != 0) {

                    int qty2Prpgt = Integer.parseInt(this.getQuantityExpiryDates(apPurchaseOrderLine.getPlMisc()));
                    String miscList2Prpgt = this.propagateExpiryDates(apPurchaseOrderLine.getPlMisc(), qty2Prpgt);
                    prevExpiryDates = prevExpiryDates.substring(1);
                    String propagateMiscPrpgt = miscList2Prpgt + prevExpiryDates;

                    invCosting.setCstExpiryDate(propagateMiscPrpgt);
                } else {
                    invCosting.setCstExpiryDate(prevExpiryDates);
                }
            } else {
                if (apPurchaseOrderLine.getPlMisc() != null && apPurchaseOrderLine.getPlMisc() != "" && apPurchaseOrderLine.getPlMisc().length() != 0) {
                    int initialQty = Integer.parseInt(this.getQuantityExpiryDates(apPurchaseOrderLine.getPlMisc()));
                    String initialPrpgt = this.propagateExpiryDates(apPurchaseOrderLine.getPlMisc(), initialQty);

                    invCosting.setCstExpiryDate(initialPrpgt);
                } else {
                    invCosting.setCstExpiryDate(prevExpiryDates);
                }
            }

            // if cost variance is not 0, generate cost variance for the transaction
            if (CST_VRNC_VL != 0) {

                this.generateCostVariance(invCosting.getInvItemLocation(), CST_VRNC_VL, "APRI" + apPurchaseOrderLine.getApPurchaseOrder().getPoDocumentNumber(), apPurchaseOrderLine.getApPurchaseOrder().getPoDescription(), apPurchaseOrderLine.getApPurchaseOrder().getPoDate(), username, branchCode, companyCode);
            }

            // propagate balance if necessary

            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
            String miscList = "";
            i = invCostings.iterator();
            if (apPurchaseOrderLine.getPlMisc() != null && apPurchaseOrderLine.getPlMisc() != "" && apPurchaseOrderLine.getPlMisc().length() != 0) {
                double qty = Double.parseDouble(this.getQuantityExpiryDates(apPurchaseOrderLine.getPlMisc()));
                miscList = this.propagateExpiryDates(apPurchaseOrderLine.getPlMisc(), qty);
            }
            // miscList = miscList.substring(1);
            while (i.hasNext()) {

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) i.next();

                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_QTY_RCVD);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_ITM_CST);
                // String miscList2 =
                // propagateMisc;//this.propagateExpiryDates(invCosting.getCstExpiryDate(),
                // qty);

            }

            // regenerate cost variance
            this.regenerateCostVariance(invCostings, invCosting, branchCode, companyCode);

        } catch (AdPRFCoaGlVarianceAccountNotFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ApApprovalControllerBean ejbCreate");
    }

}