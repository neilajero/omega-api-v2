package com.ejb.txnreports.ap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;

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
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ap.LocalApCheck;
import com.ejb.dao.ap.LocalApCheckHome;
import com.ejb.entities.ap.LocalApDistributionRecord;
import com.ejb.dao.ap.LocalApDistributionRecordHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;
import com.util.mod.ap.ApModCheckDetails;
import com.util.reports.ap.ApRepApRegisterDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;

@Stateless(name = "ApRepCheckPrintControllerEJB")
public class ApRepCheckPrintControllerBean extends EJBContextClass implements ApRepCheckPrintController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdApprovalDocumentHome adApprovalDocumentHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalApCheckHome apCheckHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;

    public ArrayList executeApRepCheckPrint(ArrayList chkCodeList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepCheckPrintControllerBean executeApRepCheckPrint");

        LocalApCheck apCheck = null;

        ArrayList list = new ArrayList();

        try {

            for (Object o : chkCodeList) {

                Integer CHK_CODE = (Integer) o;

                try {

                    apCheck = apCheckHome.findByPrimaryKey(CHK_CODE);

                } catch (FinderException ex) {

                    continue;
                }

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(AD_CMPNY);
                LocalAdApprovalDocument adApprovalDocument = adApprovalDocumentHome.findByAdcType("AP CHECK", AD_CMPNY);

                if (adApprovalDocument.getAdcPrintOption().equals("PRINT APPROVED ONLY")) {

                    if (apCheck.getChkApprovalStatus() == null || apCheck.getChkApprovalStatus().equals("PENDING")) {

                        continue;
                    }

                } else if (adApprovalDocument.getAdcPrintOption().equals("PRINT UNAPPROVED ONLY")) {

                    if (apCheck.getChkApprovalStatus() != null && (apCheck.getChkApprovalStatus().equals("N/A") || apCheck.getChkApprovalStatus().equals("APPROVED"))) {

                        continue;
                    }
                }

                if (adApprovalDocument.getAdcAllowDuplicate() == EJBCommon.FALSE && apCheck.getChkPrinted() == EJBCommon.TRUE) {

                    continue;
                }

                // set printed

                apCheck.setChkPrinted(EJBCommon.TRUE);

                if (!this.getAdPrfUseBankForm(AD_CMPNY)) {

                    ApModCheckDetails mdetails = new ApModCheckDetails();

                    mdetails.setChkDescription(apCheck.getChkDescription());
                    mdetails.setChkCheckDate(apCheck.getChkCheckDate()); // change date to check date
                    mdetails.setChkNumber(apCheck.getChkNumber());
                    mdetails.setChkAmount(apCheck.getChkAmount());
                    // supplier name
                    mdetails.setChkSplName(apCheck.getChkSupplierName() == null ? apCheck.getApSupplier().getSplName() : apCheck.getChkSupplierName());
                    mdetails.setChkSplAddress(apCheck.getApSupplier().getSplAddress());
                    mdetails.setChkFcName(apCheck.getGlFunctionalCurrency().getFcName());
                    mdetails.setChkBaName(apCheck.getAdBankAccount().getBaName());
                    mdetails.setChkBaAccountNumber(apCheck.getAdBankAccount().getBaAccountNumber());

                    mdetails.setChkBaAccountNumberShow(apCheck.getAdBankAccount().getBaAccountNumberShow());
                    mdetails.setChkBaAccountNumberTop(apCheck.getAdBankAccount().getBaAccountNumberTop());
                    mdetails.setChkBaAccountNumberLeft(apCheck.getAdBankAccount().getBaAccountNumberLeft());

                    mdetails.setChkBaAccountNameShow(apCheck.getAdBankAccount().getBaAccountNameShow());
                    mdetails.setChkBaAccountNameTop(apCheck.getAdBankAccount().getBaAccountNameTop());
                    mdetails.setChkBaAccountNameLeft(apCheck.getAdBankAccount().getBaAccountNameLeft());

                    mdetails.setChkBaNumberShow(apCheck.getAdBankAccount().getBaNumberShow());
                    mdetails.setChkBaNumberTop(apCheck.getAdBankAccount().getBaNumberTop());
                    mdetails.setChkBaNumberLeft(apCheck.getAdBankAccount().getBaNumberLeft());

                    mdetails.setChkBaDateShow(apCheck.getAdBankAccount().getBaDateShow());
                    mdetails.setChkBaDateTop(apCheck.getAdBankAccount().getBaDateTop());
                    mdetails.setChkBaDateLeft(apCheck.getAdBankAccount().getBaDateLeft());

                    mdetails.setChkBaPayeeShow(apCheck.getAdBankAccount().getBaPayeeShow());
                    mdetails.setChkBaPayeeTop(apCheck.getAdBankAccount().getBaPayeeTop());
                    mdetails.setChkBaPayeeLeft(apCheck.getAdBankAccount().getBaPayeeLeft());

                    mdetails.setChkBaAmountShow(apCheck.getAdBankAccount().getBaAmountShow());
                    mdetails.setChkBaAmountTop(apCheck.getAdBankAccount().getBaAmountTop());
                    mdetails.setChkBaAmountLeft(apCheck.getAdBankAccount().getBaAmountLeft());

                    mdetails.setChkBaWordAmountShow(apCheck.getAdBankAccount().getBaWordAmountShow());
                    mdetails.setChkBaWordAmountTop(apCheck.getAdBankAccount().getBaWordAmountTop());
                    mdetails.setChkBaWordAmountLeft(apCheck.getAdBankAccount().getBaWordAmountLeft());

                    mdetails.setChkBaCurrencyShow(apCheck.getAdBankAccount().getBaCurrencyShow());
                    mdetails.setChkBaCurrencyTop(apCheck.getAdBankAccount().getBaCurrencyTop());
                    mdetails.setChkBaCurrencyLeft(apCheck.getAdBankAccount().getBaCurrencyLeft());

                    mdetails.setChkBaAddressShow(apCheck.getAdBankAccount().getBaAddressShow());
                    mdetails.setChkBaAddressTop(apCheck.getAdBankAccount().getBaAddressTop());
                    mdetails.setChkBaAddressLeft(apCheck.getAdBankAccount().getBaAddressLeft());

                    mdetails.setChkBaMemoShow(apCheck.getAdBankAccount().getBaMemoShow());
                    mdetails.setChkBaMemoTop(apCheck.getAdBankAccount().getBaMemoTop());
                    mdetails.setChkBaMemoLeft(apCheck.getAdBankAccount().getBaMemoLeft());

                    mdetails.setChkBaDocNumberShow(apCheck.getAdBankAccount().getBadocNumberShow());
                    mdetails.setChkBaDocNumberTop(apCheck.getAdBankAccount().getBadocNumberTop());
                    mdetails.setChkBaDocNumberLeft(apCheck.getAdBankAccount().getBadocNumberLeft());

                    mdetails.setChkBaFontSize(apCheck.getAdBankAccount().getBaFontSize());
                    mdetails.setChkBaFontStyle(apCheck.getAdBankAccount().getBaFontStyle());

                    mdetails.setChkCrossCheck(apCheck.getChkCrossCheck());
                    mdetails.setChkDocumentNumber(apCheck.getChkDocumentNumber());
                    Debug.print("docNumber" + apCheck.getChkDocumentNumber());
                    mdetails.setChkSplRemarks(apCheck.getApSupplier().getSplRemarks());

                    //        			Include Branch

                    Debug.print("1 Controller Check Date : " + mdetails.getChkCheckDate());
                    Debug.print("1 Controller Date : " + mdetails.getChkDate());

                    list.add(mdetails);

                } else {

                    // get distribution records

                    Collection apDistributionRecords = apDistributionRecordHome.findByChkCode(apCheck.getChkCode(), AD_CMPNY);

                    for (Object distributionRecord : apDistributionRecords) {

                        LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) distributionRecord;

                        ApModCheckDetails mdetails = new ApModCheckDetails();

                        mdetails.setChkDescription(apCheck.getChkDescription());
                        mdetails.setChkCheckDate(apCheck.getChkCheckDate()); // change date to check date
                        mdetails.setChkNumber(apCheck.getChkNumber());
                        mdetails.setChkAmount(apCheck.getChkAmount());
                        // supplier name
                        mdetails.setChkSplName(apCheck.getChkSupplierName() == null ? apCheck.getApSupplier().getSplName() : apCheck.getChkSupplierName());
                        mdetails.setChkSplAddress(apCheck.getApSupplier().getSplAddress());
                        mdetails.setChkFcName(apCheck.getGlFunctionalCurrency().getFcName());
                        mdetails.setChkBaName(apCheck.getAdBankAccount().getBaName());
                        mdetails.setChkBaAccountNumber(apCheck.getAdBankAccount().getBaAccountNumber());

                        mdetails.setChkDocumentNumber(apCheck.getChkDocumentNumber());
                        Debug.print("docNumber2" + apCheck.getChkDocumentNumber());
                        mdetails.setChkDate(apCheck.getChkDate());
                        // mdetails.setChkDescription(apCheck.getChkDescription());
                        mdetails.setChkCreatedBy(apCheck.getChkCreatedBy());
                        mdetails.setChkApprovedRejectedBy(apCheck.getChkApprovedRejectedBy());
                        mdetails.setChkApprovalStatus(apCheck.getChkApprovalStatus());
                        mdetails.setChkPosted(apCheck.getChkPosted());
                        mdetails.setChkDrDebit(apDistributionRecord.getDrDebit());
                        mdetails.setChkDrAmount(apDistributionRecord.getDrAmount());
                        mdetails.setChkDrCoaAccountNumber(apDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                        mdetails.setChkDrCoaAccountDescription(apDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());

                        mdetails.setChkBaAccountNumberShow(apCheck.getAdBankAccount().getBaAccountNumberShow());
                        mdetails.setChkBaAccountNumberTop(apCheck.getAdBankAccount().getBaAccountNumberTop());
                        mdetails.setChkBaAccountNumberLeft(apCheck.getAdBankAccount().getBaAccountNumberLeft());

                        mdetails.setChkBaAccountNameShow(apCheck.getAdBankAccount().getBaAccountNameShow());
                        mdetails.setChkBaAccountNameTop(apCheck.getAdBankAccount().getBaAccountNameTop());
                        mdetails.setChkBaAccountNameLeft(apCheck.getAdBankAccount().getBaAccountNameLeft());

                        mdetails.setChkBaNumberShow(apCheck.getAdBankAccount().getBaNumberShow());
                        mdetails.setChkBaNumberTop(apCheck.getAdBankAccount().getBaNumberTop());
                        mdetails.setChkBaNumberLeft(apCheck.getAdBankAccount().getBaNumberLeft());

                        mdetails.setChkBaDateShow(apCheck.getAdBankAccount().getBaDateShow());
                        mdetails.setChkBaDateTop(apCheck.getAdBankAccount().getBaDateTop());
                        mdetails.setChkBaDateLeft(apCheck.getAdBankAccount().getBaDateLeft());

                        mdetails.setChkBaPayeeShow(apCheck.getAdBankAccount().getBaPayeeShow());
                        mdetails.setChkBaPayeeTop(apCheck.getAdBankAccount().getBaPayeeTop());
                        mdetails.setChkBaPayeeLeft(apCheck.getAdBankAccount().getBaPayeeLeft());

                        mdetails.setChkBaAmountShow(apCheck.getAdBankAccount().getBaAmountShow());
                        mdetails.setChkBaAmountTop(apCheck.getAdBankAccount().getBaAmountTop());
                        mdetails.setChkBaAmountLeft(apCheck.getAdBankAccount().getBaAmountLeft());

                        mdetails.setChkBaWordAmountShow(apCheck.getAdBankAccount().getBaWordAmountShow());
                        mdetails.setChkBaWordAmountTop(apCheck.getAdBankAccount().getBaWordAmountTop());
                        mdetails.setChkBaWordAmountLeft(apCheck.getAdBankAccount().getBaWordAmountLeft());

                        mdetails.setChkBaCurrencyShow(apCheck.getAdBankAccount().getBaCurrencyShow());
                        mdetails.setChkBaCurrencyTop(apCheck.getAdBankAccount().getBaCurrencyTop());
                        mdetails.setChkBaCurrencyLeft(apCheck.getAdBankAccount().getBaCurrencyLeft());

                        mdetails.setChkBaAddressShow(apCheck.getAdBankAccount().getBaAddressShow());
                        mdetails.setChkBaAddressTop(apCheck.getAdBankAccount().getBaAddressTop());
                        mdetails.setChkBaAddressLeft(apCheck.getAdBankAccount().getBaAddressLeft());

                        mdetails.setChkBaMemoShow(apCheck.getAdBankAccount().getBaMemoShow());
                        mdetails.setChkBaMemoTop(apCheck.getAdBankAccount().getBaMemoTop());
                        mdetails.setChkBaMemoLeft(apCheck.getAdBankAccount().getBaMemoLeft());

                        mdetails.setChkBaDocNumberShow(apCheck.getAdBankAccount().getBadocNumberShow());
                        mdetails.setChkBaDocNumberTop(apCheck.getAdBankAccount().getBadocNumberTop());
                        mdetails.setChkBaDocNumberLeft(apCheck.getAdBankAccount().getBadocNumberLeft());

                        mdetails.setChkBaFontSize(apCheck.getAdBankAccount().getBaFontSize());
                        mdetails.setChkBaFontStyle(apCheck.getAdBankAccount().getBaFontStyle());

                        mdetails.setChkCrossCheck(apCheck.getChkCrossCheck());
                        mdetails.setChkSplRemarks(apCheck.getApSupplier().getSplRemarks());

                        //	        			Include Branch
                        LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(apCheck.getChkAdBranch());
                        mdetails.setBrnhCde(adBranch.getBrBranchCode());
                        mdetails.setBrnhNm(adBranch.getBrName());

                        Debug.print("adBranch.getBrName() 2: " + adBranch.getBrName());
                        Debug.print("2 Controller Check Date : " + mdetails.getChkCheckDate());
                        Debug.print("2 Controller Date : " + mdetails.getChkDate());

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

        Debug.print("ApRepCheckPrintControllerBean getAdCompany");

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

    public boolean getAdPrfUseBankForm(Integer AD_CMPNY) {

        Debug.print("ApRepCheckPrintControllerBean getAdPrfUseBankForm");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfCmUseBankForm() == EJBCommon.TRUE;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public static final Comparator SupplierCodeComparator = (AR, anotherAR) -> {

        String ARG_SPL_SPPLR_CODE1 = ((ApRepApRegisterDetails) AR).getArgSplSupplierCode();
        String ARG_SPL_SPPLR_TYP1 = ((ApRepApRegisterDetails) AR).getArgSplSupplierType();
        Date ARG_DT1 = ((ApRepApRegisterDetails) AR).getArgDate();
        String ARG_DCMNT_NMBR1 = ((ApRepApRegisterDetails) AR).getArgDocumentNumber();

        String ARG_SPL_SPPLR_CODE2 = ((ApRepApRegisterDetails) anotherAR).getArgSplSupplierCode();
        String ARG_SPL_SPPLR_TYP2 = ((ApRepApRegisterDetails) anotherAR).getArgSplSupplierType();
        Date ARG_DT2 = ((ApRepApRegisterDetails) anotherAR).getArgDate();
        String ARG_DCMNT_NMBR2 = ((ApRepApRegisterDetails) anotherAR).getArgDocumentNumber();

        String ORDER_BY = ((ApRepApRegisterDetails) AR).getOrderBy();

        if (!(ARG_SPL_SPPLR_CODE1.equals(ARG_SPL_SPPLR_CODE2))) {

            return ARG_SPL_SPPLR_CODE1.compareTo(ARG_SPL_SPPLR_CODE2);

        } else {

            if (ORDER_BY.equals("DATE") && !(ARG_DT1.equals(ARG_DT2))) {

                return ARG_DT1.compareTo(ARG_DT2);

            } else if (ORDER_BY.equals("SUPPLIER TYPE") && !(ARG_SPL_SPPLR_TYP1.equals(ARG_SPL_SPPLR_TYP2))) {

                return ARG_SPL_SPPLR_TYP1.compareTo(ARG_SPL_SPPLR_TYP2);

            } else {

                return ARG_DCMNT_NMBR1.compareTo(ARG_DCMNT_NMBR2);
            }
        }
    };

    // private methods

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApRepCheckPrintControllerBean ejbCreate");
    }
}