package com.ejb.dao.gl;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlSuspenseAccount;
import com.util.Debug;

@Stateless
public class LocalGlSuspenseAccountHome {

	public static final String JNDI_NAME = "LocalGlSuspenseAccountHome!com.ejb.gl.LocalGlSuspenseAccountHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlSuspenseAccountHome() {
	}

	// FINDER METHODS

	public LocalGlSuspenseAccount findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlSuspenseAccount entity = (LocalGlSuspenseAccount) em
					.find(new LocalGlSuspenseAccount(), pk);
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

	public java.util.Collection findSaAll(java.lang.Integer SA_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(sa) FROM GlSuspenseAccount sa WHERE sa.saAdCompany = ?1");
			query.setParameter(1, SA_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlSuspenseAccountHome.findSaAll(java.lang.Integer SA_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlSuspenseAccount findBySaName(java.lang.String SA_NM, java.lang.Integer SA_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sa) FROM GlSuspenseAccount sa WHERE sa.saName=?1 AND sa.saAdCompany = ?2");
			query.setParameter(1, SA_NM);
			query.setParameter(2, SA_AD_CMPNY);
            return (LocalGlSuspenseAccount) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlSuspenseAccountHome.findBySaName(java.lang.String SA_NM, java.lang.Integer SA_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlSuspenseAccountHome.findBySaName(java.lang.String SA_NM, java.lang.Integer SA_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlSuspenseAccount findByJsNameAndJcName(java.lang.String JS_NM, java.lang.String JC_NM,
			java.lang.Integer SA_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sa) FROM GlSuspenseAccount sa WHERE sa.glJournalSource.jsName=?1 AND sa.glJournalCategory.jcName=?2 AND sa.saAdCompany = ?3");
			query.setParameter(1, JS_NM);
			query.setParameter(2, JC_NM);
			query.setParameter(3, SA_AD_CMPNY);
            return (LocalGlSuspenseAccount) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlSuspenseAccountHome.findByJsNameAndJcName(java.lang.String JS_NM, java.lang.String JC_NM, java.lang.Integer SA_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlSuspenseAccountHome.findByJsNameAndJcName(java.lang.String JS_NM, java.lang.String JC_NM, java.lang.Integer SA_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlSuspenseAccount findByJsNameAndJcName(java.lang.String JS_NM, java.lang.String JC_NM,
														java.lang.Integer SA_AD_CMPNY, String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(sa) FROM GlSuspenseAccount sa "
							+ "WHERE sa.glJournalSource.jsName=?1 AND sa.glJournalCategory.jcName=?2 AND sa.saAdCompany = ?3",
					companyShortName);
			query.setParameter(1, JS_NM);
			query.setParameter(2, JC_NM);
			query.setParameter(3, SA_AD_CMPNY);
			return (LocalGlSuspenseAccount) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public LocalGlSuspenseAccount create(java.lang.Integer SA_CODE, java.lang.String SA_NM,
                                         java.lang.String SA_DESC, java.lang.Integer SA_AD_CMPNY) throws CreateException {
		try {

			LocalGlSuspenseAccount entity = new LocalGlSuspenseAccount();

			Debug.print("GlSuspenseAccountBean create");

			entity.setSaCode(SA_CODE);
			entity.setSaName(SA_NM);
			entity.setSaDescription(SA_DESC);
			entity.setSaAdCompany(SA_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlSuspenseAccount create(java.lang.String SA_NM, java.lang.String SA_DESC,
                                         java.lang.Integer SA_AD_CMPNY) throws CreateException {

		try {

			LocalGlSuspenseAccount entity = new LocalGlSuspenseAccount();

			Debug.print("GlSuspenseAccount Bean create");

			entity.setSaName(SA_NM);
			entity.setSaDescription(SA_DESC);
			entity.setSaAdCompany(SA_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}