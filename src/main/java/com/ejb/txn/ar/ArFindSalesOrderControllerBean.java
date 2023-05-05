/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArFindSalesOrderControllerBean
 * @created
 * @author
 */
package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.dao.ar.LocalArSalesOrderHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ar.LocalArSalesOrder;
import com.ejb.entities.ar.LocalArSalesOrderLine;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.*;
import com.util.mod.ar.ArModSalesOrderDetails;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

@Stateless(name = "ArFindSalesOrderControllerEJB")
public class ArFindSalesOrderControllerBean extends EJBContextClass implements ArFindSalesOrderController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalArSalesOrderHome arSalesOrderHome;

    public ArrayList getArSoForProcessingByBrCode(Integer AD_BRNCH, Integer AD_CMPNY) {
        Debug.print("ArFindSalesOrderControllerBean getArSoForProcessingByBrCode");
        ArrayList list = new ArrayList();
        try {

            Collection arSalesOrderColl = arSalesOrderHome.findPostedSoByBrCode(AD_BRNCH, AD_CMPNY);

            for (Object o : arSalesOrderColl) {

                LocalArSalesOrder arSalesOrder = (LocalArSalesOrder) o;

                if (!((arSalesOrder.getSoApprovalStatus().equals("APPROVED") || arSalesOrder.getSoApprovalStatus().equals("N/A")) && arSalesOrder.getSoLock() == (byte) (0))) {
                    continue;
                }

                ArModSalesOrderDetails mdetails = new ArModSalesOrderDetails();
                mdetails.setSoCode(arSalesOrder.getSoCode());
                mdetails.setSoDocumentType(arSalesOrder.getSoDocumentType());
                mdetails.setSoCstCustomerCode(arSalesOrder.getArCustomer().getCstCustomerCode());
                mdetails.setSoCstName(arSalesOrder.getArCustomer().getCstName());
                mdetails.setSoDate(arSalesOrder.getSoDate());
                mdetails.setSoDocumentNumber(arSalesOrder.getSoDocumentNumber());
                mdetails.setSoReferenceNumber(arSalesOrder.getSoReferenceNumber());
                mdetails.setSoTransactionType(arSalesOrder.getSoTransactionType());

                list.add(mdetails);
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArSoByCriteria(HashMap criteria, boolean isChild, Integer OFFSET, Integer LIMIT, String ORDER_BY, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArFindSalesOrderControllerBean getArSoByCriteria");

        ArrayList list = new ArrayList();

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(so) FROM ArSalesOrder so ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("referenceNumber")) {

                criteriaSize--;
            }

            if (criteria.containsKey("documentType")) {

                criteriaSize--;
            }

            if (criteria.containsKey("transactionType")) {

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

                jbossQl.append("so.soReferenceNumber LIKE '%").append(criteria.get("referenceNumber")).append("%' ");
            }

            if (criteria.containsKey("documentType")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("so.soDocumentType = '").append(criteria.get("documentType")).append("' ");
            }

            if (criteria.containsKey("transactionType")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("so.soTransactionType = '").append(criteria.get("transactionType")).append("' ");
            }

            if (criteria.containsKey("customerCode")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("so.arCustomer.cstCustomerCode=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerCode");
                ctr++;
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("so.soVoid=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("salesOrderVoid");
            ctr++;

            if (criteria.containsKey("currency")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("so.glFunctionalCurrency.fcName=?").append(ctr + 1).append(" ");
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

                    jbossQl.append("so.soApprovalStatus IS NULL ");

                } else if (approvalStatus.equals("REJECTED")) {

                    jbossQl.append("so.soReasonForRejection IS NOT NULL ");

                } else {

                    jbossQl.append("so.soApprovalStatus=?").append(ctr + 1).append(" ");
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

                jbossQl.append("so.soPosted=?").append(ctr + 1).append(" ");

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

                jbossQl.append("so.soDocumentNumber>=?").append(ctr + 1).append(" ");
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

                jbossQl.append("so.soDocumentNumber<=?").append(ctr + 1).append(" ");
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
                jbossQl.append("so.soDate>=?").append(ctr + 1).append(" ");
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
                jbossQl.append("so.soDate<=?").append(ctr + 1).append(" ");
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
                jbossQl.append("so.soDateApprovedRejected>=?").append(ctr + 1).append(" ");
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
                jbossQl.append("so.soDateApprovedRejected<=?").append(ctr + 1).append(" ");
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

            jbossQl.append("so.soAdBranch=").append(AD_BRNCH).append(" AND so.soAdCompany=").append(AD_CMPNY).append(" ");

            if (isChild) {

                jbossQl.append(" AND so.soLock=0 ");
            }

            String orderBy = null;

            if (ORDER_BY.equals("CUSTOMER CODE")) {

                orderBy = "so.arCustomer.cstCustomerCode";

            } else if (ORDER_BY.equals("DOCUMENT NUMBER")) {

                orderBy = "so.soDocumentNumber";
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy).append(", so.soDate");

            } else {

                jbossQl.append("ORDER BY so.soDate");
            }

            Collection arSalesOrders = arSalesOrderHome.getSOByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);
            if (arSalesOrders.size() <= 0) {
                throw new GlobalNoRecordFoundException();
            }

            for (Object salesOrder : arSalesOrders) {

                LocalArSalesOrder arSalesOrder = (LocalArSalesOrder) salesOrder;

                // Check If Sales Order If Completely Delivered

                ArModSalesOrderDetails mdetails = new ArModSalesOrderDetails();
                mdetails.setSoDocumentType(arSalesOrder.getSoDocumentType());
                mdetails.setSoCode(arSalesOrder.getSoCode());
                mdetails.setSoCstCustomerCode(arSalesOrder.getArCustomer().getCstCustomerCode());
                mdetails.setSoCstName(arSalesOrder.getArCustomer().getCstName());
                mdetails.setSoDate(arSalesOrder.getSoDate());
                mdetails.setSoDocumentNumber(arSalesOrder.getSoDocumentNumber());
                mdetails.setSoReferenceNumber(arSalesOrder.getSoReferenceNumber());
                mdetails.setSoTransactionType(arSalesOrder.getSoTransactionType());
                LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(arSalesOrder.getSoAdBranch());

                mdetails.setSoBranchCode(adBranch.getBrBranchCode());

                String status = arSalesOrder.getSoApprovalStatus();
                if (arSalesOrder.getSoVoid() == EJBCommon.TRUE) {
                    status = "VOID";
                } else if (arSalesOrder.getSoPosted() == EJBCommon.FALSE) {
                    status = "DRAFT";
                } else if (status.equals("N/A")) {
                    status = "POSTED";
                }
                mdetails.setSoStatus(status);


                double amount = 0;

                for (Object o : arSalesOrder.getArSalesOrderLines()) {

                    LocalArSalesOrderLine arSalesOrderLine = (LocalArSalesOrderLine) o;

                    amount += arSalesOrderLine.getSolAmount();

                }

                mdetails.setSoAmount(EJBCommon.convertDoubleToStringMoney(amount, adPreference.getPrfInvCostPrecisionUnit()));

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

    public Integer getArSoSizeByCriteria(HashMap criteria, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArFindSalesOrderControllerBean getArSoSizeByCriteria");
        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(so) FROM ArSalesOrder so ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("documentType")) {

                criteriaSize--;
            }

            if (criteria.containsKey("referenceNumber")) {

                criteriaSize--;
            }

            if (criteria.containsKey("transactionType")) {

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

                jbossQl.append("so.soReferenceNumber LIKE '%").append(criteria.get("referenceNumber")).append("%' ");
            }

            if (criteria.containsKey("documentType")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("so.soDocumentType  = '").append(criteria.get("documentType")).append("' ");
            }

            if (criteria.containsKey("transactionType")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("so.soTransactionType  = '").append(criteria.get("transactionType")).append("' ");
            }

            if (criteria.containsKey("customerCode")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("so.arCustomer.cstCustomerCode=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerCode");
                ctr++;
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("so.soVoid=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("salesOrderVoid");
            ctr++;

            if (criteria.containsKey("currency")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("so.glFunctionalCurrency.fcName=?").append(ctr + 1).append(" ");
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

                    jbossQl.append("so.soApprovalStatus IS NULL ");

                } else if (approvalStatus.equals("REJECTED")) {

                    jbossQl.append("so.soReasonForRejection IS NOT NULL ");

                } else {

                    jbossQl.append("so.soApprovalStatus=?").append(ctr + 1).append(" ");
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

                jbossQl.append("so.soPosted=?").append(ctr + 1).append(" ");

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

                jbossQl.append("so.soDocumentNumber>=?").append(ctr + 1).append(" ");
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

                jbossQl.append("so.soDocumentNumber<=?").append(ctr + 1).append(" ");
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
                jbossQl.append("so.soDate>=?").append(ctr + 1).append(" ");
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
                jbossQl.append("so.soDate<=?").append(ctr + 1).append(" ");
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
                jbossQl.append("so.soDateApprovedRejected>=?").append(ctr + 1).append(" ");
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
                jbossQl.append("so.soDateApprovedRejected<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("approvalDateTo");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("so.soAdBranch=").append(AD_BRNCH).append(" AND so.soAdCompany=").append(AD_CMPNY).append(" ");

            Collection arSalesOrders = arSalesOrderHome.getSOByCriteria(jbossQl.toString(), obj, 0, 0);

            if (arSalesOrders.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            return arSalesOrders.size();

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {
        Debug.print("ApFindSalesOrderControllerBean ejbCreate");
    }
}