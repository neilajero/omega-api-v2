package com.ejb.txn.ad;

import java.util.*;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.ad.*;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.*;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.ad.AdRepBankAccountListDetails;

@Stateless(name = "AdRepBankAccountListControllerEJB")
public class AdRepBankAccountListControllerBean extends EJBContextClass implements AdRepBankAccountListController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalAdBankAccountBalanceHome adBankAccountBalanceHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;

    public ArrayList executeAdRepBankAccountList(HashMap criteria, ArrayList branchList, String orderBy, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdRepBankAccountListControllerBean executeAdRepBankAccountList");

        LocalGlChartOfAccount glChartOfAccount;
        ArrayList list = new ArrayList();
        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT DISTINCT OBJECT(ba) FROM AdBankAccount ba ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            if (branchList.isEmpty()) {

                throw new GlobalNoRecordFoundException();

            } else {

                jbossQl.append(", in (ba.adBranchBankAccounts) bba WHERE bba.adBranch.brCode in (");

                boolean firstLoop = true;

                for (Object o : branchList) {

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

            Object[] obj;

            // Allocate the size of the object parameter
            if (criteria.containsKey("bankName")) {

                criteriaSize--;
            }
            if (criteria.containsKey("accountName")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("bankName")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("ba.adBank.bnkName LIKE '%").append(criteria.get("bankName")).append("%' ");
            }
            if (criteria.containsKey("accountName")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("ba.baName LIKE '%").append(criteria.get("accountName")).append("%' ");
            }
            if (criteria.containsKey("accountUse")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("ba.baAccountUse=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("accountUse");
                ctr++;
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("ba.baAdCompany=").append(companyCode).append(" ");

            switch (orderBy) {
                case "BANK NAME":

                    orderBy = "ba.adBank.bnkName";

                    break;
                case "ACCOUNT NAME":

                    orderBy = "ba.baName";

                    break;
                case "ACCOUNT USE":

                    orderBy = "ba.baAccountUse";
                    break;
            }
            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy);
            }
            Collection adBankAccounts = adBankAccountHome.getBaByCriteria(jbossQl.toString(), obj);

            if (adBankAccounts.size() == 0) throw new GlobalNoRecordFoundException();

            for (Object bankAccount : adBankAccounts) {

                LocalAdBankAccount adBankAccount = (LocalAdBankAccount) bankAccount;

                AdRepBankAccountListDetails details = new AdRepBankAccountListDetails();
                details.setBalBankName(adBankAccount.getAdBank().getBnkName());
                details.setBalBaName(adBankAccount.getBaName());
                details.setBalBaDescription(adBankAccount.getBaDescription());
                details.setBalBaAccountNumber(adBankAccount.getBaAccountNumber());
                details.setBalBaAccountType(adBankAccount.getBaAccountType());
                details.setBalBaAccountUse(adBankAccount.getBaAccountUse());

                // get latest bank account balance for current bank account

                double bankAccountBalance = 0;

                Collection adBankAccountBalances = adBankAccountBalanceHome.findByBaCodeAndType(adBankAccount.getBaCode(), "BOOK", companyCode);

                if (!adBankAccountBalances.isEmpty()) {

                    // get last check

                    ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                    LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                    bankAccountBalance = adBankAccountBalance.getBabBalance();
                }

                details.setBalBaAvailableBalance(bankAccountBalance);
                details.setBalBaEnable(adBankAccount.getBaEnable());

                // account numbers and description
                if (adBankAccount.getBaCoaGlCashAccount() != null) {

                    glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBankAccount.getBaCoaGlCashAccount());

                    details.setBalBaCashAccount(glChartOfAccount.getCoaAccountNumber());
                    details.setBalBaCashAccountDescription(glChartOfAccount.getCoaAccountDescription());
                }
                if (adBankAccount.getBaCoaGlAdjustmentAccount() != null) {

                    glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBankAccount.getBaCoaGlAdjustmentAccount());

                    details.setBalBaAdjustmentAccount(glChartOfAccount.getCoaAccountNumber());
                    details.setBalBaAdjustmentAccountDescription(glChartOfAccount.getCoaAccountDescription());
                }
                if (adBankAccount.getBaCoaGlInterestAccount() != null) {

                    glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBankAccount.getBaCoaGlInterestAccount());

                    details.setBalBaInterestAccount(glChartOfAccount.getCoaAccountNumber());
                    details.setBalBaInterestAccountDescription(glChartOfAccount.getCoaAccountDescription());
                }
                if (adBankAccount.getBaCoaGlBankChargeAccount() != null) {

                    glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBankAccount.getBaCoaGlBankChargeAccount());

                    details.setBalBaBankChargeAccount(glChartOfAccount.getCoaAccountNumber());
                    details.setBalBaBankChargeAccountDescription(glChartOfAccount.getCoaAccountDescription());
                }
                if (adBankAccount.getBaCoaGlSalesDiscount() != null) {

                    glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBankAccount.getBaCoaGlSalesDiscount());

                    details.setBalBaSalesDiscount(glChartOfAccount.getCoaAccountNumber());
                    details.setBalBaSalesDiscountDescription(glChartOfAccount.getCoaAccountDescription());
                }
                list.add(details);
            }
            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer companyCode) {
        Debug.print("AdRepBankAccountListControllerBean getAdCompany");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

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

    public ArrayList getAdBrResAll(Integer responsibilityCode, Integer companyCode) throws GlobalNoRecordFoundException {
        Debug.print("AdRepBankAccountListControllerBean getAdBrResAll");
        LocalAdBranchResponsibility adBranchResponsibility;
        LocalAdBranch adBranch;
        Collection adBranchResponsibilities = null;
        ArrayList list = new ArrayList();
        try {

            adBranchResponsibilities = adBranchResponsibilityHome.findByAdResponsibility(responsibilityCode, companyCode);

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

        Debug.print("AdRepBankAccountListControllerBean ejbCreate");
    }
}