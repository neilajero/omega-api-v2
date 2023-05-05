/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArFindDeliveryControllerBean
 * @created
 * @author
 */
package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ar.LocalArDeliveryHome;
import com.ejb.entities.ar.LocalArDelivery;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.*;
import com.util.mod.ar.ArModDeliveryDetails;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Stateless(name = "ArFindDeliveryControllerEJB")
public class ArFindDeliveryControllerBean extends EJBContextClass implements ArFindDeliveryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalArDeliveryHome arDeliveryHome;

    public ArrayList getArDvByCriteria(HashMap criteria, boolean isChild, Integer OFFSET, Integer LIMIT, String ORDER_BY, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArFindDeliveryControllerBean getArDvByCriteria");
        ArrayList list = new ArrayList();
        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(dv) FROM ArDelivery dv ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("referenceNumber")) {

                criteriaSize--;
            }

            if (criteria.containsKey("transactionStatus")) {

                criteriaSize--;
            }

            if (criteria.containsKey("container")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("referenceNumber")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("dv.arSalesOrder.soReferenceNumber LIKE '%").append(criteria.get("referenceNumber")).append("%' ");
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("container")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("dv.dvContainer LIKE '%").append(criteria.get("container")).append("%' ");
            }

            if (criteria.containsKey("transactionStatus")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("dv.arSalesOrder.soTransactionStatus = '").append(criteria.get("transactionStatus")).append("' ");
            }

            if (criteria.containsKey("customerCode")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("dv.arSalesOrder.arCustomer.cstCustomerCode=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerCode");
                ctr++;
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("dv.arSalesOrder.soVoid=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("salesOrderVoid");
            ctr++;

            if (criteria.containsKey("posted")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("dv.arSalesOrder.soPosted=?").append(ctr + 1).append(" ");

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

                jbossQl.append("dv.arSalesOrder.soDocumentNumber>=?").append(ctr + 1).append(" ");
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

                jbossQl.append("dv.arSalesOrder.soDocumentNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("documentNumberTo");
                ctr++;
            }

            if (criteria.containsKey("deliveryNumberFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("dv.dvDeliveryNumber>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("deliveryNumberFrom");
                ctr++;
            }

            if (criteria.containsKey("deliveryNumberTo")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("dv.dvDeliveryNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("deliveryNumberTo");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            // e

            jbossQl.append("dv.arSalesOrder.soAdBranch=").append(AD_BRNCH).append(" AND dv.arSalesOrder.soAdCompany=").append(AD_CMPNY).append(" ");

            if (isChild) {

                jbossQl.append(" AND dv.arSalesOrder.soLock=0 ");
            }

            String orderBy = null;

            if (ORDER_BY == null) {
                ORDER_BY = "CUSTOMER CODE";
            }

            switch (ORDER_BY) {
                case "CUSTOMER CODE":

                    orderBy = "dv.arSalesOrder.arCustomer.cstCustomerCode";

                    break;
                case "DOCUMENT NUMBER":

                    orderBy = "dv.arSalesOrder.soDocumentNumber";

                    break;
                case "REFERENCE NUMBER":

                    orderBy = "dv.arSalesOrder.soReferenceNumber";

                    break;
                case "CONTAINER":

                    orderBy = "dv.dvContainer";

                    break;
                case "DELIVERY NUMBER":

                    orderBy = "dv.dvDeliveryNumber";

                    break;
                default:
                    orderBy = "dv.arSalesOrder.arCustomer.cstCustomerCode";
                    break;
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy).append(", dv.arSalesOrder.soDate");

            } else {

                jbossQl.append("ORDER BY dv.arSalesOrder.soDate");
            }

            LIMIT = 0;
            OFFSET = 0;

            Debug.print("sql is: " + jbossQl);
            Debug.print("size obj is: " + obj.length);
            for (Object o : obj) {
                Debug.print("obj is: " + o.toString());
            }
            Collection arDeliverys = arDeliveryHome.getDvByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

            if (arDeliverys.size() <= 0) {
                throw new GlobalNoRecordFoundException();
            }

            for (Object delivery : arDeliverys) {

                LocalArDelivery arDelivery = (LocalArDelivery) delivery;

                ArModDeliveryDetails details = new ArModDeliveryDetails();

                details.setDvCode(arDelivery.getDvCode());
                details.setSoCode(arDelivery.getArSalesOrder().getSoCode());
                details.setDvContainer(arDelivery.getDvContainer());
                details.setDvDeliveryNumber(arDelivery.getDvDeliveryNumber());
                details.setDvBookingTime(arDelivery.getDvBookingTime());
                details.setDvTabsFeePullOut(arDelivery.getDvTabsFeePullOut());
                details.setDvReleasedDate(arDelivery.getDvReleasedDate());
                details.setDvDeliveredDate(arDelivery.getDvDeliveredDate());
                details.setDvDeliveredTo(arDelivery.getDvDeliveredTo());
                details.setDvPlateNo(arDelivery.getDvPlateNo());
                details.setDvDriverName(arDelivery.getDvDriverName());
                details.setDvEmptyReturnDate(arDelivery.getDvEmptyReturnDate());
                details.setDvEmptyReturnTo(arDelivery.getDvEmptyReturnTo());
                details.setDvTabsFeeReturn(arDelivery.getDvTabsFeeReturn());
                details.setDvStatus(arDelivery.getDvStatus());
                details.setDvRemarks(arDelivery.getDvRemarks());

                details.setCstName(arDelivery.getArSalesOrder().getArCustomer().getCstCustomerCode());
                details.setSoDocumentNumber(arDelivery.getArSalesOrder().getSoDocumentNumber());
                details.setSoReferenceNumber(arDelivery.getArSalesOrder().getSoReferenceNumber());
                details.setSoTransactionStatus(arDelivery.getArSalesOrder().getSoTransactionStatus());

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

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ApFindDeliveryControllerBean ejbCreate");
    }

}