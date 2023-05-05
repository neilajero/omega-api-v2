/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArRepJobOrderPrintControllerBean
 * @created
 * @author
 */
package com.ejb.txnreports.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdBranchItemLocationHome;
import com.ejb.dao.ap.LocalApPurchaseOrderLineHome;
import com.ejb.dao.ap.LocalApVoucherLineItemHome;
import com.ejb.dao.ar.*;
import com.ejb.dao.inv.LocalInvAdjustmentLineHome;
import com.ejb.dao.inv.LocalInvBranchStockTransferLineHome;
import com.ejb.dao.inv.LocalInvCostingHome;
import com.ejb.dao.inv.LocalInvStockTransferLineHome;
import com.ejb.entities.ad.LocalAdBranchItemLocation;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ap.LocalApPurchaseOrderLine;
import com.ejb.entities.ap.LocalApVoucherLineItem;
import com.ejb.entities.ar.*;
import com.ejb.entities.cm.LocalCmAdjustment;
import com.ejb.entities.inv.*;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ar.ArRepJobOrderPrintDetails;

import jakarta.ejb.*;
import java.util.*;

@Stateless(name = "ArRepJobOrderPrintControllerEJB")
public class ArRepJobOrderPrintControllerBean extends EJBContextClass implements ArRepJobOrderPrintController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalApVoucherLineItemHome apVoucherLineItemHome;
    @EJB
    private LocalApPurchaseOrderLineHome apPurchaseOrderLineHome;
    @EJB
    private LocalArInvoiceLineItemHome arInvoiceLineItemHome;
    @EJB
    private LocalArJobOrderInvoiceLineHome arJobOrderInvoiceLineHome;
    @EJB
    private LocalInvAdjustmentLineHome invAdjustmentLineHome;
    @EJB
    private LocalInvStockTransferLineHome invStockTransferLineHome;
    @EJB
    private LocalInvBranchStockTransferLineHome invBranchStockTransferLineHome;
    @EJB
    private LocalArJobOrderHome arJobOrderHome;
    @EJB
    private LocalArCustomerBalanceHome arCustomerBalanceHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalArInvoicePaymentScheduleHome arInvoicePaymentScheduleHome;
    @EJB
    private LocalArAppliedInvoiceHome arAppliedInvoiceHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private LocalArJobOrderLineHome arJobOrderLineHome;

    public ArrayList executeArRepJobOrderPrint(ArrayList soCodeList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepJobOrderPrintControllerBean executeArRepJobOrderPrint");
        ArrayList list = new ArrayList();
        try {

            LocalAdCompany adCompany = null;

            try {

                adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            } catch (FinderException ex) {

            }

            for (Object item : soCodeList) {

                Integer JO_CODE = (Integer) item;

                LocalArJobOrder arJobOrder = null;

                try {

                    arJobOrder = arJobOrderHome.findByPrimaryKey(JO_CODE);

                } catch (FinderException ex) {

                    continue;
                }

                Date CURR_DT = EJBCommon.getGcCurrentDateWoTime().getTime();

                // find customer balance before or equal invoice date

                Collection arCustomerBalances = null;

                try {

                    arCustomerBalances = arCustomerBalanceHome.findByBeforeOrEqualInvDateAndCstCustomerCode(CURR_DT, arJobOrder.getArCustomer().getCstCustomerCode(), AD_CMPNY);

                } catch (FinderException ex) {

                }

                double CST_BLNC = 0d;

                if (!arCustomerBalances.isEmpty()) {

                    // get latest balance

                    ArrayList arCustomerBalanceList = new ArrayList(arCustomerBalances);

                    LocalArCustomerBalance arLatestBalance = (LocalArCustomerBalance) arCustomerBalanceList.get(arCustomerBalanceList.size() - 1);

                    CST_BLNC = arLatestBalance.getCbBalance();
                }

                // get pending order total

                double TOTAL_INV_JO_MTCHD = 0d;

                Collection arInvoices = arJobOrder.getArCustomer().getArInvoices();

                Iterator invIter = arInvoices.iterator();

                while (invIter.hasNext()) {

                    LocalArInvoice arInvoice = (LocalArInvoice) invIter.next();

                    if (arInvoice.getInvPosted() == EJBCommon.FALSE || arInvoice.getInvCreditMemo() == EJBCommon.TRUE) {

                        continue;
                    }

                    if (arInvoice.getInvJoNumber() != null && !arInvoice.getInvJoNumber().equals("")) {

                        if (arInvoice.getInvJoNumber().equals(arJobOrder.getJoDocumentNumber())) {

                            continue;
                        }

                        TOTAL_INV_JO_MTCHD += arInvoice.getInvAmountDue();
                    }
                }

                double TOTAL_JB_ORDR = 0d;

                Collection arExistingJobOrders = arJobOrder.getArCustomer().getArJobOrders();

                for (Object existingJobOrder : arExistingJobOrders) {

                    LocalArJobOrder arExistingJobOrder = (LocalArJobOrder) existingJobOrder;

                    if (arExistingJobOrder.getJoPosted() == EJBCommon.FALSE) {

                        continue;
                    }

                    if (!arExistingJobOrder.getJoDocumentNumber().equals(arJobOrder.getJoDocumentNumber())) {

                        Collection arExistingJobOrderLines = arExistingJobOrder.getArJobOrderLines();

                        for (Object arExistingJobOrderLine : arExistingJobOrderLines) {

                            LocalArJobOrderLine arJobOrderLine = (LocalArJobOrderLine) arExistingJobOrderLine;

                            TOTAL_JB_ORDR += arJobOrderLine.getJolAmount();
                        }
                    }
                }

                // get customer's last receipt

                double LST_RCT_AMNT = 0d;
                Date LST_RCT_DT = null;

                Collection arReceipts = arJobOrder.getArCustomer().getArReceipts();

                if (!arReceipts.isEmpty()) {

                    for (Object receipt : arReceipts) {

                        LocalArReceipt arReceipt = (LocalArReceipt) receipt;

                        if (!arReceipt.getRctType().equals("COLLECTION") || arReceipt.getRctPosted() == EJBCommon.FALSE) {

                            continue;
                        }

                        if (LST_RCT_DT == null && arReceipt.getRctType().equals("COLLECTION") && arReceipt.getRctPosted() == EJBCommon.TRUE) {

                            LST_RCT_DT = arReceipt.getRctDate();

                        } else if ((arReceipt.getRctDate().after(LST_RCT_DT) || arReceipt.getRctDate().equals(LST_RCT_DT))) {

                            LST_RCT_AMNT = arReceipt.getRctAmount();
                            LST_RCT_DT = arReceipt.getRctDate();
                        }
                    }
                }

                // get aging

                double AG_BCKT0 = 0d;
                double AG_BCKT1 = 0d;
                double AG_BCKT2 = 0d;
                double AG_BCKT3 = 0d;
                double AG_BCKT4 = 0d;
                double AG_BCKT5 = 0d;

                try {

                    short precisionUnit = adCompany.getGlFunctionalCurrency().getFcPrecision();

                    Collection arInvoicePaymentSchedules = arInvoicePaymentScheduleHome.findByCstCustomerCode(arJobOrder.getArCustomer().getCstCustomerCode(), AD_CMPNY);

                    for (Object invoicePaymentSchedule : arInvoicePaymentSchedules) {

                        LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) invoicePaymentSchedule;

                        LocalArInvoice arInvoice = arInvoicePaymentSchedule.getArInvoice();

                        if (arInvoice.getInvPosted() == EJBCommon.FALSE || arInvoice.getInvCreditMemo() == EJBCommon.TRUE) {

                            continue;
                        }

                        // get future cm

                        double CM_AMNT = 0d;

                        Collection arCreditMemos = arInvoiceHome.findByInvCreditMemoAndInvCmInvoiceNumberAndInvVoidAndInvPosted(EJBCommon.TRUE, arInvoice.getInvNumber(), EJBCommon.FALSE, EJBCommon.TRUE, AD_CMPNY);

                        for (Object creditMemo : arCreditMemos) {

                            LocalArInvoice arCreditMemo = (LocalArInvoice) creditMemo;

                            if (arCreditMemo.getInvDate().after(CURR_DT)) {

                                CM_AMNT += EJBCommon.roundIt(arCreditMemo.getInvAmountDue() * (arInvoicePaymentSchedule.getIpsAmountDue() / arInvoice.getInvAmountDue()), precisionUnit);
                            }
                        }

                        // get future receipts

                        double RCPT_AMNT = 0d;

                        Collection arAppliedInvoices = arAppliedInvoiceHome.findPostedAiByIpsCode(arInvoicePaymentSchedule.getIpsCode(), AD_CMPNY);

                        for (Object appliedInvoice : arAppliedInvoices) {

                            LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) appliedInvoice;

                            if (arAppliedInvoice.getArReceipt().getRctDate().after(CURR_DT)) {

                                RCPT_AMNT += arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiDiscountAmount() + arAppliedInvoice.getAiCreditableWTax();
                            }
                        }

                        double AMNT_DUE = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid() + CM_AMNT + RCPT_AMNT, AD_CMPNY);

                        int INVOICE_AGE = 0;

                        INVOICE_AGE = (short) ((CURR_DT.getTime() - arInvoice.getInvDate().getTime()) / (1000 * 60 * 60 * 24));

                        if (INVOICE_AGE <= 0) {

                            AG_BCKT0 += AMNT_DUE;

                        } else if (INVOICE_AGE >= 1 && INVOICE_AGE <= 15) {

                            AG_BCKT1 += AMNT_DUE;

                        } else if (INVOICE_AGE >= 16 && INVOICE_AGE <= 30) {

                            AG_BCKT2 += AMNT_DUE;

                        } else if (INVOICE_AGE >= 31 && INVOICE_AGE <= 60) {

                            AG_BCKT3 += AMNT_DUE;

                        } else if (INVOICE_AGE >= 61 && INVOICE_AGE <= 90) {

                            AG_BCKT4 += AMNT_DUE;

                        } else if (INVOICE_AGE >= 91 && INVOICE_AGE <= 120) {

                            AG_BCKT5 += AMNT_DUE;
                        }
                    }

                } catch (FinderException ex) {

                }

                // get job order total amount

                Collection arJoLines = arJobOrder.getArJobOrderLines();
                double JO_AMNT = 0d;

                for (Object arJoLine : arJoLines) {

                    LocalArJobOrderLine arJobOrderLine = (LocalArJobOrderLine) arJoLine;

                    JO_AMNT += arJobOrderLine.getJolAmount();
                }

                // subtract sales order's amount from customer's balance if so is included in customer
                // balance

                arInvoices = null;

                arInvoices = arJobOrder.getArCustomer().getArInvoices();

                invIter = arInvoices.iterator();

                while (invIter.hasNext()) {

                    LocalArInvoice arInvoice = (LocalArInvoice) invIter.next();

                    if (arInvoice.getInvSoNumber() != null && arInvoice.getInvSoNumber().equals(arJobOrder.getJoDocumentNumber()) && arInvoice.getInvPosted() == EJBCommon.TRUE && arInvoice.getInvAmountDue() > arInvoice.getInvAmountPaid()) {

                        CST_BLNC -= JO_AMNT;
                    }
                }

                // get job order lines

                Collection arJobOrderLines = arJobOrder.getArJobOrderLines();

                for (Object jobOrderLine : arJobOrderLines) {
                    Debug.print("Calls print here.");
                    LocalArJobOrderLine arJobOrderLine = (LocalArJobOrderLine) jobOrderLine;

                    ArRepJobOrderPrintDetails details = new ArRepJobOrderPrintDetails();

                    String iii_Desc = arJobOrderLine.getInvItemLocation().getInvItem().getIiDescription();
                    String eii_Desc = arJobOrderLine.getJolLineIDesc();
                    String ii_DescDisp = "";

                    if (eii_Desc.trim().length() == 0) {
                        ii_DescDisp = iii_Desc;
                    } else if (eii_Desc.trim().length() > 0) {
                        ii_DescDisp = eii_Desc;
                    }

                    details.setJpJoDate(arJobOrder.getJoDate());
                    details.setJpJoReferenceNumber(arJobOrder.getJoReferenceNumber());
                    details.setJpJoDescription(arJobOrder.getJoDescription());
                    details.setJpJoDocumentNumber(arJobOrder.getJoDocumentNumber());
                    details.setJpJoTransactionType(arJobOrder.getJoTransactionType());
                    details.setJpJoCreatedBy(arJobOrder.getJoCreatedBy());
                    details.setJpJoApprovedRejectedBy(arJobOrder.getJoApprovedRejectedBy());
                    details.setJpJolQuantity(arJobOrderLine.getJolQuantity());
                    details.setJpJolUom(arJobOrderLine.getInvUnitOfMeasure().getUomName());
                    details.setJpJolIiCode(arJobOrderLine.getInvItemLocation().getInvItem().getIiName());
                    details.setJpJolLocName(arJobOrderLine.getInvItemLocation().getInvLocation().getLocName());
                    details.setSpIlCategory(arJobOrderLine.getInvItemLocation().getInvItem().getIiAdLvCategory());
                    details.setJpJolUnitPrice(arJobOrderLine.getJolUnitPrice());
                    details.setJpJolAmount(arJobOrderLine.getJolAmount());
                    details.setJpJoCstCustomerCode(arJobOrder.getArCustomer().getCstCustomerCode());
                    details.setJpJoCstName(arJobOrder.getArCustomer().getCstName());
                    details.setJpJoCstAddress(arJobOrder.getArCustomer().getCstAddress());
                    details.setJpJoCstCreditLimit(arJobOrder.getArCustomer().getCstCreditLimit());
                    // details.setJpJolIiDescription(arJobOrderLine.getInvItemLocation().getInvItem().getIiDescription());

                    details.setJpJoCstContactPerson(arJobOrder.getArCustomer().getCstContact());
                    details.setJpJoCstPhoneNumber(arJobOrder.getArCustomer().getCstPhone());
                    details.setJpJoCstMobileNumber(arJobOrder.getArCustomer().getCstMobilePhone());
                    details.setJpJoCstFax(arJobOrder.getArCustomer().getCstFax());
                    details.setJpJoCstEmail(arJobOrder.getArCustomer().getCstEmail());

                    details.setJpJolIiDescription(ii_DescDisp);

                    details.setJpJoFcSymbol(arJobOrder.getGlFunctionalCurrency().getFcSymbol());
                    details.setJpJoCstCbBalance(CST_BLNC);
                    details.setJpJoCstPendingOrder(TOTAL_JB_ORDR - TOTAL_INV_JO_MTCHD);
                    details.setJpJoAmount(JO_AMNT);
                    details.setJpJoCstLastReceiptAmount(LST_RCT_AMNT);
                    details.setJpJoCstLastReceiptDate(LST_RCT_DT);
                    details.setJpJoPaymentTerm(arJobOrder.getAdPaymentTerm().getPytName());
                    details.setJpReportParameter(arJobOrder.getReportParameter());
                    details.setJpJoShipTo(arJobOrder.getJoShipTo());
                    details.setJpJoBillTo(arJobOrder.getJoBillTo());
                    details.setJpJoTechnician(arJobOrder.getJoTechnician());
                    details.setJpJoMemo(arJobOrder.getJoMemo());
                    details.setJpJoJobOrderStatus(arJobOrder.getJoJobOrderStatus());

                    if (arJobOrder.getArSalesperson() != null) {

                        details.setJpJoCstSlsSalespersonCode(arJobOrder.getArSalesperson().getSlpSalespersonCode());
                        details.setJpJoCstSlsName(arJobOrder.getArSalesperson().getSlpName());
                    }

                    // trace misc
                    if (arJobOrderLine.getInvItemLocation().getInvItem().getIiTraceMisc() == EJBCommon.TRUE) {

                        if (arJobOrderLine.getJolMisc() != null && arJobOrderLine.getInvTags().size() <= 0) {
                            Debug.print("old code");
                            ArrayList miscArray = convertMiscToArrayList(arJobOrderLine.getJolMisc());
                            // property code
                            details.setJpJolPropertyCode(miscArray.get(0).toString());
                            // serial number
                            details.setJpJolSerialNumber(miscArray.get(1).toString());
                            // specs
                            details.setJpJolSpecs(miscArray.get(2).toString());
                            // custodian
                            details.setJpJolCustodian(miscArray.get(3).toString());
                            // expiration date
                            details.setJpJolExpiryDate(miscArray.get(4).toString());
                        }

                        if (arJobOrderLine.getInvTags().size() > 0) {
                            Debug.print("new code");
                            StringBuilder strBProperty = new StringBuilder();
                            StringBuilder strBSerial = new StringBuilder();
                            StringBuilder strBSpecs = new StringBuilder();
                            StringBuilder strBCustodian = new StringBuilder();
                            StringBuilder strBExpirationDate = new StringBuilder();

                            for (Object o : arJobOrderLine.getInvTags()) {

                                LocalInvTag invTag = (LocalInvTag) o;

                                // property code
                                if (!invTag.getTgPropertyCode().trim().equals("")) {
                                    strBProperty.append(invTag.getTgPropertyCode());
                                    strBProperty.append(System.getProperty("line.separator"));
                                }

                                // serial

                                if (!invTag.getTgSerialNumber().trim().equals("")) {
                                    strBSerial.append(invTag.getTgSerialNumber());
                                    strBSerial.append(System.getProperty("line.separator"));
                                }

                                // spec

                                if (!invTag.getTgSpecs().trim().equals("")) {
                                    strBSpecs.append(invTag.getTgSpecs());
                                    strBSpecs.append(System.getProperty("line.separator"));
                                }

                                // custodian

                                if (invTag.getAdUser() != null) {
                                    strBCustodian.append(invTag.getAdUser().getUsrName());
                                    strBCustodian.append(System.getProperty("line.separator"));
                                }

                                // exp date

                                if (invTag.getTgExpiryDate() != null) {
                                    strBExpirationDate.append(invTag.getTgExpiryDate());
                                    strBExpirationDate.append(System.getProperty("line.separator"));
                                }
                            }
                            // property code
                            details.setJpJolPropertyCode(strBProperty.toString());
                            // serial number
                            details.setJpJolSerialNumber(strBSerial.toString());
                            // specs
                            details.setJpJolSpecs(strBSpecs.toString());
                            // custodian
                            details.setJpJolCustodian(strBCustodian.toString());
                            // expiration date
                            details.setJpJolExpiryDate(strBExpirationDate.toString());
                        }
                    }
                    // get So Advance if have
                    double SO_ADVNC_AMNT = 0d;

                    for (Object value : arJobOrder.getCmAdjustments()) {

                        LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) value;

                        if (cmAdjustment.getAdjVoid() == EJBCommon.FALSE && cmAdjustment.getAdjReconciled() == EJBCommon.FALSE) {
                            SO_ADVNC_AMNT += cmAdjustment.getAdjAmount();
                        }
                    }

                    details.setJpJoAdvanceAmount(SO_ADVNC_AMNT);

                    details.setJpJoCstCity(arJobOrder.getArCustomer().getCstCity());
                    double unpostedQuantity = 0;
                    double committedQty = 0d;
                    try {

                        LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndIiNameAndLocName(arJobOrderLine.getInvItemLocation().getInvItem().getIiName(), arJobOrderLine.getInvItemLocation().getInvLocation().getLocName(), arJobOrder.getJoAdBranch(), AD_CMPNY);

                        LocalAdBranchItemLocation adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arJobOrderLine.getInvItemLocation().getIlCode(), arJobOrder.getJoAdBranch(), AD_CMPNY);

                        unpostedQuantity = this.getShUnpostedQuantity(adBranchItemLocation, arJobOrder.getJoDate(), AD_CMPNY);

                        // committed quantity
                        double jolQty = 0d;
                        double deliveredQty = 0d;
                        committedQty = 0d;
                        GregorianCalendar gcDateFrom = new GregorianCalendar();
                        gcDateFrom.setTime(arJobOrder.getJoDate());
                        gcDateFrom.add(Calendar.MONTH, -1);

                        Collection arJobOrderLines2 = arJobOrderLineHome.findCommittedQtyByIiNameAndLocNameAndJoAdBranch(adBranchItemLocation.getInvItemLocation().getInvItem().getIiName(), adBranchItemLocation.getInvItemLocation().getInvLocation().getLocName(), gcDateFrom.getTime(), arJobOrder.getJoDate(), adBranchItemLocation.getAdBranch().getBrCode(), AD_CMPNY);

                        for (Object o : arJobOrderLines2) {
                            LocalArJobOrderLine arJobOrderLine2 = (LocalArJobOrderLine) o;

                            committedQty += (arJobOrderLine2.getArJobOrderInvoiceLines().size() == 0 ? arJobOrderLine2.getJolQuantity() : 0);
                        }

                        Collection arJobOrderInvoiceLines = arJobOrderInvoiceLineHome.findCommittedQtyByIiNameAndLocNameAndJoAdBranch(adBranchItemLocation.getInvItemLocation().getInvItem().getIiName(), adBranchItemLocation.getInvItemLocation().getInvLocation().getLocName(), gcDateFrom.getTime(), arJobOrder.getJoDate(), adBranchItemLocation.getAdBranch().getBrCode(), AD_CMPNY);

                        for (Object jobOrderInvoiceLine : arJobOrderInvoiceLines) {
                            LocalArJobOrderInvoiceLine arJobOrderInvoiceLine = (LocalArJobOrderInvoiceLine) jobOrderInvoiceLine;

                            jolQty = arJobOrderInvoiceLine.getArJobOrderLine().getJolQuantity();
                            deliveredQty = arJobOrderInvoiceLine.getJilQuantityDelivered();
                            committedQty += (jolQty - deliveredQty);
                        }

                        Debug.print("Remaining QTY: " + invCosting.getCstRemainingQuantity());
                        Debug.print("Unposted QTY: " + unpostedQuantity);
                        Debug.print("Committed QTY: " + committedQty);
                        Debug.print("Total: " + (invCosting.getCstRemainingQuantity() + unpostedQuantity - committedQty));

                        if (invCosting.getCstRemainingQuantity() + unpostedQuantity - committedQty > arJobOrderLine.getJolQuantity()) {

                            details.setJpJolIiStockStatus("A");

                        } else {

                            details.setJpJolIiStockStatus("O");
                        }

                    } catch (FinderException ex) {

                        details.setJpJolIiStockStatus("O");
                    }

                    // aging
                    details.setJpJoAgBucket0(AG_BCKT0);
                    details.setJpJoAgBucket1(AG_BCKT1);
                    details.setJpJoAgBucket2(AG_BCKT2);
                    details.setJpJoAgBucket3(AG_BCKT3);
                    details.setJpJoAgBucket4(AG_BCKT4);
                    details.setJpJoAgBucket5(AG_BCKT5);

                    // discount
                    details.setJpJolTotalDiscount(arJobOrderLine.getJolTotalDiscount());
                    details.setJpJolDiscount1(arJobOrderLine.getJolDiscount1());
                    details.setJpJolDiscount2(arJobOrderLine.getJolDiscount2());
                    details.setJpJolDiscount3(arJobOrderLine.getJolDiscount3());
                    details.setJpJolDiscount4(arJobOrderLine.getJolDiscount4());

                    // get Tax Amount
                    double NT_AMNT = calculateSlNetAmount(details, arJobOrder.getArTaxCode().getTcRate(), arJobOrder.getArTaxCode().getTcType(), this.getGlFcPrecisionUnit(AD_CMPNY));

                    Debug.print("cal net amount: " + NT_AMNT);

                    details.setJpJolTaxAmount(calculateSlTaxAmount(details, arJobOrder.getArTaxCode().getTcRate(), arJobOrder.getArTaxCode().getTcType(), NT_AMNT, this.getGlFcPrecisionUnit(AD_CMPNY)));
                    Debug.print("cal tax amount: " + details.getJpJolTaxAmount());
                    // get unit cost wo vat
                    details.setJpJolUnitCostWoTax(calculateSlNetUnitCost(details, arJobOrder.getArTaxCode().getTcRate(), arJobOrder.getArTaxCode().getTcType(), this.getGlFcPrecisionUnit(AD_CMPNY)));

                    details.setJpJoTaxType(arJobOrder.getArTaxCode().getTcType());

                    // get quantity on hand
                    try {

                        LocalInvCosting invStockOnHand = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arJobOrder.getJoDate(), arJobOrderLine.getInvItemLocation().getInvItem().getIiName(), arJobOrderLine.getInvItemLocation().getInvLocation().getLocName(), arJobOrder.getJoAdBranch(), AD_CMPNY);

                        if (adCompany.getCmpShortName().equalsIgnoreCase("AMON")) {

                            details.setJpJolQuantityOnHand(invStockOnHand.getCstRemainingQuantity() + unpostedQuantity - committedQty);
                        } else {

                            details.setJpJolQuantityOnHand(invStockOnHand.getCstRemainingQuantity());
                        }

                    } catch (FinderException ex) {
                    }

                    Debug.print("item | qoh" + arJobOrderLine.getInvItemLocation().getInvItem().getIiName() + "\t" + details.getJpJolQuantityOnHand());

                    details.setJpJoCustomerDealPrice(arJobOrder.getArCustomer().getCstDealPrice());

                    if (arJobOrderLine.getArJobOrderAssignments().size() <= 0) {

                        list.add(details);
                    } else {
                        Collection arJobOrderAssignments = arJobOrderLine.getArJobOrderAssignments();

                        for (Object jobOrderAssignment : arJobOrderAssignments) {

                            LocalArJobOrderAssignment arJobOrderAssignment = (LocalArJobOrderAssignment) jobOrderAssignment;

                            ArRepJobOrderPrintDetails details2 = (ArRepJobOrderPrintDetails) details.clone();

                            if (arJobOrderAssignment.getJaSo() == EJBCommon.FALSE) {
                                continue;
                            }
                            details2.setJpJaPeIdNumber(arJobOrderAssignment.getArPersonel().getPeIdNumber());
                            details2.setJpJaPeName(arJobOrderAssignment.getArPersonel().getPeName());
                            details2.setJpJaQuantity(arJobOrderAssignment.getJaQuantity());
                            details2.setJpJaUnitCost(arJobOrderAssignment.getJaUnitCost());
                            details2.setJpJaAmount(arJobOrderAssignment.getJaAmount());
                            details2.setJpJaRemarks(arJobOrderAssignment.getJaRemarks());

                            list.add(details2);
                        }
                    }
                }
            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("ArRepJobOrderPrintControllerBean getAdCompany");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());
            details.setCmpAddress(adCompany.getCmpAddress());
            details.setCmpCity(adCompany.getCmpCity());
            details.setCmpContact(adCompany.getCmpContact());
            details.setCmpCountry(adCompany.getCmpCountry());
            details.setCmpEmail(adCompany.getCmpEmail());
            details.setCmpTin(adCompany.getCmpTin());
            details.setCmpDescription(adCompany.getCmpDescription());
            details.setCmpPhone(adCompany.getCmpPhone());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods

    private double getShUnpostedQuantity(LocalAdBranchItemLocation adBranchItemLocation, Date AS_OF_DT, Integer AD_CMPNY) {

        Debug.print("InvReceivingSyncControllerBean getUnpostedQuantity");
        double totalUnpostedQuantity = 0d;
        try {
            double unpostedQuantity = 0d;

            String itemName = adBranchItemLocation.getInvItemLocation().getInvItem().getIiName();
            String locationName = adBranchItemLocation.getInvItemLocation().getInvLocation().getLocName();
            Integer locationCode = adBranchItemLocation.getInvItemLocation().getInvLocation().getLocCode();
            Integer branchCode = adBranchItemLocation.getAdBranch().getBrCode();

            // (1) AP Voucher Line Item - AP Voucher
            byte debitMemo;

            Collection apVoucherLineItems = apVoucherLineItemHome.findUnpostedVouByIiNameAndLocNameAndLessEqualDateAndVouAdBranch(itemName, locationName, AS_OF_DT, branchCode, AD_CMPNY);

            Iterator x = apVoucherLineItems.iterator();

            while (x.hasNext()) {

                LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) x.next();

                unpostedQuantity = apVoucherLineItem.getVliQuantity();

                Debug.print("(1) Name: " + apVoucherLineItem.getInvItemLocation().getInvItem().getIiName() + "\tUnposted Quantity: " + unpostedQuantity);

                debitMemo = apVoucherLineItem.getApVoucher().getVouDebitMemo();

                if (debitMemo == 0) {
                    totalUnpostedQuantity += unpostedQuantity;
                } else {
                    totalUnpostedQuantity -= unpostedQuantity;
                }
            }

            // (2) AP Voucher Line Item - AP Check
            apVoucherLineItems = apVoucherLineItemHome.findUnpostedChkByIiNameAndLocNameAndLessEqualDateAndChkAdBranch(itemName, locationName, AS_OF_DT, branchCode, AD_CMPNY);

            x = apVoucherLineItems.iterator();

            while (x.hasNext()) {

                LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) x.next();

                unpostedQuantity = apVoucherLineItem.getVliQuantity();

                Debug.print("(2) Name: " + apVoucherLineItem.getInvItemLocation().getInvItem().getIiName() + "\tUnposted Quantity: " + unpostedQuantity);

                totalUnpostedQuantity += unpostedQuantity;
            }

            // (3) AP PURCHASE ORDER LINE
            Collection apPurchaseOrderLines = apPurchaseOrderLineHome.findUnpostedPoByIiNameAndLocNameAndLessEqualDateAndPoAdBranch(itemName, locationName, AS_OF_DT, branchCode, AD_CMPNY);

            x = apPurchaseOrderLines.iterator();

            while (x.hasNext()) {

                LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) x.next();

                unpostedQuantity = apPurchaseOrderLine.getPlQuantity();

                Debug.print("(3) Name: " + apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiName() + "\tUnposted Quantity: " + unpostedQuantity);

                totalUnpostedQuantity += unpostedQuantity;
            }

            // (4) AR Invoice Line Item - AR Invoice
            byte creditMemo;

            Collection arInvoiceLineItems = arInvoiceLineItemHome.findUnpostedInvcByIiNameAndLocNameAndLessEqualDateAndInvAdBranch(itemName, locationName, AS_OF_DT, branchCode, AD_CMPNY);

            x = arInvoiceLineItems.iterator();

            while (x.hasNext()) {

                LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) x.next();

                unpostedQuantity = arInvoiceLineItem.getIliQuantity();

                Debug.print("(4) Name: " + arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName() + "\tUnposted Quantity: " + unpostedQuantity);

                creditMemo = arInvoiceLineItem.getArInvoice().getInvCreditMemo();

                if (creditMemo == 0) {
                    totalUnpostedQuantity -= unpostedQuantity;
                } else {
                    totalUnpostedQuantity += unpostedQuantity;
                }
            }

            // (5) AR Invoice Line Item - AR Receipt
            arInvoiceLineItems = arInvoiceLineItemHome.findUnpostedRctByIiNameAndLocNameAndLessEqualDateAndRctAdBranch(itemName, locationName, AS_OF_DT, branchCode, AD_CMPNY);

            x = arInvoiceLineItems.iterator();

            while (x.hasNext()) {

                LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) x.next();

                unpostedQuantity = arInvoiceLineItem.getIliQuantity();

                Debug.print("(5) Name: " + arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName() + "\tUnposted Quantity: " + unpostedQuantity);

                totalUnpostedQuantity -= unpostedQuantity;
            }

            // (6)AR Sales Order Invoice Line
            Collection arJobOrderInvoiceLines = arJobOrderInvoiceLineHome.findUnpostedInvcByIiNameAndLocNameAndLessEqualDateAndInvAdBranch(itemName, locationName, AS_OF_DT, branchCode, AD_CMPNY);

            x = arJobOrderInvoiceLines.iterator();

            while (x.hasNext()) {
                LocalArJobOrderInvoiceLine arJobOrderInvoiceLine = (LocalArJobOrderInvoiceLine) x.next();

                unpostedQuantity = arJobOrderInvoiceLine.getJilQuantityDelivered();

                Debug.print("(6) Name: " + arJobOrderInvoiceLine.getArJobOrderLine().getInvItemLocation().getInvItem().getIiName() + "\tUnposted Quantity: " + unpostedQuantity);

                totalUnpostedQuantity -= unpostedQuantity;
            }

            // (7) INV Adjustment Line
            Collection invAdjustmentLines = invAdjustmentLineHome.findUnpostedAdjByIiNameAndLocNameAndLessEqualDateAndAdjAdBranch(itemName, locationName, AS_OF_DT, branchCode, AD_CMPNY);

            x = invAdjustmentLines.iterator();

            while (x.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) x.next();

                unpostedQuantity = invAdjustmentLine.getAlAdjustQuantity();

                Debug.print("(7) Name: " + invAdjustmentLine.getInvItemLocation().getInvItem().getIiName() + "\tUnposted Quantity: " + unpostedQuantity);

                if (unpostedQuantity >= 0) {
                    totalUnpostedQuantity += unpostedQuantity;
                } else if (unpostedQuantity < 0) {
                    totalUnpostedQuantity -= unpostedQuantity;
                }
            }

            // (9) INV Stock Transfer Line
            // Series of System.out's were executed in order to trace the ADDITION and DEDUCTION of
            // unposted quantities for each item.
            Collection invStockTransferLines = invStockTransferLineHome.findUnpostedStlByIiNameAndLocCodeAndLessEqualDateAndStAdBranch(itemName, locationCode, AS_OF_DT, branchCode, AD_CMPNY);

            x = invStockTransferLines.iterator();

            while (x.hasNext()) {

                LocalInvStockTransferLine invStockTransferLine = (LocalInvStockTransferLine) x.next();

                unpostedQuantity = invStockTransferLine.getStlQuantityDelivered();

                Debug.print("(9) Item Name: " + invStockTransferLine.getInvItem().getIiName() + "\tUnposted Quantity: " + unpostedQuantity);
                Debug.print("Location Code" + locationCode);
                Debug.print("Location TO: " + invStockTransferLine.getStlLocationTo());
                Debug.print("Location FROM: " + invStockTransferLine.getStlLocationFrom());

                if (invStockTransferLine.getStlLocationTo().equals(locationCode)) {
                    Debug.print("(9) Before Adding: Name: " + invStockTransferLine.getInvItem().getIiName() + "\tunpostedQuantity: " + unpostedQuantity + "\ttotalUnpostedQuantity" + totalUnpostedQuantity);
                    totalUnpostedQuantity += unpostedQuantity;
                    Debug.print("(9) After Adding: Name: " + invStockTransferLine.getInvItem().getIiName() + "\tunpostedQuantity: " + unpostedQuantity + "\ttotalUnpostedQuantity" + totalUnpostedQuantity);
                } else if (invStockTransferLine.getStlLocationFrom().equals(locationCode)) {
                    Debug.print("(9) Before Deducting: Name: " + invStockTransferLine.getInvItem().getIiName() + "\tunpostedQuantity: " + unpostedQuantity + "\ttotalUnpostedQuantity" + totalUnpostedQuantity);
                    totalUnpostedQuantity -= unpostedQuantity;
                    Debug.print("(9) After Deducting: Name: " + invStockTransferLine.getInvItem().getIiName() + "\tunpostedQuantity: " + unpostedQuantity + "\ttotalUnpostedQuantity" + totalUnpostedQuantity);
                }
            }

            // (10) INV Branch Stock Transfer Line
            String bstType = "";

            Collection invBranchStockTransferLines = invBranchStockTransferLineHome.findUnpostedBstByIiNameAndLocNameAndLessEqualDateAndBstAdBranch(itemName, locationName, AS_OF_DT, branchCode, AD_CMPNY);

            x = invBranchStockTransferLines.iterator();

            while (x.hasNext()) {

                LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) x.next();

                unpostedQuantity = invBranchStockTransferLine.getBslQuantity();

                bstType = invBranchStockTransferLine.getInvBranchStockTransfer().getBstType();

                Debug.print("(10) Name: " + invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName() + "\tUnposted Quantity: " + unpostedQuantity);

                if (bstType.equals("IN")) {
                    totalUnpostedQuantity += unpostedQuantity;
                } else if (bstType.equals("OUT")) {
                    totalUnpostedQuantity -= unpostedQuantity;
                }
            }

        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }

        return totalUnpostedQuantity;
    }

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("ArRepJobOrderPrintControllerBean convertForeignToFunctionalCurrency");
        LocalAdCompany adCompany;
        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        // Convert to functional currency if necessary
        if (CONVERSION_RATE != 1 && CONVERSION_RATE != 0) {

            AMOUNT = AMOUNT / CONVERSION_RATE;
        }
        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    private double calculateSlNetUnitCost(ArRepJobOrderPrintDetails mdetails, double tcRate, String tcType, short precisionUnit) {

        Debug.print("ArRepJobOrderPrintControllerBean calculateSlNetUnitCost");
        double amount = mdetails.getJpJolUnitPrice();
        if (tcType.equals("INCLUSIVE")) {

            amount = EJBCommon.roundIt(mdetails.getJpJolUnitPrice() / (1 + (tcRate / 100)), precisionUnit);
        }
        return amount;
    }

    private short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ArRepJobOrderPrintControllerBean getGlFcPrecisionUnit");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private double calculateSlTaxAmount(ArRepJobOrderPrintDetails mdetails, double tcRate, String tcType, double amount, short precisionUnit) {

        Debug.print("ArRepJobOrderPrintControllerBean calculateSlTaxAmount");
        double taxAmount = 0d;
        if (!tcType.equals("NONE") && !tcType.equals("EXEMPT")) {

            if (tcType.equals("INCLUSIVE")) {

                taxAmount = EJBCommon.roundIt(mdetails.getJpJolAmount() - amount, precisionUnit);

            } else if (tcType.equals("EXCLUSIVE")) {

                taxAmount = EJBCommon.roundIt(mdetails.getJpJolAmount() * tcRate / 100, precisionUnit);

            } else {

                // tax none zero-rated or exempt

            }
        }
        return taxAmount;
    }

    // private methods
    private ArrayList convertMiscToArrayList(String misc) {

        Debug.print("ArRepJobOrderPrintControllerBean convertMiscToArrayList");

        ArrayList list = new ArrayList();

        if (misc == null) {
            return list;
        }
        // String[] propertyCode, serialNumber, specs, custodian, expiryDate, tgDocumentNumber;
        String propertyCode = "";
        String serialNumber = "";
        String specs = "";
        String custodian = "";
        String expiryDate = "";
        String tgDocumentNumber = "";

        StringBuilder strB = new StringBuilder();

        // quantity
        String[] arrayMisc = misc.split("_");

        // property code
        misc = arrayMisc[1];
        arrayMisc = misc.split("@");
        String[] valueList = arrayMisc[0].split(",");
        strB = new StringBuilder();
        for (String s1 : valueList) {
            if (!s1.equals("")) {
                strB.append(s1);
                strB.append(System.getProperty("line.separator"));
            }
        }
        propertyCode = strB.toString();
        list.add(propertyCode);

        // serialNumber
        misc = arrayMisc[1];
        arrayMisc = misc.split("<");
        valueList = arrayMisc[0].split(",");
        strB = new StringBuilder();
        for (String element : valueList) {
            if (!element.equals("")) {
                strB.append(element);
                strB.append(System.getProperty("line.separator"));
            }
        }
        serialNumber = strB.toString();
        Debug.print("serial no: " + serialNumber);
        list.add(serialNumber);

        // specs
        misc = arrayMisc[1];
        arrayMisc = misc.split(">");
        valueList = arrayMisc[0].split(",");
        strB = new StringBuilder();
        for (String item : valueList) {
            if (!item.equals("")) {
                strB.append(item);
                strB.append(System.getProperty("line.separator"));
            }
        }
        specs = strB.toString();

        list.add(specs);

        // custodian
        misc = arrayMisc[1];
        arrayMisc = misc.split(";");
        valueList = arrayMisc[0].split(",");
        strB = new StringBuilder();
        for (String value : valueList) {
            if (!value.equals("")) {
                strB.append(value);
                strB.append(System.getProperty("line.separator"));
            }
        }
        custodian = strB.toString();
        list.add(custodian);

        // expiryDate

        misc = arrayMisc[1];
        arrayMisc = misc.split("%");
        valueList = arrayMisc[0].split(",");
        strB = new StringBuilder();
        for (String s : valueList) {
            if (!s.equals("")) {
                strB.append(s);
                strB.append(System.getProperty("line.separator"));
            }
        }
        expiryDate = strB.toString();
        list.add(expiryDate);

        return list;
    }

    private double calculateSlNetAmount(ArRepJobOrderPrintDetails mdetails, double tcRate, String tcType, short precisionUnit) {

        Debug.print("ArRepJobOrderPrintControllerBean calculateSlNetAmount");

        double amount = 0d;

        Debug.print("amount assigned: " + mdetails.getJpJolAmount());
        if (tcType.equals("INCLUSIVE")) {

            amount = EJBCommon.roundIt(mdetails.getJpJolAmount() / (1 + (tcRate / 100)), precisionUnit);

        } else {

            // tax exclusive, none, zero rated or exempt

            amount = mdetails.getJpJolAmount();
        }

        return amount;
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ArRepJobOrderPrintControllerBean ejbCreate");
    }

}