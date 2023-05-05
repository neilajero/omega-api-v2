package com.ejb.dao.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArJobOrderType;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalArJobOrderTypeHome {

	public static final String JNDI_NAME = "LocalArJobOrderTypeHome!com.ejb.ar.LocalArJobOrderTypeHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalArJobOrderTypeHome() {
	}

	// FINDER METHODS

	public LocalArJobOrderType findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArJobOrderType entity = (LocalArJobOrderType) em
					.find(new LocalArJobOrderType(), pk);
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

	public java.util.Collection findEnabledJotAll(java.lang.Integer JOT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jot) FROM ArJobOrderType jot WHERE jot.jotEnable = 1 AND jot.jotAdCompany = ?1");
			query.setParameter(1, JOT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArJobOrderTypeHome.findEnabledJotAll(java.lang.Integer JOT_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArJobOrderType findByJotName(java.lang.String JOT_NM, java.lang.Integer JOT_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jot) FROM ArJobOrderType jot WHERE jot.jotName = ?1 AND jot.jotAdCompany = ?2");
			query.setParameter(1, JOT_NM);
			query.setParameter(2, JOT_AD_CMPNY);
            return (LocalArJobOrderType) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArJobOrderTypeHome.findByJotName(java.lang.String JOT_NM, java.lang.Integer JOT_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArJobOrderTypeHome.findByJotName(java.lang.String JOT_NM, java.lang.Integer JOT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findJotAll(java.lang.Integer JOT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(jot) FROM ArJobOrderType jot WHERE jot.jotAdCompany = ?1");
			query.setParameter(1, JOT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArJobOrderTypeHome.findJotAll(java.lang.Integer JOT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByJotGlCoaJobOrderAccount(java.lang.Integer COA_CODE,
			java.lang.Integer JOT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jot) FROM ArJobOrderType jot WHERE jot.jotGlCoaJobOrderAccount=?1 AND jot.jotAdCompany = ?2");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, JOT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArJobOrderTypeHome.findByJotGlCoaJobOrderAccount(java.lang.Integer COA_CODE, java.lang.Integer JOT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getJotByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
			throws FinderException {

		try {
			Query query = em.createQuery(jbossQl);
			int cnt = 1;
			for (Object data : args) {
				query.setParameter(cnt, data);
				cnt++;
			}
            return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalArJobOrderType create(Integer JOT_CODE, String JOT_NM, String JOT_DESC,
                                      Integer JOT_GL_COA_JB_ORDR_ACCNT, byte JOT_ENBL, Integer JOT_AD_CMPNY) throws CreateException {
		try {

			LocalArJobOrderType entity = new LocalArJobOrderType();

			Debug.print("ArJobOrderTypeBean create");

			entity.setJotCode(JOT_CODE);
			entity.setJotName(JOT_NM);
			entity.setJotDescription(JOT_DESC);
			entity.setJotGlCoaJobOrderAccount(JOT_GL_COA_JB_ORDR_ACCNT);
			entity.setJotEnable(JOT_ENBL);
			entity.setJotAdCompany(JOT_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArJobOrderType create(String JOT_NM, String JOT_DESC, Integer JOT_GL_COA_JB_ORDR_ACCNT,
                                      byte JOT_ENBL, Integer JOT_AD_CMPNY) throws CreateException {
		try {

			LocalArJobOrderType entity = new LocalArJobOrderType();

			Debug.print("ArJobOrderTypeBean create");

			entity.setJotName(JOT_NM);
			entity.setJotDescription(JOT_DESC);
			entity.setJotGlCoaJobOrderAccount(JOT_GL_COA_JB_ORDR_ACCNT);
			entity.setJotEnable(JOT_ENBL);
			entity.setJotAdCompany(JOT_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}