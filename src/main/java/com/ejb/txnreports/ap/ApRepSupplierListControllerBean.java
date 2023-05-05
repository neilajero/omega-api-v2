package com.ejb.txnreports.ap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;


import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.dao.ap.ILocalApSupplierHome;
import com.ejb.entities.ap.LocalApDistributionRecord;
import com.ejb.dao.ap.LocalApDistributionRecordHome;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.entities.ap.LocalApSupplierBalance;
import com.ejb.dao.ap.LocalApSupplierBalanceHome;
import com.ejb.entities.ap.LocalApSupplierClass;
import com.ejb.dao.ap.LocalApSupplierClassHome;
import com.ejb.entities.ap.LocalApSupplierType;
import com.ejb.dao.ap.LocalApSupplierTypeHome;
import com.ejb.entities.ap.LocalApVoucher;
import com.ejb.dao.ap.LocalApVoucherHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ap.ApRepSupplierListDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ApRepSupplierListControllerEJB")
public class ApRepSupplierListControllerBean extends EJBContextClass implements ApRepSupplierListController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalApSupplierHome apSupplierHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    private LocalApSupplierBalanceHome apSupplierBalanceHome;
    @EJB
    private LocalApSupplierClassHome apSupplierClassHome;
    @EJB
    private LocalApSupplierTypeHome apSupplierTypeHome;
    @EJB
    private LocalApVoucherHome apVoucherHome;


    public ArrayList getApScAll(Integer AD_CMPNY) {

        Debug.print("ApRepSupplierListControllerBean getApScAll");

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

    public ArrayList getApStAll(Integer AD_CMPNY) {

        Debug.print("ApRepSupplierListControllerBean getApStAll");

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

    public ArrayList executeApRepSupplierList(HashMap criteria, String ORDER_BY, boolean includeDC, ArrayList adBrnchList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepSupplierListControllerBean executeApRepSupplierList");

        ArrayList list = new ArrayList();

        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT DISTINCT OBJECT(spl) FROM ApSupplier spl ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("supplierCode")) {

                criteriaSize--;
            }

            if (criteria.containsKey("date")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (adBrnchList.isEmpty()) {

                throw new GlobalNoRecordFoundException();

            } else {

                jbossQl.append(", in (spl.adBranchSuppliers) bspl WHERE bspl.adBranch.brCode in (");

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

            if (criteria.containsKey("supplierCode")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("spl.splSupplierCode LIKE '%").append(criteria.get("supplierCode")).append("%' ");
            }

            if (criteria.containsKey("supplierType")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("spl.apSupplierType.stName=?").append(ctr + 1).append(" ");
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

                jbossQl.append("spl.apSupplierClass.scName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("supplierClass");
                ctr++;
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("spl.splAdCompany=").append(AD_CMPNY).append(" ");

            String orderBy = null;

            switch (ORDER_BY) {
                case "SUPPLIER CODE":

                    orderBy = "spl.splSupplierCode";

                    break;
                case "SUPPLIER TYPE":

                    orderBy = "spl.apSupplierType.stName";

                    break;
                case "SUPPLIER CLASS":

                    orderBy = "spl.apSupplierClass.scName";
                    break;
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy);
            }

            Collection apSuppliers = apSupplierHome.getSplByCriteria(jbossQl.toString(), obj, 0, 0);

            if (apSuppliers.size() == 0) throw new GlobalNoRecordFoundException();

            GregorianCalendar lastDateGc = new GregorianCalendar();
            lastDateGc.setTime((Date) criteria.get("date"));

            GregorianCalendar firstDateGc = new GregorianCalendar(lastDateGc.get(Calendar.YEAR), lastDateGc.get(Calendar.MONTH), lastDateGc.getActualMinimum(Calendar.DATE), 0, 0, 0);

            GregorianCalendar lastMonthDateGc2 = null;

            Iterator i = apSuppliers.iterator();
            double drAmnt = 0d;
            double SplBalance = 0d;
            while (i.hasNext()) {

                boolean issetBegBal = true;

                LocalApSupplier apSupplier = (LocalApSupplier) i.next();

                ApRepSupplierListDetails details = new ApRepSupplierListDetails();
                details.setSlSplSupplierCode(apSupplier.getSplSupplierCode());
                details.setSlSplName(apSupplier.getSplName());
                details.setSlSplContact(apSupplier.getSplContact());
                details.setSlSplPhone(apSupplier.getSplPhone());
                details.setSlSplTin(apSupplier.getSplTin());
                details.setSlSplAddress(apSupplier.getSplAddress());

                if (includeDC) {

                    for (Object o : adBrnchList) {
                        AdBranchDetails mdetails = (AdBranchDetails) o;
                        Debug.print("mdetails.getBrBranchCode(): " + mdetails.getBrCode());
                        Collection apDistributionRecords = apDistributionRecordHome.findChkByDateAndCoaAccountDescriptionAndSupplier((Date) criteria.get("date"), apSupplier.getSplCode(), mdetails.getBrCode(), AD_CMPNY);

                        if (apDistributionRecords.isEmpty()) {
                            Debug.print("WALA!");
                        } else {

                            for (Object distributionRecord : apDistributionRecords) {

                                // && apDistributionRecord.getApCheck().getChkAdBranch()=
                                LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) distributionRecord;
                                if (apDistributionRecord.getDrDebit() != 0) {
                                    Debug.print("Supplier Name!: " + apDistributionRecord.getApCheck().getApSupplier().getSplName());
                                    Debug.print("Account!: " + apDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());
                                    Debug.print("Check!: " + apDistributionRecord.getApCheck().getChkDocumentNumber());
                                    System.out.println("Check Date!: " + apDistributionRecord.getApCheck().getChkDate());
                                    Debug.print("Amount!: " + apDistributionRecord.getDrAmount());
                                    drAmnt = drAmnt + apDistributionRecord.getDrAmount();
                                } else {
                                    drAmnt = drAmnt - apDistributionRecord.getDrAmount();
                                }
                            }
                        }
                    }
                }

                // get balance OK

                Collection apSupplierBalances = apSupplierBalanceHome.findByBeforeOrEqualSbDateAndSplCode((Date) criteria.get("date"), apSupplier.getSplCode(), AD_CMPNY);

                if (apSupplierBalances.isEmpty()) {

                    details.setSlSplBalance(0d);
                    details.setSlSplPtdBalance(0d);
                    details.setSlSplBegBalance(0d);

                } else {

                    ArrayList apSupplierBalanceList = new ArrayList(apSupplierBalances);

                    LocalApSupplierBalance apSupplierBalance = (LocalApSupplierBalance) apSupplierBalanceList.get(apSupplierBalanceList.size() - 1);

                    if (drAmnt < 0) {
                        details.setSlSplBalance(apSupplierBalance.getSbBalance() - (drAmnt * -1));
                    } else {
                        details.setSlSplBalance(apSupplierBalance.getSbBalance() - (drAmnt));
                    }

                    // get beginning balance OK
                    if (issetBegBal) {
                        GregorianCalendar lastMonthDateGc = new GregorianCalendar();
                        lastMonthDateGc.setTime((Date) criteria.get("date"));
                        lastMonthDateGc.add(Calendar.MONTH, -1);

                        lastMonthDateGc2 = new GregorianCalendar(lastMonthDateGc.get(Calendar.YEAR), lastMonthDateGc.get(Calendar.MONTH), lastMonthDateGc.getActualMaximum(Calendar.DATE), 0, 0, 0);

                        Collection apBegBalances = apSupplierBalanceHome.findByBeforeOrEqualSbDateAndSplCode(lastMonthDateGc2.getTime(), apSupplier.getSplCode(), AD_CMPNY);

                        for (Object balance : apBegBalances) {
                            LocalApSupplierBalance apBegBalance = (LocalApSupplierBalance) balance;

                            double begBalance = apBegBalance.getSbBalance();

                            details.setSlSplBegBalance(begBalance);

                            Debug.print("Supplier: " + apSupplier.getSplName() + "\tDate" + lastMonthDateGc2.getTime() + "\tBeginning Balance: " + apBegBalance.getSbBalance());
                        }

                        issetBegBal = false;
                    }

                    // get ptd balance OK (same as Net Transactions)

                    double priorBalance = 0d;

                    Collection apSupplierPriorBalances = apSupplierBalanceHome.findByBeforeSbDateAndSplCode(firstDateGc.getTime(), apSupplier.getSplCode(), AD_CMPNY);

                    if (!apSupplierPriorBalances.isEmpty()) {

                        ArrayList apSupplierPriorBalanceList = new ArrayList(apSupplierPriorBalances);

                        LocalApSupplierBalance apSupplierPriorBalance = (LocalApSupplierBalance) apSupplierPriorBalanceList.get(apSupplierPriorBalanceList.size() - 1);

                        priorBalance = apSupplierPriorBalance.getSbBalance();
                    }

                    details.setSlSplPtdBalance((apSupplierBalance.getSbBalance() - priorBalance) - drAmnt);
                    Debug.print("details.setSlSplPtdBalance: " + (apSupplierBalance.getSbBalance() - priorBalance));
                }

                // get Interest Income

                double interestIncome = 0d;

                GregorianCalendar vouDateGc = new GregorianCalendar();
                vouDateGc.setTime((Date) criteria.get("date"));

                GregorianCalendar firstDayOfMonth = new GregorianCalendar(vouDateGc.get(Calendar.YEAR), vouDateGc.get(Calendar.MONTH), vouDateGc.getActualMinimum(Calendar.DATE), 0, 0, 0);

                String referenceNumber = "INT-" + EJBCommon.convertSQLDateToString(vouDateGc.getTime());

                Collection apInterestIncomes = apVoucherHome.findByVouReferenceNumberAndSplName(referenceNumber, apSupplier.getSplName(), AD_CMPNY);

                for (Object income : apInterestIncomes) {
                    LocalApVoucher apInterestIncome = (LocalApVoucher) income;
                    interestIncome = apInterestIncome.getVouAmountDue();
                }
                details.setSlSplAmntDC(drAmnt);
                details.setSlSplInterestIncome(interestIncome);
                SplBalance = (details.getSlSplBalance() + interestIncome);
                details.setSlSplBalance(SplBalance);
                details.setSlSplPaymentTerm(apSupplier.getAdPaymentTerm().getPytName());
                list.add(details);
                drAmnt = 0;
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("ApRepSupplierListControllerBean getAdCompany");

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

    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepSupplierListControllerBean getAdBrResAll");

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

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApRepSupplierListControllerBean ejbCreate");
    }
}