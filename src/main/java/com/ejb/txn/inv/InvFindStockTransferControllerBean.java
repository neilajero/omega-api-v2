/**
 * @copyright 2022 Omega Business Consulting, Inc.
 * @class InvFindStockTransferControllerBean
 **/
package com.ejb.txn.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.inv.LocalInvStockTransferHome;
import com.ejb.entities.inv.LocalInvStockTransfer;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.mod.inv.InvModStockTransferDetails;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Stateless(name = "InvFindStockTransferControllerEJB")
public class InvFindStockTransferControllerBean extends EJBContextClass implements InvFindStockTransferController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalInvStockTransferHome invStockTransferHome;

    public ArrayList getInvStByCriteria(
            HashMap criteria, Integer OFFSET, Integer LIMIT, String ORDER_BY, Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalNoRecordFoundException {

        Debug.print("InvFindStockTransferControllerBean getInvStByCriteria");
        ArrayList list = new ArrayList();
        try {

            StringBuffer jbossQl = new StringBuffer();
            jbossQl.append("SELECT OBJECT(st) FROM InvStockTransfer st ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("referenceNumber")) {

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

                jbossQl.append("st.stReferenceNumber LIKE '%").append(criteria.get("referenceNumber")).append("%' ");

            }

            if (criteria.containsKey("documentNumberFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("st.stDocumentNumber>=?").append(ctr + 1).append(" ");
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

                jbossQl.append("st.stDocumentNumber<=?").append(ctr + 1).append(" ");
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
                jbossQl.append("st.stDate>=?").append(ctr + 1).append(" ");
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
                jbossQl.append("st.stDate<=?").append(ctr + 1).append(" ");
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

                    jbossQl.append("st.stApprovalStatus IS NULL ");

                } else {

                    jbossQl.append("st.stApprovalStatus=?").append(ctr + 1).append(" ");
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

                jbossQl.append("st.stPosted=?").append(ctr + 1).append(" ");

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

            jbossQl.append("st.stAdBranch=").append(AD_BRNCH).append(" AND st.stAdCompany=").append(AD_CMPNY).append(" ");

            String orderBy = null;

            switch (ORDER_BY) {
                case "REFERENCE NUMBER":

                    orderBy = "st.stReferenceNumber";

                    break;
                case "TYPE":

                    orderBy = "st.stType";

                    break;
                case "DOCUMENT NUMBER":

                    orderBy = "st.stDocumentNumber";

                    break;
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy).append(", st.stDate");

            } else {

                jbossQl.append("ORDER BY st.stDate");

            }


            Debug.print(jbossQl.toString());

            Collection invStockTransfers = invStockTransferHome.getStByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

            if (invStockTransfers.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            for (Object stockTransfer : invStockTransfers) {

                LocalInvStockTransfer invStockTransfer = (LocalInvStockTransfer) stockTransfer;

                InvModStockTransferDetails mdetails = new InvModStockTransferDetails();
                mdetails.setStCode(invStockTransfer.getStCode());
                mdetails.setStDate(invStockTransfer.getStDate());
                mdetails.setStDocumentNumber(invStockTransfer.getStDocumentNumber());
                mdetails.setStReferenceNumber(invStockTransfer.getStReferenceNumber());

                list.add(mdetails);

            }

            return list;

        }
        catch (GlobalNoRecordFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {


            ex.printStackTrace();
            throw new EJBException(ex.getMessage());

        }

    }

    public Integer getInvStSizeByCriteria(HashMap criteria, Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalNoRecordFoundException {

        Debug.print("InvFindStockTransferControllerBean getInvStByCriteria");
        try {

            StringBuffer jbossQl = new StringBuffer();
            jbossQl.append("SELECT OBJECT(st) FROM InvStockTransfer st ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("referenceNumber")) {

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

                jbossQl.append("st.stReferenceNumber LIKE '%").append(criteria.get("referenceNumber")).append("%' ");

            }

            if (criteria.containsKey("documentNumberFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("st.stDocumentNumber>=?").append(ctr + 1).append(" ");
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

                jbossQl.append("st.stDocumentNumber<=?").append(ctr + 1).append(" ");
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
                jbossQl.append("st.stDate>=?").append(ctr + 1).append(" ");
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
                jbossQl.append("st.stDate<=?").append(ctr + 1).append(" ");
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

                    jbossQl.append("st.stApprovalStatus IS NULL ");

                } else {

                    jbossQl.append("st.stApprovalStatus=?").append(ctr + 1).append(" ");
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

                jbossQl.append("st.stPosted=?").append(ctr + 1).append(" ");

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

            jbossQl.append("st.stAdBranch=").append(AD_BRNCH).append(" AND st.stAdCompany=").append(AD_CMPNY).append(" ");
            Debug.print(jbossQl.toString());
            Collection invStockTransfers = invStockTransferHome.getStByCriteria(jbossQl.toString(), obj, 0, 0);

            if (invStockTransfers.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            return invStockTransfers.size();

        }
        catch (GlobalNoRecordFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());

        }

    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("InvFindStockTransferControllerBean ejbCreate");

    }

}