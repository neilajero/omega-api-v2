package com.ejb.txn.ad;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.*;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;

import com.ejb.entities.ad.*;
import com.ejb.dao.ad.LocalAdAgingBucketHome;
import com.ejb.exception.global.*;
import com.util.*;
import com.util.ad.AdAgingBucketDetails;

@Stateless(name = "AdAgingBucketControllerEJB")
public class AdAgingBucketControllerBean extends EJBContextClass implements AdAgingBucketController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalAdAgingBucketHome adAgingBucketHome = null;

    public ArrayList getAdAbAll(Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdAgingBucketControllerBean getAdAbAll");

        Collection adAgingBuckets = null;
        LocalAdAgingBucket adAgingBucket = null;
        ArrayList list = new ArrayList();
        try {
            adAgingBuckets = adAgingBucketHome.findAbAll(companyCode);
        } catch (FinderException ex) {
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        if (adAgingBuckets.isEmpty()) {
            throw new GlobalNoRecordFoundException();
        }
        for (Object agingBucket : adAgingBuckets) {
            adAgingBucket = (LocalAdAgingBucket) agingBucket;
            AdAgingBucketDetails details = new AdAgingBucketDetails();
            details.setAbCode(adAgingBucket.getAbCode());
            details.setAbName(adAgingBucket.getAbName());
            details.setAbDescription(adAgingBucket.getAbDescription());
            details.setAbType(adAgingBucket.getAbType());
            details.setAbEnable(adAgingBucket.getAbEnable());
            list.add(details);
        }
        return list;
    }

    public void addAdAbEntry(AdAgingBucketDetails details, Integer companyCode) throws GlobalRecordAlreadyExistException {

        Debug.print("AdAgingBucketControllerBean addAdAbEntry");

        LocalAdAgingBucket adAgingBucket = null;
        ArrayList list = new ArrayList();
        try {
            adAgingBucket = adAgingBucketHome.findByAbName(details.getAbName(), companyCode);
            throw new GlobalRecordAlreadyExistException();
        } catch (GlobalRecordAlreadyExistException ex) {
            throw ex;
        } catch (FinderException ex) {
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        try {
            // create new aging bucket
            adAgingBucket = adAgingBucketHome.create(details.getAbName(), details.getAbDescription(), details.getAbType(), details.getAbEnable(), companyCode);
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateAdAbEntry(AdAgingBucketDetails details, Integer companyCode) throws GlobalRecordAlreadyExistException {

        Debug.print("AdAgingBucketControllerBean updateAdAbEntry");

        LocalAdAgingBucket adAgingBucket;
        ArrayList list = new ArrayList();
        try {
            LocalAdAgingBucket adExistingAgingBucket = adAgingBucketHome.findByAbName(details.getAbName(), companyCode);
            if (!adExistingAgingBucket.getAbCode().equals(details.getAbCode())) {
                throw new GlobalRecordAlreadyExistException();
            }
        } catch (GlobalRecordAlreadyExistException ex) {
            throw ex;
        } catch (FinderException ex) {
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        try {
            // find and update aging bucket
            adAgingBucket = adAgingBucketHome.findByPrimaryKey(details.getAbCode());
            adAgingBucket.setAbName(details.getAbName());
            adAgingBucket.setAbDescription(details.getAbDescription());
            adAgingBucket.setAbType(details.getAbType());
            adAgingBucket.setAbEnable(details.getAbEnable());
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteAdAbEntry(Integer agingBucketCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException {

        Debug.print("AdAgingBucketControllerBean deleteAdAbEntry");

        LocalAdAgingBucket adAgingBucket;
        try {
            adAgingBucket = adAgingBucketHome.findByPrimaryKey(agingBucketCode);
        } catch (FinderException ex) {
            throw new GlobalRecordAlreadyDeletedException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        try {
            if (!adAgingBucket.getAdAgingBucketValues().isEmpty()) {
                throw new GlobalRecordAlreadyAssignedException();
            }
        } catch (GlobalRecordAlreadyAssignedException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        try {
            em.remove(adAgingBucket);
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {
        Debug.print("AdAgingBucketControllerBean ejbCreate");
    }
}