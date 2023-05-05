package com.ejb.txn.ad;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;


import java.util.ArrayList;
import java.util.Collection;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;

import com.ejb.entities.ad.LocalAdDiscount;
import com.ejb.dao.ad.LocalAdDiscountHome;
import com.ejb.entities.ad.LocalAdPaymentSchedule;
import com.ejb.dao.ad.LocalAdPaymentScheduleHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.*;
import com.util.ad.AdDiscountDetails;
import com.util.ad.AdPaymentScheduleDetails;

@Stateless(name = "AdDiscountControllerEJB")
public class AdDiscountControllerBean extends EJBContextClass implements AdDiscountController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalAdDiscountHome adDiscountHome;
    @EJB
    private LocalAdPaymentScheduleHome adPaymentScheduleHome;

    public ArrayList getAdDscByPsCode(Integer paymentScheduleCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdDiscountControllerBean getAdDscByPsCode");

        Collection adDiscounts = null;
        LocalAdDiscount adDiscount;
        ArrayList list = new ArrayList();
        try {

            adDiscounts = adDiscountHome.findByPsCode(paymentScheduleCode, companyCode);

        } catch (FinderException ex) {
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        if (adDiscounts.isEmpty()) {

            throw new GlobalNoRecordFoundException();

        }
        for (Object discount : adDiscounts) {

            adDiscount = (LocalAdDiscount) discount;
            AdDiscountDetails details = new AdDiscountDetails();
            details.setDscCode(adDiscount.getDscCode());
            details.setDscDiscountPercent(adDiscount.getDscDiscountPercent());
            details.setDscPaidWithinDay(adDiscount.getDscPaidWithinDay());

            list.add(details);
        }
        return list;
    }

    public AdPaymentScheduleDetails getAdPsByPsCode(Integer paymentScheduleCode, Integer companyCode) {

        Debug.print("AdDiscountControllerBean getAdPsByPsCode");

        try {

            LocalAdPaymentSchedule adPaymentSchedule = adPaymentScheduleHome.findByPrimaryKey(paymentScheduleCode);

            AdPaymentScheduleDetails details = new AdPaymentScheduleDetails();
            details.setPsCode(adPaymentSchedule.getPsCode());
            details.setPsLineNumber(adPaymentSchedule.getPsLineNumber());
            details.setPsRelativeAmount(adPaymentSchedule.getPsRelativeAmount());
            details.setPsDueDay(adPaymentSchedule.getPsDueDay());

            return details;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }
    }

    public void addAdDscEntry(AdDiscountDetails details, Integer paymentScheduleCode, Integer companyCode) throws GlobalRecordAlreadyExistException {

        Debug.print("AdDiscountControllerBean addAdDscEntry");

        LocalAdPaymentSchedule adPaymentSchedule;
        LocalAdDiscount adDiscount;
        try {

            adDiscount = adDiscountHome.findByPsCodeAndDscPaidWithinDate(paymentScheduleCode, details.getDscPaidWithinDay(), companyCode);

            throw new GlobalRecordAlreadyExistException();

        } catch (GlobalRecordAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }
        try {

            // create new discount

            adDiscount = adDiscountHome.create(details.getDscDiscountPercent(), details.getDscPaidWithinDay(), companyCode);

            adPaymentSchedule = adPaymentScheduleHome.findByPrimaryKey(paymentScheduleCode);
            adPaymentSchedule.addAdDiscount(adDiscount);

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();

            throw new EJBException(ex.getMessage());

        }
    }

    public void updateAdDscEntry(AdDiscountDetails details, Integer paymentScheduleCode, Integer companyCode) throws GlobalRecordAlreadyExistException {

        Debug.print("AdDiscountControllerBean updateAdDscEntry");

        LocalAdPaymentSchedule adPaymentSchedule;
        LocalAdDiscount adDiscount;
        try {

            LocalAdDiscount arExistingDiscount = adDiscountHome.findByPsCodeAndDscPaidWithinDate(paymentScheduleCode, details.getDscPaidWithinDay(), companyCode);

            if (!arExistingDiscount.getDscCode().equals(details.getDscCode())) {

                throw new GlobalRecordAlreadyExistException();

            }

        } catch (GlobalRecordAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }
        try {

            // find and update payment schedule

            adDiscount = adDiscountHome.findByPrimaryKey(details.getDscCode());

            adDiscount.setDscDiscountPercent(details.getDscDiscountPercent());
            adDiscount.setDscPaidWithinDay(details.getDscPaidWithinDay());

            adPaymentSchedule = adPaymentScheduleHome.findByPrimaryKey(paymentScheduleCode);
            adPaymentSchedule.addAdDiscount(adDiscount);

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();

            throw new EJBException(ex.getMessage());

        }
    }

    public void deleteAdDscEntry(Integer discountCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException {

        Debug.print("AdDiscountControllerBean deleteAdDscEntry");

        LocalAdDiscount adDiscount;
        try {

            adDiscount = adDiscountHome.findByPrimaryKey(discountCode);

        } catch (FinderException ex) {

            throw new GlobalRecordAlreadyDeletedException();

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }
        try {

//	      adDiscount.entityRemove();
            em.remove(adDiscount);

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();

            throw new EJBException(ex.getMessage());

        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("AdDiscountControllerBean ejbCreate");

    }
}