/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArRepPdcPrintControllerBean
 * @created July 17, 2008, 6:26 PM
 * @author Reginald Cris Pasco, Ariel Joseph De Guzman
 */
package com.ejb.txnreports.ar;

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
import com.ejb.entities.ad.LocalAdApproval;
import com.ejb.entities.ad.LocalAdApprovalDocument;
import com.ejb.dao.ad.LocalAdApprovalDocumentHome;
import com.ejb.dao.ad.LocalAdApprovalHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ar.LocalArAppliedInvoice;
import com.ejb.dao.ar.LocalArDistributionRecordHome;
import com.ejb.entities.ar.LocalArPdc;
import com.ejb.dao.ar.LocalArPdcHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;
import com.util.mod.ar.ArModPdcDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ArRepPdcPrintControllerEJB")
public class ArRepPdcPrintControllerBean extends EJBContextClass implements ArRepPdcPrintController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdApprovalDocumentHome adApprovalDocumentHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalArPdcHome arPdcHome;


    public ArrayList executeArRepPdcPrint(ArrayList pdcCodeList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepPdcPrintControllerBean executeArRepPdcPrint");

        ArrayList list = new ArrayList();

        try {

            for (Object o : pdcCodeList) {

                Integer PDC_CODE = (Integer) o;

                LocalArPdc arPdc = null;

                try {

                    arPdc = arPdcHome.findByPrimaryKey(PDC_CODE);

                } catch (FinderException ex) {

                    continue;
                }

                Collection arPdcAppliedInvoices = arPdc.getArAppliedInvoices();

                if (!arPdcAppliedInvoices.isEmpty()) {

                    for (Object arPdcAppliedInvoice : arPdcAppliedInvoices) {

                        StringBuilder invoiceNumber = new StringBuilder();

                        LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) arPdcAppliedInvoice;

                        LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(AD_CMPNY);
                        LocalAdApprovalDocument adApprovalDocument = adApprovalDocumentHome.findByAdcType("AR RECEIPT", AD_CMPNY);

                        if (adApprovalDocument.getAdcPrintOption().equals("PRINT APPROVED ONLY")) {

                            if (arPdc.getPdcApprovalStatus() == null || arPdc.getPdcApprovalStatus().equals("PENDING")) {

                                continue;
                            }

                        } else if (adApprovalDocument.getAdcPrintOption().equals("PRINT UNAPPROVED ONLY")) {

                            if (arPdc.getPdcApprovalStatus() != null && (arPdc.getPdcApprovalStatus().equals("N/A") || arPdc.getPdcApprovalStatus().equals("APPROVED"))) {

                                continue;
                            }
                        }

                        if (adApprovalDocument.getAdcAllowDuplicate() == EJBCommon.FALSE && arPdc.getPdcPrinted() == EJBCommon.TRUE) {

                            continue;
                        }

                        // show duplicate

                        boolean showDuplicate = adApprovalDocument.getAdcTrackDuplicate() == EJBCommon.TRUE && arPdc.getPdcPrinted() == EJBCommon.TRUE;

                        // set printed

                        arPdc.setPdcPrinted(EJBCommon.TRUE);

                        ArModPdcDetails mdetails = new ArModPdcDetails();

                        mdetails.setPdcCode(arPdc.getPdcCode());
                        mdetails.setPdcCheckNumber(arPdc.getPdcCheckNumber());
                        mdetails.setPdcReferenceNumber(arPdc.getPdcReferenceNumber());
                        mdetails.setPdcDateReceived(arPdc.getPdcDateReceived());
                        mdetails.setPdcMaturityDate(arPdc.getPdcMaturityDate());
                        mdetails.setPdcDescription(arPdc.getPdcDescription());
                        mdetails.setPdcAmount(arPdc.getPdcAmount());
                        mdetails.setPdcCstCustomerCode(arPdc.getArCustomer().getCstCustomerCode());
                        mdetails.setPdcCstName(arPdc.getArCustomer().getCstName());
                        mdetails.setPdcCstTin(arPdc.getArCustomer().getCstTin());
                        mdetails.setPdcCstAddress(arPdc.getArCustomer().getCstAddress());
                        mdetails.setRctBaName(arPdc.getAdBankAccount().getBaName());
                        mdetails.setPdcBankName(arPdc.getAdBankAccount().getAdBank().getBnkName() + " - " + arPdc.getAdBankAccount().getAdBank().getBnkBranch());

                        Collection arAppliedInvoices = arPdc.getArAppliedInvoices();
                        Iterator x = arAppliedInvoices.iterator();

                        while (x.hasNext()) {

                            LocalArAppliedInvoice arAppliedInvoiceNumber = (LocalArAppliedInvoice) x.next();

                            if (x.hasNext())
                                invoiceNumber.append(arAppliedInvoiceNumber.getArInvoicePaymentSchedule().getArInvoice().getInvNumber()).append(", ");
                            else
                                invoiceNumber.append(arAppliedInvoiceNumber.getArInvoicePaymentSchedule().getArInvoice().getInvNumber());
                        }

                        mdetails.setPdcInvoiceNumbers(invoiceNumber.toString());

                        list.add(mdetails);
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

        Debug.print("ArRepPdcPrintControllerBean getAdCompany");

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

    private short getGlFcPrecisionUnit(Integer AD_CMPNY) {

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

        Debug.print("ArRepPdcPrintControllerBean ejbCreate");
    }
}