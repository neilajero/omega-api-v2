package com.ejb.txnsync.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ar.*;
import com.ejb.dao.gl.*;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ar.*;
import com.ejb.entities.gl.*;
import com.ejb.entities.inv.*;
import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.ejb.restfulapi.sync.ar.models.ArMiscReceiptSyncRequest;
import com.ejb.restfulapi.sync.ar.models.ArMiscReceiptSyncResponse;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBCommonAPIErrCodes;
import com.util.EJBContextClass;
import com.util.mod.ar.ArModInvoiceLineItemDetails;
import com.util.mod.ar.ArModReceiptDetails;
import com.util.mod.inv.InvModTagListDetails;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import java.text.SimpleDateFormat;
import java.util.*;

@Stateless(name = "ArMiscReceiptSyncControllerBeanEJB")
public class ArMiscReceiptSyncControllerBean extends EJBContextClass implements ArMiscReceiptSyncController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalArTaxCodeHome arTaxCodeHome;
    @EJB
    private LocalArWithholdingTaxCodeHome arWithholdingTaxCodeHome;
    @EJB
    private LocalArReceiptBatchHome arReceiptBatchHome;
    @EJB
    private LocalArInvoiceLineItemHome arInvoiceLineItemHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalAdPaymentTermHome adPaymentTermHome;
    @EJB
    private LocalArInvoiceBatchHome arInvoiceBatchHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private LocalAdBranchCustomerHome adBranchCustomerHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalArInvoicePaymentScheduleHome arInvoicePaymentScheduleHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalInvTagHome invTagHome;
    @EJB
    private LocalArSalespersonHome arSalespersonHome;
    @EJB
    private LocalAdBranchBankAccountHome adBranchBankAccountHome;
    @EJB
    private LocalAdBranchArTaxCodeHome adBranchArTaxCodeHome;
    @EJB
    private LocalAdBankAccountBalanceHome adBankAccountBalanceHome;
    @EJB
    private ILocalGlAccountingCalendarValueHome glAccountingCalendarValueHome;
    @EJB
    private ILocalGlSetOfBookHome glSetOfBookHome;
    @EJB
    private LocalGlJournalLineHome glJournalLineHome;
    @EJB
    private LocalGlJournalBatchHome glJournalBatchHome;
    @EJB
    private LocalGlJournalSourceHome glJournalSourceHome;
    @EJB
    private LocalGlJournalCategoryHome glJournalCategoryHome;
    @EJB
    private LocalGlSuspenseAccountHome glSuspenseAccountHome;
    @EJB
    private LocalGlForexLedgerHome glForexLedgerHome;
    @EJB
    private LocalGlJournalHome glJournalHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalInvAdjustmentLineHome invAdjustmentLineHome;
    @EJB
    private ILocalGlChartOfAccountBalanceHome glChartOfAccountBalanceHome;
    @EJB
    private LocalInvDistributionRecordHome invDistributionRecordHome;
    @EJB
    private LocalInvAdjustmentHome invAdjustmentHome;

    public static int checkExpiryDates(String misc) throws Exception {

        String separator = "$";

        // Remove first $ character
        misc = misc.substring(1);
        //System.out.println("misc: " + misc);
        // Counter
        int start = 0;
        int nextIndex = misc.indexOf(separator, start);
        int length = nextIndex - start;
        int numberExpry = 0;
        String miscList = new String();
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

    @Override
    public int setArReceiptAllNewAndVoid(String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier) {

        return 0;
    }

    @Override
    public int setArMiscReceiptAllNewAndVoid(String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier, String companyShortName) {

        Debug.print("ArMiscReceiptSyncControllerBean setArMiscReceiptAllNewAndVoid");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(branchCode);

            int success = 0;

            if (adPreference.getPrfMiscPosDiscountAccount() != null
                    && adPreference.getPrfMiscPosGiftCertificateAccount() != null
                    && adPreference.getPrfMiscPosServiceChargeAccount() != null
                    && adPreference.getPrfMiscPosDineInChargeAccount() != null) {

                // new receipts
                HashMap receiptNumbers = new HashMap();
                HashMap invoiceNumbers = new HashMap();
                String fundTransferNumber = null;
                String firstNumber = "";
                String lastNumber = "";

                for (int i = 0; i < receipts.length; i++) {

                    ArModReceiptDetails arModReceiptDetails = receiptDecode(receipts[i]);

                    if (i == 0) {
                        firstNumber = arModReceiptDetails.getRctNumber();
                    }
                    if (i == receipts.length - 1) {
                        lastNumber = arModReceiptDetails.getRctNumber();
                    }

                    if (arModReceiptDetails.getRctPosOnAccount() == 0) {

                        // generate receipt number
                        String generatedReceipt;
                        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
                        LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
                        try {
                            adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AR RECEIPT", companyCode);
                        }
                        catch (FinderException ex) {
                        }

                        try {
                            adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome
                                    .findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode);
                        }
                        catch (FinderException ex) {
                        }

                        while (true) {
                            if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {
                                try {
                                    arReceiptHome.findByRctNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), branchCode, companyCode);
                                    adDocumentSequenceAssignment.setDsaNextSequence(
                                            EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                }
                                catch (FinderException ex) {
                                    generatedReceipt = adDocumentSequenceAssignment.getDsaNextSequence();
                                    adDocumentSequenceAssignment.setDsaNextSequence(
                                            EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                    break;
                                }
                            } else {
                                try {
                                    arReceiptHome.findByRctNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), branchCode, companyCode);
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(
                                            EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                }
                                catch (FinderException ex) {
                                    generatedReceipt = adBranchDocumentSequenceAssignment.getBdsNextSequence();
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(
                                            EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                    break;
                                }
                            }
                        }

                        LocalArReceipt arExistingReceipt = null;
                        Iterator rctIter = receiptNumbers.values().iterator();
                        while (rctIter.hasNext()) {
                            try {
                                ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) rctIter.next();
                                arExistingReceipt = arReceiptHome.findByRctDateAndRctNumberAndCstCustomerCodeAndBrCode(arModReceiptDetails.getRctDate(), arModUploadReceiptDetails.getRctNumber(), arModReceiptDetails.getRctCstCustomerCode(), branchCode, companyCode);
                                break;
                            }
                            catch (FinderException ex) {
                                continue;
                            }

                        }

                        if (arExistingReceipt == null) {
                            ArModReceiptDetails arModUploadReceiptDetails = new ArModReceiptDetails();
                            arModUploadReceiptDetails.setRctNumber(generatedReceipt);
                            arModUploadReceiptDetails.setRctPosDiscount(
                                    EJBCommon.roundIt(arModReceiptDetails.getRctPosDiscount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosScAmount(
                                    EJBCommon.roundIt(arModReceiptDetails.getRctPosScAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosDcAmount(
                                    EJBCommon.roundIt(arModReceiptDetails.getRctPosDcAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            receiptNumbers.put(generatedReceipt, arModUploadReceiptDetails);

                            double totalAmount = EJBCommon.roundIt((arModReceiptDetails.getRctPosTotalAmount()
                                    - arModReceiptDetails.getRctPosVoidAmount()) * arModReceiptDetails.getRctConversionRate(), (short) 2);

                            LocalArReceipt arReceipt = arReceiptHome.create("MISC", "POS Sales "
                                            + new Date(), arModReceiptDetails.getRctDate(), generatedReceipt,
                                    null, null, null, null,
                                    null, null, null, null,
                                    totalAmount, 0d, 0d, 0d, 0d,
                                    0d, 0d, null, 1,
                                    null, "CASH", EJBCommon.FALSE, 0,
                                    null, null, EJBCommon.FALSE, null,
                                    EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE,
                                    EJBCommon.FALSE, EJBCommon.FALSE, null, null,
                                    null, null, null, cashier,
                                    EJBCommon.getGcCurrentDateWoTime().getTime(), cashier,
                                    EJBCommon.getGcCurrentDateWoTime().getTime(), null,
                                    null, null, null, EJBCommon.FALSE,
                                    null, EJBCommon.FALSE, EJBCommon.FALSE, null, null,
                                    EJBCommon.FALSE, EJBCommon.FALSE, null, branchCode, companyCode);

                            LocalGlFunctionalCurrency glFunctionalCurrency
                                    = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                            //glFunctionalCurrency.addArReceipt(arReceipt);
                            arReceipt.setGlFunctionalCurrency(glFunctionalCurrency);

                            System.out.println("arModReceiptDetails.getRctCstCustomerCode(): " + arModReceiptDetails.getRctCstCustomerCode());
                            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(arModReceiptDetails.getRctCstCustomerCode(), companyCode);
                            //arCustomer.addArReceipt(arReceipt);
                            arReceipt.setArCustomer(arCustomer);

                            String bankAccount = "";
                            String BR_BRNCH_CODE = adBranch.getBrBranchCode();
                            if (BR_BRNCH_CODE.equals("Manila-Smoking Lounge")) {
                                bankAccount = "Allied Bank-IPT Smoking";
                            } else if (BR_BRNCH_CODE.equals("Manila-Cigar Shop")) {
                                bankAccount = "Allied Bank Cigar shop";
                            } else if (BR_BRNCH_CODE.equals("Manila-Term.#2 Domestic")) {
                                bankAccount = "Terminal II Domestic";
                            } else if (BR_BRNCH_CODE.equals("Manila-Term.#2 Intl")) {
                                bankAccount = "Term2 International";
                            } else if (BR_BRNCH_CODE.equals("Cebu-Banilad")) {
                                bankAccount = "Metrobank Banilad";
                            } else if (BR_BRNCH_CODE.equals("Cebu-Gorordo")) {
                                bankAccount = "Metrobank-Gorordo";
                            } else if (BR_BRNCH_CODE.equals("Cebu-Mactan Domestic")) {
                                bankAccount = "Metrobank Mactan Domestic";
                            } else if (BR_BRNCH_CODE.equals("Cebu-Mactan Intl")) {
                                bankAccount = "Metrobank I Mactan Int'l";
                            } else if (BR_BRNCH_CODE.equals("Cebu-Supercat")) {
                                bankAccount = "Metrobank Supercat";
                            } else {
                                bankAccount = arCustomer.getAdBankAccount().getBaName();
                            }

                            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(bankAccount, companyCode);
                            //adBankAccount.addArReceipt(arReceipt);
                            arReceipt.setAdBankAccount(adBankAccount);

                            LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INCLUSIVE", companyCode);
                            //arTaxCode.addArReceipt(arReceipt);
                            arReceipt.setArTaxCode(arTaxCode);

                            LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName("NONE", companyCode);
                            //arWithholdingTaxCode.addArReceipt(arReceipt);
                            arReceipt.setArWithholdingTaxCode(arWithholdingTaxCode);

                            LocalArReceiptBatch arReceiptBatch = null;
                            try {
                                arReceiptBatch = arReceiptBatchHome.findByRbName("POS BATCH", branchCode, companyCode);
                            }
                            catch (FinderException ex) {
                                arReceiptBatch = arReceiptBatchHome.create("POS BATCH", "POS BATCH", "OPEN", "MISC", EJBCommon.getGcCurrentDateWoTime().getTime(), cashier, branchCode, companyCode);

                            }
                            //arReceiptBatch.addArReceipt(arReceipt);
                            arReceipt.setArReceiptBatch(arReceiptBatch);

                            Iterator iter = arModReceiptDetails.getInvIliList().iterator();
                            short lineNumber = 0;
                            while (iter.hasNext()) {


                                ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = (ArModInvoiceLineItemDetails) iter.next();
                                LocalArInvoiceLineItem arInvoiceLineItem
                                        = arInvoiceLineItemHome.create(++lineNumber, arModInvoiceLineItemDetails.getIliQuantity(),
                                        arModInvoiceLineItemDetails.getIliUnitPrice(),
                                        EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount()
                                                / (1 + arReceipt.getArTaxCode().getTcRate() / 100), (short) 2),
                                        EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount()
                                                - (arModInvoiceLineItemDetails.getIliAmount()
                                                / (1 + arReceipt.getArTaxCode().getTcRate() / 100)), (short) 2), EJBCommon.FALSE,
                                        0, 0, 0, 0, 0, EJBCommon.TRUE, companyCode);

                                //arReceipt.addArInvoiceLineItem(arInvoiceLineItem);
                                arInvoiceLineItem.setArReceipt(arReceipt);

                                LocalInvUnitOfMeasure invUnitOfMeasure
                                        = invUnitOfMeasureHome.findByUomName(arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                //invUnitOfMeasure.addArInvoiceLineItem(arInvoiceLineItem);
                                arInvoiceLineItem.setInvUnitOfMeasure(invUnitOfMeasure);

                                LocalInvItem invItem = invItemHome.findByIiName(arModInvoiceLineItemDetails.getIliIiName(), companyCode);
                                LocalInvLocation invLocation = null;
                                System.out.println("Item Default Location! " + invItem.getIiDefaultLocation());
                                System.out.println("2 Item Name! " + invItem.getIiName());
                                System.out.println("3 Item Name! " + arModInvoiceLineItemDetails.getIliIiName());
                                try {
                                    if (invItem.getIiDefaultLocation() != null) {
                                        invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                                    }
                                }
                                catch (FinderException ex) {
                                }

                                LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(arModInvoiceLineItemDetails.getIliIiName(), invLocation == null ? arModInvoiceLineItemDetails.getIliLocName() : invLocation.getLocName(), companyCode);
                                //invItemLocation.addArInvoiceLineItem(arInvoiceLineItem);
                                arInvoiceLineItem.setInvItemLocation(invItemLocation);

                                if (invItemLocation.getInvItem().getIiEnableAutoBuild() == EJBCommon.TRUE) {

                                    arInvoiceLineItem.setIliEnableAutoBuild(EJBCommon.TRUE);

                                }
                            }

                        } else {

                            ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) receiptNumbers.get(arExistingReceipt.getRctNumber());

                            arModUploadReceiptDetails.setRctPosDiscount(arModUploadReceiptDetails.getRctPosDiscount() + EJBCommon.roundIt(arModReceiptDetails.getRctPosDiscount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosScAmount(arModUploadReceiptDetails.getRctPosScAmount() + EJBCommon.roundIt(arModReceiptDetails.getRctPosScAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosDcAmount(arModUploadReceiptDetails.getRctPosDcAmount() + EJBCommon.roundIt(arModReceiptDetails.getRctPosDcAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            receiptNumbers.put(arExistingReceipt.getRctNumber(), arModUploadReceiptDetails);

                            double totalAmount = EJBCommon.roundIt((arModReceiptDetails.getRctPosTotalAmount() - arModReceiptDetails.getRctPosVoidAmount()) * arModReceiptDetails.getRctConversionRate(), (short) 2);
                            arExistingReceipt.setRctAmount(arExistingReceipt.getRctAmount() + totalAmount);

                            Iterator iter = arModReceiptDetails.getInvIliList().iterator();


                            while (iter.hasNext()) {

                                ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = (ArModInvoiceLineItemDetails) iter.next();
                                System.out.println(arModInvoiceLineItemDetails.getIliIiName());
                                LocalInvItem invItem = invItemHome.findByIiName(arModInvoiceLineItemDetails.getIliIiName(), companyCode);
                                LocalInvLocation invLocation = null;
                                try {
                                    if (invItem.getIiDefaultLocation() != null) {
                                        invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                                    }
                                }
                                catch (FinderException ex) {
                                }
                                LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(arModInvoiceLineItemDetails.getIliIiName(), invLocation == null ? arModInvoiceLineItemDetails.getIliLocName() : invLocation.getLocName(), companyCode);

                                LocalArInvoiceLineItem arExistingInvoiceLineItem = null;

                                try {
                                    arExistingInvoiceLineItem = arInvoiceLineItemHome.findByRctCodeAndIlCodeAndUomName(arExistingReceipt.getRctCode(), invItemLocation.getIlCode(), arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                }
                                catch (FinderException ex) {

                                }

                                if (arExistingInvoiceLineItem == null) {
                                    System.out.println(arModInvoiceLineItemDetails.getIliIiName());
                                    LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.create((short) (arExistingReceipt.getArInvoiceLineItems().size() + 1), arModInvoiceLineItemDetails.getIliQuantity(), arModInvoiceLineItemDetails.getIliUnitPrice(), EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingReceipt.getArTaxCode().getTcRate() / 100), (short) 2), EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() - (arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingReceipt.getArTaxCode().getTcRate() / 100)), (short) 2), EJBCommon.FALSE, 0, 0, 0, 0, 0, EJBCommon.TRUE, companyCode);

                                    //arExistingReceipt.addArInvoiceLineItem(arInvoiceLineItem);
                                    arInvoiceLineItem.setArReceipt(arExistingReceipt);

                                    LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                    //invUnitOfMeasure.addArInvoiceLineItem(arInvoiceLineItem);
                                    arInvoiceLineItem.setInvUnitOfMeasure(invUnitOfMeasure);

                                    //invItemLocation.addArInvoiceLineItem(arInvoiceLineItem);
                                    arInvoiceLineItem.setInvItemLocation(invItemLocation);
                                    if (invItemLocation.getInvItem().getIiEnableAutoBuild() == EJBCommon.TRUE) {

                                        arInvoiceLineItem.setIliEnableAutoBuild(EJBCommon.TRUE);

                                    }
                                } else {

                                    arExistingInvoiceLineItem.setIliQuantity(arExistingInvoiceLineItem.getIliQuantity() + arModInvoiceLineItemDetails.getIliQuantity());
                                    arExistingInvoiceLineItem.setIliUnitPrice((arExistingInvoiceLineItem.getIliUnitPrice() + arModInvoiceLineItemDetails.getIliUnitPrice()) / 2);
                                    arExistingInvoiceLineItem.setIliAmount(arExistingInvoiceLineItem.getIliAmount() + EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingReceipt.getArTaxCode().getTcRate() / 100), (short) 2));
                                    arExistingInvoiceLineItem.setIliTaxAmount(arExistingInvoiceLineItem.getIliTaxAmount() + EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() - (arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingReceipt.getArTaxCode().getTcRate() / 100)), (short) 2));

                                }
                            }
                        }

                    } else {

                        // generate Invoice number

                        String generatedInvoice = null;

                        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
                        LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;

                        try {

                            adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AR INVOICE", companyCode);

                        }
                        catch (FinderException ex) {

                        }

                        try {

                            adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode);

                        }
                        catch (FinderException ex) {

                        }

                        while (true) {

                            if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                                try {

                                    arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), (byte) 0, branchCode, companyCode);
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                                }
                                catch (FinderException ex) {

                                    generatedInvoice = adDocumentSequenceAssignment.getDsaNextSequence();
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                    break;

                                }

                            } else {

                                try {

                                    arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), (byte) 0, branchCode, companyCode);
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                                }
                                catch (FinderException ex) {

                                    generatedInvoice = adBranchDocumentSequenceAssignment.getBdsNextSequence();
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                    break;

                                }

                            }

                        }
                        LocalArInvoice arExistingInvoice = null;

                        Iterator invIter = invoiceNumbers.values().iterator();
                        while (invIter.hasNext()) {

                            try {

                                ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) invIter.next();

                                arExistingInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndCstCustomerCode(arModUploadReceiptDetails.getRctNumber(), (byte) 0, arModReceiptDetails.getRctCstCustomerCode(), companyCode);

                                break;

                            }
                            catch (FinderException ex) {

                                continue;

                            }


                        }

                        if (arExistingInvoice == null) {

                            ArModReceiptDetails arModUploadReceiptDetails = new ArModReceiptDetails();
                            arModUploadReceiptDetails.setRctNumber(generatedInvoice);
                            arModUploadReceiptDetails.setRctPosDiscount(EJBCommon.roundIt(arModReceiptDetails.getRctPosDiscount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosScAmount(EJBCommon.roundIt(arModReceiptDetails.getRctPosScAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosDcAmount(EJBCommon.roundIt(arModReceiptDetails.getRctPosDcAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            invoiceNumbers.put(generatedInvoice, arModUploadReceiptDetails);

                            double totalAmount = EJBCommon.roundIt((arModReceiptDetails.getRctPosTotalAmount() - arModReceiptDetails.getRctPosVoidAmount()) * arModReceiptDetails.getRctConversionRate(), (short) 2);

                            LocalArInvoice arInvoice = arInvoiceHome.create("ITEMS", (byte) 0, "POS Sales " + new Date().toString(), arModReceiptDetails.getRctDate(), generatedInvoice, null, null, null, null, totalAmount, 0d, 0d, 0d, 0d, 0d, null, 1, null, 0d, 0d, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, null, 0d, null, null, null, null, cashier, EJBCommon.getGcCurrentDateWoTime().getTime(), cashier, EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, null, null, EJBCommon.FALSE, null, null, null, (byte) 0, (byte) 0, null, arModReceiptDetails.getRctDate(), branchCode, companyCode);

                            /*
                             * RctPaymentMethod is Used Temporarily for Payment Term Field
                             */
                            if (adCompany.getCmpShortName().toLowerCase().equals("tinderbox") || adCompany.getCmpShortName().toLowerCase().equals("hs")) {
                                LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName("IMMEDIATE", companyCode);
                                //adPaymentTerm.addArInvoice(arInvoice);
                                arInvoice.setAdPaymentTerm(adPaymentTerm);
                            } else {
                                if (arModReceiptDetails.getRctPaymentMethod() == null || arModReceiptDetails.getRctPaymentMethod().equals("")) {
                                    LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName("IMMEDIATE", companyCode);
                                    //adPaymentTerm.addArInvoice(arInvoice);
                                    arInvoice.setAdPaymentTerm(adPaymentTerm);
                                } else {
                                    LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName(arModReceiptDetails.getRctPaymentMethod(), companyCode);
                                    //adPaymentTerm.addArInvoice(arInvoice);
                                    arInvoice.setAdPaymentTerm(adPaymentTerm);
                                }
                            }

                            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                            //glFunctionalCurrency.addArInvoice(arInvoice);
                            arInvoice.setGlFunctionalCurrency(glFunctionalCurrency);

                            System.out.println("POS Customer: " + arModReceiptDetails.getRctCstCustomerCode());
                            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(arModReceiptDetails.getRctCstCustomerCode(), companyCode);
                            //arCustomer.addArInvoice(arInvoice);
                            arInvoice.setArCustomer(arCustomer);

                            if (adCompany.getCmpShortName().toLowerCase().equals("tinderbox") || adCompany.getCmpShortName().toLowerCase().equals("hs")) {
                                LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INCLUSIVE", companyCode);
                                //arTaxCode.addArInvoice(arInvoice);
                                arInvoice.setArTaxCode(arTaxCode);
                            } else {
                                try {
                                    LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName(arModReceiptDetails.getRctTcName(), companyCode);
                                    //arTaxCode.addArInvoice(arInvoice);
                                    arInvoice.setArTaxCode(arTaxCode);
                                }
                                catch (Exception ex) {
                                    LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INCLUSIVE", companyCode);
                                    //arTaxCode.addArInvoice(arInvoice);
                                    arInvoice.setArTaxCode(arTaxCode);
                                }
                            }

                            LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName("NONE", companyCode);
                            //arWithholdingTaxCode.addArInvoice(arInvoice);
                            arInvoice.setArWithholdingTaxCode(arWithholdingTaxCode);

                            LocalArInvoiceBatch arInvoiceBatch = null;
                            try {
                                arInvoiceBatch = arInvoiceBatchHome.findByIbName("POS BATCH", branchCode, companyCode);
                            }
                            catch (FinderException ex) {
                                arInvoiceBatch = arInvoiceBatchHome.create("POS BATCH", "POS BATCH", "OPEN", "MISC", EJBCommon.getGcCurrentDateWoTime().getTime(), cashier, branchCode, companyCode);

                            }
                            //arInvoiceBatch.addArInvoice(arInvoice);
                            arInvoice.setArInvoiceBatch(arInvoiceBatch);


                            Iterator iter = arModReceiptDetails.getInvIliList().iterator();
                            short lineNumber = 0;
                            while (iter.hasNext()) {

                                ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = (ArModInvoiceLineItemDetails) iter.next();
                                LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.create(++lineNumber, arModInvoiceLineItemDetails.getIliQuantity(), arModInvoiceLineItemDetails.getIliUnitPrice(), EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() / (1 + arInvoice.getArTaxCode().getTcRate() / 100), (short) 2), EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() - (arModInvoiceLineItemDetails.getIliAmount() / (1 + arInvoice.getArTaxCode().getTcRate() / 100)), (short) 2), EJBCommon.FALSE, 0, 0, 0, 0, 0, EJBCommon.TRUE, companyCode);

                                //arInvoice.addArInvoiceLineItem(arInvoiceLineItem);
                                arInvoiceLineItem.setArInvoice(arInvoice);

                                LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                //invUnitOfMeasure.addArInvoiceLineItem(arInvoiceLineItem);
                                arInvoiceLineItem.setInvUnitOfMeasure(invUnitOfMeasure);


                                LocalInvItem invItem = invItemHome.findByIiName(arModInvoiceLineItemDetails.getIliIiName(), companyCode);
                                LocalInvLocation invLocation = null;
                                try {
                                    if (invItem.getIiDefaultLocation() != null) {
                                        invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                                    }
                                }
                                catch (FinderException ex) {
                                }
                                LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(arModInvoiceLineItemDetails.getIliIiName(), invLocation == null ? arModInvoiceLineItemDetails.getIliLocName() : invLocation.getLocName(), companyCode);
                                //invItemLocation.addArInvoiceLineItem(arInvoiceLineItem);
                                arInvoiceLineItem.setInvItemLocation(invItemLocation);
                                if (invItemLocation.getInvItem().getIiEnableAutoBuild() == EJBCommon.TRUE) {

                                    arInvoiceLineItem.setIliEnableAutoBuild(EJBCommon.TRUE);

                                }
                            }

                        } else {

                            ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) invoiceNumbers.get(arExistingInvoice.getInvNumber());

                            arModUploadReceiptDetails.setRctPosDiscount(arModUploadReceiptDetails.getRctPosDiscount() + EJBCommon.roundIt(arModReceiptDetails.getRctPosDiscount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosScAmount(arModUploadReceiptDetails.getRctPosScAmount() + EJBCommon.roundIt(arModReceiptDetails.getRctPosScAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosDcAmount(arModUploadReceiptDetails.getRctPosDcAmount() + EJBCommon.roundIt(arModReceiptDetails.getRctPosDcAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            invoiceNumbers.put(arExistingInvoice.getInvNumber(), arModUploadReceiptDetails);

                            double totalAmount = EJBCommon.roundIt((arModReceiptDetails.getRctPosTotalAmount() - arModReceiptDetails.getRctPosVoidAmount()) * arModReceiptDetails.getRctConversionRate(), (short) 2);
                            arExistingInvoice.setInvAmountDue(arExistingInvoice.getInvAmountDue() + totalAmount);

                            Iterator iter = arModReceiptDetails.getInvIliList().iterator();


                            while (iter.hasNext()) {

                                ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = (ArModInvoiceLineItemDetails) iter.next();

                                LocalInvItem invItem = invItemHome.findByIiName(arModInvoiceLineItemDetails.getIliIiName(), companyCode);
                                LocalInvLocation invLocation = null;
                                try {
                                    if (invItem.getIiDefaultLocation() != null) {
                                        invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                                    }
                                }
                                catch (FinderException ex) {
                                }
                                LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(arModInvoiceLineItemDetails.getIliIiName(), invLocation == null ? arModInvoiceLineItemDetails.getIliLocName() : invLocation.getLocName(), companyCode);

                                LocalArInvoiceLineItem arExistingInvoiceLineItem = null;

                                try {
                                    arExistingInvoiceLineItem = arInvoiceLineItemHome.findByRctCodeAndIlCodeAndUomName(arExistingInvoice.getInvCode(), invItemLocation.getIlCode(), arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                }
                                catch (FinderException ex) {

                                }

                                if (arExistingInvoiceLineItem == null) {

                                    LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.create((short) (arExistingInvoice.getArInvoiceLineItems().size() + 1), arModInvoiceLineItemDetails.getIliQuantity(), arModInvoiceLineItemDetails.getIliUnitPrice(), EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingInvoice.getArTaxCode().getTcRate() / 100), (short) 2), EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() - (arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingInvoice.getArTaxCode().getTcRate() / 100)), (short) 2), EJBCommon.FALSE, 0, 0, 0, 0, 0, EJBCommon.TRUE, companyCode);

                                    //arExistingInvoice.addArInvoiceLineItem(arInvoiceLineItem);
                                    arInvoiceLineItem.setArInvoice(arExistingInvoice);

                                    LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                    //invUnitOfMeasure.addArInvoiceLineItem(arInvoiceLineItem);
                                    arInvoiceLineItem.setInvUnitOfMeasure(invUnitOfMeasure);

                                    //invItemLocation.addArInvoiceLineItem(arInvoiceLineItem);
                                    arInvoiceLineItem.setInvItemLocation(invItemLocation);
                                    if (invItemLocation.getInvItem().getIiEnableAutoBuild() == EJBCommon.TRUE) {

                                        arInvoiceLineItem.setIliEnableAutoBuild(EJBCommon.TRUE);

                                    }
                                } else {

                                    arExistingInvoiceLineItem.setIliQuantity(arExistingInvoiceLineItem.getIliQuantity() + arModInvoiceLineItemDetails.getIliQuantity());
                                    arExistingInvoiceLineItem.setIliUnitPrice((arExistingInvoiceLineItem.getIliUnitPrice() + arModInvoiceLineItemDetails.getIliUnitPrice()) / 2);
                                    arExistingInvoiceLineItem.setIliAmount(arExistingInvoiceLineItem.getIliAmount() + EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingInvoice.getArTaxCode().getTcRate() / 100), (short) 2));
                                    arExistingInvoiceLineItem.setIliTaxAmount(arExistingInvoiceLineItem.getIliTaxAmount() + EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() - (arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingInvoice.getArTaxCode().getTcRate() / 100)), (short) 2));

                                }
                            }
                        }

                    }

                }

                // for each receipt generate distribution records

                Iterator rctIter = receiptNumbers.values().iterator();

                while (rctIter.hasNext()) {

                    ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) rctIter.next();

                    LocalArReceipt arReceipt = arReceiptHome.findByRctNumberAndBrCode(arModUploadReceiptDetails.getRctNumber(), branchCode, companyCode);
                    arReceipt.setRctReferenceNumber(firstNumber + "-" + lastNumber);

                    Iterator lineIter = arReceipt.getArInvoiceLineItems().iterator();

                    double TOTAL_TAX = 0;
                    double TOTAL_LINE = 0;

                    while (lineIter.hasNext()) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) lineIter.next();

                        TOTAL_LINE += arInvoiceLineItem.getIliTaxAmount() + arInvoiceLineItem.getIliAmount();

                        LocalInvItemLocation invItemLocation = arInvoiceLineItem.getInvItemLocation();

                        // add cost of sales distribution and inventory

                        double COST = 0d;

                        try {

                            LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arReceipt.getRctDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);

                            COST = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                        }
                        catch (FinderException ex) {

                            COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                        }

                        double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                        LocalAdBranchItemLocation adBranchItemLocation = null;

                        try {

                            adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arInvoiceLineItem.getInvItemLocation().getIlCode(), branchCode, companyCode);

                        }
                        catch (FinderException ex) {

                        }

                        if (arInvoiceLineItem.getIliEnableAutoBuild() == 0 || arInvoiceLineItem.getIliEnableAutoBuild() == 1 || arInvoiceLineItem.getInvItemLocation().getInvItem().getIiClass().equals("Stock")) {

                            if (adBranchItemLocation != null) {

                                this.addArDrIliEntry(arReceipt.getArDrNextLine(), "COGS", EJBCommon.TRUE, COST * QTY_SLD, adBranchItemLocation.getBilCoaGlCostOfSalesAccount(), arReceipt, branchCode, companyCode);

                                this.addArDrIliEntry(arReceipt.getArDrNextLine(), "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD, adBranchItemLocation.getBilCoaGlInventoryAccount(), arReceipt, branchCode, companyCode);

                            } else {

                                this.addArDrIliEntry(arReceipt.getArDrNextLine(), "COGS", EJBCommon.TRUE, COST * QTY_SLD, arInvoiceLineItem.getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arReceipt, branchCode, companyCode);

                                this.addArDrIliEntry(arReceipt.getArDrNextLine(), "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD, arInvoiceLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(), arReceipt, branchCode, companyCode);

                            }

                            // add quantity to item location committed quantity

                            double convertedQuantity = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);
                            invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() + convertedQuantity);

                        }

                        // add inventory sale distributions

                        if (adBranchItemLocation != null) {

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(), "REVENUE", EJBCommon.FALSE, arInvoiceLineItem.getIliAmount(), adBranchItemLocation.getBilCoaGlSalesAccount(), arReceipt, branchCode, companyCode);

                        } else {

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(), "REVENUE", EJBCommon.FALSE, arInvoiceLineItem.getIliAmount(), arInvoiceLineItem.getInvItemLocation().getIlGlCoaSalesAccount(), arReceipt, branchCode, companyCode);

                        }

                        TOTAL_TAX += arInvoiceLineItem.getIliTaxAmount();

                    }

                    // add tax distribution if necessary

                    if (!arReceipt.getArTaxCode().getTcType().equals("NONE") && !arReceipt.getArTaxCode().getTcType().equals("EXEMPT")) {

                        if (adCompany.getCmpShortName().toLowerCase().equals("tinderbox")) {
                            String brTaxCOA = this.getBranchTaxCOA(adBranch.getBrBranchCode(), arReceipt.getArTaxCode().getGlChartOfAccount().getCoaSegment1(), arReceipt.getArTaxCode().getGlChartOfAccount().getCoaSegment3(), arReceipt.getArTaxCode().getGlChartOfAccount().getCoaSegment4(), arReceipt.getArTaxCode().getGlChartOfAccount().getCoaSegment5());

                            this.addArDrEntry(arReceipt.getArDrNextLine(), "TAX", EJBCommon.FALSE, TOTAL_TAX, glChartOfAccountHome.findByCoaAccountNumber(brTaxCOA, companyCode).getCoaCode(), EJBCommon.FALSE, arReceipt, branchCode, companyCode);
                        } else {

                            this.addArDrEntry(arReceipt.getArDrNextLine(), "TAX", EJBCommon.FALSE, TOTAL_TAX, arReceipt.getArTaxCode().getGlChartOfAccount().getCoaCode(), EJBCommon.FALSE, arReceipt, branchCode, companyCode);

                        }

                    }

                    // add cash distribution

                    this.addArDrEntry(arReceipt.getArDrNextLine(), "CASH", EJBCommon.TRUE, arReceipt.getRctAmount(), arReceipt.getAdBankAccount().getBaCoaGlCashAccount(), EJBCommon.FALSE, arReceipt, branchCode, companyCode);

                    if (arModUploadReceiptDetails.getRctPosDiscount() != 0) {
                        // add discount
                        this.addArDrEntry(arReceipt.getArDrNextLine(), "DISCOUNT", EJBCommon.TRUE, arModUploadReceiptDetails.getRctPosDiscount(), adPreference.getPrfMiscPosDiscountAccount(), EJBCommon.FALSE, arReceipt, branchCode, companyCode);
                    }

                    if (arModUploadReceiptDetails.getRctPosScAmount() != 0) {
                        // add sc amount
                        this.addArDrEntry(arReceipt.getArDrNextLine(), "SERVCE CHARGE", EJBCommon.FALSE, arModUploadReceiptDetails.getRctPosScAmount(), adPreference.getPrfMiscPosServiceChargeAccount(), EJBCommon.FALSE, arReceipt, branchCode, companyCode);
                    }

                    if (arModUploadReceiptDetails.getRctPosDcAmount() != 0) {
                        // add dc amount
                        this.addArDrEntry(arReceipt.getArDrNextLine(), "DINEIN CHARGE", EJBCommon.FALSE, arModUploadReceiptDetails.getRctPosDcAmount(), adPreference.getPrfMiscPosDineInChargeAccount(), EJBCommon.FALSE, arReceipt, branchCode, companyCode);
                    }


                    // add forex gain/loss

                    double forexGainLoss = TOTAL_LINE - (arReceipt.getRctAmount() + arModUploadReceiptDetails.getRctPosDiscount() - arModUploadReceiptDetails.getRctPosScAmount() - arModUploadReceiptDetails.getRctPosDcAmount());
                    if (forexGainLoss != 0) {
                        this.addArDrEntry(arReceipt.getArDrNextLine(), "FOREX", forexGainLoss > 0 ? EJBCommon.TRUE : EJBCommon.FALSE, Math.abs(forexGainLoss), adPreference.getPrfMiscPosGiftCertificateAccount(), EJBCommon.FALSE, arReceipt, branchCode, companyCode);
                    }

                }

                Iterator invIter = invoiceNumbers.values().iterator();

                while (invIter.hasNext()) {

                    ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) invIter.next();

                    LocalArInvoice arInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arModUploadReceiptDetails.getRctNumber(), (byte) 0, branchCode, companyCode);
                    arInvoice.setInvReferenceNumber(firstNumber + "-" + lastNumber);

                    short precisionUnit = this.getGlFcPrecisionUnit(companyCode);
                    double TOTAL_PAYMENT_SCHEDULE = 0d;

                    GregorianCalendar gcPrevDateDue = new GregorianCalendar();
                    GregorianCalendar gcDateDue = new GregorianCalendar();
                    gcPrevDateDue.setTime(arInvoice.getInvEffectivityDate());

                    Collection adPaymentSchedules = arInvoice.getAdPaymentTerm().getAdPaymentSchedules();

                    Iterator i = adPaymentSchedules.iterator();

                    while (i.hasNext()) {

                        LocalAdPaymentSchedule adPaymentSchedule = (LocalAdPaymentSchedule) i.next();

                        // get date due

                        if (arInvoice.getAdPaymentTerm().getPytScheduleBasis().equals("DEFAULT")) {

                            gcDateDue.setTime(arInvoice.getInvEffectivityDate());
                            gcDateDue.add(Calendar.DATE, adPaymentSchedule.getPsDueDay());

                        } else if (arInvoice.getAdPaymentTerm().getPytScheduleBasis().equals("MONTHLY")) {

                            gcDateDue = gcPrevDateDue;
                            gcDateDue.add(Calendar.MONTH, 1);
                            gcPrevDateDue = gcDateDue;

                        } else if (arInvoice.getAdPaymentTerm().getPytScheduleBasis().equals("BI-MONTHLY")) {

                            gcDateDue = gcPrevDateDue;

                            if (gcPrevDateDue.get(Calendar.MONTH) != 1) {
                                if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 31 && gcPrevDateDue.get(Calendar.DATE) > 15 && gcPrevDateDue.get(Calendar.DATE) < 31) {
                                    gcDateDue.add(Calendar.DATE, 16);
                                } else {
                                    gcDateDue.add(Calendar.DATE, 15);
                                }
                            } else if (gcPrevDateDue.get(Calendar.MONTH) == 1) {
                                if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 28 && gcPrevDateDue.get(Calendar.DATE) == 14) {
                                    gcDateDue.add(Calendar.DATE, 14);
                                } else if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 28 && gcPrevDateDue.get(Calendar.DATE) >= 15 && gcPrevDateDue.get(Calendar.DATE) < 28) {
                                    gcDateDue.add(Calendar.DATE, 13);
                                } else if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 29 && gcPrevDateDue.get(Calendar.DATE) >= 15 && gcPrevDateDue.get(Calendar.DATE) < 29) {
                                    gcDateDue.add(Calendar.DATE, 14);
                                } else {
                                    gcDateDue.add(Calendar.DATE, 15);
                                }
                            }

                            gcPrevDateDue = gcDateDue;

                        }

                        // create a payment schedule

                        double PAYMENT_SCHEDULE_AMOUNT = 0;

                        // if last payment schedule subtract to avoid rounding difference error

                        if (i.hasNext()) {

                            PAYMENT_SCHEDULE_AMOUNT = EJBCommon.roundIt((adPaymentSchedule.getPsRelativeAmount() / arInvoice.getAdPaymentTerm().getPytBaseAmount()) * arInvoice.getInvAmountDue(), precisionUnit);

                        } else {

                            PAYMENT_SCHEDULE_AMOUNT = arInvoice.getInvAmountDue() - TOTAL_PAYMENT_SCHEDULE;

                        }

                        LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = arInvoicePaymentScheduleHome.create(gcDateDue.getTime(), adPaymentSchedule.getPsLineNumber(), PAYMENT_SCHEDULE_AMOUNT, 0d, EJBCommon.FALSE, (short) 0, gcDateDue.getTime(), 0d, 0d, companyCode);

                        //arInvoice.addArInvoicePaymentSchedule(arInvoicePaymentSchedule);
                        arInvoicePaymentSchedule.setArInvoice(arInvoice);

                        TOTAL_PAYMENT_SCHEDULE += PAYMENT_SCHEDULE_AMOUNT;

                    }

                    Iterator lineIter = arInvoice.getArInvoiceLineItems().iterator();

                    double TOTAL_TAX = 0;
                    double TOTAL_LINE = 0;

                    while (lineIter.hasNext()) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) lineIter.next();

                        TOTAL_LINE += arInvoiceLineItem.getIliTaxAmount() + arInvoiceLineItem.getIliAmount();

                        LocalInvItemLocation invItemLocation = arInvoiceLineItem.getInvItemLocation();

                        // add cost of sales distribution and inventory

                        double COST = 0d;

                        try {

                            LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);

                            COST = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                        }
                        catch (FinderException ex) {

                            COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                        }

                        double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                        LocalAdBranchItemLocation adBranchItemLocation = null;

                        try {

                            adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arInvoiceLineItem.getInvItemLocation().getIlCode(), branchCode, companyCode);

                        }
                        catch (FinderException ex) {

                        }

                        if (arInvoiceLineItem.getIliEnableAutoBuild() == 0 || arInvoiceLineItem.getIliEnableAutoBuild() == 1 || arInvoiceLineItem.getInvItemLocation().getInvItem().getIiClass().equals("Stock")) {

                            if (adBranchItemLocation != null) {

                                this.addArDrIliEntry(arInvoice.getArDrNextLine(), "COGS", EJBCommon.TRUE, COST * QTY_SLD, adBranchItemLocation.getBilCoaGlCostOfSalesAccount(), arInvoice, branchCode, companyCode);

                                this.addArDrIliEntry(arInvoice.getArDrNextLine(), "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD, adBranchItemLocation.getBilCoaGlInventoryAccount(), arInvoice, branchCode, companyCode);

                            } else {

                                this.addArDrIliEntry(arInvoice.getArDrNextLine(), "COGS", EJBCommon.TRUE, COST * QTY_SLD, arInvoiceLineItem.getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arInvoice, branchCode, companyCode);

                                this.addArDrIliEntry(arInvoice.getArDrNextLine(), "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD, arInvoiceLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(), arInvoice, branchCode, companyCode);

                            }

                            // add quantity to item location committed quantity

                            double convertedQuantity = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);
                            invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() + convertedQuantity);

                        }

                        // add inventory sale distributions

                        if (adBranchItemLocation != null) {

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), "REVENUE", EJBCommon.FALSE, arInvoiceLineItem.getIliAmount(), adBranchItemLocation.getBilCoaGlSalesAccount(), arInvoice, branchCode, companyCode);

                        } else {

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), "REVENUE", EJBCommon.FALSE, arInvoiceLineItem.getIliAmount(), arInvoiceLineItem.getInvItemLocation().getIlGlCoaSalesAccount(), arInvoice, branchCode, companyCode);

                        }

                        TOTAL_TAX += arInvoiceLineItem.getIliTaxAmount();

                    }

                    // add tax distribution if necessary

                    if (!arInvoice.getArTaxCode().getTcType().equals("NONE") && !arInvoice.getArTaxCode().getTcType().equals("EXEMPT")) {

                        if (adCompany.getCmpShortName().toLowerCase().equals("tinderbox")) {
                            String brTaxCOA = this.getBranchTaxCOA(adBranch.getBrBranchCode(), arInvoice.getArTaxCode().getGlChartOfAccount().getCoaSegment1(), arInvoice.getArTaxCode().getGlChartOfAccount().getCoaSegment3(), arInvoice.getArTaxCode().getGlChartOfAccount().getCoaSegment4(), arInvoice.getArTaxCode().getGlChartOfAccount().getCoaSegment5());

                            this.addArDrEntry(arInvoice.getArDrNextLine(), "TAX", EJBCommon.FALSE, TOTAL_TAX, glChartOfAccountHome.findByCoaAccountNumber(brTaxCOA, companyCode).getCoaCode(), EJBCommon.FALSE, arInvoice, branchCode, companyCode);
                        } else {

                            this.addArDrEntry(arInvoice.getArDrNextLine(), "TAX", EJBCommon.FALSE, TOTAL_TAX, arInvoice.getArTaxCode().getGlChartOfAccount().getCoaCode(), EJBCommon.FALSE, arInvoice, branchCode, companyCode);

                        }


                    }

                    // add cash distribution

                    LocalAdBranchCustomer adBranchCustomer = null;

                    try {

                        adBranchCustomer = adBranchCustomerHome.findBcstByCstCodeAndBrCode(arInvoice.getArCustomer().getCstCode(), branchCode, companyCode);

                    }
                    catch (FinderException ex) {

                    }

                    if (adBranchCustomer != null) {

                        this.addArDrEntry(arInvoice.getArDrNextLine(), "RECEIVABLE", EJBCommon.TRUE, arInvoice.getInvAmountDue(), adBranchCustomer.getBcstGlCoaReceivableAccount(), EJBCommon.FALSE, arInvoice, branchCode, companyCode);

                    } else {
                        this.addArDrEntry(arInvoice.getArDrNextLine(), "RECEIVABLE", EJBCommon.TRUE, arInvoice.getInvAmountDue(), arInvoice.getArCustomer().getCstGlCoaReceivableAccount(), EJBCommon.FALSE, arInvoice, branchCode, companyCode);
                    }


                    if (arModUploadReceiptDetails.getRctPosDiscount() != 0) {
                        // add discount
                        this.addArDrEntry(arInvoice.getArDrNextLine(), "DISCOUNT", EJBCommon.TRUE, arModUploadReceiptDetails.getRctPosDiscount(), adPreference.getPrfMiscPosDiscountAccount(), EJBCommon.FALSE, arInvoice, branchCode, companyCode);
                    }

                    if (arModUploadReceiptDetails.getRctPosScAmount() != 0) {
                        // add sc amount
                        this.addArDrEntry(arInvoice.getArDrNextLine(), "SERVCE CHARGE", EJBCommon.FALSE, arModUploadReceiptDetails.getRctPosScAmount(), adPreference.getPrfMiscPosServiceChargeAccount(), EJBCommon.FALSE, arInvoice, branchCode, companyCode);
                    }

                    if (arModUploadReceiptDetails.getRctPosDcAmount() != 0) {
                        // add dc amount
                        this.addArDrEntry(arInvoice.getArDrNextLine(), "DINEIN CHARGE", EJBCommon.FALSE, arModUploadReceiptDetails.getRctPosDcAmount(), adPreference.getPrfMiscPosDineInChargeAccount(), EJBCommon.FALSE, arInvoice, branchCode, companyCode);
                    }


                    // add forex gain/loss

                    double forexGainLoss = TOTAL_LINE - (arInvoice.getInvAmountDue() + arModUploadReceiptDetails.getRctPosDiscount() - arModUploadReceiptDetails.getRctPosScAmount() - arModUploadReceiptDetails.getRctPosDcAmount());
                    if (forexGainLoss != 0) {
                        this.addArDrEntry(arInvoice.getArDrNextLine(), "FOREX", forexGainLoss > 0 ? EJBCommon.TRUE : EJBCommon.FALSE, Math.abs(forexGainLoss), adPreference.getPrfMiscPosGiftCertificateAccount(), EJBCommon.FALSE, arInvoice, branchCode, companyCode);
                    }

                }

                success = 1;
                return success;
            } else {

                System.out.println("NULL ACCOUNTs");
                System.out.println(success);
                return success;

            }

        }
        catch (Exception ex) {

            ex.printStackTrace();
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }
    }

    @Override
    public int setArMiscReceiptAllNewAndVoidUS(String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier, String companyShortName) {

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            //LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(BR_BRNCH_CODE, companyCode);
            LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(branchCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            Integer AD_BRNCH = adBranch.getBrCode();
            String BR_BRNCH_CODE = adBranch.getBrBranchCode();

            int success = 0;

            if (adPreference.getPrfMiscPosDiscountAccount() != null && adPreference.getPrfMiscPosGiftCertificateAccount() != null &&
                    adPreference.getPrfMiscPosServiceChargeAccount() != null && adPreference.getPrfMiscPosDineInChargeAccount() != null) {

                // new receipts

                HashMap receiptNumbers = new HashMap();
                HashMap invoiceNumbers = new HashMap();
                String fundTransferNumber = null;
                String firstNumber = "";
                String lastNumber = "";

                double salesTax = 0;
                double preparedTax = 0;
                double salesTax2 = 0;
                double preparedTax2 = 0;

                double salesTaxInvoice2 = 0;
                double preparedTaxInvoice2 = 0;

                for (int i = 0; i < receipts.length; i++) {

                    ArModReceiptDetails arModReceiptDetails = receiptDecodeUS(receipts[i]);

                    if (i == 0) {
                        firstNumber = arModReceiptDetails.getRctNumber();
                    }
                    if (i == receipts.length - 1) {
                        lastNumber = arModReceiptDetails.getRctNumber();
                    }

                    if (arModReceiptDetails.getRctPosOnAccount() == 0) {

                        // generate receipt number

                        String generatedReceipt = null;

                        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
                        LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;

                        try {

                            adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AR RECEIPT", companyCode);

                        }
                        catch (FinderException ex) {

                        }

                        try {

                            adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, companyCode);

                        }
                        catch (FinderException ex) {

                        }

                        while (true) {

                            if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                                try {

                                    arReceiptHome.findByRctNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, companyCode);
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                                }
                                catch (FinderException ex) {

                                    generatedReceipt = adDocumentSequenceAssignment.getDsaNextSequence();
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                    break;

                                }

                            } else {

                                try {

                                    arReceiptHome.findByRctNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, companyCode);
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                                }
                                catch (FinderException ex) {

                                    generatedReceipt = adBranchDocumentSequenceAssignment.getBdsNextSequence();
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                    break;

                                }

                            }

                        }
                        LocalArReceipt arExistingReceipt = null;

                        Iterator rctIter = receiptNumbers.values().iterator();
                        while (rctIter.hasNext()) {

                            try {

                                ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) rctIter.next();

                                arExistingReceipt = arReceiptHome.findByRctDateAndRctNumberAndCstCustomerCodeAndBrCode(arModReceiptDetails.getRctDate(),
                                        arModUploadReceiptDetails.getRctNumber(), arModReceiptDetails.getRctCstCustomerCode(), AD_BRNCH, companyCode);

                                break;

                            }
                            catch (FinderException ex) {

                                continue;

                            }

                        }

                        if (arExistingReceipt == null) {

                            ArModReceiptDetails arModUploadReceiptDetails = new ArModReceiptDetails();
                            arModUploadReceiptDetails.setRctNumber(generatedReceipt);
                            arModUploadReceiptDetails.setRctPosDiscount(EJBCommon.roundIt(arModReceiptDetails.getRctPosDiscount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosScAmount(EJBCommon.roundIt(arModReceiptDetails.getRctPosScAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosDcAmount(EJBCommon.roundIt(arModReceiptDetails.getRctPosDcAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            receiptNumbers.put(generatedReceipt, arModUploadReceiptDetails);

                            double totalAmount = EJBCommon.roundIt((arModReceiptDetails.getRctPosTotalAmount() - arModReceiptDetails.getRctPosVoidAmount()) * arModReceiptDetails.getRctConversionRate(), (short) 2);

                            LocalArReceipt arReceipt = arReceiptHome.create("MISC",
                                    "POS Sales " + new Date(), arModReceiptDetails.getRctDate(), generatedReceipt, null, null, null,
                                    null, null, null, null, null,
                                    totalAmount, 0d, 0d, 0d, 0d, 0d, 0d,
                                    null, 1, null, "CASH", EJBCommon.FALSE,
                                    0, null, null, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE,
                                    EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE,
                                    null, null, null, null, null,
                                    cashier, EJBCommon.getGcCurrentDateWoTime().getTime(),
                                    cashier, EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, null, null,
                                    EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, null, null,
                                    EJBCommon.FALSE, EJBCommon.FALSE, null,
                                    AD_BRNCH, companyCode
                            );

                            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                            //glFunctionalCurrency.addArReceipt(arReceipt);
                            arReceipt.setGlFunctionalCurrency(glFunctionalCurrency);

                            System.out.println("arModReceiptDetails.getRctCstCustomerCode(): " + arModReceiptDetails.getRctCstCustomerCode());
                            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(arModReceiptDetails.getRctCstCustomerCode(), companyCode);
                            //arCustomer.addArReceipt(arReceipt);
                            arReceipt.setArCustomer(arCustomer);

                            String bankAccount = "";
                            if (BR_BRNCH_CODE.equals("Manila-Smoking Lounge")) {
                                bankAccount = "Allied Bank-IPT Smoking";
                            } else if (BR_BRNCH_CODE.equals("Manila-Cigar Shop")) {
                                bankAccount = "Allied Bank Cigar shop";
                            } else if (BR_BRNCH_CODE.equals("Manila-Term.#2 Domestic")) {
                                bankAccount = "Terminal II Domestic";
                            } else if (BR_BRNCH_CODE.equals("Manila-Term.#2 Intl")) {
                                bankAccount = "Term2 International";
                            } else if (BR_BRNCH_CODE.equals("Cebu-Banilad")) {
                                bankAccount = "Metrobank Banilad";
                            } else if (BR_BRNCH_CODE.equals("Cebu-Gorordo")) {
                                bankAccount = "Metrobank-Gorordo";
                            } else if (BR_BRNCH_CODE.equals("Cebu-Mactan Domestic")) {
                                bankAccount = "Metrobank Mactan Domestic";
                            } else if (BR_BRNCH_CODE.equals("Cebu-Mactan Intl")) {
                                bankAccount = "Metrobank I Mactan Int'l";
                            } else if (BR_BRNCH_CODE.equals("Cebu-Supercat")) {
                                bankAccount = "Metrobank Supercat";
                            } else {
                                bankAccount = arCustomer.getAdBankAccount().getBaName();
                            }

                            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(bankAccount, companyCode);
                            //adBankAccount.addArReceipt(arReceipt);
                            arReceipt.setAdBankAccount(adBankAccount);

                            boolean menuItem = true;
                            System.out.println("arModReceiptDetails.getRctSource(): " + arModReceiptDetails.getRctSource());
                            if (arModReceiptDetails.getRctSource().trim().equalsIgnoreCase("R")) {
                                System.out.println("****");
                                LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INCLUSIVE 7%", companyCode);
                                //arTaxCode.addArReceipt(arReceipt);
                                arReceipt.setArTaxCode(arTaxCode);
                            } else {
                                System.out.println("-------");
                                LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INCLUSIVE", companyCode);
                                //arTaxCode.addArReceipt(arReceipt);
                                arReceipt.setArTaxCode(arTaxCode);
                            }

                            LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName("NONE", companyCode);
                            //arWithholdingTaxCode.addArReceipt(arReceipt);
                            arReceipt.setArWithholdingTaxCode(arWithholdingTaxCode);

                            LocalArReceiptBatch arReceiptBatch = null;
                            try {
                                arReceiptBatch = arReceiptBatchHome.findByRbName("POS BATCH", AD_BRNCH, companyCode);
                            }
                            catch (FinderException ex) {
                                arReceiptBatch = arReceiptBatchHome.create("POS BATCH", "POS BATCH", "OPEN", "MISC", EJBCommon.getGcCurrentDateWoTime().getTime(), cashier, AD_BRNCH, companyCode);

                            }
                            //arReceiptBatch.addArReceipt(arReceipt);
                            arReceipt.setArReceiptBatch(arReceiptBatch);

                            Iterator iter = arModReceiptDetails.getInvIliList().iterator();
                            short lineNumber = 0;

                            while (iter.hasNext()) {

                                ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = (ArModInvoiceLineItemDetails) iter.next();
                                double taxRate = 0;
                                System.out.println("arModInvoiceLineItemDetails.getIliTaxCode(): " + arModInvoiceLineItemDetails.getIliTaxCode());
                                if (arModInvoiceLineItemDetails.getIliTaxCode().contains("Prepared Food Tax")) {
                                    taxRate = 7;
                                    System.out.println("taxRate: " + taxRate);
                                    System.out.println("Formula: " + (1 + taxRate / 100));
                                    System.out.println("arModInvoiceLineItemDetails.getIliAmount(): " + arModInvoiceLineItemDetails.getIliAmount());
                                    preparedTax = preparedTax + (arModInvoiceLineItemDetails.getIliAmount() * (1 + taxRate / 100)) - EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount(), (short) 2);
                                    System.out.println("preparedTax: " + preparedTax);
                                } else if (arModInvoiceLineItemDetails.getIliTaxCode().contains("Sales Tax")) {
                                    taxRate = 5;
                                    salesTax = salesTax + (arModInvoiceLineItemDetails.getIliAmount() * (1 + taxRate / 100)) - EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount(), (short) 2);
                                    System.out.println("salesTax: " + salesTax);
                                } else {
                                    taxRate = arExistingReceipt.getArTaxCode().getTcRate();
                                }

                                System.out.println("taxRate: " + taxRate);
                                LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.create(++lineNumber,
                                        arModInvoiceLineItemDetails.getIliQuantity(), arModInvoiceLineItemDetails.getIliUnitPrice(),
                                        EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() * (1 + taxRate / 100), (short) 2),
                                        (arModInvoiceLineItemDetails.getIliAmount() * (1 + taxRate / 100)) - EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount(), (short) 2),
                                        EJBCommon.FALSE, 0, 0, 0, 0, 0, EJBCommon.TRUE, companyCode);
                                System.out.println("tax: " + EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() - (arModInvoiceLineItemDetails.getIliAmount() / (1 + taxRate / 100)), (short) 2));
                                //arReceipt.addArInvoiceLineItem(arInvoiceLineItem);
                                arInvoiceLineItem.setArReceipt(arReceipt);

                                LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                //invUnitOfMeasure.addArInvoiceLineItem(arInvoiceLineItem);
                                arInvoiceLineItem.setInvUnitOfMeasure(invUnitOfMeasure);
                                if (invUnitOfMeasure.getUomName().contains("MENU")) {
                                    menuItem = true;
                                }
                                LocalInvItem invItem = invItemHome.findByIiName(arModInvoiceLineItemDetails.getIliIiName(), companyCode);
                                LocalInvLocation invLocation = null;
                                System.out.println("Item Default Location! " + invItem.getIiDefaultLocation());
                                System.out.println("2 Item Name! " + invItem.getIiName());
                                System.out.println("3 Item Name! " + arModInvoiceLineItemDetails.getIliIiName());
                                System.out.println("3 Item Name! " + arModInvoiceLineItemDetails.getIliLocName());
                                try {
                                    if (invItem.getIiDefaultLocation() != null) {
                                        invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                                    }
                                }
                                catch (FinderException ex) {
                                }

                                //System.out.println("arModInvoiceLineItemDetails.getIliLocName(): " + arModInvoiceLineItemDetails.getIliLocName());
                                //System.out.println("invLocation.getLocName(): " + invLocation.getLocName());
                                LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(arModInvoiceLineItemDetails.getIliIiName(),
                                        invLocation == null ? arModInvoiceLineItemDetails.getIliLocName() : invLocation.getLocName(), companyCode);
                                //invItemLocation.addArInvoiceLineItem(arInvoiceLineItem);
                                arInvoiceLineItem.setInvItemLocation(invItemLocation);

                                if (invItemLocation.getInvItem().getIiEnableAutoBuild() == EJBCommon.TRUE) {

                                    arInvoiceLineItem.setIliEnableAutoBuild(EJBCommon.TRUE);

                                }
                            }


                        } else {

                            ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) receiptNumbers.get(arExistingReceipt.getRctNumber());

                            arModUploadReceiptDetails.setRctPosDiscount(arModUploadReceiptDetails.getRctPosDiscount() + EJBCommon.roundIt(arModReceiptDetails.getRctPosDiscount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosScAmount(arModUploadReceiptDetails.getRctPosScAmount() + EJBCommon.roundIt(arModReceiptDetails.getRctPosScAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosDcAmount(arModUploadReceiptDetails.getRctPosDcAmount() + EJBCommon.roundIt(arModReceiptDetails.getRctPosDcAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            receiptNumbers.put(arExistingReceipt.getRctNumber(), arModUploadReceiptDetails);

                            double totalAmount = EJBCommon.roundIt((arModReceiptDetails.getRctPosTotalAmount() - arModReceiptDetails.getRctPosVoidAmount()) * arModReceiptDetails.getRctConversionRate(), (short) 2);
                            arExistingReceipt.setRctAmount(arExistingReceipt.getRctAmount() + totalAmount);

                            Iterator iter = arModReceiptDetails.getInvIliList().iterator();


                            while (iter.hasNext()) {

                                ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = (ArModInvoiceLineItemDetails) iter.next();
                                System.out.println(arModInvoiceLineItemDetails.getIliIiName());
                                LocalInvItem invItem = invItemHome.findByIiName(arModInvoiceLineItemDetails.getIliIiName(), companyCode);
                                LocalInvLocation invLocation = null;
                                try {
                                    if (invItem.getIiDefaultLocation() != null) {
                                        invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                                    }
                                }
                                catch (FinderException ex) {
                                }
                                LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(arModInvoiceLineItemDetails.getIliIiName(),
                                        invLocation == null ? arModInvoiceLineItemDetails.getIliLocName() : invLocation.getLocName(), companyCode);

                                LocalArInvoiceLineItem arExistingInvoiceLineItem = null;

                                try {
                                    arExistingInvoiceLineItem = arInvoiceLineItemHome.findByRctCodeAndIlCodeAndUomName(arExistingReceipt.getRctCode(),
                                            invItemLocation.getIlCode(), arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                }
                                catch (FinderException ex) {

                                }

                                if (arExistingInvoiceLineItem == null) {
                                    System.out.println(arModInvoiceLineItemDetails.getIliIiName());
                                    double taxRate = 0;
                                    if (arModInvoiceLineItemDetails.getIliTaxCode().contains("Prepared Food Tax")) {
                                        taxRate = 7;
                                        preparedTax = preparedTax + (arModInvoiceLineItemDetails.getIliAmount() * (1 + taxRate / 100)) - EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount(), (short) 2);
                                    } else if (arModInvoiceLineItemDetails.getIliTaxCode().contains("Sales Tax")) {
                                        taxRate = 5;
                                        salesTax = salesTax + (arModInvoiceLineItemDetails.getIliAmount() * (1 + taxRate / 100)) - EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount(), (short) 2);
                                        System.out.println("salesTax: " + salesTax);
                                    } else {
                                        taxRate = arExistingReceipt.getArTaxCode().getTcRate();
                                    }
                                    LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.create((short) (arExistingReceipt.getArInvoiceLineItems().size() + 1),
                                            arModInvoiceLineItemDetails.getIliQuantity(), arModInvoiceLineItemDetails.getIliUnitPrice(),
                                            EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() * (1 + taxRate / 100), (short) 2),
                                            (arModInvoiceLineItemDetails.getIliAmount() * (1 + taxRate / 100)) - EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount(), (short) 2),
                                            EJBCommon.FALSE, 0, 0, 0, 0, 0, EJBCommon.TRUE, companyCode);

                                    //arExistingReceipt.addArInvoiceLineItem(arInvoiceLineItem);
                                    arInvoiceLineItem.setArReceipt(arExistingReceipt);

                                    LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                    //invUnitOfMeasure.addArInvoiceLineItem(arInvoiceLineItem);
                                    arInvoiceLineItem.setInvUnitOfMeasure(invUnitOfMeasure);

                                    //invItemLocation.addArInvoiceLineItem(arInvoiceLineItem);
                                    arInvoiceLineItem.setInvItemLocation(invItemLocation);
                                    if (invItemLocation.getInvItem().getIiEnableAutoBuild() == EJBCommon.TRUE) {

                                        arInvoiceLineItem.setIliEnableAutoBuild(EJBCommon.TRUE);

                                    }
                                } else {

                                    arExistingInvoiceLineItem.setIliQuantity(arExistingInvoiceLineItem.getIliQuantity() + arModInvoiceLineItemDetails.getIliQuantity());
                                    arExistingInvoiceLineItem.setIliUnitPrice((arExistingInvoiceLineItem.getIliUnitPrice() + arModInvoiceLineItemDetails.getIliUnitPrice()) / 2);
                                    arExistingInvoiceLineItem.setIliAmount(arExistingInvoiceLineItem.getIliAmount() + EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingReceipt.getArTaxCode().getTcRate() / 100), (short) 2));
                                    arExistingInvoiceLineItem.setIliTaxAmount(arExistingInvoiceLineItem.getIliTaxAmount() + EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() - (arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingReceipt.getArTaxCode().getTcRate() / 100)), (short) 2));

                                }


                            }
                        }

                    } else {

                        // generate Invoice number

                        String generatedInvoice = null;

                        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
                        LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;

                        try {

                            adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AR INVOICE", companyCode);

                        }
                        catch (FinderException ex) {

                        }

                        try {

                            adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, companyCode);

                        }
                        catch (FinderException ex) {

                        }

                        while (true) {

                            if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                                try {

                                    arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), (byte) 0, AD_BRNCH, companyCode);
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                                }
                                catch (FinderException ex) {

                                    generatedInvoice = adDocumentSequenceAssignment.getDsaNextSequence();
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                    break;

                                }

                            } else {

                                try {

                                    arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), (byte) 0, AD_BRNCH, companyCode);
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                                }
                                catch (FinderException ex) {

                                    generatedInvoice = adBranchDocumentSequenceAssignment.getBdsNextSequence();
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                    break;

                                }

                            }

                        }
                        LocalArInvoice arExistingInvoice = null;

                        Iterator invIter = invoiceNumbers.values().iterator();
                        while (invIter.hasNext()) {

                            try {

                                ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) invIter.next();

                                arExistingInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndCstCustomerCode(arModUploadReceiptDetails.getRctNumber(), (byte) 0,
                                        arModReceiptDetails.getRctCstCustomerCode(), companyCode);

                                break;

                            }
                            catch (FinderException ex) {

                                continue;

                            }


                        }

                        if (arExistingInvoice == null) {

                            ArModReceiptDetails arModUploadReceiptDetails = new ArModReceiptDetails();
                            arModUploadReceiptDetails.setRctNumber(generatedInvoice);
                            arModUploadReceiptDetails.setRctPosDiscount(EJBCommon.roundIt(arModReceiptDetails.getRctPosDiscount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosScAmount(EJBCommon.roundIt(arModReceiptDetails.getRctPosScAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosDcAmount(EJBCommon.roundIt(arModReceiptDetails.getRctPosDcAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            invoiceNumbers.put(generatedInvoice, arModUploadReceiptDetails);

                            double totalAmount = EJBCommon.roundIt((arModReceiptDetails.getRctPosTotalAmount() - arModReceiptDetails.getRctPosVoidAmount()) * arModReceiptDetails.getRctConversionRate(), (short) 2);

                            LocalArInvoice arInvoice = arInvoiceHome.create("ITEMS",
                                    (byte) 0, "POS Sales " + new Date().toString(), arModReceiptDetails.getRctDate(), generatedInvoice, null, null,
                                    null, null, totalAmount, 0d, 0d, 0d, 0d, 0d, null, 1, null,
                                    0d, 0d, null, null, null, null, null,
                                    null, null, null, null, null,
                                    null, null, null, null, null,
                                    null, null, null, null, null,
                                    EJBCommon.FALSE,
                                    null, EJBCommon.FALSE, EJBCommon.FALSE,
                                    EJBCommon.FALSE, EJBCommon.FALSE,
                                    EJBCommon.FALSE, null, 0d, null, null, null, null,
                                    cashier, EJBCommon.getGcCurrentDateWoTime().getTime(), cashier,
                                    EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, null, null,
                                    EJBCommon.FALSE, null, null, null, (byte) 0, (byte) 0,
                                    null, arModReceiptDetails.getRctDate(), AD_BRNCH, companyCode
                            );

                            /*
                             * RctPaymentMethod is Used Temporarily for Payment Term Field
                             */
                            if (adCompany.getCmpShortName().toLowerCase().equals("tinderbox") || adCompany.getCmpShortName().toLowerCase().equals("hs")) {
                                LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName("IMMEDIATE", companyCode);
                                //adPaymentTerm.addArInvoice(arInvoice);
                                arInvoice.setAdPaymentTerm(adPaymentTerm);
                            } else {
                                if (arModReceiptDetails.getRctPaymentMethod() == null || arModReceiptDetails.getRctPaymentMethod().equals("")) {
                                    LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName("IMMEDIATE", companyCode);
                                    //adPaymentTerm.addArInvoice(arInvoice);
                                    arInvoice.setAdPaymentTerm(adPaymentTerm);
                                } else {
                                    LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName(arModReceiptDetails.getRctPaymentMethod(), companyCode);
                                    //adPaymentTerm.addArInvoice(arInvoice);
                                    arInvoice.setAdPaymentTerm(adPaymentTerm);
                                }
                            }

                            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                            //glFunctionalCurrency.addArInvoice(arInvoice);
                            arInvoice.setGlFunctionalCurrency(glFunctionalCurrency);

                            System.out.println("POS Customer: " + arModReceiptDetails.getRctCstCustomerCode());
                            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(arModReceiptDetails.getRctCstCustomerCode(), companyCode);
                            //arCustomer.addArInvoice(arInvoice);
                            arInvoice.setArCustomer(arCustomer);

                            if (arModReceiptDetails.getRctSource().trim().equalsIgnoreCase("R")) {
                                if (adCompany.getCmpShortName().toLowerCase().equals("tinderbox") || adCompany.getCmpShortName().toLowerCase().equals("hs")) {
                                    LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INCLUSIVE 7%", companyCode);
                                    //arTaxCode.addArInvoice(arInvoice);
                                    arInvoice.setArTaxCode(arTaxCode);
                                } else {
                                    try {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName(arModReceiptDetails.getRctTcName(), companyCode);
                                        //arTaxCode.addArInvoice(arInvoice);
                                        arInvoice.setArTaxCode(arTaxCode);
                                    }
                                    catch (Exception ex) {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INCLUSIVE 7%", companyCode);
                                        //arTaxCode.addArInvoice(arInvoice);
                                        arInvoice.setArTaxCode(arTaxCode);
                                    }
                                }
                            } else {
                                if (adCompany.getCmpShortName().toLowerCase().equals("tinderbox") || adCompany.getCmpShortName().toLowerCase().equals("hs")) {
                                    LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INCLUSIVE", companyCode);
                                    //arTaxCode.addArInvoice(arInvoice);
                                    arInvoice.setArTaxCode(arTaxCode);
                                } else {
                                    try {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName(arModReceiptDetails.getRctTcName(), companyCode);
                                        //arTaxCode.addArInvoice(arInvoice);
                                        arInvoice.setArTaxCode(arTaxCode);
                                    }
                                    catch (Exception ex) {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INCLUSIVE", companyCode);
                                        //arTaxCode.addArInvoice(arInvoice);
                                        arInvoice.setArTaxCode(arTaxCode);
                                    }
                                }
                            }

                            LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName("NONE", companyCode);
                            //arWithholdingTaxCode.addArInvoice(arInvoice);
                            arInvoice.setArWithholdingTaxCode(arWithholdingTaxCode);

                            LocalArInvoiceBatch arInvoiceBatch = null;
                            try {
                                arInvoiceBatch = arInvoiceBatchHome.findByIbName("POS BATCH", AD_BRNCH, companyCode);
                            }
                            catch (FinderException ex) {
                                arInvoiceBatch = arInvoiceBatchHome.create("POS BATCH", "POS BATCH", "OPEN", "MISC", EJBCommon.getGcCurrentDateWoTime().getTime(), cashier, AD_BRNCH, companyCode);

                            }
                            //arInvoiceBatch.addArInvoice(arInvoice);
                            arInvoice.setArInvoiceBatch(arInvoiceBatch);


                            Iterator iter = arModReceiptDetails.getInvIliList().iterator();
                            short lineNumber = 0;
                            boolean menuItem = false;
                            while (iter.hasNext()) {

                                ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = (ArModInvoiceLineItemDetails) iter.next();

                                double taxRate = 0;
                                if (arModInvoiceLineItemDetails.getIliTaxCode().contains("Prepared Food Tax")) {
                                    taxRate = 7;
                                    preparedTax = preparedTax + (arModInvoiceLineItemDetails.getIliAmount() * (1 + taxRate / 100)) - EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount(), (short) 2);
                                } else if (arModInvoiceLineItemDetails.getIliTaxCode().contains("Sales Tax")) {
                                    taxRate = 5;
                                    salesTax = salesTax + (arModInvoiceLineItemDetails.getIliAmount() * (1 + taxRate / 100)) - EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount(), (short) 2);
                                    System.out.println("salesTax: " + salesTax);
                                } else {
                                    taxRate = arExistingInvoice.getArTaxCode().getTcRate();
                                }

                                LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.create(++lineNumber,
                                        arModInvoiceLineItemDetails.getIliQuantity(), arModInvoiceLineItemDetails.getIliUnitPrice(),
                                        EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() * (1 + taxRate / 100), (short) 2),
                                        (arModInvoiceLineItemDetails.getIliAmount() * (1 + taxRate / 100)) - EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount(), (short) 2),
                                        EJBCommon.FALSE, 0, 0, 0, 0, 0, EJBCommon.TRUE, companyCode);

                                //arInvoice.addArInvoiceLineItem(arInvoiceLineItem);
                                arInvoiceLineItem.setArInvoice(arInvoice);

                                LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                //invUnitOfMeasure.addArInvoiceLineItem(arInvoiceLineItem);
                                arInvoiceLineItem.setInvUnitOfMeasure(invUnitOfMeasure);

                                if (invUnitOfMeasure.getUomName().contains("MENU")) {
                                    menuItem = true;
                                }

                                LocalInvItem invItem = invItemHome.findByIiName(arModInvoiceLineItemDetails.getIliIiName(), companyCode);
                                LocalInvLocation invLocation = null;
                                try {
                                    if (invItem.getIiDefaultLocation() != null) {
                                        invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                                    }
                                }
                                catch (FinderException ex) {
                                }
                                LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(arModInvoiceLineItemDetails.getIliIiName(),
                                        invLocation == null ? arModInvoiceLineItemDetails.getIliLocName() : invLocation.getLocName(), companyCode);
                                //invItemLocation.addArInvoiceLineItem(arInvoiceLineItem);
                                arInvoiceLineItem.setInvItemLocation(invItemLocation);
                                if (invItemLocation.getInvItem().getIiEnableAutoBuild() == EJBCommon.TRUE) {

                                    arInvoiceLineItem.setIliEnableAutoBuild(EJBCommon.TRUE);

                                }
                            }


                        } else {

                            ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) invoiceNumbers.get(arExistingInvoice.getInvNumber());

                            arModUploadReceiptDetails.setRctPosDiscount(arModUploadReceiptDetails.getRctPosDiscount() + EJBCommon.roundIt(arModReceiptDetails.getRctPosDiscount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosScAmount(arModUploadReceiptDetails.getRctPosScAmount() + EJBCommon.roundIt(arModReceiptDetails.getRctPosScAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosDcAmount(arModUploadReceiptDetails.getRctPosDcAmount() + EJBCommon.roundIt(arModReceiptDetails.getRctPosDcAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            invoiceNumbers.put(arExistingInvoice.getInvNumber(), arModUploadReceiptDetails);

                            double totalAmount = EJBCommon.roundIt((arModReceiptDetails.getRctPosTotalAmount() - arModReceiptDetails.getRctPosVoidAmount()) * arModReceiptDetails.getRctConversionRate(), (short) 2);
                            arExistingInvoice.setInvAmountDue(arExistingInvoice.getInvAmountDue() + totalAmount);

                            Iterator iter = arModReceiptDetails.getInvIliList().iterator();


                            while (iter.hasNext()) {

                                ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = (ArModInvoiceLineItemDetails) iter.next();

                                LocalInvItem invItem = invItemHome.findByIiName(arModInvoiceLineItemDetails.getIliIiName(), companyCode);
                                LocalInvLocation invLocation = null;
                                try {
                                    if (invItem.getIiDefaultLocation() != null) {
                                        invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                                    }
                                }
                                catch (FinderException ex) {
                                }
                                LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(arModInvoiceLineItemDetails.getIliIiName(),
                                        invLocation == null ? arModInvoiceLineItemDetails.getIliLocName() : invLocation.getLocName(), companyCode);

                                LocalArInvoiceLineItem arExistingInvoiceLineItem = null;

                                try {
                                    arExistingInvoiceLineItem = arInvoiceLineItemHome.findByRctCodeAndIlCodeAndUomName(arExistingInvoice.getInvCode(),
                                            invItemLocation.getIlCode(), arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                }
                                catch (FinderException ex) {

                                }

                                if (arExistingInvoiceLineItem == null) {

                                    double taxRate = 0;
                                    if (arModInvoiceLineItemDetails.getIliTaxCode().contains("Prepared Food Tax")) {
                                        taxRate = 7;
                                        preparedTax = preparedTax + (arModInvoiceLineItemDetails.getIliAmount() * (1 + taxRate / 100)) - EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount(), (short) 2);
                                    } else if (arModInvoiceLineItemDetails.getIliTaxCode().contains("Sales Tax")) {
                                        taxRate = 5;
                                        salesTax = salesTax + (arModInvoiceLineItemDetails.getIliAmount() * (1 + taxRate / 100)) - EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount(), (short) 2);
                                        System.out.println("salesTax: " + salesTax);
                                    } else {
                                        taxRate = arExistingInvoice.getArTaxCode().getTcRate();
                                    }


                                    LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.create((short) (arExistingInvoice.getArInvoiceLineItems().size() + 1),
                                            arModInvoiceLineItemDetails.getIliQuantity(), arModInvoiceLineItemDetails.getIliUnitPrice(),
                                            EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() * (1 + taxRate / 100), (short) 2),
                                            (arModInvoiceLineItemDetails.getIliAmount() * (1 + taxRate / 100)) - EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount(), (short) 2),
                                            EJBCommon.FALSE, 0, 0, 0, 0, 0, EJBCommon.TRUE, companyCode);

                                    //arExistingInvoice.addArInvoiceLineItem(arInvoiceLineItem);
                                    arInvoiceLineItem.setArInvoice(arExistingInvoice);

                                    LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                    //invUnitOfMeasure.addArInvoiceLineItem(arInvoiceLineItem);
                                    arInvoiceLineItem.setInvUnitOfMeasure(invUnitOfMeasure);

                                    //invItemLocation.addArInvoiceLineItem(arInvoiceLineItem);
                                    arInvoiceLineItem.setInvItemLocation(invItemLocation);
                                    if (invItemLocation.getInvItem().getIiEnableAutoBuild() == EJBCommon.TRUE) {

                                        arInvoiceLineItem.setIliEnableAutoBuild(EJBCommon.TRUE);

                                    }
                                } else {

                                    arExistingInvoiceLineItem.setIliQuantity(arExistingInvoiceLineItem.getIliQuantity() + arModInvoiceLineItemDetails.getIliQuantity());
                                    arExistingInvoiceLineItem.setIliUnitPrice((arExistingInvoiceLineItem.getIliUnitPrice() + arModInvoiceLineItemDetails.getIliUnitPrice()) / 2);
                                    arExistingInvoiceLineItem.setIliAmount(arExistingInvoiceLineItem.getIliAmount() + EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingInvoice.getArTaxCode().getTcRate() / 100), (short) 2));
                                    arExistingInvoiceLineItem.setIliTaxAmount(arExistingInvoiceLineItem.getIliTaxAmount() + EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() - (arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingInvoice.getArTaxCode().getTcRate() / 100)), (short) 2));

                                }
                            }
                        }

                    }

                }

                // for each receipt generate distribution records

                Iterator rctIter = receiptNumbers.values().iterator();

                while (rctIter.hasNext()) {

                    ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) rctIter.next();

                    LocalArReceipt arReceipt = arReceiptHome.findByRctNumberAndBrCode(arModUploadReceiptDetails.getRctNumber(), AD_BRNCH, companyCode);
                    arReceipt.setRctReferenceNumber(firstNumber + "-" + lastNumber);

                    Iterator lineIter = arReceipt.getArInvoiceLineItems().iterator();

                    double TOTAL_TAX = 0;
                    double TOTAL_LINE = 0;

                    while (lineIter.hasNext()) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) lineIter.next();

                        TOTAL_LINE += arInvoiceLineItem.getIliTaxAmount() + arInvoiceLineItem.getIliAmount();

                        LocalInvItemLocation invItemLocation = arInvoiceLineItem.getInvItemLocation();

                        // add cost of sales distribution and inventory

                        double COST = 0d;

                        try {

                            LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                    arReceipt.getRctDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);

                            COST = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                        }
                        catch (FinderException ex) {

                            COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                        }

                        double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(),
                                arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                        LocalAdBranchItemLocation adBranchItemLocation = null;

                        try {

                            adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arInvoiceLineItem.getInvItemLocation().getIlCode(), AD_BRNCH, companyCode);

                        }
                        catch (FinderException ex) {

                        }

                        if (arInvoiceLineItem.getIliEnableAutoBuild() == 0 || arInvoiceLineItem.getIliEnableAutoBuild() == 1 || arInvoiceLineItem.getInvItemLocation().getInvItem().getIiClass().equals("Stock")) {

                            if (adBranchItemLocation != null) {

                                this.addArDrIliEntry(arReceipt.getArDrNextLine(),
                                        "COGS", EJBCommon.TRUE, COST * QTY_SLD,
                                        adBranchItemLocation.getBilCoaGlCostOfSalesAccount(), arReceipt, AD_BRNCH, companyCode);

                                this.addArDrIliEntry(arReceipt.getArDrNextLine(),
                                        "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD,
                                        adBranchItemLocation.getBilCoaGlInventoryAccount(), arReceipt, AD_BRNCH, companyCode);

                            } else {

                                this.addArDrIliEntry(arReceipt.getArDrNextLine(),
                                        "COGS", EJBCommon.TRUE, COST * QTY_SLD,
                                        arInvoiceLineItem.getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arReceipt, AD_BRNCH, companyCode);

                                this.addArDrIliEntry(arReceipt.getArDrNextLine(),
                                        "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD,
                                        arInvoiceLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(), arReceipt, AD_BRNCH, companyCode);

                            }

                            // add quantity to item location committed quantity

                            double convertedQuantity = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);
                            invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() + convertedQuantity);

                        }

                        // add inventory sale distributions

                        if (adBranchItemLocation != null) {

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(),
                                    "REVENUE", EJBCommon.FALSE, arInvoiceLineItem.getIliAmount(),
                                    adBranchItemLocation.getBilCoaGlSalesAccount(), arReceipt, AD_BRNCH, companyCode);

                        } else {

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(),
                                    "REVENUE", EJBCommon.FALSE, arInvoiceLineItem.getIliAmount(),
                                    arInvoiceLineItem.getInvItemLocation().getIlGlCoaSalesAccount(), arReceipt, AD_BRNCH, companyCode);

                        }

                        TOTAL_TAX += arInvoiceLineItem.getIliTaxAmount();

                        if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiTaxCode().contains("Prepared Food Tax")) {
                            preparedTax2 = preparedTax2 + arInvoiceLineItem.getIliTaxAmount();
                        }

                        if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiTaxCode().contains("Sales Tax")) {
                            salesTax2 = salesTax2 + arInvoiceLineItem.getIliTaxAmount();
                        }

                    }

                    // add tax distribution if necessary
                    System.out.println("preparedTax2: " + preparedTax2);
                    System.out.println("preparedTax: " + preparedTax);
                    System.out.println("salesTax2: " + salesTax2);
                    if (!arReceipt.getArTaxCode().getTcType().equals("NONE") &&
                            !arReceipt.getArTaxCode().getTcType().equals("EXEMPT")) {

                        if (adCompany.getCmpShortName().toLowerCase().equals("tinderbox")) {
                            String brTaxCOA = this.getBranchTaxCOA(BR_BRNCH_CODE,
                                    arReceipt.getArTaxCode().getGlChartOfAccount().getCoaSegment1(),
                                    arReceipt.getArTaxCode().getGlChartOfAccount().getCoaSegment3(),
                                    arReceipt.getArTaxCode().getGlChartOfAccount().getCoaSegment4(),
                                    arReceipt.getArTaxCode().getGlChartOfAccount().getCoaSegment5());
                            /*****Dogdog*****/
                            System.out.println("ADD TAX: " + preparedTax);

                            System.out.println("Prep and Sales Tax");

                            LocalArTaxCode arTaxCodePreparedFoodTax = null;
                            if (preparedTax == 0 && salesTax == 0) {
                                System.out.println("Normal");
                                this.addArDrEntry(arReceipt.getArDrNextLine(),
                                        "TAX", EJBCommon.FALSE, TOTAL_TAX, arReceipt.getArTaxCode().getGlChartOfAccount().getCoaCode(),
                                        EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                            } else {
                                if (preparedTax != 0) {
                                    try {

                                        arTaxCodePreparedFoodTax = arTaxCodeHome.findByTcName("Prepared Food Tax", companyCode);

                                    }
                                    catch (FinderException ex) {

                                    }

                                    if (arTaxCodePreparedFoodTax != null) {
                                        this.addArDrEntry(arReceipt.getArDrNextLine(),
                                                "TAX", EJBCommon.FALSE, preparedTax2, arTaxCodePreparedFoodTax.getGlChartOfAccount().getCoaCode(),
                                                EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                                    }

                                }
                                System.out.println("ADD TAX salesTax: " + salesTax);
                                System.out.println("ADD TAX salesTax2: " + salesTax2);
                                LocalArTaxCode arTaxCodeSalesTax = null;
                                if (salesTax2 != 0) {
                                    try {

                                        arTaxCodeSalesTax = arTaxCodeHome.findByTcName("Sales Tax", companyCode);
                                        System.out.println("Find Sales Tax");

                                    }
                                    catch (FinderException ex) {

                                    }

                                    if (arTaxCodeSalesTax != null) {
                                        System.out.println("Create Sales Tax");
                                        this.addArDrEntry(arReceipt.getArDrNextLine(),
                                                "TAX", EJBCommon.FALSE, salesTax2, arTaxCodeSalesTax.getGlChartOfAccount().getCoaCode(),
                                                EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                                    }

                                }
                            }


							/*else{
								System.out.println("Normal");
								this.addArDrEntry(arReceipt.getArDrNextLine(),
										"TAX", EJBCommon.FALSE, TOTAL_TAX, arReceipt.getArTaxCode().getGlChartOfAccount().getCoaCode(),
										EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
							}*/

                        } else {
                            if (preparedTax == 0 && salesTax == 0) {
                                System.out.println("Normal 2");
                                this.addArDrEntry(arReceipt.getArDrNextLine(),
                                        "TAX", EJBCommon.FALSE, TOTAL_TAX, arReceipt.getArTaxCode().getGlChartOfAccount().getCoaCode(),
                                        EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

                            } else {
                                System.out.println("Prep and Sales Tax");
                                LocalArTaxCode arTaxCodePreparedFoodTax = null;
                                if (preparedTax != 0) {
                                    try {

                                        arTaxCodePreparedFoodTax = arTaxCodeHome.findByTcName("Prepared Food Tax", companyCode);

                                    }
                                    catch (FinderException ex) {

                                    }

                                    if (arTaxCodePreparedFoodTax != null) {
                                        this.addArDrEntry(arReceipt.getArDrNextLine(),
                                                "TAX", EJBCommon.FALSE, preparedTax2, arTaxCodePreparedFoodTax.getGlChartOfAccount().getCoaCode(),
                                                EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                                    }

                                }
                                System.out.println("ADD TAX salesTax: " + salesTax);
                                System.out.println("ADD TAX salesTax2: " + salesTax2);
                                LocalArTaxCode arTaxCodeSalesTax = null;
                                if (salesTax != 0) {
                                    try {

                                        arTaxCodeSalesTax = arTaxCodeHome.findByTcName("Sales Tax", companyCode);
                                        System.out.println("Find Sales Tax: " + salesTax2);
                                    }
                                    catch (FinderException ex) {

                                    }

                                    if (arTaxCodeSalesTax != null) {
                                        this.addArDrEntry(arReceipt.getArDrNextLine(),
                                                "TAX", EJBCommon.FALSE, salesTax2, arTaxCodeSalesTax.getGlChartOfAccount().getCoaCode(),
                                                EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                                    }

                                }
                            }

							/*
							this.addArDrEntry(arReceipt.getArDrNextLine(),
									"TAX", EJBCommon.FALSE, TOTAL_TAX, arReceipt.getArTaxCode().getGlChartOfAccount().getCoaCode(),
									EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);*/

                        }

                    }

                    // add cash distribution

                    this.addArDrEntry(arReceipt.getArDrNextLine(), "CASH",
                            EJBCommon.TRUE, arReceipt.getRctAmount(),
                            arReceipt.getAdBankAccount().getBaCoaGlCashAccount(),
                            EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

                    if (arModUploadReceiptDetails.getRctPosDiscount() != 0) {
                        // add discount
                        this.addArDrEntry(arReceipt.getArDrNextLine(), "DISCOUNT",
                                EJBCommon.TRUE, arModUploadReceiptDetails.getRctPosDiscount(),
                                adPreference.getPrfMiscPosDiscountAccount(),
                                EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                    }

                    if (arModUploadReceiptDetails.getRctPosScAmount() != 0) {
                        // add sc amount
                        this.addArDrEntry(arReceipt.getArDrNextLine(), "SERVCE CHARGE",
                                EJBCommon.FALSE, arModUploadReceiptDetails.getRctPosScAmount(),
                                adPreference.getPrfMiscPosServiceChargeAccount(),
                                EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                    }

                    if (arModUploadReceiptDetails.getRctPosDcAmount() != 0) {
                        // add dc amount
                        this.addArDrEntry(arReceipt.getArDrNextLine(), "DINEIN CHARGE",
                                EJBCommon.FALSE, arModUploadReceiptDetails.getRctPosDcAmount(),
                                adPreference.getPrfMiscPosDineInChargeAccount(),
                                EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                    }


                    // add forex gain/loss

                    double forexGainLoss = TOTAL_LINE - (arReceipt.getRctAmount() + arModUploadReceiptDetails.getRctPosDiscount() - arModUploadReceiptDetails.getRctPosScAmount() - arModUploadReceiptDetails.getRctPosDcAmount());
                    if (forexGainLoss != 0) {
                        this.addArDrEntry(arReceipt.getArDrNextLine(), "FOREX",
                                forexGainLoss > 0 ? EJBCommon.TRUE : EJBCommon.FALSE, Math.abs(forexGainLoss),
                                adPreference.getPrfMiscPosGiftCertificateAccount(),
                                EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                    }

                }

                Iterator invIter = invoiceNumbers.values().iterator();

                while (invIter.hasNext()) {

                    ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) invIter.next();

                    LocalArInvoice arInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arModUploadReceiptDetails.getRctNumber(), (byte) 0, AD_BRNCH, companyCode);
                    arInvoice.setInvReferenceNumber(firstNumber + "-" + lastNumber);

                    short precisionUnit = this.getGlFcPrecisionUnit(companyCode);
                    double TOTAL_PAYMENT_SCHEDULE = 0d;

                    GregorianCalendar gcPrevDateDue = new GregorianCalendar();
                    GregorianCalendar gcDateDue = new GregorianCalendar();
                    gcPrevDateDue.setTime(arInvoice.getInvEffectivityDate());

                    Collection adPaymentSchedules = arInvoice.getAdPaymentTerm().getAdPaymentSchedules();

                    Iterator i = adPaymentSchedules.iterator();

                    while (i.hasNext()) {

                        LocalAdPaymentSchedule adPaymentSchedule = (LocalAdPaymentSchedule) i.next();

                        // get date due

                        if (arInvoice.getAdPaymentTerm().getPytScheduleBasis().equals("DEFAULT")) {

                            gcDateDue.setTime(arInvoice.getInvEffectivityDate());
                            gcDateDue.add(Calendar.DATE, adPaymentSchedule.getPsDueDay());

                        } else if (arInvoice.getAdPaymentTerm().getPytScheduleBasis().equals("MONTHLY")) {

                            gcDateDue = gcPrevDateDue;
                            gcDateDue.add(Calendar.MONTH, 1);
                            gcPrevDateDue = gcDateDue;

                        } else if (arInvoice.getAdPaymentTerm().getPytScheduleBasis().equals("BI-MONTHLY")) {

                            gcDateDue = gcPrevDateDue;

                            if (gcPrevDateDue.get(Calendar.MONTH) != 1) {
                                if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 31 && gcPrevDateDue.get(Calendar.DATE) > 15 && gcPrevDateDue.get(Calendar.DATE) < 31) {
                                    gcDateDue.add(Calendar.DATE, 16);
                                } else {
                                    gcDateDue.add(Calendar.DATE, 15);
                                }
                            } else if (gcPrevDateDue.get(Calendar.MONTH) == 1) {
                                if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 28 && gcPrevDateDue.get(Calendar.DATE) == 14) {
                                    gcDateDue.add(Calendar.DATE, 14);
                                } else if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 28 && gcPrevDateDue.get(Calendar.DATE) >= 15 && gcPrevDateDue.get(Calendar.DATE) < 28) {
                                    gcDateDue.add(Calendar.DATE, 13);
                                } else if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 29 && gcPrevDateDue.get(Calendar.DATE) >= 15 && gcPrevDateDue.get(Calendar.DATE) < 29) {
                                    gcDateDue.add(Calendar.DATE, 14);
                                } else {
                                    gcDateDue.add(Calendar.DATE, 15);
                                }
                            }

                            gcPrevDateDue = gcDateDue;

                        }

                        // create a payment schedule

                        double PAYMENT_SCHEDULE_AMOUNT = 0;

                        // if last payment schedule subtract to avoid rounding difference error

                        if (i.hasNext()) {

                            PAYMENT_SCHEDULE_AMOUNT = EJBCommon.roundIt((adPaymentSchedule.getPsRelativeAmount() / arInvoice.getAdPaymentTerm().getPytBaseAmount()) * arInvoice.getInvAmountDue(), precisionUnit);

                        } else {

                            PAYMENT_SCHEDULE_AMOUNT = arInvoice.getInvAmountDue() - TOTAL_PAYMENT_SCHEDULE;

                        }

                        LocalArInvoicePaymentSchedule arInvoicePaymentSchedule =
                                arInvoicePaymentScheduleHome.create(gcDateDue.getTime(),
                                        adPaymentSchedule.getPsLineNumber(),
                                        PAYMENT_SCHEDULE_AMOUNT,
                                        0d, EJBCommon.FALSE,
                                        (short) 0, gcDateDue.getTime(), 0d, 0d,
                                        companyCode);

                        //arInvoice.addArInvoicePaymentSchedule(arInvoicePaymentSchedule);
                        arInvoicePaymentSchedule.setArInvoice(arInvoice);

                        TOTAL_PAYMENT_SCHEDULE += PAYMENT_SCHEDULE_AMOUNT;

                    }

                    Iterator lineIter = arInvoice.getArInvoiceLineItems().iterator();

                    double TOTAL_TAX = 0;
                    double TOTAL_LINE = 0;

                    while (lineIter.hasNext()) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) lineIter.next();

                        TOTAL_LINE += arInvoiceLineItem.getIliTaxAmount() + arInvoiceLineItem.getIliAmount();

                        LocalInvItemLocation invItemLocation = arInvoiceLineItem.getInvItemLocation();

                        // add cost of sales distribution and inventory

                        double COST = 0d;

                        try {

                            LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                    arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);

                            COST = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                        }
                        catch (FinderException ex) {

                            COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                        }

                        double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(),
                                arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                        LocalAdBranchItemLocation adBranchItemLocation = null;

                        try {

                            adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arInvoiceLineItem.getInvItemLocation().getIlCode(), AD_BRNCH, companyCode);

                        }
                        catch (FinderException ex) {

                        }

                        if (arInvoiceLineItem.getIliEnableAutoBuild() == 0 || arInvoiceLineItem.getIliEnableAutoBuild() == 1 || arInvoiceLineItem.getInvItemLocation().getInvItem().getIiClass().equals("Stock")) {

                            if (adBranchItemLocation != null) {

                                this.addArDrIliEntry(arInvoice.getArDrNextLine(),
                                        "COGS", EJBCommon.TRUE, COST * QTY_SLD,
                                        adBranchItemLocation.getBilCoaGlCostOfSalesAccount(), arInvoice, AD_BRNCH, companyCode);

                                this.addArDrIliEntry(arInvoice.getArDrNextLine(),
                                        "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD,
                                        adBranchItemLocation.getBilCoaGlInventoryAccount(), arInvoice, AD_BRNCH, companyCode);

                            } else {

                                this.addArDrIliEntry(arInvoice.getArDrNextLine(),
                                        "COGS", EJBCommon.TRUE, COST * QTY_SLD,
                                        arInvoiceLineItem.getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arInvoice, AD_BRNCH, companyCode);

                                this.addArDrIliEntry(arInvoice.getArDrNextLine(),
                                        "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD,
                                        arInvoiceLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(), arInvoice, AD_BRNCH, companyCode);

                            }

                            // add quantity to item location committed quantity

                            double convertedQuantity = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);
                            invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() + convertedQuantity);

                        }

                        // add inventory sale distributions

                        if (adBranchItemLocation != null) {

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(),
                                    "REVENUE", EJBCommon.FALSE, arInvoiceLineItem.getIliAmount(),
                                    adBranchItemLocation.getBilCoaGlSalesAccount(), arInvoice, AD_BRNCH, companyCode);

                        } else {

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(),
                                    "REVENUE", EJBCommon.FALSE, arInvoiceLineItem.getIliAmount(),
                                    arInvoiceLineItem.getInvItemLocation().getIlGlCoaSalesAccount(), arInvoice, AD_BRNCH, companyCode);

                        }

                        TOTAL_TAX += arInvoiceLineItem.getIliTaxAmount();

                        if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiTaxCode().contains("Prepared Food Tax")) {
                            preparedTaxInvoice2 = preparedTaxInvoice2 + arInvoiceLineItem.getIliTaxAmount();
                        }

                        if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiTaxCode().contains("Sales Tax")) {
                            salesTaxInvoice2 = salesTaxInvoice2 + arInvoiceLineItem.getIliTaxAmount();
                        }
                    }

                    // add tax distribution if necessary

                    if (!arInvoice.getArTaxCode().getTcType().equals("NONE") &&
                            !arInvoice.getArTaxCode().getTcType().equals("EXEMPT")) {

                        if (adCompany.getCmpShortName().toLowerCase().equals("tinderbox")) {
                            String brTaxCOA = this.getBranchTaxCOA(BR_BRNCH_CODE,
                                    arInvoice.getArTaxCode().getGlChartOfAccount().getCoaSegment1(),
                                    arInvoice.getArTaxCode().getGlChartOfAccount().getCoaSegment3(),
                                    arInvoice.getArTaxCode().getGlChartOfAccount().getCoaSegment4(),
                                    arInvoice.getArTaxCode().getGlChartOfAccount().getCoaSegment5());
							/*
							this.addArDrEntry(arInvoice.getArDrNextLine(),
									"TAX", EJBCommon.FALSE, TOTAL_TAX, glChartOfAccountHome.findByCoaAccountNumber(brTaxCOA, companyCode).getCoaCode(),
									EJBCommon.FALSE, arInvoice, AD_BRNCH, companyCode);*/

                            LocalArTaxCode arTaxCodePreparedFoodTax = null;
                            if (preparedTax != 0) {
                                try {

                                    arTaxCodePreparedFoodTax = arTaxCodeHome.findByTcName("Prepared Food Tax", companyCode);

                                }
                                catch (FinderException ex) {

                                }

                                if (arTaxCodePreparedFoodTax != null) {
                                    this.addArDrEntry(arInvoice.getArDrNextLine(),
                                            "TAX", EJBCommon.FALSE, preparedTaxInvoice2, arTaxCodePreparedFoodTax.getGlChartOfAccount().getCoaCode(),
                                            EJBCommon.FALSE, arInvoice, AD_BRNCH, companyCode);
                                }

                            }
                            System.out.println("ADD TAX salesTax: " + salesTax);
                            LocalArTaxCode arTaxCodeSalesTax = null;
                            if (salesTax != 0) {
                                try {

                                    arTaxCodeSalesTax = arTaxCodeHome.findByTcName("Sales Tax", companyCode);

                                }
                                catch (FinderException ex) {

                                }

                                if (arTaxCodeSalesTax != null) {
                                    this.addArDrEntry(arInvoice.getArDrNextLine(),
                                            "TAX", EJBCommon.FALSE, salesTaxInvoice2, arTaxCodeSalesTax.getGlChartOfAccount().getCoaCode(),
                                            EJBCommon.FALSE, arInvoice, AD_BRNCH, companyCode);
                                }

                            }
                        } else {
                            LocalArTaxCode arTaxCodePreparedFoodTax = null;
                            if (preparedTax != 0) {
                                try {

                                    arTaxCodePreparedFoodTax = arTaxCodeHome.findByTcName("Prepared Food Tax", companyCode);

                                }
                                catch (FinderException ex) {

                                }

                                if (arTaxCodePreparedFoodTax != null) {
                                    this.addArDrEntry(arInvoice.getArDrNextLine(),
                                            "TAX", EJBCommon.FALSE, preparedTaxInvoice2, arTaxCodePreparedFoodTax.getGlChartOfAccount().getCoaCode(),
                                            EJBCommon.FALSE, arInvoice, AD_BRNCH, companyCode);
                                }

                            }
                            System.out.println("ADD TAX salesTax: " + salesTax);
                            LocalArTaxCode arTaxCodeSalesTax = null;
                            if (salesTax != 0) {
                                try {

                                    arTaxCodeSalesTax = arTaxCodeHome.findByTcName("Sales Tax", companyCode);

                                }
                                catch (FinderException ex) {

                                }

                                if (arTaxCodeSalesTax != null) {
                                    this.addArDrEntry(arInvoice.getArDrNextLine(),
                                            "TAX", EJBCommon.FALSE, salesTaxInvoice2, arTaxCodeSalesTax.getGlChartOfAccount().getCoaCode(),
                                            EJBCommon.FALSE, arInvoice, AD_BRNCH, companyCode);
                                }

                            }
							/*
							if(preparedTax!=0){
								this.addArDrEntry(arInvoice.getArDrNextLine(),
										"TAX", EJBCommon.FALSE, preparedTaxInvoice2, adPreference.getPrfInvPreparedFoodTaxCodeAccount(),
										EJBCommon.FALSE, arInvoice, AD_BRNCH, companyCode);
							}

							if(salesTax!=0){
								this.addArDrEntry(arInvoice.getArDrNextLine(),
										"TAX", EJBCommon.FALSE, salesTaxInvoice2, adPreference.getPrfInvSalesTaxCodeAccount(),
										EJBCommon.FALSE, arInvoice, AD_BRNCH, companyCode);
							}*/
							/*
							this.addArDrEntry(arInvoice.getArDrNextLine(),
									"TAX", EJBCommon.FALSE, TOTAL_TAX, arInvoice.getArTaxCode().getGlChartOfAccount().getCoaCode(),
									EJBCommon.FALSE, arInvoice, AD_BRNCH, companyCode);*/

                        }


                    }

                    // add cash distribution

                    LocalAdBranchCustomer adBranchCustomer = null;

                    try {

                        adBranchCustomer = adBranchCustomerHome.findBcstByCstCodeAndBrCode(arInvoice.getArCustomer().getCstCode(), AD_BRNCH, companyCode);

                    }
                    catch (FinderException ex) {

                    }

                    if (adBranchCustomer != null) {

                        this.addArDrEntry(arInvoice.getArDrNextLine(), "RECEIVABLE",
                                EJBCommon.TRUE, arInvoice.getInvAmountDue(),
                                adBranchCustomer.getBcstGlCoaReceivableAccount(),
                                EJBCommon.FALSE, arInvoice, AD_BRNCH, companyCode);

                    } else {
                        this.addArDrEntry(arInvoice.getArDrNextLine(), "RECEIVABLE",
                                EJBCommon.TRUE, arInvoice.getInvAmountDue(),
                                arInvoice.getArCustomer().getCstGlCoaReceivableAccount(),
                                EJBCommon.FALSE, arInvoice, AD_BRNCH, companyCode);
                    }


                    if (arModUploadReceiptDetails.getRctPosDiscount() != 0) {
                        // add discount
                        this.addArDrEntry(arInvoice.getArDrNextLine(), "DISCOUNT",
                                EJBCommon.TRUE, arModUploadReceiptDetails.getRctPosDiscount(),
                                adPreference.getPrfMiscPosDiscountAccount(),
                                EJBCommon.FALSE, arInvoice, AD_BRNCH, companyCode);
                    }

                    if (arModUploadReceiptDetails.getRctPosScAmount() != 0) {
                        // add sc amount
                        this.addArDrEntry(arInvoice.getArDrNextLine(), "SERVCE CHARGE",
                                EJBCommon.FALSE, arModUploadReceiptDetails.getRctPosScAmount(),
                                adPreference.getPrfMiscPosServiceChargeAccount(),
                                EJBCommon.FALSE, arInvoice, AD_BRNCH, companyCode);
                    }

                    if (arModUploadReceiptDetails.getRctPosDcAmount() != 0) {
                        // add dc amount
                        this.addArDrEntry(arInvoice.getArDrNextLine(), "DINEIN CHARGE",
                                EJBCommon.FALSE, arModUploadReceiptDetails.getRctPosDcAmount(),
                                adPreference.getPrfMiscPosDineInChargeAccount(),
                                EJBCommon.FALSE, arInvoice, AD_BRNCH, companyCode);
                    }


                    // add forex gain/loss

                    double forexGainLoss = TOTAL_LINE - (arInvoice.getInvAmountDue() + arModUploadReceiptDetails.getRctPosDiscount() - arModUploadReceiptDetails.getRctPosScAmount() - arModUploadReceiptDetails.getRctPosDcAmount());
                    if (forexGainLoss != 0) {
                        this.addArDrEntry(arInvoice.getArDrNextLine(), "FOREX",
                                forexGainLoss > 0 ? EJBCommon.TRUE : EJBCommon.FALSE, Math.abs(forexGainLoss),
                                adPreference.getPrfMiscPosGiftCertificateAccount(),
                                EJBCommon.FALSE, arInvoice, AD_BRNCH, companyCode);
                    }

                }

                success = 1;
                return success;
            } else {

                System.out.println("NULL ACCOUNTs");
                System.out.println(success);
                return success;

            }

        }
        catch (Exception ex) {

            ex.printStackTrace();
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }
    }

    @Override
    public int setArMiscReceiptAllNewAndVoidWithExpiryDate(String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier, String companyShortName) {

        Debug.print("ArMiscReceiptSyncControllerBean setArMiscReceiptAllNewAndVoidWithExpiryDate");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            //LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(BR_BRNCH_CODE, companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(branchCode);
            Integer AD_BRNCH = adBranch.getBrCode();
            String BR_BRNCH_CODE = adBranch.getBrBranchCode();

            int success = 0;

            if (adPreference.getPrfMiscPosDiscountAccount() != null && adPreference.getPrfMiscPosGiftCertificateAccount() != null &&
                    adPreference.getPrfMiscPosServiceChargeAccount() != null && adPreference.getPrfMiscPosDineInChargeAccount() != null) {

                // new receipts

                HashMap receiptNumbers = new HashMap();
                HashMap invoiceNumbers = new HashMap();
                String fundTransferNumber = null;
                String firstNumber = "";
                String lastNumber = "";

                for (int i = 0; i < receipts.length; i++) {

                    ArModReceiptDetails arModReceiptDetails = receiptDecodeWithExpiryDate(receipts[i]);
                    String reference_number = arModReceiptDetails.getRctNumber();
                    String customerName = arModReceiptDetails.getRctCstName();
                    String customerAddress = arModReceiptDetails.getRctCustomerAddress();

                    // Commented to be able to create receipt in each transaction
                    //	if (i == 0) firstNumber = arModReceiptDetails.getRctNumber();
                    //	if (i == receipts.length - 1) lastNumber = arModReceiptDetails.getRctNumber();

                    if (arModReceiptDetails.getRctPosOnAccount() == 0) {

                        // generate receipt number

                        String generatedReceipt = null;

                        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
                        LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;

                        try {

                            adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AR RECEIPT", companyCode);

                        }
                        catch (FinderException ex) {

                        }

                        try {

                            adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, companyCode);

                        }
                        catch (FinderException ex) {

                        }

                        while (true) {

                            if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                                try {

                                    arReceiptHome.findByRctNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, companyCode);
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                                }
                                catch (FinderException ex) {

                                    generatedReceipt = adDocumentSequenceAssignment.getDsaNextSequence();
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                    break;

                                }

                            } else {

                                try {

                                    arReceiptHome.findByRctNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, companyCode);
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                                }
                                catch (FinderException ex) {

                                    generatedReceipt = adBranchDocumentSequenceAssignment.getBdsNextSequence();
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                    break;

                                }

                            }

                        }
                        LocalArReceipt arExistingReceipt = null;
						/* Commented to be able to create receipt each transaction
						Iterator rctIter = receiptNumbers.values().iterator();
						while (rctIter.hasNext()) {

							try {

								ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails)rctIter.next();

								arExistingReceipt = arReceiptHome.findByRctDateAndRctNumberAndCstCustomerCodeAndBrCode(arModReceiptDetails.getRctDate(),
										arModUploadReceiptDetails.getRctNumber(), arModReceiptDetails.getRctCstCustomerCode(), AD_BRNCH, companyCode);

								break;

							} catch (FinderException ex) {

								continue;

							}

						}
						*/


                        Collection existingReceipts = arReceiptHome.findByRctRfrncNmbrAndBrCode(reference_number,
                                AD_BRNCH, companyCode);

                        //check duplicate receipt if found go to next
                        if (existingReceipts.size() > 0) {
                            continue;
                        }

                        if (arExistingReceipt == null) {
                            System.out.println(generatedReceipt);
                            ArModReceiptDetails arModUploadReceiptDetails = new ArModReceiptDetails();
                            arModUploadReceiptDetails.setRctNumber(generatedReceipt);
                            arModUploadReceiptDetails.setRctPosDiscount(EJBCommon.roundIt(arModReceiptDetails.getRctPosDiscount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosScAmount(EJBCommon.roundIt(arModReceiptDetails.getRctPosScAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosDcAmount(EJBCommon.roundIt(arModReceiptDetails.getRctPosDcAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));

                            receiptNumbers.put(generatedReceipt, arModUploadReceiptDetails);


                            double rctAmount =
                                    EJBCommon.roundIt(arModReceiptDetails.getRctPosTotalAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2);

                            LocalArReceipt arReceipt = arReceiptHome.create("MISC",
                                    "POS Sales " + new Date(), arModReceiptDetails.getRctDate(), generatedReceipt,
                                    null, null, null,
                                    arModReceiptDetails.getRctChequeNumber(),
                                    arModReceiptDetails.getRctVoucherNumber(),
                                    arModReceiptDetails.getRctCardNumber1(),
                                    arModReceiptDetails.getRctCardNumber2(),
                                    arModReceiptDetails.getRctCardNumber3(),
                                    rctAmount,
                                    arModReceiptDetails.getRctAmountCash(),
                                    arModReceiptDetails.getRctAmountCheque(),
                                    arModReceiptDetails.getRctAmountVoucher(),
                                    arModReceiptDetails.getRctAmountCard1(),
                                    arModReceiptDetails.getRctAmountCard2(),
                                    arModReceiptDetails.getRctAmountCard3(),
                                    null, 1, null, "CASH", EJBCommon.FALSE,
                                    0, null, null, EJBCommon.FALSE, null, EJBCommon.FALSE,
                                    EJBCommon.FALSE,
                                    EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE,
                                    null, null, null, null, null,
                                    cashier, EJBCommon.getGcCurrentDateWoTime().getTime(),
                                    cashier, EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, null, null,
                                    EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, customerName, customerAddress,
                                    EJBCommon.FALSE, EJBCommon.FALSE, null,
                                    AD_BRNCH, companyCode
                            );

                            arReceipt.setRctReferenceNumber(reference_number);
                            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                            arReceipt.setGlFunctionalCurrency(glFunctionalCurrency);
                            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(arModReceiptDetails.getRctCstCustomerCode(), companyCode);
                            arReceipt.setArCustomer(arCustomer);

                            if (!arModReceiptDetails.getRctSlpSalespersonCode().equals("")) {
                                LocalArSalesperson arSalesperson = arSalespersonHome.findBySlpSalespersonCode(arModReceiptDetails.getRctSlpSalespersonCode(), companyCode);

                                arReceipt.setArSalesperson(arSalesperson);

                            }

                            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(arCustomer.getAdBankAccount().getBaName(), companyCode);
                            arReceipt.setAdBankAccount(adBankAccount);

                            //card 1 bank account
                            if (!arModReceiptDetails.getRctCardType1().equals("") && arModReceiptDetails.getRctAmountCard1() > 0) {
                                System.out.println(arModReceiptDetails.getRctCardType1() + " 1");
                                adBankAccount = adBankAccountHome.findByBaName(arModReceiptDetails.getRctCardType1(), companyCode);
                                arReceipt.setAdBankAccountCard1(adBankAccount);
                            }


                            //card 2 bank account
                            if (!arModReceiptDetails.getRctCardType2().equals("") && arModReceiptDetails.getRctAmountCard2() > 0) {
                                System.out.println(arModReceiptDetails.getRctCardType2() + " 2");
                                adBankAccount = adBankAccountHome.findByBaName(arModReceiptDetails.getRctCardType2(), companyCode);
                                arReceipt.setAdBankAccountCard2(adBankAccount);
                            }


                            //card 3 bank account
                            if (!arModReceiptDetails.getRctCardType3().equals("") && arModReceiptDetails.getRctAmountCard3() > 0) {
                                System.out.println(arModReceiptDetails.getRctCardType3() + " 3");
                                adBankAccount = adBankAccountHome.findByBaName(arModReceiptDetails.getRctCardType3(), companyCode);
                                arReceipt.setAdBankAccountCard3(adBankAccount);
                            }


                            LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName(arModReceiptDetails.getRctTcName(), companyCode);

                            arReceipt.setArTaxCode(arTaxCode);

                            LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName("NONE", companyCode);
                            arReceipt.setArWithholdingTaxCode(arWithholdingTaxCode);

                            LocalArReceiptBatch arReceiptBatch = null;
                            try {
                                arReceiptBatch = arReceiptBatchHome.findByRbName(arModReceiptDetails.getRctPOSName(), AD_BRNCH, companyCode);
                            }
                            catch (FinderException ex) {
                                arReceiptBatch = arReceiptBatchHome.create(arModReceiptDetails.getRctPOSName(), arModReceiptDetails.getRctPOSName(), "OPEN", "MISC", EJBCommon.getGcCurrentDateWoTime().getTime(), cashier, AD_BRNCH, companyCode);

                            }
                            arReceipt.setArReceiptBatch(arReceiptBatch);

                            Iterator iter = arModReceiptDetails.getInvIliList().iterator();
                            short lineNumber = 0;
                            while (iter.hasNext()) {

                                ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = (ArModInvoiceLineItemDetails) iter.next();

                                short precisionUnit = this.getGlFcPrecisionUnit(companyCode);

                                double ILI_AMNT = 0d;
                                double ILI_TAX_AMNT = 0d;

                                double ILI_NET_AMOUNT = arModInvoiceLineItemDetails.getIliAmount() - arModInvoiceLineItemDetails.getIliTotalDiscount();

                                // calculate net amount

                                if (arTaxCode.getTcType().equals("INCLUSIVE")) {

                                    ILI_AMNT = EJBCommon.roundIt(ILI_NET_AMOUNT / (1 + (arTaxCode.getTcRate() / 100)), precisionUnit);

                                } else {

                                    // tax exclusive, none, zero rated or exempt

                                    ILI_AMNT = ILI_NET_AMOUNT;

                                }

                                // calculate tax

                                if (!arTaxCode.getTcType().equals("NONE") &&
                                        !arTaxCode.getTcType().equals("EXEMPT")) {


                                    if (arTaxCode.getTcType().equals("INCLUSIVE")) {

                                        ILI_TAX_AMNT = EJBCommon.roundIt(ILI_NET_AMOUNT - ILI_AMNT, precisionUnit);

                                    } else if (arTaxCode.getTcType().equals("EXCLUSIVE")) {

                                        ILI_TAX_AMNT = EJBCommon.roundIt(ILI_NET_AMOUNT * arTaxCode.getTcRate() / 100, precisionUnit);

                                    } else {

                                        // tax none zero-rated or exempt

                                    }

                                }

                                LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.create(
                                        ++lineNumber, arModInvoiceLineItemDetails.getIliQuantity(),
                                        arModInvoiceLineItemDetails.getIliUnitPrice(),
                                        ILI_AMNT, ILI_TAX_AMNT,
                                        EJBCommon.FALSE, 0,
                                        0, 0, 0, arModInvoiceLineItemDetails.getIliTotalDiscount(), EJBCommon.TRUE, companyCode);


                                arInvoiceLineItem.setArReceipt(arReceipt);

                                LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                arInvoiceLineItem.setInvUnitOfMeasure(invUnitOfMeasure);
                                arInvoiceLineItem.setIliImei(arModInvoiceLineItemDetails.getILI_IMEI());


                                //create tags for IMEI

                                arInvoiceLineItem.setIliMisc(arModInvoiceLineItemDetails.getIliMisc());

                                LocalInvItem invItem = invItemHome.findByIiCode(arModInvoiceLineItemDetails.getIliCode(), companyCode);
                                LocalInvLocation invLocation = null;
                                try {
                                    if (invItem.getIiDefaultLocation() != null) {
                                        invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                                    }
                                }
                                catch (FinderException ex) {
                                }

                                LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiCodeAndLocName(arModInvoiceLineItemDetails.getIliCode(),
                                        invLocation == null ? arModInvoiceLineItemDetails.getIliLocName() : invLocation.getLocName(), companyCode);
                                arInvoiceLineItem.setInvItemLocation(invItemLocation);

                                if (invItemLocation.getInvItem().getIiEnableAutoBuild() == EJBCommon.TRUE) {

                                    arInvoiceLineItem.setIliEnableAutoBuild(EJBCommon.TRUE);

                                }
                                // new code
                                //add serial into inv tag
                                if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiTraceMisc() == 1) {

                                    ArrayList tagList = arModInvoiceLineItemDetails.getiIliTagList();

                                    Iterator iTag = tagList.iterator();

                                    while (iTag.hasNext()) {

                                        InvModTagListDetails details = (InvModTagListDetails) iTag.next();

                                        LocalInvTag invTag = invTagHome.create(details.getTgPropertyCode(), details.getTgSerialNumber(), details.getTgDocumentNumber(), details.getTgExpiryDate(), details.getTgSpecs(), companyCode, details.getTgTransactionDate(), "IN");

                                        invTag.setArInvoiceLineItem(arInvoiceLineItem);
                                        invTag.setInvItemLocation(arInvoiceLineItem.getInvItemLocation());
                                    }

                                }


                            }

                        } else {

                            ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) receiptNumbers.get(arExistingReceipt.getRctNumber());

                            arModUploadReceiptDetails.setRctPosDiscount(arModUploadReceiptDetails.getRctPosDiscount() + EJBCommon.roundIt(arModReceiptDetails.getRctPosDiscount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosScAmount(arModUploadReceiptDetails.getRctPosScAmount() + EJBCommon.roundIt(arModReceiptDetails.getRctPosScAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosDcAmount(arModUploadReceiptDetails.getRctPosDcAmount() + EJBCommon.roundIt(arModReceiptDetails.getRctPosDcAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            receiptNumbers.put(arExistingReceipt.getRctNumber(), arModUploadReceiptDetails);

                            double totalAmount = EJBCommon.roundIt((arModReceiptDetails.getRctPosTotalAmount() - arModReceiptDetails.getRctPosVoidAmount()) * arModReceiptDetails.getRctConversionRate(), (short) 2);
                            arExistingReceipt.setRctAmount(arExistingReceipt.getRctAmount() + totalAmount);

                            Iterator iter = arModReceiptDetails.getInvIliList().iterator();


                            while (iter.hasNext()) {

                                ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = (ArModInvoiceLineItemDetails) iter.next();
                                System.out.println(arModInvoiceLineItemDetails.getIliIiName());
                                LocalInvItem invItem = invItemHome.findByIiName(arModInvoiceLineItemDetails.getIliIiName(), companyCode);
                                LocalInvLocation invLocation = null;
                                try {
                                    if (invItem.getIiDefaultLocation() != null) {
                                        invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                                    }
                                }
                                catch (FinderException ex) {
                                }
                                LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(arModInvoiceLineItemDetails.getIliIiName(),
                                        invLocation == null ? arModInvoiceLineItemDetails.getIliLocName() : invLocation.getLocName(), companyCode);

                                LocalArInvoiceLineItem arExistingInvoiceLineItem = null;

                                try {
                                    arExistingInvoiceLineItem = arInvoiceLineItemHome.findByRctCodeAndIlCodeAndUomName(arExistingReceipt.getRctCode(),
                                            invItemLocation.getIlCode(), arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                }
                                catch (FinderException ex) {

                                }

                                if (arExistingInvoiceLineItem == null) {
                                    System.out.println(arModInvoiceLineItemDetails.getIliIiName());
                                    LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.create((short) (arExistingReceipt.getArInvoiceLineItems().size() + 1),
                                            arModInvoiceLineItemDetails.getIliQuantity(), arModInvoiceLineItemDetails.getIliUnitPrice(),
                                            EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingReceipt.getArTaxCode().getTcRate() / 100), (short) 2), EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() - (arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingReceipt.getArTaxCode().getTcRate() / 100)), (short) 2),
                                            EJBCommon.FALSE, 0, 0, 0, 0, 0, EJBCommon.TRUE, companyCode);

                                    arInvoiceLineItem.setArReceipt(arExistingReceipt);

                                    LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                    arInvoiceLineItem.setInvUnitOfMeasure(invUnitOfMeasure);
                                    arInvoiceLineItem.setInvItemLocation(invItemLocation);
                                    arInvoiceLineItem.setIliMisc(arModInvoiceLineItemDetails.getIliMisc());
                                    if (invItemLocation.getInvItem().getIiEnableAutoBuild() == EJBCommon.TRUE) {

                                        arInvoiceLineItem.setIliEnableAutoBuild(EJBCommon.TRUE);

                                    }
                                } else {

                                    arExistingInvoiceLineItem.setIliQuantity(arExistingInvoiceLineItem.getIliQuantity() + arModInvoiceLineItemDetails.getIliQuantity());
                                    arExistingInvoiceLineItem.setIliUnitPrice((arExistingInvoiceLineItem.getIliUnitPrice() + arModInvoiceLineItemDetails.getIliUnitPrice()) / 2);
                                    arExistingInvoiceLineItem.setIliAmount(arExistingInvoiceLineItem.getIliAmount() + EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingReceipt.getArTaxCode().getTcRate() / 100), (short) 2));
                                    arExistingInvoiceLineItem.setIliTaxAmount(arExistingInvoiceLineItem.getIliTaxAmount() + EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() - (arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingReceipt.getArTaxCode().getTcRate() / 100)), (short) 2));

                                }
                            }
                        }

                    } else {

                        // generate Invoice number

                        String generatedInvoice = null;

                        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
                        LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;

                        try {

                            adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AR INVOICE", companyCode);

                        }
                        catch (FinderException ex) {

                        }

                        try {

                            adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, companyCode);

                        }
                        catch (FinderException ex) {

                        }

                        while (true) {

                            if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                                try {

                                    arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), (byte) 0, AD_BRNCH, companyCode);
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                                }
                                catch (FinderException ex) {

                                    generatedInvoice = adDocumentSequenceAssignment.getDsaNextSequence();
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                    break;

                                }

                            } else {

                                try {

                                    arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), (byte) 0, AD_BRNCH, companyCode);
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                                }
                                catch (FinderException ex) {

                                    generatedInvoice = adBranchDocumentSequenceAssignment.getBdsNextSequence();
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                    break;

                                }

                            }

                        }


                    }

                }

                // for each receipt generate distribution records

                Iterator rctIter = receiptNumbers.values().iterator();

                while (rctIter.hasNext()) {

                    ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) rctIter.next();

                    LocalArReceipt arReceipt = arReceiptHome.findByRctNumberAndBrCode(arModUploadReceiptDetails.getRctNumber(), AD_BRNCH, companyCode);

                    Iterator lineIter = arReceipt.getArInvoiceLineItems().iterator();

                    double TOTAL_TAX = 0;
                    double TOTAL_LINE = 0;
                    double TOTAL_DISCOUNT = 0;


                    while (lineIter.hasNext()) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) lineIter.next();


                        LocalInvItemLocation invItemLocation = arInvoiceLineItem.getInvItemLocation();

                        // add cost of sales distribution and inventory

                        double COST = 0d;

                        try {


                            LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                    arReceipt.getRctDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);

                            if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Average")) {
                                if (invCosting.getCstRemainingQuantity() <= 0) {
                                    COST = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();

                                } else {
                                    COST = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                                    if (COST <= 0) {
                                        COST = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                                    }
                                }

                            } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("FIFO")) {

                                COST = Math.abs(this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(),
                                        arInvoiceLineItem.getIliQuantity(), arInvoiceLineItem.getIliUnitPrice(), false, AD_BRNCH, companyCode));

                            } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Standard")) {

                                COST = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                            }

                        }
                        catch (FinderException ex) {

                            COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                        }

                        double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(),
                                arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                        LocalAdBranchItemLocation adBranchItemLocation = null;

                        try {

                            adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arInvoiceLineItem.getInvItemLocation().getIlCode(), AD_BRNCH, companyCode);

                        }
                        catch (FinderException ex) {

                        }

                        if ((arInvoiceLineItem.getIliEnableAutoBuild() == 0 || arInvoiceLineItem.getInvItemLocation().getInvItem().getIiClass().equals("Stock"))
                                && arInvoiceLineItem.getInvItemLocation().getInvItem().getIiNonInventoriable() == EJBCommon.FALSE) {

                            if (adBranchItemLocation != null) {

                                this.addArDrIliEntry(arReceipt.getArDrNextLine(),
                                        "COGS", EJBCommon.TRUE, COST * QTY_SLD,
                                        adBranchItemLocation.getBilCoaGlCostOfSalesAccount(), arReceipt, AD_BRNCH, companyCode);

                                this.addArDrIliEntry(arReceipt.getArDrNextLine(),
                                        "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD,
                                        adBranchItemLocation.getBilCoaGlInventoryAccount(), arReceipt, AD_BRNCH, companyCode);

                            } else {

                                this.addArDrIliEntry(arReceipt.getArDrNextLine(),
                                        "COGS", EJBCommon.TRUE, COST * QTY_SLD,
                                        arInvoiceLineItem.getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arReceipt, AD_BRNCH, companyCode);

                                this.addArDrIliEntry(arReceipt.getArDrNextLine(),
                                        "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD,
                                        arInvoiceLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(), arReceipt, AD_BRNCH, companyCode);

                            }

                            // add quantity to item location committed quantity

                            double convertedQuantity = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);
                            invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() + convertedQuantity);

                        }

                        // add inventory sale distributions

                        if (adBranchItemLocation != null) {

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(),
                                    "REVENUE", EJBCommon.FALSE, arInvoiceLineItem.getIliAmount(),
                                    adBranchItemLocation.getBilCoaGlSalesAccount(), arReceipt, AD_BRNCH, companyCode);

                        } else {

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(),
                                    "REVENUE", EJBCommon.FALSE, arInvoiceLineItem.getIliAmount(),
                                    arInvoiceLineItem.getInvItemLocation().getIlGlCoaSalesAccount(), arReceipt, AD_BRNCH, companyCode);

                        }

                        TOTAL_LINE += arInvoiceLineItem.getIliAmount();

                        TOTAL_TAX += arInvoiceLineItem.getIliTaxAmount();
                        TOTAL_DISCOUNT += arInvoiceLineItem.getIliTotalDiscount();

                    }

                    // add tax distribution if necessary

                    if (!arReceipt.getArTaxCode().getTcType().equals("NONE") && !arReceipt.getArTaxCode().getTcType().equals("EXEMPT")) {
                        LocalAdBranchArTaxCode adBranchTaxCode = null;
                        try {
                            adBranchTaxCode = adBranchArTaxCodeHome.findBtcByTcCodeAndBrCode(arReceipt.getArTaxCode().getTcCode(), AD_BRNCH, companyCode);

                        }
                        catch (FinderException ex) {

                        }

                        if (adBranchTaxCode != null) {
                            this.addArDrEntry(arReceipt.getArDrNextLine(),
                                    "TAX", EJBCommon.FALSE, TOTAL_TAX, adBranchTaxCode.getBtcGlCoaTaxCode(),
                                    EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

                        } else {

                            this.addArDrEntry(arReceipt.getArDrNextLine(),
                                    "TAX", EJBCommon.FALSE, TOTAL_TAX, arReceipt.getArTaxCode().getGlChartOfAccount().getCoaCode(),
                                    EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                        }
                    }


                    // add cash distribution
                    LocalAdBranchBankAccount adBranchBankAccount = null;
                    LocalAdBranchBankAccount adBranchBankAccountCard1 = null;
                    LocalAdBranchBankAccount adBranchBankAccountCard2 = null;
                    LocalAdBranchBankAccount adBranchBankAccountCard3 = null;


                    if (arReceipt.getRctAmountCard1() > 0) {

                        try {
                            adBranchBankAccountCard1 = adBranchBankAccountHome.findBbaByBaCodeAndBrCode(arReceipt.getAdBankAccountCard1().getBaCode(), AD_BRNCH, companyCode);

                        }
                        catch (FinderException ex) {
                        }


                        if (adBranchBankAccount != null) {

                            this.addArDrEntry(arReceipt.getArDrNextLine(), "CARD 1",
                                    EJBCommon.TRUE, arReceipt.getRctAmountCard1(),
                                    adBranchBankAccountCard1.getBbaGlCoaCashAccount(),
                                    EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

                        } else {

                            this.addArDrEntry(arReceipt.getArDrNextLine(), "CARD 1",
                                    EJBCommon.TRUE, arReceipt.getRctAmountCard1(),
                                    arReceipt.getAdBankAccountCard1().getBaCoaGlCashAccount(),
                                    EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

                        }
                    }

                    if (arReceipt.getRctAmountCard2() > 0) {

                        try {
                            adBranchBankAccountCard2 = adBranchBankAccountHome.findBbaByBaCodeAndBrCode(arReceipt.getAdBankAccountCard2().getBaCode(), AD_BRNCH, companyCode);

                        }
                        catch (FinderException ex) {
                        }

                        if (adBranchBankAccount != null) {

                            this.addArDrEntry(arReceipt.getArDrNextLine(), "CARD 2",
                                    EJBCommon.TRUE, arReceipt.getRctAmountCard2(),
                                    adBranchBankAccountCard2.getBbaGlCoaCashAccount(),
                                    EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

                        } else {

                            this.addArDrEntry(arReceipt.getArDrNextLine(), "CARD 2",
                                    EJBCommon.TRUE, arReceipt.getRctAmountCard2(),
                                    arReceipt.getAdBankAccountCard2().getBaCoaGlCashAccount(),
                                    EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

                        }
                    }


                    if (arReceipt.getRctAmountCard3() > 0) {

                        try {
                            adBranchBankAccountCard3 = adBranchBankAccountHome.findBbaByBaCodeAndBrCode(arReceipt.getAdBankAccountCard3().getBaCode(), AD_BRNCH, companyCode);

                        }
                        catch (FinderException ex) {
                        }

                        if (adBranchBankAccount != null) {

                            this.addArDrEntry(arReceipt.getArDrNextLine(), "CARD 3",
                                    EJBCommon.TRUE, arReceipt.getRctAmountCard3(),
                                    adBranchBankAccountCard3.getBbaGlCoaCashAccount(),
                                    EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

                        } else {

                            this.addArDrEntry(arReceipt.getArDrNextLine(), "CARD 3",
                                    EJBCommon.TRUE, arReceipt.getRctAmountCard3(),
                                    arReceipt.getAdBankAccountCard3().getBaCoaGlCashAccount(),
                                    EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

                        }
                    }


                    // add cash distribution

                    try {
                        adBranchBankAccount = adBranchBankAccountHome.findBbaByBaCodeAndBrCode(arReceipt.getAdBankAccount().getBaCode(), AD_BRNCH, companyCode);

                    }
                    catch (FinderException ex) {
                    }


                    if (adBranchBankAccount != null) {

                        this.addArDrEntry(arReceipt.getArDrNextLine(), "CASH",
                                EJBCommon.TRUE,
                                TOTAL_LINE + TOTAL_TAX - TOTAL_DISCOUNT - arReceipt.getRctAmountVoucher() -
                                        arReceipt.getRctAmountCheque() - arReceipt.getRctAmountCard1() - arReceipt.getRctAmountCard2() - arReceipt.getRctAmountCard3(),
                                adBranchBankAccount.getBbaGlCoaCashAccount(),
                                EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

                        if (arReceipt.getRctAmountCheque() > 0) {
                            this.addArDrEntry(arReceipt.getArDrNextLine(), "CHEQUE",
                                    EJBCommon.TRUE, arReceipt.getRctAmountCheque(),
                                    adBranchBankAccount.getBbaGlCoaCashAccount(),
                                    EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

                        }

                        if (arReceipt.getRctAmountVoucher() > 0) {

                            this.addArDrEntry(arReceipt.getArDrNextLine(), "VOUCHER",
                                    EJBCommon.TRUE, arReceipt.getRctAmountVoucher(),
                                    adBranchBankAccount.getBbaGlCoaSalesDiscountAccount(),
                                    EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

                        }

                        if (TOTAL_DISCOUNT > 0) {
                            this.addArDrEntry(arReceipt.getArDrNextLine(), "SALES DISCOUNT",
                                    EJBCommon.TRUE, TOTAL_DISCOUNT,
                                    adBranchBankAccount.getBbaGlCoaSalesDiscountAccount(),
                                    EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

                        }

                    } else {

                        this.addArDrEntry(arReceipt.getArDrNextLine(), "CASH",
                                EJBCommon.TRUE,
                                TOTAL_LINE + TOTAL_TAX - TOTAL_DISCOUNT - arReceipt.getRctAmountVoucher() -
                                        arReceipt.getRctAmountCheque() - arReceipt.getRctAmountCard1() - arReceipt.getRctAmountCard2() - arReceipt.getRctAmountCard3(),
                                arReceipt.getAdBankAccount().getBaCoaGlCashAccount(),
                                EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

                        if (arReceipt.getRctAmountCheque() > 0) {

                            this.addArDrEntry(arReceipt.getArDrNextLine(), "CHEQUE",
                                    EJBCommon.TRUE,
                                    arReceipt.getRctAmountCheque(),
                                    arReceipt.getAdBankAccount().getBaCoaGlCashAccount(),
                                    EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

                        }

                        if (arReceipt.getRctAmountVoucher() > 0) {

                            this.addArDrEntry(arReceipt.getArDrNextLine(), "VOUCHER",
                                    EJBCommon.TRUE, arReceipt.getRctAmountVoucher(),
                                    arReceipt.getAdBankAccount().getBaCoaGlSalesDiscount(),
                                    EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

                        }


                        if (TOTAL_DISCOUNT > 0) {
                            this.addArDrEntry(arReceipt.getArDrNextLine(), "SALES DISCOUNT",
                                    EJBCommon.TRUE, TOTAL_DISCOUNT,
                                    arReceipt.getAdBankAccount().getBaCoaGlSalesDiscount(),
                                    EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

                        }

                    }


                    // set receipt amount

                    arReceipt.setRctAmount(TOTAL_LINE + TOTAL_TAX);


                    // generate approval status

                    if (adPreference.getPrfInvEnablePosAutoPostUpload() == EJBCommon.TRUE) {

                        this.executeArRctPost(arReceipt.getRctCode(), arReceipt.getRctLastModifiedBy(), AD_BRNCH, companyCode);
                        arReceipt.setRctApprovalStatus("N/A");
                    } else {
                        arReceipt.setRctApprovalStatus(null);
                    }

                    // set receipt approval status


                }


                success = 1;
                return success;

            } else {

                return success;

            }

        }
        catch (Exception ex) {

            ex.printStackTrace();
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }
    }

    @Override
    public int setArMiscReceiptAllNewAndVoidWithExpiryDateEnableItemAutoBuild(String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier, String companyShortName) {

        return 0;
    }

    @Override
    public int setArMiscReceiptAllNewAndVoidWithExpiryDateAndEntries(String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier, String companyShortName) {

        return 0;
    }

    @Override
    public int setArMiscReceiptAllNewAndVoidWithSalesperson(String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier, String companyShortName) {

        return 0;
    }

    @Override
    public ArMiscReceiptSyncResponse setArMiscReceiptAllNewAndVoid(ArMiscReceiptSyncRequest request) {

        Debug.print("ArMiscReceiptSyncControllerBean setArMiscReceiptAllNewAndVoid");

        ArMiscReceiptSyncResponse response = new ArMiscReceiptSyncResponse();

        LocalAdCompany adCompany;
        LocalAdBranch adBranch;
        Integer companyCode = null;
        Integer branchCode = null;
        String companyShortName = null;

        try {
            // Company Code
            try {
                if (request.getCompanyCode() == null) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_041);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_041_MSG);
                    return response;
                }
                adCompany = adCompanyHome.findByCmpShortName(request.getCompanyCode());
                if (adCompany != null) {
                    companyCode = adCompany.getCmpCode();
                    companyShortName = adCompany.getCmpShortName();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
                return response;
            }

            // Branch Code
            try {
                if (request.getBranchCode() == null || request.getBranchCode().equals("")) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return response;
                }
                assert adCompany != null;
                adBranch = adBranchHome.findByBrBranchCode(request.getBranchCode(), adCompany.getCmpCode(), companyShortName);
                if (adBranch != null) {
                    branchCode = adBranch.getBrCode();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_065);
                response.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_065_MSG, request.getBranchCode()));
                return response;
            }

            String[] receipts = request.getReceipts();
            String[] voidReceipts = request.getVoidReceipts();
            int count = this.setArMiscReceiptAllNewAndVoid(receipts, voidReceipts, branchCode, companyCode, request.getCashier(), companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setCount(count);
            response.setStatus("Set all invoice new and void data successfully.");
        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    @Override
    public ArMiscReceiptSyncResponse setArMiscReceiptAllNewAndVoidUS(ArMiscReceiptSyncRequest request) {

        Debug.print("ArMiscReceiptSyncControllerBean setArMiscReceiptAllNewAndVoidUS");

        ArMiscReceiptSyncResponse response = new ArMiscReceiptSyncResponse();

        LocalAdCompany adCompany;
        LocalAdBranch adBranch;
        Integer companyCode = null;
        Integer branchCode = null;
        String companyShortName = null;

        try {
            // Company Code
            try {
                if (request.getCompanyCode() == null) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_041);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_041_MSG);
                    return response;
                }
                adCompany = adCompanyHome.findByCmpShortName(request.getCompanyCode());
                if (adCompany != null) {
                    companyCode = adCompany.getCmpCode();
                    companyShortName = adCompany.getCmpShortName();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
                return response;
            }

            // Branch Code
            try {
                if (request.getBranchCode() == null || request.getBranchCode().equals("")) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return response;
                }
                assert adCompany != null;
                adBranch = adBranchHome.findByBrBranchCode(request.getBranchCode(), adCompany.getCmpCode(), companyShortName);
                if (adBranch != null) {
                    branchCode = adBranch.getBrCode();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_065);
                response.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_065_MSG, request.getBranchCode()));
                return response;
            }

            String[] receipts = request.getReceipts();
            String[] voidReceipts = request.getVoidReceipts();
            int count = this.setArMiscReceiptAllNewAndVoidUS(receipts, voidReceipts, branchCode, companyCode, request.getCashier(), companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setCount(count);
            response.setStatus("Set all invoice new and void data successfully.");
        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    @Override
    public ArMiscReceiptSyncResponse setArMiscReceiptAllNewAndVoidWithExpiryDate(ArMiscReceiptSyncRequest request) {

        Debug.print("ArMiscReceiptSyncControllerBean setArMiscReceiptAllNewAndVoidWithExpiryDate");

        ArMiscReceiptSyncResponse response = new ArMiscReceiptSyncResponse();

        LocalAdCompany adCompany;
        LocalAdBranch adBranch;
        Integer companyCode = null;
        Integer branchCode = null;
        String companyShortName = null;

        try {
            // Company Code
            try {
                if (request.getCompanyCode() == null) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_041);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_041_MSG);
                    return response;
                }
                adCompany = adCompanyHome.findByCmpShortName(request.getCompanyCode());
                if (adCompany != null) {
                    companyCode = adCompany.getCmpCode();
                    companyShortName = adCompany.getCmpShortName();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
                return response;
            }

            // Branch Code
            try {
                if (request.getBranchCode() == null || request.getBranchCode().equals("")) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return response;
                }
                assert adCompany != null;
                adBranch = adBranchHome.findByBrBranchCode(request.getBranchCode(), adCompany.getCmpCode(), companyShortName);
                if (adBranch != null) {
                    branchCode = adBranch.getBrCode();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_065);
                response.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_065_MSG, request.getBranchCode()));
                return response;
            }

            String[] receipts = request.getReceipts();
            String[] voidReceipts = request.getVoidReceipts();
            int count = this.setArMiscReceiptAllNewAndVoidWithExpiryDate(receipts, voidReceipts, branchCode, companyCode, request.getCashier(), companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setCount(count);
            response.setStatus("Set all invoice new and void data successfully.");
        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    @Override
    public ArMiscReceiptSyncResponse setArMiscReceiptAllNewAndVoidWithExpiryDateEnableItemAutoBuild(ArMiscReceiptSyncRequest request) {

        Debug.print("ArMiscReceiptSyncControllerBean setArMiscReceiptAllNewAndVoidWithExpiryDateEnableItemAutoBuild");

        ArMiscReceiptSyncResponse response = new ArMiscReceiptSyncResponse();

        LocalAdCompany adCompany;
        LocalAdBranch adBranch;
        Integer companyCode = null;
        Integer branchCode = null;
        String companyShortName = null;

        try {
            // Company Code
            try {
                if (request.getCompanyCode() == null) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_041);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_041_MSG);
                    return response;
                }
                adCompany = adCompanyHome.findByCmpShortName(request.getCompanyCode());
                if (adCompany != null) {
                    companyCode = adCompany.getCmpCode();
                    companyShortName = adCompany.getCmpShortName();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
                return response;
            }

            // Branch Code
            try {
                if (request.getBranchCode() == null || request.getBranchCode().equals("")) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return response;
                }
                assert adCompany != null;
                adBranch = adBranchHome.findByBrBranchCode(request.getBranchCode(), adCompany.getCmpCode(), companyShortName);
                if (adBranch != null) {
                    branchCode = adBranch.getBrCode();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_065);
                response.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_065_MSG, request.getBranchCode()));
                return response;
            }

            String[] receipts = request.getReceipts();
            String[] voidReceipts = request.getVoidReceipts();
            int count = this.setArMiscReceiptAllNewAndVoidWithExpiryDateEnableItemAutoBuild(receipts, voidReceipts, branchCode, companyCode, request.getCashier(), companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setCount(count);
            response.setStatus("Set all invoice new and void data successfully.");
        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    @Override
    public ArMiscReceiptSyncResponse setArMiscReceiptAllNewAndVoidWithExpiryDateAndEntries(ArMiscReceiptSyncRequest request) {

        Debug.print("ArMiscReceiptSyncControllerBean setArMiscReceiptAllNewAndVoidWithExpiryDateAndEntries");

        ArMiscReceiptSyncResponse response = new ArMiscReceiptSyncResponse();

        LocalAdCompany adCompany;
        LocalAdBranch adBranch;
        Integer companyCode = null;
        Integer branchCode = null;
        String companyShortName = null;

        try {
            // Company Code
            try {
                if (request.getCompanyCode() == null) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_041);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_041_MSG);
                    return response;
                }
                adCompany = adCompanyHome.findByCmpShortName(request.getCompanyCode());
                if (adCompany != null) {
                    companyCode = adCompany.getCmpCode();
                    companyShortName = adCompany.getCmpShortName();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
                return response;
            }

            // Branch Code
            try {
                if (request.getBranchCode() == null || request.getBranchCode().equals("")) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return response;
                }
                assert adCompany != null;
                adBranch = adBranchHome.findByBrBranchCode(request.getBranchCode(), adCompany.getCmpCode(), companyShortName);
                if (adBranch != null) {
                    branchCode = adBranch.getBrCode();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_065);
                response.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_065_MSG, request.getBranchCode()));
                return response;
            }

            String[] receipts = request.getReceipts();
            String[] voidReceipts = request.getVoidReceipts();
            int count = this.setArMiscReceiptAllNewAndVoidWithExpiryDateAndEntries(receipts, voidReceipts, branchCode, companyCode, request.getCashier(), companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setCount(count);
            response.setStatus("Set all invoice new and void data successfully.");
        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    @Override
    public ArMiscReceiptSyncResponse setArMiscReceiptAllNewAndVoidWithSalesperson(ArMiscReceiptSyncRequest request) {

        Debug.print("ArMiscReceiptSyncControllerBean setArMiscReceiptAllNewAndVoidWithSalesperson");

        ArMiscReceiptSyncResponse response = new ArMiscReceiptSyncResponse();

        LocalAdCompany adCompany;
        LocalAdBranch adBranch;
        Integer companyCode = null;
        Integer branchCode = null;
        String companyShortName = null;

        try {
            // Company Code
            try {
                if (request.getCompanyCode() == null) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_041);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_041_MSG);
                    return response;
                }
                adCompany = adCompanyHome.findByCmpShortName(request.getCompanyCode());
                if (adCompany != null) {
                    companyCode = adCompany.getCmpCode();
                    companyShortName = adCompany.getCmpShortName();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
                return response;
            }

            // Branch Code
            try {
                if (request.getBranchCode() == null || request.getBranchCode().equals("")) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return response;
                }
                assert adCompany != null;
                adBranch = adBranchHome.findByBrBranchCode(request.getBranchCode(), adCompany.getCmpCode(), companyShortName);
                if (adBranch != null) {
                    branchCode = adBranch.getBrCode();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_065);
                response.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_065_MSG, request.getBranchCode()));
                return response;
            }

            String[] receipts = request.getReceipts();
            String[] voidReceipts = request.getVoidReceipts();
            int count = this.setArMiscReceiptAllNewAndVoidWithSalesperson(receipts, voidReceipts, branchCode, companyCode, request.getCashier(), companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setCount(count);
            response.setStatus("Set all invoice new and void data successfully.");
        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    private void addArDrIliEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT,
                                 Integer COA_CODE, LocalArReceipt arReceipt, Integer AD_BRNCH, Integer companyCode)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArMiscReceiptSyncControllerBean addArDrIliEntry");

        try {

            // get company
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(COA_CODE);

            // create distribution record
            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, EJBCommon.FALSE, companyCode);

            //arReceipt.addArDistributionRecord(arDistributionRecord);
            arDistributionRecord.setArReceipt(arReceipt);

            //glChartOfAccount.addArDistributionRecord(arDistributionRecord);
            arDistributionRecord.setGlChartOfAccount(glChartOfAccount);

        }
        catch (FinderException ex) {

            throw new GlobalBranchAccountNumberInvalidException(ex.getMessage());

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }

    private void addArDrIliEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT,
                                 Integer COA_CODE, LocalArInvoice arInvoice, Integer AD_BRNCH, Integer companyCode)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArMiscReceiptSyncControllerBean addArDrIliEntry");

        try {

            // get company
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(COA_CODE);

            // create distribution record
            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, EJBCommon.FALSE, companyCode);

            //arInvoice.addArDistributionRecord(arDistributionRecord);
            arDistributionRecord.setArInvoice(arInvoice);
            //glChartOfAccount.addArDistributionRecord(arDistributionRecord);
            arDistributionRecord.setGlChartOfAccount(glChartOfAccount);
        }
        catch (FinderException ex) {

            throw new GlobalBranchAccountNumberInvalidException(ex.getMessage());

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }

    private void addArDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT,
                              Integer COA_CODE, byte DR_RVRSD, LocalArReceipt arReceipt, Integer AD_BRNCH, Integer companyCode)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArMiscReceipSyncControllerBean addArDrEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(COA_CODE);
            System.out.println("COA_CODE: " + COA_CODE + "    COA_DESC: " + glChartOfAccount.getCoaAccountNumber());
            if (DR_CLSS.equals("DISCOUNT") || DR_CLSS.equals("FOREX")) {

                LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(AD_BRNCH);
                System.out.println("adBranch.getBrCoaSegment() = " + adBranch.getBrCoaSegment());
                String newCoa = adBranch.getBrCoaSegment();

                StringTokenizer st = new StringTokenizer(glChartOfAccount.getCoaAccountNumber(), "-");
                int ctr = 0;
                while (st.hasMoreTokens()) {
                    ctr++;
                    if (ctr >= 3) {
                        newCoa = newCoa + "-" + st.nextToken();
                    } else {
                        st.nextToken();
                    }
                }
                System.out.println("MIRACLE: " + newCoa);
                System.out.println("glChartOfAccountHome.findByCoaAccountNumber(" + newCoa + ", " + companyCode + ")");

                glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(newCoa, companyCode);

            }

            // create distribution record
            System.out.println("Create Dstrbtn Rcrd");
            System.out.println("DR_CLSS: " + DR_CLSS);
            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, DR_RVRSD, companyCode);

            //arReceipt.addArDistributionRecord(arDistributionRecord);
            arDistributionRecord.setArReceipt(arReceipt);
            //glChartOfAccount.addArDistributionRecord(arDistributionRecord);
            arDistributionRecord.setGlChartOfAccount(glChartOfAccount);

        }
        catch (FinderException ex) {
            ex.printStackTrace();
            throw new GlobalBranchAccountNumberInvalidException(ex.getMessage());

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }

    private void addArDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT,
                              Integer COA_CODE, byte DR_RVRSD, LocalArInvoice arInvoice, Integer AD_BRNCH, Integer companyCode)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArMiscReceipSyncControllerBean addArDrEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(COA_CODE);

            if (DR_CLSS.equals("DISCOUNT") || DR_CLSS.equals("FOREX")) {
                System.out.println("DR_CLSS: " + DR_CLSS);
                LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(AD_BRNCH);
                String newCoa = adBranch.getBrCoaSegment();
                StringTokenizer st = new StringTokenizer(glChartOfAccount.getCoaAccountNumber(), "-");
                int ctr = 0;
                while (st.hasMoreTokens()) {
                    ctr++;
                    if (ctr >= 3) {
                        newCoa = newCoa + "-" + st.nextToken();
                    } else {
                        st.nextToken();
                    }
                }

                glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(newCoa, companyCode);

            }

            // create distribution record

            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, DR_RVRSD, companyCode);

            //arInvoice.addArDistributionRecord(arDistributionRecord);
            arDistributionRecord.setArInvoice(arInvoice);
            //glChartOfAccount.addArDistributionRecord(arDistributionRecord);
            arDistributionRecord.setGlChartOfAccount(glChartOfAccount);

        }
        catch (FinderException ex) {
            ex.printStackTrace();
            throw new GlobalBranchAccountNumberInvalidException(ex.getMessage());

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }

    private double convertByUomFromAndItemAndQuantity(
            LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, double ADJST_QTY, Integer companyCode) {

        Debug.print("ArMiscReceiptSyncControllerBean convertByUomFromAndItemAndQuantity");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), companyCode);

            return EJBCommon.roundIt(ADJST_QTY * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), adPreference.getPrfInvQuantityPrecisionUnit());


        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }

    private String getBranchTaxCOA(String BR_BRNCH_CODE, String segment1, String segment3, String segment4, String segment5) {

        Debug.print("ArMiscReceiptSyncControllerBean getBranchTaxCOA");

        String segment2 = "";

        System.out.println("BR_BRNCH_CODE: " + BR_BRNCH_CODE);

        if (BR_BRNCH_CODE.equals("Manila-Smoking Lounge")) {
            segment1 = "MA";
            segment2 = "SL";
        } else if (BR_BRNCH_CODE.equals("Manila-Cigar Shop")) {
            segment1 = "MA";
            segment2 = "CS";
        } else if (BR_BRNCH_CODE.equals("Manila-Term.#2 Domestic")) {
            segment1 = "MA";
            segment2 = "TD";
        } else if (BR_BRNCH_CODE.equals("Manila-Term.#2 Intl")) {
            segment1 = "MA";
            segment2 = "TI";
        } else if (BR_BRNCH_CODE.equals("Cebu-Banilad")) {
            segment1 = "CE";
            segment2 = "BA";
        } else if (BR_BRNCH_CODE.equals("Cebu-Gorordo")) {
            segment1 = "CE";
            segment2 = "GO";
        } else if (BR_BRNCH_CODE.equals("Cebu-Mactan Domestic")) {
            segment1 = "CE";
            segment2 = "MD";
        } else if (BR_BRNCH_CODE.equals("Cebu-Mactan Intl")) {
            segment1 = "CE";
            segment2 = "MI";
        } else if (BR_BRNCH_CODE.equals("Manila-Zara Marketing")) {
            segment1 = "MA";
            segment2 = "ZA";
        } else if (BR_BRNCH_CODE.equals("Cebu-Supercat")) {
            segment1 = "CE";
            segment2 = "SU";
        } else {
            segment1 = "HQ";
            segment2 = "HQ";
        }

        System.out.println("segment1: " + segment1);
        System.out.println("segment2: " + segment2);

        return segment1 + "-" + segment2 + "-" + segment3 + "-" + segment4 + "-" + segment5;
    }

    private short getGlFcPrecisionUnit(Integer companyCode) {

        Debug.print("ArMiscReceiptSyncControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }

    private double getInvFifoCost(Date CST_DT, Integer IL_CODE, double CST_QTY, double CST_COST,
                                  boolean isAdjustFifo, Integer AD_BRNCH, Integer AD_CMPNY) {

        try {

            Collection invFifoCostings = invCostingHome.findFifoRemainingQuantityByLessThanOrEqualCstDateAndIlCodeAndBrCode(CST_DT, IL_CODE, AD_BRNCH, AD_CMPNY);

            if (invFifoCostings.size() > 0) {

                Iterator x = invFifoCostings.iterator();

                if (isAdjustFifo) {

                    //executed during POST transaction

                    double totalCost = 0d;
                    double cost;

                    if (CST_QTY < 0) {

                        //for negative quantities
                        double neededQty = -(CST_QTY);

                        while (x.hasNext() && neededQty != 0) {

                            LocalInvCosting invFifoCosting = (LocalInvCosting) x.next();

                            if (invFifoCosting.getApPurchaseOrderLine() != null || invFifoCosting.getApVoucherLineItem() != null) {
                                cost = invFifoCosting.getCstItemCost() / invFifoCosting.getCstQuantityReceived();
                            } else if (invFifoCosting.getArInvoiceLineItem() != null) {
                                cost = invFifoCosting.getCstCostOfSales() / invFifoCosting.getCstQuantitySold();
                            } else if (invFifoCosting.getInvBuildUnbuildAssemblyLine() != null) {
                                cost = invFifoCosting.getCstAssemblyCost() / invFifoCosting.getCstAssemblyQuantity();
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

                        //if needed qty is not yet satisfied but no more quantities to fetch, get the default cost
                        if (neededQty != 0) {

                            LocalInvItemLocation invItemLocation = invItemLocationHome.findByPrimaryKey(IL_CODE);
                            totalCost += (neededQty * invItemLocation.getInvItem().getIiUnitCost());
                        }

                        cost = totalCost / -CST_QTY;
                    } else {

                        //for positive quantities
                        cost = CST_COST;
                    }
                    return cost;
                } else {

                    //executed during ENTRY transaction

                    LocalInvCosting invFifoCosting = (LocalInvCosting) x.next();

                    if (invFifoCosting.getApPurchaseOrderLine() != null || invFifoCosting.getApVoucherLineItem() != null) {
                        return EJBCommon.roundIt(invFifoCosting.getCstItemCost() / invFifoCosting.getCstQuantityReceived(), this.getGlFcPrecisionUnit(AD_CMPNY));
                    } else if (invFifoCosting.getArInvoiceLineItem() != null) {
                        return EJBCommon.roundIt(invFifoCosting.getCstCostOfSales() / invFifoCosting.getCstQuantitySold(), this.getGlFcPrecisionUnit(AD_CMPNY));
                    } else if (invFifoCosting.getInvBuildUnbuildAssemblyLine() != null) {
                        return EJBCommon.roundIt(invFifoCosting.getCstAssemblyCost() / invFifoCosting.getCstAssemblyQuantity(), this.getGlFcPrecisionUnit(AD_CMPNY));
                    } else {
                        return EJBCommon.roundIt(invFifoCosting.getCstAdjustCost() / invFifoCosting.getCstAdjustQuantity(), this.getGlFcPrecisionUnit(AD_CMPNY));
                    }
                }
            } else {

                //most applicable in 1st entries of data
                LocalInvItemLocation invItemLocation = invItemLocationHome.findByPrimaryKey(IL_CODE);
                return invItemLocation.getInvItem().getIiUnitCost();
            }

        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private ArModReceiptDetails receiptDecode(String receipt) throws Exception {

        Debug.print("ArMiscReceiptSyncControllerBean receiptDecode");

        String separator = "$";
        ArModReceiptDetails arModReceiptDetails = new ArModReceiptDetails();


        // Remove first $ character
        receipt = receipt.substring(1);

        // customer code
        int start = 0;
        int nextIndex = receipt.indexOf(separator, start);
        int length = nextIndex - start;
        arModReceiptDetails.setRctCstCustomerCode(receipt.substring(start, start + length));

        // payment type / TAX CODE
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctTcName(receipt.substring(start, start + length));

        // or number
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctNumber(receipt.substring(start, start + length));
        System.out.println("or-" + receipt.substring(start, start + length));

        // receipt source
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;

        // currency
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctFcName(receipt.substring(start, start + length));

        // currency rate
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctConversionRate(Double.parseDouble(receipt.substring(start, start + length)));

        // cash amount
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctPosCashAmount(Double.parseDouble(receipt.substring(start, start + length)));

        // check amount
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctPosCheckAmount(Double.parseDouble(receipt.substring(start, start + length)));

        // card amount
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctPosCardAmount(Double.parseDouble(receipt.substring(start, start + length)));

        // total amount
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctPosTotalAmount(Double.parseDouble(receipt.substring(start, start + length)));

        // discount
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctPosDiscount(Double.parseDouble(receipt.substring(start, start + length)));

        // void
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;

        // void reason
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;

        // void amount
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctPosVoidAmount(Double.parseDouble(receipt.substring(start, start + length)));

        // date
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        sdf.setLenient(false);
        try {
            arModReceiptDetails.setRctDate(sdf.parse(receipt.substring(start, start + length)));
            System.out.println("DAte-" + arModReceiptDetails.getRctDate());
        }
        catch (Exception ex) {

            throw ex;
        }

        // location
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;

        /*
         * RctPaymentMethod is Used Temporarily for Payment Term Field
         */
        // payment method / Payment Term
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctPaymentMethod(receipt.substring(start, start + length));

        // sc amount
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        try {
            arModReceiptDetails.setRctPosScAmount(Double.parseDouble(receipt.substring(start, start + length)));
        }
        catch (Exception ex) {

        }

        // dc amount
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        try {
            arModReceiptDetails.setRctPosDcAmount(Double.parseDouble(receipt.substring(start, start + length)));
        }
        catch (Exception ex) {

        }

        // on account
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctPosOnAccount((byte) Integer.parseInt(receipt.substring(start, start + length)));

        // end separator
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;

        String lineSeparator = "~";
        // begin lineSeparator
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(lineSeparator, start);
        length = nextIndex - start;

        ArrayList invIliList = new ArrayList();

        while (true) {

            ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = new ArModInvoiceLineItemDetails();

            // begin separator
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);
            length = nextIndex - start;

            // line number
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineItemDetails.setIliLine(Short.parseShort(receipt.substring(start, start + length)));

            // item name
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineItemDetails.setIliIiName(receipt.substring(start, start + length));
            System.out.println(arModInvoiceLineItemDetails.getIliIiName());
            // quantity
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineItemDetails.setIliQuantity(Double.parseDouble(receipt.substring(start, start + length)));

            // price
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineItemDetails.setIliUnitPrice(Double.parseDouble(receipt.substring(start, start + length)));

            // total
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineItemDetails.setIliAmount(Double.parseDouble(receipt.substring(start, start + length)));

            // uom
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineItemDetails.setIliUomName(receipt.substring(start, start + length));

            // location
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineItemDetails.setIliLocName(receipt.substring(start, start + length));

            invIliList.add(arModInvoiceLineItemDetails);

            // begin lineSeparator
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(lineSeparator, start);
            length = nextIndex - start;

            int tempStart = nextIndex + 1;
            if (receipt.indexOf(separator, tempStart) == -1) {
                break;
            }

        }

        arModReceiptDetails.setInvIliList(invIliList);

        return arModReceiptDetails;

    }

    private ArModReceiptDetails receiptDecodeUS(String receipt) throws Exception {

        Debug.print("ArMiscReceiptSyncControllerBean receiptDecode");


        String separator = "$";
        ArModReceiptDetails arModReceiptDetails = new ArModReceiptDetails();


        // Remove first $ character
        receipt = receipt.substring(1);

        // customer code
        int start = 0;
        int nextIndex = receipt.indexOf(separator, start);
        int length = nextIndex - start;
        arModReceiptDetails.setRctCstCustomerCode(receipt.substring(start, start + length));

        // payment type / TAX CODE
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctTcName(receipt.substring(start, start + length));

        // or number
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctNumber(receipt.substring(start, start + length));
        System.out.println("or-" + receipt.substring(start, start + length));

        // receipt source
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctSource(receipt.substring(start, start + length));

        // currency
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctFcName(receipt.substring(start, start + length));

        // currency rate
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctConversionRate(Double.parseDouble(receipt.substring(start, start + length)));

        // cash amount
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctPosCashAmount(Double.parseDouble(receipt.substring(start, start + length)));

        // check amount
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctPosCheckAmount(Double.parseDouble(receipt.substring(start, start + length)));

        // card amount
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctPosCardAmount(Double.parseDouble(receipt.substring(start, start + length)));

        // total amount
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctPosTotalAmount(Double.parseDouble(receipt.substring(start, start + length)));

        // discount
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctPosDiscount(Double.parseDouble(receipt.substring(start, start + length)));

        // void
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;

        // void reason
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;

        // void amount
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctPosVoidAmount(Double.parseDouble(receipt.substring(start, start + length)));

        // date
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        sdf.setLenient(false);
        try {
            arModReceiptDetails.setRctDate(sdf.parse(receipt.substring(start, start + length)));
            System.out.println("DAte-" + arModReceiptDetails.getRctDate());
        }
        catch (Exception ex) {

            throw ex;
        }

        // location
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;

        /*
         * RctPaymentMethod is Used Temporarily for Payment Term Field
         */
        // payment method / Payment Term
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctPaymentMethod(receipt.substring(start, start + length));

        // sc amount
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        try {
            arModReceiptDetails.setRctPosScAmount(Double.parseDouble(receipt.substring(start, start + length)));
        }
        catch (Exception ex) {

        }

        // dc amount
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        try {
            arModReceiptDetails.setRctPosDcAmount(Double.parseDouble(receipt.substring(start, start + length)));
        }
        catch (Exception ex) {

        }

        // on account
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctPosOnAccount((byte) Integer.parseInt(receipt.substring(start, start + length)));

        // end separator
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;

        String lineSeparator = "~";
        // begin lineSeparator
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(lineSeparator, start);
        length = nextIndex - start;

        ArrayList invIliList = new ArrayList();

        while (true) {

            ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = new ArModInvoiceLineItemDetails();

            // begin separator
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);
            length = nextIndex - start;

            // line number
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineItemDetails.setIliLine(Short.parseShort(receipt.substring(start, start + length)));

            // item name
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineItemDetails.setIliIiName(receipt.substring(start, start + length));
            System.out.println(arModInvoiceLineItemDetails.getIliIiName());
            // quantity
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineItemDetails.setIliQuantity(Double.parseDouble(receipt.substring(start, start + length)));

            // price
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineItemDetails.setIliUnitPrice(Double.parseDouble(receipt.substring(start, start + length)));

            // total
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineItemDetails.setIliAmount(Double.parseDouble(receipt.substring(start, start + length)));

            // uom
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineItemDetails.setIliUomName(receipt.substring(start, start + length));

            // location
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineItemDetails.setIliLocName(receipt.substring(start, start + length));

            // location
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineItemDetails.setIliTaxCode(receipt.substring(start, start + length));

            invIliList.add(arModInvoiceLineItemDetails);

            // begin lineSeparator
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(lineSeparator, start);
            length = nextIndex - start;

            int tempStart = nextIndex + 1;
            if (receipt.indexOf(separator, tempStart) == -1) {
                break;
            }

        }

        arModReceiptDetails.setInvIliList(invIliList);

        return arModReceiptDetails;

    }

    private ArModReceiptDetails receiptDecodeWithExpiryDate(String receipt) throws Exception {

        Debug.print("ArMiscReceiptSyncControllerBean receiptDecodeWithExpiryDate");

        ///USED METHOD ENCODE
        String separator = "$";
        ArModReceiptDetails arModReceiptDetails = new ArModReceiptDetails();


        // Remove first $ character
        receipt = receipt.substring(1);

        // customer code
        int start = 0;
        int nextIndex = receipt.indexOf(separator, start);
        int length = nextIndex - start;

        arModReceiptDetails.setRctCstCustomerCode(receipt.substring(start, start + length));
        System.out.println("CUSTOMER CODE:" + receipt.substring(start, start + length));

        // payment type
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;

        // or number
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctNumber(receipt.substring(start, start + length));
        System.out.println("setRctNumber=" + receipt.substring(start, start + length));

        // pos name
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctPOSName(receipt.substring(start, start + length));
        System.out.println("setRctPOSName=" + receipt.substring(start, start + length));

        // receipt source
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        System.out.println("SOURCE:" + receipt.substring(start, start + length));
        length = nextIndex - start;

        // currency
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctFcName(receipt.substring(start, start + length));
        System.out.println("setRctFcName:" + receipt.substring(start, start + length));

        // currency rate
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctConversionRate(Double.parseDouble(receipt.substring(start, start + length)));
        System.out.println("setRctConversionRate=" + receipt.substring(start, start + length));

        // cash amount
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctPosCashAmount(Double.parseDouble(receipt.substring(start, start + length)));
        System.out.println("setRctPosCashAmount=" + receipt.substring(start, start + length));

        // check amount
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctPosCheckAmount(Double.parseDouble(receipt.substring(start, start + length)));
        System.out.println("setRctPosCheckAmount=" + receipt.substring(start, start + length));

        // card amount
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctPosCardAmount(Double.parseDouble(receipt.substring(start, start + length)));
        System.out.println("setRctPosCardAmount=" + receipt.substring(start, start + length));

        // total amount
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctPosTotalAmount(Double.parseDouble(receipt.substring(start, start + length)));
        System.out.println("setRctPosTotalAmount=" + receipt.substring(start, start + length));

        // discount
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctPosDiscount(Double.parseDouble(receipt.substring(start, start + length)));
        System.out.println("setRctPosDiscount=" + receipt.substring(start, start + length));

        // void
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;

        // void reason
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;

        // void amount
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctPosVoidAmount(Double.parseDouble(receipt.substring(start, start + length)));

        // date
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        sdf.setLenient(false);
        try {
            arModReceiptDetails.setRctDate(sdf.parse(receipt.substring(start, start + length)));
            System.out.println("setRctDate=" + arModReceiptDetails.getRctDate());
        }
        catch (Exception ex) {

            throw ex;
        }

        // location
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);

        length = nextIndex - start;
        System.out.println("Location:" + receipt.substring(start, start + length));

        // payment method
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);

        length = nextIndex - start;
        System.out.println("payment method:" + receipt.substring(start, start + length));

        // sc amount
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);

        length = nextIndex - start;
        System.out.println("sc amount:" + receipt.substring(start, start + length));
        try {
            arModReceiptDetails.setRctPosScAmount(Double.parseDouble(receipt.substring(start, start + length)));
        }
        catch (Exception ex) {

        }

        // dc amount
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);

        length = nextIndex - start;
        System.out.println("dc amnt:" + receipt.substring(start, start + length));
        try {
            arModReceiptDetails.setRctPosDcAmount(Double.parseDouble(receipt.substring(start, start + length)));
        }
        catch (Exception ex) {

        }

        // on account
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        System.out.println("on account:" + receipt.substring(start, start + length));
        arModReceiptDetails.setRctPosOnAccount((byte) Integer.parseInt(receipt.substring(start, start + length) == "True" ? "1" : "0"));

        // vat code name
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        System.out.println("on account:" + receipt.substring(start, start + length));
        arModReceiptDetails.setRctTcName(receipt.substring(start, start + length));

        // CUSTOMER NAME / NOTE
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        System.out.println("CUSTOMER NAME" + receipt.substring(start, start + length));
        arModReceiptDetails.setRctCstName(receipt.substring(start, start + length));


        // SALESPERSON

        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        System.out.println("SALESPERSON" + receipt.substring(start, start + length));
        arModReceiptDetails.setRctSlpSalespersonCode(receipt.substring(start, start + length));

        //	arModReceiptDetails.setRctSlpSalespersonCode("");

        // CASH AMOUNT
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        System.out.println("CASH AMOUNT" + receipt.substring(start, start + length));
        arModReceiptDetails.setRctAmountCash(Double.parseDouble(receipt.substring(start, start + length)));


        // CARD NUMBER 1
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        System.out.println("CARD NO 1" + receipt.substring(start, start + length));
        arModReceiptDetails.setRctCardNumber1(receipt.substring(start, start + length));


        // CARD TYPE 1
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        System.out.println("CARD TYPE 1" + receipt.substring(start, start + length));
        arModReceiptDetails.setRctCardType1(receipt.substring(start, start + length));


        // CARD AMOUNT 1
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        System.out.println("CARD AMOUNT 1" + receipt.substring(start, start + length));
        arModReceiptDetails.setRctAmountCard1(Double.parseDouble(receipt.substring(start, start + length)));

        // CARD NUMBER 2
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        System.out.println("CARD NO 2" + receipt.substring(start, start + length));
        arModReceiptDetails.setRctCardNumber2(receipt.substring(start, start + length));


        // CARD TYPE 2
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        System.out.println("CARD TYPE 2" + receipt.substring(start, start + length));
        arModReceiptDetails.setRctCardType2(receipt.substring(start, start + length));


        // CARD AMOUNT 2
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        System.out.println("CARD AMOUNT 2" + receipt.substring(start, start + length));
        arModReceiptDetails.setRctAmountCard2(Double.parseDouble(receipt.substring(start, start + length)));

        // CARD NUMBER 3
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        System.out.println("CARD NO 3 " + receipt.substring(start, start + length));
        arModReceiptDetails.setRctCardNumber3(receipt.substring(start, start + length));

        // CARD TYPE 3
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        System.out.println("CARD TYPE 3" + receipt.substring(start, start + length));
        arModReceiptDetails.setRctCardType3(receipt.substring(start, start + length));

        // CARD AMOUNT 3
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        System.out.println("CARD AMOUNT 3" + receipt.substring(start, start + length));
        arModReceiptDetails.setRctAmountCard3(Double.parseDouble(receipt.substring(start, start + length)));

        //CHEQUE NUMBER
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        System.out.println("CHEQUE NO  " + receipt.substring(start, start + length));
        arModReceiptDetails.setRctChequeNumber(receipt.substring(start, start + length));


        //CHEQUE AMOUNT
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        System.out.println("CHEQUE AMOUNT " + receipt.substring(start, start + length));
        arModReceiptDetails.setRctAmountCheque(Double.parseDouble(receipt.substring(start, start + length)));

        //VOUCHER NUMBER
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        System.out.println("VOUCHER NO " + receipt.substring(start, start + length));
        arModReceiptDetails.setRctVoucherNumber(receipt.substring(start, start + length));

        //VOUCHER AMOUNT
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        System.out.println("VOUCHER AMOUNT " + receipt.substring(start, start + length));
        arModReceiptDetails.setRctAmountVoucher(Double.parseDouble(receipt.substring(start, start + length)));

        // end separator
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;

        String lineSeparator = "~";
        // begin lineSeparator
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(lineSeparator, start);
        length = nextIndex - start;

        ArrayList invIliList = new ArrayList();

        while (true) {
            String asdf = "";
            String asdf2 = "";
            ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = new ArModInvoiceLineItemDetails();

            // begin separator
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);
            length = nextIndex - start;

            // line number
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);
            length = nextIndex - start;

            arModInvoiceLineItemDetails.setIliLine(Short.parseShort(receipt.substring(start, start + length)));

            // item code

            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineItemDetails.setIliCode(Integer.parseInt(receipt.substring(start, start + length)));
            System.out.println("receiptDecodeWithExpiryfate getIliIiName()" + arModInvoiceLineItemDetails.getIliIiName());
            // item name
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineItemDetails.setIliIiName(receipt.substring(start, start + length));
            System.out.println(arModInvoiceLineItemDetails.getIliIiName());
            // quantity
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineItemDetails.setIliQuantity(Double.parseDouble(receipt.substring(start, start + length)));

            // price
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineItemDetails.setIliUnitPrice(Double.parseDouble(receipt.substring(start, start + length)));

            // total
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineItemDetails.setIliAmount(Double.parseDouble(receipt.substring(start, start + length)));

            // total Discount
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineItemDetails.setIliTotalDiscount(Double.parseDouble(receipt.substring(start, start + length)));


            // uom
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineItemDetails.setIliUomName(receipt.substring(start, start + length));

            // EMEI no use
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineItemDetails.setILI_IMEI(receipt.substring(start, start + length));

            // location
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineItemDetails.setIliLocName(receipt.substring(start, start + length));


//			Expiry Date / IMEI
            start = nextIndex + 1;
            nextIndex = receipt.indexOf("$~", start);
            length = nextIndex - start;

            asdf = receipt.substring(start);
            //MessageBox.Show("asdf: " + asdf);
            if (asdf != "~~") {
                asdf = asdf.substring(1, asdf.length() - 2);

                //OLD CODE
                asdf2 = expiryDates2("$" + asdf + "fin$");
                System.out.println("KABOOM!    " + asdf2);

                //arModInvoiceLineItemDetails.setIliMisc(asdf2);
                arModInvoiceLineItemDetails.setILI_IMEI(asdf2);
                System.out.println("asdf2: " + asdf2);
                //

                //NEW CODE
                System.out.println("asdf2: " + asdf);
                ArrayList tagList = this.convertMiscToInvModTagList(asdf);
                arModInvoiceLineItemDetails.setIliTagList(tagList);
            }
            invIliList.add(arModInvoiceLineItemDetails);

            // begin lineSeparator
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(lineSeparator, start);
            length = nextIndex - start;

            int tempStart = nextIndex + 1;
            if (receipt.indexOf(separator, tempStart) == -1) {
                break;
            }

        }

        arModReceiptDetails.setInvIliList(invIliList);

        return arModReceiptDetails;

    }

    private String expiryDates2(String misc) throws Exception {

        Debug.print("ApReceivingItemControllerBean getExpiryDates");
        System.out.println("misc: " + misc);
        String separator = "$";


        // Remove first $ character
        misc = misc.substring(1);

        // Counter
        int start = 0;
        int nextIndex = misc.indexOf(separator, start);
        int length = nextIndex - start;

        ArrayList miscList = new ArrayList();
        String checker = "";
        String checker2 = "";
        int qty = 0;
        try {
            while (!checker.equals("fin")) {

                // Date
                start = nextIndex + 1;
                nextIndex = misc.indexOf(separator, start);
                length = nextIndex - start;

                checker = misc.substring(start, start + length);
                System.out.println("checker: " + checker);
                if (checker.length() == 0 || checker.equals("~")) {
                    break;
                }
                if (checker.equals("fin")) {
                    break;
                }
                if (checker.length() != 0 || checker != "null") {
                    miscList.add(checker);
                    checker2 = checker2 + "$" + checker;
                    qty++;
                } else {
                    miscList.add("null");
                }
            }
        }
        catch (Exception e) {
            qty = qty - 1;
        }
        String retExp = "";
        if (qty != 0) {
            retExp = "$" + qty + checker2 + "$";
        }

        //System.out.println("qty :" + qty);
        return retExp;
    }

    private void executeArRctPost(Integer RCT_CODE, String USR_NM, Integer AD_BRNCH, Integer AD_CMPNY) throws
            GlobalRecordAlreadyDeletedException,
            GlobalTransactionAlreadyPostedException,
            GlobalTransactionAlreadyVoidPostedException,
            GlJREffectiveDateNoPeriodExistException,
            GlJREffectiveDatePeriodClosedException,
            GlobalJournalNotBalanceException,
            AdPRFCoaGlVarianceAccountNotFoundException,
            GlobalExpiryDateNotFoundException {

        Debug.print("ArMiscReceiptSyncControllerBean executeArRctPost");

        LocalArReceipt arReceipt;

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            // validate if receipt is already deleted

            try {

                arReceipt = arReceiptHome.findByPrimaryKey(RCT_CODE);

            }
            catch (FinderException ex) {

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

                this.regenerateInventoryDr(arReceipt, AD_BRNCH, AD_CMPNY);

            }

            // post receipt

            if (arReceipt.getRctVoid() == EJBCommon.FALSE && arReceipt.getRctPosted() == EJBCommon.FALSE) {


                if (arReceipt.getRctType().equals("MISC") && !arReceipt.getArInvoiceLineItems().isEmpty()) {

                    Iterator c = arReceipt.getArInvoiceLineItems().iterator();

                    while (c.hasNext()) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) c.next();

                        String II_NM = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName();
                        String LOC_NM = arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName();

                        double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(),
                                arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), AD_CMPNY);

                        LocalInvCosting invCosting = null;

                        try {

                            System.out.println("Sync RCT date is : " + arReceipt.getRctDate());


                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arReceipt.getRctDate(), II_NM, LOC_NM, AD_BRNCH, AD_CMPNY);

                        }
                        catch (FinderException ex) {
                            System.out.println("Sync RCT date not found");
                        }

                        double COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                        if (invCosting == null) {


                            this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(),
                                    QTY_SLD, COST * QTY_SLD,
                                    -QTY_SLD, -COST * QTY_SLD,
                                    0d, null, AD_BRNCH, AD_CMPNY);

                        } else {


                            if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Average")) {

                                double avgCost = invCosting.getCstRemainingQuantity() == 0 ? COST :
                                        Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                                this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(),
                                        QTY_SLD, avgCost * QTY_SLD,
                                        invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (avgCost * QTY_SLD),
                                        0d, null, AD_BRNCH, AD_CMPNY);

                            } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("FIFO")) {

                                double fifoCost = invCosting.getCstRemainingQuantity() == 0 ? COST :
                                        this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(),
                                                QTY_SLD, arInvoiceLineItem.getIliUnitPrice(), true, AD_BRNCH, AD_CMPNY);

                                this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(),
                                        QTY_SLD, fifoCost * QTY_SLD,
                                        invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (fifoCost * QTY_SLD),
                                        0d, null, AD_BRNCH, AD_CMPNY);

                            } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Standard")) {

                                double standardCost = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();

                                this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(),
                                        QTY_SLD, standardCost * QTY_SLD,
                                        invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (standardCost * QTY_SLD),
                                        0d, null, AD_BRNCH, AD_CMPNY);
                            }

                        }


                    }

                }

                // increase bank balance CASH
                if (arReceipt.getRctAmountCash() > 0) {

                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccount().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", AD_CMPNY);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(
                                        arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCash(), "BOOK", AD_CMPNY);

                                //adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCash());

                            }

                        } else {

                            // create new balance
                            System.out.println("arReceipt.getRctAmountCash()=" + arReceipt.getRctAmountCash());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(
                                    arReceipt.getRctDate(), (arReceipt.getRctAmountCash()), "BOOK", AD_CMPNY);

                            //adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);

                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", AD_CMPNY);

                        Iterator i = adBankAccountBalances.iterator();

                        while (i.hasNext()) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) i.next();

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCash());

                        }

                    }
                    catch (Exception ex) {

                        ex.printStackTrace();

                    }
                }

                // increase bank balance CARD 1
                if (arReceipt.getRctAmountCard1() > 0) {


                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccountCard1().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard1().getBaCode(), "BOOK", AD_CMPNY);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(
                                        arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard1(), "BOOK", AD_CMPNY);

                                //adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard1());

                            }

                        } else {

                            // create new balance
                            System.out.println("arReceipt.getRctAmountCard1()=" + arReceipt.getRctAmountCard1());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(
                                    arReceipt.getRctDate(), (arReceipt.getRctAmountCard1()), "BOOK", AD_CMPNY);

                            //adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);

                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard1().getBaCode(), "BOOK", AD_CMPNY);

                        Iterator i = adBankAccountBalances.iterator();

                        while (i.hasNext()) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) i.next();

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard1());

                        }

                    }
                    catch (Exception ex) {

                        ex.printStackTrace();

                    }

                }

//increase bank balance CARD 2
                if (arReceipt.getRctAmountCard2() > 0) {
                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccountCard2().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard2().getBaCode(), "BOOK", AD_CMPNY);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(
                                        arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard2(), "BOOK", AD_CMPNY);

                                //adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard2());

                            }

                        } else {

                            // create new balance
                            System.out.println("arReceipt.getRctAmountCard2()=" + arReceipt.getRctAmountCard2());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(
                                    arReceipt.getRctDate(), (arReceipt.getRctAmountCard2()), "BOOK", AD_CMPNY);

                            //adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);

                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard2().getBaCode(), "BOOK", AD_CMPNY);

                        Iterator i = adBankAccountBalances.iterator();

                        while (i.hasNext()) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) i.next();

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard2());

                        }

                    }
                    catch (Exception ex) {

                        ex.printStackTrace();

                    }
                }

                //increase bank balance CARD 3
                if (arReceipt.getRctAmountCard3() > 0) {
                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccountCard3().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard3().getBaCode(), "BOOK", AD_CMPNY);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(
                                        arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard3(), "BOOK", AD_CMPNY);

                                //adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard3());

                            }

                        } else {

                            // create new balance
                            System.out.println("arReceipt.getRctAmountCard3()=" + arReceipt.getRctAmountCard3());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(
                                    arReceipt.getRctDate(), (arReceipt.getRctAmountCard3()), "BOOK", AD_CMPNY);

                            //adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);

                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard3().getBaCode(), "BOOK", AD_CMPNY);

                        Iterator i = adBankAccountBalances.iterator();

                        while (i.hasNext()) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) i.next();

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard3());

                        }

                    }
                    catch (Exception ex) {

                        ex.printStackTrace();

                    }
                }
                // increase bank balance CHEQUE
                if (arReceipt.getRctAmountCheque() > 0) {
                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccount().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", AD_CMPNY);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(
                                        arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCheque(), "BOOK", AD_CMPNY);

                                //adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCheque());

                            }

                        } else {

                            // create new balance
                            System.out.println("arReceipt.getRctAmountCheque()=" + arReceipt.getRctAmountCheque());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(
                                    arReceipt.getRctDate(), (arReceipt.getRctAmountCheque()), "BOOK", AD_CMPNY);

                            //adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);

                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", AD_CMPNY);

                        Iterator i = adBankAccountBalances.iterator();

                        while (i.hasNext()) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) i.next();

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCheque());

                        }

                    }
                    catch (Exception ex) {

                        ex.printStackTrace();

                    }
                }

                // set receipt post status

                arReceipt.setRctPosted(EJBCommon.TRUE);
                arReceipt.setRctPostedBy(USR_NM);
                arReceipt.setRctDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());


            }

            // post to gl if necessary

            if (adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {
                System.out.println(arReceipt.getRctReferenceNumber() + "  Reference Number");
                // validate if date has no period and period is closed

                LocalGlSetOfBook glJournalSetOfBook = null;

                try {

                    glJournalSetOfBook = glSetOfBookHome.findByDate(arReceipt.getRctDate(), AD_CMPNY);

                }
                catch (FinderException ex) {

                    throw new GlJREffectiveDateNoPeriodExistException();

                }

                LocalGlAccountingCalendarValue glAccountingCalendarValue =
                        glAccountingCalendarValueHome.findByAcCodeAndDate(
                                glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), arReceipt.getRctDate(), AD_CMPNY);


                if (glAccountingCalendarValue.getAcvStatus() == 'N' ||
                        glAccountingCalendarValue.getAcvStatus() == 'C' ||
                        glAccountingCalendarValue.getAcvStatus() == 'P') {

                    throw new GlJREffectiveDatePeriodClosedException();

                }

                // check if invoice is balance if not check suspense posting

                LocalGlJournalLine glOffsetJournalLine = null;

                Collection arDistributionRecords = null;

                if (arReceipt.getRctVoid() == EJBCommon.FALSE) {

                    arDistributionRecords = arDistributionRecordHome.findImportableDrByDrReversedAndRctCode(EJBCommon.FALSE, arReceipt.getRctCode(), AD_CMPNY);

                } else {

                    arDistributionRecords = arDistributionRecordHome.findImportableDrByDrReversedAndRctCode(EJBCommon.TRUE, arReceipt.getRctCode(), AD_CMPNY);

                }


                Iterator j = arDistributionRecords.iterator();

                double TOTAL_DEBIT = 0d;
                double TOTAL_CREDIT = 0d;

                while (j.hasNext()) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) j.next();

                    double DR_AMNT = 0d;

                    if (arDistributionRecord.getArAppliedInvoice() != null) {

                        LocalArInvoice arInvoice = arDistributionRecord.getArAppliedInvoice().getArInvoicePaymentSchedule().getArInvoice();

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(),
                                arInvoice.getGlFunctionalCurrency().getFcName(),
                                arInvoice.getInvConversionDate(),
                                arInvoice.getInvConversionRate(),
                                arDistributionRecord.getDrAmount(), AD_CMPNY);

                    } else {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(),
                                arReceipt.getGlFunctionalCurrency().getFcName(),
                                arReceipt.getRctConversionDate(),
                                arReceipt.getRctConversionRate(),
                                arDistributionRecord.getDrAmount(), AD_CMPNY);

                    }

                    if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                        System.out.println("TOTAL DEBIT to add : " + DR_AMNT);
                        TOTAL_DEBIT += DR_AMNT;

                    } else {
                        System.out.println("TOTAL Credit to add : " + DR_AMNT);
                        TOTAL_CREDIT += DR_AMNT;

                    }


                }

                TOTAL_DEBIT = EJBCommon.roundIt(TOTAL_DEBIT, adCompany.getGlFunctionalCurrency().getFcPrecision());
                TOTAL_CREDIT = EJBCommon.roundIt(TOTAL_CREDIT, adCompany.getGlFunctionalCurrency().getFcPrecision());

                if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.TRUE &&
                        TOTAL_DEBIT != TOTAL_CREDIT) {

                    LocalGlSuspenseAccount glSuspenseAccount = null;

                    try {

                        glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("ACCOUNTS RECEIVABLES", "SALES RECEIPTS", AD_CMPNY);

                    }
                    catch (FinderException ex) {

                        throw new GlobalJournalNotBalanceException();

                    }

                    if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                        glOffsetJournalLine = glJournalLineHome.create((short) (arDistributionRecords.size() + 1),
                                EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", AD_CMPNY);

                    } else {

                        glOffsetJournalLine = glJournalLineHome.create((short) (arDistributionRecords.size() + 1),
                                EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", AD_CMPNY);

                    }

                    LocalGlChartOfAccount glChartOfAccount = glSuspenseAccount.getGlChartOfAccount();
                    //glChartOfAccount.addGlJournalLine(glOffsetJournalLine);
                    glOffsetJournalLine.setGlChartOfAccount(glChartOfAccount);


                } else if (TOTAL_DEBIT > 9999999999d || TOTAL_CREDIT > 9999999999d || (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE &&
                        TOTAL_DEBIT != TOTAL_CREDIT)) {
                    throw new GlobalJournalNotBalanceException();

                }

                // create journal batch if necessary

                LocalGlJournalBatch glJournalBatch = null;
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");

                try {

                    if (adPreference.getPrfEnableArReceiptBatch() == EJBCommon.TRUE) {

                        glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " " + arReceipt.getArReceiptBatch().getRbName(), AD_BRNCH, AD_CMPNY);

                    } else {

                        glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " SALES RECEIPTS", AD_BRNCH, AD_CMPNY);

                    }


                }
                catch (FinderException ex) {
                }

                if (adPreference.getPrfEnableGlJournalBatch() == EJBCommon.TRUE &&
                        glJournalBatch == null) {

                    if (adPreference.getPrfEnableArReceiptBatch() == EJBCommon.TRUE) {

                        glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " " + arReceipt.getArReceiptBatch().getRbName(), "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, AD_BRNCH, AD_CMPNY);

                    } else {

                        glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " SALES RECEIPTS", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, AD_BRNCH, AD_CMPNY);

                    }

                }

                // create journal entry
                String customerName = arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() :
                        arReceipt.getRctCustomerName();

                LocalGlJournal glJournal = glJournalHome.create(arReceipt.getRctReferenceNumber(),
                        arReceipt.getRctDescription(), arReceipt.getRctDate(),
                        0.0d, null, arReceipt.getRctNumber(), null, 1d, "N/A", null,
                        'N', EJBCommon.TRUE, EJBCommon.FALSE,
                        USR_NM, new Date(),
                        USR_NM, new Date(),
                        null, null,
                        USR_NM, EJBCommon.getGcCurrentDateWoTime().getTime(),
                        arReceipt.getArCustomer().getCstTin(),
                        customerName, EJBCommon.FALSE,
                        null,
                        AD_BRNCH, AD_CMPNY);

                LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("ACCOUNTS RECEIVABLES", AD_CMPNY);
                glJournal.setGlJournalSource(glJournalSource);

                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), AD_CMPNY);
                glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

                LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("SALES RECEIPTS", AD_CMPNY);
                glJournal.setGlJournalCategory(glJournalCategory);

                if (glJournalBatch != null) {

                    glJournal.setGlJournalBatch(glJournalBatch);

                }


                // create journal lines

                j = arDistributionRecords.iterator();

                while (j.hasNext()) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) j.next();

                    double DR_AMNT = 0d;

                    LocalArInvoice arInvoice = null;

                    if (arDistributionRecord.getArAppliedInvoice() != null) {

                        arInvoice = arDistributionRecord.getArAppliedInvoice().getArInvoicePaymentSchedule().getArInvoice();

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(),
                                arInvoice.getGlFunctionalCurrency().getFcName(),
                                arInvoice.getInvConversionDate(),
                                arInvoice.getInvConversionRate(),
                                arDistributionRecord.getDrAmount(), AD_CMPNY);

                    } else {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(),
                                arReceipt.getGlFunctionalCurrency().getFcName(),
                                arReceipt.getRctConversionDate(),
                                arReceipt.getRctConversionRate(),
                                arDistributionRecord.getDrAmount(), AD_CMPNY);

                    }

                    LocalGlJournalLine glJournalLine = glJournalLineHome.create(arDistributionRecord.getDrLine(),
                            arDistributionRecord.getDrDebit(), DR_AMNT, "", AD_CMPNY);

                    glJournalLine.setGlChartOfAccount(arDistributionRecord.getGlChartOfAccount());

                    glJournalLine.setGlJournal(glJournal);

                    arDistributionRecord.setDrImported(EJBCommon.TRUE);

                    // for FOREX revaluation

                    int FC_CODE = arDistributionRecord.getArAppliedInvoice() != null ?
                            arInvoice.getGlFunctionalCurrency().getFcCode().intValue() :
                            arReceipt.getGlFunctionalCurrency().getFcCode().intValue();

                    if ((FC_CODE != adCompany.getGlFunctionalCurrency().getFcCode().intValue()) &&
                            glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null && (FC_CODE ==
                            glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode().intValue())) {

                        double CONVERSION_RATE = arDistributionRecord.getArAppliedInvoice() != null ?
                                arInvoice.getInvConversionRate() : arReceipt.getRctConversionRate();

                        Date DATE = arDistributionRecord.getArAppliedInvoice() != null ?
                                arInvoice.getInvConversionDate() : arReceipt.getRctConversionDate();

                        if (DATE != null && (CONVERSION_RATE == 0 || CONVERSION_RATE == 1)) {

                            CONVERSION_RATE = this.getFrRateByFrNameAndFrDate(
                                    glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcName(),
                                    glJournal.getJrConversionDate(), AD_CMPNY);

                        } else if (CONVERSION_RATE == 0) {

                            CONVERSION_RATE = 1;

                        }

                        Collection glForexLedgers = null;

                        DATE = arDistributionRecord.getArAppliedInvoice() != null ?
                                arInvoice.getInvDate() : arReceipt.getRctDate();

                        try {

                            glForexLedgers = glForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(
                                    DATE, glJournalLine.getGlChartOfAccount().getCoaCode(),
                                    AD_CMPNY);

                        }
                        catch (FinderException ex) {

                        }

                        LocalGlForexLedger glForexLedger =
                                (glForexLedgers.isEmpty() || glForexLedgers == null) ? null :
                                        (LocalGlForexLedger) glForexLedgers.iterator().next();

                        int FRL_LN = (glForexLedger != null &&
                                glForexLedger.getFrlDate().compareTo(DATE) == 0) ?
                                glForexLedger.getFrlLine().intValue() + 1 : 1;

                        // compute balance
                        double COA_FRX_BLNC = glForexLedger == null ? 0 : glForexLedger.getFrlBalance();
                        double FRL_AMNT = arDistributionRecord.getDrAmount();

                        if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET")) {
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                        } else {
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);
                        }

                        COA_FRX_BLNC = COA_FRX_BLNC + FRL_AMNT;

                        glForexLedger = glForexLedgerHome.create(DATE, FRL_LN, "OR", FRL_AMNT,
                                CONVERSION_RATE, COA_FRX_BLNC, 0d, AD_CMPNY);

                        //glJournalLine.getGlChartOfAccount().addGlForexLedger(glForexLedger);
                        glForexLedger.setGlChartOfAccount(glJournalLine.getGlChartOfAccount());

                        // propagate balances
                        try {

                            glForexLedgers = glForexLedgerHome.findByGreaterThanFrlDateAndCoaCode(
                                    glForexLedger.getFrlDate(), glForexLedger.getGlChartOfAccount().getCoaCode(),
                                    glForexLedger.getFrlAdCompany());

                        }
                        catch (FinderException ex) {

                        }

                        Iterator itrFrl = glForexLedgers.iterator();

                        while (itrFrl.hasNext()) {

                            glForexLedger = (LocalGlForexLedger) itrFrl.next();
                            FRL_AMNT = arDistributionRecord.getDrAmount();

                            if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET")) {
                                FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT :
                                        (-1 * FRL_AMNT));
                            } else {
                                FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) :
                                        FRL_AMNT);
                            }

                            glForexLedger.setFrlBalance(glForexLedger.getFrlBalance() + FRL_AMNT);

                        }

                    }

                }

                if (glOffsetJournalLine != null) {

                    //glJournal.addGlJournalLine(glOffsetJournalLine);
                    glOffsetJournalLine.setGlJournal(glJournal);
                }

                // post journal to gl

                Collection glJournalLines = glJournal.getGlJournalLines();

                Iterator i = glJournalLines.iterator();

                while (i.hasNext()) {

                    LocalGlJournalLine glJournalLine = (LocalGlJournalLine) i.next();

                    // post current to current acv

                    this.postToGl(glAccountingCalendarValue,
                            glJournalLine.getGlChartOfAccount(),
                            true, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), AD_CMPNY);


                    // post to subsequent acvs (propagate)

                    Collection glSubsequentAccountingCalendarValues =
                            glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(
                                    glJournalSetOfBook.getGlAccountingCalendar().getAcCode(),
                                    glAccountingCalendarValue.getAcvPeriodNumber(), AD_CMPNY);

                    Iterator acvsIter = glSubsequentAccountingCalendarValues.iterator();

                    while (acvsIter.hasNext()) {

                        LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue =
                                (LocalGlAccountingCalendarValue) acvsIter.next();

                        this.postToGl(glSubsequentAccountingCalendarValue,
                                glJournalLine.getGlChartOfAccount(),
                                false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), AD_CMPNY);

                    }

                    // post to subsequent years if necessary

                    Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), AD_CMPNY);

                    if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                        adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
                        LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome.findByCoaAccountNumber(adCompany.getCmpRetainedEarnings(), AD_CMPNY);

                        Iterator sobIter = glSubsequentSetOfBooks.iterator();

                        while (sobIter.hasNext()) {

                            LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) sobIter.next();

                            String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                            // post to subsequent acvs of subsequent set of book(propagate)

                            Collection glAccountingCalendarValues =
                                    glAccountingCalendarValueHome.findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), AD_CMPNY);

                            Iterator acvIter = glAccountingCalendarValues.iterator();

                            while (acvIter.hasNext()) {

                                LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue =
                                        (LocalGlAccountingCalendarValue) acvIter.next();

                                if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY") ||
                                        ACCOUNT_TYPE.equals("OWNERS EQUITY")) {

                                    this.postToGl(glSubsequentAccountingCalendarValue,
                                            glJournalLine.getGlChartOfAccount(),
                                            false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), AD_CMPNY);

                                } else { // revenue & expense

                                    this.postToGl(glSubsequentAccountingCalendarValue,
                                            glRetainedEarningsAccount,
                                            false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), AD_CMPNY);

                                }

                            }

                            if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) {
                                break;
                            }

                        }

                    }


                }

            }

        }
        catch (GlJREffectiveDateNoPeriodExistException ex) {

            //getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlJREffectiveDatePeriodClosedException ex) {

            //getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlobalJournalNotBalanceException ex) {

            //getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlobalRecordAlreadyDeletedException ex) {

            //getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlobalTransactionAlreadyPostedException ex) {

            //getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlobalTransactionAlreadyVoidPostedException ex) {

            //getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (AdPRFCoaGlVarianceAccountNotFoundException ex) {

            throw ex;

        }
        catch (GlobalExpiryDateNotFoundException ex) {

            //getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            //getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }

    private void regenerateInventoryDr(LocalArReceipt arReceipt, Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException {

        Debug.print("ArMiscReceiptSyncControllerBean regenerateInventoryDr");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            // regenerate inventory distribution records

            Collection arDistributionRecords = null;

            if (arReceipt.getRctVoid() == EJBCommon.FALSE) {

                arDistributionRecords = arDistributionRecordHome.findImportableDrByDrReversedAndRctCode(EJBCommon.FALSE, arReceipt.getRctCode(), AD_CMPNY);

            } else {

                arDistributionRecords = arDistributionRecordHome.findImportableDrByDrReversedAndRctCode(EJBCommon.TRUE, arReceipt.getRctCode(), AD_CMPNY);

            }

            Iterator i = arDistributionRecords.iterator();

            while (i.hasNext()) {

                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

                if (arDistributionRecord.getDrClass().equals("COGS") || arDistributionRecord.getDrClass().equals("INVENTORY")) {

                    i.remove();
                    //arDistributionRecord.remove();

                }

            }

            Collection arInvoiceLineItems = arReceipt.getArInvoiceLineItems();

            if (arInvoiceLineItems != null && !arInvoiceLineItems.isEmpty()) {

                i = arInvoiceLineItems.iterator();

                while (i.hasNext()) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) i.next();
                    LocalInvItemLocation invItemLocation = arInvoiceLineItem.getInvItemLocation();
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(
                                arReceipt.getRctDate(), invItemLocation.getInvItem().getIiName(),
                                invItemLocation.getInvLocation().getLocName(), AD_BRNCH, AD_CMPNY);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }

                    // add cost of sales distribution and inventory


                    double COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                    try {

                        LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                arReceipt.getRctDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, AD_CMPNY);


                        if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Average")) {
                            COST = invCosting.getCstRemainingQuantity() <= 0 ? COST :
                                    Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                        }

                        if (COST <= 0) {
                            COST = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                        } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("FIFO")) {
                            COST = invCosting.getCstRemainingQuantity() == 0 ? COST :
                                    Math.abs(this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(),
                                            arInvoiceLineItem.getIliQuantity(), arInvoiceLineItem.getIliUnitPrice(), false, AD_BRNCH, AD_CMPNY));
                        } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Standard")) {
                            COST = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                        }

                    }
                    catch (FinderException ex) {
                    }

                    double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(),
                            arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), AD_CMPNY);

                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {

                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arInvoiceLineItem.getInvItemLocation().getIlCode(), AD_BRNCH, AD_CMPNY);

                    }
                    catch (FinderException ex) {

                    }

                    if (arInvoiceLineItem.getIliEnableAutoBuild() == EJBCommon.FALSE && arInvoiceLineItem.getInvItemLocation().getInvItem().getIiNonInventoriable() == EJBCommon.FALSE) {

                        if (adBranchItemLocation != null) {

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(),
                                    "COGS", EJBCommon.TRUE, COST * QTY_SLD,
                                    adBranchItemLocation.getBilCoaGlCostOfSalesAccount(), arReceipt, AD_BRNCH, AD_CMPNY);

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(),
                                    "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD,
                                    adBranchItemLocation.getBilCoaGlInventoryAccount(), arReceipt, AD_BRNCH, AD_CMPNY);

                        } else {

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(),
                                    "COGS", EJBCommon.TRUE, COST * QTY_SLD,
                                    arInvoiceLineItem.getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arReceipt, AD_BRNCH, AD_CMPNY);

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(),
                                    "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD,
                                    arInvoiceLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(), arReceipt, AD_BRNCH, AD_CMPNY);

                        }

                    }


                }
            }

        }
        catch (GlobalInventoryDateException ex) {

            //getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlobalBranchAccountNumberInvalidException ex) {

            //getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            //getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }

    private void postToInv(LocalArInvoiceLineItem arInvoiceLineItem, Date CST_DT, double CST_QTY_SLD, double CST_CST_OF_SLS,
                           double CST_RMNNG_QTY, double CST_RMNNG_VL, double CST_VRNC_VL, String USR_NM, Integer AD_BRNCH, Integer AD_CMPNY) throws
            AdPRFCoaGlVarianceAccountNotFoundException,
            GlobalExpiryDateNotFoundException {

        Debug.print("ArMiscReceiptSyncControllerBean postToInv");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            LocalInvItemLocation invItemLocation = arInvoiceLineItem.getInvItemLocation();
            int CST_LN_NMBR = 0;

            CST_QTY_SLD = EJBCommon.roundIt(CST_QTY_SLD, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_CST_OF_SLS = EJBCommon.roundIt(CST_CST_OF_SLS, adCompany.getGlFunctionalCurrency().getFcPrecision());
            CST_RMNNG_QTY = EJBCommon.roundIt(CST_RMNNG_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_RMNNG_VL = EJBCommon.roundIt(CST_RMNNG_VL, adCompany.getGlFunctionalCurrency().getFcPrecision());

            if (CST_QTY_SLD > 0 && arInvoiceLineItem.getIliEnableAutoBuild() == EJBCommon.FALSE) {

                invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - CST_QTY_SLD);

            }

            try {

                // generate line number
                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, AD_CMPNY);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            }
            catch (FinderException ex) {

                CST_LN_NMBR = 1;

            }

            //void subsequent cost variance adjustments
            Collection invAdjustmentLines = invAdjustmentLineHome.findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(
                    CST_DT, invItemLocation.getIlCode(), AD_BRNCH, AD_CMPNY);
            Iterator i = invAdjustmentLines.iterator();

            while (i.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();
                this.voidInvAdjustment(invAdjustmentLine.getInvAdjustment(), AD_BRNCH, AD_CMPNY);

            }

            String prevExpiryDates = "";
            String miscListPrpgt = "";
            double qtyPrpgt = 0;
            try {
                LocalInvCosting prevCst = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndRemainingQuantityNotEqualToZeroAndIlCode(
                        CST_DT, invItemLocation.getIlCode(), AD_BRNCH, AD_CMPNY);
                Debug.print("ArReceiptPostControllerBean postToInv B");
                prevExpiryDates = prevCst.getCstExpiryDate();
                qtyPrpgt = prevCst.getCstRemainingQuantity();

                if (prevExpiryDates == null) {
                    prevExpiryDates = "";
                }

            }
            catch (Exception ex) {
                System.out.println("prevExpiryDates CATCH: " + prevExpiryDates);
                prevExpiryDates = "";
            }

            // create costing
            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d, 0d, 0d, 0d, 0d, CST_QTY_SLD, CST_CST_OF_SLS, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, 0d, AD_BRNCH, AD_CMPNY);
            //invItemLocation.addInvCosting(invCosting);
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setArInvoiceLineItem(arInvoiceLineItem);

            // Get Latest Expiry Dates
            if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiTraceMisc() != 0) {
                if (prevExpiryDates != null && prevExpiryDates != "" && prevExpiryDates.length() != 0) {
                    if (arInvoiceLineItem.getIliMisc() != null && arInvoiceLineItem.getIliMisc() != "" && arInvoiceLineItem.getIliMisc().length() != 0) {
                        int qty2Prpgt = Integer.parseInt(this.getQuantityExpiryDates(arInvoiceLineItem.getIliMisc()));

                        String miscList2Prpgt = this.propagateExpiryDates(arInvoiceLineItem.getIliMisc(), qty2Prpgt, "False");
                        ArrayList miscList = this.expiryDates(arInvoiceLineItem.getIliMisc(), qty2Prpgt);
                        String propagateMiscPrpgt = "";
                        String ret = "";
                        String exp = "";
                        String Checker = "";

                        Iterator mi = miscList.iterator();

                        propagateMiscPrpgt = prevExpiryDates;
                        ret = propagateMiscPrpgt;
                        while (mi.hasNext()) {
                            String miscStr = (String) mi.next();

                            Integer qTest = this.checkExpiryDates(ret + "fin$");
                            ArrayList miscList2 = this.expiryDates("$" + ret, Double.parseDouble(qTest.toString()));

                            //ArrayList miscList2 = this.expiryDates("$" + ret, qtyPrpgt);
                            Iterator m2 = miscList2.iterator();
                            ret = "";
                            String ret2 = "false";
                            int a = 0;
                            while (m2.hasNext()) {
                                String miscStr2 = (String) m2.next();

                                if (ret2 == "1st") {
                                    ret2 = "false";
                                }
                                System.out.println("miscStr: " + miscStr);
                                System.out.println("miscStr2: " + miscStr2);

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
                                System.out.println("Checker: " + Checker);
                                System.out.println("ret2: " + ret2);
                                if (!miscStr2.equals(miscStr) || a > 1) {
                                    if ((ret2 != "1st") && ((ret2 == "false") || (ret2 == "true"))) {
                                        if (miscStr2 != "") {
                                            miscStr2 = "$" + miscStr2;
                                            ret = ret + miscStr2;
                                            System.out.println("ret " + ret);
                                            ret2 = "false";
                                        }
                                    }
                                }

                            }
                            if (ret != "") {
                                ret = ret + "$";
                            }
                            System.out.println("ret una: " + ret);
                            exp = exp + ret;
                            qtyPrpgt = qtyPrpgt - 1;
                        }
                        System.out.println("ret fin " + ret);
                        System.out.println("exp fin " + exp);
                        //propagateMiscPrpgt = propagateMiscPrpgt.replace(" ", "$");
                        propagateMiscPrpgt = ret;
                        //propagateMiscPrpgt = this.propagateExpiryDates(propagateMiscPrpgt, a, "True");
                        if (Checker == "true") {
                            //invCosting.setCstExpiryDate(propagateMiscPrpgt);
                        } else {
                            System.out.println("Exp Not Found");
                            throw new GlobalExpiryDateNotFoundException(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                        }
                        invCosting.setCstExpiryDate(propagateMiscPrpgt);
                    } else {
                        invCosting.setCstExpiryDate(prevExpiryDates);
                    }

                } else {
                    if (arInvoiceLineItem.getIliMisc() != null && arInvoiceLineItem.getIliMisc() != "" && arInvoiceLineItem.getIliMisc().length() != 0) {
                        int initialQty = Integer.parseInt(this.getQuantityExpiryDates(arInvoiceLineItem.getIliMisc()));
                        String initialPrpgt = this.propagateExpiryDates(arInvoiceLineItem.getIliMisc(), initialQty, "False");

                        invCosting.setCstExpiryDate(initialPrpgt);
                    } else {
                        invCosting.setCstExpiryDate(prevExpiryDates);
                    }

                }
            }


            // if cost variance is not 0, generate cost variance for the transaction
            if (CST_VRNC_VL != 0) {

                this.generateCostVariance(invCosting.getInvItemLocation(), CST_VRNC_VL,
                        "APMR" + arInvoiceLineItem.getArReceipt().getRctNumber(),
                        arInvoiceLineItem.getArReceipt().getRctDescription(), arInvoiceLineItem.getArReceipt().getRctDate(),
                        USR_NM, AD_BRNCH, AD_CMPNY);

            }

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, AD_CMPNY);
            String miscList = "";
            ArrayList miscList2 = null;
            i = invCostings.iterator();
            String propagateMisc = "";
            String ret = "";
            while (i.hasNext()) {
                String Checker = "";
                String Checker2 = "";

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) i.next();

                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() - CST_QTY_SLD);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() - CST_CST_OF_SLS);

                if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiTraceMisc() != 0) {
                    if (arInvoiceLineItem.getIliMisc() != null && arInvoiceLineItem.getIliMisc() != "" && arInvoiceLineItem.getIliMisc().length() != 0) {
                        double qty = Double.parseDouble(this.getQuantityExpiryDates(arInvoiceLineItem.getIliMisc()));
                        //invPropagatedCosting.getInvAdjustmentLine().getAlMisc();
                        miscList = this.propagateExpiryDates(arInvoiceLineItem.getIliMisc(), qty, "False");
                        miscList2 = this.expiryDates(arInvoiceLineItem.getIliMisc(), qty);
                        System.out.println("arInvoiceLineItem.getIliMisc(): " + arInvoiceLineItem.getIliMisc());
                        System.out.println("getAlAdjustQuantity(): " + arInvoiceLineItem.getIliQuantity());

                        if (arInvoiceLineItem.getIliQuantity() < 0) {
                            Iterator mi = miscList2.iterator();

                            propagateMisc = invPropagatedCosting.getCstExpiryDate();
                            ret = invPropagatedCosting.getCstExpiryDate();
                            while (mi.hasNext()) {
                                String miscStr = (String) mi.next();

                                Integer qTest = this.checkExpiryDates(ret + "fin$");
                                ArrayList miscList3 = this.expiryDates("$" + ret, Double.parseDouble(qTest.toString()));

                                // ArrayList miscList3 = this.expiryDates("$" + ret, qtyPrpgt);
                                System.out.println("ret: " + ret);
                                Iterator m2 = miscList3.iterator();
                                ret = "";
                                String ret2 = "false";
                                int a = 0;
                                while (m2.hasNext()) {
                                    String miscStr2 = (String) m2.next();

                                    if (ret2 == "1st") {
                                        ret2 = "false";
                                    }
                                    System.out.println("2 miscStr: " + miscStr);
                                    System.out.println("2 miscStr2: " + miscStr2);
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
                                    System.out.println("Checker: " + Checker2);
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
                                }

                                ret = ret + "$";
                                qtyPrpgt = qtyPrpgt - 1;
                            }
                        }

                    }


                    if (arInvoiceLineItem.getIliMisc() != null && arInvoiceLineItem.getIliMisc() != "" && arInvoiceLineItem.getIliMisc().length() != 0) {
                        if (prevExpiryDates != null && prevExpiryDates != "" && prevExpiryDates.length() != 0) {

                            Iterator mi = miscList2.iterator();

                            propagateMisc = invPropagatedCosting.getCstExpiryDate();
                            ret = propagateMisc;
                            while (mi.hasNext()) {
                                String miscStr = (String) mi.next();

                                Integer qTest = this.checkExpiryDates(ret + "fin$");
                                ArrayList miscList3 = this.expiryDates("$" + ret, Double.parseDouble(qTest.toString()));

                                // ArrayList miscList3 = this.expiryDates("$" + ret, qtyPrpgt);
                                System.out.println("ret: " + ret);
                                Iterator m2 = miscList3.iterator();
                                ret = "";
                                String ret2 = "false";
                                int a = 0;
                                while (m2.hasNext()) {
                                    String miscStr2 = (String) m2.next();

                                    if (ret2 == "1st") {
                                        ret2 = "false";
                                    }
                                    System.out.println("2 miscStr: " + miscStr);
                                    System.out.println("2 miscStr2: " + miscStr2);
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
                                    System.out.println("Checker: " + Checker);
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
                        }

                    } else {
                        try {
                            propagateMisc = miscList + invPropagatedCosting.getCstExpiryDate().substring(1, invPropagatedCosting.getCstExpiryDate().length());
                        }
                        catch (Exception e) {
                            propagateMisc = miscList;
                        }
                    }
                    invPropagatedCosting.setCstExpiryDate(propagateMisc);
                }

            }

            if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                // regenerate cost variance
                this.regenerateCostVariance(invCostings, invCosting, AD_BRNCH, AD_CMPNY);
            }

        }
        catch (AdPRFCoaGlVarianceAccountNotFoundException ex) {

            throw ex;

        }
        catch (GlobalExpiryDateNotFoundException ex) {
            throw ex;
        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM,
                                                      Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("ArMiscReceiptSyncControllerBean convertForeignToFunctionalCurrency");

        LocalAdCompany adCompany = null;


        // get company and extended precision

        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

        }
        catch (Exception ex) {

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

                    glReceiptFunctionalCurrencyRate =
                            glFunctionalCurrencyRateHome.findByFcCodeAndDate(FC_CODE,
                                    CONVERSION_DATE, AD_CMPNY);

                    AMOUNT = AMOUNT * glReceiptFunctionalCurrencyRate.getFrXToUsd();

                }

                // Get set of book functional currency rate if necessary

                if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                    LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate =
                            glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().
                                    getFcCode(), CONVERSION_DATE, AD_CMPNY);

                    AMOUNT = AMOUNT / glCompanyFunctionalCurrencyRate.getFrXToUsd();

                }


            }
            catch (Exception ex) {

                throw new EJBException(ex.getMessage());

            }

        }

        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());

    }

    private ArrayList expiryDates(String misc, double qty) throws Exception {

        Debug.print("ApReceivingItemControllerBean getExpiryDates");
        System.out.println("misc: " + misc);
        String separator = "$";


        // Remove first $ character
        misc = misc.substring(1);

        // Counter
        int start = 0;
        int nextIndex = misc.indexOf(separator, start);
        int length = nextIndex - start;

        System.out.println("qty" + qty);
        ArrayList miscList = new ArrayList();

        for (int x = 0; x < qty; x++) {

            // Date
            start = nextIndex + 1;
            nextIndex = misc.indexOf(separator, start);
            length = nextIndex - start;
            System.out.println("x" + x);
            String checker = misc.substring(start, start + length);
            System.out.println("checker" + checker);
            if (checker.length() != 0 || checker != "null") {
                miscList.add(checker);
            } else {
                miscList.add("null");
            }
        }

        System.out.println("miscList :" + miscList);
        return miscList;
    }

    public String propagateExpiryDates(String misc, double qty, String reverse) throws Exception {
        //ActionErrors errors = new ActionErrors();
        Debug.print("ApReceivingItemControllerBean getExpiryDates");
        System.out.println("misc: " + misc);

        String separator = "";
        if (reverse == "False") {
            separator = "$";
        } else {
            separator = " ";
        }

        // Remove first $ character
        misc = misc.substring(1);
        System.out.println("misc: " + misc);
        // Counter
        int start = 0;
        int nextIndex = misc.indexOf(separator, start);
        int length = nextIndex - start;

        String miscList = new String();

        for (int x = 0; x < qty; x++) {

            // Date
            start = nextIndex + 1;
            nextIndex = misc.indexOf(separator, start);
            length = nextIndex - start;
            String g = misc.substring(start, start + length);
            System.out.println("g: " + g);
            System.out.println("g length: " + g.length());
            if (g.length() != 0) {
                miscList = miscList + "$" + g;
                System.out.println("miscList G: " + miscList);
            }
        }

        miscList = miscList + "$";
        System.out.println("miscList :" + miscList);
        return (miscList);
    }

    public String getQuantityExpiryDates(String qntty) {

        String separator = "$";
        String y = "";
        try {
//     		Remove first $ character
            qntty = qntty.substring(1);

            // Counter
            int start = 0;
            int nextIndex = qntty.indexOf(separator, start);
            int length = nextIndex - start;

            y = (qntty.substring(start, start + length));
            System.out.println("Y " + y);
        }
        catch (Exception e) {
            y = "0";
        }


        return y;
    }

    private void generateCostVariance(LocalInvItemLocation invItemLocation, double CST_VRNC_VL, String ADJ_RFRNC_NMBR, String ADJ_DSCRPTN, Date ADJ_DT, String USR_NM, Integer AD_BRNCH,
                                      Integer AD_CMPNY) throws
            AdPRFCoaGlVarianceAccountNotFoundException {
     	/*
     	Debug.print("ArMiscReceiptEntryControllerBean generateCostVariance");

     	LocalAdPreferenceHome adPreferenceHome = null;
     	LocalGlChartOfAccountHome glChartOfAccountHome = null;
     	LocalAdBranchItemLocationHome adBranchItemLocationHome = null;

     	// Initialize EJB Home

     	try {

     		adPreferenceHome = (LocalAdPreferenceHome)EJBHomeFactory.
 			lookUpLocalHome(LocalAdPreferenceHome.JNDI_NAME, LocalAdPreferenceHome.class);
     		glChartOfAccountHome = (LocalGlChartOfAccountHome)EJBHomeFactory.
 			lookUpLocalHome(LocalGlChartOfAccountHome.JNDI_NAME, LocalGlChartOfAccountHome.class);
     		adBranchItemLocationHome = (LocalAdBranchItemLocationHome)EJBHomeFactory.
 			lookUpLocalHome(LocalAdBranchItemLocationHome.JNDI_NAME, LocalAdBranchItemLocationHome.class);


     	} catch (NamingException ex) {

     		throw new EJBException(ex.getMessage());

     	}

     	try{

     		LocalInvAdjustment newInvAdjustment = this.saveInvAdjustment(ADJ_RFRNC_NMBR, ADJ_DSCRPTN, ADJ_DT, USR_NM, AD_BRNCH,
     				AD_CMPNY);
     		LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
     		LocalGlChartOfAccount glCoaVarianceAccount = null;

     		if(adPreference.getPrfInvGlCoaVarianceAccount() == null)
     			throw new AdPRFCoaGlVarianceAccountNotFoundException();

     		try{

     			glCoaVarianceAccount = glChartOfAccountHome.findByPrimaryKey(adPreference.getPrfInvGlCoaVarianceAccount());
     			//glCoaVarianceAccount.addInvAdjustment(newInvAdjustment);
     			newInvAdjustment.setGlChartOfAccount(glCoaVarianceAccount);

     		} catch (FinderException ex) {

     			throw new AdPRFCoaGlVarianceAccountNotFoundException();

     		}

     		LocalInvAdjustmentLine invAdjustmentLine = this.addInvAlEntry(invItemLocation, newInvAdjustment, CST_VRNC_VL,
     				EJBCommon.FALSE, AD_CMPNY);

     		// check for branch mapping

     		LocalAdBranchItemLocation adBranchItemLocation = null;

     		try{

     			adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(
     					invAdjustmentLine.getInvItemLocation().getIlCode(), AD_BRNCH, AD_CMPNY);

     		} catch (FinderException ex) {

     		}

     		LocalGlChartOfAccount glInventoryChartOfAccount = null;

     		if (adBranchItemLocation == null) {

     			glInventoryChartOfAccount = glChartOfAccountHome.findByPrimaryKey(
     					invAdjustmentLine.getInvItemLocation().getIlGlCoaInventoryAccount());
     		} else {

     			glInventoryChartOfAccount = glChartOfAccountHome.findByPrimaryKey(
     					adBranchItemLocation.getBilCoaGlInventoryAccount());

     		}


     		boolean isDebit = CST_VRNC_VL < 0 ? false : true;

     		//inventory dr
     		this.addInvDrEntry(newInvAdjustment.getInvDrNextLine(), "INVENTORY",
     				isDebit == true ? EJBCommon.TRUE : EJBCommon.FALSE, Math.abs(CST_VRNC_VL), EJBCommon.FALSE,
     						glInventoryChartOfAccount.getCoaCode(), newInvAdjustment, AD_BRNCH, AD_CMPNY);

     		//variance dr
     		this.addInvDrEntry(newInvAdjustment.getInvDrNextLine(),"VARIANCE",
     				!isDebit == true ? EJBCommon.TRUE : EJBCommon.FALSE, Math.abs(CST_VRNC_VL), EJBCommon.FALSE,
     						glCoaVarianceAccount.getCoaCode(), newInvAdjustment, AD_BRNCH, AD_CMPNY);

     		this.executeInvAdjPost(newInvAdjustment.getAdjCode(), newInvAdjustment.getAdjLastModifiedBy(), AD_BRNCH,
     				AD_CMPNY);

     	} catch (AdPRFCoaGlVarianceAccountNotFoundException ex) {

     		getSessionContext().setRollbackOnly();
     		throw ex;

     	} catch (Exception ex) {

     		Debug.printStackTrace(ex);
     		getSessionContext().setRollbackOnly();
     		throw new EJBException(ex.getMessage());

     	}
     	*/
    }

    private void regenerateCostVariance(Collection invCostings, LocalInvCosting invCosting, Integer AD_BRNCH, Integer AD_CMPNY)
            throws AdPRFCoaGlVarianceAccountNotFoundException {
    		    	/*
    		    	Debug.print("ArMiscReceiptEntryControllerBean regenerateCostVariance");

    		    	try {

    		    		Iterator i = invCostings.iterator();
    		    		LocalInvCosting prevInvCosting = invCosting;

    		    		while (i.hasNext()) {

    		    			LocalInvCosting invPropagatedCosting = (LocalInvCosting)i.next();

    		    			Debug.print("ArMiscReceiptEntryControllerBean regenerateCostVariance A");
    		    			if(prevInvCosting.getCstRemainingQuantity() < 0) {

    		    				double TTL_CST = 0;
    		    				double QNTY = 0;
    		    				String ADJ_RFRNC_NMBR = "";
    		    				String ADJ_DSCRPTN = "";
    		    				String ADJ_CRTD_BY = "";

    		    				// get unit cost adjusment, document number and unit of measure
    		    				if (invPropagatedCosting.getApPurchaseOrderLine() != null) {

    		    					TTL_CST = invPropagatedCosting.getApPurchaseOrderLine().getPlAmount();
    								QNTY =  this.convertByUomFromAndItemAndQuantity(
    										invPropagatedCosting.getApPurchaseOrderLine().getInvUnitOfMeasure(),
    										invPropagatedCosting.getApPurchaseOrderLine().getInvItemLocation().getInvItem(),
    										invPropagatedCosting.getApPurchaseOrderLine().getPlQuantity(), AD_CMPNY);
    								Debug.print("ArMiscReceiptEntryControllerBean regenerateCostVariance B");
    		    					ADJ_DSCRPTN = invPropagatedCosting.getApPurchaseOrderLine().getApPurchaseOrder().getPoDescription();
    		    					ADJ_CRTD_BY = invPropagatedCosting.getApPurchaseOrderLine().getApPurchaseOrder().getPoPostedBy();
    		    					ADJ_RFRNC_NMBR = "APRI" +
    								invPropagatedCosting.getApPurchaseOrderLine().getApPurchaseOrder().getPoDocumentNumber();

    		    				} else if (invPropagatedCosting.getApVoucherLineItem() != null){

    		    					TTL_CST = invPropagatedCosting.getApVoucherLineItem().getVliAmount();
    								QNTY = this.convertByUomFromAndItemAndQuantity(
    										invPropagatedCosting.getApVoucherLineItem().getInvUnitOfMeasure(),
    										invPropagatedCosting.getApVoucherLineItem().getInvItemLocation().getInvItem(),
    										invPropagatedCosting.getApVoucherLineItem().getVliQuantity(), AD_CMPNY);
    								Debug.print("ArMiscReceiptEntryControllerBean regenerateCostVariance C");
    		    					if (invPropagatedCosting.getApVoucherLineItem().getApVoucher() != null) {

    		    						ADJ_DSCRPTN = invPropagatedCosting.getApVoucherLineItem().getApVoucher().getVouDescription();
    		    						ADJ_CRTD_BY = invPropagatedCosting.getApVoucherLineItem().getApVoucher().getVouPostedBy();
    		    						ADJ_RFRNC_NMBR = "APVOU" +
    									invPropagatedCosting.getApVoucherLineItem().getApVoucher().getVouDocumentNumber();

    		    					} else if (invPropagatedCosting.getApVoucherLineItem().getApCheck() != null) {

    		    						ADJ_DSCRPTN = invPropagatedCosting.getApVoucherLineItem().getApCheck().getChkDescription();
    		    						ADJ_CRTD_BY = invPropagatedCosting.getApVoucherLineItem().getApCheck().getChkPostedBy();
    		    						ADJ_RFRNC_NMBR = "APCHK" +
    									invPropagatedCosting.getApVoucherLineItem().getApCheck().getChkDocumentNumber();

    		    					}

    		    				} else if (invPropagatedCosting.getArInvoiceLineItem() != null){

    		    					QNTY = this.convertByUomFromAndItemAndQuantity(
    										invPropagatedCosting.getArInvoiceLineItem().getInvUnitOfMeasure(),
    										invPropagatedCosting.getArInvoiceLineItem().getInvItemLocation().getInvItem(),
    										invPropagatedCosting.getArInvoiceLineItem().getIliQuantity(), AD_CMPNY);
    		   						TTL_CST = prevInvCosting.getCstRemainingValue() - invPropagatedCosting.getCstRemainingValue();
    		   						Debug.print("ArMiscReceiptEntryControllerBean regenerateCostVariance D");
    		   						if(invPropagatedCosting.getArInvoiceLineItem().getArInvoice() != null){

    		   							ADJ_DSCRPTN = invPropagatedCosting.getArInvoiceLineItem().getArInvoice().getInvDescription();
    		   							ADJ_CRTD_BY = invPropagatedCosting.getArInvoiceLineItem().getArInvoice().getInvPostedBy();
    		   							ADJ_RFRNC_NMBR = "ARCM" +
    									invPropagatedCosting.getArInvoiceLineItem().getArInvoice().getInvNumber();

    		   						} else if(invPropagatedCosting.getArInvoiceLineItem().getArReceipt() != null){

    		   							ADJ_DSCRPTN = invPropagatedCosting.getArInvoiceLineItem().getArReceipt().getRctDescription();
    		   							ADJ_CRTD_BY = invPropagatedCosting.getArInvoiceLineItem().getArReceipt().getRctPostedBy();
    		   							ADJ_RFRNC_NMBR = "ARMR" +
    									invPropagatedCosting.getArInvoiceLineItem().getArReceipt().getRctNumber();

    		   						}

    		    				} else if (invPropagatedCosting.getArSalesOrderInvoiceLine() != null){

    		   						TTL_CST = prevInvCosting.getCstRemainingValue() - invPropagatedCosting.getCstRemainingValue();
    		    					QNTY = this.convertByUomFromAndItemAndQuantity(
    										invPropagatedCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getInvUnitOfMeasure(),
    										invPropagatedCosting.getArSalesOrderInvoiceLine().getArSalesOrderLine().getInvItemLocation().getInvItem(),
    										invPropagatedCosting.getArSalesOrderInvoiceLine().getSilQuantityDelivered(), AD_CMPNY);
    		    					Debug.print("ArMiscReceiptEntryControllerBean regenerateCostVariance E");
    		    					ADJ_DSCRPTN = invPropagatedCosting.getArSalesOrderInvoiceLine().getArInvoice().getInvDescription();
    		    					ADJ_CRTD_BY = invPropagatedCosting.getArSalesOrderInvoiceLine().getArInvoice().getInvPostedBy();
    		    					ADJ_RFRNC_NMBR = "ARCM" +
    								invPropagatedCosting.getArSalesOrderInvoiceLine().getArInvoice().getInvNumber();

    		    				} else if (invPropagatedCosting.getInvAdjustmentLine() != null){

    		    					ADJ_DSCRPTN = invPropagatedCosting.getInvAdjustmentLine().getInvAdjustment().getAdjDescription();
    		    					ADJ_CRTD_BY = invPropagatedCosting.getInvAdjustmentLine().getInvAdjustment().getAdjPostedBy();
    		    					ADJ_RFRNC_NMBR = "INVADJ" +
    								invPropagatedCosting.getInvAdjustmentLine().getInvAdjustment().getAdjDocumentNumber();

    		    					if(invPropagatedCosting.getInvAdjustmentLine().getAlAdjustQuantity() != 0) {

    		    						TTL_CST = (invPropagatedCosting.getInvAdjustmentLine().getAlUnitCost() *
    		    								invPropagatedCosting.getInvAdjustmentLine().getAlAdjustQuantity());
    		    						QNTY =  this.convertByUomFromAndItemAndQuantity(
    		    								invPropagatedCosting.getInvAdjustmentLine().getInvUnitOfMeasure(),
    											invPropagatedCosting.getInvAdjustmentLine().getInvItemLocation().getInvItem(),
    											invPropagatedCosting.getInvAdjustmentLine().getAlAdjustQuantity(), AD_CMPNY);
    		    						Debug.print("ArMiscReceiptEntryControllerBean regenerateCostVariance F");
    		    					}

    		    				} else if (invPropagatedCosting.getInvAssemblyTransferLine() != null){

    		    					QNTY = invPropagatedCosting.getInvAssemblyTransferLine().getAtlAssembleQuantity();
    		    					TTL_CST = invPropagatedCosting.getInvAssemblyTransferLine().getAtlAssembleCost();

    		    					ADJ_DSCRPTN = invPropagatedCosting.getInvAssemblyTransferLine().getInvAssemblyTransfer().getAtrDescription();
    		    					ADJ_CRTD_BY = invPropagatedCosting.getInvAssemblyTransferLine().getInvAssemblyTransfer().getAtrPostedBy();
    		    					ADJ_RFRNC_NMBR = "INVAT" +
    								invPropagatedCosting.getInvAssemblyTransferLine().getInvAssemblyTransfer().getAtrDocumentNumber();

    		    				} else if (invPropagatedCosting.getInvBranchStockTransferLine() != null){

    		    					if(invPropagatedCosting.getInvBranchStockTransferLine().getInvBranchStockTransfer().getBstTransferOutNumber()
    		    							!= null) {

    		    						TTL_CST = invPropagatedCosting.getInvBranchStockTransferLine().getBslAmount();
    									QNTY =  this.convertByUomFromAndItemAndQuantity(
    											invPropagatedCosting.getInvBranchStockTransferLine().getInvUnitOfMeasure(),
    											invPropagatedCosting.getInvBranchStockTransferLine().getInvItemLocation().getInvItem(),
    											invPropagatedCosting.getInvBranchStockTransferLine().getBslQuantityReceived(), AD_CMPNY);
    									Debug.print("ArMiscReceiptEntryControllerBean regenerateCostVariance G");
    		    					} else {

    		    						TTL_CST = invPropagatedCosting.getInvBranchStockTransferLine().getBslAmount();
    									QNTY =  this.convertByUomFromAndItemAndQuantity(
    											invPropagatedCosting.getInvBranchStockTransferLine().getInvUnitOfMeasure(),
    											invPropagatedCosting.getInvBranchStockTransferLine().getInvItemLocation().getInvItem(),
    											invPropagatedCosting.getInvBranchStockTransferLine().getBslQuantity(), AD_CMPNY);
    									Debug.print("ArMiscReceiptEntryControllerBean regenerateCostVariance H");
    		    					}

    		    					ADJ_DSCRPTN =
    		    						invPropagatedCosting.getInvBranchStockTransferLine().getInvBranchStockTransfer().getBstDescription();
    		    					ADJ_CRTD_BY =
    		    						invPropagatedCosting.getInvBranchStockTransferLine().getInvBranchStockTransfer().getBstPostedBy();
    		    					ADJ_RFRNC_NMBR = "INVBST" +
    								invPropagatedCosting.getInvBranchStockTransferLine().getInvBranchStockTransfer().getBstNumber();

    		    				} else if (invPropagatedCosting.getInvBuildUnbuildAssemblyLine() != null){

    		   						TTL_CST = prevInvCosting.getCstRemainingValue() - invPropagatedCosting.getCstRemainingValue();
    		    					QNTY =  invPropagatedCosting.getInvBuildUnbuildAssemblyLine().getBlBuildQuantity();
    		    					ADJ_DSCRPTN =
    		    						invPropagatedCosting.getInvBuildUnbuildAssemblyLine().getInvBuildUnbuildAssembly().getBuaDescription();
    		    					ADJ_CRTD_BY =
    		    						invPropagatedCosting.getInvBuildUnbuildAssemblyLine().getInvBuildUnbuildAssembly().getBuaPostedBy();
    		    					ADJ_RFRNC_NMBR = "INVBUA" +
    								invPropagatedCosting.getInvBuildUnbuildAssemblyLine().getInvBuildUnbuildAssembly().getBuaDocumentNumber();

    		    				} else if (invPropagatedCosting.getInvStockIssuanceLine()!= null){

    		    					TTL_CST = invPropagatedCosting.getInvStockIssuanceLine().getSilIssueCost();
    								QNTY =  this.convertByUomFromAndItemAndQuantity(
    										invPropagatedCosting.getInvStockIssuanceLine().getInvUnitOfMeasure(),
    										invPropagatedCosting.getInvStockIssuanceLine().getInvItemLocation().getInvItem(),
    										invPropagatedCosting.getInvStockIssuanceLine().getSilIssueQuantity(), AD_CMPNY);
    								Debug.print("ArMiscReceiptEntryControllerBean regenerateCostVariance I");
    		    					ADJ_DSCRPTN = invPropagatedCosting.getInvStockIssuanceLine().getInvStockIssuance().getSiDescription();
    		    					ADJ_CRTD_BY = invPropagatedCosting.getInvStockIssuanceLine().getInvStockIssuance().getSiPostedBy();
    		    					ADJ_RFRNC_NMBR = "INVSI" +
    								invPropagatedCosting.getInvStockIssuanceLine().getInvStockIssuance().getSiDocumentNumber();

    		    				} else if (invPropagatedCosting.getInvStockTransferLine()!= null) {

    		    					TTL_CST = invPropagatedCosting.getInvStockTransferLine().getStlAmount();
    								QNTY =  this.convertByUomFromAndItemAndQuantity(
    										invPropagatedCosting.getInvStockTransferLine().getInvUnitOfMeasure(),
    										invPropagatedCosting.getInvStockTransferLine().getInvItem(),
    										invPropagatedCosting.getInvStockTransferLine().getStlQuantityDelivered(), AD_CMPNY);
    								Debug.print("ArMiscReceiptEntryControllerBean regenerateCostVariance J");
    		    					ADJ_DSCRPTN = invPropagatedCosting.getInvStockTransferLine().getInvStockTransfer().getStDescription();
    		    					ADJ_CRTD_BY = invPropagatedCosting.getInvStockTransferLine().getInvStockTransfer().getStPostedBy();
    		    					ADJ_RFRNC_NMBR = "INVST" +
    								invPropagatedCosting.getInvStockTransferLine().getInvStockTransfer().getStDocumentNumber();

    		    				} else {

    		    					prevInvCosting = invPropagatedCosting;
    		    					continue;

    		    				}

    		    				// if quantity is equal 0, no variance.
    		    				if(QNTY == 0) continue;

    		    				// compute new cost variance
    		    				double UNT_CST = TTL_CST/QNTY;
    		    				double CST_VRNC_VL = (invPropagatedCosting.getCstRemainingQuantity() * UNT_CST -
    		    						invPropagatedCosting.getCstRemainingValue());

    		    				if(CST_VRNC_VL != 0)
    		    					this.generateCostVariance(invPropagatedCosting.getInvItemLocation(), CST_VRNC_VL, ADJ_RFRNC_NMBR,
    		    							ADJ_DSCRPTN, invPropagatedCosting.getCstDate(), ADJ_CRTD_BY, AD_BRNCH, AD_CMPNY);

    		    			}

    		    			// set previous costing
    		    			prevInvCosting = invPropagatedCosting;

    		    		}

    		    	} catch (AdPRFCoaGlVarianceAccountNotFoundException ex){

    		    		throw ex;

    		    	} catch (Exception ex) {

    		    		Debug.printStackTrace(ex);
    		    		//getSessionContext().setRollbackOnly();
    		    		throw new EJBException(ex.getMessage());

    		    	}        */
    }

    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue,
                          LocalGlChartOfAccount glChartOfAccount,
                          boolean isCurrentAcv, byte isDebit, double JL_AMNT, Integer AD_CMPNY) {

        Debug.print("ArMiscReceiptEntryControllerBean postToGl 1");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            LocalGlChartOfAccountBalance glChartOfAccountBalance =
                    glChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(
                            glAccountingCalendarValue.getAcvCode(),
                            glChartOfAccount.getCoaCode(), AD_CMPNY);

            String ACCOUNT_TYPE = glChartOfAccount.getCoaAccountType();
            short FC_EXTNDD_PRCSN = adCompany.getGlFunctionalCurrency().getFcPrecision();


            if (((ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("EXPENSE")) &&
                    isDebit == EJBCommon.TRUE) ||
                    (!ACCOUNT_TYPE.equals("ASSET") && !ACCOUNT_TYPE.equals("EXPENSE") &&
                            isDebit == EJBCommon.FALSE)) {

                glChartOfAccountBalance.setCoabEndingBalance(
                        EJBCommon.roundIt(glChartOfAccountBalance.getCoabEndingBalance() + JL_AMNT, FC_EXTNDD_PRCSN));

                if (!isCurrentAcv) {

                    glChartOfAccountBalance.setCoabBeginningBalance(
                            EJBCommon.roundIt(glChartOfAccountBalance.getCoabBeginningBalance() + JL_AMNT, FC_EXTNDD_PRCSN));

                }


            } else {

                glChartOfAccountBalance.setCoabEndingBalance(
                        EJBCommon.roundIt(glChartOfAccountBalance.getCoabEndingBalance() - JL_AMNT, FC_EXTNDD_PRCSN));

                if (!isCurrentAcv) {

                    glChartOfAccountBalance.setCoabBeginningBalance(
                            EJBCommon.roundIt(glChartOfAccountBalance.getCoabBeginningBalance() - JL_AMNT, FC_EXTNDD_PRCSN));

                }

            }

            if (isCurrentAcv) {

                if (isDebit == EJBCommon.TRUE) {

                    glChartOfAccountBalance.setCoabTotalDebit(
                            EJBCommon.roundIt(glChartOfAccountBalance.getCoabTotalDebit() + JL_AMNT, FC_EXTNDD_PRCSN));

                } else {

                    glChartOfAccountBalance.setCoabTotalCredit(
                            EJBCommon.roundIt(glChartOfAccountBalance.getCoabTotalCredit() + JL_AMNT, FC_EXTNDD_PRCSN));
                }

            }

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }

    private void voidInvAdjustment(LocalInvAdjustment invAdjustment, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ArMiscReceiptEntryControllerBean voidInvAdjustment");

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
                        invDistributionRecord.getInvChartOfAccount().getCoaCode(), invAdjustment, AD_BRNCH, AD_CMPNY);

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

                this.addInvAlEntry(invAdjustmentLine.getInvItemLocation(),
                        invAdjustment, (invAdjustmentLine.getAlUnitCost()) * -1, EJBCommon.TRUE, AD_CMPNY);

            }

            invAdjustment.setAdjVoid(EJBCommon.TRUE);

            this.executeInvAdjPost(invAdjustment.getAdjCode(), invAdjustment.getAdjLastModifiedBy(), AD_BRNCH, AD_CMPNY);

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            //getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }

    private void addInvDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, byte DR_RVRSL, Integer COA_CODE,
                               LocalInvAdjustment invAdjustment, Integer AD_BRNCH, Integer AD_CMPNY)

            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArMiscReceiptEntryControllerBean addInvDrEntry");


        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            // validate coa

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, AD_BRNCH, AD_CMPNY);

            }
            catch (FinderException ex) {

                throw new GlobalBranchAccountNumberInvalidException();

            }

            // create distribution record

            LocalInvDistributionRecord invDistributionRecord = invDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT,
                    EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), DR_RVRSL, EJBCommon.FALSE,
                    AD_CMPNY);

            //invAdjustment.addInvDistributionRecord(invDistributionRecord);
            invDistributionRecord.setInvAdjustment(invAdjustment);
            //glChartOfAccount.addInvDistributionRecord(invDistributionRecord);
            invDistributionRecord.setInvChartOfAccount(glChartOfAccount);


        }
        catch (GlobalBranchAccountNumberInvalidException ex) {

            throw new GlobalBranchAccountNumberInvalidException();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            //getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }

    private LocalInvAdjustmentLine addInvAlEntry(LocalInvItemLocation invItemLocation, LocalInvAdjustment invAdjustment,
                                                 double CST_VRNC_VL, byte AL_VD, Integer AD_CMPNY) {

        Debug.print("ArMiscReceiptEntryControllerBean addInvAlEntry");

        try {

            // create dr entry
            LocalInvAdjustmentLine invAdjustmentLine = null;
            invAdjustmentLine = invAdjustmentLineHome.create(CST_VRNC_VL, null, null, 0, 0, AL_VD, AD_CMPNY);

            // map adjustment, unit of measure, item location
            //invAdjustment.addInvAdjustmentLine(invAdjustmentLine);
            invAdjustmentLine.setInvAdjustment(invAdjustment);
            //invItemLocation.getInvItem().getInvUnitOfMeasure().addInvAdjustmentLine(invAdjustmentLine);
            invAdjustmentLine.setInvUnitOfMeasure(invItemLocation.getInvItem().getInvUnitOfMeasure());
            //invItemLocation.addInvAdjustmentLine(invAdjustmentLine);
            invAdjustmentLine.setInvItemLocation(invItemLocation);

            return invAdjustmentLine;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            //getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }

    private void executeInvAdjPost(Integer ADJ_CODE, String USR_NM, Integer AD_BRNCH, Integer AD_CMPNY) throws
            GlobalRecordAlreadyDeletedException,
            GlobalTransactionAlreadyPostedException,
            GlJREffectiveDateNoPeriodExistException,
            GlJREffectiveDatePeriodClosedException,
            GlobalJournalNotBalanceException,
            GlobalBranchAccountNumberInvalidException {

        Debug.print("ArMiscReceiptEntryControllerBean executeInvAdjPost");

        try {

            // validate if adjustment is already deleted

            LocalInvAdjustment invAdjustment = null;

            try {

                invAdjustment = invAdjustmentHome.findByPrimaryKey(ADJ_CODE);

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

            Collection invAdjustmentLines = null;

            if (invAdjustment.getAdjVoid() == EJBCommon.FALSE) {
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.FALSE, invAdjustment.getAdjCode(), AD_CMPNY);
            } else {
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.TRUE, invAdjustment.getAdjCode(), AD_CMPNY);
            }

            Iterator i = invAdjustmentLines.iterator();

            while (i.hasNext()) {


                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();

                LocalInvCosting invCosting =
                        invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                invAdjustment.getAdjDate(), invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(),
                                invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName(), AD_BRNCH, AD_CMPNY);

                this.postInvAdjustmentToInventory(invAdjustmentLine, invAdjustment.getAdjDate(), 0,
                        invAdjustmentLine.getAlUnitCost(), invCosting.getCstRemainingQuantity(),
                        invCosting.getCstRemainingValue() + invAdjustmentLine.getAlUnitCost(), AD_BRNCH, AD_CMPNY);

            }

            // post to gl if necessary

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            // validate if date has no period and period is closed

            LocalGlSetOfBook glJournalSetOfBook = null;

            try {

                glJournalSetOfBook = glSetOfBookHome.findByDate(invAdjustment.getAdjDate(), AD_CMPNY);

            }
            catch (FinderException ex) {

                throw new GlJREffectiveDateNoPeriodExistException();

            }

            LocalGlAccountingCalendarValue glAccountingCalendarValue =
                    glAccountingCalendarValueHome.findByAcCodeAndDate(
                            glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), invAdjustment.getAdjDate(), AD_CMPNY);


            if (glAccountingCalendarValue.getAcvStatus() == 'N' ||
                    glAccountingCalendarValue.getAcvStatus() == 'C' ||
                    glAccountingCalendarValue.getAcvStatus() == 'P') {

                throw new GlJREffectiveDatePeriodClosedException();

            }

            // check if invoice is balance if not check suspense posting

            LocalGlJournalLine glOffsetJournalLine = null;

            Collection invDistributionRecords = null;

            if (invAdjustment.getAdjVoid() == EJBCommon.FALSE) {

                invDistributionRecords = invDistributionRecordHome.findImportableDrByDrReversedAndAdjCode(EJBCommon.FALSE,
                        invAdjustment.getAdjCode(), AD_CMPNY);

            } else {

                invDistributionRecords = invDistributionRecordHome.findImportableDrByDrReversedAndAdjCode(EJBCommon.TRUE,
                        invAdjustment.getAdjCode(), AD_CMPNY);

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

            if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.TRUE &&
                    TOTAL_DEBIT != TOTAL_CREDIT) {

                LocalGlSuspenseAccount glSuspenseAccount = null;

                try {

                    glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("INVENTORY", "INVENTORY ADJUSTMENTS",
                            AD_CMPNY);

                }
                catch (FinderException ex) {

                    throw new GlobalJournalNotBalanceException();

                }

                if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                    glOffsetJournalLine = glJournalLineHome.create((short) (invDistributionRecords.size() + 1), EJBCommon.TRUE,
                            TOTAL_CREDIT - TOTAL_DEBIT, "", AD_CMPNY);

                } else {

                    glOffsetJournalLine = glJournalLineHome.create((short) (invDistributionRecords.size() + 1), EJBCommon.FALSE,
                            TOTAL_DEBIT - TOTAL_CREDIT, "", AD_CMPNY);

                }

                LocalGlChartOfAccount glChartOfAccount = glSuspenseAccount.getGlChartOfAccount();
                //glChartOfAccount.addGlJournalLine(glOffsetJournalLine);
                glOffsetJournalLine.setGlChartOfAccount(glChartOfAccount);

            } else if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE &&
                    TOTAL_DEBIT != TOTAL_CREDIT) {

                throw new GlobalJournalNotBalanceException();

            }

            // create journal batch if necessary

            LocalGlJournalBatch glJournalBatch = null;
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");

            try {

                glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) +
                        " INVENTORY ADJUSTMENTS", AD_BRNCH, AD_CMPNY);

            }
            catch (FinderException ex) {

            }

            if (glJournalBatch == null) {

                glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) +
                                " INVENTORY ADJUSTMENTS", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(),
                        USR_NM, AD_BRNCH, AD_CMPNY);

            }

            // create journal entry

            LocalGlJournal glJournal = glJournalHome.create(invAdjustment.getAdjReferenceNumber(),
                    invAdjustment.getAdjDescription(), invAdjustment.getAdjDate(),
                    0.0d, null, invAdjustment.getAdjDocumentNumber(), null, 1d, "N/A", null,
                    'N', EJBCommon.TRUE, EJBCommon.FALSE,
                    USR_NM, new Date(),
                    USR_NM, new Date(),
                    null, null,
                    USR_NM, EJBCommon.getGcCurrentDateWoTime().getTime(),
                    null, null, EJBCommon.FALSE, null,

                    AD_BRNCH, AD_CMPNY);

            LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("INVENTORY", AD_CMPNY);
            glJournal.setGlJournalSource(glJournalSource);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), AD_CMPNY);
            glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("INVENTORY ADJUSTMENTS", AD_CMPNY);
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

                LocalGlJournalLine glJournalLine = glJournalLineHome.create(invDistributionRecord.getDrLine(),
                        invDistributionRecord.getDrDebit(), DR_AMNT, "", AD_CMPNY);

                glJournalLine.setGlChartOfAccount(invDistributionRecord.getInvChartOfAccount());

                glJournalLine.setGlJournal(glJournal);

                invDistributionRecord.setDrImported(EJBCommon.TRUE);


            }

            if (glOffsetJournalLine != null) {

                glOffsetJournalLine.setGlJournal(glJournal);

            }

            // post journal to gl

            Collection glJournalLines = glJournal.getGlJournalLines();

            i = glJournalLines.iterator();

            while (i.hasNext()) {

                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) i.next();

                // post current to current acv

                this.postToGl(glAccountingCalendarValue,
                        glJournalLine.getGlChartOfAccount(),
                        true, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), AD_CMPNY);


                // post to subsequent acvs (propagate)

                Collection glSubsequentAccountingCalendarValues =
                        glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(
                                glJournalSetOfBook.getGlAccountingCalendar().getAcCode(),
                                glAccountingCalendarValue.getAcvPeriodNumber(), AD_CMPNY);

                Iterator acvsIter = glSubsequentAccountingCalendarValues.iterator();

                while (acvsIter.hasNext()) {

                    LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue =
                            (LocalGlAccountingCalendarValue) acvsIter.next();

                    this.postToGl(glSubsequentAccountingCalendarValue,
                            glJournalLine.getGlChartOfAccount(),
                            false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), AD_CMPNY);

                }

                // post to subsequent years if necessary

                Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(
                        glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), AD_CMPNY);

                if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                    adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
                    LocalGlChartOfAccount glRetainedEarningsAccount =
                            glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(adCompany.getCmpRetainedEarnings(),
                                    AD_BRNCH, AD_CMPNY);

                    Iterator sobIter = glSubsequentSetOfBooks.iterator();

                    while (sobIter.hasNext()) {

                        LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) sobIter.next();

                        String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                        // post to subsequent acvs of subsequent set of book(propagate)

                        Collection glAccountingCalendarValues =
                                glAccountingCalendarValueHome.findByAcCode(
                                        glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), AD_CMPNY);

                        Iterator acvIter = glAccountingCalendarValues.iterator();

                        while (acvIter.hasNext()) {

                            LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue =
                                    (LocalGlAccountingCalendarValue) acvIter.next();

                            if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY") ||
                                    ACCOUNT_TYPE.equals("OWNERS EQUITY")) {

                                this.postToGl(glSubsequentAccountingCalendarValue,
                                        glJournalLine.getGlChartOfAccount(),
                                        false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), AD_CMPNY);

                            } else { // revenue & expense

                                this.postToGl(glSubsequentAccountingCalendarValue,
                                        glRetainedEarningsAccount,
                                        false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), AD_CMPNY);

                            }

                        }

                        if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) {
                            break;
                        }

                    }

                }

            }

            invAdjustment.setAdjPosted(EJBCommon.TRUE);

        }
        catch (GlJREffectiveDateNoPeriodExistException ex) {

            //getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlJREffectiveDatePeriodClosedException ex) {

            //getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlobalJournalNotBalanceException ex) {

            //getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlobalRecordAlreadyDeletedException ex) {

            //getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlobalTransactionAlreadyPostedException ex) {

            //getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            //getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }

    private void postInvAdjustmentToInventory(LocalInvAdjustmentLine invAdjustmentLine, Date CST_DT, double CST_ADJST_QTY,
                                              double CST_ADJST_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ArMiscReceiptEntryControllerBean postInvAdjustmentToInventory");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
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

                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(
                        CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(),
                        AD_BRNCH, AD_CMPNY);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            }
            catch (FinderException ex) {

                CST_LN_NMBR = 1;

            }

            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d, 0d, 0d,
                    CST_ADJST_QTY, CST_ADJST_CST, 0d, 0d, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, CST_ADJST_QTY > 0 ? CST_ADJST_QTY : 0, AD_BRNCH, AD_CMPNY);
            //invItemLocation.addInvCosting(invCosting);
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setInvAdjustmentLine(invAdjustmentLine);

            // propagate balance if necessary

            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, AD_CMPNY);

            Iterator i = invCostings.iterator();

            while (i.hasNext()) {

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) i.next();

                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_ADJST_QTY);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_ADJST_CST);

            }


        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            //getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }


    }

    private ArrayList convertMiscToInvModTagList(String misc) {

        ArrayList list = new ArrayList();

        String[] imei_array = misc.split("\\$");


        System.out.println("to be split: " + misc);


        for (int x = 0; x < imei_array.length; x++) {
            if (imei_array[x].trim().equals("")) {
                continue;
            }
            InvModTagListDetails details = new InvModTagListDetails();
            System.out.println("tokenized: " + imei_array[x]);
            details.setTgPropertyCode("");
            details.setTgSpecs("");
            details.setTgTransactionDate(new Date());
            details.setTgSerialNumber(imei_array[x]);

            list.add(details);
        }


        return list;


    }

    public double getFrRateByFrNameAndFrDate(String FC_NM, Date CONVERSION_DATE, Integer AD_CMPNY)
            throws GlobalConversionDateNotExistException {

        Debug.print("ArMiscReceiptEntryControllerBean getFrRateByFrNameAndFrDate");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);

            double CONVERSION_RATE = 1;

            // Get functional currency rate

            if (!FC_NM.equals("USD")) {

                LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate =
                        glFunctionalCurrencyRateHome.findByFcCodeAndDate(glFunctionalCurrency.getFcCode(),
                                CONVERSION_DATE, AD_CMPNY);

                CONVERSION_RATE = glFunctionalCurrencyRate.getFrXToUsd();

            }

            // Get set of book functional currency rate if necessary

            if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate =
                        glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(),
                                CONVERSION_DATE, AD_CMPNY);

                CONVERSION_RATE = CONVERSION_RATE / glCompanyFunctionalCurrencyRate.getFrXToUsd();

            }

            return CONVERSION_RATE;

        }
        catch (FinderException ex) {

            //getSessionContext().setRollbackOnly();
            throw new GlobalConversionDateNotExistException();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }

}