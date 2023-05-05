/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class InvFindLocationControllerBean
 * @created June 3, 2004 06:08 PM
 * @author Enrico C. Yap
 **/
package com.ejb.txn.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.inv.LocalInvLocationHome;
import com.ejb.entities.inv.LocalInvLocation;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.inv.InvLocationDetails;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Stateless(name = "InvFindLocationControllerEJB")
public class InvFindLocationControllerBean extends EJBContextClass implements InvFindLocationController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalInvLocationHome invLocationHome;

    public ArrayList getInvLocByCriteria(
            HashMap criteria, Integer OFFSET, Integer LIMIT, Integer AD_CMPNY)
            throws GlobalNoRecordFoundException {

        Debug.print("InvFindLocationControllerBean getInvLocByCriteria");
        ArrayList list = new ArrayList();
        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(loc) FROM InvLocation loc ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();


            Object[] obj = null;

            // Allocate the size of the object parameter

            if (criteria.containsKey("locationName")) {

                criteriaSize--;

            }

            if (criteria.containsKey("type")) {

                criteriaSize--;

            }

            if (criteria.containsKey("contactPerson")) {

                criteriaSize--;

            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("locationName")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("loc.locName LIKE '%").append(criteria.get("locationName")).append("%' ");

            }

            if (criteria.containsKey("type")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("loc.locLvType LIKE '%").append(criteria.get("type")).append("%' ");

            }

            if (criteria.containsKey("contactPerson")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("loc.locContactPerson LIKE '%").append(criteria.get("contactPerson")).append("%' ");

            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");

            }

            jbossQl.append("loc.locAdCompany=").append(AD_CMPNY).append(" ");

            jbossQl.append("ORDER BY " + "loc.locName");


            Collection invLocations = invLocationHome.getLocByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

            if (invLocations.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            for (Object location : invLocations) {

                LocalInvLocation invLocation = (LocalInvLocation) location;

                InvLocationDetails details = new InvLocationDetails();
                details.setLocCode(invLocation.getLocCode());
                details.setLocName(invLocation.getLocName());
                details.setLocDescription(invLocation.getLocDescription());
                details.setLocLvType(invLocation.getLocLvType());
                details.setLocAddress(invLocation.getLocAddress());
                details.setLocContactPerson(invLocation.getLocContactPerson());
                details.setLocContactNumber(invLocation.getLocContactNumber());
                details.setLocEmailAddress(invLocation.getLocEmailAddress());

                list.add(details);

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

    public Integer getInvLocSizeByCriteria(HashMap criteria, Integer AD_CMPNY)
            throws GlobalNoRecordFoundException {

        Debug.print("InvFindLocationControllerBean getInvLocSizeByCriteria");
        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(loc) FROM InvLocation loc ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();


            Object[] obj = null;

            // Allocate the size of the object parameter

            if (criteria.containsKey("locationName")) {

                criteriaSize--;

            }

            if (criteria.containsKey("type")) {

                criteriaSize--;

            }

            if (criteria.containsKey("contactPerson")) {

                criteriaSize--;

            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("locationName")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("loc.locName LIKE '%").append(criteria.get("locationName")).append("%' ");

            }

            if (criteria.containsKey("type")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("loc.locLvType LIKE '%").append(criteria.get("type")).append("%' ");

            }

            if (criteria.containsKey("contactPerson")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("loc.locContactPerson LIKE '%").append(criteria.get("contactPerson")).append("%' ");

            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");

            }

            jbossQl.append("loc.locAdCompany=").append(AD_CMPNY).append(" ");


            Collection invLocations = invLocationHome.getLocByCriteria(jbossQl.toString(), obj, 0, 0);

            if (invLocations.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            return invLocations.size();

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

        Debug.print("InvFindLocationControllerBean ejbCreate");

    }

}