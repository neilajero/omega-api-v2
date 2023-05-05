package com.ejb.txn.gl;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.exception.gl.GlTCNoTransactionCalendarFoundException;
import com.ejb.exception.gl.GlTCVNoTransactionCalendarValueFoundException;
import com.ejb.exception.gl.GlTCVTransactionCalendarValueAlreadyDeletedException;
import com.ejb.dao.gl.ILocalGlTransactionCalendarHome;
import com.ejb.dao.gl.ILocalGlTransactionCalendarValueHome;
import com.ejb.entities.gl.LocalGlTransactionCalendar;
import com.ejb.entities.gl.LocalGlTransactionCalendarValue;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.gl.GlTransactionCalendarDetails;
import com.util.gl.GlTransactionCalendarValueDetails;

@Stateless(name = "GlTransactionCalendarValueControllerEJB")
public class GlTransactionCalendarValueControllerBean extends EJBContextClass implements GlTransactionCalendarValueController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalGlTransactionCalendarHome glTransactionCalendarHome;
    @EJB
    private ILocalGlTransactionCalendarValueHome glTransactionCalendarValueHome;

    public ArrayList getGlTcAll(Integer AD_CMPNY) throws GlTCNoTransactionCalendarFoundException {

        Debug.print("GlTransactionCalendarControllerValueBean getGlTcvAll");

        ArrayList tcAllList = new ArrayList();
        Collection glTransactionCalendars = null;

        try {
            glTransactionCalendars = glTransactionCalendarHome.findTcAll(AD_CMPNY);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (glTransactionCalendars.size() == 0) throw new GlTCNoTransactionCalendarFoundException();

        for (Object transactionCalendar : glTransactionCalendars) {
            LocalGlTransactionCalendar glTransactionCalendar = (LocalGlTransactionCalendar) transactionCalendar;
            GlTransactionCalendarDetails details = new GlTransactionCalendarDetails(glTransactionCalendar.getTcName());
            tcAllList.add(details);
        }

        return tcAllList;
    }

    public GlTransactionCalendarDetails getGlTcDescriptionByGlTcName(String TC_NM, Integer AD_CMPNY) throws GlTCNoTransactionCalendarFoundException {

        Debug.print("GlTransactionCalendarControllerValueBean getGlTcDescriptionByGlTcName");

        LocalGlTransactionCalendar glTransactionCalendar = null;

        try {
            glTransactionCalendar = glTransactionCalendarHome.findByTcName(TC_NM, AD_CMPNY);
        } catch (FinderException ex) {
            throw new GlTCNoTransactionCalendarFoundException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        return new GlTransactionCalendarDetails(glTransactionCalendar.getTcName(), glTransactionCalendar.getTcDescription());

    }

    public ArrayList getGlTcvByGlTcName(String TC_NM, Integer AD_CMPNY) throws GlTCVNoTransactionCalendarValueFoundException, GlTCNoTransactionCalendarFoundException {

        Debug.print("GlTransactionCalendarControllerValueBean getGlTcvByGlTcName");

        LocalGlTransactionCalendar glTransactionCalendar = null;

        ArrayList tcvAllList = new ArrayList();
        Collection glTransactionCalendarValues = null;

        try {
            glTransactionCalendar = glTransactionCalendarHome.findByTcName(TC_NM, AD_CMPNY);
        } catch (FinderException ex) {
            throw new GlTCNoTransactionCalendarFoundException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        try {
            glTransactionCalendarValues = glTransactionCalendar.getGlTransactionCalendarValues();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (glTransactionCalendarValues.size() == 0) throw new GlTCVNoTransactionCalendarValueFoundException();

        for (Object transactionCalendarValue : glTransactionCalendarValues) {
            LocalGlTransactionCalendarValue glTransactionCalendarValue = (LocalGlTransactionCalendarValue) transactionCalendarValue;

            GlTransactionCalendarValueDetails details = new GlTransactionCalendarValueDetails(glTransactionCalendarValue.getTcvCode(), glTransactionCalendarValue.getTcvDate(), glTransactionCalendarValue.getTcvDayOfWeek(), glTransactionCalendarValue.getTcvBusinessDay());

            tcvAllList.add(details);
        }

        return tcvAllList;
    }

    public void updateGlTcvEntry(GlTransactionCalendarValueDetails details, Integer AD_CMPNY) throws GlTCVTransactionCalendarValueAlreadyDeletedException {

        Debug.print("GlTransactionCalendarValueControllerBean updateGlTcvEntry");

        LocalGlTransactionCalendarValue glTransactionCalendarValue = null;

        try {
            glTransactionCalendarValue = glTransactionCalendarValueHome.findByPrimaryKey(details.getTcvCode());
        } catch (Exception ex) {
            throw new GlTCVTransactionCalendarValueAlreadyDeletedException();
        }

        try {
            glTransactionCalendarValue.setTcvBusinessDay(details.getTcvBusinessDay());
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlTransactionCalendarControllerValueBean ejbCreate");

    }

    // private methods

    private void addGlTransactionCalendarValueToGlTransactionCalendar(Integer TCV_CODE, Integer TC_CODE, Integer AD_CMPNY) {

        Debug.print("GlTransactionCalendarValueControllerBean addGlTransactionCalendarValueToGlTransactionCalendar");

        try {
            LocalGlTransactionCalendarValue glTransactionCalendarValue = glTransactionCalendarValueHome.findByPrimaryKey(TCV_CODE);
            LocalGlTransactionCalendar glTransactionCalendar = glTransactionCalendarHome.findByPrimaryKey(TC_CODE);
            glTransactionCalendar.addGlTransactionCalendarValue(glTransactionCalendarValue);
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }
}