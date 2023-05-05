/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArFindCustomerControllerBean
 * @created March 4, 2004, 1:04 PM
 * @author Neil Andrew M. Ajero
 **/

package com.ejb.txn.ar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.entities.ad.LocalAdLookUpValue;
import com.ejb.dao.ad.LocalAdLookUpValueHome;
import com.ejb.entities.ar.LocalArCustomer;
import com.ejb.entities.ar.LocalArCustomerClass;
import com.ejb.dao.ar.LocalArCustomerClassHome;
import com.ejb.dao.ar.LocalArCustomerHome;
import com.ejb.entities.ar.LocalArCustomerType;
import com.ejb.dao.ar.LocalArCustomerTypeHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdBranchDetails;
import com.util.mod.ad.AdModBranchCustomerDetails;
import com.util.mod.ar.ArModCustomerDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;

@Stateless(name = "ArFindCustomerControllerEJB")
public class ArFindCustomerControllerBean extends EJBContextClass implements ArFindCustomerController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    LocalArCustomerTypeHome arCustomerTypeHome;
    @EJB
    LocalArCustomerClassHome arCustomerClassHome;
    @EJB
    LocalArCustomerHome arCustomerHome;
    @EJB
    LocalAdBranchResponsibilityHome adBrResHome;

    public ArrayList getAdLvCustomerBatchAll(Integer AD_CMPNY) {

        Debug.print("ArFindCustomerControllerBean getAdLvCustomerBatchAll");

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

    public ArrayList getArCtAll(Integer AD_CMPNY) {

        Debug.print("ArFindCustomerControllerBean getArCtAll");

        LocalArCustomerType arCustomerType = null;

        ArrayList list = new ArrayList();

        try {

            Collection arCustomerTypes = arCustomerTypeHome.findEnabledCtAll(AD_CMPNY);

            for (Object customerType : arCustomerTypes) {

                arCustomerType = (LocalArCustomerType) customerType;

                list.add(arCustomerType.getCtName());

            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }

    public ArrayList getArCcAll(Integer AD_CMPNY) {

        Debug.print("ArFindCustomerControllerBean getArCcAll");

        LocalArCustomerClass arCustomerClass = null;

        ArrayList list = new ArrayList();

        try {

            Collection arCustomerClasses = arCustomerClassHome.findEnabledCcAll(AD_CMPNY);

            for (Object customerClass : arCustomerClasses) {

                arCustomerClass = (LocalArCustomerClass) customerClass;

                list.add(arCustomerClass.getCcName());

            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }

    public ArrayList getArCstByCriteria(HashMap criteria, ArrayList branchList, Integer OFFSET, Integer LIMIT, String ORDER_BY, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArFindCustomerControllerBean getArCstByCriteria");

        ArrayList list = new ArrayList();

        try {

            Collection arCustomers = this.getCriteriaQuery(criteria, branchList, OFFSET, LIMIT, ORDER_BY, AD_CMPNY);
            if (arCustomers.size() == 0) throw new GlobalNoRecordFoundException();

            for (Object customer : arCustomers) {

                LocalArCustomer arCustomer = (LocalArCustomer) customer;

                ArModCustomerDetails mdetails = new ArModCustomerDetails();
                mdetails.setCstCode(arCustomer.getCstCode());
                mdetails.setCstCustomerCode(arCustomer.getCstCustomerCode());
                mdetails.setCstName(arCustomer.getCstName());
                mdetails.setCstEmployeeID(arCustomer.getCstEmployeeID());
                mdetails.setCstAddress(arCustomer.getCstAddress());
                mdetails.setCstArea(arCustomer.getArCustomerClass().getCcName());
                mdetails.setCstSlpName(arCustomer.getArSalesperson() != null ? arCustomer.getArSalesperson().getSlpName() : null);
                mdetails.setCstTin(arCustomer.getCstTin());
                mdetails.setCstEmail(arCustomer.getCstEmail());
                mdetails.setCstCtName(arCustomer.getArCustomerType() != null ? arCustomer.getArCustomerType().getCtName() : null);
                mdetails.setCstCcName(arCustomer.getArCustomerClass().getCcName());
                mdetails.setCstPytName(arCustomer.getAdPaymentTerm().getPytName());
                mdetails.setCstEnable(arCustomer.getCstEnable());

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

    public Integer getArCstSizeByCriteria(HashMap criteria, ArrayList branchList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArFindCustomerControllerBean getArCstSizeByCriteria");

        try {

            Collection arCustomers = this.getCriteriaQuery(criteria, branchList, 0, 0, "", AD_CMPNY);
            if (arCustomers.size() == 0) throw new GlobalNoRecordFoundException();

            return arCustomers.size();

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());

        }

    }

    public ArrayList getAdBrResAll(int resCode, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArFindCustomerControllerBean getAdResAll");

        LocalAdBranchResponsibility adBrRes = null;

        Collection adBranches = null;
        ArrayList list = new ArrayList();

        try {

            adBranches = adBrResHome.findByAdResponsibility(resCode, AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (adBranches.isEmpty()) {
            throw new GlobalNoRecordFoundException();

        }

        for (Object adBranch : adBranches) {

            adBrRes = (LocalAdBranchResponsibility) adBranch;
            AdBranchDetails details = new AdBranchDetails();

            details.setBrBranchCode(adBrRes.getAdBranch().getBrBranchCode());
            details.setBrName(adBrRes.getAdBranch().getBrName());
            list.add(details);
        }

        return list;

    }

    private java.util.Collection getCriteriaQuery(HashMap criteria, ArrayList branchList, Integer OFFSET, Integer LIMIT, String ORDER_BY, Integer AD_CMPNY) throws Exception {

        try {

            StringBuilder jbossQl = new StringBuilder();

            if (branchList.size() > 0) {

                jbossQl.append("SELECT DISTINCT OBJECT(cst) FROM ArCustomer cst, IN(cst.adBranchCustomers)bcst ");

            } else {

                jbossQl.append("SELECT OBJECT(cst) FROM ArCustomer cst ");

            }

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size() + branchList.size();

            Object[] obj = null;

            // Allocate the size of the object parameter

            if (criteria.containsKey("customerCode")) {

                criteriaSize--;

            }

            if (criteria.containsKey("name")) {

                criteriaSize--;

            }

            if (criteria.containsKey("employeeId")) {

                criteriaSize--;

            }

            if (criteria.containsKey("email")) {

                criteriaSize--;

            }

            if (criteria.containsKey("approvalStatus")) {

                String approvalStatus = (String) criteria.get("approvalStatus");

                if (approvalStatus.equals("DRAFT") || approvalStatus.equals("REJECTED")) {

                    criteriaSize--;

                }

            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("customerCode")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("cst.cstCustomerCode LIKE '%").append(criteria.get("customerCode")).append("%' ");

            }

            if (branchList.size() > 0) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                AdModBranchCustomerDetails details = null;

                Iterator i = branchList.iterator();

                details = (AdModBranchCustomerDetails) i.next();

                jbossQl.append(" ( bcst.adBranch.brBranchCode=?").append(ctr + 1).append(" ");
                obj[ctr] = details.getBcstBranchCode();

                ctr++;

                while (i.hasNext()) {

                    details = (AdModBranchCustomerDetails) i.next();

                    jbossQl.append("OR bcst.adBranch.brBranchCode=?").append(ctr + 1).append(" ");
                    obj[ctr] = details.getBcstBranchCode();

                    ctr++;

                }

                jbossQl.append(" ) ");

            }

            if (criteria.containsKey("name")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.cstName LIKE '%").append(criteria.get("name")).append("%' ");

            }

            if (criteria.containsKey("employeeId")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.cstEmployeeID = '").append(criteria.get("employeeId")).append("' ");

            }

            if (criteria.containsKey("email")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.cstEmail LIKE '%").append(criteria.get("email")).append("%' ");

            }

            if (criteria.containsKey("customerType")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.arCustomerType.ctName=?").append(ctr + 1).append(" ");
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

                jbossQl.append("cst.arCustomerClass.ccName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerClass");
                ctr++;

            }

            if (criteria.containsKey("customerBatch")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.cstCustomerBatch=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerBatch");
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

                    jbossQl.append("cst.cstApprovalStatus IS NULL ");

                } else if (approvalStatus.equals("REJECTED")) {

                    jbossQl.append("cst.cstReasonForRejection IS NOT NULL ");

                } else {

                    jbossQl.append("cst.cstApprovalStatus=?").append(ctr + 1).append(" ");
                    obj[ctr] = approvalStatus;
                    ctr++;

                }

            }

            if (criteria.containsKey("enable") && criteria.containsKey("disable")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("(cst.cstEnable=?").append(ctr + 1).append(" OR ");
                obj[ctr] = EJBCommon.TRUE;
                ctr++;

                jbossQl.append("cst.cstEnable=?").append(ctr + 1).append(") ");
                obj[ctr] = EJBCommon.FALSE;
                ctr++;

            } else {

                if (criteria.containsKey("enable")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("cst.cstEnable=?").append(ctr + 1).append(" ");
                    obj[ctr] = EJBCommon.TRUE;
                    ctr++;

                }

                if (criteria.containsKey("disable")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("cst.cstEnable=?").append(ctr + 1).append(" ");
                    obj[ctr] = EJBCommon.FALSE;
                    ctr++;

                }

            }
            if (criteria.containsKey("enablePayroll")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.cstEnablePayroll=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("enablePayroll");
                ctr++;

            }
            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");

            }

            jbossQl.append("cst.cstAdCompany=").append(AD_CMPNY).append(" ");

            String orderBy = null;

            if (ORDER_BY.equals("CUSTOMER CODE")) {

                orderBy = "cst.cstCustomerCode";

            } else {

                orderBy = "cst.cstName";

            }

            jbossQl.append("ORDER BY ").append(orderBy);

            return arCustomerHome.getCstByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);
        } catch (Exception ex) {
            throw ex;
        }

    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ArFindCustomerControllerBean ejbCreate");

    }
}