package com.ejb.dao.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.inv.LocalInvPriceLevel;
import com.util.Debug;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

@Stateless
public class LocalInvPriceLevelHome {

    public static final String JNDI_NAME = "LocalInvPriceLevelHome!com.ejb.inv.LocalInvPriceLevelHome";

    @EJB
    public PersistenceBeanClass em;
    private double PL_AMNT = 0d;
    private double PL_MRGN = 0d;
    private double PL_PRCNT_MRKUP = 0d;
    private double PL_SHPPNG_CST = 0d;
    private String PL_AD_LV_PRC_LVL = null;
    private char PL_DWNLD_STATUS = 'N';
    private Integer PL_AD_CMPNY = null;

    public LocalInvPriceLevelHome() {

    }

    // FINDER METHODS

    public LocalInvPriceLevel findByPrimaryKey(java.lang.Integer pk) throws FinderException {

        try {

            LocalInvPriceLevel entity = (LocalInvPriceLevel) em
                    .find(new LocalInvPriceLevel(), pk);
            if (entity == null) {
                throw new FinderException();
            }
            return entity;
        }
        catch (FinderException ex) {
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection findByIiCode(java.lang.Integer II_CODE, java.lang.Integer PL_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(pl) FROM InvPriceLevel pl WHERE pl.invItem.iiCode=?1 AND pl.plAdCompany=?2");
            query.setParameter(1, II_CODE);
            query.setParameter(2, PL_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvPriceLevelHome.findByIiCode(java.lang.Integer II_CODE, java.lang.Integer PL_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByPlAdLvPriceLevel(java.lang.String PL_AD_LV_PRC_LVL, java.lang.Integer PL_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(pl) FROM InvPriceLevel pl WHERE pl.plAdLvPriceLevel=?1 AND pl.plAdCompany=?2");
            query.setParameter(1, PL_AD_LV_PRC_LVL);
            query.setParameter(2, PL_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvPriceLevelHome.findByPlAdLvPriceLevel(java.lang.String PL_AD_LV_PRC_LVL, java.lang.Integer PL_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalInvPriceLevel findByIiNameAndAdLvPriceLevel(java.lang.String II_NM, java.lang.String PL_AD_LV_PRC_LVL,
                                                            java.lang.Integer PL_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(pl) FROM InvPriceLevel pl WHERE pl.invItem.iiName=?1 AND pl.plAdLvPriceLevel=?2 AND pl.plAdCompany=?3");
            query.setParameter(1, II_NM);
            query.setParameter(2, PL_AD_LV_PRC_LVL);
            query.setParameter(3, PL_AD_CMPNY);
            return (LocalInvPriceLevel) query.getSingleResult();
        }
        catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.inv.LocalInvPriceLevelHome.findByIiNameAndAdLvPriceLevel(java.lang.String II_NM, java.lang.String PL_AD_LV_PRC_LVL, java.lang.Integer PL_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvPriceLevelHome.findByIiNameAndAdLvPriceLevel(java.lang.String II_NM, java.lang.String PL_AD_LV_PRC_LVL, java.lang.Integer PL_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByPlCodeAndPlDownloadStatus(java.lang.Integer INV_PL_CODE, char PL_DWNLD_STATUS,
                                                                java.lang.Integer PL_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(pl) FROM InvPriceLevel pl WHERE pl.plCode = ?1 AND pl.plDownloadStatus = ?2 AND pl.plAdCompany = ?3");
            query.setParameter(1, INV_PL_CODE);
            query.setParameter(2, PL_DWNLD_STATUS);
            query.setParameter(3, PL_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvPriceLevelHome.findByPlCodeAndPlDownloadStatus(java.lang.Integer INV_PL_CODE, char PL_DWNLD_STATUS, java.lang.Integer PL_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findPlByPlNewAndUpdated(java.lang.Integer PL_AD_CMPNY, char NEW, char UPDATED,
                                                        char DOWNLOADED_UPDATED) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(pl) FROM InvPriceLevel pl WHERE pl.plAdCompany = ?1 AND (pl.plDownloadStatus = ?2 OR pl.plDownloadStatus = ?3 OR pl.plDownloadStatus = ?4)");
            query.setParameter(1, PL_AD_CMPNY);
            query.setParameter(2, NEW);
            query.setParameter(3, UPDATED);
            query.setParameter(4, DOWNLOADED_UPDATED);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvPriceLevelHome.findPlByPlNewAndUpdated(java.lang.Integer PL_AD_CMPNY, char NEW, char UPDATED, char DOWNLOADED_UPDATED)");
            throw ex;
        }
    }

    public java.util.Collection getPlByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
            throws FinderException {

        try {
            Query query = em.createQuery(jbossQl);
            int cnt = 1;
            for (Object data : args) {
                query.setParameter(cnt, data);
                cnt++;
            }
            return query.getResultList();
        }
        catch (Exception ex) {
            throw ex;
        }
    }


    public LocalInvPriceLevel buildPriceLevel() throws CreateException {

        try {

            LocalInvPriceLevel entity = new LocalInvPriceLevel();

            Debug.print("InvPriceLevelBean buildPriceLevel");
            entity.setPlAmount(PL_AMNT);
            entity.setPlMargin(PL_MRGN);
            entity.setPlPercentMarkup(PL_PRCNT_MRKUP);
            entity.setPlShippingCost(PL_SHPPNG_CST);
            entity.setPlAdLvPriceLevel(PL_AD_LV_PRC_LVL);
            entity.setPlDownloadStatus(PL_DWNLD_STATUS);
            entity.setPlAdCompany(PL_AD_CMPNY);

            em.persist(entity);
            return entity;

        }
        catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalInvPriceLevelHome PlAmount(double PL_AMNT) {

        this.PL_AMNT = PL_AMNT;
        return this;
    }

    public LocalInvPriceLevelHome PlMargin(double PL_MRGN) {

        this.PL_AMNT = PL_AMNT;
        return this;
    }

    public LocalInvPriceLevelHome PlPercentMarkup(double PL_PRCNT_MRKUP) {

        this.PL_PRCNT_MRKUP = PL_PRCNT_MRKUP;
        return this;
    }

    public LocalInvPriceLevelHome PlShippingCost(double PL_SHPPNG_CST) {

        this.PL_SHPPNG_CST = PL_SHPPNG_CST;
        return this;
    }

    public LocalInvPriceLevelHome PlAdLvPriceLevel(String PL_AD_LV_PRC_LVL) {

        this.PL_AD_LV_PRC_LVL = PL_AD_LV_PRC_LVL;
        return this;
    }

    public LocalInvPriceLevelHome PlDownloadStatus(char PL_DWNLD_STATUS) {

        this.PL_DWNLD_STATUS = PL_DWNLD_STATUS;
        return this;
    }

    public LocalInvPriceLevelHome PlAdCompany(Integer PL_AD_CMPNY) {

        this.PL_AD_CMPNY = PL_AD_CMPNY;
        return this;
    }

    public LocalInvPriceLevel create(java.lang.Integer INV_PL_CODE, double PL_AMNT, double PL_MRGN,
                                     double PL_PRCNT_MRKUP, double PL_SHPPNG_CST, java.lang.String PL_AD_LV_PRC_LVL, char PL_DWNLD_STATUS,
                                     java.lang.Integer PL_AD_CMPNY) throws CreateException {

        try {

            LocalInvPriceLevel entity = new LocalInvPriceLevel();

            Debug.print("InvPriceLevelBean create");
            entity.setPlCode(INV_PL_CODE);
            entity.setPlAmount(PL_AMNT);
            entity.setPlMargin(PL_MRGN);
            entity.setPlPercentMarkup(PL_PRCNT_MRKUP);
            entity.setPlShippingCost(PL_SHPPNG_CST);
            entity.setPlAdLvPriceLevel(PL_AD_LV_PRC_LVL);
            entity.setPlDownloadStatus(PL_DWNLD_STATUS);
            entity.setPlAdCompany(PL_AD_CMPNY);

            em.persist(entity);
            return entity;

        }
        catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalInvPriceLevel create(double PL_AMNT, double PL_MRGN, double PL_PRCNT_MRKUP,
                                     double PL_SHPPNG_CST, java.lang.String PL_AD_LV_PRC_LVL, char PL_DWNLD_STATUS,
                                     java.lang.Integer PL_AD_CMPNY) throws CreateException {

        try {

            LocalInvPriceLevel entity = new LocalInvPriceLevel();

            Debug.print("InvPriceLevelBean create");
            entity.setPlAmount(PL_AMNT);
            entity.setPlMargin(PL_MRGN);
            entity.setPlPercentMarkup(PL_PRCNT_MRKUP);
            entity.setPlShippingCost(PL_SHPPNG_CST);
            entity.setPlAdLvPriceLevel(PL_AD_LV_PRC_LVL);
            entity.setPlDownloadStatus(PL_DWNLD_STATUS);
            entity.setPlAdCompany(PL_AD_CMPNY);

            em.persist(entity);
            return entity;

        }
        catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

}