package com.ejb.txnsync.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ar.*;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ar.*;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.entities.inv.*;
import com.ejb.exception.global.GlobalBranchAccountNumberInvalidException;
import com.ejb.restfulapi.sync.ar.models.ArInvoiceSyncResponse;
import com.ejb.restfulapi.sync.ar.models.ArMiscReceiptSyncRequest;
import com.ejb.restfulapi.sync.ar.models.ArMiscReceiptSyncResponse;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBCommonAPIErrCodes;
import com.util.EJBContextClass;
import com.util.mod.ar.ArModInvoiceLineItemDetails;
import com.util.mod.ar.ArModReceiptDetails;
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

    @Override
    public int setArReceiptAllNewAndVoid(String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier) {

        Debug.print("ArMiscReceiptSyncControllerBean setArReceiptAllNewAndVoid");
//        try {
//
//            ejbRCT = homeRCT.create();
//
//        } catch(Exception e) {}
//
//        int success = 0;


        //ejbRCT.saveArRctEntry(details, BA_NM, FC_NM, CST_CSTMR_CODE, RB_NM, aiList, isDraft, branchCode, companyCode)


        //return success;

        return 0;
    }

    @Override
    public int setArMiscReceiptAllNewAndVoid(String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier) {

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
                            adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode);
                        }
                        catch (FinderException ex) {
                        }

                        while (true) {
                            if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {
                                try {
                                    arReceiptHome.findByRctNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), branchCode, companyCode);
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                }
                                catch (FinderException ex) {
                                    generatedReceipt = adDocumentSequenceAssignment.getDsaNextSequence();
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                    break;
                                }
                            } else {
                                try {
                                    arReceiptHome.findByRctNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), branchCode, companyCode);
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
                            arModUploadReceiptDetails.setRctPosDiscount(EJBCommon.roundIt(arModReceiptDetails.getRctPosDiscount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosScAmount(EJBCommon.roundIt(arModReceiptDetails.getRctPosScAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            arModUploadReceiptDetails.setRctPosDcAmount(EJBCommon.roundIt(arModReceiptDetails.getRctPosDcAmount() * arModReceiptDetails.getRctConversionRate(), (short) 2));
                            receiptNumbers.put(generatedReceipt, arModUploadReceiptDetails);

                            double totalAmount = EJBCommon.roundIt((arModReceiptDetails.getRctPosTotalAmount() - arModReceiptDetails.getRctPosVoidAmount()) * arModReceiptDetails.getRctConversionRate(), (short) 2);

                            LocalArReceipt arReceipt = arReceiptHome.create("MISC", "POS Sales " + new Date(), arModReceiptDetails.getRctDate(), generatedReceipt, null, null, null, null, null, null, null, null, totalAmount, 0d, 0d, 0d, 0d, 0d, 0d, null, 1, null, "CASH", EJBCommon.FALSE, 0, null, null, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, null, null, null, null, null, cashier, EJBCommon.getGcCurrentDateWoTime().getTime(), cashier, EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, null, null, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, null, null, EJBCommon.FALSE, EJBCommon.FALSE, null, branchCode, companyCode);

                            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
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
                                LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.create(++lineNumber, arModInvoiceLineItemDetails.getIliQuantity(), arModInvoiceLineItemDetails.getIliUnitPrice(), EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() / (1 + arReceipt.getArTaxCode().getTcRate() / 100), (short) 2), EJBCommon.roundIt(arModInvoiceLineItemDetails.getIliAmount() - (arModInvoiceLineItemDetails.getIliAmount() / (1 + arReceipt.getArTaxCode().getTcRate() / 100)), (short) 2), EJBCommon.FALSE, 0, 0, 0, 0, 0, EJBCommon.TRUE, companyCode);

                                //arReceipt.addArInvoiceLineItem(arInvoiceLineItem);
                                arInvoiceLineItem.setArReceipt(arReceipt);

                                LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(arModInvoiceLineItemDetails.getIliUomName(), companyCode);
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
    public int setArMiscReceiptAllNewAndVoidUS(String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier) {

        return 0;
    }

    @Override
    public int setArMiscReceiptAllNewAndVoidWithExpiryDate(String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier) {

        return 0;
    }

    @Override
    public int setArMiscReceiptAllNewAndVoidWithExpiryDateEnableItemAutoBuild(String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier) {

        return 0;
    }

    @Override
    public int setArMiscReceiptAllNewAndVoidWithExpiryDateAndEntries(String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier) {

        return 0;
    }

    @Override
    public int setArMiscReceiptAllNewAndVoidWithSalesperson(String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier) {

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
            int count = this.setArMiscReceiptAllNewAndVoid(receipts, voidReceipts, branchCode, companyCode, companyShortName);

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


    private void addArDrIliEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE, LocalArReceipt arReceipt, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArMiscReceiptSyncControllerBean addArDrIliEntry");

        try {

            // get company
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(COA_CODE);

            // create distribution record
            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, EJBCommon.FALSE, AD_CMPNY);

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

    private void addArDrIliEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE, LocalArInvoice arInvoice, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArMiscReceiptSyncControllerBean addArDrIliEntry");

        try {

            // get company
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(COA_CODE);

            // create distribution record
            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, EJBCommon.FALSE, AD_CMPNY);

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

    private void addArDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE, byte DR_RVRSD, LocalArReceipt arReceipt, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArMiscReceipSyncControllerBean addArDrEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

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
                System.out.println("glChartOfAccountHome.findByCoaAccountNumber(" + newCoa + ", " + AD_CMPNY + ")");

                glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(newCoa, AD_CMPNY);

            }

            // create distribution record
            System.out.println("Create Dstrbtn Rcrd");
            System.out.println("DR_CLSS: " + DR_CLSS);
            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, DR_RVRSD, AD_CMPNY);

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

    private void addArDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE, byte DR_RVRSD, LocalArInvoice arInvoice, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArMiscReceipSyncControllerBean addArDrEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

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

                glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(newCoa, AD_CMPNY);

            }

            // create distribution record

            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, DR_RVRSD, AD_CMPNY);

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

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, double ADJST_QTY, Integer AD_CMPNY) {

        Debug.print("ArMiscReceiptSyncControllerBean convertByUomFromAndItemAndQuantity");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), AD_CMPNY);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), AD_CMPNY);

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

    private short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ArMiscReceiptSyncControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

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

}