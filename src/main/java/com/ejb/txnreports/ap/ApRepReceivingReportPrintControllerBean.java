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
import com.ejb.dao.ad.LocalAdApprovalDocumentHome;
import com.ejb.dao.ad.LocalAdApprovalHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ad.LocalAdUser;
import com.ejb.dao.ad.LocalAdUserHome;
import com.ejb.entities.ap.LocalApDistributionRecord;
import com.ejb.dao.ap.LocalApDistributionRecordHome;
import com.ejb.entities.ap.LocalApPurchaseOrder;
import com.ejb.dao.ap.LocalApPurchaseOrderHome;
import com.ejb.entities.ap.LocalApPurchaseOrderLine;
import com.ejb.dao.ap.LocalApPurchaseOrderLineHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.inv.LocalInvCosting;
import com.ejb.dao.inv.LocalInvCostingHome;
import com.ejb.entities.inv.LocalInvItem;
import com.ejb.dao.inv.LocalInvItemHome;
import com.ejb.entities.inv.LocalInvTag;
import com.ejb.dao.inv.LocalInvTagHome;
import com.util.ad.AdCompanyDetails;
import com.util.mod.ap.ApModDistributionRecordDetails;
import com.util.reports.ap.ApRepReceivingReportPrintDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.inv.InvItemDetails;

@Stateless(name = "ApRepReceivingReportPrintControllerEJB")
public class ApRepReceivingReportPrintControllerBean extends EJBContextClass implements ApRepReceivingReportPrintController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalAdApprovalDocumentHome adApprovalDocumentHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    private LocalApPurchaseOrderHome apPurchaseOrderHome;
    @EJB
    private LocalApPurchaseOrderLineHome apPurchaseOrderLineHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvTagHome invTagHome;

    public ArrayList executeApRepReceivingReportPrintSub(ArrayList poCodeList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepReceivingReportPrintControllerBean executeApRepReceivingReportPrintSub");

        ArrayList list = new ArrayList();

        try {

            for (Object o : poCodeList) {

                Integer PO_CODE = (Integer) o;

                LocalApPurchaseOrder apPurchaseOrder = null;

                try {

                    apPurchaseOrder = apPurchaseOrderHome.findByPrimaryKey(PO_CODE);

                } catch (FinderException ex) {

                    continue;
                }

                // get total vat of all receipt

                Collection apDistributionRecords = apDistributionRecordHome.findByChkCode(apPurchaseOrder.getPoCode(), AD_CMPNY);

                for (Object distributionRecord : apDistributionRecords) {

                    LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) distributionRecord;

                    ApModDistributionRecordDetails mdetails = new ApModDistributionRecordDetails();

                    mdetails.setDrCoaAccountNumber(apDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                    mdetails.setDrCoaAccountDescription(apDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());
                    mdetails.setDrDebit(apDistributionRecord.getDrDebit());
                    mdetails.setDrAmount(apDistributionRecord.getDrAmount());
                    mdetails.setDrClass(apDistributionRecord.getDrClass());
                    Debug.print("mdetails: " + mdetails.getDrCoaAccountDescription());
                    list.add(mdetails);
                }
            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList executeApRepReceivingReportPrint(ArrayList poCodeList, String USR_NM, Integer AD_BRNCH, Integer AD_CMPNY, String reportType) throws GlobalNoRecordFoundException {

        Debug.print("ApRepReceivingReportPrintControllerBean executeApRepReceivingReportPrint");

        ArrayList list = new ArrayList();

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            for (Object o : poCodeList) {

                Integer PO_CODE = (Integer) o;

                LocalApPurchaseOrder apPurchaseOrder = null;

                try {

                    apPurchaseOrder = apPurchaseOrderHome.findByPrimaryKey(PO_CODE);

                } catch (FinderException ex) {

                    continue;
                }

                String DFLT_APPRVR = null;

                // get purchase order lines

                Collection apPurchaseOrderLines = apPurchaseOrder.getApPurchaseOrderLines();

                for (Object purchaseOrderLine : apPurchaseOrderLines) {

                    LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) purchaseOrderLine;

                    String II_NM = apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiName();
                    String LOC_NM = apPurchaseOrderLine.getInvItemLocation().getInvLocation().getLocName();
                    boolean isService = apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiAdLvCategory().startsWith("Service");
                    String userDepartment = this.getAdUsrDepartment(USR_NM, AD_CMPNY);

                    if (!apPurchaseOrderLine.getInvTags().isEmpty() && reportType.equals("NormalFormat") && apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiTraceMisc() == 1) {
                        Debug.print("invTags is not empty");

                        ApRepReceivingReportPrintDetails details = new ApRepReceivingReportPrintDetails();
                        if (isService) {
                            Debug.print(isService + " <== service ba?");
                            details.setRrpPlIsService(true);
                            Debug.print(details.getRrpPlIsService() + " <== details.getRrpPlIsService");
                        } else {
                            Debug.print(isService + " <== o hindi?");
                            details.setRrpPlIsService(false);
                            Debug.print(details.getRrpPlIsService() + " <== details.getRrpPlIsService");
                        }
                        details.setRrpPlIsInventoriable(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiNonInventoriable() != 1);
                        details.setRrpPoType(apPurchaseOrder.getPoType());
                        details.setRrpPoDate(apPurchaseOrder.getPoDate());
                        details.setRrpPoDocumentNumber(apPurchaseOrder.getPoDocumentNumber());
                        details.setRrpPoReferenceNumber(apPurchaseOrder.getPoReferenceNumber());
                        details.setRrpPoCreatedBy(apPurchaseOrder.getPoCreatedBy());
                        details.setRrpPaymentTerm(apPurchaseOrder.getAdPaymentTerm().getPytName());
                        // details.setRrpFixedAssetReceiving(apPurchaseOrder.getPoFixedAssetReceiving());
                        details.setRrpPoDepartment(userDepartment);

                        if (apPurchaseOrder.getPoApprovedRejectedBy() != null && !apPurchaseOrder.getPoApprovedRejectedBy().equals("")) {

                            details.setRrpPoApprovedRejectedBy(apPurchaseOrder.getPoApprovedRejectedBy());

                        } else {

                            details.setRrpPoApprovedRejectedBy(adPreference.getPrfApDefaultApprover());
                        }

                        details.setRrpPoRcvPoNumber(apPurchaseOrder.getPoRcvPoNumber());
                        details.setRrpPoDescription(apPurchaseOrder.getPoDescription());
                        details.setRrpPllIiName(II_NM);
                        details.setRrpPlLocName(LOC_NM);
                        details.setRrpPlLocPosition(apPurchaseOrderLine.getInvItemLocation().getInvLocation().getLocPosition());
                        details.setRrpPlLocBranch(apPurchaseOrderLine.getInvItemLocation().getInvLocation().getLocBranch());
                        details.setRrpPlLocDepartment(apPurchaseOrderLine.getInvItemLocation().getInvLocation().getLocDepartment());
                        details.setRrpPlLocDateHired(apPurchaseOrderLine.getInvItemLocation().getInvLocation().getLocDateHired());
                        details.setRrpPlLocEmploymentStatus(apPurchaseOrderLine.getInvItemLocation().getInvLocation().getLocEmploymentStatus());
                        details.setRrpPlIiRemarks(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiRemarks());
                        details.setRrpPlUomName(apPurchaseOrderLine.getInvUnitOfMeasure().getUomName());
                        // details.setRrpPlItemBarcode(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiPartNumber());

                        details.setRrpPlQuantity(apPurchaseOrderLine.getPlQuantity());
                        details.setRrpPlUnitCost(apPurchaseOrderLine.getPlUnitCost());
                        details.setRrpPlAmount(apPurchaseOrderLine.getPlAmount());
                        details.setRrpPoPosted(apPurchaseOrder.getPoPosted());

                        details.setRrpPlTaxAmount(apPurchaseOrderLine.getPlTaxAmount());
                        details.setRrpPlItemDescription(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiDescription());
                        details.setRrpPoSupplierName(apPurchaseOrderLine.getApPurchaseOrder().getApSupplier().getSplName());
                        details.setRrpPoSupplierAddress(apPurchaseOrder.getApSupplier().getSplAddress());
                        details.setRrpPoContactPerson(apPurchaseOrderLine.getApPurchaseOrder().getApSupplier().getSplContact());
                        details.setRrpPoContactNumber(apPurchaseOrderLine.getApPurchaseOrder().getApSupplier().getSplPhone());
                        details.setRrpPoCheckedBy(adPreference.getPrfApDefaultChecker());

                        details.setRrpPoTaxRate(apPurchaseOrder.getApTaxCode().getTcRate());

                        // get unit cost wo vat
                        details.setRrpPlUnitCostWoTax(calculatePlNetUnitCost(details, apPurchaseOrder.getApTaxCode().getTcRate(), apPurchaseOrder.getApTaxCode().getTcType(), this.getGlFcPrecisionUnit(AD_CMPNY)));

                        // get unit cost vat inclusive
                        details.setRrpPlUnitCostTaxInclusive(calculatePlUnitCostTaxInclusive(details, apPurchaseOrder.getApTaxCode().getTcRate(), apPurchaseOrder.getApTaxCode().getTcType(), this.getGlFcPrecisionUnit(AD_CMPNY)));

                        //            			Include Branch
                        LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(apPurchaseOrder.getPoAdBranch());
                        details.setBranchCode(adBranch.getBrBranchCode());
                        details.setBranchName(adBranch.getBrName());

                        if (apPurchaseOrderLine.getApPurchaseOrder().getPoType().equals("PO MATCHED")) {

                            try {

                                LocalApPurchaseOrder existingApPurchaseOrder = apPurchaseOrderHome.findByPoDocumentNumberAndBrCode(apPurchaseOrderLine.getApPurchaseOrder().getPoRcvPoNumber(), AD_BRNCH, AD_CMPNY);
                                details.setRrpPoRcvPoDate(existingApPurchaseOrder.getPoDate());

                            } catch (FinderException ex) {

                            }

                            try {

                                LocalApPurchaseOrderLine existingApPurchaseOrderLine = apPurchaseOrderLineHome.findByPrimaryKey(apPurchaseOrderLine.getPlPlCode());
                                details.setRrpPlQuantityOrdered(existingApPurchaseOrderLine.getPlQuantity());

                            } catch (FinderException ex) {

                            }
                        }

                        LocalInvCosting invCosting = null;

                        try {

                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndIiNameAndLocName(II_NM, LOC_NM, AD_BRNCH, AD_CMPNY);
                            details.setRrpPlEndingBalance(apPurchaseOrderLine.getPlAmount() + invCosting.getCstRemainingValue());

                        } catch (FinderException ex) {
                            details.setRrpPlEndingBalance(apPurchaseOrderLine.getPlAmount());
                        }

                        Debug.print("new code");
                        StringBuilder strBProperty = new StringBuilder();
                        StringBuilder strBSerial = new StringBuilder();
                        StringBuilder strBSpecs = new StringBuilder();
                        StringBuilder strBCustodian = new StringBuilder();
                        StringBuilder strBExpirationDate = new StringBuilder();
                        Iterator it = apPurchaseOrderLine.getInvTags().iterator();

                        Collection invTags = apPurchaseOrderLine.getInvTags();
                        for (Object tag : invTags) {

                            LocalInvTag invTag = (LocalInvTag) tag;

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

                            // property code
                            details.setRrpTgPropertyCode(strBProperty.toString());
                            // serial number
                            details.setRrpTgSerialNumber(strBSerial.toString());
                            // specs
                            details.setRrpTgSpecs(strBSpecs.toString());
                            // custodian
                            details.setRrpTgCustodian(strBCustodian.toString());
                            // expiration date
                            details.setRrpTgExpiryDateString(strBExpirationDate.toString());
                        }

                        // property code
                        details.setRrpTgPropertyCode(strBProperty.toString());
                        // serial number
                        details.setRrpTgSerialNumber(strBSerial.toString());
                        // specs
                        details.setRrpTgSpecs(strBSpecs.toString());
                        // custodian
                        details.setRrpTgCustodian(strBCustodian.toString());
                        // expiration date
                        details.setRrpTgExpiryDateString(strBExpirationDate.toString());

                        list.add(details);
                    } else {
                        Debug.print("invTags is empty");
                        ApRepReceivingReportPrintDetails details = new ApRepReceivingReportPrintDetails();
                        if (isService) {
                            Debug.print(isService + " <== service ba?");
                            details.setRrpPlIsService(true);
                            Debug.print(details.getRrpPlIsService() + " <== details.getRrpPlIsService");
                        } else {
                            Debug.print(isService + " <== o hindi?");
                            details.setRrpPlIsService(false);
                            Debug.print(details.getRrpPlIsService() + " <== details.getRrpPlIsService");
                        }
                        details.setRrpPlIsInventoriable(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiNonInventoriable() != 1);
                        details.setRrpPoType(apPurchaseOrder.getPoType());
                        details.setRrpPoDate(apPurchaseOrder.getPoDate());
                        details.setRrpPoDocumentNumber(apPurchaseOrder.getPoDocumentNumber());
                        details.setRrpPoReferenceNumber(apPurchaseOrder.getPoReferenceNumber());
                        details.setRrpPoCreatedBy(apPurchaseOrder.getPoCreatedBy());
                        details.setRrpPaymentTerm(apPurchaseOrder.getAdPaymentTerm().getPytName());
                        // details.setRrpFixedAssetReceiving(apPurchaseOrder.getPoFixedAssetReceiving());

                        details.setRrpPoDepartment(userDepartment);

                        if (apPurchaseOrder.getPoApprovedRejectedBy() != null && !apPurchaseOrder.getPoApprovedRejectedBy().equals("")) {

                            details.setRrpPoApprovedRejectedBy(apPurchaseOrder.getPoApprovedRejectedBy());

                        } else {

                            details.setRrpPoApprovedRejectedBy(adPreference.getPrfApDefaultApprover());
                        }

                        details.setRrpPoRcvPoNumber(apPurchaseOrder.getPoRcvPoNumber());
                        details.setRrpPoDescription(apPurchaseOrder.getPoDescription());
                        details.setRrpPllIiName(II_NM);
                        details.setRrpPlLocName(LOC_NM);
                        details.setRrpPlLocPosition(apPurchaseOrderLine.getInvItemLocation().getInvLocation().getLocPosition());
                        details.setRrpPlLocBranch(apPurchaseOrderLine.getInvItemLocation().getInvLocation().getLocBranch());
                        details.setRrpPlLocDepartment(apPurchaseOrderLine.getInvItemLocation().getInvLocation().getLocDepartment());
                        details.setRrpPlLocDateHired(apPurchaseOrderLine.getInvItemLocation().getInvLocation().getLocDateHired());
                        details.setRrpPlLocEmploymentStatus(apPurchaseOrderLine.getInvItemLocation().getInvLocation().getLocEmploymentStatus());
                        details.setRrpPlIiRemarks(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiRemarks());
                        details.setRrpPlUomName(apPurchaseOrderLine.getInvUnitOfMeasure().getUomName());
                        // details.setRrpPlItemBarcode(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiPartNumber());

                        details.setRrpPlQuantity(apPurchaseOrderLine.getPlQuantity());
                        details.setRrpPlUnitCost(apPurchaseOrderLine.getPlUnitCost());
                        details.setRrpPlAmount(apPurchaseOrderLine.getPlAmount());

                        details.setRrpPlTaxAmount(apPurchaseOrderLine.getPlTaxAmount());
                        details.setRrpPlItemDescription(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiDescription());
                        details.setRrpPoSupplierName(apPurchaseOrderLine.getApPurchaseOrder().getApSupplier().getSplName());
                        details.setRrpPoSupplierAddress(apPurchaseOrder.getApSupplier().getSplAddress());
                        details.setRrpPoContactPerson(apPurchaseOrderLine.getApPurchaseOrder().getApSupplier().getSplContact());
                        details.setRrpPoContactNumber(apPurchaseOrderLine.getApPurchaseOrder().getApSupplier().getSplPhone());
                        details.setRrpPoCheckedBy(adPreference.getPrfApDefaultChecker());

                        details.setRrpPoTaxRate(apPurchaseOrder.getApTaxCode().getTcRate());

                        // get unit cost wo vat
                        details.setRrpPlUnitCostWoTax(calculatePlNetUnitCost(details, apPurchaseOrder.getApTaxCode().getTcRate(), apPurchaseOrder.getApTaxCode().getTcType(), this.getGlFcPrecisionUnit(AD_CMPNY)));

                        // get unit cost vat inclusive
                        details.setRrpPlUnitCostTaxInclusive(calculatePlUnitCostTaxInclusive(details, apPurchaseOrder.getApTaxCode().getTcRate(), apPurchaseOrder.getApTaxCode().getTcType(), this.getGlFcPrecisionUnit(AD_CMPNY)));

                        //            			Include Branch
                        LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(apPurchaseOrder.getPoAdBranch());
                        details.setBranchCode(adBranch.getBrBranchCode());
                        details.setBranchName(adBranch.getBrName());

                        if (apPurchaseOrderLine.getApPurchaseOrder().getPoType().equals("PO MATCHED")) {

                            try {

                                LocalApPurchaseOrder existingApPurchaseOrder = apPurchaseOrderHome.findByPoDocumentNumberAndBrCode(apPurchaseOrderLine.getApPurchaseOrder().getPoRcvPoNumber(), AD_BRNCH, AD_CMPNY);
                                details.setRrpPoRcvPoDate(existingApPurchaseOrder.getPoDate());

                            } catch (FinderException ex) {

                            }

                            try {

                                LocalApPurchaseOrderLine existingApPurchaseOrderLine = apPurchaseOrderLineHome.findByPrimaryKey(apPurchaseOrderLine.getPlPlCode());
                                details.setRrpPlQuantityOrdered(existingApPurchaseOrderLine.getPlQuantity());

                            } catch (FinderException ex) {

                            }
                        }

                        LocalInvCosting invCosting = null;

                        try {

                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndIiNameAndLocName(II_NM, LOC_NM, AD_BRNCH, AD_CMPNY);
                            details.setRrpPlEndingBalance(apPurchaseOrderLine.getPlAmount() + invCosting.getCstRemainingValue());

                        } catch (FinderException ex) {
                            details.setRrpPlEndingBalance(apPurchaseOrderLine.getPlAmount());
                        }

                        list.add(details);
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

        Debug.print("ApRepReceivingReportPrintControllerBean getAdCompany");

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

    public InvItemDetails getInvItemByIiName(String II_NM, Integer AD_CMPNY) {

        Debug.print("InvItemLocationEntryControllerBean getInvItemByIiName");

        LocalInvItem invItem = null;

        try {

            Debug.print("II_NM : " + II_NM);
            Debug.print("AD_CMPNY : " + AD_CMPNY);

            invItem = invItemHome.findByIiName(II_NM, AD_CMPNY);

            InvItemDetails details = new InvItemDetails();

            details.setIiName(invItem.getIiName());
            details.setIiDescription(invItem.getIiDescription());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public String getAdUsrDepartment(String USR_NM, Integer AD_CMPNY) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getAdUsrAll");

        LocalAdUser adUser = null;
        ArrayList list = new ArrayList();

        try {

            // Collection adUsers = adUserHome.findUsrByDepartment(USR_DEPT, AD_CMPNY);
            adUser = adUserHome.findByUsrName(USR_NM, AD_CMPNY);

            return adUser.getUsrDept();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ApRepReceivingReportPrintControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private double calculatePlNetAmount(double plAmount, double tcRate, String tcType, short precisionUnit) {

        Debug.print("ApRepReceivingReportPrintControllerBean calculatePlNetAmount");

        double amount = 0d;

        if (tcType.equals("INCLUSIVE")) {

            amount = EJBCommon.roundIt(plAmount / (1 + (tcRate / 100)), precisionUnit);

        } else {

            // tax exclusive, none, zero rated or exempt

            amount = plAmount;
        }

        return amount;
    }

    private double calculatePlTaxAmount(double plAmount, double tcRate, String tcType, double amount, short precisionUnit) {

        Debug.print("ApRepReceivingReportPrintControllerBean calculatePlTaxAmount");

        double taxAmount = 0d;

        if (!tcType.equals("NONE") && !tcType.equals("EXEMPT")) {

            if (tcType.equals("INCLUSIVE")) {

                taxAmount = EJBCommon.roundIt(plAmount - amount, precisionUnit);

            } else if (tcType.equals("EXCLUSIVE")) {

                taxAmount = EJBCommon.roundIt(plAmount * tcRate / 100, precisionUnit);

            } else {

                // tax none zero-rated or exempt

            }
        }

        return taxAmount;
    }

    private double calculatePlNetUnitCost(ApRepReceivingReportPrintDetails mdetails, double tcRate, String tcType, short precisionUnit) {

        Debug.print("ApRepReceivingReportPrintControllerBean calculatePlNetUnitCost");

        double amount = mdetails.getRrpPlUnitCost();

        if (tcType.equals("INCLUSIVE")) {

            amount = EJBCommon.roundIt(mdetails.getRrpPlUnitCost() / (1 + (tcRate / 100)), precisionUnit);
        }

        return amount;
    }

    private double calculatePlUnitCostTaxInclusive(ApRepReceivingReportPrintDetails mdetails, double tcRate, String tcType, short precisionUnit) {

        Debug.print("ApRepReceivingReportPrintControllerBean calculatePlUnitCostTaxInclusive");

        double amount = mdetails.getRrpPlUnitCost();

        if (tcType.equals("EXCLUSIVE")) {

            amount = EJBCommon.roundIt(mdetails.getRrpPlUnitCost() * (1 + (tcRate / 100)), precisionUnit);
        }

        return amount;
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApRepReceivingReportPrintControllerBean ejbCreate");
    }
}