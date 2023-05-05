/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArInvoiceBatchPrintControllerBean
 * @created May 14, 2004, 5:24 PM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.ar.LocalArInvoiceHome;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ar.LocalArInvoice;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.*;
import com.util.mod.ar.ArModInvoiceDetails;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Stateless(name = "ArInvoiceBatchPrintControllerEJB")
public class ArInvoiceBatchPrintControllerBean extends EJBContextClass implements ArInvoiceBatchPrintController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;

    public byte getAdPrfEnableArInvoiceBatch(Integer AD_CMPNY) {
        Debug.print("ArInvoiceBatchPrintControllerBean getAdPrfEnableArInvoiceBatch");
        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            return adPreference.getPrfEnableArInvoiceBatch();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArInvByCriteria(HashMap criteria, Integer OFFSET, Integer LIMIT, String ORDER_BY, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArInvoiceBatchPrintControllerBean getArInvByCriteria");
        ArrayList list = new ArrayList();
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

                    jbossQl.append("inv.invAmountDue < inv.invAmountPaid OR inv.invAmountDue > inv.invAmountPaid ");
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
                mdetails.setInvCstCustomerCode(arInvoice.getArCustomer().getCstCustomerCode());
                mdetails.setInvDate(arInvoice.getInvDate());
                mdetails.setInvNumber(arInvoice.getInvNumber());
                mdetails.setInvReferenceNumber(arInvoice.getInvReferenceNumber());
                mdetails.setInvAmountDue(arInvoice.getInvAmountDue());
                mdetails.setInvAmountPaid(arInvoice.getInvAmountPaid());
                mdetails.setInvCreditMemo(arInvoice.getInvCreditMemo());

                list.add(mdetails);
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ArInvoiceBatchPrintControllerBean ejbCreate");
    }

}