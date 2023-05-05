/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class GlBudgetDefinitionControllerBean
 * @created
 * @author
 **/
package com.ejb.txn.gl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.exception.gl.GlBGTFirstLastPeriodDifferentAcYearException;
import com.ejb.exception.gl.GlBGTFirstPeriodGreaterThanLastPeriodException;
import com.ejb.exception.gl.GlBGTStatusAlreadyDefinedException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.dao.gl.ILocalGlAccountingCalendarValueHome;
import com.ejb.dao.gl.ILocalGlSetOfBookHome;
import com.ejb.entities.gl.LocalGlAccountingCalendarValue;
import com.ejb.entities.gl.LocalGlBudget;
import com.ejb.dao.gl.LocalGlBudgetHome;
import com.ejb.entities.gl.LocalGlSetOfBook;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.gl.GlBudgetDetails;
import com.util.mod.gl.GlModAccountingCalendarValueDetails;

@Stateless(name = "GlBudgetDefinitionControllerEJB")
public class GlBudgetDefinitionControllerBean extends EJBContextClass implements GlBudgetDefinitionController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalGlSetOfBookHome glSetOfBookHome;
    @EJB
    private ILocalGlAccountingCalendarValueHome glAccountingCalendarValueHome;
    @EJB
    private LocalGlBudgetHome glBudgetHome;

    public ArrayList getGlAcvAll(Integer AD_CMPNY) {

        Debug.print("GlBudgetDefinitionControllerBean getGlAcvAll");

        ArrayList list = new ArrayList();

        try {

            Collection glSetOfBooks = glSetOfBookHome.findBySobYearEndClosed(EJBCommon.FALSE, AD_CMPNY);

            for (Object setOfBook : glSetOfBooks) {

                LocalGlSetOfBook glSetOfBook = (LocalGlSetOfBook) setOfBook;

                Collection glAccountingCalendarValues = glSetOfBook.getGlAccountingCalendar().getGlAccountingCalendarValues();

                for (Object accountingCalendarValue : glAccountingCalendarValues) {

                    LocalGlAccountingCalendarValue glAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                    GlModAccountingCalendarValueDetails mdetails = new GlModAccountingCalendarValueDetails();

                    mdetails.setAcvPeriodPrefix(glAccountingCalendarValue.getAcvPeriodPrefix());

                    GregorianCalendar gc = new GregorianCalendar();
                    gc.setTime(glAccountingCalendarValue.getAcvDateTo());

                    mdetails.setAcvYear(gc.get(Calendar.YEAR));

                    list.add(mdetails);

                }

            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }

    public ArrayList getGlBgtAll(Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlBudgetDefinitionControllerBean getGlBgtAll");

        ArrayList list = new ArrayList();


        try {

            Collection glBudgets = glBudgetHome.findBgtAll(AD_CMPNY);

            if (glBudgets.isEmpty()) {

                throw new GlobalNoRecordFoundException();

            }

            for (Object budget : glBudgets) {

                LocalGlBudget glBudget = (LocalGlBudget) budget;

                GlBudgetDetails details = new GlBudgetDetails();
                details.setBgtCode(glBudget.getBgtCode());
                details.setBgtName(glBudget.getBgtName());
                details.setBgtDescription(glBudget.getBgtDescription());
                details.setBgtStatus(glBudget.getBgtStatus());
                details.setBgtFirstPeriod(glBudget.getBgtFirstPeriod());
                details.setBgtLastPeriod(glBudget.getBgtLastPeriod());

                list.add(details);

            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }

    public void addGlBgtEntry(GlBudgetDetails details, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlBGTStatusAlreadyDefinedException, GlBGTFirstPeriodGreaterThanLastPeriodException, GlBGTFirstLastPeriodDifferentAcYearException {

        Debug.print("GlBudgetDefinitionControllerBean addGlBgtEntry");

        LocalGlBudget glBudget = null;


        try {

            try {

                glBudget = glBudgetHome.findByBgtName(details.getBgtName(), AD_CMPNY);

                if (glBudget != null) {

                    throw new GlobalRecordAlreadyExistException();

                }

            } catch (FinderException ex) {

            }

            try {

                glBudget = glBudgetHome.findByBgtStatus("CURRENT", AD_CMPNY);

                if (glBudget != null && details.getBgtStatus().equals("CURRENT")) {

                    throw new GlBGTStatusAlreadyDefinedException();

                }

            } catch (FinderException ex) {

            }

            // validate if first period is not greater than last period

            Collection glFirstSetOfBooks = glSetOfBookHome.findByAcvPeriodPrefixAndDate(details.getBgtFirstPeriod().substring(0, details.getBgtFirstPeriod().indexOf('-')), EJBCommon.getIntendedDate(Integer.parseInt(details.getBgtFirstPeriod().substring(details.getBgtFirstPeriod().indexOf('-') + 1))), AD_CMPNY);
            ArrayList glFirstSetOfBookList = new ArrayList(glFirstSetOfBooks);
            LocalGlSetOfBook glFirstSetOfBook = (LocalGlSetOfBook) glFirstSetOfBookList.get(0);
            LocalGlAccountingCalendarValue glFirstAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodPrefix(glFirstSetOfBook.getGlAccountingCalendar().getAcCode(), details.getBgtFirstPeriod().substring(0, details.getBgtFirstPeriod().indexOf('-')), AD_CMPNY);

            Collection glLastSetOfBooks = glSetOfBookHome.findByAcvPeriodPrefixAndDate(details.getBgtLastPeriod().substring(0, details.getBgtLastPeriod().indexOf('-')), EJBCommon.getIntendedDate(Integer.parseInt(details.getBgtLastPeriod().substring(details.getBgtLastPeriod().indexOf('-') + 1))), AD_CMPNY);
            ArrayList glLastSetOfBookList = new ArrayList(glLastSetOfBooks);
            LocalGlSetOfBook glLastSetOfBook = (LocalGlSetOfBook) glLastSetOfBookList.get(0);
            LocalGlAccountingCalendarValue glLastAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodPrefix(glLastSetOfBook.getGlAccountingCalendar().getAcCode(), details.getBgtLastPeriod().substring(0, details.getBgtLastPeriod().indexOf('-')), AD_CMPNY);

            if (glFirstAccountingCalendarValue.getAcvDateTo().after(glLastAccountingCalendarValue.getAcvDateTo())) {

                throw new GlBGTFirstPeriodGreaterThanLastPeriodException();

            }

            if (!glFirstSetOfBook.getSobCode().equals(glLastSetOfBook.getSobCode())) {

                throw new GlBGTFirstLastPeriodDifferentAcYearException();

            }

            // create new budget definition

            glBudget = glBudgetHome.create(details.getBgtName(), details.getBgtDescription(), details.getBgtStatus(), details.getBgtFirstPeriod(), details.getBgtLastPeriod(), AD_CMPNY);

        } catch (GlobalRecordAlreadyExistException | GlBGTFirstLastPeriodDifferentAcYearException |
                 GlBGTFirstPeriodGreaterThanLastPeriodException | GlBGTStatusAlreadyDefinedException ex) {

            throw ex;

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }

    public void updateGlBgtEntry(GlBudgetDetails details, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalRecordAlreadyAssignedException, GlBGTStatusAlreadyDefinedException, GlBGTFirstPeriodGreaterThanLastPeriodException, GlBGTFirstLastPeriodDifferentAcYearException {

        Debug.print("GlBudgetDefinitionControllerBean updateGlBgtEntry");

        LocalGlBudget glExistingBudget = null;

        try {

            try {

                glExistingBudget = glBudgetHome.findByBgtName(details.getBgtName(), AD_CMPNY);

                if (glExistingBudget != null && !glExistingBudget.getBgtCode().equals(details.getBgtCode())) {

                    throw new GlobalRecordAlreadyExistException();

                }

            } catch (FinderException ex) {

            }

            try {

                glExistingBudget = glBudgetHome.findByBgtStatus("CURRENT", AD_CMPNY);

                if (glExistingBudget != null && !glExistingBudget.getBgtCode().equals(details.getBgtCode()) && details.getBgtStatus().equals("CURRENT")) {

                    throw new GlBGTStatusAlreadyDefinedException();

                }

            } catch (FinderException ex) {

            }

            // validate if first period is not greater than last period

            Collection glFirstSetOfBooks = glSetOfBookHome.findByAcvPeriodPrefixAndDate(details.getBgtFirstPeriod().substring(0, details.getBgtFirstPeriod().indexOf('-')), EJBCommon.getIntendedDate(Integer.parseInt(details.getBgtFirstPeriod().substring(details.getBgtFirstPeriod().indexOf('-') + 1))), AD_CMPNY);
            ArrayList glFirstSetOfBookList = new ArrayList(glFirstSetOfBooks);
            LocalGlSetOfBook glFirstSetOfBook = (LocalGlSetOfBook) glFirstSetOfBookList.get(0);
            LocalGlAccountingCalendarValue glFirstAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodPrefix(glFirstSetOfBook.getGlAccountingCalendar().getAcCode(), details.getBgtFirstPeriod().substring(0, details.getBgtFirstPeriod().indexOf('-')), AD_CMPNY);

            Collection glLastSetOfBooks = glSetOfBookHome.findByAcvPeriodPrefixAndDate(details.getBgtLastPeriod().substring(0, details.getBgtLastPeriod().indexOf('-')), EJBCommon.getIntendedDate(Integer.parseInt(details.getBgtLastPeriod().substring(details.getBgtLastPeriod().indexOf('-') + 1))), AD_CMPNY);
            ArrayList glLastSetOfBookList = new ArrayList(glLastSetOfBooks);
            LocalGlSetOfBook glLastSetOfBook = (LocalGlSetOfBook) glLastSetOfBookList.get(0);
            LocalGlAccountingCalendarValue glLastAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodPrefix(glLastSetOfBook.getGlAccountingCalendar().getAcCode(), details.getBgtLastPeriod().substring(0, details.getBgtLastPeriod().indexOf('-')), AD_CMPNY);

            if (glFirstAccountingCalendarValue.getAcvDateTo().after(glLastAccountingCalendarValue.getAcvDateTo())) {

                throw new GlBGTFirstPeriodGreaterThanLastPeriodException();

            }

            if (!glFirstSetOfBook.getSobCode().equals(glLastSetOfBook.getSobCode())) {

                throw new GlBGTFirstLastPeriodDifferentAcYearException();

            }

            LocalGlBudget glBudget = glBudgetHome.findByPrimaryKey(details.getBgtCode());

            if (!glBudget.getGlBudgetAmounts().isEmpty() && (!glBudget.getBgtName().equals(details.getBgtName()) || !glBudget.getBgtFirstPeriod().equals(details.getBgtFirstPeriod()) || !glBudget.getBgtLastPeriod().equals(details.getBgtLastPeriod()))) {

                throw new GlobalRecordAlreadyAssignedException();
            }

            // Find and Update Budget Definition

            glBudget.setBgtName(details.getBgtName());
            glBudget.setBgtDescription(details.getBgtDescription());
            glBudget.setBgtStatus(details.getBgtStatus());
            glBudget.setBgtFirstPeriod(details.getBgtFirstPeriod());
            glBudget.setBgtLastPeriod(details.getBgtLastPeriod());

        } catch (GlobalRecordAlreadyExistException | GlBGTFirstLastPeriodDifferentAcYearException |
                 GlBGTFirstPeriodGreaterThanLastPeriodException | GlBGTStatusAlreadyDefinedException |
                 GlobalRecordAlreadyAssignedException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }

    public void deleteGlBgtEntry(Integer BGT_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException {

        Debug.print("GlFrgColumnEntryControllerBean deleteGlBgtEntry");

        LocalGlBudget glBudget = null;

        try {

            glBudget = glBudgetHome.findByPrimaryKey(BGT_CODE);

        } catch (FinderException ex) {

            throw new GlobalRecordAlreadyDeletedException();

        }

        try {

            if (!glBudget.getGlBudgetAmounts().isEmpty()) {

                throw new GlobalRecordAlreadyAssignedException();
            }

//        	glBudget.entityRemove();
            em.remove(glBudget);

        } catch (GlobalRecordAlreadyAssignedException ex) {

            throw ex;

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlBudgetDefinitionControllerBean ejbCreate");

    }

    // private methods

}