package com.ejb.dao.gl;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlBudgetAmountPeriod;
import com.util.Debug;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

@Stateless
public class LocalGlBudgetAmountPeriodHome {

    public static final String JNDI_NAME = "LocalGlBudgetAmountPeriodHome!com.ejb.gl.LocalGlBudgetAmountPeriodHome";

    @EJB
    public PersistenceBeanClass em;

    public LocalGlBudgetAmountPeriodHome() {

    }

    // FINDER METHODS

    public LocalGlBudgetAmountPeriod findByPrimaryKey(java.lang.Integer pk) throws FinderException {

        try {

            LocalGlBudgetAmountPeriod entity = (LocalGlBudgetAmountPeriod) em
                    .find(new LocalGlBudgetAmountPeriod(), pk);
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

    public LocalGlBudgetAmountPeriod findByBcCodeAndAcvCode(java.lang.Integer BC_CODE, java.lang.Integer ACV_CODE,
                                                            java.lang.Integer BAP_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(bap) FROM GlBudgetAmountPeriod bap WHERE bap.glBudgetAmountCoa.bcCode = ?1 AND bap.glAccountingCalendarValue.acvCode = ?2 AND bap.bapAdCompany = ?3");
            query.setParameter(1, BC_CODE);
            query.setParameter(2, ACV_CODE);
            query.setParameter(3, BAP_AD_CMPNY);
            return (LocalGlBudgetAmountPeriod) query.getSingleResult();
        }
        catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.gl.LocalGlBudgetAmountPeriodHome.findByBcCodeAndAcvCode(java.lang.Integer BC_CODE, java.lang.Integer ACV_CODE, java.lang.Integer BAP_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.gl.LocalGlBudgetAmountPeriodHome.findByBcCodeAndAcvCode(java.lang.Integer BC_CODE, java.lang.Integer ACV_CODE, java.lang.Integer BAP_AD_CMPNY)");
            throw ex;
        }
    }

    // OTHER METHODS

    // CREATE METHODS

    public LocalGlBudgetAmountPeriod create(java.lang.Integer BAP_CODE, double BAP_AMNT,
                                            Integer BAP_AD_CMPNY) throws CreateException {

        try {

            LocalGlBudgetAmountPeriod entity = new LocalGlBudgetAmountPeriod();

            Debug.print("GlBudgetAmountPeriod create");
            entity.setBapCode(BAP_CODE);
            entity.setBapAmount(BAP_AMNT);
            entity.setBapAdCompany(BAP_AD_CMPNY);

            em.persist(entity);
            return entity;

        }
        catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalGlBudgetAmountPeriod create(double BAP_AMNT, Integer BAP_AD_CMPNY) throws CreateException {

        try {

            LocalGlBudgetAmountPeriod entity = new LocalGlBudgetAmountPeriod();

            Debug.print("GlBudgetAmountPeriod create");
            entity.setBapAmount(BAP_AMNT);
            entity.setBapAdCompany(BAP_AD_CMPNY);

            em.persist(entity);
            return entity;

        }
        catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

}