/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ApSupplierTypeControllerBean
 * @created
 * @author
 **/
package com.ejb.txn.ap;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;


import java.util.ArrayList;
import java.util.Collection;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import javax.naming.NamingException;

import com.ejb.entities.ad.LocalAdBankAccount;
import com.ejb.dao.ad.LocalAdBankAccountHome;
import com.ejb.entities.ap.LocalApSupplierType;
import com.ejb.dao.ap.LocalApSupplierTypeHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.EJBContextClass;
import com.util.ap.ApSupplierTypeDetails;
import com.util.mod.ap.ApModSupplierTypeDetails;
import com.util.Debug;
import com.util.EJBHomeFactory;

@Stateless(name = "ApSupplierTypeControllerEJB")
public class ApSupplierTypeControllerBean extends EJBContextClass implements ApSupplierTypeController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalApSupplierTypeHome apSupplierTypeHome;


    public ArrayList getApStAll(Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApSupplierTypeControllerBean getApStAll");

        ArrayList list = new ArrayList();

        try {

            Collection apSupplierTypes = apSupplierTypeHome.findStAll(AD_CMPNY);

            if (apSupplierTypes.isEmpty()) {

                throw new GlobalNoRecordFoundException();

            }

            for (Object supplierType : apSupplierTypes) {

                LocalApSupplierType apSupplierType = (LocalApSupplierType) supplierType;

                ApModSupplierTypeDetails mdetails = new ApModSupplierTypeDetails();
                mdetails.setStCode(apSupplierType.getStCode());
                mdetails.setStName(apSupplierType.getStName());
                mdetails.setStDescription(apSupplierType.getStDescription());
                mdetails.setStEnable(apSupplierType.getStEnable());
                mdetails.setStBaName(apSupplierType.getAdBankAccount().getBaName());

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

        Debug.print("ApSupplierTypeControllerBean getAdBaAll");

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


    public void addApStEntry(ApSupplierTypeDetails details, String ST_BA_NM, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException {

        Debug.print("ApSupplierTypeControllerBean addApStEntry");

        ArrayList list = new ArrayList();

        try {

            LocalApSupplierType apSupplierType = null;
            LocalAdBankAccount adBankAccount = null;

            try {

                apSupplierType = apSupplierTypeHome.findByStName(details.getStName(), AD_CMPNY);

                throw new GlobalRecordAlreadyExistException();

            } catch (FinderException ex) {


            }

            // create new supplier class

            apSupplierType = apSupplierTypeHome.create(details.getStName(), details.getStDescription(), details.getStEnable(), AD_CMPNY);

            adBankAccount = adBankAccountHome.findByBaName(ST_BA_NM, AD_CMPNY);
            adBankAccount.addApSupplierType(apSupplierType);

        } catch (GlobalRecordAlreadyExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }


    public void updateApStEntry(ApSupplierTypeDetails details, String ST_BA_NM, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException {

        Debug.print("ApSupplierTypeControllerBean updateApTcEntry");

        LocalApSupplierType apSupplierType = null;
        LocalAdBankAccount adBankAccount = null;

        ArrayList list = new ArrayList();

        try {

            try {

                LocalApSupplierType apExistingSupplierType = apSupplierTypeHome.findByStName(details.getStName(), AD_CMPNY);

                if (!apExistingSupplierType.getStCode().equals(details.getStCode())) {

                    throw new GlobalRecordAlreadyExistException();

                }

            } catch (FinderException ex) {

            }

            // find and update supplier class

            apSupplierType = apSupplierTypeHome.findByPrimaryKey(details.getStCode());

            apSupplierType.setStName(details.getStName());
            apSupplierType.setStDescription(details.getStDescription());
            apSupplierType.setStEnable(details.getStEnable());

            adBankAccount = adBankAccountHome.findByBaName(ST_BA_NM, AD_CMPNY);
            adBankAccount.addApSupplierType(apSupplierType);

        } catch (GlobalRecordAlreadyExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }


    public void deleteApStEntry(Integer ST_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException {

        Debug.print("ApSupplierTypeControllerBean deleteApStEntry");

        try {

            LocalApSupplierType apSupplierType = null;

            try {

                apSupplierType = apSupplierTypeHome.findByPrimaryKey(ST_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();

            }

            if (!apSupplierType.getApSuppliers().isEmpty()) {

                throw new GlobalRecordAlreadyAssignedException();

            }

//		    apSupplierType.entityRemove();
            em.remove(apSupplierType);

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

        Debug.print("ApSupplierTypeControllerBean ejbCreate");

    }

}