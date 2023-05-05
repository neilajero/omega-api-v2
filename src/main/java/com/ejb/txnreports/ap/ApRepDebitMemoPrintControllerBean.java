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
import com.ejb.entities.ad.LocalAdApproval;
import com.ejb.entities.ad.LocalAdApprovalDocument;
import com.ejb.dao.ad.LocalAdApprovalDocumentHome;
import com.ejb.dao.ad.LocalAdApprovalHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ad.LocalAdUser;
import com.ejb.dao.ad.LocalAdUserHome;
import com.ejb.entities.ap.LocalApDistributionRecord;
import com.ejb.dao.ap.LocalApDistributionRecordHome;
import com.ejb.entities.ap.LocalApVoucher;
import com.ejb.dao.ap.LocalApVoucherHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ap.ApRepDebitMemoPrintDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ApRepDebitMemoPrintControllerEJB")
public class ApRepDebitMemoPrintControllerBean extends EJBContextClass implements ApRepDebitMemoPrintController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdApprovalDocumentHome adApprovalDocumentHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    private LocalApVoucherHome apVoucherHome;


    public ArrayList executeApRepDebitMemoPrint(ArrayList vouCodeList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepDebitMemoPrintControllerBean executeApRepDebitMemoPrint");

        ArrayList list = new ArrayList();

        try {

            for (Object o : vouCodeList) {

                Integer VOU_CODE = (Integer) o;

                LocalApVoucher apDebitMemo = null;

                try {

                    apDebitMemo = apVoucherHome.findByPrimaryKey(VOU_CODE);

                } catch (FinderException ex) {

                    continue;
                }

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(AD_CMPNY);
                LocalAdApprovalDocument adApprovalDocument = adApprovalDocumentHome.findByAdcType("AP DEBIT MEMO", AD_CMPNY);

                if (adApprovalDocument.getAdcPrintOption().equals("PRINT APPROVED ONLY")) {

                    if (apDebitMemo.getVouApprovalStatus() == null || apDebitMemo.getVouApprovalStatus().equals("PENDING")) {

                        continue;
                    }

                } else if (adApprovalDocument.getAdcPrintOption().equals("PRINT UNAPPROVED ONLY")) {

                    if (apDebitMemo.getVouApprovalStatus() != null && (apDebitMemo.getVouApprovalStatus().equals("N/A") || apDebitMemo.getVouApprovalStatus().equals("APPROVED"))) {

                        continue;
                    }
                }

                if (adApprovalDocument.getAdcAllowDuplicate() == EJBCommon.FALSE && apDebitMemo.getVouPrinted() == EJBCommon.TRUE) {

                    continue;
                }

                // show duplicate

                boolean showDuplicate = adApprovalDocument.getAdcTrackDuplicate() == EJBCommon.TRUE && apDebitMemo.getVouPrinted() == EJBCommon.TRUE;

                // set printed

                apDebitMemo.setVouPrinted(EJBCommon.TRUE);

                // get distribution records

                Collection apDistributionRecords = apDistributionRecordHome.findByVouCode(apDebitMemo.getVouCode(), AD_CMPNY);

                Iterator drIter = apDistributionRecords.iterator();
                LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

                while (drIter.hasNext()) {

                    LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) drIter.next();

                    ApRepDebitMemoPrintDetails details = new ApRepDebitMemoPrintDetails();

                    details.setDpVouSplSupplierCode(apDebitMemo.getApSupplier().getSplSupplierCode());
                    details.setDpVouDate(apDebitMemo.getVouDate());
                    details.setDpVouSplAddress(apDebitMemo.getApSupplier().getSplAddress());
                    details.setDpVouDocumentNumber(apDebitMemo.getVouDocumentNumber());
                    details.setDpVouSplTin(apDebitMemo.getApSupplier().getSplTin());
                    details.setDpVouDmVoucherNumber(apDebitMemo.getVouDmVoucherNumber());

                    try {
                        LocalApVoucher apLocalApVoucher = apVoucherHome.findByVouDocumentNumber(apDebitMemo.getVouDmVoucherNumber(), AD_CMPNY);
                        Debug.print("getVouReferenceNumber" + apLocalApVoucher.getVouReferenceNumber());
                        details.setDpVouDrNumber(apLocalApVoucher.getVouReferenceNumber());
                    } catch (Exception e) {
                        // TODO: handle exception
                        details.setDpVouDrNumber("");
                    }

                    details.setDpVouDescription(apDebitMemo.getVouDescription());
                    details.setDpVouBillAmount(apDebitMemo.getVouBillAmount());
                    details.setDpVouCreatedBy(apDebitMemo.getVouCreatedBy());
                    details.setDpVouApprovedBy(apDebitMemo.getVouApprovedRejectedBy());
                    details.setDpDrCoaAccountNumber(apDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                    details.setDpDrCoaAccountDescription(apDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());
                    details.setDpDrDebit(apDistributionRecord.getDrDebit());
                    details.setDpDrAmount(apDistributionRecord.getDrAmount());
                    details.setDpShowDuplicate(showDuplicate ? EJBCommon.TRUE : EJBCommon.FALSE);
                    details.setDpVouApprovalStatus(apDebitMemo.getVouApprovalStatus());

                    // get user name description

                    Debug.print("apVoucher.getChkCreatedBy(): " + apDebitMemo.getVouCreatedBy());
                    try {
                        LocalAdUser adUser = adUserHome.findByUsrName(apDebitMemo.getVouCreatedBy(), AD_CMPNY);
                        details.setDpVouCreatedByDescription(adUser.getUsrDescription());
                    } catch (Exception e) {
                        details.setDpVouCreatedByDescription("");
                    }

                    Debug.print("apVoucher.getChkCreatedBy(): " + details.getDpVouCreatedByDescription());

                    try {
                        Debug.print("apVoucher.getVouApprovedRejectedBy(): " + apDebitMemo.getVouApprovedRejectedBy());
                        LocalAdUser adUser3 = adUserHome.findByUsrName(apDebitMemo.getVouApprovedRejectedBy(), AD_CMPNY);
                        details.setDpVouApprovedRejectedByDescription(adUser3.getUsrDescription());

                    } catch (Exception e) {
                        details.setDpVouApprovedRejectedByDescription("");
                    }
                    Debug.print("DpVouApprovedRejectedByDescription(): " + details.getDpVouApprovedRejectedByDescription());
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