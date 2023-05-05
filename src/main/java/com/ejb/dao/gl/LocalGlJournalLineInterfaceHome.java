package com.ejb.dao.gl;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlJournalLineInterface;
import com.util.Debug;

@Stateless
public class LocalGlJournalLineInterfaceHome {

	public static final String JNDI_NAME = "LocalGlJournalLineInterfaceHome!com.ejb.gl.LocalGlJournalLineInterfaceHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlJournalLineInterfaceHome() {
	}

	// FINDER METHODS

	public LocalGlJournalLineInterface findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlJournalLineInterface entity = (LocalGlJournalLineInterface) em
					.find(new LocalGlJournalLineInterface(), pk);
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

	public java.util.Collection findJliByJliCoaAccountNumber(java.lang.String JLI_COA_ACCNT_NMBR,
			java.lang.Integer JLI_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jli) FROM GlJournalLineInterface jli WHERE jli.jliCoaAccountNumber = ?1 AND jli.jliAdCompany = ?2");
			query.setParameter(1, JLI_COA_ACCNT_NMBR);
			query.setParameter(2, JLI_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlJournalLineInterfaceHome.findJliByJliCoaAccountNumber(java.lang.String JLI_COA_ACCNT_NMBR, java.lang.Integer JLI_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGlJournalLineInterface create(java.lang.Integer JLI_CODE, short JLI_LN_NMBR, byte JLI_DBT,
                                              double JLI_AMNT, String JLI_COA_ACCNT_NMBR, Integer JLI_AD_CMPNY) throws CreateException {
		try {

			LocalGlJournalLineInterface entity = new LocalGlJournalLineInterface();

			Debug.print("glJournalLineInterfaceBean create");

			entity.setJliCode(JLI_CODE);
			entity.setJliLineNumber(JLI_LN_NMBR);
			entity.setJliDebit(JLI_DBT);
			entity.setJliAmount(JLI_AMNT);
			entity.setJliCoaAccountNumber(JLI_COA_ACCNT_NMBR);
			entity.setJliAdCompany(JLI_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlJournalLineInterface create(short JLI_LN_NMBR, byte JLI_DBT, double JLI_AMNT,
                                              String JLI_COA_ACCNT_NMBR, Integer JLI_AD_CMPNY) throws CreateException {
		try {

			LocalGlJournalLineInterface entity = new LocalGlJournalLineInterface();

			Debug.print("glJournalLineInterfaceBean create");

			entity.setJliLineNumber(JLI_LN_NMBR);
			entity.setJliDebit(JLI_DBT);
			entity.setJliAmount(JLI_AMNT);
			entity.setJliCoaAccountNumber(JLI_COA_ACCNT_NMBR);
			entity.setJliAdCompany(JLI_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}