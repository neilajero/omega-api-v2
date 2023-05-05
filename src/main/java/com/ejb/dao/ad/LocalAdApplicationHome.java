package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdApplication;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalAdApplicationHome {

  public static final String JNDI_NAME = "LocalAdApplicationHome!com.ejb.ad.LocalAdApplicationHome";

  @EJB public PersistenceBeanClass em;

  public LocalAdApplicationHome() {}

  // FINDER METHODS

  public LocalAdApplication findByPrimaryKey(java.lang.Integer pk)
      throws FinderException {

    try {

      LocalAdApplication entity =
          (LocalAdApplication) em.find(new LocalAdApplication(), pk);
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

  public java.util.Collection findAppAll(java.lang.Integer APP_AD_CMPNY) throws FinderException {

    try {
      Query query =
          em.createQuery("SELECT OBJECT(app) FROM AdApplication app WHERE app.appAdCompany = ?1");
      query.setParameter(1, APP_AD_CMPNY);
        return query.getResultList();
    } catch (Exception ex) {
      Debug.print(
          "EXCEPTION: Exception com.ejb.gl.LocalAdApplicationHome.findAppAll(java.lang.Integer APP_AD_CMPNY)");
      throw ex;
    }
  }

  public java.util.Collection findAppAllInstalled(java.lang.Integer APP_AD_CMPNY)
      throws FinderException {

    try {
      Query query =
          em.createQuery(
              "SELECT OBJECT(app) FROM AdApplication app WHERE app.appInstalled = 1 AND app.appAdCompany = ?1");
      query.setParameter(1, APP_AD_CMPNY);
        return query.getResultList();
    } catch (Exception ex) {
      Debug.print(
          "EXCEPTION: Exception com.ejb.gl.LocalAdApplicationHome.findAppAllInstalled(java.lang.Integer APP_AD_CMPNY)");
      throw ex;
    }
  }

  public LocalAdApplication findByAppName(java.lang.String APP_NM, java.lang.Integer APP_AD_CMPNY)
      throws FinderException {

    try {
      Query query =
          em.createQuery(
              "SELECT OBJECT(app) FROM AdApplication app WHERE app.appName=?1 AND app.appAdCompany = ?2");
      query.setParameter(1, APP_NM);
      query.setParameter(2, APP_AD_CMPNY);
        return (LocalAdApplication) query.getSingleResult();
    } catch (NoResultException ex) {
      Debug.print(
          "EXCEPTION: NoResultException com.ejb.gl.LocalAdApplicationHome.findByAppName(java.lang.String APP_NM, java.lang.Integer APP_AD_CMPNY)");
      throw new FinderException(ex.getMessage());
    } catch (Exception ex) {
      Debug.print(
          "EXCEPTION: Exception com.ejb.gl.LocalAdApplicationHome.findByAppName(java.lang.String APP_NM, java.lang.Integer APP_AD_CMPNY)");
      throw ex;
    }
  }

  // OTHER METHODS

  // CREATE METHODS

  public LocalAdApplication create(
      java.lang.Integer APP_CODE,
      java.lang.String APP_NM,
      java.lang.String APP_DESC,
      byte APP_INSTLLD,
      Integer APP_AD_CMPNY)
      throws CreateException {
    try {

      LocalAdApplication entity = new LocalAdApplication();

      Debug.print("AdApplication create");

      entity.setAppCode(APP_CODE);
      entity.setAppName(APP_NM);
      entity.setAppDescription(APP_DESC);
      entity.setAppInstalled(APP_INSTLLD);
      entity.setAppAdCompany(APP_AD_CMPNY);

      em.persist(entity);
      return entity;

    } catch (Exception ex) {
      throw new CreateException(ex.getMessage());
    }
  }

  public LocalAdApplication create(
      java.lang.String APP_NM, java.lang.String APP_DESC, byte APP_INSTLLD, Integer APP_AD_CMPNY)
      throws CreateException {
    try {

      LocalAdApplication entity = new LocalAdApplication();

      Debug.print("AdApplication create");

      entity.setAppName(APP_NM);
      entity.setAppDescription(APP_DESC);
      entity.setAppInstalled(APP_INSTLLD);
      entity.setAppAdCompany(APP_AD_CMPNY);

      em.persist(entity);
      return entity;

    } catch (Exception ex) {
      throw new CreateException(ex.getMessage());
    }
  }
}