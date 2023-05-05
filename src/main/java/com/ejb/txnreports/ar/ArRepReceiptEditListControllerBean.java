/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArRepReceiptEditListControllerBean
 * @created June 25, 2004, 1:30 PM
 * @author Neil Andrew M. Ajero
 */
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
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ar.LocalArAppliedInvoice;
import com.ejb.entities.ar.LocalArDistributionRecord;
import com.ejb.entities.ar.LocalArReceipt;
import com.ejb.dao.ar.LocalArReceiptHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ar.ArRepReceiptEditListDetails;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ArRepReceiptEditListControllerEJB")
public class ArRepReceiptEditListControllerBean extends EJBContextClass implements ArRepReceiptEditListController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;

    public ArrayList executeArRepReceiptEditList(ArrayList rctCodeList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepReceiptPrintControllerBean executeArRepReceiptEditList");

        ArrayList list = new ArrayList();

        try {

            for (Object o : rctCodeList) {

                Integer RCT_CODE = (Integer) o;

                LocalArReceipt arReceipt = null;

                try {

                    arReceipt = arReceiptHome.findByPrimaryKey(RCT_CODE);

                } catch (FinderException ex) {

                    continue;
                }

                // get receipt distribution records

                Collection arDistributionRecords = arReceipt.getArDistributionRecords();

                for (Object distributionRecord : arDistributionRecords) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                    ArRepReceiptEditListDetails details = new ArRepReceiptEditListDetails();

                    details.setRelRctBatchName(arReceipt.getArReceiptBatch() != null ? arReceipt.getArReceiptBatch().getRbName() : "N/A");
                    details.setRelRctBatchDescription(arReceipt.getArReceiptBatch() != null ? arReceipt.getArReceiptBatch().getRbDescription() : "N/A");
                    details.setRelRctTransactionTotal(arReceipt.getArReceiptBatch() != null ? arReceipt.getArReceiptBatch().getArReceipts().size() : 0);
                    details.setRelRctDateCreated(arReceipt.getRctDateCreated());
                    details.setRelRctCreatedBy(arReceipt.getRctCreatedBy());
                    details.setRelRctNumber(arReceipt.getRctNumber());
                    details.setRelRctDate(arReceipt.getRctDate());
                    details.setRelCstCustomerCode(arReceipt.getArCustomer().getCstCustomerCode());
                    details.setRelCstCustomerName(arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName());
                    details.setRelRctReferenceNumber(arReceipt.getRctReferenceNumber());
                    details.setRelRctDescription(arReceipt.getRctDescription());
                    details.setRelRctAmount(arReceipt.getRctAmount());
                    details.setRelDrAccountNumber(arDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                    details.setRelDrAccountDescription(arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());
                    details.setRelDrClass(arDistributionRecord.getDrClass());
                    details.setRelDrDebit(arDistributionRecord.getDrDebit());
                    details.setRelDrAmount(arDistributionRecord.getDrAmount());

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

    public ArrayList executeArRepReceiptEditListSub(ArrayList rctCodeList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepReceiptPrintControllerBean executeArRepReceiptEditListSub");

        ArrayList list = new ArrayList();

        try {

            for (Object o : rctCodeList) {

                Integer RCT_CODE = (Integer) o;

                LocalArReceipt arReceipt = null;

                try {

                    arReceipt = arReceiptHome.findByPrimaryKey(RCT_CODE);

                } catch (FinderException ex) {

                    continue;
                }

                // get receipt distribution records

                Collection arAppliedInvoices = arReceipt.getArAppliedInvoices();

                for (Object appliedInvoice : arAppliedInvoices) {

                    LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) appliedInvoice;

                    ArRepReceiptEditListDetails details = new ArRepReceiptEditListDetails();

                    details.setRelRctNumber(arReceipt.getRctNumber());
                    details.setRelInvNumber(arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvNumber());
                    details.setRelInvAmountApplied(arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiCreditableWTax() + arAppliedInvoice.getAiDiscountAmount());

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

        Debug.print("ApRepVoucherPrintControllerBean getAdCompany");

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

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApRepVoucherPrintControllerBean ejbCreate");
    }
}