package com.ejb.txn.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.dao.ad.LocalAdUserHome;
import com.ejb.dao.ap.LocalApCheckHome;
import com.ejb.dao.ap.LocalApVoucherHome;
import com.ejb.dao.ar.LocalArInvoiceHome;
import com.ejb.dao.ar.LocalArReceiptHome;
import com.ejb.dao.ar.LocalArSalesOrderHome;
import com.ejb.dao.cm.LocalCmAdjustmentHome;
import com.ejb.dao.cm.LocalCmFundTransferHome;
import com.ejb.dao.gl.LocalGlJournalHome;
import com.ejb.dao.inv.LocalInvAdjustmentHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdUser;
import com.ejb.entities.ap.LocalApCheck;
import com.ejb.entities.ap.LocalApVoucher;
import com.ejb.entities.ar.LocalArInvoice;
import com.ejb.entities.ar.LocalArReceipt;
import com.ejb.entities.ar.LocalArSalesOrder;
import com.ejb.entities.ar.LocalArSalesOrderLine;
import com.ejb.entities.cm.LocalCmAdjustment;
import com.ejb.entities.cm.LocalCmFundTransfer;
import com.ejb.entities.gl.LocalGlJournal;
import com.ejb.entities.gl.LocalGlJournalLine;
import com.ejb.entities.inv.LocalInvAdjustment;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.ad.AdRepTransactionSummaryDetails;

import jakarta.ejb.*;
import java.util.*;

@Stateless(name = "AdRepTransactionSummaryControllerEJB")
public class AdRepTransactionSummaryControllerBean extends EJBContextClass implements AdRepTransactionSummaryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalGlJournalHome glJournalHome;
    @EJB
    private LocalApVoucherHome apVoucherHome;
    @EJB
    private LocalApCheckHome apCheckHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalArSalesOrderHome arSalesOrderHome;
    @EJB
    private LocalCmAdjustmentHome cmAdjustmentHome;
    @EJB
    private LocalCmFundTransferHome cmFundTransferHome;
    @EJB
    private LocalInvAdjustmentHome invAdjustmentHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;

    public ArrayList getAdUsrAll(Integer companyCode) {

        Debug.print("AdRepTransactionSummaryControllerBean getAdUsrAll");

        ArrayList list = new ArrayList();
        try {

            Collection adUsers = adUserHome.findUsrAll(companyCode);

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

    public ArrayList executeAdRepTransactionSummary(HashMap criteria, ArrayList branchList, String orderBy, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdRepTransactionSummaryControllerBean executeAdRepTransactionSummary");

        ArrayList list = new ArrayList();
        try {

            if (!criteria.containsKey("type") || criteria.get("type").equals("GL JOURNAL")) {

                int criteriaSize = 0;

                if (criteria.containsKey("docDateFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("docDateTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateCreatedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateCreatedTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateLastModifiedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateLastModifiedTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateApprovedRejectedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateApprovedRejectedTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("datePostedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("datePostedTo")) {
                    criteriaSize++;
                }

                Object[] obj = new Object[criteriaSize];

                ArrayList tcarray = new ArrayList();

                AdRepTransactionSummaryDetails details = new AdRepTransactionSummaryDetails();

                Collection glJournals = glJournalHome.getJrByCriteria(this.getQueryByTxnTypeAndCriteria("GlJournal", "jr", criteria, branchList, obj, companyCode), obj, 0, 0);

                for (Object journal : glJournals) {

                    LocalGlJournal glJournal = (LocalGlJournal) journal;

                    details.setTxlType("GL JOURNAL");
                    details.setTxlDate(glJournal.getJrEffectiveDate());
                    details.setTxlDocumentNumber(glJournal.getJrDocumentNumber());

                    Collection glJournalLines = glJournal.getGlJournalLines();

                    for (Object journalLine : glJournalLines) {

                        LocalGlJournalLine glJournalLine = (LocalGlJournalLine) journalLine;

                        if (glJournalLine.getJlDebit() == EJBCommon.TRUE) {

                            details.setTxlAmount(details.getTxlAmount() + glJournalLine.getJlAmount());
                        }
                    }
                    details.setTxlApprovalStatus(glJournal.getJrApprovalStatus());
                    details.setTxlPosted(glJournal.getJrPosted());
                    details.setTxlCreatedBy(glJournal.getJrCreatedBy());
                    details.setTxlDateCreated(glJournal.getJrDateCreated());
                    details.setTxlLastModifiedBy(glJournal.getJrLastModifiedBy());
                    details.setTxlDateLastModified(glJournal.getJrDateLastModified());
                    details.setTxlApprovedRejectedBy(glJournal.getJrApprovedRejectedBy());
                    details.setTxlDateApprovedRejected(glJournal.getJrDateApprovedRejected());
                    details.setTxlPostedBy(glJournal.getJrPostedBy());
                    details.setTxlDatePosted(glJournal.getJrDatePosted());
                    details.setTxlOrderBy(orderBy);

                    list.add(details);
                }
            }

            if (!criteria.containsKey("type") || criteria.get("type").equals("AP VOUCHER")) {

                int criteriaSize = 0;

                if (criteria.containsKey("docDateFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("docDateTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateCreatedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateCreatedTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateLastModifiedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateLastModifiedTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateApprovedRejectedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateApprovedRejectedTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("datePostedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("datePostedTo")) {
                    criteriaSize++;
                }

                Object[] obj = new Object[criteriaSize];

                AdRepTransactionSummaryDetails details = new AdRepTransactionSummaryDetails();

                Collection apVouchers = apVoucherHome.getVouByCriteria(this.getQueryByTxnTypeAndCriteria("ApVoucher", "vou", criteria, branchList, obj, companyCode), obj, 0, 0);

                for (Object voucher : apVouchers) {

                    LocalApVoucher apVoucher = (LocalApVoucher) voucher;

                    if (apVoucher.getVouDebitMemo() == EJBCommon.FALSE) {

                        details.setTxlType("AP VOUCHER");

                    } else {

                        details.setTxlType("AP DEBIT MEMO");
                    }
                    details.setTxlDate(apVoucher.getVouDate());
                    details.setTxlDocumentNumber(apVoucher.getVouDocumentNumber());
                    details.setTxlAmount(apVoucher.getVouAmountDue());
                    details.setTxlApprovalStatus(apVoucher.getVouApprovalStatus());
                    details.setTxlPosted(apVoucher.getVouPosted());
                    details.setTxlCreatedBy(apVoucher.getVouCreatedBy());
                    details.setTxlDateCreated(apVoucher.getVouDateCreated());
                    details.setTxlLastModifiedBy(apVoucher.getVouLastModifiedBy());
                    details.setTxlDateLastModified(apVoucher.getVouDateLastModified());
                    details.setTxlApprovedRejectedBy(apVoucher.getVouApprovedRejectedBy());
                    details.setTxlDateApprovedRejected(apVoucher.getVouDateApprovedRejected());
                    details.setTxlPostedBy(apVoucher.getVouPostedBy());
                    details.setTxlDatePosted(apVoucher.getVouDatePosted());
                    details.setTxlOrderBy(orderBy);

                    list.add(details);
                }
            }

            if (!criteria.containsKey("type") || criteria.get("type").equals("AP CHECK")) {

                int criteriaSize = 0;

                if (criteria.containsKey("docDateFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("docDateTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateCreatedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateCreatedTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateLastModifiedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateLastModifiedTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateApprovedRejectedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateApprovedRejectedTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("datePostedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("datePostedTo")) {
                    criteriaSize++;
                }

                Object[] obj = new Object[criteriaSize];

                AdRepTransactionSummaryDetails details = new AdRepTransactionSummaryDetails();

                Collection apChecks = apCheckHome.getChkByCriteria(this.getQueryByTxnTypeAndCriteria("ApCheck", "chk", criteria, branchList, obj, companyCode), obj, 0, 0);

                for (Object check : apChecks) {

                    LocalApCheck apCheck = (LocalApCheck) check;

                    details.setTxlType("AP CHECK");

                    details.setTxlDate(apCheck.getChkDate());
                    details.setTxlDocumentNumber(apCheck.getChkDocumentNumber());
                    details.setTxlAmount(apCheck.getChkAmount());
                    details.setTxlApprovalStatus(apCheck.getChkApprovalStatus());
                    details.setTxlPosted(apCheck.getChkPosted());
                    details.setTxlCreatedBy(apCheck.getChkCreatedBy());
                    details.setTxlDateCreated(apCheck.getChkDateCreated());
                    details.setTxlLastModifiedBy(apCheck.getChkLastModifiedBy());
                    details.setTxlDateLastModified(apCheck.getChkDateLastModified());
                    details.setTxlApprovedRejectedBy(apCheck.getChkApprovedRejectedBy());
                    details.setTxlDateApprovedRejected(apCheck.getChkDateApprovedRejected());
                    details.setTxlPostedBy(apCheck.getChkPostedBy());
                    details.setTxlDatePosted(apCheck.getChkDatePosted());
                    details.setTxlOrderBy(orderBy);

                    list.add(details);
                }
            }

            if (!criteria.containsKey("type") || criteria.get("type").equals("AR INVOICE")) {

                int criteriaSize = 0;

                if (criteria.containsKey("docDateFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("docDateTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateCreatedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateCreatedTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateLastModifiedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateLastModifiedTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateApprovedRejectedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateApprovedRejectedTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("datePostedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("datePostedTo")) {
                    criteriaSize++;
                }

                Object[] obj = new Object[criteriaSize];

                int ifelse = 0;

                Collection arInvoices = arInvoiceHome.getInvByCriteria(this.getQueryByTxnTypeAndCriteria("ArInvoice", "inv", criteria, branchList, obj, companyCode), obj, 0, 0);

                Iterator i = arInvoices.iterator();

                AdRepTransactionSummaryDetails details = new AdRepTransactionSummaryDetails();

                while (i.hasNext()) {

                    LocalArInvoice arInvoice = (LocalArInvoice) i.next();

                    if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {

                        details.setTxlType("AR INVOICE");

                    } else {

                        details.setTxlType("AR CREDIT MEMO");
                    }
                    details.setTxlDate(arInvoice.getInvDate());
                    details.setTxlDocumentNumber(arInvoice.getInvNumber());
                    details.setTxlAmount(arInvoice.getInvAmountDue());
                    details.setTxlApprovalStatus(arInvoice.getInvApprovalStatus());
                    details.setTxlPosted(arInvoice.getInvPosted());
                    details.setTxlCreatedBy(arInvoice.getInvCreatedBy());
                    details.setTxlDateCreated(arInvoice.getInvDateCreated());
                    details.setTxlLastModifiedBy(arInvoice.getInvLastModifiedBy());
                    details.setTxlDateLastModified(arInvoice.getInvDateLastModified());
                    details.setTxlApprovedRejectedBy(arInvoice.getInvApprovedRejectedBy());
                    details.setTxlDateApprovedRejected(arInvoice.getInvDateApprovedRejected());
                    details.setTxlPostedBy(arInvoice.getInvPostedBy());
                    details.setTxlDatePosted(arInvoice.getInvDatePosted());
                    details.setTxlOrderBy(orderBy);

                    list.add(details);
                }
            }

            if (!criteria.containsKey("type") || criteria.get("type").equals("AR RECEIPT")) {

                int criteriaSize = 0;

                if (criteria.containsKey("docDateFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("docDateTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateCreatedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateCreatedTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateLastModifiedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateLastModifiedTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateApprovedRejectedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateApprovedRejectedTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("datePostedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("datePostedTo")) {
                    criteriaSize++;
                }

                Object[] obj = new Object[criteriaSize];

                AdRepTransactionSummaryDetails details = new AdRepTransactionSummaryDetails();

                Collection arReceipts = arReceiptHome.getRctByCriteria(this.getQueryByTxnTypeAndCriteria("ArReceipt", "rct", criteria, branchList, obj, companyCode), obj, 0, 0);

                for (Object receipt : arReceipts) {

                    LocalArReceipt arReceipt = (LocalArReceipt) receipt;

                    details.setTxlType("AR RECEIPT");
                    details.setTxlDate(arReceipt.getRctDate());
                    details.setTxlDocumentNumber(arReceipt.getRctNumber());
                    details.setTxlAmount(arReceipt.getRctAmount());
                    details.setTxlApprovalStatus(arReceipt.getRctApprovalStatus());
                    details.setTxlPosted(arReceipt.getRctPosted());
                    details.setTxlCreatedBy(arReceipt.getRctCreatedBy());
                    details.setTxlDateCreated(arReceipt.getRctDateCreated());
                    details.setTxlLastModifiedBy(arReceipt.getRctLastModifiedBy());
                    details.setTxlDateLastModified(arReceipt.getRctDateLastModified());
                    details.setTxlApprovedRejectedBy(arReceipt.getRctApprovedRejectedBy());
                    details.setTxlDateApprovedRejected(arReceipt.getRctDateApprovedRejected());
                    details.setTxlPostedBy(arReceipt.getRctPostedBy());
                    details.setTxlDatePosted(arReceipt.getRctDatePosted());
                    details.setTxlOrderBy(orderBy);

                    list.add(details);
                }
            }

            if (!criteria.containsKey("type") || criteria.get("type").equals("AR SALES ORDER")) {

                int criteriaSize = 0;

                if (criteria.containsKey("dateCreatedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateCreatedTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateLastModifiedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateLastModifiedTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateApprovedRejectedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateApprovedRejectedTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("datePostedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("datePostedTo")) {
                    criteriaSize++;
                }

                Object[] obj = new Object[criteriaSize];

                Collection arSalesOrders = arSalesOrderHome.getSOByCriteria(this.getQueryByTxnTypeAndCriteria("ArSalesOrder", "so", criteria, branchList, obj, companyCode), obj, 0, 0);

                for (Object salesOrder : arSalesOrders) {

                    LocalArSalesOrder arSalesOrder = (LocalArSalesOrder) salesOrder;

                    AdRepTransactionSummaryDetails details = new AdRepTransactionSummaryDetails();

                    details.setTxlType("AR SALES ORDER");
                    details.setTxlDate(arSalesOrder.getSoDate());
                    details.setTxlDocumentNumber(arSalesOrder.getSoDocumentNumber());
                    details.setTxlApprovalStatus(arSalesOrder.getSoApprovalStatus());
                    details.setTxlPosted(arSalesOrder.getSoPosted());
                    details.setTxlCreatedBy(arSalesOrder.getSoCreatedBy());
                    details.setTxlDateCreated(arSalesOrder.getSoDateCreated());
                    details.setTxlLastModifiedBy(arSalesOrder.getSoLastModifiedBy());
                    details.setTxlDateLastModified(arSalesOrder.getSoDateLastModified());
                    details.setTxlApprovedRejectedBy(arSalesOrder.getSoApprovedRejectedBy());
                    details.setTxlDateApprovedRejected(arSalesOrder.getSoDateApprovedRejected());
                    details.setTxlPostedBy(arSalesOrder.getSoPostedBy());
                    details.setTxlDatePosted(arSalesOrder.getSoDatePosted());
                    details.setTxlOrderBy(orderBy);

                    Collection arSalesOrderLines = arSalesOrder.getArSalesOrderLines();
                    Iterator j = arSalesOrderLines.iterator();

                    double SO_AMNT = 0;

                    while (j.hasNext()) {

                        LocalArSalesOrderLine arSalesOrderLine = (LocalArSalesOrderLine) j.next();

                        SO_AMNT = SO_AMNT + arSalesOrderLine.getSolAmount();
                    }

                    details.setTxlAmount(SO_AMNT);

                    list.add(details);
                }
            }

            if (!criteria.containsKey("type") || criteria.get("type").equals("CM ADJUSTMENT")) {

                int criteriaSize = 0;

                if (criteria.containsKey("docDateFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("docDateTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateCreatedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateCreatedTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateLastModifiedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateLastModifiedTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateApprovedRejectedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateApprovedRejectedTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("datePostedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("datePostedTo")) {
                    criteriaSize++;
                }

                Object[] obj = new Object[criteriaSize];

                AdRepTransactionSummaryDetails details = new AdRepTransactionSummaryDetails();

                Collection cmAdjustments = cmAdjustmentHome.getAdjByCriteria(this.getQueryByTxnTypeAndCriteria("CmAdjustment", "adj", criteria, branchList, obj, companyCode), obj, 0, 0);

                for (Object adjustment : cmAdjustments) {

                    LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) adjustment;

                    details.setTxlType("CM ADJUSTMENT");
                    details.setTxlDate(cmAdjustment.getAdjDate());
                    details.setTxlDocumentNumber(cmAdjustment.getAdjReferenceNumber());
                    details.setTxlAmount(cmAdjustment.getAdjAmount());
                    details.setTxlApprovalStatus(cmAdjustment.getAdjApprovalStatus());
                    details.setTxlPosted(cmAdjustment.getAdjPosted());
                    details.setTxlCreatedBy(cmAdjustment.getAdjCreatedBy());
                    details.setTxlDateCreated(cmAdjustment.getAdjDateCreated());
                    details.setTxlLastModifiedBy(cmAdjustment.getAdjLastModifiedBy());
                    details.setTxlDateLastModified(cmAdjustment.getAdjDateLastModified());
                    details.setTxlApprovedRejectedBy(cmAdjustment.getAdjApprovedRejectedBy());
                    details.setTxlDateApprovedRejected(cmAdjustment.getAdjDateApprovedRejected());
                    details.setTxlPostedBy(cmAdjustment.getAdjPostedBy());
                    details.setTxlDatePosted(cmAdjustment.getAdjDatePosted());
                    details.setTxlOrderBy(orderBy);

                    list.add(details);
                }
            }

            if (!criteria.containsKey("type") || criteria.get("type").equals("CM FUND TRANSFER")) {

                int criteriaSize = 0;

                if (criteria.containsKey("docDateFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("docDateTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateCreatedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateCreatedTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateLastModifiedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateLastModifiedTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateApprovedRejectedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateApprovedRejectedTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("datePostedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("datePostedTo")) {
                    criteriaSize++;
                }

                Object[] obj = new Object[criteriaSize];

                AdRepTransactionSummaryDetails details = new AdRepTransactionSummaryDetails();

                Collection cmFundTransfers = cmFundTransferHome.getFtByCriteria(this.getQueryByTxnTypeAndCriteria("CmFundTransfer", "ft", criteria, branchList, obj, companyCode), obj, 0, 0);

                for (Object fundTransfer : cmFundTransfers) {

                    LocalCmFundTransfer cmFundTransfer = (LocalCmFundTransfer) fundTransfer;

                    details.setTxlType("CM FUND TRANSFER");
                    details.setTxlDate(cmFundTransfer.getFtDate());
                    details.setTxlDocumentNumber(cmFundTransfer.getFtReferenceNumber());
                    details.setTxlAmount(cmFundTransfer.getFtAmount());
                    details.setTxlApprovalStatus(cmFundTransfer.getFtApprovalStatus());
                    details.setTxlPosted(cmFundTransfer.getFtPosted());
                    details.setTxlCreatedBy(cmFundTransfer.getFtCreatedBy());
                    details.setTxlDateCreated(cmFundTransfer.getFtDateCreated());
                    details.setTxlLastModifiedBy(cmFundTransfer.getFtLastModifiedBy());
                    details.setTxlDateLastModified(cmFundTransfer.getFtDateLastModified());
                    details.setTxlApprovedRejectedBy(cmFundTransfer.getFtApprovedRejectedBy());
                    details.setTxlDateApprovedRejected(cmFundTransfer.getFtDateApprovedRejected());
                    details.setTxlPostedBy(cmFundTransfer.getFtPostedBy());
                    details.setTxlDatePosted(cmFundTransfer.getFtDatePosted());
                    details.setTxlOrderBy(orderBy);

                    list.add(details);
                }
            }

            if (!criteria.containsKey("type") || criteria.get("type").equals("INV ADJUSTMENT")) {

                int criteriaSize = 0;

                if (criteria.containsKey("docDateFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("docDateTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateCreatedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateCreatedTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateLastModifiedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateLastModifiedTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateApprovedRejectedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("dateApprovedRejectedTo")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("datePostedFrom")) {
                    criteriaSize++;
                }
                if (criteria.containsKey("datePostedTo")) {
                    criteriaSize++;
                }

                Object[] obj = new Object[criteriaSize];

                AdRepTransactionSummaryDetails details = new AdRepTransactionSummaryDetails();

                Collection invAdjustments = invAdjustmentHome.getAdjByCriteria(this.getQueryByTxnTypeAndCriteria("InvAdjustment", "adj", criteria, branchList, obj, companyCode), obj, 0, 0);

                for (Object adjustment : invAdjustments) {

                    LocalInvAdjustment invAdjustment = (LocalInvAdjustment) adjustment;

                    details.setTxlType("INV ADJUSTMENT");

                    details.setTxlDate(invAdjustment.getAdjDate());
                    details.setTxlDocumentNumber(invAdjustment.getAdjDocumentNumber());
                    details.setTxlApprovalStatus(invAdjustment.getAdjApprovalStatus());
                    details.setTxlPosted(invAdjustment.getAdjPosted());
                    details.setTxlCreatedBy(invAdjustment.getAdjCreatedBy());
                    details.setTxlDateCreated(invAdjustment.getAdjDateCreated());
                    details.setTxlLastModifiedBy(invAdjustment.getAdjLastModifiedBy());
                    details.setTxlDateLastModified(invAdjustment.getAdjDateLastModified());
                    details.setTxlApprovedRejectedBy(invAdjustment.getAdjApprovedRejectedBy());
                    details.setTxlDateApprovedRejected(invAdjustment.getAdjDateApprovedRejected());
                    details.setTxlPostedBy(invAdjustment.getAdjPostedBy());
                    details.setTxlDatePosted(invAdjustment.getAdjDatePosted());
                    details.setTxlOrderBy(orderBy);

                    list.add(details);
                }
            }

            if (list.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }

            Collections.sort(list);

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer companyCode) {

        Debug.print("AdRepTransactionSummaryControllerBean getAdCompany");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());
            details.setCmpAddress(adCompany.getCmpAddress());
            details.setCmpCity(adCompany.getCmpCity());
            details.setCmpContact(adCompany.getCmpContact());
            details.setCmpCountry(adCompany.getCmpCountry());
            details.setCmpEmail(adCompany.getCmpEmail());
            details.setCmpTin(adCompany.getCmpTin());
            details.setCmpDescription(adCompany.getCmpDescription());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdBrResAll(Integer responsibilityCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdRepBankAccountListControllerBean getAdBrResAll");

        LocalAdBranchResponsibility adBranchResponsibility;
        LocalAdBranch adBranch;
        Collection adBranchResponsibilities = null;
        ArrayList list = new ArrayList();
        try {

            adBranchResponsibilities = adBranchResponsibilityHome.findByAdResponsibility(responsibilityCode, companyCode);

        } catch (FinderException ex) {
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        if (adBranchResponsibilities.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }
        try {

            for (Object branchResponsibility : adBranchResponsibilities) {

                adBranchResponsibility = (LocalAdBranchResponsibility) branchResponsibility;

                adBranch = adBranchResponsibility.getAdBranch();

                AdBranchDetails details = new AdBranchDetails();

                details.setBrCode(adBranch.getBrCode());
                details.setBrBranchCode(adBranch.getBrBranchCode());
                details.setBrName(adBranch.getBrName());
                details.setBrHeadQuarter(adBranch.getBrHeadQuarter());

                list.add(details);
            }

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        return list;
    }

    // private methods
    private String getQueryByTxnTypeAndCriteria(String table, String tablePrefix, HashMap criteria, ArrayList branchList, Object[] obj, Integer companyCode) throws GlobalNoRecordFoundException {

        StringBuilder jbossQl = new StringBuilder();
        int ctr = 0;
        boolean firstArgument = true;

        jbossQl.append("SELECT OBJECT(").append(tablePrefix).append(") FROM ").append(table).append(" ").append(tablePrefix).append(" ");

        if (branchList.isEmpty()) {

            throw new GlobalNoRecordFoundException();

        } else {

            jbossQl.append("WHERE ").append(tablePrefix).append(".").append(tablePrefix).append("AdBranch in (");

            boolean firstLoop = true;

            for (Object o : branchList) {

                if (!firstLoop) {
                    jbossQl.append(", ");
                } else {
                    firstLoop = false;
                }

                AdBranchDetails mdetails = (AdBranchDetails) o;

                jbossQl.append(mdetails.getBrCode());
            }

            jbossQl.append(") ");

            firstArgument = false;
        }

        if (criteria.containsKey("createdBy")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append(tablePrefix).append(".").append(tablePrefix).append("CreatedBy = '").append(criteria.get("createdBy")).append("' ");
        }

        if (criteria.containsKey("dateCreatedFrom")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append(tablePrefix).append(".").append(tablePrefix).append("DateCreated >= ?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("dateCreatedFrom");
            ctr++;
        }

        if (criteria.containsKey("dateCreatedTo")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append(tablePrefix).append(".").append(tablePrefix).append("DateCreated <= ?").append(ctr + 1).append(" ");
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime((Date) criteria.get("dateCreatedTo"));
            gc.set(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE), 23, 59, 59);
            obj[ctr] = gc.getTime();
            ctr++;
        }

        if (criteria.containsKey("docDateFrom")) {

            String effectiveDate = "";

            if (table.equals("GlJournal")) {

                effectiveDate = "Effective";
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append(tablePrefix).append(".").append(tablePrefix).append(effectiveDate).append("Date >= ?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("docDateFrom");
            ctr++;
        }

        if (criteria.containsKey("docDateTo")) {

            String effectiveDate = "";

            if (table.equals("GlJournal")) {

                effectiveDate = "Effective";
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append(tablePrefix).append(".").append(tablePrefix).append(effectiveDate).append("Date <= ?").append(ctr + 1).append(" ");
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime((Date) criteria.get("docDateTo"));
            gc.set(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE), 23, 59, 59);
            obj[ctr] = gc.getTime();
            ctr++;
        }

        if (criteria.containsKey("lastModifiedBy")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append(tablePrefix).append(".").append(tablePrefix).append("LastModifiedBy = '").append(criteria.get("lastModifiedBy")).append("' ");
        }

        if (criteria.containsKey("dateLastModifiedFrom")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append(tablePrefix).append(".").append(tablePrefix).append("DateLastModified >= ?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("dateLastModifiedFrom");
            ctr++;
        }

        if (criteria.containsKey("dateLastModifiedTo")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append(tablePrefix).append(".").append(tablePrefix).append("DateLastModified <= ?").append(ctr + 1).append(" ");
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime((Date) criteria.get("dateLastModifiedTo"));
            gc.set(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE), 23, 59, 59);
            obj[ctr] = gc.getTime();
            ctr++;
        }

        if (criteria.containsKey("approvedRejectedBy")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append(tablePrefix).append(".").append(tablePrefix).append("ApprovedRejectedBy = '").append(criteria.get("approvedRejectedBy")).append("' ");
        }

        if (criteria.containsKey("dateApprovedRejectedFrom")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append(tablePrefix).append(".").append(tablePrefix).append("DateApprovedRejected >= ?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("dateApprovedRejectedFrom");
            ctr++;
        }

        if (criteria.containsKey("dateApprovedRejectedTo")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append(tablePrefix).append(".").append(tablePrefix).append("DateApprovedRejected <= ?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("dateApprovedRejectedTo");
            ctr++;
        }

        if (criteria.containsKey("postedBy")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append(tablePrefix).append(".").append(tablePrefix).append("PostedBy = '").append(criteria.get("postedBy")).append("' ");
        }

        if (criteria.containsKey("datePostedFrom")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append(tablePrefix).append(".").append(tablePrefix).append("DatePosted >= ?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("datePostedFrom");
            ctr++;
        }

        if (criteria.containsKey("datePostedTo")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append(tablePrefix).append(".").append(tablePrefix).append("DatePosted <= ?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("datePostedTo");
            ctr++;
        }

        if (!firstArgument) {

            jbossQl.append("AND ");

        } else {

            firstArgument = false;
            jbossQl.append("WHERE ");
        }

        jbossQl.append(tablePrefix).append(".").append(tablePrefix).append("AdCompany=").append(companyCode).append(" ");

        return jbossQl.toString();
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("AdRepTransactionSummaryControllerBean ejbCreate");
    }

}