package com.ejb.txn.ar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdLookUpValue;
import com.ejb.dao.ad.LocalAdLookUpValueHome;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ar.LocalArCustomer;
import com.ejb.dao.ar.LocalArCustomerHome;
import com.ejb.entities.ar.LocalArInvoice;
import com.ejb.entities.ar.LocalArInvoiceBatch;
import com.ejb.dao.ar.LocalArInvoiceBatchHome;
import com.ejb.dao.ar.LocalArInvoiceHome;
import com.ejb.entities.ar.LocalArInvoiceLineItem;
import com.ejb.entities.ar.LocalArJobOrderInvoiceLine;
import com.ejb.entities.ar.LocalArSalesOrderInvoiceLine;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
import com.ejb.dao.inv.LocalInvTagHome;
import com.util.mod.ar.ArModInvoiceDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;

@Stateless(name = "ArFindInvoiceControllerEJB")
public class ArFindInvoiceControllerBean extends EJBContextClass implements ArFindInvoiceController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalArInvoiceBatchHome arInvoiceBatchHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalInvTagHome invTagHome;

    public ArrayList getAdLvCustomerBatchAll(Integer AD_CMPNY) {
        Debug.print("ArFindInvoiceControllerBean getAdLvCustomerBatchAll");

        ArrayList list = new ArrayList();
        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AR CUSTOMER BATCH - SOA", AD_CMPNY);
            for (Object lookUpValue : adLookUpValues) {
                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;
                list.add(adLookUpValue.getLvName());
            }
            return list;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArCstAll(Integer AD_BRNCH, Integer AD_CMPNY) {
        Debug.print("ArFindInvoiceControllerBean getArCstAll");

        ArrayList list = new ArrayList();
        try {
            Collection arCustomers = arCustomerHome.findEnabledCstAll(AD_BRNCH, AD_CMPNY);
            for (Object customer : arCustomers) {
                LocalArCustomer arCustomer = (LocalArCustomer) customer;
                list.add(arCustomer.getCstCustomerCode());
            }
            return list;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGlFcAll(Integer AD_CMPNY) {
        Debug.print("ArFindInvoiceControllerBean getGlFcAll");

        ArrayList list = new ArrayList();
        try {
            Collection glFunctionalCurrencies = glFunctionalCurrencyHome.findFcAll(AD_CMPNY);
            for (Object functionalCurrency : glFunctionalCurrencies) {
                LocalGlFunctionalCurrency glFunctionalCurrency = (LocalGlFunctionalCurrency) functionalCurrency;
                list.add(glFunctionalCurrency.getFcName());
            }
            return list;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArOpenIbAll(Integer AD_BRNCH, Integer AD_CMPNY) {
        Debug.print("ApFindVoucherControllerBean getArOpenIbAll");

        ArrayList list = new ArrayList();
        try {
            Collection arInvoiceBatches = arInvoiceBatchHome.findOpenIbAll(AD_BRNCH, AD_CMPNY);
            for (Object invoiceBatch : arInvoiceBatches) {
                LocalArInvoiceBatch arInvoiceBatch = (LocalArInvoiceBatch) invoiceBatch;
                list.add(arInvoiceBatch.getIbName());
            }
            return list;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdLvInvShiftAll(Integer AD_CMPNY) {
        Debug.print("ArFindInvoiceControllerBean getAdLvInvShiftAll");

        ArrayList list = new ArrayList();
        try {
            Collection adLookUpValues = adLookUpValueHome.findByLuName("INV SHIFT", AD_CMPNY);
            for (Object lookUpValue : adLookUpValues) {
                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;
                list.add(adLookUpValue.getLvName());
            }
            return list;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfEnableArInvoiceBatch(Integer AD_CMPNY) {
        Debug.print("ApFindVoucherControllerBean getAdPrfEnableArInvoiceBatch");

        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            return adPreference.getPrfEnableArInvoiceBatch();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfEnableInvShift(Integer AD_CMPNY) {
        Debug.print("ArFindInvoiceControllerBean getAdPrfEnableInvShift");

        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            return adPreference.getPrfInvEnableShift();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArInvByCriteria(HashMap criteria, Integer OFFSET, Integer LIMIT, String ORDER_BY, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArFindInvoiceControllerBean getArInvByCriteria");

        ArrayList list = new ArrayList();

        try {

            String invoiceType = "";
            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(inv) FROM ArInvoice inv ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();
            boolean creditMemo = false;
            String serialNumber = "";

            Object[] obj;

            // Allocate the size of the object parameter
            if (criteria.containsKey("referenceNumber")) {
                criteriaSize--;
            }

            if (criteria.containsKey("paymentStatus")) {
                criteriaSize--;
            }

            if (criteria.containsKey("serialNumber")) {
                criteriaSize--;
            }

            if (criteria.containsKey("approvalStatus")) {
                String approvalStatus = (String) criteria.get("approvalStatus");
                if (approvalStatus.equals("DRAFT") || approvalStatus.equals("REJECTED")) {
                    criteriaSize--;
                }
            }

            obj = new Object[criteriaSize];
            if (criteria.containsKey("referenceNumber")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("inv.invReferenceNumber LIKE '%").append(criteria.get("referenceNumber")).append("%' ");
            }

            if (criteria.containsKey("serialNumber")) {
                serialNumber = (String) criteria.get("serialNumber");
            }

            if (criteria.containsKey("invoiceType")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("inv.invType=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("invoiceType");
                ctr++;
            }

            if (criteria.containsKey("batchName")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("inv.arInvoiceBatch.ibName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("batchName");
                ctr++;
            }

            if (criteria.containsKey("customerBatch")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("inv.arCustomer.cstCustomerBatch=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerBatch");
                ctr++;
            }

            if (criteria.containsKey("shift")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("inv.invLvShift=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("shift");
                ctr++;
            }

            if (criteria.containsKey("customerCode")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("inv.arCustomer.cstCustomerCode=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerCode");
                ctr++;
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("inv.invCreditMemo=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("creditMemo");
            creditMemo = (Byte) criteria.get("creditMemo") != 0;
            ctr++;

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("inv.invVoid=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("invoiceVoid");
            ctr++;

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

            if (criteria.containsKey("currency")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("inv.glFunctionalCurrency.fcName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("currency");
                ctr++;
            }

            if (criteria.containsKey("approvalStatus")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                String approvalStatus = (String) criteria.get("approvalStatus");
                if (approvalStatus.equals("DRAFT")) {
                    jbossQl.append("inv.invApprovalStatus IS NULL ");
                } else if (approvalStatus.equals("REJECTED")) {
                    jbossQl.append("inv.invReasonForRejection IS NOT NULL ");
                } else {
                    jbossQl.append("inv.invApprovalStatus=?").append(ctr + 1).append(" ");
                    obj[ctr] = approvalStatus;
                    ctr++;
                }
            }

            if (criteria.containsKey("posted")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("inv.invPosted=?").append(ctr + 1).append(" ");

                String posted = (String) criteria.get("posted");
                if (posted.equals("YES")) {
                    obj[ctr] = EJBCommon.TRUE;
                } else {
                    obj[ctr] = EJBCommon.FALSE;
                }
                ctr++;
            }

            if (criteria.containsKey("interest")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("inv.invInterest=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("interest");
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
                    jbossQl.append("inv.invAmountDue=inv.invAmountPaid ");
                } else if (paymentStatus.equals("UNPAID")) {
                    jbossQl.append("(inv.invAmountDue <> inv.invAmountPaid) ");
                }
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("inv.invAdBranch=").append(AD_BRNCH).append(" AND inv.invAdCompany=").append(AD_CMPNY).append(" ");
            String orderBy = null;

            if (ORDER_BY.equals("CUSTOMER CODE")) {
                orderBy = "inv.arCustomer.cstCustomerCode";
            } else if (ORDER_BY.equals("INVOICE NUMBER")) {
                orderBy = "inv.invNumber";
            }

            if (orderBy != null) {
                jbossQl.append("ORDER BY ").append(orderBy).append(", inv.invDate");
            } else {
                jbossQl.append("ORDER BY inv.invDate");
            }

            Collection arInvoices = arInvoiceHome.getInvByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);
            if (arInvoices.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            for (Object invoice : arInvoices) {

                LocalArInvoice arInvoice = (LocalArInvoice) invoice;

                ArModInvoiceDetails mdetails = new ArModInvoiceDetails();
                mdetails.setInvCode(arInvoice.getInvCode());
                mdetails.setInvCstName(arInvoice.getArCustomer().getCstName());
                mdetails.setInvDate(arInvoice.getInvDate());
                mdetails.setInvNumber(arInvoice.getInvNumber());
                mdetails.setInvReferenceNumber(arInvoice.getInvReferenceNumber());
                mdetails.setInvAmountDue(arInvoice.getInvAmountDue());
                mdetails.setInvAmountPaid(arInvoice.getInvAmountPaid());
                mdetails.setInvCreditMemo(arInvoice.getInvCreditMemo());
                mdetails.setInvPosted(arInvoice.getInvPosted());
                mdetails.setInvCstCustomerCode(arInvoice.getArCustomer().getCstCustomerCode());
                LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(arInvoice.getInvAdBranch());
                mdetails.setInvBranchCode(adBranch.getBrBranchCode());

                String status = arInvoice.getInvApprovalStatus();
                if (arInvoice.getInvVoid() == EJBCommon.TRUE) {
                    status = "VOID";
                } else if (arInvoice.getInvPosted() == EJBCommon.FALSE) {
                    status = "DRAFT";
                } else if (status.equals("N/A")) {
                    status = "POSTED";
                }
                mdetails.setInvStatus(status);

                try {
                    // in case of error of null invoice ar batch name
                    mdetails.setInvArBatchName(arInvoice.getArInvoiceBatch().getIbName());
                } catch (Exception ex) {
                    mdetails.setInvArBatchName("");
                }

                mdetails.setInvType(arInvoice.getInvType());
                if (!serialNumber.trim().equals("")) {
                    if (this.searchSerialNumber(arInvoice, serialNumber)) {
                        list.add(mdetails);
                    }
                } else {
                    list.add(mdetails);
                }
            }
            return list;

        } catch (GlobalNoRecordFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer getArInvSizeByCriteria(HashMap criteria, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArFindInvoiceControllerBean getArInvSizeByCriteria");

        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(inv) FROM ArInvoice inv ");

            boolean firstArgument = true;

            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter
            if (criteria.containsKey("referenceNumber")) {
                criteriaSize--;
            }

            if (criteria.containsKey("paymentStatus")) {
                criteriaSize--;
            }

            if (criteria.containsKey("approvalStatus")) {
                String approvalStatus = (String) criteria.get("approvalStatus");
                if (approvalStatus.equals("DRAFT") || approvalStatus.equals("REJECTED")) {
                    criteriaSize--;
                }
            }

            obj = new Object[criteriaSize];
            if (criteria.containsKey("referenceNumber")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("inv.invReferenceNumber LIKE '%").append(criteria.get("referenceNumber")).append("%' ");
            }

            if (criteria.containsKey("invoiceType")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("inv.invType=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("invoiceType");
                ctr++;
            }

            if (criteria.containsKey("batchName")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("inv.arInvoiceBatch.ibName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("batchName");
                ctr++;
            }

            if (criteria.containsKey("customerBatch")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("inv.arCustomer.cstCustomerBatch=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerBatch");
                ctr++;
            }

            if (criteria.containsKey("shift")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("inv.invLvShift=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("shift");
                ctr++;
            }

            if (criteria.containsKey("customerCode")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("inv.arCustomer.cstCustomerCode=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerCode");
                ctr++;
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("inv.invCreditMemo=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("creditMemo");
            ctr++;

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("inv.invVoid=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("invoiceVoid");
            ctr++;

            if (criteria.containsKey("interest")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("inv.invInterest=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("interest");
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

            if (criteria.containsKey("currency")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("inv.glFunctionalCurrency.fcName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("currency");
                ctr++;
            }

            if (criteria.containsKey("approvalStatus")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                String approvalStatus = (String) criteria.get("approvalStatus");
                if (approvalStatus.equals("DRAFT")) {
                    jbossQl.append("inv.invApprovalStatus IS NULL ");
                } else if (approvalStatus.equals("REJECTED")) {
                    jbossQl.append("inv.invReasonForRejection IS NOT NULL ");
                } else {
                    jbossQl.append("inv.invApprovalStatus=?").append(ctr + 1).append(" ");
                    obj[ctr] = approvalStatus;
                    ctr++;
                }
            }

            if (criteria.containsKey("posted")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("inv.invPosted=?").append(ctr + 1).append(" ");
                String posted = (String) criteria.get("posted");

                if (posted.equals("YES")) {
                    obj[ctr] = EJBCommon.TRUE;
                } else {
                    obj[ctr] = EJBCommon.FALSE;
                }
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
                    jbossQl.append("inv.invAmountDue=inv.invAmountPaid ");
                } else if (paymentStatus.equals("UNPAID")) {
                    jbossQl.append("(inv.invAmountDue <> inv.invAmountPaid) ");
                }
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("inv.invAdBranch=").append(AD_BRNCH).append(" AND inv.invAdCompany=").append(AD_CMPNY).append(" ");

            Collection arInvoices = arInvoiceHome.getInvByCriteria(jbossQl.toString(), obj, 0, 0);
            if (arInvoices.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }
            return arInvoices.size();

        } catch (GlobalNoRecordFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    private boolean searchSerialNumber(LocalArInvoice arInvoice, String serialNumber) throws FinderException {
        Debug.print("ArFindInvoiceControllerBean searchSerialNumber");

        switch (arInvoice.getInvType()) {
            case "ITEMS":
                Collection arInvoiceLineItems = arInvoice.getArInvoiceLineItems();
                for (Object invoiceLineItem : arInvoiceLineItems) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) invoiceLineItem;
                    if (arInvoiceLineItem.getInvTags().size() > 0) {
                        Collection invTags = invTagHome.findTgSerialNumberByIliCode(serialNumber, arInvoiceLineItem.getIliCode());
                        if (invTags.size() > 0) {
                            return true;
                        }
                    }
                }
                break;
            case "SO MATCHED":
                Collection arSalesOrderInvoiceLines = arInvoice.getArSalesOrderInvoiceLines();
                for (Object salesOrderInvoiceLine : arSalesOrderInvoiceLines) {
                    LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) salesOrderInvoiceLine;
                    if (arSalesOrderInvoiceLine.getInvTags().size() > 0) {
                        Collection invTags = invTagHome.findTgSerialNumberBySilCode(serialNumber, arSalesOrderInvoiceLine.getSilCode());
                        if (invTags.size() > 0) {
                            return true;
                        }
                    }
                }
                break;
            case "JO MATCHED":
                Collection arJobOrderInvoiceLines = arInvoice.getArJobOrderInvoiceLines();
                for (Object jobOrderInvoiceLine : arJobOrderInvoiceLines) {
                    LocalArJobOrderInvoiceLine arJobOrderInvoiceLine = (LocalArJobOrderInvoiceLine) jobOrderInvoiceLine;
                    if (arJobOrderInvoiceLine.getInvTags().size() > 0) {
                        Collection invTags = invTagHome.findTgSerialNumberByIliCode(serialNumber, arJobOrderInvoiceLine.getJilCode());
                        if (invTags.size() > 0) {
                            return true;
                        }
                    }
                }
                break;
        }
        return false;
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {
        Debug.print("ArFindInvoiceControllerBean getGlFcPrecisionUnit");

        try {
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            return adCompany.getGlFunctionalCurrency().getFcPrecision();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {
        Debug.print("ArFindInvoiceControllerBean ejbCreate");
    }
}