package com.ejb.dao.gl;

import java.util.Date;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlRecurringJournal;
import com.util.Debug;

@Stateless
public class LocalGlRecurringJournalHome {

	public static final String JNDI_NAME = "LocalGlRecurringJournalHome!com.ejb.gl.LocalGlRecurringJournalHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlRecurringJournalHome() {
	}

	// FINDER METHODS

	public LocalGlRecurringJournal findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlRecurringJournal entity = (LocalGlRecurringJournal) em
					.find(new LocalGlRecurringJournal(), pk);
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

	public java.util.Collection findRjAll(java.lang.Integer RJ_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(rj) FROM GlRecurringJournal rj WHERE rj.rjAdCompany = ?1");
			query.setParameter(1, RJ_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlRecurringJournalHome.findRjAll(java.lang.Integer RJ_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlRecurringJournal findByRjName(java.lang.String RJ_NM, java.lang.Integer RJ_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rj) FROM GlRecurringJournal rj WHERE rj.rjName=?1 AND rj.rjAdCompany = ?2");
			query.setParameter(1, RJ_NM);
			query.setParameter(2, RJ_AD_CMPNY);
            return (LocalGlRecurringJournal) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlRecurringJournalHome.findByRjName(java.lang.String RJ_NM, java.lang.Integer RJ_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlRecurringJournalHome.findByRjName(java.lang.String RJ_NM, java.lang.Integer RJ_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findRjToGenerateByAdUsrNameAndDateAndBrCode(java.lang.String USR_NM, java.util.Date DT,
			java.lang.Integer RJ_AD_BRNCH, java.lang.Integer RJ_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rj) FROM GlRecurringJournal rj WHERE (rj.rjUserName1=?1 OR rj.rjUserName2=?1 OR rj.rjUserName3=?1 OR rj.rjUserName4=?1 OR rj.rjUserName5=?1) AND rj.rjNextRunDate <= ?2 AND rj.rjAdBranch = ?3 AND rj.rjAdCompany = ?4");
			query.setParameter(1, USR_NM);
			query.setParameter(2, DT);
			query.setParameter(3, RJ_AD_BRNCH);
			query.setParameter(4, RJ_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlRecurringJournalHome.findRjToGenerateByAdUsrNameAndDateAndBrCode(java.lang.String USR_NM, java.com.util.Date DT, java.lang.Integer RJ_AD_BRNCH, java.lang.Integer RJ_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByUserName1(java.lang.String USR_NM, java.lang.Integer RJ_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rj) FROM GlRecurringJournal rj WHERE rj.rjUserName1 = ?1 AND rj.rjAdCompany = ?2");
			query.setParameter(1, USR_NM);
			query.setParameter(2, RJ_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlRecurringJournalHome.findByUserName1(java.lang.String USR_NM, java.lang.Integer RJ_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByUserName2(java.lang.String USR_NM, java.lang.Integer RJ_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rj) FROM GlRecurringJournal rj WHERE rj.rjUserName2 = ?1 AND rj.rjAdCompany = ?2");
			query.setParameter(1, USR_NM);
			query.setParameter(2, RJ_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlRecurringJournalHome.findByUserName2(java.lang.String USR_NM, java.lang.Integer RJ_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByUserName3(java.lang.String USR_NM, java.lang.Integer RJ_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rj) FROM GlRecurringJournal rj WHERE rj.rjUserName3 = ?1 AND rj.rjAdCompany = ?2");
			query.setParameter(1, USR_NM);
			query.setParameter(2, RJ_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlRecurringJournalHome.findByUserName3(java.lang.String USR_NM, java.lang.Integer RJ_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByUserName4(java.lang.String USR_NM, java.lang.Integer RJ_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rj) FROM GlRecurringJournal rj WHERE rj.rjUserName4 = ?1 AND rj.rjAdCompany = ?2");
			query.setParameter(1, USR_NM);
			query.setParameter(2, RJ_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlRecurringJournalHome.findByUserName4(java.lang.String USR_NM, java.lang.Integer RJ_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByUserName5(java.lang.String USR_NM, java.lang.Integer RJ_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rj) FROM GlRecurringJournal rj WHERE rj.rjUserName5 = ?1 AND rj.rjAdCompany = ?2");
			query.setParameter(1, USR_NM);
			query.setParameter(2, RJ_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlRecurringJournalHome.findByUserName5(java.lang.String USR_NM, java.lang.Integer RJ_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByJbName(java.lang.String JB_NM, java.lang.Integer RJ_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rj) FROM GlRecurringJournal rj WHERE rj.glJournalBatch.jbName=?1 AND rj.rjAdCompany = ?2");
			query.setParameter(1, JB_NM);
			query.setParameter(2, RJ_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlRecurringJournalHome.findByJbName(java.lang.String JB_NM, java.lang.Integer RJ_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getRjByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
			java.lang.Integer LIMIT, java.lang.Integer OFFSET) throws FinderException {

		try {
			Query query = em.createQuery(jbossQl);
			int cnt = 1;
			for (Object data : args) {
				query.setParameter(cnt, data);
				cnt++;
			}
			if(LIMIT>0){query.setMaxResults(LIMIT);}
            query.setFirstResult(OFFSET);
            return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGlRecurringJournal create(java.lang.Integer RJ_CODE, java.lang.String RJ_NM,
                                          java.lang.String RJ_DESC, String RJ_USR_NM1, String RJ_USR_NM2, String RJ_USR_NM3, String RJ_USR_NM4,
                                          String RJ_USR_NM5, String RJ_SCHDL, Date RJ_NXT_RN_DT, Date RJ_LST_RN_DT, Integer RJ_AD_BRNCH,
                                          Integer RJ_AD_CMPNY) throws CreateException {
		try {

			LocalGlRecurringJournal entity = new LocalGlRecurringJournal();

			Debug.print("GlRecurringJournalBean create");

			entity.setRjCode(RJ_CODE);
			entity.setRjName(RJ_NM);
			entity.setRjDescription(RJ_DESC);
			entity.setRjUserName1(RJ_USR_NM1);
			entity.setRjUserName2(RJ_USR_NM2);
			entity.setRjUserName3(RJ_USR_NM3);
			entity.setRjUserName4(RJ_USR_NM4);
			entity.setRjUserName5(RJ_USR_NM5);
			entity.setRjSchedule(RJ_SCHDL);
			entity.setRjNextRunDate(RJ_NXT_RN_DT);
			entity.setRjLastRunDate(RJ_LST_RN_DT);
			entity.setRjAdBranch(RJ_AD_BRNCH);
			entity.setRjAdCompany(RJ_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlRecurringJournal create(java.lang.String RJ_NM, java.lang.String RJ_DESC,
                                          String RJ_USR_NM1, String RJ_USR_NM2, String RJ_USR_NM3, String RJ_USR_NM4, String RJ_USR_NM5,
                                          String RJ_SCHDL, Date RJ_NXT_RN_DT, Date RJ_LST_RN_DT, Integer RJ_AD_BRNCH, Integer RJ_AD_CMPNY)
			throws CreateException {
		try {

			LocalGlRecurringJournal entity = new LocalGlRecurringJournal();

			Debug.print("GlRecurringJournalBean create");

			entity.setRjName(RJ_NM);
			entity.setRjDescription(RJ_DESC);
			entity.setRjUserName1(RJ_USR_NM1);
			entity.setRjUserName2(RJ_USR_NM2);
			entity.setRjUserName3(RJ_USR_NM3);
			entity.setRjUserName4(RJ_USR_NM4);
			entity.setRjUserName5(RJ_USR_NM5);
			entity.setRjSchedule(RJ_SCHDL);
			entity.setRjNextRunDate(RJ_NXT_RN_DT);
			entity.setRjLastRunDate(RJ_LST_RN_DT);
			entity.setRjAdBranch(RJ_AD_BRNCH);
			entity.setRjAdCompany(RJ_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}