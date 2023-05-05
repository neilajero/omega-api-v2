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
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ap.LocalApDistributionRecord;
import com.ejb.entities.ap.LocalApVoucher;
import com.ejb.dao.ap.LocalApVoucherHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ap.ApRepVoucherEditListDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ApRepVoucherEditListControllerEJB")
public class ApRepVoucherEditListControllerBean extends EJBContextClass implements ApRepVoucherEditListController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalApVoucherHome apVoucherHome;

    public ArrayList executeApRepVoucherEditList(ArrayList vouCodeList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepVoucherPrintControllerBean executeApRepVoucherEditList");

        ArrayList list = new ArrayList();

        try {

            for (Object o : vouCodeList) {

                Integer VOU_CODE = (Integer) o;

                LocalApVoucher apVoucher = null;

                try {

                    apVoucher = apVoucherHome.findByPrimaryKey(VOU_CODE);

                } catch (FinderException ex) {

                    continue;
                }

                // get voucher distribution records

                Collection apDistributionRecords = apVoucher.getApDistributionRecords();

                Iterator drIter = apDistributionRecords.iterator();
                LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

                while (drIter.hasNext()) {

                    LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) drIter.next();

                    ApRepVoucherEditListDetails details = new ApRepVoucherEditListDetails();

                    if (apVoucher.getVouDebitMemo() == EJBCommon.FALSE) {

                        details.setVelVouType("VOU");
                        details.setVelVouAmountDue(apVoucher.getVouAmountDue());
                        details.setVelWithholdingTaxCode(apVoucher.getApWithholdingTaxCode().getWtcName());
                        details.setVelCurrencySymbol(apVoucher.getGlFunctionalCurrency().getFcSymbol());

                    } else {

                        details.setVelVouType("DM");
                        details.setVelVouAmountDue(apVoucher.getVouBillAmount());
                    }

                    details.setVelVouDateCreated(apVoucher.getVouDateCreated());
                    details.setVelVouCreatedBy(apVoucher.getVouCreatedBy());
                    details.setVelVouDocumentNumber(apVoucher.getVouDocumentNumber());
                    details.setVelVouReferenceNumber(apVoucher.getVouReferenceNumber());
                    details.setVelVouDate(apVoucher.getVouDate());
                    details.setVelSplSupplierCode(apVoucher.getApSupplier().getSplSupplierCode());
                    details.setVelVouBatchName(apVoucher.getApVoucherBatch() != null ? apVoucher.getApVoucherBatch().getVbName() : "N/A");
                    details.setVelVouBatchDescription(apVoucher.getApVoucherBatch() != null ? apVoucher.getApVoucherBatch().getVbDescription() : "N/A");
                    details.setVelVouTransactionTotal(apVoucher.getApVoucherBatch() != null ? apVoucher.getApVoucherBatch().getApVouchers().size() : 0);
                    details.setVelDrAccountNumber(apDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                    details.setVelDrClass(apDistributionRecord.getDrClass());
                    details.setVelDrDebit(apDistributionRecord.getDrDebit());
                    details.setVelDrAmount(apDistributionRecord.getDrAmount());
                    details.setVelDrAccountDescription(apDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());
                    details.setVelVouDescription(apVoucher.getVouDescription());

                    details.setVelSplSupplierName(apVoucher.getApSupplier().getSplName());

                    if (apVoucher.getVouApprovedRejectedBy() == null || apVoucher.getVouApprovedRejectedBy().equals("")) {

                        details.setVelApprovedBy(adPreference.getPrfApDefaultApprover());

                    } else {

                        details.setVelApprovedBy(apVoucher.getVouApprovedRejectedBy());
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
            details.setCmpCountry(adCompany.getCmpCountry());
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