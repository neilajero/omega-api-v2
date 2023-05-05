package com.ejb.txnapi.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.ad.LocalAdAmountLimitHome;
import com.ejb.dao.ad.LocalAdApprovalQueueHome;
import com.ejb.dao.ad.LocalAdApprovalUserHome;
import com.ejb.dao.ar.LocalArCustomerHome;
import com.ejb.dao.ar.LocalArInvoiceHome;
import com.ejb.dao.ar.LocalArReceiptHome;
import com.ejb.dao.ar.LocalArSalesOrderHome;
import com.ejb.entities.ad.LocalAdAmountLimit;
import com.ejb.entities.ad.LocalAdApprovalQueue;
import com.ejb.entities.ad.LocalAdApprovalUser;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ar.*;
import com.ejb.exception.global.GlobalNoApprovalApproverFoundException;
import com.ejb.exception.global.GlobalNoApprovalRequesterFoundException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.mod.ad.AdModApprovalQueueDetails;

import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import java.util.*;

@Stateless(name = "ArApprovalApiControllerEJB")
public class ArApprovalApiControllerBean extends EJBContextClass implements ArApprovalApiController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalAdAmountLimitHome adAmountLimitHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalAdApprovalUserHome adApprovalUserHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalArSalesOrderHome arSalesOrderHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;

    @Override
    public String getApprovalStatus(String userDepartment, String username, String userDesc,
                                    String approvalQueueDocument, Integer approvalQueueDocumentCode,
                                    String approvalQueueDocumentNumber, Date approvalQueueDate,
                                    Integer branchCode, Integer companyCode, String companyShortName)
            throws GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException {

        Debug.print("ArApprovalControllerBean getApprovalStatus");

        String prApprovalStatus;
        Double absTotalAmount = 0d;
        String approvalUserType = EJBCommon.REQUESTER;
        try {
            LocalAdAmountLimit adAmountLimit;
            try {
                adAmountLimit = adAmountLimitHome.findAmountLimitPerApprovalUser(
                        userDepartment, approvalQueueDocument, approvalUserType, username, companyCode, companyShortName);
            }
            catch (FinderException ex) {
                throw new GlobalNoApprovalRequesterFoundException();
            }
            if (!(absTotalAmount <= adAmountLimit.getCalAmountLimit())) {
                prApprovalStatus = EJBCommon.NOT_APPLICABLE;
            } else {
                // for approval, create approval queue
                Collection adAmountLimits = adAmountLimitHome.findAmountLimitsPerDepartment(
                        approvalQueueDocument, userDepartment, adAmountLimit.getCalAmountLimit(), companyCode, companyShortName);
                if (adAmountLimits.isEmpty()) {
                    Collection adApprovalUsers = adApprovalUserHome.findApprovalUsersPerDepartment(
                            userDepartment, EJBCommon.APPROVER, adAmountLimit.getCalCode(), companyCode, companyShortName);
                    if (adApprovalUsers.isEmpty()) {
                        throw new GlobalNoApprovalApproverFoundException();
                    }
                    Iterator j = adApprovalUsers.iterator();
                    while (j.hasNext()) {
                        LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) j.next();
                        LocalAdApprovalQueue adApprovalQueue = adApprovalQueueHome
                                .AqDepartment(userDepartment)
                                .AqLevel(adApprovalUser.getAuLevel())
                                .AqNextLevel(EJBCommon.incrementStringNumber(adApprovalUser.getAuLevel()))
                                .AqDocument(approvalQueueDocument)
                                .AqDocumentCode(approvalQueueDocumentCode)
                                .AqDocumentNumber(approvalQueueDocumentNumber)
                                .AqDate(approvalQueueDate)
                                .AqAndOr(adAmountLimit.getCalAndOr())
                                .AqUserOr(adApprovalUser.getAuOr())
                                .AqRequesterName(userDesc)
                                .AqAdBranch(branchCode)
                                .AqAdCompany(companyCode)
                                .buildApprovalQueue(companyShortName);
                        adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                        if (adApprovalUser.getAuLevel().equals(EJBCommon.LEVEL1APPROVER)) {
                            adApprovalQueue.setAqForApproval(EJBCommon.TRUE);
                            //TODO: Add an email notification mechanism based on the Preference Setup
                            // getPrfAdEnableEmailNotification with Approval Queue User Email Address
                        }
                    }
                } else {
                    boolean isApprovalUsersFound = false;
                    Iterator i = adAmountLimits.iterator();
                    while (i.hasNext()) {
                        LocalAdAmountLimit adNextAmountLimit = (LocalAdAmountLimit) i.next();
                        if (absTotalAmount <= adNextAmountLimit.getCalAmountLimit()) {
                            Collection adApprovalUsers = adApprovalUserHome.findApprovalUsersPerDepartment(
                                    userDepartment, EJBCommon.APPROVER, adAmountLimit.getCalCode(), companyCode, companyShortName);
                            Iterator j = adApprovalUsers.iterator();
                            while (j.hasNext()) {
                                isApprovalUsersFound = true;
                                LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) j.next();
                                LocalAdApprovalQueue adApprovalQueue = adApprovalQueueHome
                                        .AqDepartment(userDepartment)
                                        .AqLevel(adApprovalUser.getAuLevel())
                                        .AqNextLevel(EJBCommon.incrementStringNumber(adApprovalUser.getAuLevel()))
                                        .AqDocument(approvalQueueDocument)
                                        .AqDocumentCode(approvalQueueDocumentCode)
                                        .AqDocumentNumber(approvalQueueDocumentNumber)
                                        .AqDate(approvalQueueDate)
                                        .AqAndOr(adAmountLimit.getCalAndOr())
                                        .AqUserOr(adApprovalUser.getAuOr())
                                        .AqRequesterName(userDesc)
                                        .AqAdBranch(branchCode)
                                        .AqAdCompany(companyCode)
                                        .buildApprovalQueue(companyShortName);
                                if (adApprovalUser.getAuLevel().equals(EJBCommon.LEVEL1APPROVER)) {
                                    adApprovalQueue.setAqForApproval(EJBCommon.TRUE);
                                    //TODO: Add an email notification mechanism based on the Preference Setup
                                    // getPrfAdEnableEmailNotification with Approval Queue User Email Address
                                }
                            }
                            break;
                        } else if (!i.hasNext()) {
                            Collection adApprovalUsers = adApprovalUserHome.findApprovalUsersPerDepartment(
                                    userDepartment, EJBCommon.APPROVER, adNextAmountLimit.getCalCode(), companyCode, companyShortName);
                            Iterator j = adApprovalUsers.iterator();
                            while (j.hasNext()) {
                                isApprovalUsersFound = true;
                                LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) j.next();
                                LocalAdApprovalQueue adApprovalQueue = adApprovalQueueHome
                                        .AqDepartment(userDepartment)
                                        .AqLevel(adApprovalUser.getAuLevel())
                                        .AqNextLevel(EJBCommon.incrementStringNumber(adApprovalUser.getAuLevel()))
                                        .AqDocument(approvalQueueDocument)
                                        .AqDocumentCode(approvalQueueDocumentCode)
                                        .AqDocumentNumber(approvalQueueDocumentNumber)
                                        .AqDate(approvalQueueDate)
                                        .AqAndOr(adAmountLimit.getCalAndOr())
                                        .AqUserOr(adApprovalUser.getAuOr())
                                        .AqRequesterName(userDesc)
                                        .AqAdBranch(branchCode)
                                        .AqAdCompany(companyCode)
                                        .buildApprovalQueue(companyShortName);
                                adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                                if (adApprovalUser.getAuLevel().equals(EJBCommon.LEVEL1APPROVER)) {
                                    adApprovalQueue.setAqForApproval(EJBCommon.TRUE);
                                    //TODO: Add an email notification mechanism based on the Preference Setup
                                    // getPrfAdEnableEmailNotification with Approval Queue User Email Address
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
                prApprovalStatus = EJBCommon.PENDING;
            }
            return prApprovalStatus;
        }
        catch (GlobalNoApprovalApproverFoundException ex) {
            throw new GlobalNoApprovalApproverFoundException();
        }
        catch (GlobalNoApprovalRequesterFoundException ex) {
            throw new GlobalNoApprovalRequesterFoundException();
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public ArrayList getAdApprovalNotifiedUsersByAqCode(Integer approvalQueueCode, Integer companyCode, String companyShortName) {

        Debug.print("ArApprovalControllerBean getAdApprovalNotifiedUsersByAqCode");
        ArrayList list = new ArrayList();
        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode, companyShortName);
            LocalAdApprovalQueue adApprovalQueue;
            try {
                adApprovalQueue = adApprovalQueueHome.findByPrimaryKey(approvalQueueCode, companyShortName);
                if (adApprovalQueue.getAqApproved() == EJBCommon.TRUE) {
                    list.add(EJBCommon.DOCUMENT_POSTED);
                }
            }
            catch (Exception e) {
                list.add(EJBCommon.DOCUMENT_REJECTED);
                return list;
            }

            if (adApprovalQueue.getAqDocument().equals(EJBCommon.AR_INVOICE)) {
                LocalArInvoice arInvoice = arInvoiceHome.findByPrimaryKey(adApprovalQueue.getAqDocumentCode(), companyShortName);
                if (arInvoice.getInvPosted() == EJBCommon.TRUE) {
                    list.add(EJBCommon.DOCUMENT_POSTED);
                    return list;
                }
            }

            if (adApprovalQueue.getAqDocument().equals(EJBCommon.AR_SALES_ORDER)) {
                LocalArSalesOrder arSalesOrder = arSalesOrderHome.findByPrimaryKey(adApprovalQueue.getAqDocumentCode(), companyShortName);
                if (arSalesOrder.getSoPosted() == EJBCommon.TRUE) {
                    list.add(EJBCommon.DOCUMENT_POSTED);
                    return list;
                }
            }

            if (adApprovalQueue.getAqDocument().equals(EJBCommon.AR_RECEIPT)) {
                LocalArReceipt arReceipt = arReceiptHome.findByPrimaryKey(adApprovalQueue.getAqDocumentCode(), companyShortName);
                if (arReceipt.getRctPosted() == EJBCommon.TRUE) {
                    list.add(EJBCommon.DOCUMENT_POSTED);
                    return list;
                }
            }

            String messageUser;
            LocalAdApprovalQueue adNextApprovalQueue = adApprovalQueueHome.findByAqDeptAndAqLevelAndAqDocumentCode(
                    adApprovalQueue.getAqDepartment(), adApprovalQueue.getAqNextLevel(), adApprovalQueue.getAqDocumentCode(), companyCode,
                    companyShortName);
            messageUser = adNextApprovalQueue.getAqLevel() + " APPROVER - " + adNextApprovalQueue.getAdUser().getUsrDescription();
            try {
                adNextApprovalQueue.setAqForApproval(EJBCommon.TRUE);
            }
            catch (Exception e) {
                messageUser += " [Email Notification Not Sent. Cannot connect host or no internet connection.]";
            }
            list.add(messageUser);
            return list;

        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public ArrayList getAdAqByAqDocumentAndUserName(HashMap criteria, String username, Integer offset,
                                                    Integer limit, String orderBy, Integer branchCode, Integer companyCode,
                                                    String companyShortName)
            throws GlobalNoRecordFoundException {

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

            jbossQl.append("aq.aqForApproval = 1 AND aq.aqApproved = 0 AND aq.aqAdCompany=" +
                    companyCode + " " + "AND aq.adUser.usrName='" + username + "'");

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

            Collection adApprovalQueues = adApprovalQueueHome.getAqByCriteria(jbossQl.toString(), obj, limit, offset, companyShortName);
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
                if (approvalQueueDocument.equals(EJBCommon.AR_INVOICE) || approvalQueueDocument.equals(EJBCommon.AR_CREDIT_MEMO)) {
                    LocalArInvoice arInvoice = arInvoiceHome.findByPrimaryKey(adApprovalQueue.getAqDocumentCode(), companyShortName);
                    details.setAqDate(arInvoice.getInvDate());
                    details.setAqDocumentNumber(arInvoice.getInvNumber());
                    details.setAqAmount(arInvoice.getInvAmountDue());
                    details.setAqCustomerCode(arInvoice.getArCustomer().getCstCustomerCode());
                    details.setAqCustomerName(arInvoice.getArCustomer().getCstName());
                    if (!arInvoice.getArInvoiceLineItems().isEmpty()) {
                        details.setAqType(EJBCommon.API_ITEMS);
                    } else {
                        details.setAqType(EJBCommon.API_MEMOLINES);
                    }
                } else if (approvalQueueDocument.equals(EJBCommon.AR_RECEIPT)) {
                    LocalArReceipt arReceipt = arReceiptHome.findByPrimaryKey(adApprovalQueue.getAqDocumentCode(), companyShortName);
                    details.setAqDate(arReceipt.getRctDate());
                    details.setAqDocumentNumber(arReceipt.getRctNumber());
                    details.setAqAmount(arReceipt.getRctAmount());
                    details.setAqCustomerCode(arReceipt.getArCustomer().getCstCustomerCode());
                    details.setAqCustomerName(arReceipt.getArCustomer().getCstName());
                    details.setAqDocumentType(arReceipt.getRctType());
                    if (!arReceipt.getArInvoiceLineItems().isEmpty()) {
                        details.setAqType(EJBCommon.API_ITEMS);
                    } else {
                        details.setAqType(EJBCommon.API_MEMOLINES);
                    }
                } else if (approvalQueueDocument.equals(EJBCommon.AR_CUSTOMER)) {
                    LocalArCustomer arCustomer = arCustomerHome.findByPrimaryKey(adApprovalQueue.getAqDocumentCode(), companyShortName);
                    details.setAqDate(arCustomer.getCstDateLastModified());
                    details.setAqDocumentNumber(arCustomer.getCstName());
                    details.setAqCustomerCode(arCustomer.getCstCustomerCode());
                    details.setAqReferenceNumber(null);
                    details.setAqCustomerName(arCustomer.getCstCustomerCode());
                } else {
                    LocalArSalesOrder arSalesOrder = arSalesOrderHome.findByPrimaryKey(adApprovalQueue.getAqDocumentCode(), companyShortName);
                    details.setAqDate(arSalesOrder.getSoDate());
                    details.setAqDocumentNumber(arSalesOrder.getSoDocumentNumber());
                    details.setAqCustomerCode(arSalesOrder.getArCustomer().getCstCustomerCode());
                    details.setAqCustomerName(arSalesOrder.getArCustomer().getCstName());
                    details.setAqType(EJBCommon.API_ITEMS);
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

        }
        catch (GlobalNoRecordFoundException ex) {
            throw ex;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

}