/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArRepSalesOrderPrintControllerBean
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
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
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
import com.util.EJBHomeFactory;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ar.ArRepSalesOrderPrintDetails;

import jakarta.ejb.*;

import java.util.*;

@Stateless(name = "ArRepSalesOrderPrintControllerEJB")
public class ArRepSalesOrderPrintControllerBean extends EJBContextClass implements ArRepSalesOrderPrintController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private LocalApPurchaseOrderLineHome apPurchaseOrderLineHome;
    @EJB
    private LocalApVoucherLineItemHome apVoucherLineItemHome;
    @EJB
    private LocalArAppliedInvoiceHome arAppliedInvoiceHome;
    @EJB
    private LocalArCustomerBalanceHome arCustomerBalanceHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalArInvoiceLineItemHome arInvoiceLineItemHome;
    @EJB
    private LocalArInvoicePaymentScheduleHome arInvoicePaymentScheduleHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalArSalesOrderHome arSalesOrderHome;
    @EJB
    private LocalArSalesOrderInvoiceLineHome arSalesOrderInvoiceLineHome;
    @EJB
    private LocalArSalesOrderLineHome arSalesOrderLineHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalInvAdjustmentLineHome invAdjustmentLineHome;
    @EJB
    private LocalInvBranchStockTransferLineHome invBranchStockTransferLineHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalInvStockTransferLineHome invStockTransferLineHome;


    public ArrayList executeArRepSalesOrderPrint(ArrayList soCodeList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepSalesOrderPrintControllerBean executeArRepSalesOrderPrint");

        ArrayList list = new ArrayList();


        try {

            LocalAdCompany adCompany = null;

            try {

                adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            } catch (FinderException ex) {

            }

            for (Object item : soCodeList) {

                Integer SO_CODE = (Integer) item;

                LocalArSalesOrder arSalesOrder = null;

                try {

                    arSalesOrder = arSalesOrderHome.findByPrimaryKey(SO_CODE);

                } catch (FinderException ex) {

                    continue;
                }

                Date CURR_DT = EJBCommon.getGcCurrentDateWoTime().getTime();

                // find customer balance before or equal invoice date

                Collection arCustomerBalances = null;

                try {

                    arCustomerBalances = arCustomerBalanceHome.findByBeforeOrEqualInvDateAndCstCustomerCode(CURR_DT, arSalesOrder.getArCustomer().getCstCustomerCode(), AD_CMPNY);

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

                double TOTAL_INV_SO_MTCHD = 0d;

                Collection arInvoices = arSalesOrder.getArCustomer().getArInvoices();

                Iterator invIter = arInvoices.iterator();

                while (invIter.hasNext()) {

                    LocalArInvoice arInvoice = (LocalArInvoice) invIter.next();

                    if (arInvoice.getInvPosted() == EJBCommon.FALSE || arInvoice.getInvCreditMemo() == EJBCommon.TRUE) {

                        continue;
                    }

                    if (arInvoice.getInvSoNumber() != null && !arInvoice.getInvSoNumber().equals("")) {

                        if (arInvoice.getInvSoNumber().equals(arSalesOrder.getSoDocumentNumber())) {

                            continue;
                        }

                        TOTAL_INV_SO_MTCHD += arInvoice.getInvAmountDue();
                    }
                }

                double TOTAL_SLS_ORDR = 0d;

                Collection arExistingSalesOrders = arSalesOrder.getArCustomer().getArSalesOrders();

                for (Object existingSalesOrder : arExistingSalesOrders) {

                    LocalArSalesOrder arExistingSalesOrder = (LocalArSalesOrder) existingSalesOrder;

                    if (arExistingSalesOrder.getSoPosted() == EJBCommon.FALSE) {

                        continue;
                    }

                    if (!arExistingSalesOrder.getSoDocumentNumber().equals(arSalesOrder.getSoDocumentNumber())) {

                        Collection arExistingSalesOrderLines = arExistingSalesOrder.getArSalesOrderLines();

                        for (Object arExistingSalesOrderLine : arExistingSalesOrderLines) {

                            LocalArSalesOrderLine arSalesOrderLine = (LocalArSalesOrderLine) arExistingSalesOrderLine;

                            TOTAL_SLS_ORDR += arSalesOrderLine.getSolAmount();
                        }
                    }
                }

                // get customer's last receipt

                double LST_RCT_AMNT = 0d;
                Date LST_RCT_DT = null;

                Collection arReceipts = arSalesOrder.getArCustomer().getArReceipts();

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

                    Collection arInvoicePaymentSchedules = arInvoicePaymentScheduleHome.findByCstCustomerCode(arSalesOrder.getArCustomer().getCstCustomerCode(), AD_CMPNY);

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

                // get sales order total amount

                Collection arSoLines = arSalesOrder.getArSalesOrderLines();
                double SO_AMNT = 0d;

                for (Object arSoLine : arSoLines) {

                    LocalArSalesOrderLine arSalesOrderLine = (LocalArSalesOrderLine) arSoLine;

                    SO_AMNT += arSalesOrderLine.getSolAmount();
                }

                // subtract sales order's amount from customer's balance if so is included in customer
                // balance

                arInvoices = null;

                arInvoices = arSalesOrder.getArCustomer().getArInvoices();

                invIter = arInvoices.iterator();

                while (invIter.hasNext()) {

                    LocalArInvoice arInvoice = (LocalArInvoice) invIter.next();

                    if (arInvoice.getInvSoNumber() != null && arInvoice.getInvSoNumber().equals(arSalesOrder.getSoDocumentNumber()) && arInvoice.getInvPosted() == EJBCommon.TRUE && arInvoice.getInvAmountDue() > arInvoice.getInvAmountPaid()) {

                        CST_BLNC -= SO_AMNT;
                    }
                }

                // get sales order lines

                Collection arSalesOrderLines = arSalesOrder.getArSalesOrderLines();

                for (Object salesOrderLine : arSalesOrderLines) {
                    Debug.print("Calls print here.");
                    LocalArSalesOrderLine arSalesOrderLine = (LocalArSalesOrderLine) salesOrderLine;

                    ArRepSalesOrderPrintDetails details = new ArRepSalesOrderPrintDetails();

                    String iii_Desc = arSalesOrderLine.getInvItemLocation().getInvItem().getIiDescription();
                    String eii_Desc = arSalesOrderLine.getSolLineIDesc();
                    String ii_DescDisp = "";

                    if (eii_Desc.trim().length() == 0) {
                        ii_DescDisp = iii_Desc;
                    } else if (eii_Desc.trim().length() > 0) {
                        ii_DescDisp = eii_Desc;
                    }

                    details.setSpSoDate(arSalesOrder.getSoDate());
                    details.setSpSoReferenceNumber(arSalesOrder.getSoReferenceNumber());
                    details.setSpSoDescription(arSalesOrder.getSoDescription());
                    details.setSpSoDocumentNumber(arSalesOrder.getSoDocumentNumber());
                    details.setSpSoTransactionType(arSalesOrder.getSoTransactionType());
                    details.setSpSoCreatedBy(arSalesOrder.getSoCreatedBy());
                    details.setSpSoApprovedRejectedBy(arSalesOrder.getSoApprovedRejectedBy());
                    details.setSpSolQuantity(arSalesOrderLine.getSolQuantity());
                    details.setSpSolUom(arSalesOrderLine.getInvUnitOfMeasure().getUomName());
                    details.setSpSolIiCode(arSalesOrderLine.getInvItemLocation().getInvItem().getIiName());
                    details.setSpSolLocName(arSalesOrderLine.getInvItemLocation().getInvLocation().getLocName());
                    details.setSpIlCategory(arSalesOrderLine.getInvItemLocation().getInvItem().getIiAdLvCategory());
                    details.setSpSolUnitPrice(arSalesOrderLine.getSolUnitPrice());
                    details.setSpSolAmount(arSalesOrderLine.getSolAmount());
                    details.setSpSoCstCustomerCode(arSalesOrder.getArCustomer().getCstCustomerCode());
                    details.setSpSoCstName(arSalesOrder.getArCustomer().getCstName());
                    details.setSpSoCstAddress(arSalesOrder.getArCustomer().getCstAddress());
                    details.setSpSoCstCreditLimit(arSalesOrder.getArCustomer().getCstCreditLimit());
                    // details.setSpSolIiDescription(arSalesOrderLine.getInvItemLocation().getInvItem().getIiDescription());

                    details.setSpSoCstContactPerson(arSalesOrder.getArCustomer().getCstContact());
                    details.setSpSoCstPhoneNumber(arSalesOrder.getArCustomer().getCstPhone());
                    details.setSpSoCstMobileNumber(arSalesOrder.getArCustomer().getCstMobilePhone());
                    details.setSpSoCstFax(arSalesOrder.getArCustomer().getCstFax());
                    details.setSpSoCstEmail(arSalesOrder.getArCustomer().getCstEmail());

                    details.setSpSolIiDescription(ii_DescDisp);

                    details.setSpSoFcSymbol(arSalesOrder.getGlFunctionalCurrency().getFcSymbol());
                    details.setSpSoCstCbBalance(CST_BLNC);
                    details.setSpSoCstPendingOrder(TOTAL_SLS_ORDR - TOTAL_INV_SO_MTCHD);
                    details.setSpSoAmount(SO_AMNT);
                    details.setSpSoCstLastReceiptAmount(LST_RCT_AMNT);
                    details.setSpSoCstLastReceiptDate(LST_RCT_DT);
                    details.setSpSoPaymentTerm(arSalesOrder.getAdPaymentTerm().getPytName());
                    details.setSpReportParameter(arSalesOrder.getReportParameter());
                    details.setSpSoShipTo(arSalesOrder.getSoShipTo());
                    details.setSpSoBillTo(arSalesOrder.getSoBillTo());
                    details.setSpSoMemo(arSalesOrder.getSoMemo());
                    details.setSpSoPosted(arSalesOrder.getSoPosted());

                    if (arSalesOrder.getArSalesperson() != null) {

                        details.setSpSoCstSlsSalespersonCode(arSalesOrder.getArSalesperson().getSlpSalespersonCode());
                        details.setSpSoCstSlsName(arSalesOrder.getArSalesperson().getSlpName());
                    }

                    // trace misc
                    if (arSalesOrderLine.getInvItemLocation().getInvItem().getIiTraceMisc() == EJBCommon.TRUE) {

                        if (arSalesOrderLine.getSolMisc() != null && arSalesOrderLine.getInvTags().size() <= 0) {
                            Debug.print("old code");
                            ArrayList miscArray = convertMiscToArrayList(arSalesOrderLine.getSolMisc());
                            // property code
                            details.setSpSolPropertyCode(miscArray.get(0).toString());
                            // serial number
                            details.setSpSolSerialNumber(miscArray.get(1).toString());
                            // specs
                            details.setSpSolSpecs(miscArray.get(2).toString());
                            // custodian
                            details.setSpSolCustodian(miscArray.get(3).toString());
                            // expiration date
                            details.setSpSolExpiryDate(miscArray.get(4).toString());
                        }

                        if (arSalesOrderLine.getInvTags().size() > 0) {
                            Debug.print("new code");
                            StringBuilder strBProperty = new StringBuilder();
                            StringBuilder strBSerial = new StringBuilder();
                            StringBuilder strBSpecs = new StringBuilder();
                            StringBuilder strBCustodian = new StringBuilder();
                            StringBuilder strBExpirationDate = new StringBuilder();

                            for (Object o : arSalesOrderLine.getInvTags()) {

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
                            details.setSpSolPropertyCode(strBProperty.toString());
                            // serial number
                            details.setSpSolSerialNumber(strBSerial.toString());
                            // specs
                            details.setSpSolSpecs(strBSpecs.toString());
                            // custodian
                            details.setSpSolCustodian(strBCustodian.toString());
                            // expiration date
                            details.setSpSolExpiryDate(strBExpirationDate.toString());
                        }
                    }
                    // get So Advance if have
                    double SO_ADVNC_AMNT = 0d;

                    for (Object value : arSalesOrder.getCmAdjustments()) {

                        LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) value;

                        if (cmAdjustment.getAdjVoid() == EJBCommon.FALSE && cmAdjustment.getAdjReconciled() == EJBCommon.FALSE) {
                            SO_ADVNC_AMNT += cmAdjustment.getAdjAmount();
                        }
                    }

                    details.setSpSoAdvanceAmount(SO_ADVNC_AMNT);

                    details.setSpSoCstCity(arSalesOrder.getArCustomer().getCstCity());
                    double unpostedQuantity = 0;
                    double committedQty = 0d;
                    try {

                        LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndIiNameAndLocName(arSalesOrderLine.getInvItemLocation().getInvItem().getIiName(), arSalesOrderLine.getInvItemLocation().getInvLocation().getLocName(), arSalesOrder.getSoAdBranch(), AD_CMPNY);

                        LocalAdBranchItemLocation adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arSalesOrderLine.getInvItemLocation().getIlCode(), arSalesOrder.getSoAdBranch(), AD_CMPNY);

                        unpostedQuantity = this.getShUnpostedQuantity(adBranchItemLocation, arSalesOrder.getSoDate(), AD_CMPNY);

                        // committed quantity
                        double solQty = 0d;
                        double deliveredQty = 0d;
                        committedQty = 0d;
                        GregorianCalendar gcDateFrom = new GregorianCalendar();
                        gcDateFrom.setTime(arSalesOrder.getSoDate());
                        gcDateFrom.add(Calendar.MONTH, -1);

                        Collection arSalesOrderLines2 = arSalesOrderLineHome.findCommittedQtyByIiNameAndLocNameAndSoAdBranch(adBranchItemLocation.getInvItemLocation().getInvItem().getIiName(), adBranchItemLocation.getInvItemLocation().getInvLocation().getLocName(), gcDateFrom.getTime(), arSalesOrder.getSoDate(), adBranchItemLocation.getAdBranch().getBrCode(), AD_CMPNY);

                        for (Object o : arSalesOrderLines2) {
                            LocalArSalesOrderLine arSalesOrderLine2 = (LocalArSalesOrderLine) o;

                            committedQty += (arSalesOrderLine2.getArSalesOrderInvoiceLines().size() == 0 ? arSalesOrderLine2.getSolQuantity() : 0);
                        }

                        Collection arSalesOrderInvoiceLines = arSalesOrderInvoiceLineHome.findCommittedQtyByIiNameAndLocNameAndSoAdBranch(adBranchItemLocation.getInvItemLocation().getInvItem().getIiName(), adBranchItemLocation.getInvItemLocation().getInvLocation().getLocName(), gcDateFrom.getTime(), arSalesOrder.getSoDate(), adBranchItemLocation.getAdBranch().getBrCode(), AD_CMPNY);

                        for (Object salesOrderInvoiceLine : arSalesOrderInvoiceLines) {
                            LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) salesOrderInvoiceLine;

                            solQty = arSalesOrderInvoiceLine.getArSalesOrderLine().getSolQuantity();
                            deliveredQty = arSalesOrderInvoiceLine.getSilQuantityDelivered();
                            committedQty += (solQty - deliveredQty);
                        }

                        Debug.print("Remaining QTY: " + invCosting.getCstRemainingQuantity());
                        Debug.print("Unposted QTY: " + unpostedQuantity);
                        Debug.print("Committed QTY: " + committedQty);
                        Debug.print("Total: " + (invCosting.getCstRemainingQuantity() + unpostedQuantity - committedQty));

                        if (invCosting.getCstRemainingQuantity() + unpostedQuantity - committedQty > arSalesOrderLine.getSolQuantity()) {

                            details.setSpSolIiStockStatus("A");

                        } else {

                            details.setSpSolIiStockStatus("O");
                        }

                    } catch (FinderException ex) {

                        details.setSpSolIiStockStatus("O");
                    }

                    // aging
                    details.setSpSoAgBucket0(AG_BCKT0);
                    details.setSpSoAgBucket1(AG_BCKT1);
                    details.setSpSoAgBucket2(AG_BCKT2);
                    details.setSpSoAgBucket3(AG_BCKT3);
                    details.setSpSoAgBucket4(AG_BCKT4);
                    details.setSpSoAgBucket5(AG_BCKT5);

                    // discount
                    details.setSpSolTotalDiscount(arSalesOrderLine.getSolTotalDiscount());
                    details.setSpSolDiscount1(arSalesOrderLine.getSolDiscount1());
                    details.setSpSolDiscount2(arSalesOrderLine.getSolDiscount2());
                    details.setSpSolDiscount3(arSalesOrderLine.getSolDiscount3());
                    details.setSpSolDiscount4(arSalesOrderLine.getSolDiscount4());

                    // get Tax Amount
                    double NT_AMNT = calculateSlNetAmount(details, arSalesOrder.getArTaxCode().getTcRate(), arSalesOrder.getArTaxCode().getTcType(), this.getGlFcPrecisionUnit(AD_CMPNY));

                    details.setSpSolTaxAmount(calculateSlTaxAmount(details, arSalesOrder.getArTaxCode().getTcRate(), arSalesOrder.getArTaxCode().getTcType(), NT_AMNT, this.getGlFcPrecisionUnit(AD_CMPNY)));

                    // get unit cost wo vat
                    details.setSpSolUnitCostWoTax(calculateSlNetUnitCost(details, arSalesOrder.getArTaxCode().getTcRate(), arSalesOrder.getArTaxCode().getTcType(), this.getGlFcPrecisionUnit(AD_CMPNY)));

                    details.setSpSoTaxType(arSalesOrder.getArTaxCode().getTcType());

                    // get quantity on hand
                    try {

                        LocalInvCosting invStockOnHand = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arSalesOrder.getSoDate(), arSalesOrderLine.getInvItemLocation().getInvItem().getIiName(), arSalesOrderLine.getInvItemLocation().getInvLocation().getLocName(), arSalesOrder.getSoAdBranch(), AD_CMPNY);

                        if (adCompany.getCmpShortName().equalsIgnoreCase("AMON")) {

                            details.setSpSolQuantityOnHand(invStockOnHand.getCstRemainingQuantity() + unpostedQuantity - committedQty);
                        } else {

                            details.setSpSolQuantityOnHand(invStockOnHand.getCstRemainingQuantity());
                        }

                    } catch (FinderException ex) {
                    }

                    Debug.print("item | qoh" + arSalesOrderLine.getInvItemLocation().getInvItem().getIiName() + "\t" + details.getSpSolQuantityOnHand());

                    details.setSpSoCustomerDealPrice(arSalesOrder.getArCustomer().getCstDealPrice());
                    list.add(details);
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

        Debug.print("ArRepSalesOrderPrintControllerBean getAdCompany");

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
            Collection arSalesOrderInvoiceLines = arSalesOrderInvoiceLineHome.findUnpostedInvcByIiNameAndLocNameAndLessEqualDateAndInvAdBranch(itemName, locationName, AS_OF_DT, branchCode, AD_CMPNY);

            x = arSalesOrderInvoiceLines.iterator();

            while (x.hasNext()) {
                LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) x.next();

                unpostedQuantity = arSalesOrderInvoiceLine.getSilQuantityDelivered();

                Debug.print("(6) Name: " + arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvItem().getIiName() + "\tUnposted Quantity: " + unpostedQuantity);

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

        Debug.print("ArRepSalesOrderPrintControllerBean convertForeignToFunctionalCurrency");

        LocalAdCompany adCompany = null;

        // get company and extended precision

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

    private double calculateSlNetUnitCost(ArRepSalesOrderPrintDetails mdetails, double tcRate, String tcType, short precisionUnit) {

        Debug.print("ArRepSalesOrderPrintControllerBean calculateSlNetUnitCost");

        double amount = mdetails.getSpSolUnitPrice();

        if (tcType.equals("INCLUSIVE")) {

            amount = EJBCommon.roundIt(mdetails.getSpSolUnitPrice() / (1 + (tcRate / 100)), precisionUnit);
        }

        return amount;
    }

    private short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ArRepSalesOrderPrintControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private double calculateSlTaxAmount(ArRepSalesOrderPrintDetails mdetails, double tcRate, String tcType, double amount, short precisionUnit) {

        Debug.print("ArRepSalesOrderPrintControllerBean calculateSlTaxAmount");

        double taxAmount = 0d;

        if (!tcType.equals("NONE") && !tcType.equals("EXEMPT")) {

            if (tcType.equals("INCLUSIVE")) {

                taxAmount = EJBCommon.roundIt(mdetails.getSpSolAmount() - amount, precisionUnit);

            } else if (tcType.equals("EXCLUSIVE")) {

                taxAmount = EJBCommon.roundIt(mdetails.getSpSolAmount() * tcRate / 100, precisionUnit);

            } else {

                // tax none zero-rated or exempt

            }
        }

        return taxAmount;
    }

    // private methods
    private ArrayList convertMiscToArrayList(String misc) {

        Debug.print("ArRepSalesOrderControllerBean convertMiscToArrayList");

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

    private double calculateSlNetAmount(ArRepSalesOrderPrintDetails mdetails, double tcRate, String tcType, short precisionUnit) {

        Debug.print("ArRepSalesOrderPrintControllerBean calculateSlNetAmount");

        double amount = 0d;

        if (tcType.equals("INCLUSIVE")) {

            amount = EJBCommon.roundIt(mdetails.getSpSolAmount() / (1 + (tcRate / 100)), precisionUnit);

        } else {

            // tax exclusive, none, zero rated or exempt

            amount = mdetails.getSpSolAmount();
        }

        return amount;
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ArRepSalesOrderPrintControllerBean ejbCreate");
    }

}