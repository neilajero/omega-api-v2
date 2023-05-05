/**
 * @copyright 2022 Omega Business Consulting, Inc.
 * @class InvFindAdjustmentControllerBean
 * @created June 28, 2004, 09:25 AM
 * @author Enrico C. Yap
 **/
package com.ejb.txn.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.inv.LocalInvAdjustmentHome;
import com.ejb.entities.inv.LocalInvAdjustment;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.mod.inv.InvModAdjustmentDetails;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Stateless(name = "InvFindAdjustmentControllerEJB")
public class InvFindAdjustmentControllerBean extends EJBContextClass implements InvFindAdjustmentController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalInvAdjustmentHome invAdjustmentHome;

    public ArrayList getInvAdjByCriteria(HashMap criteria,
                                         Integer OFFSET, Integer LIMIT, String ORDER_BY,
                                         Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalNoRecordFoundException {

        Debug.print("InvFindAdjustmentControllerBean getInvAdjByCriteria");
        Debug.print("Offset " + OFFSET + " Limit " + LIMIT);

        ArrayList list = new ArrayList();

        try {

            StringBuffer jbossQl = new StringBuffer();
            jbossQl.append("SELECT OBJECT(adj) FROM InvAdjustment adj ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = 0;

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("dateFrom")) {

                criteriaSize++;

            }

            if (criteria.containsKey("dateTo")) {

                criteriaSize++;

            }


            obj = new Object[criteriaSize];


            if (criteria.containsKey("referenceNumber")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("adj.adjReferenceNumber LIKE '%").append(criteria.get("referenceNumber")).append("%' ");

            }

            if (criteria.containsKey("documentNumberFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }
                String documentNumberFrom = (String) criteria.get("documentNumberFrom");
                jbossQl.append("adj.adjDocumentNumber>='").append(documentNumberFrom).append("' ");

            }

            if (criteria.containsKey("documentNumberTo")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                String documentNumberTo = (String) criteria.get("documentNumberFrom");
                jbossQl.append("adj.adjDocumentNumber<='").append(documentNumberTo).append("' ");

            }

            if (criteria.containsKey("type")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                String adjType = (String) criteria.get("type");
                jbossQl.append("adj.adjType='").append(adjType).append("' ");


            }

            if (criteria.containsKey("generated")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }


                jbossQl.append("adj.adjGenerated=").append(criteria.get("generated")).append(" ");

            }


            if (criteria.containsKey("void")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("adj.adjVoid=").append(criteria.get("void")).append(" ");

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

                } else {

                    jbossQl.append("adj.adjApprovalStatus='").append(approvalStatus).append("' ");

                }

            }

            if (criteria.containsKey("posted")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }


                String posted = (String) criteria.get("posted");

                if (posted.equals("YES")) {

                    //  	obj[ctr] = new Byte(EJBCommon.TRUE);

                    jbossQl.append("adj.adjPosted=1");

                } else {
                    jbossQl.append("adj.adjPosted=0");

                    // 	obj[ctr] = new Byte(EJBCommon.FALSE);

                }


                ctr++;

            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");

            }

            jbossQl.append("adj.adjAdBranch=").append(AD_BRNCH).append(" AND adj.adjAdCompany=").append(AD_CMPNY).append(" ");

            String orderBy = null;

            switch (ORDER_BY) {
                case "REFERENCE NUMBER":

                    orderBy = "adj.adjReferenceNumber";

                    break;
                case "TYPE":

                    orderBy = "adj.adjType";

                    break;
                case "DOCUMENT NUMBER":

                    orderBy = "adj.adjDocumentNumber";

                    break;
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy).append(", adj.adjDate");

            } else {

                jbossQl.append("ORDER BY adj.adjDate");

            }

            Collection invAdjustments = invAdjustmentHome.getAdjByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

            if (invAdjustments.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            for (Object adjustment : invAdjustments) {

                LocalInvAdjustment invAdjustment = (LocalInvAdjustment) adjustment;

                InvModAdjustmentDetails mdetails = new InvModAdjustmentDetails();
                mdetails.setAdjCode(invAdjustment.getAdjCode());
                mdetails.setAdjDate(invAdjustment.getAdjDate());
                mdetails.setAdjDocumentNumber(invAdjustment.getAdjDocumentNumber());
                mdetails.setAdjReferenceNumber(invAdjustment.getAdjReferenceNumber());
                mdetails.setAdjType(invAdjustment.getAdjType());
                try {
                    mdetails.setAdjCoaAccountNumber(invAdjustment.getGlChartOfAccount().getCoaAccountNumber());
                    mdetails.setAdjCoaAccountDescription(invAdjustment.getGlChartOfAccount().getCoaAccountDescription());

                }
                catch (Exception e) {
                    mdetails.setAdjCoaAccountNumber("");
                    mdetails.setAdjCoaAccountDescription("");
                }


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

    public Integer getInvAdjSizeByCriteria(HashMap criteria, Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalNoRecordFoundException {

        Debug.print("InvFindAdjustmentControllerBean getInvAdjSizeByCriteria");

        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(adj) FROM InvAdjustment adj ");

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

                jbossQl.append("adj.adjReferenceNumber LIKE '%").append(criteria.get("referenceNumber")).append("%' ");

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

            if (criteria.containsKey("generated")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("adj.adjGenerated=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("generated");
                ctr++;

            }

            if (criteria.containsKey("void")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("adj.adjVoid=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("void");
                Debug.print(obj[ctr].toString());

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

                } else {

                    jbossQl.append("adj.adjApprovalStatus=?").append(ctr + 1).append(" ");
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

            jbossQl.append("adj.adjAdBranch=").append(AD_BRNCH).append(" AND adj.adjAdCompany=").append(AD_CMPNY).append(" ");
            Collection invAdjustments = invAdjustmentHome.getAdjByCriteria(jbossQl.toString(), obj, 0, 0);

            if (invAdjustments.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            return invAdjustments.size();

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

        Debug.print("InvFindAdjustmentControllerBean ejbCreate");

    }

}