/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class InvFindPhysicalInventoryControllerBean
 * @created July 09, 2004, 10:08 AM
 * @author EnricoC. Yap
 */
package com.ejb.txn.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.inv.LocalInvPhysicalInventoryHome;
import com.ejb.entities.inv.LocalInvPhysicalInventory;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.mod.inv.InvModPhysicalInventoryDetails;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Stateless(name = "InvFindPhysicalInventoryControllerEJB")
public class InvFindPhysicalInventoryControllerBean extends EJBContextClass
        implements InvFindPhysicalInventoryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalInvPhysicalInventoryHome invPhysicalInventoryHome;

    public ArrayList getInvPiByCriteria(
            HashMap criteria, Integer OFFSET, Integer LIMIT, String ORDER_BY, Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalNoRecordFoundException {

        Debug.print("InvFindPhysicalInventoryControllerBean getInvPiByCriteria");
        ArrayList list = new ArrayList();
        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(pi) FROM InvPhysicalInventory pi ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("referenceNumber")) {

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

                jbossQl.append("pi.piReferenceNumber LIKE '%").append(criteria.get("referenceNumber")).append("%' ");
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("pi.piDate>=?").append(ctr + 1).append(" ");
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
                jbossQl.append("pi.piDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (criteria.containsKey("location")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pi.invLocation.locName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("location");
                ctr++;
            }

            if (criteria.containsKey("category")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pi.piAdLvCategory=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("category");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("pi.piAdBranch=").append(AD_BRNCH).append(" AND pi.piAdCompany=").append(AD_CMPNY).append(" ");

            String orderBy = null;

            switch (ORDER_BY) {
                case "REFERENCE NUMBER":

                    orderBy = "pi.piReferenceNumber";

                    break;
                case "CATEGORY":

                    orderBy = "pi.piAdLvCategory";

                    break;
                case "LOCATION":

                    orderBy = "pi.invLocation.locName";
                    break;
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy).append(", pi.piDate");

            } else {

                jbossQl.append("ORDER BY pi.piDate");
            }

            Collection invPhysicalInventories = invPhysicalInventoryHome.getPiByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

            if (invPhysicalInventories.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            for (Object physicalInventory : invPhysicalInventories) {

                LocalInvPhysicalInventory invPhysicalInventory = (LocalInvPhysicalInventory) physicalInventory;

                InvModPhysicalInventoryDetails mdetails = new InvModPhysicalInventoryDetails();
                mdetails.setPiCode(invPhysicalInventory.getPiCode());
                mdetails.setPiDate(invPhysicalInventory.getPiDate());
                mdetails.setPiReferenceNumber(invPhysicalInventory.getPiReferenceNumber());
                mdetails.setPiDescription(invPhysicalInventory.getPiDescription());
                try {
                    mdetails.setPiLocName(invPhysicalInventory.getInvLocation().getLocName());
                }
                catch (Exception e) {
                    mdetails.setPiLocName("");
                }

                mdetails.setPiAdLvCategory(invPhysicalInventory.getPiAdLvCategory());

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

    public Integer getInvPiSizeByCriteria(
            HashMap criteria, Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalNoRecordFoundException {

        Debug.print("InvFindPhysicalInventoryControllerBean getInvPiSizeByCriteria");
        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(pi) FROM InvPhysicalInventory pi ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("referenceNumber")) {

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

                jbossQl.append("pi.piReferenceNumber LIKE '%").append(criteria.get("referenceNumber")).append("%' ");
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("pi.piDate>=?").append(ctr + 1).append(" ");
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
                jbossQl.append("pi.piDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (criteria.containsKey("location")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pi.invLocation.locName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("location");
                ctr++;
            }

            if (criteria.containsKey("category")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pi.piAdLvCategory=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("category");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("pi.piAdBranch=").append(AD_BRNCH).append(" AND pi.piAdCompany=").append(AD_CMPNY).append(" ");

            Collection invPhysicalInventories = invPhysicalInventoryHome.getPiByCriteria(jbossQl.toString(), obj, 0, 0);

            if (invPhysicalInventories.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            return invPhysicalInventories.size();

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

        Debug.print("InvFindPhysicalInventoryControllerBean ejbCreate");
    }

}