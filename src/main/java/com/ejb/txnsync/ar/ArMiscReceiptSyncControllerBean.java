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

    private int setArReceiptAllNewAndVoid(String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier) {

        Debug.print("ArMiscReceiptSyncControllerBean setArMiscReceiptAllNewAndVoid");

        return 0;
    }

    private int setArMiscReceiptAllNewAndVoid(
            String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier, String companyShortName) {

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
                HashMap<String, Object> receiptNumbers = new HashMap<>();
                HashMap<String, Object> invoiceNumbers = new HashMap<>();
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
                            Debug.print("FinderException : " + ex.getMessage());
                        }

                        try {
                            assert adDocumentSequenceAssignment != null;
                            adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome
                                    .findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode);
                        }
                        catch (FinderException ex) {
                            Debug.print("FinderException : " + ex.getMessage());
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
                        Iterator<Object> rctIter = receiptNumbers.values().iterator();
                        while (rctIter.hasNext()) {
                            try {
                                ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) rctIter.next();
                                arExistingReceipt = arReceiptHome
                                        .findByRctDateAndRctNumberAndCstCustomerCodeAndBrCode(
                                                arModReceiptDetails.getRctDate(), arModUploadReceiptDetails.getRctNumber(),
                                                arModReceiptDetails.getRctCstCustomerCode(), branchCode, companyCode);
                                break;
                            }
                            catch (FinderException ex) {
                                Debug.print("FinderException : " + ex.getMessage());
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

                            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(arModReceiptDetails.getRctCstCustomerCode(), companyCode);
                            //arCustomer.addArReceipt(arReceipt);
                            arReceipt.setArCustomer(arCustomer);

                            String bankAccount;
                            String BR_BRNCH_CODE = adBranch.getBrBranchCode();
                            bankAccount = switch (BR_BRNCH_CODE) {
                                case "Manila-Smoking Lounge" -> "Allied Bank-IPT Smoking";
                                case "Manila-Cigar Shop" -> "Allied Bank Cigar shop";
                                case "Manila-Term.#2 Domestic" -> "Terminal II Domestic";
                                case "Manila-Term.#2 Intl" -> "Term2 International";
                                case "Cebu-Banilad" -> "Metrobank Banilad";
                                case "Cebu-Gorordo" -> "Metrobank-Gorordo";
                                case "Cebu-Mactan Domestic" -> "Metrobank Mactan Domestic";
                                case "Cebu-Mactan Intl" -> "Metrobank I Mactan Int'l";
                                case "Cebu-Supercat" -> "Metrobank Supercat";
                                default -> arCustomer.getAdBankAccount().getBaName();
                            };

                            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(bankAccount, companyCode);
                            //adBankAccount.addArReceipt(arReceipt);
                            arReceipt.setAdBankAccount(adBankAccount);

                            LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INCLUSIVE", companyCode);
                            //arTaxCode.addArReceipt(arReceipt);
                            arReceipt.setArTaxCode(arTaxCode);

                            LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName("NONE", companyCode);
                            //arWithholdingTaxCode.addArReceipt(arReceipt);
                            arReceipt.setArWithholdingTaxCode(arWithholdingTaxCode);

                            LocalArReceiptBatch arReceiptBatch;
                            try {
                                arReceiptBatch = arReceiptBatchHome.findByRbName("POS BATCH", branchCode, companyCode);
                            }
                            catch (FinderException ex) {
                                arReceiptBatch = arReceiptBatchHome.create("POS BATCH", "POS BATCH",
                                        "OPEN", "MISC", EJBCommon.getGcCurrentDateWoTime().getTime(),
                                        cashier, branchCode, companyCode);
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

                                try {
                                    if (invItem.getIiDefaultLocation() != null) {
                                        invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                                    }
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
                                }

                                LocalInvItemLocation invItemLocation = invItemLocationHome
                                        .findByIiNameAndLocName(arModInvoiceLineItemDetails.getIliIiName(), invLocation == null ?
                                                arModInvoiceLineItemDetails.getIliLocName() : invLocation.getLocName(), companyCode);
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


                            for (Object o : arModReceiptDetails.getInvIliList()) {

                                ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = (ArModInvoiceLineItemDetails) o;
                                LocalInvItem invItem = invItemHome.findByIiName(arModInvoiceLineItemDetails.getIliIiName(), companyCode);
                                LocalInvLocation invLocation = null;

                                try {
                                    if (invItem.getIiDefaultLocation() != null) {
                                        invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                                    }
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
                                }

                                LocalInvItemLocation invItemLocation = invItemLocationHome
                                        .findByIiNameAndLocName(arModInvoiceLineItemDetails.getIliIiName(), invLocation == null ?
                                                arModInvoiceLineItemDetails.getIliLocName() : invLocation.getLocName(), companyCode);

                                LocalArInvoiceLineItem arExistingInvoiceLineItem = null;

                                try {
                                    arExistingInvoiceLineItem = arInvoiceLineItemHome.findByRctCodeAndIlCodeAndUomName(arExistingReceipt.getRctCode(), invItemLocation.getIlCode(), arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
                                }

                                if (arExistingInvoiceLineItem == null) {

                                    LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.create((short)(arExistingReceipt.getArInvoiceLineItems().size() + 1), arModInvoiceLineItemDetails.getIliQuantity(), arModInvoiceLineItemDetails.getIliUnitPrice(), EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingReceipt.getArTaxCode().getTcRate() / 100), (short) 2), EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() - (arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingReceipt.getArTaxCode().getTcRate() / 100)), (short) 2), EJBCommon.FALSE, 0, 0, 0, 0, 0, EJBCommon.TRUE, companyCode);

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
                        String generatedInvoice;
                        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
                        LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;

                        try {

                            adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AR INVOICE", companyCode);

                        }
                        catch (FinderException ex) {
                            Debug.print("FinderException : " + ex.getMessage());
                        }

                        try {
                            assert adDocumentSequenceAssignment != null;
                            adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome
                                    .findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode);
                        }
                        catch (FinderException ex) {
                            Debug.print("FinderException : " + ex.getMessage());
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
                                arExistingInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndCstCustomerCode(
                                        arModUploadReceiptDetails.getRctNumber(), (byte) 0,
                                        arModReceiptDetails.getRctCstCustomerCode(), companyCode);
                                break;
                            }
                            catch (FinderException ex) {
                                Debug.print("Finder Exception : " + ex.getMessage());
                            }
                        }

                        if (arExistingInvoice == null) {

                            ArModReceiptDetails arModUploadReceiptDetails = new ArModReceiptDetails();
                            arModUploadReceiptDetails.setRctNumber(generatedInvoice);
                            arModUploadReceiptDetails.setRctPosDiscount(
                                    EJBCommon.roundIt(arModReceiptDetails.getRctPosDiscount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosScAmount(
                                    EJBCommon.roundIt(arModReceiptDetails.getRctPosScAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosDcAmount(
                                    EJBCommon.roundIt(arModReceiptDetails.getRctPosDcAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            invoiceNumbers.put(generatedInvoice, arModUploadReceiptDetails);

                            double totalAmount = EJBCommon.roundIt((arModReceiptDetails.getRctPosTotalAmount()
                                    - arModReceiptDetails.getRctPosVoidAmount()) * arModReceiptDetails.getRctConversionRate(), (short) 2);

                            LocalArInvoice arInvoice = arInvoiceHome.create("ITEMS", (byte) 0, "POS Sales " + new Date(),
                                    arModReceiptDetails.getRctDate(), generatedInvoice, null, null, null, null, totalAmount, 0d, 0d, 0d, 0d, 0d, null, 1, null, 0d, 0d, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, null, 0d, null, null, null, null, cashier, EJBCommon.getGcCurrentDateWoTime().getTime(), cashier, EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, null, null, EJBCommon.FALSE, null, null, null, (byte) 0, (byte) 0, null, arModReceiptDetails.getRctDate(), branchCode, companyCode);

                            /*
                             * RctPaymentMethod is Used Temporarily for Payment Term Field
                             */
                            if (adCompany.getCmpShortName().equalsIgnoreCase("tinderbox")
                                    || adCompany.getCmpShortName().equalsIgnoreCase("hs")) {
                                LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName("IMMEDIATE", companyCode);
                                //adPaymentTerm.addArInvoice(arInvoice);
                                arInvoice.setAdPaymentTerm(adPaymentTerm);
                            } else {
                                LocalAdPaymentTerm adPaymentTerm;
                                if (arModReceiptDetails.getRctPaymentMethod() == null || arModReceiptDetails.getRctPaymentMethod().equals("")) {
                                    adPaymentTerm = adPaymentTermHome.findByPytName("IMMEDIATE", companyCode);
                                    //adPaymentTerm.addArInvoice(arInvoice);
                                } else {
                                    adPaymentTerm = adPaymentTermHome.findByPytName(arModReceiptDetails.getRctPaymentMethod(), companyCode);
                                    //adPaymentTerm.addArInvoice(arInvoice);
                                }
                                arInvoice.setAdPaymentTerm(adPaymentTerm);
                            }

                            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                            //glFunctionalCurrency.addArInvoice(arInvoice);
                            arInvoice.setGlFunctionalCurrency(glFunctionalCurrency);

                            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(arModReceiptDetails.getRctCstCustomerCode(), companyCode);
                            //arCustomer.addArInvoice(arInvoice);
                            arInvoice.setArCustomer(arCustomer);

                            if (adCompany.getCmpShortName().equalsIgnoreCase("tinderbox") || adCompany.getCmpShortName().equalsIgnoreCase("hs")) {
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

                            LocalArInvoiceBatch arInvoiceBatch;
                            try {
                                arInvoiceBatch = arInvoiceBatchHome.findByIbName("POS BATCH", branchCode, companyCode);
                            }
                            catch (FinderException ex) {
                                arInvoiceBatch = arInvoiceBatchHome.create("POS BATCH",
                                        "POS BATCH", "OPEN", "MISC",
                                        EJBCommon.getGcCurrentDateWoTime().getTime(), cashier, branchCode, companyCode);
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
                                    Debug.print("Finder Exception : " + ex.getMessage());
                                }

                                LocalInvItemLocation invItemLocation = invItemLocationHome
                                        .findByIiNameAndLocName(arModInvoiceLineItemDetails.getIliIiName(), invLocation == null ?
                                                arModInvoiceLineItemDetails.getIliLocName() : invLocation.getLocName(), companyCode);
                                //invItemLocation.addArInvoiceLineItem(arInvoiceLineItem);
                                arInvoiceLineItem.setInvItemLocation(invItemLocation);
                                if (invItemLocation.getInvItem().getIiEnableAutoBuild() == EJBCommon.TRUE) {
                                    arInvoiceLineItem.setIliEnableAutoBuild(EJBCommon.TRUE);
                                }
                            }

                        } else {

                            ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) invoiceNumbers.get(arExistingInvoice.getInvNumber());

                            arModUploadReceiptDetails.setRctPosDiscount(arModUploadReceiptDetails.getRctPosDiscount() +
                                    EJBCommon.roundIt(arModReceiptDetails.getRctPosDiscount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosScAmount(arModUploadReceiptDetails.getRctPosScAmount() +
                                    EJBCommon.roundIt(arModReceiptDetails.getRctPosScAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosDcAmount(arModUploadReceiptDetails.getRctPosDcAmount() +
                                    EJBCommon.roundIt(arModReceiptDetails.getRctPosDcAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            invoiceNumbers.put(arExistingInvoice.getInvNumber(), arModUploadReceiptDetails);

                            double totalAmount = EJBCommon.roundIt((arModReceiptDetails.getRctPosTotalAmount() -
                                    arModReceiptDetails.getRctPosVoidAmount()) * arModReceiptDetails.getRctConversionRate(), (short) 2);
                            arExistingInvoice.setInvAmountDue(arExistingInvoice.getInvAmountDue() + totalAmount);

                            for (Object o : arModReceiptDetails.getInvIliList()) {
                                ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = (ArModInvoiceLineItemDetails) o;
                                LocalInvItem invItem = invItemHome.findByIiName(arModInvoiceLineItemDetails.getIliIiName(), companyCode);

                                LocalInvLocation invLocation = null;
                                try {
                                    if (invItem.getIiDefaultLocation() != null) {
                                        invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                                    }
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
                                }

                                LocalInvItemLocation invItemLocation = invItemLocationHome
                                        .findByIiNameAndLocName(arModInvoiceLineItemDetails.getIliIiName(), invLocation == null ?
                                                arModInvoiceLineItemDetails.getIliLocName() : invLocation.getLocName(), companyCode);

                                LocalArInvoiceLineItem arExistingInvoiceLineItem = null;
                                try {
                                    arExistingInvoiceLineItem = arInvoiceLineItemHome
                                            .findByRctCodeAndIlCodeAndUomName(arExistingInvoice.getInvCode(),
                                                    invItemLocation.getIlCode(), arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
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
                for (Object o : receiptNumbers.values()) {
                    ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) o;

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
                        double COST;

                        try {

                            LocalInvCosting invCosting = invCostingHome
                                    .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                            arReceipt.getRctDate(), invItemLocation.getInvItem().getIiName(),
                                            invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);

                            COST = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                        }
                        catch (FinderException ex) {

                            COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                        }

                        double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                        LocalAdBranchItemLocation adBranchItemLocation = null;

                        try {
                            adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(
                                    arInvoiceLineItem.getInvItemLocation().getIlCode(), branchCode, companyCode);
                        }
                        catch (FinderException ex) {
                            Debug.print("Finder Exception : " + ex.getMessage());
                        }

                        if (arInvoiceLineItem.getIliEnableAutoBuild() == 0
                                || arInvoiceLineItem.getIliEnableAutoBuild() == 1
                                || arInvoiceLineItem.getInvItemLocation().getInvItem().getIiClass().equals("Stock")) {

                            if (adBranchItemLocation != null) {

                                this.addArDrIliEntry(arReceipt.getArDrNextLine(), "COGS", EJBCommon.TRUE, COST * QTY_SLD,
                                        adBranchItemLocation.getBilCoaGlCostOfSalesAccount(), arReceipt, branchCode, companyCode);

                                this.addArDrIliEntry(arReceipt.getArDrNextLine(), "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD,
                                        adBranchItemLocation.getBilCoaGlInventoryAccount(), arReceipt, branchCode, companyCode);

                            } else {

                                this.addArDrIliEntry(arReceipt.getArDrNextLine(), "COGS", EJBCommon.TRUE, COST * QTY_SLD,
                                        arInvoiceLineItem.getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arReceipt, branchCode, companyCode);

                                this.addArDrIliEntry(arReceipt.getArDrNextLine(), "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD,
                                        arInvoiceLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(), arReceipt, branchCode, companyCode);

                            }

                            // add quantity to item location committed quantity
                            double convertedQuantity = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(),
                                    arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);
                            invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() + convertedQuantity);

                        }

                        // add inventory sale distributions
                        if (adBranchItemLocation != null) {

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(), "REVENUE", EJBCommon.FALSE, arInvoiceLineItem.getIliAmount(),
                                    adBranchItemLocation.getBilCoaGlSalesAccount(), arReceipt, branchCode, companyCode);

                        } else {

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(), "REVENUE", EJBCommon.FALSE, arInvoiceLineItem.getIliAmount(),
                                    arInvoiceLineItem.getInvItemLocation().getIlGlCoaSalesAccount(), arReceipt, branchCode, companyCode);

                        }
                        TOTAL_TAX += arInvoiceLineItem.getIliTaxAmount();
                    }

                    // add tax distribution if necessary
                    if (!arReceipt.getArTaxCode().getTcType().equals("NONE") && !arReceipt.getArTaxCode().getTcType().equals("EXEMPT")) {

                        if (adCompany.getCmpShortName().equalsIgnoreCase("tinderbox")) {
                            String brTaxCOA = this.getBranchTaxCOA(adBranch.getBrBranchCode(),
                                    arReceipt.getArTaxCode().getGlChartOfAccount().getCoaSegment1(),
                                    arReceipt.getArTaxCode().getGlChartOfAccount().getCoaSegment3(),
                                    arReceipt.getArTaxCode().getGlChartOfAccount().getCoaSegment4(),
                                    arReceipt.getArTaxCode().getGlChartOfAccount().getCoaSegment5());

                            this.addArDrEntry(arReceipt.getArDrNextLine(), "TAX",
                                    EJBCommon.FALSE, TOTAL_TAX,
                                    glChartOfAccountHome.findByCoaAccountNumber(brTaxCOA, companyCode).getCoaCode(),
                                    EJBCommon.FALSE, arReceipt, branchCode, companyCode);
                        } else {

                            this.addArDrEntry(arReceipt.getArDrNextLine(), "TAX", EJBCommon.FALSE, TOTAL_TAX,
                                    arReceipt.getArTaxCode().getGlChartOfAccount().getCoaCode(), EJBCommon.FALSE,
                                    arReceipt, branchCode, companyCode);

                        }
                    }

                    // add cash distribution
                    this.addArDrEntry(arReceipt.getArDrNextLine(), "CASH", EJBCommon.TRUE, arReceipt.getRctAmount(),
                            arReceipt.getAdBankAccount().getBaCoaGlCashAccount(), EJBCommon.FALSE,
                            arReceipt, branchCode, companyCode);

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

                for (Object o : invoiceNumbers.values()) {
                    ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) o;

                    LocalArInvoice arInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(
                            arModUploadReceiptDetails.getRctNumber(), (byte) 0, branchCode, companyCode);
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
                        switch (arInvoice.getAdPaymentTerm().getPytScheduleBasis()) {
                            case "DEFAULT" -> {
                                gcDateDue.setTime(arInvoice.getInvEffectivityDate());
                                gcDateDue.add(Calendar.DATE, adPaymentSchedule.getPsDueDay());
                            }
                            case "MONTHLY" -> {
                                gcDateDue = gcPrevDateDue;
                                gcDateDue.add(Calendar.MONTH, 1);
                            }
                            case "BI-MONTHLY" -> {
                                gcDateDue = gcPrevDateDue;
                                if (gcPrevDateDue.get(Calendar.MONTH) != 1) {
                                    if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 31
                                            && gcPrevDateDue.get(Calendar.DATE) > 15 && gcPrevDateDue.get(Calendar.DATE) < 31) {
                                        gcDateDue.add(Calendar.DATE, 16);
                                    } else {
                                        gcDateDue.add(Calendar.DATE, 15);
                                    }
                                } else if (gcPrevDateDue.get(Calendar.MONTH) == 1) {
                                    if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 28 && gcPrevDateDue.get(Calendar.DATE) == 14) {
                                        gcDateDue.add(Calendar.DATE, 14);
                                    } else if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 28
                                            && gcPrevDateDue.get(Calendar.DATE) >= 15 && gcPrevDateDue.get(Calendar.DATE) < 28) {
                                        gcDateDue.add(Calendar.DATE, 13);
                                    } else if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 29
                                            && gcPrevDateDue.get(Calendar.DATE) >= 15 && gcPrevDateDue.get(Calendar.DATE) < 29) {
                                        gcDateDue.add(Calendar.DATE, 14);
                                    } else {
                                        gcDateDue.add(Calendar.DATE, 15);
                                    }
                                }
                            }
                        }

                        // create a payment schedule
                        double PAYMENT_SCHEDULE_AMOUNT;

                        // if last payment schedule subtract to avoid rounding difference error
                        if (i.hasNext()) {
                            PAYMENT_SCHEDULE_AMOUNT = EJBCommon.roundIt(
                                    (adPaymentSchedule.getPsRelativeAmount() / arInvoice.getAdPaymentTerm().getPytBaseAmount())
                                            * arInvoice.getInvAmountDue(), precisionUnit);
                        } else {
                            PAYMENT_SCHEDULE_AMOUNT = arInvoice.getInvAmountDue() - TOTAL_PAYMENT_SCHEDULE;
                        }

                        LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = arInvoicePaymentScheduleHome.create(
                                gcDateDue.getTime(), adPaymentSchedule.getPsLineNumber(), PAYMENT_SCHEDULE_AMOUNT, 0d,
                                EJBCommon.FALSE, (short) 0, gcDateDue.getTime(), 0d, 0d, companyCode);

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
                        double COST;
                        try {
                            LocalInvCosting invCosting = invCostingHome
                                    .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                            arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(),
                                            invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                            COST = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                        }
                        catch (FinderException ex) {
                            COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();
                        }

                        double QTY_SLD = this.convertByUomFromAndItemAndQuantity(
                                arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(),
                                arInvoiceLineItem.getIliQuantity(), companyCode);

                        LocalAdBranchItemLocation adBranchItemLocation = null;
                        try {
                            adBranchItemLocation = adBranchItemLocationHome
                                    .findBilByIlCodeAndBrCode(arInvoiceLineItem.getInvItemLocation().getIlCode(), branchCode, companyCode);
                        }
                        catch (FinderException ex) {
                            Debug.print("Finder Exception : " + ex.getMessage());
                        }

                        if (arInvoiceLineItem.getIliEnableAutoBuild() == 0
                                || arInvoiceLineItem.getIliEnableAutoBuild() == 1
                                || arInvoiceLineItem.getInvItemLocation().getInvItem().getIiClass().equals("Stock")) {

                            if (adBranchItemLocation != null) {

                                this.addArDrIliEntry(arInvoice.getArDrNextLine(), "COGS", EJBCommon.TRUE, COST * QTY_SLD,
                                        adBranchItemLocation.getBilCoaGlCostOfSalesAccount(), arInvoice, branchCode, companyCode);

                                this.addArDrIliEntry(arInvoice.getArDrNextLine(), "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD,
                                        adBranchItemLocation.getBilCoaGlInventoryAccount(), arInvoice, branchCode, companyCode);

                            } else {

                                this.addArDrIliEntry(arInvoice.getArDrNextLine(), "COGS", EJBCommon.TRUE, COST * QTY_SLD,
                                        arInvoiceLineItem.getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arInvoice, branchCode, companyCode);

                                this.addArDrIliEntry(arInvoice.getArDrNextLine(), "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD,
                                        arInvoiceLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(), arInvoice, branchCode, companyCode);

                            }

                            // add quantity to item location committed quantity
                            double convertedQuantity = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(),
                                    arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);
                            invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() + convertedQuantity);

                        }

                        // add inventory sale distributions
                        if (adBranchItemLocation != null) {
                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), "REVENUE", EJBCommon.FALSE,
                                    arInvoiceLineItem.getIliAmount(), adBranchItemLocation.getBilCoaGlSalesAccount(),
                                    arInvoice, branchCode, companyCode);
                        } else {
                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), "REVENUE", EJBCommon.FALSE,
                                    arInvoiceLineItem.getIliAmount(), arInvoiceLineItem.getInvItemLocation().getIlGlCoaSalesAccount(),
                                    arInvoice, branchCode, companyCode);
                        }
                        TOTAL_TAX += arInvoiceLineItem.getIliTaxAmount();
                    }

                    // add tax distribution if necessary
                    if (!arInvoice.getArTaxCode().getTcType().equals("NONE") && !arInvoice.getArTaxCode().getTcType().equals("EXEMPT")) {
                        if (adCompany.getCmpShortName().equalsIgnoreCase("tinderbox")) {
                            String brTaxCOA = this.getBranchTaxCOA(adBranch.getBrBranchCode(),
                                    arInvoice.getArTaxCode().getGlChartOfAccount().getCoaSegment1(),
                                    arInvoice.getArTaxCode().getGlChartOfAccount().getCoaSegment3(),
                                    arInvoice.getArTaxCode().getGlChartOfAccount().getCoaSegment4(),
                                    arInvoice.getArTaxCode().getGlChartOfAccount().getCoaSegment5());
                            this.addArDrEntry(arInvoice.getArDrNextLine(), "TAX", EJBCommon.FALSE, TOTAL_TAX,
                                    glChartOfAccountHome.findByCoaAccountNumber(brTaxCOA, companyCode).getCoaCode(),
                                    EJBCommon.FALSE, arInvoice, branchCode, companyCode);
                        } else {
                            this.addArDrEntry(arInvoice.getArDrNextLine(), "TAX", EJBCommon.FALSE, TOTAL_TAX,
                                    arInvoice.getArTaxCode().getGlChartOfAccount().getCoaCode(),
                                    EJBCommon.FALSE, arInvoice, branchCode, companyCode);
                        }
                    }

                    // add cash distribution
                    LocalAdBranchCustomer adBranchCustomer = null;
                    try {
                        adBranchCustomer = adBranchCustomerHome.findBcstByCstCodeAndBrCode(arInvoice.getArCustomer().getCstCode(), branchCode, companyCode);
                    }
                    catch (FinderException ex) {
                        Debug.print("Finder Exception : " + ex.getMessage());
                    }

                    if (adBranchCustomer != null) {
                        this.addArDrEntry(arInvoice.getArDrNextLine(), "RECEIVABLE", EJBCommon.TRUE,
                                arInvoice.getInvAmountDue(), adBranchCustomer.getBcstGlCoaReceivableAccount(),
                                EJBCommon.FALSE, arInvoice, branchCode, companyCode);

                    } else {
                        this.addArDrEntry(arInvoice.getArDrNextLine(), "RECEIVABLE", EJBCommon.TRUE,
                                arInvoice.getInvAmountDue(), arInvoice.getArCustomer().getCstGlCoaReceivableAccount(),
                                EJBCommon.FALSE, arInvoice, branchCode, companyCode);
                    }


                    if (arModUploadReceiptDetails.getRctPosDiscount() != 0) {
                        // add discount
                        this.addArDrEntry(arInvoice.getArDrNextLine(), "DISCOUNT", EJBCommon.TRUE,
                                arModUploadReceiptDetails.getRctPosDiscount(), adPreference.getPrfMiscPosDiscountAccount(),
                                EJBCommon.FALSE, arInvoice, branchCode, companyCode);
                    }

                    if (arModUploadReceiptDetails.getRctPosScAmount() != 0) {
                        // add sc amount
                        this.addArDrEntry(arInvoice.getArDrNextLine(), "SERVCE CHARGE", EJBCommon.FALSE,
                                arModUploadReceiptDetails.getRctPosScAmount(), adPreference.getPrfMiscPosServiceChargeAccount(),
                                EJBCommon.FALSE, arInvoice, branchCode, companyCode);
                    }

                    if (arModUploadReceiptDetails.getRctPosDcAmount() != 0) {
                        // add dc amount
                        this.addArDrEntry(arInvoice.getArDrNextLine(), "DINEIN CHARGE", EJBCommon.FALSE,
                                arModUploadReceiptDetails.getRctPosDcAmount(), adPreference.getPrfMiscPosDineInChargeAccount(),
                                EJBCommon.FALSE, arInvoice, branchCode, companyCode);
                    }

                    // add forex gain/loss
                    double forexGainLoss = TOTAL_LINE - (arInvoice.getInvAmountDue() +
                            arModUploadReceiptDetails.getRctPosDiscount() -
                            arModUploadReceiptDetails.getRctPosScAmount() -
                            arModUploadReceiptDetails.getRctPosDcAmount());
                    if (forexGainLoss != 0) {
                        this.addArDrEntry(arInvoice.getArDrNextLine(), "FOREX", forexGainLoss > 0 ?
                                        EJBCommon.TRUE : EJBCommon.FALSE, Math.abs(forexGainLoss),
                                adPreference.getPrfMiscPosGiftCertificateAccount(),
                                EJBCommon.FALSE, arInvoice, branchCode, companyCode);
                    }
                }
                success = 1;
            }
            return success;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }
    private int setArMiscReceiptAllNewAndVoidUS(
            String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier, String companyShortName) {

        Debug.print("ArMiscReceiptSyncControllerBean setArMiscReceiptAllNewAndVoid");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(branchCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            Integer AD_BRNCH = adBranch.getBrCode();
            String BR_BRNCH_CODE = adBranch.getBrBranchCode();

            int success = 0;

            if (adPreference.getPrfMiscPosDiscountAccount() != null && adPreference.getPrfMiscPosGiftCertificateAccount() != null &&
                    adPreference.getPrfMiscPosServiceChargeAccount() != null && adPreference.getPrfMiscPosDineInChargeAccount() != null) {

                // new receipts
                HashMap<String, Object> receiptNumbers = new HashMap<>();
                HashMap<String, Object> invoiceNumbers = new HashMap<>();
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
                        String generatedReceipt;

                        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
                        LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;

                        try {
                            adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AR RECEIPT", companyCode);
                        }
                        catch (FinderException ex) {
                            Debug.print("Finder Exception : " + ex.getMessage());
                        }

                        try {
                            adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome
                                    .findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, companyCode);
                        }
                        catch (FinderException ex) {
                            Debug.print("Finder Exception : " + ex.getMessage());
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
                                Debug.print("Finder Exception : " + ex.getMessage());
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

                            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(arModReceiptDetails.getRctCstCustomerCode(), companyCode);
                            //arCustomer.addArReceipt(arReceipt);
                            arReceipt.setArCustomer(arCustomer);

                            String bankAccount = switch (BR_BRNCH_CODE) {
                                case "Manila-Smoking Lounge" -> "Allied Bank-IPT Smoking";
                                case "Manila-Cigar Shop" -> "Allied Bank Cigar shop";
                                case "Manila-Term.#2 Domestic" -> "Terminal II Domestic";
                                case "Manila-Term.#2 Intl" -> "Term2 International";
                                case "Cebu-Banilad" -> "Metrobank Banilad";
                                case "Cebu-Gorordo" -> "Metrobank-Gorordo";
                                case "Cebu-Mactan Domestic" -> "Metrobank Mactan Domestic";
                                case "Cebu-Mactan Intl" -> "Metrobank I Mactan Int'l";
                                case "Cebu-Supercat" -> "Metrobank Supercat";
                                default -> arCustomer.getAdBankAccount().getBaName();
                            };

                            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(bankAccount, companyCode);
                            //adBankAccount.addArReceipt(arReceipt);
                            arReceipt.setAdBankAccount(adBankAccount);

                            LocalArTaxCode arTaxCode;
                            if (arModReceiptDetails.getRctSource().trim().equalsIgnoreCase("R")) {
                                arTaxCode = arTaxCodeHome.findByTcName("VAT INCLUSIVE 7%", companyCode);
                                //arTaxCode.addArReceipt(arReceipt);
                            } else {
                                arTaxCode = arTaxCodeHome.findByTcName("VAT INCLUSIVE", companyCode);
                                //arTaxCode.addArReceipt(arReceipt);
                            }
                            arReceipt.setArTaxCode(arTaxCode);

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
                                double taxRate;
                                if (arModInvoiceLineItemDetails.getIliTaxCode().contains("Prepared Food Tax")) {
                                    taxRate = 7;
                                    preparedTax = preparedTax + (arModInvoiceLineItemDetails.getIliAmount() * (1 + taxRate / 100)) -
                                            EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount(), (short) 2);
                                } else if (arModInvoiceLineItemDetails.getIliTaxCode().contains("Sales Tax")) {
                                    taxRate = 5;
                                    salesTax = salesTax + (arModInvoiceLineItemDetails.getIliAmount() * (1 + taxRate / 100)) -
                                            EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount(), (short) 2);
                                } else {
                                    taxRate = arExistingReceipt.getArTaxCode().getTcRate();
                                }

                                LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.create(++lineNumber,
                                        arModInvoiceLineItemDetails.getIliQuantity(), arModInvoiceLineItemDetails.getIliUnitPrice(),
                                        EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() * (1 + taxRate / 100), (short) 2),
                                        (arModInvoiceLineItemDetails.getIliAmount() * (1 + taxRate / 100)) - EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount(), (short) 2),
                                        EJBCommon.FALSE, 0, 0, 0, 0, 0, EJBCommon.TRUE, companyCode);
                                //arReceipt.addArInvoiceLineItem(arInvoiceLineItem);
                                arInvoiceLineItem.setArReceipt(arReceipt);

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
                                    Debug.print("Finder Exception : " + ex.getMessage());
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

                            ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) receiptNumbers.get(arExistingReceipt.getRctNumber());

                            arModUploadReceiptDetails.setRctPosDiscount(arModUploadReceiptDetails.getRctPosDiscount() + EJBCommon.roundIt(arModReceiptDetails.getRctPosDiscount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosScAmount(arModUploadReceiptDetails.getRctPosScAmount() + EJBCommon.roundIt(arModReceiptDetails.getRctPosScAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosDcAmount(arModUploadReceiptDetails.getRctPosDcAmount() + EJBCommon.roundIt(arModReceiptDetails.getRctPosDcAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            receiptNumbers.put(arExistingReceipt.getRctNumber(), arModUploadReceiptDetails);

                            double totalAmount = EJBCommon.roundIt((arModReceiptDetails.getRctPosTotalAmount() - arModReceiptDetails.getRctPosVoidAmount()) * arModReceiptDetails.getRctConversionRate(), (short) 2);
                            arExistingReceipt.setRctAmount(arExistingReceipt.getRctAmount() + totalAmount);

                            for (Object o : arModReceiptDetails.getInvIliList()) {

                                ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = (ArModInvoiceLineItemDetails) o;
                                LocalInvItem invItem = invItemHome.findByIiName(arModInvoiceLineItemDetails.getIliIiName(), companyCode);
                                LocalInvLocation invLocation = null;

                                try {
                                    if (invItem.getIiDefaultLocation() != null) {
                                        invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                                    }
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
                                }

                                LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(arModInvoiceLineItemDetails.getIliIiName(),
                                        invLocation == null ? arModInvoiceLineItemDetails.getIliLocName() : invLocation.getLocName(), companyCode);

                                LocalArInvoiceLineItem arExistingInvoiceLineItem = null;

                                try {
                                    arExistingInvoiceLineItem = arInvoiceLineItemHome.findByRctCodeAndIlCodeAndUomName(arExistingReceipt.getRctCode(),
                                            invItemLocation.getIlCode(), arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
                                }

                                if (arExistingInvoiceLineItem == null) {

                                    double taxRate;
                                    if (arModInvoiceLineItemDetails.getIliTaxCode().contains("Prepared Food Tax")) {
                                        taxRate = 7;
                                        preparedTax = preparedTax + (arModInvoiceLineItemDetails.getIliAmount() * (1 + taxRate / 100)) - EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount(), (short) 2);
                                    } else if (arModInvoiceLineItemDetails.getIliTaxCode().contains("Sales Tax")) {
                                        taxRate = 5;
                                        salesTax = salesTax + (arModInvoiceLineItemDetails.getIliAmount() * (1 + taxRate / 100)) -
                                                EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount(), (short) 2);
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
                        String generatedInvoice;

                        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
                        LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;

                        try {
                            adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AR INVOICE", companyCode);
                        }
                        catch (FinderException ex) {
                            Debug.print("Finder Exception : " + ex.getMessage());
                        }

                        try {

                            adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, companyCode);

                        }
                        catch (FinderException ex) {
                            Debug.print("Finder Exception : " + ex.getMessage());
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
                                Debug.print("Finder Exception : " + ex.getMessage());
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
                                    (byte) 0, "POS Sales " + new Date(), arModReceiptDetails.getRctDate(), generatedInvoice, null, null,
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

                            if (adCompany.getCmpShortName().equalsIgnoreCase("tinderbox") || adCompany.getCmpShortName().equalsIgnoreCase("hs")) {
                                LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName("IMMEDIATE", companyCode);
                                //adPaymentTerm.addArInvoice(arInvoice);
                                arInvoice.setAdPaymentTerm(adPaymentTerm);
                            } else {
                                LocalAdPaymentTerm adPaymentTerm;
                                if (arModReceiptDetails.getRctPaymentMethod() == null || arModReceiptDetails.getRctPaymentMethod().equals("")) {
                                    adPaymentTerm = adPaymentTermHome.findByPytName("IMMEDIATE", companyCode);
                                    //adPaymentTerm.addArInvoice(arInvoice);
                                } else {
                                    adPaymentTerm = adPaymentTermHome.findByPytName(arModReceiptDetails.getRctPaymentMethod(), companyCode);
                                    //adPaymentTerm.addArInvoice(arInvoice);
                                }
                                arInvoice.setAdPaymentTerm(adPaymentTerm);
                            }

                            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                            //glFunctionalCurrency.addArInvoice(arInvoice);
                            arInvoice.setGlFunctionalCurrency(glFunctionalCurrency);

                            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(arModReceiptDetails.getRctCstCustomerCode(), companyCode);
                            //arCustomer.addArInvoice(arInvoice);
                            arInvoice.setArCustomer(arCustomer);

                            if (arModReceiptDetails.getRctSource().trim().equalsIgnoreCase("R")) {
                                if (adCompany.getCmpShortName().equalsIgnoreCase("tinderbox") || adCompany.getCmpShortName().equalsIgnoreCase("hs")) {
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
                                if (adCompany.getCmpShortName().equalsIgnoreCase("tinderbox") || adCompany.getCmpShortName().equalsIgnoreCase("hs")) {
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

                            LocalArInvoiceBatch arInvoiceBatch;
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
                            while (iter.hasNext()) {

                                ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = (ArModInvoiceLineItemDetails) iter.next();

                                double taxRate;
                                if (arModInvoiceLineItemDetails.getIliTaxCode().contains("Prepared Food Tax")) {
                                    taxRate = 7;
                                    preparedTax = preparedTax + (arModInvoiceLineItemDetails.getIliAmount() * (1 + taxRate / 100)) - EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount(), (short) 2);
                                } else if (arModInvoiceLineItemDetails.getIliTaxCode().contains("Sales Tax")) {
                                    taxRate = 5;
                                    salesTax = salesTax + (arModInvoiceLineItemDetails.getIliAmount() * (1 + taxRate / 100)) -
                                            EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount(), (short) 2);
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

                                LocalInvItem invItem = invItemHome.findByIiName(arModInvoiceLineItemDetails.getIliIiName(), companyCode);
                                LocalInvLocation invLocation = null;

                                try {
                                    if (invItem.getIiDefaultLocation() != null) {
                                        invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                                    }
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
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

                            for (Object o : arModReceiptDetails.getInvIliList()) {
                                ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = (ArModInvoiceLineItemDetails) o;
                                LocalInvItem invItem = invItemHome.findByIiName(arModInvoiceLineItemDetails.getIliIiName(), companyCode);
                                LocalInvLocation invLocation = null;
                                try {
                                    if (invItem.getIiDefaultLocation() != null) {
                                        invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                                    }
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
                                }

                                LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(arModInvoiceLineItemDetails.getIliIiName(),
                                        invLocation == null ? arModInvoiceLineItemDetails.getIliLocName() : invLocation.getLocName(), companyCode);

                                LocalArInvoiceLineItem arExistingInvoiceLineItem = null;
                                try {
                                    arExistingInvoiceLineItem = arInvoiceLineItemHome.findByRctCodeAndIlCodeAndUomName(arExistingInvoice.getInvCode(),
                                            invItemLocation.getIlCode(), arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
                                }

                                if (arExistingInvoiceLineItem == null) {
                                    double taxRate;
                                    if (arModInvoiceLineItemDetails.getIliTaxCode().contains("Prepared Food Tax")) {
                                        taxRate = 7;
                                        preparedTax = preparedTax + (arModInvoiceLineItemDetails.getIliAmount() *
                                                (1 + taxRate / 100)) - EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount(), (short) 2);
                                    } else if (arModInvoiceLineItemDetails.getIliTaxCode().contains("Sales Tax")) {
                                        taxRate = 5;
                                        salesTax = salesTax + (arModInvoiceLineItemDetails.getIliAmount() * (1 + taxRate / 100)) -
                                                EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount(), (short) 2);
                                    } else {
                                        taxRate = arExistingInvoice.getArTaxCode().getTcRate();
                                    }

                                    LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.create(
                                            (short)(arExistingInvoice.getArInvoiceLineItems().size() + 1),
                                            arModInvoiceLineItemDetails.getIliQuantity(), arModInvoiceLineItemDetails.getIliUnitPrice(),
                                            EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() * (1 + taxRate / 100), (short) 2),
                                            (arModInvoiceLineItemDetails.getIliAmount() * (1 + taxRate / 100)) -
                                                    EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount(), (short) 2),
                                            EJBCommon.FALSE, 0, 0, 0, 0, 0, EJBCommon.TRUE, companyCode);

                                    //arExistingInvoice.addArInvoiceLineItem(arInvoiceLineItem);
                                    arInvoiceLineItem.setArInvoice(arExistingInvoice);

                                    LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome
                                            .findByUomName(arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                    //invUnitOfMeasure.addArInvoiceLineItem(arInvoiceLineItem);
                                    arInvoiceLineItem.setInvUnitOfMeasure(invUnitOfMeasure);

                                    //invItemLocation.addArInvoiceLineItem(arInvoiceLineItem);
                                    arInvoiceLineItem.setInvItemLocation(invItemLocation);
                                    if (invItemLocation.getInvItem().getIiEnableAutoBuild() == EJBCommon.TRUE) {

                                        arInvoiceLineItem.setIliEnableAutoBuild(EJBCommon.TRUE);

                                    }
                                } else {

                                    arExistingInvoiceLineItem.setIliQuantity(arExistingInvoiceLineItem.getIliQuantity() +
                                            arModInvoiceLineItemDetails.getIliQuantity());
                                    arExistingInvoiceLineItem.setIliUnitPrice((arExistingInvoiceLineItem.getIliUnitPrice() +
                                            arModInvoiceLineItemDetails.getIliUnitPrice()) / 2);
                                    arExistingInvoiceLineItem.setIliAmount(arExistingInvoiceLineItem.getIliAmount() +
                                            EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() /
                                                    (1 + arExistingInvoice.getArTaxCode().getTcRate() / 100), (short) 2));
                                    arExistingInvoiceLineItem.setIliTaxAmount(arExistingInvoiceLineItem.getIliTaxAmount() +
                                            EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() -
                                                    (arModInvoiceLineItemDetails.getIliAmount() /
                                                            (1 + arExistingInvoice.getArTaxCode().getTcRate() / 100)), (short) 2));
                                }
                            }
                        }
                    }
                }

                // for each receipt generate distribution records
                for (Object o : receiptNumbers.values()) {
                    ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) o;

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

                        double COST;

                        try {

                            LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                    arReceipt.getRctDate(), invItemLocation.getInvItem().getIiName(),
                                    invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);

                            COST = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                        }
                        catch (FinderException ex) {

                            COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                        }

                        double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(),
                                arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                        LocalAdBranchItemLocation adBranchItemLocation = null;
                        try {
                            adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(
                                    arInvoiceLineItem.getInvItemLocation().getIlCode(), AD_BRNCH, companyCode);
                        }
                        catch (FinderException ex) {
                            Debug.print("Finder Exception : " + ex.getMessage());
                        }

                        if (arInvoiceLineItem.getIliEnableAutoBuild() == 0
                                || arInvoiceLineItem.getIliEnableAutoBuild() == 1
                                || arInvoiceLineItem.getInvItemLocation().getInvItem().getIiClass().equals("Stock")) {

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
                            double convertedQuantity = this.convertByUomFromAndItemAndQuantity(
                                    arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(),
                                    arInvoiceLineItem.getIliQuantity(), companyCode);
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
                    if (!arReceipt.getArTaxCode().getTcType().equals("NONE") &&
                            !arReceipt.getArTaxCode().getTcType().equals("EXEMPT")) {

                        if (adCompany.getCmpShortName().equalsIgnoreCase("tinderbox")) {
                            String brTaxCOA = this.getBranchTaxCOA(BR_BRNCH_CODE,
                                    arReceipt.getArTaxCode().getGlChartOfAccount().getCoaSegment1(),
                                    arReceipt.getArTaxCode().getGlChartOfAccount().getCoaSegment3(),
                                    arReceipt.getArTaxCode().getGlChartOfAccount().getCoaSegment4(),
                                    arReceipt.getArTaxCode().getGlChartOfAccount().getCoaSegment5());

                            LocalArTaxCode arTaxCodePreparedFoodTax = null;
                            if (preparedTax == 0 && salesTax == 0) {
                                this.addArDrEntry(arReceipt.getArDrNextLine(),
                                        "TAX", EJBCommon.FALSE, TOTAL_TAX, arReceipt.getArTaxCode().getGlChartOfAccount().getCoaCode(),
                                        EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                            } else {
                                if (preparedTax != 0) {
                                    try {
                                        arTaxCodePreparedFoodTax = arTaxCodeHome.findByTcName("Prepared Food Tax", companyCode);
                                    }
                                    catch (FinderException ex) {
                                        Debug.print("Finder Exception : " + ex.getMessage());
                                    }

                                    if (arTaxCodePreparedFoodTax != null) {
                                        this.addArDrEntry(arReceipt.getArDrNextLine(),
                                                "TAX", EJBCommon.FALSE, preparedTax2, arTaxCodePreparedFoodTax.getGlChartOfAccount().getCoaCode(),
                                                EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                                    }

                                }
                                LocalArTaxCode arTaxCodeSalesTax = null;
                                if (salesTax2 != 0) {
                                    try {
                                        arTaxCodeSalesTax = arTaxCodeHome.findByTcName("Sales Tax", companyCode);
                                    }
                                    catch (FinderException ex) {
                                        Debug.print("Finder Exception : " + ex.getMessage());
                                    }

                                    if (arTaxCodeSalesTax != null) {
                                        this.addArDrEntry(arReceipt.getArDrNextLine(),
                                                "TAX", EJBCommon.FALSE, salesTax2, arTaxCodeSalesTax.getGlChartOfAccount().getCoaCode(),
                                                EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                                    }
                                }
                            }
                        } else {
                            if (preparedTax == 0 && salesTax == 0) {
                                this.addArDrEntry(arReceipt.getArDrNextLine(),
                                        "TAX", EJBCommon.FALSE, TOTAL_TAX, arReceipt.getArTaxCode().getGlChartOfAccount().getCoaCode(),
                                        EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

                            } else {
                                LocalArTaxCode arTaxCodePreparedFoodTax = null;
                                if (preparedTax != 0) {
                                    try {
                                        arTaxCodePreparedFoodTax = arTaxCodeHome.findByTcName("Prepared Food Tax", companyCode);
                                    }
                                    catch (FinderException ex) {
                                        Debug.print("Finder Exception : " + ex.getMessage());
                                    }

                                    if (arTaxCodePreparedFoodTax != null) {
                                        this.addArDrEntry(arReceipt.getArDrNextLine(),
                                                "TAX", EJBCommon.FALSE, preparedTax2, arTaxCodePreparedFoodTax.getGlChartOfAccount().getCoaCode(),
                                                EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                                    }

                                }
                                LocalArTaxCode arTaxCodeSalesTax = null;
                                if (salesTax != 0) {
                                    try {
                                        arTaxCodeSalesTax = arTaxCodeHome.findByTcName("Sales Tax", companyCode);
                                    }
                                    catch (FinderException ex) {
                                        Debug.print("Finder Exception : " + ex.getMessage());
                                    }
                                    if (arTaxCodeSalesTax != null) {
                                        this.addArDrEntry(arReceipt.getArDrNextLine(),
                                                "TAX", EJBCommon.FALSE, salesTax2, arTaxCodeSalesTax.getGlChartOfAccount().getCoaCode(),
                                                EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                                    }
                                }
                            }
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
                    double forexGainLoss = TOTAL_LINE - (arReceipt.getRctAmount() +
                            arModUploadReceiptDetails.getRctPosDiscount() -
                            arModUploadReceiptDetails.getRctPosScAmount() -
                            arModUploadReceiptDetails.getRctPosDcAmount());
                    if (forexGainLoss != 0) {
                        this.addArDrEntry(arReceipt.getArDrNextLine(), "FOREX",
                                forexGainLoss > 0 ? EJBCommon.TRUE : EJBCommon.FALSE, Math.abs(forexGainLoss),
                                adPreference.getPrfMiscPosGiftCertificateAccount(),
                                EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                    }
                }

                for (Object o : invoiceNumbers.values()) {

                    ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) o;

                    LocalArInvoice arInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(
                            arModUploadReceiptDetails.getRctNumber(), (byte) 0, AD_BRNCH, companyCode);
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
                        switch (arInvoice.getAdPaymentTerm().getPytScheduleBasis()) {
                            case "DEFAULT" -> {
                                gcDateDue.setTime(arInvoice.getInvEffectivityDate());
                                gcDateDue.add(Calendar.DATE, adPaymentSchedule.getPsDueDay());
                            }
                            case "MONTHLY" -> {
                                gcDateDue = gcPrevDateDue;
                                gcDateDue.add(Calendar.MONTH, 1);
                                gcPrevDateDue = gcDateDue;
                            }
                            case "BI-MONTHLY" -> {
                                gcDateDue = gcPrevDateDue;
                                if (gcPrevDateDue.get(Calendar.MONTH) != 1) {
                                    if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 31
                                            && gcPrevDateDue.get(Calendar.DATE) > 15
                                            && gcPrevDateDue.get(Calendar.DATE) < 31) {
                                        gcDateDue.add(Calendar.DATE, 16);
                                    } else {
                                        gcDateDue.add(Calendar.DATE, 15);
                                    }
                                } else if (gcPrevDateDue.get(Calendar.MONTH) == 1) {
                                    if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 28
                                            && gcPrevDateDue.get(Calendar.DATE) == 14) {
                                        gcDateDue.add(Calendar.DATE, 14);
                                    } else if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 28
                                            && gcPrevDateDue.get(Calendar.DATE) >= 15
                                            && gcPrevDateDue.get(Calendar.DATE) < 28) {
                                        gcDateDue.add(Calendar.DATE, 13);
                                    } else if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 29
                                            && gcPrevDateDue.get(Calendar.DATE) >= 15
                                            && gcPrevDateDue.get(Calendar.DATE) < 29) {
                                        gcDateDue.add(Calendar.DATE, 14);
                                    } else {
                                        gcDateDue.add(Calendar.DATE, 15);
                                    }
                                }
                                gcPrevDateDue = gcDateDue;
                            }
                        }

                        // create a payment schedule
                        double PAYMENT_SCHEDULE_AMOUNT;

                        // if last payment schedule subtract to avoid rounding difference error
                        if (i.hasNext()) {
                            PAYMENT_SCHEDULE_AMOUNT = EJBCommon.roundIt((adPaymentSchedule.getPsRelativeAmount()
                                    / arInvoice.getAdPaymentTerm().getPytBaseAmount()) * arInvoice.getInvAmountDue(), precisionUnit);
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

                    double TOTAL_LINE = 0;

                    while (lineIter.hasNext()) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) lineIter.next();

                        TOTAL_LINE += arInvoiceLineItem.getIliTaxAmount() + arInvoiceLineItem.getIliAmount();

                        LocalInvItemLocation invItemLocation = arInvoiceLineItem.getInvItemLocation();

                        // add cost of sales distribution and inventory

                        double COST;

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

                            adBranchItemLocation = adBranchItemLocationHome
                                    .findBilByIlCodeAndBrCode(arInvoiceLineItem.getInvItemLocation().getIlCode(), AD_BRNCH, companyCode);

                        }
                        catch (FinderException ex) {
                            Debug.print("Finder Exception : " + ex.getMessage());
                        }

                        if (arInvoiceLineItem.getIliEnableAutoBuild() == 0
                                || arInvoiceLineItem.getIliEnableAutoBuild() == 1
                                || arInvoiceLineItem.getInvItemLocation().getInvItem().getIiClass().equals("Stock")) {

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

                        if (adCompany.getCmpShortName().equalsIgnoreCase("tinderbox")) {
                            String brTaxCOA = this.getBranchTaxCOA(BR_BRNCH_CODE,
                                    arInvoice.getArTaxCode().getGlChartOfAccount().getCoaSegment1(),
                                    arInvoice.getArTaxCode().getGlChartOfAccount().getCoaSegment3(),
                                    arInvoice.getArTaxCode().getGlChartOfAccount().getCoaSegment4(),
                                    arInvoice.getArTaxCode().getGlChartOfAccount().getCoaSegment5());

                            LocalArTaxCode arTaxCodePreparedFoodTax = null;
                            if (preparedTax != 0) {
                                try {
                                    arTaxCodePreparedFoodTax = arTaxCodeHome.findByTcName("Prepared Food Tax", companyCode);
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
                                }

                                if (arTaxCodePreparedFoodTax != null) {
                                    this.addArDrEntry(arInvoice.getArDrNextLine(),
                                            "TAX", EJBCommon.FALSE, preparedTaxInvoice2, arTaxCodePreparedFoodTax.getGlChartOfAccount().getCoaCode(),
                                            EJBCommon.FALSE, arInvoice, AD_BRNCH, companyCode);
                                }

                            }
                            LocalArTaxCode arTaxCodeSalesTax = null;
                            if (salesTax != 0) {
                                try {
                                    arTaxCodeSalesTax = arTaxCodeHome.findByTcName("Sales Tax", companyCode);
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
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
                                    Debug.print("Finder Exception : " + ex.getMessage());
                                }

                                if (arTaxCodePreparedFoodTax != null) {
                                    this.addArDrEntry(arInvoice.getArDrNextLine(),
                                            "TAX", EJBCommon.FALSE, preparedTaxInvoice2, arTaxCodePreparedFoodTax.getGlChartOfAccount().getCoaCode(),
                                            EJBCommon.FALSE, arInvoice, AD_BRNCH, companyCode);
                                }
                            }
                            LocalArTaxCode arTaxCodeSalesTax = null;
                            if (salesTax != 0) {
                                try {
                                    arTaxCodeSalesTax = arTaxCodeHome.findByTcName("Sales Tax", companyCode);
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
                                }

                                if (arTaxCodeSalesTax != null) {
                                    this.addArDrEntry(arInvoice.getArDrNextLine(),
                                            "TAX", EJBCommon.FALSE, salesTaxInvoice2, arTaxCodeSalesTax.getGlChartOfAccount().getCoaCode(),
                                            EJBCommon.FALSE, arInvoice, AD_BRNCH, companyCode);
                                }

                            }
                        }
                    }

                    // add cash distribution
                    LocalAdBranchCustomer adBranchCustomer = null;
                    try {
                        adBranchCustomer = adBranchCustomerHome.findBcstByCstCodeAndBrCode(arInvoice.getArCustomer().getCstCode(), AD_BRNCH, companyCode);
                    }
                    catch (FinderException ex) {
                        Debug.print("Finder Exception : " + ex.getMessage());
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
                    double forexGainLoss = TOTAL_LINE - (arInvoice.getInvAmountDue()
                            + arModUploadReceiptDetails.getRctPosDiscount() - arModUploadReceiptDetails.getRctPosScAmount()
                            - arModUploadReceiptDetails.getRctPosDcAmount());
                    if (forexGainLoss != 0) {
                        this.addArDrEntry(arInvoice.getArDrNextLine(), "FOREX",
                                forexGainLoss > 0 ? EJBCommon.TRUE : EJBCommon.FALSE, Math.abs(forexGainLoss),
                                adPreference.getPrfMiscPosGiftCertificateAccount(),
                                EJBCommon.FALSE, arInvoice, AD_BRNCH, companyCode);
                    }

                }
                success = 1;
            }
            return success;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }
    private int setArMiscReceiptAllNewAndVoidWithExpiryDate(
            String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier, String companyShortName) {

        Debug.print("ArMiscReceiptSyncControllerBean setArMiscReceiptAllNewAndVoidWithExpiryDate");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(branchCode);
            Integer AD_BRNCH = adBranch.getBrCode();

            int success = 0;

            if (adPreference.getPrfMiscPosDiscountAccount() != null && adPreference.getPrfMiscPosGiftCertificateAccount() != null &&
                    adPreference.getPrfMiscPosServiceChargeAccount() != null && adPreference.getPrfMiscPosDineInChargeAccount() != null) {

                // new receipts
                HashMap<String, Object> receiptNumbers = new HashMap<>();

                for (String receipt : receipts) {

                    ArModReceiptDetails arModReceiptDetails = receiptDecodeWithExpiryDate(receipt);
                    String reference_number = arModReceiptDetails.getRctNumber();
                    String customerName = arModReceiptDetails.getRctCstName();
                    String customerAddress = arModReceiptDetails.getRctCustomerAddress();

                    if (arModReceiptDetails.getRctPosOnAccount() == 0) {

                        // generate receipt number
                        String generatedReceipt;

                        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
                        LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;

                        try {
                            adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AR RECEIPT", companyCode);
                        }
                        catch (FinderException ex) {
                            Debug.print("Finder Exception : " + ex.getMessage());
                        }

                        try {
                            assert adDocumentSequenceAssignment != null;
                            adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome
                                    .findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, companyCode);
                        }
                        catch (FinderException ex) {
                            Debug.print("Finder Exception : " + ex.getMessage());
                        }

                        while (true) {
                            if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {
                                try {
                                    arReceiptHome.findByRctNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, companyCode);
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

                        Collection existingReceipts = arReceiptHome.findByRctRfrncNmbrAndBrCode(reference_number, AD_BRNCH, companyCode);

                        //check duplicate receipt if found go to next
                        if (existingReceipts.size() > 0) {
                            continue;
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
                                adBankAccount = adBankAccountHome.findByBaName(arModReceiptDetails.getRctCardType1(), companyCode);
                                arReceipt.setAdBankAccountCard1(adBankAccount);
                            }


                            //card 2 bank account
                            if (!arModReceiptDetails.getRctCardType2().equals("") && arModReceiptDetails.getRctAmountCard2() > 0) {
                                adBankAccount = adBankAccountHome.findByBaName(arModReceiptDetails.getRctCardType2(), companyCode);
                                arReceipt.setAdBankAccountCard2(adBankAccount);
                            }


                            //card 3 bank account
                            if (!arModReceiptDetails.getRctCardType3().equals("") && arModReceiptDetails.getRctAmountCard3() > 0) {
                                adBankAccount = adBankAccountHome.findByBaName(arModReceiptDetails.getRctCardType3(), companyCode);
                                arReceipt.setAdBankAccountCard3(adBankAccount);
                            }

                            LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName(arModReceiptDetails.getRctTcName(), companyCode);
                            arReceipt.setArTaxCode(arTaxCode);

                            LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName("NONE", companyCode);
                            arReceipt.setArWithholdingTaxCode(arWithholdingTaxCode);

                            LocalArReceiptBatch arReceiptBatch;
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

                                double ILI_AMNT;
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
                                if (!arTaxCode.getTcType().equals("NONE") && !arTaxCode.getTcType().equals("EXEMPT")) {
                                    if (arTaxCode.getTcType().equals("INCLUSIVE")) {
                                        ILI_TAX_AMNT = EJBCommon.roundIt(ILI_NET_AMOUNT - ILI_AMNT, precisionUnit);
                                    } else if (arTaxCode.getTcType().equals("EXCLUSIVE")) {
                                        ILI_TAX_AMNT = EJBCommon.roundIt(ILI_NET_AMOUNT * arTaxCode.getTcRate() / 100, precisionUnit);
                                    }
                                }

                                LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.create(
                                        ++lineNumber, arModInvoiceLineItemDetails.getIliQuantity(),
                                        arModInvoiceLineItemDetails.getIliUnitPrice(),
                                        ILI_AMNT, ILI_TAX_AMNT,
                                        EJBCommon.FALSE, 0,
                                        0, 0, 0,
                                        arModInvoiceLineItemDetails.getIliTotalDiscount(), EJBCommon.TRUE, companyCode);


                                arInvoiceLineItem.setArReceipt(arReceipt);

                                LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(
                                        arModInvoiceLineItemDetails.getIliUomName(), companyCode);
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
                                    Debug.print("Finder Exception : " + ex.getMessage());
                                }

                                LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiCodeAndLocName(arModInvoiceLineItemDetails.getIliCode(),
                                        invLocation == null ? arModInvoiceLineItemDetails.getIliLocName() : invLocation.getLocName(), companyCode);
                                arInvoiceLineItem.setInvItemLocation(invItemLocation);

                                if (invItemLocation.getInvItem().getIiEnableAutoBuild() == EJBCommon.TRUE) {
                                    arInvoiceLineItem.setIliEnableAutoBuild(EJBCommon.TRUE);
                                }

                                //add serial into inv tag
                                if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiTraceMisc() == 1) {
                                    ArrayList tagList = arModInvoiceLineItemDetails.getiIliTagList();
                                    for (Object o : tagList) {
                                        InvModTagListDetails details = (InvModTagListDetails) o;
                                        LocalInvTag invTag = invTagHome.create(details.getTgPropertyCode(),
                                                details.getTgSerialNumber(), details.getTgDocumentNumber(),
                                                details.getTgExpiryDate(), details.getTgSpecs(), companyCode,
                                                details.getTgTransactionDate(), "IN");
                                        invTag.setArInvoiceLineItem(arInvoiceLineItem);
                                        invTag.setInvItemLocation(arInvoiceLineItem.getInvItemLocation());
                                    }
                                }
                            }
                        } else {

                            ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) receiptNumbers.get(arExistingReceipt.getRctNumber());

                            arModUploadReceiptDetails.setRctPosDiscount(
                                    arModUploadReceiptDetails.getRctPosDiscount() +
                                            EJBCommon.roundIt(arModReceiptDetails.getRctPosDiscount() *
                                                    arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosScAmount(
                                    arModUploadReceiptDetails.getRctPosScAmount() +
                                            EJBCommon.roundIt(arModReceiptDetails.getRctPosScAmount() *
                                                    arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosDcAmount(
                                    arModUploadReceiptDetails.getRctPosDcAmount() +
                                            EJBCommon.roundIt(arModReceiptDetails.getRctPosDcAmount() *
                                                    arModReceiptDetails.getRctConversionRate(), (short) 2));

                            receiptNumbers.put(arExistingReceipt.getRctNumber(), arModUploadReceiptDetails);

                            double totalAmount = EJBCommon.roundIt((arModReceiptDetails.getRctPosTotalAmount() -
                                    arModReceiptDetails.getRctPosVoidAmount()) * arModReceiptDetails.getRctConversionRate(), (short) 2);
                            arExistingReceipt.setRctAmount(arExistingReceipt.getRctAmount() + totalAmount);

                            for (Object o : arModReceiptDetails.getInvIliList()) {

                                ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = (ArModInvoiceLineItemDetails) o;
                                LocalInvItem invItem = invItemHome.findByIiName(arModInvoiceLineItemDetails.getIliIiName(), companyCode);

                                LocalInvLocation invLocation = null;
                                try {
                                    if (invItem.getIiDefaultLocation() != null) {
                                        invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                                    }
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
                                }

                                LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(arModInvoiceLineItemDetails.getIliIiName(),
                                        invLocation == null ? arModInvoiceLineItemDetails.getIliLocName() : invLocation.getLocName(), companyCode);

                                LocalArInvoiceLineItem arExistingInvoiceLineItem = null;
                                try {
                                    arExistingInvoiceLineItem = arInvoiceLineItemHome.findByRctCodeAndIlCodeAndUomName(arExistingReceipt.getRctCode(),
                                            invItemLocation.getIlCode(), arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
                                }

                                if (arExistingInvoiceLineItem == null) {
                                    LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.create((short) (arExistingReceipt.getArInvoiceLineItems().size() + 1),
                                            arModInvoiceLineItemDetails.getIliQuantity(), arModInvoiceLineItemDetails.getIliUnitPrice(),
                                            EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingReceipt.getArTaxCode().getTcRate() / 100), (short) 2), EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() - (arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingReceipt.getArTaxCode().getTcRate() / 100)), (short) 2),
                                            EJBCommon.FALSE, 0, 0, 0, 0, 0, EJBCommon.TRUE, companyCode);

                                    arInvoiceLineItem.setArReceipt(arExistingReceipt);

                                    LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome
                                            .findByUomName(arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                    arInvoiceLineItem.setInvUnitOfMeasure(invUnitOfMeasure);
                                    arInvoiceLineItem.setInvItemLocation(invItemLocation);
                                    arInvoiceLineItem.setIliMisc(arModInvoiceLineItemDetails.getIliMisc());
                                    if (invItemLocation.getInvItem().getIiEnableAutoBuild() == EJBCommon.TRUE) {

                                        arInvoiceLineItem.setIliEnableAutoBuild(EJBCommon.TRUE);

                                    }
                                } else {

                                    arExistingInvoiceLineItem.setIliQuantity(
                                            arExistingInvoiceLineItem.getIliQuantity() + arModInvoiceLineItemDetails.getIliQuantity());
                                    arExistingInvoiceLineItem.setIliUnitPrice(
                                            (arExistingInvoiceLineItem.getIliUnitPrice() + arModInvoiceLineItemDetails.getIliUnitPrice()) / 2);
                                    arExistingInvoiceLineItem.setIliAmount(
                                            arExistingInvoiceLineItem.getIliAmount() +
                                                    EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() /
                                                            (1 + arExistingReceipt.getArTaxCode().getTcRate() / 100), (short) 2));
                                    arExistingInvoiceLineItem.setIliTaxAmount(
                                            arExistingInvoiceLineItem.getIliTaxAmount() +
                                                    EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() -
                                                            (arModInvoiceLineItemDetails.getIliAmount() /
                                                                    (1 + arExistingReceipt.getArTaxCode().getTcRate() / 100)), (short) 2));

                                }
                            }
                        }

                    } else {

                        // generate Invoice number
                        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
                        LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;

                        try {

                            adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AR INVOICE", companyCode);

                        }
                        catch (FinderException ex) {
                            Debug.print("Finder Exception : " + ex.getMessage());
                        }

                        try {
                            assert adDocumentSequenceAssignment != null;
                            adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome
                                    .findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, companyCode);
                        }
                        catch (FinderException ex) {
                            Debug.print("Finder Exception : " + ex.getMessage());
                        }

                        while (true) {

                            if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                                try {

                                    arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), (byte) 0, AD_BRNCH, companyCode);
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                                }
                                catch (FinderException ex) {

                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                    break;

                                }

                            } else {

                                try {

                                    arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), (byte) 0, AD_BRNCH, companyCode);
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                                }
                                catch (FinderException ex) {

                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                    break;

                                }

                            }

                        }
                    }
                }

                // for each receipt generate distribution records
                for (Object o : receiptNumbers.values()) {
                    ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) o;
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

                            switch (invCosting.getInvItemLocation().getInvItem().getIiCostMethod()) {
                                case "Average" -> {
                                    if (invCosting.getCstRemainingQuantity() <= 0) {
                                        COST = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                                    } else {
                                        COST = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                                        if (COST <= 0) {
                                            COST = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                                        }
                                    }
                                }
                                case "FIFO" ->
                                        COST = Math.abs(this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(),
                                                arInvoiceLineItem.getIliQuantity(), arInvoiceLineItem.getIliUnitPrice(), false, AD_BRNCH, companyCode));
                                case "Standard" -> COST = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
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
                            Debug.print("Finder Exception : " + ex.getMessage());
                        }

                        if ((arInvoiceLineItem.getIliEnableAutoBuild() == 0
                                || arInvoiceLineItem.getInvItemLocation().getInvItem().getIiClass().equals("Stock"))
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
                    if (!arReceipt.getArTaxCode().getTcType().equals("NONE")
                            && !arReceipt.getArTaxCode().getTcType().equals("EXEMPT")) {
                        LocalAdBranchArTaxCode adBranchTaxCode = null;

                        try {
                            adBranchTaxCode = adBranchArTaxCodeHome
                                    .findBtcByTcCodeAndBrCode(arReceipt.getArTaxCode().getTcCode(), AD_BRNCH, companyCode);
                        }
                        catch (FinderException ex) {
                            Debug.print("Finder Exception : " + ex.getMessage());
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
                            adBranchBankAccountCard1 = adBranchBankAccountHome
                                    .findBbaByBaCodeAndBrCode(arReceipt.getAdBankAccountCard1().getBaCode(), AD_BRNCH, companyCode);
                        }
                        catch (FinderException ex) {
                            Debug.print("Finder Exception : " + ex.getMessage());
                        }

                        if (adBranchBankAccountCard1 != null) {

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
                            adBranchBankAccountCard2 = adBranchBankAccountHome
                                    .findBbaByBaCodeAndBrCode(arReceipt.getAdBankAccountCard2().getBaCode(), AD_BRNCH, companyCode);
                        }
                        catch (FinderException ex) {
                            Debug.print("Finder Exception : " + ex.getMessage());
                        }

                        if (adBranchBankAccountCard2 != null) {
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
                            adBranchBankAccountCard3 = adBranchBankAccountHome
                                    .findBbaByBaCodeAndBrCode(arReceipt.getAdBankAccountCard3().getBaCode(), AD_BRNCH, companyCode);
                        }
                        catch (FinderException ex) {
                            Debug.print("Finder Exception : " + ex.getMessage());
                        }

                        if (adBranchBankAccountCard3 != null) {
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
                        Debug.print("Finder Exception : " + ex.getMessage());
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
                                        arReceipt.getRctAmountCheque() - arReceipt.getRctAmountCard1() -
                                        arReceipt.getRctAmountCard2() - arReceipt.getRctAmountCard3(),
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
                }

                success = 1;
            }
            return success;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private int setArMiscReceiptAllNewAndVoidWithExpiryDateEnableItemAutoBuild(
            String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier, String companyShortName) {

        Debug.print("ArMiscReceiptSyncControllerBean setArMiscReceiptAllNewAndVoidWithExpiryDate");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(branchCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            Integer AD_BRNCH = adBranch.getBrCode();
            String BR_BRNCH_CODE = adBranch.getBrBranchCode();

            int success = 0;

            if (adPreference.getPrfMiscPosDiscountAccount() != null && adPreference.getPrfMiscPosGiftCertificateAccount() != null &&
                    adPreference.getPrfMiscPosServiceChargeAccount() != null && adPreference.getPrfMiscPosDineInChargeAccount() != null) {

                // new receipts
                HashMap<String, Object> receiptNumbers = new HashMap<>();
                HashMap<String, Object> invoiceNumbers = new HashMap<>();

                for (String receipt : receipts) {
                    ArModReceiptDetails arModReceiptDetails = receiptDecodeWithExpiryDate(receipt);
                    String reference_number = arModReceiptDetails.getRctNumber();
                    if (arModReceiptDetails.getRctPosOnAccount() == 0) {

                        // generate receipt number
                        String generatedReceipt;

                        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
                        LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;

                        try {
                            adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AR RECEIPT", companyCode);
                        }
                        catch (FinderException ex) {
                            Debug.print("Finder Exception : " + ex.getMessage());
                        }

                        try {
                            assert adDocumentSequenceAssignment != null;
                            adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome
                                    .findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, companyCode);
                        }
                        catch (FinderException ex) {
                            Debug.print("Finder Exception : " + ex.getMessage());
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
                                Debug.print("Finder Exception : " + ex.getMessage());
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

                            double totalAmount = EJBCommon.roundIt((arModReceiptDetails.getRctPosTotalAmount() - arModReceiptDetails.getRctPosVoidAmount()) * arModReceiptDetails.getRctConversionRate(), (short) 2);

                            LocalArReceipt arReceipt = arReceiptHome.create("MISC",
                                    "POS Sales " + new Date(), arModReceiptDetails.getRctDate(), generatedReceipt, null, null, null,
                                    null, null, null, null, null,
                                    totalAmount, 0d, 0d, 0d, 0d, 0d, 0d,
                                    null, 1, null, "CASH", EJBCommon.FALSE,
                                    0, null, null, EJBCommon.FALSE, null, EJBCommon.FALSE,
                                    EJBCommon.FALSE,
                                    EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE,
                                    null, null, null, null, null,
                                    cashier, EJBCommon.getGcCurrentDateWoTime().getTime(),
                                    cashier, EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, null, null,
                                    EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, null, null,
                                    EJBCommon.FALSE, EJBCommon.FALSE, null,
                                    AD_BRNCH, companyCode
                            );

                            arReceipt.setRctReferenceNumber(reference_number);
                            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                            //glFunctionalCurrency.addArReceipt(arReceipt);
                            arReceipt.setGlFunctionalCurrency(glFunctionalCurrency);

                            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(arModReceiptDetails.getRctCstCustomerCode(), companyCode);
                            //arCustomer.addArReceipt(arReceipt);
                            arReceipt.setArCustomer(arCustomer);

                            String bankAccount = switch (BR_BRNCH_CODE) {
                                case "Manila-Smoking Lounge" -> "Allied Bank-IPT Smoking";
                                case "Manila-Cigar Shop" -> "Allied Bank Cigar shop";
                                case "Manila-Term.#2 Domestic" -> "Terminal II Domestic";
                                case "Manila-Term.#2 Intl" -> "Term2 International";
                                case "Cebu-Banilad" -> "Metrobank Banilad";
                                case "Cebu-Gorordo" -> "Metrobank-Gorordo";
                                case "Cebu-Mactan Domestic" -> "Metrobank Mactan Domestic";
                                case "Cebu-Mactan Intl" -> "Metrobank I Mactan Int'l";
                                case "Cebu-Supercat" -> "Metrobank Supercat";
                                default -> arCustomer.getAdBankAccount().getBaName();
                            };

                            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(bankAccount, companyCode);
                            //adBankAccount.addArReceipt(arReceipt);
                            arReceipt.setAdBankAccount(adBankAccount);

                            if (adCompany.getCmpShortName().equalsIgnoreCase("vertext")) {
                                switch (BR_BRNCH_CODE) {
                                    case "HO" -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INC - Main", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arReceipt.setArTaxCode(arTaxCode);
                                    }
                                    case "OL-CENTRO" -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INC - Centro", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arReceipt.setArTaxCode(arTaxCode);
                                    }
                                    case "OL-ROBINSON" -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INC - Robinson", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arReceipt.setArTaxCode(arTaxCode);
                                    }
                                    case "OL-ROUTA" -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INC - Routa", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arReceipt.setArTaxCode(arTaxCode);
                                    }
                                    case "OL-CAPITOL" -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INC - Capitol", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arReceipt.setArTaxCode(arTaxCode);
                                    }
                                    default -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INCLUSIVE", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arReceipt.setArTaxCode(arTaxCode);

                                    }
                                }
                            } else {
                                LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INCLUSIVE", companyCode);
                                //arTaxCode.addArReceipt(arReceipt);
                                arReceipt.setArTaxCode(arTaxCode);

                            }

                            LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName("NONE", companyCode);
                            //arWithholdingTaxCode.addArReceipt(arReceipt);
                            arReceipt.setArWithholdingTaxCode(arWithholdingTaxCode);

                            LocalArReceiptBatch arReceiptBatch;
                            try {
                                arReceiptBatch = arReceiptBatchHome.findByRbName("POS BATCH", AD_BRNCH, companyCode);
                            }
                            catch (FinderException ex) {
                                arReceiptBatch = arReceiptBatchHome.create("POS BATCH", "POS BATCH",
                                        "OPEN", "MISC", EJBCommon.getGcCurrentDateWoTime().getTime(), cashier, AD_BRNCH, companyCode);
                            }
                            //arReceiptBatch.addArReceipt(arReceipt);
                            arReceipt.setArReceiptBatch(arReceiptBatch);

                            Iterator iter = arModReceiptDetails.getInvIliList().iterator();
                            short lineNumber = 0;
                            while (iter.hasNext()) {

                                ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = (ArModInvoiceLineItemDetails) iter.next();
                                LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.create(++lineNumber,
                                        arModInvoiceLineItemDetails.getIliQuantity(), arModInvoiceLineItemDetails.getIliUnitPrice(),
                                        EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() / (1 + arReceipt.getArTaxCode().getTcRate() / 100), (short) 2),
                                        EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() - (arModInvoiceLineItemDetails.getIliAmount() / (1 + arReceipt.getArTaxCode().getTcRate() / 100)), (short) 2),
                                        EJBCommon.TRUE, 0, 0, 0, 0, 0, EJBCommon.TRUE, companyCode);

                                //arReceipt.addArInvoiceLineItem(arInvoiceLineItem);
                                arInvoiceLineItem.setArReceipt(arReceipt);

                                LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                //invUnitOfMeasure.addArInvoiceLineItem(arInvoiceLineItem);
                                arInvoiceLineItem.setInvUnitOfMeasure(invUnitOfMeasure);
                                arInvoiceLineItem.setIliMisc(arModInvoiceLineItemDetails.getIliMisc());
                                LocalInvItem invItem = invItemHome.findByIiName(arModInvoiceLineItemDetails.getIliIiName(), companyCode);
                                LocalInvLocation invLocation = null;
                                try {
                                    if (invItem.getIiDefaultLocation() != null) {
                                        invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                                    }
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
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

                            ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) receiptNumbers.get(arExistingReceipt.getRctNumber());

                            arModUploadReceiptDetails.setRctPosDiscount(arModUploadReceiptDetails.getRctPosDiscount() + EJBCommon.roundIt(arModReceiptDetails.getRctPosDiscount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosScAmount(arModUploadReceiptDetails.getRctPosScAmount() + EJBCommon.roundIt(arModReceiptDetails.getRctPosScAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosDcAmount(arModUploadReceiptDetails.getRctPosDcAmount() + EJBCommon.roundIt(arModReceiptDetails.getRctPosDcAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            receiptNumbers.put(arExistingReceipt.getRctNumber(), arModUploadReceiptDetails);

                            double totalAmount = EJBCommon.roundIt((arModReceiptDetails.getRctPosTotalAmount() - arModReceiptDetails.getRctPosVoidAmount()) * arModReceiptDetails.getRctConversionRate(), (short) 2);
                            arExistingReceipt.setRctAmount(arExistingReceipt.getRctAmount() + totalAmount);

                            for (Object o : arModReceiptDetails.getInvIliList()) {
                                ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = (ArModInvoiceLineItemDetails) o;
                                LocalInvItem invItem = invItemHome.findByIiName(arModInvoiceLineItemDetails.getIliIiName(), companyCode);
                                LocalInvLocation invLocation = null;

                                try {
                                    if (invItem.getIiDefaultLocation() != null) {
                                        invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                                    }
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
                                }

                                LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(arModInvoiceLineItemDetails.getIliIiName(),
                                        invLocation == null ? arModInvoiceLineItemDetails.getIliLocName() : invLocation.getLocName(), companyCode);

                                LocalArInvoiceLineItem arExistingInvoiceLineItem = null;

                                try {
                                    arExistingInvoiceLineItem = arInvoiceLineItemHome.findByRctCodeAndIlCodeAndUomName(arExistingReceipt.getRctCode(),
                                            invItemLocation.getIlCode(), arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
                                }

                                if (arExistingInvoiceLineItem == null) {
                                    LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.create((short) (arExistingReceipt.getArInvoiceLineItems().size() + 1),
                                            arModInvoiceLineItemDetails.getIliQuantity(), arModInvoiceLineItemDetails.getIliUnitPrice(),
                                            EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingReceipt.getArTaxCode().getTcRate() / 100), (short) 2), EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() - (arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingReceipt.getArTaxCode().getTcRate() / 100)), (short) 2),
                                            EJBCommon.TRUE, 0, 0, 0, 0, 0, EJBCommon.TRUE, companyCode);

                                    //arExistingReceipt.addArInvoiceLineItem(arInvoiceLineItem);
                                    arInvoiceLineItem.setArReceipt(arExistingReceipt);

                                    LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                    //invUnitOfMeasure.addArInvoiceLineItem(arInvoiceLineItem);
                                    arInvoiceLineItem.setInvUnitOfMeasure(invUnitOfMeasure);

                                    //invItemLocation.addArInvoiceLineItem(arInvoiceLineItem);
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
                        String generatedInvoice;

                        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
                        LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;

                        try {
                            adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AR INVOICE", companyCode);
                        }
                        catch (FinderException ex) {
                            Debug.print("Finder Exception : " + ex.getMessage());
                        }

                        try {
                            assert adDocumentSequenceAssignment != null;
                            adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome
                                    .findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, companyCode);
                        }
                        catch (FinderException ex) {
                            Debug.print("Finder Exception : " + ex.getMessage());
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
                                arExistingInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndCstCustomerCode(
                                        arModUploadReceiptDetails.getRctNumber(), (byte) 0,
                                        arModReceiptDetails.getRctCstCustomerCode(), companyCode);
                                break;
                            }
                            catch (FinderException ex) {
                                Debug.print("Finder Exception : " + ex.getMessage());
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
                                    (byte) 0, "POS Sales " + new Date(), arModReceiptDetails.getRctDate(), generatedInvoice, null, null,
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

                            arInvoice.setInvReferenceNumber(reference_number);
                            LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName("IMMEDIATE", companyCode);
                            //adPaymentTerm.addArInvoice(arInvoice);
                            arInvoice.setAdPaymentTerm(adPaymentTerm);

                            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                            //glFunctionalCurrency.addArInvoice(arInvoice);
                            arInvoice.setGlFunctionalCurrency(glFunctionalCurrency);

                            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(arModReceiptDetails.getRctCstCustomerCode(), companyCode);
                            //arCustomer.addArInvoice(arInvoice);
                            arInvoice.setArCustomer(arCustomer);

                            if (adCompany.getCmpShortName().equalsIgnoreCase("vertext")) {
                                switch (BR_BRNCH_CODE) {
                                    case "HO" -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INC - Main", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arInvoice.setArTaxCode(arTaxCode);
                                    }
                                    case "OL-CENTRO" -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INC - Centro", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arInvoice.setArTaxCode(arTaxCode);
                                    }
                                    case "OL-ROBINSON" -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INC - Robinson", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arInvoice.setArTaxCode(arTaxCode);
                                    }
                                    case "OL-ROUTA" -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INC - Routa", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arInvoice.setArTaxCode(arTaxCode);
                                    }
                                    case "OL-CAPITOL" -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INC - Capitol", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arInvoice.setArTaxCode(arTaxCode);
                                    }
                                    default -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INCLUSIVE", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arInvoice.setArTaxCode(arTaxCode);

                                    }
                                }
                            } else {
                                LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INCLUSIVE", companyCode);
                                //arTaxCode.addArReceipt(arReceipt);
                                arInvoice.setArTaxCode(arTaxCode);

                            }

                            LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName("NONE", companyCode);
                            //arWithholdingTaxCode.addArInvoice(arInvoice);
                            arInvoice.setArWithholdingTaxCode(arWithholdingTaxCode);

                            LocalArInvoiceBatch arInvoiceBatch;
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
                            while (iter.hasNext()) {

                                ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = (ArModInvoiceLineItemDetails) iter.next();
                                LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.create(++lineNumber,
                                        arModInvoiceLineItemDetails.getIliQuantity(), arModInvoiceLineItemDetails.getIliUnitPrice(),
                                        EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() / (1 + arInvoice.getArTaxCode().getTcRate() / 100), (short) 2), EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() - (arModInvoiceLineItemDetails.getIliAmount() / (1 + arInvoice.getArTaxCode().getTcRate() / 100)), (short) 2),
                                        EJBCommon.TRUE, 0, 0, 0, 0, 0, EJBCommon.TRUE, companyCode);

                                //arInvoice.addArInvoiceLineItem(arInvoiceLineItem);
                                arInvoiceLineItem.setArInvoice(arInvoice);

                                LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                //invUnitOfMeasure.addArInvoiceLineItem(arInvoiceLineItem);
                                arInvoiceLineItem.setInvUnitOfMeasure(invUnitOfMeasure);

                                arInvoiceLineItem.setIliMisc(arModInvoiceLineItemDetails.getIliMisc());

                                LocalInvItem invItem = invItemHome.findByIiName(arModInvoiceLineItemDetails.getIliIiName(), companyCode);

                                LocalInvLocation invLocation = null;
                                try {
                                    if (invItem.getIiDefaultLocation() != null) {
                                        invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                                    }
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
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

                            for (Object o : arModReceiptDetails.getInvIliList()) {
                                ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = (ArModInvoiceLineItemDetails) o;

                                LocalInvItem invItem = invItemHome.findByIiName(arModInvoiceLineItemDetails.getIliIiName(), companyCode);

                                LocalInvLocation invLocation = null;
                                try {
                                    if (invItem.getIiDefaultLocation() != null) {
                                        invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                                    }
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
                                }

                                LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(arModInvoiceLineItemDetails.getIliIiName(),
                                        invLocation == null ? arModInvoiceLineItemDetails.getIliLocName() : invLocation.getLocName(), companyCode);

                                LocalArInvoiceLineItem arExistingInvoiceLineItem = null;
                                try {
                                    arExistingInvoiceLineItem = arInvoiceLineItemHome.findByRctCodeAndIlCodeAndUomName(arExistingInvoice.getInvCode(),
                                            invItemLocation.getIlCode(), arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
                                }

                                if (arExistingInvoiceLineItem == null) {

                                    LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.create((short) (arExistingInvoice.getArInvoiceLineItems().size() + 1),
                                            arModInvoiceLineItemDetails.getIliQuantity(), arModInvoiceLineItemDetails.getIliUnitPrice(),
                                            EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingInvoice.getArTaxCode().getTcRate() / 100), (short) 2), EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() - (arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingInvoice.getArTaxCode().getTcRate() / 100)), (short) 2),
                                            EJBCommon.TRUE, 0, 0, 0, 0, 0, EJBCommon.TRUE, companyCode);

                                    //arExistingInvoice.addArInvoiceLineItem(arInvoiceLineItem);
                                    arInvoiceLineItem.setArInvoice(arExistingInvoice);

                                    LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                    //invUnitOfMeasure.addArInvoiceLineItem(arInvoiceLineItem);
                                    arInvoiceLineItem.setInvUnitOfMeasure(invUnitOfMeasure);
                                    arInvoiceLineItem.setIliMisc(arModInvoiceLineItemDetails.getIliMisc());
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
                for (Object o : receiptNumbers.values()) {

                    ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) o;

                    LocalArReceipt arReceipt = arReceiptHome.findByRctNumberAndBrCode(arModUploadReceiptDetails.getRctNumber(), AD_BRNCH, companyCode);
                    arReceipt.setRctReferenceNumber(arModUploadReceiptDetails.getRctReferenceNumber());

                    Iterator lineIter = arReceipt.getArInvoiceLineItems().iterator();

                    double TOTAL_TAX = 0;
                    double TOTAL_LINE = 0;

                    while (lineIter.hasNext()) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) lineIter.next();

                        TOTAL_LINE += arInvoiceLineItem.getIliTaxAmount() + arInvoiceLineItem.getIliAmount();

                        LocalInvItemLocation invItemLocation = arInvoiceLineItem.getInvItemLocation();

                        // add cost of sales distribution and inventory
                        double COST;
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
                            Debug.print("Finder Exception : " + ex.getMessage());
                        }

                        if (arInvoiceLineItem.getIliEnableAutoBuild() == 0
                                || arInvoiceLineItem.getIliEnableAutoBuild() == 1
                                || arInvoiceLineItem.getInvItemLocation().getInvItem().getIiClass().equals("Stock")) {

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
                    }

                    // add tax distribution if necessary
                    if (!arReceipt.getArTaxCode().getTcType().equals("NONE") &&
                            !arReceipt.getArTaxCode().getTcType().equals("EXEMPT")) {

                        this.addArDrEntry(arReceipt.getArDrNextLine(),
                                "TAX", EJBCommon.FALSE, TOTAL_TAX, arReceipt.getArTaxCode().getGlChartOfAccount().getCoaCode(),
                                EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

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
                    double forexGainLoss = TOTAL_LINE - (arReceipt.getRctAmount() +
                            arModUploadReceiptDetails.getRctPosDiscount() -
                            arModUploadReceiptDetails.getRctPosScAmount() -
                            arModUploadReceiptDetails.getRctPosDcAmount());
                    if (forexGainLoss != 0) {
                        this.addArDrEntry(arReceipt.getArDrNextLine(), "FOREX",
                                forexGainLoss > 0 ? EJBCommon.TRUE : EJBCommon.FALSE, Math.abs(forexGainLoss),
                                adPreference.getPrfMiscPosGiftCertificateAccount(),
                                EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                    }
                }

                for (Object o : invoiceNumbers.values()) {

                    ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) o;

                    LocalArInvoice arInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arModUploadReceiptDetails.getRctNumber(), (byte) 0, AD_BRNCH, companyCode);
                    //	arInvoice.setInvReferenceNumber(firstNumber + "-" + lastNumber);

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
                        switch (arInvoice.getAdPaymentTerm().getPytScheduleBasis()) {
                            case "DEFAULT" -> {
                                gcDateDue.setTime(arInvoice.getInvEffectivityDate());
                                gcDateDue.add(Calendar.DATE, adPaymentSchedule.getPsDueDay());
                            }
                            case "MONTHLY" -> {
                                gcDateDue = gcPrevDateDue;
                                gcDateDue.add(Calendar.MONTH, 1);
                            }
                            case "BI-MONTHLY" -> {
                                gcDateDue = gcPrevDateDue;
                                if (gcPrevDateDue.get(Calendar.MONTH) != 1) {
                                    if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 31
                                            && gcPrevDateDue.get(Calendar.DATE) > 15 && gcPrevDateDue.get(Calendar.DATE) < 31) {
                                        gcDateDue.add(Calendar.DATE, 16);
                                    } else {
                                        gcDateDue.add(Calendar.DATE, 15);
                                    }
                                } else if (gcPrevDateDue.get(Calendar.MONTH) == 1) {
                                    if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 28 && gcPrevDateDue.get(Calendar.DATE) == 14) {
                                        gcDateDue.add(Calendar.DATE, 14);
                                    } else if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 28
                                            && gcPrevDateDue.get(Calendar.DATE) >= 15 && gcPrevDateDue.get(Calendar.DATE) < 28) {
                                        gcDateDue.add(Calendar.DATE, 13);
                                    } else if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 29
                                            && gcPrevDateDue.get(Calendar.DATE) >= 15 && gcPrevDateDue.get(Calendar.DATE) < 29) {
                                        gcDateDue.add(Calendar.DATE, 14);
                                    } else {
                                        gcDateDue.add(Calendar.DATE, 15);
                                    }
                                }
                            }
                        }

                        // create a payment schedule
                        double PAYMENT_SCHEDULE_AMOUNT;

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
                        double COST;

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
                            Debug.print("Finder Exception : " + ex.getMessage());
                        }

                        if (arInvoiceLineItem.getIliEnableAutoBuild() == 0
                                || arInvoiceLineItem.getIliEnableAutoBuild() == 1
                                || arInvoiceLineItem.getInvItemLocation().getInvItem().getIiClass().equals("Stock")) {

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
                    }

                    // add tax distribution if necessary
                    if (!arInvoice.getArTaxCode().getTcType().equals("NONE") &&
                            !arInvoice.getArTaxCode().getTcType().equals("EXEMPT")) {

                        this.addArDrEntry(arInvoice.getArDrNextLine(),
                                "TAX", EJBCommon.FALSE, TOTAL_TAX, arInvoice.getArTaxCode().getGlChartOfAccount().getCoaCode(),
                                EJBCommon.FALSE, arInvoice, AD_BRNCH, companyCode);

                    }

                    // add cash distribution
                    LocalAdBranchCustomer adBranchCustomer = null;
                    try {

                        adBranchCustomer = adBranchCustomerHome.findBcstByCstCodeAndBrCode(arInvoice.getArCustomer().getCstCode(), AD_BRNCH, companyCode);

                    }
                    catch (FinderException ex) {
                        Debug.print("Finder Exception : " + ex.getMessage());
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
                        // add discount.
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
                    double forexGainLoss = TOTAL_LINE - (arInvoice.getInvAmountDue() +
                            arModUploadReceiptDetails.getRctPosDiscount() -
                            arModUploadReceiptDetails.getRctPosScAmount() -
                            arModUploadReceiptDetails.getRctPosDcAmount());
                    if (forexGainLoss != 0) {
                        this.addArDrEntry(arInvoice.getArDrNextLine(), "FOREX",
                                forexGainLoss > 0 ? EJBCommon.TRUE : EJBCommon.FALSE, Math.abs(forexGainLoss),
                                adPreference.getPrfMiscPosGiftCertificateAccount(),
                                EJBCommon.FALSE, arInvoice, AD_BRNCH, companyCode);
                    }
                }
                success = 1;
            }
            return success;
        }
        catch (Exception ex) {

            ex.printStackTrace();
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }
    }

    private int setArMiscReceiptAllNewAndVoidWithExpiryDateAndEntries(
            String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier, String companyShortName) {

        Debug.print("ArMiscReceiptSyncControllerBean setArMiscReceiptAllNewAndVoidWithExpiryDateAndEntries");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(branchCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            Integer AD_BRNCH = adBranch.getBrCode();
            String BR_BRNCH_CODE = adBranch.getBrBranchCode();

            int success = 0;

            if (adPreference.getPrfMiscPosDiscountAccount() != null && adPreference.getPrfMiscPosGiftCertificateAccount() != null &&
                    adPreference.getPrfMiscPosServiceChargeAccount() != null && adPreference.getPrfMiscPosDineInChargeAccount() != null) {

                // new receipts
                HashMap<String, Object> receiptNumbers = new HashMap<>();
                HashMap<String, Object> invoiceNumbers = new HashMap();

                for (int i = 0; i < receipts.length; i++) {

                    ArModReceiptDetails arModReceiptDetails = receiptDecodeWithExpiryDateAndEntries(receipts[i]);
                    if (arModReceiptDetails.getRctPosOnAccount() == 0) {

                        // generate receipt number

                        String generatedReceipt = null;

                        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
                        LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;

                        try {
                            adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AR RECEIPT", companyCode);
                        }
                        catch (FinderException ex) {
                            Debug.print("Finder Exception : " + ex.getMessage());
                        }

                        try {
                            assert adDocumentSequenceAssignment != null;
                            adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome
                                    .findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, companyCode);
                        }
                        catch (FinderException ex) {
                            Debug.print("Finder Exception : " + ex.getMessage());
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
                                Debug.print("Finder Exception : " + ex.getMessage());
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
                                    0, null, null, EJBCommon.FALSE, null, EJBCommon.FALSE,
                                    EJBCommon.FALSE,
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

                            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(arModReceiptDetails.getRctCstCustomerCode(), companyCode);
                            //arCustomer.addArReceipt(arReceipt);
                            arReceipt.setArCustomer(arCustomer);

                            String bankAccount = switch (BR_BRNCH_CODE) {
                                case "Manila-Smoking Lounge" -> "Allied Bank-IPT Smoking";
                                case "Manila-Cigar Shop" -> "Allied Bank Cigar shop";
                                case "Manila-Term.#2 Domestic" -> "Terminal II Domestic";
                                case "Manila-Term.#2 Intl" -> "Term2 International";
                                case "Cebu-Banilad" -> "Metrobank Banilad";
                                case "Cebu-Gorordo" -> "Metrobank-Gorordo";
                                case "Cebu-Mactan Domestic" -> "Metrobank Mactan Domestic";
                                case "Cebu-Mactan Intl" -> "Metrobank I Mactan Int'l";
                                case "Cebu-Supercat" -> "Metrobank Supercat";
                                default -> arCustomer.getAdBankAccount().getBaName();
                            };

                            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(bankAccount, companyCode);
                            //adBankAccount.addArReceipt(arReceipt);
                            arReceipt.setAdBankAccount(adBankAccount);

                            if (adCompany.getCmpShortName().equalsIgnoreCase("vertext")) {
                                switch (BR_BRNCH_CODE) {
                                    case "HO" -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INC - Main", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arReceipt.setArTaxCode(arTaxCode);
                                    }
                                    case "OL-CENTRO" -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INC - Centro", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arReceipt.setArTaxCode(arTaxCode);
                                    }
                                    case "OL-ROBINSON" -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INC - Robinson", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arReceipt.setArTaxCode(arTaxCode);
                                    }
                                    case "OL-ROUTA" -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INC - Routa", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arReceipt.setArTaxCode(arTaxCode);
                                    }
                                    case "OL-CAPITOL" -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INC - Capitol", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arReceipt.setArTaxCode(arTaxCode);
                                    }
                                    default -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INCLUSIVE", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arReceipt.setArTaxCode(arTaxCode);

                                    }
                                }

                            } else {
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
                                LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.create(++lineNumber,
                                        arModInvoiceLineItemDetails.getIliQuantity(), arModInvoiceLineItemDetails.getIliUnitPrice(),
                                        EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() / (1 + arReceipt.getArTaxCode().getTcRate() / 100), (short) 2), EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() - (arModInvoiceLineItemDetails.getIliAmount() / (1 + arReceipt.getArTaxCode().getTcRate() / 100)), (short) 2),
                                        EJBCommon.FALSE, 0, 0, 0, 0, 0, EJBCommon.TRUE, companyCode);

                                //arReceipt.addArInvoiceLineItem(arInvoiceLineItem);
                                arInvoiceLineItem.setArReceipt(arReceipt);

                                LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                //invUnitOfMeasure.addArInvoiceLineItem(arInvoiceLineItem);
                                arInvoiceLineItem.setInvUnitOfMeasure(invUnitOfMeasure);
                                arInvoiceLineItem.setIliMisc(arModInvoiceLineItemDetails.getIliMisc());
                                LocalInvItem invItem = invItemHome.findByIiName(arModInvoiceLineItemDetails.getIliIiName(), companyCode);

                                LocalInvLocation invLocation = null;
                                try {
                                    if (invItem.getIiDefaultLocation() != null) {
                                        invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                                    }
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
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

                            ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) receiptNumbers.get(arExistingReceipt.getRctNumber());

                            arModUploadReceiptDetails.setRctPosDiscount(arModUploadReceiptDetails.getRctPosDiscount() + EJBCommon.roundIt(arModReceiptDetails.getRctPosDiscount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosScAmount(arModUploadReceiptDetails.getRctPosScAmount() + EJBCommon.roundIt(arModReceiptDetails.getRctPosScAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosDcAmount(arModUploadReceiptDetails.getRctPosDcAmount() + EJBCommon.roundIt(arModReceiptDetails.getRctPosDcAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            receiptNumbers.put(arExistingReceipt.getRctNumber(), arModUploadReceiptDetails);

                            double totalAmount = EJBCommon.roundIt((arModReceiptDetails.getRctPosTotalAmount() - arModReceiptDetails.getRctPosVoidAmount()) * arModReceiptDetails.getRctConversionRate(), (short) 2);
                            arExistingReceipt.setRctAmount(arExistingReceipt.getRctAmount() + totalAmount);

                            for (Object o : arModReceiptDetails.getInvIliList()) {

                                ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = (ArModInvoiceLineItemDetails) o;
                                LocalInvItem invItem = invItemHome.findByIiName(arModInvoiceLineItemDetails.getIliIiName(), companyCode);

                                LocalInvLocation invLocation = null;
                                try {
                                    if (invItem.getIiDefaultLocation() != null) {
                                        invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                                    }
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
                                }

                                LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(arModInvoiceLineItemDetails.getIliIiName(),
                                        invLocation == null ? arModInvoiceLineItemDetails.getIliLocName() : invLocation.getLocName(), companyCode);

                                LocalArInvoiceLineItem arExistingInvoiceLineItem = null;

                                try {
                                    arExistingInvoiceLineItem = arInvoiceLineItemHome.findByRctCodeAndIlCodeAndUomName(arExistingReceipt.getRctCode(),
                                            invItemLocation.getIlCode(), arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
                                }

                                if (arExistingInvoiceLineItem == null) {
                                    LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.create((short) (arExistingReceipt.getArInvoiceLineItems().size() + 1),
                                            arModInvoiceLineItemDetails.getIliQuantity(), arModInvoiceLineItemDetails.getIliUnitPrice(),
                                            EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingReceipt.getArTaxCode().getTcRate() / 100), (short) 2), EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() - (arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingReceipt.getArTaxCode().getTcRate() / 100)), (short) 2),
                                            EJBCommon.FALSE, 0, 0, 0, 0, 0, EJBCommon.TRUE, companyCode);

                                    //arExistingReceipt.addArInvoiceLineItem(arInvoiceLineItem);
                                    arInvoiceLineItem.setArReceipt(arExistingReceipt);

                                    LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                    //invUnitOfMeasure.addArInvoiceLineItem(arInvoiceLineItem);
                                    arInvoiceLineItem.setInvUnitOfMeasure(invUnitOfMeasure);

                                    //invItemLocation.addArInvoiceLineItem(arInvoiceLineItem);
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
                        String generatedInvoice;

                        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
                        LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;

                        try {

                            adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AR INVOICE", companyCode);

                        }
                        catch (FinderException ex) {
                            Debug.print("Finder Exception : " + ex.getMessage());
                        }

                        try {
                            assert adDocumentSequenceAssignment != null;
                            adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome
                                    .findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, companyCode);
                        }
                        catch (FinderException ex) {
                            Debug.print("Finder Exception : " + ex.getMessage());
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
                                Debug.print("Finder Exception : " + ex.getMessage());
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
                                    (byte) 0, "POS Sales " + new Date(), arModReceiptDetails.getRctDate(), generatedInvoice, null, null,
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

                            LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName("IMMEDIATE", companyCode);
                            //adPaymentTerm.addArInvoice(arInvoice);
                            arInvoice.setAdPaymentTerm(adPaymentTerm);

                            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                            //glFunctionalCurrency.addArInvoice(arInvoice);
                            arInvoice.setGlFunctionalCurrency(glFunctionalCurrency);

                            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(arModReceiptDetails.getRctCstCustomerCode(), companyCode);
                            //arCustomer.addArInvoice(arInvoice);
                            arInvoice.setArCustomer(arCustomer);

                            if (adCompany.getCmpShortName().equalsIgnoreCase("vertext")) {
                                switch (BR_BRNCH_CODE) {
                                    case "HO" -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INC - Main", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arInvoice.setArTaxCode(arTaxCode);
                                    }
                                    case "OL-CENTRO" -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INC - Centro", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arInvoice.setArTaxCode(arTaxCode);
                                    }
                                    case "OL-ROBINSON" -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INC - Robinson", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arInvoice.setArTaxCode(arTaxCode);
                                    }
                                    case "OL-ROUTA" -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INC - Routa", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arInvoice.setArTaxCode(arTaxCode);
                                    }
                                    case "OL-CAPITOL" -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INC - Capitol", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arInvoice.setArTaxCode(arTaxCode);
                                    }
                                    default -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INCLUSIVE", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arInvoice.setArTaxCode(arTaxCode);

                                    }
                                }
                            } else {
                                LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INCLUSIVE", companyCode);
                                //arTaxCode.addArReceipt(arReceipt);
                                arInvoice.setArTaxCode(arTaxCode);

                            }

                            LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName("NONE", companyCode);
                            //arWithholdingTaxCode.addArInvoice(arInvoice);
                            arInvoice.setArWithholdingTaxCode(arWithholdingTaxCode);

                            LocalArInvoiceBatch arInvoiceBatch;
                            try {
                                arInvoiceBatch = arInvoiceBatchHome.findByIbName("POS BATCH", AD_BRNCH, companyCode);
                            }
                            catch (FinderException ex) {
                                arInvoiceBatch = arInvoiceBatchHome.create("POS BATCH", "POS BATCH",
                                        "OPEN", "MISC", EJBCommon.getGcCurrentDateWoTime().getTime(), cashier, AD_BRNCH, companyCode);
                            }

                            //arInvoiceBatch.addArInvoice(arInvoice);
                            arInvoice.setArInvoiceBatch(arInvoiceBatch);

                            Iterator iter = arModReceiptDetails.getInvIliList().iterator();
                            short lineNumber = 0;
                            while (iter.hasNext()) {

                                ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = (ArModInvoiceLineItemDetails) iter.next();
                                LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.create(++lineNumber,
                                        arModInvoiceLineItemDetails.getIliQuantity(), arModInvoiceLineItemDetails.getIliUnitPrice(),
                                        EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() / (1 + arInvoice.getArTaxCode().getTcRate() / 100), (short) 2), EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() - (arModInvoiceLineItemDetails.getIliAmount() / (1 + arInvoice.getArTaxCode().getTcRate() / 100)), (short) 2),
                                        EJBCommon.FALSE, 0, 0, 0, 0, 0, EJBCommon.TRUE, companyCode);

                                //arInvoice.addArInvoiceLineItem(arInvoiceLineItem);
                                arInvoiceLineItem.setArInvoice(arInvoice);

                                LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                //invUnitOfMeasure.addArInvoiceLineItem(arInvoiceLineItem);
                                arInvoiceLineItem.setInvUnitOfMeasure(invUnitOfMeasure);

                                arInvoiceLineItem.setIliMisc(arModInvoiceLineItemDetails.getIliMisc());

                                LocalInvItem invItem = invItemHome.findByIiName(arModInvoiceLineItemDetails.getIliIiName(), companyCode);

                                LocalInvLocation invLocation = null;
                                try {
                                    if (invItem.getIiDefaultLocation() != null) {
                                        invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                                    }
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
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

                            for (Object o : arModReceiptDetails.getInvIliList()) {

                                ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = (ArModInvoiceLineItemDetails) o;
                                LocalInvItem invItem = invItemHome.findByIiName(arModInvoiceLineItemDetails.getIliIiName(), companyCode);

                                LocalInvLocation invLocation = null;
                                try {
                                    if (invItem.getIiDefaultLocation() != null) {
                                        invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                                    }
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
                                }

                                LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(arModInvoiceLineItemDetails.getIliIiName(),
                                        invLocation == null ? arModInvoiceLineItemDetails.getIliLocName() : invLocation.getLocName(), companyCode);

                                LocalArInvoiceLineItem arExistingInvoiceLineItem = null;
                                try {
                                    arExistingInvoiceLineItem = arInvoiceLineItemHome.findByRctCodeAndIlCodeAndUomName(arExistingInvoice.getInvCode(),
                                            invItemLocation.getIlCode(), arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
                                }

                                if (arExistingInvoiceLineItem == null) {

                                    LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.create((short) (arExistingInvoice.getArInvoiceLineItems().size() + 1),
                                            arModInvoiceLineItemDetails.getIliQuantity(), arModInvoiceLineItemDetails.getIliUnitPrice(),
                                            EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingInvoice.getArTaxCode().getTcRate() / 100), (short) 2), EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() - (arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingInvoice.getArTaxCode().getTcRate() / 100)), (short) 2),
                                            EJBCommon.FALSE, 0, 0, 0, 0, 0, EJBCommon.TRUE, companyCode);

                                    //arExistingInvoice.addArInvoiceLineItem(arInvoiceLineItem);
                                    arInvoiceLineItem.setArInvoice(arExistingInvoice);

                                    LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                    //invUnitOfMeasure.addArInvoiceLineItem(arInvoiceLineItem);
                                    arInvoiceLineItem.setInvUnitOfMeasure(invUnitOfMeasure);
                                    arInvoiceLineItem.setIliMisc(arModInvoiceLineItemDetails.getIliMisc());
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
                for (Object o : receiptNumbers.values()) {

                    ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) o;

                    LocalArReceipt arReceipt = arReceiptHome.findByRctNumberAndBrCode(arModUploadReceiptDetails.getRctNumber(), AD_BRNCH, companyCode);
                    arReceipt.setRctReferenceNumber(arModUploadReceiptDetails.getRctNumber());

                    Iterator lineIter = arReceipt.getArInvoiceLineItems().iterator();

                    double TOTAL_TAX = 0;
                    double TOTAL_LINE = 0;

                    while (lineIter.hasNext()) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) lineIter.next();

                        TOTAL_LINE += arInvoiceLineItem.getIliTaxAmount() + arInvoiceLineItem.getIliAmount();

                        LocalInvItemLocation invItemLocation = arInvoiceLineItem.getInvItemLocation();

                        // add cost of sales distribution and inventory
                        double COST;

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
                            adBranchItemLocation = adBranchItemLocationHome
                                    .findBilByIlCodeAndBrCode(arInvoiceLineItem.getInvItemLocation().getIlCode(), AD_BRNCH, companyCode);
                        }
                        catch (FinderException ex) {
                            Debug.print("Finder Exception : " + ex.getMessage());
                        }

                        if (arInvoiceLineItem.getIliEnableAutoBuild() == 0
                                || arInvoiceLineItem.getIliEnableAutoBuild() == 1
                                || arInvoiceLineItem.getInvItemLocation().getInvItem().getIiClass().equals("Stock")) {

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
                    }

                    // add tax distribution if necessary
                    if (!arReceipt.getArTaxCode().getTcType().equals("NONE") &&
                            !arReceipt.getArTaxCode().getTcType().equals("EXEMPT")) {

                        this.addArDrEntry(arReceipt.getArDrNextLine(),
                                "TAX", EJBCommon.FALSE, TOTAL_TAX, arReceipt.getArTaxCode().getGlChartOfAccount().getCoaCode(),
                                EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

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
                    double forexGainLoss = TOTAL_LINE - (arReceipt.getRctAmount() +
                            arModUploadReceiptDetails.getRctPosDiscount() -
                            arModUploadReceiptDetails.getRctPosScAmount() -
                            arModUploadReceiptDetails.getRctPosDcAmount());
                    if (forexGainLoss != 0) {
                        this.addArDrEntry(arReceipt.getArDrNextLine(), "FOREX",
                                forexGainLoss > 0 ? EJBCommon.TRUE : EJBCommon.FALSE, Math.abs(forexGainLoss),
                                adPreference.getPrfMiscPosGiftCertificateAccount(),
                                EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                    }

                }
                for (Object o : invoiceNumbers.values()) {

                    ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) o;

                    LocalArInvoice arInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arModUploadReceiptDetails.getRctNumber(), (byte) 0, AD_BRNCH, companyCode);
                    arInvoice.setInvReferenceNumber(arModUploadReceiptDetails.getRctNumber());

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
                        switch (arInvoice.getAdPaymentTerm().getPytScheduleBasis()) {
                            case "DEFAULT" -> {
                                gcDateDue.setTime(arInvoice.getInvEffectivityDate());
                                gcDateDue.add(Calendar.DATE, adPaymentSchedule.getPsDueDay());
                            }
                            case "MONTHLY" -> {
                                gcDateDue = gcPrevDateDue;
                                gcDateDue.add(Calendar.MONTH, 1);
                            }
                            case "BI-MONTHLY" -> {
                                gcDateDue = gcPrevDateDue;
                                if (gcPrevDateDue.get(Calendar.MONTH) != 1) {
                                    if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 31
                                            && gcPrevDateDue.get(Calendar.DATE) > 15
                                            && gcPrevDateDue.get(Calendar.DATE) < 31) {
                                        gcDateDue.add(Calendar.DATE, 16);
                                    } else {
                                        gcDateDue.add(Calendar.DATE, 15);
                                    }
                                } else if (gcPrevDateDue.get(Calendar.MONTH) == 1) {
                                    if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 28
                                            && gcPrevDateDue.get(Calendar.DATE) == 14) {
                                        gcDateDue.add(Calendar.DATE, 14);
                                    } else if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 28
                                            && gcPrevDateDue.get(Calendar.DATE) >= 15
                                            && gcPrevDateDue.get(Calendar.DATE) < 28) {
                                        gcDateDue.add(Calendar.DATE, 13);
                                    } else if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 29
                                            && gcPrevDateDue.get(Calendar.DATE) >= 15
                                            && gcPrevDateDue.get(Calendar.DATE) < 29) {
                                        gcDateDue.add(Calendar.DATE, 14);
                                    } else {
                                        gcDateDue.add(Calendar.DATE, 15);
                                    }
                                }
                            }
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
                        double COST;
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
                            adBranchItemLocation = adBranchItemLocationHome
                                    .findBilByIlCodeAndBrCode(arInvoiceLineItem.getInvItemLocation().getIlCode(), AD_BRNCH, companyCode);
                        }
                        catch (FinderException ex) {
                            Debug.print("Finder Exception : " + ex.getMessage());
                        }

                        if (arInvoiceLineItem.getIliEnableAutoBuild() == 0
                                || arInvoiceLineItem.getIliEnableAutoBuild() == 1
                                || arInvoiceLineItem.getInvItemLocation().getInvItem().getIiClass().equals("Stock")) {

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

                    }

                    // add tax distribution if necessary
                    if (!arInvoice.getArTaxCode().getTcType().equals("NONE") &&
                            !arInvoice.getArTaxCode().getTcType().equals("EXEMPT")) {

                        this.addArDrEntry(arInvoice.getArDrNextLine(),
                                "TAX", EJBCommon.FALSE, TOTAL_TAX, arInvoice.getArTaxCode().getGlChartOfAccount().getCoaCode(),
                                EJBCommon.FALSE, arInvoice, AD_BRNCH, companyCode);

                    }

                    // add cash distribution
                    LocalAdBranchCustomer adBranchCustomer = null;

                    try {

                        adBranchCustomer = adBranchCustomerHome.findBcstByCstCodeAndBrCode(arInvoice.getArCustomer().getCstCode(), AD_BRNCH, companyCode);

                    }
                    catch (FinderException ex) {
                        Debug.print("Finder Exception : " + ex.getMessage());
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
                    double forexGainLoss = TOTAL_LINE - (arInvoice.getInvAmountDue()
                            + arModUploadReceiptDetails.getRctPosDiscount()
                            - arModUploadReceiptDetails.getRctPosScAmount()
                            - arModUploadReceiptDetails.getRctPosDcAmount());
                    if (forexGainLoss != 0) {
                        this.addArDrEntry(arInvoice.getArDrNextLine(), "FOREX",
                                forexGainLoss > 0 ? EJBCommon.TRUE : EJBCommon.FALSE, Math.abs(forexGainLoss),
                                adPreference.getPrfMiscPosGiftCertificateAccount(),
                                EJBCommon.FALSE, arInvoice, AD_BRNCH, companyCode);
                    }
                }
                success = 1;
            }
            return success;
        }
        catch (Exception ex) {

            ex.printStackTrace();
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }
    }

    private int setArMiscReceiptAllNewAndVoidWithSalesperson(
            String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier, String companyShortName) {

        Debug.print("ArMiscReceiptSyncControllerBean setArMiscReceiptAllNewAndVoidWithSalesperson");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(branchCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            Integer AD_BRNCH = adBranch.getBrCode();
            String BR_BRNCH_CODE = adBranch.getBrBranchCode();

            int success = 0;

            if (adPreference.getPrfMiscPosDiscountAccount() != null && adPreference.getPrfMiscPosGiftCertificateAccount() != null &&
                    adPreference.getPrfMiscPosServiceChargeAccount() != null && adPreference.getPrfMiscPosDineInChargeAccount() != null) {

                // new receipts
                HashMap<String, Object> receiptNumbers = new HashMap<>();
                HashMap<String, Object> invoiceNumbers = new HashMap<>();
                String firstNumber = "";
                String lastNumber = "";

                for (int i = 0; i < receipts.length; i++) {

                    ArModReceiptDetails arModReceiptDetails = receiptDecodeWithSalesperson(receipts[i]);

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
                            Debug.print("Finder Exception : " + ex.getMessage());
                        }

                        try {
                            assert adDocumentSequenceAssignment != null;
                            adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome
                                    .findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, companyCode);
                        }
                        catch (FinderException ex) {
                            Debug.print("Finder Exception : " + ex.getMessage());
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
                                Debug.print("Finder Exception : " + ex.getMessage());
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
                                    0, null, null, EJBCommon.FALSE, null, EJBCommon.FALSE,
                                    EJBCommon.FALSE,
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

                            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(arModReceiptDetails.getRctCstCustomerCode(), companyCode);
                            //arCustomer.addArReceipt(arReceipt);
                            arReceipt.setArCustomer(arCustomer);

                            if (!arModReceiptDetails.getRctSlpSalespersonCode().equals("null") && !arModReceiptDetails.getRctSlpSalespersonCode().equals("")) {
                                try {
                                    //rowen 2
                                    LocalArSalesperson arSalesperson = arSalespersonHome.findByPrimaryKey(Integer.parseInt(arModReceiptDetails.getRctSlpSalespersonCode()));
                                    arReceipt.setArSalesperson(arSalesperson);
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
                                }
                            }

                            String bankAccount = switch (BR_BRNCH_CODE) {
                                case "Manila-Smoking Lounge" -> "Allied Bank-IPT Smoking";
                                case "Manila-Cigar Shop" -> "Allied Bank Cigar shop";
                                case "Manila-Term.#2 Domestic" -> "Terminal II Domestic";
                                case "Manila-Term.#2 Intl" -> "Term2 International";
                                case "Cebu-Banilad" -> "Metrobank Banilad";
                                case "Cebu-Gorordo" -> "Metrobank-Gorordo";
                                case "Cebu-Mactan Domestic" -> "Metrobank Mactan Domestic";
                                case "Cebu-Mactan Intl" -> "Metrobank I Mactan Int'l";
                                case "Cebu-Supercat" -> "Metrobank Supercat";
                                default -> arCustomer.getAdBankAccount().getBaName();
                            };

                            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(bankAccount, companyCode);
                            //adBankAccount.addArReceipt(arReceipt);
                            arReceipt.setAdBankAccount(adBankAccount);

                            if (adCompany.getCmpShortName().equalsIgnoreCase("vertext")) {
                                switch (BR_BRNCH_CODE) {
                                    case "HO" -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INC - Main", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arReceipt.setArTaxCode(arTaxCode);
                                    }
                                    case "OL-CENTRO" -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INC - Centro", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arReceipt.setArTaxCode(arTaxCode);
                                    }
                                    case "OL-ROBINSON" -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INC - Robinson", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arReceipt.setArTaxCode(arTaxCode);
                                    }
                                    case "OL-ROUTA" -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INC - Routa", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arReceipt.setArTaxCode(arTaxCode);
                                    }
                                    case "OL-CAPITOL" -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INC - Capitol", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arReceipt.setArTaxCode(arTaxCode);
                                    }
                                    default -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INCLUSIVE", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arReceipt.setArTaxCode(arTaxCode);

                                    }
                                }
                            } else {
                                LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INCLUSIVE", companyCode);
                                //arTaxCode.addArReceipt(arReceipt);
                                arReceipt.setArTaxCode(arTaxCode);

                            }

                            LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName("NONE", companyCode);
                            //arWithholdingTaxCode.addArReceipt(arReceipt);
                            arReceipt.setArWithholdingTaxCode(arWithholdingTaxCode);

                            LocalArReceiptBatch arReceiptBatch;
                            try {
                                arReceiptBatch = arReceiptBatchHome.findByRbName("POS BATCH", AD_BRNCH, companyCode);
                            }
                            catch (FinderException ex) {
                                arReceiptBatch = arReceiptBatchHome.create("POS BATCH",
                                        "POS BATCH", "OPEN", "MISC",
                                        EJBCommon.getGcCurrentDateWoTime().getTime(), cashier, AD_BRNCH, companyCode);
                            }
                            //arReceiptBatch.addArReceipt(arReceipt);
                            arReceipt.setArReceiptBatch(arReceiptBatch);

                            Iterator iter = arModReceiptDetails.getInvIliList().iterator();
                            short lineNumber = 0;
                            while (iter.hasNext()) {

                                ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = (ArModInvoiceLineItemDetails) iter.next();
                                LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.create(++lineNumber,
                                        arModInvoiceLineItemDetails.getIliQuantity(), arModInvoiceLineItemDetails.getIliUnitPrice(),
                                        EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() / (1 + arReceipt.getArTaxCode().getTcRate() / 100), (short) 2), EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() - (arModInvoiceLineItemDetails.getIliAmount() / (1 + arReceipt.getArTaxCode().getTcRate() / 100)), (short) 2),
                                        EJBCommon.FALSE, 0, 0, 0, 0, 0, EJBCommon.TRUE, companyCode);

                                //arReceipt.addArInvoiceLineItem(arInvoiceLineItem);
                                arInvoiceLineItem.setArReceipt(arReceipt);

                                LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                //invUnitOfMeasure.addArInvoiceLineItem(arInvoiceLineItem);
                                arInvoiceLineItem.setInvUnitOfMeasure(invUnitOfMeasure);
                                arInvoiceLineItem.setIliMisc(arModInvoiceLineItemDetails.getIliMisc());
                                LocalInvItem invItem = invItemHome.findByIiName(arModInvoiceLineItemDetails.getIliIiName(), companyCode);

                                LocalInvLocation invLocation = null;
                                try {
                                    if (invItem.getIiDefaultLocation() != null) {
                                        invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                                    }
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
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

                            ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) receiptNumbers.get(arExistingReceipt.getRctNumber());

                            arModUploadReceiptDetails.setRctPosDiscount(arModUploadReceiptDetails.getRctPosDiscount() + EJBCommon.roundIt(arModReceiptDetails.getRctPosDiscount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosScAmount(arModUploadReceiptDetails.getRctPosScAmount() + EJBCommon.roundIt(arModReceiptDetails.getRctPosScAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosDcAmount(arModUploadReceiptDetails.getRctPosDcAmount() + EJBCommon.roundIt(arModReceiptDetails.getRctPosDcAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            receiptNumbers.put(arExistingReceipt.getRctNumber(), arModUploadReceiptDetails);

                            double totalAmount = EJBCommon.roundIt((arModReceiptDetails.getRctPosTotalAmount() - arModReceiptDetails.getRctPosVoidAmount()) * arModReceiptDetails.getRctConversionRate(), (short) 2);
                            arExistingReceipt.setRctAmount(arExistingReceipt.getRctAmount() + totalAmount);

                            for (Object o : arModReceiptDetails.getInvIliList()) {

                                ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = (ArModInvoiceLineItemDetails) o;
                                LocalInvItem invItem = invItemHome.findByIiName(arModInvoiceLineItemDetails.getIliIiName(), companyCode);

                                LocalInvLocation invLocation = null;
                                try {
                                    if (invItem.getIiDefaultLocation() != null) {
                                        invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                                    }
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
                                }
                                LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(arModInvoiceLineItemDetails.getIliIiName(),
                                        invLocation == null ? arModInvoiceLineItemDetails.getIliLocName() : invLocation.getLocName(), companyCode);

                                LocalArInvoiceLineItem arExistingInvoiceLineItem = null;
                                try {
                                    arExistingInvoiceLineItem = arInvoiceLineItemHome.findByRctCodeAndIlCodeAndUomName(arExistingReceipt.getRctCode(),
                                            invItemLocation.getIlCode(), arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
                                }
                                if (arExistingInvoiceLineItem == null) {
                                    LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.create((short) (arExistingReceipt.getArInvoiceLineItems().size() + 1),
                                            arModInvoiceLineItemDetails.getIliQuantity(), arModInvoiceLineItemDetails.getIliUnitPrice(),
                                            EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingReceipt.getArTaxCode().getTcRate() / 100), (short) 2), EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() - (arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingReceipt.getArTaxCode().getTcRate() / 100)), (short) 2),
                                            EJBCommon.FALSE, 0, 0, 0, 0, 0, EJBCommon.TRUE, companyCode);

                                    //arExistingReceipt.addArInvoiceLineItem(arInvoiceLineItem);
                                    arInvoiceLineItem.setArReceipt(arExistingReceipt);

                                    LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                    //invUnitOfMeasure.addArInvoiceLineItem(arInvoiceLineItem);
                                    arInvoiceLineItem.setInvUnitOfMeasure(invUnitOfMeasure);

                                    //invItemLocation.addArInvoiceLineItem(arInvoiceLineItem);
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
                        String generatedInvoice;
                        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
                        LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;

                        try {
                            adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AR INVOICE", companyCode);
                        }
                        catch (FinderException ex) {
                            Debug.print("Finder Exception : " + ex.getMessage());
                        }

                        try {
                            assert adDocumentSequenceAssignment != null;
                            adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome
                                    .findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, companyCode);
                        }
                        catch (FinderException ex) {
                            Debug.print("Finder Exception : " + ex.getMessage());
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
                                Debug.print("Finder Exception : " + ex.getMessage());
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
                                    (byte) 0, "POS Sales " + new Date(), arModReceiptDetails.getRctDate(), generatedInvoice, null, null,
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

                            LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName("IMMEDIATE", companyCode);
                            //adPaymentTerm.addArInvoice(arInvoice);
                            arInvoice.setAdPaymentTerm(adPaymentTerm);

                            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                            //glFunctionalCurrency.addArInvoice(arInvoice);
                            arInvoice.setGlFunctionalCurrency(glFunctionalCurrency);

                            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(arModReceiptDetails.getRctCstCustomerCode(), companyCode);
                            //arCustomer.addArInvoice(arInvoice);
                            arInvoice.setArCustomer(arCustomer);


                            if (!arModReceiptDetails.getRctSlpSalespersonCode().equals("null") && !arModReceiptDetails.getRctSlpSalespersonCode().equals("")) {
                                try {
                                    //rowen 2
                                    LocalArSalesperson arSalesperson = arSalespersonHome.findByPrimaryKey(Integer.parseInt(arModReceiptDetails.getRctSlpSalespersonCode()));
                                    arInvoice.setArSalesperson(arSalesperson);
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
                                }
                            }

                            if (adCompany.getCmpShortName().equalsIgnoreCase("vertext")) {
                                switch (BR_BRNCH_CODE) {
                                    case "HO" -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INC - Main", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arInvoice.setArTaxCode(arTaxCode);
                                    }
                                    case "OL-CENTRO" -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INC - Centro", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arInvoice.setArTaxCode(arTaxCode);
                                    }
                                    case "OL-ROBINSON" -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INC - Robinson", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arInvoice.setArTaxCode(arTaxCode);
                                    }
                                    case "OL-ROUTA" -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INC - Routa", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arInvoice.setArTaxCode(arTaxCode);
                                    }
                                    case "OL-CAPITOL" -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INC - Capitol", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arInvoice.setArTaxCode(arTaxCode);
                                    }
                                    default -> {
                                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INCLUSIVE", companyCode);
                                        //arTaxCode.addArReceipt(arReceipt);
                                        arInvoice.setArTaxCode(arTaxCode);

                                    }
                                }
                            } else {
                                LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("VAT INCLUSIVE", companyCode);
                                //arTaxCode.addArReceipt(arReceipt);
                                arInvoice.setArTaxCode(arTaxCode);

                            }

                            LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName("NONE", companyCode);
                            //arWithholdingTaxCode.addArInvoice(arInvoice);
                            arInvoice.setArWithholdingTaxCode(arWithholdingTaxCode);

                            LocalArInvoiceBatch arInvoiceBatch;
                            try {
                                arInvoiceBatch = arInvoiceBatchHome.findByIbName("POS BATCH", AD_BRNCH, companyCode);
                            }
                            catch (FinderException ex) {
                                arInvoiceBatch = arInvoiceBatchHome.create("POS BATCH",
                                        "POS BATCH", "OPEN", "MISC",
                                        EJBCommon.getGcCurrentDateWoTime().getTime(), cashier, AD_BRNCH, companyCode);
                            }
                            //arInvoiceBatch.addArInvoice(arInvoice);
                            arInvoice.setArInvoiceBatch(arInvoiceBatch);

                            Iterator iter = arModReceiptDetails.getInvIliList().iterator();
                            short lineNumber = 0;
                            while (iter.hasNext()) {

                                ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = (ArModInvoiceLineItemDetails) iter.next();
                                LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.create(++lineNumber,
                                        arModInvoiceLineItemDetails.getIliQuantity(), arModInvoiceLineItemDetails.getIliUnitPrice(),
                                        EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() / (1 + arInvoice.getArTaxCode().getTcRate() / 100), (short) 2), EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() - (arModInvoiceLineItemDetails.getIliAmount() / (1 + arInvoice.getArTaxCode().getTcRate() / 100)), (short) 2),
                                        EJBCommon.FALSE, 0, 0, 0, 0, 0, EJBCommon.TRUE, companyCode);

                                //arInvoice.addArInvoiceLineItem(arInvoiceLineItem);
                                arInvoiceLineItem.setArInvoice(arInvoice);

                                LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                //invUnitOfMeasure.addArInvoiceLineItem(arInvoiceLineItem);
                                arInvoiceLineItem.setInvUnitOfMeasure(invUnitOfMeasure);

                                arInvoiceLineItem.setIliMisc(arModInvoiceLineItemDetails.getIliMisc());

                                LocalInvItem invItem = invItemHome.findByIiName(arModInvoiceLineItemDetails.getIliIiName(), companyCode);

                                LocalInvLocation invLocation = null;
                                try {
                                    if (invItem.getIiDefaultLocation() != null) {
                                        invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                                    }
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
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

                            for (Object o : arModReceiptDetails.getInvIliList()) {

                                ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = (ArModInvoiceLineItemDetails) o;

                                LocalInvItem invItem = invItemHome.findByIiName(arModInvoiceLineItemDetails.getIliIiName(), companyCode);

                                LocalInvLocation invLocation = null;
                                try {
                                    if (invItem.getIiDefaultLocation() != null) {
                                        invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                                    }
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
                                }
                                LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(arModInvoiceLineItemDetails.getIliIiName(),
                                        invLocation == null ? arModInvoiceLineItemDetails.getIliLocName() : invLocation.getLocName(), companyCode);

                                LocalArInvoiceLineItem arExistingInvoiceLineItem = null;
                                try {
                                    arExistingInvoiceLineItem = arInvoiceLineItemHome.findByRctCodeAndIlCodeAndUomName(arExistingInvoice.getInvCode(),
                                            invItemLocation.getIlCode(), arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                }
                                catch (FinderException ex) {
                                    Debug.print("Finder Exception : " + ex.getMessage());
                                }
                                if (arExistingInvoiceLineItem == null) {

                                    LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.create((short) (arExistingInvoice.getArInvoiceLineItems().size() + 1),
                                            arModInvoiceLineItemDetails.getIliQuantity(), arModInvoiceLineItemDetails.getIliUnitPrice(),
                                            EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingInvoice.getArTaxCode().getTcRate() / 100), (short) 2), EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() - (arModInvoiceLineItemDetails.getIliAmount() / (1 + arExistingInvoice.getArTaxCode().getTcRate() / 100)), (short) 2),
                                            EJBCommon.FALSE, 0, 0, 0, 0, 0, EJBCommon.TRUE, companyCode);

                                    //arExistingInvoice.addArInvoiceLineItem(arInvoiceLineItem);
                                    arInvoiceLineItem.setArInvoice(arExistingInvoice);

                                    LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(arModInvoiceLineItemDetails.getIliUomName(), companyCode);
                                    //invUnitOfMeasure.addArInvoiceLineItem(arInvoiceLineItem);
                                    arInvoiceLineItem.setInvUnitOfMeasure(invUnitOfMeasure);
                                    arInvoiceLineItem.setIliMisc(arModInvoiceLineItemDetails.getIliMisc());
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
                for (Object o : receiptNumbers.values()) {

                    ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) o;

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
                        double COST;
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
                            Debug.print("Finder Exception : " + ex.getMessage());
                        }

                        if (arInvoiceLineItem.getIliEnableAutoBuild() == 0
                                || arInvoiceLineItem.getIliEnableAutoBuild() == 1
                                || arInvoiceLineItem.getInvItemLocation().getInvItem().getIiClass().equals("Stock")) {

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
                    }

                    // add tax distribution if necessary
                    if (!arReceipt.getArTaxCode().getTcType().equals("NONE") &&
                            !arReceipt.getArTaxCode().getTcType().equals("EXEMPT")) {

                        this.addArDrEntry(arReceipt.getArDrNextLine(),
                                "TAX", EJBCommon.FALSE, TOTAL_TAX, arReceipt.getArTaxCode().getGlChartOfAccount().getCoaCode(),
                                EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

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
                    double forexGainLoss = TOTAL_LINE - (arReceipt.getRctAmount() +
                            arModUploadReceiptDetails.getRctPosDiscount() -
                            arModUploadReceiptDetails.getRctPosScAmount() -
                            arModUploadReceiptDetails.getRctPosDcAmount());
                    if (forexGainLoss != 0) {
                        this.addArDrEntry(arReceipt.getArDrNextLine(), "FOREX",
                                forexGainLoss > 0 ? EJBCommon.TRUE : EJBCommon.FALSE, Math.abs(forexGainLoss),
                                adPreference.getPrfMiscPosGiftCertificateAccount(),
                                EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                    }
                }

                for (Object o : invoiceNumbers.values()) {

                    ArModReceiptDetails arModUploadReceiptDetails = (ArModReceiptDetails) o;

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
                        switch (arInvoice.getAdPaymentTerm().getPytScheduleBasis()) {
                            case "DEFAULT" -> {
                                gcDateDue.setTime(arInvoice.getInvEffectivityDate());
                                gcDateDue.add(Calendar.DATE, adPaymentSchedule.getPsDueDay());
                            }
                            case "MONTHLY" -> {
                                gcDateDue = gcPrevDateDue;
                                gcDateDue.add(Calendar.MONTH, 1);
                                gcPrevDateDue = gcDateDue;
                            }
                            case "BI-MONTHLY" -> {
                                gcDateDue = gcPrevDateDue;
                                if (gcPrevDateDue.get(Calendar.MONTH) != 1) {
                                    if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 31
                                            && gcPrevDateDue.get(Calendar.DATE) > 15
                                            && gcPrevDateDue.get(Calendar.DATE) < 31) {
                                        gcDateDue.add(Calendar.DATE, 16);
                                    } else {
                                        gcDateDue.add(Calendar.DATE, 15);
                                    }
                                } else if (gcPrevDateDue.get(Calendar.MONTH) == 1) {
                                    if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 28
                                            && gcPrevDateDue.get(Calendar.DATE) == 14) {
                                        gcDateDue.add(Calendar.DATE, 14);
                                    } else if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 28
                                            && gcPrevDateDue.get(Calendar.DATE) >= 15
                                            && gcPrevDateDue.get(Calendar.DATE) < 28) {
                                        gcDateDue.add(Calendar.DATE, 13);
                                    } else if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 29
                                            && gcPrevDateDue.get(Calendar.DATE) >= 15
                                            && gcPrevDateDue.get(Calendar.DATE) < 29) {
                                        gcDateDue.add(Calendar.DATE, 14);
                                    } else {
                                        gcDateDue.add(Calendar.DATE, 15);
                                    }
                                }
                                gcPrevDateDue = gcDateDue;
                            }
                        }

                        // create a payment schedule
                        double PAYMENT_SCHEDULE_AMOUNT;

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
                        double COST;

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
                            Debug.print("Finder Exception : " + ex.getMessage());
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

                    }

                    // add tax distribution if necessary
                    if (!arInvoice.getArTaxCode().getTcType().equals("NONE") &&
                            !arInvoice.getArTaxCode().getTcType().equals("EXEMPT")) {

                        this.addArDrEntry(arInvoice.getArDrNextLine(),
                                "TAX", EJBCommon.FALSE, TOTAL_TAX, arInvoice.getArTaxCode().getGlChartOfAccount().getCoaCode(),
                                EJBCommon.FALSE, arInvoice, AD_BRNCH, companyCode);

                    }

                    // add cash distribution
                    LocalAdBranchCustomer adBranchCustomer = null;
                    try {

                        adBranchCustomer = adBranchCustomerHome.findBcstByCstCodeAndBrCode(arInvoice.getArCustomer().getCstCode(), AD_BRNCH, companyCode);

                    }
                    catch (FinderException ex) {
                        Debug.print("Finder Exception : " + ex.getMessage());
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
                        // add discount.
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
                    double forexGainLoss = TOTAL_LINE - (arInvoice.getInvAmountDue() +
                            arModUploadReceiptDetails.getRctPosDiscount() -
                            arModUploadReceiptDetails.getRctPosScAmount() -
                            arModUploadReceiptDetails.getRctPosDcAmount());
                    if (forexGainLoss != 0) {
                        this.addArDrEntry(arInvoice.getArDrNextLine(), "FOREX",
                                forexGainLoss > 0 ? EJBCommon.TRUE : EJBCommon.FALSE, Math.abs(forexGainLoss),
                                adPreference.getPrfMiscPosGiftCertificateAccount(),
                                EJBCommon.FALSE, arInvoice, AD_BRNCH, companyCode);
                    }
                }
                success = 1;
            }
            return success;
        }
        catch (Exception ex) {

            ex.printStackTrace();
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }
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
            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome
                    .create(DR_LN, DR_CLSS, DR_DBT,
                            EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()),
                            EJBCommon.FALSE, EJBCommon.FALSE, companyCode);

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
            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome
                    .create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()),
                            EJBCommon.FALSE, EJBCommon.FALSE, companyCode);

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
            if (DR_CLSS.equals("DISCOUNT") || DR_CLSS.equals("FOREX")) {

                LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(AD_BRNCH);
                StringBuilder newCoa = new StringBuilder(adBranch.getBrCoaSegment());

                StringTokenizer st = new StringTokenizer(glChartOfAccount.getCoaAccountNumber(), "-");
                int ctr = 0;
                while (st.hasMoreTokens()) {
                    ctr++;
                    if (ctr >= 3) {
                        newCoa.append("-").append(st.nextToken());
                    } else {
                        st.nextToken();
                    }
                }
                glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(newCoa.toString(), companyCode);
            }

            // create distribution record
            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome
                    .create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT,
                            adCompany.getGlFunctionalCurrency().getFcPrecision()),
                            EJBCommon.FALSE, DR_RVRSD, companyCode);

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
                LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(AD_BRNCH);
                StringBuilder newCoa = new StringBuilder(adBranch.getBrCoaSegment());
                StringTokenizer st = new StringTokenizer(glChartOfAccount.getCoaAccountNumber(), "-");
                int ctr = 0;
                while (st.hasMoreTokens()) {
                    ctr++;
                    if (ctr >= 3) {
                        newCoa.append("-").append(st.nextToken());
                    } else {
                        st.nextToken();
                    }
                }
                glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(newCoa.toString(), companyCode);

            }

            // create distribution record
            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome
                    .create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()),
                            EJBCommon.FALSE, DR_RVRSD, companyCode);

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

        String segment2;
        switch (BR_BRNCH_CODE) {
            case "Manila-Smoking Lounge" -> {
                segment1 = "MA";
                segment2 = "SL";
            }
            case "Manila-Cigar Shop" -> {
                segment1 = "MA";
                segment2 = "CS";
            }
            case "Manila-Term.#2 Domestic" -> {
                segment1 = "MA";
                segment2 = "TD";
            }
            case "Manila-Term.#2 Intl" -> {
                segment1 = "MA";
                segment2 = "TI";
            }
            case "Cebu-Banilad" -> {
                segment1 = "CE";
                segment2 = "BA";
            }
            case "Cebu-Gorordo" -> {
                segment1 = "CE";
                segment2 = "GO";
            }
            case "Cebu-Mactan Domestic" -> {
                segment1 = "CE";
                segment2 = "MD";
            }
            case "Cebu-Mactan Intl" -> {
                segment1 = "CE";
                segment2 = "MI";
            }
            case "Manila-Zara Marketing" -> {
                segment1 = "MA";
                segment2 = "ZA";
            }
            case "Cebu-Supercat" -> {
                segment1 = "CE";
                segment2 = "SU";
            }
            default -> {
                segment1 = "HQ";
                segment2 = "HQ";
            }
        }

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
                                  boolean isAdjustFifo, Integer AD_BRNCH, Integer companyCode) {

        Debug.print("ArMiscReceiptSyncControllerBean getInvFifoCost");

        try {

            Collection invFifoCostings = invCostingHome.findFifoRemainingQuantityByLessThanOrEqualCstDateAndIlCodeAndBrCode(CST_DT, IL_CODE, AD_BRNCH, companyCode);

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
                        return EJBCommon.roundIt(invFifoCosting.getCstItemCost() / invFifoCosting.getCstQuantityReceived(), this.getGlFcPrecisionUnit(companyCode));
                    } else if (invFifoCosting.getArInvoiceLineItem() != null) {
                        return EJBCommon.roundIt(invFifoCosting.getCstCostOfSales() / invFifoCosting.getCstQuantitySold(), this.getGlFcPrecisionUnit(companyCode));
                    } else if (invFifoCosting.getInvBuildUnbuildAssemblyLine() != null) {
                        return EJBCommon.roundIt(invFifoCosting.getCstAssemblyCost() / invFifoCosting.getCstAssemblyQuantity(), this.getGlFcPrecisionUnit(companyCode));
                    } else {
                        return EJBCommon.roundIt(invFifoCosting.getCstAdjustCost() / invFifoCosting.getCstAdjustQuantity(), this.getGlFcPrecisionUnit(companyCode));
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

        // void reason
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);

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
        }
        catch (Exception ex) {
            throw ex;
        }

        // location
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);

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
            Debug.print("Finder Exception : " + ex.getMessage());
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

        String lineSeparator = "~";
        // begin lineSeparator
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(lineSeparator, start);

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

        Debug.print("ArMiscReceiptSyncControllerBean receiptDecodeUS");


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
        }
        catch (Exception ex) {

            throw ex;
        }

        // location
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;

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
        catch (Exception ex) { }

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

        // payment type
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;

        // or number
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctNumber(receipt.substring(start, start + length));

        // pos name
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctPOSName(receipt.substring(start, start + length));

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

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            arModReceiptDetails.setRctDate(sdf.parse(receipt.substring(start, start + length)));
        }
        catch (Exception ex) {

            throw ex;
        }

        // location
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);

        // payment method
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);

        // sc amount
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);

        length = nextIndex - start;

        try {
            arModReceiptDetails.setRctPosScAmount(Double.parseDouble(receipt.substring(start, start + length)));
        }
        catch (Exception ex) { }

        // dc amount
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);

        length = nextIndex - start;

        try {
            arModReceiptDetails.setRctPosDcAmount(Double.parseDouble(receipt.substring(start, start + length)));
        }
        catch (Exception ex) { }

        // on account
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctPosOnAccount((byte) Integer.parseInt(receipt.substring(start, start + length).equals("True") ? "1" : "0"));

        // vat code name
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctTcName(receipt.substring(start, start + length));

        // CUSTOMER NAME / NOTE
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctCstName(receipt.substring(start, start + length));

        // SALESPERSON
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctSlpSalespersonCode(receipt.substring(start, start + length));

        // CASH AMOUNT
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctAmountCash(Double.parseDouble(receipt.substring(start, start + length)));

        // CARD NUMBER 1
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctCardNumber1(receipt.substring(start, start + length));

        // CARD TYPE 1
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctCardType1(receipt.substring(start, start + length));

        // CARD AMOUNT 1
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctAmountCard1(Double.parseDouble(receipt.substring(start, start + length)));

        // CARD NUMBER 2
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctCardNumber2(receipt.substring(start, start + length));

        // CARD TYPE 2
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctCardType2(receipt.substring(start, start + length));

        // CARD AMOUNT 2
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctAmountCard2(Double.parseDouble(receipt.substring(start, start + length)));

        // CARD NUMBER 3
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctCardNumber3(receipt.substring(start, start + length));

        // CARD TYPE 3
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctCardType3(receipt.substring(start, start + length));

        // CARD AMOUNT 3
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctAmountCard3(Double.parseDouble(receipt.substring(start, start + length)));

        //CHEQUE NUMBER
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctChequeNumber(receipt.substring(start, start + length));

        //CHEQUE AMOUNT
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctAmountCheque(Double.parseDouble(receipt.substring(start, start + length)));

        //VOUCHER NUMBER
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctVoucherNumber(receipt.substring(start, start + length));

        //VOUCHER AMOUNT
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctAmountVoucher(Double.parseDouble(receipt.substring(start, start + length)));

        // end separator
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;

        String lineSeparator = "~";
        // begin lineSeparator
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(lineSeparator, start);

        ArrayList invIliList = new ArrayList();

        while (true) {
            String asdf;
            String asdf2;
            ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = new ArModInvoiceLineItemDetails();

            // begin separator
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);

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

            // item name
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineItemDetails.setIliIiName(receipt.substring(start, start + length));

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

            // Expiry Date / IMEI
            start = nextIndex + 1;
            nextIndex = receipt.indexOf("$~", start);

            asdf = receipt.substring(start);
            if (!asdf.equals("~~")) {
                asdf = asdf.substring(1, asdf.length() - 2);

                //OLD CODE
                asdf2 = expiryDates2("$" + asdf + "fin$");
                arModInvoiceLineItemDetails.setILI_IMEI(asdf2);

                //NEW CODE
                ArrayList tagList = this.convertMiscToInvModTagList(asdf);
                arModInvoiceLineItemDetails.setIliTagList(tagList);
            }
            invIliList.add(arModInvoiceLineItemDetails);

            // begin lineSeparator
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(lineSeparator, start);

            int tempStart = nextIndex + 1;
            if (receipt.indexOf(separator, tempStart) == -1) {
                break;
            }

        }

        arModReceiptDetails.setInvIliList(invIliList);

        return arModReceiptDetails;

    }

    private ArModReceiptDetails receiptDecodeWithExpiryDateAndEntries(String receipt) throws Exception {

        Debug.print("ArMiscReceiptSyncControllerBean receiptDecodeWithExpiryDateAndEntries");

        String separator = "$";
        ArModReceiptDetails arModReceiptDetails = new ArModReceiptDetails();

        // Remove first $ character
        receipt = receipt.substring(1);

        // customer code
        int start = 0;
        int nextIndex = receipt.indexOf(separator, start);
        int length = nextIndex - start;
        arModReceiptDetails.setRctCstCustomerCode(receipt.substring(start, start + length));

        // payment type
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);

        // or number
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctNumber(receipt.substring(start, start + length));

        // receipt source
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);

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

        // void reason
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);

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
        }
        catch (Exception ex) {
            throw ex;
        }

        // location
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);

        // payment method
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);

        // sc amount
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;

        try {
            arModReceiptDetails.setRctPosScAmount(Double.parseDouble(receipt.substring(start, start + length)));
        }
        catch (Exception ex) { }

        // dc amount
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;

        try {
            arModReceiptDetails.setRctPosDcAmount(Double.parseDouble(receipt.substring(start, start + length)));
        }
        catch (Exception ex) { }

        // on account
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctPosOnAccount((byte) Integer.parseInt(receipt.substring(start, start + length)));

        // end separator
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);

        String lineSeparator = "~";
        // begin lineSeparator
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(lineSeparator, start);

        ArrayList invIliList = new ArrayList();

        while (true) {
            String asdf;
            ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = new ArModInvoiceLineItemDetails();

            // begin separator
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);

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

            // Expiry Date
            start = nextIndex + 1;
            nextIndex = receipt.indexOf("$~", start);

            asdf = receipt.substring(start);
            if (!asdf.equals("~~")) {
                asdf = asdf.substring(1, asdf.length() - 2);
                arModInvoiceLineItemDetails.setIliMisc(asdf);
            }
            invIliList.add(arModInvoiceLineItemDetails);

            // begin lineSeparator
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(lineSeparator, start);

            int tempStart = nextIndex + 1;
            if (receipt.indexOf(separator, tempStart) == -1) {
                break;
            }

        }
        arModReceiptDetails.setInvIliList(invIliList);

        while (true) {
            ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = new ArModInvoiceLineItemDetails();

            // begin separator
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);

            // line number
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);
            length = nextIndex - start;
            arModInvoiceLineItemDetails.setIliLine(Short.parseShort(receipt.substring(start, start + length)));

            // begin lineSeparator
            start = nextIndex + 1;

            break;
        }
        return arModReceiptDetails;
    }

    private ArModReceiptDetails receiptDecodeWithSalesperson(String receipt) throws Exception {

        Debug.print("ArMiscReceiptSyncControllerBean receiptDecodeWithSalesperson");

        String separator = "$";
        ArModReceiptDetails arModReceiptDetails = new ArModReceiptDetails();

        // Remove first $ character
        receipt = receipt.substring(1);

        // customer code
        int start = 0;
        int nextIndex = receipt.indexOf(separator, start);
        int length = nextIndex - start;
        arModReceiptDetails.setRctCstCustomerCode(receipt.substring(start, start + length));

        // payment type
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);

        // or number
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctNumber(receipt.substring(start, start + length));

        // waiter/salesperson
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);
        length = nextIndex - start;
        arModReceiptDetails.setRctSlpSalespersonCode(receipt.substring(start, start + length));

        // receipt source
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);

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

        // void reason
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);

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
        }
        catch (Exception ex) {

            throw ex;
        }

        // location
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);

        // payment method
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(separator, start);

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

        String lineSeparator = "~";
        // begin lineSeparator
        start = nextIndex + 1;
        nextIndex = receipt.indexOf(lineSeparator, start);

        ArrayList invIliList = new ArrayList();

        while (true) {
            String asdf = "";
            String asdf2 = "";
            ArModInvoiceLineItemDetails arModInvoiceLineItemDetails = new ArModInvoiceLineItemDetails();

            // begin separator
            start = nextIndex + 1;
            nextIndex = receipt.indexOf(separator, start);

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

            // Expiry Date
            start = nextIndex + 1;
            nextIndex = receipt.indexOf("$~", start);

            asdf = receipt.substring(start);
            if (!asdf.equals("~~")) {
                asdf = asdf.substring(1, asdf.length() - 2);
                asdf2 = expiryDates2("$" + asdf + "fin$");
                arModInvoiceLineItemDetails.setIliMisc(asdf2);
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

    private String expiryDates2(String misc) {

        Debug.print("ArMiscReceiptSyncControllerBean expiryDates2");

        String separator = "$";

        // Remove first $ character
        misc = misc.substring(1);

        // Counter
        int start = 0;
        int nextIndex = misc.indexOf(separator, start);
        int length;

        ArrayList miscList = new ArrayList();
        String checker = "";
        StringBuilder checker2 = new StringBuilder();
        int qty = 0;
        try {
            while (!checker.equals("fin")) {

                // Date
                start = nextIndex + 1;
                nextIndex = misc.indexOf(separator, start);
                length = nextIndex - start;

                checker = misc.substring(start, start + length);
                if (checker.length() == 0 || checker.equals("~")) {
                    break;
                }
                if (checker.equals("fin")) {
                    break;
                }
                if (checker.length() != 0 || checker != "null") {
                    miscList.add(checker);
                    checker2.append("$").append(checker);
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
        return retExp;
    }

    private void executeArRctPost(Integer RCT_CODE, String USR_NM, Integer AD_BRNCH, Integer companyCode) throws
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

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

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
                this.regenerateInventoryDr(arReceipt, AD_BRNCH, companyCode);
            }

            // post receipt
            if (arReceipt.getRctVoid() == EJBCommon.FALSE && arReceipt.getRctPosted() == EJBCommon.FALSE) {
                if (arReceipt.getRctType().equals("MISC") && !arReceipt.getArInvoiceLineItems().isEmpty()) {
                    for (Object o : arReceipt.getArInvoiceLineItems()) {
                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) o;

                        String II_NM = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName();
                        String LOC_NM = arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName();

                        double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(),
                                arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                        LocalInvCosting invCosting = null;

                        try {
                            invCosting = invCostingHome
                                    .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                            arReceipt.getRctDate(), II_NM, LOC_NM, AD_BRNCH, companyCode);
                        }
                        catch (FinderException ex) {
                            Debug.print("Sync RCT date not found : " + ex.getMessage());
                        }

                        double COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                        if (invCosting == null) {
                            this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(),
                                    QTY_SLD, COST * QTY_SLD,
                                    -QTY_SLD, -COST * QTY_SLD,
                                    0d, null, AD_BRNCH, companyCode);
                        } else {
                            switch (invCosting.getInvItemLocation().getInvItem().getIiCostMethod()) {
                                case "Average" -> {
                                    double avgCost = invCosting.getCstRemainingQuantity() == 0 ? COST :
                                            Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                                    this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(),
                                            QTY_SLD, avgCost * QTY_SLD,
                                            invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (avgCost * QTY_SLD),
                                            0d, null, AD_BRNCH, companyCode);
                                }
                                case "FIFO" -> {
                                    double fifoCost = invCosting.getCstRemainingQuantity() == 0 ? COST :
                                            this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(),
                                                    QTY_SLD, arInvoiceLineItem.getIliUnitPrice(), true, AD_BRNCH, companyCode);
                                    this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(),
                                            QTY_SLD, fifoCost * QTY_SLD,
                                            invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (fifoCost * QTY_SLD),
                                            0d, null, AD_BRNCH, companyCode);
                                }
                                case "Standard" -> {
                                    double standardCost = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                                    this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(),
                                            QTY_SLD, standardCost * QTY_SLD,
                                            invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (standardCost * QTY_SLD),
                                            0d, null, AD_BRNCH, companyCode);
                                }
                            }
                        }
                    }
                }

                // increase bank balance CASH
                if (arReceipt.getRctAmountCash() > 0) {
                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccount().getBaCode());
                    try {
                        // find bank account balance before or equal receipt date
                        Collection adBankAccountBalances = adBankAccountBalanceHome
                                .findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(),
                                        arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {
                            // get last check
                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);
                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);
                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {
                                // create new balance
                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(
                                        arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCash(), "BOOK", companyCode);
                                //adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date
                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCash());
                            }
                        } else {
                            // create new balance
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(
                                    arReceipt.getRctDate(), (arReceipt.getRctAmountCash()), "BOOK", companyCode);
                            //adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary
                        adBankAccountBalances = adBankAccountBalanceHome
                                .findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(),
                                        arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);
                        for (Object bankAccountBalance : adBankAccountBalances) {
                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;
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
                        // find bank account balance before or equal receipt date
                        Collection adBankAccountBalances = adBankAccountBalanceHome
                                .findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(),
                                        arReceipt.getAdBankAccountCard1().getBaCode(), "BOOK", companyCode);
                        if (!adBankAccountBalances.isEmpty()) {
                            // get last check
                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);
                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);
                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {
                                // create new balance
                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(
                                        arReceipt.getRctDate(), adBankAccountBalance.getBabBalance()
                                                + arReceipt.getRctAmountCard1(), "BOOK", companyCode);
                                //adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date
                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard1());
                            }
                        } else {
                            // create new balance
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(
                                    arReceipt.getRctDate(), (arReceipt.getRctAmountCard1()), "BOOK", companyCode);
                            //adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary
                        adBankAccountBalances = adBankAccountBalanceHome
                                .findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(),
                                        arReceipt.getAdBankAccountCard1().getBaCode(), "BOOK", companyCode);
                        for (Object bankAccountBalance : adBankAccountBalances) {
                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;
                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard1());
                        }
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                // increase bank balance CARD 2
                if (arReceipt.getRctAmountCard2() > 0) {
                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccountCard2().getBaCode());
                    try {
                        // find bank account balance before or equal receipt date
                        Collection adBankAccountBalances = adBankAccountBalanceHome
                                .findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(),
                                        arReceipt.getAdBankAccountCard2().getBaCode(), "BOOK", companyCode);
                        if (!adBankAccountBalances.isEmpty()) {
                            // get last check
                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);
                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);
                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {
                                // create new balance
                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(
                                        arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard2(), "BOOK", companyCode);
                                //adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date
                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard2());
                            }
                        } else {
                            // create new balance
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(
                                    arReceipt.getRctDate(), (arReceipt.getRctAmountCard2()), "BOOK", companyCode);
                            //adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary
                        adBankAccountBalances = adBankAccountBalanceHome.
                                findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(),
                                        arReceipt.getAdBankAccountCard2().getBaCode(), "BOOK", companyCode);
                        for (Object bankAccountBalance : adBankAccountBalances) {
                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;
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

                        // find bank account balance before or equal receipt date
                        Collection adBankAccountBalances = adBankAccountBalanceHome
                                .findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(),
                                        arReceipt.getAdBankAccountCard3().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(
                                        arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard3(), "BOOK", companyCode);

                                //adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard3());

                            }

                        } else {

                            // create new balance
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(
                                    arReceipt.getRctDate(), (arReceipt.getRctAmountCard3()), "BOOK", companyCode);

                            //adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);

                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard3().getBaCode(), "BOOK", companyCode);

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

                        // find bank account balance before or equal receipt date
                        Collection adBankAccountBalances = adBankAccountBalanceHome
                                .findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(),
                                        arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(
                                        arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCheque(), "BOOK", companyCode);

                                //adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCheque());

                            }

                        } else {

                            // create new balance
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(
                                    arReceipt.getRctDate(), (arReceipt.getRctAmountCheque()), "BOOK", companyCode);

                            //adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);

                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

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

                // validate if date has no period and period is closed
                LocalGlSetOfBook glJournalSetOfBook;
                try {
                    glJournalSetOfBook = glSetOfBookHome.findByDate(arReceipt.getRctDate(), companyCode);
                }
                catch (FinderException ex) {
                    throw new GlJREffectiveDateNoPeriodExistException();
                }

                LocalGlAccountingCalendarValue glAccountingCalendarValue =
                        glAccountingCalendarValueHome.findByAcCodeAndDate(
                                glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), arReceipt.getRctDate(), companyCode);

                if (glAccountingCalendarValue.getAcvStatus() == 'N' ||
                        glAccountingCalendarValue.getAcvStatus() == 'C' ||
                        glAccountingCalendarValue.getAcvStatus() == 'P') {
                    throw new GlJREffectiveDatePeriodClosedException();
                }

                // check if invoice is balance if not check suspense posting
                LocalGlJournalLine glOffsetJournalLine = null;
                Collection arDistributionRecords;
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
                    double DR_AMNT;
                    if (arDistributionRecord.getArAppliedInvoice() != null) {
                        LocalArInvoice arInvoice = arDistributionRecord.getArAppliedInvoice().getArInvoicePaymentSchedule().getArInvoice();
                        DR_AMNT = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(),
                                arInvoice.getGlFunctionalCurrency().getFcName(),
                                arInvoice.getInvConversionDate(),
                                arInvoice.getInvConversionRate(),
                                arDistributionRecord.getDrAmount(), companyCode);
                    } else {
                        DR_AMNT = this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(),
                                arReceipt.getGlFunctionalCurrency().getFcName(),
                                arReceipt.getRctConversionDate(),
                                arReceipt.getRctConversionRate(),
                                arDistributionRecord.getDrAmount(), companyCode);
                    }

                    if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                        TOTAL_DEBIT += DR_AMNT;
                    } else {
                        TOTAL_CREDIT += DR_AMNT;
                    }
                }
                TOTAL_DEBIT = EJBCommon.roundIt(TOTAL_DEBIT, adCompany.getGlFunctionalCurrency().getFcPrecision());
                TOTAL_CREDIT = EJBCommon.roundIt(TOTAL_CREDIT, adCompany.getGlFunctionalCurrency().getFcPrecision());

                if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.TRUE &&
                        TOTAL_DEBIT != TOTAL_CREDIT) {
                    LocalGlSuspenseAccount glSuspenseAccount;
                    try {
                        glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("ACCOUNTS RECEIVABLES", "SALES RECEIPTS", companyCode);
                    }
                    catch (FinderException ex) {
                        throw new GlobalJournalNotBalanceException();
                    }

                    if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {
                        glOffsetJournalLine = glJournalLineHome.create((short) (arDistributionRecords.size() + 1),
                                EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);
                    } else {
                        glOffsetJournalLine = glJournalLineHome.create((short) (arDistributionRecords.size() + 1),
                                EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
                    }

                    LocalGlChartOfAccount glChartOfAccount = glSuspenseAccount.getGlChartOfAccount();
                    //glChartOfAccount.addGlJournalLine(glOffsetJournalLine);
                    glOffsetJournalLine.setGlChartOfAccount(glChartOfAccount);


                } else if (TOTAL_DEBIT > 9999999999d ||
                        TOTAL_CREDIT > 9999999999d || (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE &&
                        TOTAL_DEBIT != TOTAL_CREDIT)) {
                    throw new GlobalJournalNotBalanceException();

                }

                // create journal batch if necessary
                LocalGlJournalBatch glJournalBatch = null;
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");

                try {

                    if (adPreference.getPrfEnableArReceiptBatch() == EJBCommon.TRUE) {

                        glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " " + arReceipt.getArReceiptBatch().getRbName(), AD_BRNCH, companyCode);

                    } else {

                        glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " SALES RECEIPTS", AD_BRNCH, companyCode);

                    }


                }
                catch (FinderException ex) {
                    Debug.print("Finder Exception : " + ex.getMessage());
                }

                if (adPreference.getPrfEnableGlJournalBatch() == EJBCommon.TRUE &&
                        glJournalBatch == null) {

                    if (adPreference.getPrfEnableArReceiptBatch() == EJBCommon.TRUE) {

                        glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " " + arReceipt.getArReceiptBatch().getRbName(), "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, AD_BRNCH, companyCode);

                    } else {

                        glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " SALES RECEIPTS", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, AD_BRNCH, companyCode);

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
                        AD_BRNCH, companyCode);

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

                while (j.hasNext()) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) j.next();
                    LocalArInvoice arInvoice = null;

                    double DR_AMNT;

                    if (arDistributionRecord.getArAppliedInvoice() != null) {

                        arInvoice = arDistributionRecord.getArAppliedInvoice().getArInvoicePaymentSchedule().getArInvoice();

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(),
                                arInvoice.getGlFunctionalCurrency().getFcName(),
                                arInvoice.getInvConversionDate(),
                                arInvoice.getInvConversionRate(),
                                arDistributionRecord.getDrAmount(), companyCode);

                    } else {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(),
                                arReceipt.getGlFunctionalCurrency().getFcName(),
                                arReceipt.getRctConversionDate(),
                                arReceipt.getRctConversionRate(),
                                arDistributionRecord.getDrAmount(), companyCode);

                    }

                    LocalGlJournalLine glJournalLine = glJournalLineHome.create(arDistributionRecord.getDrLine(),
                            arDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

                    glJournalLine.setGlChartOfAccount(arDistributionRecord.getGlChartOfAccount());

                    glJournalLine.setGlJournal(glJournal);

                    arDistributionRecord.setDrImported(EJBCommon.TRUE);

                    // for FOREX revaluation
                    int FC_CODE;
                    if (arDistributionRecord.getArAppliedInvoice() != null) {
                        assert arInvoice != null;
                        FC_CODE = arInvoice.getGlFunctionalCurrency().getFcCode();
                    } else {
                        FC_CODE = arReceipt.getGlFunctionalCurrency().getFcCode();
                    }

                    if ((FC_CODE != adCompany.getGlFunctionalCurrency().getFcCode()) &&
                            glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null && (FC_CODE ==
                            glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode())) {

                        double CONVERSION_RATE;
                        if (arDistributionRecord.getArAppliedInvoice() != null) {
                            assert arInvoice != null;
                            CONVERSION_RATE = arInvoice.getInvConversionRate();
                        } else {
                            CONVERSION_RATE = arReceipt.getRctConversionRate();
                        }

                        Date DATE;
                        if (arDistributionRecord.getArAppliedInvoice() != null) {
                            assert arInvoice != null;
                            DATE = arInvoice.getInvConversionDate();
                        } else {
                            DATE = arReceipt.getRctConversionDate();
                        }

                        if (DATE != null && (CONVERSION_RATE == 0 || CONVERSION_RATE == 1)) {

                            CONVERSION_RATE = this.getFrRateByFrNameAndFrDate(
                                    glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcName(),
                                    glJournal.getJrConversionDate(), companyCode);

                        } else if (CONVERSION_RATE == 0) {

                            CONVERSION_RATE = 1;

                        }

                        Collection glForexLedgers = null;

                        if (arDistributionRecord.getArAppliedInvoice() != null) {
                            assert arInvoice != null;
                            DATE = arInvoice.getInvDate();
                        } else {
                            DATE = arReceipt.getRctDate();
                        }

                        try {
                            glForexLedgers = glForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(
                                    DATE, glJournalLine.getGlChartOfAccount().getCoaCode(),
                                    companyCode);
                        }
                        catch (FinderException ex) {
                            Debug.print("Finder Exception : " + ex.getMessage());
                        }

                        assert glForexLedgers != null;
                        LocalGlForexLedger glForexLedger =
                                glForexLedgers.isEmpty() ? null :
                                        (LocalGlForexLedger) glForexLedgers.iterator().next();

                        int FRL_LN = (glForexLedger != null &&
                                glForexLedger.getFrlDate().compareTo(DATE) == 0) ?
                                glForexLedger.getFrlLine() + 1 : 1;

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
                                CONVERSION_RATE, COA_FRX_BLNC, 0d, companyCode);

                        //glJournalLine.getGlChartOfAccount().addGlForexLedger(glForexLedger);
                        glForexLedger.setGlChartOfAccount(glJournalLine.getGlChartOfAccount());

                        // propagate balances
                        try {

                            glForexLedgers = glForexLedgerHome.findByGreaterThanFrlDateAndCoaCode(
                                    glForexLedger.getFrlDate(), glForexLedger.getGlChartOfAccount().getCoaCode(),
                                    glForexLedger.getFrlAdCompany());

                        }
                        catch (FinderException ex) {
                            Debug.print("Finder Exception : " + ex.getMessage());
                        }
                        for (Object forexLedger : glForexLedgers) {

                            glForexLedger = (LocalGlForexLedger) forexLedger;
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
                for (Object journalLine : glJournalLines) {

                    LocalGlJournalLine glJournalLine = (LocalGlJournalLine) journalLine;

                    // post current to current acv

                    this.postToGl(glAccountingCalendarValue,
                            glJournalLine.getGlChartOfAccount(),
                            true, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);


                    // post to subsequent acvs (propagate)

                    Collection glSubsequentAccountingCalendarValues =
                            glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(
                                    glJournalSetOfBook.getGlAccountingCalendar().getAcCode(),
                                    glAccountingCalendarValue.getAcvPeriodNumber(), companyCode);

                    Iterator acvsIter = glSubsequentAccountingCalendarValues.iterator();

                    while (acvsIter.hasNext()) {

                        LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue =
                                (LocalGlAccountingCalendarValue) acvsIter.next();

                        this.postToGl(glSubsequentAccountingCalendarValue,
                                glJournalLine.getGlChartOfAccount(),
                                false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                    }

                    // post to subsequent years if necessary

                    Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode);

                    if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                        adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                        LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome
                                .findByCoaAccountNumber(adCompany.getCmpRetainedEarnings(), companyCode);

                        for (Object subsequentSetOfBook : glSubsequentSetOfBooks) {
                            LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) subsequentSetOfBook;
                            String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                            // post to subsequent acvs of subsequent set of book(propagate)
                            Collection glAccountingCalendarValues =
                                    glAccountingCalendarValueHome
                                            .findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                            for (Object accountingCalendarValue : glAccountingCalendarValues) {

                                LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue =
                                        (LocalGlAccountingCalendarValue) accountingCalendarValue;

                                if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY") ||
                                        ACCOUNT_TYPE.equals("OWNERS EQUITY")) {

                                    this.postToGl(glSubsequentAccountingCalendarValue,
                                            glJournalLine.getGlChartOfAccount(),
                                            false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                                } else { // revenue & expense

                                    this.postToGl(glSubsequentAccountingCalendarValue,
                                            glRetainedEarningsAccount,
                                            false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

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
        catch (GlJREffectiveDateNoPeriodExistException | GlJREffectiveDatePeriodClosedException |
               GlobalJournalNotBalanceException | GlobalRecordAlreadyDeletedException |
               GlobalTransactionAlreadyPostedException | GlobalTransactionAlreadyVoidPostedException |
               GlobalExpiryDateNotFoundException ex) {

            //getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (AdPRFCoaGlVarianceAccountNotFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            //getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }
    }

    private void regenerateInventoryDr(LocalArReceipt arReceipt, Integer AD_BRNCH, Integer companyCode)
            throws GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException {

        Debug.print("ArMiscReceiptSyncControllerBean regenerateInventoryDr");

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
                                invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }

                    // add cost of sales distribution and inventory


                    double COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                    try {

                        LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                arReceipt.getRctDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);


                        if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Average")) {
                            COST = invCosting.getCstRemainingQuantity() <= 0 ? COST :
                                    Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                        }

                        if (COST <= 0) {
                            COST = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                        } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("FIFO")) {
                            COST = invCosting.getCstRemainingQuantity() == 0 ? COST :
                                    Math.abs(this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(),
                                            arInvoiceLineItem.getIliQuantity(), arInvoiceLineItem.getIliUnitPrice(), false, AD_BRNCH, companyCode));
                        } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Standard")) {
                            COST = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                        }

                    }
                    catch (FinderException ex) {
                    }

                    double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(),
                            arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {

                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arInvoiceLineItem.getInvItemLocation().getIlCode(), AD_BRNCH, companyCode);

                    }
                    catch (FinderException ex) {

                    }

                    if (arInvoiceLineItem.getIliEnableAutoBuild() == EJBCommon.FALSE && arInvoiceLineItem.getInvItemLocation().getInvItem().getIiNonInventoriable() == EJBCommon.FALSE) {

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

                    }


                }
            }

        }
        catch (GlobalInventoryDateException | GlobalBranchAccountNumberInvalidException ex) {

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
                           double CST_RMNNG_QTY, double CST_RMNNG_VL, double CST_VRNC_VL, String USR_NM, Integer AD_BRNCH, Integer companyCode) throws
            AdPRFCoaGlVarianceAccountNotFoundException,
            GlobalExpiryDateNotFoundException {

        Debug.print("ArMiscReceiptSyncControllerBean postToInv");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
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
                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            }
            catch (FinderException ex) {

                CST_LN_NMBR = 1;

            }

            //void subsequent cost variance adjustments
            Collection invAdjustmentLines = invAdjustmentLineHome.findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(
                    CST_DT, invItemLocation.getIlCode(), AD_BRNCH, companyCode);
            Iterator i = invAdjustmentLines.iterator();

            while (i.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();
                this.voidInvAdjustment(invAdjustmentLine.getInvAdjustment(), AD_BRNCH, companyCode);

            }

            String prevExpiryDates = "";
            double qtyPrpgt = 0;
            try {
                LocalInvCosting prevCst = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndRemainingQuantityNotEqualToZeroAndIlCode(
                        CST_DT, invItemLocation.getIlCode(), AD_BRNCH, companyCode);
                Debug.print("ArReceiptPostControllerBean postToInv B");
                prevExpiryDates = prevCst.getCstExpiryDate();
                qtyPrpgt = prevCst.getCstRemainingQuantity();

                if (prevExpiryDates == null) {
                    prevExpiryDates = "";
                }

            }
            catch (Exception ex) {
                prevExpiryDates = "";
            }

            // create costing
            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d, 0d, 0d, 0d, 0d, CST_QTY_SLD, CST_CST_OF_SLS, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, 0d, AD_BRNCH, companyCode);
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
                        StringBuilder ret = new StringBuilder();
                        StringBuilder exp = new StringBuilder();
                        String Checker = "";

                        Iterator mi = miscList.iterator();

                        propagateMiscPrpgt = prevExpiryDates;
                        ret = new StringBuilder(propagateMiscPrpgt);
                        while (mi.hasNext()) {
                            String miscStr = (String) mi.next();

                            Integer qTest = this.checkExpiryDates(ret + "fin$");
                            ArrayList miscList2 = this.expiryDates("$" + ret, Double.parseDouble(qTest.toString()));

                            //ArrayList miscList2 = this.expiryDates("$" + ret, qtyPrpgt);
                            Iterator m2 = miscList2.iterator();
                            ret = new StringBuilder();
                            String ret2 = "false";
                            int a = 0;
                            while (m2.hasNext()) {
                                String miscStr2 = (String) m2.next();

                                if (ret2.equals("1st")) {
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
                                    if ((!ret2.equals("1st")) && ((ret2.equals("false")) || (ret2.equals("true")))) {
                                        if (!miscStr2.equals("")) {
                                            miscStr2 = "$" + miscStr2;
                                            ret.append(miscStr2);
                                            ret2 = "false";
                                        }
                                    }
                                }

                            }
                            if (!ret.toString().equals("")) {
                                ret.append("$");
                            }
                            exp.append(ret);
                            qtyPrpgt = qtyPrpgt - 1;
                        }

                        propagateMiscPrpgt = ret.toString();
                        if (!Checker.equals("true")) {
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
                        USR_NM, AD_BRNCH, companyCode);
            }

            // propagate balance if necessary
            Collection invCostings = invCostingHome
                    .findByGreaterThanCstDateAndIiNameAndLocName(CST_DT,
                            invItemLocation.getInvItem().getIiName(),
                            invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
            String miscList = "";
            ArrayList miscList2 = null;
            i = invCostings.iterator();
            String propagateMisc = "";
            StringBuilder ret;
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

                        if (arInvoiceLineItem.getIliQuantity() < 0) {
                            Iterator mi = miscList2.iterator();

                            propagateMisc = invPropagatedCosting.getCstExpiryDate();
                            ret = new StringBuilder(invPropagatedCosting.getCstExpiryDate());
                            while (mi.hasNext()) {
                                String miscStr = (String) mi.next();

                                Integer qTest = this.checkExpiryDates(ret + "fin$");
                                ArrayList miscList3 = this.expiryDates("$" + ret, Double.parseDouble(qTest.toString()));

                                Iterator m2 = miscList3.iterator();
                                ret = new StringBuilder();
                                String ret2 = "false";
                                int a = 0;
                                while (m2.hasNext()) {
                                    String miscStr2 = (String) m2.next();
                                    if (ret2.equals("1st")) {
                                        ret2 = "false";
                                    }
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
                                    if (!miscStr2.equals(miscStr) || a > 1) {
                                        if ((ret2 != "1st") || (ret2 == "false") || (ret2 == "true")) {
                                            if (miscStr2 != "") {
                                                miscStr2 = "$" + miscStr2;
                                                ret.append(miscStr2);
                                                ret2 = "false";
                                            }
                                        }
                                    }
                                }
                                if (!Checker2.equals("true")) {
                                    throw new GlobalExpiryDateNotFoundException(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                                }

                                ret.append("$");
                                qtyPrpgt = qtyPrpgt - 1;
                            }
                        }

                    }

                    if (arInvoiceLineItem.getIliMisc() != null
                            && !Objects.equals(arInvoiceLineItem.getIliMisc(), "")
                            && arInvoiceLineItem.getIliMisc().length() != 0) {
                        if (prevExpiryDates != null && prevExpiryDates != "" && prevExpiryDates.length() != 0) {

                            Iterator mi = miscList2.iterator();

                            propagateMisc = invPropagatedCosting.getCstExpiryDate();
                            ret = new StringBuilder(propagateMisc);
                            while (mi.hasNext()) {
                                String miscStr = (String) mi.next();

                                Integer qTest = this.checkExpiryDates(ret + "fin$");
                                ArrayList miscList3 = this.expiryDates("$" + ret, Double.parseDouble(qTest.toString()));

                                Iterator m2 = miscList3.iterator();
                                ret = new StringBuilder();
                                String ret2 = "false";
                                int a = 0;
                                while (m2.hasNext()) {
                                    String miscStr2 = (String) m2.next();

                                    if (ret2.equals("1st")) {
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
                                        if (!miscStr2.equals("")) {
                                            miscStr2 = "$" + miscStr2;
                                            ret.append(miscStr2);
                                            ret2 = "false";
                                        }
                                    }

                                }
                                ret.append("$");
                                qtyPrpgt = qtyPrpgt - 1;
                            }
                            propagateMisc = ret.toString();
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
                this.regenerateCostVariance(invCostings, invCosting, AD_BRNCH, companyCode);
            }

        }
        catch (AdPRFCoaGlVarianceAccountNotFoundException | GlobalExpiryDateNotFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM,
                                                      Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer companyCode) {

        Debug.print("ArMiscReceiptSyncControllerBean convertForeignToFunctionalCurrency");

        LocalAdCompany adCompany;

        try {

            adCompany = adCompanyHome.findByPrimaryKey(companyCode);

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
                                    CONVERSION_DATE, companyCode);

                    AMOUNT = AMOUNT * glReceiptFunctionalCurrencyRate.getFrXToUsd();

                }

                // Get set of book functional currency rate if necessary

                if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                    LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate =
                            glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().
                                    getFcCode(), CONVERSION_DATE, companyCode);

                    AMOUNT = AMOUNT / glCompanyFunctionalCurrencyRate.getFrXToUsd();

                }


            }
            catch (Exception ex) {

                throw new EJBException(ex.getMessage());

            }

        }
        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    private ArrayList expiryDates(String misc, double qty) {

        Debug.print("ArMiscReceiptSyncControllerBean expiryDates");

        String separator = "$";

        // Remove first $ character
        misc = misc.substring(1);

        // Counter
        int start = 0;
        int nextIndex = misc.indexOf(separator, start);
        int length = nextIndex - start;

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
            }
        }

        return miscList;
    }

    private String propagateExpiryDates(String misc, double qty, String reverse) {

        Debug.print("ArMiscReceiptSyncControllerBean propagateExpiryDates");

        String separator;
        if (Objects.equals(reverse, "False")) {
            separator = "$";
        } else {
            separator = " ";
        }

        // Remove first $ character
        misc = misc.substring(1);

        // Counter
        int start = 0;
        int nextIndex = misc.indexOf(separator, start);
        int length;

        StringBuilder miscList = new StringBuilder();

        for (int x = 0; x < qty; x++) {

            // Date
            start = nextIndex + 1;
            nextIndex = misc.indexOf(separator, start);
            length = nextIndex - start;
            String g = misc.substring(start, start + length);
            if (g.length() != 0) {
                miscList.append("$").append(g);
            }
        }

        miscList.append("$");
        return (miscList.toString());
    }

    private String getQuantityExpiryDates(String qntty) {

        Debug.print("ArMiscReceiptSyncControllerBean getQuantityExpiryDates");

        String separator = "$";
        String y;
        try {
//     		Remove first $ character
            qntty = qntty.substring(1);

            // Counter
            int start = 0;
            int nextIndex = qntty.indexOf(separator, start);
            int length = nextIndex - start;

            y = (qntty.substring(start, start + length));
        }
        catch (Exception e) {
            y = "0";
        }
        return y;
    }

    private void generateCostVariance(LocalInvItemLocation invItemLocation, double CST_VRNC_VL, String ADJ_RFRNC_NMBR,
                                      String ADJ_DSCRPTN, Date ADJ_DT, String USR_NM, Integer AD_BRNCH,
                                      Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {
     	//TODO: Review the proper calculation
    }

    private void regenerateCostVariance(Collection invCostings, LocalInvCosting invCosting, Integer AD_BRNCH, Integer companyCode)
            throws AdPRFCoaGlVarianceAccountNotFoundException {
        //TODO: Review the proper calculation
    }

    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue,
                          LocalGlChartOfAccount glChartOfAccount,
                          boolean isCurrentAcv, byte isDebit, double JL_AMNT, Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean postToGl 1");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccountBalance glChartOfAccountBalance =
                    glChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(
                            glAccountingCalendarValue.getAcvCode(),
                            glChartOfAccount.getCoaCode(), companyCode);

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

    private void voidInvAdjustment(LocalInvAdjustment invAdjustment, Integer AD_BRNCH, Integer companyCode) {

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
                        invDistributionRecord.getInvChartOfAccount().getCoaCode(), invAdjustment, AD_BRNCH, companyCode);
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
                        invAdjustment, (invAdjustmentLine.getAlUnitCost()) * -1, EJBCommon.TRUE, companyCode);
            }
            invAdjustment.setAdjVoid(EJBCommon.TRUE);
            this.executeInvAdjPost(invAdjustment.getAdjCode(), invAdjustment.getAdjLastModifiedBy(), AD_BRNCH, companyCode);
        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            //getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }
    }

    private void addInvDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, byte DR_RVRSL, Integer COA_CODE,
                               LocalInvAdjustment invAdjustment, Integer AD_BRNCH, Integer companyCode)

            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArMiscReceiptEntryControllerBean addInvDrEntry");


        try {
            // get company
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate coa
            LocalGlChartOfAccount glChartOfAccount;
            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, AD_BRNCH, companyCode);

            }
            catch (FinderException ex) {

                throw new GlobalBranchAccountNumberInvalidException();

            }

            // create distribution record
            LocalInvDistributionRecord invDistributionRecord = invDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT,
                    EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), DR_RVRSL, EJBCommon.FALSE,
                    companyCode);

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
                                                 double CST_VRNC_VL, byte AL_VD, Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean addInvAlEntry");

        try {

            // create dr entry
            LocalInvAdjustmentLine invAdjustmentLine;
            invAdjustmentLine = invAdjustmentLineHome.create(CST_VRNC_VL, null, null, 0, 0, AL_VD, companyCode);

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

    private void executeInvAdjPost(Integer ADJ_CODE, String USR_NM, Integer AD_BRNCH, Integer companyCode) throws
            GlobalRecordAlreadyDeletedException,
            GlobalTransactionAlreadyPostedException,
            GlJREffectiveDateNoPeriodExistException,
            GlJREffectiveDatePeriodClosedException,
            GlobalJournalNotBalanceException {

        Debug.print("ArMiscReceiptEntryControllerBean executeInvAdjPost");

        try {

            // validate if adjustment is already deleted
            LocalInvAdjustment invAdjustment;
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

            Collection invAdjustmentLines;
            if (invAdjustment.getAdjVoid() == EJBCommon.FALSE) {
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.FALSE, invAdjustment.getAdjCode(), companyCode);
            } else {
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.TRUE, invAdjustment.getAdjCode(), companyCode);
            }

            Iterator i = invAdjustmentLines.iterator();
            while (i.hasNext()) {


                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();

                LocalInvCosting invCosting =
                        invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                invAdjustment.getAdjDate(), invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(),
                                invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName(), AD_BRNCH, companyCode);

                this.postInvAdjustmentToInventory(invAdjustmentLine, invAdjustment.getAdjDate(), 0,
                        invAdjustmentLine.getAlUnitCost(), invCosting.getCstRemainingQuantity(),
                        invCosting.getCstRemainingValue() + invAdjustmentLine.getAlUnitCost(), AD_BRNCH, companyCode);

            }

            // post to gl if necessary
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate if date has no period and period is closed
            LocalGlSetOfBook glJournalSetOfBook;
            try {

                glJournalSetOfBook = glSetOfBookHome.findByDate(invAdjustment.getAdjDate(), companyCode);

            }
            catch (FinderException ex) {

                throw new GlJREffectiveDateNoPeriodExistException();

            }

            LocalGlAccountingCalendarValue glAccountingCalendarValue =
                    glAccountingCalendarValueHome.findByAcCodeAndDate(
                            glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), invAdjustment.getAdjDate(), companyCode);


            if (glAccountingCalendarValue.getAcvStatus() == 'N' ||
                    glAccountingCalendarValue.getAcvStatus() == 'C' ||
                    glAccountingCalendarValue.getAcvStatus() == 'P') {

                throw new GlJREffectiveDatePeriodClosedException();

            }

            // check if invoice is balance if not check suspense posting
            LocalGlJournalLine glOffsetJournalLine = null;
            Collection invDistributionRecords;
            if (invAdjustment.getAdjVoid() == EJBCommon.FALSE) {

                invDistributionRecords = invDistributionRecordHome.findImportableDrByDrReversedAndAdjCode(EJBCommon.FALSE,
                        invAdjustment.getAdjCode(), companyCode);

            } else {

                invDistributionRecords = invDistributionRecordHome.findImportableDrByDrReversedAndAdjCode(EJBCommon.TRUE,
                        invAdjustment.getAdjCode(), companyCode);

            }

            Iterator j = invDistributionRecords.iterator();

            double TOTAL_DEBIT = 0d;
            double TOTAL_CREDIT = 0d;

            while (j.hasNext()) {
                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) j.next();
                double DR_AMNT = invDistributionRecord.getDrAmount();
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
                            companyCode);

                }
                catch (FinderException ex) {

                    throw new GlobalJournalNotBalanceException();

                }

                if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                    glOffsetJournalLine = glJournalLineHome.create((short) (invDistributionRecords.size() + 1), EJBCommon.TRUE,
                            TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);

                } else {

                    glOffsetJournalLine = glJournalLineHome.create((short) (invDistributionRecords.size() + 1), EJBCommon.FALSE,
                            TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);

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
                        " INVENTORY ADJUSTMENTS", AD_BRNCH, companyCode);

            }
            catch (FinderException ex) {
                Debug.print("Finder Exception : " + ex.getMessage());
            }

            if (glJournalBatch == null) {

                glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) +
                                " INVENTORY ADJUSTMENTS", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(),
                        USR_NM, AD_BRNCH, companyCode);

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

                    AD_BRNCH, companyCode);

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
                LocalGlJournalLine glJournalLine = glJournalLineHome.create(invDistributionRecord.getDrLine(),
                        invDistributionRecord.getDrDebit(), invDistributionRecord.getDrAmount(), "", companyCode);
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
                        true, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                // post to subsequent acvs (propagate)
                Collection glSubsequentAccountingCalendarValues =
                        glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(
                                glJournalSetOfBook.getGlAccountingCalendar().getAcCode(),
                                glAccountingCalendarValue.getAcvPeriodNumber(), companyCode);

                for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {
                    LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue =
                            (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;
                    this.postToGl(glSubsequentAccountingCalendarValue,
                            glJournalLine.getGlChartOfAccount(),
                            false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                }

                // post to subsequent years if necessary
                Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(
                        glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode);
                if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                    adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                    LocalGlChartOfAccount glRetainedEarningsAccount =
                            glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(adCompany.getCmpRetainedEarnings(),
                                    AD_BRNCH, companyCode);

                    for (Object subsequentSetOfBook : glSubsequentSetOfBooks) {
                        LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) subsequentSetOfBook;
                        String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();
                        // post to subsequent acvs of subsequent set of book(propagate)
                        Collection glAccountingCalendarValues =
                                glAccountingCalendarValueHome.findByAcCode(
                                        glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);
                        for (Object accountingCalendarValue : glAccountingCalendarValues) {
                            LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue =
                                    (LocalGlAccountingCalendarValue) accountingCalendarValue;
                            if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY") ||
                                    ACCOUNT_TYPE.equals("OWNERS EQUITY")) {

                                this.postToGl(glSubsequentAccountingCalendarValue,
                                        glJournalLine.getGlChartOfAccount(),
                                        false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                            } else { // revenue & expense

                                this.postToGl(glSubsequentAccountingCalendarValue,
                                        glRetainedEarningsAccount,
                                        false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

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
        catch (GlJREffectiveDateNoPeriodExistException | GlJREffectiveDatePeriodClosedException |
               GlobalJournalNotBalanceException | GlobalRecordAlreadyDeletedException |
               GlobalTransactionAlreadyPostedException ex) {

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
                                              double CST_ADJST_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, Integer AD_BRNCH, Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean postInvAdjustmentToInventory");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalInvItemLocation invItemLocation = invAdjustmentLine.getInvItemLocation();

            int CST_LN_NMBR;

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
                        AD_BRNCH, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            }
            catch (FinderException ex) {

                CST_LN_NMBR = 1;

            }

            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d, 0d, 0d,
                    CST_ADJST_QTY, CST_ADJST_CST, 0d, 0d, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, CST_ADJST_QTY > 0 ? CST_ADJST_QTY : 0, AD_BRNCH, companyCode);
            //invItemLocation.addInvCosting(invCosting);
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setInvAdjustmentLine(invAdjustmentLine);

            // propagate balance if necessary
            Collection invCostings = invCostingHome
                    .findByGreaterThanCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(),
                            invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
            for (Object costing : invCostings) {

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) costing;

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

    private ArrayList<InvModTagListDetails> convertMiscToInvModTagList(String misc) {
        ArrayList<InvModTagListDetails> list = new ArrayList<>();
        String[] imei_array = misc.split("\\$");
        for (String s : imei_array) {
            if (s.trim().equals("")) {
                continue;
            }
            InvModTagListDetails details = new InvModTagListDetails();
            details.setTgPropertyCode("");
            details.setTgSpecs("");
            details.setTgTransactionDate(new Date());
            details.setTgSerialNumber(s);
            list.add(details);
        }
        return list;
    }

    private double getFrRateByFrNameAndFrDate(String FC_NM, Date CONVERSION_DATE, Integer companyCode)
            throws GlobalConversionDateNotExistException {

        Debug.print("ArMiscReceiptEntryControllerBean getFrRateByFrNameAndFrDate");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, companyCode);

            double CONVERSION_RATE = 1;

            // Get functional currency rate

            if (!FC_NM.equals("USD")) {

                LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate =
                        glFunctionalCurrencyRateHome.findByFcCodeAndDate(glFunctionalCurrency.getFcCode(),
                                CONVERSION_DATE, companyCode);

                CONVERSION_RATE = glFunctionalCurrencyRate.getFrXToUsd();

            }

            // Get set of book functional currency rate if necessary

            if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate =
                        glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(),
                                CONVERSION_DATE, companyCode);

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

    private int checkExpiryDates(String misc) {

        String separator = "$";

        // Remove first $ character
        misc = misc.substring(1);
        // Counter
        int start = 0;
        int nextIndex = misc.indexOf(separator, start);
        int length;
        int numberExpry = 0;
        StringBuilder miscList = new StringBuilder();
        String miscList2 = "";
        String g = "";
        try {
            while (!g.equals("fin")) {
                // Date
                start = nextIndex + 1;
                nextIndex = misc.indexOf(separator, start);
                length = nextIndex - start;
                g = misc.substring(start, start + length);
                if (g.length() != 0) {
                    if (g.contains("null")) {
                        miscList2 = "Error";
                    } else {
                        miscList.append("$").append(g);
                        numberExpry++;
                    }

                } else {
                    miscList2 = "Error";
                }
            }
        }
        catch (Exception ex) {
            throw ex;
        }

        if (miscList2.equals("")) {
            miscList.append("$");
        } else {
            miscList = new StringBuilder(miscList2);
        }

        return (numberExpry);
    }


}