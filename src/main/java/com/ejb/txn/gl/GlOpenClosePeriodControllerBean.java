package com.ejb.txn.gl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.exception.global.GlobalNoSetOfBookFoundException;
import com.ejb.dao.gl.ILocalGlAccountingCalendarValueHome;
import com.ejb.dao.gl.ILocalGlSetOfBookHome;
import com.ejb.entities.gl.LocalGlAccountingCalendarValue;
import com.ejb.entities.gl.LocalGlSetOfBook;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.gl.GlAccountingCalendarValueDetails;

@Stateless(name = "GlOpenClosePeriodControllerEJB")
public class GlOpenClosePeriodControllerBean extends EJBContextClass implements GlOpenClosePeriodController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlSetOfBookHome glSetOfBookHome;
    @EJB
    private ILocalGlAccountingCalendarValueHome glAccountingCalendarValueHome;

    public ArrayList getEditableYears(Integer AD_CMPNY) throws GlobalNoSetOfBookFoundException {

        Debug.print("GlOpenClosePeriodControllerBean getEditableYears");

        ArrayList list = new ArrayList();

        try {

            Collection glSetOfBooks = glSetOfBookHome.findSobAll(AD_CMPNY);

            for (Object setOfBook : glSetOfBooks) {

                LocalGlSetOfBook glSetOfBook = (LocalGlSetOfBook) setOfBook;

                Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(glSetOfBook.getGlAccountingCalendar().getAcCode(), AD_CMPNY);

                ArrayList glAcvList = new ArrayList(glAccountingCalendarValues);

                LocalGlAccountingCalendarValue glAccountingCalendarValue = (LocalGlAccountingCalendarValue) glAcvList.get(glAcvList.size() - 1);

                GregorianCalendar gc = new GregorianCalendar();
                gc.setTime(glAccountingCalendarValue.getAcvDateTo());

                list.add(gc.get(Calendar.YEAR));
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGlAcvByYear(int YR, Integer AD_CMPNY) {

        Debug.print("GlOpenClosePeriodControllerBean getGlAcvByYear");

        ArrayList list = new ArrayList();

        try {

            LocalGlSetOfBook glSetOfBook = glSetOfBookHome.findByDate(EJBCommon.getIntendedDate(YR), AD_CMPNY);

            Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(glSetOfBook.getGlAccountingCalendar().getAcCode(), AD_CMPNY);

            for (Object accountingCalendarValue : glAccountingCalendarValues) {

                LocalGlAccountingCalendarValue glAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                GlAccountingCalendarValueDetails details = new GlAccountingCalendarValueDetails(glAccountingCalendarValue.getAcvCode(), glAccountingCalendarValue.getAcvPeriodPrefix(), glAccountingCalendarValue.getAcvQuarter(), glAccountingCalendarValue.getAcvPeriodNumber(), glAccountingCalendarValue.getAcvDateFrom(), glAccountingCalendarValue.getAcvDateTo(), glAccountingCalendarValue.getAcvStatus());

                list.add(details);
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateGlAcvStatus(GlAccountingCalendarValueDetails details, int YR, Integer AD_CMPNY) {

        Debug.print("GlOpenClosePeriodControllerBean updateGlAcvStatus");

        try {

            LocalGlSetOfBook glSetOfBook = glSetOfBookHome.findByDate(EJBCommon.getIntendedDate(YR), AD_CMPNY);

            LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByPrimaryKey(details.getAcvCode());

            if (details.getAcvStatus() == 'F') {

                Collection glSetOfBooks = glSetOfBookHome.findSobAll(AD_CMPNY);

                for (Object setOfBook : glSetOfBooks) {

                    LocalGlSetOfBook glRunningSetOfBook = (LocalGlSetOfBook) setOfBook;

                    Collection glToChangeAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCodeAndAcvStatus(glRunningSetOfBook.getGlAccountingCalendar().getAcCode(), 'N', AD_CMPNY);

                    for (Object toChangeAccountingCalendarValue : glToChangeAccountingCalendarValues) {

                        LocalGlAccountingCalendarValue glToChangeAccountingCalendarValue = (LocalGlAccountingCalendarValue) toChangeAccountingCalendarValue;

                        if (glRunningSetOfBook.equals(glSetOfBook) && glAccountingCalendarValue.getAcvPeriodNumber() < glToChangeAccountingCalendarValue.getAcvPeriodNumber()) {

                            break;
                        }

                        glToChangeAccountingCalendarValue.setAcvStatus('F');
                        glToChangeAccountingCalendarValue.setAcvDateFutureEntered(EJBCommon.getGcCurrentDateWoTime().getTime());
                    }

                    if (glRunningSetOfBook.equals(glSetOfBook)) {

                        break;
                    }
                }

            } else if (details.getAcvStatus() == 'O' && glAccountingCalendarValue.getAcvDateClosed() == null) {

                Collection glSetOfBooks = glSetOfBookHome.findSobAll(AD_CMPNY);

                for (Object setOfBook : glSetOfBooks) {

                    LocalGlSetOfBook glRunningSetOfBook = (LocalGlSetOfBook) setOfBook;

                    Collection glToChangeAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCodeAndAcvStatus(glRunningSetOfBook.getGlAccountingCalendar().getAcCode(), 'N', AD_CMPNY);

                    Iterator j = glToChangeAccountingCalendarValues.iterator();

                    while (j.hasNext()) {

                        LocalGlAccountingCalendarValue glToChangeAccountingCalendarValue = (LocalGlAccountingCalendarValue) j.next();

                        if (glRunningSetOfBook.equals(glSetOfBook) && glAccountingCalendarValue.getAcvPeriodNumber() < glToChangeAccountingCalendarValue.getAcvPeriodNumber()) {

                            break;
                        }

                        glToChangeAccountingCalendarValue.setAcvStatus('O');
                        glToChangeAccountingCalendarValue.setAcvDateOpened(EJBCommon.getGcCurrentDateWoTime().getTime());
                    }

                    glToChangeAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCodeAndAcvStatus(glRunningSetOfBook.getGlAccountingCalendar().getAcCode(), 'F', AD_CMPNY);

                    j = glToChangeAccountingCalendarValues.iterator();

                    while (j.hasNext()) {

                        LocalGlAccountingCalendarValue glToChangeAccountingCalendarValue = (LocalGlAccountingCalendarValue) j.next();

                        if (glRunningSetOfBook.equals(glSetOfBook) && glAccountingCalendarValue.getAcvPeriodNumber() < glToChangeAccountingCalendarValue.getAcvPeriodNumber()) {

                            break;
                        }

                        glToChangeAccountingCalendarValue.setAcvStatus('O');
                        glToChangeAccountingCalendarValue.setAcvDateOpened(EJBCommon.getGcCurrentDateWoTime().getTime());
                    }

                    if (glRunningSetOfBook.equals(glSetOfBook)) {

                        break;
                    }
                }

            } else if (details.getAcvStatus() == 'O' && glAccountingCalendarValue.getAcvDateClosed() != null) {

                glAccountingCalendarValue.setAcvStatus('O');
                glAccountingCalendarValue.setAcvDateOpened(EJBCommon.getGcCurrentDateWoTime().getTime());

            } else if (details.getAcvStatus() == 'C') {

                Collection glSetOfBooks = glSetOfBookHome.findSobAll(AD_CMPNY);

                for (Object setOfBook : glSetOfBooks) {

                    LocalGlSetOfBook glRunningSetOfBook = (LocalGlSetOfBook) setOfBook;

                    Collection glToChangeAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCodeAndAcvStatus(glRunningSetOfBook.getGlAccountingCalendar().getAcCode(), 'O', AD_CMPNY);

                    for (Object toChangeAccountingCalendarValue : glToChangeAccountingCalendarValues) {

                        LocalGlAccountingCalendarValue glToChangeAccountingCalendarValue = (LocalGlAccountingCalendarValue) toChangeAccountingCalendarValue;

                        if (glRunningSetOfBook.equals(glSetOfBook) && glAccountingCalendarValue.getAcvPeriodNumber() < glToChangeAccountingCalendarValue.getAcvPeriodNumber()) {

                            break;
                        }

                        glToChangeAccountingCalendarValue.setAcvStatus('C');
                        glToChangeAccountingCalendarValue.setAcvDateClosed(EJBCommon.getGcCurrentDateWoTime().getTime());
                    }

                    if (glRunningSetOfBook.equals(glSetOfBook)) {

                        break;
                    }
                }

            } else if (details.getAcvStatus() == 'P') {

                Collection glSetOfBooks = glSetOfBookHome.findSobAll(AD_CMPNY);

                for (Object setOfBook : glSetOfBooks) {

                    LocalGlSetOfBook glRunningSetOfBook = (LocalGlSetOfBook) setOfBook;

                    Collection glToChangeAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCodeAndAcvStatus(glRunningSetOfBook.getGlAccountingCalendar().getAcCode(), 'O', AD_CMPNY);

                    Iterator j = glToChangeAccountingCalendarValues.iterator();

                    while (j.hasNext()) {

                        LocalGlAccountingCalendarValue glToChangeAccountingCalendarValue = (LocalGlAccountingCalendarValue) j.next();

                        if (glRunningSetOfBook.equals(glSetOfBook) && glAccountingCalendarValue.getAcvPeriodNumber() < glToChangeAccountingCalendarValue.getAcvPeriodNumber()) {

                            break;
                        }

                        glToChangeAccountingCalendarValue.setAcvStatus('P');
                        glToChangeAccountingCalendarValue.setAcvDatePermanentlyClosed(EJBCommon.getGcCurrentDateWoTime().getTime());
                    }

                    glToChangeAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCodeAndAcvStatus(glRunningSetOfBook.getGlAccountingCalendar().getAcCode(), 'C', AD_CMPNY);

                    j = glToChangeAccountingCalendarValues.iterator();

                    while (j.hasNext()) {

                        LocalGlAccountingCalendarValue glToChangeAccountingCalendarValue = (LocalGlAccountingCalendarValue) j.next();

                        if (glRunningSetOfBook.equals(glSetOfBook) && glAccountingCalendarValue.getAcvPeriodNumber() < glToChangeAccountingCalendarValue.getAcvPeriodNumber()) {

                            break;
                        }

                        glToChangeAccountingCalendarValue.setAcvStatus('P');
                        glToChangeAccountingCalendarValue.setAcvDatePermanentlyClosed(EJBCommon.getGcCurrentDateWoTime().getTime());
                    }

                    if (glRunningSetOfBook.equals(glSetOfBook)) {

                        break;
                    }
                }
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public boolean isGlAcvPriorPeriodStatusNeedConfirm(Integer ACV_CODE, int YR, Integer AD_CMPNY) {

        Debug.print("GlOpenClosePeriodControllerBean isGlAcvPriorPeriodStatusNeedConfirm");

        ArrayList list = new ArrayList();

        try {

            LocalGlSetOfBook glSetOfBook = glSetOfBookHome.findByDate(EJBCommon.getIntendedDate(YR), AD_CMPNY);

            LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByPrimaryKey(ACV_CODE);

            if (glAccountingCalendarValue.getAcvStatus() == 'N') {

                Collection glSetOfBooks = glSetOfBookHome.findSobAll(AD_CMPNY);

                for (Object setOfBook : glSetOfBooks) {

                    LocalGlSetOfBook glRunningSetOfBook = (LocalGlSetOfBook) setOfBook;

                    Collection glToChangeAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCodeAndAcvStatus(glRunningSetOfBook.getGlAccountingCalendar().getAcCode(), 'N', AD_CMPNY);

                    for (Object toChangeAccountingCalendarValue : glToChangeAccountingCalendarValues) {

                        LocalGlAccountingCalendarValue glToChangeAccountingCalendarValue = (LocalGlAccountingCalendarValue) toChangeAccountingCalendarValue;

                        if (!glRunningSetOfBook.equals(glSetOfBook)) {

                            return true;

                        } else {

                            if (glAccountingCalendarValue.getAcvPeriodNumber() > glToChangeAccountingCalendarValue.getAcvPeriodNumber()) {

                                return true;
                            }
                        }
                    }

                    if (glRunningSetOfBook.equals(glSetOfBook)) {

                        break;
                    }
                }

            } else if (glAccountingCalendarValue.getAcvStatus() == 'F') {

                Collection glSetOfBooks = glSetOfBookHome.findSobAll(AD_CMPNY);

                for (Object setOfBook : glSetOfBooks) {

                    LocalGlSetOfBook glRunningSetOfBook = (LocalGlSetOfBook) setOfBook;

                    Collection glToChangeAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCodeAndAcvStatus(glRunningSetOfBook.getGlAccountingCalendar().getAcCode(), 'F', AD_CMPNY);

                    for (Object toChangeAccountingCalendarValue : glToChangeAccountingCalendarValues) {

                        LocalGlAccountingCalendarValue glToChangeAccountingCalendarValue = (LocalGlAccountingCalendarValue) toChangeAccountingCalendarValue;

                        if (!glRunningSetOfBook.equals(glSetOfBook)) {

                            return true;

                        } else {

                            if (glAccountingCalendarValue.getAcvPeriodNumber() > glToChangeAccountingCalendarValue.getAcvPeriodNumber()) {

                                return true;
                            }
                        }
                    }

                    if (glRunningSetOfBook.equals(glSetOfBook)) {

                        break;
                    }
                }

            } else if (glAccountingCalendarValue.getAcvStatus() == 'O') {

                Collection glSetOfBooks = glSetOfBookHome.findSobAll(AD_CMPNY);

                for (Object setOfBook : glSetOfBooks) {

                    LocalGlSetOfBook glRunningSetOfBook = (LocalGlSetOfBook) setOfBook;

                    Collection glToChangeAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCodeAndAcvStatus(glRunningSetOfBook.getGlAccountingCalendar().getAcCode(), 'O', AD_CMPNY);

                    for (Object toChangeAccountingCalendarValue : glToChangeAccountingCalendarValues) {

                        LocalGlAccountingCalendarValue glToChangeAccountingCalendarValue = (LocalGlAccountingCalendarValue) toChangeAccountingCalendarValue;

                        if (!glRunningSetOfBook.equals(glSetOfBook)) {

                            return true;

                        } else {

                            if (glAccountingCalendarValue.getAcvPeriodNumber() > glToChangeAccountingCalendarValue.getAcvPeriodNumber()) {

                                return true;
                            }
                        }
                    }

                    if (glRunningSetOfBook.equals(glSetOfBook)) {

                        break;
                    }
                }

            } else if (glAccountingCalendarValue.getAcvStatus() == 'C') {

                Collection glSetOfBooks = glSetOfBookHome.findSobAll(AD_CMPNY);

                for (Object setOfBook : glSetOfBooks) {

                    LocalGlSetOfBook glRunningSetOfBook = (LocalGlSetOfBook) setOfBook;

                    Collection glToChangeAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCodeAndAcvStatus(glRunningSetOfBook.getGlAccountingCalendar().getAcCode(), 'C', AD_CMPNY);

                    for (Object toChangeAccountingCalendarValue : glToChangeAccountingCalendarValues) {

                        LocalGlAccountingCalendarValue glToChangeAccountingCalendarValue = (LocalGlAccountingCalendarValue) toChangeAccountingCalendarValue;

                        if (!glRunningSetOfBook.equals(glSetOfBook)) {

                            return true;

                        } else {

                            if (glAccountingCalendarValue.getAcvPeriodNumber() > glToChangeAccountingCalendarValue.getAcvPeriodNumber()) {

                                return true;
                            }
                        }
                    }

                    if (glRunningSetOfBook.equals(glSetOfBook)) {

                        break;
                    }
                }
            }

            return false;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlOpenClosePeriodControllerBean ejbCreate");
    }

    // private methods

}