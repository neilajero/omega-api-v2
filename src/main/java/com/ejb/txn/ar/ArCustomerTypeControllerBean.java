/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArCustomerTypeControllerBean
 * @created
 * @author
 */
package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;

import com.ejb.entities.ad.LocalAdBankAccount;
import com.ejb.dao.ad.LocalAdBankAccountHome;
import com.ejb.entities.ar.LocalArCustomerType;
import com.ejb.dao.ar.LocalArCustomerTypeHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.EJBContextClass;
import com.util.ar.ArCustomerTypeDetails;
import com.util.Debug;

@Stateless(name = "ArCustomerTypeControllerEJB")
public class ArCustomerTypeControllerBean extends EJBContextClass implements ArCustomerTypeController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    LocalArCustomerTypeHome arCustomerTypeHome;
    @EJB
    LocalAdBankAccountHome adBankAccountHome;

    public ArrayList getArCtAll(Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArCustomerTypeControllerBean getArCtAll");

        ArrayList list = new ArrayList();

        try {

            Collection arCustomerTypes = arCustomerTypeHome.findCtAll(AD_CMPNY);

            if (arCustomerTypes.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            for (Object customerType : arCustomerTypes) {

                LocalArCustomerType arCustomerType = (LocalArCustomerType) customerType;

                ArCustomerTypeDetails mdetails = new ArCustomerTypeDetails(arCustomerType);
                /*
                 *
                 * mdetails.setCtCode(arCustomerType.getCtCode());
                 * mdetails.setCtName(arCustomerType.getCtName());
                 * mdetails.setCtDescription(arCustomerType.getCtDescription());
                 * mdetails.setCtEnable(arCustomerType.getCtEnable());
                 *
                 */

                mdetails.setCtBaName(arCustomerType.getAdBankAccount().getBaName());

                list.add(mdetails);
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdBaAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ArCustomerTypeControllerBean getAdBaAll");

        LocalAdBankAccount adBankAccount = null;

        Collection adBankAccounts = null;

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

    public void addArCtEntry(ArCustomerTypeDetails details, String CT_BA_NM, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException {

        Debug.print("ArCustomerTypeControllerBean addArCtEntry");

        ArrayList list = new ArrayList();

        try {

            LocalArCustomerType arCustomerType = new LocalArCustomerType();
            LocalAdBankAccount adBankAccount = null;

            try {

                arCustomerType = arCustomerTypeHome.findByCtName(details.getCtName(), AD_CMPNY);

                throw new GlobalRecordAlreadyExistException();

            } catch (FinderException ex) {

            }

            // create new supplier class

            /*
             *
             * arCustomerType = arCustomerTypeHome.create(details.getCtName(),
             * details.getCtDescription(), details.getCtEnable(), AD_CMPNY);
             */
            details.setCtAdCompany(AD_CMPNY);

            arCustomerType.setValues(details);
            em.persist(arCustomerType);

            adBankAccount = adBankAccountHome.findByBaName(CT_BA_NM, AD_CMPNY);
            adBankAccount.addArCustomerType(arCustomerType);

        } catch (GlobalRecordAlreadyExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateArCtEntry(ArCustomerTypeDetails details, String CT_BA_NM, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException {

        Debug.print("ArCustomerTypeControllerBean updateApTcEntry");

        LocalArCustomerType arCustomerType = null;
        LocalAdBankAccount adBankAccount = null;

        ArrayList list = new ArrayList();

        try {

            try {

                LocalArCustomerType apExistingSupplierType = arCustomerTypeHome.findByCtName(details.getCtName(), AD_CMPNY);

                if (!apExistingSupplierType.getCtCode().equals(details.getCtCode())) {

                    throw new GlobalRecordAlreadyExistException();
                }

            } catch (FinderException ex) {

            }

            // find and update supplier class

            arCustomerType = arCustomerTypeHome.findByPrimaryKey(details.getCtCode());

            arCustomerType.setCtName(details.getCtName());
            arCustomerType.setCtDescription(details.getCtDescription());
            arCustomerType.setCtEnable(details.getCtEnable());

            adBankAccount = adBankAccountHome.findByBaName(CT_BA_NM, AD_CMPNY);
            adBankAccount.addArCustomerType(arCustomerType);

        } catch (GlobalRecordAlreadyExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteArCtEntry(Integer CT_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException {

        Debug.print("ArCustomerTypeControllerBean deleteArCtEntry");

        try {

            LocalArCustomerType arCustomerType = null;

            try {

                arCustomerType = arCustomerTypeHome.findByPrimaryKey(CT_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            if (!arCustomerType.getArCustomers().isEmpty()) {

                throw new GlobalRecordAlreadyAssignedException();
            }

            em.remove(arCustomerType);

        } catch (GlobalRecordAlreadyDeletedException | GlobalRecordAlreadyAssignedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ArCustomerTypeControllerBean ejbCreate");
    }
}