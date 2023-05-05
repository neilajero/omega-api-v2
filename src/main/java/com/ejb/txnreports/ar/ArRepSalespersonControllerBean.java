/**
 * @copyright 2022 Omega Business Consulting, Inc.
 * @class ArRepSalespersonControllerBean
 * @created February 6, 2006, 4:34 PM
 * @author Franco Antonio R. Roig
 */
package com.ejb.txnreports.ar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdBranchItemLocation;
import com.ejb.dao.ad.LocalAdBranchItemLocationHome;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.entities.ad.LocalAdBranchStandardMemoLine;
import com.ejb.dao.ad.LocalAdBranchStandardMemoLineHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ar.LocalArAppliedInvoice;
import com.ejb.entities.ar.LocalArCustomerClass;
import com.ejb.dao.ar.LocalArCustomerClassHome;
import com.ejb.entities.ar.LocalArCustomerType;
import com.ejb.dao.ar.LocalArCustomerTypeHome;
import com.ejb.entities.ar.LocalArInvoice;
import com.ejb.dao.ar.LocalArInvoiceHome;
import com.ejb.entities.ar.LocalArInvoiceLine;
import com.ejb.dao.ar.LocalArInvoiceLineHome;
import com.ejb.entities.ar.LocalArInvoiceLineItem;
import com.ejb.dao.ar.LocalArInvoiceLineItemHome;
import com.ejb.entities.ar.LocalArInvoicePaymentSchedule;
import com.ejb.entities.ar.LocalArReceipt;
import com.ejb.dao.ar.LocalArReceiptHome;
import com.ejb.entities.ar.LocalArSalesOrderInvoiceLine;
import com.ejb.dao.ar.LocalArSalesOrderInvoiceLineHome;
import com.ejb.entities.ar.LocalArStandardMemoLine;
import com.ejb.dao.ar.LocalArStandardMemoLineHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.entities.inv.LocalInvItemLocation;
import com.ejb.dao.inv.LocalInvItemLocationHome;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ar.ArRepSalespersonDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import jakarta.ejb.Stateless;

@Stateless(name = "ArRepSalespersonControllerEJB")
public class ArRepSalespersonControllerBean extends EJBContextClass implements ArRepSalespersonController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalAdBranchStandardMemoLineHome adBranchStandardMemoLineHome;
    @EJB
    private LocalArCustomerClassHome arCustomerClassHome;
    @EJB
    private LocalArCustomerTypeHome arCustomerTypeHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalArInvoiceLineHome arInvoiceLineHome;
    @EJB
    private LocalArInvoiceLineItemHome arInvoiceLineItemHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalArSalesOrderInvoiceLineHome arSalesOrderInvoiceLineHome;
    @EJB
    private LocalArStandardMemoLineHome arStandardMemoLineHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;


    public ArrayList getArCcAll(Integer AD_CMPNY) {

        Debug.print("ArRepSalespersonControllerBean getArCcAll");

        ArrayList list = new ArrayList();
        LocalArCustomerClass arCustomerClass = null;

        try {

            Collection arCustomerClasses = arCustomerClassHome.findEnabledCcAll(AD_CMPNY);

            for (Object customerClass : arCustomerClasses) {

                arCustomerClass = (LocalArCustomerClass) customerClass;

                list.add(arCustomerClass.getCcName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArCtAll(Integer AD_CMPNY) {

        Debug.print("ArCustomerEntryControllerBean getArCtAll");

        ArrayList list = new ArrayList();
        LocalArCustomerType arCustomerType = null;

        try {

            Collection arCustomerTypes = arCustomerTypeHome.findEnabledCtAll(AD_CMPNY);

            for (Object customerType : arCustomerTypes) {

                arCustomerType = (LocalArCustomerType) customerType;

                list.add(arCustomerType.getCtName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArCustomerEntryControllerBean getAdBrResAll");

        LocalAdBranchResponsibility adBranchResponsibility = null;
        LocalAdBranch adBranch = null;

        Collection adBranchResponsibilities = null;

        ArrayList list = new ArrayList();

        try {

            adBranchResponsibilities = adBranchResponsibilityHome.findByAdResponsibility(RS_CODE, AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (adBranchResponsibilities.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }

        try {

            for (Object branchResponsibility : adBranchResponsibilities) {

                adBranchResponsibility = (LocalAdBranchResponsibility) branchResponsibility;

                adBranch = adBranchHome.findByPrimaryKey(adBranchResponsibility.getAdBranch().getBrCode());

                AdBranchDetails details = new AdBranchDetails();
                details.setBrCode(adBranch.getBrCode());
                details.setBrBranchCode(adBranch.getBrBranchCode());
                details.setBrName(adBranch.getBrName());
                details.setBrHeadQuarter(adBranch.getBrHeadQuarter());

                list.add(details);
            }

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        return list;
    }

    public ArrayList executeArRepSalesperson(HashMap criteria, ArrayList branchList, String GROUP_BY, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepSalespersonControllerBean executeArRepSalesperson");

        ArrayList list = new ArrayList();

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();
            String PYMNT_STTS = null;

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(inv) FROM ArInvoice inv WHERE (");

            if (branchList.isEmpty()) throw new GlobalNoRecordFoundException();

            Iterator brIter = branchList.iterator();

            AdBranchDetails details = (AdBranchDetails) brIter.next();
            jbossQl.append("inv.invAdBranch=").append(details.getBrCode());

            while (brIter.hasNext()) {

                details = (AdBranchDetails) brIter.next();

                jbossQl.append(" OR inv.invAdBranch=").append(details.getBrCode());
            }

            jbossQl.append(") AND inv.arSalesperson.slpSalespersonCode IS NOT NULL ");
            firstArgument = false;

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("salespersonCode")) {

                criteriaSize--;
            }

            if (criteria.containsKey("customerCode")) {

                criteriaSize--;
            }

            if (criteria.containsKey("includedMiscReceipts")) {

                criteriaSize--;
            }

            if (criteria.containsKey("paymentStatus")) {

                criteriaSize--;
                PYMNT_STTS = (String) criteria.get("paymentStatus");
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("salespersonCode")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("inv.arSalesperson.slpSalespersonCode LIKE '%").append(criteria.get("salespersonCode")).append("%' ");
            }

            if (criteria.containsKey("customerCode")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("inv.arCustomer.cstCustomerCode LIKE '%").append(criteria.get("customerCode")).append("%' ");
            }

            if (criteria.containsKey("customerClass")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("inv.arCustomer.arCustomerClass.ccName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerClass");
                ctr++;
            }

            if (criteria.containsKey("customerType")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("inv.arCustomer.arCustomerType.ctName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerType");
                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("inv.invDate>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateFrom");
                ctr++;
            }

            if (criteria.containsKey("dateTo")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("inv.invDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (criteria.containsKey("invoiceNumberFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("inv.invNumber>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("invoiceNumberFrom");
                ctr++;
            }

            if (criteria.containsKey("invoiceNumberTo")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("inv.invNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("invoiceNumberTo");
                ctr++;
            }

            if (criteria.containsKey("paymentStatus")) {

                String paymentStatus = (String) criteria.get("paymentStatus");

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                if (paymentStatus.equals("PAID")) {

                    jbossQl.append("inv.invAmountDue = inv.invAmountPaid ");

                } else if (paymentStatus.equals("UNPAID")) {

                    jbossQl.append("inv.invAmountDue <> inv.invAmountPaid ");
                }
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("inv.invPosted = 1 AND inv.invVoid = 0 AND inv.invAdCompany=").append(AD_CMPNY).append(" ");

            Collection arInvoices = arInvoiceHome.getInvByCriteria(jbossQl.toString(), obj, 0, 0);

            if (!arInvoices.isEmpty()) {

                for (Object invoice : arInvoices) {

                    LocalArInvoice arInvoice = (LocalArInvoice) invoice;

                    ArRepSalespersonDetails mdetails = new ArRepSalespersonDetails();
                    mdetails.setSlpDate(arInvoice.getInvDate());
                    mdetails.setSlpInvoiceNumber(arInvoice.getInvNumber());
                    mdetails.setSlpCstCustomerCode(arInvoice.getArCustomer().getCstCustomerCode());
                    mdetails.setSlpCstCustomerName(arInvoice.getArCustomer().getCstName());
                    mdetails.setSlpCstCustomerClass(arInvoice.getArCustomer().getArCustomerClass().getCcName());
                    mdetails.setSlpCstCustomerType(arInvoice.getArCustomer().getArCustomerType() != null ? arInvoice.getArCustomer().getArCustomerType().getCtName() : "N/A");
                    mdetails.setSlpSubjectToCommission(arInvoice.getInvSubjectToCommission());
                    mdetails.setSlpType("INVOICE");
                    mdetails.setSlpPrimaryKey(arInvoice.getInvCode());
                    mdetails.setSlpSalespersonCode(arInvoice.getArSalesperson().getSlpSalespersonCode());
                    mdetails.setSlpSalespersonName(arInvoice.getArSalesperson().getSlpName());
                    mdetails.setSlpAdBranch(arInvoice.getInvAdBranch());

                    for (Object o1 : arInvoice.getArInvoiceLines()) {

                        LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) o1;
                        mdetails.saveSlpArInvoiceLines(arInvoiceLine);
                    }

                    for (Object element : arInvoice.getArInvoiceLineItems()) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) element;
                        mdetails.saveSlpArInvoiceLineItems(arInvoiceLineItem);
                    }

                    for (Object item : arInvoice.getArSalesOrderInvoiceLines()) {

                        LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) item;
                        mdetails.saveSlpArSalesOrderInvoiceLines(arSalesOrderInvoiceLine);
                    }

                    double AMNT_DUE = 0d;
                    double AMNT_PD = 0d;

                    if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {

                        AMNT_DUE = EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arInvoice.getInvAmountDue(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision());

                        AMNT_PD = EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arInvoice.getInvAmountPaid(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision());

                    } else {

                        LocalArInvoice arCreditedInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arInvoice.getInvCmInvoiceNumber(), EJBCommon.FALSE, AD_BRNCH, AD_CMPNY);

                        AMNT_DUE = EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arCreditedInvoice.getGlFunctionalCurrency().getFcCode(), arCreditedInvoice.getGlFunctionalCurrency().getFcName(), arCreditedInvoice.getInvConversionDate(), arCreditedInvoice.getInvConversionRate(), arInvoice.getInvAmountDue(), AD_CMPNY) * -1, adCompany.getGlFunctionalCurrency().getFcPrecision());

                        AMNT_PD = EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arCreditedInvoice.getGlFunctionalCurrency().getFcCode(), arCreditedInvoice.getGlFunctionalCurrency().getFcName(), arCreditedInvoice.getInvConversionDate(), arCreditedInvoice.getInvConversionRate(), arInvoice.getInvAmountPaid(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision());
                    }

                    mdetails.setSlpAmount(AMNT_DUE);
                    mdetails.setSlpInvcAmountPd(AMNT_PD);

                    if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {
                        if (arInvoice.getInvAmountDue() == arInvoice.getInvAmountPaid()) {
                            mdetails.setSlpPaymentStatus("PAID");
                            for (Object value : arInvoice.getArInvoicePaymentSchedules()) {

                                LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) value;
                                for (Object o : arInvoicePaymentSchedule.getArAppliedInvoices()) {
                                    LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) o;

                                    mdetails.setSlpCollectionDate(arAppliedInvoice.getArReceipt() != null ? arAppliedInvoice.getArReceipt().getRctDate() : arAppliedInvoice.getArPdc().getPdcDateReceived());
                                }
                            }

                        } else {
                            mdetails.setSlpPaymentStatus("UNPAID");
                        }
                    } else {
                        mdetails.setSlpPaymentStatus("PAID");
                    }

                    list.add(mdetails);
                }
            }

            Collection arReceipts = null;
            String miscReceipts = null;

            if (criteria.containsKey("includedMiscReceipts")) {

                miscReceipts = (String) criteria.get("includedMiscReceipts");

                if (miscReceipts.equals("YES")) {

                    // misc receipt

                    jbossQl = new StringBuilder();
                    jbossQl.append("SELECT OBJECT(rct) FROM ArReceipt rct WHERE (");

                    brIter = branchList.iterator();

                    details = (AdBranchDetails) brIter.next();
                    jbossQl.append("rct.rctAdBranch=").append(details.getBrCode());

                    while (brIter.hasNext()) {

                        details = (AdBranchDetails) brIter.next();

                        jbossQl.append(" OR rct.rctAdBranch=").append(details.getBrCode());
                    }

                    jbossQl.append(") AND rct.arSalesperson.slpSalespersonCode IS NOT NULL ");
                    firstArgument = false;

                    ctr = 0;

                    if (criteria.containsKey("invoiceNumberFrom")) {

                        criteriaSize--;
                    }

                    if (criteria.containsKey("invoiceNumberTo")) {

                        criteriaSize--;
                    }

                    obj = new Object[criteriaSize];

                    if (criteria.containsKey("customerCode")) {

                        if (!firstArgument) {

                            jbossQl.append("AND ");

                        } else {

                            firstArgument = false;
                            jbossQl.append("WHERE ");
                        }

                        jbossQl.append("rct.arCustomer.cstCustomerCode LIKE '%").append(criteria.get("customerCode")).append("%' ");
                    }

                    if (criteria.containsKey("salespersonCode")) {

                        if (!firstArgument) {

                            jbossQl.append("AND ");

                        } else {

                            firstArgument = false;
                            jbossQl.append("WHERE ");
                        }

                        jbossQl.append("rct.arSalesperson.slpSalespersonCode LIKE '%").append(criteria.get("salespersonCode")).append("%' ");
                    }

                    if (criteria.containsKey("customerClass")) {

                        if (!firstArgument) {

                            jbossQl.append("AND ");

                        } else {

                            firstArgument = false;
                            jbossQl.append("WHERE ");
                        }

                        jbossQl.append("rct.arCustomer.arCustomerClass.ccName=?").append(ctr + 1).append(" ");
                        obj[ctr] = criteria.get("customerClass");
                        ctr++;
                    }

                    if (criteria.containsKey("customerType")) {

                        if (!firstArgument) {

                            jbossQl.append("AND ");

                        } else {

                            firstArgument = false;
                            jbossQl.append("WHERE ");
                        }

                        jbossQl.append("rct.arCustomer.arCustomerType.ctName=?").append(ctr + 1).append(" ");
                        obj[ctr] = criteria.get("customerType");
                        ctr++;
                    }

                    if (criteria.containsKey("dateFrom")) {

                        if (!firstArgument) {

                            jbossQl.append("AND ");

                        } else {

                            firstArgument = false;
                            jbossQl.append("WHERE ");
                        }
                        jbossQl.append("rct.rctDate>=?").append(ctr + 1).append(" ");
                        obj[ctr] = criteria.get("dateFrom");
                        ctr++;
                    }

                    if (criteria.containsKey("dateTo")) {

                        if (!firstArgument) {

                            jbossQl.append("AND ");

                        } else {

                            firstArgument = false;
                            jbossQl.append("WHERE ");
                        }
                        jbossQl.append("rct.rctDate<=?").append(ctr + 1).append(" ");
                        obj[ctr] = criteria.get("dateTo");
                        ctr++;
                    }

                    if (criteria.containsKey("paymentStatus")) {

                        String paymentStatus = (String) criteria.get("paymentStatus");

                        if (!firstArgument) {

                            jbossQl.append("AND ");

                        } else {

                            firstArgument = false;
                            jbossQl.append("WHERE ");
                        }

                        if (paymentStatus.equals("PAID")) {

                            jbossQl.append("rct.rctPosted = 1 ");

                        } else if (paymentStatus.equals("UNPAID")) {

                            jbossQl.append("rct.rctPosted = 0 ");
                        }
                    }

                    //       	  		if (criteria.containsKey("paymentStatus")) {
                    //
                    //       	  			String paymentStatus = (String)criteria.get("paymentStatus");
                    //
                    //
                    //       	  			if (paymentStatus.equals("PAID")) {
                    //
                    //       	  				if (!firstArgument) {
                    //
                    //       	  					jbossQl.append("AND ");
                    //
                    //       	  				} else {
                    //
                    //       	  					firstArgument = false;
                    //       	  					jbossQl.append("WHERE ");
                    //
                    //       	  				}
                    //
                    //       	  				jbossQl.append("(rct.rctApprovalStatus = 'N/A' OR rct.rctApprovalStatus =
                    // 'APPROVED') ");
                    //
                    //       	  			}
                    //
                    //       	  		}

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("rct.rctVoid = 0 AND rct.rctType='MISC' AND rct.rctAdCompany=").append(AD_CMPNY).append(" ");

                    arReceipts = arReceiptHome.getRctByCriteria(jbossQl.toString(), obj, 0, 0);

                    if (!arReceipts.isEmpty()) {

                        for (Object receipt : arReceipts) {

                            LocalArReceipt arReceipt = (LocalArReceipt) receipt;

                            ArRepSalespersonDetails mdetails = new ArRepSalespersonDetails();
                            mdetails.setSlpDate(arReceipt.getRctDate());
                            mdetails.setSlpInvoiceNumber(arReceipt.getRctNumber());
                            mdetails.setSlpCstCustomerCode(arReceipt.getArCustomer().getCstCustomerCode());
                            mdetails.setSlpCstCustomerName(arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName());
                            mdetails.setSlpCstCustomerClass(arReceipt.getArCustomer().getArCustomerClass().getCcName());
                            mdetails.setSlpCstCustomerType(arReceipt.getArCustomer().getArCustomerType() != null ? arReceipt.getArCustomer().getArCustomerType().getCtName() : "N/A");
                            mdetails.setSlpSubjectToCommission(arReceipt.getRctSubjectToCommission());
                            mdetails.setSlpType("RECEIPT");
                            mdetails.setSlpPrimaryKey(arReceipt.getRctCode());
                            mdetails.setSlpSalespersonCode(arReceipt.getArSalesperson().getSlpSalespersonCode());
                            mdetails.setSlpSalespersonName(arReceipt.getArSalesperson().getSlpName());
                            mdetails.setSlpAdBranch(arReceipt.getRctAdBranch());
                            mdetails.setSlpRctCustomerDeposit(arReceipt.getRctCustomerDeposit() == 1);

                            for (Object value : arReceipt.getArInvoiceLines()) {

                                LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) value;
                                mdetails.saveSlpArInvoiceLines(arInvoiceLine);
                            }

                            for (Object o : arReceipt.getArInvoiceLineItems()) {

                                LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) o;
                                mdetails.saveSlpArInvoiceLineItems(arInvoiceLineItem);
                            }

                            double AMNT_DUE = 0d;
                            AMNT_DUE = EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision());
                            mdetails.setSlpAmount(AMNT_DUE);
                            mdetails.setSlpPaymentStatus("PAID");
                            mdetails.setSlpCollectionDate(arReceipt.getRctDate());
                            list.add(mdetails);
                        }
                    }
                }
            }

            if (arInvoices.size() == 0) {

                if (arReceipts != null && arReceipts.size() == 0) throw new GlobalNoRecordFoundException();
                else if (miscReceipts.equals("NO")) throw new GlobalNoRecordFoundException();
            }

            ArrayList finalList = new ArrayList();

            finalList = findAndComputeCommission(list, GROUP_BY, criteria, branchList, AD_CMPNY);

            return finalList;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("ArRepSalespersonControllerBean getAdCompany");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods
    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("ArRepSalespersonControllerBean convertForeignToFunctionalCurrency");

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

    // Private Methods

    private ArrayList findAndComputeCommission(ArrayList tempList, String GROUP_BY, HashMap criteria, ArrayList branchList, Integer AD_CMPNY) {

        ArrayList list = new ArrayList();
        ArrayList existingCmList = new ArrayList();
        Iterator i = tempList.iterator();
        double grandTotalSales = 0;

        while (i.hasNext()) {

            ArRepSalespersonDetails details = (ArRepSalespersonDetails) i.next();

            // this is an invoice

            if (!details.getSlpArInvoiceLines().isEmpty()) { // Invoice is MEMO LINE

                for (Object o : details.getSlpArInvoiceLines()) {

                    LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) o;
                    LocalArStandardMemoLine arStandardMemoLine = null;
                    LocalAdBranchStandardMemoLine adBranchStandardMemoLine = null;

                    try {

                        arStandardMemoLine = arStandardMemoLineHome.findByPrimaryKey(arInvoiceLine.getArStandardMemoLine().getSmlCode());

                    } catch (FinderException ex) {

                    }

                    if (arStandardMemoLine.getSmlSubjectToCommission() == EJBCommon.TRUE) { // SML is subject to commission

                        details.setSlpCommissionAmount(details.getSlpCommissionAmount() + arInvoiceLine.getIlAmount());

                    } else { // branch sml might be subject to commission

                        try {

                            adBranchStandardMemoLine = adBranchStandardMemoLineHome.findBSMLBySMLCodeAndBrCode(arInvoiceLine.getArStandardMemoLine().getSmlCode(), details.getSlpAdBranch(), AD_CMPNY);

                        } catch (FinderException ex) {

                        }

                        if (adBranchStandardMemoLine.getBsmlSubjectToCommission() == EJBCommon.TRUE) { // branch sml is subject to commission

                            details.setSlpCommissionAmount(details.getSlpCommissionAmount() + arInvoiceLine.getIlAmount());

                        } else { // branch sml is not subject to commission

                            details.setSlpNonCommissionAmount(details.getSlpNonCommissionAmount() + arInvoiceLine.getIlAmount());
                        }
                    }
                }

            } else if (!details.getSlpArInvoiceLineItems().isEmpty()) { // Invoice is ITEMS

                for (Object o : details.getSlpArInvoiceLineItems()) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) o;
                    LocalInvItemLocation invItemLocation = null;
                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {
                        invItemLocation = invItemLocationHome.findByPrimaryKey(arInvoiceLineItem.getInvItemLocation().getIlCode());

                    } catch (FinderException ex) {

                    }

                    if (invItemLocation.getIlSubjectToCommission() == EJBCommon.TRUE) { // IL is subject to commission

                        details.setSlpCommissionAmount(details.getSlpCommissionAmount() + arInvoiceLineItem.getIliAmount());

                    } else { // branch IL might be subject to commission

                        try {

                            adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arInvoiceLineItem.getInvItemLocation().getIlCode(), details.getSlpAdBranch(), AD_CMPNY);

                            if (adBranchItemLocation.getBilSubjectToCommission() == EJBCommon.TRUE) { // branch IL is subject to commission

                                details.setSlpCommissionAmount(details.getSlpCommissionAmount() + arInvoiceLineItem.getIliAmount());

                            } else { // branch IL is not subject to commission

                                details.setSlpNonCommissionAmount(details.getSlpNonCommissionAmount() + arInvoiceLineItem.getIliAmount());
                            }

                        } catch (FinderException ex) {

                        }
                    }
                }

            } else if (!details.getSlpArSalesOrderInvoiceLines().isEmpty()) { // Invoice is SO MATCHED

                for (Object o : details.getSlpArSalesOrderInvoiceLines()) {

                    LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) o;
                    LocalInvItemLocation invItemLocation = null;
                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {

                        invItemLocation = invItemLocationHome.findByPrimaryKey(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getIlCode());

                    } catch (FinderException ex) {

                    }

                    if (invItemLocation.getIlSubjectToCommission() == EJBCommon.TRUE) { // SO is subject to commission

                        details.setSlpCommissionAmount(details.getSlpCommissionAmount() + arSalesOrderInvoiceLine.getSilAmount());

                    } else { // branch IL might be subject to commission

                        try {

                            adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getIlCode(), details.getSlpAdBranch(), AD_CMPNY);

                            if (adBranchItemLocation.getBilSubjectToCommission() == EJBCommon.TRUE) { // branch SO is subject to commission

                                details.setSlpCommissionAmount(details.getSlpCommissionAmount() + arSalesOrderInvoiceLine.getSilAmount());

                            } else { // branch IL is not subject to commission

                                details.setSlpNonCommissionAmount(details.getSlpNonCommissionAmount() + arSalesOrderInvoiceLine.getSilAmount());
                            }
                        } catch (FinderException ex) {
                        }
                    }
                }
            }

            boolean hasCreditMemo = false;
            double originalNonCommAmount = details.getSlpNonCommissionAmount();
            double difference = 0d;

            if (details.getSlpType().equalsIgnoreCase("INVOICE")) {

                Collection arCreditMemos = null;

                try {

                    arCreditMemos = arInvoiceHome.findByInvCreditMemoAndInvCmInvoiceNumberAndInvVoidAndInvPosted((byte) 1, details.getSlpInvoiceNumber(), (byte) 0, (byte) 1, AD_CMPNY);

                } catch (FinderException ex) {

                }

                // find credit memos with previous invoices

                if (criteria.containsKey("dateFrom") && !existingCmList.contains(details.getSlpCstCustomerCode())) {
                    existingCmList.add(details.getSlpCstCustomerCode());
                    StringBuilder jbossQl = new StringBuilder();
                    jbossQl.append("SELECT OBJECT(inv) FROM ArInvoice inv WHERE (");

                    Iterator brIter = branchList.iterator();

                    AdBranchDetails brDetails = (AdBranchDetails) brIter.next();
                    jbossQl.append("inv.invAdBranch=").append(brDetails.getBrCode());

                    while (brIter.hasNext()) {

                        brDetails = (AdBranchDetails) brIter.next();
                        jbossQl.append(" OR inv.invAdBranch=").append(brDetails.getBrCode());
                    }

                    int criteriaSize = 1;
                    int ctr = 0;
                    Object[] obj;

                    if (criteria.containsKey("dateTo")) {
                        criteriaSize++;
                    }
                    obj = new Object[criteriaSize];

                    jbossQl.append(") AND inv.invDate>=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateFrom");
                    ctr++;

                    if (criteria.containsKey("dateTo")) {

                        jbossQl.append("AND inv.invDate<=?").append(ctr + 1).append(" ");
                        obj[ctr] = criteria.get("dateTo");
                    }

                    jbossQl.append("AND inv.arCustomer.cstCustomerCode='").append(details.getSlpCstCustomerCode()).append("' AND inv.invCreditMemo=1 AND inv.invPosted = 1 AND inv.invVoid = 0 AND inv.invAdCompany=").append(AD_CMPNY).append(" ");

                    try {
                        Collection arPreviousCreditMemos = arInvoiceHome.getInvByCriteria(jbossQl.toString(), obj, 0, 0);

                        for (Object arPreviousCreditMemo : arPreviousCreditMemos) {
                            LocalArInvoice arPrevCreditMemo = (LocalArInvoice) arPreviousCreditMemo;
                            LocalArInvoice arPrevInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arPrevCreditMemo.getInvCmInvoiceNumber(), EJBCommon.FALSE, arPrevCreditMemo.getInvAdBranch(), AD_CMPNY);
                            if (arPrevInvoice.getInvDate().before((Date) criteria.get("dateFrom"))) {
                                arCreditMemos.add(arPrevCreditMemo);
                            }
                        }

                    } catch (FinderException ex) {

                    }
                }

                for (Object creditMemo : arCreditMemos) {

                    hasCreditMemo = true;

                    double creditMemoAmount = 0;
                    String creditMemoType = "";

                    LocalArInvoice arCreditMemo = (LocalArInvoice) creditMemo;

                    if (arCreditMemo.getInvPosted() == EJBCommon.TRUE) {

                        // get total credit memo amount

                        if (arCreditMemo.getArInvoiceLines().isEmpty() && arCreditMemo.getArInvoiceLineItems().isEmpty()) { // MEMO LINES

                            creditMemoType = "MEMO LINES";
                            creditMemoAmount = creditMemoAmount + arCreditMemo.getInvAmountDue();

                        } else if (!arCreditMemo.getArInvoiceLineItems().isEmpty() && arCreditMemo.getInvSubjectToCommission() == EJBCommon.TRUE) { // ITEMS

                            for (Object o : arCreditMemo.getArInvoiceLineItems()) {

                                LocalArInvoiceLineItem arCreditMemoLineItem = (LocalArInvoiceLineItem) o;
                                creditMemoAmount = creditMemoAmount + arCreditMemoLineItem.getIliAmount();
                            }
                        }

                        if (arCreditMemo.getInvSubjectToCommission() == EJBCommon.TRUE) { // MEMO LINE + SUBJECT TO COMMISSION

                            details.setSlpCommissionAmount(details.getSlpCommissionAmount() - creditMemoAmount);

                        } else if (creditMemoType.equalsIgnoreCase("MEMO LINES")) { // MEMO LINE + NOT SUBJECT TO COMMISSION

                            details.setSlpNonCommissionAmount(details.getSlpNonCommissionAmount() - creditMemoAmount);

                        } else { // ITEMS + NOT SUBJECT TO COMMISSION

                            for (Object o : arCreditMemo.getArInvoiceLineItems()) {

                                LocalArInvoiceLineItem arCreditMemoLineItem = (LocalArInvoiceLineItem) o;
                                LocalInvItemLocation invItemLocation = null;
                                LocalAdBranchItemLocation adBranchItemLocation = null;

                                try {

                                    invItemLocation = invItemLocationHome.findByPrimaryKey(arCreditMemoLineItem.getInvItemLocation().getIlCode());

                                } catch (FinderException ex) {

                                }

                                if (invItemLocation.getIlSubjectToCommission() == EJBCommon.TRUE) { // IL is subject to commission

                                    details.setSlpCommissionAmount(details.getSlpCommissionAmount() - arCreditMemoLineItem.getIliAmount());

                                } else { // branch IL might be subject to commission

                                    try {

                                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arCreditMemoLineItem.getInvItemLocation().getIlCode(), details.getSlpAdBranch(), AD_CMPNY);

                                        if (adBranchItemLocation.getBilSubjectToCommission() == EJBCommon.TRUE) { // branch IL is subject to commission

                                            details.setSlpCommissionAmount(details.getSlpCommissionAmount() - arCreditMemoLineItem.getIliAmount());

                                        } else { // branch IL is not subject to commission

                                            details.setSlpNonCommissionAmount(details.getSlpNonCommissionAmount() - arCreditMemoLineItem.getIliAmount());
                                        }
                                    } catch (FinderException ex) {

                                    }
                                }
                            }
                        }

                    } else {

                        break;
                    }
                }
            }

            grandTotalSales = grandTotalSales + details.getSlpCommissionAmount() + details.getSlpNonCommissionAmount();

            if (details.getSlpSubjectToCommission() == EJBCommon.TRUE) {

                if (hasCreditMemo == true) difference = originalNonCommAmount - details.getSlpNonCommissionAmount();

                details.setSlpCommissionAmount(details.getSlpCommissionAmount() + details.getSlpNonCommissionAmount());
                details.setSlpNonCommissionAmount(0d);

                details.setSlpCommissionAmount(details.getSlpCommissionAmount() + difference);
                details.setSlpNonCommissionAmount(details.getSlpNonCommissionAmount() - difference);
            }

            list.add(details);
        }

        for (Object o : tempList) {

            ArRepSalespersonDetails arRepSlpdetails = (ArRepSalespersonDetails) o;
            arRepSlpdetails.setSlpGrandTotalSales(grandTotalSales);
        }

        list.sort(ArRepSalespersonDetails.sortByCustomerCode);
        list.sort(ArRepSalespersonDetails.sortBySalespersonCode);

        if (GROUP_BY.equalsIgnoreCase("CUSTOMER CODE")) {

            list.sort(ArRepSalespersonDetails.sortByCustomerCode);

        } else if (GROUP_BY.equalsIgnoreCase("CUSTOMER TYPE")) {

            list.sort(ArRepSalespersonDetails.sortByCustomerType);
        }

        return list;
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ArRepSalespersonControllerBean ejbCreate");
    }
}