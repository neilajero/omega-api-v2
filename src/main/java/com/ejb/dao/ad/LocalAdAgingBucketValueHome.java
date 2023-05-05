package com.ejb.dao.ad;

import java.util.Collection;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdAgingBucketValue;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import com.util.Debug;

@Stateless
public class LocalAdAgingBucketValueHome {

  public static final String JNDI_NAME = "LocalAdAgingBucketValueHome!com.ejb.ad.LocalAdAgingBucketValueHome";

  @EJB public PersistenceBeanClass em;

  public LocalAdAgingBucketValueHome() {}

  // FINDER METHODS
  public LocalAdAgingBucketValue findByPrimaryKey(java.lang.Integer pk)
      throws FinderException {

    try {

      LocalAdAgingBucketValue entity =
          (LocalAdAgingBucketValue)
              em.find(new LocalAdAgingBucketValue(), pk);
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

  public Collection<LocalAdAgingBucketValue> findByAbCode(Integer AB_CODE, Integer AV_AD_CMPNY)
      throws FinderException {

    try {
      Query query =
          em.createQuery(
              "SELECT OBJECT(av) FROM AdAgingBucket ab, IN(ab.adAgingBucketValues) av WHERE ab.abCode = ?1 AND av.avAdCompany = ?2");
      query.setParameter(1, AB_CODE);
      query.setParameter(2, AV_AD_CMPNY);

      @SuppressWarnings("unchecked")
      Collection<LocalAdAgingBucketValue> lists = query.getResultList();
      return lists;
    } catch (Exception ex) {
      Debug.print(
          "EXCEPTION: Exception com.ejb.ad.LocalAdAgingBucketValueHome.findByAbCode(java.lang.Integer AB_CODE, java.lang.Integer AV_AD_CMPNY)");
      throw ex;
    }
  }

  public Collection<LocalAdAgingBucketValue> findByAbName(
      java.lang.String AB_NM, java.lang.Integer AV_AD_CMPNY) throws FinderException {

    try {
      Query query =
          em.createQuery(
              "SELECT OBJECT(av) FROM AdAgingBucket ab, IN(ab.adAgingBucketValues) av WHERE ab.abName = ?1 AND av.avAdCompany = ?2 ");
      query.setParameter(1, AB_NM);
      query.setParameter(2, AV_AD_CMPNY);

      @SuppressWarnings("unchecked")
      Collection<LocalAdAgingBucketValue> lists = query.getResultList();
      return lists;
    } catch (Exception ex) {
      Debug.print(
          "EXCEPTION: Exception com.ejb.ad.LocalAdAgingBucketValueHome.findByAbName(java.lang.String AB_NM, java.lang.Integer AV_AD_CMPNY)");
      throw ex;
    }
  }

  public LocalAdAgingBucketValue findByAvSequenceNumberAndAbCode(
      short AV_SQNC_NMBR, java.lang.Integer AB_CODE, java.lang.Integer AV_AD_CMPNY)
      throws FinderException {

    try {
      Query query =
          em.createQuery(
              "SELECT OBJECT(av) FROM AdAgingBucket ab, IN(ab.adAgingBucketValues) av WHERE av.avSequenceNumber=?1 AND ab.abCode=?2 AND av.avAdCompany = ?3");
      query.setParameter(1, AV_SQNC_NMBR);
      query.setParameter(2, AB_CODE);
      query.setParameter(3, AV_AD_CMPNY);
        return (LocalAdAgingBucketValue) query.getSingleResult();
    } catch (NoResultException ex) {
      Debug.print(
          "EXCEPTION: NoResultException com.ejb.ad.LocalAdAgingBucketValueHome.findByAvSequenceNumberAndAbCode(short AV_SQNC_NMBR, java.lang.Integer AB_CODE, java.lang.Integer AV_AD_CMPNY)");
      throw new FinderException(ex.getMessage());
    } catch (Exception ex) {
      Debug.print(
          "EXCEPTION: Exception com.ejb.ad.LocalAdAgingBucketValueHome.findByAvSequenceNumberAndAbCode(short AV_SQNC_NMBR, java.lang.Integer AB_CODE, java.lang.Integer AV_AD_CMPNY)");
      throw ex;
    }
  }

  // CREATE METHODS
  public LocalAdAgingBucketValue create(
      Integer AD_AV_CODE,
      short AV_SQNC_NMBR,
      String AV_TYP,
      long AV_DYS_FRM,
      long AV_DYS_TO,
      String AV_DESC,
      Integer AV_AD_CMPNY)
      throws CreateException {
    try {

      LocalAdAgingBucketValue entity = new LocalAdAgingBucketValue();

      Debug.print("AdAgingBucketValueBean create");

      entity.setAvCode(AD_AV_CODE);
      entity.setAvSequenceNumber(AV_SQNC_NMBR);
      entity.setAvType(AV_TYP);
      entity.setAvDaysFrom(AV_DYS_FRM);
      entity.setAvDaysTo(AV_DYS_TO);
      entity.setAvDescription(AV_DESC);
      entity.setAvAdCompany(AV_AD_CMPNY);

      em.persist(entity);
      return entity;

    } catch (Exception ex) {
      throw new CreateException(ex.getMessage());
    }
  }

  public LocalAdAgingBucketValue create(
      short AV_SQNC_NMBR,
      String AV_TYP,
      long AV_DYS_FRM,
      long AV_DYS_TO,
      String AV_DESC,
      Integer AV_AD_CMPNY)
      throws CreateException {
    try {

      LocalAdAgingBucketValue entity = new LocalAdAgingBucketValue();

      Debug.print("AdAgingBucketValueBean create");

      entity.setAvSequenceNumber(AV_SQNC_NMBR);
      entity.setAvType(AV_TYP);
      entity.setAvDaysFrom(AV_DYS_FRM);
      entity.setAvDaysTo(AV_DYS_TO);
      entity.setAvDescription(AV_DESC);
      entity.setAvAdCompany(AV_AD_CMPNY);

      em.persist(entity);
      return entity;

    } catch (Exception ex) {
      throw new CreateException(ex.getMessage());
    }
  }
}