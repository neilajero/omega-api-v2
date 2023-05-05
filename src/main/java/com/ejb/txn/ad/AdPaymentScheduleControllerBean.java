/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class AdPaymentScheduleControllerBean
 * @created May 29, 2003, 2:27 PM
 * @author Enrico C. Yap
 **/
package com.ejb.txn.ad;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;

import com.ejb.entities.ad.LocalAdPaymentSchedule;
import com.ejb.dao.ad.LocalAdPaymentScheduleHome;
import com.ejb.entities.ad.LocalAdPaymentTerm;
import com.ejb.dao.ad.LocalAdPaymentTermHome;
import com.ejb.exception.ad.AdPSRelativeAmountLessPytBaseAmountException;
import com.ejb.exception.ad.AdPSRelativeAmountOverPytBaseAmountException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.util.EJBContextClass;
import com.util.ad.AdPaymentScheduleDetails;
import com.util.ad.AdPaymentTermDetails;
import com.util.Debug;
import com.util.EJBCommon;

@Stateless(name = "AdPaymentScheduleControllerEJB")
public class AdPaymentScheduleControllerBean extends EJBContextClass implements AdPaymentScheduleController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalAdPaymentScheduleHome adPaymentScheduleHome;
    @EJB
    private LocalAdPaymentTermHome adPaymentTermHome;

    public ArrayList getAdPsByPytCode(Integer paymentTermCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdPaymentScheduleControllerBean getAdPsByPytCode");

        Collection adPaymentSchedules = null;
        LocalAdPaymentSchedule adPaymentSchedule;
        ArrayList list = new ArrayList();
        try {
            adPaymentSchedules = adPaymentScheduleHome.findByPytCode(paymentTermCode, companyCode);
        } catch (FinderException ex) {
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        if (adPaymentSchedules.isEmpty()) {
            throw new GlobalNoRecordFoundException();
        }
        for (Object paymentSchedule : adPaymentSchedules) {
            adPaymentSchedule = (LocalAdPaymentSchedule) paymentSchedule;
            AdPaymentScheduleDetails details = new AdPaymentScheduleDetails();
            details.setPsCode(adPaymentSchedule.getPsCode());
            details.setPsLineNumber(adPaymentSchedule.getPsLineNumber());
            details.setPsRelativeAmount(adPaymentSchedule.getPsRelativeAmount());
            details.setPsDueDay(adPaymentSchedule.getPsDueDay());
            list.add(details);
        }
        return list;
    }

    public AdPaymentTermDetails getAdPytByPytCode(Integer paymentTermCode, Integer companyCode) {

        Debug.print("AdPaymentScheduleControllerBean getAdPytByPytCode");

        try {
            LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPrimaryKey(paymentTermCode);
            AdPaymentTermDetails details = new AdPaymentTermDetails();
            details.setPytCode(adPaymentTerm.getPytCode());
            details.setPytName(adPaymentTerm.getPytName());
            details.setPytDescription(adPaymentTerm.getPytDescription());
            details.setPytBaseAmount(adPaymentTerm.getPytBaseAmount());
            details.setPytEnable(adPaymentTerm.getPytEnable());
            return details;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public void addAdPsEntry(ArrayList psList, Integer paymentTermCode, Integer companyCode) throws AdPSRelativeAmountOverPytBaseAmountException, AdPSRelativeAmountLessPytBaseAmountException {

        Debug.print("AdPaymentScheduleControllerBean addAdPsEntry");

        LocalAdPaymentSchedule adPaymentSchedule;
        LocalAdPaymentTerm adPaymentTerm;
        Collection adPaymentSchedules;
        try {
            adPaymentSchedules = adPaymentScheduleHome.findByPytCode(paymentTermCode, companyCode);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        double totalRelativeAmount = 0.0d;
        for (Object paymentSchedule : adPaymentSchedules) {
            adPaymentSchedule = (LocalAdPaymentSchedule) paymentSchedule;
            totalRelativeAmount += adPaymentSchedule.getPsRelativeAmount();
        }
        try {
            Iterator i = psList.iterator();
            while (i.hasNext()) {
                AdPaymentScheduleDetails details = (AdPaymentScheduleDetails) i.next();
                if (details.getPsCode() == null) {
                    totalRelativeAmount += details.getPsRelativeAmount();
                } else {
                    adPaymentSchedule = adPaymentScheduleHome.findByPrimaryKey(details.getPsCode());
                    totalRelativeAmount += details.getPsRelativeAmount() - adPaymentSchedule.getPsRelativeAmount();
                }
            }
            totalRelativeAmount = EJBCommon.roundIt(totalRelativeAmount, (short) 6);
            adPaymentTerm = adPaymentTermHome.findByPrimaryKey(paymentTermCode);
            if (totalRelativeAmount > adPaymentTerm.getPytBaseAmount()) {
                throw new AdPSRelativeAmountOverPytBaseAmountException();
            } else if (totalRelativeAmount < adPaymentTerm.getPytBaseAmount()) {
                throw new AdPSRelativeAmountLessPytBaseAmountException();
            }
            i = psList.iterator();
            while (i.hasNext()) {
                AdPaymentScheduleDetails details = (AdPaymentScheduleDetails) i.next();
                // create
                if (details.getPsCode() == null) {
                    adPaymentSchedule = adPaymentScheduleHome.create(details.getPsLineNumber(), details.getPsRelativeAmount(), details.getPsDueDay(), companyCode);
                    adPaymentTerm.addAdPaymentSchedule(adPaymentSchedule);
                    // update
                } else {
                    adPaymentSchedule = adPaymentScheduleHome.findByPrimaryKey(details.getPsCode());
                    adPaymentSchedule.setPsLineNumber(details.getPsLineNumber());
                    adPaymentSchedule.setPsRelativeAmount(details.getPsRelativeAmount());
                    adPaymentSchedule.setPsDueDay(details.getPsDueDay());
                }
            }
        } catch (AdPSRelativeAmountOverPytBaseAmountException | AdPSRelativeAmountLessPytBaseAmountException ex) {
            throw ex;
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteAdPsEntry(Integer paymentScheduleCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException {

        Debug.print("AdPaymentScheduleControllerBean deleteAdPsEntry");

        LocalAdPaymentSchedule adPaymentSchedule;
        try {
            adPaymentSchedule = adPaymentScheduleHome.findByPrimaryKey(paymentScheduleCode);
        } catch (FinderException ex) {
            throw new GlobalRecordAlreadyDeletedException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        try {
            em.remove(adPaymentSchedule);
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {
        Debug.print("AdPaymentScheduleControllerBean ejbCreate");
    }
}