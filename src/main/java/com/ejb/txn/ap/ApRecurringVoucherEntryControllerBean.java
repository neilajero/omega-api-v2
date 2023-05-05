/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ApRecurringVoucherEntryControllerBean
 * @created February 18, 2004, 9:31 AM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txn.ap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPaymentTerm;
import com.ejb.dao.ad.LocalAdPaymentTermHome;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ad.LocalAdUser;
import com.ejb.dao.ad.LocalAdUserHome;
import com.ejb.dao.ap.ILocalApSupplierHome;
import com.ejb.entities.ap.LocalApDistributionRecord;
import com.ejb.dao.ap.LocalApDistributionRecordHome;
import com.ejb.entities.ap.LocalApRecurringVoucher;
import com.ejb.dao.ap.LocalApRecurringVoucherHome;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.entities.ap.LocalApTaxCode;
import com.ejb.dao.ap.LocalApTaxCodeHome;
import com.ejb.dao.ap.LocalApVoucherBatch;
import com.ejb.dao.ap.LocalApVoucherBatchHome;
import com.ejb.entities.ap.LocalApWithholdingTaxCode;
import com.ejb.dao.ap.LocalApWithholdingTaxCodeHome;
import com.ejb.exception.global.GlobalBranchAccountNumberInvalidException;
import com.ejb.exception.global.GlobalConversionDateNotExistException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalPaymentTermInvalidException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.exception.global.GlobalRecordInvalidException;
import com.ejb.dao.gen.LocalGenSegmentHome;
import com.ejb.dao.gen.LocalGenValueSetValueHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
import com.ejb.entities.gl.LocalGlFunctionalCurrencyRate;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.util.mod.ap.ApModDistributionRecordDetails;
import com.util.mod.ap.ApModRecurringVoucherDetails;
import com.util.mod.ap.ApModSupplierDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.mod.gl.GlModFunctionalCurrencyDetails;
import com.util.ap.ApRecurringVoucherDetails;

@Stateless(name = "ApRecurringVoucherEntryControllerEJB")
public class ApRecurringVoucherEntryControllerBean extends EJBContextClass implements ApRecurringVoucherEntryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private ILocalApSupplierHome apSupplierHome;
    @EJB
    private LocalAdPaymentTermHome adPaymentTermHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    private LocalApRecurringVoucherHome apRecurringVoucherHome;
    @EJB
    private LocalApTaxCodeHome apTaxCodeHome;
    @EJB
    private LocalApVoucherBatchHome apVoucherBatchHome;
    @EJB
    private LocalApWithholdingTaxCodeHome apWithholdingTaxCodeHome;
    @EJB
    private LocalGenSegmentHome genSegmentHome;
    @EJB
    private LocalGenValueSetValueHome genValueSetValueHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;

    public ArrayList getGlFcAllWithDefault(Integer AD_CMPNY) {

        Debug.print("ApRecurringVoucherEntryControllerBean getGlFcAllWithDefault");

        Collection glFunctionalCurrencies = null;

        LocalGlFunctionalCurrency glFunctionalCurrency = null;
        LocalAdCompany adCompany = null;

        ArrayList list = new ArrayList();


        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            glFunctionalCurrencies = glFunctionalCurrencyHome.findFcAllEnabled(EJBCommon.getGcCurrentDateWoTime().getTime(), AD_CMPNY);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (glFunctionalCurrencies.isEmpty()) {

            return null;
        }

        for (Object functionalCurrency : glFunctionalCurrencies) {

            glFunctionalCurrency = (LocalGlFunctionalCurrency) functionalCurrency;

            GlModFunctionalCurrencyDetails mdetails = new GlModFunctionalCurrencyDetails(glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), adCompany.getGlFunctionalCurrency().getFcName().equals(glFunctionalCurrency.getFcName()) ? EJBCommon.TRUE : EJBCommon.FALSE);

            list.add(mdetails);
        }

        return list;
    }

    public ArrayList getAdPytAll(Integer AD_CMPNY) {

        Debug.print("ApRecurringVoucherEntryControllerBean getAdPytAll");

        ArrayList list = new ArrayList();

        try {

            Collection adPaymentTerms = adPaymentTermHome.findEnabledPytAll(AD_CMPNY);

            for (Object paymentTerm : adPaymentTerms) {

                LocalAdPaymentTerm adPaymentTerm = (LocalAdPaymentTerm) paymentTerm;

                list.add(adPaymentTerm.getPytName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApTcAll(Integer AD_CMPNY) {

        Debug.print("ApRecurringVoucherEntryControllerBean getApTcAll");

        ArrayList list = new ArrayList();

        try {

            Collection apTaxCodes = apTaxCodeHome.findEnabledTcAll(AD_CMPNY);

            for (Object taxCode : apTaxCodes) {

                LocalApTaxCode apTaxCode = (LocalApTaxCode) taxCode;

                list.add(apTaxCode.getTcName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdUsrAll(Integer AD_CMPNY) {

        Debug.print("ApRecurringVoucherEntryControllerBean getAdUsrAll");

        ArrayList list = new ArrayList();


        try {

            Collection adUsers = adUserHome.findUsrAll(AD_CMPNY);

            for (Object user : adUsers) {

                LocalAdUser adUser = (LocalAdUser) user;

                list.add(adUser.getUsrName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApWtcAll(Integer AD_CMPNY) {

        Debug.print("ApRecurringVoucherEntryControllerBean getApWtcAll");

        ArrayList list = new ArrayList();

        try {

            Collection apWithholdingTaxCodes = apWithholdingTaxCodeHome.findEnabledWtcAll(AD_CMPNY);

            for (Object withholdingTaxCode : apWithholdingTaxCodes) {

                LocalApWithholdingTaxCode apWithholdingTaxCode = (LocalApWithholdingTaxCode) withholdingTaxCode;

                list.add(apWithholdingTaxCode.getWtcName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApOpenVbAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ApRecurringVoucherEntryControllerBean getApOpenVbAll");

        ArrayList list = new ArrayList();


        try {

            Collection apVoucherBatches = apVoucherBatchHome.findOpenVbByVbType("VOUCHER", AD_BRNCH, AD_CMPNY);

            for (Object voucherBatch : apVoucherBatches) {

                LocalApVoucherBatch apVoucherBatch = (LocalApVoucherBatch) voucherBatch;

                list.add(apVoucherBatch.getVbName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getAdPrfApJournalLineNumber(Integer AD_CMPNY) {

        Debug.print("ApDebitMemoEntryControllerBean getAdPrfApJournalLineNumber");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfApJournalLineNumber();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfEnableApVoucherBatch(Integer AD_CMPNY) {

        Debug.print("ApDebitMemoEntryControllerBean getAdPrfApEnableApVoucherBatch");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfEnableApVoucherBatch();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ApModRecurringVoucherDetails getApRvByRvCode(Integer RV_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRecurringVoucherEntryControllerBean getApRvByRvCode");

        try {

            LocalApRecurringVoucher apRecurringVoucher = null;

            try {

                apRecurringVoucher = apRecurringVoucherHome.findByPrimaryKey(RV_CODE);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList rvDrList = new ArrayList();

            // get distribution records

            Collection apDistributionRecords = apDistributionRecordHome.findByRvCode(apRecurringVoucher.getRvCode(), AD_CMPNY);

            short lineNumber = 1;

            double TOTAL_DEBIT = 0d;
            double TOTAL_CREDIT = 0d;

            for (Object distributionRecord : apDistributionRecords) {

                LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) distributionRecord;

                ApModDistributionRecordDetails mdetails = new ApModDistributionRecordDetails();

                mdetails.setDrCode(apDistributionRecord.getDrCode());
                mdetails.setDrLine(lineNumber);
                mdetails.setDrClass(apDistributionRecord.getDrClass());
                mdetails.setDrDebit(apDistributionRecord.getDrDebit());
                mdetails.setDrAmount(apDistributionRecord.getDrAmount());
                mdetails.setDrCoaAccountNumber(apDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                mdetails.setDrCoaAccountDescription(apDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());

                if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                    TOTAL_DEBIT += apDistributionRecord.getDrAmount();

                } else {

                    TOTAL_CREDIT += apDistributionRecord.getDrAmount();
                }

                rvDrList.add(mdetails);

                lineNumber++;
            }

            ApModRecurringVoucherDetails mRvDetails = new ApModRecurringVoucherDetails();

            mRvDetails.setRvCode(apRecurringVoucher.getRvCode());
            mRvDetails.setRvName(apRecurringVoucher.getRvName());
            mRvDetails.setRvDescription(apRecurringVoucher.getRvDescription());
            mRvDetails.setRvConversionDate(apRecurringVoucher.getRvConversionDate());
            mRvDetails.setRvConversionRate(apRecurringVoucher.getRvConversionRate());
            mRvDetails.setRvAmount(apRecurringVoucher.getRvAmount());
            mRvDetails.setRvAmountDue(apRecurringVoucher.getRvAmountDue());
            mRvDetails.setRvTotalDebit(TOTAL_DEBIT);
            mRvDetails.setRvTotalCredit(TOTAL_CREDIT);
            mRvDetails.setRvSchedule(apRecurringVoucher.getRvSchedule());
            mRvDetails.setRvNextRunDate(apRecurringVoucher.getRvNextRunDate());
            mRvDetails.setRvLastRunDate(apRecurringVoucher.getRvLastRunDate());

            if (apRecurringVoucher.getRvAdUserName1() != null) {

                LocalAdUser userName1 = adUserHome.findByPrimaryKey(apRecurringVoucher.getRvAdUserName1());
                mRvDetails.setRvAdNotifiedUser1(userName1.getUsrName());
            }

            if (apRecurringVoucher.getRvAdUserName2() != null) {

                LocalAdUser userName2 = adUserHome.findByPrimaryKey(apRecurringVoucher.getRvAdUserName2());
                mRvDetails.setRvAdNotifiedUser2(userName2.getUsrName());
            }

            if (apRecurringVoucher.getRvAdUserName3() != null) {

                LocalAdUser userName3 = adUserHome.findByPrimaryKey(apRecurringVoucher.getRvAdUserName3());
                mRvDetails.setRvAdNotifiedUser3(userName3.getUsrName());
            }

            if (apRecurringVoucher.getRvAdUserName4() != null) {

                LocalAdUser userName4 = adUserHome.findByPrimaryKey(apRecurringVoucher.getRvAdUserName4());
                mRvDetails.setRvAdNotifiedUser4(userName4.getUsrName());
            }

            if (apRecurringVoucher.getRvAdUserName5() != null) {

                LocalAdUser userName5 = adUserHome.findByPrimaryKey(apRecurringVoucher.getRvAdUserName5());
                mRvDetails.setRvAdNotifiedUser5(userName5.getUsrName());
            }

            mRvDetails.setRvTcName(apRecurringVoucher.getApTaxCode().getTcName());
            mRvDetails.setRvPytName(apRecurringVoucher.getAdPaymentTerm().getPytName());
            mRvDetails.setRvSplSupplierCode(apRecurringVoucher.getApSupplier().getSplSupplierCode());
            mRvDetails.setRvWtcName(apRecurringVoucher.getApWithholdingTaxCode().getWtcName());
            mRvDetails.setRvFcName(apRecurringVoucher.getGlFunctionalCurrency().getFcName());
            mRvDetails.setRvDrList(rvDrList);
            mRvDetails.setRvVbName(apRecurringVoucher.getApVoucherBatch() != null ? apRecurringVoucher.getApVoucherBatch().getVbName() : null);

            return mRvDetails;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ApModSupplierDetails getApSplBySplSupplierCode(String SPL_SPPLR_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRecurringVoucherEntryControllerBean getApSplBySplSupplierCode");

        try {

            LocalApSupplier apSupplier = null;

            try {

                apSupplier = apSupplierHome.findBySplSupplierCode(SPL_SPPLR_CODE, AD_CMPNY);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ApModSupplierDetails mdetails = new ApModSupplierDetails();

            mdetails.setSplPytName(apSupplier.getAdPaymentTerm() != null ? apSupplier.getAdPaymentTerm().getPytName() : null);
            mdetails.setSplScTcName(apSupplier.getApSupplierClass().getApTaxCode() != null ? apSupplier.getApSupplierClass().getApTaxCode().getTcName() : null);
            mdetails.setSplScWtcName(apSupplier.getApSupplierClass().getApWithholdingTaxCode() != null ? apSupplier.getApSupplierClass().getApWithholdingTaxCode().getWtcName() : null);

            return mdetails;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApDrBySplSupplierCodeAndTcNameAndWtcNameAndRvAmount(String SPL_SPPLR_CODE, String TC_NM, String WTC_NM, double RV_AMNT, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRecurringVoucherEntryControllerBean getApDrBySplSupplierCodeAndTcNameAndWtcNameAndRvAmount");

        ArrayList list = new ArrayList();

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            LocalApSupplier apSupplier = null;
            LocalApTaxCode apTaxCode = null;
            LocalApWithholdingTaxCode apWithholdingTaxCode = null;

            try {

                apSupplier = apSupplierHome.findBySplSupplierCode(SPL_SPPLR_CODE, AD_CMPNY);
                apTaxCode = apTaxCodeHome.findByTcName(TC_NM, AD_CMPNY);
                apWithholdingTaxCode = apWithholdingTaxCodeHome.findByWtcName(WTC_NM, AD_CMPNY);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            double NET_AMOUNT = 0d;
            double TAX_AMOUNT = 0d;
            double W_TAX_AMOUNT = 0d;
            short LINE_NUMBER = 0;

            // create dr net expense

            if (apTaxCode.getTcType().equals("INCLUSIVE")) {

                NET_AMOUNT = EJBCommon.roundIt(RV_AMNT / (1 + (apTaxCode.getTcRate() / 100)), this.getGlFcPrecisionUnit(AD_CMPNY));

            } else {

                // tax exclusive, none, zero rated or exempt

                NET_AMOUNT = RV_AMNT;
            }

            ApModDistributionRecordDetails mdetails = new ApModDistributionRecordDetails();
            mdetails.setDrLine(++LINE_NUMBER);
            mdetails.setDrClass("EXPENSE");
            mdetails.setDrDebit(EJBCommon.TRUE);
            mdetails.setDrAmount(NET_AMOUNT);

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(apSupplier.getSplCoaGlExpenseAccount());

            mdetails.setDrCoaAccountNumber(glChartOfAccount.getCoaAccountNumber());
            mdetails.setDrCoaAccountDescription(glChartOfAccount.getCoaAccountDescription());

            list.add(mdetails);

            // create tax line if necessary

            if (!apTaxCode.getTcType().equals("NONE") && !apTaxCode.getTcType().equals("EXEMPT")) {

                if (apTaxCode.getTcType().equals("INCLUSIVE")) {

                    TAX_AMOUNT = EJBCommon.roundIt(RV_AMNT - NET_AMOUNT, this.getGlFcPrecisionUnit(AD_CMPNY));

                } else if (apTaxCode.getTcType().equals("EXCLUSIVE")) {

                    TAX_AMOUNT = EJBCommon.roundIt(RV_AMNT * apTaxCode.getTcRate() / 100, this.getGlFcPrecisionUnit(AD_CMPNY));

                } else {

                    // tax none zero-rated or exempt

                }

                mdetails = new ApModDistributionRecordDetails();
                mdetails.setDrLine(++LINE_NUMBER);
                mdetails.setDrClass("TAX");
                mdetails.setDrDebit(EJBCommon.TRUE);
                mdetails.setDrAmount(TAX_AMOUNT);

                mdetails.setDrCoaAccountNumber(apTaxCode.getGlChartOfAccount().getCoaAccountNumber());
                mdetails.setDrCoaAccountDescription(apTaxCode.getGlChartOfAccount().getCoaAccountDescription());

                list.add(mdetails);
            }

            // create withholding tax if necessary

            if (apWithholdingTaxCode.getWtcRate() != 0 && adPreference.getPrfApWTaxRealization().equals("VOUCHER")) {

                W_TAX_AMOUNT = EJBCommon.roundIt(NET_AMOUNT * (apWithholdingTaxCode.getWtcRate() / 100), this.getGlFcPrecisionUnit(AD_CMPNY));

                mdetails = new ApModDistributionRecordDetails();
                mdetails.setDrLine(++LINE_NUMBER);
                mdetails.setDrClass("W-TAX");
                mdetails.setDrDebit(EJBCommon.FALSE);
                mdetails.setDrAmount(W_TAX_AMOUNT);

                mdetails.setDrCoaAccountNumber(apWithholdingTaxCode.getGlChartOfAccount().getCoaAccountNumber());
                mdetails.setDrCoaAccountDescription(apWithholdingTaxCode.getGlChartOfAccount().getCoaAccountDescription());

                list.add(mdetails);
            }

            // create accounts payable

            mdetails = new ApModDistributionRecordDetails();
            mdetails.setDrLine(++LINE_NUMBER);
            mdetails.setDrClass("PAYABLE");
            mdetails.setDrDebit(EJBCommon.FALSE);
            mdetails.setDrAmount(NET_AMOUNT + TAX_AMOUNT - W_TAX_AMOUNT);

            glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(apSupplier.getSplCoaGlPayableAccount());

            mdetails.setDrCoaAccountNumber(glChartOfAccount.getCoaAccountNumber());
            mdetails.setDrCoaAccountDescription(glChartOfAccount.getCoaAccountDescription());

            list.add(mdetails);

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void saveApRvEntry(ApRecurringVoucherDetails details, String RV_AD_NTFD_USR1, String RV_AD_NTFD_USR2, String RV_AD_NTFD_USR3, String RV_AD_NTFD_USR4, String RV_AD_NTFD_USR5, String PYT_NM, String TC_NM, String WTC_NM, String FC_NM, String SPL_SPPLR_CODE, ArrayList drList, String VB_NM, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalRecordAlreadyDeletedException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, GlobalBranchAccountNumberInvalidException, GlobalRecordInvalidException {

        Debug.print("ApRecurringVoucherEntryControllerBean saveApRvEntry");

        LocalApRecurringVoucher apRecurringVoucher = null;

        try {

            // validate if recurring voucher is already deleted

            try {

                if (details.getRvCode() != null) {

                    apRecurringVoucher = apRecurringVoucherHome.findByPrimaryKey(details.getRvCode());
                }

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if recurring voucher exists

            try {

                if (details.getRvName().equals("Interest Income")) {

                    LocalApRecurringVoucher apExistingRecurringVoucher = apRecurringVoucherHome.findByRvNameAndSupplierCode(details.getRvName(), SPL_SPPLR_CODE, AD_BRNCH, AD_CMPNY);

                    if (details.getRvCode() == null || details.getRvCode() != null && !apExistingRecurringVoucher.getRvCode().equals(details.getRvCode())) {

                        throw new GlobalRecordAlreadyExistException();
                    }

                } else {

                    LocalApRecurringVoucher apExistingRecurringVoucher = apRecurringVoucherHome.findByRvName(details.getRvName(), AD_BRNCH, AD_CMPNY);

                    if (details.getRvCode() == null || details.getRvCode() != null && !apExistingRecurringVoucher.getRvCode().equals(details.getRvCode())) {

                        throw new GlobalRecordAlreadyExistException();
                    }
                }

            } catch (FinderException ex) {

            }

            // validate if conversion date exists

            try {

                if (details.getRvConversionDate() != null) {

                    LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
                    LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);

                    if (!glValidateFunctionalCurrency.getFcName().equals("USD")) {

                        LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(), details.getRvConversionDate(), AD_CMPNY);

                    } else if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                        LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), details.getRvConversionDate(), AD_CMPNY);
                    }
                }

            } catch (FinderException ex) {

                throw new GlobalConversionDateNotExistException();
            }

            // validate if payment term has at least one payment schedule

            if (adPaymentTermHome.findByPytName(PYT_NM, AD_CMPNY).getAdPaymentSchedules().isEmpty()) {

                throw new GlobalPaymentTermInvalidException();
            }

            LocalAdUser userName2 = null;
            LocalAdUser userName3 = null;
            LocalAdUser userName4 = null;
            LocalAdUser userName5 = null;

            // get all notified users

            LocalAdUser userName1 = adUserHome.findByUsrName(RV_AD_NTFD_USR1, AD_CMPNY);

            try {

                userName2 = adUserHome.findByUsrName(RV_AD_NTFD_USR2, AD_CMPNY);

            } catch (FinderException ex) {

            }

            if (RV_AD_NTFD_USR3 != null) {

                try {

                    userName3 = adUserHome.findByUsrName(RV_AD_NTFD_USR3, AD_CMPNY);

                } catch (FinderException ex) {

                }
            }

            if (RV_AD_NTFD_USR4 != null) {

                try {

                    userName4 = adUserHome.findByUsrName(RV_AD_NTFD_USR4, AD_CMPNY);

                } catch (FinderException ex) {

                }
            }

            if (RV_AD_NTFD_USR5 != null) {

                try {

                    userName5 = adUserHome.findByUsrName(RV_AD_NTFD_USR5, AD_CMPNY);

                } catch (FinderException ex) {

                }
            }

            // create recurring voucher

            if (details.getRvCode() == null) {

                apRecurringVoucher = apRecurringVoucherHome.create(details.getRvName(), details.getRvDescription(), details.getRvConversionDate(), details.getRvConversionRate(), details.getRvAmount(), details.getRvAmountDue(), userName1.getUsrCode(), userName2 != null ? userName2.getUsrCode() : null, userName3 != null ? userName3.getUsrCode() : null, userName4 != null ? userName4.getUsrCode() : null, userName5 != null ? userName5.getUsrCode() : null, details.getRvSchedule(), details.getRvNextRunDate(), null, AD_BRNCH, AD_CMPNY);

            } else {

                apRecurringVoucher.setRvName(details.getRvName());
                apRecurringVoucher.setRvDescription(details.getRvDescription());
                apRecurringVoucher.setRvConversionDate(details.getRvConversionDate());
                apRecurringVoucher.setRvConversionRate(details.getRvConversionRate());
                apRecurringVoucher.setRvAdUserName1(userName1.getUsrCode());
                apRecurringVoucher.setRvAdUserName2(userName2 != null ? userName2.getUsrCode() : null);
                apRecurringVoucher.setRvAdUserName3(userName3 != null ? userName3.getUsrCode() : null);
                apRecurringVoucher.setRvAdUserName4(userName4 != null ? userName4.getUsrCode() : null);
                apRecurringVoucher.setRvAdUserName5(userName5 != null ? userName5.getUsrCode() : null);
                apRecurringVoucher.setRvAmount(details.getRvAmount());
                apRecurringVoucher.setRvAmountDue(details.getRvAmountDue());
                apRecurringVoucher.setRvSchedule(details.getRvSchedule());
                apRecurringVoucher.setRvNextRunDate(details.getRvNextRunDate());
            }

            LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName(PYT_NM, AD_CMPNY);
            adPaymentTerm.addApRecurringVoucher(apRecurringVoucher);

            LocalApTaxCode apTaxCode = apTaxCodeHome.findByTcName(TC_NM, AD_CMPNY);
            apTaxCode.addApRecurringVoucher(apRecurringVoucher);

            LocalApWithholdingTaxCode apWithholdingTaxCode = apWithholdingTaxCodeHome.findByWtcName(WTC_NM, AD_CMPNY);
            apWithholdingTaxCode.addApRecurringVoucher(apRecurringVoucher);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);
            glFunctionalCurrency.addApRecurringVoucher(apRecurringVoucher);

            LocalApSupplier apSupplier = apSupplierHome.findBySplSupplierCode(SPL_SPPLR_CODE, AD_CMPNY);
            apSupplier.addApRecurringVoucher(apRecurringVoucher);

            // interest income
            if (details.getRvName().equals("Interest Income") && !apSupplier.getApSupplierClass().getScName().contains("Investors")) {

                throw new GlobalRecordInvalidException();
            }

            try {

                LocalApVoucherBatch apVoucherBatch = apVoucherBatchHome.findByVbName(VB_NM, AD_BRNCH, AD_CMPNY);
                apVoucherBatch.addApRecurringVoucher(apRecurringVoucher);

            } catch (FinderException ex) {

            }

            // remove all distribution records

            Collection apDistributionRecords = apRecurringVoucher.getApDistributionRecords();

            Iterator i = apDistributionRecords.iterator();

            while (i.hasNext()) {

                LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                i.remove();

                // apDistributionRecord.entityRemove();
                em.remove(apDistributionRecord);
            }

            // add new distribution records

            i = drList.iterator();

            while (i.hasNext()) {

                ApModDistributionRecordDetails mDrDetails = (ApModDistributionRecordDetails) i.next();

                this.addApDrEntry(mDrDetails, apRecurringVoucher, AD_BRNCH, AD_CMPNY);
            }

        } catch (GlobalRecordAlreadyDeletedException | GlobalRecordInvalidException |
                 GlobalBranchAccountNumberInvalidException | GlobalPaymentTermInvalidException |
                 GlobalConversionDateNotExistException | GlobalRecordAlreadyExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ApRecurringVoucherEntryControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteApRvEntry(Integer RV_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException {

        Debug.print("ApRecurringVoucherControllerBean deleteApRvEntry");

        try {

            LocalApRecurringVoucher apRecurringVoucher = apRecurringVoucherHome.findByPrimaryKey(RV_CODE);

            // apRecurringVoucher.entityRemove();
            em.remove(apRecurringVoucher);

        } catch (FinderException ex) {

            throw new GlobalRecordAlreadyDeletedException();

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfApUseSupplierPulldown(Integer AD_CMPNY) {

        Debug.print("ApRecurringVoucherEntryControllerBean getAdPrfApUseSupplierPulldown");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfApUseSupplierPulldown();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public double getFrRateByFrNameAndFrDate(String FC_NM, Date CONVERSION_DATE, Integer AD_CMPNY) throws GlobalConversionDateNotExistException {

        Debug.print("ApRecurringVoucherEntryControllerBean getFrRateByFrNameAndFrDate");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);

            double CONVERSION_RATE = 1;

            // Get functional currency rate

            if (!FC_NM.equals("USD")) {

                LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glFunctionalCurrency.getFcCode(), CONVERSION_DATE, AD_CMPNY);

                CONVERSION_RATE = glFunctionalCurrencyRate.getFrXToUsd();
            }

            // Get set of book functional currency rate if necessary

            if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), CONVERSION_DATE, AD_CMPNY);

                CONVERSION_RATE = CONVERSION_RATE / glCompanyFunctionalCurrencyRate.getFrXToUsd();
            }

            return CONVERSION_RATE;

        } catch (FinderException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlobalConversionDateNotExistException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods

    private void addApDrEntry(ApModDistributionRecordDetails mdetails, LocalApRecurringVoucher apRecurringVoucher, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ApRecurringVoucherEntryControllerBean addApDrEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            // validate if coa exists

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(mdetails.getDrCoaAccountNumber(), AD_BRNCH, AD_CMPNY);

                if (glChartOfAccount.getCoaEnable() == EJBCommon.FALSE)
                    throw new GlobalBranchAccountNumberInvalidException();

            } catch (FinderException ex) {

                throw new GlobalBranchAccountNumberInvalidException();
            }

            // create distribution record

            LocalApDistributionRecord apDistributionRecord = apDistributionRecordHome.create(mdetails.getDrLine(), mdetails.getDrClass(), EJBCommon.roundIt(mdetails.getDrAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()), mdetails.getDrDebit(), EJBCommon.FALSE, EJBCommon.FALSE, AD_CMPNY);

            apRecurringVoucher.addApDistributionRecord(apDistributionRecord);
            glChartOfAccount.addApDistributionRecord(apDistributionRecord);

        } catch (GlobalBranchAccountNumberInvalidException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApRecurringVoucherEntryControllerBean ejbCreate");
    }
}