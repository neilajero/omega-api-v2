package com.ejb.txnreports.ap;

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
import com.ejb.entities.ap.LocalApAppliedVoucher;
import com.ejb.entities.ap.LocalApCheck;
import com.ejb.dao.ap.LocalApCheckHome;
import com.ejb.entities.ap.LocalApDistributionRecord;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.dao.gen.LocalGenSegmentHome;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ap.ApRepCheckEditListDetails;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ApRepCheckEditListControllerEJB")
public class ApRepCheckEditListControllerBean extends EJBContextClass implements ApRepCheckEditListController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalApCheckHome apCheckHome;
    @EJB
    private LocalGenSegmentHome genSegmentHome;

    public ArrayList executeApRepCheckEditList(ArrayList chkCodeList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepCheckPrintControllerBean executeApRepCheckEditList");

        ArrayList list = new ArrayList();

        try {

            for (Object o : chkCodeList) {

                Integer CHK_CODE = (Integer) o;

                LocalApCheck apCheck = null;

                try {

                    apCheck = apCheckHome.findByPrimaryKey(CHK_CODE);

                } catch (FinderException ex) {

                    continue;
                }

                // get check distribution records

                Collection apDistributionRecords = apCheck.getApDistributionRecords();

                for (Object distributionRecord : apDistributionRecords) {

                    LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) distributionRecord;

                    ApRepCheckEditListDetails details = new ApRepCheckEditListDetails();

                    details.setCelChkBatchName(apCheck.getApCheckBatch() != null ? apCheck.getApCheckBatch().getCbName() : "N/A");
                    details.setCelChkBatchDescription(apCheck.getApCheckBatch() != null ? apCheck.getApCheckBatch().getCbDescription() : "N/A");
                    details.setCelChkTransactionTotal(apCheck.getApCheckBatch() != null ? apCheck.getApCheckBatch().getApChecks().size() : 0);
                    details.setCelChkDateCreated(apCheck.getChkDateCreated());
                    details.setCelChkCreatedBy(apCheck.getChkCreatedBy());
                    details.setCelChkNumber(apCheck.getChkNumber());
                    details.setCelChkDescription(apCheck.getChkDescription());
                    details.setCelChkDocumentNumber(apCheck.getChkDocumentNumber());
                    details.setCelChkDate(apCheck.getChkDate());
                    details.setCelSplSupplierCode(apCheck.getApSupplier().getSplSupplierCode());
                    details.setCelSplSupplierName(apCheck.getChkSupplierName() == null ? apCheck.getApSupplier().getSplName() : apCheck.getChkSupplierName());
                    details.setCelChkAdBaName(apCheck.getAdBankAccount().getBaName());
                    details.setCelChkAmount(apCheck.getChkAmount());
                    details.setCelDrAccountNumber(apDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                    details.setCelDrAccountDescription(apDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());
                    details.setCelDrClass(apDistributionRecord.getDrClass());
                    details.setCelDrDebit(apDistributionRecord.getDrDebit());
                    details.setCelDrAmount(apDistributionRecord.getDrAmount());

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

    public ArrayList executeApRepCheckEditListSub(ArrayList chkCodeList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepCheckPrintControllerBean executeApRepCheckEditListSub");

        ArrayList list = new ArrayList();

        try {

            for (Object o : chkCodeList) {

                Integer CHK_CODE = (Integer) o;

                LocalApCheck apCheck = null;

                try {

                    apCheck = apCheckHome.findByPrimaryKey(CHK_CODE);

                } catch (FinderException ex) {

                    continue;
                }

                Collection apAppliedVouchers = apCheck.getApAppliedVouchers();

                for (Object appliedVoucher : apAppliedVouchers) {

                    LocalApAppliedVoucher apAppliedVoucher = (LocalApAppliedVoucher) appliedVoucher;

                    ApRepCheckEditListDetails details = new ApRepCheckEditListDetails();

                    details.setCelChkDocumentNumber(apCheck.getChkDocumentNumber());
                    details.setCelVouNumber(apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouDocumentNumber());
                    details.setCelAppliedAmount(apAppliedVoucher.getAvApplyAmount() + apAppliedVoucher.getAvTaxWithheld() + apAppliedVoucher.getAvDiscountAmount());

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