package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdLookUp;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalAdLookUpHome {

    public static final String JNDI_NAME = "LocalAdLookUpHome!com.ejb.ad.LocalAdLookUpHome";

    @EJB
    public PersistenceBeanClass em;

    public LocalAdLookUpHome() { }

    // FINDER METHODS
    public LocalAdLookUp findByPrimaryKey(Integer pk) throws FinderException {
        try {
            LocalAdLookUp entity = (LocalAdLookUp) em.find(new LocalAdLookUp(), pk);
            if (entity == null) {
                throw new FinderException();
            }
            return entity;
        } catch (FinderException ex) {
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection findLuAll(Integer companyCode) throws FinderException {
        try {
            Query query = em.createQuery("SELECT OBJECT(lu) FROM AdLookUp lu WHERE lu.luAdCompany = ?1");
            query.setParameter(1, companyCode);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print("EXCEPTION: Exception LocalAdLookUpHome.findLuAll(Integer companyCode)");
            throw ex;
        }
    }

    public LocalAdLookUp findByLookUpName(String lookUpName, Integer companyCode) throws FinderException {
        try {
            Query query = em.createQuery("SELECT OBJECT(lu) FROM AdLookUp lu WHERE lu.luName = ?1 AND lu.luAdCompany = ?2");
            query.setParameter(1, lookUpName);
            query.setParameter(2, companyCode);
            return (LocalAdLookUp) query.getSingleResult();
        } catch (NoResultException ex) {
            Debug.print("EXCEPTION: NoResultException LocalAdLookUpHome.findByLookUpName(String lookUpName, Integer companyCode)");
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            Debug.print("EXCEPTION: Exception LocalAdLookUpHome.findByLookUpName(String lookUpName, Integer companyCode)");
            throw ex;
        }
    }

    // CREATE METHODS
    public LocalAdLookUp create(Integer lookUpCode, String lookUpName, String lookUpDesc, Integer companyCode) throws CreateException {
        try {
            LocalAdLookUp entity = new LocalAdLookUp();
            Debug.print("AdLookUpBean create");
            entity.setLuCode(lookUpCode);
            entity.setLuName(lookUpName);
            entity.setLuDescription(lookUpDesc);
            entity.setLuAdCompany(companyCode);
            em.persist(entity);
            return entity;
        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalAdLookUp create(String lookUpName, String lookUpDesc, Integer companyCode) throws CreateException {
        try {
            LocalAdLookUp entity = new LocalAdLookUp();
            Debug.print("AdLookUpBean create");
            entity.setLuName(lookUpName);
            entity.setLuDescription(lookUpDesc);
            entity.setLuAdCompany(companyCode);
            em.persist(entity);
            return entity;
        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }
}