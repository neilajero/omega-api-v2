package com.ejb.txn.ad;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.*;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;

import com.ejb.dao.ad.*;
import com.ejb.entities.ad.*;
import com.ejb.exception.global.*;
import com.util.*;
import com.util.ad.AdAgingBucketDetails;
import com.util.ad.AdAgingBucketValueDetails;

@Stateless(name = "AdAgingBucketValueControllerEJB")
public class AdAgingBucketValueControllerBean extends EJBContextClass implements AdAgingBucketValueController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalAdAgingBucketValueHome adAgingBucketValueHome = null;
    @EJB
    private LocalAdAgingBucketHome adAgingBucketHome = null;

    public ArrayList getAdAvByAbCode(Integer agingBucketCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdAgingBucketValueControllerBean getAdAvByAbCode");

        Collection adAgingBucketValues = null;
        LocalAdAgingBucketValue adAgingBucketValue;
        ArrayList list = new ArrayList();
        try {
            adAgingBucketValues = adAgingBucketValueHome.findByAbCode(agingBucketCode, companyCode);
        } catch (FinderException ex) {
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        if (adAgingBucketValues.isEmpty()) {
            throw new GlobalNoRecordFoundException();
        }
        for (Object agingBucketValue : adAgingBucketValues) {
            adAgingBucketValue = (LocalAdAgingBucketValue) agingBucketValue;
            AdAgingBucketValueDetails details = new AdAgingBucketValueDetails();
            details.setAvCode(adAgingBucketValue.getAvCode());
            details.setAvSequenceNumber(adAgingBucketValue.getAvSequenceNumber());
            details.setAvType(adAgingBucketValue.getAvType());
            details.setAvDaysFrom(adAgingBucketValue.getAvDaysFrom());
            details.setAvDaysTo(adAgingBucketValue.getAvDaysTo());
            details.setAvDescription(adAgingBucketValue.getAvDescription());
            list.add(details);
        }
        return list;
    }

    public AdAgingBucketDetails getAdAbByAbCode(Integer agingBucketCode, Integer companyCode) {

        Debug.print("AdAgingBucketValueControllerBean getAdAbByAbCode");

        try {
            LocalAdAgingBucket adAgingBucket = adAgingBucketHome.findByPrimaryKey(agingBucketCode);
            AdAgingBucketDetails details = new AdAgingBucketDetails();
            details.setAbCode(adAgingBucket.getAbCode());
            details.setAbName(adAgingBucket.getAbName());
            details.setAbDescription(adAgingBucket.getAbDescription());
            details.setAbType(adAgingBucket.getAbType());
            details.setAbEnable(adAgingBucket.getAbEnable());
            return details;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public void addAdAvEntry(AdAgingBucketValueDetails details, Integer agingBucketCode, Integer companyCode) throws GlobalRecordAlreadyExistException {

        Debug.print("AdAgingBucketValueControllerBean addAdAvEntry");

        LocalAdAgingBucketValue adAgingBucketValue;
        LocalAdAgingBucket adAgingBucket;
        try {
            adAgingBucketValue = adAgingBucketValueHome.findByAvSequenceNumberAndAbCode(details.getAvSequenceNumber(), agingBucketCode, companyCode);
            throw new GlobalRecordAlreadyExistException();
        } catch (GlobalRecordAlreadyExistException ex) {
            throw ex;
        } catch (FinderException ex) {
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        try {

            // create new aging bucket values
            adAgingBucketValue = adAgingBucketValueHome.create(details.getAvSequenceNumber(), details.getAvType(), details.getAvDaysFrom(), details.getAvDaysTo(), details.getAvDescription(), companyCode);
            adAgingBucket = adAgingBucketHome.findByPrimaryKey(agingBucketCode);
            adAgingBucket.addAdAgingBucketValue(adAgingBucketValue);

        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateAdAvEntry(AdAgingBucketValueDetails details, Integer agingBucketCode, Integer companyCode) throws GlobalRecordAlreadyExistException {

        Debug.print("AdAgingBucketValueControllerBean updateArAvEntry");

        LocalAdAgingBucketValue adAgingBucketValue;
        LocalAdAgingBucket adAgingBucket;
        try {
            adAgingBucketValue = adAgingBucketValueHome.findByAvSequenceNumberAndAbCode(details.getAvSequenceNumber(), agingBucketCode, companyCode);
            if (adAgingBucketValue != null && !adAgingBucketValue.getAvCode().equals(details.getAvCode())) {
                throw new GlobalRecordAlreadyExistException();
            }
        } catch (GlobalRecordAlreadyExistException ex) {
            throw ex;
        } catch (FinderException ex) {
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        // Find and Update Aging Bucket Value
        try {
            adAgingBucketValue = adAgingBucketValueHome.findByPrimaryKey(details.getAvCode());
            adAgingBucketValue.setAvSequenceNumber(details.getAvSequenceNumber());
            adAgingBucketValue.setAvType(details.getAvType());
            adAgingBucketValue.setAvDaysFrom(details.getAvDaysFrom());
            adAgingBucketValue.setAvDaysTo(details.getAvDaysTo());
            adAgingBucketValue.setAvDescription(details.getAvDescription());
            adAgingBucket = adAgingBucketHome.findByPrimaryKey(agingBucketCode);
            adAgingBucket.addAdAgingBucketValue(adAgingBucketValue);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteAdAvEntry(Integer agingBucketValueCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException {

        Debug.print("AdAgingBucketValueControllerBean deleteAdAvEntry");

        LocalAdAgingBucketValue adAgingBucketValue;
        try {
            adAgingBucketValue = adAgingBucketValueHome.findByPrimaryKey(agingBucketValueCode);
        } catch (FinderException ex) {
            throw new GlobalRecordAlreadyDeletedException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        try {
            em.remove(adAgingBucketValue);
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {
        Debug.print("AdAgingBucketValueControllerBean ejbCreate");
    }
}