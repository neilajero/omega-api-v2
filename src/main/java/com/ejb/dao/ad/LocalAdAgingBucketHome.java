package com.ejb.dao.ad;

import java.util.Collection;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdAgingBucket;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalAdAgingBucketHome {

    public static final String JNDI_NAME = "LocalAdAgingBucketHome!com.ejb.ad.LocalAdAgingBucketHome";

    @EJB
    public PersistenceBeanClass em;

    public LocalAdAgingBucketHome() {
    }

    // FINDER METHODS
    public LocalAdAgingBucket findByPrimaryKey(java.lang.Integer pk) throws FinderException {
        try {
            LocalAdAgingBucket entity = (LocalAdAgingBucket) em.find(new LocalAdAgingBucket(), pk);
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

    public Collection<LocalAdAgingBucket> findAbAll(java.lang.Integer AB_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery("SELECT OBJECT(ab) FROM AdAgingBucket ab WHERE ab.abAdCompany = ?1");
            query.setParameter(1, AB_AD_CMPNY);

            @SuppressWarnings("unchecked")
            Collection<LocalAdAgingBucket> lists = query.getResultList();
            return lists;
        } catch (Exception ex) {
            Debug.print("EXCEPTION: Exception com.ejb.ad.LocalAdAgingBucketHome.findAbAll(java.lang.Integer AB_AD_CMPNY)");
            throw ex;
        }
    }

    public Collection<?> findEnabledAbAll(java.lang.Integer AB_AD_CMPNY) throws FinderException {

        try {
            Query query =
                    em.createQuery(
                            "SELECT OBJECT(ab) FROM AdAgingBucket ab WHERE ab.abEnable=1 AND ab.abAdCompany = ?1");
            query.setParameter(1, AB_AD_CMPNY);
            return (Collection<?>) query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ad.LocalAdAgingBucketHome.findEnabledAbAll(java.lang.Integer AB_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalAdAgingBucket findByAbName(java.lang.String AB_NM, java.lang.Integer AB_AD_CMPNY)
            throws FinderException {

        try {
            Query query =
                    em.createQuery(
                            "SELECT OBJECT(ab) FROM AdAgingBucket ab WHERE ab.abName = ?1 AND ab.abAdCompany = ?2");
            query.setParameter(1, AB_NM);
            query.setParameter(2, AB_AD_CMPNY);
            return (LocalAdAgingBucket) query.getSingleResult();
        } catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.ad.LocalAdAgingBucketHome.findByAbName(java.lang.String AB_NM, java.lang.Integer AB_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ad.LocalAdAgingBucketHome.findByAbName(java.lang.String AB_NM, java.lang.Integer AB_AD_CMPNY)");
            throw ex;
        }
    }

    // CREATE METHODS
    public LocalAdAgingBucket create(
            Integer AB_CODE,
            String AB_NM,
            String AB_DESC,
            String AB_TYP,
            byte AB_ENBL,
            Integer AB_AD_CMPNY)
            throws CreateException {
        try {

            LocalAdAgingBucket entity = new LocalAdAgingBucket();
            Debug.print("AdAgingBucketBean create");
            entity.setAbCode(AB_CODE);
            entity.setAbName(AB_NM);
            entity.setAbDescription(AB_DESC);
            entity.setAbType(AB_TYP);
            entity.setAbEnable(AB_ENBL);
            entity.setAbAdCompany(AB_AD_CMPNY);
            em.persist(entity);
            return entity;
        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalAdAgingBucket create(
            String AB_NM, String AB_DESC, String AB_TYP, byte AB_ENBL, Integer AB_AD_CMPNY)
            throws CreateException {
        try {
            LocalAdAgingBucket entity = new LocalAdAgingBucket();
            Debug.print("AdLookUpBean create");
            entity.setAbName(AB_NM);
            entity.setAbDescription(AB_DESC);
            entity.setAbType(AB_TYP);
            entity.setAbEnable(AB_ENBL);
            entity.setAbAdCompany(AB_AD_CMPNY);
            em.persist(entity);
            return entity;
        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }
}