package com.ejb.dao.gl;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlJournalCategory;
import com.util.Debug;

@Stateless
public class LocalGlJournalCategoryHome {

	public static final String JNDI_NAME = "LocalGlJournalCategoryHome!com.ejb.gl.LocalGlJournalCategoryHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlJournalCategoryHome() {
	}

	// FINDER METHODS

	public LocalGlJournalCategory findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlJournalCategory entity = (LocalGlJournalCategory) em
					.find(new LocalGlJournalCategory(), pk);
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

	public java.util.Collection findJcAll(java.lang.Integer JC_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(jc) FROM GlJournalCategory jc WHERE jc.jcAdCompany = ?1");
			query.setParameter(1, JC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlJournalCategoryHome.findJcAll(java.lang.Integer JC_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlJournalCategory findByJcName(java.lang.String JC_NM, java.lang.Integer JC_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jc) FROM GlJournalCategory jc WHERE jc.jcName=?1 AND jc.jcAdCompany = ?2");
			query.setParameter(1, JC_NM);
			query.setParameter(2, JC_AD_CMPNY);
            return (LocalGlJournalCategory) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlJournalCategoryHome.findByJcName(java.lang.String JC_NM, java.lang.Integer JC_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlJournalCategoryHome.findByJcName(java.lang.String JC_NM, java.lang.Integer JC_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlJournalCategory findByJcName(java.lang.String JC_NM, java.lang.Integer JC_AD_CMPNY, String companyShortName)
			throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(jc) FROM GlJournalCategory jc WHERE jc.jcName=?1 AND jc.jcAdCompany = ?2",
					companyShortName);
			query.setParameter(1, JC_NM);
			query.setParameter(2, JC_AD_CMPNY);
			return (LocalGlJournalCategory) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public LocalGlJournalCategory create(java.lang.Integer JC_CODE, java.lang.String JC_NM,
                                         java.lang.String JC_DESC, char JC_RVRSL_MTHD, Integer JC_AD_CMPNY) throws CreateException {
		try {

			LocalGlJournalCategory entity = new LocalGlJournalCategory();

			Debug.print("GlJournalCategoryBean create");

			entity.setJcCode(JC_CODE);
			entity.setJcName(JC_NM);
			entity.setJcDescription(JC_DESC);
			entity.setJcReversalMethod(JC_RVRSL_MTHD);
			entity.setJcAdCompany(JC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlJournalCategory create(java.lang.String JC_NM, java.lang.String JC_DESC,
                                         char JC_RVRSL_MTHD, Integer JC_AD_CMPNY) throws CreateException {
		try {

			LocalGlJournalCategory entity = new LocalGlJournalCategory();

			Debug.print("GlJournalCategoryBean create");

			entity.setJcName(JC_NM);
			entity.setJcDescription(JC_DESC);
			entity.setJcReversalMethod(JC_RVRSL_MTHD);
			entity.setJcAdCompany(JC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}