package com.ejb.dao.gl;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlRecurringJournalLine;
import com.util.Debug;

@Stateless
public class LocalGlRecurringJournalLineHome {

	public static final String JNDI_NAME = "LocalGlRecurringJournalLineHome!com.ejb.gl.LocalGlRecurringJournalLineHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlRecurringJournalLineHome() {
	}

	// FINDER METHODS

	public LocalGlRecurringJournalLine findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlRecurringJournalLine entity = (LocalGlRecurringJournalLine) em
					.find(new LocalGlRecurringJournalLine(), pk);
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

	public java.util.Collection findRjlAll(java.lang.Integer RJL_AD_CMPNY) throws FinderException {

		try {
			Query query = em
					.createQuery("SELECT OBJECT(rjl) FROM GlRecurringJournalLine rjl WHERE rjl.rjlAdCompany = ?1");
			query.setParameter(1, RJL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlRecurringJournalLineHome.findRjlAll(java.lang.Integer RJL_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlRecurringJournalLine findByRjlLineNumber(short RJL_LN_NMBR, java.lang.Integer RJL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rjl) FROM GlRecurringJournalLine rjl WHERE rjl.rjlLineNumber=?1 AND rjl.rjlAdCompany = ?2");
			query.setParameter(1, RJL_LN_NMBR);
			query.setParameter(2, RJL_AD_CMPNY);
            return (LocalGlRecurringJournalLine) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlRecurringJournalLineHome.findByRjlLineNumber(short RJL_LN_NMBR, java.lang.Integer RJL_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlRecurringJournalLineHome.findByRjlLineNumber(short RJL_LN_NMBR, java.lang.Integer RJL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByRjCode(java.lang.Integer RJ_CODE, java.lang.Integer RJL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rjl) FROM GlRecurringJournal rj, IN(rj.glRecurringJournalLines) rjl WHERE rj.rjCode = ?1 AND rjl.rjlAdCompany = ?2");
			query.setParameter(1, RJ_CODE);
			query.setParameter(2, RJL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlRecurringJournalLineHome.findByRjCode(java.lang.Integer RJ_CODE, java.lang.Integer RJL_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGlRecurringJournalLine create(java.lang.Integer RJL_CODE, short RJL_LN_NMBR, byte RJL_DBT,
                                              double RJL_AMNT, Integer RJL_AD_CMPNY) throws CreateException {
		try {

			LocalGlRecurringJournalLine entity = new LocalGlRecurringJournalLine();

			Debug.print("GlRecurringJournalLineBean create");
			entity.setRjlCode(RJL_CODE);
			entity.setRjlLineNumber(RJL_LN_NMBR);
			entity.setRjlDebit(RJL_DBT);
			entity.setRjlAmount(RJL_AMNT);
			entity.setRjlAdCompany(RJL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlRecurringJournalLine create(short RJL_LN_NMBR, byte RJL_DBT, double RJL_AMNT,
                                              Integer RJL_AD_CMPNY) throws CreateException {
		try {

			LocalGlRecurringJournalLine entity = new LocalGlRecurringJournalLine();

			Debug.print("GlRecurringJournalLineBean create");

			entity.setRjlLineNumber(RJL_LN_NMBR);
			entity.setRjlDebit(RJL_DBT);
			entity.setRjlAmount(RJL_AMNT);
			entity.setRjlAdCompany(RJL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}