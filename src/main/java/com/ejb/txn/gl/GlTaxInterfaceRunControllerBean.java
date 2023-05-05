package com.ejb.txn.gl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ap.LocalApAppliedVoucher;
import com.ejb.entities.ap.LocalApCheck;
import com.ejb.dao.ap.LocalApCheckHome;
import com.ejb.entities.ap.LocalApDistributionRecord;
import com.ejb.dao.ap.LocalApDistributionRecordHome;
import com.ejb.entities.ap.LocalApPurchaseOrder;
import com.ejb.dao.ap.LocalApPurchaseOrderHome;
import com.ejb.dao.ap.LocalApTaxCodeHome;
import com.ejb.entities.ap.LocalApVoucher;
import com.ejb.dao.ap.LocalApVoucherHome;
import com.ejb.entities.ap.LocalApVoucherLineItem;
import com.ejb.entities.ap.LocalApWithholdingTaxCode;
import com.ejb.dao.ap.LocalApWithholdingTaxCodeHome;
import com.ejb.entities.ar.LocalArAppliedInvoice;
import com.ejb.entities.ar.LocalArDistributionRecord;
import com.ejb.dao.ar.LocalArDistributionRecordHome;
import com.ejb.entities.ar.LocalArInvoice;
import com.ejb.dao.ar.LocalArInvoiceHome;
import com.ejb.entities.ar.LocalArReceipt;
import com.ejb.dao.ar.LocalArReceiptHome;
import com.ejb.dao.ar.LocalArTaxCodeHome;
import com.ejb.entities.ar.LocalArWithholdingTaxCode;
import com.ejb.dao.ar.LocalArWithholdingTaxCodeHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.gl.LocalGlFunctionalCurrencyRate;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.entities.gl.LocalGlJournalLine;
import com.ejb.dao.gl.LocalGlJournalLineHome;
import com.ejb.entities.gl.LocalGlTaxInterface;
import com.ejb.dao.gl.LocalGlTaxInterfaceHome;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.gl.GlTaxInterfaceDetails;

@Stateless(name = "GlTaxInterfaceRunControllerEJB")
public class GlTaxInterfaceRunControllerBean extends EJBContextClass implements GlTaxInterfaceRunController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalApCheckHome apCheckHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    private LocalApPurchaseOrderHome apReceivingItemHome;
    @EJB
    private LocalApTaxCodeHome apTaxCodeHome;
    @EJB
    private LocalApVoucherHome apVoucherHome;
    @EJB
    private LocalApWithholdingTaxCodeHome apWithholdingTaxCodeHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalArTaxCodeHome arTaxCodeHome;
    @EJB
    private LocalArWithholdingTaxCodeHome arWithholdingTaxCodeHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalGlJournalLineHome glJournalLineHome;
    @EJB
    private LocalGlTaxInterfaceHome glTaxInterfaceHome;

    public void runTaxInterface(String DOC_TYP, Date DT_FRM, Date DT_TO, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("GlTaxInterfaceRunControllerBean arTaxInterface");

        try {

            if (DOC_TYP.equals("AR INVOICE") || DOC_TYP.equals("AR CREDIT MEMO") || DOC_TYP.equals("AR RECEIPT")) {

                this.runArTaxCode(DOC_TYP, DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);
                this.runArWithholdingTaxCode(DOC_TYP, DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);

            } else if (DOC_TYP.equals("AP VOUCHER") || DOC_TYP.equals("AP DEBIT MEMO") || DOC_TYP.equals("AP CHECK") || DOC_TYP.equals("AP RECEIVING ITEM")) {

                this.runApTaxCode(DOC_TYP, DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);
                this.runApWithholdingTaxCode(DOC_TYP, DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);

            } else {

                this.runArTaxCode(DOC_TYP, DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);
                this.runArWithholdingTaxCode(DOC_TYP, DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);
                this.runApTaxCode(DOC_TYP, DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);
                this.runApWithholdingTaxCode(DOC_TYP, DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    //  private methods
    private short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("GlTaxInterfaceRunControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void runArTaxCode(String DOC_TYP, Date DT_FRM, Date DT_TO, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("GlTaxInterfaceRunControllerBean arTaxCodeInteraface");

        // ar tax
        short PRECISION_UNIT = this.getGlFcPrecisionUnit(AD_CMPNY);

        try {

            if (DOC_TYP.equals("AR INVOICE") || DOC_TYP.equals("")) {
                Debug.print("AR INVOICE");

                Collection arInvoices = arInvoiceHome.findPostedTaxInvByInvDateRange(EJBCommon.FALSE, DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);

                for (Object invoice : arInvoices) {

                    LocalArInvoice arInvoice = (LocalArInvoice) invoice;

                    if (arInvoice.getArTaxCode().getTcInterimAccount() != null) continue;

                    removeExisting("AR INVOICE", arInvoice.getInvCode(), AD_CMPNY);

                    // create tax interface

                    GlTaxInterfaceDetails details = new GlTaxInterfaceDetails();

                    Collection apDrs = arInvoice.getArDistributionRecords();

                    double SALES_AMOUNT = 0d;
                    Integer DR_CODE = 0;
                    for (Object apDr : apDrs) {

                        LocalArDistributionRecord apDistributionRecord = (LocalArDistributionRecord) apDr;
                        if (apDistributionRecord.getDrClass().equals("RECEIVABLE")) {
                            SALES_AMOUNT += apDistributionRecord.getDrAmount();
                        }

                        if (apDistributionRecord.getDrClass().equals("TAX")) {
                            DR_CODE = apDistributionRecord.getDrCode();
                        }
                    }

                    Debug.print("SALES_AMOUNT=" + SALES_AMOUNT);
                    double NET_AMNT = EJBCommon.roundIt(SALES_AMOUNT / ((arInvoice.getArTaxCode().getTcRate() / 100) + 1), PRECISION_UNIT);
                    details.setTiDocumentType("AR INVOICE");
                    details.setTiSource("AR");
                    details.setTiNetAmount(NET_AMNT);
                    details.setTiTaxAmount(EJBCommon.roundIt(SALES_AMOUNT - NET_AMNT, PRECISION_UNIT));
                    details.setTiSalesAmount(EJBCommon.roundIt(SALES_AMOUNT, PRECISION_UNIT));
                    details.setTiServicesAmount(0d);
                    details.setTiCapitalGoodsAmount(0d);
                    details.setTiOtherCapitalGoodsAmount(0d);
                    details.setTiTxnCode(arInvoice.getInvCode());
                    details.setTiTxnDate(arInvoice.getInvDate());
                    details.setTiTxnDocumentNumber(arInvoice.getInvNumber());
                    details.setTiTxnReferenceNumber(arInvoice.getInvReferenceNumber());
                    details.setTiTaxExempt(EJBCommon.roundIt(arInvoice.getArTaxCode().getTcName().equals("EXEMPT") ? SALES_AMOUNT : 0d, PRECISION_UNIT));
                    details.setTiTaxZeroRated(EJBCommon.roundIt(arInvoice.getArTaxCode().getTcName().equals("ZERO-RATED") ? SALES_AMOUNT : 0d, PRECISION_UNIT));
                    details.setTiTxlCode(DR_CODE);
                    details.setTiTxlCoaCode(arInvoice.getArTaxCode().getGlChartOfAccount() != null ? arInvoice.getArTaxCode().getGlChartOfAccount().getCoaCode() : null);
                    details.setTiTcCode(arInvoice.getArTaxCode().getTcCode());
                    details.setTiWtcCode(null);
                    details.setTiSlCode(arInvoice.getArCustomer().getCstCode());
                    details.setTiSlTin(arInvoice.getArCustomer().getCstTin());
                    details.setTiSlSubledgerCode(arInvoice.getArCustomer().getCstCustomerCode());
                    details.setTiSlName(arInvoice.getArCustomer().getCstName());
                    details.setTiSlAddress(arInvoice.getArCustomer().getCstAddress());
                    details.setTiSlAddress2(arInvoice.getArCustomer().getCstCity() + " " + arInvoice.getArCustomer().getCstStateProvince());

                    details.setTiIsArDocument(EJBCommon.TRUE);
                    details.setTiAdBranch(AD_BRNCH);
                    details.setTiAdCompany(AD_CMPNY);

                    this.createTaxInterface(details);
                }
            }

            if (DOC_TYP.equals("AR RECEIPT") || DOC_TYP.equals("")) {
                Debug.print("AR RECEIPT");

                Collection arReceipts = arReceiptHome.findPostedTaxRctByRctDateRange(DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);

                for (Object receipt : arReceipts) {

                    LocalArReceipt arReceipt = (LocalArReceipt) receipt;

                    removeExisting("AR RECEIPT", arReceipt.getRctCode(), AD_CMPNY);

                    // create tax interface

                    GlTaxInterfaceDetails details = new GlTaxInterfaceDetails();

                    Collection apDrs = arReceipt.getArDistributionRecords();

                    double SALES_AMOUNT = 0d;
                    Integer DR_CODE = 0;
                    for (Object apDr : apDrs) {

                        LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) apDr;

                        Debug.print("get tax and sales 1 in code " + arDistributionRecord.getDrCode());
                        if (arDistributionRecord.getDrClass().equals("RECEIVABLE")) {
                            SALES_AMOUNT += arDistributionRecord.getDrAmount();
                        }

                        if (arDistributionRecord.getDrClass().equals("TAX")) {
                            Debug.print("get code");
                            DR_CODE = arDistributionRecord.getDrCode();
                        }
                    }

                    ArrayList arAppliedInvoiceList = new ArrayList(arReceipt.getArAppliedInvoices());
                    LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) arAppliedInvoiceList.get(0);
                    LocalArInvoice arInvoice = arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice();

                    Debug.print("SALES_AMOUNT=" + SALES_AMOUNT);
                    double NET_AMNT = EJBCommon.roundIt(SALES_AMOUNT / ((arInvoice.getArTaxCode().getTcRate() / 100) + 1), PRECISION_UNIT);
                    details.setTiDocumentType("AR RECEIPT");
                    details.setTiSource("AR");
                    details.setTiNetAmount(NET_AMNT);
                    details.setTiTaxAmount(EJBCommon.roundIt(SALES_AMOUNT - NET_AMNT, PRECISION_UNIT));
                    details.setTiSalesAmount(EJBCommon.roundIt(SALES_AMOUNT, PRECISION_UNIT));
                    details.setTiServicesAmount(0d);
                    details.setTiCapitalGoodsAmount(0d);
                    details.setTiOtherCapitalGoodsAmount(0d);
                    details.setTiTxnCode(arReceipt.getRctCode());
                    details.setTiTxnDate(arReceipt.getRctDate());
                    details.setTiTxnDocumentNumber(arReceipt.getRctNumber());
                    details.setTiTxnReferenceNumber(arReceipt.getRctReferenceNumber());
                    details.setTiTaxExempt(EJBCommon.roundIt(arInvoice.getArTaxCode().getTcName().equals("EXEMPT") ? SALES_AMOUNT : 0d, PRECISION_UNIT));
                    details.setTiTaxZeroRated(EJBCommon.roundIt(arInvoice.getArTaxCode().getTcName().equals("ZERO-RATED") ? SALES_AMOUNT : 0d, PRECISION_UNIT));
                    details.setTiTxlCode(DR_CODE);
                    details.setTiTxlCoaCode(arInvoice.getArTaxCode().getGlChartOfAccount() != null ? arInvoice.getArTaxCode().getGlChartOfAccount().getCoaCode() : null);
                    details.setTiTcCode(arInvoice.getArTaxCode().getTcCode());
                    details.setTiWtcCode(null);
                    details.setTiSlCode(arInvoice.getArCustomer().getCstCode());
                    details.setTiSlTin(arInvoice.getArCustomer().getCstTin());
                    details.setTiSlSubledgerCode(arInvoice.getArCustomer().getCstCustomerCode());
                    details.setTiSlName(arInvoice.getArCustomer().getCstName());
                    details.setTiSlAddress(arInvoice.getArCustomer().getCstAddress());
                    details.setTiSlAddress2(arInvoice.getArCustomer().getCstCity() + " " + arInvoice.getArCustomer().getCstStateProvince());

                    details.setTiIsArDocument(EJBCommon.TRUE);
                    details.setTiAdBranch(AD_BRNCH);
                    details.setTiAdCompany(AD_CMPNY);

                    this.createTaxInterface(details);
                }
            }

            if (DOC_TYP.equals("AR RECEIPT") || DOC_TYP.equals("")) {
                Debug.print("AR MISC RECEIPT");

                Collection arReceipts = arReceiptHome.findPostedTaxMiscRctByRctDateRange(DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);

                for (Object receipt : arReceipts) {

                    LocalArReceipt arReceipt = (LocalArReceipt) receipt;

                    removeExisting("AR RECEIPT", arReceipt.getRctCode(), AD_CMPNY);

                    // create tax interface

                    GlTaxInterfaceDetails details = new GlTaxInterfaceDetails();

                    Collection apDrs = arReceipt.getArDistributionRecords();

                    double SALES_AMOUNT = 0d;
                    Integer DR_CODE = 0;
                    for (Object apDr : apDrs) {

                        LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) apDr;

                        Debug.print("get tax and sales 2 in code " + arDistributionRecord.getDrCode());
                        if (arDistributionRecord.getDrClass().equals("CASH")) {
                            SALES_AMOUNT += arDistributionRecord.getDrAmount();
                        }

                        if (arDistributionRecord.getDrClass().equals("TAX")) {

                            Debug.print("get code");
                            DR_CODE = arDistributionRecord.getDrCode();
                        }
                    }

                    Debug.print("SALES_AMOUNT=" + SALES_AMOUNT);
                    double NET_AMNT = EJBCommon.roundIt(SALES_AMOUNT / ((arReceipt.getArTaxCode().getTcRate() / 100) + 1), PRECISION_UNIT);
                    details.setTiDocumentType("AR RECEIPT");
                    details.setTiSource("AR");
                    details.setTiNetAmount(NET_AMNT);
                    details.setTiTaxAmount(EJBCommon.roundIt(SALES_AMOUNT - NET_AMNT, PRECISION_UNIT));
                    details.setTiSalesAmount(EJBCommon.roundIt(SALES_AMOUNT, PRECISION_UNIT));
                    details.setTiServicesAmount(0d);
                    details.setTiCapitalGoodsAmount(0d);
                    details.setTiOtherCapitalGoodsAmount(0d);
                    details.setTiTxnCode(arReceipt.getRctCode());
                    details.setTiTxnDate(arReceipt.getRctDate());
                    details.setTiTxnDocumentNumber(arReceipt.getRctNumber());
                    details.setTiTxnReferenceNumber(arReceipt.getRctReferenceNumber());
                    details.setTiTaxExempt(EJBCommon.roundIt(arReceipt.getArTaxCode().getTcName().equals("EXEMPT") ? SALES_AMOUNT : 0d, PRECISION_UNIT));
                    details.setTiTaxZeroRated(EJBCommon.roundIt(arReceipt.getArTaxCode().getTcName().equals("ZERO-RATED") ? SALES_AMOUNT : 0d, PRECISION_UNIT));
                    details.setTiTxlCode(DR_CODE);
                    details.setTiTxlCoaCode(arReceipt.getArTaxCode().getGlChartOfAccount() != null ? arReceipt.getArTaxCode().getGlChartOfAccount().getCoaCode() : null);
                    details.setTiTcCode(arReceipt.getArTaxCode().getTcCode());
                    details.setTiWtcCode(null);
                    details.setTiSlCode(arReceipt.getArCustomer().getCstCode());
                    details.setTiSlTin(arReceipt.getArCustomer().getCstTin());
                    details.setTiSlSubledgerCode(arReceipt.getArCustomer().getCstCustomerCode());
                    details.setTiSlName(arReceipt.getArCustomer().getCstName());
                    details.setTiSlAddress(arReceipt.getArCustomer().getCstAddress());
                    details.setTiSlAddress2(arReceipt.getArCustomer().getCstCity() + " " + arReceipt.getArCustomer().getCstStateProvince());

                    details.setTiIsArDocument(EJBCommon.TRUE);
                    details.setTiAdBranch(AD_BRNCH);
                    details.setTiAdCompany(AD_CMPNY);

                    this.createTaxInterface(details);
                }
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void runArWithholdingTaxCode(String DOC_TYP, Date DT_FRM, Date DT_TO, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("GlTaxInterfaceRunControllerBean arWithholdingTaxCodeInteraface");

        // ar withholding tax
        short PRECISION_UNIT = this.getGlFcPrecisionUnit(AD_CMPNY);

        try {

            Collection arWithholdingTaxCodes = arWithholdingTaxCodeHome.findEnabledWtcAll(AD_CMPNY);

            for (Object withholdingTaxCode : arWithholdingTaxCodes) {

                LocalArWithholdingTaxCode arWithholdingTaxCode = (LocalArWithholdingTaxCode) withholdingTaxCode;

                LocalGlChartOfAccount glChartOfAccount = arWithholdingTaxCode.getGlChartOfAccount();

                if (glChartOfAccount != null) {

                    // AR INVOICE

                    if (DOC_TYP.equals("AR INVOICE") || DOC_TYP.equals("")) {

                        Collection arDistributionRecords = arDistributionRecordHome.findInvByDateRangeAndCoaAccountNumber(EJBCommon.FALSE, DT_FRM, DT_TO, glChartOfAccount.getCoaCode(), AD_CMPNY);

                        for (Object distributionRecord : arDistributionRecords) {

                            LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                            Integer invoiceWtcCode = null;
                            if (arDistributionRecord.getArInvoice().getArWithholdingTaxCode() != null) {

                                invoiceWtcCode = arDistributionRecord.getArInvoice().getArWithholdingTaxCode().getWtcCode();
                            }

                            if ((invoiceWtcCode != null && invoiceWtcCode.equals(arWithholdingTaxCode.getWtcCode()))) {

                                removeExisting("AR INVOICE", arDistributionRecord.getDrCode(), AD_CMPNY);

                                double DR_AMNT = this.convertForeignToFunctionalCurrency(arDistributionRecord.getArInvoice().getGlFunctionalCurrency().getFcCode(), arDistributionRecord.getArInvoice().getGlFunctionalCurrency().getFcName(), arDistributionRecord.getArInvoice().getInvConversionDate(), arDistributionRecord.getArInvoice().getInvConversionRate(), arDistributionRecord.getDrAmount(), AD_CMPNY);

                                if (arWithholdingTaxCode.getWtcRate() == 0) continue;

                                double NET_AMNT = DR_AMNT / (arWithholdingTaxCode.getWtcRate() / 100);

                                GlTaxInterfaceDetails details = new GlTaxInterfaceDetails();
                                details.setTiDocumentType("AR INVOICE");
                                details.setTiSource("AR");
                                details.setTiNetAmount(NET_AMNT);
                                details.setTiSalesAmount(EJBCommon.roundIt(NET_AMNT + DR_AMNT, PRECISION_UNIT));
                                details.setTiTxnCode(arDistributionRecord.getArInvoice().getInvCode());
                                details.setTiTxnDate(arDistributionRecord.getArInvoice().getInvDate());
                                details.setTiTxnDocumentNumber(arDistributionRecord.getArInvoice().getInvNumber());
                                details.setTiTxnReferenceNumber(arDistributionRecord.getArInvoice().getInvReferenceNumber());
                                details.setTiTxlCode(arDistributionRecord.getDrCode());
                                details.setTiTxlCoaCode(arDistributionRecord.getGlChartOfAccount().getCoaCode());
                                details.setTiTcCode(null);
                                details.setTiWtcCode(invoiceWtcCode);
                                details.setTiSlCode(arDistributionRecord.getArInvoice().getArCustomer().getCstCode());
                                details.setTiSlTin(arDistributionRecord.getArInvoice().getArCustomer().getCstTin());
                                details.setTiSlSubledgerCode(arDistributionRecord.getArInvoice().getArCustomer().getCstCustomerCode());
                                details.setTiSlName(arDistributionRecord.getArInvoice().getArCustomer().getCstName());
                                details.setTiSlAddress(arDistributionRecord.getArInvoice().getArCustomer().getCstAddress());
                                details.setTiIsArDocument(EJBCommon.TRUE);
                                details.setTiAdBranch(AD_BRNCH);
                                details.setTiAdCompany(AD_CMPNY);

                                this.createTaxInterface(details);
                            }
                        }
                    }

                    // AR CREDIT MEMO

                    if (DOC_TYP.equals("AR CREDIT MEMO") || DOC_TYP.equals("")) {

                        Collection arDistributionRecords = arDistributionRecordHome.findInvByDateRangeAndCoaAccountNumber(EJBCommon.TRUE, DT_FRM, DT_TO, glChartOfAccount.getCoaCode(), AD_CMPNY);

                        for (Object distributionRecord : arDistributionRecords) {

                            LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;
                            LocalArInvoice arInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arDistributionRecord.getArInvoice().getInvCmInvoiceNumber(), EJBCommon.FALSE, AD_BRNCH, AD_CMPNY);

                            Integer creditmemoWtcCode = null;
                            if (arInvoice.getArWithholdingTaxCode() != null) {

                                creditmemoWtcCode = arInvoice.getArWithholdingTaxCode().getWtcCode();
                            }

                            if ((creditmemoWtcCode != null && creditmemoWtcCode.equals(arWithholdingTaxCode.getWtcCode()))) {
                                removeExisting("AR CREDIT MEMO", arDistributionRecord.getDrCode(), AD_CMPNY);
                                double DR_AMNT = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), AD_CMPNY);

                                if (arWithholdingTaxCode.getWtcRate() == 0) continue;

                                double NET_AMNT = DR_AMNT / (arWithholdingTaxCode.getWtcRate() / 100);

                                GlTaxInterfaceDetails details = new GlTaxInterfaceDetails();
                                details.setTiDocumentType("AR CREDIT MEMO");
                                details.setTiSource("AR");
                                details.setTiNetAmount(NET_AMNT);
                                details.setTiSalesAmount(EJBCommon.roundIt(NET_AMNT + DR_AMNT, PRECISION_UNIT));
                                details.setTiTxnCode(arDistributionRecord.getArInvoice().getInvCode());
                                details.setTiTxnDate(arDistributionRecord.getArInvoice().getInvDate());
                                details.setTiTxnDocumentNumber(arDistributionRecord.getArInvoice().getInvNumber());
                                details.setTiTxnReferenceNumber(arDistributionRecord.getArInvoice().getInvCmInvoiceNumber());
                                details.setTiTxlCode(arDistributionRecord.getDrCode());
                                details.setTiTxlCoaCode(arDistributionRecord.getGlChartOfAccount().getCoaCode());
                                details.setTiTcCode(null);
                                details.setTiWtcCode(creditmemoWtcCode);
                                details.setTiSlCode(arDistributionRecord.getArInvoice().getArCustomer().getCstCode());
                                details.setTiSlTin(arDistributionRecord.getArInvoice().getArCustomer().getCstTin());
                                details.setTiSlSubledgerCode(arDistributionRecord.getArInvoice().getArCustomer().getCstCustomerCode());
                                details.setTiSlName(arDistributionRecord.getArInvoice().getArCustomer().getCstName());
                                details.setTiSlAddress(arDistributionRecord.getArInvoice().getArCustomer().getCstAddress());
                                details.setTiIsArDocument(EJBCommon.TRUE);
                                details.setTiAdBranch(AD_BRNCH);
                                details.setTiAdCompany(AD_CMPNY);

                                this.createTaxInterface(details);
                            }
                        }
                    }

                    // AR RECEIPT

                    if (DOC_TYP.equals("AR RECEIPT") || DOC_TYP.equals("")) {

                        Collection arDistributionRecords = arDistributionRecordHome.findRctByDateRangeAndCoaAccountNumber(DT_FRM, DT_TO, glChartOfAccount.getCoaCode(), AD_CMPNY);

                        for (Object distributionRecord : arDistributionRecords) {

                            LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                            Integer receiptWtcCode = null;
                            if (arDistributionRecord.getArReceipt().getArWithholdingTaxCode() != null) {

                                receiptWtcCode = arDistributionRecord.getArReceipt().getArWithholdingTaxCode().getWtcCode();
                            }

                            Integer appliedWtcCode = null;
                            if (arDistributionRecord.getArAppliedInvoice() != null) {

                                double appliedWtcRate = arDistributionRecord.getArAppliedInvoice().getArInvoicePaymentSchedule().getArInvoice().getArWithholdingTaxCode().getWtcRate();

                                if (appliedWtcRate == 0d) {

                                    LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
                                    appliedWtcCode = adPreference.getArWithholdingTaxCode().getWtcCode();

                                } else {

                                    appliedWtcCode = arDistributionRecord.getArAppliedInvoice().getArInvoicePaymentSchedule().getArInvoice().getArWithholdingTaxCode().getWtcCode();
                                }
                            }

                            if (((receiptWtcCode != null && receiptWtcCode.equals(arWithholdingTaxCode.getWtcCode())) || (appliedWtcCode != null && appliedWtcCode.equals(arWithholdingTaxCode.getWtcCode())))) {
                                removeExisting("AR RECEIPT", arDistributionRecord.getDrCode(), AD_CMPNY);
                                LocalArAppliedInvoice arAppliedInvoice = arDistributionRecord.getArAppliedInvoice();
                                LocalArInvoice arInvoice = null;

                                double DR_AMNT = 0d;
                                if (arAppliedInvoice == null) {

                                    DR_AMNT = this.convertForeignToFunctionalCurrency(arDistributionRecord.getArReceipt().getGlFunctionalCurrency().getFcCode(), arDistributionRecord.getArReceipt().getGlFunctionalCurrency().getFcName(), arDistributionRecord.getArReceipt().getRctConversionDate(), arDistributionRecord.getArReceipt().getRctConversionRate(), arDistributionRecord.getDrAmount(), AD_CMPNY);

                                } else {

                                    arInvoice = arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice();

                                    DR_AMNT = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), AD_CMPNY);
                                }

                                if (arWithholdingTaxCode.getWtcRate() == 0) continue;

                                double NET_AMNT = DR_AMNT / (arWithholdingTaxCode.getWtcRate() / 100);

                                GlTaxInterfaceDetails details = new GlTaxInterfaceDetails();
                                details.setTiDocumentType("AR RECEIPT");
                                details.setTiSource("AR");
                                details.setTiNetAmount(NET_AMNT);
                                details.setTiSalesAmount(EJBCommon.roundIt(NET_AMNT + DR_AMNT, PRECISION_UNIT));
                                details.setTiTxnCode(arDistributionRecord.getArReceipt().getRctCode());
                                details.setTiTxnDate(arDistributionRecord.getArReceipt().getRctDate());
                                details.setTiTxnDocumentNumber(arDistributionRecord.getArReceipt().getRctNumber());
                                details.setTiTxnReferenceNumber(arDistributionRecord.getArReceipt().getRctReferenceNumber());
                                details.setTiTxlCode(arDistributionRecord.getDrCode());
                                details.setTiTxlCoaCode(arDistributionRecord.getGlChartOfAccount().getCoaCode());
                                details.setTiTcCode(null);
                                if (arInvoice == null) {

                                    details.setTiWtcCode(receiptWtcCode);

                                } else {

                                    details.setTiWtcCode(appliedWtcCode);
                                }
                                details.setTiSlCode(arDistributionRecord.getArReceipt().getArCustomer().getCstCode());
                                details.setTiSlTin(arDistributionRecord.getArReceipt().getArCustomer().getCstTin());
                                details.setTiSlSubledgerCode(arDistributionRecord.getArReceipt().getArCustomer().getCstCustomerCode());
                                details.setTiSlName(arDistributionRecord.getArReceipt().getArCustomer().getCstName());
                                details.setTiSlAddress(arDistributionRecord.getArReceipt().getArCustomer().getCstAddress());
                                details.setTiIsArDocument(EJBCommon.TRUE);
                                details.setTiAdBranch(AD_BRNCH);
                                details.setTiAdCompany(AD_CMPNY);

                                this.createTaxInterface(details);
                            }
                        }
                    }

                    // GL JOURNAL

                    Collection glJournalLines = glJournalLineHome.findManualJrByEffectiveDateRangeAndCoaCode(DT_FRM, DT_TO, glChartOfAccount.getCoaCode(), AD_CMPNY);

                    for (Object journalLine : glJournalLines) {

                        LocalGlJournalLine glJournalLine = (LocalGlJournalLine) journalLine;

                        removeExisting("GL JOURNAL", glJournalLine.getJlCode(), AD_CMPNY);

                        // if(!existing) {

                        GlTaxInterfaceDetails details = new GlTaxInterfaceDetails();
                        details.setTiDocumentType("GL JOURNAL");
                        details.setTiSource("GL");
                        details.setTiNetAmount(0d);
                        details.setTiTxnCode(glJournalLine.getGlJournal().getJrCode());
                        details.setTiTxnDate(glJournalLine.getGlJournal().getJrEffectiveDate());
                        details.setTiTxnDocumentNumber(glJournalLine.getGlJournal().getJrDocumentNumber());
                        details.setTiTxnReferenceNumber(null);
                        details.setTiTxlCode(glJournalLine.getJlCode());
                        details.setTiTxlCoaCode(glJournalLine.getGlChartOfAccount().getCoaCode());
                        details.setTiTcCode(null);
                        details.setTiWtcCode(null);
                        details.setTiSlCode(null);
                        details.setTiSlTin(null);
                        details.setTiSlSubledgerCode(null);
                        details.setTiSlName(null);
                        details.setTiSlAddress(null);
                        details.setTiIsArDocument(EJBCommon.FALSE);
                        details.setTiAdBranch(AD_BRNCH);
                        details.setTiAdCompany(AD_CMPNY);

                        this.createTaxInterface(details);

                        // }

                    }
                }
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void runApTaxCode(String DOC_TYP, Date DT_FRM, Date DT_TO, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("GlTaxInterfaceRunControllerBean apTaxCodeInterface");

        // ap tax
        short PRECISION_UNIT = this.getGlFcPrecisionUnit(AD_CMPNY);

        try {

            if (DOC_TYP.equals("AP VOUCHER") || DOC_TYP.equals("")) {
                Debug.print("AP VOUCHER");

                Collection apVouchers = apVoucherHome.findPostedTaxVouByVouDateRange(EJBCommon.FALSE, DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);

                for (Object voucher : apVouchers) {

                    LocalApVoucher apVoucher = (LocalApVoucher) voucher;

                    removeExisting("AP VOUCHER", apVoucher.getVouCode(), AD_CMPNY);

                    // create tax interface

                    GlTaxInterfaceDetails details = new GlTaxInterfaceDetails();

                    Collection apDrs = apVoucher.getApDistributionRecords();

                    double SALES_AMOUNT = 0d;
                    double SERVICES = 0d;
                    double CAPITAL_GOODS = 0d;
                    double OTHER_CAPITAL_GOODS = 0d;

                    for (Object apDr : apDrs) {

                        LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) apDr;
                        if (apDistributionRecord.getDrClass().equals("PAYABLE")) {
                            SALES_AMOUNT += apDistributionRecord.getDrAmount();
                        }

                        if (apDistributionRecord.getGlChartOfAccount().getCoaTaxType().equals("SERVICES")) {
                            SERVICES += apDistributionRecord.getDrAmount();
                        }

                        if (apDistributionRecord.getGlChartOfAccount().getCoaTaxType().equals("CAPITAL GOODS")) {
                            CAPITAL_GOODS += apDistributionRecord.getDrAmount();
                        }

                        if (apDistributionRecord.getGlChartOfAccount().getCoaTaxType().equals("OTHER CAPITAL GOODS")) {
                            OTHER_CAPITAL_GOODS += apDistributionRecord.getDrAmount();
                        }
                    }

                    Debug.print("SALES_AMOUNT=" + SALES_AMOUNT);
                    double NET_AMNT = EJBCommon.roundIt(SALES_AMOUNT / ((apVoucher.getApTaxCode().getTcRate() / 100) + 1), PRECISION_UNIT);
                    details.setTiDocumentType("AP VOUCHER");
                    details.setTiSource("AP");
                    details.setTiNetAmount(NET_AMNT);
                    details.setTiTaxAmount(EJBCommon.roundIt(SALES_AMOUNT - NET_AMNT, PRECISION_UNIT));
                    details.setTiSalesAmount(EJBCommon.roundIt(SALES_AMOUNT, PRECISION_UNIT));
                    details.setTiServicesAmount(EJBCommon.roundIt(SERVICES * ((apVoucher.getApTaxCode().getTcRate() / 100) + 1), PRECISION_UNIT));
                    details.setTiCapitalGoodsAmount(EJBCommon.roundIt(CAPITAL_GOODS * ((apVoucher.getApTaxCode().getTcRate() / 100) + 1), PRECISION_UNIT));
                    details.setTiOtherCapitalGoodsAmount(EJBCommon.roundIt(OTHER_CAPITAL_GOODS * ((apVoucher.getApTaxCode().getTcRate() / 100) + 1), PRECISION_UNIT));
                    details.setTiTxnCode(apVoucher.getVouCode());
                    details.setTiTxnDate(apVoucher.getVouDate());
                    details.setTiTxnDocumentNumber(apVoucher.getVouDocumentNumber());
                    details.setTiTxnReferenceNumber(apVoucher.getVouReferenceNumber());
                    details.setTiTaxExempt(EJBCommon.roundIt(apVoucher.getApTaxCode().getTcName().equals("EXEMPT") ? SALES_AMOUNT : 0d, PRECISION_UNIT));
                    details.setTiTaxZeroRated(EJBCommon.roundIt(apVoucher.getApTaxCode().getTcName().equals("ZERO-RATED") ? SALES_AMOUNT : 0d, PRECISION_UNIT));
                    details.setTiTxlCode(0);
                    details.setTiTxlCoaCode(apVoucher.getApTaxCode().getGlChartOfAccount() != null ? apVoucher.getApTaxCode().getGlChartOfAccount().getCoaCode() : null);
                    details.setTiTcCode(apVoucher.getApTaxCode().getTcCode());
                    details.setTiWtcCode(null);
                    details.setTiSlCode(apVoucher.getApSupplier().getSplCode());
                    details.setTiSlTin(apVoucher.getApSupplier().getSplTin());
                    details.setTiSlSubledgerCode(apVoucher.getApSupplier().getSplSupplierCode());
                    details.setTiSlName(apVoucher.getApSupplier().getSplName());
                    details.setTiSlAddress(apVoucher.getApSupplier().getSplAddress());
                    details.setTiSlAddress2(apVoucher.getApSupplier().getSplCity() + " " + apVoucher.getApSupplier().getSplStateProvince());

                    details.setTiIsArDocument(EJBCommon.FALSE);
                    details.setTiAdBranch(AD_BRNCH);
                    details.setTiAdCompany(AD_CMPNY);

                    this.createTaxInterface(details);
                }

                // Create tax interface for Officer and Employees voucher per line items

                Collection apVoucherOAEs = apVoucherHome.findPostedTaxVouByVouOaeDateRange(EJBCommon.FALSE, DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);

                for (Object apVoucherOAE : apVoucherOAEs) {

                    LocalApVoucher apVoucher = (LocalApVoucher) apVoucherOAE;

                    removeExisting("AP VOUCHER", apVoucher.getVouCode(), AD_CMPNY);

                    if (apVoucher.getApVoucherLineItems().size() > 0) {

                        Collection apVlis = apVoucher.getApVoucherLineItems();

                        for (Object apVli : apVlis) {

                            LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) apVli;

                            // create tax interface

                            GlTaxInterfaceDetails details = new GlTaxInterfaceDetails();

                            if (apVoucherLineItem.getVliTax() == EJBCommon.FALSE) continue;

                            double SALES_AMOUNT = EJBCommon.roundIt(apVoucherLineItem.getVliAmount() + apVoucherLineItem.getVliTaxAmount(), PRECISION_UNIT);
                            double NET_AMNT = EJBCommon.roundIt(SALES_AMOUNT / ((apVoucher.getApTaxCode().getTcRate() / 100) + 1), PRECISION_UNIT);
                            double TAX_AMNT = EJBCommon.roundIt(SALES_AMOUNT - NET_AMNT, PRECISION_UNIT);

                            details.setTiDocumentType("AP VOUCHER");
                            details.setTiSource("AP");
                            details.setTiNetAmount(NET_AMNT);
                            details.setTiTaxAmount(TAX_AMNT);
                            details.setTiSalesAmount(SALES_AMOUNT);

                            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCode(apVoucherLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(), AD_CMPNY);

                            double SERVICES = glChartOfAccount.getCoaTaxType().equals("SERVICES") ? TAX_AMNT : 0d;
                            double CAPITAL_GOODS = glChartOfAccount.getCoaTaxType().equals("CAPITAL GOODS") ? TAX_AMNT : 0d;
                            double OTHER_CAPITAL_GOODS = glChartOfAccount.getCoaTaxType().equals("OTHER CAPITAL GOODS") ? TAX_AMNT : 0d;

                            details.setTiServicesAmount(SERVICES);
                            details.setTiCapitalGoodsAmount(CAPITAL_GOODS);
                            details.setTiOtherCapitalGoodsAmount(OTHER_CAPITAL_GOODS);

                            details.setTiTxnCode(apVoucher.getVouCode());
                            details.setTiTxnDate(apVoucher.getVouDate());
                            details.setTiTxnDocumentNumber(apVoucher.getVouDocumentNumber());
                            details.setTiTxnReferenceNumber(apVoucher.getVouReferenceNumber());
                            details.setTiTaxExempt(EJBCommon.roundIt(apVoucher.getApTaxCode().getTcName().equals("EXEMPT") ? SALES_AMOUNT : 0d, PRECISION_UNIT));
                            details.setTiTaxZeroRated(EJBCommon.roundIt(apVoucher.getApTaxCode().getTcName().equals("ZERO-RATED") ? SALES_AMOUNT : 0d, PRECISION_UNIT));
                            details.setTiTxlCode(0);
                            details.setTiTxlCoaCode(apVoucher.getApTaxCode().getGlChartOfAccount() != null ? apVoucher.getApTaxCode().getGlChartOfAccount().getCoaCode() : null);
                            details.setTiTcCode(apVoucher.getApTaxCode().getTcCode());
                            details.setTiWtcCode(null);

                            details.setTiSlTin(apVoucher.getApSupplier().getSplTin());
                            details.setTiSlSubledgerCode(apVoucher.getApSupplier().getSplSupplierCode());

                            details.setTiSlCode(null);
                            details.setTiSlSubledgerCode(apVoucherLineItem.getVliSplName());
                            details.setTiSlName(apVoucherLineItem.getVliSplName());
                            details.setTiSlTin(apVoucherLineItem.getVliSplTin());
                            details.setTiSlAddress2(apVoucherLineItem.getVliSplAddress());

                            details.setTiIsArDocument(EJBCommon.FALSE);
                            details.setTiAdBranch(AD_BRNCH);
                            details.setTiAdCompany(AD_CMPNY);

                            this.createTaxInterface(details);
                        }
                    }
                }
            }

            if (DOC_TYP.equals("AP DEBIT MEMO") || DOC_TYP.equals("")) {
                Debug.print("AP DEBIT MEMO");

                Collection apDebitMemos = apVoucherHome.findPostedTaxVouByVouDateRange(EJBCommon.TRUE, DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);

                for (Object apDebitMemo : apDebitMemos) {

                    LocalApVoucher apVoucher = (LocalApVoucher) apDebitMemo;

                    removeExisting("AP DEBIT MEMO", apVoucher.getVouCode(), AD_CMPNY);

                    // create tax interface

                    GlTaxInterfaceDetails details = new GlTaxInterfaceDetails();

                    Collection apDrs = apVoucher.getApDistributionRecords();

                    double SALES_AMOUNT = 0d;
                    double SERVICES = 0d;
                    double CAPITAL_GOODS = 0d;
                    double OTHER_CAPITAL_GOODS = 0d;

                    for (Object apDr : apDrs) {

                        LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) apDr;
                        if (apDistributionRecord.getDrClass().equals("PAYABLE")) {
                            SALES_AMOUNT += apDistributionRecord.getDrAmount();
                        }

                        if (apDistributionRecord.getGlChartOfAccount().getCoaTaxType().equals("SERVICES")) {
                            SERVICES = apDistributionRecord.getDrAmount();
                        }

                        if (apDistributionRecord.getGlChartOfAccount().getCoaTaxType().equals("CAPITAL GOODS")) {
                            CAPITAL_GOODS = apDistributionRecord.getDrAmount();
                        }

                        if (apDistributionRecord.getGlChartOfAccount().getCoaTaxType().equals("OTHER CAPITAL GOODS")) {
                            OTHER_CAPITAL_GOODS = apDistributionRecord.getDrAmount();
                        }
                    }

                    Debug.print("SALES_AMOUNT=" + SALES_AMOUNT);
                    double NET_AMNT = EJBCommon.roundIt(SALES_AMOUNT / ((apVoucher.getApTaxCode().getTcRate() / 100) + 1), PRECISION_UNIT);
                    details.setTiDocumentType("AP DEBIT MEMO");
                    details.setTiSource("AP");
                    details.setTiNetAmount(NET_AMNT);
                    details.setTiTaxAmount(EJBCommon.roundIt(SALES_AMOUNT - NET_AMNT, PRECISION_UNIT));
                    details.setTiSalesAmount(EJBCommon.roundIt(SALES_AMOUNT, PRECISION_UNIT));
                    details.setTiServicesAmount(EJBCommon.roundIt(SERVICES * ((apVoucher.getApTaxCode().getTcRate() / 100) + 1), PRECISION_UNIT));
                    details.setTiCapitalGoodsAmount(EJBCommon.roundIt(CAPITAL_GOODS * ((apVoucher.getApTaxCode().getTcRate() / 100) + 1), PRECISION_UNIT));
                    details.setTiOtherCapitalGoodsAmount(EJBCommon.roundIt(OTHER_CAPITAL_GOODS * ((apVoucher.getApTaxCode().getTcRate() / 100) + 1), PRECISION_UNIT));
                    details.setTiTxnCode(apVoucher.getVouCode());
                    details.setTiTxnDate(apVoucher.getVouDate());
                    details.setTiTxnDocumentNumber(apVoucher.getVouDocumentNumber());
                    details.setTiTxnReferenceNumber(apVoucher.getVouReferenceNumber());
                    details.setTiTaxExempt(EJBCommon.roundIt(apVoucher.getApTaxCode().getTcName().equals("EXEMPT") ? SALES_AMOUNT : 0d, PRECISION_UNIT));
                    details.setTiTaxZeroRated(EJBCommon.roundIt(apVoucher.getApTaxCode().getTcName().equals("ZERO-RATED") ? SALES_AMOUNT : 0d, PRECISION_UNIT));
                    details.setTiTxlCode(0);
                    details.setTiTxlCoaCode(apVoucher.getApTaxCode().getGlChartOfAccount() != null ? apVoucher.getApTaxCode().getGlChartOfAccount().getCoaCode() : null);
                    details.setTiTcCode(apVoucher.getApTaxCode().getTcCode());
                    details.setTiWtcCode(null);
                    details.setTiSlCode(apVoucher.getApSupplier().getSplCode());
                    details.setTiSlTin(apVoucher.getApSupplier().getSplTin());
                    details.setTiSlSubledgerCode(apVoucher.getApSupplier().getSplSupplierCode());
                    details.setTiSlName(apVoucher.getApSupplier().getSplName());
                    details.setTiSlAddress(apVoucher.getApSupplier().getSplAddress());
                    details.setTiSlAddress2(apVoucher.getApSupplier().getSplCity() + " " + apVoucher.getApSupplier().getSplStateProvince());

                    details.setTiIsArDocument(EJBCommon.FALSE);
                    details.setTiAdBranch(AD_BRNCH);
                    details.setTiAdCompany(AD_CMPNY);

                    this.createTaxInterface(details);
                }
            }

            if (DOC_TYP.equals("AP CHECK") || DOC_TYP.equals("")) {
                Debug.print("AP CHECK");

                Collection apChecks = apCheckHome.findPostedTaxDirectChkByChkDateRange(DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);

                for (Object check : apChecks) {

                    LocalApCheck apCheck = (LocalApCheck) check;

                    removeExisting("AP CHECK", apCheck.getChkCode(), AD_CMPNY);

                    // create tax interface

                    GlTaxInterfaceDetails details = new GlTaxInterfaceDetails();

                    Collection apDrs = apCheck.getApDistributionRecords();

                    double SALES_AMOUNT = 0d;
                    double SERVICES = 0d;
                    double CAPITAL_GOODS = 0d;
                    double OTHER_CAPITAL_GOODS = 0d;

                    for (Object apDr : apDrs) {

                        LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) apDr;

                        if (apDistributionRecord.getDrClass().equals("CASH")) {
                            SALES_AMOUNT += apDistributionRecord.getDrAmount();
                        }

                        if (apDistributionRecord.getGlChartOfAccount().getCoaTaxType().equals("SERVICES")) {
                            SERVICES += apDistributionRecord.getDrAmount();
                        }

                        if (apDistributionRecord.getGlChartOfAccount().getCoaTaxType().equals("CAPITAL GOODS")) {
                            CAPITAL_GOODS += apDistributionRecord.getDrAmount();
                        }

                        if (apDistributionRecord.getGlChartOfAccount().getCoaTaxType().equals("OTHER CAPITAL GOODS")) {
                            OTHER_CAPITAL_GOODS += apDistributionRecord.getDrAmount();
                        }
                    }

                    Debug.print("SALES_AMOUNT=" + SALES_AMOUNT);
                    Debug.print("SERVICES=" + SERVICES);
                    Debug.print("CAPITAL_GOODS=" + CAPITAL_GOODS);
                    Debug.print("OTHER_CAPITAL_GOODS=" + OTHER_CAPITAL_GOODS);

                    double NET_AMNT = EJBCommon.roundIt(SALES_AMOUNT / ((apCheck.getApTaxCode().getTcRate() / 100) + 1), PRECISION_UNIT);
                    details.setTiDocumentType("AP CHECK");
                    details.setTiSource("AP");
                    details.setTiNetAmount(NET_AMNT);
                    details.setTiTaxAmount(EJBCommon.roundIt(SALES_AMOUNT - NET_AMNT, PRECISION_UNIT));
                    details.setTiSalesAmount(EJBCommon.roundIt(SALES_AMOUNT, PRECISION_UNIT));
                    details.setTiServicesAmount(EJBCommon.roundIt(SERVICES * ((apCheck.getApTaxCode().getTcRate() / 100) + 1), PRECISION_UNIT));
                    details.setTiCapitalGoodsAmount(EJBCommon.roundIt(CAPITAL_GOODS * ((apCheck.getApTaxCode().getTcRate() / 100) + 1), PRECISION_UNIT));
                    details.setTiOtherCapitalGoodsAmount(EJBCommon.roundIt(OTHER_CAPITAL_GOODS * ((apCheck.getApTaxCode().getTcRate() / 100) + 1), PRECISION_UNIT));
                    details.setTiTxnCode(apCheck.getChkCode());
                    details.setTiTxnDate(apCheck.getChkDate());
                    details.setTiTxnDocumentNumber(apCheck.getChkDocumentNumber());
                    details.setTiTxnReferenceNumber(apCheck.getChkReferenceNumber());
                    details.setTiTaxExempt(EJBCommon.roundIt(apCheck.getApTaxCode().getTcName().equals("EXEMPT") ? SALES_AMOUNT : 0d, PRECISION_UNIT));
                    details.setTiTaxZeroRated(EJBCommon.roundIt(apCheck.getApTaxCode().getTcName().equals("ZERO-RATED") ? SALES_AMOUNT : 0d, PRECISION_UNIT));
                    details.setTiTxlCode(0);
                    details.setTiTxlCoaCode(apCheck.getApTaxCode().getGlChartOfAccount() != null ? apCheck.getApTaxCode().getGlChartOfAccount().getCoaCode() : null);
                    details.setTiTcCode(apCheck.getApTaxCode().getTcCode());
                    details.setTiWtcCode(null);
                    details.setTiSlCode(apCheck.getApSupplier().getSplCode());
                    details.setTiSlTin(apCheck.getApSupplier().getSplTin());
                    details.setTiSlSubledgerCode(apCheck.getApSupplier().getSplSupplierCode());
                    details.setTiSlName(apCheck.getApSupplier().getSplName());
                    details.setTiSlAddress(apCheck.getApSupplier().getSplAddress());
                    details.setTiSlAddress2(apCheck.getApSupplier().getSplCity() + " " + apCheck.getApSupplier().getSplStateProvince());

                    details.setTiIsArDocument(EJBCommon.FALSE);
                    details.setTiAdBranch(AD_BRNCH);
                    details.setTiAdCompany(AD_CMPNY);

                    this.createTaxInterface(details);
                }
            }

            if (DOC_TYP.equals("AP RECEIVING ITEM") || DOC_TYP.equals("")) {
                Debug.print("AP RECEIVING ITEM");
                Collection apReceivings = apReceivingItemHome.findPostedTaxPoByVouDateRange(DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);

                for (Object apReceiving : apReceivings) {

                    LocalApPurchaseOrder apReceivingItem = (LocalApPurchaseOrder) apReceiving;

                    removeExisting("AP RECEIVING ITEM", apReceivingItem.getPoCode(), AD_CMPNY);

                    // create tax interface

                    GlTaxInterfaceDetails details = new GlTaxInterfaceDetails();

                    Collection apDrs = apReceivingItem.getApDistributionRecords();

                    double SALES_AMOUNT = 0d;
                    double SERVICES = 0d;
                    double CAPITAL_GOODS = 0d;
                    double OTHER_CAPITAL_GOODS = 0d;

                    for (Object apDr : apDrs) {

                        LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) apDr;
                        Debug.print("COA=" + apDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                        Debug.print("apDistributionRecord.getDrClass()=" + apDistributionRecord.getDrClass());
                        if (apDistributionRecord.getDrClass().equals("ACC INV")) {
                            SALES_AMOUNT += apDistributionRecord.getDrAmount();
                        }

                        if (apDistributionRecord.getDrClass().equals("INVENTORY") && apDistributionRecord.getGlChartOfAccount().getCoaTaxType().equals("SERVICES")) {
                            SERVICES += apDistributionRecord.getDrAmount();
                        }

                        if (apDistributionRecord.getDrClass().equals("INVENTORY") && apDistributionRecord.getGlChartOfAccount().getCoaTaxType().equals("CAPITAL GOODS")) {
                            CAPITAL_GOODS += apDistributionRecord.getDrAmount();
                        }

                        if (apDistributionRecord.getDrClass().equals("INVENTORY") && apDistributionRecord.getGlChartOfAccount().getCoaTaxType().equals("OTHER CAPITAL GOODS")) {
                            OTHER_CAPITAL_GOODS += apDistributionRecord.getDrAmount();
                        }
                    }

                    Debug.print("SALES_AMOUNT=" + SALES_AMOUNT);
                    double NET_AMNT = EJBCommon.roundIt(SALES_AMOUNT / ((apReceivingItem.getApTaxCode().getTcRate() / 100) + 1), PRECISION_UNIT);
                    details.setTiDocumentType("AP RECEIVING ITEM");
                    details.setTiSource("AP");
                    details.setTiNetAmount(NET_AMNT);
                    details.setTiTaxAmount(EJBCommon.roundIt(SALES_AMOUNT - NET_AMNT, PRECISION_UNIT));
                    details.setTiSalesAmount(EJBCommon.roundIt(SALES_AMOUNT, PRECISION_UNIT));
                    details.setTiServicesAmount(EJBCommon.roundIt(SERVICES * ((apReceivingItem.getApTaxCode().getTcRate() / 100) + 1), PRECISION_UNIT));
                    details.setTiCapitalGoodsAmount(EJBCommon.roundIt(CAPITAL_GOODS * ((apReceivingItem.getApTaxCode().getTcRate() / 100) + 1), PRECISION_UNIT));
                    details.setTiOtherCapitalGoodsAmount(EJBCommon.roundIt(OTHER_CAPITAL_GOODS * ((apReceivingItem.getApTaxCode().getTcRate() / 100) + 1), PRECISION_UNIT));
                    details.setTiTxnCode(apReceivingItem.getPoCode());
                    details.setTiTxnDate(apReceivingItem.getPoDate());
                    details.setTiTxnDocumentNumber(apReceivingItem.getPoDocumentNumber());
                    details.setTiTxnReferenceNumber(apReceivingItem.getPoReferenceNumber());
                    details.setTiTaxExempt(EJBCommon.roundIt(apReceivingItem.getApTaxCode().getTcName().equals("EXEMPT") ? SALES_AMOUNT : 0d, PRECISION_UNIT));
                    details.setTiTaxZeroRated(EJBCommon.roundIt(apReceivingItem.getApTaxCode().getTcName().equals("ZERO-RATED") ? SALES_AMOUNT : 0d, PRECISION_UNIT));
                    details.setTiTxlCode(0);
                    details.setTiTxlCoaCode(apReceivingItem.getApTaxCode().getGlChartOfAccount() != null ? apReceivingItem.getApTaxCode().getGlChartOfAccount().getCoaCode() : null);
                    details.setTiTcCode(apReceivingItem.getApTaxCode().getTcCode());
                    details.setTiWtcCode(null);
                    details.setTiSlCode(apReceivingItem.getApSupplier().getSplCode());
                    details.setTiSlTin(apReceivingItem.getApSupplier().getSplTin());
                    details.setTiSlSubledgerCode(apReceivingItem.getApSupplier().getSplSupplierCode());
                    details.setTiSlName(apReceivingItem.getApSupplier().getSplName());
                    details.setTiSlAddress(apReceivingItem.getApSupplier().getSplAddress());
                    details.setTiSlAddress2(apReceivingItem.getApSupplier().getSplCity() + " " + apReceivingItem.getApSupplier().getSplStateProvince());

                    details.setTiIsArDocument(EJBCommon.FALSE);
                    details.setTiAdBranch(AD_BRNCH);
                    details.setTiAdCompany(AD_CMPNY);

                    this.createTaxInterface(details);
                }
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void runApWithholdingTaxCode(String DOC_TYP, Date DT_FRM, Date DT_TO, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("GlTaxInterfaceRunControllerBean apWithholdingTaxCodeInteraface");

        // ap withholding tax
        short PRECISION_UNIT = this.getGlFcPrecisionUnit(AD_CMPNY);

        try {

            Collection apWithholdingTaxCodes = apWithholdingTaxCodeHome.findEnabledWtcAll(AD_CMPNY);

            for (Object withholdingTaxCode : apWithholdingTaxCodes) {

                LocalApWithholdingTaxCode apWithholdingTaxCode = (LocalApWithholdingTaxCode) withholdingTaxCode;
                LocalGlChartOfAccount glChartOfAccount = apWithholdingTaxCode.getGlChartOfAccount();

                if (glChartOfAccount != null) {

                    // AP VOUCHER

                    if (DOC_TYP.equals("AP VOUCHER") || DOC_TYP.equals("")) {

                        Collection apDistributionRecords = apDistributionRecordHome.findVouByDateRangeAndCoaAccountNumber(EJBCommon.FALSE, DT_FRM, DT_TO, glChartOfAccount.getCoaCode(), AD_CMPNY);

                        for (Object distributionRecord : apDistributionRecords) {

                            LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) distributionRecord;

                            Integer voucherWtcCode = null;
                            if (apDistributionRecord.getApVoucher().getApWithholdingTaxCode() != null) {

                                voucherWtcCode = apDistributionRecord.getApVoucher().getApWithholdingTaxCode().getWtcCode();
                            }

                            if ((voucherWtcCode != null && voucherWtcCode.equals(apWithholdingTaxCode.getWtcCode()))) {
                                removeExisting("AP VOUCHER", apDistributionRecord.getDrCode(), AD_CMPNY);
                                double DR_AMNT = this.convertForeignToFunctionalCurrency(apDistributionRecord.getApVoucher().getGlFunctionalCurrency().getFcCode(), apDistributionRecord.getApVoucher().getGlFunctionalCurrency().getFcName(), apDistributionRecord.getApVoucher().getVouConversionDate(), apDistributionRecord.getApVoucher().getVouConversionRate(), apDistributionRecord.getDrAmount(), AD_CMPNY);

                                if (apWithholdingTaxCode.getWtcRate() == 0) continue;

                                double NET_AMNT = DR_AMNT / (apWithholdingTaxCode.getWtcRate() / 100);

                                GlTaxInterfaceDetails details = new GlTaxInterfaceDetails();
                                details.setTiDocumentType("AP VOUCHER");
                                details.setTiSource("AP");
                                details.setTiNetAmount(NET_AMNT);
                                details.setTiSalesAmount(EJBCommon.roundIt(NET_AMNT + DR_AMNT, PRECISION_UNIT));
                                details.setTiTxnCode(apDistributionRecord.getApVoucher().getVouCode());
                                details.setTiTxnDate(apDistributionRecord.getApVoucher().getVouDate());
                                details.setTiTxnDocumentNumber(apDistributionRecord.getApVoucher().getVouDocumentNumber());
                                details.setTiTxnReferenceNumber(apDistributionRecord.getApVoucher().getVouReferenceNumber());
                                details.setTiTxlCode(apDistributionRecord.getDrCode());
                                details.setTiTxlCoaCode(apDistributionRecord.getGlChartOfAccount().getCoaCode());
                                details.setTiTcCode(null);
                                details.setTiWtcCode(voucherWtcCode);
                                details.setTiSlCode(apDistributionRecord.getApVoucher().getApSupplier().getSplCode());
                                details.setTiSlTin(apDistributionRecord.getApVoucher().getApSupplier().getSplTin());
                                details.setTiSlSubledgerCode(apDistributionRecord.getApVoucher().getApSupplier().getSplSupplierCode());
                                details.setTiSlName(apDistributionRecord.getApVoucher().getApSupplier().getSplName());
                                details.setTiSlAddress(apDistributionRecord.getApVoucher().getApSupplier().getSplAddress());
                                details.setTiIsArDocument(EJBCommon.FALSE);
                                details.setTiAdBranch(AD_BRNCH);
                                details.setTiAdCompany(AD_CMPNY);

                                this.createTaxInterface(details);
                            }
                        }
                    }

                    // AP DEBIT MEMO

                    if (DOC_TYP.equals("AP DEBIT MEMO") || DOC_TYP.equals("")) {

                        Collection apDistributionRecords = apDistributionRecordHome.findVouByDateRangeAndCoaAccountNumber(EJBCommon.TRUE, DT_FRM, DT_TO, glChartOfAccount.getCoaCode(), AD_CMPNY);

                        for (Object distributionRecord : apDistributionRecords) {

                            LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) distributionRecord;
                            LocalApVoucher apVoucher = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(apDistributionRecord.getApVoucher().getVouDmVoucherNumber(), EJBCommon.FALSE, AD_BRNCH, AD_CMPNY);

                            Integer debitmemoWtcCode = null;
                            if (apVoucher.getApWithholdingTaxCode() != null) {

                                debitmemoWtcCode = apVoucher.getApWithholdingTaxCode().getWtcCode();
                            }

                            if ((debitmemoWtcCode != null && debitmemoWtcCode.equals(apWithholdingTaxCode.getWtcCode()))) {
                                removeExisting("AP DEBIT MEMO", apDistributionRecord.getDrCode(), AD_CMPNY);
                                double DR_AMNT = this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apDistributionRecord.getDrAmount(), AD_CMPNY);

                                if (apWithholdingTaxCode.getWtcRate() == 0) continue;

                                double NET_AMNT = DR_AMNT / (apWithholdingTaxCode.getWtcRate() / 100);

                                GlTaxInterfaceDetails details = new GlTaxInterfaceDetails();
                                details.setTiDocumentType("AP DEBIT MEMO");
                                details.setTiSource("AP");
                                details.setTiNetAmount(NET_AMNT);
                                details.setTiSalesAmount(EJBCommon.roundIt(NET_AMNT + DR_AMNT, PRECISION_UNIT));
                                details.setTiTxnCode(apDistributionRecord.getApVoucher().getVouCode());
                                details.setTiTxnDate(apDistributionRecord.getApVoucher().getVouDate());
                                details.setTiTxnDocumentNumber(apDistributionRecord.getApVoucher().getVouDocumentNumber());
                                details.setTiTxnReferenceNumber(apDistributionRecord.getApVoucher().getVouDmVoucherNumber());
                                details.setTiTxlCode(apDistributionRecord.getDrCode());
                                details.setTiTxlCoaCode(apDistributionRecord.getGlChartOfAccount().getCoaCode());
                                details.setTiTcCode(null);
                                details.setTiWtcCode(debitmemoWtcCode);
                                details.setTiSlCode(apDistributionRecord.getApVoucher().getApSupplier().getSplCode());
                                details.setTiSlTin(apDistributionRecord.getApVoucher().getApSupplier().getSplTin());
                                details.setTiSlSubledgerCode(apDistributionRecord.getApVoucher().getApSupplier().getSplSupplierCode());
                                details.setTiSlName(apDistributionRecord.getApVoucher().getApSupplier().getSplName());
                                details.setTiSlAddress(apDistributionRecord.getApVoucher().getApSupplier().getSplAddress());
                                details.setTiIsArDocument(EJBCommon.FALSE);
                                details.setTiAdBranch(AD_BRNCH);
                                details.setTiAdCompany(AD_CMPNY);

                                this.createTaxInterface(details);
                            }
                        }
                    }

                    // AP CHECK

                    if (DOC_TYP.equals("AP CHECK") || DOC_TYP.equals("")) {

                        Collection apDistributionRecords = apDistributionRecordHome.findChkByDateRangeAndCoaAccountNumber(DT_FRM, DT_TO, glChartOfAccount.getCoaCode(), AD_CMPNY);

                        for (Object distributionRecord : apDistributionRecords) {

                            LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) distributionRecord;

                            Integer checkWtcCode = null;
                            if (apDistributionRecord.getApCheck().getApWithholdingTaxCode() != null) {

                                checkWtcCode = apDistributionRecord.getApCheck().getApWithholdingTaxCode().getWtcCode();
                            }

                            Integer appliedWtcCode = null;
                            if (apDistributionRecord.getApAppliedVoucher() != null) {

                                appliedWtcCode = apDistributionRecord.getApAppliedVoucher().getApVoucherPaymentSchedule().getApVoucher().getApWithholdingTaxCode().getWtcCode();
                            }

                            if (((checkWtcCode != null && checkWtcCode.equals(apWithholdingTaxCode.getWtcCode()))) || (appliedWtcCode != null && appliedWtcCode.equals(apWithholdingTaxCode.getWtcCode()))) {
                                removeExisting("AP CHECK", apDistributionRecord.getDrCode(), AD_CMPNY);
                                LocalApAppliedVoucher apAppliedVoucher = apDistributionRecord.getApAppliedVoucher();
                                LocalApVoucher apVoucher = null;

                                double DR_AMNT = 0d;
                                if (apAppliedVoucher == null) {

                                    DR_AMNT = this.convertForeignToFunctionalCurrency(apDistributionRecord.getApCheck().getGlFunctionalCurrency().getFcCode(), apDistributionRecord.getApCheck().getGlFunctionalCurrency().getFcName(), apDistributionRecord.getApCheck().getChkConversionDate(), apDistributionRecord.getApCheck().getChkConversionRate(), apDistributionRecord.getDrAmount(), AD_CMPNY);

                                } else {

                                    apVoucher = apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher();

                                    DR_AMNT = this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apDistributionRecord.getDrAmount(), AD_CMPNY);
                                }

                                if (apWithholdingTaxCode.getWtcRate() == 0) continue;

                                double NET_AMNT = DR_AMNT / (apWithholdingTaxCode.getWtcRate() / 100);

                                GlTaxInterfaceDetails details = new GlTaxInterfaceDetails();
                                details.setTiDocumentType("AP CHECK");
                                details.setTiSource("AP");
                                details.setTiNetAmount(NET_AMNT);
                                details.setTiSalesAmount(EJBCommon.roundIt(NET_AMNT + DR_AMNT, PRECISION_UNIT));
                                details.setTiTxnCode(apDistributionRecord.getApCheck().getChkCode());
                                details.setTiTxnDate(apDistributionRecord.getApCheck().getChkDate());
                                details.setTiTxnDocumentNumber(apDistributionRecord.getApCheck().getChkDocumentNumber());
                                details.setTiTxnReferenceNumber(apDistributionRecord.getApCheck().getChkReferenceNumber());
                                details.setTiTxlCode(apDistributionRecord.getDrCode());
                                details.setTiTxlCoaCode(apDistributionRecord.getGlChartOfAccount().getCoaCode());
                                details.setTiTcCode(null);
                                if (apVoucher == null) {

                                    details.setTiWtcCode(checkWtcCode);

                                } else {

                                    details.setTiWtcCode(appliedWtcCode);
                                }
                                details.setTiSlCode(apDistributionRecord.getApCheck().getApSupplier().getSplCode());
                                details.setTiSlTin(apDistributionRecord.getApCheck().getApSupplier().getSplTin());
                                details.setTiSlSubledgerCode(apDistributionRecord.getApCheck().getApSupplier().getSplSupplierCode());
                                details.setTiSlName(apDistributionRecord.getApCheck().getApSupplier().getSplName());
                                details.setTiSlAddress(apDistributionRecord.getApCheck().getApSupplier().getSplAddress());
                                details.setTiIsArDocument(EJBCommon.FALSE);
                                details.setTiAdBranch(AD_BRNCH);
                                details.setTiAdCompany(AD_CMPNY);

                                this.createTaxInterface(details);
                            }
                        }
                    }

                    // GL JOURNAL

                    Collection glJournalLines = glJournalLineHome.findManualJrByEffectiveDateRangeAndCoaCode(DT_FRM, DT_TO, glChartOfAccount.getCoaCode(), AD_CMPNY);

                    for (Object journalLine : glJournalLines) {

                        LocalGlJournalLine glJournalLine = (LocalGlJournalLine) journalLine;

                        removeExisting("GL JOURNAL", glJournalLine.getJlCode(), AD_CMPNY);

                        // if(!existing) {

                        GlTaxInterfaceDetails details = new GlTaxInterfaceDetails();
                        details.setTiDocumentType("GL JOURNAL");
                        details.setTiSource("GL");
                        details.setTiNetAmount(0d);
                        details.setTiTxnCode(glJournalLine.getGlJournal().getJrCode());
                        details.setTiTxnDate(glJournalLine.getGlJournal().getJrEffectiveDate());
                        details.setTiTxnDocumentNumber(glJournalLine.getGlJournal().getJrDocumentNumber());
                        details.setTiTxnReferenceNumber(null);
                        details.setTiTxlCode(glJournalLine.getJlCode());
                        details.setTiTxlCoaCode(glJournalLine.getGlChartOfAccount().getCoaCode());
                        details.setTiTcCode(null);
                        details.setTiWtcCode(null);
                        details.setTiSlCode(null);
                        details.setTiSlTin(null);
                        details.setTiSlSubledgerCode(null);
                        details.setTiSlName(null);
                        details.setTiSlAddress(null);
                        details.setTiIsArDocument(EJBCommon.FALSE);
                        details.setTiAdBranch(AD_BRNCH);
                        details.setTiAdCompany(AD_CMPNY);

                        this.createTaxInterface(details);

                        // }

                    }
                }
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private boolean checkExistence(String DOC_TYP, Integer TXL_CODE, Integer AD_CMPNY) {

        Debug.print("GlTaxInterfaceRunControllerBean checkExistence");

        boolean existing = true;

        try {

            glTaxInterfaceHome.findByTiDocumentTypeAndTxlCode(DOC_TYP, TXL_CODE, AD_CMPNY);

        } catch (Exception e) {

            existing = false;
        }

        return existing;
    }

    private void removeExisting(String DOC_TYP, Integer TXN_CODE, Integer AD_CMPNY) {

        Debug.print("GlTaxInterfaceRunControllerBean removeExisting");

        try {

            Collection glTaxInterfaces = glTaxInterfaceHome.findAllByTiDocumentTypeAndTxnCode(DOC_TYP, TXN_CODE, AD_CMPNY);

            for (Object taxInterface : glTaxInterfaces) {
                LocalGlTaxInterface glTaxInterface = (LocalGlTaxInterface) taxInterface;
                //    			glTaxInterface.entityRemove();
                em.remove(glTaxInterface);
            }

        } catch (Exception e) {

        }
    }

    private void createTaxInterface(GlTaxInterfaceDetails details) {

        Debug.print("GlTaxInterfaceRunControllerBean createTaxInteraface");

        try {

            Debug.print("txlcode is: " + details.getTiTxlCode());
            LocalGlTaxInterface glTaxInterface = glTaxInterfaceHome.create(details.getTiDocumentType(), details.getTiSource(), details.getTiNetAmount(), details.getTiTaxAmount(), details.getTiSalesAmount(), details.getTiServicesAmount(), details.getTiCapitalGoodsAmount(), details.getTiOtherCapitalGoodsAmount(), details.getTiTxnCode(), details.getTiTxnDate(), details.getTiTxnDocumentNumber(), details.getTiTxnReferenceNumber(), details.getTiTaxExempt(), details.getTiTaxZeroRated(), details.getTiTxlCode(), details.getTiTxlCoaCode(), details.getTiTcCode(), details.getTiWtcCode(), details.getTiSlCode(), details.getTiSlTin(), details.getTiSlSubledgerCode(), details.getTiSlName(), details.getTiSlAddress(), details.getTiSlAddress2(), details.getTiIsArDocument(), details.getTiAdBranch(), details.getTiAdCompany());

            Debug.print("Create at Code: " + glTaxInterface.getTiCode());

        } catch (Exception ex) {
            Debug.print("Something wnet wrong 2.");
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("GlTaxInterfaceRunControllerBean convertForeignToFunctionalCurrency");

        LocalAdCompany adCompany = null;


        // get company and extended precision

        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // Convert to functional currency if necessary

        if (CONVERSION_RATE != 1 && CONVERSION_RATE != 0) {

            AMOUNT = AMOUNT * CONVERSION_RATE;

        } else if (CONVERSION_DATE != null) {

            try {

                // Get functional currency rate

                LocalGlFunctionalCurrencyRate glReceiptFunctionalCurrencyRate = null;

                if (!FC_NM.equals("USD")) {

                    glReceiptFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(FC_CODE, CONVERSION_DATE, AD_CMPNY);

                    AMOUNT = AMOUNT * glReceiptFunctionalCurrencyRate.getFrXToUsd();
                }

                // Get set of book functional currency rate if necessary

                if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                    LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), CONVERSION_DATE, AD_CMPNY);

                    AMOUNT = AMOUNT / glCompanyFunctionalCurrencyRate.getFrXToUsd();
                }

            } catch (Exception ex) {

                throw new EJBException(ex.getMessage());
            }
        }

        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlCoaGeneratorControllerBean ejbCreate");
    }
}