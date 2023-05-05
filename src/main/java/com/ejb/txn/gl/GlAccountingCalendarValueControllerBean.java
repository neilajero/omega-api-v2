/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class GlAccountingCalendarValueControllerBean
 * @created
 * @author
 */
package com.ejb.txn.gl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ap.ILocalApSupplierHome;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.exception.gl.GlACAccountingCalendarAlreadyAssignedException;
import com.ejb.exception.gl.GlACNoAccountingCalendarFoundException;
import com.ejb.exception.gl.GlACVDateIsNotSequentialOrHasGapException;
import com.ejb.exception.gl.GlACVLastPeriodIncorrectException;
import com.ejb.exception.gl.GlACVNoAccountingCalendarValueFoundException;
import com.ejb.exception.gl.GlACVNotTotalToOneYearException;
import com.ejb.exception.gl.GlACVPeriodNumberNotUniqueException;
import com.ejb.exception.gl.GlACVPeriodOverlappedException;
import com.ejb.exception.gl.GlACVPeriodPrefixNotUniqueException;
import com.ejb.exception.gl.GlACVQuarterIsNotSequentialOrHasGapException;
import com.ejb.exception.gl.GlPTNoPeriodTypeFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.exception.global.GlobalRecordInvalidException;
import com.ejb.dao.gl.ILocalGlAccountingCalendarHome;
import com.ejb.dao.gl.ILocalGlAccountingCalendarValueHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountBalanceHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.dao.gl.ILocalGlInvestorAccountBalanceHome;
import com.ejb.dao.gl.ILocalGlSetOfBookHome;
import com.ejb.dao.gl.ILocalGlTransactionCalendarHome;
import com.ejb.dao.gl.ILocalGlTransactionCalendarValueHome;
import com.ejb.entities.gl.LocalGlAccountingCalendar;
import com.ejb.entities.gl.LocalGlAccountingCalendarValue;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.gl.LocalGlChartOfAccountBalance;
import com.ejb.entities.gl.LocalGlInvestorAccountBalance;
import com.ejb.entities.gl.LocalGlPeriodType;
import com.ejb.entities.gl.LocalGlSetOfBook;
import com.ejb.entities.gl.LocalGlTransactionCalendar;
import com.ejb.entities.gl.LocalGlTransactionCalendarValue;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.gl.GlAccountingCalendarDetails;
import com.util.gl.GlAccountingCalendarValueDetails;
import com.util.mod.gl.GlModAccountingCalendarDetails;

@Stateless(name = "GlAccountingCalendarValueControllerEJB")
public class GlAccountingCalendarValueControllerBean extends EJBContextClass implements GlAccountingCalendarValueController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private ILocalGlAccountingCalendarHome glAccountingCalendarHome;
    @EJB
    private ILocalGlAccountingCalendarValueHome glAccountingCalendarValueHome;
    @EJB
    private ILocalGlTransactionCalendarHome glTransactionCalendarHome;
    @EJB
    private ILocalGlTransactionCalendarValueHome glTransactionCalendarValueHome;
    @EJB
    private ILocalGlSetOfBookHome glSetOfBookHome;
    @EJB
    private ILocalGlChartOfAccountBalanceHome glChartOfAccountBalanceHome;
    @EJB
    private ILocalGlInvestorAccountBalanceHome glInvestorAccountBalanceHome;
    @EJB
    private ILocalApSupplierHome apSupplierHome;

    public ArrayList getGlAcAll(Integer AD_CMPNY) throws GlACNoAccountingCalendarFoundException {

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

            GlAccountingCalendarDetails details = new GlAccountingCalendarDetails(glAccountingCalendar.getAcName());
            acAllList.add(details);
        }

        return acAllList;
    }

    public GlModAccountingCalendarDetails getGlPtNameAndAcDescriptionByGlAcName(String AC_NM, Integer AD_CMPNY) throws GlACNoAccountingCalendarFoundException, GlPTNoPeriodTypeFoundException {

        Debug.print("GlAccountingCalendarValueControllerBean getGlPtNameAndAcDescriptionByGlAcName");

        LocalGlAccountingCalendar glAccountingCalendar = null;
        LocalGlPeriodType glPeriodType = null;

        try {
            glAccountingCalendar = glAccountingCalendarHome.findByAcName(AC_NM, AD_CMPNY);
        } catch (FinderException ex) {
            throw new GlACNoAccountingCalendarFoundException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        try {
            glPeriodType = glAccountingCalendar.getGlPeriodType();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (glPeriodType == null) {
            throw new GlPTNoPeriodTypeFoundException();
        }

        return new GlModAccountingCalendarDetails(glAccountingCalendar.getAcDescription(), glPeriodType.getPtName());
    }

    public ArrayList getGlAcvByGlAcName(String AC_NM, Integer AD_CMPNY) throws GlACVNoAccountingCalendarValueFoundException, GlACNoAccountingCalendarFoundException {

        Debug.print("GlAccountingCalendarValueControllerBean getGlAcvByAcName");

        LocalGlAccountingCalendar glAccountingCalendar = null;

        ArrayList acvAllList = new ArrayList();
        Collection glAccountingCalendarValues = null;
        Collection glSetOfBooks = null;

        try {

            glAccountingCalendar = glAccountingCalendarHome.findByAcName(AC_NM, AD_CMPNY);

        } catch (FinderException ex) {

            throw new GlACNoAccountingCalendarFoundException();

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            glAccountingCalendarValues = glAccountingCalendar.getGlAccountingCalendarValues();

            glSetOfBooks = glSetOfBookHome.findSobAll(AD_CMPNY);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (glAccountingCalendarValues.isEmpty() && glSetOfBooks.isEmpty())
            throw new GlACVNoAccountingCalendarValueFoundException();

        try {

            if (!glAccountingCalendarValues.isEmpty()) {

                for (Object accountingCalendarValue : glAccountingCalendarValues) {

                    LocalGlAccountingCalendarValue glAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                    GlAccountingCalendarValueDetails details = new GlAccountingCalendarValueDetails(glAccountingCalendarValue.getAcvCode(), glAccountingCalendarValue.getAcvPeriodPrefix(), glAccountingCalendarValue.getAcvQuarter(), glAccountingCalendarValue.getAcvPeriodNumber(), glAccountingCalendarValue.getAcvDateFrom(), glAccountingCalendarValue.getAcvDateTo(), glAccountingCalendarValue.getAcvStatus(), glAccountingCalendarValue.getAcvDateOpened(), glAccountingCalendarValue.getAcvDateClosed(), glAccountingCalendarValue.getAcvDatePermanentlyClosed(), glAccountingCalendarValue.getAcvDateFutureEntered());

                    acvAllList.add(details);
                }

            } else if (!glSetOfBooks.isEmpty()) {

                ArrayList glSetOfBookList = new ArrayList(glSetOfBooks);

                LocalGlSetOfBook glSetOfBook = (LocalGlSetOfBook) glSetOfBookList.get(glSetOfBookList.size() - 1);

                Collection glPreviousAccountingCalendarValues = glSetOfBook.getGlAccountingCalendar().getGlAccountingCalendarValues();

                for (Object glPreviousAccountingCalendarValue : glPreviousAccountingCalendarValues) {

                    LocalGlAccountingCalendarValue glAccountingCalendarValue = (LocalGlAccountingCalendarValue) glPreviousAccountingCalendarValue;

                    GregorianCalendar gcFrom = new GregorianCalendar();
                    GregorianCalendar gcTo = new GregorianCalendar();

                    gcFrom.setTime(glAccountingCalendarValue.getAcvDateFrom());
                    gcTo.setTime(glAccountingCalendarValue.getAcvDateTo());

                    gcFrom.add(Calendar.YEAR, 1);
                    gcTo.add(Calendar.YEAR, 1);

                    if (gcTo.isLeapYear(gcTo.get(Calendar.YEAR)) && gcTo.get(Calendar.MONTH) == 1 && gcTo.get(Calendar.DATE) == 28) {

                        gcTo.add(Calendar.DATE, 1);

                    } else if (!gcTo.isLeapYear(gcTo.get(Calendar.YEAR)) && gcTo.get(Calendar.MONTH) == 2 && gcTo.get(Calendar.DATE) == 1) {

                        gcTo.add(Calendar.DATE, -1);
                    }

                    GlAccountingCalendarValueDetails details = new GlAccountingCalendarValueDetails(null, glAccountingCalendarValue.getAcvPeriodPrefix(), glAccountingCalendarValue.getAcvQuarter(), glAccountingCalendarValue.getAcvPeriodNumber(), gcFrom.getTime(), gcTo.getTime(), 'N', null, null, null, null);

                    acvAllList.add(details);
                }
            }

            return acvAllList;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void saveGlAcvEntry(ArrayList acvList, String AC_NM, Integer AD_CMPNY) throws GlACAccountingCalendarAlreadyAssignedException, GlACNoAccountingCalendarFoundException, GlACVPeriodPrefixNotUniqueException, GlACVPeriodNumberNotUniqueException, GlACVPeriodOverlappedException, GlACVLastPeriodIncorrectException, GlACVQuarterIsNotSequentialOrHasGapException, GlACVDateIsNotSequentialOrHasGapException, GlACVNotTotalToOneYearException, GlobalRecordInvalidException, GlobalRecordAlreadyExistException {

        Debug.print("GlAccountingCalendarValueControllerBean saveGlAcvEntry");

        LocalGlAccountingCalendar glAccountingCalendar = null;
        LocalGlAccountingCalendarValue glFirstAccountingCalendarValue = null;
        LocalGlAccountingCalendarValue glLastAccountingCalendarValue = null;

        try {

            glAccountingCalendar = glAccountingCalendarHome.findByAcName(AC_NM, AD_CMPNY);

        } catch (FinderException ex) {

            throw new GlACNoAccountingCalendarFoundException();

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (hasRelation(glAccountingCalendar, AD_CMPNY)) throw new GlACAccountingCalendarAlreadyAssignedException();
        else {

            ArrayList acvCodeList = new ArrayList();
            Collection glAccountingCalendarValues = null;

            try {

                glAccountingCalendarValues = glAccountingCalendar.getGlAccountingCalendarValues();

            } catch (Exception ex) {

                throw new EJBException(ex.getMessage());
            }

            if (glAccountingCalendarValues.size() != 0) {

                Iterator i = glAccountingCalendarValues.iterator();

                while (i.hasNext()) {

                    Integer ACV_CODE = null;
                    LocalGlAccountingCalendarValue glAccountingCalendarValue = (LocalGlAccountingCalendarValue) i.next();
                    try {

                        ACV_CODE = glAccountingCalendarValue.getAcvCode();

                    } catch (Exception ex) {

                        throw new EJBException(ex.getMessage());
                    }
                    acvCodeList.add(ACV_CODE);
                }

                i = acvCodeList.iterator();
                while (i.hasNext()) {

                    Integer ACV_CODE = (Integer) i.next();
                    try {

                        LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByPrimaryKey(ACV_CODE);
                        // glAccountingCalendarValue.entityRemove();
                        em.remove(glAccountingCalendarValue);

                    } catch (FinderException ex) {

                        getSessionContext().setRollbackOnly();
                        throw new GlACNoAccountingCalendarFoundException();

                    } catch (Exception ex) {

                        getSessionContext().setRollbackOnly();
                        throw new EJBException(ex.getMessage());

                    }
                }
            }

            Iterator i = acvList.iterator();
            while (i.hasNext()) {

                GlAccountingCalendarValueDetails details = (GlAccountingCalendarValueDetails) i.next();

                if (!isPeriodPrefixUnique(details, glAccountingCalendar, AD_CMPNY)) {

                    Debug.print("Period Prefix Not Unique ROLLBACK");
                    getSessionContext().setRollbackOnly();
                    throw new GlACVPeriodPrefixNotUniqueException();

                } else if (!isPeriodNumberUnique(details, glAccountingCalendar, AD_CMPNY)) {

                    Debug.print("Period Number Not Unique ROLLBACK");
                    getSessionContext().setRollbackOnly();
                    throw new GlACVPeriodNumberNotUniqueException();

                } else if (!isPeriodDateCoveredNotOverlapped(details, glAccountingCalendar, AD_CMPNY)) {

                    Debug.print("Period Overlapped ROLLBACK");
                    getSessionContext().setRollbackOnly();
                    throw new GlACVPeriodOverlappedException();

                } else if (!i.hasNext() && !isLastPeriodEqualToPeriodTypeLastPeriod(details, glAccountingCalendar, AD_CMPNY)) {

                    Debug.print("Last period not equal to PTs specified last ROLLBACK");
                    getSessionContext().setRollbackOnly();
                    throw new GlACVLastPeriodIncorrectException();

                } else if (!i.hasNext() && !isQuarterSequentialAndHasNoGap(details, glAccountingCalendar, AD_CMPNY)) {

                    Debug.print("Quarter is not sequential and/or has gap ROLLBACK");
                    getSessionContext().setRollbackOnly();
                    throw new GlACVQuarterIsNotSequentialOrHasGapException();

                } else if (!i.hasNext() && !isDateSequentialAndHasNoGap(details, glAccountingCalendar, AD_CMPNY)) {

                    Debug.print("Dates is not sequential and/or has gap ROLLBACK");
                    getSessionContext().setRollbackOnly();
                    throw new GlACVDateIsNotSequentialOrHasGapException();

                } else if (!i.hasNext() && !isAccountingCalendarOneYear(details, glAccountingCalendar, AD_CMPNY)) {

                    Debug.print("Calendar is not equal to one year ROLLBACK");
                    getSessionContext().setRollbackOnly();
                    throw new GlACVNotTotalToOneYearException();

                } else {

                    if (!i.hasNext()) {

                        GregorianCalendar gc = new GregorianCalendar();
                        gc.setTime(details.getAcvDateTo());

                        Collection glSetOfBooks = null;

                        try {

                            glSetOfBooks = glSetOfBookHome.findBySobYearEndClosedAndGreaterThanAcYear(EJBCommon.TRUE, gc.get(Calendar.YEAR), AD_CMPNY);

                        } catch (FinderException ex) {

                        }

                        if (!glSetOfBooks.isEmpty()) {
                            getSessionContext().setRollbackOnly();
                            throw new GlobalRecordInvalidException();
                        }
                    }

                    try {

                        LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.create(details.getAcvPeriodPrefix(), details.getAcvQuarter(), details.getAcvPeriodNumber(), details.getAcvDateFrom(), details.getAcvDateTo(), 'N', null, null, null, null, AD_CMPNY);

                        addGlAccountingCalendarValueToGlAccountingCalendar(glAccountingCalendarValue.getAcvCode(), glAccountingCalendar.getAcCode(), AD_CMPNY);

                        // check if first to be used in tc generation

                        if (glAccountingCalendarValue.getAcvPeriodNumber() == 1) {

                            glFirstAccountingCalendarValue = glAccountingCalendarValue;
                        }

                        // check if last to be used in tc generation

                        if (!i.hasNext()) {

                            glLastAccountingCalendarValue = glAccountingCalendarValue;

                            GregorianCalendar gcAcvDateTo = new GregorianCalendar();
                            gcAcvDateTo.setTime(glLastAccountingCalendarValue.getAcvDateTo());

                            try {

                                glAccountingCalendarHome.findByAcYear(gcAcvDateTo.get(Calendar.YEAR), AD_CMPNY);

                                throw new GlobalRecordAlreadyExistException();

                            } catch (FinderException ex) {

                            }

                            glAccountingCalendar.setAcYear(gcAcvDateTo.get(Calendar.YEAR));
                        }

                    } catch (GlobalRecordAlreadyExistException ex) {

                        getSessionContext().setRollbackOnly();
                        throw ex;

                    } catch (Exception ex) {

                        Debug.printStackTrace(ex);
                        getSessionContext().setRollbackOnly();
                        throw new EJBException(ex.getMessage());
                    }
                }
            }

            try {

                // generate transaction calendar

                LocalGlTransactionCalendar glTransactionCalendar = glTransactionCalendarHome.create(glAccountingCalendar.getAcName(), glAccountingCalendar.getAcDescription(), AD_CMPNY);

                GregorianCalendar gc = new GregorianCalendar();
                gc.setTime(glFirstAccountingCalendarValue.getAcvDateFrom());

                while (!gc.getTime().after(glLastAccountingCalendarValue.getAcvDateTo())) {

                    LocalGlTransactionCalendarValue glTransactionCalendarValue = glTransactionCalendarValueHome.create(gc.getTime(), (short) gc.get(Calendar.DAY_OF_WEEK), EJBCommon.TRUE, AD_CMPNY);

                    // glTransactionCalendar.addGlTransactionCalendarValue(glTransactionCalendarValue);
                    glTransactionCalendarValue.setGlTransactionCalendar(glTransactionCalendar);
                    gc.add(Calendar.DATE, 1);
                }

                // generate set of book

                LocalGlSetOfBook glSetOfBook = glSetOfBookHome.create(EJBCommon.FALSE, AD_CMPNY);

                // glAccountingCalendar.addGlSetOfBook(glSetOfBook);
                glSetOfBook.setGlAccountingCalendar(glAccountingCalendar);
                // glTransactionCalendar.addGlSetOfBook(glSetOfBook);
                glSetOfBook.setGlTransactionCalendar(glTransactionCalendar);

                // generate coa balances

                Collection glChartOfAccounts = glChartOfAccountHome.findCoaAll(AD_CMPNY);

                glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(glAccountingCalendar.getAcCode(), AD_CMPNY);

                for (Object chartOfAccount : glChartOfAccounts) {

                    LocalGlChartOfAccount glChartOfAccount = (LocalGlChartOfAccount) chartOfAccount;

                    for (Object accountingCalendarValue : glAccountingCalendarValues) {

                        LocalGlAccountingCalendarValue glAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                        LocalGlChartOfAccountBalance glChartOfAccountBalance = glChartOfAccountBalanceHome.create(0d, 0d, 0d, 0d, AD_CMPNY);
                        // glAccountingCalendarValue.addGlChartOfAccountBalance(glChartOfAccountBalance);
                        glChartOfAccountBalance.setGlAccountingCalendarValue(glAccountingCalendarValue);
                        // glChartOfAccount.addGlChartOfAccountBalance(glChartOfAccountBalance);
                        glChartOfAccountBalance.setGlChartOfAccount(glChartOfAccount);
                    }
                }

                // generate investor balances

                Collection glInvestors = apSupplierHome.findAllSplInvestor(AD_CMPNY);
                glAccountingCalendarValues = glAccountingCalendar.getGlAccountingCalendarValues();

                for (Object glInvestor : glInvestors) {

                    LocalApSupplier apSupplier = (LocalApSupplier) glInvestor;

                    for (Object accountingCalendarValue : glAccountingCalendarValues) {

                        LocalGlAccountingCalendarValue glAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                        LocalGlInvestorAccountBalance glInvestorAccountBalance = glInvestorAccountBalanceHome.create(0d, 0d, (byte) 0, (byte) 0, 0d, 0d, 0d, 0d, 0d, 0d, AD_CMPNY);

                        glAccountingCalendarValue.addGlInvestorAccountBalance(glInvestorAccountBalance);
                        apSupplier.addGlInvestorAccountBalance(glInvestorAccountBalance);
                    }
                }

            } catch (Exception ex) {

                Debug.printStackTrace(ex);
                getSessionContext().setRollbackOnly();
                throw new EJBException(ex.getMessage());
            }
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlAccountingCalendarValueControllerBean ejbCreate");
    }

    // private methods

    private void addGlAccountingCalendarValueToGlAccountingCalendar(Integer ACV_CODE, Integer AC_CODE, Integer AD_CMPNY) {

        Debug.print("GlAccountingCalendarValueControllerBean addGlAccountingCalendarValueToGlAccountingCalendar");

        try {
            LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByPrimaryKey(ACV_CODE);
            LocalGlAccountingCalendar glAccountingCalendar = glAccountingCalendarHome.findByPrimaryKey(AC_CODE);
            // glAccountingCalendar.addGlAccountingCalendarValue(glAccountingCalendarValue);
            glAccountingCalendarValue.setGlAccountingCalendar(glAccountingCalendar);
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private boolean hasRelation(LocalGlAccountingCalendar glAccountingCalendar, Integer AD_CMPNY) {

        Debug.print("GlAccountingCalendarValueControllerBean hasRelation");

        Collection glSetOfBooks = null;

        try {
            glSetOfBooks = glAccountingCalendar.getGlSetOfBooks();
        } catch (Exception ex) {
        }

        return glSetOfBooks.size() != 0;
    }

    private boolean isPeriodPrefixUnique(GlAccountingCalendarValueDetails details, LocalGlAccountingCalendar glAccountingCalendar, Integer AD_CMPNY) {

        Debug.print("GlAccountingCalendarValueControllerBean isPeriodPrefixUnique");

        try {
            LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodPrefix(glAccountingCalendar.getAcCode(), details.getAcvPeriodPrefix(), AD_CMPNY);
            return false;
        } catch (Exception ex) {
            return true;
        }
    }

    private boolean isPeriodNumberUnique(GlAccountingCalendarValueDetails details, LocalGlAccountingCalendar glAccountingCalendar, Integer AD_CMPNY) {

        Debug.print("GlAccountingCalendarValueControllerBean isPeriodNumberUnique");

        try {
            LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodNumber(glAccountingCalendar.getAcCode(), details.getAcvPeriodNumber(), AD_CMPNY);
            return false;
        } catch (FinderException ex) {
            return true;
        }
    }

    private boolean isPeriodDateCoveredNotOverlapped(GlAccountingCalendarValueDetails details, LocalGlAccountingCalendar glAccountingCalendar, Integer AD_CMPNY) {

        Debug.print("GlAccountingCalendarValueControllerBean isPeriodDateCoveredNotOverlapped");

        Collection glAccountingCalendarValues = null;

        try {
            glAccountingCalendarValues = glAccountingCalendar.getGlAccountingCalendarValues();
        } catch (Exception ex) {
        }

        for (Object accountingCalendarValue : glAccountingCalendarValues) {
            LocalGlAccountingCalendarValue glAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

            if (details.getAcvDateFrom().getTime() >= glAccountingCalendarValue.getAcvDateFrom().getTime() && details.getAcvDateFrom().getTime() <= glAccountingCalendarValue.getAcvDateTo().getTime() || details.getAcvDateTo().getTime() >= glAccountingCalendarValue.getAcvDateFrom().getTime() && details.getAcvDateTo().getTime() <= glAccountingCalendarValue.getAcvDateTo().getTime())
                return false;
        }

        return true;
    }

    private boolean isLastPeriodEqualToPeriodTypeLastPeriod(GlAccountingCalendarValueDetails details, LocalGlAccountingCalendar glAccountingCalendar, Integer AD_CMPNY) {

        Debug.print("GlAccountingCalendarValueControllerBean lastPeriodCheck");

        Collection glAccountingCalendarValues = null;
        LocalGlPeriodType glPeriodType = null;

        try {
            glPeriodType = glAccountingCalendar.getGlPeriodType();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        try {
            glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCodeAndPtPeriodPerYear(glAccountingCalendar.getAcCode(), glPeriodType.getPtPeriodPerYear(), AD_CMPNY);
        } catch (Exception ex) {
        }

        return glAccountingCalendarValues.size() == (glPeriodType.getPtPeriodPerYear() - 1) && details.getAcvPeriodNumber() > 0 && details.getAcvPeriodNumber() <= glPeriodType.getPtPeriodPerYear();
    }

    private boolean isQuarterSequentialAndHasNoGap(GlAccountingCalendarValueDetails details, LocalGlAccountingCalendar glAccountingCalendar, Integer AD_CMPNY) {

        Debug.print("GlAccountingCalendarValueControllerBean isQuarterSequentialAndHasNoGap");

        LocalGlPeriodType glPeriodType = null;

        try {
            glPeriodType = glAccountingCalendar.getGlPeriodType();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        LocalGlAccountingCalendarValue glAccountingCalendarValue = null;
        LocalGlAccountingCalendarValue glAccountingCalendarValueNext = null;

        for (short i = 1; i < glPeriodType.getPtPeriodPerYear() - 1; i++) {

            try {
                glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodNumber(glAccountingCalendar.getAcCode(), i, AD_CMPNY);

                glAccountingCalendarValueNext = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodNumber(glAccountingCalendar.getAcCode(), (short) (i + 1), AD_CMPNY);
            } catch (Exception ex) {
                throw new EJBException();
            }

            if (i == 1 && glAccountingCalendarValue.getAcvQuarter() != 1) return false;

            if (glAccountingCalendarValueNext.getAcvQuarter() != glAccountingCalendarValue.getAcvQuarter() && glAccountingCalendarValueNext.getAcvQuarter() != glAccountingCalendarValue.getAcvQuarter() + 1)
                return false;
        }

        return details.getAcvQuarter() == 4 && (details.getAcvQuarter() == glAccountingCalendarValueNext.getAcvQuarter() || details.getAcvQuarter() == glAccountingCalendarValueNext.getAcvQuarter() + 1);
    }

    private boolean isDateSequentialAndHasNoGap(GlAccountingCalendarValueDetails details, LocalGlAccountingCalendar glAccountingCalendar, Integer AD_CMPNY) {

        Debug.print("GlAccountingCalendarValueControllerBean isDateSequentialAndHasNoGap");

        LocalGlPeriodType glPeriodType = null;
        GregorianCalendar gc = new GregorianCalendar();
        GregorianCalendar gcNext = new GregorianCalendar();
        LocalGlAccountingCalendarValue glAccountingCalendarValue = null;
        LocalGlAccountingCalendarValue glAccountingCalendarValueNext = null;

        try {
            glPeriodType = glAccountingCalendar.getGlPeriodType();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        for (short i = 1; i < glPeriodType.getPtPeriodPerYear() - 1; i++) {

            try {
                glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodNumber(glAccountingCalendar.getAcCode(), i, AD_CMPNY);

                glAccountingCalendarValueNext = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodNumber(glAccountingCalendar.getAcCode(), (short) (i + 1), AD_CMPNY);
            } catch (FinderException ex) {
                throw new EJBException();
            } catch (Exception ex) {
                throw new EJBException(ex.getMessage());
            }

            gc.setTime(new Date(glAccountingCalendarValue.getAcvDateTo().getTime()));
            gcNext.setTime(new Date(glAccountingCalendarValueNext.getAcvDateFrom().getTime()));

            gc.add(Calendar.DATE, 1);

            if (!(gc.get(Calendar.MONTH) == gcNext.get(Calendar.MONTH) && gc.get(Calendar.DATE) == gcNext.get(Calendar.DATE) && gc.get(Calendar.YEAR) == gcNext.get(Calendar.YEAR)))
                return false;
        }

        gc.setTime(new Date(glAccountingCalendarValueNext.getAcvDateTo().getTime()));
        gcNext.setTime(new Date(details.getAcvDateFrom().getTime()));
        gc.add(Calendar.DATE, 1);

        return gc.get(Calendar.MONTH) == gcNext.get(Calendar.MONTH) && gc.get(Calendar.DATE) == gcNext.get(Calendar.DATE) && gc.get(Calendar.YEAR) == gcNext.get(Calendar.YEAR);
    }

    private boolean isAccountingCalendarOneYear(GlAccountingCalendarValueDetails details, LocalGlAccountingCalendar glAccountingCalendar, Integer AD_CMPNY) {

        Debug.print("GlAccountingCalendarValueControllerBean isAccountingCalendarOneYear");

        GregorianCalendar gcFirstDateFrom = new GregorianCalendar();
        GregorianCalendar gcLastDateTo = new GregorianCalendar();
        LocalGlAccountingCalendarValue glAccountingCalendarValue = null;

        try {
            glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodNumber(glAccountingCalendar.getAcCode(), (short) 1, AD_CMPNY);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        gcFirstDateFrom.setTime(new Date(glAccountingCalendarValue.getAcvDateFrom().getTime()));
        gcLastDateTo.setTime(new Date(details.getAcvDateTo().getTime()));

        gcFirstDateFrom.add(Calendar.DATE, 364);

        if (gcFirstDateFrom.get(Calendar.MONTH) == gcLastDateTo.get(Calendar.MONTH) && gcFirstDateFrom.get(Calendar.DATE) == gcLastDateTo.get(Calendar.DATE) && gcFirstDateFrom.get(Calendar.YEAR) == gcLastDateTo.get(Calendar.YEAR))
            return true;
        else {
            gcFirstDateFrom.add(Calendar.DATE, 1);
            return gcFirstDateFrom.get(Calendar.MONTH) == gcLastDateTo.get(Calendar.MONTH) && gcFirstDateFrom.get(Calendar.DATE) == gcLastDateTo.get(Calendar.DATE) && gcFirstDateFrom.get(Calendar.YEAR) == gcLastDateTo.get(Calendar.YEAR);
        }
    }
}