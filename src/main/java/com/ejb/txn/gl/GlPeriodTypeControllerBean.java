package com.ejb.txn.gl;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.exception.gl.GlPTNoPeriodTypeFoundException;
import com.ejb.exception.gl.GlPTPeriodTypeAlreadyAssignedException;
import com.ejb.exception.gl.GlPTPeriodTypeAlreadyDeletedException;
import com.ejb.exception.gl.GlPTPeriodTypeAlreadyExistException;
import com.ejb.dao.gl.ILocalGlAccountingCalendarHome;
import com.ejb.entities.gl.LocalGlPeriodType;
import com.ejb.dao.gl.LocalGlPeriodTypeHome;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.gl.GlPeriodTypeDetails;

@Stateless(name = "GlPeriodTypeControllerEJB")
public class GlPeriodTypeControllerBean extends EJBContextClass implements GlPeriodTypeController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalGlAccountingCalendarHome glAccountingCalendarHome;
    @EJB
    private LocalGlPeriodTypeHome glPeriodTypeHome;

    public ArrayList getGlPtAll(Integer AD_CMPNY) throws GlPTNoPeriodTypeFoundException {

        Debug.print("GlPeriodTypeControllerBean getGlPtAll");

        ArrayList ptAllList = new ArrayList();
        Collection glPeriodTypes = null;

        try {
            glPeriodTypes = glPeriodTypeHome.findPtAll(AD_CMPNY);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (glPeriodTypes.size() == 0) throw new GlPTNoPeriodTypeFoundException();

        for (Object periodType : glPeriodTypes) {
            LocalGlPeriodType glPeriodType = (LocalGlPeriodType) periodType;
            GlPeriodTypeDetails details = new GlPeriodTypeDetails(glPeriodType.getPtCode(), glPeriodType.getPtName(), glPeriodType.getPtDescription(), glPeriodType.getPtPeriodPerYear(), glPeriodType.getPtYearType());
            ptAllList.add(details);
        }

        return ptAllList;
    }

    public void addGlPtEntry(GlPeriodTypeDetails details, Integer AD_CMPNY) throws GlPTPeriodTypeAlreadyExistException {

        Debug.print("GlPeriodTypeControllerBean addGlPtEntry");

        try {

            glPeriodTypeHome.findByPtName(details.getPtName(), AD_CMPNY);

            getSessionContext().setRollbackOnly();
            throw new GlPTPeriodTypeAlreadyExistException();

        } catch (FinderException ex) {

        }

        try {
            LocalGlPeriodType glPeriodType = glPeriodTypeHome.create(details.getPtName(), details.getPtDescription(), details.getPtPeriodPerYear(), details.getPtYearType(), AD_CMPNY);
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateGlPtEntry(GlPeriodTypeDetails details, Integer AD_CMPNY) throws GlPTPeriodTypeAlreadyExistException, GlPTPeriodTypeAlreadyAssignedException, GlPTPeriodTypeAlreadyDeletedException {

        Debug.print("GlPeriodTypeControllerBean updateGlPtEntry");

        LocalGlPeriodType glPeriodType = null;

        try {
            glPeriodType = glPeriodTypeHome.findByPrimaryKey(details.getPtCode());
        } catch (FinderException ex) {
            throw new GlPTPeriodTypeAlreadyDeletedException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (hasRelation(glPeriodType, AD_CMPNY)) throw new GlPTPeriodTypeAlreadyAssignedException();
        else {

            LocalGlPeriodType glPeriodType2 = null;

            try {
                glPeriodType2 = glPeriodTypeHome.findByPtName(details.getPtName(), AD_CMPNY);
            } catch (FinderException ex) {
                glPeriodType.setPtName(details.getPtName());
                glPeriodType.setPtDescription(details.getPtDescription());
                glPeriodType.setPtPeriodPerYear(details.getPtPeriodPerYear());
                glPeriodType.setPtYearType(details.getPtYearType());
            } catch (Exception ex) {
                throw new EJBException(ex.getMessage());
            }

            if (glPeriodType2 != null && !glPeriodType.getPtCode().equals(glPeriodType2.getPtCode())) {
                getSessionContext().setRollbackOnly();
                throw new GlPTPeriodTypeAlreadyExistException();
            } else if (glPeriodType2 != null && glPeriodType.getPtCode().equals(glPeriodType2.getPtCode())) {
                glPeriodType.setPtDescription(details.getPtDescription());
                glPeriodType.setPtPeriodPerYear(details.getPtPeriodPerYear());
                glPeriodType.setPtYearType(details.getPtYearType());
            }
        }
    }

    public void deleteGlPtEntry(Integer PT_CODE, Integer AD_CMPNY) throws GlPTPeriodTypeAlreadyDeletedException, GlPTPeriodTypeAlreadyAssignedException {

        Debug.print("GlPeriodTypeControllerBean deleteGlPtEntry");

        LocalGlPeriodType glPeriodType = null;

        try {
            glPeriodType = glPeriodTypeHome.findByPrimaryKey(PT_CODE);
        } catch (FinderException ex) {
            throw new GlPTPeriodTypeAlreadyDeletedException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (hasRelation(glPeriodType, AD_CMPNY)) {
            throw new GlPTPeriodTypeAlreadyAssignedException();
        }else {
            try {

                em.remove(glPeriodType);
            } catch (Exception ex) {
                getSessionContext().setRollbackOnly();
                throw new EJBException(ex.getMessage());
            }
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlPeriodTypeControllerBean ejbCreate");

    }

    // private methods

    private boolean hasRelation(LocalGlPeriodType glPeriodType, Integer AD_CMPNY) {

        Debug.print("GlPeriodTypeControllerBean hasRelation");

        return !glPeriodType.getGlAccountingCalendars().isEmpty();

    }

}