package com.ejb.txn.gl;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;


import java.util.ArrayList;
import java.util.Collection;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import javax.naming.NamingException;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.entities.gl.LocalGlFrgColumnSet;
import com.ejb.dao.gl.LocalGlFrgColumnSetHome;
import com.util.EJBContextClass;
import com.util.Debug;
import com.util.EJBHomeFactory;
import com.util.gl.GlFrgColumnSetDetails;

@Stateless(name = "GlFrgColumnSetControllerEJB")
public class GlFrgColumnSetControllerBean extends EJBContextClass implements GlFrgColumnSetController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalGlFrgColumnSetHome glFrgColumnSetHome;

    public ArrayList getGlFrgCsAll(Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlFrgColumnSetControllerBean getGlFrgCsAll");

        Collection glFrgColumns = null;

        LocalGlFrgColumnSet glFrgColumnSet = null;

        ArrayList list = new ArrayList();

        try {

            glFrgColumns = glFrgColumnSetHome.findCsAll(AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (glFrgColumns.isEmpty()) {

            throw new GlobalNoRecordFoundException();

        }

        for (Object glFrgColumn : glFrgColumns) {

            glFrgColumnSet = (LocalGlFrgColumnSet) glFrgColumn;

            GlFrgColumnSetDetails mdetails = new GlFrgColumnSetDetails();
            mdetails.setCsCode(glFrgColumnSet.getCsCode());
            mdetails.setCsName(glFrgColumnSet.getCsName());
            mdetails.setCsDescription(glFrgColumnSet.getCsDescription());

            list.add(mdetails);
        }

        return list;

    }


    public void addGlFrgCsEntry(GlFrgColumnSetDetails details, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException {

        Debug.print("GlFrgColumnSetControllerBean addGlFrgCsEntry");

        LocalGlFrgColumnSet glFrgColumnSet = null;

        ArrayList list = new ArrayList();

        try {

            glFrgColumnSet = glFrgColumnSetHome.findByCsName(details.getCsName(), AD_CMPNY);

            throw new GlobalRecordAlreadyExistException();

        } catch (GlobalRecordAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

        try {

            // create new set of columns

            glFrgColumnSet = glFrgColumnSetHome.create(details.getCsName(), details.getCsDescription(), AD_CMPNY);

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }


    public void updateGlFrgCsEntry(GlFrgColumnSetDetails details, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException {

        Debug.print("GlFrgColumnSetControllerBean updateGlFrgFrEntry");

        LocalGlFrgColumnSet glFrgColumnSet = null;

        ArrayList list = new ArrayList();

        try {

            LocalGlFrgColumnSet glExistingColumnSet = glFrgColumnSetHome.findByCsName(details.getCsName(), AD_CMPNY);

            if (!glExistingColumnSet.getCsCode().equals(details.getCsCode())) {

                throw new GlobalRecordAlreadyExistException();

            }

        } catch (GlobalRecordAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

        try {

            // find and update column set

            glFrgColumnSet = glFrgColumnSetHome.findByPrimaryKey(details.getCsCode());

            glFrgColumnSet.setCsName(details.getCsName());
            glFrgColumnSet.setCsDescription(details.getCsDescription());

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

    }


    public void deleteGlFrgCsEntry(Integer CS_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyAssignedException, GlobalRecordAlreadyDeletedException {

        Debug.print("GlFrgColumnSetControllerBean deleteGlFrgCsEntry");

        LocalGlFrgColumnSet glFrgColumnSet = null;

        try {

            glFrgColumnSet = glFrgColumnSetHome.findByPrimaryKey(CS_CODE);

        } catch (FinderException ex) {

            throw new GlobalRecordAlreadyDeletedException();

        }

        try {

            if (!glFrgColumnSet.getGlFrgFinancialReports().isEmpty()) {

                throw new GlobalRecordAlreadyAssignedException();

            }

            em.remove(glFrgColumnSet);

        } catch (GlobalRecordAlreadyAssignedException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

    }


    // SessionBean methods


    public void ejbCreate() throws CreateException {

        Debug.print("GlFrgColumnSetControllerBean ejbCreate");

    }
}