package com.ejb.txnreports.ap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ap.LocalApAppliedVoucher;
import com.ejb.dao.ap.LocalApAppliedVoucherHome;
import com.ejb.entities.ap.LocalApSupplierClass;
import com.ejb.dao.ap.LocalApSupplierClassHome;
import com.ejb.entities.ap.LocalApSupplierType;
import com.ejb.dao.ap.LocalApSupplierTypeHome;
import com.ejb.entities.ap.LocalApVoucher;
import com.ejb.dao.ap.LocalApVoucherHome;
import com.ejb.entities.ap.LocalApVoucherPaymentSchedule;
import com.ejb.dao.ap.LocalApVoucherPaymentScheduleHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.ad.AdPreferenceDetails;
import com.util.reports.ap.ApRepAgingSummaryDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;

@Stateless(name = "ApRepAgingSummaryControllerEJB")
public class ApRepAgingSummaryControllerBean extends EJBContextClass implements ApRepAgingSummaryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalApSupplierClassHome apSupplierClassHome;
    @EJB
    private LocalApSupplierTypeHome apSupplierTypeHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalApVoucherPaymentScheduleHome apVoucherPaymentScheduleHome;
    @EJB
    private LocalApVoucherHome apVoucherHome;
    @EJB
    private LocalApAppliedVoucherHome apAppliedVoucherHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;

    // Execute AP Aging Summary
    public ArrayList executeApRepAgingSummary(HashMap criteria, String AGNG_BY, String GROUP_BY, ArrayList adBrnchList, String currency, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepAgingSummaryControllerBean executeApRepAgingSummary");

        ArrayList list = new ArrayList();

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            short precisionUnit = adCompany.getGlFunctionalCurrency().getFcPrecision();
            Date agingDate = null;

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(adCompany.getCmpCode());
            int agingBucket1 = adPreference.getPrfApAgingBucket(); // 30 - default
            int agingBucket2 = adPreference.getPrfApAgingBucket() * 2; // 60
            int agingBucket3 = adPreference.getPrfApAgingBucket() * 3; // 90
            int agingBucket4 = adPreference.getPrfApAgingBucket() * 4; // 120

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(vps) FROM ApVoucherPaymentSchedule vps ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("supplierCode")) {

                criteriaSize--;
            }

            if (criteria.containsKey("includeUnpostedTransaction")) {

                criteriaSize--;
            }

            if (criteria.containsKey("includePaid")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("supplierCode")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("vps.apVoucher.apSupplier.splSupplierCode LIKE '%").append(criteria.get("supplierCode")).append("%' ");
            }

            if (criteria.containsKey("supplierType")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("vps.apVoucher.apSupplier.apSupplierType.stName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("supplierType");
                ctr++;
            }

            if (criteria.containsKey("supplierClass")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("vps.apVoucher.apSupplier.apSupplierClass.scName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("supplierClass");
                ctr++;
            }

            if (criteria.containsKey("date")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("vps.apVoucher.vouDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("date");
                agingDate = (Date) criteria.get("date");
                ctr++;
            }

            if (criteria.containsKey("includeUnpostedTransaction")) {

                String unposted = (String) criteria.get("includeUnpostedTransaction");

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                if (unposted.equals("NO")) {

                    jbossQl.append("vps.apVoucher.vouPosted = 1 ");

                } else {

                    jbossQl.append("vps.apVoucher.vouVoid = 0 ");
                }
            }

            if (adBrnchList.isEmpty()) {

                throw new GlobalNoRecordFoundException();

            } else {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("vps.apVoucher.vouAdBranch in (");

                boolean firstLoop = true;

                for (Object o : adBrnchList) {

                    if (!firstLoop) {
                        jbossQl.append(", ");
                    } else {
                        firstLoop = false;
                    }

                    AdBranchDetails mdetails = (AdBranchDetails) o;

                    jbossQl.append(mdetails.getBrCode());
                }

                jbossQl.append(") ");

                firstArgument = false;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("vps.apVoucher.vouDebitMemo = 0 AND vps.vpsAdCompany=").append(AD_CMPNY);

            String includePaid = (String) criteria.get("includePaid");

            Collection apVoucherPaymentSchedules = apVoucherPaymentScheduleHome.getVpsByCriteria(jbossQl.toString(), obj);

            if (apVoucherPaymentSchedules.size() == 0) throw new GlobalNoRecordFoundException();

            for (Object voucherPaymentSchedule : apVoucherPaymentSchedules) {

                LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = (LocalApVoucherPaymentSchedule) voucherPaymentSchedule;

                LocalApVoucher apVoucher = apVoucherPaymentSchedule.getApVoucher();

                ApRepAgingSummaryDetails mdetails = new ApRepAgingSummaryDetails();

                mdetails.setAgSupplierCode(apVoucher.getApSupplier().getSplName());
                mdetails.setAgSupplierClass(apVoucher.getApSupplier().getApSupplierClass().getScName());
                mdetails.setAgDescription(apVoucher.getVouDescription());
                if (apVoucher.getApSupplier().getApSupplierType() == null) {

                    mdetails.setAgSupplierType("UNDEFINE");
                } else {

                    mdetails.setAgSupplierType(apVoucher.getApSupplier().getApSupplierType().getStName());
                }
                mdetails.setGroupBy(GROUP_BY);

                double AMNT_DUE = 0d;
                double VOUCHER_AMOUNT = apVoucherPaymentSchedule.getVpsAmountDue();

                if (includePaid.equals("NO")) {

                    // Get Debit Memos
                    double DM_AMNT = getDebitMemos(AD_CMPNY, precisionUnit, agingDate, apVoucherPaymentSchedule, apVoucher);

                    // Get Checks
                    double CHK_AMNT = getAppliedVoucherChecks(AD_CMPNY, agingDate, apVoucherPaymentSchedule);

                    VOUCHER_AMOUNT = VOUCHER_AMOUNT - apVoucherPaymentSchedule.getVpsAmountPaid();
                    VOUCHER_AMOUNT = VOUCHER_AMOUNT + DM_AMNT;
                    VOUCHER_AMOUNT = VOUCHER_AMOUNT + CHK_AMNT;

                }

                // Get Amount Due
                AMNT_DUE = getVoucherAmountDue(currency, AD_CMPNY, apVoucher, VOUCHER_AMOUNT);

                mdetails.setAgAmount(AMNT_DUE);

                // Get Voucher Age
                int VOUCHER_AGE = 0;

                if (AGNG_BY.equals("DUE DATE")) {

                    VOUCHER_AGE = (short) ((agingDate.getTime() - apVoucherPaymentSchedule.getVpsDueDate().getTime()) / (1000 * 60 * 60 * 24));

                } else {

                    VOUCHER_AGE = (short) ((agingDate.getTime() - apVoucher.getVouDate().getTime()) / (1000 * 60 * 60 * 24));
                }

                if (VOUCHER_AGE <= 0) {

                    mdetails.setAgBucket0(AMNT_DUE);

                } else if (VOUCHER_AGE >= 1 && VOUCHER_AGE <= agingBucket1) {

                    mdetails.setAgBucket1(AMNT_DUE);

                } else if (VOUCHER_AGE >= (agingBucket1 + 1) && VOUCHER_AGE <= agingBucket2) {

                    mdetails.setAgBucket2(AMNT_DUE);

                } else if (VOUCHER_AGE >= (agingBucket2 + 1) && VOUCHER_AGE <= agingBucket3) {

                    mdetails.setAgBucket3(AMNT_DUE);

                } else if (VOUCHER_AGE >= (agingBucket3 + 1) && VOUCHER_AGE <= agingBucket4) {

                    mdetails.setAgBucket4(AMNT_DUE);

                } else if (VOUCHER_AGE > agingBucket4) {

                    mdetails.setAgBucket5(AMNT_DUE);
                }

                mdetails.setAgVouFcSymbol(apVoucher.getGlFunctionalCurrency().getFcSymbol());

                list.add(mdetails);

                if (includePaid.equals("YES") && apVoucherPaymentSchedule.getVpsAmountPaid() != 0) {

                    // get dm on or after aging date

                    double DM_AMNT = 0d;

                    Collection apDebitMemos = apVoucherHome.findByVouDebitMemoAndVouDmVoucherNumberAndVouVoidAndVouPosted(EJBCommon.TRUE, apVoucher.getVouDocumentNumber(), EJBCommon.FALSE, EJBCommon.TRUE, AD_CMPNY);

                    for (Object debitMemo : apDebitMemos) {

                        LocalApVoucher apDebitMemo = (LocalApVoucher) debitMemo;

                        mdetails = new ApRepAgingSummaryDetails();
                        mdetails.setAgSupplierCode(apVoucher.getApSupplier().getSplName());
                        mdetails.setAgSupplierClass(apVoucher.getApSupplier().getApSupplierClass().getScName());
                        mdetails.setAgDescription(apVoucher.getVouDescription());

                        if (apVoucher.getApSupplier().getApSupplierType() == null) {
                            mdetails.setAgSupplierType("UNDEFINE");
                        } else {
                            mdetails.setAgSupplierType(apVoucher.getApSupplier().getApSupplierType().getStName());
                        }

                        mdetails.setGroupBy(GROUP_BY);

                        if (apDebitMemo.getVouDate().before(agingDate) || apDebitMemo.getVouDate().equals(agingDate)) {

                            if (currency.equals("USD") && apVoucher.getGlFunctionalCurrency().getFcName().equals("USD")) {

                                DM_AMNT = (EJBCommon.roundIt(apDebitMemo.getVouBillAmount() * (apVoucherPaymentSchedule.getVpsAmountDue() / apVoucher.getVouAmountDue()), precisionUnit)) * -1;

                            } else {
                                DM_AMNT = this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), EJBCommon.roundIt(apDebitMemo.getVouBillAmount() * (apVoucherPaymentSchedule.getVpsAmountDue() / apVoucher.getVouAmountDue()), precisionUnit), AD_CMPNY) * -1;
                            }

                            mdetails.setAgAmount(DM_AMNT);

                            if (VOUCHER_AGE <= 0) {

                                mdetails.setAgBucket0(DM_AMNT);

                            } else if (VOUCHER_AGE >= 1 && VOUCHER_AGE <= agingBucket1) {

                                mdetails.setAgBucket1(DM_AMNT);

                            } else if (VOUCHER_AGE >= (agingBucket1 + 1) && VOUCHER_AGE <= agingBucket2) {

                                mdetails.setAgBucket2(DM_AMNT);

                            } else if (VOUCHER_AGE >= (agingBucket2 + 1) && VOUCHER_AGE <= agingBucket3) {

                                mdetails.setAgBucket3(DM_AMNT);

                            } else if (VOUCHER_AGE >= (agingBucket3 + 1) && VOUCHER_AGE <= agingBucket4) {

                                mdetails.setAgBucket4(DM_AMNT);

                            } else if (VOUCHER_AGE > agingBucket4) {

                                mdetails.setAgBucket5(DM_AMNT);
                            }
                            mdetails.setAgVouFcSymbol(apVoucher.getGlFunctionalCurrency().getFcSymbol());
                            list.add(mdetails);
                        }
                    }

                    // get checks on or before aging date

                    double CHK_AMNT = 0d;

                    Collection apAppliedVouchers = apAppliedVoucherHome.findPostedAvByVpsCode(apVoucherPaymentSchedule.getVpsCode(), AD_CMPNY);

                    for (Object appliedVoucher : apAppliedVouchers) {

                        LocalApAppliedVoucher apAppliedVoucher = (LocalApAppliedVoucher) appliedVoucher;

                        mdetails = new ApRepAgingSummaryDetails();
                        mdetails.setAgSupplierCode(apVoucher.getApSupplier().getSplName());
                        mdetails.setAgSupplierClass(apVoucher.getApSupplier().getApSupplierClass().getScName());
                        mdetails.setAgDescription(apVoucher.getVouDescription());
                        if (apVoucher.getApSupplier().getApSupplierType() == null) {

                            mdetails.setAgSupplierType("UNDEFINE");

                        } else {

                            mdetails.setAgSupplierType(apVoucher.getApSupplier().getApSupplierType().getStName());
                        }
                        mdetails.setGroupBy(GROUP_BY);

                        if (apAppliedVoucher.getApCheck().getChkDate().before(agingDate) || apAppliedVoucher.getApCheck().getChkDate().equals(agingDate)) {

                            if (currency.equals("USD") && apVoucher.getGlFunctionalCurrency().getFcName().equals("USD")) {

                                DM_AMNT = (apAppliedVoucher.getAvApplyAmount() + apAppliedVoucher.getAvDiscountAmount() + apAppliedVoucher.getAvTaxWithheld()) * -1;

                            } else {
                                CHK_AMNT = this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apAppliedVoucher.getAvApplyAmount() + apAppliedVoucher.getAvDiscountAmount() + apAppliedVoucher.getAvTaxWithheld(), AD_CMPNY) * -1;
                            }

                            mdetails.setAgAmount(CHK_AMNT);

                            if (VOUCHER_AGE <= 0) {

                                mdetails.setAgBucket0(CHK_AMNT);

                            } else if (VOUCHER_AGE >= 1 && VOUCHER_AGE <= agingBucket1) {

                                mdetails.setAgBucket1(CHK_AMNT);

                            } else if (VOUCHER_AGE >= (agingBucket1 + 1) && VOUCHER_AGE <= agingBucket2) {

                                mdetails.setAgBucket2(CHK_AMNT);

                            } else if (VOUCHER_AGE >= (agingBucket2 + 1) && VOUCHER_AGE <= agingBucket3) {

                                mdetails.setAgBucket3(CHK_AMNT);

                            } else if (VOUCHER_AGE >= (agingBucket3 + 1) && VOUCHER_AGE <= agingBucket4) {

                                mdetails.setAgBucket4(CHK_AMNT);

                            } else if (VOUCHER_AGE > agingBucket4) {

                                mdetails.setAgBucket5(CHK_AMNT);
                            }
                            mdetails.setAgVouFcSymbol(apVoucher.getGlFunctionalCurrency().getFcSymbol());

                            list.add(mdetails);
                        }
                    }
                }
            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            switch (GROUP_BY) {
                case "SUPPLIER CODE":

                    list.sort(ApRepAgingSummaryDetails.SupplierCodeComparator);

                    break;
                case "SUPPLIER TYPE":

                    list.sort(ApRepAgingSummaryDetails.SupplierTypeComparator);

                    break;
                case "SUPPLIER CLASS":

                    list.sort(ApRepAgingSummaryDetails.SupplierClassComparator);
                    break;
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // Get All Supplier Class
    public ArrayList getApScAll(Integer AD_CMPNY) {

        Debug.print("ApRepAgingSummaryControllerBean getApScAll");

        ArrayList list = new ArrayList();

        try {

            Collection apSupplierClasses = apSupplierClassHome.findEnabledScAll(AD_CMPNY);
            for (Object supplierClass : apSupplierClasses) {
                LocalApSupplierClass apSupplierClass = (LocalApSupplierClass) supplierClass;
                list.add(apSupplierClass.getScName());
            }

            return list;

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // Get All Supplier Types
    public ArrayList getApStAll(Integer AD_CMPNY) {

        Debug.print("ApRepAgingSummaryControllerBean getApStAll");

        ArrayList list = new ArrayList();

        try {

            Collection apSupplierTypes = apSupplierTypeHome.findEnabledStAll(AD_CMPNY);
            for (Object supplierType : apSupplierTypes) {
                LocalApSupplierType apSupplierType = (LocalApSupplierType) supplierType;
                list.add(apSupplierType.getStName());
            }

            return list;

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // Get Company Details
    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("ApRepAgingSummaryControllerBean getAdCompany");

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

    // Get Branch Responsibility
    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepAgingSummaryControllerBean getAdBrResAll");

        LocalAdBranchResponsibility adBranchResponsibility = null;
        LocalAdBranch adBranch = null;

        Collection adBranchResponsibilities = null;
        ArrayList list = new ArrayList();

        try {

            adBranchResponsibilities = adBranchResponsibilityHome.findByAdResponsibility(RS_CODE, AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (adBranchResponsibilities.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }

        try {

            for (Object branchResponsibility : adBranchResponsibilities) {

                adBranchResponsibility = (LocalAdBranchResponsibility) branchResponsibility;
                adBranch = adBranchResponsibility.getAdBranch();

                AdBranchDetails details = new AdBranchDetails();

                details.setBrCode(adBranch.getBrCode());
                details.setBrBranchCode(adBranch.getBrBranchCode());
                details.setBrName(adBranch.getBrName());
                details.setBrHeadQuarter(adBranch.getBrHeadQuarter());

                list.add(details);
            }

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        return list;
    }

    // Get Ad Preference
    public AdPreferenceDetails getAdPreference(Integer AD_CMPNY) {

        Debug.print("ApRepAgingSummaryControllerBean getAdPreference");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            AdPreferenceDetails details = new AdPreferenceDetails();
            details.setPrfApAgingBucket(adPreference.getPrfApAgingBucket());
            details.setPrfArAgingBucket(adPreference.getPrfArAgingBucket());
            return details;

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // PRIVATE METHODS

    private double getAppliedVoucherChecks(Integer AD_CMPNY, Date agingDate, LocalApVoucherPaymentSchedule apVoucherPaymentSchedule) throws FinderException { // Get Future Checks from Applied Vouchers
        double CHK_AMNT = 0d;

        Collection apAppliedVouchers = apAppliedVoucherHome.findPostedAvByVpsCode(apVoucherPaymentSchedule.getVpsCode(), AD_CMPNY);

        for (Object appliedVoucher : apAppliedVouchers) {

            LocalApAppliedVoucher apAppliedVoucher = (LocalApAppliedVoucher) appliedVoucher;

            if (apAppliedVoucher.getApCheck().getChkDate().after(agingDate)) {

                CHK_AMNT += apAppliedVoucher.getAvApplyAmount() + apAppliedVoucher.getAvDiscountAmount() + apAppliedVoucher.getAvTaxWithheld();
            }
        }
        return CHK_AMNT;
    }

    private double getDebitMemos(Integer AD_CMPNY, short precisionUnit, Date agingDate, LocalApVoucherPaymentSchedule apVoucherPaymentSchedule, LocalApVoucher apVoucher) throws FinderException { // Get Future Debit Memo (Voucher)
        double DM_AMNT = 0d;

        Collection apDebitMemos = apVoucherHome.findByVouDebitMemoAndVouDmVoucherNumberAndVouVoidAndVouPosted(EJBCommon.TRUE, apVoucher.getVouDocumentNumber(), EJBCommon.FALSE, EJBCommon.TRUE, AD_CMPNY);

        for (Object debitMemo : apDebitMemos) {

            LocalApVoucher apDebitMemo = (LocalApVoucher) debitMemo;

            if (apDebitMemo.getVouDate().after(agingDate)) {

                DM_AMNT += EJBCommon.roundIt(apDebitMemo.getVouBillAmount() * (apVoucherPaymentSchedule.getVpsAmountDue() / apVoucher.getVouAmountDue()), precisionUnit);
            }
        }
        return DM_AMNT;
    }

    private double getVoucherAmountDue(String currency, Integer AD_CMPNY, LocalApVoucher apVoucher, double VOUCHER_AMOUNT) {
        double AMNT_DUE;
        if (currency.equals("USD") && apVoucher.getGlFunctionalCurrency().getFcName().equals("USD")) {

            AMNT_DUE = VOUCHER_AMOUNT;

        } else {

            AMNT_DUE = this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), VOUCHER_AMOUNT, AD_CMPNY);
        }
        return AMNT_DUE;
    }

    // Convert Foreign To Functional Currency
    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("ApRepAgingSummaryControllerBean convertForeignToFunctionalCurrency");

        LocalAdCompany adCompany = null;

        // get company and extended precision

        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // Convert to functional currency if necessary

        if (CONVERSION_RATE != 1 && CONVERSION_RATE != 0) {

            AMOUNT = AMOUNT / CONVERSION_RATE;
        }
        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ApRepAgingSummaryControllerBean ejbCreate");
    }
}