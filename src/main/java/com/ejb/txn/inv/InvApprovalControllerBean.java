/**
 * @copyright 2022 Omega Business Consulting, Inc.
 * @class InvApprovalControllerBean
 * @modified June 22, 2022, 18:26
 * @modified Neil M. Ajero
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
import com.ejb.txnreports.inv.InvRepItemCostingController;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.ad.AdBranchDetails;
import com.util.mod.ad.AdModApprovalQueueDetails;
import com.util.mod.inv.InvModBranchStockTransferDetails;

import jakarta.ejb.*;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.NamingException;
import java.util.*;

@Stateless(name = "InvApprovalControllerEJB")
public class InvApprovalControllerBean extends EJBContextClass implements InvApprovalController {

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
    private ILocalGlSetOfBookHome glSetOfBookHome;
    @EJB
    private ILocalGlAccountingCalendarValueHome glAccountingCalendarValueHome;
    @EJB
    private ILocalGlChartOfAccountBalanceHome glChartOfAccountBalanceHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalInvAdjustmentHome invAdjustmentHome;
    @EJB
    private LocalInvStockTransferHome invStockTransferHome;
    @EJB
    private LocalInvBranchStockTransferHome invBranchStockTransferHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
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
    private LocalInvDistributionRecordHome invDistributionRecordHome;
    @EJB
    private LocalInvAdjustmentLineHome invAdjustmentLineHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;

    public ArrayList getAdApprovalNotifiedUsersByAqCode(Integer approvalQueueCode, Integer companyCode) {

        Debug.print("InvApprovalControllerBean getAdApprovalNotifiedUsersByAqCode");
        ArrayList list = new ArrayList();
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdApprovalQueue adApprovalQueue = null;
            try {

                adApprovalQueue = adApprovalQueueHome.findByPrimaryKey(approvalQueueCode);

            }
            catch (Exception e) {
                list.add("DOCUMENT REJECTED");
                return list;
            }

            LocalInvAdjustment invAdjustment = invAdjustmentHome.findByPrimaryKey(adApprovalQueue.getAqDocumentCode());

            if (invAdjustment.getAdjPosted() == EJBCommon.TRUE) {

                list.add("DOCUMENT POSTED");
                return list;
            }

            try {

                LocalAdApprovalQueue adNextApprovalQueue = adApprovalQueueHome.findByAqDeptAndAqLevelAndAqDocumentCode(
                        adApprovalQueue.getAqDepartment(), adApprovalQueue.getAqNextLevel(),
                        adApprovalQueue.getAqDocumentCode(), companyCode);
                list.add(adNextApprovalQueue.getAqLevel() + " APPROVER - "
                        + adNextApprovalQueue.getAdUser().getUsrDescription());
                Debug.print("adNextApprovalQueue.getAdUser().getUsrDescription()="
                        + adNextApprovalQueue.getAdUser().getUsrDescription());
                adNextApprovalQueue.setAqForApproval(EJBCommon.TRUE);
                if ((!adApprovalQueue.getAdUser().getUsrEmailAddress().equals(""))
                        && (adPreference.getPrfAdEnableEmailNotification() == EJBCommon.TRUE)) {
                    this.sendEmail(adNextApprovalQueue, companyCode);
                }

            }
            catch (Exception e) {

            }

            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdAqByAqDocumentAndUserName(HashMap criteria, String username, Integer offset, Integer limit,
                                                    String orderBy, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("InvApprovalControllerBean getAdAqByAqDocumentAndUserName");
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
            jbossQl.append("aq.aqForApproval = 1 AND aq.aqApproved = 0 AND aq.aqAdCompany=" + companyCode + " "
                    + "AND aq.adUser.usrName='" + username + "'");

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

            // Include Draft BST IN
            if (approvalQueueDocument.equals("INV BRANCH STOCK TRANSFER")) {

                HashMap bstCriteria = new HashMap();

                if (criteria.containsKey("documentNumberFrom")) {
                    bstCriteria.put("documentNumberFrom", criteria.get("documentNumberFrom"));
                }
                if (criteria.containsKey("documentNumberTo")) {
                    bstCriteria.put("documentNumberTo", criteria.get("documentNumberTo"));
                }
                if (criteria.containsKey("dateFrom")) {
                    bstCriteria.put("dateFrom", criteria.get("dateFrom"));
                }
                if (criteria.containsKey("dateTo")) {
                    bstCriteria.put("dateTo", criteria.get("dateTo"));
                }

                ArrayList bstInList = this.getInvBstByCriteria(bstCriteria, offset, limit, "", branchCode, companyCode);

                Iterator bstInIter = bstInList.iterator();

                while (bstInIter.hasNext()) {

                    InvModBranchStockTransferDetails bstInDetail = (InvModBranchStockTransferDetails) bstInIter.next();

                    AdModApprovalQueueDetails details = new AdModApprovalQueueDetails();

                    details.setAqDocument("INV BRANCH STOCK TRANSFER");
                    details.setAqDocumentCode(bstInDetail.getBstCode());
                    details.setAqBrCode(bstInDetail.getBstAdBranch());

                    details.setAqDate(bstInDetail.getBstDate());
                    details.setAqReferenceNumber(
                            bstInDetail.getBstTransferOutNumber() == null ? "" : bstInDetail.getBstTransferOutNumber());
                    details.setAqDocumentNumber(bstInDetail.getBstNumber());
                    LocalAdBranch adBranchCode = adBranchHome.findByPrimaryKey(bstInDetail.getBstAdBranch());

                    details.setAqAdBranchCode(adBranchCode.getBrBranchCode());

                    // get lines

                    double TTL_ST_AMNT = 0d;

                    Collection invDrSt = invBranchStockTransferHome.findByPrimaryKey(bstInDetail.getBstCode())
                            .getInvDistributionRecords();

                    Iterator stIter = invDrSt.iterator();

                    while (stIter.hasNext()) {

                        LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) stIter.next();

                        if (invDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                            TTL_ST_AMNT += invDistributionRecord.getDrAmount();
                        }
                    }

                    details.setAqAmount(TTL_ST_AMNT);
                    list.add(details);
                }
            }

            if (adApprovalQueues.size() == 0 && list.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            Iterator i = adApprovalQueues.iterator();

            while (i.hasNext()) {

                LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) i.next();

                AdModApprovalQueueDetails details = new AdModApprovalQueueDetails();

                details.setAqCode(adApprovalQueue.getAqCode());
                details.setAqDocument(adApprovalQueue.getAqDocument());
                details.setAqDocumentCode(adApprovalQueue.getAqDocumentCode());
                details.setAqDepartment(adApprovalQueue.getAqDepartment());

                if (approvalQueueDocument.equals("INV ADJUSTMENT") || approvalQueueDocument.equals("INV ADJUSTMENT REQUEST")) {

                    LocalInvAdjustment invAdjustment = invAdjustmentHome
                            .findByPrimaryKey(adApprovalQueue.getAqDocumentCode());

                    details.setAqDate(invAdjustment.getAdjDate());
                    details.setAqReferenceNumber(invAdjustment.getAdjReferenceNumber());
                    details.setAqDocumentNumber(invAdjustment.getAdjDocumentNumber());
                    LocalAdBranch adBranchCode = adBranchHome.findByPrimaryKey(invAdjustment.getAdjAdBranch());
                    details.setAqBrCode(invAdjustment.getAdjAdBranch());
                    details.setAqAdBranchCode(adBranchCode.getBrBranchCode());

                    // get lines

                    double TTL_ADJ_AMNT = 0d;

                    Collection invDrAdj = invAdjustment.getInvDistributionRecords();

                    Iterator ilIter = invDrAdj.iterator();

                    while (ilIter.hasNext()) {

                        LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) ilIter.next();

                        if (invDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                            TTL_ADJ_AMNT += invDistributionRecord.getDrAmount();
                        }
                    }

                    details.setAqAmount(TTL_ADJ_AMNT);

                } else if (approvalQueueDocument.equals("INV STOCK TRANSFER")) {

                    LocalInvStockTransfer invStockTransfer = invStockTransferHome
                            .findByPrimaryKey(adApprovalQueue.getAqDocumentCode());

                    details.setAqDate(invStockTransfer.getStDate());
                    details.setAqReferenceNumber(invStockTransfer.getStReferenceNumber());
                    details.setAqDocumentNumber(invStockTransfer.getStDocumentNumber());
                    details.setAqBrCode(invStockTransfer.getStAdBranch());
                    LocalAdBranch adBranchCode = adBranchHome.findByPrimaryKey(invStockTransfer.getStAdBranch());

                    details.setAqAdBranchCode(adBranchCode.getBrBranchCode());

                    // get lines

                    double TTL_ST_AMNT = 0d;

                    Collection invDrSt = invStockTransfer.getInvDistributionRecords();

                    Iterator stIter = invDrSt.iterator();

                    while (stIter.hasNext()) {

                        LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) stIter.next();

                        if (invDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                            TTL_ST_AMNT += invDistributionRecord.getDrAmount();
                        }
                    }

                    details.setAqAmount(TTL_ST_AMNT);

                } else if (approvalQueueDocument.equals("INV BRANCH STOCK TRANSFER")) {

                    LocalInvBranchStockTransfer invBranchStockTransfer = invBranchStockTransferHome
                            .findByPrimaryKey(adApprovalQueue.getAqDocumentCode());

                    details.setAqDate(invBranchStockTransfer.getBstDate());
                    details.setAqReferenceNumber(invBranchStockTransfer.getBstTransferOutNumber() == null ? ""
                            : invBranchStockTransfer.getBstTransferOutNumber());
                    details.setAqDocumentNumber(invBranchStockTransfer.getBstNumber());
                    LocalAdBranch adBranchCode = adBranchHome.findByPrimaryKey(invBranchStockTransfer.getBstAdBranch());

                    details.setAqAdBranchCode(adBranchCode.getBrBranchCode());
                    details.setAqBrCode(invBranchStockTransfer.getBstAdBranch());
                    // get lines

                    double TTL_ST_AMNT = 0d;

                    Collection invDrSt = invBranchStockTransfer.getInvDistributionRecords();

                    Iterator stIter = invDrSt.iterator();

                    while (stIter.hasNext()) {

                        LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) stIter.next();

                        if (invDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                            TTL_ST_AMNT += invDistributionRecord.getDrAmount();
                        }
                    }

                    details.setAqAmount(TTL_ST_AMNT);

                } else if (approvalQueueDocument.equals("INV BRANCH STOCK TRANSFER ORDER")) {

                    LocalInvBranchStockTransfer invBranchStockTransfer = invBranchStockTransferHome
                            .findByPrimaryKey(adApprovalQueue.getAqDocumentCode());

                    details.setAqDate(invBranchStockTransfer.getBstDate());
                    details.setAqReferenceNumber(invBranchStockTransfer.getBstTransferOutNumber() == null ? ""
                            : invBranchStockTransfer.getBstTransferOutNumber());
                    details.setAqDocumentNumber(invBranchStockTransfer.getBstNumber());
                    LocalAdBranch adBranchCode = adBranchHome.findByPrimaryKey(invBranchStockTransfer.getBstAdBranch());

                    details.setAqAdBranchCode(adBranchCode.getBrBranchCode());
                    details.setAqBrCode(invBranchStockTransfer.getBstAdBranch());

                    // get lines

                    double TTL_ST_AMNT = 0d;

                    Collection invBranchStockTransferLines = invBranchStockTransfer.getInvBranchStockTransferLines();
                    Iterator bsos = invBranchStockTransferLines.iterator();
                    while (bsos.hasNext()) {
                        LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) bsos
                                .next();
                        TTL_ST_AMNT += invBranchStockTransferLine.getBslAmount();
                    }
                    details.setAqAmount(0.00);
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

    public void executeInvApproval(String approvalQueueDocument, Integer approvalQueueDocumentCode, String username, boolean isApproved,
                                   String reasonForRejection, Integer branchCode, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException, GlJREffectiveDateNoPeriodExistException,
            GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalInventoryDateException,
            GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("InvApprovalControllerBean executeInvApproval");
        LocalAdApprovalQueue adApprovalQueue = null;
        try {

            // validate if approval queue is already deleted

            try {

                adApprovalQueue = adApprovalQueueHome.findByAqDocumentAndAqDocumentCodeAndUsrName(approvalQueueDocument,
                        approvalQueueDocumentCode, username, companyCode);

            }
            catch (FinderException ex) {

                if (approvalQueueDocument.equals("INV BRANCH STOCK TRANSFER")
                        || approvalQueueDocument.equals("INV BRANCH STOCK TRANSFER ORDER")) {

                    LocalInvBranchStockTransfer invBranchStockTransfer = invBranchStockTransferHome
                            .findByPrimaryKey(approvalQueueDocumentCode);
                    Debug.print("STATUS: " + invBranchStockTransfer.getBstApprovalStatus());
                    if (invBranchStockTransfer.getBstApprovalStatus() != null) {

                        throw new GlobalRecordAlreadyDeletedException();
                    }
                }
            }

            // approve/reject
            Collection adApprovalQueues = null;
            Collection adApprovalQueuesDesc = null;
            Collection adApprovalQueuesAsc = null;
            Collection adAllApprovalQueues = null;

            if (approvalQueueDocument.equals("INV BRANCH STOCK TRANSFER") || approvalQueueDocument.equals("INV BRANCH STOCK TRANSFER ORDER")) {

                LocalInvBranchStockTransfer invBranchStockTransfer = invBranchStockTransferHome
                        .findByPrimaryKey(approvalQueueDocumentCode);

                if (invBranchStockTransfer.getBstApprovalStatus() != null) {

                    adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode(approvalQueueDocument, approvalQueueDocumentCode,
                            companyCode);
                    adApprovalQueuesDesc = adApprovalQueueHome.findByAqDocumentAndAqDocumentCodeLessThanDesc(approvalQueueDocument,
                            approvalQueueDocumentCode, adApprovalQueue.getAqCode(), companyCode);
                    adApprovalQueuesAsc = adApprovalQueueHome.findByAqDocumentAndAqDocumentCodeGreaterThanAsc(approvalQueueDocument,
                            approvalQueueDocumentCode, adApprovalQueue.getAqCode(), companyCode);
                }

            } else {

                adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode(approvalQueueDocument, approvalQueueDocumentCode,
                        companyCode);
                adApprovalQueuesDesc = adApprovalQueueHome.findByAqDocumentAndAqDocumentCodeLessThanDesc(approvalQueueDocument,
                        approvalQueueDocumentCode, adApprovalQueue.getAqCode(), companyCode);
                adApprovalQueuesAsc = adApprovalQueueHome.findByAqDocumentAndAqDocumentCodeGreaterThanAsc(approvalQueueDocument,
                        approvalQueueDocumentCode, adApprovalQueue.getAqCode(), companyCode);
                adAllApprovalQueues = adApprovalQueueHome.findAllByAqDocumentAndAqDocumentCode(approvalQueueDocument, approvalQueueDocumentCode,
                        companyCode);
            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            if (approvalQueueDocument.equals("INV ADJUSTMENT")) {

                LocalInvAdjustment invAdjustment = invAdjustmentHome.findByPrimaryKey(approvalQueueDocumentCode);

                if (isApproved) {

                    if (adApprovalQueue.getAqAndOr().equals("AND")) {

                        if (adApprovalQueues.size() == 1) {

                            invAdjustment.setAdjApprovalStatus("APPROVED");
                            invAdjustment.setAdjApprovedRejectedBy(username);
                            invAdjustment.setAdjDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                            adApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adApprovalQueue.setAqApprovedDate(new java.util.Date());

                            if (adPreference.getPrfInvGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                                this.executeInvAdjPost(invAdjustment.getAdjCode(), username, branchCode, companyCode);
                            }

                        } else {

                            // looping up
                            Iterator i = adApprovalQueuesDesc.iterator();

                            while (i.hasNext()) {

                                LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                                if (adRemoveApprovalQueue.getAqUserOr() == (byte) 1) {
                                    i.remove();
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
                                        adRemoveApprovalQueue.setAqApprovedDate(new java.util.Date());
                                        if (first) {
                                            first = false;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            }

                            // adApprovalQueue.remove();
                            adApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adApprovalQueue.setAqApprovedDate(new java.util.Date());

                            Collection adRemoveApprovalQueues = adApprovalQueueHome
                                    .findByAqDocumentAndAqDocumentCode(approvalQueueDocument, approvalQueueDocumentCode, companyCode);

                            if (adRemoveApprovalQueues.size() == 0) {

                                invAdjustment.setAdjApprovalStatus("APPROVED");
                                invAdjustment.setAdjApprovedRejectedBy(username);
                                invAdjustment.setAdjDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                                if (adPreference.getPrfInvGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                                    this.executeInvAdjPost(invAdjustment.getAdjCode(), username, branchCode, companyCode);
                                }
                            }
                        }
                    } else if (adApprovalQueue.getAqAndOr().equals("OR")) {

                        invAdjustment.setAdjApprovalStatus("APPROVED");
                        invAdjustment.setAdjApprovedRejectedBy(username);
                        invAdjustment.setAdjDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                        Iterator i = adApprovalQueues.iterator();

                        while (i.hasNext()) {

                            LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                            i.remove();
                            // adRemoveApprovalQueue.remove();
                            adRemoveApprovalQueue.setAqApproved(EJBCommon.TRUE);
                            adRemoveApprovalQueue.setAqApprovedDate(new java.util.Date());
                        }

                        if (adPreference.getPrfInvGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                            this.executeInvAdjPost(invAdjustment.getAdjCode(), username, branchCode, companyCode);
                        }
                    }

                } else if (!isApproved) {

                    invAdjustment.setAdjApprovalStatus(null);
                    invAdjustment.setAdjApprovedRejectedBy(username);
                    invAdjustment.setAdjDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());
                    invAdjustment.setAdjReasonForRejection(reasonForRejection);

                    Iterator x = adAllApprovalQueues.iterator();

                    while (x.hasNext()) {
                        LocalAdApprovalQueue adRemoveAllApprovalQueue = (LocalAdApprovalQueue) x.next();
                        x.remove();
                        // adRemoveAllApprovalQueue.entityRemove();
                        em.remove(adRemoveAllApprovalQueue);
                    }
                }

            } else if (approvalQueueDocument.equals("INV ADJUSTMENT REQUEST")) {

                LocalInvAdjustment invAdjustment = invAdjustmentHome.findByPrimaryKey(approvalQueueDocumentCode);

                Collection invAdjustmentLineItems = invAdjustment.getInvAdjustmentLines();

                Iterator i = invAdjustmentLineItems.iterator();

                while (i.hasNext()) {

                    LocalInvAdjustmentLine invAdjustmentLineItem = (LocalInvAdjustmentLine) i.next();

                    // start date validation

                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(
                                invAdjustment.getAdjDate(),
                                invAdjustmentLineItem.getInvItemLocation().getInvItem().getIiName(),
                                invAdjustmentLineItem.getInvItemLocation().getInvLocation().getLocName(), branchCode,
                                companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(
                                    invAdjustmentLineItem.getInvItemLocation().getInvItem().getIiName());
                        }
                    }
                }

                if (isApproved) {

                    if (adApprovalQueue.getAqAndOr().equals("AND")) {

                        if (adApprovalQueues.size() == 1) {

                            invAdjustment.setAdjApprovalStatus("APPROVED");
                            invAdjustment.setAdjApprovedRejectedBy(username);
                            invAdjustment.setAdjDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                            // if (adPreference.getPrfInvGlPostingType().equals("AUTO-POST UPON APPROVAL"))
                            // {

                            this.executeInvAdjPost(invAdjustment.getAdjCode(), username, branchCode, companyCode);

                            // }

                            // adApprovalQueue.entityRemove();
                            em.remove(adApprovalQueue);

                        } else {

                            // looping up
                            i = adApprovalQueuesDesc.iterator();

                            while (i.hasNext()) {

                                LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                                if (adRemoveApprovalQueue.getAqUserOr() == (byte) 1) {

                                    i.remove();
                                    // adRemoveApprovalQueue.entityRemove();
                                    em.remove(adRemoveApprovalQueue);

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
                                        em.remove(adRemoveApprovalQueue);

                                        if (first) {
                                            first = false;
                                        }

                                    } else {

                                        break;
                                    }
                                }
                            }

                            // adApprovalQueue.entityRemove();
                            em.remove(adApprovalQueue);

                            Collection adRemoveApprovalQueues = adApprovalQueueHome
                                    .findByAqDocumentAndAqDocumentCode(approvalQueueDocument, approvalQueueDocumentCode, companyCode);

                            if (adRemoveApprovalQueues.size() == 0) {

                                invAdjustment.setAdjApprovalStatus("APPROVED");
                                invAdjustment.setAdjApprovedRejectedBy(username);
                                invAdjustment.setAdjDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                                // if (adPreference.getPrfInvGlPostingType().equals("AUTO-POST UPON APPROVAL"))
                                // {

                                this.executeInvAdjPost(invAdjustment.getAdjCode(), username, branchCode, companyCode);

                                // }

                            }
                        }

                    } else if (adApprovalQueue.getAqAndOr().equals("OR")) {

                        invAdjustment.setAdjApprovalStatus("APPROVED");
                        invAdjustment.setAdjApprovedRejectedBy(username);
                        invAdjustment.setAdjDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                        i = adApprovalQueues.iterator();

                        while (i.hasNext()) {

                            LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                            i.remove();
                            // adRemoveApprovalQueue.entityRemove();
                            em.remove(adRemoveApprovalQueue);
                        }

                        // if (adPreference.getPrfInvGlPostingType().equals("AUTO-POST UPON APPROVAL"))
                        // {

                        invAdjustment.setAdjPosted(EJBCommon.TRUE);
                        // this.executeInvAdjPost(invAdjustment.getAdjCode(), username, branchCode,
                        // companyCode);

                        // }

                    }

                } else if (!isApproved) {

                    invAdjustment.setAdjApprovalStatus(null);
                    invAdjustment.setAdjApprovedRejectedBy(username);
                    invAdjustment.setAdjDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());
                    invAdjustment.setAdjReasonForRejection(reasonForRejection);

                    i = adApprovalQueues.iterator();

                    while (i.hasNext()) {

                        LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                        i.remove();
                        // adRemoveApprovalQueue.entityRemove();
                        em.remove(adRemoveApprovalQueue);
                    }
                }
            } else if (approvalQueueDocument.equals("INV STOCK TRANSFER")) {

                LocalInvStockTransfer invStockTransfer = invStockTransferHome.findByPrimaryKey(approvalQueueDocumentCode);

                Collection invStockTransferLines = invStockTransfer.getInvStockTransferLines();

                Iterator i = invStockTransferLines.iterator();

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

                    // start date validation
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(
                                invStockTransfer.getStDate(), invStockTransferLine.getInvItem().getIiName(),
                                invLocFrom.getLocName(), branchCode, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invStockTransferLine.getInvItem().getIiName());
                        }

                        invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(
                                invStockTransfer.getStDate(), invStockTransferLine.getInvItem().getIiName(),
                                invLocTo.getLocName(), branchCode, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invStockTransferLine.getInvItem().getIiName());
                        }
                    }
                }

                if (isApproved) {

                    if (adApprovalQueue.getAqAndOr().equals("AND")) {

                        if (adApprovalQueues.size() == 1) {

                            invStockTransfer.setStApprovalStatus("APPROVED");
                            invStockTransfer.setStApprovedRejectedBy(username);
                            invStockTransfer.setStDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                            this.executeInvStPost(invStockTransfer.getStCode(), username, branchCode, companyCode);

                            // adApprovalQueue.entityRemove();
                            em.remove(adApprovalQueue);

                        } else {

                            // looping up
                            i = adApprovalQueuesDesc.iterator();

                            while (i.hasNext()) {

                                LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                                if (adRemoveApprovalQueue.getAqUserOr() == (byte) 1) {

                                    i.remove();
                                    // adRemoveApprovalQueue.entityRemove();
                                    em.remove(adRemoveApprovalQueue);

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
                                        em.remove(adRemoveApprovalQueue);

                                        if (first) {
                                            first = false;
                                        }

                                    } else {

                                        break;
                                    }
                                }
                            }

                            // adApprovalQueue.entityRemove();
                            em.remove(adApprovalQueue);

                            Collection adRemoveApprovalQueues = adApprovalQueueHome
                                    .findByAqDocumentAndAqDocumentCode(approvalQueueDocument, approvalQueueDocumentCode, companyCode);

                            if (adRemoveApprovalQueues.size() == 0) {

                                invStockTransfer.setStApprovalStatus("APPROVED");
                                invStockTransfer.setStApprovedRejectedBy(username);
                                invStockTransfer
                                        .setStDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                                this.executeInvStPost(invStockTransfer.getStCode(), username, branchCode, companyCode);
                            }
                        }

                    } else if (adApprovalQueue.getAqAndOr().equals("OR")) {

                        invStockTransfer.setStApprovalStatus("APPROVED");
                        invStockTransfer.setStApprovedRejectedBy(username);
                        invStockTransfer.setStDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                        i = adApprovalQueues.iterator();

                        while (i.hasNext()) {

                            LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                            i.remove();
                            // adRemoveApprovalQueue.entityRemove();
                            em.remove(adRemoveApprovalQueue);
                        }

                        this.executeInvStPost(invStockTransfer.getStCode(), username, branchCode, companyCode);
                    }

                } else if (!isApproved) {

                    invStockTransfer.setStApprovalStatus(null);
                    invStockTransfer.setStApprovedRejectedBy(username);
                    invStockTransfer.setStDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());
                    invStockTransfer.setStReasonForRejection(reasonForRejection);

                    i = adApprovalQueues.iterator();

                    while (i.hasNext()) {

                        LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                        i.remove();
                        // adRemoveApprovalQueue.entityRemove();
                        em.remove(adRemoveApprovalQueue);
                    }
                }

            } else if (approvalQueueDocument.equals("INV BRANCH STOCK TRANSFER")
                    || approvalQueueDocument.equals("INV BRANCH STOCK TRANSFER ORDER")) {

                LocalInvBranchStockTransfer invBranchStockTransfer = invBranchStockTransferHome
                        .findByPrimaryKey(approvalQueueDocumentCode);

                Iterator i = null;

                if (isApproved) {
                    Debug.print("STATUS: " + invBranchStockTransfer.getBstApprovalStatus());
                    if (invBranchStockTransfer.getBstApprovalStatus() == null) {

                        System.out
                                .println("invBranchStockTransfer.getBstCode(): " + invBranchStockTransfer.getBstCode());
                        this.executeInvBstPost(invBranchStockTransfer.getBstCode(), username, branchCode, companyCode);

                    } else {

                        if (adApprovalQueue.getAqAndOr().equals("AND")) {

                            if (adApprovalQueues.size() == 1) {

                                invBranchStockTransfer.setBstApprovalStatus("APPROVED");
                                invBranchStockTransfer.setBstApprovedRejectedBy(username);
                                invBranchStockTransfer
                                        .setBstDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                                this.executeInvBstPost(invBranchStockTransfer.getBstCode(), username, branchCode, companyCode);

                                // adApprovalQueue.entityRemove();
                                em.remove(adApprovalQueue);

                            } else {

                                // looping up
                                i = adApprovalQueuesDesc.iterator();

                                while (i.hasNext()) {

                                    LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                                    if (adRemoveApprovalQueue.getAqUserOr() == (byte) 1) {

                                        i.remove();
                                        // adRemoveApprovalQueue.entityRemove();
                                        em.remove(adRemoveApprovalQueue);

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
                                            em.remove(adRemoveApprovalQueue);

                                            if (first) {
                                                first = false;
                                            }

                                        } else {

                                            break;
                                        }
                                    }
                                }

                                // adApprovalQueue.entityRemove();
                                em.remove(adApprovalQueue);

                                Collection adRemoveApprovalQueues = adApprovalQueueHome
                                        .findByAqDocumentAndAqDocumentCode(approvalQueueDocument, approvalQueueDocumentCode, companyCode);

                                if (adRemoveApprovalQueues.size() == 0) {

                                    invBranchStockTransfer.setBstApprovalStatus("APPROVED");
                                    invBranchStockTransfer.setBstApprovedRejectedBy(username);
                                    invBranchStockTransfer
                                            .setBstDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                                    this.executeInvBstPost(invBranchStockTransfer.getBstCode(), username, branchCode,
                                            companyCode);
                                }
                            }

                        } else if (adApprovalQueue.getAqAndOr().equals("OR")) {

                            invBranchStockTransfer.setBstApprovalStatus("APPROVED");
                            invBranchStockTransfer.setBstApprovedRejectedBy(username);
                            invBranchStockTransfer
                                    .setBstDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                            i = adApprovalQueues.iterator();

                            while (i.hasNext()) {

                                LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                                i.remove();
                                // adRemoveApprovalQueue.entityRemove();
                                em.remove(adRemoveApprovalQueue);
                            }

                            this.executeInvBstPost(invBranchStockTransfer.getBstCode(), username, branchCode, companyCode);
                        }
                    }

                } else if (!isApproved) {

                    invBranchStockTransfer.setBstApprovalStatus(null);
                    invBranchStockTransfer.setBstApprovedRejectedBy(username);
                    invBranchStockTransfer.setBstDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());
                    invBranchStockTransfer.setBstReasonForRejection(reasonForRejection);

                    i = adApprovalQueues.iterator();

                    while (i.hasNext()) {

                        LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                        i.remove();
                        // adRemoveApprovalQueue.entityRemove();
                        em.remove(adRemoveApprovalQueue);
                    }
                }
            }

        }
        catch (GlobalRecordAlreadyDeletedException ex) {

            throw ex;

        }
        catch (GlJREffectiveDateNoPeriodExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlobalJournalNotBalanceException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlobalInventoryDateException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlobalBranchAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

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

    public short getGlFcPrecisionUnit(Integer companyCode) {

        Debug.print("InvApprovalControllerBean getGlFcPrecisionUnit");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods
    private ArrayList getInvBstByCriteria(HashMap criteria, Integer offset, Integer limit, String orderBy,
                                          Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("InvApprovalControllerBean getInvBstByCriteria");
        ArrayList list = new ArrayList();
        try {

            StringBuffer jbossQl = new StringBuffer();
            jbossQl.append("SELECT OBJECT(bst) FROM InvBranchStockTransfer bst WHERE ");

            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            String type = "";

            obj = new Object[criteriaSize];

            jbossQl.append(" bst.bstType='IN' ");
            jbossQl.append(" AND bst.bstApprovalStatus IS NULL ");
            jbossQl.append(" AND bst.bstPosted=0 ");
            jbossQl.append(" AND bst.bstVoid=0 ");
            jbossQl.append(" AND bst.bstAdBranch=" + branchCode + " AND bst.bstAdCompany=" + companyCode + " ");

            if (criteria.containsKey("documentNumberFrom")) {

                jbossQl.append(" AND bst.bstNumber>=?" + (ctr + 1) + " ");
                obj[ctr] = criteria.get("documentNumberFrom");
                ctr++;
            }

            if (criteria.containsKey("documentNumberTo")) {

                jbossQl.append(" AND bst.bstNumber<=?" + (ctr + 1) + " ");
                obj[ctr] = criteria.get("documentNumberTo");
                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {

                jbossQl.append(" AND bst.bstDate>=?" + (ctr + 1) + " ");
                obj[ctr] = criteria.get("dateFrom");
                ctr++;
            }

            if (criteria.containsKey("dateTo")) {

                jbossQl.append(" AND bst.bstDate<=?" + (ctr + 1) + " ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (orderBy.equals("DOCUMENT NUMBER")) {

                orderBy = "bst.bstNumber";
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY " + orderBy + ", bst.bstDate");

            } else {

                jbossQl.append("ORDER BY bst.bstDate");
            }

            for (int i = 0; i < criteriaSize; i++) {
                Debug.print("#" + i + " " + obj[i]);
            }

            Debug.print("jbossQl.toString(): " + jbossQl);

            Collection invBranchStockTransfers = invBranchStockTransferHome.getBstByCriteria(jbossQl.toString(), obj,
                    limit, offset);
            if (invBranchStockTransfers.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            Iterator i = invBranchStockTransfers.iterator();

            while (i.hasNext()) {

                LocalInvBranchStockTransfer invBranchStockTransfer = (LocalInvBranchStockTransfer) i.next();

                InvModBranchStockTransferDetails mdetails = new InvModBranchStockTransferDetails();
                mdetails.setBstCode(invBranchStockTransfer.getBstCode());
                mdetails.setBstType(invBranchStockTransfer.getBstType());
                mdetails.setBstDate(invBranchStockTransfer.getBstDate());
                mdetails.setBstNumber(invBranchStockTransfer.getBstNumber());
                mdetails.setBstTransferOutNumber(invBranchStockTransfer.getBstTransferOutNumber());

                mdetails.setBstBranchFrom(invBranchStockTransfer.getAdBranch().getBrName());
                mdetails.setBstBranchTo(this.getAdBrNameByBrCode(invBranchStockTransfer.getBstAdBranch()));
                mdetails.setBstAdBranch(invBranchStockTransfer.getBstAdBranch());
                list.add(mdetails);
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

    private String getAdBrNameByBrCode(Integer branchCode) {

        Debug.print("InvFindBranchStockTransferControllerBean getAdBrNameByBrCode");
        try {

            return adBranchHome.findByPrimaryKey(branchCode).getBrName();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    private void executeInvAdjPost(Integer adjustmentCode, String username, Integer branchCode, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException,
            GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException,
            GlobalJournalNotBalanceException, GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException,
            GlobalAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException,
            GlobalExpiryDateNotFoundException {

        Debug.print("InvAdjustmentPostControllerBean executeInvAdjPost");
        try {

            // validate if adjustment is already deleted

            LocalInvAdjustment invAdjustment = null;

            try {

                invAdjustment = invAdjustmentHome.findByPrimaryKey(adjustmentCode);

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

            // regenerate inventory dr

            this.regenerateInventoryDr(invAdjustment, branchCode, companyCode);

            Collection invAdjustmentLines = null;

            if (invAdjustment.getAdjVoid() == EJBCommon.FALSE) {
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.FALSE,
                        invAdjustment.getAdjCode(), companyCode);
            } else {
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.TRUE,
                        invAdjustment.getAdjCode(), companyCode);
            }

            Iterator c = invAdjustmentLines.iterator();

            while (c.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) c.next();

                String itemName = invAdjustmentLine.getInvItemLocation().getInvItem().getIiName();
                String locationName = invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName();

                double ADJUST_QTY = this.convertByUomFromAndItemAndQuantity(invAdjustmentLine.getInvUnitOfMeasure(),
                        invAdjustmentLine.getInvItemLocation().getInvItem(), invAdjustmentLine.getAlAdjustQuantity(),
                        companyCode);

                LocalInvCosting invCosting = null;

                try {

                    invCosting = invCostingHome
                            .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                    invAdjustment.getAdjDate(), itemName, locationName, branchCode, companyCode);

                }
                catch (FinderException ex) {

                }

                double COST = invAdjustmentLine.getAlUnitCost();

                if (invCosting == null) {

                    this.postToInv(invAdjustmentLine, invAdjustment.getAdjDate(), ADJUST_QTY, COST * ADJUST_QTY,
                            ADJUST_QTY, COST * ADJUST_QTY, 0d, null, branchCode, companyCode);

                } else {

                    this.postToInv(invAdjustmentLine, invAdjustment.getAdjDate(), ADJUST_QTY, COST * ADJUST_QTY,
                            invCosting.getCstRemainingQuantity() + ADJUST_QTY,
                            invCosting.getCstRemainingValue() + (COST * ADJUST_QTY), 0d, null, branchCode, companyCode);
                }
            }

            // set invoice post status

            invAdjustment.setAdjPosted(EJBCommon.TRUE);
            invAdjustment.setAdjPostedBy(username);
            invAdjustment.setAdjDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

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

                double distributionRecordAmount = 0d;

                distributionRecordAmount = invDistributionRecord.getDrAmount();

                if (invDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                    TOTAL_DEBIT += distributionRecordAmount;

                } else {

                    TOTAL_CREDIT += distributionRecordAmount;
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
                        "JOURNAL IMPORT " + formatter.format(new Date()) + " INVENTORY ADJUSTMENTS", branchCode,
                        companyCode);

            }
            catch (FinderException ex) {

            }

            if (glJournalBatch == null) {

                glJournalBatch = glJournalBatchHome.create(
                        "JOURNAL IMPORT " + formatter.format(new Date()) + " INVENTORY ADJUSTMENTS", "JOURNAL IMPORT",
                        "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), username, branchCode, companyCode);
            }

            // create journal entry

            LocalGlJournal glJournal = glJournalHome.create(invAdjustment.getAdjReferenceNumber(),
                    invAdjustment.getAdjDescription(), invAdjustment.getAdjDate(), 0.0d, null,
                    invAdjustment.getAdjDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE,
                    username, new Date(), username, new Date(), null, null, username,
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

                double distributionRecordAmount = 0d;

                distributionRecordAmount = invDistributionRecord.getDrAmount();

                LocalGlJournalLine glJournalLine = glJournalLineHome.create(invDistributionRecord.getDrLine(),
                        invDistributionRecord.getDrDebit(), distributionRecordAmount, "", companyCode);

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
            Iterator i = glJournalLines.iterator();

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

                Iterator acvsIter = glSubsequentAccountingCalendarValues.iterator();

                while (acvsIter.hasNext()) {

                    LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) acvsIter
                            .next();

                    this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false,
                            glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                }

                // post to subsequent years if necessary

                Collection glSubsequentSetOfBooks = glSetOfBookHome
                        .findSubsequentSobByAcYear(glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode);

                if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                    adCompany = adCompanyHome.findByPrimaryKey(companyCode);

                    LocalGlChartOfAccount glRetainedEarningsAccount = null;

                    try {

                        glRetainedEarningsAccount = glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(
                                adCompany.getCmpRetainedEarnings(), branchCode, companyCode);

                    }
                    catch (FinderException ex) {

                        throw new GlobalAccountNumberInvalidException();
                    }
                    Iterator sobIter = glSubsequentSetOfBooks.iterator();

                    while (sobIter.hasNext()) {

                        LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) sobIter.next();

                        String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                        // post to subsequent acvs of subsequent set of book(propagate)

                        Collection glAccountingCalendarValues = glAccountingCalendarValueHome
                                .findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                        Iterator acvIter = glAccountingCalendarValues.iterator();

                        while (acvIter.hasNext()) {

                            LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) acvIter
                                    .next();

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

        }
        catch (GlJREffectiveDateNoPeriodExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlobalJournalNotBalanceException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlobalRecordAlreadyDeletedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlobalTransactionAlreadyPostedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlobalInventoryDateException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlobalBranchAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlobalAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

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

    private void executeInvStPost(Integer stockTransferCode, String username, Integer branchCode, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException,
            GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException,
            GlobalJournalNotBalanceException, AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("InvApprovalControllerBean executeInvStPost");
        try {

            // validate if adjustment is already deleted

            LocalInvStockTransfer invStockTransfer = null;

            try {

                invStockTransfer = invStockTransferHome.findByPrimaryKey(stockTransferCode);

            }
            catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if adjustment is already posted or void

            if (invStockTransfer.getStPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();
            }

            // regenearte inventory dr

            this.regenerateInventoryDr(invStockTransfer, branchCode, companyCode);

            Iterator i = invStockTransfer.getInvStockTransferLines().iterator();

            while (i.hasNext()) {

                LocalInvStockTransferLine invStockTransferLine = (LocalInvStockTransferLine) i.next();

                String LOC_NM_FRM = invLocationHome.findByPrimaryKey(invStockTransferLine.getStlLocationFrom())
                        .getLocName();
                String LOC_NM_TO = invLocationHome.findByPrimaryKey(invStockTransferLine.getStlLocationTo())
                        .getLocName();

                LocalInvItemLocation invItemLocationFrom = invItemLocationHome.findByLocNameAndIiName(LOC_NM_FRM,
                        invStockTransferLine.getInvItem().getIiName(), companyCode);
                LocalInvItemLocation invItemLocationTo = invItemLocationHome.findByLocNameAndIiName(LOC_NM_TO,
                        invStockTransferLine.getInvItem().getIiName(), companyCode);

                double ST_COST = invStockTransferLine.getStlQuantityDelivered() * invStockTransferLine.getStlUnitCost();

                double ST_QTY = this.convertByUomFromAndItemAndQuantity(invStockTransferLine.getInvUnitOfMeasure(),
                        invStockTransferLine.getInvItem(), invStockTransferLine.getStlQuantityDelivered(), companyCode);

                String itemName = invStockTransferLine.getInvItem().getIiName();

                LocalInvCosting invCostingFrom = null;
                LocalInvCosting invCostingTo = null;

                try {
                    invCostingFrom = invCostingHome
                            .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                    invStockTransfer.getStDate(), itemName, LOC_NM_FRM, branchCode, companyCode);
                }
                catch (FinderException ex) {

                }

                try {
                    invCostingTo = invCostingHome
                            .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                    invStockTransfer.getStDate(), itemName, LOC_NM_TO, branchCode, companyCode);
                }
                catch (FinderException ex) {

                }

                if (invCostingFrom == null) {

                    this.post(invStockTransferLine, invItemLocationFrom, invStockTransfer.getStDate(), -ST_QTY,
                            -ST_COST, -ST_QTY, -ST_COST, 0d, null, branchCode, companyCode);

                    this.post(invStockTransferLine, invItemLocationTo, invStockTransfer.getStDate(), ST_QTY, ST_COST,
                            ST_QTY, ST_COST, 0d, null, branchCode, companyCode);

                } else {

                    if (invCostingFrom.getInvItemLocation().getInvItem().getIiCostMethod().equals("Average")) {

                        LocalInvCosting invLastCostingFrom = invCostingHome
                                .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                        invStockTransfer.getStDate(), itemName, LOC_NM_FRM, branchCode, companyCode);

                        double COST = Math
                                .abs(invCostingFrom.getCstRemainingValue() / invCostingFrom.getCstRemainingQuantity());

                        this.post(invStockTransferLine, invItemLocationFrom, invStockTransfer.getStDate(), -ST_QTY,
                                -ST_QTY * COST, invLastCostingFrom.getCstRemainingQuantity() - ST_QTY,
                                invLastCostingFrom.getCstRemainingValue() - (ST_QTY * COST), 0d, null, branchCode,
                                companyCode);

                        LocalInvCosting invLastCostingTo = null;

                        try {

                            invLastCostingTo = invCostingHome
                                    .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                            invStockTransfer.getStDate(), itemName, LOC_NM_TO, branchCode, companyCode);

                        }
                        catch (FinderException ex) {

                        }

                        if (invLastCostingTo == null) {

                            this.post(invStockTransferLine, invItemLocationTo, invStockTransfer.getStDate(), ST_QTY,
                                    ST_COST, ST_QTY, ST_COST, 0d, null, branchCode, companyCode);

                        } else {

                            // compute cost variance
                            double costVarianceValue = 0d;

                            if (invLastCostingTo.getCstRemainingQuantity() < 0) {
                                costVarianceValue = (invLastCostingTo.getCstRemainingQuantity() * (COST)
                                        - invLastCostingTo.getCstRemainingValue());
                            }

                            this.post(invStockTransferLine, invItemLocationTo, invStockTransfer.getStDate(), ST_QTY,
                                    ST_QTY * COST, invLastCostingTo.getCstRemainingQuantity() + ST_QTY,
                                    invLastCostingTo.getCstRemainingValue() + (ST_QTY * COST), costVarianceValue, username,
                                    branchCode, companyCode);
                        }

                    } else if (invCostingFrom.getInvItemLocation().getInvItem().getIiCostMethod().equals("FIFO")) {

                        LocalInvCosting invFifoCosting = invCostingHome
                                .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                        invStockTransfer.getStDate(), itemName, LOC_NM_FRM, branchCode, companyCode);

                        double fifoCost = this.getInvFifoCost(invFifoCosting.getCstDate(),
                                invFifoCosting.getInvItemLocation().getIlCode(), -ST_QTY,
                                invStockTransferLine.getStlUnitCost(), true, branchCode, companyCode);

                        // post entries to database
                        this.post(invStockTransferLine, invItemLocationFrom, invStockTransfer.getStDate(), -ST_QTY,
                                fifoCost * -ST_QTY, invFifoCosting.getCstRemainingQuantity() - ST_QTY,
                                invFifoCosting.getCstRemainingValue() - (fifoCost * ST_QTY), 0d, null, branchCode,
                                companyCode);

                        invFifoCosting = invCostingHome
                                .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                        invStockTransfer.getStDate(), itemName, LOC_NM_TO, branchCode, companyCode);

                        // compute cost variance
                        double costVarianceValue = 0d;

                        if (invFifoCosting.getCstRemainingQuantity() < 0) {
                            costVarianceValue = (invFifoCosting.getCstRemainingQuantity() * (fifoCost)
                                    - invFifoCosting.getCstRemainingValue());
                        }

                        this.post(invStockTransferLine, invItemLocationTo, invStockTransfer.getStDate(), ST_QTY,
                                ST_QTY * fifoCost, invFifoCosting.getCstRemainingQuantity() + ST_QTY,
                                invFifoCosting.getCstRemainingValue() + (ST_QTY * fifoCost), costVarianceValue, username,
                                branchCode, companyCode);
                    } else if (invCostingFrom.getInvItemLocation().getInvItem().getIiCostMethod().equals("Standard")) {
                        LocalInvCosting invStandardCosting = invCostingHome
                                .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                        invStockTransfer.getStDate(), itemName, LOC_NM_FRM, branchCode, companyCode);

                        double standardCost = invStandardCosting.getInvItemLocation().getInvItem().getIiUnitCost();

                        // post entries to database
                        this.post(invStockTransferLine, invItemLocationFrom, invStockTransfer.getStDate(), -ST_QTY,
                                standardCost * -ST_QTY, invStandardCosting.getCstRemainingQuantity() - ST_QTY,
                                invStandardCosting.getCstRemainingValue() - (standardCost * ST_QTY), 0d, null, branchCode,
                                companyCode);

                        invStandardCosting = invCostingHome
                                .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                        invStockTransfer.getStDate(), itemName, LOC_NM_TO, branchCode, companyCode);

                        // compute cost variance
                        double costVarianceValue = 0d;

                        if (invStandardCosting.getCstRemainingQuantity() < 0) {
                            costVarianceValue = (invStandardCosting.getCstRemainingQuantity() * (standardCost)
                                    - invStandardCosting.getCstRemainingValue());
                        }

                        this.post(invStockTransferLine, invItemLocationTo, invStockTransfer.getStDate(), ST_QTY,
                                ST_QTY * standardCost, invStandardCosting.getCstRemainingQuantity() + ST_QTY,
                                invStandardCosting.getCstRemainingValue() + (ST_QTY * standardCost), costVarianceValue,
                                username, branchCode, companyCode);
                    }
                }
            }

            // set invoice post status

            invStockTransfer.setStPosted(EJBCommon.TRUE);
            invStockTransfer.setStPostedBy(username);
            invStockTransfer.setStDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            // post to GL
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

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

                double distributionRecordAmount = 0d;

                distributionRecordAmount = invDistributionRecord.getDrAmount();

                if (invDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                    TOTAL_DEBIT += distributionRecordAmount;

                } else {

                    TOTAL_CREDIT += distributionRecordAmount;
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
                        "JOURNAL IMPORT " + formatter.format(new Date()) + " STOCK TRANSFERS", branchCode, companyCode);

            }
            catch (FinderException ex) {

            }

            if (glJournalBatch == null) {

                glJournalBatch = glJournalBatchHome.create(
                        "JOURNAL IMPORT " + formatter.format(new Date()) + " STOCK TRANSFERS", "JOURNAL IMPORT",
                        "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), username, branchCode, companyCode);
            }

            // create journal entry

            LocalGlJournal glJournal = glJournalHome.create(invStockTransfer.getStReferenceNumber(),
                    invStockTransfer.getStDescription(), invStockTransfer.getStDate(), 0.0d, null,
                    invStockTransfer.getStDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE,
                    username, new Date(), username, new Date(), null, null, username,
                    EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, EJBCommon.FALSE, null, branchCode,
                    companyCode);

            LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("INVENTORY", companyCode);
            glJournal.setGlJournalSource(glJournalSource);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome
                    .findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
            glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("STOCK TRANSFERS", companyCode);
            glJournal.setGlJournalCategory(glJournalCategory);

            if (glJournalBatch != null) {

                glJournal.setGlJournalBatch(glJournalBatch);
            }

            // create journal lines

            i = invDistributionRecords.iterator();

            while (i.hasNext()) {

                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) i.next();

                double distributionRecordAmount = 0d;

                distributionRecordAmount = invDistributionRecord.getDrAmount();

                LocalGlJournalLine glJournalLine = glJournalLineHome.create(invDistributionRecord.getDrLine(),
                        invDistributionRecord.getDrDebit(), distributionRecordAmount, "", companyCode);

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

                Iterator acvsIter = glSubsequentAccountingCalendarValues.iterator();

                while (acvsIter.hasNext()) {

                    LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) acvsIter
                            .next();

                    this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false,
                            glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                }

                // post to subsequent years if necessary

                Collection glSubsequentSetOfBooks = glSetOfBookHome
                        .findSubsequentSobByAcYear(glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode);

                if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                    adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                    LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome
                            .findByCoaAccountNumberAndBranchCode(adCompany.getCmpRetainedEarnings(), branchCode,
                                    companyCode);

                    Iterator sobIter = glSubsequentSetOfBooks.iterator();

                    while (sobIter.hasNext()) {

                        LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) sobIter.next();

                        String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                        // post to subsequent acvs of subsequent set of book(propagate)

                        Collection glAccountingCalendarValues = glAccountingCalendarValueHome
                                .findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                        Iterator acvIter = glAccountingCalendarValues.iterator();

                        while (acvIter.hasNext()) {

                            LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) acvIter
                                    .next();

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
        catch (GlJREffectiveDateNoPeriodExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlobalJournalNotBalanceException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlobalRecordAlreadyDeletedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlobalTransactionAlreadyPostedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

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

    private double convertByUomAndQuantityBst(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem,
                                              double ADJST_QTY, Integer companyCode) {

        Debug.print("InvApprovalControllerBean convertByUomFromAndUomToAndQuantity");
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

    private double getBstInvIiUnitCostByIiNameAndLocFromAndUomNameAndDate(String itemName, String locationFrom, String unitOfMeasureName,
                                                                          Date stockTransferDate, String branchName, Integer companyCode) {

        Debug.print("InvApprovalControllerBean getInvIiUnitCostByIiNameAndUomName");
        try {

            LocalAdBranch adBranch = adBranchHome.findByBrName(branchName, companyCode);

            LocalInvItem invItem = invItemHome.findByIiName(itemName, companyCode);

            double COST = invItem.getIiUnitCost();

            LocalInvItemLocation invItemLocation = null;

            try {

                invItemLocation = invItemLocationHome.findByLocNameAndIiName(locationFrom, itemName, companyCode);

                LocalInvCosting invCosting = invCostingHome
                        .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(stockTransferDate,
                                invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(),
                                adBranch.getBrCode(), companyCode);

                if (invItemLocation.getInvItem().getIiCostMethod().equals("Average")) {
                    COST = EJBCommon.roundIt(
                            Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity()),
                            this.getGlFcPrecisionUnit(companyCode));
                } else if (invItemLocation.getInvItem().getIiCostMethod().equals("FIFO")) {
                    COST = this.getInvFifoCost(stockTransferDate, invItemLocation.getIlCode(), invCosting.getCstAdjustQuantity(),
                            invCosting.getCstAdjustCost(), false, adBranch.getBrCode(), companyCode);
                } else if (invItemLocation.getInvItem().getIiCostMethod().equals("Standard")) {
                    COST = invItemLocation.getInvItem().getIiUnitCost();
                }

            }
            catch (FinderException ex) {

            }

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(itemName, unitOfMeasureName, companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(itemName, invItem.getInvUnitOfMeasure().getUomName(), companyCode);

            return EJBCommon.roundIt(COST * invDefaultUomConversion.getUmcConversionFactor()
                    / invUnitOfMeasureConversion.getUmcConversionFactor(), this.getGlFcPrecisionUnit(companyCode));

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void executeInvBstPost(Integer branchStockTransferCode, String username, Integer branchCode, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException,
            GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException,
            GlobalJournalNotBalanceException, AdPRFCoaGlVarianceAccountNotFoundException,
            GlobalExpiryDateNotFoundException, GlobalBranchAccountNumberInvalidException {

        Debug.print("InvApprovalControllerBean executeInvBstPost");
        try {

            // validate if branch stock transfer is already deleted

            LocalInvBranchStockTransfer invBranchStockTransfer = null;

            try {

                invBranchStockTransfer = invBranchStockTransferHome.findByPrimaryKey(branchStockTransferCode);

            }
            catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if branch stock transfer is already posted or void

            if (invBranchStockTransfer.getBstPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();
            }

            // regenerate inventory dr

            Integer BRNCH_FRM = invBranchStockTransfer.getAdBranch().getBrCode();

            Iterator i = invBranchStockTransfer.getInvBranchStockTransferLines().iterator();
            Debug.print("InvBranchStockTransferInEntryControllerBean executeInvBstPost A");
            while (i.hasNext()) {

                LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) i.next();

                String locName = invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName();
                String invItemName = invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName();
                String transitLocationName = invBranchStockTransfer.getInvLocation().getLocName();

                LocalInvItemLocation invItemLocationFrom = invItemLocationHome.findByLocNameAndIiName(locName,
                        invItemName, companyCode);

                LocalInvItemLocation invItemTransitLocation = invItemLocationHome
                        .findByLocNameAndIiName(transitLocationName, invItemName, companyCode);

                double BST_COST = invBranchStockTransferLine.getBslQuantityReceived()
                        * invBranchStockTransferLine.getBslUnitCost();

                LocalAdBranch adBranchFrom = invBranchStockTransfer.getAdBranch();

                double TRANSIT_COST = this.getBstInvIiUnitCostByIiNameAndLocFromAndUomNameAndDate(invItemName,
                        transitLocationName, invBranchStockTransferLine.getInvUnitOfMeasure().getUomName(),
                        invBranchStockTransfer.getBstDate(), adBranchFrom.getBrName(), companyCode)
                        * invBranchStockTransferLine.getBslQuantityReceived();

                double BST_QTY = this.convertByUomAndQuantityBst(invBranchStockTransferLine.getInvUnitOfMeasure(),
                        invBranchStockTransferLine.getInvItemLocation().getInvItem(),
                        invBranchStockTransferLine.getBslQuantityReceived(), companyCode);
                Debug.print("InvBranchStockTransferInEntryControllerBean executeInvBstPost A01");

                LocalInvCosting invCosting = null;
                LocalInvCosting invTransitCosting = null;

                try {
                    invCosting = invCostingHome
                            .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                    invBranchStockTransfer.getBstDate(), invItemName, locName, branchCode, companyCode);
                    Debug.print("InvBranchStockTransferInEntryControllerBean executeInvBstPost A02");
                }
                catch (FinderException ex) {

                }

                LocalInvCosting invLastCosting = null;

                Debug.print("InvBranchStockTransferInEntryControllerBean executeInvBstPost B");
                if (invCosting == null) {

                    this.postBstl(invBranchStockTransferLine, invItemLocationFrom, invBranchStockTransfer.getBstDate(),
                            BST_QTY, BST_COST, BST_QTY, BST_COST, 0d, null, branchCode, companyCode);
                    Debug.print("InvBranchStockTransferInEntryControllerBean executeInvBstPost B01");
                } else if (invCosting != null) {

                    invLastCosting = invCostingHome
                            .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                    invBranchStockTransfer.getBstDate(), invItemName, locName, branchCode, companyCode);
                    Debug.print("InvBranchStockTransferInEntryControllerBean executeInvBstPost B02");
                    // compute cost variance
                    double costVarianceValue = 0d;

                    if (invLastCosting.getCstRemainingQuantity() < 0
                            && invBranchStockTransferLine.getBslQuantityReceived() > 0) {
                        costVarianceValue = (invLastCosting.getCstRemainingQuantity()
                                * (BST_COST / invBranchStockTransferLine.getBslQuantityReceived())
                                - invLastCosting.getCstRemainingValue());
                    }

                    if (invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiCostMethod()
                            .equals("Average")) {

                        this.postBstl(invBranchStockTransferLine, invItemLocationFrom,
                                invBranchStockTransfer.getBstDate(), BST_QTY, BST_COST,
                                invLastCosting.getCstRemainingQuantity() + BST_QTY,
                                invLastCosting.getCstRemainingValue() + BST_COST, costVarianceValue, username, branchCode,
                                companyCode);
                        Debug.print("InvBranchStockTransferInEntryControllerBean executeInvBstPost B03");

                    } else if (invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiCostMethod()
                            .equals("FIFO")) {

                        LocalInvCosting invFifoCosting = invCostingHome
                                .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                        invBranchStockTransferLine.getInvBranchStockTransfer().getBstDate(),
                                        invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(),
                                        invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName(),
                                        branchCode, companyCode);

                        double fifoCost = this.getInvFifoCost(invFifoCosting.getCstDate(),
                                invFifoCosting.getInvItemLocation().getIlCode(), BST_QTY,
                                invBranchStockTransferLine.getBslUnitCost(), true, branchCode, companyCode);

                        // post entries to database
                        this.postBstl(invBranchStockTransferLine, invItemTransitLocation,
                                invBranchStockTransfer.getBstDate(), BST_QTY, fifoCost * BST_QTY,
                                invFifoCosting.getCstRemainingQuantity() + BST_QTY,
                                invFifoCosting.getCstRemainingValue() + (fifoCost * BST_QTY), costVarianceValue, username,
                                branchCode, companyCode);

                    } else if (invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiCostMethod()
                            .equals("Standard")) {

                        LocalInvCosting invStandardCosting = invCostingHome
                                .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                        invBranchStockTransferLine.getInvBranchStockTransfer().getBstDate(),
                                        invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(),
                                        invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName(),
                                        branchCode, companyCode);

                        double standardCost = invStandardCosting.getInvItemLocation().getInvItem().getIiUnitCost();

                        // post entries to database
                        this.postBstl(invBranchStockTransferLine, invItemTransitLocation,
                                invBranchStockTransfer.getBstDate(), BST_QTY, standardCost * BST_QTY,
                                invStandardCosting.getCstRemainingQuantity() + BST_QTY,
                                invStandardCosting.getCstRemainingValue() + (standardCost * BST_QTY), costVarianceValue,
                                username, branchCode, companyCode);
                    }
                }
                Debug.print("InvBranchStockTransferInEntryControllerBean executeInvBstPost C");
                try {
                    invTransitCosting = invCostingHome
                            .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                    invBranchStockTransfer.getBstDate(), invItemName, transitLocationName, BRNCH_FRM,
                                    companyCode);
                    Debug.print("InvBranchStockTransferInEntryControllerBean executeInvBstPost C01");
                }
                catch (FinderException ex) {

                }

                if (invTransitCosting == null) {

                    this.postBstl(invBranchStockTransferLine, invItemTransitLocation,
                            invBranchStockTransfer.getBstDate(), -BST_QTY, -TRANSIT_COST, -BST_QTY, -TRANSIT_COST, 0d,
                            null, BRNCH_FRM, companyCode);
                    Debug.print("InvBranchStockTransferInEntryControllerBean executeInvBstPost C02");
                } else if (invTransitCosting != null) {

                    if (invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiCostMethod()
                            .equals("Average")) {

                        invLastCosting = invCostingHome
                                .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                        invBranchStockTransfer.getBstDate(), invItemName, transitLocationName,
                                        BRNCH_FRM, companyCode);

                        this.postBstl(invBranchStockTransferLine, invItemTransitLocation,
                                invBranchStockTransfer.getBstDate(), -BST_QTY, -TRANSIT_COST,
                                invLastCosting.getCstRemainingQuantity() - BST_QTY,
                                invLastCosting.getCstRemainingValue() - TRANSIT_COST, 0d, null, BRNCH_FRM, companyCode);
                        Debug.print("InvBranchStockTransferInEntryControllerBean executeInvBstPost C03");

                    } else if (invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiCostMethod()
                            .equals("FIFO")) {
                        LocalInvCosting invFifoCosting = invCostingHome
                                .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                        invBranchStockTransferLine.getInvBranchStockTransfer().getBstDate(),
                                        invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(),
                                        invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName(),
                                        branchCode, companyCode);

                        double fifoCost = this.getInvFifoCost(invFifoCosting.getCstDate(),
                                invFifoCosting.getInvItemLocation().getIlCode(), -BST_QTY,
                                invBranchStockTransferLine.getBslUnitCost(), true, branchCode, companyCode);

                        // post entries to database
                        this.postBstl(invBranchStockTransferLine, invItemTransitLocation,
                                invBranchStockTransfer.getBstDate(), -BST_QTY, fifoCost * -BST_QTY,
                                invFifoCosting.getCstRemainingQuantity() + -BST_QTY,
                                invFifoCosting.getCstRemainingValue() + (fifoCost * -BST_QTY), 0d, null, BRNCH_FRM,
                                companyCode);

                        Debug.print("2");
                        Debug.print("CST Code : " + invFifoCosting.getCstCode());
                        Debug.print("CST Remaining Qty : " + invFifoCosting.getCstRemainingQuantity());
                        Debug.print("BST Qty : " + (-BST_QTY));
                        Debug.print("CST Remaining Value : " + invFifoCosting.getCstRemainingValue());
                        Debug.print("BST Value : " + (fifoCost + -BST_QTY));

                    } else if (invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiCostMethod()
                            .equals("Standard")) {
                        LocalInvCosting invStandardCosting = invCostingHome
                                .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                        invBranchStockTransferLine.getInvBranchStockTransfer().getBstDate(),
                                        invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(),
                                        invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName(),
                                        branchCode, companyCode);

                        double standardCost = invStandardCosting.getInvItemLocation().getInvItem().getIiUnitCost();

                        // post entries to database
                        this.postBstl(invBranchStockTransferLine, invItemTransitLocation,
                                invBranchStockTransfer.getBstDate(), -BST_QTY, standardCost * -BST_QTY,
                                invStandardCosting.getCstRemainingQuantity() + -BST_QTY,
                                invStandardCosting.getCstRemainingValue() + (standardCost * -BST_QTY), 0d, null,
                                BRNCH_FRM, companyCode);
                    }
                }
            }

            // set branch stock transfer post status

            invBranchStockTransfer.setBstPosted(EJBCommon.TRUE);
            invBranchStockTransfer.setBstPostedBy(username);
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

                double distributionRecordAmount = 0d;

                distributionRecordAmount = invDistributionRecord.getDrAmount();

                if (invDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                    TOTAL_DEBIT += distributionRecordAmount;

                } else {

                    TOTAL_CREDIT += distributionRecordAmount;
                }
            }

            TOTAL_DEBIT = EJBCommon.roundIt(TOTAL_DEBIT, adCompany.getGlFunctionalCurrency().getFcPrecision());
            TOTAL_CREDIT = EJBCommon.roundIt(TOTAL_CREDIT, adCompany.getGlFunctionalCurrency().getFcPrecision());
            Debug.print("InvBranchStockTransferInEntryControllerBean executeInvBstPost D");
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
            Debug.print("InvBranchStockTransferInEntryControllerBean executeInvBstPost E");
            try {

                glJournalBatch = glJournalBatchHome.findByJbName(
                        "JOURNAL IMPORT " + formatter.format(new Date()) + " BRANCH STOCK TRANSFERS", branchCode,
                        companyCode);

            }
            catch (FinderException ex) {

            }

            if (glJournalBatch == null) {

                glJournalBatch = glJournalBatchHome.create(
                        "JOURNAL IMPORT " + formatter.format(new Date()) + " BRANCH STOCK TRANSFERS", "JOURNAL IMPORT",
                        "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), username, branchCode, companyCode);
            }

            // create journal entry

            LocalGlJournal glJournal = glJournalHome.create(invBranchStockTransfer.getBstNumber(),
                    invBranchStockTransfer.getBstDescription(), invBranchStockTransfer.getBstDate(), 0.0d, null,
                    invBranchStockTransfer.getBstNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE,
                    username, new Date(), username, new Date(), null, null, username,
                    EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, EJBCommon.FALSE, null, branchCode,
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
            Debug.print("InvBranchStockTransferInEntryControllerBean executeInvBstPost E");
            i = invDistributionRecords.iterator();

            while (i.hasNext()) {

                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) i.next();

                double distributionRecordAmount = 0d;

                distributionRecordAmount = invDistributionRecord.getDrAmount();

                LocalGlJournalLine glJournalLine = glJournalLineHome.create(invDistributionRecord.getDrLine(),
                        invDistributionRecord.getDrDebit(), distributionRecordAmount, "", companyCode);

                // invDistributionRecord.getInvChartOfAccount().addGlJournalLine(glJournalLine);
                glJournalLine.setGlChartOfAccount(invDistributionRecord.getInvChartOfAccount());

                // glJournal.addGlJournalLine(glJournalLine);
                glJournalLine.setGlJournal(glJournal);

                invDistributionRecord.setDrImported(EJBCommon.TRUE);
            }
            Debug.print("InvBranchStockTransferInEntryControllerBean executeInvBstPost E");
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
                Debug.print("InvBranchStockTransferInEntryControllerBean executeInvBstPost E01");
                // post to subsequent acvs (propagate)

                Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome
                        .findSubsequentAcvByAcCodeAndAcvPeriodNumber(
                                glJournalSetOfBook.getGlAccountingCalendar().getAcCode(),
                                glAccountingCalendarValue.getAcvPeriodNumber(), companyCode);

                Iterator acvsIter = glSubsequentAccountingCalendarValues.iterator();

                while (acvsIter.hasNext()) {

                    LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) acvsIter
                            .next();

                    this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false,
                            glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                    Debug.print("InvBranchStockTransferInEntryControllerBean executeInvBstPost E02");
                }

                // post to subsequent years if necessary

                Collection glSubsequentSetOfBooks = glSetOfBookHome
                        .findSubsequentSobByAcYear(glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode);

                if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                    adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                    LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome
                            .findByCoaAccountNumberAndBranchCode(adCompany.getCmpRetainedEarnings(), branchCode,
                                    companyCode);

                    Iterator sobIter = glSubsequentSetOfBooks.iterator();

                    while (sobIter.hasNext()) {

                        LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) sobIter.next();

                        String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                        // post to subsequent acvs of subsequent set of book(propagate)

                        Collection glAccountingCalendarValues = glAccountingCalendarValueHome
                                .findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                        Iterator acvIter = glAccountingCalendarValues.iterator();

                        while (acvIter.hasNext()) {

                            LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) acvIter
                                    .next();

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
            Debug.print("InvBranchStockTransferInEntryControllerBean executeInvBstPost E Done");
        }
        catch (GlJREffectiveDateNoPeriodExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlobalJournalNotBalanceException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlobalRecordAlreadyDeletedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlobalTransactionAlreadyPostedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

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

    private double getInvFifoCost(Date costDate, Integer itemLocationCode, double costQuantity, double costCost, boolean isAdjustFifo,
                                  Integer branchCode, Integer companyCode) {

        try {

            Collection invFifoCostings = invCostingHome
                    .findFifoRemainingQuantityByLessThanOrEqualCstDateAndIlCodeAndBrCode(costDate, itemLocationCode, branchCode,
                            companyCode);

            if (invFifoCostings.size() > 0) {

                Iterator x = invFifoCostings.iterator();

                if (isAdjustFifo) {

                    // executed during POST transaction

                    double totalCost = 0d;
                    double cost;

                    if (costQuantity < 0) {

                        // for negative quantities
                        double neededQty = -(costQuantity);

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

                            LocalInvItemLocation invItemLocation = invItemLocationHome.findByPrimaryKey(itemLocationCode);
                            totalCost += (neededQty * invItemLocation.getInvItem().getIiUnitCost());
                        }

                        cost = totalCost / -costQuantity;
                    } else {

                        // for positive quantities
                        cost = costCost;
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
                LocalInvItemLocation invItemLocation = invItemLocationHome.findByPrimaryKey(itemLocationCode);
                return invItemLocation.getInvItem().getIiUnitCost();
            }

        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
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
        }
        catch (Exception e) {

        }

        if (miscList2 == "") {
            miscList = miscList + "$";
        } else {
            miscList = miscList2;
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

        String miscList = "";

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
        Debug.print("miscList :" + miscList);
        return (miscList);
    }

    private void post(LocalInvStockTransferLine invStockTransferLine, LocalInvItemLocation invItemLocation, Date costDate,
                      double stockTransferQuantity, double stockTransferCost, double costRemainingQuantity, double costRemainingValue, double costVarianceValue,
                      String username, Integer branchCode, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("InvApprovalControllerBean post");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            int CST_LN_NMBR = 0;

            stockTransferQuantity = EJBCommon.roundIt(stockTransferQuantity, adPreference.getPrfInvQuantityPrecisionUnit());
            stockTransferCost = EJBCommon.roundIt(stockTransferCost, adCompany.getGlFunctionalCurrency().getFcPrecision());
            costRemainingQuantity = EJBCommon.roundIt(costRemainingQuantity, adPreference.getPrfInvQuantityPrecisionUnit());
            costRemainingValue = EJBCommon.roundIt(costRemainingValue, adCompany.getGlFunctionalCurrency().getFcPrecision());

            if (stockTransferQuantity < 0) {

                invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - Math.abs(stockTransferQuantity));
            }

            try {

                // generate line number
                LocalInvCosting invCurrentCosting = invCostingHome
                        .getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(costDate.getTime(),
                                invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(),
                                branchCode, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            }
            catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            // void subsequent cost variance adjustments
            Collection invAdjustmentLines = invAdjustmentLineHome
                    .findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(costDate,
                            invItemLocation.getIlCode(), branchCode, companyCode);
            Iterator i = invAdjustmentLines.iterator();

            while (i.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();
                this.voidInvAdjustment(invAdjustmentLine.getInvAdjustment(), branchCode, companyCode);
            }

            String prevExpiryDates = "";
            String miscListPrpgt = "";
            double qtyPrpgt = 0;
            try {
                LocalInvCosting prevCst = invCostingHome
                        .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndRemainingQuantityNotEqualToZeroAndIlCode(
                                costDate, invItemLocation.getIlCode(), branchCode, companyCode);

                prevExpiryDates = prevCst.getCstExpiryDate();
                qtyPrpgt = prevCst.getCstRemainingQuantity();

                if (prevExpiryDates == null) {
                    prevExpiryDates = "";
                }

            }
            catch (Exception ex) {

            }

            // create costing
            LocalInvCosting invCosting = invCostingHome.create(costDate, costDate.getTime(), CST_LN_NMBR, 0d, 0d,
                    stockTransferQuantity, stockTransferCost, 0d, 0d, costRemainingQuantity, costRemainingValue, 0d, 0d,
                    stockTransferQuantity > 0 ? stockTransferQuantity : 0, branchCode, companyCode);

            invItemLocation.addInvCosting(invCosting);
            invCosting.setInvStockTransferLine(invStockTransferLine);

            invCosting.setCstQuantity(stockTransferQuantity);
            invCosting.setCstCost(stockTransferCost);

            if (prevExpiryDates != null && prevExpiryDates != "" && prevExpiryDates.length() != 0) {
                Debug.print("apPurchaseOrderLine.getPlMisc(): " + invStockTransferLine.getStlMisc().length());

                if (invStockTransferLine.getStlMisc() != null && invStockTransferLine.getStlMisc() != ""
                        && invStockTransferLine.getStlMisc().length() != 0) {
                    int qty2Prpgt = Integer.parseInt(this.getQuantityExpiryDates(invStockTransferLine.getStlMisc()));

                    String miscList2Prpgt = this.propagateExpiryDates(invStockTransferLine.getStlMisc(), qty2Prpgt,
                            "False");
                    ArrayList miscList = this.expiryDates(invStockTransferLine.getStlMisc(), qty2Prpgt);
                    String propagateMiscPrpgt = "";
                    String ret = "";
                    String check = "";
                    Debug.print("stockTransferQuantity Before Trans: " + stockTransferQuantity);
                    // ArrayList miscList2 = null;
                    if (stockTransferQuantity > 0) {
                        prevExpiryDates = prevExpiryDates.substring(1);
                        propagateMiscPrpgt = miscList2Prpgt + prevExpiryDates;
                    } else {
                        Iterator mi = miscList.iterator();
                        propagateMiscPrpgt = prevExpiryDates;
                        ret = propagateMiscPrpgt;
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
                        }
                        // propagateMiscPrpgt = propagateMiscPrpgt.replace(" ", "$");
                        propagateMiscPrpgt = ret;
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
                    int initialQty = Integer.parseInt(this.getQuantityExpiryDates(invStockTransferLine.getStlMisc()));
                    String initialPrpgt = this.propagateExpiryDates(invStockTransferLine.getStlMisc(), initialQty,
                            "False");

                    invCosting.setCstExpiryDate(initialPrpgt);
                } else {
                    invCosting.setCstExpiryDate(prevExpiryDates);
                    Debug.print("prevExpiryDates");
                }
            }

            // if cost variance is not 0, generate cost variance for the transaction
            if (costVarianceValue != 0) {

                this.generateCostVariance(invCosting.getInvItemLocation(), costVarianceValue,
                        "INVST" + invStockTransferLine.getInvStockTransfer().getStDocumentNumber(),
                        invStockTransferLine.getInvStockTransfer().getStDescription(),
                        invStockTransferLine.getInvStockTransfer().getStDate(), username, branchCode, companyCode);
            }

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(costDate,
                    invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode,
                    companyCode);

            Debug.print("itemLocationCode : " + invItemLocation.getIlCode());
            Debug.print("costDate : " + costDate);
            Debug.print("itemName : " + invItemLocation.getInvItem().getIiName());
            Debug.print("locationName : " + invItemLocation.getInvLocation().getLocName());
            Debug.print("(3853) size : " + invCostings.size());

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
            String ret = "";
            String Checker = "";

            while (i.hasNext()) {

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) i.next();

                invPropagatedCosting
                        .setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + stockTransferQuantity);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + stockTransferCost);

                if (invStockTransferLine.getStlMisc() != null && invStockTransferLine.getStlMisc() != ""
                        && invStockTransferLine.getStlMisc().length() != 0) {
                    if (stockTransferQuantity > 0) {
                        miscList = miscList.substring(1);
                        propagateMisc = invPropagatedCosting.getCstExpiryDate() + miscList;
                        Debug.print("propagateMiscPrpgt : " + propagateMisc);
                    } else {
                        Iterator mi = miscList2.iterator();

                        propagateMisc = prevExpiryDates;
                        ret = propagateMisc;
                        while (mi.hasNext()) {
                            String miscStr = (String) mi.next();
                            ArrayList miscList3 = this.expiryDates("$" + ret, qtyPrpgt);
                            Iterator m2 = miscList3.iterator();
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
                        }
                        propagateMisc = ret;
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

            // regenerate cost varaince
            this.regenerateCostVariance(invCostings, invCosting, branchCode, companyCode);

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

    private void postBstl(LocalInvBranchStockTransferLine invBranchStockTransferLine,
                          LocalInvItemLocation invItemLocation, Date costDate, double stockTransferQuantity, double stockTransferCost,
                          double costRemainingQuantity, double costRemainingValue, double costVarianceValue, String username, Integer branchCode,
                          Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("InvBranchStockTransferInEntryControllerBean post");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            int CST_LN_NMBR = 0;

            stockTransferQuantity = EJBCommon.roundIt(stockTransferQuantity, adPreference.getPrfInvQuantityPrecisionUnit());
            stockTransferCost = EJBCommon.roundIt(stockTransferCost, adCompany.getGlFunctionalCurrency().getFcPrecision());
            costRemainingQuantity = EJBCommon.roundIt(costRemainingQuantity, adPreference.getPrfInvQuantityPrecisionUnit());
            costRemainingValue = EJBCommon.roundIt(costRemainingValue, adCompany.getGlFunctionalCurrency().getFcPrecision());

            if (stockTransferQuantity < 0) {

                invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - Math.abs(stockTransferQuantity));
            }

            try {

                // generate line number

                LocalInvCosting invCurrentCosting = invCostingHome
                        .getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(costDate.getTime(),
                                invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(),
                                branchCode, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            }
            catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            // void subsequent cost variance adjustments
            Collection invAdjustmentLines = invAdjustmentLineHome
                    .findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(costDate,
                            invItemLocation.getIlCode(), branchCode, companyCode);
            Iterator i = invAdjustmentLines.iterator();

            while (i.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();
                this.voidInvAdjustment(invAdjustmentLine.getInvAdjustment(), branchCode, companyCode);
            }

            String prevExpiryDates = "";
            String miscListPrpgt = "";
            double qtyPrpgt = 0;
            double qtyPrpgt2 = 0;
            try {
                LocalInvCosting prevCst = invCostingHome
                        .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndRemainingQuantityNotEqualToZeroAndIlCode(
                                costDate, invItemLocation.getIlCode(), branchCode, companyCode);
                prevExpiryDates = prevCst.getCstExpiryDate();
                qtyPrpgt = prevCst.getCstRemainingQuantity();
                if (prevExpiryDates == null) {
                    prevExpiryDates = "";
                }

            }
            catch (Exception ex) {

            }

            // create costing
            LocalInvCosting invCosting = invCostingHome.create(costDate, costDate.getTime(), CST_LN_NMBR, 0d, 0d,
                    stockTransferQuantity, stockTransferCost, 0d, 0d, costRemainingQuantity, costRemainingValue, 0d, 0d,
                    stockTransferQuantity > 0 ? stockTransferQuantity : 0, branchCode, companyCode);
            // invItemLocation.addInvCosting(invCosting);
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setInvBranchStockTransferLine(invBranchStockTransferLine);

            invCosting.setCstQuantity(stockTransferQuantity);
            invCosting.setCstCost(stockTransferCost);

            // Get Latest Expiry Dates

            // if cost variance is not 0, generate cost variance for the transaction
            if (costVarianceValue != 0) {

                this.generateCostVariance(invCosting.getInvItemLocation(), costVarianceValue,
                        "INVBST" + invBranchStockTransferLine.getInvBranchStockTransfer().getBstNumber(),
                        invBranchStockTransferLine.getInvBranchStockTransfer().getBstDescription(),
                        invBranchStockTransferLine.getInvBranchStockTransfer().getBstDate(), username, branchCode,
                        companyCode);
            }

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(costDate,
                    invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode,
                    companyCode);

            i = invCostings.iterator();

            String miscList = "";
            ArrayList miscList2 = null;
            if (invBranchStockTransferLine.getBslMisc() != null && invBranchStockTransferLine.getBslMisc() != ""
                    && invBranchStockTransferLine.getBslMisc().length() != 0) {
                double qty = Double.parseDouble(this.getQuantityExpiryDates(invBranchStockTransferLine.getBslMisc()));
                miscList = this.propagateExpiryDates(invBranchStockTransferLine.getBslMisc(), qty, "False");
                miscList2 = this.expiryDates(invBranchStockTransferLine.getBslMisc(), qty);
            }

            Debug.print("miscList Propagate:" + miscList);
            String propagateMisc = "";
            String ret = "";
            String Checker = "";

            while (i.hasNext()) {

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) i.next();

                invPropagatedCosting
                        .setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + stockTransferQuantity);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + stockTransferQuantity);
            }

            // regenerate cost varaince
            this.regenerateCostVariance(invCostings, invCosting, branchCode, companyCode);

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

    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue,
                          LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double journalLineAmount,
                          Integer companyCode) {

        Debug.print("InvApprovalControllerBean postToGl");
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
                        EJBCommon.roundIt(glChartOfAccountBalance.getCoabEndingBalance() + journalLineAmount, FC_EXTNDD_PRCSN));

                if (!isCurrentAcv) {

                    glChartOfAccountBalance.setCoabBeginningBalance(EJBCommon
                            .roundIt(glChartOfAccountBalance.getCoabBeginningBalance() + journalLineAmount, FC_EXTNDD_PRCSN));
                }

            } else {

                glChartOfAccountBalance.setCoabEndingBalance(
                        EJBCommon.roundIt(glChartOfAccountBalance.getCoabEndingBalance() - journalLineAmount, FC_EXTNDD_PRCSN));

                if (!isCurrentAcv) {

                    glChartOfAccountBalance.setCoabBeginningBalance(EJBCommon
                            .roundIt(glChartOfAccountBalance.getCoabBeginningBalance() - journalLineAmount, FC_EXTNDD_PRCSN));
                }
            }

            if (isCurrentAcv) {

                if (isDebit == EJBCommon.TRUE) {

                    glChartOfAccountBalance.setCoabTotalDebit(
                            EJBCommon.roundIt(glChartOfAccountBalance.getCoabTotalDebit() + journalLineAmount, FC_EXTNDD_PRCSN));

                } else {

                    glChartOfAccountBalance.setCoabTotalCredit(
                            EJBCommon.roundIt(glChartOfAccountBalance.getCoabTotalCredit() + journalLineAmount, FC_EXTNDD_PRCSN));
                }
            }

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem,
                                                      double ADJST_QTY, Integer companyCode) {

        Debug.print("InvApprovalControllerBean convertByUomFromAndItemAndQuantity");
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

    private void regenerateInventoryDr(LocalInvAdjustment invAdjustment, Integer branchCode, Integer companyCode)
            throws GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException {

        Debug.print("InvApprovalControllerBean regenerateInventoryDr");

        LocalInvDistributionRecordHome invDistributionRecordHome = null;
        LocalInvCostingHome invCostingHome = null;
        LocalInvItemHome invItemHome = null;
        LocalInvItemLocationHome invItemLocationHome = null;
        LocalAdBranchItemLocationHome adBranchItemLocationHome = null;

        // Initialize EJB Home

        try {

            invDistributionRecordHome = (LocalInvDistributionRecordHome) EJBHomeFactory
                    .lookUpLocalHome(LocalInvDistributionRecordHome.JNDI_NAME, LocalInvDistributionRecordHome.class);
            invCostingHome = (LocalInvCostingHome) EJBHomeFactory.lookUpLocalHome(LocalInvCostingHome.JNDI_NAME,
                    LocalInvCostingHome.class);
            invItemHome = (LocalInvItemHome) EJBHomeFactory.lookUpLocalHome(LocalInvItemHome.JNDI_NAME,
                    LocalInvItemHome.class);
            invItemLocationHome = (LocalInvItemLocationHome) EJBHomeFactory
                    .lookUpLocalHome(LocalInvItemLocationHome.JNDI_NAME, LocalInvItemLocationHome.class);
            adBranchItemLocationHome = (LocalAdBranchItemLocationHome) EJBHomeFactory
                    .lookUpLocalHome(LocalAdBranchItemLocationHome.JNDI_NAME, LocalAdBranchItemLocationHome.class);

        }
        catch (NamingException ex) {

            throw new EJBException(ex.getMessage());
        }

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

                                invCosting = invCosting = invCostingHome
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
                                this.getGlFcPrecisionUnit(companyCode));

                        DEBIT = EJBCommon.FALSE;
                    }

                    // check for branch mapping

                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {

                        adBranchItemLocation = adBranchItemLocationHome
                                .findBilByIlCodeAndBrCode(invItemLocation.getIlCode(), branchCode, companyCode);

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

    private void regenerateInventoryDr(LocalInvStockTransfer invStockTransfer, Integer branchCode, Integer companyCode)
            throws GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException {

        Debug.print("InvApprovalControllerBean regenerateInventoryDr");

        LocalInvDistributionRecordHome invDistributionRecordHome = null;
        LocalInvCostingHome invCostingHome = null;
        LocalInvLocationHome invLocationHome = null;
        LocalInvItemLocationHome invItemLocationHome = null;
        LocalAdBranchItemLocationHome adBranchItemLocationHome = null;

        // Initialize EJB Home

        try {

            invDistributionRecordHome = (LocalInvDistributionRecordHome) EJBHomeFactory
                    .lookUpLocalHome(LocalInvDistributionRecordHome.JNDI_NAME, LocalInvDistributionRecordHome.class);
            invCostingHome = (LocalInvCostingHome) EJBHomeFactory.lookUpLocalHome(LocalInvCostingHome.JNDI_NAME,
                    LocalInvCostingHome.class);
            invLocationHome = (LocalInvLocationHome) EJBHomeFactory.lookUpLocalHome(LocalInvLocationHome.JNDI_NAME,
                    LocalInvLocationHome.class);
            invItemLocationHome = (LocalInvItemLocationHome) EJBHomeFactory
                    .lookUpLocalHome(LocalInvItemLocationHome.JNDI_NAME, LocalInvItemLocationHome.class);
            adBranchItemLocationHome = (LocalAdBranchItemLocationHome) EJBHomeFactory
                    .lookUpLocalHome(LocalAdBranchItemLocationHome.JNDI_NAME, LocalAdBranchItemLocationHome.class);

        }
        catch (NamingException ex) {

            throw new EJBException(ex.getMessage());
        }

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
                            invItemLocFrom.getInvLocation().getLocName(), branchCode, companyCode);
                    if (!invNegTxnCosting.isEmpty()) {
                        throw new GlobalInventoryDateException(invItemLocFrom.getInvItem().getIiName());
                    }

                    invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(
                            invStockTransfer.getStDate(), invItemLocTo.getInvItem().getIiName(),
                            invItemLocTo.getInvLocation().getLocName(), branchCode, companyCode);
                    if (!invNegTxnCosting.isEmpty()) {
                        throw new GlobalInventoryDateException(invItemLocTo.getInvItem().getIiName());
                    }
                }
                // add physical inventory distribution

                double COST = this.getInvIiUnitCostByIiNameAndLocFromAndUomNameAndDate(
                        invItemLocFrom.getInvItem().getIiName(), invItemLocFrom.getInvLocation().getLocName(),
                        invStockTransferLine.getInvUnitOfMeasure().getUomName(), invStockTransfer.getStDate(), branchCode,
                        companyCode);

                double AMOUNT = 0d;

                AMOUNT = EJBCommon.roundIt(invStockTransferLine.getStlQuantityDelivered() * COST,
                        this.getGlFcPrecisionUnit(companyCode));

                // dr to locationTo

                // check branch mapping

                LocalAdBranchItemLocation adBranchItemLocation = null;

                try {

                    adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(invItemLocTo.getIlCode(),
                            branchCode, companyCode);

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
                        glChartOfAccountTo.getCoaCode(), invStockTransfer, branchCode, companyCode);

                // cr to locationFrom

                // check branch mapping

                try {

                    adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(invItemLocFrom.getIlCode(),
                            branchCode, companyCode);

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
                        glChartOfAccountFrom.getCoaCode(), invStockTransfer, branchCode, companyCode);
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

    private void addInvDrEntry(short distributionRecordLine, String distributionRecordClass, byte isDebit, double distributionRecordAmount, byte isReversal,
                               Integer chartOfAccountCode, LocalInvAdjustment invAdjustment, Integer branchCode, Integer companyCode)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("InvApprovalControllerBean addInvDrEntry");
        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate coa

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(chartOfAccountCode, branchCode, companyCode);

            }
            catch (FinderException ex) {

                throw new GlobalBranchAccountNumberInvalidException();
            }

            // create distribution record

            LocalInvDistributionRecord invDistributionRecord = invDistributionRecordHome.create(distributionRecordLine, distributionRecordClass, isDebit,
                    EJBCommon.roundIt(distributionRecordAmount, adCompany.getGlFunctionalCurrency().getFcPrecision()), isReversal,
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

    private void addInvDrEntry(short distributionRecordLine, String distributionRecordClass, byte isDebit, double distributionRecordAmount, Integer chartOfAccountCode,
                               LocalInvStockTransfer invStockTransfer, Integer branchCode, Integer companyCode)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("InvApprovalControllerBean addInvDrEntry");
        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate coa

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(chartOfAccountCode, branchCode, companyCode);

            }
            catch (FinderException ex) {

                throw new GlobalBranchAccountNumberInvalidException();
            }

            // create distribution record

            LocalInvDistributionRecord invDistributionRecord = invDistributionRecordHome.create(distributionRecordLine, distributionRecordClass, isDebit,
                    EJBCommon.roundIt(distributionRecordAmount, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE,
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

    private short getInvGpQuantityPrecisionUnit(Integer companyCode) {

        Debug.print("InvApprovalControllerBean getInvGpQuantityPrecisionUnit");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfInvQuantityPrecisionUnit();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertCostByUom(String itemName, String unitOfMeasureName, double unitCost, boolean isFromDefault,
                                    Integer companyCode) {

        Debug.print("InvApprovalControllerBean convertCostByUom");
        try {

            LocalInvItem invItem = invItemHome.findByIiName(itemName, companyCode);
            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(itemName, unitOfMeasureName, companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(itemName, invItem.getInvUnitOfMeasure().getUomName(), companyCode);

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

    private double getInvIiUnitCostByIiNameAndLocFromAndUomNameAndDate(
            String itemName, String locationFrom, String unitOfMeasureName,
            Date stockTransferDate, Integer branchCode, Integer companyCode)
            throws GlobalInvItemLocationNotFoundException {

        Debug.print("InvApprovalControllerBean getInvIiUnitCostByIiNameAndUomName");
        try {

            LocalInvItemLocation invItemLocation = null;

            try {

                invItemLocation = invItemLocationHome.findByLocNameAndIiName(locationFrom, itemName, companyCode);

            }
            catch (FinderException ex) {

                throw new GlobalInvItemLocationNotFoundException(locationFrom + " for item " + itemName);
            }

            double COST = 0d;

            try {

                LocalInvCosting invCosting = invCostingHome
                        .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(stockTransferDate,
                                invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(),
                                branchCode, companyCode);

                COST = EJBCommon.roundIt(
                        Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity()),
                        this.getGlFcPrecisionUnit(companyCode));

            }
            catch (FinderException ex) {

                COST = invItemLocation.getInvItem().getIiUnitCost();
            }

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(itemName, unitOfMeasureName, companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(itemName, invItemLocation.getInvItem().getInvUnitOfMeasure().getUomName(),
                            companyCode);

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

    private void voidInvAdjustment(LocalInvAdjustment invAdjustment, Integer branchCode, Integer companyCode) {

        Debug.print("InvApprovalControllerBean voidInvAdjustment");
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

                this.addInvAlEntry(invAdjustmentLine.getInvItemLocation(), invAdjustment,
                        (invAdjustmentLine.getAlUnitCost()) * -1, EJBCommon.TRUE, companyCode);
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

    private void generateCostVariance(LocalInvItemLocation invItemLocation, double costVarianceValue, String adjustmentReferenceNumber,
                                      String adjustmentDesc, Date adjustmentDate, String username, Integer branchCode, Integer companyCode)
            throws AdPRFCoaGlVarianceAccountNotFoundException {

    }

    private void regenerateCostVariance(Collection invCostings, LocalInvCosting invCosting, Integer branchCode,
                                        Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

    }

    private LocalInvAdjustmentLine addInvAlEntry(LocalInvItemLocation invItemLocation, LocalInvAdjustment invAdjustment,
                                                 double costVarianceValue, byte isVoid, Integer companyCode) {

        Debug.print("InvApprovalControllerBean addInvAlEntry");
        try {

            // create dr entry
            LocalInvAdjustmentLine invAdjustmentLine = null;
            invAdjustmentLine = invAdjustmentLineHome.create(costVarianceValue, null, null, 0, 0, isVoid, companyCode);

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

    private void postToInv(LocalInvAdjustmentLine invAdjustmentLine, Date costDate, double costAdjustmentQuantity,
                           double costAdjustmentCost, double costRemainingQuantity, double costRemainingValue,
                           double costVarianceValue, String username, Integer branchCode, Integer companyCode)
            throws AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException {

        Debug.print("InvAdjustmentEntryControllerBean postToInv");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalInvItemLocation invItemLocation = invAdjustmentLine.getInvItemLocation();
            int CST_LN_NMBR = 0;

            costAdjustmentQuantity = EJBCommon.roundIt(costAdjustmentQuantity, adPreference.getPrfInvQuantityPrecisionUnit());
            costAdjustmentCost = EJBCommon.roundIt(costAdjustmentCost, adPreference.getPrfInvCostPrecisionUnit());
            costRemainingQuantity = EJBCommon.roundIt(costRemainingQuantity, adPreference.getPrfInvQuantityPrecisionUnit());
            costRemainingValue = EJBCommon.roundIt(costRemainingValue, adPreference.getPrfInvCostPrecisionUnit());

            if (costAdjustmentQuantity < 0) {

                invItemLocation
                        .setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - Math.abs(costAdjustmentQuantity));
            }

            try {
                Debug.print("generate line number");
                // generate line number
                LocalInvCosting invCurrentCosting = invCostingHome
                        .getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(costDate.getTime(),
                                invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(),
                                branchCode, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;
                Debug.print("generate line number: " + CST_LN_NMBR);
            }
            catch (FinderException ex) {
                Debug.print("generate line number CATCH");
                CST_LN_NMBR = 1;
            }

            if (costVarianceValue != 0) {

                // void subsequent cost variance adjustments
                Collection invAdjustmentLines = invAdjustmentLineHome
                        .findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(costDate,
                                invItemLocation.getIlCode(), branchCode, companyCode);
                Iterator i = invAdjustmentLines.iterator();

                while (i.hasNext()) {

                    LocalInvAdjustmentLine invAdjustmentLineTemp = (LocalInvAdjustmentLine) i.next();
                    this.voidInvAdjustment(invAdjustmentLineTemp.getInvAdjustment(), branchCode, companyCode);
                }
            }

            String prevExpiryDates = "";
            String miscListPrpgt = "";
            double qtyPrpgt = 0;
            try {
                LocalInvCosting prevCst = invCostingHome
                        .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndRemainingQuantityNotEqualToZeroAndIlCode(
                                costDate, invItemLocation.getIlCode(), branchCode, companyCode);
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
            LocalInvCosting invCosting = invCostingHome.create(costDate, costDate.getTime(), CST_LN_NMBR, 0d, 0d,
                    costAdjustmentQuantity, costAdjustmentCost, 0d, 0d, costRemainingQuantity, costRemainingValue, 0d, 0d, 0d, branchCode, companyCode);
            invCosting.setCstQCNumber(invAdjustmentLine.getAlQcNumber());
            invCosting.setCstQCExpiryDate(invAdjustmentLine.getAlQcExpiryDate());
            // invItemLocation.addInvCosting(invCosting);
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setInvAdjustmentLine(invAdjustmentLine);

            invCosting.setCstQuantity(costAdjustmentQuantity);
            invCosting.setCstCost(costAdjustmentCost);

            // Get Latest Expiry Dates
            if (invAdjustmentLine.getInvItemLocation().getInvItem().getIiTraceMisc() != 0) {
                if (prevExpiryDates != null && prevExpiryDates != "" && prevExpiryDates.length() != 0) {

                    if (invAdjustmentLine.getAlMisc() != null && invAdjustmentLine.getAlMisc() != ""
                            && invAdjustmentLine.getAlMisc().length() != 0) {
                        int qty2Prpgt = Integer.parseInt(this.getQuantityExpiryDates(invAdjustmentLine.getAlMisc()));

                        String miscList2Prpgt = this.propagateExpiryDates(invAdjustmentLine.getAlMisc(), qty2Prpgt,
                                "False");
                        ArrayList miscList = this.expiryDates(invAdjustmentLine.getAlMisc(), qty2Prpgt);
                        String propagateMiscPrpgt = "";
                        String ret = "";
                        String exp = "";
                        String Checker = "";

                        // ArrayList miscList2 = null;
                        if (costAdjustmentQuantity > 0) {
                            prevExpiryDates = prevExpiryDates.substring(1);
                            propagateMiscPrpgt = miscList2Prpgt + prevExpiryDates;
                        } else {
                            Iterator mi = miscList.iterator();

                            propagateMiscPrpgt = prevExpiryDates;
                            ret = propagateMiscPrpgt;
                            while (mi.hasNext()) {
                                String miscStr = (String) mi.next();

                                Integer qTest = checkExpiryDates(ret + "fin$");
                                ArrayList miscList2 = this.expiryDates("$" + ret, Double.parseDouble(qTest.toString()));

                                // ArrayList miscList2 = this.expiryDates("$" + ret, qtyPrpgt);
                                Iterator m2 = miscList2.iterator();
                                ret = "";
                                String ret2 = "false";
                                int a = 0;
                                while (m2.hasNext()) {
                                    String miscStr2 = (String) m2.next();

                                    if (ret2 == "1st") {
                                        ret2 = "false";
                                    }
                                    Debug.print("miscStr1: " + miscStr);
                                    Debug.print("miscStr2: " + miscStr2);

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
                                    Debug.print("Checker: " + Checker);
                                    Debug.print("ret2: " + ret2);
                                    if (!miscStr2.equals(miscStr) || a > 1) {
                                        if ((ret2 != "1st") && ((ret2 == "false") || (ret2 == "true"))) {
                                            if (miscStr2 != "") {
                                                miscStr2 = "$" + miscStr2;
                                                ret = ret + miscStr2;
                                                Debug.print("ret " + ret);
                                                ret2 = "false";
                                            }
                                        }
                                    }
                                }
                                if (ret != "") {
                                    ret = ret + "$";
                                }
                                Debug.print("ret una: " + ret);
                                exp = exp + ret;
                                qtyPrpgt = qtyPrpgt - 1;
                            }
                            Debug.print("ret fin " + ret);
                            Debug.print("exp fin " + exp);
                            // propagateMiscPrpgt = propagateMiscPrpgt.replace(" ", "$");
                            propagateMiscPrpgt = ret;
                            if (Checker == "true") {
                                // invCosting.setCstExpiryDate(propagateMiscPrpgt);
                            } else {
                                Debug.print("Exp Not Found");
                                throw new GlobalExpiryDateNotFoundException(
                                        invAdjustmentLine.getInvItemLocation().getInvItem().getIiName());
                            }
                        }
                        invCosting.setCstExpiryDate(propagateMiscPrpgt);

                    } else {
                        invCosting.setCstExpiryDate(prevExpiryDates);
                    }

                } else {
                    Debug.print("invAdjustmentLine ETO NA: " + invAdjustmentLine.getAlAdjustQuantity());
                    if (invAdjustmentLine.getAlAdjustQuantity() > 0) {
                        if (invAdjustmentLine.getAlMisc() != null && invAdjustmentLine.getAlMisc() != ""
                                && invAdjustmentLine.getAlMisc().length() != 0) {
                            int initialQty = Integer
                                    .parseInt(this.getQuantityExpiryDates(invAdjustmentLine.getAlMisc()));
                            String initialPrpgt = this.propagateExpiryDates(invAdjustmentLine.getAlMisc(), initialQty,
                                    "False");

                            invCosting.setCstExpiryDate(initialPrpgt);
                        } else {
                            invCosting.setCstExpiryDate(prevExpiryDates);
                        }
                    } else {
                    }
                }
            }

            // if cost variance is not 0, generate cost variance for the transaction
            if (costVarianceValue != 0) {

                this.generateCostVariance(invCosting.getInvItemLocation(), costVarianceValue,
                        "INVADJ" + invAdjustmentLine.getInvAdjustment().getAdjDocumentNumber(),
                        invAdjustmentLine.getInvAdjustment().getAdjDescription(),
                        invAdjustmentLine.getInvAdjustment().getAdjDate(), username, branchCode, companyCode);
            }

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(costDate,
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
                        .setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + costAdjustmentQuantity);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + costAdjustmentCost);
                if (costAdjustmentQuantity > 0) {
                    invPropagatedCosting.setCstRemainingLifoQuantity(
                            invPropagatedCosting.getCstRemainingLifoQuantity() + costAdjustmentQuantity);
                }
                Debug.print(
                        "invPropagatedCosting.getCstExpiryDate() : " + invPropagatedCosting.getCstExpiryDate());
                if (invAdjustmentLine.getInvItemLocation().getInvItem().getIiTraceMisc() != 0) {
                    if (invAdjustmentLine.getAlMisc() != null && invAdjustmentLine.getAlMisc() != ""
                            && invAdjustmentLine.getAlMisc().length() != 0) {
                        double qty = Double.parseDouble(this.getQuantityExpiryDates(invAdjustmentLine.getAlMisc()));
                        // invPropagatedCosting.getInvAdjustmentLine().getAlMisc();
                        miscList = this.propagateExpiryDates(invAdjustmentLine.getAlMisc(), qty, "False");
                        miscList2 = this.expiryDates(invAdjustmentLine.getAlMisc(), qty);
                        Debug.print("invAdjustmentLine.getAlMisc(): " + invAdjustmentLine.getAlMisc());
                        Debug.print("getAlAdjustQuantity(): " + invAdjustmentLine.getAlAdjustQuantity());

                        if (invAdjustmentLine.getAlAdjustQuantity() < 0) {
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
                                    throw new GlobalExpiryDateNotFoundException(
                                            invAdjustmentLine.getInvItemLocation().getInvItem().getIiName());
                                } else {
                                    Debug.print("TAE");
                                }

                                ret = ret + "$";
                                qtyPrpgt = qtyPrpgt - 1;
                            }
                        }
                    }

                    Debug.print("getAlAdjustQuantity(): " + invAdjustmentLine.getAlAdjustQuantity());
                    if (invAdjustmentLine.getAlAdjustQuantity() < 0) {

                        Iterator mi = miscList2.iterator();

                        propagateMisc = invPropagatedCosting.getCstExpiryDate();
                        ret = propagateMisc;
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
                                        Checker = "true";
                                    } else {
                                        a = a + 1;
                                        ret2 = "true";
                                    }
                                }
                                Debug.print("Checker: " + Checker);
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
                        }
                        propagateMisc = ret;
                    } else {
                        propagateMisc = miscList + invPropagatedCosting.getCstExpiryDate().substring(1
                        );
                    }

                    invPropagatedCosting.setCstExpiryDate(propagateMisc);
                }
            }

            // regenerate cost variance
            this.regenerateCostVariance(invCostings, invCosting, branchCode, companyCode);

        }
        catch (AdPRFCoaGlVarianceAccountNotFoundException ex) {

            throw ex;

        }
        catch (GlobalExpiryDateNotFoundException ex) {

            throw ex;

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
        LocalInvAdjustment invAdjustment = null;

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

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("InvApprovalControllerBean ejbCreate");
    }

}