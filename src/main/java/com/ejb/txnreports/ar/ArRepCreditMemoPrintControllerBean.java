package com.ejb.txnreports.ar;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;


import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdApproval;
import com.ejb.entities.ad.LocalAdApprovalDocument;
import com.ejb.dao.ad.LocalAdApprovalDocumentHome;
import com.ejb.dao.ad.LocalAdApprovalHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdUser;
import com.ejb.dao.ad.LocalAdUserHome;
import com.ejb.entities.ar.LocalArDistributionRecord;
import com.ejb.dao.ar.LocalArDistributionRecordHome;
import com.ejb.entities.ar.LocalArInvoice;
import com.ejb.dao.ar.LocalArInvoiceHome;
import com.ejb.entities.ar.LocalArInvoiceLine;
import com.ejb.dao.ar.LocalArInvoiceLineHome;
import com.ejb.entities.ar.LocalArInvoiceLineItem;
import com.ejb.dao.ar.LocalArInvoiceLineItemHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ar.ArRepInvoicePrintDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ArRepCreditMemoPrintControllerEJB")
public class ArRepCreditMemoPrintControllerBean extends EJBContextClass implements ArRepCreditMemoPrintController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdApprovalDocumentHome adApprovalDocumentHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalArInvoiceLineHome arInvoiceLineHome;
    @EJB
    private LocalArInvoiceLineItemHome arInvoiceLineItemHome;


    public ArrayList executeArRepInvoicePrint(ArrayList invCodeList, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepCreditMemoPrintControllerBean executeArRepInvoicePrint");

        ArrayList list = new ArrayList();

        try {

            for (Object o : invCodeList) {

                Integer INV_CODE = (Integer) o;

                LocalArInvoice arInvoice = null;
                LocalArInvoice arCreditedInvoice = null;

                try {

                    arInvoice = arInvoiceHome.findByPrimaryKey(INV_CODE);
                    arCreditedInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arInvoice.getInvCmInvoiceNumber(), EJBCommon.FALSE, AD_BRNCH, AD_CMPNY);

                } catch (FinderException ex) {

                    continue;
                }

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(AD_CMPNY);
                LocalAdApprovalDocument adApprovalDocument = adApprovalDocumentHome.findByAdcType("AR CREDIT MEMO", AD_CMPNY);

                if (adApprovalDocument.getAdcPrintOption().equals("PRINT APPROVED ONLY")) {

                    if (arInvoice.getInvApprovalStatus() == null || arInvoice.getInvApprovalStatus().equals("PENDING")) {

                        continue;
                    }

                } else if (adApprovalDocument.getAdcPrintOption().equals("PRINT UNAPPROVED ONLY")) {

                    if (arInvoice.getInvApprovalStatus() != null && (arInvoice.getInvApprovalStatus().equals("N/A") || arInvoice.getInvApprovalStatus().equals("APPROVED"))) {

                        continue;
                    }
                }

                if (adApprovalDocument.getAdcAllowDuplicate() == EJBCommon.FALSE && arInvoice.getInvPrinted() == EJBCommon.TRUE) {

                    continue;
                }

                // show duplicate

                boolean showDuplicate = adApprovalDocument.getAdcTrackDuplicate() == EJBCommon.TRUE && arInvoice.getInvPrinted() == EJBCommon.TRUE;

                // set printed

                Collection arInvoiceLineItems = null;
                Collection arInvoiceLines = null;

                try {

                    if (arInvoice.getInvCmInvoiceNumber() != null) {

                        arInvoiceLines = arInvoiceLineHome.findInvoiceLineByInvCodeAndAdCompany(arCreditedInvoice.getInvCode(), AD_CMPNY);
                    }

                } catch (FinderException ex) {
                    Debug.print("no invoice lines");
                }

                try {

                    arInvoiceLineItems = arInvoiceLineItemHome.findByInvCode(arInvoice.getInvCode(), AD_CMPNY);

                } catch (FinderException ex) {
                    Debug.print("no invoice line items");
                }

                double TOTAL_TAX_AMNT = 0;
                double TOTAL_WTAX_AMNT = 0;
                Collection arDistributionRecords = null;

                arDistributionRecords = arInvoice.getArDistributionRecords();
                Debug.print("size dr: " + arDistributionRecords.size());

                for (Object distributionRecord : arDistributionRecords) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;
                    Debug.print("dr class:" + arDistributionRecord.getDrClass());
                    if (arDistributionRecord.getDrClass().equals("TAX")) {
                        TOTAL_TAX_AMNT += arDistributionRecord.getDrAmount();
                    }
                    if (arDistributionRecord.getDrClass().equals("W-TAX")) {
                        TOTAL_WTAX_AMNT += arDistributionRecord.getDrAmount();
                    }
                }

                if (arInvoiceLineItems.size() > 0) {

                    for (Object invoiceLineItem : arInvoiceLineItems) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) invoiceLineItem;

                        arInvoice.setInvPrinted(EJBCommon.TRUE);

                        ArRepInvoicePrintDetails details = new ArRepInvoicePrintDetails();
                        details.setIpInvType(arInvoice.getInvType());
                        details.setIpInvDate(arInvoice.getInvDate());
                        details.setIpInvNumber(arInvoice.getInvNumber());
                        details.setIpInvCmReferenceNumber(arInvoice.getInvCmReferenceNumber());
                        Debug.print("arInvoice.getInvCmReferenceNumber()=" + arInvoice.getInvCmReferenceNumber());

                        details.setIpInvAmount(arInvoice.getInvAmountDue());
                        details.setIpInvCustomerName(arInvoice.getArCustomer().getCstName());
                        details.setIpInvCustomerAddress(arInvoice.getArCustomer().getCstAddress());
                        details.setIpInvCreatedBy(arInvoice.getInvCreatedBy());
                        details.setIpInvApprovedRejectedBy(arInvoice.getInvApprovedRejectedBy());
                        details.setIpIlAmount(arInvoice.getInvAmountDue());
                        details.setIpInvDescription(arInvoice.getInvDescription());
                        details.setIpInvCurrencySymbol(arCreditedInvoice.getGlFunctionalCurrency().getFcSymbol());
                        details.setIpInvCurrencyDescription(arCreditedInvoice.getGlFunctionalCurrency().getFcDescription());
                        // details.setIpShowDuplicate(showDuplicate ? EJBCommon.TRUE : EJBCommon.FALSE);
                        details.setIpInvReferenceNumber(arInvoice.getInvCmInvoiceNumber());
                        details.setIpInvCustomerCity(arInvoice.getArCustomer().getCstCity());

                        if (arCreditedInvoice.getArSalesperson() != null) {

                            details.setIpSlpSalespersonCode(arCreditedInvoice.getArSalesperson().getSlpSalespersonCode());
                            details.setIpSlpName(arCreditedInvoice.getArSalesperson().getSlpName());
                        }
                        details.setIpInvCmInvoiceNumber(arCreditedInvoice.getInvNumber());
                        details.setIpInvTotalTax(TOTAL_TAX_AMNT);
                        details.setIpInvWithholdingTaxAmount(TOTAL_WTAX_AMNT);
                        details.setIpInvCmInvoiceDate(EJBCommon.convertSQLDateToString(arCreditedInvoice.getInvDate()));
                        details.setIpInvApprovalStatus(arInvoice.getInvApprovalStatus());
                        Debug.print(arInvoice.getInvApprovalStatus());
                        // get user name description

                        Debug.print("apVoucher.getChkCreatedBy(): " + arInvoice.getInvCreatedBy());

                        try {
                            LocalAdUser adUser = adUserHome.findByUsrName(arInvoice.getInvCreatedBy(), AD_CMPNY);
                            details.setIpInvCreatedByDescription(adUser.getUsrDescription());
                        } catch (Exception e) {
                            details.setIpInvCreatedByDescription("");
                        }

                        Debug.print("apVoucher.getChkCreatedBy(): " + details.getIpInvCreatedByDescription());

                        try {
                            Debug.print("apVoucher.getVouApprovedRejectedBy(): " + arInvoice.getInvApprovedRejectedBy());
                            LocalAdUser adUser3 = adUserHome.findByUsrName(arInvoice.getInvApprovedRejectedBy(), AD_CMPNY);
                            details.setIpInvApprovedRejectedByDescription(adUser3.getUsrDescription());

                        } catch (Exception e) {
                            details.setIpInvApprovedRejectedByDescription("");
                        }

                        Debug.print("DpVouApprovedRejectedByDescription(): " + details.getIpInvApprovedRejectedByDescription());

                        details.setIpIlName(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                        details.setIpIlDescription(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiDescription());
                        details.setIpIlQuantity(arInvoiceLineItem.getIliQuantity());
                        details.setIpInvIlSmlUnitOfMeasure(arInvoiceLineItem.getInvUnitOfMeasure().getUomName());
                        details.setIpIlUnitPrice(arInvoiceLineItem.getIliUnitPrice());
                        details.setIpIlAmount(arInvoiceLineItem.getIliAmount() + arInvoiceLineItem.getIliTaxAmount());

                        Debug.print("amount line is: " + details.getIpIlAmount());
                        details.setIpIliDiscount1(arInvoiceLineItem.getIliDiscount1());
                        details.setIpIliTotalDiscount(arInvoiceLineItem.getIliTotalDiscount());

                        Debug.print("arInvoiceLine.getIlDescription(): " + arInvoiceLineItem.getInvUnitOfMeasure().getUomName());
                        Debug.print("arInvoiceLine.getIlQuantity(): " + arInvoiceLineItem.getIliQuantity());
                        list.add(details);
                    }
                } else {

                    for (Object invoiceLine : arInvoiceLines) {

                        LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) invoiceLine;

                        arInvoice.setInvPrinted(EJBCommon.TRUE);

                        ArRepInvoicePrintDetails details = new ArRepInvoicePrintDetails();

                        details.setIpInvType(arInvoice.getInvType());
                        details.setIpInvDate(arInvoice.getInvDate());
                        details.setIpInvNumber(arInvoice.getInvNumber());
                        details.setIpInvCmReferenceNumber(arInvoice.getInvCmReferenceNumber());
                        Debug.print("arInvoice.getInvCmReferenceNumber()=" + arInvoice.getInvCmReferenceNumber());

                        details.setIpInvAmount(arInvoice.getInvAmountDue());
                        details.setIpInvCustomerName(arInvoice.getArCustomer().getCstName());
                        details.setIpInvCustomerAddress(arInvoice.getArCustomer().getCstAddress());
                        details.setIpInvCreatedBy(arInvoice.getInvCreatedBy());
                        details.setIpInvApprovedRejectedBy(arInvoice.getInvApprovedRejectedBy());
                        details.setIpIlAmount(arInvoice.getInvAmountDue());
                        details.setIpInvDescription(arInvoice.getInvDescription());
                        details.setIpInvCurrencySymbol(arCreditedInvoice.getGlFunctionalCurrency().getFcSymbol());
                        details.setIpInvCurrencyDescription(arCreditedInvoice.getGlFunctionalCurrency().getFcDescription());
                        // details.setIpShowDuplicate(showDuplicate ? EJBCommon.TRUE : EJBCommon.FALSE);
                        details.setIpInvReferenceNumber(arInvoice.getInvCmInvoiceNumber());
                        details.setIpInvCustomerCity(arInvoice.getArCustomer().getCstCity());

                        if (arCreditedInvoice.getArSalesperson() != null) {

                            details.setIpSlpSalespersonCode(arCreditedInvoice.getArSalesperson().getSlpSalespersonCode());
                            details.setIpSlpName(arCreditedInvoice.getArSalesperson().getSlpName());
                        }
                        details.setIpInvCmInvoiceNumber(arCreditedInvoice.getInvNumber());

                        details.setIpInvCmInvoiceDate(EJBCommon.convertSQLDateToString(arCreditedInvoice.getInvDate()));
                        details.setIpInvApprovalStatus(arInvoice.getInvApprovalStatus());
                        Debug.print(arInvoice.getInvApprovalStatus());
                        // get user name description

                        Debug.print("apVoucher.getChkCreatedBy(): " + arInvoice.getInvCreatedBy());

                        try {
                            LocalAdUser adUser = adUserHome.findByUsrName(arInvoice.getInvCreatedBy(), AD_CMPNY);
                            details.setIpInvCreatedByDescription(adUser.getUsrDescription());
                        } catch (Exception e) {
                            details.setIpInvCreatedByDescription("");
                        }

                        Debug.print("apVoucher.getChkCreatedBy(): " + details.getIpInvCreatedByDescription());

                        try {
                            Debug.print("apVoucher.getVouApprovedRejectedBy(): " + arInvoice.getInvApprovedRejectedBy());
                            LocalAdUser adUser3 = adUserHome.findByUsrName(arInvoice.getInvApprovedRejectedBy(), AD_CMPNY);
                            details.setIpInvApprovedRejectedByDescription(adUser3.getUsrDescription());

                        } catch (Exception e) {
                            details.setIpInvApprovedRejectedByDescription("");
                        }
                        Debug.print("DpVouApprovedRejectedByDescription(): " + details.getIpInvApprovedRejectedByDescription());

                        // details.setIpIlName(arInvoiceLine.getInvItemLocation().getInvItem().getIiName());
                        details.setIpIlDescription(arInvoiceLine.getIlDescription());
                        details.setIpIlQuantity(arInvoiceLine.getIlQuantity());
                        //	details.setIpInvIlSmlUnitOfMeasure(arInvoiceLine.getInvUnitOfMeasure().getUomName());
                        details.setIpIlUnitPrice(arInvoiceLine.getIlUnitPrice());
                        details.setIpIlAmount(arInvoiceLine.getIlAmount());
                        details.setIpInvTotalTax(TOTAL_TAX_AMNT);
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

        Debug.print("ArRepCreditMemoPrintControllerBean getAdCompany");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());
            details.setCmpAddress(adCompany.getCmpAddress());
            details.setCmpCity(adCompany.getCmpCity());
            details.setCmpCountry(adCompany.getCmpCountry());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList executeArRepInvoicePrintSub(ArrayList invCodeList, Integer AD_CMPNY) {

        Debug.print("ArRepCreditMemoPrintControllerBean executeArRepInvoicePrintSub");

        ArrayList list = new ArrayList();

        try {

            for (Object o : invCodeList) {

                Integer INV_CODE = (Integer) o;

                LocalArInvoice arInvoice = null;

                try {

                    arInvoice = arInvoiceHome.findByPrimaryKey(INV_CODE);

                } catch (FinderException ex) {

                    continue;
                }

                // set printed

                arInvoice.setInvPrinted(EJBCommon.TRUE);

                // get invoice distribution records

                Collection arDistributionRecords = arDistributionRecordHome.findByInvCode(arInvoice.getInvCode(), AD_CMPNY);

                for (Object distributionRecord : arDistributionRecords) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                    ArRepInvoicePrintDetails details = new ArRepInvoicePrintDetails();

                    details.setIpDrCoaAccountNumber(arDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                    details.setIpDrCoaAccountDescription(arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());
                    details.setIpDrAmount(arDistributionRecord.getDrAmount());
                    details.setIpDrDebit(arDistributionRecord.getDrDebit());

                    list.add(details);
                }
            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ArRepCreditMemoPrintControllerBean ejbCreate");
    }
}