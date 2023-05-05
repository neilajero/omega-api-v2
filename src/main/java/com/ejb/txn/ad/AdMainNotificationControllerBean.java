package com.ejb.txn.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.ad.LocalAdApprovalQueueHome;
import com.ejb.dao.ad.LocalAdUserHome;
import com.ejb.dao.ap.*;
import com.ejb.dao.ar.*;
import com.ejb.dao.cm.LocalCmAdjustmentHome;
import com.ejb.dao.cm.LocalCmFundTransferHome;
import com.ejb.dao.gl.*;
import com.ejb.dao.inv.LocalInvAdjustmentHome;
import com.ejb.dao.inv.LocalInvBranchStockTransferHome;
import com.ejb.dao.inv.LocalInvBranchStockTransferLineHome;
import com.ejb.dao.inv.LocalInvStockTransferHome;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ad.LocalAdUser;
import com.ejb.entities.ap.LocalApCheck;
import com.ejb.entities.ap.LocalApVoucher;
import com.ejb.entities.ap.LocalApVoucherLineItem;
import com.ejb.entities.ar.*;
import com.ejb.entities.gl.LocalGlAccountingCalendarValue;
import com.ejb.entities.gl.LocalGlInvestorAccountBalance;
import com.ejb.entities.gl.LocalGlSetOfBook;
import com.ejb.entities.inv.LocalInvBranchStockTransfer;
import com.ejb.entities.inv.LocalInvBranchStockTransferLine;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.txn.ar.ArInvoiceEntryController;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.mod.ar.ArModInvoiceDetails;
import com.util.mod.ar.ArModInvoiceLineItemDetails;
import com.util.mod.inv.InvModBranchStockTransferDetails;

import jakarta.ejb.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Stateless(name = "AdMainNotificationControllerEJB")
public class AdMainNotificationControllerBean extends EJBContextClass implements AdMainNotificationController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    public ArInvoiceEntryController ejbINV;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlSetOfBookHome glSetOfBookHome;
    @EJB
    private ILocalGlInvestorAccountBalanceHome glInvestorAccountBalanceHome;
    @EJB
    private ILocalGlAccountingCalendarValueHome glAccountingCalendarValueHome;
    @EJB
    private LocalArSalesOrderHome arSalesOrderHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalGlRecurringJournalHome glRecurringJournalHome;
    @EJB
    private LocalApVoucherHome apVoucherHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalGlJournalHome glJournalHome;
    @EJB
    private LocalArJobOrderHome arJobOrderHome;
    @EJB
    private LocalApRecurringVoucherHome apRecurringVoucherHome;
    @EJB
    private LocalApPurchaseRequisitionHome apPurchaseRequisitionHome;
    @EJB
    private LocalApPurchaseOrderHome apPurchaseOrderHome;
    @EJB
    private LocalApCheckHome apCheckHome;
    @EJB
    private LocalApVoucherPaymentScheduleHome apVoucherPaymentScheduleHome;
    @EJB
    private LocalApPurchaseOrderLineHome apPurchaseOrderLineHome;
    @EJB
    private LocalInvAdjustmentHome invAdjustmentHome;
    @EJB
    private LocalArPdcHome arPdcHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalArInvoiceLineHome arInvoiceLineHome;
    @EJB
    private LocalArInvoiceLineItemHome arInvoiceLineItemHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalInvStockTransferHome invStockTransferHome;
    @EJB
    private LocalArInvoicePaymentScheduleHome arInvoicePaymentScheduleHome;
    @EJB
    private LocalInvBranchStockTransferHome invBranchStockTransferHome;
    @EJB
    private LocalInvBranchStockTransferLineHome invBranchStockTransferLineHome;
    @EJB
    private LocalCmAdjustmentHome cmAdjustmentHome;
    @EJB
    private LocalCmFundTransferHome cmFundTransferHome;

    public int generateLoan(String username, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException, GlJREffectiveDatePeriodClosedException {

        Debug.print("AdMainNotificationControllerBean generateLoan");

        try {
            Collection vouLoans = apVoucherHome.findVouForLoanGeneration(branchCode, companyCode);
            Iterator x = vouLoans.iterator();
            int loansGenerated = 1;
            while (x.hasNext()) {
                LocalApVoucher apVoucher = (LocalApVoucher) x.next();
                Collection vLis = apVoucher.getApVoucherLineItems();
                Iterator y = vLis.iterator();
                ArrayList iliList = new ArrayList();
                ArModInvoiceLineItemDetails mdetails = new ArModInvoiceLineItemDetails();

                int ctr = 1;
                while (y.hasNext()) {

                    LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) y.next();
                    mdetails.setIliLine((short) ctr);
                    mdetails.setIliIiName(apVoucherLineItem.getInvItemLocation().getInvItem().getIiName());
                    mdetails.setIliLocName(apVoucherLineItem.getInvItemLocation().getInvLocation().getLocName());
                    mdetails.setIliQuantity(apVoucherLineItem.getVliQuantity());
                    mdetails.setIliUomName(apVoucherLineItem.getInvUnitOfMeasure().getUomName());
                    mdetails.setIliUnitPrice(apVoucher.getVouAmountDue());
                    mdetails.setIliAmount(apVoucher.getVouAmountDue());
                    mdetails.setIliDiscount1(0d);
                    mdetails.setIliDiscount2(0d);
                    mdetails.setIliDiscount3(0d);
                    mdetails.setIliDiscount4(0d);
                    mdetails.setIliTotalDiscount(0d);
                    ctr++;

                    iliList.add(mdetails);
                }

                LocalArCustomer arCustomer = arCustomerHome.findByCstBySupplierCode(apVoucher.getApSupplier().getSplCode(), companyCode);
                String customerCode = arCustomer.getCstCustomerCode();
                String referenceNumber = apVoucher.getVouDocumentNumber();

                ArModInvoiceDetails details = new ArModInvoiceDetails();
                details.setInvCode(null);
                details.setInvType("ITEMS");
                details.setInvDate(apVoucher.getVouDate());
                details.setInvNumber(null);
                details.setInvReferenceNumber(referenceNumber);
                details.setInvVoid(EJBCommon.FALSE);
                details.setInvDescription("SYSTEM GENERATED LOAN FOR VOUCHER: " + referenceNumber);
                details.setInvBillToAddress("");
                details.setInvBillToContact("");
                details.setInvBillToAltContact("");
                details.setInvBillToPhone("");
                details.setInvBillingHeader("");
                details.setInvBillingFooter("");
                details.setInvBillingHeader2("");
                details.setInvBillingFooter2("");
                details.setInvBillingHeader3(null);
                details.setInvBillingFooter3(null);
                details.setInvBillingSignatory("");
                details.setInvSignatoryTitle("");
                details.setInvShipToAddress("");
                details.setInvShipToContact("");
                details.setInvShipToAltContact("");
                details.setInvShipToPhone("");
                details.setInvLvFreight("");
                details.setInvShipDate(EJBCommon.convertStringToSQLDate(null));
                details.setInvConversionDate(EJBCommon.convertStringToSQLDate(null));
                details.setInvConversionRate(1);
                details.setInvDebitMemo((byte) 0);
                details.setInvSubjectToCommission((byte) 0);
                details.setInvClientPO("");
                details.setInvEffectivityDate(apVoucher.getVouDate());
                details.setInvRecieveDate(EJBCommon.convertStringToSQLDate(""));
                details.setInvCreatedBy(username);
                details.setInvDateCreated(new java.util.Date());
                details.setInvLastModifiedBy(username);
                details.setInvDateLastModified(new java.util.Date());
                details.setInvDisableInterest(EJBCommon.TRUE);
                details.setInvInterest(EJBCommon.FALSE);
                details.setInvInterestReferenceNumber(null);
                details.setInvInterestAmount(0d);
                details.setInvInterestCreatedBy(null);
                details.setInvInterestDateCreated(null);

                // assign batch name , if not existed, create one
                Integer INV_CODE = ejbINV.saveArInvIliEntry(details, apVoucher.getAdPaymentTerm2().getPytName(), "NONE", "NONE", "PHP", customerCode, null, iliList, true, null, "", branchCode, companyCode);

                loansGenerated++;
                apVoucher.setVouLoanGenerated(EJBCommon.TRUE);
            }

            return loansGenerated;

        } catch (GlJREffectiveDatePeriodClosedException gx) {
            getSessionContext().setRollbackOnly();
            throw new GlJREffectiveDatePeriodClosedException();

        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeSalesOrderTransactionStatus(String soTransactionStatus, Integer branchCode, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeSalesOrderTransactionStatus");

        try {
            return arSalesOrderHome.findDraftSoByTransactionStatus(soTransactionStatus, branchCode, companyCode).size();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeGlRjbUserNotification(Integer userCode, Integer branchCode, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeGLRjbUserNotification");

        try {
            LocalAdUser adUser = adUserHome.findByPrimaryKey(userCode);
            Collection glRecurringJournals = glRecurringJournalHome.findRjToGenerateByAdUsrNameAndDateAndBrCode(adUser.getUsrName(), EJBCommon.getGcCurrentDateWoTime().getTime(), branchCode, companyCode);
            return glRecurringJournals.size();
        } catch (FinderException ex) {
            return 0;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfEnableArInvoiceInterestGeneration(Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean getAdPrfEnableArInvoiceInterestGeneration");

        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            return adPreference.getPrfEnableArInvoiceInterestGeneration();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeGlJrForReversal(Integer branchCode, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeGlJrForReversal");

        try {
            Collection glJournals = glJournalHome.findReversibleJrByJrReversalDateAndBrCode(EJBCommon.getGcCurrentDateWoTime().getTime(), branchCode, companyCode);
            return glJournals.size();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeArSalesOrdersForProcessing(Integer branchCode, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeArSalesOrdersForProcessing");

        try {
            int count = 0;
            Collection salesOrdersForProcessing = arSalesOrderHome.findPostedSoByBrCode(branchCode, companyCode);
            for (Object o : salesOrdersForProcessing) {
                LocalArSalesOrder arSalesOrder = (LocalArSalesOrder) o;
                if ((arSalesOrder.getSoApprovalStatus().equals("APPROVED") || arSalesOrder.getSoApprovalStatus().equals("N/A")) && arSalesOrder.getSoLock() == (byte) (0)) {
                    count++;
                }
            }
            return count;

        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeArJobOrdersForProcessing(Integer branchCode, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeArJobOrdersForProcessing");

        try {
            int count = 0;
            Collection jobOrdersForProcessing = arJobOrderHome.findPostedJoByBrCode(branchCode, companyCode);
            for (Object o : jobOrdersForProcessing) {
                LocalArJobOrder arJobOrder = (LocalArJobOrder) o;
                if ((arJobOrder.getJoApprovalStatus().equals("APPROVED") || arJobOrder.getJoApprovalStatus().equals("N/A")) && arJobOrder.getJoLock() == (byte) (0)) {
                    count++;
                }
            }
            return count;

        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeApRvNumberOfVouchersToGenerate(Integer userCode, Integer branchCode, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeApRvNumberOfVouchersToGenerate");

        try {

            LocalAdUser adUser = adUserHome.findByPrimaryKey(userCode);
            Collection apRecurringVouchers = apRecurringVoucherHome.findRvToGenerateByAdUsrCodeAndDate(adUser.getUsrCode(), EJBCommon.getGcCurrentDateWoTime().getTime(), branchCode, companyCode);
            return apRecurringVouchers.size();

        } catch (FinderException ex) {
            return 0;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeApPrNumberOfPurchaseRequisitionsToGenerate(Integer userCode, Integer branchCode, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeApPrNumberOfPurchaseRequisitionsToGenerate");

        try {

            LocalAdUser adUser = adUserHome.findByPrimaryKey(userCode);
            Collection apRecurringPurchaseRequisitions = apPurchaseRequisitionHome.findPrToGenerateByAdUsrCodeAndDate(adUser.getUsrCode(), EJBCommon.getGcCurrentDateWoTime().getTime(), branchCode, companyCode);
            return apRecurringPurchaseRequisitions.size();

        } catch (FinderException ex) {
            return 0;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeApPurchaseReqForCanvass(Integer branchCode, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeApPurchaseReqForCanvass");

        try {

            Collection apPurchaseReqForCanvass = apPurchaseRequisitionHome.findPostedAndCanvassUnPostedPr(companyCode);
            return apPurchaseReqForCanvass.size();

        } catch (FinderException ex) {
            return 0;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeApCanvassRejected(Integer branchCode, String prLastModifiedBy, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeApCanvassRejected");

        try {

            Collection apCanvassRejected = apPurchaseRequisitionHome.findRejectedCanvassByBrCodeAndPrLastModifiedBy(prLastModifiedBy, companyCode);
            return apCanvassRejected.size();
        } catch (FinderException ex) {
            return 0;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeApPurchaseOrderRejected(Integer branchCode, String prCreatedBy, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeApPurchaseOrderRejected");

        try {

            Collection apPurchaseOrdersRejected = apPurchaseOrderHome.findRejectedPoByBrCodeAndPoCreatedBy(branchCode, prCreatedBy, companyCode);
            return apPurchaseOrdersRejected.size();

        } catch (FinderException ex) {
            return 0;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeApPurchaseOrderReceivingRejected(Integer branchCode, String prCreatedBy, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeApPurchaseOrderReceivingRejected");

        try {

            Collection apReceivings = apPurchaseOrderHome.findRejectedRiByBrCodeAndPoCreatedBy(branchCode, prCreatedBy, companyCode);
            return apReceivings.size();
        } catch (FinderException ex) {
            return 0;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeApPurchaseReqRejected(Integer branchCode, String prCreatedBy, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeApPurchaseReqRejected");

        try {

            Collection apPurchaseRequisitionsRejected = apPurchaseRequisitionHome.findRejectedPrByBrCodeAndPrCreatedBy(branchCode, prCreatedBy, companyCode);
            return apPurchaseRequisitionsRejected.size();
        } catch (FinderException ex) {
            return 0;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeApChecksForRelease(Integer branchCode, String prCreatedBy, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeApChecksForRelease");

        try {
            Collection apChecks = apCheckHome.findChecksForReleaseByBrCode(branchCode, companyCode);
            return apChecks.size();
        } catch (FinderException ex) {
            return 0;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeApPaymentRequestDraft(Integer branchCode, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeApPaymentRequestRejected");

        try {

            Collection apVouchers = apVoucherHome.findDraftRequestVouByBrCode(branchCode, companyCode);
            return apVouchers.size();
        } catch (FinderException ex) {
            return 0;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeApPaymentRequestGeneration(Integer branchCode, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeApPaymentRequestRejected");

        try {

            Collection apVouchers = apVoucherHome.findForGenerationRequestVouByBrCode(branchCode, companyCode);
            return apVouchers.size();
        } catch (FinderException ex) {
            return 0;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeApPaymentRequestProcessing(Integer branchCode, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeApPaymentRequestRejected");

        try {
            Collection apVouchers = apVoucherHome.findForProcessingRequestVouByBrCode(branchCode, companyCode);
            return apVouchers.size();
        } catch (FinderException ex) {
            return 0;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeApPaymentsForProcessing(Integer branchCode, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeApPaymentsForProcessing");

        int count = 0;
        try {
            Collection apVoucherPaymentSchedules = apVoucherPaymentScheduleHome.findOpenVpsByVpsLock(EJBCommon.FALSE, branchCode, companyCode);
            return apVoucherPaymentSchedules.size();
        } catch (Exception e) {
            // TODO: handle exception
            return 0;
        }
    }

    public int executeApPaymentRequestRejected(Integer branchCode, String vouCreatedBy, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeApPaymentRequestRejected");

        try {
            Collection apVouchers = apVoucherHome.findByVouRejectedCPRByByBrCodeAndVouCreatedBy(branchCode, vouCreatedBy, companyCode);
            return apVouchers.size();
        } catch (FinderException ex) {
            return 0;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeApVoucherForProcessing(Integer branchCode, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeApVoucherForProcessing");

        try {
            Collection apPurchaseOrderLines = apPurchaseOrderLineHome.findRrForProcessing(branchCode, companyCode);
            return apPurchaseOrderLines.size();
        } catch (FinderException ex) {
            return 0;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeApChecksRejected(Integer branchCode, String poCreatedBy, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeApChecksRejected");

        try {
            Collection apChecksRejected = apCheckHome.findRejectedChkByBrCodeAndChkCreatedBy(branchCode, poCreatedBy, companyCode);
            return apChecksRejected.size();
        } catch (FinderException ex) {
            return 0;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeApDirectChecksRejected(Integer branchCode, String poCreatedBy, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeApDirectChecksRejected");

        try {
            Collection apDirectChecksRejected = apCheckHome.findRejectedDirectChkByBrCodeAndChkCreatedBy(branchCode, poCreatedBy, companyCode);
            return apDirectChecksRejected.size();
        } catch (FinderException ex) {
            return 0;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeApVoucherRejected(Integer branchCode, String poCreatedBy, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeApVoucherRejected");

        try {
            Collection apVouchersRejected = apVoucherHome.findRejectedVouByBrCodeAndPoCreatedBy(branchCode, poCreatedBy, companyCode);
            return apVouchersRejected.size();
        } catch (FinderException ex) {
            return 0;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeApPurchaseOrdersForGeneration(Integer branchCode, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeApPurchaseOrdersForGeneration");

        try {
            Collection apPurchaseRequisitions = apPurchaseRequisitionHome.findUnPostedGeneratedPr(companyCode);
            return apPurchaseRequisitions.size();
        } catch (FinderException ex) {
            return 0;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeInvAdjustmentRequestToGenerate(Integer branchCode, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeInvAdjustmentRequestToGenerate");

        try {
            Collection invAdjustmentReqs = invAdjustmentHome.findAdjRequestToGenerateByAdCompanyAndBrCode(branchCode, companyCode);
            return invAdjustmentReqs.size();
        } catch (FinderException ex) {
            return 0;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeApPurchaseOrdersForPrinting(Integer branchCode, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeApPurchaseOrdersForPrinting");

        try {
            Collection apPurchaseOrders = apPurchaseOrderHome.findPOforPrint(branchCode, companyCode);
            return apPurchaseOrders.size();
        } catch (FinderException ex) {
            return 0;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeArCustomerRejected(Integer branchCode, String cstCreatedBy, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeArCustomerRejected");

        try {
            Collection arCustomers = arCustomerHome.findByCstRejectedByByBrCodeAndCstLastModifiedBy(branchCode, cstCreatedBy, companyCode);
            return arCustomers.size();
        } catch (FinderException ex) {
            return 0;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeArPdcNumberOfInvoicesToGenerate(Integer userCode, Integer branchCode, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeArPdcNumberOfInvoicesToGenerate");

        try {

            Collection openPdcs = arPdcHome.findOpenPdcAll(companyCode);
            Date current = EJBCommon.getGcCurrentDateWoTime().getTime();
            for (Object openPdc : openPdcs) {
                LocalArPdc arPdc = (LocalArPdc) openPdc;
                if (arPdc.getPdcMaturityDate().equals(current) || arPdc.getPdcMaturityDate().before(current)) {
                    arPdc.setPdcStatus("MATURED");
                }
            }
        } catch (FinderException ex) {
        }
        try {
            Collection arPdcs = arPdcHome.findPdcToGenerateByBrCode(branchCode, companyCode);
            return arPdcs.size();
        } catch (FinderException ex) {
            return 0;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeArInvoiceForPosting(String invoiceType, Integer branchCode, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeArInvoiceForPosting");

        try {

            Collection arInvoices = arInvoiceHome.findInvForPostingByBranch(branchCode, companyCode);
            int totalSize = 0;
            for (Object invoice : arInvoices) {
                LocalArInvoice arInvoice = (LocalArInvoice) invoice;
                String invType = "";

                // confirm if it is item or memo line
                Collection ar_inv_ln = arInvoiceLineHome.findInvoiceLineByInvCodeAndAdCompany(arInvoice.getInvCode(), companyCode);
                if (ar_inv_ln.size() > 0) {
                    invType = "MEMO LINES";
                }

                Collection ar_inv_ln_itm = arInvoiceLineItemHome.findByInvCode(arInvoice.getInvCode(), companyCode);
                if (ar_inv_ln_itm.size() > 0) {
                    invType = "ITEMS";
                }

                // search if invoice type have value.filter item and memo line
                if (invType.equals(invoiceType)) {
                    totalSize++;
                }
            }
            return totalSize;

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeArInvoiceCreditMemoForPosting(Integer branchCode, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeArInvoiceForPosting");

        try {
            Collection arInvoices = arInvoiceHome.findInvCreditMemoForPostingByBranch(branchCode, companyCode);
            return arInvoices.size();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeArReceiptForPosting(String receiptType, Integer branchCode, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeArReceiptForPosting");

        try {
            Collection arReceipts = arReceiptHome.findRctForPostingByBranch(receiptType, branchCode, companyCode);
            return arReceipts.size();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeAdDocumentToApprove(String approvalQueueDoc, Integer userCode, Integer branchCode, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeAdDocumentToApprove");

        try {
            Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndUsrCode(approvalQueueDoc, userCode, branchCode, companyCode);
            return adApprovalQueues.size();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeInvStockTransferUnposted(Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeInvStockTransferUnposted");

        try {
            Collection invStockTransfers = invStockTransferHome.findUnpostedSt(companyCode);
            return invStockTransfers.size();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeArOverDueInvoices(Integer branchCode, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeArOverDueInvoices");

        try {
            Collection overDueInvoices = arInvoicePaymentScheduleHome.findOverdueIpsByNextRunDate(EJBCommon.convertStringToSQLDate(EJBCommon.convertSQLDateToString(new Date())), branchCode, companyCode);
            return overDueInvoices.size();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeArInvestorBonusAndInterest(Integer branchCode, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeArInvestorBonusInterest");

        try {
            LocalGlSetOfBook glJournalSetOfBook = null;
            Date dateNow = EJBCommon.getGcCurrentStartOfDay();
            try {
                glJournalSetOfBook = glSetOfBookHome.findByDate(dateNow, companyCode);
            } catch (FinderException ex) {
                return 0;
            }

            LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), dateNow, companyCode);

            int supplierToGenerate = 0;

            Collection glInvestorAccountBalancesSupplierList = glInvestorAccountBalanceHome.findByAcvCode(glAccountingCalendarValue.getAcvCode(), companyCode);

            // loop for multiple supplier investors
            for (Object o : glInvestorAccountBalancesSupplierList) {
                LocalGlInvestorAccountBalance glInvestorAccountBalancesPerSupplier = (LocalGlInvestorAccountBalance) o;
                Date dateNowEndOfTheMonth = glInvestorAccountBalancesPerSupplier.getGlAccountingCalendarValue().getAcvDateTo();

                int supplierCode = glInvestorAccountBalancesPerSupplier.getApSupplier().getSplCode();

                Collection glInvestorAccountBalances = glInvestorAccountBalanceHome.findBonusAndInterestByAcCodeAndSplCode(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), supplierCode, companyCode);

                Iterator j = glInvestorAccountBalances.iterator();

                // loop for every investor account balance per supplier
                int monthsUngenerated = 0;
                while (j.hasNext()) {
                    LocalGlInvestorAccountBalance glInvestorAccountBalance = (LocalGlInvestorAccountBalance) j.next();
                    Date acvDateFrom = glInvestorAccountBalance.getGlAccountingCalendarValue().getAcvDateFrom();
                    Date acvDateTo = glInvestorAccountBalance.getGlAccountingCalendarValue().getAcvDateTo();

                    int CHECK_DATE_GAP = (int) ChronoUnit.DAYS.between(EJBCommon.convertLocalDateObject(acvDateTo), EJBCommon.convertLocalDateObject(dateNowEndOfTheMonth));

                    // This will check if lines is not exceeding in current date
                    if (CHECK_DATE_GAP < 0) {
                        Debug.print("Date exceed. Loop will now exit");
                        break;
                    } else {
                        monthsUngenerated++;
                    }
                }
                if (monthsUngenerated > 0) {
                    supplierToGenerate++;
                }
            }
            return supplierToGenerate;

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeArGenerateAccruedInterestIS(Integer branchCode, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeArGenerateAccruedInterestIS");

        try {

            int countToGenerate = 0;
            Collection inscribedStocks = apCheckHome.findDirectChkForAccruedInterestISGeneration(branchCode, companyCode);
            for (Object inscribedStock : inscribedStocks) {
                LocalApCheck apCheck = (LocalApCheck) inscribedStock;
                Date maturityDate = apCheck.getChkInvtMaturityDate();
                Date nextRunDate = apCheck.getChkInvtNextRunDate();
                int CHECK_MATURITY_DATE_GAP = (int) ChronoUnit.DAYS.between(EJBCommon.convertLocalDateObject(nextRunDate), EJBCommon.convertLocalDateObject(maturityDate));

                // This will check if lines is not exceeding in maturity date
                if (CHECK_MATURITY_DATE_GAP < 0) {
                    Debug.print("maturity date gap " + CHECK_MATURITY_DATE_GAP);
                    continue;
                }

                int CHECK_NEXTRUN_DATE_GAP = (int) ChronoUnit.DAYS.between(EJBCommon.convertLocalDateObject(nextRunDate), EJBCommon.convertLocalDateObject(new Date()));

                // This will check if lines is not exceeding in current date
                if (CHECK_NEXTRUN_DATE_GAP < 0) {
                    Debug.print("nextrundate date gap " + CHECK_NEXTRUN_DATE_GAP);
                    continue;
                }
                countToGenerate++;
            }
            return countToGenerate;

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeApGenerateLoan(Integer branchCode, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeApGenerateLoan");

        try {
            Collection generateLoans = apVoucherHome.findVouForLoanGeneration(branchCode, companyCode);
            return generateLoans.size();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeArGenerateAccruedInterestTB(Integer branchCode, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeArGenerateAccruedInterestTB");

        try {
            Collection treasuryBills = apCheckHome.findDirectChkForAccruedInterestTBGeneration(branchCode, companyCode);
            return treasuryBills.size();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeArPastDueInvoices(Integer branchCode, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeArPastDueInvoices");

        try {
            Collection executeArPastDueInvoices = arInvoicePaymentScheduleHome.findPastdueIpsByPenaltyDueDate(EJBCommon.convertStringToSQLDate(EJBCommon.convertSQLDateToString(new Date())), branchCode, companyCode);
            return executeArPastDueInvoices.size();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeInvBSTOrdersUnserved(Integer branchCode, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeInvBSTOrdersUnserved");

        try {
            Collection invBranchStockTransfers = invBranchStockTransferHome.findBstByBstTypeAndAdBranchAndAdCompany("ORDER", branchCode, companyCode);
            Iterator i = invBranchStockTransfers.iterator();
            ArrayList list = new ArrayList();
            while (i.hasNext()) {
                LocalInvBranchStockTransfer invBranchStockTransfer = (LocalInvBranchStockTransfer) i.next();
                boolean isBstBreak = false;
                Collection invBranchStockTransferLines = invBranchStockTransfer.getInvBranchStockTransferLines();
                for (Object branchStockTransferLine : invBranchStockTransferLines) {
                    LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) branchStockTransferLine;
                    Collection invServedBsts = null;
                    if (invBranchStockTransfer.getBstType().equals("ORDER") || invBranchStockTransfer.getBstType().equals("REGULAR") || invBranchStockTransfer.getBstType().equals("EMERGENCY")) {
                        invServedBsts = invBranchStockTransferLineHome.findByBstTransferOrderNumberAndIiNameAndLocNameAndBrCode(invBranchStockTransfer.getBstNumber(), invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(), invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName(), invBranchStockTransfer.getAdBranch().getBrCode(), companyCode);
                    } else if (invBranchStockTransfer.getBstType().equals("OUT")) {
                        invServedBsts = invBranchStockTransferLineHome.findByBstTransferOutNumberAndIiNameAndLocNameAndBrCode(invBranchStockTransfer.getBstNumber(), invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(), invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName(), invBranchStockTransfer.getAdBranch().getBrCode(), companyCode);
                    }

                    double totalServed = 0;
                    for (Object servedBst : invServedBsts) {
                        LocalInvBranchStockTransferLine invServedBst = (LocalInvBranchStockTransferLine) servedBst;
                        totalServed += invServedBst.getBslQuantity();
                    }

                    if (invServedBsts.size() == 0 || totalServed != invBranchStockTransferLine.getBslQuantity()) {
                        InvModBranchStockTransferDetails mdetails = new InvModBranchStockTransferDetails();
                        mdetails.setBstCode(invBranchStockTransfer.getBstCode());
                        mdetails.setBstType(invBranchStockTransfer.getBstType());
                        mdetails.setBstDate(invBranchStockTransfer.getBstDate());
                        mdetails.setBstNumber(invBranchStockTransfer.getBstNumber());
                        mdetails.setBstTransferOutNumber(invBranchStockTransfer.getBstTransferOutNumber());
                        mdetails.setBstTransferOrderNumber(invBranchStockTransfer.getBstTransferOrderNumber());
                        list.add(mdetails);
                        isBstBreak = true;
                    }
                    if (isBstBreak) {
                        break;
                    }
                }
                if (isBstBreak) {
                    continue;
                }
            }
            return list.size();

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            return 0;
        }
    }

    public int executeChecksForPrinting(Integer branchCode, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeChecksForPrinting");

        try {
            Collection apCheckForPrinting = apCheckHome.findChecksForPrintingByBrCode(branchCode, companyCode);
            return apCheckForPrinting.size();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int executeAdDocumentInDraft(String documentType, Integer branchCode, Integer companyCode) {

        Debug.print("AdMainNotificationControllerBean executeAdDocumentInDraft");

        try {

            switch (documentType) {
                case "GL JOURNAL":
                    return (glJournalHome.findDraftJrByBrCode(branchCode, companyCode)).size();
                case "AP CHECK PAYMENT REQUEST":
                    return (apVoucherHome.findDraftVouByVouTypeAndVouDebitMemoAndBrCode("REQUEST", EJBCommon.FALSE, branchCode, companyCode)).size();
                case "AP VOUCHER":
                    return (apVoucherHome.findDraftVouAndVouDebitMemoAndBrCode(EJBCommon.FALSE, branchCode, companyCode)).size();
                case "AP DEBIT MEMO":
                    return (apVoucherHome.findDraftVouByVouTypeAndVouDebitMemoAndBrCode("DEBIT MEMO", EJBCommon.TRUE, branchCode, companyCode)).size();
                case "AP CHECK":
                    return (apCheckHome.findDraftChkByBrCode(branchCode, companyCode)).size();
                case "AP DIRECT CHECK":
                    return (apCheckHome.findDraftDirectChkByBrCode(branchCode, companyCode)).size();
                case "AR SALES ORDER":
                    return (arSalesOrderHome.findDraftSoByBrCode(branchCode, companyCode)).size();
                case "AR JOB ORDER":
                    return (arJobOrderHome.findDraftJoByBrCode(branchCode, companyCode)).size();
                case "AR INVOICE ITEMS":
                    return (arInvoiceHome.findDraftInvByInvCreditMemoAndBrCode("ITEMS", EJBCommon.FALSE, branchCode, companyCode)).size();
                case "AR INVOICE MEMO LINES":
                    return (arInvoiceHome.findDraftInvByInvCreditMemoAndBrCode("MEMO LINES", EJBCommon.FALSE, branchCode, companyCode)).size();
                case "AR INVOICE SO MATCHED":
                    return (arInvoiceHome.findDraftInvByInvCreditMemoAndBrCode("SO MATCHED", EJBCommon.FALSE, branchCode, companyCode)).size();
                case "AR INVOICE JO MATCHED":
                    return (arInvoiceHome.findDraftInvByInvCreditMemoAndBrCode("JO MATCHED", EJBCommon.FALSE, branchCode, companyCode)).size();
                case "AR CREDIT MEMO ITEMS":
                    return (arInvoiceHome.findDraftInvByInvCreditMemoAndBrCode("ITEMS", EJBCommon.TRUE, branchCode, companyCode)).size();
                case "AR CREDIT MEMO MEMO LINES":
                    return (arInvoiceHome.findDraftInvByInvCreditMemoAndBrCode("MEMO LINES", EJBCommon.TRUE, branchCode, companyCode)).size();
                case "AR RECEIPT COLLECTION":
                    return (arReceiptHome.findDraftRctByTypeBrCode("COLLECTION", branchCode, companyCode)).size();
                case "AR RECEIPT MISC":
                    return (arReceiptHome.findDraftRctByTypeBrCode("MISC", branchCode, companyCode)).size();
                case "CM ADJUSTMENT":
                    return (cmAdjustmentHome.findDraftAdjByBrCode(branchCode, companyCode)).size();
                case "CM FUND TRANSFER":
                    return (cmFundTransferHome.findDraftFtByBrCode(branchCode, companyCode)).size();
                case "INV ADJUSTMENT":
                    return (invAdjustmentHome.findDraftAdjByBrCode(branchCode, companyCode)).size();
                case "INV BRANCH STOCK TRANSFER ORDER DRAFT":
                    return (invBranchStockTransferHome.findDraftBstOrderByBrCode(branchCode, companyCode)).size();
                case "INV BRANCH STOCK TRANSFER OUT DRAFT":
                    return invBranchStockTransferHome.findDraftBstOutByBrCode(branchCode, companyCode).size();
                case "INV BRANCH STOCK TRANSFER OUT INCOMING":
                    ArrayList incomingBSTOut = new ArrayList();
                    for (Object o : invBranchStockTransferHome.findPostedIncomingBstByAdBranchAndBstAdCompany(branchCode, companyCode)) {
                        LocalInvBranchStockTransfer invBranchStockTransfer = (LocalInvBranchStockTransfer) o;
                        if (invBranchStockTransfer.getBstLock() == EJBCommon.FALSE) {
                            incomingBSTOut.add(invBranchStockTransfer);
                        }
                    }
                    return incomingBSTOut.size();
                case "INV BRANCH STOCK TRANSFER IN DRAFT":
                    return invBranchStockTransferHome.findDraftBstInByBrCode(branchCode, companyCode).size();
                case "AP PURCHASE ORDER":
                    return (apPurchaseOrderHome.findDraftPoByBrCode(branchCode, companyCode)).size();
                case "AP RECEIVING ITEM":
                    return (apPurchaseOrderHome.findDraftRiByBrCode(branchCode, companyCode)).size();
                case "AP PURCHASE REQUISITION":
                    return (apPurchaseRequisitionHome.findDraftPrByBrCode(branchCode, companyCode)).size();
            }
            return 0;

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void ejbCreate() throws CreateException {

        Debug.print("AdMainNotificationControllerBean ejbCreate");
    }

}