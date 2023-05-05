/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class CmFindAdjustmentControllerBean
 * @created
 * @author
 */
package com.ejb.txn.cm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdBankAccount;
import com.ejb.dao.ad.LocalAdBankAccountHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdLookUpValue;
import com.ejb.dao.ad.LocalAdLookUpValueHome;
import com.ejb.entities.ar.LocalArAppliedCredit;
import com.ejb.entities.ar.LocalArCustomer;
import com.ejb.dao.ar.LocalArCustomerHome;
import com.ejb.entities.cm.LocalCmAdjustment;
import com.ejb.dao.cm.LocalCmAdjustmentHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.mod.cm.CmModAdjustmentDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;

@Stateless(name = "CmFindAdjustmentControllerEJB")
public class CmFindAdjustmentControllerBean extends EJBContextClass implements CmFindAdjustmentController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    LocalCmAdjustmentHome cmAdjustmentHome;
    @EJB
    LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    LocalArCustomerHome arCustomerHome;
    @EJB
    LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;

    public ArrayList getAdLvCustomerBatchAll(Integer AD_CMPNY) {

        Debug.print("CmFindAdjustmentControllerBean getAdLvCustomerBatchAll");


        ArrayList list = new ArrayList();


        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AR CUSTOMER BATCH - SOA", AD_CMPNY);

            for (Object lookUpValue : adLookUpValues) {

                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;

                list.add(adLookUpValue.getLvName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArCstAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("CmFindAdjustmentControllerBean getArCstAll");


        ArrayList list = new ArrayList();


        try {

            Collection arCustomers = arCustomerHome.findEnabledCstAll(AD_BRNCH, AD_CMPNY);

            for (Object customer : arCustomers) {

                LocalArCustomer arCustomer = (LocalArCustomer) customer;

                list.add(arCustomer.getCstCustomerCode());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdBaAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("CmFindAdjustmentControllerBean getAdBaAll");


        Collection adBankAccounts = null;

        LocalAdBankAccount adBankAccount = null;

        ArrayList list = new ArrayList();


        try {

            adBankAccounts = adBankAccountHome.findEnabledBaAll(AD_BRNCH, AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (adBankAccounts.isEmpty()) {

            return null;
        }

        for (Object bankAccount : adBankAccounts) {

            adBankAccount = (LocalAdBankAccount) bankAccount;

            list.add(adBankAccount.getBaName());
        }

        return list;
    }

    public ArrayList getCmAdjByCriteria(HashMap criteria, String ORDER_BY, Integer OFFSET, Integer LIMIT, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("CmFindAdjustmentControllerBean getCmAdjByCriteria");


        ArrayList list = new ArrayList();

        try {

            StringBuffer jbossQl = new StringBuffer();
            Collection cmAdjustments = executeCriteriaQuery(criteria, ORDER_BY, OFFSET, LIMIT, AD_BRNCH, AD_CMPNY, jbossQl);

            if (cmAdjustments.size() == 0) throw new GlobalNoRecordFoundException();

            for (Object adjustment : cmAdjustments) {

                LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) adjustment;

                CmModAdjustmentDetails mdetails = new CmModAdjustmentDetails();
                mdetails.setAdjCode(cmAdjustment.getAdjCode());
                mdetails.setAdjType(cmAdjustment.getAdjType());
                mdetails.setAdjDate(cmAdjustment.getAdjDate());
                mdetails.setAdjDocumentNumber(cmAdjustment.getAdjDocumentNumber());
                mdetails.setAdjReferenceNumber(cmAdjustment.getAdjReferenceNumber());
                String customerCode = "";
                String customerName = "";
                try {
                    customerCode = cmAdjustment.getArCustomer().getCstCustomerCode();
                    customerName = cmAdjustment.getArCustomer().getCstName();
                } catch (Exception ex) {

                }
                mdetails.setAdjCustomerCode(customerCode);
                mdetails.setAdjCustomerName(customerName);
                mdetails.setAdjAmount(cmAdjustment.getAdjAmount());

                // get applied credit
                double totalAppliedCredit = 0d;
                try {

                    Collection arAppliedCredits = cmAdjustment.getArAppliedCredits();
                    Debug.print("arAppliedCredits=" + arAppliedCredits.size());

                    for (Object appliedCredit : arAppliedCredits) {

                        LocalArAppliedCredit arAppliedCredit = (LocalArAppliedCredit) appliedCredit;

                        totalAppliedCredit += arAppliedCredit.getAcApplyCredit();
                    }
                } catch (Exception ex) {

                }

                Debug.print("CM_CODE=" + cmAdjustment.getAdjCode());
                Debug.print("cmAdjustment.getAdjDocumentNumber()=" + cmAdjustment.getAdjDocumentNumber());
                Debug.print("cmAdjustment.getAdjRefundAmount()=" + cmAdjustment.getAdjRefundAmount());
                Debug.print("totalAppliedCredit=" + totalAppliedCredit);
                mdetails.setAdjAmountApplied(totalAppliedCredit);
                mdetails.setAdjRefundAmount(cmAdjustment.getAdjRefundAmount());
                mdetails.setAdjVoid(cmAdjustment.getAdjVoid());
                mdetails.setAdjBaName(cmAdjustment.getAdBankAccount().getBaName());
                mdetails.setAdjPosted(cmAdjustment.getAdjPosted());

                list.add(mdetails);
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    private Collection executeCriteriaQuery(HashMap criteria, String ORDER_BY, Integer OFFSET, Integer LIMIT, Integer AD_BRNCH, Integer AD_CMPNY, StringBuffer jbossQl) throws FinderException {
        Object[] obj;
        jbossQl.append("SELECT OBJECT(adj) FROM CmAdjustment adj ");

        boolean firstArgument = true;
        short ctr = 0;
        int criteriaSize = criteria.size();


        if (criteria.containsKey("approvalStatus")) {

            String approvalStatus = (String) criteria.get("approvalStatus");

            if (approvalStatus.equals("DRAFT") || approvalStatus.equals("REJECTED")) {

                criteriaSize--;
            }
        }

        obj = new Object[criteriaSize];

        if (criteria.containsKey("customerBatch")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("adj.arCustomer.cstCustomerBatch=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("customerBatch");
            ctr++;
        }

        if (criteria.containsKey("customerCode")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("adj.arCustomer.cstCustomerCode=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("customerCode");
            ctr++;
        }

        if (criteria.containsKey("bankAccount")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append(" adj.adBankAccount.baName=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("bankAccount");
            ctr++;
        }

        if (criteria.containsKey("type")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("adj.adjType=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("type");
            ctr++;
        }

        if (criteria.containsKey("documentNumberFrom")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("adj.adjDocumentNumber>=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("documentNumberFrom");
            ctr++;
        }

        if (criteria.containsKey("documentNumberTo")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("adj.adjDocumentNumber<=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("documentNumberTo");
            ctr++;
        }

        if (criteria.containsKey("referenceNumber")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("adj.adjReferenceNumber=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("referenceNumber");
            ctr++;
        }

        if (criteria.containsKey("dateFrom")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("adj.adjDate>=?").append(ctr + 1).append(" ");
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
            jbossQl.append("adj.adjDate<=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("dateTo");
            ctr++;
        }

        if (criteria.containsKey("approvalStatus")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            String approvalStatus = (String) criteria.get("approvalStatus");

            if (approvalStatus.equals("DRAFT")) {

                jbossQl.append("adj.adjApprovalStatus IS NULL ");

            } else if (approvalStatus.equals("REJECTED")) {

                jbossQl.append("adj.adjReasonForRejection IS NOT NULL ");

            } else {

                jbossQl.append("adj.adjApprovalStatus=?").append(ctr + 1).append(" ");
                obj[ctr] = approvalStatus;
                ctr++;
            }
        }

        if (!firstArgument) {
            jbossQl.append("AND ");
        } else {
            firstArgument = false;
            jbossQl.append("WHERE ");
        }

        jbossQl.append("adj.adjVoid=?").append(ctr + 1).append(" ");
        obj[ctr] = criteria.get("adjustmentVoid");
        ctr++;

        if (criteria.containsKey("posted")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("adj.adjPosted=?").append(ctr + 1).append(" ");

            String posted = (String) criteria.get("posted");

            if (posted.equals("YES")) {

                obj[ctr] = EJBCommon.TRUE;

            } else {

                obj[ctr] = EJBCommon.FALSE;
            }

            ctr++;
        }

        if (!firstArgument) {

            jbossQl.append("AND ");

        } else {

            firstArgument = false;
            jbossQl.append("WHERE ");
        }

        jbossQl.append("adj.adjAdBranch=").append(AD_BRNCH).append(" ");

        if (!firstArgument) {

            jbossQl.append("AND ");

        } else {

            firstArgument = false;
            jbossQl.append("WHERE ");
        }

        jbossQl.append("adj.adjAdCompany=").append(AD_CMPNY).append(" ");

        String orderBy = null;

        if (ORDER_BY != null && ORDER_BY.equals("REFERENCE NUMBER")) {

            orderBy = "adj.adjReferenceNumber, adj.adjDate";

        } else if (ORDER_BY != null && ORDER_BY.equals("DOCUMENT NUMBER")) {

            orderBy = "adj.adjDocumentNumber, adj.adjDate";

        } else {

            orderBy = "adj.adjDate";
        }

        jbossQl.append("ORDER BY ").append(orderBy);

        Collection cmAdjustments = null;
        System.out.print("sql is :" + jbossQl);
        cmAdjustments = cmAdjustmentHome.getAdjByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);
        return cmAdjustments;
    }

    public Integer getCmAdjSizeByCriteria(HashMap criteria, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("CmFindAdjustmentControllerBean getCmAdjSizeByCriteria");

        try {

            StringBuffer jbossQl = new StringBuffer();
            Collection cmAdjustments = executeCriteriaQuery(criteria, "REFERENCE NUMBER", 0, 0, AD_BRNCH, AD_CMPNY, jbossQl);

            if (cmAdjustments.size() == 0) throw new GlobalNoRecordFoundException();

            return cmAdjustments.size();

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("CmFindAdjustmentControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("CmFindAdjustmentControllerBean ejbCreate");
    }
}