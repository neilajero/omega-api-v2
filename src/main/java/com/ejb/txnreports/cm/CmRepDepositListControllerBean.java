/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class CmRepDepositListControllerBean
 * @created Decmeber 06 , 01:20 PM
 * @author Farrah S. Garing
 */
package com.ejb.txnreports.cm;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdBankAccountHome;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.dao.ar.LocalArCustomerClassHome;
import com.ejb.dao.ar.LocalArCustomerTypeHome;
import com.ejb.dao.ar.LocalArReceiptHome;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.entities.ad.LocalAdBankAccount;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ar.LocalArCustomerClass;
import com.ejb.entities.ar.LocalArCustomerType;
import com.ejb.entities.ar.LocalArReceipt;
import com.ejb.entities.cm.LocalCmFundTransferReceipt;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.reports.cm.CmRepDepositListDetails;
import jakarta.ejb.*;

import java.util.*;

;

@Stateless(name = "CmRepDepositListControllerEJB")
public class CmRepDepositListControllerBean extends EJBContextClass implements CmRepDepositListController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalArCustomerClassHome arCustomerClassHome;
    @EJB
    private LocalArCustomerTypeHome arCustomerTypeHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;


    public ArrayList getArCcAll(Integer AD_CMPNY) {

        Debug.print("CmRepDepositListControllerBean getArCcAll");

        LocalArCustomerClass arCustomerClass = null;

        ArrayList list = new ArrayList();

        try {

            Collection arCustomerClasses = arCustomerClassHome.findEnabledCcAll(AD_CMPNY);

            for (Object customerClass : arCustomerClasses) {

                arCustomerClass = (LocalArCustomerClass) customerClass;

                list.add(arCustomerClass.getCcName());
            }

            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArCtAll(Integer AD_CMPNY) {

        Debug.print("CmRepDepositListControllerBean getArCtAll");

        LocalArCustomerType arCustomerType = null;

        ArrayList list = new ArrayList();

        try {

            Collection arCustomerTypes = arCustomerTypeHome.findEnabledCtAll(AD_CMPNY);

            for (Object customerType : arCustomerTypes) {

                arCustomerType = (LocalArCustomerType) customerType;

                list.add(arCustomerType.getCtName());
            }

            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdBaAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("CmRepDepositListControllerBean getAdBaAll");

        LocalAdBankAccount adBankAccount = null;

        ArrayList list = new ArrayList();

        try {

            Collection adBankAccounts = adBankAccountHome.findEnabledBaAll(AD_BRNCH, AD_CMPNY);

            for (Object bankAccount : adBankAccounts) {

                adBankAccount = (LocalAdBankAccount) bankAccount;

                if (adBankAccount.getBaIsCashAccount() == EJBCommon.TRUE) {
                    list.add(adBankAccount.getBaName());
                }
            }

            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("CmRepDepositListControllerBean getAdBrResAll");

        LocalAdBranchResponsibility adBranchResponsibility = null;
        LocalAdBranch adBranch = null;

        Collection adBranchResponsibilities = null;

        ArrayList list = new ArrayList();

        try {

            adBranchResponsibilities = adBranchResponsibilityHome.findByAdResponsibility(RS_CODE, AD_CMPNY);

        }
        catch (FinderException ex) {

        }
        catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (adBranchResponsibilities.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }

        try {

            for (Object branchResponsibility : adBranchResponsibilities) {

                adBranchResponsibility = (LocalAdBranchResponsibility) branchResponsibility;

                adBranch = adBranchHome.findByPrimaryKey(adBranchResponsibility.getAdBranch().getBrCode());

                AdBranchDetails details = new AdBranchDetails();
                details.setBrCode(adBranch.getBrCode());
                details.setBrBranchCode(adBranch.getBrBranchCode());
                details.setBrName(adBranch.getBrName());
                details.setBrHeadQuarter(adBranch.getBrHeadQuarter());

                list.add(details);
            }

        }
        catch (FinderException ex) {

        }
        catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        return list;
    }

    public ArrayList executeCmRepDepositList(HashMap criteria, ArrayList branchList, String ORDER_BY, String GROUP_BY, String status, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("CmRepDepositListControllerBean executeCmRepDepositList");

        ArrayList list = new ArrayList();

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(rct) FROM ArReceipt rct WHERE (");

            if (branchList.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }

            Iterator brIter = branchList.iterator();

            AdBranchDetails details = (AdBranchDetails) brIter.next();
            jbossQl.append("rct.rctAdBranch=").append(details.getBrCode());

            while (brIter.hasNext()) {

                details = (AdBranchDetails) brIter.next();

                jbossQl.append(" OR rct.rctAdBranch=").append(details.getBrCode());
            }

            jbossQl.append(") ");
            firstArgument = false;

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("customerCode")) {

                criteriaSize--;
            }

            if (criteria.containsKey("includedUnposted")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("customerCode")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.arCustomer.cstCustomerCode LIKE '%").append(criteria.get("customerCode")).append("%' ");
            }

            if (criteria.containsKey("bankAccount")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.adBankAccount.baName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("bankAccount");
                ctr++;
            }

            if (criteria.containsKey("customerType")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.arCustomer.arCustomerType.ctName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerType");
                ctr++;
            }

            if (criteria.containsKey("customerClass")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.arCustomer.arCustomerClass.ccName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerClass");
                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("rct.rctDate>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateFrom");
                ctr++;
            }

            if (criteria.containsKey("dateTo")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("rct.rctDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (criteria.containsKey("receiptType")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("rct.rctType>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("receiptType");
                ctr++;
            }

            if (criteria.containsKey("receiptNumberFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("rct.rctNumber>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("receiptNumberFrom");
                ctr++;
            }

            if (criteria.containsKey("receiptNumberTo")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("rct.rctNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("receiptNumberTo");
                ctr++;
            }

            if (criteria.containsKey("includedUnposted")) {

                String unposted = (String) criteria.get("includedUnposted");

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                if (unposted.equals("NO")) {

                    jbossQl.append("((rct.rctPosted = 1 AND rct.rctVoid = 0) OR (rct.rctPosted = 1 AND rct.rctVoid = 1 AND rct.rctVoidPosted = 0)) ");

                } else {

                    jbossQl.append("rct.rctVoid = 0 ");
                }
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append(" rct.adBankAccount.baIsCashAccount = 1 AND rct.rctAdCompany = ").append(AD_CMPNY).append(" ");

            Collection arReceipts = arReceiptHome.getRctByCriteria(jbossQl.toString(), obj, 0, 0);

            if (arReceipts.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            for (Object receipt : arReceipts) {

                LocalArReceipt arReceipt = (LocalArReceipt) receipt;

                CmRepDepositListDetails mdetails = new CmRepDepositListDetails();
                mdetails.setDlDate(arReceipt.getRctDate());
                mdetails.setDlReceiptNumber(arReceipt.getRctNumber());
                mdetails.setDlReferenceNumber(arReceipt.getRctReferenceNumber());
                mdetails.setDlDescription(arReceipt.getRctDescription());
                mdetails.setDlCstCustomerCode(arReceipt.getArCustomer().getCstCustomerCode() + "-" + arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName());
                mdetails.setDlCstCustomerClass(arReceipt.getArCustomer().getArCustomerClass().getCcName());

                // type
                if (arReceipt.getArCustomer().getArCustomerType() == null) {

                    mdetails.setDlCstCustomerType("UNDEFINE");

                } else {

                    mdetails.setDlCstCustomerType(arReceipt.getArCustomer().getArCustomerType().getCtName());
                }

                mdetails.setDlAmount(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                mdetails.setDlBankAccount(arReceipt.getAdBankAccount().getBaName());
                mdetails.setOrderBy(ORDER_BY);

                Collection cmFundTransferReceipt = arReceipt.getCmFundTransferReceipts();

                Iterator iFTR = cmFundTransferReceipt.iterator();

                double TTL_DPSTD_AMNT = 0;

                while (iFTR.hasNext()) {

                    LocalCmFundTransferReceipt cmFundTransferReciept = (LocalCmFundTransferReceipt) iFTR.next();

                    if (criteria.get("includedUnposted").equals("YES") || (criteria.get("includedUnposted").equals("NO") && (cmFundTransferReciept.getCmFundTransfer().getFtPosted() == 1))) {
                        TTL_DPSTD_AMNT = TTL_DPSTD_AMNT + cmFundTransferReciept.getFtrAmountDeposited();
                    }
                }

                mdetails.setDlAmountDeposited(TTL_DPSTD_AMNT);

                if ((status.equals("")) || (status.equals("UNDEPOSITED") && TTL_DPSTD_AMNT <= 0) || (status.equals("DEPOSITED") && TTL_DPSTD_AMNT > 0)) {

                    list.add(mdetails);
                }
            }

            // sort

            switch (GROUP_BY) {
                case "CUSTOMER CODE":

                    list.sort(CmRepDepositListDetails.CustomerCodeComparator);

                    break;
                case "CUSTOMER TYPE":

                    list.sort(CmRepDepositListDetails.CustomerTypeComparator);

                    break;
                case "CUSTOMER CLASS":

                    list.sort(CmRepDepositListDetails.CustomerClassComparator);

                    break;
                default:

                    list.sort(CmRepDepositListDetails.NoGroupComparator);
                    break;
            }

            if (list.size() > 0) {

                return list;

            } else {

                throw new GlobalNoRecordFoundException();
            }

        }
        catch (GlobalNoRecordFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("CmRepDepositListControllerBean getAdCompany");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());

            return details;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods
    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("CmRepDepositListControllerBean convertForeignToFunctionalCurrency");

        LocalAdCompany adCompany = null;

        // get company and extended precision

        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

        }
        catch (Exception ex) {

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

        Debug.print("CmRepDepositListControllerBean ejbCreate");
    }

}