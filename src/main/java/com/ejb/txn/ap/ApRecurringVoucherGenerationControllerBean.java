/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ApRecurringVoucherGenerationControllerBean
 * @created February 20, 2004, 9:54 AM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txn.ap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
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
import com.ejb.entities.ad.LocalAdDocumentSequenceAssignment;
import com.ejb.dao.ad.LocalAdDocumentSequenceAssignmentHome;
import com.ejb.entities.ad.LocalAdPaymentSchedule;
import com.ejb.entities.ad.LocalAdPaymentTerm;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.dao.ap.ILocalApSupplierHome;
import com.ejb.entities.ap.LocalApDistributionRecord;
import com.ejb.dao.ap.LocalApDistributionRecordHome;
import com.ejb.entities.ap.LocalApRecurringVoucher;
import com.ejb.dao.ap.LocalApRecurringVoucherHome;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.entities.ap.LocalApSupplierBalance;
import com.ejb.dao.ap.LocalApSupplierBalanceHome;
import com.ejb.entities.ap.LocalApVoucher;
import com.ejb.dao.ap.LocalApVoucherBatch;
import com.ejb.dao.ap.LocalApVoucherBatchHome;
import com.ejb.dao.ap.LocalApVoucherHome;
import com.ejb.entities.ap.LocalApVoucherPaymentSchedule;
import com.ejb.dao.ap.LocalApVoucherPaymentScheduleHome;
import com.ejb.exception.global.GlobalAmountInvalidException;
import com.ejb.exception.global.GlobalDocumentNumberNotUniqueException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.util.mod.ap.ApModRecurringVoucherDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ApRecurringVoucherGenerationControllerEJB")
public class ApRecurringVoucherGenerationControllerBean extends EJBContextClass implements ApRecurringVoucherGenerationController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalApSupplierHome apSupplierHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    private LocalApRecurringVoucherHome apRecurringJournalHome;
    @EJB
    private LocalApRecurringVoucherHome apRecurringVoucherHome;
    @EJB
    private LocalApSupplierBalanceHome apSupplierBalanceHome;
    @EJB
    private LocalApVoucherBatchHome apVoucherBatchHome;
    @EJB
    private LocalApVoucherHome apVoucherHome;
    @EJB
    private LocalApVoucherPaymentScheduleHome apVoucherPaymentScheduleHome;


    public ArrayList getApSplAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ApRecurringVoucherGenerationControllerBean getApSplAll");

        ArrayList list = new ArrayList();

        try {

            Collection apSuppliers = apSupplierHome.findEnabledSplAll(AD_BRNCH, AD_CMPNY);

            for (Object supplier : apSuppliers) {

                LocalApSupplier apSupplier = (LocalApSupplier) supplier;

                list.add(apSupplier.getSplSupplierCode());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApRvByCriteria(HashMap criteria, String ORDER_BY, Integer OFFSET, Integer LIMIT, double RV_INTRST_RT, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRecurringVoucherGenerationControllerBean getApRvByCriteria");

        ArrayList rvList = new ArrayList();

        StringBuilder jbossQl = new StringBuilder();
        jbossQl.append("SELECT OBJECT(rv) FROM ApRecurringVoucher rv ");

        boolean firstArgument = true;
        short ctr = 0;
        int criteriaSize = criteria.size();
        Object[] obj;

        // Allocate the size of the object parameter

        if (criteria.containsKey("name")) {

            criteriaSize--;
        }

        obj = new Object[criteriaSize];

        if (criteria.containsKey("name")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("rv.rvName LIKE '%").append(criteria.get("name")).append("%' ");
        }

        if (criteria.containsKey("supplierCode")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("rv.apSupplier.splSupplierCode=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("supplierCode");
            ctr++;
        }

        if (criteria.containsKey("nextRunDateFrom")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("rv.rvNextRunDate>=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("nextRunDateFrom");
            ctr++;
        }

        if (criteria.containsKey("nextRunDateTo")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("rv.rvNextRunDate<=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("nextRunDateTo");
            ctr++;
        }

        if (!firstArgument) {
            jbossQl.append("AND ");
        } else {
            firstArgument = false;
            jbossQl.append("WHERE ");
        }

        jbossQl.append("rv.rvAdBranch=").append(AD_BRNCH).append(" ");

        if (!firstArgument) {
            jbossQl.append("AND ");
        } else {
            firstArgument = false;
            jbossQl.append("WHERE ");
        }

        jbossQl.append("rv.rvAdCompany=").append(AD_CMPNY).append(" ");

        String orderBy = null;

        switch (ORDER_BY) {
            case "NAME":

                orderBy = "rv.rvName";

                break;
            case "NEXT RUN DATE":

                orderBy = "rv.rvNextRunDate";

                break;
            case "SUPPLIER CODE":

                orderBy = "rv.apSupplier.splSupplierCode";
                break;
        }

        if (orderBy != null) {

            jbossQl.append("ORDER BY ").append(orderBy);
        }

        Debug.print("QL + " + jbossQl);

        Collection apRecurringJournals = null;

        try {

            apRecurringJournals = apRecurringJournalHome.getRvByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (apRecurringJournals.isEmpty()) throw new GlobalNoRecordFoundException();

        for (Object apRecurringJournal : apRecurringJournals) {

            LocalApRecurringVoucher apRecurringVoucher = (LocalApRecurringVoucher) apRecurringJournal;

            // generate new next run date

            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(apRecurringVoucher.getRvNextRunDate());

            switch (apRecurringVoucher.getRvSchedule()) {
                case "DAILY":

                    gc.add(Calendar.DATE, 1);

                    break;
                case "WEEKLY":

                    gc.add(Calendar.DATE, 7);

                    break;
                case "SEMI MONTHLY":

                    gc.add(Calendar.DATE, 15);

                    break;
                case "MONTHLY":

                    gc.add(Calendar.MONTH, 1);

                    break;
                case "QUARTERLY":

                    gc.add(Calendar.MONTH, 3);

                    break;
                case "SEMI ANNUALLY":

                    gc.add(Calendar.MONTH, 6);

                    break;
                case "ANNUALLY":

                    gc.add(Calendar.YEAR, 1);
                    break;
            }

            ApModRecurringVoucherDetails mdetails = new ApModRecurringVoucherDetails();
            mdetails.setRvCode(apRecurringVoucher.getRvCode());
            mdetails.setRvName(apRecurringVoucher.getRvName());
            mdetails.setRvNextRunDate(apRecurringVoucher.getRvNextRunDate());
            mdetails.setRvSplSupplierCode(apRecurringVoucher.getApSupplier().getSplSupplierCode());
            mdetails.setRvSchedule(apRecurringVoucher.getRvSchedule());
            mdetails.setRvLastRunDate(apRecurringVoucher.getRvLastRunDate());
            mdetails.setRvNewNextRunDate(gc.getTime());
            mdetails.setRvInterestRate(RV_INTRST_RT);

            rvList.add(mdetails);
        }

        return rvList;
    }

    public void executeApRvGeneration(Integer RV_CODE, Date RV_NXT_RN_DT, String VOU_DCMNT_NMBR, Date VOU_DT, double RV_INTRST_RT, String USR_NM, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalAmountInvalidException {

        Debug.print("ApRecurringVoucherGenerationControllerBean executeApRvGeneration");

        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSSS");

        try {

            // get recurring voucher

            LocalApRecurringVoucher apRecurringVoucher = null;

            try {

                apRecurringVoucher = apRecurringVoucherHome.findByPrimaryKey(RV_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if document number is unique

            try {

                LocalApVoucher apExistingVoucher = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(VOU_DCMNT_NMBR, EJBCommon.FALSE, AD_BRNCH, AD_CMPNY);

                throw new GlobalDocumentNumberNotUniqueException(apRecurringVoucher.getRvName());

            } catch (FinderException ex) {
            }

            // generate document number if necessary

            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AP VOUCHER", AD_CMPNY);

            if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (VOU_DCMNT_NMBR == null || VOU_DCMNT_NMBR.trim().length() == 0)) {

                while (true) {

                    try {

                        apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), EJBCommon.FALSE, AD_BRNCH, AD_CMPNY);
                        adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                    } catch (FinderException ex) {

                        VOU_DCMNT_NMBR = adDocumentSequenceAssignment.getDsaNextSequence();
                        adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                        break;
                    }
                }
            }

            // generate approval status

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            String VOU_APPRVL_STATUS = null;

            // generate voucher batch if necessary

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            LocalApVoucherBatch apVoucherBatch = null;

            if (adPreference.getPrfEnableApVoucherBatch() == EJBCommon.TRUE) {

                apVoucherBatch = apRecurringVoucher.getApVoucherBatch();
            }

            // generate recurring voucher

            LocalApVoucher apVoucher = apVoucherHome.create("EXPENSES", EJBCommon.FALSE, apRecurringVoucher.getRvDescription(), VOU_DT, VOU_DCMNT_NMBR, VOU_DCMNT_NMBR, null, apRecurringVoucher.getRvConversionDate(), apRecurringVoucher.getRvConversionRate(), apRecurringVoucher.getRvAmount(), apRecurringVoucher.getRvAmountDue(), 0d, VOU_APPRVL_STATUS, null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null, null, null, null, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, AD_BRNCH, AD_CMPNY);

            apVoucher.setAdPaymentTerm(apRecurringVoucher.getAdPaymentTerm());
            apVoucher.setApTaxCode(apRecurringVoucher.getApTaxCode());
            apVoucher.setApWithholdingTaxCode(apRecurringVoucher.getApWithholdingTaxCode());
            apVoucher.setGlFunctionalCurrency(adCompany.getGlFunctionalCurrency());
            apVoucher.setApSupplier(apRecurringVoucher.getApSupplier());

            if (apVoucherBatch != null) {

                apVoucherBatch.addApVoucher(apVoucher);
            }

            double interestIncome = 0;
            if (apRecurringVoucher.getRvName().equals("Interest Income")) {

                // get interest income
                interestIncome = getInterestIncome(apRecurringVoucher.getApSupplier(), VOU_DT, RV_INTRST_RT, AD_CMPNY);

                // set voucher reference number
                GregorianCalendar firstDateGc = new GregorianCalendar();
                firstDateGc.setTime(VOU_DT);
                firstDateGc.add(Calendar.MONTH, -1);

                GregorianCalendar lastDateGc = new GregorianCalendar(firstDateGc.get(Calendar.YEAR), firstDateGc.get(Calendar.MONTH), firstDateGc.getActualMaximum(Calendar.DATE), 0, 0, 0);

                apVoucher.setVouReferenceNumber("INT-" + EJBCommon.convertSQLDateToString(lastDateGc.getTime()));

                // set voucher amount
                apVoucher.setVouBillAmount(interestIncome);
                apVoucher.setVouAmountDue(interestIncome);
            }

            Collection apDistributionRecords = apRecurringVoucher.getApDistributionRecords();

            Iterator i = apDistributionRecords.iterator();

            while (i.hasNext()) {

                LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                double drAmount = EJBCommon.roundIt(apDistributionRecord.getDrAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision());

                if (apRecurringVoucher.getRvName().equals("Interest Income")) {
                    drAmount = EJBCommon.roundIt(interestIncome, adCompany.getGlFunctionalCurrency().getFcPrecision());
                }

                LocalApDistributionRecord apNewDistributionRecord = apDistributionRecordHome.create(apDistributionRecord.getDrLine(), apDistributionRecord.getDrClass(), drAmount, apDistributionRecord.getDrDebit(), EJBCommon.FALSE, EJBCommon.FALSE, AD_CMPNY);

                apDistributionRecord.getGlChartOfAccount().addApDistributionRecord(apNewDistributionRecord);

                apVoucher.addApDistributionRecord(apNewDistributionRecord);
            }

            // create voucher payment schedule

            short precisionUnit = this.getGlFcPrecisionUnit(AD_CMPNY);
            double TOTAL_PAYMENT_SCHEDULE = 0d;

            LocalAdPaymentTerm adPaymentTerm = apRecurringVoucher.getAdPaymentTerm();

            Collection adPaymentSchedules = adPaymentTerm.getAdPaymentSchedules();

            i = adPaymentSchedules.iterator();

            while (i.hasNext()) {

                LocalAdPaymentSchedule adPaymentSchedule = (LocalAdPaymentSchedule) i.next();

                // get date due

                GregorianCalendar gcDateDue = new GregorianCalendar();
                gcDateDue.setTime(apVoucher.getVouDate());
                gcDateDue.add(Calendar.DATE, adPaymentSchedule.getPsDueDay());

                // create a payment schedule

                double PAYMENT_SCHEDULE_AMOUNT = 0;

                // if last payment schedule subtract to avoid rounding difference error

                if (i.hasNext()) {

                    PAYMENT_SCHEDULE_AMOUNT = EJBCommon.roundIt((adPaymentSchedule.getPsRelativeAmount() / adPaymentTerm.getPytBaseAmount()) * apVoucher.getVouAmountDue(), precisionUnit);

                } else {

                    PAYMENT_SCHEDULE_AMOUNT = apVoucher.getVouAmountDue() - TOTAL_PAYMENT_SCHEDULE;
                }

                LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = apVoucherPaymentScheduleHome.create(gcDateDue.getTime(), adPaymentSchedule.getPsLineNumber(), PAYMENT_SCHEDULE_AMOUNT, 0d, EJBCommon.FALSE, AD_CMPNY);

                apVoucher.addApVoucherPaymentSchedule(apVoucherPaymentSchedule);

                TOTAL_PAYMENT_SCHEDULE += PAYMENT_SCHEDULE_AMOUNT;
            }

            // set new run date

            apRecurringVoucher.setRvNextRunDate(RV_NXT_RN_DT);
            apRecurringVoucher.setRvLastRunDate(EJBCommon.getGcCurrentDateWoTime().getTime());

        } catch (GlobalRecordAlreadyDeletedException | GlobalAmountInvalidException |
                 GlobalDocumentNumberNotUniqueException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ApRecurringVoucherGenerationControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfApUseSupplierPulldown(Integer AD_CMPNY) {

        Debug.print("ApRecurringVoucherGenerationControllerBean getAdPrfApUseSupplierPulldown");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfApUseSupplierPulldown();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private double getInterestIncome(LocalApSupplier apSupplier, Date VOU_DT, double RV_INTRST_RT, Integer AD_CMPNY) throws GlobalAmountInvalidException {

        Debug.print("ApRecurringVoucherGenerationControllerBean getInterestIncome");

        double totalInterestIncome = 0d;
        double interestIncome = 0;
        double interest = 0d;

        try {

            GregorianCalendar firstDateGc = new GregorianCalendar();
            firstDateGc.setTime(VOU_DT);
            firstDateGc.add(Calendar.MONTH, -1);

            GregorianCalendar lastDateGc = new GregorianCalendar(firstDateGc.get(Calendar.YEAR), firstDateGc.get(Calendar.MONTH), firstDateGc.getActualMaximum(Calendar.DATE), 0, 0, 0);

            GregorianCalendar lastLastMonthGc = new GregorianCalendar();
            lastLastMonthGc.setTime(lastDateGc.getTime());
            lastLastMonthGc.add(Calendar.MONTH, -1);

            Collection apSupplierBalances = apSupplierBalanceHome.findBySbDateFromAndSbDateToAndSplCode(lastLastMonthGc.getTime(), lastDateGc.getTime(), apSupplier.getSplCode(), AD_CMPNY);

            if (!apSupplierBalances.isEmpty()) {

                long inclusiveDays = 0;

                // A.) calculate interest if first txn is not on the first day of the period

                ArrayList apSupplierBalancesList = new ArrayList(apSupplierBalances);

                LocalApSupplierBalance apSupplierBalance = (LocalApSupplierBalance) apSupplierBalancesList.get(0);

                if (apSupplierBalance.getSbDate().after(firstDateGc.getTime())) {

                    double balance = 0;

                    Collection apSupplierPriorBalances = apSupplierBalanceHome.findByBeforeSbDateAndSplCode(firstDateGc.getTime(), apSupplier.getSplCode(), AD_CMPNY);

                    if (!apSupplierPriorBalances.isEmpty()) {

                        ArrayList apSupplierPriorBalanceList = new ArrayList(apSupplierPriorBalances);

                        LocalApSupplierBalance apSupplierPriorBalance = (LocalApSupplierBalance) apSupplierPriorBalanceList.get(apSupplierPriorBalanceList.size() - 1);

                        balance = apSupplierPriorBalance.getSbBalance();
                    }

                    inclusiveDays = (VOU_DT.getTime() - apSupplierBalance.getSbDate().getTime()) / (1000 * 60 * 60 * 24);

                    if (balance > 500000) {

                        interestIncome = balance * (((RV_INTRST_RT + 0.5) / 100) / 365) * inclusiveDays;

                    } else {

                        interestIncome = balance * ((RV_INTRST_RT / 100) / 365) * inclusiveDays;
                    }
                }

                if (!apSupplierBalances.isEmpty()) {

                    double balance = 0d;

                    double baseInterest = 0d;
                    double bonusInterest = 0d;
                    double totalBaseInterest = 0d;
                    double totalBonusInterest = 0d;
                    double totalInclusiveDays = 0d;

                    apSupplierBalancesList = new ArrayList(apSupplierBalances);

                    apSupplierBalance = (LocalApSupplierBalance) apSupplierBalancesList.get(0);

                    Date olderDateBeforeVouDate = apSupplierBalance.getSbDate();
                    Date latestDateBeforeVouDate = apSupplierBalance.getSbDate();

                    Collection apLatestDateAndBalances = apSupplierBalanceHome.findBySbDateFromAndSbDateToAndSplCode(apSupplierBalance.getSbDate(), lastDateGc.getTime(), apSupplier.getSplCode(), AD_CMPNY);

                    for (Object latestDateAndBalance : apLatestDateAndBalances) {
                        LocalApSupplierBalance apLatestDateAndBalance = (LocalApSupplierBalance) latestDateAndBalance;

                        if ((apLatestDateAndBalance.getSbDate().getTime() <= lastDateGc.getTime().getTime()) && (apLatestDateAndBalance.getSbDate().getTime() >= olderDateBeforeVouDate.getTime())) {
                            latestDateBeforeVouDate = apLatestDateAndBalance.getSbDate();
                        }

                        balance = apLatestDateAndBalance.getSbBalance();

                        inclusiveDays = ((latestDateBeforeVouDate.getTime() - olderDateBeforeVouDate.getTime()) / (24 * 60 * 60 * 1000));
                        totalInclusiveDays += inclusiveDays;

                        // B.) calculate period interest income BASE

                        baseInterest = (balance) * ((RV_INTRST_RT / 100) / 365) * inclusiveDays;
                        totalBaseInterest += baseInterest;

                        // C.) compute interest for inexcess of 500k BONUS
                        if ((interestIncome + balance) > 500000) {
                            bonusInterest = (balance - 500000) * ((0.5 / 100) / 365) * inclusiveDays;
                        }
                        totalBonusInterest += bonusInterest;

                        interestIncome = baseInterest + bonusInterest;
                        totalInterestIncome += interestIncome;

                        Debug.print("Since Date : " + olderDateBeforeVouDate);
                        Debug.print("Balance : " + balance);
                        Debug.print("Inclusive Days : " + inclusiveDays);
                        Debug.print("Base interest : " + baseInterest);
                        Debug.print("Bonus interest : " + bonusInterest);
                        Debug.print("Total Interest Income : " + interestIncome);

                        olderDateBeforeVouDate = latestDateBeforeVouDate;
                    }

                    Debug.print("****************************************");
                    Debug.print("Ending Balance : " + balance);
                    Debug.print("Total Inclusive Days : " + totalInclusiveDays);
                    Debug.print("Total Base interest : " + totalBaseInterest);
                    Debug.print("Total Bonus interest : " + totalBonusInterest);
                    Debug.print("Total Interest Income : " + totalInterestIncome);
                    Debug.print("****************************************");
                }

            } else {

                double balance = 0;

                long inclusiveDays = firstDateGc.getActualMaximum(Calendar.DATE);

                Collection apSupplierPriorBalances = apSupplierBalanceHome.findByBeforeSbDateAndSplCode(firstDateGc.getTime(), apSupplier.getSplCode(), AD_CMPNY);

                if (!apSupplierPriorBalances.isEmpty()) {

                    ArrayList apSupplierPriorBalanceList = new ArrayList(apSupplierPriorBalances);

                    LocalApSupplierBalance apSupplierPriorBalance = (LocalApSupplierBalance) apSupplierPriorBalanceList.get(apSupplierPriorBalanceList.size() - 1);

                    balance = apSupplierPriorBalance.getSbBalance();
                }

                interestIncome = balance * ((RV_INTRST_RT / 100) / 365) * inclusiveDays;

                // D.) compute interest for inexcess of 500k
                if (balance > 500000) {

                    interest = (balance - 500000) * (((0.5) / 100) / 365) * inclusiveDays;
                    interestIncome += interest;
                    totalInterestIncome += interestIncome;
                }
            }

            if (interestIncome == 0) throw new GlobalAmountInvalidException();

            return totalInterestIncome;

        } catch (GlobalAmountInvalidException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlJournalBatchControllerBean ejbCreate");
    }

    // private methods

}