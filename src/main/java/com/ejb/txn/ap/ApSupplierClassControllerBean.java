/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ApSupplierClassControllerBean
 * @created
 * @author
 */
package com.ejb.txn.ap;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ap.ILocalApSupplierHome;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.entities.ap.LocalApSupplierClass;
import com.ejb.dao.ap.LocalApSupplierClassHome;
import com.ejb.entities.ap.LocalApTaxCode;
import com.ejb.dao.ap.LocalApTaxCodeHome;
import com.ejb.entities.ap.LocalApWithholdingTaxCode;
import com.ejb.dao.ap.LocalApWithholdingTaxCodeHome;
import com.ejb.exception.ap.ApSCCoaGlExpenseAccountNotFoundException;
import com.ejb.exception.ap.ApSCCoaGlPayableAccountNotFoundException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.dao.gl.ILocalGlInvestorAccountBalanceHome;
import com.ejb.dao.gl.ILocalGlSetOfBookHome;
import com.ejb.entities.gl.LocalGlAccountingCalendarValue;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.gl.LocalGlInvestorAccountBalance;
import com.ejb.entities.gl.LocalGlSetOfBook;
import com.util.mod.ap.ApModSupplierClassDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ApSupplierClassControllerEJB")
public class ApSupplierClassControllerBean extends EJBContextClass implements ApSupplierClassController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private ILocalGlSetOfBookHome glSetOfBookHome;
    @EJB
    private ILocalGlInvestorAccountBalanceHome glInvestorAccountBalanceHome;
    @EJB
    private ILocalApSupplierHome apSupplierHome;
    @EJB
    private LocalApSupplierClassHome apSupplierClassHome;
    @EJB
    private LocalApTaxCodeHome apTaxCodeHome;
    @EJB
    private LocalApWithholdingTaxCodeHome apWithholdingTaxCodeHome;


    public ArrayList getApScAll(Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApSupplierClassControllerBean getApScAll");

        ArrayList list = new ArrayList();

        try {

            Collection apSupplierClasses = apSupplierClassHome.findScAll(AD_CMPNY);

            if (apSupplierClasses.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            for (Object supplierClass : apSupplierClasses) {

                LocalApSupplierClass apSupplierClass = (LocalApSupplierClass) supplierClass;

                LocalGlChartOfAccount glPayableAccount = glChartOfAccountHome.findByPrimaryKey(apSupplierClass.getScGlCoaPayableAccount());
                LocalGlChartOfAccount glExpenseAccount = null;

                if (apSupplierClass.getScGlCoaExpenseAccount() != null) {

                    glExpenseAccount = glChartOfAccountHome.findByPrimaryKey(apSupplierClass.getScGlCoaExpenseAccount());
                }

                ApModSupplierClassDetails mdetails = new ApModSupplierClassDetails();
                mdetails.setScCode(apSupplierClass.getScCode());
                mdetails.setScName(apSupplierClass.getScName());
                mdetails.setScDescription(apSupplierClass.getScDescription());

                mdetails.setScTcName(apSupplierClass.getApTaxCode().getTcName());
                mdetails.setScWtcName(apSupplierClass.getApWithholdingTaxCode().getWtcName());
                mdetails.setScCoaGlPayableAccountNumber(glPayableAccount.getCoaAccountNumber());
                mdetails.setScCoaGlExpenseAccountNumber(glExpenseAccount != null ? glExpenseAccount.getCoaAccountNumber() : null);
                mdetails.setScCoaGlPayableAccountDescription(glPayableAccount.getCoaAccountDescription());
                mdetails.setScInvestorBonusRate(apSupplierClass.getScInvestorBonusRate());
                mdetails.setScInvestorInterestRate(apSupplierClass.getScInvestorInterestRate());
                if (glExpenseAccount != null) {

                    mdetails.setScCoaGlExpenseAccountDescription(glExpenseAccount.getCoaAccountDescription());
                }

                mdetails.setScEnable(apSupplierClass.getScEnable());
                mdetails.setScLedger(apSupplierClass.getScLedger());
                mdetails.setScIsInvestment(apSupplierClass.getScIsInvestment());
                mdetails.setScIsLoan(apSupplierClass.getScIsLoan());
                mdetails.setScIsVatReliefVoucherItem(apSupplierClass.getScIsVatReliefVoucherItem());
                mdetails.setScLastModifiedBy(apSupplierClass.getScLastModifiedBy());
                mdetails.setScDateLastModified(apSupplierClass.getScDateLastModified());

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

    public ArrayList getApTcAll(Integer AD_CMPNY) {

        Debug.print("ApSupplierClassControllerBean getApTcAll");

        Collection apTaxCodes = null;

        LocalApTaxCode apTaxCode = null;

        ArrayList list = new ArrayList();

        try {

            apTaxCodes = apTaxCodeHome.findEnabledTcAll(AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (apTaxCodes.isEmpty()) {

            return null;
        }

        for (Object taxCode : apTaxCodes) {

            apTaxCode = (LocalApTaxCode) taxCode;

            list.add(apTaxCode.getTcName());
        }

        return list;
    }

    public ArrayList getApWtcAll(Integer AD_CMPNY) {

        Debug.print("ApSupplierClassControllerBean getApWtcAll");

        Collection apWithholdingTaxCodes = null;

        LocalApWithholdingTaxCode apWithholdingTaxCode = null;

        ArrayList list = new ArrayList();

        try {

            apWithholdingTaxCodes = apWithholdingTaxCodeHome.findEnabledWtcAll(AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (apWithholdingTaxCodes.isEmpty()) {

            return null;
        }

        for (Object withholdingTaxCode : apWithholdingTaxCodes) {

            apWithholdingTaxCode = (LocalApWithholdingTaxCode) withholdingTaxCode;

            list.add(apWithholdingTaxCode.getWtcName());
        }

        return list;
    }

    public void addApScEntry(ApModSupplierClassDetails mdetails, String SC_TC_NM, String SC_WTC_NM, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, ApSCCoaGlPayableAccountNotFoundException, ApSCCoaGlExpenseAccountNotFoundException {

        Debug.print("ApSupplierClassControllerBean addApTcEntry");

        LocalGlChartOfAccount glPayableAccount = null;
        LocalGlChartOfAccount glExpenseAccount = null;

        ArrayList list = new ArrayList();

        try {

            LocalApSupplierClass apSupplierClass = null;
            LocalApTaxCode apTaxCode = null;
            LocalApWithholdingTaxCode apWithholdingTaxCode = null;

            try {

                apSupplierClass = apSupplierClassHome.findByScName(mdetails.getScName(), AD_CMPNY);

                throw new GlobalRecordAlreadyExistException();

            } catch (FinderException ex) {

            }

            // get payable and expense account to validate accounts

            try {

                glPayableAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getScCoaGlPayableAccountNumber(), AD_CMPNY);

            } catch (FinderException ex) {

                throw new ApSCCoaGlPayableAccountNotFoundException();
            }

            if (mdetails.getScCoaGlExpenseAccountNumber() != null && mdetails.getScCoaGlExpenseAccountNumber().length() > 0) {

                try {

                    glExpenseAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getScCoaGlExpenseAccountNumber(), AD_CMPNY);

                } catch (FinderException ex) {

                    throw new ApSCCoaGlExpenseAccountNotFoundException();
                }
            }

            // create new supplier class

            apSupplierClass = apSupplierClassHome.create(mdetails.getScName(), mdetails.getScDescription(), glPayableAccount.getCoaCode(), glExpenseAccount != null ? glExpenseAccount.getCoaCode() : null, mdetails.getScInvestorBonusRate(), mdetails.getScInvestorInterestRate(), mdetails.getScEnable(), mdetails.getScLedger(), mdetails.getScIsInvestment(), mdetails.getScIsLoan(), mdetails.getScIsVatReliefVoucherItem(), mdetails.getScLastModifiedBy(), mdetails.getScDateLastModified(), AD_CMPNY);

            apTaxCode = apTaxCodeHome.findByTcName(SC_TC_NM, AD_CMPNY);
            apTaxCode.addApSupplierClass(apSupplierClass);

            apWithholdingTaxCode = apWithholdingTaxCodeHome.findByWtcName(SC_WTC_NM, AD_CMPNY);
            apWithholdingTaxCode.addApSupplierClass(apSupplierClass);

            if (apSupplierClass.getScLedger() == EJBCommon.TRUE) {
                Collection suppliers = apSupplierHome.findAllSplByScCode(apSupplierClass.getScCode(), AD_CMPNY);

                for (Object supplier : suppliers) {

                    LocalApSupplier apSupplier = (LocalApSupplier) supplier;

                    if (apSupplier.getGlInvestorAccountBalances().size() == 0)
                        this.generateGlInvtrBalance(apSupplier, AD_CMPNY);
                }
            }

        } catch (GlobalRecordAlreadyExistException | ApSCCoaGlExpenseAccountNotFoundException |
                 ApSCCoaGlPayableAccountNotFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateApScEntry(ApModSupplierClassDetails mdetails, String SC_TC_NM, String SC_WTC_NM, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, ApSCCoaGlPayableAccountNotFoundException, ApSCCoaGlExpenseAccountNotFoundException {

        Debug.print("ApSupplierClassControllerBean updateApTcEntry");

        LocalApSupplierClass apSupplierClass = null;
        LocalApTaxCode apTaxCode = null;
        LocalApWithholdingTaxCode apWithholdingTaxCode = null;
        LocalGlChartOfAccount glPayableAccount = null;
        LocalGlChartOfAccount glExpenseAccount = null;

        ArrayList list = new ArrayList();

        try {

            try {

                LocalApSupplierClass apExistingSupplierClass = apSupplierClassHome.findByScName(mdetails.getScName(), AD_CMPNY);

                if (!apExistingSupplierClass.getScCode().equals(mdetails.getScCode())) {

                    throw new GlobalRecordAlreadyExistException();
                }

            } catch (FinderException ex) {

            }

            // get payable and expense account to validate accounts

            try {

                glPayableAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getScCoaGlPayableAccountNumber(), AD_CMPNY);

            } catch (FinderException ex) {

                throw new ApSCCoaGlPayableAccountNotFoundException();
            }

            if (mdetails.getScCoaGlExpenseAccountNumber() != null && mdetails.getScCoaGlExpenseAccountNumber().length() > 0) {

                try {

                    glExpenseAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getScCoaGlExpenseAccountNumber(), AD_CMPNY);

                } catch (FinderException ex) {

                    throw new ApSCCoaGlExpenseAccountNotFoundException();
                }
            }

            // find and update supplier class

            apSupplierClass = apSupplierClassHome.findByPrimaryKey(mdetails.getScCode());

            apSupplierClass.setScName(mdetails.getScName());
            apSupplierClass.setScDescription(mdetails.getScDescription());
            apSupplierClass.setScGlCoaPayableAccount(glPayableAccount.getCoaCode());
            apSupplierClass.setScGlCoaExpenseAccount(glExpenseAccount != null ? glExpenseAccount.getCoaCode() : null);
            apSupplierClass.setScInvestorBonusRate(mdetails.getScInvestorBonusRate());
            apSupplierClass.setScInvestorInterestRate(mdetails.getScInvestorInterestRate());
            apSupplierClass.setScEnable(mdetails.getScEnable());
            apSupplierClass.setScLedger(mdetails.getScLedger());
            apSupplierClass.setScIsInvestment(mdetails.getScIsInvestment());
            apSupplierClass.setScIsLoan(mdetails.getScIsLoan());
            apSupplierClass.setScIsVatReliefVoucherItem(mdetails.getScIsVatReliefVoucherItem());
            apSupplierClass.setScLastModifiedBy(mdetails.getScLastModifiedBy());
            apSupplierClass.setScDateLastModified(mdetails.getScDateLastModified());

            apTaxCode = apTaxCodeHome.findByTcName(SC_TC_NM, AD_CMPNY);
            apTaxCode.addApSupplierClass(apSupplierClass);

            apWithholdingTaxCode = apWithholdingTaxCodeHome.findByWtcName(SC_WTC_NM, AD_CMPNY);
            apWithholdingTaxCode.addApSupplierClass(apSupplierClass);

            if (apSupplierClass.getScLedger() == EJBCommon.TRUE) {
                Collection suppliers = apSupplierHome.findAllSplByScCode(apSupplierClass.getScCode(), AD_CMPNY);

                for (Object supplier : suppliers) {

                    LocalApSupplier apSupplier = (LocalApSupplier) supplier;

                    if (apSupplier.getGlInvestorAccountBalances().size() == 0)
                        this.generateGlInvtrBalance(apSupplier, AD_CMPNY);
                }
            }

        } catch (GlobalRecordAlreadyExistException | ApSCCoaGlExpenseAccountNotFoundException |
                 ApSCCoaGlPayableAccountNotFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteApScEntry(Integer SC_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException {

        Debug.print("ApSupplierClassControllerBean deleteApTcEntry");

        try {

            LocalApSupplierClass apSupplierClass = null;

            try {

                apSupplierClass = apSupplierClassHome.findByPrimaryKey(SC_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            if (!apSupplierClass.getApSuppliers().isEmpty()) {

                throw new GlobalRecordAlreadyAssignedException();
            }

            // apSupplierClass.entityRemove();
            em.remove(apSupplierClass);

        } catch (GlobalRecordAlreadyDeletedException | GlobalRecordAlreadyAssignedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void generateGlInvtrBalance(LocalApSupplier apSupplier, Integer AD_CMPNY) {

        Debug.print("ApSupplierEntryControllerBean generateGlInvtrBalance");

        try {

            // create balances to all existing customer

            Collection glSetOfBooks = glSetOfBookHome.findSobAll(AD_CMPNY);

            for (Object setOfBook : glSetOfBooks) {

                LocalGlSetOfBook glSetOfBook = (LocalGlSetOfBook) setOfBook;

                Collection glAccountingCalendarValues = glSetOfBook.getGlAccountingCalendar().getGlAccountingCalendarValues();

                for (Object accountingCalendarValue : glAccountingCalendarValues) {

                    LocalGlAccountingCalendarValue glAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                    LocalGlInvestorAccountBalance glInvestorAccountBalance = null;
                    try {

                        glInvestorAccountBalance = glInvestorAccountBalanceHome.create(0d, 0d, (byte) 0, (byte) 0, 0d, 0d, 0d, 0d, 0d, 0d, AD_CMPNY);
                        glInvestorAccountBalance.setGlAccountingCalendarValue(glAccountingCalendarValue);

                        glInvestorAccountBalance.setApSupplier(apSupplier);
                    } catch (Exception ex) {

                        getSessionContext().setRollbackOnly();
                        throw new EJBException(ex.getMessage());
                    }
                }
            }

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApSupplierClassControllerBean ejbCreate");
    }
}