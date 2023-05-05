/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArApprovalControllerBean
 * @created March 24, 2004, 8:37 PM
 * @author Dennis M. Hilario
 */
package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ar.*;
import com.ejb.dao.cm.LocalCmAdjustmentHome;
import com.ejb.dao.cm.LocalCmDistributionRecordHome;
import com.ejb.dao.gl.*;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ar.*;
import com.ejb.entities.cm.LocalCmAdjustment;
import com.ejb.entities.cm.LocalCmDistributionRecord;
import com.ejb.entities.gl.*;
import com.ejb.entities.inv.*;
import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.cm.CmAdjustmentDetails;
import com.util.mod.ad.AdModApprovalQueueDetails;

import jakarta.ejb.*;
import java.util.*;

@Stateless(name = "ArApprovalControllerEJB")
public class ArApprovalControllerBean extends EJBContextClass implements ArApprovalController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalArSalesOrderHome arSalesOrderHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalAdAmountLimitHome adAmountLimitHome;
    @EJB
    private LocalAdApprovalUserHome adApprovalUserHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private ILocalGlSetOfBookHome glSetOfBookHome;
    @EJB
    private ILocalGlAccountingCalendarValueHome glAccountingCalendarValueHome;
    @EJB
    private ILocalGlChartOfAccountBalanceHome glChartOfAccountBalanceHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalCmAdjustmentHome cmAdjustmentHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalCmDistributionRecordHome cmDistributionRecordHome;
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
    private LocalAdBankAccountBalanceHome adBankAccountBalanceHome;
    @EJB
    private LocalGlForexLedgerHome glForexLedgerHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalArCustomerBalanceHome arCustomerBalanceHome;
    @EJB
    private LocalInvAdjustmentLineHome invAdjustmentLineHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private LocalInvDistributionRecordHome invDistributionRecordHome;
    @EJB
    private LocalInvAdjustmentHome invAdjustmentHome;

    public String getApprovalStatus(String userDepartment, String username, String userDesc, String approvalQueueDocument, Integer approvalQueueDocumentCode, String approvalQueueDocumentNumber, Date approvalQueueDate, Integer branchCode, Integer companyCode) throws GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException {

        Debug.print("ArApprovalControllerBean getApprovalStatus");
        String prApprovalStatus;
        Double absTotalAmount = 0d;
        String approvalUserType = "REQUESTER";
        try {
            LocalAdAmountLimit adAmountLimit;
            try {
                adAmountLimit = adAmountLimitHome.findAmountLimitPerApprovalUser(userDepartment, approvalQueueDocument, approvalUserType, username, companyCode);
            } catch (FinderException ex) {
                throw new GlobalNoApprovalRequesterFoundException();
            }
            if (!(absTotalAmount <= adAmountLimit.getCalAmountLimit())) {
                prApprovalStatus = "N/A";
            } else {
                // for approval, create approval queue
                Collection adAmountLimits = adAmountLimitHome.findAmountLimitsPerDepartment(userDepartment, approvalQueueDocument, adAmountLimit.getCalAmountLimit(), companyCode);
                if (adAmountLimits.isEmpty()) {
                    Collection adApprovalUsers = adApprovalUserHome.findApprovalUsersPerDepartment(userDepartment, "APPROVER", adAmountLimit.getCalCode(), companyCode);
                    if (adApprovalUsers.isEmpty()) {
                        throw new GlobalNoApprovalApproverFoundException();
                    }

                    Iterator j = adApprovalUsers.iterator();
                    while (j.hasNext()) {
                        LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) j.next();

                        LocalAdApprovalQueue adApprovalQueue = adApprovalQueueHome.AqDepartment(userDepartment).AqLevel(adApprovalUser.getAuLevel()).AqNextLevel(EJBCommon.incrementStringNumber(adApprovalUser.getAuLevel())).AqDocument(approvalQueueDocument).AqDocumentCode(approvalQueueDocumentCode).AqDocumentNumber(approvalQueueDocumentNumber).AqDate(approvalQueueDate).AqAndOr(adAmountLimit.getCalAndOr()).AqUserOr(adApprovalUser.getAuOr()).AqRequesterName(userDesc).AqAdBranch(branchCode).AqAdCompany(companyCode).buildApprovalQueue();

                        adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);

                        if (adApprovalUser.getAuLevel().equals("LEVEL 1")) {
                            adApprovalQueue.setAqForApproval(EJBCommon.TRUE);
                            //TODO: Add an email notification mechanism based on the Preference Setup getPrfAdEnableEmailNotification with Approval Queue User Email Address
                        }
                    }

                } else {
                    boolean isApprovalUsersFound = false;
                    Iterator i = adAmountLimits.iterator();
                    while (i.hasNext()) {
                        LocalAdAmountLimit adNextAmountLimit = (LocalAdAmountLimit) i.next();
                        if (absTotalAmount <= adNextAmountLimit.getCalAmountLimit()) {
                            Collection adApprovalUsers = adApprovalUserHome.findApprovalUsersPerDepartment(userDepartment, "APPROVER", adAmountLimit.getCalCode(), companyCode);

                            Iterator j = adApprovalUsers.iterator();
                            while (j.hasNext()) {

                                isApprovalUsersFound = true;

                                LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) j.next();

                                LocalAdApprovalQueue adApprovalQueue = adApprovalQueueHome.AqDepartment(userDepartment).AqLevel(adApprovalUser.getAuLevel()).AqNextLevel(EJBCommon.incrementStringNumber(adApprovalUser.getAuLevel())).AqDocument(approvalQueueDocument).AqDocumentCode(approvalQueueDocumentCode).AqDocumentNumber(approvalQueueDocumentNumber).AqDate(approvalQueueDate).AqAndOr(adAmountLimit.getCalAndOr()).AqUserOr(adApprovalUser.getAuOr()).AqRequesterName(userDesc).AqAdBranch(branchCode).AqAdCompany(companyCode).buildApprovalQueue();

                                if (adApprovalUser.getAuLevel().equals("LEVEL 1")) {
                                    adApprovalQueue.setAqForApproval(EJBCommon.TRUE);
                                    //TODO: Add an email notification mechanism based on the Preference Setup getPrfAdEnableEmailNotification with Approval Queue User Email Address
                                }
                            }
                            break;

                        } else if (!i.hasNext()) {
                            Collection adApprovalUsers = adApprovalUserHome.findApprovalUsersPerDepartment(userDepartment, "APPROVER", adNextAmountLimit.getCalCode(), companyCode);
                            Iterator j = adApprovalUsers.iterator();
                            while (j.hasNext()) {

                                isApprovalUsersFound = true;

                                LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) j.next();

                                LocalAdApprovalQueue adApprovalQueue = adApprovalQueueHome.AqDepartment(userDepartment).AqLevel(adApprovalUser.getAuLevel()).AqNextLevel(EJBCommon.incrementStringNumber(adApprovalUser.getAuLevel())).AqDocument(approvalQueueDocument).AqDocumentCode(approvalQueueDocumentCode).AqDocumentNumber(approvalQueueDocumentNumber).AqDate(approvalQueueDate).AqAndOr(adAmountLimit.getCalAndOr()).AqUserOr(adApprovalUser.getAuOr()).AqRequesterName(userDesc).AqAdBranch(branchCode).AqAdCompany(companyCode).buildApprovalQueue();

                                adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);

                                if (adApprovalUser.getAuLevel().equals("LEVEL 1")) {
                                    adApprovalQueue.setAqForApproval(EJBCommon.TRUE);
                                    //TODO: Add an email notification mechanism based on the Preference Setup getPrfAdEnableEmailNotification with Approval Queue User Email Address
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

    public ArrayList getArApproverList(String approvalQueueDocument, Integer approvalQueueDocumentCode, Integer companyCode) {

        Debug.print("ArApprovalControllerBean getApApproverList");

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

    public ArrayList getAdApprovalNotifiedUsersByAqCode(Integer approvalQueueCode, Integer companyCode) {

        Debug.print("ArApprovalControllerBean getAdApprovalNotifiedUsersByAqCode");

        ArrayList list = new ArrayList();

        // Initialize EJB Home

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdApprovalQueue adApprovalQueue = null;
            try {
                Debug.print("approval queue code is: " + approvalQueueCode);
                adApprovalQueue = adApprovalQueueHome.findByPrimaryKey(approvalQueueCode);

                if (adApprovalQueue.getAqApproved() == EJBCommon.TRUE) {
                    list.add("DOCUMENT POSTED");
                }

            } catch (Exception e) {

                Debug.print("rejected");
                list.add("DOCUMENT REJECTED");
                return list;
            }

            /*
             * LocalApPurchaseRequisition apPurchaseRequisition =
             * apPurchaseRequisitionHome.findByPrimaryKey(adApprovalQueue.getAqDocumentCode(
             * )); if (apPurchaseRequisition.getPrPosted() == EJBCommon.TRUE) {
             *
             * Debug.print("posted"); list.add("DOCUMENT POSTED"); return list;
             *
             * }
             */

            if (adApprovalQueue.getAqDocument().equals("AR INVOICE")) {

                LocalArInvoice arInvoice = arInvoiceHome.findByPrimaryKey(adApprovalQueue.getAqDocumentCode());
                if (arInvoice.getInvPosted() == EJBCommon.TRUE) {

                    list.add("DOCUMENT POSTED");
                    return list;
                }
            }

            if (adApprovalQueue.getAqDocument().equals("AR SALES ORDER")) {

                LocalArSalesOrder arSalesOrder = arSalesOrderHome.findByPrimaryKey(adApprovalQueue.getAqDocumentCode());
                if (arSalesOrder.getSoPosted() == EJBCommon.TRUE) {

                    list.add("DOCUMENT POSTED");
                    return list;
                }
            }

            if (adApprovalQueue.getAqDocument().equals("AR RECEIPT")) {

                LocalArReceipt arReceipt = arReceiptHome.findByPrimaryKey(adApprovalQueue.getAqDocumentCode());
                if (arReceipt.getRctPosted() == EJBCommon.TRUE) {

                    list.add("DOCUMENT POSTED");
                    return list;
                }
            }

            String messageUser = "";
            LocalAdApprovalQueue adNextApprovalQueue = adApprovalQueueHome.findByAqDeptAndAqLevelAndAqDocumentCode(adApprovalQueue.getAqDepartment(), adApprovalQueue.getAqNextLevel(), adApprovalQueue.getAqDocumentCode(), companyCode);

            messageUser = adNextApprovalQueue.getAqLevel() + " APPROVER - " + adNextApprovalQueue.getAdUser().getUsrDescription();
            Debug.print("adNextApprovalQueue.getAdUser().getUsrDescription()=" + adNextApprovalQueue.getAdUser().getUsrDescription());

            try {

                adNextApprovalQueue.setAqForApproval(EJBCommon.TRUE);

            } catch (Exception e) {
                messageUser += " [Email Notification Not Sent. Cannot connect host or no internet connection.]";
            }

            list.add(messageUser);
            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdAqByAqDocumentAndUserName(HashMap criteria, String username, Integer offset, Integer limit, String orderBy, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArApprovalControllerBean getAdAqByAqDocumentAndUserName");

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

            // jbossQl.append("aq.aqAdBranch=" + branchCode + " AND aq.aqAdCompany=" +
            // companyCode + " " + "AND
            // aq.adUser.usrName='" + username + "'");
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

            Collection adApprovalQueues = adApprovalQueueHome.getAqByCriteria(jbossQl.toString(), obj, limit, offset);

            String approvalQueueDocument = (String) criteria.get("document");

            if (adApprovalQueues.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            Iterator i = adApprovalQueues.iterator();

            while (i.hasNext()) {

                LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) i.next();

                AdModApprovalQueueDetails details = new AdModApprovalQueueDetails();

                details.setAqCode(adApprovalQueue.getAqCode());
                details.setAqDocument(adApprovalQueue.getAqDocument());
                details.setAqDocumentCode(adApprovalQueue.getAqDocumentCode());

                if (approvalQueueDocument.equals("AR INVOICE") || approvalQueueDocument.equals("AR CREDIT MEMO")) {

                    LocalArInvoice arInvoice = arInvoiceHome.findByPrimaryKey(adApprovalQueue.getAqDocumentCode());

                    details.setAqDate(arInvoice.getInvDate());
                    details.setAqDocumentNumber(arInvoice.getInvNumber());
                    details.setAqAmount(arInvoice.getInvAmountDue());
                    details.setAqCustomerCode(arInvoice.getArCustomer().getCstCustomerCode());
                    details.setAqCustomerName(arInvoice.getArCustomer().getCstName());

                    if (!arInvoice.getArInvoiceLineItems().isEmpty()) {

                        details.setAqType("ITEMS");

                    } else {

                        details.setAqType("MEMO LINES");
                    }

                } else if (approvalQueueDocument.equals("AR RECEIPT")) {

                    LocalArReceipt arReceipt = arReceiptHome.findByPrimaryKey(adApprovalQueue.getAqDocumentCode());

                    details.setAqDate(arReceipt.getRctDate());
                    details.setAqDocumentNumber(arReceipt.getRctNumber());
                    details.setAqAmount(arReceipt.getRctAmount());
                    details.setAqCustomerCode(arReceipt.getArCustomer().getCstCustomerCode());
                    details.setAqCustomerName(arReceipt.getArCustomer().getCstName());
                    details.setAqDocumentType(arReceipt.getRctType());

                    if (!arReceipt.getArInvoiceLineItems().isEmpty()) {

                        details.setAqType("ITEMS");

                    } else {

                        details.setAqType("MEMO LINES");
                    }

                } else if (approvalQueueDocument.equals("AR CUSTOMER")) {

                    LocalArCustomer arCustomer = arCustomerHome.findByPrimaryKey(adApprovalQueue.getAqDocumentCode());

                    details.setAqDate(arCustomer.getCstDateLastModified());
                    details.setAqDocumentNumber(arCustomer.getCstName());

                    // details.setAqAmount(0d);
                    details.setAqCustomerCode(arCustomer.getCstCustomerCode());
                    // details.setAqDocumentType(arCustomer.getCstCustomerBatch());

                    details.setAqReferenceNumber(null);
                    details.setAqCustomerName(arCustomer.getCstCustomerCode());

                } else {

                    LocalArSalesOrder arSalesOrder = arSalesOrderHome.findByPrimaryKey(adApprovalQueue.getAqDocumentCode());
                    Debug.print(" here it is: " + adApprovalQueue.getAqDocumentCode());
                    details.setAqDate(arSalesOrder.getSoDate());
                    details.setAqDocumentNumber(arSalesOrder.getSoDocumentNumber());
                    details.setAqCustomerCode(arSalesOrder.getArCustomer().getCstCustomerCode());
                    details.setAqCustomerName(arSalesOrder.getArCustomer().getCstName());
                    details.setAqType("ITEMS");

                    Collection arSalesOrderLines = arSalesOrder.getArSalesOrderLines();
                    Iterator j = arSalesOrderLines.iterator();

                    double SO_TTL_AMOUNT = 0;

                    while (j.hasNext()) {

                        LocalArSalesOrderLine arSalesOrderLine = (LocalArSalesOrderLine) j.next();

                        SO_TTL_AMOUNT = SO_TTL_AMOUNT + arSalesOrderLine.getSolAmount();
                    }

                    details.setAqAmount(SO_TTL_AMOUNT);
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

    public void executeArApproval(String approvalQueueDocument, Integer approvalQueueDocumentCode, String username, boolean isApproved, String memo, String reasonForRejection, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalTransactionAlreadyVoidPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ArApprovalControllerBean executeArApproval");
        LocalAdApprovalQueue adApprovalQueue;
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

            if (approvalQueueDocument.equals("AR INVOICE") || approvalQueueDocument.equals("AR CREDIT MEMO")) {

                LocalArInvoice arInvoice = arInvoiceHome.findByPrimaryKey(approvalQueueDocumentCode);

                Collection arInvoiceLineItems = arInvoice.getArInvoiceLineItems();

                Iterator i = arInvoiceLineItems.iterator();

                while (i.hasNext()) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) i.next();

                    // start date validation
                    System.out.println("adPreference.getPrfArAllowPriorDate(): " + adPreference.getPrfArAllowPriorDate());
                    Debug.print("EJBCommon.FALSE: " + EJBCommon.FALSE);
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {

                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(arInvoice.getInvDate(), arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName(), arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName(), branchCode, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                        }
                    }
                }

                if (isApproved) {

                    if (adApprovalQueue.getAqAndOr().equals("AND")) {

                        if (adApprovalQueues.size() == 1) {

                            arInvoice.setInvApprovalStatus("APPROVED");
                            arInvoice.setInvApprovedRejectedBy(username);
                            arInvoice.setInvDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                            if (adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                                this.executeArInvPost(arInvoice.getInvCode(), username, branchCode, companyCode);
                            }

                            adApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adApprovalQueue.setAqApprovedDate(new java.util.Date());

                        } else {

                            // looping up
                            i = adApprovalQueuesDesc.iterator();

                            while (i.hasNext()) {

                                LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                                if (adRemoveApprovalQueue.getAqUserOr() == (byte) 1) {

                                    i.remove();
                                    // adRemoveApprovalQueue.entityRemove();

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
                                        // adRemoveApprovalQueue.entityRemove();
                                        adRemoveApprovalQueue.setAqApproved(EJBCommon.TRUE);
                                        adRemoveApprovalQueue.setAqApprovedDate(new java.util.Date());
                                        // em.remove(adRemoveApprovalQueue);

                                        if (first) {
                                            first = false;
                                        }

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

                                arInvoice.setInvApprovalStatus("APPROVED");
                                arInvoice.setInvApprovedRejectedBy(username);
                                arInvoice.setInvDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                                if (adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                                    this.executeArInvPost(arInvoice.getInvCode(), username, branchCode, companyCode);
                                }
                            }
                        }

                    } else if (adApprovalQueue.getAqAndOr().equals("OR")) {

                        arInvoice.setInvApprovalStatus("APPROVED");
                        arInvoice.setInvApprovedRejectedBy(username);
                        arInvoice.setInvDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                        i = adApprovalQueues.iterator();

                        while (i.hasNext()) {

                            LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                            i.remove();
                            adRemoveApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adRemoveApprovalQueue.setAqApprovedDate(new java.util.Date());

                        }

                        if (adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                            this.executeArInvPost(arInvoice.getInvCode(), username, branchCode, companyCode);

                            // Set SO Lock if Fully Served

                            LocalArSalesOrder arExistingSalesOrder = null;

                            try {

                                if (arInvoice.getArSalesOrderInvoiceLines().size() > 0) {
                                    arExistingSalesOrder = ((LocalArSalesOrderLine) arInvoice.getArSalesOrderInvoiceLines().toArray()[0]).getArSalesOrder();
                                }

                                Iterator solIter = arExistingSalesOrder.getArSalesOrderLines().iterator();

                                boolean isOpenSO = false;

                                while (solIter.hasNext()) {

                                    LocalArSalesOrderLine arSalesOrderLine = (LocalArSalesOrderLine) solIter.next();

                                    Iterator soInvLnIter = arSalesOrderLine.getArSalesOrderInvoiceLines().iterator();
                                    double QUANTITY_SOLD = 0d;

                                    while (soInvLnIter.hasNext()) {

                                        LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) soInvLnIter.next();

                                        if (arSalesOrderInvoiceLine.getArInvoice().getInvPosted() == EJBCommon.TRUE) {

                                            QUANTITY_SOLD += arSalesOrderInvoiceLine.getSilQuantityDelivered();
                                        }
                                    }

                                    double TOTAL_REMAINING_QTY = arSalesOrderLine.getSolQuantity() - QUANTITY_SOLD;

                                    if (TOTAL_REMAINING_QTY > 0) {
                                        isOpenSO = true;
                                        break;
                                    }
                                }

                                if (isOpenSO) {
                                    arExistingSalesOrder.setSoLock(EJBCommon.FALSE);
                                } else {
                                    arExistingSalesOrder.setSoLock(EJBCommon.TRUE);
                                }

                            } catch (Exception ex) {
                                // Do Nothing
                            }
                        }
                    }

                } else if (!isApproved) {

                    arInvoice.setInvApprovalStatus(null);
                    arInvoice.setInvApprovedRejectedBy(username);
                    arInvoice.setInvReasonForRejection(reasonForRejection);
                    arInvoice.setInvDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                    i = adAllApprovalQueues.iterator();

                    while (i.hasNext()) {

                        LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                        i.remove();
                        // adRemoveApprovalQueue.entityRemove();
                        em.remove(adRemoveApprovalQueue);
                    }
                }

            } else if (approvalQueueDocument.equals("AR CUSTOMER")) {

                LocalArCustomer arCustomer = arCustomerHome.findByPrimaryKey(approvalQueueDocumentCode);

                if (isApproved) {
                    if (adApprovalQueue.getAqAndOr().equals("AND")) {
                        if (adApprovalQueues.size() == 1) {
                            arCustomer.setCstApprovalStatus("APPROVED");

                            LocalAdUser approver = null;
                            try {
                                approver = adUserHome.findByUsrName(username, companyCode);
                                approver.setUsrPurchaseOrderApprovalCounter(EJBCommon.incrementStringNumber(approver.getUsrPurchaseOrderApprovalCounter()));
                            } catch (Exception ex) {
                                Debug.printStackTrace(ex);
                            }

                            arCustomer.setCstApprovedRejectedBy(username);
                            arCustomer.setCstDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());
                            arCustomer.setCstPosted(EJBCommon.TRUE);
                            // arCustomer.setCstEnable(EJBCommon.TRUE);

                            arCustomer.setCstPostedBy(arCustomer.getCstLastModifiedBy());
                            arCustomer.setCstDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

                            adApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adApprovalQueue.setAqApprovedDate(new java.util.Date());

                        } else {
                            // looping up
                            Iterator i = adApprovalQueuesDesc.iterator();
                            while (i.hasNext()) {
                                LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();
                                if (adRemoveApprovalQueue.getAqUserOr() == (byte) 1) {
                                    i.remove();
                                    // adRemoveApprovalQueue.entityRemove();
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
                                        // adRemoveApprovalQueue.entityRemove();

                                        adRemoveApprovalQueue.setAqApproved(EJBCommon.TRUE);
                                        adRemoveApprovalQueue.setAqApprovedDate(new java.util.Date());
                                        // em.remove(adRemoveApprovalQueue);
                                        if (first) {
                                            first = false;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            }

                            // adApprovalQueue.entityRemove();
                            adApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adApprovalQueue.setAqApprovedDate(new java.util.Date());
                            em.remove(adApprovalQueue);
                            Collection adRemoveApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode(approvalQueueDocument, approvalQueueDocumentCode, companyCode);
                            if (adRemoveApprovalQueues.size() == 0) {
                                arCustomer.setCstApprovalStatus("APPROVED");

                                LocalAdUser approver = null;
                                try {
                                    approver = adUserHome.findByUsrName(username, companyCode);
                                    approver.setUsrPurchaseOrderApprovalCounter(EJBCommon.incrementStringNumber(approver.getUsrPurchaseOrderApprovalCounter()));
                                } catch (Exception ex) {
                                    Debug.printStackTrace(ex);
                                }

                                arCustomer.setCstApprovedRejectedBy(username);
                                arCustomer.setCstDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());
                                arCustomer.setCstPosted(EJBCommon.TRUE);
                                // arCustomer.setCstEnable(EJBCommon.TRUE);
                                arCustomer.setCstPostedBy(arCustomer.getCstLastModifiedBy());
                                arCustomer.setCstDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
                            }
                        }
                    } else if (adApprovalQueue.getAqAndOr().equals("OR")) {

                        arCustomer.setCstApprovalStatus("APPROVED");

                        LocalAdUser approver = null;
                        try {
                            approver = adUserHome.findByUsrName(username, companyCode);
                            approver.setUsrPurchaseOrderApprovalCounter(EJBCommon.incrementStringNumber(approver.getUsrPurchaseOrderApprovalCounter()));
                        } catch (Exception ex) {
                            Debug.printStackTrace(ex);
                        }

                        arCustomer.setCstApprovedRejectedBy(username);
                        arCustomer.setCstDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                        Iterator i = adApprovalQueues.iterator();

                        while (i.hasNext()) {
                            LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();
                            i.remove();
                            // adRemoveApprovalQueue.entityRemove();
                            adRemoveApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adRemoveApprovalQueue.setAqApprovedDate(new java.util.Date());
                            // em.remove(adRemoveApprovalQueue);

                        }
                        arCustomer.setCstPosted(EJBCommon.TRUE);
                        // arCustomer.setCstEnable(EJBCommon.TRUE);
                        arCustomer.setCstPostedBy(arCustomer.getCstLastModifiedBy());
                        arCustomer.setCstDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
                    }
                } else if (!isApproved) {
                    arCustomer.setCstApprovalStatus(null);
                    arCustomer.setCstReasonForRejection(reasonForRejection);
                    arCustomer.setCstApprovedRejectedBy(username);
                    arCustomer.setCstDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());
                    Iterator i = adAllApprovalQueues.iterator();

                    while (i.hasNext()) {
                        LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();
                        i.remove();
                        // adRemoveApprovalQueue.entityRemove();
                        em.remove(adRemoveApprovalQueue);
                    }
                }

            } else if (approvalQueueDocument.equals("AR RECEIPT")) {

                LocalArReceipt arReceipt = arReceiptHome.findByPrimaryKey(approvalQueueDocumentCode);

                Collection arInvoiceLineItems = arReceipt.getArInvoiceLineItems();

                Iterator i = arInvoiceLineItems.iterator();

                while (i.hasNext()) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) i.next();

                    // start date validation

                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(arReceipt.getRctDate(), arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName(), arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName(), branchCode, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                        }
                    }
                }

                if (isApproved) {

                    if (adApprovalQueue.getAqAndOr().equals("AND")) {

                        if (adApprovalQueues.size() == 1) {

                            if (arReceipt.getRctVoid() == EJBCommon.FALSE) {

                                arReceipt.setRctApprovalStatus("APPROVED");

                            } else {

                                arReceipt.setRctVoidApprovalStatus("APPROVED");
                            }

                            adApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adApprovalQueue.setAqApprovedDate(new java.util.Date());

                            arReceipt.setRctApprovedRejectedBy(username);
                            arReceipt.setRctDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                            if (adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                                // create cm adjustment for inserting advance payment
                                if (arReceipt.getRctEnableAdvancePayment() != 0) {

                                    this.postAdvncPymntByCmAdj(arReceipt, arReceipt.getAdBankAccount().getBaName(), branchCode, companyCode);
                                }

                                this.executeArRctPost(arReceipt.getRctCode(), username, branchCode, companyCode);
                            }

                        } else {

                            // looping up
                            i = adApprovalQueuesDesc.iterator();

                            while (i.hasNext()) {

                                LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                                if (adRemoveApprovalQueue.getAqUserOr() == (byte) 1) {

                                    i.remove();
                                    // adRemoveApprovalQueue.entityRemove();
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
                                        // adRemoveApprovalQueue.entityRemove();
                                        adRemoveApprovalQueue.setAqApproved(EJBCommon.TRUE);
                                        adRemoveApprovalQueue.setAqApprovedDate(new java.util.Date());
                                        // em.remove(adRemoveApprovalQueue);

                                        if (first) {
                                            first = false;
                                        }

                                    } else {

                                        break;
                                    }
                                }
                            }
                            adApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adApprovalQueue.setAqApprovedDate(new java.util.Date());

                            Collection adRemoveApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode(approvalQueueDocument, approvalQueueDocumentCode, companyCode);

                            if (adRemoveApprovalQueues.size() == 0) {

                                if (arReceipt.getRctVoid() == EJBCommon.FALSE) {

                                    arReceipt.setRctApprovalStatus("APPROVED");

                                } else {

                                    arReceipt.setRctVoidApprovalStatus("APPROVED");
                                }

                                arReceipt.setRctApprovedRejectedBy(username);
                                arReceipt.setRctDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                                if (adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                                    // create cm adjustment for inserting advance payment
                                    if (arReceipt.getRctEnableAdvancePayment() != 0) {

                                        this.postAdvncPymntByCmAdj(arReceipt, arReceipt.getAdBankAccount().getBaName(), branchCode, companyCode);
                                    }

                                    this.executeArRctPost(arReceipt.getRctCode(), username, branchCode, companyCode);
                                }
                            }
                        }

                    } else if (adApprovalQueue.getAqAndOr().equals("OR")) {

                        if (arReceipt.getRctVoid() == EJBCommon.FALSE) {

                            arReceipt.setRctApprovalStatus("APPROVED");

                        } else {

                            arReceipt.setRctVoidApprovalStatus("APPROVED");
                        }

                        arReceipt.setRctApprovedRejectedBy(username);
                        arReceipt.setRctDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                        i = adApprovalQueues.iterator();

                        while (i.hasNext()) {

                            LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                            i.remove();
                            // adRemoveApprovalQueue.entityRemove();
                            adRemoveApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adRemoveApprovalQueue.setAqApprovedDate(new java.util.Date());
                            // em.remove(adRemoveApprovalQueue);

                        }

                        if (adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                            // create cm adjustment for inserting advance payment
                            if (arReceipt.getRctEnableAdvancePayment() != 0) {

                                this.postAdvncPymntByCmAdj(arReceipt, arReceipt.getAdBankAccount().getBaName(), branchCode, companyCode);
                            }

                            this.executeArRctPost(arReceipt.getRctCode(), username, branchCode, companyCode);
                        }
                    }

                } else if (!isApproved) {

                    if (arReceipt.getRctVoid() == EJBCommon.FALSE) {

                        arReceipt.setRctApprovalStatus(null);

                    } else {

                        arReceipt.setRctVoidApprovalStatus(null);
                    }

                    arReceipt.setRctApprovedRejectedBy(username);
                    arReceipt.setRctDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());
                    arReceipt.setRctReasonForRejection(reasonForRejection);

                    i = adAllApprovalQueues.iterator();

                    while (i.hasNext()) {

                        LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                        i.remove();
                        // adRemoveApprovalQueue.entityRemove();
                        em.remove(adRemoveApprovalQueue);
                    }
                }

            } else {

                LocalArSalesOrder arSalesOrder = arSalesOrderHome.findByPrimaryKey(approvalQueueDocumentCode);

                if (isApproved) {

                    if (adApprovalQueue.getAqAndOr().equals("AND")) {

                        if (adApprovalQueues.size() == 1) {

                            arSalesOrder.setSoMemo(arSalesOrder.getSoMemo() + "\n" + memo);
                            arSalesOrder.setSoApprovalStatus("APPROVED");
                            arSalesOrder.setSoApprovedRejectedBy(username);
                            arSalesOrder.setSoDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());
                            arSalesOrder.setSoPosted(EJBCommon.TRUE);
                            arSalesOrder.setSoPostedBy(arSalesOrder.getSoLastModifiedBy());
                            arSalesOrder.setSoDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
                            arSalesOrder.setSoOrderStatus("Good");
                            // adApprovalQueue.entityRemove();
                            adApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adApprovalQueue.setAqApprovedDate(new java.util.Date());
                            // em.remove(adApprovalQueue);

                        } else {

                            // looping up
                            Iterator i = adApprovalQueuesDesc.iterator();

                            while (i.hasNext()) {

                                LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                                if (adRemoveApprovalQueue.getAqUserOr() == (byte) 1) {

                                    i.remove();
                                    // adRemoveApprovalQueue.entityRemove();
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
                                        // adRemoveApprovalQueue.entityRemove();
                                        adRemoveApprovalQueue.setAqApproved(EJBCommon.TRUE);
                                        adRemoveApprovalQueue.setAqApprovedDate(new java.util.Date());
                                        // em.remove(adRemoveApprovalQueue);

                                        if (first) {
                                            first = false;
                                        }

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

                                arSalesOrder.setSoMemo(arSalesOrder.getSoMemo() + "\n" + memo);
                                arSalesOrder.setSoApprovalStatus("APPROVED");
                                arSalesOrder.setSoApprovedRejectedBy(username);
                                arSalesOrder.setSoDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());
                                arSalesOrder.setSoPosted(EJBCommon.TRUE);
                                arSalesOrder.setSoPostedBy(arSalesOrder.getSoLastModifiedBy());
                                arSalesOrder.setSoDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
                                arSalesOrder.setSoOrderStatus("Good");
                            }
                        }

                    } else if (adApprovalQueue.getAqAndOr().equals("OR")) {

                        arSalesOrder.setSoMemo(arSalesOrder.getSoMemo() + "\n" + memo);
                        arSalesOrder.setSoApprovalStatus("APPROVED");
                        arSalesOrder.setSoApprovedRejectedBy(username);
                        arSalesOrder.setSoDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());
                        arSalesOrder.setSoOrderStatus("Good");
                        Iterator i = adApprovalQueues.iterator();

                        while (i.hasNext()) {

                            LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                            i.remove();
                            // adRemoveApprovalQueue.entityRemove();
                            adRemoveApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adRemoveApprovalQueue.setAqApprovedDate(new java.util.Date());
                            // em.remove(adRemoveApprovalQueue);

                        }

                        arSalesOrder.setSoPosted(EJBCommon.TRUE);
                        arSalesOrder.setSoPostedBy(arSalesOrder.getSoLastModifiedBy());
                        arSalesOrder.setSoDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
                    }

                } else if (!isApproved) {

                    arSalesOrder.setSoApprovalStatus(null);
                    arSalesOrder.setSoApprovedRejectedBy(username);
                    arSalesOrder.setSoDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());
                    arSalesOrder.setSoReasonForRejection(reasonForRejection);
                    // arSalesOrder.setSoOrderStatus("Good");

                    Iterator i = adAllApprovalQueues.iterator();

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

        } catch (GlobalBranchAccountNumberInvalidException ex) {

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

        Debug.print("ArApprovalControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods
    private void postAdvncPymntByCmAdj(LocalArReceipt arReceipt, String BA_NM, Integer branchCode, Integer companyCode) throws Exception {

        Debug.print("CmAdjustmentEntryControllerBean saveCmAdjEntry");
        CmAdjustmentDetails details = new CmAdjustmentDetails();
        details.setAdjCode(null);
        details.setAdjType("ADVANCE");
        details.setAdjDate(arReceipt.getRctDate());
        details.setAdjDocumentNumber("");
        details.setAdjReferenceNumber(arReceipt.getRctNumber());
        details.setAdjAmount(arReceipt.getRctExcessAmount());
        details.setAdjConversionDate(arReceipt.getRctConversionDate());
        details.setAdjConversionRate(arReceipt.getRctConversionRate());
        details.setAdjMemo("");
        details.setAdjVoid(arReceipt.getRctVoid());

        details.setAdjCreatedBy(arReceipt.getRctCreatedBy());
        details.setAdjDateCreated(new java.util.Date());

        details.setAdjLastModifiedBy(arReceipt.getRctLastModifiedBy());
        details.setAdjDateLastModified(new java.util.Date());

        /// validate document number is unique

        LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

        try {

            adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("CM ADJUSTMENT", companyCode);

        } catch (FinderException ex) {

        }

        try {

            adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode);

        } catch (FinderException ex) {

        }

        LocalCmAdjustment cmExistingAdjustment = null;

        try {

            cmExistingAdjustment = cmAdjustmentHome.findByAdjDocumentNumberAndBrCode(details.getAdjDocumentNumber(), branchCode, companyCode);

        } catch (FinderException ex) {
        }

        if (cmExistingAdjustment != null) {

            throw new GlobalDocumentNumberNotUniqueException();
        }

        if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (details.getAdjDocumentNumber() == null || details.getAdjDocumentNumber().trim().length() == 0)) {

            while (true) {

                if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                    try {

                        cmAdjustmentHome.findByAdjDocumentNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), branchCode, companyCode);
                        adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                    } catch (FinderException ex) {

                        details.setAdjDocumentNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                        adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                        break;
                    }

                } else {

                    try {

                        cmAdjustmentHome.findByAdjDocumentNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), branchCode, companyCode);
                        adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                    } catch (FinderException ex) {

                        details.setAdjDocumentNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                        adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                        break;
                    }
                }
            }
        }

        // validate if conversion date exists

        try {

            if (details.getAdjConversionDate() != null) {

                LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);

                if (!glValidateFunctionalCurrency.getFcName().equals("USD")) {

                    LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(), details.getAdjConversionDate(), companyCode);

                } else if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                    LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), details.getAdjConversionDate(), companyCode);
                }
            }

        } catch (FinderException ex) {

            throw new GlobalConversionDateNotExistException();
        }

        LocalCmAdjustment cmAdjustment = null;
        try {

            cmAdjustment = cmAdjustmentHome.create(details.getAdjType(), details.getAdjDate(), details.getAdjDocumentNumber(), details.getAdjReferenceNumber(), details.getAdjCheckNumber(), details.getAdjAmount(), 0d, details.getAdjConversionDate(), details.getAdjConversionRate(), details.getAdjMemo(), null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, 0d, null, EJBCommon.FALSE, null, null, EJBCommon.FALSE, details.getAdjCreatedBy(), details.getAdjDateCreated(), details.getAdjLastModifiedBy(), details.getAdjDateLastModified(), null, null, null, null, null, branchCode, companyCode);

            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(BA_NM, companyCode);
            adBankAccount.addCmAdjustment(cmAdjustment);

            // remove all distribution records
            Debug.print("flag 3");
            Collection cmDistributionRecords = cmAdjustment.getCmDistributionRecords();

            Iterator i = cmDistributionRecords.iterator();

            while (i.hasNext()) {

                LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) i.next();

                i.remove();

                // cmDistributionRecord.entityRemove();
                em.remove(cmDistributionRecord);
            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "ADJUSTMENT", EJBCommon.TRUE, cmAdjustment.getAdjAmount(), adBankAccount.getBaCoaGlAdjustmentAccount(), cmAdjustment, branchCode, companyCode);

            this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "CASH", EJBCommon.FALSE, cmAdjustment.getAdjAmount(), adPreference.getPrfArGlCoaCustomerDepositAccount(), cmAdjustment, branchCode, companyCode);

            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(arReceipt.getArCustomer().getCstCustomerCode(), companyCode);

            arCustomer.addCmAdjustment(cmAdjustment);

            String ADJ_APPRVL_STATUS = "N/A";

            this.executeCmAdjPost(cmAdjustment.getAdjCode(), cmAdjustment.getAdjLastModifiedBy(), branchCode, companyCode);
        } catch (Exception ex) {
            throw ex;
        }
    }

    private void executeCmAdjPost(Integer ADJ_CODE, String username, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException {

        Debug.print("CmAdjustmentEntryControllerBean executeCmAdjPost");
        LocalCmAdjustment cmAdjustment = null;
        try {

            // validate if adjustment is already deleted

            try {

                cmAdjustment = cmAdjustmentHome.findByPrimaryKey(ADJ_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if adjustment is already posted or void

            if (cmAdjustment.getAdjPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();

            } else if (cmAdjustment.getAdjVoid() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyVoidException();
            }

            // post advance payment

            // post adjustment

            if (cmAdjustment.getAdjVoid() == EJBCommon.FALSE && cmAdjustment.getAdjPosted() == EJBCommon.FALSE) {

                if (cmAdjustment.getAdjType().equals("INTEREST") || cmAdjustment.getAdjType().equals("DEBIT MEMO")) {

                    // increase bank account balances

                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(cmAdjustment.getAdBankAccount().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(cmAdjustment.getAdjDate(), cmAdjustment.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(cmAdjustment.getAdjDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(cmAdjustment.getAdjDate(), adBankAccountBalance.getBabBalance() + cmAdjustment.getAdjAmount(), "BOOK", companyCode);

                                adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);

                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + cmAdjustment.getAdjAmount());
                            }

                        } else {

                            // create new balance

                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(cmAdjustment.getAdjDate(), (cmAdjustment.getAdjAmount()), "BOOK", companyCode);

                            adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(cmAdjustment.getAdjDate(), cmAdjustment.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        Iterator i = adBankAccountBalances.iterator();

                        while (i.hasNext()) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) i.next();

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + cmAdjustment.getAdjAmount());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }

                } else {

                    // decrease bank account balances

                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(cmAdjustment.getAdBankAccount().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(cmAdjustment.getAdjDate(), cmAdjustment.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(cmAdjustment.getAdjDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(cmAdjustment.getAdjDate(), adBankAccountBalance.getBabBalance() - cmAdjustment.getAdjAmount(), "BOOK", companyCode);

                                adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);

                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - cmAdjustment.getAdjAmount());
                            }

                        } else {

                            // create new balance

                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(cmAdjustment.getAdjDate(), (0 - cmAdjustment.getAdjAmount()), "BOOK", companyCode);

                            adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(cmAdjustment.getAdjDate(), cmAdjustment.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        Iterator i = adBankAccountBalances.iterator();

                        while (i.hasNext()) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) i.next();

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - cmAdjustment.getAdjAmount());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }
            }

            // set adjcher post status

            cmAdjustment.setAdjPosted(EJBCommon.TRUE);
            cmAdjustment.setAdjPostedBy(username);
            cmAdjustment.setAdjDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            // post to gl if necessary

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            if (adPreference.getPrfCmGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                // validate if date has no period and period is closed

                LocalGlSetOfBook glJournalSetOfBook = null;

                try {

                    glJournalSetOfBook = glSetOfBookHome.findByDate(cmAdjustment.getAdjDate(), companyCode);

                } catch (FinderException ex) {

                    throw new GlJREffectiveDateNoPeriodExistException();
                }

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), cmAdjustment.getAdjDate(), companyCode);

                if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P') {

                    throw new GlJREffectiveDatePeriodClosedException();
                }

                // check if invoice is balance if not check suspense posting

                LocalGlJournalLine glOffsetJournalLine = null;

                Collection cmDistributionRecords = cmDistributionRecordHome.findByDrReversalAndDrImportedAndAdjCode(EJBCommon.FALSE, EJBCommon.FALSE, cmAdjustment.getAdjCode(), companyCode);

                Iterator j = cmDistributionRecords.iterator();

                double TOTAL_DEBIT = 0d;
                double TOTAL_CREDIT = 0d;

                while (j.hasNext()) {

                    LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) j.next();

                    double DR_AMNT = this.convertForeignToFunctionalCurrency(cmAdjustment.getAdBankAccount().getGlFunctionalCurrency().getFcCode(), cmAdjustment.getAdBankAccount().getGlFunctionalCurrency().getFcName(), cmAdjustment.getAdjConversionDate(), cmAdjustment.getAdjConversionRate(), cmDistributionRecord.getDrAmount(), companyCode);

                    if (cmDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

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

                        glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("CASH MANAGEMENT", "BANK ADJUSTMENTS", companyCode);

                    } catch (FinderException ex) {

                        throw new GlobalJournalNotBalanceException();
                    }

                    if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                        glOffsetJournalLine = glJournalLineHome.create((short) (cmDistributionRecords.size() + 1), EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);

                    } else {

                        glOffsetJournalLine = glJournalLineHome.create((short) (cmDistributionRecords.size() + 1), EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
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

                    glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " BANK ADJUSTMENTS", branchCode, companyCode);

                } catch (FinderException ex) {
                }

                if (adPreference.getPrfEnableGlJournalBatch() == EJBCommon.TRUE && glJournalBatch == null) {

                    glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " BANK ADJUSTMENTS", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), username, branchCode, companyCode);
                }

                // create journal entry

                LocalGlJournal glJournal = glJournalHome.create(cmAdjustment.getAdjReferenceNumber(), cmAdjustment.getAdjMemo(), cmAdjustment.getAdjDate(), 0.0d, null, cmAdjustment.getAdjDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, username, new Date(), username, new Date(), null, null, username, EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, EJBCommon.FALSE, null, branchCode, companyCode);

                LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("CASH MANAGEMENT", companyCode);
                glJournalSource.addGlJournal(glJournal);

                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                glFunctionalCurrency.addGlJournal(glJournal);

                LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("BANK ADJUSTMENTS", companyCode);
                glJournalCategory.addGlJournal(glJournal);

                if (glJournalBatch != null) {

                    glJournalBatch.addGlJournal(glJournal);
                }

                // create journal lines

                j = cmDistributionRecords.iterator();

                while (j.hasNext()) {

                    LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) j.next();

                    double DR_AMNT = this.convertForeignToFunctionalCurrency(cmAdjustment.getAdBankAccount().getGlFunctionalCurrency().getFcCode(), cmAdjustment.getAdBankAccount().getGlFunctionalCurrency().getFcName(), cmAdjustment.getAdjConversionDate(), cmAdjustment.getAdjConversionRate(), cmDistributionRecord.getDrAmount(), companyCode);

                    LocalGlJournalLine glJournalLine = glJournalLineHome.create(cmDistributionRecord.getDrLine(), cmDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

                    cmDistributionRecord.getGlChartOfAccount().addGlJournalLine(glJournalLine);

                    glJournal.addGlJournalLine(glJournalLine);

                    cmDistributionRecord.setDrImported(EJBCommon.TRUE);

                    // for FOREX revaluation
                    if ((!Objects.equals(cmAdjustment.getAdBankAccount().getGlFunctionalCurrency().getFcCode(), adCompany.getGlFunctionalCurrency().getFcCode())) && glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null && (glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode().equals(cmAdjustment.getAdBankAccount().getGlFunctionalCurrency().getFcCode()))) {

                        double CONVERSION_RATE = 1;

                        if (cmAdjustment.getAdjConversionRate() != 0 && cmAdjustment.getAdjConversionRate() != 1) {

                            CONVERSION_RATE = cmAdjustment.getAdjConversionRate();

                        } else if (cmAdjustment.getAdjConversionDate() != null) {

                            CONVERSION_RATE = this.getFrRateByFrNameAndFrDate(glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), companyCode);
                        }

                        Collection glForexLedgers = null;

                        try {

                            glForexLedgers = glForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(cmAdjustment.getAdjDate(), glJournalLine.getGlChartOfAccount().getCoaCode(), companyCode);

                        } catch (FinderException ex) {

                        }

                        LocalGlForexLedger glForexLedger = (glForexLedgers.isEmpty() || glForexLedgers == null) ? null : (LocalGlForexLedger) glForexLedgers.iterator().next();

                        int FRL_LN = (glForexLedger != null && glForexLedger.getFrlDate().compareTo(cmAdjustment.getAdjDate()) == 0) ? glForexLedger.getFrlLine().intValue() + 1 : 1;

                        // compute balance
                        double COA_FRX_BLNC = glForexLedger == null ? 0 : glForexLedger.getFrlBalance();
                        double FRL_AMNT = cmDistributionRecord.getDrAmount();

                        if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET")) {
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                        } else {
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);
                        }

                        COA_FRX_BLNC = COA_FRX_BLNC + FRL_AMNT;

                        glForexLedger = glForexLedgerHome.create(cmAdjustment.getAdjDate(), FRL_LN, "OTH", FRL_AMNT, CONVERSION_RATE, COA_FRX_BLNC, 0d, companyCode);

                        glJournalLine.getGlChartOfAccount().addGlForexLedger(glForexLedger);

                        // propagate balances
                        try {

                            glForexLedgers = glForexLedgerHome.findByGreaterThanFrlDateAndCoaCode(glForexLedger.getFrlDate(), glForexLedger.getGlChartOfAccount().getCoaCode(), glForexLedger.getFrlAdCompany());

                        } catch (FinderException ex) {

                        }

                        Iterator itrFrl = glForexLedgers.iterator();

                        while (itrFrl.hasNext()) {

                            glForexLedger = (LocalGlForexLedger) itrFrl.next();
                            FRL_AMNT = cmDistributionRecord.getDrAmount();

                            if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET")) {
                                FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                            } else {
                                FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);
                            }

                            glForexLedger.setFrlBalance(glForexLedger.getFrlBalance() + FRL_AMNT);
                        }
                    }
                }

                if (glOffsetJournalLine != null) {

                    glJournal.addGlJournalLine(glOffsetJournalLine);
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

                            if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) {
                                break;
                            }
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

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToInvSo(LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine, Date CST_DT, double CST_QTY_SLD, double CST_CST_OF_SLS, double CST_RMNNG_QTY, double CST_RMNNG_VL, double CST_VRNC_VL, String username, Integer branchCode, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ArInvoiceEntryControllerBean postToInvSo");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalArSalesOrderLine arSalesOrderLine = arSalesOrderInvoiceLine.getArSalesOrderLine();
            LocalInvItemLocation invItemLocation = arSalesOrderLine.getInvItemLocation();
            int CST_LN_NMBR = 0;

            CST_QTY_SLD = EJBCommon.roundIt(CST_QTY_SLD, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_CST_OF_SLS = EJBCommon.roundIt(CST_CST_OF_SLS, adCompany.getGlFunctionalCurrency().getFcPrecision());
            CST_RMNNG_QTY = EJBCommon.roundIt(CST_RMNNG_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_RMNNG_VL = EJBCommon.roundIt(CST_RMNNG_VL, adCompany.getGlFunctionalCurrency().getFcPrecision());

            try {

                // generate line number

                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            } catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                // void subsequent cost variance adjustments
                Collection invAdjustmentLines = invAdjustmentLineHome.findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(CST_DT, invItemLocation.getIlCode(), branchCode, companyCode);
                Iterator i = invAdjustmentLines.iterator();

                while (i.hasNext()) {

                    LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();
                    this.voidInvAdjustment(invAdjustmentLine.getInvAdjustment(), branchCode, companyCode);
                }
            }

            // create costing
            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d, 0d, 0d, CST_QTY_SLD, CST_CST_OF_SLS, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, 0d, branchCode, companyCode);
            // invItemLocation.addInvCosting(invCosting);
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setArSalesOrderInvoiceLine(arSalesOrderInvoiceLine);

            invCosting.setCstQuantity(CST_QTY_SLD * -1);
            invCosting.setCstCost(CST_CST_OF_SLS * -1);

            // if cost variance is not 0, generate cost variance for the transaction
            if (CST_VRNC_VL != 0 && adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {

                this.generateCostVariance(invCosting.getInvItemLocation(), CST_VRNC_VL, "ARCM" + arSalesOrderInvoiceLine.getArInvoice().getInvNumber(), arSalesOrderInvoiceLine.getArInvoice().getInvDescription(), arSalesOrderInvoiceLine.getArInvoice().getInvDate(), username, branchCode, companyCode);
            }

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);

            Iterator i = invCostings.iterator();

            while (i.hasNext()) {

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) i.next();

                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_QTY_SLD);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_CST_OF_SLS);
            }

            // regenerate cost varaince
            if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                this.regenerateCostVariance(invCostings, invCosting, branchCode, companyCode);
            }

        } catch (AdPRFCoaGlVarianceAccountNotFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void addCmDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE, LocalCmAdjustment cmAdjustment, Integer branchCode, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArInvoiceEntryControllerBean addCmDrEntry");
        try {

            // get company
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, branchCode, companyCode);

            // create distribution record

            LocalCmDistributionRecord cmDistributionRecord = cmDistributionRecordHome.create(DR_LN, DR_CLSS, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), DR_DBT, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);

            cmAdjustment.addCmDistributionRecord(cmDistributionRecord);
            glChartOfAccount.addCmDistributionRecord(cmDistributionRecord);
        } catch (FinderException ex) {

            throw new GlobalBranchAccountNumberInvalidException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void executeArInvPost(Integer INV_CODE, String username, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ArApprovalControllerBean executeApInvPost");
        LocalArInvoice arInvoice = null;
        LocalArInvoice arCreditMemo = null;
        LocalArInvoice arCreditedInvoice = null;
        LocalInvItemLocationHome invItemLocationHome = null;
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            // validate if invoice/credit memo is already deleted

            try {

                arInvoice = arInvoiceHome.findByPrimaryKey(INV_CODE);
                arCreditMemo = arInvoiceHome.findByPrimaryKey(INV_CODE);
            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if invoice/credit memo is already posted or void

            if (arInvoice.getInvPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();

            } else if (arInvoice.getInvVoid() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyVoidException();
            }

            // regenerate cogs & inventory dr
            if (adPreference.getPrfArAutoComputeCogs() == EJBCommon.TRUE) {
                this.regenerateInventoryDr(arInvoice, branchCode, companyCode);
            }

            // post invoice/credit memo

            if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {

                // increase customer balance

                double INV_AMNT = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arInvoice.getInvAmountDue(), companyCode);

                this.post(arInvoice.getInvDate(), INV_AMNT, arInvoice.getArCustomer(), companyCode);

                Collection arInvoiceLineItems = arInvoice.getArInvoiceLineItems();
                Collection arSalesOrderInvoiceLines = arInvoice.getArSalesOrderInvoiceLines();

                if (arInvoiceLineItems != null && !arInvoiceLineItems.isEmpty()) {

                    Iterator c = arInvoiceLineItems.iterator();

                    while (c.hasNext()) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) c.next();

                        String II_NM = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName();
                        String LOC_NM = arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName();

                        double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                        LocalInvCosting invCosting = null;

                        try {

                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arInvoice.getInvDate(), II_NM, LOC_NM, branchCode, companyCode);

                        } catch (FinderException ex) {

                        }

                        double COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                        if (invCosting == null) {

                            this.postToInv(arInvoiceLineItem, arInvoice.getInvDate(), QTY_SLD, QTY_SLD * COST, -QTY_SLD, -QTY_SLD * COST, 0d, null, branchCode, companyCode);

                        } else {

                            if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Average")) {

                                double avgCost = invCosting.getCstRemainingQuantity() == 0 ? COST : Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                                this.postToInv(arInvoiceLineItem, arInvoice.getInvDate(), QTY_SLD, avgCost * QTY_SLD, invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (avgCost * QTY_SLD), 0d, null, branchCode, companyCode);

                            } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("FIFO")) {

                                double fifoCost = invCosting.getCstRemainingQuantity() == 0 ? COST : this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), QTY_SLD, arInvoiceLineItem.getIliUnitPrice(), true, branchCode, companyCode);

                                this.postToInv(arInvoiceLineItem, arInvoice.getInvDate(), QTY_SLD, fifoCost * QTY_SLD, invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (fifoCost * QTY_SLD), 0d, null, branchCode, companyCode);

                            } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Standard")) {

                                double standardCost = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();

                                this.postToInv(arInvoiceLineItem, arInvoice.getInvDate(), QTY_SLD, standardCost * QTY_SLD, invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (standardCost * QTY_SLD), 0d, null, branchCode, companyCode);
                            }
                        }
                    }

                } else if (arSalesOrderInvoiceLines != null && !arSalesOrderInvoiceLines.isEmpty()) {

                    Iterator c = arSalesOrderInvoiceLines.iterator();

                    while (c.hasNext()) {

                        LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) c.next();
                        LocalArSalesOrderLine arSalesOrderLine = arSalesOrderInvoiceLine.getArSalesOrderLine();

                        String II_NM = arSalesOrderLine.getInvItemLocation().getInvItem().getIiName();
                        String LOC_NM = arSalesOrderLine.getInvItemLocation().getInvLocation().getLocName();
                        double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arSalesOrderLine.getInvUnitOfMeasure(), arSalesOrderLine.getInvItemLocation().getInvItem(), arSalesOrderInvoiceLine.getSilQuantityDelivered(), companyCode);

                        LocalInvCosting invCosting = null;

                        try {

                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arInvoice.getInvDate(), II_NM, LOC_NM, branchCode, companyCode);

                        } catch (FinderException ex) {

                        }

                        double COST = arSalesOrderLine.getInvItemLocation().getInvItem().getIiUnitCost();

                        if (invCosting == null) {

                            this.postToInvSo(arSalesOrderInvoiceLine, arInvoice.getInvDate(), QTY_SLD, COST * QTY_SLD, -QTY_SLD, -(COST * QTY_SLD), 0d, null, branchCode, companyCode);

                        } else {

                            if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Average")) {

                                double avgCost = invCosting.getCstRemainingQuantity() == 0 ? COST : Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                                this.postToInvSo(arSalesOrderInvoiceLine, arInvoice.getInvDate(), QTY_SLD, avgCost * QTY_SLD, invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (avgCost * QTY_SLD), 0d, null, branchCode, companyCode);

                            } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("FIFO")) {

                                double fifoCost = this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), QTY_SLD, arSalesOrderInvoiceLine.getArSalesOrderLine().getSolUnitPrice(), true, branchCode, companyCode);

                                this.postToInvSo(arSalesOrderInvoiceLine, arInvoice.getInvDate(), QTY_SLD, fifoCost * QTY_SLD, invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (fifoCost * QTY_SLD), 0d, null, branchCode, companyCode);

                            } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Standard")) {

                                double standardCost = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();

                                this.postToInvSo(arSalesOrderInvoiceLine, arInvoice.getInvDate(), QTY_SLD, standardCost * QTY_SLD, invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (standardCost * QTY_SLD), 0d, null, branchCode, companyCode);
                            }
                        }
                    }
                }

            } else {
                if (arCreditMemo.getInvVoid() == EJBCommon.TRUE && arCreditMemo.getInvVoidPosted() == EJBCommon.FALSE) {

                    // get credited invoic
                    arCreditedInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arCreditMemo.getInvCmInvoiceNumber(), EJBCommon.FALSE, branchCode, companyCode);

                    // increase customer balance

                    double INV_AMNT = this.convertForeignToFunctionalCurrency(arCreditedInvoice.getGlFunctionalCurrency().getFcCode(), arCreditedInvoice.getGlFunctionalCurrency().getFcName(), arCreditedInvoice.getInvConversionDate(), arCreditedInvoice.getInvConversionRate(), arCreditMemo.getInvAmountDue(), companyCode);

                    this.post(arCreditMemo.getInvDate(), INV_AMNT, arCreditMemo.getArCustomer(), companyCode);

                    // decrease invoice and ips amounts and release lock

                    double CREDIT_PERCENT = EJBCommon.roundIt(arCreditMemo.getInvAmountDue() / arCreditedInvoice.getInvAmountDue(), (short) 6);

                    arCreditedInvoice.setInvAmountPaid(arCreditedInvoice.getInvAmountPaid() - arCreditMemo.getInvAmountDue());

                    double TOTAL_INVOICE_PAYMENT_SCHEDULE = 0d;

                    Collection arInvoicePaymentSchedules = arCreditedInvoice.getArInvoicePaymentSchedules();

                    Iterator i = arInvoicePaymentSchedules.iterator();

                    while (i.hasNext()) {

                        LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) i.next();

                        double INVOICE_PAYMENT_SCHEDULE_AMOUNT = 0;

                        // if last payment schedule subtract to avoid rounding difference error

                        if (i.hasNext()) {

                            INVOICE_PAYMENT_SCHEDULE_AMOUNT = EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsAmountDue() * CREDIT_PERCENT, this.getGlFcPrecisionUnit(companyCode));

                        } else {

                            INVOICE_PAYMENT_SCHEDULE_AMOUNT = arCreditMemo.getInvAmountDue() - TOTAL_INVOICE_PAYMENT_SCHEDULE;
                        }

                        arInvoicePaymentSchedule.setIpsAmountPaid(arInvoicePaymentSchedule.getIpsAmountPaid() - INVOICE_PAYMENT_SCHEDULE_AMOUNT);

                        arInvoicePaymentSchedule.setIpsLock(EJBCommon.FALSE);

                        TOTAL_INVOICE_PAYMENT_SCHEDULE += INVOICE_PAYMENT_SCHEDULE_AMOUNT;
                    }

                    Collection arInvoiceLineItems = arCreditMemo.getArInvoiceLineItems();

                    if (arInvoiceLineItems != null && !arInvoiceLineItems.isEmpty()) {

                        Iterator c = arInvoiceLineItems.iterator();

                        while (c.hasNext()) {

                            LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) c.next();

                            String II_NM = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName();
                            String LOC_NM = arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName();

                            double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                            LocalInvCosting invCosting = null;

                            try {

                                invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arCreditMemo.getInvDate(), II_NM, LOC_NM, branchCode, companyCode);

                            } catch (FinderException ex) {

                            }

                            double COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                            if (invCosting == null) {

                                this.postToInv(arInvoiceLineItem, arCreditMemo.getInvDate(), QTY_SLD, COST * QTY_SLD, QTY_SLD, COST * QTY_SLD, 0d, null, branchCode, companyCode);

                            } else {

                                if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Average")) {

                                    double avgCost = invCosting.getCstRemainingQuantity() == 0 ? COST : Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                                    this.postToInv(arInvoiceLineItem, arCreditMemo.getInvDate(), QTY_SLD, (avgCost * QTY_SLD), invCosting.getCstRemainingQuantity() + QTY_SLD, invCosting.getCstRemainingValue() + (avgCost * QTY_SLD), 0d, null, branchCode, companyCode);

                                } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("FIFO")) {

                                    double fifoCost = invCosting.getCstRemainingQuantity() == 0 ? COST : this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), QTY_SLD, arInvoiceLineItem.getIliUnitPrice(), true, branchCode, companyCode);

                                    this.postToInv(arInvoiceLineItem, arCreditMemo.getInvDate(), QTY_SLD, (fifoCost * QTY_SLD), invCosting.getCstRemainingQuantity() + QTY_SLD, invCosting.getCstRemainingValue() + (fifoCost * QTY_SLD), 0d, null, branchCode, companyCode);

                                } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Standard")) {

                                    double standardCost = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();

                                    this.postToInv(arInvoiceLineItem, arCreditMemo.getInvDate(), QTY_SLD, (standardCost * QTY_SLD), invCosting.getCstRemainingQuantity() + QTY_SLD, invCosting.getCstRemainingValue() + (standardCost * QTY_SLD), 0d, null, branchCode, companyCode);
                                }
                            }
                        }
                    }

                    // set cmAdjustment post status

                    arCreditMemo.setInvVoidPosted(EJBCommon.TRUE);
                    arCreditMemo.setInvPostedBy(username);
                    arCreditMemo.setInvDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

                } else if (arCreditMemo.getInvVoid() == EJBCommon.FALSE && arCreditMemo.getInvPosted() == EJBCommon.FALSE) {
                    // get credited invoice
                    arCreditedInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arCreditMemo.getInvCmInvoiceNumber(), EJBCommon.FALSE, branchCode, companyCode);

                    // decrease customer balance
                    double INV_AMNT = this.convertForeignToFunctionalCurrency(arCreditedInvoice.getGlFunctionalCurrency().getFcCode(), arCreditedInvoice.getGlFunctionalCurrency().getFcName(), arCreditedInvoice.getInvConversionDate(), arCreditedInvoice.getInvConversionRate(), arCreditMemo.getInvAmountDue(), companyCode);

                    this.post(arCreditMemo.getInvDate(), -INV_AMNT, arCreditMemo.getArCustomer(), companyCode);

                    // decrease invoice and ips amounts and release lock

                    double CREDIT_PERCENT = EJBCommon.roundIt(arCreditMemo.getInvAmountDue() / arCreditedInvoice.getInvAmountDue(), (short) 6);

                    arCreditedInvoice.setInvAmountPaid(arCreditedInvoice.getInvAmountPaid() + arCreditMemo.getInvAmountDue());

                    double TOTAL_INVOICE_PAYMENT_SCHEDULE = 0d;

                    Collection arInvoicePaymentSchedules = arCreditedInvoice.getArInvoicePaymentSchedules();

                    Iterator i = arInvoicePaymentSchedules.iterator();

                    while (i.hasNext()) {

                        LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) i.next();

                        double INVOICE_PAYMENT_SCHEDULE_AMOUNT = 0;

                        // if last payment schedule subtract to avoid rounding difference error

                        if (i.hasNext()) {

                            INVOICE_PAYMENT_SCHEDULE_AMOUNT = EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsAmountDue() * CREDIT_PERCENT, this.getGlFcPrecisionUnit(companyCode));

                        } else {

                            INVOICE_PAYMENT_SCHEDULE_AMOUNT = arCreditMemo.getInvAmountDue() - TOTAL_INVOICE_PAYMENT_SCHEDULE;
                        }

                        arInvoicePaymentSchedule.setIpsAmountPaid(arInvoicePaymentSchedule.getIpsAmountPaid() + INVOICE_PAYMENT_SCHEDULE_AMOUNT);

                        arInvoicePaymentSchedule.setIpsLock(EJBCommon.FALSE);

                        TOTAL_INVOICE_PAYMENT_SCHEDULE += INVOICE_PAYMENT_SCHEDULE_AMOUNT;
                    }

                    Collection arInvoiceLineItems = arCreditMemo.getArInvoiceLineItems();

                    if (arInvoiceLineItems != null && !arInvoiceLineItems.isEmpty()) {

                        Iterator c = arInvoiceLineItems.iterator();

                        while (c.hasNext()) {

                            LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) c.next();

                            String II_NM = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName();
                            String LOC_NM = arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName();

                            double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                            LocalInvCosting invCosting = null;

                            try {

                                invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arCreditMemo.getInvDate(), II_NM, LOC_NM, branchCode, companyCode);

                            } catch (FinderException ex) {
                            }

                            double COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                            if (invCosting == null) {

                                this.postToInv(arInvoiceLineItem, arCreditMemo.getInvDate(), -QTY_SLD, -COST * QTY_SLD, QTY_SLD, COST * QTY_SLD, 0d, null, branchCode, companyCode);

                            } else {

                                if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Average")) {

                                    double avgCost = invCosting.getCstRemainingQuantity() == 0 ? COST : Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                                    this.postToInv(arInvoiceLineItem, arCreditMemo.getInvDate(), -QTY_SLD, -avgCost * QTY_SLD, invCosting.getCstRemainingQuantity() + QTY_SLD, invCosting.getCstRemainingValue() + (avgCost * QTY_SLD), 0d, null, branchCode, companyCode);

                                } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("FIFO")) {

                                    double fifoCost = invCosting.getCstRemainingQuantity() == 0 ? COST : this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), QTY_SLD, arInvoiceLineItem.getIliUnitPrice() * QTY_SLD, true, branchCode, companyCode);

                                    // post entries to database
                                    this.postToInv(arInvoiceLineItem, arCreditMemo.getInvDate(), -QTY_SLD, -fifoCost * QTY_SLD, invCosting.getCstRemainingQuantity() + QTY_SLD, invCosting.getCstRemainingValue() + (fifoCost * QTY_SLD), 0d, null, branchCode, companyCode);

                                } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Standard")) {
                                    double standardCost = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();

                                    // post entries to database
                                    this.postToInv(arInvoiceLineItem, arCreditMemo.getInvDate(), -QTY_SLD, -standardCost * QTY_SLD, invCosting.getCstRemainingQuantity() + QTY_SLD, invCosting.getCstRemainingValue() + (standardCost * QTY_SLD), 0d, null, branchCode, companyCode);
                                }
                            }
                        }
                    }
                }
            }

            // set invoice post status

            arInvoice.setInvPosted(EJBCommon.TRUE);
            arInvoice.setInvPostedBy(username);
            arInvoice.setInvDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            // post to gl if necessary

            if (adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                // validate if date has no period and period is closed

                LocalGlSetOfBook glJournalSetOfBook = null;

                try {

                    glJournalSetOfBook = glSetOfBookHome.findByDate(arInvoice.getInvDate(), companyCode);

                } catch (FinderException ex) {

                    throw new GlJREffectiveDateNoPeriodExistException();
                }

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), arInvoice.getInvDate(), companyCode);

                if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P') {

                    throw new GlJREffectiveDatePeriodClosedException();
                }

                // check if invoice is balance if not check suspense posting

                LocalGlJournalLine glOffsetJournalLine = null;

                Collection arDistributionRecords = arDistributionRecordHome.findImportableDrByInvCode(arInvoice.getInvCode(), companyCode);

                Iterator j = arDistributionRecords.iterator();

                double TOTAL_DEBIT = 0d;
                double TOTAL_CREDIT = 0d;

                while (j.hasNext()) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) j.next();

                    double DR_AMNT = 0d;

                    if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), companyCode);

                    } else {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arCreditedInvoice.getGlFunctionalCurrency().getFcCode(), arCreditedInvoice.getGlFunctionalCurrency().getFcName(), arCreditedInvoice.getInvConversionDate(), arCreditedInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), companyCode);
                    }

                    if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

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

                        glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("ACCOUNTS RECEIVABLES", arInvoice.getInvCreditMemo() == EJBCommon.FALSE ? "SALES INVOICES" : "CREDIT MEMOS", companyCode);

                    } catch (FinderException ex) {

                        throw new GlobalJournalNotBalanceException();
                    }

                    if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                        glOffsetJournalLine = glJournalLineHome.create((short) (arDistributionRecords.size() + 1), EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);

                    } else {

                        glOffsetJournalLine = glJournalLineHome.create((short) (arDistributionRecords.size() + 1), EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
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

                    if (adPreference.getPrfEnableArInvoiceBatch() == EJBCommon.TRUE) {

                        glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " " + arInvoice.getArInvoiceBatch().getIbName(), branchCode, companyCode);

                    } else {

                        glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " SALES INVOICES", branchCode, companyCode);
                    }

                } catch (FinderException ex) {
                }

                if (adPreference.getPrfEnableGlJournalBatch() == EJBCommon.TRUE && glJournalBatch == null) {

                    if (adPreference.getPrfEnableArInvoiceBatch() == EJBCommon.TRUE) {

                        glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " " + arInvoice.getArInvoiceBatch().getIbName(), "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), username, branchCode, companyCode);

                    } else {

                        glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " SALES INVOICES", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), username, branchCode, companyCode);
                    }
                }

                // create journal entry

                LocalGlJournal glJournal = glJournalHome.create(arInvoice.getInvCreditMemo() == EJBCommon.FALSE ? arInvoice.getInvReferenceNumber() : arInvoice.getInvCmInvoiceNumber(), arInvoice.getInvDescription(), arInvoice.getInvDate(), 0.0d, null, arInvoice.getInvNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, username, new Date(), username, new Date(), null, null, username, EJBCommon.getGcCurrentDateWoTime().getTime(), arInvoice.getArCustomer().getCstTin(), arInvoice.getArCustomer().getCstName(), EJBCommon.FALSE, null, branchCode, companyCode);

                LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("ACCOUNTS RECEIVABLES", companyCode);
                glJournal.setGlJournalSource(glJournalSource);

                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

                LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName(arInvoice.getInvCreditMemo() == EJBCommon.FALSE ? "SALES INVOICES" : "CREDIT MEMOS", companyCode);
                glJournal.setGlJournalCategory(glJournalCategory);

                if (glJournalBatch != null) {

                    glJournal.setGlJournalBatch(glJournalBatch);
                }

                // create journal lines

                j = arDistributionRecords.iterator();

                while (j.hasNext()) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) j.next();

                    double DR_AMNT = 0d;

                    if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), companyCode);

                    } else {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arCreditedInvoice.getGlFunctionalCurrency().getFcCode(), arCreditedInvoice.getGlFunctionalCurrency().getFcName(), arCreditedInvoice.getInvConversionDate(), arCreditedInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), companyCode);
                    }

                    LocalGlJournalLine glJournalLine = glJournalLineHome.create(arDistributionRecord.getDrLine(), arDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

                    arDistributionRecord.getGlChartOfAccount().addGlJournalLine(glJournalLine);

                    glJournal.addGlJournalLine(glJournalLine);

                    arDistributionRecord.setDrImported(EJBCommon.TRUE);

                    // for FOREX revaluation

                    LocalArInvoice arInvoiceTemp = arInvoice.getInvCreditMemo() == EJBCommon.FALSE ? arInvoice : arCreditedInvoice;

                    if ((!Objects.equals(arInvoiceTemp.getGlFunctionalCurrency().getFcCode(), adCompany.getGlFunctionalCurrency().getFcCode())) && glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null && (glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode().equals(arInvoiceTemp.getGlFunctionalCurrency().getFcCode()))) {

                        double CONVERSION_RATE = 1;

                        if (arInvoiceTemp.getInvConversionRate() != 0 && arInvoiceTemp.getInvConversionRate() != 1) {

                            CONVERSION_RATE = arInvoiceTemp.getInvConversionRate();

                        } else if (arInvoice.getInvConversionDate() != null) {

                            CONVERSION_RATE = this.getFrRateByFrNameAndFrDate(glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), companyCode);
                        }

                        Collection glForexLedgers = null;

                        try {

                            glForexLedgers = glForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(arInvoiceTemp.getInvDate(), glJournalLine.getGlChartOfAccount().getCoaCode(), companyCode);

                        } catch (FinderException ex) {

                        }

                        LocalGlForexLedger glForexLedger = (glForexLedgers.isEmpty() || glForexLedgers == null) ? null : (LocalGlForexLedger) glForexLedgers.iterator().next();

                        int FRL_LN = (glForexLedger != null && glForexLedger.getFrlDate().compareTo(arInvoiceTemp.getInvDate()) == 0) ? glForexLedger.getFrlLine().intValue() + 1 : 1;

                        // compute balance
                        double COA_FRX_BLNC = glForexLedger == null ? 0 : glForexLedger.getFrlBalance();
                        double FRL_AMNT = arDistributionRecord.getDrAmount();

                        if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET")) {
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                        } else {
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);
                        }

                        COA_FRX_BLNC = COA_FRX_BLNC + FRL_AMNT;

                        glForexLedger = glForexLedgerHome.create(arInvoiceTemp.getInvDate(), FRL_LN, arInvoice.getInvCreditMemo() == EJBCommon.FALSE ? "SI" : "CM", FRL_AMNT, CONVERSION_RATE, COA_FRX_BLNC, 0d, companyCode);

                        glJournalLine.getGlChartOfAccount().addGlForexLedger(glForexLedger);

                        // propagate balances
                        try {

                            glForexLedgers = glForexLedgerHome.findByGreaterThanFrlDateAndCoaCode(glForexLedger.getFrlDate(), glForexLedger.getGlChartOfAccount().getCoaCode(), glForexLedger.getFrlAdCompany());

                        } catch (FinderException ex) {

                        }

                        Iterator itrFrl = glForexLedgers.iterator();

                        while (itrFrl.hasNext()) {

                            glForexLedger = (LocalGlForexLedger) itrFrl.next();
                            FRL_AMNT = arDistributionRecord.getDrAmount();

                            if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET")) {
                                FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                            } else {
                                FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);
                            }

                            glForexLedger.setFrlBalance(glForexLedger.getFrlBalance() + FRL_AMNT);
                        }
                    }
                }

                if (glOffsetJournalLine != null) {

                    glJournal.addGlJournalLine(glOffsetJournalLine);
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

                            if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) {
                                break;
                            }
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

        } catch (GlobalBranchAccountNumberInvalidException ex) {

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

    private void executeArRctPost(Integer RCT_CODE, String username, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ArApprovalControllerBean executeArRctPost");
        LocalArReceipt arReceipt = null;
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate if receipt is already deleted

            try {

                arReceipt = arReceiptHome.findByPrimaryKey(RCT_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if receipt is already posted

            if (arReceipt.getRctVoid() == EJBCommon.FALSE && arReceipt.getRctPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();

                // validate if receipt void is already posted

            } else if (arReceipt.getRctVoid() == EJBCommon.TRUE && arReceipt.getRctVoidPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyVoidPostedException();
            }

            if (arReceipt.getRctVoid() == EJBCommon.FALSE && arReceipt.getRctType().equals("MISC") && !arReceipt.getArInvoiceLineItems().isEmpty()) {

                // regenerate inventory dr

                this.regenerateInventoryDr(arReceipt, branchCode, companyCode);
            }

            // post receipt

            if (arReceipt.getRctVoid() == EJBCommon.FALSE && arReceipt.getRctPosted() == EJBCommon.FALSE) {

                if (arReceipt.getRctType().equals("COLLECTION")) {

                    double RCT_CRDTS = 0d;
                    double RCT_AI_APPLD_DPSTS = 0d;
                    double RCT_EXCSS_AMNT = arReceipt.getRctExcessAmount();

                    // create adjustment for advance payment
                    if (arReceipt.getRctEnableAdvancePayment() != 0) {
                    }

                    // increase amount paid in invoice payment schedules and invoice

                    Collection arAppliedInvoices = arReceipt.getArAppliedInvoices();

                    Iterator i = arAppliedInvoices.iterator();

                    while (i.hasNext()) {

                        LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) i.next();

                        LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = arAppliedInvoice.getArInvoicePaymentSchedule();

                        double AMOUNT_PAID = arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiCreditableWTax() + arAppliedInvoice.getAiDiscountAmount() + arAppliedInvoice.getAiAppliedDeposit() + arAppliedInvoice.getAiCreditBalancePaid() + arAppliedInvoice.getAiRebate();

                        double PENALTY_PAID = arAppliedInvoice.getAiPenaltyApplyAmount();

                        RCT_CRDTS += this.convertForeignToFunctionalCurrency(arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcCode(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcName(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(), arAppliedInvoice.getAiDiscountAmount() + arAppliedInvoice.getAiCreditableWTax(), companyCode);

                        RCT_AI_APPLD_DPSTS += this.convertForeignToFunctionalCurrency(arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcCode(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcName(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(), arAppliedInvoice.getAiAppliedDeposit(), companyCode);

                        arInvoicePaymentSchedule.setIpsAmountPaid(EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsAmountPaid() + AMOUNT_PAID, this.getGlFcPrecisionUnit(companyCode)));
                        arInvoicePaymentSchedule.setIpsPenaltyPaid(EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsPenaltyPaid() + PENALTY_PAID, this.getGlFcPrecisionUnit(companyCode)));

                        arInvoicePaymentSchedule.getArInvoice().setInvAmountPaid(EJBCommon.roundIt(arInvoicePaymentSchedule.getArInvoice().getInvAmountPaid() + AMOUNT_PAID, this.getGlFcPrecisionUnit(companyCode)));

                        arInvoicePaymentSchedule.getArInvoice().setInvPenaltyPaid(EJBCommon.roundIt(arInvoicePaymentSchedule.getArInvoice().getInvPenaltyPaid() + PENALTY_PAID, this.getGlFcPrecisionUnit(companyCode)));

                        // release invoice lock

                        arInvoicePaymentSchedule.setIpsLock(EJBCommon.FALSE);
                    }

                    // decrease customer balance

                    double RCT_AMNT = this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmount(), companyCode);

                    this.post(arReceipt.getRctDate(), (RCT_AMNT + RCT_CRDTS + RCT_AI_APPLD_DPSTS) * -1, arReceipt.getArCustomer(), companyCode);

                } else if (arReceipt.getRctType().equals("MISC") && !arReceipt.getArInvoiceLineItems().isEmpty()) {

                    Iterator c = arReceipt.getArInvoiceLineItems().iterator();

                    while (c.hasNext()) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) c.next();

                        String II_NM = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName();
                        String LOC_NM = arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName();

                        double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                        LocalInvCosting invCosting = null;

                        try {

                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arReceipt.getRctDate(), II_NM, LOC_NM, branchCode, companyCode);

                        } catch (FinderException ex) {

                        }

                        double COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                        if (invCosting == null) {

                            this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), QTY_SLD, QTY_SLD * COST, -QTY_SLD, -COST * QTY_SLD, 0d, null, branchCode, companyCode);
                        } else {

                            if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Average")) {

                                double avgCost = invCosting.getCstRemainingQuantity() == 0 ? COST : Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                                this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), QTY_SLD, avgCost * QTY_SLD, invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (avgCost * QTY_SLD), 0d, null, branchCode, companyCode);

                            } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("FIFO")) {

                                double fifoCost = invCosting.getCstRemainingQuantity() == 0 ? COST : this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), QTY_SLD, arInvoiceLineItem.getIliUnitPrice(), true, branchCode, companyCode);

                                this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), QTY_SLD, fifoCost * QTY_SLD, invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (fifoCost * QTY_SLD), 0d, null, branchCode, companyCode);

                            } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Standard")) {

                                double standardCost = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();

                                this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), QTY_SLD, standardCost * QTY_SLD, invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (standardCost * QTY_SLD), 0d, null, branchCode, companyCode);
                            }
                        }
                    }
                }
                // increase bank balance CASH
                if (arReceipt.getRctAmountCash() > 0) {

                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccount().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCash(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCash());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCash()=" + arReceipt.getRctAmountCash());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (arReceipt.getRctAmountCash()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        Iterator i = adBankAccountBalances.iterator();

                        while (i.hasNext()) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) i.next();

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCash());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // increase bank balance CARD 1
                if (arReceipt.getRctAmountCard1() > 0) {

                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccountCard1().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard1().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard1(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard1());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCard1()=" + arReceipt.getRctAmountCard1());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (arReceipt.getRctAmountCard1()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard1().getBaCode(), "BOOK", companyCode);

                        Iterator i = adBankAccountBalances.iterator();

                        while (i.hasNext()) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) i.next();

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard1());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // increase bank balance CARD 2
                if (arReceipt.getRctAmountCard2() > 0) {
                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccountCard2().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard2().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard2(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard2());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCard2()=" + arReceipt.getRctAmountCard2());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (arReceipt.getRctAmountCard2()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard2().getBaCode(), "BOOK", companyCode);

                        Iterator i = adBankAccountBalances.iterator();

                        while (i.hasNext()) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) i.next();

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard2());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // increase bank balance CARD 3
                if (arReceipt.getRctAmountCard3() > 0) {
                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccountCard3().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard3().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard3(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard3());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCard3()=" + arReceipt.getRctAmountCard3());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (arReceipt.getRctAmountCard3()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard3().getBaCode(), "BOOK", companyCode);

                        Iterator i = adBankAccountBalances.iterator();

                        while (i.hasNext()) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) i.next();

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard3());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }
                // increase bank balance CHEQUE
                if (arReceipt.getRctAmountCheque() > 0) {
                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccount().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCheque(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCheque());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCheque()=" + arReceipt.getRctAmountCheque());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (arReceipt.getRctAmountCheque()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        Iterator i = adBankAccountBalances.iterator();

                        while (i.hasNext()) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) i.next();

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCheque());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // set receipt post status

                arReceipt.setRctPosted(EJBCommon.TRUE);
                arReceipt.setRctPostedBy(username);
                arReceipt.setRctDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

                // increase bank balance

                LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccount().getBaCode());

                try {

                    // find bankaccount balance before or equal receipt date

                    Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                    if (!adBankAccountBalances.isEmpty()) {

                        // get last check

                        ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                        if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                            // create new balance

                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmount(), "BOOK", companyCode);

                            adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);

                        } else { // equals to check date

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmount());
                        }

                    } else {

                        // create new balance

                        LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (arReceipt.getRctAmount()), "BOOK", companyCode);

                        adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                    }

                    // propagate to subsequent balances if necessary

                    adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                    Iterator i = adBankAccountBalances.iterator();

                    while (i.hasNext()) {

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) i.next();

                        adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmount());
                    }

                } catch (Exception ex) {

                    ex.printStackTrace();
                }

                // set receipt post status

                arReceipt.setRctPosted(EJBCommon.TRUE);
                arReceipt.setRctPostedBy(username);
                arReceipt.setRctDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            } else if (arReceipt.getRctVoid() == EJBCommon.TRUE && arReceipt.getRctVoidPosted() == EJBCommon.FALSE) {

                if (arReceipt.getRctType().equals("COLLECTION")) {

                    double RCT_CRDTS = 0d;
                    double RCT_AI_APPLD_DPSTS = 0d;

                    // decrease amount paid in invoice payment schedules and invoice

                    Collection arAppliedInvoices = arReceipt.getArAppliedInvoices();

                    Iterator i = arAppliedInvoices.iterator();

                    while (i.hasNext()) {

                        LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) i.next();

                        LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = arAppliedInvoice.getArInvoicePaymentSchedule();

                        double AMOUNT_PAID = arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiCreditableWTax() + arAppliedInvoice.getAiDiscountAmount() + arAppliedInvoice.getAiAppliedDeposit() + arAppliedInvoice.getAiCreditBalancePaid() + arAppliedInvoice.getAiRebate();

                        double PENALTY_PAID = arAppliedInvoice.getAiPenaltyApplyAmount();

                        RCT_CRDTS += this.convertForeignToFunctionalCurrency(arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcCode(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcName(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(), arAppliedInvoice.getAiDiscountAmount() + arAppliedInvoice.getAiCreditableWTax(), companyCode);

                        RCT_AI_APPLD_DPSTS += this.convertForeignToFunctionalCurrency(arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcCode(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcName(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(), arAppliedInvoice.getAiAppliedDeposit(), companyCode);

                        try {

                            Collection arAppliedCredits = arAppliedInvoice.getArAppliedCredits();

                            Debug.print("arAppliedCredits=" + arAppliedCredits.size());
                            Iterator x = arAppliedCredits.iterator();

                            while (x.hasNext()) {

                                LocalArAppliedCredit arAppliedCredit = (LocalArAppliedCredit) x.next();

                                x.remove();

                                // arAppliedCredit.entityRemove();
                                em.remove(arAppliedCredit);
                            }

                        } catch (Exception ex) {

                        }

                        arInvoicePaymentSchedule.setIpsAmountPaid(EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsAmountPaid() - AMOUNT_PAID, this.getGlFcPrecisionUnit(companyCode)));
                        arInvoicePaymentSchedule.setIpsPenaltyPaid(EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsPenaltyPaid() - PENALTY_PAID, this.getGlFcPrecisionUnit(companyCode)));

                        arInvoicePaymentSchedule.getArInvoice().setInvAmountPaid(EJBCommon.roundIt(arInvoicePaymentSchedule.getArInvoice().getInvAmountPaid() - AMOUNT_PAID, this.getGlFcPrecisionUnit(companyCode)));
                        arInvoicePaymentSchedule.getArInvoice().setInvPenaltyPaid(EJBCommon.roundIt(arInvoicePaymentSchedule.getArInvoice().getInvPenaltyPaid() - PENALTY_PAID, this.getGlFcPrecisionUnit(companyCode)));
                    }

                    // increase customer balance

                    double RCT_AMNT = this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmount(), companyCode);

                    this.post(arReceipt.getRctDate(), RCT_AMNT + RCT_CRDTS + RCT_AI_APPLD_DPSTS, arReceipt.getArCustomer(), companyCode);

                } else if (arReceipt.getRctType().equals("MISC") && !arReceipt.getArInvoiceLineItems().isEmpty()) {

                    Iterator c = arReceipt.getArInvoiceLineItems().iterator();

                    while (c.hasNext()) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) c.next();

                        String II_NM = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName();
                        String LOC_NM = arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName();

                        double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                        LocalInvCosting invCosting = null;

                        try {

                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arReceipt.getRctDate(), II_NM, LOC_NM, branchCode, companyCode);

                        } catch (FinderException ex) {

                        }

                        double COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                        if (invCosting == null) {

                            this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), -QTY_SLD, -QTY_SLD * COST, QTY_SLD, QTY_SLD * COST, 0d, null, branchCode, companyCode);

                        } else {

                            if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Average")) {

                                double avgCost = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                                this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), -QTY_SLD, -COST * QTY_SLD, invCosting.getCstRemainingQuantity() + QTY_SLD, invCosting.getCstRemainingValue() + (COST * QTY_SLD), 0d, username, branchCode, companyCode);

                            } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("FIFO")) {

                                double fifoCost = this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), QTY_SLD, arInvoiceLineItem.getIliUnitPrice(), true, branchCode, companyCode);

                                this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), -QTY_SLD, -fifoCost * QTY_SLD, invCosting.getCstRemainingQuantity() + QTY_SLD, invCosting.getCstRemainingValue() + (fifoCost * QTY_SLD), 0d, username, branchCode, companyCode);

                            } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Standard")) {

                                double standardCost = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();

                                this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), -QTY_SLD, -standardCost * QTY_SLD, invCosting.getCstRemainingQuantity() + QTY_SLD, invCosting.getCstRemainingValue() + (standardCost * QTY_SLD), 0d, username, branchCode, companyCode);
                            }
                        }
                    }
                }

                // decrease bank balance cash

                if (arReceipt.getRctAmountCash() > 0) {

                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccount().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCash(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCash());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCash()=" + arReceipt.getRctAmountCash());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (-arReceipt.getRctAmountCash()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        Iterator i = adBankAccountBalances.iterator();

                        while (i.hasNext()) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) i.next();

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCash());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // decrease bank balance CARD 1
                if (arReceipt.getRctAmountCard1() > 0) {

                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccountCard1().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard1().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard1(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard1());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCard1()=" + arReceipt.getRctAmountCard1());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (-arReceipt.getRctAmountCard1()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard1().getBaCode(), "BOOK", companyCode);

                        Iterator i = adBankAccountBalances.iterator();

                        while (i.hasNext()) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) i.next();

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard1());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // decrease bank balance CARD 2
                if (arReceipt.getRctAmountCard2() > 0) {
                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccountCard2().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard2().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard2(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard2());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCard2()=" + arReceipt.getRctAmountCard2());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (-arReceipt.getRctAmountCard2()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard2().getBaCode(), "BOOK", companyCode);

                        Iterator i = adBankAccountBalances.iterator();

                        while (i.hasNext()) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) i.next();

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard2());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // decrease bank balance CARD 3
                if (arReceipt.getRctAmountCard3() > 0) {
                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccountCard3().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard3().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard3(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard3());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCard3()=" + arReceipt.getRctAmountCard3());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (-arReceipt.getRctAmountCard3()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard3().getBaCode(), "BOOK", companyCode);

                        Iterator i = adBankAccountBalances.iterator();

                        while (i.hasNext()) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) i.next();

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard3());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }
                // decrease bank balance CHEQUE
                if (arReceipt.getRctAmountCheque() > 0) {
                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccount().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCheque(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCheque());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCheque()=" + arReceipt.getRctAmountCheque());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (-arReceipt.getRctAmountCheque()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        Iterator i = adBankAccountBalances.iterator();

                        while (i.hasNext()) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) i.next();

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCheque());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // set receipt post status

                arReceipt.setRctPosted(EJBCommon.TRUE);
                arReceipt.setRctPostedBy(username);
                arReceipt.setRctDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
            }

            // post to gl if necessary

            if (adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                // validate if date has no period and period is closed

                LocalGlSetOfBook glJournalSetOfBook = null;

                try {

                    glJournalSetOfBook = glSetOfBookHome.findByDate(arReceipt.getRctDate(), companyCode);

                } catch (FinderException ex) {

                    throw new GlJREffectiveDateNoPeriodExistException();
                }

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), arReceipt.getRctDate(), companyCode);

                if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P') {

                    throw new GlJREffectiveDatePeriodClosedException();
                }

                // check if invoice is balance if not check suspense posting

                LocalGlJournalLine glOffsetJournalLine = null;

                Collection arDistributionRecords = null;

                if (arReceipt.getRctVoid() == EJBCommon.FALSE) {

                    arDistributionRecords = arDistributionRecordHome.findImportableDrByDrReversedAndRctCode(EJBCommon.FALSE, arReceipt.getRctCode(), companyCode);

                } else {

                    arDistributionRecords = arDistributionRecordHome.findImportableDrByDrReversedAndRctCode(EJBCommon.TRUE, arReceipt.getRctCode(), companyCode);
                }

                Iterator j = arDistributionRecords.iterator();

                double TOTAL_DEBIT = 0d;
                double TOTAL_CREDIT = 0d;

                while (j.hasNext()) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) j.next();

                    double DR_AMNT = 0d;

                    if (arDistributionRecord.getArAppliedInvoice() != null) {

                        LocalArInvoice arInvoice = arDistributionRecord.getArAppliedInvoice().getArInvoicePaymentSchedule().getArInvoice();

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), companyCode);

                    } else {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arDistributionRecord.getDrAmount(), companyCode);
                    }

                    if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

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

                        glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("ACCOUNTS RECEIVABLES", "SALES RECEIPTS", companyCode);

                    } catch (FinderException ex) {

                        throw new GlobalJournalNotBalanceException();
                    }

                    if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                        glOffsetJournalLine = glJournalLineHome.create((short) (arDistributionRecords.size() + 1), EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);

                    } else {

                        glOffsetJournalLine = glJournalLineHome.create((short) (arDistributionRecords.size() + 1), EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
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

                    if (adPreference.getPrfEnableArReceiptBatch() == EJBCommon.TRUE) {

                        glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " " + arReceipt.getArReceiptBatch().getRbName(), branchCode, companyCode);

                    } else {

                        glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " SALES RECEIPTS", branchCode, companyCode);
                    }

                } catch (FinderException ex) {
                }

                if (adPreference.getPrfEnableGlJournalBatch() == EJBCommon.TRUE && glJournalBatch == null) {

                    if (adPreference.getPrfEnableArReceiptBatch() == EJBCommon.TRUE) {

                        glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " " + arReceipt.getArReceiptBatch().getRbName(), "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), username, branchCode, companyCode);

                    } else {

                        glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " SALES RECEIPTS", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), username, branchCode, companyCode);
                    }
                }

                // create journal entry

                LocalGlJournal glJournal = glJournalHome.create(arReceipt.getRctReferenceNumber(), arReceipt.getRctDescription(), arReceipt.getRctDate(), 0.0d, null, arReceipt.getRctNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, username, new Date(), username, new Date(), null, null, username, EJBCommon.getGcCurrentDateWoTime().getTime(), arReceipt.getArCustomer().getCstTin(), arReceipt.getArCustomer().getCstName(), EJBCommon.FALSE, null, branchCode, companyCode);

                LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("ACCOUNTS RECEIVABLES", companyCode);
                glJournal.setGlJournalSource(glJournalSource);

                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

                LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("SALES RECEIPTS", companyCode);
                glJournal.setGlJournalCategory(glJournalCategory);

                if (glJournalBatch != null) {

                    glJournal.setGlJournalBatch(glJournalBatch);
                }

                // create journal lines

                j = arDistributionRecords.iterator();
                boolean firstFlag = true;

                while (j.hasNext()) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) j.next();

                    double DR_AMNT = 0d;
                    LocalArInvoice arInvoice = null;

                    if (arDistributionRecord.getArAppliedInvoice() != null) {

                        arInvoice = arDistributionRecord.getArAppliedInvoice().getArInvoicePaymentSchedule().getArInvoice();

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), companyCode);

                    } else {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arDistributionRecord.getDrAmount(), companyCode);
                    }

                    LocalGlJournalLine glJournalLine = glJournalLineHome.create(arDistributionRecord.getDrLine(), arDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

                    arDistributionRecord.getGlChartOfAccount().addGlJournalLine(glJournalLine);

                    glJournal.addGlJournalLine(glJournalLine);

                    arDistributionRecord.setDrImported(EJBCommon.TRUE);

                    // for FOREX revaluation

                    int FC_CODE = arDistributionRecord.getArAppliedInvoice() != null ? arInvoice.getGlFunctionalCurrency().getFcCode().intValue() : arReceipt.getGlFunctionalCurrency().getFcCode().intValue();

                    if ((FC_CODE != adCompany.getGlFunctionalCurrency().getFcCode().intValue()) && glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null && (FC_CODE == glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode().intValue())) {

                        double CONVERSION_RATE = arDistributionRecord.getArAppliedInvoice() != null ? arInvoice.getInvConversionRate() : arReceipt.getRctConversionRate();

                        Date DATE = arDistributionRecord.getArAppliedInvoice() != null ? arInvoice.getInvConversionDate() : arReceipt.getRctConversionDate();

                        if (DATE != null && (CONVERSION_RATE == 0 || CONVERSION_RATE == 1)) {

                            CONVERSION_RATE = this.getFrRateByFrNameAndFrDate(glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), companyCode);

                        } else if (CONVERSION_RATE == 0) {

                            CONVERSION_RATE = 1;
                        }

                        Collection glForexLedgers = null;

                        DATE = arDistributionRecord.getArAppliedInvoice() != null ? arInvoice.getInvDate() : arReceipt.getRctDate();

                        try {

                            glForexLedgers = glForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(DATE, glJournalLine.getGlChartOfAccount().getCoaCode(), companyCode);

                        } catch (FinderException ex) {

                        }

                        LocalGlForexLedger glForexLedger = (glForexLedgers.isEmpty() || glForexLedgers == null) ? null : (LocalGlForexLedger) glForexLedgers.iterator().next();

                        int FRL_LN = (glForexLedger != null && glForexLedger.getFrlDate().compareTo(DATE) == 0) ? glForexLedger.getFrlLine().intValue() + 1 : 1;

                        // compute balance
                        double COA_FRX_BLNC = glForexLedger == null ? 0 : glForexLedger.getFrlBalance();
                        double FRL_AMNT = arDistributionRecord.getDrAmount();

                        if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET")) {
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                        } else {
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);
                        }

                        COA_FRX_BLNC = COA_FRX_BLNC + FRL_AMNT;

                        double FRX_GN_LSS = 0d;

                        if (glOffsetJournalLine != null && firstFlag) {

                            if (glOffsetJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET")) {
                                FRX_GN_LSS = (glOffsetJournalLine.getJlDebit() == EJBCommon.TRUE ? glOffsetJournalLine.getJlAmount() : (-1 * glOffsetJournalLine.getJlAmount()));
                            } else {
                                FRX_GN_LSS = (glOffsetJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * glOffsetJournalLine.getJlAmount()) : glOffsetJournalLine.getJlAmount());
                            }

                            firstFlag = false;
                        }

                        glForexLedger = glForexLedgerHome.create(DATE, FRL_LN, "OR", FRL_AMNT, CONVERSION_RATE, COA_FRX_BLNC, FRX_GN_LSS, companyCode);

                        glJournalLine.getGlChartOfAccount().addGlForexLedger(glForexLedger);

                        // propagate balances
                        try {

                            glForexLedgers = glForexLedgerHome.findByGreaterThanFrlDateAndCoaCode(glForexLedger.getFrlDate(), glForexLedger.getGlChartOfAccount().getCoaCode(), glForexLedger.getFrlAdCompany());

                        } catch (FinderException ex) {

                        }

                        Iterator itrFrl = glForexLedgers.iterator();

                        while (itrFrl.hasNext()) {

                            glForexLedger = (LocalGlForexLedger) itrFrl.next();
                            FRL_AMNT = arDistributionRecord.getDrAmount();

                            if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET")) {
                                FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                            } else {
                                FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);
                            }

                            glForexLedger.setFrlBalance(glForexLedger.getFrlBalance() + FRL_AMNT);
                        }
                    }
                }

                if (glOffsetJournalLine != null) {

                    glJournal.addGlJournalLine(glOffsetJournalLine);
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

                            if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) {
                                break;
                            }
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

        } catch (GlobalBranchAccountNumberInvalidException ex) {

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

    private double getInvFifoCost(Date CST_DT, Integer IL_CODE, double CST_QTY, double CST_COST, boolean isAdjustFifo, Integer branchCode, Integer companyCode) {

        try {

            Collection invFifoCostings = invCostingHome.findFifoRemainingQuantityByLessThanOrEqualCstDateAndIlCodeAndBrCode(CST_DT, IL_CODE, branchCode, companyCode);

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

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void post(Date INV_DT, double INV_AMNT, LocalArCustomer arCustomer, Integer companyCode) {

        Debug.print("ArApprovalControllerBean post");
        try {

            // find customer balance before or equal invoice date

            Collection arCustomerBalances = arCustomerBalanceHome.findByBeforeOrEqualInvDateAndCstCustomerCode(INV_DT, arCustomer.getCstCustomerCode(), companyCode);

            if (!arCustomerBalances.isEmpty()) {

                // get last invoice

                ArrayList arCustomerBalanceList = new ArrayList(arCustomerBalances);

                LocalArCustomerBalance arCustomerBalance = (LocalArCustomerBalance) arCustomerBalanceList.get(arCustomerBalanceList.size() - 1);

                if (arCustomerBalance.getCbDate().before(INV_DT)) {

                    // create new balance

                    LocalArCustomerBalance apNewCustomerBalance = arCustomerBalanceHome.create(INV_DT, arCustomerBalance.getCbBalance() + INV_AMNT, companyCode);

                    arCustomer.addArCustomerBalance(apNewCustomerBalance);

                } else { // equals to invoice date

                    arCustomerBalance.setCbBalance(arCustomerBalance.getCbBalance() + INV_AMNT);
                }

            } else {

                // create new balance

                LocalArCustomerBalance apNewCustomerBalance = arCustomerBalanceHome.create(INV_DT, INV_AMNT, companyCode);

                arCustomer.addArCustomerBalance(apNewCustomerBalance);
            }

            // propagate to subsequent balances if necessary

            arCustomerBalances = arCustomerBalanceHome.findByAfterInvDateAndCstCustomerCode(INV_DT, arCustomer.getCstCustomerCode(), companyCode);

            Iterator i = arCustomerBalances.iterator();

            while (i.hasNext()) {

                LocalArCustomerBalance arCustomerBalance = (LocalArCustomerBalance) i.next();

                arCustomerBalance.setCbBalance(arCustomerBalance.getCbBalance() + INV_AMNT);
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer companyCode) {

        Debug.print("ArApprovalControllerBean convertForeignToFunctionalCurrency");
        LocalAdCompany adCompany = null;
        // get company and extended precision
        try {

            adCompany = adCompanyHome.findByPrimaryKey(companyCode);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        // Convert to functional currency if necessary
        if (CONVERSION_RATE != 1 && CONVERSION_RATE != 0) {

            AMOUNT = AMOUNT / CONVERSION_RATE;
        }
        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double JL_AMNT, Integer companyCode) {

        Debug.print("ArApprovalControllerBean postToGl");

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

    private void postToInv(LocalArInvoiceLineItem arInvoiceLineItem, Date CST_DT, double CST_QTY_SLD, double CST_CST_OF_SLS, double CST_RMNNG_QTY, double CST_RMNNG_VL, double CST_VRNC_VL, String username, Integer branchCode, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ArApprovalControllerBean postToInv");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalInvItemLocation invItemLocation = arInvoiceLineItem.getInvItemLocation();
            int CST_LN_NMBR = 0;

            CST_QTY_SLD = EJBCommon.roundIt(CST_QTY_SLD, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_CST_OF_SLS = EJBCommon.roundIt(CST_CST_OF_SLS, adCompany.getGlFunctionalCurrency().getFcPrecision());
            CST_RMNNG_QTY = EJBCommon.roundIt(CST_RMNNG_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_RMNNG_VL = EJBCommon.roundIt(CST_RMNNG_VL, adCompany.getGlFunctionalCurrency().getFcPrecision());

            if (CST_QTY_SLD > 0) {

                invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - CST_QTY_SLD);
            }

            // create costing

            try {

                // generate line number

                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            } catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                // void subsequent cost variance adjustments
                Collection invAdjustmentLines = invAdjustmentLineHome.findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(CST_DT, invItemLocation.getIlCode(), branchCode, companyCode);
                Iterator i = invAdjustmentLines.iterator();

                while (i.hasNext()) {

                    LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();
                    this.voidInvAdjustment(invAdjustmentLine.getInvAdjustment(), branchCode, companyCode);
                }
            }

            String prevExpiryDates = "";
            String miscListPrpgt = "";
            double qtyPrpgt = 0;
            double qtyPrpgt2 = 0;
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
            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d, 0d, 0d, CST_QTY_SLD, CST_CST_OF_SLS, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, 0d, branchCode, companyCode);
            invItemLocation.addInvCosting(invCosting);
            invCosting.setArInvoiceLineItem(arInvoiceLineItem);

            invCosting.setCstQuantity(CST_QTY_SLD * -1);
            invCosting.setCstCost(CST_CST_OF_SLS * -1);

            // Get Latest Expiry Dates
            if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiTraceMisc() != 0) {
                if (prevExpiryDates != null && prevExpiryDates != "" && prevExpiryDates.length() != 0) {
                    if (arInvoiceLineItem.getArInvoice().getInvCreditMemo() == EJBCommon.FALSE || arInvoiceLineItem.getArReceipt().getRctType().equals("MISC")) {

                        if (prevExpiryDates != null && prevExpiryDates != "" && prevExpiryDates.length() != 0) {
                            if (arInvoiceLineItem.getIliMisc() != null && arInvoiceLineItem.getIliMisc() != "" && arInvoiceLineItem.getIliMisc().length() != 0) {
                                int qty2Prpgt = Integer.parseInt(this.getQuantityExpiryDates(arInvoiceLineItem.getIliMisc()));
                                ArrayList miscList = this.expiryDates(arInvoiceLineItem.getIliMisc(), qty2Prpgt);
                                String propagateMiscPrpgt = "";
                                String ret = "";

                                Debug.print("qty2Prpgt: " + qty2Prpgt);
                                Iterator mi = miscList.iterator();
                                propagateMiscPrpgt = prevExpiryDates;
                                ret = propagateMiscPrpgt;
                                Debug.print("ret: " + ret);
                                String Checker = "";
                                while (mi.hasNext()) {
                                    String miscStr = (String) mi.next();

                                    ArrayList miscList2 = this.expiryDates("$" + ret, qtyPrpgt);
                                    Iterator m2 = miscList2.iterator();
                                    ret = "";
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
                                                    ret = ret + miscStr2;
                                                    ret2 = "false";
                                                }
                                            }
                                        }
                                    }
                                    ret = ret + "$";
                                    qtyPrpgt = qtyPrpgt - 1;
                                    if (Checker != "true") {
                                        throw new GlobalExpiryDateNotFoundException();
                                    }
                                }

                                propagateMiscPrpgt = ret;
                                Debug.print("propagateMiscPrpgt: " + propagateMiscPrpgt);

                                invCosting.setCstExpiryDate(propagateMiscPrpgt);
                            } else {
                                invCosting.setCstExpiryDate(prevExpiryDates);
                            }
                        }
                    } else {
                        if (arInvoiceLineItem.getIliMisc() != null && arInvoiceLineItem.getIliMisc() != "" && arInvoiceLineItem.getIliMisc().length() != 0) {
                            int qty2Prpgt = Integer.parseInt(this.getQuantityExpiryDates(arInvoiceLineItem.getIliMisc()));
                            String miscList2Prpgt = this.propagateExpiryDates(arInvoiceLineItem.getIliMisc(), qty2Prpgt);
                            prevExpiryDates = prevExpiryDates.substring(1);
                            String propagateMiscPrpgt = miscList2Prpgt + prevExpiryDates;

                            invCosting.setCstExpiryDate(propagateMiscPrpgt);
                        } else {
                            invCosting.setCstExpiryDate(prevExpiryDates);
                        }
                    }
                } else {
                    if (arInvoiceLineItem.getIliMisc() != null && arInvoiceLineItem.getIliMisc() != "" && arInvoiceLineItem.getIliMisc().length() != 0) {
                        int initialQty = Integer.parseInt(this.getQuantityExpiryDates(arInvoiceLineItem.getIliMisc()));
                        String initialPrpgt = this.propagateExpiryDates(arInvoiceLineItem.getIliMisc(), initialQty);

                        invCosting.setCstExpiryDate(initialPrpgt);
                    } else {
                        invCosting.setCstExpiryDate(prevExpiryDates);
                    }
                }
            }

            // if cost variance is not 0, generate cost variance for the transaction
            if (CST_VRNC_VL != 0 && adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {

                if (arInvoiceLineItem.getArInvoice() != null) {
                    this.generateCostVariance(invCosting.getInvItemLocation(), CST_VRNC_VL, "ARCM" + arInvoiceLineItem.getArInvoice().getInvNumber(), arInvoiceLineItem.getArInvoice().getInvDescription(), arInvoiceLineItem.getArInvoice().getInvDate(), username, branchCode, companyCode);
                } else if (arInvoiceLineItem.getArReceipt() != null) {
                    this.generateCostVariance(invCosting.getInvItemLocation(), CST_VRNC_VL, "ARMR" + arInvoiceLineItem.getArReceipt().getRctNumber(), arInvoiceLineItem.getArReceipt().getRctDescription(), arInvoiceLineItem.getArReceipt().getRctDate(), username, branchCode, companyCode);
                }
            }

            // propagate balance if necessary

            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);

            Iterator i = invCostings.iterator();

            String miscList = "";
            ArrayList miscList2 = null;

            String propagateMisc = "";
            String ret = "";

            Debug.print("CST_ST_QTY: " + CST_QTY_SLD);

            while (i.hasNext()) {
                String Checker = "";
                String Checker2 = "";

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) i.next();

                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() - CST_QTY_SLD);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() - CST_CST_OF_SLS);

                if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiTraceMisc() != 0) {
                    if (arInvoiceLineItem.getIliMisc() != null && arInvoiceLineItem.getIliMisc() != "" && arInvoiceLineItem.getIliMisc().length() != 0) {
                        Debug.print("BAGO ANG MALI: " + arInvoiceLineItem.getIliMisc());

                        double qty = Double.parseDouble(this.getQuantityExpiryDates(arInvoiceLineItem.getIliMisc()));
                        // double qty2 = this.checkExpiryDates2(arInvoiceLineItem.getIliMisc());
                        miscList = this.propagateExpiryDates(arInvoiceLineItem.getIliMisc(), qty, "False");
                        miscList2 = this.expiryDates(arInvoiceLineItem.getIliMisc(), qty);

                        Debug.print("invAdjustmentLine.getAlMisc(): " + arInvoiceLineItem.getIliMisc());
                        Debug.print("getAlAdjustQuantity(): " + arInvoiceLineItem.getIliQuantity());

                        if (arInvoiceLineItem.getIliQuantity() < 0) {
                            Iterator mi = miscList2.iterator();

                            propagateMisc = invPropagatedCosting.getCstExpiryDate();
                            ret = invPropagatedCosting.getCstExpiryDate();
                            while (mi.hasNext()) {
                                String miscStr = (String) mi.next();

                                Integer qTest = checkExpiryDates(ret + "fin$");
                                ArrayList miscList3 = this.expiryDates("$" + ret, Double.parseDouble(qTest.toString()));

                                // ArrayList miscList3 = this.expiryDates("$" + ret, qtyPrpgt);
                                Debug.print("ret: " + ret);
                                Iterator m2 = miscList3.iterator();
                                ret = "";
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
                                                ret = ret + miscStr2;
                                                ret2 = "false";
                                            }
                                        }
                                    }
                                }
                                if (Checker2 != "true") {
                                    throw new GlobalExpiryDateNotFoundException(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                                } else {
                                    Debug.print("TAE");
                                }

                                ret = ret + "$";
                                qtyPrpgt = qtyPrpgt - 1;
                            }
                        }
                    }

                    if (prevExpiryDates != null && prevExpiryDates != "" && prevExpiryDates.length() != 0) {
                        if (arInvoiceLineItem.getArInvoice().getInvCreditMemo() == EJBCommon.FALSE || arInvoiceLineItem.getArReceipt().getRctType().equals("MISC")) {

                            if (prevExpiryDates != null && prevExpiryDates != "" && prevExpiryDates.length() != 0) {
                                if (arInvoiceLineItem.getIliMisc() != null && arInvoiceLineItem.getIliMisc() != "" && arInvoiceLineItem.getIliMisc().length() != 0) {
                                    if (CST_QTY_SLD < 0) {
                                        propagateMisc = miscList + invPropagatedCosting.getCstExpiryDate().substring(1);
                                        Debug.print("propagateMiscPrpgt : " + propagateMisc);

                                    } else {
                                        Iterator mi = miscList2.iterator();

                                        propagateMisc = prevExpiryDates;
                                        ret = propagateMisc;
                                        while (mi.hasNext()) {
                                            String miscStr = (String) mi.next();
                                            Debug.print("ret123: " + ret);
                                            Debug.print("qtyPrpgt123: " + qtyPrpgt);
                                            Debug.print("qtyPrpgt2: " + qtyPrpgt2);
                                            if (qtyPrpgt <= 0) {
                                                qtyPrpgt = qtyPrpgt2;
                                            }

                                            Integer qTest = checkExpiryDates(ret + "fin$");
                                            ArrayList miscList3 = this.expiryDates("$" + ret, Double.parseDouble(qTest.toString()));
                                            Iterator m2 = miscList3.iterator();
                                            ret = "";
                                            String ret2 = "false";
                                            int a = 0;
                                            while (m2.hasNext()) {
                                                String miscStr2 = (String) m2.next();

                                                if (ret2 == "1st") {
                                                    ret2 = "false";
                                                }

                                                Debug.print("miscStr2: " + miscStr2);
                                                Debug.print("miscStr: " + miscStr);
                                                if (miscStr2.trim().equals(miscStr.trim())) {
                                                    if (a == 0) {
                                                        a = 1;
                                                        ret2 = "1st";
                                                        Checker = "true";
                                                    } else {
                                                        a = a + 1;
                                                        ret2 = "true";
                                                    }
                                                }

                                                if (!miscStr2.trim().equals(miscStr.trim()) || a > 1) {
                                                    if ((ret2 != "1st") || (ret2 == "false") || (ret2 == "true")) {
                                                        if (miscStr2 != "") {
                                                            miscStr2 = "$" + miscStr2.trim();
                                                            ret = ret + miscStr2;
                                                            ret2 = "false";
                                                        }
                                                    }
                                                }
                                            }
                                            ret = ret + "$";
                                            qtyPrpgt = qtyPrpgt - 1;
                                        }
                                        propagateMisc = ret;
                                        Debug.print("propagateMiscPrpgt: " + propagateMisc);

                                        if (Checker == "true") {
                                            // invPropagatedCosting.setCstExpiryDate(propagateMisc);
                                            Debug.print("Yes");
                                        } else {
                                            Debug.print("ex1");

                                        }
                                    }

                                    invPropagatedCosting.setCstExpiryDate(propagateMisc);
                                } else {
                                    invPropagatedCosting.setCstExpiryDate(prevExpiryDates);
                                }
                            }
                        } else {
                            if (arInvoiceLineItem.getIliMisc() != null && arInvoiceLineItem.getIliMisc() != "" && arInvoiceLineItem.getIliMisc().length() != 0) {
                                int qty2Prpgt = Integer.parseInt(this.getQuantityExpiryDates(arInvoiceLineItem.getIliMisc()));
                                String miscList2Prpgt = this.propagateExpiryDates(arInvoiceLineItem.getIliMisc(), qty2Prpgt);
                                prevExpiryDates = prevExpiryDates.substring(1);
                                String propagateMiscPrpgt = miscList2Prpgt + prevExpiryDates;

                                invPropagatedCosting.setCstExpiryDate(propagateMiscPrpgt);
                            } else {
                                invPropagatedCosting.setCstExpiryDate(prevExpiryDates);
                            }
                        }
                    } else {
                        if (arInvoiceLineItem.getIliMisc() != null && arInvoiceLineItem.getIliMisc() != "" && arInvoiceLineItem.getIliMisc().length() != 0) {
                            int initialQty = Integer.parseInt(this.getQuantityExpiryDates(arInvoiceLineItem.getIliMisc()));
                            String initialPrpgt = this.propagateExpiryDates(arInvoiceLineItem.getIliMisc(), initialQty);

                            invPropagatedCosting.setCstExpiryDate(initialPrpgt);
                        } else {
                            invPropagatedCosting.setCstExpiryDate(prevExpiryDates);
                        }
                    }
                }
            }

            if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                // regenerate cost variance
                this.regenerateCostVariance(invCostings, invCosting, branchCode, companyCode);
            }

        } catch (AdPRFCoaGlVarianceAccountNotFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

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
        String miscList = "";
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
                            miscList = miscList + "$" + g;
                            numberExpry++;
                        }
                    } else {
                        miscList2 = "Error";
                    }

                } else {
                    miscList2 = "Error";
                }
            }
        } catch (Exception e) {

        }

        if (miscList2 == "") {
            miscList = miscList + "$";
        } else {
            miscList = miscList2;
        }

        return (numberExpry);
    }

    public String propagateExpiryDates(String misc, double qty, String reverse) throws Exception {
        // ActionErrors errors = new ActionErrors();
        Debug.print("ApReceivingItemControllerBean getExpiryDates");
        Debug.print("misc: " + misc);
        String miscList = "";
        try {
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

            for (int x = 0; x < qty; x++) {

                // Date
                start = nextIndex + 1;
                nextIndex = misc.indexOf(separator, start);
                length = nextIndex - start;
                String g = misc.substring(start, start + length);
                Debug.print("g: " + g);
                Debug.print("g length: " + g.length());
                if (g.length() != 0) {
                    miscList = miscList + "$" + g;
                    Debug.print("miscList G: " + miscList);
                }
            }

            miscList = miscList + "$";
        } catch (Exception e) {
            miscList = "";
        }

        Debug.print("miscList :" + miscList);
        return (miscList);
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
            Debug.print("x" + x);
            String checker = misc.substring(start, start + length);
            Debug.print("checker" + checker);
            if (checker.length() != 0 || checker != "null") {
                miscList.add(checker);
            } else {
                miscList.add("null");
            }
        }

        Debug.print("miscList :" + miscList);
        return miscList;
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

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, double QTY_SLD, Integer companyCode) {

        Debug.print("ArApprovalControllerBean convertByUomFromAndItemAndQuantity");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), companyCode);

            return EJBCommon.roundIt(QTY_SLD * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), adPreference.getPrfInvQuantityPrecisionUnit());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void regenerateInventoryDr(LocalArInvoice arInvoice, Integer branchCode, Integer companyCode) throws GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException {

        Debug.print("ArApprovalControllerBean regenerateInventoryDr");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            // regenerate inventory distribution records

            Collection arDistributionRecords = arDistributionRecordHome.findImportableDrByInvCode(arInvoice.getInvCode(), companyCode);

            Iterator i = arDistributionRecords.iterator();

            while (i.hasNext()) {

                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

                if (arDistributionRecord.getDrClass().equals("COGS") || arDistributionRecord.getDrClass().equals("INVENTORY")) {

                    i.remove();
                    // arDistributionRecord.entityRemove();
                    em.remove(arDistributionRecord);
                }
            }

            Collection arInvoiceLineItems = arInvoice.getArInvoiceLineItems();

            if (arInvoiceLineItems != null && !arInvoiceLineItems.isEmpty()) {

                i = arInvoiceLineItems.iterator();

                while (i.hasNext()) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) i.next();
                    LocalInvItemLocation invItemLocation = arInvoiceLineItem.getInvItemLocation();

                    // start date validation
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }

                    // add cost of sales distribution and inventory
                    double COST = 0d;

                    try {

                        LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);

                        if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Average")) {
                            COST = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                        } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("FIFO")) {
                            COST = Math.abs(this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), arInvoiceLineItem.getIliQuantity(), arInvoiceLineItem.getIliUnitPrice(), false, branchCode, companyCode));
                        } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Standard")) {
                            COST = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                        }

                    } catch (FinderException ex) {

                        COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();
                    }

                    double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {

                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arInvoiceLineItem.getInvItemLocation().getIlCode(), branchCode, companyCode);

                    } catch (FinderException ex) {

                    }

                    if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {

                        if (adBranchItemLocation != null) {

                            // Use AdBranchItemLocation Cost of Sales & Inventory Account
                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), "COGS", EJBCommon.TRUE, COST * QTY_SLD, adBranchItemLocation.getBilCoaGlCostOfSalesAccount(), arInvoice, branchCode, companyCode);

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD, adBranchItemLocation.getBilCoaGlInventoryAccount(), arInvoice, branchCode, companyCode);

                        } else {

                            // Use default accounts
                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), "COGS", EJBCommon.TRUE, COST * QTY_SLD, arInvoiceLineItem.getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arInvoice, branchCode, companyCode);

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD, arInvoiceLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(), arInvoice, branchCode, companyCode);
                        }

                    } else {

                        if (adBranchItemLocation != null) {

                            // Use AdBranchItemLocation Accounts
                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), "COGS", EJBCommon.FALSE, COST * QTY_SLD, adBranchItemLocation.getBilCoaGlCostOfSalesAccount(), arInvoice, branchCode, companyCode);

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), "INVENTORY", EJBCommon.TRUE, COST * QTY_SLD, adBranchItemLocation.getBilCoaGlInventoryAccount(), arInvoice, branchCode, companyCode);

                        } else {

                            // Use default Accounts
                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), "COGS", EJBCommon.FALSE, COST * QTY_SLD, arInvoiceLineItem.getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arInvoice, branchCode, companyCode);

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), "INVENTORY", EJBCommon.TRUE, COST * QTY_SLD, arInvoiceLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(), arInvoice, branchCode, companyCode);
                        }
                    }
                }
            }

        } catch (GlobalInventoryDateException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (GlobalBranchAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void addArDrIliEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE, LocalArInvoice arInvoice, Integer branchCode, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArApprovalControllerBean addArDrIliEntry");
        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, branchCode, companyCode);

            // create distribution record

            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, EJBCommon.FALSE, companyCode);

            arInvoice.addArDistributionRecord(arDistributionRecord);
            glChartOfAccount.addArDistributionRecord(arDistributionRecord);

        } catch (FinderException ex) {

            throw new GlobalBranchAccountNumberInvalidException(ex.getMessage());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void regenerateInventoryDr(LocalArReceipt arReceipt, Integer branchCode, Integer companyCode) throws GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException {

        Debug.print("ArApprovalControllerBean regenerateInventoryDr");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            // regenerate inventory distribution records

            Collection arDistributionRecords = null;

            if (arReceipt.getRctVoid() == EJBCommon.FALSE) {

                arDistributionRecords = arDistributionRecordHome.findImportableDrByDrReversedAndRctCode(EJBCommon.FALSE, arReceipt.getRctCode(), companyCode);

            } else {

                arDistributionRecords = arDistributionRecordHome.findImportableDrByDrReversedAndRctCode(EJBCommon.TRUE, arReceipt.getRctCode(), companyCode);
            }

            Iterator i = arDistributionRecords.iterator();

            while (i.hasNext()) {

                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

                if (arDistributionRecord.getDrClass().equals("COGS") || arDistributionRecord.getDrClass().equals("INVENTORY")) {

                    i.remove();
                    // arDistributionRecord.entityRemove();
                    em.remove(arDistributionRecord);
                }
            }

            Collection arInvoiceLineItems = arReceipt.getArInvoiceLineItems();

            if (arInvoiceLineItems != null && !arInvoiceLineItems.isEmpty()) {

                i = arInvoiceLineItems.iterator();

                while (i.hasNext()) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) i.next();
                    LocalInvItemLocation invItemLocation = arInvoiceLineItem.getInvItemLocation();
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(arReceipt.getRctDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }

                    // add cost of sales distribution and inventory

                    double COST = 0d;

                    try {

                        LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arReceipt.getRctDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);

                        if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Average")) {
                            COST = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                        } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("FIFO")) {
                            COST = Math.abs(this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), arInvoiceLineItem.getIliQuantity(), arInvoiceLineItem.getIliUnitPrice(), false, branchCode, companyCode));
                        } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Standard")) {
                            COST = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                        }

                    } catch (FinderException ex) {

                        COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();
                    }

                    double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {

                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arInvoiceLineItem.getInvItemLocation().getIlCode(), branchCode, companyCode);

                    } catch (FinderException ex) {

                    }

                    if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiNonInventoriable() == EJBCommon.FALSE) {

                        if (adBranchItemLocation != null) {

                            // Use AdBranchItemLocation Accounts
                            this.addArDrIliEntry(arReceipt.getArDrNextLine(), "COGS", EJBCommon.TRUE, COST * QTY_SLD, adBranchItemLocation.getBilCoaGlCostOfSalesAccount(), arReceipt, branchCode, companyCode);

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(), "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD, adBranchItemLocation.getBilCoaGlInventoryAccount(), arReceipt, branchCode, companyCode);

                        } else {

                            // Use default Accounts
                            this.addArDrIliEntry(arReceipt.getArDrNextLine(), "COGS", EJBCommon.TRUE, COST * QTY_SLD, arInvoiceLineItem.getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arReceipt, branchCode, companyCode);

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(), "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD, arInvoiceLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(), arReceipt, branchCode, companyCode);
                        }
                    }
                }
            }

        } catch (GlobalInventoryDateException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (GlobalBranchAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void addArDrIliEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE, LocalArReceipt arReceipt, Integer branchCode, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArApprovalControllerBean addArDrIliEntry");
        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, branchCode, companyCode);

            // create distribution record

            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, EJBCommon.FALSE, companyCode);

            arReceipt.addArDistributionRecord(arDistributionRecord);
            glChartOfAccount.addArDistributionRecord(arDistributionRecord);

        } catch (FinderException ex) {

            throw new GlobalBranchAccountNumberInvalidException(ex.getMessage());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private short getInvGpQuantityPrecisionUnit(Integer companyCode) {

        Debug.print("ArApprovalControllerBean getInvGpQuantityPrecisionUnit");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfInvQuantityPrecisionUnit();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertCostByUom(String II_NM, String UOM_NM, double unitCost, boolean isFromDefault, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean convertCostByUom");
        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NM, companyCode);
            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, UOM_NM, companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, invItem.getInvUnitOfMeasure().getUomName(), companyCode);

            if (isFromDefault) {

                return unitCost * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor();

            } else {

                return unitCost * invUnitOfMeasureConversion.getUmcConversionFactor() / invDefaultUomConversion.getUmcConversionFactor();
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double getFrRateByFrNameAndFrDate(String FC_NM, Date CONVERSION_DATE, Integer companyCode) throws GlobalConversionDateNotExistException {

        Debug.print("ArApprovalControllerBean getFrRateByFrNameAndFrDate");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, companyCode);

            double CONVERSION_RATE = 1;

            // Get functional currency rate

            if (!FC_NM.equals("USD")) {

                LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glFunctionalCurrency.getFcCode(), CONVERSION_DATE, companyCode);

                CONVERSION_RATE = glFunctionalCurrencyRate.getFrXToUsd();
            }

            // Get set of book functional currency rate if necessary

            if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), CONVERSION_DATE, companyCode);

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

    private void voidInvAdjustment(LocalInvAdjustment invAdjustment, Integer branchCode, Integer companyCode) {

        Debug.print("ArApprovalControllerBean voidInvAdjustment");

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

        Debug.print("ArApprovalControllerBean addInvDrEntry");
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

            invAdjustment.addInvDistributionRecord(invDistributionRecord);
            glChartOfAccount.addInvDistributionRecord(invDistributionRecord);

        } catch (GlobalBranchAccountNumberInvalidException ex) {

            throw new GlobalBranchAccountNumberInvalidException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void executeInvAdjPost(Integer ADJ_CODE, String username, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException {

        Debug.print("ArApprovalControllerBean executeInvAdjPost");
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
                glChartOfAccount.addGlJournalLine(glOffsetJournalLine);

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

                        if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) {
                            break;
                        }
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

        Debug.print("ArApprovalControllerBean addInvAlEntry");
        try {

            // create dr entry
            LocalInvAdjustmentLine invAdjustmentLine = null;
            invAdjustmentLine = invAdjustmentLineHome.create(CST_VRNC_VL, null, null, 0, 0, AL_VD, companyCode);

            // map adjustment, unit of measure, item location
            invAdjustment.addInvAdjustmentLine(invAdjustmentLine);
            invItemLocation.getInvItem().getInvUnitOfMeasure().addInvAdjustmentLine(invAdjustmentLine);
            invItemLocation.addInvAdjustmentLine(invAdjustmentLine);

            return invAdjustmentLine;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postInvAdjustmentToInventory(LocalInvAdjustmentLine invAdjustmentLine, Date CST_DT, double CST_ADJST_QTY, double CST_ADJST_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, Integer branchCode, Integer companyCode) {

        Debug.print("ArApprovalControllerBean postInvAdjustmentToInventory");
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
            invItemLocation.addInvCosting(invCosting);
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

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ArApprovalControllerBean ejbCreate");
    }

}