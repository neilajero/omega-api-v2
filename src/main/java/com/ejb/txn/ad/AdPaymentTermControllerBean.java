package com.ejb.txn.ad;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdPaymentSchedule;
import com.ejb.dao.ad.LocalAdPaymentScheduleHome;
import com.ejb.entities.ad.LocalAdPaymentTerm;
import com.ejb.dao.ad.LocalAdPaymentTermHome;
import com.ejb.exception.global.GlobalAccountNumberInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.util.*;
import com.util.ad.AdPaymentTermDetails;
import com.util.mod.ad.AdModPaymentTermDetails;

@Stateless(name = "AdPaymentTermControllerEJB")
public class AdPaymentTermControllerBean extends EJBContextClass implements AdPaymentTermController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalAdPaymentTermHome adPaymentTermHome;
    @EJB
    private LocalAdPaymentScheduleHome adPaymentScheduleHome;

    public ArrayList getAdPytAll(Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdPaymentTermControllerBean getAdPytAll");

        ArrayList list = new ArrayList();
        try {

            Collection adPaymentTerms = adPaymentTermHome.findPytAll(companyCode);

            if (adPaymentTerms.isEmpty()) {

                Debug.print("Empty");
            }

            if (adPaymentTerms.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            for (Object paymentTerm : adPaymentTerms) {

                LocalAdPaymentTerm adPaymentTerm = (LocalAdPaymentTerm) paymentTerm;

                AdModPaymentTermDetails mdetails = new AdModPaymentTermDetails();
                mdetails.setPytCode(adPaymentTerm.getPytCode());
                mdetails.setPytName(adPaymentTerm.getPytName());
                mdetails.setPytDescription(adPaymentTerm.getPytDescription());
                mdetails.setPytBaseAmount(adPaymentTerm.getPytBaseAmount());
                mdetails.setPytMonthlyInterestRate(adPaymentTerm.getPytMonthlyInterestRate());
                mdetails.setPytEnable(adPaymentTerm.getPytEnable());
                mdetails.setPytEnableRebate(adPaymentTerm.getPytEnableRebate());
                mdetails.setPytEnableInterest(adPaymentTerm.getPytEnableInterest());
                mdetails.setPytDiscountOnInvoice(adPaymentTerm.getPytDiscountOnInvoice());
                mdetails.setPytScheduleBasis(adPaymentTerm.getPytScheduleBasis());

                if (adPaymentTerm.getPytDiscountOnInvoice() == EJBCommon.TRUE) {
                    mdetails.setPtGlCoaAccountNumber(adPaymentTerm.getGlChartOfAccount().getCoaAccountNumber());
                    mdetails.setPtGlCoaAccountDescription(adPaymentTerm.getGlChartOfAccount().getCoaAccountDescription());
                    mdetails.setPytDiscountDescription(adPaymentTerm.getPytDiscountDescription());
                }

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

    public void addAdPytEntry(AdPaymentTermDetails details, String coaAccountNumber, Integer companyCode) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException {

        Debug.print("AdPaymentTermControllerBean addAdPytEntry");

        LocalAdPaymentTerm adPaymentTerm;
        LocalGlChartOfAccount glAccount = null;
        LocalAdPaymentSchedule adPaymentSchedule;
        try {

            try {

                adPaymentTerm = adPaymentTermHome.findByPytName(details.getPytName(), companyCode);

                throw new GlobalRecordAlreadyExistException();

            } catch (FinderException ex) {

            }

            try {

                if (coaAccountNumber != null && coaAccountNumber.length() > 0) {

                    glAccount = glChartOfAccountHome.findByCoaAccountNumber(coaAccountNumber, companyCode);
                }

            } catch (FinderException ex) {

                throw new GlobalAccountNumberInvalidException();
            }

            try {

                // create new payment term

                adPaymentTerm = adPaymentTermHome.create(details.getPytName(), details.getPytDescription(), details.getPytBaseAmount(), details.getPytMonthlyInterestRate(), details.getPytEnable(), details.getPytEnableRebate(), details.getPytEnableInterest(), details.getPytDiscountOnInvoice(), details.getPytDiscountDescription(), details.getPytScheduleBasis(), companyCode);

                if (glAccount != null) {

                    glAccount.addAdPaymentTerm(adPaymentTerm);
                }

                // create payment schedule
                short lineNumber = 1;
                short dueDay = 0;

                adPaymentSchedule = adPaymentScheduleHome.create(lineNumber, details.getPytBaseAmount(), dueDay, companyCode);

                adPaymentTerm.addAdPaymentSchedule(adPaymentSchedule);

            } catch (Exception ex) {

                throw new EJBException(ex.getMessage());
            }

        } catch (GlobalRecordAlreadyExistException | GlobalAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateAdPytEntry(AdPaymentTermDetails details, String coaAccountNumber, Integer companyCode) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException {

        Debug.print("AdPaymentTermControllerBean updateAdPytEntry");

        LocalAdPaymentTerm adPaymentTerm;
        LocalGlChartOfAccount glAccount = null;
        try {
            try {

                LocalAdPaymentTerm arExistingPaymentTerm = adPaymentTermHome.findByPytName(details.getPytName(), companyCode);

                if (!arExistingPaymentTerm.getPytCode().equals(details.getPytCode())) {

                    throw new GlobalRecordAlreadyExistException();
                }

            } catch (FinderException ex) {

            }

            try {

                if (coaAccountNumber != null && coaAccountNumber.length() > 0) {

                    glAccount = glChartOfAccountHome.findByCoaAccountNumber(coaAccountNumber, companyCode);
                }

            } catch (FinderException ex) {

                throw new GlobalAccountNumberInvalidException();
            }

            try {

                // find and update payment term

                adPaymentTerm = adPaymentTermHome.findByPrimaryKey(details.getPytCode());

                adPaymentTerm.setPytName(details.getPytName());
                adPaymentTerm.setPytDescription(details.getPytDescription());
                adPaymentTerm.setPytBaseAmount(details.getPytBaseAmount());
                adPaymentTerm.setPytMonthlyInterestRate(details.getPytMonthlyInterestRate());
                adPaymentTerm.setPytEnable(details.getPytEnable());
                adPaymentTerm.setPytEnableRebate(details.getPytEnableRebate());
                adPaymentTerm.setPytEnableInterest(details.getPytEnableInterest());
                adPaymentTerm.setPytDiscountOnInvoice(details.getPytDiscountOnInvoice());
                adPaymentTerm.setPytDiscountDescription(details.getPytDiscountDescription());
                adPaymentTerm.setPytScheduleBasis(details.getPytScheduleBasis());

                if (glAccount != null) {

                    glAccount.addAdPaymentTerm(adPaymentTerm);

                } else if (glAccount == null && adPaymentTerm.getGlChartOfAccount() != null) {

                    adPaymentTerm.getGlChartOfAccount().dropAdPaymentTerm(adPaymentTerm);
                }

            } catch (Exception ex) {

                throw new EJBException(ex.getMessage());
            }

        } catch (GlobalRecordAlreadyExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (GlobalAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            ex.printStackTrace();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteAdPytEntry(Integer paymentTermCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException {

        Debug.print("AdPaymentTermControllerBean deleteAdPytEntry");

        LocalAdPaymentTerm adPaymentTerm;
        try {
            adPaymentTerm = adPaymentTermHome.findByPrimaryKey(paymentTermCode);
        } catch (FinderException ex) {

            throw new GlobalRecordAlreadyDeletedException();

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        try {

            if (!adPaymentTerm.getApVouchers().isEmpty() || !adPaymentTerm.getApRecurringVouchers().isEmpty() || !adPaymentTerm.getApSuppliers().isEmpty() || !adPaymentTerm.getArCustomers().isEmpty() || !adPaymentTerm.getArInvoices().isEmpty() || !adPaymentTerm.getApPurchaseOrders().isEmpty() || !adPaymentTerm.getArPdcs().isEmpty() || !adPaymentTerm.getArSalesOrders().isEmpty()) {

                throw new GlobalRecordAlreadyAssignedException();
            }
        } catch (GlobalRecordAlreadyAssignedException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        try {

            //	      adPaymentTerm.entityRemove();
            em.remove(adPaymentTerm);

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();

            throw new EJBException(ex.getMessage());

        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("AdPaymentTermControllerBean ejbCreate");
    }
}