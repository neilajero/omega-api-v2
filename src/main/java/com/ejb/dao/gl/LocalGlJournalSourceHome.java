package com.ejb.dao.gl;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlJournalSource;
import com.util.Debug;

@Stateless
public class LocalGlJournalSourceHome {

	public static final String JNDI_NAME = "LocalGlJournalSourceHome!com.ejb.gl.LocalGlJournalSourceHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlJournalSourceHome() {
	}

	// FINDER METHODS

	public LocalGlJournalSource findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlJournalSource entity = (LocalGlJournalSource) em
					.find(new LocalGlJournalSource(), pk);
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

	public java.util.Collection findJsAll(java.lang.Integer JS_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(js) FROM GlJournalSource js WHERE js.jsAdCompany = ?1");
			query.setParameter(1, JS_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlJournalSourceHome.findJsAll(java.lang.Integer JS_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlJournalSource findByJsName(java.lang.String JS_NM, java.lang.Integer JS_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(js) FROM GlJournalSource js WHERE js.jsName=?1 AND js.jsAdCompany = ?2");
			query.setParameter(1, JS_NM);
			query.setParameter(2, JS_AD_CMPNY);
            return (LocalGlJournalSource) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlJournalSourceHome.findByJsName(java.lang.String JS_NM, java.lang.Integer JS_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlJournalSourceHome.findByJsName(java.lang.String JS_NM, java.lang.Integer JS_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlJournalSource findByJsName(java.lang.String JS_NM, java.lang.Integer JS_AD_CMPNY, String companyShortName)
			throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(js) FROM GlJournalSource js WHERE js.jsName=?1 AND js.jsAdCompany = ?2",
					companyShortName);
			query.setParameter(1, JS_NM);
			query.setParameter(2, JS_AD_CMPNY);
			return (LocalGlJournalSource) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public LocalGlJournalSource create(java.lang.Integer JS_CODE, java.lang.String JS_NM,
                                       java.lang.String JS_DESC, byte JS_FRZ_JRNL, byte JS_JRNL_APPRVL, char JS_EFFCTV_DT_RL, Integer JS_AD_CMPNY)
			throws CreateException {
		try {

			LocalGlJournalSource entity = new LocalGlJournalSource();

			Debug.print("GlJournalSourceBean create");

			entity.setJsCode(JS_CODE);
			entity.setJsName(JS_NM);
			entity.setJsDescription(JS_DESC);
			entity.setJsFreezeJournal(JS_FRZ_JRNL);
			entity.setJsJournalApproval(JS_JRNL_APPRVL);
			entity.setJsEffectiveDateRule(JS_EFFCTV_DT_RL);
			entity.setJsAdCompany(JS_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlJournalSource create(java.lang.String JS_NM, java.lang.String JS_DESC, byte JS_FRZ_JRNL,
                                       byte JS_JRNL_APPRVL, char JS_EFFCTV_DT_RL, Integer JS_AD_CMPNY) throws CreateException {
		try {

			LocalGlJournalSource entity = new LocalGlJournalSource();

			Debug.print("GlJournalSourceBean create");

			entity.setJsName(JS_NM);
			entity.setJsDescription(JS_DESC);
			entity.setJsFreezeJournal(JS_FRZ_JRNL);
			entity.setJsJournalApproval(JS_JRNL_APPRVL);
			entity.setJsEffectiveDateRule(JS_EFFCTV_DT_RL);
			entity.setJsAdCompany(JS_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}