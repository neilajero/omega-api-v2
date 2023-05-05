package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ar.LocalArPersonelHome;
import com.ejb.dao.ar.LocalArPersonelTypeHome;
import com.ejb.entities.ar.LocalArPersonel;
import com.ejb.entities.ar.LocalArPersonelType;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.mod.ar.ArModPersonelDetails;
import com.util.Debug;
import com.util.EJBContextClass;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Stateless(name = "ArFindPersonelControllerEJB")
public class ArFindPersonelControllerBean extends EJBContextClass implements ArFindPersonelController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalArPersonelHome arPersonelHome;
    @EJB
    private LocalArPersonelTypeHome arPersonelTypeHome;

    public ArrayList getArPtNmAll(Integer AD_CMPNY) {
        Debug.print("ArFindPersonelControllerBean getArPtNmAll");
        ArrayList list = new ArrayList();
        try {

            Collection arPersonelTypes = arPersonelTypeHome.findPtAll(AD_CMPNY);

            for (Object personelType : arPersonelTypes) {

                LocalArPersonelType arPersonelType = (LocalArPersonelType) personelType;

                list.add(arPersonelType.getPtName());

            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }
    }

    public ArrayList getArPeByCriteria(HashMap criteria, Integer OFFSET, Integer LIMIT, String ORDER_BY, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArFindPersonelControllerBean getArPeByCriteria");
        ArrayList list = new ArrayList();
        try {

            StringBuffer jbossQl = new StringBuffer();


            jbossQl.append("SELECT OBJECT(pe) FROM ArPersonel pe ");


            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Debug.print("crit size:" + criteriaSize);


            Object[] obj = null;

            // Allocate the size of the object parameter


            if (criteria.containsKey("idNumber")) {

                criteriaSize--;

            }

            if (criteria.containsKey("personelName")) {

                criteriaSize--;

            }


            obj = new Object[criteriaSize];

            if (criteria.containsKey("idNumber")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("pe.peIdNumber LIKE '%").append(criteria.get("idNumber")).append("%' ");

            }


            if (criteria.containsKey("personelName")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pe.peName LIKE '%").append(criteria.get("personelName")).append("%' ");

            }


            if (criteria.containsKey("personelType")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pe.arPersonelType.ptName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("personelType");
                ctr++;

            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");

            }


            jbossQl.append("pe.peAdCompany=").append(AD_CMPNY).append(" ");

            String orderBy = null;

            if (ORDER_BY.equals("ID NUMBER")) {

                orderBy = "pe.peIdNumber";

            } else {

                orderBy = "pe.peName";

            }

            jbossQl.append("ORDER BY ").append(orderBy);
            Debug.print(jbossQl.toString());
            Collection arPersonels = arPersonelHome.getPeByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

            if (arPersonels.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            for (Object personel : arPersonels) {

                LocalArPersonel arPersonel = (LocalArPersonel) personel;

                ArModPersonelDetails mdetails = new ArModPersonelDetails();


                mdetails.setPeCode(arPersonel.getPeCode());
                mdetails.setPeIdNumber(arPersonel.getPeIdNumber());
                mdetails.setPeName(arPersonel.getPeName());
                mdetails.setPeDescription(arPersonel.getPeDescription());
                mdetails.setPeAddress(arPersonel.getPeAddress());
                mdetails.setPeAdCompany(arPersonel.getPeAdCompany());
                mdetails.setPePtShortName(arPersonel.getArPersonelType() == null ? "" : arPersonel.getArPersonelType().getPtShortName());
                mdetails.setPePtName(arPersonel.getArPersonelType() == null ? "" : arPersonel.getArPersonelType().getPtName());
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

    public Integer getArPeSizeByCriteria(HashMap criteria, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArFindPersonelControllerBean getArPeSizeByCriteria");
        try {

            StringBuffer jbossQl = new StringBuffer();


            jbossQl.append("SELECT OBJECT(pe) FROM ArPersonel pe ");


            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();


            Object[] obj = null;

            // Allocate the size of the object parameter


            if (criteria.containsKey("idNumber")) {

                criteriaSize--;

            }

            if (criteria.containsKey("personelName")) {

                criteriaSize--;

            }


            obj = new Object[criteriaSize];

            if (criteria.containsKey("idNumber")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("pe.peIdNumber LIKE '%").append(criteria.get("idNumber")).append("%' ");

            }


            if (criteria.containsKey("personelName")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pe.peName LIKE '%").append(criteria.get("personelName")).append("%' ");

            }


            if (criteria.containsKey("personelType")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pe.arPersonelType.ptName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("personelType");
                ctr++;

            }


            jbossQl.append("pe.peAdCompany=").append(AD_CMPNY).append(" ");
            Debug.print(jbossQl.toString());
            Collection arPersonels = arPersonelHome.getPeByCriteria(jbossQl.toString(), obj, 0, 0);

            if (arPersonels.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            return arPersonels.size();

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {


            ex.printStackTrace();
            throw new EJBException(ex.getMessage());

        }

    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ArFindPersonelControllerBean ejbCreate");

    }

}