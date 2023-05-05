/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class GlAccountingCalendarControllerBean
 * @created
 * @author
 **/
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
import com.ejb.exception.gl.GlACAccountingCalendarAlreadyAssignedException;
import com.ejb.exception.gl.GlACAccountingCalendarAlreadyDeletedException;
import com.ejb.exception.gl.GlACAccountingCalendarAlreadyExistException;
import com.ejb.exception.gl.GlACNoAccountingCalendarFoundException;
import com.ejb.exception.gl.GlPTNoPeriodTypeFoundException;
import com.ejb.dao.gl.ILocalGlAccountingCalendarHome;
import com.ejb.entities.gl.LocalGlAccountingCalendar;
import com.ejb.entities.gl.LocalGlPeriodType;
import com.ejb.dao.gl.LocalGlPeriodTypeHome;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.gl.GlAccountingCalendarDetails;
import com.util.mod.gl.GlModAccountingCalendarDetails;
import com.util.gl.GlPeriodTypeDetails;

@Stateless(name = "GlAccountingCalendarControllerEJB")
public class GlAccountingCalendarControllerBean extends EJBContextClass implements GlAccountingCalendarController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalGlAccountingCalendarHome glAccountingCalendarHome;
    @EJB
    private LocalGlPeriodTypeHome glPeriodTypeHome;

    public ArrayList getGlAcAll(Integer AD_CMPNY) throws GlACNoAccountingCalendarFoundException, GlPTNoPeriodTypeFoundException {

        Debug.print("GlAccountingCalendarControllerBean getGlAcAll");

        ArrayList acAllList = new ArrayList();
        Collection glAccountingCalendars = null;

        try {
            glAccountingCalendars = glAccountingCalendarHome.findAcAll(AD_CMPNY);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (glAccountingCalendars.size() == 0) throw new GlACNoAccountingCalendarFoundException();

        for (Object accountingCalendar : glAccountingCalendars) {
            LocalGlAccountingCalendar glAccountingCalendar = (LocalGlAccountingCalendar) accountingCalendar;
            LocalGlPeriodType glPeriodType = null;

            glPeriodType = glAccountingCalendar.getGlPeriodType();

            String MAC_PT_NM = null;

            try {
                MAC_PT_NM = glPeriodType.getPtName();
            } catch (Exception ex) {
                throw new GlPTNoPeriodTypeFoundException();
            }

            GlModAccountingCalendarDetails details = new GlModAccountingCalendarDetails(glAccountingCalendar.getAcCode(), glAccountingCalendar.getAcName(), glAccountingCalendar.getAcDescription(), MAC_PT_NM);
            acAllList.add(details);
        }

        return acAllList;
    }

    public ArrayList getGlPtAll(Integer AD_CMPNY) throws GlPTNoPeriodTypeFoundException {

        Debug.print("GlAccountingCalendarControllerBean getGlPtAll");

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

    public void addGlAcEntry(GlAccountingCalendarDetails details, String AC_PT_NM, Integer AD_CMPNY) throws GlACAccountingCalendarAlreadyExistException, GlPTNoPeriodTypeFoundException {

        Debug.print("GlAccountingCalendarControllerBean addGlAcEntry");

        LocalGlAccountingCalendar glAccountingCalendar = null;
        LocalGlPeriodType glPeriodType = null;

        try {
            glPeriodType = glPeriodTypeHome.findByPtName(AC_PT_NM, AD_CMPNY);
        } catch (FinderException ex) {
            throw new GlPTNoPeriodTypeFoundException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        try {

            glAccountingCalendarHome.findByAcName(details.getAcName(), AD_CMPNY);

            getSessionContext().setRollbackOnly();
            throw new GlACAccountingCalendarAlreadyExistException();

        } catch (FinderException ex) {

        }

        try {
            glAccountingCalendar = glAccountingCalendarHome.create(details.getAcName(), details.getAcDescription(), null, AD_CMPNY);
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

        addGlAccountingCalendarToGlPeriodType(glAccountingCalendar.getAcCode(), glPeriodType.getPtCode(), AD_CMPNY);
    }

    public void updateGlAcEntry(GlAccountingCalendarDetails details, String AC_PT_NM, Integer AD_CMPNY) throws GlACAccountingCalendarAlreadyExistException, GlACAccountingCalendarAlreadyAssignedException, GlPTNoPeriodTypeFoundException, GlACAccountingCalendarAlreadyDeletedException {

        Debug.print("GlAccountingCalendarControllerBean updateGlAcEntry");

        LocalGlAccountingCalendar glAccountingCalendar = null;
        LocalGlPeriodType glPeriodType = null;

        try {
            glAccountingCalendar = glAccountingCalendarHome.findByPrimaryKey(details.getAcCode());
        } catch (FinderException ex) {
            throw new GlACAccountingCalendarAlreadyDeletedException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (hasRelation(glAccountingCalendar, AD_CMPNY) && !AC_PT_NM.equals(glAccountingCalendar.getGlPeriodType().getPtName()))
            throw new GlACAccountingCalendarAlreadyAssignedException();
        else {

            LocalGlAccountingCalendar glAccountingCalendar2 = null;

            try {
                glPeriodType = glPeriodTypeHome.findByPtName(AC_PT_NM, AD_CMPNY);
            } catch (FinderException ex) {
                throw new GlPTNoPeriodTypeFoundException();
            } catch (Exception ex) {
                throw new EJBException(ex.getMessage());
            }

            try {
                glAccountingCalendar2 = glAccountingCalendarHome.findByAcName(details.getAcName(), AD_CMPNY);
            } catch (FinderException ex) {

                glAccountingCalendar.setAcName(details.getAcName());
                glAccountingCalendar.setAcDescription(details.getAcDescription());
                addGlAccountingCalendarToGlPeriodType(details.getAcCode(), glPeriodType.getPtCode(), AD_CMPNY);

            }

            if (glAccountingCalendar2 != null && !glAccountingCalendar.getAcCode().equals(glAccountingCalendar2.getAcCode())) {

                getSessionContext().setRollbackOnly();
                throw new GlACAccountingCalendarAlreadyExistException();

            } else {
                if (glAccountingCalendar2 != null && glAccountingCalendar.getAcCode().equals(glAccountingCalendar2.getAcCode())) {

                    glAccountingCalendar.setAcName(details.getAcName());
                    glAccountingCalendar.setAcDescription(details.getAcDescription());
                    addGlAccountingCalendarToGlPeriodType(details.getAcCode(), glPeriodType.getPtCode(), AD_CMPNY);

                }
            }
        }
    }

    public void deleteGlAcEntry(Integer AC_CODE, Integer AD_CMPNY) throws GlACAccountingCalendarAlreadyAssignedException, GlACAccountingCalendarAlreadyDeletedException {

        Debug.print("GlAccountingCalendarControllerBean deleteGlAcEntry");

        LocalGlAccountingCalendar glAccountingCalendar;

        try {
            glAccountingCalendar = glAccountingCalendarHome.findByPrimaryKey(AC_CODE);
        } catch (FinderException ex) {
            throw new GlACAccountingCalendarAlreadyDeletedException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (hasRelation(glAccountingCalendar, AD_CMPNY)) throw new GlACAccountingCalendarAlreadyAssignedException();
        else {
            try {
//				glAccountingCalendar.entityRemove();
                em.remove(glAccountingCalendar);
            } catch (Exception ex) {
                getSessionContext().setRollbackOnly();
                throw new EJBException(ex.getMessage());
            }
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlAccountingCalendarControllerBean ejbCreate");

    }

    // private methods

    private void addGlAccountingCalendarToGlPeriodType(Integer AC_CODE, Integer PT_CODE, Integer AD_CMPNY) {

        Debug.print("GlAccountingCalendarControllerBean addGlAccountingCalendarToGlPeriodType");

        try {
            LocalGlAccountingCalendar glAccountingCalendar = glAccountingCalendarHome.findByPrimaryKey(AC_CODE);
            LocalGlPeriodType glPeriodType = glPeriodTypeHome.findByPrimaryKey(PT_CODE);
            glPeriodType.addGlAccountingCalendar(glAccountingCalendar);
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private boolean hasRelation(LocalGlAccountingCalendar glAccountingCalendar, Integer AD_CMPNY) {

        Debug.print("GlAccountingCalendarControllerBean hasRelation");

        Collection glSetOfBooks = null;

        try {
            glSetOfBooks = glAccountingCalendar.getGlSetOfBooks();
        } catch (Exception ex) {
        }

        return glSetOfBooks.size() != 0;

    }

}