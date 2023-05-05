/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArPersonelTypeControllerBean
 * @created
 * @author
 */
package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdBankAccountHome;
import com.ejb.dao.ar.LocalArPersonelTypeHome;
import com.ejb.entities.ad.LocalAdBankAccount;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ar.LocalArPersonelType;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.mod.ar.ArModPersonelTypeDetails;
import com.util.Debug;
import com.util.EJBContextClass;

import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.Collection;

@Stateless(name = "ArPersonelTypeControllerEJB")
public class ArPersonelTypeControllerBean extends EJBContextClass implements ArPersonelTypeController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalArPersonelTypeHome arPersonelTypeHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;

    public ArrayList getArPtAll(Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArPersonelTypeControllerBean getArCtAll");
        ArrayList list = new ArrayList();
        try {

            Collection arPersonelTypes = arPersonelTypeHome.findPtAll(AD_CMPNY);

            if (arPersonelTypes.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            for (Object personelType : arPersonelTypes) {

                LocalArPersonelType arPersonelType = (LocalArPersonelType) personelType;

                ArModPersonelTypeDetails mdetails = new ArModPersonelTypeDetails();

                mdetails.setPtCode(arPersonelType.getPtCode());
                mdetails.setPtShortName(arPersonelType.getPtShortName());
                mdetails.setPtName(arPersonelType.getPtName());
                mdetails.setPtDescription(arPersonelType.getPtDescription());
                mdetails.setPtRate(arPersonelType.getPtRate());
                mdetails.setPtAdCompany(arPersonelType.getPtAdCompany());

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

    public void addArPtEntry(ArModPersonelTypeDetails details, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException {
        Debug.print("ArPersonelTypeControllerBean addArCtEntry");
        try {

            LocalArPersonelType arPersonelType = null;
            LocalAdBankAccount adBankAccount = null;

            try {

                arPersonelType = arPersonelTypeHome.findByPtShortName(details.getPtShortName(), AD_CMPNY);
                throw new GlobalRecordAlreadyExistException(details.getPtShortName());
            } catch (FinderException ex) {

            }

            try {

                arPersonelType = arPersonelTypeHome.findByPtName(details.getPtName(), AD_CMPNY);
                throw new GlobalRecordAlreadyExistException(details.getPtName());

            } catch (FinderException ex) {

            }

            // create new supplier class

            arPersonelType = arPersonelTypeHome.create(details.getPtShortName(), details.getPtName(), details.getPtDescription(), details.getPtRate(), AD_CMPNY);

        } catch (GlobalRecordAlreadyExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateArPtEntry(ArModPersonelTypeDetails details, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException {
        Debug.print("ArPersonelTypeControllerBean updateArPtEntry");
        LocalArPersonelType arPersonelType = null;
        LocalAdBankAccount adBankAccount = null;
        try {

            try {

                LocalArPersonelType apExistingPersonelType = arPersonelTypeHome.findByPtName(details.getPtName(), AD_CMPNY);

                if (!apExistingPersonelType.getPtCode().equals(details.getPtCode())) {

                    throw new GlobalRecordAlreadyExistException();
                }

            } catch (FinderException ex) {

            }

            try {

                LocalArPersonelType apExistingPersonelType = arPersonelTypeHome.findByPtShortName(details.getPtShortName(), AD_CMPNY);

                if (!apExistingPersonelType.getPtCode().equals(details.getPtCode())) {

                    throw new GlobalRecordAlreadyExistException();
                }

            } catch (FinderException ex) {

            }

            // find and update supplier class

            arPersonelType = arPersonelTypeHome.findByPrimaryKey(details.getPtCode());

            arPersonelType.setPtShortName(details.getPtShortName());
            arPersonelType.setPtName(details.getPtName());
            arPersonelType.setPtDescription(details.getPtDescription());
            arPersonelType.setPtRate(details.getPtRate());

        } catch (GlobalRecordAlreadyExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteArPtEntry(Integer PT_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException {

        Debug.print("ArPersonelTypeControllerBean deleteArPtEntry");
        try {

            LocalArPersonelType arPersonelType = null;

            try {

                arPersonelType = arPersonelTypeHome.findByPrimaryKey(PT_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            if (!arPersonelType.getArPersonels().isEmpty()) {

                throw new GlobalRecordAlreadyAssignedException();
            }

            //		    ArPersonelType.entityRemove();
            em.remove(arPersonelType);

        } catch (GlobalRecordAlreadyDeletedException | GlobalRecordAlreadyAssignedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {
        Debug.print("ArPersonelTypeControllerBean getGlFcPrecisionUnit");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }
    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ArPersonelTypeControllerBean ejbCreate");
    }

}