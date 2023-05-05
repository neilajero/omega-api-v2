/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArFindJobOrderControllerBean
 * @created
 * @author
 */
package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.ar.LocalArJobOrderHome;
import com.ejb.dao.ar.LocalArJobOrderTypeHome;
import com.ejb.entities.ar.LocalArJobOrder;
import com.ejb.entities.ar.LocalArJobOrderType;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.*;
import com.util.mod.ar.ArModJobOrderDetails;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

@Stateless(name = "ArFindJobOrderControllerEJB")
public class ArFindJobOrderControllerBean extends EJBContextClass implements ArFindJobOrderController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalArJobOrderHome arJobOrderHome;
    @EJB
    private LocalArJobOrderTypeHome arJobOrderTypeHome;

    public ArrayList getArJoForProcessingByBrCode(Integer AD_BRNCH, Integer AD_CMPNY) {
        Debug.print("ArFindJobOrderControllerBean getArJoForProcessingByBrCode");
        ArrayList list = new ArrayList();
        try {

            Collection arJobOrderColl = arJobOrderHome.findPostedJoByBrCode(AD_BRNCH, AD_CMPNY);

            for (Object o : arJobOrderColl) {

                LocalArJobOrder arJobOrder = (LocalArJobOrder) o;

                if (!((arJobOrder.getJoApprovalStatus().equals("APPROVED") || arJobOrder.getJoApprovalStatus().equals("N/A")) && arJobOrder.getJoLock() == (byte) (0))) {
                    continue;
                }

                ArModJobOrderDetails mdetails = new ArModJobOrderDetails();
                mdetails.setJoCode(arJobOrder.getJoCode());
                mdetails.setJoCstCustomerCode(arJobOrder.getArCustomer().getCstCustomerCode());
                mdetails.setJoCstName(arJobOrder.getArCustomer().getCstName());
                mdetails.setJoDate(arJobOrder.getJoDate());
                mdetails.setJoDocumentNumber(arJobOrder.getJoDocumentNumber());
                mdetails.setJoReferenceNumber(arJobOrder.getJoReferenceNumber());
                mdetails.setJoTransactionType(arJobOrder.getJoTransactionType());
                mdetails.setJoTechnician(arJobOrder.getJoTechnician());

                list.add(mdetails);
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArJoByCriteria(HashMap criteria, boolean isChild, Integer OFFSET, Integer LIMIT, String ORDER_BY, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArFindJobOrderControllerBean getArJoByCriteria");
        ArrayList list = new ArrayList();
        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(jo) FROM ArJobOrder jo ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter
            if (criteria.containsKey("jobOrderType")) {

                criteriaSize--;
            }

            if (criteria.containsKey("referenceNumber")) {

                criteriaSize--;
            }

            if (criteria.containsKey("transactionType")) {

                criteriaSize--;
            }

            if (criteria.containsKey("technician")) {

                criteriaSize--;
            }

            if (criteria.containsKey("jobOrderStatus")) {

                criteriaSize--;
            }

            if (criteria.containsKey("approvalStatus")) {

                String approvalStatus = (String) criteria.get("approvalStatus");

                if (approvalStatus.equals("DRAFT") || approvalStatus.equals("REJECTED")) {

                    criteriaSize--;
                }
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("jobOrderType")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("jo.arJobOrderType.jotName = '").append(criteria.get("jobOrderType")).append("' ");
            }

            if (criteria.containsKey("referenceNumber")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("jo.joReferenceNumber LIKE '%").append(criteria.get("referenceNumber")).append("%' ");
            }

            if (criteria.containsKey("jobOrderStatus")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("jo.joJobOrderStatus LIKE '%").append(criteria.get("jobOrderStatus")).append("%' ");
            }

            if (criteria.containsKey("transactionType")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("jo.joTransactionType = '").append(criteria.get("transactionType")).append("' ");
            }

            if (criteria.containsKey("technician")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("jo.joTechnician = '").append(criteria.get("technician")).append("' ");
            }

            if (criteria.containsKey("customerCode")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("jo.arCustomer.cstCustomerCode=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerCode");
                ctr++;
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("jo.joVoid=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("jobOrderVoid");
            ctr++;

            if (criteria.containsKey("currency")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("jo.glFunctionalCurrency.fcName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("currency");
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

                    jbossQl.append("jo.joApprovalStatus IS NULL ");

                } else if (approvalStatus.equals("REJECTED")) {

                    jbossQl.append("jo.joReasonForRejection IS NOT NULL ");

                } else {

                    jbossQl.append("jo.joApprovalStatus=?").append(ctr + 1).append(" ");
                    obj[ctr] = approvalStatus;
                    ctr++;
                }
            }

            if (criteria.containsKey("posted")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("jo.joPosted=?").append(ctr + 1).append(" ");

                String posted = (String) criteria.get("posted");

                if (posted.equals("YES")) {

                    obj[ctr] = EJBCommon.TRUE;

                } else {

                    obj[ctr] = EJBCommon.FALSE;
                }

                ctr++;
            }

            if (criteria.containsKey("documentNumberFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("jo.joDocumentNumber>=?").append(ctr + 1).append(" ");
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

                jbossQl.append("jo.joDocumentNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("documentNumberTo");
                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("jo.joDate>=?").append(ctr + 1).append(" ");
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
                jbossQl.append("jo.joDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            // s

            if (criteria.containsKey("approvalDateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("jo.joDateApprovedRejected>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("approvalDateFrom");
                Date sample1 = (Date) criteria.get("approvalDateFrom");
                System.out.print("we: " + obj[ctr].toString());
                ctr++;
                System.out.print(sample1.toString());
            }

            if (criteria.containsKey("approvalDateTo")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("jo.joDateApprovedRejected<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("approvalDateTo");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            // e
            Debug.print("jo branch is: " + AD_BRNCH);

            jbossQl.append("jo.joAdBranch=").append(AD_BRNCH).append(" AND jo.joAdCompany=").append(AD_CMPNY).append(" ");

            if (isChild) {

                jbossQl.append(" AND jo.joLock=0 ");
            }

            String orderBy = null;

            if (ORDER_BY.equals("CUSTOMER CODE")) {

                orderBy = "jo.arCustomer.cstCustomerCode";

            } else if (ORDER_BY.equals("DOCUMENT NUMBER")) {

                orderBy = "jo.joDocumentNumber";
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy).append(", jo.joDate");

            } else {

                jbossQl.append("ORDER BY jo.joDate");
            }

            Collection arJobOrders = arJobOrderHome.getJOByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

            if (arJobOrders.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            for (Object jobOrder : arJobOrders) {

                LocalArJobOrder arJobOrder = (LocalArJobOrder) jobOrder;

                // Check If Sales Order If Completely Delivered

                ArModJobOrderDetails mdetails = new ArModJobOrderDetails();
                mdetails.setJoCode(arJobOrder.getJoCode());

                mdetails.setJoCstCustomerCode(arJobOrder.getArCustomer().getCstCustomerCode());
                mdetails.setJoCstName(arJobOrder.getArCustomer().getCstName());
                mdetails.setJoDate(arJobOrder.getJoDate());
                mdetails.setJoDocumentNumber(arJobOrder.getJoDocumentNumber());
                mdetails.setJoReferenceNumber(arJobOrder.getJoReferenceNumber());
                mdetails.setJoTransactionType(arJobOrder.getJoTransactionType());
                mdetails.setJoTechnician(arJobOrder.getJoTechnician());
                mdetails.setJoJobOrderStatus(arJobOrder.getJoJobOrderStatus());

                if (arJobOrder.getArJobOrderType() != null) {
                    mdetails.setJoType(arJobOrder.getArJobOrderType().getJotName());
                }

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

    public ArrayList getAllJobOrderTypeName(Integer AD_CMPNY) {
        Debug.print("ArFindJobOrderControllerBean getAllJobOrderType");
        try {
            Collection arJobOrderTypes = arJobOrderTypeHome.findJotAll(AD_CMPNY);
            ArrayList list = new ArrayList();
            for (Object jobOrderType : arJobOrderTypes) {
                LocalArJobOrderType arJobOrderType = (LocalArJobOrderType) jobOrderType;
                list.add(arJobOrderType.getJotName());
            }
            return list;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer getArJoSizeByCriteria(HashMap criteria, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArFindJobOrderControllerBean getArJoSizeByCriteria");
        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(jo) FROM ArJobOrder jo ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("referenceNumber")) {

                criteriaSize--;
            }

            if (criteria.containsKey("transactionType")) {

                criteriaSize--;
            }

            if (criteria.containsKey("jobOrderStatus")) {

                criteriaSize--;
            }

            if (criteria.containsKey("approvalStatus")) {

                String approvalStatus = (String) criteria.get("approvalStatus");

                if (approvalStatus.equals("DRAFT") || approvalStatus.equals("REJECTED")) {

                    criteriaSize--;
                }
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("referenceNumber")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("jo.joReferenceNumber LIKE '%").append(criteria.get("referenceNumber")).append("%' ");
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("jobOrderStatus")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("jo.joJobOrderStatus LIKE '%").append(criteria.get("jobOrderStatus")).append("%' ");
            }

            if (criteria.containsKey("transactionType")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("jo.joTransactionType  = '").append(criteria.get("transactionType")).append("' ");
            }

            if (criteria.containsKey("customerCode")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("jo.arCustomer.cstCustomerCode=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerCode");
                ctr++;
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("jo.joVoid=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("jobOrderVoid");
            ctr++;

            if (criteria.containsKey("currency")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("jo.glFunctionalCurrency.fcName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("currency");
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

                    jbossQl.append("jo.joApprovalStatus IS NULL ");

                } else if (approvalStatus.equals("REJECTED")) {

                    jbossQl.append("jo.joReasonForRejection IS NOT NULL ");

                } else {

                    jbossQl.append("jo.joApprovalStatus=?").append(ctr + 1).append(" ");
                    obj[ctr] = approvalStatus;
                    ctr++;
                }
            }

            if (criteria.containsKey("posted")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("jo.joPosted=?").append(ctr + 1).append(" ");

                String posted = (String) criteria.get("posted");

                if (posted.equals("YES")) {

                    obj[ctr] = EJBCommon.TRUE;

                } else {

                    obj[ctr] = EJBCommon.FALSE;
                }

                ctr++;
            }

            if (criteria.containsKey("documentNumberFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("jo.joDocumentNumber>=?").append(ctr + 1).append(" ");
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

                jbossQl.append("jo.joDocumentNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("documentNumberTo");
                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("jo.joDate>=?").append(ctr + 1).append(" ");
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
                jbossQl.append("jo.joDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            // s

            if (criteria.containsKey("approvalDateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("jo.joDateApprovedRejected>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("approvalDateFrom");
                Date sample1 = (Date) criteria.get("approvalDateFrom");
                ctr++;
            }

            if (criteria.containsKey("approvalDateTo")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("jo.joDateApprovedRejected<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("approvalDateTo");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("jo.joAdBranch=").append(AD_BRNCH).append(" AND jo.joAdCompany=").append(AD_CMPNY).append(" ");

            Collection arJobOrders = arJobOrderHome.getJOByCriteria(jbossQl.toString(), obj, 0, 0);

            if (arJobOrders.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            return arJobOrders.size();

        } catch (GlobalNoRecordFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApFindSalesOrderControllerBean ejbCreate");
    }

}