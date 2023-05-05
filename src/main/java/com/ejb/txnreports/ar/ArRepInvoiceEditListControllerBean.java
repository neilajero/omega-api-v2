/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArRepInvoiceEditListControllerBean
 * @created June 25, 2004, 9:36 AM
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
import com.ejb.entities.ar.LocalArDistributionRecord;
import com.ejb.entities.ar.LocalArInvoice;
import com.ejb.dao.ar.LocalArInvoiceHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ar.ArRepInvoiceEditListDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ArRepInvoiceEditListControllerEJB")
public class ArRepInvoiceEditListControllerBean extends EJBContextClass implements ArRepInvoiceEditListController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;

    public ArrayList executeArRepInvoiceEditList(ArrayList invCodeList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepInvoicePrintControllerBean executeArRepInvoiceEditList");

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

                // get invoice distribution records

                Collection arDistributionRecords = arInvoice.getArDistributionRecords();

                for (Object distributionRecord : arDistributionRecords) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                    ArRepInvoiceEditListDetails details = new ArRepInvoiceEditListDetails();

                    if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {

                        details.setIelInvType("INV");
                        details.setIelInvAmountDue(arInvoice.getInvAmountDue());

                    } else {

                        details.setIelInvType("CM");
                        details.setIelInvAmountDue(arInvoice.getInvAmountDue());
                    }

                    details.setIelInvDateCreated(arInvoice.getInvDateCreated());
                    details.setIelInvCreatedBy(arInvoice.getInvCreatedBy());
                    details.setIelInvDocumentNumber(arInvoice.getInvNumber());
                    details.setIelInvDate(arInvoice.getInvDate());
                    details.setIelCstCustomerCode(arInvoice.getArCustomer().getCstCustomerCode());
                    details.setIelCstCustomerName(arInvoice.getArCustomer().getCstName());
                    details.setIelInvBatchName(arInvoice.getArInvoiceBatch() != null ? arInvoice.getArInvoiceBatch().getIbName() : "N/A");
                    details.setIelInvBatchDescription(arInvoice.getArInvoiceBatch() != null ? arInvoice.getArInvoiceBatch().getIbDescription() : "N/A");
                    details.setIelInvTransactionTotal(arInvoice.getArInvoiceBatch() != null ? arInvoice.getArInvoiceBatch().getArInvoices().size() : 0);
                    details.setIelDrAccountNumber(arDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                    details.setIelDrAccountDescription(arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());
                    details.setIelDrClass(arDistributionRecord.getDrClass());
                    details.setIelDrDebit(arDistributionRecord.getDrDebit());
                    details.setIelDrAmount(arDistributionRecord.getDrAmount());
                    details.setIelInvDescription(arInvoice.getInvDescription());

                    try {
                        details.setIelSalesPersonCode(arInvoice.getArSalesperson().getSlpSalespersonCode());
                        Debug.print("arInvoice.getArSalesperson().getSlpSalespersonCode(): " + arInvoice.getArSalesperson().getSlpSalespersonCode());
                    } catch (Exception e) {

                    }

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

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApRepVoucherPrintControllerBean ejbCreate");
    }
}