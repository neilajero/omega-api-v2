package com.ejb.txnreports.ap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;


import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.entities.ad.LocalAdApproval;
import com.ejb.entities.ad.LocalAdApprovalDocument;
import com.ejb.dao.ad.LocalAdApprovalDocumentHome;
import com.ejb.dao.ad.LocalAdApprovalHome;
import com.ejb.entities.ad.LocalAdApprovalQueue;
import com.ejb.dao.ad.LocalAdApprovalQueueHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ad.LocalAdUser;
import com.ejb.dao.ad.LocalAdUserHome;
import com.ejb.entities.ad.LocalAdUserResponsibility;
import com.ejb.dao.ad.LocalAdUserResponsibilityHome;
import com.ejb.entities.ap.LocalApPurchaseOrder;
import com.ejb.dao.ap.LocalApPurchaseOrderHome;
import com.ejb.entities.ap.LocalApPurchaseOrderLine;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.inv.LocalInvCosting;
import com.ejb.dao.inv.LocalInvCostingHome;
import com.ejb.dao.inv.LocalInvItemHome;
import com.ejb.entities.inv.LocalInvTag;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ap.ApRepPurchaseOrderPrintDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ApRepPurchaseOrderPrintControllerEJB")
public class ApRepPurchaseOrderPrintControllerBean extends EJBContextClass implements ApRepPurchaseOrderPrintController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalAdApprovalDocumentHome adApprovalDocumentHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalAdUserResponsibilityHome adUserResponsibilityHome;
    @EJB
    private LocalApPurchaseOrderHome apPurchaseOrderHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalInvItemHome invItemHome;


    public ArrayList executeApRepPurchaseOrderPrint(ArrayList poCodeList, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepPurchaseOrderPrintControllerBean executeApRepPurchaseOrderPrint");

        ArrayList list = new ArrayList();

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            for (Object value : poCodeList) {

                Integer PO_CODE = (Integer) value;

                LocalApPurchaseOrder apPurchaseOrder = null;

                try {

                    apPurchaseOrder = apPurchaseOrderHome.findByPrimaryKey(PO_CODE);

                } catch (FinderException ex) {

                    continue;
                }

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(AD_CMPNY);

                LocalAdApprovalDocument adApprovalDocument = adApprovalDocumentHome.findByAdcType("AP PURCHASE ORDER", AD_CMPNY);

                if (adApprovalDocument.getAdcPrintOption().equals("PRINT APPROVED ONLY")) {

                    if (apPurchaseOrder.getPoApprovalStatus() == null || apPurchaseOrder.getPoApprovalStatus().equals("PENDING")) {

                        continue;
                    }

                } else if (adApprovalDocument.getAdcPrintOption().equals("PRINT UNAPPROVED ONLY")) {

                    if (apPurchaseOrder.getPoApprovalStatus() != null && (apPurchaseOrder.getPoApprovalStatus().equals("N/A") || apPurchaseOrder.getPoApprovalStatus().equals("APPROVED"))) {

                        continue;
                    }
                }

                if (adApprovalDocument.getAdcAllowDuplicate() == EJBCommon.FALSE && apPurchaseOrder.getPoPrinted() == EJBCommon.TRUE) {

                    continue;
                }

                // set printed

                apPurchaseOrder.setPoPrinted(EJBCommon.TRUE);

                // get purchase order lines

                Collection apPurchaseOrderLines = apPurchaseOrder.getApPurchaseOrderLines();

                Iterator ilIter = apPurchaseOrderLines.iterator();

                double totalAmount = 0;
                while (ilIter.hasNext()) {
                    LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) ilIter.next();
                    totalAmount += apPurchaseOrderLine.getPlAmount();
                }
                ilIter = apPurchaseOrderLines.iterator();
                while (ilIter.hasNext()) {

                    LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) ilIter.next();

                    String II_NM = apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiName();
                    String LOC_NM = apPurchaseOrderLine.getInvItemLocation().getInvLocation().getLocName();
                    boolean isService = apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiAdLvCategory().startsWith("Service");
                    ApRepPurchaseOrderPrintDetails details = new ApRepPurchaseOrderPrintDetails();
                    details.setPopPoDate(apPurchaseOrder.getPoDate());
                    details.setPopPoDeliveryPeriod(apPurchaseOrder.getPoDeliveryPeriod());
                    details.setPopPoSupplierName(apPurchaseOrder.getApSupplier().getSplName());

                    details.setPopPlIsService(isService);
                    details.setPopPoContactPerson(apPurchaseOrder.getApSupplier().getSplContact());
                    details.setPopPoContactNumber(apPurchaseOrder.getApSupplier().getSplPhone());
                    details.setPopPoDocumentNumber(apPurchaseOrder.getPoDocumentNumber());
                    details.setPopPoReferenceNumber(apPurchaseOrder.getPoReferenceNumber());

                    details.setPopPoCreatedBy(apPurchaseOrder.getPoCreatedBy());
                    LocalAdUser adUser = null;

                    try {
                        adUser = adUserHome.findByUsrName(apPurchaseOrder.getPoCreatedBy(), AD_CMPNY);
                        String desc = adUser.getUsrDescription();
                    } catch (Exception e) {
                    }

                    details.setPopDepartment(apPurchaseOrder.getPoDepartment());

                    details.setPopPoShipTo(apPurchaseOrder.getPoShipTo());
                    details.setPopPoPaymentTermName(apPurchaseOrder.getAdPaymentTerm().getPytName());
                    details.setPopPoApprovalStatus(apPurchaseOrder.getPoApprovalStatus());

                    if (adUser != null) {
                        try {
                            LocalAdUser user1 = null;
                            try {
                                user1 = adUserHome.findByUsrName(apPurchaseOrder.getPoApprovedRejectedBy(), AD_CMPNY);
                            } catch (FinderException ex) {
                            }
                            details.setPopPlApprovedRejectedBy(apPurchaseOrder.getPoApprovedRejectedBy().substring(0, 2) + apPurchaseOrder.getPoDocumentNumber().substring(2) + "-" + user1.getUsrPurchaseOrderApprovalCounter());
                            Collection userResponsibility = adUserResponsibilityHome.findByUsrCode(adUser.getUsrCode(), AD_CMPNY);
                            for (Object o : userResponsibility) {
                                LocalAdUserResponsibility rs = (LocalAdUserResponsibility) o;
                                Debug.print("rs d2: " + rs.getAdResponsibility().getRsName());
                                details.setPopPoUserResponsibility(rs.getAdResponsibility().getRsName());
                            }
                        } catch (Exception ex) {

                        }

                    } else {
                        details.setPopPlApprovedRejectedBy("");
                        details.setPopPoUserResponsibility("");
                    }

                    if (apPurchaseOrder.getPoApprovedRejectedBy() == null || apPurchaseOrder.getPoApprovedRejectedBy().equals("")) {
                        // details.setPopPoApprovedRejectedBy(adPreference.getPrfApDefaultApprover());

                    } else {
                        // details.setPopPoApprovedRejectedBy(apPurchaseOrder.getPoApprovedRejectedBy());

                    }

                    details.setPopPoCheckedBy(adPreference.getPrfApDefaultChecker());
                    details.setPopPoDescription(apPurchaseOrder.getPoDescription());
                    details.setPopPllIiName(II_NM);
                    details.setPopPlIiDescription(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiDescription());

                    details.setPopPlIiPartNumber(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiPartNumber());
                    details.setPopPlIiBarCode1(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiBarCode1());
                    details.setPopPlIiBarCode2(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiBarCode2());
                    details.setPopPlIiBarCode3(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiBarCode3());
                    details.setPopPlIiBrand(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiBrand());

                    details.setPopPlLocName(LOC_NM);
                    details.setPopPlUomName(apPurchaseOrderLine.getInvUnitOfMeasure().getUomName());
                    details.setPopPlQuantity(apPurchaseOrderLine.getPlQuantity());
                    details.setPopPlUnitCost(apPurchaseOrderLine.getPlUnitCost());
                    details.setPopPlAmount(apPurchaseOrderLine.getPlAmount());
                    details.setPopPlTotalAmount(totalAmount);

                    // details.setPopPlBranchName(apPurchaseOrder.getPoAdBranch().toString());

                    //        			Include Branch
                    LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(apPurchaseOrder.getPoAdBranch());
                    details.setPopPlBranchCode(adBranch.getBrBranchCode());
                    details.setPopPlBranchName(adBranch.getBrName());

                    try {

                        LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndIiNameAndLocName(II_NM, LOC_NM, AD_BRNCH, AD_CMPNY);
                        details.setPopPlEndingBalance(invCosting.getCstRemainingValue() + apPurchaseOrderLine.getPlAmount());

                    } catch (FinderException ex) {
                        details.setPopPlEndingBalance(apPurchaseOrderLine.getPlAmount());
                    }

                    // get discount
                    String discount = "";

                    if (apPurchaseOrderLine.getPlDiscount1() != 0) discount = "" + apPurchaseOrderLine.getPlDiscount1();

                    if (apPurchaseOrderLine.getPlDiscount2() != 0)
                        discount = discount + (!discount.equals("") ? "," : "") + apPurchaseOrderLine.getPlDiscount2();

                    if (apPurchaseOrderLine.getPlDiscount3() != 0)
                        discount = discount + (!discount.endsWith(",") && !discount.equals("") ? "," : "") + apPurchaseOrderLine.getPlDiscount3();

                    if (apPurchaseOrderLine.getPlDiscount4() != 0)
                        discount = discount + (!discount.endsWith(",") && !discount.equals("") ? "," : "") + apPurchaseOrderLine.getPlDiscount4();

                    discount = discount.replaceAll(".0,", ",");

                    if (discount.endsWith(",")) discount = discount.substring(0, discount.length() - 1);

                    if (discount.endsWith(".0")) discount = discount.substring(0, discount.length() - 2);

                    details.setPopPlDiscount(discount);
                    details.setPopPlDiscount1(apPurchaseOrderLine.getPlDiscount1());
                    details.setPopPlDiscount2(apPurchaseOrderLine.getPlDiscount2());
                    details.setPopPlDiscount3(apPurchaseOrderLine.getPlDiscount3());
                    details.setPopPlDiscount4(apPurchaseOrderLine.getPlDiscount4());
                    details.setPopPlTotalDiscount(apPurchaseOrderLine.getPlTotalDiscount());
                    details.setPopPoBillTo(apPurchaseOrder.getPoBillTo());
                    details.setPopPlUnitOfMeasureShortName(apPurchaseOrderLine.getInvUnitOfMeasure().getUomShortName());
                    details.setPopPoTaxRate(apPurchaseOrder.getApTaxCode().getTcRate());

                    // get Tax Amount
                    double NT_AMNT = calculatePlNetAmount(details, apPurchaseOrder.getApTaxCode().getTcRate(), apPurchaseOrder.getApTaxCode().getTcType(), this.getGlFcPrecisionUnit(AD_CMPNY));

                    details.setPopPlTaxAmount(calculatePlTaxAmount(details, apPurchaseOrder.getApTaxCode().getTcRate(), apPurchaseOrder.getApTaxCode().getTcType(), NT_AMNT, this.getGlFcPrecisionUnit(AD_CMPNY)));

                    // get unit cost wo vat
                    details.setPopPlUnitCostWoTax(calculatePlNetUnitCost(details, apPurchaseOrder.getApTaxCode().getTcRate(), apPurchaseOrder.getApTaxCode().getTcType(), this.getGlFcPrecisionUnit(AD_CMPNY)));

                    // get unit cost vat inclusive
                    details.setPopPlUnitCostTaxInclusive(calculatePlUnitCostTaxInclusive(details, apPurchaseOrder.getApTaxCode().getTcRate(), apPurchaseOrder.getApTaxCode().getTcType(), this.getGlFcPrecisionUnit(AD_CMPNY)));

                    details.setPopPoTaxType(apPurchaseOrder.getApTaxCode().getTcType());

                    details.setPopPoCurrency(apPurchaseOrder.getGlFunctionalCurrency().getFcName());

                    details.setPopPoSupplierAddress(apPurchaseOrder.getApSupplier().getSplAddress());
                    details.setPopPoSupplierCode(apPurchaseOrder.getApSupplier().getSplSupplierCode());
                    details.setPopPlSupplierPhoneNumber(apPurchaseOrder.getApSupplier().getSplPhone());
                    details.setPopPlSupplierFaxNumber(apPurchaseOrder.getApSupplier().getSplFax());
                    details.setPopPoPosted(apPurchaseOrder.getPoPosted());

                    // trace misc
                    if (apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiTraceMisc() == EJBCommon.TRUE) {

                        if (apPurchaseOrderLine.getInvTags().size() > 0) {
                            Debug.print("new code");
                            StringBuilder strBProperty = new StringBuilder();
                            StringBuilder strBSerial = new StringBuilder();
                            StringBuilder strBSpecs = new StringBuilder();
                            StringBuilder strBCustodian = new StringBuilder();
                            StringBuilder strBExpirationDate = new StringBuilder();

                            for (Object o : apPurchaseOrderLine.getInvTags()) {

                                LocalInvTag invTag = (LocalInvTag) o;

                                // property code
                                if (!invTag.getTgPropertyCode().equals("")) {
                                    strBProperty.append(invTag.getTgPropertyCode());
                                    strBProperty.append(System.getProperty("line.separator"));
                                }

                                // serial

                                if (!invTag.getTgSerialNumber().equals("")) {
                                    strBSerial.append(invTag.getTgSerialNumber());
                                    strBSerial.append(System.getProperty("line.separator"));
                                }

                                // spec

                                if (!invTag.getTgSpecs().equals("")) {
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
                            details.setPopPlPropertyCode(strBProperty.toString());
                            // serial number
                            details.setPopPlSerialNumber(strBSerial.toString());
                            // specs
                            details.setPopPlSpecs(strBSpecs.toString());
                            // custodian
                            details.setPopPlCustodian(strBCustodian.toString());
                            // expiration date
                            details.setPopPlExpiryDate(strBExpirationDate.toString());
                        }
                    }

                    details.setPopPlRemarks(apPurchaseOrderLine.getPlRemarks());


                    //get approver list that approved the document
                    StringBuilder strBldr = new StringBuilder();
                    Collection adApprovalQueues = adApprovalQueueHome.findAllByAqDocumentAndAqDocumentCode("AP PURCHASE ORDER", apPurchaseOrderLine.getApPurchaseOrder().getPoCode(), AD_CMPNY);

                    for (Object approvalQueue : adApprovalQueues) {

                        LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) approvalQueue;

                        if (adApprovalQueue.getAqApproved() == EJBCommon.FALSE) continue;

                        strBldr.append(adApprovalQueue.getAdUser().getUsrName());
                        strBldr.append(System.getProperty("line.separator"));

                    }

                    details.setPopPoApprovedBy(strBldr.toString());


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

        Debug.print("ApRepPurchaseOrderPrintControllerBean getAdCompany");

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
            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods

    private double calculatePlNetUnitCost(ApRepPurchaseOrderPrintDetails mdetails, double tcRate, String tcType, short precisionUnit) {

        Debug.print("ApRepPurchaseOrderPrintControllerBean calculatePlNetUnitCost");

        double amount = mdetails.getPopPlUnitCost();

        if (tcType.equals("INCLUSIVE")) {

            amount = EJBCommon.roundIt(mdetails.getPopPlUnitCost() / (1 + (tcRate / 100)), precisionUnit);
        }

        return amount;
    }

    private short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ApRepPurchaseOrderPrintControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private double calculatePlTaxAmount(ApRepPurchaseOrderPrintDetails mdetails, double tcRate, String tcType, double amount, short precisionUnit) {

        Debug.print("ApRepPurchaseOrderPrintControllerBean calculatePlTaxAmount");

        double taxAmount = 0d;

        if (!tcType.equals("NONE") && !tcType.equals("EXEMPT")) {

            if (tcType.equals("INCLUSIVE")) {

                taxAmount = EJBCommon.roundIt(mdetails.getPopPlAmount() - amount, precisionUnit);

            } else if (tcType.equals("EXCLUSIVE")) {

                taxAmount = EJBCommon.roundIt(mdetails.getPopPlAmount() * tcRate / 100, precisionUnit);

            } else {

                // tax none zero-rated or exempt

            }
        }

        return taxAmount;
    }

    private double calculatePlNetAmount(ApRepPurchaseOrderPrintDetails mdetails, double tcRate, String tcType, short precisionUnit) {

        Debug.print("ApRepPurchaseOrderPrintControllerBean calculatePlNetAmount");

        double amount = 0d;

        if (tcType.equals("INCLUSIVE")) {

            amount = EJBCommon.roundIt(mdetails.getPopPlAmount() / (1 + (tcRate / 100)), precisionUnit);

        } else {

            // tax exclusive, none, zero rated or exempt

            amount = mdetails.getPopPlAmount();
        }

        return amount;
    }

    private double calculatePlUnitCostTaxInclusive(ApRepPurchaseOrderPrintDetails mdetails, double tcRate, String tcType, short precisionUnit) {

        Debug.print("ApRepPurchaseOrderPrintControllerBean calculatePlUnitCostTaxInclusive");

        double amount = mdetails.getPopPlUnitCost();

        if (tcType.equals("EXCLUSIVE")) {

            amount = EJBCommon.roundIt(mdetails.getPopPlUnitCost() * (1 + (tcRate / 100)), precisionUnit);
        }

        return amount;
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApRepPurchaseOrderPrintControllerBean ejbCreate");
    }
}