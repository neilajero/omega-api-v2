package com.ejb.dao.ad;

import java.util.Collection;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdLookUpValue;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalAdLookUpValueHome {

  public static final String JNDI_NAME = "LocalAdLookUpValueHome!com.ejb.ad.LocalAdLookUpValueHome";

  @EJB public PersistenceBeanClass em;

  public LocalAdLookUpValueHome() {}

  // FINDER METHODS

  public LocalAdLookUpValue findByPrimaryKey(java.lang.Integer pk)
      throws FinderException {

    try {

      LocalAdLookUpValue entity =
          (LocalAdLookUpValue) em.find(new LocalAdLookUpValue(), pk);
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

  public Collection<LocalAdLookUpValue> findByLuName(
      java.lang.String LU_NM, java.lang.Integer LV_AD_CMPNY) throws FinderException {

    try {
      Query query = em.createQuery("SELECT OBJECT(lv) FROM AdLookUp lu, IN(lu.adLookUpValues) lv WHERE lu.luName = ?1 AND lv.lvAdCompany = ?2");
      query.setParameter(1, LU_NM);
      query.setParameter(2, LV_AD_CMPNY);
      Collection<LocalAdLookUpValue> lists = query.getResultList();
      return lists;
    } catch (Exception ex) {
      Debug.print(
          "EXCEPTION: Exception com.ejb.ad.LocalAdLookUpValueHome.findByLuName(java.lang.String LU_NM, java.lang.Integer LV_AD_CMPNY)");
      throw ex;
    }
  }

  public LocalAdLookUpValue findByLuNameAndLvName(
      java.lang.String LU_NM, java.lang.String LV_NM, java.lang.Integer LV_AD_CMPNY)
      throws FinderException {

    try {
      Query query =
          em.createQuery(
              "SELECT OBJECT(lv) FROM AdLookUp lu, IN(lu.adLookUpValues) lv WHERE lu.luName=?1 AND lv.lvName=?2 AND lv.lvAdCompany = ?3");
      query.setParameter(1, LU_NM);
      query.setParameter(2, LV_NM);
      query.setParameter(3, LV_AD_CMPNY);
        return (LocalAdLookUpValue) query.getSingleResult();
    } catch (NoResultException ex) {
      Debug.print(
          "EXCEPTION: NoResultException com.ejb.ad.LocalAdLookUpValueHome.findByLuNameAndLvName(java.lang.String LU_NM, java.lang.String LV_NM, java.lang.Integer LV_AD_CMPNY)");
      throw new FinderException(ex.getMessage());
    } catch (Exception ex) {
      Debug.print(
          "EXCEPTION: Exception com.ejb.ad.LocalAdLookUpValueHome.findByLuNameAndLvName(java.lang.String LU_NM, java.lang.String LV_NM, java.lang.Integer LV_AD_CMPNY)");
      throw ex;
    }
  }

  public LocalAdLookUpValue findByLuNameAndLvMasterCode(
      java.lang.String LU_NM, java.lang.Integer MSTR_CD, java.lang.Integer LV_AD_CMPNY)
      throws FinderException {

    try {
      Query query =
          em.createQuery(
              "SELECT OBJECT(lv) FROM AdLookUp lu, IN(lu.adLookUpValues) lv WHERE lu.luName=?1 AND lv.lvMasterCode=?2 AND lv.lvAdCompany = ?3");
      query.setParameter(1, LU_NM);
      query.setParameter(2, MSTR_CD);
      query.setParameter(3, LV_AD_CMPNY);
        return (LocalAdLookUpValue) query.getSingleResult();
    } catch (NoResultException ex) {
      Debug.print(
          "EXCEPTION: NoResultException com.ejb.ad.LocalAdLookUpValueHome.findByLuNameAndLvMasterCode(java.lang.String LU_NM, java.lang.Integer MSTR_CD, java.lang.Integer LV_AD_CMPNY)");
      throw new FinderException(ex.getMessage());
    } catch (Exception ex) {
      Debug.print(
          "EXCEPTION: Exception com.ejb.ad.LocalAdLookUpValueHome.findByLuNameAndLvMasterCode(java.lang.String LU_NM, java.lang.Integer MSTR_CD, java.lang.Integer LV_AD_CMPNY)");
      throw ex;
    }
  }

  public LocalAdLookUpValue findByLvCodeAndLuName(
      java.lang.Integer LV_CODE, java.lang.String LU_NM, java.lang.Integer LV_AD_CMPNY)
      throws FinderException {

    try {
      Query query =
          em.createQuery(
              "SELECT OBJECT(lv) FROM AdLookUp lu, IN(lu.adLookUpValues) lv WHERE lv.lvCode=?1 AND lu.luName=?2 AND lv.lvAdCompany = ?3");
      query.setParameter(1, LV_CODE);
      query.setParameter(2, LU_NM);
      query.setParameter(3, LV_AD_CMPNY);
        return (LocalAdLookUpValue) query.getSingleResult();
    } catch (NoResultException ex) {
      Debug.print(
          "EXCEPTION: NoResultException com.ejb.ad.LocalAdLookUpValueHome.findByLvCodeAndLuName(java.lang.Integer LV_CODE, java.lang.String LU_NM, java.lang.Integer LV_AD_CMPNY)");
      throw new FinderException(ex.getMessage());
    } catch (Exception ex) {
      Debug.print(
          "EXCEPTION: Exception com.ejb.ad.LocalAdLookUpValueHome.findByLvCodeAndLuName(java.lang.Integer LV_CODE, java.lang.String LU_NM, java.lang.Integer LV_AD_CMPNY)");
      throw ex;
    }
  }

  public LocalAdLookUpValue findByLvName(java.lang.String LV_NM, java.lang.Integer LV_AD_CMPNY)
      throws FinderException {

    try {
      Query query =
          em.createQuery(
              "SELECT OBJECT(lv) FROM AdLookUpValue lv WHERE lv.lvName = ?1 AND lv.lvAdCompany = ?2");
      query.setParameter(1, LV_NM);
      query.setParameter(2, LV_AD_CMPNY);
        return (LocalAdLookUpValue) query.getSingleResult();
    } catch (NoResultException ex) {
      Debug.print(
          "EXCEPTION: NoResultException com.ejb.ad.LocalAdLookUpValueHome.findByLvName(java.lang.String LV_NM, java.lang.Integer LV_AD_CMPNY)");
      throw new FinderException(ex.getMessage());
    } catch (Exception ex) {
      Debug.print(
          "EXCEPTION: Exception com.ejb.ad.LocalAdLookUpValueHome.findByLvName(java.lang.String LV_NM, java.lang.Integer LV_AD_CMPNY)");
      throw ex;
    }
  }

  public Collection findByLvCodeAndLvDownloadStatus(
      java.lang.Integer LV_CODE, char LV_DWNLD_STATUS, java.lang.Integer LV_AD_CMPNY)
      throws FinderException {

    try {
      Query query =
          em.createQuery(
              "SELECT OBJECT(lv) FROM AdLookUpValue lv WHERE lv.lvCode = ?1 AND lv.lvDownloadStatus = ?2 AND lv.lvAdCompany = ?3");
      query.setParameter(1, LV_CODE);
      query.setParameter(2, LV_DWNLD_STATUS);
      query.setParameter(3, LV_AD_CMPNY);
        return query.getResultList();
    } catch (Exception ex) {
      Debug.print(
          "EXCEPTION: Exception com.ejb.ad.LocalAdLookUpValueHome.findByLvCodeAndLvDownloadStatus(java.lang.Integer LV_CODE, char LV_DWNLD_STATUS, java.lang.Integer LV_AD_CMPNY)");
      throw ex;
    }
  }

  public Collection findEnabledLvByLvNewAndUpdated(
      java.lang.String LU_NM,
      java.lang.Integer LV_AD_CMPNY,
      char NEW,
      char UPDATED,
      char DOWNLOADED_UPDATED)
      throws FinderException {

    try {
      Query query =
          em.createQuery(
              "SELECT OBJECT(lv) FROM AdLookUp lu, IN(lu.adLookUpValues) lv WHERE lu.luName = ?1 AND lv.lvAdCompany = ?2 AND lv.lvEnable = 1 AND (lv.lvDownloadStatus = ?3 OR lv.lvDownloadStatus = ?4 OR lv.lvDownloadStatus = ?5)");
      query.setParameter(1, LU_NM);
      query.setParameter(2, LV_AD_CMPNY);
      query.setParameter(3, NEW);
      query.setParameter(4, UPDATED);
      query.setParameter(5, DOWNLOADED_UPDATED);
        return query.getResultList();
    } catch (Exception ex) {
      Debug.print(
          "EXCEPTION: Exception com.ejb.ad.LocalAdLookUpValueHome.findEnabledLvByLvNewAndUpdated(java.lang.String LU_NM, java.lang.Integer LV_AD_CMPNY, char NEW, char UPDATED, char DOWNLOADED_UPDATED)");
      throw ex;
    }
  }

  // OTHER METHODS

  // CREATE METHODS

  public LocalAdLookUpValue create(
      Integer LV_CODE,
      String LV_NM,
      String LV_DESC,
      Integer LV_MSTR_CD,
      byte LV_ENBL,
      char LV_DWNLD_STATUS,
      Integer LV_AD_CMPNY)
      throws CreateException {
    try {

      LocalAdLookUpValue entity = new LocalAdLookUpValue();

      Debug.print("AdLookUpValueBean create");
      entity.setLvCode(LV_CODE);
      entity.setLvName(LV_NM);
      entity.setLvDescription(LV_DESC);
      entity.setLvMasterCode(LV_MSTR_CD);
      entity.setLvEnable(LV_ENBL);
      entity.setLvDownloadStatus(LV_DWNLD_STATUS);
      entity.setLvAdCompany(LV_AD_CMPNY);

      em.persist(entity);
      return entity;

    } catch (Exception ex) {
      throw new CreateException(ex.getMessage());
    }
  }

  public LocalAdLookUpValue create(
      String LV_NM,
      String LV_DESC,
      Integer LV_MSTR_CD,
      byte LV_ENBL,
      char LV_DWNLD_STATUS,
      Integer LV_AD_CMPNY)
      throws CreateException {
    try {

      LocalAdLookUpValue entity = new LocalAdLookUpValue();

      Debug.print("AdLookUpValueBean create");
      entity.setLvName(LV_NM);
      entity.setLvDescription(LV_DESC);
      entity.setLvMasterCode(LV_MSTR_CD);
      entity.setLvEnable(LV_ENBL);
      entity.setLvDownloadStatus(LV_DWNLD_STATUS);
      entity.setLvAdCompany(LV_AD_CMPNY);

      em.persist(entity);
      return entity;

    } catch (Exception ex) {
      throw new CreateException(ex.getMessage());
    }
  }
}